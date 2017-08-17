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
public class CPIIFB00Param extends BaseBatchParam {

    /** serialVersionUID */
    private static final long serialVersionUID = -3342466769502716860L;

    /** Stock Date */
    private String batchDate;

    /** businessPattern */
    private String businessPattern;

    /**
     * Get the batchDate.
     *
     * @return batchDate
     */
    public String getBatchDate() {
        return this.batchDate;
    }

    /**
     * Set the batchDate.
     *
     * @param batchDate batchDate
     */
    public void setBatchDate(String batchDate) {
        this.batchDate = batchDate;
        
    }

    /**
     * Get the businessPattern.
     *
     * @return businessPattern
     */
    public String getBusinessPattern() {
        return this.businessPattern;
    }

    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     */
    public void setBusinessPattern(String businessPattern) {
        this.businessPattern = businessPattern;
        
    }


}
