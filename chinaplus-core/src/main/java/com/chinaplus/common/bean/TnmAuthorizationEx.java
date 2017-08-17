package com.chinaplus.common.bean;

import java.io.Serializable;

import com.chinaplus.common.entity.TnmAuthorization;


/**
 * The persistent class for the TNM_SCREEN database table.
 * 
 */
public class TnmAuthorizationEx extends TnmAuthorization implements Serializable {
    
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** authScreenId */
    private String authScreenId;

    /** screenId */
    private String screenId;

    /** projectFlag */
    private Integer projectFlag;

    /** screenUrl */
    private String screenUrl;

    /**
     * Get the authScreenId.
     *
     * @return authScreenId
     */
    public String getAuthScreenId() {
        return this.authScreenId;
    }

    /**
     * Set the authScreenId.
     *
     * @param authScreenId authScreenId
     */
    public void setAuthScreenId(String authScreenId) {
        this.authScreenId = authScreenId;
        
    }

    /**
     * Get the screenId.
     *
     * @return screenId
     */
    public String getScreenId() {
        return this.screenId;
    }

    /**
     * Set the screenId.
     *
     * @param screenId screenId
     */
    public void setScreenId(String screenId) {
        this.screenId = screenId;
        
    }

    /**
     * Get the projectFlag.
     *
     * @return projectFlag
     */
    public Integer getProjectFlag() {
        return this.projectFlag;
    }

    /**
     * Set the projectFlag.
     *
     * @param projectFlag projectFlag
     */
    public void setProjectFlag(Integer projectFlag) {
        this.projectFlag = projectFlag;
        
    }

    /**
     * Get the screenUrl.
     *
     * @return screenUrl
     */
    public String getScreenUrl() {
        return this.screenUrl;
    }

    /**
     * Set the screenUrl.
     *
     * @param screenUrl screenUrl
     */
    public void setScreenUrl(String screenUrl) {
        this.screenUrl = screenUrl;
        
    }

}