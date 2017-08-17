/**
 * CPOCSF11Controller.java
 * 
 * @screen CPOCSF11
 * @author li_feng
 */
package com.chinaplus.web.om.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CategoryLanguage;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.WorkingDay;
import com.chinaplus.common.entity.TnmPartsMaster;
import com.chinaplus.common.entity.TntCfcDay;
import com.chinaplus.common.entity.TntCfcMonth;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.ExcelUtil;
import com.chinaplus.core.util.FileUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.om.entity.CPOCFF11Entity;
import com.chinaplus.web.om.entity.DBTEMPEntity;
import com.chinaplus.web.om.service.CPOCFF11Service;

/**
 * Upload Customer Forecast File(Day&Month) Controller.
 */
@Controller
public class CPOCFF11Controller extends BaseFileController {

    /** business pattern row */
    private static final String BSPTN_ROW = "7";
    /** upload type byDay */
    private static final String UPLOAD_TYPE_BYDAY = "byDay";
    /** upload type byMonth */
    private static final String UPLOAD_TYPE_BYMONTH = "byMonth";
    /** read excel begin column number */
    private static final int READ_START_COL = 2;
    /** detail data begin row number */
    private static final int DETAIL_START_LINE = 10;
    /** uom style sheet */
    private static final String UOM_STYLE_SHEET = "styleSheet";

    /*------------------------------------------------------------------------------------------------*/
    /** max length for db */
    private static final String INTEGER_DIGITS = "10";
    /** decimal digits */
    private static final String DECIMAL_DIGITS = "6";

    /** Customer Stock Upload Screen Service */
    @Autowired
    private CPOCFF11Service service;

    @Override
    protected String getFileId() {
        return FileId.CPOCFF01;
    }

    /**
     * Customer Stock Upload
     * 
     * @param file MultipartFile
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @RequestMapping(value = "/om/CPOCFF11/upload",
        method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam(value = "fileData",
        required = true) MultipartFile file, BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        this.setCommonParam(param, request);
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();

        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {
            if (null == param.getSwapData().get("receivedDate")) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1001_005);
                message.setMessageArgs(new String[] { "CPOCFS02_Label_FcReceivedDate" });
                messageLists.add(message);
            } else if ((DateTimeUtil.parseDate(param.getSwapData().get("receivedDate").toString())).getTime() > service
                .getDBDateTime(param.getOfficeTimezone()).getTime()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1001_007);
                message.setMessageArgs(new String[] { "CPOCFS02_Label_FcReceivedDate" });
                messageLists.add(message);
            }
            if (0 == file.getSize()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1001_005);
                message.setMessageArgs(new String[] { "CPOCFS02_Label_FileName" });
                messageLists.add(message);
            }
            if (param.getSwapData().get("reMark").toString().length() > IntDef.INT_EIGHTY) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1001_006);
                message.setMessageArgs(new String[] { "CPOCFS02_Label_UploadRemark", "80" });
                messageLists.add(message);
            }
            if (messageLists.size() > 0) {
                throw new BusinessException(messageLists);
            }
        }
        String fileName = file.getOriginalFilename();
        param.setSwapData("originalFileName", fileName);
        BaseResult<BaseEntity> baseResult = uploadFileProcess(file, FileType.EXCEL, param, request, response);
        this.setUploadResult(request, response, baseResult);
    }

    /**
     * Process uploaded excel.
     * 
     * @param file the upload file
     * @param workbook uploaded excel
     * @param param the parameters
     * @param <T> the parameter class type
     * @param request the HttpServletRequest
     * @return result message
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook,
        T param, HttpServletRequest request) throws IOException {

        SessionInfoManager context = SessionInfoManager.getContextInstance(request);

        // file data in uploaded file
        List<CPOCFF11Entity> allDatalst = new ArrayList<CPOCFF11Entity>();
        List<List<CPOCFF11Entity>> allCalendarInfoLst = new ArrayList<List<CPOCFF11Entity>>();

        String path = ConfigUtil.get(Properties.TEMPORARY_PATH) + "cfc";
        // path = "E:/common/temp";

        Date officeTime = service.getDBDateTime(param.getOfficeTimezone());
        // get and check data from excel file
        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {

            Map<String, Object> dataMap = new HashMap<String, Object>();
            
            dataMap = doUploadCheck(param, workbook, request, officeTime);

            if (dataMap.get("MessageList") != null) {

                File tempfile = new File(path);
                if (!tempfile.exists()) {
                    tempfile.mkdirs();
                    tempfile = new File(path, param.getSwapData().get("originalFileName").toString());
                } else {
                    tempfile = new File(path, param.getSwapData().get("originalFileName").toString());
                }
                OutputStream outputStream = null;
                outputStream = new FileOutputStream(tempfile);
                workbook.write(outputStream);
                outputStream.close();

                throw new BusinessException((List<BaseMessage>) dataMap.get("MessageList"));
            }

            allDatalst = (List<CPOCFF11Entity>) dataMap.get("FileDate");
            allCalendarInfoLst = (List<List<CPOCFF11Entity>>) dataMap.get("AllCalendarInfo");
            FileUtil.deleteAllFile(new File(path));

        }

        // upload logic
        if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())
                || UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {

            // get file content from session.
            if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())) {

                String mapKey = param.getSessionKey();
                allDatalst = (List<CPOCFF11Entity>) context.get(mapKey);
                allCalendarInfoLst = (List<List<CPOCFF11Entity>>) context.get(mapKey + "_AllCalendarInfo");

                param.setSwapData("originalFileName", context.get(mapKey + "_FileName").toString());
                File newFile = new File(path, context.get(mapKey + "_FileName").toString());
                FileInputStream inputStream = new FileInputStream(newFile);
                workbook = (XSSFWorkbook) ExcelUtil.getWorkBook(inputStream);

                context.remove(mapKey);

            }
            // deleted for requirement change at 20160322
            // write datas
            // writeData(param, workbook, allDatalst, officeTime);
            // upload
            service.doDataUpdate(param, allDatalst, allCalendarInfoLst);
        }

        return new ArrayList<BaseMessage>();

    }

    /**
     * write data & save file
     * 
     * @param param BasepParam
     * @param workbook workbook
     * @param allDatalst List<CPOCFCOMEntity>
     * @param officeTime Office time
     * @throws IOException Exception
     */
    @SuppressWarnings("unused")
    private void writeData(BaseParam param, Workbook workbook, List<CPOCFF11Entity> allDatalst, Date officeTime) throws IOException {

        XSSFWorkbook styleWorkbook = (XSSFWorkbook) ExcelUtil.getWorkBook(getExcelTemplateFilePath("UomStyle"));

        // traversal sheet
        for (int i = 0; i < allDatalst.size(); i++) {

            // get work sheet sheet
            Sheet sheet = workbook.getSheetAt(i);
            CPOCFF11Entity dataEntity = allDatalst.get(i);
            Integer businessPattern = dataEntity.getBusinessPattern();
            Map<String, TnmPartsMaster> partsMap = (Map<String, TnmPartsMaster>) allDatalst.get(i).getPartsMap();

            int sheetMaxRow = sheet.getLastRowNum() + 1;
            for (int startRow = DETAIL_START_LINE; startRow <= sheetMaxRow; startRow++) {

                TnmPartsMaster partsEntity = partsMap.get(String.valueOf(startRow));
                if (partsEntity != null && partsEntity.getInactiveFlag() == null) {

                    if (BusinessPattern.V_V == businessPattern.intValue()) {
                        PoiUtil.setCellValue(sheet, startRow, IntDef.INT_FOUR, partsEntity.getCustPartsNo());
                    } else if (BusinessPattern.AISIN == businessPattern.intValue()) {
                        PoiUtil.setCellValue(sheet, startRow, IntDef.INT_FOUR, partsEntity.getTtcPartsNo());
                    }
                    PoiUtil.setCellValue(
                        sheet,
                        startRow,
                        IntDef.INT_FIVE,
                        CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.PARTS_TYPE,
                            partsEntity.getPartsType()));
                    PoiUtil.setCellValue(sheet, startRow, IntDef.INT_SIX, partsEntity.getCarModel());
                    // UOM
                    int digits = MasterManager.getUomDigits(partsEntity.getUomCode());
                    CellStyle nonInvoiceQtyStyle = super.getDecimalStyle(styleWorkbook, UOM_STYLE_SHEET, digits);
                    CellStyle newStyle = workbook.createCellStyle();
                    newStyle.cloneStyleFrom(nonInvoiceQtyStyle);
                    newStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    newStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    PoiUtil.setCellStyle(sheet, startRow, IntDef.INT_SEVEN, newStyle);
                    PoiUtil.setCellValue(sheet, startRow, IntDef.INT_SEVEN,
                        StringUtil.formatBigDecimal(partsEntity.getUomCode(), partsEntity.getOrderLot()));

                    PoiUtil.setCellValue(sheet, startRow, IntDef.INT_EIGHT, partsEntity.getOldTtcPartsNo());
                    PoiUtil.setCellValue(sheet, startRow, IntDef.INT_NINE, partsEntity.getPartsNameEn());
                    PoiUtil.setCellValue(sheet, startRow, IntDef.INT_TEN, partsEntity.getPartsNameCn());
                }
            }
            String targetFormat = "";
            if (param.getLanguage().equals(1)) {
                targetFormat = DateTimeUtil.FORMAT_DDMMMYYYY;
            } else {
                targetFormat = DateTimeUtil.FORMAT_DATE_YYYYMMDD;
            }
            // set A3~A5 data
            String fromMonth = allDatalst.get(i).getFirstFcMonth();
            String toMonth = allDatalst.get(i).getLastFcMonth();
            String A3 = MessageManager.getMessage("CPOCFF01_Label_ForecastNo", Language.ENGLISH.getLocale()) + ": "
                    + "_" + fromMonth + "-" + toMonth + "_"
                    + DateTimeUtil.formatDate("yyyyMMdd", allDatalst.get(i).getFcDate()) + "_";
            String A4 = MessageManager.getMessage("CPOCFF01_Label_ForecastReceivedDate", Language.ENGLISH.getLocale())
                    + ": " + DateTimeUtil.formatDate(targetFormat, allDatalst.get(i).getFcDate());
            String A5 = MessageManager.getMessage("CPOCFF01_Label_UploadedDate", Language.ENGLISH.getLocale()) + ": "
                    + DateTimeUtil.formatDate(targetFormat, officeTime);

            Font font = workbook.createFont();
            CellStyle cellStyle = workbook.createCellStyle();
            font.setFontName("Arial");
            cellStyle.setFont(font);
            PoiUtil.setCellStyle(sheet, IntDef.INT_THREE, IntDef.INT_ONE, cellStyle);
            PoiUtil.setCellStyle(sheet, IntDef.INT_FOUR, IntDef.INT_ONE, cellStyle);
            PoiUtil.setCellStyle(sheet, IntDef.INT_FIVE, IntDef.INT_ONE, cellStyle);
            PoiUtil.setCellValue(sheet, IntDef.INT_THREE, IntDef.INT_ONE, A3);
            PoiUtil.setCellValue(sheet, IntDef.INT_FOUR, IntDef.INT_ONE, A4);
            PoiUtil.setCellValue(sheet, IntDef.INT_FIVE, IntDef.INT_ONE, A5);
        }

