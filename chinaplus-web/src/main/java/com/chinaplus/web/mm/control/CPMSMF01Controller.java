/**
 * Controller of Download Customer Data in SSMS & ORION PLUS data
 * 
 * @screen CPMSMF01
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

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

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMSMF01Entity;
import com.chinaplus.web.mm.service.CPMSMF01Service;

/**
 * Controller of Download Customer Data in SSMS & ORION PLUS data
 */
@Controller
public class CPMSMF01Controller extends BaseFileController {

    private static final String STYLE = "style";

    private static final String SSMSCUSTOMER = "SSMSCustomer";

    private static final String CUSTOMERDATAINSSMSORIONPLUS_XLSX = "CustomerDatainSSMS&ORIONPLUS{0}.xlsx";

    /**
     * cpmsmf01Service.
     */
    @Autowired
    private CPMSMF01Service cpmsmf01Service;

    @Override
    protected String getFileId() {
        return FileId.CPMSMF01;
    }

    /**
     * Download Customer Data in SSMS & ORION PLUS data check
     * 
     * @param param param
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     * @throws Exception e
     */
    @RequestMapping(value = "/mm/CPMSMF01/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        String ssmsCustomerCode = (String) param.getSwapData().get("ssmsCustomerCode");
        String vendorRoute = (String) param.getSwapData().get("vendorRoute");
    	BaseResult<BaseEntity> result =  new BaseResult<BaseEntity>();
        List<CPMSMF01Entity> cpmsmf01EntityList = cpmsmf01Service.getCPMSMF01EntityList(ssmsCustomerCode, vendorRoute);
        if (null == cpmsmf01EntityList || cpmsmf01EntityList.isEmpty()) {
            result.addMessage(new BaseMessage(MessageCodeConst.W1005_001));
        }
        return result;
    }

    /**
     * Download Customer Data in SSMS & ORION PLUS data
     * 
     * @param request request
     * @param response response
     */
    @RequestMapping(value = "/mm/CPMSMF01/download",
        method = RequestMethod.POST)
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response) {

        BaseParam param = this.convertJsonDataForForm(BaseParam.class);

        this.setCommonParam(param, request);

        String fileName = StringUtil.formatMessage(CUSTOMERDATAINSSMSORIONPLUS_XLSX, param.getClientTime());

        this.downloadExcelWithTemplate(fileName, param, request, response);

    }

    /**
     * Download Customer Data in SSMS & ORION PLUS data
     *
     * @param param
     * @param wbTemplate
     * @param wbOutput
     * @see com.chinaplus.core.base.BaseFileController#writeContentToExcel(com.chinaplus.core.bean.BaseParam,
     *      org.apache.poi.ss.usermodel.Workbook, org.apache.poi.xssf.streaming.SXSSFWorkbook)
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

        String ssmsCustomerCode = (String) param.getSwapData().get("ssmsCustomerCode");
        String vendorRoute = (String) param.getSwapData().get("vendorRoute");

        String[] allOfficeCode = cpmsmf01Service.getAllOfficeCode();
        String[] orionPlusNames = CodeCategoryManager.getCodeName(param.getLanguage(),
            CodeMasterCategory.ORION_PLUS_FLAG);
        List<CPMSMF01Entity> cpmsmf01EntityList = cpmsmf01Service.getCPMSMF01EntityList(ssmsCustomerCode, vendorRoute);
        org.apache.poi.ss.usermodel.Sheet sheet = wbTemplate.getSheet(SSMSCUSTOMER);
        int size = 0;
        if (cpmsmf01EntityList != null && cpmsmf01EntityList.size() > 0) {
            size = cpmsmf01EntityList.size();
            Cell[] TemplateCells = getTemplateCells(STYLE, IntDef.INT_NINE, wbTemplate);

            for (int i = 0; i < cpmsmf01EntityList.size(); i++) {
                CPMSMF01Entity entity = cpmsmf01EntityList.get(i);
                if (null == entity) {
                    continue;
                }
                Object[] arrayObj = null;

                arrayObj = new Object[] { StringConst.EMPTY, StringConst.EMPTY, entity.getSsmsCustomerCode(),
                    entity.getSsmsCustomerName(), entity.getVendorRouteSet(), entity.getOfficeCode(),
                    entity.getOrionPlusFlag() };

                cpmsmf01Service.createOneDataRowByTemplate(sheet, IntDef.INT_FIVE + i, TemplateCells, arrayObj,
                    param.getLanguage(), orionPlusNames, allOfficeCode);
            }
        } else {
            size = IntDef.INT_EIGHT;
        }

        // set value from style sheet
        for (int i = IntDef.INT_THREE; i < IntDef.INT_EIGHT; i++) {
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
