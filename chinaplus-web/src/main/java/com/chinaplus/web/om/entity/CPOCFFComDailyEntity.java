/**
 * CPOCFFComDailyEntity.java
 * 
 * @screen CPOCFF03, CPOCFF04, CPOCFF05
 * @author li_feng
 */
package com.chinaplus.web.om.entity;

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

}