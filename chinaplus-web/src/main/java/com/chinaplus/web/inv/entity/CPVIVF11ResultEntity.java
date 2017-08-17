/**
 * CPVIVF11ResultEntity.java
 * 
 * @screen CPVIVF11
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import com.chinaplus.core.base.BaseEntity;

/**
 * Invoice Upload Result Entity.
 */
public class CPVIVF11ResultEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Upload Result 0:success 1:pop supplier 2:pop irregular 3:pop supplementary */
    private Integer uploadResult;

    /** Upload ID */
    private String uploadId;

    /**
     * Get the uploadResult.
     *
     * @return uploadResult
     */
    public Integer getUploadResult() {
        return this.uploadResult;
    }

    /**
     * Set the uploadResult.
     *
     * @param uploadResult uploadResult
     */
    public void setUploadResult(Integer uploadResult) {
        this.uploadResult = uploadResult;
    }

    /**
     * Get the uploadId.
     *
     * @return uploadId
     */
    public String getUploadId() {
        return this.uploadId;
    }

    /**
     * Set the uploadId.
     *
     * @param uploadId uploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

}
