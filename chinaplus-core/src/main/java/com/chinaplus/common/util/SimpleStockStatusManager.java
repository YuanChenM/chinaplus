/**
 * @screen Simple Stock Status file manager
 * @author liu_yinchuan
 */
package com.chinaplus.common.util;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.chinaplus.common.bean.TntRdDetailAttachEx;
import com.chinaplus.common.bean.TntStockStatusEx;
import com.chinaplus.common.bean.TntStockStatusHeader;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.AlarmFlag;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.CustStockFlag;
import com.chinaplus.common.consts.CodeConst.ImpStockFlag;
import com.chinaplus.common.consts.CodeConst.InventoryByBox;
import com.chinaplus.common.consts.CodeConst.NoCfcFlag;
import com.chinaplus.common.entity.TntRdAttachShipping;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Simple Stock status file manager.
 * 
 * @author liu_yinchuan
 */
public class SimpleStockStatusManager {

    /** sheet name */
    private final static String SHEET_NAME_STYLE = "styleSheet";

    /** sheet name */
    private final static String SHEET_NAME_WORKING = "Stock Status";

    /** template DATE_WHITE_RED_FONT */
    private final static String DATE_WHITE_RED_FONT = "DATE_WHITE_RED_FONT";

    /** template DATE_WHITE_NORMAL */
    private final static String DATE_WHITE_NORMAL = "DATE_WHITE_NORMAL";

    /** template MONTH_PREFIX */
    private final static String MONTH_PREFIX = "MONTH_PREFIX";

    /** template DATE_GREEN_NORMAL */
    private final static String DATE_GREEN_NORMAL = "DATE_GREEN_NORMAL";

    /** template DATE_GREEN_RED_FONT */
    private final static String DATE_GREEN_RED_FONT = "DATE_GREEN_RED_FONT";

    /** template LABEL_WHITE_LEFT */
    private final static String LABEL_WHITE_LEFT = "LABEL_WHITE_LEFT";

    /** template LABEL_WHITE_CENTER */
    private final static String LABEL_WHITE_CENTER = "LABEL_WHITE_CENTER";

    /** template LABEL_WHITE_RED_CENTER */
    private final static String LABEL_WHITE_RED_CENTER = "LABEL_WHITE_RED_CENTER";

    /** template LABEL_GREEN_LEFT */
    private final static String LABEL_GREEN_LEFT = "LABEL_GREEN_LEFT";

    /** template LABEL_GREEN_CENTER */
    private final static String LABEL_GREEN_CENTER = "LABEL_GREEN_CENTER";

    /** template LABEL_GREEN_RED_CENTER */
    private final static String LABEL_GREEN_RED_CENTER = "LABEL_GREEN_RED_CENTER";

    /** template label gray 15% */
    private final static String LABEL_GRAY15 = "LABEL_GRAY15";

    /** template decimal percentage */
    private final static String NORMAL_LABEL_WO_LINE = "NORMAL_LABEL_WO_LINE";

    /** template decimal percentage */
    private final static String DECIMAL_PERCENTAGE = "DECIMAL_PERCENTAGE";

    /** template decimal percentage */
    private final static String DECIMAL_PERCENTAGE_GREEN = "DECIMAL_PERCENTAGE_GREEN";

    /** template decimal white */
    private final static String DECIMAL_WHITE = "DECIMAL_WHITE_";

    /** template decimal green */
    private final static String DECIMAL_GREEN = "DECIMAL_GREEN_";

    /** template decimal white */
    private final static String DECIMAL_WHITE_RED_FONT = "DECIMAL_WHITE_RED_FONT_";

    /** template decimal green */
    private final static String DECIMAL_GREEN_RED_FONT = "DECIMAL_GREEN_RED_FONT_";

    /** point width of column */
    private final static double POINT_WIDTH = 357;

    /** CPSSSF01_Label_NoCustomerStock=No Customer Stock */
    private final static String LABEL_NOCUSTOMERSTOCK = "CPSSSF02_Label_NoCustomerStock";

    /** CPSSSF01_Label_NoCustomerForecast=No Customer Forecast */
    private final static String LABEL_NOCUSTOMERFORECAST = "CPSSSF02_Label_NoCustomerForecast";

    /** CPSSSF01_Label_NoOrderCustomerForecast */
    private final static String DOWNLOAD_TIME = "CPSSSF02_Label_DownloadTime";

    /** CPSSSF01_Label_NoOrderCustomerForecast */
    private final static String SYNC_SSMS_DATE_TIME = "CPSSSF02_Label_LastSSMSInterfaceTime";

    /** CPSSSF01_Label_NoOrderCustomerForecast */
    private final static String SYNC_VV_DATE_TIME = "CPSRDF02_Label_LastVVInterfaceTime";

    /** CPSSSF01_Label_NoOrderCustomerForecast */
    private final static String SYNC_AISIN_DATE_TIME = "CPSRDF02_Label_LastASInterfaceTime";

