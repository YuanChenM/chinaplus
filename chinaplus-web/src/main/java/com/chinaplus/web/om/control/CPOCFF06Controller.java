/**
 * CPOCFF06Controller.java
 * 
 * @screen CPOCFF06
 * @author li_feng
 */

package com.chinaplus.web.om.control;

import java.io.File;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
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
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.PartSortManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOCFFComDailyEntity;
import com.chinaplus.web.om.entity.CPOCFFComEntity;
import com.chinaplus.web.om.entity.CPOCFFComMonthlyEntity;
import com.chinaplus.web.om.service.CPOCFF06Service;
import com.chinaplus.web.om.service.CPOCFFComService;

/**
 * Download Customer Forecast Controller.
 */
@Controller
public class CPOCFF06Controller extends BaseFileController {

    /** ZIP file content type */
    private static final String ZIP_CONTENT_TYPE = "application/x-zip-compressed;charset=UTF-8";
    /** Download zip file name */
    private static final String DOWNLOAD_ZIP_FILE_NAME = "CustomerForecast_{0}.zip";
    /** Download excel file name */
    private static final String DOWNLOAD_EXCEL_FILE_NAME = "{0}.xlsx";
    /** by day */
    private static final String BY_DAY = "ByDay";
    /** report sheet */
    private static final String VV_SHEET = "OutputSample(V-V)";
    /** report sheet */
    private static final String AISIN_SHEET = "OutputSample(AISIN)";
    /** style sheet */
    private static final String SHEET_STYLE = "style";
    /** uom style grey */
    private static final String UOM_STYLE_GREY = "uom_style_grey";
    /** uom style white */
    private static final String UOM_STYLE_WHITE = "uom_style_white";
    /** header row */
    private static final Integer HEADER_ROW = 9;
    /** data start row */
    private static final Integer DATA_START_ROW = 10;
    /** start col */
    private static final Integer START_COL = 11;
    /** partsStartCol */
    private static final Integer PARTS_START_COL = 1;
    /** col width */
    private static final Integer FIVE_THOUSAND = 5000;
    
    @Autowired
    private CPOCFFComService service;
    
    @Autowired
    private CPOCFF06Service cpocff06Service;

    /**
     * Download Customer Forecast History Check
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     * @throws ParseException E
     */
    @RequestMapping(value = "/om/CPOCFF06/custdownloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> custdownloadcheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) throws ParseException {
        
        BaseResult<String> result = new BaseResult<String>();
        return result;
    }

    /**
     * Download Customer Forecast History
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the exception
     */
    @RequestMapping(value = "om/CPOCFF06/download",
        method = RequestMethod.POST)
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        this.setCommonParam(param, request);
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

            // Generate temporary folder path in websphere
            String tempFolderPath = ConfigUtil.get(Properties.UPLOAD_PATH_CFC) + UUID.randomUUID().toString();
            // Generate temporary folder path in local
            //String tempFolderPath = "E:/common/cfc/" + UUID.randomUUID().toString();
            
