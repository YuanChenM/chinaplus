package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_USER_OFFICE_ROLE database table.
 * 
 */
@Entity(name="TNM_USER_OFFICE_ROLE")
public class TnmUserOfficeRole extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_USER_OFFICE_ROLE_USEROFFICEROLEID_GENERATOR", sequenceName="SEQ_TNM_USER_OFFICE_ROLE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_USER_OFFICE_ROLE_USEROFFICEROLEID_GENERATOR")
	@Column(name="USER_OFFICE_ROLE_ID")
	private Integer userOfficeRoleId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="OFFICE_ID")
	private Integer officeId;

	@Column(name="ROLE_ID")
	private Integer roleId;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="USER_ID")
	private Integer userId;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmUserOfficeRole() {
	}

	public Integer getUserOfficeRoleId() {
		return this.userOfficeRoleId;
	}

	public void setUserOfficeRoleId(Integer userOfficeRoleId) {
		this.userOfficeRoleId = userOfficeRoleId;
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

	public Integer getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}