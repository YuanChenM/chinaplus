package com.chinaplus.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the TNT_RUNDOWN_DETAIL database table.
 * 
 */
@Entity(name="TNT_RD_DETAIL")
public class TntRdDetail extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="TNT_RUNDOWN_DETAIL_RUNDOWNDETAILID_GENERATOR", sequenceName="SEQ_TNT_RUNDOWN_DETAIL", allocationSize = 1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_RUNDOWN_DETAIL_RUNDOWNDETAILID_GENERATOR")
	@Column(name="RUNDOWN_DETAIL_ID")
	private Integer rundownDetailId;

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

	@Column(name="SIMULATION_TYPE")
	private Integer simulationType;

    @Temporal(TemporalType.DATE)
    @Column(name="IMP_INB_PLAN_DATE")
    private Date impInbPlanDate;

	@Column(name="ORDER_FORECAST_QTY")
	private BigDecimal orderForecastQty;

	@Column(name="NOT_ETD_QTY")
	private BigDecimal notEtdQty;

    @Column(name="ON_SHIPPING_QTY")
    private BigDecimal onShippingQty;

    @Column(name="DAILY_USAGE_QTY")
    private BigDecimal daliyUsageQty;
    
    @Column(name="ENDING_STOCK")
    private BigDecimal endingStock;

    @Column(name="STOCK_DAYS")
    private BigDecimal stockDays;

    @Column(name="NO_CFC_FLAG")
    private Integer noCfcFlag;

    @Column(name="NO_PFC_FLAG")
    private Integer noPfcFlag;

    @Column(name="ADD_ON_MIN_QTY")
    private BigDecimal addOnMinQty;

    @Column(name="ADD_ON_SAFTY_QTY")
    private BigDecimal addOnSaftyQty;
    
	@Column(name="WORKING_FLAG")
	private Integer workingFlag;
	
	//bi-directional many-to-one association to TntRundownMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RUNDOWN_MASTER_ID")
	private TntRdMaster tntRundownMaster;
	
    // bi-directional many-to-one association to TntRdDetailAttach
    @OneToMany(mappedBy = "tntRundownDetail")
    private List<TntRdAttachShipping> tntRdDetailAttachs;

	public TntRdDetail() {
	}

	public Integer getRundownDetailId() {
		return this.rundownDetailId;
	}

	public void setRundownDetailId(Integer rundownDetailId) {
		this.rundownDetailId = rundownDetailId;
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

	public BigDecimal getOnShippingQty() {
		return this.onShippingQty;
	}

	public void setOnShippingQty(BigDecimal onShippingQty) {
		this.onShippingQty = onShippingQty;
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

	public Integer getWorkingFlag() {
		return this.workingFlag;
	}

	public void setWorkingFlag(Integer workingFlag) {
		this.workingFlag = workingFlag;
	}

	public TntRdMaster getTntRundownMaster() {
		return this.tntRundownMaster;
	}

	public void setTntRundownMaster(TntRdMaster tntRundownMaster) {
		this.tntRundownMaster = tntRundownMaster;
	}

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
        
    }

    public Integer getSimulationType() {
        return this.simulationType;
    }

    public void setSimulationType(Integer simulationType) {
        this.simulationType = simulationType;
        
    }

    public Date getImpInbPlanDate() {
        return this.impInbPlanDate;
    }

    public void setImpInbPlanDate(Date impInbPlanDate) {
        this.impInbPlanDate = impInbPlanDate;
        
    }

    public BigDecimal getOrderForecastQty() {
        return this.orderForecastQty;
    }

    public void setOrderForecastQty(BigDecimal orderForecastQty) {
        this.orderForecastQty = orderForecastQty;
        
    }

    public BigDecimal getNotEtdQty() {
        return this.notEtdQty;
    }

    public void setNotEtdQty(BigDecimal notEtdQty) {
        this.notEtdQty = notEtdQty;
        
    }

    public BigDecimal getDaliyUsageQty() {
        return this.daliyUsageQty;
    }

    public void setDaliyUsageQty(BigDecimal daliyUsageQty) {
        this.daliyUsageQty = daliyUsageQty;
        
    }

    public BigDecimal getEndingStock() {
        return this.endingStock;
    }

    public void setEndingStock(BigDecimal endingStock) {
        this.endingStock = endingStock;
        
    }

    public BigDecimal getStockDays() {
        return this.stockDays;
    }

    public void setStockDays(BigDecimal stockDays) {
        this.stockDays = stockDays;
        
    }

    public Integer getNoCfcFlag() {
        return this.noCfcFlag;
    }

    public void setNoCfcFlag(Integer noCfcFlag) {
        this.noCfcFlag = noCfcFlag;
        
    }

    public Integer getNoPfcFlag() {
        return this.noPfcFlag;
    }

    public void setNoPfcFlag(Integer noPfcFlag) {
        this.noPfcFlag = noPfcFlag;
        
    }

    public BigDecimal getAddOnMinQty() {
        return this.addOnMinQty;
    }

    public void setAddOnMinQty(BigDecimal addOnMinQty) {
        this.addOnMinQty = addOnMinQty;
        
    }

    public BigDecimal getAddOnSaftyQty() {
        return this.addOnSaftyQty;
    }

    public void setAddOnSaftyQty(BigDecimal addOnSaftyQty) {
        this.addOnSaftyQty = addOnSaftyQty;
        
    }
    
    public List<TntRdAttachShipping> getTntRdDetailAttachs() {
        return this.tntRdDetailAttachs;
    }

    public void setTntRdDetailAttachs(List<TntRdAttachShipping> tntRdDetailAttachs) {
        this.tntRdDetailAttachs = tntRdDetailAttachs;
    }

    public TntRdAttachShipping addTntRdDetailAttach(TntRdAttachShipping tntRdDetailAttach) {
        
        // set into database
        if(this.getTntRdDetailAttachs() == null) {
            this.setTntRdDetailAttachs(new ArrayList<TntRdAttachShipping>());
        }
        
        this.getTntRdDetailAttachs().add(tntRdDetailAttach);
        tntRdDetailAttach.setTntRundownDetail(this);

        return tntRdDetailAttach;
    }

    public TntRdAttachShipping removeTntRdDetailAttach(TntRdAttachShipping tntRdDetailAttach) {
        if (this.getTntRdDetailAttachs() != null) {
            this.getTntRdDetailAttachs().remove(tntRdDetailAttach);
        }
        tntRdDetailAttach.setTntRundownDetail(null);

        return tntRdDetailAttach;
    }

}