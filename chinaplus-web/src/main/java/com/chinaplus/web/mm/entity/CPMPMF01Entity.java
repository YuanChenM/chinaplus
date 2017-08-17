/**
 * CPMPMF01Entity.java
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

import java.math.BigDecimal;


/** 
 * CPMPMF01Entity.
 */
public class CPMPMF01Entity  extends MMCommonEntity{

    /** serialVersionUID */
    private static final long serialVersionUID = 6901683044031317722L;
   
    /** the ttcPartsNo */
    private String ttcPartsNo;
    /** the ttcPartsName */
    private String ttcPartsName;  
    /** the partsNameCn */
    private String partsNameCn;   
    /** the oldTtcPartsNo */
    private String oldTtcPartsNo;    
    /** the expUomCode */
    private String expUomCode;
    
    /** the expRegion */
    private String expRegion;  
    /** the ttcSuppCode */
    private String ttcSuppCode; 
    /** the ssmsMainRoute */
    private String ssmsMainRoute;   
    /** the ssmsVendorRoute */
    private String ssmsVendorRoute;  
    /** the expSuppCode */
    private String expSuppCode;  
    
    /** the supplierName */
    private String supplierName;
    /** the suppPartsNo */
    private String suppPartsNo;  
    /** the impRegion */
    private String impRegion;   
    /** the officeCode */
    private String officeCode; 
    /** the customerCode */
    private String customerCode; 
    
    /** the expCustCode */
    private String expCustCode;   
    /** the customerName */
    private String customerName; 
    /** the custPartsNo */
    private String custPartsNo;
    /** the custBackNo */
    private String custBackNo;     
    /** the westCustCode */
    private String westCustCode;
    
    /** the westPartsNo */
    private String westPartsNo;  
    /** the invCustCode */
    private String invCustCode;  
    /** the impWhsCode */
    private String impWhsCode;
    /** the orderLot */
    private BigDecimal orderLot; 
    /** the srbq */
    private BigDecimal srbq;
    
    /** the spq */
    private BigDecimal spq;  
    /** the spqM3 */
    private BigDecimal spqM3;    
    /** the businessPattern */
    private String businessPattern;  
    /** the businessType */
    private String businessType;      
    /** the partsType */
    private String partsType; 
    
    /** the carModel */
    private String carModel; 
    /** the orderDay */
    private Integer orderDay;  
    /** the targetMonth */
    private Integer targetMonth;  
    /** the forecastNum */
    private Integer forecastNum;   
    /** the orderFcType */
    private String orderFcType;  
    
    /** the expCalendarCode */
    private String expCalendarCode;    
    /** the osCustStockFlag */
    private String osCustStockFlag;    
    /** the saCustStockFlag */
    private String saCustStockFlag; 
    /** the inventoryBoxFlag */
    private String inventoryBoxFlag;  
    /** the minStock */
    private Integer minStock;  
    
    /** the minStock */
    private Integer maxStock;
    /** the minBox */
    private Integer minBox;   
    /** the maxBox */
    private Integer maxBox;   
    /** the orderSafetyStock */
    private Integer orderSafetyStock;   
    /** the rundownSafetyStock */
    private Integer rundownSafetyStock; 
    
    /** the outboundFluctuation */
    private Double outboundFluctuation;   
    /** the simulationEndDatePattern */
    private String simulationEndDatePattern;   
    /** the shippingRouteCode */
    private String shippingRouteCode;     
    /** the delayAdjustmentPattern */
    private String delayAdjustmentPattern; 
    /** the airEtdLeadtime */
    private Integer airEtdLeadtime; 
    
    /** the airEtaLeadtime */
    private Integer airEtaLeadtime;  
    /** the airInboundLeadtime */
    private Integer airInboundLeadtime;  
    /** the seaEtaLeadtime */
    private Integer seaEtaLeadtime;   
    /** the seaInboundLeadtime */
    private Integer seaInboundLeadtime;  
    /** the allocationFcType */
    private String allocationFcType;  
    
    /** the cfcAdjustmentType1 */
    private String cfcAdjustmentType1;
    /** the cfcAdjustmentType2 */
    private String cfcAdjustmentType2;  
    /** the remark1 */
    private String remark1;   
    /** the remark2 */
    private String remark2; 
    /** the remark3 */
    private String remark3;   
    
