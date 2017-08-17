/**
 * CPOOFS01Entity.java
 * 
 * @screen CPOOFF11
 * @author shi_yuxi
 */
package com.chinaplus.web.om.entity;

import java.math.BigDecimal;
import java.util.List;

import com.chinaplus.common.entity.TntPfcDetail;

/** 
 * Upload Order Forecast File                                           
 */ 
public class CPOOFF11Entity extends TntPfcDetail{
    
    
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** routeList */
    private List<MainRouteEntity> routeList;
    
    /** route */
    private MainRouteEntity route;
    
    /** uploadResult */
    private Integer uploadResult;
    
    /** uploadFlag */
    private String uploadFlag;
    
    /** uploadFlag */
    private String orderMonth;
    
    /** uploadFlag */
    private String remark;
    
    /** realForcastNum */
    private Integer realForcastNum;

    /** customerName */
    private String customerName;
    
    /** customerCode */
    private String customerCode;
    
    /** partDescription */
    private String partDescription;
    
    /** PartNo */
    private String partNo;
    
    /** qtySum */
    private BigDecimal qtySum;

    /**
     * Get the routeList.
     *
     * @return routeList
     */
    public List<MainRouteEntity> getRouteList() {
        return this.routeList;
    }

    /**
     * Set the routeList.
     *
     * @param routeList routeList
     */
    public void setRouteList(List<MainRouteEntity> routeList) {
        this.routeList = routeList;
    }

    /**
     * Get the route.
     *
     * @return route
     */
    public MainRouteEntity getRoute() {
        return this.route;
    }

    /**
     * Set the route.
     *
     * @param route route
     */
    public void setRoute(MainRouteEntity route) {
        this.route = route;
    }


    /**
     * Get the uploadResult.
     *
     * @return uploadResult
     */
    public Integer getUploadResult() {
        return this.uploadResult;
    }

    /**
     * Set the uploadResult.
     *
     * @param uploadResult uploadResult
     */
    public void setUploadResult(Integer uploadResult) {
        this.uploadResult = uploadResult;
    }

    /**
     * Get the uploadFlag.
     *
     * @return uploadFlag
     */
    public String getUploadFlag() {
        return this.uploadFlag;
    }

    /**
     * Set the uploadFlag.
     *
     * @param uploadFlag uploadFlag
     */
    public void setUploadFlag(String uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

    /**
     * Get the orderMonth.
     *
     * @return orderMonth
     */
    public String getOrderMonth() {
        return this.orderMonth;
    }

    /**
     * Set the orderMonth.
     *
     * @param orderMonth orderMonth
     */
    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    /**
     * Get the remark.
     *
     * @return remark
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * Set the remark.
     *
     * @param remark remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Get the realForcastNum.
     *
     * @return realForcastNum
     */
    public Integer getRealForcastNum() {
        return this.realForcastNum;
    }

    /**
     * Set the realForcastNum.
     *
     * @param realForcastNum realForcastNum
     */
    public void setRealForcastNum(Integer realForcastNum) {
        this.realForcastNum = realForcastNum;
    }

    /**
     * Get the customerName.
     *
     * @return customerName
     */
    public String getCustomerName() {
        return this.customerName;
    }

    /**
     * Set the customerName.
     *
     * @param customerName customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
     * Get the partDescription.
     *
     * @return partDescription
     */
    public String getPartDescription() {
        return this.partDescription;
    }

    /**
     * Set the partDescription.
     *
     * @param partDescription partDescription
     */
    public void setPartDescription(String partDescription) {
        this.partDescription = partDescription;
    }

    /**
     * Get the partNo.
     *
     * @return partNo
     */
    public String getPartNo() {
        return this.partNo;
    }

    /**
     * Set the partNo.
     *
     * @param partNo partNo
     */
    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    /**
     * Get the qtySum.
     *
     * @return qtySum
     */
    public BigDecimal getQtySum() {
        return this.qtySum;
    }

    /**
     * Set the qtySum.
     *
     * @param qtySum qtySum
     */
    public void setQtySum(BigDecimal qtySum) {
        this.qtySum = qtySum;
    }
    
}
