package com.chinaplus.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


/**
 * The persistent class for the TNT_IP_KANBAN database table.
 * 
 */
@Entity(name="TNT_IP_KANBAN")
public class TntIpKanban extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_IP_KANBANID_GENERATOR", sequenceName="SEQ_TNT_IP_KANBAN", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_IP_KANBANID_GENERATOR")
	@Column(name="IP_KANBAN_ID")
	private Integer ipKanbanId;

    @Column(name = "IF_IP_ID")
    private Integer ifIpId;

    @Column(name = "DATA_TYPE")
    private Integer dataType;

    @Column(name = "KANBAN_PLAN_NO")
    private String kanbanPlanNo;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    @Column(name = "SUPPLIER_ID")
    private Integer supplierId;

    @Column(name = "EXP_PARTS_ID")
    private Integer expPartsId;
    
    @Column(name = "PID_NO")
    private String pidNo;

    @Column(name = "QTY")
    private BigDecimal qty;

    @Column(name = "SA_QTY")
    private BigDecimal saQty;

    @Column(name = "CONTAINER_NO")
    private String containerNo;
    
    @Column(name = "MODULE_NO")
    private String moduleNo;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

	public TntIpKanban() {
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

    /**
     * Get the ifIpId.
     *
     * @return ifIpId
     *
     */
    public Integer getIfIpId() {
        return this.ifIpId;
    }

    /**
     * Set the ifIpId.
     *
     * @param ifIpId ifIpId
     *
     */
    public void setIfIpId(Integer ifIpId) {
        this.ifIpId = ifIpId;

    }

    /**
     * Get the dataType.
     *
     * @return dataType
     *
     */
    public Integer getDataType() {
        return this.dataType;
    }

    /**
     * Set the dataType.
     *
     * @param dataType dataType
     *
     */
    public void setDataType(Integer dataType) {
        this.dataType = dataType;

    }

    /**
     * Get the kanbanPlanNo.
     *
     * @return kanbanPlanNo
     *
     */
    public String getKanbanPlanNo() {
        return this.kanbanPlanNo;
    }

    /**
     * Set the kanbanPlanNo.
     *
     * @param kanbanPlanNo kanbanPlanNo
     *
     */
    public void setKanbanPlanNo(String kanbanPlanNo) {
        this.kanbanPlanNo = kanbanPlanNo;

    }

    /**
     * Get the invoiceNo.
     *
     * @return invoiceNo
     *
     */
    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    /**
     * Set the invoiceNo.
     *
     * @param invoiceNo invoiceNo
     *
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;

    }

    /**
     * Get the partsId.
     *
     * @return partsId
     *
     */
    public Integer getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     *
     */
    public void setPartsId(Integer partsId) {
        this.partsId = partsId;

    }

    /**
     * Get the supplierId.
     *
     * @return supplierId
     *
     */
    public Integer getSupplierId() {
        return this.supplierId;
    }

    /**
     * Set the supplierId.
     *
     * @param supplierId supplierId
     *
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;

    }

    /**
     * Get the expPartsId.
     *
     * @return expPartsId
     *
     */
    public Integer getExpPartsId() {
        return this.expPartsId;
    }

    /**
     * Set the expPartsId.
     *
     * @param expPartsId expPartsId
     *
     */
    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;

    }

    /**
     * Get the qty.
     *
     * @return qty
     *
     */
    public BigDecimal getQty() {
        return this.qty;
    }

    /**
     * Set the qty.
     *
     * @param qty qty
     *
     */
    public void setQty(BigDecimal qty) {
        this.qty = qty;

    }

    /**
     * Get the ipKanbanId.
     *
     * @return ipKanbanId
     *
     */
    public Integer getIpKanbanId() {
        return this.ipKanbanId;
    }

    /**
     * Set the ipKanbanId.
     *
     * @param ipKanbanId ipKanbanId
     *
     */
    public void setIpKanbanId(Integer ipKanbanId) {
        this.ipKanbanId = ipKanbanId;

    }

    /**
     * Get the pidNo.
     *
     * @return pidNo
     */
    public String getPidNo() {
        return this.pidNo;
    }

    /**
     * Set the pidNo.
     *
     * @param pidNo pidNo
     */
    public void setPidNo(String pidNo) {
        this.pidNo = pidNo;
        
    }

    /**
     * Get the moduleNo.
     *
     * @return moduleNo
     */
    public String getModuleNo() {
        return this.moduleNo;
    }

    /**
     * Set the moduleNo.
     *
     * @param moduleNo moduleNo
     */
    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
        
    }

    /**
     * Get the saQty.
     *
     * @return saQty
     */
    public BigDecimal getSaQty() {
        return this.saQty;
    }

    /**
     * Set the saQty.
     *
     * @param saQty saQty
     */
    public void setSaQty(BigDecimal saQty) {
        this.saQty = saQty;
        
    }

    /**
     * Get the containerNo.
     *
     * @return containerNo
     */
    public String getContainerNo() {
        return this.containerNo;
    }

    /**
     * Set the containerNo.
     *
     * @param containerNo containerNo
     */
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
        
    }

}