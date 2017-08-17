/**
 * CodeConst.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.common.consts;

/**
 * CodeMasterConst.
 */
public interface CodeConst {

    /**
     * TNM_USER.
     */
    public interface User {

        /**
         * TNM_USER STATUS.
         */
        public interface Status {

            /** inactive */
            public static final int INACTIVE = 0;

            /** active */
            public static final int ACTIVE = 1;

            /** locked */
            public static final int LOCKED = 9;

        }
    }

    /**
     * ACCESS_LEVEL.
     */
    public interface AccessLevel {
        /** no authority */
        public static final int NONE = 0;

        /** general staff:low authority */
        public static final int LOW = 1;

        /** pic:medium low authority */
        public static final int MEDIUM_LOW = 2;

        /** invoice upload:medium high authority */
        public static final int MEDIUM_HIGH = 3;

        /** manager:high authority */
        public static final int HIGH = 4;

        /** maintainer:system administrator */
        public static final int MAINTAINERS = 5;
    }

    /**
     * Auth_Type.
     */
    public interface AuthType {

        /** check by office id */
        public static final int OFFICE = 1;

        /** check by all office */
        public static final int ALL = 2;

        /** Any auth could do */
        public static final int NON = 3;
    }

    /**
     * ActiveFlag.
     */
    public interface ActiveFlag {

        /** y */
        public static final int Y = 1;

        /** n */
        public static final int N = 0;
    }

    /**
     * InactiveFlag.
     */
    public interface InactiveFlag {

        /** Active */
        public static final int ACTIVE = 0;

        /** Inactive */
        public static final int INACTIVE = 1;
    }

    /**
     * PartsStatus.
     */
    public interface PartsStatus {

        /** Not Registered */
        public static final int NOT_REGISTERED = 1;

        /** Completed */
        public static final int COMPLETED = 2;

        /** Not Required */
        public static final int NOT_REQUIRED = 3;

    }

    /**
     * Kanban PartsStatus.
     */
    public interface KanbanPartsStatus {

        /** Pending */
        public static final int PENDING = 1;

        /** Inbound Completed */
        public static final int INBOUND_COMPLETED = 2;

        /** Completed */
        public static final int COMPLETED = 3;

        /** Force Completed */
        public static final int FORCE_COMPLETED = 9;

    }

    /**
     * CustStockStatus.
     */
    public interface CustStockStatus {

        /** Active */
        public static final int ACTIVE = 1;

        /** Past */
        public static final int PAST = 0;

    }


    /**
     * CustStockStatus.
     */
    public interface DtStatus {

        /** Active */
        public static final int ACTIVE = 1;

        /** Past */
        public static final int CANCELL = 99;

    }

    /**
     * CompletedFlag.
     */
    public interface CompletedFlag {

        /** Active */
        public static final int NORMAL = 0;

        /** Past */
        public static final int COMPLETED = 1;
    }

    /**
     * not in rundown flag.
     */
    public interface NirdFlag {

        /** Active */
        public static final int NORMAL = 0;

        /** Past */
        public static final int NOT_IN_RUNDOWN = 1;

    }

    /**
     * OrderForecastStatus.
     */
    public interface OrderForecastStatus {

        /** Normal */
        public static final int NORMAL = 1;

    }

    /**
     * ON-HOLD FLAG.
     */
    public interface OnHoldFlag {

        /** On-hold */
        public static final int NG_ON_HOLD = 1;

        /** On-Hold ECI */
        public static final int ECI_ON_HOLD = 2;

        /** Normal */
        public static final int NORMAL = 3;

    }

    /**
     * Ip Status.
     */
    public interface ActionType {

        /** Customs Clearance */
        public static final int CUSTOMS_CLEARANCE = 1;

        /** Devanned */
        public static final int DEVANNED = 2;

        /** Imp Inbound */
        public static final int IMP_INBOUND = 3;

        /** WHS Transfer */
        public static final int WHS_TRANSFER = 4;

        /** Stock Transfer */
        public static final int STOCK_TRANSFER = 5;

        /** NG */
        public static final int NG = 6;

        /** ECI Onhold */
        public static final int ECI_ONHOLD = 7;

        /** Stock Adjust */
        public static final int STOCK_ADJUST = 8;

        /** Decant */
        public static final int DECANT = 9;

        /** Imp Outbound */
        public static final int IMP_OUTBOUND = 10;

