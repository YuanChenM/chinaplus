package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TNT_EXP_DT database table.
 * 
 */
@Entity(name="TNT_EXP_DT")
public class TntExpDt extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_EXP_DT_EXPDTID_GENERATOR", sequenceName="SEQ_TNT_EXP_DT", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_EXP_DT_EXPDTID_GENERATOR")
	@Column(name="EXP_DT_ID")
	private Integer expDtId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="DT_NO")
	private String dtNo;

	@Temporal(TemporalType.DATE)
	@Column(name="EXP_INB_ACTUAL_DATE")
	private Date expInbActualDate;

	@Temporal(TemporalType.DATE)
	@Column(name="EXP_INB_PLAN_DATE")
	private Date expInbPlanDate;

	@Column(name="EXP_PARTS_ID")
	private Integer expPartsId;

	@Column(name="EXP_PO_ITEM_NO")
	private String expPoItemNo;

	@Column(name="EXP_PO_NO")
	private String expPoNo;

	@Column(name="IMP_PO_NO")
	private String impPoNo;

	@Column(name="PARTS_ID")
	private Integer partsId;

	private BigDecimal qty;

	@Column(name="\"STATUS\"")
	private Integer status;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TntExpDt() {
	}

	public Integer getExpDtId() {
		return this.expDtId;
	}

	public void setExpDtId(Integer expDtId) {
		this.expDtId = expDtId;
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

	public String getDtNo() {
		return this.dtNo;
	}

	public void setDtNo(String dtNo) {
		this.dtNo = dtNo;
	}

	public Date getExpInbActualDate() {
		return this.expInbActualDate;
	}

	public void setExpInbActualDate(Date expInbActualDate) {
		this.expInbActualDate = expInbActualDate;
	}

	public Date getExpInbPlanDate() {
		return this.expInbPlanDate;
	}

	public void setExpInbPlanDate(Date expInbPlanDate) {
		this.expInbPlanDate = expInbPlanDate;
	}

	public Integer getExpPartsId() {
		return this.expPartsId;
	}

	public void setExpPartsId(Integer expPartsId) {
		this.expPartsId = expPartsId;
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

	public String getImpPoNo() {
		return this.impPoNo;
	}

	public void setImpPoNo(String impPoNo) {
		this.impPoNo = impPoNo;
	}

	public Integer getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Integer partsId) {
		this.partsId = partsId;
	}

	public BigDecimal getQty() {
		return this.qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
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

}