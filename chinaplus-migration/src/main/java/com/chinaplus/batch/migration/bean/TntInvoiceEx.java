package com.chinaplus.batch.migration.bean;

import java.io.Serializable;
import java.util.List;

import com.chinaplus.common.entity.TntInvoice;

/**
 * The persistent class for the TNT_IF_IMP_IP database table.
 * 
 */
public class TntInvoiceEx extends TntInvoice implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String ifCcDate;

    private List<String> invNoList;

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
     * Get the invNoList.
     *
     * @return invNoList
     */
    public List<String> getInvNoList() {
        return this.invNoList;
    }

    /**
     * Set the invNoList.
     *
     * @param invNoList invNoList
     */
    public void setInvNoList(List<String> invNoList) {
        this.invNoList = invNoList;
        
    }
    
}