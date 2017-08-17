/**
 * CPSSMF11UploadEntity.java
 * 
 * @screen CPSSMF11
 * @author gu_chengchen
 */
package com.chinaplus.web.sa.entity;

import java.util.List;
import java.util.Map;

import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Shipping Status Upload Entity.
 */
public class CPSSMF11UploadEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Order ID */
    private Integer orderId;

    /** LastSsId */
    private Integer lastSsId;

    /** Last Version */
    private Integer lastVersion;

    /** Reason Code Set */
    private String reasonCodeSet;

    /** Reason Set */
    private String reasonSet;

    /** Max Original Version */
    private Integer maxOriginalVersion;

    /** Part Map */
    private Map<Integer, CPSSMF11PartEntity> partMap;

    /** Part Key List */
    private List<String> partKeyList;

    /** Plan List */
    private List<CPSSMF11PlanEntity> excelPlanList;

    /**
     * Get the orderId.
     *
     * @return orderId
     */
    public Integer getOrderId() {
        return this.orderId;
    }

    /**
     * Set the orderId.
     *
     * @param orderId orderId
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * Get the lastSsId.
     *
     * @return lastSsId
     */
    public Integer getLastSsId() {
        return this.lastSsId;
    }

    /**
     * Set the lastSsId.
     *
     * @param lastSsId lastSsId
     */
    public void setLastSsId(Integer lastSsId) {
        this.lastSsId = lastSsId;
    }

    /**
     * Get the lastVersion.
     *
     * @return lastVersion
     */
    public Integer getLastVersion() {
        return this.lastVersion;
    }

    /**
     * Set the lastVersion.
     *
     * @param lastVersion lastVersion
     */
    public void setLastVersion(Integer lastVersion) {
        this.lastVersion = lastVersion;
    }

    /**
     * Get the reasonCodeSet.
     *
     * @return reasonCodeSet
     */
    public String getReasonCodeSet() {
        return this.reasonCodeSet;
    }

    /**
     * Set the reasonCodeSet.
     *
     * @param reasonCodeSet reasonCodeSet
     */
    public void setReasonCodeSet(String reasonCodeSet) {
        this.reasonCodeSet = reasonCodeSet;
    }

    /**
     * Get the reasonSet.
     *
     * @return reasonSet
     */
    public String getReasonSet() {
        return this.reasonSet;
    }

    /**
     * Set the reasonSet.
     *
     * @param reasonSet reasonSet
     */
    public void setReasonSet(String reasonSet) {
        this.reasonSet = reasonSet;
    }

    /**
     * Get the maxOriginalVersion.
     *
     * @return maxOriginalVersion
     */
    public Integer getMaxOriginalVersion() {
        return this.maxOriginalVersion;
    }

    /**
     * Set the maxOriginalVersion.
     *
     * @param maxOriginalVersion maxOriginalVersion
     */
    public void setMaxOriginalVersion(Integer maxOriginalVersion) {
        this.maxOriginalVersion = maxOriginalVersion;
    }

    /**
     * Get the partMap.
     *
     * @return partMap
     */
    public Map<Integer, CPSSMF11PartEntity> getPartMap() {
        return this.partMap;
    }

    /**
     * Set the partMap.
     *
     * @param partMap partMap
     */
    public void setPartMap(Map<Integer, CPSSMF11PartEntity> partMap) {
        this.partMap = partMap;
    }

    /**
     * Get the partKeyList.
     *
     * @return partKeyList
     */
    public List<String> getPartKeyList() {
        return this.partKeyList;
    }

    /**
     * Set the partKeyList.
     *
     * @param partKeyList partKeyList
     */
    public void setPartKeyList(List<String> partKeyList) {
        this.partKeyList = partKeyList;
    }

    /**
     * Get the excelPlanList.
     *
     * @return excelPlanList
     */
    public List<CPSSMF11PlanEntity> getExcelPlanList() {
        return this.excelPlanList;
    }

    /**
     * Set the excelPlanList.
     *
     * @param excelPlanList excelPlanList
     */
    public void setExcelPlanList(List<CPSSMF11PlanEntity> excelPlanList) {
        this.excelPlanList = excelPlanList;
    }

}
