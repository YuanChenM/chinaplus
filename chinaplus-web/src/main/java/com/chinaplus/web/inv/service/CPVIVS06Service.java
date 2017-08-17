/**
 * CPVIVS06Service.java
 * 
 * @screen CPVIVS06
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.PostRiFlag;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.common.service.PostGrGiService;
import com.chinaplus.core.base.BaseService;

/**
 * Post GR/GI Service.
 */
@Service
public class CPVIVS06Service extends BaseService {

    /** Post GR/GI Service */
    @Autowired
    private PostGrGiService postGrGiService;

    /**
     * Check exclusive.
     * 
     * @param invoiceSummaryId invoice summary ID
     * @param version version
     * @return check result
     */
    public boolean checkExclusive(Integer invoiceSummaryId, Integer version) {

        TntInvoiceSummary summaryData = super.getOneById(TntInvoiceSummary.class, invoiceSummaryId);
        if (summaryData == null) {
            return true;
        }
        if (summaryData.getVersion() != version) {
            return true;
        }

        return false;
    }

    /**
     * Post GR/GI.
     * 
     * @param officeId office ID
     * @param loginUserId login user ID
     * @param invoiceSummaryId invoice summary ID
     * @param grDate GR date
     * @param giDate GI date
     */
    public void doPost(Integer officeId, Integer loginUserId, Integer invoiceSummaryId, Date grDate, Date giDate) {

        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();

        // Update TNT_INVOICE_SUMMARY
        TntInvoiceSummary summaryData = super.getOneById(TntInvoiceSummary.class, invoiceSummaryId);
        summaryData.setGrDate(grDate);
        summaryData.setGiDate(giDate);
        summaryData.setPostRiFlag(PostRiFlag.Y);
        summaryData.setUpdatedBy(loginUserId);
        summaryData.setUpdatedDate(systemTime);
        summaryData.setVersion(summaryData.getVersion() + 1);
        super.baseDao.update(summaryData);

        // Update TNT_INVOICE
        TntInvoice invoiceCondition = new TntInvoice();
        invoiceCondition.setInvoiceSummaryId(invoiceSummaryId);
        List<TntInvoice> invoiceDatas = super.baseDao.select(invoiceCondition);
        for (TntInvoice invoiceData : invoiceDatas) {
            invoiceData.setImpInbActualDate(grDate);
            invoiceData.setUpdatedBy(loginUserId);
            invoiceData.setUpdatedDate(systemTime);
            invoiceData.setVersion(invoiceData.getVersion() + 1);
            super.baseDao.update(invoiceData);
        }
        super.baseDao.flush();

        // Do stock adjustment
        postGrGiService.doGrGiPost(officeId, invoiceSummaryId);
    }

}
