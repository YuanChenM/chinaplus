/**
 * CPMSRF02Entity.java
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/** 
 * CPMSRF02Entity.
 */
public class CPMSRF03Entity  extends CPMSRF02Entity{

    /** serialVersionUID */
    private static final long serialVersionUID = 6901683044031317722L;
   

    /** deliveryStartDate  the deliveryStartDate */
    private  Date  deliveryStartDate;  

    /** impInboundLeadtime  the impInboundLeadtime */
    private  Integer  impInboundLeadtime;
    
    
    /**
     * Get the deliveryStartDate.
     *
     * @return deliveryStartDate
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getDeliveryStartDate() {
        return this.deliveryStartDate;
    }

    /**
     * Set the deliveryStartDate.
     *
     * @param deliveryStartDate deliveryStartDate
     */
    public void setDeliveryStartDate(Date deliveryStartDate) {
        this.deliveryStartDate = deliveryStartDate;
    }

    /**
     * Get the impInboundLeadtime.
     *
     * @return impInboundLeadtime
     */
    public Integer getImpInboundLeadtime() {
        return this.impInboundLeadtime;
    }

    /**
     * Set the impInboundLeadtime.
     *
     * @param impInboundLeadtime impInboundLeadtime
     */
    public void setImpInboundLeadtime(Integer impInboundLeadtime) {
        this.impInboundLeadtime = impInboundLeadtime;
    }

}
