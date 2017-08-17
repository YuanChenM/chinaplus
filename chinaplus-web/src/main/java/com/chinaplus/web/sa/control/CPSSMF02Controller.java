/**
 * CPSSMF02Controller.java
 * 
 * @screen CPSSMS02
 * @author ma_chao
 */
package com.chinaplus.web.sa.control;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst.CategoryLanguage;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataType;
import com.chinaplus.common.entity.TntOrder;
import com.chinaplus.common.entity.TntSsPlan;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.sa.entity.CPSSMF01ColEntity;
import com.chinaplus.web.sa.entity.CPSSMF01Entity;
import com.chinaplus.web.sa.entity.CPSSMF01PartEntity;
import com.chinaplus.web.sa.service.CPSSMF02Service;

/**
 * Download Revised Shipping Status (Doc2) Controller.
 */
@SuppressWarnings({ "deprecation", "unchecked", "unused" })
@Controller
public class CPSSMF02Controller extends BaseFileController {

    /** The begin of download constant define */
    private static final String DOWNLOAD_ZIP_FILE_NAME = "Shipping Status Doc2_{0}.zip";
    private static final String ZIP_CONTENT_TYPE = "application/x-zip-compressed;charset=UTF-8";
    private static final String DOWNLOAD_EXCEL_FILE_NAME = "Shipping Status Doc2_{0}.xlsx";
    private static final String DOWNLOAD_EXCEL_SHEET_NAME = "Shipping Status Doc2";
    /** The end of download constant define */

    /** The begin of cell style name constant define */
    private static final String CELL_PLAN = "CELL_PLAN";
    private static final String CELL_ACTUAL = "CELL_ACTUAL";
    private static final String CELL_GRAY_LEFT = "CELL_GRAY_LEFT";
    private static final String CELL_GRAY_LEFT_MOST_RIGHT = "CELL_GRAY_LEFT_MOST_RIGHT";
    private static final String CELL_GRAY_LEFT_MOST_BOTTOM = "CELL_GRAY_LEFT_MOST_BOTTOM";
    private static final String CELL_GRAY_LEFT_LAST = "CELL_GRAY_LEFT_LAST";
    private static final String CELL_GRAY_CENTER = "CELL_GRAY_CENTER";
    private static final String CELL_GRAY_CENTER_MOST_RIGHT = "CELL_GRAY_CENTER_MOST_RIGHT";
    private static final String CELL_GRAY_CENTER_MOST_BOTTOM = "CELL_GRAY_CENTER_MOST_BOTTOM";
    private static final String CELL_GRAY_CENTER_LAST = "CELL_GRAY_CENTER_LAST";
    private static final String CELL_GRAY_RIGHT = "CELL_GRAY_RIGHT";
    private static final String CELL_GRAY_DATE_EN = "CELL_GRAY_DATE_EN";
    private static final String CELL_GRAY_DATE_MOST_RIGHT_EN = "CELL_GRAY_DATE_MOST_RIGHT_EN";
    private static final String CELL_GRAY_DATE_MOST_BOTTOM_EN = "CELL_GRAY_DATE_MOST_BOTTOM_EN";
    private static final String CELL_GRAY_DATE_LAST_EN = "CELL_GRAY_DATE_LAST_EN";
    private static final String CELL_GRAY_DATE_CN = "CELL_GRAY_DATE_CN";
    private static final String CELL_GRAY_DATE_MOST_RIGHT_CN = "CELL_GRAY_DATE_MOST_RIGHT_CN";
    private static final String CELL_GRAY_DATE_MOST_BOTTOM_CN = "CELL_GRAY_DATE_MOST_BOTTOM_CN";
    private static final String CELL_GRAY_DATE_LAST_CN = "CELL_GRAY_DATE_LAST_CN";
    private static final String CELL_WHITE_LEFT = "CELL_WHITE_LEFT";
    private static final String CELL_WHITE_LEFT_MOST_RIGHT = "CELL_WHITE_LEFT_MOST_RIGHT";
    private static final String CELL_WHITE_LEFT_MOST_BOTTOM = "CELL_WHITE_LEFT_MOST_BOTTOM";
    private static final String CELL_WHITE_LEFT_LAST = "CELL_WHITE_LEFT_LAST";
    private static final String CELL_WHITE_CENTER = "CELL_WHITE_CENTER";
    private static final String CELL_WHITE_RIGHT = "CELL_WHITE_RIGHT";
    private static final String CELL_WHITE_DATE_EN = "CELL_WHITE_DATE_EN";
    private static final String CELL_WHITE_DATE_MOST_RIGHT_EN = "CELL_WHITE_DATE_MOST_RIGHT_EN";
    private static final String CELL_WHITE_DATE_MOST_BOTTOM_EN = "CELL_WHITE_DATE_MOST_BOTTOM_EN";
    private static final String CELL_WHITE_DATE_LAST_EN = "CELL_WHITE_DATE_LAST_EN";
    private static final String CELL_WHITE_DATE_CN = "CELL_WHITE_DATE_CN";
    private static final String CELL_WHITE_DATE_MOST_RIGHT_CN = "CELL_WHITE_DATE_MOST_RIGHT_CN";
    private static final String CELL_WHITE_DATE_MOST_BOTTOM_CN = "CELL_WHITE_DATE_MOST_BOTTOM_CN";
    private static final String CELL_WHITE_DATE_LAST_CN = "CELL_WHITE_DATE_LAST_CN";
    private static final String CELL_BEFORE_TODAY = "CELL_BEFORE_TODAY";
    private static final String CELL_DOWNLOAD_TIME_EN = "CELL_DOWNLOAD_TIME_EN";
    private static final String CELL_DOWNLOAD_TIME_CN = "CELL_DOWNLOAD_TIME_CN";
    private static final String CELL_LAST_TTLOGIC_TIME_EN = "CELL_LAST_TTLOGIC_TIME_EN";
    private static final String CELL_LAST_TTLOGIC_TIME_CN = "CELL_LAST_TTLOGIC_TIME_CN";
    private static final String CELL_NEW_CELL = "CELL_NEW_CELL";
    /** The end of cell style name constant define */

    /** begin row number of base info */
    private static final int BEGIN_ROW_NUMBER = 12;
    /** row start line of plan or invoice info (4th line - 1) */
    private static final int BEGIN_ROW_NUMBER_PLAN_OR_INVOICE = 3;
    /** column start line of plan or invoice info */
    private static final int BEGIN_COLUMN_NUMBER_PLAN_OR_INVOICE = 18;
    /** column width */
    private static final int COLUMN_WIDTH = 3500;
    /** summary qty title blank row count */
    private static final int SUMMARY_QTY_TITLE_BLANK_ROW_COUNT = 8;
    /** Download Revised Shipping Status (Doc1) Service */
    @Autowired
    private CPSSMF02Service cpssmf02Service;

