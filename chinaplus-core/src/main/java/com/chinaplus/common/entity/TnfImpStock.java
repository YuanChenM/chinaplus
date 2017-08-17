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
 * The persistent class for the TNF_IMP_STOCK database table.
 * 
 */
@Entity(name = "TNF_IMP_STOCK")
public class TnfImpStock extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNF_IMP_STOCK_IMPSTOCKID_GENERATOR",
        sequenceName = "SEQ_TNF_IMP_STOCK",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNF_IMP_STOCK_IMPSTOCKID_GENERATOR")
    @Column(name = "IMP_STOCK_ID")
    private Integer impStockId;

    @Column(name = "BUSINESS_PATTERN")
    private Integer businessPattern;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "DELIVERED_QTY")
    private BigDecimal deliveredQty;

    @Column(name = "IMP_IN_RACK_QTY")
    private BigDecimal impInRackQty;

    @Column(name = "IMP_STOCK_QTY")
    private BigDecimal impStockQty;

    @Column(name = "NG_QTY")
    private BigDecimal ngQty;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "ONHOLD_QTY")
    private BigDecimal onholdQty;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    @Column(name = "SUPPLIER_ID")
    private Integer supplierId;

    @Column(name = "SYSTEM_STOCK_QTY")
    private BigDecimal systemStockQty;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "WHS_ID")
    private Integer whsId;

    public TnfImpStock() {}

    public Integer getImpStockId() {
        return this.impStockId;
    }

    public void setImpStockId(Integer impStockId) {
        this.impStockId = impStockId;
    }

    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
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

    public BigDecimal getDeliveredQty() {
        return this.deliveredQty;
    }

    public void setDeliveredQty(BigDecimal deliveredQty) {
        this.deliveredQty = deliveredQty;
    }

    public BigDecimal getImpInRackQty() {
        return this.impInRackQty;
    }

    public void setImpInRackQty(BigDecimal impInRackQty) {
        this.impInRackQty = impInRackQty;
    }

    public BigDecimal getImpStockQty() {
        return this.impStockQty;
    }

    public void setImpStockQty(BigDecimal impStockQty) {
        this.impStockQty = impStockQty;
    }

    public BigDecimal getNgQty() {
        return this.ngQty;
    }

    public void setNgQty(BigDecimal ngQty) {
        this.ngQty = ngQty;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public BigDecimal getOnholdQty() {
        return this.onholdQty;
    }

    public void setOnholdQty(BigDecimal onholdQty) {
        this.onholdQty = onholdQty;
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public Integer getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getSystemStockQty() {
        return this.systemStockQty;
    }

    public void setSystemStockQty(BigDecimal systemStockQty) {
        this.systemStockQty = systemStockQty;
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

    public Integer getWhsId() {
        return this.whsId;
    }

    public void setWhsId(Integer whsId) {
        this.whsId = whsId;
    }

}