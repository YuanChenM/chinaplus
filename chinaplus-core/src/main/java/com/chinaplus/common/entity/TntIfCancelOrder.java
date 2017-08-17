package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNT_IF_CANCEL_ORDER database table.
 * 
 */
@Entity(name="TNT_IF_CANCEL_ORDER")
public class TntIfCancelOrder extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="TNT_IF_CANCEL_ORDER_IFCANCELID_GENERATOR", sequenceName="SEQ_TNT_IF_CANCEL_ORDER", allocationSize = 1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_IF_CANCEL_ORDER_IFCANCELID_GENERATOR")
	@Column(name="IF_CANCEL_ID")
	private Integer ifCancelId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMER_CODE")
	private String customerCode;

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

	@Column(name="PARTS_NO")
	private String partsNo;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="VALID_FLAG")
	private Integer validFlag;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TntIfCancelOrder() {
	}

	public Integer getIfCancelId() {
		return this.ifCancelId;
	}

	public void setIfCancelId(Integer ifCancelId) {
		this.ifCancelId = ifCancelId;
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

	public String getPartsNo() {
		return this.partsNo;
	}

	public void setPartsNo(String partsNo) {
		this.partsNo = partsNo;
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