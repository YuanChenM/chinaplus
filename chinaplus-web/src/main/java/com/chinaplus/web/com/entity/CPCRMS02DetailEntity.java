/**
 * CPCRMS02DetailEntity.java
 * 
 * @screen YMCRDS01
 * @author liu_yinchuan
 */
package com.chinaplus.web.com.entity;

import com.chinaplus.core.base.BaseEntity;

/**
 * YMCRDS01DetailEntity.
 */
public class CPCRMS02DetailEntity extends BaseEntity {

    /** serialVersionUID. */
    private static final long serialVersionUID = -2078974163416049033L;

    /** customerCode. */
    private String roleId;

    /** resourceId. */
    private Integer sysResourceId;

    /** resourceId. */
    private String resourceId;

    /** min select able level. */
    private Integer minSelectedLevel;

    /** accessLevel. */
    private Integer accessLevel;

    /**
     * Get the roleId.
     * 
     * @return roleId
     */
    public String getRoleId() {
        return this.roleId;
    }

    /**
     * Set the roleId.
     * 
     * @param roleId roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;

    }

    /**
     * Get the resourceId.
     * 
     * @return resourceId
     */
    public String getResourceId() {
        return this.resourceId;
    }

    /**
     * Set the resourceId.
     * 
     * @param resourceId resourceId
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

    /**
     * Get the minSelectedLevel.
     *
     * @return minSelectedLevel
     */
    public Integer getMinSelectedLevel() {
        return this.minSelectedLevel;
    }

    /**
     * Set the minSelectedLevel.
     *
     * @param minSelectedLevel minSelectedLevel
     */
    public void setMinSelectedLevel(Integer minSelectedLevel) {
        this.minSelectedLevel = minSelectedLevel;

    }

}
