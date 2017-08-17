/**
 * @screen CPMPMS02
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * CPMPMS02Entity.
 */
public class CPMPMS02Entity extends BaseEntity {
  
    /** serialVersionUID */
    private static final long serialVersionUID = 5891553838561686745L;
    

//    /** the vvFlag */
//    private Boolean  vvFlag;
//    
//    /** the shipRouteType */
//    private Boolean  aisinFlag;

    /** the effFromEtd */
    private Date effFromEtd; 
    
    /** the effFromEtdStr */
    private String effFromEtdStr; 
    
    /** the effTotd */
    private Date effTotd; 

    /** the effTotdStr */
    private String effTotdStr; 
    
    /** the orderMonthFrom */
    private String orderMonthFrom;
    
    /** the orderMonth */
    private String orderMonthTo;
    
    /**
     * Get the effFromEtdStr.
     *
     * @return effFromEtdStr
     */
    public String getEffFromEtdStr() {
        return this.effFromEtdStr;
    }

    /**
     * Set the effFromEtdStr.
     *
     * @param effFromEtdStr effFromEtdStr
     */
    public void setEffFromEtdStr(String effFromEtdStr) {
        this.effFromEtdStr = effFromEtdStr;
    }

    /**
     * Get the effTotdStr.
     *
     * @return effTotdStr
     */
    public String getEffTotdStr() {
        return this.effTotdStr;
    }

    /**
     * Set the effTotdStr.
     *
     * @param effTotdStr effTotdStr
     */
    public void setEffTotdStr(String effTotdStr) {
        this.effTotdStr = effTotdStr;
    }

    /**
     * Get the orderMonthFrom.
     *
     * @return orderMonthFrom
     */
    public String getOrderMonthFrom() {
        return this.orderMonthFrom;
    }

    /**
     * Set the orderMonthFrom.
     *
     * @param orderMonthFrom orderMonthFrom
     */
    public void setOrderMonthFrom(String orderMonthFrom) {
        this.orderMonthFrom = orderMonthFrom;
    }

    /**
     * Get the orderMonthTo.
     *
     * @return orderMonthTo
     */
    public String getOrderMonthTo() {
        return this.orderMonthTo;
    }

    /**
     * Set the orderMonthTo.
     *
     * @param orderMonthTo orderMonthTo
     */
    public void setOrderMonthTo(String orderMonthTo) {
        this.orderMonthTo = orderMonthTo;
    }
    
    /**
     * Get the effTotd.
     *
     * @return effTotd
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getEffTotd() {
        return this.effTotd;
    }

    /**
     * Set the effTotd.
     *
     * @param effTotd effTotd
     */
    public void setEffTotd(Date effTotd) {
        this.effTotd = effTotd;
    }

    /**
     * Get the effFromEtd.
     *
     * @return effFromEtd
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getEffFromEtd() {
        return this.effFromEtd;
    }

    /**
     * Set the effFromEtd.
     *
     * @param effFromEtd effFromEtd
     */
    public void setEffFromEtd(Date effFromEtd) {
        this.effFromEtd = effFromEtd;
    }

       
//    /**
//     * Get the vvFlag.
//     *
//     * @return vvFlag
//     */
//    public Boolean isVvFlag() {
//        return this.vvFlag;
//    }
//
//    /**
//     * Set the vvFlag.
//     *
//     * @param vvFlag vvFlag
//     */
//    public void setVvFlag(Boolean vvFlag) {
//        this.vvFlag = vvFlag;
//    }
//
//    /**
//     * Get the aisinFlag.
//     *
//     * @return aisinFlag
//     */
//    public Boolean isAisinFlag() {
//        return this.aisinFlag;
//    }
//
//    /**
//     * Set the aisinFlag.
//     *
//     * @param aisinFlag aisinFlag
//     */
//    public void setAisinFlag(Boolean aisinFlag) {
//        this.aisinFlag = aisinFlag;
//    }

}
