/**
 * CPMOLS02Entity.
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.entity;

import java.sql.Timestamp;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Office Detail Screen Entity.
 */
public class CPMOLS02Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** OFFICE_ID */
    private String officeId;
    /** TIME_ZONE */
    private String timeZone;
    /** OFFICE_CODE */
    private String officeCode;
    /** OFFICE_NAME */
    private String officeName;
    /** REGION_CODE */
    private String regionCode;
    /** CALENDAR_ID */
    private String calendarId;
    /** CALENDAR_CODE */
    private String calendarCode;
    /** ADDRESS1 */
    private String address1;
    /** ADDRESS2 */
    private String address2;
    /** ADDRESS3 */
    private String address3;
    /** ADDRESS4 */
    private String address4;
    /** POSTAL_CODE */
    private String postalCode;
    /** CONTACT1 */
    private String contact1;
    /** TELEPHONE1 */
    private String telephone1;
    /** FAX1 */
    private String fax1;
    /** EMAIL1 */
    private String email1;
    /** CONTACT2 */
    private String contact2;
    /** TELEPHONE2 */
    private String telephone2;
    /** FAX2 */
    private String fax2;
    /** EMAIL2 */
    private String email2;
    /** INACTIVE_FLAG */
    private String inActiveFlag;
    /** CREATE_BY */
    private String createdBy;
    /** CREATE_DATE */
    private Timestamp createdDate;
    /** UPDATE_BY */
    private String updatedBy;
    /** UPDATE_DATE */
    private Timestamp updatedDate;
    /** version */
    private Integer version;
    /** impStockFlag */
    private String impStockFlag;
    /** dataDateTime */
    private String dataDateTime;
    /** hasOfficeRole */
    private String hasOfficeRole;

    /**
     * Get the timeZone.
     *
     * @return timeZone
     */
    public String getTimeZone() {
        return this.timeZone;
    }

    /**
     * Set the timeZone.
     *
     * @param timeZone timeZone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
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
     * Get the address1.
     * 
     * @return address1
     */
    public String getAddress1() {
        return this.address1;
    }

    /**
     * Set the address1.
     * 
     * @param address1 address1
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * Get the address2.
     * 
     * @return address2
     */
    public String getAddress2() {
        return this.address2;
    }

    /**
     * Set the address2.
     * 
     * @param address2 address2
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * Get the address3.
     * 
     * @return address3
     */
    public String getAddress3() {
        return this.address3;
    }

    /**
     * Set the address3.
     * 
     * @param address3 address3
     */
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    /**
     * Get the address4.
     * 
     * @return address4
     */
    public String getAddress4() {
        return this.address4;
    }

    /**
     * Set the address4.
     * 
     * @param address4 address4
     */
    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    /**
     * Get the postalCode.
     * 
     * @return postalCode
     */
    public String getPostalCode() {
        return this.postalCode;
    }

    /**
     * Set the postalCode.
     * 
     * @param postalCode postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Get the contact1.
     * 
     * @return contact1
     */
    public String getContact1() {
        return this.contact1;
    }

    /**
     * Set the contact1.
     * 
     * @param contact1 contact1
     */
    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    /**
     * Get the telephone1.
     * 
     * @return telephone1
     */
    public String getTelephone1() {
        return this.telephone1;
    }

    /**
     * Set the telephone1.
     * 
     * @param telephone1 telephone1
     */
    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    /**
     * Get the fax1.
     * 
     * @return fax1
     */
    public String getFax1() {
        return this.fax1;
    }

    /**
     * Set the fax1.
     * 
     * @param fax1 fax1
     */
    public void setFax1(String fax1) {
        this.fax1 = fax1;
    }

    /**
     * Get the email1.
     * 
     * @return email1
     */
    public String getEmail1() {
        return this.email1;
    }

    /**
     * Set the email1.
     * 
     * @param email1 email1
     */
    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    /**
     * Get the contact2.
     * 
     * @return contact2
     */
    public String getContact2() {
        return this.contact2;
    }

    /**
     * Set the contact2.
     * 
     * @param contact2 contact2
     */
    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    /**
     * Get the telephone2.
     * 
     * @return telephone2
     */
    public String getTelephone2() {
        return this.telephone2;
    }

    /**
     * Set the telephone2.
     * 
     * @param telephone2 telephone2
     */
    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    /**
     * Get the fax2.
     * 
     * @return fax2
     */
    public String getFax2() {
        return this.fax2;
    }

    /**
     * Set the fax2.
     * 
     * @param fax2 fax2
     */
    public void setFax2(String fax2) {
        this.fax2 = fax2;
    }

    /**
     * Get the email2.
     * 
     * @return email2
     */
    public String getEmail2() {
        return this.email2;
    }

    /**
     * Set the email2.
     * 
     * @param email2 email2
     */
    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    /**
     * Get the createdBy.
     * 
     * @return createdBy
     */
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Set the createdBy.
     * 
     * @param createdBy createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the createdDate.
     * 
     * @return createdDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
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
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Set the updatedBy.
     * 
     * @param updatedBy updatedBy
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the updatedDate.
     * 
     * @return updatedDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
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
     * Get the impStockFlag.
     *
     * @return impStockFlag
     */
    public String getImpStockFlag() {
        return this.impStockFlag;
    }

    /**
     * Set the impStockFlag.
     *
     * @param impStockFlag impStockFlag
     */
    public void setImpStockFlag(String impStockFlag) {
        this.impStockFlag = impStockFlag;
    }

    /**
     * Get the dataDateTime.
     *
     * @return dataDateTime
     */
    public String getDataDateTime() {
        return this.dataDateTime;
    }

    /**
     * Set the dataDateTime.
     *
     * @param dataDateTime dataDateTime
     */
    public void setDataDateTime(String dataDateTime) {
        this.dataDateTime = dataDateTime;
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

    /**
     * Get the hasOfficeRole.
     *
     * @return hasOfficeRole
     */
    public String getHasOfficeRole() {
        return this.hasOfficeRole;
    }

    /**
     * Set the hasOfficeRole.
     *
     * @param hasOfficeRole hasOfficeRole
     */
    public void setHasOfficeRole(String hasOfficeRole) {
        this.hasOfficeRole = hasOfficeRole;
    }

}
