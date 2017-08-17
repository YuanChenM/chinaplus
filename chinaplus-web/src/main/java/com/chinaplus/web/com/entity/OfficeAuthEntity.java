/**
 * @screen YMCRDS01
 * @author liu_yinchuan
 */
package com.chinaplus.web.com.entity;

import java.io.Serializable;

/**
 * MainmenuAuthEntity.
 */
public class OfficeAuthEntity implements Serializable  {

    /** serialVersionUID */
    private static final long serialVersionUID = 6809553770863004633L;
    
    /** officeId */
    private Integer officeId;
    
    /** accessLevel */
    private Integer accessLevel;

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
}
