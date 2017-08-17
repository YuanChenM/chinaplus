/**
 * CPVIVF03Entity.java
 * 
 * @screen CPVIVF03
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.math.BigDecimal;

import com.chinaplus.core.base.BaseEntity;

/**
 * Download New Invoice Entity.
 */
public class CPVIVF03Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** TTC Part No. */
    private String ttcPartNo;

    /** TTC Supplier Code */
    private String supplierCode;

    /** Parts Description (Chinese) */
    private String partsNameCn;

    /** Import Order No. */
    private String impOrderNo;

    /** Export Order No. */
    private String expOrderNo;

    /** Customer Order No. */
    private String cusOrderNo;

    /** TTC Customer Code */
    private String ttcCustomerCode;

    /** Non Invoiced Qty */
    private BigDecimal nonInvoicedQty;

    /** Uom Code */
    private String uomCode;

    /**
     * Get the ttcPartNo.
     *
     * @return ttcPartNo
     */
    public String getTtcPartNo() {
        return this.ttcPartNo;
    }

    /**
     * Set the ttcPartNo.
     *
     * @param ttcPartNo ttcPartNo
     */
    public void setTtcPartNo(String ttcPartNo) {
        this.ttcPartNo = ttcPartNo;
    }

    /**
     * Get the supplierCode.
     *
     * @return supplierCode
     */
    public String getSupplierCode() {
        return this.supplierCode;
    }

    /**
     * Set the supplierCode.
     *
     * @param supplierCode supplierCode
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * Get the partsNameCn.
     *
     * @return partsNameCn
     */
    public String getPartsNameCn() {
        return this.partsNameCn;
    }

    /**
     * Set the partsNameCn.
     *
     * @param partsNameCn partsNameCn
     */
    public void setPartsNameCn(String partsNameCn) {
        this.partsNameCn = partsNameCn;
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
     * Get the expOrderNo.
     *
     * @return expOrderNo
     */
    public String getExpOrderNo() {
        return this.expOrderNo;
    }

    /**
     * Set the expOrderNo.
     *
     * @param expOrderNo expOrderNo
     */
    public void setExpOrderNo(String expOrderNo) {
        this.expOrderNo = expOrderNo;
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

}
