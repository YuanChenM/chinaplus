/**
 * UserCustmorEntity.java
 * 
 * @screen CPMKBF01
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.entity;

import com.chinaplus.core.base.BaseEntity;

/**
 * UserCustmorEntity.
 */
public class UserCustmorEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private String custmorCode;

    private int bussinessPattern;

    /**
     * Get the custmorCode.
     *
     * @return custmorCode
     */
    public String getCustmorCode() {
        return this.custmorCode;
    }

    /**
     * Set the custmorCode.
     *
     * @param custmorCode custmorCode
     */
    public void setCustmorCode(String custmorCode) {
        this.custmorCode = custmorCode;
    }

    /**
     * Get the bussinessPattern.
     *
     * @return bussinessPattern
     */
    public int getBussinessPattern() {
        return this.bussinessPattern;
    }

    /**
     * Set the bussinessPattern.
     *
     * @param bussinessPattern bussinessPattern
     */
    public void setBussinessPattern(int bussinessPattern) {
        this.bussinessPattern = bussinessPattern;
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

    /**
     *
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        try {
            UserCustmorEntity temp = (UserCustmorEntity) obj;
            // 2代表 aixin
            if (temp != null && custmorCode.equals(temp.getCustmorCode())
                    && bussinessPattern == temp.getBussinessPattern()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
