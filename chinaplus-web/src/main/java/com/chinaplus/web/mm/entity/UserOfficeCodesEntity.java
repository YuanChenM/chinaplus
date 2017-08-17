/**
 * UserOfficeCodesEntity.java
 * 
 * @screen CPMKBF01
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.entity;

import com.chinaplus.core.base.BaseEntity;

/**
 * UserOfficeCodes
 * UserOfficeCodesEntity.
 */
public class UserOfficeCodesEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    private String officeCodes;

    /**
     * Get the officeCodes.
     *
     * @return officeCodes
     */
    public String getOfficeCodes() {
        return this.officeCodes;
    }

    /**
     * Set the officeCodes.
     *
     * @param officeCodes officeCodes
     */
    public void setOfficeCodes(String officeCodes) {
        this.officeCodes = officeCodes;
    }

    /**
     *
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        UserOfficeCodesEntity temp = (UserOfficeCodesEntity) obj;
        if (temp != null && officeCodes.equals(temp.getOfficeCodes())) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