        /** RELEASE_ONHOLD */
        public static final int RELEASE_ONHOLD = 11;

        /** Cancel INVOICE */
        public static final int CANCEL_INVOICE = 12;
    }

    /**
     * Ip Status.
     */
    public interface IpStatus {

        /** Exp Outbound */
        public static final int EXP_OUTBOUND = 1;

        /** Invoice */
        public static final int INVOICE = 2;

        /** Customs Clearance */
        public static final int CUSTOMS_CLEARANCE = 3;

        /** Devanned */
        public static final int DEVANNED = 4;

        /** Imp Inbound */
        public static final int IMP_INBOUND = 5;

        /** WHS Transfer */
        public static final int WHS_TRANSFER = 6;

        /** Stock Transfer */
        public static final int STOCK_TRANSFER = 7;

        /** NG */
        // public static final int NG = 6;

        /** ECI Onhold */
        // public static final int ECI_ONHOLD = 7;

        /** Stock Adjust */
        public static final int STOCK_ADJUST = 8;

        /** Decant */
        public static final int DECANT = 9;

        /** Imp Outbound */
        public static final int IMP_OUTBOUND = 10;

        /** Invoice */
        public static final int IMP_ACTUAL_OUTBOUND = 11;

        /** Cancelled */
        public static final int CANCELLED = 99;
    }

    /**
     * BusinessPattern.
     */
    public interface BusinessPattern {

        /** V-V */
        public static final int V_V = 1;

        /** AISIN */
        public static final int AISIN = 2;
    }

    /**
     * BusinessPatternName.
     */
    public interface BusinessPatternName {

        /** V-V */
        public static final String V_V = "V-V";

        /** AISIN */
        public static final String AISIN = "AISIN";
    }

    /**
     * alert Level.
     */
    public interface AlertLevel {

        /** LEVEL0 */
        public static final int LEVEL0 = 99;

        /** LEVEL1 */
        public static final int LEVEL1 = 1;

        /** LEVEL2 */
        public static final int LEVEL2 = 2;

        /** LEVEL3 */
        public static final int LEVEL3 = 3;
    }

    /**
     * KbsNirdFlag.
     */
    public interface KbsNirdFlag {

        /** Normal */
        public static final int NORMAL = 0;

        /** Not in rundown */
        public static final int NOT_IN_RUNDOWN = 1;

    }

    /**
     * KbsCompletedFlag.
     */
    public interface KbsCompletedFlag {

        /** Normal */
        public static final int NORMAL = 0;

        /** Completed */
        public static final int COMPLETED = 1;

        /** History */
        public static final int HISTORY = 2;
    }

    /**
     * InvoiceShippingStatus.
     */
    public interface InvoiceShippingStatus {

        /** Normal */
        public static final int NORMAL = 0;

        /** Cancelled */
        public static final int CANCELLED = 1;
    }

    /**
     * InvoiceGroupStatus.
     */
    public interface InvoiceGroupStatus {

        /** Normal */
        public static final int NORMAL = 0;

        /** Cancelled */
        public static final int CANCELLED = 1;
    }

    /**
     * InvoiceIrregularsStatus.
     */
    public interface InvoiceIrregularsStatus {

        /** Draft */
        public static final int DRAFT = 1;

        /** Done */
        public static final int DONE = 2;
    }

    /**
     * InvoiceUploadStatus.
     */
    public interface InvoiceUploadStatus {

        /** Draft */
        public static final int DRAFT = 1;

        /** Done */
        public static final int DONE = 2;
    }

    /**
     * InvoiceStatus.
     */
    public interface InvoiceStatus {

        /** Not Approved */
        public static final int NOT_APPROVED = 1;

        /** Pending */
        public static final int PENDING = 2;

        /** Inbound Completed */
        public static final int INBOUND_COMPLETED = 3;

        /** Cancelled */
        public static final int CANCELLED = 99;
    }

    /**
     * PostRiFlag.
     */
    public interface PostRiFlag {

        /** N */
        public static final int N = 0;

        /** Y */
        public static final int Y = 1;

        /** After GR */
        public static final int AFTER_GR = 2;

        /** After GI */
        public static final int AFTER_GI = 3;
    }

    /**
     * InvoiceRiStatus.
     */
    public interface InvoiceRiStatus {

