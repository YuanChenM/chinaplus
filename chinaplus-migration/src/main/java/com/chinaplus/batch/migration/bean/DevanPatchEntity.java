package com.chinaplus.batch.migration.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.batch.common.bean.BaseCsvFileEntity;

/**
 * The persistent class for the TNT_IF_IMP_IP database table.
 * 
 */
public class DevanPatchEntity extends BaseCsvFileEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String officeCode;

    private String pidNo;

    private BigDecimal qty;
    
    private BigDecimal obQty;

    private String invoiceNo;

    private String moduleNo;

    private Integer status;

    private Integer partsId;

    private Integer whsId;

    private Integer officeId;

    private Integer customerId;
    
    private Integer ifIpId;
    
    private Integer actionType;
    
    private Date endDate;

    /**
     * Get the pidNo.
     *
     * @return pidNo
     */
    public String getPidNo() {
        return this.pidNo;
    }

    /**
     * Set the pidNo.
     *
     * @param pidNo pidNo
     */
    public void setPidNo(String pidNo) {
        this.pidNo = pidNo;

    }

    /**
     * Get the officeCode.
     *
     * @return officeCode
     */
    public String getOfficeCode() {
        return this.officeCode;
    }

    /**
     * Set the officeCode.
     *
     * @param officeCode officeCode
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;

    }

    /**
     * Get the qty.
     *
     * @return qty
     */
    public BigDecimal getQty() {
        return this.qty;
    }

    /**
     * Set the qty.
     *
     * @param qty qty
     */
    public void setQty(BigDecimal qty) {
        this.qty = qty;

    }

    /**
     * Get the invoiceNo.
     *
     * @return invoiceNo
     */
    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    /**
     * Set the invoiceNo.
     *
     * @param invoiceNo invoiceNo
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;

    }

    /**
     * Get the moduleNo.
     *
     * @return moduleNo
     */
    public String getModuleNo() {
        return this.moduleNo;
    }

    /**
     * Set the moduleNo.
     *
     * @param moduleNo moduleNo
     */
    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;

    }

    /**
     * Get the status.
     *
     * @return status
     */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * Set the status.
     *
     * @param status status
     */
    public void setStatus(Integer status) {
        this.status = status;

    }

    /**
     * Get the partsId.
     *
     * @return partsId
     */
    public Integer getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     */
    public void setPartsId(Integer partsId) {
        this.partsId = partsId;

    }

    /**
     * Get the officeId.
     *
     * @return officeId
     */
    public Integer getOfficeId() {
        return this.officeId;
    }

    /**
     * Set the officeId.
     *
     * @param officeId officeId
     */
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;

    }

    /**
     * Get the customerId.
     *
     * @return customerId
     */
    public Integer getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     *
     * @param customerId customerId
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;

    }

    /**
     * Get the ifIpId.
     *
     * @return ifIpId
     */
    public Integer getIfIpId() {
        return this.ifIpId;
    }

    /**
     * Set the ifIpId.
     *
     * @param ifIpId ifIpId
     */
    public void setIfIpId(Integer ifIpId) {
        this.ifIpId = ifIpId;
        
    }

    /**
     * Get the actionType.
     *
     * @return actionType
     */
    public Integer getActionType() {
        return this.actionType;
    }

    /**
     * Set the actionType.
     *
     * @param actionType actionType
     */
    public void setActionType(Integer actionType) {
        this.actionType = actionType;
        
    }

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
     * Get the obQty.
     *
     * @return obQty
     */
    public BigDecimal getObQty() {
        return this.obQty;
    }

    /**
     * Set the obQty.
     *
     * @param obQty obQty
     */
    public void setObQty(BigDecimal obQty) {
        this.obQty = obQty;
        
    }

    /**
     * Get the whsId.
     *
     * @return whsId
     */
    public Integer getWhsId() {
        return this.whsId;
    }

    /**
     * Set the whsId.
     *
     * @param whsId whsId
     */
    public void setWhsId(Integer whsId) {
        this.whsId = whsId;
        
    }
    
}