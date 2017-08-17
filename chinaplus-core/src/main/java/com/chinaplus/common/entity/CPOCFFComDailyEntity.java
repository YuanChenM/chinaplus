/**
 * CPOCFFComDailyEntity.java
 * 
 * @screen CPOCFF03, CPOCFF04, CPOCFF05
 * @author li_feng
 */
package com.chinaplus.common.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.common.entity.TntCfcDay;

/**
 * Upload Data Entity.
 */
public class CPOCFFComDailyEntity extends TntCfcDay {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** adjustDate */
    private Date adjustDate;

    /** adjustSeq */
    private Date adjustSeq;

    /** workingFlag */
    private Integer workingFlag;

    /** actualQty */
    private BigDecimal actualQty;

    /** adjustedQty */
    private BigDecimal adjustedQty;

    /** partsId */
    private Integer partsId;
    
    /** deliveredQty */
    private BigDecimal deliveredQty;
    
    /** endDate */
    private Date endDate;
    
    /**cfcMonthId */
    private Integer cfcMonthId;
    
    /**customerId */
    private Integer customerId;
    
    /** cfcDate */
    private Date cfcDate;
    
    /** cfcQty */
    private BigDecimal cfcQty;
    
    /** cfcMonth */
    private String cfcMonth;
    
    /** fcDate */
    private Date fcDate;
    
    /** cfcMonthQty */
    private BigDecimal cfcMonthQty;
    

    public Integer getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * Get the partsId.
     *
     * @return partsId
     */
    public Integer getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     */
    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }


    /**
     * Get the adjustDate.
     *
     * @return adjustDate
     */
    public Date getAdjustDate() {
        return this.adjustDate;
    }

    /**
     * Set the adjustDate.
     *
     * @param adjustDate adjustDate
     */
    public void setAdjustDate(Date adjustDate) {
        this.adjustDate = adjustDate;
    }

    /**
     * Get the adjustSeq.
     *
     * @return adjustSeq
     */
    public Date getAdjustSeq() {
        return this.adjustSeq;
    }

    /**
     * Set the adjustSeq.
     *
     * @param adjustSeq adjustSeq
     */
    public void setAdjustSeq(Date adjustSeq) {
        this.adjustSeq = adjustSeq;
    }

    /**
     * Get the workingFlag.
     *
     * @return workingFlag
     */
    public Integer getWorkingFlag() {
        return this.workingFlag;
    }

    /**
     * Set the workingFlag.
     *
     * @param workingFlag workingFlag
     */
    public void setWorkingFlag(Integer workingFlag) {
        this.workingFlag = workingFlag;
    }

    /**
     * Get the actualQty.
     *
     * @return actualQty
     */
    public BigDecimal getActualQty() {
        return this.actualQty;
    }

    /**
     * Set the actualQty.
     *
     * @param actualQty actualQty
     */
    public void setActualQty(BigDecimal actualQty) {
        this.actualQty = actualQty;
    }

    /**
     * Get the adjustedQty.
     *
     * @return adjustedQty
     */
    public BigDecimal getAdjustedQty() {
        return this.adjustedQty;
    }

    /**
     * Set the adjustedQty.
     *
     * @param adjustedQty adjustedQty
     */
    public void setAdjustedQty(BigDecimal adjustedQty) {
        this.adjustedQty = adjustedQty;
    }

    /**
     * Get the deliveredQty.
     *
     * @return deliveredQty
     */
    public BigDecimal getDeliveredQty() {
        return this.deliveredQty;
    }

    /**
     * Set the deliveredQty.
     *
     * @param deliveredQty deliveredQty
     */
    public void setDeliveredQty(BigDecimal deliveredQty) {
        this.deliveredQty = deliveredQty;
    }

    /**
     * Get the endDate.
     *
     * @return endDate
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * Set the endDate.
     *
     * @param endDate endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getCfcMonthId() {
        return this.cfcMonthId;
    }

    public void setCfcMonthId(Integer cfcMonthId) {
        this.cfcMonthId = cfcMonthId;
    }

    public Date getCfcDate() {
        return this.cfcDate;
    }

    public void setCfcDate(Date cfcDate) {
        this.cfcDate = cfcDate;
    }



    public BigDecimal getCfcQty() {
        return this.cfcQty;
    }

    public void setCfcQty(BigDecimal cfcQty) {
        this.cfcQty = cfcQty;
    }

    public String getCfcMonth() {
        return this.cfcMonth;
    }

    public void setCfcMonth(String cfcMonth) {
        this.cfcMonth = cfcMonth;
    }

    public Date getFcDate() {
        return this.fcDate;
    }

    public void setFcDate(Date fcDate) {
        this.fcDate = fcDate;
    }

    public BigDecimal getCfcMonthQty() {
        return this.cfcMonthQty;
    }

    public void setCfcMonthQty(BigDecimal cfcMonthQty) {
        this.cfcMonthQty = cfcMonthQty;
    }

}