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
 * The persistent class for the TNT_ORDER database table.
 * 
 */
@Entity(name = "TNT_ORDER")
public class TntOrder extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_ORDER_ORDERID_GENERATOR",
        sequenceName = "SEQ_TNT_ORDER",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_ORDER_ORDERID_GENERATOR")
    @Column(name = "ORDER_ID")
    private Integer orderId;

    @Column(name = "BUSINESS_PATTERN")
    private Integer businessPattern;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "CUSTOMER_ORDER_NO")
    private String customerOrderNo;

    @Column(name = "EXP_REGION")
    private String expRegion;

    @Temporal(TemporalType.DATE)
    @Column(name = "EXP_SO_DATE")
    private Date expSoDate;

    @Column(name = "IMP_PO_NO")
    private String impPoNo;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "ORDER_TYPE")
    private String orderType;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    public TntOrder() {}

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public Integer getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerOrderNo() {
        return this.customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    public String getExpRegion() {
        return this.expRegion;
    }

    public void setExpRegion(String expRegion) {
        this.expRegion = expRegion;
    }

    public Date getExpSoDate() {
        return this.expSoDate;
    }

    public void setExpSoDate(Date expSoDate) {
        this.expSoDate = expSoDate;
    }

    public String getImpPoNo() {
        return this.impPoNo;
    }

    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public String getOrderType() {
        return this.orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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