/**
 * CPIIFB01Param.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.bean;

import java.util.Date;

import com.chinaplus.batch.common.bean.BaseBatchParam;

/**
 * Class for batch parameters.
 * 
 * @author shi_xf
 */
public class CPIIFB02Param extends BaseBatchParam {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** batchDate */
    private Date batchDate;

    /** impStockFlag */
    private Integer impStockFlag;

    /** impStockFlag */
    private String businessPattern;

    /** impStockFlag */
    private Integer migrationFlag;

    /**
     * Get the batchDate.
     *
     * @return batchDate
     */
    public Date getBatchDate() {
        return this.batchDate;
    }

    /**
     * Set the batchDate.
     *
     * @param batchDate batchDate
     */
    public void setBatchDate(Date batchDate) {
        this.batchDate = batchDate;

    }

    /**
     * Get the impStockFlag.
     *
     * @return impStockFlag
     */
    public Integer getImpStockFlag() {
        return this.impStockFlag;
    }

    /**
     * Set the impStockFlag.
     *
     * @param impStockFlag impStockFlag
     */
    public void setImpStockFlag(Integer impStockFlag) {
        this.impStockFlag = impStockFlag;
        
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

    /**
     * Get the migrationFlag.
     *
     * @return migrationFlag
     */
    public Integer getMigrationFlag() {
        return this.migrationFlag;
    }

    /**
     * Set the migrationFlag.
     *
     * @param migrationFlag migrationFlag
     */
    public void setMigrationFlag(Integer migrationFlag) {
        this.migrationFlag = migrationFlag;
        
    }
}
