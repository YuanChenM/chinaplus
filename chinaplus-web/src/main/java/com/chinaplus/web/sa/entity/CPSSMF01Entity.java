/**
 * CPSSMF01Entity.java
 * 
 * @screen CPSSMS01
 * @author ma_chao
 */
package com.chinaplus.web.sa.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Shipping Status Download search Entity.
 */
public class CPSSMF01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** ORDER ID */
    private Integer orderId;
    /** SS Plan ID */
    private Integer ssPlanId;
    /** Invocie ID */
    private Integer invoiceId;
    /** Invoice No. */
    private String invoiceNo;
    /** Transport Mode */
    private Integer transportMode;
    /* ETD */
    private Date etd;
    /* ETA */
    private Date eta;
    /* Customer Clearance Date */
    private Date ccDate;
    /* Imp Inbound Plan Date */
    private Date impInbPlanDate;
    /* Imp Inbound Actual Date */
    private Date ipmInbActulDate;
    /* Original Version */
    private Integer originalVersion;
    /* Revision Version */
    private Integer revisionVersion;
    /* Revision Reason */
    private String revisionReason;
    /* Not in Run Down Flag */
    private Integer nirdFlag;
    /* Completed Flag */
    private Integer completedFlag;
    /* Parts Id */
    private Integer partsId;
    /* UOM Code */
    private String uom;
    /* TTC Parts No. */
    private String ttcPartsNo;
    /* Parts Name in English */
    private String partsNameEn;
    /* Parts Name in Chinese */
    private String partsNameCn;
    /* Exp Country */
    private String expRegion;
    /* TTC Supplier Code */
    private String ttcSupplierCode;
    /* Imp Country */
    private String impRegion;
    /* TTC Customer Code */
    private String customerCode;
    /* Customer Parts No. */
    private String customerPartsNo;
    /* Customer Order No. */
    private String customerOrderNo;
    /* Back No. */
    private String custBackNo;
    /* IPO No. */
    private String ipo;
    /* EPO No. */
    private String epo;
    /* Order Type */
    private String orderType;
    /* On Shipping Delay Pattern */
    private Integer delayAdjustmentPattern;
    /* SPQ */
    private BigDecimal spq;
    /* GSCM Sales Order Issued Date */
    private Date expSoDate;
    /* Total Order Qty */
    private BigDecimal orderQty;
    /* Force Completed Qty */
    private BigDecimal forceCompletedQty;
    /* Quantity */
    private BigDecimal qty;

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
     * Get the ssPlanId.
     *
     * @return ssPlanId
     */
    public Integer getSsPlanId() {
        return this.ssPlanId;
    }

    /**
     * Set the ssPlanId.
     *
     * @param ssPlanId ssPlanId
     */
    public void setSsPlanId(Integer ssPlanId) {
        this.ssPlanId = ssPlanId;
    }

    /**
     * Get the invoiceId.
     *
     * @return invoiceId
     */
    public Integer getInvoiceId() {
        return this.invoiceId;
    }

    /**
     * Set the invoiceId.
     *
     * @param invoiceId invoiceId
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * Get the invoiceNo.
     *
     * @return invoiceNo
     */
    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    /**
     * Set the invoiceNo.
     *
     * @param invoiceNo invoiceNo
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     * Get the transportMode.
     *
     * @return transportMode
     */
    public Integer getTransportMode() {
        return this.transportMode;
    }

    /**
     * Set the transportMode.
     *
     * @param transportMode transportMode
     */
    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * Get the etd.
     *
     * @return etd
     */
    public Date getEtd() {
        return this.etd;
    }

    /**
     * Set the etd.
     *
     * @param etd etd
     */
    public void setEtd(Date etd) {
        this.etd = etd;
    }

    /**
     * Get the eta.
     *
     * @return eta
     */
    public Date getEta() {
        return this.eta;
    }

    /**
     * Set the eta.
     *
     * @param eta eta
     */
    public void setEta(Date eta) {
        this.eta = eta;
    }

    /**
     * Get the ccDate.
     *
     * @return ccDate
     */
    public Date getCcDate() {
        return this.ccDate;
    }

    /**
     * Set the ccDate.
     *
     * @param ccDate ccDate
     */
    public void setCcDate(Date ccDate) {
        this.ccDate = ccDate;
    }

    /**
     * Get the impInbPlanDate.
     *
     * @return impInbPlanDate
     */
    public Date getImpInbPlanDate() {
        return this.impInbPlanDate;
    }

    /**
     * Set the impInbPlanDate.
     *
     * @param impInbPlanDate impInbPlanDate
     */
    public void setImpInbPlanDate(Date impInbPlanDate) {
        this.impInbPlanDate = impInbPlanDate;
    }

    /**
     * Get the ipmInbActulDate.
     *
     * @return ipmInbActulDate
     */
    public Date getIpmInbActulDate() {
        return this.ipmInbActulDate;
    }

    /**
     * Set the ipmInbActulDate.
     *
     * @param ipmInbActulDate ipmInbActulDate
     */
    public void setIpmInbActulDate(Date ipmInbActulDate) {
        this.ipmInbActulDate = ipmInbActulDate;
    }

    /**
     * Get the originalVersion.
     *
     * @return originalVersion
     */
    public Integer getOriginalVersion() {
        return this.originalVersion;
    }

    /**
     * Set the originalVersion.
     *
     * @param originalVersion originalVersion
     */
    public void setOriginalVersion(Integer originalVersion) {
        this.originalVersion = originalVersion;
    }

    /**
     * Get the revisionVersion.
     *
     * @return revisionVersion
     */
    public Integer getRevisionVersion() {
        return this.revisionVersion;
    }

    /**
     * Set the revisionVersion.
     *
     * @param revisionVersion revisionVersion
     */
    public void setRevisionVersion(Integer revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    /**
     * Get the revisionReason.
     *
     * @return revisionReason
     */
    public String getRevisionReason() {
        return this.revisionReason;
    }

    /**
     * Set the revisionReason.
     *
     * @param revisionReason revisionReason
     */
    public void setRevisionReason(String revisionReason) {
        this.revisionReason = revisionReason;
    }

    /**
     * Get the nirdFlag.
     *
     * @return nirdFlag
     */
    public Integer getNirdFlag() {
        return this.nirdFlag;
    }

    /**
     * Set the nirdFlag.
     *
     * @param nirdFlag nirdFlag
     */
    public void setNirdFlag(Integer nirdFlag) {
        this.nirdFlag = nirdFlag;
    }

    /**
     * Get the completedFlag.
     *
     * @return completedFlag
     */
    public Integer getCompletedFlag() {
        return this.completedFlag;
    }

    /**
     * Set the completedFlag.
     *
     * @param completedFlag completedFlag
     */
    public void setCompletedFlag(Integer completedFlag) {
        this.completedFlag = completedFlag;
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
     * Get the partsNameEn.
     *
     * @return partsNameEn
     */
    public String getPartsNameEn() {
        return this.partsNameEn;
    }

    /**
     * Set the partsNameEn.
     *
     * @param partsNameEn partsNameEn
     */
    public void setPartsNameEn(String partsNameEn) {
        this.partsNameEn = partsNameEn;
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
     * Get the customerPartsNo.
     *
     * @return customerPartsNo
     */
    public String getCustomerPartsNo() {
        return this.customerPartsNo;
    }

    /**
     * Set the customerPartsNo.
     *
     * @param customerPartsNo customerPartsNo
     */
    public void setCustomerPartsNo(String customerPartsNo) {
        this.customerPartsNo = customerPartsNo;
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
     * Get the custBackNo.
     *
     * @return custBackNo
     */
    public String getCustBackNo() {
        return this.custBackNo;
    }

    /**
     * Set the custBackNo.
     *
     * @param custBackNo custBackNo
     */
    public void setCustBackNo(String custBackNo) {
        this.custBackNo = custBackNo;
    }

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

    /**
     * Get the orderType.
     *
     * @return orderType
     */
    public String getOrderType() {
        return this.orderType;
    }

    /**
     * Set the orderType.
     *
     * @param orderType orderType
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * Get the delayAdjustmentPattern.
     *
     * @return delayAdjustmentPattern
     */
    public Integer getDelayAdjustmentPattern() {
        return this.delayAdjustmentPattern;
    }

    /**
     * Set the delayAdjustmentPattern.
     *
     * @param delayAdjustmentPattern delayAdjustmentPattern
     */
    public void setDelayAdjustmentPattern(Integer delayAdjustmentPattern) {
        this.delayAdjustmentPattern = delayAdjustmentPattern;
    }

    /**
     * Get the spq.
     *
     * @return spq
     */
    public BigDecimal getSpq() {
        return this.spq;
    }

    /**
     * Set the spq.
     *
     * @param spq spq
     */
    public void setSpq(BigDecimal spq) {
        this.spq = spq;
    }

    /**
     * Get the expSoDate.
     *
     * @return expSoDate
     */
    public Date getExpSoDate() {
        return this.expSoDate;
    }

    /**
     * Set the expSoDate.
     *
     * @param expSoDate expSoDate
     */
    public void setExpSoDate(Date expSoDate) {
        this.expSoDate = expSoDate;
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

}
