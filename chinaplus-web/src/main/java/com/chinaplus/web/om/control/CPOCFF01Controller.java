/**
 * CPOCFF01Controller.java
 * 
 * @screen CPOCFF01
 * @author li_feng
 */
package com.chinaplus.web.om.control;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOCFF01Entity;
import com.chinaplus.web.om.service.CPOCFF01Service;

/**
 * Customer Stock DownLoad Screen Controller.
 */
@Controller
public class CPOCFF01Controller extends BaseFileController {

    /** download file name */
    private static final String DL_FILE_NAME_MONTH = "Customer Forecast by Month";
    /** download file name */
    private static final String DL_FILE_NAME_DAY = "Customer Forecast by Day";
    /** by month */
    private static final String BY_MONTH = "ByMonth";
    /** by day */
    private static final String BY_DAY = "ByDay";
    /** six */
    private static final Integer SIX = 6;
    /** three */
    private static final Integer THREE = 3;
    /** business pattern V-V No */
    private static final Integer BSPTN_VV_NO = 1;
    /** business pattern AISIN NO */
    private static final Integer BSPTN_AISIN_NO = 2;
    /** col width */
    private static final Integer FIVE_THOUSAND = 5000;
    /** v_v sheet */
    private static final String SHEET_VV = "OutputSample(V-V)";
    /** aisin sheet */
    private static final String SHEET_AISIN = "OutputSample(AISIN)";
    /** style sheet */
    private static final String SHEET_STYLE = "style";
    /** separator */
    private static final String KEY_SEPARATOR = "***separator***";

    /** Download Customer Forecast Blank Form Screen Service */
    @Autowired
    private CPOCFF01Service service;

