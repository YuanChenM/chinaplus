/**
 * CPVIVF11PartEntity.java
 * 
 * @screen CPVIVF11
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import com.chinaplus.core.base.BaseEntity;

/**
 * Invoice Upload Part Entity.
 */
public class CPVIVF11PartEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Parts ID */
    private Integer partsId;

    /** TTC Parts No. */
    private String ttcPartsNo;

    /** UOM Code */
    private String uomCode;

    /** Customer ID */
    private Integer customerId;

    /** TTC Customer Code */
    private String ttcCustCode;

    /** Export Customer Code */
    private String expCustCode;

    /** Invoice Customer Code */
    private String invCustCode;

    /** Supplier ID */
    private Integer supplierId;

    /** Exp Parts ID */
    private Integer expPartsId;

    /** TTC Supplier Code */
    private String ttcSuppCode;

    /** Supplier Part No. */
    private String suppPartsNo;

    /** Exp Region */
    private String expRegion;

    /** Imp Region */
    private String impRegion;

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
     * Get the expCustCode.
     *
     * @return expCustCode
     */
    public String getExpCustCode() {
        return this.expCustCode;
    }

    /**
     * Set the expCustCode.
     *
     * @param expCustCode expCustCode
     */
    public void setExpCustCode(String expCustCode) {
        this.expCustCode = expCustCode;
    }

    /**
     * Get the invCustCode.
     *
     * @return invCustCode
     */
    public String getInvCustCode() {
        return this.invCustCode;
    }

    /**
     * Set the invCustCode.
     *
     * @param invCustCode invCustCode
     */
    public void setInvCustCode(String invCustCode) {
        this.invCustCode = invCustCode;
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
     * Get the expPartsId.
     *
     * @return expPartsId
     */
    public Integer getExpPartsId() {
        return this.expPartsId;
    }

    /**
     * Set the expPartsId.
     *
     * @param expPartsId expPartsId
     */
    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;
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

}
