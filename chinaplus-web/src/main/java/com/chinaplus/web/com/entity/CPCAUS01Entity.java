/**
 * CPCAUS01Entity.java
 * 
 * @screen CPCAUS01
 * @author shi_yuxi
 */
package com.chinaplus.web.com.entity;

import java.sql.Timestamp;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPCAUS01Entity.
 */
public class CPCAUS01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** loginId. */
    private String loginId;
    
    /** userId. */
    private Integer userId;
    
    /** customerId. */
    private Integer customerId;
    
    /** userCode. */
    private String userCode;
    
    /** userName. */
    private String userName;
    
    /** customerCode. */
    private String customerCode;
    
    /** customerName. */
    private String customerName;
    
    /** useFlag. */
    private String useFlag;
    
    /** activeFlag. */
    private String activeFlag;
    
    /** createdBy. */
    private Integer createdBy;
    
    /** createdDate. */
    private Timestamp createdDate;
    
    /** updatedBy. */
    private Integer updatedBy;
    
    /** updatedDate. */
    private Timestamp updatedDate;
    
    /** version. */
    private Integer version;
    
    /** officeCode. */
    private String officeCode;
    
    /** allCustomerFlag. */
    private Integer allCustomerFlag;
    
    /** businessPattern. */
    private Integer businessPattern;

    /**
     * Get the userCode.
     *
     * @return userCode
     */
    public String getUserCode() {
        return this.userCode;
    }

    /**
     * Set the userCode.
     *
     * @param userCode userCode
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    /**
     * Get the userName.
     *
     * @return userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Set the userName.
     *
     * @param userName userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
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
     * Get the createdBy.
     *
     * @return createdBy
     */
    public Integer getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Set the createdBy.
     *
     * @param createdBy createdBy
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the createdDate.
     *
     * @return createdDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    /**
     * Set the createdDate.
     *
     * @param createdDate createdDate
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
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
     * Get the updatedDate.
     *
     * @return updatedDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    /**
     * Set the updatedDate.
     *
     * @param updatedDate updatedDate
     */
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * Get the version.
     *
     * @return version
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * Set the version.
     *
     * @param version version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get the userId.
     *
     * @return userId
     */
    public Integer getUserId() {
        return this.userId;
    }

    /**
     * Set the userId.
     *
     * @param userId userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;

    }

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
     * Get the activeFlag.
     *
     * @return activeFlag
     */
    public String getActiveFlag() {
        return this.activeFlag;
    }

    /**
     * Set the activeFlag.
     *
     * @param activeFlag activeFlag
     */
    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;

    }

    /**
     * Get the useFlag.
     *
     * @return useFlag
     */
    public String getUseFlag() {
        return this.useFlag;
    }

    /**
     * Set the useFlag.
     *
     * @param useFlag useFlag
     */
    public void setUseFlag(String useFlag) {
        this.useFlag = useFlag;

    }

    /**
     * Get the loginId.
     *
     * @return loginId
     */
    public String getLoginId() {
        return this.loginId;
    }

    /**
     * Set the loginId.
     *
     * @param loginId loginId
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;

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
     * Get the allCustomerFlag.
     *
     * @return allCustomerFlag
     */
    public Integer getAllCustomerFlag() {
        return this.allCustomerFlag;
    }

    /**
     * Set the allCustomerFlag.
     *
     * @param allCustomerFlag allCustomerFlag
     */
    public void setAllCustomerFlag(Integer allCustomerFlag) {
        this.allCustomerFlag = allCustomerFlag;
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

}
