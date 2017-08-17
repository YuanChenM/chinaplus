package com.chinaplus.web.com.entity;

import com.chinaplus.common.entity.TnmUserOfficeRole;


/**
 * YMCRLS03Entity.
 */
public class CPCUMS02DetailEntity extends TnmUserOfficeRole {

    /** serialVersionUID. */
    private static final long serialVersionUID = 7622418315340737082L;

    /** officeCode. */
    private String officeCode;
    
    /** roleName. */
    private String roleName;


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

    /**
     * Get the roleName.
     *
     * @return roleName
     */
    public String getRoleName() {
        return this.roleName;
    }

    /**
     * Set the roleName.
     *
     * @param roleName roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
        
    }

}
