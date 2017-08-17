package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNM_REASON database table.
 * 
 */
@Entity(name="TNM_REASON")
public class TnmReason extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_REASON_REASONID_GENERATOR", sequenceName="SEQ_TNM_REASON", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_REASON_REASONID_GENERATOR")
	@Column(name="REASON_ID")
	private Integer reasonId;

	@Column(name="BUSINESS_PATTERN")
	private Integer businessPattern;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="INACTIVE_FLAG")
	private Integer inactiveFlag;

	@Column(name="REASON_CODE")
	private String reasonCode;

	@Column(name="REASON_DESC")
	private String reasonDesc;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	private String newMod;
	
	private String reason;
	
	public TnmReason() {
	}

	public Integer getReasonId() {
		return this.reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Integer getBusinessPattern() {
		return this.businessPattern;
	}

	public void setBusinessPattern(Integer businessPattern) {
		this.businessPattern = businessPattern;
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

	public Integer getInactiveFlag() {
		return this.inactiveFlag;
	}

	public void setInactiveFlag(Integer inactiveFlag) {
		this.inactiveFlag = inactiveFlag;
	}

	public String getReasonCode() {
		return this.reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonDesc() {
		return this.reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

    /**
     * Get the newMod.
     *
     * @return newMod
     */
    public String getNewMod() {
        return this.newMod;
    }

    /**
     * Set the newMod.
     *
     * @param newMod newMod
     */
    public void setNewMod(String newMod) {
        this.newMod = newMod;
    }

    /**
     * Get the reason.
     *
     * @return reason
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Set the reason.
     *
     * @param reason reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

}