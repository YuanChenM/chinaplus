/**
 * CPOCFF04Controller.java
 * 
 * @screen CPOCFF04
 * @author li_feng
 */

package com.chinaplus.web.om.control;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
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
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.PartSortManager;
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
import com.chinaplus.web.om.entity.CPOCFFComEntity;
import com.chinaplus.web.om.entity.CPOCFFComMonthlyEntity;
import com.chinaplus.web.om.service.CPOCFFComService;

/**
 * Download Customer Forecast History Report Controller.
 */
@Controller
public class CPOCFF04Controller extends BaseFileController {

    /** twelve */
    private static final Integer TWELVE = 12;
    /** download file name */
    private static final String FILE_NAME = "Customer Forecast History Report";
    /** report sheet */
    private static final String REPORT_SHEET = "toSheet";
    /** style sheet */
    private static final String SHEET_STYLE = "style";
    /** uom style grey */
    private static final String UOM_STYLE_GREY = "uom_style_grey";
    /** uom style white */
    private static final String UOM_STYLE_WHITE = "uom_style_white";
    /** header row */
    private static final Integer HEADER_ROW = 5;
    /** data start row */
    private static final Integer DATA_START_ROW = 6;
    /** start col */
    private static final Integer START_COL = 11;
    /** partsStartCol */
    private static final Integer PARTS_START_COL = 2;
    /** col width */
    private static final Integer FIVE_THOUSAND = 5000;
    /** no cfc en */
    private static final String NO_CFC_EN = "No Customer Forecast";
    /** no cfc cn */
    private static final String NO_CFC_CN = "无客户内示";
    /** actualqty en */
    private static final String ACTUALQTY_EN = "Actual";
    /** actualqty cn */
    private static final String ACTUALQTY_CN = "实际";
    /** sheetName en */
    private static final String SHEET_NAME_EN = "History";
    /** sheetName cn */
    private static final String SHEET_NAME_CN = "历史";
    

    @Autowired
    private CPOCFFComService service;

