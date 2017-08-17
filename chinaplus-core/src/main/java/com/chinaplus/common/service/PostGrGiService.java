/**
 * PostGrGiService.java
 * 
 * @screen CPVIVB01
 * @author gu_chengchen
 */
package com.chinaplus.common.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.consts.CodeConst.InvoiceStatus;
import com.chinaplus.common.consts.CodeConst.PostRiFlag;
import com.chinaplus.common.entity.PostGrGiEntity;
import com.chinaplus.common.entity.TnfImpStock;
import com.chinaplus.common.entity.TnfOrderStatus;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Post GR/GI Service.
 */
@Service
public class PostGrGiService extends BaseService {

    /** SQL ID: find need GR invoice */
    private static final String SQLID_FIND_NEED_GR_INVOICE = "findNeedGRInvoice";

    /** SQL ID: find need GI invoice */
    private static final String SQLID_FIND_NEED_GI_INVOICE = "findNeedGIInvoice";

    /** SQL ID: find post GR/GI stock */
    private static final String SQLID_FIND_POST_RI_STOCK = "findPostRIStock";

    /** Type: GR */
    private static final String TYPE_GR = "1";

    /** Type: GI */
    private static final String TYPE_GI = "2";

    /**
     * Execute post GR/GI.
     * 
     * @param officeId office ID
     * @param invoiceSummaryId invoice summary ID
     */
    public void doGrGiPost(Integer officeId, Integer invoiceSummaryId) {

        // Find all active office's time zone information
        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
        List<TnmOffice> offices = null;
        if (officeId != null) {
            offices = new ArrayList<TnmOffice>();
            TnmOffice curOffice = super.getOneById(TnmOffice.class, officeId);
            if (curOffice != null) {
                offices.add(curOffice);
            }
        } else {
            TnmOffice officeCondition = new TnmOffice();
            officeCondition.setInactiveFlag(InactiveFlag.ACTIVE);
            offices = super.baseDao.select(officeCondition);
        }
        for (TnmOffice office : offices) {
            PostGrGiEntity condition = new PostGrGiEntity();
            Timestamp officeTime = super.getDBDateTime(office.getTimeZone());
            condition.setConditionDate(officeTime);
            condition.setOfficeId(office.getOfficeId());
            if (invoiceSummaryId != null) {
                condition.setInvoiceSummaryId(invoiceSummaryId);
            }

            // Post GR
            PostGrOrGi(TYPE_GR, systemTime, condition);

            // Post GI
            PostGrOrGi(TYPE_GI, systemTime, condition);
        }
    }

    /**
     * Execute post GR/GI.
     * 
     * @param officeId office ID
     */
    public void doGrGiPost(Integer officeId) {

        doGrGiPost(officeId, null);
    }

    /**
     * Execute post GR/GI.
     */
    public void doGrGiPost() {

        doGrGiPost(null, null);
    }

