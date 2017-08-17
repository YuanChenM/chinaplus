/**
 * CPKKPF02Controller.java
 * 
 * @screen CPKKPF02
 * @author shiyang
 */
package com.chinaplus.web.kbp.control;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.ImpStockFlag;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.PartSortManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.kbp.entity.CPKKPF02Entity;
import com.chinaplus.web.kbp.service.CPKKPF02Service;

/**
 * Download Kanban Plan for Revision History(doc2) Controller.
 */
@SuppressWarnings("deprecation")
@Controller
public class CPKKPF02Controller extends BaseFileController {

    /** Download zip file name */
    private static final String DOWNLOAD_ZIP_FILE_NAME = "Kanban Plan_doc2_{0}.zip";
    /** Download excel file name */
    private static final String DOWNLOAD_EXCEL_FILE_NAME = "Kanban Plan_{0}_doc2_{1}.xlsx";

    /** ZIP file content type */
    private static final String ZIP_CONTENT_TYPE = "application/x-zip-compressed;charset=UTF-8";

    /** MAP_KEY_INVOICE */
    private static final String MAP_KEY_INVOICE = "MAP_KEY_INVOICE";
    /** MAP_KEY_INVOICE_PARTS */
    private static final String MAP_KEY_INVOICE_PARTS = "MAP_KEY_INVOICE_PARTS";
    /** MAP_KEY_SHIPPING_PLAN_PARTS */
    private static final String MAP_KEY_SHIPPING_PLAN_PARTS = "MAP_KEY_SHIPPING_PLAN_PARTS";
    /** MAP_KEY_SHIPPING_PLAN_HISTORY */
    private static final String MAP_KEY_SHIPPING_PLAN_HISTORY = "MAP_KEY_SHIPPING_PLAN_HISTORY";
    /** MAP_KEY_SHIPPING_PLAN_HISTORY_PARTS */
    private static final String MAP_KEY_SHIPPING_PLAN_HISTORY_PARTS = "MAP_KEY_SHIPPING_PLAN_HISTORY_PARTS";
    /** MAP_KEY_SHIPPING_PLAN_BOX */
    private static final String MAP_KEY_SHIPPING_PLAN_BOX = "MAP_KEY_SHIPPING_PLAN_BOX";
    /** MAP_KEY_SHIPPING_PLAN_BOX_PARTS */
    private static final String MAP_KEY_SHIPPING_PLAN_BOX_PARTS = "MAP_KEY_SHIPPING_PLAN_BOX_PARTS";

    /** OUTPUT_QTY_ITEM_NAME_QTY for output QTY cell */
    private static final String OUTPUT_QTY_ITEM_NAME_QTY = "QTY";
    /** OUTPUT_QTY_ITEM_NAME_KANBAN_QTY for output QTY cell */
    private static final String OUTPUT_QTY_ITEM_NAME_KANBAN_QTY = "KANBAN_QTY";
    /** OUTPUT_QTY_ITEM_NAME_INVOICE_GROUP_TOTAL_QTY for output QTY cell */
    private static final String OUTPUT_QTY_ITEM_NAME_INVOICE_GROUP_TOTAL_QTY = "INVOICE_GROUP_TOTAL_QTY";

    /** base en sheet */
    private static final String SHEET_BASE = "base";
    /** style qty gray sheet */
    private static final String SHEET_STYLE_QTY_GRAY = "style_qty_gray";
    /** style qty white sheet */
    private static final String SHEET_STYLE_QTY_WHITE = "style_qty_white";
    /** style qty red sheet */
    private static final String SHEET_STYLE_QTY_RED = "style_qty_red";
    /** style cell sheet */
    private static final String SHEET_STYLE_CELL = "style_cell";
    /** style parts qty info title sheet */
    private static final String SHEET_STYLE_PARTS_QTY_INFO_TITLE = "style_parts_qty_info_title";
    /** style invoice shipping plan sheet en */
    private static final String SHEET_STYLE_INVOICE_SHIPPING_PLAN_EN = "style_invoice_shipping_plan_en";
    /** style invoice shipping plan sheet cn */
    private static final String SHEET_STYLE_INVOICE_SHIPPING_PLAN_CN = "style_invoice_shipping_plan_cn";
    /** style nird en */
    private static final String SHEET_STYLE_NIRD_EN = "style_nird_en";
    /** style nird cn */
    private static final String SHEET_STYLE_NIRD_CN = "style_nird_cn";

    /** Row start line (19th line - 1) */
    private static final int ROW_START_LINE = 13;

    /** Column start line (11th line - 1) */
    private static final int COLUMN_START_LINE = 10;

    /** INVOICE_SHIPPING_PLAN_ROW_NUMBER */
    private static final int INVOICE_SHIPPING_PLAN_ROW_NUMBER = 13;

    /** STYLE_NIRD_ITEM_NAME_COLUMN for SHEET_STYLE_NIRD */
    private static final int STYLE_NIRD_ITEM_NAME_COLUMN = 0;

    /** STYLE_NIRD_ITEM_VALUE_COLUMN for SHEET_STYLE_NIRD */
    private static final int STYLE_NIRD_ITEM_VALUE_COLUMN = 1;

    /** ROW_NIRD_HEADER for SHEET_STYLE_NIRD */
    private static final int[] ROW_NIRD_HEADER = { 1, 4 };

    /** ROW_NIRD_REVISON_REASON for SHEET_STYLE_NIRD */
    private static final int[] ROW_NIRD_REVISON_REASON = { 10, 12 };

    /** style parts qty info title en columns number for SHEET_STYLE_PARTS_QTY_INFO_TITLE */
    private static final int[] STYLE_PARTS_QTY_INFO_TITLE = { 0, 2, 4, 6, 8, 10 };

    /** STYLE_SEA_INVOICE_GROUP_COLMUN for SHEET_STYLE_INVOICE_SHIPPING_PLAN */
    private static final int STYLE_SEA_INVOICE_GROUP_COLMUN = 0;

    /** STYLE_SEA_INVOICE_PAST_COLMUN for SHEET_STYLE_INVOICE_SHIPPING_PLAN */
    private static final int STYLE_SEA_INVOICE_PAST_COLMUN = 1;

    /** STYLE_SEA_DIFFERENCE_COLMUN for SHEET_STYLE_INVOICE_SHIPPING_PLAN */
    private static final int STYLE_SEA_DIFFERENCE_COLMUN = 2;

    /** STYLE_SEA_BOX_COLMUN for SHEET_STYLE_INVOICE_SHIPPING_PLAN */
    private static final int STYLE_SEA_BOX_COLMUN = 3;

    /** STYLE_SEA_PLAN_COLMUN for SHEET_STYLE_INVOICE_SHIPPING_PLAN */
    private static final int STYLE_SEA_PLAN_COLMUN = 4;

    /** STYLE_AIR_INVOICE_GROUP_COLMUN for SHEET_STYLE_INVOICE_SHIPPING_PLAN */
    private static final int STYLE_AIR_INVOICE_GROUP_COLMUN = 5;

    /** STYLE_AIR_PLAN_COLMUN for SHEET_STYLE_INVOICE_SHIPPING_PLAN */
    private static final int STYLE_AIR_PLAN_COLMUN = 6;

    /** CELL_GRAY_DATA_NORMAL_RIGHT for SHEET_STYLE_CELL */
    private static final int[] CELL_GRAY_DATA_NORMAL_RIGHT = { 7, 1 };

    /** CELL_GRAY_DATA_NORMAL_RIGHT_KEY for SHEET_STYLE_CELL */
    private static final String CELL_GRAY_DATA_NORMAL_RIGHT_KEY = "CELL_GRAY_DATA_NORMAL_RIGHT_KEY";

    /** CELL_WHITE_DATA_NORMAL_LEFT for SHEET_STYLE_CELL */
    private static final int[] CELL_WHITE_DATA_NORMAL_LEFT = { 13, 1 };

    /** CELL_WHITE_DATA_NORMAL_LEFT_KEY for SHEET_STYLE_CELL */
    private static final String CELL_WHITE_DATA_NORMAL_LEFT_KEY = "CELL_WHITE_DATA_NORMAL_LEFT_KEY";

    /** CELL_WHITE_DATA_NORMAL_CENTER for SHEET_STYLE_CELL */
    private static final int[] CELL_WHITE_DATA_NORMAL_CENTER = { 14, 1 };

    /** CELL_WHITE_DATA_NORMAL_CENTER_KEY for SHEET_STYLE_CELL */
    private static final String CELL_WHITE_DATA_NORMAL_CENTER_KEY = "CELL_WHITE_DATA_NORMAL_CENTER_KEY";

    /** CELL_WHITE_DATA_NORMAL_RIGHT for SHEET_STYLE_CELL */
    private static final int[] CELL_WHITE_DATA_NORMAL_RIGHT = { 15, 1 };

    /** CELL_WHITE_DATA_NORMAL_RIGHT_KEY for SHEET_STYLE_CELL */
    private static final String CELL_WHITE_DATA_NORMAL_RIGHT_KEY = "CELL_WHITE_DATA_NORMAL_RIGHT_KEY";

    /** CELL_RED_DATA_NORMAL_RIGHT for SHEET_STYLE_CELL */
    private static final int[] CELL_RED_DATA_NORMAL_RIGHT = { 19, 1 };

    /** CELL_RED_DATA_NORMAL_RIGHT_KEY for SHEET_STYLE_CELL */
    private static final String CELL_RED_DATA_NORMAL_RIGHT_KEY = "CELL_RED_DATA_NORMAL_RIGHT_KEY";

    /** CELL_TODAY for SHEET_STYLE_CELL */
    private static final int[] CELL_TODAY = { 25, 1 };

    /** CELL_DOWNLOAD_TIME for SHEET_STYLE_CELL */
    private static final int[] CELL_DOWNLOAD_TIME = { 31, 1 };

    /** CELL_LAST_ORION_TIME for SHEET_STYLE_CELL */
    private static final int[] CELL_LAST_ORION_TIME = { 32, 1 };

    /** Height of row 1 */
    private static final short HEIGHT = 1000;

    /** Office Time */
    private Date officeTime;

    /** Download Kanban Plan for Revision History(doc2) Service */
    @Autowired
    private CPKKPF02Service cpkkpf02service;

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {
        return FileId.CPKKPF02;
    }

    /**
     * Download check for blank invoice file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return check result
     */
    @RequestMapping(value = "/kbp/CPKKPF02/downloadcheck")
    @ResponseBody
    public BaseResult<String> downloadCheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {

        return new BaseResult<String>();
    }