    /**
     * Download Customer Stock download check
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     * @throws ParseException E
     */
    @RequestMapping(value = "/om/CPOCFF01/custdownloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> custdownloadcheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) throws ParseException {

        BaseResult<String> result = new BaseResult<String>();

        if ((boolean) param.getSwapData().get("needCheck")) {
            param.setFilters(param.getSwapData());
            // set common parameters by session
            this.setCommonParam(param, request);
            Locale lang = MessageManager.getLanguage(param.getLanguage()).getLocale();
            String custCode = StringUtil.toSafeString(param.getSwapData().get("customerCode"));
            String custStartMonth = StringUtil.toSafeString(param.getSwapData().get("custStartMonth"));
            String custEndMonth = StringUtil.toSafeString(param.getSwapData().get("custEndMonth"));
            String downLoadBy = StringUtil.toSafeString(param.getSwapData().get("downLoadBy"));
            String Msg = null;
            String Msg2 = null;
            SimpleDateFormat formatYM = new SimpleDateFormat("yyyyMM");

            // customerCode is not empty check
            if (StringUtil.isEmpty(custCode)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
                message.setMessageArgs(new String[] { "CPOCFS03_Label_TTCCustomerCode" });
                result.addMessage(message);
            }

            // start month and end month is not empty check
            if (!StringUtil.isEmpty(custStartMonth) && !StringUtil.isEmpty(custEndMonth)) {
                // 3-1-1 show error if customr forecast start month after the end month.
                if (StringUtil.toSafeInteger(custEndMonth) < StringUtil.toSafeInteger(custStartMonth)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                    Msg = MessageManager.getMessage("CPOCFS03_Start_Month", lang);
                    Msg2 = MessageManager.getMessage("CPOCFS03_End_Month", lang);
                    message.setMessageArgs(new String[] { Msg, Msg2 });
                    result.addMessage(message);
                } else {
                    // 3-1-3 for by month ,show error if customr forecast month over 6 months.
                    Calendar c = Calendar.getInstance();
                    c.setTime(formatYM.parse(custEndMonth));
                    int endYear = c.get(Calendar.YEAR);
                    int endMonth = c.get(Calendar.MONTH);
                    c.setTime(formatYM.parse(custStartMonth));
                    int startYear = c.get(Calendar.YEAR);
                    int startMonth = c.get(Calendar.MONTH);
                    int month;
                    if (endYear == endMonth) {
                        month = endMonth - startMonth;
                    } else {
                        month = IntDef.INT_TWELVE * (endYear - startYear) + endMonth - startMonth;
                    }

                    if (BY_MONTH.equals(downLoadBy)) {
                        if (month > IntDef.INT_FIVE) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_003);
                            message.setMessageArgs(new String[] { SIX.toString() });
                            result.addMessage(message);
                        }
                    }
                    // 3-1-4 for by day ,show error if customr forecast month over 3 months.
                    if (BY_DAY.equals(downLoadBy)) {
                        if (month > IntDef.INT_TWO) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_003);
                            message.setMessageArgs(new String[] { THREE.toString() });
                            result.addMessage(message);
                        }
                    }
                    if (!StringUtil.isEmpty(custCode)) {
                        // 3-1-5 show error if customr forecast start month after the last update forecast month.
                        List<CPOCFF01Entity> list = service.getLastUpdateForecastMonth(param);
                        for (CPOCFF01Entity entity : list) {
                            if (StringUtil.toSafeInteger(custStartMonth) > StringUtil.toSafeInteger(entity.getLastFcMonth())+1) {
                                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_007);
                                message
                                    .setMessageArgs(new String[] { entity.getLastFcMonth(), entity.getCustomercode() });
                                result.addMessage(message);
                            }
                        }
                    }

                    if (!StringUtil.isEmpty(custCode)) {
                        // show if endMonthDay is undefined
                        Map<String, String> map = service.getCustomerCalendarEndDateMap(param);
                        List<String> customercodeList = new ArrayList<String>();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(DateTimeUtil.parseMonth(custEndMonth));
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                        String[] cusrCodeArray = custCode.split(",");
                        for (int i = 0; i < cusrCodeArray.length; i++) {
                            String custCodeStr = service.getCustCode(Integer.valueOf(cusrCodeArray[i]));
                            String mapKey = new StringBuilder().append(custCodeStr).append(KEY_SEPARATOR)
                                .append(calendar.getTime()).toString();
                            if (map.get(mapKey) == null) {
                                customercodeList.add(custCodeStr);
                            }
                        }

                        if (!customercodeList.isEmpty()) {
                            StringBuilder parts = new StringBuilder();
                            if (customercodeList != null && customercodeList.size() > 0) {
                                for (int i = 0; i < customercodeList.size(); i++) {
                                    if (i < customercodeList.size() - 1) {
                                        parts.append(customercodeList.get(i) + ",");
                                    } else {
                                        parts.append(customercodeList.get(i));
                                    }
                                }
                            }
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_027);
                            message.setMessageArgs(new String[] { parts.toString() });
                            result.addMessage(message);
                        }
                    }
                }

                if (!StringUtil.isEmpty(custCode)) {
                    // 3-1-2 show error if customr forecast start month before current month.
                    String monthNow = formatYM.format(service.getDBDateTimeByDefaultTimezone()).toString();
                    if (StringUtil.toSafeInteger(custStartMonth) < StringUtil.toSafeInteger(monthNow)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1003_008);
                        result.addMessage(message);
                    }
                }

            } else {
                if (StringUtil.isEmpty(custStartMonth)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
                    message.setMessageArgs(new String[] { "CPOCFS03_Start_Month" });
                    result.addMessage(message);
                }
                if (StringUtil.isEmpty(custEndMonth)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
                    message.setMessageArgs(new String[] { "CPOCFS03_End_Month" });
                    result.addMessage(message);
                }

            }
        }

        return result;
    }

    /**
     * Download Customer Stock download
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "om/CPOCFF01/download",
        method = RequestMethod.POST)
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response) {

        this.setCommonParam(param, request);
        String custStartMonth = StringUtil.toSafeString(param.getSwapData().get("custStartMonth"));
        String custEndMonth = StringUtil.toSafeString(param.getSwapData().get("custEndMonth"));
        String filename = DL_FILE_NAME_DAY + "_" + custStartMonth + "-" + custEndMonth + ".xlsx";
        if (param.getSwapData().get("downLoadBy").equals(BY_MONTH)) {
            filename = DL_FILE_NAME_MONTH + "_" + custStartMonth + "-" + custEndMonth + ".xlsx";
        }
        this.downloadExcelWithTemplate(StringUtil.formatMessage(filename, param.getClientTime()), param, request,
            response);
    }

    /**
     * Write To Customer Stock Excel
     * 
     * @param param T
     * @param wbTemplate Workbook
     * @param wbOutput SXSSFWorkbook
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

        Map<String, Integer> calendarMap = new HashMap<String, Integer>();
        Map<Integer, String> sheetMap = new HashMap<Integer, String>();
        Map<String, String> customercodeMap = new LinkedHashMap<String, String>();
        Map<String, String> sheetcalendarMap = new HashMap<String, String>();
        Map<String, ArrayList<String>> sheetListMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> workflgList = new ArrayList<String>();
        String toSheet = "";
        Sheet toSheetName;
        String businessPattern;
        int col = IntDef.INT_ELEVEN;
        @SuppressWarnings("unused")
        int row = IntDef.INT_TEN;
        String calendarMapKey = null;
        // get style sheet
        Sheet styleSheetName = wbTemplate.getSheet(SHEET_STYLE);
        Sheet vvSheetName = wbTemplate.getSheet(SHEET_VV);
        Sheet aisinSheetName = wbTemplate.getSheet(SHEET_AISIN);

        if (BY_DAY.equals(param.getSwapData().get("downLoadBy").toString())) {
            // get calendar list by day
            List<CPOCFF01Entity> list = service.getCustomerCalendar(param);
            ArrayList<String> writeCustomercodeList = new ArrayList<String>();
            for (CPOCFF01Entity entity : list) {

                if (null == calendarMap.get(new StringBuilder().append(entity.getBusinessPattern())
                    .append(KEY_SEPARATOR).append(entity.getCalendarId()).toString())) {

                    if (BSPTN_VV_NO.equals(entity.getBusinessPattern())) {
                        businessPattern = CodeCategoryManager.getCodeName(param.getLanguage(),
                            CodeMasterCategory.BUSINESS_PATTERN, BusinessPattern.V_V);

                        toSheet = entity.getCustomercode() + "(" + businessPattern + ")";
                        sheetMap.put(entity.getBusinessPattern(), toSheet);

                        calendarMapKey = new StringBuilder().append(entity.getCalendarId()).append(KEY_SEPARATOR)
                            .append(entity.getBusinessPattern()).toString();
                        customercodeMap.put(entity.getCustomercode(), calendarMapKey);
                        sheetcalendarMap.put(toSheet, calendarMapKey);

                        cloneSheetByTemplate(wbTemplate, toSheet, SHEET_VV);
                        // get work sheet
                        toSheetName = wbTemplate.getSheet(toSheet);
                        // set calendar date style
                        if (entity.getWorkingFlag().equals(0)) {
                            PoiUtil.setCellStyle(toSheetName, IntDef.INT_NINE, IntDef.INT_ELEVEN, PoiUtil
                                .getOrCreateCell(styleSheetName, IntDef.INT_TWO, IntDef.INT_ELEVEN).getCellStyle());
                            PoiUtil.setCellStyle(toSheetName, IntDef.INT_TEN, IntDef.INT_ELEVEN, PoiUtil
                                .getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_ELEVEN).getCellStyle());
                        } else {
                            PoiUtil.setCellStyle(toSheetName, IntDef.INT_NINE, IntDef.INT_ELEVEN, PoiUtil
                                .getOrCreateCell(styleSheetName, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                            PoiUtil.setCellStyle(toSheetName, IntDef.INT_TEN, IntDef.INT_ELEVEN, PoiUtil
                                .getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_TWELVE).getCellStyle());
                        }
                        // set calendar date value
                        if (param.getLanguage() == IntDef.INT_TWO) {
                            PoiUtil.setCellValue(toSheetName, IntDef.INT_NINE, IntDef.INT_ELEVEN,
                                DateTimeUtil.formatDate("yyyy-MM-dd", entity.getCalendarDate()));
                        } else {
                            PoiUtil.setCellValue(toSheetName, IntDef.INT_NINE, IntDef.INT_ELEVEN,
                                DateTimeUtil.formatDate("dd MMM yyyy", entity.getCalendarDate()));
                        }

                        PoiUtil.setCellValue(toSheetName, IntDef.INT_SEVEN, IntDef.INT_THREE, businessPattern);
                        toSheetName.setColumnWidth(IntDef.INT_TEN, FIVE_THOUSAND);

                        col = IntDef.INT_ELEVEN;
                        row = IntDef.INT_TEN;
                        writeCustomercodeList.add(entity.getCustomercode());

                    } else if (BSPTN_AISIN_NO.equals(entity.getBusinessPattern())) {
                        businessPattern = CodeCategoryManager.getCodeName(param.getLanguage(),
                            CodeMasterCategory.BUSINESS_PATTERN, BusinessPattern.AISIN);

                        toSheet = entity.getCustomercode() + "(" + businessPattern + ")";
                        sheetMap.put(entity.getBusinessPattern(), toSheet);

                        calendarMapKey = new StringBuilder().append(entity.getCalendarId()).append(KEY_SEPARATOR)
                            .append(entity.getBusinessPattern()).toString();
                        customercodeMap.put(entity.getCustomercode(), calendarMapKey);
                        sheetcalendarMap.put(toSheet, calendarMapKey);

                        cloneSheetByTemplate(wbTemplate, toSheet, SHEET_AISIN);
                        // get work sheet
                        toSheetName = wbTemplate.getSheet(toSheet);
                        // set calendar date style
                        if (entity.getWorkingFlag().equals(0)) {
                            PoiUtil.setCellStyle(toSheetName, IntDef.INT_NINE, IntDef.INT_ELEVEN, PoiUtil
                                .getOrCreateCell(styleSheetName, IntDef.INT_TWO, IntDef.INT_ELEVEN).getCellStyle());
                            PoiUtil.setCellStyle(toSheetName, IntDef.INT_TEN, IntDef.INT_ELEVEN, PoiUtil
                                .getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_ELEVEN).getCellStyle());
                        } else {
                            PoiUtil.setCellStyle(toSheetName, IntDef.INT_NINE, IntDef.INT_ELEVEN, PoiUtil
                                .getOrCreateCell(styleSheetName, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                            PoiUtil.setCellStyle(toSheetName, IntDef.INT_TEN, IntDef.INT_ELEVEN, PoiUtil
                                .getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_TWELVE).getCellStyle());
                        }
                        // set calendar date value
                        if (param.getLanguage() == IntDef.INT_TWO) {
                            PoiUtil.setCellValue(toSheetName, IntDef.INT_NINE, IntDef.INT_ELEVEN,
                                DateTimeUtil.formatDate("yyyy-MM-dd", entity.getCalendarDate()));
                        } else {
                            PoiUtil.setCellValue(toSheetName, IntDef.INT_NINE, IntDef.INT_ELEVEN,
                                DateTimeUtil.formatDate("dd MMM yyyy", entity.getCalendarDate()));
                        }
                        PoiUtil.setCellValue(toSheetName, IntDef.INT_SEVEN, IntDef.INT_THREE, businessPattern);
                        toSheetName.setColumnWidth(IntDef.INT_TEN, FIVE_THOUSAND);

                        col = IntDef.INT_ELEVEN;
                        row = IntDef.INT_TEN;
                        writeCustomercodeList.add(entity.getCustomercode());
                    }

                    sheetListMap.put(toSheet, workflgList);

                } else {

                    for (int i = IntDef.INT_ZERO; i < writeCustomercodeList.size(); i++) {
                        calendarMapKey = new StringBuilder().append(entity.getCalendarId()).append(KEY_SEPARATOR)
                            .append(entity.getBusinessPattern()).toString();
                        customercodeMap.put(entity.getCustomercode(), calendarMapKey);
                        if (writeCustomercodeList.get(i).equals(entity.getCustomercode())) {
                            // get work sheet
                            toSheetName = wbTemplate.getSheet(sheetMap.get(entity.getBusinessPattern()));
                            // set calendar date style
                            if (entity.getWorkingFlag().equals(0)) {
                                PoiUtil.setCellStyle(toSheetName, IntDef.INT_NINE, col + 1,
                                    PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_TWO, IntDef.INT_ELEVEN)
                                        .getCellStyle());
                                PoiUtil.setCellStyle(toSheetName, IntDef.INT_TEN, col + 1,
                                    PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_ELEVEN)
                                        .getCellStyle());
                            } else {
                                PoiUtil.setCellStyle(toSheetName, IntDef.INT_NINE, col + 1,
                                    PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_TWO, IntDef.INT_TWELVE)
                                        .getCellStyle());
                                PoiUtil.setCellStyle(toSheetName, IntDef.INT_TEN, col + 1,
                                    PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_TWELVE)
                                        .getCellStyle());
                            }
                            // set calendar date value
                            if (param.getLanguage() == IntDef.INT_TWO) {
                                PoiUtil.setCellValue(toSheetName, IntDef.INT_NINE, col + 1,
                                    DateTimeUtil.formatDate("yyyy-MM-dd", entity.getCalendarDate()));
                            } else {
                                PoiUtil.setCellValue(toSheetName, IntDef.INT_NINE, col + 1,
                                    DateTimeUtil.formatDate("dd MMM yyyy", entity.getCalendarDate()));
                            }
                            toSheetName.setColumnWidth(col, FIVE_THOUSAND);
                            col++;
                        }
                    }

                }
                String key = new StringBuilder().append(entity.getBusinessPattern()).append(KEY_SEPARATOR)
                    .append(entity.getCalendarId()).toString();

                calendarMap.put(key, entity.getCalendarId());
            }

            // set customercode
            for (Map.Entry<String, String> sheetcalendarentry : sheetcalendarMap.entrySet()) {
                String writetoSheet = sheetcalendarentry.getKey();
                // get work sheet
                toSheetName = wbTemplate.getSheet(writetoSheet);
                int maxRow = toSheetName.getLastRowNum() + 1;
                for (Map.Entry<String, String> customerentry : customercodeMap.entrySet()) {
                    if (customerentry.getValue().equals(sheetcalendarentry.getValue())) {
                        int maxCol = toSheetName.getRow(IntDef.INT_EIGHT).getLastCellNum();
                        int styleMaxCol = IntDef.INT_ELEVEN;
                        for (int i = 1; i < styleMaxCol; i++) {
                            PoiUtil.setCellStyle(toSheetName, maxRow, i,
                                PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, i).getCellStyle());
                            PoiUtil.setCellValue(toSheetName, maxRow, IntDef.INT_THREE, customerentry.getKey());
                            PoiUtil.setCellStyle(toSheetName, maxRow + 1, i,
                                PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, i).getCellStyle());
                            PoiUtil.setCellValue(toSheetName, maxRow + 1, IntDef.INT_THREE, customerentry.getKey());
                            PoiUtil.setCellStyle(toSheetName, maxRow + IntDef.INT_TWO, i,
                                PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, i).getCellStyle());
                            PoiUtil.setCellValue(toSheetName, maxRow + IntDef.INT_TWO, IntDef.INT_THREE,
                                customerentry.getKey());
                        }

                        workflgList = sheetListMap.get(toSheetName);
                        for (int i = styleMaxCol; i <= maxCol; i++) {
                            PoiUtil.setCellStyle(toSheetName, maxRow, i,
                                PoiUtil.getOrCreateCell(toSheetName, IntDef.INT_TEN, i).getCellStyle());
                            PoiUtil.setCellStyle(toSheetName, maxRow + 1, i,
                                PoiUtil.getOrCreateCell(toSheetName, IntDef.INT_TEN, i).getCellStyle());
                            PoiUtil.setCellStyle(toSheetName, maxRow + IntDef.INT_TWO, i,
                                PoiUtil.getOrCreateCell(toSheetName, IntDef.INT_TEN, i).getCellStyle());
                        }
                        maxRow += IntDef.INT_THREE;
                    }

                }

                CellStyle style = wbTemplate.createCellStyle();
                Font font = wbTemplate.createFont();
                font.setFontName("Arial");
                style.setFont(font);
                style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                style.setRightBorderColor(HSSFColor.BLACK.index);
                style.setLeftBorderColor(HSSFColor.BLACK.index);
                style.setTopBorderColor(HSSFColor.BLACK.index);
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                for (int i = IntDef.INT_ELEVEN; i <= toSheetName.getRow(IntDef.INT_EIGHT).getLastCellNum(); i++) {
                    PoiUtil.setCellStyle(toSheetName, IntDef.INT_EIGHT, i, style);
                }
                toSheetName.addMergedRegion(new CellRangeAddress(IntDef.INT_SEVEN, IntDef.INT_SEVEN, IntDef.INT_TEN,
                    toSheetName.getRow(IntDef.INT_EIGHT).getLastCellNum() - 1));
                PoiUtil.setCellValue(toSheetName, IntDef.INT_EIGHT, IntDef.INT_ELEVEN,
                    PoiUtil.getStringCellValue(styleSheetName, IntDef.INT_TWO, IntDef.INT_FOURTEEN));
                PoiUtil.setCellValue(toSheetName, IntDef.INT_ONE, IntDef.INT_ONE,
                    PoiUtil.getStringCellValue(styleSheetName, IntDef.INT_THREE, IntDef.INT_FOURTEEN));

            }

            wbTemplate.removeSheetAt(wbOutput.getSheetIndex(SHEET_VV));
            wbTemplate.removeSheetAt(wbOutput.getSheetIndex(SHEET_AISIN));

        } else {
            // get calendar list by month
            List<CPOCFF01Entity> codelist = service.getCustomerCode(param);
            boolean vvSheetUseflg = false;
            boolean aisinSheetUseflg = false;
            boolean dateFlg = true;
            String custStartMonth = param.getSwapData().get("custStartMonth").toString();
            String custEndMonth = param.getSwapData().get("custEndMonth").toString();
            ArrayList<String> monthArray = new ArrayList<String>();
            Calendar c = Calendar.getInstance();
            c.setTime(DateTimeUtil.parseMonth(custStartMonth));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            // month made
            if (custStartMonth.equals(custEndMonth)) {
                dateFlg = false;
                String str = sdf.format(c.getTime());
                if (IntDef.INT_ONE == param.getLanguage()) {
                    monthArray.add(DateTimeUtil.formatDate("MMM yyyy", "yyyy-MM", str));
                } else {
                    monthArray.add(str);
                }
            }
            while (c.getTime().before(DateTimeUtil.parseMonth(custEndMonth))) {
                String str = sdf.format(c.getTime());
                if (IntDef.INT_ONE == param.getLanguage()) {
                    monthArray.add(DateTimeUtil.formatDate("MMM yyyy", "yyyy-MM", str));
                } else {
                    monthArray.add(str);
                }
                c.add(Calendar.MONTH, 1);
            }
            if (dateFlg) {
                c.setTime(DateTimeUtil.parseMonth(custEndMonth));
                String str = sdf.format(c.getTime());
                if (IntDef.INT_ONE == param.getLanguage()) {
                    monthArray.add(DateTimeUtil.formatDate("MMM yyyy", "yyyy-MM", str));
                } else {
                    monthArray.add(str);
                }
            }

            int j = 0;
            for (int i = IntDef.INT_ELEVEN; i < monthArray.size() + IntDef.INT_ELEVEN; i++) {
                // vv sheet header
                PoiUtil.setCellStyle(vvSheetName, IntDef.INT_NINE, i,
                    PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());

                PoiUtil.setCellValue(vvSheetName, IntDef.INT_NINE, i, monthArray.get(j));
                vvSheetName.setColumnWidth(i, FIVE_THOUSAND);

                // aisin sheet header
                PoiUtil.setCellStyle(aisinSheetName, IntDef.INT_NINE, i,
                    PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                PoiUtil.setCellValue(aisinSheetName, IntDef.INT_NINE, i, monthArray.get(j));
                aisinSheetName.setColumnWidth(i, FIVE_THOUSAND);

                j += 1;

            }

            vvSheetName.setColumnWidth(IntDef.INT_TEN, FIVE_THOUSAND);
            aisinSheetName.setColumnWidth(IntDef.INT_TEN, FIVE_THOUSAND);
            vvSheetName.setColumnWidth(IntDef.INT_NINE, FIVE_THOUSAND);
            aisinSheetName.setColumnWidth(IntDef.INT_NINE, FIVE_THOUSAND);

            // merge cell
            CellStyle style = wbTemplate.createCellStyle();
            Font font = wbTemplate.createFont();
            font.setFontName("Arial");
            style.setFont(font);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setRightBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            style.setWrapText(true);
            for (int i = IntDef.INT_ELEVEN; i <= vvSheetName.getRow(IntDef.INT_EIGHT).getLastCellNum(); i++) {
                PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT, i, style);
            }
            vvSheetName.addMergedRegion(new CellRangeAddress(IntDef.INT_SEVEN, IntDef.INT_SEVEN, IntDef.INT_TEN,
                vvSheetName.getRow(IntDef.INT_EIGHT).getLastCellNum() - 1));
            PoiUtil.setCellValue(vvSheetName, IntDef.INT_EIGHT, IntDef.INT_ELEVEN,
                PoiUtil.getStringCellValue(styleSheetName, IntDef.INT_TWO, IntDef.INT_FIFTEEN));
            PoiUtil.setCellValue(vvSheetName, IntDef.INT_ONE, IntDef.INT_ONE,
                PoiUtil.getStringCellValue(styleSheetName, IntDef.INT_THREE, IntDef.INT_FIFTEEN));
            for (int i = IntDef.INT_ELEVEN; i <= aisinSheetName.getRow(IntDef.INT_EIGHT).getLastCellNum(); i++) {
                PoiUtil.setCellStyle(aisinSheetName, IntDef.INT_EIGHT, i, style);
            }
            aisinSheetName.addMergedRegion(new CellRangeAddress(IntDef.INT_SEVEN, IntDef.INT_SEVEN, IntDef.INT_TEN,
                aisinSheetName.getRow(IntDef.INT_EIGHT).getLastCellNum() - 1));
            PoiUtil.setCellValue(aisinSheetName, IntDef.INT_EIGHT, IntDef.INT_ELEVEN,
                PoiUtil.getStringCellValue(styleSheetName, IntDef.INT_TWO, IntDef.INT_FIFTEEN));
            PoiUtil.setCellValue(aisinSheetName, IntDef.INT_ONE, IntDef.INT_ONE,
                PoiUtil.getStringCellValue(styleSheetName, IntDef.INT_THREE, IntDef.INT_FIFTEEN));

            // set sheet data and style
            int vvMaxRow = vvSheetName.getLastRowNum() + 1;
            int aisinMaxRow = aisinSheetName.getLastRowNum() + 1;
            for (CPOCFF01Entity entity : codelist) {
                if (BSPTN_VV_NO.equals(entity.getBusinessPattern())) {
                    businessPattern = CodeCategoryManager.getCodeName(param.getLanguage(),
                        CodeMasterCategory.BUSINESS_PATTERN, BusinessPattern.V_V);
                    PoiUtil.setCellValue(vvSheetName, IntDef.INT_SEVEN, IntDef.INT_THREE, businessPattern);

                    int maxCol = vvSheetName.getRow(IntDef.INT_EIGHT).getLastCellNum();
                    int styleMaxCol = IntDef.INT_ELEVEN;
                    for (int i = 1; i < styleMaxCol; i++) {
                        PoiUtil.setCellStyle(vvSheetName, vvMaxRow, i,
                            PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, i).getCellStyle());
                        PoiUtil.setCellValue(vvSheetName, vvMaxRow, IntDef.INT_THREE, entity.getCustomercode());
                        PoiUtil.setCellStyle(vvSheetName, vvMaxRow + 1, i,
                            PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, i).getCellStyle());
                        PoiUtil.setCellValue(vvSheetName, vvMaxRow + 1, IntDef.INT_THREE, entity.getCustomercode());
                        PoiUtil.setCellStyle(vvSheetName, vvMaxRow + IntDef.INT_TWO, i,
                            PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, i).getCellStyle());
                        PoiUtil.setCellValue(vvSheetName, vvMaxRow + IntDef.INT_TWO, IntDef.INT_THREE,
                            entity.getCustomercode());
                    }
                    for (int i = styleMaxCol; i <= maxCol; i++) {
                        PoiUtil
                            .setCellStyle(vvSheetName, vvMaxRow, i,
                                PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_TWELVE)
                                    .getCellStyle());
                        PoiUtil
                            .setCellStyle(vvSheetName, vvMaxRow + 1, i,
                                PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_TWELVE)
                                    .getCellStyle());
                        PoiUtil
                            .setCellStyle(vvSheetName, vvMaxRow + IntDef.INT_TWO, i,
                                PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_TWELVE)
                                    .getCellStyle());
                    }
                    vvMaxRow += IntDef.INT_THREE;

                    vvSheetUseflg = true;

                } else if (BSPTN_AISIN_NO.equals(entity.getBusinessPattern())) {
                    businessPattern = CodeCategoryManager.getCodeName(param.getLanguage(),
                        CodeMasterCategory.BUSINESS_PATTERN, BusinessPattern.AISIN);
                    PoiUtil.setCellValue(aisinSheetName, IntDef.INT_SEVEN, IntDef.INT_THREE, businessPattern);

                    int maxCol = aisinSheetName.getRow(IntDef.INT_EIGHT).getLastCellNum();
                    int styleMaxCol = IntDef.INT_ELEVEN;
                    for (int i = 1; i < styleMaxCol; i++) {
                        PoiUtil.setCellStyle(aisinSheetName, aisinMaxRow, i,
                            PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, i).getCellStyle());
                        PoiUtil.setCellValue(aisinSheetName, aisinMaxRow, IntDef.INT_THREE, entity.getCustomercode());
                        PoiUtil.setCellStyle(aisinSheetName, aisinMaxRow + 1, i,
                            PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, i).getCellStyle());
                        PoiUtil.setCellValue(aisinSheetName, aisinMaxRow + 1, IntDef.INT_THREE,
                            entity.getCustomercode());
                        PoiUtil.setCellStyle(aisinSheetName, aisinMaxRow + IntDef.INT_TWO, i,
                            PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, i).getCellStyle());
                        PoiUtil.setCellValue(aisinSheetName, aisinMaxRow + IntDef.INT_TWO, IntDef.INT_THREE,
                            entity.getCustomercode());
                    }
                    for (int i = styleMaxCol; i <= maxCol; i++) {
                        PoiUtil
                            .setCellStyle(aisinSheetName, aisinMaxRow, i,
                                PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_TWELVE)
                                    .getCellStyle());
                        PoiUtil
                            .setCellStyle(aisinSheetName, aisinMaxRow + 1, i,
                                PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_TWELVE)
                                    .getCellStyle());
                        PoiUtil
                            .setCellStyle(aisinSheetName, aisinMaxRow + IntDef.INT_TWO, i,
                                PoiUtil.getOrCreateCell(styleSheetName, IntDef.INT_THREE, IntDef.INT_TWELVE)
                                    .getCellStyle());

                    }
                    aisinMaxRow += IntDef.INT_THREE;

                    aisinSheetUseflg = true;

                }
            }

            // change sheet name
            if (vvSheetUseflg) {
                String sheetName = PoiUtil.getStringCellValue(vvSheetName, IntDef.INT_TEN, IntDef.INT_THREE)
                        + "("
                        + CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.BUSINESS_PATTERN,
                            BusinessPattern.V_V) + ")";

                wbTemplate.setSheetName(0, sheetName);

            }
            if (aisinSheetUseflg) {
                String sheetName = PoiUtil.getStringCellValue(aisinSheetName, IntDef.INT_TEN, IntDef.INT_THREE)
                        + "("
                        + CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.BUSINESS_PATTERN,
                            BusinessPattern.AISIN) + ")";

                wbTemplate.setSheetName(1, sheetName);
            }

            if (!vvSheetUseflg) {
                wbTemplate.removeSheetAt(wbOutput.getSheetIndex(SHEET_VV));

            }
            if (!aisinSheetUseflg) {
                wbTemplate.removeSheetAt(wbOutput.getSheetIndex(SHEET_AISIN));
            }

        }

        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE));

    }

    @Override
    protected String getFileId() {
        return FileId.CPOCFF01;
    }

}
