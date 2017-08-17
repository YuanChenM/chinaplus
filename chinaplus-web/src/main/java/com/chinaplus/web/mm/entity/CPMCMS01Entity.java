/**
 * @screen CPMCMS01Entity
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.entity;

import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * CPMCMS01Entity.
 */
public class CPMCMS01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** country. */
    private String country;

    /** year. */
    private String year;

    /** calenYear. */
    private Integer calenYear;

    /** calendarCode. */
    private String calendarCode;

    /** ttcOfficeCode. */
    private String ttcOfficeCode;

    /** party. */
    private String party;

    /** partyCode. */
    private String partyCode;

    /** calendarDate. */
    private Date calendarDate;

    /** disCalendarDate. */
    private String disCalendarDate;

    /** workingFlag. */
    private Integer workingFlag;

    /** nonWorkingDays. */
    private String[] nonWorkingDays;

    /** officeId */
    private String officeId;

    /** calendarId */
    private int calendarId;

    /** calendarDetailId */
    private int calendarDetailId;
    
    /** partyCode. */
    private String partyCodeCombo;
    
    /** calendarCodeRawValue. */
    private String calendarCodeRawValue;

    /**
     * Get the country.
     *
     * @return country
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * Set the country.
     *
     * @param country country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get the year.
     *
     * @return year
     */
    public String getYear() {
        return this.year;
    }

    /**
     * Set the year.
     *
     * @param year year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Get the calendarCode.
     *
     * @return calendarCode
     */
    public String getCalendarCode() {
        return this.calendarCode;
    }

    /**
     * Set the calendarCode.
     *
     * @param calendarCode calendarCode
     */
    public void setCalendarCode(String calendarCode) {
        this.calendarCode = calendarCode;
    }

    /**
     * Get the calendarDate.
     *
     * @return calendarDate
     */
    public Date getCalendarDate() {
        return this.calendarDate;
    }

    /**
     * Set the calendarDate.
     *
     * @param calendarDate calendarDate
     */
    public void setCalendarDate(Date calendarDate) {
        this.calendarDate = calendarDate;
    }

    /**
     * Get the workingFlag.
     *
     * @return workingFlag
     */
    public Integer getWorkingFlag() {
        return this.workingFlag;
    }

    /**
     * Set the workingFlag.
     *
     * @param workingFlag workingFlag
     */
    public void setWorkingFlag(Integer workingFlag) {
        this.workingFlag = workingFlag;
    }

    /**
     * Get the disCalendarDate.
     *
     * @return disCalendarDate
     */
    public String getDisCalendarDate() {
        return this.disCalendarDate;
    }

    /**
     * Set the disCalendarDate.
     *
     * @param disCalendarDate disCalendarDate
     */
    public void setDisCalendarDate(String disCalendarDate) {
        this.disCalendarDate = disCalendarDate;
    }

    /**
     * Get the calenYear.
     *
     * @return calenYear
     */
    public Integer getCalenYear() {
        return this.calenYear;
    }

    /**
     * Set the calenYear.
     *
     * @param calenYear calenYear
     */
    public void setCalenYear(Integer calenYear) {
        this.calenYear = calenYear;
    }

    /**
     * Get the nonWorkingDays.
     *
     * @return nonWorkingDays
     */
    public String[] getNonWorkingDays() {
        return this.nonWorkingDays;
    }

    /**
     * Set the nonWorkingDays.
     *
     * @param nonWorkingDays nonWorkingDays
     */
    public void setNonWorkingDays(String[] nonWorkingDays) {
        this.nonWorkingDays = nonWorkingDays;
    }

    /**
     * Get the ttcOfficeCode.
     *
     * @return ttcOfficeCode
     */
    public String getTtcOfficeCode() {
        return this.ttcOfficeCode;
    }

    /**
     * Set the ttcOfficeCode.
     *
     * @param ttcOfficeCode ttcOfficeCode
     */
    public void setTtcOfficeCode(String ttcOfficeCode) {
        this.ttcOfficeCode = ttcOfficeCode;
    }

    /**
     * Get the party.
     *
     * @return party
     */
    public String getParty() {
        return this.party;
    }

    /**
     * Set the party.
     *
     * @param party party
     */
    public void setParty(String party) {
        this.party = party;
    }

    /**
     * Get the partyCode.
     *
     * @return partyCode
     */
    public String getPartyCode() {
        return this.partyCode;
    }

    /**
     * Set the partyCode.
     *
     * @param partyCode partyCode
     */
    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    /**
     * Get the officeId.
     *
     * @return officeId
     */
    public String getOfficeId() {
        return this.officeId;
    }

    /**
     * Set the officeId.
     *
     * @param officeId officeId
     */
    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    /**
     * Get the calendarId.
     *
     * @return calendarId
     */
    public int getCalendarId() {
        return this.calendarId;
    }

    /**
     * Set the calendarId.
     *
     * @param calendarId calendarId
     */
    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    /**
     * Get the calendarDetailId.
     *
     * @return calendarDetailId
     */
    public int getCalendarDetailId() {
        return this.calendarDetailId;
    }

    /**
     * Set the calendarDetailId.
     *
     * @param calendarDetailId calendarDetailId
     */
    public void setCalendarDetailId(int calendarDetailId) {
        this.calendarDetailId = calendarDetailId;
    }

    /**
     * Get the partyCodeCombo.
     *
     * @return partyCodeCombo
     */
    public String getPartyCodeCombo() {
        return this.partyCodeCombo;
    }

    /**
     * Set the partyCodeCombo.
     *
     * @param partyCodeCombo partyCodeCombo
     */
    public void setPartyCodeCombo(String partyCodeCombo) {
        this.partyCodeCombo = partyCodeCombo;
    }

    /**
     * Get the calendarCodeRawValue.
     *
     * @return calendarCodeRawValue
     */
    public String getCalendarCodeRawValue() {
        return this.calendarCodeRawValue;
    }

    /**
     * Set the calendarCodeRawValue.
     *
     * @param calendarCodeRawValue calendarCodeRawValue
     */
    public void setCalendarCodeRawValue(String calendarCodeRawValue) {
        this.calendarCodeRawValue = calendarCodeRawValue;
    }
}