        /** Not Allow Post GR/GI */
        public static final int NOT_ALLOW = 1;

        /** GR/GI Posted */
        public static final int POSTED = 2;

        /** Post GR/GI button */
        public static final int BUTTON = 3;

        /** Blank */
        public static final int BLANK = 4;
    }

    /**
     * InvoiceType.
     */
    public interface InvoiceType {

        /** Manual */
        public static final int MANUAL = 1;

        /** Interface */
        public static final int INTERFACE = 2;

        /** Aisin */
        public static final int AISIN = 3;
    }

    /**
     * CancelCustomerForecastStatus.
     */
    public interface CancelCustomerForecastStatus {

        /** Draft */
        public static final int NORMAL = 1;

        /** Done */
        public static final int CANCELLED = 99;
    }

    /**
     * 
     * CodeMasterCategory.
     * 
     */
    public interface CodeMasterCategory {

        /** USER_STATUS */
        public static final String USER_STATUS = "USER_STATUS";

        /** BUSINESS_PATTERN */
        public static final String BUSINESS_PATTERN = "BUSINESS_PATTERN";

        /** PARTS_STATUS */
        public static final String PARTS_STATUS = "PARTS_STATUS";

        /** INVOICE_IRREGULAR_STATUS */
        public static final String INVOICE_IRREGULAR_STATUS = "INVOICE_IRREGULAR_STATUS";

        /** INVOICE_UPLOAD_STATUS */
        public static final String INVOICE_UPLOAD_STATUS = "INVOICE_UPLOAD_STATUS";

        /** INVOICE_STATUS */
        public static final String INVOICE_STATUS = "INVOICE_STATUS";

        /** DIS_ORDER_STATUS */
        public static final String DIS_ORDER_STATUS = "DIS_ORDER_STATUS";

        /** POST_RI_FLAG */
        public static final String POST_RI_FLAG = "POST_RI_FLAG";

        /** INVOICE_RI_STATUS */
        public static final String INVOICE_RI_STATUS = "INVOICE_RI_STATUS";

        /** INVOICE_TYPE */
        public static final String INVOICE_TYPE = "INVOICE_TYPE";

        /** TRANSPORT_MODE */
        public static final String TRANSPORT_MODE = "TRANSPORT_MODE";

        /** ACTIVE_FLAG */
        public static final String ACTIVE_FLAG = "ACTIVE_FLAG";

        /** CALENDAR_PARTY */
        public static final String CALENDAR_PARTY = "CALENDAR_PARTY";

        /** SUPPLIER */
        public static final String SUPPLIER = "Supplier";

        /** SHIPPING_ROUTE_TYPE */
        public static final String SHIPPING_ROUTE_TYPE = "SHIPPING_ROUTE_TYPE";

        /** EXPORT_WH_CALENDER */
        public static final String EXPORT_WH_CALENDER = "EXPORT_WH_CALENDER";

        /** PARTS_TYPE */
        public static final String PARTS_TYPE = "PARTS_TYPE";

        /** BUILD_OUT_INDICATOR */
        public static final String BUILD_OUT_INDICATOR = "BUILD_OUT_INDICATOR";

        /** BUSINESS_TYPE */
        public static final String BUSINESS_TYPE = "BUSINESS_TYPE";

        /** DISCONTINUE_INDICATOR */
        public static final String DISCONTINUE_INDICATOR = "DISCONTINUE_INDICATOR";

        /** ORDER_FORECAST_TYPE */
        public static final String ORDER_FORECAST_TYPE = "ORDER_FORECAST_TYPE";

        /** CUSTOMER_STOCK_FLAG */
        public static final String CUSTOMER_STOCK_FLAG = "CUSTOMER_STOCK_FLAG";

        /** INVENTORY_BY_BOX */
        public static final String INVENTORY_BY_BOX = "INVENTORY_BY_BOX";

        /** SIMULATION_END_DAY_P */
        public static final String SIMULATION_END_DAY_P = "SIMULATION_END_DAY_P";

        /** CUST_FORECAST_ADJUST_P1 */
        public static final String CUST_FORECAST_ADJUST_P1 = "CUST_FORECAST_ADJUST_P1";

        /** CUST_FORECAST_ADJUST_P2 */
        public static final String CUST_FORECAST_ADJUST_P2 = "CUST_FORECAST_ADJUST_P2";

