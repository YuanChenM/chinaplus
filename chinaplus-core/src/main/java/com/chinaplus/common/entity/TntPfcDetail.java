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
 * The persistent class for the TNT_PFC_DETAIL database table.
 * 
 */
@Entity(name = "TNT_PFC_DETAIL")
public class TntPfcDetail extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_PFC_DETAIL_PFCDETAILID_GENERATOR",
        sequenceName = "SEQ_TNT_PFC_DETAIL",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_PFC_DETAIL_PFCDETAILID_GENERATOR")
    @Column(name = "PFC_DETAIL_ID")
    private Integer pfcDetailId;

    @Column(name = "BUYING_CURRENCY")
    private String buyingCurrency;

    @Column(name = "BUYING_PRICE")
    private BigDecimal buyingPrice;

    private String cpn;

    private String cpo;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "FC_QTY1")
    private BigDecimal fcQty1;

    @Column(name = "FC_QTY10")
    private BigDecimal fcQty10;

    @Column(name = "FC_QTY11")
    private BigDecimal fcQty11;

    @Column(name = "FC_QTY12")
    private BigDecimal fcQty12;

    @Column(name = "FC_QTY2")
    private BigDecimal fcQty2;

    @Column(name = "FC_QTY3")
    private BigDecimal fcQty3;

    @Column(name = "FC_QTY4")
    private BigDecimal fcQty4;

    @Column(name = "FC_QTY5")
    private BigDecimal fcQty5;

    @Column(name = "FC_QTY6")
    private BigDecimal fcQty6;

    @Column(name = "FC_QTY7")
    private BigDecimal fcQty7;

    @Column(name = "FC_QTY8")
    private BigDecimal fcQty8;

    @Column(name = "FC_QTY9")
    private BigDecimal fcQty9;

    @Column(name = "KANBAN_PLAN_NO")
    private String kanbanPlanNo;

    @Column(name = "ORDER_LOT")
    private BigDecimal orderLot;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    @Column(name = "PRICE_UNIT")
    private BigDecimal priceUnit;

    @Column(name = "PURCHASE_QTY")
    private BigDecimal purchaseQty;

    @Column(name = "REDO_SHIPPING_FLAG")
    private Integer redoShippingFlag;

    @Column(name = "SALE_CONTRACT_NO")
    private String saleContractNo;

    @Column(name = "SALE_QTY")
    private BigDecimal saleQty;

    @Column(name = "SHIPPING_ROUTE_CODE")
    private String shippingRouteCode;

    @Column(name = "SUPP_REGION")
    private String suppRegion;

    @Column(name = "SUPPLIER_ID")
    private Integer supplierId;

    @Column(name = "TRADE_NAME")
    private String tradeName;

    @Column(name = "TRADE_NO")
    private String tradeNo;

    private String uom;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "ZERO_FLAG")
    private String zeroFlag;

    @Column(name = "PFC_ID")
    private Integer pfcId;

    public TntPfcDetail() {}

    public Integer getPfcDetailId() {
        return this.pfcDetailId;
    }

    public void setPfcDetailId(Integer pfcDetailId) {
        this.pfcDetailId = pfcDetailId;
    }

    public String getBuyingCurrency() {
        return this.buyingCurrency;
    }

    public void setBuyingCurrency(String buyingCurrency) {
        this.buyingCurrency = buyingCurrency;
    }

    public BigDecimal getBuyingPrice() {
        return this.buyingPrice;
    }

    public void setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getCpn() {
        return this.cpn;
    }

    public void setCpn(String cpn) {
        this.cpn = cpn;
    }

    public String getCpo() {
        return this.cpo;
    }

    public void setCpo(String cpo) {
        this.cpo = cpo;
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

    public BigDecimal getFcQty1() {
        return this.fcQty1;
    }

    public void setFcQty1(BigDecimal fcQty1) {
        this.fcQty1 = fcQty1;
    }

    public BigDecimal getFcQty10() {
        return this.fcQty10;
    }

    public void setFcQty10(BigDecimal fcQty10) {
        this.fcQty10 = fcQty10;
    }

    public BigDecimal getFcQty11() {
        return this.fcQty11;
    }

    public void setFcQty11(BigDecimal fcQty11) {
        this.fcQty11 = fcQty11;
    }

    public BigDecimal getFcQty12() {
        return this.fcQty12;
    }

    public void setFcQty12(BigDecimal fcQty12) {
        this.fcQty12 = fcQty12;
    }

    public BigDecimal getFcQty2() {
        return this.fcQty2;
    }

    public void setFcQty2(BigDecimal fcQty2) {
        this.fcQty2 = fcQty2;
    }

    public BigDecimal getFcQty3() {
        return this.fcQty3;
    }

    public void setFcQty3(BigDecimal fcQty3) {
        this.fcQty3 = fcQty3;
    }

    public BigDecimal getFcQty4() {
        return this.fcQty4;
    }

    public void setFcQty4(BigDecimal fcQty4) {
        this.fcQty4 = fcQty4;
    }

    public BigDecimal getFcQty5() {
        return this.fcQty5;
    }

    public void setFcQty5(BigDecimal fcQty5) {
        this.fcQty5 = fcQty5;
    }

    public BigDecimal getFcQty6() {
        return this.fcQty6;
    }

    public void setFcQty6(BigDecimal fcQty6) {
        this.fcQty6 = fcQty6;
    }

    public BigDecimal getFcQty7() {
        return this.fcQty7;
    }

    public void setFcQty7(BigDecimal fcQty7) {
        this.fcQty7 = fcQty7;
    }

    public BigDecimal getFcQty8() {
        return this.fcQty8;
    }

    public void setFcQty8(BigDecimal fcQty8) {
        this.fcQty8 = fcQty8;
    }

    public BigDecimal getFcQty9() {
        return this.fcQty9;
    }

    public void setFcQty9(BigDecimal fcQty9) {
        this.fcQty9 = fcQty9;
    }

    public String getKanbanPlanNo() {
        return this.kanbanPlanNo;
    }

    public void setKanbanPlanNo(String kanbanPlanNo) {
        this.kanbanPlanNo = kanbanPlanNo;
    }

    public BigDecimal getOrderLot() {
        return this.orderLot;
    }

    public void setOrderLot(BigDecimal orderLot) {
        this.orderLot = orderLot;
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public BigDecimal getPriceUnit() {
        return this.priceUnit;
    }

    public void setPriceUnit(BigDecimal priceUnit) {
        this.priceUnit = priceUnit;
    }

    public BigDecimal getPurchaseQty() {
        return this.purchaseQty;
    }

    public void setPurchaseQty(BigDecimal purchaseQty) {
        this.purchaseQty = purchaseQty;
    }

    public Integer getRedoShippingFlag() {
        return this.redoShippingFlag;
    }

    public void setRedoShippingFlag(Integer redoShippingFlag) {
        this.redoShippingFlag = redoShippingFlag;
    }

    public String getSaleContractNo() {
        return this.saleContractNo;
    }

    public void setSaleContractNo(String saleContractNo) {
        this.saleContractNo = saleContractNo;
    }

    public BigDecimal getSaleQty() {
        return this.saleQty;
    }

    public void setSaleQty(BigDecimal saleQty) {
        this.saleQty = saleQty;
    }

    public String getShippingRouteCode() {
        return this.shippingRouteCode;
    }

    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;
    }

    public String getSuppRegion() {
        return this.suppRegion;
    }

    public void setSuppRegion(String suppRegion) {
        this.suppRegion = suppRegion;
    }

    public Integer getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getTradeName() {
        return this.tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getTradeNo() {
        return this.tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getUom() {
        return this.uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
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

    public String getZeroFlag() {
        return this.zeroFlag;
    }

    public void setZeroFlag(String zeroFlag) {
        this.zeroFlag = zeroFlag;
    }

    public Integer getPfcId() {
        return this.pfcId;
    }

    public void setPfcId(Integer pfcId) {
        this.pfcId = pfcId;

    }

}