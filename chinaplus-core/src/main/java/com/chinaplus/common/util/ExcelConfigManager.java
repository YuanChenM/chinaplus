/**
 * ExcelConfigManager.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.ExcelConfig;
import com.chinaplus.core.bean.ExcelConfigItem;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * The excel configuration util.
 */
public final class ExcelConfigManager {

    /** the key prefix in excel.properties */
    private static final String KEY_PREFIX = "excel.static.key.";
    /** the key in config.properties for system languages */
    private static final String KEY_LANGUAGES = "system.language.list";
    /** the key regex */
    private static final String KEY_REGEX = ("^" + KEY_PREFIX + "(.*)(\\.\\[)(.*)(\\?)(.*)(\\]\\()([0-9]+)(,)([0-9]+)(\\))$");
    /** the sheet count regex */
    private static final String SHEETCOUNT_REGEX = ("^" + KEY_PREFIX + "(.*)(\\.sheetcount)$");
    /** the index of file id in properties key */
    private static final int INDEX_FILEID = 1;
    /** the index of sheet type(name or index) in properties key */
    private static final int INDEX_SHEETTYPE = 3;
    /** the index of sheet name or sheet index in properties key */
    private static final int INDEX_SHEETNAME = 5;
    /** the index of cell row number in properties key */
    private static final int INDEX_CELLROW = 7;
    /** the index of cell column number in properties key */
    private static final int INDEX_CELLCOL = 9;
    /** the char for sheet name */
    private static final String SHEETTYPE_NAME = "NAME";
    /** the char for sheet index */
    private static final String SHEETTYPE_INDEX = "INDEX";

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ExcelConfigManager.class);

    /**
     * the excel configurations
     * Key is excel file id
     */
    private static Map<String, ExcelConfig> configs;

    /**
     * Set a excel configuration.
     * 
     * @param config excel configuration
     */
    public static void setConfig(ExcelConfig config) {
        if (config != null) {
            if (configs == null) {
                configs = new HashMap<String, ExcelConfig>();
            }

            // key: excel file id
            // value: excel file configuration
            configs.put(config.getFileId(), config);
        }
    }

    /**
     * Set excel configuration by config properties.
     * 
     * @param configPropKey configuration properties key
     * @param configPropValue configuration properties value
     */
    public static void setConfig(String configPropKey, String configPropValue) {

        // memory excel configurations in excel config util
        if (configs == null) {
            configs = new HashMap<String, ExcelConfig>();
        }

        // get excel configurations from configuration key and value
        if (StringUtil.matchs(configPropKey, SHEETCOUNT_REGEX)) {
            // sheet count configurations
            Pattern p = Pattern.compile(SHEETCOUNT_REGEX);
            Matcher m = p.matcher(configPropKey);
            if (m.find()) {
                String fileId = m.group(INDEX_FILEID);

                // check sheet count
                if (!StringUtil.isNumeric(configPropValue)) {
                    BaseMessage message = new BaseMessage(
                        "The sheet count configuration for \"{0}\" file is illegal:{1}"); // TODO
                    message.setMessageArgs(new String[] { fileId, configPropValue });
                    throw new BusinessException(message);
                }

                // set sheet count
                if (!configs.containsKey(fileId)) {
                    configs.put(fileId, new ExcelConfig(fileId));
                }
                ExcelConfig config = configs.get(fileId);
                config.setSheetCount(Integer.parseInt(configPropValue));
            }
        } else if (StringUtil.matchs(configPropKey, KEY_REGEX)) {
            // static items configurations
            Pattern p = Pattern.compile(KEY_REGEX);
            Matcher m = p.matcher(configPropKey);
            if (m.find()) {
                String fileId = m.group(INDEX_FILEID);

                if (!configs.containsKey(fileId)) {
                    configs.put(fileId, new ExcelConfig(fileId));
                }
                ExcelConfig config = configs.get(fileId);

                ExcelConfigItem item = new ExcelConfigItem();
                String sheetType = m.group(INDEX_SHEETTYPE);
                String sheetNameOrIndex = m.group(INDEX_SHEETNAME);
                if (SHEETTYPE_NAME.equalsIgnoreCase(sheetType)) {
                    // sheet name
                    item.setSheetName(sheetNameOrIndex);
                } else if (SHEETTYPE_INDEX.equalsIgnoreCase(sheetType)) {
                    // sheet index
                    if (!StringUtil.isNumeric(sheetNameOrIndex)) {
                        BaseMessage message = new BaseMessage("The configuration for \"{0}\" file is illegal:{1}"); // TODO
                        message.setMessageArgs(new String[] { fileId, configPropValue });
                        throw new BusinessException(message);
                    }
                    item.setSheetIndex(Integer.valueOf(sheetNameOrIndex));
                } else if (StringUtil.isEmpty(sheetType) && StringUtil.isEmpty(sheetNameOrIndex)) {
                    // all sheet
                    item.setForAllSheet(true);
                } else {
                    BaseMessage message = new BaseMessage("The configuration for \"{0}\" file is illegal:{1}"); // TODO
                    message.setMessageArgs(new String[] { fileId, configPropValue });
                    throw new BusinessException(message);
                }
                item.setCellRow(Integer.parseInt(m.group(INDEX_CELLROW)));
                item.setCellCol(Integer.parseInt(m.group(INDEX_CELLCOL)));
                item.setResourceKey(fileId, configPropValue);
                config.addItem(item);
            }
        }
    }

    /**
     * Get the excel configuration by excel file id.
     * 
     * @param fileId excel file id
     * @return the excel configuration
     */
    public static ExcelConfig getConfig(String fileId) {
        ExcelConfig config = null;
        if (configs != null && configs.containsKey(fileId)) {
            config = configs.get(fileId);
        }
        return config;
    }

    /**
     * Check excel configuration by key.
     * 
     * @param configPropKey properties key
     * @return true is excel configuration
     */
    public static boolean isExcelConfig(String configPropKey) {
        // return StringUtil.matchs(configPropKey, KEY_REGEX);
        return StringUtil.toSafeString(configPropKey).startsWith(KEY_PREFIX);
    }

    /**
     * check file format (static contents) by excel configurations.
     * 
     * @param fileId excel file id
     * @param workbook check target excel
     * @return result message (when there is no error return null)
     * @exception BusinessException the exception
     */
    public static List<BaseMessage> checkStaticItems(String fileId, Workbook workbook) throws BusinessException {
        List<BaseMessage> messages = new ArrayList<BaseMessage>();

        // get excel config by file id
        ExcelConfig config = ExcelConfigManager.getConfig(fileId);
        if (config == null) {
            // if no configration, then do not check
            return null;
        }

        // check sheet count by configuration
        if (config.getSheetCount() > 0) {
            if (workbook.getNumberOfSheets() > config.getSheetCount()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_024);
                message.setMessageArgs(new String[] { StringUtil.toSafeString(config.getSheetCount()) });
                throw new BusinessException(message);
            }
        }

        // get system language list for check
        // language list those wait to match with configurates
        List<String> waitMatchLanguages = ConfigManager.getSystemLanguages();
        // check language config
        if (waitMatchLanguages == null || waitMatchLanguages.isEmpty()) {
            logger.warn("\"{}\" is not found in config.properties. There will use the default language [en_US].",
                KEY_LANGUAGES);
            if (waitMatchLanguages == null) {
                waitMatchLanguages = new ArrayList<String>();
            }
            waitMatchLanguages.add(ChinaPlusConst.Language.ENGLISH.getName());
        }

        // check excel config
        if (config == null || config.getItems() == null || config.getItems().isEmpty()) {
            BaseMessage message = new BaseMessage("The configuration for \"{0}\" file, is not found."); // TODO
            message.setMessageArgs(new String[] { fileId });
            throw new BusinessException(message);
        }

        // check all static items these are definded in configuration
        // if there is at least one language that can match all items, then this excel file format is ok.
        Collection<ExcelConfigItem> configItems = config.getItems();
        for (ExcelConfigItem configItem : configItems) {
            Sheet sheet = null;

            List<Integer> sheetIndexs = new ArrayList<Integer>();
            if (configItem.isForAllSheet()) {
                // check this cell in all sheet
                int sheetCount = workbook.getNumberOfSheets();
                for (int index = 0; index < sheetCount; index++) {
                    sheetIndexs.add(index);
                }
            } else if (!StringUtil.isEmpty(configItem.getSheetName())) {
                int index = workbook.getSheetIndex(configItem.getSheetName());
                if (index >= 0) {
                    sheetIndexs.add(index);
                }
            } else {
                sheetIndexs.add(configItem.getSheetIndex());
            }
            if (sheetIndexs.isEmpty()) {
                // if sheet is not found, return message
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_006);
                // message.setMessageArgs(new String[] { workbook.getSheetAt(0).getSheetName() });
                messages.add(message);
                return messages;
            }

            // check staitc cell for all sheets that configuated
            for (int index : sheetIndexs) {

                sheet = workbook.getSheetAt(index);
                if (sheet == null) {
                    // if sheet is not found, return message
                    BaseMessage message = new BaseMessage("The sheet [{}] is not found in excel."); // TODO
                    message.setMessageArgs(new String[] { StringUtil.isEmpty(configItem.getSheetName()) ? StringUtil
                        .toSafeString(index) : configItem.getSheetName() });
                    messages.add(message);
                    return messages;
                }

                // get cell string value
                String cellValue = PoiUtil.getStringCellValue(sheet, configItem.getCellRow(), configItem.getCellCol());

                // check cell string value by matched languages
                boolean matchFlag = false;
                List<String> unmatchedLanguages = new ArrayList<String>();
                for (String language : waitMatchLanguages) {
                    // get resource value from i18n properites
                    String i18nValue = MessageManager.getMessage(configItem.getResourceKey(), language).replaceAll(
                        "<br>", "\n");
                    if (StringUtil.equals(cellValue, i18nValue)) {
                        matchFlag = true;
                    } else {
                        // add this language to ummatched language list
                        unmatchedLanguages.add(language);
                    }
                }
                // if this cell value didn't matched in all language, add message to result
                if (!matchFlag) {
                    // BaseMessage message = new BaseMessage(
                    // "The static cell's value is illegal. sheet[{0}] cell({1},{2})={3}"); // TODO
                    // message.setMessageArgs(new String[] { sheet.getSheetName(),
                    // StringUtil.toString(configItem.getCellRow()), StringUtil.toString(configItem.getCellCol()),
                    // cellValue });
                    if (config.getSheetCount() != 1) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1014);
                        // message.setMessageArgs(new String[] { sheet.getSheetName() });
                        messages.add(message);
                        return messages;
                    } else {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1014);
                        messages.add(message);
                        return messages;
                    }
                }

                // remove unmatchedF language from matched language list
                if (!unmatchedLanguages.isEmpty()) {
                    for (String language : unmatchedLanguages) {
                        waitMatchLanguages.remove(language);
                    }
                }
            }
        }

        return messages;
    }
}