        /** ON_SHIPPING_DELAY_ADJUST */
        public static final String ON_SHIPPING_DELAY_ADJUST_P = "ON_SHIPPING_DELAY_ADJUST_P";

        /** EMAIL_ALERT_LEVEL */
        public static final String EMAIL_ALERT_LEVEL = "EMAIL_ALERT_LEVEL";

        /** EMAIL_ALERT_LEVEL */
        public static final String ORION_PLUS_FLAG = "ORION_PLUS_FLAG";

        /** ALARM_FLAG */
        public static final String ALARM_FLAG = "ALARM_FLAG";

        /** IF_BATCH_STATUS */
        public static final String IF_BATCH_STATUS = "IF_BATCH_STATUS";

        /** INVOICE_ISSUE_TYPE */
        public static final String INVOICE_ISSUE_TYPE = "INVOICE_ISSUE_TYPE";

        /** PRODUCT_MATERIAL */
        public static final String PRODUCT_MATERIAL = "PRODUCT_MATERIAL";

        /** SECTION_OF_CAR */
        public static final String SECTION_OF_CAR = "SECTION_OF_CAR";

        /** PRODUCTION_PROCESS */
        public static final String PRODUCTION_PROCESS = "PRODUCTION_PROCESS";

        /** BUSINESS_WITH_TTC_FLAG */
        public static final String BUSINESS_WITH_TTC_FLAG = "BUSINESS_WITH_TTC_FLAG";

        /** NDA_AGREEMENT_FLAG */
        public static final String NDA_AGREEMENT_FLAG = "NDA_AGREEMENT_FLAG";

        /** OVERALL_EVALUATION */
        public static final String OVERALL_EVALUATION = "OVERALL_EVALUATION";

        /** SUPPLIER_STATUS */
        public static final String SUPPLIER_STATUS = "SUPPLIER_STATUS";
    }

    /**
     * 
     * Calendar_Party.
     * 
     */
    public interface CalendarParty {

        /** SUPPLIER */
        final static int SUPPLIER = 1;
        /** IMP_SEAPORT_CUSTOMS */
        final static int IMP_SEAPORT_CUSTOMS = 2;
        /** TTC_IMP_WAREHOUSE */
        final static int TTC_IMP_WAREHOUSE = 3;
        /** CUSTOMER */
        final static int CUSTOMER = 4;
        /** IMP_AIRPORT_CUSTOMS */
        final static int IMP_AIRPORT_CUSTOMS = 5;
        /** TTC_IMPORT_OFFICE */
        final static int TTC_IMPORT_OFFICE = 6;
        /** EXP_WAREHOUSE */
        final static int EXP_WAREHOUSE = 7;
    }

    /**
     * 
     * ShippingRouteType.
     * 
     */
    public interface ShippingRouteType {

        /** VV */
        final static int VV = 1;
        /** AISIN_TTTJ */
        final static int AISIN_TTTJ = 2;
        /** AISIN_TTSH */
        final static int AISIN_TTSH = 3;
    }

    /**
     * 
     * ShippingRouteType.
     * 
     */
    public interface OrderForecastType {

        /** Daily */
        final static String DAILY = "1";
        /** AISIN */
        final static String AISIN = "2";
    }

    /**
     * 
     * week day
     * 
     */
    public interface WeekDay {

        /** MONDAY */
        final static String MONDAY = "1";
        /** TUESDAY */
        final static String TUESDAY = "2";
        /** WEDNESDAY */
        final static String WEDNESDAY = "3";
        /** THURSDAY */
        final static String THURSDAY = "4";
        /** FRIDAY */
        final static String FRIDAY = "5";
        /** ON_SATURDAY */
        final static String ON_SATURDAY = "6";
        /** ON_SUNDAY */
        final static String ON_SUNDAY = "7";

    }

    /**
     * 
     * category Language.
     * 
     */
    public interface CategoryLanguage {

        /** VV */
        final static int ENGLISH = 1;
        /** AISIN_TTTJ */
        final static int CHINESE = 2;

    }

    /**
     * 
     * Type.
     * 
     */
    public interface ShipType {

        /** NEW */
        final static String NEW = "NEW";
        /** MOD */
        final static String MOD = "MOD";
    }

    /**
     * 
     * Type.
     * 
     */
    public interface TypeYN {

        /** Y */
        final static String Y = "Y";
        /** N */
        final static String N = "N";
        /** Y/N */
        final static String Y_N = "Y,N";
    }

