package com.chinaplus.batch.migration.bean;

import java.io.Serializable;

import com.chinaplus.batch.common.bean.BaseCsvFileEntity;

/**
 * The persistent class for the TNT_IF_IMP_IP database table.
 * 
 */
public class TntMgImpIpEx extends BaseCsvFileEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer mgIpId;

    private String indicator;
    
    private Integer seqNo;
    
    private String officeCode;
    
    private String sourceIpNo;
    
    private String pidNo;

    private String parentPidNo;
    
    private String whsCode;
    
    private String originalWhsCode;
    
    private String originalCustCode;
    
    private String customerCode;
    
    private String ttcPartsNo;
    
    private String originalQty;
    
    private String qty;
    
    private String invoiceNo;
    
    private String moduleNo;
    
    private Integer status;
    
    private String inboundDate;
    
    private String sysObDateTime;
    
    private String actualObDateTime;
    
    private String adjustmentDate;
    
    private String decantDateTime;
    
    private Integer partsId;
    
    private Integer officeId;
    
    private Integer customerId;
    
    private Integer whsId;
    
    private Integer ssmsIpId;
    
    private Integer businessPattern;
    
    private Integer impStockFlag;
    
    private Integer invoiceSummaryId;
    
    private Integer startRow;
    
    private Integer limit;
    
    private Integer rownum;
    
    private Integer ifIpId;

    /**
     * Get the indicator.
     *
     * @return indicator
     */
    public String getIndicator() {
        return this.indicator;
    }

    /**
     * Set the indicator.
     *
     * @param indicator indicator
     */
    public void setIndicator(String indicator) {
        this.indicator = indicator;
        
    }

    /**
     * Get the seqNo.
     *
     * @return seqNo
     */
    public Integer getSeqNo() {
        return this.seqNo;
    }

    /**
     * Set the seqNo.
     *
     * @param seqNo seqNo
     */
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
        
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
     * Get the sourceIpNo.
     *
     * @return sourceIpNo
     */
    public String getSourceIpNo() {
        return this.sourceIpNo;
    }

    /**
     * Set the sourceIpNo.
     *
     * @param sourceIpNo sourceIpNo
     */
    public void setSourceIpNo(String sourceIpNo) {
        this.sourceIpNo = sourceIpNo;
        
    }

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
     * Get the parentPidNo.
     *
     * @return parentPidNo
     */
    public String getParentPidNo() {
        return this.parentPidNo;
    }

    /**
     * Set the parentPidNo.
     *
     * @param parentPidNo parentPidNo
     */
    public void setParentPidNo(String parentPidNo) {
        this.parentPidNo = parentPidNo;
        
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
     * Get the originalCustCode.
     *
     * @return originalCustCode
     */
    public String getOriginalCustCode() {
        return this.originalCustCode;
    }

    /**
     * Set the originalCustCode.
     *
     * @param originalCustCode originalCustCode
     */
    public void setOriginalCustCode(String originalCustCode) {
        this.originalCustCode = originalCustCode;
        
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
     * Get the ttcPartsNo.
     *
     * @return ttcPartsNo
     */
    public String getTtcPartsNo() {
        return this.ttcPartsNo;
    }

    /**
     * Set the ttcPartsNo.
     *
     * @param ttcPartsNo ttcPartsNo
     */
    public void setTtcPartsNo(String ttcPartsNo) {
        this.ttcPartsNo = ttcPartsNo;
        
    }

    /**
     * Get the originalQty.
     *
     * @return originalQty
     */
    public String getOriginalQty() {
        return this.originalQty;
    }

    /**
     * Set the originalQty.
     *
     * @param originalQty originalQty
     */
    public void setOriginalQty(String originalQty) {
        this.originalQty = originalQty;
        
    }

    /**
     * Get the qty.
     *
     * @return qty
     */
    public String getQty() {
        return this.qty;
    }

    /**
     * Set the qty.
     *
     * @param qty qty
     */
    public void setQty(String qty) {
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
     * Get the inboundDate.
     *
     * @return inboundDate
     */
    public String getInboundDate() {
        return this.inboundDate;
    }

    /**
     * Set the inboundDate.
     *
     * @param inboundDate inboundDate
     */
    public void setInboundDate(String inboundDate) {
        this.inboundDate = inboundDate;
        
    }

    /**
     * Get the sysObDateTime.
     *
     * @return sysObDateTime
     */
    public String getSysObDateTime() {
        return this.sysObDateTime;
    }

    /**
     * Set the sysObDateTime.
     *
     * @param sysObDateTime sysObDateTime
     */
    public void setSysObDateTime(String sysObDateTime) {
        this.sysObDateTime = sysObDateTime;
        
    }

    /**
     * Get the actualObDateTime.
     *
     * @return actualObDateTime
     */
    public String getActualObDateTime() {
        return this.actualObDateTime;
    }

    /**
     * Set the actualObDateTime.
     *
     * @param actualObDateTime actualObDateTime
     */
    public void setActualObDateTime(String actualObDateTime) {
        this.actualObDateTime = actualObDateTime;
        
    }

    /**
     * Get the adjustmentDate.
     *
     * @return adjustmentDate
     */
    public String getAdjustmentDate() {
        return this.adjustmentDate;
    }

    /**
     * Set the adjustmentDate.
     *
     * @param adjustmentDate adjustmentDate
     */
    public void setAdjustmentDate(String adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
        
    }

    /**
     * Get the mgIpId.
     *
     * @return mgIpId
     */
    public Integer getMgIpId() {
        return this.mgIpId;
    }

    /**
     * Set the mgIpId.
     *
     * @param mgIpId mgIpId
     */
    public void setMgIpId(Integer mgIpId) {
        this.mgIpId = mgIpId;
        
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
     * Get the businessPattern.
     *
     * @return businessPattern
     */
    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     */
    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
        
    }

    /**
     * Get the decantDateTime.
     *
     * @return decantDateTime
     */
    public String getDecantDateTime() {
        return this.decantDateTime;
    }

    /**
     * Set the decantDateTime.
     *
     * @param decantDateTime decantDateTime
     */
    public void setDecantDateTime(String decantDateTime) {
        this.decantDateTime = decantDateTime;
        
    }

    /**
     * Get the ssmsIpId.
     *
     * @return ssmsIpId
     */
    public Integer getSsmsIpId() {
        return this.ssmsIpId;
    }

    /**
     * Set the ssmsIpId.
     *
     * @param ssmsIpId ssmsIpId
     */
    public void setSsmsIpId(Integer ssmsIpId) {
        this.ssmsIpId = ssmsIpId;
        
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
     * Get the invoiceSummaryId.
     *
     * @return invoiceSummaryId
     */
    public Integer getInvoiceSummaryId() {
        return this.invoiceSummaryId;
    }

    /**
     * Set the invoiceSummaryId.
     *
     * @param invoiceSummaryId invoiceSummaryId
     */
    public void setInvoiceSummaryId(Integer invoiceSummaryId) {
        this.invoiceSummaryId = invoiceSummaryId;
        
    }

    /**
     * Get the originalWhsCode.
     *
     * @return originalWhsCode
     */
    public String getOriginalWhsCode() {
        return this.originalWhsCode;
    }

    /**
     * Set the originalWhsCode.
     *
     * @param originalWhsCode originalWhsCode
     */
    public void setOriginalWhsCode(String originalWhsCode) {
        this.originalWhsCode = originalWhsCode;
        
    }

    /**
     * Get the startRow.
     *
     * @return startRow
     */
    public Integer getStartRow() {
        return this.startRow;
    }

    /**
     * Set the startRow.
     *
     * @param startRow startRow
     */
    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
        
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

    /**
     * Get the rownum.
     *
     * @return rownum
     */
    public Integer getRownum() {
        return this.rownum;
    }

    /**
     * Set the rownum.
     *
     * @param rownum rownum
     */
    public void setRownum(Integer rownum) {
        this.rownum = rownum;
        
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
    
}