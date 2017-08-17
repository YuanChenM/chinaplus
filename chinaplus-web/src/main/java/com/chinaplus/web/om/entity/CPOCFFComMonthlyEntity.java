/**
 * CPOCFFComMonthlyEntity.java
 * 
 * @screen CPOCFF03, CPOCFF04, CPOCFF05
 * @author li_feng
 */
package com.chinaplus.web.om.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.chinaplus.common.entity.TntCfcMaster;
import com.chinaplus.common.entity.TntCfcMonth;

/**
 * Upload Data Entity.
 */
public class CPOCFFComMonthlyEntity extends TntCfcMonth {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** receiveDate */
    private Date receiveDate;

    /** actualQty */
    private BigDecimal actualQty;

    /** receiveInfoLst */
    private List<TntCfcMaster> receiveInfoLst;

    /** dailyInfoLst */
    private List<CPOCFFComDailyEntity> dailyInfoLst;

    /** totalAdjQty */
    private BigDecimal totalAdjQty;

    /**
     * Get the adjustDate.
     *
     * @return adjustDate
     */
    public Date getReceiveDate() {
        return this.receiveDate;
    }

    /**
     * Set the adjustDate.
     *
     * @param adjustDate adjustDate
     */
    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    /**
     * Get the adjustDate.
     *
     * @return adjustDate
     */
    public BigDecimal getActualQty() {
        return this.actualQty;
    }

    /**
     * Set the adjustDate.
     *
     * @param adjustDate adjustDate
     */
    public void setActualQty(BigDecimal actualQty) {
        this.actualQty = actualQty;
    }

    /**
     * Get the adjustDate.
     *
     * @return adjustDate
     */
    public List<TntCfcMaster> getReceiveInfoLst() {
        return this.receiveInfoLst;
    }

    /**
     * Set the adjustDate.
     *
     * @param adjustDate adjustDate
     */
    public void setReceiveInfoLst(List<TntCfcMaster> receiveInfoLst) {
        this.receiveInfoLst = receiveInfoLst;
    }

    /**
     * Get the adjustDate.
     *
     * @return adjustDate
     */
    public List<CPOCFFComDailyEntity> getDailyInfoLst() {
        return this.dailyInfoLst;
    }

    /**
     * Set the adjustDate.
     *
     * @param adjustDate adjustDate
     */
    public void setDailyInfoLst(List<CPOCFFComDailyEntity> dailyInfoLst) {
        this.dailyInfoLst = dailyInfoLst;
    }

    /**
     * Get the totalAdjQty.
     *
     * @return totalAdjQty
     */
    public BigDecimal getTotalAdjQty() {
        return this.totalAdjQty;
    }

    /**
     * Set the totalAdjQty.
     *
     * @param totalAdjQty totalAdjQty
     */
    public void setTotalAdjQty(BigDecimal totalAdjQty) {
        this.totalAdjQty = totalAdjQty;
    }

}