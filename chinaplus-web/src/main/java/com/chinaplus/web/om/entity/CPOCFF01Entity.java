/**
 * CPOCFF01Entity.java
 * 
 * @screen CPOCFF01
 * @author li_feng
 */
package com.chinaplus.web.om.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Customer Stock DownLoad Screen Entity.
 */
public class CPOCFF01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = -5657985529727844948L;

    /** CUSTOMER_ID */
    private Integer customerId;
    
    /** CUSTOMER_CODE */
    private String customercode;

    /** LAST_FC_MONTH */
    private String lastFcMonth;
    
    /** Data */
    private String data;

    /** BUSINESS_PATTERN */
    private Integer businessPattern;
    
    /** CALENDAR_DATE */
    private Date calendarDate;
    
    /** WORKING_FLAG */
    private Integer workingFlag;
    
    /** CALENDAR_ID */
    private Integer calendarId;
    
    /**
     * Get the customerId.
     *
     * @return customerId
     */
    public Integer getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     *
     * @param customerId customerId
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
     * Get the customercode.
     *
     * @return customercode
     */
    public String getCustomercode() {
        return this.customercode;
    }

    /**
     * Set the customercode.
     *
     * @param customercode customercode
     */
    public void setCustomercode(String customercode) {
        this.customercode = customercode;
    }

    /**
     * Get the data.
     *
     * @return data
     */
    public String getData() {
        return this.data;
    }

    /**
     * Set the data.
     *
     * @param data data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Get the businessPattern.
     *
     * @return businessPattern
     */
    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     */
    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
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

    /**
     * Get the calendarId.
     *
     * @return calendarId
     */
    public Integer getCalendarId() {
        return this.calendarId;
    }

    /**
     * Set the calendarId.
     *
     * @param calendarId calendarId
     */
    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

}