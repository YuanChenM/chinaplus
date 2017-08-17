package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_CALENDAR_PARTY database table.
 * 
 */
@Entity(name="TNM_CALENDAR_PARTY")
public class TnmCalendarParty extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_CALENDAR_PARTY_CALENDARPARTYID_GENERATOR", sequenceName="SEQ_TNM_CALENDAR_PARTY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_CALENDAR_PARTY_CALENDARPARTYID_GENERATOR")
	@Column(name="CALENDAR_PARTY_ID")
	private Integer calendarPartyId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMER_ID")
	private Integer customerId;

	@Column(name="OFFICE_ID")
	private Integer officeId;

	@Column(name="PARTY_TYPE")
	private Integer partyType;

	@Column(name="SUPPLIER_ID")
	private Integer supplierId;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	@Column(name="WHS_ID")
	private Integer whsId;

	//bi-directional many-to-one association to TnmCalendarMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CALENDAR_ID")
	private TnmCalendarMaster tnmCalendarMaster;

	public TnmCalendarParty() {
	}

	public Integer getCalendarPartyId() {
		return this.calendarPartyId;
	}

	public void setCalendarPartyId(Integer calendarPartyId) {
		this.calendarPartyId = calendarPartyId;
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

	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
	}

	public Integer getPartyType() {
		return this.partyType;
	}

	public void setPartyType(Integer partyType) {
		this.partyType = partyType;
	}

	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
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

	public Integer getWhsId() {
		return this.whsId;
	}

	public void setWhsId(Integer whsId) {
		this.whsId = whsId;
	}

	public TnmCalendarMaster getTnmCalendarMaster() {
		return this.tnmCalendarMaster;
	}

	public void setTnmCalendarMaster(TnmCalendarMaster tnmCalendarMaster) {
		this.tnmCalendarMaster = tnmCalendarMaster;
	}

}