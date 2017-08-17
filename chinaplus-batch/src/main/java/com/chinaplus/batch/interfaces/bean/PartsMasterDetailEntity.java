package com.chinaplus.batch.interfaces.bean;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPCAUS01Entity.
 */
public class PartsMasterDetailEntity extends BaseEntity {
  
    /** serialVersionUID */
    private static final long serialVersionUID = 5891553838561686745L;
    
    
    /** the expPartsId */
    private Integer expPartsId;
      
    /** the ttcPartNo */
    private String ttcPartNo;    
    /** the partsDesEnglish */
    private String  partsDesEnglish;
    /** the partsDesChinese */
    private String  partsDesChinese;        
    /** the oldTTCPartNo */
    private String oldTTCPartNo;
    /** the impOfficeCode */
    private String impOfficeCode;
    
    /** the businessPattern */
    private String businessPattern;       
    /** the ttcCustCd */
    private String ttcCustCd;
    /** the ssmskbCustCd */
    private String ssmskbCustCd;
    /** the custPartNo */
    private String custPartNo;
    /** the exportCountry */
    private String exportCountry;    
    
    /** the ttcSuppCd */
    private String ttcSuppCd;
    /** the ssmsMainRoute */
    private String ssmsMainRoute;   
    /** the ssmsVendorRoute */
    private String ssmsVendorRoute;
    /** the ssmsSuppCd */
    private String ssmsSuppCd;
    /** the suppPartNo */
    private String suppPartNo;
            
    /** the westCustCd */
    private String westCustCd;
    /** the westPartNo */
    private String westPartNo;
    /** the mailInvCustCd */
    private String mailInvCustCd;
    /** the ttcImpWHCd */
    private String ttcImpWHCd;
    /** the partType */
    private String partType;
  
    /** the carModel */
    private String carModel;  
    /** the buildOutIndicator */
    private String buildOutIndicator;
    /** the partStatus */
    private String partStatus;
    /** the discontIndicator */
    private String discontIndicator;   
    /** the createdDate */
    private Date createdDate;