    /**
     * Prepare style map for stock status report run-down file.
     *
     * @param wbTemplate wbTemplate
     * @param styleMap style map
     * @param lang language
     */
    public static void prepStyleMapStockStatus(Workbook wbTemplate, Map<String, CellStyle> styleMap, Language lang) {

        // get style Sheet
        Sheet styleSheet = wbTemplate.getSheet(SHEET_NAME_STYLE);

        // prepare label
        // styleMap.put(LABEL_GRAY25, PoiUtil.getCell(styleSheet, IntDef.INT_TWO, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_WHITE_LEFT, PoiUtil.getCell(styleSheet, IntDef.INT_THREE, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_WHITE_CENTER, PoiUtil.getCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_WHITE_RED_CENTER, PoiUtil.getCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_ONE)
            .getCellStyle());
        styleMap.put(LABEL_GREEN_LEFT, PoiUtil.getCell(styleSheet, IntDef.INT_SIX, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_GREEN_CENTER, PoiUtil.getCell(styleSheet, IntDef.INT_SEVEN, IntDef.INT_ONE).getCellStyle());
        styleMap.put(LABEL_GREEN_RED_CENTER, PoiUtil.getCell(styleSheet, IntDef.INT_EIGHT, IntDef.INT_ONE)
            .getCellStyle());
        styleMap.put(LABEL_GRAY15, PoiUtil.getCell(styleSheet, IntDef.INT_NINE, IntDef.INT_ONE).getCellStyle());
        styleMap.put(NORMAL_LABEL_WO_LINE, PoiUtil.getCell(styleSheet, IntDef.INT_TWELVE, IntDef.INT_ONE)
            .getCellStyle());

        // prepare Date
        // check language
        if (Language.CHINESE.equals(lang)) {
            styleMap.put(DATE_WHITE_RED_FONT, PoiUtil.getCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWO)
                .getCellStyle());
            styleMap
                .put(DATE_WHITE_NORMAL, PoiUtil.getCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWO).getCellStyle());
            styleMap.put(MONTH_PREFIX, PoiUtil.getCell(styleSheet, IntDef.INT_SIX, IntDef.INT_TWO).getCellStyle());
            styleMap.put(DATE_GREEN_NORMAL, PoiUtil.getCell(styleSheet, IntDef.INT_EIGHT, IntDef.INT_TWO)
                .getCellStyle());
            styleMap.put(DATE_GREEN_RED_FONT, PoiUtil.getCell(styleSheet, IntDef.INT_TEN, IntDef.INT_TWO)
                .getCellStyle());
        } else {
            styleMap.put(DATE_WHITE_RED_FONT, PoiUtil.getCell(styleSheet, IntDef.INT_THREE, IntDef.INT_TWO)
                .getCellStyle());
            styleMap
                .put(DATE_WHITE_NORMAL, PoiUtil.getCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TWO).getCellStyle());
            styleMap.put(MONTH_PREFIX, PoiUtil.getCell(styleSheet, IntDef.INT_SEVEN, IntDef.INT_TWO).getCellStyle());
            styleMap
                .put(DATE_GREEN_NORMAL, PoiUtil.getCell(styleSheet, IntDef.INT_NINE, IntDef.INT_TWO).getCellStyle());
            styleMap.put(DATE_GREEN_RED_FONT, PoiUtil.getCell(styleSheet, IntDef.INT_ELEVEN, IntDef.INT_TWO)
                .getCellStyle());
        }

        // prepare Decimal Percentage
        styleMap.put(DECIMAL_PERCENTAGE, PoiUtil.getCell(styleSheet, IntDef.INT_TEN, IntDef.INT_ONE).getCellStyle());
        styleMap.put(DECIMAL_PERCENTAGE_GREEN, PoiUtil.getCell(styleSheet, IntDef.INT_ELEVEN, IntDef.INT_ONE)
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
            // green
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_GREEN).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_FOUR).getCellStyle());
            // write
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_WHITE_RED_FONT).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_FIVE).getCellStyle());
            // green
            sb.setLength(IntDef.INT_ZERO);
            sb.append(DECIMAL_GREEN_RED_FONT).append(i);
            styleMap.put(sb.toString(), PoiUtil.getCell(styleSheet, row, IntDef.INT_SIX).getCellStyle());
        }
    }

    /**
     * prepare header content of Stock status report.
     *
     * @param workBook current work book
     * @param lang language
     * @param headMap head information Map
     * @param header stock status header
     */
    public static void setHeaderForStockStatus(Workbook workBook, Language lang, Map<Integer, String> headMap,
        TntStockStatusHeader header) {

        // prepare header
        int cntRow = IntDef.INT_FOUR;
        int styRow = IntDef.INT_FOURTEEN;
        int cntCol = IntDef.INT_SIXTEEN;
        int styCol = IntDef.INT_ONE;

        // get style sheet
        Sheet stySheet = workBook.getSheet(SHEET_NAME_STYLE);
        // get working sheet
        Sheet cntSheet = workBook.getSheet(SHEET_NAME_WORKING);

        // prepare on-shipping information
        // set Total On-Shipping Qty
        copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
        mergeCells(cntSheet, cntRow + IntDef.INT_ONE, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
        cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_FIFTEEN));
        // set on-shipping detail
        cntCol++;
        styCol = lang.equals(Language.CHINESE) ? styCol + IntDef.INT_TWO : styCol + IntDef.INT_ONE;
        List<TntRdDetailAttachEx> onShippingList = header.getOnShippingDetailList();
        int size = onShippingList.size();
        for (int i = IntDef.INT_ZERO; i < size; i++) {

            // get shipping detail information
            TntRdDetailAttachEx detail = onShippingList.get(i);

            // copy cells
            copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
            cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHTEEN));

            // set etd
            PoiUtil.setCellValue(cntSheet, cntRow + IntDef.INT_TWO, cntCol, detail.getEtd());
            // set eta
            PoiUtil.setCellValue(cntSheet, cntRow + IntDef.INT_FOUR, cntCol, detail.getEta());

            // string key
            StringBuffer key = new StringBuffer();
            key.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, detail.getImpInbPlanDate()));
            key.append(StringConst.MIDDLE_LINE);
            key.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, detail.getEtd()));
            key.append(StringConst.MIDDLE_LINE);
            key.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, detail.getEta()));

            // set into header map
            headMap.put(cntCol, key.toString());

            // next
            cntCol++;
        }
        // set OnShipping title(Merge)
        if (size > IntDef.INT_ZERO) {

            // merge
            mergeCells(cntSheet, cntRow, cntRow, cntCol - IntDef.INT_ONE - size, cntCol - IntDef.INT_ONE);
            // group
            cntSheet.groupColumn(cntCol - IntDef.INT_ONE - size, cntCol - IntDef.INT_TWO);
            cntSheet.setColumnGroupCollapsed(cntCol - IntDef.INT_TWO, true);
        }

        // Imp Customs Clearnance
        styCol = lang.equals(Language.CHINESE) ? styCol + IntDef.INT_TWO : styCol + IntDef.INT_THREE;
        copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
        mergeCells(cntSheet, cntRow + IntDef.INT_ONE, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
        cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_FIFTEEN));

        // Imp W/H Stock
        for (int i = IntDef.INT_ZERO; i < IntDef.INT_FOUR; i++) {
            cntCol++;
            styCol++;
            copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
            mergeCells(cntSheet, cntRow + IntDef.INT_ONE, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
            cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_FIFTEEN));
        }
        // merge
        mergeCells(cntSheet, cntRow, cntRow, cntCol - IntDef.INT_THREE, cntCol);

        // Customer Stock
        cntCol++;
        styCol++;
        copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
        mergeCells(cntSheet, cntRow, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
        cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_TWENTY_TWO));

        // Total Supply Chain Stock Qty
        cntCol++;
        styCol++;
        copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
        mergeCells(cntSheet, cntRow, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
        cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_FIFTEEN));

        // Not In Rundown
        cntCol++;
        styCol++;
        copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
        mergeCells(cntSheet, cntRow + IntDef.INT_ONE, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
        cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_FIFTEEN));

        // alarm 1
        for (int i = IntDef.INT_ZERO; i < IntDef.INT_THREE; i++) {
            cntCol++;
            styCol++;
            copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
            mergeCells(cntSheet, cntRow + IntDef.INT_ONE, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
            cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHTEEN));
        }
        // merge
        mergeCells(cntSheet, cntRow, cntRow, cntCol - IntDef.INT_TWO, cntCol);

        // alarm 2
        for (int i = IntDef.INT_ZERO; i < IntDef.INT_FOUR; i++) {
            cntCol++;
            styCol++;
            copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
            mergeCells(cntSheet, cntRow + IntDef.INT_ONE, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
            cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHTEEN));
        }
        // merge
        mergeCells(cntSheet, cntRow, cntRow, cntCol - IntDef.INT_THREE, cntCol);

        // alarm 3
        for (int i = IntDef.INT_ZERO; i < IntDef.INT_THREE; i++) {
            cntCol++;
            styCol++;
            copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
            mergeCells(cntSheet, cntRow + IntDef.INT_ONE, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
            cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHTEEN));
        }
        // merge
        mergeCells(cntSheet, cntRow, cntRow, cntCol - IntDef.INT_TWO, cntCol);

        // alarm 4
        for (int i = IntDef.INT_ZERO; i < IntDef.INT_TWO; i++) {
            cntCol++;
            styCol++;
            copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
            mergeCells(cntSheet, cntRow + IntDef.INT_ONE, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
            cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_EIGHTEEN));
        }
        // merge
        mergeCells(cntSheet, cntRow, cntRow, cntCol - IntDef.INT_ONE, cntCol);

        // Build out Data
        cntCol++;
        styCol++;
        copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
        mergeCells(cntSheet, cntRow + IntDef.INT_ONE, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
        cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_FIFTEEN));

        // Discontinue Indicator
        cntCol++;
        styCol++;
        copyCells(stySheet, cntSheet, styRow, styCol, cntRow, cntCol, IntDef.INT_FIVE);
        mergeCells(cntSheet, cntRow, cntRow + IntDef.INT_FOUR, cntCol, cntCol);
        cntSheet.setColumnWidth(cntCol - IntDef.INT_ONE, (short) (POINT_WIDTH * IntDef.INT_FIFTEEN));
    }

    /**
     * Copy cell with column.
     *
     * @param fromSheet fromSheet
     * @param toSheet toSheet
     * @param fmRow from Row
     * @param fmCol from column
     * @param toRow to row
     * @param toCol to column
     * @param rows rows
     */
    private static void copyCells(Sheet fromSheet, Sheet toSheet, int fmRow, int fmCol, int toRow, int toCol, int rows) {

        // define
        Cell srcCell = null;
        Cell targCell = null;

        // loop set
        for (int i = IntDef.INT_ZERO; i < rows; i++) {

            // from cell
            srcCell = PoiUtil.getCell(fromSheet, fmRow + i, fmCol);
            // target cell
            targCell = PoiUtil.getOrCreateCell(toSheet, toRow + i, toCol);
            // copy
            copyCell(srcCell, targCell, true);
        }
    }

    /**
     * Copy cell.
     * 
     * @param srcCell source cell
     * @param targCell target cell
     * @param isCopyValue need do copy values
     */
    private static void copyCell(Cell srcCell, Cell targCell, boolean isCopyValue) {

        // copy
        targCell.setCellStyle(srcCell.getCellStyle());
        targCell.setCellType(srcCell.getCellType());

        // copy values
        if (isCopyValue) {
            targCell.setCellValue(srcCell.getRichStringCellValue());
        }
    }

    /**
     * Merge cells.
     *
     * @param sheet sheet
     * @param startRow start row
     * @param endRow end row
     * @param startCol start column
     * @param endCol end column
     */
    private static void mergeCells(Sheet sheet, int startRow, int endRow, int startCol, int endCol) {

        // get cell range address
        CellRangeAddress add = new CellRangeAddress(startRow - IntDef.INT_ONE, endRow - IntDef.INT_ONE, startCol
                - IntDef.INT_ONE, endCol - IntDef.INT_ONE);

        // add
        sheet.addMergedRegion(add);
    }

    /**
     * Set stock status detail information by each parts.
     *
     * @param workbook work book
     * @param stockStatusList stock status list
     * @param lang language
     * @param styleMap style map
     * @param headMap head map
     * @param office office code
     * @param dateList sync date time
     */
    public static void setStockStatusDetailInfo(SXSSFWorkbook workbook, List<TntStockStatusEx> stockStatusList,
        Language lang, Map<String, CellStyle> styleMap, Map<Integer, String> headMap, String office, Date[] dateList) {

        // get sheet
        Sheet cntSheet = workbook.getSheet(SHEET_NAME_WORKING);
        int row = IntDef.INT_NINE;
        int col = IntDef.INT_ONE;

        // get labels
        // get no customer stock update
        String noCustomerStock = MessageManager.getMessage(LABEL_NOCUSTOMERSTOCK, lang.getName());
        // get no customer forecast
        String noCustForecast = MessageManager.getMessage(LABEL_NOCUSTOMERFORECAST, lang.getName());

        // get header map key
        // get head map key set
        Set<Integer> colSet = headMap.keySet();
        List<Integer> colList = new ArrayList<Integer>(colSet);
        Collections.sort(colList);

        // style key
        StringBuffer styleKeyBuffer = new StringBuffer();
        CellStyle decimalStyle = null;
        StringBuffer formula = new StringBuffer();
        StringBuffer supplyFormula = new StringBuffer();
        String styleKey = null;
        // get no master style
        CellStyle noMasterStyle = null;
        // get no master style
        CellStyle labelGrayStyle = styleMap.get(LABEL_GRAY15);
        CellStyle normalLabelStyle = styleMap.get(LABEL_WHITE_LEFT);

        // loop each stock status information from information list
        for (TntStockStatusEx stockStatus : stockStatusList) {

            // set no master style
            noMasterStyle = styleMap.get(stockStatus.isHasMoreSupplier() ? LABEL_GREEN_RED_CENTER
                : LABEL_WHITE_RED_CENTER);

            // TTC Parts No.
            PoiUtil.setCellStyle(cntSheet, row, col, normalLabelStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getTtcPartsNo());
            // Parts Name In English
            PoiUtil.setCellStyle(cntSheet, row, col, normalLabelStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getPartsNameEn());
            // Parts Name In Chinese
            PoiUtil.setCellStyle(cntSheet, row, col, normalLabelStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getPartsNameCn());
            // Exp Country
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_CENTER));
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getExpCounty());
            // TTC Supplier Code
            PoiUtil.setCellStyle(cntSheet, row, col, normalLabelStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getSupplierCode());
            // Imp Country
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_CENTER));
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getImpCountry());
            // Imp Office Code
            PoiUtil.setCellStyle(cntSheet, row, col, normalLabelStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getImpOfficeCode());
            // TTC Customer Code
            PoiUtil.setCellStyle(cntSheet, row, col, normalLabelStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getCustomerCode());
            // Customer Part No.
            PoiUtil.setCellStyle(cntSheet, row, col, normalLabelStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getCustPartsNo());
            // Customer Back No.
            PoiUtil.setCellStyle(cntSheet, row, col, normalLabelStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getCustBackNo());
            // Business Type
            String businessType = CodeCategoryManager.getCodeName(lang.getCode(), CodeMasterCategory.BUSINESS_TYPE,
                stockStatus.getBusinessType());
            PoiUtil.setCellStyle(cntSheet, row, col, normalLabelStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, businessType);
            // Parts Type
            String partsType = CodeCategoryManager.getCodeName(lang.getCode(), CodeMasterCategory.PARTS_TYPE,
                stockStatus.getPartsType());
            PoiUtil.setCellStyle(cntSheet, row, col, normalLabelStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, partsType);
            // Car Model
            PoiUtil.setCellStyle(cntSheet, row, col, normalLabelStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getCarModel());
            
            // reset decimal style
            styleKeyBuffer.setLength(IntDef.INT_ZERO);
            styleKeyBuffer.append(DECIMAL_WHITE).append(stockStatus.getDigit());
            decimalStyle = styleMap.get(styleKeyBuffer.toString());

            // Exp Balance Order
            // Total Exp Balance Order
            if (stockStatus.getBusinessPattern().equals(BusinessPattern.V_V)) {
                PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
                BigDecimal totalExpBalance = DecimalUtil.add(stockStatus.getExpPlanDelayQty(),
                    stockStatus.getExpFuturePlanQty());
                PoiUtil.setCellValue(cntSheet, row, col++, totalExpBalance);
            } else {
                PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
                PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getExpFuturePlanQty());
            }
            // supplyFormula
            supplyFormula.setLength(IntDef.INT_ZERO);
            supplyFormula.append("SUM(");
            supplyFormula.append(CellReference.convertNumToColString(col - IntDef.INT_TWO));
            supplyFormula.append(row).append(StringConst.COMMA);

            // Exp W/H Stock
            // Total Stock Qty
            if (stockStatus.getBusinessPattern().equals(BusinessPattern.V_V)) {
                PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
                PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getExpStockQty());
            } else {
                PoiUtil.setCellStyle(cntSheet, row, col++, labelGrayStyle);
            }
            supplyFormula.append(CellReference.convertNumToColString(col - IntDef.INT_TWO));
            supplyFormula.append(row).append(StringConst.COMMA);

            // On Shipping
            // Total On Shipping Qty
            PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
            if (headMap.size() > IntDef.INT_ZERO) {
                formula.setLength(IntDef.INT_ZERO);
                formula.append("SUM(");
                formula.append(CellReference.convertNumToColString(col));
                formula.append(row).append(StringConst.COLON);
                formula.append(CellReference.convertNumToColString(col + headMap.size() - IntDef.INT_ONE));
                formula.append(row).append(StringConst.RIGHT_BRACKET);

                PoiUtil.setCellFormula(cntSheet, row, col, formula.toString());
            } else {
                PoiUtil.setCellValue(cntSheet, row, col, BigDecimal.ZERO);
            }
            // set supply formula
            supplyFormula.append(CellReference.convertNumToColString(col - IntDef.INT_ONE));
            supplyFormula.append(row).append(StringConst.COMMA);

            // On Shipping detail
            int idx = IntDef.INT_ZERO;
            List<TntRdAttachShipping> onShppingList = stockStatus.getOnShippingList();
            StringBuffer shipKey = new StringBuffer();
            for (int s = IntDef.INT_ZERO; s < colList.size(); s++) {
                // get column index
                col = colList.get(s);
                // get values
                String shipInfo = headMap.get(col);
                // set style
                PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
                // get on-shipping information
                for (; idx < onShppingList.size(); idx++) {
                    // get detail
                    TntRdAttachShipping detail = onShppingList.get(idx);
                    // prepare key
                    shipKey.setLength(IntDef.INT_ZERO);
                    shipKey.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, detail.getImpInbPlanDate()));
                    shipKey.append(StringConst.MIDDLE_LINE);
                    shipKey.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, detail.getEtd()));
                    shipKey.append(StringConst.MIDDLE_LINE);
                    shipKey.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, detail.getEta()));
                    // compare key
                    int comp = shipInfo.compareTo(shipKey.toString());
                    if (comp > IntDef.INT_ZERO) {
                        continue;
                    } else if (comp < IntDef.INT_ZERO) {
                        break;
                    }
                    // set Invoice Qty
                    PoiUtil.setCellValue(cntSheet, row, col, detail.getQty());
                    break;
                }
            }

            // Imp Customs Clearnance
            // Total Stock Qty after Customs Clearance before Imp Inbound
            col++;
            if (stockStatus.getImpStockFlag().equals(ImpStockFlag.WITH_CLEARANCE)) {
                PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
                PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getInRackQty());
            } else {
                PoiUtil.setCellStyle(cntSheet, row, col++, labelGrayStyle);
            }
            supplyFormula.append(CellReference.convertNumToColString(col - IntDef.INT_TWO));
            supplyFormula.append(row).append(StringConst.COMMA);

            // Imp W/H Stock
            // Total Import Stock Qty(Not Include NG on-hold in Warehouse)
            PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
            formula.setLength(IntDef.INT_ZERO);
            formula.append("SUM(");
            formula.append(CellReference.convertNumToColString(col));
            formula.append(row).append(StringConst.COLON);
            formula.append(CellReference.convertNumToColString(col + IntDef.INT_TWO));
            formula.append(row).append(StringConst.RIGHT_BRACKET);
            PoiUtil.setCellFormula(cntSheet, row, col++, formula.toString());
            supplyFormula.append(CellReference.convertNumToColString(col - IntDef.INT_TWO));
            supplyFormula.append(row).append(StringConst.COMMA);
            // Total ECI on hold Stock Qty
            PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getEciOnholdQty());
            // Available Import Stock(Not include ECI on-hold in Warehouse)
            PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getImpWhsQty());
            // Already Prepared for Warehouse Outbound
            PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getPreparedObQty());

            // Customer Stock
            if (stockStatus.getSaCustStockFlag().equals(CustStockFlag.Y)) {
                if (stockStatus.getEndingStockDate() != null) {
                    // Total Customer Stock
                    PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
                    PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getCustStockQty());
                } else {
                    // Total Customer Stock
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_RED_CENTER));
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustomerStock);
                }
            } else {
                // Total Customer Stock
                PoiUtil.setCellStyle(cntSheet, row, col++, labelGrayStyle);
            }

            // Total Supply Chain Stock Qty
            PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
            supplyFormula.append(CellReference.convertNumToColString(col - IntDef.INT_TWO));
            supplyFormula.append(row).append(StringConst.RIGHT_BRACKET);
            PoiUtil.setCellFormula(cntSheet, row, col++, supplyFormula.toString());

            // Not In Rundown
            // Total Not In Rundown Qty
            styleKeyBuffer.setLength(IntDef.INT_ZERO);
            if (stockStatus.isHasMoreSupplier()) {
                styleKeyBuffer.append(DECIMAL_GREEN_RED_FONT).append(stockStatus.getDigit());
            } else {
                styleKeyBuffer.append(DECIMAL_WHITE_RED_FONT).append(stockStatus.getDigit());
            }
            CellStyle notInRdStyle = styleMap.get(styleKeyBuffer.toString());
            // set style
            PoiUtil.setCellStyle(cntSheet, row, col, notInRdStyle);
            BigDecimal totalNoInRd = DecimalUtil.add(stockStatus.getEtdDelayQty(), stockStatus.getInboundDelayQty());
            totalNoInRd = DecimalUtil.add(totalNoInRd, stockStatus.getNgOnholdQty());
            PoiUtil.setCellValue(cntSheet, row, col++, totalNoInRd);

            // reset
            CellStyle stockDayStyle = null;
            CellStyle stockBoxStyle = null;
            if (stockStatus.isHasMoreSupplier()) {
                styleKeyBuffer.setLength(IntDef.INT_ZERO);
                styleKeyBuffer.append(DECIMAL_GREEN).append(stockStatus.getDigit());
                decimalStyle = styleMap.get(styleKeyBuffer.toString());

                styleKeyBuffer.setLength(IntDef.INT_ZERO);
                styleKeyBuffer.append(DECIMAL_GREEN).append(IntDef.INT_ONE);
                stockDayStyle = styleMap.get(styleKeyBuffer.toString());

                styleKeyBuffer.setLength(IntDef.INT_ZERO);
                styleKeyBuffer.append(DECIMAL_GREEN).append(IntDef.INT_TWO);
                stockBoxStyle = styleMap.get(styleKeyBuffer.toString());
            } else {
                styleKeyBuffer.setLength(IntDef.INT_ZERO);
                styleKeyBuffer.append(DECIMAL_WHITE).append(IntDef.INT_ONE);
                stockDayStyle = styleMap.get(styleKeyBuffer.toString());

                styleKeyBuffer.setLength(IntDef.INT_ZERO);
                styleKeyBuffer.append(DECIMAL_WHITE).append(IntDef.INT_TWO);
                stockBoxStyle = styleMap.get(styleKeyBuffer.toString());
            }

            // Alarm1
            // End of Last day Stock Qty(Exclude NG on hold Qty)
            PoiUtil.setCellStyle(cntSheet, row, col, decimalStyle);
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getStockQty1a());

            // Stock Days
            if (stockStatus.getInventoryBoxFlag().equals(InventoryByBox.Y)) {
                PoiUtil.setCellStyle(cntSheet, row, col++, labelGrayStyle);
            } else {
                // if does not has any stock days?
                if (stockStatus.getStockDays1a() != null) {
                    // already ok
                    PoiUtil.setCellStyle(cntSheet, row, col, stockDayStyle);
                    if (stockStatus.getNoMaster1a().equals(NoCfcFlag.N)) {
                        PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getStockDays1a());
                    } else {
                        StringBuffer stockDays = new StringBuffer(StringConst.GREATER).append(StringConst.BLANK);
                        stockDays.append(StringUtil.formatBigDecimal(IntDef.INT_ONE, stockStatus.getStockDays1a()));
                        PoiUtil.setCellValue(cntSheet, row, col++, stockDays.toString());
                    }
                } else {
                    // no customer forecast
                    PoiUtil.setCellStyle(cntSheet, row, col, noMasterStyle);
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                }
            }

            // No. of Boxes
            if (stockStatus.getInventoryBoxFlag().equals(InventoryByBox.Y)) {
                // if does not has any stock days?
                if (stockStatus.getStockBoxes1a() != null) {
                    // already ok
                    PoiUtil.setCellStyle(cntSheet, row, col, stockBoxStyle);
                    PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getStockBoxes1a());
                } else {
                    // no customer forecast
                    PoiUtil.setCellStyle(cntSheet, row, col, noMasterStyle);
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                }
            } else {
                PoiUtil.setCellStyle(cntSheet, row, col++, labelGrayStyle);
            }

            // Alarm 2
            // Target Month
            // Shortage Date
            if (stockStatus.getShortageDateTm() != null) {
                styleKey = stockStatus.isHasMoreSupplier() ? DATE_GREEN_RED_FONT : DATE_WHITE_RED_FONT;
                PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(styleKey));
                PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getShortageDateTm());
            } else {
                if (stockStatus.getEndCfcDate() == null
                        || stockStatus.getEndCfcDate().before(stockStatus.getEndDateTm())) {
                    PoiUtil.setCellStyle(cntSheet, row, col, noMasterStyle);
                    PoiUtil.setCellValue(cntSheet, row, col, noCustForecast);
                } else {
                    styleKey = stockStatus.isHasMoreSupplier() ? DATE_GREEN_RED_FONT : DATE_WHITE_RED_FONT;
                    PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(styleKey));
                }
                col++;
            }

            // Min Alarm Flag
            if (stockStatus.getMinAlarmTm() != null) {
                CellStyle alarmStyle = null;
                if (!stockStatus.isHasMoreSupplier()) {
                    alarmStyle = stockStatus.getMinAlarmTm().equals(AlarmFlag.N) ? styleMap.get(LABEL_WHITE_CENTER)
                        : styleMap.get(LABEL_WHITE_RED_CENTER);
                } else {
                    alarmStyle = stockStatus.getMinAlarmTm().equals(AlarmFlag.N) ? styleMap.get(LABEL_GREEN_CENTER)
                        : styleMap.get(LABEL_GREEN_RED_CENTER);
                }
                PoiUtil.setCellStyle(cntSheet, row, col, alarmStyle);
                // get value
                String alarmFlag = CodeCategoryManager.getCodeName(lang.getCode(), CodeMasterCategory.ALARM_FLAG,
                    stockStatus.getMinAlarmTm());
                PoiUtil.setCellValue(cntSheet, row, col++, alarmFlag);
            } else {
                // no customer forecast
                PoiUtil.setCellStyle(cntSheet, row, col, noMasterStyle);
                PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
            }

            // Lowest Stock Days
            // Highest Stock Days
            if (stockStatus.getInventoryBoxFlag().equals(InventoryByBox.Y)) {
                PoiUtil.setCellStyle(cntSheet, row, col++, labelGrayStyle);
            } else {
                // Lowest Stock Days
                // if does not has any stock days?
                if (stockStatus.getMinStockDayTm() != null) {
                    // already exist
                    PoiUtil.setCellStyle(cntSheet, row, col, stockDayStyle);
                    if (stockStatus.getNoMasterMinTm().equals(NoCfcFlag.N)) {
                        PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getMinStockDayTm());
                    } else {
                        StringBuffer stockDays = new StringBuffer(StringConst.GREATER).append(StringConst.BLANK);
                        stockDays.append(StringUtil.formatBigDecimal(IntDef.INT_ONE, stockStatus.getMinStockDayTm()));
                        PoiUtil.setCellValue(cntSheet, row, col++, stockDays.toString());
                    }
                } else {
                    // no customer forecast
                    PoiUtil.setCellStyle(cntSheet, row, col, noMasterStyle);
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                }
            }

            // Lowest No. of Boxes
            // Highest No. of Boxes
            if (stockStatus.getInventoryBoxFlag().equals(InventoryByBox.N)) {
                PoiUtil.setCellStyle(cntSheet, row, col++, labelGrayStyle);
            } else {
                // Lowest No. of Boxes
                // if does not has any stock days?
                if (stockStatus.getMinBoxesTm() != null) {
                    // already exist
                    PoiUtil.setCellStyle(cntSheet, row, col, stockBoxStyle);
                    PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getMinBoxesTm());
                } else {
                    // no customer forecast
                    PoiUtil.setCellStyle(cntSheet, row, col, noMasterStyle);
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                }
            }

            // Alarm 3: To Verify Order Calculation
            if (stockStatus.getEndDate3() != null) {

                // Ending Stock Days (Until Last Day of Target Month)
                if (stockStatus.getStockDay3() != null) {
                    // set style
                    PoiUtil.setCellStyle(cntSheet, row, col, stockDayStyle);
                    // set value
                    StringBuffer stockDays = new StringBuffer();
                    if (stockStatus.getNoMaster3().equals(NoCfcFlag.Y)) {
                        stockDays.append(StringConst.GREATER).append(StringConst.BLANK);
                    }
                    stockDays.append(StringUtil.formatBigDecimal(IntDef.INT_ONE, stockStatus.getStockDay3()));
                    PoiUtil.setCellValue(cntSheet, row, col++, stockDays.toString());
                } else {
                    // check is has no customer forecast?
                    PoiUtil.setCellStyle(cntSheet, row, col, noMasterStyle);
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                }

                // Safety Stock Flag
                if (stockStatus.getSafetyAlarm3() != null) {
                    // get style
                    CellStyle alarmStyle = null;
                    if (!stockStatus.isHasMoreSupplier()) {
                        alarmStyle = stockStatus.getSafetyAlarm3().equals(AlarmFlag.N) ? styleMap
                            .get(LABEL_WHITE_CENTER) : styleMap.get(LABEL_WHITE_RED_CENTER);
                    } else {
                        alarmStyle = stockStatus.getSafetyAlarm3().equals(AlarmFlag.N) ? styleMap
                            .get(LABEL_GREEN_CENTER) : styleMap.get(LABEL_GREEN_RED_CENTER);
                    }
                    PoiUtil.setCellStyle(cntSheet, row, col, alarmStyle);
                    // get value
                    String alarmFlag = CodeCategoryManager.getCodeName(lang.getCode(), CodeMasterCategory.ALARM_FLAG,
                        stockStatus.getSafetyAlarm3());
                    PoiUtil.setCellValue(cntSheet, row, col++, alarmFlag);
                } else {
                    // check is has no order forecast?
                    PoiUtil.setCellStyle(cntSheet, row, col, noMasterStyle);
                    PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
                }

                // Safety Stock Days For Alarm 3
                PoiUtil.setCellStyle(cntSheet, row, col, stockDayStyle);
                PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getOrderSafetyStock());
            } else {
                PoiUtil.setCellStyle(cntSheet, row, col++, labelGrayStyle);
                PoiUtil.setCellStyle(cntSheet, row, col++, labelGrayStyle);
                PoiUtil.setCellStyle(cntSheet, row, col++, labelGrayStyle);
            }

            // Alarm 4: Actual Outbound Fluctuation
            if (stockStatus.getTotalCfcQty4() != null) {

                // Actual Outbound Over Total Monthly Plan and Over Agreed Fluctuation Ratio Date
                styleKey = stockStatus.isHasMoreSupplier() ? DATE_GREEN_RED_FONT : DATE_WHITE_RED_FONT;
                PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(styleKey));
                PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getOverRatioDate4());

                // Comparison of Actual Outbound vs. Monthly Plan
                styleKey = stockStatus.isHasMoreSupplier() ? DECIMAL_PERCENTAGE_GREEN : DECIMAL_PERCENTAGE;
                PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(styleKey));
                PoiUtil.setCellValue(cntSheet, row, col++,
                    DecimalUtil.divide(stockStatus.getFluctuationRatio4(), new BigDecimal(IntDef.INT_HUNDRED)));
            } else {

                // Actual Outbound Over Total Monthly Plan and Over Agreed Fluctuation Ratio Date
                PoiUtil.setCellStyle(cntSheet, row, col, noMasterStyle);
                PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);

                // Comparison of Actual Outbound vs. Monthly Plan
                PoiUtil.setCellStyle(cntSheet, row, col, noMasterStyle);
                PoiUtil.setCellValue(cntSheet, row, col++, noCustForecast);
            }

            // Build Out Data
            // Build-out Flag
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_CENTER));
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getBuildOutFlag());

            // Discontinue Indicator
            PoiUtil.setCellStyle(cntSheet, row, col, styleMap.get(LABEL_WHITE_CENTER));
            PoiUtil.setCellValue(cntSheet, row, col++, stockStatus.getInActiveFlag());

            // reset
            row++;
            col = IntDef.INT_ONE;
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

}