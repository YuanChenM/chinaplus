/**
 * @screen Run-down file manager
 * @author liu_yinchuan
 */
package com.chinaplus.common.util;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.chinaplus.common.bean.TntNotInRundownEx;
import com.chinaplus.common.bean.TntRdDetailAttachEx;
import com.chinaplus.common.bean.TntRundownDetailEx;
import com.chinaplus.common.bean.TntRundownHeader;
import com.chinaplus.common.bean.TntRundownMasterEx;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.AttachType;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.CustStockFlag;
import com.chinaplus.common.consts.CodeConst.InventoryByBox;
import com.chinaplus.common.consts.CodeConst.NoCfcFlag;
import com.chinaplus.common.consts.CodeConst.NoPfcFlag;
import com.chinaplus.common.consts.CodeConst.NotInRDReasonType;
import com.chinaplus.common.consts.CodeConst.WorkingDay;
import com.chinaplus.common.entity.TntRdAttachCfc;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Run-down file manager.
 * 
 * @author liu_yinchuan
 */
public class RunDownAllPartsManager {

    /** sheet name */
    private final static String SHEET_NAME_STYLE = "styleSheet";

    /** template date white */
    private final static String DATE_WHITE_PREFIX = "Date_White";

    /** template date red font */
    private final static String DATE_RED_FONT_PREFIX = "Date_Red_Font";

    /** template Month */
    private final static String MONTH_PREFIX = "MONTH_PREFIX";

    /** template label gray */
    private final static String LABEL_WHITE = "Label_White";

    /** template label gray */
    private final static String LABEL_WHITE_BOLD = "LABEL_WHITE_BOLD";

    /** template label gray */
    private final static String LABEL_BLACK_BOLD = "LABEL_BLACK_BOLD";

    /** template label yellow */
    private final static String LABEL_YELLOW = "LABEL_YELLOW";

    /** template label yellow */
    private final static String LABEL_YELLOW_BOLD = "LABEL_YELLOW_BOLD";

    /** template label black 25% */
    private final static String LABEL_BLACK25 = "LABEL_BLACK25";

    /** template label gray 15% */
    private final static String LABEL_GRAY15 = "Label_Gray_15";

    /** template decimal percentage */
    private final static String NORMAL_LABEL_WO_LINE = "NORMAL_LABEL_WO_LINE";

    /** template decimal white */
    private final static String DECIMAL_WHITE = "Decimal_White_";

    /** template decimal yellow */
    private final static String DECIMAL_YELLOW = "Decimal_Yellow_";

    /** template decimal red font */
    private final static String DECIMAL_RED = "Decimal_Red_";

    /** template decimal write font red back */
    private final static String DECIMAL_RED_BOLD = "Decimal_Red_Bold_";

    /** template decimal red font */
    private final static String DECIMAL_PINK = "Decimal_Pink_";

    /** template decimal red font */
    private final static String DECIMAL_BLACK = "Decimal_Black_";

    /** template lowest stock days(Boxes) header */
    private final static String HEADER_LOWEST_STOCK = "HEADER_LOWEST_STOCK";

    /** template Latest Order Target Month header */
    private final static String HEADER_LATEST_TARGET_MONTH = "HEADER_LATEST_TARGET_MONTH";

    /** template Delivered To Customer header */
    private final static String HEADER_DELIVERED_TO_CUST = "HEADER_DELIVERED_TO_CUST";

    /** template Customer Usage header */
    private final static String HEADER_CUSTOMER_USAGE = "HEADER_CUSTOMER_USAGE";

    /** template Not In Rundown header */
    private final static String HEADER_NOT_IN_RUNDOWN = "HEADER_NOT_IN_RUNDOWN";

    /** template Not In Rundown(Detail) header */
    private final static String HEADER_NOT_IN_RUNDOWN_DETIAL = "HEADER_NOT_IN_RUNDOWN_DETIAL";

    /** template Not In Rundown(Detail) header */
    private final static String HEADER_NOT_IN_RUNDOWN_HEADER = "HEADER_NOT_IN_RUNDOWN_HEADER";

    /** template Imp Stock Qty header */
    private final static String HEADER_IMP_STOCK = "HEADER_IMP_STOCK";

    /** template each stock Qty header */
    private final static String HEADER_STOCK_QTY = "HEADER_STOCK_QTY";

    /** template each stock Days header */
    private final static String HEADER_STOCK_DAYS = "HEADER_STOCK_DAYS";

    /** template each stock Days header */
    private final static String HEADER_INVOICE_STYLE = "HEADER_INVOICE_STYLE";

    /** template each stock Days header */
    private final static String HEADER_IPO_GREEN = "HEADER_IPO_GREEN";

    /** template each stock Days header */
    private final static String HEADER_IPO_PINK = "HEADER_IPO_PINK";

    /** template each stock Days header */
    private final static String HEADER_IPO_ORANGE = "HEADER_IPO_ORANGE";

    /** NO_CUSTOMER_STOCK */
    private final static String NO_CUSTOMER_STOCK = "CPSRDF01_Label_NoCustomerStock";

    /** NO_ORDER_FORECAST */
    private final static String NO_ORDER_FORECAST = "CPSRDF01_Label_NoOrderForecast";

    /** NO_CUSTOMER_FORECAST */
    private final static String NO_CUSTOMER_FORECAST = "CPSRDF01_Label_NoCustomerForecast";

    /** NO_OREDER_AND_CUSTOMER_FORECAST */
    private final static String NO_FORECAST_ALL = "CPSRDF01_Label_NoOrderCustomerForecast";

    /** LOWEST_STOCK_DAYS */
    private final static String LOWEST_STOCK_DAYS = "CPSRDF01_Grid_LowestStockDays";

    /** LOWEST_STOCK_BOX */
    private final static String LOWEST_STOCK_BOX = "CPSRDF01_Grid_LowestStockBox";

    /** STOCK_DATE */
    private final static String STOCK_DATE = "CPSRDF01_Grid_StockDate";

    /** ADD_ON_QTY */
    private final static String ADD_ON_QTY = "CPSRDF01_Grid_AddOnQty";

    /** LATEST_ORDER_TARGET_MONTH */
    private final static String LATEST_ORDER_TARGET_MONTH = "CPSRDF01_Grid_LatestTargetMonth";

    /** ENDING_STOCK_DAYS */
    private final static String ENDING_STOCK_DAYS = "CPSRDF01_Grid_EndingStockDays";

    /** SAFETY_STOCK_DAYS */
    private final static String SAFETY_STOCK_DAYS = "CPSRDF01_Grid_SafetyStockDays";

    /** ADD_ON_AVOID_SHORTAGE */
    private final static String ADD_ON_AVOID_SHORTAGE = "CPSRDF01_Grid_AddToShortage";

    /** ADD_ON_AVOID_SAFTY */
    private final static String ADD_ON_AVOID_SAFTY = "CPSRDF01_Grid_AddToSafetyStock";

    /** DELIVERED_TO_CUSTOMER */
    private final static String DELIVERED_TO_CUSTOMER = "CPSRDF01_Grid_DeliveredToCustomer";

    /** CUSTOMER_USAGE */
    private final static String CUSTOMER_USAGE = "CPSRDF01_Grid_CustomerUsage";

    /** NOT_IN_RUNDOWN_REASON */
    private final static String NOT_IN_RUNDOWN_REASON = "CPSRDF01_Grid_NotInRundownReason";

    /** INVOICE_NO */
    private final static String INVOICE_NO = "CPSRDF01_Grid_InvoiceNo";

    /** ORDER_NO */
    private final static String ORDER_NO = "CPSRDF01_Grid_OrderNo";

    /** NOT_IN_RUNDOWN */
    private final static String NOT_IN_RUNDOWN = "CPSRDF01_Grid_NotInRundown";

    /** ETD_DELAY */
    private final static String ETD_DELAY = "CPSRDF01_Label_ETDDelay";

    /** INBOUND_DELAY */
    private final static String INBOUND_DELAY = "CPSRDF01_Label_InboundDelay";

    /** NG_ON_HOLD */
    private final static String NG_ON_HOLD = "CPSRDF01_Label_NGOnHold";

    /** VAN */
    private final static String VAN = "VAN";

    /** ETD */
    private final static String ETD = "ETD";

    /** ETA */
    private final static String ETA = "ETA";

    /** PLAN_INBOUND */
    private final static String PLAN_INBOUND = "CPSRDF01_Label_PlanInbound";

    /** ACTUAL_INBOUND */
    private final static String ACTUAL_INBOUND = "CPSRDF01_Label_ActualInbound";

    /** TOTAL_NOT_IN_RUNDOWN */
    private final static String TOTAL_NOT_IN_RUNDOWN = "CPSRDF01_Grid_NotInRundownQty";

    /** CUSTOMER_STOCK_QTY */
    private final static String CUSTOMER_STOCK_QTY = "CPSRDF01_Grid_CustomerStockQty";

    /** CUSTOMER_STOCK_DATE */
    private final static String CUSTOMER_STOCK_DATE = "CPSRDF01_Grid_CustomerStockDate";

    /** TOTAL_IMPORT_STOCK */
    private final static String TOTAL_IMPORT_STOCK = "CPSRDF01_Grid_TotalImportStock";

    /** ECI_ON_HOLD */
    private final static String ECI_ON_HOLD = "CPSRDF01_Grid_ECIOnHold";

    /** AVAILABLE_IMPORT_STOCK */
    private final static String AVAILABLE_IMPORT_STOCK = "CPSRDF01_Grid_ImportStock";

    /** PREPARED_OUTBOUND */
    private final static String PREPARED_OUTBOUND = "CPSRDF01_Grid_PreparedOutbound";

    /** STOCK_QTY */
    private final static String STOCK_QTY = "CPSRDF01_Grid_StockQty";

    /** STOCK_DAYS */
    private final static String STOCK_DAYS = "CPSRDF01_Grid_StockDays";

    /** FORECAST_LABEL */
    private final static String FORECAST_LABEL = "CPSRDF01_Grid_Forecast";

    /** DOWNLOAD_TIME */
    private final static String DOWNLOAD_TIME = "CPSRDF01_Label_DownloadTime";

    /** CPSSSF01_Label_NoOrderCustomerForecast */
    private final static String SYNC_SSMS_DATE_TIME = "CPSRDF01_Label_LastSSMSInterfaceTime";

    /** CPSSSF01_Label_NoOrderCustomerForecast */
    private final static String SYNC_VV_DATE_TIME = "CPSRDF01_Label_LastVVInterfaceTime";

