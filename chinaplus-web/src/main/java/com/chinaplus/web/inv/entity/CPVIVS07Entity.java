/**
 * CPVIVS07Entity.java
 * 
 * @screen CPVIVS07
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Create New Invoice Screen Entity.
 */
public class CPVIVS07Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Order ID */
    private Integer orderId;

    /** Import Order No. */
    private String impOrderNo;

    /** Customer Order No. */
    private String cusOrderNo;

    /** GSCM Sales Order Issue Date */
    private Date soDate;

    /** Exp Country */
    private String expCountry;

    /** Imp Country */
    private String impCountry;

    /** TTC Customer Code */
    private String ttcCustomerCode;

    /** Non Invoiced Qty */
    private BigDecimal nonInvoicedQty;

    /** Order Status */
    private Integer orderStatus;

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
     * Get the impOrderNo.
     *
     * @return impOrderNo
     */
    public String getImpOrderNo() {
        return this.impOrderNo;
    }

    /**
     * Set the impOrderNo.
     *
     * @param impOrderNo impOrderNo
     */
    public void setImpOrderNo(String impOrderNo) {
        this.impOrderNo = impOrderNo;
    }

    /**
     * Get the cusOrderNo.
     *
     * @return cusOrderNo
     */
    public String getCusOrderNo() {
        return this.cusOrderNo;
    }

    /**
     * Set the cusOrderNo.
     *
     * @param cusOrderNo cusOrderNo
     */
    public void setCusOrderNo(String cusOrderNo) {
        this.cusOrderNo = cusOrderNo;
    }

    /**
     * Get the soDate.
     *
     * @return soDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getSoDate() {
        return this.soDate;
    }

    /**
     * Set the soDate.
     *
     * @param soDate soDate
     */
    public void setSoDate(Date soDate) {
        this.soDate = soDate;
    }

    /**
     * Get the expCountry.
     *
     * @return expCountry
     */
    public String getExpCountry() {
        return this.expCountry;
    }

    /**
     * Set the expCountry.
     *
     * @param expCountry expCountry
     */
    public void setExpCountry(String expCountry) {
        this.expCountry = expCountry;
    }

    /**
     * Get the impCountry.
     *
     * @return impCountry
     */
    public String getImpCountry() {
        return this.impCountry;
    }

    /**
     * Set the impCountry.
     *
     * @param impCountry impCountry
     */
    public void setImpCountry(String impCountry) {
        this.impCountry = impCountry;
    }

    /**
     * Get the ttcCustomerCode.
     *
     * @return ttcCustomerCode
     */
    public String getTtcCustomerCode() {
        return this.ttcCustomerCode;
    }

    /**
     * Set the ttcCustomerCode.
     *
     * @param ttcCustomerCode ttcCustomerCode
     */
    public void setTtcCustomerCode(String ttcCustomerCode) {
        this.ttcCustomerCode = ttcCustomerCode;
    }

    /**
     * Get the nonInvoicedQty.
     *
     * @return nonInvoicedQty
     */
    public BigDecimal getNonInvoicedQty() {
        return this.nonInvoicedQty;
    }

    /**
     * Set the nonInvoicedQty.
     *
     * @param nonInvoicedQty nonInvoicedQty
     */
    public void setNonInvoicedQty(BigDecimal nonInvoicedQty) {
        this.nonInvoicedQty = nonInvoicedQty;
    }

    /**
     * Get the orderStatus.
     *
     * @return orderStatus
     */
    public Integer getOrderStatus() {
        return this.orderStatus;
    }

    /**
     * Set the orderStatus.
     *
     * @param orderStatus orderStatus
     */
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

}
