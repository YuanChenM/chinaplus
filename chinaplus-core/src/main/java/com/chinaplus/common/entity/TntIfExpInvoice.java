package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the TNT_IF_EXP_INVOICE database table.
 * 
 */
@Entity(name="TNT_IF_EXP_INVOICE")
public class TntIfExpInvoice extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="TNT_IF_EXP_INVOICE_IFINVOICEID_GENERATOR", sequenceName="SEQ_TNT_IF_EXP_INVOICE", allocationSize = 1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_IF_EXP_INVOICE_IFINVOICEID_GENERATOR")
	@Column(name="IF_INVOICE_ID")
	private Integer ifInvoiceId;

	@Column(name="BL_DATE")
	private String blDate;

	@Column(name="BL_NO")
	private String blNo;

	@Column(name="CONTAINER_NO")
	private String containerNo;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="DATA_ID")
	private String dataId;

	private String eta;

	private String etd;

	@Column(name="EXP_CODE")
	private String expCode;

	@Column(name="HANDLE_FLAG")
	private Integer handleFlag;

	@Column(name="IF_DATE_TIME")
	private Timestamp ifDateTime;

	@Column(name="INVOICE_NO")
	private String invoiceNo;

	@Column(name="MAIN_ROUTE")
	private String mainRoute;

	@Column(name="MODULE_NO")
	private String moduleNo;

	@Column(name="TRANSPORT_MODE")
	private String transportMode;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="VALID_FLAG")
	private Integer validFlag;

	@Column(name="\"VERSION\"")
	private Integer version;

	@Column(name="VESSEL_NAME")
	private String vesselName;

	public TntIfExpInvoice() {
	}

	public Integer getIfInvoiceId() {
		return this.ifInvoiceId;
	}

	public void setIfInvoiceId(Integer ifInvoiceId) {
		this.ifInvoiceId = ifInvoiceId;
	}

	public String getBlDate() {
		return this.blDate;
	}

	public void setBlDate(String blDate) {
		this.blDate = blDate;
	}

	public String getBlNo() {
		return this.blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getContainerNo() {
		return this.containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
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

	public String getDataId() {
		return this.dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getEta() {
		return this.eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getEtd() {
		return this.etd;
	}

	public void setEtd(String etd) {
		this.etd = etd;
	}

	public String getExpCode() {
		return this.expCode;
	}

	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}

	public Integer getHandleFlag() {
		return this.handleFlag;
	}

	public void setHandleFlag(Integer handleFlag) {
		this.handleFlag = handleFlag;
	}

	public Timestamp getIfDateTime() {
		return this.ifDateTime;
	}

	public void setIfDateTime(Timestamp ifDateTime) {
		this.ifDateTime = ifDateTime;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getMainRoute() {
		return this.mainRoute;
	}

	public void setMainRoute(String mainRoute) {
		this.mainRoute = mainRoute;
	}

	public String getModuleNo() {
		return this.moduleNo;
	}

	public void setModuleNo(String moduleNo) {
		this.moduleNo = moduleNo;
	}

	public String getTransportMode() {
		return this.transportMode;
	}

	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
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

	public Integer getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getVesselName() {
		return this.vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

}