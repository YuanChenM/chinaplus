/**
 * CPKKPS01Entity.java
 * 
 * @screen CPKKPS01
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
 * Kanban Issued Plan Screen Entity.
 */
public class CPKKPS01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Kanban Id */
    private Integer kanbanId;

    /** Kanban Plan No. */
    private String kanbanPlanNo;

    /** Kanban Plan No. for Display */
    private String kanbanPlanNoDisplay;

    /** Order Month */
    private String orderMonth;

    /** TTC Customer Code */
    private String ttcCustomerCode;

    /** TTC Supplier Code */
    private String ttcSupplierCode;

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

    /** Status */
    private Integer status;

    /** Upload ID */
    private String uploadId;

    /** Last Upload Time */
    private Timestamp lastUploadTime;

    /** Version */
    private Integer version;

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
}
