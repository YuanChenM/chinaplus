/*
 * Orion System
 * Copyright(C) TOYOTSU SYSCOM CORPORATION All Rights Reserved.
 */
package com.chinaplus.common.entity;

import java.math.BigDecimal;

import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * 
 * OrderCancel.
 * 
 * @author yang_jia1
 */
public class IFOrderCancelEntity extends BaseInterfaceEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ifCancelId */
    private Integer ifCancelId;

    /** represetativeRoute */
    private String represetativeRoute;

    /** impPoNo */
    private String impPoNo;

    /** expPoNo */
    private String expPoNo;

    /** expPoItemNo */
    private String expPoItemNo;

    /** customerCode */
    private String customerCode;

    /** partsNo */
    private String partsNo;

    /** expInboundPlanDate */
    private String expInboundPlanDate;

    /** orderQty */
    private BigDecimal orderQty;

    /** orderId */
    private Integer orderId;

    /** customerOrderNo */
    private String customerOrderNo;

    /** orderDetailId */
    private Integer orderDetailId;

    /** orderStatusId */
    private Integer orderStatusId;
    
    /** forceCompletedBy */
    private Integer forceCompletedBy;

    /**
     * @return the ifCancelId
     */
    public Integer getIfCancelId() {
        return ifCancelId;
    }

    /**
     * @param ifCancelId the ifCancelId to set
     */
    public void setIfCancelId(Integer ifCancelId) {
        this.ifCancelId = ifCancelId;
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
     * @return the expInboundPlanDate
     */
    public String getExpInboundPlanDate() {
        return expInboundPlanDate;
    }

    /**
     * @param expInboundPlanDate the expInboundPlanDate to set
     */
    public void setExpInboundPlanDate(String expInboundPlanDate) {
        this.expInboundPlanDate = expInboundPlanDate;
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

    @Override
    public int[] getFieldsPosition() {
        int[] filedsPi = { IntDef.INT_ONE, IntDef.INT_TWO, IntDef.INT_TWO, IntDef.INT_TEN, IntDef.INT_TEN,
            IntDef.INT_FIVE, IntDef.INT_TEN, IntDef.INT_EIGHTEEN, IntDef.INT_EIGHT };
        return filedsPi;
    }

    @Override
    public String[] getFieldsName() {
        String[] filedsNm = { "dataId", "expCode", "represetativeRoute", "impPoNo", "expPoNo", "expPoItemNo",
            "customerCode", "partsNo", "expInboundPlanDate" };
        return filedsNm;
    }

    @Override
    public int getTotalLength() {
        return IntDef.INT_SIXTYSIX;
    }
}
