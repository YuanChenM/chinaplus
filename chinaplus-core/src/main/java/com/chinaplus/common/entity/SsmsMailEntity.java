/**
 * SsmsMailEntity.java
 * 
 * @screen CPIIFB01
 * @author cheng_xingfei
 */
package com.chinaplus.common.entity;

import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * Order.
 * 
 * @author cheng_xingfei
 */
public class SsmsMailEntity extends BaseEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** officeId */
    private Integer officeId;

    /** officeCode */
    private String officeCode;

    /** timeZone */
    private String timeZone;

    /** ttcCustomerCode */
    private String ttcCustomerCode;

    /** impPoNo */
    private String impPoNo;

    /** expSoDate */
    private Date expSoDate;

    /** impPoNo */
    private String shippingRouteCode;

    /**
     * @return the officeId
     */
    public Integer getOfficeId() {
        return officeId;
    }

    /**
     * @param officeId the officeId to set
     */
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    /**
     * @return the officeCode
     */
    public String getOfficeCode() {
        return officeCode;
    }

    /**
     * @param officeCode the officeCode to set
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    /**
     * @return the timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * @param timeZone the timeZone to set
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * @return the ttcCustomerCode
     */
    public String getTtcCustomerCode() {
        return ttcCustomerCode;
    }

    /**
     * @param ttcCustomerCode the ttcCustomerCode to set
     */
    public void setTtcCustomerCode(String ttcCustomerCode) {
        this.ttcCustomerCode = ttcCustomerCode;
    }

    /**
     * @return the impPoNo
     */
    public String getImpPoNo() {
        return impPoNo;
    }

    /**
     * @param impPoNo the impPoNo to set
     */
    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
    }

    /**
     * @return the expSoDate
     */
    public Date getExpSoDate() {
        return expSoDate;
    }

    /**
     * @param expSoDate the expSoDate to set
     */
    public void setExpSoDate(Date expSoDate) {
        this.expSoDate = expSoDate;
    }

    /**
     * @return the shippingRouteCode
     */
    public String getShippingRouteCode() {
        return shippingRouteCode;
    }

    /**
     * @param shippingRouteCode the shippingRouteCode to set
     */
    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;
    }
}
