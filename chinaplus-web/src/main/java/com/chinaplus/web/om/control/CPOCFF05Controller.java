/**
 * CPOCFF05Controller.java
 * 
 * @screen CPOCFF05
 * @author li_feng
 */

package com.chinaplus.web.om.control;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.chinaplus.common.entity.CPOCFFComEntity;
import com.chinaplus.common.entity.CPOCFFTEMPEntity;
import com.chinaplus.common.service.CfcRundownService;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.PartSortManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.service.CPMPMS01Service;
import com.chinaplus.web.om.entity.CPOCFF05Entity;
import com.chinaplus.web.om.service.CPOCFFComService;

/**
 * Download Customer Forecast History Report Controller.
 */
@Controller
public class CPOCFF05Controller extends BaseFileController {

    /** twelve */
    private static final Integer SIX = 6;
    /** download file name */
    private static final String FILE_NAME = "Latest Customer Forecast In Rundown";
    /** header_start_col */
    private static final Integer HEADER_START_COL = 14;
    /** header_start_row */
    private static final Integer HEADER_START_ROW = 4;
    /** report sheet */
    private static final String REPORT_SHEET_CN = "toSheet_CN";
    /** report sheet */
    private static final String REPORT_SHEET_EN = "toSheet_EN";
    /** style sheet */
    private static final String SHEET_STYLE = "style";
    /** uom style red */
    private static final String UOM_STYLE_GREY = "uom_style_grey";
    /** uom style white */
    private static final String UOM_STYLE_WHITE = "uom_style_white";
    /** uom style blue */
    private static final String UOM_STYLE_YELLOW = "uom_style_yellow";
    /** col width */
    private static final Integer FIVE_THOUSAND = 5000;
    /** col width */
    private static final Integer EIGHT_THOUSAND = 8000;
    /** sheetName en */
    private static final String SHEET_NAME_EN = "LatestCustomerForecastInRundown";
    /** sheetName cn */
    private static final String SHEET_NAME_CN = "最新客户推移内示";

    @Autowired
    private CPOCFFComService service;

    @Autowired
    private CfcRundownService comService;

    @Autowired
    private CPMPMS01Service cpmpms01Service;

    /**
     * Get OfficeTime
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "/om/CPOCFF05/getOfficeTime",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> getOfficeTime(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        this.setCommonParam(param, request);
        Date officeTime = service.getDBDateTime(param.getOfficeTimezone());
        BaseResult<String> result = new BaseResult<String>();
        result.setData(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMM_SOLIDUS, officeTime));
        return result;
    }

    /**
     * Download Latest Customer Forecast In Rundown Check
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     * @throws ParseException E
     */
    @RequestMapping(value = "/om/CPOCFF05/rundownDownloadCheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> rundownDownloadCheck(@RequestBody PageParam param, HttpServletRequest request,
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
                    if (month >= IntDef.INT_SIX) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1003_003);
                        message.setMessageArgs(new String[] { SIX.toString() });
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
            
