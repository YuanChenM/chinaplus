/**
 * CalculateDrEntity.java
 * 
 * @screen COMMON
 * @author shi_yuxi
 */
package com.chinaplus.web.om.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Customer Stock DownLoad Screen Entity.
 */
public class CalculateDrEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** drQty */
    private BigDecimal drQty;

    /** CALENDAR_DATE */
    private Date calendarDate;

    /** WORKING_FLAG */
    private Integer workingFlag;

    /** part */
    private Integer part;

    /** partNo */
    private String partNo;

    /** partQty */
    private BigDecimal partQty;

    /** partBoxes */
    private BigDecimal partBoxes;

    /** srbq */
    private BigDecimal srbq;

    /** dailyBoxes */
    private BigDecimal dailyBoxes;

    /** remainder1 */
    private BigDecimal remainder1;

    /** selectedCnt */
    private int selectedCnt;

    /** calcRatio */
    private BigDecimal calcRatio;

    /**
     * Get the selectedCnt.
     *
     * @return selectedCnt
     */
    public int getSelectedCnt() {
        return this.selectedCnt;
    }

    /**
     * Set the selectedCnt.
     *
     * @param selectedCnt selectedCnt
     */
    public void setSelectedCnt(int selectedCnt) {
        this.selectedCnt = selectedCnt;
    }

    /**
     * Get the calcRatio.
     *
     * @return calcRatio
     */
    public BigDecimal getCalcRatio() {
        return this.calcRatio;
    }

    /**
     * Set the calcRatio.
     *
     * @param calcRatio calcRatio
     */
    public void setCalcRatio(BigDecimal calcRatio) {
        this.calcRatio = calcRatio;
    }

    /**
     * Get the partNo.
     *
     * @return partNo
     */
    public String getPartNo() {
        return this.partNo;
    }

    /**
     * Set the partNo.
     *
     * @param partNo partNo
     */
    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    /**
     * Get the dailyBoxes.
     *
     * @return dailyBoxes
     */
    public BigDecimal getDailyBoxes() {
        return this.dailyBoxes;
    }

    /**
     * Set the dailyBoxes.
     *
     * @param dailyBoxes dailyBoxes
     */
    public void setDailyBoxes(BigDecimal dailyBoxes) {
        this.dailyBoxes = dailyBoxes;
    }

    /**
     * Get the remainder1.
     *
     * @return remainder1
     */
    public BigDecimal getRemainder1() {
        return this.remainder1;
    }

    /**
     * Set the remainder1.
     *
     * @param remainder1 remainder1
     */
    public void setRemainder1(BigDecimal remainder1) {
        this.remainder1 = remainder1;
    }

    /**
     * Get the partBoxes.
     *
     * @return partBoxes
     */
    public BigDecimal getPartBoxes() {
        return this.partBoxes;
    }

    /**
     * Set the partBoxes.
     *
     * @param partBoxes partBoxes
     */
    public void setPartBoxes(BigDecimal partBoxes) {
        this.partBoxes = partBoxes;
    }

    /**
     * Get the srbq.
     *
     * @return srbq
     */
    public BigDecimal getSrbq() {
        return this.srbq;
    }

    /**
     * Set the srbq.
     *
     * @param srbq srbq
     */
    public void setSrbq(BigDecimal srbq) {
        this.srbq = srbq;
    }

    /**
     * Get the part.
     *
     * @return part
     */
    public Integer getPart() {
        return this.part;
    }

    /**
     * Set the part.
     *
     * @param part part
     */
    public void setPart(Integer part) {
        this.part = part;
    }

    /**
     * Get the partQty.
     *
     * @return partQty
     */
    public BigDecimal getPartQty() {
        return this.partQty;
    }

    /**
     * Set the partQty.
     *
     * @param partQty partQty
     */
    public void setPartQty(BigDecimal partQty) {
        this.partQty = partQty;
    }

    /**
     * Get the drQty.
     *
     * @return drQty
     */
    public BigDecimal getDrQty() {
        return this.drQty;
    }

    /**
     * Set the drQty.
     *
     * @param drQty drQty
     */
    public void setDrQty(BigDecimal drQty) {
        this.drQty = drQty;
    }

    /**
     * Get the calendarDate.
     *
     * @return calendarDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Date getCalendarDate() {
        return this.calendarDate;
    }

    /**
     * Set the calendarDate.
     *
     * @param calendarDate calendarDate
     */
    public void setCalendarDate(Date calendarDate) {
        this.calendarDate = calendarDate;
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

}