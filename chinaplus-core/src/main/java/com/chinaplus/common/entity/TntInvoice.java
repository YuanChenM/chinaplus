package com.chinaplus.common.entity;

import java.io.Serializable;
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
 * The persistent class for the TNT_INVOICE database table.
 * 
 */
@Entity(name = "TNT_INVOICE")
public class TntInvoice extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_INVOICE_INVOICEID_GENERATOR",
        sequenceName = "SEQ_TNT_INVOICE",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_INVOICE_INVOICEID_GENERATOR")
    @Column(name = "INVOICE_ID")
    private Integer invoiceId;

    @Temporal(TemporalType.DATE)
    @Column(name = "CC_DATE")
    private Date ccDate;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Temporal(TemporalType.DATE)
    private Date eta;

    @Temporal(TemporalType.DATE)
    private Date etd;

    @Temporal(TemporalType.DATE)
    @Column(name = "IMP_CC_ACTUAL_DATE")
    private Date impCcActualDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "IMP_INB_ACTUAL_DATE")
    private Date impInbActualDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "IMP_INB_PLAN_DATE")
    private Date impInbPlanDate;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "ORIGINAL_VERSION")
    private Integer originalVersion;

    @Column(name = "REVISION_REASON")
    private String revisionReason;

    @Column(name = "REVISION_VERSION")
    private Integer revisionVersion;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "VANNING_DATE")
    private Date vanningDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "INVOICE_GROUP_ID")
    private Integer invoiceGroupId;

    @Column(name = "INVOICE_SUMMARY_ID")
    private Integer invoiceSummaryId;

    public TntInvoice() {}

    public Integer getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Date getCcDate() {
        return this.ccDate;
    }

    public void setCcDate(Date ccDate) {
        this.ccDate = ccDate;
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

    public Date getImpCcActualDate() {
        return this.impCcActualDate;
    }

    public void setImpCcActualDate(Date impCcActualDate) {
        this.impCcActualDate = impCcActualDate;
    }

    public Date getImpInbActualDate() {
        return this.impInbActualDate;
    }

    public void setImpInbActualDate(Date impInbActualDate) {
        this.impInbActualDate = impInbActualDate;
    }

    public Date getImpInbPlanDate() {
        return this.impInbPlanDate;
    }

    public void setImpInbPlanDate(Date impInbPlanDate) {
        this.impInbPlanDate = impInbPlanDate;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public Integer getOriginalVersion() {
        return this.originalVersion;
    }

    public void setOriginalVersion(Integer originalVersion) {
        this.originalVersion = originalVersion;
    }

    public String getRevisionReason() {
        return this.revisionReason;
    }

    public void setRevisionReason(String revisionReason) {
        this.revisionReason = revisionReason;
    }

    public Integer getRevisionVersion() {
        return this.revisionVersion;
    }

    public void setRevisionVersion(Integer revisionVersion) {
        this.revisionVersion = revisionVersion;
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

    public Integer getInvoiceGroupId() {
        return this.invoiceGroupId;
    }

    public void setInvoiceGroupId(Integer invoiceGroupId) {
        this.invoiceGroupId = invoiceGroupId;
    }

    public Integer getInvoiceSummaryId() {
        return this.invoiceSummaryId;
    }

    public void setInvoiceSummaryId(Integer invoiceSummaryId) {
        this.invoiceSummaryId = invoiceSummaryId;
    }

}