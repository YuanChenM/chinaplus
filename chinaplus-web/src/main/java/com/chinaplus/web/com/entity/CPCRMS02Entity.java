/**
 * CPCRMS02Entity.java
 * 
 * @screen CPCRMS02Entity
 * @author shi_yuxi
 */
package com.chinaplus.web.com.entity;

import java.util.List;

import com.chinaplus.common.entity.TnmRole;

/**
 * YMCRDS01Entity.
 */
public class CPCRMS02Entity extends TnmRole {

    /** serialVersionUID. */
    private static final long serialVersionUID = -2078974163416049033L;

    /** roleResource. */
    private List<CPCRMS02DetailEntity> roleResource;

    /**
     * Get the roleResource.
     *
     * @return roleResource
     */
    public List<CPCRMS02DetailEntity> getRoleResource() {
        return this.roleResource;
    }

    /**
     * Set the roleResource.
     *
     * @param roleResource roleResource
     */
    public void setRoleResource(List<CPCRMS02DetailEntity> roleResource) {
        this.roleResource = roleResource;

    }

}