    /**
     * ChainStep.
     */
    public interface ChainStep {

        /** 1: Plan */
        public static final int PLAN = 1;

        /** 2: Invoice */
        public static final int INVOICE = 2;

        /** 3: Forecast */
        public static final int FORECAST = 3;

        /** 4: Next ETD */
        public static final int NEXT_ETD = 4;

        /** ALL */
        public static final Integer[] ALL = new Integer[] { PLAN, INVOICE, FORECAST, NEXT_ETD };
    }

    /**
     * 
     * Type.
     * 
     */
    public interface WorkingDay {

        /** Y */
        final static Integer WORKING_DAY = 1;
        /** N */
        final static Integer REST_DAY = 0;
    }

    /**
     * TransportMode.
     */
    public interface TransportMode {

        /** 1: Plan */
        public static final int SEA = 1;

        /** 2: Invoice */
        public static final int AIR = 2;
    }

    /**
     * Import Stock Flag.
     */
    public interface ImpStockFlag {

        /** 1: Plan */
        public static final int WITH_CLEARANCE = 1;

        /** 2: Invoice */
        public static final int WITHOUT_CLEARANCE = 2;
    }

    /**
     * Not in run-down reason type.
     */
    public interface NotInRDReasonType {

        /** 1:ETD Delay(Pattern A) */
        public static final int ETD_DELAY_A = 1;

        /** 2:ETD Delay(Pattern B) */
        public static final int ETD_DELAY_B = 2;

        /** 3:Inbound Delay */
        public static final int INBOUND_DELAY = 3;

        /** 4:NG On-Hold */
        public static final int NG_ONHOLD = 4;
    }

    /**
     * Simulation end day pattern.
     */
    public interface SimulationEndDayPattern {

        /** 1:Pattern A */
        public static final int PATTERN_A = 1;

        /** 2:Pattern B */
        public static final int PATTERN_B = 2;
    }

    /**
     * Rundown Detail Attach Type.
     */
    public interface AttachType {

        /** 1: Order Forecast */
        public static final int ORDER_FORECAST = 1;

        /** 2: Plan */
        public static final int PLAN = 2;

        /** 3: On-Shipping */
        public static final int ON_SHIPPING = 3;
    }

    /**
     * Simulation Type.
     */
    public interface SimulationType {

        /** 1:Target Month */
        public static final int TARGET = 1;

        /** 2:Forecast Month */
        public static final int FORECAST1 = 2;

        /** 2:Forecast Month */
        public static final int FORECAST2 = 3;

        /** 2:Forecast Month */
        public static final int FORECAST3 = 4;

        /** 2:Forecast Month */
        public static final int FORECAST4 = 5;

        /** 2:Forecast Month */
        public static final int FORECAST5 = 6;

        /** 2:Forecast Month */
        public static final int FORECAST6 = 7;

        /** 2:Forecast Month */
        public static final int OTHERS = 8;

        /** 2:Forecast Month */
        public static final int NOT_REQUIRE = 9;
    }

    /**
     * NoCfcFlag.
     */
    public interface NoCfcFlag {

        /** 1:Y */
        public static final int Y = 1;

        /** 0:N */
        public static final int N = 0;

    }

    /**
     * NoPfcFlag.
     */
    public interface NoPfcFlag {

        /** 1:Y */
        public static final int Y = 1;

        /** 0:N */
        public static final int N = 0;

    }

    /**
     * Plan Type.
     */
    public interface PlanType {

        /** 1: Difference */
        public static final int DIFFERENCE = 1;

        /** 2: Box */
        public static final int BOX = 2;

        /** 3: Shipping Plan */
        public static final int SHIPPING_PLAN = 3;
    }

    /**
     * InventoryByBox.
     */
    public interface InventoryByBox {

        /** 1:Y */
        public static final int Y = 1;

        /** 0:N */
        public static final int N = 0;

    }

    /**
     * CustStockFlag.
     */
    public interface CustStockFlag {

        /** 1:Y */
        public static final int Y = 1;

        /** 0:N */
        public static final int N = 0;
    }

    /**
     * CustForcastStatus.
     */
    public interface CustForcastStatus {

        /** 1:Y */
        public static final int Y = 1;

        /** 99:N */
        public static final int N = 99;
    }

