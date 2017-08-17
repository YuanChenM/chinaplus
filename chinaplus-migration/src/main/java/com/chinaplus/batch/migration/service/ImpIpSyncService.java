/**
 * CPIIFB16Service.java
 * 
 * @screen CPIIFB16
 * @author yang_jia1
 */
package com.chinaplus.batch.migration.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.MigrationIpStatus;
import com.chinaplus.batch.common.consts.BatchConst.ValidFlag;
import com.chinaplus.batch.migration.bean.TntMgImpIpEx;
import com.chinaplus.common.consts.CodeConst.ActionType;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.HandleFlag;
import com.chinaplus.common.consts.CodeConst.OnHoldFlag;
import com.chinaplus.common.entity.TntIfImpIp;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * CPIIFB16Service.
 * 
 * @author yang_jia1
 */
@Service
public class ImpIpSyncService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ImpIpSyncService.class);

    /**
     * do process imp ip.
     * 
     * @param ipInfoList ipInfoList
     * @param businessPattern businessPattern
     * @param dbTime dbTime
     * @param decantParentIPs decantParentIPs
     * @param devanWhsCodeMap devanWhsCodeMap
     * @param decantWhsCodeMap decantWhsCodeMap
     */
    public void doProcessImpIp(List<TntMgImpIpEx> ipInfoList, Integer businessPattern, Timestamp dbTime,
        List<String> decantParentIPs, Map<String, String> devanWhsCodeMap, Map<String, String> decantWhsCodeMap) {

        logger.info("----------------- doProcessImpIp Start ------------------");

        // define
        int count = 0;
        // if ip information
        List<TntIfImpIp> impIpInfoList = null;
        // get start index
        int ifIpId = this.getMaxIfIpId();
        Timestamp ifDateTime = super.baseMapper.getCurrentTime();

        // loop split them
        for (TntMgImpIpEx mgIpInfo : ipInfoList) {
            count++;
            // as other
            impIpInfoList = new ArrayList<TntIfImpIp>();
            // set business pattern
            mgIpInfo.setBusinessPattern(businessPattern);
            mgIpInfo.setIfDateTime(ifDateTime);
            // if current IP does not has any parent, means that does not has any devan, so we need do
            if (businessPattern.equals(BusinessPattern.AISIN)) {
                // do import inbound
                impIpInfoList.add(this.makeNewIfip(mgIpInfo, ActionType.IMP_INBOUND));
            } else {
                // re-prepare original whs code
                if (StringUtil.isEmpty(mgIpInfo.getOriginalWhsCode())) {
                    if (StringUtil.isEmpty(mgIpInfo.getParentPidNo())) {
                        mgIpInfo.setOriginalWhsCode(devanWhsCodeMap.get(this.parepareDevanModuleKey(mgIpInfo)));
                    } else {
                        mgIpInfo.setOriginalWhsCode(decantWhsCodeMap.get(mgIpInfo.getParentPidNo()));
                    }
                }
                
                // prepare imp inbound
                if (StringUtil.isEmpty(mgIpInfo.getParentPidNo())) {

                    // first, we need do make a in-bound information
                    impIpInfoList.add(this.makeNewIfip(mgIpInfo, ActionType.IMP_INBOUND));
                } else {

                    // means current PID is from decant
                    if (!decantParentIPs.contains(mgIpInfo.getParentPidNo())) {
                        impIpInfoList.add(this.makeNewIfip(mgIpInfo, ActionType.DECANT, true));
                        decantParentIPs.add(mgIpInfo.getParentPidNo());
                    }
                    // set
                    impIpInfoList.add(this.makeNewIfip(mgIpInfo, ActionType.DECANT, false));
                }

                // check need whs transfer
                if (mgIpInfo.getOriginalWhsCode() != null
                        && !mgIpInfo.getOriginalWhsCode().equals(mgIpInfo.getWhsCode())) {
                    // means has stock transfer, we need prepare a stock transfer information
                    impIpInfoList.add(this.makeNewIfip(mgIpInfo, ActionType.WHS_TRANSFER));
                }

                // check has stock adjust or not
                if (!DecimalUtil.isEquals(StringUtil.toBigDecimal(mgIpInfo.getOriginalQty()),
                    StringUtil.toBigDecimal(mgIpInfo.getQty()))) {

                    // means has stock transfer, we need prepare a stock transfer information
                    if (mgIpInfo.getAdjustmentDate() != null) {
                        impIpInfoList.add(this.makeNewIfip(mgIpInfo, ActionType.STOCK_ADJUST));
                    }
                }

                // check if has original customer code
                if (mgIpInfo.getOriginalCustCode() != null
                        && !mgIpInfo.getOriginalCustCode().equals(mgIpInfo.getCustomerCode())) {
                    // means has stock transfer, we need prepare a stock transfer information
                    impIpInfoList.add(this.makeNewIfip(mgIpInfo, ActionType.STOCK_TRANSFER));
                }
            }

            // if QTY becomes zero, and status is cancel, means current IP has been decant
            if (mgIpInfo.getStatus().equals(MigrationIpStatus.DELIVERY)) {

                // do outbound
                impIpInfoList.add(this.makeNewIfip(mgIpInfo, ActionType.IMP_OUTBOUND));
            } else if (mgIpInfo.getStatus().equals(MigrationIpStatus.ONHOLD)) {

                // do outbound
                impIpInfoList.add(this.makeNewIfip(mgIpInfo, ActionType.NG));
            } else if (mgIpInfo.getStatus().equals(MigrationIpStatus.ECI)) {

                // do outbound
                impIpInfoList.add(this.makeNewIfip(mgIpInfo, ActionType.ECI_ONHOLD));
            }

            // save
            ifIpId = this.saveDataIntoIfTable(impIpInfoList, dbTime, ifIpId);
            impIpInfoList = null;

            // print
            if (count % IntDef.INT_THOUSAND == 0) {
                logger.info("" + count);
            }
        }
        logger.info("----------------- doProcessImpIp End ------------------");
    }

    /**
     * make new if imp ip.
     * 
     * @param mgIpInfo mgIpInfo
     * @param actionType actionType
     * @return new if imp ip information
     */
    private TntIfImpIp makeNewIfip(TntMgImpIpEx mgIpInfo, int actionType) {
        return this.makeNewIfip(mgIpInfo, actionType, false);
    }

    /**
     * make new if imp ip.
     * 
     * @param mgIpInfo mgIpInfo
     * @param actionType actionType
     * @param isParent isParent
     * @return new if imp ip information
     */
    private TntIfImpIp makeNewIfip(TntMgImpIpEx mgIpInfo, int actionType, boolean isParent) {

        // prepare If IP information
        TntIfImpIp inbIp = new TntIfImpIp();

        // DATA SOURCE
        inbIp.setDataSource("WEST");
        // detail informations
        inbIp.setSeqNo(StringUtil.toString(IntDef.INT_ONE));
        // OFFICE_CODE
        inbIp.setOfficeCode(mgIpInfo.getOfficeCode());
        // source IP No
        inbIp.setSourceIpNo(mgIpInfo.getSourceIpNo());
        // PID_NO
        inbIp.setPidNo(mgIpInfo.getPidNo());
        // WHS_CODE
        inbIp.setWhsCode(mgIpInfo.getWhsCode());
        // TTC_PARTS_NO
        inbIp.setTtcPartsNo(mgIpInfo.getTtcPartsNo());
        // STATUS
        inbIp.setStatus(StringUtil.toString(mgIpInfo.getStatus()));
        // IF_DATE_TIME
        inbIp.setIfDateTime(mgIpInfo.getIfDateTime());
        // VALID_FLAG
        inbIp.setValidFlag(ValidFlag.OTHER);
        // HANDLE_FLAG
        inbIp.setHandleFlag(HandleFlag.UNPROCESS);

        // do prepare data
        switch (actionType) {

            case ActionType.IMP_INBOUND:

                // ACTION_TYPE
                inbIp.setActionType(String.valueOf(ActionType.IMP_INBOUND));
                // INB_JOB_NO
                inbIp.setInbJobNo(StringConst.BLANK);
                // INB_INVOICE_NO
                inbIp.setInbInvoiceNo(mgIpInfo.getInvoiceNo());
                // INB_MODULE_NO
                inbIp.setInbModuleNo(mgIpInfo.getModuleNo());
                // INB_TYPE
                inbIp.setInbType(StringConst.BLANK);
                // INB_DATE
                inbIp.setInbDate(mgIpInfo.getInboundDate());
                // set process date
                inbIp.setProcessDate(DateTimeUtil.parseDateTime(inbIp.getInbDate(), DateTimeUtil.FORMAT_IP_DATE));
                if (mgIpInfo.getBusinessPattern().equals(BusinessPattern.AISIN)) {
                    // no warehouse transfer and no stock adjustment
                    inbIp.setWhsCustomerCode(mgIpInfo.getCustomerCode());
                    inbIp.setQty(mgIpInfo.getQty());
                } else {
                    // set customer code
                    if (mgIpInfo.getOriginalCustCode() != null) {
                        inbIp.setWhsCustomerCode(mgIpInfo.getOriginalCustCode());
                    } else {
                        inbIp.setWhsCustomerCode(mgIpInfo.getCustomerCode());
                    }
                    // set QTY
                    if (mgIpInfo.getOriginalQty() != null) {
                        inbIp.setQty(mgIpInfo.getOriginalQty());
                    } else {
                        inbIp.setQty(mgIpInfo.getQty());
                    }
                    // WHS TRANSFER
                    if (mgIpInfo.getOriginalWhsCode() != null) {
                        inbIp.setWhsCode(mgIpInfo.getOriginalWhsCode());
                }
                }
                
                break;

            case ActionType.DECANT:

                // ACTION_TYPE
                inbIp.setActionType(String.valueOf(ActionType.DECANT));
                // INB_JOB_NO
                inbIp.setDecantDate(mgIpInfo.getInboundDate());
                // set customer code
                if (mgIpInfo.getParentPidNo() != null && mgIpInfo.getOriginalCustCode() != null) {
                    inbIp.setWhsCustomerCode(mgIpInfo.getOriginalCustCode());
                } else {
                    inbIp.setWhsCustomerCode(mgIpInfo.getCustomerCode());
                }
                // INB_INVOICE_NO
                if (!isParent) {
                    // set qty
                    inbIp.setFromIpNo(mgIpInfo.getParentPidNo());
                    // set QTY
                    if (mgIpInfo.getParentPidNo() != null && mgIpInfo.getOriginalQty() != null) {
                        inbIp.setQty(mgIpInfo.getOriginalQty());
                    } else {
                        inbIp.setQty(mgIpInfo.getQty());
                    }
                } else {
                    // reset
                    inbIp.setPidNo(mgIpInfo.getParentPidNo());
                    inbIp.setQty("0");
                    inbIp.setStatus("99");
                }
                // WHS TRANSFER
                if (mgIpInfo.getOriginalWhsCode() != null) {
                    inbIp.setWhsCode(mgIpInfo.getOriginalWhsCode());
                }
                // set process date
                inbIp.setProcessDate(DateTimeUtil.parseDateTime(inbIp.getDecantDate(), DateTimeUtil.FORMAT_IP_DATE));
                break;

            case ActionType.STOCK_ADJUST:

                // ACTION_TYPE
                inbIp.setActionType(String.valueOf(ActionType.STOCK_ADJUST));
                // INB_JOB_NO
                inbIp.setSaDate(mgIpInfo.getAdjustmentDate());
                // INB_INVOICE_NO
                inbIp.setSaQty(DecimalUtil.subtract(StringUtil.toBigDecimal(mgIpInfo.getQty()),
                    StringUtil.toBigDecimal(mgIpInfo.getOriginalQty())).toPlainString());
                // set process date
                inbIp.setProcessDate(DateTimeUtil.parseDateTime(inbIp.getSaDate(), DateTimeUtil.FORMAT_IP_DATE));
                // set customer code
                if (mgIpInfo.getOriginalCustCode() != null) {
                    inbIp.setWhsCustomerCode(mgIpInfo.getOriginalCustCode());
                } else {
                    inbIp.setWhsCustomerCode(mgIpInfo.getCustomerCode());
                }
                // set QTY
                inbIp.setQty(mgIpInfo.getQty());
                break;
            case ActionType.NG:

                // ACTION_TYPE
                inbIp.setActionType(String.valueOf(ActionType.NG));
                // INB_JOB_NO
                inbIp.setOnholdDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_IP_DATE, mgIpInfo.getIfDateTime()));
                // INB_INVOICE_NO
                inbIp.setOnholdFlag(String.valueOf(OnHoldFlag.NG_ON_HOLD));
                // set process date
                inbIp.setProcessDate(mgIpInfo.getIfDateTime());
                // set customer code
                inbIp.setWhsCustomerCode(mgIpInfo.getCustomerCode());
                // set QTY
                inbIp.setQty(mgIpInfo.getQty());
                break;
            case ActionType.ECI_ONHOLD:

                // ACTION_TYPE
                inbIp.setActionType(String.valueOf(ActionType.ECI_ONHOLD));
                // INB_JOB_NO
                inbIp.setOnholdDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_IP_DATE, mgIpInfo.getIfDateTime()));
                // INB_INVOICE_NO
                inbIp.setOnholdFlag(String.valueOf(OnHoldFlag.ECI_ON_HOLD));
                // set process date
                inbIp.setProcessDate(mgIpInfo.getIfDateTime());
                // set customer code
                inbIp.setWhsCustomerCode(mgIpInfo.getCustomerCode());
                // set QTY
                inbIp.setQty(mgIpInfo.getQty());
                break;
            case ActionType.STOCK_TRANSFER:

                // ACTION_TYPE
                inbIp.setActionType(String.valueOf(ActionType.STOCK_TRANSFER));
                if (mgIpInfo.getAdjustmentDate() != null) {
                    // after stock adjustment date
                    if (mgIpInfo.getSysObDateTime() != null) {
                        inbIp.setStockTransferDate(mgIpInfo.getSysObDateTime());
                    } else {
                        // cast just date
                        Date dateAdjust = DateTimeUtil.parseDate(mgIpInfo.getAdjustmentDate(),
                            DateTimeUtil.FORMAT_IP_DATE);
                        Date stockDate = DateTimeUtil.add(dateAdjust, 0, 0, 0, 0, 0, IntDef.INT_THIRTY);
                        inbIp.setStockTransferDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_IP_DATE, stockDate));
                    }
                } else if (mgIpInfo.getInboundDate() != null) {
                    // after stock adjustment date
                    if (mgIpInfo.getParentPidNo() == null) {
                        inbIp.setStockTransferDate(mgIpInfo.getInboundDate());
                    } else {
                        // cast just date
                        Date dateInbound = DateTimeUtil.parseDate(mgIpInfo.getInboundDate(),
                            DateTimeUtil.FORMAT_IP_DATE);
                        Date stockDate = DateTimeUtil.add(dateInbound, 0, 0, 0, 0, 0, IntDef.INT_SIXTY);
                        inbIp.setStockTransferDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_IP_DATE, stockDate));
                    }
                } else if (mgIpInfo.getSysObDateTime() != null) {
                    inbIp.setStockTransferDate(mgIpInfo.getSysObDateTime());
                } else {
                    inbIp.setStockTransferDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_IP_DATE,
                        mgIpInfo.getIfDateTime()));
                }
                // set
                inbIp.setFromCustomerCode(mgIpInfo.getOriginalCustCode());
                // set process date
                inbIp.setProcessDate(DateTimeUtil.parseDateTime(inbIp.getStockTransferDate(),
                    DateTimeUtil.FORMAT_IP_DATE));
                // set customer code
                inbIp.setWhsCustomerCode(mgIpInfo.getCustomerCode());
                // set QTY
                inbIp.setQty(mgIpInfo.getQty());
                break;
            case ActionType.WHS_TRANSFER:

                // ACTION_TYPE
                inbIp.setActionType(String.valueOf(ActionType.WHS_TRANSFER));
                // cast just date
                Date dateInbound = DateTimeUtil.parseDate(mgIpInfo.getInboundDate(), DateTimeUtil.FORMAT_IP_DATE);
                Date whsTransferDate = DateTimeUtil.add(dateInbound, 0, 0, 0, 0, 0, IntDef.INT_THIRTY);
                // set as inbound date
                inbIp.setWhsTransferDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_IP_DATE, whsTransferDate));
                // set
                inbIp.setFromWhsCode(mgIpInfo.getOriginalWhsCode());
                // set process date
                inbIp
                    .setProcessDate(DateTimeUtil.parseDateTime(inbIp.getWhsTransferDate(), DateTimeUtil.FORMAT_IP_DATE));
                // set customer code
                if (mgIpInfo.getOriginalCustCode() != null) {
                    inbIp.setWhsCustomerCode(mgIpInfo.getOriginalCustCode());
                } else {
                    inbIp.setWhsCustomerCode(mgIpInfo.getCustomerCode());
                }
                // set QTY
                if (mgIpInfo.getOriginalQty() != null) {
                    inbIp.setQty(mgIpInfo.getOriginalQty());
                } else {
                    inbIp.setQty(mgIpInfo.getQty());
                }
                break;
            case ActionType.IMP_OUTBOUND:

                // ACTION_TYPE
                inbIp.setActionType(String.valueOf(ActionType.IMP_OUTBOUND));
                // setDispatchedDatetime
                inbIp.setDispatchedDatetime(mgIpInfo.getSysObDateTime());
                // setOutboundDatetime
                inbIp.setOutboundDatetime(mgIpInfo.getActualObDateTime());
                // DELIVERY_NOTE_NO
                inbIp.setDeliveryNoteNo(StringConst.BLANK);
                // OUTBOUND_NO
                inbIp.setOutboundNo(StringConst.BLANK);
                // OUTBOUND_PKG_NO
                inbIp.setOutboundPkgNo(StringConst.BLANK);
                // OUTBOUND_TYPE
                inbIp.setOutboundType(StringConst.BLANK);
                // set process date
                inbIp.setProcessDate(DateTimeUtil.parseDateTime(inbIp.getDispatchedDatetime(),
                    DateTimeUtil.FORMAT_IP_DATE));
                // set customer code
                inbIp.setWhsCustomerCode(mgIpInfo.getCustomerCode());
                // set QTY
                inbIp.setQty(mgIpInfo.getQty());
                break;

            default:
                break;
        }

        return inbIp;
    }

    /**
     * Save into if imp ip information.
     * 
     * @param impIpInfoList impIpInfoList
     * @param dbTime dbTime
     * @param ifIpId ifIpId
     * @return index
     */
    private int saveDataIntoIfTable(List<TntIfImpIp> impIpInfoList, Timestamp dbTime, int ifIpId) {
        // return
        int index = ifIpId;
        // insert into
        for (TntIfImpIp ifIp : impIpInfoList) {
            // set into database
            ifIp.setIfIpId(index++);

            // set date
            ifIp.setCreatedBy(BatchConst.BATCH_USER_ID);
            ifIp.setCreatedDate(dbTime);
            ifIp.setUpdatedBy(BatchConst.BATCH_USER_ID);
            ifIp.setUpdatedDate(dbTime);
            ifIp.setVersion(NumberConst.IntDef.INT_ONE);

            // save
            // super.baseDao.insert(ifIp);
            this.baseMapper.insert(this.getSqlId("addTTLogicIPIf"), ifIp);
        }
        
        // return 
        return index;
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param param parameter
     * @param businessPattern business Pattern
     * @return inbound invoice qty
     */
    public List<TntMgImpIpEx> getPartsStockIPInfoList(TntMgImpIpEx param, int businessPattern) {
        if (businessPattern == BusinessPattern.V_V) {
            return baseMapper.selectList(this.getSqlId("getPartsStockIPInfoListForVV"), param);
        } else {
            return baseMapper.selectList(this.getSqlId("getPartsStockIPInfoListForAISIN"), param);
        }
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param param parameter
     * @return inbound invoice qty
     */
    public List<TntMgImpIpEx> getModuleDevanWhsCodeList(TntMgImpIpEx param) {
        return baseMapper.selectList(this.getSqlId("getModuleDevanWhsCodeList"), param);
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param param parameter
     * @return inbound invoice qty
     */
    public List<TntMgImpIpEx> getParentDecanWhsCodeList(TntMgImpIpEx param) {
        return baseMapper.selectList(this.getSqlId("getParentDecanWhsCodeList"), param);
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param param parameter
     * @return count
     */
    public Integer getPartsStockIPInfoCount(TntMgImpIpEx param) {
        return baseMapper.getSqlSession().selectOne(this.getSqlId("getPartsStockIPInfoCount"), param);
    }

    /**
     * Get max NotInRundown id.
     * 
     * @return NotInRundown id
     */
    private int getMaxIfIpId() {

        // get maxRundowId
        Integer maxIfIpId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxIfIpId"));

        // get form databse
        return maxIfIpId == null ? IntDef.INT_ONE : maxIfIpId.intValue();
    }

    /**
     * prepare devan module key.
     * 
     * @param info info
     * @return module key
     */
    public String parepareDevanModuleKey(TntMgImpIpEx info) {

        // prepare
        StringBuffer sb = new StringBuffer();
        sb.append(info.getOfficeCode());
        sb.append(StringConst.NEW_COMMA);
        sb.append(StringUtil.toSafeString(info.getInvoiceNo()));
        sb.append(StringConst.NEW_COMMA);
        sb.append(StringUtil.toSafeString(info.getModuleNo()));

        return sb.toString();
    }

}
