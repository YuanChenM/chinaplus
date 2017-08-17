/**
 * CPOCFS01Entity.java
 * 
 * @screen CPOCFS01
 * @author li_feng
 */
package com.chinaplus.web.om.entity;

import java.sql.Timestamp;
import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.common.util.JsonMonthSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Customer Forecast Screen Entity.
 */
public class CPOCFS01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = -7413332070781688941L;

    /** CFC_ID */
    private Integer forecastId;

    /** CFC_NO */
    private String forecastNo;

    /** FC_DATE */
    private Date fcReceivedDate;

    /** CUSTOMER_CD */
    private String tTCCustomerCode;
    
    /** FIRST_FC_MONTH */
    private String cusFcMonthRange;

    /** FIRST_FC_MONTH */
    private String firstFcMonth;

    /** LAST_FC_MONTH */
    private String lastFcMonth;

    /** REMARK */
    private String remark;

    /** STATUS */
    private Integer status;

    /** UPLOADED_BY */
    private Integer uploadID;

    /** UPLOADED_DATE */
    private Timestamp uploadTime;
    
    /** USER_ID */
    private String userId;

    /**
     * Get the forecastId.
     *
     * @return forecastId
     */
    public Integer getForecastId() {
        return this.forecastId;
    }

    /**
     * Set the forecastId.
     *
     * @param forecastId forecastId
     */
    public void setForecastId(Integer forecastId) {
        this.forecastId = forecastId;
    }

    /**
     * Get the forecastNo.
     *
     * @return forecastNo
     */
    public String getForecastNo() {
        return this.forecastNo;
    }

    /**
     * Set the forecastNo.
     *
     * @param forecastNo forecastNo
     */
    public void setForecastNo(String forecastNo) {
        this.forecastNo = forecastNo;
    }

    /**
     * Get the fcReceivedDate.
     *
     * @return fcReceivedDate
     */
    @JsonSerialize(using = JsonDateSerializer.class) 
    public Date getFcReceivedDate() {
        return this.fcReceivedDate;
    }

    /**
     * Set the fcReceivedDate.
     *
     * @param fcReceivedDate fcReceivedDate
     */
    public void setFcReceivedDate(Date fcReceivedDate) {
        this.fcReceivedDate = fcReceivedDate;
    }

    /**
     * Get the tTCCustomerCode.
     *
     * @return tTCCustomerCode
     */
    public String gettTCCustomerCode() {
        return this.tTCCustomerCode;
    }

    /**
     * Set the tTCCustomerCode.
     *
     * @param tTCCustomerCode tTCCustomerCode
     */
    public void settTCCustomerCode(String tTCCustomerCode) {
        this.tTCCustomerCode = tTCCustomerCode;
    }

    /**
     * Get the firstFcMonth.
     *
     * @return firstFcMonth
     */
    @JsonSerialize(using = JsonMonthSerializer.class) 
    public String getFirstFcMonth() {
        return this.firstFcMonth;
    }

    /**
     * Set the firstFcMonth.
     *
     * @param firstFcMonth firstFcMonth
     */
    public void setFirstFcMonth(String firstFcMonth) {
        this.firstFcMonth = firstFcMonth;
    }

    /**
     * Get the lastFcMonth.
     *
     * @return lastFcMonth
     */
    @JsonSerialize(using = JsonMonthSerializer.class) 
    public String getLastFcMonth() {
        return this.lastFcMonth;
    }

    /**
     * Set the lastFcMonth.
     *
     * @param lastFcMonth lastFcMonth
     */
    public void setLastFcMonth(String lastFcMonth) {
        this.lastFcMonth = lastFcMonth;
    }

    /**
     * Get the remark.
     *
     * @return remark
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * Set the remark.
     *
     * @param remark remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Get the status.
     *
     * @return status
     */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * Set the status.
     *
     * @param status status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Get the uploadID.
     *
     * @return uploadID
     */
    public Integer getUploadID() {
        return this.uploadID;
    }

    /**
     * Set the uploadID.
     *
     * @param uploadID uploadID
     */
    public void setUploadID(Integer uploadID) {
        this.uploadID = uploadID;
    }

    /**
     * Get the uploadTime.
     *
     * @return uploadTime
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class) 
    public Timestamp getUploadTime() {
        return this.uploadTime;
    }

    /**
     * Set the uploadTime.
     *
     * @param uploadTime uploadTime
     */
    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    /**
     * Get the cusFcMonthRange.
     *
     * @return cusFcMonthRange
     */
    public String getCusFcMonthRange() {
        return this.cusFcMonthRange;
    }

    /**
     * Set the cusFcMonthRange.
     *
     * @param cusFcMonthRange cusFcMonthRange
     */
    public void setCusFcMonthRange(String cusFcMonthRange) {
        this.cusFcMonthRange = cusFcMonthRange;
    }

    /**
     * Get the userId.
     *
     * @return userId
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Set the userId.
     *
     * @param userId userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

}