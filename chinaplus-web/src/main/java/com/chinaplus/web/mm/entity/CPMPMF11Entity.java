/**
 * CPMPMF11Entity.java
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPMPMF11Entity
 */
public class CPMPMF11Entity extends CPMPMF01Entity {

    /** serialVersionUID */
    private static final long serialVersionUID = -5223799592079817992L;

    /** type the type */
    private String type;

    /** rowNum the rowNum */
    private Integer rowNum;

    /** partsId the partsId */
    private Integer partsId;

    /** expPartsId the expPartsId */
    private Integer expPartsId;

    /** expInnerPartsId the expInnerPartsId */
    private Integer expInnerPartsId;

    /** ordeTime the ordeTime */
    private String orderTime;

    /** minStockS the minStockS */
    private String minStockS;
    /** maxStockS the maxStockS */
    private String maxStockS;
    /** minBoxS the minBoxS */
    private String minBoxS;
    /** maxBoxS the maxBoxS */
    private String maxBoxS;

    /** toEtd the toEtd */
    private Date toEtd;

    /** ttcSuppCodeType the ttcSuppCodeType */
    private String ttcSuppCodeType;
    /** impWhsCodeType the impWhsCodeType */
    private String impWhsCodeType;

    /** officeId the officeId */
    private Integer officeId;

    /** customerId the customerId */
    private Integer customerId;

    /** partsStatusFlag the partsStatusFlag */
    private boolean partsStatusFlag;

    /** sheetName the sheetName */
    private String sheetName;

    /** supplierId the supplierId */
    private Integer supplierId;

    /** shipRouteCodeChange the shipRouteCodeChange */
    private boolean shipRouteCodeChange;

    /** the digits */
    private Integer digits;

    /**
     * Get the shipRouteCodeChange.
     *
     * @return shipRouteCodeChange
     */
    public boolean isShipRouteCodeChange() {
        return this.shipRouteCodeChange;
    }

    /**
     * Set the shipRouteCodeChange.
     *
     * @param shipRouteCodeChange shipRouteCodeChange
     */
    public void setShipRouteCodeChange(boolean shipRouteCodeChange) {
        this.shipRouteCodeChange = shipRouteCodeChange;
    }

    /**
     * Get the supplierId.
     *
     * @return supplierId
     */
    public Integer getSupplierId() {
        return this.supplierId;
    }

    /**
     * Set the supplierId.
     *
     * @param supplierId supplierId
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Get the sheetName.
     *
     * @return sheetName
     */
    public String getSheetName() {
        return this.sheetName;
    }

    /**
     * Set the sheetName.
     *
     * @param sheetName sheetName
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * Get the partsStatusFlag.
     *
     * @return partsStatusFlag
     */
    public boolean isPartsStatusFlag() {
        return this.partsStatusFlag;
    }

    /**
     * Set the partsStatusFlag.
     *
     * @param partsStatusFlag partsStatusFlag
     */
    public void setPartsStatusFlag(boolean partsStatusFlag) {
        this.partsStatusFlag = partsStatusFlag;
    }

    /**
     * Get the expInnerPartsId.
     *
     * @return expInnerPartsId
     */
    public Integer getExpInnerPartsId() {
        return this.expInnerPartsId;
    }

    /**
     * Set the expInnerPartsId.
     *
     * @param expInnerPartsId expInnerPartsId
     */
    public void setExpInnerPartsId(Integer expInnerPartsId) {
        this.expInnerPartsId = expInnerPartsId;
    }

    /**
     * Get the expPartsId.
     *
     * @return expPartsId
     */
    public Integer getExpPartsId() {
        return this.expPartsId;
    }

    /**
     * Set the expPartsId.
     *
     * @param expPartsId expPartsId
     */
    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;
    }

    /**
     * Get the customerId.
     *
     * @return customerId
     */
    public Integer getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     *
     * @param customerId customerId
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

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
     * Get the impWhsCodeType.
     *
     * @return impWhsCodeType
     */
    public String getImpWhsCodeType() {
        return this.impWhsCodeType;
    }

    /**
     * Set the impWhsCodeType.
     *
     * @param impWhsCodeType impWhsCodeType
     */
    public void setImpWhsCodeType(String impWhsCodeType) {
        this.impWhsCodeType = impWhsCodeType;
    }

    /**
     * Get the ttcSuppCodeType.
     *
     * @return ttcSuppCodeType
     */
    public String getTtcSuppCodeType() {
        return this.ttcSuppCodeType;
    }

    /**
     * Set the ttcSuppCodeType.
     *
     * @param ttcSuppCodeType ttcSuppCodeType
     */
    public void setTtcSuppCodeType(String ttcSuppCodeType) {
        this.ttcSuppCodeType = ttcSuppCodeType;
    }

    /**
     * Get the toEtd.
     *
     * @return toEtd
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getToEtd() {
        return this.toEtd;
    }

    /**
     * Set the toEtd.
     *
     * @param toEtd toEtd
     */
    public void setToEtd(Date toEtd) {
        this.toEtd = toEtd;
    }

    /**
     * Get the minStockS.
     *
     * @return minStockS
     */
    public String getMinStockS() {
        return this.minStockS;
    }

    /**
     * Set the minStockS.
     *
     * @param minStockS minStockS
     */
    public void setMinStockS(String minStockS) {
        this.minStockS = minStockS;
    }

    /**
     * Get the maxStockS.
     *
     * @return maxStockS
     */
    public String getMaxStockS() {
        return this.maxStockS;
    }

    /**
     * Set the maxStockS.
     *
     * @param maxStockS maxStockS
     */
    public void setMaxStockS(String maxStockS) {
        this.maxStockS = maxStockS;
    }

    /**
     * Get the minBoxS.
     *
     * @return minBoxS
     */
    public String getMinBoxS() {
        return this.minBoxS;
    }

    /**
     * Set the minBoxS.
     *
     * @param minBoxS minBoxS
     */
    public void setMinBoxS(String minBoxS) {
        this.minBoxS = minBoxS;
    }

    /**
     * Get the maxBoxS.
     *
     * @return maxBoxS
     */
    public String getMaxBoxS() {
        return this.maxBoxS;
    }

    /**
     * Set the maxBoxS.
     *
     * @param maxBoxS maxBoxS
     */
    public void setMaxBoxS(String maxBoxS) {
        this.maxBoxS = maxBoxS;
    }

    /**
     * Get the orderTime.
     *
     * @return orderTime
     */
    public String getOrderTime() {
        return this.orderTime;
    }

    /**
     * Set the orderTime.
     *
     * @param orderTime orderTime
     */
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * Get the partsId.
     *
     * @return partsId
     */
    public Integer getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     */
    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    /**
     * Get the rowNum.
     *
     * @return rowNum
     */
    public Integer getRowNum() {
        return this.rowNum;
    }

    /**
     * Set the rowNum.
     *
     * @param rowNum rowNum
     */
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    /**
     * Get the type.
     *
     * @return type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Set the type.
     *
     * @param type type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the digits.
     *
     * @return digits
     */
    public Integer getDigits() {
        return this.digits;
    }

    /**
     * Set the digits.
     *
     * @param digits digits
     */
    public void setDigits(Integer digits) {
        this.digits = digits;
    }
}
