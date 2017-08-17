/**
 * CPVIVF11SessionEntity.java
 * 
 * @screen CPVIVF11
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.util.List;

import com.chinaplus.core.base.BaseEntity;

/**
 * Invoice Upload Session Entity.
 */
public class CPVIVF11SessionEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Upload ID */
    private String uploadId;

    /** Temp Folder Path */
    private String tempFolderPath;

    /** KANB Data Total List */
    private List<CPVIVF11KanbEntity> kanbTotalList;

    /** KANB Data List */
    private List<CPVIVF11KanbEntity> kanbDataList;

    /** Mail Invoice Data List */
    private List<CPVIVF11MailInvoiceEntity> mailInvoiceList;

    /**
     * Get the uploadId.
     *
     * @return uploadId
     */
    public String getUploadId() {
        return this.uploadId;
    }

    /**
     * Set the uploadId.
     *
     * @param uploadId uploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * Get the tempFolderPath.
     *
     * @return tempFolderPath
     */
    public String getTempFolderPath() {
        return this.tempFolderPath;
    }

    /**
     * Set the tempFolderPath.
     *
     * @param tempFolderPath tempFolderPath
     */
    public void setTempFolderPath(String tempFolderPath) {
        this.tempFolderPath = tempFolderPath;
    }

    /**
     * Get the kanbTotalList.
     *
     * @return kanbTotalList
     */
    public List<CPVIVF11KanbEntity> getKanbTotalList() {
        return this.kanbTotalList;
    }

    /**
     * Set the kanbTotalList.
     *
     * @param kanbTotalList kanbTotalList
     */
    public void setKanbTotalList(List<CPVIVF11KanbEntity> kanbTotalList) {
        this.kanbTotalList = kanbTotalList;
    }

    /**
     * Get the kanbDataList.
     *
     * @return kanbDataList
     */
    public List<CPVIVF11KanbEntity> getKanbDataList() {
        return this.kanbDataList;
    }

    /**
     * Set the kanbDataList.
     *
     * @param kanbDataList kanbDataList
     */
    public void setKanbDataList(List<CPVIVF11KanbEntity> kanbDataList) {
        this.kanbDataList = kanbDataList;
    }

    /**
     * Get the mailInvoiceList.
     *
     * @return mailInvoiceList
     */
    public List<CPVIVF11MailInvoiceEntity> getMailInvoiceList() {
        return this.mailInvoiceList;
    }

    /**
     * Set the mailInvoiceList.
     *
     * @param mailInvoiceList mailInvoiceList
     */
    public void setMailInvoiceList(List<CPVIVF11MailInvoiceEntity> mailInvoiceList) {
        this.mailInvoiceList = mailInvoiceList;
    }

}
