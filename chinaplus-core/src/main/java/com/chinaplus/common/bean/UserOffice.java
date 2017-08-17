/**
 * Login User info.
 * 
 * @screen common
 * @author Common
 */
package com.chinaplus.common.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * login user info.
 * 
 * @author Common
 * 
 */
public class UserOffice implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1870382081212763054L;

    /** The officeCode. */
    private Integer officeId;

    /** The officeCode. */
    private String officeCode;

    /** The regionCode. */
    private String regionCode;

    /** The timezone. */
    private String timezone;

    /** The timezone. */
    private Timestamp impSyncTime;

    /** The inactiveFlag. */
    private Integer inactiveFlag;
    
    /** The resourceList. */
    private List<OfficeResource> resourceList;
    
    /** The current office vvFlag. */
    private boolean vvFlag;
    
    /** The current office aisinFlag. */
    private boolean aisinFlag;
    

    /** The BusinessPattern. */
    private List<BusinessPattern>  businessPatternList;
    
    /**
     * Get the businessPatternList.
     *
     * @return businessPatternList
     */
    public List<BusinessPattern> getBusinessPatternList() {
        return this.businessPatternList;
    }

    /**
     * Set the businessPatternList.
     *
     * @param businessPatternList businessPatternList
     */
    public void setBusinessPatternList(List<BusinessPattern> businessPatternList) {
        this.businessPatternList = businessPatternList;
    }

    /**
     * Get the vvFlag.
     *
     * @return vvFlag
     */
    public boolean getVvFlag() {
        return this.vvFlag;
    }

    /**
     * Set the vvFlag.
     *
     * @param vvFlag vvFlag
     */
    public void setVvFlag(boolean vvFlag) {
        this.vvFlag = vvFlag;
    }

    /**
     * Get the aisinFlag.
     *
     * @return aisinFlag
     */
    public boolean getAisinFlag() {
        return this.aisinFlag;
    }

    /**
     * Set the aisinFlag.
     *
     * @param aisinFlag aisinFlag
     */
    public void setAisinFlag(boolean aisinFlag) {
        this.aisinFlag = aisinFlag;
    }

    /**
     * Get the officeCode.
     * 
     * @return officeCode
     * 
     * @author Common
     */
    public String getOfficeCode() {
        return this.officeCode;
    }

    /**
     * Set the officeCode.
     * 
     * @param officeCode officeCode
     * 
     * @author Common
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;

    }

    /**
     * Get the regionCode.
     * 
     * @return regionCode
     * 
     * @author Common
     */
    public String getRegionCode() {
        return this.regionCode;
    }

    /**
     * Set the regionCode.
     * 
     * @param regionCode regionCode
     * 
     * @author Common
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
        
    }

    /**
     * Get the resourceList.
     *
     * @return resourceList
     */
    public List<OfficeResource> getResourceList() {
        return this.resourceList;
    }

    /**
     * Set the resourceList.
     *
     * @param resourceList resourceList
     */
    public void setResourceList(List<OfficeResource> resourceList) {
        this.resourceList = resourceList;
        
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
     * Get the timezone.
     *
     * @return timezone
     */
    public String getTimezone() {
        return this.timezone;
    }

    /**
     * Set the timezone.
     *
     * @param timezone timezone
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
        
    }

    /**
     * Get the inactiveFlag.
     *
     * @return inactiveFlag
     */
    public Integer getInactiveFlag() {
        return this.inactiveFlag;
    }

    /**
     * Set the inactiveFlag.
     *
     * @param inactiveFlag inactiveFlag
     */
    public void setInactiveFlag(Integer inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
        
    }

    /**
     * Get the impSyncTime.
     *
     * @return impSyncTime
     *
     * @author liu_yinchuan
     */
    public Timestamp getImpSyncTime() {
        return this.impSyncTime;
    }

    /**
     * Set the impSyncTime.
     *
     * @param impSyncTime impSyncTime
     *
     * @author liu_yinchuan
     */
    public void setImpSyncTime(Timestamp impSyncTime) {
        this.impSyncTime = impSyncTime;
        
    }

}
