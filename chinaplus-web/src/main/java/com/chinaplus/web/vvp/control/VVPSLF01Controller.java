/**
 * Stock Status Report download controller
 * 
 * @screen VVPSLF01
 * @author liu_yinchuan
 */
package com.chinaplus.web.vvp.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
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
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.vvp.entity.VVPSDF01Entity;
import com.chinaplus.web.vvp.service.VVPSDF01Service;

/**
 * Stock Status Report download
 */
@Controller
public class VVPSLF01Controller extends BaseFileController {

    /** DOWNLOAD_NAME */
    private static final String DOWNLOAD_NAME = "Full Suppiler List_{0}.xlsx";

    /** SHEET NAME */
    private static final String SHEET_NAME = "Full Suppiler List";

    /** SHEET_STYLE */
    private static final String SHEET_STYLE = "style";

    /** Row start */
    private static final int ROW_START = IntDef.INT_FIVE;
    
    private int dataRows = IntDef.INT_ZERO;

    @Autowired
    private VVPSDF01Service service;

    @Override
    protected String getFileId() {
        return FileId.VVPSLF01;
    }

    /**
     * Stock Status Report download check.
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     */
    @RequestMapping(value = "/vvp/VVPSLF01/downloadcheck",
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
    @RequestMapping(value = "/vvp/VVPSLF01/download",
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
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

		List<Integer> supplierId =  (List<Integer>) param.getSwapData().get("supplierIds");
    	if (supplierId != null && supplierId.size() > 0){
    		param.setFilters(null);
    	}
        // get supplier list
		List<VVPSDF01Entity> supplierList = (List<VVPSDF01Entity>) service.getAllSupplierList(param);
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
        if ( supplierList != null && supplierList.size() > 0 ) {
        	dataRows = supplierList.size();
        	Cell[] templateCells = getTemplateCells(SHEET_STYLE, IntDef.INT_FOUR, wbTemplate);
        	for ( int i = 0; i < supplierList.size(); i++ ) {
        		
                VVPSDF01Entity supplier = supplierList.get(i);
                
                Object[] dataArray = new Object[] {
                		expOfficeMap.get(supplier.getExportOffice().toString()),
                		supplier.getCreateBy(),
                		supplier.getFullCompanyName(),
                		supplier.getLocalAddress(),
                		supplier.getCapital(),
                		supplier.getShareHolder(),
                		supplier.getEmployeeNo(),
                		regionMap.get(supplier.getHeadquarter().toString()),
                		regionMap.get(supplier.getOfficeBranch().toString()),
                		supplier.getBusinessActivity(),
                		supplier.getCompanyWebsite(),
                		supplier.getMainCustomer(),
                		supplier.getEndUserOem(),
                		codeMaps.get(CodeConst.CodeMasterCategory.PRODUCT_MATERIAL).get(supplier.getProductMateial()),
                		codeMaps.get(CodeConst.CodeMasterCategory.SECTION_OF_CAR).get(supplier.getSectionofthecar()),
                		codeMaps.get(CodeConst.CodeMasterCategory.PRODUCTION_PROCESS).get(supplier.getProductionProcess()),
                		supplier.getRemask1(),
                		codeMaps.get(CodeConst.CodeMasterCategory.BUSINESS_WITH_TTC_FLAG).get(supplier.getWithTTC()),
                		codeMaps.get(CodeConst.CodeMasterCategory.NDA_AGREEMENT_FLAG).get(supplier.getNdaAgreement()),
                		supplier.getSupplierTarget(),
                		supplier.getAppealingPoint(),
                		codeMaps.get(CodeConst.CodeMasterCategory.OVERALL_EVALUATION).get(supplier.getOverallEvaluation()),
                		supplier.getRiskConcern(),
                		supplier.getRemask2()
                    };
                super.createOneDataRowByTemplate(sheet, ROW_START + i - IntDef.INT_ONE, templateCells, dataArray);
        	}
        }

        // set exp office select
        setValidation(sheet, IntDef.INT_ONE, expOffices);

        // set Head quarter Office select
        setValidation(sheet, IntDef.INT_EIGHT, regions);

        // set Office Branch select
        setValidation(sheet, IntDef.INT_NINE, regions);

        // set Product Material select
        List<String> mateials = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.PRODUCT_MATERIAL).values());
        setValidation(sheet, IntDef.INT_FOURTEEN, mateials);

        // set Section of the car select
        List<String> carSections = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.SECTION_OF_CAR).values());
        setValidation(sheet, IntDef.INT_FIFTEEN, carSections);

        // set Production Process select
        List<String> process = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.PRODUCTION_PROCESS).values());
        setValidation(sheet, IntDef.INT_SIXTEEN, process);

        // set with TTC select
        List<String> withTTC = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.BUSINESS_WITH_TTC_FLAG).values());
        setValidation(sheet, IntDef.INT_EIGHTEEN, withTTC);

        // set NDA Agreement select
        List<String> ndas = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.NDA_AGREEMENT_FLAG).values());
        setValidation(sheet, IntDef.INT_NINETEEN, ndas);

        // set Overall Evaluation select
        List<String> overAlls = new ArrayList<String>(codeMaps.get(CodeConst.CodeMasterCategory.OVERALL_EVALUATION).values());
        setValidation(sheet, IntDef.INT_TWENTY_TWO, overAlls);

        // Remove style sheet
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(SHEET_STYLE));

    }

    public void setValidation(Sheet sheet, int colIndex, List<String> arrays) {

        DataValidationHelper dvHelper = null;
        DataValidationConstraint dvConstraint = null;
        DataValidation validation = null;
        CellRangeAddressList addressList = null;

        // get change Cancel list data formula
        dvHelper = sheet.getDataValidationHelper();
        dvConstraint = dvHelper.createExplicitListConstraint(arrays.toArray(new String[arrays.size()]));

        // set Reason For Fluctuation
        addressList = new CellRangeAddressList(ROW_START - IntDef.INT_ONE, ROW_START + dataRows - IntDef.INT_ONE, colIndex - IntDef.INT_ONE, colIndex - IntDef.INT_ONE);
        validation = dvHelper.createValidation(dvConstraint, addressList);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }
}