            // Create temporary folder
            File tempFolder = new File(tempFolderPath);
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }

            @SuppressWarnings("unchecked")
            List<Integer> selectedId = (List<Integer>) param.getSwapData().get("cfcIdList");
            @SuppressWarnings("unchecked")
            List<String> selectedNo = (List<String>) param.getSwapData().get("cfcNoList");
            for (int i = 0; i < selectedId.size(); i++) {
                Integer cfcId = selectedId.get(i);

                String fileName = StringUtil.formatMessage(DOWNLOAD_EXCEL_FILE_NAME, selectedNo.get(i));

                param.setSwapData("cfcId", cfcId);
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
     * Write To Customer Forecast History Excel
     * 
     * @param param T
     * @param wbTemplate Workbook
     * @param wbOutput SXSSFWorkbook
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput, String fileId) {
        String downLoadType = (String)param.getSwapData().get("downLoadBy");
        List<String> fcMonthList = new ArrayList<String>();
        LinkedHashMap<Date, Integer> workingDayMap = new LinkedHashMap<Date, Integer>();
        
        List<CPOCFFComEntity> listEntity = new ArrayList<CPOCFFComEntity>();
        if(downLoadType.equals(BY_DAY)){
            listEntity = cpocff06Service.getPartsInfoByDaily(param);
            PartSortManager.sort(listEntity, "ttcPartsNo", "oldTtcPartsNo");
            String startFcMonth = listEntity.get(0).getFirstFcMonth();
            String endFcMonth = listEntity.get(0).getLastFcMonth();
            param.setSwapData("startFcMonth", DateTimeUtil.parseMonth(startFcMonth));
            param.setSwapData("endFcMonth", DateTimeUtil.lastDay(DateTimeUtil.parseMonth(endFcMonth)));
            param.setSwapData("customerCode", listEntity.get(0).getCustomerCode());
            workingDayMap = cpocff06Service.getCalendarList(param);
        } else {
            listEntity = cpocff06Service.getPartsInfoByMonth(param);
            PartSortManager.sort(listEntity, "ttcPartsNo", "oldTtcPartsNo");
            String startFcMonth = listEntity.get(0).getFirstFcMonth();
            String endFcMonth = listEntity.get(0).getLastFcMonth();
            fcMonthList = cpocff06Service.getCfcMonth(startFcMonth, endFcMonth);
        }
        
        // get sheet
        Sheet reportSheet = null;
        Sheet styleSheet = wbTemplate.getSheet(SHEET_STYLE);
        
        Integer businessPattern = listEntity.get(0).getBusinessPattern();
        String customerCode = listEntity.get(0).getCustomerCode();
        String cfcNo = listEntity.get(0).getCfcNo();
        String deleteSheet;
        if(businessPattern.equals(BusinessPattern.V_V)){
            reportSheet = wbTemplate.getSheet(VV_SHEET);
            deleteSheet = AISIN_SHEET;
        } else {
            reportSheet = wbTemplate.getSheet(AISIN_SHEET);
            deleteSheet = VV_SHEET;
        }

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
        reportSheet.setColumnWidth(startCol - 1, FIVE_THOUSAND);
        // made header
        if(downLoadType.equals(BY_DAY)){
            for(Map.Entry<Date, Integer> entry : workingDayMap.entrySet()) {
                if(entry.getValue().equals(0)){
                    PoiUtil.setCellStyle(reportSheet, headerRow, startCol,
                        PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_ELEVEN).getCellStyle());
                } else {
                    PoiUtil.setCellStyle(reportSheet, headerRow, startCol,
                        PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                }
                PoiUtil.setCellValue(reportSheet, headerRow, startCol,
                    DateTimeUtil.formatDate(formartYMD, entry.getKey()));
                reportSheet.setColumnWidth(startCol, FIVE_THOUSAND);
                PoiUtil.setCellStyle(reportSheet, headerRow - 1, startCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                startCol++;
            }
            
            PoiUtil.setCellValue(reportSheet, headerRow - 1, START_COL, 
                PoiUtil.getStringCellValue(styleSheet,  IntDef.INT_TWO, IntDef.INT_FOURTEEN));
            PoiUtil.setCellValue(reportSheet, 1, 1,
                PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_FOURTEEN));
        } else {
            for (String month : fcMonthList) {
                PoiUtil.setCellStyle(reportSheet, headerRow, startCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                PoiUtil.setCellStyle(reportSheet, headerRow - 1, startCol,
                    PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                PoiUtil.setCellValue(reportSheet, headerRow, startCol,
                    DateTimeUtil.formatDate(formartYM, DateTimeUtil.parseMonth(month)));
                reportSheet.setColumnWidth(startCol, FIVE_THOUSAND);
                startCol++;
            }
            
            PoiUtil.setCellValue(reportSheet, headerRow - 1, START_COL, 
                PoiUtil.getStringCellValue(styleSheet, IntDef.INT_TWO, IntDef.INT_FIFTEEN));
            PoiUtil.setCellValue(reportSheet, 1, 1,
                PoiUtil.getStringCellValue(styleSheet, IntDef.INT_THREE, IntDef.INT_FIFTEEN));
        }
    
        reportSheet.addMergedRegion(new CellRangeAddress(headerRow - IntDef.INT_TWO, headerRow - IntDef.INT_TWO, START_COL -1,
            reportSheet.getRow(HEADER_ROW - 1).getLastCellNum() - 1));
        PoiUtil.setCellValue(reportSheet, IntDef.INT_SEVEN, IntDef.INT_THREE, CodeCategoryManager.getCodeName(param.getLanguage(),
            CodeMasterCategory.BUSINESS_PATTERN, businessPattern));
        
        // set A3~A5 data
        String targetFormat = "";
        if (param.getLanguage().equals(1)) {
            targetFormat = DateTimeUtil.FORMAT_DDMMMYYYY;
        } else {
            targetFormat = DateTimeUtil.FORMAT_DATE_YYYYMMDD;
        }
        String A3 = MessageManager.getMessage("CPOCFF06_Label_ForecastNo", Language.ENGLISH.getLocale()) + ": " + cfcNo;
        String A4 = MessageManager.getMessage("CPOCFF06_Label_ForecastReceivedDate", Language.ENGLISH.getLocale())
                + ": " + DateTimeUtil.formatDate(targetFormat, listEntity.get(0).getFcDate());
        String A5 = MessageManager.getMessage("CPOCFF06_Label_UploadedDate", Language.ENGLISH.getLocale()) + ": "
                + DateTimeUtil.formatDate(targetFormat, listEntity.get(0).getUploadedTime());

        Font font = wbTemplate.createFont();
        CellStyle cellStyle = wbTemplate.createCellStyle();
        font.setFontName("Arial");
        cellStyle.setFont(font);
        PoiUtil.setCellStyle(reportSheet, IntDef.INT_THREE, IntDef.INT_ONE, cellStyle);
        PoiUtil.setCellStyle(reportSheet, IntDef.INT_FOUR, IntDef.INT_ONE, cellStyle);
        PoiUtil.setCellStyle(reportSheet, IntDef.INT_FIVE, IntDef.INT_ONE, cellStyle);
        PoiUtil.setCellValue(reportSheet, IntDef.INT_THREE, IntDef.INT_ONE, A3);
        PoiUtil.setCellValue(reportSheet, IntDef.INT_FOUR, IntDef.INT_ONE, A4);
        PoiUtil.setCellValue(reportSheet, IntDef.INT_FIVE, IntDef.INT_ONE, A5);      

        // write parts data
        int startRow = DATA_START_ROW;
        int maxColNum = PoiUtil.getRow(reportSheet, headerRow).getPhysicalNumberOfCells();
        int no = 1;
        for (CPOCFFComEntity comEntity : listEntity) {
            int partsStartCol = PARTS_START_COL;
            
            // set parts data
            Cell[] templateCells = getTemplateCells(SHEET_STYLE, IntDef.INT_THREE, wbTemplate);
            Object[] dataArray = new Object[] { null, null, null, null, null, null, null, null, null, null };
            createOneDataRowByTemplate(reportSheet, startRow - 1, partsStartCol - 1, templateCells,
                dataArray);
            
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, no++);
            if(businessPattern.equals(BusinessPattern.V_V)){
                PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getTtcPartsNo());
            } else {
                PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getCustPartsNo());
            }
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getCustomerCode());
            if(businessPattern.equals(BusinessPattern.AISIN)){
                PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getTtcPartsNo());
            } else {
                PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getCustPartsNo());
            }
            
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
            CellStyle nonInvoiceQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_GREY, digits);
            PoiUtil.setCellStyle(reportSheet, startRow, partsStartCol, nonInvoiceQtyStyle);
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++,
                StringUtil.formatBigDecimal(uomCode, comEntity.getOrderLot()));
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getOldTtcPartsNo());
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getPartsNameEn());
            PoiUtil.setCellValue(reportSheet, startRow, partsStartCol++, comEntity.getPartsNameCn());

            // set cfc info
            
            if(downLoadType.equals(BY_DAY)){
                List<CPOCFFComDailyEntity> comDailyEntityList = comEntity.getAdjustDateLst();
                String header = "";
                for (int i = partsStartCol; i <= maxColNum; i++) {
                    if(!StringUtil.isEmpty(PoiUtil.getStringCellValue(reportSheet, headerRow, i))){
                        header = PoiUtil.getStringCellValue(reportSheet, headerRow, i);
                    }
                
                    for (CPOCFFComDailyEntity cpocffComDailyEntity : comDailyEntityList) {
                        // set cfc QTY
                        if (cpocffComDailyEntity.getCfcDate().equals(DateTimeUtil.parseDate(header))) {
                            CellStyle cfcQtyStyle;
                            if(cpocffComDailyEntity.getWorkingFlag().equals(1)){
                                cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                            } else {
                                cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_GREY, digits);
                            }
                            PoiUtil.setCellStyle(reportSheet, startRow, i, cfcQtyStyle);
                            // PoiUtil.setCellValue(reportSheet, startRow, i,
                            //     StringUtil.formatBigDecimal(uomCode, cpocffComDailyEntity.getCfcQty()));
                            PoiUtil.setCellValue(reportSheet, startRow, i, cpocffComDailyEntity.getCfcQty());
                            break;
                        }
                    }
                    if (StringUtil.isEmpty(PoiUtil.getStringCellValue(reportSheet, startRow, i))) {
                        if (workingDayMap.get(
                            DateTimeUtil.parseDate(PoiUtil.getStringCellValue(reportSheet, HEADER_ROW, i))).equals(1)) {
                            PoiUtil.setCellStyle(reportSheet, startRow, i,
                                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                        } else {
                            PoiUtil.setCellStyle(reportSheet, startRow, i,
                                PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_ELEVEN).getCellStyle());
                        }
                        
                        PoiUtil.setCellValue(reportSheet, startRow, i,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_TWO, IntDef.INT_SIXTEEN));
                    }
                    
                }
                
            } else {
                List<CPOCFFComMonthlyEntity> comMonthlyEntitylist = comEntity.getCfcMonthlyLst();         
                String header = "";
                for (int i = partsStartCol; i <= maxColNum; i++) {
                    if(!StringUtil.isEmpty(PoiUtil.getStringCellValue(reportSheet, headerRow, i))){
                        header = PoiUtil.getStringCellValue(reportSheet, headerRow, i);
                    }
    
                    for (CPOCFFComMonthlyEntity cpocffComMonthlyEntity : comMonthlyEntitylist) {
                        // set cfc QTY
                        if (cpocffComMonthlyEntity.getCfcMonth().equals(
                            DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, DateTimeUtil.parseMonth(header)))) {
                            CellStyle cfcQtyStyle = super.getDecimalStyle(wbTemplate, UOM_STYLE_WHITE, digits);
                            PoiUtil.setCellStyle(reportSheet, startRow, i, cfcQtyStyle);
                            // PoiUtil.setCellValue(reportSheet, startRow, i,
                            //     StringUtil.formatBigDecimal(uomCode, cpocffComMonthlyEntity.getCfcQty()));
                            PoiUtil.setCellValue(reportSheet, startRow, i, cpocffComMonthlyEntity.getCfcQty());
                            break;
                        }
                    }
                    if (StringUtil.isEmpty(PoiUtil.getStringCellValue(reportSheet, startRow, i))) {
                        PoiUtil.setCellStyle(reportSheet, startRow, i,
                            PoiUtil.getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWELVE).getCellStyle());
                        PoiUtil.setCellValue(reportSheet, startRow, i,
                            PoiUtil.getStringCellValue(styleSheet, IntDef.INT_TWO, IntDef.INT_SIXTEEN));
                    }
                }
            
            }
            startRow++;
        }
        
        String sheetName = "";
        sheetName = customerCode
                + "("
                + CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.BUSINESS_PATTERN,
                    businessPattern) + ")";
        
        
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(UOM_STYLE_GREY));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(UOM_STYLE_WHITE));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(deleteSheet));
        wbTemplate.setSheetName(0, sheetName);
        
    }  

    @Override
    protected String getFileId() {
        return FileId.CPOCFF06;
    }

}