/**
 * CPVIVF01Controller.java
 * 
 * @screen CPVIVF01
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.chinaplus.common.consts.ChinaPlusConst.DownloadConst;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inv.entity.CPVIVF01Entity;
import com.chinaplus.web.inv.service.CPVIVF01Service;

/**
 * Download Invoice Supplementary Data File Controller.
 */
@Controller
public class CPVIVF01Controller extends BaseFileController {

    /** Download file name */
    private static final String DOWNLOAD_FILE_NAME = "Invoice Supplementary Data_{0}.xlsx";

    /** Excel sheet name */
    private static final String SHEET_NAME = "Invoice Supplementary Data";

    /** Normal qty style sheet */
    private static final String NORMAL_QTY_STYLE_SHEET = "normalQtyStyle";

    /** White qty style sheet */
    private static final String WHITE_QTY_STYLE_SHEET = "whiteQtyStyle";

    /** Detail start line */
    private static final int DETAIL_START_LINE = 4;

    /** Total qty column number */
    private static final int TOTAL_QTY_COL_NUMBER = 6;

    /** Detail qty columns number */
    private static final int[] DETAIL_QTY_COLS_NUMBER = { 9, 11, 13, 15 };

    /** Download Invoice Supplementary Data File Service */
    @Autowired
    private CPVIVF01Service cpvivf01Service;

    /**
     * Download check for blank invoice file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return check result
     */
    @RequestMapping(value = "/inv/CPVIVF01/downloadcheck")
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
    @RequestMapping(value = "/inv/CPVIVF01/download")
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

        // Find all invoice supplementary data
        List<String> multiSupplierList = new ArrayList<String>();
        String uploadId = ((PageParam) param).getSelectedDatas().get(0);
        CPVIVF01Entity condition = new CPVIVF01Entity();
        condition.setUploadId(uploadId);
        List<CPVIVF01Entity> supplementaryDatas = cpvivf01Service.getSupplementaryData(condition);
        if (supplementaryDatas != null && supplementaryDatas.size() > 0) {
            for (int i = 0; i < supplementaryDatas.size(); i++) {
                CPVIVF01Entity detail = supplementaryDatas.get(i);
                String key = detail.getInvoiceNo() + StringConst.COMMA + detail.getTtcPartNo() + StringConst.COMMA
                        + detail.getSupplierPartNo();
                if (detail.getRownum() > 1 && !multiSupplierList.contains(key)) {
                    multiSupplierList.add(key);
                }
            }
        }

        // Get excel template cells
        Cell[] normalCells = null;
        Cell[] whiteCells = null;
        Cell[] normalRedCells = null;
        Cell[] whiteRedCells = null;
        String styleSheetName = null;
        if (Language.ENGLISH.getCode() == param.getLanguage()) {
            styleSheetName = DownloadConst.STYLE_SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_EN;
            normalCells = super.getTemplateCells(styleSheetName, DETAIL_START_LINE, wbTemplate);
            whiteCells = super.getTemplateCells(styleSheetName, DETAIL_START_LINE + IntDef.INT_ONE, wbTemplate);
            normalRedCells = super.getTemplateCells(styleSheetName, DETAIL_START_LINE + IntDef.INT_TWO, wbTemplate);
            whiteRedCells = super.getTemplateCells(styleSheetName, DETAIL_START_LINE + IntDef.INT_THREE, wbTemplate);
        } else {
            styleSheetName = DownloadConst.STYLE_SHEET_NAME + DownloadConst.SHEET_NAME_SUFFIX_CN;
            normalCells = super.getTemplateCells(styleSheetName, DETAIL_START_LINE, wbTemplate);
            whiteCells = super.getTemplateCells(styleSheetName, DETAIL_START_LINE + IntDef.INT_ONE, wbTemplate);
            normalRedCells = super.getTemplateCells(styleSheetName, DETAIL_START_LINE + IntDef.INT_TWO, wbTemplate);
            whiteRedCells = super.getTemplateCells(styleSheetName, DETAIL_START_LINE + IntDef.INT_THREE, wbTemplate);
        }

        // Output supplementary data to excel file
        Sheet sheet = wbOutput.getSheet(SHEET_NAME);
        if (supplementaryDatas != null && supplementaryDatas.size() > 0) {
            for (int i = 0; i < supplementaryDatas.size(); i++) {
                // Set supplier qty when only has one supplier
                CPVIVF01Entity detail = supplementaryDatas.get(i);
                String key = detail.getInvoiceNo() + StringConst.COMMA + detail.getTtcPartNo() + StringConst.COMMA
                        + detail.getSupplierPartNo();
                if (!multiSupplierList.contains(key)) {
                    detail.setSupplierQty(detail.getTotalQty());
                }

                // Choose the right style
                Cell[] templateCells = null;
                if (detail.getRownum() == 1) {
                    if (detail.getEta() == null) {
                        templateCells = normalCells;
                    } else {
                        templateCells = normalRedCells;
                    }
                } else {
                    if (detail.getEta() == null) {
                        templateCells = whiteCells;
                    } else {
                        templateCells = whiteRedCells;
                    }
                }

                // Set decimal style
                int digits = MasterManager.getUomDigits(detail.getUomCode());
                CellStyle normalQtyStyle = super.getDecimalStyle(wbTemplate, NORMAL_QTY_STYLE_SHEET, digits);
                CellStyle whiteQtyStyle = super.getDecimalStyle(wbTemplate, WHITE_QTY_STYLE_SHEET, digits);
                if (detail.getRownum() == 1) {
                    templateCells[TOTAL_QTY_COL_NUMBER].setCellStyle(normalQtyStyle);
                } else {
                    templateCells[TOTAL_QTY_COL_NUMBER].setCellStyle(whiteQtyStyle);
                }
                for (int col : DETAIL_QTY_COLS_NUMBER) {
                    templateCells[col].setCellStyle(normalQtyStyle);
                }

                // Output data
                BigDecimal outTotalQty = StringUtil.toBigDecimal(DecimalUtil.format(detail.getTotalQty(),
                    detail.getUomCode()).replaceAll(StringConst.COMMA, StringConst.EMPTY));
                BigDecimal outSuppQty = null;
                if (detail.getSupplierQty() != null) {
                    outSuppQty = outTotalQty;
                }
                Object[] dataArray = new Object[] { StringUtil.toSafeString(detail.getInvoiceNo()), detail.getEtd(),
                    StringUtil.toSafeString(detail.getTtcCustomerCode()),
                    StringUtil.toSafeString(detail.getMailCustomerCode()),
                    StringUtil.toSafeString(detail.getTtcPartNo()),
                    StringUtil.toSafeString(detail.getSupplierPartNo()), outTotalQty,
                    StringUtil.toSafeString(detail.getTransportMode()),
                    StringUtil.toSafeString(detail.getTtcSupplierCode()), outSuppQty, null, null, null, null, null,
                    null, detail.getEta(), detail.getInboundDate() };
                super.createOneDataRowByTemplate(sheet, i + DETAIL_START_LINE, templateCells, dataArray);
            }
        }

        // Remove style sheet
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(DownloadConst.STYLE_SHEET_NAME
                + DownloadConst.SHEET_NAME_SUFFIX_EN));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(DownloadConst.STYLE_SHEET_NAME
                + DownloadConst.SHEET_NAME_SUFFIX_CN));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(NORMAL_QTY_STYLE_SHEET));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(WHITE_QTY_STYLE_SHEET));
    }

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {

        return FileId.CPVIVF01;
    }

}
