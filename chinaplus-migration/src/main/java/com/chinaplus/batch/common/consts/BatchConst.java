package com.chinaplus.batch.common.consts;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * BatchConst.
 * @author yang_jia1
 */
public interface BatchConst {

    /** batch user */
    public static final int BATCH_USER_ID = 0;

    /** "" */
    public static final String NULL_SPACE = "";

    /** " " */
    public static final String HALF_SPACE = " ";

    /** "　" */
    public static final String FULL_SPACE = "　";

    /** "," */
    public static final String COMMA = ",";

    /** "." */
    public static final String PERIOD = ".";

    /** "-" */
    public static final String HYPHEN = "-";

    /** "_" */
    public static final String DOWN_LINE = "_";

    /** "/" */
    public static final String SLASH = "/";

    /** Batch relation prefix */
    public static final String BATCH_RELATION_PREFIX = "batch.relation.";

    /** "0" */
    public static final String STRING_ZERO = "0";

    /** "1" */
    public static final String STRING_ONE = "1";

    /** "2" */
    public static final String STRING_TWO = "2";

    /** "3" */
    public static final String STRING_THREE = "3";

    /** "4" */
    public static final String STRING_FOUR = "4";

    /** "5" */
    public static final String STRING_FIVE = "5";

    /** "6" */
    public static final String STRING_SIX = "6";

    /** "7" */
    public static final String STRING_SEVEN = "7";

    /** "8" */
    public static final String STRING_EIGHT = "8";

    /** "9" */
    public static final String STRING_NINE = "9";

    /** "999" */
    public static final String STRING_NINENINENINE = "999";

    /** 0 */
    public static final int INT_ZERO = 0;

    /** 1 */
    public static final int INT_ONE = 1;

    /** 2 */
    public static final int INT_TWO = 2;
    
    /** 0 */
    public static final BigDecimal DEC_ZERO = new BigDecimal(0);

    /** 1 */
    public static final BigDecimal DEC_ONE = new BigDecimal(1);

    /** 2 */
    public static final BigDecimal DEC_TWO = new BigDecimal(2);
    
    /** 3 */
    public static final int INT_THREE = 3;

    /** 4 */
    public static final int INT_FOUR = 4;

    /** 5 */
    public static final int INT_FIVE = 5;

    /** 6 */
    public static final int INT_SIX = 6;

    /** 7 */
    public static final int INT_SEVEN = 7;

    /** 8 */
    public static final int INT_EIGHT = 8;

    /** 9 */
    public static final int INT_NINE = 9;

    /** 10 */
    public static final int INT_TEN = 10;

    /** 11 */
    public static final int INT_ELEVEN = 11;

    /** 12 */
    public static final int INT_TWELVE = 12;

    /** 13 */
    public static final int INT_THIRTEEN = 13;

    /** 14 */
    public static final int INT_FOURTEEN = 14;

    /** 15 */
    public static final int INT_FIFTEEN = 15;

    /** 16 */
    public static final int INT_SIXTEEN = 16;

    /** 17 */
    public static final int INT_SEVENTEEN = 17;

    /** 18 */
    public static final int INT_EIGHTEEN = 18;

    /** 19 */
    public static final int INT_NINETEEN = 19;

    /** 20 */
    public static final int INT_TWENTY = 20;

    /** 1024 */
    public static final int INT_1024 = 1024;

    /** 1900 */
    public static final int INT_1900 = 1900;

    /** 9999999 */
    public static final int INT_NINENNNNNN = 9999999;

    /** maxOrderQty */
    final static BigDecimal MAX_QTY = new BigDecimal(9999999999999.999);
    
    /** MORNING */
    final static String MORNING = "morning";

    /** AFTERNOON */
    final static String AFTERNOON = "afternoon";

    /** CRITICALTIME */
    final static String CRITICALTIME = "criticaltime";

    /**
     * Batch status.
     */
    public interface BatchStatus {

        /** Success */
        final static int SUCCESS = 0;

        /** Fail */
        final static int FAIL = 1;

        /** Running */
        final static int RUNNING = 9;
    }
    
