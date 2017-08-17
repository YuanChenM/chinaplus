/**
 * CPOOFS01Entity.java
 * 
 * @screen CPOOFS01
 * @author shi_yuxi
 */
package com.chinaplus.web.om.entity;

import java.sql.Timestamp;

import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPOOFS01Entity.
 */
public class CPOOFS01Entity extends BaseEntity {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** orderForecastId. */
    private Integer orderForecastId;

    /** orderForecastNo. */
    private String orderForecastNo;

    /** impRegion. */
    private String impRegion;

    /** customerCode. */
    private String customerCode;

    /** orderMonth. */
    private String orderMonth;

    /** firstFcMonth. */
    private String firstFcMonth;

    /** lastFcMonth. */
    private String lastFcMonth;

    /** remark. */
    private String remark;

    /** uploadedBy. */
    private String uploadedBy;

    /** uploadedDate. */
    private Timestamp uploadedDate;

    /** createdBy. */
    private Integer createdBy;

    /** createdDate. */
    private Timestamp createdDate;

    /** updatedBy. */
    private Integer updatedBy;

    /** updatedDate. */
    private Timestamp updatedDate;
    /** version. */
    private Integer version;

    /**
     * Get the orderForecastId.
     *
     * @return orderForecastId
     */
    public Integer getOrderForecastId() {
        return this.orderForecastId;
    }

    /**
     * Set the orderForecastId.
     *
     * @param orderForecastId orderForecastId
     */
    public void setOrderForecastId(Integer orderForecastId) {
        this.orderForecastId = orderForecastId;
    }

    /**
     * Get the orderForecastNo.
     *
     * @return orderForecastNo
     */
    public String getOrderForecastNo() {
        return this.orderForecastNo;
    }

    /**
     * Set the orderForecastNo.
     *
     * @param orderForecastNo orderForecastNo
     */
    public void setOrderForecastNo(String orderForecastNo) {
        this.orderForecastNo = orderForecastNo;
    }

    /**
     * Get the impRegion.
     *
     * @return impRegion
     */
    public String getImpRegion() {
        return this.impRegion;
    }

    /**
     * Set the impRegion.
     *
     * @param impRegion impRegion
     */
    public void setImpRegion(String impRegion) {
        this.impRegion = impRegion;
    }

    /**
     * Get the customerCode.
     *
     * @return customerCode
     */
    public String getCustomerCode() {
        return this.customerCode;
    }

    /**
     * Set the customerCode.
     *
     * @param customerCode customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

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
     * Get the firstFcMonth.
     *
     * @return firstFcMonth
     */
    public String getFirstFcMonth() {
        return this.firstFcMonth;
    }

    /**
     * Set the firstFcMonth.
     *
     * @param firstFcMonth firstFcMonth
     */
    public void setFirstFcMonth(String firstFcMonth) {
        this.firstFcMonth = firstFcMonth;
    }

    /**
     * Get the lastFcMonth.
     *
     * @return lastFcMonth
     */
    public String getLastFcMonth() {
        return this.lastFcMonth;
    }

    /**
     * Set the lastFcMonth.
     *
     * @param lastFcMonth lastFcMonth
     */
    public void setLastFcMonth(String lastFcMonth) {
        this.lastFcMonth = lastFcMonth;
    }

    /**
     * Get the remark.
     *
     * @return remark
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * Set the remark.
     *
     * @param remark remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Get the uploadedBy.
     *
     * @return uploadedBy
     */
    public String getUploadedBy() {
        return this.uploadedBy;
    }

    /**
     * Set the uploadedBy.
     *
     * @param uploadedBy uploadedBy
     */
    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    /**
     * Get the uploadedDate.
     *
     * @return uploadedDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getUploadedDate() {
        return this.uploadedDate;
    }

    /**
     * Set the uploadedDate.
     *
     * @param uploadedDate uploadedDate
     */
    public void setUploadedDate(Timestamp uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    /**
     * Get the createdBy.
     *
     * @return createdBy
     */
    public Integer getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Set the createdBy.
     *
     * @param createdBy createdBy
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the createdDate.
     *
     * @return createdDate
     */
    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    /**
     * Set the createdDate.
     *
     * @param createdDate createdDate
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get the updatedBy.
     *
     * @return updatedBy
     */
    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Set the updatedBy.
     *
     * @param updatedBy updatedBy
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the updatedDate.
     *
     * @return updatedDate
     */
    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    /**
     * Set the updatedDate.
     *
     * @param updatedDate updatedDate
     */
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * Get the version.
     *
     * @return version
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * Set the version.
     *
     * @param version version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}
