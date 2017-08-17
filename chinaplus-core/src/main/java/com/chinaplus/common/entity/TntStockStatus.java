package com.chinaplus.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.chinaplus.common.util.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The persistent class for the TNR_STOCK_STATUS database table.
 * 
 */
@Entity(name = "TNR_STOCK_STATUS")
public class TntStockStatus extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    //@SequenceGenerator(name = "TNT_STOCK_STATUS_STOCKSTATUSID_GENERATOR", sequenceName = "SEQ_TNT_STOCK_STATUS", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TNT_STOCK_STATUS_STOCKSTATUSID_GENERATOR")
    @Column(name = "STOCK_STATUS_ID")
    private Integer stockStatusId;

    @Column(name = "ALERT_LEVEL")
    private Integer alertLevel;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CUST_STOCK_QTY")
    private BigDecimal custStockQty;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "DELAY_ADJUSTMENT_PATTERN")
    private Integer delayAdjustmentPattern;

    @Column(name = "ECI_ONHOLD_QTY")
    private BigDecimal eciOnholdQty;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE_3")
    private Date endDate3;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE_F1")
    private Date endDateF1;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE_F2")
    private Date endDateF2;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE_F3")
    private Date endDateF3;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE_F4")
    private Date endDateF4;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE_F5")
    private Date endDateF5;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE_F6")
    private Date endDateF6;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE_TM")
    private Date endDateTm;

    @Temporal(TemporalType.DATE)
    @Column(name = "ENDING_STOCK_DATE")
    private Date endingStockDate;

    @Column(name = "ETD_DELAY_QTY")
    private BigDecimal etdDelayQty;

    @Column(name = "EXP_FUTURE_PLAN_ADD_AIR")
    private BigDecimal expFuturePlanAddAir;

    @Column(name = "EXP_FUTURE_PLAN_ADD_SEA")
    private BigDecimal expFuturePlanAddSea;

    @Column(name = "EXP_FUTURE_PLAN_MON_AIR")
    private BigDecimal expFuturePlanMonAir;

    @Column(name = "EXP_FUTURE_PLAN_MON_SEA")
    private BigDecimal expFuturePlanMonSea;

    @Column(name = "EXP_FUTURE_PLAN_QTY")
    private BigDecimal expFuturePlanQty;

    @Column(name = "EXP_ONSHIPPING_QTY")
    private BigDecimal expOnshippingQty;

    @Column(name = "EXP_PLAN_DELAY_ADD_AIR")
    private BigDecimal expPlanDelayAddAir;

    @Column(name = "EXP_PLAN_DELAY_ADD_SEA")
    private BigDecimal expPlanDelayAddSea;

    @Column(name = "EXP_PLAN_DELAY_MON_AIR")
    private BigDecimal expPlanDelayMonAir;

    @Column(name = "EXP_PLAN_DELAY_MON_SEA")
    private BigDecimal expPlanDelayMonSea;

    @Column(name = "EXP_PLAN_DELAY_QTY")
    private BigDecimal expPlanDelayQty;

    @Column(name = "EXP_STOCK_QTY")
    private BigDecimal expStockQty;

    @Column(name = "FLUCTUATION_RATIO_4")
    private BigDecimal fluctuationRatio4;

    @Column(name = "FORECAST_NUM")
    private Integer forecastNum;

    @Column(name = "GAP_VALUE_4")
    private BigDecimal gapValue4;

    @Column(name = "IMP_WHS_QTY")
    private BigDecimal impWhsQty;

    @Column(name = "IN_RACK_QTY")
    private BigDecimal inRackQty;

    @Column(name = "INBOUND_DELAY_QTY")
    private BigDecimal inboundDelayQty;

    @Column(name = "INVENTORY_BOX_FLAG")
    private Integer inventoryBoxFlag;

    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_OB_ACTUAL_DATE")
    private Date lastObActualDate;

    @Column(name = "EXP_PARTS_SET")
    private String expPartsSet;
    
    @Column(name = "MAX_ALARM_1A")
    private Integer  maxAlarm1a;

    @Column(name = "MAX_ALARM_1B")
    private Integer maxAlarm1b;

    @Column(name = "MAX_ALARM_F1")
    private Integer maxAlarmF1;

    @Column(name = "MAX_ALARM_F2")
    private Integer maxAlarmF2;

    @Column(name = "MAX_ALARM_F3")
    private Integer maxAlarmF3;

    @Column(name = "MAX_ALARM_F4")
    private Integer maxAlarmF4;

    @Column(name = "MAX_ALARM_F5")
    private Integer maxAlarmF5;

    @Column(name = "MAX_ALARM_F6")
    private Integer maxAlarmF6;

    @Column(name = "MAX_ALARM_TM")
    private Integer maxAlarmTm;

    @Column(name = "MAX_BOX")
    private Integer maxBox;

    @Column(name = "MAX_BOXES_F1")
    private BigDecimal maxBoxesF1;

    @Column(name = "MAX_BOXES_F2")
    private BigDecimal maxBoxesF2;

    @Column(name = "MAX_BOXES_F3")
    private BigDecimal maxBoxesF3;

    @Column(name = "MAX_BOXES_F4")
    private BigDecimal maxBoxesF4;

    @Column(name = "MAX_BOXES_F5")
    private BigDecimal maxBoxesF5;

    @Column(name = "MAX_BOXES_F6")
    private BigDecimal maxBoxesF6;

    @Column(name = "MAX_BOXES_TM")
    private BigDecimal maxBoxesTm;

    @Column(name = "MAX_STOCK")
    private Integer maxStock;

    @Column(name = "MAX_STOCK_DAY_F1")
    private BigDecimal maxStockDayF1;

    @Column(name = "MAX_STOCK_DAY_F2")
    private BigDecimal maxStockDayF2;

    @Column(name = "MAX_STOCK_DAY_F3")
    private BigDecimal maxStockDayF3;

    @Column(name = "MAX_STOCK_DAY_F4")
    private BigDecimal maxStockDayF4;

    @Column(name = "MAX_STOCK_DAY_F5")
    private BigDecimal maxStockDayF5;

    @Column(name = "MAX_STOCK_DAY_F6")
    private BigDecimal maxStockDayF6;

    @Column(name = "MAX_STOCK_DAY_TM")
    private BigDecimal maxStockDayTm;

    @Column(name = "MIN_ALARM_1A")
    private Integer minAlarm1a;

    @Column(name = "MIN_ALARM_1B")
    private Integer minAlarm1b;

    @Column(name = "MIN_ALARM_F1")
    private Integer minAlarmF1;

    @Column(name = "MIN_ALARM_F2")
    private Integer minAlarmF2;

    @Column(name = "MIN_ALARM_F3")
    private Integer minAlarmF3;

    @Column(name = "MIN_ALARM_F4")
    private Integer minAlarmF4;

    @Column(name = "MIN_ALARM_F5")
    private Integer minAlarmF5;

    @Column(name = "MIN_ALARM_F6")
    private Integer minAlarmF6;

    @Column(name = "MIN_ALARM_TM")
    private Integer minAlarmTm;

    @Column(name = "MIN_BOX")
    private Integer minBox;

    @Column(name = "MIN_BOXES_F1")
    private BigDecimal minBoxesF1;

    @Column(name = "MIN_BOXES_F2")
    private BigDecimal minBoxesF2;

    @Column(name = "MIN_BOXES_F3")
    private BigDecimal minBoxesF3;

    @Column(name = "MIN_BOXES_F4")
    private BigDecimal minBoxesF4;

    @Column(name = "MIN_BOXES_F5")
    private BigDecimal minBoxesF5;

    @Column(name = "MIN_BOXES_F6")
    private BigDecimal minBoxesF6;

    @Column(name = "MIN_BOXES_TM")
    private BigDecimal minBoxesTm;

    @Column(name = "MIN_STOCK")
    private Integer minStock;

    @Column(name = "MIN_STOCK_DAY_F1")
    private BigDecimal minStockDayF1;

    @Column(name = "MIN_STOCK_DAY_F2")
    private BigDecimal minStockDayF2;

    @Column(name = "MIN_STOCK_DAY_F3")
    private BigDecimal minStockDayF3;

    @Column(name = "MIN_STOCK_DAY_F4")
    private BigDecimal minStockDayF4;

    @Column(name = "MIN_STOCK_DAY_F5")
    private BigDecimal minStockDayF5;

    @Column(name = "MIN_STOCK_DAY_F6")
    private BigDecimal minStockDayF6;

    @Column(name = "MIN_STOCK_DAY_TM")
    private BigDecimal minStockDayTm;

    @Temporal(TemporalType.DATE)
    @Column(name = "NEXT_INBOUND_DATE")
    private Date nextInboundDate;

    @Column(name = "NEXT_INBOUND_QTY")
    private BigDecimal nextInboundQty;

    @Column(name = "NEXT_INVOICE")
    private String nextInvoice;

    @Column(name = "NG_ONHOLD_QTY")
    private BigDecimal ngOnholdQty;

    @Column(name = "NO_CFC_FLAG_F1")
    private Integer noCfcFlagF1;

    @Column(name = "NO_CFC_FLAG_F2")
    private Integer noCfcFlagF2;

    @Column(name = "NO_CFC_FLAG_F3")
    private Integer noCfcFlagF3;

    @Column(name = "NO_CFC_FLAG_F4")
    private Integer noCfcFlagF4;

    @Column(name = "NO_CFC_FLAG_F5")
    private Integer noCfcFlagF5;

    @Column(name = "NO_CFC_FLAG_F6")
    private Integer noCfcFlagF6;

    @Column(name = "NO_MASTER_1A")
    private Integer noMaster1a;

    @Column(name = "NO_MASTER_1B")
    private Integer noMaster1b;

    @Column(name = "NO_MASTER_3")
    private Integer noMaster3;

    @Column(name = "NO_MASTER_MAX_TM")
    private Integer noMasterMaxTm;

    @Column(name = "NO_MASTER_MIN_TM")
    private Integer noMasterMinTm;

    @Column(name = "NO_MASTER_PLAN_1A")
    private Integer noMasterPlan1a;

    @Column(name = "NO_MASTER_PLAN_1B")
    private Integer noMasterPlan1b;

    @Column(name = "NO_PFC_FLAG_F1")
    private Integer noPfcFlagF1;

    @Column(name = "NO_PFC_FLAG_F2")
    private Integer noPfcFlagF2;

    @Column(name = "NO_PFC_FLAG_F3")
    private Integer noPfcFlagF3;

    @Column(name = "NO_PFC_FLAG_F4")
    private Integer noPfcFlagF4;

    @Column(name = "NO_PFC_FLAG_F5")
    private Integer noPfcFlagF5;

    @Column(name = "NO_PFC_FLAG_F6")
    private Integer noPfcFlagF6;

    @Column(name = "NO_PFC_MAX_F1")
    private Integer noPfcMaxF1;

    @Column(name = "NO_PFC_MAX_F2")
    private Integer noPfcMaxF2;

    @Column(name = "NO_PFC_MAX_F3")
    private Integer noPfcMaxF3;

    @Column(name = "NO_PFC_MAX_F4")
    private Integer noPfcMaxF4;

    @Column(name = "NO_PFC_MAX_F5")
    private Integer noPfcMaxF5;

    @Column(name = "NO_PFC_MAX_F6")
    private Integer noPfcMaxF6;
    
    @Column(name = "NO_PFC_MIN_F1")
    private Integer noPfcMinF1;

    @Column(name = "NO_PFC_MIN_F2")
    private Integer noPfcMinF2;

    @Column(name = "NO_PFC_MIN_F3")
    private Integer noPfcMinF3;

    @Column(name = "NO_PFC_MIN_F4")
    private Integer noPfcMinF4;

    @Column(name = "NO_PFC_MIN_F5")
    private Integer noPfcMinF5;

    @Column(name = "NO_PFC_MIN_F6")
    private Integer noPfcMinF6;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "ORDER_SAFETY_STOCK")
    private Integer orderSafetyStock;

    @Column(name = "OS_CUST_STOCK_FLAG")
    private Integer osCustStockFlag;

    @Column(name = "OUTBOUND_FLUCTUATION")
    private BigDecimal outboundFluctuation;

    @Temporal(TemporalType.DATE)
    @Column(name = "OVER_RATIO_DATE_4")
    private Date overRatioDate4;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    @Column(name = "PREPARED_OB_QTY")
    private BigDecimal preparedObQty;

    @Column(name = "SA_CUST_STOCK_FLAG")
    private Integer saCustStockFlag;

    @Column(name = "SAFETY_ADD_ON_3")
    private BigDecimal safetyAddOn3;

    @Column(name = "SAFETY_ALARM_3")
    private Integer safetyAlarm3;

    @Column(name = "SPQ")
    private BigDecimal spq;

    @Column(name = "STOCK_BOXES_1A")
    private BigDecimal stockBoxes1a;

    @Column(name = "STOCK_BOXES_1B")
    private BigDecimal stockBoxes1b;

    @Temporal(TemporalType.DATE)
    @Column(name = "STOCK_DATE")
    private Date stockDate;

    @Column(name = "STOCK_DAY_3")
    private BigDecimal stockDay3;

    @Column(name = "STOCK_DAYS_1A")
    private BigDecimal stockDays1a;

    @Column(name = "STOCK_DAYS_1B")
    private BigDecimal stockDays1b;

    @Column(name = "STOCK_DAYS_PLAN_1A")
    private BigDecimal stockDaysPlan1a;

    @Column(name = "STOCK_DAYS_PLAN_1B")
    private BigDecimal stockDaysPlan1b;

    @Column(name = "STOCK_QTY_1A")
    private BigDecimal stockQty1a;

    @Column(name = "STOCK_QTY_1B")
    private BigDecimal stockQty1b;

    @Temporal(TemporalType.DATE)
    @Column(name = "SHORTAGE_DATE_F1")
    private Date shortageDateF1;

    @Temporal(TemporalType.DATE)
    @Column(name = "SHORTAGE_DATE_F2")
    private Date shortageDateF2;

    @Temporal(TemporalType.DATE)
    @Column(name = "SHORTAGE_DATE_TM")
    private Date shortageDateTm;

    @Column(name = "SHORTAGE_ADD_ON_3")
    private BigDecimal shortageAddOn3;

    @Temporal(TemporalType.DATE)
    @Column(name = "SHORTAGE_DATE_3")
    private Date shortageDate3;

    @Temporal(TemporalType.DATE)
    @Column(name = "SHORTAGE_DATE_F3")
    private Date shortageDateF3;

    @Temporal(TemporalType.DATE)
    @Column(name = "SHORTAGE_DATE_F4")
    private Date shortageDateF4;

    @Temporal(TemporalType.DATE)
    @Column(name = "SHORTAGE_DATE_F5")
    private Date shortageDateF5;

    @Temporal(TemporalType.DATE)
    @Column(name = "SHORTAGE_DATE_F6")
    private Date shortageDateF6;

    @Column(name = "SUPPLIER_ID")
    private Integer supplierId;

    @Column(name = "TOTAL_CFC_QTY_4")
    private BigDecimal totalCfcQty4;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    public TntStockStatus() {}

    public Integer getStockStatusId() {
        return this.stockStatusId;
    }

    public void setStockStatusId(Integer stockStatusId) {
        this.stockStatusId = stockStatusId;
    }

    public Integer getAlertLevel() {
        return this.alertLevel;
    }

    public void setAlertLevel(Integer alertLevel) {
        this.alertLevel = alertLevel;
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

    public BigDecimal getCustStockQty() {
        return this.custStockQty;
    }

    public void setCustStockQty(BigDecimal custStockQty) {
        this.custStockQty = custStockQty;
    }

    public Integer getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getDelayAdjustmentPattern() {
        return this.delayAdjustmentPattern;
    }

    public void setDelayAdjustmentPattern(Integer delayAdjustmentPattern) {
        this.delayAdjustmentPattern = delayAdjustmentPattern;
    }

    public BigDecimal getEciOnholdQty() {
        return this.eciOnholdQty;
    }

    public void setEciOnholdQty(BigDecimal eciOnholdQty) {
        this.eciOnholdQty = eciOnholdQty;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEndDate3() {
        return this.endDate3;
    }

    public void setEndDate3(Date endDate3) {
        this.endDate3 = endDate3;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEndDateF1() {
        return this.endDateF1;
    }

    public void setEndDateF1(Date endDateF1) {
        this.endDateF1 = endDateF1;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEndDateF2() {
        return this.endDateF2;
    }

    public void setEndDateF2(Date endDateF2) {
        this.endDateF2 = endDateF2;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEndDateF3() {
        return this.endDateF3;
    }

    public void setEndDateF3(Date endDateF3) {
        this.endDateF3 = endDateF3;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEndDateF4() {
        return this.endDateF4;
    }

    public void setEndDateF4(Date endDateF4) {
        this.endDateF4 = endDateF4;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEndDateF5() {
        return this.endDateF5;
    }

    public void setEndDateF5(Date endDateF5) {
        this.endDateF5 = endDateF5;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEndDateF6() {
        return this.endDateF6;
    }

    public void setEndDateF6(Date endDateF6) {
        this.endDateF6 = endDateF6;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEndDateTm() {
        return this.endDateTm;
    }

    public void setEndDateTm(Date endDateTm) {
        this.endDateTm = endDateTm;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEndingStockDate() {
        return this.endingStockDate;
    }

    public void setEndingStockDate(Date endingStockDate) {
        this.endingStockDate = endingStockDate;
    }

    public BigDecimal getEtdDelayQty() {
        return this.etdDelayQty;
    }

    public void setEtdDelayQty(BigDecimal etdDelayQty) {
        this.etdDelayQty = etdDelayQty;
    }

    public BigDecimal getExpFuturePlanAddAir() {
        return this.expFuturePlanAddAir;
    }

    public void setExpFuturePlanAddAir(BigDecimal expFuturePlanAddAir) {
        this.expFuturePlanAddAir = expFuturePlanAddAir;
    }

    public BigDecimal getExpFuturePlanAddSea() {
        return this.expFuturePlanAddSea;
    }

    public void setExpFuturePlanAddSea(BigDecimal expFuturePlanAddSea) {
        this.expFuturePlanAddSea = expFuturePlanAddSea;
    }

    public BigDecimal getExpFuturePlanMonAir() {
        return this.expFuturePlanMonAir;
    }

    public void setExpFuturePlanMonAir(BigDecimal expFuturePlanMonAir) {
        this.expFuturePlanMonAir = expFuturePlanMonAir;
    }

    public BigDecimal getExpFuturePlanMonSea() {
        return this.expFuturePlanMonSea;
    }

    public void setExpFuturePlanMonSea(BigDecimal expFuturePlanMonSea) {
        this.expFuturePlanMonSea = expFuturePlanMonSea;
    }

    public BigDecimal getExpFuturePlanQty() {
        return this.expFuturePlanQty;
    }

    public void setExpFuturePlanQty(BigDecimal expFuturePlanQty) {
        this.expFuturePlanQty = expFuturePlanQty;
    }

    public BigDecimal getExpOnshippingQty() {
        return this.expOnshippingQty;
    }

    public void setExpOnshippingQty(BigDecimal expOnshippingQty) {
        this.expOnshippingQty = expOnshippingQty;
    }

    public BigDecimal getExpPlanDelayAddAir() {
        return this.expPlanDelayAddAir;
    }

    public void setExpPlanDelayAddAir(BigDecimal expPlanDelayAddAir) {
        this.expPlanDelayAddAir = expPlanDelayAddAir;
    }

    public BigDecimal getExpPlanDelayAddSea() {
        return this.expPlanDelayAddSea;
    }

    public void setExpPlanDelayAddSea(BigDecimal expPlanDelayAddSea) {
        this.expPlanDelayAddSea = expPlanDelayAddSea;
    }

    public BigDecimal getExpPlanDelayMonAir() {
        return this.expPlanDelayMonAir;
    }

    public void setExpPlanDelayMonAir(BigDecimal expPlanDelayMonAir) {
        this.expPlanDelayMonAir = expPlanDelayMonAir;
    }

    public BigDecimal getExpPlanDelayMonSea() {
        return this.expPlanDelayMonSea;
    }

    public void setExpPlanDelayMonSea(BigDecimal expPlanDelayMonSea) {
        this.expPlanDelayMonSea = expPlanDelayMonSea;
    }

    public BigDecimal getExpPlanDelayQty() {
        return this.expPlanDelayQty;
    }

    public void setExpPlanDelayQty(BigDecimal expPlanDelayQty) {
        this.expPlanDelayQty = expPlanDelayQty;
    }

    public BigDecimal getExpStockQty() {
        return this.expStockQty;
    }

    public void setExpStockQty(BigDecimal expStockQty) {
        this.expStockQty = expStockQty;
    }

    public BigDecimal getFluctuationRatio4() {
        return this.fluctuationRatio4;
    }

    public void setFluctuationRatio4(BigDecimal fluctuationRatio4) {
        this.fluctuationRatio4 = fluctuationRatio4;
    }

    public Integer getForecastNum() {
        return this.forecastNum;
    }

    public void setForecastNum(Integer forecastNum) {
        this.forecastNum = forecastNum;
    }

    public BigDecimal getGapValue4() {
        return this.gapValue4;
    }

    public void setGapValue4(BigDecimal gapValue4) {
        this.gapValue4 = gapValue4;
    }

    public BigDecimal getImpWhsQty() {
        return this.impWhsQty;
    }

    public void setImpWhsQty(BigDecimal impWhsQty) {
        this.impWhsQty = impWhsQty;
    }

    public BigDecimal getInRackQty() {
        return this.inRackQty;
    }

    public void setInRackQty(BigDecimal inRackQty) {
        this.inRackQty = inRackQty;
    }

    public BigDecimal getInboundDelayQty() {
        return this.inboundDelayQty;
    }

    public void setInboundDelayQty(BigDecimal inboundDelayQty) {
        this.inboundDelayQty = inboundDelayQty;
    }

    public Integer getInventoryBoxFlag() {
        return this.inventoryBoxFlag;
    }

    public void setInventoryBoxFlag(Integer inventoryBoxFlag) {
        this.inventoryBoxFlag = inventoryBoxFlag;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getLastObActualDate() {
        return this.lastObActualDate;
    }

    public void setLastObActualDate(Date lastObActualDate) {
        this.lastObActualDate = lastObActualDate;
    }

    public Integer getMaxAlarm1a() {
        return this.maxAlarm1a;
    }

    public void setMaxAlarm1a(Integer maxAlarm1a) {
        this.maxAlarm1a = maxAlarm1a;
    }

    public Integer getMaxAlarm1b() {
        return this.maxAlarm1b;
    }

    public void setMaxAlarm1b(Integer maxAlarm1b) {
        this.maxAlarm1b = maxAlarm1b;
    }

    public Integer getMaxAlarmF1() {
        return this.maxAlarmF1;
    }

    public void setMaxAlarmF1(Integer maxAlarmF1) {
        this.maxAlarmF1 = maxAlarmF1;
    }

    public Integer getMaxAlarmF2() {
        return this.maxAlarmF2;
    }

    public void setMaxAlarmF2(Integer maxAlarmF2) {
        this.maxAlarmF2 = maxAlarmF2;
    }

    public Integer getMaxAlarmF3() {
        return this.maxAlarmF3;
    }

    public void setMaxAlarmF3(Integer maxAlarmF3) {
        this.maxAlarmF3 = maxAlarmF3;
    }

    public Integer getMaxAlarmF4() {
        return this.maxAlarmF4;
    }

    public void setMaxAlarmF4(Integer maxAlarmF4) {
        this.maxAlarmF4 = maxAlarmF4;
    }

    public Integer getMaxAlarmF5() {
        return this.maxAlarmF5;
    }

    public void setMaxAlarmF5(Integer maxAlarmF5) {
        this.maxAlarmF5 = maxAlarmF5;
    }

    public Integer getMaxAlarmF6() {
        return this.maxAlarmF6;
    }

    public void setMaxAlarmF6(Integer maxAlarmF6) {
        this.maxAlarmF6 = maxAlarmF6;
    }

    public Integer getMaxAlarmTm() {
        return this.maxAlarmTm;
    }

    public void setMaxAlarmTm(Integer maxAlarmTm) {
        this.maxAlarmTm = maxAlarmTm;
    }

    public Integer getMaxBox() {
        return this.maxBox;
    }

    public void setMaxBox(Integer maxBox) {
        this.maxBox = maxBox;
    }

    public BigDecimal getMaxBoxesF1() {
        return this.maxBoxesF1;
    }

    public void setMaxBoxesF1(BigDecimal maxBoxesF1) {
        this.maxBoxesF1 = maxBoxesF1;
    }

    public BigDecimal getMaxBoxesF2() {
        return this.maxBoxesF2;
    }

    public void setMaxBoxesF2(BigDecimal maxBoxesF2) {
        this.maxBoxesF2 = maxBoxesF2;
    }

    public BigDecimal getMaxBoxesF3() {
        return this.maxBoxesF3;
    }

    public void setMaxBoxesF3(BigDecimal maxBoxesF3) {
        this.maxBoxesF3 = maxBoxesF3;
    }

    public BigDecimal getMaxBoxesF4() {
        return this.maxBoxesF4;
    }

    public void setMaxBoxesF4(BigDecimal maxBoxesF4) {
        this.maxBoxesF4 = maxBoxesF4;
    }

    public BigDecimal getMaxBoxesF5() {
        return this.maxBoxesF5;
    }

    public void setMaxBoxesF5(BigDecimal maxBoxesF5) {
        this.maxBoxesF5 = maxBoxesF5;
    }

    public BigDecimal getMaxBoxesF6() {
        return this.maxBoxesF6;
    }

    public void setMaxBoxesF6(BigDecimal maxBoxesF6) {
        this.maxBoxesF6 = maxBoxesF6;
    }

    public BigDecimal getMaxBoxesTm() {
        return this.maxBoxesTm;
    }

    public void setMaxBoxesTm(BigDecimal maxBoxesTm) {
        this.maxBoxesTm = maxBoxesTm;
    }

    public Integer getMaxStock() {
        return this.maxStock;
    }

    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }

    public BigDecimal getMaxStockDayF1() {
        return this.maxStockDayF1;
    }

    public void setMaxStockDayF1(BigDecimal maxStockDayF1) {
        this.maxStockDayF1 = maxStockDayF1;
    }

    public BigDecimal getMaxStockDayF2() {
        return this.maxStockDayF2;
    }

    public void setMaxStockDayF2(BigDecimal maxStockDayF2) {
        this.maxStockDayF2 = maxStockDayF2;
    }

    public BigDecimal getMaxStockDayF3() {
        return this.maxStockDayF3;
    }

    public void setMaxStockDayF3(BigDecimal maxStockDayF3) {
        this.maxStockDayF3 = maxStockDayF3;
    }

    public BigDecimal getMaxStockDayF4() {
        return this.maxStockDayF4;
    }

    public void setMaxStockDayF4(BigDecimal maxStockDayF4) {
        this.maxStockDayF4 = maxStockDayF4;
    }

    public BigDecimal getMaxStockDayF5() {
        return this.maxStockDayF5;
    }

    public void setMaxStockDayF5(BigDecimal maxStockDayF5) {
        this.maxStockDayF5 = maxStockDayF5;
    }

    public BigDecimal getMaxStockDayF6() {
        return this.maxStockDayF6;
    }

    public void setMaxStockDayF6(BigDecimal maxStockDayF6) {
        this.maxStockDayF6 = maxStockDayF6;
    }

    public BigDecimal getMaxStockDayTm() {
        return this.maxStockDayTm;
    }

    public void setMaxStockDayTm(BigDecimal maxStockDayTm) {
        this.maxStockDayTm = maxStockDayTm;
    }

    public Integer getMinAlarm1a() {
        return this.minAlarm1a;
    }

    public void setMinAlarm1a(Integer minAlarm1a) {
        this.minAlarm1a = minAlarm1a;
    }

    public Integer getMinAlarm1b() {
        return this.minAlarm1b;
    }

    public void setMinAlarm1b(Integer minAlarm1b) {
        this.minAlarm1b = minAlarm1b;
    }

    public Integer getMinAlarmF1() {
        return this.minAlarmF1;
    }

    public void setMinAlarmF1(Integer minAlarmF1) {
        this.minAlarmF1 = minAlarmF1;
    }

    public Integer getMinAlarmF2() {
        return this.minAlarmF2;
    }

    public void setMinAlarmF2(Integer minAlarmF2) {
        this.minAlarmF2 = minAlarmF2;
    }

    public Integer getMinAlarmF3() {
        return this.minAlarmF3;
    }

    public void setMinAlarmF3(Integer minAlarmF3) {
        this.minAlarmF3 = minAlarmF3;
    }

    public Integer getMinAlarmF4() {
        return this.minAlarmF4;
    }

    public void setMinAlarmF4(Integer minAlarmF4) {
        this.minAlarmF4 = minAlarmF4;
    }

    public Integer getMinAlarmF5() {
        return this.minAlarmF5;
    }

    public void setMinAlarmF5(Integer minAlarmF5) {
        this.minAlarmF5 = minAlarmF5;
    }

    public Integer getMinAlarmF6() {
        return this.minAlarmF6;
    }

    public void setMinAlarmF6(Integer minAlarmF6) {
        this.minAlarmF6 = minAlarmF6;
    }

    public Integer getMinAlarmTm() {
        return this.minAlarmTm;
    }

    public void setMinAlarmTm(Integer minAlarmTm) {
        this.minAlarmTm = minAlarmTm;
    }

    public Integer getMinBox() {
        return this.minBox;
    }

    public void setMinBox(Integer minBox) {
        this.minBox = minBox;
    }

    public BigDecimal getMinBoxesF1() {
        return this.minBoxesF1;
    }

    public void setMinBoxesF1(BigDecimal minBoxesF1) {
        this.minBoxesF1 = minBoxesF1;
    }

    public BigDecimal getMinBoxesF2() {
        return this.minBoxesF2;
    }

    public void setMinBoxesF2(BigDecimal minBoxesF2) {
        this.minBoxesF2 = minBoxesF2;
    }

    public BigDecimal getMinBoxesF3() {
        return this.minBoxesF3;
    }

    public void setMinBoxesF3(BigDecimal minBoxesF3) {
        this.minBoxesF3 = minBoxesF3;
    }

    public BigDecimal getMinBoxesF4() {
        return this.minBoxesF4;
    }

    public void setMinBoxesF4(BigDecimal minBoxesF4) {
        this.minBoxesF4 = minBoxesF4;
    }

    public BigDecimal getMinBoxesF5() {
        return this.minBoxesF5;
    }

    public void setMinBoxesF5(BigDecimal minBoxesF5) {
        this.minBoxesF5 = minBoxesF5;
    }

    public BigDecimal getMinBoxesF6() {
        return this.minBoxesF6;
    }

    public void setMinBoxesF6(BigDecimal minBoxesF6) {
        this.minBoxesF6 = minBoxesF6;
    }

    public BigDecimal getMinBoxesTm() {
        return this.minBoxesTm;
    }

    public void setMinBoxesTm(BigDecimal minBoxesTm) {
        this.minBoxesTm = minBoxesTm;
    }

    public Integer getMinStock() {
        return this.minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    public BigDecimal getMinStockDayF1() {
        return this.minStockDayF1;
    }

    public void setMinStockDayF1(BigDecimal minStockDayF1) {
        this.minStockDayF1 = minStockDayF1;
    }

    public BigDecimal getMinStockDayF2() {
        return this.minStockDayF2;
    }

    public void setMinStockDayF2(BigDecimal minStockDayF2) {
        this.minStockDayF2 = minStockDayF2;
    }

    public BigDecimal getMinStockDayF3() {
        return this.minStockDayF3;
    }

    public void setMinStockDayF3(BigDecimal minStockDayF3) {
        this.minStockDayF3 = minStockDayF3;
    }

    public BigDecimal getMinStockDayF4() {
        return this.minStockDayF4;
    }

    public void setMinStockDayF4(BigDecimal minStockDayF4) {
        this.minStockDayF4 = minStockDayF4;
    }

    public BigDecimal getMinStockDayF5() {
        return this.minStockDayF5;
    }

    public void setMinStockDayF5(BigDecimal minStockDayF5) {
        this.minStockDayF5 = minStockDayF5;
    }

    public BigDecimal getMinStockDayF6() {
        return this.minStockDayF6;
    }

    public void setMinStockDayF6(BigDecimal minStockDayF6) {
        this.minStockDayF6 = minStockDayF6;
    }

    public BigDecimal getMinStockDayTm() {
        return this.minStockDayTm;
    }

    public void setMinStockDayTm(BigDecimal minStockDayTm) {
        this.minStockDayTm = minStockDayTm;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getNextInboundDate() {
        return this.nextInboundDate;
    }

    public void setNextInboundDate(Date nextInboundDate) {
        this.nextInboundDate = nextInboundDate;
    }

    public BigDecimal getNextInboundQty() {
        return this.nextInboundQty;
    }

    public void setNextInboundQty(BigDecimal nextInboundQty) {
        this.nextInboundQty = nextInboundQty;
    }

    public String getNextInvoice() {
        return this.nextInvoice;
    }

    public void setNextInvoice(String nextInvoice) {
        this.nextInvoice = nextInvoice;
    }

    public BigDecimal getNgOnholdQty() {
        return this.ngOnholdQty;
    }

    public void setNgOnholdQty(BigDecimal ngOnholdQty) {
        this.ngOnholdQty = ngOnholdQty;
    }

    public Integer getNoCfcFlagF1() {
        return this.noCfcFlagF1;
    }

    public void setNoCfcFlagF1(Integer noCfcFlagF1) {
        this.noCfcFlagF1 = noCfcFlagF1;
    }

    public Integer getNoCfcFlagF2() {
        return this.noCfcFlagF2;
    }

    public void setNoCfcFlagF2(Integer noCfcFlagF2) {
        this.noCfcFlagF2 = noCfcFlagF2;
    }

    public Integer getNoCfcFlagF3() {
        return this.noCfcFlagF3;
    }

    public void setNoCfcFlagF3(Integer noCfcFlagF3) {
        this.noCfcFlagF3 = noCfcFlagF3;
    }

    public Integer getNoCfcFlagF4() {
        return this.noCfcFlagF4;
    }

    public void setNoCfcFlagF4(Integer noCfcFlagF4) {
        this.noCfcFlagF4 = noCfcFlagF4;
    }

    public Integer getNoCfcFlagF5() {
        return this.noCfcFlagF5;
    }

    public void setNoCfcFlagF5(Integer noCfcFlagF5) {
        this.noCfcFlagF5 = noCfcFlagF5;
    }

    public Integer getNoCfcFlagF6() {
        return this.noCfcFlagF6;
    }

    public void setNoCfcFlagF6(Integer noCfcFlagF6) {
        this.noCfcFlagF6 = noCfcFlagF6;
    }

    public Integer getNoMaster1a() {
        return this.noMaster1a;
    }

    public void setNoMaster1a(Integer noMaster1a) {
        this.noMaster1a = noMaster1a;
    }

    public Integer getNoMaster1b() {
        return this.noMaster1b;
    }

    public void setNoMaster1b(Integer noMaster1b) {
        this.noMaster1b = noMaster1b;
    }

    public Integer getNoMasterMaxTm() {
        return this.noMasterMaxTm;
    }

    public void setNoMasterMaxTm(Integer noMasterMaxTm) {
        this.noMasterMaxTm = noMasterMaxTm;
    }

    public Integer getNoMasterMinTm() {
        return this.noMasterMinTm;
    }

    public void setNoMasterMinTm(Integer noMasterMinTm) {
        this.noMasterMinTm = noMasterMinTm;
    }

    public Integer getNoMasterPlan1a() {
        return this.noMasterPlan1a;
    }

    public void setNoMasterPlan1a(Integer noMasterPlan1a) {
        this.noMasterPlan1a = noMasterPlan1a;
    }

    public Integer getNoMasterPlan1b() {
        return this.noMasterPlan1b;
    }

    public void setNoMasterPlan1b(Integer noMasterPlan1b) {
        this.noMasterPlan1b = noMasterPlan1b;
    }

    public Integer getNoPfcFlagF1() {
        return this.noPfcFlagF1;
    }

    public void setNoPfcFlagF1(Integer noPfcFlagF1) {
        this.noPfcFlagF1 = noPfcFlagF1;
    }

    public Integer getNoPfcFlagF2() {
        return this.noPfcFlagF2;
    }

    public void setNoPfcFlagF2(Integer noPfcFlagF2) {
        this.noPfcFlagF2 = noPfcFlagF2;
    }

    public Integer getNoPfcFlagF3() {
        return this.noPfcFlagF3;
    }

    public void setNoPfcFlagF3(Integer noPfcFlagF3) {
        this.noPfcFlagF3 = noPfcFlagF3;
    }

    public Integer getNoPfcFlagF4() {
        return this.noPfcFlagF4;
    }

    public void setNoPfcFlagF4(Integer noPfcFlagF4) {
        this.noPfcFlagF4 = noPfcFlagF4;
    }

    public Integer getNoPfcFlagF5() {
        return this.noPfcFlagF5;
    }

    public void setNoPfcFlagF5(Integer noPfcFlagF5) {
        this.noPfcFlagF5 = noPfcFlagF5;
    }

    public Integer getNoPfcFlagF6() {
        return this.noPfcFlagF6;
    }

    public void setNoPfcFlagF6(Integer noPfcFlagF6) {
        this.noPfcFlagF6 = noPfcFlagF6;
    }

    public Integer getNoPfcMaxF1() {
        return this.noPfcMaxF1;
    }

    public void setNoPfcMaxF1(Integer noPfcMaxF1) {
        this.noPfcMaxF1 = noPfcMaxF1;
    }

    public Integer getNoPfcMaxF2() {
        return this.noPfcMaxF2;
    }

    public void setNoPfcMaxF2(Integer noPfcMaxF2) {
        this.noPfcMaxF2 = noPfcMaxF2;
    }

    public Integer getNoPfcMaxF3() {
        return this.noPfcMaxF3;
    }

    public void setNoPfcMaxF3(Integer noPfcMaxF3) {
        this.noPfcMaxF3 = noPfcMaxF3;
    }

    public Integer getNoPfcMaxF4() {
        return this.noPfcMaxF4;
    }

    public void setNoPfcMaxF4(Integer noPfcMaxF4) {
        this.noPfcMaxF4 = noPfcMaxF4;
    }

    public Integer getNoPfcMaxF5() {
        return this.noPfcMaxF5;
    }

    public void setNoPfcMaxF5(Integer noPfcMaxF5) {
        this.noPfcMaxF5 = noPfcMaxF5;
    }

    public Integer getNoPfcMaxF6() {
        return this.noPfcMaxF6;
    }

    public void setNoPfcMaxF6(Integer noPfcMaxF6) {
        this.noPfcMaxF6 = noPfcMaxF6;
    }

    public Integer getNoPfcMinF1() {
        return this.noPfcMinF1;
    }

    public void setNoPfcMinF1(Integer noPfcMinF1) {
        this.noPfcMinF1 = noPfcMinF1;
    }

    public Integer getNoPfcMinF2() {
        return this.noPfcMinF2;
    }

    public void setNoPfcMinF2(Integer noPfcMinF2) {
        this.noPfcMinF2 = noPfcMinF2;
    }

    public Integer getNoPfcMinF3() {
        return this.noPfcMinF3;
    }

    public void setNoPfcMinF3(Integer noPfcMinF3) {
        this.noPfcMinF3 = noPfcMinF3;
    }

    public Integer getNoPfcMinF4() {
        return this.noPfcMinF4;
    }

    public void setNoPfcMinF4(Integer noPfcMinF4) {
        this.noPfcMinF4 = noPfcMinF4;
    }

    public Integer getNoPfcMinF5() {
        return this.noPfcMinF5;
    }

    public void setNoPfcMinF5(Integer noPfcMinF5) {
        this.noPfcMinF5 = noPfcMinF5;
    }

    public Integer getNoPfcMinF6() {
        return this.noPfcMinF6;
    }

    public void setNoPfcMinF6(Integer noPfcMinF6) {
        this.noPfcMinF6 = noPfcMinF6;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public Integer getOrderSafetyStock() {
        return this.orderSafetyStock;
    }

    public void setOrderSafetyStock(Integer orderSafetyStock) {
        this.orderSafetyStock = orderSafetyStock;
    }

    public Integer getOsCustStockFlag() {
        return this.osCustStockFlag;
    }

    public void setOsCustStockFlag(Integer osCustStockFlag) {
        this.osCustStockFlag = osCustStockFlag;
    }

    public BigDecimal getOutboundFluctuation() {
        return this.outboundFluctuation;
    }

    public void setOutboundFluctuation(BigDecimal outboundFluctuation) {
        this.outboundFluctuation = outboundFluctuation;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getOverRatioDate4() {
        return this.overRatioDate4;
    }

    public void setOverRatioDate4(Date overRatioDate4) {
        this.overRatioDate4 = overRatioDate4;
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public BigDecimal getPreparedObQty() {
        return this.preparedObQty;
    }

    public void setPreparedObQty(BigDecimal preparedObQty) {
        this.preparedObQty = preparedObQty;
    }

    public Integer getSaCustStockFlag() {
        return this.saCustStockFlag;
    }

    public void setSaCustStockFlag(Integer saCustStockFlag) {
        this.saCustStockFlag = saCustStockFlag;
    }

    public BigDecimal getSafetyAddOn3() {
        return this.safetyAddOn3;
    }

    public void setSafetyAddOn3(BigDecimal safetyAddOn3) {
        this.safetyAddOn3 = safetyAddOn3;
    }

    public Integer getSafetyAlarm3() {
        return this.safetyAlarm3;
    }

    public void setSafetyAlarm3(Integer safetyAlarm3) {
        this.safetyAlarm3 = safetyAlarm3;
    }

    public BigDecimal getSpq() {
        return this.spq;
    }

    public void setSpq(BigDecimal spq) {
        this.spq = spq;
    }

    public BigDecimal getStockBoxes1a() {
        return this.stockBoxes1a;
    }

    public void setStockBoxes1a(BigDecimal stockBoxes1a) {
        this.stockBoxes1a = stockBoxes1a;
    }

    public BigDecimal getStockBoxes1b() {
        return this.stockBoxes1b;
    }

    public void setStockBoxes1b(BigDecimal stockBoxes1b) {
        this.stockBoxes1b = stockBoxes1b;
    }

    public Date getStockDate() {
        return this.stockDate;
    }

    public void setStockDate(Date stockDate) {
        this.stockDate = stockDate;
    }

    public BigDecimal getStockDay3() {
        return this.stockDay3;
    }

    public void setStockDay3(BigDecimal stockDay3) {
        this.stockDay3 = stockDay3;
    }

    public BigDecimal getStockDays1a() {
        return this.stockDays1a;
    }

    public void setStockDays1a(BigDecimal stockDays1a) {
        this.stockDays1a = stockDays1a;
    }

    public BigDecimal getStockDays1b() {
        return this.stockDays1b;
    }

    public void setStockDays1b(BigDecimal stockDays1b) {
        this.stockDays1b = stockDays1b;
    }

    public BigDecimal getStockDaysPlan1a() {
        return this.stockDaysPlan1a;
    }

    public void setStockDaysPlan1a(BigDecimal stockDaysPlan1a) {
        this.stockDaysPlan1a = stockDaysPlan1a;
    }

    public BigDecimal getStockDaysPlan1b() {
        return this.stockDaysPlan1b;
    }

    public void setStockDaysPlan1b(BigDecimal stockDaysPlan1b) {
        this.stockDaysPlan1b = stockDaysPlan1b;
    }

    public BigDecimal getStockQty1a() {
        return this.stockQty1a;
    }

    public void setStockQty1a(BigDecimal stockQty1a) {
        this.stockQty1a = stockQty1a;
    }

    public BigDecimal getStockQty1b() {
        return this.stockQty1b;
    }

    public void setStockQty1b(BigDecimal stockQty1b) {
        this.stockQty1b = stockQty1b;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getShortageDateF1() {
        return this.shortageDateF1;
    }

    public void setShortageDateF1(Date shortageDateF1) {
        this.shortageDateF1 = shortageDateF1;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getShortageDateF2() {
        return this.shortageDateF2;
    }

    public void setShortageDateF2(Date shortageDateF2) {
        this.shortageDateF2 = shortageDateF2;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getShortageDateTm() {
        return this.shortageDateTm;
    }

    public void setShortageDateTm(Date shortageDateTm) {
        this.shortageDateTm = shortageDateTm;
    }

    public BigDecimal getShortageAddOn3() {
        return this.shortageAddOn3;
    }

    public void setShortageAddOn3(BigDecimal shortageAddOn3) {
        this.shortageAddOn3 = shortageAddOn3;
    }
    
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getShortageDate3() {
        return this.shortageDate3;
    }

    public void setShortageDate3(Date shortageDate3) {
        this.shortageDate3 = shortageDate3;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getShortageDateF3() {
        return this.shortageDateF3;
    }

    public void setShortageDateF3(Date shortageDateF3) {
        this.shortageDateF3 = shortageDateF3;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getShortageDateF4() {
        return this.shortageDateF4;
    }

    public void setShortageDateF4(Date shortageDateF4) {
        this.shortageDateF4 = shortageDateF4;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getShortageDateF5() {
        return this.shortageDateF5;
    }

    public void setShortageDateF5(Date shortageDateF5) {
        this.shortageDateF5 = shortageDateF5;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getShortageDateF6() {
        return this.shortageDateF6;
    }

    public void setShortageDateF6(Date shortageDateF6) {
        this.shortageDateF6 = shortageDateF6;
    }

    public Integer getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getTotalCfcQty4() {
        return this.totalCfcQty4;
    }

    public void setTotalCfcQty4(BigDecimal totalCfcQty4) {
        this.totalCfcQty4 = totalCfcQty4;
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
     * Get the expPartsSet.
     *
     * @return expPartsSet
     *
     * @author liu_yinchuan
     */
    public String getExpPartsSet() {
        return this.expPartsSet;
    }

    /**
     * Set the expPartsSet.
     *
     * @param expPartsSet expPartsSet
     *
     * @author liu_yinchuan
     */
    public void setExpPartsSet(String expPartsSet) {
        this.expPartsSet = expPartsSet;
        
    }

    /**
     * Get the noMaster3.
     *
     * @return noMaster3
     *
     * @author liu_yinchuan
     */
    public Integer getNoMaster3() {
        return this.noMaster3;
    }

    /**
     * Set the noMaster3.
     *
     * @param noMaster3 noMaster3
     *
     * @author liu_yinchuan
     */
    public void setNoMaster3(Integer noMaster3) {
        this.noMaster3 = noMaster3;
        
    }

}