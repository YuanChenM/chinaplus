/**
 * CPVIVF13Entity.java
 * 
 * @screen CPVIVF13
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.chinaplus.core.base.BaseEntity;

/**
 * New Invoice Upload Entity.
 */
public class CPVIVF13Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Office ID */
    private Integer officeId;

    /** Part ID */
    private Integer partsId;

    /** Supplier ID */
    private Integer supplierId;

    /** Supplier Code */
    private String supplierCode;

    /** Exp Region */
    private String expRegion;

    /** Imp Region */
    private String impRegion;

    /** TTC Part No. */
    private String ttcPartNo;

    /** Import Order No. */
    private String impOrderNo;

    /** Export Order No. */
    private String expOrderNo;

    /** Customer Order No. */
    private String cusOrderNo;

    /** TTC Customer Code */
    private String ttcCustomerCode;

    /** UOM Code */
    private String uomCode;

    /** Invoice Qty Map */
    private Map<Integer, BigDecimal> invoiceQtyMap;

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
     * Get the expRegion.
     *
     * @return expRegion
     */
    public String getExpRegion() {
        return this.expRegion;
    }

    /**
     * Set the expRegion.
     *
     * @param expRegion expRegion
     */
    public void setExpRegion(String expRegion) {
        this.expRegion = expRegion;
    }

    /**
     * Get the impRegion.
     *
     * @return impRegion
     */
    public String getImpRegion() {
        return this.impRegion;
    }

    /**
     * Set the impRegion.
     *
     * @param impRegion impRegion
     */
    public void setImpRegion(String impRegion) {
        this.impRegion = impRegion;
    }

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
     * Get the invoiceQtyMap.
     *
     * @return invoiceQtyMap
     */
    public Map<Integer, BigDecimal> getInvoiceQtyMap() {
        return this.invoiceQtyMap;
    }

    /**
     * Set the invoiceQtyMap.
     *
     * @param invoiceQtyMap invoiceQtyMap
     */
    public void setInvoiceQtyMap(Map<Integer, BigDecimal> invoiceQtyMap) {
        this.invoiceQtyMap = invoiceQtyMap;
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
