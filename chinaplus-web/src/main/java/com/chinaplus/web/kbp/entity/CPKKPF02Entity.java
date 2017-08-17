/**
 * CPKKPF02Entity.java
 * 
 * @screen CPKKPF02
 * @author shiyang
 */
package com.chinaplus.web.kbp.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Download Kanban Plan for Revision History(doc2) Entity.
 */
public class CPKKPF02Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Kanban Id */
    private Integer kanbanId;

    /** Parts Id */
    private Integer partsId;

    /** TTC Parts No. */
    private String ttcPartsNo;

    /** Old TTC Parts No. */
    private String oldTtcPartsNo;

    /** Customer Code */
    private String customerCode;

    /** Exp Cust Code */
    private String expCustCode;

    /** Supp Parts No. */
    private String suppPartsNo;

    /** TTC Supp Code */
    private String ttcSuppCode;

    /** Remark */
    private String remark;

    /** SPQ */
    private BigDecimal spq;

    /** QTY */
    private BigDecimal qty;

    /** Kanban QTY */
    private BigDecimal kanbanQty;

    /** Invoice Group Total QTY */
    private BigDecimal invoiceGroupTotalQty;

    /** FC QTY1 */
    private BigDecimal fcQty1;

    /** FC QTY2 */
    private BigDecimal fcQty2;

    /** FC QTY3 */
    private BigDecimal fcQty3;

    /** FC QTY4 */
    private BigDecimal fcQty4;

    /** FC QTY5 */
    private BigDecimal fcQty5;

    /** FC QTY6 */
    private BigDecimal fcQty6;

    /** Forecast Num */
    private Integer forecastNum;

    /** Inactive Flag */
    private String inactiveFlag;

    /** Inactive Flag */
    private Integer decimalDigits;

    /** Exp OnShipping QTY */
    private BigDecimal expOnShippingQty;

    /** In Rack QTY */
    private BigDecimal inRackQty;

    /** Imp Stock QTY */
    private BigDecimal impStockQty;

    /** Imp Adjusted QTY */
    private BigDecimal impAdjustedQty;

    /** Imp Delivered QTY */
    private BigDecimal impDeliveredQty;

    /** Force Completed QTY */
    private BigDecimal forceCompletedQty;

    /** Transfer Out QTY */
    private BigDecimal transferOutQty;

    /** Transfer Out Detail QTY */
    private BigDecimal transferOutDetailQty;

    /** Transfer Out Detail Customer Code */
    private String transferOutDetailCustomerCode;

    /** Shipping UUID */
    private String shippingUuid;

    /** Kanban Shipping Id */
    private String kanbanShippingId;

    /** Kanban Plan Id */
    private String kanbanPlanId;

    /** Plan Type */
    private Integer planType;

    /** Issued Date (KANBAN_PLAN) */
    private Timestamp issuedDate;

    /** Delivered Date */
    private Timestamp deliveredDate;

    /** Vanning Date */
    private Timestamp vanningDate;

    /** Revision Reason */
    private String revisionReason;

    /** Invoice Group Id */
    private String invoiceGroupId;

    /** Transport Mode */
    private Integer transportMode;

    /** ETD (INVOICE_GROUP / SHIPPING PLAN) */
    private Timestamp etd;

    /** ETA (INVOICE_GROUP / SHIPPING PLAN) */
    private Timestamp eta;

    /** Imp Inb Plan Date (INVOICE_GROUP / SHIPPING PLAN) */
    private Timestamp impInbPlanDate;

    /** Invoice No */
    private String invoiceNo;

    /** Original Version */
    private String originalVersion;

    /** Revision Version */
    private String revisionVersion;

    /** ETD (INVOICE) */
    private Timestamp etdInvoice;

    /** ETA (INVOICE) */
    private Timestamp etaInvoice;

    /** Imp Inb Plan Date (INVOICE) */
    private Timestamp impInbPlanDateInvoice;

    /** Imp Inb Actual Date (INVOICE) */
    private Timestamp impInbActualDateInvoice;

    /** Nird Flag */
    private Integer nirdFlag;

    /** Issue Remark */
    private String issueRemark;

    /** Delivere Remark */
    private String delivereRemark;

    /** Vanning Remark */
    private String vanningRemark;

    /** Qty With Invoice */
    private BigDecimal qtyWithInvoice;

    /** Order Balance based on actual ETD */
    private BigDecimal orderBalanceBasedOnActualEtd;

    /** Actual Inbound Qty */
    private BigDecimal actualInboundQty;

    /** Order Balance based on actual inbound */
    private BigDecimal orderBalanceBasedOnActualInbound;

    /** Status */
    private Integer status;

    /** Stock Transfer Out Details */
    private String stockTransferOutDetails;

    /** First Invice Group BoxDate */
    private Timestamp firstInviceGroupBoxDate;

    /** Last Shipping Plan Box Date */
    private Timestamp lastShippingPlanBoxDate;

    /** IMP Stock Flag */
    private Integer impStockFlag;

    /**
     * Get the kanbanId.
     *
     * @return kanbanId
     */
    public Integer getKanbanId() {
        return this.kanbanId;
    }

    /**
     * Set the kanbanId.
     *
     * @param kanbanId kanbanId
     */
    public void setKanbanId(Integer kanbanId) {
        this.kanbanId = kanbanId;
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
     * Get the oldTtcPartsNo.
     *
     * @return oldTtcPartsNo
     */
    public String getOldTtcPartsNo() {
        return this.oldTtcPartsNo;
    }

    /**
     * Set the oldTtcPartsNo.
     *
     * @param oldTtcPartsNo oldTtcPartsNo
     */
    public void setOldTtcPartsNo(String oldTtcPartsNo) {
        this.oldTtcPartsNo = oldTtcPartsNo;
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
     * Get the remark.
     *
     * @return remark
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * Set the remark.
     *
     * @param remark remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * Get the kanbanQty.
     *
     * @return kanbanQty
     */
    public BigDecimal getKanbanQty() {
        return this.kanbanQty;
    }

    /**
     * Set the kanbanQty.
     *
     * @param kanbanQty kanbanQty
     */
    public void setKanbanQty(BigDecimal kanbanQty) {
        this.kanbanQty = kanbanQty;
    }

    /**
     * Get the invoiceGroupTotalQty.
     *
     * @return invoiceGroupTotalQty
     */
    public BigDecimal getInvoiceGroupTotalQty() {
        return this.invoiceGroupTotalQty;
    }

    /**
     * Set the invoiceGroupTotalQty.
     *
     * @param invoiceGroupTotalQty invoiceGroupTotalQty
     */
    public void setInvoiceGroupTotalQty(BigDecimal invoiceGroupTotalQty) {
        this.invoiceGroupTotalQty = invoiceGroupTotalQty;
    }

    /**
     * Get the fcQty1.
     *
     * @return fcQty1
     */
    public BigDecimal getFcQty1() {
        return this.fcQty1;
    }

    /**
     * Set the fcQty1.
     *
     * @param fcQty1 fcQty1
     */
    public void setFcQty1(BigDecimal fcQty1) {
        this.fcQty1 = fcQty1;
    }

    /**
     * Get the fcQty2.
     *
     * @return fcQty2
     */
    public BigDecimal getFcQty2() {
        return this.fcQty2;
    }

    /**
     * Set the fcQty2.
     *
     * @param fcQty2 fcQty2
     */
    public void setFcQty2(BigDecimal fcQty2) {
        this.fcQty2 = fcQty2;
    }

    /**
     * Get the fcQty3.
     *
     * @return fcQty3
     */
    public BigDecimal getFcQty3() {
        return this.fcQty3;
    }

    /**
     * Set the fcQty3.
     *
     * @param fcQty3 fcQty3
     */
    public void setFcQty3(BigDecimal fcQty3) {
        this.fcQty3 = fcQty3;
    }

    /**
     * Get the fcQty4.
     *
     * @return fcQty4
     */
    public BigDecimal getFcQty4() {
        return this.fcQty4;
    }

    /**
     * Set the fcQty4.
     *
     * @param fcQty4 fcQty4
     */
    public void setFcQty4(BigDecimal fcQty4) {
        this.fcQty4 = fcQty4;
    }

    /**
     * Get the fcQty5.
     *
     * @return fcQty5
     */
    public BigDecimal getFcQty5() {
        return this.fcQty5;
    }

    /**
     * Set the fcQty5.
     *
     * @param fcQty5 fcQty5
     */
    public void setFcQty5(BigDecimal fcQty5) {
        this.fcQty5 = fcQty5;
    }

    /**
     * Get the fcQty6.
     *
     * @return fcQty6
     */
    public BigDecimal getFcQty6() {
        return this.fcQty6;
    }

    /**
     * Set the fcQty6.
     *
     * @param fcQty6 fcQty6
     */
    public void setFcQty6(BigDecimal fcQty6) {
        this.fcQty6 = fcQty6;
    }

    /**
     * Get the forecastNum.
     *
     * @return forecastNum
     */
    public Integer getForecastNum() {
        return this.forecastNum;
    }

    /**
     * Set the forecastNum.
     *
     * @param forecastNum forecastNum
     */
    public void setForecastNum(Integer forecastNum) {
        this.forecastNum = forecastNum;
    }

    /**
     * Get the inactiveFlag.
     *
     * @return inactiveFlag
     */
    public String getInactiveFlag() {
        return this.inactiveFlag;
    }

    /**
     * Set the inactiveFlag.
     *
     * @param inactiveFlag inactiveFlag
     */
    public void setInactiveFlag(String inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
    }

    /**
     * Get the decimalDigits.
     *
     * @return decimalDigits
     */
    public Integer getDecimalDigits() {
        return this.decimalDigits;
    }

    /**
     * Set the decimalDigits.
     *
     * @param decimalDigits decimalDigits
     */
    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    /**
     * Get the expOnShippingQty.
     *
     * @return expOnShippingQty
     */
    public BigDecimal getExpOnShippingQty() {
        return this.expOnShippingQty;
    }

    /**
     * Set the expOnShippingQty.
     *
     * @param expOnShippingQty expOnShippingQty
     */
    public void setExpOnShippingQty(BigDecimal expOnShippingQty) {
        this.expOnShippingQty = expOnShippingQty;
    }

    /**
     * Get the inRackQty.
     *
     * @return inRackQty
     */
    public BigDecimal getInRackQty() {
        return this.inRackQty;
    }

    /**
     * Set the inRackQty.
     *
     * @param inRackQty inRackQty
     */
    public void setInRackQty(BigDecimal inRackQty) {
        this.inRackQty = inRackQty;
    }

    /**
     * Get the impStockQty.
     *
     * @return impStockQty
     */
    public BigDecimal getImpStockQty() {
        return this.impStockQty;
    }

    /**
     * Set the impStockQty.
     *
     * @param impStockQty impStockQty
     */
    public void setImpStockQty(BigDecimal impStockQty) {
        this.impStockQty = impStockQty;
    }

    /**
     * Get the impAdjustedQty.
     *
     * @return impAdjustedQty
     */
    public BigDecimal getImpAdjustedQty() {
        return this.impAdjustedQty;
    }

    /**
     * Set the impAdjustedQty.
     *
     * @param impAdjustedQty impAdjustedQty
     */
    public void setImpAdjustedQty(BigDecimal impAdjustedQty) {
        this.impAdjustedQty = impAdjustedQty;
    }

    /**
     * Get the impDeliveredQty.
     *
     * @return impDeliveredQty
     */
    public BigDecimal getImpDeliveredQty() {
        return this.impDeliveredQty;
    }

    /**
     * Set the impDeliveredQty.
     *
     * @param impDeliveredQty impDeliveredQty
     */
    public void setImpDeliveredQty(BigDecimal impDeliveredQty) {
        this.impDeliveredQty = impDeliveredQty;
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
     * Get the transferOutQty.
     *
     * @return transferOutQty
     */
    public BigDecimal getTransferOutQty() {
        return this.transferOutQty;
    }

    /**
     * Set the transferOutQty.
     *
     * @param transferOutQty transferOutQty
     */
    public void setTransferOutQty(BigDecimal transferOutQty) {
        this.transferOutQty = transferOutQty;
    }

    /**
     * Get the transferOutDetailQty.
     *
     * @return transferOutDetailQty
     */
    public BigDecimal getTransferOutDetailQty() {
        return this.transferOutDetailQty;
    }

    /**
     * Set the transferOutDetailQty.
     *
     * @param transferOutDetailQty transferOutDetailQty
     */
    public void setTransferOutDetailQty(BigDecimal transferOutDetailQty) {
        this.transferOutDetailQty = transferOutDetailQty;
    }

    /**
     * Get the transferOutDetailCustomerCode.
     *
     * @return transferOutDetailCustomerCode
     */
    public String getTransferOutDetailCustomerCode() {
        return this.transferOutDetailCustomerCode;
    }

    /**
     * Set the transferOutDetailCustomerCode.
     *
     * @param transferOutDetailCustomerCode transferOutDetailCustomerCode
     */
    public void setTransferOutDetailCustomerCode(String transferOutDetailCustomerCode) {
        this.transferOutDetailCustomerCode = transferOutDetailCustomerCode;
    }

    /**
     * Get the shippingUuid.
     *
     * @return shippingUuid
     */
    public String getShippingUuid() {
        return this.shippingUuid;
    }

    /**
     * Set the shippingUuid.
     *
     * @param shippingUuid shippingUuid
     */
    public void setShippingUuid(String shippingUuid) {
        this.shippingUuid = shippingUuid;
    }

    /**
     * Get the kanbanShippingId.
     *
     * @return kanbanShippingId
     */
    public String getKanbanShippingId() {
        return this.kanbanShippingId;
    }

    /**
     * Set the kanbanShippingId.
     *
     * @param kanbanShippingId kanbanShippingId
     */
    public void setKanbanShippingId(String kanbanShippingId) {
        this.kanbanShippingId = kanbanShippingId;
    }

    /**
     * Get the kanbanPlanId.
     *
     * @return kanbanPlanId
     */
    public String getKanbanPlanId() {
        return this.kanbanPlanId;
    }

    /**
     * Set the kanbanPlanId.
     *
     * @param kanbanPlanId kanbanPlanId
     */
    public void setKanbanPlanId(String kanbanPlanId) {
        this.kanbanPlanId = kanbanPlanId;
    }

    /**
     * Get the planType.
     *
     * @return planType
     */
    public Integer getPlanType() {
        return this.planType;
    }

    /**
     * Set the planType.
     *
     * @param planType planType
     */
    public void setPlanType(Integer planType) {
        this.planType = planType;
    }

    /**
     * Get the issuedDate.
     *
     * @return issuedDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getIssuedDate() {
        return this.issuedDate;
    }

    /**
     * Set the issuedDate.
     *
     * @param issuedDate issuedDate
     */
    public void setIssuedDate(Timestamp issuedDate) {
        this.issuedDate = issuedDate;
    }

    /**
     * Get the deliveredDate.
     *
     * @return deliveredDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getDeliveredDate() {
        return this.deliveredDate;
    }

    /**
     * Set the deliveredDate.
     *
     * @param deliveredDate deliveredDate
     */
    public void setDeliveredDate(Timestamp deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    /**
     * Get the vanningDate.
     *
     * @return vanningDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getVanningDate() {
        return this.vanningDate;
    }

    /**
     * Set the vanningDate.
     *
     * @param vanningDate vanningDate
     */
    public void setVanningDate(Timestamp vanningDate) {
        this.vanningDate = vanningDate;
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
     * Get the invoiceGroupId.
     *
     * @return invoiceGroupId
     */
    public String getInvoiceGroupId() {
        return this.invoiceGroupId;
    }

    /**
     * Set the invoiceGroupId.
     *
     * @param invoiceGroupId invoiceGroupId
     */
    public void setInvoiceGroupId(String invoiceGroupId) {
        this.invoiceGroupId = invoiceGroupId;
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
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getEtd() {
        return this.etd;
    }

    /**
     * Set the etd.
     *
     * @param etd etd
     */
    public void setEtd(Timestamp etd) {
        this.etd = etd;
    }

    /**
     * Get the eta.
     *
     * @return eta
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getEta() {
        return this.eta;
    }

    /**
     * Set the eta.
     *
     * @param eta eta
     */
    public void setEta(Timestamp eta) {
        this.eta = eta;
    }

    /**
     * Get the impInbPlanDate.
     *
     * @return impInbPlanDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getImpInbPlanDate() {
        return this.impInbPlanDate;
    }

    /**
     * Set the impInbPlanDate.
     *
     * @param impInbPlanDate impInbPlanDate
     */
    public void setImpInbPlanDate(Timestamp impInbPlanDate) {
        this.impInbPlanDate = impInbPlanDate;
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
     * Get the originalVersion.
     *
     * @return originalVersion
     */
    public String getOriginalVersion() {
        return this.originalVersion;
    }

    /**
     * Set the originalVersion.
     *
     * @param originalVersion originalVersion
     */
    public void setOriginalVersion(String originalVersion) {
        this.originalVersion = originalVersion;
    }

    /**
     * Get the revisionVersion.
     *
     * @return revisionVersion
     */
    public String getRevisionVersion() {
        return this.revisionVersion;
    }

    /**
     * Set the revisionVersion.
     *
     * @param revisionVersion revisionVersion
     */
    public void setRevisionVersion(String revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    /**
     * Get the etdInvoice.
     *
     * @return etdInvoice
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getEtdInvoice() {
        return this.etdInvoice;
    }

    /**
     * Set the etdInvoice.
     *
     * @param etdInvoice etdInvoice
     */
    public void setEtdInvoice(Timestamp etdInvoice) {
        this.etdInvoice = etdInvoice;
    }

    /**
     * Get the etaInvoice.
     *
     * @return etaInvoice
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getEtaInvoice() {
        return this.etaInvoice;
    }

    /**
     * Set the etaInvoice.
     *
     * @param etaInvoice etaInvoice
     */
    public void setEtaInvoice(Timestamp etaInvoice) {
        this.etaInvoice = etaInvoice;
    }

    /**
     * Get the impInbPlanDateInvoice.
     *
     * @return impInbPlanDateInvoice
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getImpInbPlanDateInvoice() {
        return this.impInbPlanDateInvoice;
    }

    /**
     * Set the impInbPlanDateInvoice.
     *
     * @param impInbPlanDateInvoice impInbPlanDateInvoice
     */
    public void setImpInbPlanDateInvoice(Timestamp impInbPlanDateInvoice) {
        this.impInbPlanDateInvoice = impInbPlanDateInvoice;
    }

    /**
     * Get the impInbActualDateInvoice.
     *
     * @return impInbActualDateInvoice
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getImpInbActualDateInvoice() {
        return this.impInbActualDateInvoice;
    }

    /**
     * Set the impInbActualDateInvoice.
     *
     * @param impInbActualDateInvoice impInbActualDateInvoice
     */
    public void setImpInbActualDateInvoice(Timestamp impInbActualDateInvoice) {
        this.impInbActualDateInvoice = impInbActualDateInvoice;
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
     * Get the issueRemark.
     *
     * @return issueRemark
     */
    public String getIssueRemark() {
        return this.issueRemark;
    }

    /**
     * Set the issueRemark.
     *
     * @param issueRemark issueRemark
     */
    public void setIssueRemark(String issueRemark) {
        this.issueRemark = issueRemark;
    }

    /**
     * Get the delivereRemark.
     *
     * @return delivereRemark
     */
    public String getDelivereRemark() {
        return this.delivereRemark;
    }

    /**
     * Set the delivereRemark.
     *
     * @param delivereRemark delivereRemark
     */
    public void setDelivereRemark(String delivereRemark) {
        this.delivereRemark = delivereRemark;
    }

    /**
     * Get the vanningRemark.
     *
     * @return vanningRemark
     */
    public String getVanningRemark() {
        return this.vanningRemark;
    }

    /**
     * Set the vanningRemark.
     *
     * @param vanningRemark vanningRemark
     */
    public void setVanningRemark(String vanningRemark) {
        this.vanningRemark = vanningRemark;
    }

    /**
     * Get the qtyWithInvoice.
     *
     * @return qtyWithInvoice
     */
    public BigDecimal getQtyWithInvoice() {
        return this.qtyWithInvoice;
    }

    /**
     * Set the qtyWithInvoice.
     *
     * @param qtyWithInvoice qtyWithInvoice
     */
    public void setQtyWithInvoice(BigDecimal qtyWithInvoice) {
        this.qtyWithInvoice = qtyWithInvoice;
    }

    /**
     * Get the orderBalanceBasedOnActualEtd.
     *
     * @return orderBalanceBasedOnActualEtd
     */
    public BigDecimal getOrderBalanceBasedOnActualEtd() {
        return this.orderBalanceBasedOnActualEtd;
    }

    /**
     * Set the orderBalanceBasedOnActualEtd.
     *
     * @param orderBalanceBasedOnActualEtd orderBalanceBasedOnActualEtd
     */
    public void setOrderBalanceBasedOnActualEtd(BigDecimal orderBalanceBasedOnActualEtd) {
        this.orderBalanceBasedOnActualEtd = orderBalanceBasedOnActualEtd;
    }

    /**
     * Get the actualInboundQty.
     *
     * @return actualInboundQty
     */
    public BigDecimal getActualInboundQty() {
        return this.actualInboundQty;
    }

    /**
     * Set the actualInboundQty.
     *
     * @param actualInboundQty actualInboundQty
     */
    public void setActualInboundQty(BigDecimal actualInboundQty) {
        this.actualInboundQty = actualInboundQty;
    }

    /**
     * Get the orderBalanceBasedOnActualInbound.
     *
     * @return orderBalanceBasedOnActualInbound
     */
    public BigDecimal getOrderBalanceBasedOnActualInbound() {
        return this.orderBalanceBasedOnActualInbound;
    }

    /**
     * Set the orderBalanceBasedOnActualInbound.
     *
     * @param orderBalanceBasedOnActualInbound orderBalanceBasedOnActualInbound
     */
    public void setOrderBalanceBasedOnActualInbound(BigDecimal orderBalanceBasedOnActualInbound) {
        this.orderBalanceBasedOnActualInbound = orderBalanceBasedOnActualInbound;
    }

    /**
     * Get the status.
     *
     * @return status
     */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * Set the status.
     *
     * @param status status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Get the stockTransferOutDetails.
     *
     * @return stockTransferOutDetails
     */
    public String getStockTransferOutDetails() {
        return this.stockTransferOutDetails;
    }

    /**
     * Set the stockTransferOutDetails.
     *
     * @param stockTransferOutDetails stockTransferOutDetails
     */
    public void setStockTransferOutDetails(String stockTransferOutDetails) {
        this.stockTransferOutDetails = stockTransferOutDetails;
    }

    /**
     * Get the firstInviceGroupBoxDate.
     *
     * @return firstInviceGroupBoxDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getFirstInviceGroupBoxDate() {
        return this.firstInviceGroupBoxDate;
    }

    /**
     * Set the firstInviceGroupBoxDate.
     *
     * @param firstInviceGroupBoxDate firstInviceGroupBoxDate
     */
    public void setFirstInviceGroupBoxDate(Timestamp firstInviceGroupBoxDate) {
        this.firstInviceGroupBoxDate = firstInviceGroupBoxDate;
    }

    /**
     * Get the lastShippingPlanBoxDate.
     *
     * @return lastShippingPlanBoxDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getLastShippingPlanBoxDate() {
        return this.lastShippingPlanBoxDate;
    }

    /**
     * Set the lastShippingPlanBoxDate.
     *
     * @param lastShippingPlanBoxDate lastShippingPlanBoxDate
     */
    public void setLastShippingPlanBoxDate(Timestamp lastShippingPlanBoxDate) {
        this.lastShippingPlanBoxDate = lastShippingPlanBoxDate;
    }

    /**
     * Get the impStockFlag.
     *
     * @return impStockFlag
     */
    public Integer getImpStockFlag() {
        return this.impStockFlag;
    }

    /**
     * Set the impStockFlag.
     *
     * @param impStockFlag impStockFlag
     */
    public void setImpStockFlag(Integer impStockFlag) {
        this.impStockFlag = impStockFlag;
    }
}
