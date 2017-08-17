/**
 * Run-Down Batch parameter entity
 * 
 * @screen Run-Down Batch
 * @author liu_yinchuan
 */
package com.chinaplus.batch.interfaces.bean;

import java.util.Date;

import com.chinaplus.batch.common.bean.BaseBatchParam;

/**
 * Class for Inventory by day batch parameters.
 * 
 * @author liu_yinchuan
 */
public class CPIIFB11Param extends BaseBatchParam {

    /** serialVersionUID */
    private static final long serialVersionUID = -3342466769502716860L;

    /** Date */
    private Date date;

    /** sub */
    private String[] subProcess;

    /** Integer */
    private Integer onlineFlag;

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
     * Get the date.
     *
     * @return date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Set the date.
     *
     * @param date date
     */
    public void setDate(Date date) {
        this.date = date;
        
    }

    /**
     * Get the subProcess.
     *
     * @return subProcess
     */
    public String[] getSubProcess() {
        return this.subProcess;
    }

    /**
     * Set the subProcess.
     *
     * @param subProcess subProcess
     */
    public void setSubProcess(String[] subProcess) {
        this.subProcess = subProcess;
        
    }

}
