/**
 * @screen Run-down file manager
 * @author liu_yinchuan
 */
package com.chinaplus.common.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.chinaplus.common.bean.TntNotInRundownEx;
import com.chinaplus.common.bean.TntRdDetailAttachEx;
import com.chinaplus.common.bean.TntRundownDetailEx;
import com.chinaplus.common.bean.TntRundownMasterEx;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.AttachType;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.CustStockFlag;
import com.chinaplus.common.consts.CodeConst.InventoryByBox;
import com.chinaplus.common.consts.CodeConst.SimulationType;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.PoiUtil;

/**
 * Run-down file manager.
 * 
 * @author liu_yinchuan
 */
public class RunDownOnePartsManager {

    /** sheet name prefix of single parts template */
    private final static String SHEET_NAME_PREFIX = "Rundown";

    /** sheet name */
    private final static String SHEET_NAME_STYLE = "styleSheet";

    /** template date white */
    private final static String DATE_WHITE_PREFIX = "Date_White_";

    /** template date gray */
    private final static String DATE_GRAY_PREFIX = "Date_Gray_";

    /** template Month */
    private final static String MONTH_PREFIX = "Month_";

    /** template label gray */
    private final static String LABEL_GRAY = "Label_Gray";

    /** template label gray */
    private final static String LABEL_GRAY_FONT_RED = "Label_Gray_font_red";

    /** template label gray */
    private final static String LABEL_WHITE = "Label_White";

    /** template label gray */
    private final static String LABEL_WHITE_FONT_RED = "Label_White_font_red";

    /** template label gray */
    private final static String LABEL_WHITE_WO_LINE = "Label_White_Without_Line";

    /** template label gray */
    private final static String LAST_ORION_DATETIME = "LAST_ORION_DATETIME_style";

    /** template label font red */
    private final static String LABEL_FONT_RED = "Label_Font_Red";

    /** template decimal gray */
    private final static String DECIMAL_GRAY = "Decimal_Gray_";

    /** template decimal white */
    private final static String DECIMAL_WHITE = "Decimal_White_";

    /** template decimal yellow */
    private final static String DECIMAL_YELLOW = "Decimal_Yellow_";

    /** template decimal blue */
    private final static String DECIMAL_BLUE = "Decimal_Blue_";

    /** template decimal pink */
    private final static String DECIMAL_PINK = "Decimal_Pink_";

    /** template decimal red font */
    private final static String DECIMAL_RED = "Decimal_Red_";

    /** template not in header */
    private final static String NOT_IN_RD_HEADER = "Not_In_Rundown_Header";

    /** CPSSSF01_Label_NoOrderCustomerForecast */
    private final static String DOWNLOAD_TIME = "CPSRDF02_Label_DownloadTime";

    /** CPSSSF01_Label_NoOrderCustomerForecast */
    private final static String SYNC_SSMS_DATE_TIME = "CPSRDF02_Label_LastSSMSInterfaceTime";

    /** CPSSSF01_Label_NoOrderCustomerForecast */
    private final static String SYNC_VV_DATE_TIME = "CPSRDF02_Label_LastVVInterfaceTime";

    /** CPSSSF01_Label_NoOrderCustomerForecast */
    private final static String SYNC_AISIN_DATE_TIME = "CPSRDF02_Label_LastASInterfaceTime";

    /** MIN_BOX_NUMBER */
    private final static String MIN_BOX_NUMBER = "MIN_BOX_NUMBER";

    /** NOT_IN_RUNDOWN_NG */
    private final static String NOT_IN_RUNDOWN_NG = "NOT_IN_RUNDOWN_NG";

    /** NOT_IN_YETINBOUND */
    private final static String NOT_IN_YETINBOUND = "NOT_IN_RUNDOWN_YET_INBOUND";

    /** NO_CUSTOMER_STOCK */
    private final static String NO_CUSTOMER_STOCK = "NO_CUSTOMER_STOCK";

    /** NO_ORDER_FORECAST */
    private final static String NO_ORDER_FORECAST = "NO_ORDER_FORECAST";

    /** NO_CUSTOMER_FORECAST */
    private final static String NO_CUSTOMER_FORECAST = "NO_CUSTOMER_FORECAST";

    /** NO_OREDER_AND_CUSTOMER_FORECAST */
    private final static String NO_FORECAST_ALL = "NO_OREDER_AND_CUSTOMER_FORECAST";

    /** _TITLE */
    private final static String NOT_TITLE = "_TITLE";

    /** CONDITION */
    private final static String CONDITION = "_CONDITION";

    /** title length of NG on-hold */
    private final static Integer TITLE_LEN_NG = IntDef.INT_SEVEN;

    /** title length of YET In-bound */
    private final static Integer TITLE_LEN_YETINBOUND = IntDef.INT_FIVE;

    /** title length of YET In-bound */
    private final static Integer NOT_IN_START_ROW = IntDef.INT_THIRTY_SIX;

    /** title length of YET In-bound */
    private final static Integer NOT_IN_STEP = IntDef.INT_ONE;

    /** rundown start column for single parts */
    private final static Integer RD_START_COL_FOR_SINGLE = IntDef.INT_TEN;

    /** rundown start row for single parts */
    private final static Integer RD_START_ROW_FOR_SINGLE = IntDef.INT_FIVE;

