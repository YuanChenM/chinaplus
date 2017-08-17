/**
 * CPKKPF12HistoryCompletedEntity.java
 * 
 * @screen CPKKPF12
 * @author shiyang
 */
package com.chinaplus.web.kbp.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Revised Kanban Plan Upload Entity.
 */
public class CPKKPF12HistoryCompletedEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Kanban Id */
    private Integer kanbanId;

    /** Shipping Uuid */
    private String shippingUuid;

    /** Transport Mode */
    private int transportMode;

    /** ETD */
    private Timestamp etd;

    /** ETA */
    private Timestamp eta;

    /** Imp Inb Plan Date */
    private Timestamp impInbPlanDate;

    /** Original Version */
    private int originalVersion;

    /** Revision Version */
    private String revisionVersion;

    /** Revision Reason */
    private String revisionReason;

    /** Completed Flag */
    private int completedFlag;

    /** Parts ID */
    private int partsId;

    /** Qty */
    private BigDecimal qty;

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
     * Get the transportMode.
     *
     * @return transportMode
     */
    public int getTransportMode() {
        return this.transportMode;
    }

    /**
     * Set the transportMode.
     *
     * @param transportMode transportMode
     */
    public void setTransportMode(int transportMode) {
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
     * Get the originalVersion.
     *
     * @return originalVersion
     */
    public int getOriginalVersion() {
        return this.originalVersion;
    }

    /**
     * Set the originalVersion.
     *
     * @param originalVersion originalVersion
     */
    public void setOriginalVersion(int originalVersion) {
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
     * Get the completedFlag.
     *
     * @return completedFlag
     */
    public int getCompletedFlag() {
        return this.completedFlag;
    }

    /**
     * Set the completedFlag.
     *
     * @param completedFlag completedFlag
     */
    public void setCompletedFlag(int completedFlag) {
        this.completedFlag = completedFlag;
    }

    /**
     * Get the partsId.
     *
     * @return partsId
     */
    public int getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     */
    public void setPartsId(int partsId) {
        this.partsId = partsId;
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
}
