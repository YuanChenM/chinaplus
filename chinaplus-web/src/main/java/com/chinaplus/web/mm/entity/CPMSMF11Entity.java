/**
 * @screen CPMSMF11
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;


/**
 * CPMSMF11
 * 
 */
public class CPMSMF11Entity extends CPMSMF01Entity {

    /** serialVersionUID */
    private static final long serialVersionUID = 3380974115202426680L;
    
    /** rowNum  the  rowNum */
    private Integer  rowNum;
    
    /** the vendorRoute */
    private String vendorRoute;
    
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

}