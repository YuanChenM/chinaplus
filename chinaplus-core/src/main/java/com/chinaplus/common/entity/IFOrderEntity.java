/**
 * Order.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.common.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * 
 * Order.
 * @author yang_jia1
 */
public class IFOrderEntity extends BaseInterfaceEntity{
    
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** orderDetailId */
    private Integer orderDetailId;

    /** mainRoute */
    private String mainRoute;

    /** vendorRoute */
    private String vendorRoute;

    /** impPoNo */
    private String impPoNo;

    /** ssmsSupplierCode */
    private String ssmsSupplierCode;

    /** soDate */
    private String soDate;

    /** poDate */
    private String poDate;

    /** expSoDate */
    private Date expSoDate;

    /** expPoDate */
    private Date expPoDate;

    /** supplierCodeAccounting */
    private String supplierCodeAccounting;

    /** transportModeStr */
    private String transportModeStr;

    /** originalTransportMode */
    private Integer originalTransportMode;

    /** expPoNo */
    private String expPoNo;

    /** expPoItemNo */
    private String expPoItemNo;

    /** ssmsCustomerCode */
    private String ssmsCustomerCode;

    /** customerOrderNo */
    private String customerOrderNo;

    /** ttcPartsNo */
    private String ttcPartsNo;

    /** orderQty */
    private BigDecimal orderQty;

    /** originalQty */
    private BigDecimal originalQty;

    /** orderLot */
    private BigDecimal orderLot;

    /** expInbPlanDate */
    private String expInbPlanDateStr;

    /** expInbPlanDate */
    private Date expInbPlanDate;

    /** orderType */
    private String orderType;

    /** orderId */
    private Integer orderId;

    /** expRegion */
    private String expRegion;

    /** ifOrderId */
    private Integer ifOrderId;

    /** spq */
    private BigDecimal spq;

    /** ssId */
    private Integer ssId;

    /** ssPlanId */
    private Integer ssPlanId;

    /** shippingRouteCode */
    private String shippingRouteCode;

    /** orderStatusId */
    private Integer orderStatusId;

    /** delayAdjustmentPattern */
    private Integer delayAdjustmentPattern;

    /** partsIdList */
    private List<Integer> partsIdList;

    /** qty */
    private BigDecimal qty;

    /** ssMasterKey */
    private String ssMasterKey;

    /** ssPlanKey */
    private String ssPlanKey;

    /** ssPartsId */
    private Integer ssPartsId;

    /** cnt */
    private int cnt;

    /** sortNum */
    private Integer sortNum;

    /** revisionVersion */
    private Integer revisionVersion;

    /** originalVersion */
    private Integer originalVersion;

    /** newPlanFlag */
    private Integer newPlanFlag;

    /** timeZone */
    private String timeZone;
    
    /** ttcCustomerCode */
    private String ttcCustomerCode;
    
    /** officeCode */
    private String officeCode;

    /** orderDay */
    private Integer orderDay;

    /** calendarId */
    private Integer calendarId;
    
    /** orderMonth */
    private String orderMonth;
    
    /** forceCompletedBy */
    private Integer forceCompletedBy;
    
    /** minExpInbPlanDate */
    private String minExpInbPlanDate;

    /**
     * @return the orderDetailId
     */
    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    /**
     * @param orderDetailId the orderDetailId to set
     */
    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    /**
     * @return the mainRoute
     */
    public String getMainRoute() {
        return mainRoute;
    }

