/**
 * CPOCFFTEMPEntity.java
 * 
 * @screen CPOCFF05
 * @author li_feng
 */
package com.chinaplus.common.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.chinaplus.common.entity.TnmPartsMaster;
import com.chinaplus.common.entity.TntCfcMaster;

/**
 * Customer Stock DownLoad Screen Entity.
 */
public class CPOCFFTEMPEntity extends TnmPartsMaster {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** cfcMonthId */
    private Integer cfcMonthId;

    /** cfcMonthId */
    private Integer cfcId;

    /** cfcMonth */
    private String cfcMonth;

    /** cfcQty */
    private BigDecimal cfcQty;

    /** fcDate */
    private Date fcDate;

    /** firstFcMonth */
    private String firstFcMonth;

    /** lastFcMonth */
    private String lastFcMonth;

    /** cfcNo */
    private String cfcNo;

    /** totalAdjDays */
    private int totalAdjDays;
    
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
    
    /** startDate */
    private Date startDate;
    
    /** endDate */
    private Date endDate;
    
    /** officeTimeNow */
    private String timeZone;
    

    /**
     * Get the cfcMonthId.
     *
     * @return cfcMonthId
     */
    public Integer getCfcMonthId() {
        return this.cfcMonthId;
    }

    /**
     * Set the cfcMonthId.
     *
     * @param cfcMonthId cfcMonthId
     */
    public void setCfcMonthId(Integer cfcMonthId) {
        this.cfcMonthId = cfcMonthId;
    }

    /**
     * Get the cfcId.
     *
     * @return cfcId
     */
    public Integer getCfcId() {
        return this.cfcId;
    }

    /**
     * Set the cfcId.
     *
     * @param cfcId cfcId
     */
    public void setCfcId(Integer cfcId) {
        this.cfcId = cfcId;
    }

    /**
     * Get the cfcMonth.
     *
     * @return cfcMonth
     */
    public String getCfcMonth() {
        return this.cfcMonth;
    }

    /**
     * Set the cfcMonth.
     *
     * @param cfcMonth cfcMonth
     */
    public void setCfcMonth(String cfcMonth) {
        this.cfcMonth = cfcMonth;
    }

    /**
     * Get the cfcQty.
     *
     * @return cfcQty
     */
    public BigDecimal getCfcQty() {
        return this.cfcQty;
    }

    /**
     * Set the cfcQty.
     *
     * @param cfcQty cfcQty
     */
    public void setCfcQty(BigDecimal cfcQty) {
        this.cfcQty = cfcQty;
    }

    /**
     * Get the fcDate.
     *
     * @return fcDate
     */
    public Date getFcDate() {
        return this.fcDate;
    }

    /**
     * Set the fcDate.
     *
     * @param fcDate fcDate
     */
    public void setFcDate(Date fcDate) {
        this.fcDate = fcDate;
    }

    /**
     * Get the firstFcMonth.
     *
     * @return firstFcMonth
     */
    public String getFirstFcMonth() {
        return this.firstFcMonth;
    }

    /**
     * Set the firstFcMonth.
     *
     * @param firstFcMonth firstFcMonth
     */
    public void setFirstFcMonth(String firstFcMonth) {
        this.firstFcMonth = firstFcMonth;
    }

    /**
     * Get the lastFcMonth.
     *
     * @return lastFcMonth
     */
    public String getLastFcMonth() {
        return this.lastFcMonth;
    }

    /**
     * Set the lastFcMonth.
     *
     * @param lastFcMonth lastFcMonth
     */
    public void setLastFcMonth(String lastFcMonth) {
        this.lastFcMonth = lastFcMonth;
    }

    /**
     * Get the cfcNo.
     *
     * @return cfcNo
     */
    public String getCfcNo() {
        return this.cfcNo;
    }

    /**
     * Set the cfcNo.
     *
     * @param cfcNo cfcNo
     */
    public void setCfcNo(String cfcNo) {
        this.cfcNo = cfcNo;
    }

    /**
     * Get the totalAdjDays.
     *
     * @return totalAdjDays
     */
    public int getTotalAdjDays() {
        return this.totalAdjDays;
    }

    /**
     * Set the totalAdjDays.
     *
     * @param totalAdjDays totalAdjDays
     */
    public void setTotalAdjDays(int totalAdjDays) {
        this.totalAdjDays = totalAdjDays;
    }

    /**
     * Get the receiveDate.
     *
     * @return receiveDate
     */
    public Date getReceiveDate() {
        return this.receiveDate;
    }

    /**
     * Set the receiveDate.
     *
     * @param receiveDate receiveDate
     */
    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
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
     * Get the receiveInfoLst.
     *
     * @return receiveInfoLst
     */
    public List<TntCfcMaster> getReceiveInfoLst() {
        return this.receiveInfoLst;
    }

    /**
     * Set the receiveInfoLst.
     *
     * @param receiveInfoLst receiveInfoLst
     */
    public void setReceiveInfoLst(List<TntCfcMaster> receiveInfoLst) {
        this.receiveInfoLst = receiveInfoLst;
    }

    /**
     * Get the dailyInfoLst.
     *
     * @return dailyInfoLst
     */
    public List<CPOCFFComDailyEntity> getDailyInfoLst() {
        return this.dailyInfoLst;
    }

    /**
     * Set the dailyInfoLst.
     *
     * @param dailyInfoLst dailyInfoLst
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

    /**
     * Get the startDate.
     *
     * @return startDate
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * Set the startDate.
     *
     * @param startDate startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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

    /**
     * Get the timeZone.
     *
     * @return timeZone
     */
    public String getTimeZone() {
        return this.timeZone;
    }

    /**
     * Set the timeZone.
     *
     * @param timeZone timeZone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }



}