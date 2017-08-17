/**
 * CPVIVF11Service.java
 * 
 * @screen CPVIVF11
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.SupplyChainEntity;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.consts.CodeConst.InvoiceContainerStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceGroupStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceIrregularsStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceShippingStatus;
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
import com.chinaplus.common.entity.TnmNonTtcCustomer;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceContainer;
import com.chinaplus.common.entity.TntInvoiceGroup;
import com.chinaplus.common.entity.TntInvoicePart;
import com.chinaplus.common.entity.TntInvoiceShipping;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.common.entity.TntKanbanPlan;
import com.chinaplus.common.entity.TntKanbanPlanPart;
import com.chinaplus.common.entity.TntKanbanShipping;
import com.chinaplus.common.entity.TntKanbanShippingPart;
import com.chinaplus.common.service.SupplyChainService;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inv.control.CPVIVF11Controller;
import com.chinaplus.web.inv.entity.CPVIVF11AdjustEntity;
import com.chinaplus.web.inv.entity.CPVIVF11KanbEntity;
import com.chinaplus.web.inv.entity.CPVIVF11MailInvoiceEntity;
import com.chinaplus.web.inv.entity.CPVIVF11SupportEntity;
import com.chinaplus.web.inv.entity.CPVIVS01IrregularEntity;

/**
 * Invoice Upload Service.
 */
@Service
public class CPVIVF11Service extends BaseService {

    /** System adjust reason */
    private static final String SYSTEM_ADJUST_REASON = "系统自动调整";

    /** SQL ID: find all parts */
    private static final String SQLID_FIND_ALL_PARTS = "findAllParts";

    /** SQL ID: find TTC customer code */
    private static final String SQLID_FIND_TTC_CUSTOMER_CODE = "findTtcCustomerCode";

    /** SQL ID: find all order month */
    private static final String SQLID_FIND_ALL_ORDER_MONTH = "findAllOrderMonth";

    /** SQL ID: find order month */
    private static final String SQLID_FIND_ORDER_MONTH = "findOrderMonth";

    /** SQL ID: find KANBAN PLAN No. */
    private static final String SQLID_FIND_KANBAN_PLAN_NO = "findKanbanPlanNo";

    /** SQL ID: find irregular count */
    private static final String SQLID_FIND_IRREGULAR_COUNT = "findIrregularCount";

    /** SQL ID: find supplementary count */
    private static final String SQLID_FIND_SUPPLEMENTARY_COUNT = "findSupplementaryCount";

    /** SQL ID: find uploaded invoices */
    private static final String SQLID_FIND_UPLOADED_INVOICES = "findUploadedInvoices";

    /** SQL ID: find need adjust KANBAN plan */
    private static final String SQLID_FIND_NEED_ADJUEST_KANBAN_PLAN = "findNeedAdjuestKanbanPlan";

    /** SQL ID: find all KANBAN parts */
    private static final String SQLID_FIND_ALL_KANBAN_PARTS = "findAllKanbanParts";

    /** SQL ID: find max original version */
    private static final String SQLID_FIND_MAX_ORIGINAL_VERSION = "findMaxOriginalVersion";

    /** SQL ID: update invoice summary qty */
    private static final String SQLID_UPDATE_INVOICE_SUMMARY_QTY = "updateInvoiceSummaryQty";

    /** Qty Type: Plan */
    private static final int QTY_TYPE_PLAN = 0;

    /** Qty Type: Not in rundown */
    private static final int QTY_TYPE_NIRD = 1;

    /** Qty Type: Difference */
    private static final int QTY_TYPE_DIFF = 2;

    /** Supply Chain Service */
    @Autowired
    private SupplyChainService supplyChainService;

    /**
     * Get non TTC customer list.
     * 
     * @return Non TTC customer list
     */
    public List<String> getNonTtcCustomerList() {

        List<String> result = new ArrayList<String>();
        List<TnmNonTtcCustomer> nonTtcCustomerList = super.baseDao.select(new TnmNonTtcCustomer());
        if (nonTtcCustomerList != null && nonTtcCustomerList.size() > 0) {
            for (TnmNonTtcCustomer nonTtcCustomer : nonTtcCustomerList) {
                result.add(nonTtcCustomer.getKanbCustomerCode());
            }
        }
        return result;
    }

    /**
     * Get all order month by customer IDs.
     * 
     * @param customerIdList customer IDs
     * @return all order month map
     */
    public Map<Integer, List<CPVIVF11SupportEntity>> getAllOrderMonth(List<Integer> customerIdList) {

        Map<Integer, List<CPVIVF11SupportEntity>> orderMonthMap = new HashMap<Integer, List<CPVIVF11SupportEntity>>();
        if (customerIdList.size() == 0) {
            return orderMonthMap;
        }

        CPVIVF11SupportEntity condition = new CPVIVF11SupportEntity();
        condition.setCustomerIdList(customerIdList);
        List<CPVIVF11SupportEntity> results = super.baseMapper.select(getSqlId(SQLID_FIND_ALL_ORDER_MONTH), condition);
        for (CPVIVF11SupportEntity result : results) {
            Integer customerId = result.getCustomerId();
            List<CPVIVF11SupportEntity> orderMonthList = orderMonthMap.get(customerId);
            if (orderMonthList == null) {
                orderMonthList = new ArrayList<CPVIVF11SupportEntity>();
                orderMonthMap.put(customerId, orderMonthList);
            }
            orderMonthList.add(result);
        }

        return orderMonthMap;
    }

    /**
     * Get all parts by customer code.
     * 
     * @param officeId the office ID
     * @param expCustomerCodeList the export customer code list
     * @param invCustomerCodeList the invoice customer code list
     * @return all parts
     */
    public List<CPVIVF11SupportEntity> getAllPartsByCustCode(Integer officeId, List<String> expCustomerCodeList,
        List<String> invCustomerCodeList) {

        CPVIVF11SupportEntity condition = new CPVIVF11SupportEntity();
        condition.setOfficeId(officeId);
        condition.setExpCustomerCodeList(expCustomerCodeList);
        condition.setInvCustomerCodeList(invCustomerCodeList);
        return super.baseMapper.select(getSqlId(SQLID_FIND_ALL_PARTS), condition);
    }

    /**
     * Get TTC customer code.
     * 
     * @param officeId the office ID
     * @param expCustomerCodeList the export customer code list
     * @param invCustomerCodeList the invoice customer code list
     * @return TTC customer code
     */
    public List<String> getTtcCustCode(Integer officeId, List<String> expCustomerCodeList,
        List<String> invCustomerCodeList) {

        List<String> customerCodes = new ArrayList<String>();
        CPVIVF11SupportEntity condition = new CPVIVF11SupportEntity();
        condition.setOfficeId(officeId);
        condition.setExpCustomerCodeList(expCustomerCodeList);
        condition.setInvCustomerCodeList(invCustomerCodeList);
        List<CPVIVF11SupportEntity> result = super.baseMapper.select(getSqlId(SQLID_FIND_TTC_CUSTOMER_CODE), condition);
        for (CPVIVF11SupportEntity entity : result) {
            customerCodes.add(entity.getTtcCustCode());
        }
        return customerCodes;
    }

    /**
     * Get KANBAN plan No.
     * 
     * @param officeId the office ID
     * @param customerId the customer ID
     * @param supplierId the supplier ID
     * @param orderMonth the order month
     * @return KANBAN plan No
     */
    public String getKanbanPlanNo(Integer officeId, Integer customerId, Integer supplierId, String orderMonth) {

        if (customerId == null || supplierId == null || orderMonth == null) {
            return null;
        }

        CPVIVF11SupportEntity condition = new CPVIVF11SupportEntity();
        condition.setOfficeId(officeId);
        condition.setCustomerId(customerId);
        condition.setSupplierId(supplierId);
        condition.setOrderMonth(orderMonth);
        List<CPVIVF11SupportEntity> result = super.baseMapper.select(getSqlId(SQLID_FIND_KANBAN_PLAN_NO), condition);
        if (result != null && result.size() > 0) {
            return result.get(0).getKanbanPlanNo();
        }
        return null;
    }

