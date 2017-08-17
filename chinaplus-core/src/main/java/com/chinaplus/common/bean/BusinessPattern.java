/**
 * BusinessPattern.java
 * 
 * @screen common
 * @author zhang_chi
 */
package com.chinaplus.common.bean;

import com.chinaplus.core.base.BaseEntity;

/** 
 * BusinessPattern.
 */ 
public class BusinessPattern extends BaseEntity{

   
    /** serialVersionUID */
    private static final long serialVersionUID = -2756016644521227842L;
   
    private Integer officeId;
    private Integer customerId;
    private String  customerCode;
    private Integer businessPattern;
    private Integer allCustomerFlag;
    private Integer inactiveFlag;
    
    /**
     * Get the inactiveFlag.
     *
     * @return inactiveFlag
     */
    public Integer getInactiveFlag() {
        return this.inactiveFlag;
    }
    /**
     * Set the inactiveFlag.
     *
     * @param inactiveFlag inactiveFlag
     */
    public void setInactiveFlag(Integer inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
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