    private Map<String, Cell> styleCellMap;

    @Override
    protected String getFileId() {
        return FileId.CPSSMF02;
    }

    /**
     * Download check for Revised Shipping Status.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return check result
     * @throws ParseException ParseException
     */
    @RequestMapping(value = "/sa/CPSSMF02/downloadcheck")
    @ResponseBody
    public BaseResult<String> downloadCheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) throws ParseException {
        BaseResult<String> result = new BaseResult<String>();
        super.setCommonParam(param, request);

        Locale lang = MessageManager.getLanguage(param.getLanguage()).getLocale();
        String Msg = null;
        String Msg2 = null;
        String Msg3 = param.getLanguage() == IntDef.INT_TWO ? "和" : "and";
        String saleDateFrom = (String) param.getSwapData().get("saleDateFrom");
        String saleDateTo = (String) param.getSwapData().get("saleDateTo");
        String planEtdFrom = (String) param.getSwapData().get("planEtdFrom");
        String planEtdTo = (String) param.getSwapData().get("planEtdTo");
        String planEtaFrom = (String) param.getSwapData().get("planEtaFrom");
        String planEtaTo = (String) param.getSwapData().get("planEtaTo");
        String invoiceEtdFrom = (String) param.getSwapData().get("invoiceEtdFrom");
        String invoiceEtdTo = (String) param.getSwapData().get("invoiceEtdTo");
        String invoiceEtaFrom = (String) param.getSwapData().get("invoiceEtaFrom");
        String invoiceEtaTo = (String) param.getSwapData().get("invoiceEtaTo");
        // saleDateFrom && saleDateTo
        if (!StringUtil.isNullOrEmpty(saleDateFrom) && !StringUtil.isNullOrEmpty(saleDateTo)) {
            Msg = MessageManager.getMessage("CPSSMS02_Label_JpGscmSalesOrderCreateDateFrom", lang).replace("<br>", "");
            Msg2 = MessageManager.getMessage("CPSSMS02_Label_JpGscmSalesOrderCreateDateTo", lang);
            String to = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(saleDateTo));
            String from = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(saleDateFrom));
            if (StringUtil.toSafeInteger(to) < StringUtil.toSafeInteger(from)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { Msg, Msg2 });
                result.addMessage(message);
            } else {
                if (getDateDifference(from, to)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_019);
                    String msgValue = Msg + Msg3 + Msg2;
                    message.setMessageArgs(new String[] { msgValue, String.valueOf(IntDef.INT_SIX) });
                    result.addMessage(message);
                }
            }
        }
        // planEtdFrom && planEtdTo
        if (!StringUtil.isNullOrEmpty(planEtdFrom) && !StringUtil.isNullOrEmpty(planEtdTo)) {
            Msg = MessageManager.getMessage("CPSSMS02_Label_PlanEtdFrom", lang).replace("<br>", "");
            Msg2 = MessageManager.getMessage("CPSSMS02_Label_PlanEtdTo", lang);
            String to = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(planEtdTo));
            String from = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(planEtdFrom));
            if (StringUtil.toSafeInteger(to) < StringUtil.toSafeInteger(from)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { Msg, Msg2 });
                result.addMessage(message);
            } else {
                if (getDateDifference(from, to)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_019);
                    String msgValue = Msg + Msg3 + Msg2;
                    message.setMessageArgs(new String[] { msgValue, String.valueOf(IntDef.INT_SIX) });
                    result.addMessage(message);
                }
            }
        }
        // planEtaFrom && planEtaTo
        if (!StringUtil.isNullOrEmpty(planEtaFrom) && !StringUtil.isNullOrEmpty(planEtaTo)) {
            Msg = MessageManager.getMessage("CPSSMS02_Label_PlanEtaFrom", lang).replace("<br>", "");
            Msg2 = MessageManager.getMessage("CPSSMS02_Label_PlanEtaTo", lang);
            String to = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(planEtaTo));
            String from = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(planEtaFrom));
            if (StringUtil.toSafeInteger(to) < StringUtil.toSafeInteger(from)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { Msg, Msg2 });
                result.addMessage(message);
            } else {
                if (getDateDifference(from, to)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_019);
                    String msgValue = Msg + Msg3 + Msg2;
                    message.setMessageArgs(new String[] { msgValue, String.valueOf(IntDef.INT_SIX) });
                    result.addMessage(message);
                }
            }
        }
        // invoiceEtdFrom && invoiceEtdTo
        if (!StringUtil.isNullOrEmpty(invoiceEtdFrom) && !StringUtil.isNullOrEmpty(invoiceEtdTo)) {
            Msg = MessageManager.getMessage("CPSSMS02_Label_InvoiceEtdFrom", lang).replace("<br>", "");
            Msg2 = MessageManager.getMessage("CPSSMS02_Label_InvoiceEtdTo", lang);
            String to = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(invoiceEtdTo));
            String from = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(invoiceEtdFrom));
            if (StringUtil.toSafeInteger(to) < StringUtil.toSafeInteger(from)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { Msg, Msg2 });
                result.addMessage(message);
            } else {
                if (getDateDifference(from, to)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_019);
                    String msgValue = Msg + Msg3 + Msg2;
                    message.setMessageArgs(new String[] { msgValue, String.valueOf(IntDef.INT_SIX) });
                    result.addMessage(message);
                }
            }
        }
        // invoiceEtaFrom && invoiceEtaTo
        if (!StringUtil.isNullOrEmpty(invoiceEtaFrom) && !StringUtil.isNullOrEmpty(invoiceEtaTo)) {
            Msg = MessageManager.getMessage("CPSSMS02_Label_InvoiceEtaFrom", lang).replace("<br>", "");
            Msg2 = MessageManager.getMessage("CPSSMS02_Label_InvoiceEtaTo", lang);
            String to = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(invoiceEtaTo));
            String from = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(invoiceEtaFrom));
            if (StringUtil.toSafeInteger(to) < StringUtil.toSafeInteger(from)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { Msg, Msg2 });
                result.addMessage(message);
            } else {
                if (getDateDifference(from, to)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_019);
                    String msgValue = Msg + Msg3 + Msg2;
                    message.setMessageArgs(new String[] { msgValue, String.valueOf(IntDef.INT_SIX) });
                    result.addMessage(message);
                }
            }
        }

        boolean validResult = CPSSMFXXUtil.validDateTimeFilter(param, result);
        if (validResult) {
            CPSSMFXXUtil.convertParam(param);
            List<TntSsPlan> planList = this.cpssmf02Service.searchSsPlanList(param);
            List<TntOrder> orderList = null;
            List<TntOrder> orderListByInvoice = null;
            if (null != planList && !planList.isEmpty()) {
                orderList = this.cpssmf02Service.searchOrderList(param);
            }

            if (null != orderList && !orderList.isEmpty()) {
                orderListByInvoice = this.cpssmf02Service.searchOrderListByInvoice(param);
            }

            // search base info
            List<CPSSMF01Entity> planInfoList = this.cpssmf02Service.searchShippingPlanInfo(param, orderList,
                orderListByInvoice, planList);
            if (null == planInfoList || planInfoList.isEmpty()) {
                result.addMessage(new BaseMessage(MessageCodeConst.W1005_001));
            }
        }

        return result;
    }

    /**
     * Download file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the exception
     */
    @RequestMapping(value = "/sa/CPSSMF02/download")
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream os = null;
        ZipOutputStream zos = null;
        try {
            super.setCommonParam(param, request);
            String clientTime = param.getClientTime();

            response.setContentType(ZIP_CONTENT_TYPE);
            response.setCharacterEncoding(ENCODE);

            String zipFileName = StringUtil.formatMessage(DOWNLOAD_ZIP_FILE_NAME, clientTime);

            response.setHeader("Content-disposition",
                StringUtil.formatMessage("attachment; filename=\"{0}\"", zipFileName));
            os = response.getOutputStream();
            zos = new ZipOutputStream(os);

            // Generate temporary folder path
            String tempFolderPath = ConfigUtil.get(Properties.TEMPORARY_PATH) + UUID.randomUUID().toString();
            // Create temporary folder
            File tempFolder = new File(tempFolderPath);
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }

            CPSSMFXXUtil.convertParam(param);
            String fileName = StringUtil.formatMessage(DOWNLOAD_EXCEL_FILE_NAME, param.getClientTime());
            super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), new String[] { fileName },
                new String[] { getFileId() }, param, zos);

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
     * @param service CPKKPF01Service
     */
    public void outputMainProcess(BaseParam param, Workbook wbTemplate, SXSSFWorkbook wbOutput, CPSSMF02Service service) {
        if (null == cpssmf02Service) {
            cpssmf02Service = service;
        }
        Date officeTime = DateTimeUtil.parseDate(DateTimeUtil.formatDate(null,
            cpssmf02Service.getDBDateTime(param.getOfficeTimezone())));

        this.doOutput(param, officeTime, wbTemplate, wbOutput);

        CPSSMFXXUtil.removeStyleSheet(wbOutput);

        wbOutput.setSheetName(0, DOWNLOAD_EXCEL_SHEET_NAME);
    }

    /**
     * Output.
     * 
     * @param param page parameter
     * @param officeTime officeTime
     * @param wbTemplate the template excel
     * @param wbOutput the output excel
     */
    private void doOutput(BaseParam param, Date officeTime, Workbook wbTemplate, SXSSFWorkbook wbOutput) {
        Sheet sheet = wbTemplate.getSheet(CPSSMFXXUtil.SHEET_BASE);
        styleCellMap = this.initStyleCell(param, wbTemplate);
        // search order and plan
        List<TntSsPlan> planList = this.cpssmf02Service.searchSsPlanList(param);
        List<TntOrder> orderList = null;
        List<TntOrder> orderListByPlan = null;
        List<TntOrder> orderListByInvoice = null;
        if (null != planList && !planList.isEmpty()) {
            orderList = this.cpssmf02Service.searchOrderList(param);
            orderListByPlan = this.cpssmf02Service.searchOrderListByPlan(param);
        }

        if (null != orderList && !orderList.isEmpty()) {
            orderListByInvoice = this.cpssmf02Service.searchOrderListByInvoice(param);
        }

        // search base info
        List<CPSSMF01Entity> planInfoList = this.cpssmf02Service.searchShippingPlanInfo(param, orderList,
            orderListByInvoice, planList);

        // need output mod cell and new column
        boolean needModNew = true;
        // key: ipo + customerOrderNo, value: beginRowNum & endRowNum
        Map<String, Integer[]> ipoRowNumMap = new HashMap<String, Integer[]>();
        // when po count > 1
        if (null != planInfoList && !planInfoList.isEmpty()) {
            int ipoCount = CPSSMFXXUtil.countIpoAndCustomerOrderNo(planInfoList);
            if (ipoCount > 1) {
                needModNew = false;
                ipoRowNumMap = CPSSMFXXUtil.mapRowNumOfIPO(BEGIN_ROW_NUMBER, CPSSMFXXUtil.combineRowData(planInfoList));
            }
        }

        // search actual datas
        List<CPSSMF01Entity> invoiceInfoList = null;
        if (null != planInfoList && !planInfoList.isEmpty()) {
            invoiceInfoList = this.cpssmf02Service.searchInvoiceInfo(param, planInfoList);
        }

        // combine row data by ssId & partsId
        List<CPSSMF01Entity> rowDataList = CPSSMFXXUtil.combineRowData(planInfoList);
        // combine and sort row data to column data
        List<CPSSMF01ColEntity> planAndActualList = new ArrayList<CPSSMF01ColEntity>();
        this.cpssmf02Service.sortPlanAndActualInfo(planInfoList, invoiceInfoList, planAndActualList, officeTime);

        // Output base row data
        Map<String, Integer> rowNumMap = new HashMap<String, Integer>();
        Map<Integer, String> uomMap = new HashMap<Integer, String>();
        Map<Integer, BigDecimal> orderQtyMap = new HashMap<Integer, BigDecimal>(); // key: rowNum
        Map<String, List<Integer>> samePoRowNumMap = new HashMap<String, List<Integer>>();
        int maxRowNum = this.outputBaseInfo(param, rowDataList, wbTemplate, sheet, rowNumMap, uomMap, orderQtyMap,
            samePoRowNumMap);

        // Output plan and actual column data
        Map<Integer, CPSSMF01PartEntity> summaryQtyMap = new HashMap<Integer, CPSSMF01PartEntity>();
        int columnNum = this.outputPlanAndActualInfo(param, planAndActualList, new Object[] { uomMap, rowNumMap,
            ipoRowNumMap, summaryQtyMap, samePoRowNumMap }, wbTemplate, sheet, maxRowNum, needModNew);

        if (needModNew) {
            int beforeTodayColumn = CPSSMFXXUtil.calculateBeforeTodayColumn(BEGIN_COLUMN_NUMBER_PLAN_OR_INVOICE,
                planAndActualList, false);
            if (beforeTodayColumn >= BEGIN_COLUMN_NUMBER_PLAN_OR_INVOICE) {
                int rowNum = BEGIN_ROW_NUMBER_PLAN_OR_INVOICE - IntDef.INT_TWO;
                int templateRowNum = IntDef.INT_FIFTY_ONE;
                int templateColumnNum = IntDef.INT_ONE;
                super.copyCell(wbTemplate.getSheet(CPSSMFXXUtil.SHEET_STYLE_CELL), sheet, templateRowNum,
                    templateColumnNum, rowNum, beforeTodayColumn - IntDef.INT_ONE, false);
                rowNum++;
                super.copyCell(wbTemplate.getSheet(CPSSMFXXUtil.SHEET_STYLE_CELL), sheet, templateRowNum,
                    templateColumnNum, rowNum, beforeTodayColumn - IntDef.INT_ONE, false);

                rowNum = maxRowNum;
                super.copyCell(wbTemplate.getSheet(CPSSMFXXUtil.SHEET_STYLE_CELL), sheet, templateRowNum,
                    templateColumnNum, rowNum, beforeTodayColumn - IntDef.INT_ONE, false);
                rowNum++;
                super.copyCell(wbTemplate.getSheet(CPSSMFXXUtil.SHEET_STYLE_CELL), sheet, templateRowNum,
                    templateColumnNum, rowNum, beforeTodayColumn - IntDef.INT_ONE, false);
            }
        }

        // Output qty summary columns
        this.outputSummaryQtyInfo(planInfoList, summaryQtyMap, uomMap, orderQtyMap, wbTemplate, sheet, columnNum,
            maxRowNum);

        // output footer
        this.outputFooter(param, wbTemplate, sheet, maxRowNum);
    }

    /**
     * output footer
     * 
     * @param param param
     * @param wbTemplate wbTemplate
     * @param sheet sheet
     * @param maxRowNum maxRowNum
     */
    private void outputFooter(BaseParam param, Workbook wbTemplate, Sheet sheet, int maxRowNum) {
        // get datetime format and language
        String dateTimeFormat = DateTimeUtil.FORMAT_DDMMMYYYYHHMM;
        Language lang = Language.ENGLISH;
        if (param.getLanguage() == CategoryLanguage.CHINESE) {
            dateTimeFormat = DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMM;
            lang = Language.CHINESE;
        }

        String officeCode = param.getCurrentOfficeCode();

        String downloadTime = DateTimeUtil.formatDate(dateTimeFormat,
            this.cpssmf02Service.getDBDateTime(param.getOfficeTimezone()), lang);

        param.setFilter("officeId", param.getCurrentOfficeId());
        Map<Integer, String> lastSyncTimeMap = this.cpssmf02Service.searchDataSyncTime(param, dateTimeFormat, lang);

        String lastSsmsSyncTime = lastSyncTimeMap.get(SyncTimeDataType.SSMS);
        String lastTTLogicVVTime = lastSyncTimeMap.get(SyncTimeDataType.TT_LOGIX_VV);

        // output
        int srcRowNum = IntDef.INT_FIFTY_THREE;
        int scrColumnNum = IntDef.INT_ONE;
        int rowNum = maxRowNum + IntDef.INT_ONE;
        int columnNum = IntDef.INT_ZERO;

        Sheet sheet_style_cell = wbTemplate.getSheet(CPSSMFXXUtil.SHEET_STYLE_CELL);

        Cell cell = super.copyCell(sheet_style_cell, sheet, srcRowNum, scrColumnNum, rowNum, columnNum, false);
        cell.setCellValue(MessageManager.getMessage("CPSSMF01_Label_DownloadTime") + StringConst.BLANK + downloadTime
                + StringConst.BLANK + "(" + officeCode + ")");
        cell = super.copyCell(sheet_style_cell, sheet, srcRowNum, scrColumnNum, rowNum + IntDef.INT_ONE, columnNum,
            false);
        cell.setCellValue(MessageManager.getMessage("CPSSMF01_Label_LastSsmsDataTime") + StringConst.BLANK
                + lastSsmsSyncTime);
        cell = super.copyCell(sheet_style_cell, sheet, srcRowNum, scrColumnNum, rowNum + IntDef.INT_TWO, columnNum,
            false);
        cell.setCellValue(MessageManager.getMessage("CPSSMF01_Label_LastTime", new String[] { officeCode })
                + StringConst.BLANK + lastTTLogicVVTime);
    }

    /**
     * output qty summary columns
     * 
     * @param uomMap uomMap
     * @param planInfoList planInfoList
     * @param summaryQtyMap summaryQtyMap
     * @param orderQtyMap orderQtyMap
     * @param wbTemplate wbTemplate
     * @param sheet sheet
     * @param columnNum columnNum
     * @param maxRowNum maxRowNum
     */
    private void outputSummaryQtyInfo(List<CPSSMF01Entity> planInfoList,
        Map<Integer, CPSSMF01PartEntity> summaryQtyMap, Map<Integer, String> uomMap,
        Map<Integer, BigDecimal> orderQtyMap, Workbook wbTemplate, Sheet sheet, int columnNum, int maxRowNum) {
        // output cells
        int rowNum = BEGIN_ROW_NUMBER + IntDef.INT_ONE;
        while (rowNum <= maxRowNum) {
            String uom = uomMap.get(rowNum - IntDef.INT_ONE);
            CellStyle cell_gray_qty_style = this.getQtyStyleCell(uom, wbTemplate, CPSSMFXXUtil.SHEET_STYLE_QTY_GRAY);
            for (int i = 0; i < IntDef.INT_THREE; i++) {
                PoiUtil.setCellStyle(sheet, rowNum, columnNum + i, cell_gray_qty_style);
                PoiUtil.setCellValue(sheet, rowNum, columnNum + i, BigDecimal.ZERO);
            }
            rowNum++;
        }

        // output titles
        // total shipment qty
        this.outputMergedQtyTitle(columnNum, IntDef.INT_ELEVEN, wbTemplate, sheet);
        // total shipment balance qty
        this.outputMergedQtyTitle(columnNum + IntDef.INT_ONE, IntDef.INT_THIRTEEN, wbTemplate, sheet);
        // force completed qty
        this.outputMergedQtyTitle(columnNum + IntDef.INT_TWO, IntDef.INT_FIFTEEN, wbTemplate, sheet);

        // output qty
        if (null != summaryQtyMap && !summaryQtyMap.isEmpty()) {
            for (Entry<Integer, CPSSMF01PartEntity> entry : summaryQtyMap.entrySet()) {
                CPSSMF01PartEntity item = entry.getValue();
                BigDecimal etdQty = item.getEtdQty();
                CellStyle cell_gray_qty_style = this.getQtyStyleCell(item.getUom(), wbTemplate,
                    CPSSMFXXUtil.SHEET_STYLE_QTY_GRAY);

                rowNum = entry.getKey();
                // etd qty
                PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_gray_qty_style);
                PoiUtil.setCellValue(sheet, rowNum, columnNum, etdQty);

                // 2016/07/12 shiyang add start (item.getForceCompletedQty() is wrong, reason is not understand)
                for (CPSSMF01Entity e : planInfoList) {
                    if (e.getPartsId().equals(item.getPartsId()) && e.getCustomerOrderNo().equals(item.getCpo())
                            && e.getIpo().equals(item.getIpo()) && e.getCustomerCode().equals(item.getCustomerCode())
                            && e.getEpo().equals(item.getEpo())
                            && e.getTtcSupplierCode().equals(item.getTtcSupplierCode())) {
                        item.setForceCompletedQty(e.getForceCompletedQty());
                        break;
                    }
                }
                // 2016/07/12 shiyang add end

                // etd balance qty
                BigDecimal orderQty = orderQtyMap.get(rowNum - IntDef.INT_ONE);
                PoiUtil.setCellStyle(sheet, rowNum, columnNum + IntDef.INT_ONE, cell_gray_qty_style);
                // 2016/07/04 shiyang mod start
                // PoiUtil.setCellValue(sheet, rowNum, columnNum + IntDef.INT_ONE, DecimalUtil.subtract(orderQty,
                // etdQty));
                if (item.getForceCompletedQty().compareTo(BigDecimal.ZERO) > 0) {
                    PoiUtil.setCellValue(sheet, rowNum, columnNum + IntDef.INT_ONE, BigDecimal.ZERO);
                } else {
                    PoiUtil.setCellValue(sheet, rowNum, columnNum + IntDef.INT_ONE,
                        DecimalUtil.subtract(orderQty, etdQty));
                }
                // 2016/07/04 shiyang mod end

                // force completed qty
                BigDecimal forceCompQty = item.getForceCompletedQty();
                if (forceCompQty.compareTo(BigDecimal.ZERO) > 0) {

                    BigDecimal qtyAfterFc = DecimalUtil.subtract(DecimalUtil.add(etdQty, forceCompQty), orderQty);
                    if (qtyAfterFc.compareTo(forceCompQty) >= 0) {
                        forceCompQty = BigDecimal.ZERO;
                    } else {
                        forceCompQty = DecimalUtil.subtract(forceCompQty, qtyAfterFc);
                    }

                }
                PoiUtil.setCellStyle(sheet, rowNum, columnNum + IntDef.INT_TWO, cell_gray_qty_style);
                PoiUtil.setCellValue(sheet, rowNum, columnNum + IntDef.INT_TWO, forceCompQty);
            }
        }
    }

    /**
     * Output Summary Qty Title
     * 
     * @param columnNum columnNum
     * @param tempSheetRowNum tempSheetRowNum
     * @param wbTemplate wbTemplate
     * @param sheet sheet
     */
    private void outputMergedQtyTitle(int columnNum, int tempSheetRowNum, Workbook wbTemplate, Sheet sheet) {
        int rowNum = BEGIN_ROW_NUMBER_PLAN_OR_INVOICE;
        int columnNo = columnNum - IntDef.INT_ONE;
        sheet.setColumnWidth(columnNo, COLUMN_WIDTH);
        super.copyCell(wbTemplate.getSheet(CPSSMFXXUtil.SHEET_STYLE_CELL_VALUE), sheet, tempSheetRowNum,
            IntDef.INT_ONE, rowNum, columnNo, true);
        rowNum++;
        while (rowNum <= BEGIN_ROW_NUMBER_PLAN_OR_INVOICE + SUMMARY_QTY_TITLE_BLANK_ROW_COUNT) {
            super.copyCell(wbTemplate.getSheet(CPSSMFXXUtil.SHEET_STYLE_CELL_VALUE), sheet, tempSheetRowNum,
                IntDef.INT_ONE, rowNum, columnNo, false);
            rowNum++;
        }

        // merged column
        int fromRow = BEGIN_ROW_NUMBER_PLAN_OR_INVOICE;
        int toRow = fromRow + SUMMARY_QTY_TITLE_BLANK_ROW_COUNT;
        int column = columnNum - IntDef.INT_ONE;
        CellRangeAddress mergedRegion = new CellRangeAddress(fromRow, toRow, column, column);
        sheet.addMergedRegion(mergedRegion);
    }

    /**
     * output plan and invoice information
     * 
     * @param param BaseParam
     * @param planAndActualList planAndActualList
     * @param maps maps
     * @param wbTemplate wbTemplate
     * @param sheet sheet
     * @param maxRowNum maxRowNum
     * @param needModNew needModNew
     * @return outputed columnNum
     */
    private int outputPlanAndActualInfo(BaseParam param, List<CPSSMF01ColEntity> planAndActualList, Object[] maps,
        Workbook wbTemplate, Sheet sheet, int maxRowNum, boolean needModNew) {
        int columnNum = BEGIN_COLUMN_NUMBER_PLAN_OR_INVOICE;
        if (null == planAndActualList || planAndActualList.isEmpty()) {
            return columnNum;
        }

        // set column width
        sheet.setColumnWidth(columnNum - 1, COLUMN_WIDTH);
        int listSize = planAndActualList.size();
        for (int i = 0; i < listSize; i++) {
            CPSSMF01ColEntity item = planAndActualList.get(i);
            boolean isLastEntity = false;
            if (i == listSize - IntDef.INT_ONE) {
                isLastEntity = true;
            }

            // output one column data of plan or actual
            this.outputPlanAndActualData(param, item, maps, wbTemplate, sheet, new int[] { columnNum, maxRowNum },
                needModNew);
            columnNum++;
        }

        return columnNum++;
    }

    /**
     * output one column data of plan or actual
     * 
     * @param param BaseParam
     * @param item CPSSMF01Entity
     * @param maps maps
     * @param wbTemplate Workbook
     * @param sheet sheet
     * @param position position
     * @param needModNew needModNew
     */
    private void outputPlanAndActualData(BaseParam param, CPSSMF01ColEntity item, Object[] maps, Workbook wbTemplate,
        Sheet sheet, int[] position, boolean needModNew) {
        // get param
        int columnNo = position[IntDef.INT_ZERO];
        int maxRowNum = position[IntDef.INT_ONE];
        // get cell style
        CellStyle cell_plan_style = styleCellMap.get(CELL_PLAN).getCellStyle();
        CellStyle cell_actual_style = styleCellMap.get(CELL_ACTUAL).getCellStyle();
        CellStyle cell_white_left_style = styleCellMap.get(CELL_WHITE_LEFT).getCellStyle();
        CellStyle cell_white_center_style = styleCellMap.get(CELL_WHITE_CENTER).getCellStyle();
        CellStyle cell_gray_left_style = styleCellMap.get(CELL_GRAY_LEFT).getCellStyle();
        CellStyle cell_white_date_style = styleCellMap.get(CELL_WHITE_DATE_EN).getCellStyle();
        CellStyle cell_gray_date_style = styleCellMap.get(CELL_GRAY_DATE_EN).getCellStyle();
        if (param.getLanguage() == CategoryLanguage.CHINESE) {
            cell_white_date_style = styleCellMap.get(CELL_WHITE_DATE_CN).getCellStyle();
            cell_gray_date_style = styleCellMap.get(CELL_GRAY_DATE_CN).getCellStyle();
        }

        // get position params
        int rowNum = BEGIN_ROW_NUMBER_PLAN_OR_INVOICE + 1;
        int columnNum = columnNo;
        // set column width
        sheet.setColumnWidth(columnNum - 1, COLUMN_WIDTH);

        // when isPlan column is white, else is gray.
        String invoiceNo = item.getInvoiceNo();
        boolean isPlan = false;
        if (StringUtil.isEmpty(invoiceNo)) {
            isPlan = true;
        }

        /** output plan or actual column begin */
        // invoice number
        PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_white_left_style);
        PoiUtil.setCellValue(sheet, rowNum, columnNum, invoiceNo);

        // transport mode
        rowNum++;
        PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_white_center_style);
        PoiUtil.setCellValue(
            sheet,
            rowNum,
            columnNum,
            CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.TRANSPORT_MODE,
                item.getTransportMode()));

        // etd
        rowNum++;
        PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_white_date_style);
        PoiUtil.setCellValue(sheet, rowNum, columnNum, item.getEtd());

        // eta
        rowNum++;
        PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_white_date_style);
        PoiUtil.setCellValue(sheet, rowNum, columnNum, item.getEta());

        // ccDate
        rowNum++;
        PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_white_date_style);
        PoiUtil.setCellValue(sheet, rowNum, columnNum, item.getCcDate());

        // imp inbound plan date
        rowNum++;

        PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_white_date_style);
        PoiUtil.setCellValue(sheet, rowNum, columnNum, item.getImpInbPlanDate());

        // imp inbound actual date
        rowNum++;
        PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_white_date_style);
        PoiUtil.setCellValue(sheet, rowNum, columnNum, item.getIpmInbActulDate());

        // revision reason
        rowNum++;
        PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_white_left_style);
        PoiUtil.setCellValue(sheet, rowNum, columnNum, item.getRevisionReason());

        // plan or invoice flag
        rowNum++;
        if (isPlan) {
            PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_plan_style);
        } else {
            PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_actual_style);
        }
        PoiUtil.setCellValue(sheet, rowNum, columnNum, CPSSMFXXUtil.generateVersion(isPlan, item.getOriginalVersion(),
            null == item.getRevisionVersion() ? IntDef.INT_ZERO : item.getRevisionVersion(), false));

        // output plan or actual qty infomation
        this.outputPlanOrActualQtyInfo(item, maps, wbTemplate, sheet, new int[] { columnNum, maxRowNum },
            new boolean[] { isPlan, needModNew });
        columnNum++;
        /** output plan or actual column end */

    }

    /**
     * output plan or actual qty infomation column
     * 
     * @param item CPSSMF01Entity
     * @param maps maps
     * @param wbTemplate wbTemplate
     * @param sheet sheet
     * @param position position.
     * @param flag flag[0]:isPlan,flag[1]:isModColumn.
     */
    private void outputPlanOrActualQtyInfo(CPSSMF01ColEntity item, Object[] maps, Workbook wbTemplate, Sheet sheet,
        int[] position, boolean[] flag) {
        // get param
        Map<Integer, String> uomMap = (Map<Integer, String>) maps[IntDef.INT_ZERO];
        Map<String, Integer> rowNumMap = (Map<String, Integer>) maps[IntDef.INT_ONE];
        Map<String, Integer[]> ipoRowNumMap = (Map<String, Integer[]>) maps[IntDef.INT_TWO];
        Map<Integer, CPSSMF01PartEntity> summaryQtyMap = (Map<Integer, CPSSMF01PartEntity>) maps[IntDef.INT_THREE];
        Map<String, List<Integer>> samePoRowNumMap = (Map<String, List<Integer>>) maps[IntDef.INT_FOUR];

        // get position
        int columnNum = position[IntDef.INT_ZERO];
        int maxRowNum = position[IntDef.INT_ONE];

        // get flag
        boolean isPlan = flag[IntDef.INT_ZERO];
        boolean needModNew = flag[IntDef.INT_ONE];

        // get ipo begin end rowNum when needModNew is false
        int ipoBeginRowNum = IntDef.INT_ZERO;
        int ipoEndRowNum = IntDef.INT_ONE;
        if (!needModNew) {
            String ipoRowNumKey = item.getIpo() + StringConst.COMMA + item.getCustomerOrderNo() + StringConst.COMMA
                    + item.getCustomerCode();
            Integer[] ipoRowNums = ipoRowNumMap.get(ipoRowNumKey);
            ipoBeginRowNum = ipoRowNums[IntDef.INT_ZERO] + IntDef.INT_ONE;
            ipoEndRowNum = ipoRowNums[IntDef.INT_ONE] + IntDef.INT_ONE;
        }

        int rowNum = BEGIN_ROW_NUMBER + IntDef.INT_ONE;

        List<Integer> rowNumLst = null;
        if (!isPlan) {

            rowNumLst = new ArrayList<Integer>();
            List<CPSSMF01PartEntity> partsList = item.getPartsList();
            if (null != partsList && !partsList.isEmpty()) {
                String key = null;
                for (CPSSMF01PartEntity entity : partsList) {

                    if (StringUtil.isEmpty(key)
                            || !(entity.getIpo() + StringConst.COMMA + entity.getCpo() + StringConst.COMMA + entity
                                .getCustomerCode()).endsWith(key)) {

                        key = entity.getIpo() + StringConst.COMMA + entity.getCpo() + StringConst.COMMA
                                + entity.getCustomerCode();
                        List<Integer> poRowNumLst = samePoRowNumMap.get(key);
                        rowNumLst.addAll(poRowNumLst);
                    }
                }
            }
        }
        while (rowNum <= maxRowNum) {
            boolean sameIpo = true;
            String tempSheetName = CPSSMFXXUtil.SHEET_STYLE_QTY_WHITE;
            if (!needModNew) {
                if (rowNum < ipoBeginRowNum || rowNum > ipoEndRowNum) {
                    if (null == rowNumLst || !rowNumLst.contains(new Integer(rowNum))) {
                        tempSheetName = CPSSMFXXUtil.SHEET_STYLE_QTY_GRAY;
                    }
                    sameIpo = false;
                }
            }

            String uom = uomMap.get(rowNum - IntDef.INT_ONE);
            CellStyle cell_white_qty_style = this.getQtyStyleCell(uom, wbTemplate, tempSheetName);
            PoiUtil.setCellStyle(sheet, rowNum, columnNum, cell_white_qty_style);
            if (!sameIpo) {
                if (null != rowNumLst && rowNumLst.contains(new Integer(rowNum))) {
                    PoiUtil.setCellValue(sheet, rowNum, columnNum, BigDecimal.ZERO);
                } else {
                    PoiUtil.setCellValue(sheet, rowNum, columnNum, StringConst.EMPTY);
                }
            } else {
                PoiUtil.setCellValue(sheet, rowNum, columnNum, BigDecimal.ZERO);
            }

            rowNum++;
        }

        // output parts qty
        List<CPSSMF01PartEntity> partsList = item.getPartsList();
        if (null != partsList && !partsList.isEmpty()) {
            for (CPSSMF01PartEntity entity : partsList) {
                // get row number
                // String key = item.getIpo() + StringConst.COMMA + item.getCustomerOrderNo() + StringConst.COMMA
                String key = entity.getIpo() + StringConst.COMMA + entity.getCpo() + StringConst.COMMA
                        + entity.getCustomerCode() + StringConst.COMMA + entity.getPartsId() + StringConst.COMMA
                        + entity.getTtcSupplierCode() + StringConst.COMMA + entity.getEpo();
                Integer value = rowNumMap.get(key);
                if (null == value) {
                    continue;
                }
                rowNum = value + 1;

                // record summary qty
                CPSSMF01PartEntity summaryEntity = new CPSSMF01PartEntity();
                if (summaryQtyMap.containsKey(rowNum)) {
                    summaryEntity = summaryQtyMap.get(rowNum);
                } else {
                    summaryEntity.setEtdBalanceQty(BigDecimal.ZERO);
                    summaryEntity.setEtdQty(BigDecimal.ZERO);
                    BigDecimal forceCompletedQty = entity.getForceCompletedQty();
                    if (null == forceCompletedQty) {
                        summaryEntity.setForceCompletedQty(BigDecimal.ZERO);
                    } else {
                        summaryEntity.setForceCompletedQty(forceCompletedQty);
                    }
                    summaryEntity.setPartsId(entity.getPartsId());
                    summaryEntity.setUom(entity.getUom());
                    // add key info
                    summaryEntity.setCpo(entity.getCpo());
                    summaryEntity.setIpo(entity.getIpo());
                    summaryEntity.setEpo(entity.getEpo());
                    summaryEntity.setTtcSupplierCode(entity.getTtcSupplierCode());
                    summaryEntity.setCustomerCode(entity.getCustomerCode());
                }

                // get cell style
                String uom = entity.getUom();
                CellStyle cellStyle = this.getQtyStyleCell(uom, wbTemplate, CPSSMFXXUtil.SHEET_STYLE_QTY_WHITE);

                // output qty cell
                PoiUtil.setCellStyle(sheet, rowNum, columnNum, cellStyle);
                PoiUtil.setCellValue(sheet, rowNum, columnNum, entity.getQty());

                if (isPlan) {
                    BigDecimal etdBalanceQty = summaryEntity.getEtdBalanceQty();
                    summaryEntity.setEtdBalanceQty(DecimalUtil.add(etdBalanceQty, entity.getQty()));
                } else {
                    BigDecimal etdQty = summaryEntity.getEtdQty();
                    summaryEntity.setEtdQty(DecimalUtil.add(etdQty, entity.getQty()));
                }
                summaryQtyMap.put(rowNum, summaryEntity);
            }
        }
    }

    /**
     * output parts base information
     * 
     * @param param BaseParam
     * @param planInfoList planInfoList
     * @param wbTemplate wbTemplate
     * @param sheet sheet
     * @param rowNumMap rowNumMap
     * @param uomMap uomMap
     * @param orderQtyMap orderQtyMap
     * @param samePoRowNumMap samePoRowNumMap
     * @return maxRowNum
     */
    private int outputBaseInfo(BaseParam param, List<CPSSMF01Entity> planInfoList, Workbook wbTemplate, Sheet sheet,
        Map<String, Integer> rowNumMap, Map<Integer, String> uomMap, Map<Integer, BigDecimal> orderQtyMap,
        Map<String, List<Integer>> samePoRowNumMap) {
        int rowNum = BEGIN_ROW_NUMBER;

        // get style cell
        Cell new_cell = styleCellMap.get(CELL_NEW_CELL);
        Cell cell_white_center = styleCellMap.get(CELL_WHITE_CENTER);
        Cell cell_white_date = styleCellMap.get(CELL_WHITE_DATE_EN);
        if (param.getLanguage() == CategoryLanguage.CHINESE) {
            cell_white_date = styleCellMap.get(CELL_WHITE_DATE_CN);
        }

        List<Integer> rowNumLst = null;
        for (int i = 0; i < planInfoList.size(); i++) {
            CPSSMF01Entity item = planInfoList.get(i);
            Cell cell_white_qty = styleCellMap.get(CELL_WHITE_RIGHT);
            cell_white_qty.setCellStyle(this.getQtyStyleCell(item.getUom(), wbTemplate,
                CPSSMFXXUtil.SHEET_STYLE_QTY_WHITE));

            // Cell Style
            Cell[] rowStyle = new Cell[] { new_cell, new_cell, new_cell, cell_white_center, new_cell,
                cell_white_center, new_cell, new_cell, new_cell, new_cell, new_cell, new_cell, cell_white_center,
                cell_white_center, cell_white_qty, cell_white_date, cell_white_qty };

            // output data
            Object[] rowData = new Object[] {
                item.getTtcPartsNo(),
                item.getPartsNameEn(),
                item.getPartsNameCn(),
                item.getExpRegion(),
                item.getTtcSupplierCode(),
                item.getImpRegion(),
                item.getCustomerCode(),
                item.getCustomerPartsNo(),
                item.getCustomerOrderNo(),
                item.getCustBackNo(),
                item.getIpo(),
                item.getEpo(),
                item.getOrderType(),
                CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P,
                    item.getDelayAdjustmentPattern()), item.getSpq(), item.getExpSoDate(), item.getOrderQty() };

            // output
            super.createOneDataRowByTemplate(sheet, rowNum, rowStyle, rowData);

            // map the rowNum of current ipo parts
            String key = item.getIpo() + StringConst.COMMA + item.getCustomerOrderNo() + StringConst.COMMA
                    + item.getCustomerCode() + StringConst.COMMA + item.getPartsId() + StringConst.COMMA
                    + item.getTtcSupplierCode() + StringConst.COMMA + item.getEpo();
            rowNumMap.put(key, rowNum);
            String poKey = item.getIpo() + StringConst.COMMA + item.getCustomerOrderNo() + StringConst.COMMA
                    + item.getCustomerCode();
            if (null != samePoRowNumMap.get(poKey)) {
                rowNumLst = samePoRowNumMap.get(poKey);
                rowNumLst.add(rowNum + 1);
                samePoRowNumMap.put(poKey, rowNumLst);
            } else {
                rowNumLst = new ArrayList<Integer>();
                rowNumLst.add(rowNum + 1);
                samePoRowNumMap.put(poKey, rowNumLst);
            }
            // map row number's uom
            uomMap.put(rowNum, item.getUom());
            // map order qty
            orderQtyMap.put(rowNum, item.getOrderQty());

            // rowNum count
            rowNum++;
        }
        return rowNum;
    }

    /**
     * get decimal cell style by uom
     * 
     * @param uom uom
     * @param wbTemplate Workbook
     * @param tempSheetName tempSheetName
     * @return CellStyle
     */
    private CellStyle getQtyStyleCell(String uom, Workbook wbTemplate, String tempSheetName) {
        int uomDigits = MasterManager.getUomDigits(uom);
        return this.getDecimalStyle(wbTemplate, tempSheetName, uomDigits);
    }

    /**
     * init Style Cell Map
     * 
     * @param param BaseParam
     * @param wbTemplate Workbook
     * @return Style Cell Map
     */
    private Map<String, Cell> initStyleCell(BaseParam param, Workbook wbTemplate) {
        Map<String, Cell> map = new HashMap<String, Cell>();

        String sheetName = CPSSMFXXUtil.SHEET_STYLE_CELL;
        int rowNum = IntDef.INT_ONE;
        int columnNum = IntDef.INT_ONE;
        int columnNumCn = IntDef.INT_THREE;

        Cell cell_plan = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_PLAN, cell_plan);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_actual = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_ACTUAL, cell_actual);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_left = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_LEFT, cell_gray_left);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_left_most_right = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_LEFT_MOST_RIGHT, cell_gray_left_most_right);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_left_most_bottom = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_LEFT_MOST_BOTTOM, cell_gray_left_most_bottom);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_left_last = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_LEFT_LAST, cell_gray_left_last);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_center = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_CENTER, cell_gray_center);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_center_most_right = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_CENTER_MOST_RIGHT, cell_gray_center_most_right);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_center_most_bottom = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_CENTER_MOST_BOTTOM, cell_gray_center_most_bottom);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_center_last = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_CENTER_LAST, cell_gray_center_last);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_right = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_RIGHT, cell_gray_right);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_date_en = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_DATE_EN, cell_gray_date_en);
        Cell cell_gray_date_cn = super.getTemplateCell(sheetName, rowNum, columnNumCn, wbTemplate);
        map.put(CELL_GRAY_DATE_CN, cell_gray_date_cn);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_date_most_right_en = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_DATE_MOST_RIGHT_EN, cell_gray_date_most_right_en);
        Cell cell_gray_date_most_right_cn = super.getTemplateCell(sheetName, rowNum, columnNumCn, wbTemplate);
        map.put(CELL_GRAY_DATE_MOST_RIGHT_CN, cell_gray_date_most_right_cn);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_date_most_bottom_en = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_DATE_MOST_BOTTOM_EN, cell_gray_date_most_bottom_en);
        Cell cell_gray_date_most_bottom_cn = super.getTemplateCell(sheetName, rowNum, columnNumCn, wbTemplate);
        map.put(CELL_GRAY_DATE_MOST_BOTTOM_CN, cell_gray_date_most_bottom_cn);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_gray_date_last_en = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_GRAY_DATE_LAST_EN, cell_gray_date_last_en);
        Cell cell_gray_date_last_cn = super.getTemplateCell(sheetName, rowNum, columnNumCn, wbTemplate);
        map.put(CELL_GRAY_DATE_LAST_CN, cell_gray_date_last_cn);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_white_left = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_WHITE_LEFT, cell_white_left);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_white_left_most_right = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_WHITE_LEFT_MOST_RIGHT, cell_white_left_most_right);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_white_left_most_bottom = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_WHITE_LEFT_MOST_BOTTOM, cell_white_left_most_bottom);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_white_left_last = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_WHITE_LEFT_LAST, cell_white_left_last);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_white_center = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_WHITE_CENTER, cell_white_center);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_white_right = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_WHITE_RIGHT, cell_white_right);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_white_date_en = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_WHITE_DATE_EN, cell_white_date_en);
        Cell cell_white_date_cn = super.getTemplateCell(sheetName, rowNum, columnNumCn, wbTemplate);
        map.put(CELL_WHITE_DATE_CN, cell_white_date_cn);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_white_date_most_right_en = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_WHITE_DATE_MOST_RIGHT_EN, cell_white_date_most_right_en);
        Cell cell_white_date_most_right_cn = super.getTemplateCell(sheetName, rowNum, columnNumCn, wbTemplate);
        map.put(CELL_WHITE_DATE_MOST_RIGHT_CN, cell_white_date_most_right_cn);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_white_date_most_bottom_en = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_WHITE_DATE_MOST_BOTTOM_EN, cell_white_date_most_bottom_en);
        Cell cell_white_date_most_bottom_cn = super.getTemplateCell(sheetName, rowNum, columnNumCn, wbTemplate);
        map.put(CELL_WHITE_DATE_MOST_BOTTOM_CN, cell_white_date_most_bottom_cn);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_white_date_last_en = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_WHITE_DATE_LAST_EN, cell_white_date_last_en);
        Cell cell_white_date_last_cn = super.getTemplateCell(sheetName, rowNum, columnNumCn, wbTemplate);
        map.put(CELL_WHITE_DATE_LAST_CN, cell_white_date_last_cn);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_before_today = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_BEFORE_TODAY, cell_before_today);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_download_time_en = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_DOWNLOAD_TIME_EN, cell_download_time_en);
        Cell cell_download_time_cn = super.getTemplateCell(sheetName, rowNum, columnNumCn, wbTemplate);
        map.put(CELL_DOWNLOAD_TIME_CN, cell_download_time_cn);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_last_ttlogic_time_en = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_LAST_TTLOGIC_TIME_EN, cell_last_ttlogic_time_en);
        Cell cell_last_ttlogic_time_cn = super.getTemplateCell(sheetName, rowNum, columnNumCn, wbTemplate);
        map.put(CELL_LAST_TTLOGIC_TIME_CN, cell_last_ttlogic_time_cn);
        rowNum = rowNum + IntDef.INT_TWO;
        Cell cell_new_cell = super.getTemplateCell(sheetName, rowNum, columnNum, wbTemplate);
        map.put(CELL_NEW_CELL, cell_new_cell);
        return map;
    }

    /**
     * Get Month Difference
     * 
     * @param custStartMonth String
     * @param custEndMonth String
     * @return difference
     * @throws ParseException E
     */
    public boolean getDateDifference(String custStartMonth, String custEndMonth) throws ParseException {
        SimpleDateFormat formatYMD = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(formatYMD.parse(custStartMonth));
        c.add(Calendar.MONTH, IntDef.INT_SIX);
        Date afterDate = c.getTime();
        if (Integer.valueOf(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, afterDate)) < Integer
            .valueOf(custEndMonth)) {
            return true;
        }

        return false;
    }
}