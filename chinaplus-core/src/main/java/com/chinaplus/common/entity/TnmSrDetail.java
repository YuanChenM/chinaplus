package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the TNM_SR_DETAIL database table.
 * 
 */
@Entity(name="TNM_SR_DETAIL")
public class TnmSrDetail extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNM_SR_DETAIL_SRDETAILID_GENERATOR", sequenceName="SEQ_TNM_SR_DETAIL", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNM_SR_DETAIL_SRDETAILID_GENERATOR")
	@Column(name="SR_DETAIL_ID")
	private Integer srDetailId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Temporal(TemporalType.DATE)
	@Column(name="CUSTOM_CLEARANCE")
	private Date customClearance;

	@Temporal(TemporalType.DATE)
	@Column(name="CY_CUT")
	private Date cyCut;

	@Temporal(TemporalType.DATE)
	@Column(name="DELIVERY_END")
	private Date deliveryEnd;

	@Temporal(TemporalType.DATE)
	@Column(name="DELIVERY_START")
	private Date deliveryStart;

	@Temporal(TemporalType.DATE)
	@Column(name="DOCS_PREPARATION")
	private Date docsPreparation;

	@Temporal(TemporalType.DATE)
	private Date eta;

	@Temporal(TemporalType.DATE)
	private Date etd;

	@Temporal(TemporalType.DATE)
	@Column(name="EXP_INBOUND_DATE")
	private Date expInboundDate;

	@Temporal(TemporalType.DATE)
	@Column(name="IMP_INB_PLAN_DATE")
	private Date impInbPlanDate;

	@Column(name="INACTIVE_FLAG")
	private Integer inactiveFlag;

	@Temporal(TemporalType.DATE)
	@Column(name="KANBAN_ISSUE_DATE")
	private Date kanbanIssueDate;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_VANNING")
	private Date lastVanning;

	@Temporal(TemporalType.DATE)
	@Column(name="PACKING_END")
	private Date packingEnd;

	@Temporal(TemporalType.DATE)
	@Column(name="PORT_IN")
	private Date portIn;

	@Temporal(TemporalType.DATE)
	@Column(name="SHIPPING_INSTRUCTION")
	private Date shippingInstruction;

	@Column(name="SUPPLIER_ID")
	private Integer supplierId;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	//bi-directional many-to-one association to TnmSrMaster
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SR_ID")
	private TnmSrMaster tnmSrMaster;

	public TnmSrDetail() {
	}

	public Integer getSrDetailId() {
		return this.srDetailId;
	}

	public void setSrDetailId(Integer srDetailId) {
		this.srDetailId = srDetailId;
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

	public Date getCustomClearance() {
		return this.customClearance;
	}

	public void setCustomClearance(Date customClearance) {
		this.customClearance = customClearance;
	}

	public Date getCyCut() {
		return this.cyCut;
	}

	public void setCyCut(Date cyCut) {
		this.cyCut = cyCut;
	}

	public Date getDeliveryEnd() {
		return this.deliveryEnd;
	}

	public void setDeliveryEnd(Date deliveryEnd) {
		this.deliveryEnd = deliveryEnd;
	}

	public Date getDeliveryStart() {
		return this.deliveryStart;
	}

	public void setDeliveryStart(Date deliveryStart) {
		this.deliveryStart = deliveryStart;
	}

	public Date getDocsPreparation() {
		return this.docsPreparation;
	}

	public void setDocsPreparation(Date docsPreparation) {
		this.docsPreparation = docsPreparation;
	}

	public Date getEta() {
		return this.eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Date getEtd() {
		return this.etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public Date getExpInboundDate() {
		return this.expInboundDate;
	}

	public void setExpInboundDate(Date expInboundDate) {
		this.expInboundDate = expInboundDate;
	}

	public Date getImpInbPlanDate() {
		return this.impInbPlanDate;
	}

	public void setImpInbPlanDate(Date impInbPlanDate) {
		this.impInbPlanDate = impInbPlanDate;
	}

	public Integer getInactiveFlag() {
		return this.inactiveFlag;
	}

	public void setInactiveFlag(Integer inactiveFlag) {
		this.inactiveFlag = inactiveFlag;
	}

	public Date getKanbanIssueDate() {
		return this.kanbanIssueDate;
	}

	public void setKanbanIssueDate(Date kanbanIssueDate) {
		this.kanbanIssueDate = kanbanIssueDate;
	}

	public Date getLastVanning() {
		return this.lastVanning;
	}

	public void setLastVanning(Date lastVanning) {
		this.lastVanning = lastVanning;
	}

	public Date getPackingEnd() {
		return this.packingEnd;
	}

	public void setPackingEnd(Date packingEnd) {
		this.packingEnd = packingEnd;
	}

	public Date getPortIn() {
		return this.portIn;
	}

	public void setPortIn(Date portIn) {
		this.portIn = portIn;
	}

	public Date getShippingInstruction() {
		return this.shippingInstruction;
	}

	public void setShippingInstruction(Date shippingInstruction) {
		this.shippingInstruction = shippingInstruction;
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

	public TnmSrMaster getTnmSrMaster() {
		return this.tnmSrMaster;
	}

	public void setTnmSrMaster(TnmSrMaster tnmSrMaster) {
		this.tnmSrMaster = tnmSrMaster;
	}

}