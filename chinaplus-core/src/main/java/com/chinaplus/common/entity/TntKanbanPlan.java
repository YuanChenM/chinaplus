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
 * The persistent class for the TNT_KANBAN_PLAN database table.
 * 
 */
@Entity(name = "TNT_KANBAN_PLAN")
public class TntKanbanPlan extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_KANBAN_PLAN_KANBANPLANID_GENERATOR",
        sequenceName = "SEQ_TNT_KANBAN_PLAN",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_KANBAN_PLAN_KANBANPLANID_GENERATOR")
    @Column(name = "KANBAN_PLAN_ID")
    private Integer kanbanPlanId;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "DELIVERE_REMARK")
    private String delivereRemark;

    @Temporal(TemporalType.DATE)
    @Column(name = "DELIVERED_DATE")
    private Date deliveredDate;

    @Column(name = "ISSUE_REMARK")
    private String issueRemark;

    @Temporal(TemporalType.DATE)
    @Column(name = "ISSUED_DATE")
    private Date issuedDate;

    @Column(name = "ORDER_MONTH")
    private String orderMonth;

    @Column(name = "PLAN_TYPE")
    private Integer planType;

    @Column(name = "REVISION_REASON")
    private String revisionReason;

    @Column(name = "SHIPPING_UUID")
    private String shippingUuid;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "VANNING_DATE")
    private Date vanningDate;

    @Column(name = "VANNING_REMARK")
    private String vanningRemark;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "KANBAN_ID")
    private Integer kanbanId;

    public TntKanbanPlan() {}

    public Integer getKanbanPlanId() {
        return this.kanbanPlanId;
    }

    public void setKanbanPlanId(Integer kanbanPlanId) {
        this.kanbanPlanId = kanbanPlanId;
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

    public String getDelivereRemark() {
        return this.delivereRemark;
    }

    public void setDelivereRemark(String delivereRemark) {
        this.delivereRemark = delivereRemark;
    }

    public Date getDeliveredDate() {
        return this.deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getIssueRemark() {
        return this.issueRemark;
    }

    public void setIssueRemark(String issueRemark) {
        this.issueRemark = issueRemark;
    }

    public Date getIssuedDate() {
        return this.issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getOrderMonth() {
        return this.orderMonth;
    }

    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    public Integer getPlanType() {
        return this.planType;
    }

    public void setPlanType(Integer planType) {
        this.planType = planType;
    }

    public String getRevisionReason() {
        return this.revisionReason;
    }

    public void setRevisionReason(String revisionReason) {
        this.revisionReason = revisionReason;
    }

    public String getShippingUuid() {
        return this.shippingUuid;
    }

    public void setShippingUuid(String shippingUuid) {
        this.shippingUuid = shippingUuid;
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

    public String getVanningRemark() {
        return this.vanningRemark;
    }

    public void setVanningRemark(String vanningRemark) {
        this.vanningRemark = vanningRemark;
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