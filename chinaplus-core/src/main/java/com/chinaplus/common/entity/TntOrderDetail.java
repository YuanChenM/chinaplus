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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the TNT_ORDER_DETAIL database table.
 * 
 */
@Entity(name = "TNT_ORDER_DETAIL")
public class TntOrderDetail extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_ORDER_DETAIL_ORDERDETAILID_GENERATOR",
        sequenceName = "SEQ_TNT_ORDER_DETAIL",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_ORDER_DETAIL_ORDERDETAILID_GENERATOR")
    @Column(name = "ORDER_DETAIL_ID")
    private Integer orderDetailId;

    @Column(name = "ORDER_ID")
    private Integer orderId;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "EXP_INB_PLAN_DATE")
    private Date expInbPlanDate;

    @Column(name = "EXP_PARTS_ID")
    private Integer expPartsId;

    @Temporal(TemporalType.DATE)
    @Column(name = "EXP_PO_DATE")
    private Date expPoDate;

    @Column(name = "EXP_PO_ITEM_NO")
    private String expPoItemNo;

    @Column(name = "EXP_PO_NO")
    private String expPoNo;

    @Column(name = "EXP_REGION")
    private String expRegion;

    @Column(name = "FORCE_COMPLETED_BY")
    private Integer forceCompletedBy;

    @Column(name = "FORCE_COMPLETED_DATE")
    private Timestamp forceCompletedDate;

    @Column(name = "ORDER_LOT")
    private BigDecimal orderLot;

    @Column(name = "ORDER_QTY")
    private BigDecimal orderQty;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    private BigDecimal spq;

    @Column(name = "\"STATUS\"")
    private Integer status;

    @Column(name = "TRANSPORT_MODE")
    private Integer transportMode;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    public TntOrderDetail() {}

    public Integer getOrderDetailId() {
        return this.orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public Date getExpInbPlanDate() {
        return this.expInbPlanDate;
    }

    public void setExpInbPlanDate(Date expInbPlanDate) {
        this.expInbPlanDate = expInbPlanDate;
    }

    public Integer getExpPartsId() {
        return this.expPartsId;
    }

    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;
    }

    public Date getExpPoDate() {
        return this.expPoDate;
    }

    public void setExpPoDate(Date expPoDate) {
        this.expPoDate = expPoDate;
    }

    public String getExpPoItemNo() {
        return this.expPoItemNo;
    }

    public void setExpPoItemNo(String expPoItemNo) {
        this.expPoItemNo = expPoItemNo;
    }

    public String getExpPoNo() {
        return this.expPoNo;
    }

    public void setExpPoNo(String expPoNo) {
        this.expPoNo = expPoNo;
    }

    public String getExpRegion() {
        return this.expRegion;
    }

    public void setExpRegion(String expRegion) {
        this.expRegion = expRegion;
    }

    public Integer getForceCompletedBy() {
        return this.forceCompletedBy;
    }

    public void setForceCompletedBy(Integer forceCompletedBy) {
        this.forceCompletedBy = forceCompletedBy;
    }

    public Timestamp getForceCompletedDate() {
        return this.forceCompletedDate;
    }

    public void setForceCompletedDate(Timestamp forceCompletedDate) {
        this.forceCompletedDate = forceCompletedDate;
    }

    public BigDecimal getOrderLot() {
        return this.orderLot;
    }

    public void setOrderLot(BigDecimal orderLot) {
        this.orderLot = orderLot;
    }

    public BigDecimal getOrderQty() {
        return this.orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public BigDecimal getSpq() {
        return this.spq;
    }

    public void setSpq(BigDecimal spq) {
        this.spq = spq;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

}