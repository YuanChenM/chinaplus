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
 * The persistent class for the TNM_PARTS_MASTER database table.
 * 
 */
@Entity(name = "TNM_PARTS_MASTER")
public class TnmPartsMaster extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNM_PARTS_MASTER_PARTSID_GENERATOR",
        sequenceName = "SEQ_TNM_PARTS_MASTER",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNM_PARTS_MASTER_PARTSID_GENERATOR")
    @Column(name = "PARTS_ID")
    private Integer partsId;

    @Column(name = "ALLOCATION_FC_TYPE")
    private String allocationFcType;

    @Column(name = "BUSINESS_PATTERN")
    private Integer businessPattern;

    @Column(name = "BUSINESS_TYPE")
    private Integer businessType;

    @Column(name = "CAR_MODEL")
    private String carModel;

    @Column(name = "CFC_ADJUSTMENT_TYPE1")
    private Integer cfcAdjustmentType1;

    @Column(name = "CFC_ADJUSTMENT_TYPE2")
    private Integer cfcAdjustmentType2;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CUST_BACK_NO")
    private String custBackNo;

    @Column(name = "CUST_PARTS_NO")
    private String custPartsNo;

    @Column(name = "CUSTOMER_CODE")
    private String customerCode;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "EXP_CALENDAR_CODE")
    private String expCalendarCode;

    @Column(name = "KANBAN_CUST_CODE")
    private String kanbanCustCode;

    @Column(name = "FORECAST_NUM")
    private Integer forecastNum;

    @Column(name = "IMP_REGION")
    private String impRegion;

    @Column(name = "IMP_WHS_CODE")
    private String impWhsCode;

    @Column(name = "INACTIVE_FLAG")
    private Integer inactiveFlag;

    @Column(name = "INV_CUST_CODE")
    private String invCustCode;

    @Column(name = "INVENTORY_BOX_FLAG")
    private Integer inventoryBoxFlag;

    @Column(name = "MAX_BOX")
    private Integer maxBox;

    @Column(name = "MAX_STOCK")
    private Integer maxStock;

    @Column(name = "MIN_BOX")
    private Integer minBox;

    @Column(name = "MIN_STOCK")
    private Integer minStock;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "OLD_TTC_PARTS_NO")
    private String oldTtcPartsNo;

    @Column(name = "ORDER_DAY")
    private Integer orderDay;

    @Column(name = "ORDER_FC_TYPE")
    private Integer orderFcType;

    @Column(name = "ORDER_LOT")
    private BigDecimal orderLot;

    @Column(name = "ORDER_SAFETY_STOCK")
    private Integer orderSafetyStock;

    @Column(name = "OS_CUST_STOCK_FLAG")
    private Integer osCustStockFlag;

    @Column(name = "OUTBOUND_FLUCTUATION")
    private BigDecimal outboundFluctuation;

    @Column(name = "PARTS_NAME_CN")
    private String partsNameCn;

    @Column(name = "PARTS_NAME_EN")
    private String partsNameEn;

    @Column(name = "PARTS_TYPE")
    private Integer partsType;

    @Column(name = "RUNDOWN_SAFETY_STOCK")
    private Integer rundownSafetyStock;

    @Column(name = "SA_CUST_STOCK_FLAG")
    private Integer saCustStockFlag;

    @Column(name = "SIMULATION_END_DATE_PATTERN")
    private Integer simulationEndDatePattern;

    private BigDecimal spq;

    @Column(name = "SPQ_M3")
    private BigDecimal spqM3;

    @Column(name = "TARGET_MONTH")
    private Integer targetMonth;

    @Column(name = "TTC_PARTS_NO")
    private String ttcPartsNo;

    @Column(name = "UOM_CODE")
    private String uomCode;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    // bi-directional many-to-one association to TnmExpPart
    //@OneToMany(mappedBy = "tnmPartsMaster")
    //private List<TnmExpPart> tnmExpParts;

    public TnmPartsMaster() {}

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public String getAllocationFcType() {
        return this.allocationFcType;
    }

    public void setAllocationFcType(String allocationFcType) {
        this.allocationFcType = allocationFcType;
    }

    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
    }

    public Integer getBusinessType() {
        return this.businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getCarModel() {
        return this.carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public Integer getCfcAdjustmentType1() {
        return this.cfcAdjustmentType1;
    }

    public void setCfcAdjustmentType1(Integer cfcAdjustmentType1) {
        this.cfcAdjustmentType1 = cfcAdjustmentType1;
    }

    public Integer getCfcAdjustmentType2() {
        return this.cfcAdjustmentType2;
    }

    public void setCfcAdjustmentType2(Integer cfcAdjustmentType2) {
        this.cfcAdjustmentType2 = cfcAdjustmentType2;
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

    public String getCustBackNo() {
        return this.custBackNo;
    }

    public void setCustBackNo(String custBackNo) {
        this.custBackNo = custBackNo;
    }

    public String getCustPartsNo() {
        return this.custPartsNo;
    }

    public void setCustPartsNo(String custPartsNo) {
        this.custPartsNo = custPartsNo;
    }

    public String getCustomerCode() {
        return this.customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Integer getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getExpCalendarCode() {
        return this.expCalendarCode;
    }

    public void setExpCalendarCode(String expCalendarCode) {
        this.expCalendarCode = expCalendarCode;
    }

    public String getKanbanCustCode() {
        return this.kanbanCustCode;
    }

    public void setKanbanCustCode(String kanbanCustCode) {
        this.kanbanCustCode = kanbanCustCode;
    }

    public Integer getForecastNum() {
        return this.forecastNum;
    }

    public void setForecastNum(Integer forecastNum) {
        this.forecastNum = forecastNum;
    }

    public String getImpRegion() {
        return this.impRegion;
    }

    public void setImpRegion(String impRegion) {
        this.impRegion = impRegion;
    }

    public String getImpWhsCode() {
        return this.impWhsCode;
    }

    public void setImpWhsCode(String impWhsCode) {
        this.impWhsCode = impWhsCode;
    }

    public Integer getInactiveFlag() {
        return this.inactiveFlag;
    }

    public void setInactiveFlag(Integer inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
    }

    public String getInvCustCode() {
        return this.invCustCode;
    }

    public void setInvCustCode(String invCustCode) {
        this.invCustCode = invCustCode;
    }

    public Integer getInventoryBoxFlag() {
        return this.inventoryBoxFlag;
    }

    public void setInventoryBoxFlag(Integer inventoryBoxFlag) {
        this.inventoryBoxFlag = inventoryBoxFlag;
    }

    public Integer getMaxBox() {
        return this.maxBox;
    }

    public void setMaxBox(Integer maxBox) {
        this.maxBox = maxBox;
    }

    public Integer getMaxStock() {
        return this.maxStock;
    }

    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }

    public Integer getMinBox() {
        return this.minBox;
    }

    public void setMinBox(Integer minBox) {
        this.minBox = minBox;
    }

    public Integer getMinStock() {
        return this.minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public String getOldTtcPartsNo() {
        return this.oldTtcPartsNo;
    }

    public void setOldTtcPartsNo(String oldTtcPartsNo) {
        this.oldTtcPartsNo = oldTtcPartsNo;
    }

    public Integer getOrderDay() {
        return this.orderDay;
    }

    public void setOrderDay(Integer orderDay) {
        this.orderDay = orderDay;
    }

    public Integer getOrderFcType() {
        return this.orderFcType;
    }

    public void setOrderFcType(Integer orderFcType) {
        this.orderFcType = orderFcType;
    }

    public BigDecimal getOrderLot() {
        return this.orderLot;
    }

    public void setOrderLot(BigDecimal orderLot) {
        this.orderLot = orderLot;
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

    public String getPartsNameCn() {
        return this.partsNameCn;
    }

    public void setPartsNameCn(String partsNameCn) {
        this.partsNameCn = partsNameCn;
    }

    public String getPartsNameEn() {
        return this.partsNameEn;
    }

    public void setPartsNameEn(String partsNameEn) {
        this.partsNameEn = partsNameEn;
    }

    public Integer getPartsType() {
        return this.partsType;
    }

    public void setPartsType(Integer partsType) {
        this.partsType = partsType;
    }

    public Integer getRundownSafetyStock() {
        return this.rundownSafetyStock;
    }

    public void setRundownSafetyStock(Integer rundownSafetyStock) {
        this.rundownSafetyStock = rundownSafetyStock;
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

    public BigDecimal getSpq() {
        return this.spq;
    }

    public void setSpq(BigDecimal spq) {
        this.spq = spq;
    }

    public BigDecimal getSpqM3() {
        return this.spqM3;
    }

    public void setSpqM3(BigDecimal spqM3) {
        this.spqM3 = spqM3;
    }

    public Integer getTargetMonth() {
        return this.targetMonth;
    }

    public void setTargetMonth(Integer targetMonth) {
        this.targetMonth = targetMonth;
    }

    public String getTtcPartsNo() {
        return this.ttcPartsNo;
    }

    public void setTtcPartsNo(String ttcPartsNo) {
        this.ttcPartsNo = ttcPartsNo;
    }

    public String getUomCode() {
        return this.uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
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

    /*public List<TnmExpPart> getTnmExpParts() {
        return this.tnmExpParts;
    }

    public void setTnmExpParts(List<TnmExpPart> tnmExpParts) {
        this.tnmExpParts = tnmExpParts;
    }

    public TnmExpPart addTnmExpPart(TnmExpPart tnmExpPart) {
        getTnmExpParts().add(tnmExpPart);
        //tnmExpPart.setTnmPartsMaster(this);

        return tnmExpPart;
    }

    public TnmExpPart removeTnmExpPart(TnmExpPart tnmExpPart) {
        getTnmExpParts().remove(tnmExpPart);
        //tnmExpPart.setTnmPartsMaster(null);

        return tnmExpPart;
    }
    */

}