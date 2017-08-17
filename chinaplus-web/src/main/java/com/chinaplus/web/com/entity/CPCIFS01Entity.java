/**
 * CPCIFS01Entity.java
 * 
 * @screen CPCIFS01
 * @author gu_chengchen
 */
package com.chinaplus.web.com.entity;

import java.sql.Timestamp;

import com.chinaplus.core.base.BaseEntity;

/**
 * Information Screen Entity.
 */
public class CPCIFS01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Office ID */
    private Integer officeId;

    /** Office Code */
    private String officeCode;

    /** Batch Type */
    private Integer batchType;

    /** IF Date Time */
    private Timestamp ifDateTime;

    /** IF Batch Status */
    private Integer ifBatchStatus;

    /** Batch ID */
    private String batchId;

    /** Process Date */
    private Timestamp processDate;

    /** Batch Job Status */
    private Integer batchJobStatus;

    /** Time Zone */
    private String timeZone;

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
     * Get the batchType.
     *
     * @return batchType
     */
    public Integer getBatchType() {
        return this.batchType;
    }

    /**
     * Set the batchType.
     *
     * @param batchType batchType
     */
    public void setBatchType(Integer batchType) {
        this.batchType = batchType;
    }

    /**
     * Get the ifDateTime.
     *
     * @return ifDateTime
     */
    public Timestamp getIfDateTime() {
        return this.ifDateTime;
    }

    /**
     * Set the ifDateTime.
     *
     * @param ifDateTime ifDateTime
     */
    public void setIfDateTime(Timestamp ifDateTime) {
        this.ifDateTime = ifDateTime;
    }

    /**
     * Get the ifBatchStatus.
     *
     * @return ifBatchStatus
     */
    public Integer getIfBatchStatus() {
        return this.ifBatchStatus;
    }

    /**
     * Set the ifBatchStatus.
     *
     * @param ifBatchStatus ifBatchStatus
     */
    public void setIfBatchStatus(Integer ifBatchStatus) {
        this.ifBatchStatus = ifBatchStatus;
    }

    /**
     * Get the batchId.
     *
     * @return batchId
     */
    public String getBatchId() {
        return this.batchId;
    }

    /**
     * Set the batchId.
     *
     * @param batchId batchId
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    /**
     * Get the processDate.
     *
     * @return processDate
     */
    public Timestamp getProcessDate() {
        return this.processDate;
    }

    /**
     * Set the processDate.
     *
     * @param processDate processDate
     */
    public void setProcessDate(Timestamp processDate) {
        this.processDate = processDate;
    }

    /**
     * Get the batchJobStatus.
     *
     * @return batchJobStatus
     */
    public Integer getBatchJobStatus() {
        return this.batchJobStatus;
    }

    /**
     * Set the batchJobStatus.
     *
     * @param batchJobStatus batchJobStatus
     */
    public void setBatchJobStatus(Integer batchJobStatus) {
        this.batchJobStatus = batchJobStatus;
    }

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

}
