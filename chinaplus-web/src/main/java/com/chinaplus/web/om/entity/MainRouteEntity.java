/**
 * CPOOFS01Entity.java
 * 
 * @screen CPOOFF11
 * @author shi_yuxi
 */
package com.chinaplus.web.om.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.chinaplus.core.base.BaseEntity;

/**
 * Upload Order Forecast File
 */
public class MainRouteEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** partsId. */
    private String partsId;

    /** ttcPartsNo. */
    private String ttcPartsNo;

    /** customerId. */
    private String customerId;

    /** customerCode. */
    private String customerCode;

    /** forecastNum. */
    private Integer forecastNum;

    /** impRegion. */
    private String impRegion;

    /** expCalendarCode. */
    private String expCalendarCode;

    /** targetMonth. */
    private Integer targetMonth;

    /** supplierId. */
    private String supplierId;

    /** supplierCode. */
    private String supplierCode;

    /** expPartsId. */
    private String expPartsId;

    /** westPartsNo. */
    private String westPartsNo;

    /** westCustCode. */
    private String westCustCode;

    /** shippingRouteCode. */
    private String shippingRouteCode;

    /** ssmsMainRoute. */
    private String ssmsMainRoute;

    /** ssmsVendorRoute. */
    private String ssmsVendorRoute;

    /** ssmsSupplierCode. */
    private String ssmsSupplierCode;

    /** decimalDigits. */
    private String decimalDigits;

    /** createdBy. */
    private Integer createdBy;

    /** createdDate. */
    private Timestamp createdDate;

    /** updatedBy. */
    private Integer updatedBy;

    /** updatedDate. */
    private Timestamp updatedDate;

    /** version. */
    private Integer version;

    /** partsStatus. */
    private Integer partsStatus;

    /** partsStatus. */
    private Integer inActiveFlag;

    /** srbq. */
    private BigDecimal srbq;

    /** showFlag. */
    private Integer showFlag;

    /**
     * Get the partsId.
     *
     * @return partsId
     */
    public String getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     */
    public void setPartsId(String partsId) {
        this.partsId = partsId;
    }

    /**
     * Get the ttcPartsNo.
     *
     * @return ttcPartsNo
     */
    public String getTtcPartsNo() {
        return this.ttcPartsNo;
    }

    /**
     * Set the ttcPartsNo.
     *
     * @param ttcPartsNo ttcPartsNo
     */
    public void setTtcPartsNo(String ttcPartsNo) {
        this.ttcPartsNo = ttcPartsNo;
    }

    /**
     * Get the customerId.
     *
     * @return customerId
     */
    public String getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     *
     * @param customerId customerId
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Get the impRegion.
     *
     * @return impRegion
     */
    public String getImpRegion() {
        return this.impRegion;
    }

    /**
     * Set the impRegion.
     *
     * @param impRegion impRegion
     */
    public void setImpRegion(String impRegion) {
        this.impRegion = impRegion;
    }

    /**
     * Get the expCalendarCode.
     *
     * @return expCalendarCode
     */
    public String getExpCalendarCode() {
        return this.expCalendarCode;
    }

    /**
     * Set the expCalendarCode.
     *
     * @param expCalendarCode expCalendarCode
     */
    public void setExpCalendarCode(String expCalendarCode) {
        this.expCalendarCode = expCalendarCode;
    }

    /**
     * Get the supplierId.
     *
     * @return supplierId
     */
    public String getSupplierId() {
        return this.supplierId;
    }

    /**
     * Set the supplierId.
     *
     * @param supplierId supplierId
     */
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Get the expPartsId.
     *
     * @return expPartsId
     */
    public String getExpPartsId() {
        return this.expPartsId;
    }

    /**
     * Set the expPartsId.
     *
     * @param expPartsId expPartsId
     */
    public void setExpPartsId(String expPartsId) {
        this.expPartsId = expPartsId;
    }

    /**
     * Get the westPartsNo.
     *
     * @return westPartsNo
     */
    public String getWestPartsNo() {
        return this.westPartsNo;
    }

    /**
     * Set the westPartsNo.
     *
     * @param westPartsNo westPartsNo
     */
    public void setWestPartsNo(String westPartsNo) {
        this.westPartsNo = westPartsNo;
    }

    /**
     * Get the shippingRouteCode.
     *
     * @return shippingRouteCode
     */
    public String getShippingRouteCode() {
        return this.shippingRouteCode;
    }

    /**
     * Set the shippingRouteCode.
     *
     * @param shippingRouteCode shippingRouteCode
     */
    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;
    }

    /**
     * Get the ssmsMainRoute.
     *
     * @return ssmsMainRoute
     */
    public String getSsmsMainRoute() {
        return this.ssmsMainRoute;
    }

    /**
     * Set the ssmsMainRoute.
     *
     * @param ssmsMainRoute ssmsMainRoute
     */
    public void setSsmsMainRoute(String ssmsMainRoute) {
        this.ssmsMainRoute = ssmsMainRoute;
    }

    /**
     * Get the decimalDigits.
     *
     * @return decimalDigits
     */
    public String getDecimalDigits() {
        return this.decimalDigits;
    }

    /**
     * Set the decimalDigits.
     *
     * @param decimalDigits decimalDigits
     */
    public void setDecimalDigits(String decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    /**
     * Get the createdBy.
     *
     * @return createdBy
     */
    public Integer getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Set the createdBy.
     *
     * @param createdBy createdBy
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the createdDate.
     *
     * @return createdDate
     */
    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    /**
     * Set the createdDate.
     *
     * @param createdDate createdDate
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get the updatedBy.
     *
     * @return updatedBy
     */
    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Set the updatedBy.
     *
     * @param updatedBy updatedBy
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the updatedDate.
     *
     * @return updatedDate
     */
    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    /**
     * Set the updatedDate.
     *
     * @param updatedDate updatedDate
     */
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * Get the version.
     *
     * @return version
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * Set the version.
     *
     * @param version version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get the forecastNum.
     *
     * @return forecastNum
     */
    public Integer getForecastNum() {
        return this.forecastNum;
    }

    /**
     * Set the forecastNum.
     *
     * @param forecastNum forecastNum
     */
    public void setForecastNum(Integer forecastNum) {
        this.forecastNum = forecastNum;
    }

    /**
     * Get the partsStatus.
     *
     * @return partsStatus
     */
    public Integer getPartsStatus() {
        return this.partsStatus;
    }

    /**
     * Set the partsStatus.
     *
     * @param partsStatus partsStatus
     */
    public void setPartsStatus(Integer partsStatus) {
        this.partsStatus = partsStatus;
    }

    /**
     * Get the targetMonth.
     *
     * @return targetMonth
     */
    public Integer getTargetMonth() {
        return this.targetMonth;
    }

    /**
     * Set the targetMonth.
     *
     * @param targetMonth targetMonth
     */
    public void setTargetMonth(Integer targetMonth) {
        this.targetMonth = targetMonth;
    }

    /**
     * Get the srbq.
     *
     * @return srbq
     */
    public BigDecimal getSrbq() {
        return this.srbq;
    }

    /**
     * Set the srbq.
     *
     * @param srbq srbq
     */
    public void setSrbq(BigDecimal srbq) {
        this.srbq = srbq;
    }

    /**
     * Get the westCustCode.
     *
     * @return westCustCode
     */
    public String getWestCustCode() {
        return this.westCustCode;
    }

    /**
     * Set the westCustCode.
     *
     * @param westCustCode westCustCode
     */
    public void setWestCustCode(String westCustCode) {
        this.westCustCode = westCustCode;
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
     * Get the supplierCode.
     *
     * @return supplierCode
     */
    public String getSupplierCode() {
        return this.supplierCode;
    }

    /**
     * Set the supplierCode.
     *
     * @param supplierCode supplierCode
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * Get the ssmsVendorRoute.
     *
     * @return ssmsVendorRoute
     */
    public String getSsmsVendorRoute() {
        return this.ssmsVendorRoute;
    }

    /**
     * Set the ssmsVendorRoute.
     *
     * @param ssmsVendorRoute ssmsVendorRoute
     */
    public void setSsmsVendorRoute(String ssmsVendorRoute) {
        this.ssmsVendorRoute = ssmsVendorRoute;
    }

    /**
     * Set the showFlag.
     * 
     * @return the ssmsSupplierCode
     */
    public String getSsmsSupplierCode() {
        return ssmsSupplierCode;
    }

    /**
     * Get the showFlag.
     * 
     * @param ssmsSupplierCode the ssmsSupplierCode to set
     */
    public void setSsmsSupplierCode(String ssmsSupplierCode) {
        this.ssmsSupplierCode = ssmsSupplierCode;
    }

    /**
     * Get the showFlag.
     *
     * @return showFlag
     */
    public Integer getShowFlag() {
        return this.showFlag;
    }

    /**
     * Set the showFlag.
     *
     * @param showFlag showFlag
     */
    public void setShowFlag(Integer showFlag) {
        this.showFlag = showFlag;
    }

    /**
     * Get the inActiveFlag.
     *
     * @return inActiveFlag
     */
    public Integer getInActiveFlag() {
        return this.inActiveFlag;
    }

    /**
     * Set the inActiveFlag.
     *
     * @param inActiveFlag inActiveFlag
     */
    public void setInActiveFlag(Integer inActiveFlag) {
        this.inActiveFlag = inActiveFlag;

    }

}
