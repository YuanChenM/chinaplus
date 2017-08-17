/**
 * CPVIVF13Service.java
 * 
 * @screen CPVIVF13
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.InvoiceStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceType;
import com.chinaplus.common.consts.CodeConst.PostRiFlag;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoicePart;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inv.entity.CPVIVF13Entity;

/**
 * New Invoice Upload Service.
 */
@Service
public class CPVIVF13Service extends BaseService {

    /**
     * Query the part information.
     *
     * @param condition the query condition
     * @return the query result
     */
    public CPVIVF13Entity getPartInfo(CPVIVF13Entity condition) {

        List<CPVIVF13Entity> result = super.baseMapper.select(getSqlId(SQLID_FIND_ALL), condition);
        if (result == null || result.size() != 1) {
            return null;
        }

        return result.get(0);
    }

    /**
     * Check the Invoice No. is exist or not.
     * 
     * @param invoice the invoice no
     * @return check result
     */
    public boolean isInvoiceExist(String invoice) {

        ObjectParam<String> condition = new ObjectParam<String>();
        condition.setData(invoice);
        int dataCount = super.getDatasCount(condition);
        return dataCount > 0 ? true : false;
    }

    /**
     * Insert new invoice.
     * 
     * @param param common parameter
     * @param invoiceMap invoice data map
     * @param partList part qty list
     */
    public void doInvoiceInsert(BaseParam param, Map<Integer, TntInvoiceSummary> invoiceMap,
        List<CPVIVF13Entity> partList) {

        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
        Timestamp officeTime = super.getDBDateTime(param.getOfficeTimezone());
        boolean hasUploadData = false;
        for (Map.Entry<Integer, TntInvoiceSummary> entry : invoiceMap.entrySet()) {
            TntInvoiceSummary invoiceData = entry.getValue();
            if (invoiceData != null) {
                hasUploadData = true;

                // Insert into TNT_INVOICE_SUMMARY
                invoiceData.setOfficeId(param.getCurrentOfficeId());
                invoiceData.setBusinessPattern(BusinessPattern.V_V);
                invoiceData.setInvoiceType(InvoiceType.MANUAL);
                invoiceData.setInboundQty(BigDecimal.ZERO);
                invoiceData.setPostRiFlag(PostRiFlag.N);
                invoiceData.setInvoiceStatus(InvoiceStatus.NOT_APPROVED);
                invoiceData.setUploadId(StringUtil.genUploadId(param.getLoginId()));
                invoiceData.setUploadedBy(param.getLoginUserId());
                invoiceData.setUploadedDate(officeTime);
                invoiceData.setCreatedBy(param.getLoginUserId());
                invoiceData.setCreatedDate(systemTime);
                invoiceData.setUpdatedBy(param.getLoginUserId());
                invoiceData.setUpdatedDate(systemTime);
                invoiceData.setVersion(1);
                super.baseDao.insert(invoiceData);

                // Insert into TNT_INVOICE
                TntInvoice invoice = new TntInvoice();
                invoice.setInvoiceSummaryId(invoiceData.getInvoiceSummaryId());
                invoice.setOfficeId(param.getCurrentOfficeId());
                invoice.setInvoiceNo(invoiceData.getInvoiceNo());
                invoice.setRevisionVersion(1);
                invoice.setEtd(invoiceData.getEtd());
                invoice.setEta(invoiceData.getEta());
                invoice.setImpInbActualDate(invoiceData.getGrDate());
                invoice.setCreatedBy(param.getLoginUserId());
                invoice.setCreatedDate(systemTime);
                invoice.setUpdatedBy(param.getLoginUserId());
                invoice.setUpdatedDate(systemTime);
                invoice.setVersion(1);
                super.baseDao.insert(invoice);

                // Insert into TNT_INVOICE_PARTS
                for (CPVIVF13Entity partData : partList) {
                    Map<Integer, BigDecimal> invoiceQtyMap = partData.getInvoiceQtyMap();
                    BigDecimal invoiceQty = invoiceQtyMap.get(entry.getKey());
                    if (invoiceQty != null) {
                        TntInvoicePart part = new TntInvoicePart();
                        part.setInvoiceId(invoice.getInvoiceId());
                        part.setPartsId(partData.getPartsId());
                        part.setSupplierId(partData.getSupplierId());
                        part.setImpPoNo(partData.getImpOrderNo());
                        part.setCustomerOrderNo(partData.getCusOrderNo());
                        part.setExpPoNo(partData.getExpOrderNo());
                        part.setOriginalQty(invoiceQty);
                        part.setQty(invoiceQty);
                        part.setCreatedBy(param.getLoginUserId());
                        part.setCreatedDate(systemTime);
                        part.setUpdatedBy(param.getLoginUserId());
                        part.setUpdatedDate(systemTime);
                        part.setVersion(1);
                        super.baseDao.insert(part);
                    }
                }
            }
        }

        // Check whether has upload data
        if (!hasUploadData) {
            throw new BusinessException(MessageCodeConst.W1004_005);
        }
    }

}