    /**
     * Make Run Down
     *
     * @param workbook wbOutput
     * @param rdMasterInfo run down master information\
     * @param lang language
     * @param dateList date list
     */
    public static void makeRDSinglePart(XSSFWorkbook workbook, TntRundownMasterEx rdMasterInfo, Language lang,
        Date[] dateList) {

        // defined
        Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();
        Map<String, String> valuesMap = new HashMap<String, String>();

        // prepare cell map
        prepStyleMapForSingle(workbook, styleMap);

        // prepare value map
        prepValueMapForSingle(workbook, valuesMap);

        // get first sheet
        Sheet cntSheet = null;
        if (lang.equals(Language.CHINESE)) {
            cntSheet = workbook.getSheetAt(IntDef.INT_ONE);
        } else {
            cntSheet = workbook.getSheetAt(IntDef.INT_ZERO);
        }

        // set header string
        setHeaderSinglePart(workbook, cntSheet, rdMasterInfo, lang, styleMap, valuesMap);

        // set run-down detail
        setRundownDetailInfo(cntSheet, rdMasterInfo, styleMap, valuesMap, lang);

        // set Yet ETD
        int lastRow = setYetNotETDTable(cntSheet, rdMasterInfo, styleMap, lang);

        // set Yet Inbound
        lastRow = setYetNotInboundTable(cntSheet, rdMasterInfo, styleMap, valuesMap, lastRow, lang);

        // set Imp W/H On Hold
        lastRow = setNgOnHoldTable(cntSheet, rdMasterInfo, styleMap, valuesMap, lastRow, lang);

        // set download time
        lastRow = lastRow + NOT_IN_STEP;
        String targetFormat = lang.equals(Language.CHINESE) ? DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMMSS
            : DateTimeUtil.FORMAT_DDMMMYYYYHHMMSS;
        String dateStr = DateTimeUtil.formatDate(targetFormat, dateList[IntDef.INT_ZERO]);
        String timeStr = MessageManager.getMessage(DOWNLOAD_TIME, lang.getName());
        timeStr = MessageFormat.format(timeStr, new Object[] { dateStr, rdMasterInfo.getOfficeCode() });
        PoiUtil.setCellStyle(cntSheet, lastRow, IntDef.INT_ONE, styleMap.get(LAST_ORION_DATETIME));
        PoiUtil.setCellValue(cntSheet, lastRow++, IntDef.INT_ONE, timeStr);
        // set last office data time of SSMS
        if (dateList[IntDef.INT_ONE] != null) {
            dateStr = DateTimeUtil.formatDate(targetFormat, dateList[IntDef.INT_ONE]);
            timeStr = MessageManager.getMessage(SYNC_SSMS_DATE_TIME, lang.getName());
            timeStr = MessageFormat.format(timeStr, new Object[] { dateStr });
            PoiUtil.setCellStyle(cntSheet, lastRow, IntDef.INT_ONE, styleMap.get(LAST_ORION_DATETIME));
            PoiUtil.setCellValue(cntSheet, lastRow++, IntDef.INT_ONE, timeStr);
        }
        // set last office data time of V-V
        if (dateList[IntDef.INT_TWO] != null) {
            dateStr = DateTimeUtil.formatDate(targetFormat, dateList[IntDef.INT_TWO]);
            timeStr = MessageManager.getMessage(SYNC_VV_DATE_TIME, lang.getName());
            timeStr = MessageFormat.format(timeStr, new Object[] { rdMasterInfo.getOfficeCode(), dateStr });
            PoiUtil.setCellStyle(cntSheet, lastRow, IntDef.INT_ONE, styleMap.get(LAST_ORION_DATETIME));
            PoiUtil.setCellValue(cntSheet, lastRow++, IntDef.INT_ONE, timeStr);
        }
        // set last office data time of AISIN
        if (dateList[IntDef.INT_THREE] != null) {
            dateStr = DateTimeUtil.formatDate(targetFormat, dateList[IntDef.INT_THREE]);
            timeStr = MessageManager.getMessage(SYNC_AISIN_DATE_TIME, lang.getName());
            timeStr = MessageFormat.format(timeStr, new Object[] { rdMasterInfo.getOfficeCode(), dateStr });
            PoiUtil.setCellStyle(cntSheet, lastRow, IntDef.INT_ONE, styleMap.get(LAST_ORION_DATETIME));
            PoiUtil.setCellValue(cntSheet, lastRow, IntDef.INT_ONE, timeStr);
        }

        // rename sheet
        // remove style sheet
        workbook.removeSheetAt(IntDef.INT_TWO);
        // remove sheet
        if (lang.equals(Language.CHINESE)) {
            // remove English
            workbook.removeSheetAt(IntDef.INT_ZERO);
        } else {
            // remove Chinese
            workbook.removeSheetAt(IntDef.INT_ONE);
        }
        // rename
        workbook.setSheetName(IntDef.INT_ZERO, SHEET_NAME_PREFIX);
        workbook.setForceFormulaRecalculation(true);
    }

