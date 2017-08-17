/**
 * Parts.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.chinaplus.common.entity.BaseInterfaceEntity;
import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * 
 * Parts.
 * 
 * @author yang_jia1
 */
public class Parts extends BaseInterfaceEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    private Integer ifPartsId;
    
    private Integer expPartsId;
    
    private String represetativeRoute;
    
    private String vendorRoute;

    private String partsNo;

    private String displayPN;

    private String customerPartsNo;

    private String TTCPartsName;

    private String customerCode;

    private String supplierCode;

    private BigDecimal spq;

    private BigDecimal orderLot;

    private String uom;

    private String transportMode;

    private String buildOutFlag;

    private Integer buildOutFlagInt;
    
    private Integer vaildFlag;
    
    private Integer inactiveFlag;
    
    private Integer supplierId;

    private String officeCode;

    private String timeZone;
    
    private List<Timestamp> ifDateTimeList;

    /**
     * @return the ifPartsId
     */
    public Integer getIfPartsId() {
        return ifPartsId;
    }

    /**
     * @param ifPartsId the ifPartsId to set
     */
    public void setIfPartsId(Integer ifPartsId) {
        this.ifPartsId = ifPartsId;
    }

    /**
     * @return the expPartsId
     */
    public Integer getExpPartsId() {
        return expPartsId;
    }

    /**
     * @param expPartsId the expPartsId to set
     */
    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;
    }

    /**
     * @return the represetativeRoute
     */
    public String getRepresetativeRoute() {
        return represetativeRoute;
    }

    /**
     * @param represetativeRoute the represetativeRoute to set
     */
    public void setRepresetativeRoute(String represetativeRoute) {
        this.represetativeRoute = represetativeRoute;
    }

    /**
     * @return the vendorRoute
     */
    public String getVendorRoute() {
        return vendorRoute;
    }

    /**
     * @param vendorRoute the vendorRoute to set
     */
    public void setVendorRoute(String vendorRoute) {
        this.vendorRoute = vendorRoute;
    }

    /**
     * @return the partsNo
     */
    public String getPartsNo() {
        return partsNo;
    }

    /**
     * @param partsNo the partsNo to set
     */
    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }

    /**
     * @return the displayPN
     */
    public String getDisplayPN() {
        return displayPN;
    }

    /**
     * @param displayPN the displayPN to set
     */
    public void setDisplayPN(String displayPN) {
        this.displayPN = displayPN;
    }

    /**
     * @return the customerPartsNo
     */
    public String getCustomerPartsNo() {
        return customerPartsNo;
    }

    /**
     * @param customerPartsNo the customerPartsNo to set
     */
    public void setCustomerPartsNo(String customerPartsNo) {
        this.customerPartsNo = customerPartsNo;
    }

    /**
     * @return the tTCPartsName
     */
    public String getTTCPartsName() {
        return TTCPartsName;
    }

    /**
     * @param tTCPartsName the tTCPartsName to set
     */
    public void setTTCPartsName(String tTCPartsName) {
        TTCPartsName = tTCPartsName;
    }

    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * @return the supplierCode
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    /**
     * @param supplierCode the supplierCode to set
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * @return the spq
     */
    public BigDecimal getSpq() {
        return spq;
    }

    /**
     * @param spq the spq to set
     */
    public void setSpq(BigDecimal spq) {
        this.spq = spq;
    }

    /**
     * @return the orderLot
     */
    public BigDecimal getOrderLot() {
        return orderLot;
    }

    /**
     * @param orderLot the orderLot to set
     */
    public void setOrderLot(BigDecimal orderLot) {
        this.orderLot = orderLot;
    }

    /**
     * @return the uom
     */
    public String getUom() {
        return uom;
    }

    /**
     * @param uom the uom to set
     */
    public void setUom(String uom) {
        this.uom = uom;
    }

    /**
     * @return the transportMode
     */
    public String getTransportMode() {
        return transportMode;
    }

    /**
     * @param transportMode the transportMode to set
     */
    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * @return the buildOutFlag
     */
    public String getBuildOutFlag() {
        return buildOutFlag;
    }

    /**
     * @param buildOutFlag the buildOutFlag to set
     */
    public void setBuildOutFlag(String buildOutFlag) {
        this.buildOutFlag = buildOutFlag;
    }

    /**
     * @return the buildOutFlagInt
     */
    public Integer getBuildOutFlagInt() {
        return buildOutFlagInt;
    }

    /**
     * @param buildOutFlagInt the buildOutFlagInt to set
     */
    public void setBuildOutFlagInt(Integer buildOutFlagInt) {
        this.buildOutFlagInt = buildOutFlagInt;
    }

    /**
     * @return the vaildFlag
     */
    public Integer getVaildFlag() {
        return vaildFlag;
    }

    /**
     * @param vaildFlag the vaildFlag to set
     */
    public void setVaildFlag(Integer vaildFlag) {
        this.vaildFlag = vaildFlag;
    }

    /**
     * @return the inactiveFlag
     */
    public Integer getInactiveFlag() {
        return inactiveFlag;
    }

    /**
     * @param inactiveFlag the inactiveFlag to set
     */
    public void setInactiveFlag(Integer inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
    }

    /**
     * @return the supplierId
     */
    public Integer getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
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
     * @return the ifDateTimeList
     */
    public List<Timestamp> getIfDateTimeList() {
        return ifDateTimeList;
    }

    /**
     * @param ifDateTimeList the ifDateTimeList to set
     */
    public void setIfDateTimeList(List<Timestamp> ifDateTimeList) {
        this.ifDateTimeList = ifDateTimeList;
    }

    @Override
    public int[] getFieldsPosition() {
        int[] filedsPi = { IntDef.INT_ONE, IntDef.INT_TWO, IntDef.INT_TWO, IntDef.INT_EIGHTEEN, IntDef.INT_EIGHTEEN,
            IntDef.INT_EIGHTEEN, IntDef.INT_FOURTY, IntDef.INT_TEN, IntDef.INT_TEN, IntDef.INT_TEN, IntDef.INT_TEN,
            IntDef.INT_THREE, IntDef.INT_ONE, IntDef.INT_TWO };
        return filedsPi;
    }

    @Override
    public String[] getFieldsName() {
        String[] filedsNm = { "dataId", "expCode", "represetativeRoute", "partsNo", "displayPN", 
            "customerPartsNo", "TTCPartsName", "customerCode", "supplierCode",
            "spq","orderLot","uom","transportMode","buildOutFlag" };
        return filedsNm;
    }

    @Override
    public int getTotalLength() {
        return IntDef.INT_ONE_HUNDRED_AND_FOURTY_FIVE;
    }
}
