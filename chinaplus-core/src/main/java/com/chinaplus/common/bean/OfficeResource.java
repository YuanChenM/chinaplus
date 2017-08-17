/**
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.bean;

import java.io.Serializable;

/**
 * login user info
 * 
 */
public class OfficeResource implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1488781388949116534L;

    /** The rootId. */
    private Integer sysRootId;

    /** The rootId. */
    private String rootId;

    /** The resourceId. */
    private Integer sysResourceId;

    /** The resourceId. */
    private String resourceId;

    /** The resourceUrl. */
    private String resourceUrl;

    /** The accessLevel. */
    private Integer accessLevel;

    /**
     * Get the resourceId.
     * 
     * @return resourceId
     * 
     * @author Common
     */
    public String getResourceId() {
        return this.resourceId;
    }

    /**
     * Set the resourceId.
     * 
     * @param resourceId resourceId
     * 
     * @author Common
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;

    }

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
     * Get the rootId.
     * 
     * @return rootId
     */
    public String getRootId() {
        return this.rootId;
    }

    /**
     * Set the rootId.
     * 
     * @param rootId rootId
     */
    public void setRootId(String rootId) {
        this.rootId = rootId;

    }

    /**
     * Get the resourceUrl.
     * 
     * @return resourceUrl
     */
    public String getResourceUrl() {
        return this.resourceUrl;
    }

    /**
     * Set the resourceUrl.
     * 
     * @param resourceUrl resourceUrl
     */
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    /**
     * Get the sysRootId.
     *
     * @return sysRootId
     */
    public Integer getSysRootId() {
        return this.sysRootId;
    }

    /**
     * Set the sysRootId.
     *
     * @param sysRootId sysRootId
     */
    public void setSysRootId(Integer sysRootId) {
        this.sysRootId = sysRootId;
        
    }

    /**
     * Get the sysResourceId.
     *
     * @return sysResourceId
     */
    public Integer getSysResourceId() {
        return this.sysResourceId;
    }

    /**
     * Set the sysResourceId.
     *
     * @param sysResourceId sysResourceId
     */
    public void setSysResourceId(Integer sysResourceId) {
        this.sysResourceId = sysResourceId;
        
    }

}
