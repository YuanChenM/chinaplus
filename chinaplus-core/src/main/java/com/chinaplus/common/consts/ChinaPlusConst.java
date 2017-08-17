/**
 * YanmarConst.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.common.consts;

import java.util.Locale;

/**
 * Yanmar Const.
 */
public final class ChinaPlusConst {

    /**
     * Table Sequence.
     */
    public interface Sequence {

        /** TNM_PARTS_MASTER */
        public static final String TNM_PARTS_MASTER = "SEQ_TNM_PARTS_MASTER";

        /** TNM_CUSTOMER */
        public static final String TNM_CUSTOMER = "SEQ_TNM_CUSTOMER";

        /** TNM_SUPPLIER */
        public static final String TNM_SUPPLIER = "SEQ_TNM_SUPPLIER";

        /** TNF_ORDER_STATUS */
        public static final String TNF_ORDER_STATUS = "SEQ_TNF_ORDER_STATUS";

        /** SEQ_TNT_INVOICE_HISTORY */
        public static final String TNT_INVOICE_HISTORY = "SEQ_TNT_INVOICE_HISTORY";

        /** TNT_KANBAN */
        public static final String TNT_KANBAN = "SEQ_TNT_KANBAN";

        /** TNT_KANBAN_PARTS */
        public static final String TNT_KANBAN_PARTS = "SEQ_TNT_KANBAN_PARTS";

        /** TNT_KANBAN_SHIPPING */
        public static final String TNT_KANBAN_SHIPPING = "SEQ_TNT_KANBAN_SHIPPING";

        /** TNT_KANBAN_SHIPPING_PARTS */
        public static final String TNT_KANBAN_SHIPPING_PARTS = "SEQ_TNT_KANBAN_SHIPPING_PARTS";

        /** TNT_KANBAN_PLAN */
        public static final String TNT_KANBAN_PLAN = "SEQ_TNT_KANBAN_PLAN";

        /** TNT_KANBAN_PLAN_PARTS */
        public static final String TNT_KANBAN_PLAN_PARTS = "SEQ_TNT_KANBAN_PLAN_PARTS";

        /** SEQ_TNT_PFC_MASTER */
        public static final String TNT_PFC_MASTER = "SEQ_TNT_PFC_MASTER";

        /** SEQ_TNT_PFC_DETAIL */
        public static final String TNT_PFC_DETAIL = "SEQ_TNT_PFC_DETAIL";

        /** SEQ_TNT_PFC_SHIPPING */
        public static final String TNT_PFC_SHIPPING = "SEQ_TNT_PFC_SHIPPING";
    }

    /**
     * The file id const define.
     */
    public interface FileId {

        /** CPOCSF01: */
        public static final String CPOCSF01 = "CPOCSF01";

        /** CPOCSF11 */
        public static final String CPOCSF11 = "CPOCSF11";

        /** CPMKBF01 */
        public static final String CPMKBF01 = "CPMKBF01";

        /** CPMKBF11 */
        public static final String CPMKBF11 = "CPMKBF11";

        /** CPMPMS02 */
        public static final String CPMPMS02 = "CPMPMS02";

        /** CPMSRF01 */
        public static final String CPMSRF01 = "CPMSRF01";

        /** CPMSRF11 */
        public static final String CPMSRF11 = "CPMSRF11";

        /** CPMSRF02 */
        public static final String CPMSRF02 = "CPMSRF02";

        /** CPMSRF12 */
        public static final String CPMSRF12 = "CPMSRF12";

        /** CPMSRF03 */
        public static final String CPMSRF03 = "CPMSRF03";

        /** CPMSRF13 */
        public static final String CPMSRF13 = "CPMSRF13";

        /** CPOCFS01 */
        public static final String CPOCFS01 = "CPOCFS01";

        /** CPVIVF01 */
        public static final String CPVIVF01 = "CPVIVF01";

        /** CPVIVF02 */
        public static final String CPVIVF02 = "CPVIVF02";

        /** CPVIVF03 */
        public static final String CPVIVF03 = "CPVIVF03";

        /** CPVIVF11 */
        public static final String CPVIVF11 = "CPVIVF11";

        /** CPVIVF12 */
        public static final String CPVIVF12 = "CPVIVF12";

        /** CPVIVF13 */
        public static final String CPVIVF13 = "CPVIVF13";

        /** CPMCMF01 */
        public static final String CPMCMF01 = "CPMCMF01";

        /** CPOCFF01 */
        public static final String CPOCFF01 = "CPOCFF01";

        /** CPOCFF03 */
        public static final String CPOCFF03 = "CPOCFF03";

        /** CPOCFF04 */
        public static final String CPOCFF04 = "CPOCFF04";

        /** CPKKPF01 */
        public static final String CPKKPF01 = "CPKKPF01";

        /** CPKKPF02 */
        public static final String CPKKPF02 = "CPKKPF02";

