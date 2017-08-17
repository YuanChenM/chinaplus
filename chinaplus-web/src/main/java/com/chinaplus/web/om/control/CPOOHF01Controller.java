package com.chinaplus.web.om.control;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
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

import com.chinaplus.common.consts.ChinaPlusConst.DownloadConst;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.CategoryLanguage;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataType;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.exception.DownloadException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOOHF01Entity;
import com.chinaplus.web.om.service.CPOOHF01Service;

@Controller
public class CPOOHF01Controller extends BaseFileController {

    /** download file name */
    private static final String DL_FILE_NAME = "Order History";

    /** Order History part 1 sheet */
    private static final String SHEET_ORDERHISTORY_PART1 = "ORDER History";

    /** Order History part 2 sheet */
    private static final String SHEET_ORDERHISTORY_PART2 = "ORDER History part2";

    /** QtyStyle sheet */
    private static final String SHEET_QTYSTYLE = "QtyStyle";

    /** String '_part1' */
    private static final String PART1 = "_part1";

    /** String '_part2' */
    private static final String PART2 = "_part2";

    /** Detail start line */
    private static final int DETAIL_START_LINE = 7;

    /** SHIPPING start line */
    private static final int SHIPPING_LINE = 3;

    /** SHIPPING start column number */
    private static final int SHIPPING_COL_NUMBER = 21;

    /** part 2 total column number */
    private static final int PART2_COL_NUMBER = 15;

    /** SQL ID: select Order History */
    private static final String SQLID_SELECT_ORDERHISTORY_LIST = "getOrderHistoryList";

    /** Download Order History File Service */
    @Autowired
    private CPOOHF01Service service;

