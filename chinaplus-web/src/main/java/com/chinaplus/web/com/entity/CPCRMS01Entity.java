/**
 * CPCRMS01Entity.java
 * 
 * @screen CPCRMS01
 * @author shi_yuxi
 */
package com.chinaplus.web.com.entity;

import com.chinaplus.common.entity.TnmRole;

/**
 * CPCRMS01Entity.
 */
public class CPCRMS01Entity extends TnmRole {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** defaultOfficeName. */
    private String updateUser;

    /**
     * Get the updateUser.
     *
     * @return updateUser
     */
    public String getUpdateUser() {
        return this.updateUser;
    }

    /**
     * Set the updateUser.
     *
     * @param updateUser updateUser
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;

    }

}