    /** the buildoutFlag */
    private String buildoutFlag; 
    /** the buildoutMonth */
    private String buildoutMonth;  
    /** the lastPoMonth */
    private String lastPoMonth;  
    /** the lastDeliveryMonth */
    private String lastDeliveryMonth;  
    /** the partsStatus */
    private String partsStatus; 
    /** the inactiveFlag */
    private String inactiveFlag;                       
    
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
     * Get the ttcPartsName.
     *
     * @return ttcPartsName
     */
    public String getTtcPartsName() {
        return this.ttcPartsName;
    }
    /**
     * Set the ttcPartsName.
     *
     * @param ttcPartsName ttcPartsName
     */
    public void setTtcPartsName(String ttcPartsName) {
        this.ttcPartsName = ttcPartsName;
    }
    /**
     * Get the partsNameCn.
     *
     * @return partsNameCn
     */
    public String getPartsNameCn() {
        return this.partsNameCn;
    }
    /**
     * Set the partsNameCn.
     *
     * @param partsNameCn partsNameCn
     */
    public void setPartsNameCn(String partsNameCn) {
        this.partsNameCn = partsNameCn;
    }
    /**
     * Get the oldTtcPartsNo.
     *
     * @return oldTtcPartsNo
     */
    public String getOldTtcPartsNo() {
        return this.oldTtcPartsNo;
    }
    /**
     * Set the oldTtcPartsNo.
     *
     * @param oldTtcPartsNo oldTtcPartsNo
     */
    public void setOldTtcPartsNo(String oldTtcPartsNo) {
        this.oldTtcPartsNo = oldTtcPartsNo;
    }
    /**
     * Get the expUomCode.
     *
     * @return expUomCode
     */
    public String getExpUomCode() {
        return this.expUomCode;
    }
    /**
     * Set the expUomCode.
     *
     * @param expUomCode expUomCode
     */
    public void setExpUomCode(String expUomCode) {
        this.expUomCode = expUomCode;
    }
    /**
     * Get the expRegion.
     *
     * @return expRegion
     */
    public String getExpRegion() {
        return this.expRegion;
    }
    /**
     * Set the expRegion.
     *
     * @param expRegion expRegion
     */
    public void setExpRegion(String expRegion) {
        this.expRegion = expRegion;
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
     * Get the ssmsMainRoute.
     *
     * @return ssmsMainRoute
     */
    public String getSsmsMainRoute() {
        return this.ssmsMainRoute;
    }
    /**
     * Set the ssmsMainRoute.
     *
     * @param ssmsMainRoute ssmsMainRoute
     */
    public void setSsmsMainRoute(String ssmsMainRoute) {
        this.ssmsMainRoute = ssmsMainRoute;
    }
    /**
     * Get the ssmsVendorRoute.
     *
     * @return ssmsVendorRoute
     */
    public String getSsmsVendorRoute() {
        return this.ssmsVendorRoute;
    }
    /**
     * Set the ssmsVendorRoute.
     *
     * @param ssmsVendorRoute ssmsVendorRoute
     */
    public void setSsmsVendorRoute(String ssmsVendorRoute) {
        this.ssmsVendorRoute = ssmsVendorRoute;
    }
    /**
     * Get the expSuppCode.
     *
     * @return expSuppCode
     */
    public String getExpSuppCode() {
        return this.expSuppCode;
    }
    /**
     * Set the expSuppCode.
     *
     * @param expSuppCode expSuppCode
     */
    public void setExpSuppCode(String expSuppCode) {
        this.expSuppCode = expSuppCode;
    }
    /**
     * Get the supplierName.
     *
     * @return supplierName
     */
    public String getSupplierName() {
        return this.supplierName;
    }
    /**
     * Set the supplierName.
     *
     * @param supplierName supplierName
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
     * Get the impRegion.
     *
     * @return impRegion
     */
    public String getImpRegion() {
        return this.impRegion;
    }
    /**
     * Set the impRegion.
     *
     * @param impRegion impRegion
     */
    public void setImpRegion(String impRegion) {
        this.impRegion = impRegion;
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
     * Get the customerName.
     *
     * @return customerName
     */
    public String getCustomerName() {
        return this.customerName;
    }
    /**
     * Set the customerName.
     *
     * @param customerName customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    /**
     * Get the custPartsNo.
     *
     * @return custPartsNo
     */
    public String getCustPartsNo() {
        return this.custPartsNo;
    }
    /**
     * Set the custPartsNo.
     *
     * @param custPartsNo custPartsNo
     */
    public void setCustPartsNo(String custPartsNo) {
        this.custPartsNo = custPartsNo;
    }
    /**
     * Get the custBackNo.
     *
     * @return custBackNo
     */
    public String getCustBackNo() {
        return this.custBackNo;
    }
    /**
     * Set the custBackNo.
     *
     * @param custBackNo custBackNo
     */
    public void setCustBackNo(String custBackNo) {
        this.custBackNo = custBackNo;
    }
    /**
     * Get the westCustCode.
     *
     * @return westCustCode
     */
    public String getWestCustCode() {
        return this.westCustCode;
    }
    /**
     * Set the westCustCode.
     *
     * @param westCustCode westCustCode
     */
    public void setWestCustCode(String westCustCode) {
        this.westCustCode = westCustCode;
    }
    /**
     * Get the westPartsNo.
     *
     * @return westPartsNo
     */
    public String getWestPartsNo() {
        return this.westPartsNo;
    }
    /**
     * Set the westPartsNo.
     *
     * @param westPartsNo westPartsNo
     */
    public void setWestPartsNo(String westPartsNo) {
        this.westPartsNo = westPartsNo;
    }
    /**
     * Get the invCustCode.
     *
     * @return invCustCode
     */
    public String getInvCustCode() {
        return this.invCustCode;
    }
    /**
     * Set the invCustCode.
     *
     * @param invCustCode invCustCode
     */
    public void setInvCustCode(String invCustCode) {
        this.invCustCode = invCustCode;
    }
    /**
     * Get the impWhsCode.
     *
     * @return impWhsCode
     */
    public String getImpWhsCode() {
        return this.impWhsCode;
    }
    /**
     * Set the impWhsCode.
     *
     * @param impWhsCode impWhsCode
     */
    public void setImpWhsCode(String impWhsCode) {
        this.impWhsCode = impWhsCode;
    }
    /**
     * Get the orderLot.
     *
     * @return orderLot
     */
    public BigDecimal getOrderLot() {
        return this.orderLot;
    }
    /**
     * Set the orderLot.
     *
     * @param orderLot orderLot
     */
    public void setOrderLot(BigDecimal orderLot) {
        this.orderLot = orderLot;
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
     * Get the spq.
     *
     * @return spq
     */
    public BigDecimal getSpq() {
        return this.spq;
    }
    /**
     * Set the spq.
     *
     * @param spq spq
     */
    public void setSpq(BigDecimal spq) {
        this.spq = spq;
    }
    /**
     * Get the spqM3.
     *
     * @return spqM3
     */
    public BigDecimal getSpqM3() {
        return this.spqM3;
    }
    /**
     * Set the spqM3.
     *
     * @param spqM3 spqM3
     */
    public void setSpqM3(BigDecimal spqM3) {
        this.spqM3 = spqM3;
    }
    /**
     * Get the businessPattern.
     *
     * @return businessPattern
     */
    public String getBusinessPattern() {
        return this.businessPattern;
    }
    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     */
    public void setBusinessPattern(String businessPattern) {
        this.businessPattern = businessPattern;
    }
    /**
     * Get the businessType.
     *
     * @return businessType
     */
    public String getBusinessType() {
        return this.businessType;
    }
    /**
     * Set the businessType.
     *
     * @param businessType businessType
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    /**
     * Get the partsType.
     *
     * @return partsType
     */
    public String getPartsType() {
        return this.partsType;
    }
    /**
     * Set the partsType.
     *
     * @param partsType partsType
     */
    public void setPartsType(String partsType) {
        this.partsType = partsType;
    }
    /**
     * Get the carModel.
     *
     * @return carModel
     */
    public String getCarModel() {
        return this.carModel;
    }
    /**
     * Set the carModel.
     *
     * @param carModel carModel
     */
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
    /**
     * Get the orderDay.
     *
     * @return orderDay
     */
    public Integer getOrderDay() {
        return this.orderDay;
    }
    /**
     * Set the orderDay.
     *
     * @param orderDay orderDay
     */
    public void setOrderDay(Integer orderDay) {
        this.orderDay = orderDay;
    }
    /**
     * Get the targetMonth.
     *
     * @return targetMonth
     */
    public Integer getTargetMonth() {
        return this.targetMonth;
    }
    /**
     * Set the targetMonth.
     *
     * @param targetMonth targetMonth
     */
    public void setTargetMonth(Integer targetMonth) {
        this.targetMonth = targetMonth;
    }
    /**
     * Get the forecastNum.
     *
     * @return forecastNum
     */
    public Integer getForecastNum() {
        return this.forecastNum;
    }
    /**
     * Set the forecastNum.
     *
     * @param forecastNum forecastNum
     */
    public void setForecastNum(Integer forecastNum) {
        this.forecastNum = forecastNum;
    }
    /**
     * Get the orderFcType.
     *
     * @return orderFcType
     */
    public String getOrderFcType() {
        return this.orderFcType;
    }
    /**
     * Set the orderFcType.
     *
     * @param orderFcType orderFcType
     */
    public void setOrderFcType(String orderFcType) {
        this.orderFcType = orderFcType;
    }
    /**
     * Get the expCalendarCode.
     *
     * @return expCalendarCode
     */
    public String getExpCalendarCode() {
        return this.expCalendarCode;
    }
    /**
     * Set the expCalendarCode.
     *
     * @param expCalendarCode expCalendarCode
     */
    public void setExpCalendarCode(String expCalendarCode) {
        this.expCalendarCode = expCalendarCode;
    }
    /**
     * Get the osCustStockFlag.
     *
     * @return osCustStockFlag
     */
    public String getOsCustStockFlag() {
        return this.osCustStockFlag;
    }
    /**
     * Set the osCustStockFlag.
     *
     * @param osCustStockFlag osCustStockFlag
     */
    public void setOsCustStockFlag(String osCustStockFlag) {
        this.osCustStockFlag = osCustStockFlag;
    }
    /**
     * Get the saCustStockFlag.
     *
     * @return saCustStockFlag
     */
    public String getSaCustStockFlag() {
        return this.saCustStockFlag;
    }
    /**
     * Set the saCustStockFlag.
     *
     * @param saCustStockFlag saCustStockFlag
     */
    public void setSaCustStockFlag(String saCustStockFlag) {
        this.saCustStockFlag = saCustStockFlag;
    }
    /**
     * Get the inventoryBoxFlag.
     *
     * @return inventoryBoxFlag
     */
    public String getInventoryBoxFlag() {
        return this.inventoryBoxFlag;
    }
    /**
     * Set the inventoryBoxFlag.
     *
     * @param inventoryBoxFlag inventoryBoxFlag
     */
    public void setInventoryBoxFlag(String inventoryBoxFlag) {
        this.inventoryBoxFlag = inventoryBoxFlag;
    }
    /**
     * Get the minStock.
     *
     * @return minStock
     */
    public Integer getMinStock() {
        return this.minStock;
    }
    /**
     * Set the minStock.
     *
     * @param minStock minStock
     */
    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }
    /**
     * Get the maxStock.
     *
     * @return maxStock
     */
    public Integer getMaxStock() {
        return this.maxStock;
    }
    /**
     * Set the maxStock.
     *
     * @param maxStock maxStock
     */
    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }
    /**
     * Get the minBox.
     *
     * @return minBox
     */
    public Integer getMinBox() {
        return this.minBox;
    }
    /**
     * Set the minBox.
     *
     * @param minBox minBox
     */
    public void setMinBox(Integer minBox) {
        this.minBox = minBox;
    }
    /**
     * Get the maxBox.
     *
     * @return maxBox
     */
    public Integer getMaxBox() {
        return this.maxBox;
    }
    /**
     * Set the maxBox.
     *
     * @param maxBox maxBox
     */
    public void setMaxBox(Integer maxBox) {
        this.maxBox = maxBox;
    }
    /**
     * Get the orderSafetyStock.
     *
     * @return orderSafetyStock
     */
    public Integer getOrderSafetyStock() {
        return this.orderSafetyStock;
    }
    /**
     * Set the orderSafetyStock.
     *
     * @param orderSafetyStock orderSafetyStock
     */
    public void setOrderSafetyStock(Integer orderSafetyStock) {
        this.orderSafetyStock = orderSafetyStock;
    }
    /**
     * Get the rundownSafetyStock.
     *
     * @return rundownSafetyStock
     */
    public Integer getRundownSafetyStock() {
        return this.rundownSafetyStock;
    }
    /**
     * Set the rundownSafetyStock.
     *
     * @param rundownSafetyStock rundownSafetyStock
     */
    public void setRundownSafetyStock(Integer rundownSafetyStock) {
        this.rundownSafetyStock = rundownSafetyStock;
    }
    /**
     * Get the outboundFluctuation.
     *
     * @return outboundFluctuation
     */
    public Double getOutboundFluctuation() {
        return this.outboundFluctuation;
    }
    /**
     * Set the outboundFluctuation.
     *
     * @param outboundFluctuation outboundFluctuation
     */
    public void setOutboundFluctuation(Double outboundFluctuation) {
        this.outboundFluctuation = outboundFluctuation;
    }
    /**
     * Get the simulationEndDatePattern.
     *
     * @return simulationEndDatePattern
     */
    public String getSimulationEndDatePattern() {
        return this.simulationEndDatePattern;
    }
    /**
     * Set the simulationEndDatePattern.
     *
     * @param simulationEndDatePattern simulationEndDatePattern
     */
    public void setSimulationEndDatePattern(String simulationEndDatePattern) {
        this.simulationEndDatePattern = simulationEndDatePattern;
    }
    /**
     * Get the shippingRouteCode.
     *
     * @return shippingRouteCode
     */
    public String getShippingRouteCode() {
        return this.shippingRouteCode;
    }
    /**
     * Set the shippingRouteCode.
     *
     * @param shippingRouteCode shippingRouteCode
     */
    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;
    }
    /**
     * Get the delayAdjustmentPattern.
     *
     * @return delayAdjustmentPattern
     */
    public String getDelayAdjustmentPattern() {
        return this.delayAdjustmentPattern;
    }
    /**
     * Set the delayAdjustmentPattern.
     *
     * @param delayAdjustmentPattern delayAdjustmentPattern
     */
    public void setDelayAdjustmentPattern(String delayAdjustmentPattern) {
        this.delayAdjustmentPattern = delayAdjustmentPattern;
    }
    /**
     * Get the airEtdLeadtime.
     *
     * @return airEtdLeadtime
     */
    public Integer getAirEtdLeadtime() {
        return this.airEtdLeadtime;
    }
    /**
     * Set the airEtdLeadtime.
     *
     * @param airEtdLeadtime airEtdLeadtime
     */
    public void setAirEtdLeadtime(Integer airEtdLeadtime) {
        this.airEtdLeadtime = airEtdLeadtime;
    }
    /**
     * Get the airEtaLeadtime.
     *
     * @return airEtaLeadtime
     */
    public Integer getAirEtaLeadtime() {
        return this.airEtaLeadtime;
    }
    /**
     * Set the airEtaLeadtime.
     *
     * @param airEtaLeadtime airEtaLeadtime
     */
    public void setAirEtaLeadtime(Integer airEtaLeadtime) {
        this.airEtaLeadtime = airEtaLeadtime;
    }
    /**
     * Get the airInboundLeadtime.
     *
     * @return airInboundLeadtime
     */
    public Integer getAirInboundLeadtime() {
        return this.airInboundLeadtime;
    }
    /**
     * Set the airInboundLeadtime.
     *
     * @param airInboundLeadtime airInboundLeadtime
     */
    public void setAirInboundLeadtime(Integer airInboundLeadtime) {
        this.airInboundLeadtime = airInboundLeadtime;
    }
    /**
     * Get the seaEtaLeadtime.
     *
     * @return seaEtaLeadtime
     */
    public Integer getSeaEtaLeadtime() {
        return this.seaEtaLeadtime;
    }
    /**
     * Set the seaEtaLeadtime.
     *
     * @param seaEtaLeadtime seaEtaLeadtime
     */
    public void setSeaEtaLeadtime(Integer seaEtaLeadtime) {
        this.seaEtaLeadtime = seaEtaLeadtime;
    }
    /**
     * Get the seaInboundLeadtime.
     *
     * @return seaInboundLeadtime
     */
    public Integer getSeaInboundLeadtime() {
        return this.seaInboundLeadtime;
    }
    /**
     * Set the seaInboundLeadtime.
     *
     * @param seaInboundLeadtime seaInboundLeadtime
     */
    public void setSeaInboundLeadtime(Integer seaInboundLeadtime) {
        this.seaInboundLeadtime = seaInboundLeadtime;
    }
    /**
     * Get the allocationFcType.
     *
     * @return allocationFcType
     */
    public String getAllocationFcType() {
        return this.allocationFcType;
    }
    /**
     * Set the allocationFcType.
     *
     * @param allocationFcType allocationFcType
     */
    public void setAllocationFcType(String allocationFcType) {
        this.allocationFcType = allocationFcType;
    }
    /**
     * Get the cfcAdjustmentType1.
     *
     * @return cfcAdjustmentType1
     */
    public String getCfcAdjustmentType1() {
        return this.cfcAdjustmentType1;
    }
    /**
     * Set the cfcAdjustmentType1.
     *
     * @param cfcAdjustmentType1 cfcAdjustmentType1
     */
    public void setCfcAdjustmentType1(String cfcAdjustmentType1) {
        this.cfcAdjustmentType1 = cfcAdjustmentType1;
    }
    /**
     * Get the cfcAdjustmentType2.
     *
     * @return cfcAdjustmentType2
     */
    public String getCfcAdjustmentType2() {
        return this.cfcAdjustmentType2;
    }
    /**
     * Set the cfcAdjustmentType2.
     *
     * @param cfcAdjustmentType2 cfcAdjustmentType2
     */
    public void setCfcAdjustmentType2(String cfcAdjustmentType2) {
        this.cfcAdjustmentType2 = cfcAdjustmentType2;
    }
    /**
     * Get the remark1.
     *
     * @return remark1
     */
    public String getRemark1() {
        return this.remark1;
    }
    /**
     * Set the remark1.
     *
     * @param remark1 remark1
     */
    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }
    /**
     * Get the remark2.
     *
     * @return remark2
     */
    public String getRemark2() {
        return this.remark2;
    }
    /**
     * Set the remark2.
     *
     * @param remark2 remark2
     */
    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }
    /**
     * Get the remark3.
     *
     * @return remark3
     */
    public String getRemark3() {
        return this.remark3;
    }
    /**
     * Set the remark3.
     *
     * @param remark3 remark3
     */
    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }
    /**
     * Get the buildoutFlag.
     *
     * @return buildoutFlag
     */
    public String getBuildoutFlag() {
        return this.buildoutFlag;
    }
    /**
     * Set the buildoutFlag.
     *
     * @param buildoutFlag buildoutFlag
     */
    public void setBuildoutFlag(String buildoutFlag) {
        this.buildoutFlag = buildoutFlag;
    }
    /**
     * Get the buildoutMonth.
     *
     * @return buildoutMonth
     */
    public String getBuildoutMonth() {
        return this.buildoutMonth;
    }
    /**
     * Set the buildoutMonth.
     *
     * @param buildoutMonth buildoutMonth
     */
    public void setBuildoutMonth(String buildoutMonth) {
        this.buildoutMonth = buildoutMonth;
    }
    /**
     * Get the lastPoMonth.
     *
     * @return lastPoMonth
     */
    public String getLastPoMonth() {
        return this.lastPoMonth;
    }
    /**
     * Set the lastPoMonth.
     *
     * @param lastPoMonth lastPoMonth
     */
    public void setLastPoMonth(String lastPoMonth) {
        this.lastPoMonth = lastPoMonth;
    }
    /**
     * Get the lastDeliveryMonth.
     *
     * @return lastDeliveryMonth
     */
    public String getLastDeliveryMonth() {
        return this.lastDeliveryMonth;
    }
    /**
     * Set the lastDeliveryMonth.
     *
     * @param lastDeliveryMonth lastDeliveryMonth
     */
    public void setLastDeliveryMonth(String lastDeliveryMonth) {
        this.lastDeliveryMonth = lastDeliveryMonth;
    }
    /**
     * Get the partsStatus.
     *
     * @return partsStatus
     */
    public String getPartsStatus() {
        return this.partsStatus;
    }
    /**
     * Set the partsStatus.
     *
     * @param partsStatus partsStatus
     */
    public void setPartsStatus(String partsStatus) {
        this.partsStatus = partsStatus;
    }
    /**
     * Get the inactiveFlag.
     *
     * @return inactiveFlag
     */
    public String getInactiveFlag() {
        return this.inactiveFlag;
    }
    /**
     * Set the inactiveFlag.
     *
     * @param inactiveFlag inactiveFlag
     */
    public void setInactiveFlag(String inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
    }
    
}
