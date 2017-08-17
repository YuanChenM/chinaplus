package com.chinaplus.batch.interfaces.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.chinaplus.common.entity.TnfImpStockByDay;

/**
 * The persistent class for the TNF_IMP_STOCK_BY_DAY database table.
 * 
 */
public class TnfImpStockByDayEx extends TnfImpStockByDay {

    /** serialVersionUID */
    private static final long serialVersionUID = -4247675060694194151L;

    /** dayInRackQty */
    private BigDecimal dayInRackQty;

    /** dayImpStockQty */
    private BigDecimal dayImpStockQty;

    /** dayWhsStockQty */
    private BigDecimal dayWhsStockQty;

    /** dayNgQty */
    private BigDecimal dayNgQty;

    /** dayOnholdQty */
    private BigDecimal dayOnholdQty;

    /** lastBatchTime */
    private Timestamp lastBatchTime;

    /** currBatchTime */
    private Timestamp currBatchTime;

    /** lastEndDate */
    private Date lastEndDate;

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
     * Get the dayInRackQty.
     *
     * @return dayInRackQty
     */
    public BigDecimal getDayInRackQty() {
        return this.dayInRackQty;
    }

    /**
     * Set the dayInRackQty.
     *
     * @param dayInRackQty dayInRackQty
     */
    public void setDayInRackQty(BigDecimal dayInRackQty) {
        this.dayInRackQty = dayInRackQty;

    }

    /**
     * Get the dayNgQty.
     *
     * @return dayNgQty
     */
    public BigDecimal getDayNgQty() {
        return this.dayNgQty;
    }

    /**
     * Set the dayNgQty.
     *
     * @param dayNgQty dayNgQty
     */
    public void setDayNgQty(BigDecimal dayNgQty) {
        this.dayNgQty = dayNgQty;

    }

    /**
     * Get the dayOnholdQty.
     *
     * @return dayOnholdQty
     */
    public BigDecimal getDayOnholdQty() {
        return this.dayOnholdQty;
    }

    /**
     * Set the dayOnholdQty.
     *
     * @param dayOnholdQty dayOnholdQty
     */
    public void setDayOnholdQty(BigDecimal dayOnholdQty) {
        this.dayOnholdQty = dayOnholdQty;

    }

    /**
     * Get the dayImpStockQty.
     *
     * @return dayImpStockQty
     */
    public BigDecimal getDayImpStockQty() {
        return this.dayImpStockQty;
    }

    /**
     * Set the dayImpStockQty.
     *
     * @param dayImpStockQty dayImpStockQty
     */
    public void setDayImpStockQty(BigDecimal dayImpStockQty) {
        this.dayImpStockQty = dayImpStockQty;

    }

    /**
     * Get the dayWhsStockQty.
     *
     * @return dayWhsStockQty
     */
    public BigDecimal getDayWhsStockQty() {
        return this.dayWhsStockQty;
    }

    /**
     * Set the dayWhsStockQty.
     *
     * @param dayWhsStockQty dayWhsStockQty
     */
    public void setDayWhsStockQty(BigDecimal dayWhsStockQty) {
        this.dayWhsStockQty = dayWhsStockQty;

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

}