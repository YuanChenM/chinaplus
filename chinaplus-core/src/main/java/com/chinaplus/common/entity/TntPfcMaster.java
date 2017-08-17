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
 * The persistent class for the TNT_PFC_MASTER database table.
 * 
 */
@Entity(name = "TNT_PFC_MASTER")
public class TntPfcMaster extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_PFC_MASTER_PFCID_GENERATOR",
        sequenceName = "SEQ_TNT_PFC_MASTER",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_PFC_MASTER_PFCID_GENERATOR")
    @Column(name = "PFC_ID")
    private Integer pfcId;

    @Column(name = "BUSINESS_PATTERN")
    private Integer businessPattern;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "FIRST_FC_MONTH")
    private String firstFcMonth;

    @Column(name = "IMP_REGION")
    private String impRegion;

    @Column(name = "LAST_FC_MONTH")
    private String lastFcMonth;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "ORDER_MONTH")
    private String orderMonth;

    @Column(name = "PFC_NO")
    private String pfcNo;

    private String remark;

    @Column(name = "REVISE_VERSION")
    private Integer reviseVersion;

    @Column(name = "\"STATUS\"")
    private Integer status;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "UPLOADED_BY")
    private Integer uploadedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "UPLOADED_DATE")
    private Date uploadedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    public TntPfcMaster() {}

    public Integer getPfcId() {
        return this.pfcId;
    }

    public void setPfcId(Integer pfcId) {
        this.pfcId = pfcId;
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

    public String getFirstFcMonth() {
        return this.firstFcMonth;
    }

    public void setFirstFcMonth(String firstFcMonth) {
        this.firstFcMonth = firstFcMonth;
    }

    public String getImpRegion() {
        return this.impRegion;
    }

    public void setImpRegion(String impRegion) {
        this.impRegion = impRegion;
    }

    public String getLastFcMonth() {
        return this.lastFcMonth;
    }

    public void setLastFcMonth(String lastFcMonth) {
        this.lastFcMonth = lastFcMonth;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public String getOrderMonth() {
        return this.orderMonth;
    }

    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    public String getPfcNo() {
        return this.pfcNo;
    }

    public void setPfcNo(String pfcNo) {
        this.pfcNo = pfcNo;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getReviseVersion() {
        return this.reviseVersion;
    }

    public void setReviseVersion(Integer reviseVersion) {
        this.reviseVersion = reviseVersion;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getUploadedBy() {
        return this.uploadedBy;
    }

    public void setUploadedBy(Integer uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Date getUploadedDate() {
        return this.uploadedDate;
    }

    public void setUploadedDate(Date uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}