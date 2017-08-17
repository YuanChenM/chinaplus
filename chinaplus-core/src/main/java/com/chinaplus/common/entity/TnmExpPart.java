package com.chinaplus.common.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;

/**
 * The persistent class for the TNM_EXP_PARTS database table.
 * 
 */
@Entity(name = "TNM_EXP_PARTS")
public class TnmExpPart extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNM_EXP_PARTS_EXPPARTSID_GENERATOR",
        sequenceName = "SEQ_TNM_EXP_PARTS",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNM_EXP_PARTS_EXPPARTSID_GENERATOR")
    @Column(name = "EXP_PARTS_ID")
    private Integer expPartsId;

    @Column(name = "AIR_ETA_LEADTIME")
    private Integer airEtaLeadtime;

    @Column(name = "AIR_ETD_LEADTIME")
    private Integer airEtdLeadtime;

    @Column(name = "AIR_INBOUND_LEADTIME")
    private Integer airInboundLeadtime;

    @Column(name = "BUILDOUT_FLAG")
    private Integer buildoutFlag;

    @Column(name = "BUILDOUT_MONTH")
    private String buildoutMonth;

    @Column(name = "BUSINESS_PATTERN")
    private Integer businessPattern;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CUST_PARTS_NO")
    private String custPartsNo;

    @Column(name = "DELAY_ADJUSTMENT_PATTERN")
    private Integer delayAdjustmentPattern;

    @Column(name = "EXP_CODE")
    private String expCode;

    @Column(name = "SSMS_CUST_CODE")
    private String ssmsCustCode;

    @Column(name = "EXP_REGION")
    private String expRegion;

    @Column(name = "EXP_SUPP_CODE")
    private String expSuppCode;

    @Column(name = "EXP_UOM_CODE")
    private String expUomCode;

    @Column(name = "INACTIVE_FLAG")
    private Integer inactiveFlag;

    @Column(name = "LAST_DELIVERY_MONTH")
    private String lastDeliveryMonth;

    @Column(name = "LAST_PO_MONTH")
    private String lastPoMonth;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "ORDER_LOT")
    private BigDecimal orderLot;

    @Column(name = "PARTS_STATUS")
    private Integer partsStatus;

    @Column(name = "REMARK1")
    private String remark1;

    @Column(name = "REMARK2")
    private String remark2;

    @Column(name = "REMARK3")
    private String remark3;

    @Column(name = "SEA_ETA_LEADTIME")
    private Integer seaEtaLeadtime;

    @Column(name = "SEA_INBOUND_LEADTIME")
    private Integer seaInboundLeadtime;

    @Column(name = "SHIPPING_ROUTE_CODE")
    private String shippingRouteCode;

    @Column(name = "SPQ")
    private BigDecimal spq;

    @Column(name = "SRBQ")
    private BigDecimal srbq;

    @Column(name = "SSMS_MAIN_ROUTE")
    private String ssmsMainRoute;

    @Column(name = "SSMS_VENDOR_ROUTE")
    private String ssmsVendorRoute;

    @Column(name = "SUPP_PARTS_NO")
    private String suppPartsNo;

    @Column(name = "SUPPLIER_ID")
    private Integer supplierId;

    @Column(name = "TTC_PARTS_NAME")
    private String ttcPartsName;

    @Column(name = "TTC_PARTS_NO")
    private String ttcPartsNo;

    @Column(name = "TTC_SUPP_CODE")
    private String ttcSuppCode;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "WEST_CUST_CODE")
    private String westCustCode;

    @Column(name = "WEST_PARTS_NO")
    private String westPartsNo;

    // bi-directional many-to-one association to TnmPartsMaster
   // @ManyToOne(fetch = FetchType.LAZY)
   // @JoinColumn(name = "PARTS_ID")
   // private TnmPartsMaster tnmPartsMaster;
    @Column(name = "PARTS_ID")
    private Integer partsId;

    public TnmExpPart() {}

    public Integer getExpPartsId() {
        return this.expPartsId;
    }

    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;
    }

    public Integer getAirEtaLeadtime() {
        return this.airEtaLeadtime;
    }

    public void setAirEtaLeadtime(Integer airEtaLeadtime) {
        this.airEtaLeadtime = airEtaLeadtime;
    }

    public Integer getAirEtdLeadtime() {
        return this.airEtdLeadtime;
    }

    public void setAirEtdLeadtime(Integer airEtdLeadtime) {
        this.airEtdLeadtime = airEtdLeadtime;
    }

    public Integer getAirInboundLeadtime() {
        return this.airInboundLeadtime;
    }

    public void setAirInboundLeadtime(Integer airInboundLeadtime) {
        this.airInboundLeadtime = airInboundLeadtime;
    }

    public Integer getBuildoutFlag() {
        return this.buildoutFlag;
    }

    public void setBuildoutFlag(Integer buildoutFlag) {
        this.buildoutFlag = buildoutFlag;
    }

    public String getBuildoutMonth() {
        return this.buildoutMonth;
    }

    public void setBuildoutMonth(String buildoutMonth) {
        this.buildoutMonth = buildoutMonth;
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

    public String getCustPartsNo() {
        return this.custPartsNo;
    }

    public void setCustPartsNo(String custPartsNo) {
        this.custPartsNo = custPartsNo;
    }

    public Integer getDelayAdjustmentPattern() {
        return this.delayAdjustmentPattern;
    }

    public void setDelayAdjustmentPattern(Integer delayAdjustmentPattern) {
        this.delayAdjustmentPattern = delayAdjustmentPattern;
    }

    public String getExpCode() {
        return this.expCode;
    }

    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

    public String getSsmsCustCode() {
        return this.ssmsCustCode;
    }

    public void setSsmsCustCode(String ssmsCustCode) {
        this.ssmsCustCode = ssmsCustCode;
    }

    public String getExpRegion() {
        return this.expRegion;
    }

    public void setExpRegion(String expRegion) {
        this.expRegion = expRegion;
    }

    public String getExpSuppCode() {
        return this.expSuppCode;
    }

    public void setExpSuppCode(String expSuppCode) {
        this.expSuppCode = expSuppCode;
    }

    public String getExpUomCode() {
        return this.expUomCode;
    }

    public void setExpUomCode(String expUomCode) {
        this.expUomCode = expUomCode;
    }

    public Integer getInactiveFlag() {
        return this.inactiveFlag;
    }

    public void setInactiveFlag(Integer inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
    }

    public String getLastDeliveryMonth() {
        return this.lastDeliveryMonth;
    }

    public void setLastDeliveryMonth(String lastDeliveryMonth) {
        this.lastDeliveryMonth = lastDeliveryMonth;
    }

    public String getLastPoMonth() {
        return this.lastPoMonth;
    }

    public void setLastPoMonth(String lastPoMonth) {
        this.lastPoMonth = lastPoMonth;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public BigDecimal getOrderLot() {
        return this.orderLot;
    }

    public void setOrderLot(BigDecimal orderLot) {
        this.orderLot = orderLot;
    }

    public Integer getPartsStatus() {
        return this.partsStatus;
    }

    public void setPartsStatus(Integer partsStatus) {
        this.partsStatus = partsStatus;
    }

    public String getRemark1() {
        return this.remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return this.remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return this.remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public Integer getSeaEtaLeadtime() {
        return this.seaEtaLeadtime;
    }

    public void setSeaEtaLeadtime(Integer seaEtaLeadtime) {
        this.seaEtaLeadtime = seaEtaLeadtime;
    }

    public Integer getSeaInboundLeadtime() {
        return this.seaInboundLeadtime;
    }

    public void setSeaInboundLeadtime(Integer seaInboundLeadtime) {
        this.seaInboundLeadtime = seaInboundLeadtime;
    }

    public String getShippingRouteCode() {
        return this.shippingRouteCode;
    }

    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;
    }

    public BigDecimal getSpq() {
        return this.spq;
    }

    public void setSpq(BigDecimal spq) {
        this.spq = spq;
    }

    public BigDecimal getSrbq() {
        return this.srbq;
    }

    public void setSrbq(BigDecimal srbq) {
        this.srbq = srbq;
    }

    public String getSsmsMainRoute() {
        return this.ssmsMainRoute;
    }

    public void setSsmsMainRoute(String ssmsMainRoute) {
        this.ssmsMainRoute = ssmsMainRoute;
    }

    public String getSsmsVendorRoute() {
        return this.ssmsVendorRoute;
    }

    public void setSsmsVendorRoute(String ssmsVendorRoute) {
        this.ssmsVendorRoute = ssmsVendorRoute;
    }

    public String getSuppPartsNo() {
        return this.suppPartsNo;
    }

    public void setSuppPartsNo(String suppPartsNo) {
        this.suppPartsNo = suppPartsNo;
    }

    public Integer getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getTtcPartsName() {
        return this.ttcPartsName;
    }

    public void setTtcPartsName(String ttcPartsName) {
        this.ttcPartsName = ttcPartsName;
    }

    public String getTtcPartsNo() {
        return this.ttcPartsNo;
    }

    public void setTtcPartsNo(String ttcPartsNo) {
        this.ttcPartsNo = ttcPartsNo;
    }

    public String getTtcSuppCode() {
        return this.ttcSuppCode;
    }

    public void setTtcSuppCode(String ttcSuppCode) {
        this.ttcSuppCode = ttcSuppCode;
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

    public String getWestCustCode() {
        return this.westCustCode;
    }

    public void setWestCustCode(String westCustCode) {
        this.westCustCode = westCustCode;
    }

    public String getWestPartsNo() {
        return this.westPartsNo;
    }

    public void setWestPartsNo(String westPartsNo) {
        this.westPartsNo = westPartsNo;
    }

    /**
     * Get the partsId.
     *
     * @return partsId
     *
     * @author liu_yinchuan
     */
    public Integer getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     *
     * @author liu_yinchuan
     */
    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
        
    }

    /*public TnmPartsMaster getTnmPartsMaster() {
        return this.tnmPartsMaster;
    }

    public void setTnmPartsMaster(TnmPartsMaster tnmPartsMaster) {
        this.tnmPartsMaster = tnmPartsMaster;
    }
*/
}