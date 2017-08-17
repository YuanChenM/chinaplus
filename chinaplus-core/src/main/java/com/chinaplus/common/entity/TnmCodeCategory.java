package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the TNM_CODE_CATEGORY database table.
 * 
 */
@Entity(name="TNM_CODE_CATEGORY")
public class TnmCodeCategory extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_CODE_CATEGORY_CODECATEGORYID_GENERATOR", sequenceName="SEQ_TNM_CODE_CATEGORY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_CODE_CATEGORY_CODECATEGORYID_GENERATOR")
	@Column(name="CODE_CATEGORY_ID")
	private Integer codeCategoryId;

	@Column(name="CODE_CATEGORY")
	private String codeCategory;

	@Column(name="CODE_NAME")
	private String codeName;

	@Column(name="CODE_VALUE")
	private Integer codeValue;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="LANGUAGE_FLAG")
	private Integer languageFlag;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_DATE")
	private Date updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnmCodeCategory() {
	}

	public Integer getCodeCategoryId() {
		return this.codeCategoryId;
	}

	public void setCodeCategoryId(Integer codeCategoryId) {
		this.codeCategoryId = codeCategoryId;
	}

	public String getCodeCategory() {
		return this.codeCategory;
	}

	public void setCodeCategory(String codeCategory) {
		this.codeCategory = codeCategory;
	}

	public String getCodeName() {
		return this.codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public Integer getCodeValue() {
		return this.codeValue;
	}

	public void setCodeValue(Integer codeValue) {
		this.codeValue = codeValue;
	}

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getLanguageFlag() {
		return this.languageFlag;
	}

	public void setLanguageFlag(Integer languageFlag) {
		this.languageFlag = languageFlag;
	}

	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}