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
 * The persistent class for the TNT_INVOICE_PARTS database table.
 * 
 */
@Entity(name = "TNT_INVOICE_PARTS")
public class TntInvoicePart extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_INVOICE_PARTS_INVOICEDETAILID_GENERATOR",
        sequenceName = "SEQ_TNT_INVOICE_PARTS",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_INVOICE_PARTS_INVOICEDETAILID_GENERATOR")
    @Column(name = "INVOICE_DETAIL_ID")
    private Integer invoiceDetailId;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CUSTOMER_ORDER_NO")
    private String customerOrderNo;

    @Column(name = "EXP_PO_NO")
    private String expPoNo;

    @Column(name = "IMP_PO_NO")
    private String impPoNo;

    @Column(name = "INV_CUST_CODE")
    private String invCustCode;

    @Column(name = "ORIGINAL_QTY")
    private BigDecimal originalQty;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    private BigDecimal qty;

    @Column(name = "SUPPLIER_ID")
    private Integer supplierId;

    @Column(name = "SUPPLIER_PARTS_NO")
    private String supplierPartsNo;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "INVOICE_ID")
    private Integer invoiceId;

    public TntInvoicePart() {}

    public Integer getInvoiceDetailId() {
        return this.invoiceDetailId;
    }

    public void setInvoiceDetailId(Integer invoiceDetailId) {
        this.invoiceDetailId = invoiceDetailId;
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

    public String getCustomerOrderNo() {
        return this.customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    public String getExpPoNo() {
        return this.expPoNo;
    }

    public void setExpPoNo(String expPoNo) {
        this.expPoNo = expPoNo;
    }

    public String getImpPoNo() {
        return this.impPoNo;
    }

    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
    }

    public String getInvCustCode() {
        return this.invCustCode;
    }

    public void setInvCustCode(String invCustCode) {
        this.invCustCode = invCustCode;
    }

    public BigDecimal getOriginalQty() {
        return this.originalQty;
    }

    public void setOriginalQty(BigDecimal originalQty) {
        this.originalQty = originalQty;
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

    public Integer getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierPartsNo() {
        return this.supplierPartsNo;
    }

    public void setSupplierPartsNo(String supplierPartsNo) {
        this.supplierPartsNo = supplierPartsNo;
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

    public Integer getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

}