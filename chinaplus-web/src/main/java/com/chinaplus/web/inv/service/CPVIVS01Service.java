/**
 * CPVIVS01Service.java
 * 
 * @screen CPVIVS01
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.AccessLevel;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.InvoiceIrregularsStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceIssueType;
import com.chinaplus.common.consts.CodeConst.InvoiceRiStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceType;
import com.chinaplus.common.consts.CodeConst.InvoiceUploadStatus;
import com.chinaplus.common.consts.CodeConst.KanbanPartsStatus;
import com.chinaplus.common.consts.CodeConst.KbsCompletedFlag;
import com.chinaplus.common.consts.CodeConst.KbsNirdFlag;
import com.chinaplus.common.consts.CodeConst.PlanType;
import com.chinaplus.common.consts.CodeConst.PostRiFlag;
import com.chinaplus.common.consts.CodeConst.TransportMode;
import com.chinaplus.common.entity.TnfOrderStatus;
import com.chinaplus.common.entity.TntKanbanPlan;
import com.chinaplus.common.entity.TntKanbanPlanPart;
import com.chinaplus.common.entity.TntKanbanShipping;
import com.chinaplus.common.entity.TntKanbanShippingPart;
import com.chinaplus.common.service.InvoiceAdjService;
import com.chinaplus.common.service.PostGrGiService;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inv.control.CPVIVF11Controller;
import com.chinaplus.web.inv.entity.CPVIVS01AdjustEntity;
import com.chinaplus.web.inv.entity.CPVIVS01Entity;
import com.chinaplus.web.inv.entity.CPVIVS01IrregularEntity;

/**
 * Invoice Screen Service.
 */
@Service
public class CPVIVS01Service extends BaseService {

    /** Invoice Cancel reason */
    private static final String INVOICE_CANCEL_REASON = "发票撤销调整";

    /** SQL ID: find draft count */
    private static final String SQLID_FIND_DRAFT_COUNT = "findDraftCount";

    /** SQL ID: find invoice by upload ID */
    private static final String SQLID_FIND_INVOICE_BY_UPLOAD_ID = "findInvoiceByUploadId";

    /** SQL ID: find inbound count */
    private static final String SQLID_FIND_INBOUND_COUNT = "findInboundCount";

    /** SQL ID: find actual CC count */
    private static final String SQLID_FIND_ACTUAL_CC_COUNT = "findActualCCCount";

    /** SQL ID: find cancelled count */
    private static final String SQLID_FIND_CANCELLED_COUNT = "findCancelledCount";

    /** SQL ID: find approve status count */
    private static final String SQLID_FIND_APPROVE_STATUS_COUNT = "findApproveStatusCount";

    /** SQL ID: cancel invoice summary */
    private static final String SQLID_CANCEL_INVOICE_SUMMARY = "cancelInvoiceSummary";

    /** SQL ID: approve invoice summary */
    private static final String SQLID_APPROVE_INVOICE_SUMMARY = "approveInvoiceSummary";

    /** SQL ID: find approve parts */
    private static final String SQLID_FIND_APPROVE_PARTS = "findApproveParts";

    /** SQL ID: find approve invoice ID */
    private static final String SQLID_FIND_APPROVE_INVOICE_ID = "findApproveInvoiceId";

    /** SQL ID: find cancelled parts */
    private static final String SQLID_FIND_CANCELLED_PARTS = "findCancelledParts";

    /** SQL ID: find irregular parts */
    private static final String SQLID_FIND_IRREGULAR_PARTS = "findIrregularParts";

    /** SQL ID: find cancelled invoices */
    private static final String SQLID_FIND_CANCELLED_INVOICES = "findCancelledInvoices";

    /** SQL ID: find cancel adjust KANBAN plan */
    private static final String SQLID_FIND_CANCEL_ADJUST_KANBAN_PLAN = "findCancelAdjustKanbanPlan";

    /** SQL ID: find exist latest plan */
    private static final String SQLID_FIND_EXIST_LATEST_PLAN = "findExistLatestPlan";

    /** SQL ID: cancel invoice shipping */
    private static final String SQLID_CANCEL_INVOICE_SHIPPING = "cancelInvoiceShipping";

    /** SQL ID: cancel invoice group */
    private static final String SQLID_CANCEL_INVOICE_GROUP = "cancelInvoiceGroup";

    /** Condition:invoiceNo */
    private static final String CONDITION_INVOICE_NO = "invoiceNo";

    /** Condition:uploadId */
    private static final String CONDITION_UPLOAD_ID = "uploadId";

    /** Condition:uploadReceiveTime */
    private static final String CONDITION_UPLOAD_RECEIVE_TIME = "uploadReceiveTime";

    /** Qty Type: Plan */
    private static final int QTY_TYPE_PLAN = 0;

    /** Qty Type: Difference */
    private static final int QTY_TYPE_DIFF = 1;

    /** Post GR/GI Service */
    @Autowired
    private PostGrGiService postGrGiService;

    /** Invoice Upload Service */
    @Autowired
    private CPVIVF11Service cpvivf11Service;

    /** Invoice Adjust Service */
    @Autowired
    private InvoiceAdjService invoiceAdjService;

