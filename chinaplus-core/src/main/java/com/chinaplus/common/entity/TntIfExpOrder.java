package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNT_IF_EXP_ORDER database table.
 * 
 */
@Entity(name="TNT_IF_EXP_ORDER")
public class TntIfExpOrder extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="TNT_IF_EXP_ORDER_IFORDERID_GENERATOR", sequenceName="SEQ_TNT_IF_EXP_ORDER", allocationSize = 1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_IF_EXP_ORDER_IFORDERID_GENERATOR")
	@Column(name="IF_ORDER_ID")
	private Integer ifOrderId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMR_ORDER_NO")
	private String customrOrderNo;

	@Column(name="DATA_ID")
	private String dataId;

	@Column(name="EXP_CODE")
	private String expCode;

	@Column(name="EXP_INB_PLAN_DATE")
	private String expInbPlanDate;

	@Column(name="EXP_PO_ITEM_NO")
	private String expPoItemNo;

	@Column(name="EXP_PO_NO")
	private String expPoNo;

	@Column(name="HANDLE_FLAG")
	private Integer handleFlag;

	@Column(name="IF_DATE_TIME")
	private Timestamp ifDateTime;

	@Column(name="IMP_PO_NO")
	private String impPoNo;

	@Column(name="MAIN_ROUTE")
	private String mainRoute;

	@Column(name="ORDER_QTY")
	private String orderQty;

	@Column(name="ORDER_TYPE")
	private String orderType;

	@Column(name="PO_DATE")
	private String poDate;

	@Column(name="SO_DATE")
	private String soDate;

	@Column(name="SSMS_CUSTOMER_CODE")
	private String ssmsCustomerCode;

	@Column(name="SSMS_SUPPLIER_CODE")
	private String ssmsSupplierCode;

	@Column(name="TRANSPORT_MODE")
	private String transportMode;

	@Column(name="TTC_PARTS_NO")
	private String ttcPartsNo;

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

	public TntIfExpOrder() {
	}

	public Integer getIfOrderId() {
		return this.ifOrderId;
	}

	public void setIfOrderId(Integer ifOrderId) {
		this.ifOrderId = ifOrderId;
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

	public String getCustomrOrderNo() {
		return this.customrOrderNo;
	}

	public void setCustomrOrderNo(String customrOrderNo) {
		this.customrOrderNo = customrOrderNo;
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

	public String getExpInbPlanDate() {
		return this.expInbPlanDate;
	}

	public void setExpInbPlanDate(String expInbPlanDate) {
		this.expInbPlanDate = expInbPlanDate;
	}

	public String getExpPoItemNo() {
		return this.expPoItemNo;
	}

	public void setExpPoItemNo(String expPoItemNo) {
		this.expPoItemNo = expPoItemNo;
	}

	public String getExpPoNo() {
		return this.expPoNo;
	}

	public void setExpPoNo(String expPoNo) {
		this.expPoNo = expPoNo;
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

	public String getImpPoNo() {
		return this.impPoNo;
	}

	public void setImpPoNo(String impPoNo) {
		this.impPoNo = impPoNo;
	}

	public String getMainRoute() {
		return this.mainRoute;
	}

	public void setMainRoute(String mainRoute) {
		this.mainRoute = mainRoute;
	}

	public String getOrderQty() {
		return this.orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPoDate() {
		return this.poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}

	public String getSoDate() {
		return this.soDate;
	}

	public void setSoDate(String soDate) {
		this.soDate = soDate;
	}

	public String getSsmsCustomerCode() {
		return this.ssmsCustomerCode;
	}

	public void setSsmsCustomerCode(String ssmsCustomerCode) {
		this.ssmsCustomerCode = ssmsCustomerCode;
	}

	public String getSsmsSupplierCode() {
		return this.ssmsSupplierCode;
	}

	public void setSsmsSupplierCode(String ssmsSupplierCode) {
		this.ssmsSupplierCode = ssmsSupplierCode;
	}

	public String getTransportMode() {
		return this.transportMode;
	}

	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}

	public String getTtcPartsNo() {
		return this.ttcPartsNo;
	}

	public void setTtcPartsNo(String ttcPartsNo) {
		this.ttcPartsNo = ttcPartsNo;
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