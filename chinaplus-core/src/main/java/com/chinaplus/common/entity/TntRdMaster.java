package com.chinaplus.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the TNT_RUNDOWN_MASTER database table.
 * 
 */
@Entity(name="TNR_RD_MASTER")
public class TntRdMaster extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="TNT_RUNDOWN_MASTER_RUNDOWNMASTERID_GENERATOR", sequenceName="SEQ_TNT_RUNDOWN_MASTER", allocationSize = 1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_RUNDOWN_MASTER_RUNDOWNMASTERID_GENERATOR")
    @Column(name = "RUNDOWN_MASTER_ID")
    private Integer rundownMasterId;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    @Column(name = "IMP_STOCK_FLAG")
    private Integer impStockFlag;

    @Column(name = "SA_CUST_STOCK_FLAG")
    private Integer saCustStockFlag;

    @Column(name = "SIMULATION_END_DATE_PATTERN")
    private Integer simulationEndDatePattern;

    @Column(name = "INVENTORY_BOX_FLAG")
    private Integer inventoryBoxFlag;

    @Column(name = "RUNDOWN_SAFETY_STOCK")
    private Integer rundownSafetyStock;

    @Column(name = "MIN_STOCK")
    private Integer minStock;

    @Column(name = "MIN_BOX")
    private Integer minBox;

    @Column(name = "ORDER_MONTH")
    private String orderMonth;

    @Column(name = "TARGET_MONTH")
    private String targetMonth;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_CFC_DATE")
    private Date endCfcDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_PFC_DATE")
    private Date endPfcDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "CUST_STOCK_END_DATE")
    private Date custStockEndDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUN_START_DATE")
    private Date runStartDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "RUN_END_DATE")
    private Date runEndDate;

    @Column(name = "EXP_BALANCE_ORDER")
    private BigDecimal expBalanceOrder;

    @Column(name = "EXP_WHS_STOCK")
    private BigDecimal expWhsStock;

    @Column(name = "ON_SHIPPING_STOCK")
    private BigDecimal onShippingStock;

    @Column(name = "IN_RACK_QTY")
    private BigDecimal inRackQty;

    @Column(name = "IMP_WHS_QTY")
    private BigDecimal impWhsQty;

    @Column(name = "PREPARED_OB_QTY")
    private BigDecimal preparedObQty;

    @Column(name = "NG_QTY")
    private BigDecimal ngQty;

    @Column(name = "ECI_ONHOLD_QTY")
    private BigDecimal eciOnholdQty;

    @Column(name = "PFC_QTY")
    private BigDecimal pfcQty;

    @Column(name = "ETD_DELAY_QTY")
    private BigDecimal etdDelayQty;

    @Column(name = "INBOUND_DELAY_QTY")
    private BigDecimal inboundDelayQty;

    @Column(name = "CUST_STOCK_QTY")
    private BigDecimal custStockQty;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "\"REMARK\"")
    private String remark;
    
    @Column(name = "\"DELI_QTY_OF_LAST_MONTH\"")
    private BigDecimal deliQtyOfLastMonth;

    // bi-directional many-to-one association to TntRundownDetail
    @OneToMany(mappedBy = "tntRundownMaster")
    private List<TntRdDetail> tntRundownDetails;

    // bi-directional many-to-one association to TntRundownDetail
    @OneToMany(mappedBy = "tntRundownMaster")
    private List<TntRdAttachCfc> tntRundownAttachs;

    // bi-directional many-to-one association to TntNotInRundown
    @OneToMany(mappedBy = "tntRundownMaster")
    private List<TntNotInRundown> tntNotInRundowns;

    public TntRdMaster() {}

    public Integer getRundownMasterId() {
        return this.rundownMasterId;
    }

    public void setRundownMasterId(Integer rundownMasterId) {
        this.rundownMasterId = rundownMasterId;
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

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
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

    public List<TntRdDetail> getTntRundownDetails() {
        return this.tntRundownDetails;
    }

    public void setTntRundownDetails(List<TntRdDetail> tntRundownDetails) {
        this.tntRundownDetails = tntRundownDetails;
    }

    public TntRdDetail addTntRundownDetail(TntRdDetail tntRundownDetail) {
        if(this.getTntRundownDetails() == null) {
            this.setTntRundownDetails(new ArrayList<TntRdDetail>());
        }
        
        getTntRundownDetails().add(tntRundownDetail);
        tntRundownDetail.setTntRundownMaster(this);

        return tntRundownDetail;
    }

    public TntRdDetail removeTntRundownDetail(TntRdDetail tntRundownDetail) {
        if(this.getTntRundownDetails() != null) {
            this.getTntRundownDetails().remove(tntRundownDetail);
        }
        tntRundownDetail.setTntRundownMaster(null);

        return tntRundownDetail;
    }

    public List<TntRdAttachCfc> getTntRundownAttachs() {
        return this.tntRundownAttachs;
    }

    public void setTntRundownAttachs(List<TntRdAttachCfc> tntRundownAttachs) {
        this.tntRundownAttachs = tntRundownAttachs;
    }

    public TntRdAttachCfc addTntRundownAttach(TntRdAttachCfc tntRundownAttach) {
        if(this.getTntRundownAttachs() == null) {
            this.setTntRundownAttachs(new ArrayList<TntRdAttachCfc>());
        }
        
        getTntRundownAttachs().add(tntRundownAttach);
        tntRundownAttach.setTntRundownMaster(this);

        return tntRundownAttach;
    }

    public TntRdAttachCfc removeTntRundownAttach(TntRdAttachCfc tntRundownAttach) {
        if(this.getTntRundownAttachs() != null) {
            this.getTntRundownAttachs().remove(tntRundownAttach);
        }
        tntRundownAttach.setTntRundownMaster(null);

        return tntRundownAttach;
    }

    public List<TntNotInRundown> getTntNotInRundowns() {
        return this.tntNotInRundowns;
    }

    public void setTntNotInRundowns(List<TntNotInRundown> tntNotInRundowns) {
        this.tntNotInRundowns = tntNotInRundowns;
    }

    public TntNotInRundown addTntNotInRundown(TntNotInRundown tntNotInRundown) {
        if(this.getTntNotInRundowns() == null) {
            this.setTntNotInRundowns(new ArrayList<TntNotInRundown>());
        }
        getTntNotInRundowns().add(tntNotInRundown);
        tntNotInRundown.setTntRundownMaster(this);

        return tntNotInRundown;
    }

    public TntNotInRundown removeTntNotInRundown(TntNotInRundown tntNotInRundown) {
        if(this.getTntNotInRundowns() != null) {
            this.getTntNotInRundowns().remove(tntNotInRundown);
        }
        tntNotInRundown.setTntRundownMaster(null);

        return tntNotInRundown;
    }

    public Integer getImpStockFlag() {
        return this.impStockFlag;
    }

    public void setImpStockFlag(Integer impStockFlag) {
        this.impStockFlag = impStockFlag;

    }

    public Integer getSaCustStockFlag() {
        return this.saCustStockFlag;
    }

    public void setSaCustStockFlag(Integer saCustStockFlag) {
        this.saCustStockFlag = saCustStockFlag;

    }

    public Integer getSimulationEndDatePattern() {
        return this.simulationEndDatePattern;
    }

    public void setSimulationEndDatePattern(Integer simulationEndDatePattern) {
        this.simulationEndDatePattern = simulationEndDatePattern;

    }

    public Integer getInventoryBoxFlag() {
        return this.inventoryBoxFlag;
    }

    public void setInventoryBoxFlag(Integer inventoryBoxFlag) {
        this.inventoryBoxFlag = inventoryBoxFlag;

    }

    public Integer getRundownSafetyStock() {
        return this.rundownSafetyStock;
    }

    public void setRundownSafetyStock(Integer rundownSafetyStock) {
        this.rundownSafetyStock = rundownSafetyStock;

    }

    public Integer getMinStock() {
        return this.minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;

    }

    public Integer getMinBox() {
        return this.minBox;
    }

    public void setMinBox(Integer minBox) {
        this.minBox = minBox;

    }

    public String getOrderMonth() {
        return this.orderMonth;
    }

    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;

    }

    public String getTargetMonth() {
        return this.targetMonth;
    }

    public void setTargetMonth(String targetMonth) {
        this.targetMonth = targetMonth;

    }

    public Date getEndCfcDate() {
        return this.endCfcDate;
    }

    public void setEndCfcDate(Date endCfcDate) {
        this.endCfcDate = endCfcDate;

    }

    public Date getEndPfcDate() {
        return this.endPfcDate;
    }

    public void setEndPfcDate(Date endPfcDate) {
        this.endPfcDate = endPfcDate;

    }

    public Date getCustStockEndDate() {
        return this.custStockEndDate;
    }

    public void setCustStockEndDate(Date custStockEndDate) {
        this.custStockEndDate = custStockEndDate;

    }

    public Date getRunStartDate() {
        return this.runStartDate;
    }

    public void setRunStartDate(Date runStartDate) {
        this.runStartDate = runStartDate;

    }

    public Date getRunEndDate() {
        return this.runEndDate;
    }

    public void setRunEndDate(Date runEndDate) {
        this.runEndDate = runEndDate;

    }

    public BigDecimal getExpBalanceOrder() {
        return this.expBalanceOrder;
    }

    public void setExpBalanceOrder(BigDecimal expBalanceOrder) {
        this.expBalanceOrder = expBalanceOrder;

    }

    public BigDecimal getExpWhsStock() {
        return this.expWhsStock;
    }

    public void setExpWhsStock(BigDecimal expWhsStock) {
        this.expWhsStock = expWhsStock;

    }

    public BigDecimal getOnShippingStock() {
        return this.onShippingStock;
    }

    public void setOnShippingStock(BigDecimal onShippingStock) {
        this.onShippingStock = onShippingStock;

    }

    public BigDecimal getInRackQty() {
        return this.inRackQty;
    }

    public void setInRackQty(BigDecimal inRackQty) {
        this.inRackQty = inRackQty;

    }

    public BigDecimal getImpWhsQty() {
        return this.impWhsQty;
    }

    public void setImpWhsQty(BigDecimal impWhsQty) {
        this.impWhsQty = impWhsQty;

    }

    public BigDecimal getPreparedObQty() {
        return this.preparedObQty;
    }

    public void setPreparedObQty(BigDecimal preparedObQty) {
        this.preparedObQty = preparedObQty;

    }

    public BigDecimal getPfcQty() {
        return this.pfcQty;
    }

    public void setPfcQty(BigDecimal pfcQty) {
        this.pfcQty = pfcQty;

    }

    public BigDecimal getEtdDelayQty() {
        return this.etdDelayQty;
    }

    public void setEtdDelayQty(BigDecimal etdDelayQty) {
        this.etdDelayQty = etdDelayQty;

    }

    public BigDecimal getInboundDelayQty() {
        return this.inboundDelayQty;
    }

    public void setInboundDelayQty(BigDecimal inboundDelayQty) {
        this.inboundDelayQty = inboundDelayQty;

    }

    public BigDecimal getCustStockQty() {
        return this.custStockQty;
    }

    public void setCustStockQty(BigDecimal custStockQty) {
        this.custStockQty = custStockQty;

    }

    /**
     * Get the ngQty.
     *
     * @return ngQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getNgQty() {
        return this.ngQty;
    }

    /**
     * Set the ngQty.
     *
     * @param ngQty ngQty
     *
     * @author liu_yinchuan
     */
    public void setNgQty(BigDecimal ngQty) {
        this.ngQty = ngQty;
        
    }

    /**
     * Get the eciOnholdQty.
     *
     * @return eciOnholdQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getEciOnholdQty() {
        return this.eciOnholdQty;
    }

    /**
     * Set the eciOnholdQty.
     *
     * @param eciOnholdQty eciOnholdQty
     *
     * @author liu_yinchuan
     */
    public void setEciOnholdQty(BigDecimal eciOnholdQty) {
        this.eciOnholdQty = eciOnholdQty;
        
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
        
    }

    /**
     * Get the deliQtyOfLastMonth.
     *
     * @return deliQtyOfLastMonth
     *
     * @author liu_yinchuan
     */
    public BigDecimal getDeliQtyOfLastMonth() {
        return this.deliQtyOfLastMonth;
    }

    /**
     * Set the deliQtyOfLastMonth.
     *
     * @param deliQtyOfLastMonth deliQtyOfLastMonth
     *
     * @author liu_yinchuan
     */
    public void setDeliQtyOfLastMonth(BigDecimal deliQtyOfLastMonth) {
        this.deliQtyOfLastMonth = deliQtyOfLastMonth;
        
    }

}