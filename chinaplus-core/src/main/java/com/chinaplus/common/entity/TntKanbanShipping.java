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
 * The persistent class for the TNT_KANBAN_SHIPPING database table.
 * 
 */
@Entity(name = "TNT_KANBAN_SHIPPING")
public class TntKanbanShipping extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_KANBAN_SHIPPING_KANBANSHIPPINGID_GENERATOR",
        sequenceName = "SEQ_TNT_KANBAN_SHIPPING",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_KANBAN_SHIPPING_KANBANSHIPPINGID_GENERATOR")
    @Column(name = "KANBAN_SHIPPING_ID")
    private Integer kanbanShippingId;

    @Column(name = "COMPLETED_FLAG")
    private Integer completedFlag;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Temporal(TemporalType.DATE)
    private Date eta;

    @Temporal(TemporalType.DATE)
    private Date etd;

    @Temporal(TemporalType.DATE)
    @Column(name = "IMP_INB_PLAN_DATE")
    private Date impInbPlanDate;

    @Column(name = "NIRD_FLAG")
    private Integer nirdFlag;

    @Column(name = "ORIGINAL_VERSION")
    private Integer originalVersion;

    @Column(name = "REVISION_REASON")
    private String revisionReason;

    @Column(name = "REVISION_VERSION")
    private Integer revisionVersion;

    @Column(name = "SHIPPING_UUID")
    private String shippingUuid;

    @Column(name = "TRANSPORT_MODE")
    private Integer transportMode;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "KANBAN_ID")
    private Integer kanbanId;

    public TntKanbanShipping() {}

    public Integer getKanbanShippingId() {
        return this.kanbanShippingId;
    }

    public void setKanbanShippingId(Integer kanbanShippingId) {
        this.kanbanShippingId = kanbanShippingId;
    }

    public Integer getCompletedFlag() {
        return this.completedFlag;
    }

    public void setCompletedFlag(Integer completedFlag) {
        this.completedFlag = completedFlag;
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

    public Date getImpInbPlanDate() {
        return this.impInbPlanDate;
    }

    public void setImpInbPlanDate(Date impInbPlanDate) {
        this.impInbPlanDate = impInbPlanDate;
    }

    public Integer getNirdFlag() {
        return this.nirdFlag;
    }

    public void setNirdFlag(Integer nirdFlag) {
        this.nirdFlag = nirdFlag;
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

    public String getShippingUuid() {
        return this.shippingUuid;
    }

    public void setShippingUuid(String shippingUuid) {
        this.shippingUuid = shippingUuid;
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

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getKanbanId() {
        return this.kanbanId;
    }

    public void setKanbanId(Integer kanbanId) {
        this.kanbanId = kanbanId;
    }

}