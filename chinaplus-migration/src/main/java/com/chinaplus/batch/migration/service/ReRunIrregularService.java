/**
 * CPIIFB02Service.java
 * 
 * @screen CPIIFB02
 * @author shi_xf
 */
package com.chinaplus.batch.migration.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.chinaplus.common.bean.ExceptionAttach;
import com.chinaplus.common.bean.IfIpEntity;
import com.chinaplus.common.bean.ReceiveData;
import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.ChinaPlusConst.BatchType;
import com.chinaplus.common.consts.CodeConst.ActionType;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.HandleFlag;
import com.chinaplus.common.consts.CodeConst.IFBatchStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceStatus;
import com.chinaplus.common.consts.CodeConst.IpKbDataType;
import com.chinaplus.common.consts.CodeConst.IpStatus;
import com.chinaplus.common.consts.CodeConst.OnHoldFlag;
import com.chinaplus.common.consts.CodeConst.PartsStatus;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataType;
import com.chinaplus.common.consts.CodeConst.TransportMode;
import com.chinaplus.common.entity.TnfImpStock;
import com.chinaplus.common.entity.TnfOrderStatus;
import com.chinaplus.common.entity.TnmExpPart;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TntIfBatch;
import com.chinaplus.common.entity.TntIfImpIp;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceHistory;
import com.chinaplus.common.entity.TntInvoicePart;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.common.entity.TntIp;
import com.chinaplus.common.entity.TntIpKanban;
import com.chinaplus.common.entity.TntTransferDelivery;
import com.chinaplus.common.entity.TntTransferOut;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Receive Data File from TT-LOGIX batch start base service.
 * 
 * @author shi_xf
 */
public class ReRunIrregularService extends BaseService {

    /** process type: VV */
    public static int PROCESS_TYPE_VV = SyncTimeDataType.TT_LOGIX_VV;

