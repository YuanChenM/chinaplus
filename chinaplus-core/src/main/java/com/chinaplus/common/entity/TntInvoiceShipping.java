package com.chinaplus.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * The persistent class for the TNT_INVOICE_SHIPPING database table.
 * 
 */
@Entity(name = "TNT_INVOICE_SHIPPING")
public class TntInvoiceShipping extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_INVOICE_SHIPPING_INVOICESHIPPINGID_GENERATOR",
        sequenceName = "SEQ_TNT_INVOICE_SHIPPING",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_INVOICE_SHIPPING_INVOICESHIPPINGID_GENERATOR")
    @Column(name = "INVOICE_SHIPPING_ID")
    private Integer invoiceShippingId;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "KANBAN_SHIPPING_ID")
    private Integer kanbanShippingId;

    @Column(name = "\"STATUS\"")
    private Integer status;

    @Column(name = "NIRD_FLAG")
    private Integer nirdFlag;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    public TntInvoiceShipping() {}

    public Integer getInvoiceShippingId() {
        return this.invoiceShippingId;
    }

    public void setInvoiceShippingId(Integer invoiceShippingId) {
        this.invoiceShippingId = invoiceShippingId;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Integer getKanbanShippingId() {
        return this.kanbanShippingId;
    }

    public void setKanbanShippingId(Integer kanbanShippingId) {
        this.kanbanShippingId = kanbanShippingId;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNirdFlag() {
        return this.nirdFlag;
    }

    public void setNirdFlag(Integer nirdFlag) {
        this.nirdFlag = nirdFlag;
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