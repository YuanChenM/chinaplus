package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TNT_RD_DETAIL_ATTACH database table.
 * 
 */
@Entity(name="TNR_RD_ATTACH_SHIPPING")
public class TntRdAttachShipping extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="TNT_RD_DETAIL_ATTACH_RDDETAILATTACHID_GENERATOR", sequenceName="SEQ_TNT_RD_DETAIL_ATTACH", allocationSize = 1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_RD_DETAIL_ATTACH_RDDETAILATTACHID_GENERATOR")
	@Column(name="RD_DETAIL_ATTACH_ID")
	private Integer rdDetailAttachId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;
	
	@Column(name="PARTS_ID")
    private Integer partsId;
	   
    @Column(name="SUPPLIER_ID")
    private Integer supplierId;

    @Column(name="ATTACH_TYPE")
    private Integer attachType;

    @Column(name="PFC_MONTH")
    private String pfcMonth;

    @Column(name="IMP_PO_NO")
    private String impPoNo;

    @Column(name="VANNING_DATE")
    private Date vanningDate;

    @Column(name="ETD")
    private Date etd;

    @Column(name="ETA")
    private Date eta;

    @Column(name="IMP_INB_PLAN_DATE")
    private Date impInbPlanDate;

    @Column(name="QTY")
    private BigDecimal qty;
    
    @Column(name="INVOICE_NO")
    private String invoiceNo;
    
	//bi-directional many-to-one association to TntRundownMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RUNDOWN_DETAIL_ID")
	private TntRdDetail tntRundownDetail;

	public TntRdAttachShipping() {
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

    public Integer getRdDetailAttachId() {
        return this.rdDetailAttachId;
    }

    public void setRdDetailAttachId(Integer rdDetailAttachId) {
        this.rdDetailAttachId = rdDetailAttachId;
        
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
        
    }

    public Integer getAttachType() {
        return this.attachType;
    }

    public void setAttachType(Integer attachType) {
        this.attachType = attachType;
        
    }

    public String getPfcMonth() {
        return this.pfcMonth;
    }

    public void setPfcMonth(String pfcMonth) {
        this.pfcMonth = pfcMonth;
        
    }

    public String getImpPoNo() {
        return this.impPoNo;
    }

    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
        
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

    public Date getImpInbPlanDate() {
        return this.impInbPlanDate;
    }

    public void setImpInbPlanDate(Date impInbPlanDate) {
        this.impInbPlanDate = impInbPlanDate;
        
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
        
    }

    public TntRdDetail getTntRundownDetail() {
        return this.tntRundownDetail;
    }

    public void setTntRundownDetail(TntRdDetail tntRundownDetail) {
        this.tntRundownDetail = tntRundownDetail;
        
    }

    /**
     * Get the invoiceNo.
     *
     * @return invoiceNo
     *
     * @author liu_yinchuan
     */
    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    /**
     * Set the invoiceNo.
     *
     * @param invoiceNo invoiceNo
     *
     * @author liu_yinchuan
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        
    }

    /**
     * Get the supplierId.
     *
     * @return supplierId
     *
     * @author liu_yinchuan
     */
    public Integer getSupplierId() {
        return this.supplierId;
    }

    /**
     * Set the supplierId.
     *
     * @param supplierId supplierId
     *
     * @author liu_yinchuan
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
        
    }

}