    /**
     * Post GR/GI.
     * 
     * @param dataType the data type
     * @param systemTime system time
     * @param condition office condition
     */
    private void PostGrOrGi(String dataType, Timestamp systemTime, PostGrGiEntity condition) {

        // Find need Post GR invoice
        List<Integer> updatedInvoices = new ArrayList<Integer>();
        List<PostGrGiEntity> needProcessDatas = null;
        if (TYPE_GR.equals(dataType)) {
            needProcessDatas = super.baseMapper.select(getSqlId(SQLID_FIND_NEED_GR_INVOICE), condition);
        } else {
            needProcessDatas = super.baseMapper.select(getSqlId(SQLID_FIND_NEED_GI_INVOICE), condition);
        }
        for (PostGrGiEntity processData : needProcessDatas) {
            BigDecimal loopedQty = processData.getQty();
            // Find TNF_ORDER_STATUS
            TnfOrderStatus orderCondition = new TnfOrderStatus();
            orderCondition.setPartsId(processData.getPartsId());
            if (StringUtil.isEmpty(processData.getKanbanPlanNo())) {
                orderCondition.setImpPoNo(processData.getIpo());
                orderCondition.setCustomerOrderNo(processData.getCpo());
                orderCondition.setExpPoNo(processData.getEpo());
                orderCondition.setSupplierId(processData.getSupplierId());
            } else {
                orderCondition.setKanbanPlanNo(processData.getKanbanPlanNo());
            }
            List<TnfOrderStatus> orderDatas = super.baseDao.select(orderCondition);

            // Update TNF_ORDER_STATUS
            if (orderDatas.size() > 0) {
                TnfOrderStatus orderData = orderDatas.get(0);
                if (TYPE_GR.equals(dataType)) {
                    orderData.setExpOnshippingQty(DecimalUtil.subtract(orderData.getExpOnshippingQty(), loopedQty));
                    orderData.setImpStockQty(DecimalUtil.add(orderData.getImpStockQty(), loopedQty));
                } else {
                    orderData.setImpStockQty(DecimalUtil.subtract(orderData.getImpStockQty(), loopedQty));
                    orderData.setImpDeliveredQty(DecimalUtil.add(orderData.getImpDeliveredQty(), loopedQty));
                }
                orderData.setUpdatedBy(IntDef.INT_ZERO);
                orderData.setUpdatedDate(systemTime);
                orderData.setVersion(orderData.getVersion() + IntDef.INT_ONE);
                super.baseDao.update(orderData);
            }

            // Find TNT_IMP_STOCK
            PostGrGiEntity stockCondition = new PostGrGiEntity();
            stockCondition.setOfficeId(condition.getOfficeId());
            stockCondition.setPartsId(processData.getPartsId());
            stockCondition.setSupplierId(processData.getSupplierId());
            List<PostGrGiEntity> stockDatas = super.baseMapper.select(getSqlId(SQLID_FIND_POST_RI_STOCK),
                stockCondition);

            // Save TNT_IMP_STOCK
            if (stockDatas.size() > 0) {
                TnfImpStock stockData = super.getOneById(TnfImpStock.class, stockDatas.get(0).getStockId());
                if (stockData != null) {
                    if (TYPE_GR.equals(dataType)) {
                        stockData.setImpStockQty(DecimalUtil.add(stockData.getImpStockQty(), loopedQty));
                        stockData.setSystemStockQty(DecimalUtil.add(stockData.getSystemStockQty(), loopedQty));
                    } else {
                        stockData.setImpStockQty(DecimalUtil.subtract(stockData.getImpStockQty(), loopedQty));
                        stockData.setSystemStockQty(DecimalUtil.subtract(stockData.getSystemStockQty(), loopedQty));
                        stockData.setDeliveredQty(DecimalUtil.add(stockData.getDeliveredQty(), loopedQty));
                    }
                    stockData.setUpdatedBy(IntDef.INT_ZERO);
                    stockData.setUpdatedDate(systemTime);
                    stockData.setVersion(stockData.getVersion() + IntDef.INT_ONE);
                    super.baseDao.update(stockData);
                }
            } else {
                TnfImpStock stockData = new TnfImpStock();
                stockData.setOfficeId(condition.getOfficeId());
                stockData.setWhsId(null);
                stockData.setPartsId(processData.getPartsId());
                stockData.setBusinessPattern(processData.getBusinessPattern());
                stockData.setSupplierId(processData.getSupplierId());
                stockData.setImpInRackQty(BigDecimal.ZERO);
                if (TYPE_GR.equals(dataType)) {
                    stockData.setImpStockQty(loopedQty);
                    stockData.setSystemStockQty(loopedQty);
                    stockData.setDeliveredQty(BigDecimal.ZERO);
                } else {
                    stockData.setImpStockQty(BigDecimal.ZERO);
                    stockData.setSystemStockQty(BigDecimal.ZERO);
                    stockData.setDeliveredQty(loopedQty);
                }
                stockData.setNgQty(BigDecimal.ZERO);
                stockData.setOnholdQty(BigDecimal.ZERO);
                stockData.setCreatedBy(IntDef.INT_ZERO);
                stockData.setCreatedDate(systemTime);
                stockData.setUpdatedBy(IntDef.INT_ZERO);
                stockData.setUpdatedDate(systemTime);
                stockData.setVersion(IntDef.INT_ONE);
                super.baseDao.insert(stockData);
            }

            // Update TNT_INVOICE_SUMMARY
            if (!updatedInvoices.contains(processData.getInvoiceSummaryId())) {
                updatedInvoices.add(processData.getInvoiceSummaryId());
                TntInvoiceSummary summaryData = super.getOneById(TntInvoiceSummary.class,
                    processData.getInvoiceSummaryId());
                if (summaryData != null) {
                    if (TYPE_GR.equals(dataType)) {
                        summaryData.setPostRiFlag(PostRiFlag.AFTER_GR);
                        summaryData.setInboundQty(summaryData.getInvoiceQty());
                        summaryData.setInvoiceStatus(InvoiceStatus.INBOUND_COMPLETED);
                    } else {
                        summaryData.setPostRiFlag(PostRiFlag.AFTER_GI);
                    }
                    summaryData.setUpdatedBy(IntDef.INT_ZERO);
                    summaryData.setUpdatedDate(systemTime);
                    summaryData.setVersion(summaryData.getVersion() + IntDef.INT_ONE);
                    super.baseDao.update(summaryData);
                }
            }

            super.baseDao.flush();
        }
    }

}
