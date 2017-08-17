package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_SSMS_CUSTOMER database table.
 * 
 */
@Entity(name="TNM_SSMS_CUSTOMER")
public class TnmSsmsCustomer extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_SSMS_CUSTOMER_SSMSCUSTOMERID_GENERATOR", sequenceName="SEQ_TNM_SSMS_CUSTOMER", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_SSMS_CUSTOMER_SSMSCUSTOMERID_GENERATOR")
	@Column(name="SSMS_CUSTOMER_ID")
	private Integer ssmsCustomerId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="EXP_CODE")
	private String expCode;

	@Column(name="ORION_PLUS_FLAG")
	private Integer orionPlusFlag;

	@Column(name="SSMS_CUSTOMER_CODE")
	private String ssmsCustomerCode;

	@Column(name="SSMS_CUSTOMER_NAME")
	private String ssmsCustomerName;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="VENDOR_ROUTE")
	private String vendorRoute;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmSsmsCustomer() {
	}

	public Integer getSsmsCustomerId() {
		return this.ssmsCustomerId;
	}

	public void setSsmsCustomerId(Integer ssmsCustomerId) {
		this.ssmsCustomerId = ssmsCustomerId;
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

	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	public Integer getOrionPlusFlag() {
		return this.orionPlusFlag;
	}

	public void setOrionPlusFlag(Integer orionPlusFlag) {
		this.orionPlusFlag = orionPlusFlag;
	}

	public String getSsmsCustomerCode() {
		return this.ssmsCustomerCode;
	}

	public void setSsmsCustomerCode(String ssmsCustomerCode) {
		this.ssmsCustomerCode = ssmsCustomerCode;
	}

	public String getSsmsCustomerName() {
		return this.ssmsCustomerName;
	}

	public void setSsmsCustomerName(String ssmsCustomerName) {
		this.ssmsCustomerName = ssmsCustomerName;
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

	public String getVendorRoute() {
		return this.vendorRoute;
	}

	public void setVendorRoute(String vendorRoute) {
		this.vendorRoute = vendorRoute;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}