/**
 * Run-Down Batch parameter entity
 * 
 * @screen Run-Down Batch
 * @author liu_yinchuan
 */
package com.chinaplus.batch.migration.bean;

import java.util.Date;

import com.chinaplus.batch.common.bean.BaseBatchParam;

/**
 * Class for data migration batch parameters.
 * 
 * @author liu_yinchuan
 */
public class MigrationComParam extends BaseBatchParam {

    /** serialVersionUID */
    private static final long serialVersionUID = -3342466769502716860L;

    /** This date is the day which date line of imp stock */
    private Date endDate;
    
    /** file path */
    private String filePath;
    
    /** file path */
    private Integer limit;

    /**
     * Get the endDate.
     *
     * @return endDate
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * Set the endDate.
     *
     * @param endDate endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        
    }

    /**
     * Get the filePath.
     *
     * @return filePath
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * Set the filePath.
     *
     * @param filePath filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
        
    }

    /**
     * Get the limit.
     *
     * @return limit
     */
    public Integer getLimit() {
        return this.limit;
    }

    /**
     * Set the limit.
     *
     * @param limit limit
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
        
    }

}
