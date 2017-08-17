/**
 * @screen YMCRDS01
 * @author liu_yinchuan
 */
package com.chinaplus.web.com.entity;

import java.io.Serializable;
import java.util.List;

import com.chinaplus.common.bean.TnmScreenEx;

/**
 * MainmenuAuthEntity.
 */
public class MainmenuAuthEntity implements Serializable  {
    
    /** serialVersionUID. */
    private static final long serialVersionUID = -2078974163416049033L;

    /** accessLevel. */
    private Integer accessLevel;
    
    /** activeFlag. */
    private Integer activeFlag;
    
    /** mainResourceId. */
    private String mainResourceId;
    
    /** officeLst. */
    private List<OfficeAuthEntity> officeLst;
    
    /** screenAuthInfo. */
    private List<TnmScreenEx> screenAuthInfo;

    /**
     * Get the accessLevel.
     *
     * @return accessLevel
     */
    public Integer getAccessLevel() {
        return this.accessLevel;
    }

    /**
     * Set the accessLevel.
     *
     * @param accessLevel accessLevel
     */
    public void setAccessLevel(Integer accessLevel) {
        this.accessLevel = accessLevel;
        
    }

    /**
     * Get the mainResourceId.
     *
     * @return mainResourceId
     */
    public String getMainResourceId() {
        return this.mainResourceId;
    }

    /**
     * Set the mainResourceId.
     *
     * @param mainResourceId mainResourceId
     */
    public void setMainResourceId(String mainResourceId) {
        this.mainResourceId = mainResourceId;
        
    }

    /**
     * Get the screenAuthInfo.
     *
     * @return screenAuthInfo
     */
    public List<TnmScreenEx> getScreenAuthInfo() {
        return this.screenAuthInfo;
    }

    /**
     * Set the screenAuthInfo.
     *
     * @param screenAuthInfo screenAuthInfo
     */
    public void setScreenAuthInfo(List<TnmScreenEx> screenAuthInfo) {
        this.screenAuthInfo = screenAuthInfo;
        
    }

    /**
     * Get the activeFlag.
     *
     * @return activeFlag
     */
    public Integer getActiveFlag() {
        return this.activeFlag;
    }

    /**
     * Set the activeFlag.
     *
     * @param activeFlag activeFlag
     */
    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
        
    }

    /**
     * Get the officeLst.
     *
     * @return officeLst
     */
    public List<OfficeAuthEntity> getOfficeLst() {
        return this.officeLst;
    }

    /**
     * Set the officeLst.
     *
     * @param officeLst officeLst
     */
    public void setOfficeLst(List<OfficeAuthEntity> officeLst) {
        this.officeLst = officeLst;
        
    }
}
