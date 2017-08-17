package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TNM_SR_MASTER database table.
 * 
 */
@Entity(name="TNM_SR_MASTER")
public class TnmSrMaster extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_SR_MASTER_SRID_GENERATOR", sequenceName="SEQ_TNM_SR_MASTER", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_SR_MASTER_SRID_GENERATOR")
	@Column(name="SR_ID")
	private Integer srId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="DELIVERY_LEADTIME")
	private Integer deliveryLeadtime;

	@Temporal(TemporalType.DATE)
	@Column(name="DELIVERY_START_DATE")
	private Date deliveryStartDate;

	@Column(name="ETD_DAY")
	private String etdDay;

	@Column(name="ETD_WEEK")
	private String etdWeek;

	@Column(name="EXP_INBOUND_LEADTIME")
	private Integer expInboundLeadtime;

	@Column(name="EXP_REGION")
	private String expRegion;

	@Column(name="EXP_VANNING_LEADTIME")
	private Integer expVanningLeadtime;

	@Column(name="FORWARDER_NAME")
	private String forwarderName;

	@Temporal(TemporalType.DATE)
	@Column(name="FROM_ETD")
	private Date fromEtd;

	@Column(name="IMP_CC_LEADTIME")
	private Integer impCcLeadtime;

	@Column(name="IMP_INBOUND_LEADTIME")
	private Integer impInboundLeadtime;

	@Column(name="INACTIVE_FLAG")
	private Integer inactiveFlag;

	@Column(name="OFFICE_ID")
	private Integer officeId;

	@Column(name="SHIPPING_ROUTE_CODE")
	private String shippingRouteCode;

	@Column(name="SHIPPING_ROUTE_TYPE")
	private Integer shippingRouteType;

	@Temporal(TemporalType.DATE)
	@Column(name="TO_ETD")
	private Date toEtd;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="VANNING_DAY")
	private String vanningDay;

	@Column(name="\"VERSION\"")
	private Integer version;

	//bi-directional many-to-one association to TnmSrDetail
	@OneToMany(mappedBy="tnmSrMaster")
	private List<TnmSrDetail> tnmSrDetails;

	public TnmSrMaster() {
	}

	public Integer getSrId() {
		return this.srId;
	}

	public void setSrId(Integer srId) {
		this.srId = srId;
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

	public Integer getDeliveryLeadtime() {
		return this.deliveryLeadtime;
	}

	public void setDeliveryLeadtime(Integer deliveryLeadtime) {
		this.deliveryLeadtime = deliveryLeadtime;
	}

	public Date getDeliveryStartDate() {
		return this.deliveryStartDate;
	}

	public void setDeliveryStartDate(Date deliveryStartDate) {
		this.deliveryStartDate = deliveryStartDate;
	}

	public String getEtdDay() {
		return this.etdDay;
	}

	public void setEtdDay(String etdDay) {
		this.etdDay = etdDay;
	}

	public String getEtdWeek() {
		return this.etdWeek;
	}

	public void setEtdWeek(String etdWeek) {
		this.etdWeek = etdWeek;
	}

	public Integer getExpInboundLeadtime() {
		return this.expInboundLeadtime;
	}

	public void setExpInboundLeadtime(Integer expInboundLeadtime) {
		this.expInboundLeadtime = expInboundLeadtime;
	}

	public String getExpRegion() {
		return this.expRegion;
	}

	public void setExpRegion(String expRegion) {
		this.expRegion = expRegion;
	}

	public Integer getExpVanningLeadtime() {
		return this.expVanningLeadtime;
	}

	public void setExpVanningLeadtime(Integer expVanningLeadtime) {
		this.expVanningLeadtime = expVanningLeadtime;
	}

	public String getForwarderName() {
		return this.forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
	}

	public Date getFromEtd() {
		return this.fromEtd;
	}

	public void setFromEtd(Date fromEtd) {
		this.fromEtd = fromEtd;
	}

	public Integer getImpCcLeadtime() {
		return this.impCcLeadtime;
	}

	public void setImpCcLeadtime(Integer impCcLeadtime) {
		this.impCcLeadtime = impCcLeadtime;
	}

	public Integer getImpInboundLeadtime() {
		return this.impInboundLeadtime;
	}

	public void setImpInboundLeadtime(Integer impInboundLeadtime) {
		this.impInboundLeadtime = impInboundLeadtime;
	}

	public Integer getInactiveFlag() {
		return this.inactiveFlag;
	}

	public void setInactiveFlag(Integer inactiveFlag) {
		this.inactiveFlag = inactiveFlag;
	}

	public Integer getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
	}

	public String getShippingRouteCode() {
		return this.shippingRouteCode;
	}

	public void setShippingRouteCode(String shippingRouteCode) {
		this.shippingRouteCode = shippingRouteCode;
	}

	public Integer getShippingRouteType() {
		return this.shippingRouteType;
	}

	public void setShippingRouteType(Integer shippingRouteType) {
		this.shippingRouteType = shippingRouteType;
	}

	public Date getToEtd() {
		return this.toEtd;
	}

	public void setToEtd(Date toEtd) {
		this.toEtd = toEtd;
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

	public String getVanningDay() {
		return this.vanningDay;
	}

	public void setVanningDay(String vanningDay) {
		this.vanningDay = vanningDay;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<TnmSrDetail> getTnmSrDetails() {
		return this.tnmSrDetails;
	}

	public void setTnmSrDetails(List<TnmSrDetail> tnmSrDetails) {
		this.tnmSrDetails = tnmSrDetails;
	}

	public TnmSrDetail addTnmSrDetail(TnmSrDetail tnmSrDetail) {
		getTnmSrDetails().add(tnmSrDetail);
		tnmSrDetail.setTnmSrMaster(this);

		return tnmSrDetail;
	}

	public TnmSrDetail removeTnmSrDetail(TnmSrDetail tnmSrDetail) {
		getTnmSrDetails().remove(tnmSrDetail);
		tnmSrDetail.setTnmSrMaster(null);

		return tnmSrDetail;
	}

}