    /**
     * Prepare style map for single rundown file.
     *
     * @param workbook wbTemplate
     * @param styleMap style map
     */
    private static void prepStyleMapForSingle(Workbook workbook, Map<String, CellStyle> styleMap) {

        // get style Sheet
        Sheet styleSheet = workbook.getSheet(SHEET_NAME_STYLE);

        // prepare label
        styleMap.put(LABEL_GRAY, PoiUtil.getCell(styleSheet, IntDef.INT_TWO, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_WHITE, PoiUtil.getCell(styleSheet, IntDef.INT_THREE, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_GRAY_FONT_RED, PoiUtil.getCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_WHITE_FONT_RED, PoiUtil.getCell(styleSheet, IntDef.INT_FOURTEEN, IntDef.INT_ONE)
            .getCellStyle());
        styleMap.put(LAST_ORION_DATETIME, PoiUtil.getCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_FONT_RED, PoiUtil.getCell(styleSheet, IntDef.INT_SIX, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_WHITE_WO_LINE, PoiUtil.getCell(styleSheet, IntDef.INT_EIGHT, IntDef.INT_ONE).getCellStyle());
        styleMap.put(NOT_IN_RD_HEADER, PoiUtil.getCell(styleSheet, IntDef.INT_FIFTEEN, IntDef.INT_ONE).getCellStyle());

        // prepare Date
        styleMap.put(DATE_GRAY_PREFIX.concat(Language.CHINESE.getName()),
            PoiUtil.getCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWO).getCellStyle());
        styleMap.put(DATE_GRAY_PREFIX.concat(Language.ENGLISH.getName()),
            PoiUtil.getCell(styleSheet, IntDef.INT_THREE, IntDef.INT_TWO).getCellStyle());
        styleMap.put(DATE_WHITE_PREFIX.concat(Language.CHINESE.getName()),
            PoiUtil.getCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWO).getCellStyle());
        styleMap.put(DATE_WHITE_PREFIX.concat(Language.ENGLISH.getName()),
            PoiUtil.getCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TWO).getCellStyle());
        styleMap.put(MONTH_PREFIX.concat(Language.CHINESE.getName()),
            PoiUtil.getCell(styleSheet, IntDef.INT_SIX, IntDef.INT_TWO).getCellStyle());
        styleMap.put(MONTH_PREFIX.concat(Language.ENGLISH.getName()),
            PoiUtil.getCell(styleSheet, IntDef.INT_SEVEN, IntDef.INT_TWO).getCellStyle());

        // prepare Decimal
        StringBuffer sb = new StringBuffer();
        for (int i = IntDef.INT_ZERO; i < IntDef.INT_SEVEN; i++) {

            // get current row
            int row = IntDef.INT_TWO + i;

            // gray
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_GRAY).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_THREE).getCellStyle());

            // yellow
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_YELLOW).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_FOUR).getCellStyle());

            // blue
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_BLUE).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_FIVE).getCellStyle());

            // write
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_WHITE).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_SIX).getCellStyle());

            // red
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_RED).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_SEVEN).getCellStyle());

            // red
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_PINK).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_EIGHT).getCellStyle());
        }
    }

    /**
     * Prepare values map for single rundown file.
     *
     * @param workbook wbTemplate
     * @param valueMap values map
     */
    private static void prepValueMapForSingle(Workbook workbook, Map<String, String> valueMap) {

        // get style Sheet
        Sheet styleSheet = workbook.getSheet(SHEET_NAME_STYLE);

        // get value map
        // MinBoxNum
        valueMap.put(MIN_BOX_NUMBER, PoiUtil.getStringCellValue(styleSheet, IntDef.INT_TWO, IntDef.INT_ONE));
        // Last orion data time
        valueMap.put(LAST_ORION_DATETIME, PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FIVE, IntDef.INT_ONE));
        // not in run down for ng on-hold
        valueMap.put(NOT_IN_RUNDOWN_NG, PoiUtil.getStringCellValue(styleSheet, IntDef.INT_SIX, IntDef.INT_ONE));
        // not in run down for yet not inbound
        valueMap.put(NOT_IN_YETINBOUND, PoiUtil.getStringCellValue(styleSheet, IntDef.INT_SEVEN, IntDef.INT_ONE));
        // not in run down for yet not inbound condition
        valueMap.put(NOT_IN_YETINBOUND.concat(CONDITION),
            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_EIGHT, IntDef.INT_ONE));
        // not in run down for ng on-hold condition
        valueMap.put(NOT_IN_RUNDOWN_NG.concat(CONDITION),
            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_NINE, IntDef.INT_ONE));
        // no customer stock
        valueMap.put(NO_CUSTOMER_STOCK, PoiUtil.getStringCellValue(styleSheet, IntDef.INT_TEN, IntDef.INT_ONE));
        // no order forecast
        valueMap.put(NO_ORDER_FORECAST, PoiUtil.getStringCellValue(styleSheet, IntDef.INT_ELEVEN, IntDef.INT_ONE));
        // no customer forecast
        valueMap.put(NO_CUSTOMER_FORECAST, PoiUtil.getStringCellValue(styleSheet, IntDef.INT_TWELVE, IntDef.INT_ONE));
        // no order & customer forecast
        valueMap.put(NO_FORECAST_ALL, PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THIRTEEN, IntDef.INT_ONE));

        // prepare yet inbound title
        StringBuffer key = new StringBuffer();
        for (int i = IntDef.INT_ONE; i <= TITLE_LEN_YETINBOUND; i++) {

            // get key
            key.setLength(IntDef.INT_ZERO);
            key.append(NOT_IN_YETINBOUND).append(NOT_TITLE).append(i);

            // set into map
            valueMap.put(key.toString(), PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FIFTEEN, i));
        }

        // prepare Ng on-hold title
        for (int i = IntDef.INT_ONE; i <= TITLE_LEN_NG; i++) {

            // get key
            key.setLength(IntDef.INT_ZERO);
            key.append(NOT_IN_RUNDOWN_NG).append(NOT_TITLE).append(i);

            // set into map
            valueMap.put(key.toString(), PoiUtil.getStringCellValue(styleSheet, IntDef.INT_TWENTY, i));
        }
    }

    /**
     * prepare header content of single parts.
     *
     * @param workbook workbook
     * @param cntSheet current Sheet
     * @param rdMasterInfo run-down master information
     * @param lang language
     * @param styleMap cell style map
     * @param valuesMap title values Maps
     */
    private static void setHeaderSinglePart(XSSFWorkbook workbook, Sheet cntSheet, TntRundownMasterEx rdMasterInfo,
        Language lang, Map<String, CellStyle> styleMap, Map<String, String> valuesMap) {

        // prepare header
        int startRow = RD_START_ROW_FOR_SINGLE + IntDef.INT_SEVEN;

        // TTC Part No.
        PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, rdMasterInfo.getTtcPartsNo());
        // Parts Name
        String partName = lang.equals(Language.ENGLISH) ? rdMasterInfo.getPartNameEn() : rdMasterInfo.getPartNameCn();
        PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, partName);
        // Back No.
        PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, rdMasterInfo.getCustBackNo());
        // Exp Country
        PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, rdMasterInfo.getExpRegion());
        // Supplier Code
        PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, rdMasterInfo.getTtcSupplierCode());
        // Supplier Parts No.
        PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, rdMasterInfo.getSupplierPartsNo());
        // Imp Country
        PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, rdMasterInfo.getImpRegion());
        // Customer Code
        PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, rdMasterInfo.getCustomerCode());
        // Customer Parts No.
        PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, rdMasterInfo.getCustPartsNo());
        // Business Type
        String busType = CodeCategoryManager.getCodeName(lang.getCode(), CodeMasterCategory.BUSINESS_TYPE,
            rdMasterInfo.getBusinessType());
        PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, busType);
        // Min Stock Days in Master
        if (rdMasterInfo.getInventoryBoxFlag().equals(InventoryByBox.N)) {
            // set min stock day
            PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, rdMasterInfo.getMinStock());
        } else {
            // reset title
            PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_ONE, valuesMap.get(MIN_BOX_NUMBER));
            // set min box
            PoiUtil.setCellValue(cntSheet, startRow++, IntDef.INT_TWO, rdMasterInfo.getMinBox());
        }
        // Part Remark(In Master)
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_TWO, rdMasterInfo.getPartsRemark());
        // Part Remark(In Rundown)
        startRow += IntDef.INT_THREE;
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_TWO, rdMasterInfo.getRemark());

        // reset row
        startRow = RD_START_ROW_FOR_SINGLE + IntDef.INT_SEVEN;

        // get decimal digit
        String decimalKey = new StringBuffer(DECIMAL_GRAY).append(rdMasterInfo.getDigit()).toString();
        CellStyle decimalStyle = workbook.createCellStyle();
        decimalStyle.cloneStyleFrom(styleMap.get(decimalKey));
        decimalStyle.setAlignment(CellStyle.ALIGN_CENTER);
        // get decimal digit
        String decimalRedKey = new StringBuffer(DECIMAL_RED).append(rdMasterInfo.getDigit()).toString();
        CellStyle decimalRedStyle = workbook.createCellStyle();
        decimalRedStyle.cloneStyleFrom(styleMap.get(decimalRedKey));
        decimalRedStyle.setAlignment(CellStyle.ALIGN_CENTER);
        // get decimal digit
        String decimalWhiteKey = new StringBuffer(DECIMAL_WHITE).append(rdMasterInfo.getDigit()).toString();
        CellStyle decimalWhiteStyle = workbook.createCellStyle();
        decimalWhiteStyle.cloneStyleFrom(styleMap.get(decimalWhiteKey));
        decimalWhiteStyle.setAlignment(CellStyle.ALIGN_CENTER);
        // get sting of end column
        int lastCol = prepareRundownEndColumn(rdMasterInfo);
        String strEndCol = CellReference.convertNumToColString(lastCol - IntDef.INT_ONE);

        // Order Forecast
        // total
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_SEVEN, decimalStyle);
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SEVEN, rdMasterInfo.getPfcQty());
        // in run-down Qty
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_EIGHT, decimalStyle);
        PoiUtil.setCellFormula(cntSheet, startRow, IntDef.INT_EIGHT, "SUM(J12:" + strEndCol + "12)");

        // Yet ETD
        // Yet ETD in not run-down
        startRow++;
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_NINE, decimalRedStyle);
        // Exp Balance Order
        startRow++;
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_SEVEN, decimalStyle);
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SEVEN, rdMasterInfo.getExpBalanceOrder());
        // Yet ETD in run-down
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_EIGHT, decimalWhiteStyle);
        PoiUtil.setCellFormula(cntSheet, startRow, IntDef.INT_EIGHT, "SUM(J14:" + strEndCol + "15)");
        // Exp W/H Stock
        startRow++;
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_SEVEN, decimalStyle);
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SEVEN, rdMasterInfo.getExpWhsStock());

        // On-Shipping Stock
        // total
        startRow++;
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_SEVEN, decimalStyle);
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SEVEN, rdMasterInfo.getOnShippingStock());
        // In Rundown
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_EIGHT, decimalWhiteStyle);
        PoiUtil.setCellFormula(cntSheet, startRow, IntDef.INT_EIGHT, "SUM(J19:" + strEndCol + "19)");
        // not run-down
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_NINE, decimalRedStyle);

        // Imp On Hold
        // total
        startRow += IntDef.INT_FOUR;
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_SEVEN, decimalStyle);
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SEVEN, rdMasterInfo.getNgQty());
        // In Rundown
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_EIGHT, decimalWhiteStyle);
        PoiUtil.setCellFormula(cntSheet, startRow, IntDef.INT_EIGHT, "SUM(J20:" + strEndCol + "20)");
        // not run-down
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_NINE, decimalRedStyle);

        // ECI on Hold
        startRow++;
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_SEVEN, decimalStyle);
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SEVEN, rdMasterInfo.getEciOnholdQty());
        // Imp W/H Available Qty (Exclude ECI on hold)
        startRow++;
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_SEVEN, decimalStyle);
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SEVEN,
            DecimalUtil.add(rdMasterInfo.getInRackQty(), rdMasterInfo.getImpWhsQty()));
        // Already Prepared for Imp Outbound
        startRow++;
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_SEVEN, decimalStyle);
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SEVEN, rdMasterInfo.getPreparedObQty());

        // Total Import Stock Qty
        startRow++;
        PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_SEVEN, decimalWhiteStyle);

        // set customer stock
        startRow++;
        if (rdMasterInfo.getSaCustStockFlag().equals(CustStockFlag.Y)) {
            // check is exist customer forecast or not
            if (rdMasterInfo.getCustStockEndDate() != null) {

                // Customer Stock(Date)
                PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_FIVE,
                    styleMap.get(DATE_GRAY_PREFIX.concat(lang.getName())));
                PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_FIVE, rdMasterInfo.getCustStockEndDate());

                // Customer Stock(Qty)
                PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_SEVEN, decimalWhiteStyle);
                PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SEVEN, rdMasterInfo.getCustStockQty());
            } else {

                // set no master
                PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_FIVE, styleMap.get(LABEL_GRAY_FONT_RED));
                PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_FIVE, valuesMap.get(NO_CUSTOMER_STOCK));

                // set no master
                PoiUtil.setCellStyle(cntSheet, startRow, IntDef.INT_SEVEN, styleMap.get(LABEL_WHITE_FONT_RED));
                PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SEVEN, valuesMap.get(NO_CUSTOMER_STOCK));
            }
        }
        
        // set formula
        startRow = RD_START_ROW_FOR_SINGLE + IntDef.INT_TWO;
        PoiUtil.setCellFormula(cntSheet, startRow, IntDef.INT_SEVEN, "MAX(J7:" + strEndCol + "7)");
        if (rdMasterInfo.getEndPfcDate() != null) {
            PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_EIGHT, rdMasterInfo.getEndPfcDate());
        } else {
            //Date endDate = DateTimeUtil.addDay(rdMasterInfo.getRunStartDate(), IntDef.INT_N_ONE);
            PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_EIGHT, rdMasterInfo.getEndTargetDate());
        }
        startRow++;
        PoiUtil.setCellFormula(cntSheet, startRow, IntDef.INT_SEVEN, "MIN(J8:" + strEndCol + "8)");
        startRow += IntDef.INT_TWO;
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SIX, lastCol);
        PoiUtil.setCellValue(cntSheet, startRow, IntDef.INT_SEVEN, rdMasterInfo.getInventoryBoxFlag());
    }

    /**
     * prepare information for rundown detail.
     *
     * @param cntSheet current sheet
     * @param rdMasterInfo run-down master information
     * @param styleMap cell style map
     * @param valuesMap values map
     * @param lang Language
     * 
     * @return last column
     */
    private static int setRundownDetailInfo(Sheet cntSheet, TntRundownMasterEx rdMasterInfo,
        Map<String, CellStyle> styleMap, Map<String, String> valuesMap, Language lang) {

        // start column
        int detailCol = RD_START_COL_FOR_SINGLE;

        // get format
        CellStyle labelStyle = styleMap.get(LABEL_WHITE);
        CellStyle grayDateStyle = styleMap.get(DATE_GRAY_PREFIX.concat(lang.getName()));
        CellStyle whiteDateStyle = styleMap.get(DATE_WHITE_PREFIX.concat(lang.getName()));
        // get decimal digit
        String decimaGraylKey = new StringBuffer(DECIMAL_GRAY).append(rdMasterInfo.getDigit()).toString();
        CellStyle decimalGrayStyle = styleMap.get(decimaGraylKey);
        // get decimal digit
        //String decimaBluelKey = new StringBuffer(DECIMAL_BLUE).append(rdMasterInfo.getDigit()).toString();
        //CellStyle decimalBlueStyle = styleMap.get(decimaBluelKey);
        // get decimal digit
        String decimaPinkKey = new StringBuffer(DECIMAL_PINK).append(rdMasterInfo.getDigit()).toString();
        CellStyle decimalPinkStyle = styleMap.get(decimaPinkKey);
        // get decimal digit
        String decimalYellowKey = new StringBuffer(DECIMAL_YELLOW).append(rdMasterInfo.getDigit()).toString();
        CellStyle decimalYellowStyle = styleMap.get(decimalYellowKey);
        // get decimal digit
        String decimalWhiteKey = new StringBuffer(DECIMAL_WHITE).append(rdMasterInfo.getDigit()).toString();
        CellStyle decimalWhiteStyle = styleMap.get(decimalWhiteKey);
        // get decimal digit(for working day)
        String decimalNonKey = new StringBuffer(DECIMAL_GRAY).append(IntDef.INT_ZERO).toString();
        CellStyle decimalNonStyle = styleMap.get(decimalNonKey);
        // get decimal digit(for stock day)
        String decimalOneKey = new StringBuffer(DECIMAL_GRAY).append(IntDef.INT_ONE).toString();
        CellStyle decimalOneStyle = styleMap.get(decimalOneKey);

        // get last column
        int lastCol = prepareRundownEndColumn(rdMasterInfo);
        String strEndCol = CellReference.convertNumToColString(lastCol - IntDef.INT_ONE);

        // loop
        int lastOrgCol = IntDef.INT_ZERO;
        Map<String, TntRdDetailAttachEx> etdEtaMap = new LinkedHashMap<String, TntRdDetailAttachEx>();
        for (TntRundownDetailEx runDetail : rdMasterInfo.getRundownDetailList()) {
            
            // get orginal column
            int orgCol = detailCol;
            // get string of original Column
            String colName = CellReference.convertNumToColString(orgCol - IntDef.INT_ONE);
            StringBuffer formula = new StringBuffer();

            // loop each attached information
            prepareEtdEtaMap(runDetail.getAttachedInfoList(), etdEtaMap, rdMasterInfo.getDigit());

            // prepare ETD/ETA
            if (!etdEtaMap.isEmpty()) {

                // loop
                Iterator<TntRdDetailAttachEx> iterator = etdEtaMap.values().iterator();
                while (iterator.hasNext()) {

                    // get detail information
                    TntRdDetailAttachEx item = iterator.next();

                    // start row
                    int row = RD_START_ROW_FOR_SINGLE;

                    // ETD
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, grayDateStyle);
                    PoiUtil.setCellValue(cntSheet, row, detailCol, item.getEtd());

                    // ETA
                    row++;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, grayDateStyle);
                    if (item.getEta() != null) {
                        PoiUtil.setCellValue(cntSheet, row, detailCol, item.getEta());
                    }

                    // Last cfc date
                    row++;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, grayDateStyle);
                    // =IF(ISBLANK(J12),$H$7,J11)'
                    formula.setLength(IntDef.INT_ZERO);
                    formula.append("IF(ISBLANK(").append(colName).append("12),$H$7,").append(colName).append("11)");
                    PoiUtil.setCellFormula(cntSheet, row, detailCol, formula.toString());

                    // Last pfc date
                    row++;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, grayDateStyle);
                    // =IF(ISNUMBER(J26),$DC$11 + 1,J11)
                    formula.setLength(IntDef.INT_ZERO);
                    formula.append("IF(ISNUMBER(").append(colName).append("26),").append(strEndCol).append("11+1,")
                        .append(colName).append("11)");
                    PoiUtil.setCellFormula(cntSheet, row, detailCol, formula.toString());

                    // Is Working Days
                    row++;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalNonStyle);
                    PoiUtil.setCellValue(cntSheet, row, detailCol, runDetail.getWorkingFlag().intValue());

                    // Is Actual Working
                    row++;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalNonStyle);
                    formula.setLength(IntDef.INT_ZERO);
                    formula.append("IF(AND(ISNUMBER(").append(colName).append("26),").append(colName);
                    formula.append("26> 0),1,").append(colName).append("9)");
                    PoiUtil.setCellFormula(cntSheet, row, detailCol, formula.toString());

                    // Order Forecast
                    // Qty
                    row += IntDef.INT_TWO;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalPinkStyle);
                    if (item.getOfcQty() != null) {
                        PoiUtil.setCellValue(cntSheet, row, detailCol, item.getOfcQty());
                    }

                    // Yet ETD
                    // PO No.
                    row++;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, labelStyle);
                    if (item.getImpPoNo() != null && !item.getAttachType().equals(AttachType.ON_SHIPPING)) {
                        PoiUtil.setCellValue(cntSheet, row, detailCol, item.getImpPoNo());
                    }
                    // Qty
                    row++;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalWhiteStyle);
                    PoiUtil.setCellStyle(cntSheet, row + IntDef.INT_ONE, detailCol, decimalWhiteStyle);
                    if (item.getOrderPlanQty() != null) {
                        PoiUtil.setCellValue(cntSheet, row, detailCol, item.getOrderPlanQty());
                    }
                    // merge
                    CellRangeAddress address = new CellRangeAddress(row - IntDef.INT_ONE, row, detailCol
                            - IntDef.INT_ONE, detailCol - IntDef.INT_ONE);
                    cntSheet.addMergedRegion(address);

                    // On-Shipping Stock
                    // Exp Invoice No.
                    row += IntDef.INT_TWO;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, labelStyle);
                    if (item.getInvoiceNo() != null) {
                        PoiUtil.setCellValue(cntSheet, row, detailCol, item.getInvoiceNo());
                    }
                    // B/L Date
                    row++;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, whiteDateStyle);
                    if (item.getBlDate() != null) {
                        PoiUtil.setCellValue(cntSheet, row, detailCol, item.getBlDate());
                    }
                    // Transport Mode
                    row++;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, labelStyle);
                    if (item.getTransportMode() != null) {
                        // set transport mode
                        String transportMode = CodeCategoryManager.getCodeName(lang.getCode(),
                            CodeMasterCategory.TRANSPORT_MODE, item.getTransportMode());
                        PoiUtil.setCellValue(cntSheet, row, detailCol, transportMode);
                    }
                    // Qty
                    row++;
                    PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalWhiteStyle);
                    if (item.getOnShippingQty() != null) {
                        PoiUtil.setCellValue(cntSheet, row, detailCol, item.getOnShippingQty());
                    }

                    // set blank cell
                    row++;
                    for (int i = IntDef.INT_ZERO; i < IntDef.INT_SIX; i++) {
                        // set as Qty
                        PoiUtil.setCellStyle(cntSheet, row + i, detailCol, decimalWhiteStyle);
                    }

                    // set style
                    if (detailCol > orgCol) {

                        // Import In-bound Date
                        row = RD_START_ROW_FOR_SINGLE + IntDef.INT_SIX;
                        PoiUtil.setCellStyle(cntSheet, row, detailCol, grayDateStyle);

                        // Daily Usage Qty
                        row += IntDef.INT_FIFTEEN;
                        PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalYellowStyle);

                        // End of Day Inventory
                        row++;
                        PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalGrayStyle);

                        // Days on Hand
                        row++;
                        PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalOneStyle);
                    }

                    // next column
                    detailCol++;
                }
            } else {

                // start row
                int row = RD_START_ROW_FOR_SINGLE;

                // ETD
                PoiUtil.setCellStyle(cntSheet, row, detailCol, grayDateStyle);

                // ETA
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, grayDateStyle);

                // Last cfc date
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, grayDateStyle);
                // =IF(ISBLANK(J12),$H$7,J11)'
                formula.setLength(IntDef.INT_ZERO);
                formula.append("IF(ISBLANK(").append(colName).append("12),$H$7,").append(colName).append("11)");
                PoiUtil.setCellFormula(cntSheet, row, detailCol, formula.toString());

                // Last pfc date
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, grayDateStyle);
                // =IF(ISNUMBER(J26),$DC$11 + 1,J11)
                formula.setLength(IntDef.INT_ZERO);
                formula.append("IF(ISNUMBER(").append(colName).append("26),").append(strEndCol).append("11+1,")
                    .append(colName).append("11)");
                PoiUtil.setCellFormula(cntSheet, row, detailCol, formula.toString());

                // Is Working Days
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalNonStyle);
                PoiUtil.setCellValue(cntSheet, row, detailCol, runDetail.getWorkingFlag().intValue());

                // Is Actual Working
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalNonStyle);
                formula.setLength(IntDef.INT_ZERO);
                formula.append("IF(AND(ISNUMBER(").append(colName).append("26),").append(colName);
                formula.append("26> 0),1,").append(colName).append("9)");
                PoiUtil.setCellFormula(cntSheet, row, detailCol, formula.toString());

                // Order Forecast
                // Qty
                row += IntDef.INT_TWO;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalPinkStyle);

                // Yet ETD
                // PO No.
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, labelStyle);
                // Qty
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalWhiteStyle);
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalWhiteStyle);
                // merge
                CellRangeAddress address = new CellRangeAddress(row - IntDef.INT_TWO, row - IntDef.INT_ONE, detailCol
                        - IntDef.INT_ONE, detailCol - IntDef.INT_ONE);
                cntSheet.addMergedRegion(address);

                // On-Shipping Stock
                // Exp Invoice No.
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, labelStyle);
                // B/L Date
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, whiteDateStyle);
                // Transport Mode
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, labelStyle);
                // Qty
                row++;
                PoiUtil.setCellStyle(cntSheet, row, detailCol, decimalWhiteStyle);

                // set blank cell
                row++;
                for (int i = IntDef.INT_ZERO; i < IntDef.INT_SIX; i++) {
                    // set as Qty
                    PoiUtil.setCellStyle(cntSheet, row + i, detailCol, decimalWhiteStyle);
                }

                // next column
                detailCol++;
            }

            // set common information
            int row = RD_START_ROW_FOR_SINGLE + IntDef.INT_SIX;
            
            // get last column
            String strMerEndCol = CellReference.convertNumToColString(detailCol - IntDef.INT_TWO);

            // Import In-bound Date
            PoiUtil.setCellStyle(cntSheet, row, orgCol, grayDateStyle);
            PoiUtil.setCellValue(cntSheet, row, orgCol, runDetail.getImpInbPlanDate());

            // Daily Usage Qty
            row += IntDef.INT_FIFTEEN;
            PoiUtil.setCellStyle(cntSheet, row, orgCol, decimalYellowStyle);
            if (rdMasterInfo.getEndCfcDate() == null
                    || runDetail.getImpInbPlanDate().after(rdMasterInfo.getEndCfcDate())) {
                // set no order forecast
                PoiUtil.setCellValue(cntSheet, row, orgCol, valuesMap.get(NO_CUSTOMER_FORECAST));
            } else {
                // set qty
                PoiUtil.setCellValue(cntSheet, row, orgCol, runDetail.getDaliyUsageQty());
            }

            // End of Day Inventory
            row++;
            PoiUtil.setCellStyle(cntSheet, row, orgCol, decimalGrayStyle);
            // first column or no No Order Forecast?
            // IF($G$8>L11,IF($G$7>=L11,SUM(K27,L14:L15,L19)-L26,"No Order Forecast"),IF($G$7>=L11,"No Customer Forecast","No Order & Customer Forecast"))
            // "SUM(J25,K12:K13,K17:K23)-K24"
            formula.setLength(IntDef.INT_ZERO);
            formula.append("IF($G$8>").append(colName).append("11,IF($G$7>=");
            formula.append(colName).append("11,").append("SUM(");
            if (orgCol == RD_START_COL_FOR_SINGLE) {
                formula.append("G24:G25,").append(colName);
            } else {
                String preColName = CellReference.convertNumToColString(lastOrgCol - IntDef.INT_ONE);
                formula.append(preColName).append("27,").append(colName);
            }
            formula.append("12:").append(strMerEndCol);
            formula.append("12,").append(colName);
            formula.append("14:").append(strMerEndCol).append("15,").append(colName);
            formula.append("19:").append(strMerEndCol);
            formula.append("19").append(")-").append(colName).append("26").append(",\"");
            formula.append(valuesMap.get(NO_ORDER_FORECAST)).append("\"),IF($G$7>=").append(colName);
            formula.append("11,\"").append(valuesMap.get(NO_CUSTOMER_FORECAST)).append("\",\"");
            formula.append(valuesMap.get(NO_FORECAST_ALL)).append("\"))");
            // set formula
            PoiUtil.setCellFormula(cntSheet, row, orgCol, formula.toString());

            // Days on Hand
            row++;
            PoiUtil.setCellStyle(cntSheet, row, orgCol, decimalOneStyle);

            // if more than on column, need do merge
            if (etdEtaMap.size() > IntDef.INT_ONE) {

                // reset
                row = RD_START_ROW_FOR_SINGLE + IntDef.INT_FIVE;

                // Import In-bound Date
                CellRangeAddress address = new CellRangeAddress(row, row, orgCol - IntDef.INT_ONE, detailCol
                        - IntDef.INT_TWO);
                cntSheet.addMergedRegion(address);

                // Daily Usage Qty
                row += IntDef.INT_FIFTEEN;
                address = new CellRangeAddress(row, row, orgCol - IntDef.INT_ONE, detailCol - IntDef.INT_TWO);
                cntSheet.addMergedRegion(address);

                // End of Day Inventory
                row++;
                address = new CellRangeAddress(row, row, orgCol - IntDef.INT_ONE, detailCol - IntDef.INT_TWO);
                cntSheet.addMergedRegion(address);

                // Days on Hand
                row++;
                address = new CellRangeAddress(row, row, orgCol - IntDef.INT_ONE, detailCol - IntDef.INT_TWO);
                cntSheet.addMergedRegion(address);
            }

            // clear map
            etdEtaMap.clear();
            
            // set as hidden column
            if (runDetail.getSimulationType().equals(SimulationType.NOT_REQUIRE)) {
                cntSheet.setColumnHidden(orgCol - IntDef.INT_ONE, true);
            }

            // reset last orgCol
            lastOrgCol = orgCol;
        }

        // return last column
        return detailCol - IntDef.INT_ONE;
    }
    
    /**
     * get last column of rundown.
     *
     * @param rdMasterInfo rundown MasterInfo
     * @return last column of rundown
     */
    private static int prepareRundownEndColumn(TntRundownMasterEx rdMasterInfo) {
        
        // detail size
        int column = rdMasterInfo.getRundownDetailList().size();

        // if has attached information, and attached size is more than one,
        for (TntRundownDetailEx runDetail : rdMasterInfo.getRundownDetailList()) {

            // check if has attached
            if (runDetail.getAttachedInfoList() != null && !runDetail.getAttachedInfoList().isEmpty()) {
                
                // define
                List<String> keySet = new ArrayList<String>();
                for (TntRdDetailAttachEx attachInfo : runDetail.getAttachedInfoList()) {
                    // prepare key
                    String etdkey = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, attachInfo.getEtd());
                    String etakey = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, attachInfo.getEta());
                    String mapKey = etdkey.concat(etakey);
                    // if exist
                    if (!keySet.contains(mapKey)) {
                        // check current attached type
                        keySet.add(mapKey);
                    }
                }
                
                
                // add column
                column = column + keySet.size() - IntDef.INT_ONE;
            }
        }
        
        // return column
        return column + RD_START_COL_FOR_SINGLE - IntDef.INT_ONE;
    }

    /**
     * Prepare run-down detail attached information map with key (ETD + ETA).
     *
     * @param runAttachedInfoList run-down detail attached information list
     * @param etdEtaMap ETD ETA map
     * @param digit digit
     */
    private static void prepareEtdEtaMap(List<TntRdDetailAttachEx> runAttachedInfoList,
        Map<String, TntRdDetailAttachEx> etdEtaMap, Integer digit) {

        // loop
        TntRdDetailAttachEx activeAttach = null;

        // if no attached info
        if (runAttachedInfoList == null || runAttachedInfoList.isEmpty()) {
            return;
        }

        // loop each attached information
        for (TntRdDetailAttachEx attachInfo : runAttachedInfoList) {

            // prepare key
            String etdkey = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, attachInfo.getEtd());
            String etakey = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, attachInfo.getEta());
            String mapKey = etdkey.concat(etakey);

            // if exist
            if (etdEtaMap.containsKey(mapKey)) {

                // check current attached type
                activeAttach = etdEtaMap.get(mapKey);
            } else {

                // set as current info
                activeAttach = attachInfo;
                etdEtaMap.put(mapKey, activeAttach);
            }

            // prepare qty
            if (attachInfo.getAttachType().equals(AttachType.ORDER_FORECAST)) {

                // if order forecast
                activeAttach.setOfcQty(DecimalUtil.add(activeAttach.getOfcQty(), attachInfo.getQty()));
            } else if (attachInfo.getAttachType().equals(AttachType.PLAN)) {

                // if delivery plan
                // set PO list
                if (activeAttach.getImpPoCount() == null || activeAttach.getImpPoCount().intValue() == IntDef.INT_ZERO) {

                    // set imp PO count
                    activeAttach.setImpPoCount(IntDef.INT_ONE);
                    activeAttach.setImpPoNo(attachInfo.getImpPoNo());
                    //activeAttach.setOrderPlanQty(attachInfo.getQty());
                } else {

                    // contact
                    StringBuffer newImpCombStr = new StringBuffer(activeAttach.getImpPoNo());

                    // add qty
                    if (activeAttach.getImpPoCount().intValue() == IntDef.INT_ONE) {
                        newImpCombStr.append(StringConst.LEFT_BRACKET);
                        newImpCombStr.append(activeAttach.getOrderPlanQty().setScale(digit));
                        newImpCombStr.append(StringConst.RIGHT_BRACKET);
                    }

                    // add new imp PO no.
                    newImpCombStr.append(StringConst.COMMA);
                    newImpCombStr.append(attachInfo.getImpPoNo());
                    newImpCombStr.append(StringConst.LEFT_BRACKET);
                    newImpCombStr.append(attachInfo.getQty().setScale(digit));
                    newImpCombStr.append(StringConst.RIGHT_BRACKET);

                    // reset imp PO no.
                    activeAttach.setImpPoNo(newImpCombStr.toString());
                    activeAttach.setImpPoCount(activeAttach.getImpPoCount() + IntDef.INT_ONE);
                }

                // set delivery plan qty
                activeAttach.setOrderPlanQty(DecimalUtil.add(activeAttach.getOrderPlanQty(), attachInfo.getQty()));
            } else {

                // if on-shipping
                // set Invoice list
                if (activeAttach.getInvCount() == null || activeAttach.getInvCount().intValue() == IntDef.INT_ZERO) {

                    // set Invoice count
                    activeAttach.setInvCount(IntDef.INT_ONE);
                    activeAttach.setInvoiceNo(attachInfo.getInvoiceNo());
                    //activeAttach.setOnShippingQty(attachInfo.getQty());
                } else {

                    // contact
                    StringBuffer newInvCombStr = new StringBuffer(activeAttach.getInvoiceNo());

                    // add qty
                    if (activeAttach.getInvCount().intValue() == IntDef.INT_ONE) {
                        newInvCombStr.append(StringConst.LEFT_BRACKET);
                        newInvCombStr.append(activeAttach.getOnShippingQty().setScale(digit));
                        newInvCombStr.append(StringConst.RIGHT_BRACKET);
                    }

                    // add new imp PO no.
                    newInvCombStr.append(StringConst.COMMA);
                    newInvCombStr.append(attachInfo.getInvoiceNo());
                    newInvCombStr.append(StringConst.LEFT_BRACKET);
                    newInvCombStr.append(attachInfo.getQty().setScale(digit));
                    newInvCombStr.append(StringConst.RIGHT_BRACKET);

                    // reset invoice No.
                    activeAttach.setInvoiceNo(newInvCombStr.toString());
                    activeAttach.setInvCount(activeAttach.getInvCount() + IntDef.INT_ONE);
                }

                // set on-Shipping qty
                activeAttach.setOnShippingQty(DecimalUtil.add(activeAttach.getOnShippingQty(), attachInfo.getQty()));
            }
        }
    }

    /**
     * prepare information for yet not etd.
     *
     * @param cntSheet current sheet
     * @param rdMasterInfo run-down master information
     * @param styleMap cell style map
     * @param lang language
     * 
     * @return last index of table
     */
    private static int setYetNotETDTable(Sheet cntSheet, TntRundownMasterEx rdMasterInfo,
        Map<String, CellStyle> styleMap, Language lang) {

        // start row
        int writeRow = NOT_IN_START_ROW;

        // check list
        if (rdMasterInfo.getYetEtdList() == null || rdMasterInfo.getYetEtdList().isEmpty()) {
            return ++writeRow;
        }

        // get decimal digit
        String decimalKey = new StringBuffer(DECIMAL_WHITE).append(rdMasterInfo.getDigit()).toString();
        CellStyle decimalStyle = styleMap.get(decimalKey);
        CellStyle labelStyle = styleMap.get(LABEL_WHITE);
        CellStyle monthStyle = styleMap.get(MONTH_PREFIX.concat(lang.getName()));

        // loop set value
        int col = IntDef.INT_ONE;
        // set
        for (TntNotInRundownEx yetEtdDetail : rdMasterInfo.getYetEtdList()) {

            // reset
            col = IntDef.INT_ONE;

            // set Imp PO No./Kan Ban No.
            PoiUtil.setCellStyle(cntSheet, writeRow, col, labelStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetEtdDetail.getImpPoNo());

            // set Exp PO No.
            PoiUtil.setCellStyle(cntSheet, writeRow, col, labelStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetEtdDetail.getExpPoNo());

            // set Order Month
            PoiUtil.setCellStyle(cntSheet, writeRow, col, monthStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++,
                DateTimeUtil.parseDate(yetEtdDetail.getOrderMonth(), DateTimeUtil.FORMAT_YEAR_MONTH));

            // set Total Qty
            PoiUtil.setCellStyle(cntSheet, writeRow, col, decimalStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetEtdDetail.getQty());

            // next row
            writeRow++;
        }

        // return last row
        return writeRow;
    }

    /**
     * prepare information for yet not in bound.
     *
     * @param cntSheet current sheet
     * @param rdMasterInfo run-down master information
     * @param styleMap cell style map
     * @param valuesMap title values Maps
     * @param startRow start row
     * @param lang language
     * 
     * @return last index of table
     */
    private static int setYetNotInboundTable(Sheet cntSheet, TntRundownMasterEx rdMasterInfo,
        Map<String, CellStyle> styleMap, Map<String, String> valuesMap, int startRow, Language lang) {

        // start row
        int writeRow = startRow + NOT_IN_STEP;
        int col = IntDef.INT_ONE;

        // set Yet Inbound (Not in Rundown)
        PoiUtil.setCellStyle(cntSheet, writeRow, col, styleMap.get(LABEL_FONT_RED));
        PoiUtil.setCellValue(cntSheet, writeRow, col, valuesMap.get(NOT_IN_YETINBOUND));
        writeRow++;

        // set Condition
        PoiUtil.setCellStyle(cntSheet, writeRow, col, styleMap.get(LABEL_WHITE_WO_LINE));
        PoiUtil.setCellValue(cntSheet, writeRow, col, valuesMap.get(NOT_IN_YETINBOUND.concat(CONDITION)));
        writeRow++;

        // set list header
        // get header cell format
        CellStyle headerStyle = styleMap.get(NOT_IN_RD_HEADER);

        // prepare yet in-bound title
        StringBuffer key = new StringBuffer();
        for (int i = IntDef.INT_ZERO; i < TITLE_LEN_YETINBOUND; i++) {

            // get key
            key.setLength(IntDef.INT_ZERO);
            key.append(NOT_IN_YETINBOUND).append(NOT_TITLE).append(i + 1);

            // set into map
            PoiUtil.setCellStyle(cntSheet, writeRow, col, headerStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col, valuesMap.get(key.toString()));

            // next row
            PoiUtil.setCellStyle(cntSheet, writeRow + IntDef.INT_ONE, col, headerStyle);

            // merge
            CellRangeAddress address = new CellRangeAddress(writeRow - IntDef.INT_ONE, writeRow, col - IntDef.INT_ONE,
                col - IntDef.INT_ONE);
            cntSheet.addMergedRegion(address);

            // next column
            col++;
        }
        // next row
        writeRow++;
        col = IntDef.INT_ONE;

        // check list data
        if (rdMasterInfo.getYetInboundList() == null || rdMasterInfo.getYetInboundList().isEmpty()) {
            return ++writeRow;
        }

        // get decimal digit
        String decimalKey = new StringBuffer(DECIMAL_WHITE).append(rdMasterInfo.getDigit()).toString();
        CellStyle decimalStyle = styleMap.get(decimalKey);
        CellStyle labelStyle = styleMap.get(LABEL_WHITE);
        CellStyle dateStyle = styleMap.get(DATE_WHITE_PREFIX.concat(lang.getName()));

        // set
        writeRow++;
        for (TntNotInRundownEx yetInboundDetail : rdMasterInfo.getYetInboundList()) {

            // reset
            col = IntDef.INT_ONE;

            // set Exp Invoice No.
            PoiUtil.setCellStyle(cntSheet, writeRow, col, labelStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetInboundDetail.getInvoiceNo());

            // set ETD Date
            PoiUtil.setCellStyle(cntSheet, writeRow, col, dateStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetInboundDetail.getEtd());

            // set Transport Mode
            String transportMode = CodeCategoryManager.getCodeName(lang.getCode(), CodeMasterCategory.TRANSPORT_MODE,
                yetInboundDetail.getTransportMode());
            PoiUtil.setCellStyle(cntSheet, writeRow, col, labelStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, transportMode);

            // set Invoice ETA
            PoiUtil.setCellStyle(cntSheet, writeRow, col, dateStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetInboundDetail.getEta());

            // set Qty
            PoiUtil.setCellStyle(cntSheet, writeRow, col, decimalStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetInboundDetail.getQty());

            // next row
            writeRow++;
        }

        // return last row
        return writeRow;
    }

    /**
     * prepare information for ng on-hold list.
     *
     * @param cntSheet current sheet
     * @param rdMasterInfo run-down master information
     * @param styleMap cell style map
     * @param valuesMap title values Maps
     * @param startRow start row
     * @param lang language
     * 
     * @return last index of table
     */
    private static int setNgOnHoldTable(Sheet cntSheet, TntRundownMasterEx rdMasterInfo,
        Map<String, CellStyle> styleMap, Map<String, String> valuesMap, int startRow, Language lang) {

        // start row
        int writeRow = startRow + NOT_IN_STEP;
        int col = IntDef.INT_ONE;

        // set Imp W/H On Hold (Not in Rundown)
        PoiUtil.setCellStyle(cntSheet, writeRow, col, styleMap.get(LABEL_FONT_RED));
        PoiUtil.setCellValue(cntSheet, writeRow, col, valuesMap.get(NOT_IN_RUNDOWN_NG));
        writeRow++;

        // set Condition
        PoiUtil.setCellStyle(cntSheet, writeRow, col, styleMap.get(LABEL_WHITE_WO_LINE));
        PoiUtil.setCellValue(cntSheet, writeRow, col, valuesMap.get(NOT_IN_RUNDOWN_NG.concat(CONDITION)));
        writeRow++;

        // set list header
        // get header cell format
        CellStyle headerStyle = styleMap.get(NOT_IN_RD_HEADER);

        // prepare yet in-bound title
        StringBuffer key = new StringBuffer();
        for (int i = IntDef.INT_ONE; i <= TITLE_LEN_NG; i++) {

            // get key
            key.setLength(IntDef.INT_ZERO);
            key.append(NOT_IN_RUNDOWN_NG).append(NOT_TITLE).append(i);

            // set into map
            PoiUtil.setCellStyle(cntSheet, writeRow, col, headerStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col, valuesMap.get(key.toString()));

            // next row
            PoiUtil.setCellStyle(cntSheet, writeRow + IntDef.INT_ONE, col, headerStyle);

            // merge
            CellRangeAddress address = new CellRangeAddress(writeRow - IntDef.INT_ONE, writeRow, col - IntDef.INT_ONE,
                col - IntDef.INT_ONE);
            cntSheet.addMergedRegion(address);

            // next column
            col++;
        }
        // next row
        writeRow++;
        col = IntDef.INT_ONE;

        // check list data
        if (rdMasterInfo.getImpWhOnHoldList() == null || rdMasterInfo.getImpWhOnHoldList().isEmpty()) {
            return ++writeRow;
        }

        // get decimal digit
        String decimalKey = new StringBuffer(DECIMAL_WHITE).append(rdMasterInfo.getDigit()).toString();
        CellStyle decimalStyle = styleMap.get(decimalKey);
        CellStyle labelStyle = styleMap.get(LABEL_WHITE);
        CellStyle dateStyle = styleMap.get(DATE_WHITE_PREFIX.concat(lang.getName()));

        // set
        writeRow++;
        for (TntNotInRundownEx yetInboundDetail : rdMasterInfo.getImpWhOnHoldList()) {

            // reset
            col = IntDef.INT_ONE;

            // set Exp Invoice No.
            PoiUtil.setCellStyle(cntSheet, writeRow, col, labelStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetInboundDetail.getInvoiceNo());

            // set ETD Date
            PoiUtil.setCellStyle(cntSheet, writeRow, col, dateStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetInboundDetail.getEtd());

            // set Transport Mode
            String transportMode = CodeCategoryManager.getCodeName(lang.getCode(), CodeMasterCategory.TRANSPORT_MODE,
                yetInboundDetail.getTransportMode());
            PoiUtil.setCellStyle(cntSheet, writeRow, col, labelStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, transportMode);

            // set Invoice ETA
            PoiUtil.setCellStyle(cntSheet, writeRow, col, dateStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetInboundDetail.getEta());

            // Imp Inbound Date
            PoiUtil.setCellStyle(cntSheet, writeRow, col, dateStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetInboundDetail.getImpInbDate());

            // On Hold Date
            PoiUtil.setCellStyle(cntSheet, writeRow, col, dateStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetInboundDetail.getProcessDate());

            // set Qty
            PoiUtil.setCellStyle(cntSheet, writeRow, col, decimalStyle);
            PoiUtil.setCellValue(cntSheet, writeRow, col++, yetInboundDetail.getQty());

            // next row
            writeRow++;
        }

        // return last row
        return writeRow;
    }
}
