/**
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.bean;

import java.util.List;

/**
 * TntRundownMasterEx.
 */
public class TntStockStatusHeader extends com.chinaplus.core.base.BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = -1208221433312269051L;

    /** Max Forecast Number */
    private Integer maxFCNum;

    /** On-Shipping Detail Information List */
    private List<TntRdDetailAttachEx> onShippingDetailList;

    /**
     * Get the maxFCNum.
     *
     * @return maxFCNum
     */
    public Integer getMaxFCNum() {
        return this.maxFCNum;
    }

    /**
     * Set the maxFCNum.
     *
     * @param maxFCNum maxFCNum
     */
    public void setMaxFCNum(Integer maxFCNum) {
        this.maxFCNum = maxFCNum;
        
    }

    /**
     * Get the onShippingDetailList.
     *
     * @return onShippingDetailList
     */
    public List<TntRdDetailAttachEx> getOnShippingDetailList() {
        return this.onShippingDetailList;
    }

    /**
     * Set the onShippingDetailList.
     *
     * @param onShippingDetailList onShippingDetailList
     */
    public void setOnShippingDetailList(List<TntRdDetailAttachEx> onShippingDetailList) {
        this.onShippingDetailList = onShippingDetailList;
        
    }
}
