/**
 * CPVIVF12Service.java
 * 
 * @screen CPVIVF12
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.InvoiceGroupStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceUploadStatus;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceGroup;
import com.chinaplus.common.entity.TntInvoicePart;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inv.entity.CPVIVF11SupportEntity;
import com.chinaplus.web.inv.entity.CPVIVF12Entity;

/**
 * Invoice Supplementary Upload Service.
 */
@Service
public class CPVIVF12Service extends BaseService {

    /** SQL ID: find invoice part */
    private static final String SQLID_FIND_INVOICE_PART = "findInvoicePart";

    /** SQL ID: find all supplementary data */
    private static final String SQLID_FIND_ALL_SUPPLEMENTARY_DATA = "findAllSupplementaryData";

    /** SQL ID: find completed invoices */
    private static final String SQLID_FIND_COMPLETED_INVOICES = "findCompletedInvoices";

    /** SQL ID: delete invoice parts */
    private static final String SQLID_DELETE_INVOICE_PARTS = "deleteInvoiceParts";

    /** SQL ID: delete invoice */
    private static final String SQLID_DELETE_INVOICE = "deleteInvoice";

    /** Invoice Upload Service */
    @Autowired
    private CPVIVF11Service cpvivf11Service;

    /**
     * Get invoice part information.
     * 
     * @param condition search condition
     * @return invoice part information
     */
    public CPVIVF12Entity getInvoicePart(CPVIVF12Entity condition) {

        List<CPVIVF12Entity> result = super.baseMapper.select(getSqlId(SQLID_FIND_INVOICE_PART), condition);
        if (result == null || result.size() != 1) {
            return null;
        }

        return result.get(0);
    }

    /**
     * Get all supplementary data.
     * 
     * @param uploadId the upload ID
     * @return all supplementary data
     */
    public List<CPVIVF12Entity> getAllSupplementaryData(String uploadId) {

        CPVIVF12Entity condition = new CPVIVF12Entity();
        condition.setUploadId(uploadId);
        return super.baseMapper.select(getSqlId(SQLID_FIND_ALL_SUPPLEMENTARY_DATA), condition);
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
        return super.baseMapper.select(getSqlId(SQLID_FIND_COMPLETED_INVOICES), condition);
    }

    /**
     * Update supplementary invoice.
     * 
     * @param param common parameter
     * @param dataList upload data list
     */
    public void doInvoiceUpdate(BaseParam param, List<CPVIVF12Entity> dataList) {

        // Delete supplementary data in DB
        String uploadId = dataList.get(0).getUploadId();
        CPVIVF12Entity deleteCondition = new CPVIVF12Entity();
        deleteCondition.setUploadId(uploadId);
        super.baseMapper.delete(SQLID_DELETE_INVOICE_PARTS, deleteCondition);
        super.baseMapper.delete(SQLID_DELETE_INVOICE, deleteCondition);

        // Save new invoice information for supplementary data
        Map<Integer, Date> summaryDateMap = new HashMap<Integer, Date>();
        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
        for (CPVIVF12Entity data : dataList) {
            saveSupplementaryData(param, data, systemTime, IntDef.INT_ONE, summaryDateMap);
            saveSupplementaryData(param, data, systemTime, IntDef.INT_TWO, summaryDateMap);
            saveSupplementaryData(param, data, systemTime, IntDef.INT_THREE, summaryDateMap);
        }
        super.baseDao.flush();

        // Find completed invoice after upload
        List<CPVIVF11SupportEntity> invoiceList = getCompletedInvoices(uploadId);
        cpvivf11Service.doPlanAdjust(invoiceList, param.getLoginUserId(), systemTime);

        // Update TNT_INVOICE_SUMMARY's UPLOAD_STATUS
        TntInvoiceSummary summaryCondition = new TntInvoiceSummary();
        summaryCondition.setUploadId(uploadId);
        summaryCondition.setUploadStatus(InvoiceUploadStatus.DRAFT);
        List<TntInvoiceSummary> summaryDatas = super.baseDao.select(summaryCondition);
        if (summaryDatas != null && summaryDatas.size() > 0) {
            for (TntInvoiceSummary summaryData : summaryDatas) {
                if (summaryData.getEta() == null) {
                    summaryData.setEta(summaryDateMap.get(summaryData.getInvoiceSummaryId()));
                }
                summaryData.setUploadStatus(InvoiceUploadStatus.DONE);
                summaryData.setUpdatedBy(param.getLoginUserId());
                summaryData.setUpdatedDate(systemTime);
                summaryData.setVersion(summaryData.getVersion() + 1);
                super.baseDao.update(summaryData);
            }
        }
    }

