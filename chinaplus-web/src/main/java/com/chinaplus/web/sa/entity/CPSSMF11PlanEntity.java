/**
 * CPSSMF11PlanEntity.java
 * 
 * @screen CPSSMF11
 * @author gu_chengchen
 */
package com.chinaplus.web.sa.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chinaplus.common.entity.TntSsPart;
import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Shipping Status Upload Plan Entity.
 */
public class CPSSMF11PlanEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Office ID */
    private Integer officeId;

    /** Customer ID */
    private Integer custId;

    /** Current Customers */
    private List<Integer> currentCustomers;

    /** Order ID */
    private Integer orderId;

    /** IPO No. */
    private String impPoNo;

    /** Customer Order No. */
    private String customerOrderNo;

    /** LastSsId */
    private Integer lastSsId;

    /** Last Version */
    private Integer lastVersion;

    /** SS Plan ID */
    private Integer ssPlanId;

    /** Invoice Summary ID */
    private Integer invoiceSummaryId;

    /** Invoice ID */
    private Integer invoiceId;

    /** Invoice No. */
    private String invoiceNo;

    /** Transport Mode */
    private Integer transportMode;

    /** VAN Date */
    private Date vanningDate;

    /** ETD */
    private Date etd;

    /** ETA */
    private Date eta;

    /** Customer Clearance Date */
    private Date ccDate;

    /** Inbound Plan Date */
    private Date inboundPlanDate;

    /** Inbound Actual Date */
    private Date inboundActualDate;

    /** Revision Reason */
    private String revisionReason;

    /** Original Version */
    private Integer originalVersion;

    /** Revision Version */
    private Integer revisionVersion;

    /** NIRD Flag */
    private Integer nirdFlag;

    /** Completed Flag */
    private Integer completedFlag;

    /** QTY Map */
    private Map<Integer, BigDecimal> qtyMap;

    /** Left QTY Map (NIRD Data Use) */
    private Map<Integer, BigDecimal> leftQtyMap;

    /** Original QTY Map (Plan MOD Use) */
    private Map<Integer, BigDecimal> originalQtyMap;

    /** MOD Entity */
    private CPSSMF11PlanEntity modEntity;

    /** Column Type */
    private int columnType;

    /** NIRD update flag */
    private boolean isNirdUpdate;

    /** Exist Part List */
    private List<TntSsPart> existPartList;

    /** Remove Flag */
    private boolean isRemoved;

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
     * Get the custId.
     *
     * @return custId
     */
    public Integer getCustId() {
        return this.custId;
    }

    /**
     * Set the custId.
     *
     * @param custId custId
     */
    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    /**
     * Get the currentCustomers.
     *
     * @return currentCustomers
     */
    public List<Integer> getCurrentCustomers() {
        return this.currentCustomers;
    }

    /**
     * Set the currentCustomers.
     *
     * @param currentCustomers currentCustomers
     */
    public void setCurrentCustomers(List<Integer> currentCustomers) {
        this.currentCustomers = currentCustomers;
    }

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
     * Get the impPoNo.
     *
     * @return impPoNo
     */
    public String getImpPoNo() {
        return this.impPoNo;
    }

    /**
     * Set the impPoNo.
     *
     * @param impPoNo impPoNo
     */
    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
    }

    /**
     * Get the customerOrderNo.
     *
     * @return customerOrderNo
     */
    public String getCustomerOrderNo() {
        return this.customerOrderNo;
    }

    /**
     * Set the customerOrderNo.
     *
     * @param customerOrderNo customerOrderNo
     */
    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
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
     * Get the ssPlanId.
     *
     * @return ssPlanId
     */
    public Integer getSsPlanId() {
        return this.ssPlanId;
    }

    /**
     * Set the ssPlanId.
     *
     * @param ssPlanId ssPlanId
     */
    public void setSsPlanId(Integer ssPlanId) {
        this.ssPlanId = ssPlanId;
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
     * Get the invoiceId.
     *
     * @return invoiceId
     */
    public Integer getInvoiceId() {
        return this.invoiceId;
    }

    /**
     * Set the invoiceId.
     *
     * @param invoiceId invoiceId
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
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
     * Get the transportMode.
     *
     * @return transportMode
     */
    public Integer getTransportMode() {
        return this.transportMode;
    }

    /**
     * Set the transportMode.
     *
     * @param transportMode transportMode
     */
    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * Get the vanningDate.
     *
     * @return vanningDate
     */
    public Date getVanningDate() {
        return this.vanningDate;
    }

    /**
     * Set the vanningDate.
     *
     * @param vanningDate vanningDate
     */
    public void setVanningDate(Date vanningDate) {
        this.vanningDate = vanningDate;
    }

    /**
     * Get the etd.
     *
     * @return etd
     */
    public Date getEtd() {
        return this.etd;
    }

    /**
     * Set the etd.
     *
     * @param etd etd
     */
    public void setEtd(Date etd) {
        this.etd = etd;
    }

    /**
     * Get the eta.
     *
     * @return eta
     */
    public Date getEta() {
        return this.eta;
    }

    /**
     * Set the eta.
     *
     * @param eta eta
     */
    public void setEta(Date eta) {
        this.eta = eta;
    }

    /**
     * Get the ccDate.
     *
     * @return ccDate
     */
    public Date getCcDate() {
        return this.ccDate;
    }

    /**
     * Set the ccDate.
     *
     * @param ccDate ccDate
     */
    public void setCcDate(Date ccDate) {
        this.ccDate = ccDate;
    }

    /**
     * Get the inboundPlanDate.
     *
     * @return inboundPlanDate
     */
    public Date getInboundPlanDate() {
        return this.inboundPlanDate;
    }

    /**
     * Set the inboundPlanDate.
     *
     * @param inboundPlanDate inboundPlanDate
     */
    public void setInboundPlanDate(Date inboundPlanDate) {
        this.inboundPlanDate = inboundPlanDate;
    }

    /**
     * Get the inboundActualDate.
     *
     * @return inboundActualDate
     */
    public Date getInboundActualDate() {
        return this.inboundActualDate;
    }

    /**
     * Set the inboundActualDate.
     *
     * @param inboundActualDate inboundActualDate
     */
    public void setInboundActualDate(Date inboundActualDate) {
        this.inboundActualDate = inboundActualDate;
    }

    /**
     * Get the revisionReason.
     *
     * @return revisionReason
     */
    public String getRevisionReason() {
        return this.revisionReason;
    }

    /**
     * Set the revisionReason.
     *
     * @param revisionReason revisionReason
     */
    public void setRevisionReason(String revisionReason) {
        this.revisionReason = revisionReason;
    }

    /**
     * Get the originalVersion.
     *
     * @return originalVersion
     */
    public Integer getOriginalVersion() {
        return this.originalVersion;
    }

    /**
     * Set the originalVersion.
     *
     * @param originalVersion originalVersion
     */
    public void setOriginalVersion(Integer originalVersion) {
        this.originalVersion = originalVersion;
    }

    /**
     * Get the revisionVersion.
     *
     * @return revisionVersion
     */
    public Integer getRevisionVersion() {
        return this.revisionVersion;
    }

    /**
     * Set the revisionVersion.
     *
     * @param revisionVersion revisionVersion
     */
    public void setRevisionVersion(Integer revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    /**
     * Get the nirdFlag.
     *
     * @return nirdFlag
     */
    public Integer getNirdFlag() {
        return this.nirdFlag;
    }

    /**
     * Set the nirdFlag.
     *
     * @param nirdFlag nirdFlag
     */
    public void setNirdFlag(Integer nirdFlag) {
        this.nirdFlag = nirdFlag;
    }

    /**
     * Get the completedFlag.
     *
     * @return completedFlag
     */
    public Integer getCompletedFlag() {
        return this.completedFlag;
    }

    /**
     * Set the completedFlag.
     *
     * @param completedFlag completedFlag
     */
    public void setCompletedFlag(Integer completedFlag) {
        this.completedFlag = completedFlag;
    }

    /**
     * Get the qtyMap.
     *
     * @return qtyMap
     */
    public Map<Integer, BigDecimal> getQtyMap() {
        return this.qtyMap;
    }

    /**
     * Set the qtyMap.
     *
     * @param qtyMap qtyMap
     */
    public void setQtyMap(Map<Integer, BigDecimal> qtyMap) {
        this.qtyMap = qtyMap;
    }

    /**
     * Get the leftQtyMap.
     *
     * @return leftQtyMap
     */
    public Map<Integer, BigDecimal> getLeftQtyMap() {
        return this.leftQtyMap;
    }

    /**
     * Set the leftQtyMap.
     *
     * @param leftQtyMap leftQtyMap
     */
    public void setLeftQtyMap(Map<Integer, BigDecimal> leftQtyMap) {
        this.leftQtyMap = leftQtyMap;
    }

    /**
     * Get the originalQtyMap.
     *
     * @return originalQtyMap
     */
    public Map<Integer, BigDecimal> getOriginalQtyMap() {
        return this.originalQtyMap;
    }

    /**
     * Set the originalQtyMap.
     *
     * @param originalQtyMap originalQtyMap
     */
    public void setOriginalQtyMap(Map<Integer, BigDecimal> originalQtyMap) {
        this.originalQtyMap = originalQtyMap;
    }

    /**
     * Get the modEntity.
     *
     * @return modEntity
     */
    public CPSSMF11PlanEntity getModEntity() {
        return this.modEntity;
    }

    /**
     * Set the modEntity.
     *
     * @param modEntity modEntity
     */
    public void setModEntity(CPSSMF11PlanEntity modEntity) {
        this.modEntity = modEntity;
    }

    /**
     * Get the columnType.
     *
     * @return columnType
     */
    public int getColumnType() {
        return this.columnType;
    }

    /**
     * Set the columnType.
     *
     * @param columnType columnType
     */
    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    /**
     * Get the isNirdUpdate.
     *
     * @return isNirdUpdate
     */
    public boolean isNirdUpdate() {
        return this.isNirdUpdate;
    }

    /**
     * Set the isNirdUpdate.
     *
     * @param isNirdUpdate isNirdUpdate
     */
    public void setNirdUpdate(boolean isNirdUpdate) {
        this.isNirdUpdate = isNirdUpdate;
    }

    /**
     * Get the existPartList.
     *
     * @return existPartList
     */
    public List<TntSsPart> getExistPartList() {
        return this.existPartList;
    }

    /**
     * Set the existPartList.
     *
     * @param existPartList existPartList
     */
    public void setExistPartList(List<TntSsPart> existPartList) {
        this.existPartList = existPartList;
    }

    /**
     * Get the isRemoved.
     *
     * @return isRemoved
     */
    public boolean isRemoved() {
        return this.isRemoved;
    }

    /**
     * Set the isRemoved.
     *
     * @param isRemoved isRemoved
     */
    public void setRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

}
