/**
 * Controller of tianjing Shipping Route Master Download
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMSRF02Entity;
import com.chinaplus.web.mm.service.CPMSRF02Service;

/**
 * CPMSRF02Controller.
 */
@Controller
public class CPMSRF02Controller extends BaseFileController {

    private static final String STYLE = "style";

    private static final String TIANJINAISIN_SHIPPING_ROUTE = "TIANJINAISIN_Shipping_Route";

    /**
     * cpmpms02Service.
     */
    @Autowired
    private CPMSRF02Service cpmsrf02Service;

    @Override
    protected String getFileId() {
        return FileId.CPMSRF02;
    }

    /**
     * download shipRouteType is tianjing
     *
     * @param param
     * @param wbTemplate
     * @param wbOutput
     * @see com.chinaplus.core.base.BaseFileController#writeContentToExcel(com.chinaplus.core.bean.BaseParam,
     *      org.apache.poi.ss.usermodel.Workbook, org.apache.poi.xssf.streaming.SXSSFWorkbook)
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

        boolean blankFormatDownSR = (boolean) param.getSwapData().get("blankFormatDownSR");
        List<CPMSRF02Entity> cpmsrf02EntityList = new ArrayList<CPMSRF02Entity>();

        if (!blankFormatDownSR) {
            String impOfficeId = StringUtil.toSafeString(param.getSwapData().get("srImpOfficeCode"));
            String shipRouteCode = (String) param.getSwapData().get("shipRouteCode");
            String effFromEtd = (String) param.getSwapData().get("effFromEtd");
            String effToEtd = (String) param.getSwapData().get("effToEtd");
            cpmsrf02EntityList = cpmsrf02Service.getCPMSRF02EntityList(impOfficeId, shipRouteCode,
                DateTimeUtil.parseDate(effFromEtd, "yyyy-MM-dd"), DateTimeUtil.parseDate(effToEtd, "yyyy-MM-dd"));
        } else {
            for(int i = IntDef.INT_ZERO; i < IntDef.INT_TEN; i++) {
                cpmsrf02EntityList.add(new CPMSRF02Entity());
            }
        }
        
        org.apache.poi.ss.usermodel.Sheet sheet = wbTemplate.getSheet(TIANJINAISIN_SHIPPING_ROUTE);
        int size = 0;
        if (cpmsrf02EntityList != null && cpmsrf02EntityList.size() > 0) {
            size = cpmsrf02EntityList.size();
            Cell[] TemplateCells = getTemplateCells(STYLE, NumberConst.IntDef.INT_SEVEN, wbTemplate);

            for (int i = 0; i < cpmsrf02EntityList.size(); i++) {
                CPMSRF02Entity entity = cpmsrf02EntityList.get(i);
                if (null == entity) {
                    continue;
                }
                Object[] arrayObj = null;

                if (entity.getDiscontinueIndicator() != null) {
                    entity.setDiscontinueIndicator(CodeCategoryManager.getCodeName(param.getLanguage(),
                        CodeMasterCategory.DISCONTINUE_INDICATOR, Integer.valueOf(entity.getDiscontinueIndicator())));
                }

                arrayObj = new Object[] { "", "", entity.getShippingRouteCode(), entity.getFirstEtd(),
                    entity.getLastEtd(), entity.getWorkingDays(), entity.getExpVanningLeadtime(),
                    entity.getVanningDate(), entity.getEtdWeek(), entity.getEtdDate(), "",
                    entity.getDeliveryLeadtime(), entity.getImpCcLeadtime(), entity.getDiscontinueIndicator() };

                cpmsrf02Service.createOneDataRowByTemplate(sheet, NumberConst.IntDef.INT_FIVE + i, TemplateCells,
                    arrayObj, param.getLanguage());
            }

        } else {
            size = IntDef.INT_EIGHT;
        }

        // set value from style sheet
        for (int i = 0; i < IntDef.INT_FIVE; i++) {
            Cell[] styleCells = getTemplateCells(STYLE, i, wbTemplate);
            Row dataRow = sheet.createRow(IntDef.INT_SEVEN + size + i);

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
        wbTemplate.setForceFormulaRecalculation(true);
        // remove sheet style
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(STYLE));
    }

}
