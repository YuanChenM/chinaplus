package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_WAREHOUSE database table.
 * 
 */
@Entity(name="TNM_WAREHOUSE")
public class TnmWarehouse extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_WAREHOUSE_WHSID_GENERATOR", sequenceName="SEQ_TNM_WAREHOUSE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_WAREHOUSE_WHSID_GENERATOR")
	@Column(name="WHS_ID")
	private Integer whsId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="INACTIVE_FLAG")
	private Integer inactiveFlag;

	@Column(name="REGION_CODE")
	private String regionCode;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	@Column(name="WHS_CODE")
	private String whsCode;

	@Column(name="WHS_NAME")
	private String whsName;

	public TnmWarehouse() {
	}

	public Integer getWhsId() {
		return this.whsId;
	}

	public void setWhsId(Integer whsId) {
		this.whsId = whsId;
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

	public Integer getInactiveFlag() {
		return this.inactiveFlag;
	}

	public void setInactiveFlag(Integer inactiveFlag) {
		this.inactiveFlag = inactiveFlag;
	}

	public String getRegionCode() {
		return this.regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
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

	public String getWhsCode() {
		return this.whsCode;
	}

	public void setWhsCode(String whsCode) {
		this.whsCode = whsCode;
	}

	public String getWhsName() {
		return this.whsName;
	}

	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}

}