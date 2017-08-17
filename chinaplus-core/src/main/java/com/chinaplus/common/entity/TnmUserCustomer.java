package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_USER_CUSTOMER database table.
 * 
 */
@Entity(name="TNM_USER_CUSTOMER")
public class TnmUserCustomer extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_USER_CUSTOMER_USERCUSTOMERID_GENERATOR", sequenceName="SEQ_TNM_USER_CUSTOMER", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_USER_CUSTOMER_USERCUSTOMERID_GENERATOR")
	@Column(name="USER_CUSTOMER_ID")
	private Integer userCustomerId;

	@Column(name="ALL_CUSTOMER_FLAG")
	private Integer allCustomerFlag;

	@Column(name="BUSINESS_PATTERN")
	private Integer businessPattern;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMER_ID")
	private Integer customerId;

	@Column(name="OFFICE_ID")
	private Integer officeId;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="USER_ID")
	private Integer userId;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmUserCustomer() {
	}

	public Integer getUserCustomerId() {
		return this.userCustomerId;
	}

	public void setUserCustomerId(Integer userCustomerId) {
		this.userCustomerId = userCustomerId;
	}

	public Integer getAllCustomerFlag() {
		return this.allCustomerFlag;
	}

	public void setAllCustomerFlag(Integer allCustomerFlag) {
		this.allCustomerFlag = allCustomerFlag;
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

	public Integer getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
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