/**
 * VVPLS01Entity.java
 * 
 * @screen VVPLS01
 * @author ren_yi
 */
package com.chinaplus.web.vvp.entity;

import java.sql.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * Single List Screen Entity.
 */
public class VVPSDF01Entity extends BaseEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/** supplier id */
	private Integer supplierId;

	/** exportOffice */
	private Integer exportOffice;

	/** fullCompanyName */
	private String fullCompanyName;

    /** local Address str */
    private String localAddress;

    /** CAPITAL str */
    private String capital;
    
    /** SHARE_HOLDER */
    private String shareHolder;
    
    /** NO_OF_EMPLOYEE */
    private String employeeNo;

    /** headquarter */
    private Integer headquarter;

	/** officeBranch */
	private Integer officeBranch;
	
	/** BUSINESS_ACTIVITY */
	private String businessActivity;
	
	/** COMPANY_WEBSITE */
	private String companyWebsite;
	
	/** MAIN_CUSTOMER */
	private String mainCustomer;
	
	/** END_USER_OEM */
	private String endUserOem;

	/** productMateial */
	private Integer productMateial;

	/** sectionofthecar */
	private Integer sectionofthecar;
	
	/** productionProcess */
	private Integer productionProcess;
	
	/** REMARKS1 */
	private String remask1;
    
    /** BUSINESS_WITH_LOCAL_TTC */
    private Integer withTTC;
    
    /** NDA_AGREEMENT */
    private Integer ndaAgreement;
    
    /** supplier target */
    private String supplierTarget;
    
    /** APPEALING_Point */
    private String appealingPoint;

	/** overallEvaluation */
	private Integer overallEvaluation;
    
    /** RISK_CONCERN_IF_ANY */
    private String riskConcern;
    
    /** REMARKS2 */
    private String remask2;
	
	/** CREATED_DATE */
	private Date createdDate;
	
	/** CREATED_BY */
	private String createBy;

	/** LAST_UPDATED_DATE */
	private Date lastUpdatedDate;
	
	/** createdDateForDisplay */
	private String createdDateForDisplay;

	/** lastUpdatedDateForDisplay */
	private String lastUpdatedDateForDisplay;

	/** STATUS */
	private Integer status;
	
	/** STATUSName */
	private String statusName;

	/**
	 * @return the supplierId
	 */
	public Integer getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId the supplierId to set
	 */
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the exportOffice
	 */
	public Integer getExportOffice() {
		return exportOffice;
	}

	/**
	 * @param exportOffice the exportOffice to set
	 */
	public void setExportOffice(Integer exportOffice) {
		this.exportOffice = exportOffice;
	}

	/**
	 * @return the fullCompanyName
	 */
	public String getFullCompanyName() {
		return fullCompanyName;
	}

	/**
	 * @param fullCompanyName the fullCompanyName to set
	 */
	public void setFullCompanyName(String fullCompanyName) {
		this.fullCompanyName = fullCompanyName;
	}

	/**
	 * @return the localAddress
	 */
	public String getLocalAddress() {
		return localAddress;
	}

	/**
	 * @param localAddress the localAddress to set
	 */
	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	/**
	 * @return the capital
	 */
	public String getCapital() {
		return capital;
	}

	/**
	 * @param capital the capital to set
	 */
	public void setCapital(String capital) {
		this.capital = capital;
	}

	/**
	 * @return the shareHolder
	 */
	public String getShareHolder() {
		return shareHolder;
	}

	/**
	 * @param shareHolder the shareHolder to set
	 */
	public void setShareHolder(String shareHolder) {
		this.shareHolder = shareHolder;
	}

	/**
	 * @return the employeeNo
	 */
	public String getEmployeeNo() {
		return employeeNo;
	}

	/**
	 * @param employeeNo the employeeNo to set
	 */
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	/**
	 * @return the headquarter
	 */
	public Integer getHeadquarter() {
		return headquarter;
	}

	/**
	 * @param headquarter the headquarter to set
	 */
	public void setHeadquarter(Integer headquarter) {
		this.headquarter = headquarter;
	}

	/**
	 * @return the officeBranch
	 */
	public Integer getOfficeBranch() {
		return officeBranch;
	}

	/**
	 * @param officeBranch the officeBranch to set
	 */
	public void setOfficeBranch(Integer officeBranch) {
		this.officeBranch = officeBranch;
	}

	/**
	 * @return the businessActivity
	 */
	public String getBusinessActivity() {
		return businessActivity;
	}

	/**
	 * @param businessActivity the businessActivity to set
	 */
	public void setBusinessActivity(String businessActivity) {
		this.businessActivity = businessActivity;
	}

	/**
	 * @return the companyWebsite
	 */
	public String getCompanyWebsite() {
		return companyWebsite;
	}

	/**
	 * @param companyWebsite the companyWebsite to set
	 */
	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	/**
	 * @return the mainCustomer
	 */
	public String getMainCustomer() {
		return mainCustomer;
	}

	/**
	 * @param mainCustomer the mainCustomer to set
	 */
	public void setMainCustomer(String mainCustomer) {
		this.mainCustomer = mainCustomer;
	}

	/**
	 * @return the endUserOem
	 */
	public String getEndUserOem() {
		return endUserOem;
	}

	/**
	 * @param endUserOem the endUserOem to set
	 */
	public void setEndUserOem(String endUserOem) {
		this.endUserOem = endUserOem;
	}

	/**
	 * @return the productMateial
	 */
	public Integer getProductMateial() {
		return productMateial;
	}

	/**
	 * @param productMateial the productMateial to set
	 */
	public void setProductMateial(Integer productMateial) {
		this.productMateial = productMateial;
	}

	/**
	 * @return the sectionofthecar
	 */
	public Integer getSectionofthecar() {
		return sectionofthecar;
	}

	/**
	 * @param sectionofthecar the sectionofthecar to set
	 */
	public void setSectionofthecar(Integer sectionofthecar) {
		this.sectionofthecar = sectionofthecar;
	}

	/**
	 * @return the productionProcess
	 */
	public Integer getProductionProcess() {
		return productionProcess;
	}

	/**
	 * @param productionProcess the productionProcess to set
	 */
	public void setProductionProcess(Integer productionProcess) {
		this.productionProcess = productionProcess;
	}

	/**
	 * @return the remask1
	 */
	public String getRemask1() {
		return remask1;
	}

	/**
	 * @param remask1 the remask1 to set
	 */
	public void setRemask1(String remask1) {
		this.remask1 = remask1;
	}

	/**
	 * @return the withTTC
	 */
	public Integer getWithTTC() {
		return withTTC;
	}

	/**
	 * @param withTTC the withTTC to set
	 */
	public void setWithTTC(Integer withTTC) {
		this.withTTC = withTTC;
	}

	/**
	 * @return the ndaAgreement
	 */
	public Integer getNdaAgreement() {
		return ndaAgreement;
	}

	/**
	 * @param ndaAgreement the ndaAgreement to set
	 */
	public void setNdaAgreement(Integer ndaAgreement) {
		this.ndaAgreement = ndaAgreement;
	}

	/**
	 * @return the supplierTarget
	 */
	public String getSupplierTarget() {
		return supplierTarget;
	}

	/**
	 * @param supplierTarget the supplierTarget to set
	 */
	public void setSupplierTarget(String supplierTarget) {
		this.supplierTarget = supplierTarget;
	}

	/**
	 * @return the appealingPoint
	 */
	public String getAppealingPoint() {
		return appealingPoint;
	}

	/**
	 * @param appealingPoint the appealingPoint to set
	 */
	public void setAppealingPoint(String appealingPoint) {
		this.appealingPoint = appealingPoint;
	}

	/**
	 * @return the overallEvaluation
	 */
	public Integer getOverallEvaluation() {
		return overallEvaluation;
	}

	/**
	 * @param overallEvaluation the overallEvaluation to set
	 */
	public void setOverallEvaluation(Integer overallEvaluation) {
		this.overallEvaluation = overallEvaluation;
	}

	/**
	 * @return the riskConcern
	 */
	public String getRiskConcern() {
		return riskConcern;
	}

	/**
	 * @param riskConcern the riskConcern to set
	 */
	public void setRiskConcern(String riskConcern) {
		this.riskConcern = riskConcern;
	}

	/**
	 * @return the remask2
	 */
	public String getRemask2() {
		return remask2;
	}

	/**
	 * @param remask2 the remask2 to set
	 */
	public void setRemask2(String remask2) {
		this.remask2 = remask2;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return the lastUpdatedDate
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	/**
	 * @param lastUpdatedDate the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	/**
	 * @return the createdDateForDisplay
	 */
	public String getCreatedDateForDisplay() {
		return createdDateForDisplay;
	}

	/**
	 * @param createdDateForDisplay the createdDateForDisplay to set
	 */
	public void setCreatedDateForDisplay(String createdDateForDisplay) {
		this.createdDateForDisplay = createdDateForDisplay;
	}

	/**
	 * @return the lastUpdatedDateForDisplay
	 */
	public String getLastUpdatedDateForDisplay() {
		return lastUpdatedDateForDisplay;
	}

	/**
	 * @param lastUpdatedDateForDisplay the lastUpdatedDateForDisplay to set
	 */
	public void setLastUpdatedDateForDisplay(String lastUpdatedDateForDisplay) {
		this.lastUpdatedDateForDisplay = lastUpdatedDateForDisplay;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the statusName
	 */
	public String getStatusName() {
		return statusName;
	}

	/**
	 * @param statusName the statusName to set
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
