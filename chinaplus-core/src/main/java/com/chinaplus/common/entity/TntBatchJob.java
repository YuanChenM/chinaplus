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
 * The persistent class for the TNT_BATCH_JOB database table.
 * 
 */
@Entity(name="TNT_BATCH_JOB")
public class TntBatchJob extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_BATCH_JOB_BATCHJOBID_GENERATOR", sequenceName="SEQ_TNT_BATCH_JOB", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_BATCH_JOB_BATCHJOBID_GENERATOR")
	@Column(name="BATCH_JOB_ID")
	private Integer batchJobId;

	@Column(name="BATCH_ID")
	private String batchId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="END_TIME")
	private Timestamp endTime;

	@Column(name="OFFICE_ID")
	private Integer officeId;

	@Column(name="PROCESS_DATE")
	private Timestamp processDate;

	@Column(name="START_TIME")
	private Timestamp startTime;

	@Column(name="\"STATUS\"")
	private Integer status;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TntBatchJob() {
	}

	public Integer getBatchJobId() {
		return this.batchJobId;
	}

	public void setBatchJobId(Integer batchJobId) {
		this.batchJobId = batchJobId;
	}

	public String getBatchId() {
		return this.batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
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

	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Integer getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
	}

	public Timestamp getProcessDate() {
		return this.processDate;
	}

	public void setProcessDate(Timestamp processDate) {
		this.processDate = processDate;
	}

	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
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

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}