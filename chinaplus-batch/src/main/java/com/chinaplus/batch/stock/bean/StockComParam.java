/**
 * Run-Down Batch parameter entity
 * 
 * @screen Run-Down Batch
 * @author liu_yinchuan
 */
package com.chinaplus.batch.stock.bean;

import java.util.Date;

import com.chinaplus.batch.common.bean.BaseBatchParam;

/**
 * Class for Run-Down/Stock Status batch parameters.
 * 
 * @author liu_yinchuan
 */
public class StockComParam extends BaseBatchParam {

    /** serialVersionUID */
    private static final long serialVersionUID = -3342466769502716860L;

    /** Stock Date */
    private Date stockDate;

    /** Integer */
    private Integer onlineFlag;

    /** Stock Date */
    private String timeZone;

    /**
     * Get the stockDate.
     *
     * @return stockDate
     */
    public Date getStockDate() {
        return this.stockDate;
    }

    /**
     * Set the stockDate.
     *
     * @param stockDate stockDate
     */
    public void setStockDate(Date stockDate) {
        this.stockDate = stockDate;

    }

    /**
     * Get the onlineFlag.
     *
     * @return onlineFlag
     */
    public Integer getOnlineFlag() {
        return this.onlineFlag;
    }

    /**
     * Set the onlineFlag.
     *
     * @param onlineFlag onlineFlag
     */
    public void setOnlineFlag(Integer onlineFlag) {
        this.onlineFlag = onlineFlag;
        
    }

    /**
     * Get the timeZone.
     *
     * @return timeZone
     */
    public String getTimeZone() {
        return this.timeZone;
    }

    /**
     * Set the timeZone.
     *
     * @param timeZone timeZone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        
    }

}
