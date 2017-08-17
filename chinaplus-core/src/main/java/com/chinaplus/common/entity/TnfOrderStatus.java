package com.chinaplus.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * The persistent class for the TNF_ORDER_STATUS database table.
 * 
 */
@Entity(name = "TNF_ORDER_STATUS")
public class TnfOrderStatus extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNF_ORDER_STATUS_ORDERSTATUSID_GENERATOR",
        sequenceName = "SEQ_TNF_ORDER_STATUS",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNF_ORDER_STATUS_ORDERSTATUSID_GENERATOR")
    @Column(name = "ORDER_STATUS_ID")
    private Integer orderStatusId;

    @Column(name = "BUSINESS_PATTERN")
    private Integer businessPattern;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CUSTOMER_ORDER_NO")
    private String customerOrderNo;

    @Column(name = "EXP_INBOUND_QTY")
    private BigDecimal expInboundQty;

    @Column(name = "EXP_ONSHIPPING_QTY")
    private BigDecimal expOnshippingQty;

    @Column(name = "EXP_OUTBOUND_QTY")
    private BigDecimal expOutboundQty;

    @Column(name = "EXP_PO_NO")
    private String expPoNo;

    @Column(name = "FORCE_COMPLETED_QTY")
    private BigDecimal forceCompletedQty;

    @Column(name = "IMP_ADJUSTED_QTY")
    private BigDecimal impAdjustedQty;

    @Column(name = "IMP_DELIVERED_QTY")
    private BigDecimal impDeliveredQty;

    @Column(name = "IMP_ECI_QTY")
    private BigDecimal impEciQty;

    @Column(name = "IMP_NG_QTY")
    private BigDecimal impNgQty;

    @Column(name = "IMP_PO_NO")
    private String impPoNo;

    @Column(name = "IMP_PREPARE_OB_QTY")
    private BigDecimal impPrepareObQty;

    @Column(name = "IMP_STOCK_QTY")
    private BigDecimal impStockQty;

    @Column(name = "IN_RACK_QTY")
    private BigDecimal inRackQty;
    
    @Column(name = "CANCELLED_QTY")
    private BigDecimal cancelledQty;

    @Column(name = "KANBAN_PLAN_NO")
    private String kanbanPlanNo;

    @Column(name = "ORDER_MONTH")
    private String orderMonth;

    @Column(name = "ORDER_QTY")
    private BigDecimal orderQty;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    @Column(name = "SHIPPING_STATUS_FLAG")
    private Integer shippingStatusFlag;

    @Column(name = "SUPPLIER_ID")
    private Integer supplierId;

    @Column(name = "TRANSFER_OUT_QTY")
    private BigDecimal transferOutQty;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    public TnfOrderStatus() {}

    public Integer getOrderStatusId() {
        return this.orderStatusId;
    }

    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
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

    public String getCustomerOrderNo() {
        return this.customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    public BigDecimal getExpInboundQty() {
        return this.expInboundQty;
    }

    public void setExpInboundQty(BigDecimal expInboundQty) {
        this.expInboundQty = expInboundQty;
    }

    public BigDecimal getExpOnshippingQty() {
        return this.expOnshippingQty;
    }

    public void setExpOnshippingQty(BigDecimal expOnshippingQty) {
        this.expOnshippingQty = expOnshippingQty;
    }

    public BigDecimal getExpOutboundQty() {
        return this.expOutboundQty;
    }

    public void setExpOutboundQty(BigDecimal expOutboundQty) {
        this.expOutboundQty = expOutboundQty;
    }

    public String getExpPoNo() {
        return this.expPoNo;
    }

    public void setExpPoNo(String expPoNo) {
        this.expPoNo = expPoNo;
    }

    public BigDecimal getForceCompletedQty() {
        return this.forceCompletedQty;
    }

    public void setForceCompletedQty(BigDecimal forceCompletedQty) {
        this.forceCompletedQty = forceCompletedQty;
    }

    public BigDecimal getImpAdjustedQty() {
        return this.impAdjustedQty;
    }

    public void setImpAdjustedQty(BigDecimal impAdjustedQty) {
        this.impAdjustedQty = impAdjustedQty;
    }

    public BigDecimal getImpDeliveredQty() {
        return this.impDeliveredQty;
    }

    public void setImpDeliveredQty(BigDecimal impDeliveredQty) {
        this.impDeliveredQty = impDeliveredQty;
    }

    public BigDecimal getImpEciQty() {
        return this.impEciQty;
    }

    public void setImpEciQty(BigDecimal impEciQty) {
        this.impEciQty = impEciQty;
    }

    public BigDecimal getImpNgQty() {
        return this.impNgQty;
    }

    public void setImpNgQty(BigDecimal impNgQty) {
        this.impNgQty = impNgQty;
    }

    public String getImpPoNo() {
        return this.impPoNo;
    }

    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
    }

    public BigDecimal getImpPrepareObQty() {
        return this.impPrepareObQty;
    }

    public void setImpPrepareObQty(BigDecimal impPrepareObQty) {
        this.impPrepareObQty = impPrepareObQty;
    }

    public BigDecimal getImpStockQty() {
        return this.impStockQty;
    }

    public void setImpStockQty(BigDecimal impStockQty) {
        this.impStockQty = impStockQty;
    }

    public BigDecimal getInRackQty() {
        return this.inRackQty;
    }

    public void setInRackQty(BigDecimal inRackQty) {
        this.inRackQty = inRackQty;
    }

    public String getKanbanPlanNo() {
        return this.kanbanPlanNo;
    }

    public void setKanbanPlanNo(String kanbanPlanNo) {
        this.kanbanPlanNo = kanbanPlanNo;
    }

    public String getOrderMonth() {
        return this.orderMonth;
    }

    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    public BigDecimal getOrderQty() {
        return this.orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public Integer getShippingStatusFlag() {
        return this.shippingStatusFlag;
    }

    public void setShippingStatusFlag(Integer shippingStatusFlag) {
        this.shippingStatusFlag = shippingStatusFlag;
    }

    public Integer getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getTransferOutQty() {
        return this.transferOutQty;
    }

    public void setTransferOutQty(BigDecimal transferOutQty) {
        this.transferOutQty = transferOutQty;
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

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get the cancelledQty.
     *
     * @return cancelledQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getCancelledQty() {
        return this.cancelledQty;
    }

    /**
     * Set the cancelledQty.
     *
     * @param cancelledQty cancelledQty
     *
     * @author liu_yinchuan
     */
    public void setCancelledQty(BigDecimal cancelledQty) {
        this.cancelledQty = cancelledQty;
        
    }

}