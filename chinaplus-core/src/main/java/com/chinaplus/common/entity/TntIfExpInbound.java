package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;


/**
 * The persistent class for the TNT_IF_EXP_INBOUND database table.
 * 
 */
@Entity(name="TNT_IF_EXP_INBOUND")
public class TntIfExpInbound extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="TNT_IF_EXP_INBOUND_IFINBOUNDID_GENERATOR", sequenceName="SEQ_TNT_IF_EXP_INBOUND", allocationSize = 1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_IF_EXP_INBOUND_IFINBOUNDID_GENERATOR")
	@Column(name="IF_INBOUND_ID")
	private Integer ifInboundId;

	@Column(name="CANCEL_FLAG")
	private String cancelFlag;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="DATA_ID")
	private String dataId;

	@Column(name="DT_NO")
	private String dtNo;

	@Column(name="EXP_CODE")
	private String expCode;

	@Column(name="EXP_INB_ACTUAL_DATE")
	private String expInbActualDate;

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

	private BigDecimal qty;

	@Column(name="SSMS_CUSTOMER_CODE")
	private String ssmsCustomerCode;

	@Column(name="TTC_PARTS_NO")
	private String ttcPartsNo;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="VALID_FLAG")
	private Integer validFlag;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TntIfExpInbound() {
	}

	public Integer getIfInboundId() {
		return this.ifInboundId;
	}

	public void setIfInboundId(Integer ifInboundId) {
		this.ifInboundId = ifInboundId;
	}

	public String getCancelFlag() {
		return this.cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
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

	public String getDataId() {
		return this.dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getDtNo() {
		return this.dtNo;
	}

	public void setDtNo(String dtNo) {
		this.dtNo = dtNo;
	}

	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	public String getExpInbActualDate() {
		return this.expInbActualDate;
	}

	public void setExpInbActualDate(String expInbActualDate) {
		this.expInbActualDate = expInbActualDate;
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

	public BigDecimal getQty() {
		return this.qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public String getSsmsCustomerCode() {
		return this.ssmsCustomerCode;
	}

	public void setSsmsCustomerCode(String ssmsCustomerCode) {
		this.ssmsCustomerCode = ssmsCustomerCode;
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

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}