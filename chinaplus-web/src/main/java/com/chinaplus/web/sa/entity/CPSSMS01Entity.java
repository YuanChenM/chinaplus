/**
 * @screen CPSSMS01Entity
 * @author li_feng
 */
package com.chinaplus.web.sa.entity;

import java.sql.Timestamp;

import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * CPSSMS01Entity.
 */
public class CPSSMS01Entity extends BaseEntity{

    /** serialVersionUID */
    private static final long serialVersionUID = 755406863058248580L;
    
    /** ipoOrderNo */
    private String ipoOrderNo;
    
    /** customerOrderNo */
    private String customerOrderNo;
    
    /** expCountry */
    private String expCountry;
    
    /** ttcSupplierCode */
    private String ttcSupplierCode;
    
    /** ttcCustomerCode */
    private String ttcCustomerCode;
    
    /** transportMode */
    private String transportMode;
    
    /** revisionReason */
    private String revisionReason;
    
    /** uploadID */
    private String uploadID;
    
    /** ssId */
    private Integer ssId;
    
    /** uploadDateTime */
    private Timestamp uploadDateTime;
    
    /** uploadDateTimeFrom */
    private Timestamp uploadDateTimeFrom;
    
    /** uploadDateTimeTo */
    private Timestamp uploadDateTimeTo;
    
    /** rowNum */
    private Integer rownumb;
    

    /**
     * Get the ipoOrderNo.
     *
     * @return ipoOrderNo
     */
    public String getIpoOrderNo() {
        return this.ipoOrderNo;
    }

    /**
     * Set the ipoOrderNo.
     *
     * @param ipoOrderNo ipoOrderNo
     */
    public void setIpoOrderNo(String ipoOrderNo) {
        this.ipoOrderNo = ipoOrderNo;
    }

    /**
     * Get the customerOrderNo.
     *
     * @return customerOrderNo
     */
    public String getCustomerOrderNo() {
        return this.customerOrderNo;
    }

    /**
     * Set the customerOrderNo.
     *
     * @param customerOrderNo customerOrderNo
     */
    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    /**
     * Get the expCountry.
     *
     * @return expCountry
     */
    public String getExpCountry() {
        return this.expCountry;
    }

    /**
     * Set the expCountry.
     *
     * @param expCountry expCountry
     */
    public void setExpCountry(String expCountry) {
        this.expCountry = expCountry;
    }

    /**
     * Get the ttcSupplierCode.
     *
     * @return ttcSupplierCode
     */
    public String getTtcSupplierCode() {
        return this.ttcSupplierCode;
    }

    /**
     * Set the ttcSupplierCode.
     *
     * @param ttcSupplierCode ttcSupplierCode
     */
    public void setTtcSupplierCode(String ttcSupplierCode) {
        this.ttcSupplierCode = ttcSupplierCode;
    }

    /**
     * Get the ttcCustomerCode.
     *
     * @return ttcCustomerCode
     */
    public String getTtcCustomerCode() {
        return this.ttcCustomerCode;
    }

    /**
     * Set the ttcCustomerCode.
     *
     * @param ttcCustomerCode ttcCustomerCode
     */
    public void setTtcCustomerCode(String ttcCustomerCode) {
        this.ttcCustomerCode = ttcCustomerCode;
    }

    /**
     * Get the transportMode.
     *
     * @return transportMode
     */
    public String getTransportMode() {
        return this.transportMode;
    }

    /**
     * Set the transportMode.
     *
     * @param transportMode transportMode
     */
    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * Get the revisionReason.
     *
     * @return revisionReason
     */
    public String getRevisionReason() {
        return this.revisionReason;
    }

    /**
     * Set the revisionReason.
     *
     * @param revisionReason revisionReason
     */
    public void setRevisionReason(String revisionReason) {
        this.revisionReason = revisionReason;
    }

    /**
     * Get the uploadID.
     *
     * @return uploadID
     */
    public String getUploadID() {
        return this.uploadID;
    }

    /**
     * Set the uploadID.
     *
     * @param uploadID uploadID
     */
    public void setUploadID(String uploadID) {
        this.uploadID = uploadID;
    }

    /**
     * Get the uploadDateTime.
     *
     * @return uploadDateTime
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getUploadDateTime() {
        return this.uploadDateTime;
    }

    /**
     * Set the uploadDateTime.
     *
     * @param uploadDateTime uploadDateTime
     */
    public void setUploadDateTime(Timestamp uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }

    /**
     * Get the uploadDateTimeFrom.
     *
     * @return uploadDateTimeFrom
     */
    public Timestamp getUploadDateTimeFrom() {
        return this.uploadDateTimeFrom;
    }

    /**
     * Set the uploadDateTimeFrom.
     *
     * @param uploadDateTimeFrom uploadDateTimeFrom
     */
    public void setUploadDateTimeFrom(Timestamp uploadDateTimeFrom) {
        this.uploadDateTimeFrom = uploadDateTimeFrom;
    }

    /**
     * Get the uploadDateTimeTo.
     *
     * @return uploadDateTimeTo
     */
    public Timestamp getUploadDateTimeTo() {
        return this.uploadDateTimeTo;
    }

    /**
     * Set the uploadDateTimeTo.
     *
     * @param uploadDateTimeTo uploadDateTimeTo
     */
    public void setUploadDateTimeTo(Timestamp uploadDateTimeTo) {
        this.uploadDateTimeTo = uploadDateTimeTo;
    }

    /**
     * Get the ssId.
     *
     * @return ssId
     */
    public Integer getSsId() {
        return this.ssId;
    }

    /**
     * Set the ssId.
     *
     * @param ssId ssId
     */
    public void setSsId(Integer ssId) {
        this.ssId = ssId;
    }

    /**
     * Get the rownumb.
     *
     * @return rownumb
     */
    public Integer getRownumb() {
        return this.rownumb;
    }

    /**
     * Set the rownumb.
     *
     * @param rownumb rownumb
     */
    public void setRownumb(Integer rownumb) {
        this.rownumb = rownumb;
    }
    
}