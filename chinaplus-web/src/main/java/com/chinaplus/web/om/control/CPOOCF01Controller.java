package com.chinaplus.web.om.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.ActiveFlag;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.consts.CodeConst.PartsStatus;
import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.PartSortManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOOCF01CompareByDayEntity;
import com.chinaplus.web.om.entity.CPOOCF01CurrentMonthEntity;
import com.chinaplus.web.om.entity.CPOOCF01DateEntity;
import com.chinaplus.web.om.entity.CPOOCF01Entity;
import com.chinaplus.web.om.service.CPOOCF01Service;

/**
 * Order Calculation Supporting Data Summary Download
 */
@Controller
public class CPOOCF01Controller extends BaseFileController {

    /** DOWNLOAD_NAME */
    private static final String DOWNLOAD_NAME = "OrderCalculationSupportingData_{0}.xlsx";

    /** SHEET_STYLE */
    private static final String SHEET_STYLE = "style";

    /** DECIMAL */
    private static final String DECIMAL = "decimal";

    /** Kanban Issued Plan Date Master Download service */
    @Autowired
    private CPOOCF01Service service;

    @Override
    protected String getFileId() {
        return FileId.CPOOCF01;
    }

    /**
     * Order Calculation Supporting Data Summary Downloadcheck
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     */
    @RequestMapping(value = "/om/CPOOCF01/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        this.setCommonParam(param, request);
        String downloadProcess = param.getUploadProcess();
        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(downloadProcess)) {
            List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
            String csDateString = (String) param.getSwapData().get("csDate");
            Date csDate = DateTimeUtil.parseDate(csDateString);
            String customerId = (String) param.getSwapData().get("customerId");
            String balanceDateString = (String) param.getSwapData().get("balanceDate");
            Date balanceDate = DateTimeUtil.parseDate(balanceDateString);
            String monthRangeTo = (String) param.getSwapData().get("monthRangeTo");
            Date monthRangeToDate = DateTimeUtil.parseDate(monthRangeTo);
            String monthRangeFrom = (String) param.getSwapData().get("monthRangeFrom");
            Date monthRangeFromDate = DateTimeUtil.parseDate(monthRangeFrom);
            boolean current = (boolean) param.getSwapData().get("current");
            if (monthRangeFromDate != null && monthRangeToDate != null) {
                if (monthRangeToDate.getTime() < monthRangeFromDate.getTime()) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                    message.setMessageArgs(new String[] { "CPOOCS01_Label_RangeofCusFcStart",
                        "CPOOCS01_Label_RangeofCusFcEnd" });
                    messageLists.add(message);
                }
            }
            Date systemDateTime = service.getDBDateTimeByDefaultTimezone();
            TnmCustomer customer = service.getOneById(TnmCustomer.class, StringUtil.toSafeInteger(customerId));
            if (csDate.getTime() > systemDateTime.getTime()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_005);
                message.setMessageArgs(new String[] { "CPOOCS01_Label_ClosingStockDate" });
                messageLists.add(message);
            }
            if (balanceDate.getTime() > systemDateTime.getTime()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_005);
                message.setMessageArgs(new String[] { "CPOOCS01_Label_ClosingOrderBalanceDate" });
                messageLists.add(message);
            }
            param.getSwapData().put("officeId", param.getCurrentOfficeId());
            param.getSwapData().put("customerId", customerId);
            CPOOCF01Entity range = service.checkRange(param);
            if (range != null && range.getCheckMaxDate() != null) {
                if (range.getCheckMaxDate().compareTo(
                    DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, monthRangeToDate)) < 0) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_006);
                    message.setMessageArgs(new String[] {
                        customer.getCustomerCode(),
                        MessageManager.getMessage(
                            DateTimeUtil.getDisOrderMonth(range.getCheckMaxDate(), DateTimeUtil.FORMAT_YEAR_MONTH),
                            MessageManager.getLanguage(param.getLanguage()).getName()) });
                    messageLists.add(message);
                }
            }
            if (current && messageLists.size() == 0) {
                Date now = service.getDBDateTimeByDefaultTimezone();
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.MONTH, -1);
                Date firstDate = DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                    DateTimeUtil.firstDay(cal.getTime())));
                Date lastDate = DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                    DateTimeUtil.lastDay(cal.getTime())));
                if (firstDate.getTime() > balanceDate.getTime()) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.C1013);
                    messageLists.add(message);
                }
                if (lastDate.getTime() < balanceDate.getTime()) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.C1012);
                    messageLists.add(message);
                }
            }
            if (messageLists.size() != 0) {
                throw new BusinessException(messageLists);
            }
        }

        return new BaseResult<BaseEntity>();
    }

    /**
     * Order Calculation Supporting Data Summary
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/om/CPOOCF01/download",
        method = RequestMethod.POST)
    public void download(HttpServletRequest request, HttpServletResponse response) {
        // set common parameters by session
        BaseParam param = this.convertJsonDataForForm(BaseParam.class);
        this.setCommonParam(param, request);
        // checkbox or filters
        String fileName = StringUtil.formatMessage(DOWNLOAD_NAME, param.getClientTime());

        this.downloadExcelWithTemplate(fileName, param, request, response);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

        String csDateString = (String) param.getSwapData().get("csDate");
        Date csDate = DateTimeUtil.parseDate(csDateString);
        String customerId = (String) param.getSwapData().get("customerId");
        String balanceDateString = (String) param.getSwapData().get("balanceDate");
        Date balanceDate = DateTimeUtil.parseDate(balanceDateString);
        String monthRangeFrom = (String) param.getSwapData().get("monthRangeFrom");
        Date monthRangeFromDate = DateTimeUtil.parseDate(monthRangeFrom);
        String monthRangeTo = (String) param.getSwapData().get("monthRangeTo");
        Date monthRangeToDate = DateTimeUtil.parseDate(monthRangeTo);
        boolean current = (boolean) param.getSwapData().get("current");
        List<Integer> cfcIdList = (List<Integer>) param.getSwapData().get("cfcIdList");
        param.setSwapData("cfcIdList", cfcIdList);
        Sheet vvSheetName = wbTemplate.getSheetAt(0);
        Sheet styleSheet = wbTemplate.getSheetAt(1);
        TnmCustomer customer = service.getOneById(TnmCustomer.class, StringUtil.toSafeInteger(customerId));
        wbTemplate.setSheetName(0, customer.getCustomerCode());
        param.getSwapData().put("csDate", csDate);
        param.getSwapData().put("officeId", param.getCurrentOfficeId());
        param.getSwapData().put("customerId", customerId);
        param.getSwapData().put("partsStatus", PartsStatus.COMPLETED);
        param.getSwapData().put("inActiveFlag", InactiveFlag.ACTIVE);
        List<CPOOCF01Entity> list = service.getMainList(param);
        if (list == null || list.size() == 0) {
            throw new BusinessException(new BaseMessage(MessageCodeConst.W1005_001));
        }
        PartSortManager.sort(list, "ttcPartNo", "oldTtcPartNo");
        param.getSwapData().put("customerId", customerId);
        param.getSwapData().put("balanceDate", balanceDate);
        List<CPOOCF01CompareByDayEntity> balances = service.getBalance(param);
        int violetLength = 0;
        // yellow
        Map<String, List<CPOOCF01CompareByDayEntity>> yellowList = new LinkedHashMap<String, List<CPOOCF01CompareByDayEntity>>();
        CellStyle style = styleSheet.getRow(0).getCell(0).getCellStyle();

        PoiUtil.setCellStyle(vvSheetName, IntDef.INT_SEVEN, IntDef.INT_TEN, style);
        PoiUtil.setCellValue(vvSheetName, IntDef.INT_SEVEN, IntDef.INT_TEN, MessageManager.getMessage(
            "CPOOCF01_Grid_OBTotal", MessageManager.getLanguage(param.getLanguage()).getName()));
        if (balances.size() > 0) {
            String orderName = null;
            for (int i = 0; i < balances.size(); i++) {
                if (customer.getBusinessPattern() == BusinessPattern.V_V) {
                    orderName = balances.get(i).getImpPoNo();
                } else {
                    orderName = balances.get(i).getKanbanPlanNo();
                }
                if (!yellowList.containsKey(orderName)) {
                    PoiUtil.setCellStyle(vvSheetName, IntDef.INT_SEVEN, yellowList.size() + IntDef.INT_ELEVEN, style);
                    PoiUtil.setCellValue(vvSheetName, IntDef.INT_SEVEN, yellowList.size() + IntDef.INT_ELEVEN,
                        orderName);
                    List<CPOOCF01CompareByDayEntity> dayList = new ArrayList<CPOOCF01CompareByDayEntity>();
                    dayList.add(balances.get(i));
                    yellowList.put(orderName, dayList);
                } else {
                    yellowList.get(orderName).add(balances.get(i));
                }
            }
        }
        for (int j = IntDef.INT_FOUR; j <= IntDef.INT_SIX; j++) {
            for (int i = IntDef.INT_TEN; i <= yellowList.size() + IntDef.INT_TEN; i++) {
                PoiUtil.setCellStyle(vvSheetName, j, i, style);
            }
        }
        vvSheetName.addMergedRegion(new CellRangeAddress(IntDef.INT_THREE, IntDef.INT_FIVE, IntDef.INT_NINE, yellowList
            .size() + IntDef.INT_NINE));
        String stringDateBalance = null;
        if (param.getLanguage() == IntDef.INT_TWO) {
            int month = DateTimeUtil.getMonth(balanceDate);
            int day = DateTimeUtil.getDayOfMonth(balanceDate);
            stringDateBalance = String.valueOf(month) + "月" + String.valueOf(day) + "日";
        } else {
            stringDateBalance = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMM, balanceDate);
        }
        String message = MessageManager.getMessage("CPOOCF01_Grid_OrderBalance",
            MessageManager.getLanguage(param.getLanguage()).getName());

        PoiUtil.setCellValue(vvSheetName, IntDef.INT_FOUR, IntDef.INT_TEN, stringDateBalance + StringConst.BLANK
                + message);
        violetLength += yellowList.size() + 1;
        // yellow end
        Map<String, List<CPOOCF01CurrentMonthEntity>> violetString = new LinkedHashMap<String, List<CPOOCF01CurrentMonthEntity>>();
        if (current) {
            // violet
            Date now = service.getDBDateTimeByDefaultTimezone();
            int month = DateTimeUtil.getMonth(now);
            List<CPOOCF01CurrentMonthEntity> currentQtys = new ArrayList<CPOOCF01CurrentMonthEntity>();
            if (customer.getBusinessPattern() == BusinessPattern.V_V) {
                Date firstDate = DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY,
                    DateTimeUtil.firstDay(now)));
                Date lastDate = DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY,
                    DateTimeUtil.lastDay(now)));

                param.getSwapData().put("officeId", param.getCurrentOfficeId());
                param.getSwapData().put("customerId", customerId);
                param.getSwapData().put("firstDate", firstDate);
                param.getSwapData().put("lastDate", lastDate);
                param.getSwapData().put("status", CodeConst.ActiveFlag.Y);
                currentQtys = service.getCurrentQty(param);
            } else {
                param.getSwapData().put("officeId", param.getCurrentOfficeId());
                param.getSwapData().put("customerId", customerId);
                param.getSwapData().put("status", CodeConst.ActiveFlag.Y);
                param.getSwapData().put("orderMonth", DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, now));
                currentQtys = service.getCurrentQtyAisin(param);
            }
            CellStyle styleViolet = styleSheet.getRow(0).getCell(1).getCellStyle();

            PoiUtil.setCellStyle(vvSheetName, IntDef.INT_SEVEN, IntDef.INT_TEN + violetLength, styleViolet);
            PoiUtil.setCellValue(vvSheetName, IntDef.INT_SEVEN, IntDef.INT_TEN + violetLength, MessageManager
                .getMessage("CPOOCF01_Grid_CMTotal", MessageManager.getLanguage(param.getLanguage()).getName()));

            if (currentQtys.size() > 0) {
                for (int i = 0; i < currentQtys.size(); i++) {
                    if (!violetString.containsKey(currentQtys.get(i).getImpPoNo())) {
                        PoiUtil.setCellStyle(vvSheetName, IntDef.INT_SEVEN, violetString.size() + violetLength
                                + IntDef.INT_ELEVEN, styleViolet);
                        PoiUtil.setCellValue(vvSheetName, IntDef.INT_SEVEN, violetString.size() + violetLength
                                + IntDef.INT_ELEVEN, currentQtys.get(i).getImpPoNo());
                        List<CPOOCF01CurrentMonthEntity> monthList = new ArrayList<CPOOCF01CurrentMonthEntity>();
                        monthList.add(currentQtys.get(i));
                        violetString.put(currentQtys.get(i).getImpPoNo(), monthList);
                    } else {
                        violetString.get(currentQtys.get(i).getImpPoNo()).add(currentQtys.get(i));
                    }
                }
            }

            for (int j = IntDef.INT_FOUR; j <= IntDef.INT_SIX; j++) {
                for (int i = IntDef.INT_TEN + violetLength; i <= violetLength + IntDef.INT_TEN + violetString.size(); i++) {
                    PoiUtil.setCellStyle(vvSheetName, j, i, styleViolet);
                }
            }
            vvSheetName.addMergedRegion(new CellRangeAddress(IntDef.INT_THREE, IntDef.INT_FIVE, IntDef.INT_NINE
                    + violetLength, violetLength + IntDef.INT_NINE + violetString.size()));

            String stringDateViolet = null;
            if (param.getLanguage() == IntDef.INT_TWO) {
                stringDateViolet = String.valueOf(month);
            } else {
                stringDateViolet = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MMM, now);
            }
            String messageViolet = MessageManager.getMessage("CPOOCF01_Grid_MonthOrder",
                MessageManager.getLanguage(param.getLanguage()).getName());

            PoiUtil.setCellValue(vvSheetName, IntDef.INT_FOUR, IntDef.INT_TEN + violetLength, stringDateViolet
                    + StringConst.BLANK + messageViolet);

            violetLength += violetString.size() + 1;
        }
        // light yellow
        CellStyle styleLyellow = styleSheet.getRow(0).getCell(IntDef.INT_TWO).getCellStyle();
        for (int j = IntDef.INT_FOUR; j <= IntDef.INT_SIX; j++) {
            for (int i = IntDef.INT_TEN + violetLength; i <= violetLength + IntDef.INT_TEN + IntDef.INT_TWO; i++) {
                PoiUtil.setCellStyle(vvSheetName, j, i, styleLyellow);
            }
        }
        vvSheetName.addMergedRegion(new CellRangeAddress(IntDef.INT_THREE, IntDef.INT_FIVE, IntDef.INT_NINE
                + violetLength, violetLength + IntDef.INT_NINE + IntDef.INT_ONE));

        PoiUtil.setCellValue(vvSheetName, IntDef.INT_FOUR, IntDef.INT_TEN + violetLength, MessageManager.getMessage(
            "CPOOCF01_Grid_NearestCusStock", MessageManager.getLanguage(param.getLanguage()).getName()));

        PoiUtil.setCellStyle(vvSheetName, IntDef.INT_SEVEN, IntDef.INT_TEN + violetLength, styleLyellow);
        PoiUtil.setCellValue(vvSheetName, IntDef.INT_SEVEN, IntDef.INT_TEN + violetLength, MessageManager.getMessage(
            "CPOOCF01_Grid_StockQty", MessageManager.getLanguage(param.getLanguage()).getName()));

        PoiUtil.setCellStyle(vvSheetName, IntDef.INT_SEVEN, IntDef.INT_ELEVEN + violetLength, styleLyellow);
        PoiUtil.setCellValue(
            vvSheetName,
            IntDef.INT_SEVEN,
            IntDef.INT_ELEVEN + violetLength,
            MessageManager.getMessage("CPOOCF01_Grid_NearestClosingStDate",
                MessageManager.getLanguage(param.getLanguage()).getName()));
        violetLength += IntDef.INT_TWO;
        // light yellow one
        for (int j = IntDef.INT_FOUR; j <= IntDef.INT_SIX; j++) {
            PoiUtil.setCellStyle(vvSheetName, j, IntDef.INT_TEN + violetLength, styleLyellow);
        }
        vvSheetName.addMergedRegion(new CellRangeAddress(IntDef.INT_THREE, IntDef.INT_FIVE, IntDef.INT_NINE
                + violetLength, violetLength + IntDef.INT_NINE));
        PoiUtil.setCellValue(vvSheetName, IntDef.INT_FOUR, IntDef.INT_TEN + violetLength, MessageManager.getMessage(
            "CPOOCF01_Grid_TtcImpStock", MessageManager.getLanguage(param.getLanguage()).getName()));

        PoiUtil.setCellStyle(vvSheetName, IntDef.INT_SEVEN, IntDef.INT_TEN + violetLength, styleLyellow);
        String ttcDate = null;
        if (param.getLanguage() == IntDef.INT_TWO) {
            ttcDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, csDate);
        } else {
            ttcDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, csDate);
        }
        PoiUtil.setCellValue(vvSheetName, IntDef.INT_SEVEN, IntDef.INT_TEN + violetLength, ttcDate);
        violetLength += IntDef.INT_ONE;

        // blue
        Map<String, List<CPOOCF01DateEntity>> dateString = new LinkedHashMap<String, List<CPOOCF01DateEntity>>();
        param.getSwapData().put("officeId", param.getCurrentOfficeId());
        param.getSwapData().put("customerId", customerId);
        param.getSwapData().put("status", ActiveFlag.Y);
        param.getSwapData().put("monthRangeFrom",
            DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, monthRangeFromDate));
        param.getSwapData().put("monthRangeTo",
            DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, monthRangeToDate));
        List<CPOOCF01DateEntity> dateList = service.getDateList(param);
        CellStyle styleBlue = styleSheet.getRow(0).getCell(IntDef.INT_THREE).getCellStyle();
        if (dateList.size() > 0) {
            for (int i = 0; i < dateList.size(); i++) {
                if (!dateString.containsKey(dateList.get(i).getCfcMonth())) {
                    PoiUtil.setCellStyle(vvSheetName, IntDef.INT_SEVEN, dateString.size() + violetLength
                            + IntDef.INT_TEN, styleBlue);
                    PoiUtil.setCellStyle(vvSheetName, IntDef.INT_SIX,
                        dateString.size() + violetLength + IntDef.INT_TEN, styleBlue);
                    String dateDate = null;
                    String dateMonth = null;
                    if (param.getLanguage() == IntDef.INT_TWO) {
                        dateDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, dateList.get(i)
                            .getFcDate());
                        String month = MessageManager.getMessage("CPOOCF01_Grid_Month",
                            MessageManager.getLanguage(param.getLanguage()).getName());
                        dateMonth = DateTimeUtil.getMonth(DateTimeUtil.parseDate(dateList.get(i).getCfcMonth(),
                            DateTimeUtil.FORMAT_YEAR_MONTH)) + month;
                    } else {
                        dateDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, dateList.get(i).getFcDate());
                        dateMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MMM, DateTimeUtil.FORMAT_YEAR_MONTH,
                            dateList.get(i).getCfcMonth());
                    }
                    PoiUtil.setCellValue(vvSheetName, IntDef.INT_SEVEN, dateString.size() + violetLength
                            + IntDef.INT_TEN, dateDate);
                    PoiUtil.setCellValue(vvSheetName, IntDef.INT_SIX,
                        dateString.size() + violetLength + IntDef.INT_TEN, dateMonth);
                    List<CPOOCF01DateEntity> dateDetailList = new ArrayList<CPOOCF01DateEntity>();
                    dateDetailList.add(dateList.get(i));
                    dateString.put(dateList.get(i).getCfcMonth(), dateDetailList);
                } else {
                    dateString.get(dateList.get(i).getCfcMonth()).add(dateList.get(i));
                }
            }
            for (int j = IntDef.INT_FOUR; j <= IntDef.INT_FIVE; j++) {
                for (int i = IntDef.INT_TEN + violetLength; i <= violetLength + IntDef.INT_TEN + dateString.size() - 1; i++) {
                    PoiUtil.setCellStyle(vvSheetName, j, i, styleBlue);
                }
            }
            vvSheetName.addMergedRegion(new CellRangeAddress(IntDef.INT_THREE, IntDef.INT_FOUR, IntDef.INT_NINE
                    + violetLength, violetLength + IntDef.INT_NINE + dateString.size() - 1));
        }
        // set no data
        else {
            PoiUtil.setCellStyle(vvSheetName, IntDef.INT_FOUR, violetLength + IntDef.INT_TEN, styleBlue);
            PoiUtil.setCellStyle(vvSheetName, IntDef.INT_FIVE, violetLength + IntDef.INT_TEN, styleBlue);
            vvSheetName.addMergedRegion(new CellRangeAddress(IntDef.INT_THREE, IntDef.INT_FOUR, IntDef.INT_NINE
                    + violetLength, violetLength + IntDef.INT_NINE));
            PoiUtil.setCellStyle(vvSheetName, IntDef.INT_SIX, violetLength + IntDef.INT_TEN, styleBlue);
            PoiUtil.setCellStyle(vvSheetName, IntDef.INT_SEVEN, violetLength + IntDef.INT_TEN, styleBlue);

            String noData = MessageManager.getMessage("CPOOCF01_Grid_BlueNodata",
                MessageManager.getLanguage(param.getLanguage()).getName());
            PoiUtil.setCellValue(vvSheetName, IntDef.INT_SIX, violetLength + IntDef.INT_TEN, noData);
        }

        String messageBlue = MessageManager.getMessage("CPOOCF01_Grid_CustomerForecast",
            MessageManager.getLanguage(param.getLanguage()).getName());
        PoiUtil.setCellValue(vvSheetName, IntDef.INT_FOUR, IntDef.INT_TEN + violetLength, messageBlue);
        if (dateList.size() <= 0) {
            violetLength += 1;
        } else {
            violetLength += dateString.size();
        }

        Cell cell = PoiUtil.getCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWO);
        Cell cellRed = PoiUtil.getCell(styleSheet, IntDef.INT_TWO, IntDef.INT_THREE);
        // setvalue
        for (int i = 0; i < list.size(); i++) {

            CPOOCF01Entity entity = list.get(i);
            String partyType = CodeCategoryManager.getCodeName(param.getLanguage(),
                CodeConst.CodeMasterCategory.PARTS_TYPE, StringUtil.toSafeInteger(entity.getPartType()));
            Cell[] cells = getTemplateCells(SHEET_STYLE, IntDef.INT_TWO, wbTemplate);
            Object[] datas = new Object[] { entity.getNo(), entity.getTtcPartNo(), entity.getCustomerPartNo(),
                entity.getOldTtcPartNo(), entity.getCustomerCode(), entity.getSupplierCode(), partyType,
                entity.getOrderlot() };
            createOneDataRowByTemplate(vvSheetName, IntDef.INT_SEVEN + i, 1, cells, datas);
            int digits = MasterManager.getUomDigits(entity.getUomCode());
            String uomCode = entity.getUomCode();
            CellStyle nonInvoiceQtyStyle = super.getDecimalStyle(wbTemplate, DECIMAL, digits);
            PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT + i, IntDef.INT_NINE, nonInvoiceQtyStyle);
            PoiUtil.setCellValue(vvSheetName, IntDef.INT_EIGHT + i, IntDef.INT_NINE,
                StringUtil.formatBigDecimal(uomCode, entity.getOrderlot()));

            for (int x = IntDef.INT_EIGHT; x < violetLength + IntDef.INT_EIGHT; x++) {
                PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT + i, x + IntDef.INT_TWO, nonInvoiceQtyStyle);
            }

            // yellow
            int j = 0;
            int rowLenth = IntDef.INT_NINE;
            BigDecimal sum = new BigDecimal(0);
            for (Map.Entry<String, List<CPOOCF01CompareByDayEntity>> s : yellowList.entrySet()) {
                boolean flg = true;
                for (CPOOCF01CompareByDayEntity day : s.getValue()) {
                    if (day.getPartsId().equals(entity.getPartsId())
                            && day.getSupplierId().equals(entity.getSupplierId())) {
                        PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT + i, IntDef.INT_ELEVEN + j,
                            nonInvoiceQtyStyle);
                        PoiUtil.setCellValue(vvSheetName, IntDef.INT_EIGHT + i, IntDef.INT_ELEVEN + j,
                            StringUtil.formatBigDecimal(uomCode, day.getOrderBalanceQty()));
                        // sum = sum.add(day.getOrderBalanceQty());
                        sum = DecimalUtil.add(sum, day.getOrderBalanceQty());
                        j++;
                        flg=false;
                        break;
                    }
                }
                if(flg){
                    j++;
                }
            }
            if (yellowList.size() != 0) {
                if (!sum.equals(new BigDecimal(0))) {
                    PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + 1, nonInvoiceQtyStyle);
                    PoiUtil.setCellValue(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + 1,
                        StringUtil.formatBigDecimal(uomCode, sum));
                }
                rowLenth += yellowList.size() + 1;
            } else {
                rowLenth += 1;
            }
            // violet
            if (current) {
                BigDecimal sumMonth = new BigDecimal(0);
                int k = 0;
                for (Map.Entry<String, List<CPOOCF01CurrentMonthEntity>> s : violetString.entrySet()) {
                    for (CPOOCF01CurrentMonthEntity month : s.getValue()) {
                        if (entity.getPartsId().equals(month.getPartsId())
                                && entity.getSupplierId().equals(month.getSupplierId())) {

                            PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + IntDef.INT_TWO + k,
                                nonInvoiceQtyStyle);
                            PoiUtil.setCellValue(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + IntDef.INT_TWO + k,
                                StringUtil.formatBigDecimal(uomCode, month.getSumQty()));

                            sumMonth = sumMonth.add(month.getSumQty());
                            k++;
                            break;
                        }
                    }
                }
                if (violetString.size() != 0) {
                    if (!sumMonth.equals(new BigDecimal(0))) {
                        PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + 1, nonInvoiceQtyStyle);
                        PoiUtil.setCellValue(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + 1,
                            StringUtil.formatBigDecimal(uomCode, sumMonth));
                    }
                    rowLenth += violetString.size() + 1;
                } else {
                    rowLenth += 1;
                }
            }
            param.getSwapData().put("csDate", csDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(csDate);
            cal.add(Calendar.MONTH, IntDef.INT_ONE - IntDef.INT_THREE);
            param.getSwapData().put("twoMonthAgo", cal.getTime());
            param.getSwapData().put("officeId", param.getCurrentOfficeId());
            param.getSwapData().put("customerId", customerId);
            param.getSwapData().put("csDate1", DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, csDate));
            List<CPOOCF01Entity> nearestList = service.getNearestDateList(param);
            PartSortManager.sort(nearestList, "ttcPartNo", "oldTtcPartNo");
            for (int l = 0; l < nearestList.size(); l++) {
                if (entity.getPartsId().equals(nearestList.get(l).getPartsId())) {
                    PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + 1, nonInvoiceQtyStyle);
                    PoiUtil.setCellValue(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + 1,
                        StringUtil.formatBigDecimal(uomCode, nearestList.get(l).getStockQty()));
                    String stringDate = null;
                    if (param.getLanguage() == IntDef.INT_TWO) {
                        stringDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, nearestList.get(l)
                            .getEndingStockDate());
                    } else {
                        stringDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, nearestList.get(l)
                            .getEndingStockDate());
                    }
                    PoiUtil.setCellValue(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + IntDef.INT_TWO, stringDate);

                    if (nearestList.get(l).getEndingStockDate().getTime() != csDate.getTime()) {
                        PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + IntDef.INT_TWO,
                            cellRed.getCellStyle());
                    } else {
                        PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + IntDef.INT_TWO,
                            cell.getCellStyle());
                    }
                }
            }
            rowLenth += IntDef.INT_TWO;

            // light yellow
            PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + 1, nonInvoiceQtyStyle);
            PoiUtil.setCellValue(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + 1,
                StringUtil.formatBigDecimal(uomCode, entity.getImpStockQty()));

            rowLenth += IntDef.INT_ONE;

            // blue
            int k = 0;
            for (Map.Entry<String, List<CPOOCF01DateEntity>> s : dateString.entrySet()) {
                for (CPOOCF01DateEntity dateEntity : s.getValue()) {
                    if (entity.getPartsId().equals(dateEntity.getPartsId())) {
                        PoiUtil.setCellStyle(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + 1 + k, nonInvoiceQtyStyle);
                        PoiUtil.setCellValue(vvSheetName, IntDef.INT_EIGHT + i, rowLenth + 1 + k,
                            StringUtil.formatBigDecimal(uomCode, dateEntity.getCfcQty()));
                        k++;
                        break;
                    }
                }
            }

        }
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE));
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(DECIMAL));
    }
}
