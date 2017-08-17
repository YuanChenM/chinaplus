/**
 * CPKKPF12Entity.java
 * 
 * @screen CPKKPF12
 * @author shiyang
 */
package com.chinaplus.web.kbp.entity;

import java.util.Locale;

import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Kanban Plan Upload Entity.
 */
public class CPKKPF12Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Upload Result */
    private String uploadResult;

    /** Kanban Plan No. */
    private String kanbanPlanNo;

    /** Sheet Name */
    private String sheetName;

    /** Language */
    private Locale language;

    /** Language Index */
    private int languageIndex;

    /**
     * Get the uploadResult.
     *
     * @return uploadResult
     */
    public String getUploadResult() {
        return this.uploadResult;
    }

    /**
     * Set the uploadResult.
     *
     * @param uploadResult uploadResult
     */
    public void setUploadResult(String uploadResult) {
        this.uploadResult = uploadResult;
    }

    /**
     * Get the kanbanPlanNo.
     *
     * @return kanbanPlanNo
     */
    public String getKanbanPlanNo() {
        return this.kanbanPlanNo;
    }

    /**
     * Set the kanbanPlanNo.
     *
     * @param kanbanPlanNo kanbanPlanNo
     */
    public void setKanbanPlanNo(String kanbanPlanNo) {
        this.kanbanPlanNo = kanbanPlanNo;
    }

    /**
     * Get the sheetName.
     *
     * @return sheetName
     */
    public String getSheetName() {
        return this.sheetName;
    }

    /**
     * Set the sheetName.
     *
     * @param sheetName sheetName
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * Get the language.
     *
     * @return language
     */
    public Locale getLanguage() {
        return this.language;
    }

    /**
     * Set the language.
     *
     * @param language language
     */
    public void setLanguage(Locale language) {
        this.language = language;
    }

    /**
     * Get the languageIndex.
     *
     * @return languageIndex
     */
    public int getLanguageIndex() {
        return this.languageIndex;
    }

    /**
     * Set the languageIndex.
     *
     * @param languageIndex languageIndex
     */
    public void setLanguageIndex(int languageIndex) {
        this.languageIndex = languageIndex;
    }
}
