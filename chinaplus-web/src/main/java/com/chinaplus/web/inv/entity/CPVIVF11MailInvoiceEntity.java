/**
 * CPVIVF11MailInvoiceEntity.java
 * 
 * @screen CPVIVF11
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Invoice Upload Mail Invoice Entity.
 */
public class CPVIVF11MailInvoiceEntity extends CPVIVF11PartEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** WEST Invoice No. */
    private String invoiceNo;

    /** ETD */
    private Date etd;

    /** Container No. */
    private String containerNo;

    /** Start Pallet No. */
    private Integer startPalletNo;

    /** End Pallet No. */
    private Integer endPalletNo;

    /** Original Part No. */
    private String originalPartNo;

    /** Invoice Part No. */
    private String invoicePartNo;

    /** Qty */
    private BigDecimal qty;

    /** Excess Qty */
    private BigDecimal excessQty;

    /** Price */
    private String price;

    /** Currency */
    private String currency;

    /** Transport Mode */
    private String transportMode;

    /** Seal No. */
    private String sealNo;

    /** File Name */
    private String fileName;

    /** Line Number */
    private Integer lineNum;

    /** Supplier Codes */
    private List<String> supplierCodes;

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
     * Get the containerNo.
     *
     * @return containerNo
     */
    public String getContainerNo() {
        return this.containerNo;
    }

    /**
     * Set the containerNo.
     *
     * @param containerNo containerNo
     */
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    /**
     * Get the startPalletNo.
     *
     * @return startPalletNo
     */
    public Integer getStartPalletNo() {
        return this.startPalletNo;
    }

    /**
     * Set the startPalletNo.
     *
     * @param startPalletNo startPalletNo
     */
    public void setStartPalletNo(Integer startPalletNo) {
        this.startPalletNo = startPalletNo;
    }

    /**
     * Get the endPalletNo.
     *
     * @return endPalletNo
     */
    public Integer getEndPalletNo() {
        return this.endPalletNo;
    }

    /**
     * Set the endPalletNo.
     *
     * @param endPalletNo endPalletNo
     */
    public void setEndPalletNo(Integer endPalletNo) {
        this.endPalletNo = endPalletNo;
    }

    /**
     * Get the originalPartNo.
     *
     * @return originalPartNo
     */
    public String getOriginalPartNo() {
        return this.originalPartNo;
    }

    /**
     * Set the originalPartNo.
     *
     * @param originalPartNo originalPartNo
     */
    public void setOriginalPartNo(String originalPartNo) {
        this.originalPartNo = originalPartNo;
    }

    /**
     * Get the invoicePartNo.
     *
     * @return invoicePartNo
     */
    public String getInvoicePartNo() {
        return this.invoicePartNo;
    }

    /**
     * Set the invoicePartNo.
     *
     * @param invoicePartNo invoicePartNo
     */
    public void setInvoicePartNo(String invoicePartNo) {
        this.invoicePartNo = invoicePartNo;
    }

    /**
     * Get the excessQty.
     *
     * @return excessQty
     */
    public BigDecimal getExcessQty() {
        return this.excessQty;
    }

    /**
     * Set the excessQty.
     *
     * @param excessQty excessQty
     */
    public void setExcessQty(BigDecimal excessQty) {
        this.excessQty = excessQty;
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
     * Get the price.
     *
     * @return price
     */
    public String getPrice() {
        return this.price;
    }

    /**
     * Set the price.
     *
     * @param price price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Get the currency.
     *
     * @return currency
     */
    public String getCurrency() {
        return this.currency;
    }

    /**
     * Set the currency.
     *
     * @param currency currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
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
     * Get the sealNo.
     *
     * @return sealNo
     */
    public String getSealNo() {
        return this.sealNo;
    }

    /**
     * Set the sealNo.
     *
     * @param sealNo sealNo
     */
    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    /**
     * Get the fileName.
     *
     * @return fileName
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Set the fileName.
     *
     * @param fileName fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get the lineNum.
     *
     * @return lineNum
     */
    public Integer getLineNum() {
        return this.lineNum;
    }

    /**
     * Set the lineNum.
     *
     * @param lineNum lineNum
     */
    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    /**
     * Get the supplierCodes.
     *
     * @return supplierCodes
     */
    public List<String> getSupplierCodes() {
        return this.supplierCodes;
    }

    /**
     * Set the supplierCodes.
     *
     * @param supplierCodes supplierCodes
     */
    public void setSupplierCodes(List<String> supplierCodes) {
        this.supplierCodes = supplierCodes;
    }

}