    /**
     * Batch status.
     */
    public interface IFBatchStatus {

        /** Success */
        final static int SUCCESS = 1;

        /** Fail */
        final static int FAIL = 0;
        
        /** Fail */
        final static int INCOMPLETE = 2;
    }

    /**
     * interface sub batch id.
     */
    public interface IFBatchId {

        /** main batch */
        final static String SSMS_MAIN = "CPIIFB01";
        
        /** main batch */
        final static String SSMS_IFTABLESYNC = "CPIIFB15";
        
        /** main batch */
        final static String TTLOGIC_IFTABLESYNC = "CPIIFB16";
        
        /** main batch */
        final static String SSMS_COPYFILE = "CPIIFB17";
        
        /** main batch */
        final static String TTLOGIC_COPYFILE = "CPIIFB18";
        
        /** main batch */
        final static String TTLOGIC_MAIN = "CPIIFB02";
        
        /** ssms customer */
        final static String SSMS_CUSTOMER = "CPIIFB03";
        
        /** ssms parts */
        final static String SSMS_PARTS = "CPIIFB04";
        
        /** ssms parts */
        final static String SHIPPING_BYDAY = "CPIIFB19";
    }
    
    /**
     * bean id
     */
    public interface IFBeanId {

        /** ssms customer */
        final static String SMSS_CUSTOMER = "SMSSCustomer";
        
    }
    
    /**
     * interface service id.
     */
    public interface IFServiceId {

        /** ssms customer */
        final static String SMSS_CUSTOMER = "SMSSCustomerService";
        
        /** ssms parts */
        final static String SMSS_PARTS = "SMSSPartsService";
        
        /** ssms logic */
        final static String SMSS_LOGIC = "SMSSService";
        
        /** ssms logic */
        final static String SMSS_SYNC = "SMSSSyncService";
        
        /** ssms logic */
        final static String ORDER_PLAN = "ORDERPlan";
        
    }
    
    /** CPIIFB11Param */
    public interface InvenByDayParam {

        /** Stock Date */
        final static int DATE = 0;

        /** Office Code */
        final static int OFFICE_CODE = 1;

        /** Office Code */
        final static int SUB_PROCESS = 2;

        /** Stock Date */
        final static int IS_ONLINE = 3;
    }
    
    /**
     * 
     * VALID_FLAG.
     */
    public interface ValidFlag {
        /**  */
        final static int OTHER = 0;
        /**  */
        final static int SYSTEM_DATA = 1;
    }
    
    /**  orionPlusFlag. */
    public interface OrionPlusFlag {
        /**  */
        final static int NO = 0;
        /**  */
        final static int YES = 1;
    }
    
    /**
     * interface file name.
     */
    public interface IFBatchFileName {

        /** ssms customer */
        final static String SSMS_CUSTOMER = "MM06Z9X4";
        
        /** ssms parts */
        final static String SSMS_PARTS = "MM05Z9X4";

        /** ssms order */
        final static String SSMS_ORDER = "MM00Z9X4";
        
        /** ssms orderCancel */
        final static String SSMS_ORDERCANCEL = "MM01Z9X4";
        
        /** ssms exp inbound */
        final static String SSMS_EXP_INBOUND = "MM02Z9X4";
        
        /** ssms exp outbound */
        final static String SSMS_EXP_OUTBOUND = "MM03Z9X4";
        
        /** ssms invoice */
        final static String SSMS_INVOICE = "MM04Z9X4";
        
        /** TTLOGIC */
        final static String TTLOGIC = "MM15X4V1";
    }

    /** source */
    public interface Source {
        /** INBOUND */
        final static String INBOUND = "INBOUND";
        /** STOCKTRANSFER */
        final static String STOCKTRANSFER = "STOCKTRANSFER";
        /** DD */
        final static String DD = "DD";
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
    
    /** InboundSource */
    public interface InboundSource {
        /** OUTBOUND */
        final static String INBOUND = "INBOUND";
        /** DD */
        final static String DELETE = "D";
        /** ADD */
        final static String ADD_MODIFY = "";
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
    
    /** BatchTypeName */
    public interface BatchTypeName {
        
