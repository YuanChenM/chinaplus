package com.chinaplus.common.bean;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the Supply Chain interface.
 * 
 */
public class ShippingRouteInfoEntity extends com.chinaplus.common.entity.TnmSrDetail implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1454922036369776761L;

    /** shippingRouteType */
    private List<Integer> routeTypeList;
    
    /** shippingRouteCode */
    private String shippingRouteCode;
    
    /** impCcLt */
    private Integer impCcLt;
    
    /** impInboundLt */
    private Integer impInboundLt;
    
    /** office id List */
    private List<Integer> officeList;

    /**
     * Get the routeTypeList.
     *
     * @return routeTypeList
     *
     * @author liu_yinchuan
     */
    public List<Integer> getRouteTypeList() {
        return this.routeTypeList;
    }

    /**
     * Set the routeTypeList.
     *
     * @param routeTypeList routeTypeList
     *
     * @author liu_yinchuan
     */
    public void setRouteTypeList(List<Integer> routeTypeList) {
        this.routeTypeList = routeTypeList;
        
    }

    /**
     * Get the shippingRouteCode.
     *
     * @return shippingRouteCode
     *
     * @author liu_yinchuan
     */
    public String getShippingRouteCode() {
        return this.shippingRouteCode;
    }

    /**
     * Set the shippingRouteCode.
     *
     * @param shippingRouteCode shippingRouteCode
     *
     * @author liu_yinchuan
     */
    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;
        
    }

    /**
     * Get the impCcLt.
     *
     * @return impCcLt
     *
     * @author liu_yinchuan
     */
    public Integer getImpCcLt() {
        return this.impCcLt;
    }

    /**
     * Set the impCcLt.
     *
     * @param impCcLt impCcLt
     *
     * @author liu_yinchuan
     */
    public void setImpCcLt(Integer impCcLt) {
        this.impCcLt = impCcLt;
        
    }

    /**
     * Get the impInboundLt.
     *
     * @return impInboundLt
     *
     * @author liu_yinchuan
     */
    public Integer getImpInboundLt() {
        return this.impInboundLt;
    }

    /**
     * Set the impInboundLt.
     *
     * @param impInboundLt impInboundLt
     *
     * @author liu_yinchuan
     */
    public void setImpInboundLt(Integer impInboundLt) {
        this.impInboundLt = impInboundLt;
        
    }

    /**
     * Get the officeList.
     *
     * @return officeList
     */
    public List<Integer> getOfficeList() {
        return this.officeList;
    }

    /**
     * Set the officeList.
     *
     * @param officeList officeList
     */
    public void setOfficeList(List<Integer> officeList) {
        this.officeList = officeList;
        
    }
}