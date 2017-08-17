/**
 * CPVIVF03Controller.java
 * 
 * @screen CPVIVF03
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.control;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst.DownloadConst;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inv.entity.CPVIVF03Entity;
import com.chinaplus.web.inv.service.CPVIVF03Service;

/**
 * Download New Invoice Controller.
 */
@Controller
public class CPVIVF03Controller extends BaseFileController {

    /** Download file name */
    private static final String DOWNLOAD_FILE_NAME = "New Invoice_{0}.xlsx";

    /** Excel sheet name */
    private static final String SHEET_NAME = "New Invoice";

    /** Non invoice qty style sheet */
    private static final String NON_INVOICE_QTY_STYLE_SHEET = "nonInvoiceQtyStyle";

    /** Invoice qty style sheet */
    private static final String INVOICE_QTY_STYLE_SHEET = "invoiceQtyStyle";

    /** Detail start line */
    private static final int DETAIL_START_LINE = 9;

    /** Non invoice qty column number */
    private static final int NON_INVOICE_QTY_COL_NUMBER = 7;

    /** Invoice qty start column number */
    private static final int START_INVOICE_QTY_COL_NUMBER = 8;

    /** Invoice qty end column number */
    private static final int END_INVOICE_QTY_COL_NUMBER = 13;

    /** Invoice qty column array */
    private static final String[] INVOICE_QTY_COLUMNS = { "I", "J", "K", "L", "M", "N" };

    /** Non invoice qty column */
    private static final String NON_INVOICE_QTY_COLUMN = "H";

    /** Download New Invoice Service */
    @Autowired
    private CPVIVF03Service cpvivf03Service;

    /**
     * Download check for blank invoice file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return check result
     */
    @RequestMapping(value = "/inv/CPVIVF03/downloadcheck")
    @ResponseBody
    public BaseResult<String> downloadCheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {

        return new BaseResult<String>();
    }

    /**
     * Download blank invoice file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/inv/CPVIVF03/download")
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response) {

        super.setCommonParam(param, request);

        String fileName = StringUtil.formatMessage(DOWNLOAD_FILE_NAME, param.getClientTime());
        super.downloadExcelWithTemplate(fileName, param, request, response);
    }

    /**
     * Write content to excel.
     * 
     * @param param page parameter
     * @param wbTemplate the template excel
     * @param wbOutput the output excel
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

        // Find all parts information in selected orders
        PageResult<CPVIVF03Entity> result = cpvivf03Service.getAllList(param);
        List<CPVIVF03Entity> orderDetailList = result.getDatas();

        // Output order detail to excel file
        Sheet sheet = null;
        if (Language.ENGLISH.getCode() == param.getLanguage()) {
            sheet = wbOutput.getSheet(SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_EN);
        } else {
            sheet = wbOutput.getSheet(SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_CN);
        }

        Cell[] templateCells = super.getTemplateCells(DownloadConst.STYLE_SHEET_NAME, DETAIL_START_LINE, wbTemplate);
        if (orderDetailList != null && orderDetailList.size() > 0) {
            for (int i = 0; i < orderDetailList.size(); i++) {
                CPVIVF03Entity detail = orderDetailList.get(i);

                // Set decimal style
                int digits = MasterManager.getUomDigits(detail.getUomCode());
                CellStyle nonInvoiceQtyStyle = super.getDecimalStyle(wbTemplate, NON_INVOICE_QTY_STYLE_SHEET, digits);
                CellStyle invoiceQtyStyle = super.getDecimalStyle(wbTemplate, INVOICE_QTY_STYLE_SHEET, digits);
                templateCells[NON_INVOICE_QTY_COL_NUMBER].setCellStyle(nonInvoiceQtyStyle);
                for (int j = START_INVOICE_QTY_COL_NUMBER; j <= END_INVOICE_QTY_COL_NUMBER; j++) {
                    templateCells[j].setCellStyle(invoiceQtyStyle);
                }

                // Set conditional format
                SheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();
                for (String invoiceQtyCol : INVOICE_QTY_COLUMNS) {
                    setConditionStyle(scf, invoiceQtyCol, i + DETAIL_START_LINE + 1);
                }

                // Output data
                BigDecimal outNonInvoicedQty = StringUtil.toBigDecimal(DecimalUtil.format(detail.getNonInvoicedQty(),
                    detail.getUomCode()).replaceAll(StringConst.COMMA, StringConst.EMPTY));
                Object[] dataArray = new Object[] { StringUtil.toSafeString(detail.getTtcPartNo()),
                    StringUtil.toSafeString(detail.getPartsNameCn()), StringUtil.toSafeString(detail.getImpOrderNo()),
                    StringUtil.toSafeString(detail.getExpOrderNo()), StringUtil.toSafeString(detail.getCusOrderNo()),
                    StringUtil.toSafeString(detail.getTtcCustomerCode()),
                    StringUtil.toSafeString(detail.getSupplierCode()), outNonInvoicedQty, null, null, null, null, null,
                    null };
                super.createOneDataRowByTemplate(sheet, i + DETAIL_START_LINE, templateCells, dataArray);
            }
        }

        // Remove style sheet
        if (Language.ENGLISH.getCode() == param.getLanguage()) {
            wbOutput.setSheetName(wbOutput.getSheetIndex(SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_EN), SHEET_NAME);
            wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_CN));
        } else {
            wbOutput.setSheetName(wbOutput.getSheetIndex(SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_CN), SHEET_NAME);
            wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_EN));
        }
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(DownloadConst.STYLE_SHEET_NAME));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(NON_INVOICE_QTY_STYLE_SHEET));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(INVOICE_QTY_STYLE_SHEET));
    }

    /**
     * Set excel condition style.
     * 
     * @param scf condition style
     * @param column condition style column
     * @param rownum condition style row
     */
    private void setConditionStyle(SheetConditionalFormatting scf, String column, int rownum) {

        StringBuffer rule = new StringBuffer();
        rule.append(StringConst.DOLLAR + column + rownum);
        rule.append(StringConst.MIDDLE_LINE);
        rule.append(StringConst.DOLLAR + NON_INVOICE_QTY_COLUMN + rownum);
        rule.append(StringConst.GREATER + IntDef.INT_ZERO);
        ConditionalFormattingRule cfr = scf.createConditionalFormattingRule(rule.toString());
        PatternFormatting pf = cfr.createPatternFormatting();
        pf.setFillBackgroundColor(HSSFColor.RED.index);
        FontFormatting ff = cfr.createFontFormatting();
        ff.setFontColorIndex(HSSFColor.WHITE.index);
        ff.setFontStyle(false, true);
        ConditionalFormattingRule[] cfRules = { cfr };
        CellRangeAddress[] regions = { CellRangeAddress.valueOf(column + rownum + StringConst.COLON + column + rownum) };
        scf.addConditionalFormatting(regions, cfRules);
    }

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {

        return FileId.CPVIVF03;
    }

}