    /**
     * Get order month.
     * 
     * @param customerId the office ID
     * @param minDate the export customer code list
     * @param maxDate the invoice customer code list
     * @return order month
     */
    public List<String> getOrderMonth(Integer customerId, Date minDate, Date maxDate) {

        List<String> monthList = new ArrayList<String>();
        if (customerId == null || minDate == null || maxDate == null) {
            return monthList;
        }

        CPVIVF11SupportEntity condition = new CPVIVF11SupportEntity();
        condition.setCustomerId(customerId);
        condition.setMinIssuedDate(minDate);
        condition.setMaxIssuedDate(maxDate);
        List<CPVIVF11SupportEntity> dataList = super.baseMapper.select(getSqlId(SQLID_FIND_ORDER_MONTH), condition);
        if (dataList != null && dataList.size() > 0) {
            for (CPVIVF11SupportEntity data : dataList) {
                monthList.add(data.getOrderMonth());
            }
        }

        return monthList;
    }

    /**
     * Check the irregular shipping schedule is exist or not.
     * 
     * @param uploadId the upload ID
     * @return check result
     */
    public boolean isIrregularExist(String uploadId) {

        ObjectParam<String> condition = new ObjectParam<String>();
        condition.setData(uploadId);
        int dataCount = super.getDatasCount(SQLID_FIND_IRREGULAR_COUNT, condition);
        return dataCount > 0 ? true : false;
    }

    /**
     * Check the irregular shipping schedule is exist or not.
     * 
     * @param uploadId the upload ID
     * @return check result
     */
    public boolean isSupplementaryExist(String uploadId) {

        ObjectParam<String> condition = new ObjectParam<String>();
        condition.setData(uploadId);
        int dataCount = super.getDatasCount(SQLID_FIND_SUPPLEMENTARY_COUNT, condition);
        return dataCount > 0 ? true : false;
    }

    /**
     * Get completed invoices.
     * 
     * @param uploadId the upload ID
     * @return completed invoices
     */
    public List<CPVIVF11SupportEntity> getCompletedInvoices(String uploadId) {

        CPVIVF11SupportEntity condition = new CPVIVF11SupportEntity();
        condition.setUploadId(uploadId);
        return super.baseMapper.select(getSqlId(SQLID_FIND_UPLOADED_INVOICES), condition);
    }

    /**
     * Get need adjust KANBAN plans.
     * 
     * @param kanbanPlanNo the KANBAN plan No.
     * @return need adjust KANBAN plans
     */
    public List<CPVIVF11AdjustEntity> getNeedAdjustPlans(String kanbanPlanNo) {

        super.baseDao.flush();
        CPVIVF11AdjustEntity condition = new CPVIVF11AdjustEntity();
        condition.setKanbanPlanNo(kanbanPlanNo);
        return super.baseMapper.select(getSqlId(SQLID_FIND_NEED_ADJUEST_KANBAN_PLAN), condition);
    }

    /**
     * Get all KANBAN parts.
     * 
     * @param kanbanId the KANBAN ID
     * @return all KANBAN parts
     */
    public List<CPVIVF11AdjustEntity> getAllKanbanParts(Integer kanbanId) {

        CPVIVF11AdjustEntity condition = new CPVIVF11AdjustEntity();
        condition.setKanbanId(kanbanId);
        return super.baseMapper.select(getSqlId(SQLID_FIND_ALL_KANBAN_PARTS), condition);
    }

