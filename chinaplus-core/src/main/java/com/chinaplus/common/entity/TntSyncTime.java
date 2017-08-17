package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNT_SYNC_TIME database table.
 * 
 */
@Entity(name="TNT_SYNC_TIME")
public class TntSyncTime extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_SYNC_TIME_SYNCID_GENERATOR", sequenceName="SEQ_TNT_SYNC_TIME", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_SYNC_TIME_SYNCID_GENERATOR")
	@Column(name="SYNC_ID")
	private Integer syncId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="SYNC_DATA_TIME")
	private Timestamp impSyncTime;

	@Column(name="OFFICE_ID")
	private Integer officeId;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;
	
	@Column(name="DATA_TYPE")
    private Integer dataType;

	public TntSyncTime() {
	}

	public Integer getSyncId() {
		return this.syncId;
	}

	public void setSyncId(Integer syncId) {
		this.syncId = syncId;
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

	public Timestamp getImpSyncTime() {
		return this.impSyncTime;
	}

	public void setImpSyncTime(Timestamp impSyncTime) {
		this.impSyncTime = impSyncTime;
	}

	public Integer getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
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

    
    public Integer getDataType() {
        return this.dataType;
    }

    
    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

}