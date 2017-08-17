/**
 * DownloadZipEntity.java
 * 
 * @screen CPKKPF01&CPKKPF02
 * @author shiyang
 */
package com.chinaplus.web.kbp.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.chinaplus.common.bean.SupplyChainEntity;
import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Download Latest Kanban Plan File(doc1) & Kanban Plan for Revision History(doc2) Entity.
 */
public class DownloadZipEntity extends SupplyChainEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Kanban Id */
    private String kanbanId;

    /** KSP Id */
    private int kspId;

    /** KB Plan Parts Id */
    private int kbPlanPartsId;

    // /** Parts Id */
    // private Integer partsId;

    /** QTY */
    private BigDecimal qty;

    /** NIRD QTY */
    private BigDecimal nirdQty;

    /** Shipping UUID */
    private String shippingUuid;

    /** Kanban Shipping Id */
    private String kanbanShippingId;

    /** Kanban Plan Id */
    private String kanbanPlanId;

    /** Max ETD */
    private Timestamp maxEtd;

    /** New ETD */
    private Timestamp newEtd;

    /** Created By */
    private Integer createdBy;

    /** Created Date */
    private Timestamp createdDate;

    /** Updated By */
    private Integer updatedBy;

    /** Updated Date */
    private Timestamp updatedDate;

    /** Revison Reason */
    private String revisonReason;

    /**
     * Get the kanbanId.
     *
     * @return kanbanId
     */
    public String getKanbanId() {
        return this.kanbanId;
    }

    /**
     * Set the kanbanId.
     *
     * @param kanbanId kanbanId
     */
    public void setKanbanId(String kanbanId) {
        this.kanbanId = kanbanId;
    }

    /**
     * Get the kspId.
     *
     * @return kspId
     */
    public int getKspId() {
        return this.kspId;
    }

    /**
     * Set the kspId.
     *
     * @param kspId kspId
     */
    public void setKspId(int kspId) {
        this.kspId = kspId;
    }

    /**
     * Get the kbPlanPartsId.
     *
     * @return kbPlanPartsId
     */
    public int getKbPlanPartsId() {
        return this.kbPlanPartsId;
    }

    /**
     * Set the kbPlanPartsId.
     *
     * @param kbPlanPartsId kbPlanPartsId
     */
    public void setKbPlanPartsId(int kbPlanPartsId) {
        this.kbPlanPartsId = kbPlanPartsId;
    }

    // /**
    // * Get the partsId.
    // *
    // * @return partsId
    // */
    // public Integer getPartsId() {
    // return this.partsId;
    // }
    //
    // /**
    // * Set the partsId.
    // *
    // * @param partsId partsId
    // */
    // public void setPartsId(Integer partsId) {
    // this.partsId = partsId;
    // }

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
     * Get the nirdQty.
     *
     * @return nirdQty
     */
    public BigDecimal getNirdQty() {
        return this.nirdQty;
    }

    /**
     * Set the nirdQty.
     *
     * @param nirdQty nirdQty
     */
    public void setNirdQty(BigDecimal nirdQty) {
        this.nirdQty = nirdQty;
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
     * Get the maxEtd.
     *
     * @return maxEtd
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getMaxEtd() {
        return this.maxEtd;
    }

    /**
     * Set the maxEtd.
     *
     * @param maxEtd maxEtd
     */
    public void setMaxEtd(Timestamp maxEtd) {
        this.maxEtd = maxEtd;
    }

    /**
     * Get the newEtd.
     *
     * @return newEtd
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getNewEtd() {
        return this.newEtd;
    }

    /**
     * Set the newEtd.
     *
     * @param newEtd newEtd
     */
    public void setNewEtd(Timestamp newEtd) {
        this.newEtd = newEtd;
    }

    /**
     * Get the createdBy.
     *
     * @return createdBy
     */
    public Integer getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Set the createdBy.
     *
     * @param createdBy createdBy
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the createdDate.
     *
     * @return createdDate
     */
    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    /**
     * Set the createdDate.
     *
     * @param createdDate createdDate
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
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

    /**
     * Get the revisonReason.
     *
     * @return revisonReason
     */
    public String getRevisonReason() {
        return this.revisonReason;
    }

    /**
     * Set the revisonReason.
     *
     * @param revisonReason revisonReason
     */
    public void setRevisonReason(String revisonReason) {
        this.revisonReason = revisonReason;
    }
}
