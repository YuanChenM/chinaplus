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
 * The persistent class for the TNT_SS_MASTER database table.
 * 
 */
@Entity(name = "TNT_SS_MASTER")
public class TntSsMaster extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_SS_MASTER_SSID_GENERATOR",
        sequenceName = "SEQ_TNT_SS_MASTER",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_SS_MASTER_SSID_GENERATOR")
    @Column(name = "SS_ID")
    private Integer ssId;

    @Column(name = "ORDER_ID")
    private Integer orderId;

    @Column(name = "AIR_FLAG")
    private Integer airFlag;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "REASON_CODE_SET")
    private String reasonCodeSet;

    @Column(name = "REVISION_REASON")
    private String revisionReason;

    @Column(name = "REVISION_VERSION")
    private Integer revisionVersion;

    @Column(name = "SEA_FLAG")
    private Integer seaFlag;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "UPLOADED_BY")
    private Integer uploadedBy;

    @Column(name = "UPLOADED_DATE")
    private Timestamp uploadedDate;

    @Column(name = "UPLOADED_FILE_NAME")
    private String uploadedFileName;

    @Column(name = "\"VERSION\"")
    private Integer version;

    public TntSsMaster() {}

    public Integer getSsId() {
        return this.ssId;
    }

    public void setSsId(Integer ssId) {
        this.ssId = ssId;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getAirFlag() {
        return this.airFlag;
    }

    public void setAirFlag(Integer airFlag) {
        this.airFlag = airFlag;
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

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public String getReasonCodeSet() {
        return this.reasonCodeSet;
    }

    public void setReasonCodeSet(String reasonCodeSet) {
        this.reasonCodeSet = reasonCodeSet;
    }

    public String getRevisionReason() {
        return this.revisionReason;
    }

    public void setRevisionReason(String revisionReason) {
        this.revisionReason = revisionReason;
    }

    public Integer getRevisionVersion() {
        return this.revisionVersion;
    }

    public void setRevisionVersion(Integer revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    public Integer getSeaFlag() {
        return this.seaFlag;
    }

    public void setSeaFlag(Integer seaFlag) {
        this.seaFlag = seaFlag;
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

    public Timestamp getUploadedDate() {
        return this.uploadedDate;
    }

    public void setUploadedDate(Timestamp uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public String getUploadedFileName() {
        return this.uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}