/**
 * Customer.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.bean;

import com.chinaplus.common.entity.BaseInterfaceEntity;
import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * SSMS客户实体类
 * SSMSCustomer.
 * 
 * @author yang_jia1
 */
public class Customer extends BaseInterfaceEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 供应商 */
    private String vendorRoute;

    /** 客户编码 */
    private String customerCode;

    /** 客户名称 */
    private String customerName;

    /** */
    private Integer orionPlusFlag;
    
    /** */
    private String vendorRouteSet;
    
    private Integer ssmsCustomerId;
    
    private Integer ifCustomerId; 
    
    /**
     * getVendorRoute
     * 
     * @return vendorRoute
     */
    public String getVendorRoute() {
        return this.vendorRoute;
    }

    /**
     * setVendorRoute
     * 
     * @param vendorRoute vendorRoute
     */
    public void setVendorRoute(String vendorRoute) {
        this.vendorRoute = vendorRoute;
    }

    /**
     * getCustomerCode
     * 
     * @return customerCode
     */
    public String getCustomerCode() {
        return this.customerCode;
    }

    /**
     * setCustomerCode
     * 
     * @param customerCode customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * getCustomerName
     * 
     * @return customerName
     */
    public String getCustomerName() {
        return this.customerName;
    }

    /**
     * setCustomerName
     * 
     * @param customerName customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the orionPlusFlag
     */
    public Integer getOrionPlusFlag() {
        return orionPlusFlag;
    }

    /**
     * @param orionPlusFlag the orionPlusFlag to set
     */
    public void setOrionPlusFlag(Integer orionPlusFlag) {
        this.orionPlusFlag = orionPlusFlag;
    }

    /**
     * @return the vendorRouteSet
     */
    public String getVendorRouteSet() {
        return vendorRouteSet;
    }

    /**
     * @param vendorRouteSet the vendorRouteSet to set
     */
    public void setVendorRouteSet(String vendorRouteSet) {
        this.vendorRouteSet = vendorRouteSet;
    }

    /**
     * @return the ssmsCustomerId
     */
    public Integer getSsmsCustomerId() {
        return ssmsCustomerId;
    }

    /**
     * @param ssmsCustomerId the ssmsCustomerId to set
     */
    public void setSsmsCustomerId(Integer ssmsCustomerId) {
        this.ssmsCustomerId = ssmsCustomerId;
    }

    /**
     * @return the ifCustomerId
     */
    public Integer getIfCustomerId() {
        return ifCustomerId;
    }

    /**
     * @param ifCustomerId the ifCustomerId to set
     */
    public void setIfCustomerId(Integer ifCustomerId) {
        this.ifCustomerId = ifCustomerId;
    }

    @Override
    public int[] getFieldsPosition() {
        int[] filedsPi = { IntDef.INT_ONE, IntDef.INT_TWO, IntDef.INT_TWO, IntDef.INT_TEN, IntDef.INT_THIRTY_FIVE };
        return filedsPi;
    }

    @Override
    public String[] getFieldsName() {
        String[] filedsNm = { "dataId", "expCode", "vendorRoute", "customerCode", "customerName" };
        return filedsNm;
    }

    @Override
    public int getTotalLength() {
        return IntDef.INT_FIFTY;
    }

    /**
     * Customer clone
     * 
     * @return Customer
     * @throws CloneNotSupportedException e
     */
    public Customer clone() throws CloneNotSupportedException {
        return (Customer) this.clone();
    }

}
