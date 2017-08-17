/**
 * CPOCFF03Controller.java
 * 
 * @screen CPOCFF03
 * @author li_feng
 */

package com.chinaplus.web.om.control;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

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
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOCFFComEntity;
import com.chinaplus.web.om.entity.CPOCFFComMonthlyEntity;
import com.chinaplus.web.om.service.CPOCFFComService;

/**
 * Download Customer Forecast Comparison Report Controller.
 */
@Controller
public class CPOCFF03Controller extends BaseFileController {

    /** file name en */
    private static final String FILE_NAME_EN = "Customer Forecast Comparison Report";
    /** report sheet */
    private static final String REPORT_SHEET = "toSheet";
    /** style sheet */
    private static final String SHEET_STYLE = "style";
    /** uom style red */
    private static final String UOM_STYLE_RED = "uom_style_red";
    /** uom style white */
    private static final String UOM_STYLE_WHITE = "uom_style_white";
    /** uom style blue */
    private static final String UOM_STYLE_BLUE = "uom_style_blue";
    /** col width */
    private static final Integer FIVE_THOUSAND = 5000;
    /** sheetName en */
    private static final String SHEET_NAME_EN = "ComparisonReport";
    /** sheetName cn */
    private static final String SHEET_NAME_CN = "对比报告";
    /** na */
    private static final String NA = "NA";

    @Autowired
    private CPOCFFComService service;

    /**
     * Download Customer Forecast Comparison Report Check
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     * @throws ParseException E
     */
    @RequestMapping(value = "/om/CPOCFF03/cfcComparisonReportDownloadCheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> cfcComparisonReportDownloadCheck(@RequestBody PageParam param,
        HttpServletRequest request, HttpServletResponse response) throws ParseException {

        this.setCommonParam(param, request);

        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        String mapKey = param.getSessionKey();
        List<CPOCFFComEntity> file1ListEntity = new ArrayList<CPOCFFComEntity>();
        List<CPOCFFComEntity> file2ListEntity = new ArrayList<CPOCFFComEntity>();

        // get two file data
        param.setSwapData("SelectFrom", "CPOCFF03");
        BaseResult<String> result = new BaseResult<String>();
        @SuppressWarnings("unchecked")
        List<Integer> cfcId = (List<Integer>) param.getSwapData().get("cfcId");
        Integer cdcId1 = null;
        Integer cdcId2 = null;
        String file1StartMonth = "";
        String file1EndMonth = "";
        String file2StartMonth = "";
        String file2EndMonth = "";

        String file1CfcNo = (String) param.getSwapData().get("file1CfcNo");
        String file2CfcNo = (String) param.getSwapData().get("file2CfcNo");
        String file1FcDate = (String) param.getSwapData().get("file1FcDate");
        String file2FcDate = (String) param.getSwapData().get("file2FcDate");

        if (DateTimeUtil.parseDate(file1FcDate).getTime() > DateTimeUtil.parseDate(file2FcDate).getTime()) {
            cdcId1 = cfcId.get(0);
            cdcId2 = cfcId.get(1);
            file1StartMonth = (String) param.getSwapData().get("file1StartMonth");
            file1EndMonth = (String) param.getSwapData().get("file1EndMonth");
            file2StartMonth = (String) param.getSwapData().get("file2StartMonth");
            file2EndMonth = (String) param.getSwapData().get("file2EndMonth");

        } else if (DateTimeUtil.parseDate(file1FcDate).getTime() == DateTimeUtil.parseDate(file2FcDate).getTime()) {
            if (file1CfcNo.compareTo(file2CfcNo) > 0) {
                cdcId1 = cfcId.get(0);
                cdcId2 = cfcId.get(1);
                file1StartMonth = (String) param.getSwapData().get("file1StartMonth");
                file1EndMonth = (String) param.getSwapData().get("file1EndMonth");
                file2StartMonth = (String) param.getSwapData().get("file2StartMonth");
                file2EndMonth = (String) param.getSwapData().get("file2EndMonth");
            } else {
                cdcId1 = cfcId.get(1);
                cdcId2 = cfcId.get(0);
                file1StartMonth = (String) param.getSwapData().get("file2StartMonth");
                file1EndMonth = (String) param.getSwapData().get("file2EndMonth");
                file2StartMonth = (String) param.getSwapData().get("file1StartMonth");
                file2EndMonth = (String) param.getSwapData().get("file1EndMonth");
            }
        } else {
            cdcId1 = cfcId.get(1);
            cdcId2 = cfcId.get(0);
            file1StartMonth = (String) param.getSwapData().get("file2StartMonth");
            file1EndMonth = (String) param.getSwapData().get("file2EndMonth");
            file2StartMonth = (String) param.getSwapData().get("file1StartMonth");
            file2EndMonth = (String) param.getSwapData().get("file1EndMonth");
            file1FcDate = (String) param.getSwapData().get("file2FcDate");
            file2FcDate = (String) param.getSwapData().get("file1FcDate");
        }
        param.setSwapData("cfcId", cdcId1);
        file1ListEntity = service.getPartsInfo(param);
        param.setSwapData("cfcId", cdcId2);
        file2ListEntity = service.getPartsInfo(param);

        // Not exist the same P/N,Cus Code and Forecast Month check
        List<String> file1MonthList = new ArrayList<String>();
        List<String> file2MonthList = new ArrayList<String>();
        boolean sameCfcCodeFlg = false;
        boolean samePnFlg = false;
        boolean sameCfcMonthFlg = false;
        for (CPOCFFComEntity file1Entity : file1ListEntity) {

            List<CPOCFFComMonthlyEntity> file1MapLst = file1Entity.getCfcMonthlyLst();
            for (CPOCFFComMonthlyEntity cpocffComMonthlyEntity : file1MapLst) {
                file1MonthList.add(cpocffComMonthlyEntity.getCfcMonth());
            }

            for (CPOCFFComEntity file2Entity : file2ListEntity) {

                List<CPOCFFComMonthlyEntity> file2MapLst = file2Entity.getCfcMonthlyLst();
                for (CPOCFFComMonthlyEntity cpocffComMonthlyEntity : file2MapLst) {
                    file2MonthList.add(cpocffComMonthlyEntity.getCfcMonth());
                }

                sameCfcCodeFlg = false;
                samePnFlg = false;
                if (file2Entity.getTtcPartsNo().equals(file1Entity.getTtcPartsNo())
                        && file2Entity.getCustPartsNo().equals(file1Entity.getCustPartsNo())) {
                    samePnFlg = true;
                }
                if (file2Entity.getCustomerCode().equals(file1Entity.getCustomerCode())) {
                    sameCfcCodeFlg = true;
                }
                if (samePnFlg && sameCfcCodeFlg) {
                    break;
                }
            }
            if (samePnFlg && sameCfcCodeFlg) {
                break;
            }
        }

        HashSet<String> hs = new HashSet<String>(file1MonthList);
        file1MonthList.clear();
        file1MonthList.addAll(hs);
        HashSet<String> hs2 = new HashSet<String>(file2MonthList);
        file2MonthList.clear();
        file2MonthList.addAll(hs2);
        file1MonthList.retainAll(file2MonthList);
        if (file1MonthList.size() > 0) {
            sameCfcMonthFlg = true;
        }

        // set error message
        if (!sameCfcCodeFlg || !samePnFlg || !sameCfcMonthFlg) {
            List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
            BaseMessage message = new BaseMessage(MessageCodeConst.W1005_003);
            messageLists.add(message);
            throw new BusinessException(messageLists);
        }
        context.put(mapKey + "File1", file1ListEntity);
        context.put(mapKey + "File2", file2ListEntity);

        context.put(mapKey + "File1StartMonth", file1StartMonth);
        context.put(mapKey + "File1EndMonth", file1EndMonth);
        context.put(mapKey + "File2StartMonth", file2StartMonth);
        context.put(mapKey + "File2EndMonth", file2EndMonth);
        context.put(mapKey + "File1FcDate", file1FcDate);
        context.put(mapKey + "File2FcDate", file2FcDate);

        return result;
    }