    /**
     * Get invoice list.
     * 
     * @param param page parameter
     * @param accessLevel access level
     * @return invoice list
     */
    public PageResult<CPVIVS01Entity> getInvoiceList(PageParam param, Integer accessLevel) {

        // Search invoices
        StringUtil.buildLikeCondition(param, CONDITION_INVOICE_NO);
        StringUtil.buildLikeCondition(param, CONDITION_UPLOAD_ID);
        StringUtil.buildDateTimeCondition(param, CONDITION_UPLOAD_RECEIVE_TIME);
        PageResult<CPVIVS01Entity> result = super.getPageList(param);

        // Deal with search result
        List<CPVIVS01Entity> invoiceList = result.getDatas();
        if (invoiceList != null && invoiceList.size() > 0) {
            for (CPVIVS01Entity invoice : invoiceList) {
                // Business Pattern
                int businessPattern = DecimalUtil.integerToInt(invoice.getBusinessPattern());
                // Invoice Type
                int invoiceType = DecimalUtil.integerToInt(invoice.getInvoiceType());
                // Invoice Status
                int invoiceStatus = DecimalUtil.integerToInt(invoice.getInvoiceStatus());
                // Post GR/GI Flag
                int postRIFlag = DecimalUtil.integerToInt(invoice.getPostRIFlag());
                // Irregular Status
                int irregularStatus = DecimalUtil.integerToInt(invoice.getIrregularStatus());
                // Upload Status
                int uploadStatus = DecimalUtil.integerToInt(invoice.getUploadStatus());

                // Irregular Shipping Schedule Update Status
                if (InvoiceIrregularsStatus.DRAFT == irregularStatus && InvoiceStatus.CANCELLED != invoiceStatus
                        && accessLevel >= AccessLevel.MEDIUM_LOW) {
                    invoice.setHasIrregularLink(true);
                }

                // Invoice Uploading Status
                if (InvoiceUploadStatus.DRAFT == uploadStatus
                        && InvoiceIrregularsStatus.DONE == invoice.getGroupIrregularStatus()
                        && InvoiceStatus.CANCELLED != invoiceStatus) {
                    invoice.setHasUploadLink(true);
                }

                // Post GR/GI
                int postRIStatus = InvoiceRiStatus.BLANK;
                if (BusinessPattern.V_V == businessPattern && InvoiceType.MANUAL == invoiceType) {
                    postRIStatus = InvoiceRiStatus.NOT_ALLOW;
                } else if (postRIFlag >= PostRiFlag.Y) {
                    postRIStatus = InvoiceRiStatus.POSTED;
                } else if (DecimalUtil.isGreater(invoice.getInboundQty(), new BigDecimal(0))
                        || IntDef.INT_ONE == invoice.getCcStatus() || InvoiceIrregularsStatus.DRAFT == irregularStatus
                        || InvoiceUploadStatus.DRAFT == uploadStatus) {
                    postRIStatus = InvoiceRiStatus.NOT_ALLOW;
                } else if (InvoiceStatus.CANCELLED != invoiceStatus && accessLevel >= AccessLevel.MEDIUM_LOW) {
                    postRIStatus = InvoiceRiStatus.BUTTON;
                }
                invoice.setPostRIStatus(postRIStatus);
            }
        }

        return result;
    }

    /**
     * Check has draft invoice or not.
     * 
     * @param param page parameter
     * @return check result
     */
    public boolean hasDraftInvoice(PageParam param) {

        int dataCount = super.getDatasCount(SQLID_FIND_DRAFT_COUNT, param);
        return dataCount > 0 ? true : false;
    }

    /**
     * Get invoice No. set by upload ID.
     * 
     * @param uploadIds Upload ID list
     * @return invoice No. set
     */
    public List<String> getInvoiceNoByUploadId(List<String> uploadIds) {

        List<String> invoiceNos = new ArrayList<String>();
        PageParam param = new PageParam();
        param.setSelectedDatas(uploadIds);
        List<CPVIVS01Entity> result = super.baseMapper.select(getSqlId(SQLID_FIND_INVOICE_BY_UPLOAD_ID), param);
        for (CPVIVS01Entity data : result) {
            invoiceNos.add(data.getInvoiceNo());
        }

        return invoiceNos;
    }

    /**
     * Get invoice summary ID set by upload ID.
     * 
     * @param uploadIds upload ID list
     * @return invoice summary ID set
     */
    public List<String> getSummaryIdByUploadId(List<String> uploadIds) {

        List<String> summaryIds = new ArrayList<String>();
        PageParam param = new PageParam();
        param.setSelectedDatas(uploadIds);
        List<CPVIVS01Entity> result = super.baseMapper.select(getSqlId(SQLID_FIND_INVOICE_BY_UPLOAD_ID), param);
        for (CPVIVS01Entity data : result) {
            summaryIds.add(StringUtil.toSafeString(data.getInvoiceSummaryId()));
        }

        return summaryIds;
    }

    /**
     * Check inbound.
     * 
     * @param summaryIds invoice summary ID list
     * @return check result
     */
    public boolean checkInbound(List<String> summaryIds) {

        PageParam param = new PageParam();
        param.setSelectedDatas(summaryIds);
        int dataCount = super.getDatasCount(SQLID_FIND_INBOUND_COUNT, param);
        return dataCount > 0 ? true : false;
    }

    /**
     * Check actual CC.
     * 
     * @param summaryIds invoice summary ID list
     * @return check result
     */
    public boolean checkActualCC(List<String> summaryIds) {

        PageParam param = new PageParam();
        param.setSelectedDatas(summaryIds);
        int dataCount = super.getDatasCount(SQLID_FIND_ACTUAL_CC_COUNT, param);
        return dataCount > 0 ? true : false;
    }

    /**
     * Check already cancelled.
     * 
     * @param summaryIds invoice summary ID list
     * @return check result
     */
    public boolean checkCancelled(List<String> summaryIds) {

        PageParam param = new PageParam();
        param.setSelectedDatas(summaryIds);
        int dataCount = super.getDatasCount(SQLID_FIND_CANCELLED_COUNT, param);
        return dataCount > 0 ? true : false;
    }