    /**
     * Upload Logic.
     * 
     * @param param common parameter
     * @param uploadId the upload ID
     * @param kanbDataList the KANB data list
     * @param mailInvoiceList the mail invoice data list
     * @param kanbTotalList the KANB data tatal list
     * @param invoiceSupplierMap the invoice supplier map
     */
    public void doUpload(BaseParam param, String uploadId, List<CPVIVF11KanbEntity> kanbDataList,
        List<CPVIVF11MailInvoiceEntity> mailInvoiceList, List<CPVIVF11KanbEntity> kanbTotalList,
        Map<String, List<String>> invoiceSupplierMap) {

        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
        Timestamp officeTime = super.getDBDateTime(param.getOfficeTimezone());
        Map<String, TntInvoiceSummary> summaryMap = new HashMap<String, TntInvoiceSummary>();

        // Find ETA&Inbound Plan Date by Vessel&ETD
        Map<String, Date[]> dateMap = new HashMap<String, Date[]>();
        List<CPVIVS01IrregularEntity> supplyList = new ArrayList<CPVIVS01IrregularEntity>();
        for (CPVIVF11KanbEntity kanbTotalData : kanbTotalList) {
            String vesselName = kanbTotalData.getVesselName();
            Date etd = kanbTotalData.getMailInvoice().getEtd();
            Integer expPartsId = kanbTotalData.getExpPartsId();
            String mapKey = vesselName + StringConst.UNDERLINE + DateTimeUtil.getDisDate(etd);
            if (!dateMap.containsKey(mapKey)) {
                Date[] dateArray = new Date[IntDef.INT_TWO];
                dateMap.put(mapKey, dateArray);
                CPVIVS01IrregularEntity entity = new CPVIVS01IrregularEntity();
                entity.setVesselName(vesselName);
                entity.setVesselEtd(etd);
                entity.setExpPartsId(expPartsId);
                entity.setTansportMode(TransportMode.SEA);
                entity.setChainStartDate(etd);
                supplyList.add(entity);
            }
        }
        if (supplyList.size() > 0) {
            supplyChainService.prepareSupplyChain(supplyList, ChainStep.INVOICE, BusinessPattern.AISIN, false);
            for (CPVIVS01IrregularEntity entity : supplyList) {
                Date eta = entity.getEta();
                Date inboundDate = entity.getImpPlanInboundDate();
                if (eta == null || inboundDate == null) {
                    continue;
                }
                String mapKey = entity.getVesselName() + StringConst.UNDERLINE
                        + DateTimeUtil.getDisDate(entity.getVesselEtd());
                Date[] dateArray = dateMap.get(mapKey);
                dateArray[0] = eta;
                dateArray[1] = inboundDate;
            }
        }

        // Do invoice process for the parts qty that both in V-net&Mail Invoice
        Map<String, TntInvoice> invoiceMap = new HashMap<String, TntInvoice>();
        Map<String, TntInvoiceGroup> groupMap = new HashMap<String, TntInvoiceGroup>();
        for (CPVIVF11KanbEntity kanbTotalData : kanbTotalList) {
            String invoiceNo = kanbTotalData.getInvoiceNo();
            String kanbanPlanNo = kanbTotalData.getKanbanPlanNo();
            String vesselName = kanbTotalData.getVesselName();
            CPVIVF11MailInvoiceEntity invoiceInfo = kanbTotalData.getMailInvoice();
            Date etd = invoiceInfo.getEtd();
            Integer transportMode = StringUtil.toInteger(invoiceInfo.getTransportMode());

            // TNT_INVOICE_GROUP
            String groupKey = kanbanPlanNo + CPVIVF11Controller.SEPARATOR + kanbTotalData.getVesselName()
                    + CPVIVF11Controller.SEPARATOR + DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, etd);
            TntInvoiceGroup groupData = groupMap.get(groupKey);
            if (groupData == null) {
                // Find exist group data from DB
                groupData = getInvoiceGroup(kanbanPlanNo, vesselName, etd);
                if (groupData == null) {
                    // Find ETA&Inbound Plan Date
                    String mapKey = vesselName + StringConst.UNDERLINE + DateTimeUtil.getDisDate(etd);
                    Date[] dateArray = dateMap.get(mapKey);

                    // Create new invoice group
                    groupData = new TntInvoiceGroup();
                    groupData.setKanbanPlanNo(kanbanPlanNo);
                    groupData.setTransportMode(transportMode);
                    groupData.setVesselName(vesselName);
                    groupData.setEtd(etd);
                    groupData.setEta(dateArray[0]);
                    groupData.setImpInbPlanDate(dateArray[1]);
                    groupData.setStatus(InvoiceGroupStatus.NORMAL);
                    groupData.setCreatedBy(param.getLoginUserId());
                    groupData.setCreatedDate(systemTime);
                    groupData.setUpdatedBy(param.getLoginUserId());
                    groupData.setUpdatedDate(systemTime);
                    groupData.setVersion(1);
                    super.baseDao.insert(groupData);
                    groupMap.put(groupKey, groupData);
                }
            }
            Date eta = groupData.getEta();
            Date inbPlanDate = groupData.getImpInbPlanDate();

            // TNT_INVOICE_SUMMARY
            TntInvoiceSummary summaryData = summaryMap.get(invoiceNo);
            if (summaryData == null) {
                summaryData = new TntInvoiceSummary();
                summaryData.setOfficeId(param.getCurrentOfficeId());
                summaryData.setBusinessPattern(BusinessPattern.AISIN);
                summaryData.setInvoiceNo(invoiceNo);
                summaryData.setInvoiceType(InvoiceType.AISIN);
                summaryData.setVesselName(vesselName);
                summaryData.setBlNo(null);
                summaryData.setBlDate(null);
                summaryData.setExpRegion(kanbTotalData.getExpRegion());
                summaryData.setImpRegion(kanbTotalData.getImpRegion());
                summaryData.setTransportMode(transportMode);
                summaryData.setSupplierCodeSet(getSupplierCodeSet(invoiceSupplierMap.get(invoiceNo)));
                summaryData.setEtd(etd);
                summaryData.setEta(eta);
                summaryData.setVanningDate(kanbTotalData.getVanningDate());
                summaryData.setInvoiceQty(BigDecimal.ZERO);
                summaryData.setInboundQty(BigDecimal.ZERO);
                summaryData.setPostRiFlag(PostRiFlag.N);
                summaryData.setGrDate(null);
                summaryData.setGiDate(null);
                summaryData.setInvoiceStatus(InvoiceStatus.PENDING);
                summaryData.setNonShippingRoute(inbPlanDate == null ? InvoiceIrregularsStatus.DRAFT : null);
                summaryData.setUploadId(uploadId);
                summaryData.setUploadedBy(param.getLoginUserId());
                summaryData.setUploadedDate(officeTime);
                summaryData.setUploadStatus(InvoiceUploadStatus.DONE);
                summaryData.setCreatedBy(param.getLoginUserId());
                summaryData.setCreatedDate(systemTime);
                summaryData.setUpdatedBy(param.getLoginUserId());
                summaryData.setUpdatedDate(systemTime);
                summaryData.setVersion(1);
                super.baseDao.insert(summaryData);
                summaryMap.put(invoiceNo, summaryData);
            }

            // TNT_INVOICE
            String invoiceKey = invoiceNo + CPVIVF11Controller.SEPARATOR + kanbanPlanNo;
            TntInvoice invoiceData = invoiceMap.get(invoiceKey);
            if (invoiceData == null) {
                invoiceData = new TntInvoice();
                invoiceData.setInvoiceGroupId(groupData.getInvoiceGroupId());
                invoiceData.setInvoiceSummaryId(summaryData.getInvoiceSummaryId());
                invoiceData.setOfficeId(param.getCurrentOfficeId());
                invoiceData.setInvoiceNo(invoiceNo);
                invoiceData.setOriginalVersion(null);
                invoiceData.setRevisionVersion(1);
                invoiceData.setRevisionReason(null);
                invoiceData.setVanningDate(kanbTotalData.getVanningDate());
                invoiceData.setEtd(etd);
                invoiceData.setEta(eta);
                invoiceData.setCcDate(null);
                invoiceData.setImpInbPlanDate(inbPlanDate);
                invoiceData.setImpCcActualDate(null);
                invoiceData.setImpInbActualDate(null);
                invoiceData.setCreatedBy(param.getLoginUserId());
                invoiceData.setCreatedDate(systemTime);
                invoiceData.setUpdatedBy(param.getLoginUserId());
                invoiceData.setUpdatedDate(systemTime);
                invoiceData.setVersion(1);
                super.baseDao.insert(invoiceData);
                invoiceMap.put(invoiceKey, invoiceData);
            }

            // TNT_INVOICE_PARTS
            TntInvoicePart part = new TntInvoicePart();
            part.setInvoiceId(invoiceData.getInvoiceId());
            part.setPartsId(kanbTotalData.getPartsId());
            part.setSupplierId(kanbTotalData.getSupplierId());
            part.setSupplierPartsNo(null);
            part.setImpPoNo(null);
            part.setCustomerOrderNo(null);
            part.setExpPoNo(null);
            part.setOriginalQty(kanbTotalData.getQty());
            part.setQty(kanbTotalData.getQty());
            part.setCreatedBy(param.getLoginUserId());
            part.setCreatedDate(systemTime);
            part.setUpdatedBy(param.getLoginUserId());
            part.setUpdatedDate(systemTime);
            part.setVersion(1);
            super.baseDao.insert(part);
        }