    /**
     * Download Customer Forecast Comparison Report download
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "om/CPOCFF03/download",
        method = RequestMethod.POST)
    public void cfcComparisonReportDownload(PageParam param, HttpServletRequest request, HttpServletResponse response) {

        this.setCommonParam(param, request);
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        String mapKey = param.getSessionKey();

        param.setSwapData("file1StartMonth", context.get(mapKey + "File1StartMonth"));
        param.setSwapData("file2StartMonth", context.get(mapKey + "File2StartMonth"));
        param.setSwapData("file1EndMonth", context.get(mapKey + "File1EndMonth"));
        param.setSwapData("file2EndMonth", context.get(mapKey + "File2EndMonth"));
        param.setSwapData("file1FcDate", context.get(mapKey + "File1FcDate"));
        param.setSwapData("file2FcDate", context.get(mapKey + "File2FcDate"));
        param.setSwapData("file1ListEntity", context.get(mapKey + "File1"));
        param.setSwapData("file2ListEntity", context.get(mapKey + "File2"));

        context.remove(mapKey + "File1StartMonth");
        context.remove(mapKey + "File2StartMonth");
        context.remove(mapKey + "File1EndMonth");
        context.remove(mapKey + "File2EndMonth");
        context.remove(mapKey + "File1FcDate");
        context.remove(mapKey + "File2FcDate");
        context.remove(mapKey + "File1");
        context.remove(mapKey + "File2");

        String filename = FILE_NAME_EN + "_" + param.getClientTime() + ".xlsx";
        this.downloadExcelWithTemplate(StringUtil.formatMessage(filename, param.getClientTime()), param, request,
            response);

    }

    /**
     * Write To Customer Forecast Comparison Report Excel
     * 
     * @param param T
     * @param wbTemplate Workbook
     * @param wbOutput SXSSFWorkbook
     */
    @Override
    // 1:file1, 2:file2, 3:difference, 4:fluctuation ratio
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {
        
        String file1StartMonth = (String) param.getSwapData().get("file1StartMonth");
        String file1EndMonth = (String) param.getSwapData().get("file1EndMonth");
        String file2StartMonth = (String) param.getSwapData().get("file2StartMonth");
        String file2EndMonth = (String) param.getSwapData().get("file2EndMonth");
        String file1FcDate = (String) param.getSwapData().get("file1FcDate");
        String file2FcDate = (String) param.getSwapData().get("file2FcDate");
        @SuppressWarnings("unchecked")
        List<CPOCFFComEntity> file1ListEntity = (List<CPOCFFComEntity>) param.getSwapData().get("file1ListEntity");
        PartSortManager.sort(file1ListEntity, "ttcPartsNo", "oldTtcPartsNo");
        @SuppressWarnings("unchecked")
        List<CPOCFFComEntity> file2ListEntity = (List<CPOCFFComEntity>) param.getSwapData().get("file2ListEntity");
        PartSortManager.sort(file2ListEntity, "ttcPartsNo", "oldTtcPartsNo");
        
        int file1Num = file1ListEntity.size();
        int file2Num = file2ListEntity.size();
        int file1HeaderRow = IntDef.INT_SEVEN;
        int file2HeaderRow = file1HeaderRow + file1Num + IntDef.INT_FOUR;
        int file3HeaderRow = file2HeaderRow + file2Num + IntDef.INT_FIVE;
        int fileStartCol = IntDef.INT_ELEVEN;
        boolean actualFlg = false;
        Locale lang = MessageManager.getLanguage(param.getLanguage()).getLocale();
        // get sheet
        Sheet reportSheet = wbTemplate.getSheet(REPORT_SHEET);
        Sheet styleSheet = wbTemplate.getSheet(SHEET_STYLE);
        // all file headList
        List<List<String>> fileHeadList = new ArrayList<List<String>>();
        // get file1 & file2 cfcMonth list
        List<String> file1Header = service.getCfcMonth(file1StartMonth, file1EndMonth);
        fileHeadList.add(file1Header);
        List<String> file2Header = service.getCfcMonth(file2StartMonth, file2EndMonth);
        fileHeadList.add(file2Header);
        List<String> temp = new ArrayList<String>(file1Header);
        temp.retainAll(file2Header);
        fileHeadList.add(temp);

        
        String formartYM = "";
        if (param.getLanguage().equals(IntDef.INT_TWO)) {
            formartYM = DateTimeUtil.FORMAT_YYYYMM;
        } else {
            formartYM = DateTimeUtil.FORMAT_MMMYYYY;
        }
        
        
        // made header
        int[] distFromRowColumn1 = {file2HeaderRow, 1};
        copyRowByFrom(reportSheet, reportSheet, file1HeaderRow - 1, 1, 1, IntDef.INT_NINE, distFromRowColumn1, true);
        int[] distFromRowColumn2 = {file3HeaderRow, 1};
        copyRowByFrom(reportSheet, reportSheet, file1HeaderRow - 1, 1, 1, IntDef.INT_NINE, distFromRowColumn2, true);
        
        PoiUtil.setCellValue(reportSheet, IntDef.INT_THREE, 1,
            PoiUtil.getStringCellValue(reportSheet, IntDef.INT_THREE, 1) + " (" + file1FcDate + " & " + file2FcDate + ") ");
        
        PoiUtil.setCellStyle(reportSheet, file2HeaderRow - 1, 1, PoiUtil
            .getOrCreateCell(reportSheet, file1HeaderRow - IntDef.INT_TWO, 1).getCellStyle());
        PoiUtil.setCellValue(reportSheet, file2HeaderRow - 1, 1,
            MessageManager.getMessage("CPOCFF03_Label_Comparison2", lang));
        PoiUtil.setCellStyle(reportSheet, file3HeaderRow - 1, 1, PoiUtil
            .getOrCreateCell(reportSheet, file1HeaderRow - IntDef.INT_TWO, 1).getCellStyle());
        PoiUtil.setCellValue(reportSheet, file3HeaderRow - 1, 1,
            MessageManager.getMessage("CPOCFF03_Label_Comparison3", lang));
        
        int listIndex = 1;
        int fileHeaderRow = 0;
        String fileFcDate1;
        String fileFcDate2;
        String fileStartMonth;
        String fileEndMonth;
        int file1SumCol = 0;
        int file2SumCol = 0;
        int file3SumCol = 0;
        for (List<String> list : fileHeadList) {
            if(listIndex == 1){
                fileHeaderRow = file1HeaderRow;
                fileFcDate1 = file1FcDate;
                fileFcDate2 = file2FcDate;
                fileStartMonth = file1StartMonth;
                fileEndMonth = file1EndMonth;
            } else if(listIndex == IntDef.INT_TWO){
                fileHeaderRow = file2HeaderRow +1;
                fileFcDate1 = file2FcDate;
                fileFcDate2 = file1FcDate;
                fileStartMonth = file2StartMonth;
                fileEndMonth = file2EndMonth;   
            } else {
                fileHeaderRow = file3HeaderRow +1;
                fileFcDate1 = file1FcDate;
                fileFcDate2 = file2FcDate;
                fileStartMonth = list.get(0);
                fileEndMonth = list.get(list.size()-1);
            }
            fileStartCol = IntDef.INT_ELEVEN;
            reportSheet.setColumnWidth(fileStartCol - 1, FIVE_THOUSAND);
            int col;
            // file header
            for (String cfcMonth : list) {
                
                //1
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_THIRTEEN).getCellStyle());
    //            PoiUtil.setCellValue(reportSheet, file1HeaderRow, fileStartCol,
    //                DateTimeUtil.formatDate(formartYM, DateTimeUtil.parseMonth(cfcMonth)) + "\n" + "(" + file1FcDate + ")");
                PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol,
                    fileFcDate1);
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                PoiUtil.setCellValue(reportSheet, fileHeaderRow - 1, fileStartCol, DateTimeUtil.formatDate(formartYM, DateTimeUtil.parseMonth(cfcMonth)));
                reportSheet.setColumnWidth(fileStartCol, FIVE_THOUSAND);
                
