/**
 * @screen CPCUMS02
 * @author zhang_chi
 */
package com.chinaplus.web.com.entity;

import java.util.List;

import com.chinaplus.common.entity.TnmUser;

/**
 * CPCUMS02Entity.
 */
public class CPCUMS02Entity extends TnmUser {

    /** serialVersionUID. */
    private static final long serialVersionUID = -5869391736847242330L;

    /** officeName. */
    private String officeCode;

    /** roleResource. */
    private List<CPCUMS02DetailEntity> userOfficeRole;

    /**
     * Get the userOfficeRole.
     *
     * @return userOfficeRole
     */
    public List<CPCUMS02DetailEntity> getUserOfficeRole() {
        return this.userOfficeRole;
    }

    /**
     * Set the userOfficeRole.
     *
     * @param userOfficeRole userOfficeRole
     */
    public void setUserOfficeRole(List<CPCUMS02DetailEntity> userOfficeRole) {
        this.userOfficeRole = userOfficeRole;
        
    }

    /**
     * Get the officeCode.
     *
     * @return officeCode
     */
    public String getOfficeCode() {
        return this.officeCode;
    }

    /**
     * Set the officeCode.
     *
     * @param officeCode officeCode
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;

    }
}
