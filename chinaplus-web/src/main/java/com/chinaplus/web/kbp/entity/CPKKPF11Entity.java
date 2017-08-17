/**
 * CPKKPF11Entity.java
 * 
 * @screen CPKKPF11
 * @author shiyang
 */
package com.chinaplus.web.kbp.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.chinaplus.common.bean.SupplyChainEntity;
import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.common.util.JsonMonthSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Upload Monthly Kanban Plan Entity.
 */
public class CPKKPF11Entity extends SupplyChainEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Upload Result */
    private String uploadResult;

    /** Row No. (File.Row.Row No.) */
    private int rowNo;

    /** Col No. (File.Col.Col No.) */
    private int colNo;

    /** Seq No (File.Col.IssuedPlanDate Index, not colNo, from 0) */
    private int issuedPlanDateIndex;

    /** Seq No (File.Row.No.) */
    private String seqNo;

    /** Plant (File.Row.Plant) */
    private String plant;

    /** Plant (File.Row.AISIN Parts No.) */
    private String partsNo;

    /** Dock (File.Row.Dock) */
    private String dock;

    /** Box Type (File.Row.Box type) */
    private String boxType;

    /** Box No (File.Row.Box No.) */
    private String boxNo;

    /** SPQ (File.Row.Qty/Box) */
    private String spq;

    /** Order (File.Row.Order) */
    private String order;

    /** Kanban Qty (File.Row.Total Kanban Qty) */
    private String kanbanQty;

    /** Count */
    private int count;

    /** Kanban Id */
    private String kanbanId;

    /** Kanban Shipping Id */
    private int kanbanShippingId;

    /** Kanban Plan Id */
    private int kanbanPlanId;

    /** Kanban Plan No. */
    private String kanbanPlanNo;

    /** Air Flag */
    private int airFlag;

    /** Order Status Id */
    private int orderStatusId;

    // /** Parts ID */
    // private int partsId;

    // /** Supplier ID */
    // private int supplierId;

    /** Customer Code */
    private String customerCode;

    /** Supplier Code */
    private String supplierCode;

    /** Inactive Flag */
    private int inactiveFlag;

    /** Forecast Num */
    private int forecastNum;

    /** Order Month */
    private String orderMonth;

    /** Exp Cust Code (File.Row.Customer) */
    private String expCustCode;

    /** Supp Parts No. (File.Row.Customer Parts No.) */
    private String suppPartsNo;

    /** TTC Supp Code */
    private String ttcSuppCode;

    /** Revision Version */
    private String revisionVersion;

    /** Revision Code Set */
    private String revisionCodeSet;

    /** Revision Reason */
    private String revisionReason;

    /** Srbq */
    private BigDecimal srbq;

    /** Order Qty */
    private BigDecimal orderQty;

    /** On Shipping Qty */
    private BigDecimal onShippingQty;

    /** Inbound Qty */
    private BigDecimal inboundQty;

    /** Order Balance */
    private BigDecimal orderBalance;

    /** From Date */
    private Date fromDate;

    /** To Date */
    private Date toDate;

    /** Updated By */
    private int updatedBy;

    /** Updated Date */
    private Timestamp updatedDate;

    /** Row Kanban Qty (File.Row.Kanban Qty) [0]:ColNumber; [1]:Kanban Qty */
    private List<String[]> rowKanbanQty;

    /** Shipping Uuid */
    private String shippingUuid;

    /** Nird Flag */
    private int nirdFlag;

    /** Oiginal Version */
    private int originalVersion;

    /** Completed Flag */
    private int completedFlag;

    /** Qty */
    private BigDecimal qty;

    /** Plan Type */
    private int planType;

    /** Delivere Remark */
    private String delivereRemark;

    /** Delivered Date */
    @Temporal(TemporalType.DATE)
    private Date deliveredDate;

    /** Issue Remark */
    private String issueRemark;

    /** Issued Date */
    @Temporal(TemporalType.DATE)
    private Date issuedDate;

    /** Vanning Remark */
    private String vanningRemark;

    /** Digits */
    private int digits;

    /**
     * Get the uploadResult.
     *
     * @return uploadResult
     */
    public String getUploadResult() {
        return this.uploadResult;
    }

    /**
     * Set the uploadResult.
     *
     * @param uploadResult uploadResult
     */
    public void setUploadResult(String uploadResult) {
        this.uploadResult = uploadResult;
    }

    /**
     * Get the rowNo.
     *
     * @return rowNo
     */
    public int getRowNo() {
        return this.rowNo;
    }

    /**
     * Set the rowNo.
     *
     * @param rowNo rowNo
     */
    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    /**
     * Get the colNo.
     *
     * @return colNo
     */
    public int getColNo() {
        return this.colNo;
    }

    /**
     * Set the colNo.
     *
     * @param colNo colNo
     */
    public void setColNo(int colNo) {
        this.colNo = colNo;
    }

    /**
     * Get the issuedPlanDateIndex.
     *
     * @return issuedPlanDateIndex
     */
    public int getIssuedPlanDateIndex() {
        return this.issuedPlanDateIndex;
    }

    /**
     * Set the issuedPlanDateIndex.
     *
     * @param issuedPlanDateIndex issuedPlanDateIndex
     */
    public void setIssuedPlanDateIndex(int issuedPlanDateIndex) {
        this.issuedPlanDateIndex = issuedPlanDateIndex;
    }

    /**
     * Get the seqNo.
     *
     * @return seqNo
     */
    public String getSeqNo() {
        return this.seqNo;
    }

    /**
     * Set the seqNo.
     *
     * @param seqNo seqNo
     */
    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    /**
     * Get the plant.
     *
     * @return plant
     */
    public String getPlant() {
        return this.plant;
    }

    /**
     * Set the plant.
     *
     * @param plant plant
     */
    public void setPlant(String plant) {
        this.plant = plant;
    }

    /**
     * Get the partsNo.
     *
     * @return partsNo
     */
    public String getPartsNo() {
        return this.partsNo;
    }

    /**
     * Set the partsNo.
     *
     * @param partsNo partsNo
     */
    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }

    /**
     * Get the dock.
     *
     * @return dock
     */
    public String getDock() {
        return this.dock;
    }

    /**
     * Set the dock.
     *
     * @param dock dock
     */
    public void setDock(String dock) {
        this.dock = dock;
    }

    /**
     * Get the boxType.
     *
     * @return boxType
     */
    public String getBoxType() {
        return this.boxType;
    }

    /**
     * Set the boxType.
     *
     * @param boxType boxType
     */
    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    /**
     * Get the boxNo.
     *
     * @return boxNo
     */
    public String getBoxNo() {
        return this.boxNo;
    }

    /**
     * Set the boxNo.
     *
     * @param boxNo boxNo
     */
    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    /**
     * Get the spq.
     *
     * @return spq
     */
    public String getSpq() {
        return this.spq;
    }

    /**
     * Set the spq.
     *
     * @param spq spq
     */
    public void setSpq(String spq) {
        this.spq = spq;
    }

    /**
     * Get the order.
     *
     * @return order
     */
    public String getOrder() {
        return this.order;
    }

    /**
     * Set the order.
     *
     * @param order order
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * Get the kanbanQty.
     *
     * @return kanbanQty
     */
    public String getKanbanQty() {
        return this.kanbanQty;
    }

    /**
     * Set the kanbanQty.
     *
     * @param kanbanQty kanbanQty
     */
    public void setKanbanQty(String kanbanQty) {
        this.kanbanQty = kanbanQty;
    }

    /**
     * Get the count.
     *
     * @return count
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Set the count.
     *
     * @param count count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Get the kanbanId.
     *
     * @return kanbanId
     */
    public String getKanbanId() {
        return this.kanbanId;
    }

    /**
     * Set the kanbanId.
     *
     * @param kanbanId kanbanId
     */
    public void setKanbanId(String kanbanId) {
        this.kanbanId = kanbanId;
    }

    /**
     * Get the kanbanShippingId.
     *
     * @return kanbanShippingId
     */
    public int getKanbanShippingId() {
        return this.kanbanShippingId;
    }

    /**
     * Set the kanbanShippingId.
     *
     * @param kanbanShippingId kanbanShippingId
     */
    public void setKanbanShippingId(int kanbanShippingId) {
        this.kanbanShippingId = kanbanShippingId;
    }

    /**
     * Get the kanbanPlanId.
     *
     * @return kanbanPlanId
     */
    public int getKanbanPlanId() {
        return this.kanbanPlanId;
    }

    /**
     * Set the kanbanPlanId.
     *
     * @param kanbanPlanId kanbanPlanId
     */
    public void setKanbanPlanId(int kanbanPlanId) {
        this.kanbanPlanId = kanbanPlanId;
    }

    /**
     * Get the kanbanPlanNo.
     *
     * @return kanbanPlanNo
     */
    public String getKanbanPlanNo() {
        return this.kanbanPlanNo;
    }

    /**
     * Set the kanbanPlanNo.
     *
     * @param kanbanPlanNo kanbanPlanNo
     */
    public void setKanbanPlanNo(String kanbanPlanNo) {
        this.kanbanPlanNo = kanbanPlanNo;
    }

    /**
     * Get the airFlag.
     *
     * @return airFlag
     */
    public int getAirFlag() {
        return this.airFlag;
    }

    /**
     * Set the airFlag.
     *
     * @param airFlag airFlag
     */
    public void setAirFlag(int airFlag) {
        this.airFlag = airFlag;
    }

    /**
     * Get the orderStatusId.
     *
     * @return orderStatusId
     */
    public int getOrderStatusId() {
        return this.orderStatusId;
    }

    /**
     * Set the orderStatusId.
     *
     * @param orderStatusId orderStatusId
     */
    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    // /**
    // * Get the partsId.
    // *
    // * @return partsId
    // */
    // public int getPartsId() {
    // return this.partsId;
    // }
    //
    // /**
    // * Set the partsId.
    // *
    // * @param partsId partsId
    // */
    // public void setPartsId(int partsId) {
    // this.partsId = partsId;
    // }

    // /**
    // * Get the supplierId.
    // *
    // * @return supplierId
    // */
    // public int getSupplierId() {
    // return this.supplierId;
    // }
    //
    // /**
    // * Set the supplierId.
    // *
    // * @param supplierId supplierId
    // */
    // public void setSupplierId(int supplierId) {
    // this.supplierId = supplierId;
    // }

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
     * Get the inactiveFlag.
     *
     * @return inactiveFlag
     */
    public int getInactiveFlag() {
        return this.inactiveFlag;
    }

    /**
     * Set the inactiveFlag.
     *
     * @param inactiveFlag inactiveFlag
     */
    public void setInactiveFlag(int inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
    }

    /**
     * Get the forecastNum.
     *
     * @return forecastNum
     */
    public int getForecastNum() {
        return this.forecastNum;
    }

    /**
     * Set the forecastNum.
     *
     * @param forecastNum forecastNum
     */
    public void setForecastNum(int forecastNum) {
        this.forecastNum = forecastNum;
    }

    /**
     * Get the orderMonth.
     *
     * @return orderMonth
     */
    @JsonSerialize(using = JsonMonthSerializer.class)
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
     * Get the expCustCode.
     *
     * @return expCustCode
     */
    public String getExpCustCode() {
        return this.expCustCode;
    }

    /**
     * Set the expCustCode.
     *
     * @param expCustCode expCustCode
     */
    public void setExpCustCode(String expCustCode) {
        this.expCustCode = expCustCode;
    }

    /**
     * Get the suppPartsNo.
     *
     * @return suppPartsNo
     */
    public String getSuppPartsNo() {
        return this.suppPartsNo;
    }

    /**
     * Set the suppPartsNo.
     *
     * @param suppPartsNo suppPartsNo
     */
    public void setSuppPartsNo(String suppPartsNo) {
        this.suppPartsNo = suppPartsNo;
    }

    /**
     * Get the ttcSuppCode.
     *
     * @return ttcSuppCode
     */
    public String getTtcSuppCode() {
        return this.ttcSuppCode;
    }

    /**
     * Set the ttcSuppCode.
     *
     * @param ttcSuppCode ttcSuppCode
     */
    public void setTtcSuppCode(String ttcSuppCode) {
        this.ttcSuppCode = ttcSuppCode;
    }

    /**
     * Get the revisionVersion.
     *
     * @return revisionVersion
     */
    public String getRevisionVersion() {
        return this.revisionVersion;
    }

    /**
     * Set the revisionVersion.
     *
     * @param revisionVersion revisionVersion
     */
    public void setRevisionVersion(String revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    /**
     * Get the revisionCodeSet.
     *
     * @return revisionCodeSet
     */
    public String getRevisionCodeSet() {
        return this.revisionCodeSet;
    }

    /**
     * Set the revisionCodeSet.
     *
     * @param revisionCodeSet revisionCodeSet
     */
    public void setRevisionCodeSet(String revisionCodeSet) {
        this.revisionCodeSet = revisionCodeSet;
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
     * Get the srbq.
     *
     * @return srbq
     */
    public BigDecimal getSrbq() {
        return this.srbq;
    }

    /**
     * Set the srbq.
     *
     * @param srbq srbq
     */
    public void setSrbq(BigDecimal srbq) {
        this.srbq = srbq;
    }

    /**
     * Get the orderQty.
     *
     * @return orderQty
     */
    public BigDecimal getOrderQty() {
        return this.orderQty;
    }

    /**
     * Set the orderQty.
     *
     * @param orderQty orderQty
     */
    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    /**
     * Get the onShippingQty.
     *
     * @return onShippingQty
     */
    public BigDecimal getOnShippingQty() {
        return this.onShippingQty;
    }

    /**
     * Set the onShippingQty.
     *
     * @param onShippingQty onShippingQty
     */
    public void setOnShippingQty(BigDecimal onShippingQty) {
        this.onShippingQty = onShippingQty;
    }

    /**
     * Get the inboundQty.
     *
     * @return inboundQty
     */
    public BigDecimal getInboundQty() {
        return this.inboundQty;
    }

    /**
     * Set the inboundQty.
     *
     * @param inboundQty inboundQty
     */
    public void setInboundQty(BigDecimal inboundQty) {
        this.inboundQty = inboundQty;
    }

    /**
     * Get the orderBalance.
     *
     * @return orderBalance
     */
    public BigDecimal getOrderBalance() {
        return this.orderBalance;
    }

    /**
     * Set the orderBalance.
     *
     * @param orderBalance orderBalance
     */
    public void setOrderBalance(BigDecimal orderBalance) {
        this.orderBalance = orderBalance;
    }

    /**
     * Get the fromDate.
     *
     * @return fromDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Date getFromDate() {
        return this.fromDate;
    }

    /**
     * Set the fromDate.
     *
     * @param fromDate fromDate
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Get the toDate.
     *
     * @return toDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Date getToDate() {
        return this.toDate;
    }

    /**
     * Set the toDate.
     *
     * @param toDate toDate
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * Get the updatedBy.
     *
     * @return updatedBy
     */
    public int getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Set the updatedBy.
     *
     * @param updatedBy updatedBy
     */
    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the updatedDate.
     *
     * @return updatedDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
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
     * Get the rowKanbanQty.
     *
     * @return rowKanbanQty
     */
    public List<String[]> getRowKanbanQty() {
        return this.rowKanbanQty;
    }

    /**
     * Set the rowKanbanQty.
     *
     * @param rowKanbanQty rowKanbanQty
     */
    public void setRowKanbanQty(List<String[]> rowKanbanQty) {
        this.rowKanbanQty = rowKanbanQty;
    }

    /**
     * Get the shippingUuid.
     *
     * @return shippingUuid
     */
    public String getShippingUuid() {
        return this.shippingUuid;
    }

    /**
     * Set the shippingUuid.
     *
     * @param shippingUuid shippingUuid
     */
    public void setShippingUuid(String shippingUuid) {
        this.shippingUuid = shippingUuid;
    }

    /**
     * Get the nirdFlag.
     *
     * @return nirdFlag
     */
    public int getNirdFlag() {
        return this.nirdFlag;
    }

    /**
     * Set the nirdFlag.
     *
     * @param nirdFlag nirdFlag
     */
    public void setNirdFlag(int nirdFlag) {
        this.nirdFlag = nirdFlag;
    }

    /**
     * Get the originalVersion.
     *
     * @return originalVersion
     */
    public int getOriginalVersion() {
        return this.originalVersion;
    }

    /**
     * Set the originalVersion.
     *
     * @param originalVersion originalVersion
     */
    public void setOriginalVersion(int originalVersion) {
        this.originalVersion = originalVersion;
    }

    /**
     * Get the completedFlag.
     *
     * @return completedFlag
     */
    public int getCompletedFlag() {
        return this.completedFlag;
    }

    /**
     * Set the completedFlag.
     *
     * @param completedFlag completedFlag
     */
    public void setCompletedFlag(int completedFlag) {
        this.completedFlag = completedFlag;
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
     * Get the planType.
     *
     * @return planType
     */
    public int getPlanType() {
        return this.planType;
    }

    /**
     * Set the planType.
     *
     * @param planType planType
     */
    public void setPlanType(int planType) {
        this.planType = planType;
    }

    /**
     * Get the delivereRemark.
     *
     * @return delivereRemark
     */
    public String getDelivereRemark() {
        return this.delivereRemark;
    }

    /**
     * Set the delivereRemark.
     *
     * @param delivereRemark delivereRemark
     */
    public void setDelivereRemark(String delivereRemark) {
        this.delivereRemark = delivereRemark;
    }

    /**
     * Get the deliveredDate.
     *
     * @return deliveredDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Date getDeliveredDate() {
        return this.deliveredDate;
    }

    /**
     * Set the deliveredDate.
     *
     * @param deliveredDate deliveredDate
     */
    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    /**
     * Get the issueRemark.
     *
     * @return issueRemark
     */
    public String getIssueRemark() {
        return this.issueRemark;
    }

    /**
     * Set the issueRemark.
     *
     * @param issueRemark issueRemark
     */
    public void setIssueRemark(String issueRemark) {
        this.issueRemark = issueRemark;
    }

    /**
     * Get the issuedDate.
     *
     * @return issuedDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Date getIssuedDate() {
        return this.issuedDate;
    }

    /**
     * Set the issuedDate.
     *
     * @param issuedDate issuedDate
     */
    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    /**
     * Get the vanningRemark.
     *
     * @return vanningRemark
     */
    public String getVanningRemark() {
        return this.vanningRemark;
    }

    /**
     * Set the vanningRemark.
     *
     * @param vanningRemark vanningRemark
     */
    public void setVanningRemark(String vanningRemark) {
        this.vanningRemark = vanningRemark;
    }

    /**
     * Get the digits.
     *
     * @return digits
     */
    public int getDigits() {
        return this.digits;
    }

    /**
     * Set the digits.
     *
     * @param digits digits
     */
    public void setDigits(int digits) {
        this.digits = digits;
    }
}
