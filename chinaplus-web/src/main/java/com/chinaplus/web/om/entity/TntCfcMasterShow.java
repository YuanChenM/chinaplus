/**
 * TntCfcMasterShow.java
 * 
 * @screen CPOOCS01
 * @author shi_yuxi
 */
package com.chinaplus.web.om.entity;

import java.util.Date;

import com.chinaplus.common.entity.TntCfcMaster;
import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.common.util.JsonMonthSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Order Calculation Supporting Data Report Download Screen
 */
public class TntCfcMasterShow extends TntCfcMaster {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** uploadByName */
    private String uploadedByName;
    
    /** fcDateLan */
    private Date fcDateLan;
    
    /** uploadedDateLan */
    private Date uploadedDateLan;
    
    /** firstFcMonthLan */
    private String firstFcMonthLan;
    
    /** lastFcMonthLan */
    private String lastFcMonthLan;


    /**
     * Get the fcDateLan.
     *
     * @return fcDateLan
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getFcDateLan() {
        return this.fcDateLan;
    }

    /**
     * Set the fcDateLan.
     *
     * @param fcDateLan fcDateLan
     */
    public void setFcDateLan(Date fcDateLan) {
        this.fcDateLan = fcDateLan;
    }

    /**
     * Get the uploadedDateLan.
     *
     * @return uploadedDateLan
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getUploadedDateLan() {
        return this.uploadedDateLan;
    }

    /**
     * Set the uploadedDateLan.
     *
     * @param uploadedDateLan uploadedDateLan
     */
    public void setUploadedDateLan(Date uploadedDateLan) {
        this.uploadedDateLan = uploadedDateLan;
    }


    /**
     * Get the uploadedByName.
     *
     * @return uploadedByName
     */
    public String getUploadedByName() {
        return this.uploadedByName;
    }

    /**
     * Set the uploadedByName.
     *
     * @param uploadedByName uploadedByName
     */
    public void setUploadedByName(String uploadedByName) {
        this.uploadedByName = uploadedByName;
    }

    /**
     * Get the firstFcMonthLan.
     *
     * @return firstFcMonthLan
     */
    @JsonSerialize(using = JsonMonthSerializer.class)
    public String getFirstFcMonthLan() {
        return this.firstFcMonthLan;
    }

    /**
     * Set the firstFcMonthLan.
     *
     * @param firstFcMonthLan firstFcMonthLan
     */
    public void setFirstFcMonthLan(String firstFcMonthLan) {
        this.firstFcMonthLan = firstFcMonthLan;
    }

    /**
     * Get the lastFcMonthLan.
     *
     * @return lastFcMonthLan
     */
    @JsonSerialize(using = JsonMonthSerializer.class)
    public String getLastFcMonthLan() {
        return this.lastFcMonthLan;
    }

    /**
     * Set the lastFcMonthLan.
     *
     * @param lastFcMonthLan lastFcMonthLan
     */
    public void setLastFcMonthLan(String lastFcMonthLan) {
        this.lastFcMonthLan = lastFcMonthLan;
    }
    
    
}
