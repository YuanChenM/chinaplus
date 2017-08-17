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
public class VVPLS01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** supplierId */
    private Integer supplierId;

    /** exportOfficeCode */
    private Integer exportOfficeCode;

    /** exportOfficeName */
    private String exportOfficeName;

    /** fullCompanyName */
    private String fullCompanyName;

    /** headquarterCode */
    private Integer headquarterCode;

    /** headquarterName */
    private String headquarterName;

    /** officeBranchCode */
    private Integer officeBranchCode;

    /** officeBranchName */
    private String officeBranchName;

    /** productMateialCode */
    private Integer productMateialCode;

    /** productMateialName */
    private String productMateialName;

    /** sectionofthecarCode */
    private Integer sectionofthecarCode;

    /** sectionofthecarName */
    private String sectionofthecarName;

    /** productionProcessCode */
    private Integer productionProcessCode;

    /** productionProcessName */
    private String productionProcessName;

    /** overallEvaluationCode */
    private Integer overallEvaluationCode;

    /** overallEvaluationName */
    private String overallEvaluationName;

    /** CREATED_DATE */
    private Date createdDate;

    /** LAST_UPDATED_DATE */
    private Date lastUpdatedDate;

    /** createdDateForDisplay */
    private String createdDateForDisplay;

    /** lastUpdatedDateForDisplay */
    private String lastUpdatedDateForDisplay;

    /** STATUS */
    private Integer statusCode;

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

	public Integer getExportOfficeCode() {
        return exportOfficeCode;
    }

    public void setExportOfficeCode(Integer exportOfficeCode) {
        this.exportOfficeCode = exportOfficeCode;
    }

    public String getExportOfficeName() {
        return exportOfficeName;
    }

    public void setExportOfficeName(String exportOfficeName) {
        this.exportOfficeName = exportOfficeName;
    }

    public String getFullCompanyName() {
        return fullCompanyName;
    }

    public void setFullCompanyName(String fullCompanyName) {
        this.fullCompanyName = fullCompanyName;
    }

    public Integer getHeadquarterCode() {
        return headquarterCode;
    }

    public void setHeadquarterCode(Integer headquarterCode) {
        this.headquarterCode = headquarterCode;
    }

    public String getHeadquarterName() {
        return headquarterName;
    }

    public void setHeadquarterName(String headquarterName) {
        this.headquarterName = headquarterName;
    }

    public Integer getOfficeBranchCode() {
        return officeBranchCode;
    }

    public void setOfficeBranchCode(Integer officeBranchCode) {
        this.officeBranchCode = officeBranchCode;
    }

    public String getOfficeBranchName() {
        return officeBranchName;
    }

    public void setOfficeBranchName(String officeBranchName) {
        this.officeBranchName = officeBranchName;
    }

    public Integer getProductMateialCode() {
        return productMateialCode;
    }

    public void setProductMateialCode(Integer productMateialCode) {
        this.productMateialCode = productMateialCode;
    }

    public String getProductMateialName() {
        return productMateialName;
    }

    public void setProductMateialName(String productMateialName) {
        this.productMateialName = productMateialName;
    }

    public Integer getSectionofthecarCode() {
        return sectionofthecarCode;
    }

    public void setSectionofthecarCode(Integer sectionofthecarCode) {
        this.sectionofthecarCode = sectionofthecarCode;
    }

    public String getSectionofthecarName() {
        return sectionofthecarName;
    }

    public void setSectionofthecarName(String sectionofthecarName) {
        this.sectionofthecarName = sectionofthecarName;
    }

    public Integer getProductionProcessCode() {
        return productionProcessCode;
    }

    public void setProductionProcessCode(Integer productionProcessCode) {
        this.productionProcessCode = productionProcessCode;
    }

    public String getProductionProcessName() {
        return productionProcessName;
    }

    public void setProductionProcessName(String productionProcessName) {
        this.productionProcessName = productionProcessName;
    }

    public Integer getOverallEvaluationCode() {
        return overallEvaluationCode;
    }

    public void setOverallEvaluationCode(Integer overallEvaluationCode) {
        this.overallEvaluationCode = overallEvaluationCode;
    }

    public String getOverallEvaluationName() {
        return overallEvaluationName;
    }

    public void setOverallEvaluationName(String overallEvaluationName) {
        this.overallEvaluationName = overallEvaluationName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getCreatedDateForDisplay() {
        return createdDateForDisplay;
    }

    public void setCreatedDateForDisplay(String createdDateForDisplay) {
        this.createdDateForDisplay = createdDateForDisplay;
    }

    public String getLastUpdatedDateForDisplay() {
        return lastUpdatedDateForDisplay;
    }

    public void setLastUpdatedDateForDisplay(String lastUpdatedDateForDisplay) {
        this.lastUpdatedDateForDisplay = lastUpdatedDateForDisplay;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
