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
 * The persistent class for the TNT_SS_PARTS database table.
 * 
 */
@Entity(name = "TNT_SS_PARTS")
public class TntSsPart extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_SS_PARTS_SSPARTSID_GENERATOR",
        sequenceName = "SEQ_TNT_SS_PARTS",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_SS_PARTS_SSPARTSID_GENERATOR")
    @Column(name = "SS_PARTS_ID")
    private Integer ssPartsId;

    @Column(name = "ORDER_STATUS_ID")
    private Integer orderStatusId;

    @Column(name = "SS_PLAN_ID")
    private Integer ssPlanId;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    private BigDecimal qty;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    public TntSsPart() {}

    public Integer getSsPartsId() {
        return this.ssPartsId;
    }

    public void setSsPartsId(Integer ssPartsId) {
        this.ssPartsId = ssPartsId;
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

    public Integer getOrderStatusId() {
        return this.orderStatusId;
    }

    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public Integer getSsPlanId() {
        return this.ssPlanId;
    }

    public void setSsPlanId(Integer ssPlanId) {
        this.ssPlanId = ssPlanId;
    }

}