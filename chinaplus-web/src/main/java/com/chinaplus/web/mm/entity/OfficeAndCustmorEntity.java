/**
 * OfficeAndCustmorEntity.java
 * 
 * @screen CPMKBF01
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.entity;

import com.chinaplus.core.base.BaseEntity;

/**
 * OfficeAndCustmorEntity.
 */
public class OfficeAndCustmorEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private String officeCode;

    private String customerCode;

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
     * Get the customerCode.
     *
     * @return customerCode
     */
    public String getCustomerCode() {
        return this.customerCode;
    }

    /**
     * Set the customerCode.
     *
     * @param customerCode customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     *
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        OfficeAndCustmorEntity e = (OfficeAndCustmorEntity) obj;
        if (e.getOfficeCode() != null && e.getOfficeCode().equals(officeCode) && e.getCustomerCode() != null
                && e.getCustomerCode().equals(customerCode)) {
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
