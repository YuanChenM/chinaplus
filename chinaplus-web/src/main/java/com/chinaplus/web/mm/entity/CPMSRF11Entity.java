/**
 * @screen CPMSRF11
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPMSRF11Entity.
 */
public class CPMSRF11Entity extends CPMSRF01Entity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** srId  the srId  */
    private  Integer  srId;

    /** shippingRCodeFalg  the shippingRCodeFalg  */
   // private  Boolean shippingRCodeFalg; 
    
    /** maxDeliveryStart  the maxDeliveryStart  */
    private  Date maxDeliveryStart;
    
    /** minDeliveryEnd  the minDeliveryEnd  */
    private  Date minDeliveryEnd;
    
    /** rowNum  the  rowNum */
    private Integer  rowNum;
    
    /** fromEtd  the  fromEtd */
    private Date  fromEtd;
    
    /** toEtd  the  toEtd */
    private Date  toEtd;
    
    /** type  the  type */
    private String type;
    
    /** officeId  the  officeId */
    private Integer  officeId;
       
    /** shippingRouteType  the shippingRouteType */
    private  String  shippingRouteType;
    
    /**
     * Get the shippingRouteType.
     *
     * @return shippingRouteType
     */
    public String getShippingRouteType() {
        return this.shippingRouteType;
    }
    /**
     * Set the shippingRouteType.
     *
     * @param shippingRouteType shippingRouteType
     */
    public void setShippingRouteType(String shippingRouteType) {
        this.shippingRouteType = shippingRouteType;
    }
    
    /**
     * Get the officeId.
     *
     * @return officeId
     */
    public Integer getOfficeId() {
        return this.officeId;
    }
    /**
     * Set the officeId.
     *
     * @param officeId officeId
     */
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }
    
    /**
     * Get the type.
     *
     * @return type
     */
    public String getType() {
        return this.type;
    }
    /**
     * Set the type.
     *
     * @param type type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Get the srId.
     *
     * @return srId
     */
    public Integer getSrId() {
        return this.srId;
    }
    
    /**
     * Set the srId.
     *
     * @param srId idsrId
     */
    public void setSrId(Integer srId) {
        this.srId = srId;
    }
//    /**
//     * Get the shippingRCodeFalg.
//     *
//     * @return shippingRCodeFalg
//     */
//    public Boolean getShippingRCodeFalg() {
//        return this.shippingRCodeFalg;
//    }
//    /**
//     * Set the shippingRCodeFalg.
//     *
//     * @param shippingRCodeFalg shippingRCodeFalg
//     */
//    public void setShippingRCodeFalg(Boolean shippingRCodeFalg) {
//        this.shippingRCodeFalg = shippingRCodeFalg;
//    }
    /**
     * Get the fromEtd.
     *
     * @return fromEtd
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getFromEtd() {
        return this.fromEtd;
    }
    /**
     * Set the fromEtd.
     *
     * @param fromEtd fromEtd
     */
    public void setFromEtd(Date fromEtd) {
        this.fromEtd = fromEtd;
    }
    /**
     * Get the toEtd.
     *
     * @return toEtd
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getToEtd() {
        return this.toEtd;
    }
    /**
     * Set the toEtd.
     *
     * @param toEtd toEtd
     */
    public void setToEtd(Date toEtd) {
        this.toEtd = toEtd;
    }
   
    
    /**
     * Get the maxDeliveryStart.
     *
     * @return maxDeliveryStart
     */
    public Date getMaxDeliveryStart() {
        return this.maxDeliveryStart;
    }
    /**
     * Set the maxDeliveryStart.
     *
     * @param maxDeliveryStart maxDeliveryStart
     */
    public void setMaxDeliveryStart(Date maxDeliveryStart) {
        this.maxDeliveryStart = maxDeliveryStart;
    }
    /**
     * Get the minDeliveryEnd.
     *
     * @return minDeliveryEnd
     */
    public Date getMinDeliveryEnd() {
        return this.minDeliveryEnd;
    }
    /**
     * Set the minDeliveryEnd.
     *
     * @param minDeliveryEnd minDeliveryEnd
     */
    public void setMinDeliveryEnd(Date minDeliveryEnd) {
        this.minDeliveryEnd = minDeliveryEnd;
    }
    
    /**
     * Get the rowNum.
     *
     * @return rowNum
     */
    public Integer getRowNum() {
        return this.rowNum;
    }
    /**
     * Set the rowNum.
     *
     * @param rowNum rowNum
     */
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

}
