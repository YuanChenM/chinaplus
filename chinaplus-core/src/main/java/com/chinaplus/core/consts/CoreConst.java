/**
 * CoreConst.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.consts;

import java.util.Locale;

/**
 * The utility class of core constant definition.
 */
public interface CoreConst {

    /** Change Line. */
    public static final String CHANGE_LINE = "\r\n";

    /** SUFFIX_JSON */
    public static final String SUFFIX_JSON = ".json";

    /** SUFFIX_EXCEL */
    public static final String SUFFIX_EXCEL = ".xlsx";

    /** SUFFIX_EXCEL(xlsm) */
    public static final String SUFFIX_EXCEL_M = ".xlsm";

    /** The Constant SESSION TOKEN. */
    public static final String KEY_SESSION_TOKEN = "token";

    /** The Constant SESSION TOKEN. */
    public static final String KEY_SESSION_SCREEN = "screenId";

    /** The Constant KEY_SESSION_CLIENTTIME. */
    public static final String KEY_SESSION_CLIENTTIME = "clientTime";

    /** The Constant BASE_PROPERTIES_PATH. */
    public final static String BASE_PROPERTIES_PATH = "i18n/";

    /**
     * Name space of JavaScript framework file.
     */
    public static final String JAVASCRIPT_FRAMEWORK_NAMESPACE = "TRF";

    /**
     * Default local(English).Return this if don't have language settings.
     */
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    /**
     * The key of excel data in ModelAndView.
     */
    public static final String KEY_EXCEL_DATA = "KEY_EXCEL_DATA";

    /**
     * The key of local data in ModelAndView.
     */
    public static final String KEY_LOCALE = "KEY_LOCALE";

    /**
     * The key of file name in ModelAndView.
     */
    public static final String KEY_FILENAME = "KEY_FILENAME";

    /**
     * The key of file name in ModelAndView.
     */
    //public static final Integer PROJECT_FLAG = 1;

    /**
     * The key of file name in JSONDATA.
     */
    public static final String JSONDATA = "jsonData";

    /**
     * The key of file name in CPV00000.
     */
    public static final String CPV00000 = "CPV00000";
    
    /**
     * The key of file name in VV.
     */
    public static final String  VV = "VV";
    
    /**
     * The key of file name in CPA00000.
     */
    public static final String CPA00000 = "CPA00000";
    
    /**
     * The key of file name in CPC00000.
     */
    public static final String CPC00000 = "CPC00000";
    
    /**
     * The key of file name in CPM00000.
     */
    public static final String CPM00000 = "CPM00000";
    
    /**
     * The key of file name in VVP0000.
     */
    public static final String VVP0000 = "VVP0000";
    
    /**
     * The key of file name in AISIN.
     */
    public static final String AISIN = "AISIN";
    
    
    /**
     * UrlJson.
     */
    public interface UrlJson {
        /**
         * Path of JSON setting file(defult).
         */
        public static final String URL_JSON_DEFULT = "/WEB-INF/";

        /**
         * Path of JSON setting file(defult).
         */
        public static final String URL_JSON_FILE_DEFULT = "/WEB-INF/json/";

        /**
         * Path of JSON setting file(order).
         */
        public static final String URL_JSON_ORDER = "/WEB-INF/json/order/";

        /**
         * Path of JSON setting file(warehouse).
         */
        public static final String URL_JSON_WAREHOUSE = "/WEB-INF/json/warehouse/";

        /**
         * Path of JSON setting file(master).
         */
        public static final String URL_JSON_MASTER = "/WEB-INF/json/master/";

        /**
         * Path of JSON setting file(common).
         */
        public static final String URL_JSON_COMMON = "/WEB-INF/json/manage/";
    }

    /**
     * Like condition modes.
     */
    public enum LikeMode {
        /** front match */
        FRONT,
        /** front match */
        BEHIND,
        /** partial match */
        PARTIAL
    }

    /**
     * The enumerable Direction.
     */
    public enum Direction {

        /** The Left. */
        Left,
        /** The Right. */
        Right,
        /** The Both. */
        Both
    }

}