    /** CPSSSF01_Label_NoOrderCustomerForecast */
    private final static String SYNC_AISIN_DATE_TIME = "CPSRDF01_Label_LastASInterfaceTime";

    /** rundown start row for all parts */
    private final static Integer RD_START_ROW_FOR_HEADER = IntDef.INT_SEVEN;

    /** rundown start row for all parts */
    private final static Integer RD_START_ROW_FOR_DATA = IntDef.INT_EIGHT;

    /** rundown start column for all parts */
    private final static Integer RD_START_COL_LOWTEST_INFO = IntDef.INT_TWENTY_FOUR;

    /** rundown start row for all parts */
    private final static Integer RD_START_ROW_FOR_NOT_IN = IntDef.INT_THREE;

    /** header map key of lowest stock */
    private final static String HEAD_LOWEST_STOCK = "HEAD_LOWEST_STOCK";

    /** header map key of customer usage share */
    private final static String HEAD_LATEST_TARGET_MONTH = "HEAD_LATEST_TARGET_MONTH";

    /** header map key of customer usage share */
    private final static String HEAD_USAGE_SHARE = "HEAD_USAGE_SHARE";

    /** header map key of not in rundown */
    private final static String HEAD_NOT_IN_RUNDOWN = "HEAD_NOT_IN_RUNDOWN";

    /** header map key of not in rundown */
    private final static String HEAD_TOTAL_NOT_IN_RUNDOWN = "HEAD_TOTAL_NOT_IN_RUNDOWN";

    /** header map key of not in rundown */
    private final static String HEAD_RUNDOWN_DETAIL = "HEAD_RUNDOWN_DETAIL";

    /** header map key of not in rundown */
    private final static String HEAD_RUNDOWN_DETAIL_ATTACH = "HEAD_RUNDOWN_DETAIL_ATTACH";

    /** NA */
    private final static String NA = "NA";

    /** point width of column */
    private final static double POINT_WIDTH = 357;

