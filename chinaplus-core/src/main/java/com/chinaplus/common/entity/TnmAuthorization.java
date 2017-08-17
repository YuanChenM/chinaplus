package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_AUTHORIZATION database table.
 * 
 */
@Entity(name="TNM_AUTHORIZATION")
public class TnmAuthorization extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_AUTHORIZATION_AUTHORITYID_GENERATOR", sequenceName="SEQ_TNM_AUTHORIZATION", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_AUTHORIZATION_AUTHORITYID_GENERATOR")
	@Column(name="AUTHORITY_ID")
	private Integer authorityId;

	@Column(name="ACCESS_LEVEL")
	private Integer accessLevel;

	@Column(name="AUTH_CODE")
	private String authCode;

	@Column(name="AUTH_SCREEN_ID")
	private Integer authSysScreenId;

	@Column(name="AUTH_TYPE")
	private Integer authType;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="SYS_SCREEN_ID")
	private Integer sysScreenId;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmAuthorization() {
	}

	public Integer getAuthorityId() {
		return this.authorityId;
	}

	public void setAuthorityId(Integer authorityId) {
		this.authorityId = authorityId;
	}

	public Integer getAccessLevel() {
		return this.accessLevel;
	}

	public void setAccessLevel(Integer accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getAuthCode() {
		return this.authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public Integer getAuthSysScreenId() {
		return this.authSysScreenId;
	}

	public void setAuthSysScreenId(Integer authSysScreenId) {
		this.authSysScreenId = authSysScreenId;
	}

	public Integer getAuthType() {
		return this.authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
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