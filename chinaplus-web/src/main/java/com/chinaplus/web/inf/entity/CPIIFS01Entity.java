/**
 * @screen CPIIFS01
 * @author zhang_chi
 */
package com.chinaplus.web.inf.entity;

import java.sql.Timestamp;

import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPIIFS01Entity.
 */
public class CPIIFS01Entity extends BaseEntity {
    
    /** serialVersionUID. */
    private static final long serialVersionUID = -2078974163416049033L;

    /** batchId */
    private Integer batchId;    
    
    /** officeCode */
    private String officeCode;   

    /** batchType */
    private Integer batchType;
    
    /** batchTypeName */
    private String batchTypeName;    
    
    /** dateTime */
    private Timestamp dateTime;     
    
    /** status */
    private Integer status;
    
    /**
     * Get the batchId.
     *
     * @return batchId
     */
    public Integer getBatchId() {
        return this.batchId;
    }
    /**
     * Set the batchId.
     *
     * @param batchId batchId
     */
    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
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
     * Get the batchTypeName.
     *
     * @return batchTypeName
     */
    public String getBatchTypeName() {
        return this.batchTypeName;
    }
    /**
     * Set the batchTypeName.
     *
     * @param batchTypeName batchTypeName
     */
    public void setBatchTypeName(String batchTypeName) {
        this.batchTypeName = batchTypeName;
    }
    /**
     * Get the dateTime.
     *
     * @return dateTime
     */
    @JsonSerialize(using= JsonDateTimeSerializer.class)
    public Timestamp getDateTime() {
        return this.dateTime;
    }
    /**
     * Set the dateTime.
     *
     * @param dateTime dateTime
     */
    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
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

}
