package com.chinaplus.batch.interfaces.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.chinaplus.common.entity.TnfExpPartsStatus;


/**
 * The persistent class for the TNF_PARTS_STATUS database table.
 * 
 */
public class TnfExpPartsStatusEx extends TnfExpPartsStatus  {

    /** serialVersionUID */
    private static final long serialVersionUID = -4247675060694194151L;
    
    /** order no. */
    private String orderNo;

    /** dayOrderQty */ 
    private BigDecimal dayOrderQty;

    /** dayExpInboundQty */ 
    private BigDecimal dayExpInboundQty;

    /** prePlanInboundQty */ 
    private BigDecimal preInboundQty;

    /** dayExpPlanInboundQty */ 
    private BigDecimal dayExpPlanInboundQty;

    /** prePlanInboundQty */ 
    private BigDecimal prePlanInboundQty;

    /** dayInvoiceQty */ 
    private BigDecimal dayInvoiceQty;

    /** dayImpReceivedQty */ 
    private BigDecimal dayDicReceivedQty;
    
    /** dayImpReceivedQty */ 
    private BigDecimal preDicReceivedQty;
    
    /** preInvoiceQty */ 
    private BigDecimal preInvoiceQty;

    /** dayImpReceivedQty */ 
    private BigDecimal dayImpReceivedQty;

    /** dayForeCompletedQty */ 
    private BigDecimal foreCompletedQty;

    /** lastBatchTime */ 
    private Timestamp lastBatchTime;

    /** currBatchTime */ 
    private Timestamp currBatchTime;
    
    /** lastEndDate */
    private Date lastEndDate;

    /**
     * Get the dayExpInboundQty.
     *
     * @return dayExpInboundQty
     */
    public BigDecimal getDayExpInboundQty() {
        return this.dayExpInboundQty;
    }

    /**
     * Set the dayExpInboundQty.
     *
     * @param dayExpInboundQty dayExpInboundQty
     */
    public void setDayExpInboundQty(BigDecimal dayExpInboundQty) {
        this.dayExpInboundQty = dayExpInboundQty;
        
    }

    /**
     * Get the dayExpPlanInboundQty.
     *
     * @return dayExpPlanInboundQty
     */
    public BigDecimal getDayExpPlanInboundQty() {
        return this.dayExpPlanInboundQty;
    }

    /**
     * Set the dayExpPlanInboundQty.
     *
     * @param dayExpPlanInboundQty dayExpPlanInboundQty
     */
    public void setDayExpPlanInboundQty(BigDecimal dayExpPlanInboundQty) {
        this.dayExpPlanInboundQty = dayExpPlanInboundQty;
        
    }

    /**
     * Get the dayInvoiceQty.
     *
     * @return dayInvoiceQty
     */
    public BigDecimal getDayInvoiceQty() {
        return this.dayInvoiceQty;
    }

    /**
     * Set the dayInvoiceQty.
     *
     * @param dayInvoiceQty dayInvoiceQty
     */
    public void setDayInvoiceQty(BigDecimal dayInvoiceQty) {
        this.dayInvoiceQty = dayInvoiceQty;
        
    }

    /**
     * Get the currBatchTime.
     *
     * @return currBatchTime
     */
    public Timestamp getCurrBatchTime() {
        return this.currBatchTime;
    }

    /**
     * Set the currBatchTime.
     *
     * @param currBatchTime currBatchTime
     */
    public void setCurrBatchTime(Timestamp currBatchTime) {
        this.currBatchTime = currBatchTime;
        
    }

    /**
     * Get the lastBatchTime.
     *
     * @return lastBatchTime
     */
    public Timestamp getLastBatchTime() {
        return this.lastBatchTime;
    }

    /**
     * Set the lastBatchTime.
     *
     * @param lastBatchTime lastBatchTime
     */
    public void setLastBatchTime(Timestamp lastBatchTime) {
        this.lastBatchTime = lastBatchTime;
        
    }

    /**
     * Get the dayDicReceivedQty.
     *
     * @return dayDicReceivedQty
     */
    public BigDecimal getDayDicReceivedQty() {
        return this.dayDicReceivedQty;
    }