    /**
     * Prepare style map for all parts run-down file.
     *
     * @param wbTemplate wbTemplate
     * @param styleMap style map
     * @param lang language
     */
    public static void prepStyleMapForAllParts(Workbook wbTemplate, Map<String, CellStyle> styleMap, Language lang) {

        // get style Sheet
        Sheet styleSheet = wbTemplate.getSheet(SHEET_NAME_STYLE);
        // prepare label
        styleMap.put(LABEL_WHITE, PoiUtil.getCell(styleSheet, IntDef.INT_THREE, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_YELLOW, PoiUtil.getCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_GRAY15, PoiUtil.getCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_BLACK_BOLD, PoiUtil.getCell(styleSheet, IntDef.INT_SEVEN, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_WHITE_BOLD, PoiUtil.getCell(styleSheet, IntDef.INT_EIGHT, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_YELLOW_BOLD, PoiUtil.getCell(styleSheet, IntDef.INT_NINE, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_BLACK25, PoiUtil.getCell(styleSheet, IntDef.INT_TEN, IntDef.INT_ONE).getCellStyle());
        styleMap.put(NORMAL_LABEL_WO_LINE, PoiUtil.getCell(styleSheet, IntDef.INT_TWELVE, IntDef.INT_ONE)
            .getCellStyle());
        // prepare Date
        if (Language.CHINESE.equals(lang)) {
            styleMap.put(DATE_RED_FONT_PREFIX, PoiUtil.getCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWO)
                .getCellStyle());
            styleMap
                .put(DATE_WHITE_PREFIX, PoiUtil.getCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWO).getCellStyle());
            styleMap.put(MONTH_PREFIX, PoiUtil.getCell(styleSheet, IntDef.INT_SIX, IntDef.INT_TWO).getCellStyle());
        } else {
            styleMap.put(DATE_RED_FONT_PREFIX, PoiUtil.getCell(styleSheet, IntDef.INT_THREE, IntDef.INT_TWO)
                .getCellStyle());
            styleMap
                .put(DATE_WHITE_PREFIX, PoiUtil.getCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TWO).getCellStyle());
            styleMap.put(MONTH_PREFIX, PoiUtil.getCell(styleSheet, IntDef.INT_SEVEN, IntDef.INT_TWO).getCellStyle());
        }
        // prepare header
        styleMap.put(HEADER_LOWEST_STOCK, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_ONE)
            .getCellStyle());
        styleMap.put(HEADER_LATEST_TARGET_MONTH, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_FIVE)
            .getCellStyle());
        styleMap.put(HEADER_DELIVERED_TO_CUST, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_TEN)
            .getCellStyle());
        styleMap.put(HEADER_CUSTOMER_USAGE, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_ELEVEN)
            .getCellStyle());
        styleMap.put(HEADER_NOT_IN_RUNDOWN, PoiUtil.getCell(styleSheet, IntDef.INT_TWELVE, IntDef.INT_TWELVE)
            .getCellStyle());
        styleMap.put(HEADER_NOT_IN_RUNDOWN_HEADER, PoiUtil.getCell(styleSheet, IntDef.INT_ELEVEN, IntDef.INT_THIRTEEN)
            .getCellStyle());
        styleMap.put(HEADER_NOT_IN_RUNDOWN_DETIAL, PoiUtil.getCell(styleSheet, IntDef.INT_TWELVE, IntDef.INT_THIRTEEN)
            .getCellStyle());
        styleMap.put(HEADER_IMP_STOCK, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_SIXTEEN)
            .getCellStyle());
        styleMap.put(HEADER_STOCK_QTY, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_TWENTY_ONE)
            .getCellStyle());
        styleMap.put(HEADER_STOCK_DAYS, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_TWENTY_TWO)
            .getCellStyle());
        styleMap.put(HEADER_INVOICE_STYLE, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_TWENTY_FOUR)
            .getCellStyle());
        styleMap.put(HEADER_IPO_GREEN, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_TWENTY_FIVE)
            .getCellStyle());
        styleMap.put(HEADER_IPO_PINK, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_TWENTY_SIX)
            .getCellStyle());
        styleMap.put(HEADER_IPO_ORANGE, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_TWENTY_SEVEN)
            .getCellStyle());
        // prepare Decimal
        StringBuffer sb = new StringBuffer();
        for (int i = IntDef.INT_ZERO; i < IntDef.INT_SEVEN; i++) {
            // get current row
            int row = IntDef.INT_TWO + i;
            // write
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_WHITE).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_THREE).getCellStyle());
            // red font
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_RED).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_FOUR).getCellStyle());
            // yellow
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_YELLOW).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_FIVE).getCellStyle());
            // red bold
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_RED_BOLD).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_SIX).getCellStyle());
            // pink
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_PINK).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_SEVEN).getCellStyle());
            // black
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_BLACK).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_EIGHT).getCellStyle());
        }
    }

    /**
     * prepare header content of all parts.
     *
     * @param workBook current work book
     * @param lang language
     * @param styleMap cell style map
     * @param headMap head information Map
     * @param rundownHeader run-down master entity
     */
    public static void setHeaderAllPart(Workbook workBook, Language lang, Map<String, CellStyle> styleMap,
        Map<Integer, String> headMap, TntRundownHeader rundownHeader) {
        // get cnt sheet
        Sheet cntSheet = workBook.getSheetAt(IntDef.INT_ZERO);
        // language
        String langName = lang.getName();
        // prepare header
        int row = RD_START_ROW_FOR_HEADER;
        // set header of Lowest Stock
        int col = setHeaderLowestStock(cntSheet, styleMap, lang, headMap, rundownHeader);
        // get date
        Date stockDate = rundownHeader.getStockDate();
        Date runStartDate = stockDate;//DateTimeUtil.addDay(stockDate, IntDef.INT_ONE);
        // reset style
        CellStyle style = styleMap.get(HEADER_LATEST_TARGET_MONTH);
        // set latest order target month
        PoiUtil.setCellStyle(cntSheet, row, col, style);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(LATEST_ORDER_TARGET_MONTH, langName));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_NINE));
        headMap.put(col, HEAD_LATEST_TARGET_MONTH);
        // Ending Stock days (Until Last Day of Target Month for Firm PO)
        PoiUtil.setCellStyle(cntSheet, row, ++col, style);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(ENDING_STOCK_DAYS, langName));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_TWELVE));
        // Safety Stock Days For Rundown and Stock Status Alarm 1&2
        PoiUtil.setCellStyle(cntSheet, row, ++col, style);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(SAFETY_STOCK_DAYS, langName));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_TWELVE));
        // Add On Qty to Avoid Shortage
        PoiUtil.setCellStyle(cntSheet, row, ++col, style);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(ADD_ON_AVOID_SHORTAGE, langName));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_TWELVE));
        // Add On Qty to Achieve Safety Stock Level
        PoiUtil.setCellStyle(cntSheet, row, ++col, style);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(ADD_ON_AVOID_SAFTY, langName));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_TWELVE));
        // reset style
        style = styleMap.get(HEADER_DELIVERED_TO_CUST);
        // actual out-bound(Delivered To Customer)
        PoiUtil.setCellStyle(cntSheet, row, ++col, style);
        String monthStr = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MMM,
            DateTimeUtil.addMonth(runStartDate, IntDef.INT_N_ONE), lang);
        PoiUtil.setCellValue(cntSheet, row, col, MessageFormat.format(
            MessageManager.getMessage(DELIVERED_TO_CUSTOMER, langName).replaceAll("<br>", "\n"),
            new Object[] { monthStr }));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_NINE));
        // set customer usage header
        col = setHeaderOfUsage(cntSheet, lang, styleMap, headMap, rundownHeader, ++col);
        // set not in run-down header
        col = setHeaderOfNotInRd(cntSheet, lang, styleMap, headMap, rundownHeader, col);
        // reset
        style = styleMap.get(HEADER_IMP_STOCK);
        // set Customer Stock Qty
        PoiUtil.setCellStyle(cntSheet, row, ++col, style);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(CUSTOMER_STOCK_QTY, langName));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_NINE));
        // Customer Stock Date
        PoiUtil.setCellStyle(cntSheet, row, ++col, style);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(CUSTOMER_STOCK_DATE, langName));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_NINE));
        // Total Import Stock Qty
        PoiUtil.setCellStyle(cntSheet, row, ++col, style);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(TOTAL_IMPORT_STOCK, langName));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_NINE));
        // ECI on hold
        PoiUtil.setCellStyle(cntSheet, row, ++col, style);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(ECI_ON_HOLD, langName)); 
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_NINE));
        // Available Import Stock (Not include ECI on-hold in Warehouse)
        PoiUtil.setCellStyle(cntSheet, row, ++col, style);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(AVAILABLE_IMPORT_STOCK, langName));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_TWELVE));
        // Already Prepared for Warehouse Outbound
        PoiUtil.setCellStyle(cntSheet, row, ++col, style);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(PREPARED_OUTBOUND, langName));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_TWELVE));
        // group column
        cntSheet.groupColumn(col - IntDef.INT_THREE, col - IntDef.INT_ONE);
        cntSheet.setColumnGroupCollapsed(col - IntDef.INT_TWO, true);
        // set run-down detail information header
        setHeaderOfRdDetail(workBook, cntSheet, lang, styleMap, headMap, rundownHeader, ++col);
    }

    /**
     * prepare header content of lowest stock information.
     *
     * @param cntSheet current Sheet
     * @param styleMap cell style map
     * @param lang Language
     * @param headMap head information Map
     * @param rundownHeader run-down master entity
     * @return last column
     */
    private static int setHeaderLowestStock(Sheet cntSheet, Map<String, CellStyle> styleMap, Language lang,
        Map<Integer, String> headMap, TntRundownHeader rundownHeader) {

        // prepare header
        int col = RD_START_COL_LOWTEST_INFO;
        int row = RD_START_ROW_FOR_HEADER;
        // prepare Latest Stock
        Date stockDate = rundownHeader.getStockDate();
        Date runStartDate = stockDate;//DateTimeUtil.addDay(stockDate, IntDef.INT_ONE);
        Date lastRundDate = rundownHeader.getLatestRunEndDate();

        // get string
        String lowestDaysStr = MessageManager.getMessage(LOWEST_STOCK_DAYS, lang.getName()).replaceAll("<br>", "\n");
        String lowestBoxStr = MessageManager.getMessage(LOWEST_STOCK_BOX, lang.getName()).replaceAll("<br>", "\n");
        String stockDateStr = MessageManager.getMessage(STOCK_DATE, lang.getName()).replaceAll("<br>", "\n");
        String addOnStr = MessageManager.getMessage(ADD_ON_QTY, lang.getName()).replaceAll("<br>", "\n");
        String monthStr = null;
        // get Month diff
        int monthDiff = DateTimeUtil.getDiffMonths(runStartDate, lastRundDate);
        // set header of Lowest Stock
        CellStyle style = styleMap.get(HEADER_LOWEST_STOCK);
        // loop each lowest stock of month
        for (int i = IntDef.INT_ZERO; i <= monthDiff; i++) {
            // current month
            Date loopMonth = DateTimeUtil.addMonth(runStartDate, i);
            // Month String
            monthStr = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MMM, loopMonth, lang);
            // set into map
            StringBuffer key = new StringBuffer(HEAD_LOWEST_STOCK).append(StringConst.NEW_COMMA)
                .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, loopMonth));
            headMap.put(col, key.toString());
            // Lowest Stock Days
            PoiUtil.setCellStyle(cntSheet, row, col, style);
            PoiUtil.setCellValue(cntSheet, row, col, MessageFormat.format(lowestDaysStr, new Object[] { monthStr }));
            cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHT));
            // Lowest Stock Box
            col++;
            PoiUtil.setCellStyle(cntSheet, row, col, style);
            PoiUtil.setCellValue(cntSheet, row, col, MessageFormat.format(lowestBoxStr, new Object[] { monthStr }));
            cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHT));
            // Stock Date
            col++;
            PoiUtil.setCellStyle(cntSheet, row, col, style);
            PoiUtil.setCellValue(cntSheet, row, col, MessageFormat.format(stockDateStr, new Object[] { monthStr }));
            cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHT));
            // Add-on Qty (Mar)
            col++;
            PoiUtil.setCellStyle(cntSheet, row, col, style);
            PoiUtil.setCellValue(cntSheet, row, col, MessageFormat.format(addOnStr, new Object[] { monthStr }));
            cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHT));
            // set group
            cntSheet.groupColumn(col - IntDef.INT_TWO, col - IntDef.INT_ONE);
            cntSheet.setColumnGroupCollapsed(col - IntDef.INT_TWO, true);
            col++;
        }
        // return
        return col;
    }

    /**
     * prepare header content of customer usage information.
     *
     * @param cntSheet current Sheet
     * @param lang language
     * @param styleMap cell style map
     * @param headMap head information Map
     * @param rundownHeader run-down master entity
     * @param stCol start column
     * @return last column
     */
    private static int setHeaderOfUsage(Sheet cntSheet, Language lang, Map<String, CellStyle> styleMap,
        Map<Integer, String> headMap, TntRundownHeader rundownHeader, int stCol) {

        // prepare header
        int col = stCol;
        int row = RD_START_ROW_FOR_HEADER;

        // get run end date
        Date lastRundDate = rundownHeader.getLatestRunEndDate();
        // run end month
        String runEndMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, lastRundDate);
        String custUsageStr = MessageManager.getMessage(CUSTOMER_USAGE, lang.getName()).replaceAll("<br>", "\n");
        // get share list
        List<TntRdAttachCfc> shareList = rundownHeader.getShareList();

        // get style
        CellStyle groupStyle = styleMap.get(HEADER_DELIVERED_TO_CUST);
        CellStyle usageStyle = styleMap.get(HEADER_CUSTOMER_USAGE);
        CellStyle currentStyle = null;

        // get size
        int size = shareList.size() - IntDef.INT_ONE;
        int lastCol = col;
        // loop
        for (int i = IntDef.INT_ZERO; i <= size; i++) {
            // get share information
            TntRdAttachCfc shareInfo = shareList.get(i);
            // if more than runEndMonth, break;
            if (runEndMonth.compareTo(shareInfo.getCfcMonth()) < IntDef.INT_ZERO) {
                break;
            }

            // get share data
            if (i == size) {
                // set style
                currentStyle = groupStyle;
                // group
                if (col > lastCol) {
                    cntSheet.groupColumn(lastCol - IntDef.INT_ONE, col - IntDef.INT_TWO);
                    cntSheet.setColumnGroupCollapsed(col - IntDef.INT_TWO, true);
                }
            } else {
                // get next
                TntRdAttachCfc nextShareInfo = shareList.get(i + IntDef.INT_ONE);

                // check same month?
                if (shareInfo.getCfcMonth().equals(nextShareInfo.getCfcMonth())) {
                    // same
                    currentStyle = usageStyle;
                } else {
                    // next month
                    currentStyle = groupStyle;
                    // group
                    if (col > lastCol) {
                        cntSheet.groupColumn(lastCol - IntDef.INT_ONE, col - IntDef.INT_TWO);
                        cntSheet.setColumnGroupCollapsed(col - IntDef.INT_TWO, true);
                    }

                    // reset lastCol
                    lastCol = col + IntDef.INT_ONE;
                }
            }

            // get cfc month string
            String cfcMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MMM,
                DateTimeUtil.parseDate(shareInfo.getCfcMonth(), DateTimeUtil.FORMAT_YEAR_MONTH), lang);
            // according language, format share date
            //String format = lang.equals(Language.CHINESE) ? DateTimeUtil.FORMAT_DDMMM : DateTimeUtil.FORMAT_MMDD;
            String shareDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, shareInfo.getShareDate());
            // set value
            PoiUtil.setCellStyle(cntSheet, row, col, currentStyle);
            PoiUtil.setCellValue(cntSheet, row, col,
                MessageFormat.format(custUsageStr, new Object[] { cfcMonth, shareDate }));
            cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHT));

            // set into head map
            StringBuffer value = new StringBuffer(HEAD_USAGE_SHARE);
            value.append(StringConst.NEW_COMMA).append(shareInfo.getCfcMonth());
            value.append(StringConst.NEW_COMMA)
                .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, shareInfo.getShareDate()));
            value.append(StringConst.NEW_COMMA).append(
                currentStyle.equals(groupStyle) ? IntDef.INT_ONE : IntDef.INT_ZERO);

            // put into database
            headMap.put(col, value.toString());

            // next column
            col++;
        }

        return --col;
    }

    /**
     * prepare header content of not in run-down information.
     *
     * @param cntSheet current Sheet
     * @param lang language
     * @param styleMap cell style map
     * @param headMap head information Map
     * @param rundownHeader run-down master entity
     * @param stCol start column
     * @return last column
     */
    private static int setHeaderOfNotInRd(Sheet cntSheet, Language lang, Map<String, CellStyle> styleMap,
        Map<Integer, String> headMap, TntRundownHeader rundownHeader, int stCol) {

        // prepare header
        int col = stCol;
        int row = RD_START_ROW_FOR_NOT_IN;
        // get style
        CellStyle headerStyle = styleMap.get(HEADER_NOT_IN_RUNDOWN);
        // Not in Rundown Reason
        PoiUtil.setCellStyle(cntSheet, ++row, col, headerStyle);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(NOT_IN_RUNDOWN_REASON, lang.getName()));
        // Invoice No.
        PoiUtil.setCellStyle(cntSheet, ++row, col, headerStyle);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(INVOICE_NO, lang.getName()));
        // Order No./Kanban Plan No.
        PoiUtil.setCellStyle(cntSheet, ++row, col, headerStyle);
        PoiUtil.setCellValue(cntSheet, row, col,
            MessageManager.getMessage(ORDER_NO, lang.getName()).replaceAll("<br>", "\n"));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_NINE));

        // get detail style(Detail column start)
        col++;
        CellStyle detailStyle = styleMap.get(HEADER_NOT_IN_RUNDOWN_DETIAL);
        CellStyle detailStyleCent = styleMap.get(HEADER_NOT_IN_RUNDOWN_HEADER);

        // get not in run down information list
        List<TntNotInRundownEx> notInList = rundownHeader.getNotInRundownList();
        // get size
        int size = notInList.size();
        // loop
        for (int i = IntDef.INT_ZERO; i < size; i++) {
            // get share information
            TntNotInRundownEx notInInfo = notInList.get(i);

            // reset key
            StringBuffer dataKey = new StringBuffer(HEAD_NOT_IN_RUNDOWN);
            dataKey.append(StringConst.NEW_COMMA).append(notInInfo.getReasonType());
            dataKey.append(StringConst.NEW_COMMA).append(
                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, notInInfo.getImpInbDate()));
            dataKey.append(StringConst.NEW_COMMA).append(
                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, notInInfo.getEtd()));
            dataKey.append(StringConst.NEW_COMMA).append(
                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, notInInfo.getEta()));
            dataKey.append(StringConst.NEW_COMMA).append(StringUtil.toSafeString(notInInfo.getImpPoNo()));
            dataKey.append(StringConst.NEW_COMMA).append(StringUtil.toSafeString(notInInfo.getInvoiceNo()));
            dataKey.append(StringConst.BLANK);

            // we could use min and max get last vanning Date and first vanning Date
            headMap.put(col, dataKey.toString());
            setHeaderDetailOfNotInRd(cntSheet, detailStyle, detailStyleCent, lang, notInInfo, col);

            // next column
            col++;
        }

        // merge cell
        if (size > IntDef.INT_ZERO) {

            // reset row
            row = RD_START_ROW_FOR_NOT_IN;
            // set value
            PoiUtil.setCellValue(cntSheet, row, stCol + IntDef.INT_ONE,
                MessageManager.getMessage(NOT_IN_RUNDOWN, lang.getName()));

            // merge
            row -= IntDef.INT_ONE;
            CellRangeAddress address = new CellRangeAddress(row, row, stCol, col - IntDef.INT_TWO);
            cntSheet.addMergedRegion(address);

            // group
            cntSheet.groupColumn(stCol, col - IntDef.INT_TWO);
        }

        // set total
        row = RD_START_ROW_FOR_HEADER;
        PoiUtil.setCellStyle(cntSheet, row, col, detailStyleCent);
        PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(TOTAL_NOT_IN_RUNDOWN, lang.getName()));
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_NINE));
        headMap.put(col, HEAD_TOTAL_NOT_IN_RUNDOWN);

        // return
        return col;
    }

    /**
     * prepare header content of not in run-down detail information.
     *
     * @param cntSheet current Sheet
     * @param headerStyle style
     * @param headerStyleCnt style
     * @param lang Language
     * @param notInInfo not in rundown information
     * @param col column
     */
    private static void setHeaderDetailOfNotInRd(Sheet cntSheet, CellStyle headerStyle, CellStyle headerStyleCnt, Language lang,
        TntNotInRundownEx notInInfo, int col) {

        // prepare header
        int row = RD_START_ROW_FOR_NOT_IN;
        
        // set value
        PoiUtil.setCellStyle(cntSheet, row, col, headerStyleCnt);

        // set Not in Run-down Reason
        PoiUtil.setCellStyle(cntSheet, ++row, col, headerStyle);
        if (notInInfo.getReasonType().equals(NotInRDReasonType.ETD_DELAY_A)) {
            // set header
            PoiUtil.setCellValue(cntSheet, row, col, MessageFormat.format(
                MessageManager.getMessage(ETD_DELAY, lang.getName()), new Object[] { StringConst.ALPHABET_A }));
        } else if (notInInfo.getReasonType().equals(NotInRDReasonType.ETD_DELAY_B)) {
            // set header
            PoiUtil.setCellValue(cntSheet, row, col, MessageFormat.format(
                MessageManager.getMessage(ETD_DELAY, lang.getName()), new Object[] { StringConst.ALPHABET_B }));
        } else if (notInInfo.getReasonType().equals(NotInRDReasonType.INBOUND_DELAY)) {
            // set header
            PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(INBOUND_DELAY, lang.getName()));
        } else {
            // set header
            PoiUtil.setCellValue(cntSheet, row, col, MessageManager.getMessage(NG_ON_HOLD, lang.getName()));
        }

        // Invoice No.
        PoiUtil.setCellStyle(cntSheet, ++row, col, headerStyle);
        if (notInInfo.getInvoiceNo() != null) {
            PoiUtil.setCellValue(cntSheet, row, col, notInInfo.getInvoiceNo());
        }

        // Order No./Kanban Plan No.
        PoiUtil.setCellStyle(cntSheet, ++row, col, headerStyle);
        if (notInInfo.getImpPoNo() != null) {
            PoiUtil.setCellValue(cntSheet, row, col, notInInfo.getImpPoNo());
        }

        // set shipping information
        StringBuffer shipInfo = new StringBuffer();
        // if vanning date is not null, mean is AISIN parts
        if (notInInfo.getVanningDate() != null) {
            // append aision information
            shipInfo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, notInInfo.getVanningDate()));
            // if has next vanning date 
            if (notInInfo.getLastVanDate() != null && notInInfo.getLastVanDate().after(notInInfo.getVanningDate())) {

                // set last Van
                shipInfo.append(StringConst.MIDDLE_LINE);
                shipInfo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, notInInfo.getLastVanDate()));
            }
            shipInfo.append(VAN);
            shipInfo.append(StringConst.NEWLINE);
        }
        // append etd
        shipInfo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, notInInfo.getEtd()));
        shipInfo.append(ETD);
        shipInfo.append(StringConst.NEWLINE);
        // append eta
        shipInfo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, notInInfo.getEta()));
        shipInfo.append(ETA);
        shipInfo.append(StringConst.NEWLINE);
        // append imp inbound date
        shipInfo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, notInInfo.getImpInbDate()));
        shipInfo.append(StringConst.MIDDLE_LINE);
        if (!notInInfo.getReasonType().equals(NotInRDReasonType.NG_ONHOLD)) {
            shipInfo.append(MessageManager.getMessage(PLAN_INBOUND, lang.getName()));
        } else {
            shipInfo.append(MessageManager.getMessage(ACTUAL_INBOUND, lang.getName()));
        }

        // set shipping information into excel
        PoiUtil.setCellStyle(cntSheet, ++row, col, headerStyleCnt);
        PoiUtil.setCellValue(cntSheet, row, col, shipInfo.toString());
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_FIFTEEN));
    }

    /**
     * prepare header content of not in run-down information.
     *
     * @param workBook current work book
     * @param cntSheet current Sheet
     * @param lang language
     * @param styleMap cell style map
     * @param headMap head information Map
     * @param rundownHeader run-down master entity
     * @param stCol start column
     */
    private static void setHeaderOfRdDetail(Workbook workBook, Sheet cntSheet, Language lang,
        Map<String, CellStyle> styleMap, Map<Integer, String> headMap, TntRundownHeader rundownHeader, int stCol) {

        // prepare header
        int row = RD_START_ROW_FOR_HEADER;
        int col = stCol;
        // define
        Date loopDate = rundownHeader.getStockDate();
        Date endDate = rundownHeader.getLatestRunEndDate();
        // style
        CellStyle qtyStyle = styleMap.get(HEADER_STOCK_QTY);
        CellStyle daysStyle = styleMap.get(HEADER_STOCK_DAYS);
        CellStyle yellowStyle = styleMap.get(HEADER_INVOICE_STYLE);
        CellStyle greenStyle = styleMap.get(HEADER_IPO_GREEN);
        CellStyle pinkStyle = styleMap.get(HEADER_IPO_PINK);
        CellStyle orangeStyle = styleMap.get(HEADER_IPO_ORANGE);
        
        // set map
        Map<String, CellStyle> alternateSyeMap = new HashMap<String, CellStyle>();
        // get color
        CellStyle[] clrIdxArray = new CellStyle[] { greenStyle, pinkStyle, orangeStyle };

        // attched information list and index
        int idx = IntDef.INT_ZERO;
        int clrIdx = IntDef.INT_ZERO;
        List<TntRdDetailAttachEx> attachedList = rundownHeader.getRdDetailAttachList();
        String stockQtyStr = MessageManager.getMessage(STOCK_QTY, lang.getName()).replaceAll("<br>", "\n");
        String stockDaysStr = MessageManager.getMessage(STOCK_DAYS, lang.getName());
        
        // prepare style of master
        if (attachedList != null && !attachedList.isEmpty()) {
            // get month list
            List<String> monthList = new ArrayList<String>();
            // loop attached information
            for (int i = 0; i < attachedList.size(); i++) {
                // get attached information
                TntRdDetailAttachEx attached = attachedList.get(i);
                // if not on-shipping
                if (!attached.getAttachType().equals(AttachType.ON_SHIPPING)) {
                    // get month
                    String poMonth = attached.getAttachType().equals(AttachType.PLAN) ? attached.getOrderMonth()
                        : attached.getPfcMonth();
                    // add into list
                    if (!monthList.contains(poMonth)) {
                        monthList.add(poMonth);
                    }
                }
            }

            // check
            if (!monthList.isEmpty()) {
                // sort
                Collections.sort(monthList);
                for (String month : monthList) {
                    // set into alternateSyeMap
                    alternateSyeMap.put(month, clrIdxArray[clrIdx++ % clrIdxArray.length]);
                }
            }
        }

        // loop
        while (!loopDate.after(endDate)) {
            // if has attached information
            if (attachedList != null && !attachedList.isEmpty()) {
                // loop attached information
                for (; idx < attachedList.size(); idx++) {
                    // get attached information
                    TntRdDetailAttachEx attached = attachedList.get(idx);
                    if (attached.getImpInbPlanDate().after(loopDate)) {
                        break;
                    } else if (attached.getImpInbPlanDate().before(loopDate)) {
                        continue;
                    }

                    // get style
                    CellStyle headerStyle = null;
                    String poMonth = null;
                    if (attached.getAttachType().equals(AttachType.ON_SHIPPING)) {
                        // get month
                        poMonth = attached.getOrderMonth();
                        // all on-shipping information will use yellow color
                        headerStyle = yellowStyle;
                    } else {
                        // change style(Imp Po and forecast will change by each order month)
                        if (attached.getAttachType().equals(AttachType.PLAN)) {
                            poMonth = attached.getOrderMonth();
                        } else {
                            poMonth = attached.getPfcMonth();
                        }
                        
                        // get style
                        headerStyle = alternateSyeMap.get(poMonth);
                    }
                    // set detail attached information
                    setHeaderDetailAttached(cntSheet, headerStyle, lang, attached, col);
                    // prepare key
                    StringBuffer dataKey = new StringBuffer(HEAD_RUNDOWN_DETAIL_ATTACH);
                    dataKey.append(StringConst.NEW_COMMA).append(
                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, attached.getImpInbPlanDate()));
                    dataKey.append(StringConst.NEW_COMMA).append(
                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, attached.getEtd()));
                    dataKey.append(StringConst.NEW_COMMA).append(
                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, attached.getEta()));
                    dataKey.append(StringConst.NEW_COMMA).append(StringUtil.toSafeString(attached.getInvoiceNo()));
                    dataKey.append(StringConst.NEW_COMMA).append(StringUtil.toSafeString(attached.getImpPoNo()));
                    dataKey.append(StringConst.NEW_COMMA).append(StringUtil.toSafeString(attached.getOrderMonth()));
                    dataKey.append(StringConst.NEW_COMMA).append(StringUtil.toSafeString(attached.getPfcMonth()));
                    dataKey.append(StringConst.NEW_COMMA).append(attached.getAttachType());
                    // set into map
                    headMap.put(col, dataKey.toString());
                    // next column
                    col++;
                }
            }
            
            // set into head map
            StringBuffer dataKey = new StringBuffer(HEAD_RUNDOWN_DETAIL);
            dataKey.append(StringConst.NEW_COMMA).append(
                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, loopDate));
            headMap.put(col, dataKey.toString());
            
            // Stock Qty
            String loopDateStr = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, loopDate);
            PoiUtil.setCellStyle(cntSheet, row, col, qtyStyle);
            PoiUtil.setCellValue(cntSheet, row, col, MessageFormat.format(stockQtyStr, new Object[] { loopDateStr }));
            cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHT));
            // Stock days
            PoiUtil.setCellStyle(cntSheet, row, ++col, daysStyle);
            PoiUtil.setCellValue(cntSheet, row, col, stockDaysStr);
            cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHT));

            // next date
            loopDate = DateTimeUtil.addDay(loopDate, IntDef.INT_ONE);
            // next column
            col++;
        }
    }

    /**
     * prepare header content of rundown detail attached information.
     *
     * @param cntSheet current Sheet
     * @param headerStyle headerStyle
     * @param lang Language
     * @param attached run-down attached information
     * @param col column
     */
    private static void setHeaderDetailAttached(Sheet cntSheet, CellStyle headerStyle, Language lang,
        TntRdDetailAttachEx attached, int col) {

        // prepare header
        int row = RD_START_ROW_FOR_HEADER;
        // set invoice and IMP PO information
        StringBuffer headStr = new StringBuffer();
        // if has imp po
        if (attached.getImpPoNo() != null) {
            // if has invoice
            if (attached.getInvoiceNo() != null) {
                // set invoice
                headStr.append(attached.getInvoiceNo()).append(StringConst.NEWLINE);
            }
            // set invoice
            headStr.append(attached.getImpPoNo());
        } else {
            // get forcast number
            Date orderMonth = DateTimeUtil.parseDate(attached.getOrderMonth(), DateTimeUtil.FORMAT_YEAR_MONTH);
            Date pfcMonth = DateTimeUtil.parseDate(attached.getPfcMonth(), DateTimeUtil.FORMAT_YEAR_MONTH);
            int monthDiff = DateTimeUtil.getDifferMonth(orderMonth, pfcMonth);

            // is forecast
            headStr.append(MessageFormat.format(MessageManager.getMessage(FORECAST_LABEL, lang.getName()),
                new Object[] { monthDiff }));
        }
        // set header string
        PoiUtil.setCellStyle(cntSheet, row - IntDef.INT_ONE, col, headerStyle);
        PoiUtil.setCellValue(cntSheet, row - IntDef.INT_ONE, col, headStr.toString());

        // set shipping information
        StringBuffer shippingInfo = new StringBuffer();
        // if vanning date is not null, mean is AISIN parts
        if (attached.getVanningDate() != null) {
            // append aision information
            shippingInfo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, attached.getVanningDate()));
            // if has next vanning date 
            if (attached.getLastVanDate() != null && attached.getLastVanDate().after(attached.getVanningDate())) {
                // set last Van
                shippingInfo.append(StringConst.MIDDLE_LINE);
                shippingInfo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, attached.getLastVanDate()));
            }
            shippingInfo.append(VAN).append(StringConst.NEWLINE);
        }
        // append etd
        shippingInfo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, attached.getEtd()));
        shippingInfo.append(ETD).append(StringConst.NEWLINE);
        // append eta
        if (attached.getEta() != null) {
            shippingInfo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, attached.getEta()));
            shippingInfo.append(ETA).append(StringConst.NEWLINE);
        }
        // append imp inbound date
        shippingInfo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MM_DD, attached.getImpInbPlanDate()));
        shippingInfo.append(StringConst.MIDDLE_LINE).append(MessageManager.getMessage(PLAN_INBOUND, lang.getName()));
        // set shipping information into excel
        PoiUtil.setCellStyle(cntSheet, row, col, headerStyle);
        PoiUtil.setCellValue(cntSheet, row, col, shippingInfo.toString());
        cntSheet.setColumnWidth(col - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_FIFTEEN));
    }

    /**
     * Set run-down detail information by each parts.
     *
     * @param workbook work book
     * @param rdMasterList run-down master list
     * @param lang language
     * @param styleMap style map
     * @param headMap head map
     * @param office office code
     * @param dateList dateList
     */
    public static void setRundownDetailInfo(SXSSFWorkbook workbook, List<TntRundownMasterEx> rdMasterList,
        Language lang, Map<String, CellStyle> styleMap, Map<Integer, String> headMap, String office, Date[] dateList) {

        // get sheet
        Sheet cntSheet = workbook.getSheetAt(IntDef.INT_ZERO);

        // get head map key set
        Set<Integer> colSet = headMap.keySet();
        List<Integer> colList = new ArrayList<Integer>(colSet);
        Collections.sort(colList);

        // loop list and set value
        int row = RD_START_ROW_FOR_DATA;
        int colIdx = IntDef.INT_ZERO;
        String noCustForecast = MessageManager.getMessage(NO_CUSTOMER_FORECAST, lang.getName());
        String noCustStock = MessageManager.getMessage(NO_CUSTOMER_STOCK, lang.getName());

        // loop each run-down master
        for (TntRundownMasterEx rundownMaster : rdMasterList) {
            // reset column
            setPartsInfoByEachRun(cntSheet, rundownMaster, lang, styleMap, row);
            // reset lowest stock information
            colIdx = setLowestInfoByEachRun(cntSheet, rundownMaster, styleMap, lang, headMap, row, colList);
            // get column
            int col = colList.get(colIdx).intValue();
            // Latest Order Target Month
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(MONTH_PREFIX));
            Date targetMonth = DateTimeUtil.parseDate(rundownMaster.getTargetMonth(), DateTimeUtil.FORMAT_YEAR_MONTH);
            PoiUtil.setCellValue(cntSheet, row, col++, targetMonth);
            
            // get run end date
            Date lastDateOfTarget = DateTimeUtil.lastDay(targetMonth);
            // if not after run start date
            if (!lastDateOfTarget.before(rundownMaster.getRunStartDate())) {

                // Ending Stock days (Until Last Day of Target Month for Firm PO)
                if (rundownMaster.getLastStockDays() == null) {
                    // set last stock days
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                } else {
                    // set last stock days
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + IntDef.INT_ONE));
                    PoiUtil.setCellValue(cntSheet, row, col++, rundownMaster.getLastStockDays());
                }
                // Safety Stock Days For Run-down and Stock Status Alarm 1&2
                PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + IntDef.INT_ONE));
                PoiUtil.setCellValue(cntSheet, row, col++, rundownMaster.getRundownSafetyStock());
                // Add On Qty to Avoid Shortage
                if (rundownMaster.getLastShortAddOnQty() == null) {
                    // set last stock days
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                } else {
                    // set last stock days
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundownMaster.getDigit()));
                    PoiUtil.setCellValue(cntSheet, row, col++, rundownMaster.getLastShortAddOnQty());
                }
                // Add On Qty to Achieve Safety Stock Level
                if (rundownMaster.getLastSaftyAddOnQty() == null) {
                    // set last stock days
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                } else {
                    // set last stock days
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundownMaster.getDigit()));
                    PoiUtil.setCellValue(cntSheet, row, col++, rundownMaster.getLastSaftyAddOnQty());
                }
            } else {

                // set last stock days
                PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
                // Safety Stock Days For Run-down and Stock Status Alarm 1&2
                PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + IntDef.INT_ONE));
                PoiUtil.setCellValue(cntSheet, row, col++, rundownMaster.getRundownSafetyStock());
                // Add On Qty to Avoid Shortage
                // set last stock days
                PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
                // Add On Qty to Achieve Safety Stock Level
                PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
            }
          
            // Delivered To Customer
            // set last stock days
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_YELLOW + rundownMaster.getDigit()));
            PoiUtil.setCellValue(cntSheet, row, col++, rundownMaster.getDeliQtyOfLastMonth());

            // Customer Usage
            colIdx = setCustShareByEachRun(cntSheet, rundownMaster, styleMap, lang, headMap, row, colList, ++colIdx);

            // Not in Run-down Reason
            colIdx = setNotInRunDownEachRun(cntSheet, rundownMaster, styleMap, headMap, row, colList, colIdx);

            // get column
            col = colList.get(colIdx).intValue();

            // Total Not In Run down Qty
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundownMaster.getDigit()));
            // get not in run down qty
            BigDecimal notInRunQty = DecimalUtil
                .add(rundownMaster.getEtdDelayQty(), rundownMaster.getInboundDelayQty());
            notInRunQty = DecimalUtil.add(notInRunQty, rundownMaster.getNgQty());
            PoiUtil.setCellValue(cntSheet, row, col++, notInRunQty);

            // Customer Stock Qty
            if (rundownMaster.getSaCustStockFlag().equals(CustStockFlag.N)) {
                // set as gray
                // Customer Stock Qty
                PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
                // Customer Stock Date
                PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
            } else {
                // if no customer stock?
                if (rundownMaster.getCustStockQty() != null) {
                    // Customer Stock Qty
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundownMaster.getDigit()));
                    PoiUtil.setCellValue(cntSheet, row, col++, rundownMaster.getCustStockQty());

                    // Customer Stock Date
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DATE_WHITE_PREFIX));
                    PoiUtil.setCellValue(cntSheet, row, col++, rundownMaster.getCustStockEndDate());
                } else {
                    // Customer Stock Qty
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustStock);

                    // Customer Stock Date
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustStock);
                }
            }

            // Total Import Stock Qty
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundownMaster.getDigit()));
            StringBuffer formula = new StringBuffer("SUM(");
            formula.append(CellReference.convertNumToColString(col));
            formula.append(row).append(StringConst.COLON);
            formula.append(CellReference.convertNumToColString(col + IntDef.INT_TWO));
            formula.append(row).append(StringConst.RIGHT_BRACKET);
            PoiUtil.setCellFormula(cntSheet, row, col++, formula.toString());
            // ECI On-Hold
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundownMaster.getDigit()));
            PoiUtil.setCellValue(cntSheet, row, col++, rundownMaster.getEciOnholdQty());
            // Available Import Stock(Exclude ECI On-Hold In W/H)
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundownMaster.getDigit()));
            PoiUtil.setCellValue(cntSheet, row, col++,
                DecimalUtil.add(rundownMaster.getInRackQty(), rundownMaster.getImpWhsQty()));
            // Already Prepared for Warehouse Outbound
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundownMaster.getDigit()));
            PoiUtil.setCellValue(cntSheet, row, col++, rundownMaster.getPreparedObQty());

            // prepare run-down detail information
            setDetailInfoByEachRun(cntSheet, rundownMaster, styleMap, lang, headMap, row, colList, ++colIdx);

            // next sequence
            row++;
        }
        // set download time:
        row++;
        // set download time
        String targetFormat = lang.equals(Language.CHINESE) ? DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMMSS
            : DateTimeUtil.FORMAT_DDMMMYYYYHHMMSS;
        String dateStr = DateTimeUtil.formatDate(targetFormat, dateList[IntDef.INT_ZERO]);
        String timeStr = MessageManager.getMessage(DOWNLOAD_TIME, lang.getName());
        timeStr = MessageFormat.format(timeStr, new Object[] { dateStr, office });
        PoiUtil.setCellStyle(cntSheet, row, IntDef.INT_ONE, styleMap.get(NORMAL_LABEL_WO_LINE));
        PoiUtil.setCellValue(cntSheet, row++, IntDef.INT_ONE, timeStr);
        
        // set last office data time of SSMS
        if (dateList[IntDef.INT_ONE] != null) {
            dateStr = DateTimeUtil.formatDate(targetFormat, dateList[IntDef.INT_ONE]);
            timeStr = MessageManager.getMessage(SYNC_SSMS_DATE_TIME, lang.getName());
            timeStr = MessageFormat.format(timeStr, new Object[] { dateStr });
            PoiUtil.setCellStyle(cntSheet, row, IntDef.INT_ONE, styleMap.get(NORMAL_LABEL_WO_LINE));
            PoiUtil.setCellValue(cntSheet, row++, IntDef.INT_ONE, timeStr);
        }
        // set last office data time of V-V
        if (dateList[IntDef.INT_TWO] != null) {
            dateStr = DateTimeUtil.formatDate(targetFormat, dateList[IntDef.INT_TWO]);
            timeStr = MessageManager.getMessage(SYNC_VV_DATE_TIME, lang.getName());
            timeStr = MessageFormat.format(timeStr, new Object[] { office, dateStr });
            PoiUtil.setCellStyle(cntSheet, row, IntDef.INT_ONE, styleMap.get(NORMAL_LABEL_WO_LINE));
            PoiUtil.setCellValue(cntSheet, row++, IntDef.INT_ONE, timeStr);
        }
        // set last office data time of AISIN
        if (dateList[IntDef.INT_THREE] != null) {
            dateStr = DateTimeUtil.formatDate(targetFormat, dateList[IntDef.INT_THREE]);
            timeStr = MessageManager.getMessage(SYNC_AISIN_DATE_TIME, lang.getName());
            timeStr = MessageFormat.format(timeStr, new Object[] { office, dateStr });
            PoiUtil.setCellStyle(cntSheet, row, IntDef.INT_ONE, styleMap.get(NORMAL_LABEL_WO_LINE));
            PoiUtil.setCellValue(cntSheet, row, IntDef.INT_ONE, timeStr);
        }

        // remove style sheet
        workbook.removeSheetAt(IntDef.INT_ONE);
        workbook.setForceFormulaRecalculation(true);
    }

    /**
     * prepare parts information for each run-down master.
     *
     * @param cntSheet current sheet
     * @param rundMaster run down master
     * @param lang language
     * @param styleMap style map
     * @param row row
     * @return last column
     */
    private static int setPartsInfoByEachRun(Sheet cntSheet, TntRundownMasterEx rundMaster, Language lang,
        Map<String, CellStyle> styleMap, int row) {

        // reset col
        int col = IntDef.INT_ONE;
        // get style
        CellStyle labelStyle = styleMap.get(LABEL_WHITE);
        CellStyle labelStyleYel = styleMap.get(LABEL_YELLOW);

        // MOD
        PoiUtil.setCellStyle(cntSheet, row, col++, labelStyle);
        // No.
        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + IntDef.INT_ZERO));
        PoiUtil.setCellValue(cntSheet, row, col++, row - RD_START_ROW_FOR_DATA + IntDef.INT_ONE);
        // TTC Part No.
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getTtcPartsNo());
        // Parts Description (English)
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getPartNameEn());
        // Parts Description (Chinese)
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getPartNameCn());
        // TTC Supplier Code
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getTtcSupplierCode());
        // Supplier Part No.
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getSupplierPartsNo());
        // Imp Office Code
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getOfficeCode());
        // TTC Customer Code
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getCustomerCode());
        // Customer Part No.
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getCustPartsNo());
        // Old Part No.
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, StringUtil.nullToEmpty(rundMaster.getOldPartsNo()));
        // Customer Back No.
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, StringUtil.nullToEmpty(rundMaster.getCustBackNo()));
        // SPQ
        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundMaster.getDigit()));
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getSpq());
        // Car Model
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, StringUtil.nullToEmpty(rundMaster.getCarModel()));
        // Part Type
        String partsType = CodeCategoryManager.getCodeName(lang.getCode(), CodeMasterCategory.PARTS_TYPE,
            rundMaster.getPartType());
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, partsType);
        // On Shipping Delay Pattern
        String delayPattern = CodeCategoryManager.getCodeName(lang.getCode(),
            CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P, rundMaster.getDelayAdjPattern());
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, delayPattern);
        // Build Out Indicator
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getBuildoutFlagName());
        // Build Out Month
        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DATE_WHITE_PREFIX));
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getBuildoutMonth());
        // Last PO Month
        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DATE_WHITE_PREFIX));
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getLastPoMonth());
        // Part Remark
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyle);
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getPartsRemark());
        // Rundown Remark
        PoiUtil.setCellStyle(cntSheet, row, col, labelStyleYel);
        PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getRemark());
        // Min Stock Days
        if (rundMaster.getInventoryBoxFlag().equals(InventoryByBox.N)) {
            // if N, then set value
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + IntDef.INT_ONE));
            PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getMinStock());
        } else {
            PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
        }
        // Min No. of Box
        if (rundMaster.getInventoryBoxFlag().equals(InventoryByBox.Y)) {
            // if Y, then set value
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + IntDef.INT_TWO));
            PoiUtil.setCellValue(cntSheet, row, col++, rundMaster.getMinBox());
        } else {
            PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
        }

        // return column
        return col;
    }

    /**
     * Prepare detail information for lowest information.
     *
     * @param cntSheet current sheet
     * @param rundMaster run-down master
     * @param styleMap style map
     * @param lang Language
     * @param headMap header information map
     * @param row row
     * @param colList column list
     * 
     * @return last column
     */
    private static int setLowestInfoByEachRun(Sheet cntSheet, TntRundownMasterEx rundMaster,
        Map<String, CellStyle> styleMap, Language lang, Map<Integer, String> headMap, int row, List<Integer> colList) {
        // define index
        int idx = IntDef.INT_ZERO;
        int dIdx = IntDef.INT_ZERO;
        // get lowest information from rundMaster
        List<TntRundownDetailEx> detailList = rundMaster.getRundownDetailList();
        boolean isBoxCon = rundMaster.getInventoryBoxFlag().equals(InventoryByBox.Y);
        Date lastTargetDate = DateTimeUtil.lastDay(DateTimeUtil.parseDate(rundMaster.getTargetMonth(),
            DateTimeUtil.FORMAT_YEAR_MONTH));
        // get forecast str
        String noCustForecast = MessageManager.getMessage(NO_CUSTOMER_FORECAST, lang.getName());
        String noOrderForecast = MessageManager.getMessage(NO_ORDER_FORECAST, lang.getName());
        String noForecastAll = MessageManager.getMessage(NO_FORECAST_ALL, lang.getName());

        // loop
        for (; idx < colList.size(); idx++) {
            // prepare key
            int col = colList.get(idx).intValue();
            // String values
            String[] splitValues = headMap.get(col).split(StringConst.NEW_COMMA);
            // if belong to lowest information
            if (!HEAD_LOWEST_STOCK.equals(splitValues[IntDef.INT_ZERO])) {
                // next step
                break;
            }

            // get current month
            String currMonth = splitValues[IntDef.INT_ONE];
            String runMonth = null;
            boolean noPfc = false;
            boolean noCfc = false;
            boolean noCfcForEnd = false;
            BigDecimal minStock = null;
            BigDecimal addOnQty = null;
            Date stockDate = null;
            // do loop
            for (; dIdx < detailList.size(); dIdx++) {
                // get next information
                TntRundownDetailEx detail = detailList.get(dIdx);
                // check last day of target month
                if (lastTargetDate.equals(detail.getImpInbPlanDate())) {
                    rundMaster.setLastStockDays(detail.getStockDays());
                    if (detail.getEndingStock() != null) {
                        if (DecimalUtil.isLess(detail.getEndingStock(), BigDecimal.ZERO)) {
                            rundMaster.setLastShortAddOnQty(detail.getEndingStock().abs());
                        } else {
                            rundMaster.setLastShortAddOnQty(BigDecimal.ZERO);
                        }
                    }
                    rundMaster.setLastSaftyAddOnQty(detail.getAddOnSaftyQty());
                }
                // do check month
                runMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, detail.getImpInbPlanDate());
                int comp = runMonth.compareTo(currMonth);
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }
                // check if no order forecast
                noPfc = detail.getNoPfcFlag().equals(NoPfcFlag.Y);
                // check if no customer forecast
                noCfc = (rundMaster.getEndCfcDate() == null || detail.getImpInbPlanDate().after(
                    rundMaster.getEndCfcDate()));
                // do next
                if (noCfc || noPfc) {
                    // reset
                    if (addOnQty == null || addOnQty.compareTo(BigDecimal.ZERO) == IntDef.INT_ZERO) {
                        minStock = null;
                        addOnQty = null;
                        stockDate = null;
                    }
                    // next
                    continue;
                }
                // get compare stock
                BigDecimal compStock = isBoxCon ? detail.getEndingStock() : detail.getStockDays();
                // start need delete
                compStock = compStock == null? BigDecimal.ZERO: compStock;
                // set values
                if (minStock == null || minStock.compareTo(compStock) > IntDef.INT_ZERO) {
                    // get add on qty
                    minStock = compStock;
                    // if has add on qty, means already under min stock
                    if (detail.getAddOnMinQty() != null) {
                        // set add on qty
                        stockDate = detail.getImpInbPlanDate();
                        addOnQty = detail.getAddOnMinQty();
                    }
                } else if (minStock.compareTo(compStock) == 0) {
                    // if min stock
                    if (detail.getAddOnMinQty() != null) {
                        // check
                        if (addOnQty != null && addOnQty.compareTo(detail.getAddOnMinQty()) < IntDef.INT_ZERO) {
                            // set add on qty
                            stockDate = detail.getImpInbPlanDate();
                            addOnQty = detail.getAddOnMinQty();
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
                // check is no customer stock?
                if (detail.getNoCfcFlag().equals(NoCfcFlag.Y)) {
                    noCfcForEnd = true;
                }
            }
            // Lowest Stock Days
            if (isBoxCon) {
                // set as blank
                PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
            } else {
                // check
                if (minStock != null) {
                    // set style
                    if (DecimalUtil.isLess(minStock, new BigDecimal(rundMaster.getMinStock()))) {
                        // set style
                        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_RED + IntDef.INT_ONE));
                    } else {
                        // set style
                        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + IntDef.INT_ONE));
                    }
                    PoiUtil.setCellValue(cntSheet, row, col++, minStock);
                } else {
                    // set data
                    if (runMonth == null) {
                        PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
                    } else {
                        // set style
                        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                        // set value
                        if (noCfc && noPfc) {
                            PoiUtil.setCellValue(cntSheet, row, col, noForecastAll);
                        } else if (noCfc) {
                            PoiUtil.setCellValue(cntSheet, row, col, noCustForecast);
                        } else if (noPfc) {
                            PoiUtil.setCellValue(cntSheet, row, col, noOrderForecast);
                        }
                        col++;
                    }
                }
            }
            // Lowest Stock Box
            if (isBoxCon) {
                // get box
                if (minStock != null) {
                    // get box
                    BigDecimal box = DecimalUtil.divide(minStock, rundMaster.getSpq(), IntDef.INT_ONE);
                    // check is under min box or not
                    if (DecimalUtil.isLess(box, new BigDecimal(rundMaster.getMinBox()))) {
                        // set style
                        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_RED + IntDef.INT_TWO));
                    } else {
                        // set style
                        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + IntDef.INT_TWO));
                    }
                    PoiUtil.setCellValue(cntSheet, row, col++, box);
                } else {
                    // set data
                    if (runMonth == null) {
                        PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
                    } else {
                        // next
                        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                        // set value
                        if (noCfc && noPfc) {
                            PoiUtil.setCellValue(cntSheet, row, col, noForecastAll);
                        } else if (noCfc) {
                            PoiUtil.setCellValue(cntSheet, row, col, noCustForecast);
                        } else if (noPfc) {
                            PoiUtil.setCellValue(cntSheet, row, col, noOrderForecast);
                        }
                        col++;
                    }
                }
            } else {
                PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
            }
            // set stock date and add on qty
            if (addOnQty == null) {
                // set data
                if (runMonth == null) {
                    // set gray
                    PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
                    PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
                } else if (noCfc && noPfc) {
                    // stock date
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noForecastAll);
                    // add on qty
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noForecastAll);
                } else if (noCfc || noCfcForEnd) {
                    // stock date
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                    // add on qty
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                } else if (noPfc) {
                    // stock date
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noOrderForecast);
                    // add on qty
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noOrderForecast);
                } else {
                    // stock date
                    PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_WHITE));
                    // add on qty
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE));
                    PoiUtil.setCellValue(cntSheet, row, col++, NA);
                }
            } else if (addOnQty.compareTo(BigDecimal.ZERO) > IntDef.INT_ZERO) {
                // Stock Date
                PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DATE_RED_FONT_PREFIX));
                PoiUtil.setCellValue(cntSheet, row, col++, stockDate);
                // Add-on Qty
                PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundMaster.getDigit()));
                PoiUtil.setCellValue(cntSheet, row, col++, addOnQty);
            } else if (addOnQty.compareTo(BigDecimal.ZERO) == IntDef.INT_ZERO) {
                // stock date
                PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_WHITE));
                // add on qty
                PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE));
                PoiUtil.setCellValue(cntSheet, row, col++, NA);
            }
        }

        // return index
        return idx;
    }

    /**
     * prepare customer share information by each run down master.
     *
     * @param cntSheet current sheet
     * @param rundMaster run-down master information
     * @param styleMap style map
     * @param lang Language
     * @param headMap head map
     * @param row row
     * @param colList column list
     * @param strIdx start index
     * @return end index
     */
    private static int setCustShareByEachRun(Sheet cntSheet, TntRundownMasterEx rundMaster,
        Map<String, CellStyle> styleMap, Language lang, Map<Integer, String> headMap, int row, List<Integer> colList,
        int strIdx) {

        // set start index
        int idx = strIdx;
        int dIdx = IntDef.INT_ZERO;
        // get share list
        List<TntRdAttachCfc> shareInfoList = rundMaster.getShareInfoList();
        // get forecast str
        String noCustForecast = MessageManager.getMessage(NO_CUSTOMER_FORECAST, lang.getName());

        // loop
        for (; idx < colList.size(); idx++) {
            // prepare key
            int col = colList.get(idx).intValue();
            // String values
            String[] splitValues = headMap.get(col).split(StringConst.NEW_COMMA);

            // if belong to customer usage information
            if (!HEAD_USAGE_SHARE.equals(splitValues[IntDef.INT_ZERO])) {
                // next step
                break;
            }

            // get cfcMonth
            String compCfcMonth = splitValues[IntDef.INT_ONE];
            String compShareDate = splitValues[IntDef.INT_TWO];
            Integer groupFlag = StringUtil.toInteger(splitValues[IntDef.INT_THREE]);
            boolean isGroup = groupFlag.equals(IntDef.INT_ONE) ? true : false;
            BigDecimal qty = null;

            // for each data
            for (; dIdx < shareInfoList.size(); dIdx++) {
                // get information
                TntRdAttachCfc shareInfo = shareInfoList.get(dIdx);

                // get keys
                String cfcMonth = shareInfo.getCfcMonth();
                String shareDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, shareInfo.getShareDate());
                // compare cfcMonth
                int comp = cfcMonth.compareTo(compCfcMonth);
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }
                // compare shareDate
                comp = shareDate.compareTo(compShareDate);
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }
                // set qty
                qty = shareInfo.getQty();
                break;
            }

            // set into database
            if (isGroup) {
                if (qty != null) {
                    // Add-on Qty
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_YELLOW + rundMaster.getDigit()));
                    PoiUtil.setCellValue(cntSheet, row, col++, qty);
                } else {
                    // Add-on Qty
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_YELLOW_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                }
            } else {
                if (qty != null) {
                    // Add-on Qty
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundMaster.getDigit()));
                    PoiUtil.setCellValue(cntSheet, row, col++, qty);
                } else {
                    // Add-on Qty
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                }
            }
        }

        return idx;
    }

    /**
     * prepare customer share information by each run down master.
     *
     * @param cntSheet current sheet
     * @param rundMaster run-down master information
     * @param styleMap style map
     * @param headMap head map
     * @param row row
     * @param colList column list
     * @param strIdx start index
     * @return end index
     */
    private static int setNotInRunDownEachRun(Sheet cntSheet, TntRundownMasterEx rundMaster,
        Map<String, CellStyle> styleMap, Map<Integer, String> headMap, int row, List<Integer> colList, int strIdx) {

        // set start index
        int idx = strIdx;
        int dIdx = IntDef.INT_ZERO;
        // get not in run-down list
        List<TntNotInRundownEx> notInRdInfoList = rundMaster.getNotInRundownList();

        // loop
        for (; idx < colList.size(); idx++) {
            // prepare key
            int col = colList.get(idx).intValue();
            // String values
            String[] splitValues = headMap.get(col).split(StringConst.NEW_COMMA, IntDef.INT_N_ONE);

            // if belong to customer usage information
            if (!HEAD_NOT_IN_RUNDOWN.equals(splitValues[IntDef.INT_ZERO])) {
                // next step
                break;
            }

            // get cfcMonth
            Integer reasonType = StringUtil.toInteger(splitValues[IntDef.INT_ONE]);
            String inbPlanDate = splitValues[IntDef.INT_TWO];
            String etd = splitValues[IntDef.INT_THREE];
            String eta = splitValues[IntDef.INT_FOUR];
            String impPoNo = splitValues[IntDef.INT_FIVE];
            String invoiceNo = splitValues[IntDef.INT_SIX].trim();
            
            BigDecimal qty = null;
            // foreach data
            for (; dIdx < notInRdInfoList.size(); dIdx++) {
                // get information
                TntNotInRundownEx notInRdInfo = notInRdInfoList.get(dIdx);
                
                // compare reasonType
                int comp = notInRdInfo.getReasonType().compareTo(reasonType);
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }
                
                // compare etd
                comp = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, notInRdInfo.getEtd()).compareTo(etd);
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }
                
                // compare eta
                comp = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, notInRdInfo.getEta()).compareTo(eta);
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }
                
                // compare imp inbound date
                comp = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, notInRdInfo.getImpInbDate()).compareTo(
                    inbPlanDate);
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }
                
                // compare impPoNo
                comp = StringUtil.toSafeString(notInRdInfo.getImpPoNo()).compareTo(impPoNo);
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }
                
                // compare invoiceNo
                comp = StringUtil.toSafeString(notInRdInfo.getInvoiceNo()).compareTo(invoiceNo);
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }
                
                // set qty
                qty = notInRdInfo.getQty();
                break;
            }
            // not in run-down qty
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundMaster.getDigit()));
            // if exist and not zero, set into file
            if (qty != null && qty.compareTo(BigDecimal.ZERO) > IntDef.INT_ZERO) {
                // set value
                PoiUtil.setCellValue(cntSheet, row, col++, qty);
            }
        }

        return idx;
    }

    /**
     * prepare customer share information by each run down master.
     *
     * @param cntSheet current sheet
     * @param rundMaster run-down master information
     * @param styleMap style map
     * @param lang Language
     * @param headMap head map
     * @param row row
     * @param colList column list
     * @param strIdx start index
     * @return end index
     */
    private static int setDetailInfoByEachRun(Sheet cntSheet, TntRundownMasterEx rundMaster,
        Map<String, CellStyle> styleMap, Language lang, Map<Integer, String> headMap, int row, List<Integer> colList,
        int strIdx) {

        // set start index
        int idx = strIdx;
        int dIdx = IntDef.INT_ZERO;
        int aIdx = IntDef.INT_ZERO;
        int lastCol = colList.get(strIdx).intValue();

        // get forecast str
        String noCustForecast = MessageManager.getMessage(NO_CUSTOMER_FORECAST, lang.getName());
        String noOrderForecast = MessageManager.getMessage(NO_ORDER_FORECAST, lang.getName());
        String noForecastAll = MessageManager.getMessage(NO_FORECAST_ALL, lang.getName());

        // get detail information
        List<TntRundownDetailEx> detailList = rundMaster.getRundownDetailList();
        // get rundown detail attached information
        List<TntRdDetailAttachEx> rdAttachList = rundMaster.getRdDetailAttachList();
        // start
        StringBuffer formula = new StringBuffer();
        // customer stock
        if (rundMaster.getCustStockQty() != null) {
            formula.append(CellReference.convertNumToColString(lastCol - IntDef.INT_SEVEN));
            formula.append(row).append(StringConst.PLUS);
        } else {
            formula.append(IntDef.INT_ZERO).append(StringConst.PLUS);
        }
        // Total Import Stock Qty
        formula.append(CellReference.convertNumToColString(lastCol - IntDef.INT_FIVE)).append(row);

        // loop
        for (; idx < colList.size(); idx++) {
            // prepare key
            int col = colList.get(idx).intValue();
            // String values
            String[] splitValues = headMap.get(col).split(StringConst.NEW_COMMA);

            // if belong to run down detail attached information
            if (HEAD_RUNDOWN_DETAIL_ATTACH.equals(splitValues[IntDef.INT_ZERO])) {
                // get qty
                TntRdDetailAttachEx actAttached = null;
                // get current date
                Date inbPlanDate = DateTimeUtil.parseDate(splitValues[IntDef.INT_ONE], DateTimeUtil.FORMAT_YYYYMMDD);
                Date etd = DateTimeUtil.parseDate(splitValues[IntDef.INT_TWO], DateTimeUtil.FORMAT_YYYYMMDD);
                Date eta = DateTimeUtil.parseDate(splitValues[IntDef.INT_THREE], DateTimeUtil.FORMAT_YYYYMMDD);
                String invoiceNo = splitValues[IntDef.INT_FOUR];
                String impPoNo = splitValues[IntDef.INT_FIVE];
                String orderMonth = splitValues[IntDef.INT_SIX];
                String pfcMonth = splitValues[IntDef.INT_SEVEN];
                String attachType = splitValues[IntDef.INT_EIGHT];

                // foreach data
                for (; aIdx < rdAttachList.size(); aIdx++) {
                    // get information
                    TntRdDetailAttachEx rdAttached = rdAttachList.get(aIdx);

                    // compare impPlanDate
                    int comp = rdAttached.getImpInbPlanDate().compareTo(inbPlanDate);
                    if (comp < IntDef.INT_ZERO) {
                        continue;
                    } else if (comp > IntDef.INT_ZERO) {
                        break;
                    }
                    // compare etd
                    comp = rdAttached.getEtd().compareTo(etd);
                    if (comp < IntDef.INT_ZERO) {
                        continue;
                    } else if (comp > IntDef.INT_ZERO) {
                        break;
                    }
                    // compare eta
                    if (rdAttached.getEta() == null) {
                        if (eta == null) {
                            comp = IntDef.INT_ZERO;
                        } else {
                            comp = IntDef.INT_ONE;
                        }
                    } else {
                        if (eta == null) {
                            comp = IntDef.INT_N_ONE;
                        } else {
                            comp = rdAttached.getEta().compareTo(eta);
                        }
                    }
                    if (comp < IntDef.INT_ZERO) {
                        continue;
                    } else if (comp > IntDef.INT_ZERO) {
                        break;
                    }
                    // compare attachType
                    comp = StringUtil.toSafeString(rdAttached.getAttachType()).compareTo(attachType);
                    if (comp > IntDef.INT_ZERO) {
                        continue;
                    } else if (comp < IntDef.INT_ZERO) {
                        break;
                    }
                    // compare orderMonth
                    comp = StringUtil.toSafeString(rdAttached.getOrderMonth()).compareTo(orderMonth);
                    if (comp < IntDef.INT_ZERO) {
                        continue;
                    } else if (comp > IntDef.INT_ZERO) {
                        break;
                    }
                    // compare pfcMonth
                    comp = StringUtil.toSafeString(rdAttached.getPfcMonth()).compareTo(pfcMonth);
                    if (comp < IntDef.INT_ZERO) {
                        continue;
                    } else if (comp > IntDef.INT_ZERO) {
                        break;
                    }
                    // compare ImpPoNo
                    comp = StringUtil.toSafeString(rdAttached.getImpPoNo()).compareTo(impPoNo);
                    if (comp < IntDef.INT_ZERO) {
                        continue;
                    } else if (comp > IntDef.INT_ZERO) {
                        break;
                    }
                    // compare InvoiceNo
                    comp = StringUtil.toSafeString(rdAttached.getInvoiceNo()).compareTo(invoiceNo);
                    if (comp < IntDef.INT_ZERO) {
                        continue;
                    } else if (comp > IntDef.INT_ZERO) {
                        break;
                    }
                    // set qty
                    actAttached = rdAttached;
                    break;
                }

                // check is attached information exist
                if (actAttached == null) {
                    // if already after run down range, set as gray
                    if (inbPlanDate.after(rundMaster.getRunEndDate())) {
                        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_GRAY15));
                    } else {
                        // check no cfc
                        boolean noPfcFlag = rundMaster.getEndPfcDate() == null ? inbPlanDate.after(rundMaster
                            .getEndTargetDate()) : inbPlanDate.after(rundMaster.getEndPfcDate());
                        if (noPfcFlag) {
                            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_BOLD));
                            PoiUtil.setCellValue(cntSheet, row, col, noOrderForecast);
                        } else {
                            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE));
                        }
                    }
                } else {
                    // set into database
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_WHITE + rundMaster.getDigit()));
                    PoiUtil.setCellValue(cntSheet, row, col, actAttached.getQty());
                }
            } else if (HEAD_RUNDOWN_DETAIL.equals(splitValues[IntDef.INT_ZERO])) {
                // get qty
                TntRundownDetailEx activeInfo = null;
                // foreach data
                for (; dIdx < detailList.size(); dIdx++) {
                    // get information
                    TntRundownDetailEx detailInfo = detailList.get(dIdx);
                    String impPlanDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD,
                        detailInfo.getImpInbPlanDate());
                    // compare
                    int comp = impPlanDate.compareTo(splitValues[IntDef.INT_ONE]);
                    if (comp < IntDef.INT_ZERO) {
                        continue;
                    } else if (comp > IntDef.INT_ZERO) {
                        break;
                    }
                    // set
                    activeInfo = detailInfo;
                    break;
                }

                // if already not exist
                if (activeInfo == null) {
                    // Stock Qty
                    PoiUtil.setCellStyle(cntSheet, row, col++, styleMap.get(LABEL_GRAY15));
                    // Stock days
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_GRAY15));
                } else {
                    // is working?
                    boolean isWorking = activeInfo.getWorkingFlag().equals(WorkingDay.REST_DAY) ? false : true;
                    String prefix = (isWorking ? DECIMAL_WHITE : DECIMAL_BLACK);
                    String prefixBold = (isWorking ? LABEL_WHITE_BOLD : LABEL_BLACK_BOLD);

                    // if ending stock is not null
                    if (activeInfo.getEndingStock() != null) {
                        // Stock Qty
                        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(prefix + rundMaster.getDigit()));
                        // get formilation
                        if (formula.length() == IntDef.INT_ZERO) {
                            formula.append(CellReference.convertNumToColString(lastCol - IntDef.INT_ONE)).append(row);
                        }
                        // if has attached information
                        if (lastCol + IntDef.INT_TWO < col) {
                            formula.append(StringConst.PLUS).append("SUM(");
                            formula.append(CellReference.convertNumToColString(lastCol + IntDef.INT_ONE));
                            formula.append(row).append(StringConst.COLON);
                            formula.append(CellReference.convertNumToColString(col - IntDef.INT_TWO));
                            formula.append(row).append(StringConst.RIGHT_BRACKET);
                        }
                        // customer usage
                        formula.append(StringConst.MIDDLE_LINE);
                        BigDecimal daliyUsageQty = activeInfo.getDaliyUsageQty();
                        formula.append(daliyUsageQty == null ? BigDecimal.ZERO : daliyUsageQty);
                        PoiUtil.setCellFormula(cntSheet, row, col++, formula.toString());
                    } else {
                        // no forecast
                        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(prefixBold));
                        // check imp inbound plan date
                        Date impDate = activeInfo.getImpInbPlanDate();
                        boolean noPfcFlag = activeInfo.getNoPfcFlag().equals(NoPfcFlag.Y);
                        boolean noCfcFlag = (rundMaster.getEndCfcDate() == null || impDate.after(rundMaster
                            .getEndCfcDate()));
                        if (noCfcFlag && noPfcFlag) {
                            PoiUtil.setCellValue(cntSheet, row, col++, noForecastAll);
                        } else if (noCfcFlag) {
                            PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                        } else {
                            PoiUtil.setCellValue(cntSheet, row, col++, noOrderForecast);
                        }
                    }
                    // if Stock days is not null
                    if (activeInfo.getStockDays() != null) {
                        // contral by box
                        if (rundMaster.getInventoryBoxFlag().equals(InventoryByBox.Y)) {
                            // Stock days
                            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(prefix + IntDef.INT_ONE));
                            PoiUtil.setCellValue(cntSheet, row, col, activeInfo.getStockDays());
                        } else {
                            // if min stock alarm
                            if (DecimalUtil.isLess(activeInfo.getStockDays(), BigDecimal.ZERO)) {
                                // shortage
                                PoiUtil.setCellStyle(cntSheet, row, col,
                                    styleMap.get(DECIMAL_RED_BOLD + IntDef.INT_ONE));
                            } else if (DecimalUtil.isLess(activeInfo.getStockDays(),
                                StringUtil.toBigDecimal(rundMaster.getMinStock()))) {
                                // set pink
                                PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(DECIMAL_PINK + IntDef.INT_ONE));
                            } else {
                                PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(prefix + IntDef.INT_ONE));
                            }
                            // set values
                            PoiUtil.setCellValue(cntSheet, row, col, activeInfo.getStockDays());
                        }
                    } else {
                        // if all no forecast
                        PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(prefixBold));
                        // set value
                        if (activeInfo.getNoCfcFlag().equals(NoCfcFlag.Y)
                                && activeInfo.getNoPfcFlag().equals(NoPfcFlag.Y)) {
                            PoiUtil.setCellValue(cntSheet, row, col, noForecastAll);
                        } else if (activeInfo.getNoCfcFlag().equals(NoCfcFlag.Y)) {
                            PoiUtil.setCellValue(cntSheet, row, col, noCustForecast);
                        } else {
                            PoiUtil.setCellValue(cntSheet, row, col, noOrderForecast);
                        }
                    }
                }
                // reset
                lastCol = col - IntDef.INT_ONE;
                formula.setLength(IntDef.INT_ZERO);
            }
        }
        // return
        return idx;
    }
}