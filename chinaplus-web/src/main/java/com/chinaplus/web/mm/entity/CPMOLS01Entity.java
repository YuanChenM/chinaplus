/**
 * CPMOLS01Entity
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.entity;

import java.sql.Timestamp;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * office list screen
 */
public class CPMOLS01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** officeId */
    private String officeId;

    /** officeCode */
    private String officeCode;

    /** officeName */
    private String officeName;

    /** regionCode */
    private String regionCode;

    /** regionName */
    private String regionName;

    /** calendarId */
    private String calendarId;

    /** calendarCode */
    private String calendarCode;

    /** createBy */
    private String createBy;

    /** createDate */
    private Timestamp createDate;

    /** activeFlag */
    private String inActiveFlag;
    
    /** sendDateTime */
    private String sendDateTime;

    /** rownum */
    private String rownum;

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
     * Get the regionName.
     * 
     * @return regionName
     */
    public String getRegionName() {
        return this.regionName;
    }

    /**
     * Set the regionName.
     * 
     * @param regionName regionName
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * Get the officeCode.
     * 
     * @return officeCode
     */
    public String getOfficeCode() {
        return this.officeCode;
    }

    /**
     * Set the officeCode.
     * 
     * @param officeCode officeCode
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    /**
     * Get the officeName.
     * 
     * @return officeName
     */
    public String getOfficeName() {
        return this.officeName;
    }

    /**
     * Set the officeName.
     * 
     * @param officeName officeName
     */
    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    /**
     * Get the regionCode.
     * 
     * @return regionCode
     */
    public String getRegionCode() {
        return this.regionCode;
    }

    /**
     * Set the regionCode.
     * 
     * @param regionCode regionCode
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * Get the calendarId.
     * 
     * @return calendarId
     */
    public String getCalendarId() {
        return this.calendarId;
    }

    /**
     * Set the calendarId.
     * 
     * @param calendarId calendarId
     */
    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
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
     * Get the createBy.
     * 
     * @return createBy
     */
    public String getCreateBy() {
        return this.createBy;
    }

    /**
     * Set the createBy.
     * 
     * @param createBy createBy
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * Get the createDate.
     * 
     * @return createDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    /**
     * Set the createDate.
     * 
     * @param createDate createDate
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }


    /**
     * Get the sendDateTime.
     *
     * @return sendDateTime
     */
    public String getSendDateTime() {
        return this.sendDateTime;
    }

    /**
     * Set the sendDateTime.
     *
     * @param sendDateTime sendDateTime
     */
    public void setSendDateTime(String sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    /**
     * Get the inActiveFlag.
     *
     * @return inActiveFlag
     */
    public String getInActiveFlag() {
        return this.inActiveFlag;
    }

    /**
     * Set the inActiveFlag.
     *
     * @param inActiveFlag inActiveFlag
     */
    public void setInActiveFlag(String inActiveFlag) {
        this.inActiveFlag = inActiveFlag;
    }

	/**
	 * @return the rownum
	 */
	public String getRownum() {
		return rownum;
	}

	/**
	 * @param rownum the rownum to set
	 */
	public void setRownum(String rownum) {
		this.rownum = rownum;
	}

}
