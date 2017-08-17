/**
 * CPMSRF02Entity.java
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

/**
 * CPMSRF02Entity.
 */
public class CPMSRF12Entity extends CPMSRF02Entity {

    /** serialVersionUID */
    private static final long serialVersionUID = 6901683044031317722L;

    /** type the type */
    private String type;

    /** rowNum the rowNum */
    private Integer rowNum;

    /** shippingRouteType the shippingRouteType */
    private String shippingRouteType;

    /** officeId the officeId */
    private Integer officeId;

    /** srId the srId */
    private Integer srId;

    /** EXP Region */
    private String expRegion;

    /**
     * Get the srId.
     *
     * @return srId
     */
    public Integer getSrId() {
        return this.srId;
    }

    /**
     * Set the srId.
     *
     * @param srId srId
     */
    public void setSrId(Integer srId) {
        this.srId = srId;
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
     * Get the shippingRouteType.
     *
     * @return shippingRouteType
     */
    public String getShippingRouteType() {
        return this.shippingRouteType;
    }

    /**
     * Set the shippingRouteType.
     *
     * @param shippingRouteType shippingRouteType
     */
    public void setShippingRouteType(String shippingRouteType) {
        this.shippingRouteType = shippingRouteType;
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

}
