/**
 * ExcelConfig.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.core.util.StringUtil;

/**
 * The configuration for upload excel.
 */
public class ExcelConfig {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ExcelConfig.class);

    /** excel file id */
    private String fileId;
    /** sheet count in excel */
    private int sheetCount;
    /** configuration items */
    private Map<String, ExcelConfigItem> items;

    /**
     * The Constructors Method.
     *
     * @param fileId excel file id
     */
    public ExcelConfig(String fileId) {
        this.fileId = fileId;
        items = new HashMap<String, ExcelConfigItem>();
    }

    /**
     * Get the fileId.
     *
     * @return fileId
     */
    public String getFileId() {
        return this.fileId;
    }

    /**
     * Get the sheetCount.
     *
     * @return sheetCount
     */
    public int getSheetCount() {
        return this.sheetCount;
    }

    /**
     * Set the sheetCount.
     *
     * @param sheetCount sheetCount
     */
    public void setSheetCount(int sheetCount) {
        this.sheetCount = sheetCount;
    }

    /**
     * Get the items.
     *
     * @return items
     */
    public Collection<ExcelConfigItem> getItems() {
        return this.items.values();
    }

    /**
     * Set the items.
     *
     * @param items items
     */
    public void setItems(List<ExcelConfigItem> items) {
        if (items != null) {
            if (this.items == null) {
                this.items = new HashMap<String, ExcelConfigItem>();
            }

            for (ExcelConfigItem item : items) {
                String key = getItemKey(item);
                if (!StringUtil.isEmpty(key)) {
                    if (logger.isWarnEnabled()) {
                        if (this.items.containsKey(key)) {
                            logger.warn("Override excel configuration:{}", key);
                        }
                    }
                    this.items.put(key, item);
                }
            }
        }
    }

    /**
     * Add a item.
     *
     * @param item item
     */
    public void addItem(ExcelConfigItem item) {
        if (item != null) {
            if (this.items == null) {
                this.items = new HashMap<String, ExcelConfigItem>();
            }
            String key = getItemKey(item);
            if (!StringUtil.isEmpty(key)) {
                if (logger.isWarnEnabled()) {
                    if (this.items.containsKey(key)) {
                        logger.warn("Override excel configuration:{}", key);
                    }
                }
                this.items.put(key, item);
            }
        }
    }

    /**
     * Get the cell resource key from excel configuration by cell information.
     * 
     * @param sheetName the sheet name
     * @param cellRow the cell row number
     * @param cellCol the cell column number
     * @return resource key
     */
    public String getItemResourceKey(String sheetName, int cellRow, int cellCol) {
        String resourceKey = null;

        if (this.items != null) {
            ExcelConfigItem item = this.items.get(getItemKey(sheetName, cellRow, cellCol));
            if (item != null) {
                resourceKey = item.getResourceKey();
            }
        }

        return resourceKey;
    }

    /**
     * Get the key of this excel configuration item.
     * 
     * @param item excel configuration item
     * @return configuration key
     */
    private String getItemKey(ExcelConfigItem item) {
        String key = "";
        if (item != null) {
            key = getItemKey(item.getSheetName(), item.getCellRow(), item.getCellCol());
        }
        return key;
    }

    /**
     * Get the key of this excel configuration item.
     * 
     * @param sheetName sheet name
     * @param cellRow cell row number
     * @param cellCol cell column number
     * @return configuration key
     */
    private String getItemKey(String sheetName, int cellRow, int cellCol) {
        return StringUtil.formatMessage("[{0}]({1},{2})", StringUtil.toSafeString(sheetName), cellRow, cellCol);
    }
}
