package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_WHS_CUSTOMER database table.
 * 
 */
@Entity(name="TNM_WHS_CUSTOMER")
public class TnmWhsCustomer extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_WHS_CUSTOMER_WHSCUSTOMERID_GENERATOR", sequenceName="SEQ_TNM_WHS_CUSTOMER", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_WHS_CUSTOMER_WHSCUSTOMERID_GENERATOR")
	@Column(name="WHS_CUSTOMER_ID")
	private Integer whsCustomerId;

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

	@Column(name="\"VERSION\"")
	private Integer version;

	@Column(name="WHS_CUSTOMER_CODE")
	private String whsCustomerCode;

	@Column(name="WHS_ID")
	private Integer whsId;

	public TnmWhsCustomer() {
	}

	public Integer getWhsCustomerId() {
		return this.whsCustomerId;
	}

	public void setWhsCustomerId(Integer whsCustomerId) {
		this.whsCustomerId = whsCustomerId;
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

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getWhsCustomerCode() {
		return this.whsCustomerCode;
	}

	public void setWhsCustomerCode(String whsCustomerCode) {
		this.whsCustomerCode = whsCustomerCode;
	}

	public Integer getWhsId() {
		return this.whsId;
	}

	public void setWhsId(Integer whsId) {
		this.whsId = whsId;
	}

}