    /**
     * Check approve invoice status.
     * 
     * @param param page parameter
     * @return check result
     */
    public boolean checkApproveStatus(PageParam param) {

        int dataCount = super.getDatasCount(SQLID_FIND_APPROVE_STATUS_COUNT, param);
        return dataCount > 0 ? true : false;
    }

    /**
     * Get cancelled invoices.
     * 
     * @param summaryIds invoice summary ID list
     * @return cancelled invoices
     */
    public List<CPVIVS01AdjustEntity> getCancelledInvoices(List<String> summaryIds) {

        CPVIVS01AdjustEntity condition = new CPVIVS01AdjustEntity();
        condition.setSummaryIds(summaryIds);
        return super.baseMapper.select(getSqlId(SQLID_FIND_CANCELLED_INVOICES), condition);
    }

    /**
     * Get cancel adjust plans.
     * 
     * @param invoiceNo Invoice No.
     * @param kanbanPlanNo KANBAN plan No.
     * @return cancel adjust plans
     */
    public List<CPVIVS01AdjustEntity> getCancelAdjustPlans(String invoiceNo, String kanbanPlanNo) {

        CPVIVS01AdjustEntity condition = new CPVIVS01AdjustEntity();
        condition.setInvoiceNo(invoiceNo);
        condition.setKanbanPlanNo(kanbanPlanNo);
        return super.baseMapper.select(getSqlId(SQLID_FIND_CANCEL_ADJUST_KANBAN_PLAN), condition);
    }

    /**
     * Get exist latest plan.
     * 
     * @param latestKanbanId latest KANBAN ID
     * @param transportMode transport mode
     * @param nirdFlag not in rundown flag
     * @param etd ETD
     * @param eta ETA
     * @param inbPlanDate inbound plan date
     * @return exist latest plan
     */
    public CPVIVS01AdjustEntity getExistLatestPlan(Integer latestKanbanId, Integer transportMode, Integer nirdFlag,
        Date etd, Date eta, Date inbPlanDate) {

        CPVIVS01AdjustEntity condition = new CPVIVS01AdjustEntity();
        condition.setLatestKanbanId(latestKanbanId);
        condition.setTransportMode(transportMode);
        condition.setNirdFlag(nirdFlag);
        condition.setEtd(etd);
        condition.setEta(eta);
        condition.setInbPlanDate(inbPlanDate);
        List<CPVIVS01AdjustEntity> result = super.baseMapper.select(getSqlId(SQLID_FIND_EXIST_LATEST_PLAN), condition);
        if (result.size() > 0) {
            return result.get(0);
        }

        return null;
    }

    /**
     * Approve process.
     * 
     * @param param page parameter
     */
    public void doApproveInvoice(PageParam param) {

        // Update TNT_INVOICE_SUMMARY
        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
        CPVIVS01Entity condition = new CPVIVS01Entity();
        condition.setSystemTime(systemTime);
        condition.setLoginUserId(param.getLoginUserId());
        condition.setSummaryIds(param.getSelectedDatas());
        super.baseMapper.update(getSqlId(SQLID_APPROVE_INVOICE_SUMMARY), condition);

        // Update Order Status
        List<CPVIVS01Entity> approveParts = super.baseMapper.select(getSqlId(SQLID_FIND_APPROVE_PARTS), condition);
        for (CPVIVS01Entity approvePart : approveParts) {
            TnfOrderStatus orderCondition = new TnfOrderStatus();
            orderCondition.setImpPoNo(approvePart.getIpo());
            orderCondition.setCustomerOrderNo(approvePart.getCpo());
            orderCondition.setExpPoNo(approvePart.getEpo());
            orderCondition.setSupplierId(approvePart.getSupplierId());
            orderCondition.setPartsId(approvePart.getPartsId());
            List<TnfOrderStatus> orderDatas = super.baseDao.select(orderCondition);
            if (orderDatas.size() > 0) {
                TnfOrderStatus orderData = orderDatas.get(0);
                if (approvePart.getIssueType() != null && approvePart.getIssueType() == InvoiceIssueType.EXP_WH) {
                    orderData.setExpOutboundQty(DecimalUtil.subtract(orderData.getExpOutboundQty(),
                        approvePart.getPartsQty()));
                }
                orderData.setExpOnshippingQty(DecimalUtil.add(orderData.getExpOnshippingQty(),
                    approvePart.getPartsQty()));
                orderData.setUpdatedBy(param.getLoginUserId());
                orderData.setUpdatedDate(systemTime);
                orderData.setVersion(orderData.getVersion() + IntDef.INT_ONE);
                super.baseDao.update(orderData);
                super.baseDao.flush();
            }
        }

        // Do stock adjustment
        postGrGiService.doGrGiPost(param.getCurrentOfficeId(), null);

        // Auto adjust shipping plan
        List<Integer> approveInvoiceIdList = new ArrayList<Integer>();
        List<CPVIVS01Entity> approveInvoices = super.baseMapper.select(getSqlId(SQLID_FIND_APPROVE_INVOICE_ID),
            condition);
        for (CPVIVS01Entity approveInvoice : approveInvoices) {
            approveInvoiceIdList.add(approveInvoice.getInvoiceId());
        }
        invoiceAdjService.doInvoiceAdjLogic(null, approveInvoiceIdList);
    }

