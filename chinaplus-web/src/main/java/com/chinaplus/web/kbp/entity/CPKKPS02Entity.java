/**
 * CPKKPS02Entity.java
 * 
 * @screen CPKKPS02
 * @author shiyang
 */
package com.chinaplus.web.kbp.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.common.util.JsonMonthSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Kanban Plan Detailed Information Screen Entity.
 */
public class CPKKPS02Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Kanban Id */
    private Integer kanbanId;

    /** Kanban Id (Upload History) */
    private Integer kanbanIdUpload;

    /** Kanban Plan No. */
    private String kanbanPlanNo;

    /** Kanban Plan No. for Display */
    private String kanbanPlanNoDisplay;

    /** Revision Version */
    private Integer revisionVersion;

    /** Order Month */
    private String orderMonth;

    /** TTC Customer Code */
    private String customerCode;

    /** TTC Supplier Code */
    private String supplierCode;

    /** (PartsInfo) Kanban Parts Id */
    private Integer kanbanPartsId;

    /** (PartsInfo) Parts Id */
    private Integer partsId;

    /** (PartsInfo) TTC Parts No. */
    private String ttcPartsNo;

    /** (PartsInfo) Order Qty */
    private BigDecimal partsOrderQty;

    /** (PartsInfo) On Shipping Qty */
    private BigDecimal partsOnShippingQty;

    /** (PartsInfo) Inbound Qty */
    private BigDecimal partsInboundQty;

    /** (PartsInfo) Inbound Qty */
    private BigDecimal partsOutboundQty;

    /** (PartsInfo) Order Balance */
    private BigDecimal partsOrderBalance;

    /** (PartsInfo) Status */
    private Integer partsStatus;

    /** (PartsInfo) Decimal Digits */
    private Integer decimalDigits;

    /** Transport Mode */
    private String transportMode;

    /** Sea Flag */
    private Integer seaFlag;

    /** Air Flag */
    private Integer airFlag;

    /** Order Qty */
    private BigDecimal orderQty;

    /** On Shipping Qty */
    private BigDecimal onShippingQty;

    /** Inbound Qty */
    private BigDecimal inboundQty;

    /** Order Balance */
    private BigDecimal orderBalance;

    /** Total Order Qty */
    private BigDecimal totalOrderQty;

    /** Total On Shipping Qty */
    private BigDecimal totalOnShippingQty;

    /** Total Inbound Qty */
    private BigDecimal totalInboundQty;

    /** Total Order Balance */
    private BigDecimal totalBalanceQty;

    /** Upload File Type */
    private Integer uploadFileType;

    /** Revision Reason */
    private String revisionReason;

    /** Status */
    private Integer status;

    /** Upload ID */
    private String uploadId;

    /** Last Upload Time */
    private Timestamp lastUploadTime;

    /** Updated By */
    private Integer updatedBy;

    /** Updated Date */
    private Timestamp updatedDate;

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
     * Get the kanbanIdUpload.
     *
     * @return kanbanIdUpload
     */
    public Integer getKanbanIdUpload() {
        return this.kanbanIdUpload;
    }

    /**
     * Set the kanbanIdUpload.
     *
     * @param kanbanIdUpload kanbanIdUpload
     */
    public void setKanbanIdUpload(Integer kanbanIdUpload) {
        this.kanbanIdUpload = kanbanIdUpload;
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
     * Get the kanbanPlanNoDisplay.
     *
     * @return kanbanPlanNoDisplay
     */
    public String getKanbanPlanNoDisplay() {
        return this.kanbanPlanNoDisplay;
    }

    /**
     * Set the kanbanPlanNoDisplay.
     *
     * @param kanbanPlanNoDisplay kanbanPlanNoDisplay
     */
    public void setKanbanPlanNoDisplay(String kanbanPlanNoDisplay) {
        this.kanbanPlanNoDisplay = kanbanPlanNoDisplay;
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
     * Get the orderMonth.
     *
     * @return orderMonth
     */
    @JsonSerialize(using = JsonMonthSerializer.class)
    public String getOrderMonth() {
        return this.orderMonth;
    }

    /**
     * Set the orderMonth.
     *
     * @param orderMonth orderMonth
     */
    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
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
     * Get the supplierCode.
     *
     * @return supplierCode
     */
    public String getSupplierCode() {
        return this.supplierCode;
    }

    /**
     * Set the supplierCode.
     *
     * @param supplierCode supplierCode
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * Get the kanbanPartsId.
     *
     * @return kanbanPartsId
     */
    public Integer getKanbanPartsId() {
        return this.kanbanPartsId;
    }

    /**
     * Set the kanbanPartsId.
     *
     * @param kanbanPartsId kanbanPartsId
     */
    public void setKanbanPartsId(Integer kanbanPartsId) {
        this.kanbanPartsId = kanbanPartsId;
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
     * Get the partsOrderQty.
     *
     * @return partsOrderQty
     */
    public BigDecimal getPartsOrderQty() {
        return this.partsOrderQty;
    }

    /**
     * Set the partsOrderQty.
     *
     * @param partsOrderQty partsOrderQty
     */
    public void setPartsOrderQty(BigDecimal partsOrderQty) {
        this.partsOrderQty = partsOrderQty;
    }

    /**
     * Get the partsOnShippingQty.
     *
     * @return partsOnShippingQty
     */
    public BigDecimal getPartsOnShippingQty() {
        return this.partsOnShippingQty;
    }

    /**
     * Set the partsOnShippingQty.
     *
     * @param partsOnShippingQty partsOnShippingQty
     */
    public void setPartsOnShippingQty(BigDecimal partsOnShippingQty) {
        this.partsOnShippingQty = partsOnShippingQty;
    }

    /**
     * Get the partsInboundQty.
     *
     * @return partsInboundQty
     */
    public BigDecimal getPartsInboundQty() {
        return this.partsInboundQty;
    }

    /**
     * Set the partsInboundQty.
     *
     * @param partsInboundQty partsInboundQty
     */
    public void setPartsInboundQty(BigDecimal partsInboundQty) {
        this.partsInboundQty = partsInboundQty;
    }

    /**
     * Get the partsOutboundQty.
     *
     * @return partsOutboundQty
     */
    public BigDecimal getPartsOutboundQty() {
        return this.partsOutboundQty;
    }

    /**
     * Set the partsOutboundQty.
     *
     * @param partsOutboundQty partsOutboundQty
     */
    public void setPartsOutboundQty(BigDecimal partsOutboundQty) {
        this.partsOutboundQty = partsOutboundQty;
    }

    /**
     * Get the partsOrderBalance.
     *
     * @return partsOrderBalance
     */
    public BigDecimal getPartsOrderBalance() {
        return this.partsOrderBalance;
    }

    /**
     * Set the partsOrderBalance.
     *
     * @param partsOrderBalance partsOrderBalance
     */
    public void setPartsOrderBalance(BigDecimal partsOrderBalance) {
        this.partsOrderBalance = partsOrderBalance;
    }

    /**
     * Get the partsStatus.
     *
     * @return partsStatus
     */
    public Integer getPartsStatus() {
        return this.partsStatus;
    }

    /**
     * Set the partsStatus.
     *
     * @param partsStatus partsStatus
     */
    public void setPartsStatus(Integer partsStatus) {
        this.partsStatus = partsStatus;
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
     * Get the seaFlag.
     *
     * @return seaFlag
     */
    public Integer getSeaFlag() {
        return this.seaFlag;
    }

    /**
     * Set the seaFlag.
     *
     * @param seaFlag seaFlag
     */
    public void setSeaFlag(Integer seaFlag) {
        this.seaFlag = seaFlag;
    }

    /**
     * Get the airFlag.
     *
     * @return airFlag
     */
    public Integer getAirFlag() {
        return this.airFlag;
    }

    /**
     * Set the airFlag.
     *
     * @param airFlag airFlag
     */
    public void setAirFlag(Integer airFlag) {
        this.airFlag = airFlag;
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
     * Get the onShippingQty.
     *
     * @return onShippingQty
     */
    public BigDecimal getOnShippingQty() {
        return this.onShippingQty;
    }

    /**
     * Set the onShippingQty.
     *
     * @param onShippingQty onShippingQty
     */
    public void setOnShippingQty(BigDecimal onShippingQty) {
        this.onShippingQty = onShippingQty;
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
     * Get the orderBalance.
     *
     * @return orderBalance
     */
    public BigDecimal getOrderBalance() {
        return this.orderBalance;
    }

    /**
     * Set the orderBalance.
     *
     * @param orderBalance orderBalance
     */
    public void setOrderBalance(BigDecimal orderBalance) {
        this.orderBalance = orderBalance;
    }

    /**
     * Get the totalOrderQty.
     *
     * @return totalOrderQty
     */
    public BigDecimal getTotalOrderQty() {
        return this.totalOrderQty;
    }

    /**
     * Set the totalOrderQty.
     *
     * @param totalOrderQty totalOrderQty
     */
    public void setTotalOrderQty(BigDecimal totalOrderQty) {
        this.totalOrderQty = totalOrderQty;
    }

    /**
     * Get the totalOnShippingQty.
     *
     * @return totalOnShippingQty
     */
    public BigDecimal getTotalOnShippingQty() {
        return this.totalOnShippingQty;
    }

    /**
     * Set the totalOnShippingQty.
     *
     * @param totalOnShippingQty totalOnShippingQty
     */
    public void setTotalOnShippingQty(BigDecimal totalOnShippingQty) {
        this.totalOnShippingQty = totalOnShippingQty;
    }

    /**
     * Get the totalInboundQty.
     *
     * @return totalInboundQty
     */
    public BigDecimal getTotalInboundQty() {
        return this.totalInboundQty;
    }

    /**
     * Set the totalInboundQty.
     *
     * @param totalInboundQty totalInboundQty
     */
    public void setTotalInboundQty(BigDecimal totalInboundQty) {
        this.totalInboundQty = totalInboundQty;
    }

    /**
     * Get the totalBalanceQty.
     *
     * @return totalBalanceQty
     */
    public BigDecimal getTotalBalanceQty() {
        return this.totalBalanceQty;
    }

    /**
     * Set the totalBalanceQty.
     *
     * @param totalBalanceQty totalBalanceQty
     */
    public void setTotalBalanceQty(BigDecimal totalBalanceQty) {
        this.totalBalanceQty = totalBalanceQty;
    }

    /**
     * Get the uploadFileType.
     *
     * @return uploadFileType
     */
    public Integer getUploadFileType() {
        return this.uploadFileType;
    }

    /**
     * Set the uploadFileType.
     *
     * @param uploadFileType uploadFileType
     */
    public void setUploadFileType(Integer uploadFileType) {
        this.uploadFileType = uploadFileType;
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
     * Get the lastUploadTime.
     *
     * @return lastUploadTime
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getLastUploadTime() {
        return this.lastUploadTime;
    }

    /**
     * Set the lastUploadTime.
     *
     * @param lastUploadTime lastUploadTime
     */
    public void setLastUploadTime(Timestamp lastUploadTime) {
        this.lastUploadTime = lastUploadTime;
    }

    /**
     * Get the updatedBy.
     *
     * @return updatedBy
     */
    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Set the updatedBy.
     *
     * @param updatedBy updatedBy
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the updatedDate.
     *
     * @return updatedDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    /**
     * Set the updatedDate.
     *
     * @param updatedDate updatedDate
     */
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