        // save original File
        OutputStream outputStream = null;
        String path = ConfigUtil.get(Properties.UPLOAD_PATH_CFC);
        // path = "E:/common/cfc";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
            file = new File(path, param.getSwapData().get("originalFileName").toString());
        } else {
            file = new File(path, param.getSwapData().get("originalFileName").toString());
        }
        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
        // workbook.close();

        // split file&sheet by customerCode
        @SuppressWarnings("resource")
        XSSFWorkbook newWorkbook = new XSSFWorkbook();

        for (CPOCFF11Entity list : allDatalst) {

            List<String> customerCodelist = new ArrayList<String>();
            customerCodelist = list.getCustomerCodeLst();
            String dataSheetName = list.getSheetName();

            String oldFileName = param.getSwapData().get("originalFileName").toString();
            String newSheetName = "";
            param.setSwapData("FirstFcMonth", list.getFirstFcMonth());
            param.setSwapData("LastFcMonth", list.getLastFcMonth());

            Map<String, String> forecastNoMap = new HashMap<String, String>();
            for (String data : customerCodelist) {

                File newFile = new File(path, oldFileName);
                FileInputStream inputStream = new FileInputStream(newFile);
                newWorkbook = (XSSFWorkbook) ExcelUtil.getWorkBook(inputStream);
                Sheet newSheet = newWorkbook.getSheet(dataSheetName);
                int maxRow = newSheet.getLastRowNum() + 1;
                // remove other sheet
                int sheetNum = newWorkbook.getNumberOfSheets();
                List<String> removeShhetNmLst = new ArrayList<String>();
                for (int i = 0; i < sheetNum; i++) {
                    if (!newWorkbook.getSheetAt(i).getSheetName().equals(dataSheetName)) {
                        removeShhetNmLst.add(newWorkbook.getSheetAt(i).getSheetName());
                    }
                }
                for (int i = 0; i < removeShhetNmLst.size(); i++) {
                    newWorkbook.removeSheetAt(newWorkbook.getSheetIndex(removeShhetNmLst.get(i)));
                }

                int dataLastRow = IntDef.INT_TEN;
                for (int i = DETAIL_START_LINE; i <= maxRow + 1; i++) {
                    if (ValidatorUtils.isExcelEnd(newSheet, i, READ_START_COL, newSheet.getRow(IntDef.INT_EIGHT)
                        .getPhysicalNumberOfCells())) {
                        dataLastRow = i - 1;
                        break;
                    }
                }

                // remove other customerCode
                for (int i = DETAIL_START_LINE; i <= maxRow; i++) {
                    if (!data.equals(PoiUtil.getStringCellValue(newSheet, i, IntDef.INT_THREE))) {
                        // newSheet.shiftRows(i, i, -1);
                        if (newSheet.getRow(i - 1) != null) {
                            newSheet.removeRow(newSheet.getRow(i - 1));
                        }

                    }
                }
                int j = IntDef.INT_TEN;
                for (int i = DETAIL_START_LINE; i <= dataLastRow; i++) {

                    if (StringUtil.isEmpty(PoiUtil.getStringCellValue(newSheet, j, IntDef.INT_THREE))) {
                        newSheet.shiftRows(j, maxRow, -1);
                    } else {
                        j++;
                    }

                }

                param.setSwapData("CustomerCode", data);
                List<DBTEMPEntity> dbTempList = service.getOldReceivedDateData(param);
                int verNo = 1;
                if (dbTempList.size() == 0) {
                    verNo = 1;
                } else {
                    DBTEMPEntity entity = dbTempList.get(0);
                    verNo = Integer
                        .parseInt(entity.getcFCNO().substring(entity.getcFCNO().length() - IntDef.INT_THREE));
                    if (entity.getFcDate().compareTo(list.getFcDate()) != 0) {
                        verNo++;
                    }
                }
                String version = StringUtil.PadLeft(String.valueOf(verNo), IntDef.INT_THREE, "0");

                String forecastNo = PoiUtil.getStringCellValue(newSheet, IntDef.INT_THREE, 1).substring(0,
                    PoiUtil.getStringCellValue(newSheet, IntDef.INT_THREE, 1).indexOf(":") + 1)
                        + data
                        + PoiUtil.getStringCellValue(newSheet, IntDef.INT_THREE, 1).substring(
                            PoiUtil.getStringCellValue(newSheet, IntDef.INT_THREE, 1).indexOf(":") + IntDef.INT_TWO,
                            PoiUtil.getStringCellValue(newSheet, IntDef.INT_THREE, 1).length()) + version;
                PoiUtil.setCellValue(newSheet, IntDef.INT_THREE, 1, forecastNo);
                newSheetName = data + "(" + PoiUtil.getStringCellValue(newSheet, IntDef.INT_SEVEN, IntDef.INT_THREE)
                        + ")";
                newWorkbook.setSheetName(0, newSheetName);

                // save file
                String cfcNO = forecastNo.substring(forecastNo.indexOf(":") + 1, forecastNo.length());
                forecastNoMap.put(data, cfcNO);
                String fileName = cfcNO + ".xlsx";
                file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                    file = new File(path, fileName);
                } else {
                    file = new File(path, fileName);
                }
                outputStream = new FileOutputStream(file);
                newWorkbook.write(outputStream);
                outputStream.close();
            }
            list.setForecastNoMap(forecastNoMap);
        }

    }


    /**
     * do file upload check.
     * 
     * @param param parm
     * @param workbook workbook
     * @param request request
     * @param officeTime Office time
     * @return check result
     */
    private Map<String, Object> doUploadCheck(BaseParam param, Workbook workbook, HttpServletRequest request, Date officeTime) {

        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        List<BaseMessage> confirmMessageLists = new ArrayList<BaseMessage>();

        super.setCommonParam(param, request);

        // file data in uploaded file
        List<CPOCFF11Entity> allDatalst = new ArrayList<CPOCFF11Entity>();
        // all customers in uploaded file
        List<String> checkRepeatCusLst = new ArrayList<String>();
        // all calendar in uploaded file
        List<List<CPOCFF11Entity>> allCalendarInfoLst = new ArrayList<List<CPOCFF11Entity>>();
        List<Map<String, Integer>> allCalendarInfoMapLst = null;

        // do upload file format check
        boolean headerErrorFlg = doFormatCheck(param, confirmMessageLists, allDatalst, workbook, request);
        if (headerErrorFlg) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1014);
            messageLists.add(message);
            dataMap.put("MessageList", messageLists);
            return dataMap;
        }
        // do error check
        doErrorCheck(workbook, param, request, messageLists, allDatalst, checkRepeatCusLst, allCalendarInfoLst, officeTime);

        if (!messageLists.isEmpty()) {

            dataMap.put("MessageList", messageLists);
        } else {

            // confirm check
            doConfirmCheck(param, confirmMessageLists, allDatalst, checkRepeatCusLst, allCalendarInfoLst,
                allCalendarInfoMapLst);
            if (!confirmMessageLists.isEmpty()) {
                dataMap.put("MessageList", confirmMessageLists);
            } else {
                dataMap.put("FileDate", allDatalst);
                dataMap.put("AllCalendarInfo", allCalendarInfoLst);
            }
        }
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        context.put(param.getSessionKey(), allDatalst);
        context.put(param.getSessionKey() + "_FileName", param.getSwapData().get("originalFileName").toString());
        context.put(param.getSessionKey() + "_AllCalendarInfo", allCalendarInfoLst);
        return dataMap;
    }

    /**
     * check the upload excel file format
     * 
     * @param key item key
     * @param itemValue item value
     * @return true:error false:right
     */
    private boolean checkValueByKey(String key, String itemValue) {

        boolean headerErrorFlg = false;
        if (!MessageManager.getMessage(key, Language.ENGLISH.getLocale()).equals(itemValue)
                && !MessageManager.getMessage(key, Language.CHINESE.getLocale()).equals(itemValue)) {
            headerErrorFlg = true;
        }
        return headerErrorFlg;
    }

    /**
     * format check: sequential Date
     * 
     * @param sheet worksheet
     * @param uploadType business pattern
     * @return true:error false:right
     */
    private boolean checkSequentialDate(Sheet sheet, String uploadType) {

        boolean headerErrorFlg = false;
        String previousData = null;
        String nextData = null;
        // max column
        Integer colMaxNumber = sheet.getRow(IntDef.INT_EIGHT).getPhysicalNumberOfCells();

        for (int cowIndex = IntDef.INT_ELEVEN; cowIndex < colMaxNumber.intValue(); cowIndex++) {

            previousData = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, cowIndex);
            nextData = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, cowIndex + 1);

            if (UPLOAD_TYPE_BYDAY.equals(uploadType)) {
                // by day
                if (null != DateTimeUtil.parseDate(previousData) && null != DateTimeUtil.parseDate(nextData)) {

                    // first day
                    if (cowIndex == IntDef.INT_ELEVEN) {
                        // not start with the first day of the month
                        if (DateTimeUtil.getDayDifferent(DateTimeUtil.firstDay(DateTimeUtil.parseDate(previousData)),
                            DateTimeUtil.parseDate(previousData)) > 0) {
                            headerErrorFlg = true;
                            break;
                        }
                    }
                    // last day
                    if (cowIndex == colMaxNumber.intValue() - 1) {
                        // not end with the last day of the month
                        if (DateTimeUtil.getDayDifferent(DateTimeUtil.lastDay(DateTimeUtil.parseDate(nextData)),
                            DateTimeUtil.parseDate(nextData)) > 0) {
                            headerErrorFlg = true;
                            break;
                        }
                    }
                    if (DateTimeUtil.getDayDifferent(DateTimeUtil.parseDate(previousData),
                        DateTimeUtil.parseDate(nextData)) != 1) {
                        headerErrorFlg = true;
                        break;
                    }
                } else {
                    headerErrorFlg = true;
                    break;
                }
            } else {
                // by month
                if (null != DateTimeUtil.parseMonth(previousData) && null != DateTimeUtil.parseMonth(nextData)) {

                    if (DateTimeUtil.getDiffMonths(DateTimeUtil.parseMonth(previousData),
                        DateTimeUtil.parseMonth(nextData)) != 1) {
                        headerErrorFlg = true;
                        break;
                    }
                } else {
                    headerErrorFlg = true;
                    break;
                }
            }
        }
        return headerErrorFlg;
    }

    /**
     * get the upload file data
     * 
     * @param allDatalst data object
     * @param sheet sheet
     * @param uploadType uploadType
     * @param param parm HttpServletRequest
     * @param request HttpServletRequest
     */
    private void getFileDate(List<CPOCFF11Entity> allDatalst, Sheet sheet, String uploadType, BaseParam param,
        HttpServletRequest request) {

        CPOCFF11Entity objEntity = new CPOCFF11Entity();
        Date receivedDate = DateTimeUtil.parseDate((String) param.getSwapData().get("receivedDate"),
            DateTimeUtil.FORMAT_DATE_YYYYMMDD);
        String reMark = (String) param.getSwapData().get("reMark");
        objEntity.setRemark(reMark);
        objEntity.setFcDate(receivedDate);
        objEntity.setUploadType(uploadType);

        // business pattern
        String businessPattern = PoiUtil.getStringCellValue(sheet, IntDef.INT_SEVEN, IntDef.INT_THREE);
        objEntity.setBusinessPatternValue(businessPattern);

        // detail end column
        Integer endCol = sheet.getRow(IntDef.INT_EIGHT).getPhysicalNumberOfCells();
        // detail end row
        int sheetMaxRow = sheet.getLastRowNum() + 1;

        // forecast range
        String startDate = StringUtil.toSafeString(PoiUtil
            .getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_ELEVEN));
        String endDate = StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, endCol));
        if (UPLOAD_TYPE_BYDAY.equals(uploadType)) {

            objEntity.setStartFcDate(DateTimeUtil.parseDate(startDate));
            objEntity.setEndFcDate(DateTimeUtil.parseDate(endDate));

            objEntity.setFirstFcMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD,
                DateTimeUtil.parseDate(startDate)).substring(0, IntDef.INT_SIX));
            objEntity.setLastFcMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD,
                DateTimeUtil.parseDate(endDate)).substring(0, IntDef.INT_SIX));
        } else {

            objEntity.setStartFcDate(DateTimeUtil.firstDay(DateTimeUtil.parseMonth(startDate)));
            objEntity.setEndFcDate(DateTimeUtil.lastDay(DateTimeUtil.parseMonth(endDate)));
            objEntity.setFirstFcMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                DateTimeUtil.parseMonth(startDate)));
            objEntity.setLastFcMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                DateTimeUtil.parseMonth(endDate)));
        }

        // read detail data
        Map<String, TnmPartsMaster> partsMap = new LinkedHashMap<String, TnmPartsMaster>();
        Map<String, List<TntCfcMonth>> cfcMonthMap = new LinkedHashMap<String, List<TntCfcMonth>>();
        Map<String, List<TntCfcDay>> cfcDayMap = new LinkedHashMap<String, List<TntCfcDay>>();
        List<String> customerCodeLst = new ArrayList<String>();

        for (int startRow = DETAIL_START_LINE; startRow <= sheetMaxRow; startRow++) {

            if (!ValidatorUtils.isBlankRow(sheet, startRow, READ_START_COL, endCol)) {

                TnmPartsMaster partsEntity = new TnmPartsMaster();
                partsEntity.setTtcPartsNo(PoiUtil.getStringCellValue(sheet, startRow, IntDef.INT_TWO));
                String customerCode = PoiUtil.getStringCellValue(sheet, startRow, IntDef.INT_THREE);
                partsEntity.setCustomerCode(customerCode);
                if (!StringUtil.isEmpty(customerCode) && !customerCodeLst.contains(customerCode)) {
                    customerCodeLst.add(customerCode);
                }

                List<TntCfcMonth> cfcMonthLst = null;
                List<TntCfcDay> cfcDayLst = null;
                if (UPLOAD_TYPE_BYDAY.equals(uploadType)) {
                    cfcDayLst = new ArrayList<TntCfcDay>();
                } else {
                    cfcMonthLst = new ArrayList<TntCfcMonth>();
                }
                // forecast data
                String qty = null;
                for (int i = IntDef.INT_ELEVEN; i <= endCol; i++) {

                    qty = PoiUtil.getStringCellValue(sheet, startRow, i);
                    if (UPLOAD_TYPE_BYDAY.equals(uploadType)) {
                        // by day
                        TntCfcDay dayEntity = new TntCfcDay();
                        dayEntity.setCfcDate(DateTimeUtil.parseDate(PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE,
                            i)));
                        if (!StringUtil.isEmpty(qty) && null == DecimalUtil.getBigDecimalWithNUll(qty)) {
                            dayEntity.setCfcQty(new BigDecimal(-1));
                        } else {
                            dayEntity.setCfcQty(DecimalUtil.getBigDecimalWithNUll(qty));
                        }
                        cfcDayLst.add(dayEntity);
                    } else {
                        // by month
                        TntCfcMonth monthEntity = new TntCfcMonth();
                        monthEntity.setCfcMonth(PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, i));
                        if (!StringUtil.isEmpty(qty) && null == DecimalUtil.getBigDecimalWithNUll(qty)) {
                            monthEntity.setCfcQty(new BigDecimal(-1));
                        } else {
                            monthEntity.setCfcQty(DecimalUtil.getBigDecimalWithNUll(qty));
                        }
                        cfcMonthLst.add(monthEntity);
                    }
                }

                partsMap.put(StringUtil.toSafeString(startRow), partsEntity);
                cfcMonthMap.put(StringUtil.toSafeString(startRow), cfcMonthLst);
                cfcDayMap.put(StringUtil.toSafeString(startRow), cfcDayLst);

            } else {
                if (ValidatorUtils.isExcelEnd(sheet, startRow, READ_START_COL, endCol)) {
                    break;
                }
            }
        }

        objEntity.setSheetName(sheet.getSheetName());
        objEntity.setPartsMap(partsMap);
        objEntity.setCfcMonthMap(cfcMonthMap);
        objEntity.setCfcDayMap(cfcDayMap);
        objEntity.setCustomerCodeLst(customerCodeLst);
        allDatalst.add(objEntity);

    }

    /**
     * check the effective business pattern.
     * 
     * @param objEntity data entity
     * @param param parm
     * @param messageLists msg list
     * @param lang lang
     */
    private void checkEffectiveBP(CPOCFF11Entity objEntity, BaseParam param, List<BaseMessage> messageLists, String lang) {

        String businessPattern = objEntity.getBusinessPatternValue();
        if (StringUtil.isEmpty(businessPattern)) {
            // (1-1) if C7 is blank, show the error message(w1004_020).

            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
            message.setMessageArgs(new String[] { BSPTN_ROW, objEntity.getSheetName(),
                MessageManager.getMessage("CPOCFF01_Label_BusinessPattern", lang) });
            messageLists.add(message);
        } else {
            // (1-2) if C7 is not an effective business pattern, show the error message(w1004_045).

            Integer busPatternCode = CodeCategoryManager.getCodeValue(CategoryLanguage.ENGLISH,
                CodeMasterCategory.BUSINESS_PATTERN, businessPattern);
            if (null == busPatternCode) {
                busPatternCode = CodeCategoryManager.getCodeValue(CategoryLanguage.CHINESE,
                    CodeMasterCategory.BUSINESS_PATTERN, businessPattern);
            }
            if (null == busPatternCode) {

                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_045);
                message.setMessageArgs(new String[] { BSPTN_ROW, objEntity.getSheetName(),
                    MessageManager.getMessage("CPOCFF01_Label_BusinessPattern", lang) });
                messageLists.add(message);
            }
        }
    }

    /**
     * check the upload forecast range.
     * 
     * @param objEntity data entity
     * @param messageLists msg list
     * @param officeTime Office time
     * @param lang lang
     */
    private void checkFcRange(CPOCFF11Entity objEntity, List<BaseMessage> messageLists, Date officeTime, String lang) {

        if (UPLOAD_TYPE_BYDAY.equals(objEntity.getUploadType())) {

            // (5-1) if the upload forecast month range over 3 months, show the error message(w1004_072).
            if (DateTimeUtil.getDiffMonths(objEntity.getStartFcDate(), objEntity.getEndFcDate()) >= IntDef.INT_THREE) {

                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_072);
                message.setMessageArgs(new String[] { objEntity.getSheetName(), MessageManager.getMessage("CPOCFF01_Label_byDay", lang), "3" });
                messageLists.add(message);
            }
            // (5-5) if the upload forecast month earlier than current month, show the error message(w1004_071).
            if (DateTimeUtil.getDiffMonths(objEntity.getStartFcDate(), officeTime) > 0) {

                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_071);
                message.setMessageArgs(new String[] { objEntity.getSheetName() });
                messageLists.add(message);
            }
        } else {

            // (5-3) if the upload forecast month range over 6 months, show the error message(w1004_072).
            if (DateTimeUtil.getDiffMonths(
                DateTimeUtil.parseDate(objEntity.getFirstFcMonth(), DateTimeUtil.FORMAT_YEAR_MONTH),
                DateTimeUtil.parseDate(objEntity.getLastFcMonth(), DateTimeUtil.FORMAT_YEAR_MONTH)) >= IntDef.INT_SIX) {

                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_072);
                message.setMessageArgs(new String[] { objEntity.getSheetName(), MessageManager.getMessage("CPOCFF01_Label_byMonth", lang), "6" });
                messageLists.add(message);
            }
            // (5-5) if the upload forecast month earlier than current month, show the error message(w1004_071).
            if (DateTimeUtil.getDiffMonths(
                DateTimeUtil.parseDate(objEntity.getFirstFcMonth(), DateTimeUtil.FORMAT_YEAR_MONTH), officeTime) > 0) {

                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_071);
                message.setMessageArgs(new String[] { objEntity.getSheetName() });
                messageLists.add(message);
            }
        }
    }

    /**
     * check the PK of the upload file.
     * 
     * @param busPatternCode business pattern code
     * @param objEntity data entity
     * @param messageLists msg list
     */
    private void checkPK(Integer busPatternCode, CPOCFF11Entity objEntity, List<BaseMessage> messageLists) {

        String msgParm = StringConst.EMPTY;
        if (BusinessPattern.V_V == busPatternCode.intValue()) {
            msgParm = "CPOCFF01_Grid_TTCPN";
        } else if (BusinessPattern.AISIN == busPatternCode.intValue()) {
            msgParm = "CPOCFF01_Grid_CustomerPN";
        }
        // primary key. 2
        Map<String, TnmPartsMaster> partsMap = objEntity.getPartsMap();
        if (null != partsMap && !partsMap.isEmpty()) {

            List<String> repeatKeyLst = new ArrayList<String>();
            for (Map.Entry<String, TnmPartsMaster> partsInfo : partsMap.entrySet()) {

                TnmPartsMaster partsEntity = partsInfo.getValue();
                StringBuffer repeatKey = new StringBuffer();
                // if the date detail row that is not blank, check the column B or column C is not blank,
                // otherwise,
                // show the error message(w1004_020). 2-1
                if (StringUtil.isEmpty(partsEntity.getTtcPartsNo())) {

                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { partsInfo.getKey(), objEntity.getSheetName(), msgParm });
                    messageLists.add(message);

                } else {
                    repeatKey.append(partsEntity.getTtcPartsNo()).append(StringConst.COMMA);
                }

                if (StringUtil.isEmpty(partsEntity.getCustomerCode())) {

                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { partsInfo.getKey(), objEntity.getSheetName(),
                        "CPOCFF01_Grid_TTCCustomerCode" });
                    messageLists.add(message);

                } else if (repeatKey.toString().endsWith(StringConst.COMMA)) {
                    repeatKey.append(partsEntity.getCustomerCode());
                }

                if (!StringUtil.isEmpty(repeatKey.toString())) {

                    if (!repeatKeyLst.contains(repeatKey.toString())) {
                        repeatKeyLst.add(repeatKey.toString());
                    } else {

                        // if the date detail row that is not blank, check the column B and column C is not repeat
                        // with other rows, otherwise,
                        // show the error message(w1004_044). 2-2
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_044);
                        message.setMessageArgs(new String[] { msgParm, partsEntity.getTtcPartsNo(),
                            "CPOCFF01_Grid_TTCCustomerCode", partsEntity.getCustomerCode(), objEntity.getSheetName() });
                        messageLists.add(message);
                    }
                }
            }
        }
    }

    /**
     * adjust upload type by cell of k8.
     * 
     * @param k8 key value
     * @return upload type
     */
    private String adjustUploadedType(String k8) {

        String uploadType = StringConst.EMPTY;
        if (MessageManager.getMessage("CPOCFF01_Label_CusProdDailyFc", Language.ENGLISH.getLocale()).equals(k8)
                || MessageManager.getMessage("CPOCFF01_Label_CusProdDailyFc", Language.CHINESE.getLocale()).equals(k8)) {
            // by day
            uploadType = UPLOAD_TYPE_BYDAY;
        } else if (MessageManager.getMessage("CPOCFF01_Label_CusMonthlyFc", Language.ENGLISH.getLocale()).equals(k8)
                || MessageManager.getMessage("CPOCFF01_Label_CusMonthlyFc", Language.CHINESE.getLocale()).equals(k8)) {
            // by month
            uploadType = UPLOAD_TYPE_BYMONTH;
        }
        return uploadType;
    }

    /**
     * do file format check.
     * 
     * @param sheet sheet
     * @param uploadType upload type
     * @return true:error false:right
     */
    private boolean checkFileFormat(Sheet sheet, String uploadType) {

        Boolean headerErrorFlg = false;

        // A1
        String fileTitle = PoiUtil.getStringCellValue(sheet, IntDef.INT_ONE, IntDef.INT_ONE);
        // K8
        if (StringUtil.isEmpty(uploadType)) {
            headerErrorFlg = true;
        } else if (UPLOAD_TYPE_BYDAY.equals(uploadType)) {
            // by day
            headerErrorFlg = checkValueByKey("CPOCFF01_Label_PageTitle1", fileTitle);
        } else {
            // by month
            headerErrorFlg = checkValueByKey("CPOCFF01_Label_PageTitle2", fileTitle);
        }
        // A7
        String busPatternLabel = PoiUtil.getStringCellValue(sheet, IntDef.INT_SEVEN, IntDef.INT_ONE);
        // A9~J9
        String noLabel = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_ONE);
        String ttcPNLabel4VV = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_TWO);
        String ttcPNLabel4AISIN = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_FOUR);
        String ttcCusCdLabel = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_THREE);
        String cusPNLabel4VV = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_FOUR);
        String cusPNLabel4AISIN = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_TWO);
        String partTypeLabel = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_FIVE);
        String modelLabel = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_SIX);
        String OrderLotLabel = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_SEVEN);
        String oldTTCPNLabel = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_EIGHT);
        String partNmEn = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_NINE);
        String partNmCn = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_TEN);

        // A7:business pattern, A9~J9(except B9&D9):detail title
        headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Label_BusinessPattern", busPatternLabel);
        headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_No", noLabel);
        headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_TTCCustomerCode", ttcCusCdLabel);
        headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_PartType", partTypeLabel);
        headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_Model", modelLabel);
        headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_OrderLot", OrderLotLabel);
        headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_OldTTCPN", oldTTCPNLabel);
        headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_EnglishPartName", partNmEn);
        headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_ChinesePartName", partNmCn);

        String busPatternName = PoiUtil.getStringCellValue(sheet, IntDef.INT_SEVEN, IntDef.INT_THREE);
        if (!StringUtil.isEmpty(busPatternName)) {
            Integer busPatternCode = CodeCategoryManager.getCodeValue(CategoryLanguage.ENGLISH,
                CodeMasterCategory.BUSINESS_PATTERN, busPatternName);
            if (null == busPatternCode) {
                busPatternCode = CodeCategoryManager.getCodeValue(CategoryLanguage.CHINESE,
                    CodeMasterCategory.BUSINESS_PATTERN, busPatternName);
            }
            if (null != busPatternCode) {
                // B9
                if (BusinessPattern.V_V == busPatternCode.intValue()) {
                    headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_TTCPN", ttcPNLabel4VV);
                    headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_CustomerPN", cusPNLabel4VV);
                } else if (BusinessPattern.AISIN == busPatternCode.intValue()) {
                    // D9
                    headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_CustomerPN", cusPNLabel4AISIN);
                    headerErrorFlg = headerErrorFlg || checkValueByKey("CPOCFF01_Grid_TTCPN", ttcPNLabel4AISIN);
                }
            } else {
                // B9
                headerErrorFlg = headerErrorFlg
                        || (checkValueByKey("CPOCFF01_Grid_TTCPN", ttcPNLabel4VV) && checkValueByKey(
                            "CPOCFF01_Grid_CustomerPN", cusPNLabel4AISIN));
                // D9
                headerErrorFlg = headerErrorFlg
                        || (checkValueByKey("CPOCFF01_Grid_CustomerPN", cusPNLabel4VV) && checkValueByKey(
                            "CPOCFF01_Grid_TTCPN", ttcPNLabel4AISIN));
            }
        } else {
            // B9
            headerErrorFlg = headerErrorFlg
                    || (checkValueByKey("CPOCFF01_Grid_TTCPN", ttcPNLabel4VV) && checkValueByKey(
                        "CPOCFF01_Grid_CustomerPN", cusPNLabel4AISIN));
            // D9
            headerErrorFlg = headerErrorFlg
                    || (checkValueByKey("CPOCFF01_Grid_CustomerPN", cusPNLabel4VV) && checkValueByKey(
                        "CPOCFF01_Grid_TTCPN", ttcPNLabel4AISIN));
        }

        // check K9 ~ end column is sequential date(by day) or sequential month(by month)
        headerErrorFlg = headerErrorFlg || checkSequentialDate(sheet, uploadType);

        return headerErrorFlg;
    }

    /**
     * check forecast Qty.
     * 
     * @param objEntity data entity
     * @param messageLists msg list
     */
    private void checkForecastQty(CPOCFF11Entity objEntity, List<BaseMessage> messageLists) {

        if (UPLOAD_TYPE_BYDAY.equals(objEntity.getUploadType())) {
            // by day

            Map<String, List<TntCfcDay>> cfcDayMap = objEntity.getCfcDayMap();
            if (null != cfcDayMap && !cfcDayMap.isEmpty()) {

                for (Map.Entry<String, List<TntCfcDay>> cfcDayInfo : cfcDayMap.entrySet()) {

                    List<TntCfcDay> cfcDayLst = cfcDayInfo.getValue();
                    if (null != cfcDayLst && !cfcDayLst.isEmpty()) {

                        boolean qtyErr = false;
                        boolean lengthErr = false;
                        boolean monthlyQtyErr = false;
                        BigDecimal monthlyQty = null;
                        for (int k = 0; k < cfcDayLst.size(); k++) {

                            TntCfcDay cfcDayEntity = cfcDayLst.get(k);
                            if (!qtyErr && null != cfcDayEntity.getCfcQty()
                                    && cfcDayEntity.getCfcQty().compareTo(new BigDecimal(0)) < 0) {
                                // Qty is not a number or <0

                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
                                message.setMessageArgs(new String[] { cfcDayInfo.getKey(), objEntity.getSheetName(),
                                    "CPOCFF01_Label_CusFcQty", StringConst.ZERO });
                                messageLists.add(message);
                                qtyErr = true;
                            } else if (!monthlyQtyErr && null != cfcDayEntity.getCfcQty()
                                    && (cfcDayEntity.getCfcQty().compareTo(new BigDecimal(0)) >= 0)) {

                                // over max length in DB
                                if (!lengthErr && !ValidatorUtils.checkMaxDecimal(cfcDayEntity.getCfcQty())) {

                                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_043);
                                    message.setMessageArgs(new String[] { cfcDayInfo.getKey(),
                                        objEntity.getSheetName(), "CPOCFF01_Label_CusFcQty", INTEGER_DIGITS,
                                        DECIMAL_DIGITS });
                                    messageLists.add(message);
                                    lengthErr = true;
                                }
                                // sum monthly Qty
                                if (null == monthlyQty) {
                                    monthlyQty = new BigDecimal(0);
                                }
                                monthlyQty = monthlyQty.add(cfcDayEntity.getCfcQty());
                            }
                            // last day of the month
                            if (DateTimeUtil.getDayDifferent(DateTimeUtil.lastDay(cfcDayEntity.getCfcDate()),
                                cfcDayEntity.getCfcDate()) == 0) {

                                if (!monthlyQtyErr && null == monthlyQty) {

                                    // if there is no date in a whole month, show the error message(w1004_002). 5-2
                                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_002);
                                    message
                                        .setMessageArgs(new String[] { cfcDayInfo.getKey(), objEntity.getSheetName() });
                                    messageLists.add(message);
                                    monthlyQtyErr = true;
                                }
                                monthlyQty = null;
                            }
                        }
                    }
                }
            }
        } else {
            // by month

            Map<String, List<TntCfcMonth>> cfcMonthMap = objEntity.getCfcMonthMap();
            if (null != cfcMonthMap && !cfcMonthMap.isEmpty()) {

                for (Map.Entry<String, List<TntCfcMonth>> cfcMonthInfo : cfcMonthMap.entrySet()) {

                    List<TntCfcMonth> cfcMonthLst = cfcMonthInfo.getValue();
                    if (null != cfcMonthLst && !cfcMonthLst.isEmpty()) {

                        boolean qtyErr = false;
                        boolean lengthErr = false;
                        BigDecimal totalMonthQty = null;
                        int fcQtyCnt = 0;
                        for (int k = 0; k < cfcMonthLst.size(); k++) {

                            TntCfcMonth cfcMonthEntity = cfcMonthLst.get(k);
                            if (!qtyErr && null != cfcMonthEntity.getCfcQty()
                                    && cfcMonthEntity.getCfcQty().compareTo(new BigDecimal(0)) < 0) {

                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
                                message.setMessageArgs(new String[] { cfcMonthInfo.getKey(), objEntity.getSheetName(),
                                    "CPOCFF01_Label_CusFcQty", StringConst.ZERO });
                                messageLists.add(message);
                                qtyErr = true;
                            } else if (null != cfcMonthEntity.getCfcQty()
                                    && cfcMonthEntity.getCfcQty().compareTo(new BigDecimal(0)) >= 0) {

                                // over max length in DB
                                if (!lengthErr && !ValidatorUtils.checkMaxDecimal(cfcMonthEntity.getCfcQty())) {

                                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_043);
                                    message.setMessageArgs(new String[] { cfcMonthInfo.getKey(),
                                        objEntity.getSheetName(), "CPOCFF01_Label_CusFcQty", INTEGER_DIGITS,
                                        DECIMAL_DIGITS });
                                    messageLists.add(message);
                                    lengthErr = true;
                                }
                                if (null == totalMonthQty) {
                                    totalMonthQty = new BigDecimal(0);
                                }
                                totalMonthQty = totalMonthQty.add(cfcMonthEntity.getCfcQty());
                            } else if (null == cfcMonthEntity.getCfcQty()) {
                                fcQtyCnt++;
                            }
                        }
                        // no forecast qty in all month.
                        if (fcQtyCnt == cfcMonthLst.size()) {

                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_148);
                            message.setMessageArgs(new String[] { cfcMonthInfo.getKey(), objEntity.getSheetName() });
                            messageLists.add(message);
                        }
                        // if the forecast is not sequential month date, show the error message(w1004_003). 5-4
                        BigDecimal currQty = new BigDecimal(0);
                        for (int k = 0; k < cfcMonthLst.size(); k++) {

                            TntCfcMonth cfcMonthEntity = cfcMonthLst.get(k);
                            if (null != cfcMonthEntity.getCfcQty()
                                    && cfcMonthEntity.getCfcQty().compareTo(new BigDecimal(0)) >= 0) {
                                currQty = currQty.add(cfcMonthEntity.getCfcQty());
                            }
                            // currQty < totalMonthQty
                            if (null != totalMonthQty && null == cfcMonthEntity.getCfcQty()
                                    && currQty.compareTo(totalMonthQty) < 0) {

                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_003);
                                message
                                    .setMessageArgs(new String[] { cfcMonthInfo.getKey(), objEntity.getSheetName() });
                                messageLists.add(message);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * check the pk in upload file and the effective customers.
     * 
     * @param param parm
     * @param request request
     * @param objEntity data entity
     * @param messageLists msg list
     * @param checkRepeatCusLst all sheet of the customers
     * @param bfRepeatCus repeat customers
     * @param allCalendarInfoLst all sheet of the calendar info
     * @param officeTime Office time
     */
    private void checkEffectiveCustomers(BaseParam param, HttpServletRequest request, CPOCFF11Entity objEntity,
        List<BaseMessage> messageLists, List<String> checkRepeatCusLst, StringBuffer bfRepeatCus,
        List<List<CPOCFF11Entity>> allCalendarInfoLst, Date officeTime) {

        // get business pattern
        String busPatternName = objEntity.getBusinessPatternValue();
        Integer busPatternCode = CodeCategoryManager.getCodeValue(CategoryLanguage.ENGLISH,
            CodeMasterCategory.BUSINESS_PATTERN, busPatternName);
        if (null == busPatternCode) {
            busPatternCode = CodeCategoryManager.getCodeValue(CategoryLanguage.CHINESE,
                CodeMasterCategory.BUSINESS_PATTERN, busPatternName);
        }

        if (null != busPatternCode) {

            objEntity.setBusinessPattern(busPatternCode);
            // check primary key. 2)
            checkPK(busPatternCode, objEntity, messageLists);

            // customer belong to login user
            SessionInfoManager context = SessionInfoManager.getContextInstance(request);
            UserManager um = UserManager.getLocalInstance(context);
            List<com.chinaplus.common.bean.BusinessPattern> userCustomer = um.getCurrentBusPattern();

            Map<String, com.chinaplus.common.bean.BusinessPattern> userCusInfo = null;
            if (null != userCustomer && !userCustomer.isEmpty()) {
                userCusInfo = new HashMap<String, com.chinaplus.common.bean.BusinessPattern>();
                for (int m = 0; m < userCustomer.size(); m++) {
                    com.chinaplus.common.bean.BusinessPattern userCus = userCustomer.get(m);
                    userCusInfo.put(userCus.getCustomerCode(), userCus);
                }
            }

            // check object customer and the same business pattern and the same calendar. 4 5-6 5-7 5-8
            List<String> customerCodeLst = objEntity.getCustomerCodeLst();
            if (null != customerCodeLst && !customerCodeLst.isEmpty()) {

                StringBuffer bfCusCdBelognToUser = new StringBuffer();
                StringBuffer bfCusCdWithDifBP = new StringBuffer();
                for (int j = 0; j < customerCodeLst.size(); j++) {

                    if (!checkRepeatCusLst.contains(customerCodeLst.get(j))) {
                        checkRepeatCusLst.add(customerCodeLst.get(j));
                    } else {
                        bfRepeatCus.append(customerCodeLst.get(j)).append(StringConst.COMMA);
                    }
                    if (null != userCusInfo) {
                        // the customer code in upload file is not belong to login user
                        if (null == userCusInfo.get(customerCodeLst.get(j))) {
                            bfCusCdBelognToUser.append(customerCodeLst.get(j)).append(StringConst.COMMA);
                        } else if (busPatternCode.intValue() != userCusInfo.get(customerCodeLst.get(j))
                            .getBusinessPattern().intValue()) {
                            // the same business pattern to the filled value(C7)
                            bfCusCdWithDifBP.append(customerCodeLst.get(j)).append(StringConst.COMMA);
                        }
                    }
                }

                // if the customer code in upload file is not belong to login user, show the error message(w1004_023). 4
                String cusCdBelognToUser = bfCusCdBelognToUser.toString();
                if (cusCdBelognToUser.endsWith(StringConst.COMMA)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_146);
                    message.setMessageArgs(new String[] { objEntity.getSheetName(),
                        cusCdBelognToUser.substring(0, cusCdBelognToUser.length() - 1) });
                    messageLists.add(message);
                }

                // if the upload forecast has more than one customer in a sheet, check whether there are have the
                // same business
                // pattern
                // to the filled value(C7), otherwise,show the error message(w1004_067). 5-6
                String CusCdWithDifBP = bfCusCdWithDifBP.toString();
                if (CusCdWithDifBP.endsWith(StringConst.COMMA)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_067);
                    message.setMessageArgs(new String[] { objEntity.getSheetName(),
                        CusCdWithDifBP.substring(0, CusCdWithDifBP.length() - 1) });
                    messageLists.add(message);
                }

                // check the same customer calendar 5-7 5-8
                param.setSwapData("customerCodeLst", customerCodeLst);
                param.setSwapData("parmStartFcDate", objEntity.getStartFcDate());
                param.setSwapData("parmEndFcDate", objEntity.getEndFcDate());
                Map<String, List<CPOCFF11Entity>> calendarInfoMap = service.getCalendarInfo(param);

                StringBuffer bfCusCdWithoutCal = new StringBuffer();
                StringBuffer bfCusCdWithDiffCal = new StringBuffer();
                Integer compareedCalendarId = null;
                boolean addedFlg = false;
                int i = 0;
                for (Map.Entry<String, List<CPOCFF11Entity>> calendarInfo : calendarInfoMap.entrySet()) {

                    Integer calendarId = calendarInfo.getValue().get(i).getCalendarId();
                    if (null == calendarId
                            || calendarId.intValue() == 0
                            || !calendarInfoMap.get(calendarInfo.getKey()).get(0).getCalendarDate().toString()
                                .equals(param.getSwapData().get("parmStartFcDate").toString())
                            || !calendarInfoMap.get(calendarInfo.getKey())
                                .get(calendarInfoMap.get(calendarInfo.getKey()).size() - 1).getCalendarDate().toString()
                                .equals(param.getSwapData().get("parmEndFcDate").toString())) {
                        // customer calendar not be set
                        bfCusCdWithoutCal.append(calendarInfo.getKey()).append(StringConst.COMMA);
                    } else if (null != compareedCalendarId && compareedCalendarId.intValue() != calendarId.intValue()) {
                        // different calendar
                        bfCusCdWithDiffCal.append(calendarInfo.getKey()).append(StringConst.COMMA);
                    }
                    compareedCalendarId = calendarId;

                    if (!addedFlg) {
                        // one calendar per sheet
                        allCalendarInfoLst.add(calendarInfo.getValue());
                        addedFlg = true;
                    }
                    i++;
                }

                // if the upload forecast has more than one customer in a sheet, check whether all of the customers
                // has been registered calendar in master,
                // otherwise,show the error message(w1004_145). 5-7
                String cusCdWithoutCal = bfCusCdWithoutCal.toString();
                if (cusCdWithoutCal.endsWith(StringConst.COMMA)) {

                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_145);
                    message.setMessageArgs(new String[] { objEntity.getSheetName(),
                        cusCdWithoutCal.substring(0, cusCdWithoutCal.length() - 1) });
                    messageLists.add(message);
                }
                // if the upload forecast has more than one customer in a sheet, check whether there are have the
                // same calendar,
                // otherwise,show the error message(w1004_004). 5-8

                if (objEntity.getUploadType().equals(UPLOAD_TYPE_BYDAY)) {
                    String cusCdWithDiffCal = bfCusCdWithDiffCal.toString();
                    if (cusCdWithDiffCal.endsWith(StringConst.COMMA)) {

                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_004);
                        message.setMessageArgs(new String[] { objEntity.getSheetName() });
                        messageLists.add(message);
                    }
                }
                // check the continuous forecast. 5-9
                Map<String, String> lastFcMonthMap = service.getLastFcMonth(param);
                param.setSwapData("parmLastFcMonth", DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, officeTime));
                Map<String, String> firstFcMonthMap = service.getFirstFcMonth(param);
                for (Map.Entry<String, String> fcMonth : lastFcMonthMap.entrySet()) {

                    Date lastFcMonth = DateTimeUtil.parseDate(fcMonth.getValue(), DateTimeUtil.FORMAT_YEAR_MONTH);
                    // ex. if the last forecast month in DB is May, and it is later than current month(>),
                    // so the uploaded forecast month must contains June, otherwise show the error message(w1004_007).
                    if (DateTimeUtil.getDiffMonths(lastFcMonth, officeTime) <= 0) {
                        if (DateTimeUtil.getDiffMonths(lastFcMonth,
                            DateTimeUtil.parseDate(objEntity.getFirstFcMonth(), DateTimeUtil.FORMAT_YEAR_MONTH)) > 1) {

                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_007);
                            message.setMessageArgs(new String[] {
                                fcMonth.getKey(),
                                DateTimeUtil.getDisOrderMonth(DateTimeUtil.monthAddN(fcMonth.getValue(), 1),
                                    DateTimeUtil.FORMAT_YEAR_MONTH), objEntity.getSheetName() });
                            messageLists.add(message);
                        } else if (DateTimeUtil.getDiffMonths(DateTimeUtil.parseDate(
                            firstFcMonthMap.get(fcMonth.getKey()), DateTimeUtil.FORMAT_YEAR_MONTH), DateTimeUtil
                            .parseDate(objEntity.getLastFcMonth(), DateTimeUtil.FORMAT_YEAR_MONTH)) < -1) {

                            String firstFcMonth = firstFcMonthMap.get(fcMonth.getKey());
                            StringBuffer bfDisDate = new StringBuffer();
                            bfDisDate.append(DateTimeUtil.getDisOrderMonth(firstFcMonth, DateTimeUtil.FORMAT_YEAR_MONTH));
                            if (!firstFcMonth.equals(fcMonth.getValue())) {
                                bfDisDate.append(StringConst.BLANK).append(StringConst.CONNECTOR).append(StringConst.BLANK);
                                bfDisDate.append(DateTimeUtil.getDisOrderMonth(fcMonth.getValue(), DateTimeUtil.FORMAT_YEAR_MONTH));
                            }
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_157);
                            message.setMessageArgs(new String[] { fcMonth.getKey(), bfDisDate.toString(), objEntity.getSheetName() });
                            messageLists.add(message);
                        }
                    }
                }
            }
        }
    }

    /**
     * check last receive date.
     * 
     * @param param parm
     * @param objEntity data entity
     * @param confirmMessageLists confirm msg list
     */
    private void checkFcDate(BaseParam param, CPOCFF11Entity objEntity, List<BaseMessage> confirmMessageLists) {

        param.setSwapData("customerCodeLst", objEntity.getCustomerCodeLst());
        param.setSwapData("parmStartFcMonth", objEntity.getFirstFcMonth());
        param.setSwapData("parmLastFcMonth", objEntity.getLastFcMonth());
        param.setSwapData("parmReceivedDate", objEntity.getFcDate());
        List<CPOCFF11Entity> receivedDateLst = service.getLastReceivedDate(param);
        // selected receive date before last receive date in DB
        for (CPOCFF11Entity receiveDateInfo : receivedDateLst) {

            StringBuffer bfRange = new StringBuffer();
            String firstFcMonth = receiveDateInfo.getFirstFcMonth();
            if (firstFcMonth.compareTo(objEntity.getFirstFcMonth()) < 0) {
                firstFcMonth = objEntity.getFirstFcMonth();
            }
            String endFcMonth = receiveDateInfo.getLastFcMonth();
            if (endFcMonth.compareTo(objEntity.getLastFcMonth()) > 0) {
                endFcMonth = objEntity.getLastFcMonth();
            }
            bfRange.append(DateTimeUtil.getDisOrderMonth(firstFcMonth, DateTimeUtil.FORMAT_YEAR_MONTH));
            if (!endFcMonth.equals(firstFcMonth)) {
                bfRange.append(StringConst.BLANK).append(StringConst.CONNECTOR).append(StringConst.BLANK);
                bfRange.append(DateTimeUtil.getDisOrderMonth(endFcMonth, DateTimeUtil.FORMAT_YEAR_MONTH));
            }
            BaseMessage message = new BaseMessage(MessageCodeConst.C1003);
            message.setMessageArgs(new String[] { receiveDateInfo.getCustomerCode(), bfRange.toString(),
                DateTimeUtil.getDisDate(receiveDateInfo.getFcDate()) });
            confirmMessageLists.add(message);
        }
    }

    /**
     * do confirm check.
     * 
     * @param param parm
     * @param confirmMessageLists confirm msg list
     * @param allDatalst file data
     * @param checkRepeatCusLst all customers in file
     * @param allCalendarInfoLst all calendar in file
     * @param allCalendarInfoMapLst all calendar in file
     */
    private void doConfirmCheck(BaseParam param, List<BaseMessage> confirmMessageLists,
        List<CPOCFF11Entity> allDatalst, List<String> checkRepeatCusLst, List<List<CPOCFF11Entity>> allCalendarInfoLst,
        List<Map<String, Integer>> allCalendarInfoMapLst) {

        // change calendar list to map, prepare for non-working day check.
        allCalendarInfoMapLst = new ArrayList<Map<String, Integer>>();
        for (List<CPOCFF11Entity> calendarInfoLst : allCalendarInfoLst) {

            Map<String, Integer> calendarInfoMap = null;
            if (null != calendarInfoLst && !calendarInfoLst.isEmpty()) {

                calendarInfoMap = new HashMap<String, Integer>();
                for (CPOCFF11Entity calendarInfo : calendarInfoLst) {

                    calendarInfoMap.put(
                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, calendarInfo.getCalendarDate()),
                        calendarInfo.getWorkingFlag());
                }
            }
            allCalendarInfoMapLst.add(calendarInfoMap);
        }

        // select the parts info in master.
        param.setSwapData("allCustomerCodeLst", checkRepeatCusLst);
        Map<String, TnmPartsMaster> masterPsrtsMap = service.getRegisteredParts(param);

        StringBuffer bfErrParts = new StringBuffer();
        StringBuffer bfErrCalendar = new StringBuffer();
        for (int i = 0; i < allDatalst.size(); i++) {

            CPOCFF11Entity objEntity = allDatalst.get(i);
            if (null != objEntity.getCustomerCodeLst() && !objEntity.getCustomerCodeLst().isEmpty()) {

                // file data
                Map<String, TnmPartsMaster> partsMap = objEntity.getPartsMap();

                for (Map.Entry<String, TnmPartsMaster> partMap : partsMap.entrySet()) {

                    TnmPartsMaster partInfo = partMap.getValue();
                    StringBuffer bfKey = new StringBuffer();
                    bfKey.append(partInfo.getCustomerCode()).append(StringConst.COMMA).append(partInfo.getTtcPartsNo());

                    // not exist in master
                    if (null == masterPsrtsMap.get(bfKey.toString())) {

                        bfErrParts.append(partInfo.getTtcPartsNo()).append(StringConst.COMMA);
                        // data effective flg
                        partInfo.setInactiveFlag(1);
                    } else {

                        // copy parts info from master to file data.
                        TnmPartsMaster msterPartInfo = masterPsrtsMap.get(bfKey.toString());

                        partInfo.setTtcPartsNo(msterPartInfo.getTtcPartsNo());
                        partInfo.setBusinessPattern(msterPartInfo.getBusinessPattern());
                        partInfo.setCustPartsNo(msterPartInfo.getCustPartsNo());
                        partInfo.setPartsType(msterPartInfo.getPartsType());
                        partInfo.setCarModel(msterPartInfo.getCarModel());
                        partInfo.setOrderLot(msterPartInfo.getOrderLot());
                        partInfo.setOldTtcPartsNo(msterPartInfo.getOldTtcPartsNo());
                        partInfo.setPartsNameEn(msterPartInfo.getPartsNameEn());
                        partInfo.setPartsNameCn(msterPartInfo.getPartsNameCn());
                        partInfo.setCustomerId(msterPartInfo.getCustomerId());
                        partInfo.setPartsId(msterPartInfo.getPartsId());
                        partInfo.setOrderLot(msterPartInfo.getOrderLot());
                        partInfo.setSpq(msterPartInfo.getSpq());
                        partInfo.setUomCode(msterPartInfo.getUomCode());
                    }
                }
                // not registered parts
                if (bfErrParts.toString().endsWith(StringConst.COMMA)) {

                    bfErrParts.delete(bfErrParts.length() - 1, bfErrParts.length());
                    bfErrParts.append(StringConst.LEFT_BRACKET).append("Sheet: ").append(objEntity.getSheetName())
                        .append(StringConst.RIGHT_BRACKET).append(StringConst.SEMICOLON);
                }

                // get last forecast received date, show the confirm message(c1003).
                checkFcDate(param, objEntity, confirmMessageLists);

                // Check non-working day (for by day)
                if (UPLOAD_TYPE_BYDAY.equals(objEntity.getUploadType())) {

                    List<String> nonworkingDayLst = new ArrayList<String>();
                    Map<String, List<TntCfcDay>> cfcDaysMap = objEntity.getCfcDayMap();
                    for (Map.Entry<String, List<TntCfcDay>> cfcDayMap : cfcDaysMap.entrySet()) {

                        List<TntCfcDay> cfcDayByRowLst = cfcDayMap.getValue();
                        for (TntCfcDay cfcDayByRow : cfcDayByRowLst) {

                            // qty > 0
                            if (null != cfcDayByRow.getCfcQty()
                                    && cfcDayByRow.getCfcQty().compareTo(new BigDecimal(0)) > 0) {

                                Map<String, Integer> calendarInfoMap = allCalendarInfoMapLst.get(i);
                                String cfcDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD,
                                    cfcDayByRow.getCfcDate());
                                // non-working day
                                if (WorkingDay.REST_DAY.intValue() == calendarInfoMap.get(cfcDate).intValue()) {

                                    String disCfcDate = DateTimeUtil.getDisDate(cfcDate, DateTimeUtil.FORMAT_YYYYMMDD);
                                    // except repeat days
                                    if (!nonworkingDayLst.contains(disCfcDate)) {
                                        nonworkingDayLst.add(disCfcDate);
                                    }
                                }
                            }
                        }
                    }
                    for (int j = 0; j < nonworkingDayLst.size(); j++) {

                        bfErrCalendar.append(nonworkingDayLst.get(j));
                        if (j < nonworkingDayLst.size() - 1) {
                            bfErrCalendar.append(StringConst.COMMA);
                        } else {
                            bfErrCalendar.append(StringConst.LEFT_BRACKET).append("Sheet: ")
                                .append(objEntity.getSheetName()).append(StringConst.RIGHT_BRACKET)
                                .append(StringConst.SEMICOLON);
                        }
                    }
                }
            }
        }
        // if the uploaded parts have not been registered in the master, show the confirm message(c1002).
        String errParts = bfErrParts.toString();
        if (errParts.endsWith(StringConst.SEMICOLON)) {

            BaseMessage message = new BaseMessage(MessageCodeConst.C1002);
            message.setMessageArgs(new String[] { errParts.substring(0, errParts.length() - 1) });
            confirmMessageLists.add(message);
        }

        // if there is the forecast data on the non-working day, show the confirm message(c1004).
        String errCalendar = bfErrCalendar.toString();
        if (errCalendar.endsWith(StringConst.SEMICOLON)) {

            BaseMessage message = new BaseMessage(MessageCodeConst.C1004);
            message.setMessageArgs(new String[] { errCalendar.substring(0, errCalendar.length() - 1) });
            confirmMessageLists.add(message);
        }
    }

    /**
     * do upload file format check.
     * 
     * @param param parm
     * @param messageLists msg list
     * @param allDatalst file data
     * @param workbook workbook
     * @param request HttpServletRequest
     * @return true:error;false:right
     */
    private boolean doFormatCheck(BaseParam param, List<BaseMessage> messageLists, List<CPOCFF11Entity> allDatalst,
        Workbook workbook, HttpServletRequest request) {

        Integer sheetNumber = workbook.getNumberOfSheets();
        boolean headerErrorFlg = false;
        for (int sheetIndex = 0; sheetIndex < sheetNumber; sheetIndex++) {

            // get work sheet sheet
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            // adjust upload type by k8
            String k8 = PoiUtil.getStringCellValue(sheet, IntDef.INT_EIGHT, IntDef.INT_ELEVEN);
            String uploadType = adjustUploadedType(k8);

            // 1-1. Error Check
            // (1)Check the upload file header column whether it is correct.
            headerErrorFlg = checkFileFormat(sheet, uploadType);

            if (headerErrorFlg) {
                break;
            }

            // save excel data
            getFileDate(allDatalst, sheet, uploadType, param, request);
        }

        return headerErrorFlg;
    }
    
    /**
     * do upload file format check.
     * 
     * @param param BaseParam
     * @param request request
     * @param objEntity data entity
     * @param messageLists List<BaseMessage>
     * @param checkRepeatCusLst all sheet of the customers
     * 
     */
    private void checkInActiveFlg(BaseParam param, HttpServletRequest request, CPOCFF11Entity objEntity,
        List<BaseMessage> messageLists, List<String> checkRepeatCusLst) {
        
        Map<String, TnmPartsMaster> partsMap = objEntity.getPartsMap();
        
        // select the parts info in master.
        param.setSwapData("allCustomerCodeLst", checkRepeatCusLst);
        Map<String, TnmPartsMaster> masterPsrtsMap = service.getRegisteredParts(param);
        // select the parts info in cfcMonth.
        Map<String, TnmPartsMaster> cfcMonthMap = service.getCfcMonth(param);
        StringBuffer errorKey = new StringBuffer();
        
        if (null != partsMap && !partsMap.isEmpty()) {
            for (Map.Entry<String, TnmPartsMaster> partsInfo : partsMap.entrySet()) {

                TnmPartsMaster partsEntity = partsInfo.getValue();
                StringBuffer bfKey = new StringBuffer();
                bfKey.append(partsEntity.getCustomerCode()).append(StringConst.COMMA);
                bfKey.append(partsEntity.getTtcPartsNo()).toString();

                if (masterPsrtsMap.get(bfKey) != null && masterPsrtsMap.get(bfKey).getInactiveFlag() == 1) {
                    if(cfcMonthMap.get(bfKey) == null ){
                        errorKey.append(bfKey.substring(bfKey.indexOf(",") + 1, bfKey.length())).append(StringConst.COMMA);
                    }
                }
                
            }
        }
        
        if(errorKey.length() > 0){
            String msg = errorKey.toString().substring(0, errorKey.length()-1);
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_180);
            message.setMessageArgs(new String[] { msg });
            messageLists.add(message);
        }
    }

    /**
     * do upload file error check.
     * 
     * @param workbook Workbook
     * @param param parm
     * @param request request
     * @param messageLists msg list
     * @param allDatalst file data
     * @param checkRepeatCusLst all customers in uploaded file
     * @param allCalendarInfoLst all calendar in uploaded file
     * @param officeTime Office time
     */
    private void doErrorCheck(Workbook workbook, BaseParam param, HttpServletRequest request,
        List<BaseMessage> messageLists, List<CPOCFF11Entity> allDatalst, List<String> checkRepeatCusLst,
        List<List<CPOCFF11Entity>> allCalendarInfoLst, Date officeTime) {

        // detail data count
        int emptyCnt = 0;

        StringBuffer bfRepeatCus = new StringBuffer();

        List<Integer> emptySheetLst = new ArrayList<Integer>();
        for (int i = 0; i < allDatalst.size(); i++) {

            CPOCFF11Entity objEntity = allDatalst.get(i);

            boolean isEmpty = false;
            // Check if upload file have data. (2)
            if (null == objEntity.getPartsMap() || objEntity.getPartsMap().isEmpty()) {
                emptyCnt++;
                isEmpty = true;
                emptySheetLst.add(i);
            }
            if (!isEmpty) {
                
                String lang = MessageManager.getLanguage(param.getLanguage()).toString();
                // check the effective business pattern. 1-1, 1-2
                checkEffectiveBP(objEntity, param, messageLists, lang);

                // check the upload forecast range. 5-1, 5-3, 5-5
                checkFcRange(objEntity, messageLists, officeTime, lang);

                // Qty format check. 3), 5-2, 5-4
                checkForecastQty(objEntity, messageLists);

                // check pk and EffectiveCustomers. 2) 4 5-6 5-7 5-8 5-9
                checkEffectiveCustomers(param, request, objEntity, messageLists, checkRepeatCusLst, bfRepeatCus,
                    allCalendarInfoLst, officeTime);
                
                // check inactiveFlg partsId 
                checkInActiveFlg(param, request, objEntity, messageLists, checkRepeatCusLst);
                
            }

        }

        // if no detail data in all sheets of the uploaded file, show the error message(w1004_005). (2)
        if (emptyCnt == allDatalst.size()) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
            messageLists.add(message);
        }

        String repeatCus = bfRepeatCus.toString();
        // if there is the same customer code in different sheet, show the error message(w1004_147).
        if (repeatCus.endsWith(StringConst.COMMA)) {

            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_147);
            message.setMessageArgs(new String[] { repeatCus.substring(0, repeatCus.length() - 1) });
            messageLists.add(message);
        }

        // delete empty sheet
        if (!emptySheetLst.isEmpty()) {
            for (int i = emptySheetLst.size() - 1; i >= 0; i--) {
                allDatalst.remove(emptySheetLst.get(i).intValue());
                workbook.removeSheetAt(emptySheetLst.get(i).intValue());
            }
        }

    }

}