        /** CPKKPF11 */
        public static final String CPKKPF11 = "CPKKPF11";

        /** CPKKPF12 */
        public static final String CPKKPF12 = "CPKKPF12";

        /** CPMMAF01 */
        public static final String CPMMAF01 = "CPMMAF01";

        /** CPMMAF11 */
        public static final String CPMMAF11 = "CPMMAF11";

        /** CPMSPF01 */
        public static final String CPMSPF01 = "CPMSPF01";

        /** CPMSPF11 */
        public static final String CPMSPF11 = "CPMSPF11";

        /** CPMPMF01 */
        public static final String CPMPMF01 = "CPMPMF01";

        /** CPOOFF11 */
        public static final String CPOOFF11 = "CPOOFF11";

        /** CPMPMF11 */
        public static final String CPMPMF11 = "CPMPMF11";

        /** CPMPMS01 */
        public static final String CPMPMS01 = "CPMPMS01";

        /** CPSSMS01 */
        public static final String CPSSMS01 = "CPSSMS01";

        /** CPOCFS04 */
        public static final String CPOCFS04 = "CPOCFS04";

        /** CPOOCF01 */
        public static final String CPOOCF01 = "CPOOCF01";

        /** CPMSMF01 */
        public static final String CPMSMF01 = "CPMSMF01";

        /** CPMSMF11 */
        public static final String CPMSMF11 = "CPMSMF11";

        /** CPOCFS05 */
        public static final String CPOCFS05 = "CPOCFS05";

        /** CPSSMF11 */
        public static final String CPSSMF11 = "CPSSMF11";

        /** CPOCFF05 */
        public static final String CPOCFF05 = "CPOCFF05";

        /** CPSRDF11 */
        public static final String CPSRDF11 = "CPSRDF11";

        /** CPSDRF01 */
        public static final String CPSDRF01 = "CPSDRF01";

        /** CPSSSF01 */
        public static final String CPSSSF01 = "CPSSSF01";

        /** CPSSSF01 */
        public static final String CPSSSF02 = "CPSSSF02";

        /** CPSRDF01 */
        public static final String CPSRDF01 = "CPSRDF01";

        /** CPSRDF02 */
        public static final String CPSRDF02 = "CPSRDF02";

        /** CPSSMS02 */
        public static final String CPSSMS02 = "CPSSMS02";

        /** CPOOHF01 */
        public static final String CPOOHF01 = "CPOOHF01";

        /** CPSSMF01 */
        public static final String CPSSMF01 = "CPSSMF01";

        /** CPSSMF02 */
        public static final String CPSSMF02 = "CPSSMF02";

        /** CPSSMF03 */
        public static final String CPSSMF03 = "CPSSMF03";

        /** CPOCFS06 */
        public static final String CPOCFS06 = "CPOCFS06";

        /** CPOCFF06 */
        public static final String CPOCFF06 = "CPOCFF06";

        /** CPMSMS01 */
        public static final String CPMSMS01 = "CPMSMS01";

        /** CPIIFB01 */
        public static final String CPIIFB01 = "CPIIFB01";

        /** VVPSDF01 */
        public static final String VVPSDF01 = "VVPSDF01";

        /** VVPSLF01 */
        public static final String VVPSLF01 = "VVPSLF01";

    }

    /**
     * Download Consts.
     */
    public interface DownloadConst {

        /** style */
        public static final String STYLE_SHEET_NAME = "style";

        /** decimal style */
        public static final String DECIMAL_STYLE_SHEET_NAME = "decimalStyle";

        /** decimal style */
        public static final String SHEET_NAME_SUFFIX_EN = "_en";

        /** decimal style */
        public static final String SHEET_NAME_SUFFIX_CN = "_cn";
    }

    /**
     * Upload Consts.
     */
    public interface UploadConst {

        /** upload process : check */
        final static String PARAM_KEY_UPLOAD_PROCESS = "uploadProcess";

        /** upload process : check */
        final static String PARAM_KEY_SESSION_KEY = "sessionKey";

        /** upload process : check */
        final static String UPLOAD_PROCESS_CHECK = "check";

        /** upload process : confirmed */
        final static String UPLOAD_PROCESS_CONFIRMED = "yes";

        /** upload process : unconfirmed */
        final static String UPLOAD_PROCESS_UNCONFIRMED = "no";
    }

    /** InboundSource */
    public interface InboundSource {
        /** OUTBOUND */
        final static String INBOUND = "INBOUND";
        /** DD */
        final static String DELETE = "D";
        /** ADD */
        final static String ADD_MODIFY = "";
    }

    /** OutboundSource */
    public interface OutboundSource {
        /** OUTBOUND */
        final static String OUTBOUND = "OUTBOUND";
        /** DD */
        final static String DELETE = "D";
        /** ADD */
        final static String ADD_MODIFY = "";
    }