    /**
     * Download Customer Forecast History Check
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     * @throws ParseException E
     */
    @RequestMapping(value = "/om/CPOCFF04/custHistoryDownloadCheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> custHistoryDownloadCheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) throws ParseException {

        BaseResult<String> result = new BaseResult<String>();

        if ((boolean) param.getSwapData().get("needCheck")) {
            param.setFilters(param.getSwapData());

            // set common parameters by session
            this.setCommonParam(param, request);
            String lang = MessageManager.getLanguage(param.getLanguage()).toString();
            String custCode = StringUtil.toSafeString(param.getSwapData().get("customerCode"));
            String custStartMonth = StringUtil.toSafeString(param.getSwapData().get("custStartMonth"));
            String custEndMonth = StringUtil.toSafeString(param.getSwapData().get("custEndMonth"));
            String Msg = null;
            String Msg2 = null;

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
                    int month = getMonthDifference(custStartMonth, custEndMonth);
                    if (month >= IntDef.INT_TWELVE) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1003_003);
                        message.setMessageArgs(new String[] { TWELVE.toString() });
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
            
            if(result.getMessages() == null){
                param.setSwapData("SelectFrom", "CPOCFF04");
                List<CPOCFFComEntity> listEntity = service.getPartsInfo(param);
                if(listEntity.size() == 0){
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1005_001);
                    result.addMessage(message);
                }
            }
            
        }
        return result;
    }

    /**
     * Download Customer Forecast History
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "om/CPOCFF04/download",
        method = RequestMethod.POST)
    public void custHistoryDownload(PageParam param, HttpServletRequest request, HttpServletResponse response) {

        this.setCommonParam(param, request);
        String custCodeName = StringUtil.toSafeString(param.getSwapData().get("customerCodeName"));
        String filename = FILE_NAME + "_" + custCodeName + "_" + param.getClientTime() + ".xlsx";

        this.downloadExcelWithTemplate(StringUtil.formatMessage(filename, param.getClientTime()), param, request,
            response);
    }

    /**
     * Write To Customer Forecast History Excel
     * 
     * @param param T
     * @param wbTemplate Workbook
     * @param wbOutput SXSSFWorkbook
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {
        param.setSwapData("SelectFrom", "CPOCFF04");
        List<CPOCFFComEntity> listEntity = service.getPartsInfo(param);
        PartSortManager.sort(listEntity, "ttcPartsNo", "oldTtcPartsNo");
        String startFcMonth = (String) param.getSwapData().get("custStartMonth");
        String endFcMonth = (String) param.getSwapData().get("custEndMonth");
        List<String> fcMonthList = service.getCfcMonth(startFcMonth, endFcMonth);
        LinkedHashMap<String, List<Date>> fileHeadMap = new LinkedHashMap<String, List<Date>>();
        fileHeadMap = getFileHead(listEntity, fcMonthList);

        // get sheet
        Sheet reportSheet = wbTemplate.getSheet(REPORT_SHEET);
        Sheet styleSheet = wbTemplate.getSheet(SHEET_STYLE);

        int headerRow = HEADER_ROW;
        int startCol = START_COL;
        String formartYM = "";
        String formartYMD = "";
        if (param.getLanguage().equals(IntDef.INT_TWO)) {
            formartYM = DateTimeUtil.FORMAT_YYYYMM;
            formartYMD = DateTimeUtil.FORMAT_DATE_YYYYMMDD;
        } else {
            formartYM = DateTimeUtil.FORMAT_MMMYYYY;
            formartYMD = DateTimeUtil.FORMAT_DDMMMYYYY;
        }
        // made header
        for (Map.Entry<String, List<Date>> fileHeadMapEntry : fileHeadMap.entrySet()) {
            boolean actualFlg = false;
            List<Date> fcDateList = fileHeadMapEntry.getValue();
            String cfcMonth = DateTimeUtil.formatDate(formartYM, "yyyyMM", fileHeadMapEntry.getKey());
            reportSheet.setColumnWidth(startCol - 1, FIVE_THOUSAND);
            if (fcDateList.size() == 0) {
                reportSheet.setColumnWidth(startCol, FIVE_THOUSAND);
                PoiUtil.setCellStyle(reportSheet, headerRow, startCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_THIRTEEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, headerRow, startCol,
                    PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_THIRTEEN));
                PoiUtil.setCellStyle(reportSheet, headerRow - 1, startCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                PoiUtil.setCellValue(reportSheet, headerRow - 1, startCol, cfcMonth);

                startCol++;
            } else if (fcDateList.size() >= 1) {
                int colNum = 0;
                String month = fileHeadMapEntry.getKey();
                if (DateTimeUtil.parseMonth(month).getTime() < DateTimeUtil.parseMonth(
                    DateTimeUtil.formatDate("yyyyMM", service.getDBDateTimeByDefaultTimezone())).getTime()) {
                    colNum = fcDateList.size() + 1;
                    actualFlg = true;
                } else {
                    colNum = fcDateList.size();
                }

                PoiUtil.setCellStyle(reportSheet, headerRow - 1, startCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                PoiUtil.setCellValue(reportSheet, headerRow - 1, startCol, cfcMonth);
                reportSheet.setColumnWidth(startCol, FIVE_THOUSAND);
                // have actialQTY
                Integer relColNum = 0;
                if(actualFlg){
                    relColNum = colNum - 1;
                } else {
                    relColNum = colNum;
                }
                for (int i = startCol; i < startCol + relColNum; i++) {
                    String fcDate = DateTimeUtil.formatDate(formartYMD, fcDateList.get(i - startCol));
                    PoiUtil.setCellStyle(reportSheet, headerRow, i,
                        PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_ELEVEN).getCellStyle());
                    PoiUtil.setCellValue(reportSheet, headerRow, i, fcDate);
                    reportSheet.setColumnWidth(i, FIVE_THOUSAND);
                }
                if (actualFlg) {
                    PoiUtil.setCellStyle(reportSheet, headerRow, startCol + relColNum,
                        PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_TWELVE).getCellStyle());
                    PoiUtil.setCellValue(reportSheet, headerRow, startCol + relColNum,
                        PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_TWELVE));
                    reportSheet.setColumnWidth(startCol + relColNum, FIVE_THOUSAND);
                }

                int megerIndex = startCol + colNum;
                for (int i = startCol; i < megerIndex; i++) {
                    PoiUtil.setCellStyle(reportSheet, IntDef.INT_FOUR, i,
                        PoiUtil.getCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                }

                reportSheet.addMergedRegion(new CellRangeAddress(headerRow - IntDef.INT_TWO,
                    headerRow - IntDef.INT_TWO, startCol - 1, megerIndex - IntDef.INT_TWO));
                startCol = startCol + colNum;
            }
        }

        // write parts data
        int startRow = DATA_START_ROW;
        int maxColNum = PoiUtil.getRow(reportSheet, headerRow).getPhysicalNumberOfCells();
        for (CPOCFFComEntity comEntity : listEntity) {
            int partsStartCol = PARTS_START_COL;
            // set parts data
            Cell[] templateCells = getTemplateCells(SHEET_STYLE, IntDef.INT_FOUR, wbTemplate);
            Object[] dataArray = new Object[] { null, null, null, null, null, null, null, null, null, null };
            createOneDataRowByTemplate(reportSheet, startRow - 1, partsStartCol - IntDef.INT_TWO, templateCells,
                dataArray);

            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getTtcPartsNo());
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getCustPartsNo());
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getCustomerCode());
            PoiUtil.setCellValue(
                reportSheet,
                startRow,
                partsStartCol++,
                CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.PARTS_TYPE,
                    comEntity.getPartsType()));
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getCarModel());
            // uom
            int digits = MasterManager.getUomDigits(comEntity.getUomCode());
            String uomCode = comEntity.getUomCode();
            CellStyle nonInvoiceQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
            PoiUtil.setCellStyle(reportSheet, startRow, partsStartCol, nonInvoiceQtyStyle);
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++,
                StringUtil.formatBigDecimal(uomCode, comEntity.getOrderLot()));
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getOldTtcPartsNo());
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getPartsNameEn());
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getPartsNameCn());

            // set cfc info
            List<CPOCFFComMonthlyEntity> comMonthlyEntitylist = comEntity.getCfcMonthlyLst();


            
            String dateHeader = "";
            String monthHeader = "";
            for (int i = partsStartCol; i <= maxColNum + 1; i++) {
                
                dateHeader = PoiUtil.getStringCellValue(reportSheet, headerRow, i);
                if(!StringUtil.isEmpty(PoiUtil.getStringCellValue(reportSheet, headerRow - 1, i))){
                    monthHeader = PoiUtil.getStringCellValue(reportSheet, headerRow - 1, i);
                }
                
                if(StringUtil.isEmpty(monthHeader)){
                    monthHeader = PoiUtil.getStringCellValue(reportSheet, headerRow - 1, i);
                }



                if (dateHeader.equals(NO_CFC_EN) || dateHeader.equals(NO_CFC_CN)) {
                    PoiUtil.setCellStyle(reportSheet, startRow, i,
                        PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_ELEVEN).getCellStyle());
                } else {
                    for (CPOCFFComMonthlyEntity cpocffComMonthlyEntity : comMonthlyEntitylist) {
                        // if have cfc QTY
                        if (cpocffComMonthlyEntity.getReceiveDate().equals(DateTimeUtil.parseDate(dateHeader))
                                && cpocffComMonthlyEntity.getCfcMonth().equals(
                                    DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                                        DateTimeUtil.parseMonth(monthHeader)))) {
                            CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                            PoiUtil.setCellStyle(reportSheet, startRow, i, cfcQtyStyle);
                            PoiUtil.setCellValue(reportSheet, startRow, i,
                                StringUtil.formatBigDecimal(uomCode, cpocffComMonthlyEntity.getCfcQty()));
                            break;
                        } else if (cpocffComMonthlyEntity.getCfcMonth().equals(
                            DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                                DateTimeUtil.parseMonth(monthHeader)))
                                && (dateHeader.equals(ACTUALQTY_EN) || dateHeader.equals(ACTUALQTY_CN))) {
                            if (cpocffComMonthlyEntity.getActualQty() != null) {
                                CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                                PoiUtil.setCellStyle(reportSheet, startRow, i, cfcQtyStyle);
                                PoiUtil.setCellValue(reportSheet, startRow, i,
                                    StringUtil.formatBigDecimal(uomCode, cpocffComMonthlyEntity.getActualQty()));
                                break;
                            }
                        } else {
                            CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                            PoiUtil.setCellStyle(reportSheet, startRow, i, cfcQtyStyle);
                        }
                    }
                    if (PoiUtil.getStringCellValue(reportSheet, startRow, i) == null) {
                        PoiUtil.setCellStyle(reportSheet, startRow, i,
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_ELEVEN).getCellStyle());
                    }
                }

            }
            startRow++;
        }
        
        String sheetName = "";
        if(param.getLanguage().equals(IntDef.INT_TWO)){
            sheetName = SHEET_NAME_CN;
        } else {
            sheetName = SHEET_NAME_EN;
        }
        
        wbTemplate.setSheetName(0, sheetName);
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(UOM_STYLE_GREY));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(UOM_STYLE_WHITE));

    }

    /**
     * Get Month Difference
     * 
     * @param custStartMonth String
     * @param custEndMonth String
     * @return difference
     * @throws ParseException E
     */
    public int getMonthDifference(String custStartMonth, String custEndMonth) throws ParseException {
        SimpleDateFormat formatYM = new SimpleDateFormat("yyyyMM");
        Calendar c = Calendar.getInstance();
        c.setTime(formatYM.parse(custEndMonth));
        int endYear = c.get(Calendar.YEAR);
        int endMonth = c.get(Calendar.MONTH);
        c.setTime(formatYM.parse(custStartMonth));
        int startYear = c.get(Calendar.YEAR);
        int startMonth = c.get(Calendar.MONTH);
        int month = 0;
        if (endYear == startYear) {
            month = endMonth - startMonth;
        } else {
            month = IntDef.INT_TWELVE * (endYear - startYear) + endMonth - startMonth;
        }
        return month;
    }

    /**
     * Get File Head Map
     * 
     * @param comMonthEntityList List<CPOCFFComMonthlyEntity>
     * @param fcMonthList List<String>
     * @return LinkedHashMap<String, Integer>
     */
    public LinkedHashMap<String, List<Date>> getHeadMap(List<CPOCFFComMonthlyEntity> comMonthEntityList,
        List<String> fcMonthList) {
        LinkedHashMap<String, List<Date>> topMap = new LinkedHashMap<String, List<Date>>();

        for (String cfcMonth : fcMonthList) {
            List<Date> fcDateList = new ArrayList<Date>();
            for (CPOCFFComMonthlyEntity listEntity : comMonthEntityList) {
                if (listEntity.getCfcMonth().equals(cfcMonth)) {
                    fcDateList.add(listEntity.getReceiveDate());
                }
            }
            topMap.put(cfcMonth, fcDateList);
        }
        return topMap;
    }

    /**
     * Get File Head
     * 
     * @param listEntity List<CPOCFFComEntity>
     * @param fcMonthList List<String>
     * @return LinkedHashMap<String, List<Date>>
     */
    public LinkedHashMap<String, List<Date>> getFileHead(List<CPOCFFComEntity> listEntity, List<String> fcMonthList) {

        LinkedHashMap<String, List<Date>> fileHeadMap = new LinkedHashMap<String, List<Date>>();
        LinkedHashMap<String, LinkedHashMap<String, List<Date>>> partsHeadMap = new LinkedHashMap<String, LinkedHashMap<String, List<Date>>>();
        // get parts head
        for (CPOCFFComEntity cpocffComEntity : listEntity) {
            List<CPOCFFComMonthlyEntity> comMonthEntityList = cpocffComEntity.getCfcMonthlyLst();
            LinkedHashMap<String, List<Date>> topMap = getHeadMap(comMonthEntityList, fcMonthList);
            partsHeadMap.put(cpocffComEntity.getTtcPartsNo(), topMap);
        }
        // get all file head
        for (String fcMonth : fcMonthList) {
            List<Date> dateList = new ArrayList<Date>();
            for (Map.Entry<String, LinkedHashMap<String, List<Date>>> partsEntry : partsHeadMap.entrySet()) {
                LinkedHashMap<String, List<Date>> dateMap = partsEntry.getValue();
                for (Map.Entry<String, List<Date>> entry : dateMap.entrySet()) {
                    if (fcMonth.equals(entry.getKey())) {
                        for (Date date : entry.getValue()) {
                            dateList.add(date);
                        }
                    }
                }
            }
            if (dateList.size() == 0) {
                fileHeadMap.put(fcMonth, dateList);
            } else {
                HashSet<Date> hs = new HashSet<Date>(dateList);
                dateList.clear();
                dateList.addAll(hs);
                Collections.sort(dateList);
                fileHeadMap.put(fcMonth, dateList);
            }
        }
        return fileHeadMap;
    }

    @Override
    protected String getFileId() {
        return FileId.CPOCFF04;
    }

}