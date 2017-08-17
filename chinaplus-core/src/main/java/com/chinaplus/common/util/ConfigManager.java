/**
 * ConfigManager.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.common.util;

import java.util.ArrayList;
import java.util.List;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * The configuration files util.
 * 
 * @author ma_b
 */
public final class ConfigManager {

    // /** logger */
    // private static Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    /**
     * Get property value by property key from configuration file.
     * 
     * @param key the property key
     * @return the property value
     */
    public static String getProperty(String key) {
        return ConfigUtil.get(key);
    }

    /**
     * Get debug flag in config.properties.
     * 
     * @return true:this is debug
     */
    public static boolean isDebug() {
        return getProperty("debug.flag").equalsIgnoreCase("on");
    }

    /**
     * Get the download file path.
     * 
     * @return the download file path
     */
    public static String getDownloadPath() {
        return getProperty("common.file.downloadpath");
    }


    /**
     * Get the download file path.
     * 
     * @return the download file path
     */
    public static String getBatchFileSavePath() {
        return getProperty("common.file.batchfilesavepath");
    }

    /**
     * Get the download file path.
     * 
     * @return the download file path
     */
    public static String getUploadPath() {
        return getProperty("common.file.uploadpath");
    }

    /**
     * Get the temporary path for china plus system.
     * 
     * @return the temporary path
     */
    public static String getSystemTempPath() {
        return getProperty("common.temporarypath");
    }

    /**
     * Get the timezone of database.
     * 
     * @return the timezone of database
     */
    public static String getSystemDbTimezone() {
        return getProperty("system.database.timezone");
    }

    /**
     * Get the timezone of database.
     * 
     * @return the timezone of database
     */
    public static String getBatchStartDate() {
        return getProperty("batch.start.date");
    }

    /**
     * Get the timezone of database.
     * 
     * @return the timezone of database
     */
    public static String getBatchTransCount() {
        return getProperty("batch.trans.count");
    }
    
    /**
     * Get Maxsize of upload file from config.properties
     * 
     * @return max size of file that you can upload
     */
    public static long getUploadFileMaxSize() {
        try {
            String maxSizeStr = getProperty("common.uploadfile.maxsize");
            Long longNum = Long.parseLong(maxSizeStr);
            return longNum;
        } catch (Exception e) {
            return ChinaPlusConst.UPLOAD_MAX_SIZE;
        }
    }

    /**
     * Get the file path of failure file.
     * 
     * @return the timezone of database
     */
    public static String getFailureFilePath() {
        return getProperty("common.filespace.failure");
    }

    /**
     * Get MaxRecord of download file from config.properties
     * 
     * @return max size of file that you can upload
     */
    public static long getDownloadFileMaxRecore() {
        try {
            String maxRecoreStr = getProperty("common.downloadfile.maxrecord");
            Long longNum = Long.parseLong(maxRecoreStr);
            return longNum;
        } catch (Exception e) {
            return ChinaPlusConst.DOWNLOAD_MAX_RECORD;
        }
    }

    /**
     * Get system lauguage list from configration file.
     * 
     * @return the system lauguage list
     */
    public static List<String> getSystemLanguages() {
        // get system language list for check
        String systemLanguages = getProperty("system.language.list");
        // language list those wait to match with configurates
        List<String> waitMatchLanguages = new ArrayList<String>();
        // check language config
        if (!StringUtil.isEmpty(systemLanguages)) {
            String[] systemLanguageArray = systemLanguages.split(StringConst.COMMA);
            for (String language : systemLanguageArray) {
                if (!waitMatchLanguages.contains(language)) {
                    waitMatchLanguages.add(language);
                }
            }
        }
        return waitMatchLanguages;
    }

    /**
     * Get interface system by office code.
     * 
     * @param officeCode office code
     * @return interface system
     */
    public static String getInterfaceSystem(String officeCode) {

        String propertiesKey = StringUtil.toSafeString(officeCode).replaceAll(StringConst.COLON, StringConst.EMPTY);
        return StringUtil.toSafeString(getProperty(propertiesKey));
    }

}
