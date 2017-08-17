package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;


/**
 * The persistent class for the TNT_IF_EXP_OUTBOUND database table.
 * 
 */
@Entity(name="TNT_IF_EXP_OUTBOUND")
public class TntIfExpOutbound extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="TNT_IF_EXP_OUTBOUND_IFOUTBOUNDID_GENERATOR", sequenceName="SEQ_TNT_IF_EXP_OUTBOUND", allocationSize = 1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_IF_EXP_OUTBOUND_IFOUTBOUNDID_GENERATOR")
	@Column(name="IF_OUTBOUND_ID")
	private Integer ifOutboundId;

	@Column(name="CANCEL_FLAG")
	private String cancelFlag;

	@Column(name="CONTAINER_NO")
	private String containerNo;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="DATA_ID")
	private Integer dataId;

	@Column(name="DT_NO")
	private String dtNo;

	@Column(name="EXP_CODE")
	private String expCode;

	@Column(name="EXP_INB_PLAN_DATE")
	private String expInbPlanDate;

	@Column(name="EXP_OB_ACTUAL_DATE")
	private String expObActualDate;

	@Column(name="HANDLE_FLAG")
	private Integer handleFlag;

	@Column(name="IF_DATE_TIME")
	private Timestamp ifDateTime;

	@Column(name="IMP_PO_NO")
	private String impPoNo;

	@Column(name="IP_NO")
	private String ipNo;

	@Column(name="MAIN_ROUTE")
	private String mainRoute;

	@Column(name="MODULE_NO")
	private String moduleNo;

	@Column(name="OUTBOUND_QTY")
	private BigDecimal outboundQty;

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

	@Column(name="\"VERSION\"")
	private Integer version;

	public TntIfExpOutbound() {
	}

	public Integer getIfOutboundId() {
		return this.ifOutboundId;
	}

	public void setIfOutboundId(Integer ifOutboundId) {
		this.ifOutboundId = ifOutboundId;
	}

	public String getCancelFlag() {
		return this.cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public String getContainerNo() {
		return this.containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
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

	public Integer getDataId() {
		return this.dataId;
	}

	public void setDataId(Integer dataId) {
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

	public String getExpInbPlanDate() {
		return this.expInbPlanDate;
	}

	public void setExpInbPlanDate(String expInbPlanDate) {
		this.expInbPlanDate = expInbPlanDate;
	}

	public String getExpObActualDate() {
		return this.expObActualDate;
	}

	public void setExpObActualDate(String expObActualDate) {
		this.expObActualDate = expObActualDate;
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

	public String getIpNo() {
		return this.ipNo;
	}

	public void setIpNo(String ipNo) {
		this.ipNo = ipNo;
	}

	public String getMainRoute() {
		return this.mainRoute;
	}

	public void setMainRoute(String mainRoute) {
		this.mainRoute = mainRoute;
	}

	public String getModuleNo() {
		return this.moduleNo;
	}

	public void setModuleNo(String moduleNo) {
		this.moduleNo = moduleNo;
	}

	public BigDecimal getOutboundQty() {
		return this.outboundQty;
	}

	public void setOutboundQty(BigDecimal outboundQty) {
		this.outboundQty = outboundQty;
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

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}