    /** process type: AISIN */
    public static int PROCESS_TYPE_AISIN = SyncTimeDataType.TT_LOGIX_AISIN;

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ReRunIrregularService.class);

    /** orderMonthMap */
    private Map<String, String> orderMonthMap = new HashMap<String, String>();

    /** decantOriginalMap */
    private Map<String, List<TntIpKanban>> decantOriginalMap = new HashMap<String, List<TntIpKanban>>();

    /** expPartsIdMap */
    private Map<String, TnmExpPart> expPartsIdMap = new HashMap<String, TnmExpPart>();

    /**
     * do process all IP information when irregular.
     *
     * @param officeId officeId
     * @param processType processType
     * @param handleFlag handleFlag
     */
    public void doReceiveIpForIrregular(Integer officeId, Integer processType, Integer handleFlag) {

        // get office id
        TnmOffice office = baseDao.findOne(TnmOffice.class, officeId);
        // get last end date
        Timestamp lastSucTime = this.getLastSuccessProcessTime(processType, office.getOfficeId());
        if (lastSucTime == null) {
            // no success batch ... continue
            return;
        }

        // get office date
        Timestamp currentDate = super.getDBDateTime(office.getTimeZone());
        // get update date
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();
        
        // update irregular information
        this.updateIrregularValidFlag(lastSucTime, officeId, processType, handleFlag, dbTime);

        // Set VALID_FLAG of imp IF table
        this.updateValidFlagForIrregular(lastSucTime, officeId, processType, handleFlag, dbTime);

        // Check Invoice is already POST GR/GI
        this.checkInvoicePostGRGI(lastSucTime, officeId, null, processType, dbTime);

        // Get pending process IF IP information from database
        List<TntIfImpIp> ifImpIpList = getIfImpIpList(lastSucTime, officeId, processType);
        if (ifImpIpList != null && !ifImpIpList.isEmpty()) {

            // do process IP information
            this.doProcessIPInfo(dbTime, ifImpIpList, null, processType);

            // Save imp inbound/outbound info into TNT_SYNC_TIME
            this.doUpdateInvoice(dbTime, processType);

            // do actual outbound
            this.doPrepareActualOutboundInfo(currentDate, office.getOfficeId(), dbTime, processType);
        }

        // do update
        this.updateHFForMainInfoNotMatch(lastSucTime, processType, null, dbTime);
    }

    /**
     * process exp parts when do register parts.
     *
     * @param expPartsIds expPartsIds
     */
    public void doReceiveIpForRegParts(List<Integer> expPartsIds) {

        // prepare by each office
        List<ReceiveData> expPartsInfoList = getExpPartsInfoByIds(expPartsIds);

        // split them by each office
        Map<Integer, List<Integer>> officePartsMap = new HashMap<Integer, List<Integer>>();
        for (ReceiveData expPartsInfo : expPartsInfoList) {
            Integer officeId = expPartsInfo.getOfficeId();
            List<Integer> idList = officePartsMap.get(officeId);
            if (idList == null) {
                idList = new ArrayList<Integer>();
            }
            // set
            idList.add(expPartsInfo.getExpPartsId());
            officePartsMap.put(officeId, idList);
        }

        // do by each office
        for (Integer officeId : officePartsMap.keySet()) {

            // get office id
            TnmOffice office = baseDao.findOne(TnmOffice.class, officeId);

            // get last end date
            Timestamp lastSucTime = this.getLastSuccessProcessTime(PROCESS_TYPE_VV, office.getOfficeId());
            if (lastSucTime == null) {
                // no success batch ... continue
                continue;
            }
            // get office date
            Timestamp currentDate = super.getDBDateTime(office.getTimeZone());
            // get update date
            Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();

            // EXP parts list
            List<Integer> offPartsList = officePartsMap.get(officeId);

            // Set VALID_FLAG of imp IF table
            int count = updateValidFlagForRegister(offPartsList, lastSucTime, dbTime);
            // if no data
            if (count == IntDef.INT_ZERO) {
                continue;
            }

            // Check Invoice is already POST GR/GI
            checkInvoicePostGRGI(lastSucTime, office.getOfficeId(), offPartsList, PROCESS_TYPE_VV, dbTime);

            // Get pending process IF IP information from database
            List<TntIfImpIp> ifImpIpList = getIfImpIpListForRegister(offPartsList, dbTime, lastSucTime);
            if (ifImpIpList != null && !ifImpIpList.isEmpty()) {
                // do process IP information
                this.doProcessIPInfo(dbTime, ifImpIpList, offPartsList, PROCESS_TYPE_VV);

                // Save imp inbound/outbound info into TNT_SYNC_TIME
                this.doUpdateInvoice(offPartsList, dbTime, PROCESS_TYPE_VV);

                // do actual outbound
                this.doPrepareActualOutboundInfo(currentDate, office.getOfficeId(), dbTime, PROCESS_TYPE_VV);
            }

            // do update
            this.updateHFForMainInfoNotMatch(lastSucTime, PROCESS_TYPE_VV, offPartsList, dbTime);
        }
    }

    /**
     * process all parts when do Invoice matched.
     *
     * @param matchInvIds invoices
     */
    public void doReceiveIpForMatchInvoice(List<Integer> matchInvIds) {

        // prepare by each office
        List<ReceiveData> matchInvInfoList = this.getMatchInvoiceInfoByIds(matchInvIds);

        // split them by each office
        Map<Integer, List<Integer>> matchInvMap = new HashMap<Integer, List<Integer>>();
        for (ReceiveData matchInvInfo : matchInvInfoList) {
            Integer officeId = matchInvInfo.getOfficeId();
            List<Integer> idList = matchInvMap.get(officeId);
            if (idList == null) {
                idList = new ArrayList<Integer>();
            }
            // set
            idList.add(matchInvInfo.getMatchInvId());
            matchInvMap.put(officeId, idList);
        }

        // do by each office
        for (Integer officeId : matchInvMap.keySet()) {

            // get office id
            TnmOffice office = baseDao.findOne(TnmOffice.class, officeId);

            // get last end date
            Timestamp lastSucTime = this.getLastSuccessProcessTime(PROCESS_TYPE_AISIN, office.getOfficeId());
            if (lastSucTime == null) {
                // no success batch ... continue
                continue;
            }
            // get office date
            Timestamp currentDate = super.getDBDateTime(office.getTimeZone());
            // get update date
            Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();

            // Match invoice id list
            List<Integer> matchInvIdList = matchInvMap.get(officeId);

            // Get pending process IF IP information from database
            List<TntIfImpIp> ifImpIpList = getIfImpIpListForMatchInvoice(matchInvIdList, lastSucTime);

            if (ifImpIpList != null && !ifImpIpList.isEmpty()) {
                // do process IP information
                this.doProcessIPInfo(dbTime, ifImpIpList, null, PROCESS_TYPE_AISIN);

                // Save imp inbound/outbound info into TNT_SYNC_TIME
                this.doUpdateInvoice(dbTime, PROCESS_TYPE_AISIN);

                // do actual outbound
                this.doPrepareActualOutboundInfo(currentDate, office.getOfficeId(), dbTime, PROCESS_TYPE_AISIN);
            }
        }
    }

    /**
     * Batch for ReceiveData.
     * 
     * @param officeId office ID
     * @param ifDateTime IF IP Date Time
     * @param processType process type
     * @param migrationFlag migrationFlag
     */
    public void doReceiveIpForBatch(Integer officeId, Timestamp ifDateTime, int processType, Integer migrationFlag) {

        // get update date
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();

        // check also has error data
        if (!this.checkExistErrorInfo(ifDateTime, officeId, processType)) {
            logger.error("There are some error Ip information in database.");
            // throw
            throw new BusinessException();
        }

        // Set VALID_FLAG of imp IF table
        int count = updateValidFlag(ifDateTime, officeId, processType, dbTime);
        logger.info("update, count:" + count);
        // if no data
        if (count == IntDef.INT_ZERO) {
            return;
        }

        // if data migration, no process
        if (migrationFlag == null) {

            // Get the effective parts data of IF file's data contents, and then react them to VALID_FLAG of TNM_IF_IMP_IP.
            checkInvoiceMissingMatched(ifDateTime, officeId, processType, dbTime);

            // Check Invoice is already POST GR/GI
            checkInvoicePostGRGI(ifDateTime, officeId, null, processType, dbTime);
        }

        // Get pending process IF IP information from database
        List<TntIfImpIp> ifImpIpList = getIfImpIpList(ifDateTime, officeId, processType);

        // do process IP information
        if (ifImpIpList != null && !ifImpIpList.isEmpty()) {

            // do process IP information
            this.doProcessIPInfo(dbTime, ifImpIpList, null, processType);

            // Save imp inbound/outbound info into TNT_SYNC_TIME
            this.doUpdateInvoice(dbTime, processType);
        }

        // do update
        this.updateHFForMainInfoNotMatch(ifDateTime, processType, null, dbTime);

        // update sync time
        // this.saveSyncTime(officeId, ifDateTime, dbTime, processType);
    }

    /**
     * check exist error information in current parts.
     *
     * @param ifDateTime ifDateTime
     * @param officeId officeId
     * @param processType processType
     * @return check result
     */
    private boolean checkExistErrorInfo(Timestamp ifDateTime, Integer officeId, int processType) {
        // prepare parameter
        ReceiveData param = new ReceiveData();
        param.setIfDateTime(ifDateTime);
        param.setOfficeId(officeId);
        if (PROCESS_TYPE_VV == processType) {
            param.setBusinessPattern(BusinessPattern.V_V);
        } else {
            param.setBusinessPattern(BusinessPattern.AISIN);
        }
        // get maxRundowId
        Integer errorCount = this.baseMapper.getSqlSession().selectOne(this.getSqlId("checkExistErrorInfoCnt"), param);
        if (errorCount != null && errorCount.intValue() > IntDef.INT_ZERO) {
            return false;
        }

        // get form database
        return true;
    }

    /**
     * Do process IP information.
     *
     * @param dbTime dbTime
     * @param ifImpIpList ifImpIpList
     * @param expPartsIds expPartsIds
     * @param processType processType
     */
    private void doProcessIPInfo(Timestamp dbTime, List<TntIfImpIp> ifImpIpList, List<Integer> expPartsIds,
        int processType) {

        // get time
        List<ExceptionAttach> checkErrorList = new ArrayList<ExceptionAttach>();
        Map<String, Integer> missPid = new HashMap<String, Integer>();
        if (ifImpIpList != null) {
            logger.info("Size of IF Imp Information List is :" + ifImpIpList.size());
            int count = 0;
            // Loop each IF Data, and do logic check for each parts
            for (TntIfImpIp ifImpInfo : ifImpIpList) {
                count++;
                // Get IF IP information from database
                IfIpEntity ifIpInfo = getIfIpEntity(ifImpInfo, processType);
                // CHECK IS MISSING INVOICE
                if (checkPIDMissInvoice(ifIpInfo, missPid, dbTime)) {
                    // next
                    continue;
                }
                // Do logic check
                boolean isCheckOK = this.doLogicCheck(ifIpInfo, checkErrorList, processType);
                if (isCheckOK) {
                    // Save imp ip information into TNT_IP, and prepare imp stock information and order status.
                    ifIpInfo.setExpPartsIds(expPartsIds);
                    ifIpInfo.setUpdatedDate(dbTime);
                    ifIpInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                    // set business Pattern -- add by YINCHUAN LIU for no warehouse customer 
                    if (processType == PROCESS_TYPE_VV) {
                        ifIpInfo.setBusinessPattern(BusinessPattern.V_V);
                    } else {
                        ifIpInfo.setBusinessPattern(BusinessPattern.AISIN);
                    }
                    saveEffectiveImpIpInfo(ifIpInfo, processType);
                    // if check error
                    if (ifIpInfo.getHandleFlag() != null
                            && ifIpInfo.getHandleFlag().intValue() > HandleFlag.PROCESS_SUCCESS) {
                        // make error entity
                        ExceptionAttach newAttach = new ExceptionAttach();
                        newAttach.setId(ifIpInfo.getIfIpId());
                        newAttach.setFlag(ifIpInfo.getHandleFlag());
                        checkErrorList.add(newAttach);
                    } else {
                        doUpdateHandleFlagByIFIPId(ifIpInfo.getIfIpId(), HandleFlag.PROCESS_SUCCESS, dbTime);
                    }
                }
                if (count % IntDef.INT_THOUSAND == IntDef.INT_ZERO) {
                    logger.info("Already process:" + count);
                }
            }
            logger.info("Already process:" + count);
        }

        // If any one of IP has been check NG or execute failure,
        if (checkErrorList.size() > IntDef.INT_ZERO) {
            // throw
            for (ExceptionAttach attach : checkErrorList) {
                // insert
                this.doUpdateHandleFlagByIFIPId(attach.getId(), attach.getFlag(), dbTime);
            }
        }
    }
    
    /**
     * do update invoice.
     *
     * @param dbTime dbTime
     * @param processType processType
     */ 
    private void doUpdateInvoice(Timestamp dbTime, int processType) {
        this.doUpdateInvoice(null, dbTime, processType);
    }

    /**
     * do update invoice.
     *
     * @param expPartsList expPartsList 
     * @param dbTime dbTime
     * @param processType processType
     */
    private void doUpdateInvoice(List<Integer> expPartsList, Timestamp dbTime, int processType) {
        logger.info("doUpdateInvoice start:");
        // pick up all actual In bound date from database
        List<ReceiveData> inbDateList = this.getInbDateList(dbTime, processType);
        Map<Integer, Map<String, IfIpEntity>> partialInboundInvoiceMap = null;
        if (inbDateList != null) {
            logger.info("inbDateList count: " + inbDateList.size());
            int count = 0;
            for (ReceiveData inbDateInfo : inbDateList) {
                count++;
                logger.info("process date: " + inbDateInfo.getInbDate() + "(" + count + ")");
                // define
                List<String> invoiceNoList = new ArrayList<String>();
                // Get imp in-bound information for invoice by each Inbound Date
                List<ReceiveData> inbInvoiceQtyList = this.getInbInvoiceQtyList(dbTime, inbDateInfo.getInbDate(),
                    processType);

                // if no data continue
                if (inbInvoiceQtyList.isEmpty()) {
                    continue;
                }

                // loop reset
                for (ReceiveData inbInvoiceQtyInfo : inbInvoiceQtyList) {
                    inbInvoiceQtyInfo.setUpdatedDate(dbTime);
                    updateInbInvoiceQty(inbInvoiceQtyInfo);
                    if (!invoiceNoList.contains(inbInvoiceQtyInfo.getInbInvoiceNo())) {
                        invoiceNoList.add(inbInvoiceQtyInfo.getInbInvoiceNo());
                    }
                }
                baseDao.flush();

                // Update Status of Invoice Summary
                if (invoiceNoList.size() > IntDef.INT_ZERO) {
                    // update
                    updateInvoiceSummaryStatus(invoiceNoList, dbTime);

                    // Do split partial in-bound Invoice
                    updateInvoiceIbActualDateForComp(invoiceNoList, inbDateInfo.getInbDate(), dbTime, processType);
                }

                // partial Inbound
                partialInboundInvoiceMap = getPartialInboundInvoice(expPartsList, dbTime, inbDateInfo.getInbDate(),
                    processType);
                updateAndSplitInvoice(partialInboundInvoiceMap, inbDateInfo.getInbDate(), processType);
            }
        }
    }

    /**
     * check PID_NO Missing Invoice.
     *
     * @param ifIpInfo ifIpInfo
     * @param missPid missing PID_NO
     * @param dbTime database time
     * @return true: missing invoice, false : not missing
     */
    private boolean checkPIDMissInvoice(IfIpEntity ifIpInfo, Map<String, Integer> missPid, Timestamp dbTime) {
        // define
        Integer handleFlag = null;
        // get actionType
        int actionType = Integer.parseInt(ifIpInfo.getActionType());
        // check
        if (ifIpInfo.getIpId() == null && actionType > ActionType.IMP_INBOUND) {
            // get PID_NO
            String pidNo = ifIpInfo.getFromIpNo() == null ? ifIpInfo.getPidNo() : ifIpInfo.getFromIpNo();
            if (StringUtil.isEmpty(pidNo)) {
                return false;
            }
            // check exist missPId or not
            if (missPid.containsKey(pidNo)) {
                // get handle flag
                handleFlag = missPid.get(pidNo);
            } else {
                // prepare parameter
                IfIpEntity param = new IfIpEntity();
                param.setPidNo(ifIpInfo.getFromIpNo() == null ? ifIpInfo.getPidNo() : ifIpInfo.getFromIpNo());
                // get form database
                List<IfIpEntity> existInfoList = this.selectPIDMissInvoice(param);
                // check
                if (existInfoList != null && !existInfoList.isEmpty()) {
                    // get handle flag
                    handleFlag = existInfoList.get(IntDef.INT_ZERO).getHandleFlag();
                }
            }
        }
        // check handle flag
        if (handleFlag != null) {
            // update handle flag as other
            doUpdateHandleFlagByIFIPId(ifIpInfo.getIfIpId(), handleFlag, dbTime);
            // check exist missPId or not
            if (!missPid.containsKey(ifIpInfo.getPidNo())) {
                missPid.put(ifIpInfo.getPidNo(), handleFlag);
            }
            // retrun error
            return true;
        }
        // return false
        return false;
    }

    /**
     * Get all un-process date time data from TNT_IF_IMP_IP.
     * 
     * @param officeId officeId
     * @param ifBatchInfo If batch information
     * @param processType Process Type
     * @return un-process date time data
     */
    public List<TntIfImpIp> getIfIpDateList(Integer officeId, TntIfBatch ifBatchInfo, int processType) {
        Timestamp ssmsProcessTime = null;
        Integer ssmsProcessStatus = null;
        if (PROCESS_TYPE_VV == processType) {
            // Get lastest success date for SSMS Data
            // Object[] param = new Object[] { BatchType.SSMS, IFBatchStatus.SUCCESS };
            Object[] param = new Object[] { BatchType.SSMS };
            // String hql = "FROM TNT_IF_BATCH A WHERE A.batchType = ? AND A.status = ? ORDER BY A.ifDateTime DESC";
            String hql = "FROM TNT_IF_BATCH A WHERE A.batchType = ? ORDER BY A.ifDateTime DESC";
            List<TntIfBatch> ifBatchList = baseDao.select(hql, param);
            if (ifBatchList != null && ifBatchList.size() > IntDef.INT_ZERO) {
                ssmsProcessTime = ifBatchList.get(IntDef.INT_ZERO).getIfDateTime();
                ssmsProcessStatus = ifBatchList.get(IntDef.INT_ZERO).getStatus();
            }

            if (ssmsProcessTime == null) {
                return null;
            }

            if (!ssmsProcessStatus.equals(IFBatchStatus.SUCCESS)) {
                ssmsProcessStatus = null;
            }
        }

        // Get all un-process date time data from TNT_IF_IMP_IP
        ReceiveData param = new ReceiveData();
        param.setBatchProcessTime(ifBatchInfo.getIfDateTime());
        param.setLastBatchStatus(ifBatchInfo.getStatus());
        param.setSsmsProcessTime(ssmsProcessTime);
        param.setSsmsProcessStatus(ssmsProcessStatus);
        param.setOfficeId(officeId);

        // get list
        return super.baseMapper.selectList(super.getSqlId("selectIfDataDateTime"), param);
    }

    /**
     * checkBatchLogStatus
     * 
     * @param processType Process Type
     * @param officeId office id
     * @return Batch Process Time
     */
    public TntIfBatch getBatchProcessTime(int processType, Integer officeId) {
        TntIfBatch result = null;
        Object[] param = new Object[] { processType, officeId };
        String hql = "FROM TNT_IF_BATCH A WHERE A.batchType = ? AND A.officeId = ? ORDER BY A.ifBatchId DESC";
        List<TntIfBatch> ifBatchList = baseDao.select(hql, param);
        if (ifBatchList != null && ifBatchList.size() > IntDef.INT_ZERO) {
            // TntIfBatch ifBatch = ;
            // if (ifBatch.getStatus() != null && IFBatchStatus.SUCCESS == ifBatch.getStatus().intValue()) {
            result = ifBatchList.get(IntDef.INT_ZERO);
            // }
        } else {
            Calendar date = Calendar.getInstance();
            date.set(IntDef.INT_THOUSAND_AND_NINE_HUNDRED, 0, 1);
            // result = new Timestamp(date.getTimeInMillis());
            result = new TntIfBatch();
            result.setIfDateTime(new Timestamp(date.getTimeInMillis()));
            result.setStatus(IFBatchStatus.SUCCESS);
        }
        return result;
    }

    /**
     * checkBatchLogStatus
     * 
     * @param processType Process Type
     * @param officeId office id
     * @return Batch Process Time
     */
    private Timestamp getLastSuccessProcessTime(int processType, Integer officeId) {
        Object[] param = new Object[] { processType, officeId };
        String hql = "FROM TNT_IF_BATCH A WHERE A.batchType = ? AND A.officeId = ? ORDER BY A.ifBatchId DESC";
        List<TntIfBatch> ifBatchList = baseDao.select(hql, param);
        if (ifBatchList != null && ifBatchList.size() > IntDef.INT_ZERO) {
            return ifBatchList.get(IntDef.INT_ZERO).getIfDateTime();
        } else {
            return null;
        }
    }

    /**
     * Get AISIN Exp parts ID by parts id and supplier id.
     * 
     * @param partsId partsId
     * @param supplierId supplierId
     * @return Exp parts Id
     */
    private Integer getAISINExpPartsId(Integer partsId, Integer supplierId) {

        // check exist?
        TnmExpPart expPartsInfo = getAISINExpPartsInfo(partsId, supplierId);
        if (expPartsInfo != null) {
            return expPartsInfo.getExpPartsId();
        }

        return null;
    }
    

    /**
     * Get AISIN Exp parts ID by parts id and supplier id.
     * 
     * @param partsId partsId
     * @param supplierId supplierId
     * @return Exp parts Id
     */
    private TnmExpPart getAISINExpPartsInfo(Integer partsId, Integer supplierId) {

        // export parts information
        TnmExpPart expPartsInfo = null;

        // prepare map key
        StringBuffer keySb = new StringBuffer();
        keySb.append(partsId);
        if (supplierId != null) {
            keySb.append(StringConst.UNDERLINE);
            keySb.append(supplierId);
        }

        // check exist?
        if (expPartsIdMap.containsKey(keySb.toString())) {

            // get from map
            expPartsInfo = expPartsIdMap.get(keySb.toString());
        } else {
            // get from database
            TnmExpPart param = new TnmExpPart();
            param.setPartsId(partsId);
            if (supplierId != null) {
                param.setSupplierId(supplierId);
            }
            param.setPartsStatus(PartsStatus.COMPLETED);

            // get
            List<TnmExpPart> expPartsList = baseDao.select(param);
            if (expPartsList != null && !expPartsList.isEmpty()) {
                expPartsInfo = expPartsList.get(IntDef.INT_ZERO);
            }

            // set into map
            expPartsIdMap.put(keySb.toString(), expPartsInfo);
        }

        // retrun
        return expPartsInfo;
    }

    /**
     * Prepare actual outbound information.
     * 
     * @param batchDate Batch Date
     * @param officeId Office ID
     * @param updatedDate updatedDate
     * @param processType Process Type
     */
    public void doPrepareActualOutboundInfo(Date batchDate, Integer officeId, Timestamp updatedDate, int processType) {
        List<TntIp> ipInfoList = getIpInfoList(batchDate, officeId, updatedDate, processType);

        if (ipInfoList != null && !ipInfoList.isEmpty()) {
            Timestamp dbTime = updatedDate != null ? updatedDate : getDBDateTimeByDefaultTimezone();
            Map<String, TnfImpStock> impStockMap = new HashMap<String, TnfImpStock>();
            Map<String, TnfOrderStatus> orderStatusMap = new HashMap<String, TnfOrderStatus>();
            logger.info("Start prepare ip");
            for (TntIp ipInfo : ipInfoList) {
                // Update Imp Stock Qty for TNT_IMP_STOCK
                prepareImpStock(ipInfo, dbTime, impStockMap);

                // if no KANBAN, no need do transfer update
                if (processType == PROCESS_TYPE_AISIN && ipInfo.getKanbanPlanNo() == null) {
                    continue;
                }

                // update order status
                if (ipInfo.getOriginalPartsId() == null) {
                    // Update Imp Stock Qty for TNF_ORDER_STATUS
                    prepareOrderStatus(ipInfo, dbTime, processType, orderStatusMap);
                } else {
                    // Update Qty for TNT_TRANSFER_DELIVERY
                    updateTransferDelivery(ipInfo, dbTime, processType);
                    // Update Outbound Qty for TNT_TRANSFER_OUT
                    updateTransferOut(ipInfo, dbTime, processType);
                }
            }
            logger.info("Start save imp stock");
            // save imp stock
            if (!impStockMap.isEmpty()) {
                for (TnfImpStock impStockInfo : impStockMap.values()) {
                    if (impStockInfo != null) {
                        super.baseDao.update(impStockInfo);
                    }
                }
            }
            logger.info("Start save order status");
            // save order status
            if (!orderStatusMap.isEmpty()) {
                for (TnfOrderStatus orderStatusInfo : orderStatusMap.values()) {
                    if (orderStatusInfo != null) {
                        super.baseDao.update(orderStatusInfo);
                    }
                }
            }

            // flush
            baseDao.flush();
            logger.info("Start update ip status");
            // Update all process IP status to 13: Imp Actual Outbound
            updateIpStatus(ipInfoList.get(IntDef.INT_ZERO).getOfficeId(), batchDate, updatedDate, dbTime, processType);
            logger.info("End update ip status");
        }
    }

    /**
     * update valid flag.
     *
     * @param ifDateTime ifDateTime
     * @param officeId officeId
     * @param processType processType
     * @param dbTime database Time
     * 
     * @return update count
     */
    private int updateValidFlag(Timestamp ifDateTime, Integer officeId, int processType, Timestamp dbTime) {
        ReceiveData param = new ReceiveData();
        param.setIfDateTime(ifDateTime);
        param.setOfficeId(officeId);
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setUpdatedDate(dbTime);
        if (PROCESS_TYPE_VV == processType) {
            param.setBusinessPattern(BusinessPattern.V_V);
        } else {
            param.setBusinessPattern(BusinessPattern.AISIN);
        }
        return baseMapper.update(this.getSqlId("updateValidFlag"), param);
    }

    /**
     * update valid flag for register parts.
     *
     * @param expPartsIds exp past id list
     * @param lastSucTime last batch success time
     * @param dbTime database time
     * @return UPDATE COUNT
     */
    private int updateValidFlagForRegister(List<Integer> expPartsIds, Timestamp lastSucTime, Timestamp dbTime) {
        ReceiveData param = new ReceiveData();
        param.setExpPartsIdList(expPartsIds);
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setIfDateTime(lastSucTime);
        param.setUpdatedDate(dbTime);
        return baseMapper.update(this.getSqlId("updateValidFlagForRegister"), param);
    }

    /**
     * update valid flag for register parts.
     *
     * @param lastSucTime lastSucTime
     * @param officeId officeId
     * @param processType processType
     * @param handleFlag handleFlag
     * @param dbTime database time
     * 
     * @return count
     */
    private int updateValidFlagForIrregular(Timestamp lastSucTime, Integer officeId, int processType,
        Integer handleFlag, Timestamp dbTime) {
        ReceiveData param = new ReceiveData();
        param.setIfDateTime(lastSucTime);
        param.setOfficeId(officeId);
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setUpdatedDate(dbTime);
        if (PROCESS_TYPE_VV == processType) {
            param.setBusinessPattern(BusinessPattern.V_V);
        } else {
            param.setBusinessPattern(BusinessPattern.AISIN);
        }
        // set handle Flag
        param.setHandleFlag(handleFlag);

        return baseMapper.update(this.getSqlId("updateValidFlagForIrregular"), param);
    }

    /**
     * update valid flag for register parts.
     *
     * @param lastSucTime lastSucTime
     * @param officeId officeId
     * @param processType processType
     * @param handleFlag handleFlag
     * @param dbTime database time
     * 
     * @return count
     */
    private int updateIrregularValidFlag(Timestamp lastSucTime, Integer officeId, int processType,
        Integer handleFlag, Timestamp dbTime) {
        ReceiveData param = new ReceiveData();
        param.setIfDateTime(lastSucTime);
        param.setOfficeId(officeId);
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setUpdatedDate(dbTime);
        if (PROCESS_TYPE_VV == processType) {
            param.setBusinessPattern(BusinessPattern.V_V);
        } else {
            param.setBusinessPattern(BusinessPattern.AISIN);
        }
        // set handle Flag
        param.setHandleFlag(handleFlag);

        return baseMapper.update(this.getSqlId("updateIrregularValidFlag"), param);
    }

    /**
     * Update all process IP status
     * 
     * @param officeId office if
     * @param batchDate IF IP Date Time
     * @param updatedDate updatedDate
     * @param dbTime dbTime
     * @param processType Process Type
     */
    private void updateIpStatus(Integer officeId, Date batchDate, Timestamp updatedDate, Timestamp dbTime,
        int processType) {
        ReceiveData param = new ReceiveData();
        param.setOfficeId(officeId);
        param.setProcessDate(new java.sql.Date(batchDate.getTime()));
        param.setCreatedDate(dbTime);
        param.setUpdatedDate(updatedDate);
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        if (PROCESS_TYPE_VV == processType) {
            param.setBusinessPattern(BusinessPattern.V_V);
        } else {
            param.setBusinessPattern(BusinessPattern.AISIN);
        }
        baseMapper.update(this.getSqlId("updateIpStatusToImpActualOutbound"), param);
    }

    /**
     * Update Outbound Qty for TNT_TRANSFER_OUT
     * 
     * @param ipInfo ipInfo
     * @param dbTime dbTime
     * @param processType processType
     */
    private void updateTransferOut(TntIp ipInfo, Timestamp dbTime, int processType) {
        TntTransferOut param = new TntTransferOut();
        if (PROCESS_TYPE_VV == processType) {
            param.setImpPoNo(ipInfo.getImpPoNo());
            param.setExpPoNo(ipInfo.getExpPoNo());
        } else {
            param.setKanbanPlanNo(ipInfo.getKanbanPlanNo());
        }
        param.setFromPartsId(ipInfo.getOriginalPartsId());
        param.setToPartsId(ipInfo.getPartsId());
        param.setSupplierId(ipInfo.getSupplierId());
        param.setTransferedDate(ipInfo.getImpStDate());

        TntTransferOut transferOutInfo = baseDao.findOne(param);
        if (transferOutInfo != null) {
            transferOutInfo.setOutboundQty(DecimalUtil.add(transferOutInfo.getOutboundQty(), ipInfo.getQty()));
            transferOutInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            transferOutInfo.setUpdatedDate(dbTime);
            transferOutInfo.setVersion(transferOutInfo.getVersion() + IntDef.INT_ONE);
            baseDao.update(transferOutInfo);
        }
    }

    /**
     * Update Qty for TNT_TRANSFER_DELIVERY
     * 
     * @param ipInfo ipInfo
     * @param dbTime dbTime
     * @param processType processType
     */
    private void updateTransferDelivery(TntIp ipInfo, Timestamp dbTime, int processType) {
        TntTransferDelivery param = new TntTransferDelivery();
        param.setPartsId(ipInfo.getPartsId());
        param.setSupplierId(ipInfo.getSupplierId());
        param.setDeliveredDate(ipInfo.getImpObActualDate());
        if (PROCESS_TYPE_VV == processType) {
            param.setImpPoNo(ipInfo.getImpPoNo());
            param.setExpPoNo(ipInfo.getExpPoNo());
        } else {
            param.setImpPoNo(ipInfo.getKanbanPlanNo());
        }

        TntTransferDelivery transferDeliveryInfo = baseDao.findOne(param);
        if (transferDeliveryInfo != null) {
            transferDeliveryInfo.setQty(DecimalUtil.add(transferDeliveryInfo.getQty(), ipInfo.getQty()));
            transferDeliveryInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            transferDeliveryInfo.setUpdatedDate(dbTime);
            transferDeliveryInfo.setVersion(transferDeliveryInfo.getVersion() + IntDef.INT_ONE);
            baseDao.update(transferDeliveryInfo);
        } else {
            transferDeliveryInfo = new TntTransferDelivery();
            if (PROCESS_TYPE_VV == processType) {
                transferDeliveryInfo.setImpPoNo(ipInfo.getImpPoNo());
                transferDeliveryInfo.setExpPoNo(ipInfo.getExpPoNo());
            } else {
                transferDeliveryInfo.setImpPoNo(ipInfo.getKanbanPlanNo());
            }
            transferDeliveryInfo.setCustomerId(ipInfo.getCustomerId());
            transferDeliveryInfo.setPartsId(ipInfo.getPartsId());
            transferDeliveryInfo.setOrderMonth(getOrderMonth(ipInfo.getImpPoNo(), ipInfo.getKanbanPlanNo()));
            transferDeliveryInfo.setSupplierId(ipInfo.getSupplierId());
            // transferDeliveryInfo.setDeliveredDate(ipInfo.getImpOutboundDatetime());
            transferDeliveryInfo.setDeliveredDate(ipInfo.getImpObActualDate());
            transferDeliveryInfo.setQty(ipInfo.getQty());
            transferDeliveryInfo.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
            transferDeliveryInfo.setCreatedDate(dbTime);
            transferDeliveryInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            transferDeliveryInfo.setUpdatedDate(dbTime);
            transferDeliveryInfo.setVersion(IntDef.INT_ONE);
            baseDao.insert(transferDeliveryInfo);
        }
    }

    /**
     * Update Imp Stock Qty for TNF_ORDER_STATUS
     * 
     * @param ipInfo ipInfo
     * @param dbTime dbTime
     * @param processType processType
     * @param orderStatusMap orderStatusMap
     */
    private void prepareOrderStatus(TntIp ipInfo, Timestamp dbTime, int processType,
        Map<String, TnfOrderStatus> orderStatusMap) {
        
        // prepare key
        StringBuffer statusKey = new StringBuffer();
        statusKey.append(ipInfo.getPartsId());
        statusKey.append(StringConst.NEW_COMMA);
        statusKey.append(ipInfo.getSupplierId());
        statusKey.append(StringConst.NEW_COMMA);
        if (PROCESS_TYPE_VV == processType) {
            statusKey.append(ipInfo.getImpPoNo());
            statusKey.append(StringConst.NEW_COMMA);
            statusKey.append(ipInfo.getExpPoNo());
        } else {
            statusKey.append(ipInfo.getKanbanPlanNo());
        }

        TnfOrderStatus orderStatusInfo = null;
        if (orderStatusMap.containsKey(statusKey.toString())) {
            // get from map
            orderStatusInfo = orderStatusMap.get(statusKey.toString());
        } else {

            // get from database
            TnfOrderStatus param = new TnfOrderStatus();
            param.setPartsId(ipInfo.getPartsId());
            param.setSupplierId(ipInfo.getSupplierId());
            if (PROCESS_TYPE_VV == processType) {
                param.setImpPoNo(ipInfo.getImpPoNo());
                param.setExpPoNo(ipInfo.getExpPoNo());
            } else {
                param.setKanbanPlanNo(ipInfo.getKanbanPlanNo());
            }

            // get from database
            orderStatusInfo = baseDao.findOne(param);
        }

        if (orderStatusInfo != null) {
            orderStatusInfo.setImpStockQty(DecimalUtil.subtract(orderStatusInfo.getImpStockQty(), ipInfo.getQty()));
            orderStatusInfo.setImpPrepareObQty(DecimalUtil.subtract(orderStatusInfo.getImpPrepareObQty(),
                ipInfo.getQty()));
            orderStatusInfo.setImpDeliveredQty(DecimalUtil.add(orderStatusInfo.getImpDeliveredQty(), ipInfo.getQty()));
            orderStatusInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            orderStatusInfo.setUpdatedDate(dbTime);
            orderStatusInfo.setVersion(orderStatusInfo.getVersion() + IntDef.INT_ONE);

            // baseDao.update(orderStatusInfo);
            orderStatusMap.put(statusKey.toString(), orderStatusInfo);
        }
    }

    /**
     * Update Imp Stock Qty for TNT_IMP_STOCK
     * 
     * @param ipInfo ipInfo
     * @param dbTime dbTime
     * @param impStockMap impStockMap
     */
    private void prepareImpStock(TntIp ipInfo, Timestamp dbTime, Map<String, TnfImpStock> impStockMap) {
        
        // prepare key
        StringBuffer stockKey = new StringBuffer();
        stockKey.append(ipInfo.getOfficeId());
        stockKey.append(StringConst.NEW_COMMA);
        stockKey.append(ipInfo.getWhsId());
        stockKey.append(StringConst.NEW_COMMA);
        stockKey.append(ipInfo.getPartsId());
        stockKey.append(StringConst.NEW_COMMA);
        stockKey.append(ipInfo.getSupplierId());
        
        TnfImpStock impStockInfo = null;
        if (impStockMap.containsKey(stockKey.toString())) {
            // get from map
            impStockInfo = impStockMap.get(stockKey.toString());
        } else {
            // get from database
            TnfImpStock param = new TnfImpStock();
            param.setOfficeId(ipInfo.getOfficeId());
            param.setWhsId(ipInfo.getWhsId());
            param.setPartsId(ipInfo.getPartsId());
            param.setSupplierId(ipInfo.getSupplierId());
            // get 
            impStockInfo = baseDao.findOne(param);
        }

        if (impStockInfo != null) {

            // reset
            impStockInfo.setImpStockQty(DecimalUtil.subtract(impStockInfo.getImpStockQty(), ipInfo.getQty()));
            impStockInfo.setDeliveredQty(DecimalUtil.add(impStockInfo.getDeliveredQty(), ipInfo.getQty()));
            impStockInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            impStockInfo.setUpdatedDate(dbTime);
            impStockInfo.setVersion(impStockInfo.getVersion() + IntDef.INT_ONE);

            // baseDao.update(impStockInfo);
            impStockMap.put(stockKey.toString(), impStockInfo);
        }
    }

    /**
     * Get all already outbound informaiton from TNT_IP
     * 
     * @param batchDate IF IP ID
     * @param officeId officeId
     * @param updatedDate updatedDate
     * @param processType Process Type
     * @return IF IP information
     */
    private List<TntIp> getIpInfoList(Date batchDate, Integer officeId, Timestamp updatedDate, int processType) {
        ReceiveData param = new ReceiveData();
        param.setProcessDate(new java.sql.Date(batchDate.getTime()));
        param.setUpdatedDate(updatedDate);
        param.setOfficeId(officeId);
        if (PROCESS_TYPE_VV == processType) {
            param.setBusinessPattern(BusinessPattern.V_V);
        } else {
            param.setBusinessPattern(BusinessPattern.AISIN);
        }
        return baseMapper.selectList(this.getSqlId("selectIpOutboundInfo"), param);
    }

    /**
     * Check Invoice Missing Matched.
     *
     * @param ifDateTime ifDateTime
     * @param officeId office Id
     * @param processType processType
     * @param dbTime dbTime
     */
    private void checkInvoiceMissingMatched(Timestamp ifDateTime, Integer officeId, int processType, Timestamp dbTime) {
        ReceiveData param = new ReceiveData();
        param.setIfDateTime(ifDateTime);
        param.setOfficeId(officeId);
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setUpdatedDate(dbTime);
        if (PROCESS_TYPE_VV == processType) {
            ; // delete by lyc, for v_v does not has nay match invoices cause if IP no is same use ssms
              // baseMapper.update(this.getSqlId("updateHandleFlagForVV"), param);
        } else {
            baseMapper.update(this.getSqlId("updateHandleFlagForAisin"), param);
        }

    }

    /**
     * Check Invoice Missing Matched.
     *
     * @param ifDateTime ifDateTime
     * @param processType processType
     * @param expPartsIds expPartsIds
     * @param dbTime dbTime
     */
    private void updateHFForMainInfoNotMatch(Timestamp ifDateTime, int processType, List<Integer> expPartsIds,
        Timestamp dbTime) {
        ReceiveData param = new ReceiveData();
        param.setHandleFlag(HandleFlag.MAIN_INFORMATION_DOESNOT_MATCHED);
        param.setExpPartsIdList(expPartsIds);
        param.setIfDateTime(ifDateTime);
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setUpdatedDate(dbTime);
        if (PROCESS_TYPE_VV == processType) {
            baseMapper.update(this.getSqlId("updateHandleFlagForMainInfoNotMatched"), param);
        }

    }

    /**
     * Check Invoice is already POST GR/GI
     *
     * @param ifDateTime ifDateTime
     * @param officeId officeId
     * @param expPartsIds exp Parts Id list
     * @param processType processType
     * @param dbTime dbTime
     */
    private void checkInvoicePostGRGI(Timestamp ifDateTime, Integer officeId, List<Integer> expPartsIds,
        int processType, Timestamp dbTime) {

        ReceiveData param = new ReceiveData();
        param.setIfDateTime(ifDateTime);
        param.setExpPartsIdList(expPartsIds);
        param.setOfficeId(officeId);
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setUpdatedDate(dbTime);
        if (PROCESS_TYPE_VV == processType) {
            param.setBusinessPattern(BusinessPattern.V_V);
        } else {
            param.setBusinessPattern(BusinessPattern.AISIN);
        }
        baseMapper.update(this.getSqlId("updateHandleFlagForPostGRGI"), param);
    }

    /**
     * Get pending process IF IP information from database
     * 
     * @param ifDateTime IF IP Date Time
     * @param officeId Office ID
     * @param processType Process Type
     * @return pending process IF IP information
     */
    private List<TntIfImpIp> getIfImpIpList(Timestamp ifDateTime, Integer officeId, int processType) {
        // Get pending process IF IP information from database
        ReceiveData param = new ReceiveData();
        param.setIfDateTime(ifDateTime);
        param.setOfficeId(officeId);
        List<TntIfImpIp> result = null;
        if (PROCESS_TYPE_VV == processType) {
            result = baseMapper.selectList(this.getSqlId("selectIfImpIpForVV"), param);
        } else {
            result = baseMapper.selectList(this.getSqlId("selectIfImpIpForAisin"), param);
        }
        return result;
    }

    /**
     * Get pending process IF IP information from database
     *
     * @param expPartsIds exp parts id list
     * @param dbTime dbTime
     * @param lastSucTime last batch success time
     * @return pending process IF IP information
     */
    private List<TntIfImpIp> getIfImpIpListForRegister(List<Integer> expPartsIds, Timestamp dbTime,
        Timestamp lastSucTime) {
        ReceiveData param = new ReceiveData();
        param.setExpPartsIdList(expPartsIds);
        param.setUpdatedDate(dbTime);
        param.setIfDateTime(lastSucTime);
        return baseMapper.selectList(this.getSqlId("selectIfImpIpForRegister"), param);
    }

    /**
     * Get pending process IF IP information from database
     *
     * @param matchInvList matchInvList
     * @param lastSucTime lastSucTime
     * @return pending process IF IP information
     */
    private List<TntIfImpIp> getIfImpIpListForMatchInvoice(List<Integer> matchInvList, Timestamp lastSucTime) {
        ReceiveData param = new ReceiveData();
        param.setMatchInvIdList(matchInvList);
        param.setIfDateTime(lastSucTime);
        return baseMapper.selectList(this.getSqlId("selectIfImpIpForMatchInvoice"), param);
    }

    /**
     * Get IF IP information from database
     * 
     * @param ifIpInfo IF IP information
     * @param processType Process Type
     * @return IF IP information
     */
    private IfIpEntity getIfIpEntity(TntIfImpIp ifIpInfo, int processType) {

        IfIpEntity param = new IfIpEntity();
        param.setIfIpId(ifIpInfo.getIfIpId());
        param.setActionType(ifIpInfo.getActionType());
        if (PROCESS_TYPE_VV == processType) {
            param.setBusinessPattern(BusinessPattern.V_V);
        } else {
            param.setBusinessPattern(BusinessPattern.AISIN);
        }
        // defined
        List<IfIpEntity> list = null;
        IfIpEntity result = null;
        Integer actionType = StringUtil.toInteger(ifIpInfo.getActionType());
        if (actionType.equals(ActionType.CUSTOMS_CLEARANCE)) {
            list = baseMapper.select(this.getSqlId("selectIfImpIpInfoByIdForCC"), param);
        } else if (actionType.equals(ActionType.DEVANNED)) {
            list = baseMapper.select(this.getSqlId("selectIfImpIpInfoByIdForDevan"), param);
        } else {
            list = baseMapper.select(this.getSqlId("selectIfImpIpInfoById"), param);
        }
        if (list != null && !list.isEmpty()) {
            result = list.get(IntDef.INT_ZERO);
        }
        // return
        return result;
    }

    /**
     * Do logic check.
     * 
     * @param ifIpInfo ifIpInfo
     * @param checkErrorList check error list
     * @param processType processType
     * @return true/false
     */
    private boolean doLogicCheck(IfIpEntity ifIpInfo, List<ExceptionAttach> checkErrorList, int processType) {

        // check handleflag
        Integer handleFlag = ifIpInfo.getHandleFlag();
        // if already do handle success, does not need do it again
        if (handleFlag != null && handleFlag.intValue() == HandleFlag.PROCESS_SUCCESS) {
            return true;
        }
        // reset
        handleFlag = null;
        BigDecimal ifQty = null;
        int actionType = Integer.parseInt(ifIpInfo.getActionType());
        // check action type
        Integer ipStatus = ifIpInfo.getDecantActionType();
        switch (actionType) {
            case ActionType.CUSTOMS_CLEARANCE:
                if (ifIpInfo.getInbInvoiceNo() != null) {
                    handleFlag = HandleFlag.ALREADY_IMP_INBOUND;
                    break;
                }
                if (ifIpInfo.getIfWhsId() == null) {
                    handleFlag = HandleFlag.WAREHOUSE_ISNOT_EXIST;
                    break;
                }
                break;
            case ActionType.DEVANNED:
                if (ifIpInfo.getInvDevanDate() != null || ifIpInfo.getInbModuleNo() != null) {
                    handleFlag = HandleFlag.ALREADY_IMP_INBOUND;
                    break;
                }
                if (ifIpInfo.getIfWhsId() == null) {
                    handleFlag = HandleFlag.WAREHOUSE_ISNOT_EXIST;
                    break;
                }
                if (ifIpInfo.getModuleNo() == null) {
                    handleFlag = HandleFlag.MAIN_INFORMATION_DOESNOT_MATCHED;
                    break;
                }
                break;
            case ActionType.IMP_INBOUND:
                if (ipStatus != null && ipStatus.intValue() >= IpStatus.IMP_INBOUND) {
                    handleFlag = HandleFlag.ALREADY_IMP_INBOUND;
                    break;
                }
                if (ifIpInfo.getIpPidNo() != null && !ifIpInfo.getPidNo().equals(ifIpInfo.getIpPidNo())) {
                    handleFlag = HandleFlag.MAIN_INFORMATION_DOESNOT_MATCHED;
                    break;
                }
                if (ifIpInfo.getIfWhsId() == null) {
                    handleFlag = HandleFlag.WAREHOUSE_ISNOT_EXIST;
                    break;
                }
                if (processType == PROCESS_TYPE_VV) {
                    // for part does not exist
                    if (ifIpInfo.getIpId() == null) {
                        handleFlag = HandleFlag.IP_DOES_NOT_EXIST_FOR_VV;
                        break;
                    }
                } else {
                    // for part does not exist
                    if (ifIpInfo.getPartsId() != null) {
                        handleFlag = HandleFlag.ALREADY_IMP_INBOUND;
                        break;
                    }
                }
                break;
            case ActionType.WHS_TRANSFER:
            case ActionType.STOCK_TRANSFER:
            case ActionType.STOCK_ADJUST:
            case ActionType.DECANT:
            case ActionType.NG:
            case ActionType.ECI_ONHOLD:
            case ActionType.RELEASE_ONHOLD:
            case ActionType.IMP_OUTBOUND:
                if (ifIpInfo.getIpId() == null) {
                    handleFlag = HandleFlag.IP_DOES_NOT_EXIST_FOR_VV;
                    break;
                }
                if (ifIpInfo.getIfWhsId() == null) {
                    handleFlag = HandleFlag.WAREHOUSE_ISNOT_EXIST;
                    break;
                }
                if (!ifIpInfo.getOfficeId().equals(ifIpInfo.getIfOfficeId())) {
                    // CHECK MAIN INFORMATION
                    handleFlag = HandleFlag.MAIN_INFORMATION_DOESNOT_MATCHED;
                    break;
                }
                if (ipStatus == IpStatus.CANCELLED) {
                    if (actionType != ActionType.DECANT) {
                        handleFlag = HandleFlag.LOGIC_CHECK_NG;
                        break;
                    }
                } else if (ipStatus < IpStatus.IMP_INBOUND || ipStatus >= IpStatus.IMP_OUTBOUND) {
                    handleFlag = HandleFlag.LOGIC_CHECK_NG;
                    break;
                }
                // check NG(if not normal IP, can only do release on hold)
                if (ifIpInfo.getIpOnholdFlag() != null && !ifIpInfo.getIpOnholdFlag().equals(OnHoldFlag.NORMAL)) {
                    if (actionType != ActionType.RELEASE_ONHOLD) {
                        handleFlag = HandleFlag.LOGIC_CHECK_NG;
                        break;
                    }
                } else {
                    if (actionType == ActionType.RELEASE_ONHOLD) {
                        handleFlag = HandleFlag.LOGIC_CHECK_NG;
                        break;
                    }
                }
                if (actionType != ActionType.WHS_TRANSFER) {
                    if (!ifIpInfo.getWhsId().equals(ifIpInfo.getIfWhsId())) {
                        // CHECK MAIN INFORMATION
                        handleFlag = HandleFlag.MAIN_INFORMATION_DOESNOT_MATCHED;
                        break;
                    }
                }
                if (actionType == ActionType.STOCK_TRANSFER) {
                    if (ifIpInfo.getIfPartsId() == null) {
                        handleFlag = HandleFlag.CUSTOMER_ISNOT_EXIST;
                        break;
                    }
                    if (ifIpInfo.getExpPartsId() != null && ifIpInfo.getIfExpPartsId() == null) {
                        handleFlag = HandleFlag.SUPPLIER_PARTS_ISNOT_EXIST;
                        break;
                    }
                } else {
                    // check customer id master same
                    if (processType == PROCESS_TYPE_AISIN) {
                        if (ifIpInfo.getIfCustomerId() == null
                                || !ifIpInfo.getIfCustomerId().equals(ifIpInfo.getCustomerId())) {
                            handleFlag = HandleFlag.MAIN_INFORMATION_DOESNOT_MATCHED;
                            break;
                        }
                    }
                }
                if (actionType == ActionType.STOCK_ADJUST) {
                    // check information
                    ifQty = StringUtil.toBigDecimal(ifIpInfo.getQty());
                    BigDecimal ifSaQty = StringUtil.toBigDecimal(ifIpInfo.getSaQty());
                    BigDecimal orgQty = DecimalUtil.subtract(ifQty, ifSaQty);
                    if (ifIpInfo.getOriginalQty() == null || !DecimalUtil.isEquals(ifIpInfo.getOriginalQty(), orgQty)) {
                        // CHECK MAIN INFORMATION
                        handleFlag = HandleFlag.QTY_NOT_MATCHED;
                        break;
                    }
                } else if (actionType == ActionType.IMP_OUTBOUND || actionType == ActionType.WHS_TRANSFER
                        || actionType == ActionType.STOCK_TRANSFER) {
                    
                    // check information
                    ifQty = StringUtil.toBigDecimal(ifIpInfo.getQty());
                    if (ifIpInfo.getOriginalQty() == null || !DecimalUtil.isEquals(ifIpInfo.getOriginalQty(), ifQty)) {
                        // CHECK MAIN INFORMATION
                        handleFlag = HandleFlag.QTY_NOT_MATCHED;
                        break;
                    }
                }
                break;
            case ActionType.CANCEL_INVOICE:
                if (processType == PROCESS_TYPE_VV) {
                    // if does not exist in database
                    if (ifIpInfo.getIpId() == null) {
                        // CHECK MAIN INFORMATION
                        handleFlag = HandleFlag.IP_DOES_NOT_EXIST_FOR_VV;
                        break;
                    } else if (ipStatus.intValue() > IpStatus.DEVANNED) {
                        // CHECK MAIN INFORMATION
                        handleFlag = HandleFlag.LOGIC_CHECK_NG;
                        break;
                    }
                }
                break;
        }

        if (handleFlag != null) {
            // make error entity
            ExceptionAttach newAttach = new ExceptionAttach();
            newAttach.setId(ifIpInfo.getIfIpId());
            newAttach.setFlag(handleFlag);
            checkErrorList.add(newAttach);
        }

        // reset
        ifIpInfo.setHandleFlag(null);

        // return
        return (handleFlag == null);
    }

    /**
     * Update Handle Flag.
     * 
     * @param IfIpId IF IP ID
     * @param handleFlag Handle Flag
     * @param dbTime dbTime
     */
    private void doUpdateHandleFlagByIFIPId(int IfIpId, int handleFlag, Timestamp dbTime) {
        ReceiveData param = new ReceiveData();
        param.setIfIpId(IfIpId);
        param.setHandleFlag(handleFlag);
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setUpdatedDate(dbTime);
        baseMapper.update(this.getSqlId("updateHandleFlagByIFIPId"), param);
    }

    /**
     * Save effective imp ip information into TNT_IP, and prepare imp stock information and order status.
     * 
     * @param ifIpInfo Effective Imp Ip Info
     * @param processType Process Type
     */
    private void saveEffectiveImpIpInfo(IfIpEntity ifIpInfo, int processType) {
        int actionType = Integer.parseInt(ifIpInfo.getActionType());
        if (ActionType.CUSTOMS_CLEARANCE == actionType) {
            // reset container no
            if (ifIpInfo.getTransportMode() != null && ifIpInfo.getTransportMode().equals(TransportMode.AIR)) {
                ifIpInfo.setContainerNo(StringConst.EMPTY);
            }
            // If Action Type of current IP is 1(Customs Clearance), need update
            this.updateImpCcActualDateIntoDb(ifIpInfo, processType);
            // do prepare imp stock and order status
            if (processType == PROCESS_TYPE_VV) {
                // update imp stock
                this.prepareImpStockForInvoiceVV(ifIpInfo);
                // update order status
                this.prepareOrderStatusForInvoiceVV(ifIpInfo);
            } else {
                // do split module for KANBAN
                this.splitContainerForKanban(ifIpInfo);
                // update imp stock and order status
                this.prepareStockAndStatusForContAISIN(ifIpInfo);
            }
        } else if (ActionType.DEVANNED == actionType) {
            // update module information
            updateDevannedDateIntoDb(ifIpInfo, processType);
            if (processType == PROCESS_TYPE_VV) {
                // update imp stock
                this.prepareImpStockForModuleVV(ifIpInfo);
                // update order status
                this.prepareOrderStatusForModuleVV(ifIpInfo);
            } else {
                // do split module for KANBAN
                this.splitModuleForKanban(ifIpInfo);
                // update imp stock and order status
                this.prepareStockAndStatusForModAISIN(ifIpInfo);
            }
        } else if (ActionType.DECANT == actionType) {
            // cancel from IP and then insert new IP
            saveIpInfo(ifIpInfo, processType);
            if (ifIpInfo.getHandleFlag() != null && ifIpInfo.getHandleFlag().intValue() > HandleFlag.PROCESS_SUCCESS) {
                return;
            }
            if (processType == PROCESS_TYPE_AISIN) {
                this.splitIPForKanban(ifIpInfo);
            }
        } else if (ActionType.CANCEL_INVOICE == actionType) {
            // only update imp stock when do after devan
            if (ifIpInfo.getInvDevanDate() != null) {
                // if AISIN
                if (processType == PROCESS_TYPE_AISIN) {
                    // do save TNT_IP_KANBAN for AISIN
                    this.splitIPForKanban(ifIpInfo);
                    // if has error
                    if (ifIpInfo.getIpKanbanList() != null) {
                        // check split information
                        for (TntIpKanban ipKanban : ifIpInfo.getIpKanbanList()) {
                            // reset values of supplier
                            ifIpInfo.setSupplierId(ipKanban.getSupplierId());
                            ifIpInfo.setKanbanPlanNo(ipKanban.getKanbanPlanNo());
                            ifIpInfo.setQty(StringUtil.toDecimalString(ipKanban.getQty()));
                            // do save other information
                            this.saveOtherInfoForIfIp(ifIpInfo, processType);
                        }
                    }
                } else {
                    // update
                    this.updateIpInfo(ifIpInfo);
                    // update imp stock
                    this.saveOtherInfoForIfIp(ifIpInfo, processType);
                }
            }
        } else {
            // is VV or AISIN
            if (processType == PROCESS_TYPE_VV) {
                // Update IF IP information into TNT_IP
                this.updateIpInfo(ifIpInfo);
                // do save
                this.saveOtherInfoForIfIp(ifIpInfo, processType);
            } else {
                // do save TNT_IP_KANBAN for AISIN
                this.splitIPForKanban(ifIpInfo);
                // if has error
                if (ifIpInfo.getIpKanbanList() != null) {
                    // Update IF IP information into TNT_IP
                    this.updateIpInfo(ifIpInfo);
                    // check split information
                    for (TntIpKanban ipKanban : ifIpInfo.getIpKanbanList()) {
                        // reset values of supplier
                        ifIpInfo.setSupplierId(ipKanban.getSupplierId());
                        ifIpInfo.setKanbanPlanNo(ipKanban.getKanbanPlanNo());
                        ifIpInfo.setQty(StringUtil.toDecimalString(ipKanban.getQty()));
                        ifIpInfo.setSaQty(StringUtil.toDecimalString(ipKanban.getSaQty()));
                        // do save other information
                        this.saveOtherInfoForIfIp(ifIpInfo, processType);
                    }
                }
            }
        }
        // flush
        baseDao.flush();
        baseDao.clear();
    }

    /**
     * do save other information for each IF_IP.
     * 
     * @param ifIpInfo ifIpInfo
     * @param processType processType
     */
    private void saveOtherInfoForIfIp(IfIpEntity ifIpInfo, int processType) {

        // get action type
        int actionType = Integer.parseInt(ifIpInfo.getActionType());

        // Save all kinds of QTY in TNT_IMP_STOCK
        saveImpStock(ifIpInfo);

        // If Action Type of current IP is 4(WHS Transfer) or 5(Stock Transfer), we need reduce
        if (ActionType.WHS_TRANSFER == actionType || ActionType.STOCK_TRANSFER == actionType) {
            // update old imp stock
            updateOldImpStock(ifIpInfo);
        }

        // if no KANBAN, no need do transfer update
        if (processType == PROCESS_TYPE_AISIN && ifIpInfo.getKanbanPlanNo() == null) {
            return;
        }

        // update order status if current PID does not has any stock transfer
        if (ifIpInfo.getDecantOriginalPartsId() == null) {
            // If Action Type of current IP is 5(Stock Transfer), we need update TRANSFER_OUT_QTY
            if (ActionType.STOCK_TRANSFER == actionType) {
                // If Action Type of current IP is 5(Stock Transfer), we need update TRANSFER_OUT_QTY
                updateOldTnfOrderStatus(ifIpInfo, processType);
            } else if (ActionType.WHS_TRANSFER != actionType && ActionType.DECANT != actionType) {
                // Save all kinds of QTY in TNF_ORDER_STATUS
                updateTnfOrderStatus(ifIpInfo, processType);
            }
        }

        // If Action Type of current IP is 5(Stock Transfer), record current date into TNT_TRANSFER_OUT
        if (ActionType.STOCK_TRANSFER == actionType) {
            // If IP already has been stock transfered({Effective IP}.ORIGINAL_PARTS_ID is not null),
            // we need update transfer information before
            if (ifIpInfo.getDecantOriginalPartsId() != null) {
                updateOriginalTransferOut(ifIpInfo, processType);
            }
            // save transfer information
            saveTransferOut(ifIpInfo, processType);
        }
    }

    /**
     * split all parts under module to each kanban.
     * 
     * @param ifIpInfo interface inner packing information
     */
    private void splitModuleForKanban(IfIpEntity ifIpInfo) {
        // Get all parts QTY for current module.
        List<ReceiveData> modPartsList = this.getModPartsInfoList(ifIpInfo.getModuleNo(), ifIpInfo.getInvoiceNo());
        // Get all parts QTY for current invoice with each KANBAN plan no.
        // if no DEVANNED date
        List<TntIpKanban> ipKanbanList = null;
        if (ifIpInfo.getCcDate() == null) {
            // Get all parts QTY for current invoice with each KANBAN plan no.
            List<ReceiveData> invPartsList = this.getInvPartsInfoList(null, ifIpInfo.getInvoiceNo(),
                IpKbDataType.DEVANNED);
            // loop each parts and do allocate
            int invIdx = IntDef.INT_ZERO;
            ipKanbanList = new ArrayList<TntIpKanban>();
            for (ReceiveData modParts : modPartsList) {
                // set if informations
                modParts.setIfIpId(ifIpInfo.getIfIpId());
                modParts.setInvoiceNo(ifIpInfo.getInvoiceNo());
                modParts.setModuleNo(ifIpInfo.getModuleNo());
                // defined
                List<ReceiveData> kanbanList = new ArrayList<ReceiveData>();
                // pick up all exp parts from invoice parts list
                for (; invIdx < invPartsList.size(); invIdx++) {
                    // get list
                    ReceiveData invParts = invPartsList.get(invIdx);
                    int comp = invParts.getPartsId().compareTo(modParts.getPartsId());
                    // check
                    if (comp < 0) {
                        continue;
                    } else if (comp > 0) {
                        break;
                    }
                    // set into list
                    kanbanList.add(invParts);
                }
                // do allocate
                List<TntIpKanban> splitList = this.allocateKanbanForParts(modParts, kanbanList, IpKbDataType.DEVANNED);
                if (splitList != null) {
                    ipKanbanList.addAll(splitList);
                }
            }
        } else {
            // get inbound module information
            List<TntIpKanban> conKanbanList = this.getAllocateListForModule(ifIpInfo.getContainerNo(),
                ifIpInfo.getInvoiceNo());
            // loop each parts and do allocate
            int invIdx = IntDef.INT_ZERO;
            ipKanbanList = new ArrayList<TntIpKanban>();
            for (ReceiveData modParts : modPartsList) {
                // set if informations
                modParts.setIfIpId(ifIpInfo.getIfIpId());
                modParts.setInvoiceNo(ifIpInfo.getInvoiceNo());
                // ONLY HAS CUSTOM MODULE NEED SAVE CONTAINER NO
                modParts.setContainerNo(ifIpInfo.getContainerNo());
                modParts.setModuleNo(ifIpInfo.getModuleNo());
                // defined
                List<TntIpKanban> kanbanList = new ArrayList<TntIpKanban>();
                // pick up all exp parts from invoice parts list
                for (; invIdx < conKanbanList.size(); invIdx++) {
                    // get list
                    TntIpKanban invParts = conKanbanList.get(invIdx);
                    int comp = invParts.getPartsId().compareTo(modParts.getPartsId());
                    // check
                    if (comp < 0) {
                        continue;
                    } else if (comp > 0) {
                        break;
                    }
                    // set into list
                    kanbanList.add(invParts);
                }
                // do allocate
                List<TntIpKanban> splitList = this.rePrepareAllocateKanbanMod(modParts, kanbanList);
                if (splitList != null) {
                    ipKanbanList.addAll(splitList);
                }
            }
        }
        
        // save
        this.saveIpKanbanList(ipKanbanList, ifIpInfo.getUpdatedDate());
        // reset
        ifIpInfo.setIpKanbanList(ipKanbanList);
    }

    /**
     * split all parts under module to each kanban.
     * 
     * @param ifIpInfo interface inner packing information
     */
    private void splitContainerForKanban(IfIpEntity ifIpInfo) {
        // Get all parts QTY for current module.
        List<ReceiveData> conPartsList = this.getConPartsInfoList(ifIpInfo.getContainerNo(), ifIpInfo.getInvoiceNo());
        // Get all parts QTY for current invoice with each KANBAN plan no.
        List<ReceiveData> invPartsList = this.getInvPartsInfoList(null, ifIpInfo.getInvoiceNo(),
            IpKbDataType.CUSTOMS_CLEARANCE);
        // loop each parts and do allocate
        int invIdx = IntDef.INT_ZERO;
        List<TntIpKanban> ipKanbanList = new ArrayList<TntIpKanban>();
        for (ReceiveData conParts : conPartsList) {
            // set if informations
            conParts.setIfIpId(ifIpInfo.getIfIpId());
            conParts.setInvoiceNo(ifIpInfo.getInvoiceNo());
            conParts.setContainerNo(ifIpInfo.getContainerNo());
            // defined
            List<ReceiveData> kanbanList = new ArrayList<ReceiveData>();
            // pick up all exp parts from invoice parts list
            for (; invIdx < invPartsList.size(); invIdx++) {
                // get list
                ReceiveData invParts = invPartsList.get(invIdx);
                int comp = invParts.getPartsId().compareTo(conParts.getPartsId());
                // check
                if (comp < 0) {
                    continue;
                } else if (comp > 0) {
                    break;
                }
                // set into list
                kanbanList.add(invParts);
            }
            // do allocate
            List<TntIpKanban> splitList = this.allocateKanbanForParts(conParts, kanbanList,
                IpKbDataType.CUSTOMS_CLEARANCE);
            if (splitList != null) {
                ipKanbanList.addAll(splitList);
            }
        }
        // save
        this.saveIpKanbanList(ipKanbanList, ifIpInfo.getUpdatedDate());
        // reset
        ifIpInfo.setIpKanbanList(ipKanbanList);
    }

    /**
     * save ip kanban list.
     * 
     * @param ipKanbanList ip kanban list
     * @param dataTime data date time
     */
    private void saveIpKanbanList(List<TntIpKanban> ipKanbanList, Timestamp dataTime) {
        // save into database
        for (TntIpKanban ipKanban : ipKanbanList) {
            // set information
            ipKanban.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
            ipKanban.setCreatedDate(dataTime);
            ipKanban.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            ipKanban.setUpdatedDate(dataTime);
            ipKanban.setVersion(IntDef.INT_ONE);
            // save
            baseDao.insert(ipKanban);
        }
    }

    /**
     * Allocate KANBAN information for parts and save into database.
     * 
     * @param ifIpInfo ifIpInfo
     * @param expPartsInfo expPartsInfo
     * @param dataType data type
     * 
     * @return return result of allocate
     */
    private List<TntIpKanban> allocateKanbanForParts(IfIpEntity ifIpInfo, TnmExpPart expPartsInfo, Integer dataType) {
        // defined
        List<TntIpKanban> ipKanbanList = new ArrayList<TntIpKanban>();
        TntIpKanban ipKanban = new TntIpKanban();

        // set values
        ipKanban.setIfIpId(ifIpInfo.getIfIpId());
        ipKanban.setPidNo(ifIpInfo.getPidNo());
        ipKanban.setDataType(dataType);
        ipKanban.setKanbanPlanNo(null);
        ipKanban.setInvoiceNo(ifIpInfo.getInbInvoiceNo());
        ipKanban.setModuleNo(ifIpInfo.getInbModuleNo());
        ipKanban.setPartsId(ifIpInfo.getPartsId());
        ipKanban.setSupplierId(expPartsInfo.getSupplierId());
        ipKanban.setExpPartsId(expPartsInfo.getExpPartsId());
        ipKanban.setQty(StringUtil.toBigDecimal(ifIpInfo.getQty()));
        ipKanban.setSaQty(BigDecimal.ZERO);

        // set into list
        ipKanbanList.add(ipKanban);

        // deduct
        return ipKanbanList;
    }

    /**
     * Allocate KANBAN information for parts and save into database.
     * 
     * @param partsInfo parts information
     * @param kanbanList KANBAN list
     * @param dataType data type
     * 
     * @return return result of allocate
     */
    private List<TntIpKanban> allocateKanbanForParts(BaseEntity partsInfo, List<ReceiveData> kanbanList,
        Integer dataType) {
        // defined
        List<TntIpKanban> ipKanbanList = new ArrayList<TntIpKanban>();
        TntIpKanban ipKanban = null;
        Integer partsId = null;
        Integer ifIpId = null;
        String pidNo = null;
        String invoice = null;
        String containerNo = null;
        String moduleNo = null;
        BigDecimal allocateQty = null;
        if (partsInfo instanceof ReceiveData) {
            ReceiveData baseParts = (ReceiveData) partsInfo;
            partsId = baseParts.getPartsId();
            ifIpId = baseParts.getIfIpId();
            invoice = baseParts.getInvoiceNo();
            allocateQty = baseParts.getSumQty();
            moduleNo = baseParts.getModuleNo();
            containerNo = baseParts.getContainerNo();
        } else {
            IfIpEntity baseParts = (IfIpEntity) partsInfo;
            partsId = baseParts.getPartsId();
            if (partsId == null) {
                partsId = baseParts.getIfPartsId();
            }
            ifIpId = baseParts.getIfIpId();
            pidNo = baseParts.getPidNo();
            invoice = baseParts.getInvoiceNo();
            allocateQty = StringUtil.toBigDecimal(baseParts.getQty());
            // if DEVANNED, set module no
            if (baseParts.getInvDevanDate() != null) {
                moduleNo = baseParts.getModuleNo();
            }
        }
        // prepare TntIpKanban
        for (ReceiveData kanban : kanbanList) {
            // get need qty
            BigDecimal needQty = DecimalUtil.subtract(kanban.getInvoiceQty(), kanban.getSplitQty());
            // prepare ipKanban
            ipKanban = new TntIpKanban();
            ipKanban.setIfIpId(ifIpId);
            ipKanban.setPidNo(pidNo);
            ipKanban.setDataType(dataType);
            ipKanban.setInvoiceNo(invoice);
            ipKanban.setKanbanPlanNo(kanban.getKanbanPlanNo());
            ipKanban.setPartsId(kanban.getPartsId());
            ipKanban.setSupplierId(kanban.getSupplierId());
            ipKanban.setExpPartsId(kanban.getExpPartsId());
            ipKanban.setQty(allocateQty.min(needQty));
            ipKanban.setSaQty(BigDecimal.ZERO);
            ipKanban.setContainerNo(containerNo);
            ipKanban.setModuleNo(moduleNo);
            ipKanbanList.add(ipKanban);
            // deduct
            allocateQty = DecimalUtil.subtract(allocateQty, needQty);
            // check qty
            if (DecimalUtil.isLessEquals(allocateQty, BigDecimal.ZERO)) {
                break;
            }
        }
        // set into List
        if (DecimalUtil.isGreater(allocateQty, BigDecimal.ZERO)) {
            // define
            TnmExpPart expPartsInfo = this.getAISINExpPartsInfo(partsId, null);
            // add new KANBAN
            ipKanban = new TntIpKanban();
            // set values
            ipKanban.setIfIpId(ifIpId);
            ipKanban.setPidNo(pidNo);
            ipKanban.setDataType(dataType);
            ipKanban.setKanbanPlanNo(null);
            ipKanban.setInvoiceNo(invoice);
            ipKanban.setContainerNo(containerNo);
            ipKanban.setModuleNo(moduleNo);
            ipKanban.setPartsId(partsId);
            ipKanban.setSupplierId(expPartsInfo.getSupplierId());
            ipKanban.setExpPartsId(expPartsInfo.getExpPartsId());
            ipKanban.setQty(allocateQty);
            ipKanban.setSaQty(BigDecimal.ZERO);
            // set into list
            ipKanbanList.add(ipKanban);
        }

        // return list
        return ipKanbanList;
    }

    /**
     * Get all parts qty for current module.
     * 
     * @param moduleNo module No.
     * @param invoiceNo invoice No.
     * @return parts list
     */
    private List<ReceiveData> getModPartsInfoList(String moduleNo, String invoiceNo) {
        // Get pending process IF IP information from database
        ReceiveData param = new ReceiveData();
        param.setModuleNo(moduleNo);
        param.setInvoiceNo(invoiceNo);
        // get list
        return baseMapper.selectList(this.getSqlId("selectModulePartsInfo"), param);
    }

    /**
     * Get all parts qty for current module.
     * 
     * @param containerNo container No.
     * @param invoiceNo invoice No.
     * @return parts list
     */
    private List<ReceiveData> getConPartsInfoList(String containerNo, String invoiceNo) {
        // Get pending process IF IP information from database
        ReceiveData param = new ReceiveData();
        param.setContainerNo(containerNo);
        param.setInvoiceNo(invoiceNo);
        // get list
        return baseMapper.selectList(this.getSqlId("selectContainerPartsInfo"), param);
    }

    /**
     * split all parts under IP to each kanban.
     * 
     * @param ifIpInfo interface inner packing information
     */
    private void splitIPForKanban(IfIpEntity ifIpInfo) {
        // Get all parts QTY for current module.
        int actionType = Integer.parseInt(ifIpInfo.getActionType());
        List<TntIpKanban> ipKanbanList = null;
        // if is in-bound, will do allocation
        if (actionType == ActionType.IMP_INBOUND) {
            // if does not has any invoice, will do
            if (ifIpInfo.getActualInvoiceNo() != null) {
                // if no devanned date
                if (ifIpInfo.getInvDevanDate() == null) {
                    // Get all parts QTY for current invoice with each KANBAN plan no.
                    List<ReceiveData> partSupList = this.getInvPartsInfoList(ifIpInfo.getIfPartsId(),
                        ifIpInfo.getInvoiceNo(), IpKbDataType.INBOUND);
                    // do allocate
                    ipKanbanList = this.allocateKanbanForParts(ifIpInfo, partSupList, IpKbDataType.INBOUND);
                } else {
                    // get inbound module information
                    ipKanbanList = this.getAllocatedKanbanList(null, ifIpInfo);
                    // do re-prepare allocate kankan list.
                    ipKanbanList = this.rePrepareAllocateKanban(ifIpInfo, ipKanbanList);
                }
            } else {
                // set values
                ifIpInfo.setPartsId(ifIpInfo.getIfPartsId());
                // get first supplier and then put all parts after it
                TnmExpPart expPartsInfo = this.getAISINExpPartsInfo(ifIpInfo.getPartsId(), null);
                // prepare new parts
                ipKanbanList = this.allocateKanbanForParts(ifIpInfo, expPartsInfo, IpKbDataType.INBOUND);
            }
        } else if (actionType == ActionType.CANCEL_INVOICE) {
            // if does not has any invoice, will do
            // get inbound module information
            ipKanbanList = this.getAllocatedKanbanList(null, ifIpInfo);
            // do re-prepare allocate kankan list.
            ipKanbanList = this.rePrepareAllocateKanban(ifIpInfo, ipKanbanList);
        } else {
            // get PID no
            String pidNo = ifIpInfo.getFromIpNo();
            if (StringUtil.isEmpty(pidNo)) {
                pidNo = ifIpInfo.getPidNo();
            }
            // if other, get last one from database
            if (actionType == ActionType.DECANT) {
                if (decantOriginalMap.containsKey(pidNo)) {
                    ipKanbanList = decantOriginalMap.get(pidNo);
                } else {
                    ipKanbanList = this.getAllocatedKanbanList(pidNo, null);
                    decantOriginalMap.put(pidNo, ipKanbanList);
                }
            } else {
                ipKanbanList = this.getAllocatedKanbanList(pidNo, null);
            }
            // do re-prepare allocate kankan list.
            ipKanbanList = this.rePrepareAllocateKanban(ifIpInfo, ipKanbanList);
        }
        // check
        if (ipKanbanList != null) {
            // save
            this.saveIpKanbanList(ipKanbanList, ifIpInfo.getUpdatedDate());
            // reset
            ifIpInfo.setIpKanbanList(ipKanbanList);
        }
    }

    /**
     * do re-prepare allocate KANKAN list.
     * 
     * @param ifIpInfo if IP information
     * @param ipKanbanList IP KANBAN list
     * 
     * @return prepared IP KANBAN list
     */
    private List<TntIpKanban> rePrepareAllocateKanbanMod(ReceiveData ifIpInfo, List<TntIpKanban> ipKanbanList) {
        // defined
        List<TntIpKanban> newIpKanbanList = new ArrayList<TntIpKanban>();
        TntIpKanban newIpKanban = null;
        BigDecimal allocateQty = ifIpInfo.getSumQty();
        // loop
        for (TntIpKanban ipKanban : ipKanbanList) {
            // set if ip id
            ipKanban.setIfIpId(ifIpInfo.getIfIpId());
            // check
            if (DecimalUtil.isZero(allocateQty)) {
                break;
            }
            // prepare new object
            newIpKanban = new TntIpKanban();
            BeanUtils.copyProperties(ipKanban, newIpKanban);
            newIpKanban.setPidNo(null);
            newIpKanban.setModuleNo(ifIpInfo.getModuleNo());
            newIpKanban.setDataType(IpKbDataType.DEVANNED);
            // do split? check QTY
            if (DecimalUtil.isGreaterEquals(ipKanban.getQty(), allocateQty)) {
                newIpKanban.setQty(allocateQty);
                ipKanban.setQty(DecimalUtil.subtract(ipKanban.getQty(), allocateQty));
                allocateQty = BigDecimal.ZERO;
            } else {
                newIpKanban.setQty(ipKanban.getQty());
                ipKanban.setQty(BigDecimal.ZERO);
                allocateQty = DecimalUtil.subtract(allocateQty, ipKanban.getQty());
            }
            newIpKanbanList.add(newIpKanban);
        }
        // if no new IP KANBAN list
        return newIpKanbanList;
    }

    /**
     * do re-prepare allocate KANKAN list.
     * 
     * @param ifIpInfo if IP information
     * @param ipKanbanList IP KANBAN list
     * 
     * @return prepared IP KANBAN list
     */
    private List<TntIpKanban> rePrepareAllocateKanban(IfIpEntity ifIpInfo, List<TntIpKanban> ipKanbanList) {
        // defined
        List<TntIpKanban> newIpKanbanList = new ArrayList<TntIpKanban>();
        TntIpKanban newIpKanban = null;
        // get action type
        int actionType = Integer.parseInt(ifIpInfo.getActionType());
        BigDecimal allocateQty = null;
        // only stock adjust and imp inbound need do allocate
        if (actionType == ActionType.STOCK_ADJUST || actionType == ActionType.IMP_INBOUND
                || actionType == ActionType.CANCEL_INVOICE) {
            // set qty
            if (StringUtil.isEmpty(ifIpInfo.getSaQty())) {
                allocateQty = StringUtil.toBigDecimal(ifIpInfo.getQty());
            } else {
                allocateQty = StringUtil.toBigDecimal(ifIpInfo.getSaQty());
            }
        }
        // loop
        for (TntIpKanban ipKanban : ipKanbanList) {
            // set if ip id
            ipKanban.setIfIpId(ifIpInfo.getIfIpId());
            // if action type
            switch (actionType) {
                case ActionType.WHS_TRANSFER:
                    ipKanban.setDataType(IpKbDataType.WHS_TRANSFER);
                    break;
                case ActionType.STOCK_TRANSFER:
                    ipKanban.setDataType(IpKbDataType.STOCK_TRANSFER);
                    ipKanban.setPartsId(ifIpInfo.getIfPartsId());
                    // ipKanban.setExpPartsId(ifIpInfo.getIfExpPartsId());
                    ipKanban.setExpPartsId(getAISINExpPartsId(ipKanban.getPartsId(), ipKanban.getSupplierId()));
                    break;
                case ActionType.NG:
                    ipKanban.setDataType(IpKbDataType.NG);
                    break;
                case ActionType.ECI_ONHOLD:
                    ipKanban.setDataType(IpKbDataType.ECI_ONHOLD);
                    break;
                case ActionType.RELEASE_ONHOLD:
                    ipKanban.setDataType(IpKbDataType.RELEASE_ONHOLD);
                    break;
                case ActionType.IMP_OUTBOUND:
                    ipKanban.setDataType(IpKbDataType.OUTBOUND);
                    break;
                case ActionType.STOCK_ADJUST:
                    ipKanban.setDataType(IpKbDataType.STOCK_ADJUST);
                    if (DecimalUtil.isGreaterEquals(allocateQty, BigDecimal.ZERO)
                            || DecimalUtil.isLess(allocateQty.abs(), ipKanban.getQty())) {
                        ipKanban.setQty(DecimalUtil.add(ipKanban.getQty(), allocateQty));
                        ipKanban.setSaQty(allocateQty);
                        allocateQty = BigDecimal.ZERO;
                    } else {
                        allocateQty = DecimalUtil.add(ipKanban.getQty(), allocateQty);
                        ipKanban.setSaQty(ipKanban.getQty().negate());
                        ipKanban.setQty(BigDecimal.ZERO);
                    }
                    break;
                case ActionType.DECANT:
                case ActionType.IMP_INBOUND:
                case ActionType.CANCEL_INVOICE:
                    // check
                    if (DecimalUtil.isZero(allocateQty)) {
                        break;
                    }
                    // prepare new object
                    newIpKanban = new TntIpKanban();
                    BeanUtils.copyProperties(ipKanban, newIpKanban);
                    newIpKanban.setPidNo(ifIpInfo.getPidNo());
                    if (ActionType.DECANT == actionType) {
                        newIpKanban.setDataType(IpKbDataType.DECANT);
                    } else if (ActionType.IMP_INBOUND == actionType) {
                        newIpKanban.setDataType(IpKbDataType.INBOUND);
                    } else {
                        newIpKanban.setDataType(IpKbDataType.CANCELLED);
                    }
                    // do split? check QTY
                    if (DecimalUtil.isGreaterEquals(ipKanban.getQty(), allocateQty)) {
                        newIpKanban.setQty(allocateQty);
                        ipKanban.setQty(DecimalUtil.subtract(ipKanban.getQty(), allocateQty));
                        allocateQty = BigDecimal.ZERO;
                    } else {
                        newIpKanban.setQty(ipKanban.getQty());
                        ipKanban.setQty(BigDecimal.ZERO);
                        allocateQty = DecimalUtil.subtract(allocateQty, ipKanban.getQty());
                    }
                    newIpKanbanList.add(newIpKanban);
                    break;
            }
        }

        // only for inbound
        if (ActionType.IMP_INBOUND == actionType && DecimalUtil.isGreater(allocateQty, BigDecimal.ZERO)) {
            // get first supplier and then put all parts after it
            TnmExpPart expPartsInfo = this.getAISINExpPartsInfo(ifIpInfo.getPartsId(), null);
            // add new kanban
            TntIpKanban ipKanban = new TntIpKanban();
            // set values
            ipKanban.setIfIpId(ifIpInfo.getIfIpId());
            ipKanban.setPidNo(ifIpInfo.getPidNo());
            ipKanban.setDataType(IpKbDataType.INBOUND);
            ipKanban.setKanbanPlanNo(null);
            ipKanban.setInvoiceNo(ifIpInfo.getInbInvoiceNo());
            ipKanban.setModuleNo(ifIpInfo.getInbModuleNo());
            ipKanban.setPartsId(ifIpInfo.getPartsId());
            ipKanban.setSupplierId(expPartsInfo.getSupplierId());
            ipKanban.setExpPartsId(expPartsInfo.getExpPartsId());
            ipKanban.setQty(allocateQty);
            ipKanban.setSaQty(BigDecimal.ZERO);
            // set into list
            ipKanbanList.add(ipKanban);
        }

        // if no new IP KANBAN list
        if (!newIpKanbanList.isEmpty()) {
            return newIpKanbanList;
        } else {
            // return new one
            return ipKanbanList;
        }
    }

    /**
     * Pick up KANBAN_PLAN_NOs of IP.Invoice which does not has been allocated complete.
     * 
     * @param partsId Parts Id
     * @param invoiceNo invoice No
     * @param dataType dataType
     * @return kanban plan data
     */
    private List<ReceiveData> getInvPartsInfoList(Integer partsId, String invoiceNo, Integer dataType) {
        // prepare parameter
        ReceiveData param = new ReceiveData();
        param.setPartsId(partsId);
        param.setInvoiceNo(invoiceNo);
        param.setDataType(dataType);
        // get list
        return baseMapper.select(this.getSqlId("selectKanbanPlanData"), param);
    }

    /**
     * save ip information.
     * 
     * @param ifIpInfo ifIpInfo
     * @param processType processType
     */
    private void saveIpInfo(IfIpEntity ifIpInfo, int processType) {

        // exist data
        if (StringUtil.isEmpty(ifIpInfo.getFromIpNo()) || ifIpInfo.getFromIpNo().equals(ifIpInfo.getPidNo())) {

            // get pid
            TntIp param = new TntIp();
            param.setPidNo(ifIpInfo.getPidNo());

            // update current date
            TntIp oldIpInfo = baseDao.findOne(param);
            // if exist
            if (oldIpInfo != null) {
                // get residue qty
                BigDecimal qty = StringUtil.toBigDecimal(ifIpInfo.getQty());
                // set values
                if (qty == null) {
                    // update
                    qty = BigDecimal.ZERO;
                }
                // check qty
                if (qty.compareTo(BigDecimal.ZERO) <= IntDef.INT_ZERO) {
                    // set as cancel
                    oldIpInfo.setStatus(IpStatus.CANCELLED);
                }
                oldIpInfo.setQty(qty);
                oldIpInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                oldIpInfo.setUpdatedDate(ifIpInfo.getUpdatedDate());
                oldIpInfo.setVersion(oldIpInfo.getVersion() + IntDef.INT_ONE);
                // update
                baseDao.update(oldIpInfo);
            }
        } else {

            // do check PID is exist or not
            TntIp param = new TntIp();
            param.setPidNo(ifIpInfo.getPidNo());

            // get
            List<TntIp> existIp = baseDao.select(param);
            if (existIp != null && !existIp.isEmpty()) {
                ifIpInfo.setHandleFlag(HandleFlag.PID_NO_IS_ARLREADY_EXIST);
                return;
            }

            // add new IP
            TntIp newIpInfo = new TntIp();
            // reset prepare information
            this.prepareIpInformation(newIpInfo, ifIpInfo);
            // insert
            baseDao.insert(newIpInfo);
        }
    }

    /**
     * update invoice.
     * 
     * @param ifInfo ifIpInfo
     * @param processType processType
     */
    private void updateImpCcActualDateIntoDb(IfIpEntity ifInfo, int processType) {

        // prepare parameter
        ReceiveData param = new ReceiveData();
        param.setInvoiceNo(ifInfo.getInvoiceNo());
        param.setWhsId(ifInfo.getIfWhsId());
        param.setContainerNo(ifInfo.getContainerNo());
        param.setActualCcDate(DateTimeUtil.parseDate(ifInfo.getCustomsClrDate(), DateTimeUtil.FORMAT_IP_DATE));
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setUpdatedDate(ifInfo.getUpdatedDate());
        // update invoice
        baseMapper.update(this.getSqlId("updateImpCcActualDate"), param);
        // update invoice container
        baseMapper.update(this.getSqlId("updateImpCcDateForContainer"), param);
        if (processType == PROCESS_TYPE_VV) {
            // update tnt_ip
            baseMapper.update(this.getSqlId("updateImpCcActualDateForIp"), param);
        }
    }

    /**
     * update TNT_INVOICE_CONTAINER.
     * 
     * @param ifIpInfo ifIpInfo
     * @param processType processType
     */
    private void updateDevannedDateIntoDb(IfIpEntity ifIpInfo, int processType) {

        // prepare parameter
        ReceiveData param = new ReceiveData();
        param.setModuleNo(ifIpInfo.getModuleNo());
        param.setInvoiceNo(ifIpInfo.getInvoiceNo());
        param.setWhsId(ifIpInfo.getIfWhsId());
        param.setDevannedDate(DateTimeUtil.parseDate(ifIpInfo.getDevannedDate(), DateTimeUtil.FORMAT_IP_DATE));
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setUpdatedDate(ifIpInfo.getUpdatedDate());
        // update into invoice container
        baseMapper.update(this.getSqlId("updateDevanDateForContainer"), param);
        if (processType == PROCESS_TYPE_VV) {
            // update tnt_ip
            baseMapper.update(this.getSqlId("updateDevannedDateForIp"), param);
        }
    }

    /**
     * save transfer out.
     * 
     * @param ifIpInfo ifIpInfo
     * @param processType processType
     */
    private void saveTransferOut(IfIpEntity ifIpInfo, int processType) {

        // prepare from parts id
        Integer fromPartsId = ifIpInfo.getDecantOriginalPartsId();
        if (fromPartsId == null) {
            fromPartsId = ifIpInfo.getPartsId();
        }
        // imp stock date
        Date impStDate = new Date(ifIpInfo.getProcessDate().getTime());

        // prepare parameter
        TntTransferOut param = new TntTransferOut();
        param.setFromPartsId(fromPartsId);
        param.setToPartsId(ifIpInfo.getIfPartsId());
        param.setSupplierId(ifIpInfo.getSupplierId());
        param.setTransferedDate(impStDate);
        if (PROCESS_TYPE_VV == processType) {
            param.setImpPoNo(ifIpInfo.getDecantImpPoNo());
            param.setExpPoNo(ifIpInfo.getDecantExpPoNo());
        } else {
            param.setKanbanPlanNo(ifIpInfo.getKanbanPlanNo());
        }

        // get entity
        TntTransferOut transferOutInfo = baseDao.findOne(param);
        BigDecimal qty = StringUtil.toBigDecimal(ifIpInfo.getQty());

        // check
        if (transferOutInfo != null) {
            // if exist
            transferOutInfo.setQty(DecimalUtil.add(transferOutInfo.getQty(), qty));
            transferOutInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            transferOutInfo.setUpdatedDate(ifIpInfo.getUpdatedDate());
            transferOutInfo.setVersion(transferOutInfo.getVersion() + IntDef.INT_ONE);
            baseDao.update(transferOutInfo);
        } else {

            // if not exist
            transferOutInfo = new TntTransferOut();
            if (PROCESS_TYPE_VV == processType) {
                transferOutInfo.setImpPoNo(ifIpInfo.getDecantImpPoNo());
                transferOutInfo.setCustomerOrderNo(ifIpInfo.getDecantCustomerOrderNo());
                transferOutInfo.setExpPoNo(ifIpInfo.getDecantExpPoNo());
            } else {
                transferOutInfo.setKanbanPlanNo(ifIpInfo.getKanbanPlanNo());
            }
            // prepare order month
            transferOutInfo.setOrderMonth(getOrderMonth(ifIpInfo.getDecantImpPoNo(), ifIpInfo.getKanbanPlanNo()));
            if (ifIpInfo.getDecantOriginalPartsId() != null) {
                transferOutInfo.setFromPartsId(ifIpInfo.getDecantOriginalPartsId());
                transferOutInfo.setFromCustomerId(ifIpInfo.getDecantOriginalCustId());
            } else {
                transferOutInfo.setFromPartsId(ifIpInfo.getPartsId());
                transferOutInfo.setFromCustomerId(ifIpInfo.getCustomerId());
            }
            transferOutInfo.setToCustomerId(ifIpInfo.getIfCustomerId());
            transferOutInfo.setToPartsId(ifIpInfo.getIfPartsId());
            transferOutInfo.setSupplierId(ifIpInfo.getSupplierId());
            transferOutInfo.setTransferedDate(impStDate);
            transferOutInfo.setQty(qty);
            transferOutInfo.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
            transferOutInfo.setCreatedDate(ifIpInfo.getUpdatedDate());
            transferOutInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            transferOutInfo.setUpdatedDate(ifIpInfo.getUpdatedDate());
            transferOutInfo.setVersion(IntDef.INT_ONE);

            // insert
            baseDao.insert(transferOutInfo);
        }
    }

    /**
     * Get Order Month
     * 
     * @param impPoNo impPoNo
     * @param kanbanPlanNo kanbanPlanNo
     * @return OrderMonth
     */
    private String getOrderMonth(String impPoNo, String kanbanPlanNo) {
        String result = null;
        if (!StringUtil.isNullOrEmpty(impPoNo)) {
            result = orderMonthMap.get(impPoNo);
        } else {
            result = orderMonthMap.get(kanbanPlanNo);
        }

        if (StringUtil.isNullOrEmpty(result)) {
            TnfOrderStatus param = new TnfOrderStatus();
            if (!StringUtil.isNullOrEmpty(impPoNo)) {
                param.setImpPoNo(impPoNo);
            } else {
                param.setKanbanPlanNo(kanbanPlanNo);
            }
            TnfOrderStatus orderStatusInfo = baseDao.findOne(param);
            if (orderStatusInfo != null) {
                result = orderStatusInfo.getOrderMonth();
                if (!StringUtil.isNullOrEmpty(impPoNo)) {
                    orderMonthMap.put(impPoNo, result);
                } else {
                    orderMonthMap.put(kanbanPlanNo, result);
                }
            }
        }
        return result;
    }

    /**
     * update original transfer out.
     * 
     * @param ifIpInfo ifIpInfo
     * @param processType processType
     */
    private void updateOriginalTransferOut(IfIpEntity ifIpInfo, int processType) {
        TntTransferOut param = new TntTransferOut();
        param.setFromPartsId(ifIpInfo.getDecantOriginalPartsId());
        param.setToPartsId(ifIpInfo.getPartsId());
        param.setSupplierId(ifIpInfo.getSupplierId());
        param.setTransferedDate(ifIpInfo.getImpStDate());
        if (PROCESS_TYPE_VV == processType) {
            param.setImpPoNo(ifIpInfo.getDecantImpPoNo());
            param.setExpPoNo(ifIpInfo.getDecantExpPoNo());
        } else {
            param.setKanbanPlanNo(ifIpInfo.getKanbanPlanNo());
        }

        TntTransferOut transferOutInfo = baseDao.findOne(param);
        if (transferOutInfo != null) {
            BigDecimal qty = StringUtil.toBigDecimal(ifIpInfo.getQty());
            transferOutInfo.setQty(DecimalUtil.subtract(transferOutInfo.getQty(), qty));
            transferOutInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            transferOutInfo.setUpdatedDate(ifIpInfo.getUpdatedDate());
            transferOutInfo.setVersion(transferOutInfo.getVersion() + IntDef.INT_ONE);
            baseDao.update(transferOutInfo);
        }
    }

    /**
     * update TRANSFER_OUT_QTY
     * 
     * @param ifIpInfo ifIpInfo
     * @param processType processType
     */
    private void updateOldTnfOrderStatus(IfIpEntity ifIpInfo, int processType) {
        TnfOrderStatus param = new TnfOrderStatus();
        param.setPartsId(ifIpInfo.getPartsId());
        param.setSupplierId(ifIpInfo.getSupplierId());
        if (PROCESS_TYPE_VV == processType) {
            param.setImpPoNo(ifIpInfo.getDecantImpPoNo());
            param.setExpPoNo(ifIpInfo.getDecantExpPoNo());
        } else {
            param.setKanbanPlanNo(ifIpInfo.getKanbanPlanNo());
        }

        TnfOrderStatus orderStatusInfo = baseDao.findOne(param);
        if (orderStatusInfo != null) {
            BigDecimal qty = StringUtil.toBigDecimal(ifIpInfo.getQty());
            orderStatusInfo.setImpStockQty(DecimalUtil.subtract(orderStatusInfo.getImpStockQty(), qty));
            orderStatusInfo.setTransferOutQty(DecimalUtil.add(orderStatusInfo.getTransferOutQty(), qty));
            orderStatusInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            orderStatusInfo.setUpdatedDate(ifIpInfo.getUpdatedDate());
            orderStatusInfo.setVersion(orderStatusInfo.getVersion() + IntDef.INT_ONE);
            baseDao.update(orderStatusInfo);
        }
    }

    /**
     * Save into order information, update TNF_ORDER_STATUS
     * 
     * @param ifIpInfo ifIpInfo
     * @param processType processType
     */
    private void updateTnfOrderStatus(IfIpEntity ifIpInfo, int processType) {

        // get parts id
        Integer partsId = ifIpInfo.getPartsId() == null ? ifIpInfo.getIfPartsId() : ifIpInfo.getPartsId();

        TnfOrderStatus param = new TnfOrderStatus();
        param.setPartsId(partsId);
        param.setSupplierId(ifIpInfo.getSupplierId());
        if (PROCESS_TYPE_VV == processType) {
            param.setImpPoNo(ifIpInfo.getDecantImpPoNo());
            param.setExpPoNo(ifIpInfo.getDecantExpPoNo());
        } else {
            param.setKanbanPlanNo(ifIpInfo.getKanbanPlanNo());
        }
        TnfOrderStatus orderStatusInfo = baseDao.findOne(param);
        if (orderStatusInfo != null) {
            BigDecimal qty = StringUtil.toBigDecimal(ifIpInfo.getQty());
            BigDecimal adjustQty = StringUtil.toBigDecimal(ifIpInfo.getSaQty());
            // check
            int actionType = Integer.parseInt(ifIpInfo.getActionType());
            // check action type
            switch (actionType) {
                case ActionType.CUSTOMS_CLEARANCE:
                    orderStatusInfo
                        .setExpOnshippingQty(DecimalUtil.subtract(orderStatusInfo.getExpOnshippingQty(), qty));
                    orderStatusInfo.setInRackQty(DecimalUtil.add(orderStatusInfo.getInRackQty(), qty));
                    break;
                case ActionType.DEVANNED:
                case ActionType.IMP_INBOUND:
                    // no devanned
                    if (actionType == ActionType.DEVANNED || ifIpInfo.getInvDevanDate() == null) {
                        // set imp stock qty
                        orderStatusInfo.setImpStockQty(DecimalUtil.add(orderStatusInfo.getImpStockQty(), qty));
                        if (ifIpInfo.getCcDate() != null) {
                            // reset in rack qty
                            orderStatusInfo.setInRackQty(DecimalUtil.subtract(orderStatusInfo.getInRackQty(), qty));
                        }
                        if (ifIpInfo.getCcDate() == null) {
                            // reset on-shipping qty
                            orderStatusInfo.setExpOnshippingQty(DecimalUtil.subtract(
                                orderStatusInfo.getExpOnshippingQty(), qty));
                        }
                    }
                    break;
                case ActionType.STOCK_ADJUST:
                    orderStatusInfo.setImpStockQty(DecimalUtil.add(orderStatusInfo.getImpStockQty(), adjustQty));
                    orderStatusInfo.setImpAdjustedQty(DecimalUtil.add(orderStatusInfo.getImpAdjustedQty(), adjustQty));
                    break;
                case ActionType.NG:
                    orderStatusInfo.setImpNgQty(DecimalUtil.add(orderStatusInfo.getImpNgQty(), qty));
                    break;
                case ActionType.ECI_ONHOLD:
                    orderStatusInfo.setImpEciQty(DecimalUtil.add(orderStatusInfo.getImpEciQty(), qty));
                    break;
                case ActionType.RELEASE_ONHOLD:
                    int oldOnHoldFlag = ifIpInfo.getIpOnholdFlag().intValue();
                    if (oldOnHoldFlag == OnHoldFlag.NG_ON_HOLD) {
                        orderStatusInfo.setImpNgQty(DecimalUtil.subtract(orderStatusInfo.getImpNgQty(), qty));
                    } else if (oldOnHoldFlag == OnHoldFlag.ECI_ON_HOLD) {
                        orderStatusInfo.setImpEciQty(DecimalUtil.subtract(orderStatusInfo.getImpEciQty(), qty));
                    }
                    break;
                case ActionType.IMP_OUTBOUND:
                    orderStatusInfo.setImpPrepareObQty(DecimalUtil.add(orderStatusInfo.getImpPrepareObQty(), qty));
                    break;
                case ActionType.CANCEL_INVOICE:
                    orderStatusInfo.setImpStockQty(DecimalUtil.subtract(orderStatusInfo.getImpStockQty(), qty));
                    orderStatusInfo.setCancelledQty(DecimalUtil.add(orderStatusInfo.getCancelledQty(), qty));
                    break;
            }
            // update information
            orderStatusInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            orderStatusInfo.setUpdatedDate(ifIpInfo.getUpdatedDate());
            orderStatusInfo.setVersion(orderStatusInfo.getVersion() + IntDef.INT_ONE);
            // update
            baseDao.update(orderStatusInfo);
        }
    }

    /**
     * updateOldImpStock.
     * 
     * @param ifIpInfo ifIpInfo
     */
    private void updateOldImpStock(IfIpEntity ifIpInfo) {

        TnfImpStock impStockParam = new TnfImpStock();
        impStockParam.setOfficeId(ifIpInfo.getOfficeId());
        impStockParam.setWhsId(ifIpInfo.getWhsId());
        impStockParam.setPartsId(ifIpInfo.getPartsId());
        impStockParam.setSupplierId(ifIpInfo.getSupplierId());

        TnfImpStock impStockInfo = baseDao.findOne(impStockParam);
        if (impStockInfo != null) {
            BigDecimal qty = StringUtil.toBigDecimal(ifIpInfo.getQty());
            impStockInfo.setImpStockQty(DecimalUtil.subtract(impStockInfo.getImpStockQty(), qty));
            impStockInfo.setSystemStockQty(DecimalUtil.subtract(impStockInfo.getSystemStockQty(), qty));
            impStockInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            impStockInfo.setUpdatedDate(ifIpInfo.getUpdatedDate());
            impStockInfo.setVersion(impStockInfo.getVersion() + IntDef.INT_ONE);
            baseDao.update(impStockInfo);
        }
    }

    /**
     * Update TNT_IMP_STOCK
     * 
     * @param ifIpInfo Effective Imp Ip Info
     * @return TnfImpStock
     */
    private TnfImpStock saveImpStock(IfIpEntity ifIpInfo) {

        // prepare
        int actionType = Integer.parseInt(ifIpInfo.getActionType());
        Integer officeId = ifIpInfo.getOfficeId();
        Integer whsId = ifIpInfo.getWhsId();
        Integer partsId = ifIpInfo.getPartsId();
        if (actionType == ActionType.IMP_INBOUND 
                || actionType == ActionType.CANCEL_INVOICE) {
            officeId = ifIpInfo.getIfOfficeId();
            whsId = ifIpInfo.getIfWhsId();
            partsId = ifIpInfo.getPartsId() == null ? ifIpInfo.getIfPartsId() : ifIpInfo.getPartsId();
        } else if (actionType == ActionType.STOCK_TRANSFER) {
            officeId = ifIpInfo.getIfOfficeId();
            whsId = ifIpInfo.getIfWhsId();
            partsId = ifIpInfo.getIfPartsId();
        } else if (actionType == ActionType.WHS_TRANSFER) {
            whsId = ifIpInfo.getIfWhsId();
        } else if (actionType == ActionType.CUSTOMS_CLEARANCE || actionType == ActionType.DEVANNED) {
            officeId = ifIpInfo.getIfOfficeId();
            whsId = ifIpInfo.getIfWhsId();
        }

        // get tnf imp stock
        TnfImpStock impStockParam = new TnfImpStock();
        impStockParam.setOfficeId(officeId);
        impStockParam.setWhsId(whsId);
        impStockParam.setPartsId(partsId);
        impStockParam.setSupplierId(ifIpInfo.getSupplierId());

        TnfImpStock impStockInfo = baseDao.findOne(impStockParam);
        if (impStockInfo == null) {
            // If the record do not exist in TNT_IMP_STOCK, new record into database.
            impStockInfo = new TnfImpStock();
            impStockInfo.setOfficeId(officeId);
            impStockInfo.setWhsId(whsId);
            impStockInfo.setPartsId(partsId);
            impStockInfo.setBusinessPattern(ifIpInfo.getBusinessPattern());
            impStockInfo.setSupplierId(ifIpInfo.getSupplierId());
            impStockInfo.setImpInRackQty(BigDecimal.ZERO);
            impStockInfo.setImpStockQty(BigDecimal.ZERO);
            impStockInfo.setSystemStockQty(BigDecimal.ZERO);
            impStockInfo.setNgQty(BigDecimal.ZERO);
            impStockInfo.setOnholdQty(BigDecimal.ZERO);
            impStockInfo.setDeliveredQty(BigDecimal.ZERO);
            impStockInfo.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
            impStockInfo.setCreatedDate(ifIpInfo.getUpdatedDate());
            impStockInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            impStockInfo.setUpdatedDate(ifIpInfo.getUpdatedDate());
            impStockInfo.setVersion(IntDef.INT_ONE);
        } else {
            impStockInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            impStockInfo.setUpdatedDate(ifIpInfo.getUpdatedDate());
            impStockInfo.setVersion(impStockInfo.getVersion() + IntDef.INT_ONE);
        }
        // define
        BigDecimal impInRackQty = null;
        BigDecimal impStockQty = null;
        BigDecimal whsStockQty = null;
        BigDecimal ngQty = null;
        BigDecimal onholdQty = null;
        BigDecimal qty = StringUtil.toBigDecimal(ifIpInfo.getQty());
        BigDecimal adjustQty = StringUtil.toBigDecimal(ifIpInfo.getSaQty());
        
        // check action type
        switch (actionType) {
            case ActionType.CUSTOMS_CLEARANCE:
                impInRackQty = qty;
                break;
            case ActionType.DEVANNED:
            case ActionType.IMP_INBOUND:
                // no devanned
                if (actionType == ActionType.DEVANNED || ifIpInfo.getInvDevanDate() == null) {
                    // set imp stock qty
                    impStockQty = qty;
                    whsStockQty = qty;
                    // if has custom clearance and is belong to with clearance
                    if (ifIpInfo.getCcDate() != null) {
                        // reset in rack qty
                        impInRackQty = DecimalUtil.subtract(BigDecimal.ZERO, qty);
                    }
                }
                break;
            case ActionType.WHS_TRANSFER:
            case ActionType.STOCK_TRANSFER:
                impStockQty = qty;
                whsStockQty = qty;
                break;
            case ActionType.STOCK_ADJUST:
                impStockQty = adjustQty;
                whsStockQty = adjustQty;
                break;
            case ActionType.NG:
                ngQty = qty;
                break;
            case ActionType.ECI_ONHOLD:
                onholdQty = qty;
                break;
            case ActionType.RELEASE_ONHOLD:
                int oldOnHoldFlag = ifIpInfo.getIpOnholdFlag().intValue();
                if (oldOnHoldFlag == OnHoldFlag.NG_ON_HOLD) {
                    ngQty = qty.negate();
                } else if (oldOnHoldFlag == OnHoldFlag.ECI_ON_HOLD) {
                    onholdQty = qty.negate();
                }
                break;
            case ActionType.IMP_OUTBOUND:
                whsStockQty = DecimalUtil.subtract(BigDecimal.ZERO, qty);
                break;
            case ActionType.CANCEL_INVOICE:
                // set imp stock qty
                whsStockQty = DecimalUtil.subtract(BigDecimal.ZERO, qty);
                impStockQty = DecimalUtil.subtract(BigDecimal.ZERO, qty);
                break;
        }
        // reset values
        impStockInfo.setImpInRackQty(DecimalUtil.add(impStockInfo.getImpInRackQty(), impInRackQty));
        impStockInfo.setImpStockQty(DecimalUtil.add(impStockInfo.getImpStockQty(), impStockQty));
        impStockInfo.setSystemStockQty(DecimalUtil.add(impStockInfo.getSystemStockQty(), whsStockQty));
        impStockInfo.setNgQty(DecimalUtil.add(impStockInfo.getNgQty(), ngQty));
        impStockInfo.setOnholdQty(DecimalUtil.add(impStockInfo.getOnholdQty(), onholdQty));
        // save or update
        baseDao.saveOrUpdate(impStockInfo);
        return impStockInfo;
    }

    /**
     * Update TNT_IP
     * 
     * @param ifIpInfo Effective Imp Ip Info
     * @return TntIp
     */
    private TntIp updateIpInfo(IfIpEntity ifIpInfo) {
        // Update IF IP information into TNT_IP, with update condition: IP_ID = {Effective IP}.IP_ID
        TntIp ipInfo = null;
        if (ifIpInfo.getIpId() != null) {
            // Get IP information
            ipInfo = baseDao.findOne(TntIp.class, ifIpInfo.getIpId());
        } else {
            // new par
            ipInfo = new TntIp();
        }
        // reset prepare information
        this.prepareIpInformation(ipInfo, ifIpInfo);
        // update
        baseDao.saveOrUpdate(ipInfo);
        // return
        return ipInfo;
    }

    /**
     * prepare ip information.
     * 
     * @param ipInfo ipInfo
     * @param ifIpInfo ifIpInfo
     */
    private void prepareIpInformation(TntIp ipInfo, IfIpEntity ifIpInfo) {
        // set other information for new
        if (ipInfo.getIpId() == null) {
            // other information
            ipInfo.setPidNo(ifIpInfo.getPidNo());
            // if inbound
            if (ifIpInfo.getActionType().equals(String.valueOf(ActionType.IMP_INBOUND))) {
                ipInfo.setWhsId(ifIpInfo.getIfWhsId());
                ipInfo.setCustomerId(ifIpInfo.getIfCustomerId());
                ipInfo.setPartsId(ifIpInfo.getIfPartsId());
                ipInfo.setOfficeId(ifIpInfo.getIfOfficeId());
                ipInfo.setInvoiceNo(ifIpInfo.getInvoiceNo());
                ipInfo.setCustomsClrDate(ifIpInfo.getCcDate());
                ipInfo.setDevannedDate(ifIpInfo.getInvDevanDate());
                ipInfo.setOnholdFlag(OnHoldFlag.NORMAL);
            } else {
                ipInfo.setWhsId(ifIpInfo.getWhsId());
                ipInfo.setCustomerId(ifIpInfo.getCustomerId());
                ipInfo.setPartsId(ifIpInfo.getPartsId());
                ipInfo.setOfficeId(ifIpInfo.getOfficeId());
                ipInfo.setInvoiceNo(ifIpInfo.getDecantInvoiceNo());
                ipInfo.setCustomsClrDate(ifIpInfo.getDecantCcDate());
                ipInfo.setDevannedDate(ifIpInfo.getDecantDevanDate());
                ipInfo.setExpObActualDate(ifIpInfo.getExpObActualDate());
                ipInfo.setImpInbActualDate(ifIpInfo.getImpInbActualDate());
                ipInfo.setImpWtDate(ifIpInfo.getImpWtDate());
                ipInfo.setImpStDate(ifIpInfo.getImpStDate());
                ipInfo.setImpSaDatetime(ifIpInfo.getImpSaDate());
                ipInfo.setOriginalPartsId(ifIpInfo.getDecantOriginalPartsId());
                ipInfo.setOnholdFlag(ifIpInfo.getIpOnholdFlag());
                ipInfo.setOnholdDate(ifIpInfo.getIpOnholdDate());
            }
            if (ifIpInfo.getBusinessPattern() != null
                    && ifIpInfo.getBusinessPattern().intValue() == BusinessPattern.V_V) {
                ipInfo.setIpNo(ifIpInfo.getSourceIpNo());
            }
            ipInfo.setBusinessPattern(ifIpInfo.getBusinessPattern());
            ipInfo.setSupplierId(ifIpInfo.getSupplierId());
            ipInfo.setExpPartsId(ifIpInfo.getExpPartsId());
            ipInfo.setKanbanPlanNo(ifIpInfo.getKanbanPlanNo());
            ipInfo.setImpPoNo(ifIpInfo.getDecantImpPoNo());
            ipInfo.setCustomerOrderNo(ifIpInfo.getDecantCustomerOrderNo());
            ipInfo.setExpPoNo(ifIpInfo.getDecantExpPoNo());
            ipInfo.setExpPoItemNo(ifIpInfo.getDecantExpPoItemNo());
            ipInfo.setContainerNo(ifIpInfo.getDecantContainerNo());
            ipInfo.setModuleNo(ifIpInfo.getDecantModuleNo());
            ipInfo.setExpInbPlanDate(ifIpInfo.getExpInbPlanDate());
        }
        // according action type
        int actionType = Integer.parseInt(ifIpInfo.getActionType());
        switch (actionType) {
            case ActionType.IMP_INBOUND:
                ipInfo.setPidNo(ifIpInfo.getPidNo());
                ipInfo.setWhsId(ifIpInfo.getIfWhsId());
                ipInfo.setStatus(IpStatus.IMP_INBOUND);
                ipInfo.setImpInbActualDate(ifIpInfo.getProcessDate());
                ipInfo.setQty(StringUtil.toBigDecimal(ifIpInfo.getQty()));
                if (ifIpInfo.getBusinessPattern() != null
                        && ifIpInfo.getBusinessPattern().intValue() == BusinessPattern.AISIN) {
                    ipInfo.setContainerNo(ifIpInfo.getContainerNo());
                    ipInfo.setModuleNo(ifIpInfo.getModuleNo());
                }
                break;
            case ActionType.WHS_TRANSFER:
                ipInfo.setStatus(IpStatus.WHS_TRANSFER);
                ipInfo.setImpWtDate(ifIpInfo.getProcessDate());
                ipInfo.setWhsId(ifIpInfo.getIfWhsId());
                break;
            case ActionType.STOCK_TRANSFER:
                ipInfo.setCustomerId(ifIpInfo.getIfCustomerId());
                ipInfo.setPartsId(ifIpInfo.getIfPartsId());
                ipInfo.setExpPartsId(ifIpInfo.getIfExpPartsId());
                ipInfo.setStatus(IpStatus.STOCK_TRANSFER);
                ipInfo.setImpStDate(ifIpInfo.getProcessDate());
                if (ipInfo.getOriginalPartsId() == null) {
                    ipInfo.setOriginalPartsId(ifIpInfo.getPartsId());
                }
                break;
            case ActionType.STOCK_ADJUST:
                ipInfo.setStatus(IpStatus.STOCK_ADJUST);
                ipInfo.setImpSaDatetime(ifIpInfo.getProcessDate());
                ipInfo.setQty(StringUtil.toBigDecimal(ifIpInfo.getQty()));
                break;
            case ActionType.NG:
            case ActionType.ECI_ONHOLD:
            case ActionType.RELEASE_ONHOLD:
                ipInfo.setOnholdDate(ifIpInfo.getProcessDate());
                ipInfo.setOnholdFlag(StringUtil.toInteger(ifIpInfo.getOnholdFlag()));
                break;
            case ActionType.DECANT:
                ipInfo.setStatus(IpStatus.DECANT);
                ipInfo.setImpDecantDatetime(ifIpInfo.getProcessDate());
                ipInfo.setQty(StringUtil.toBigDecimal(ifIpInfo.getQty()));
                break;
            case ActionType.IMP_OUTBOUND:
                Timestamp outboundDateTime = DateTimeUtil.parseDateTime(ifIpInfo.getOutboundDatetime(),
                    DateTimeUtil.FORMAT_IP_DATE);
                ipInfo.setStatus(IpStatus.IMP_OUTBOUND);
                ipInfo.setImpDispatchedDatetime(ifIpInfo.getProcessDate());
                ipInfo.setImpObActualDate(outboundDateTime);
                ipInfo.setImpOutboundDatetime(outboundDateTime);
                ipInfo.setDeliveryNoteNo(ifIpInfo.getDeliveryNoteNo());
                break;
            case ActionType.CANCEL_INVOICE:
                ipInfo.setStatus(IpStatus.CANCELLED);
                break;
        }
        // set update information
        if (ipInfo.getCreatedDate() == null) {
            ipInfo.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
            ipInfo.setCreatedDate(ifIpInfo.getUpdatedDate());
            ipInfo.setVersion(IntDef.INT_ZERO);
        }
        ipInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        ipInfo.setUpdatedDate(ifIpInfo.getUpdatedDate());
        ipInfo.setVersion(ipInfo.getVersion() + IntDef.INT_ONE);
    }

    /**
     * pick up all actual Inbound date from database.
     * 
     * @param dbTime data base update time
     * @param processType Process Type
     * @return inbound data
     */
    private List<ReceiveData> getInbDateList(Timestamp dbTime, int processType) {
        ReceiveData param = new ReceiveData();
        param.setUpdatedDate(dbTime);
        // param.setIfDateTime(ifDateTime);
        // param.setOfficeCode(officeCode);
        param.setUpdatedDate(dbTime);
        if (PROCESS_TYPE_VV == processType) {
            param.setBusinessPattern(BusinessPattern.V_V);
        } else {
            param.setBusinessPattern(BusinessPattern.AISIN);
        }
        return baseMapper.selectList(this.getSqlId("selectInbDate"), param);
    }

    /**
     * pick up all Allocated Kanban List
     * 
     * @param pidNo pid no
     * @param ifIpInfo ifIpInfo
     * @return Kanban List
     */
    private List<TntIpKanban> getAllocatedKanbanList(String pidNo, IfIpEntity ifIpInfo) {
        // prepare parameter
        IfIpEntity param = new IfIpEntity();
        if (pidNo != null) {
            // set parameter
            param.setPidNo(pidNo);
            // get
            return baseMapper.selectList(this.getSqlId("selectAllocatedKanbanList"), param);
        } else {
            // set parameter
            param.setModuleNo(ifIpInfo.getModuleNo());
            param.setPartsId(ifIpInfo.getIfPartsId());
            param.setInvoiceNo(ifIpInfo.getInvoiceNo());
            // get
            return baseMapper.selectList(this.getSqlId("selectAllocatedKanbanListForInbound"), param);
        }
    }

    /**
     * Pick up KANBAN_PLAN_NOs of IP.Invoice which does not has been allocated completed.
     * 
     * @param contaninerNo contaninerNo
     * @param invoiceNo invoice No
     * @return KANBAN plan data
     */
    private List<TntIpKanban> getAllocateListForModule(String contaninerNo, String invoiceNo) {
        // prepare parameter
        IfIpEntity param = new IfIpEntity();
        param.setContainerNo(contaninerNo);
        param.setInvoiceNo(invoiceNo);
        // get list
        return baseMapper.selectList(this.getSqlId("selectAllocatedKanbanListForModule"), param);
    }

    /**
     * Get imp inbound information for invoice by each Inbound Date.
     * 
     * @param dbTime dbTime
     * @param inbDate Inbound Date
     * @param processType Process Type
     * @return inbound invoice qty
     */
    private List<ReceiveData> getInbInvoiceQtyList(Timestamp dbTime, String inbDate, int processType) {
        ReceiveData param = new ReceiveData();
        param.setUpdatedDate(dbTime);
        // param.setIfDateTime(ifDateTime);
        // param.setOfficeCode(officeCode);
        param.setInbDate(inbDate);
        if (PROCESS_TYPE_VV == processType) {
            param.setBusinessPattern(BusinessPattern.V_V);
        } else {
            param.setBusinessPattern(BusinessPattern.AISIN);
        }
        return baseMapper.selectList(this.getSqlId("selectInbInvocieQty"), param);
    }

    /**
     * Update INBOUND_QTY of TNT_INVOICE_SUMMARY by each Invoice.
     * 
     * @param inbInvoiceQty inbInvoiceQty
     */
    private void updateInbInvoiceQty(ReceiveData inbInvoiceQty) {
        String invoiceNo = inbInvoiceQty.getInbInvoiceNo();
        Object[] param = new Object[] { invoiceNo, InvoiceStatus.PENDING };
        String hql = "FROM TNT_INVOICE_SUMMARY A WHERE A.invoiceNo = ? AND A.invoiceStatus = ?";
        TntInvoiceSummary invoiceSummary = baseDao.findOne(hql, param);
        if (invoiceSummary != null) {
            BigDecimal inboundQty = invoiceSummary.getInboundQty();
            inboundQty = DecimalUtil.add(inboundQty, inbInvoiceQty.getSumQty());
            invoiceSummary.setInboundQty(inboundQty);
            invoiceSummary.setUpdatedDate(inbInvoiceQty.getUpdatedDate());
            invoiceSummary.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            invoiceSummary.setVersion(invoiceSummary.getVersion() + IntDef.INT_ONE);
            baseDao.update(invoiceSummary);
        }
    }

    /**
     * Update Status of Invoice Summary
     * 
     * @param invoiceNoList invoiceNo List
     * @param dbTime database time
     */
    private void updateInvoiceSummaryStatus(List<String> invoiceNoList, Timestamp dbTime) {
        ReceiveData param = new ReceiveData();
        param.setInvoiceNoList(invoiceNoList);
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setUpdatedDate(dbTime);
        baseMapper.update(this.getSqlId("updateInvoiceSummaryStatus"), param);
    }

    /**
     * Update Status of Invoice Summary
     * 
     * @param invoiceNoList invoiceNo List
     * @param inbDate inbound Date
     * @param dbTime database time
     * @param processType processType
     */
    private void updateInvoiceIbActualDateForComp(List<String> invoiceNoList, String inbDate, Timestamp dbTime,
        int processType) {
        ReceiveData param = new ReceiveData();
        param.setInvoiceNoList(invoiceNoList);
        param.setInbDate(inbDate);
        param.setActualInbDate(DateTimeUtil.parseDate(inbDate, DateTimeUtil.FORMAT_YYYYMMDD));
        param.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        param.setUpdatedDate(dbTime);
        if (processType == PROCESS_TYPE_VV) {
            baseMapper.update(this.getSqlId("updateInvoiceIbActualDateForVV"), param);
        } else {
            baseMapper.update(this.getSqlId("updateInvoiceIbActualDateForAISIN"), param);
        }
    }

    /**
     * Get partial inbound invoice and invoice parts from TNT_IF_IMP_IP.
     * 
     * @param expPartsList expPartsList
     * @param dbTime dbTime
     * @param inbDate Inbound Date
     * @param processType Process Type
     * @return partial inbound invoice
     */
    private Map<Integer, Map<String, IfIpEntity>> getPartialInboundInvoice(List<Integer> expPartsList,
        Timestamp dbTime, String inbDate, int processType) {

        // get IP entity list
        List<IfIpEntity> ifIpEntityList = this.getInboundInvoiceList(expPartsList, dbTime, inbDate, processType);
        // loop
        Map<Integer, Map<String, IfIpEntity>> result = new HashMap<Integer, Map<String, IfIpEntity>>();
        if (ifIpEntityList != null) {
            for (IfIpEntity ifIpEntity : ifIpEntityList) {
                Map<String, IfIpEntity> ifIpListMap = result.get(ifIpEntity.getInvoiceId());
                if (ifIpListMap == null) {
                    ifIpListMap = new HashMap<String, IfIpEntity>();
                    result.put(ifIpEntity.getInvoiceId(), ifIpListMap);
                }
                // get Key
                StringBuffer partsKey = new StringBuffer("");
                if (PROCESS_TYPE_VV == processType) {
                    partsKey.append(ifIpEntity.getImpPoNo());
                    partsKey.append(StringConst.UNDERLINE);
                    partsKey.append(ifIpEntity.getExpPoNo());
                    partsKey.append(StringConst.UNDERLINE);
                } else {
                    partsKey.append(ifIpEntity.getInvoiceId());
                    partsKey.append(StringConst.UNDERLINE);
                }
                partsKey.append(ifIpEntity.getPartsId());
                partsKey.append(StringConst.UNDERLINE);
                partsKey.append(ifIpEntity.getSupplierId());
                ifIpListMap.put(partsKey.toString(), ifIpEntity);
            }
        }
        return result;
    }

    /**
     * get Inbound Invoice List for partial inbound.
     * 
     * @param expPartsList expPartsList
     * @param dbTime dbTime
     * @param inbDate inbDate
     * @param processType processType
     * @return list
     */
    private List<IfIpEntity> getInboundInvoiceList(List<Integer> expPartsList, Timestamp dbTime, String inbDate,
        int processType) {

        // prepare parameter
        ReceiveData param = new ReceiveData();
        param.setUpdatedDate(dbTime);
        // param.setIfDateTime(ifDateTime);
        // param.setOfficeCode(officeCode);
        param.setExpPartsIdList(expPartsList);
        param.setInbDate(inbDate);
        param.setActualInbDate(DateTimeUtil.parseDate(inbDate, DateTimeUtil.FORMAT_YYYYMMDD));
        if (PROCESS_TYPE_VV == processType) {
            param.setBusinessPattern(BusinessPattern.V_V);
        } else {
            param.setBusinessPattern(BusinessPattern.AISIN);
        }

        // get IP entity list
        if (processType == PROCESS_TYPE_VV) {
            return baseMapper.selectList(this.getSqlId("selectPartialInboundInvoiceForVV"), param);
        } else {
            return baseMapper.selectList(this.getSqlId("selectPartialInboundInvoiceForAISIN"), param);
        }
    }

    /**
     * Update Invoice and split Invoice by Actual Inbound Date.
     * 
     * @param partialInvoiceMap partialInvoiceMap
     * @param inbDate inbDate
     * @param processType processType
     */
    private void updateAndSplitInvoice(Map<Integer, Map<String, IfIpEntity>> partialInvoiceMap, String inbDate,
        int processType) {
        if (partialInvoiceMap.size() == 0) {
            return;
        }
        Timestamp dbTime = getDBDateTimeByDefaultTimezone();
        // Loop each Invoice(already split by Imp Inbound Actual Date)
        Iterator<Integer> key = partialInvoiceMap.keySet().iterator();
        while (key.hasNext()) {
            // Get latest update Invoice information from TNT_INVOICE
            Integer invoiceId = key.next();
            Object[] param = new Object[] { invoiceId };
            String hql = "FROM TNT_INVOICE A WHERE A.invoiceId = ?";
            TntInvoice invoiceInfo = baseDao.findOne(hql, param);
            // Back up current invoice information,
            // insert current invoice into TNT_INVOICE_HISTORY.
            TntInvoiceHistory invoiceHistory = new TntInvoiceHistory();
            invoiceHistory.setInvoiceId(invoiceId);
            invoiceHistory.setOriginalVersion(invoiceInfo.getOriginalVersion());
            if (invoiceInfo.getOriginalVersion() != null) {
                invoiceHistory.setRevisionVersion(invoiceInfo.getOriginalVersion() + IntDef.INT_ONE);
            } else {
                invoiceHistory.setRevisionVersion(IntDef.INT_ONE);
            }
            invoiceHistory.setEtd(invoiceInfo.getEtd());
            invoiceHistory.setEta(invoiceInfo.getEta());
            invoiceHistory.setVanningDate(invoiceInfo.getVanningDate());
            invoiceHistory.setImpInbPlanDate(invoiceInfo.getImpInbPlanDate());
            invoiceHistory.setCcDate(invoiceInfo.getCcDate());
            invoiceHistory.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
            invoiceHistory.setCreatedDate(dbTime);
            invoiceHistory.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            invoiceHistory.setUpdatedDate(dbTime);
            invoiceHistory.setVersion(IntDef.INT_ONE);
            baseDao.insert(invoiceHistory);
            // Update IMP_INB_ACTUAL_DATE data to actual INB_DATE for table TNT_INVOICE.
            invoiceInfo.setImpInbActualDate(DateTimeUtil.parseDate(inbDate, DateTimeUtil.FORMAT_YYYYMMDD));
            invoiceInfo.setOriginalVersion(invoiceInfo.getRevisionVersion());
            invoiceInfo.setRevisionVersion(invoiceInfo.getRevisionVersion() + IntDef.INT_ONE);
            invoiceInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            invoiceInfo.setUpdatedDate(dbTime);
            invoiceInfo.setVersion(invoiceInfo.getVersion() + IntDef.INT_ONE);
            baseDao.update(invoiceInfo);
            // Copy and create a new recored which IMP_INB_ACTUAL_DATE is null's data into TNT_INVOICE from the record which has been updated.
            TntInvoice newInvoiceInfo = new TntInvoice();
            newInvoiceInfo.setInvoiceGroupId(invoiceInfo.getInvoiceGroupId());
            newInvoiceInfo.setInvoiceSummaryId(invoiceInfo.getInvoiceSummaryId());
            newInvoiceInfo.setOfficeId(invoiceInfo.getOfficeId());
            newInvoiceInfo.setInvoiceNo(invoiceInfo.getInvoiceNo());
            newInvoiceInfo.setOriginalVersion(invoiceInfo.getOriginalVersion());
            newInvoiceInfo.setRevisionVersion(invoiceInfo.getRevisionVersion() + IntDef.INT_ONE);
            newInvoiceInfo.setEtd(invoiceInfo.getEtd());
            newInvoiceInfo.setEta(invoiceInfo.getEta());
            newInvoiceInfo.setVanningDate(invoiceInfo.getVanningDate());
            newInvoiceInfo.setCcDate(invoiceInfo.getCcDate());
            newInvoiceInfo.setImpInbPlanDate(invoiceInfo.getImpInbPlanDate());
            newInvoiceInfo.setImpCcActualDate(invoiceInfo.getImpCcActualDate());
            newInvoiceInfo.setImpInbActualDate(null);
            newInvoiceInfo.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
            newInvoiceInfo.setCreatedDate(dbTime);
            newInvoiceInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            newInvoiceInfo.setUpdatedDate(dbTime);
            newInvoiceInfo.setVersion(IntDef.INT_ONE);
            baseDao.insert(newInvoiceInfo);
            Integer newInvoiceId = newInvoiceInfo.getInvoiceId();
            // Update and Copy Invoice Parts.
            updateAndCopyInvoiceParts(invoiceInfo, partialInvoiceMap.get(invoiceId), newInvoiceId, dbTime, processType);
        }
    }

    /**
     * Update and Copy Invoice Parts.
     * 
     * @param invoiceInfo Invoice Info
     * @param updatePartsMap Update Parts Map
     * @param newInvoiceId New Invoice Id
     * @param dbTime dbTime
     * @param processType Process Type
     */
    private void updateAndCopyInvoiceParts(TntInvoice invoiceInfo, Map<String, IfIpEntity> updatePartsMap,
        Integer newInvoiceId, Timestamp dbTime, int processType) {
        // select invoice parts
        Object[] param = new Object[] { invoiceInfo.getInvoiceId() };
        String hql = "FROM TNT_INVOICE_PARTS A WHERE A.invoiceId = ?";
        List<TntInvoicePart> invoicePartsList = baseDao.select(hql, param);
        if (invoicePartsList != null) {
            for (TntInvoicePart invoiceParts : invoicePartsList) {
                // get Key
                StringBuffer partsKey = new StringBuffer("");
                if (PROCESS_TYPE_VV == processType) {
                    partsKey.append(invoiceParts.getImpPoNo());
                    partsKey.append(StringConst.UNDERLINE);
                    partsKey.append(invoiceParts.getExpPoNo());
                    partsKey.append(StringConst.UNDERLINE);
                } else {
                    partsKey.append(invoiceInfo.getInvoiceId());
                    partsKey.append(StringConst.UNDERLINE);
                }
                partsKey.append(invoiceParts.getPartsId());
                partsKey.append(StringConst.UNDERLINE);
                partsKey.append(invoiceParts.getSupplierId());
                IfIpEntity updateInfo = updatePartsMap.get(partsKey.toString());
                // update old parts
                invoiceParts.setOriginalQty(invoiceParts.getQty());
                if (updateInfo != null) {
                    // set new parts
                    invoiceParts.setQty(updateInfo.getInvoiceQty());
                } else {
                    // set as zero
                    invoiceParts.setQty(BigDecimal.ZERO);
                }
                invoiceParts.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                invoiceParts.setUpdatedDate(dbTime);
                invoiceParts.setVersion(invoiceParts.getVersion() + IntDef.INT_ONE);
                baseDao.update(invoiceParts);
                // Copy and create new recoreds into TNT_INVOICE_PARTS.
                TntInvoicePart newInvoiceParts = new TntInvoicePart();
                newInvoiceParts.setInvoiceId(newInvoiceId);
                newInvoiceParts.setInvoiceDetailId(invoiceParts.getInvoiceDetailId());
                newInvoiceParts.setPartsId(invoiceParts.getPartsId());
                newInvoiceParts.setSupplierId(invoiceParts.getSupplierId());
                newInvoiceParts.setSupplierPartsNo(invoiceParts.getSupplierPartsNo());
                newInvoiceParts.setImpPoNo(invoiceParts.getImpPoNo());
                newInvoiceParts.setExpPoNo(invoiceParts.getExpPoNo());
                newInvoiceParts.setCustomerOrderNo(invoiceParts.getCustomerOrderNo());
                newInvoiceParts.setInvCustCode(invoiceParts.getInvCustCode());
                newInvoiceParts.setOriginalQty(invoiceParts.getOriginalQty());
                newInvoiceParts.setQty(DecimalUtil.subtract(invoiceParts.getOriginalQty(), invoiceParts.getQty()));
                newInvoiceParts.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                newInvoiceParts.setCreatedDate(dbTime);
                newInvoiceParts.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                newInvoiceParts.setUpdatedDate(dbTime);
                newInvoiceParts.setVersion(IntDef.INT_ONE);
                baseDao.insert(newInvoiceParts);
            }
        }
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param param parameter
     * @param processType processType
     * @return inbound invoice qty
     */
    private List<TnfImpStock> getInvoicePartsInfoByInvoice(IfIpEntity param, int processType) {
        if (processType == PROCESS_TYPE_VV) {
            return baseMapper.selectList(this.getSqlId("getContainerPartsInfoForVV"), param);
        } else {
            return baseMapper.selectList(this.getSqlId("getContainerPartsInfoForAISIN"), param);
        }
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param param parameter
     * @return inbound invoice qty
     */
    private List<IfIpEntity> selectPIDMissInvoice(IfIpEntity param) {
        return baseMapper.select(this.getSqlId("selectPIDMissInvoice"), param);
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param param parameter
     * @param processType processType
     * @return inbound invoice qty
     */
    private List<TnfOrderStatus> getOrderStatusByInvoice(IfIpEntity param, int processType) {
        if (processType == PROCESS_TYPE_VV) {
            return baseMapper.selectList(this.getSqlId("getOrderStatusByInvoiceVV"), param);
        } else {
            return baseMapper.selectList(this.getSqlId("getOrderStatusByInvoiceAISIN"), param);
        }
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param expPartsIds expPartsIds
     * @return inbound invoice qty
     */
    private List<ReceiveData> getExpPartsInfoByIds(List<Integer> expPartsIds) {
        ReceiveData param = new ReceiveData();
        param.setExpPartsIdList(expPartsIds);
        return baseMapper.selectList(this.getSqlId("selectExpPartsInfoByIds"), param);
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param matchInvIdList matchInvIdList
     * @return in-bound invoice qty
     */
    private List<ReceiveData> getMatchInvoiceInfoByIds(List<Integer> matchInvIdList) {
        ReceiveData param = new ReceiveData();
        param.setMatchInvIdList(matchInvIdList);
        return baseMapper.selectList(this.getSqlId("selectMatchInvoiceInfoByIds"), param);
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param param parameter
     * @return inbound invoice qty
     */
    private List<TnfImpStock> getModulePartsInfoForVV(IfIpEntity param) {
        return baseMapper.selectList(this.getSqlId("getModulePartsInfoForVV"), param);
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param param parameter
     * @return inbound invoice qty
     */
    private List<TnfOrderStatus> getOrderStatusByModuleVV(IfIpEntity param) {
        return baseMapper.selectList(this.getSqlId("getOrderStatusByModuleVV"), param);
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param ifIpInfo if ip information
     */
    private void prepareImpStockForInvoiceVV(IfIpEntity ifIpInfo) {
        // get imp stock information
        List<TnfImpStock> impStockList = this.getInvoicePartsInfoByInvoice(ifIpInfo, PROCESS_TYPE_VV);
        if (impStockList != null && !impStockList.isEmpty()) {
            for (TnfImpStock impStock : impStockList) {
                if (impStock.getImpStockId() == null) {
                    // add new
                    impStock.setWhsId(ifIpInfo.getIfWhsId());
                    impStock.setImpStockQty(BigDecimal.ZERO);
                    impStock.setSystemStockQty(BigDecimal.ZERO);
                    impStock.setNgQty(BigDecimal.ZERO);
                    impStock.setOnholdQty(BigDecimal.ZERO);
                    impStock.setDeliveredQty(BigDecimal.ZERO);
                    impStock.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                    impStock.setCreatedDate(ifIpInfo.getUpdatedDate());
                    impStock.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                    impStock.setUpdatedDate(ifIpInfo.getUpdatedDate());
                    impStock.setVersion(IntDef.INT_ONE);
                    // set into databae
                    baseDao.insert(impStock);
                    baseDao.flush();
                } else {
                    // update
                    impStock.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                    impStock.setUpdatedDate(ifIpInfo.getUpdatedDate());
                    impStock.setVersion(impStock.getVersion() + IntDef.INT_ONE);
                    // update
                    super.baseMapper.update(this.getSqlId("updateImpStockInfo"), impStock);
                }
            }
        }
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param ifIpInfo if ip information
     */
    private void prepareOrderStatusForInvoiceVV(IfIpEntity ifIpInfo) {
        // get imp stock information
        List<TnfOrderStatus> orderStatusList = this.getOrderStatusByInvoice(ifIpInfo, PROCESS_TYPE_VV);
        if (orderStatusList != null && !orderStatusList.isEmpty()) {
            for (TnfOrderStatus orderStatus : orderStatusList) {
                // if with clearance,reset on-shipping
                orderStatus.setExpOnshippingQty(DecimalUtil.subtract(BigDecimal.ZERO, orderStatus.getInRackQty()));

                // update
                orderStatus.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                orderStatus.setUpdatedDate(ifIpInfo.getUpdatedDate());
                // update
                super.baseMapper.update(this.getSqlId("updateOrderStatusInfo"), orderStatus);
            }
        }
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param ifIpInfo if ip information
     */
    private void prepareImpStockForModuleVV(IfIpEntity ifIpInfo) {
        // get imp stock information
        List<TnfImpStock> impStockList = this.getModulePartsInfoForVV(ifIpInfo);
        if (impStockList != null && !impStockList.isEmpty()) {
            for (TnfImpStock impStock : impStockList) {
                // check inRack Qty
                if (impStock.getImpStockId() == null) {
                    // add new
                    impStock.setSystemStockQty(impStock.getImpStockQty());
                    impStock.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                    impStock.setCreatedDate(ifIpInfo.getUpdatedDate());
                    impStock.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                    impStock.setUpdatedDate(ifIpInfo.getUpdatedDate());
                    impStock.setVersion(IntDef.INT_ONE);
                    // set into databae
                    baseDao.insert(impStock);
                    baseDao.flush();
                } else {
                    // update
                    impStock.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                    impStock.setUpdatedDate(ifIpInfo.getUpdatedDate());
                    impStock.setVersion(impStock.getVersion() + IntDef.INT_ONE);
                    // update
                    super.baseMapper.update(this.getSqlId("updateImpStockInfo"), impStock);
                }
            }
        }
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param ifIpInfo if ip information
     */
    private void prepareStockAndStatusForContAISIN(IfIpEntity ifIpInfo) {
        // get kanban list
        List<TntIpKanban> ipKanbanList = ifIpInfo.getIpKanbanList();
        // get imp stock information
        if (ipKanbanList != null && !ipKanbanList.isEmpty()) {
            for (TntIpKanban partsInfo : ipKanbanList) {
                // prepare list
                ifIpInfo.setPartsId(partsInfo.getPartsId());
                ifIpInfo.setSupplierId(partsInfo.getSupplierId());
                ifIpInfo.setExpPartsId(partsInfo.getExpPartsId());
                ifIpInfo.setQty(StringUtil.toDecimalString(partsInfo.getQty()));
                ifIpInfo.setKanbanPlanNo(partsInfo.getKanbanPlanNo());
                ifIpInfo.setBusinessPattern(BusinessPattern.AISIN);
                // check inRack Qty
                this.saveImpStock(ifIpInfo);
                this.updateTnfOrderStatus(ifIpInfo, PROCESS_TYPE_AISIN);
            }
        }
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param ifIpInfo if ip information
     */
    private void prepareStockAndStatusForModAISIN(IfIpEntity ifIpInfo) {
        // get kanban list
        List<TntIpKanban> ipKanbanList = ifIpInfo.getIpKanbanList();
        // get imp stock information
        if (ipKanbanList != null && !ipKanbanList.isEmpty()) {
            for (TntIpKanban partsInfo : ipKanbanList) {
                // prepare list
                ifIpInfo.setPartsId(partsInfo.getPartsId());
                ifIpInfo.setSupplierId(partsInfo.getSupplierId());
                ifIpInfo.setExpPartsId(partsInfo.getExpPartsId());
                ifIpInfo.setQty(StringUtil.toDecimalString(partsInfo.getQty()));
                ifIpInfo.setKanbanPlanNo(partsInfo.getKanbanPlanNo());
                ifIpInfo.setBusinessPattern(BusinessPattern.AISIN);
                // check inRack Qty
                this.saveImpStock(ifIpInfo);
                this.updateTnfOrderStatus(ifIpInfo, PROCESS_TYPE_AISIN);
            }
        }
    }

    /**
     * prepare OrderStatus For Module VV.
     * 
     * @param ifIpInfo if ip information
     */
    private void prepareOrderStatusForModuleVV(IfIpEntity ifIpInfo) {
        // get imp stock information
        List<TnfOrderStatus> orderStatusList = this.getOrderStatusByModuleVV(ifIpInfo);
        if (orderStatusList != null && !orderStatusList.isEmpty()) {
            for (TnfOrderStatus orderStatus : orderStatusList) {
                // check on-shipping
                if (ifIpInfo.getCcDate() == null) {
                    orderStatus
                        .setExpOnshippingQty(DecimalUtil.subtract(BigDecimal.ZERO, orderStatus.getImpStockQty()));
                }
                // update
                orderStatus.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                orderStatus.setUpdatedDate(ifIpInfo.getUpdatedDate());
                // update
                super.baseMapper.update(this.getSqlId("updateOrderStatusInfo"), orderStatus);
            }
        }
    }
}