    /**
     * Get irregular parts.
     * 
     * @param uploadId upload ID
     * @return irregular parts
     */
    public List<CPVIVS01IrregularEntity> getIrregularParts(String uploadId) {

        List<CPVIVS01IrregularEntity> result = new ArrayList<CPVIVS01IrregularEntity>();
        List<String> vesselList = new ArrayList<String>();
        CPVIVS01IrregularEntity condition = new CPVIVS01IrregularEntity();
        condition.setUploadId(uploadId);
        List<CPVIVS01IrregularEntity> irregularParts = super.baseMapper.select(getSqlId(SQLID_FIND_IRREGULAR_PARTS),
            condition);
        for (CPVIVS01IrregularEntity irregularPart : irregularParts) {
            String dataKey = irregularPart.getVesselName() + StringConst.UNDERLINE
                    + DateTimeUtil.getDisDate(irregularPart.getVesselEtd());
            if (!vesselList.contains(dataKey)) {
                vesselList.add(dataKey);
                irregularPart.setTansportMode(TransportMode.SEA);
                irregularPart.setChainStartDate(irregularPart.getVesselEtd());
                result.add(irregularPart);
            }
        }

        return result;
    }

    /**
     * Cancel process.
     * 
     * @param loginUserId login user ID
     * @param summaryIds invoice summary ID list
     */
    public void doCancelInvoice(Integer loginUserId, List<String> summaryIds) {

        // Update TNT_INVOICE_SUMMARY
        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
        CPVIVS01Entity cancelCondition = new CPVIVS01Entity();
        cancelCondition.setSystemTime(systemTime);
        cancelCondition.setLoginUserId(loginUserId);
        cancelCondition.setSummaryIds(summaryIds);
        super.baseMapper.update(getSqlId(SQLID_CANCEL_INVOICE_SUMMARY), cancelCondition);

        // Update Order Status
        List<CPVIVS01Entity> cancelledParts = super.baseMapper.select(getSqlId(SQLID_FIND_CANCELLED_PARTS),
            cancelCondition);
        for (CPVIVS01Entity cancelledPart : cancelledParts) {
            TnfOrderStatus orderCondition = new TnfOrderStatus();
            orderCondition.setKanbanPlanNo(cancelledPart.getKanbanPlanNo());
            orderCondition.setPartsId(cancelledPart.getPartsId());
            List<TnfOrderStatus> orderDatas = super.baseDao.select(orderCondition);
            if (orderDatas.size() > 0) {
                TnfOrderStatus orderData = orderDatas.get(0);
                orderData.setExpOnshippingQty(DecimalUtil.subtract(orderData.getExpOnshippingQty(),
                    cancelledPart.getPartsQty()));
                orderData.setUpdatedBy(loginUserId);
                orderData.setUpdatedDate(systemTime);
                orderData.setVersion(orderData.getVersion() + IntDef.INT_ONE);
                super.baseDao.update(orderData);
            }
        }

        // Automatic adjustment plan
        Map<String, CPVIVS01AdjustEntity> kanbanInvoiceMap = new LinkedHashMap<String, CPVIVS01AdjustEntity>();
        List<CPVIVS01AdjustEntity> invoiceList = getCancelledInvoices(summaryIds);
        for (CPVIVS01AdjustEntity invoice : invoiceList) {
            String kanbanPlanNo = invoice.getKanbanPlanNo();
            String invoiceNo = invoice.getInvoiceNo();
            String kanbanInvoiceKey = kanbanPlanNo + CPVIVF11Controller.SEPARATOR + invoiceNo;
            CPVIVS01AdjustEntity kanbanInvoice = kanbanInvoiceMap.get(kanbanInvoiceKey);
            Map<Integer, BigDecimal> partQtyMap = null;
            if (kanbanInvoice == null) {
                kanbanInvoice = new CPVIVS01AdjustEntity();
                kanbanInvoice.setKanbanPlanNo(kanbanPlanNo);
                kanbanInvoice.setInvoiceNo(invoiceNo);
                kanbanInvoice.setTransportMode(invoice.getTransportMode());
                kanbanInvoice.setEtd(invoice.getEtd());
                kanbanInvoice.setEta(invoice.getEta());
                kanbanInvoice.setInbPlanDate(invoice.getInbPlanDate());
                partQtyMap = new HashMap<Integer, BigDecimal>();
                kanbanInvoice.setPartQtyMap(partQtyMap);
                kanbanInvoiceMap.put(kanbanInvoiceKey, kanbanInvoice);
            } else {
                partQtyMap = kanbanInvoice.getPartQtyMap();
            }
            partQtyMap.put(invoice.getPartsId(), invoice.getInvoiceQty());
        }

        // Loop each KANBAN invoice and then adjustment
        for (Map.Entry<String, CPVIVS01AdjustEntity> entry : kanbanInvoiceMap.entrySet()) {
            CPVIVS01AdjustEntity kanbanInvoice = entry.getValue();
            String kanbanPlanNo = kanbanInvoice.getKanbanPlanNo();
            String invoiceNo = kanbanInvoice.getInvoiceNo();
            Integer invoiceTransMode = kanbanInvoice.getTransportMode();
            Integer latestKanbanId = null;
            String orderMonth = null;

            // Find need adjust KANBAN plan
            List<Integer> forceCompletedParts = new ArrayList<Integer>();
            Map<Integer, BigDecimal> spqMap = new HashMap<Integer, BigDecimal>();
            Map<Integer, CPVIVS01AdjustEntity> seaPlanPartsMap = new LinkedHashMap<Integer, CPVIVS01AdjustEntity>();
            Map<Integer, CPVIVS01AdjustEntity> airPlanPartsMap = new LinkedHashMap<Integer, CPVIVS01AdjustEntity>();
            List<CPVIVS01AdjustEntity> needAdjustPlans = getCancelAdjustPlans(invoiceNo, kanbanPlanNo);
            for (CPVIVS01AdjustEntity needAdjustPlan : needAdjustPlans) {
                if (KanbanPartsStatus.FORCE_COMPLETED == needAdjustPlan.getPartStatus()) {
                    addDistinct(forceCompletedParts, needAdjustPlan.getPartsId());
                }
                spqMap.put(needAdjustPlan.getPartsId(), needAdjustPlan.getLatestSpq() == null ? needAdjustPlan.getSpq()
                    : needAdjustPlan.getLatestSpq());
                latestKanbanId = needAdjustPlan.getLatestKanbanId();
                orderMonth = needAdjustPlan.getOrderMonth();
                Integer kanbanShippingId = needAdjustPlan.getKanbanShippingId();
                Integer planTransMode = needAdjustPlan.getTransportMode();
                CPVIVS01AdjustEntity planParts = null;
                if (TransportMode.SEA == planTransMode) {
                    planParts = seaPlanPartsMap.get(kanbanShippingId);
                } else {
                    planParts = airPlanPartsMap.get(kanbanShippingId);
                }
                Map<Integer, BigDecimal[]> planQtyMap = null;
                if (planParts == null) {
                    planParts = new CPVIVS01AdjustEntity();
                    BeanUtils.copyProperties(needAdjustPlan, planParts);
                    planQtyMap = new HashMap<Integer, BigDecimal[]>();
                    planParts.setPlanQtyMap(planQtyMap);
                    if (TransportMode.SEA == planTransMode) {
                        seaPlanPartsMap.put(kanbanShippingId, planParts);
                    } else {
                        airPlanPartsMap.put(kanbanShippingId, planParts);
                    }
                } else {
                    planQtyMap = planParts.getPlanQtyMap();
                }
                putQty(planQtyMap, QTY_TYPE_PLAN, needAdjustPlan.getPartsId(), needAdjustPlan.getPlanQty());
            }

            // loop each part in invoice
            Map<Integer, BigDecimal> invoiceQtyMap = kanbanInvoice.getPartQtyMap();
            for (Map.Entry<Integer, BigDecimal> partEntry : invoiceQtyMap.entrySet()) {
                Integer partsId = partEntry.getKey();
                BigDecimal invoiceQty = partEntry.getValue();

                // If the part's STATUS = 9(Force Completed), then no adjust and forward to next part
                if (forceCompletedParts.contains(partsId)) {
                    continue;
                }

                // Loop each plan that with the same transport mode as the invoice.
                Map<Integer, CPVIVS01AdjustEntity> sameModePartsMap = null;
                if (TransportMode.SEA == invoiceTransMode) {
                    sameModePartsMap = seaPlanPartsMap;
                } else {
                    sameModePartsMap = airPlanPartsMap;
                }
                for (Map.Entry<Integer, CPVIVS01AdjustEntity> planEntry : sameModePartsMap.entrySet()) {
                    CPVIVS01AdjustEntity planData = planEntry.getValue();
                    Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
                    BigDecimal planQty = getQty(planQtyMap, QTY_TYPE_PLAN, partsId);
                    if (DecimalUtil.isLessEquals(invoiceQty, BigDecimal.ZERO)) {
                        // If Invoice Qty <= 0, then do not adjust the plan, Forward to next part
                        break;
                    } else if (DecimalUtil.isLessEquals(planQty, BigDecimal.ZERO)) {
                        // If Plan Qty <= 0, then do not adjust the plan, Forward to next shipping plan
                        continue;
                    } else if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)
                            && DecimalUtil.isLessEquals(invoiceQty, planQty)) {
                        planData.setNeedAdjust(true);
                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId, invoiceQty);
                        invoiceQty = BigDecimal.ZERO;
                        break;
                    } else if (DecimalUtil.isGreater(invoiceQty, planQty)) {
                        planData.setNeedAdjust(true);
                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId, planQty);
                        invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                    }
                }

                // If Invoice Qty > 0, then adjust the plan with difference transport mode
                if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)) {
                    Map<Integer, CPVIVS01AdjustEntity> otherModePartsMap = null;
                    if (TransportMode.SEA == invoiceTransMode) {
                        otherModePartsMap = airPlanPartsMap;
                    } else {
                        otherModePartsMap = seaPlanPartsMap;
                    }
                    for (Map.Entry<Integer, CPVIVS01AdjustEntity> planEntry : otherModePartsMap.entrySet()) {
                        CPVIVS01AdjustEntity planData = planEntry.getValue();
                        Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
                        BigDecimal planQty = getQty(planQtyMap, QTY_TYPE_PLAN, partsId);
                        if (DecimalUtil.isLessEquals(invoiceQty, BigDecimal.ZERO)) {
                            // If Invoice Qty <= 0, then do not adjust the plan, Forward to next part
                            break;
                        } else if (DecimalUtil.isLessEquals(planQty, BigDecimal.ZERO)) {
                            // If Plan Qty <= 0, then do not adjust the plan, Forward to next shipping plan
                            continue;
                        } else if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)
                                && DecimalUtil.isLessEquals(invoiceQty, planQty)) {
                            planData.setNeedAdjust(true);
                            putQty(planQtyMap, QTY_TYPE_DIFF, partsId, invoiceQty);
                            invoiceQty = BigDecimal.ZERO;
                            break;
                        } else if (DecimalUtil.isGreater(invoiceQty, planQty)) {
                            planData.setNeedAdjust(true);
                            putQty(planQtyMap, QTY_TYPE_DIFF, partsId, planQty);
                            invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                        }
                    }
                }
            }

            // Do Kanban Plan Adjustment
            adjustKanbanPlan(seaPlanPartsMap, loginUserId, systemTime, latestKanbanId, orderMonth, spqMap);
            adjustKanbanPlan(airPlanPartsMap, loginUserId, systemTime, latestKanbanId, orderMonth, spqMap);
        }

        // Cancel invoice shipping data
        CPVIVS01AdjustEntity invoiceCondition = new CPVIVS01AdjustEntity();
        invoiceCondition.setSystemTime(systemTime);
        invoiceCondition.setLoginUserId(loginUserId);
        invoiceCondition.setSummaryIds(summaryIds);
        super.baseMapper.update(getSqlId(SQLID_CANCEL_INVOICE_SHIPPING), invoiceCondition);

        // Cancel invoice group data
        super.baseMapper.update(getSqlId(SQLID_CANCEL_INVOICE_GROUP), invoiceCondition);
    }

    /**
     * Adjust KANBAN plan.
     * 
     * @param planPartsMap parts map
     * @param loginUserId login user ID
     * @param systemTime system time
     * @param latestKanbanId latest KANBAN ID
     * @param orderMonth order month
     * @param spqMap SPQ map
     */
    private void adjustKanbanPlan(Map<Integer, CPVIVS01AdjustEntity> planPartsMap, Integer loginUserId,
        Timestamp systemTime, Integer latestKanbanId, String orderMonth, Map<Integer, BigDecimal> spqMap) {

        for (Map.Entry<Integer, CPVIVS01AdjustEntity> planEntry : planPartsMap.entrySet()) {
            CPVIVS01AdjustEntity planData = planEntry.getValue();
            Integer transportMode = planData.getTransportMode();
            Integer nirdFlag = planData.getNirdFlag();
            Date etd = planData.getEtd();
            Date eta = planData.getEta();
            Date inbPlanDate = planData.getInbPlanDate();
            Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
            if (!planData.isNeedAdjust()) {
                continue;
            }

            // find latest shipping plan with the old information
            CPVIVS01AdjustEntity existPlan = getExistLatestPlan(latestKanbanId, transportMode, nirdFlag, etd, eta,
                inbPlanDate);
            if (existPlan == null) {
                // Add new shipping plan and then adjust the plan
                TntKanbanShipping kanbanShipping = new TntKanbanShipping();
                kanbanShipping.setKanbanId(latestKanbanId);
                kanbanShipping.setShippingUuid(UUID.randomUUID().toString());
                kanbanShipping.setTransportMode(transportMode);
                kanbanShipping.setEtd(etd);
                kanbanShipping.setEta(eta);
                kanbanShipping.setImpInbPlanDate(inbPlanDate);
                kanbanShipping.setOriginalVersion(cpvivf11Service.getNextOriginalVersion(latestKanbanId));
                kanbanShipping.setRevisionVersion(null);
                kanbanShipping.setRevisionReason(INVOICE_CANCEL_REASON);
                kanbanShipping.setCompletedFlag(KbsCompletedFlag.NORMAL);
                kanbanShipping.setNirdFlag(nirdFlag);
                kanbanShipping.setCreatedBy(loginUserId);
                kanbanShipping.setCreatedDate(systemTime);
                kanbanShipping.setUpdatedBy(loginUserId);
                kanbanShipping.setUpdatedDate(systemTime);
                kanbanShipping.setVersion(1);
                super.baseDao.insert(kanbanShipping);

                Integer kanbanPlanId = null;
                if (KbsNirdFlag.NORMAL == nirdFlag) {
                    // the plan's NIRD_FLAG = 0(Normal), Insert into TNT_KANBAN_PLAN
                    TntKanbanPlan kanbanPlan = new TntKanbanPlan();
                    kanbanPlan.setKanbanId(latestKanbanId);
                    kanbanPlan.setShippingUuid(kanbanShipping.getShippingUuid());
                    kanbanPlan.setOrderMonth(orderMonth);
                    kanbanPlan.setPlanType(PlanType.SHIPPING_PLAN);
                    kanbanPlan.setIssuedDate(null);
                    kanbanPlan.setIssueRemark(null);
                    kanbanPlan.setDeliveredDate(null);
                    kanbanPlan.setDelivereRemark(null);
                    kanbanPlan.setVanningDate(null);
                    kanbanPlan.setVanningRemark(null);
                    kanbanPlan.setCreatedBy(loginUserId);
                    kanbanPlan.setCreatedDate(systemTime);
                    kanbanPlan.setUpdatedBy(loginUserId);
                    kanbanPlan.setUpdatedDate(systemTime);
                    kanbanPlan.setVersion(1);
                    super.baseDao.insert(kanbanPlan);
                    kanbanPlanId = kanbanPlan.getKanbanPlanId();
                }

                // Insert part information
                for (Map.Entry<Integer, BigDecimal[]> qtyEntry : planQtyMap.entrySet()) {
                    Integer partsId = qtyEntry.getKey();
                    BigDecimal[] qtyArray = qtyEntry.getValue();
                    BigDecimal diffQty = qtyArray[QTY_TYPE_DIFF];
                    if (DecimalUtil.isGreater(diffQty, BigDecimal.ZERO)) {
                        // Insert into TNT_KANBAN_SHIPPING_PARTS
                        TntKanbanShippingPart shippingPart = new TntKanbanShippingPart();
                        shippingPart.setKanbanShippingId(kanbanShipping.getKanbanShippingId());
                        shippingPart.setPartsId(partsId);
                        shippingPart.setSpq(null);
                        shippingPart.setQty(diffQty);
                        shippingPart.setKanbanQty(null);
                        shippingPart.setCreatedBy(loginUserId);
                        shippingPart.setCreatedDate(systemTime);
                        shippingPart.setUpdatedBy(loginUserId);
                        shippingPart.setUpdatedDate(systemTime);
                        shippingPart.setVersion(1);
                        super.baseDao.insert(shippingPart);

                        if (KbsNirdFlag.NORMAL == nirdFlag) {
                            // Insert into TNT_KANBAN_PLAN_PARTS
                            TntKanbanPlanPart kanbanPlanPart = new TntKanbanPlanPart();
                            kanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                            kanbanPlanPart.setPartsId(partsId);
                            kanbanPlanPart.setSpq(null);
                            kanbanPlanPart.setQty(diffQty);
                            kanbanPlanPart.setKanbanQty(null);
                            kanbanPlanPart.setCreatedBy(loginUserId);
                            kanbanPlanPart.setCreatedDate(systemTime);
                            kanbanPlanPart.setUpdatedBy(loginUserId);
                            kanbanPlanPart.setUpdatedDate(systemTime);
                            kanbanPlanPart.setVersion(1);
                            super.baseDao.insert(kanbanPlanPart);
                        }
                    }
                }
            } else {
                // Update old TNT_KANBAN_SHIPPING
                TntKanbanShipping oldShipping = super.getOneById(TntKanbanShipping.class,
                    existPlan.getKanbanShippingId());
                oldShipping.setCompletedFlag(KbsCompletedFlag.HISTORY);
                oldShipping.setNirdFlag(KbsNirdFlag.NORMAL);
                oldShipping.setUpdatedBy(loginUserId);
                oldShipping.setUpdatedDate(systemTime);
                oldShipping.setVersion(oldShipping.getVersion() + 1);
                super.baseDao.update(oldShipping);

                // Insert new TNT_KANBAN_SHIPPING
                TntKanbanShipping newShipping = new TntKanbanShipping();
                newShipping.setKanbanId(oldShipping.getKanbanId());
                newShipping.setShippingUuid(oldShipping.getShippingUuid());
                newShipping.setTransportMode(oldShipping.getTransportMode());
                newShipping.setEtd(oldShipping.getEtd());
                newShipping.setEta(oldShipping.getEta());
                newShipping.setImpInbPlanDate(oldShipping.getImpInbPlanDate());
                newShipping.setOriginalVersion(oldShipping.getOriginalVersion());
                newShipping.setRevisionVersion(getNextRevisionVersion(oldShipping.getRevisionVersion()));
                newShipping.setRevisionReason(INVOICE_CANCEL_REASON);
                newShipping.setCompletedFlag(KbsCompletedFlag.NORMAL);
                newShipping.setNirdFlag(nirdFlag);
                newShipping.setCreatedBy(loginUserId);
                newShipping.setCreatedDate(systemTime);
                newShipping.setUpdatedBy(loginUserId);
                newShipping.setUpdatedDate(systemTime);
                newShipping.setVersion(1);
                super.baseDao.insert(newShipping);

                // Insert into TNT_KANBAN_SHIPPING_PARTS
                TntKanbanShippingPart existPartCondition = new TntKanbanShippingPart();
                existPartCondition.setKanbanShippingId(existPlan.getKanbanShippingId());
                List<TntKanbanShippingPart> existShippingParts = super.baseDao.select(existPartCondition);
                for (TntKanbanShippingPart existPart : existShippingParts) {
                    TntKanbanShippingPart shippingPart = new TntKanbanShippingPart();
                    shippingPart.setKanbanShippingId(newShipping.getKanbanShippingId());
                    shippingPart.setPartsId(existPart.getPartsId());
                    shippingPart.setSpq(null);
                    shippingPart.setQty(existPart.getQty());
                    shippingPart.setKanbanQty(null);
                    shippingPart.setCreatedBy(loginUserId);
                    shippingPart.setCreatedDate(systemTime);
                    shippingPart.setUpdatedBy(loginUserId);
                    shippingPart.setUpdatedDate(systemTime);
                    shippingPart.setVersion(1);
                    super.baseDao.insert(shippingPart);
                }

                // Insert difference plan in TNT_KANBAN_PLAN
                Integer differBoxId = existPlan.getDifferBoxId();
                Integer dummyBoxId = existPlan.getDummyBoxId();
                if (KbsNirdFlag.NORMAL == nirdFlag && dummyBoxId == null && differBoxId == null) {
                    TntKanbanPlan differPlan = new TntKanbanPlan();
                    differPlan.setKanbanId(oldShipping.getKanbanId());
                    differPlan.setShippingUuid(oldShipping.getShippingUuid());
                    differPlan.setOrderMonth(orderMonth);
                    differPlan.setPlanType(PlanType.DIFFERENCE);
                    differPlan.setIssuedDate(null);
                    differPlan.setIssueRemark(null);
                    differPlan.setDeliveredDate(null);
                    differPlan.setDelivereRemark(null);
                    differPlan.setVanningDate(null);
                    differPlan.setVanningRemark(null);
                    differPlan.setCreatedBy(loginUserId);
                    differPlan.setCreatedDate(systemTime);
                    differPlan.setUpdatedBy(loginUserId);
                    differPlan.setUpdatedDate(systemTime);
                    differPlan.setVersion(1);
                    super.baseDao.insert(differPlan);
                    differBoxId = differPlan.getKanbanPlanId();
                }

                // Insert part information
                for (Map.Entry<Integer, BigDecimal[]> qtyEntry : planQtyMap.entrySet()) {
                    Integer partsId = qtyEntry.getKey();
                    BigDecimal[] qtyArray = qtyEntry.getValue();
                    BigDecimal diffQty = qtyArray[QTY_TYPE_DIFF];
                    if (DecimalUtil.isGreater(diffQty, BigDecimal.ZERO)) {
                        // Insert into TNT_KANBAN_SHIPPING_PARTS
                        TntKanbanShippingPart spPartCondition = new TntKanbanShippingPart();
                        spPartCondition.setPartsId(partsId);
                        spPartCondition.setKanbanShippingId(newShipping.getKanbanShippingId());
                        List<TntKanbanShippingPart> existSpParts = super.baseDao.select(spPartCondition);
                        TntKanbanShippingPart shippingPart = null;
                        if (existSpParts != null && existSpParts.size() > 0) {
                            shippingPart = existSpParts.get(0);
                            shippingPart.setQty(DecimalUtil.add(shippingPart.getQty(), diffQty));
                            shippingPart.setUpdatedBy(loginUserId);
                            shippingPart.setUpdatedDate(systemTime);
                            shippingPart.setVersion(shippingPart.getVersion() + IntDef.INT_ONE);
                            super.baseDao.update(shippingPart);
                        } else {
                            shippingPart = new TntKanbanShippingPart();
                            shippingPart.setKanbanShippingId(newShipping.getKanbanShippingId());
                            shippingPart.setPartsId(partsId);
                            shippingPart.setSpq(null);
                            shippingPart.setQty(diffQty);
                            shippingPart.setKanbanQty(null);
                            shippingPart.setCreatedBy(loginUserId);
                            shippingPart.setCreatedDate(systemTime);
                            shippingPart.setUpdatedBy(loginUserId);
                            shippingPart.setUpdatedDate(systemTime);
                            shippingPart.setVersion(1);
                            super.baseDao.insert(shippingPart);
                        }

                        // Insert into TNT_KANBAN_PLAN_PARTS
                        if (KbsNirdFlag.NORMAL == nirdFlag) {
                            BigDecimal kanbanQty = DecimalUtil.divide(diffQty, spqMap.get(partsId), IntDef.INT_TWO);
                            TntKanbanPlanPart kanbanPlanPart = null;
                            Integer kanbanPlanId = dummyBoxId != null ? dummyBoxId : differBoxId;
                            TntKanbanPlanPart partCondition = new TntKanbanPlanPart();
                            partCondition.setPartsId(partsId);
                            partCondition.setKanbanPlanId(kanbanPlanId);
                            List<TntKanbanPlanPart> existParts = super.baseDao.select(partCondition);
                            if (existParts != null && existParts.size() > 0) {
                                kanbanPlanPart = existParts.get(0);
                                kanbanPlanPart.setQty(dummyBoxId != null ? DecimalUtil.add(kanbanPlanPart.getQty(),
                                    diffQty) : null);
                                kanbanPlanPart.setKanbanQty(dummyBoxId != null ? null : DecimalUtil.add(
                                    kanbanPlanPart.getKanbanQty(), kanbanQty));
                                kanbanPlanPart.setUpdatedBy(loginUserId);
                                kanbanPlanPart.setUpdatedDate(systemTime);
                                kanbanPlanPart.setVersion(kanbanPlanPart.getVersion() + IntDef.INT_ONE);
                                super.baseDao.update(kanbanPlanPart);
                            } else {
                                kanbanPlanPart = new TntKanbanPlanPart();
                                kanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                                kanbanPlanPart.setPartsId(partsId);
                                kanbanPlanPart.setSpq(null);
                                kanbanPlanPart.setQty(dummyBoxId != null ? diffQty : null);
                                kanbanPlanPart.setKanbanQty(dummyBoxId != null ? null : kanbanQty);
                                kanbanPlanPart.setCreatedBy(loginUserId);
                                kanbanPlanPart.setCreatedDate(systemTime);
                                kanbanPlanPart.setUpdatedBy(loginUserId);
                                kanbanPlanPart.setUpdatedDate(systemTime);
                                kanbanPlanPart.setVersion(1);
                                super.baseDao.insert(kanbanPlanPart);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Put qty to map.
     * 
     * @param planQtyMap qty map
     * @param qtyType qty type
     * @param mapKey map key
     * @param qty the qty
     */
    private void putQty(Map<Integer, BigDecimal[]> planQtyMap, int qtyType, Integer mapKey, BigDecimal qty) {

        BigDecimal[] qtyArray = planQtyMap.get(mapKey);
        if (qtyArray == null) {
            qtyArray = new BigDecimal[IntDef.INT_TWO];
            planQtyMap.put(mapKey, qtyArray);
        }

        qtyArray[qtyType] = qty;
    }

    /**
     * Get qty from map.
     * 
     * @param planQtyMap qty map
     * @param qtyType qty type
     * @param mapKey map key
     * @return qty
     */
    private BigDecimal getQty(Map<Integer, BigDecimal[]> planQtyMap, int qtyType, Integer mapKey) {

        BigDecimal ret = null;
        BigDecimal[] qtyArray = planQtyMap.get(mapKey);
        if (qtyArray != null) {
            ret = qtyArray[qtyType];
        }
        if (ret == null && QTY_TYPE_PLAN == qtyType) {
            ret = BigDecimal.ZERO;
        }

        return ret;
    }

    /**
     * Add distinct value to list.
     * 
     * @param <T> the object
     * @param list the list
     * @param value the add value
     */
    private <T extends Object> void addDistinct(List<T> list, T value) {

        if (!list.contains(value)) {
            list.add(value);
        }
    }

    /**
     * Get next revision version.
     * 
     * @param curVersion current revision version
     * @return next revision version
     */
    private Integer getNextRevisionVersion(Integer curVersion) {

        if (curVersion == null) {
            return IntDef.INT_ONE;
        }
        return curVersion + IntDef.INT_ONE;
    }

}
