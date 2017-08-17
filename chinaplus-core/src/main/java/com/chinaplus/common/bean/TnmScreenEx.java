package com.chinaplus.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.chinaplus.common.entity.TnmScreen;


/**
 * The persistent class for the TNM_SCREEN database table.
 * 
 */
public class TnmScreenEx extends TnmScreen implements Serializable {
    
    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** authSysScreenId. */
    private Integer authSysScreenId;

    /** authScreenId. */
    private String authScreenId;

    /** authList. */
    private List<TnmAuthorizationEx> authList;

    /**
     * Get the authSysScreenId.
     *
     * @return authSysScreenId
     */
    public Integer getAuthSysScreenId() {
        return this.authSysScreenId;
    }

    /**
     * Set the authSysScreenId.
     *
     * @param authSysScreenId authSysScreenId
     */
    public void setAuthSysScreenId(Integer authSysScreenId) {
        this.authSysScreenId = authSysScreenId;
        
    }

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
     * Get the authList.
     *
     * @return authList
     */
    public List<TnmAuthorizationEx> getAuthList() {
        return this.authList;
    }

    /**
     * Set the authList.
     *
     * @param authList authList
     */
    public void setAuthList(List<TnmAuthorizationEx> authList) {
        this.authList = authList;
        
    }

    /**
     * Set the authList.
     *
     * @param authInfo authInfo
     */
    public void setAuthList(TnmAuthorizationEx authInfo) {
        if(this.authList == null) {
            this.authList = new ArrayList<TnmAuthorizationEx>();
        }
        this.authList.add(authInfo);     
    }

}