package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_OFFICE database table.
 * 
 */
@Entity(name="TNM_OFFICE")
public class TnmOffice extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_OFFICE_OFFICEID_GENERATOR", sequenceName="SEQ_TNM_OFFICE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_OFFICE_OFFICEID_GENERATOR")
	@Column(name="OFFICE_ID")
	private Integer officeId;

	private String address1;

	private String address2;

	private String address3;

	private String address4;

	private String contact1;

	private String contact2;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	private String email1;

	private String email2;

	private String fax1;

	private String fax2;

	@Column(name="IMP_STOCK_FLAG")
	private Integer impStockFlag;

	@Column(name="INACTIVE_FLAG")
	private Integer inactiveFlag;

	@Column(name="OFFICE_CODE")
	private String officeCode;

	@Column(name="OFFICE_NAME")
	private String officeName;

	@Column(name="POSTAL_CODE")
	private String postalCode;

	@Column(name="REGION_CODE")
	private String regionCode;

	@Column(name="SEND_DATA_TIME")
	private String sendDataTime;

	private String telephone1;

	private String telephone2;

	@Column(name="TIME_ZONE")
	private String timeZone;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmOffice() {
	}

	public Integer getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
	}

	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return this.address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress4() {
		return this.address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public String getContact1() {
		return this.contact1;
	}

	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}

	public String getContact2() {
		return this.contact2;
	}

	public void setContact2(String contact2) {
		this.contact2 = contact2;
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

	public String getEmail1() {
		return this.email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return this.email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getFax1() {
		return this.fax1;
	}

	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}

	public String getFax2() {
		return this.fax2;
	}

	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}

	public Integer getImpStockFlag() {
		return this.impStockFlag;
	}

	public void setImpStockFlag(Integer impStockFlag) {
		this.impStockFlag = impStockFlag;
	}

	public Integer getInactiveFlag() {
		return this.inactiveFlag;
	}

	public void setInactiveFlag(Integer inactiveFlag) {
		this.inactiveFlag = inactiveFlag;
	}

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getOfficeName() {
		return this.officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getRegionCode() {
		return this.regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getSendDataTime() {
		return this.sendDataTime;
	}

	public void setSendDataTime(String sendDataTime) {
		this.sendDataTime = sendDataTime;
	}

	public String getTelephone1() {
		return this.telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public String getTelephone2() {
		return this.telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	public String getTimeZone() {
		return this.timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
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