                //2
                if(service.getActualFlg(cfcMonth)){
                    actualFlg = true;
                    PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol + 1,
                        PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_TWELVE).getCellStyle());
    //                PoiUtil.setCellValue(reportSheet, file1HeaderRow, fileStartCol + 1,
    //                    DateTimeUtil.formatDate(formartYM, DateTimeUtil.parseMonth(cfcMonth)) + "\n" +
    //                            "(" + PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_TWELVE) +")");
                    PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + 1,
                        PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_TWELVE));
                } else {
                    PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol + 1,
                        PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_THIRTEEN).getCellStyle());
    //                PoiUtil.setCellValue(reportSheet, file1HeaderRow, fileStartCol + 1,
    //                    DateTimeUtil.formatDate(formartYM, DateTimeUtil.parseMonth(cfcMonth)) + "\n" + "(" + file2FcDate +")");
                    PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + 1,
                        fileFcDate2);
                }
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol + 1,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                reportSheet.setColumnWidth(fileStartCol + 1, FIVE_THOUSAND);
                
                //3
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_TWO,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_TEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_TWO,
                    PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_FOURTEEN));
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol + IntDef.INT_TWO,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                reportSheet.setColumnWidth(fileStartCol + IntDef.INT_TWO, FIVE_THOUSAND);
                
                //4
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_THREE,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_TEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_THREE,
                    PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_FIFTEEN));
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol + IntDef.INT_THREE,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                reportSheet.setColumnWidth(fileStartCol + IntDef.INT_THREE, FIVE_THOUSAND);
                
                //merge cell
                reportSheet.addMergedRegion(new CellRangeAddress(fileHeaderRow - IntDef.INT_TWO,
                    fileHeaderRow - IntDef.INT_TWO, fileStartCol - 1, fileStartCol + IntDef.INT_FOUR - IntDef.INT_TWO));
                
                fileStartCol += IntDef.INT_FOUR;
            }
            
            col = fileStartCol;
            // sum1
            if(service.getMonthDiff(fileStartMonth, fileEndMonth) == IntDef.INT_TWO){
                
                //1
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_THIRTEEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol,
                    fileFcDate1);
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_THIRTEEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, fileHeaderRow - 1, fileStartCol, 
                    DateTimeUtil.formatDate(formartYM, DateTimeUtil.parseMonth(fileStartMonth)) +" ~ "+
                            DateTimeUtil.formatDate(formartYM, DateTimeUtil.parseMonth(service.getLastSecondMonth(fileEndMonth))));
                reportSheet.setColumnWidth(fileStartCol, FIVE_THOUSAND);
                
                //2
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol + 1,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_THIRTEEN).getCellStyle());
                if(actualFlg){
                    PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + 1,
                        PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_TWELVE) + " & " + fileFcDate2);
                } else {
                    PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + 1,
                        fileFcDate2);
                }
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol + 1,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                reportSheet.setColumnWidth(fileStartCol + 1, FIVE_THOUSAND);
                
                //3
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_TWO,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_TEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_TWO,
                    PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_FOURTEEN));
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol + IntDef.INT_TWO,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                reportSheet.setColumnWidth(fileStartCol + IntDef.INT_TWO, FIVE_THOUSAND);
                
                //4
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_THREE,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_TEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_THREE,
                    PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_FIFTEEN));
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol + IntDef.INT_THREE,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                reportSheet.setColumnWidth(fileStartCol + IntDef.INT_THREE, FIVE_THOUSAND);
                
                //merge cell
                reportSheet.addMergedRegion(new CellRangeAddress(fileHeaderRow - IntDef.INT_TWO,
                    fileHeaderRow - IntDef.INT_TWO, fileStartCol - 1, fileStartCol + IntDef.INT_FOUR - IntDef.INT_TWO));
                
                fileStartCol += IntDef.INT_FOUR;
            }
            
            // sum2
            if(service.getMonthDiff(fileStartMonth, fileEndMonth) != 0 ){
                
                //1
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_THIRTEEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol,
                    fileFcDate1);
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_THIRTEEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, fileHeaderRow - 1, fileStartCol, 
                    DateTimeUtil.formatDate(formartYM, DateTimeUtil.parseMonth(fileStartMonth)) +" ~ "+
                            DateTimeUtil.formatDate(formartYM, DateTimeUtil.parseMonth(fileEndMonth)));
                reportSheet.setColumnWidth(fileStartCol, FIVE_THOUSAND);
                
                //2
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol + 1,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_THIRTEEN).getCellStyle());
                if(actualFlg){
                    PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + 1,
                        PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_TWELVE) + " & " + fileFcDate2);
                } else {
                    PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + 1,
                        fileFcDate2);
                }
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol + 1,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                reportSheet.setColumnWidth(fileStartCol + 1, FIVE_THOUSAND);
                
                //3
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_TWO,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_TEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_TWO,
                    PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_FOURTEEN));
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol + IntDef.INT_TWO,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                reportSheet.setColumnWidth(fileStartCol + IntDef.INT_TWO, FIVE_THOUSAND);
                
                //4
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_THREE,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_TEN).getCellStyle());
                PoiUtil.setCellValue(reportSheet, fileHeaderRow, fileStartCol + IntDef.INT_THREE,
                    PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_FIFTEEN));
                PoiUtil.setCellStyle(reportSheet, fileHeaderRow - 1, fileStartCol + IntDef.INT_THREE,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                reportSheet.setColumnWidth(fileStartCol + IntDef.INT_THREE, FIVE_THOUSAND);
                
                //merge cell
                reportSheet.addMergedRegion(new CellRangeAddress(fileHeaderRow - IntDef.INT_TWO,
                    fileHeaderRow - IntDef.INT_TWO, fileStartCol - 1, fileStartCol + IntDef.INT_FOUR - IntDef.INT_TWO));
                
                fileStartCol += IntDef.INT_FOUR;
            }
            
            if(listIndex == 1){
                file1SumCol = fileStartCol - col;
            } else if(listIndex == IntDef.INT_TWO){
                file2SumCol = fileStartCol - col;
            } else {
                file3SumCol = fileStartCol - col;
            }
            
            listIndex++;
        }
        
        // get file3 parts
        List<CPOCFFComEntity> file3ListEntity = new ArrayList<CPOCFFComEntity>();;
        for (CPOCFFComEntity entity1 : file1ListEntity) {
            for (CPOCFFComEntity entity2 : file2ListEntity) {
                if (entity1.getTtcPartsNo().equals(entity2.getTtcPartsNo())
                        && entity1.getCustPartsNo().equals(entity2.getCustPartsNo())
                        && entity1.getCustomerCode().equals(entity2.getCustomerCode())) {
                    file3ListEntity.add(entity1);
                }
            }
        }
        
        List<List<CPOCFFComEntity>> filePartsList = new ArrayList<List<CPOCFFComEntity>>();
        filePartsList.add(file1ListEntity);
        filePartsList.add(file2ListEntity);
        filePartsList.add(file3ListEntity);
        
        int partsListIndex = 1;
        int startRow = 0;
        int maxColNum = 0;
        int fileSumCol = 0;
        String listFlg;
        List<CPOCFFComEntity> fileListEntity = new ArrayList<CPOCFFComEntity>();
        for (@SuppressWarnings("unused") List<CPOCFFComEntity> listEntity : filePartsList) {
            if(partsListIndex == 1){
                fileListEntity = filePartsList.get(0);
                startRow = file1HeaderRow + 1;
                maxColNum = PoiUtil.getRow(reportSheet, file1HeaderRow).getPhysicalNumberOfCells();
                fileSumCol = file1SumCol;
                listFlg = "file1";
            } else if(partsListIndex == IntDef.INT_TWO) {
                fileListEntity = filePartsList.get(1);
                startRow =  file2HeaderRow + IntDef.INT_TWO;
                maxColNum = PoiUtil.getRow(reportSheet, file2HeaderRow +1).getPhysicalNumberOfCells();
                fileSumCol = file2SumCol;
                listFlg = "file2";
                
            } else {
                fileListEntity = filePartsList.get(IntDef.INT_TWO);
                startRow =  file3HeaderRow + IntDef.INT_TWO;
                maxColNum = PoiUtil.getRow(reportSheet, file3HeaderRow +1).getPhysicalNumberOfCells();
                fileSumCol = file3SumCol;
                listFlg = "file3";
                
            }
            int j = 1;
            for (CPOCFFComEntity cpocffComEntity : fileListEntity) {
                fileStartCol = IntDef.INT_TWO;
                
                Cell[] templateCells = getTemplateCells(SHEET_STYLE, IntDef.INT_FOUR, wbTemplate);
                Object[] dataArray = new Object[] { null, null, null, null, null, null, null, null, null, null };
                createOneDataRowByTemplate(reportSheet, startRow - 1, fileStartCol - IntDef.INT_TWO, templateCells,
                    dataArray);
                PoiUtil.setCellValue(reportSheet, startRow, fileStartCol++, cpocffComEntity.getTtcPartsNo());
                PoiUtil.setCellValue(reportSheet, startRow, fileStartCol++, cpocffComEntity.getCustPartsNo());
                PoiUtil.setCellValue(reportSheet, startRow, fileStartCol++, cpocffComEntity.getCustomerCode());
                PoiUtil.setCellValue(
                    reportSheet,
                    startRow,
                    fileStartCol++,
                    CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.PARTS_TYPE,
                        cpocffComEntity.getPartsType()));
                PoiUtil.setCellValue(reportSheet, startRow, fileStartCol++, cpocffComEntity.getCarModel());
                // uom
                int digits = MasterManager.getUomDigits(cpocffComEntity.getUomCode());
                String uomCode = cpocffComEntity.getUomCode();
                CellStyle nonInvoiceQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                PoiUtil.setCellStyle(reportSheet, startRow, fileStartCol, nonInvoiceQtyStyle);
                PoiUtil.setCellValue(reportSheet, startRow, fileStartCol++,
                    StringUtil.formatBigDecimal(uomCode, cpocffComEntity.getOrderLot()));
                PoiUtil.setCellValue(reportSheet, startRow, fileStartCol++, cpocffComEntity.getOldTtcPartsNo());
                PoiUtil.setCellValue(reportSheet, startRow, fileStartCol++, cpocffComEntity.getPartsNameEn());
                PoiUtil.setCellValue(reportSheet, startRow, fileStartCol++, cpocffComEntity.getPartsNameCn());

                // set cfc info
                List<CPOCFFComMonthlyEntity> comMonthlyEntitylist = cpocffComEntity.getCfcMonthlyLst();
                
                @SuppressWarnings("unused")
                String headerFormartYM = "";
                @SuppressWarnings("unused")
                String headerFormartYMD = "";
                
                String dateHeader1 = "";
                String monthHeader = "";
                BigDecimal file1Sum = new BigDecimal(0) ;
                BigDecimal file2Sum = new BigDecimal(0) ;
                List<Object> secondSum = new ArrayList<Object>();
                List<Object> dataFlgList = new ArrayList<Object>();
                boolean haveMonthData = false;
                
                for(int i = fileStartCol; i < maxColNum - fileSumCol; i += IntDef.INT_FOUR ){
                    
                    dateHeader1 = PoiUtil.getStringCellValue(reportSheet, startRow - j, i);

                    BigDecimal first = new BigDecimal(0) ;

                    Object second = new Object();
                    
                    
                    if(!StringUtil.isEmpty(PoiUtil.getStringCellValue(reportSheet, startRow - j -1, i))){
                        monthHeader = PoiUtil.getStringCellValue(reportSheet, startRow - j -1, i);
                    }
                    
                    if(StringUtil.isEmpty(monthHeader)){
                        monthHeader = PoiUtil.getStringCellValue(reportSheet, startRow - j -1, i);
                    }
                    
                    if (monthHeader.length() == IntDef.INT_EIGHT) {
                        headerFormartYM = DateTimeUtil.FORMAT_MMMYYYY;
                        headerFormartYMD = DateTimeUtil.FORMAT_DDMMMYYYY;
                    } else {
                        headerFormartYM = DateTimeUtil.FORMAT_YYYYMM;
                        headerFormartYMD = DateTimeUtil.FORMAT_DATE_YYYYMMDD;
                    }
                    
                    for (CPOCFFComMonthlyEntity cpocffComMonthlyEntity : comMonthlyEntitylist) {
                        
                        if (cpocffComMonthlyEntity.getReceiveDate().equals(DateTimeUtil.parseDate(dateHeader1))
                                && cpocffComMonthlyEntity.getCfcMonth().equals(
                            DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                                DateTimeUtil.parseMonth(monthHeader)))) {
                        
                            //1
                            if (cpocffComMonthlyEntity.getReceiveDate().equals(DateTimeUtil.parseDate(dateHeader1))
                                    && cpocffComMonthlyEntity.getCfcMonth().equals(
                                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                                            DateTimeUtil.parseMonth(monthHeader)))) {
                                CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                                PoiUtil.setCellStyle(reportSheet, startRow, i, cfcQtyStyle);
                                PoiUtil.setCellValue(reportSheet, startRow, i,
                                    StringUtil.formatBigDecimal(uomCode, cpocffComMonthlyEntity.getCfcQty()));
                                first = cpocffComMonthlyEntity.getCfcQty();
                                file1Sum = DecimalUtil.add(file1Sum, first);
                            }
                            
                            //2
                            if (cpocffComMonthlyEntity.getActualQty() != null) {
                                CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_RED, digits);
                                PoiUtil.setCellStyle(reportSheet, startRow, i + 1, cfcQtyStyle);
                                PoiUtil.setCellValue(reportSheet, startRow, i + 1,
                                    StringUtil.formatBigDecimal(uomCode, cpocffComMonthlyEntity.getActualQty()));
                                second = cpocffComMonthlyEntity.getActualQty();
                            } else {
                                if(listFlg.equals("file2")){
                                    Object qty = service.getCfcQty(cpocffComEntity.getTtcPartsNo(),
                                        cpocffComEntity.getCustPartsNo(), cpocffComEntity.getCustomerCode(),
                                        DateTimeUtil.parseDate(file1FcDate), cpocffComMonthlyEntity.getCfcMonth(),
                                        file1ListEntity);
                                    second = qty;
                                    if(qty instanceof String){
                                        PoiUtil.setCellStyle(reportSheet, startRow, i + 1, 
                                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                                        PoiUtil.setCellValue(reportSheet, startRow, i + 1,
                                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                                    } else {
                                        CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                                        PoiUtil.setCellStyle(reportSheet, startRow, i + 1, cfcQtyStyle); 
                                        PoiUtil.setCellValue(reportSheet, startRow, i + 1,
                                            StringUtil.formatBigDecimal(uomCode, (BigDecimal)qty));
                                    }
                                    
                                } else {
                                    Object qty = service.getCfcQty(cpocffComEntity.getTtcPartsNo(),
                                        cpocffComEntity.getCustPartsNo(), cpocffComEntity.getCustomerCode(),
                                        DateTimeUtil.parseDate(file2FcDate), cpocffComMonthlyEntity.getCfcMonth(),
                                        file2ListEntity);
                                    second = qty;
                                    if(qty instanceof String){
                                        PoiUtil.setCellStyle(reportSheet, startRow, i + 1, 
                                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                                        PoiUtil.setCellValue(reportSheet, startRow, i + 1,
                                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                                    } else {
                                        CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                                        PoiUtil.setCellStyle(reportSheet, startRow, i + 1, cfcQtyStyle); 
                                        PoiUtil.setCellValue(reportSheet, startRow, i + 1,
                                            StringUtil.formatBigDecimal(uomCode, (BigDecimal)qty));
                                    }
                                }
                                if(second instanceof String){
                                    secondSum.add(second);
                                } else {
                                    file2Sum = DecimalUtil.add(file2Sum, (BigDecimal)second);
                                } 
                            }
                            
                            //3
                            if(second instanceof String){
                                PoiUtil.setCellStyle(reportSheet, startRow, i + IntDef.INT_TWO, 
                                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_THIRTEEN).getCellStyle());
                                PoiUtil.setCellValue(reportSheet, startRow, i + IntDef.INT_TWO,
                                    PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_THIRTEEN));
                            } else { 
                                BigDecimal secondQty = (BigDecimal)second;
                                CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_BLUE, digits);
                                PoiUtil.setCellStyle(reportSheet, startRow, i + IntDef.INT_TWO, cfcQtyStyle);
                                PoiUtil.setCellValue(reportSheet, startRow, i + IntDef.INT_TWO,
                                    DecimalUtil.subtract(secondQty, first).doubleValue());
                            }
                            
                            //4
                            if(second instanceof String){
                                PoiUtil.setCellStyle(reportSheet, startRow, i + IntDef.INT_THREE, 
                                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                                PoiUtil.setCellValue(reportSheet, startRow, i + IntDef.INT_THREE,
                                    PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                            } else { 
                                BigDecimal secondQty = (BigDecimal)second;
                                BigDecimal diff = DecimalUtil.subtract(secondQty, first);
                                
                                PoiUtil.setCellStyle(reportSheet, startRow, i + IntDef.INT_THREE, 
                                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_FOURTEEN).getCellStyle());
                                PoiUtil.setCellValue(reportSheet, startRow, i + IntDef.INT_THREE,
                                    DecimalUtil.divide(diff, first, BigDecimal.ROUND_HALF_UP));
                            }

                        
                        }
                
                
                    }
                    
                    boolean haveDataFlg = false;
                    for (CPOCFFComMonthlyEntity cpocffComMonthlyEntity : comMonthlyEntitylist) {
                        if (cpocffComMonthlyEntity.getCfcMonth().equals(
                            DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                                DateTimeUtil.parseMonth(monthHeader)))) {
                            haveDataFlg = true;
                            haveMonthData = true;
                            break;
                        }
                    }
                    if(!haveDataFlg){
                        //1
                        PoiUtil.setCellStyle(reportSheet, startRow, i, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, i,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                        //2
                        if(listFlg.equals("file2")){
                            Object qty = service.getCfcQty(cpocffComEntity.getTtcPartsNo(), cpocffComEntity
                                .getCustPartsNo(), cpocffComEntity.getCustomerCode(), DateTimeUtil
                                .parseDate(file1FcDate),
                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, DateTimeUtil.parseMonth(PoiUtil
                                    .getStringCellValue(reportSheet, fileHeaderRow - 1, i))), file1ListEntity);
                            
                            if(qty instanceof String){
                                PoiUtil.setCellStyle(reportSheet, startRow, i + 1, 
                                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                                PoiUtil.setCellValue(reportSheet, startRow, i + 1,
                                    PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                            } else {
                                CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                                PoiUtil.setCellStyle(reportSheet, startRow, i + 1, cfcQtyStyle); 
                                PoiUtil.setCellValue(reportSheet, startRow, i + 1,
                                    StringUtil.formatBigDecimal(uomCode, (BigDecimal)qty));
                            }  
                           
                        } else {
                            Object qty = service.getCfcQty(cpocffComEntity.getTtcPartsNo(), cpocffComEntity
                                .getCustPartsNo(), cpocffComEntity.getCustomerCode(), DateTimeUtil
                                .parseDate(file2FcDate),
                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, DateTimeUtil.parseMonth(PoiUtil
                                    .getStringCellValue(reportSheet, fileHeaderRow - 1, i))), file2ListEntity);
                            
                            if(qty instanceof String){
                                PoiUtil.setCellStyle(reportSheet, startRow, i + 1, 
                                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                                PoiUtil.setCellValue(reportSheet, startRow, i + 1,
                                    PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                            } else {
                                CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                                PoiUtil.setCellStyle(reportSheet, startRow, i + 1, cfcQtyStyle); 
                                PoiUtil.setCellValue(reportSheet, startRow, i + 1,
                                    StringUtil.formatBigDecimal(uomCode, (BigDecimal)qty));
                            } 
                        }
                        
                        
                        //3
                        PoiUtil.setCellStyle(reportSheet, startRow, i + IntDef.INT_TWO, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_THIRTEEN).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, i + IntDef.INT_TWO,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_THIRTEEN));
                        //4
                        PoiUtil.setCellStyle(reportSheet, startRow, i + IntDef.INT_THREE, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, i + IntDef.INT_THREE,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE)); 
                        dataFlgList.add(true);
                    }
                    
                }
                
                // sum data : first to second end
                if(fileSumCol == IntDef.INT_EIGHT){
                    CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                    BigDecimal lastQty1 = null;
                    if(dataFlgList.size() > 1){
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - IntDef.INT_SIX, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum - IntDef.INT_SIX,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                    } else {
                        if(NA.equals(PoiUtil.getStringCellValue(reportSheet, startRow, maxColNum - IntDef.INT_TEN))){
                            lastQty1 = new BigDecimal(0);
                        } else {
                            lastQty1 = new BigDecimal(PoiUtil.getStringCellValue(reportSheet, startRow, maxColNum - IntDef.INT_TEN).replace(",",""));
                        }
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - IntDef.INT_SIX, cfcQtyStyle); 
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum - IntDef.INT_SIX, DecimalUtil.subtract(file1Sum, lastQty1));
                    }
                    
                    BigDecimal lastQty2 = null;        
                    if(secondSum.size() > 1 || NA.equals(PoiUtil.getStringCellValue(reportSheet, startRow, IntDef.INT_TWELVE))){
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - IntDef.INT_FIVE, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum - IntDef.INT_FIVE,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                    } else {
                        cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - IntDef.INT_FIVE, cfcQtyStyle); 
                        if(NA.equals(PoiUtil.getStringCellValue(reportSheet, startRow, maxColNum - IntDef.INT_NINE)) || dataFlgList.size() > 0){
                            lastQty2 = new BigDecimal(0);
                        } else {
                            lastQty2 = new BigDecimal(PoiUtil.getStringCellValue(reportSheet, startRow, maxColNum - IntDef.INT_NINE).replace(",",""));
                        }
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum - IntDef.INT_FIVE, DecimalUtil.subtract(file2Sum, lastQty2));
                    }
                    
                    if(secondSum.size() > 1 || NA.equals(PoiUtil.getStringCellValue(reportSheet, startRow, IntDef.INT_TWELVE))){
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - IntDef.INT_FOUR, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_THIRTEEN).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum - IntDef.INT_FOUR,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_THIRTEEN));
                    } else { 
                        cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_BLUE, digits);
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - IntDef.INT_FOUR, cfcQtyStyle);
                        PoiUtil.setCellValue(
                            reportSheet,
                            startRow,
                            maxColNum - IntDef.INT_FOUR,
                            DecimalUtil.subtract(DecimalUtil.subtract(file2Sum, lastQty2),
                                DecimalUtil.subtract(file1Sum, lastQty1)).doubleValue());
                    }
                    
                    if(secondSum.size() > 1 || NA.equals(PoiUtil.getStringCellValue(reportSheet, startRow, IntDef.INT_TWELVE))){
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - IntDef.INT_THREE, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum - IntDef.INT_THREE,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                    } else { 
                        
                        BigDecimal diff = DecimalUtil.subtract(DecimalUtil.subtract(file2Sum, lastQty2),
                            DecimalUtil.subtract(file1Sum, lastQty1));
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - IntDef.INT_THREE, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_FOURTEEN).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum - IntDef.INT_THREE,
                            
                            DecimalUtil.divide(diff, (DecimalUtil.subtract(file1Sum, lastQty1)), BigDecimal.ROUND_HALF_UP));
                    }  
                }
                
                // sum data : first to end
                if(fileSumCol != 0){    
                    CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                    if(dataFlgList.size() > 0){
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - IntDef.INT_TWO, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum - IntDef.INT_TWO,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                    } else {
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - IntDef.INT_TWO, cfcQtyStyle);
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum - IntDef.INT_TWO, file1Sum);
                    }
                    
                    if(secondSum.size() > 0){
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - 1, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum - 1,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                    } else {
                        cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum - 1, cfcQtyStyle); 
                        
                        if(!haveMonthData){
                            PoiUtil.setCellValue(reportSheet, startRow, maxColNum - 1, file2Sum);
                        } else {
                            BigDecimal lastQty;
                            if(NA.equals(PoiUtil.getStringCellValue(reportSheet, startRow, maxColNum - IntDef.INT_NINE))){
                                lastQty = new BigDecimal(0);
                            } else {
                                lastQty = new BigDecimal(PoiUtil.getStringCellValue(reportSheet, startRow, maxColNum - IntDef.INT_NINE).replace(",",""));
                            }
                            PoiUtil.setCellValue(reportSheet, startRow, maxColNum - 1, DecimalUtil.add(file2Sum, lastQty));
                        }
                        
                    }
                    
                    if(secondSum.size() > 0){
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum , 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_THIRTEEN).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum ,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_THIRTEEN));
                    } else { 
                        
                        cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_BLUE, digits);
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum , cfcQtyStyle);
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum ,
                            DecimalUtil.subtract(file2Sum, file1Sum).doubleValue());
                    }
                    
                    if(secondSum.size() > 0){
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum + 1, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum + 1,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_FOUR, IntDef.INT_TWELVE));
                    } else { 
                        
                        BigDecimal diff = DecimalUtil.subtract(file2Sum, file1Sum);
                        PoiUtil.setCellStyle(reportSheet, startRow, maxColNum + 1, 
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_FOUR, IntDef.INT_FOURTEEN).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, maxColNum + 1,
                            DecimalUtil.divide(diff, file1Sum, BigDecimal.ROUND_HALF_UP));
                    }
                }
                
                j++;
                startRow++;
            
            }
        
            partsListIndex++;
        }
        
        String sheetName = "";
        if(param.getLanguage().equals(IntDef.INT_TWO)){
            sheetName = SHEET_NAME_CN;
        } else {
            sheetName = SHEET_NAME_EN;
        }
        wbTemplate.setSheetName(0, sheetName);
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(UOM_STYLE_RED));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(UOM_STYLE_WHITE));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(UOM_STYLE_BLUE));
        
    }

    @Override
    protected String getFileId() {
        return FileId.CPOCFF03;
    }
}