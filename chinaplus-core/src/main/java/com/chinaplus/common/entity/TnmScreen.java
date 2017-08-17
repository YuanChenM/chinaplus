package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_SCREEN database table.
 * 
 */
@Entity(name="TNM_SCREEN")
public class TnmScreen extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_SCREEN_SYSSCREENID_GENERATOR", sequenceName="SEQ_TNM_SCREEN", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_SCREEN_SYSSCREENID_GENERATOR")
	@Column(name="SYS_SCREEN_ID")
	private Integer sysScreenId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="PROJECT_FLAG")
	private Integer projectFlag;

	@Column(name="SCREEN_ID")
	private String screenId;

	@Column(name="SCREEN_URL")
	private String screenUrl;

	@Column(name="SHOW_FLAG")
	private Integer showFlag;

	@Column(name="SORT_NO")
	private Integer sortNo;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmScreen() {
	}

	public Integer getSysScreenId() {
		return this.sysScreenId;
	}

	public void setSysScreenId(Integer sysScreenId) {
		this.sysScreenId = sysScreenId;
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

	public Integer getProjectFlag() {
		return this.projectFlag;
	}

	public void setProjectFlag(Integer projectFlag) {
		this.projectFlag = projectFlag;
	}

	public String getScreenId() {
		return this.screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	public String getScreenUrl() {
		return this.screenUrl;
	}

	public void setScreenUrl(String screenUrl) {
		this.screenUrl = screenUrl;
	}

	public Integer getShowFlag() {
		return this.showFlag;
	}

	public void setShowFlag(Integer showFlag) {
		this.showFlag = showFlag;
	}

	public Integer getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
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