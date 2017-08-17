/**
 * CPIIFB16Service.java
 * 
 * @screen CPIIFB16
 * @author yang_jia1
 */
package com.chinaplus.batch.migration.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.batch.migration.bean.TntInvoiceContainerEx;
import com.chinaplus.batch.migration.bean.TntInvoiceEx;
import com.chinaplus.common.bean.IfIpEntity;
import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.CodeConst.ActionType;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.HandleFlag;
import com.chinaplus.common.consts.CodeConst.InvoiceStatus;
import com.chinaplus.common.consts.CodeConst.IpStatus;
import com.chinaplus.common.consts.CodeConst.OnHoldFlag;
import com.chinaplus.common.entity.TnfImpStock;
import com.chinaplus.common.entity.TnfOrderStatus;
import com.chinaplus.common.entity.TnmPartsMaster;
import com.chinaplus.common.entity.TnmWarehouse;
import com.chinaplus.common.entity.TntIfImpIp;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceHistory;
import com.chinaplus.common.entity.TntInvoicePart;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.common.entity.TntIp;
import com.chinaplus.core.base.BaseService;
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
public class ImpIpRepairForVvService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ImpIpRepairForVvService.class);

    /** logger */
    private Map<String, TntInvoiceSummary> invoiceMap = new HashMap<String, TntInvoiceSummary>();

    private Map<String, Integer> whsInfoMap = new HashMap<String, Integer>();

    /**
     * Get max RundowMaster id.
     * 
     * @param officeId Office ID
     * @throws Exception Exception
     * 
     */
    public void doImpIpRepair(Integer officeId) throws Exception {

        // start logger
        logger.info("Start of do Imp Ip Repair for VV.");

        // Repair TNT_IF_IMP_IP/TNT_IP (By Each Parts)
        this.doRepairIpInfo(officeId);

        // Repair Invoice information
        // include TNT_INVOCE_SUMMAY/TNT_INVOCE_CONTAINER/TNT_INVOCE/TNT_INVOCE_PARTS/INVOICE_HISTORY
        this.doRepairInvoiceInfo(officeId);

        // Repair TNF_ORDER_STATUS AND TNF_IMP_STOCK
        this.doRepairInventoryInfo(officeId);

        // Repair TNT_TRANSFER_OUT/TNT_TRANSFER_DELIVERY
        this.doRepairTransferInfo(officeId);

        // end logger
        logger.info("End of do Imp Ip Repair for VV.");
    }

    /**
     * Repair Imp IP information for TNT_IF_IMP_IP/TNT_IP.
     * 
     * @param officeId Office ID
     */
    public void doRepairIpInfo(Integer officeId) {
        logger.info("Start of do Repair Imp IP information for TNT_IF_IMP_IP/TNT_IP.");

        // get all for VV form parts master
        TnmPartsMaster partsParam = new TnmPartsMaster();
        partsParam.setOfficeId(officeId);
        partsParam.setBusinessPattern(BusinessPattern.V_V);
        List<TnmPartsMaster> vvPartsList = baseDao.select(partsParam);
        
        // check parts exists
        if (vvPartsList == null || vvPartsList.isEmpty()) {
            return;
        } else {
            logger.info("No. of vv parts : " + vvPartsList.size());
        }

        // prepare map
        Object[] param = new Object[] { officeId };
        List<TntInvoiceSummary> sumList = baseDao.select(
            "FROM TNT_INVOICE_SUMMARY A WHERE A.officeId = ? and postRiFlag > 0", param);
        // init
        for (TntInvoiceSummary summ : sumList) {
            invoiceMap.put(summ.getInvoiceNo(), summ);
        }

        // repair by each parts
        //TntIp ipParam = new TntIp();
        TntIfImpIp ifIpParam = new TntIfImpIp();
        int cntPart = 1;
        for (TnmPartsMaster partsInfo : vvPartsList) {

            // reset
            //ipParam.setPartsId(partsInfo.getPartsId());
            param = new Object[] { partsInfo.getPartsId() };
            String sql = "FROM TNT_IP A WHERE A.partsId = ? and (A.status < 10 or A.customsClrDate is null or A.devannedDate is null or (A.customsClrDate < A.expObActualDate) or (A.devannedDate < A.expObActualDate) or (A.impInbActualDate < A.expObActualDate))";

            // get TNT_IP information
            List<TntIp> ipList = baseDao.select(sql, param);
            if (ipList == null || ipList.isEmpty()) {
                continue;
            }

            // check
            logger.info("Ip No. size of current parts: " + ipList.size());
            int cntIp = 1;
            for (TntIp ipInfo : ipList) {

                // check invoice is post GR/GI
                if (invoiceMap.containsKey(ipInfo.getInvoiceNo())) {
                    continue;
                }

                // check IP is OK or not
                ifIpParam.setSourceIpNo(ipInfo.getIpNo());

                // get all IF information
                List<TntIfImpIp> ifIpInfoList = baseMapper.select(this.getSqlId("getIfIpInfoBySourceIpNo"), ifIpParam);

                // check and update
                this.analyzeIpStatus(ipInfo, ifIpInfoList);
                if (cntIp % IntDef.INT_THOUSAND == 0) {
                    logger.info("Already process Ip number: " + cntIp);
                }
                cntIp++;
            }
            logger.info("Already process Ip number: " + (cntIp));
            logger.info("Already process parts number: " + (cntPart++));
        }

        logger.info("End of do Repair Imp IP information for TNT_IF_IMP_IP/TNT_IP.");
    }

    /**
     * Repair Invoice information.
     * 
     * @param officeId Office ID
     */
    public void doRepairInvoiceInfo(Integer officeId) {
        logger.info("Start of do Repair Invoice information.");

        // update inbound qty of TNT_INVOICE_SUMMARY
        TntInvoiceSummary param = new TntInvoiceSummary();
        param.setOfficeId(officeId);
        List<TntInvoiceSummary> summaryList = baseMapper.select(this.getSqlId("selectInvoiceSummaryInfoList"), param);

        // do this
        if (summaryList != null) {
            // loop
            logger.info("Need Process Invoice: " + summaryList.size());
            int count = 1;
            for (TntInvoiceSummary summary : summaryList) {
                // check status
                summary.setOfficeId(officeId);
                if (summary.getInvoiceQty().compareTo(summary.getInboundQty()) <= 0) {
                    summary.setInvoiceStatus(InvoiceStatus.INBOUND_COMPLETED);
                } else {
                    summary.setInvoiceStatus(InvoiceStatus.PENDING);
                }
                // update inbound qty
                baseMapper.update(this.getSqlId("updateInvoiceSummary"), summary);

                // invoice parts
                // get inbound date for current invoice
                this.processInvoiceSplit(summary);
                
                logger.info("Areadly Process Invoice: " + ( count++));
            }
        }

        // update invoice
        // get cc information
        List<TntInvoiceEx> invList = baseMapper.selectList(this.getSqlId("selectInvoiceCc"), param);
        if (invList != null && !invList.isEmpty()) {
            for (TntInvoiceEx invbInfo : invList) {
                invbInfo
                    .setImpCcActualDate(DateTimeUtil.parseDate(invbInfo.getIfCcDate(), DateTimeUtil.FORMAT_IP_DATE));
                baseMapper.update(this.getSqlId("updateInvoiceCc"), invbInfo);
            }
        }
        // update invoice container
        // get cc information
        List<TntInvoiceContainerEx> invConList = null;
        invConList = baseMapper.selectList(this.getSqlId("selectInvoiceContainerCc"), param);
        if (invConList != null && !invConList.isEmpty()) {
            for (TntInvoiceContainerEx invbInfo : invConList) {
                invbInfo.setCcDate(DateTimeUtil.parseDate(invbInfo.getIfCcDate(), DateTimeUtil.FORMAT_IP_DATE));
                baseMapper.update(this.getSqlId("updateInvoiceContainerCc"), invbInfo);
            }
        }
        // get devan information
        invConList = baseMapper.selectList(this.getSqlId("selectInvoiceContainerDevan"), param);
        if (invConList != null && !invConList.isEmpty()) {
            for (TntInvoiceContainerEx invbInfo : invConList) {
                invbInfo
                    .setDevannedDate(DateTimeUtil.parseDate(invbInfo.getIfDevanDate(), DateTimeUtil.FORMAT_IP_DATE));
                baseMapper.update(this.getSqlId("updateInvoiceContainerDevan"), invbInfo);
            }
        }

        logger.info("End of do Repair Invoice information.");
    }

    /**
     * Repair Invoice information.
     * 
     * @param officeId officeId
     * @param invNoList invNoList
     */
    public void doRepairInvoiceInfoByInvoiceList(Integer officeId, List<String> invNoList) {
        logger.info("Start of do Repair Invoice information.");

        // update inbound qty of TNT_INVOICE_SUMMARY
        TntInvoiceEx param = new TntInvoiceEx();
        param.setOfficeId(officeId);
        param.setInvNoList(invNoList);
        List<TntInvoiceSummary> summaryList = baseMapper.selectList(
            this.getSqlId("selectInvoiceSummaryInfoListByInvList"), param);

        // do this
        if (summaryList != null) {
            // loop
            logger.info("Need Process Invoice: " + summaryList.size());
            int count = 1;
            for (TntInvoiceSummary summary : summaryList) {
                // check status
                summary.setOfficeId(officeId);
                if (summary.getInvoiceQty().compareTo(summary.getInboundQty()) <= 0) {
                    summary.setInvoiceStatus(InvoiceStatus.INBOUND_COMPLETED);
                } else {
                    summary.setInvoiceStatus(InvoiceStatus.PENDING);
                }
                // update inbound qty
                baseMapper.update(this.getSqlId("updateInvoiceSummary"), summary);

                // invoice parts
                // get inbound date for current invoice
                this.processInvoiceSplit(summary);
                
                logger.info("Areadly Process Invoice: " + ( count++));
            }
        }

        // update invoice
        // get cc information
        List<TntInvoiceEx> invList = baseMapper.selectList(this.getSqlId("selectInvoiceCc"), param);
        if (invList != null && !invList.isEmpty()) {
            for (TntInvoiceEx invbInfo : invList) {
                invbInfo
                    .setImpCcActualDate(DateTimeUtil.parseDate(invbInfo.getIfCcDate(), DateTimeUtil.FORMAT_IP_DATE));
                baseMapper.update(this.getSqlId("updateInvoiceCc"), invbInfo);
            }
        }
        // update invoice container
        // get cc information
        List<TntInvoiceContainerEx> invConList = null;
        invConList = baseMapper.selectList(this.getSqlId("selectInvoiceContainerCc"), param);
        if (invConList != null && !invConList.isEmpty()) {
            for (TntInvoiceContainerEx invbInfo : invConList) {
                invbInfo.setCcDate(DateTimeUtil.parseDate(invbInfo.getIfCcDate(), DateTimeUtil.FORMAT_IP_DATE));
                baseMapper.update(this.getSqlId("updateInvoiceContainerCc"), invbInfo);
            }
        }
        // get devan information
        invConList = baseMapper.selectList(this.getSqlId("selectInvoiceContainerDevan"), param);
        if (invConList != null && !invConList.isEmpty()) {
            for (TntInvoiceContainerEx invbInfo : invConList) {
                invbInfo
                    .setDevannedDate(DateTimeUtil.parseDate(invbInfo.getIfDevanDate(), DateTimeUtil.FORMAT_IP_DATE));
                baseMapper.update(this.getSqlId("updateInvoiceContainerDevan"), invbInfo);
            }
        }

        logger.info("End of do Repair Invoice information.");
    }

    /**
     * Repair inventory information for TNF_ORDER_STATUS/TNF_IMP_STOCK.
     * 
     * @param officeId Office ID
     */
    public void doRepairInventoryInfo(Integer officeId) {
        logger.info("Start of do Repair inventory information for TNF_ORDER_STATUS/TNF_IMP_STOCK.");

        // update TNF_IMP_STOCK
        TnfImpStock stockParam = new TnfImpStock();
        stockParam.setOfficeId(officeId);
        List<TnfImpStock> impStockList = baseMapper.select(this.getSqlId("selectTnfImpStock"), stockParam);
        if (impStockList != null && !impStockList.isEmpty()) {
            for (TnfImpStock impStock : impStockList) {
                baseMapper.update(this.getSqlId("updateImpStock"), impStock);
            }
        }

        // update TNF_ORDER_STATUS
        List<TnfOrderStatus> orderStatusList = baseMapper.selectList(this.getSqlId("selectTnfOrderStatus"), stockParam);
        if (orderStatusList != null && !orderStatusList.isEmpty()) {
            for (TnfOrderStatus orderStatus : orderStatusList) {
                baseDao.update(orderStatus);
            }

            // flush
            baseDao.flush();
        }

        logger.info("End of do Repair inventory information for TNF_ORDER_STATUS/TNF_IMP_STOCK.");
    }

    /**
     * Repair transfer out information.
     * 
     * @param officeId Office ID
     */
    public void doRepairTransferInfo(Integer officeId) {
        logger.info("Start of do Repair transfer out information.");

        logger.info("End of do Repair transfer out information.");
    }

    /**
     * Check and revise IP Status.
     * 
     * @param ipInfo ipInfo
     * @param ifIpInfoList ifIpInfoList
     */
    private void analyzeIpStatus(TntIp ipInfo, List<TntIfImpIp> ifIpInfoList) {

        // check list
        if (ifIpInfoList != null && !ifIpInfoList.isEmpty()) {
            // define
            Integer lastStatus = IpStatus.INVOICE;
            Integer lastOhFlag = OnHoldFlag.NORMAL;
            // Date lastOhDate = null;

            // String lastCustomer = null;
            // String lastWhsCode = null;
            // Date lastWtDate = null;
            // Date lastStDate = null;

            // Date lastSaDate = null;
            // BigDecimal totalSaQty = null;
            Integer actionType = null;
            int upCount = IntDef.INT_ZERO;
            boolean changeFlag = false;
            for (TntIfImpIp ifIpInfo : ifIpInfoList) {
                // reset
                changeFlag = false;
                actionType = StringUtil.toInteger(ifIpInfo.getActionType());

                // if process success
                if (actionType.compareTo(ActionType.DEVANNED) > IntDef.INT_ZERO) {
                    if (ipInfo.getPidNo() != null && !ipInfo.getPidNo().equals(ifIpInfo.getPidNo())) {
                        continue;
                    } else if (ifIpInfo.getHandleFlag().equals(HandleFlag.PROCESS_SUCCESS)) {
                        lastStatus = this.chagneATToIpStatus(lastStatus, actionType);
                        continue;
                    }
                }

                // check custom clearance
                switch (actionType.intValue()) {
                    case ActionType.CUSTOMS_CLEARANCE:
                        if (lastStatus.compareTo(IpStatus.CUSTOMS_CLEARANCE) < IntDef.INT_ZERO) {
                            lastStatus = IpStatus.CUSTOMS_CLEARANCE;
                            // check IP Information
                            if (ipInfo.getCustomsClrDate() == null) {
                                ipInfo.setCustomsClrDate(ifIpInfo.getProcessDate());
                                if (ipInfo.getWhsId() == null) {
                                    ipInfo.setWhsId(this.getWhsIdByCode(ifIpInfo.getWhsCode()));
                                }
                                upCount++;
                            }
                            // check handle flag
                            if (!ifIpInfo.getHandleFlag().equals(HandleFlag.PROCESS_SUCCESS)) {
                                ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                                changeFlag = true;
                            }
                        } else {
                            // check handle flag
                            if (ifIpInfo.getHandleFlag().equals(HandleFlag.UNPROCESS)) {
                                ifIpInfo.setHandleFlag(HandleFlag.ALREADY_IMP_INBOUND);
                                changeFlag = true;
                            }
                        }
                        break;
                    // check devan
                    case ActionType.DEVANNED:
                        if (lastStatus.compareTo(IpStatus.DEVANNED) < IntDef.INT_ZERO) {
                            lastStatus = IpStatus.DEVANNED;
                            // check IP Information
                            if (ipInfo.getDevannedDate() == null
                                    || !ipInfo.getDevannedDate().equals(this.changeToDate(ifIpInfo.getProcessDate()))) {
                                ipInfo.setDevannedDate(ifIpInfo.getProcessDate());
                                if (ipInfo.getWhsId() == null) {
                                    ipInfo.setWhsId(this.getWhsIdByCode(ifIpInfo.getWhsCode()));
                                }
                                upCount++;
                            }
                            // check handle flag
                            if (!ifIpInfo.getHandleFlag().equals(HandleFlag.PROCESS_SUCCESS)) {
                                ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                                changeFlag = true;
                            }
                        } else {
                            // check handle flag
                            if (ifIpInfo.getHandleFlag().equals(HandleFlag.UNPROCESS)) {
                                ifIpInfo.setHandleFlag(HandleFlag.ALREADY_IMP_INBOUND);
                                changeFlag = true;
                            }
                        }
                        break;
                    // check inbound
                    case ActionType.IMP_INBOUND:
                        if (lastStatus.compareTo(IpStatus.IMP_INBOUND) < IntDef.INT_ZERO) {
                            lastStatus = IpStatus.IMP_INBOUND;
                            // check IP Information
                            if (ipInfo.getImpInbActualDate() == null) {
                                ipInfo.setImpInbActualDate(ifIpInfo.getProcessDate());
                                ipInfo.setPidNo(ifIpInfo.getPidNo());
                                if (ipInfo.getWhsId() == null) {
                                    ipInfo.setWhsId(this.getWhsIdByCode(ifIpInfo.getWhsCode()));
                                }
                                upCount++;
                            }
                            // check handle flag
                            ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                            changeFlag = true;
                        } else {
                            // check handle flag
                            if (ifIpInfo.getHandleFlag().equals(HandleFlag.UNPROCESS)) {
                                ifIpInfo.setHandleFlag(HandleFlag.ALREADY_IMP_INBOUND);
                                changeFlag = true;
                            }
                        }
                        break;
                    // check warehouse transfer
                    case ActionType.STOCK_TRANSFER:
                        if (lastStatus.compareTo(IpStatus.IMP_INBOUND) < IntDef.INT_ZERO
                                || lastStatus.compareTo(IpStatus.IMP_OUTBOUND) >= IntDef.INT_ZERO
                                || !lastOhFlag.equals(OnHoldFlag.NORMAL)) {
                            // check handle flag
                            if (ifIpInfo.getHandleFlag().equals(HandleFlag.UNPROCESS)) {
                                ifIpInfo.setHandleFlag(HandleFlag.LOGIC_CHECK_NG);
                                changeFlag = true;
                            }
                        } else {
                            // if not customer can not get
                            if (!ifIpInfo.getHandleFlag().equals(HandleFlag.CUSTOMER_ISNOT_EXIST)
                                    || ifIpInfo.getHandleFlag().equals(HandleFlag.SUPPLIER_PARTS_ISNOT_EXIST)) {
                                // set status
                                lastStatus = IpStatus.STOCK_TRANSFER;
                                // prepare date
                                //lastStDate = this.changeToDate(ifIpInfo.getProcessDate());
                                // prepare date
                                //lastCustomer = ifIpInfo.getWhsCustomerCode();
                                // check handle flag
                                ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                                changeFlag = true;
                            }
                        }
                        break;
                    // check stock transfer
                    case ActionType.WHS_TRANSFER:
                        if (lastStatus.compareTo(IpStatus.IMP_INBOUND) < IntDef.INT_ZERO
                                || lastStatus.compareTo(IpStatus.IMP_OUTBOUND) >= IntDef.INT_ZERO
                                || !lastOhFlag.equals(OnHoldFlag.NORMAL)) {
                            // check handle flag
                            if (ifIpInfo.getHandleFlag().equals(HandleFlag.UNPROCESS)) {
                                ifIpInfo.setHandleFlag(HandleFlag.LOGIC_CHECK_NG);
                                changeFlag = true;
                            }
                        } else {
                            // set status
                            lastStatus = IpStatus.WHS_TRANSFER;
                            // check IP Information
                            // lastWtDate = this.changeToDate(ifIpInfo.getProcessDate());
                            // prepare whs Code
                            // lastWhsCode = ifIpInfo.getWhsCode();
                            // check handle flag
                            ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                            changeFlag = true;
                        }
                        break;
                    // check NG/ On-Hold
                    case ActionType.NG:
                    case ActionType.ECI_ONHOLD:
                        if (lastStatus.compareTo(IpStatus.IMP_INBOUND) < IntDef.INT_ZERO
                                || lastStatus.compareTo(IpStatus.IMP_OUTBOUND) >= IntDef.INT_ZERO
                                || !lastOhFlag.equals(OnHoldFlag.NORMAL)) {
                            // check handle flag
                            if (ifIpInfo.getHandleFlag().compareTo(HandleFlag.PROCESS_SUCCESS) < IntDef.INT_ZERO) {
                                ifIpInfo.setHandleFlag(HandleFlag.LOGIC_CHECK_NG);
                                changeFlag = true;
                            }
                        } else {
                            // check IP Information
                            //lastOhDate = this.changeToDate(ifIpInfo.getProcessDate());
                            lastOhFlag = StringUtil.toInteger(ifIpInfo.getOnholdFlag());
                            // check handle flag
                            ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                            changeFlag = true;
                        }
                        break;
                    // check Stock Adjust
                    case ActionType.STOCK_ADJUST:
                        if (lastStatus.compareTo(IpStatus.IMP_INBOUND) < IntDef.INT_ZERO
                                || lastStatus.compareTo(IpStatus.IMP_OUTBOUND) >= IntDef.INT_ZERO
                                || !lastOhFlag.equals(OnHoldFlag.NORMAL)) {
                            // check handle flag
                            if (ifIpInfo.getHandleFlag().compareTo(HandleFlag.PROCESS_SUCCESS) <= IntDef.INT_ZERO) {
                                ifIpInfo.setHandleFlag(HandleFlag.LOGIC_CHECK_NG);
                                changeFlag = true;
                            }
                        } else {
                            // check IP Information
                            // lastSaDate = this.changeToDate(ifIpInfo.getProcessDate());
                            // totalSaQty = DecimalUtil.add(totalSaQty, DecimalUtil.getBigDecimal(ifIpInfo.getSaQty()));
                            // check handle flag
                            ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                            changeFlag = true;
                        }
                        break;
                    // check Stock Adjust
                    case ActionType.DECANT:
                        // check is parent not not
                        if (ifIpInfo.getFromIpNo() == null) {
                            // parent
                            if (lastStatus.compareTo(IpStatus.IMP_INBOUND) < IntDef.INT_ZERO
                                    || lastStatus.compareTo(IpStatus.IMP_OUTBOUND) >= IntDef.INT_ZERO
                                    || !lastOhFlag.equals(OnHoldFlag.NORMAL)) {
                                // check handle flag
                                ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                                changeFlag = true;
                            } else {
                                // TODO
                                // check on hold flag
                                lastStatus = IpStatus.CANCELLED;
                                // check IP Information
                                if (ipInfo.getImpDecantDatetime() == null) {
                                    ipInfo.setImpDecantDatetime(ifIpInfo.getProcessDate());
                                    ipInfo.setQty(BigDecimal.ZERO);
                                    upCount++;
                                }
                                // check handle flag
                                ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                                changeFlag = true;
                            }
                        } else {
                            // TODO
                            lastStatus = IpStatus.DECANT;
                            // check handle flag
                            ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                            changeFlag = true;
                        }
                        break;
                    // release on hold
                    case ActionType.RELEASE_ONHOLD:
                        if (lastStatus.compareTo(IpStatus.IMP_INBOUND) < IntDef.INT_ZERO
                                || lastStatus.compareTo(IpStatus.IMP_OUTBOUND) >= IntDef.INT_ZERO
                                || lastOhFlag.equals(OnHoldFlag.NORMAL)) {
                            // check handle flag
                            if (ifIpInfo.getHandleFlag().compareTo(HandleFlag.PROCESS_SUCCESS) <= IntDef.INT_ZERO) {
                                ifIpInfo.setHandleFlag(HandleFlag.LOGIC_CHECK_NG);
                                changeFlag = true;
                            }
                        } else {
                            // check IP Information
                            //lastOhDate = this.changeToDate(ifIpInfo.getProcessDate());
                            lastOhFlag = StringUtil.toInteger(ifIpInfo.getOnholdFlag());
                            // check handle flag
                            ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                            changeFlag = true;
                        }
                        break;
                    // outbound
                    case ActionType.IMP_OUTBOUND:
                        // if (lastStatus.compareTo(IpStatus.IMP_OUTBOUND) < IntDef.INT_ZERO && lastOhFlag.equals(OnHoldFlag.NORMAL)) {
                        if (lastStatus.compareTo(IpStatus.IMP_OUTBOUND) < IntDef.INT_ZERO) {
                            lastStatus = IpStatus.IMP_OUTBOUND;
                            // check IP Information
                            if (ipInfo.getImpDispatchedDatetime() == null) {
                                ipInfo.setImpDispatchedDatetime(ifIpInfo.getProcessDate());
                                ipInfo.setImpOutboundDatetime(ifIpInfo.getProcessDate());
                                ipInfo.setImpObActualDate(DateTimeUtil.parseDate(ifIpInfo.getOutboundDatetime(),
                                    DateTimeUtil.FORMAT_IP_DATE));
                                upCount++;
                            }
                            // check handle flag
                            ifIpInfo.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                            changeFlag = true;
                        } else {
                            // check handle flag
                            if (ifIpInfo.getHandleFlag().compareTo(HandleFlag.PROCESS_SUCCESS) <= IntDef.INT_ZERO) {
                                ifIpInfo.setHandleFlag(HandleFlag.LOGIC_CHECK_NG);
                                changeFlag = true;
                            }
                        }
                        break;
                }
                // if change
                if (changeFlag) {
                    // add
                    baseMapper.update(this.getSqlId("updateInfoForIfIp"), ifIpInfo);
                }
            }

            // check last status
            if (!lastStatus.equals(ipInfo.getStatus())) {
                if (!lastStatus.equals(IpStatus.IMP_OUTBOUND)
                        || !ipInfo.getStatus().equals(IpStatus.IMP_ACTUAL_OUTBOUND)) {
                    ipInfo.setStatus(lastStatus);
                    this.clearDateIfNoChange(ipInfo);
                    upCount++;
                }
            }

            // check IP information is change or not
            if (upCount > IntDef.INT_ZERO) {
                // set into list
                baseDao.update(ipInfo);
            }
        }
    }

    /**
     * clear date.
     * 
     * @param ipInfo ipInfo
     */
    private void clearDateIfNoChange(TntIp ipInfo) {
        if (ipInfo.getStatus() != null) {
            switch (ipInfo.getStatus().intValue()) {
                
                case IpStatus.EXP_OUTBOUND:
                case IpStatus.INVOICE:
                    ipInfo.setCustomsClrDate(null);
                    ipInfo.setDevannedDate(null);
                    ipInfo.setImpInbActualDate(null);
                    ipInfo.setImpObActualDate(null);
                    ipInfo.setImpOutboundDatetime(null);
                    break;
                case IpStatus.CUSTOMS_CLEARANCE:
                    ipInfo.setDevannedDate(null);
                    ipInfo.setImpInbActualDate(null);
                    ipInfo.setImpObActualDate(null);
                    ipInfo.setImpOutboundDatetime(null);
                    break;
                case IpStatus.DEVANNED:
                    ipInfo.setImpInbActualDate(null);
                    ipInfo.setImpObActualDate(null);
                    ipInfo.setImpOutboundDatetime(null);
                    break;
                case IpStatus.IMP_INBOUND:
                    ipInfo.setImpObActualDate(null);
                    ipInfo.setImpOutboundDatetime(null);
                    break;
                case IpStatus.IMP_OUTBOUND:
                case IpStatus.IMP_ACTUAL_OUTBOUND:
                case IpStatus.CANCELLED:
                    break;
                default:
                    ipInfo.setImpObActualDate(null);
                    ipInfo.setImpOutboundDatetime(null);
                    break;
            }
        }
    }

    /**
     * change timestamp to date.
     * 
     * @param time timestamp
     * @return date
     */
    private Date changeToDate(Timestamp time) {
        return DateTimeUtil.parseDate(DateTimeUtil.formatDate(time));
    }

    /**
     * Change action Type to Ip Status.
     * 
     * @param orgStatus orgStatus
     * @param actionType actionType
     * @return Ip Status
     */
    private Integer chagneATToIpStatus(Integer orgStatus, Integer actionType) {

        switch (actionType.intValue()) {
            case ActionType.CUSTOMS_CLEARANCE:
                return IpStatus.CUSTOMS_CLEARANCE;
            case ActionType.DEVANNED:
                return IpStatus.DEVANNED;
            case ActionType.IMP_INBOUND:
                return IpStatus.IMP_INBOUND;
            case ActionType.WHS_TRANSFER:
                return IpStatus.WHS_TRANSFER;
            case ActionType.STOCK_TRANSFER:
                return IpStatus.STOCK_TRANSFER;
            case ActionType.STOCK_ADJUST:
                return IpStatus.STOCK_ADJUST;
            case ActionType.DECANT:
                return IpStatus.DECANT;
            case ActionType.IMP_OUTBOUND:
                return IpStatus.IMP_OUTBOUND;
        }

        return orgStatus;
    }

    /**
     * split invoice parts.
     * 
     * @param summary summary
     */
    private void processInvoiceSplit(TntInvoiceSummary summary) {

        // get all exist invoice id
        Object[] param = new Object[] { summary.getInvoiceSummaryId() };
        List<TntInvoice> invList = baseDao.select(
            "FROM TNT_INVOICE A WHERE A.invoiceSummaryId = ? order by A.invoiceId", param);

        // invocie id
        if (invList.size() > IntDef.INT_ONE) {

            for (int i = 0; i < invList.size(); i++) {
                if (i > 0) {
                    // do delete
                    baseMapper.delete(this.getSqlId("deleteInvoice"), invList.get(i));
                    baseMapper.delete(this.getSqlId("deleteInvoiceParts"), invList.get(i));
                    baseMapper.delete(this.getSqlId("deleteInvoiceHistory"), invList.get(i));
                } else {
                    // do revise
                    baseMapper.update(this.getSqlId("reviseInvoice"), invList.get(i));
                    baseMapper.update(this.getSqlId("reviseInvoiceParts"), invList.get(i));
                }
            }
        }

        // get invoice Id
        Integer invoiceId = invList.get(0).getInvoiceId();

        // get IP entity list
        List<IfIpEntity> ifIpEntityList = baseMapper.selectList(this.getSqlId("selectPartialInboundInvoiceForVV"),
            summary);
        // loop
        Map<Date, Map<String, IfIpEntity>> partialInvoiceMap = new HashMap<Date, Map<String, IfIpEntity>>();
        List<Date> inbDateList = new ArrayList<Date>();
        if (ifIpEntityList != null) {
            for (IfIpEntity ifIpEntity : ifIpEntityList) {
                Map<String, IfIpEntity> ifIpListMap = partialInvoiceMap.get(ifIpEntity.getImpInbActualDate());
                if (ifIpListMap == null) {
                    ifIpListMap = new HashMap<String, IfIpEntity>();
                    partialInvoiceMap.put(ifIpEntity.getImpInbActualDate(), ifIpListMap);
                    inbDateList.add(ifIpEntity.getImpInbActualDate());
                }
                // get Key
                StringBuffer partsKey = new StringBuffer("");
                partsKey.append(ifIpEntity.getImpPoNo());
                partsKey.append(StringConst.UNDERLINE);
                partsKey.append(ifIpEntity.getExpPoNo());
                partsKey.append(StringConst.UNDERLINE);
                partsKey.append(ifIpEntity.getPartsId());
                partsKey.append(StringConst.UNDERLINE);
                partsKey.append(ifIpEntity.getSupplierId());
                ifIpListMap.put(partsKey.toString(), ifIpEntity);
            }
        }

        // check
        if (summary.getInvoiceStatus().equals(InvoiceStatus.INBOUND_COMPLETED)
                && partialInvoiceMap.size() <= IntDef.INT_ONE) {
            // revise
            return;
        }

        // sort
        Collections.sort(inbDateList);

        // check
        Date lastInbDate = null;
        if (summary.getInvoiceStatus().equals(InvoiceStatus.INBOUND_COMPLETED)) {
            lastInbDate = inbDateList.get(inbDateList.size() - IntDef.INT_ONE);
            inbDateList.remove(inbDateList.size() - IntDef.INT_ONE);
        }

        // do invoice split
        Timestamp dbTime = getDBDateTimeByDefaultTimezone();
        // Loop each Invoice(already split by Imp Inbound Actual Date)
        for (Date inbDate : inbDateList) {
            // Get latest update Invoice information from TNT_INVOICE
            param = new Object[] { invoiceId };
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
            invoiceInfo.setImpInbActualDate(inbDate);
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
            updateAndCopyInvoiceParts(invoiceInfo, partialInvoiceMap.get(inbDate), newInvoiceId, dbTime);

            // flush
            baseDao.flush();

            // reset invoice id
            invoiceId = newInvoiceId;
        }

        // update inbound actual date
        if (lastInbDate != null && invoiceId != null) {

            // update inbound actual date
            TntInvoice invoiceInfo = baseDao.findOne(TntInvoice.class, invoiceId);
            invoiceInfo.setImpInbActualDate(lastInbDate);
            invoiceInfo.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
            invoiceInfo.setCreatedDate(dbTime);
            invoiceInfo.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            invoiceInfo.setUpdatedDate(dbTime);
            invoiceInfo.setVersion(invoiceInfo.getVersion() + IntDef.INT_ONE);

            // update
            baseDao.update(invoiceInfo);

            // flush
            baseDao.flush();
        }
    }
    
    /**
     * getWhsIdByCode
     * 
     * @param whsCode whsCode
     * @return whsId
     */
    private Integer getWhsIdByCode(String whsCode) {
        
        if (whsInfoMap == null || whsInfoMap.isEmpty()) {
            List<TnmWarehouse> whsList = baseDao.select(new TnmWarehouse());
            
            if (whsList != null) {
                for (TnmWarehouse whsInfo : whsList) {
                    whsInfoMap.put(whsInfo.getWhsCode(), whsInfo.getWhsId());
                }
            }
        }
        
        return whsInfoMap.get(whsCode);
    }

    /**
     * Update and Copy Invoice Parts.
     * 
     * @param invoiceInfo Invoice Info
     * @param updatePartsMap Update Parts Map
     * @param newInvoiceId New Invoice Id
     * @param dbTime dbTime
     */
    private void updateAndCopyInvoiceParts(TntInvoice invoiceInfo, Map<String, IfIpEntity> updatePartsMap,
        Integer newInvoiceId, Timestamp dbTime) {
        // select invoice parts
        Object[] param = new Object[] { invoiceInfo.getInvoiceId() };
        String hql = "FROM TNT_INVOICE_PARTS A WHERE A.invoiceId = ?";
        List<TntInvoicePart> invoicePartsList = baseDao.select(hql, param);
        if (invoicePartsList != null) {
            for (TntInvoicePart invoiceParts : invoicePartsList) {
                // get Key
                StringBuffer partsKey = new StringBuffer("");
                partsKey.append(invoiceParts.getImpPoNo());
                partsKey.append(StringConst.UNDERLINE);
                partsKey.append(invoiceParts.getExpPoNo());
                partsKey.append(StringConst.UNDERLINE);
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

}
