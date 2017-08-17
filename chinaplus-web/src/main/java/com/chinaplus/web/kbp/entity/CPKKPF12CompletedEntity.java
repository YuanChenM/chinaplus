/**
 * CPKKPF12CompletedEntity.java
 * 
 * @screen CPKKPF12
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
 * Revised Kanban Plan Upload Entity.
 */
public class CPKKPF12CompletedEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Kanban Id */
    private Integer kanbanId;

    /** Shipping UUID */
    private String shippingUuid;

    /** Order Month */
    private String orderMonth;

    /** Plan Type */
    private int planType;

    /** Issued Date */
    private Timestamp issuedDate;

    /** Issue Remark */
    private String issueRemark;

    /** Delivered Date */
    private Timestamp deliveredDate;

    /** Delivere Remark */
    private String delivereRemark;

    /** Vanning Date */
    private Timestamp vanningDate;

    /** Vanning Remark */
    private String vanningRemark;

    /** Revision Reason */
    private String revisionReason;

    /** Parts Id */
    private int partsId;

    /** QTY */
    private BigDecimal qty;

    /** Kanban QTY */
    private BigDecimal kanbanQty;

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
     * Get the planType.
     *
     * @return planType
     */
    public int getPlanType() {
        return this.planType;
    }

    /**
     * Set the planType.
     *
     * @param planType planType
     */
    public void setPlanType(int planType) {
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
}