    /**
     * Order History download check
     * 
     * @param param
     *        PageParam
     * @param request
     *        HttpServletRequest
     * @param response
     *        HttpServletResponse
     * @return BaseResult<BaseEntity>
     * @throws ParseException
     *         E
     */
    @RequestMapping(value = "/om/CPOOHF01/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> custdownloadcheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {
        BaseResult<String> result = new BaseResult<>();
        if (param.getSelectedDatas() != null && param.getSelectedDatas().size() > 0) {
            param.setFilters(null);
        }
        StringUtil.buildLikeCondition(param, "ttcPartsNo");
        StringUtil.buildLikeCondition(param, "impPONo");
        StringUtil.buildLikeCondition(param, "expPONo");
        StringUtil.buildLikeCondition(param, "customerOrderNo");
        this.setCommonParam(param, request);
        PageResult<CPOOHF01Entity> orderHistoryResult = service.getAllList(SQLID_SELECT_ORDERHISTORY_LIST, param);
        List<CPOOHF01Entity> orderHistorylist = orderHistoryResult.getDatas();
        if (orderHistorylist == null || orderHistorylist.size() == 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1005_001);
            result.addMessage(message);
        }
        if (orderHistorylist.size() > ConfigManager.getDownloadFileMaxRecore()) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1005_002);
            long limitSize = ConfigManager.getDownloadFileMaxRecore();
            message.setMessageArgs(new String[] { Long.toString(limitSize) });
            result.addMessage(message);
        }
        return result;
    }

    /**
     * Order History download
     * 
     * @param param
     *        PageParam
     * @param request
     *        HttpServletRequest
     * @param response
     *        HttpServletResponse
     */
    @RequestMapping(value = "/om/CPOOHF01/download",
        method = RequestMethod.POST)
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response)
        throws BusinessException, DownloadException {
        if (param.getSelectedDatas() != null && param.getSelectedDatas().size() > 0) {
            param.setFilters(null);
        }
        StringUtil.buildLikeCondition(param, "ttcPartsNo");
        StringUtil.buildLikeCondition(param, "impPONo");
        StringUtil.buildLikeCondition(param, "expPONo");
        StringUtil.buildLikeCondition(param, "customerOrderNo");
        this.setCommonParam(param, request);
        String filename = DL_FILE_NAME + "_" + param.getClientTime() + ".xlsx";
        this.downloadExcelWithTemplate(StringUtil.formatMessage(filename, param.getClientTime()), param, request,
            response);
    }

    /**
     * Write To Order History Excel
     * 
     * @param param
     *        T
     * @param wbTemplate
     *        Workbook
     * @param wbOutput
     *        SXSSFWorkbook
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput)
        throws BusinessException, DownloadException {
        // get style sheet
        Sheet qtyStyleSheet = wbTemplate.getSheet(SHEET_QTYSTYLE);
        // Get data sheet
        Sheet orderHistorySheet = wbTemplate.getSheet(SHEET_ORDERHISTORY_PART1);
        Sheet orderHistoryPart2 = wbTemplate.getSheet(SHEET_ORDERHISTORY_PART2);
        // Get excel template cells
        Cell[] qtyCells = null;
        Cell[] qtyCellPart1 = null;
        Cell[] qtyCellPart2 = null;
        List<Cell> qtyCellList = new ArrayList<Cell>();
        String stylePart1SheetName = null;
        String stylePart2SheetName = null;
        if (Language.ENGLISH.getCode() == param.getLanguage()) {
            stylePart1SheetName = DownloadConst.STYLE_SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_EN + PART1;
            stylePart2SheetName = DownloadConst.STYLE_SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_EN + PART2;
        } else {
            stylePart1SheetName = DownloadConst.STYLE_SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_CN + PART1;
            stylePart2SheetName = DownloadConst.STYLE_SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_CN + PART2;
        }
        qtyCellPart1 = super.getTemplateCells(stylePart1SheetName, DETAIL_START_LINE, wbTemplate);
        qtyCellPart2 = super.getTemplateCells(stylePart2SheetName, DETAIL_START_LINE, wbTemplate);
        for (Cell cell : qtyCellPart1) {
            qtyCellList.add(cell);
        }
        CellStyle styleOnShipping = PoiUtil.getCell(qtyStyleSheet, 2, 6).getCellStyle();
        CellStyle styleETD = PoiUtil.getCell(qtyStyleSheet, 3, 6).getCellStyle();
        CellStyle styleETDdate = PoiUtil.getCell(qtyStyleSheet, 4, 6).getCellStyle();
        CellStyle styleETA = PoiUtil.getCell(qtyStyleSheet, 5, 6).getCellStyle();
        CellStyle styleETAdate = PoiUtil.getCell(qtyStyleSheet, 6, 6).getCellStyle();
        Cell styleQty = PoiUtil.getCell(qtyStyleSheet, 7, 6);
        // get order history list
        List<CPOOHF01Entity> orderHistoryList = service.getOrderHistory(param);

        List<Integer> partsIdList = new ArrayList<Integer>();
        for (CPOOHF01Entity entity : orderHistoryList) {
            Integer partsId = entity.getPartsId();
            if (!partsIdList.contains(partsId)) {
                partsIdList.add(partsId);
            }
        }
        param.getSwapData().put("partsIdList", partsIdList);
        Map<String, List<Date>> onShippingHeader = service.getOnShippingInfoList(param, orderHistoryList);
        List<Date> etdList = onShippingHeader.get("ETD");
        List<Date> etaList = onShippingHeader.get("ETA");
        // On shipping header
        int row = SHIPPING_LINE;
        int column = SHIPPING_COL_NUMBER;
        int shippingPlanHeaderSize = 0;
        if (etdList != null && !etdList.isEmpty()) {
            shippingPlanHeaderSize = etdList.size();
            /* fill shippingPlan column header body into Excel */
            for (int i = 0; i < shippingPlanHeaderSize; i++) {
                // set ETD column whidth
                orderHistorySheet.setColumnWidth(column + i - 1, qtyStyleSheet.getColumnWidth(5));
                /* OnShipping header */
                PoiUtil.setCellStyle(orderHistorySheet, row, column + i, styleOnShipping);
                /* etd */
                PoiUtil.setCellStyle(orderHistorySheet, row + 1, column + i, styleETD);
                PoiUtil.setCellValue(orderHistorySheet, row + 1, column + i, "ETD");
                PoiUtil.setCellStyle(orderHistorySheet, row + 2, column + i, styleETDdate);
                PoiUtil.setCellValue(orderHistorySheet, row + 2, column + i,
                    DateFormatUtils.format(etdList.get(i), "dd MMM yyyy", Locale.ENGLISH));
                /* eta */
                PoiUtil.setCellStyle(orderHistorySheet, row + 3, column + i, styleETA);
                PoiUtil.setCellValue(orderHistorySheet, row + 3, column + i, "ETA");
                PoiUtil.setCellStyle(orderHistorySheet, row + 4, column + i, styleETAdate);
                if (etaList.get(i) == null) {
                    PoiUtil.setCellValue(orderHistorySheet, row + 4, column + i, "");
                } else {
                    PoiUtil.setCellValue(orderHistorySheet, row + 4, column + i,
                        DateFormatUtils.format(etaList.get(i), "dd MMM yyyy", Locale.ENGLISH));
                }
                /* add qtyStyleCell */
                qtyCellList.add(styleQty);
            }
            orderHistorySheet.addMergedRegion(new CellRangeAddress(row - 1, row - 1, column - 2, column
                    + shippingPlanHeaderSize - 2));
        }
        // qtyStyle add part 2
        for (Cell cell : qtyCellPart2) {
            qtyCellList.add(cell);
        }
        qtyCells = qtyCellList.toArray(new Cell[1]);
        // sheet add part 2
        column = column + shippingPlanHeaderSize - 1;
        // Merged Imp W/H Stock header
        orderHistorySheet.addMergedRegion(new CellRangeAddress(row - 1, row - 1, column, column + 2));
        for (int i = 1; i <= PART2_COL_NUMBER; i++) {
            orderHistorySheet.setColumnWidth(column + i - 1, orderHistoryPart2.getColumnWidth(i - 1));
            Cell cell1 = PoiUtil.getCell(orderHistoryPart2, row, i);
            Cell cell2 = PoiUtil.getCell(orderHistoryPart2, row + 1, i);
            PoiUtil.setCellStyle(orderHistorySheet, row, column + i, cell1.getCellStyle());
            PoiUtil.setCellValue(orderHistorySheet, row, column + i, cell1.getStringCellValue());
            for (int j = 0; j < 4; j++) {
                PoiUtil.setCellStyle(orderHistorySheet, row + 1 + j, column + i, cell2.getCellStyle());
            }
            orderHistorySheet.addMergedRegion(new CellRangeAddress(row, row + 3, column + i - 1, column + i - 1));
            PoiUtil.setCellValue(orderHistorySheet, row + 1, column + i, cell2.getStringCellValue());
        }

        for (int i = 0; i < orderHistoryList.size(); i++) {
            CPOOHF01Entity entity = orderHistoryList.get(i);
            // Out put data
            List<Object> dataList = new ArrayList<Object>();
            int digits = MasterManager.getUomDigits(entity.getUomCode());
            CellStyle qtyStyle = super.getDecimalStyle(wbTemplate, SHEET_QTYSTYLE, digits);
            dataList.add(StringUtil.toSafeString(entity.getTtcPartsNo()));
            dataList.add(StringUtil.toSafeString(entity.getPartsNameEN()));
            dataList.add(StringUtil.toSafeString(entity.getPartsNameCN()));
            dataList.add(StringUtil.toSafeString(entity.getCustBackNo()));
            dataList.add(StringUtil.toSafeString(entity.getExpRegion()));
            dataList.add(StringUtil.toSafeString(entity.getTtcSuppCode()));
            dataList.add(StringUtil.toSafeString(entity.getSuppPartsNo()));
            dataList.add(StringUtil.toSafeString(entity.getImpRegion()));
            dataList.add(StringUtil.toSafeString(entity.getTtcCusCode()));
            dataList.add(StringUtil.toSafeString(entity.getCustPartsNo()));
            String transportMode = "";
            if (entity.getTransportMode() == 3) {
                transportMode = CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.TRANSPORT_MODE,
                    1)
                        + ','
                        + CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.TRANSPORT_MODE, 2);
            } else {
                transportMode = CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.TRANSPORT_MODE,
                    entity.getTransportMode());
            }
            dataList.add(transportMode);
            dataList.add(CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.BUSINESS_TYPE,
                entity.getBusinessType()));
            dataList.add(StringUtil.toSafeString(entity.getCustomerOrderNo()));
            dataList.add(StringUtil.toSafeString(entity.getImpPONo()));
            dataList.add(StringUtil.toSafeString(entity.getExpPONo()));
            dataList.add(entity.getExpSODate());
            dataList.add(entity.getOrderQty());
            qtyCells[IntDef.INT_SIXTEEN].setCellStyle(qtyStyle);
            dataList.add(entity.getExpBalanceOrder());
            qtyCells[IntDef.INT_SEVENTEEN].setCellStyle(qtyStyle);
            dataList.add(entity.getExpWHStock());
            qtyCells[IntDef.INT_EIGHTEEN].setCellStyle(qtyStyle);
            dataList.add(entity.getOnShippingQty());
            qtyCells[IntDef.INT_NINETEEN].setCellStyle(qtyStyle);
            BigDecimal[] shippingQtyList = entity.getOnShippingQtyArray();
            int onshippingStart = SHIPPING_COL_NUMBER - IntDef.INT_ONE;
            for (int j = 0; j < shippingPlanHeaderSize; j++) {
                dataList.add(shippingQtyList[j]);
                qtyCells[onshippingStart].setCellStyle(qtyStyle);
                onshippingStart++;
            }
            dataList.add(entity.getImpStockQty());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(entity.getImpECIQty());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(entity.getAvailableImpStock());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(entity.getImpPrepareOBQty());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(entity.getImpNGQty());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(entity.getTotalSmBalanceQty());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(entity.getImpOrderBalance());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(entity.getImpDeliveredQty());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(entity.getCustomerBalance());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(entity.getImpAdjustedQty());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(entity.getCancelledQty());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(entity.getStockTransferOut());
            qtyCells[onshippingStart++].setCellStyle(qtyStyle);
            dataList.add(StringUtil.toSafeString(entity.getTransferToDetails()));
            dataList.add(StringUtil.toSafeString(entity.getTransferFrom()));
            // status
            dataList.add(CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.DIS_ORDER_STATUS,
                Integer.valueOf(entity.getDisOrderStatus())));
            Object[] dataArray = dataList.toArray(new Object[1]);
            super.createOneDataRowByTemplate(orderHistorySheet, i + DETAIL_START_LINE, qtyCells, dataArray);
        }
        // footer
        outputFooter(param, wbTemplate, orderHistorySheet, orderHistoryList.size() + DETAIL_START_LINE);

        // Remove style sheet
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_ORDERHISTORY_PART2));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(DownloadConst.STYLE_SHEET_NAME
                + DownloadConst.SHEET_NAME_SUFFIX_EN + PART1));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(DownloadConst.STYLE_SHEET_NAME
                + DownloadConst.SHEET_NAME_SUFFIX_EN + PART2));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(DownloadConst.STYLE_SHEET_NAME
                + DownloadConst.SHEET_NAME_SUFFIX_CN + PART1));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(DownloadConst.STYLE_SHEET_NAME
                + DownloadConst.SHEET_NAME_SUFFIX_CN + PART2));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_QTYSTYLE));
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

        String downloadTime = DateTimeUtil.formatDate(dateTimeFormat, service.getDBDateTime(param.getOfficeTimezone()),
            lang);

        param.setFilter("officeId", param.getCurrentOfficeId());
        Map<Integer, String> lastSyncTimeMap = service.searchDataSyncTime(param, dateTimeFormat, lang);

        String lastSsmsSyncTime = lastSyncTimeMap.get(SyncTimeDataType.SSMS);
        String lastTTLogicVVTime = lastSyncTimeMap.get(SyncTimeDataType.TT_LOGIX_VV);

        // output
        int srcRowNum = IntDef.INT_FIFTEEN;
        int scrColumnNum = IntDef.INT_ZERO;
        int rowNum = maxRowNum + IntDef.INT_ONE;
        int columnNum = IntDef.INT_ZERO;

        Sheet sheet_style_cell = wbTemplate.getSheet(SHEET_QTYSTYLE);

        Cell cell = super.copyCell(sheet_style_cell, sheet, srcRowNum, scrColumnNum, rowNum, columnNum, false);
        cell.setCellValue(MessageManager.getMessage("CPOOHF01_Label_DownloadTime") + StringConst.BLANK + downloadTime
                + StringConst.BLANK + "(" + officeCode + ")");
        cell = super.copyCell(sheet_style_cell, sheet, srcRowNum, scrColumnNum, rowNum + IntDef.INT_ONE, columnNum,
            false);
        cell.setCellValue(MessageManager.getMessage("CPOOHF01_Label_LastSsmsDataTime") + StringConst.BLANK
                + lastSsmsSyncTime);
        cell = super.copyCell(sheet_style_cell, sheet, srcRowNum, scrColumnNum, rowNum + IntDef.INT_TWO, columnNum,
            false);
        cell.setCellValue(MessageManager.getMessage("CPOOHF01_Label_LastTime", new String[] { officeCode })
                + StringConst.BLANK + lastTTLogicVVTime);
    }

    @Override
    protected String getFileId() {
        return FileId.CPOOHF01;
    }

}
