package com.chinaplus.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the TNT_INVOICE_CONTAINER database table.
 * 
 */
@Entity(name = "TNT_INVOICE_CONTAINER")
public class TntInvoiceContainer extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_INVOICE_CONTAINER_INVOICECONTAINERID_GENERATOR",
        sequenceName = "SEQ_TNT_INVOICE_CONTAINER",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_INVOICE_CONTAINER_INVOICECONTAINERID_GENERATOR")
    @Column(name = "INVOICE_CONTAINER_ID")
    private Integer invoiceContainerId;

    @Column(name = "BUYING_CURRENCY")
    private String buyingCurrency;

    @Column(name = "BUYING_PRICE")
    private String buyingPrice;

    @Column(name = "CONTAINER_NO")
    private String containerNo;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    @Column(name = "INVOICE_PARTS_NO")
    private String invoicePartsNo;

    @Column(name = "MODULE_NO")
    private String moduleNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "CC_DATE")
    private Date ccDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "DEVANNED_DATE")
    private Date devannedDate;

    @Column(name = "\"STATUS\"")
    private Integer status;

    private BigDecimal qty;

    @Column(name = "SEAL_NO")
    private String sealNo;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "INVOICE_SUMMARY_ID")
    private Integer invoiceSummaryId;

    public TntInvoiceContainer() {}

    public Integer getInvoiceContainerId() {
        return this.invoiceContainerId;
    }

    public void setInvoiceContainerId(Integer invoiceContainerId) {
        this.invoiceContainerId = invoiceContainerId;
    }

    public String getBuyingCurrency() {
        return this.buyingCurrency;
    }

    public void setBuyingCurrency(String buyingCurrency) {
        this.buyingCurrency = buyingCurrency;
    }

    public String getBuyingPrice() {
        return this.buyingPrice;
    }

    public void setBuyingPrice(String buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getContainerNo() {
        return this.containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public String getInvoicePartsNo() {
        return this.invoicePartsNo;
    }

    public void setInvoicePartsNo(String invoicePartsNo) {
        this.invoicePartsNo = invoicePartsNo;
    }

    public String getModuleNo() {
        return this.moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public Date getCcDate() {
        return this.ccDate;
    }

    public void setCcDate(Date ccDate) {
        this.ccDate = ccDate;
    }

    public Date getDevannedDate() {
        return this.devannedDate;
    }

    public void setDevannedDate(Date devannedDate) {
        this.devannedDate = devannedDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getSealNo() {
        return this.sealNo;
    }

    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getInvoiceSummaryId() {
        return this.invoiceSummaryId;
    }

    public void setInvoiceSummaryId(Integer invoiceSummaryId) {
        this.invoiceSummaryId = invoiceSummaryId;
    }

}