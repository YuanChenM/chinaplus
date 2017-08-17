package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_IF_CUSTOMER database table.
 * 
 */
@Entity(name="TNM_IF_CUSTOMER")
public class TnmIfCustomer extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_IF_CUSTOMER_IFCUSTOMERID_GENERATOR", sequenceName="SEQ_TNM_IF_CUSTOMER", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_IF_CUSTOMER_IFCUSTOMERID_GENERATOR")
	@Column(name="IF_CUSTOMER_ID")
	private Integer ifCustomerId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMER_CODE")
	private String customerCode;

	@Column(name="CUSTOMER_NAME")
	private String customerName;

	@Column(name="DATA_ID")
	private String dataId;

	@Column(name="EXP_CODE")
	private String expCode;

	@Column(name="HANDLE_FLAG")
	private Integer handleFlag;

	@Column(name="IF_DATE_TIME")
	private Timestamp ifDateTime;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="VALID_FLAG")
	private Integer validFlag;

	@Column(name="VENDOR_ROUTE")
	private String vendorRoute;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmIfCustomer() {
	}

	public Integer getIfCustomerId() {
		return this.ifCustomerId;
	}

	public void setIfCustomerId(Integer ifCustomerId) {
		this.ifCustomerId = ifCustomerId;
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

	public String getCustomerCode() {
		return this.customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDataId() {
		return this.dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	public Integer getHandleFlag() {
		return this.handleFlag;
	}

	public void setHandleFlag(Integer handleFlag) {
		this.handleFlag = handleFlag;
	}

	public Timestamp getIfDateTime() {
		return this.ifDateTime;
	}

	public void setIfDateTime(Timestamp ifDateTime) {
		this.ifDateTime = ifDateTime;
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

	public Integer getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
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