package com.chinaplus.web.mm.entity;

/**
 * The persistent class for the TNM_IF_PARTS database table.
 * 
 */

public class TnmIfPart extends MMCommonEntity {

    private static final long serialVersionUID = 1L;

    /** officeId the officeId */
    private Integer officeId;

    /** ifPartsId the ifPartsId */
    private Integer ifPartsId;

    /** expCode the expCode */
    private String expCode;

    /** ttcPartsNo the ttcPartsNo */
    private String ttcPartsNo;

    /** ttcPartsName the ttcPartsName */
    private String ttcPartsName;

    /** customerCode the customerCode */
    private String customerCode;

    /** customerPartsNo the customerPartsNo */
    private String customerPartsNo;

    /** supplierCode the supplierCode */
    private String supplierCode;

    /** mainRoute the mainRoute */
    private String mainRoute;

    /** spq the spq */
    private String spq;

    /** orderLot the orderLot */
    private String orderLot;

    /** uom the uom */
    private String uom;

    /** buildoutFlag the buildoutFlag */
    private String buildoutFlag;

    /** vendorRoute the vendorRoute */
    private String vendorRoute;

    /** inactiveFlag the inactiveFlag */
    private Integer inactiveFlag;

    /**
     * Get the inactiveFlag.
     *
     * @return inactiveFlag
     */
    public Integer getInactiveFlag() {
        return this.inactiveFlag;
    }

    /**
     * Set the inactiveFlag.
     *
     * @param inactiveFlag inactiveFlag
     */
    public void setInactiveFlag(Integer inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
    }

    /**
     * Get the vendorRoute.
     *
     * @return vendorRoute
     */
    public String getVendorRoute() {
        return this.vendorRoute;
    }

    /**
     * Set the vendorRoute.
     *
     * @param vendorRoute vendorRoute
     */
    public void setVendorRoute(String vendorRoute) {
        this.vendorRoute = vendorRoute;
    }

    /**
     * Get the ifPartsId.
     *
     * @return ifPartsId
     */
    public Integer getIfPartsId() {
        return this.ifPartsId;
    }

    /**
     * Set the ifPartsId.
     *
     * @param ifPartsId ifPartsId
     */
    public void setIfPartsId(Integer ifPartsId) {
        this.ifPartsId = ifPartsId;
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
     * Get the expCode.
     *
     * @return expCode
     */
    public String getExpCode() {
        return this.expCode;
    }

    /**
     * Set the expCode.
     *
     * @param expCode expCode
     */
    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }

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
     * Get the customerPartsNo.
     *
     * @return customerPartsNo
     */
    public String getCustomerPartsNo() {
        return this.customerPartsNo;
    }

    /**
     * Set the customerPartsNo.
     *
     * @param customerPartsNo customerPartsNo
     */
    public void setCustomerPartsNo(String customerPartsNo) {
        this.customerPartsNo = customerPartsNo;
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
     * Get the mainRoute.
     *
     * @return mainRoute
     */
    public String getMainRoute() {
        return this.mainRoute;
    }

    /**
     * Set the mainRoute.
     *
     * @param mainRoute mainRoute
     */
    public void setMainRoute(String mainRoute) {
        this.mainRoute = mainRoute;
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
     * Get the orderLot.
     *
     * @return orderLot
     */
    public String getOrderLot() {
        return this.orderLot;
    }

    /**
     * Set the orderLot.
     *
     * @param orderLot orderLot
     */
    public void setOrderLot(String orderLot) {
        this.orderLot = orderLot;
    }

    /**
     * Get the uom.
     *
     * @return uom
     */
    public String getUom() {
        return this.uom;
    }

    /**
     * Set the uom.
     *
     * @param uom uom
     */
    public void setUom(String uom) {
        this.uom = uom;
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

}