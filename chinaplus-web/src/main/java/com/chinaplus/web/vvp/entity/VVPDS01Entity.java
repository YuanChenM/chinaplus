/**
 * VVPDS01Entity.java
 * 
 * @screen VVPDS01
 * @author ren_yi
 */
package com.chinaplus.web.vvp.entity;

import java.sql.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * Single List Screen Entity.
 */
public class VVPDS01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** exportOfficeName */
    private Integer supplierId;

    /** exportOfficeName */
    private Integer exportOffice;
    
    /** exportOfficeName */
    private String exportOfficeCode;

    /** CREATED_DATE */
    private Date createdDate;

    /** createdBy */
    private String createdBy;

    /** LAST_UPDATED_DATE */
    private Date lastUpdatedDate;

    /** STATUSName */
    private Integer status;

    /** fullCompanyName */
    private String fullCompanyName;

    /** localAddress */
    private String localAddress;

    /** capital */
    private String capital;

    /** businessActivity */
    private String businessActivity;

    /** shareholder */
    private String shareholder;

    /** companyWebsite */
    private String companyWebsite;

    /** noOfEmployee */
    private String noOfEmployee;

    /** mainCustomer */
    private String mainCustomer;

    /** headquarterId */
    private Integer headquarter;

    /** headquarterCode */
    private String headquarterCode;

    /** endUserOem */
    private String endUserOem;

    /** officeBranchId */
    private Integer officeBranch;

    /** officeBranchId */
    private String officeBranchCode;

    /** productMateialId */
    private Integer productMateial;

    /** sectionofthecarId */
    private Integer sectionOfTheCar;

    /** productionProcessId */
    private Integer productionProcess;

    /** remarks1 */
    private String remarks1;

    /** businessWithLocalTtcId */
    private Integer businessWithLocalTTC;

    /** NdaAgreementId */
    private Integer ndaAgreement;

    /** suppilerTarget */
    private String suppilerTarget;

    /** appealingPoint */
    private String appealingPoint;

    /** overallEvaluationId */
    private Integer overallEvaluation;
    
    /** riskConcernIfAny */
    private String riskConcern;

    /** remarks2 */
    private String remarks2;

    /** supportingDocument1 */
    private String supportingDocument1;

    /** supportingDocument2 */
    private String supportingDocument2;

    /** supportingDocument3 */
    private String supportingDocument3;

    /** supportingDocument4 */
    private String supportingDocument4;

    /** supportingDocument4 */
    private String supportingDocument5;

    /**
     * Get the exportOffice.
     *
     * @return exportOffice
     */
    public Integer getExportOffice() {
        return this.exportOffice;
    }

    /**
     * Set the exportOffice.
     *
     * @param exportOffice exportOffice
     */
    public void setExportOffice(Integer exportOffice) {
        this.exportOffice = exportOffice;
        
    }

    /**
     * Get the createdDate.
     *
     * @return createdDate
     */
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
     * Get the lastUpdatedDate.
     *
     * @return lastUpdatedDate
     */
    public Date getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }

    /**
     * Set the lastUpdatedDate.
     *
     * @param lastUpdatedDate lastUpdatedDate
     */
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
        
    }

    /**
     * Get the status.
     *
     * @return status
     */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * Set the status.
     *
     * @param status status
     */
    public void setStatus(Integer status) {
        this.status = status;
        
    }

    /**
     * Get the fullCompanyName.
     *
     * @return fullCompanyName
     */
    public String getFullCompanyName() {
        return this.fullCompanyName;
    }

    /**
     * Set the fullCompanyName.
     *
     * @param fullCompanyName fullCompanyName
     */
    public void setFullCompanyName(String fullCompanyName) {
        this.fullCompanyName = fullCompanyName;
        
    }

    /**
     * Get the localAddress.
     *
     * @return localAddress
     */
    public String getLocalAddress() {
        return this.localAddress;
    }

    /**
     * Set the localAddress.
     *
     * @param localAddress localAddress
     */
    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
        
    }

    /**
     * Get the capital.
     *
     * @return capital
     */
    public String getCapital() {
        return this.capital;
    }

    /**
     * Set the capital.
     *
     * @param capital capital
     */
    public void setCapital(String capital) {
        this.capital = capital;
        
    }

    /**
     * Get the businessActivity.
     *
     * @return businessActivity
     */
    public String getBusinessActivity() {
        return this.businessActivity;
    }

    /**
     * Set the businessActivity.
     *
     * @param businessActivity businessActivity
     */
    public void setBusinessActivity(String businessActivity) {
        this.businessActivity = businessActivity;
        
    }

    /**
     * Get the shareholder.
     *
     * @return shareholder
     */
    public String getShareholder() {
        return this.shareholder;
    }

    /**
     * Set the shareholder.
     *
     * @param shareholder shareholder
     */
    public void setShareholder(String shareholder) {
        this.shareholder = shareholder;
        
    }

    /**
     * Get the companyWebsite.
     *
     * @return companyWebsite
     */
    public String getCompanyWebsite() {
        return this.companyWebsite;
    }

    /**
     * Set the companyWebsite.
     *
     * @param companyWebsite companyWebsite
     */
    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
        
    }

    /**
     * Get the noOfEmployee.
     *
     * @return noOfEmployee
     */
    public String getNoOfEmployee() {
        return this.noOfEmployee;
    }

    /**
     * Set the noOfEmployee.
     *
     * @param noOfEmployee noOfEmployee
     */
    public void setNoOfEmployee(String noOfEmployee) {
        this.noOfEmployee = noOfEmployee;
        
    }

    /**
     * Get the mainCustomer.
     *
     * @return mainCustomer
     */
    public String getMainCustomer() {
        return this.mainCustomer;
    }

    /**
     * Set the mainCustomer.
     *
     * @param mainCustomer mainCustomer
     */
    public void setMainCustomer(String mainCustomer) {
        this.mainCustomer = mainCustomer;
        
    }

    /**
     * Get the headquarter.
     *
     * @return headquarter
     */
    public Integer getHeadquarter() {
        return this.headquarter;
    }

    /**
     * Set the headquarter.
     *
     * @param headquarter headquarter
     */
    public void setHeadquarter(Integer headquarter) {
        this.headquarter = headquarter;
        
    }

    /**
     * Get the endUserOem.
     *
     * @return endUserOem
     */
    public String getEndUserOem() {
        return this.endUserOem;
    }

    /**
     * Set the endUserOem.
     *
     * @param endUserOem endUserOem
     */
    public void setEndUserOem(String endUserOem) {
        this.endUserOem = endUserOem;
        
    }

    /**
     * Get the officeBranch.
     *
     * @return officeBranch
     */
    public Integer getOfficeBranch() {
        return this.officeBranch;
    }

    /**
     * Set the officeBranch.
     *
     * @param officeBranch officeBranch
     */
    public void setOfficeBranch(Integer officeBranch) {
        this.officeBranch = officeBranch;
        
    }

    /**
     * Get the productMateial.
     *
     * @return productMateial
     */
    public Integer getProductMateial() {
        return this.productMateial;
    }

    /**
     * Set the productMateial.
     *
     * @param productMateial productMateial
     */
    public void setProductMateial(Integer productMateial) {
        this.productMateial = productMateial;
        
    }

    /**
     * Get the sectionOfTheCar.
     *
     * @return sectionOfTheCar
     */
    public Integer getSectionOfTheCar() {
        return this.sectionOfTheCar;
    }

    /**
     * Set the sectionOfTheCar.
     *
     * @param sectionOfTheCar sectionOfTheCar
     */
    public void setSectionOfTheCar(Integer sectionOfTheCar) {
        this.sectionOfTheCar = sectionOfTheCar;
        
    }

    /**
     * Get the productionProcess.
     *
     * @return productionProcess
     */
    public Integer getProductionProcess() {
        return this.productionProcess;
    }

    /**
     * Set the productionProcess.
     *
     * @param productionProcess productionProcess
     */
    public void setProductionProcess(Integer productionProcess) {
        this.productionProcess = productionProcess;
        
    }

    /**
     * Get the remarks1.
     *
     * @return remarks1
     */
    public String getRemarks1() {
        return this.remarks1;
    }

    /**
     * Set the remarks1.
     *
     * @param remarks1 remarks1
     */
    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
        
    }

    /**
     * Get the businessWithLocalTTC.
     *
     * @return businessWithLocalTTC
     */
    public Integer getBusinessWithLocalTTC() {
        return this.businessWithLocalTTC;
    }

    /**
     * Set the businessWithLocalTTC.
     *
     * @param businessWithLocalTTC businessWithLocalTTC
     */
    public void setBusinessWithLocalTTC(Integer businessWithLocalTTC) {
        this.businessWithLocalTTC = businessWithLocalTTC;
        
    }

    /**
     * Get the ndaAgreement.
     *
     * @return ndaAgreement
     */
    public Integer getNdaAgreement() {
        return this.ndaAgreement;
    }

    /**
     * Set the ndaAgreement.
     *
     * @param ndaAgreement ndaAgreement
     */
    public void setNdaAgreement(Integer ndaAgreement) {
        this.ndaAgreement = ndaAgreement;
        
    }

    /**
     * Get the suppilerTarget.
     *
     * @return suppilerTarget
     */
    public String getSuppilerTarget() {
        return this.suppilerTarget;
    }

    /**
     * Set the suppilerTarget.
     *
     * @param suppilerTarget suppilerTarget
     */
    public void setSuppilerTarget(String suppilerTarget) {
        this.suppilerTarget = suppilerTarget;
        
    }

    /**
     * Get the appealingPoint.
     *
     * @return appealingPoint
     */
    public String getAppealingPoint() {
        return this.appealingPoint;
    }

    /**
     * Set the appealingPoint.
     *
     * @param appealingPoint appealingPoint
     */
    public void setAppealingPoint(String appealingPoint) {
        this.appealingPoint = appealingPoint;
        
    }

    /**
     * Get the overallEvaluation.
     *
     * @return overallEvaluation
     */
    public Integer getOverallEvaluation() {
        return this.overallEvaluation;
    }

    /**
     * Set the overallEvaluation.
     *
     * @param overallEvaluation overallEvaluation
     */
    public void setOverallEvaluation(Integer overallEvaluation) {
        this.overallEvaluation = overallEvaluation;
        
    }

    /**
     * Get the riskConcern.
     *
     * @return riskConcern
     */
    public String getRiskConcern() {
        return this.riskConcern;
    }

    /**
     * Set the riskConcern.
     *
     * @param riskConcern riskConcern
     */
    public void setRiskConcern(String riskConcern) {
        this.riskConcern = riskConcern;
        
    }

    /**
     * Get the remarks2.
     *
     * @return remarks2
     */
    public String getRemarks2() {
        return this.remarks2;
    }

    /**
     * Set the remarks2.
     *
     * @param remarks2 remarks2
     */
    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
        
    }

    /**
     * Get the supportingDocument1.
     *
     * @return supportingDocument1
     */
    public String getSupportingDocument1() {
        return this.supportingDocument1;
    }

    /**
     * Set the supportingDocument1.
     *
     * @param supportingDocument1 supportingDocument1
     */
    public void setSupportingDocument1(String supportingDocument1) {
        this.supportingDocument1 = supportingDocument1;
        
    }

    /**
     * Get the supportingDocument2.
     *
     * @return supportingDocument2
     */
    public String getSupportingDocument2() {
        return this.supportingDocument2;
    }

    /**
     * Set the supportingDocument2.
     *
     * @param supportingDocument2 supportingDocument2
     */
    public void setSupportingDocument2(String supportingDocument2) {
        this.supportingDocument2 = supportingDocument2;
        
    }

    /**
     * Get the supportingDocument3.
     *
     * @return supportingDocument3
     */
    public String getSupportingDocument3() {
        return this.supportingDocument3;
    }

    /**
     * Set the supportingDocument3.
     *
     * @param supportingDocument3 supportingDocument3
     */
    public void setSupportingDocument3(String supportingDocument3) {
        this.supportingDocument3 = supportingDocument3;
        
    }

    /**
     * Get the supportingDocument4.
     *
     * @return supportingDocument4
     */
    public String getSupportingDocument4() {
        return this.supportingDocument4;
    }

    /**
     * Set the supportingDocument4.
     *
     * @param supportingDocument4 supportingDocument4
     */
    public void setSupportingDocument4(String supportingDocument4) {
        this.supportingDocument4 = supportingDocument4;
        
    }

    /**
     * Get the supportingDocument5.
     *
     * @return supportingDocument5
     */
    public String getSupportingDocument5() {
        return this.supportingDocument5;
    }

    /**
     * Set the supportingDocument5.
     *
     * @param supportingDocument5 supportingDocument5
     */
    public void setSupportingDocument5(String supportingDocument5) {
        this.supportingDocument5 = supportingDocument5;
        
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
     * Get the headquarterCode.
     *
     * @return headquarterCode
     */
    public String getHeadquarterCode() {
        return this.headquarterCode;
    }

    /**
     * Set the headquarterCode.
     *
     * @param headquarterCode headquarterCode
     */
    public void setHeadquarterCode(String headquarterCode) {
        this.headquarterCode = headquarterCode;
        
    }

    /**
     * Get the officeBranchCode.
     *
     * @return officeBranchCode
     */
    public String getOfficeBranchCode() {
        return this.officeBranchCode;
    }

    /**
     * Set the officeBranchCode.
     *
     * @param officeBranchCode officeBranchCode
     */
    public void setOfficeBranchCode(String officeBranchCode) {
        this.officeBranchCode = officeBranchCode;
        
    }

    /**
     * Get the exportOfficeCode.
     *
     * @return exportOfficeCode
     */
    public String getExportOfficeCode() {
        return this.exportOfficeCode;
    }

    /**
     * Set the exportOfficeCode.
     *
     * @param exportOfficeCode exportOfficeCode
     */
    public void setExportOfficeCode(String exportOfficeCode) {
        this.exportOfficeCode = exportOfficeCode;
        
    }

}
