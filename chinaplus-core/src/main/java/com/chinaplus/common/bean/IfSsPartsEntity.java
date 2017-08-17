package com.chinaplus.common.bean;

import com.chinaplus.common.entity.TntSsPart;

/**
 * The persistent class for the TNT_SS_PARTS database table.
 * 
 * @author cheng_xingfei
 */
public class IfSsPartsEntity extends TntSsPart {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5469632935428343217L;
    
    /** delayAdjustmentPattern */
    private Integer delayAdjustmentPattern;
    
    /** hasNextEtd */
    private boolean hasNextEtd;

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
     * @return the hasNextEtd
     */
    public boolean isHasNextEtd() {
        return hasNextEtd;
    }

    /**
     * @param hasNextEtd the hasNextEtd to set
     */
    public void setHasNextEtd(boolean hasNextEtd) {
        this.hasNextEtd = hasNextEtd;
    }
}