            if (result.getMessages() == null || result.getMessages().size() == 0) {
                // start cfcMonth have Data check
                List<CPOCFFComEntity> tempList = comService.checkCfcMonth(param);
                if(tempList.size() == 0){
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1005_001);
                    result.addMessage(message);
                }
            }
            
            
        }
        return result;
    }

    /**
     * Download Latest Customer Forecast In Rundown
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "om/CPOCFF05/download",
        method = RequestMethod.POST)
    public void rundownDownload(PageParam param, HttpServletRequest request, HttpServletResponse response) {
        
        this.setCommonParam(param, request);
        String filename = FILE_NAME + "_" + param.getClientTime() + ".xlsx";
        param.setSwapData("officeId",param.getCurrentOfficeId());
        this.downloadExcelWithTemplate(StringUtil.formatMessage(filename, param.getClientTime()), param, request,
            response);
    }

    /**
     * Write To Latest Customer Forecast In Rundown Excel
     * 
     * @param param T
     * @param wbTemplate Workbook
     * @param wbOutput SXSSFWorkbook
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

        param.setSwapData("SelectFrom", "CPOCFF05");
        String startMonth = (String) param.getSwapData().get("custStartMonth");
        // get parts info
        
        List<CPOCFFComEntity> cpocffComEntityList = comService.doPreparePartsInfo(param);
        PartSortManager.sort(cpocffComEntityList, "ttcPartsNo", "oldTtcPartsNo");
        // get tntRundownCfc info
        List<CPOCFF05Entity> tntRundownCfcList = service.getTntRundownCfcInfo(param);
        // get month list
        List<String> monthList = service.getCfcMonth(param.getSwapData().get("custStartMonth").toString(), param
            .getSwapData().get("custEndMonth").toString());
        // get header list
        List<CPOCFF05Entity> headerList = new ArrayList<CPOCFF05Entity>();
        List<CPOCFF05Entity> tempList = new ArrayList<CPOCFF05Entity>();
        LinkedHashMap<Integer, List<CPOCFF05Entity>> cpocff05EntityListMap = new LinkedHashMap<Integer, List<CPOCFF05Entity>>();
        Integer key = cpocffComEntityList.get(0).getPartsId();

        for (CPOCFF05Entity cpocff05Entity : tntRundownCfcList) {
            if (cpocff05Entity.getPartsId().equals(key)) {
                headerList.add(cpocff05Entity);
            }
        }

        for (CPOCFFComEntity cpocffComEntity : cpocffComEntityList) {
            for (CPOCFF05Entity cpocff05Entity : tntRundownCfcList) {
                if (!cpocff05EntityListMap.containsKey(cpocff05Entity.getPartsId())) {
                    tempList = new ArrayList<CPOCFF05Entity>();
                }

                if (cpocffComEntity.getPartsId().equals(cpocff05Entity.getPartsId())) {
                    tempList.add(cpocff05Entity);
                    cpocff05EntityListMap.put(cpocffComEntity.getPartsId(), tempList);
                }

            }
        }

        List<CPOCFFTEMPEntity> cpocffTEMPEntityList = cpocffComEntityList.get(0).getCpocffTEMPEntityLst();
        if (!cpocffTEMPEntityList.isEmpty()) {
            cpocffTEMPEntityList.remove(0);
        }

        // remove current month data
        // cpocffComEntityList.get(0).getCpocffTEMPEntityLst().remove(0);

        // get sheet
        Sheet reportSheet;
        String removeSheetName;
        Sheet styleSheet = wbTemplate.getSheet(SHEET_STYLE);

        // made header
        String formartYM = "";
        String formartYMD = "";
        if (param.getLanguage().equals(IntDef.INT_TWO)) {
            formartYM = DateTimeUtil.FORMAT_YYYYMM;
            formartYMD = DateTimeUtil.FORMAT_DATE_YYYYMMDD;
            reportSheet = wbTemplate.getSheet(REPORT_SHEET_CN);
            removeSheetName = REPORT_SHEET_EN;
        } else {
            formartYM = DateTimeUtil.FORMAT_MMMYYYY;
            formartYMD = DateTimeUtil.FORMAT_DDMMMYYYY;
            reportSheet = wbTemplate.getSheet(REPORT_SHEET_EN);
            removeSheetName = REPORT_SHEET_CN;
        }

        Date today = DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
            service.getDBDateTime(param.getOfficeTimezone())));
        int i = HEADER_START_COL;
        reportSheet.setColumnWidth(i - 1, FIVE_THOUSAND);
        // for daily
        for (CPOCFF05Entity cpocff05Entity : headerList) {
            if (cpocff05Entity.getWorkingFlag().equals(1) && cpocff05Entity.getCfcDate().getTime() != today.getTime()) {
                // working day
                PoiUtil.setCellStyle(reportSheet, HEADER_START_ROW, i,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_FIFTEEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, HEADER_START_ROW, i,
                    DateTimeUtil.formatDate(formartYMD, cpocff05Entity.getCfcDate()));
            } else if (cpocff05Entity.getWorkingFlag().equals(0)
                    && cpocff05Entity.getCfcDate().getTime() != today.getTime()) {
                // rest Day
                PoiUtil.setCellStyle(reportSheet, HEADER_START_ROW, i,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_THIRTEEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, HEADER_START_ROW, i,
                    DateTimeUtil.formatDate(formartYMD, cpocff05Entity.getCfcDate()));
            } else {
                PoiUtil.setCellStyle(reportSheet, HEADER_START_ROW, i,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_FOURTEEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, HEADER_START_ROW, i,
                    DateTimeUtil.formatDate(formartYMD, cpocff05Entity.getCfcDate()));
            }
            reportSheet.setColumnWidth(i, FIVE_THOUSAND);
            i++;

        }
        // for monthly
        for (String month : monthList) {
            if(!month.equals(startMonth)){
                PoiUtil.setCellStyle(reportSheet, HEADER_START_ROW, i,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_FIFTEEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, HEADER_START_ROW, i,
                    DateTimeUtil.formatDate(formartYM, DateTimeUtil.parseMonth(month)));
                reportSheet.setColumnWidth(i, FIVE_THOUSAND);
                i++;
            }
        }

        // merge col
        int maxColNum = PoiUtil.getRow(reportSheet, HEADER_START_ROW).getPhysicalNumberOfCells();
        for (int j = HEADER_START_COL; j <= maxColNum; j++) {
            PoiUtil.setCellStyle(reportSheet, HEADER_START_ROW - 1, j,
                PoiUtil.getOrCreateCell(reportSheet, HEADER_START_ROW - 1, HEADER_START_COL).getCellStyle());
        }
        String title = PoiUtil.getStringCellValue(reportSheet, HEADER_START_ROW - 1, HEADER_START_COL);
        PoiUtil.setCellValue(reportSheet, HEADER_START_ROW - 1, HEADER_START_COL, "");
        reportSheet.addMergedRegion(new CellRangeAddress(HEADER_START_ROW - IntDef.INT_TWO, HEADER_START_ROW
                - IntDef.INT_TWO, HEADER_START_COL - 1, maxColNum - 1));
        PoiUtil.setCellValue(reportSheet, HEADER_START_ROW - 1, HEADER_START_COL, title);

        // set parts data
        int partsStartRow = HEADER_START_ROW + 1;
        int no = 1;
        for (CPOCFFComEntity cpocffComEntity : cpocffComEntityList) {
            int startCol = 1;
            // No.
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, 1).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, 1).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, 1).getCellStyle());
            PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, no);
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // TTC Part No.
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TWO).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TWO).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TWO).getCellStyle());
            PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, cpocffComEntity.getTtcPartsNo());
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // TTC Customer Code
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_THREE).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_THREE).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_THREE).getCellStyle());
            PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, cpocffComEntity.getCustomerCode());
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // Customer Part No.
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_FOUR).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_FOUR).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_FOUR).getCellStyle());
            PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, cpocffComEntity.getCustPartsNo());
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // Part Type
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_FIVE).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_FIVE).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_FIVE).getCellStyle());
            PoiUtil.setCellValue(
                reportSheet,
                partsStartRow,
                startCol,
                CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.PARTS_TYPE,
                    cpocffComEntity.getPartsType()));
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // Model
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_SIX).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_SIX).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_SIX).getCellStyle());
            PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, cpocffComEntity.getCarModel());
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // Order Lot
            int digits = MasterManager.getUomDigits(cpocffComEntity.getUomCode());
            String uomCode = cpocffComEntity.getUomCode();
            CellStyle nonInvoiceQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol, nonInvoiceQtyStyle);
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol, nonInvoiceQtyStyle);
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol, nonInvoiceQtyStyle);
            PoiUtil.setCellValue(reportSheet, partsStartRow, startCol,
                StringUtil.formatBigDecimal(uomCode, cpocffComEntity.getOrderLot()));
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // Old TTC Part No.
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_EIGHT).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_EIGHT).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_EIGHT).getCellStyle());
            PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, cpocffComEntity.getOldTtcPartsNo());
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // English Part Name
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_NINE).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_NINE).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_NINE).getCellStyle());
            PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, cpocffComEntity.getPartsNameEn());
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // Chinese Part Name
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TEN).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TEN).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TEN).getCellStyle());
            PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, cpocffComEntity.getPartsNameCn());
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // Customer Forecast Plan Adjustment Pattern for Case 1
            String type1 = CodeCategoryManager.getCodeName(param.getLanguage(),
                CodeMasterCategory.CUST_FORECAST_ADJUST_P1, cpocffComEntity.getCfcAdjustmentType1());
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_ELEVEN).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_ELEVEN).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_ELEVEN).getCellStyle());
            PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, type1);
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // Customer Forecast Plan Adjustment Pattern for Case 2
            String type2 = CodeCategoryManager.getCodeName(param.getLanguage(),
                CodeMasterCategory.CUST_FORECAST_ADJUST_P2, cpocffComEntity.getCfcAdjustmentType2());
            PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TWELVE).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TWELVE).getCellStyle());
            PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FIVE, IntDef.INT_TWELVE).getCellStyle());
            PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, type2);
            reportSheet.addMergedRegion(new CellRangeAddress(partsStartRow - 1, partsStartRow + 1, startCol - 1,
                startCol - 1));
            startCol++;

            // QTY name
            int[] distFromRowColumn1 = { partsStartRow - 1, IntDef.INT_TWELVE };
            copyRowByFrom(styleSheet, reportSheet, IntDef.INT_SIX, IntDef.INT_THREE, IntDef.INT_FIFTEEN, 1,
                distFromRowColumn1, true);
            reportSheet.setColumnWidth(startCol - 1, EIGHT_THOUSAND);
            startCol++;

            // set QTY
            List<CPOCFF05Entity> cpocff05TEMPList = cpocff05EntityListMap.get(cpocffComEntity.getPartsId());

            boolean setDataFlg = true;
            for (CPOCFF05Entity entity : cpocff05TEMPList) {

                if (setDataFlg) {
                    String type = CodeCategoryManager.getCodeName(param.getLanguage(),
                        CodeMasterCategory.CUST_FORECAST_ADJUST_P2, entity.getCfcAdjustmentType());
                    PoiUtil.setCellValue(reportSheet, partsStartRow + IntDef.INT_TWO, IntDef.INT_THIRTEEN,
                        PoiUtil.getStringCellValue(reportSheet, partsStartRow + IntDef.INT_TWO, IntDef.INT_THIRTEEN)
                                + " " + type);
                    setDataFlg = false;
                }

                CellStyle style = null;
                if (entity.getCfcDate().equals(today)) {
                    style = super.getDecimalStyle(wbTemplate, UOM_STYLE_YELLOW, digits);
                } else if (entity.getWorkingFlag().equals(1)) {
                    style = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                } else if (entity.getWorkingFlag().equals(0)) {
                    style = super.getDecimalStyle(wbTemplate, UOM_STYLE_GREY, digits);
                }

                PoiUtil.setCellStyle(reportSheet, partsStartRow, startCol, style);
                PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, startCol, style);
                PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, startCol, style);
                if (entity.getDeliveredQty() != null
                        || !DecimalUtil.isEquals(entity.getDeliveredQty(), new BigDecimal(0))) {
                    // PoiUtil.setCellValue(reportSheet, partsStartRow, startCol,
                    //     StringUtil.formatBigDecimal(uomCode, entity.getDeliveredQty()));
                    PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, entity.getDeliveredQty());
                } else {
                    PoiUtil.setCellValue(reportSheet, partsStartRow, startCol, "");
                }
                if (entity.getOriginalQty() != null
                        || !DecimalUtil.isEquals(entity.getOriginalQty(), new BigDecimal(0))) {
                    // PoiUtil.setCellValue(reportSheet, partsStartRow + 1, startCol,
                    //     StringUtil.formatBigDecimal(uomCode, entity.getOriginalQty()));
                    PoiUtil.setCellValue(reportSheet, partsStartRow + 1, startCol, entity.getOriginalQty());
                } else {
                    PoiUtil.setCellValue(reportSheet, partsStartRow + 1, startCol, "");
                }
                if (entity.getCfcQty() != null || !DecimalUtil.isEquals(entity.getCfcQty(), new BigDecimal(0))) {
                    // PoiUtil.setCellValue(reportSheet, partsStartRow + IntDef.INT_TWO, startCol,
                    //     StringUtil.formatBigDecimal(uomCode, entity.getCfcQty()));
                    PoiUtil.setCellValue(reportSheet, partsStartRow + IntDef.INT_TWO, startCol, entity.getCfcQty());
                } else {
                    PoiUtil.setCellValue(reportSheet, partsStartRow + IntDef.INT_TWO, startCol, "");
                }
                startCol++;

            }

            // set monthly data
            List<CPOCFFTEMPEntity> entitylist = cpocffComEntity.getCpocffTEMPEntityLst();
            int maxCol = PoiUtil.getRow(reportSheet, HEADER_START_ROW).getPhysicalNumberOfCells();
            for (int index = startCol; index <= maxCol; index++) {
                CellStyle style = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                PoiUtil.setCellStyle(reportSheet, partsStartRow, index, style);
                PoiUtil.setCellStyle(reportSheet, partsStartRow + 1, index, style);
                PoiUtil.setCellStyle(reportSheet, partsStartRow + IntDef.INT_TWO, index, style);
                
                String month = PoiUtil.getStringCellValue(reportSheet, HEADER_START_ROW, index);
                BigDecimal monthQty = null;
                for(CPOCFFTEMPEntity entity : entitylist){
                    if (entity.getCfcMonth().equals(
                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, DateTimeUtil.parseMonth(month)))) {
                        if(entity.getCfcQty() != null){
                            monthQty = entity.getCfcQty();
                            break;
                        }
                    }
                }
                if(monthQty != null){
                    // PoiUtil.setCellValue(reportSheet, partsStartRow + 1, index,
                    //     StringUtil.formatBigDecimal(uomCode, monthQty));
                    PoiUtil.setCellValue(reportSheet, partsStartRow + 1, index, monthQty);
                }
                
            }

            no++;
            partsStartRow += IntDef.INT_THREE;

        }

        String sheetName = "";
        if (param.getLanguage().equals(IntDef.INT_TWO)) {
            sheetName = SHEET_NAME_CN;
        } else {
            sheetName = SHEET_NAME_EN;
        }
        wbTemplate.setSheetName(0, sheetName);
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(removeSheetName));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(UOM_STYLE_GREY));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(UOM_STYLE_WHITE));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(UOM_STYLE_YELLOW));

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
        if (endYear == endMonth) {
            month = endMonth - startMonth;
        } else {
            month = IntDef.INT_TWELVE * (endYear - startYear) + endMonth - startMonth;
        }
        return month;
    }

    @Override
    protected String getFileId() {
        return FileId.CPOCFF05;
    }

}