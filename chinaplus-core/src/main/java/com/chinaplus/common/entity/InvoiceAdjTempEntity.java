package com.chinaplus.common.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chinaplus.common.bean.IfSsPartsEntity;
import com.chinaplus.core.base.BaseEntity;

public class InvoiceAdjTempEntity extends BaseEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    /** ssPlanId */
    private Integer ssPlanId;
    
    /** ssId */
    private Integer ssId;
    
    /** transportMode */
    private Integer transportMode;
    
    /** invoiceEtd */
    private Date invoiceEtd;
    
    /** etd */
    private Date etd;
    
    /** eta */
    private Date eta;
    
    /** ccDate */
    private Date ccDate;
    
    /** impInbPlanDate */
    private Date impInbPlanDate;
    
    /** originalVersion */
    private Integer originalVersion;
    
    /** revisionVersion */
    private Integer revisionVersion;
    
    /** nirdFlag */
    private Integer nirdFlag;
    
    /** completedFlag */
    private Integer completedFlag;
    
    /** delayAdjustmentPattern */
    private Integer delayAdjustmentPattern;
    
    /** oldSsPartsList */
    private List<TntSsPart> nirdSsPartsList;
    
    /** newSsPartsList */
    private List<TntSsPart> newSsPartsList;
    
    /** newSsPartsList */
    private List<IfSsPartsEntity> noNextEtdSsPartsList;
    
    public InvoiceAdjTempEntity() {
        nirdSsPartsList = new ArrayList<TntSsPart>();
        newSsPartsList = new ArrayList<TntSsPart>();
        noNextEtdSsPartsList = new ArrayList<IfSsPartsEntity>();
    }

    /**
     * @return the ssPlanId
     */
    public Integer getSsPlanId() {
        return ssPlanId;
    }

    /**
     * @param ssPlanId the ssPlanId to set
     */
    public void setSsPlanId(Integer ssPlanId) {
        this.ssPlanId = ssPlanId;
    }

    /**
     * @return the ssId
     */
    public Integer getSsId() {
        return ssId;
    }

    /**
     * @param ssId the ssId to set
     */
    public void setSsId(Integer ssId) {
        this.ssId = ssId;
    }

    /**
     * @return the transportMode
     */
    public Integer getTransportMode() {
        return transportMode;
    }

    /**
     * @param transportMode the transportMode to set
     */
    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * @return the invoiceEtd
     */
    public Date getInvoiceEtd() {
        return invoiceEtd;
    }

    /**
     * @param invoiceEtd the invoiceEtd to set
     */
    public void setInvoiceEtd(Date invoiceEtd) {
        this.invoiceEtd = invoiceEtd;
    }

    /**
     * @return the etd
     */
    public Date getEtd() {
        return etd;
    }

    /**
     * @param etd the etd to set
     */
    public void setEtd(Date etd) {
        this.etd = etd;
    }

    /**
     * @return the eta
     */
    public Date getEta() {
        return eta;
    }

    /**
     * @param eta the eta to set
     */
    public void setEta(Date eta) {
        this.eta = eta;
    }

    /**
     * @return the ccDate
     */
    public Date getCcDate() {
        return ccDate;
    }

    /**
     * @param ccDate the ccDate to set
     */
    public void setCcDate(Date ccDate) {
        this.ccDate = ccDate;
    }

    /**
     * @return the impInbPlanDate
     */
    public Date getImpInbPlanDate() {
        return impInbPlanDate;
    }

    /**
     * @param impInbPlanDate the impInbPlanDate to set
     */
    public void setImpInbPlanDate(Date impInbPlanDate) {
        this.impInbPlanDate = impInbPlanDate;
    }

    /**
     * @return the originalVersion
     */
    public Integer getOriginalVersion() {
        return originalVersion;
    }

    /**
     * @param originalVersion the originalVersion to set
     */
    public void setOriginalVersion(Integer originalVersion) {
        this.originalVersion = originalVersion;
    }

    /**
     * @return the revisionVersion
     */
    public Integer getRevisionVersion() {
        return revisionVersion;
    }

    /**
     * @param revisionVersion the revisionVersion to set
     */
    public void setRevisionVersion(Integer revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    /**
     * @return the nirdFlag
     */
    public Integer getNirdFlag() {
        return nirdFlag;
    }

    /**
     * @param nirdFlag the nirdFlag to set
     */
    public void setNirdFlag(Integer nirdFlag) {
        this.nirdFlag = nirdFlag;
    }

    /**
     * @return the completedFlag
     */
    public Integer getCompletedFlag() {
        return completedFlag;
    }

    /**
     * @param completedFlag the completedFlag to set
     */
    public void setCompletedFlag(Integer completedFlag) {
        this.completedFlag = completedFlag;
    }

    /**
     * @return the delayAdjustmentPattern
     */
    public Integer getDelayAdjustmentPattern() {
        return delayAdjustmentPattern;
    }

    /**
     * @param delayAdjustmentPattern the delayAdjustmentPattern to set
     */
    public void setDelayAdjustmentPattern(Integer delayAdjustmentPattern) {
        this.delayAdjustmentPattern = delayAdjustmentPattern;
    }

    /**
     * @return the nirdSsPartsList
     */
    public List<TntSsPart> getNirdSsPartsList() {
        return nirdSsPartsList;
    }

    /**
     * @param nirdSsPartsList the nirdSsPartsList to set
     */
    public void setNirdSsPartsList(List<TntSsPart> nirdSsPartsList) {
        this.nirdSsPartsList = nirdSsPartsList;
    }

    /**
     * @return the newSsPartsList
     */
    public List<TntSsPart> getNewSsPartsList() {
        return newSsPartsList;
    }

    /**
     * @param newSsPartsList the newSsPartsList to set
     */
    public void setNewSsPartsList(List<TntSsPart> newSsPartsList) {
        this.newSsPartsList = newSsPartsList;
    }

    /**
     * @return the noNextEtdSsPartsList
     */
    public List<IfSsPartsEntity> getNoNextEtdSsPartsList() {
        return noNextEtdSsPartsList;
    }

    /**
     * @param noNextEtdSsPartsList the noNextEtdSsPartsList to set
     */
    public void setNoNextEtdSsPartsList(List<IfSsPartsEntity> noNextEtdSsPartsList) {
        this.noNextEtdSsPartsList = noNextEtdSsPartsList;
    }
}
