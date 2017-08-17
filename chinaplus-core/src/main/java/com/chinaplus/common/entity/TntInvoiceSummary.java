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
 * The persistent class for the TNT_INVOICE_SUMMARY database table.
 * 
 */
@Entity(name = "TNT_INVOICE_SUMMARY")
public class TntInvoiceSummary extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_INVOICE_SUMMARY_INVOICESUMMARYID_GENERATOR",
        sequenceName = "SEQ_TNT_INVOICE_SUMMARY",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_INVOICE_SUMMARY_INVOICESUMMARYID_GENERATOR")
    @Column(name = "INVOICE_SUMMARY_ID")
    private Integer invoiceSummaryId;

    @Temporal(TemporalType.DATE)
    @Column(name = "BL_DATE")
    private Date blDate;

    @Column(name = "BL_NO")
    private String blNo;

    @Column(name = "BUSINESS_PATTERN")
    private Integer businessPattern;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Temporal(TemporalType.DATE)
    private Date eta;

    @Temporal(TemporalType.DATE)
    private Date etd;

    @Column(name = "EXP_REGION")
    private String expRegion;

    @Temporal(TemporalType.DATE)
    @Column(name = "GI_DATE")
    private Date giDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "GR_DATE")
    private Date grDate;

    @Column(name = "IMP_REGION")
    private String impRegion;

    @Column(name = "INBOUND_QTY")
    private BigDecimal inboundQty;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "INVOICE_QTY")
    private BigDecimal invoiceQty;

    @Column(name = "INVOICE_STATUS")
    private Integer invoiceStatus;

    @Column(name = "INVOICE_TYPE")
    private Integer invoiceType;

    @Column(name = "NON_SHIPPING_ROUTE")
    private Integer nonShippingRoute;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "POST_RI_FLAG")
    private Integer postRiFlag;

    @Column(name = "SUPPLIER_CODE_SET")
    private String supplierCodeSet;

    @Column(name = "TRANSPORT_MODE")
    private Integer transportMode;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "UPLOAD_ID")
    private String uploadId;

    @Column(name = "UPLOAD_STATUS")
    private Integer uploadStatus;

    @Column(name = "UPLOADED_BY")
    private Integer uploadedBy;

    @Column(name = "UPLOADED_DATE")
    private Timestamp uploadedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "VANNING_DATE")
    private Date vanningDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "VESSEL_NAME")
    private String vesselName;

    @Column(name = "ISSUE_TYPE")
    private Integer issueType;

    public TntInvoiceSummary() {}

    public Integer getInvoiceSummaryId() {
        return this.invoiceSummaryId;
    }

    public void setInvoiceSummaryId(Integer invoiceSummaryId) {
        this.invoiceSummaryId = invoiceSummaryId;
    }

    public Date getBlDate() {
        return this.blDate;
    }

    public void setBlDate(Date blDate) {
        this.blDate = blDate;
    }

    public String getBlNo() {
        return this.blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
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

    public Date getEta() {
        return this.eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getEtd() {
        return this.etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public String getExpRegion() {
        return this.expRegion;
    }

    public void setExpRegion(String expRegion) {
        this.expRegion = expRegion;
    }

    public Date getGiDate() {
        return this.giDate;
    }

    public void setGiDate(Date giDate) {
        this.giDate = giDate;
    }

    public Date getGrDate() {
        return this.grDate;
    }

    public void setGrDate(Date grDate) {
        this.grDate = grDate;
    }

    public String getImpRegion() {
        return this.impRegion;
    }

    public void setImpRegion(String impRegion) {
        this.impRegion = impRegion;
    }

    public BigDecimal getInboundQty() {
        return this.inboundQty;
    }

    public void setInboundQty(BigDecimal inboundQty) {
        this.inboundQty = inboundQty;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public BigDecimal getInvoiceQty() {
        return this.invoiceQty;
    }

    public void setInvoiceQty(BigDecimal invoiceQty) {
        this.invoiceQty = invoiceQty;
    }

    public Integer getInvoiceStatus() {
        return this.invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Integer getInvoiceType() {
        return this.invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Integer getNonShippingRoute() {
        return this.nonShippingRoute;
    }

    public void setNonShippingRoute(Integer nonShippingRoute) {
        this.nonShippingRoute = nonShippingRoute;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public Integer getPostRiFlag() {
        return this.postRiFlag;
    }

    public void setPostRiFlag(Integer postRiFlag) {
        this.postRiFlag = postRiFlag;
    }

    public String getSupplierCodeSet() {
        return this.supplierCodeSet;
    }

    public void setSupplierCodeSet(String supplierCodeSet) {
        this.supplierCodeSet = supplierCodeSet;
    }

    public Integer getTransportMode() {
        return this.transportMode;
    }

    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
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

    public String getUploadId() {
        return this.uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public Integer getUploadStatus() {
        return this.uploadStatus;
    }

    public void setUploadStatus(Integer uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public Integer getUploadedBy() {
        return this.uploadedBy;
    }

    public void setUploadedBy(Integer uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Timestamp getUploadedDate() {
        return this.uploadedDate;
    }

    public void setUploadedDate(Timestamp uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public Date getVanningDate() {
        return this.vanningDate;
    }

    public void setVanningDate(Date vanningDate) {
        this.vanningDate = vanningDate;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getVesselName() {
        return this.vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public Integer getIssueType() {
        return this.issueType;
    }

    public void setIssueType(Integer issueType) {
        this.issueType = issueType;
    }

}