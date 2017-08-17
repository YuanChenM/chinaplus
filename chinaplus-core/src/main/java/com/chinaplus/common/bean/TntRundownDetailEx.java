/**
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.bean;

import java.util.List;

import com.chinaplus.common.entity.TntRdDetail;

/**
 * TntRundownMasterEx.
 */
public class TntRundownDetailEx extends TntRdDetail {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** rundownMasterId */
    private Integer rundownMasterId;

    /** order forecast list */
    private List<TntRdDetailAttachEx> orderFcList;

    /** delivery plan list */
    private List<TntRdDetailAttachEx> planList;

    /** on-shipping list */
    private List<TntRdDetailAttachEx> onShippingList;

    /** Run-down attached information list */
    private List<TntRdDetailAttachEx> attachedInfoList;

    /**
     * Get the orderFcList.
     *
     * @return orderFcList
     */
    public List<TntRdDetailAttachEx> getOrderFcList() {
        return this.orderFcList;
    }

    /**
     * Set the orderFcList.
     *
     * @param orderFcList orderFcList
     */
    public void setOrderFcList(List<TntRdDetailAttachEx> orderFcList) {
        this.orderFcList = orderFcList;
        
    }

    /**
     * Get the planList.
     *
     * @return planList
     */
    public List<TntRdDetailAttachEx> getPlanList() {
        return this.planList;
    }

    /**
     * Set the planList.
     *
     * @param planList planList
     */
    public void setPlanList(List<TntRdDetailAttachEx> planList) {
        this.planList = planList;
        
    }

    /**
     * Get the onShippingList.
     *
     * @return onShippingList
     */
    public List<TntRdDetailAttachEx> getOnShippingList() {
        return this.onShippingList;
    }

    /**
     * Set the onShippingList.
     *
     * @param onShippingList onShippingList
     */
    public void setOnShippingList(List<TntRdDetailAttachEx> onShippingList) {
        this.onShippingList = onShippingList;
        
    }

    /**
     * Get the attachedInfoList.
     *
     * @return attachedInfoList
     */
    public List<TntRdDetailAttachEx> getAttachedInfoList() {
        return this.attachedInfoList;
    }

    /**
     * Set the attachedInfoList.
     *
     * @param attachedInfoList attachedInfoList
     */
    public void setAttachedInfoList(List<TntRdDetailAttachEx> attachedInfoList) {
        this.attachedInfoList = attachedInfoList;
        
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

}
