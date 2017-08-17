/**
 * DBTEMPEntity.java
 * 
 * @screen CPOCSF11
 * @author li_feng
 */
package com.chinaplus.web.om.entity;

import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * DB TEMP Entity.
 */
public class DBTEMPEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 6370569810059055423L;

    /** PARTS_ID */
    private Integer PARTSID;

    /** CUSTOMER_ID */
    private Integer CUSTOMERID;

    /** CFC_NO */
    private String cFCNO;

    /** CFC_ID */
    private Integer cFCID;

    /** fcDate */
    private Date fcDate;
    /** updatedBy. */
    private Integer updatedBy;

    /** ALLOCATION_FC_TYPE */
    private String allocationFcType;
    
    /** cfcMonth */
    private String cfcMonth;
    
    /** FIRSTDAYOFMONTH */
    private Date firstDayOfMonth;
    
    /** LASTDAYOFMONTH */
    private Date lastDayOfMonth;

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
     * Get the pARTSID.
     *
     * @return pARTSID
     */
    public Integer getPARTSID() {
        return this.PARTSID;
    }

    /**
     * Set the pARTSID.
     *
     * @param pARTSID pARTSID
     */
    public void setPARTSID(Integer pARTSID) {
        PARTSID = pARTSID;
    }

    /**
     * Get the cUSTOMERID.
     *
     * @return cUSTOMERID
     */
    public Integer getCUSTOMERID() {
        return this.CUSTOMERID;
    }

    /**
     * Set the cUSTOMERID.
     *
     * @param cUSTOMERID cUSTOMERID
     */
    public void setCUSTOMERID(Integer cUSTOMERID) {
        CUSTOMERID = cUSTOMERID;
    }

    /**
     * Get the cFCNO.
     *
     * @return cFCNO
     */
    public String getcFCNO() {
        return this.cFCNO;
    }

    /**
     * Set the cFCNO.
     *
     * @param cFCNO cFCNO
     */
    public void setcFCNO(String cFCNO) {
        this.cFCNO = cFCNO;
    }

    /**
     * Get the cFCID.
     *
     * @return cFCID
     */
    public Integer getcFCID() {
        return this.cFCID;
    }

    /**
     * Set the cFCID.
     *
     * @param cFCID cFCID
     */
    public void setcFCID(Integer cFCID) {
        this.cFCID = cFCID;
    }

    /**
     * Get the updatedBy.
     *
     * @return updatedBy
     */
    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Set the updatedBy.
     *
     * @param updatedBy updatedBy
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the allocationFcType.
     *
     * @return allocationFcType
     */
    public String getAllocationFcType() {
        return this.allocationFcType;
    }

    /**
     * Set the allocationFcType.
     *
     * @param allocationFcType allocationFcType
     */
    public void setAllocationFcType(String allocationFcType) {
        this.allocationFcType = allocationFcType;
    }

    /**
     * Get the firstDayOfMonth.
     *
     * @return firstDayOfMonth
     */
    public Date getFirstDayOfMonth() {
        return this.firstDayOfMonth;
    }

    /**
     * Set the firstDayOfMonth.
     *
     * @param firstDayOfMonth firstDayOfMonth
     */
    public void setFirstDayOfMonth(Date firstDayOfMonth) {
        this.firstDayOfMonth = firstDayOfMonth;
    }

    /**
     * Get the lastDayOfMonth.
     *
     * @return lastDayOfMonth
     */
    public Date getLastDayOfMonth() {
        return this.lastDayOfMonth;
    }

    /**
     * Set the lastDayOfMonth.
     *
     * @param lastDayOfMonth lastDayOfMonth
     */
    public void setLastDayOfMonth(Date lastDayOfMonth) {
        this.lastDayOfMonth = lastDayOfMonth;
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

}