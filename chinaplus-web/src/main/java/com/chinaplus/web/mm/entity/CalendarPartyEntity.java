/**
 * CalendarPartyEntity.java
 * 
 * @screen CPMCMS01
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.entity;

import java.sql.Timestamp;

import com.chinaplus.core.base.BaseEntity;

/**
 * Calendar master
 */
public class CalendarPartyEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** calendarPartyId */
    private Integer calendarPartyId;

    /** calendarId */
    private Integer calendarId;

    /** partyType */
    private Integer partyType;

    /** officeId */
    private Integer officeId;

    /** supplierId */
    private Integer supplierId;

    /** customerId */
    private Integer customerId;

    /** whsId */
    private Integer whsId;

    /** createdBy */
    private Integer createdBy;

    /** createdDate */
    private Timestamp createdDate;

    /** updatedBy */
    private Integer updatedBy;

    /** updatedDate */
    private Timestamp updatedDate;

    /** version */
    private Integer version;

    /**
     * Get the calendarPartyId.
     *
     * @return calendarPartyId
     */
    public Integer getCalendarPartyId() {
        return this.calendarPartyId;
    }

    /**
     * Set the calendarPartyId.
     *
     * @param calendarPartyId calendarPartyId
     */
    public void setCalendarPartyId(Integer calendarPartyId) {
        this.calendarPartyId = calendarPartyId;
    }

    /**
     * Get the calendarId.
     *
     * @return calendarId
     */
    public Integer getCalendarId() {
        return this.calendarId;
    }

    /**
     * Set the calendarId.
     *
     * @param calendarId calendarId
     */
    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    /**
     * Get the partyType.
     *
     * @return partyType
     */
    public Integer getPartyType() {
        return this.partyType;
    }

    /**
     * Set the partyType.
     *
     * @param partyType partyType
     */
    public void setPartyType(Integer partyType) {
        this.partyType = partyType;
    }

    /**
     * Get the officeId.
     *
     * @return officeId
     */
    public Integer getOfficeId() {
        return this.officeId;
    }

    /**
     * Set the officeId.
     *
     * @param officeId officeId
     */
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    /**
     * Get the supplierId.
     *
     * @return supplierId
     */
    public Integer getSupplierId() {
        return this.supplierId;
    }

    /**
     * Set the supplierId.
     *
     * @param supplierId supplierId
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Get the customerId.
     *
     * @return customerId
     */
    public Integer getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     *
     * @param customerId customerId
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * Get the whsId.
     *
     * @return whsId
     */
    public Integer getWhsId() {
        return this.whsId;
    }

    /**
     * Set the whsId.
     *
     * @param whsId whsId
     */
    public void setWhsId(Integer whsId) {
        this.whsId = whsId;
    }

    /**
     * Get the createdBy.
     *
     * @return createdBy
     */
    public Integer getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Set the createdBy.
     *
     * @param createdBy createdBy
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the createdDate.
     *
     * @return createdDate
     */
    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    /**
     * Set the createdDate.
     *
     * @param createdDate createdDate
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get the updatedBy.
     *
     * @return updatedBy
     */
    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Set the updatedBy.
     *
     * @param updatedBy updatedBy
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the updatedDate.
     *
     * @return updatedDate
     */
    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    /**
     * Set the updatedDate.
     *
     * @param updatedDate updatedDate
     */
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * Get the version.
     *
     * @return version
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * Set the version.
     *
     * @param version version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}
