/**
 * CPMSRF02Entity.java
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/** 
 * CPMSRF02Entity.
 */
public class CPMSRF02Entity  extends MMCommonEntity{

    /** serialVersionUID */
    private static final long serialVersionUID = 6901683044031317722L;
   
    /** shippingRouteCode  the shippingRouteCode */
    private  String  shippingRouteCode;  

    /** firstEtd  the firstEtd */
    private  Date  firstEtd;
    
    /** lastEtd  the lastEtd */
    private  Date  lastEtd;
    
    /** workingDays  the workingDays */
    private Integer workingDays;
    
    /** expVanningLeadtime  the expVanningLeadtime */
    private Integer expVanningLeadtime;
    
    /** vanningDate  the vanningDate */
    private Integer vanningDate;
    
    /** etdWeek  the etdWeek */
    private Integer etdWeek;
    
    /** etdDate  the etdDate */
    private Integer etdDate;
    
    /** etaDate  the etaDate */
    private Integer etaDate;
    
    /** deliveryLeadtime  the deliveryLeadtime */
    private Integer deliveryLeadtime;
    
    /** impCcLeadtime  the impCcLeadtime */
    private Integer impCcLeadtime;
    
    /** discontinueIndicator  the discontinueIndicator */
    private String  discontinueIndicator;
    
    /**
     * Get the shippingRouteCode.
     *
     * @return shippingRouteCode
     */
    public String getShippingRouteCode() {
        return this.shippingRouteCode;
    }

    /**
     * Set the shippingRouteCode.
     *
     * @param shippingRouteCode shippingRouteCode
     */
    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;
    }
    
    /**
     * Get the firstEtd.
     *
     * @return firstEtd
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getFirstEtd() {
        return this.firstEtd;
    }

    /**
     * Set the firstEtd.
     *
     * @param firstEtd firstEtd
     */
    public void setFirstEtd(Date firstEtd) {
        this.firstEtd = firstEtd;
    }

    /**
     * Get the lastEtd.
     *
     * @return lastEtd
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getLastEtd() {
        return this.lastEtd;
    }

    /**
     * Set the lastEtd.
     *
     * @param lastEtd lastEtd
     */
    public void setLastEtd(Date lastEtd) {
        this.lastEtd = lastEtd;
    }

    /**
     * Get the workingDays.
     *
     * @return workingDays
     */
    public Integer getWorkingDays() {
        return this.workingDays;
    }

    /**
     * Set the workingDays.
     *
     * @param workingDays workingDays
     */
    public void setWorkingDays(Integer workingDays) {
        this.workingDays = workingDays;
    }

    /**
     * Get the expVanningLeadtime.
     *
     * @return expVanningLeadtime
     */
    public Integer getExpVanningLeadtime() {
        return this.expVanningLeadtime;
    }

    /**
     * Set the expVanningLeadtime.
     *
     * @param expVanningLeadtime expVanningLeadtime
     */
    public void setExpVanningLeadtime(Integer expVanningLeadtime) {
        this.expVanningLeadtime = expVanningLeadtime;
    }

    /**
     * Get the vanningDate.
     *
     * @return vanningDate
     */
    public Integer getVanningDate() {
        return this.vanningDate;
    }

    /**
     * Set the vanningDate.
     *
     * @param vanningDate vanningDate
     */
    public void setVanningDate(Integer vanningDate) {
        this.vanningDate = vanningDate;
    }

    /**
     * Get the etdWeek.
     *
     * @return etdWeek
     */
    public Integer getEtdWeek() {
        return this.etdWeek;
    }

    /**
     * Set the etdWeek.
     *
     * @param etdWeek etdWeek
     */
    public void setEtdWeek(Integer etdWeek) {
        this.etdWeek = etdWeek;
    }

    /**
     * Get the etdDate.
     *
     * @return etdDate
     */
    public Integer getEtdDate() {
        return this.etdDate;
    }

    /**
     * Set the etdDate.
     *
     * @param etdDate etdDate
     */
    public void setEtdDate(Integer etdDate) {
        this.etdDate = etdDate;
    }

    /**
     * Get the etaDate.
     *
     * @return etaDate
     */
    public Integer getEtaDate() {
        return this.etaDate;
    }

    /**
     * Set the etaDate.
     *
     * @param etaDate etaDate
     */
    public void setEtaDate(Integer etaDate) {
        this.etaDate = etaDate;
    }

    /**
     * Get the deliveryLeadtime.
     *
     * @return deliveryLeadtime
     */
    public Integer getDeliveryLeadtime() {
        return this.deliveryLeadtime;
    }

    /**
     * Set the deliveryLeadtime.
     *
     * @param deliveryLeadtime deliveryLeadtime
     */
    public void setDeliveryLeadtime(Integer deliveryLeadtime) {
        this.deliveryLeadtime = deliveryLeadtime;
    }

    /**
     * Get the impCcLeadtime.
     *
     * @return impCcLeadtime
     */
    public Integer getImpCcLeadtime() {
        return this.impCcLeadtime;
    }

    /**
     * Set the impCcLeadtime.
     *
     * @param impCcLeadtime impCcLeadtime
     */
    public void setImpCcLeadtime(Integer impCcLeadtime) {
        this.impCcLeadtime = impCcLeadtime;
    }

    /**
     * Get the discontinueIndicator.
     *
     * @return discontinueIndicator
     */
    public String getDiscontinueIndicator() {
        return this.discontinueIndicator;
    }

    /**
     * Set the discontinueIndicator.
     *
     * @param discontinueIndicator discontinueIndicator
     */
    public void setDiscontinueIndicator(String discontinueIndicator) {
        this.discontinueIndicator = discontinueIndicator;
    }

}