    /** the createdBy */
    private String createdBy;
    /** the lastModifiedDate */
    private Date lastModifiedDate;
    /** the lastModifiedBy */
    private String lastModifiedBy;

    
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
     * Get the ttcPartNo.
     *
     * @return ttcPartNo
     */
    public String getTtcPartNo() {
        return this.ttcPartNo;
    }
    /**
     * Set the ttcPartNo.
     *
     * @param ttcPartNo ttcPartNo
     */
    public void setTtcPartNo(String ttcPartNo) {
        this.ttcPartNo = ttcPartNo;
    }
    /**
     * Get the partsDesEnglish.
     *
     * @return partsDesEnglish
     */
    public String getPartsDesEnglish() {
        return this.partsDesEnglish;
    }
    /**
     * Set the partsDesEnglish.
     *
     * @param partsDesEnglish partsDesEnglish
     */
    public void setPartsDesEnglish(String partsDesEnglish) {
        this.partsDesEnglish = partsDesEnglish;
    }
    /**
     * Get the partsDesChinese.
     *
     * @return partsDesChinese
     */
    public String getPartsDesChinese() {
        return this.partsDesChinese;
    }
    /**
     * Set the partsDesChinese.
     *
     * @param partsDesChinese partsDesChinese
     */
    public void setPartsDesChinese(String partsDesChinese) {
        this.partsDesChinese = partsDesChinese;
    }
    /**
     * Get the oldTTCPartNo.
     *
     * @return oldTTCPartNo
     */
    public String getOldTTCPartNo() {
        return this.oldTTCPartNo;
    }
    /**
     * Set the oldTTCPartNo.
     *
     * @param oldTTCPartNo oldTTCPartNo
     */
    public void setOldTTCPartNo(String oldTTCPartNo) {
        this.oldTTCPartNo = oldTTCPartNo;
    }
    /**
     * Get the impOfficeCode.
     *
     * @return impOfficeCode
     */
    public String getImpOfficeCode() {
        return this.impOfficeCode;
    }
    /**
     * Set the impOfficeCode.
     *
     * @param impOfficeCode impOfficeCode
     */
    public void setImpOfficeCode(String impOfficeCode) {
        this.impOfficeCode = impOfficeCode;
    }
    /**
     * Get the businessPattern.
     *
     * @return businessPattern
     */
    public String getBusinessPattern() {
        return this.businessPattern;
    }
    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     */
    public void setBusinessPattern(String businessPattern) {
        this.businessPattern = businessPattern;
    }
    /**
     * Get the ttcCustCd.
     *
     * @return ttcCustCd
     */
    public String getTtcCustCd() {
        return this.ttcCustCd;
    }
    /**
     * Set the ttcCustCd.
     *
     * @param ttcCustCd ttcCustCd
     */
    public void setTtcCustCd(String ttcCustCd) {
        this.ttcCustCd = ttcCustCd;
    }
    /**
     * Get the ssmskbCustCd.
     *
     * @return ssmskbCustCd
     */
    public String getSsmskbCustCd() {
        return this.ssmskbCustCd;
    }
    /**
     * Set the ssmskbCustCd.
     *
     * @param ssmskbCustCd ssmskbCustCd
     */
    public void setSsmskbCustCd(String ssmskbCustCd) {
        this.ssmskbCustCd = ssmskbCustCd;
    }
    /**
     * Get the custPartNo.
     *
     * @return custPartNo
     */
    public String getCustPartNo() {
        return this.custPartNo;
    }
    /**
     * Set the custPartNo.
     *
     * @param custPartNo custPartNo
     */
    public void setCustPartNo(String custPartNo) {
        this.custPartNo = custPartNo;
    }
    /**
     * Get the exportCountry.
     *
     * @return exportCountry
     */
    public String getExportCountry() {
        return this.exportCountry;
    }
    /**
     * Set the exportCountry.
     *
     * @param exportCountry exportCountry
     */
    public void setExportCountry(String exportCountry) {
        this.exportCountry = exportCountry;
    }
    /**
     * Get the ttcSuppCd.
     *
     * @return ttcSuppCd
     */
    public String getTtcSuppCd() {
        return this.ttcSuppCd;
    }
    /**
     * Set the ttcSuppCd.
     *
     * @param ttcSuppCd ttcSuppCd
     */
    public void setTtcSuppCd(String ttcSuppCd) {
        this.ttcSuppCd = ttcSuppCd;
    }
    /**
     * Get the ssmsMainRoute.
     *
     * @return ssmsMainRoute
     */
    public String getSsmsMainRoute() {
        return this.ssmsMainRoute;
    }
    /**
     * Set the ssmsMainRoute.
     *
     * @param ssmsMainRoute ssmsMainRoute
     */
    public void setSsmsMainRoute(String ssmsMainRoute) {
        this.ssmsMainRoute = ssmsMainRoute;
    }
    /**
     * Get the ssmsVendorRoute.
     *
     * @return ssmsVendorRoute
     */
    public String getSsmsVendorRoute() {
        return this.ssmsVendorRoute;
    }
    /**
     * Set the ssmsVendorRoute.
     *
     * @param ssmsVendorRoute ssmsVendorRoute
     */
    public void setSsmsVendorRoute(String ssmsVendorRoute) {
        this.ssmsVendorRoute = ssmsVendorRoute;
    }
    /**
     * Get the ssmsSuppCd.
     *
     * @return ssmsSuppCd
     */
    public String getSsmsSuppCd() {
        return this.ssmsSuppCd;
    }
    /**
     * Set the ssmsSuppCd.
     *
     * @param ssmsSuppCd ssmsSuppCd
     */
    public void setSsmsSuppCd(String ssmsSuppCd) {
        this.ssmsSuppCd = ssmsSuppCd;
    }
    /**
     * Get the suppPartNo.
     *
     * @return suppPartNo
     */
    public String getSuppPartNo() {
        return this.suppPartNo;
    }
    /**
     * Set the suppPartNo.
     *
     * @param suppPartNo suppPartNo
     */
    public void setSuppPartNo(String suppPartNo) {
        this.suppPartNo = suppPartNo;
    }
    /**
     * Get the westCustCd.
     *
     * @return westCustCd
     */
    public String getWestCustCd() {
        return this.westCustCd;
    }
    /**
     * Set the westCustCd.
     *
     * @param westCustCd westCustCd
     */
    public void setWestCustCd(String westCustCd) {
        this.westCustCd = westCustCd;
    }
    /**
     * Get the westPartNo.
     *
     * @return westPartNo
     */
    public String getWestPartNo() {
        return this.westPartNo;
    }
    /**
     * Set the westPartNo.
     *
     * @param westPartNo westPartNo
     */
    public void setWestPartNo(String westPartNo) {
        this.westPartNo = westPartNo;
    }
    /**
     * Get the mailInvCustCd.
     *
     * @return mailInvCustCd
     */
    public String getMailInvCustCd() {
        return this.mailInvCustCd;
    }
    /**
     * Set the mailInvCustCd.
     *
     * @param mailInvCustCd mailInvCustCd
     */
    public void setMailInvCustCd(String mailInvCustCd) {
        this.mailInvCustCd = mailInvCustCd;
    }
    /**
     * Get the ttcImpWHCd.
     *
     * @return ttcImpWHCd
     */
    public String getTtcImpWHCd() {
        return this.ttcImpWHCd;
    }
    /**
     * Set the ttcImpWHCd.
     *
     * @param ttcImpWHCd ttcImpWHCd
     */
    public void setTtcImpWHCd(String ttcImpWHCd) {
        this.ttcImpWHCd = ttcImpWHCd;
    }
    /**
     * Get the partType.
     *
     * @return partType
     */
    public String getPartType() {
        return this.partType;
    }
    /**
     * Set the partType.
     *
     * @param partType partType
     */
    public void setPartType(String partType) {
        this.partType = partType;
    }
    /**
     * Get the carModel.
     *
     * @return carModel
     */
    public String getCarModel() {
        return this.carModel;
    }
    /**
     * Set the carModel.
     *
     * @param carModel carModel
     */
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
    /**
     * Get the buildOutIndicator.
     *
     * @return buildOutIndicator
     */
    public String getBuildOutIndicator() {
        return this.buildOutIndicator;
    }
    /**
     * Set the buildOutIndicator.
     *
     * @param buildOutIndicator buildOutIndicator
     */
    public void setBuildOutIndicator(String buildOutIndicator) {
        this.buildOutIndicator = buildOutIndicator;
    }
    /**
     * Get the partStatus.
     *
     * @return partStatus
     */
    public String getPartStatus() {
        return this.partStatus;
    }
    /**
     * Set the partStatus.
     *
     * @param partStatus partStatus
     */
    public void setPartStatus(String partStatus) {
        this.partStatus = partStatus;
    }
    /**
     * Get the discontIndicator.
     *
     * @return discontIndicator
     */
    public String getDiscontIndicator() {
        return this.discontIndicator;
    }
    /**
     * Set the discontIndicator.
     *
     * @param discontIndicator discontIndicator
     */
    public void setDiscontIndicator(String discontIndicator) {
        this.discontIndicator = discontIndicator;
    }
    /**
     * Get the createdDate.
     *
     * @return createdDate
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getCreatedDate() {
        return this.createdDate;
    }
    /**
     * Set the createdDate.
     *
     * @param createdDate createdDate
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    /**
     * Get the createdBy.
     *
     * @return createdBy
     */
    public String getCreatedBy() {
        return this.createdBy;
    }
    /**
     * Set the createdBy.
     *
     * @param createdBy createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * Get the lastModifiedDate.
     *
     * @return lastModifiedDate
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getLastModifiedDate() {
        return this.lastModifiedDate;
    }
    /**
     * Set the lastModifiedDate.
     *
     * @param lastModifiedDate lastModifiedDate
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    /**
     * Get the lastModifiedBy.
     *
     * @return lastModifiedBy
     */
    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }
    /**
     * Set the lastModifiedBy.
     *
     * @param lastModifiedBy lastModifiedBy
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
