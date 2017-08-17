/**
 * CPSSMF11ResultEntity.java
 * 
 * @screen CPSSMF11
 * @author gu_chengchen
 */
package com.chinaplus.web.sa.entity;

import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Shipping Status Result Entity.
 */
public class CPSSMF11ResultEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Upload Result 0:success 1:warn success */
    private Integer uploadResult;

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

}
