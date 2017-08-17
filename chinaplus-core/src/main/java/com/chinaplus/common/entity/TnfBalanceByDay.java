package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TNF_BALANCE_BY_DAY database table.
 * 
 */
@Entity(name="TNF_BALANCE_BY_DAY")
public class TnfBalanceByDay extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="TNF_BALANCE_BY_DAY_BALANCEID_GENERATOR", sequenceName="SEQ_TNF_BALANCE_BY_DAY", allocationSize = 1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNF_BALANCE_BY_DAY_BALANCEID_GENERATOR")
	@Column(name="BALANCE_ID")
	private Integer balanceId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMER_ID")
	private Integer customerId;

	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;

	@Column(name="ORDER_BALANCE_QTY")
	private BigDecimal orderBalanceQty;

	@Column(name="IMP_PO_NO")
	private String impPoNo;

	@Column(name="KANBAN_PLAN_NO")
	private String kanbanPlanNo;

	@Column(name="ORDER_MONTH")
	private String orderMonth;

	@Column(name="PARTS_ID")
	private Integer partsId;

	@Column(name="SUPPLIER_ID")
	private Integer supplierId;

    @Column(name="BUSINESS_PATTERN")
    private Integer businessPattern;

    @Column(name="ORDER_QTY")
    private BigDecimal orderQty;

    @Column(name="IMP_INBOUND_QTY")
    private BigDecimal impInboundQty;

    @Column(name="FORCE_COMPLETED_QTY")
    private BigDecimal forceCompletedQty;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnfBalanceByDay() {
	}

	public Integer getBalanceId() {
		return this.balanceId;
	}

	public void setBalanceId(Integer balanceId) {
		this.balanceId = balanceId;
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

	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getImpPoNo() {
		return this.impPoNo;
	}

	public void setImpPoNo(String impPoNo) {
		this.impPoNo = impPoNo;
	}

	public String getKanbanPlanNo() {
		return this.kanbanPlanNo;
	}

	public void setKanbanPlanNo(String kanbanPlanNo) {
		this.kanbanPlanNo = kanbanPlanNo;
	}

	public String getOrderMonth() {
		return this.orderMonth;
	}

	public void setOrderMonth(String orderMonth) {
		this.orderMonth = orderMonth;
	}

	public Integer getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Integer partsId) {
		this.partsId = partsId;
	}

	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
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
     * Get the orderBalanceQty.
     *
     * @return orderBalanceQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getOrderBalanceQty() {
        return this.orderBalanceQty;
    }

    /**
     * Set the orderBalanceQty.
     *
     * @param orderBalanceQty orderBalanceQty
     *
     * @author liu_yinchuan
     */
    public void setOrderBalanceQty(BigDecimal orderBalanceQty) {
        this.orderBalanceQty = orderBalanceQty;
        
    }

    /**
     * Get the businessPattern.
     *
     * @return businessPattern
     *
     * @author liu_yinchuan
     */
    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     *
     * @author liu_yinchuan
     */
    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
        
    }

    /**
     * Get the orderQty.
     *
     * @return orderQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getOrderQty() {
        return this.orderQty;
    }

    /**
     * Set the orderQty.
     *
     * @param orderQty orderQty
     *
     * @author liu_yinchuan
     */
    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
        
    }

    /**
     * Get the impInboundQty.
     *
     * @return impInboundQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getImpInboundQty() {
        return this.impInboundQty;
    }

    /**
     * Set the impInboundQty.
     *
     * @param impInboundQty impInboundQty
     *
     * @author liu_yinchuan
     */
    public void setImpInboundQty(BigDecimal impInboundQty) {
        this.impInboundQty = impInboundQty;
        
    }

    /**
     * Get the forceCompletedQty.
     *
     * @return forceCompletedQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getForceCompletedQty() {
        return this.forceCompletedQty;
    }

    /**
     * Set the forceCompletedQty.
     *
     * @param forceCompletedQty forceCompletedQty
     *
     * @author liu_yinchuan
     */
    public void setForceCompletedQty(BigDecimal forceCompletedQty) {
        this.forceCompletedQty = forceCompletedQty;
        
    }

}