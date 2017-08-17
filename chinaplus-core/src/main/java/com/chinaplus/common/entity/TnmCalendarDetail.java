package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the TNM_CALENDAR_DETAIL database table.
 * 
 */
@Entity(name="TNM_CALENDAR_DETAIL")
public class TnmCalendarDetail extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_CALENDAR_DETAIL_CALENDARDETAILID_GENERATOR", sequenceName="SEQ_TNM_CALENDAR_DETAIL", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_CALENDAR_DETAIL_CALENDARDETAILID_GENERATOR")
	@Column(name="CALENDAR_DETAIL_ID")
	private Integer calendarDetailId;

	@Temporal(TemporalType.DATE)
	@Column(name="CALENDAR_DATE")
	private Date calendarDate;

	@Column(name="CALENDAR_YEAR")
	private String calendarYear;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	@Column(name="WORKING_FLAG")
	private Integer workingFlag;

	//bi-directional many-to-one association to TnmCalendarMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CALENDAR_ID")
	private TnmCalendarMaster tnmCalendarMaster;

	public TnmCalendarDetail() {
	}

	public Integer getCalendarDetailId() {
		return this.calendarDetailId;
	}

	public void setCalendarDetailId(Integer calendarDetailId) {
		this.calendarDetailId = calendarDetailId;
	}

	public Date getCalendarDate() {
		return this.calendarDate;
	}

	public void setCalendarDate(Date calendarDate) {
		this.calendarDate = calendarDate;
	}

	public String getCalendarYear() {
		return this.calendarYear;
	}

	public void setCalendarYear(String calendarYear) {
		this.calendarYear = calendarYear;
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

	public Integer getWorkingFlag() {
		return this.workingFlag;
	}

	public void setWorkingFlag(Integer workingFlag) {
		this.workingFlag = workingFlag;
	}

	public TnmCalendarMaster getTnmCalendarMaster() {
		return this.tnmCalendarMaster;
	}

	public void setTnmCalendarMaster(TnmCalendarMaster tnmCalendarMaster) {
		this.tnmCalendarMaster = tnmCalendarMaster;
	}

}