        /** SSMS */
        final static String SSMS = "SSMS";
        
        /** IP */
        final static String IP = "IP";
 
        /** BYDAY */
        final static String IM_BYDAY = "BYDAY";
    }
    
    /** StockBatchId */
    public interface StockBatchId{
        
        /** Rundown */
        final static String  CPSRDB01 = "CPSRDB01";
        
        /** Rundown make file */
        final static String  CPSRDB02 = "CPSRDB02";
        
        /** Stock Status */
        final static String  CPSSSB01 = "CPSSSB01";
    }
    
    /** StockCommonParam */
    public interface  StockCommonParam {
        
        /** Stock Date */
        final static int   IS_ONLINE = 0;
        
        /** Stock Date */
        final static int   STOCK_DATE = 1;
        
        /** Office Code */
        final static int   OFFICE_CODE = 2;
    }
    
    /** StockBatchId */
    public interface OnlineFlag{
        
        /** on line */
        final static int  ONLINE = 1;
        
        /** off line */
        final static int  OFFLINE = 2;
    }
    
    /**
     * Batch status.
     */
    public interface PartsStatus {

        /** Success */
        final static int NOT_REGISTERED = 1;

        /** Fail */
        final static int COMPLETED = 2;

        /** Running */
        final static int NOT_REQUIRED = 3;
    }
    
    /**
     * buildOutIndicator.
     */
    public interface BuildOutIndicator {

        /** Success */
        final static int YES = 1;

        /** Fail */
        final static int NO = 0;
    }

    /**
     * Sub process of inventory by day batch.
     */
    public interface SubBatchOfByDay {
        
        /** Inventory By Date */
        final static String INVENTORY_BY_DATE = "CPIIFB11";

        /** Imp Stock */
        final static String IMP_STOCK = "CPIIFB12";

        /** ImpBalance */
        final static String IMP_BALANCE = "CPIIFB13";

        /** ExpPartsStatus */
        final static String PARTS_STATUS = "CPIIFB14";
    }
    
    /**
     * OnlineBatch.
     */
    public interface OnlineBatch {
        
        /** stock status batch */
        final static String CPSSSB00 = "CPSSSB00";

        /** Interface batch */
        final static String CPIIFB00 = "CPIIFB00";
    }
    
    /**
     * OnlineBatch.
     */
    public interface MigrationIpStatus {

        /** stock status batch */
        final static int STOCK = 1;

        /** Interface batch */
        final static int DELIVERY = 2;

        /** Interface batch */
        final static int ECI = 8;

        /** Interface batch */
        final static int ONHOLD = 9;

        /** Interface batch */
        final static int CANCEL = 99;
    }

    /**
     * the Parameters of SSMSFile.
     */
    public interface SSMSFileType {
        
        /**
         * file type map
         */
        @SuppressWarnings("serial")
        static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>() {
            {
                put("MM00X4V1", "MM00Z9X4");
                put("MM01X4V1", "MM01Z9X4");
                put("MM02X4V1", "MM02Z9X4");
                put("MM03X4V1", "MM03Z9X4");
                put("MM04X4V1", "MM04Z9X4");
                put("MM05X4V1", "MM05Z9X4");
                put("MM06X4V1", "MM06Z9X4");
                put("MM07X4V1", "MM00Z9X4");
                put("MM08X4V1", "MM01Z9X4");
                put("MM09X4V1", "MM02Z9X4");
                put("MM10X4V1", "MM03Z9X4");
                put("MM11X4V1", "MM04Z9X4");
                put("MM12X4V1", "MM05Z9X4");
                put("MM13X4V1", "MM06Z9X4");
            }
        };
        /**
         * file type
         */
        static String[] ALL_FILE_TYPE = new String[] {
            "MM00X4V1",
            "MM01X4V1",
            "MM02X4V1",
            "MM03X4V1",
            "MM04X4V1",
            "MM05X4V1",
            "MM06X4V1",
            "MM07X4V1",
            "MM08X4V1",
            "MM09X4V1",
            "MM10X4V1",
            "MM11X4V1",
            "MM12X4V1",
            "MM13X4V1"
        };
    }
}
