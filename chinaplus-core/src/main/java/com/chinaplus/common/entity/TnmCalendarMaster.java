package com.chinaplus.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 * The persistent class for the TNM_CALENDAR_MASTER database table.
 * 
 */
@Entity(name = "TNM_CALENDAR_MASTER")
public class TnmCalendarMaster extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNM_CALENDAR_MASTER_CALENDARID_GENERATOR",
        sequenceName = "SEQ_TNM_CALENDAR_MASTER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNM_CALENDAR_MASTER_CALENDARID_GENERATOR")
    @Column(name = "CALENDAR_ID")
    private Integer calendarId;

    @Column(name = "INACTIVE_FLAG")
    private Integer inactiveFlag;

    @Column(name = "CALENDAR_CODE")
    private String calendarCode;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    private String description;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "REGION_CODE")
    private String regionCode;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    // bi-directional many-to-one association to TnmCalendarDetail
    @OneToMany(mappedBy = "tnmCalendarMaster")
    private List<TnmCalendarDetail> tnmCalendarDetails;

    // bi-directional many-to-one association to TnmCalendarParty
    @OneToMany(mappedBy = "tnmCalendarMaster")
    private List<TnmCalendarParty> tnmCalendarParties;

    public TnmCalendarMaster() {}

    public Integer getCalendarId() {
        return this.calendarId;
    }

    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    public Integer getInactiveFlag() {
        return this.inactiveFlag;
    }

    public void setInactiveFlag(Integer inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
    }

    public String getCalendarCode() {
        return this.calendarCode;
    }

    public void setCalendarCode(String calendarCode) {
        this.calendarCode = calendarCode;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
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

    public List<TnmCalendarDetail> getTnmCalendarDetails() {
        return this.tnmCalendarDetails;
    }

    public void setTnmCalendarDetails(List<TnmCalendarDetail> tnmCalendarDetails) {
        this.tnmCalendarDetails = tnmCalendarDetails;
    }

    public TnmCalendarDetail addTnmCalendarDetail(TnmCalendarDetail tnmCalendarDetail) {
        getTnmCalendarDetails().add(tnmCalendarDetail);
        tnmCalendarDetail.setTnmCalendarMaster(this);

        return tnmCalendarDetail;
    }

    public TnmCalendarDetail removeTnmCalendarDetail(TnmCalendarDetail tnmCalendarDetail) {
        getTnmCalendarDetails().remove(tnmCalendarDetail);
        tnmCalendarDetail.setTnmCalendarMaster(null);

        return tnmCalendarDetail;
    }

    public List<TnmCalendarParty> getTnmCalendarParties() {
        return this.tnmCalendarParties;
    }

    public void setTnmCalendarParties(List<TnmCalendarParty> tnmCalendarParties) {
        this.tnmCalendarParties = tnmCalendarParties;
    }

    public TnmCalendarParty addTnmCalendarParty(TnmCalendarParty tnmCalendarParty) {
        getTnmCalendarParties().add(tnmCalendarParty);
        tnmCalendarParty.setTnmCalendarMaster(this);

        return tnmCalendarParty;
    }

    public TnmCalendarParty removeTnmCalendarParty(TnmCalendarParty tnmCalendarParty) {
        getTnmCalendarParties().remove(tnmCalendarParty);
        tnmCalendarParty.setTnmCalendarMaster(null);

        return tnmCalendarParty;
    }

}