    /**
     * Download file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the exception
     */
    @RequestMapping(value = "/kbp/CPKKPF02/download")
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {

        OutputStream os = null;
        ZipOutputStream zos = null;
        try {
            super.setCommonParam(param, request);

            response.setContentType(ZIP_CONTENT_TYPE);
            response.setCharacterEncoding(ENCODE);
            response.setHeader(
                "Content-disposition",
                StringUtil.formatMessage("attachment; filename=\"{0}\"",
                    StringUtil.formatMessage(DOWNLOAD_ZIP_FILE_NAME, param.getClientTime())));
            os = response.getOutputStream();
            zos = new ZipOutputStream(os);

            // Generate temporary folder path
            String tempFolderPath = ConfigUtil.get(Properties.TEMPORARY_PATH) + UUID.randomUUID().toString();
            // Create temporary folder
            File tempFolder = new File(tempFolderPath);
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }

            List<String> selected = (List<String>) param.getSelectedDatas();
            for (int i = 0; i < selected.size(); i++) {
                String[] idAndNo = selected.get(i).split(StringConst.NEW_COMMA);
                String kanbanId = idAndNo[0];
                String kanbanPlanNoDisplay = idAndNo[1];
                String fileName = StringUtil.formatMessage(DOWNLOAD_EXCEL_FILE_NAME, kanbanPlanNoDisplay,
                    param.getClientTime());

                param.setSwapData("kanbanId", kanbanId);
                param.setSwapData("kanbanPlanNoDisplay", kanbanPlanNoDisplay);
                super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), new String[] { fileName },
                    new String[] { getFileId() }, param, zos);
            }

            // Delete temporary folder
            tempFolder.delete();
            zos.close();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ex) {
                    // logger.warn("OutputStream close fail.", ex);
                }
            }
            if (zos != null) {
                try {
                    zos.close();
                } catch (Exception ex) {
                    // logger.warn("OutputStream close fail.", ex);
                }
            }
        }
    }

    /**
     * Write content to excel.
     * 
     * @param param page parameter
     * @param wbTemplate the template excel
     * @param wbOutput the output excel
     * @param fileId fileId
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput,
        String fileId) {

        outputMainProcess(param, wbTemplate, wbOutput, null);
    }

    /**
     * Output Main Process.
     * 
     * @param param page parameter
     * @param wbTemplate the template excel
     * @param wbOutput the output excel
     * @param service CPKKPF02Service
     */
    public void outputMainProcess(BaseParam param, Workbook wbTemplate, SXSSFWorkbook wbOutput, CPKKPF02Service service) {

        if (cpkkpf02service == null) {
            cpkkpf02service = service;
        }

        // Office Time
        officeTime = DateTimeUtil.parseDate(DateTimeUtil.formatDate(null,
            cpkkpf02service.getDBDateTime(param.getOfficeTimezone())));

        // Output
        doOutput(param, wbTemplate, wbOutput);

        // Remove style sheet
        removeStyleSheet(wbOutput);

        // Rename sheet name to KanbanPlanNo
        wbOutput.setSheetName(0, (String) param.getSwapData().get("kanbanPlanNoDisplay"));
    }

    /**
     * Init Style Cell.
     * 
     * @param wbTemplate Workbook
     * @return Style cell hashmap
     */
    private HashMap<String, Cell> initStyleCell(Workbook wbTemplate) {
        HashMap<String, Cell> map = new HashMap<String, Cell>();

        Cell cellGrayRight = super.getTemplateCell(SHEET_STYLE_CELL, CELL_GRAY_DATA_NORMAL_RIGHT[0],
            CELL_GRAY_DATA_NORMAL_RIGHT[1], wbTemplate);
        map.put(CELL_GRAY_DATA_NORMAL_RIGHT_KEY, cellGrayRight);

        Cell cellWhiteLeft = super.getTemplateCell(SHEET_STYLE_CELL, CELL_WHITE_DATA_NORMAL_LEFT[0],
            CELL_WHITE_DATA_NORMAL_LEFT[1], wbTemplate);
        map.put(CELL_WHITE_DATA_NORMAL_LEFT_KEY, cellWhiteLeft);

        Cell cellWhiteCenter = super.getTemplateCell(SHEET_STYLE_CELL, CELL_WHITE_DATA_NORMAL_CENTER[0],
            CELL_WHITE_DATA_NORMAL_CENTER[1], wbTemplate);
        map.put(CELL_WHITE_DATA_NORMAL_CENTER_KEY, cellWhiteCenter);

        Cell cellWhiteRight = super.getTemplateCell(SHEET_STYLE_CELL, CELL_WHITE_DATA_NORMAL_RIGHT[0],
            CELL_WHITE_DATA_NORMAL_RIGHT[1], wbTemplate);
        map.put(CELL_WHITE_DATA_NORMAL_RIGHT_KEY, cellWhiteRight);

        Cell cellRedRight = super.getTemplateCell(SHEET_STYLE_CELL, CELL_RED_DATA_NORMAL_RIGHT[0],
            CELL_RED_DATA_NORMAL_RIGHT[1], wbTemplate);
        map.put(CELL_RED_DATA_NORMAL_RIGHT_KEY, cellRedRight);

        return map;
    }

    /**
     * Remove style sheet
     * 
     * @param wbOutput the output excel
     */
    private void removeStyleSheet(SXSSFWorkbook wbOutput) {
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE_QTY_GRAY));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE_QTY_WHITE));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE_QTY_RED));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE_CELL));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE_PARTS_QTY_INFO_TITLE));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE_INVOICE_SHIPPING_PLAN_EN));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE_INVOICE_SHIPPING_PLAN_CN));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE_NIRD_EN));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE_NIRD_CN));
    }

    /**
     * Output.
     * 
     * @param param page parameter
     * @param wbTemplate the template excel
     * @param wbOutput the output excel
     */
    private void doOutput(BaseParam param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {
        Sheet sheet = wbTemplate.getSheet(SHEET_BASE);
        HashMap<String, Cell> styleMap = initStyleCell(wbTemplate);

        int kanbanId = Integer.valueOf((String) param.getSwapData().get("kanbanId"));
        String kanbanPlanNoDisplay = (String) param.getSwapData().get("kanbanPlanNoDisplay");

        CPKKPF02Entity condition = new CPKKPF02Entity();
        condition.setKanbanId(kanbanId);

        // Get Parts Base Information
        List<CPKKPF02Entity> partsBaseInfoList = cpkkpf02service.getPartsBaseInfo(condition);
        HashMap<Integer, CPKKPF02Entity> partsBaseInfo = getPartsBaseInformation(partsBaseInfoList);

        // Get Parts QTY Information
        HashMap<Integer, CPKKPF02Entity> partsQtyInfo = getPartsQtyInformation(partsBaseInfo, condition);

        // Get Invoice Information
        List<CPKKPF02Entity> invoiceGroupSeaList = new ArrayList<CPKKPF02Entity>();
        List<CPKKPF02Entity> invoiceGroupAirList = new ArrayList<CPKKPF02Entity>();
        HashMap<String, List<CPKKPF02Entity>> invoiceMap = new HashMap<String, List<CPKKPF02Entity>>();
        HashMap<String, List<CPKKPF02Entity>> invoicePartsMap = new HashMap<String, List<CPKKPF02Entity>>();
        getInvoiceInformation(condition, invoiceGroupSeaList, invoiceGroupAirList, invoiceMap, invoicePartsMap);

        // Get Shipping Plan Information
        List<CPKKPF02Entity> shippingPlanSeaList = new ArrayList<CPKKPF02Entity>();
        List<CPKKPF02Entity> shippingPlanAirList = new ArrayList<CPKKPF02Entity>();
        List<CPKKPF02Entity> shippingPlanNirdList = new ArrayList<CPKKPF02Entity>();
        HashMap<String, List<CPKKPF02Entity>> shippingPlanPartsMap = new HashMap<String, List<CPKKPF02Entity>>();
        getShippingPlanInformation(condition, shippingPlanSeaList, shippingPlanAirList, shippingPlanNirdList,
            shippingPlanPartsMap);

        // Get Shipping Plan History Information
        HashMap<String, List<CPKKPF02Entity>> shippingPlanHistoryMap = new HashMap<String, List<CPKKPF02Entity>>();
        HashMap<String, List<CPKKPF02Entity>> shippingPlanHistoryPartsMap = new HashMap<String, List<CPKKPF02Entity>>();
        getShippingPlanHistoryInformation(condition, shippingPlanHistoryMap, shippingPlanHistoryPartsMap);

        // Get Parts Box Information
        HashMap<String, List<CPKKPF02Entity>> differenceBoxMap = new HashMap<String, List<CPKKPF02Entity>>();
        HashMap<String, List<CPKKPF02Entity>> differenceBoxPartsMap = new HashMap<String, List<CPKKPF02Entity>>();
        getPartsBoxInformation(condition, differenceBoxMap, differenceBoxPartsMap);

        // Sort Sea List
        sort(invoiceGroupSeaList, shippingPlanSeaList);

        // Sort Air List
        sort(invoiceGroupAirList, shippingPlanAirList);

        // Output Kanban Plan No.
        sheet.getRow(1).getCell(0).setCellValue(kanbanPlanNoDisplay);

        // Output Parts Base Information (Start Row 19, Stop Column K (Column 11).)
        HashMap<Integer, Integer[]> rowNumOfPartsId = new HashMap<Integer, Integer[]>();
        int forecastNumMax = outputPartsBaseInfo(partsBaseInfoList, wbTemplate, sheet, styleMap, rowNumOfPartsId);

        HashMap<String, HashMap<String, List<CPKKPF02Entity>>> colInfo = new HashMap<String, HashMap<String, List<CPKKPF02Entity>>>();
        colInfo.put(MAP_KEY_INVOICE, invoiceMap);
        colInfo.put(MAP_KEY_INVOICE_PARTS, invoicePartsMap);
        colInfo.put(MAP_KEY_SHIPPING_PLAN_PARTS, shippingPlanPartsMap);
        colInfo.put(MAP_KEY_SHIPPING_PLAN_HISTORY, shippingPlanHistoryMap);
        colInfo.put(MAP_KEY_SHIPPING_PLAN_HISTORY_PARTS, shippingPlanHistoryPartsMap);
        colInfo.put(MAP_KEY_SHIPPING_PLAN_BOX, differenceBoxMap);
        colInfo.put(MAP_KEY_SHIPPING_PLAN_BOX_PARTS, differenceBoxPartsMap);
        int lastColumn = COLUMN_START_LINE + 1;

        // Output Invoice and Plan (Sea Part)
        lastColumn = outputInvoiceAndShippingPlan(param, invoiceGroupSeaList, colInfo, wbTemplate, sheet,
            rowNumOfPartsId, new int[] { lastColumn, 0 });

        // Output Invoice and Plan (Air Part)
        lastColumn = outputInvoiceAndShippingPlan(param, invoiceGroupAirList, colInfo, wbTemplate, sheet,
            rowNumOfPartsId, new int[] { lastColumn, 1 });

        // Output Parts QTY Information (Start Row 19, The Last Column)
        lastColumn = outputPartsQtyInfo(partsBaseInfoList, partsQtyInfo, forecastNumMax, wbTemplate, sheet, lastColumn,
            styleMap);
        if (shippingPlanNirdList != null && shippingPlanNirdList.size() > 0) {
            // Output Not In Rundown
            lastColumn = outputNird(param, shippingPlanNirdList, shippingPlanPartsMap, wbTemplate, sheet, lastColumn,
                rowNumOfPartsId, styleMap);
        }

        // Output footer (Downloaded Server Time & Last Orion Data Time)
        outputFooter(wbTemplate, sheet, rowNumOfPartsId.size(), param);
    }

    /**
     * Output Not In Rundown.
     * 
     * @param param page parameter
     * @param shippingPlanNirdList shipping plan nird list
     * @param shippingPlanPartsMap shipping plan parts map
     * @param wbTemplate wbTemplate
     * @param sheet sheet
     * @param startColumn The start column number
     * @param rowNumOfPartsId row number of partsId
     * @param styleMap style map
     * @return The last column number
     */
    private int outputNird(BaseParam param, List<CPKKPF02Entity> shippingPlanNirdList,
        HashMap<String, List<CPKKPF02Entity>> shippingPlanPartsMap, Workbook wbTemplate, Sheet sheet, int startColumn,
        HashMap<Integer, Integer[]> rowNumOfPartsId, HashMap<String, Cell> styleMap) {
        int lastColumn = startColumn + 1;

        if (shippingPlanNirdList != null && shippingPlanNirdList.size() > 0) {
            Sheet sheet_style_nird = wbTemplate.getSheet(SHEET_STYLE_NIRD_EN);
            if (param.getLanguage().intValue() == CodeConst.CategoryLanguage.CHINESE) {
                sheet_style_nird = wbTemplate.getSheet(SHEET_STYLE_NIRD_CN);
            }

            // 1. Output NIRD item name
            super.copyColumnByFrom(sheet_style_nird, sheet, 0, INVOICE_SHIPPING_PLAN_ROW_NUMBER,
                STYLE_NIRD_ITEM_NAME_COLUMN, 1, new int[] { 0, lastColumn }, true);
            sheet.setColumnWidth(lastColumn, NumberConst.IntDef.INT_TWENTY_TWO
                    * NumberConst.IntDef.INT_TWO_HUNDRED_FIFTY_SIX);
            sheet.getRow(1).setHeight(HEIGHT);

            // 2. Output NIRD item name QTY
            for (int i = 0; i < rowNumOfPartsId.size(); i++) {
                Row row = sheet.getRow(i + INVOICE_SHIPPING_PLAN_ROW_NUMBER);
                Cell cell = row.getCell(lastColumn);
                if (cell == null) {
                    cell = row.createCell(lastColumn);
                }
                super.copyCell(sheet_style_nird.getRow(INVOICE_SHIPPING_PLAN_ROW_NUMBER).getCell(0), cell, true);
            }

            sheet.addMergedRegion(new CellRangeAddress(ROW_NIRD_REVISON_REASON[0], ROW_NIRD_REVISON_REASON[1],
                lastColumn, lastColumn));
            lastColumn++;

            // 3. Output NIRD item value
            for (int i = 0; i < shippingPlanNirdList.size(); i++) {
                CPKKPF02Entity entity = shippingPlanNirdList.get(i);

                // 3.1 Output header of NIRD item value
                HashMap<Integer, Cell[]> map = super.copyColumnByFrom(sheet_style_nird, sheet, 0,
                    INVOICE_SHIPPING_PLAN_ROW_NUMBER, STYLE_NIRD_ITEM_VALUE_COLUMN, 1, new int[] { 0, lastColumn },
                    true);

                map.get(STYLE_NIRD_ITEM_VALUE_COLUMN)[NumberConst.IntDef.INT_SIX].setCellValue(CodeCategoryManager
                    .getCodeName(param.getLanguage(), CodeMasterCategory.TRANSPORT_MODE, entity.getTransportMode()));
                map.get(STYLE_NIRD_ITEM_VALUE_COLUMN)[NumberConst.IntDef.INT_SEVEN].setCellValue(entity.getEtd());
                map.get(STYLE_NIRD_ITEM_VALUE_COLUMN)[NumberConst.IntDef.INT_EIGHT].setCellValue(entity.getEta());
                map.get(STYLE_NIRD_ITEM_VALUE_COLUMN)[NumberConst.IntDef.INT_NINE].setCellValue(entity
                    .getImpInbPlanDate());
                map.get(STYLE_NIRD_ITEM_VALUE_COLUMN)[ROW_NIRD_REVISON_REASON[0]].setCellValue(entity
                    .getRevisionReason());

                // 3.2 Output value of NIRD item value
                List<CPKKPF02Entity> qtyList = shippingPlanPartsMap.get(entity.getKanbanShippingId());
                for (int j = 0; j < rowNumOfPartsId.size(); j++) {
                    outputQty(wbTemplate, sheet, lastColumn, rowNumOfPartsId, SHEET_STYLE_QTY_GRAY,
                        CELL_GRAY_DATA_NORMAL_RIGHT, qtyList, OUTPUT_QTY_ITEM_NAME_QTY);
                }

                sheet.addMergedRegion(new CellRangeAddress(ROW_NIRD_REVISON_REASON[0], ROW_NIRD_REVISON_REASON[1],
                    lastColumn, lastColumn));
                lastColumn++;
            }

            if (lastColumn - 1 > startColumn + NumberConst.IntDef.INT_TWO) {
                sheet.addMergedRegion(new CellRangeAddress(ROW_NIRD_HEADER[0], ROW_NIRD_HEADER[1], startColumn
                        + NumberConst.IntDef.INT_TWO, lastColumn - 1));
            } else {
                sheet.addMergedRegion(new CellRangeAddress(ROW_NIRD_HEADER[0], ROW_NIRD_HEADER[1], lastColumn - 1,
                    lastColumn - 1));
            }
        }
        return lastColumn;
    }

    /**
     * Output QTY Common.
     * 
     * @param wbTemplate wbTemplate
     * @param sheet sheet
     * @param outputColumn column of output position
     * @param rowNumOfPartsId row number of partsId
     * @param sheetStyleName sheet style name
     * @param cellStylePosition position of cell for create
     * @param dataList output data list
     * @param outputItemName output item name
     */
    private void outputQty(Workbook wbTemplate, Sheet sheet, int outputColumn,
        HashMap<Integer, Integer[]> rowNumOfPartsId, String sheetStyleName, int[] cellStylePosition,
        List<CPKKPF02Entity> dataList, String outputItemName) {

        Sheet sheet_style_cell = wbTemplate.getSheet(SHEET_STYLE_CELL);

        // Output default value (0)
        for (int i = ROW_START_LINE; i < rowNumOfPartsId.size() + ROW_START_LINE; i++) {
            int digits = rowNumOfPartsId.get(i)[1];
            if (OUTPUT_QTY_ITEM_NAME_KANBAN_QTY.equals(outputItemName)) {
                digits = NumberConst.IntDef.INT_TWO;
            }
            CellStyle styleQty = super.getDecimalStyle(wbTemplate, sheetStyleName, digits);
            Cell cell = super.copyCell(sheet_style_cell, sheet, cellStylePosition[0], cellStylePosition[1], i,
                outputColumn, false);
            cell.setCellStyle(styleQty);
            if (dataList != null) {
                cell.setCellValue(0);
            }
        }

        // Output value of parts
        if (dataList != null) {
            for (int i = ROW_START_LINE; i < rowNumOfPartsId.size() + ROW_START_LINE; i++) {
                for (int j = 0; j < dataList.size(); j++) {
                    if (dataList.get(j) != null && dataList.get(j).getPartsId() != null
                            && rowNumOfPartsId.get(i)[0].intValue() == dataList.get(j).getPartsId().intValue()) {
                        BigDecimal outputValue = BigDecimal.ZERO;
                        if (OUTPUT_QTY_ITEM_NAME_QTY.equals(outputItemName)) {
                            outputValue = dataList.get(j).getQty();
                        } else if (OUTPUT_QTY_ITEM_NAME_KANBAN_QTY.equals(outputItemName)) {
                            outputValue = dataList.get(j).getKanbanQty();
                        } else if (OUTPUT_QTY_ITEM_NAME_INVOICE_GROUP_TOTAL_QTY.equals(outputItemName)) {
                            outputValue = dataList.get(j).getInvoiceGroupTotalQty();
                        }
                        sheet.getRow(i).getCell(outputColumn).setCellValue(outputValue.doubleValue());
                        break;
                    }
                }
            }
        }
    }

    /**
     * Output Invoice and Shipping Plan.
     * 
     * @param param page parameter
     * @param list List (Invoice Group + Shipping Plan)
     * @param colInfo Invoice Infomation + Shipping Plan Information
     * @param wbTemplate wbTemplate
     * @param sheet sheet
     * @param rowNumOfPartsId row number of partsId
     * @param startColumn [0]:The last column number, [1]:0:Sea/1:Air
     * @return The last column number
     */
    private int outputInvoiceAndShippingPlan(BaseParam param, List<CPKKPF02Entity> list,
        HashMap<String, HashMap<String, List<CPKKPF02Entity>>> colInfo, Workbook wbTemplate, Sheet sheet,
        HashMap<Integer, Integer[]> rowNumOfPartsId, int[] startColumn) {
        int lastColumn = startColumn[0];
        int outputTodayColumn = 0;
        int outputInvoiceGroupOrShippingPlanColumn = lastColumn;

        Sheet sheet_style_invoice_shipping_plan = wbTemplate.getSheet(SHEET_STYLE_INVOICE_SHIPPING_PLAN_EN);
        if (param.getLanguage().intValue() == CodeConst.CategoryLanguage.CHINESE) {
            sheet_style_invoice_shipping_plan = wbTemplate.getSheet(SHEET_STYLE_INVOICE_SHIPPING_PLAN_CN);
        }

        int styleInvoiceGroupColumn = STYLE_SEA_INVOICE_GROUP_COLMUN;
        int styleShippingPlanColumn = STYLE_SEA_PLAN_COLMUN;
        String title = CodeCategoryManager.getCodeName(param.getLanguage().intValue(),
            CodeMasterCategory.TRANSPORT_MODE, CodeConst.TransportMode.SEA);
        if (startColumn[1] == 1) {
            styleInvoiceGroupColumn = STYLE_AIR_INVOICE_GROUP_COLMUN;
            styleShippingPlanColumn = STYLE_AIR_PLAN_COLMUN;
            title = CodeCategoryManager.getCodeName(param.getLanguage().intValue(), CodeMasterCategory.TRANSPORT_MODE,
                CodeConst.TransportMode.AIR);
        }

        // int invoiceGroupRevision = 1;
        int invoiceRevision = 1;

        HashMap<String, List<CPKKPF02Entity>> invoiceMap = colInfo.get(MAP_KEY_INVOICE);
        HashMap<String, List<CPKKPF02Entity>> invoicePartsMap = colInfo.get(MAP_KEY_INVOICE_PARTS);
        HashMap<String, List<CPKKPF02Entity>> shippingPlanPartsMap = colInfo.get(MAP_KEY_SHIPPING_PLAN_PARTS);
        HashMap<String, List<CPKKPF02Entity>> shippingPlanHistoryMap = colInfo.get(MAP_KEY_SHIPPING_PLAN_HISTORY);
        HashMap<String, List<CPKKPF02Entity>> shippingPlanHistoryPartsMap = colInfo
            .get(MAP_KEY_SHIPPING_PLAN_HISTORY_PARTS);
        HashMap<String, List<CPKKPF02Entity>> differenceBoxMap = colInfo.get(MAP_KEY_SHIPPING_PLAN_BOX);
        HashMap<String, List<CPKKPF02Entity>> differenceBoPartsxMap = colInfo.get(MAP_KEY_SHIPPING_PLAN_BOX_PARTS);

        // Output information
        for (int i = 0; i < list.size(); i++) {
            CPKKPF02Entity entity = list.get(i);

            if (!StringUtils.isBlank(entity.getInvoiceGroupId())) {
                invoiceRevision = 1;
                List<CPKKPF02Entity> invoicePartsListTemp = new ArrayList<CPKKPF02Entity>();

                // 1. Output invoice group information (Column Part)
                // 1.1 Output cell
                HashMap<Integer, Cell[]> map = super.copyColumnByFrom(sheet_style_invoice_shipping_plan, sheet, 0,
                    INVOICE_SHIPPING_PLAN_ROW_NUMBER, styleInvoiceGroupColumn, 1, new int[] { 0, lastColumn }, false);
                int invoiceGroupColumn = lastColumn;
                outputInvoiceGroupOrShippingPlanColumn = invoiceGroupColumn;
                lastColumn++;

                // 1.2 Output value
                map.get(styleInvoiceGroupColumn)[NumberConst.IntDef.INT_ONE].setCellValue(title);
                map.get(styleInvoiceGroupColumn)[NumberConst.IntDef.INT_TWO].setCellValue(CodeCategoryManager
                    .getCodeName(param.getLanguage().intValue(), CodeMasterCategory.TRANSPORT_MODE,
                        entity.getTransportMode()));
                map.get(styleInvoiceGroupColumn)[NumberConst.IntDef.INT_SEVEN].setCellValue(entity.getEtd());
                map.get(styleInvoiceGroupColumn)[NumberConst.IntDef.INT_EIGHT].setCellValue(entity.getEta());
                map.get(styleInvoiceGroupColumn)[NumberConst.IntDef.INT_NINE].setCellValue(entity.getImpInbPlanDate());
                map.get(styleInvoiceGroupColumn)[NumberConst.IntDef.INT_TWELVE].setCellValue(MessageManager
                    .getMessage("CPKKPF02_Grid_Actual") + entity.getOriginalVersion());

                HashMap<String, String> impInbActualDateMap = new HashMap<String, String>();
                boolean noBlankImpInbActualDate = true;
                if (startColumn[1] == 0) {
                    // Output invoice information of Sea

                    // 3. Output invoice information (Column Part)
                    List<CPKKPF02Entity> invoiceList = invoiceMap.get(entity.getInvoiceGroupId());
                    for (int j = 0; j < invoiceList.size(); j++) {
                        CPKKPF02Entity entityInvoice = invoiceList.get(j);
                        HashMap<Integer, Cell[]> mapInvoice = new HashMap<Integer, Cell[]>();

                        // 3.1 Output cell
                        mapInvoice = super.copyColumnByFrom(sheet_style_invoice_shipping_plan, sheet, 0,
                            INVOICE_SHIPPING_PLAN_ROW_NUMBER, STYLE_SEA_INVOICE_PAST_COLMUN, 1, new int[] { 0,
                                lastColumn }, false);
                        lastColumn++;

                        // Add invocie Actual Inbound for output into invoice group Actual Inbound
                        if (entityInvoice.getImpInbActualDateInvoice() != null) {
                            String impInbActualDate = DateTimeUtil.getDisDate(entityInvoice
                                .getImpInbActualDateInvoice());
                            impInbActualDateMap.put(impInbActualDate, impInbActualDate);

                            mapInvoice.get(STYLE_SEA_INVOICE_PAST_COLMUN)[NumberConst.IntDef.INT_TEN]
                                .setCellValue(entityInvoice.getImpInbActualDateInvoice());
                        } else {
                            noBlankImpInbActualDate = noBlankImpInbActualDate && false;
                        }

                        // 3.2 Output value
                        mapInvoice.get(STYLE_SEA_INVOICE_PAST_COLMUN)[NumberConst.IntDef.INT_ONE]
                            .setCellValue(CodeCategoryManager.getCodeName(param.getLanguage().intValue(),
                                CodeMasterCategory.TRANSPORT_MODE, CodeConst.TransportMode.SEA));
                        Timestamp vanningDate = entityInvoice.getVanningDate();
                        if (vanningDate != null) {
                            mapInvoice.get(STYLE_SEA_INVOICE_PAST_COLMUN)[NumberConst.IntDef.INT_FIVE]
                                .setCellValue(vanningDate);
                        }
                        mapInvoice.get(STYLE_SEA_INVOICE_PAST_COLMUN)[NumberConst.IntDef.INT_SIX]
                            .setCellValue(entityInvoice.getInvoiceNo());
                        mapInvoice.get(STYLE_SEA_INVOICE_PAST_COLMUN)[NumberConst.IntDef.INT_SEVEN]
                            .setCellValue(entityInvoice.getEtdInvoice());
                        mapInvoice.get(STYLE_SEA_INVOICE_PAST_COLMUN)[NumberConst.IntDef.INT_EIGHT]
                            .setCellValue(entityInvoice.getEtaInvoice());
                        mapInvoice.get(STYLE_SEA_INVOICE_PAST_COLMUN)[NumberConst.IntDef.INT_NINE]
                            .setCellValue(entityInvoice.getImpInbPlanDateInvoice());
                        mapInvoice.get(STYLE_SEA_INVOICE_PAST_COLMUN)[NumberConst.IntDef.INT_ELEVEN]
                            .setCellValue(entityInvoice.getRevisionReason());
                        mapInvoice.get(STYLE_SEA_INVOICE_PAST_COLMUN)[NumberConst.IntDef.INT_TWELVE]
                            .setCellValue(MessageManager.getMessage("CPKKPF02_Grid_Actual")
                                    + entity.getOriginalVersion() + StringConst.UNDERLINE
                                    + MessageManager.getMessage("CPKKPF02_Grid_ActualVan") + invoiceRevision);
                        invoiceRevision++;

                        // 4. Output invoice information (Row Part - QTY)
                        List<CPKKPF02Entity> invoicePartsList = invoicePartsMap.get(entityInvoice.getInvoiceGroupId()
                                + StringConst.SEMICOLON + entityInvoice.getInvoiceNo() + StringConst.SEMICOLON
                                + entityInvoice.getRevisionVersion());
                        outputQty(wbTemplate, sheet, lastColumn - 1, rowNumOfPartsId, SHEET_STYLE_QTY_WHITE,
                            CELL_WHITE_DATA_NORMAL_RIGHT, invoicePartsList, OUTPUT_QTY_ITEM_NAME_QTY);

                        invoicePartsListTemp.addAll(invoicePartsList);
                    }
                } else {
                    map.get(styleInvoiceGroupColumn)[NumberConst.IntDef.INT_SIX].setCellValue(entity.getInvoiceNo());

                    // Output invoice information of Air
                    List<CPKKPF02Entity> invoiceList = invoiceMap.get(entity.getInvoiceGroupId());
                    for (int j = 0; j < invoiceList.size(); j++) {
                        CPKKPF02Entity entityInvoice = invoiceList.get(j);
                        if (entityInvoice.getImpInbActualDateInvoice() != null) {

                            // Add invocie Actual Inbound for output into invoice group Actual Inbound
                            String impInbActualDate = DateTimeUtil.getDisDate(entityInvoice
                                .getImpInbActualDateInvoice());
                            impInbActualDateMap.put(impInbActualDate, impInbActualDate);
                        } else {
                            noBlankImpInbActualDate = noBlankImpInbActualDate && false;
                        }

                        invoicePartsListTemp.addAll(invoicePartsMap.get(entityInvoice.getInvoiceGroupId()
                                + StringConst.SEMICOLON + entityInvoice.getInvoiceNo() + StringConst.SEMICOLON
                                + entityInvoice.getRevisionVersion()));
                        // add for UAT
                        map.get(styleInvoiceGroupColumn)[NumberConst.IntDef.INT_ELEVEN].setCellValue(entityInvoice
                            .getRevisionReason());
                        // add end
                    }
                }

                // 2. Output invoice group information (Row Part - QTY)
                outputQty(wbTemplate, sheet, invoiceGroupColumn, rowNumOfPartsId, SHEET_STYLE_QTY_WHITE,
                    CELL_WHITE_DATA_NORMAL_RIGHT, invoicePartsListTemp, OUTPUT_QTY_ITEM_NAME_INVOICE_GROUP_TOTAL_QTY);

                // 1.2 Output value (Output invoice group Actual Inbound)
                // (Cumulative display Actual Detail's Actual Inbound Format:10 Jun 2015,11 Jun2015,…)
                if (noBlankImpInbActualDate) {
                    StringBuffer sb = new StringBuffer();
                    Iterator<Entry<String, String>> iter = impInbActualDateMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Entry<String, String> e = (Entry<String, String>) iter.next();
                        if (sb.length() > 0) {
                            sb.append(StringConst.NEWLINE);
                        }
                        sb.append(e.getValue());
                    }
                    map.get(styleInvoiceGroupColumn)[NumberConst.IntDef.INT_TEN].setCellValue(sb.toString());
                }

                // invoiceGroupRevision++;
            } else if (!StringUtils.isBlank(entity.getShippingUuid())) {
                if (startColumn[1] == 0) {
                    List<CPKKPF02Entity> differenceBoxList = differenceBoxMap.get(entity.getShippingUuid());

                    // 1. Output Shipping Plan Difference - Box information (Column Part)
                    if (differenceBoxList != null) {
                        for (int j = 0; j < differenceBoxList.size(); j++) {

                            CPKKPF02Entity entityDifferenceBox = differenceBoxList.get(j);
                            String kanbanPlanId = entityDifferenceBox.getKanbanPlanId();

                            boolean keyBreakFlag = false;
                            if (j == differenceBoxList.size() - 1) {
                                keyBreakFlag = true;
                            } else {
                                CPKKPF02Entity entityDifferenceBoxNext = differenceBoxList.get(j + 1);
                                String kanbanPlanIdNext = entityDifferenceBoxNext.getKanbanPlanId();

                                if (!kanbanPlanId.equals(kanbanPlanIdNext)) {
                                    keyBreakFlag = true;
                                } else {
                                    continue;
                                }
                            }

                            boolean isAllDiffZero = true;
                            if (keyBreakFlag) {
                                // 1.1 Output cell
                                if (entityDifferenceBox.getPlanType().equals(CodeConst.PlanType.DIFFERENCE)) {

                                    // If all row qty of difference is 0, do not output
                                    List<CPKKPF02Entity> diffRow = differenceBoPartsxMap.get(entity.getShippingUuid()
                                            + StringConst.SEMICOLON + entityDifferenceBox.getKanbanPlanId());
                                    for (CPKKPF02Entity diffRowData : diffRow) {
                                        if (diffRowData.getKanbanQty().compareTo(BigDecimal.ZERO) != 0) {
                                            isAllDiffZero = false;
                                            break;
                                        }
                                    }

                                    if (!isAllDiffZero) {
                                        HashMap<Integer, Cell[]> map = super.copyColumnByFrom(
                                            sheet_style_invoice_shipping_plan, sheet, 0,
                                            INVOICE_SHIPPING_PLAN_ROW_NUMBER, STYLE_SEA_DIFFERENCE_COLMUN, 1,
                                            new int[] { 0, lastColumn }, false);
                                        lastColumn++;

                                        // 1.2 Output value
                                        map.get(STYLE_SEA_DIFFERENCE_COLMUN)[NumberConst.IntDef.INT_ONE]
                                            .setCellValue(title);
                                        map.get(STYLE_SEA_DIFFERENCE_COLMUN)[NumberConst.IntDef.INT_ELEVEN]
                                            .setCellValue(entityDifferenceBox.getRevisionReason());
                                        map.get(STYLE_SEA_DIFFERENCE_COLMUN)[NumberConst.IntDef.INT_TWELVE]
                                            .setCellValue(MessageManager.getMessage("CPKKPF02_Grid_Difference"));
                                    }

                                } else if (entityDifferenceBox.getPlanType().equals(CodeConst.PlanType.BOX)) {
                                    isAllDiffZero = false;

                                    HashMap<Integer, Cell[]> map = super.copyColumnByFrom(
                                        sheet_style_invoice_shipping_plan, sheet, 0, INVOICE_SHIPPING_PLAN_ROW_NUMBER,
                                        STYLE_SEA_BOX_COLMUN, 1, new int[] { 0, lastColumn }, false);
                                    lastColumn++;

                                    // 1.2 Output value
                                    map.get(STYLE_SEA_BOX_COLMUN)[NumberConst.IntDef.INT_ONE].setCellValue(title);
                                    map.get(STYLE_SEA_BOX_COLMUN)[NumberConst.IntDef.INT_THREE]
                                        .setCellValue(entityDifferenceBox.getIssuedDate());
                                    map.get(STYLE_SEA_BOX_COLMUN)[NumberConst.IntDef.INT_FOUR]
                                        .setCellValue(entityDifferenceBox.getDeliveredDate());
                                    Timestamp vanningDate = entityDifferenceBox.getVanningDate();
                                    if (vanningDate != null) {
                                        map.get(STYLE_SEA_BOX_COLMUN)[NumberConst.IntDef.INT_FIVE]
                                            .setCellValue(vanningDate);
                                    }
                                    map.get(STYLE_SEA_BOX_COLMUN)[NumberConst.IntDef.INT_ELEVEN]
                                        .setCellValue(entityDifferenceBox.getRevisionReason());
                                    map.get(STYLE_SEA_BOX_COLMUN)[NumberConst.IntDef.INT_TWELVE]
                                        .setCellValue(MessageManager.getMessage("CPKKPF02_Grid_Box"));
                                }

                                if (!isAllDiffZero) {
                                    // 2. Output Shipping Plan Difference - Box Parts information (Row Part)
                                    outputQty(
                                        wbTemplate,
                                        sheet,
                                        lastColumn - 1,
                                        rowNumOfPartsId,
                                        SHEET_STYLE_QTY_WHITE,
                                        CELL_WHITE_DATA_NORMAL_RIGHT,
                                        differenceBoPartsxMap.get(entity.getShippingUuid() + StringConst.SEMICOLON
                                                + entityDifferenceBox.getKanbanPlanId()),
                                        OUTPUT_QTY_ITEM_NAME_KANBAN_QTY);
                                }
                            }
                        }
                    }
                }

                // 3. Output Shipping Plan History information (Column Part)
                List<CPKKPF02Entity> shippingPlanHistoryList = shippingPlanHistoryMap.get(entity.getKanbanId()
                        + StringConst.SEMICOLON + entity.getOriginalVersion() + StringConst.SEMICOLON
                        + entity.getTransportMode());
                if (shippingPlanHistoryList != null) {
                    for (int j = 0; j < shippingPlanHistoryList.size(); j++) {
                        CPKKPF02Entity entityHistory = shippingPlanHistoryList.get(j);

                        if (entity.getKanbanId().equals(entityHistory.getKanbanId())
                                && entity.getOriginalVersion().equals(entityHistory.getOriginalVersion())
                                && entity.getRevisionVersion().equals(entityHistory.getRevisionVersion())) {
                            continue;
                        }

                        String versionHistory = MessageManager.getMessage("CPKKPF02_Grid_Plan")
                                + entityHistory.getOriginalVersion();
                        if (!StringUtils.isBlank(entityHistory.getRevisionVersion())
                                && !"0".equals(entityHistory.getRevisionVersion())) {
                            versionHistory = versionHistory + StringConst.UNDERLINE
                                    + MessageManager.getMessage("CPKKPF02_Grid_PlanRevision")
                                    + entityHistory.getRevisionVersion();
                        }

                        // 5.1 Output cell
                        HashMap<Integer, Cell[]> mapHistory = super.copyColumnByFrom(sheet_style_invoice_shipping_plan,
                            sheet, 0, INVOICE_SHIPPING_PLAN_ROW_NUMBER, styleShippingPlanColumn, 1, new int[] { 0,
                                lastColumn }, false);
                        lastColumn++;

                        // 5.2 Output value
                        mapHistory.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_ONE].setCellValue(title);
                        mapHistory.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_TWO]
                            .setCellValue(CodeCategoryManager.getCodeName(param.getLanguage().intValue(),
                                CodeMasterCategory.TRANSPORT_MODE, entityHistory.getTransportMode()));
                        // mapHistory.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_THREE].setCellValue(entity.getIssueRemark());
                        // mapHistory.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_FOUR].setCellValue(entity.getDelivereRemark());
                        // mapHistory.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_FIVE].setCellValue(entity.getVanningRemark());
                        mapHistory.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_SEVEN]
                            .setCellValue(entityHistory.getEtd());
                        mapHistory.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_EIGHT]
                            .setCellValue(entityHistory.getEta());
                        mapHistory.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_NINE].setCellValue(entityHistory
                            .getImpInbPlanDate());
                        mapHistory.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_ELEVEN]
                            .setCellValue(entityHistory.getRevisionReason());
                        mapHistory.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_TWELVE]
                            .setCellValue(versionHistory);

                        // 6. Output Shipping Plan History Parts information (Row Part)
                        List<CPKKPF02Entity> shippingPlanHistoryPartsList = shippingPlanHistoryPartsMap
                            .get(entityHistory.getKanbanId() + StringConst.SEMICOLON
                                    + entityHistory.getOriginalVersion() + StringConst.SEMICOLON
                                    + entityHistory.getTransportMode() + StringConst.SEMICOLON
                                    + entityHistory.getRevisionVersion());
                        outputQty(wbTemplate, sheet, lastColumn - 1, rowNumOfPartsId, SHEET_STYLE_QTY_WHITE,
                            CELL_WHITE_DATA_NORMAL_RIGHT, shippingPlanHistoryPartsList, OUTPUT_QTY_ITEM_NAME_QTY);
                    }
                }

                // 4. Output Shipping Plan information (Column Part)
                // 4.1 Output cell
                HashMap<Integer, Cell[]> map = super.copyColumnByFrom(sheet_style_invoice_shipping_plan, sheet, 0,
                    INVOICE_SHIPPING_PLAN_ROW_NUMBER, styleShippingPlanColumn, 1, new int[] { 0, lastColumn }, false);
                outputInvoiceGroupOrShippingPlanColumn = lastColumn;
                lastColumn++;

                // 4.2 Output value
                String version = MessageManager.getMessage("CPKKPF02_Grid_Plan") + entity.getOriginalVersion();
                if (!StringUtils.isBlank(entity.getRevisionVersion()) && !"0".equals(entity.getRevisionVersion())) {
                    version = version + StringConst.UNDERLINE + MessageManager.getMessage("CPKKPF02_Grid_PlanRevision")
                            + entity.getRevisionVersion();
                }

                map.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_ONE].setCellValue(title);
                map.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_TWO].setCellValue(CodeCategoryManager
                    .getCodeName(param.getLanguage().intValue(), CodeMasterCategory.TRANSPORT_MODE,
                        entity.getTransportMode()));
                map.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_THREE].setCellValue(entity.getIssueRemark());
                map.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_FOUR].setCellValue(entity.getDelivereRemark());
                map.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_FIVE].setCellValue(entity.getVanningRemark());
                map.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_SEVEN].setCellValue(entity.getEtd());
                map.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_EIGHT].setCellValue(entity.getEta());
                map.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_NINE].setCellValue(entity.getImpInbPlanDate());
                map.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_ELEVEN]
                    .setCellValue(entity.getRevisionReason());
                map.get(styleShippingPlanColumn)[NumberConst.IntDef.INT_TWELVE].setCellValue(version);

                // 5. Output Shipping Plan Parts information (Row Part)
                List<CPKKPF02Entity> shippingPlanPartsList = shippingPlanPartsMap.get(entity.getKanbanShippingId());
                outputQty(wbTemplate, sheet, lastColumn - 1, rowNumOfPartsId, SHEET_STYLE_QTY_WHITE,
                    CELL_WHITE_DATA_NORMAL_RIGHT, shippingPlanPartsList, OUTPUT_QTY_ITEM_NAME_QTY);
            }

            // Output today flag (yollow column)
            if (outputTodayColumn == 0 && list.get(i).getEtd().compareTo(officeTime) < 0) {
                if (i == list.size() - 1) {
                    super.copyCell(super.getTemplateCell(SHEET_STYLE_CELL, CELL_TODAY[0], CELL_TODAY[1], wbTemplate),
                        sheet.getRow(0).getCell(outputInvoiceGroupOrShippingPlanColumn), false);

                    Row row = sheet.getRow(rowNumOfPartsId.size() + INVOICE_SHIPPING_PLAN_ROW_NUMBER);
                    if (row == null) {
                        row = sheet.createRow(rowNumOfPartsId.size() + INVOICE_SHIPPING_PLAN_ROW_NUMBER);
                    }
                    Cell cell = row.getCell(outputInvoiceGroupOrShippingPlanColumn);
                    if (cell == null) {
                        cell = row.createCell(outputInvoiceGroupOrShippingPlanColumn);
                    }
                    super.copyCell(super.getTemplateCell(SHEET_STYLE_CELL, CELL_TODAY[0], CELL_TODAY[1], wbTemplate),
                        cell, false);

                    outputTodayColumn = outputInvoiceGroupOrShippingPlanColumn;
                } else {
                    if (list.get(i + 1).getEtd().compareTo(officeTime) >= 0) {
                        super.copyCell(super
                            .getTemplateCell(SHEET_STYLE_CELL, CELL_TODAY[0], CELL_TODAY[1], wbTemplate),
                            sheet.getRow(0).getCell(outputInvoiceGroupOrShippingPlanColumn), false);

                        Row row = sheet.getRow(rowNumOfPartsId.size() + INVOICE_SHIPPING_PLAN_ROW_NUMBER);
                        if (row == null) {
                            row = sheet.createRow(rowNumOfPartsId.size() + INVOICE_SHIPPING_PLAN_ROW_NUMBER);
                        }
                        Cell cell = row.getCell(outputInvoiceGroupOrShippingPlanColumn);
                        if (cell == null) {
                            cell = row.createCell(outputInvoiceGroupOrShippingPlanColumn);
                        }
                        super.copyCell(
                            super.getTemplateCell(SHEET_STYLE_CELL, CELL_TODAY[0], CELL_TODAY[1], wbTemplate), cell,
                            false);

                        outputTodayColumn = outputInvoiceGroupOrShippingPlanColumn;
                    }
                }
            }
        }
        if (startColumn[0] < lastColumn - 1) {
            sheet.addMergedRegion(new CellRangeAddress(1, 1, startColumn[0], lastColumn - 1));
        }
        return lastColumn;
    }

    /**
     * Output footer (Downloaded Server Time & Last Orion Data Time)
     * 
     * @param wbTemplate the template excel
     * @param sheet output sheet
     * @param detailSize detail size
     * @param param BaseParam
     */
    private void outputFooter(Workbook wbTemplate, Sheet sheet, int detailSize, BaseParam param) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMM);
            String downloadTime = cpkkpf02service.getDBDateTime(param.getOfficeTimezone()).toString();
            Timestamp tsLastOrionTime = cpkkpf02service.getSyncTimeList(param);
            String officeCode = param.getCurrentOfficeCode();

            String lastOrionTime = "";
            if (tsLastOrionTime != null) {
                lastOrionTime = tsLastOrionTime.toString();
                Date dLastOrionTime = sdf.parse(lastOrionTime);
                if (param.getLanguage().intValue() == CodeConst.CategoryLanguage.CHINESE) {
                    lastOrionTime = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMM, dLastOrionTime);
                } else if (param.getLanguage().intValue() == CodeConst.CategoryLanguage.ENGLISH) {
                    lastOrionTime = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYYHHMM, dLastOrionTime);
                }
            }

            Date dDownloadTime = sdf.parse(downloadTime);
            if (param.getLanguage().intValue() == CodeConst.CategoryLanguage.CHINESE) {
                downloadTime = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMM, dDownloadTime);
            } else if (param.getLanguage().intValue() == CodeConst.CategoryLanguage.ENGLISH) {
                downloadTime = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYYHHMM, dDownloadTime);
            }

            Sheet sheet_style_cell = wbTemplate.getSheet(SHEET_STYLE_CELL);
            int lastRowNo = ROW_START_LINE + detailSize;
            Cell cell = super.copyCell(sheet_style_cell, sheet, CELL_DOWNLOAD_TIME[0], CELL_DOWNLOAD_TIME[1],
                lastRowNo + 1, 0, false);
            cell.setCellValue(MessageFormat.format(MessageManager.getMessage("CPKKPF02_Label_DownloadTime"),
                new Object[] { downloadTime, officeCode }));
            cell = super.copyCell(sheet_style_cell, sheet, CELL_LAST_ORION_TIME[0], CELL_LAST_ORION_TIME[1],
                sheet.getLastRowNum() + 1, 0, false);
            cell.setCellValue(MessageFormat.format(MessageManager.getMessage("CPKKPF02_Label_LastInterfaceTime"),
                new Object[] { officeCode, lastOrionTime }));
        } catch (Exception e) {}
    }

    /**
     * Output Parts Qty Info.
     * 
     * @param partsBaseInfoList Parts Base Information List
     * @param partsQtyInfo Parts Qty Information
     * @param forecastNumMax the max forecastNum
     * @param wbTemplate the template excel
     * @param sheet output sheet
     * @param lastColumn The last column number
     * @param styleMap Style Map
     * @return The last column number
     */
    private int outputPartsQtyInfo(List<CPKKPF02Entity> partsBaseInfoList,
        HashMap<Integer, CPKKPF02Entity> partsQtyInfo, int forecastNumMax, Workbook wbTemplate, Sheet sheet,
        int lastColumn, HashMap<String, Cell> styleMap) {
        for (int i = 0; i < partsBaseInfoList.size(); i++) {
            CPKKPF02Entity partsBaseInfoEntity = partsBaseInfoList.get(i);

            // PartsId
            int PartsId = partsBaseInfoEntity.getPartsId();

            // Get parts qty information of this parts with PartsId
            CPKKPF02Entity partsQtyInfoEntity = partsQtyInfo.get(PartsId);

            // The forecast num of this parts
            int forecastNum = partsBaseInfoEntity.getForecastNum();

            // Output data
            Object[] partsQtyInfoData = new Object[NumberConst.IntDef.INT_EIGHT + forecastNumMax];
            partsQtyInfoData[NumberConst.IntDef.INT_ZERO] = partsQtyInfoEntity.getQtyWithInvoice();
            partsQtyInfoData[NumberConst.IntDef.INT_ONE] = partsQtyInfoEntity.getOrderBalanceBasedOnActualEtd();
            partsQtyInfoData[NumberConst.IntDef.INT_TWO] = partsQtyInfoEntity.getActualInboundQty();
            partsQtyInfoData[NumberConst.IntDef.INT_THREE] = partsQtyInfoEntity.getOrderBalanceBasedOnActualInbound();
            // 2016/07/14 shiyang mod start
            // partsQtyInfoData[NumberConst.IntDef.INT_FOUR] = partsQtyInfoEntity.getForceCompletedQty();
            BigDecimal forceCompletedQty = partsQtyInfoEntity.getForceCompletedQty();
            if (forceCompletedQty == null || forceCompletedQty.compareTo(BigDecimal.ZERO) == 0) {
                forceCompletedQty = BigDecimal.ZERO;
            } else {
                BigDecimal qtyWithInvoice = partsQtyInfoEntity.getQtyWithInvoice();
                BigDecimal forceCompletedQtyActual = DecimalUtil.subtract(
                    DecimalUtil.add(qtyWithInvoice, forceCompletedQty), partsBaseInfoEntity.getQty());
                if (forceCompletedQtyActual.compareTo(forceCompletedQty) >= 0) {
                    forceCompletedQty = BigDecimal.ZERO;
                } else {
                    forceCompletedQty = DecimalUtil.subtract(forceCompletedQty, forceCompletedQtyActual);
                }
            }
            partsQtyInfoData[NumberConst.IntDef.INT_FOUR] = forceCompletedQty;
            // 2016/07/14 shiyang mod end
            for (int j = 1; j <= forecastNumMax; j++) {
                if (j <= forecastNum) {
                    switch (j) {
                        case NumberConst.IntDef.INT_ONE:
                            partsQtyInfoData[NumberConst.IntDef.INT_FOUR + j] = partsBaseInfoEntity.getFcQty1();
                            break;
                        case NumberConst.IntDef.INT_TWO:
                            partsQtyInfoData[NumberConst.IntDef.INT_FOUR + j] = partsBaseInfoEntity.getFcQty2();
                            break;
                        case NumberConst.IntDef.INT_THREE:
                            partsQtyInfoData[NumberConst.IntDef.INT_FOUR + j] = partsBaseInfoEntity.getFcQty3();
                            break;
                        case NumberConst.IntDef.INT_FOUR:
                            partsQtyInfoData[NumberConst.IntDef.INT_FOUR + j] = partsBaseInfoEntity.getFcQty4();
                            break;
                        case NumberConst.IntDef.INT_FIVE:
                            partsQtyInfoData[NumberConst.IntDef.INT_FOUR + j] = partsBaseInfoEntity.getFcQty5();
                            break;
                        case NumberConst.IntDef.INT_SIX:
                            partsQtyInfoData[NumberConst.IntDef.INT_FOUR + j] = partsBaseInfoEntity.getFcQty6();
                            break;
                    }
                } else {
                    partsQtyInfoData[NumberConst.IntDef.INT_FOUR + j] = "";
                }
            }
            partsQtyInfoData[NumberConst.IntDef.INT_FOUR + forecastNumMax + NumberConst.IntDef.INT_ONE] = partsQtyInfoEntity
                .getTransferOutQty();
            partsQtyInfoData[NumberConst.IntDef.INT_FOUR + forecastNumMax + NumberConst.IntDef.INT_TWO] = partsQtyInfoEntity
                .getStockTransferOutDetails();
            partsQtyInfoData[NumberConst.IntDef.INT_FOUR + forecastNumMax + NumberConst.IntDef.INT_THREE] = partsBaseInfoEntity
                .getInactiveFlag();

            // Get cell sytle
            int digits = partsBaseInfoEntity.getDecimalDigits();
            CellStyle styleQtyWhite = super.getDecimalStyle(wbTemplate, SHEET_STYLE_QTY_WHITE, digits);
            CellStyle styleQtyRed = super.getDecimalStyle(wbTemplate, SHEET_STYLE_QTY_RED, digits);
            CellStyle styleQtyGray = super.getDecimalStyle(wbTemplate, SHEET_STYLE_QTY_GRAY, digits);
            Cell[] templateCells = new Cell[partsQtyInfoData.length];

            // Output
            Sheet sheet_style_parts_qty_info_title = wbTemplate.getSheet(SHEET_STYLE_PARTS_QTY_INFO_TITLE);

            // Output header
            super.copyRowByFrom(sheet_style_parts_qty_info_title, sheet,
                STYLE_PARTS_QTY_INFO_TITLE[forecastNumMax - 1], 1, 0, partsQtyInfoData.length, new int[] {
                    ROW_START_LINE - 1, lastColumn }, true);

            // Set output cell style (Qty with invoice - Force Complete Qty)
            for (int j = 0; j < NumberConst.IntDef.INT_FIVE; j++) {
                templateCells[j] = styleMap.get(CELL_WHITE_DATA_NORMAL_RIGHT_KEY);
                templateCells[j].setCellStyle(styleQtyWhite);
            }
            // Set output cell style (Order Forecast 1 - Order Forecast 6)
            for (int j = 1; j <= forecastNum; j++) {
                switch (j) {
                    case NumberConst.IntDef.INT_ONE:
                        if (partsBaseInfoEntity.getFcQty1() == null) {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_RED_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyRed);
                        } else {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_WHITE_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyWhite);
                        }
                        break;
                    case NumberConst.IntDef.INT_TWO:
                        if (partsBaseInfoEntity.getFcQty2() == null) {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_RED_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyRed);
                        } else {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_WHITE_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyWhite);
                        }
                        break;
                    case NumberConst.IntDef.INT_THREE:
                        if (partsBaseInfoEntity.getFcQty3() == null) {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_RED_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyRed);
                        } else {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_WHITE_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyWhite);
                        }
                        break;
                    case NumberConst.IntDef.INT_FOUR:
                        if (partsBaseInfoEntity.getFcQty4() == null) {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_RED_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyRed);
                        } else {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_WHITE_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyWhite);
                        }
                        break;
                    case NumberConst.IntDef.INT_FIVE:
                        if (partsBaseInfoEntity.getFcQty5() == null) {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_RED_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyRed);
                        } else {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_WHITE_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyWhite);
                        }
                        break;
                    case NumberConst.IntDef.INT_SIX:
                        if (partsBaseInfoEntity.getFcQty6() == null) {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_RED_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyRed);
                        } else {
                            templateCells[NumberConst.IntDef.INT_FOUR + j] = styleMap
                                .get(CELL_WHITE_DATA_NORMAL_RIGHT_KEY);
                            templateCells[NumberConst.IntDef.INT_FOUR + j].setCellStyle(styleQtyWhite);
                        }
                        break;
                }
            }
            for (int j = NumberConst.IntDef.INT_FIVE + forecastNum; j <= NumberConst.IntDef.INT_FIVE + forecastNumMax; j++) {
                templateCells[j] = styleMap.get(CELL_GRAY_DATA_NORMAL_RIGHT_KEY);
                templateCells[j].setCellStyle(styleQtyGray);
            }
            // Set output cell style (Stock Transfer Out - Discontinue Indicator)
            templateCells[partsQtyInfoData.length - NumberConst.IntDef.INT_THREE] = styleMap
                .get(CELL_WHITE_DATA_NORMAL_RIGHT_KEY);
            templateCells[partsQtyInfoData.length - NumberConst.IntDef.INT_THREE].setCellStyle(styleQtyWhite);
            templateCells[partsQtyInfoData.length - NumberConst.IntDef.INT_TWO] = styleMap
                .get(CELL_WHITE_DATA_NORMAL_LEFT_KEY);
            templateCells[partsQtyInfoData.length - NumberConst.IntDef.INT_ONE] = styleMap
                .get(CELL_WHITE_DATA_NORMAL_CENTER_KEY);

            // Output
            super.createOneDataRowByTemplate(sheet, ROW_START_LINE + i, lastColumn, templateCells, partsQtyInfoData);
        }
        return NumberConst.IntDef.INT_EIGHT + forecastNumMax + lastColumn;
    }

    /**
     * Output Parts Base Information (Start Line 19, Stop Column K (Column 11).)
     * 
     * @param partsBaseInfoList Parts Base Information
     * @param wbTemplate the template excel
     * @param sheet output sheet
     * @param styleMap Style Map
     * @param rowNumOfPartsId row number of partsId
     * @return the max forecastNum
     */
    private int outputPartsBaseInfo(List<CPKKPF02Entity> partsBaseInfoList, Workbook wbTemplate, Sheet sheet,
        HashMap<String, Cell> styleMap, HashMap<Integer, Integer[]> rowNumOfPartsId) {
        int forecastNum = 0;

        PartSortManager.sort(partsBaseInfoList, "ttcPartsNo", "oldTtcPartsNo");
        for (int i = 0; i < partsBaseInfoList.size(); i++) {
            CPKKPF02Entity entity = partsBaseInfoList.get(i);

            // Get the max forecastNum for output Parts QTY Information
            if (entity.getForecastNum() > forecastNum) {
                forecastNum = entity.getForecastNum();
            }

            // Output data
            Object[] partsBaseInfoData = new Object[] { i + 1, entity.getTtcPartsNo(), entity.getOldTtcPartsNo(),
                entity.getCustomerCode(), entity.getExpCustCode(), entity.getSuppPartsNo(), entity.getTtcSuppCode(),
                entity.getRemark(), entity.getSpq(), entity.getQty(), entity.getKanbanQty() };

            // Get cell sytle
            int digits = entity.getDecimalDigits();
            CellStyle styleQtyGreen = super.getDecimalStyle(wbTemplate, SHEET_STYLE_QTY_WHITE, digits);
            Cell[] templateCells = new Cell[partsBaseInfoData.length];

            // No. sytle
            templateCells[0] = styleMap.get(CELL_WHITE_DATA_NORMAL_CENTER_KEY);

            // TTC Part No. - Supplier Code sytle
            for (int j = 1; j < NumberConst.IntDef.INT_SEVEN; j++) {
                templateCells[j] = styleMap.get(CELL_WHITE_DATA_NORMAL_LEFT_KEY);
            }

            // Remark sytle
            templateCells[NumberConst.IntDef.INT_SEVEN] = styleMap.get(CELL_WHITE_DATA_NORMAL_LEFT_KEY);

            // Qty/Box sytle
            templateCells[NumberConst.IntDef.INT_EIGHT] = styleMap.get(CELL_WHITE_DATA_NORMAL_RIGHT_KEY);
            templateCells[NumberConst.IntDef.INT_EIGHT].setCellStyle(styleQtyGreen);

            // Order QTY sytle
            templateCells[NumberConst.IntDef.INT_NINE] = styleMap.get(CELL_WHITE_DATA_NORMAL_RIGHT_KEY);
            templateCells[NumberConst.IntDef.INT_NINE].setCellStyle(styleQtyGreen);

            // Kanban Qty sytle (Kanban Qty round to 2 decimal places.)
            templateCells[NumberConst.IntDef.INT_TEN] = styleMap.get(CELL_WHITE_DATA_NORMAL_RIGHT_KEY);

            // Output
            super.createOneDataRowByTemplate(sheet, ROW_START_LINE + i, templateCells, partsBaseInfoData);

            CellStyle styleKanbanQty = super.getDecimalStyle(wbTemplate, SHEET_STYLE_QTY_WHITE,
                NumberConst.IntDef.INT_TWO);
            sheet.getRow(ROW_START_LINE + i).getCell(NumberConst.IntDef.INT_TEN).setCellStyle(styleKanbanQty);

            rowNumOfPartsId.put(ROW_START_LINE + i, new Integer[] { entity.getPartsId(), digits });
        }
        return forecastNum;
    }

    /**
     * Get Parts Base Information
     * 
     * @param partsBaseInfoList Parts Base Information
     * @return Parts Base Information (HashMap)
     */
    private HashMap<Integer, CPKKPF02Entity> getPartsBaseInformation(List<CPKKPF02Entity> partsBaseInfoList) {
        HashMap<Integer, CPKKPF02Entity> partsBaseInfo = new HashMap<Integer, CPKKPF02Entity>();
        for (int i = 0; i < partsBaseInfoList.size(); i++) {
            partsBaseInfo.put(partsBaseInfoList.get(i).getPartsId(), partsBaseInfoList.get(i));
        }
        return partsBaseInfo;
    }

    /**
     * Get Parts QTY Information
     * 
     * @param partsBaseInfo Parts Base Information
     * @param condition condition
     * @return Parts QTY Information
     */
    private HashMap<Integer, CPKKPF02Entity> getPartsQtyInformation(HashMap<Integer, CPKKPF02Entity> partsBaseInfo,
        CPKKPF02Entity condition) {
        HashMap<Integer, CPKKPF02Entity> partsQtyInfo = new HashMap<Integer, CPKKPF02Entity>();
        List<CPKKPF02Entity> partsQtyInfoList = cpkkpf02service.getPartsQtyInfo(condition);

        StringBuffer stockTransferOutDetails = new StringBuffer();
        for (int i = 0; i < partsQtyInfoList.size(); i++) {
            CPKKPF02Entity entity = partsQtyInfoList.get(i);
            int partsId = entity.getPartsId();
            BigDecimal expOnShippingQty = entity.getExpOnShippingQty();
            BigDecimal inRackQty = entity.getInRackQty();
            BigDecimal impStockQty = entity.getImpStockQty();
            BigDecimal impAdjustedQty = entity.getImpAdjustedQty();
            BigDecimal impDeliveredQty = entity.getImpDeliveredQty();
            BigDecimal transferOutQty = entity.getTransferOutQty();
            BigDecimal transferOutDetailQty = entity.getTransferOutDetailQty();
            String transferOutDetailCustomerCode = entity.getTransferOutDetailCustomerCode();

            // Stock Transfer Out Details
            if (stockTransferOutDetails.length() > 0) {
                stockTransferOutDetails.append(StringConst.COMMA);
            }
            stockTransferOutDetails.append(StringUtil.nullToEmpty(transferOutDetailCustomerCode));
            stockTransferOutDetails.append(StringConst.LEFT_BRACKET);
            stockTransferOutDetails.append(StringUtil.formatBigDecimal(partsBaseInfo.get(partsId).getDecimalDigits(),
                transferOutDetailQty));
            stockTransferOutDetails.append(StringConst.RIGHT_BRACKET);

            boolean keyBreakFlag = false;
            if (i == partsQtyInfoList.size() - 1) {
                keyBreakFlag = true;
            } else if (partsId != partsQtyInfoList.get(i + 1).getPartsId().intValue()) {
                keyBreakFlag = true;
            }

            if (keyBreakFlag) {

                // Qty with invoice
                // EXP_ONSHIPPING_QTY + IN_RACK_QTY + (D.IMP_STOCK_QTY - D.IMP_ADJUSTED_QTY) + D.IMP_DELIVERED_QTY +
                // D.TRANSFER_OUT_QTY
                BigDecimal qtyWithInvoice = expOnShippingQty.add(inRackQty).add(impStockQty).subtract(impAdjustedQty)
                    .add(impDeliveredQty).add(transferOutQty);
                entity.setQtyWithInvoice(qtyWithInvoice);

                // Order Balance based on actual ETD
                // If Qty with invoice > Order Qty then Order Qty - Qty with invoice
                // else if STATUS=9(Force Completed) then 0
                // else Order Qty - Qty with invoice
                BigDecimal orderQty = partsBaseInfo.get(partsId).getQty();
                if (qtyWithInvoice.compareTo(orderQty) > 0) {
                    entity.setOrderBalanceBasedOnActualEtd(orderQty.subtract(qtyWithInvoice));
                } else if (CodeConst.KanbanPartsStatus.FORCE_COMPLETED == entity.getStatus().intValue()) {
                    entity.setOrderBalanceBasedOnActualEtd(BigDecimal.ZERO);
                } else {
                    entity.setOrderBalanceBasedOnActualEtd(orderQty.subtract(qtyWithInvoice));
                }

                // Actual Inbound Qty
                // (D.IMP_STOCK_QTY - D.IMP_ADJUSTED_QTY) + D.IMP_DELIVERED_QTY + D.TRANSFER_OUT_QTY
                BigDecimal actualInboundQty = impStockQty.subtract(impAdjustedQty).add(impDeliveredQty)
                    .add(transferOutQty);
                if (ImpStockFlag.WITH_CLEARANCE == entity.getImpStockFlag()) {
                    actualInboundQty = DecimalUtil.add(actualInboundQty, inRackQty);
                }
                entity.setActualInboundQty(actualInboundQty);

                // Order Balance based on actual inbound
                // If Actual Inbound Qty > Order Qty then Order Qty - Actual Inbound Qty
                // else if STATUS='9(Force Completed)' then 0
                // else Order Qty - Actual Inbound Qty
                if (actualInboundQty.compareTo(orderQty) > 0) {
                    entity.setOrderBalanceBasedOnActualInbound(orderQty.subtract(actualInboundQty));
                } else if (CodeConst.KanbanPartsStatus.FORCE_COMPLETED == entity.getStatus().intValue()) {
                    entity.setOrderBalanceBasedOnActualInbound(BigDecimal.ZERO);
                } else {
                    entity.setOrderBalanceBasedOnActualInbound(orderQty.subtract(actualInboundQty));
                }

                // Stock Transfer Out Details
                entity.setStockTransferOutDetails(stockTransferOutDetails.toString());
                stockTransferOutDetails = new StringBuffer();

                partsQtyInfo.put(partsId, entity);
            }
        }
        return partsQtyInfo;
    }

    /**
     * Get Invoice Sea List And Invoice Air List.
     * 
     * @param condition condition
     * @param invoiceGroupSeaList Invoice Group Sea List
     * @param invoiceGroupAirList Invoice Group Air List
     * @param invoiceMap Invoice Map (key: InvoiceGroupId, inculde sea and air)
     * @param invoicePartsMap (key: InvoiceGroupId + ; + InvoiceNo + ; + RevisionVersion, inculde sea and air)
     */
    private void getInvoiceInformation(CPKKPF02Entity condition, List<CPKKPF02Entity> invoiceGroupSeaList,
        List<CPKKPF02Entity> invoiceGroupAirList, HashMap<String, List<CPKKPF02Entity>> invoiceMap,
        HashMap<String, List<CPKKPF02Entity>> invoicePartsMap) {

        // Get Invoice Information.
        List<CPKKPF02Entity> invoiceInfoList = cpkkpf02service.getInvoiceInfo(condition);

        int invoiceGroupVersion = 1;
        List<CPKKPF02Entity> invoiceGroupList = new ArrayList<CPKKPF02Entity>();
        List<CPKKPF02Entity> invoiceList = new ArrayList<CPKKPF02Entity>();
        List<CPKKPF02Entity> invoicePartsList = new ArrayList<CPKKPF02Entity>();
        for (int i = 0; i < invoiceInfoList.size(); i++) {

            CPKKPF02Entity entity = invoiceInfoList.get(i);
            String invoiceGroupId = entity.getInvoiceGroupId();
            String invoiceNo = entity.getInvoiceNo();
            String revisionVersion = entity.getRevisionVersion();

            if (i != invoiceInfoList.size() - 1) {
                invoicePartsList.add(entity);

                CPKKPF02Entity entityNext = invoiceInfoList.get(i + 1);
                String invoiceGroupIdNext = entityNext.getInvoiceGroupId();
                String invoiceNoNext = entityNext.getInvoiceNo();
                String revisionVersionNext = entityNext.getRevisionVersion();

                if (!invoiceGroupIdNext.equals(invoiceGroupId)) {
                    entity.setOriginalVersion(String.valueOf(invoiceGroupVersion));
                    invoiceGroupVersion++;

                    invoiceGroupList.add(entity);
                    invoiceList.add(entity);

                    invoiceMap.put(invoiceGroupId, invoiceList);
                    invoicePartsMap.put(invoiceGroupId + StringConst.SEMICOLON + invoiceNo + StringConst.SEMICOLON
                            + revisionVersion, invoicePartsList);

                    invoiceList = new ArrayList<CPKKPF02Entity>();
                    invoicePartsList = new ArrayList<CPKKPF02Entity>();
                } else if (!invoiceNoNext.equals(invoiceNo) || !revisionVersionNext.equals(revisionVersion)) {
                    invoiceList.add(entity);

                    invoicePartsMap.put(invoiceGroupId + StringConst.SEMICOLON + invoiceNo + StringConst.SEMICOLON
                            + revisionVersion, invoicePartsList);
                    invoicePartsList = new ArrayList<CPKKPF02Entity>();
                }
            } else {
                entity.setOriginalVersion(String.valueOf(invoiceGroupVersion));
                invoiceGroupVersion++;

                invoiceGroupList.add(entity);
                invoiceList.add(entity);
                invoicePartsList.add(entity);
                invoiceMap.put(invoiceGroupId, invoiceList);
                invoicePartsMap.put(invoiceGroupId + StringConst.SEMICOLON + invoiceNo + StringConst.SEMICOLON
                        + revisionVersion, invoicePartsList);
                invoiceList = new ArrayList<CPKKPF02Entity>();
                invoicePartsList = new ArrayList<CPKKPF02Entity>();
            }
        }

        int reasonVersion = 1;
        for (CPKKPF02Entity entity : invoiceGroupList) {
            int transportMode = entity.getTransportMode();
            entity.setOriginalVersion(String.valueOf(reasonVersion));
            reasonVersion++;

            if (CodeConst.TransportMode.SEA == transportMode) {
                // Add result into Invoice Sea List
                invoiceGroupSeaList.add(entity);
            } else if (CodeConst.TransportMode.AIR == transportMode) {
                // Add result into Invoice Air List
                invoiceGroupAirList.add(entity);
            }
        }
    }

    /**
     * Get Shipping Plan Sea List And Shipping Plan Air List And Shipping Plan Nird List.
     * 
     * @param condition condition
     * @param shippingPlanSeaList Shipping Plan Sea List
     * @param shippingPlanAirList Shipping Plan Air List
     * @param shippingPlanNirdList Shipping Plan Nird List
     * @param shippingPlanPartsMap (key: kanbanShippingId, inculde sea and air)
     */
    private void getShippingPlanInformation(CPKKPF02Entity condition, List<CPKKPF02Entity> shippingPlanSeaList,
        List<CPKKPF02Entity> shippingPlanAirList, List<CPKKPF02Entity> shippingPlanNirdList,
        HashMap<String, List<CPKKPF02Entity>> shippingPlanPartsMap) {

        // Get Shipping Plan Information.
        List<CPKKPF02Entity> shippingPlanInfoList = cpkkpf02service.getShippingPlanInfo(condition);

        List<CPKKPF02Entity> shippingPlanList = new ArrayList<CPKKPF02Entity>();
        List<CPKKPF02Entity> shippingPlanPartsList = new ArrayList<CPKKPF02Entity>();

        for (int i = 0; i < shippingPlanInfoList.size(); i++) {
            CPKKPF02Entity entity = shippingPlanInfoList.get(i);
            String kanbanShippingId = entity.getKanbanShippingId();
            String shippingUuid = entity.getShippingUuid();

            if (i != shippingPlanInfoList.size() - 1) {
                shippingPlanPartsList.add(entity);

                CPKKPF02Entity entityNext = shippingPlanInfoList.get(i + 1);
                String kanbanShippingIdNext = entityNext.getKanbanShippingId();
                String shippingUuidNext = entityNext.getShippingUuid();

                if (!kanbanShippingId.equals(kanbanShippingIdNext)) {
                    shippingPlanPartsMap.put(kanbanShippingId, shippingPlanPartsList);
                    shippingPlanPartsList = new ArrayList<CPKKPF02Entity>();
                }

                if (!shippingUuid.equals(shippingUuidNext)) {
                    shippingPlanList.add(entity);
                }
            } else {
                shippingPlanList.add(entity);

                shippingPlanPartsList.add(entity);
                shippingPlanPartsMap.put(kanbanShippingId, shippingPlanPartsList);
                shippingPlanPartsList = new ArrayList<CPKKPF02Entity>();
            }
        }

        for (CPKKPF02Entity entity : shippingPlanList) {
            int transportMode = entity.getTransportMode();
            int nirdFlag = entity.getNirdFlag();

            if (CodeConst.NirdFlag.NOT_IN_RUNDOWN == nirdFlag) {
                // Add result into Shipping Plan Nird List
                shippingPlanNirdList.add(entity);
                continue;
            }

            if (CodeConst.TransportMode.SEA == transportMode) {
                // Add result into Shipping Plan Sea List
                shippingPlanSeaList.add(entity);
            } else if (CodeConst.TransportMode.AIR == transportMode) {
                // Add result into Shipping Plan Air List
                shippingPlanAirList.add(entity);
            }
        }
    }

    /**
     * Get ShippingPlan History Information.
     * 
     * @param condition codition
     * @param shippingPlanHistoryMap ShippingPlan History Information
     * @param shippingPlanHistoryPartsMap ShippingPlan History Parts Information
     */
    private void getShippingPlanHistoryInformation(CPKKPF02Entity condition,
        HashMap<String, List<CPKKPF02Entity>> shippingPlanHistoryMap,
        HashMap<String, List<CPKKPF02Entity>> shippingPlanHistoryPartsMap) {

        // Get Shipping Plan History Information.
        List<CPKKPF02Entity> shippingPlanHistoryInfoList = cpkkpf02service.getShippingPlanHistoryInfo(condition);

        List<CPKKPF02Entity> shippingPlanHistoryList = new ArrayList<CPKKPF02Entity>();
        List<CPKKPF02Entity> shippingPlanHistoryPartsList = new ArrayList<CPKKPF02Entity>();

        for (int i = 0; i < shippingPlanHistoryInfoList.size(); i++) {

            CPKKPF02Entity entity = shippingPlanHistoryInfoList.get(i);
            String kanbanId = entity.getKanbanId().toString();
            String originalVersion = entity.getOriginalVersion();
            String revisionVersion = entity.getRevisionVersion();
            int transportMode = entity.getTransportMode();

            if (i != shippingPlanHistoryInfoList.size() - 1) {
                shippingPlanHistoryPartsList.add(entity);

                CPKKPF02Entity entityNext = shippingPlanHistoryInfoList.get(i + 1);
                String kanbanIdNext = entityNext.getKanbanId().toString();
                String originalVersionNext = entityNext.getOriginalVersion();
                String revisionVersionNext = entityNext.getRevisionVersion();
                int transportModeNext = entityNext.getTransportMode();

                if (!kanbanIdNext.equals(kanbanId) || !originalVersionNext.equals(originalVersion)
                        || transportMode != transportModeNext) {
                    shippingPlanHistoryList.add(entity);

                    shippingPlanHistoryMap.put(kanbanId + StringConst.SEMICOLON + originalVersion
                            + StringConst.SEMICOLON + transportMode, shippingPlanHistoryList);
                    shippingPlanHistoryPartsMap.put(kanbanId + StringConst.SEMICOLON + originalVersion
                            + StringConst.SEMICOLON + transportMode + StringConst.SEMICOLON + revisionVersion,
                        shippingPlanHistoryPartsList);

                    shippingPlanHistoryList = new ArrayList<CPKKPF02Entity>();
                    shippingPlanHistoryPartsList = new ArrayList<CPKKPF02Entity>();
                } else if (!revisionVersionNext.equals(revisionVersion)) {
                    shippingPlanHistoryList.add(entity);

                    shippingPlanHistoryPartsMap.put(kanbanId + StringConst.SEMICOLON + originalVersion
                            + StringConst.SEMICOLON + transportMode + StringConst.SEMICOLON + revisionVersion,
                        shippingPlanHistoryPartsList);
                    shippingPlanHistoryPartsList = new ArrayList<CPKKPF02Entity>();
                }
            } else {
                shippingPlanHistoryList.add(entity);
                shippingPlanHistoryPartsList.add(entity);
                shippingPlanHistoryMap.put(kanbanId + StringConst.SEMICOLON + originalVersion + StringConst.SEMICOLON
                        + transportMode, shippingPlanHistoryList);
                shippingPlanHistoryPartsMap.put(kanbanId + StringConst.SEMICOLON + originalVersion
                        + StringConst.SEMICOLON + transportMode + StringConst.SEMICOLON + revisionVersion,
                    shippingPlanHistoryPartsList);
                shippingPlanHistoryList = new ArrayList<CPKKPF02Entity>();
                shippingPlanHistoryPartsList = new ArrayList<CPKKPF02Entity>();
            }
        }
    }

    /**
     * Get Parts Box Information.
     * 
     * @param condition codition
     * @param differenceBoxMap Difference/Box Map
     * @param differenceBoxPartsMap Parts of Difference/Box Map
     */
    private void getPartsBoxInformation(CPKKPF02Entity condition,
        HashMap<String, List<CPKKPF02Entity>> differenceBoxMap,
        HashMap<String, List<CPKKPF02Entity>> differenceBoxPartsMap) {

        List<CPKKPF02Entity> differenceBoxList = new ArrayList<CPKKPF02Entity>();
        List<CPKKPF02Entity> differenceBoxPartsList = new ArrayList<CPKKPF02Entity>();

        // Get Parts Box Information
        List<CPKKPF02Entity> partsBoxInfoList = cpkkpf02service.getPartsBoxInfo(condition);
        for (int i = 0; i < partsBoxInfoList.size(); i++) {
            CPKKPF02Entity entity = partsBoxInfoList.get(i);

            String shippingUuid = entity.getShippingUuid();
            String kanbanPlanId = entity.getKanbanPlanId();

            differenceBoxList.add(entity);
            differenceBoxPartsList.add(entity);

            if (i != partsBoxInfoList.size() - 1) {
                CPKKPF02Entity entityNext = partsBoxInfoList.get(i + 1);
                String shippingUuidNext = entityNext.getShippingUuid();
                String kanbanPlanIdNext = entityNext.getKanbanPlanId();

                if (!shippingUuidNext.equals(shippingUuid)) {
                    differenceBoxMap.put(shippingUuid, differenceBoxList);
                    differenceBoxPartsMap.put(shippingUuid + StringConst.SEMICOLON + kanbanPlanId,
                        differenceBoxPartsList);

                    differenceBoxList = new ArrayList<CPKKPF02Entity>();
                    differenceBoxPartsList = new ArrayList<CPKKPF02Entity>();
                } else if (!kanbanPlanIdNext.equals(kanbanPlanId)) {
                    differenceBoxPartsMap.put(shippingUuid + StringConst.SEMICOLON + kanbanPlanId,
                        differenceBoxPartsList);
                    differenceBoxPartsList = new ArrayList<CPKKPF02Entity>();
                }
            } else {
                differenceBoxMap.put(shippingUuid, differenceBoxList);
                differenceBoxPartsMap.put(shippingUuid + StringConst.SEMICOLON + kanbanPlanId, differenceBoxPartsList);
                differenceBoxList = new ArrayList<CPKKPF02Entity>();
                differenceBoxPartsList = new ArrayList<CPKKPF02Entity>();
            }
        }
    }

    /**
     * Sort List with ETD.
     * 
     * @param invoiceInfoList Invoice List
     * @param shippingPlanInfoList Shipping Plan List
     */
    private void sort(List<CPKKPF02Entity> invoiceInfoList, List<CPKKPF02Entity> shippingPlanInfoList) {
        invoiceInfoList.addAll(shippingPlanInfoList);

        Collections.sort(invoiceInfoList, new Comparator<Object>() {

            @Override
            public int compare(Object arg0, Object arg1) {

                CPKKPF02Entity entity1 = (CPKKPF02Entity) arg0;
                CPKKPF02Entity entity2 = (CPKKPF02Entity) arg1;
                int compareResult = entity1.getEtd().compareTo(entity2.getEtd());
                if (compareResult == 0) {
                    if (StringUtils.isBlank(entity2.getInvoiceGroupId())) {
                        compareResult = 1;
                    } else {
                        compareResult = 0;
                    }
                }
                return compareResult;
            }
        });
        sortSameEtd(invoiceInfoList);
    }

    /**
     * Sort List with same ETD.
     * 
     * @param dataList sort object
     * @return sort result
     */
    private List<CPKKPF02Entity> sortSameEtd(List<CPKKPF02Entity> dataList) {
        if (dataList.size() < NumberConst.IntDef.INT_TWO) {
            return dataList;
        }

        // Get the list which etd is the same (Invoice Group + Shipping Plan)
        HashMap<Timestamp, List<CPKKPF02Entity>> sameEtdMap = new HashMap<Timestamp, List<CPKKPF02Entity>>();
        List<CPKKPF02Entity> list = new ArrayList<CPKKPF02Entity>();
        for (int i = 0; i < dataList.size(); i++) {
            CPKKPF02Entity entity = dataList.get(i);

            if (i == 0) {
                // Compare to the next etd if it's the first etd
                CPKKPF02Entity entityNext = dataList.get(i + 1);
                if (entity.getEtd().compareTo(entityNext.getEtd()) == 0) {
                    if (sameEtdMap.containsKey(entity.getEtd())) {
                        list = sameEtdMap.get(entity.getEtd());
                        sameEtdMap.remove(entity.getEtd());
                    } else {
                        list = new ArrayList<CPKKPF02Entity>();
                    }
                    list.add(entity);
                    sameEtdMap.put(entity.getEtd(), list);
                }
            } else if (i == dataList.size() - 1) {
                // Compare to the previous etd if it's the last etd
                CPKKPF02Entity entityPre = dataList.get(i - 1);
                if (entity.getEtd().compareTo(entityPre.getEtd()) == 0) {
                    if (sameEtdMap.containsKey(entity.getEtd())) {
                        list = sameEtdMap.get(entity.getEtd());
                        sameEtdMap.remove(entity.getEtd());
                    } else {
                        list = new ArrayList<CPKKPF02Entity>();
                    }
                    list.add(entity);
                    sameEtdMap.put(entity.getEtd(), list);
                }
            } else {
                // Compare to the previous etd
                CPKKPF02Entity entityPre = dataList.get(i - 1);
                if (entity.getEtd().compareTo(entityPre.getEtd()) == 0) {
                    if (sameEtdMap.containsKey(entity.getEtd())) {
                        list = sameEtdMap.get(entity.getEtd());
                        sameEtdMap.remove(entity.getEtd());
                    } else {
                        list = new ArrayList<CPKKPF02Entity>();
                    }
                    list.add(entity);
                    sameEtdMap.put(entity.getEtd(), list);
                    continue;
                }

                // Compare to the next etd
                CPKKPF02Entity entityNext = dataList.get(i + 1);
                if (entity.getEtd().compareTo(entityNext.getEtd()) == 0) {
                    if (sameEtdMap.containsKey(entity.getEtd())) {
                        list = sameEtdMap.get(entity.getEtd());
                        sameEtdMap.remove(entity.getEtd());
                    } else {
                        list = new ArrayList<CPKKPF02Entity>();
                    }
                    list.add(entity);
                    sameEtdMap.put(entity.getEtd(), list);
                }
            }
        }

        // Sort if the record count of the same ETD is ovre 2
        Iterator<Entry<Timestamp, List<CPKKPF02Entity>>> iter = sameEtdMap.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Timestamp, List<CPKKPF02Entity>> e = (Entry<Timestamp, List<CPKKPF02Entity>>) iter.next();
            List<CPKKPF02Entity> sameEtdList = e.getValue();

            List<Integer> removeIndex = new ArrayList<Integer>();

            for (int i = 0; i < sameEtdList.size(); i++) {
                if (!StringUtils.isBlank(sameEtdList.get(i).getInvoiceGroupId())) {
                    // Deal with invoice group information (need to move invoice group information only)
                    for (int j = i + 1; j < sameEtdList.size(); j++) {
                        if (!StringUtils.isBlank(sameEtdList.get(j).getShippingUuid())) {
                            if (j == sameEtdList.size() - 1) {
                                removeIndex.add(i);
                                sameEtdList.add(sameEtdList.get(i));
                                break;
                            } else if (sameEtdList.get(i).getFirstInviceGroupBoxDate() == null) {
                                continue;
                            } else if (sameEtdList.get(j).getLastShippingPlanBoxDate() == null
                                    || sameEtdList.get(j).getLastShippingPlanBoxDate()
                                        .compareTo(sameEtdList.get(i).getFirstInviceGroupBoxDate()) >= 0) {
                                removeIndex.add(i);

                                // Move to after of invoice group information which is found after the nearest
                                // shipping plan information
                                int invoiceGroupCount = 0;
                                for (int k = j + 1; k < sameEtdList.size(); k++) {
                                    if (!StringUtils.isBlank(sameEtdList.get(k).getShippingUuid())
                                            && sameEtdList.get(k).getLastShippingPlanBoxDate() != null) {
                                        // Stop at the first shipping plan information
                                        break;
                                    } else {
                                        // Get the invoice group information count of the broken shipping plan
                                        // object
                                        invoiceGroupCount++;
                                    }
                                }

                                // Move to after of the broken shipping plan object
                                int setPositionIndex = j + invoiceGroupCount + 1;
                                for (int k = sameEtdList.size() - 1; k >= setPositionIndex; k--) {
                                    if (k == sameEtdList.size() - 1) {
                                        sameEtdList.add(sameEtdList.get(k));
                                    } else {
                                        sameEtdList.set(k + 1, sameEtdList.get(k));
                                    }
                                }
                                if (setPositionIndex == sameEtdList.size()) {
                                    sameEtdList.add(sameEtdList.get(i));
                                } else {
                                    sameEtdList.set(setPositionIndex, sameEtdList.get(i));
                                }
                                break;
                            }
                        }
                    }
                } else {
                    // Stop at the first shipping plan information (do not need to move shipping plan information)
                    break;
                }
            }

            for (int i = 0; i < removeIndex.size(); i++) {
                sameEtdList.remove(0);
            }
            sameEtdMap.put(e.getKey(), sameEtdList);
        }

        // Merge the same ETD list into the sort list
        for (int i = 0; i < dataList.size();) {
            CPKKPF02Entity entity = dataList.get(i);
            Timestamp etd = entity.getEtd();

            if (sameEtdMap.containsKey(etd)) {
                for (int j = 0; j < sameEtdMap.get(etd).size(); j++) {
                    dataList.set(i + j, sameEtdMap.get(etd).get(j));
                }
                i = i + sameEtdMap.get(etd).size();
            } else {
                i++;
            }
        }
        return dataList;
    }
}
