/**
 * CPIIFB01Param.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.chinaplus.batch.common.bean.BaseBatchParam;

/**
 * Class for batch parameters.
 * 
 * @author yang_jia1
 */
public class CPIIFB01Param extends BaseBatchParam {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** filePath */
    private String filePath;

    /** ifDateTime */
    private Timestamp ifDateTime;

    /** batchID */
    private String batchID;

    /** batchDate */
    private Date batchDate;

    /** reasourceType */
    private Integer reasourceType;

    /** expPartsId */
    private List<Integer> expPartsId;

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
     * getIfDateTime
     * 
     * @return Timestamp
     */
    public Timestamp getIfDateTime() {
        return this.ifDateTime;
    }

    /**
     * setIfDateTime
     * 
     * @param ifDateTime ifDateTime
     */
    public void setIfDateTime(Timestamp ifDateTime) {
        this.ifDateTime = ifDateTime;
    }

    /**
     * Get the batchID.
     *
     * @return batchID
     */
    public String getBatchID() {
        return this.batchID;
    }

    /**
     * Set the batchID.
     *
     * @param batchID batchID
     */
    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }

    /**
     * @return the reasourceType
     */
    public Integer getReasourceType() {
        return reasourceType;
    }

    /**
     * @param reasourceType the reasourceType to set
     */
    public void setReasourceType(Integer reasourceType) {
        this.reasourceType = reasourceType;
    }

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
     * Get the expPartsId.
     *
     * @return expPartsId
     */
    public List<Integer> getExpPartsId() {
        return this.expPartsId;
    }

    /**
     * Set the expPartsId.
     *
     * @param expPartsId expPartsId
     */
    public void setExpPartsId(List<Integer> expPartsId) {
        this.expPartsId = expPartsId;
    }

}
