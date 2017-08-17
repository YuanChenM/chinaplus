package com.chinaplus.web.mm.entity;

import java.sql.Timestamp;

import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.util.StringUtil;

/**
 * Init TT-logix Customer list info.
 */
public class TTCLogixCustomer extends BaseEntity {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** whsCustomerId */
    private String whsCustomerId;

    /** whsCode */
    private String whsCode;

    /** TT-logix Customer Code */
    private String whsCustomerCode;

    /** officeId */
    private String officeId;

    /** whsId */
    private String whsId;

    /** customerId */
    private String customerId;

    /** createBy */
    private String createBy;

    /** createDate */
    private Timestamp createDate;

    /** updateBy */
    private String updateBy;

    /** updateDate */
    private Timestamp updateDate;

    /** version */
    private Integer version;

    /**
     * Get the whsCustomerId.
     *
     * @return whsCustomerId
     */
    public String getWhsCustomerId() {
        return this.whsCustomerId;
    }

    /**
     * Set the whsCustomerId.
     *
     * @param whsCustomerId whsCustomerId
     */
    public void setWhsCustomerId(String whsCustomerId) {
        this.whsCustomerId = whsCustomerId;
    }

    /**
     * Get the whsCode.
     *
     * @return whsCode
     */
    public String getWhsCode() {
        return this.whsCode;
    }

    /**
     * Set the whsCode.
     *
     * @param whsCode whsCode
     */
    public void setWhsCode(String whsCode) {
        this.whsCode = whsCode;
    }

    /**
     * Get the whsCustomerCode.
     *
     * @return whsCustomerCode
     */
    public String getWhsCustomerCode() {
        return this.whsCustomerCode;
    }

    /**
     * Set the whsCustomerCode.
     *
     * @param whsCustomerCode whsCustomerCode
     */
    public void setWhsCustomerCode(String whsCustomerCode) {
        this.whsCustomerCode = whsCustomerCode;
    }

    /**
     * Get the officeId.
     *
     * @return officeId
     */
    public String getOfficeId() {
        return this.officeId;
    }

    /**
     * Set the officeId.
     *
     * @param officeId officeId
     */
    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    /**
     * Get the whsId.
     *
     * @return whsId
     */
    public String getWhsId() {
        return this.whsId;
    }

    /**
     * Set the whsId.
     *
     * @param whsId whsId
     */
    public void setWhsId(String whsId) {
        this.whsId = whsId;
    }

    /**
     * Get the customerId.
     *
     * @return customerId
     */
    public String getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     *
     * @param customerId customerId
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Get the createBy.
     *
     * @return createBy
     */
    public String getCreateBy() {
        return this.createBy;
    }

    /**
     * Set the createBy.
     *
     * @param createBy createBy
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * Get the createDate.
     *
     * @return createDate
     */
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    /**
     * Set the createDate.
     *
     * @param createDate createDate
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /**
     * Get the updateBy.
     *
     * @return updateBy
     */
    public String getUpdateBy() {
        return this.updateBy;
    }

    /**
     * Set the updateBy.
     *
     * @param updateBy updateBy
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * Get the updateDate.
     *
     * @return updateDate
     */
    public Timestamp getUpdateDate() {
        return this.updateDate;
    }

    /**
     * Set the updateDate.
     *
     * @param updateDate updateDate
     */
    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
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

    /**
     *
     * @return
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     *
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        TTCLogixCustomer ttc = (TTCLogixCustomer) obj;
        if (!StringUtil.isEmpty(whsCustomerId) && !StringUtil.isEmpty(ttc.getWhsCustomerId())
                && ttc.getWhsCustomerId().equals(whsCustomerId)) {
            return true;
        }
        return false;
    }

}
