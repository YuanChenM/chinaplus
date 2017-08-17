/**
 * CPMKBF01Controller.java
 * 
 * @screen CPMKBF01
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMKBF01Entity;
import com.chinaplus.web.mm.service.CPMKBF01Service;

/**
 * Kanban Issued Plan Date Master Download
 */
@Controller
public class CPMKBF01Controller extends BaseFileController {

    /** SHEET_KANBAN */
    public static final String SHEET_KANBAN = "KanbanIssuedPlanDate";

    /** DOWNLOAD_NAME */
    public static final String DOWNLOAD_NAME = "KanbanIssuedPlanDateMaster_{0}.xlsx";

    /** SHEET_STYLE */
    private static final String SHEET_STYLE = "style";

    /** Kanban Issued Plan Date Master Download service */
    @Autowired
    private CPMKBF01Service service;

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {

        return ChinaPlusConst.FileId.CPMKBF01;
    }

    /**
     * Kanban Issued Plan Date Master Download check
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     */
    @RequestMapping(value = "/mm/CPMKBF01/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        return new BaseResult<BaseEntity>();
    }

    /**
     * Kanban Issued Plan Date Master Download
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/mm/CPMKBF01/download",
        method = RequestMethod.POST)
    public void download(HttpServletRequest request, HttpServletResponse response) {

        // set common parameters by session
        BaseParam param = this.convertJsonDataForForm(BaseParam.class);
        this.setCommonParam(param, request);
        // checkbox or filters
        String fileName = StringUtil.formatMessage(DOWNLOAD_NAME, param.getClientTime());

        this.downloadExcelWithTemplate(fileName, param, request, response);
    }

    /**
     *
     * @param param
     * @param wbTemplate
     * @param wbOutput
     * @see com.chinaplus.core.base.BaseFileController#writeContentToExcel(com.chinaplus.core.bean.BaseParam,
     *      org.apache.poi.ss.usermodel.Workbook, org.apache.poi.xssf.streaming.SXSSFWorkbook)
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

        BaseParam paramNew = null;
        try {
            paramNew = (BaseParam) param;
            String ttcCustomer = (String) paramNew.getSwapData().get("ttcCustomerCode");
            String ttcNew = StringConst.EMPTY;
            if (!StringUtil.isEmpty(ttcCustomer)) {
                String[] cus = ttcCustomer.split(StringConst.COMMA);

                for (String c : cus) {
                    ttcNew = ttcNew + "'" + c + "'" + StringConst.COMMA;
                }

                ttcNew = ttcNew.substring(0, ttcNew.length() - 1);
                param.getSwapData().put("ttcCustomerCode", ttcNew);
            }

        } catch (Exception e) {
            paramNew = new BaseParam();
        }
        List<CPMKBF01Entity> list = null;
        boolean blankFormatDownK = (boolean) param.getSwapData().get("blankFormatDownK");
        if (!blankFormatDownK) {
            PageResult<CPMKBF01Entity> result = service.getAllList(paramNew);
            if (null != result && null != result.getDatas()) {
                list = result.getDatas();
            } else {
                list = new ArrayList<CPMKBF01Entity>();
            }
        } else {
            list = new ArrayList<CPMKBF01Entity>();
            for (int i = IntDef.INT_ZERO; i < IntDef.INT_TEN; i++) {
                list.add(new CPMKBF01Entity());
            }
        }
        Cell[] templateCells = null;
        int language = param.getLanguage();
        if (IntDef.INT_ONE == language) {
            templateCells = getTemplateCells(SHEET_STYLE, IntDef.INT_EIGHT, wbTemplate);

        } else {
            templateCells = getTemplateCells(SHEET_STYLE, IntDef.INT_NINE, wbTemplate);
        }
        org.apache.poi.ss.usermodel.Sheet sheet = wbTemplate.getSheet(SHEET_KANBAN);
        for (int i = 0; i < list.size(); i++) {
            CPMKBF01Entity entity = list.get(i);
            if (null == entity) {
                continue;
            }
            Object[] arrayObj = new Object[] { StringConst.EMPTY, StringConst.EMPTY, entity.getOfficeCode(),
                entity.getCustomerCode(),
                DateTimeUtil.parseDate(entity.getOrderMonth(), DateTimeUtil.FORMAT_YEAR_MONTH), entity.getFromDate(),
                entity.getToDate() };

            createOneDataRowByTemplate(sheet, IntDef.INT_FIVE + i, templateCells, arrayObj);
        }
        // set value from style sheet
        for (int i = 0; i < IntDef.INT_FIVE; i++) {
            Cell[] styleCells = getTemplateCells(SHEET_STYLE, i, wbTemplate);
            Row dataRow = sheet.createRow(IntDef.INT_SEVEN + list.size() + i);
            for (int y = 0; y < styleCells.length; y++) {
                // set cell format, formula, type
                if (styleCells != null && y < styleCells.length && null != styleCells[y]
                        && !StringUtil.isNullOrEmpty(StringUtil.trim(styleCells[y].getStringCellValue()))) {

                    // create cell
                    Cell cell = dataRow.createCell(y);

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
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE));
    }

}
