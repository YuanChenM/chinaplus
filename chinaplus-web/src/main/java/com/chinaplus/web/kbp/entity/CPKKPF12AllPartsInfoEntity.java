/**
 * CPKKPF12AllPartsInfoEntity.java
 * 
 * @screen CPKKPF12
 * @author shiyang
 */
package com.chinaplus.web.kbp.entity;

import java.math.BigDecimal;

import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Kanban Plan Upload Entity.
 */
public class CPKKPF12AllPartsInfoEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Kanban ID */
    private int kanbanId;

    /** Parts ID */
    private int partsId;

    /** TTC Parts No */
    private String ttcPartsNo;

    /** Customer Code */
    private String customerCode;

    /** Kanban Cust Code */
    private String kanbanCustCode;

    /** Forecast Num */
    private int forecastNum;

    /** Supp Parts No. */
    private String suppPartsNo;

    /** TTC Supp Code */
    private String ttcSuppCode;

    /** Srbq */
    private BigDecimal srbq;

    /** Shipping Route Code */
    private String shippingRouteCode;

    /** Force Completed Qty */
    private BigDecimal forceCompletedQty;

    /** Order Qty */
    private BigDecimal orderQty;

    /** Status */
    private int status;

    /** Digits */
    private int digits;

    /** Parts Type */
    private int partsType;

    /** Customer Id */
    private int customerId;

    /** Supplier Id */
    private int supplierId;

    /** EXP Parts ID */
    private int expPartsId;

    /**
     * Get the kanbanId.
     *
     * @return kanbanId
     */
    public int getKanbanId() {
        return this.kanbanId;
    }

    /**
     * Set the kanbanId.
     *
     * @param kanbanId kanbanId
     */
    public void setKanbanId(int kanbanId) {
        this.kanbanId = kanbanId;
    }

    /**
     * Get the partsId.
     *
     * @return partsId
     */
    public int getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     */
    public void setPartsId(int partsId) {
        this.partsId = partsId;
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
     * Get the kanbanCustCode.
     *
     * @return kanbanCustCode
     */
    public String getKanbanCustCode() {
        return this.kanbanCustCode;
    }

    /**
     * Set the kanbanCustCode.
     *
     * @param kanbanCustCode kanbanCustCode
     */
    public void setKanbanCustCode(String kanbanCustCode) {
        this.kanbanCustCode = kanbanCustCode;
    }

    /**
     * Get the forecastNum.
     *
     * @return forecastNum
     */
    public int getForecastNum() {
        return this.forecastNum;
    }

    /**
     * Set the forecastNum.
     *
     * @param forecastNum forecastNum
     */
    public void setForecastNum(int forecastNum) {
        this.forecastNum = forecastNum;
    }

    /**
     * Get the suppPartsNo.
     *
     * @return suppPartsNo
     */
    public String getSuppPartsNo() {
        return this.suppPartsNo;
    }

    /**
     * Set the suppPartsNo.
     *
     * @param suppPartsNo suppPartsNo
     */
    public void setSuppPartsNo(String suppPartsNo) {
        this.suppPartsNo = suppPartsNo;
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
     * Get the shippingRouteCode.
     *
     * @return shippingRouteCode
     */
    public String getShippingRouteCode() {
        return this.shippingRouteCode;
    }

    /**
     * Set the shippingRouteCode.
     *
     * @param shippingRouteCode shippingRouteCode
     */
    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;
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
     * Get the status.
     *
     * @return status
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * Set the status.
     *
     * @param status status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Get the digits.
     *
     * @return digits
     */
    public int getDigits() {
        return this.digits;
    }

    /**
     * Set the digits.
     *
     * @param digits digits
     */
    public void setDigits(int digits) {
        this.digits = digits;
    }

    /**
     * Get the partsType.
     *
     * @return partsType
     */
    public int getPartsType() {
        return this.partsType;
    }

    /**
     * Set the partsType.
     *
     * @param partsType partsType
     */
    public void setPartsType(int partsType) {
        this.partsType = partsType;
    }

    /**
     * Get the customerId.
     *
     * @return customerId
     */
    public int getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     *
     * @param customerId customerId
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Get the supplierId.
     *
     * @return supplierId
     */
    public int getSupplierId() {
        return this.supplierId;
    }

    /**
     * Set the supplierId.
     *
     * @param supplierId supplierId
     */
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Get the expPartsId.
     *
     * @return expPartsId
     */
    public int getExpPartsId() {
        return this.expPartsId;
    }

    /**
     * Set the expPartsId.
     *
     * @param expPartsId expPartsId
     */
    public void setExpPartsId(int expPartsId) {
        this.expPartsId = expPartsId;
    }
}
