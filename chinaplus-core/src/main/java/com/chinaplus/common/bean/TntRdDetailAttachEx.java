/**
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.common.entity.TntRdAttachShipping;

/**
 * TntRdDetailAttachEx.
 */
public class TntRdDetailAttachEx extends TntRdAttachShipping {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** rundownMasterId */
    private Integer rundownMasterId;
    
    /** rundownDetailId */
    private Integer rundownDetailId;
    
    /** BL_DATE */
    private Date blDate;

    /** trasportMode */
    private Integer transportMode;

    /** order forecast qty */
    private BigDecimal ofcQty;

    /** order plan qty */
    private BigDecimal orderPlanQty;

    /** on-shipping qty */
    private BigDecimal onShippingQty;

    /** order Month */
    private String orderMonth ;

    /** invoice count */
    private Integer invCount;

    /** imp po count */
    private Integer impPoCount;
    
    /** Last vanning Date */
    private Date lastVanDate;
    
    /** Stock Status Id */
    private Integer stockStatusId;
    
    /** effective order forecast Count */
    private Integer effectOfcCnt;

    /**
     * Get the blDate.
     *
     * @return blDate
     */
    public Date getBlDate() {
        return this.blDate;
    }

    /**
     * Set the blDate.
     *
     * @param blDate blDate
     */
    public void setBlDate(Date blDate) {
        this.blDate = blDate;
        
    }

    /**
     * Get the ofcQty.
     *
     * @return ofcQty
     */
    public BigDecimal getOfcQty() {
        return this.ofcQty;
    }

    /**
     * Set the ofcQty.
     *
     * @param ofcQty ofcQty
     */
    public void setOfcQty(BigDecimal ofcQty) {
        this.ofcQty = ofcQty;
        
    }

    /**
     * Get the orderPlanQty.
     *
     * @return orderPlanQty
     */
    public BigDecimal getOrderPlanQty() {
        return this.orderPlanQty;
    }

    /**
     * Set the orderPlanQty.
     *
     * @param orderPlanQty orderPlanQty
     */
    public void setOrderPlanQty(BigDecimal orderPlanQty) {
        this.orderPlanQty = orderPlanQty;
        
    }

    /**
     * Get the onShippingQty.
     *
     * @return onShippingQty
     */
    public BigDecimal getOnShippingQty() {
        return this.onShippingQty;
    }

    /**
     * Set the onShippingQty.
     *
     * @param onShippingQty onShippingQty
     */
    public void setOnShippingQty(BigDecimal onShippingQty) {
        this.onShippingQty = onShippingQty;
        
    }

    /**
     * Get the invCount.
     *
     * @return invCount
     */
    public Integer getInvCount() {
        return this.invCount;
    }

    /**
     * Set the invCount.
     *
     * @param invCount invCount
     */
    public void setInvCount(Integer invCount) {
        this.invCount = invCount;
        
    }

    /**
     * Get the impPoCount.
     *
     * @return impPoCount
     */
    public Integer getImpPoCount() {
        return this.impPoCount;
    }

    /**
     * Set the impPoCount.
     *
     * @param impPoCount impPoCount
     */
    public void setImpPoCount(Integer impPoCount) {
        this.impPoCount = impPoCount;
        
    }

    /**
     * Get the transportMode.
     *
     * @return transportMode
     */
    public Integer getTransportMode() {
        return this.transportMode;
    }

    /**
     * Set the transportMode.
     *
     * @param transportMode transportMode
     */
    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
        
    }

    /**
     * Get the lastVanDate.
     *
     * @return lastVanDate
     */
    public Date getLastVanDate() {
        return this.lastVanDate;
    }

    /**
     * Set the lastVanDate.
     *
     * @param lastVanDate lastVanDate
     */
    public void setLastVanDate(Date lastVanDate) {
        this.lastVanDate = lastVanDate;
        
    }

    /**
     * Get the stockStatusId.
     *
     * @return stockStatusId
     *
     * @author liu_yinchuan
     */
    public Integer getStockStatusId() {
        return this.stockStatusId;
    }

    /**
     * Set the stockStatusId.
     *
     * @param stockStatusId stockStatusId
     *
     * @author liu_yinchuan
     */
    public void setStockStatusId(Integer stockStatusId) {
        this.stockStatusId = stockStatusId;
        
    }

    /**
     * Get the rundownDetailId.
     *
     * @return rundownDetailId
     *
     * @author liu_yinchuan
     */
    public Integer getRundownDetailId() {
        return this.rundownDetailId;
    }

    /**
     * Set the rundownDetailId.
     *
     * @param rundownDetailId rundownDetailId
     *
     * @author liu_yinchuan
     */
    public void setRundownDetailId(Integer rundownDetailId) {
        this.rundownDetailId = rundownDetailId;
        
    }

    /**
     * Get the rundownMasterId.
     *
     * @return rundownMasterId
     *
     * @author liu_yinchuan
     */
    public Integer getRundownMasterId() {
        return this.rundownMasterId;
    }

    /**
     * Set the rundownMasterId.
     *
     * @param rundownMasterId rundownMasterId
     *
     * @author liu_yinchuan
     */
    public void setRundownMasterId(Integer rundownMasterId) {
        this.rundownMasterId = rundownMasterId;
        
    }

    /**
     * Get the orderMonth.
     *
     * @return orderMonth
     *
     * @author liu_yinchuan
     */
    public String getOrderMonth() {
        return this.orderMonth;
    }

    /**
     * Set the orderMonth.
     *
     * @param orderMonth orderMonth
     *
     * @author liu_yinchuan
     */
    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
        
    }

    /**
     * Get the effectOfcCnt.
     *
     * @return effectOfcCnt
     *
     * @author liu_yinchuan
     */
    public Integer getEffectOfcCnt() {
        return this.effectOfcCnt;
    }

    /**
     * Set the effectOfcCnt.
     *
     * @param effectOfcCnt effectOfcCnt
     *
     * @author liu_yinchuan
     */
    public void setEffectOfcCnt(Integer effectOfcCnt) {
        this.effectOfcCnt = effectOfcCnt;
        
    }
 
}
