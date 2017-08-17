/**
 * Run-Down Batch parameter entity
 * 
 * @screen Run-Down Batch
 * @author liu_yinchuan
 */
package com.chinaplus.batch.online.bean;

import com.chinaplus.batch.common.bean.BaseBatchParam;

/**
 * Class for Run-Down/Stock Status batch parameters.
 * 
 * @author liu_yinchuan
 */
public class CPSSSB00Param extends BaseBatchParam {

    /** serialVersionUID */
    private static final long serialVersionUID = -3342466769502716860L;

    /** Stock Date */
    private String stockDate;

    /** Integer */
    private String onlineFlag;

    /**
     * Get the stockDate.
     *
     * @return stockDate
     */
    public String getStockDate() {
        return this.stockDate;
    }

    /**
     * Set the stockDate.
     *
     * @param stockDate stockDate
     */
    public void setStockDate(String stockDate) {
        this.stockDate = stockDate;

    }

    /**
     * Get the onlineFlag.
     *
     * @return onlineFlag
     */
    public String getOnlineFlag() {
        return this.onlineFlag;
    }

    /**
     * Set the onlineFlag.
     *
     * @param onlineFlag onlineFlag
     */
    public void setOnlineFlag(String onlineFlag) {
        this.onlineFlag = onlineFlag;
        
    }

}