    /**
     * @param mainRoute the mainRoute to set
     */
    public void setMainRoute(String mainRoute) {
        this.mainRoute = mainRoute;
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
     * @return the ssmsSupplierCode
     */
    public String getSsmsSupplierCode() {
        return ssmsSupplierCode;
    }

    /**
     * @param ssmsSupplierCode the ssmsSupplierCode to set
     */
    public void setSsmsSupplierCode(String ssmsSupplierCode) {
        this.ssmsSupplierCode = ssmsSupplierCode;
    }

    /**
     * @return the soDate
     */
    public String getSoDate() {
        return soDate;
    }

    /**
     * @param soDate the soDate to set
     */
    public void setSoDate(String soDate) {
        this.soDate = soDate;
    }

    /**
     * @return the poDate
     */
    public String getPoDate() {
        return poDate;
    }

    /**
     * @param poDate the poDate to set
     */
    public void setPoDate(String poDate) {
        this.poDate = poDate;
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
     * @return the expPoDate
     */
    public Date getExpPoDate() {
        return expPoDate;
    }

    /**
     * @param expPoDate the expPoDate to set
     */
    public void setExpPoDate(Date expPoDate) {
        this.expPoDate = expPoDate;
    }

    /**
     * @return the supplierCodeAccounting
     */
    public String getSupplierCodeAccounting() {
        return supplierCodeAccounting;
    }

    /**
     * @param supplierCodeAccounting the supplierCodeAccounting to set
     */
    public void setSupplierCodeAccounting(String supplierCodeAccounting) {
        this.supplierCodeAccounting = supplierCodeAccounting;
    }

    /**
     * @return the transportModeStr
     */
    public String getTransportModeStr() {
        return transportModeStr;
    }

    /**
     * @param transportModeStr the transportModeStr to set
     */
    public void setTransportModeStr(String transportModeStr) {
        this.transportModeStr = transportModeStr;
    }

    /**
     * @return the originalTransportMode
     */
    public Integer getOriginalTransportMode() {
        return originalTransportMode;
    }

    /**
     * @param originalTransportMode the originalTransportMode to set
     */
    public void setOriginalTransportMode(Integer originalTransportMode) {
        this.originalTransportMode = originalTransportMode;
    }

    /**
     * @return the expPoNo
     */
    public String getExpPoNo() {
        return expPoNo;
    }

    /**
     * @param expPoNo the expPoNo to set
     */
    public void setExpPoNo(String expPoNo) {
        this.expPoNo = expPoNo;
    }

    /**
     * @return the expPoItemNo
     */
    public String getExpPoItemNo() {
        return expPoItemNo;
    }

    /**
     * @param expPoItemNo the expPoItemNo to set
     */
    public void setExpPoItemNo(String expPoItemNo) {
        this.expPoItemNo = expPoItemNo;
    }

    /**
     * @return the ssmsCustomerCode
     */
    public String getSsmsCustomerCode() {
        return ssmsCustomerCode;
    }

    /**
     * @param ssmsCustomerCode the ssmsCustomerCode to set
     */
    public void setSsmsCustomerCode(String ssmsCustomerCode) {
        this.ssmsCustomerCode = ssmsCustomerCode;
    }

    /**
     * @return the customerOrderNo
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    /**
     * @param customerOrderNo the customerOrderNo to set
     */
    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    /**
     * @return the ttcPartsNo
     */
    public String getTtcPartsNo() {
        return ttcPartsNo;
    }

    /**
     * @param ttcPartsNo the ttcPartsNo to set
     */
    public void setTtcPartsNo(String ttcPartsNo) {
        this.ttcPartsNo = ttcPartsNo;
    }

    /**
     * @return the orderQty
     */
    public BigDecimal getOrderQty() {
        return orderQty;
    }

    /**
     * @param orderQty the orderQty to set
     */
    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    /**
     * @return the originalQty
     */
    public BigDecimal getOriginalQty() {
        return originalQty;
    }

    /**
     * @param originalQty the originalQty to set
     */
    public void setOriginalQty(BigDecimal originalQty) {
        this.originalQty = originalQty;
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
     * @return the expInbPlanDateStr
     */
    public String getExpInbPlanDateStr() {
        return expInbPlanDateStr;
    }

    /**
     * @param expInbPlanDateStr the expInbPlanDateStr to set
     */
    public void setExpInbPlanDateStr(String expInbPlanDateStr) {
        this.expInbPlanDateStr = expInbPlanDateStr;
    }

    /**
     * @return the expInbPlanDate
     */
    public Date getExpInbPlanDate() {
        return expInbPlanDate;
    }

    /**
     * @param expInbPlanDate the expInbPlanDate to set
     */
    public void setExpInbPlanDate(Date expInbPlanDate) {
        this.expInbPlanDate = expInbPlanDate;
    }

    /**
     * @return the orderType
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * @param orderType the orderType to set
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * @return the orderId
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the expRegion
     */
    public String getExpRegion() {
        return expRegion;
    }

    /**
     * @param expRegion the expRegion to set
     */
    public void setExpRegion(String expRegion) {
        this.expRegion = expRegion;
    }

    /**
     * @return the ifOrderId
     */
    public Integer getIfOrderId() {
        return ifOrderId;
    }

    /**
     * @param ifOrderId the ifOrderId to set
     */
    public void setIfOrderId(Integer ifOrderId) {
        this.ifOrderId = ifOrderId;
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
     * @return the ssId
     */
    public Integer getSsId() {
        return ssId;
    }

    /**
     * @param ssId the ssId to set
     */
    public void setSsId(Integer ssId) {
        this.ssId = ssId;
    }

    /**
     * @return the ssPlanId
     */
    public Integer getSsPlanId() {
        return ssPlanId;
    }

    /**
     * @param ssPlanId the ssPlanId to set
     */
    public void setSsPlanId(Integer ssPlanId) {
        this.ssPlanId = ssPlanId;
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

    /**
     * @return the orderStatusId
     */
    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    /**
     * @param orderStatusId the orderStatusId to set
     */
    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    /**
     * @return the delayAdjustmentPattern
     */
    public Integer getDelayAdjustmentPattern() {
        return delayAdjustmentPattern;
    }

    /**
     * @param delayAdjustmentPattern the delayAdjustmentPattern to set
     */
    public void setDelayAdjustmentPattern(Integer delayAdjustmentPattern) {
        this.delayAdjustmentPattern = delayAdjustmentPattern;
    }

    /**
     * @return the partsIdList
     */
    public List<Integer> getPartsIdList() {
        return partsIdList;
    }

    /**
     * @param partsIdList the partsIdList to set
     */
    public void setPartsIdList(List<Integer> partsIdList) {
        this.partsIdList = partsIdList;
    }

    /**
     * @return the qty
     */
    public BigDecimal getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * @return the ssMasterKey
     */
    public String getSsMasterKey() {
        return ssMasterKey;
    }

    /**
     * @param ssMasterKey the ssMasterKey to set
     */
    public void setSsMasterKey(String ssMasterKey) {
        this.ssMasterKey = ssMasterKey;
    }

    /**
     * @return the ssPlanKey
     */
    public String getSsPlanKey() {
        return ssPlanKey;
    }

    /**
     * @param ssPlanKey the ssPlanKey to set
     */
    public void setSsPlanKey(String ssPlanKey) {
        this.ssPlanKey = ssPlanKey;
    }

    /**
     * @return the ssPartsId
     */
    public Integer getSsPartsId() {
        return ssPartsId;
    }

    /**
     * @param ssPartsId the ssPartsId to set
     */
    public void setSsPartsId(Integer ssPartsId) {
        this.ssPartsId = ssPartsId;
    }

    /**
     * @return the cnt
     */
    public int getCnt() {
        return cnt;
    }

    /**
     * @param cnt the cnt to set
     */
    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    /**
     * @return the sortNum
     */
    public Integer getSortNum() {
        return sortNum;
    }

    /**
     * @param sortNum the sortNum to set
     */
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    /**
     * @return the revisionVersion
     */
    public Integer getRevisionVersion() {
        return revisionVersion;
    }

    /**
     * @param revisionVersion the revisionVersion to set
     */
    public void setRevisionVersion(Integer revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    /**
     * @return the originalVersion
     */
    public Integer getOriginalVersion() {
        return originalVersion;
    }

    /**
     * @param originalVersion the originalVersion to set
     */
    public void setOriginalVersion(Integer originalVersion) {
        this.originalVersion = originalVersion;
    }

    /**
     * @return the newPlanFlag
     */
    public Integer getNewPlanFlag() {
        return newPlanFlag;
    }

    /**
     * @param newPlanFlag the newPlanFlag to set
     */
    public void setNewPlanFlag(Integer newPlanFlag) {
        this.newPlanFlag = newPlanFlag;
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
     * @return the orderDay
     */
    public Integer getOrderDay() {
        return orderDay;
    }

    /**
     * @param orderDay the orderDay to set
     */
    public void setOrderDay(Integer orderDay) {
        this.orderDay = orderDay;
    }

    /**
     * @return the calendarId
     */
    public Integer getCalendarId() {
        return calendarId;
    }

    /**
     * @param calendarId the calendarId to set
     */
    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    /**
     * @return the orderMonth
     */
    public String getOrderMonth() {
        return orderMonth;
    }

    /**
     * @param orderMonth the orderMonth to set
     */
    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    /**
     * @return the forceCompletedBy
     */
    public Integer getForceCompletedBy() {
        return forceCompletedBy;
    }

    /**
     * @param forceCompletedBy the forceCompletedBy to set
     */
    public void setForceCompletedBy(Integer forceCompletedBy) {
        this.forceCompletedBy = forceCompletedBy;
    }

    /**
     * @return the minExpInbPlanDate
     */
    public String getMinExpInbPlanDate() {
        return minExpInbPlanDate;
    }

    /**
     * @param minExpInbPlanDate the minExpInbPlanDate to set
     */
    public void setMinExpInbPlanDate(String minExpInbPlanDate) {
        this.minExpInbPlanDate = minExpInbPlanDate;
    }

    @Override
    public int[] getFieldsPosition() {
        int[] filedsPi = { IntDef.INT_ONE, IntDef.INT_TWO, IntDef.INT_TWO, IntDef.INT_TWO, IntDef.INT_TEN,
            IntDef.INT_TEN, IntDef.INT_TEN, IntDef.INT_EIGHT, IntDef.INT_EIGHT, IntDef.INT_ONE, IntDef.INT_TEN,
            IntDef.INT_FIVE, IntDef.INT_TEN, IntDef.INT_THIRTY_FIVE, IntDef.INT_EIGHTEEN, IntDef.INT_TEN,
            IntDef.INT_EIGHT, IntDef.INT_FOURTY };
        return filedsPi;
    }

    @Override
    public String[] getFieldsName() {
        String[] filedsNm = { "dataId", "expCode", "mainRoute", "vendorRoute", "impPoNo", 
            "supplierCodeAccounting", "ssmsSupplierCode", "soDate", "poDate",
            "transportModeStr","expPoNo","expPoItemNo","ssmsCustomerCode","customerOrderNo","ttcPartsNo","orderQty",
            "expInbPlanDateStr","orderType" };
        return filedsNm;
    }

    @Override
    public int getTotalLength() {
        return IntDef.INT_ONE_HUNDRED_AND_NINETY;
    }
}
