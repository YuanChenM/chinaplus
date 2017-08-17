/**
 * CPKKPF12PlanColumnEntity.java
 * 
 * @screen CPKKPF12
 * @author shiyang
 */
package com.chinaplus.web.kbp.entity;

import java.util.List;

import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Kanban Plan Upload Entity.
 */
public class CPKKPF12ColEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Column Type (All column) */
    private int columnTypeAll;

    /** Column Type */
    private String columnType;

    /** Merge Flag (0:not merged, do insert; 1:merged, do not insert) */
    private int mergeFlag;

    /** Col No. */
    private int colNo;

    /** Transport Mode */
    private String transportMode;

    /** Transport Mode Revision */
    private String transportModeRevision;

    /** Issue Remark */
    private String issueRemark;

    /** Delivere Remark */
    private String delivereRemark;

    /** Vanning Remark */
    private String vanningRemark;

    /** Invoice No. */
    private String invoiceNo;

    /** ETD */
    private String etd;

    /** ETD Revision */
    private String etdRevision;

    /** ETA */
    private String eta;

    /** ETA Revision */
    private String etaRevision;

    /** Imp Inb Plan Date */
    private String impInbPlanDate;

    /** Imp Inb Plan Date Revision */
    private String impInbPlanDateRevision;

    /** Imp Inb Actual Date */
    private String impInbActualDate;

    /** Revision Reason for nird */
    private String revisionReason;

    /** Revision Reason for Last Change */
    private String revisionReasonLastChange;

    /** Revision Reason for This Change */
    private String revisionReasonThisChange;

    /** Revision Version File */
    private String revisionVersionFile;

    /** Original Version */
    private int originalVersion;

    /** Revision Version */
    private int revisionVersion;

    /** Completed Flag */
    private int completedFlag;

    /** Nird Flag */
    private int nirdFlag;

    // /** Row Data Map (key:Parts Id, value: Qty) */
    // private HashMap<Integer, String> rowDataMap;

    /** Row Data List (Qty List, [0]:rowNo,[1]:value) */
    private List<String[]> rowDataList;

    /** Row Data List (Qty List, [0]:rowNo,[1]:value) */
    private List<String[]> rowDataForUpdateList;

    /** The col No. of the plan's difference */
    private List<Integer> listDiff;

    /** The col No. of the plan's box */
    private List<Integer> listBox;

    /** The col No. of the plan's difference */
    private List<Integer> listDiffMerge;

    /** The col No. of the plan's box */
    private List<Integer> listBoxMerge;

    /** The col No. of the plan */
    private List<Integer> listPlanMerge;

    /** The col No. of the plan's difference */
    private List<Integer> listDiffMod;

    /** The col No. of the plan's box */
    private List<Integer> listBoxMod;

    /**
     * Get the columnTypeAll.
     *
     * @return columnTypeAll
     */
    public int getColumnTypeAll() {
        return this.columnTypeAll;
    }

    /**
     * Set the columnTypeAll.
     *
     * @param columnTypeAll columnTypeAll
     */
    public void setColumnTypeAll(int columnTypeAll) {
        this.columnTypeAll = columnTypeAll;
    }

    /**
     * Get the columnType.
     *
     * @return columnType
     */
    public String getColumnType() {
        return this.columnType;
    }

    /**
     * Set the columnType.
     *
     * @param columnType columnType
     */
    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    /**
     * Get the mergeFlag.
     *
     * @return mergeFlag
     */
    public int getMergeFlag() {
        return this.mergeFlag;
    }

    /**
     * Set the mergeFlag.
     *
     * @param mergeFlag mergeFlag
     */
    public void setMergeFlag(int mergeFlag) {
        this.mergeFlag = mergeFlag;
    }

    /**
     * Get the colNo.
     *
     * @return colNo
     */
    public int getColNo() {
        return this.colNo;
    }

    /**
     * Set the colNo.
     *
     * @param colNo colNo
     */
    public void setColNo(int colNo) {
        this.colNo = colNo;
    }

    /**
     * Get the transportMode.
     *
     * @return transportMode
     */
    public String getTransportMode() {
        return this.transportMode;
    }

    /**
     * Set the transportMode.
     *
     * @param transportMode transportMode
     */
    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * Get the transportModeRevision.
     *
     * @return transportModeRevision
     */
    public String getTransportModeRevision() {
        return this.transportModeRevision;
    }

    /**
     * Set the transportModeRevision.
     *
     * @param transportModeRevision transportModeRevision
     */
    public void setTransportModeRevision(String transportModeRevision) {
        this.transportModeRevision = transportModeRevision;
    }

    /**
     * Get the issueRemark.
     *
     * @return issueRemark
     */
    public String getIssueRemark() {
        return this.issueRemark;
    }

    /**
     * Set the issueRemark.
     *
     * @param issueRemark issueRemark
     */
    public void setIssueRemark(String issueRemark) {
        this.issueRemark = issueRemark;
    }

    /**
     * Get the delivereRemark.
     *
     * @return delivereRemark
     */
    public String getDelivereRemark() {
        return this.delivereRemark;
    }

    /**
     * Set the delivereRemark.
     *
     * @param delivereRemark delivereRemark
     */
    public void setDelivereRemark(String delivereRemark) {
        this.delivereRemark = delivereRemark;
    }

    /**
     * Get the vanningRemark.
     *
     * @return vanningRemark
     */
    public String getVanningRemark() {
        return this.vanningRemark;
    }

    /**
     * Set the vanningRemark.
     *
     * @param vanningRemark vanningRemark
     */
    public void setVanningRemark(String vanningRemark) {
        this.vanningRemark = vanningRemark;
    }

    /**
     * Get the invoiceNo.
     *
     * @return invoiceNo
     */
    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    /**
     * Set the invoiceNo.
     *
     * @param invoiceNo invoiceNo
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     * Get the etd.
     *
     * @return etd
     */
    public String getEtd() {
        return this.etd;
    }

    /**
     * Set the etd.
     *
     * @param etd etd
     */
    public void setEtd(String etd) {
        this.etd = etd;
    }

    /**
     * Get the etdRevision.
     *
     * @return etdRevision
     */
    public String getEtdRevision() {
        return this.etdRevision;
    }

    /**
     * Set the etdRevision.
     *
     * @param etdRevision etdRevision
     */
    public void setEtdRevision(String etdRevision) {
        this.etdRevision = etdRevision;
    }

    /**
     * Get the eta.
     *
     * @return eta
     */
    public String getEta() {
        return this.eta;
    }

    /**
     * Set the eta.
     *
     * @param eta eta
     */
    public void setEta(String eta) {
        this.eta = eta;
    }

    /**
     * Get the etaRevision.
     *
     * @return etaRevision
     */
    public String getEtaRevision() {
        return this.etaRevision;
    }

    /**
     * Set the etaRevision.
     *
     * @param etaRevision etaRevision
     */
    public void setEtaRevision(String etaRevision) {
        this.etaRevision = etaRevision;
    }

    /**
     * Get the impInbPlanDate.
     *
     * @return impInbPlanDate
     */
    public String getImpInbPlanDate() {
        return this.impInbPlanDate;
    }

    /**
     * Set the impInbPlanDate.
     *
     * @param impInbPlanDate impInbPlanDate
     */
    public void setImpInbPlanDate(String impInbPlanDate) {
        this.impInbPlanDate = impInbPlanDate;
    }

    /**
     * Get the impInbPlanDateRevision.
     *
     * @return impInbPlanDateRevision
     */
    public String getImpInbPlanDateRevision() {
        return this.impInbPlanDateRevision;
    }

    /**
     * Set the impInbPlanDateRevision.
     *
     * @param impInbPlanDateRevision impInbPlanDateRevision
     */
    public void setImpInbPlanDateRevision(String impInbPlanDateRevision) {
        this.impInbPlanDateRevision = impInbPlanDateRevision;
    }

    /**
     * Get the impInbActualDate.
     *
     * @return impInbActualDate
     */
    public String getImpInbActualDate() {
        return this.impInbActualDate;
    }

    /**
     * Set the impInbActualDate.
     *
     * @param impInbActualDate impInbActualDate
     */
    public void setImpInbActualDate(String impInbActualDate) {
        this.impInbActualDate = impInbActualDate;
    }

    /**
     * Get the revisionReason.
     *
     * @return revisionReason
     */
    public String getRevisionReason() {
        return this.revisionReason;
    }

    /**
     * Set the revisionReason.
     *
     * @param revisionReason revisionReason
     */
    public void setRevisionReason(String revisionReason) {
        this.revisionReason = revisionReason;
    }

    /**
     * Get the revisionReasonLastChange.
     *
     * @return revisionReasonLastChange
     */
    public String getRevisionReasonLastChange() {
        return this.revisionReasonLastChange;
    }

    /**
     * Set the revisionReasonLastChange.
     *
     * @param revisionReasonLastChange revisionReasonLastChange
     */
    public void setRevisionReasonLastChange(String revisionReasonLastChange) {
        this.revisionReasonLastChange = revisionReasonLastChange;
    }

    /**
     * Get the revisionReasonThisChange.
     *
     * @return revisionReasonThisChange
     */
    public String getRevisionReasonThisChange() {
        return this.revisionReasonThisChange;
    }

    /**
     * Set the revisionReasonThisChange.
     *
     * @param revisionReasonThisChange revisionReasonThisChange
     */
    public void setRevisionReasonThisChange(String revisionReasonThisChange) {
        this.revisionReasonThisChange = revisionReasonThisChange;
    }

    /**
     * Get the revisionVersionFile.
     *
     * @return revisionVersionFile
     */
    public String getRevisionVersionFile() {
        return this.revisionVersionFile;
    }

    /**
     * Set the revisionVersionFile.
     *
     * @param revisionVersionFile revisionVersionFile
     */
    public void setRevisionVersionFile(String revisionVersionFile) {
        this.revisionVersionFile = revisionVersionFile;
    }

    /**
     * Get the originalVersion.
     *
     * @return originalVersion
     */
    public int getOriginalVersion() {
        return this.originalVersion;
    }

    /**
     * Set the originalVersion.
     *
     * @param originalVersion originalVersion
     */
    public void setOriginalVersion(int originalVersion) {
        this.originalVersion = originalVersion;
    }

    /**
     * Get the revisionVersion.
     *
     * @return revisionVersion
     */
    public int getRevisionVersion() {
        return this.revisionVersion;
    }

    /**
     * Set the revisionVersion.
     *
     * @param revisionVersion revisionVersion
     */
    public void setRevisionVersion(int revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    /**
     * Get the completedFlag.
     *
     * @return completedFlag
     */
    public int getCompletedFlag() {
        return this.completedFlag;
    }

    /**
     * Set the completedFlag.
     *
     * @param completedFlag completedFlag
     */
    public void setCompletedFlag(int completedFlag) {
        this.completedFlag = completedFlag;
    }

    /**
     * Get the nirdFlag.
     *
     * @return nirdFlag
     */
    public int getNirdFlag() {
        return this.nirdFlag;
    }

    /**
     * Set the nirdFlag.
     *
     * @param nirdFlag nirdFlag
     */
    public void setNirdFlag(int nirdFlag) {
        this.nirdFlag = nirdFlag;
    }

    // /**
    // * Get the rowDataMap.
    // *
    // * @return rowDataMap
    // */
    // public HashMap<int, String> getRowDataMap() {
    // return this.rowDataMap;
    // }
    //
    // /**
    // * Set the rowDataMap.
    // *
    // * @param rowDataMap rowDataMap
    // */
    // public void setRowDataMap(HashMap<int, String> rowDataMap) {
    // this.rowDataMap = rowDataMap;
    // }

    /**
     * Get the rowDataList.
     *
     * @return rowDataList
     */
    public List<String[]> getRowDataList() {
        return this.rowDataList;
    }

    /**
     * Set the rowDataList.
     *
     * @param rowDataList rowDataList
     */
    public void setRowDataList(List<String[]> rowDataList) {
        this.rowDataList = rowDataList;
    }

    /**
     * Get the rowDataForUpdateList.
     *
     * @return rowDataForUpdateList
     */
    public List<String[]> getRowDataForUpdateList() {
        return this.rowDataForUpdateList;
    }

    /**
     * Set the rowDataForUpdateList.
     *
     * @param rowDataForUpdateList rowDataForUpdateList
     */
    public void setRowDataForUpdateList(List<String[]> rowDataForUpdateList) {
        this.rowDataForUpdateList = rowDataForUpdateList;
    }

    /**
     * Get the listDiff.
     *
     * @return listDiff
     */
    public List<Integer> getListDiff() {
        return this.listDiff;
    }

    /**
     * Set the listDiff.
     *
     * @param listDiff listDiff
     */
    public void setListDiff(List<Integer> listDiff) {
        this.listDiff = listDiff;
    }

    /**
     * Get the listBox.
     *
     * @return listBox
     */
    public List<Integer> getListBox() {
        return this.listBox;
    }

    /**
     * Set the listBox.
     *
     * @param listBox listBox
     */
    public void setListBox(List<Integer> listBox) {
        this.listBox = listBox;
    }

    /**
     * Get the listDiffMerge.
     *
     * @return listDiffMerge
     */
    public List<Integer> getListDiffMerge() {
        return this.listDiffMerge;
    }

    /**
     * Set the listDiffMerge.
     *
     * @param listDiffMerge listDiffMerge
     */
    public void setListDiffMerge(List<Integer> listDiffMerge) {
        this.listDiffMerge = listDiffMerge;
    }

    /**
     * Get the listBoxMerge.
     *
     * @return listBoxMerge
     */
    public List<Integer> getListBoxMerge() {
        return this.listBoxMerge;
    }

    /**
     * Set the listBoxMerge.
     *
     * @param listBoxMerge listBoxMerge
     */
    public void setListBoxMerge(List<Integer> listBoxMerge) {
        this.listBoxMerge = listBoxMerge;
    }

    /**
     * Get the listPlanMerge.
     *
     * @return listPlanMerge
     */
    public List<Integer> getListPlanMerge() {
        return this.listPlanMerge;
    }

    /**
     * Set the listPlanMerge.
     *
     * @param listPlanMerge listPlanMerge
     */
    public void setListPlanMerge(List<Integer> listPlanMerge) {
        this.listPlanMerge = listPlanMerge;
    }

    /**
     * Get the listDiffMod.
     *
     * @return listDiffMod
     */
    public List<Integer> getListDiffMod() {
        return this.listDiffMod;
    }

    /**
     * Set the listDiffMod.
     *
     * @param listDiffMod listDiffMod
     */
    public void setListDiffMod(List<Integer> listDiffMod) {
        this.listDiffMod = listDiffMod;
    }

    /**
     * Get the listBoxMod.
     *
     * @return listBoxMod
     */
    public List<Integer> getListBoxMod() {
        return this.listBoxMod;
    }

    /**
     * Set the listBoxMod.
     *
     * @param listBoxMod listBoxMod
     */
    public void setListBoxMod(List<Integer> listBoxMod) {
        this.listBoxMod = listBoxMod;
    }
}
