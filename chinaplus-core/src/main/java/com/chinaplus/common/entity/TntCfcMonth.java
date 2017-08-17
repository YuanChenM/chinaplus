package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TNT_CFC_MONTH database table.
 * 
 */
@Entity(name="TNT_CFC_MONTH")
public class TntCfcMonth extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_CFC_MONTH_CFCMONTHID_GENERATOR", sequenceName="SEQ_TNT_CFC_MONTH", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_CFC_MONTH_CFCMONTHID_GENERATOR")
	@Column(name="CFC_MONTH_ID")
	private Integer cfcMonthId;

	@Column(name="CFC_MONTH")
	private String cfcMonth;

	@Column(name="CFC_QTY")
	private BigDecimal cfcQty;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="PARTS_ID")
	private Integer partsId;

	@Column(name="\"STATUS\"")
	private Integer status;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	//bi-directional many-to-one association to TntCfcDay
	@OneToMany(mappedBy="tntCfcMonth")
	private List<TntCfcDay> tntCfcDays;

	//bi-directional many-to-one association to TntCfcMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CFC_ID")
	private TntCfcMaster tntCfcMaster;

	public TntCfcMonth() {
	}

	public Integer getCfcMonthId() {
		return this.cfcMonthId;
	}

	public void setCfcMonthId(Integer cfcMonthId) {
		this.cfcMonthId = cfcMonthId;
	}

	public String getCfcMonth() {
		return this.cfcMonth;
	}

	public void setCfcMonth(String cfcMonth) {
		this.cfcMonth = cfcMonth;
	}

	public BigDecimal getCfcQty() {
		return this.cfcQty;
	}

	public void setCfcQty(BigDecimal cfcQty) {
		this.cfcQty = cfcQty;
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

	public Integer getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Integer partsId) {
		this.partsId = partsId;
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
		tntCfcDay.setTntCfcMonth(this);

		return tntCfcDay;
	}

	public TntCfcDay removeTntCfcDay(TntCfcDay tntCfcDay) {
		getTntCfcDays().remove(tntCfcDay);
		tntCfcDay.setTntCfcMonth(null);

		return tntCfcDay;
	}

	public TntCfcMaster getTntCfcMaster() {
		return this.tntCfcMaster;
	}

	public void setTntCfcMaster(TntCfcMaster tntCfcMaster) {
		this.tntCfcMaster = tntCfcMaster;
	}

}