package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_MENU_RESOURCE database table.
 * 
 */
@Entity(name="TNM_MENU_RESOURCE")
public class TnmMenuResource extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_MENU_RESOURCE_RESOURCEID_GENERATOR", sequenceName="SEQ_TNM_MENU_RESOURCE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_MENU_RESOURCE_RESOURCEID_GENERATOR")
	@Column(name="RESOURCE_ID")
	private Integer resourceId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="ROOT_ID")
	private Integer rootId;

	@Column(name="ROOT_SORT_NO")
	private Integer rootSortNo;

	@Column(name="SCREEN_SORT_NO")
	private Integer screenSortNo;

	@Column(name="SYS_SCREEN_ID")
	private Integer sysScreenId;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmMenuResource() {
	}

	public Integer getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
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

	public Integer getRootId() {
		return this.rootId;
	}

	public void setRootId(Integer rootId) {
		this.rootId = rootId;
	}

	public Integer getRootSortNo() {
		return this.rootSortNo;
	}

	public void setRootSortNo(Integer rootSortNo) {
		this.rootSortNo = rootSortNo;
	}

	public Integer getScreenSortNo() {
		return this.screenSortNo;
	}

	public void setScreenSortNo(Integer screenSortNo) {
		this.screenSortNo = screenSortNo;
	}

	public Integer getSysScreenId() {
		return this.sysScreenId;
	}

	public void setSysScreenId(Integer sysScreenId) {
		this.sysScreenId = sysScreenId;
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