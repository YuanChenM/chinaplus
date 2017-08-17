/**
 * BatchLog.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.bean;

import com.chinaplus.common.entity.BaseInterfaceEntity;


/**
 * 
 * BatchLog.
 * @author yang_jia1
 */
public class BatchLog extends BaseInterfaceEntity {
    
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private Integer batchType;
    
    private String batchTypeName;
    
    private Integer status;
    
    private int lastIsSuccess;

    /**
     * @return the batchType
     */
    public Integer getBatchType() {
        return batchType;
    }

    /**
     * @param batchType the batchType to set
     */
    public void setBatchType(Integer batchType) {
        this.batchType = batchType;
    }

    /**
     * @return the batchTypeName
     */
    public String getBatchTypeName() {
        return batchTypeName;
    }

    /**
     * @param batchTypeName the batchTypeName to set
     */
    public void setBatchTypeName(String batchTypeName) {
        this.batchTypeName = batchTypeName;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the lastIsSuccess
     */
    public int getLastIsSuccess() {
        return lastIsSuccess;
    }

    /**
     * @param lastIsSuccess the lastIsSuccess to set
     */
    public void setLastIsSuccess(int lastIsSuccess) {
        this.lastIsSuccess = lastIsSuccess;
    }

    @Override
    public int[] getFieldsPosition() {
        return null;
    }

    @Override
    public String[] getFieldsName() {
        return null;
    }

    @Override
    public int getTotalLength() {
        return 0;
    }
}
