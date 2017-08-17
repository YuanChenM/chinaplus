package com.chinaplus.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the TNT_RUNDOWN_ATTACH database table.
 * 
 */
@Entity(name="TNR_NOT_IN_RUNDOWN")
public class TntNotInRundown extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="TNR_NOT_IN_RUNDOWN_NOTINRUNDOWID_GENERATOR", sequenceName="SEQ_TNR_NOT_IN_RUNDOWN", allocationSize = 1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNR_NOT_IN_RUNDOWN_NOTINRUNDOWID_GENERATOR")
	@Column(name="NOT_IN_RUNDOWN_ID")
	private Integer notInRundownId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="REASON_TYPE")
	private Integer reasonType;

	@Column(name="PARTS_ID")
	private Integer partsId;

	@Column(name="IMP_PO_NO")
	private String impPoNo;

    @Column(name="EXP_PO_NO")
    private String expPoNo;

	@Temporal(TemporalType.DATE)
	@Column(name="VANNING_DATE")
	private Date vanningDate;

    @Temporal(TemporalType.DATE)
    @Column(name="ETD")
    private Date etd;

    @Temporal(TemporalType.DATE)
    @Column(name="ETA")
    private Date eta;

    @Column(name="TRANSPORT_MODE")
    private Integer transportMode;

    @Column(name="INVOICE_NO")
    private String invoiceNo;

    @Temporal(TemporalType.DATE)
    @Column(name="IMP_INB_DATE")
    private Date impInbDate;

    @Temporal(TemporalType.DATE)
    @Column(name="PROCESS_DATE")
    private Date processDate;
    
    @Column(name="QTY")
    private BigDecimal qty;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;
	
	//bi-directional many-to-one association to TntRundownMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RUNDOWN_MASTER_ID")
	private TntRdMaster tntRundownMaster;

	public TntNotInRundown() {
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

    public Integer getReasonType() {
        return this.reasonType;
    }

    public void setReasonType(Integer reasonType) {
        this.reasonType = reasonType;
        
    }

    public Integer getNotInRundownId() {
        return this.notInRundownId;
    }

    public void setNotInRundownId(Integer notInRundownId) {
        this.notInRundownId = notInRundownId;
        
    }

    public String getImpPoNo() {
        return this.impPoNo;
    }

    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
        
    }

    public String getExpPoNo() {
        return this.expPoNo;
    }

    public void setExpPoNo(String expPoNo) {
        this.expPoNo = expPoNo;
        
    }

    public Date getVanningDate() {
        return this.vanningDate;
    }

    public void setVanningDate(Date vanningDate) {
        this.vanningDate = vanningDate;
        
    }

    public Date getEtd() {
        return this.etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
        
    }

    public Date getEta() {
        return this.eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
        
    }

    public Integer getTransportMode() {
        return this.transportMode;
    }

    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
        
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        
    }

    public Date getImpInbDate() {
        return this.impInbDate;
    }

    public void setImpInbDate(Date impInbDate) {
        this.impInbDate = impInbDate;
        
    }

    public Date getProcessDate() {
        return this.processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
        
    }

}