/**
 * ExcelConfig.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.bean;

import com.chinaplus.core.base.BaseEntity;

/**
 * The configuration for upload excel.
 */
public class ExcelConfigItem extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = -5565495781739216121L;

    /** sheet name */
    private String sheetName;
    /** sheet index */
    private int sheetIndex;
    /** true: config for all sheet */
    private boolean forAllSheet;
    /** cell row number */
    private int cellRow;
    /** cell column number */
    private int cellCol;
    /** cell static resource key */
    private String resourceKey;

    /**
     * The Constructors Method.
     */
    public ExcelConfigItem() {
        forAllSheet = false;
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
     * Get the sheetIndex.
     *
     * @return sheetIndex
     */
    public int getSheetIndex() {
        return this.sheetIndex;
    }

    /**
     * Set the sheetIndex.
     *
     * @param sheetIndex sheetIndex
     */
    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    /**
     * Get the forAllSheet.
     *
     * @return forAllSheet
     */
    public boolean isForAllSheet() {
        return this.forAllSheet;
    }

    /**
     * Set the forAllSheet.
     *
     * @param forAllSheet forAllSheet
     */
    public void setForAllSheet(boolean forAllSheet) {
        this.forAllSheet = forAllSheet;
    }

    /**
     * Get the cellRow.
     *
     * @return cellRow
     */
    public int getCellRow() {
        return this.cellRow;
    }

    /**
     * Set the cellRow.
     *
     * @param cellRow cellRow
     */
    public void setCellRow(int cellRow) {
        this.cellRow = cellRow;
    }

    /**
     * Get the cellCol.
     *
     * @return cellCol
     */
    public int getCellCol() {
        return this.cellCol;
    }

    /**
     * Set the cellCol.
     *
     * @param cellCol cellCol
     */
    public void setCellCol(int cellCol) {
        this.cellCol = cellCol;
    }

    /**
     * Get the resourceKey.
     *
     * @return resourceKey
     */
    public String getResourceKey() {
        return this.resourceKey;
    }

    /**
     * Set the resourceKey.
     *
     * @param fileId file id
     * @param resourceKey resourceKey
     */
    public void setResourceKey(String fileId, String resourceKey) {
        this.resourceKey = resourceKey; // StringUtil.formatMessage("{0}_{1}", fileId, resourceKey);
    }
}
