package com.chinaplus.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * The persistent class for the TNT_KANBAN_PLAN_PARTS database table.
 * 
 */
@Entity(name = "TNT_KANBAN_PLAN_PARTS")
public class TntKanbanPlanPart extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_KANBAN_PLAN_PARTS_KBPLANPARTSID_GENERATOR",
        sequenceName = "SEQ_TNT_KANBAN_PLAN_PARTS",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_KANBAN_PLAN_PARTS_KBPLANPARTSID_GENERATOR")
    @Column(name = "KB_PLAN_PARTS_ID")
    private Integer kbPlanPartsId;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "KANBAN_QTY")
    private BigDecimal kanbanQty;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    private BigDecimal qty;

    private BigDecimal spq;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "KANBAN_PLAN_ID")
    private Integer kanbanPlanId;

    public TntKanbanPlanPart() {}

    public Integer getKbPlanPartsId() {
        return this.kbPlanPartsId;
    }

    public void setKbPlanPartsId(Integer kbPlanPartsId) {
        this.kbPlanPartsId = kbPlanPartsId;
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

    public BigDecimal getKanbanQty() {
        return this.kanbanQty;
    }

    public void setKanbanQty(BigDecimal kanbanQty) {
        this.kanbanQty = kanbanQty;
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getSpq() {
        return this.spq;
    }

    public void setSpq(BigDecimal spq) {
        this.spq = spq;
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

    public Integer getKanbanPlanId() {
        return this.kanbanPlanId;
    }

    public void setKanbanPlanId(Integer kanbanPlanId) {
        this.kanbanPlanId = kanbanPlanId;
    }

}