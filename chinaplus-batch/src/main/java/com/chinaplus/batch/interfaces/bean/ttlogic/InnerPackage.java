/**
 * InnerPackage.java
 * 
 * @screen CPIIFB02
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.bean.ttlogic;

import java.sql.Timestamp;

import com.chinaplus.batch.common.bean.BaseCsvFileEntity;

/**
 * 
 * InnerPackage.
 * @author yang_jia1
 */
public class InnerPackage extends BaseCsvFileEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    private Integer ifIpId;
    
    private String indicator;
    
    //private String comp = ",";
    
    private String sequenceNo;
    
    private String dataSource;
    
    private String impOffice;
    
    private String sourceIPNo;
    
    private String ttlogicPidNo;
    
    private String fromWhCode;
    
    private String warehouseTransferDate;
    
    private String warehouseCode;
    
    private String supplierCode;
    
    private String fromCustomerCode;
    
    private String stockTransferDate;
    
    private String customerCode;
    
    private String ttcPartsNo;
    
    private String qty;
    
    private String customsClearanceDate;
    
    private String customsClearanceInvoiceNo;
    
    private String containerNo;
    
    private String customsNo;
    
    private String devannedJobNo;
    
    private String devannedDate;
    
    private String devannedInvoiceNo;
    
    private String moduleNo;
    
    private String inboundJobNo;
    
    private String inboundInvoiceNo;
    
    private String inboundModuleNo;
    
    private String inboundType;
    
    private String inboundDate;
    
    private String outboundNo;
    
    private String outboundType;
    
    private String outboundPackageNo;
    
    private String deliveryNoteNo;
    
    private String outboundDateTime;
    
    private String dispatchedDateTime;
    
    private String onholdFlag;
    
    private String onholdDate;
    
    private String stockAdjustmentDate;
    
    private String stockAdjustmentQty;
    
    private String decantDate;
    
    private String fromIpNo;
    
    private String status; 
    
    private Timestamp processDate; 
    
    private Integer actionType;
    
    /**
     * ifDateTime
     */
    private Timestamp ifDateTime;
    
    private Integer version;
    
    /**
     * Get the ifDateTime.
     *
     * @return ifDateTime
     */
    public Timestamp getIfDateTime() {
        return this.ifDateTime;
    }

    /**
     * Set the ifDateTime.
     *
     * @param ifDateTime ifDateTime
     */
    public void setIfDateTime(Timestamp ifDateTime) {
        this.ifDateTime = ifDateTime;
        
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
     * Get the sequenceNo.
     *
     * @return sequenceNo
     */
    public String getSequenceNo() {
        return this.sequenceNo;
    }

    /**
     * Set the sequenceNo.
     *
     * @param sequenceNo sequenceNo
     */
    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
        
    }

    /**
     * Get the dataSource.
     *
     * @return dataSource
     */
    public String getDataSource() {
        return this.dataSource;
    }

    /**
     * Set the dataSource.
     *
     * @param dataSource dataSource
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
        
    }

    /**
     * Get the impOffice.
     *
     * @return impOffice
     */
    public String getImpOffice() {
        return this.impOffice;
    }

    /**
     * Set the impOffice.
     *
     * @param impOffice impOffice
     */
    public void setImpOffice(String impOffice) {
        this.impOffice = impOffice;
        
    }

    /**
     * Get the sourceIPNo.
     *
     * @return sourceIPNo
     */
    public String getSourceIPNo() {
        return this.sourceIPNo;
    }

    /**
     * Set the sourceIPNo.
     *
     * @param sourceIPNo sourceIPNo
     */
    public void setSourceIPNo(String sourceIPNo) {
        this.sourceIPNo = sourceIPNo;
        
    }

    /**
     * Get the ttlogicPidNo.
     *
     * @return ttlogicPidNo
     */
    public String getTtlogicPidNo() {
        return this.ttlogicPidNo;
    }

    /**
     * Set the ttlogicPidNo.
     *
     * @param ttlogicPidNo ttlogicPidNo
     */
    public void setTtlogicPidNo(String ttlogicPidNo) {
        this.ttlogicPidNo = ttlogicPidNo;
        
    }

    /**
     * Get the fromWhCode.
     *
     * @return fromWhCode
     */
    public String getFromWhCode() {
        return this.fromWhCode;
    }

    /**
     * Set the fromWhCode.
     *
     * @param fromWhCode fromWhCode
     */
    public void setFromWhCode(String fromWhCode) {
        this.fromWhCode = fromWhCode;
        
    }

    /**
     * Get the warehouseTransferDate.
     *
     * @return warehouseTransferDate
     */
    public String getWarehouseTransferDate() {
        return this.warehouseTransferDate;
    }

    /**
     * Set the warehouseTransferDate.
     *
     * @param warehouseTransferDate warehouseTransferDate
     */
    public void setWarehouseTransferDate(String warehouseTransferDate) {
        this.warehouseTransferDate = warehouseTransferDate;
        
    }

    /**
     * Get the warehouseCode.
     *
     * @return warehouseCode
     */
    public String getWarehouseCode() {
        return this.warehouseCode;
    }

    /**
     * Set the warehouseCode.
     *
     * @param warehouseCode warehouseCode
     */
    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
        
    }

    /**
     * Get the supplierCode.
     *
     * @return supplierCode
     */
    public String getSupplierCode() {
        return this.supplierCode;
    }

    /**
     * Set the supplierCode.
     *
     * @param supplierCode supplierCode
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
        
    }

    /**
     * Get the fromCustomerCode.
     *
     * @return fromCustomerCode
     */
    public String getFromCustomerCode() {
        return this.fromCustomerCode;
    }

    /**
     * Set the fromCustomerCode.
     *
     * @param fromCustomerCode fromCustomerCode
     */
    public void setFromCustomerCode(String fromCustomerCode) {
        this.fromCustomerCode = fromCustomerCode;
        
    }

    /**
     * Get the stockTransferDate.
     *
     * @return stockTransferDate
     */
    public String getStockTransferDate() {
        return this.stockTransferDate;
    }

    /**
     * Set the stockTransferDate.
     *
     * @param stockTransferDate stockTransferDate
     */
    public void setStockTransferDate(String stockTransferDate) {
        this.stockTransferDate = stockTransferDate;
        
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
     * Get the customsClearanceDate.
     *
     * @return customsClearanceDate
     */
    public String getCustomsClearanceDate() {
        return this.customsClearanceDate;
    }

    /**
     * Set the customsClearanceDate.
     *
     * @param customsClearanceDate customsClearanceDate
     */
    public void setCustomsClearanceDate(String customsClearanceDate) {
        this.customsClearanceDate = customsClearanceDate;
        
    }

    /**
     * Get the customsClearanceInvoiceNo.
     *
     * @return customsClearanceInvoiceNo
     */
    public String getCustomsClearanceInvoiceNo() {
        return this.customsClearanceInvoiceNo;
    }

    /**
     * Set the customsClearanceInvoiceNo.
     *
     * @param customsClearanceInvoiceNo customsClearanceInvoiceNo
     */
    public void setCustomsClearanceInvoiceNo(String customsClearanceInvoiceNo) {
        this.customsClearanceInvoiceNo = customsClearanceInvoiceNo;
        
    }

    /**
     * Get the customsNo.
     *
     * @return customsNo
     */
    public String getCustomsNo() {
        return this.customsNo;
    }

    /**
     * Set the customsNo.
     *
     * @param customsNo customsNo
     */
    public void setCustomsNo(String customsNo) {
        this.customsNo = customsNo;
        
    }

    /**
     * Get the devannedJobNo.
     *
     * @return devannedJobNo
     */
    public String getDevannedJobNo() {
        return this.devannedJobNo;
    }

    /**
     * Set the devannedJobNo.
     *
     * @param devannedJobNo devannedJobNo
     */
    public void setDevannedJobNo(String devannedJobNo) {
        this.devannedJobNo = devannedJobNo;
        
    }

    /**
     * Get the devannedDate.
     *
     * @return devannedDate
     */
    public String getDevannedDate() {
        return this.devannedDate;
    }

    /**
     * Set the devannedDate.
     *
     * @param devannedDate devannedDate
     */
    public void setDevannedDate(String devannedDate) {
        this.devannedDate = devannedDate;
        
    }

    /**
     * Get the devannedInvoiceNo.
     *
     * @return devannedInvoiceNo
     */
    public String getDevannedInvoiceNo() {
        return this.devannedInvoiceNo;
    }

    /**
     * Set the devannedInvoiceNo.
     *
     * @param devannedInvoiceNo devannedInvoiceNo
     */
    public void setDevannedInvoiceNo(String devannedInvoiceNo) {
        this.devannedInvoiceNo = devannedInvoiceNo;
        
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
     * Get the inboundJobNo.
     *
     * @return inboundJobNo
     */
    public String getInboundJobNo() {
        return this.inboundJobNo;
    }

    /**
     * Set the inboundJobNo.
     *
     * @param inboundJobNo inboundJobNo
     */
    public void setInboundJobNo(String inboundJobNo) {
        this.inboundJobNo = inboundJobNo;
        
    }

    /**
     * Get the inboundInvoiceNo.
     *
     * @return inboundInvoiceNo
     */
    public String getInboundInvoiceNo() {
        return this.inboundInvoiceNo;
    }

    /**
     * Set the inboundInvoiceNo.
     *
     * @param inboundInvoiceNo inboundInvoiceNo
     */
    public void setInboundInvoiceNo(String inboundInvoiceNo) {
        this.inboundInvoiceNo = inboundInvoiceNo;
        
    }

    /**
     * Get the inboundModuleNo.
     *
     * @return inboundModuleNo
     */
    public String getInboundModuleNo() {
        return this.inboundModuleNo;
    }

    /**
     * Set the inboundModuleNo.
     *
     * @param inboundModuleNo inboundModuleNo
     */
    public void setInboundModuleNo(String inboundModuleNo) {
        this.inboundModuleNo = inboundModuleNo;
        
    }

    /**
     * Get the inboundType.
     *
     * @return inboundType
     */
    public String getInboundType() {
        return this.inboundType;
    }

    /**
     * Set the inboundType.
     *
     * @param inboundType inboundType
     */
    public void setInboundType(String inboundType) {
        this.inboundType = inboundType;
        
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
     * Get the outboundNo.
     *
     * @return outboundNo
     */
    public String getOutboundNo() {
        return this.outboundNo;
    }

    /**
     * Set the outboundNo.
     *
     * @param outboundNo outboundNo
     */
    public void setOutboundNo(String outboundNo) {
        this.outboundNo = outboundNo;
        
    }

    /**
     * Get the outboundType.
     *
     * @return outboundType
     */
    public String getOutboundType() {
        return this.outboundType;
    }

    /**
     * Set the outboundType.
     *
     * @param outboundType outboundType
     */
    public void setOutboundType(String outboundType) {
        this.outboundType = outboundType;
        
    }

    /**
     * Get the outboundPackageNo.
     *
     * @return outboundPackageNo
     */
    public String getOutboundPackageNo() {
        return this.outboundPackageNo;
    }

    /**
     * Set the outboundPackageNo.
     *
     * @param outboundPackageNo outboundPackageNo
     */
    public void setOutboundPackageNo(String outboundPackageNo) {
        this.outboundPackageNo = outboundPackageNo;
        
    }

    /**
     * Get the deliveryNoteNo.
     *
     * @return deliveryNoteNo
     */
    public String getDeliveryNoteNo() {
        return this.deliveryNoteNo;
    }

    /**
     * Set the deliveryNoteNo.
     *
     * @param deliveryNoteNo deliveryNoteNo
     */
    public void setDeliveryNoteNo(String deliveryNoteNo) {
        this.deliveryNoteNo = deliveryNoteNo;
        
    }

    /**
     * Get the outboundDateTime.
     *
     * @return outboundDateTime
     */
    public String getOutboundDateTime() {
        return this.outboundDateTime;
    }

    /**
     * Set the outboundDateTime.
     *
     * @param outboundDateTime outboundDateTime
     */
    public void setOutboundDateTime(String outboundDateTime) {
        this.outboundDateTime = outboundDateTime;
        
    }

    /**
     * Get the dispatchedDateTime.
     *
     * @return dispatchedDateTime
     */
    public String getDispatchedDateTime() {
        return this.dispatchedDateTime;
    }

    /**
     * Set the dispatchedDateTime.
     *
     * @param dispatchedDateTime dispatchedDateTime
     */
    public void setDispatchedDateTime(String dispatchedDateTime) {
        this.dispatchedDateTime = dispatchedDateTime;
        
    }

    /**
     * Get the onholdFlag.
     *
     * @return onholdFlag
     */
    public String getOnholdFlag() {
        return this.onholdFlag;
    }

    /**
     * Set the onholdFlag.
     *
     * @param onholdFlag onholdFlag
     */
    public void setOnholdFlag(String onholdFlag) {
        this.onholdFlag = onholdFlag;
        
    }

    /**
     * Get the onholdDate.
     *
     * @return onholdDate
     */
    public String getOnholdDate() {
        return this.onholdDate;
    }

    /**
     * Set the onholdDate.
     *
     * @param onholdDate onholdDate
     */
    public void setOnholdDate(String onholdDate) {
        this.onholdDate = onholdDate;
        
    }

    /**
     * Get the stockAdjustmentDate.
     *
     * @return stockAdjustmentDate
     */
    public String getStockAdjustmentDate() {
        return this.stockAdjustmentDate;
    }

    /**
     * Set the stockAdjustmentDate.
     *
     * @param stockAdjustmentDate stockAdjustmentDate
     */
    public void setStockAdjustmentDate(String stockAdjustmentDate) {
        this.stockAdjustmentDate = stockAdjustmentDate;
        
    }

    /**
     * Get the stockAdjustmentQty.
     *
     * @return stockAdjustmentQty
     */
    public String getStockAdjustmentQty() {
        return this.stockAdjustmentQty;
    }

    /**
     * Set the stockAdjustmentQty.
     *
     * @param stockAdjustmentQty stockAdjustmentQty
     */
    public void setStockAdjustmentQty(String stockAdjustmentQty) {
        this.stockAdjustmentQty = stockAdjustmentQty;
        
    }

    /**
     * Get the decantDate.
     *
     * @return decantDate
     */
    public String getDecantDate() {
        return this.decantDate;
    }

    /**
     * Set the decantDate.
     *
     * @param decantDate decantDate
     */
    public void setDecantDate(String decantDate) {
        this.decantDate = decantDate;
        
    }

    /**
     * Get the fromIpNo.
     *
     * @return fromIpNo
     */
    public String getFromIpNo() {
        return this.fromIpNo;
    }

    /**
     * Set the fromIpNo.
     *
     * @param fromIpNo fromIpNo
     */
    public void setFromIpNo(String fromIpNo) {
        this.fromIpNo = fromIpNo;
        
    }

    /**
     * Get the status.
     *
     * @return status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Set the status.
     *
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
        
    }

    /**
     * Get the processDate.
     *
     * @return processDate
     */
    public Timestamp getProcessDate() {
        return this.processDate;
    }

    /**
     * Set the processDate.
     *
     * @param processDate processDate
     */
    public void setProcessDate(Timestamp processDate) {
        this.processDate = processDate;
        
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
     * Get the containerNo.
     *
     * @return containerNo
     */
    public String getContainerNo() {
        return this.containerNo;
    }

    /**
     * Set the containerNo.
     *
     * @param containerNo containerNo
     */
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
        
    }
}
