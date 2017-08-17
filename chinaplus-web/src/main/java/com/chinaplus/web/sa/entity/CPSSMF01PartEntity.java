/**
 * CPSSMF01PartEntity.java
 * 
 * @screen CPSSMS01,CPSSMS02
 * @author ma_chao
 */
package com.chinaplus.web.sa.entity;

import java.math.BigDecimal;

import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Shipping Status Download Part Entity.
 */
public class CPSSMF01PartEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /* Parts Id */
    private Integer partsId;
    /* UOM Code */
    private String uom;
    /* TTC Parts No. */
    private String ttcPartsNo;
    /* TTC Supplier Code */
    private String ttcSupplierCode;
    /* Force Completed Qty */
    private BigDecimal forceCompletedQty;
    /* Quantity */
    private BigDecimal qty;
    /* etd qty */
    private BigDecimal etdQty;
    /* etd balance qty */
    private BigDecimal etdBalanceQty;
    /** EXP PO NO. */
    private String epo;
    /** Imp PO NO. */
    private String ipo;
    /** Cus PO NO. */
    private String cpo;
    /** Customer ID. */
    private String customerId;
    /** Customer Code. */
    private String customerCode;

    /**
     * Get the ipo.
     *
     * @return ipo
     */
    public String getIpo() {
        return this.ipo;
    }

    /**
     * Set the ipo.
     *
     * @param ipo ipo
     */
    public void setIpo(String ipo) {
        this.ipo = ipo;
    }

    /**
     * Get the cpo.
     *
     * @return cpo
     */
    public String getCpo() {
        return this.cpo;
    }

    /**
     * Set the cpo.
     *
     * @param cpo cpo
     */
    public void setCpo(String cpo) {
        this.cpo = cpo;
    }

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
     * Get the uom.
     *
     * @return uom
     */
    public String getUom() {
        return this.uom;
    }

    /**
     * Set the uom.
     *
     * @param uom uom
     */
    public void setUom(String uom) {
        this.uom = uom;
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
     * Get the ttcSupplierCode.
     *
     * @return ttcSupplierCode
     */
    public String getTtcSupplierCode() {
        return this.ttcSupplierCode;
    }

    /**
     * Set the ttcSupplierCode.
     *
     * @param ttcSupplierCode ttcSupplierCode
     */
    public void setTtcSupplierCode(String ttcSupplierCode) {
        this.ttcSupplierCode = ttcSupplierCode;
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
     * Get the qty.
     *
     * @return qty
     */
    public BigDecimal getQty() {
        return this.qty;
    }

    /**
     * Set the qty.
     *
     * @param qty qty
     */
    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * Get the etdQty.
     *
     * @return etdQty
     */
    public BigDecimal getEtdQty() {
        return this.etdQty;
    }

    /**
     * Set the etdQty.
     *
     * @param etdQty etdQty
     */
    public void setEtdQty(BigDecimal etdQty) {
        this.etdQty = etdQty;
    }

    /**
     * Get the etdBalanceQty.
     *
     * @return etdBalanceQty
     */
    public BigDecimal getEtdBalanceQty() {
        return this.etdBalanceQty;
    }

    /**
     * Set the etdBalanceQty.
     *
     * @param etdBalanceQty etdBalanceQty
     */
    public void setEtdBalanceQty(BigDecimal etdBalanceQty) {
        this.etdBalanceQty = etdBalanceQty;
    }

    /**
     * Get the epo.
     *
     * @return epo
     */
    public String getEpo() {
        return this.epo;
    }

    /**
     * Set the epo.
     *
     * @param epo epo
     */
    public void setEpo(String epo) {
        this.epo = epo;
    }
}
