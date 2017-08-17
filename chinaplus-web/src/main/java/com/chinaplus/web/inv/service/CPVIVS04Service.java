/**
 * CPVIVS04Service.java
 * 
 * @screen CPVIVS01
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.InvoiceIrregularsStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceUploadStatus;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceGroup;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.web.inv.entity.CPVIVF11SupportEntity;
import com.chinaplus.web.inv.entity.CPVIVS04Entity;

/**
 * Irregular Shipping Schedule Service.
 */
@Service
public class CPVIVS04Service extends BaseService {

    /** SQL ID: find irregular shipping */
    private static final String SQLID_FIND_IRREGULAR_SHIPPING = "findIrregularShipping";

    /** SQL ID: find irregular parts */
    private static final String SQLID_FIND_IRREGULAR_PARTS = "findIrregularParts";

    /** SQL ID: find completed invoices */
    private static final String SQLID_FIND_COMPLETED_INVOICES = "findCompletedInvoices";

    /** Invoice Upload Service */
    @Autowired
    private CPVIVF11Service cpvivf11Service;

    /**
     * Query irregular shipping data.
     *
     * @param uploadId the upload ID
     * @return the query result
     */
    public List<CPVIVS04Entity> getIrregularShipping(String uploadId) {

        CPVIVS04Entity condition = new CPVIVS04Entity();
        condition.setUploadId(uploadId);
        return super.baseMapper.select(getSqlId(SQLID_FIND_IRREGULAR_SHIPPING), condition);
    }

    /**
     * Get irregular parts.
     * 
     * @param uploadId upload ID
     * @return irregular parts
     */
    public List<CPVIVS04Entity> getIrregularParts(String uploadId) {

        List<CPVIVS04Entity> result = new ArrayList<CPVIVS04Entity>();
        List<String> vesselList = new ArrayList<String>();
        CPVIVS04Entity condition = new CPVIVS04Entity();
        condition.setUploadId(uploadId);
        List<CPVIVS04Entity> irregularParts = super.baseMapper.select(getSqlId(SQLID_FIND_IRREGULAR_PARTS), condition);
        for (CPVIVS04Entity irregularPart : irregularParts) {
            String dataKey = irregularPart.getVesselName() + StringConst.UNDERLINE
                    + DateTimeUtil.getDisDate(irregularPart.getEtd());
            if (!vesselList.contains(dataKey)) {
                vesselList.add(dataKey);
                result.add(irregularPart);
            }
        }

        return result;
    }

    /**
     * Get completed invoices.
     * 
     * @param needAdjustList the invoice summary ID list
     * @return completed invoices
     */
    public List<CPVIVF11SupportEntity> getCompletedInvoices(List<Integer> needAdjustList) {

        CPVIVF11SupportEntity condition = new CPVIVF11SupportEntity();
        condition.setSummaryIdList(needAdjustList);
        return super.baseMapper.select(getSqlId(SQLID_FIND_COMPLETED_INVOICES), condition);
    }

    /**
     * Update irregular shipping.
     * 
     * @param loginUserId login user ID
     * @param uploadId the upload ID
     * @param dateMap date map
     */
    public void doIrregularShippingUpdate(Integer loginUserId, String uploadId, Map<String, Date[]> dateMap) {

        // Search TNT_INVOICE_SUMMARY
        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
        List<Integer> updatedGroupList = new ArrayList<Integer>();
        List<Integer> needAdjustList = new ArrayList<Integer>();
        TntInvoiceSummary summaryCondition = new TntInvoiceSummary();
        summaryCondition.setUploadId(uploadId);
        summaryCondition.setNonShippingRoute(InvoiceIrregularsStatus.DRAFT);
        List<TntInvoiceSummary> summaryDatas = super.baseDao.select(summaryCondition);
        for (TntInvoiceSummary summaryData : summaryDatas) {
            String mapKey = summaryData.getVesselName() + StringConst.UNDERLINE
                    + DateTimeUtil.getDisDate(summaryData.getEtd());
            Date[] dateArray = dateMap.get(mapKey);
            if (dateArray != null) {
                Date eta = dateArray[0];
                Date inboundPlan = dateArray[1];

                // Update TNT_INVOICE_SUMMARY
                summaryData.setEta(eta);
                summaryData.setNonShippingRoute(InvoiceIrregularsStatus.DONE);
                summaryData.setUpdatedBy(loginUserId);
                summaryData.setUpdatedDate(systemTime);
                summaryData.setVersion(summaryData.getVersion() + 1);
                super.baseDao.update(summaryData);
                if (InvoiceUploadStatus.DONE == summaryData.getUploadStatus()) {
                    needAdjustList.add(summaryData.getInvoiceSummaryId());
                }

                // Search TNT_INVOICE
                TntInvoice invoiceCondition = new TntInvoice();
                invoiceCondition.setInvoiceSummaryId(summaryData.getInvoiceSummaryId());
                List<TntInvoice> invoiceDatas = super.baseDao.select(invoiceCondition);
                for (TntInvoice invoiceData : invoiceDatas) {
                    // Update TNT_INVOICE
                    invoiceData.setEta(eta);
                    invoiceData.setImpInbPlanDate(inboundPlan);
                    invoiceData.setUpdatedBy(loginUserId);
                    invoiceData.setUpdatedDate(systemTime);
                    invoiceData.setVersion(invoiceData.getVersion() + 1);
                    super.baseDao.update(invoiceData);

                    // Search TNT_INVOICE_GROUP
                    Integer groupId = invoiceData.getInvoiceGroupId();
                    if (groupId != null && !updatedGroupList.contains(groupId)) {
                        updatedGroupList.add(groupId);
                        TntInvoiceGroup groupData = super.getOneById(TntInvoiceGroup.class, groupId);
                        // Update TNT_INVOICE_GROUP
                        groupData.setEta(eta);
                        groupData.setImpInbPlanDate(inboundPlan);
                        groupData.setUpdatedBy(loginUserId);
                        groupData.setUpdatedDate(systemTime);
                        groupData.setVersion(groupData.getVersion() + 1);
                        super.baseDao.update(groupData);
                    }
                }
            }
        }

        // Find completed invoice after upload
        if (needAdjustList.size() > 0) {
            super.baseDao.flush();
            List<CPVIVF11SupportEntity> invoiceList = getCompletedInvoices(needAdjustList);
            cpvivf11Service.doPlanAdjust(invoiceList, loginUserId, systemTime);
        }
    }

}
