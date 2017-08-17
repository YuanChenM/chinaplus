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

/**
 * The persistent class for the TNT_RUNDOWN_CFC database table.
 * 
 */
@Entity(name = "TNT_RUNDOWN_CFC")
public class TntRundownCfc extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_RUNDOWN_CFC_RUNDOWN_CFC_ID_GENERATOR",
        sequenceName = "SEQ_TNT_RUNDOWN_CFC",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_RUNDOWN_CFC_RUNDOWN_CFC_ID_GENERATOR")
    @Column(name = "RUNDOWN_CFC_ID")
    private Integer rundownCfcId;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    @Column(name = "CFC_DATE")
    private Date cfcDate;

    @Column(name = "ORIGINAL_QTY")
    private BigDecimal originalQty;

    @Column(name = "CFC_QTY")
    private BigDecimal cfcQty;

    @Column(name = "DELIVERED_QTY")
    private BigDecimal deliveredQty;

    @Column(name = "CFC_ADJUSTMENT_TYPE")
    private Integer cfcAdjustmentType;
    
    @Column(name="CREATED_BY")
    private Integer createdBy;

    @Column(name="CREATED_DATE")
    private Timestamp createdDate;
    
    @Column(name="UPDATED_BY")
    private Integer updatedBy;

    @Column(name="UPDATED_DATE")
    private Timestamp updatedDate;
    
    @Column(name = "\"VERSION\"")
    private Integer version;

    /**
     * Get the rundownCfcId.
     *
     * @return rundownCfcId
     */
    public Integer getRundownCfcId() {
        return this.rundownCfcId;
    }

    /**
     * Set the rundownCfcId.
     *
     * @param rundownCfcId rundownCfcId
     */
    public void setRundownCfcId(Integer rundownCfcId) {
        this.rundownCfcId = rundownCfcId;
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
     * Get the cfcDate.
     *
     * @return cfcDate
     */
    public Date getCfcDate() {
        return this.cfcDate;
    }

    /**
     * Set the cfcDate.
     *
     * @param cfcDate cfcDate
     */
    public void setCfcDate(Date cfcDate) {
        this.cfcDate = cfcDate;
    }

    /**
     * Get the originalQty.
     *
     * @return originalQty
     */
    public BigDecimal getOriginalQty() {
        return this.originalQty;
    }

    /**
     * Set the originalQty.
     *
     * @param originalQty originalQty
     */
    public void setOriginalQty(BigDecimal originalQty) {
        this.originalQty = originalQty;
    }

    /**
     * Get the cfcQty.
     *
     * @return cfcQty
     */
    public BigDecimal getCfcQty() {
        return this.cfcQty;
    }

    /**
     * Set the cfcQty.
     *
     * @param cfcQty cfcQty
     */
    public void setCfcQty(BigDecimal cfcQty) {
        this.cfcQty = cfcQty;
    }

    /**
     * Get the deliveredQty.
     *
     * @return deliveredQty
     */
    public BigDecimal getDeliveredQty() {
        return this.deliveredQty;
    }

    /**
     * Set the deliveredQty.
     *
     * @param deliveredQty deliveredQty
     */
    public void setDeliveredQty(BigDecimal deliveredQty) {
        this.deliveredQty = deliveredQty;
    }

    /**
     * Get the cfcAdjustmentType.
     *
     * @return cfcAdjustmentType
     */
    public Integer getCfcAdjustmentType() {
        return this.cfcAdjustmentType;
    }

    /**
     * Set the cfcAdjustmentType.
     *
     * @param cfcAdjustmentType cfcAdjustmentType
     */
    public void setCfcAdjustmentType(Integer cfcAdjustmentType) {
        this.cfcAdjustmentType = cfcAdjustmentType;
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