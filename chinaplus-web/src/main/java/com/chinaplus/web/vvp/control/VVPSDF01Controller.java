/**
 * Stock Status Report download controller
 * 
 * @screen VVPSDF01
 * @author liu_yinchuan
 */
package com.chinaplus.web.vvp.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.vvp.entity.VVPSDF01Entity;
import com.chinaplus.web.vvp.service.VVPSDF01Service;

/**
 * Stock Status Report download
 */
@Controller
public class VVPSDF01Controller extends BaseFileController {

    /** DOWNLOAD_NAME */
    private static final String DOWNLOAD_NAME = "Supplier Form_{0}.xlsx";

    /** SHEET NAME */
    private static final String SHEET_NAME = "Supplier Profile";
    
    private static int COLUMN_START = IntDef.INT_FOUR;

    @Autowired
    private VVPSDF01Service service;

    @Override
    protected String getFileId() {
        return FileId.VVPSDF01;
    }

    /**
     * Stock Status Report download check.
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     */
    @RequestMapping(value = "/vvp/VVPSDF01/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {

        return new BaseResult<BaseEntity>();
    }

    /**
     * Do download.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @RequestMapping(value = "/vvp/VVPSDF01/download",
        method = RequestMethod.POST)
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // set common parameters by session
        BaseParam param = this.convertJsonDataForForm(BaseParam.class);

        // set common information
        this.setCommonParam(param, request);

        // get file name
        String fileName = StringUtil.formatMessage(DOWNLOAD_NAME, param.getClientTime());

        // do excel with template
        this.downloadExcelWithTemplate(fileName, param, request, response);
    }

    /**
     * Write content to excel.
     *
     * @param param parameter
     * @param wbTemplate workbook template
     * @param wbOutput workbook out put
     * @param fieldId field Id
     * @see com.chinaplus.core.base.BaseFileController#writeContentToExcel(com.chinaplus.core.bean.BaseParam,
     *      org.apache.poi.ss.usermodel.Workbook, org.apache.poi.xssf.streaming.SXSSFWorkbook, java.lang.String)
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

    	Integer supplierId = (Integer) param.getSwapData().get("supplierId");
        // get supplier list
        VVPSDF01Entity supplier = null;
        if (supplierId != null) {
        	supplier = service.getSupplierById(param);
		}
		// Get excel template cells
        // String styleSheetName = DownloadConst.STYLE_SHEET_NAME;
        Sheet sheet = wbTemplate.getSheet(SHEET_NAME);
        // Cell[] templateCells = super.getTemplateCells(styleSheetName, DETAIL_START_LINE, wbTemplate);

        Map<String, Map<Integer, String>> codeMaps = CodeCategoryManager.getCodeCategaryByLang(param.getLanguage());
        
        List<ComboData> offices = service.getOffices();
        
        List<ComboData> comboRegions = service.getRegionCode();
        
        List<String> expOffices = new ArrayList<String>();
        Map<String, String> expOfficeMap = new HashMap<String, String>();
        for (ComboData data : offices){
        	expOffices.add(data.getText());
        	expOfficeMap.put(data.getId(), data.getText());
        }
        List<String> regions = new ArrayList<String>();
        Map<String, String> regionMap = new HashMap<String, String>();
        for (ComboData data : comboRegions){
        	regions.add(data.getText());
        	regionMap.put(data.getId(), data.getText());
        }
        
        // Output download data to excel file
        if ( supplier != null ) {
            PoiUtil.setCellValue(sheet, IntDef.INT_FOUR, COLUMN_START, expOfficeMap.get(supplier.getExportOffice().toString()));
            PoiUtil.setCellValue(sheet, IntDef.INT_FIVE, COLUMN_START, supplier.getCreateBy());
            PoiUtil.setCellValue(sheet, IntDef.INT_SIX, COLUMN_START, supplier.getCreatedDate());
            PoiUtil.setCellValue(sheet, IntDef.INT_TEN, COLUMN_START, supplier.getFullCompanyName());
            PoiUtil.setCellValue(sheet, IntDef.INT_ELEVEN, COLUMN_START, supplier.getLocalAddress());
            PoiUtil.setCellValue(sheet, IntDef.INT_TWELVE, COLUMN_START, supplier.getCapital());
            PoiUtil.setCellValue(sheet, IntDef.INT_THIRTEEN, COLUMN_START, supplier.getShareHolder());
            PoiUtil.setCellValue(sheet, IntDef.INT_FOURTEEN, COLUMN_START, supplier.getEmployeeNo());
            PoiUtil.setCellValue(sheet, IntDef.INT_FIFTEEN, COLUMN_START, regionMap.get(supplier.getHeadquarter().toString()));
            PoiUtil.setCellValue(sheet, IntDef.INT_SIXTEEN, COLUMN_START, regionMap.get(supplier.getOfficeBranch().toString()));
            PoiUtil.setCellValue(sheet, IntDef.INT_SEVENTEEN, COLUMN_START, supplier.getBusinessActivity());
            PoiUtil.setCellValue(sheet, IntDef.INT_EIGHTEEN, COLUMN_START, supplier.getCompanyWebsite());
            PoiUtil.setCellValue(sheet, IntDef.INT_NINETEEN, COLUMN_START, supplier.getMainCustomer());
            PoiUtil.setCellValue(sheet, IntDef.INT_TWENTY, COLUMN_START, supplier.getEndUserOem());
            PoiUtil.setCellValue(sheet, IntDef.INT_TWENTY_FOUR, COLUMN_START, codeMaps.get(CodeConst.CodeMasterCategory.PRODUCT_MATERIAL).get(supplier.getProductMateial()));
            PoiUtil.setCellValue(sheet, IntDef.INT_TWENTY_FIVE, COLUMN_START, codeMaps.get(CodeConst.CodeMasterCategory.SECTION_OF_CAR).get(supplier.getSectionofthecar()));
            PoiUtil.setCellValue(sheet, IntDef.INT_TWENTY_SIX, COLUMN_START, codeMaps.get(CodeConst.CodeMasterCategory.PRODUCTION_PROCESS).get(supplier.getProductionProcess()));
            PoiUtil.setCellValue(sheet, IntDef.INT_TWENTY_SEVEN, COLUMN_START, supplier.getRemask1());
            PoiUtil.setCellValue(sheet, IntDef.INT_THIRTY_ONE, COLUMN_START, codeMaps.get(CodeConst.CodeMasterCategory.BUSINESS_WITH_TTC_FLAG).get(supplier.getWithTTC()));
            PoiUtil.setCellValue(sheet, IntDef.INT_THIRTY_TWO, COLUMN_START, codeMaps.get(CodeConst.CodeMasterCategory.NDA_AGREEMENT_FLAG).get(supplier.getNdaAgreement()));
            PoiUtil.setCellValue(sheet, IntDef.INT_THIRTY_THREE, COLUMN_START, supplier.getSupplierTarget());
            PoiUtil.setCellValue(sheet, IntDef.INT_THIRTY_FOUR, COLUMN_START, supplier.getAppealingPoint());
            PoiUtil.setCellValue(sheet, IntDef.INT_THIRTY_EIGTH, COLUMN_START, codeMaps.get(CodeConst.CodeMasterCategory.OVERALL_EVALUATION).get(supplier.getOverallEvaluation()));
            PoiUtil.setCellValue(sheet, IntDef.INT_THIRTY_NINE, COLUMN_START, supplier.getRiskConcern());
            PoiUtil.setCellValue(sheet, IntDef.INT_FOURTY, COLUMN_START, supplier.getRemask2());
        }

        // set exp office select
        setValidation(sheet, IntDef.INT_FOUR, expOffices);

        // set Head quarter Office select
        setValidation(sheet, IntDef.INT_FIFTEEN, regions);

        // set Office Branch select
        setValidation(sheet, IntDef.INT_SIXTEEN, regions);

        // set Product Material select
        List<String> mateials = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.PRODUCT_MATERIAL).values());
        setValidation(sheet, IntDef.INT_TWENTY_FOUR, mateials);

        // set Section of the car select
        List<String> carSections = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.SECTION_OF_CAR).values());
        setValidation(sheet, IntDef.INT_TWENTY_FIVE, carSections);

        // set Production Process select
        List<String> process = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.PRODUCTION_PROCESS).values());
        setValidation(sheet, IntDef.INT_TWENTY_SIX, process);

        // set with TTC select
        List<String> withTTC = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.BUSINESS_WITH_TTC_FLAG).values());
        setValidation(sheet, IntDef.INT_THIRTY_ONE, withTTC);

        // set NDA Agreement select
        List<String> ndas = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.NDA_AGREEMENT_FLAG).values());
        setValidation(sheet, IntDef.INT_THIRTY_TWO, ndas);

        // set Overall Evaluation select
        List<String> overAlls = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.OVERALL_EVALUATION).values());
        setValidation(sheet, IntDef.INT_THIRTY_EIGTH, overAlls);

        // Remove style sheet
        // wbOutput.removeSheetAt(wbOutput.getSheetIndex(DownloadConst.STYLE_SHEET_NAME));

    }

    public void setValidation(Sheet sheet, int row, List<String> arrays) {

        DataValidationHelper dvHelper = null;
        DataValidationConstraint dvConstraint = null;
        DataValidation validation = null;
        CellRangeAddressList addressList = null;

        // get change Cancel list data formula
        dvHelper = sheet.getDataValidationHelper();
        dvConstraint = dvHelper.createExplicitListConstraint(arrays.toArray(new String[arrays.size()]));

        // set Reason For Fluctuation
        addressList = new CellRangeAddressList(row - IntDef.INT_ONE, row - IntDef.INT_ONE, COLUMN_START - IntDef.INT_ONE, COLUMN_START - IntDef.INT_ONE);
        validation = dvHelper.createValidation(dvConstraint, addressList);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }
}