    /**
     * AlarmFlag.
     */
    public interface AlarmFlag {

        /** 0:N */
        public static final int N = 0;

        /** 1:Y */
        public static final int Y = 1;
    }

    /**
     * ORION_PLUS_FLAG.
     */
    public interface OrionPlusFlag {
        /** 0:N */
        public static final int N = 0;

        /** 1:Y */
        public static final int Y = 1;
    }

    /**
     * BUILD_OUT_INDICATOR.
     */
    public interface BuildOutIndicator {
        /** 0:N */
        public static final int N = 0;

        /** 1:Y */
        public static final int Y = 1;
    }

    /**
     * UPLOAD_FILE_TYPE.
     */
    public interface UploadFileType {
        /** 1:Original Kanban File */
        public static final int ORIGINAL_KANBAN_FILE = 1;

        /** 2:Updated Kanban File */
        public static final int UPDATED_KANBAN_FILE = 2;
    }

    /**
     * BUILD_OUT_INDICATOR.
     */
    public interface HasQtyFlag {

        /** 0:N */
        public static final int IS_ZERO = 1;

        /** 1:Y */
        public static final int IS_NOT_ZERO = 2;
    }

    /**
     * DIS_ORDER_STATUS.
     */
    public interface DisOrderStatus {

        /** 1:PENDING */
        public static final int PENDING = 1;

        /** 2:PENDING */
        public static final int INBOUND_COMPLETED = 2;

        /** 3:PENDING */
        public static final int COMPLETED = 3;

        /** 4:PENDING */
        public static final int FORCE_COMPLETED = 4;

        /** 4:PENDING */
        public static final int CANCELLED = 99;
    }

    /**
     * Handle Flag.
     */
    public interface HandleFlag {

        /** UNPROCESS */
        public static final int UNPROCESS = 0;

        /** PROCESS_SUCCESS */
        public static final int PROCESS_SUCCESS = 1;

        /** FILE_CHECK_NG */
        public static final int FILE_CHECK_NG = 2;

        /** LOGIC_CHECK_NG */
        public static final int LOGIC_CHECK_NG = 3;

        /** INVOICE_MISMATCH */
        public static final int INVOICE_MISMATCH = 4;

        /** INVOICE_IS_ALREADY_POST_GRGI */
        public static final int INVOICE_IS_ALREADY_POST_GRGI = 5;

        /** TO_WAREHOUSE_ISNOT_EXIST */
        public final static int WAREHOUSE_ISNOT_EXIST = 6;

        /** TO_CUSTOMER_ISNOT_EXIST */
        public final static int CUSTOMER_ISNOT_EXIST = 7;

        /** MAIN_INFORMATION_DOESNOT_MATCHED */
        public final static int MAIN_INFORMATION_DOESNOT_MATCHED = 8;

        /** IP_QTY_MORE_THAN_INVOICE_QTY */
        public final static int IP_QTY_MORE_THAN_INVOICE_QTY = 9;

        /** DELETE_INBOUND_DATA_NOT_FOUND */
        public final static int DELETE_INBOUND_DATA_NOT_FOUND = 10;

        /** UPDATE_OB_DATA_IN_IMPORT_STOCK */
        public final static int UPDATE_OB_DATA_IN_IMPORT_STOCK = 11;

        /** DELETE_OUTBOUND_DATA_NOT_FOUND */
        public final static int DELETE_OUTBOUND_DATA_NOT_FOUND = 12;

        /** INVOICE_NO_IS_EXIST */
        public final static int INVOICE_NO_IS_EXIST = 13;

        /** ORDER_NOT_EXIST */
        public final static int ORDER_NOT_EXIST = 14;

        /** TO_SUPPLIER_PARTS_ISNOT_EXIST */
        public final static int SUPPLIER_PARTS_ISNOT_EXIST = 15;

        /** ALREADY_IMP_INBOUND */
        public final static int ALREADY_IMP_INBOUND = 16;

        /** QTY_NOT_MATCHED */
        public final static int QTY_NOT_MATCHED = 17;

        /** IP_ALREADY_HANDLE_ERROR */
        public final static int IP_ALREADY_HANDLE_ERROR = 18;

        /** IP_DOES_NOT_EXIST_FOR_VV */
        public final static int IP_DOES_NOT_EXIST_FOR_VV = 19;