        // Do invoice process for the parts qty that in Mail Invoice but not in V-net
        List<CPVIVF11MailInvoiceEntity> mailTotalList = new ArrayList<CPVIVF11MailInvoiceEntity>();
        Map<String, Integer> mailTotalMap = new HashMap<String, Integer>();
        for (CPVIVF11MailInvoiceEntity mailInvoice : mailInvoiceList) {
            BigDecimal invoiceQty = mailInvoice.getExcessQty();
            if (!DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)) {
                continue;
            }
            String totalMapKey = mailInvoice.getInvoiceNo() + CPVIVF11Controller.SEPARATOR + mailInvoice.getPartsId();
            Integer totalIndex = mailTotalMap.get(totalMapKey);
            CPVIVF11MailInvoiceEntity mailTotal = null;
            if (totalIndex == null) {
                mailTotal = new CPVIVF11MailInvoiceEntity();
                BeanUtils.copyProperties(mailInvoice, mailTotal);
                mailTotalList.add(mailTotal);
                mailTotalMap.put(totalMapKey, mailTotalList.size() - 1);
            } else {
                mailTotal = mailTotalList.get(totalIndex);
                mailTotal.setExcessQty(DecimalUtil.add(mailTotal.getExcessQty(), mailInvoice.getExcessQty()));
            }
        }

        Map<String, TntInvoice> mailInvoiceMap = new HashMap<String, TntInvoice>();
        for (CPVIVF11MailInvoiceEntity mailTotal : mailTotalList) {
            String invoiceNo = mailTotal.getInvoiceNo();
            Date etd = mailTotal.getEtd();
            Integer transportMode = StringUtil.toInteger(mailTotal.getTransportMode());

            // TNT_INVOICE_SUMMARY
            TntInvoiceSummary summaryData = summaryMap.get(invoiceNo);
            if (summaryData != null) {
                // Update
                summaryData.setUploadStatus(InvoiceUploadStatus.DRAFT);
                super.baseDao.update(summaryData);
            } else {
                // Insert
                summaryData = new TntInvoiceSummary();
                summaryData.setOfficeId(param.getCurrentOfficeId());
                summaryData.setBusinessPattern(BusinessPattern.AISIN);
                summaryData.setInvoiceNo(invoiceNo);
                summaryData.setInvoiceType(InvoiceType.AISIN);
                summaryData.setVesselName(null);
                summaryData.setBlNo(null);
                summaryData.setBlDate(null);
                summaryData.setExpRegion(mailTotal.getExpRegion());
                summaryData.setImpRegion(mailTotal.getImpRegion());
                summaryData.setTransportMode(transportMode);
                summaryData.setSupplierCodeSet(getSupplierCodeSet(mailTotal.getSupplierCodes()));
                summaryData.setEtd(etd);
                summaryData.setEta(null);
                summaryData.setVanningDate(null);
                summaryData.setInvoiceQty(BigDecimal.ZERO);
                summaryData.setInboundQty(BigDecimal.ZERO);
                summaryData.setPostRiFlag(PostRiFlag.N);
                summaryData.setGrDate(null);
                summaryData.setGiDate(null);
                summaryData.setInvoiceStatus(InvoiceStatus.PENDING);
                summaryData.setNonShippingRoute(null);
                summaryData.setUploadId(uploadId);
                summaryData.setUploadedBy(param.getLoginUserId());
                summaryData.setUploadedDate(officeTime);
                summaryData.setUploadStatus(InvoiceUploadStatus.DRAFT);
                summaryData.setCreatedBy(param.getLoginUserId());
                summaryData.setCreatedDate(systemTime);
                summaryData.setUpdatedBy(param.getLoginUserId());
                summaryData.setUpdatedDate(systemTime);
                summaryData.setVersion(1);
                super.baseDao.insert(summaryData);
                summaryMap.put(invoiceNo, summaryData);
            }

            // TNT_INVOICE
            TntInvoice invoiceData = mailInvoiceMap.get(invoiceNo);
            if (invoiceData == null) {
                invoiceData = new TntInvoice();
                invoiceData.setInvoiceGroupId(null);
                invoiceData.setInvoiceSummaryId(summaryData.getInvoiceSummaryId());
                invoiceData.setOfficeId(param.getCurrentOfficeId());
                invoiceData.setInvoiceNo(invoiceNo);
                invoiceData.setOriginalVersion(null);
                invoiceData.setRevisionVersion(1);
                invoiceData.setRevisionReason(null);
                invoiceData.setVanningDate(null);
                invoiceData.setEtd(etd);
                invoiceData.setEta(null);
                invoiceData.setCcDate(null);
                invoiceData.setImpInbPlanDate(null);
                invoiceData.setImpCcActualDate(null);
                invoiceData.setImpInbActualDate(null);
                invoiceData.setCreatedBy(param.getLoginUserId());
                invoiceData.setCreatedDate(systemTime);
                invoiceData.setUpdatedBy(param.getLoginUserId());
                invoiceData.setUpdatedDate(systemTime);
                invoiceData.setVersion(1);
                super.baseDao.insert(invoiceData);
                mailInvoiceMap.put(invoiceNo, invoiceData);
            }

            // TNT_INVOICE_PARTS
            TntInvoicePart part = new TntInvoicePart();
            part.setInvoiceId(invoiceData.getInvoiceId());
            part.setPartsId(mailTotal.getPartsId());
            part.setSupplierId(null);
            part.setSupplierPartsNo(mailTotal.getSuppPartsNo());
            part.setImpPoNo(null);
            part.setCustomerOrderNo(null);
            part.setExpPoNo(null);
            part.setInvCustCode(mailTotal.getInvCustCode());
            part.setOriginalQty(mailTotal.getExcessQty());
            part.setQty(mailTotal.getExcessQty());
            part.setCreatedBy(param.getLoginUserId());
            part.setCreatedDate(systemTime);
            part.setUpdatedBy(param.getLoginUserId());
            part.setUpdatedDate(systemTime);
            part.setVersion(1);
            super.baseDao.insert(part);
        }

        // Update summary invoice qty
        doInvoiceQtyUpdate(summaryMap);

        // Save WEST format invoice data
        Map<String, TntInvoiceContainer> invoiceContainerMap = new HashMap<String, TntInvoiceContainer>();
        for (CPVIVF11KanbEntity kanbData : kanbDataList) {
            TntInvoiceContainer invoiceContainer = new TntInvoiceContainer();
            CPVIVF11MailInvoiceEntity invoiceInfo = kanbData.getMailInvoice();
            invoiceContainer.setInvoiceSummaryId(summaryMap.get(kanbData.getInvoiceNo()).getInvoiceSummaryId());
            invoiceContainer.setContainerNo(invoiceInfo.getContainerNo());
            invoiceContainer.setSealNo(invoiceInfo.getSealNo());
            invoiceContainer.setModuleNo(StringUtil.toString(kanbData.getPalletNo()));
            invoiceContainer.setPartsId(kanbData.getPartsId());
            invoiceContainer.setInvoicePartsNo(invoiceInfo.getInvoicePartNo());
            invoiceContainer.setQty(kanbData.getQty());
            invoiceContainer.setBuyingPrice(invoiceInfo.getPrice());
            invoiceContainer.setBuyingCurrency(invoiceInfo.getCurrency());
            invoiceContainer.setCcDate(null);
            invoiceContainer.setDevannedDate(null);
            invoiceContainer.setStatus(InvoiceContainerStatus.ON_SHIPPING);
            invoiceContainer.setCreatedBy(param.getLoginUserId());
            invoiceContainer.setCreatedDate(systemTime);
            invoiceContainer.setUpdatedBy(param.getLoginUserId());
            invoiceContainer.setUpdatedDate(systemTime);
            invoiceContainer.setVersion(1);
            super.baseDao.insert(invoiceContainer);
            String mapKey = invoiceContainer.getInvoiceSummaryId() + CPVIVF11Controller.SEPARATOR
                    + invoiceContainer.getModuleNo() + CPVIVF11Controller.SEPARATOR + invoiceContainer.getPartsId();
            invoiceContainerMap.put(mapKey, invoiceContainer);
        }

        for (CPVIVF11MailInvoiceEntity mailInvoice : mailInvoiceList) {
            BigDecimal qty = mailInvoice.getQty();
            BigDecimal excessQty = mailInvoice.getExcessQty();
            String mapKey = summaryMap.get(mailInvoice.getInvoiceNo()).getInvoiceSummaryId()
                    + CPVIVF11Controller.SEPARATOR + mailInvoice.getStartPalletNo() + CPVIVF11Controller.SEPARATOR
                    + mailInvoice.getPartsId();
            TntInvoiceContainer invoiceContainer = null;
            if (!DecimalUtil.isEquals(qty, excessQty)) {
                invoiceContainer = invoiceContainerMap.get(mapKey);
            }
            if (invoiceContainer == null) {
                invoiceContainer = new TntInvoiceContainer();
                invoiceContainer.setInvoiceSummaryId(summaryMap.get(mailInvoice.getInvoiceNo()).getInvoiceSummaryId());
                invoiceContainer.setContainerNo(mailInvoice.getContainerNo());
                invoiceContainer.setSealNo(mailInvoice.getSealNo());
                invoiceContainer.setModuleNo(StringUtil.toString(mailInvoice.getStartPalletNo()));
                invoiceContainer.setPartsId(mailInvoice.getPartsId());
                invoiceContainer.setInvoicePartsNo(mailInvoice.getInvoicePartNo());
                invoiceContainer.setQty(mailInvoice.getExcessQty());
                invoiceContainer.setBuyingPrice(mailInvoice.getPrice());
                invoiceContainer.setBuyingCurrency(mailInvoice.getCurrency());
                invoiceContainer.setCcDate(null);
                invoiceContainer.setDevannedDate(null);
                invoiceContainer.setStatus(InvoiceContainerStatus.ON_SHIPPING);
                invoiceContainer.setCreatedBy(param.getLoginUserId());
                invoiceContainer.setCreatedDate(systemTime);
                invoiceContainer.setUpdatedBy(param.getLoginUserId());
                invoiceContainer.setUpdatedDate(systemTime);
                invoiceContainer.setVersion(1);
                super.baseDao.insert(invoiceContainer);
                invoiceContainerMap.put(mapKey, invoiceContainer);
            } else {
                invoiceContainer.setQty(DecimalUtil.add(invoiceContainer.getQty(), mailInvoice.getExcessQty()));
                super.baseDao.update(invoiceContainer);
            }
        }
        super.baseDao.flush();

        // Find completed invoices in this upload
        List<CPVIVF11SupportEntity> invoiceList = getCompletedInvoices(uploadId);

        // Automatic adjustment plan and order status
        doPlanAdjust(invoiceList, param.getLoginUserId(), systemTime);
    }

    /**
     * Automatic adjustment plan and order status.
     * 
     * @param invoiceList invoice list
     * @param loginUserId login user ID
     * @param systemTime system time
     */
    public void doPlanAdjust(List<CPVIVF11SupportEntity> invoiceList, Integer loginUserId, Timestamp systemTime) {

        // Create KANBAN invoice data
        Map<String, CPVIVF11AdjustEntity> kanbanInvoiceMap = new LinkedHashMap<String, CPVIVF11AdjustEntity>();
        for (CPVIVF11SupportEntity invoice : invoiceList) {
            String kanbanPlanNo = invoice.getKanbanPlanNo();
            String invoiceNo = invoice.getInvoiceNo();
            String kanbanInvoiceKey = kanbanPlanNo + CPVIVF11Controller.SEPARATOR + invoiceNo;
            CPVIVF11AdjustEntity kanbanInvoice = kanbanInvoiceMap.get(kanbanInvoiceKey);
            Map<Integer, BigDecimal> partQtyMap = null;
            if (kanbanInvoice == null) {
                kanbanInvoice = new CPVIVF11AdjustEntity();
                kanbanInvoice.setKanbanPlanNo(kanbanPlanNo);
                kanbanInvoice.setInvoiceNo(invoiceNo);
                kanbanInvoice.setTransportMode(invoice.getTransMode());
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
        for (Map.Entry<String, CPVIVF11AdjustEntity> entry : kanbanInvoiceMap.entrySet()) {
            CPVIVF11AdjustEntity kanbanInvoice = entry.getValue();
            String kanbanPlanNo = kanbanInvoice.getKanbanPlanNo();
            Integer kanbanId = null;
            String orderMonth = null;
            Integer invoiceTransMode = kanbanInvoice.getTransportMode();
            Date invoiceEtd = kanbanInvoice.getEtd();
            Map<Integer, BigDecimal> invoiceQtyMap = kanbanInvoice.getPartQtyMap();
            Map<Integer, Integer> adjustShippingMap = new HashMap<Integer, Integer>();

            // Update TNF_ORDER_STATUS
            for (Map.Entry<Integer, BigDecimal> invoiceQtyEntry : invoiceQtyMap.entrySet()) {
                Integer partsId = invoiceQtyEntry.getKey();
                BigDecimal qty = invoiceQtyEntry.getValue();
                TnfOrderStatus orderCondition = new TnfOrderStatus();
                orderCondition.setKanbanPlanNo(kanbanPlanNo);
                orderCondition.setPartsId(partsId);
                List<TnfOrderStatus> orderDatas = super.baseDao.select(orderCondition);
                if (orderDatas.size() > 0) {
                    TnfOrderStatus orderData = orderDatas.get(0);
                    orderData.setExpOnshippingQty(DecimalUtil.add(orderData.getExpOnshippingQty(), qty));
                    orderData.setUpdatedBy(loginUserId);
                    orderData.setUpdatedDate(systemTime);
                    orderData.setVersion(orderData.getVersion() + IntDef.INT_ONE);
                    super.baseDao.update(orderData);
                }
            }

            // Find need adjust KANBAN plan
            Map<Integer, CPVIVF11AdjustEntity> seaPlanPartsMap = new LinkedHashMap<Integer, CPVIVF11AdjustEntity>();
            Map<Integer, CPVIVF11AdjustEntity> airPlanPartsMap = new LinkedHashMap<Integer, CPVIVF11AdjustEntity>();
            List<CPVIVF11AdjustEntity> needAdjustPlans = getNeedAdjustPlans(kanbanPlanNo);
            for (CPVIVF11AdjustEntity needAdjustPlan : needAdjustPlans) {
                kanbanId = needAdjustPlan.getKanbanId();
                orderMonth = needAdjustPlan.getOrderMonth();
                Integer kanbanShippingId = needAdjustPlan.getKanbanShippingId();
                Integer planTransMode = needAdjustPlan.getTransportMode();
                CPVIVF11AdjustEntity planParts = null;
                if (TransportMode.SEA == planTransMode) {
                    planParts = seaPlanPartsMap.get(kanbanShippingId);
                } else {
                    planParts = airPlanPartsMap.get(kanbanShippingId);
                }
                Map<Integer, BigDecimal[]> planQtyMap = null;
                if (planParts == null) {
                    planParts = new CPVIVF11AdjustEntity();
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

            // Find all KANBAN parts
            if (kanbanId != null) {
                Map<Integer, BigDecimal> spqMap = new HashMap<Integer, BigDecimal>();
                List<CPVIVF11AdjustEntity> allKanbanParts = getAllKanbanParts(kanbanId);
                for (CPVIVF11AdjustEntity kanbanPart : allKanbanParts) {
                    // If the part's STATUS = 9(Force Completed), then no adjust and forward to next part
                    if (KanbanPartsStatus.FORCE_COMPLETED == kanbanPart.getPartStatus()) {
                        continue;
                    }

                    // Invoice Qty
                    Integer partsId = kanbanPart.getPartsId();
                    spqMap.put(partsId, kanbanPart.getSpq());
                    BigDecimal invoiceQty = invoiceQtyMap.get(partsId);
                    if (invoiceQty == null) {
                        invoiceQty = BigDecimal.ZERO;
                    }

                    // Loop each plan that with the same transport mode as the invoice.
                    Map<Integer, CPVIVF11AdjustEntity> sameModePartsMap = null;
                    if (TransportMode.SEA == invoiceTransMode) {
                        sameModePartsMap = seaPlanPartsMap;
                    } else {
                        sameModePartsMap = airPlanPartsMap;
                    }
                    for (Map.Entry<Integer, CPVIVF11AdjustEntity> planEntry : sameModePartsMap.entrySet()) {
                        CPVIVF11AdjustEntity planData = planEntry.getValue();
                        Integer nirdFlag = planData.getNirdFlag();
                        Integer kanbanShippingId = planData.getKanbanShippingId();
                        Date planEtd = planData.getEtd();
                        Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
                        BigDecimal planQty = getQty(planQtyMap, QTY_TYPE_PLAN, partsId);
                        if (KbsNirdFlag.NOT_IN_RUNDOWN == nirdFlag) {
                            // the plan's NIRD_FLAG = 1(Not in rundown)
                            if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)
                                    && DecimalUtil.isLessEquals(invoiceQty, planQty)) {
                                // 0 < Invoice Qty <= Plan Qty
                                putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                    DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                invoiceQty = BigDecimal.ZERO;
                                planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                adjustShippingMap.put(kanbanShippingId, nirdFlag);
                            } else if (DecimalUtil.isGreater(planQty, BigDecimal.ZERO)
                                    && DecimalUtil.isGreater(invoiceQty, planQty)) {
                                // Invoice Qty > Plan Qty > 0
                                putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                    DecimalUtil.subtract(BigDecimal.ZERO, planQty));
                                invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                adjustShippingMap.put(kanbanShippingId, nirdFlag);
                            }
                        } else {
                            // the plan's NIRD_FLAG = 0(Normal)
                            if (invoiceEtd.compareTo(planEtd) >= 0) {
                                // invoice's ETD >= plan's ETD, Invoice Delay
                                planData.setCompletedFlag(KbsCompletedFlag.COMPLETED);
                                adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                if (DecimalUtil.isLessEquals(invoiceQty, BigDecimal.ZERO)) {
                                    // Invoice Qty <= 0
                                    putQty(planQtyMap, QTY_TYPE_NIRD, partsId, planQty);
                                    invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                } else {
                                    // Invoice Qty > 0
                                    putQty(planQtyMap, QTY_TYPE_NIRD, partsId,
                                        DecimalUtil.subtract(planQty, invoiceQty));
                                    invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                }
                            } else {
                                // invoice's ETD < plan's ETD, Invoice Advance
                                if (DecimalUtil.isEquals(invoiceQty, BigDecimal.ZERO)) {
                                    // Invoice Qty = 0
                                    break;
                                } else {
                                    // Invoice Qty != 0
                                    if (DecimalUtil.isLess(invoiceQty, BigDecimal.ZERO)) {
                                        // Invoice Qty < 0, adjust the plan to add delay qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                        invoiceQty = BigDecimal.ZERO;
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                        break;
                                    } else if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)
                                            && DecimalUtil.isLessEquals(invoiceQty, planQty)) {
                                        // 0 < Invoice Qty <= Plan Qty, adjust the plan to remove advance qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                        invoiceQty = BigDecimal.ZERO;
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                        break;
                                    } else if (DecimalUtil.isGreater(planQty, BigDecimal.ZERO)
                                            && DecimalUtil.isGreater(invoiceQty, planQty)) {
                                        // Invoice Qty > Plan Qty > 0, adjust the plan to remove advance qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, planQty));
                                        invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                    }
                                }
                            }
                        }
                    }

                    // If Invoice Qty != 0, then adjust the plan with difference transport mode
                    if (!DecimalUtil.isEquals(invoiceQty, BigDecimal.ZERO)) {
                        if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)) {
                            // Invoice Qty > 0, then adjuset the plan to remove the advance qty.
                            Map<Integer, CPVIVF11AdjustEntity> otherModePartsMap = null;
                            if (TransportMode.SEA == invoiceTransMode) {
                                otherModePartsMap = airPlanPartsMap;
                            } else {
                                otherModePartsMap = seaPlanPartsMap;
                            }
                            for (Map.Entry<Integer, CPVIVF11AdjustEntity> planEntry : otherModePartsMap.entrySet()) {
                                CPVIVF11AdjustEntity planData = planEntry.getValue();
                                Integer nirdFlag = planData.getNirdFlag();
                                Integer kanbanShippingId = planData.getKanbanShippingId();
                                Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
                                BigDecimal planQty = getQty(planQtyMap, QTY_TYPE_PLAN, partsId);
                                if (KbsNirdFlag.NOT_IN_RUNDOWN == nirdFlag) {
                                    // the plan's NIRD_FLAG = 1(Not in rundown)
                                    if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)
                                            && DecimalUtil.isLessEquals(invoiceQty, planQty)) {
                                        // 0 < Invoice Qty <= Plan Qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                        invoiceQty = BigDecimal.ZERO;
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                        break;
                                    } else if (DecimalUtil.isGreater(planQty, BigDecimal.ZERO)
                                            && DecimalUtil.isGreater(invoiceQty, planQty)) {
                                        // Invoice Qty > Plan Qty > 0
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, planQty));
                                        invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                    }
                                } else {
                                    // the plan's NIRD_FLAG = 0(Normal)
                                    if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)
                                            && DecimalUtil.isLessEquals(invoiceQty, planQty)) {
                                        // 0 < Invoice Qty <= Plan Qty, adjust plan to remove advance qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                        invoiceQty = BigDecimal.ZERO;
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                        break;
                                    } else if (DecimalUtil.isGreater(planQty, BigDecimal.ZERO)
                                            && DecimalUtil.isGreater(invoiceQty, planQty)) {
                                        // Invoice Qty > Plan Qty > 0, adjust the plan to remove advance qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, planQty));
                                        invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                    }
                                }
                            }
                        } else {
                            // Invoice Qty < 0, then add a new plan or move the qty to not in rundown.
                            if (TransportMode.SEA == invoiceTransMode) {
                                // invoice transport mode = 1(Sea), add a new plan and move the qty to this plan
                                List<SupplyChainEntity> chainList = new ArrayList<SupplyChainEntity>();
                                SupplyChainEntity chainEntity = new SupplyChainEntity();
                                chainEntity.setTansportMode(TransportMode.SEA);
                                chainEntity.setExpPartsId(kanbanPart.getExpPartsId());
                                chainEntity.setChainStartDate(invoiceEtd);
                                chainList.add(chainEntity);
                                supplyChainService.prepareSupplyChain(chainList, ChainStep.NEXT_ETD,
                                    BusinessPattern.AISIN, false);
                                SupplyChainEntity resultChain = chainList.get(0);
                                Date foundEtd = resultChain.getEtd();
                                Date foundEta = resultChain.getEta();
                                Date foundInbDate = resultChain.getImpPlanInboundDate();
                                if (foundEtd != null && foundEta != null && foundInbDate != null) {
                                    // Insert into TNT_KANBAN_SHIPPING
                                    TntKanbanShipping shipping = new TntKanbanShipping();
                                    shipping.setKanbanId(kanbanId);
                                    shipping.setShippingUuid(UUID.randomUUID().toString());
                                    shipping.setTransportMode(TransportMode.SEA);
                                    shipping.setEtd(foundEtd);
                                    shipping.setEta(foundEta);
                                    shipping.setImpInbPlanDate(foundInbDate);
                                    shipping.setOriginalVersion(getNextOriginalVersion(kanbanId));
                                    shipping.setRevisionVersion(null);
                                    shipping.setRevisionReason(SYSTEM_ADJUST_REASON);
                                    shipping.setCompletedFlag(KbsCompletedFlag.NORMAL);
                                    shipping.setNirdFlag(KbsNirdFlag.NORMAL);
                                    shipping.setCreatedBy(loginUserId);
                                    shipping.setCreatedDate(systemTime);
                                    shipping.setUpdatedBy(loginUserId);
                                    shipping.setUpdatedDate(systemTime);
                                    shipping.setVersion(1);
                                    super.baseDao.insert(shipping);

                                    // Insert into TNT_KANBAN_PLAN
                                    TntKanbanPlan kanbanPlan = new TntKanbanPlan();
                                    kanbanPlan.setKanbanId(kanbanId);
                                    kanbanPlan.setShippingUuid(shipping.getShippingUuid());
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

                                    // Insert into TNT_KANBAN_PLAN_PARTS
                                    TntKanbanPlanPart kanbanPlanPart = new TntKanbanPlanPart();
                                    kanbanPlanPart.setKanbanPlanId(kanbanPlan.getKanbanPlanId());
                                    kanbanPlanPart.setPartsId(partsId);
                                    kanbanPlanPart.setSpq(null);
                                    kanbanPlanPart.setQty(DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                    kanbanPlanPart.setKanbanQty(null);
                                    kanbanPlanPart.setCreatedBy(loginUserId);
                                    kanbanPlanPart.setCreatedDate(systemTime);
                                    kanbanPlanPart.setUpdatedBy(loginUserId);
                                    kanbanPlanPart.setUpdatedDate(systemTime);
                                    kanbanPlanPart.setVersion(1);
                                    super.baseDao.insert(kanbanPlanPart);

                                    // Insert into TNT_KANBAN_SHIPPING_PARTS
                                    TntKanbanShippingPart shippingPart = new TntKanbanShippingPart();
                                    shippingPart.setKanbanShippingId(shipping.getKanbanShippingId());
                                    shippingPart.setPartsId(partsId);
                                    shippingPart.setSpq(null);
                                    shippingPart.setQty(DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                    shippingPart.setKanbanQty(null);
                                    shippingPart.setCreatedBy(loginUserId);
                                    shippingPart.setCreatedDate(systemTime);
                                    shippingPart.setUpdatedBy(loginUserId);
                                    shippingPart.setUpdatedDate(systemTime);
                                    shippingPart.setVersion(1);
                                    super.baseDao.insert(shippingPart);

                                    // add the new plan to plan map
                                    CPVIVF11AdjustEntity newMapPlan = new CPVIVF11AdjustEntity();
                                    newMapPlan.setKanbanId(kanbanId);
                                    newMapPlan.setKanbanPlanNo(kanbanPlanNo);
                                    newMapPlan.setOrderMonth(orderMonth);
                                    newMapPlan.setKanbanShippingId(shipping.getKanbanShippingId());
                                    newMapPlan.setShippingUUID(shipping.getShippingUuid());
                                    newMapPlan.setOriginalVersion(shipping.getOriginalVersion());
                                    newMapPlan.setRevisionVersion(shipping.getRevisionVersion());
                                    newMapPlan.setTransportMode(shipping.getTransportMode());
                                    newMapPlan.setCompletedFlag(shipping.getCompletedFlag());
                                    newMapPlan.setNirdFlag(shipping.getNirdFlag());
                                    newMapPlan.setEtd(shipping.getEtd());
                                    newMapPlan.setEta(shipping.getEta());
                                    newMapPlan.setInbPlanDate(shipping.getImpInbPlanDate());
                                    newMapPlan.setDifferBoxId(null);
                                    newMapPlan.setDummyBoxId(kanbanPlan.getKanbanPlanId());
                                    Map<Integer, BigDecimal[]> newQtyMap = new HashMap<Integer, BigDecimal[]>();
                                    putQty(newQtyMap, QTY_TYPE_PLAN, shippingPart.getPartsId(), shippingPart.getQty());
                                    newMapPlan.setPlanQtyMap(newQtyMap);
                                    seaPlanPartsMap.put(shipping.getKanbanShippingId(), newMapPlan);
                                } else {
                                    // non shipping route found, then move the qty to not in rundown
                                    moveToNotInRundown(sameModePartsMap, partsId, kanbanId, loginUserId, systemTime);
                                }
                            } else {
                                // invoice transport mode = 2(Air), move the qty to not in rundown
                                moveToNotInRundown(sameModePartsMap, partsId, kanbanId, loginUserId, systemTime);
                            }
                        }
                    }
                }

                // Do KANBAN Plan Adjustment
                adjustKanbanPlan(seaPlanPartsMap, loginUserId, systemTime, spqMap);
                adjustKanbanPlan(airPlanPartsMap, loginUserId, systemTime, spqMap);

                // Insert TNT_INVOICE_SHIPPING
                for (Map.Entry<Integer, Integer> adjustEntry : adjustShippingMap.entrySet()) {
                    TntInvoiceShipping invoiceShipping = new TntInvoiceShipping();
                    invoiceShipping.setInvoiceNo(kanbanInvoice.getInvoiceNo());
                    invoiceShipping.setKanbanShippingId(adjustEntry.getKey());
                    invoiceShipping.setStatus(InvoiceShippingStatus.NORMAL);
                    invoiceShipping.setNirdFlag(adjustEntry.getValue());
                    invoiceShipping.setCreatedBy(loginUserId);
                    invoiceShipping.setCreatedDate(systemTime);
                    invoiceShipping.setUpdatedBy(loginUserId);
                    invoiceShipping.setUpdatedDate(systemTime);
                    invoiceShipping.setVersion(1);
                    super.baseDao.insert(invoiceShipping);
                }
            }
        }
    }

    /**
     * Adjust KANBAN plan.
     * 
     * @param planPartsMap parts map
     * @param loginUserId login user ID
     * @param systemTime system time
     * @param spqMap SPQ map
     */
    private void adjustKanbanPlan(Map<Integer, CPVIVF11AdjustEntity> planPartsMap, Integer loginUserId,
        Timestamp systemTime, Map<Integer, BigDecimal> spqMap) {

        for (Map.Entry<Integer, CPVIVF11AdjustEntity> planEntry : planPartsMap.entrySet()) {
            CPVIVF11AdjustEntity planData = planEntry.getValue();
            Integer kanbanShippingId = planData.getKanbanShippingId();
            Integer completedFlag = planData.getCompletedFlag();
            Integer nirdFlag = planData.getNirdFlag();
            Integer differBoxId = planData.getDifferBoxId();
            Integer dummyBoxId = planData.getDummyBoxId();

            if (KbsCompletedFlag.COMPLETED == completedFlag) {
                // the plan's COMPLETED_FLAG = 1(Completed), update TNT_KANBAN_SHIPPING
                TntKanbanShipping shippingData = super.getOneById(TntKanbanShipping.class, kanbanShippingId);
                shippingData.setCompletedFlag(KbsCompletedFlag.COMPLETED);
                shippingData.setUpdatedBy(loginUserId);
                shippingData.setUpdatedDate(systemTime);
                shippingData.setVersion(shippingData.getVersion() + 1);
                super.baseDao.update(shippingData);
            } else if (KbsCompletedFlag.HISTORY == completedFlag) {
                // the plan's COMPLETED_FLAG = 2(History)
                boolean needCompleted = true;
                Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
                for (Map.Entry<Integer, BigDecimal[]> qtyEntry : planQtyMap.entrySet()) {
                    BigDecimal[] qtyArray = qtyEntry.getValue();
                    BigDecimal planQty = qtyArray[QTY_TYPE_PLAN];
                    BigDecimal diffQty = qtyArray[QTY_TYPE_DIFF];
                    BigDecimal totalQty = DecimalUtil.add(planQty, diffQty);
                    if (!DecimalUtil.isEquals(totalQty, BigDecimal.ZERO)) {
                        needCompleted = false;
                        break;
                    }
                }
                if (needCompleted) {
                    // Update old TNT_KANBAN_SHIPPING
                    TntKanbanShipping shippingData = super.getOneById(TntKanbanShipping.class, kanbanShippingId);
                    shippingData.setCompletedFlag(KbsCompletedFlag.COMPLETED);
                    shippingData.setNirdFlag(KbsNirdFlag.NORMAL);
                    shippingData.setUpdatedBy(loginUserId);
                    shippingData.setUpdatedDate(systemTime);
                    shippingData.setVersion(shippingData.getVersion() + 1);
                    super.baseDao.update(shippingData);
                } else {
                    // Update old TNT_KANBAN_SHIPPING
                    TntKanbanShipping oldShipping = super.getOneById(TntKanbanShipping.class, kanbanShippingId);
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
                    newShipping.setRevisionReason(SYSTEM_ADJUST_REASON);
                    newShipping.setCompletedFlag(KbsCompletedFlag.NORMAL);
                    newShipping.setNirdFlag(nirdFlag);
                    newShipping.setCreatedBy(loginUserId);
                    newShipping.setCreatedDate(systemTime);
                    newShipping.setUpdatedBy(loginUserId);
                    newShipping.setUpdatedDate(systemTime);
                    newShipping.setVersion(1);
                    super.baseDao.insert(newShipping);

                    // Insert difference plan in TNT_KANBAN_PLAN
                    if (KbsNirdFlag.NORMAL == nirdFlag && dummyBoxId == null && differBoxId == null) {
                        TntKanbanPlan differPlan = new TntKanbanPlan();
                        differPlan.setKanbanId(oldShipping.getKanbanId());
                        differPlan.setShippingUuid(oldShipping.getShippingUuid());
                        differPlan.setOrderMonth(planData.getOrderMonth());
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
                        BigDecimal planQty = qtyArray[QTY_TYPE_PLAN];
                        BigDecimal diffQty = qtyArray[QTY_TYPE_DIFF];
                        BigDecimal totalQty = DecimalUtil.add(planQty, diffQty);

                        // Insert into TNT_KANBAN_SHIPPING_PARTS
                        TntKanbanShippingPart shippingPart = new TntKanbanShippingPart();
                        shippingPart.setKanbanShippingId(newShipping.getKanbanShippingId());
                        shippingPart.setPartsId(partsId);
                        shippingPart.setSpq(null);
                        shippingPart.setQty(totalQty);
                        shippingPart.setKanbanQty(null);
                        shippingPart.setCreatedBy(loginUserId);
                        shippingPart.setCreatedDate(systemTime);
                        shippingPart.setUpdatedBy(loginUserId);
                        shippingPart.setUpdatedDate(systemTime);
                        shippingPart.setVersion(1);
                        super.baseDao.insert(shippingPart);

                        // Insert into TNT_KANBAN_PLAN_PARTS
                        if (KbsNirdFlag.NORMAL == nirdFlag && diffQty != null
                                && !DecimalUtil.isEquals(diffQty, BigDecimal.ZERO)) {
                            BigDecimal kanbanQty = DecimalUtil.divide(diffQty, spqMap.get(partsId), IntDef.INT_TWO);
                            // Find exist part
                            TntKanbanPlanPart kanbanPlanPart = null;
                            Integer kanbanPlanId = dummyBoxId != null ? dummyBoxId : differBoxId;
                            TntKanbanPlanPart partCondition = new TntKanbanPlanPart();
                            partCondition.setPartsId(partsId);
                            partCondition.setKanbanPlanId(kanbanPlanId);
                            List<TntKanbanPlanPart> existParts = super.baseDao.select(partCondition);
                            if (existParts != null && existParts.size() > 0) {
                                kanbanPlanPart = existParts.get(0);
                                kanbanPlanPart.setQty(dummyBoxId != null ? totalQty : null);
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
                                kanbanPlanPart.setQty(dummyBoxId != null ? totalQty : null);
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
     * Move qty to not in rundown.
     * 
     * @param sameModePartsMap parts map
     * @param partsId parts ID
     * @param kanbanId KANBAN ID
     * @param loginUserId login user ID
     * @param systemTime system time
     */
    private void moveToNotInRundown(Map<Integer, CPVIVF11AdjustEntity> sameModePartsMap, Integer partsId,
        Integer kanbanId, Integer loginUserId, Timestamp systemTime) {

        for (Map.Entry<Integer, CPVIVF11AdjustEntity> planEntry : sameModePartsMap.entrySet()) {
            CPVIVF11AdjustEntity planData = planEntry.getValue();
            Integer nirdFlag = planData.getNirdFlag();
            Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
            BigDecimal nirdQty = getQty(planQtyMap, QTY_TYPE_NIRD, partsId);
            if (KbsNirdFlag.NORMAL == nirdFlag && DecimalUtil.isGreater(nirdQty, BigDecimal.ZERO)) {
                // Find exist not in rundown KANBAN shipping
                TntKanbanShipping nirdData = null;
                TntKanbanShipping nirdCondition = new TntKanbanShipping();
                nirdCondition.setKanbanId(kanbanId);
                nirdCondition.setNirdFlag(KbsNirdFlag.NOT_IN_RUNDOWN);
                nirdCondition.setTransportMode(planData.getTransportMode());
                nirdCondition.setEtd(planData.getEtd());
                nirdCondition.setEta(planData.getEta());
                nirdCondition.setImpInbPlanDate(planData.getInbPlanDate());
                List<TntKanbanShipping> nirdDatas = super.baseDao.select(nirdCondition);
                if (nirdDatas != null && nirdDatas.size() > 0) {
                    nirdData = nirdDatas.get(0);
                } else {
                    // Insert into TNT_KANBAN_SHIPPING
                    nirdData = new TntKanbanShipping();
                    nirdData.setKanbanId(kanbanId);
                    nirdData.setShippingUuid(planData.getShippingUUID());
                    nirdData.setTransportMode(planData.getTransportMode());
                    nirdData.setEtd(planData.getEtd());
                    nirdData.setEta(planData.getEta());
                    nirdData.setImpInbPlanDate(planData.getInbPlanDate());
                    nirdData.setOriginalVersion(planData.getOriginalVersion());
                    nirdData.setRevisionVersion(getNextRevisionVersion(planData.getRevisionVersion()));
                    nirdData.setRevisionReason(SYSTEM_ADJUST_REASON);
                    nirdData.setCompletedFlag(KbsCompletedFlag.NORMAL);
                    nirdData.setNirdFlag(KbsNirdFlag.NOT_IN_RUNDOWN);
                    nirdData.setCreatedBy(loginUserId);
                    nirdData.setCreatedDate(systemTime);
                    nirdData.setUpdatedBy(loginUserId);
                    nirdData.setUpdatedDate(systemTime);
                    nirdData.setVersion(1);
                    super.baseDao.insert(nirdData);
                }

                // Insert into TNT_KANBAN_SHIPPING_PARTS
                TntKanbanShippingPart nirdPart = new TntKanbanShippingPart();
                nirdPart.setKanbanShippingId(nirdData.getKanbanShippingId());
                nirdPart.setPartsId(partsId);
                nirdPart.setSpq(null);
                nirdPart.setQty(nirdQty);
                nirdPart.setKanbanQty(null);
                nirdPart.setCreatedBy(loginUserId);
                nirdPart.setCreatedDate(systemTime);
                nirdPart.setUpdatedBy(loginUserId);
                nirdPart.setUpdatedDate(systemTime);
                nirdPart.setVersion(1);
                super.baseDao.insert(nirdPart);
            }
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

    /**
     * Get next original version.
     * 
     * @param kanbanId the KANBAN ID
     * @return next original version
     */
    public Integer getNextOriginalVersion(Integer kanbanId) {

        super.baseDao.flush();
        CPVIVF11AdjustEntity condition = new CPVIVF11AdjustEntity();
        condition.setKanbanId(kanbanId);
        List<CPVIVF11AdjustEntity> result = super.baseMapper.select(getSqlId(SQLID_FIND_MAX_ORIGINAL_VERSION),
            condition);
        if (result.size() > 0) {
            return result.get(0).getOriginalVersion() + IntDef.INT_ONE;
        }

        return IntDef.INT_ONE;
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
            qtyArray = new BigDecimal[IntDef.INT_THREE];
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
     * Update summary invoice qty.
     * 
     * @param summaryMap the invoice summary map
     */
    private void doInvoiceQtyUpdate(Map<String, TntInvoiceSummary> summaryMap) {

        super.baseDao.flush();
        for (Map.Entry<String, TntInvoiceSummary> entry : summaryMap.entrySet()) {
            CPVIVF11SupportEntity condition = new CPVIVF11SupportEntity();
            condition.setInvoiceSummaryId(entry.getValue().getInvoiceSummaryId());
            super.baseMapper.update(getSqlId(SQLID_UPDATE_INVOICE_SUMMARY_QTY), condition);
        }
    }

    /**
     * Get invoice group data.
     * 
     * @param kanbanPlanNo KANBAN Plan No.
     * @param vesselName Vessel Name
     * @param etd ETD
     * @return invoice group data
     */
    private TntInvoiceGroup getInvoiceGroup(String kanbanPlanNo, String vesselName, Date etd) {

        TntInvoiceGroup condition = new TntInvoiceGroup();
        condition.setKanbanPlanNo(kanbanPlanNo);
        condition.setVesselName(vesselName);
        condition.setEtd(etd);
        condition.setStatus(InvoiceGroupStatus.NORMAL);
        List<TntInvoiceGroup> groupList = super.baseDao.select(condition);
        if (groupList != null && groupList.size() > 0) {
            return groupList.get(0);
        }
        return null;
    }

    /**
     * Get supplier code set.
     * 
     * @param supplierCodes the supplier code list
     * @return supplier code set
     */
    private String getSupplierCodeSet(List<String> supplierCodes) {

        if (supplierCodes != null && supplierCodes.size() > 0) {
            Collections.sort(supplierCodes);
            StringBuffer sbCode = new StringBuffer();
            for (String supplierCode : supplierCodes) {
                sbCode.append(StringConst.COMMA).append(supplierCode);
            }
            return sbCode.substring(1);
        }

        return null;
    }

}
