package com.chinaplus.common.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TNT_RUNDOWN_ATTACH database table.
 * 
 */
@Entity(name="TNR_RD_ATTACH_CFC")
public class TntRdAttachCfc extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="TNT_RUNDOWN_ATTACH_RUNDOWNATTACHID_GENERATOR", sequenceName="SEQ_TNT_RUNDOWN_ATTACH", allocationSize = 1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_RUNDOWN_ATTACH_RUNDOWNATTACHID_GENERATOR")
	@Column(name="RUNDOWN_ATTACH_ID")
	private Integer rundownAttachId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="QTY")
	private BigDecimal qty;

	@Column(name="PARTS_ID")
	private Integer partsId;

	@Column(name="CFC_MONTH")
	private String cfcMonth;

	@Temporal(TemporalType.DATE)
	@Column(name="SHARE_DATE")
	private Date shareDate;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;
	
    private Integer rundownMasterId;
	
	//bi-directional many-to-one association to TntRundownMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RUNDOWN_MASTER_ID")
	private TntRdMaster tntRundownMaster;

	public TntRdAttachCfc() {
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

	public TntRdMaster getTntRundownMaster() {
		return this.tntRundownMaster;
	}

	public void setTntRundownMaster(TntRdMaster tntRundownMaster) {
		this.tntRundownMaster = tntRundownMaster;
	}

    /**
     * Get the rundownAttachId.
     *
     * @return rundownAttachId
     */
    public Integer getRundownAttachId() {
        return this.rundownAttachId;
    }

    /**
     * Set the rundownAttachId.
     *
     * @param rundownAttachId rundownAttachId
     */
    public void setRundownAttachId(Integer rundownAttachId) {
        this.rundownAttachId = rundownAttachId;
        
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
        
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
        
    }

    public String getCfcMonth() {
        return this.cfcMonth;
    }

    public void setCfcMonth(String cfcMonth) {
        this.cfcMonth = cfcMonth;
        
    }

    public Date getShareDate() {
        return this.shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
        
    }

    /**
     * Get the rundownMasterId.
     *
     * @return rundownMasterId
     *
     * @author liu_yinchuan
     */
    public Integer getRundownMasterId() {
        return this.rundownMasterId;
    }

    /**
     * Set the rundownMasterId.
     *
     * @param rundownMasterId rundownMasterId
     *
     * @author liu_yinchuan
     */
    public void setRundownMasterId(Integer rundownMasterId) {
        this.rundownMasterId = rundownMasterId;
        
    }

}