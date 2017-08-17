package com.chinaplus.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * The persistent class for the TNT_IF_IMP_IP database table.
 * 
 */
@Entity(name = "TNT_IF_IMP_IP")
public class TntIfImpIp extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_IF_IMP_IP_IFIPID_GENERATOR",
        sequenceName = "SEQ_TNT_IF_IMP_IP",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_IF_IMP_IP_IFIPID_GENERATOR")
    @Column(name = "IF_IP_ID")
    private Integer ifIpId;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CUSTOMS_CLR_DATE")
    private String customsClrDate;

    @Column(name = "CUSTOMS_NO")
    private String customsNo;

    @Column(name = "CONTAINER_NO")
    private String containerNo;

    @Column(name = "DATA_SOURCE")
    private String dataSource;

    @Column(name = "DECANT_DATE")
    private String decantDate;

    @Column(name = "DELIVERY_NOTE_NO")
    private String deliveryNoteNo;

    @Column(name = "DEVAN_INVOICE_NO")
    private String devanInvoiceNo;

    @Column(name = "DEVANNED_DATE")
    private String devannedDate;

    @Column(name = "DEVANNED_JOB_NO")
    private String devannedJobNo;

    @Column(name = "DISPATCHED_DATETIME")
    private String dispatchedDatetime;

    @Column(name = "FROM_CUSTOMER_CODE")
    private String fromCustomerCode;

    @Column(name = "FROM_IP_NO")
    private String fromIpNo;

    @Column(name = "FROM_WHS_CODE")
    private String fromWhsCode;

    @Column(name = "HANDLE_FLAG")
    private Integer handleFlag;

    @Column(name = "IF_DATE_TIME")
    private Timestamp ifDateTime;

    @Column(name = "INB_DATE")
    private String inbDate;

    @Column(name = "INB_INVOICE_NO")
    private String inbInvoiceNo;

    @Column(name = "INB_JOB_NO")
    private String inbJobNo;

    @Column(name = "INB_MODULE_NO")
    private String inbModuleNo;

    @Column(name = "INB_TYPE")
    private String inbType;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "MODULE_NO")
    private String moduleNo;

    @Column(name = "OFFICE_CODE")
    private String officeCode;

    @Column(name = "ONHOLD_DATE")
    private String onholdDate;

    @Column(name = "ONHOLD_FLAG")
    private String onholdFlag;

    @Column(name = "OUTBOUND_DATETIME")
    private String outboundDatetime;

    @Column(name = "OUTBOUND_NO")
    private String outboundNo;

    @Column(name = "OUTBOUND_PKG_NO")
    private String outboundPkgNo;

    @Column(name = "OUTBOUND_TYPE")
    private String outboundType;

    @Column(name = "PID_NO")
    private String pidNo;

    @Column(name = "PROCESS_DATE")
    private Timestamp processDate;

    private String qty;

    @Column(name = "SA_DATE")
    private String saDate;

    @Column(name = "SA_QTY")
    private String saQty;

    @Column(name = "SEQ_NO")
    private String seqNo;

    @Column(name = "SOURCE_IP_NO")
    private String sourceIpNo;

    @Column(name = "\"STATUS\"")
    private String status;

    @Column(name = "STOCK_TRANSFER_DATE")
    private String stockTransferDate;

    @Column(name = "SUPPLIER_CODE")
    private String supplierCode;

    @Column(name = "TTC_PARTS_NO")
    private String ttcPartsNo;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "VALID_FLAG")
    private Integer validFlag;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "WHS_CODE")
    private String whsCode;

    @Column(name = "WHS_CUSTOMER_CODE")
    private String whsCustomerCode;

    @Column(name = "WHS_TRANSFER_DATE")
    private String whsTransferDate;

    public TntIfImpIp() {}

    public Integer getIfIpId() {
        return this.ifIpId;
    }

    public void setIfIpId(Integer ifIpId) {
        this.ifIpId = ifIpId;
    }

    public String getActionType() {
        return this.actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCustomsClrDate() {
        return this.customsClrDate;
    }

    public void setCustomsClrDate(String customsClrDate) {
        this.customsClrDate = customsClrDate;
    }

    public String getCustomsNo() {
        return this.customsNo;
    }

    public void setCustomsNo(String customsNo) {
        this.customsNo = customsNo;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDecantDate() {
        return this.decantDate;
    }

    public void setDecantDate(String decantDate) {
        this.decantDate = decantDate;
    }

    public String getDeliveryNoteNo() {
        return this.deliveryNoteNo;
    }

    public void setDeliveryNoteNo(String deliveryNoteNo) {
        this.deliveryNoteNo = deliveryNoteNo;
    }

    public String getDevanInvoiceNo() {
        return this.devanInvoiceNo;
    }

    public void setDevanInvoiceNo(String devanInvoiceNo) {
        this.devanInvoiceNo = devanInvoiceNo;
    }

    public String getDevannedDate() {
        return this.devannedDate;
    }

    public void setDevannedDate(String devannedDate) {
        this.devannedDate = devannedDate;
    }

    public String getDevannedJobNo() {
        return this.devannedJobNo;
    }

    public void setDevannedJobNo(String devannedJobNo) {
        this.devannedJobNo = devannedJobNo;
    }

    public String getDispatchedDatetime() {
        return this.dispatchedDatetime;
    }

    public void setDispatchedDatetime(String dispatchedDatetime) {
        this.dispatchedDatetime = dispatchedDatetime;
    }

    public String getFromCustomerCode() {
        return this.fromCustomerCode;
    }

    public void setFromCustomerCode(String fromCustomerCode) {
        this.fromCustomerCode = fromCustomerCode;
    }

    public String getFromIpNo() {
        return this.fromIpNo;
    }

    public void setFromIpNo(String fromIpNo) {
        this.fromIpNo = fromIpNo;
    }

    public String getFromWhsCode() {
        return this.fromWhsCode;
    }

    public void setFromWhsCode(String fromWhsCode) {
        this.fromWhsCode = fromWhsCode;
    }

    public Integer getHandleFlag() {
        return this.handleFlag;
    }

    public void setHandleFlag(Integer handleFlag) {
        this.handleFlag = handleFlag;
    }

    public Timestamp getIfDateTime() {
        return this.ifDateTime;
    }

    public void setIfDateTime(Timestamp ifDateTime) {
        this.ifDateTime = ifDateTime;
    }

    public String getInbDate() {
        return this.inbDate;
    }

    public void setInbDate(String inbDate) {
        this.inbDate = inbDate;
    }

    public String getInbInvoiceNo() {
        return this.inbInvoiceNo;
    }

    public void setInbInvoiceNo(String inbInvoiceNo) {
        this.inbInvoiceNo = inbInvoiceNo;
    }

    public String getInbJobNo() {
        return this.inbJobNo;
    }

    public void setInbJobNo(String inbJobNo) {
        this.inbJobNo = inbJobNo;
    }

    public String getInbModuleNo() {
        return this.inbModuleNo;
    }

    public void setInbModuleNo(String inbModuleNo) {
        this.inbModuleNo = inbModuleNo;
    }

    public String getInbType() {
        return this.inbType;
    }

    public void setInbType(String inbType) {
        this.inbType = inbType;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getModuleNo() {
        return this.moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public String getOfficeCode() {
        return this.officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOnholdDate() {
        return this.onholdDate;
    }

    public void setOnholdDate(String onholdDate) {
        this.onholdDate = onholdDate;
    }

    public String getOnholdFlag() {
        return this.onholdFlag;
    }

    public void setOnholdFlag(String onholdFlag) {
        this.onholdFlag = onholdFlag;
    }

    public String getOutboundDatetime() {
        return this.outboundDatetime;
    }

    public void setOutboundDatetime(String outboundDatetime) {
        this.outboundDatetime = outboundDatetime;
    }

    public String getOutboundNo() {
        return this.outboundNo;
    }

    public void setOutboundNo(String outboundNo) {
        this.outboundNo = outboundNo;
    }

    public String getOutboundPkgNo() {
        return this.outboundPkgNo;
    }

    public void setOutboundPkgNo(String outboundPkgNo) {
        this.outboundPkgNo = outboundPkgNo;
    }

    public String getOutboundType() {
        return this.outboundType;
    }

    public void setOutboundType(String outboundType) {
        this.outboundType = outboundType;
    }

    public String getPidNo() {
        return this.pidNo;
    }

    public void setPidNo(String pidNo) {
        this.pidNo = pidNo;
    }

    public Timestamp getProcessDate() {
        return this.processDate;
    }

    public void setProcessDate(Timestamp processDate) {
        this.processDate = processDate;
    }

    public String getQty() {
        return this.qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSaDate() {
        return this.saDate;
    }

    public void setSaDate(String saDate) {
        this.saDate = saDate;
    }

    public String getSaQty() {
        return this.saQty;
    }

    public void setSaQty(String saQty) {
        this.saQty = saQty;
    }

    public String getSeqNo() {
        return this.seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getSourceIpNo() {
        return this.sourceIpNo;
    }

    public void setSourceIpNo(String sourceIpNo) {
        this.sourceIpNo = sourceIpNo;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStockTransferDate() {
        return this.stockTransferDate;
    }

    public void setStockTransferDate(String stockTransferDate) {
        this.stockTransferDate = stockTransferDate;
    }

    public String getSupplierCode() {
        return this.supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getTtcPartsNo() {
        return this.ttcPartsNo;
    }

    public void setTtcPartsNo(String ttcPartsNo) {
        this.ttcPartsNo = ttcPartsNo;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getValidFlag() {
        return this.validFlag;
    }

    public void setValidFlag(Integer validFlag) {
        this.validFlag = validFlag;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getWhsCode() {
        return this.whsCode;
    }

    public void setWhsCode(String whsCode) {
        this.whsCode = whsCode;
    }

    public String getWhsCustomerCode() {
        return this.whsCustomerCode;
    }

    public void setWhsCustomerCode(String whsCustomerCode) {
        this.whsCustomerCode = whsCustomerCode;
    }

    public String getWhsTransferDate() {
        return this.whsTransferDate;
    }

    public void setWhsTransferDate(String whsTransferDate) {
        this.whsTransferDate = whsTransferDate;
    }

    /**
     * Get the containerNo.
     *
     * @return containerNo
     *
     * @author liu_yinchuan
     */
    public String getContainerNo() {
        return this.containerNo;
    }

    /**
     * Set the containerNo.
     *
     * @param containerNo containerNo
     *
     * @author liu_yinchuan
     */
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
        
    }

}