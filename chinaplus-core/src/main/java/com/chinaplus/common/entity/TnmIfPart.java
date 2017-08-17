package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_IF_PARTS database table.
 * 
 */
@Entity(name="TNM_IF_PARTS")
public class TnmIfPart extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_IF_PARTS_IFPARTSID_GENERATOR", sequenceName="SEQ_TNM_IF_PARTS", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_IF_PARTS_IFPARTSID_GENERATOR")
	@Column(name="IF_PARTS_ID")
	private Integer ifPartsId;

	@Column(name="BUILDOUT_FLAG")
	private String buildoutFlag;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMER_CODE")
	private String customerCode;

	@Column(name="CUSTOMER_PARTS_NO")
	private String customerPartsNo;

	@Column(name="DATA_ID")
	private String dataId;

	@Column(name="DISPLAY_PARTS_NO")
	private String displayPartsNo;

	@Column(name="EXP_CODE")
	private String expCode;

	@Column(name="HANDLE_FLAG")
	private Integer handleFlag;

	@Column(name="IF_DATE_TIME")
	private Timestamp ifDateTime;

	@Column(name="MAIN_ROUTE")
	private String mainRoute;

	@Column(name="ORDER_LOT")
	private String orderLot;

	private String spq;

	@Column(name="SUPPLIER_CODE")
	private String supplierCode;

	@Column(name="TRANSPORT_MODE")
	private String transportMode;

	@Column(name="TTC_PARTS_NAME")
	private String ttcPartsName;

	@Column(name="TTC_PARTS_NO")
	private String ttcPartsNo;

	private String uom;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="VALID_FLAG")
	private Integer validFlag;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmIfPart() {
	}

	public Integer getIfPartsId() {
		return this.ifPartsId;
	}

	public void setIfPartsId(Integer ifPartsId) {
		this.ifPartsId = ifPartsId;
	}

	public String getBuildoutFlag() {
		return this.buildoutFlag;
	}

	public void setBuildoutFlag(String buildoutFlag) {
		this.buildoutFlag = buildoutFlag;
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

	public String getCustomerPartsNo() {
		return this.customerPartsNo;
	}

	public void setCustomerPartsNo(String customerPartsNo) {
		this.customerPartsNo = customerPartsNo;
	}

	public String getDataId() {
		return this.dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getDisplayPartsNo() {
		return this.displayPartsNo;
	}

	public void setDisplayPartsNo(String displayPartsNo) {
		this.displayPartsNo = displayPartsNo;
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

	public String getMainRoute() {
		return this.mainRoute;
	}

	public void setMainRoute(String mainRoute) {
		this.mainRoute = mainRoute;
	}

	public String getOrderLot() {
		return this.orderLot;
	}

	public void setOrderLot(String orderLot) {
		this.orderLot = orderLot;
	}

	public String getSpq() {
		return this.spq;
	}

	public void setSpq(String spq) {
		this.spq = spq;
	}

	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getTransportMode() {
		return this.transportMode;
	}

	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}

	public String getTtcPartsName() {
		return this.ttcPartsName;
	}

	public void setTtcPartsName(String ttcPartsName) {
		this.ttcPartsName = ttcPartsName;
	}

	public String getTtcPartsNo() {
		return this.ttcPartsNo;
	}

	public void setTtcPartsNo(String ttcPartsNo) {
		this.ttcPartsNo = ttcPartsNo;
	}

	public String getUom() {
		return this.uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
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

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}