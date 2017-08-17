package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TNT_CFC_MASTER database table.
 * 
 */
@Entity(name="TNT_CFC_MASTER")
public class TntCfcMaster extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_CFC_MASTER_CFCID_GENERATOR", sequenceName="SEQ_TNT_CFC_MASTER", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_CFC_MASTER_CFCID_GENERATOR")
	@Column(name="CFC_ID")
	private Integer cfcId;

	@Column(name="BUSINESS_PATTERN")
	private Integer businessPattern;

	@Column(name="CFC_NO")
	private String cfcNo;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMER_ID")
	private Integer customerId;

	@Temporal(TemporalType.DATE)
	@Column(name="FC_DATE")
	private Date fcDate;

	@Column(name="FIRST_FC_MONTH")
	private String firstFcMonth;

	@Column(name="LAST_FC_MONTH")
	private String lastFcMonth;

	@Column(name="OFFICE_ID")
	private Integer officeId;

	private String remark;

	@Column(name="\"STATUS\"")
	private Integer status;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="UPLOADED_BY")
	private Integer uploadedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="UPLOADED_DATE")
	private Date uploadedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	//bi-directional many-to-one association to TntCfcDay
	@OneToMany(mappedBy="tntCfcMaster")
	private List<TntCfcDay> tntCfcDays;

	//bi-directional many-to-one association to TntCfcMonth
	@OneToMany(mappedBy="tntCfcMaster")
	private List<TntCfcMonth> tntCfcMonths;

	public TntCfcMaster() {
	}

	public Integer getCfcId() {
		return this.cfcId;
	}

	public void setCfcId(Integer cfcId) {
		this.cfcId = cfcId;
	}

	public Integer getBusinessPattern() {
		return this.businessPattern;
	}

	public void setBusinessPattern(Integer businessPattern) {
		this.businessPattern = businessPattern;
	}

	public String getCfcNo() {
		return this.cfcNo;
	}

	public void setCfcNo(String cfcNo) {
		this.cfcNo = cfcNo;
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

	public Date getFcDate() {
		return this.fcDate;
	}

	public void setFcDate(Date fcDate) {
		this.fcDate = fcDate;
	}

	public String getFirstFcMonth() {
		return this.firstFcMonth;
	}

	public void setFirstFcMonth(String firstFcMonth) {
		this.firstFcMonth = firstFcMonth;
	}

	public String getLastFcMonth() {
		return this.lastFcMonth;
	}

	public void setLastFcMonth(String lastFcMonth) {
		this.lastFcMonth = lastFcMonth;
	}

	public Integer getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getUploadedBy() {
		return this.uploadedBy;
	}

	public void setUploadedBy(Integer uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public Date getUploadedDate() {
		return this.uploadedDate;
	}

	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<TntCfcDay> getTntCfcDays() {
		return this.tntCfcDays;
	}

	public void setTntCfcDays(List<TntCfcDay> tntCfcDays) {
		this.tntCfcDays = tntCfcDays;
	}

	public TntCfcDay addTntCfcDay(TntCfcDay tntCfcDay) {
		getTntCfcDays().add(tntCfcDay);
		tntCfcDay.setTntCfcMaster(this);

		return tntCfcDay;
	}

	public TntCfcDay removeTntCfcDay(TntCfcDay tntCfcDay) {
		getTntCfcDays().remove(tntCfcDay);
		tntCfcDay.setTntCfcMaster(null);

		return tntCfcDay;
	}

	public List<TntCfcMonth> getTntCfcMonths() {
		return this.tntCfcMonths;
	}

	public void setTntCfcMonths(List<TntCfcMonth> tntCfcMonths) {
		this.tntCfcMonths = tntCfcMonths;
	}

	public TntCfcMonth addTntCfcMonth(TntCfcMonth tntCfcMonth) {
		getTntCfcMonths().add(tntCfcMonth);
		tntCfcMonth.setTntCfcMaster(this);

		return tntCfcMonth;
	}

	public TntCfcMonth removeTntCfcMonth(TntCfcMonth tntCfcMonth) {
		getTntCfcMonths().remove(tntCfcMonth);
		tntCfcMonth.setTntCfcMaster(null);

		return tntCfcMonth;
	}

}