        /** PID_NO_IS_ARLREADY_EXIST */
        public final static int PID_NO_IS_ARLREADY_EXIST = 20;

        /** ALREADY_FORCE_COMPLETED */
        public final static int ALREADY_FORCE_COMPLETED = 21;

        /** PROCESS_FAILURE */
        public static final int PROCESS_FAILURE = 99;
    }

    /**
     * InvoiceMatchStatus.
     */
    public interface InvoiceMatchStatus {

        /** 1:MISMATCH */
        public static final int MISMATCH = 1;

        /** 2:MATCHED */
        public static final int MATCHED = 2;
    }

    /**
     * Invoice Container Status.
     */
    public interface InvoiceContainerStatus {

        /** 1:On Shipping */
        public static final int ON_SHIPPING = 1;

        /** 2:In Rack */
        public static final int IN_RACK = 2;

        /** 3:In Stock */
        public static final int IN_STOCK = 3;
    }

    /**
     * Kanban Status.
     */
    public interface KanbanStatus {

        /** 1:Normal */
        public static final int NORMAL = 1;

        /** 99:Cancelled */
        public static final int CANCELLED = 99;
    }

    /**
     * Inner packing for kanban.
     */
    public interface IpKbDataType {

        /** CUSTOMS_CLEARANCE */
        public static final int CUSTOMS_CLEARANCE = 0;

        /** Imp Inbound */
        public static final int DEVANNED = 1;

        /** Imp Inbound */
        public static final int INBOUND = 2;

        /** WHS Transfer */
        public static final int WHS_TRANSFER = 3;

        /** Stock Transfer */
        public static final int STOCK_TRANSFER = 4;

        /** NG */
        public static final int NG = 6;

        /** ECI Onhold */
        public static final int ECI_ONHOLD = 7;

        /** Stock Adjust */
        public static final int STOCK_ADJUST = 8;

        /** RELEASE_ONHOLD */
        public static final int RELEASE_ONHOLD = 9;

        /** Decant */
        public static final int DECANT = 9;

        /** Imp Outbound */
        public static final int OUTBOUND = 10;

        /** CANCELLED */
        public static final int CANCELLED = 11;
    }

    /**
     * Sync Time Data Type.
     */
    public interface SyncTimeDataType {

        /** 1:SSMS */
        public static final int SSMS = 1;

        /** 2:TT_LOGIX_VV */
        public static final int TT_LOGIX_VV = 2;

        /** 3: TT_LOGIX_AISIN */
        public static final int TT_LOGIX_AISIN = 3;
    }

    /**
     * Sync Time Data Type.
     */
    public interface SyncTimeDataTypeName {

        /** 1:SSMS */
        public static final String SSMS = "SSMS";

        /** 2:TT_LOGIX_VV */
        public static final String TT_LOGIX_VV = "TT-LOGIX V-V";

        /** 3: TT_LOGIX_AISIN */
        public static final String TT_LOGIX_AISIN = "TT-LOGIX AISIN";
    }

    /**
     * IF Batch status.
     */
    public interface IFBatchStatus {

        /** Fail */
        final static int FAIL = 0;

        /** Success */
        final static int SUCCESS = 1;

        /** Incomplete */
        final static int INCOMPLETE = 2;
    }

    /**
     * Batch Job status.
     */
    public interface BatchJobStatus {

        /** Success */
        final static int SUCCESS = 0;

        /** Fail */
        final static int FAIL = 1;

        /** Running */
        final static int RUNNING = 9;
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

    /**
     * 
     * Rundown Type.
     */
    public interface RundownType {
        /**  */
        final static String ALL_PARTS = "All Parts";
        /**  */
        final static String SINGLE_PARTS = "Single Parts";
    }

    /**
     * interface sub batch id.
     */
    public interface IFBatchId {

        /** main batch */
        final static String SSMS_MAIN = "CPIIFB01";

        /** main batch */
        final static String TTLOGIC_MAIN = "CPIIFB02";

    }

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

    /** StockBatchId */
    public interface OnlineFlag {

        /** on line */
        final static int ONLINE = 1;

        /** off line */
        final static int OFFLINE = 2;
    }

    /**
     * InvoiceIssueType.
     */
    public interface InvoiceIssueType {

        /** Exp W/H */
        public static final int EXP_WH = 1;

        /** Supplier W/H */
        public static final int SUP_WH = 2;
    }

}
