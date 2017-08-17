/**
 * CPSSMF11PartEntity.java
 * 
 * @screen CPSSMF11
 * @author gu_chengchen
 */
package com.chinaplus.web.sa.entity;

import java.math.BigDecimal;
import java.util.List;

import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Shipping Status Upload Part Entity.
 */
public class CPSSMF11PartEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Office ID */
    private Integer officeId;

    /** Order ID */
    private Integer orderId;

    /** Order Status ID */
    private Integer orderStatusId;

    /** Parts ID */
    private Integer partsId;

    /** Force Completed Flag */
    private boolean isForceCompleted;

    /** IPO No. */
    private String impPoNo;

    /** Customer Order No. */
    private String customerOrderNo;

    /** EPO No. */
    private String expPoNo;

    /** Customer ID */
    private Integer customerId;

    /** TTC Customer Code */
    private String ttcCustCode;

    /** TTC Parts No. */
    private String ttcPartsNo;

    /** Customer Parts No. */
    private String custPartsNo;

    /** Supplier ID */
    private Integer supplierId;

    /** TTC Supplier Code */
    private String ttcSuppCode;

    /** UOM Code */
    private String uomCode;

    /** Order QTY */
    private BigDecimal orderQty;

    /** Force Completed QTY */
    private BigDecimal forceCompletedQty;

    /** Current Customers */
    private List<Integer> currentCustomers;

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
     * Get the orderId.
     *
     * @return orderId
     */
    public Integer getOrderId() {
        return this.orderId;
    }

    /**
     * Set the orderId.
     *
     * @param orderId orderId
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * Get the orderStatusId.
     *
     * @return orderStatusId
     */
    public Integer getOrderStatusId() {
        return this.orderStatusId;
    }

    /**
     * Set the orderStatusId.
     *
     * @param orderStatusId orderStatusId
     */
    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
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
     * Get the isForceCompleted.
     *
     * @return isForceCompleted
     */
    public boolean isForceCompleted() {
        return this.isForceCompleted;
    }

    /**
     * Set the isForceCompleted.
     *
     * @param isForceCompleted isForceCompleted
     */
    public void setForceCompleted(boolean isForceCompleted) {
        this.isForceCompleted = isForceCompleted;
    }

    /**
     * Get the impPoNo.
     *
     * @return impPoNo
     */
    public String getImpPoNo() {
        return this.impPoNo;
    }

    /**
     * Set the impPoNo.
     *
     * @param impPoNo impPoNo
     */
    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
    }

    /**
     * Get the customerOrderNo.
     *
     * @return customerOrderNo
     */
    public String getCustomerOrderNo() {
        return this.customerOrderNo;
    }

    /**
     * Set the customerOrderNo.
     *
     * @param customerOrderNo customerOrderNo
     */
    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    /**
     * Get the expPoNo.
     *
     * @return expPoNo
     */
    public String getExpPoNo() {
        return this.expPoNo;
    }

    /**
     * Set the expPoNo.
     *
     * @param expPoNo expPoNo
     */
    public void setExpPoNo(String expPoNo) {
        this.expPoNo = expPoNo;
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
     * Get the ttcCustCode.
     *
     * @return ttcCustCode
     */
    public String getTtcCustCode() {
        return this.ttcCustCode;
    }

    /**
     * Set the ttcCustCode.
     *
     * @param ttcCustCode ttcCustCode
     */
    public void setTtcCustCode(String ttcCustCode) {
        this.ttcCustCode = ttcCustCode;
    }

    /**
     * Get the ttcPartsNo.
     *
     * @return ttcPartsNo
     */
    public String getTtcPartsNo() {
        return this.ttcPartsNo;
    }

    /**
     * Set the ttcPartsNo.
     *
     * @param ttcPartsNo ttcPartsNo
     */
    public void setTtcPartsNo(String ttcPartsNo) {
        this.ttcPartsNo = ttcPartsNo;
    }

    /**
     * Get the custPartsNo.
     *
     * @return custPartsNo
     */
    public String getCustPartsNo() {
        return this.custPartsNo;
    }

    /**
     * Set the custPartsNo.
     *
     * @param custPartsNo custPartsNo
     */
    public void setCustPartsNo(String custPartsNo) {
        this.custPartsNo = custPartsNo;
    }

    /**
     * Get the supplierId.
     *
     * @return supplierId
     */
    public Integer getSupplierId() {
        return this.supplierId;
    }

    /**
     * Set the supplierId.
     *
     * @param supplierId supplierId
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Get the ttcSuppCode.
     *
     * @return ttcSuppCode
     */
    public String getTtcSuppCode() {
        return this.ttcSuppCode;
    }

    /**
     * Set the ttcSuppCode.
     *
     * @param ttcSuppCode ttcSuppCode
     */
    public void setTtcSuppCode(String ttcSuppCode) {
        this.ttcSuppCode = ttcSuppCode;
    }

    /**
     * Get the uomCode.
     *
     * @return uomCode
     */
    public String getUomCode() {
        return this.uomCode;
    }

    /**
     * Set the uomCode.
     *
     * @param uomCode uomCode
     */
    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    /**
     * Get the orderQty.
     *
     * @return orderQty
     */
    public BigDecimal getOrderQty() {
        return this.orderQty;
    }

    /**
     * Set the orderQty.
     *
     * @param orderQty orderQty
     */
    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    /**
     * Get the forceCompletedQty.
     *
     * @return forceCompletedQty
     */
    public BigDecimal getForceCompletedQty() {
        return this.forceCompletedQty;
    }

    /**
     * Set the forceCompletedQty.
     *
     * @param forceCompletedQty forceCompletedQty
     */
    public void setForceCompletedQty(BigDecimal forceCompletedQty) {
        this.forceCompletedQty = forceCompletedQty;
    }

    /**
     * Get the currentCustomers.
     *
     * @return currentCustomers
     */
    public List<Integer> getCurrentCustomers() {
        return this.currentCustomers;
    }

    /**
     * Set the currentCustomers.
     *
     * @param currentCustomers currentCustomers
     */
    public void setCurrentCustomers(List<Integer> currentCustomers) {
        this.currentCustomers = currentCustomers;
    }

}
