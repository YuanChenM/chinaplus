package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the TNM_USER database table.
 * 
 */
@Entity(name="TNM_USER")
public class TnmUser extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_USER_USERID_GENERATOR", sequenceName="SEQ_TNM_USER", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_USER_USERID_GENERATOR")
	@Column(name="USER_ID")
	private Integer userId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="DEFAULT_LANG")
	private Integer defaultLang;

	@Column(name="DEFAULT_OFFICE_ID")
	private Integer defaultOfficeId;

	@Temporal(TemporalType.DATE)
	@Column(name="EXPIRY_DATE")
	private Date expiryDate;

	@Column(name="LAST_LOGIN_TIME")
	private Timestamp lastLoginTime;

	@Column(name="LAST_NG_TIME")
	private Timestamp lastNgTime;

	@Column(name="LOGIN_ID")
	private String loginId;

	@Column(name="MAIL_ADDR")
	private String mailAddr;

	@Column(name="MISS_COUNT")
	private Integer missCount;

	private String pwd;

	@Column(name="\"STATUS\"")
	private Integer status;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="USER_NAME")
	private String userName;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmUser() {
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public Integer getDefaultLang() {
		return this.defaultLang;
	}

	public void setDefaultLang(Integer defaultLang) {
		this.defaultLang = defaultLang;
	}

	public Integer getDefaultOfficeId() {
		return this.defaultOfficeId;
	}

	public void setDefaultOfficeId(Integer defaultOfficeId) {
		this.defaultOfficeId = defaultOfficeId;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Timestamp getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Timestamp getLastNgTime() {
		return this.lastNgTime;
	}

	public void setLastNgTime(Timestamp lastNgTime) {
		this.lastNgTime = lastNgTime;
	}

	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getMailAddr() {
		return this.mailAddr;
	}

	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}

	public Integer getMissCount() {
		return this.missCount;
	}

	public void setMissCount(Integer missCount) {
		this.missCount = missCount;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
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

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}