/**
 * @screen CPCUMS01
 * @author zhang_chi
 */
package com.chinaplus.web.com.entity;

import com.chinaplus.common.entity.TnmUser;

/**
 * CPCUMS01Entity.
 */
public class CPCUMS01Entity extends TnmUser {

    /** serialVersionUID. */
    private static final long serialVersionUID = -2078974163416049033L;

    /** defaultOfficeName. */
    private String defaultOfficeName;

    /** Customer Code Set */
    private String customerCodeSet;

    /**
     * Get the defaultOfficeName.
     *
     * @return defaultOfficeName
     */
    public String getDefaultOfficeName() {
        return this.defaultOfficeName;
    }

    /**
     * Set the defaultOfficeName.
     *
     * @param defaultOfficeName defaultOfficeName
     */
    public void setDefaultOfficeName(String defaultOfficeName) {
        this.defaultOfficeName = defaultOfficeName;

    }

    /**
     * Get the customerCodeSet.
     *
     * @return customerCodeSet
     */
    public String getCustomerCodeSet() {
        return this.customerCodeSet;
    }

    /**
     * Set the customerCodeSet.
     *
     * @param customerCodeSet customerCodeSet
     */
    public void setCustomerCodeSet(String customerCodeSet) {
        this.customerCodeSet = customerCodeSet;
    }

}
