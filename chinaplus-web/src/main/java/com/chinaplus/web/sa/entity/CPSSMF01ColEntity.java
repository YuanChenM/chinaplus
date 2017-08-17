/**
 * CPSSMF01ColEntity.java
 * 
 * @screen CPSSMS01,CPSSMS02
 * @author ma_chao
 */
package com.chinaplus.web.sa.entity;

import java.util.Date;
import java.util.List;

import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Shipping Status Download Column Entity.
 */
public class CPSSMF01ColEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** SS Plan ID */
    private Integer ssPlanId;
    /** Invocie ID */
    private Integer invoiceId;
    /** Invoice No. */
    private String invoiceNo;
    /* Customer Order No. */
    private String customerOrderNo;
    /* Customer Order No. */
    private String customerCode;
    /* epo. */
    private String epo;
    /** TTC Supplier Code */
    private String ttcSupplierCode;
    /* IPO No. */
    private String ipo;
    /** Transport Mode */
    private Integer transportMode;
    /** Before Today Flag */
    private boolean beforeToday;
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
    /** Parts List */
    private List<CPSSMF01PartEntity> partsList;

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
     * Get the beforeToday.
     *
     * @return beforeToday
     */
    public boolean isBeforeToday() {
        return this.beforeToday;
    }

    /**
     * Set the beforeToday.
     *
     * @param beforeToday beforeToday
     */
    public void setBeforeToday(boolean beforeToday) {
        this.beforeToday = beforeToday;
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
     * Get the partsList.
     *
     * @return partsList
     */
    public List<CPSSMF01PartEntity> getPartsList() {
        return this.partsList;
    }

    /**
     * Set the partsList.
     *
     * @param partsList partsList
     */
    public void setPartsList(List<CPSSMF01PartEntity> partsList) {
        this.partsList = partsList;
    }
}
