/**
 * CPMSPF01Controller.
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.entity.TnmReason;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.service.CPMSPF01Service;

/**
 * Shipping Plan Rev Reason Download
 */
@Controller
public class CPMSPF01Controller extends BaseFileController {

    /** SHEET_STYLE */
    public static final String SHEET_STYLE = "style";

    /** SHEET_EMAIL */
    public static final String SHEET_NAME = "RevisionReason";

    /** DISCONTINUE_INDICATOR */
    public static final String DISCONTINUE_INDICATOR = "DISCONTINUE_INDICATOR";

    /** BUSINESS_PATTERN */
    public static final String BUSINESS_PATTERN = "BUSINESS_PATTERN";

    /** xlsx */
    public static final String XLSX = "ShippingPlanRevisionReasonMaster_{0}.xlsx";

    /** service */
    @Autowired
    private CPMSPF01Service service;

    /**
     *
     * @return
     * @see com.chinaplus.core.base.BaseFileController#getFileId()
     */
    @Override
    protected String getFileId() {
        return ChinaPlusConst.FileId.CPMSPF01;
    }

    /**
     * check before download
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<String>
     */
    @RequestMapping(value = "/mm/CPMSPF01/downloadcheck")
    @ResponseBody
    public BaseResult<String> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        BaseResult<String> result = new BaseResult<String>();
        return result;
    }

    /**
     * download
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/mm/CPMSPF01/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        PageParam param = this.convertJsonDataForForm(PageParam.class);
        this.setCommonParam(param, request);
        // checkbox or filters
        String fileName = StringUtil.formatMessage(XLSX, param.getClientTime());

        this.downloadExcelWithTemplate(fileName, param, request, response);
    }

    @Override
    protected <T extends BaseParam> void writeDynamicTemplate(T param, Workbook workbook) {
        BaseParam paramNew = null;
        try {
            paramNew = (BaseParam) param;
        } catch (Exception e) {
            paramNew = new BaseParam();
        }
        boolean blankFormatDownSP = (boolean) param.getSwapData().get("blankFormatDownSP");

        List<TnmReason> reasons = null;
        if (!blankFormatDownSP) {
            reasons = service.getReasonList(paramNew);
        } else {
            reasons = new ArrayList<TnmReason>();
            for(int i = IntDef.INT_ZERO; i < IntDef.INT_TEN; i++) {
                reasons.add(new TnmReason());
            }
        }
        Cell[] templateCells = getTemplateCells(SHEET_STYLE, IntDef.INT_ZERO, workbook);
        Sheet sheet = workbook.getSheet(SHEET_NAME);

        for (int i = 0; i < reasons.size(); i++) {
            TnmReason entity = reasons.get(i);
            if (null == entity) {
                continue;
            }
            Object[] arrayObj = new Object[] { StringConst.EMPTY, StringConst.EMPTY,
                CodeCategoryManager.getCodeName(param.getLanguage(), BUSINESS_PATTERN, entity.getBusinessPattern()),
                entity.getReasonDesc(),
                CodeCategoryManager.getCodeName(param.getLanguage(), DISCONTINUE_INDICATOR, entity.getInactiveFlag()) };

            createOneDataRowByTemplate1(sheet, IntDef.INT_FIVE + i, templateCells, arrayObj, param.getLanguage());
        }

        // set value from style sheet
        for (int i = 1; i < IntDef.INT_SIX; i++) {
            Cell[] styleCells = getTemplateCells(SHEET_STYLE, i, workbook);
            Row dataRow = sheet.createRow(IntDef.INT_SEVEN + reasons.size() + i);
            if (styleCells != null) {
                for (int y = 0; y < styleCells.length; y++) {

                    // create cell
                    Cell cell = dataRow.createCell(y);

                    // set cell format, formula, type
                    if (styleCells != null && y < styleCells.length && null != styleCells[y]) {
                        cell.setCellStyle(styleCells[y].getCellStyle());
                        int cellType = styleCells[y].getCellType();
                        cell.setCellType(cellType);
                        if (Cell.CELL_TYPE_FORMULA == cellType) {
                            cell.setCellFormula(styleCells[y].getCellFormula());
                        }
                        cell.setCellValue(styleCells[y].getStringCellValue());
                    }
                }
            }

        }
        workbook.removeSheetAt(workbook.getSheetIndex(SHEET_STYLE));

    }

    /**
     * 
     * @param sheet sheet
     * @param rowNum rowNum
     * @param templateCells templateCells
     * @param data data
     * @param lang lang
     * @throws BusinessException BusinessException
     */
    protected static void createOneDataRowByTemplate1(Sheet sheet, int rowNum, Cell[] templateCells, Object[] data,
        Integer lang) throws BusinessException {
        if (sheet != null && data != null) {

            // output data rows
            int templateCellCount = 0;
            if (templateCells != null) {
                templateCellCount = templateCells.length;
            }

            if (data != null) {
                int rowItemCount = data.length;

                // create new data row
                Row dataRow = sheet.createRow(rowNum);
                for (int j = 0; j < rowItemCount; j++) {
                    // create cell
                    Cell cell = dataRow.createCell(j);

                    // set cell format, formula, type
                    if (templateCells != null && j < templateCellCount && null != templateCells[j]) {
                        cell.setCellStyle(templateCells[j].getCellStyle());
                        int cellType = templateCells[j].getCellType();
                        cell.setCellType(cellType);
                        if (Cell.CELL_TYPE_FORMULA == cellType) {
                            cell.setCellFormula(templateCells[j].getCellFormula());
                        }
                    }
                    String[] pos = null;
                    if (j == IntDef.INT_TWO) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_PATTERN);
                    } else if (j == IntDef.INT_FOUR) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.DISCONTINUE_INDICATOR);
                    }

                    if (pos != null && pos.length != 0) {
                        CellRangeAddressList addressList = new CellRangeAddressList(rowNum, rowNum, j, j);
                        DataValidationHelper helper = sheet.getDataValidationHelper();
                        DataValidationConstraint constraint = helper.createExplicitListConstraint(pos);
                        DataValidation dataValidation = helper.createValidation(constraint, addressList);

                        if (dataValidation instanceof XSSFDataValidation) {
                            dataValidation.setSuppressDropDownArrow(true);
                            dataValidation.setShowErrorBox(true);
                        } else {
                            dataValidation.setSuppressDropDownArrow(false);
                        }
                        sheet.addValidationData(dataValidation);
                    }

                    // set cell value
                    Object cellValue = data[j];
                    if (cellValue == null) {
                        // nothing to set
                        ;
                    } else if (cellValue instanceof Date) {
                        cell.setCellValue((Date) cellValue);
                    } else if (cellValue instanceof Calendar) {
                        cell.setCellValue((Calendar) cellValue);
                    } else if (cellValue instanceof String) {
                        cell.setCellValue(cellValue.toString());
                    } else if (cellValue instanceof Boolean) {
                        cell.setCellValue(((Boolean) cellValue));
                    } else if (cellValue instanceof BigDecimal) {
                        cell.setCellValue(((BigDecimal) cellValue).doubleValue());
                    } else if (cellValue instanceof Integer) {
                        cell.setCellValue(((Integer) cellValue).doubleValue());
                    } else if (cellValue instanceof Double) {
                        cell.setCellValue(((Double) cellValue).doubleValue());
                    } else {
                        cell.setCellValue(String.valueOf(cellValue));
                    }
                }
            }
        }
    }
}
