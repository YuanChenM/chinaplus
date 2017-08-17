package com.chinaplus.batch.migration.bean;

import java.io.Serializable;

import com.chinaplus.common.entity.TntInvoiceContainer;

/**
 * The persistent class for the TNT_IF_IMP_IP database table.
 * 
 */
public class TntInvoiceContainerEx extends TntInvoiceContainer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String ifCcDate;
    
    private String ifDevanDate;

    /**
     * Get the ifCcDate.
     *
     * @return ifCcDate
     */
    public String getIfCcDate() {
        return this.ifCcDate;
    }

    /**
     * Set the ifCcDate.
     *
     * @param ifCcDate ifCcDate
     */
    public void setIfCcDate(String ifCcDate) {
        this.ifCcDate = ifCcDate;
        
    }

    /**
     * Get the ifDevanDate.
     *
     * @return ifDevanDate
     */
    public String getIfDevanDate() {
        return this.ifDevanDate;
    }

    /**
     * Set the ifDevanDate.
     *
     * @param ifDevanDate ifDevanDate
     */
    public void setIfDevanDate(String ifDevanDate) {
        this.ifDevanDate = ifDevanDate;
        
    }
    
}