    /**
     * Save supplementary data.
     * 
     * @param param common parameter
     * @param data upload data
     * @param systemTime system data time
     * @param number order month number
     * @param summaryDateMap summary date map
     */
    private void saveSupplementaryData(BaseParam param, CPVIVF12Entity data, Timestamp systemTime, int number,
        Map<Integer, Date> summaryDateMap) {

        Integer invoiceSummaryId = data.getInvoiceSummaryId();
        Integer transportMode = StringUtil.toInteger(data.getTransportMode());
        Date etd = data.getEtd();
        Date eta = data.getEta();
        Date inboundDate = data.getInboundDate();
        if (eta != null && inboundDate != null) {
            summaryDateMap.put(invoiceSummaryId, eta);
        }

        String kanbanPlanNo = null;
        BigDecimal qty = null;
        if (number == IntDef.INT_ONE) {
            kanbanPlanNo = data.getKanbanPlanNo1();
            qty = data.getQty1();
        } else if (number == IntDef.INT_TWO) {
            kanbanPlanNo = data.getKanbanPlanNo2();
            qty = data.getQty2();
        } else if (number == IntDef.INT_THREE) {
            kanbanPlanNo = data.getKanbanPlanNo3();
            qty = data.getQty3();
        }
        if (StringUtil.isEmpty(kanbanPlanNo) || qty == null || DecimalUtil.isLessEquals(qty, BigDecimal.ZERO)) {
            return;
        }

        // Find vessel name from exist invoice
        String vesselName = null;
        TntInvoiceSummary summaryData = super.getOneById(TntInvoiceSummary.class, invoiceSummaryId);
        if (summaryData != null) {
            vesselName = summaryData.getVesselName();
        }

        // TNT_INVOICE_GROUP
        TntInvoiceGroup groupData = null;
        TntInvoiceGroup groupCondition = new TntInvoiceGroup();
        groupCondition.setKanbanPlanNo(kanbanPlanNo);
        groupCondition.setTransportMode(transportMode);
        groupCondition.setEtd(etd);
        groupCondition.setEta(eta);
        groupCondition.setImpInbPlanDate(inboundDate);
        groupCondition.setStatus(InvoiceGroupStatus.NORMAL);
        if (!StringUtil.isEmpty(vesselName)) {
            groupCondition.setVesselName(vesselName);
        }
        List<TntInvoiceGroup> groupDatas = super.baseDao.select(groupCondition);
        if (groupDatas != null && groupDatas.size() > 0) {
            groupData = groupDatas.get(0);
        } else {
            groupData = new TntInvoiceGroup();
            groupData.setKanbanPlanNo(kanbanPlanNo);
            groupData.setTransportMode(transportMode);
            groupData.setVesselName(vesselName);
            groupData.setEtd(etd);
            groupData.setEta(eta);
            groupData.setImpInbPlanDate(inboundDate);
            groupData.setStatus(InvoiceGroupStatus.NORMAL);
            groupData.setCreatedBy(param.getLoginUserId());
            groupData.setCreatedDate(systemTime);
            groupData.setUpdatedBy(param.getLoginUserId());
            groupData.setUpdatedDate(systemTime);
            groupData.setVersion(1);
            super.baseDao.insert(groupData);
        }

        // TNT_INVOICE
        TntInvoice invoiceData = null;
        TntInvoice invoiceCondition = new TntInvoice();
        invoiceCondition.setInvoiceGroupId(groupData.getInvoiceGroupId());
        invoiceCondition.setInvoiceSummaryId(invoiceSummaryId);
        List<TntInvoice> invoiceDatas = super.baseDao.select(invoiceCondition);
        if (invoiceDatas != null && invoiceDatas.size() > 0) {
            invoiceData = invoiceDatas.get(0);
        } else {
            invoiceData = new TntInvoice();
            invoiceData.setInvoiceGroupId(groupData.getInvoiceGroupId());
            invoiceData.setInvoiceSummaryId(invoiceSummaryId);
            invoiceData.setOfficeId(param.getCurrentOfficeId());
            invoiceData.setInvoiceNo(data.getInvoiceNo());
            invoiceData.setOriginalVersion(null);
            invoiceData.setRevisionVersion(1);
            invoiceData.setRevisionReason(null);
            invoiceData.setVanningDate(null);
            invoiceData.setEtd(etd);
            invoiceData.setEta(eta);
            invoiceData.setCcDate(null);
            invoiceData.setImpInbPlanDate(inboundDate);
            invoiceData.setImpCcActualDate(null);
            invoiceData.setImpInbActualDate(null);
            invoiceData.setCreatedBy(param.getLoginUserId());
            invoiceData.setCreatedDate(systemTime);
            invoiceData.setUpdatedBy(param.getLoginUserId());
            invoiceData.setUpdatedDate(systemTime);
            invoiceData.setVersion(1);
            super.baseDao.insert(invoiceData);
        }

        // TNT_INVOICE_PARTS
        TntInvoicePart partCondition = new TntInvoicePart();
        partCondition.setInvoiceId(invoiceData.getInvoiceId());
        partCondition.setPartsId(data.getPartsId());
        List<TntInvoicePart> partDatas = super.baseDao.select(partCondition);
        if (partDatas != null && partDatas.size() > 0) {
            TntInvoicePart partData = partDatas.get(0);
            partData.setOriginalQty(DecimalUtil.add(partData.getOriginalQty(), qty));
            partData.setQty(DecimalUtil.add(partData.getQty(), qty));
            partData.setUpdatedBy(param.getLoginUserId());
            partData.setUpdatedDate(systemTime);
            partData.setVersion(partData.getVersion() + 1);
            super.baseDao.update(invoiceData);
        } else {
            TntInvoicePart partData = new TntInvoicePart();
            partData.setInvoiceId(invoiceData.getInvoiceId());
            partData.setPartsId(data.getPartsId());
            partData.setSupplierId(data.getSupplierId());
            partData.setSupplierPartsNo(null);
            partData.setImpPoNo(null);
            partData.setCustomerOrderNo(null);
            partData.setExpPoNo(null);
            partData.setOriginalQty(qty);
            partData.setQty(qty);
            partData.setCreatedBy(param.getLoginUserId());
            partData.setCreatedDate(systemTime);
            partData.setUpdatedBy(param.getLoginUserId());
            partData.setUpdatedDate(systemTime);
            partData.setVersion(1);
            super.baseDao.insert(partData);
        }
    }

}
