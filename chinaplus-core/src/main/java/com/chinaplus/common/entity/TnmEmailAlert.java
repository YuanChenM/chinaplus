package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_EMAIL_ALERT database table.
 * 
 */
@Entity(name="TNM_EMAIL_ALERT")
public class TnmEmailAlert extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_EMAIL_ALERT_EMAILALERTID_GENERATOR", sequenceName="SEQ_TNM_EMAIL_ALERT", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_EMAIL_ALERT_EMAILALERTID_GENERATOR")
	@Column(name="EMAIL_ALERT_ID")
	private Integer emailAlertId;

	@Column(name="ALERT_LEVEL")
	private Integer alertLevel;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMER_ID")
	private Integer customerId;

	@Column(name="INACTIVE_FLAG")
	private Integer inactiveFlag;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="USER_ID")
	private Integer userId;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmEmailAlert() {
	}

	public Integer getEmailAlertId() {
		return this.emailAlertId;
	}

	public void setEmailAlertId(Integer emailAlertId) {
		this.emailAlertId = emailAlertId;
	}

	public Integer getAlertLevel() {
		return this.alertLevel;
	}

	public void setAlertLevel(Integer alertLevel) {
		this.alertLevel = alertLevel;
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

	public Integer getInactiveFlag() {
		return this.inactiveFlag;
	}

	public void setInactiveFlag(Integer inactiveFlag) {
		this.inactiveFlag = inactiveFlag;
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

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}