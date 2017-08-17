package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TNT_CFC_DAY database table.
 * 
 */
@Entity(name="TNT_CFC_DAY")
public class TntCfcDay extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_CFC_DAY_CFCDAYID_GENERATOR", sequenceName="SEQ_TNT_CFC_DAY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_CFC_DAY_CFCDAYID_GENERATOR")
	@Column(name="CFC_DAY_ID")
	private Integer cfcDayId;

	@Temporal(TemporalType.DATE)
	@Column(name="CFC_DATE")
	private Date cfcDate;

	@Column(name="CFC_QTY")
	private BigDecimal cfcQty;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="PARTS_ID")
	private Integer partsId;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="VALID_FLAG")
	private Integer validFlag;

	@Column(name="\"VERSION\"")
	private Integer version;

	//bi-directional many-to-one association to TntCfcMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CFC_ID")
	private TntCfcMaster tntCfcMaster;

	//bi-directional many-to-one association to TntCfcMonth
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CFC_MONTH_ID")
	private TntCfcMonth tntCfcMonth;

	public TntCfcDay() {
	}

	public Integer getCfcDayId() {
		return this.cfcDayId;
	}

	public void setCfcDayId(Integer cfcDayId) {
		this.cfcDayId = cfcDayId;
	}

	public Date getCfcDate() {
		return this.cfcDate;
	}

	public void setCfcDate(Date cfcDate) {
		this.cfcDate = cfcDate;
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

	public TntCfcMaster getTntCfcMaster() {
		return this.tntCfcMaster;
	}

	public void setTntCfcMaster(TntCfcMaster tntCfcMaster) {
		this.tntCfcMaster = tntCfcMaster;
	}

	public TntCfcMonth getTntCfcMonth() {
		return this.tntCfcMonth;
	}

	public void setTntCfcMonth(TntCfcMonth tntCfcMonth) {
		this.tntCfcMonth = tntCfcMonth;
	}

}