/**
 * CPVIVS01Entity.java
 * 
 * @screen CPVIVS01
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Invoice Screen Entity.
 */
public class CPVIVS01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Invoice Summary ID */
    private Integer invoiceSummaryId;

    /** Business Pattern */
    private Integer businessPattern;

    /** Invoice ID */
    private Integer invoiceId;

    /** Invoice No. */
    private String invoiceNo;

    /** Irregular Shipping Schedule Update Status */
    private Integer irregularStatus;

    /** Irregular Status In Same Upload ID */
    private Integer groupIrregularStatus;

    /** Irregular Status Link */
    private boolean hasIrregularLink;

    /** Invoice Uploading Status */
    private Integer uploadStatus;

    /** Upload Status Link */
    private boolean hasUploadLink;

    /** Exp Country */
    private String expCountry;

    /** Imp Country */
    private String impCountry;

    /** TTC Supplier Code */
    private String ttcSupplierCode;

    /** Transport Mode */
    private Integer transportMode;

    /** ETD Date */
    private Date etd;

    /** ETA Date */
    private Date eta;

    /** Invoice Qty */
    private BigDecimal invoiceQty;

    /** Inbound Qty */
    private BigDecimal inboundQty;

    /** Pending Import Inbound Qty */
    private BigDecimal pendingQty;

    /** Upload ID */
    private String uploadId;

    /** User ID */
    private String userId;

    /** Upload or Receive Time */
    private Timestamp uploadReceiveTime;

    /** Invoice Type */
    private Integer invoiceType;

    /** Invoice Status */
    private Integer invoiceStatus;

    /** Post GR/GI Flag */
    private Integer postRIFlag;

    /** Have CC Date Status */
    private Integer ccStatus;

    /** Post GR/GI Status */
    private Integer postRIStatus;

    /** Version */
    private Integer version;

    /** System Time */
    private Timestamp systemTime;

    /** Login User Id */
    private Integer loginUserId;

    /** Invoice Summary ID */
    private List<String> summaryIds;

    /** IPO */
    private String ipo;

    /** CPO */
    private String cpo;

    /** EPO */
    private String epo;

    /** Supplier ID */
    private Integer supplierId;

    /** Parts ID */
    private Integer partsId;

    /** Parts Qty */
    private BigDecimal partsQty;

    /** KANBAN Plan No. */
    private String kanbanPlanNo;

    /** Issue Type */
    private Integer issueType;

    /**
     * Get the invoiceSummaryId.
     *
     * @return invoiceSummaryId
     */
    public Integer getInvoiceSummaryId() {
        return this.invoiceSummaryId;
    }

    /**
     * Set the invoiceSummaryId.
     *
     * @param invoiceSummaryId invoiceSummaryId
     */
    public void setInvoiceSummaryId(Integer invoiceSummaryId) {
        this.invoiceSummaryId = invoiceSummaryId;
    }

    /**
     * Get the businessPattern.
     *
     * @return businessPattern
     */
    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     */
    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
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
     * Get the irregularStatus.
     *
     * @return irregularStatus
     */
    public Integer getIrregularStatus() {
        return this.irregularStatus;
    }

    /**
     * Set the irregularStatus.
     *
     * @param irregularStatus irregularStatus
     */
    public void setIrregularStatus(Integer irregularStatus) {
        this.irregularStatus = irregularStatus;
    }

    /**
     * Get the groupIrregularStatus.
     *
     * @return groupIrregularStatus
     */
    public Integer getGroupIrregularStatus() {
        return this.groupIrregularStatus;
    }

    /**
     * Set the groupIrregularStatus.
     *
     * @param groupIrregularStatus groupIrregularStatus
     */
    public void setGroupIrregularStatus(Integer groupIrregularStatus) {
        this.groupIrregularStatus = groupIrregularStatus;
    }

    /**
     * Get the hasIrregularLink.
     *
     * @return hasIrregularLink
     */
    public boolean isHasIrregularLink() {
        return this.hasIrregularLink;
    }

    /**
     * Set the hasIrregularLink.
     *
     * @param hasIrregularLink hasIrregularLink
     */
    public void setHasIrregularLink(boolean hasIrregularLink) {
        this.hasIrregularLink = hasIrregularLink;
    }

    /**
     * Get the uploadStatus.
     *
     * @return uploadStatus
     */
    public Integer getUploadStatus() {
        return this.uploadStatus;
    }

    /**
     * Set the uploadStatus.
     *
     * @param uploadStatus uploadStatus
     */
    public void setUploadStatus(Integer uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    /**
     * Get the hasUploadLink.
     *
     * @return hasUploadLink
     */
    public boolean isHasUploadLink() {
        return this.hasUploadLink;
    }

    /**
     * Set the hasUploadLink.
     *
     * @param hasUploadLink hasUploadLink
     */
    public void setHasUploadLink(boolean hasUploadLink) {
        this.hasUploadLink = hasUploadLink;
    }

    /**
     * Get the expCountry.
     *
     * @return expCountry
     */
    public String getExpCountry() {
        return this.expCountry;
    }

    /**
     * Set the expCountry.
     *
     * @param expCountry expCountry
     */
    public void setExpCountry(String expCountry) {
        this.expCountry = expCountry;
    }

    /**
     * Get the impCountry.
     *
     * @return impCountry
     */
    public String getImpCountry() {
        return this.impCountry;
    }

    /**
     * Set the impCountry.
     *
     * @param impCountry impCountry
     */
    public void setImpCountry(String impCountry) {
        this.impCountry = impCountry;
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
    @JsonSerialize(using = JsonDateSerializer.class)
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
    @JsonSerialize(using = JsonDateSerializer.class)
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
     * Get the invoiceQty.
     *
     * @return invoiceQty
     */
    public BigDecimal getInvoiceQty() {
        return this.invoiceQty;
    }

    /**
     * Set the invoiceQty.
     *
     * @param invoiceQty invoiceQty
     */
    public void setInvoiceQty(BigDecimal invoiceQty) {
        this.invoiceQty = invoiceQty;
    }

    /**
     * Get the inboundQty.
     *
     * @return inboundQty
     */
    public BigDecimal getInboundQty() {
        return this.inboundQty;
    }

    /**
     * Set the inboundQty.
     *
     * @param inboundQty inboundQty
     */
    public void setInboundQty(BigDecimal inboundQty) {
        this.inboundQty = inboundQty;
    }

    /**
     * Get the pendingQty.
     *
     * @return pendingQty
     */
    public BigDecimal getPendingQty() {
        return this.pendingQty;
    }

    /**
     * Set the pendingQty.
     *
     * @param pendingQty pendingQty
     */
    public void setPendingQty(BigDecimal pendingQty) {
        this.pendingQty = pendingQty;
    }

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
     * Get the userId.
     *
     * @return userId
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Set the userId.
     *
     * @param userId userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Get the uploadReceiveTime.
     *
     * @return uploadReceiveTime
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getUploadReceiveTime() {
        return this.uploadReceiveTime;
    }

    /**
     * Set the uploadReceiveTime.
     *
     * @param uploadReceiveTime uploadReceiveTime
     */
    public void setUploadReceiveTime(Timestamp uploadReceiveTime) {
        this.uploadReceiveTime = uploadReceiveTime;
    }

    /**
     * Get the invoiceType.
     *
     * @return invoiceType
     */
    public Integer getInvoiceType() {
        return this.invoiceType;
    }

    /**
     * Set the invoiceType.
     *
     * @param invoiceType invoiceType
     */
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**
     * Get the invoiceStatus.
     *
     * @return invoiceStatus
     */
    public Integer getInvoiceStatus() {
        return this.invoiceStatus;
    }

    /**
     * Set the invoiceStatus.
     *
     * @param invoiceStatus invoiceStatus
     */
    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    /**
     * Get the postRIFlag.
     *
     * @return postRIFlag
     */
    public Integer getPostRIFlag() {
        return this.postRIFlag;
    }

    /**
     * Set the postRIFlag.
     *
     * @param postRIFlag postRIFlag
     */
    public void setPostRIFlag(Integer postRIFlag) {
        this.postRIFlag = postRIFlag;
    }

    /**
     * Get the ccStatus.
     *
     * @return ccStatus
     */
    public Integer getCcStatus() {
        return this.ccStatus;
    }

    /**
     * Set the ccStatus.
     *
     * @param ccStatus ccStatus
     */
    public void setCcStatus(Integer ccStatus) {
        this.ccStatus = ccStatus;
    }

    /**
     * Get the postRIStatus.
     *
     * @return postRIStatus
     */
    public Integer getPostRIStatus() {
        return this.postRIStatus;
    }

    /**
     * Set the postRIStatus.
     *
     * @param postRIStatus postRIStatus
     */
    public void setPostRIStatus(Integer postRIStatus) {
        this.postRIStatus = postRIStatus;
    }

    /**
     * Get the version.
     *
     * @return version
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * Set the version.
     *
     * @param version version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get the systemTime.
     *
     * @return systemTime
     */
    public Timestamp getSystemTime() {
        return this.systemTime;
    }

    /**
     * Set the systemTime.
     *
     * @param systemTime systemTime
     */
    public void setSystemTime(Timestamp systemTime) {
        this.systemTime = systemTime;
    }

    /**
     * Get the loginUserId.
     *
     * @return loginUserId
     */
    public Integer getLoginUserId() {
        return this.loginUserId;
    }

    /**
     * Set the loginUserId.
     *
     * @param loginUserId loginUserId
     */
    public void setLoginUserId(Integer loginUserId) {
        this.loginUserId = loginUserId;
    }

    /**
     * Get the summaryIds.
     *
     * @return summaryIds
     */
    public List<String> getSummaryIds() {
        return this.summaryIds;
    }

    /**
     * Set the summaryIds.
     *
     * @param summaryIds summaryIds
     */
    public void setSummaryIds(List<String> summaryIds) {
        this.summaryIds = summaryIds;
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
     * Get the partsQty.
     *
     * @return partsQty
     */
    public BigDecimal getPartsQty() {
        return this.partsQty;
    }

    /**
     * Set the partsQty.
     *
     * @param partsQty partsQty
     */
    public void setPartsQty(BigDecimal partsQty) {
        this.partsQty = partsQty;
    }

    /**
     * Get the kanbanPlanNo.
     *
     * @return kanbanPlanNo
     */
    public String getKanbanPlanNo() {
        return this.kanbanPlanNo;
    }

    /**
     * Set the kanbanPlanNo.
     *
     * @param kanbanPlanNo kanbanPlanNo
     */
    public void setKanbanPlanNo(String kanbanPlanNo) {
        this.kanbanPlanNo = kanbanPlanNo;
    }

    /**
     * Get the issueType.
     *
     * @return issueType
     */
    public Integer getIssueType() {
        return this.issueType;
    }

    /**
     * Set the issueType.
     *
     * @param issueType issueType
     */
    public void setIssueType(Integer issueType) {
        this.issueType = issueType;
    }

}