    /**
     * Set the dayImpReceivedQty.
     *
     * @param dayDicReceivedQty dayDicReceivedQty
     */
    public void setDayDicReceivedQty(BigDecimal dayDicReceivedQty) {
        this.dayDicReceivedQty = dayDicReceivedQty;
        
    }

    /**
     * Get the orderNo.
     *
     * @return orderNo
     */
    public String getOrderNo() {
        return this.orderNo;
    }

    /**
     * Set the orderNo.
     *
     * @param orderNo orderNo
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        
    }

    /**
     * Get the dayImpReceivedQty.
     *
     * @return dayImpReceivedQty
     */
    public BigDecimal getDayImpReceivedQty() {
        return this.dayImpReceivedQty;
    }

    /**
     * Set the dayImpReceivedQty.
     *
     * @param dayImpReceivedQty dayImpReceivedQty
     */
    public void setDayImpReceivedQty(BigDecimal dayImpReceivedQty) {
        this.dayImpReceivedQty = dayImpReceivedQty;
        
    }

    /**
     * Get the foreCompletedQty.
     *
     * @return foreCompletedQty
     */
    public BigDecimal getForeCompletedQty() {
        return this.foreCompletedQty;
    }

    /**
     * Set the foreCompletedQty.
     *
     * @param foreCompletedQty foreCompletedQty
     */
    public void setForeCompletedQty(BigDecimal foreCompletedQty) {
        this.foreCompletedQty = foreCompletedQty;
        
    }

    /**
     * Get the dayOrderQty.
     *
     * @return dayOrderQty
     */
    public BigDecimal getDayOrderQty() {
        return this.dayOrderQty;
    }

    /**
     * Set the dayOrderQty.
     *
     * @param dayOrderQty dayOrderQty
     */
    public void setDayOrderQty(BigDecimal dayOrderQty) {
        this.dayOrderQty = dayOrderQty;
        
    }

    /**
     * Get the preInboundQty.
     *
     * @return preInboundQty
     */
    public BigDecimal getPreInboundQty() {
        return this.preInboundQty;
    }

    /**
     * Set the preInboundQty.
     *
     * @param preInboundQty preInboundQty
     */
    public void setPreInboundQty(BigDecimal preInboundQty) {
        this.preInboundQty = preInboundQty;
        
    }

    /**
     * Get the prePlanInboundQty.
     *
     * @return prePlanInboundQty
     */
    public BigDecimal getPrePlanInboundQty() {
        return this.prePlanInboundQty;
    }

    /**
     * Set the prePlanInboundQty.
     *
     * @param prePlanInboundQty prePlanInboundQty
     */
    public void setPrePlanInboundQty(BigDecimal prePlanInboundQty) {
        this.prePlanInboundQty = prePlanInboundQty;
        
    }

    /**
     * Get the preDicReceivedQty.
     *
     * @return preDicReceivedQty
     */
    public BigDecimal getPreDicReceivedQty() {
        return this.preDicReceivedQty;
    }

    /**
     * Set the preDicReceivedQty.
     *
     * @param preDicReceivedQty preDicReceivedQty
     */
    public void setPreDicReceivedQty(BigDecimal preDicReceivedQty) {
        this.preDicReceivedQty = preDicReceivedQty;
        
    }

    /**
     * Get the lastEndDate.
     *
     * @return lastEndDate
     */
    public Date getLastEndDate() {
        return this.lastEndDate;
    }

    /**
     * Set the lastEndDate.
     *
     * @param lastEndDate lastEndDate
     */
    public void setLastEndDate(Date lastEndDate) {
        this.lastEndDate = lastEndDate;
        
    }

    /**
     * Get the preInvoiceQty.
     *
     * @return preInvoiceQty
     */
    public BigDecimal getPreInvoiceQty() {
        return this.preInvoiceQty;
    }

    /**
     * Set the preInvoiceQty.
     *
     * @param preInvoiceQty preInvoiceQty
     */
    public void setPreInvoiceQty(BigDecimal preInvoiceQty) {
        this.preInvoiceQty = preInvoiceQty;
        
    }

}