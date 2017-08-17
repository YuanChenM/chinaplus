/**
 * CPMCLS01Entity
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.entity;

import java.sql.Timestamp;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Customer List Screen entity
 */
public class CPMCLS01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** officeId */
    private String officeId;
    
    /** officeCode */
    private String officeCode;
    
    /** customerId */
    private String customerId;
    
    /** customerCode */
    private String customerCode;
    
    /** customerName */
    private String customerName;
    
    /** whsCustCode */
    private String whsCustCode;
    
    /** regionCode */
    private String regionCode;
    
    /** calendarId */
    private String calendarId;
    
    /** calendarCode */
    private String calendarCode;
    
    /** inActiveFlag */
    private String inActiveFlag;
    
    /** createBy */
    private String createBy;
    
    /** createDate */
    private Timestamp createDate;
    
    /** updateBy */
    private String updateBy;
    
    /** updateDate */
    private Timestamp updateDate;
    
    /** businessPattern */
    private Integer businessPattern;
    
    /** rownum */
    private String rownum;

    /** ttlCustomerCode */
    private String ttlCustomerCode;
    
    /**
     * Get the customerId.
     *
     * @return customerId
     */
    public String getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     *
     * @param customerId customerId
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Get the officeId.
     * 
     * @return officeId
     */
    public String getOfficeId() {
        return this.officeId;
    }

    /**
     * Set the officeId.
     * 
     * @param officeId officeId
     */
    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    /**
     * Get the officeCode.
     * 
     * @return officeCode
     */
    public String getOfficeCode() {
        return this.officeCode;
    }

    /**
     * Set the officeCode.
     * 
     * @param officeCode officeCode
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    /**
     * Get the customerCode.
     * 
     * @return customerCode
     */
    public String getCustomerCode() {
        return this.customerCode;
    }

    /**
     * Set the customerCode.
     * 
     * @param customerCode customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * Get the customerName.
     * 
     * @return customerName
     */
    public String getCustomerName() {
        return this.customerName;
    }

    /**
     * Set the customerName.
     * 
     * @param customerName customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Get the whsCustCode.
     * 
     * @return whsCustCode
     */
    public String getWhsCustCode() {
        return this.whsCustCode;
    }

    /**
     * Set the whsCustCode.
     * 
     * @param whsCustCode whsCustCode
     */
    public void setWhsCustCode(String whsCustCode) {
        this.whsCustCode = whsCustCode;
    }

    /**
     * Get the regionCode.
     * 
     * @return regionCode
     */
    public String getRegionCode() {
        return this.regionCode;
    }

    /**
     * Set the regionCode.
     * 
     * @param regionCode regionCode
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * Get the calendarId.
     * 
     * @return calendarId
     */
    public String getCalendarId() {
        return this.calendarId;
    }

    /**
     * Set the calendarId.
     * 
     * @param calendarId calendarId
     */
    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }


    /**
     * Get the createBy.
     * 
     * @return createBy
     */
    public String getCreateBy() {
        return this.createBy;
    }

    /**
     * Set the createBy.
     * 
     * @param createBy createBy
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * Get the createDate.
     * 
     * @return createDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    /**
     * Set the createDate.
     * 
     * @param createDate createDate
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /**
     * Get the updateBy.
     * 
     * @return updateBy
     */
    public String getUpdateBy() {
        return this.updateBy;
    }

    /**
     * Set the updateBy.
     * 
     * @param updateBy updateBy
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * Get the updateDate.
     * 
     * @return updateDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Timestamp getUpdateDate() {
        return this.updateDate;
    }

    /**
     * Set the updateDate.
     * 
     * @param updateDate updateDate
     */
    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * Get the calendarCode.
     * 
     * @return calendarCode
     */
    public String getCalendarCode() {
        return this.calendarCode;
    }

    /**
     * Set the calendarCode.
     * 
     * @param calendarCode calendarCode
     */
    public void setCalendarCode(String calendarCode) {
        this.calendarCode = calendarCode;
    }

    /**
     * Get the inActiveFlag.
     *
     * @return inActiveFlag
     */
    public String getInActiveFlag() {
        return this.inActiveFlag;
    }

    /**
     * Set the inActiveFlag.
     *
     * @param inActiveFlag inActiveFlag
     */
    public void setInActiveFlag(String inActiveFlag) {
        this.inActiveFlag = inActiveFlag;
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
     * Get the rownum.
     *
     * @return rownum
     */
    public String getRownum() {
        return this.rownum;
    }

    /**
     * Set the rownum.
     *
     * @param rownum rownum
     */
    public void setRownum(String rownum) {
        this.rownum = rownum;
    }

    /**
     * Get the ttlCustomerCode.
     *
     * @return ttlCustomerCode
     */
    public String getTtlCustomerCode() {
        return this.ttlCustomerCode;
    }

    /**
     * Set the ttlCustomerCode.
     *
     * @param ttlCustomerCode ttlCustomerCode
     */
    public void setTtlCustomerCode(String ttlCustomerCode) {
        this.ttlCustomerCode = ttlCustomerCode;
    }
}
