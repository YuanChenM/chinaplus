/**
 * CPVIVF01Entity.java
 * 
 * @screen CPVIVF01
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * Download Invoice Supplementary Data File Entity.
 */
public class CPVIVF01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Upload ID */
    private String uploadId;

    /** WEST Invoice No. */
    private String invoiceNo;

    /** ETD */
    private Date etd;

    /** TTC Customer Code */
    private String ttcCustomerCode;

    /** Mail Invoice Customer Code */
    private String mailCustomerCode;

    /** TTC Part No. */
    private String ttcPartNo;

    /** Supplier Part No. */
    private String supplierPartNo;

    /** Total Qty */
    private BigDecimal totalQty;

    /** Transport Mode */
    private String transportMode;

    /** TTC Supplier Code */
    private String ttcSupplierCode;

    /** Supplier Qty */
    private BigDecimal supplierQty;

    /** ETA */
    private Date eta;

    /** Inbound Date */
    private Date inboundDate;

    /** Uom Code */
    private String uomCode;

    /** Group row number */
    private int rownum;

    /**
     * Get the uploadId.
     *
     * @return uploadId
     */
    public String getUploadId() {
        return this.uploadId;
    }

    /**
     * Set the uploadId.
     *
     * @param uploadId uploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
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
     * Get the mailCustomerCode.
     *
     * @return mailCustomerCode
     */
    public String getMailCustomerCode() {
        return this.mailCustomerCode;
    }

    /**
     * Set the mailCustomerCode.
     *
     * @param mailCustomerCode mailCustomerCode
     */
    public void setMailCustomerCode(String mailCustomerCode) {
        this.mailCustomerCode = mailCustomerCode;
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
     * Get the supplierPartNo.
     *
     * @return supplierPartNo
     */
    public String getSupplierPartNo() {
        return this.supplierPartNo;
    }

    /**
     * Set the supplierPartNo.
     *
     * @param supplierPartNo supplierPartNo
     */
    public void setSupplierPartNo(String supplierPartNo) {
        this.supplierPartNo = supplierPartNo;
    }

    /**
     * Get the totalQty.
     *
     * @return totalQty
     */
    public BigDecimal getTotalQty() {
        return this.totalQty;
    }

    /**
     * Set the totalQty.
     *
     * @param totalQty totalQty
     */
    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    /**
     * Get the transportMode.
     *
     * @return transportMode
     */
    public String getTransportMode() {
        return this.transportMode;
    }

    /**
     * Set the transportMode.
     *
     * @param transportMode transportMode
     */
    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
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
     * Get the supplierQty.
     *
     * @return supplierQty
     */
    public BigDecimal getSupplierQty() {
        return this.supplierQty;
    }

    /**
     * Set the supplierQty.
     *
     * @param supplierQty supplierQty
     */
    public void setSupplierQty(BigDecimal supplierQty) {
        this.supplierQty = supplierQty;
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
     * Get the inboundDate.
     *
     * @return inboundDate
     */
    public Date getInboundDate() {
        return this.inboundDate;
    }

    /**
     * Set the inboundDate.
     *
     * @param inboundDate inboundDate
     */
    public void setInboundDate(Date inboundDate) {
        this.inboundDate = inboundDate;
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
     * Get the rownum.
     *
     * @return rownum
     */
    public int getRownum() {
        return this.rownum;
    }

    /**
     * Set the rownum.
     *
     * @param rownum rownum
     */
    public void setRownum(int rownum) {
        this.rownum = rownum;
    }

}
