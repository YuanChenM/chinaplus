/**
 * CPOCSF01Controller.java
 * 
 * @screen CPOCSS01
 * @author li_feng
 */
package com.chinaplus.web.om.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Customer Stock DownLoad Screen Controller.
 */
@Controller
public class CPOCSF01Controller extends BaseFileController {

    /** download file name */
    private static final String DL_FILE_NAME = "Customer Stock Blank Table.xlsx";
    /** sheet name */
    private static final String SHEET_CPU = "CustomerStock";
    /** sheet name */
    private static final String SHEET_STYLE = "style";
    /** rol and col */
    private static final Integer ROL_COL = 2;

    /**
     * Download Customer Stock download check
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     */
    @RequestMapping(value = "/om/CPOCSF01/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        return new BaseResult<BaseEntity>();
    }

    /**
     * Download Customer Stock download
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/om/CPOCSF01/download",
        method = RequestMethod.POST)
    public void download(HttpServletRequest request, HttpServletResponse response) {

        BaseParam param = this.convertJsonDataForForm(BaseParam.class);

        this.setCommonParam(param, request);

        this.downloadExcelWithTemplate(StringUtil.formatMessage(DL_FILE_NAME, param.getClientTime()), param, request,
            response);
    }

    /**
     * Write To Customer Stock Excel
     * 
     * @param param T
     * @param wbTemplate Workbook
     * @param wbOutput SXSSFWorkbook
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

        // get style sheet
        Sheet styleSheet = wbTemplate.getSheet(SHEET_STYLE);
        Sheet blankSheet = wbOutput.getSheet("sheet1");
        
        
        for (int i = IntDef.INT_SEVEN; i < IntDef.INT_SEVENTEEN; i++) {
            int col = 1;
            
            PoiUtil.setCellStyle(blankSheet, i, col++, PoiUtil
                .getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_ONE).getCellStyle());
            PoiUtil.setCellStyle(blankSheet, i, col++, PoiUtil
                .getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWO).getCellStyle());
            PoiUtil.setCellStyle(blankSheet, i, col++, PoiUtil
                .getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_THREE).getCellStyle());
            PoiUtil.setCellStyle(blankSheet, i, col++, PoiUtil
                .getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_FOUR).getCellStyle());
            if (param.getLanguage() == IntDef.INT_TWO) {
                PoiUtil.setCellStyle(blankSheet, i, col++, PoiUtil
                    .getOrCreateCell(styleSheet, IntDef.INT_TWO, IntDef.INT_FIVE).getCellStyle());
            } else {
                PoiUtil.setCellStyle(blankSheet, i, col++, PoiUtil
                    .getOrCreateCell(styleSheet, IntDef.INT_THREE, IntDef.INT_FIVE).getCellStyle());
            }
            
        }
        
        
        wbOutput.setForceFormulaRecalculation(true);
        wbOutput.setSheetName(0, SHEET_CPU);

        String vv = CodeCategoryManager.getCodeName(param.getLanguage(),
            CodeMasterCategory.BUSINESS_PATTERN, BusinessPattern.V_V);
        String aisin = CodeCategoryManager.getCodeName(param.getLanguage(),
            CodeMasterCategory.BUSINESS_PATTERN, BusinessPattern.AISIN);
        String[] pos = { "", vv, aisin };
        CellRangeAddressList addressList = new CellRangeAddressList(ROL_COL, ROL_COL, ROL_COL, ROL_COL);
        DataValidationHelper helper = wbOutput.getSheetAt(0).getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(pos);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }

        wbOutput.getSheetAt(0).addValidationData(dataValidation);
        wbTemplate.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE));

    }

    @Override
    protected String getFileId() {
        return FileId.CPOCSF01;
    }

}
