/**
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.bean;

import java.util.Date;

import com.chinaplus.common.entity.TntNotInRundown;

/**
 * TntNotInRundownEx.
 */
public class TntNotInRundownEx extends TntNotInRundown {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** order Month */
    private String orderMonth;
    
    /** last vaning date */
    private Date lastVanDate;
    
    /** rundownMasterId */
    private Integer rundownMasterId;

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
