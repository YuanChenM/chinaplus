package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNT_DT_IP database table.
 * 
 */
@Entity(name="TNT_DT_IP")
public class TntDtIp extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_DT_IP_DTIPID_GENERATOR", sequenceName="SEQ_TNT_DT_IP", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_DT_IP_DTIPID_GENERATOR")
	@Column(name="DT_IP_ID")
	private Integer dtIpId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="DT_NO")
	private String dtNo;

	@Column(name="IP_NO")
	private String ipNo;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TntDtIp() {
	}

	public Integer getDtIpId() {
		return this.dtIpId;
	}

	public void setDtIpId(Integer dtIpId) {
		this.dtIpId = dtIpId;
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

	public String getIpNo() {
		return this.ipNo;
	}

	public void setIpNo(String ipNo) {
		this.ipNo = ipNo;
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