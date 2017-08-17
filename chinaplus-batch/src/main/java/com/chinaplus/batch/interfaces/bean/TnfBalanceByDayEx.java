package com.chinaplus.batch.interfaces.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.chinaplus.common.entity.TnfBalanceByDay;

/**
 * The persistent class for the TNF_BALANCE_BY_DAY database table.
 * 
 */
public class TnfBalanceByDayEx extends TnfBalanceByDay {

    /** serialVersionUID */
    private static final long serialVersionUID = -4247675060694194151L;
    
    /** officeId */
    private Integer officeId;

    /** dayOrderQty */
    private BigDecimal dayOrderQty;

    /** dayReceivedQty */
    private BigDecimal dayReceivedQty;

    /** lastBatchTime */
    private Timestamp lastBatchTime;

    /** lastEndDate */
    private Date lastEndDate;

    /** currBatchTime */
    private Timestamp currBatchTime;

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
     * Get the officeId.
     *
     * @return officeId
     */
    public Integer getOfficeId() {
        return this.officeId;
    }

    /**
     * Set the officeId.
     *
     * @param officeId officeId
     */
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
        
    }

    /**
     * Get the dayReceivedQty.
     *
     * @return dayReceivedQty
     */
    public BigDecimal getDayReceivedQty() {
        return this.dayReceivedQty;
    }

    /**
     * Set the dayReceivedQty.
     *
     * @param dayReceivedQty dayReceivedQty
     */
    public void setDayReceivedQty(BigDecimal dayReceivedQty) {
        this.dayReceivedQty = dayReceivedQty;
        
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