    /**
     * Property config.
     */
    public interface Properties {

        /** Temporary Path */
        public static final String TEMPORARY_PATH = "common.temporarypath";

        /** Upload Path: Kanban */
        public static final String UPLOAD_PATH_KANBAN = "common.uploadpath.kanban";

        /** Upload Path: Invoice */
        public static final String UPLOAD_PATH_INVOICE = "common.uploadpath.invoice";

        /** Upload Path: CFC */
        public static final String UPLOAD_PATH_CFC = "common.uploadpath.cfc";

        /** Upload Path: PFC */
        public static final String UPLOAD_PATH_PFC = "common.uploadpath.pfc";

        /** MAIL_SMTP_HOST */
        public static final String MAIL_SMTP_HOST = "mail.smtp.host";

        /** MAIL_SMTP_PORT */
        public static final String MAIL_SMTP_PORT = "mail.smtp.port";

        /** MAIL_SMTP_AUTH */
        public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

        /** MAIL_FROM_ADDRESS */
        public static final String MAIL_FROM_ADDRESS = "mail.from.address";

        /** MAIL_FROM_PASSWORD */
        public static final String MAIL_FROM_PASSWORD = "mail.from.password";

        /** MAIL_USE_SSL */
        public static final String MAIL_USE_SSL = "mail.use.ssl";

        /** RUN_SHELL_PATH */
        public static final String RUN_SHELL_PATH = "run.shell.path";

        /** RUN_SHELL_SYNCHRONIZE_NOW_DATA */
        public static final String RUN_SHELL_SYNCHRONIZE_NOW_DATA = "run.shell.synchronize.now.data";

        /** RUN_SHELL_STOCKSTATUS_NOW_DATA */
        public static final String RUN_SHELL_STOCKSTATUS_NOW_DATA = "run.shell.stockstatus.now.data";
    }

    /**
     * Resource Type.
     */
    public static enum ResourceType {
        /** label. */
        label,
        /** message. */
        message,
        /** validator. */
        validator,
        /** file. */
        file;
    }

    /** Language. */
    public enum Language {

        /** ENGLISH. */
        ENGLISH(1, "en_US", Locale.US),
        /** CHINESE. */
        CHINESE(2, "zh_CN", Locale.SIMPLIFIED_CHINESE);

        /**
         * code.
         */
        private int code;
        /**
         * name.
         */
        private String name;
        /**
         * locale.
         */
        private Locale locale;

        /**
         * 
         * The Constructors Method.
         * 
         * @param pCode code
         * @param pName name
         * @param pLocale locale
         */
        Language(int pCode, String pName, Locale pLocale) {
            this.code = pCode;
            this.name = pName;
            this.locale = pLocale;
        }

        /**
         * get code.
         * 
         * @return code
         */
        public int getCode() {
            return this.code;
        }

        /**
         * get name.
         * 
         * @return name
         */
        public String getName() {
            return this.name;
        }

        /**
         * get locale.
         * 
         * @return locale
         */
        public Locale getLocale() {
            return this.locale;
        }
    }

    /** BatchType */
    public interface BatchType {

        /** SSMS */
        final static int SSMS = 1;

        /** IP */
        final static int IP = 2;

        /** BYDAY */
        final static int IM_BYDAY = 3;
    }

    /** Excel max line num */
    public static final int EXCEL_MAX_LINE_NUM = 65000;

    /** Excel 2003 max row count */
    public static final int EXCEL_2003_MAX_ROW_COUNT = 65536;

    /** Excel 2003 max column count */
    public static final int EXCEL_2003_MAX_COLUMN_COUNT = 256;

    /** Excel 2007 max row count */
    public static final int EXCEL_2007_MAX_ROW_COUNT = 1048576;

    /** Excel 2007 max column count */
    public static final int EXCEL_2007_MAX_COLUMN_COUNT = 16384;

    /** the max size of the upload file - 10MB */
    public static final long UPLOAD_MAX_SIZE = 2048000; // ConfigManager.getFileMaxSize() * 1024L * 1024L;

    /** the max record of the download file - 10000 */
    public static final long DOWNLOAD_MAX_RECORD = 10000;

    /** DEFALUT_LANG_LOCALE. */
    public static final int DEFALUT_LANG_CODE = 1;

    /** DEFALUT_LANG_NAME. */
    public static final String DEFALUT_LANG_NAME = "en_US";

    /** DEFALUT_LANG_LOCALE. */
    public static final Locale DEFALUT_LANG_LOCALE = Locale.US;

    /** max qty */
    public static final String MAX_QTY = "9999999999.999999";

    /** batch user */
    public static final int BATCH_USER_ID = 0;
    
    /** TTC_IMP_WH_CODE_DIRECT_SENDING */
    public static final String IMP_WH_CODE_DIRECT_SENDING = "999999999";
}
