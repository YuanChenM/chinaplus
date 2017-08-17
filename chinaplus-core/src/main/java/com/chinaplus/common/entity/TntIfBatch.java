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
 * The persistent class for the TNT_IF_BATCH database table.
 * 
 */
@Entity(name = "TNT_IF_BATCH")
public class TntIfBatch extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_IF_BATCH_IFBATCHID_GENERATOR",
        sequenceName = "SEQ_TNT_IF_BATCH",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_IF_BATCH_IFBATCHID_GENERATOR")
    @Column(name = "IF_BATCH_ID")
    private Integer ifBatchId;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "BATCH_TYPE_NAME")
    private String batchTypeName;

    @Column(name = "BATCH_TYPE")
    private Integer batchType;

    @Column(name = "IF_DATE_TIME")
    private Timestamp ifDateTime;

    @Column(name = "STATUS")
    private Integer status;

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

    public TntIfBatch() {}

    public Integer getIfBatchId() {
        return this.ifBatchId;
    }

    public void setIfBatchId(Integer ifBatchId) {
        this.ifBatchId = ifBatchId;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public String getBatchTypeName() {
        return this.batchTypeName;
    }

    public void setBatchTypeName(String batchTypeName) {
        this.batchTypeName = batchTypeName;
    }

    public Integer getBatchType() {
        return this.batchType;
    }

    public void setBatchType(Integer batchType) {
        this.batchType = batchType;
    }

    public Timestamp getIfDateTime() {
        return this.ifDateTime;
    }

    public void setIfDateTime(Timestamp ifDateTime) {
        this.ifDateTime = ifDateTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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