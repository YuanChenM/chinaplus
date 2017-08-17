/**
 * CPPSPF11Controller.java
 * 
 * @screen CPPSPF11
 * @author xing_ming
 */
package com.chinaplus.web.vvp.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.CategoryLanguage;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.FileBaseParam;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.vvp.entity.VVPSDF11Entity;
import com.chinaplus.web.vvp.service.VVPSDF11Service;

/**
 * Upload Sample Data File Controller.
 */
@Controller
public class VVPSDF11Controller extends BaseFileController {

    /** Upload Sample Data File Service */
    @Autowired
    private VVPSDF11Service vvpsdf11Service;

    /**
     * Sample data file upload.
     * 
     * @param suppProfile suppProfile
     * @param param parameter
     * @param request request
     * @param response response
     * @throws Exception Exception
     */
    @RequestMapping(value = "/vvp/VVPSDF11/upload",
        method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam(value = "suppProfile",
        required = true) MultipartFile suppProfile, FileBaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        super.setCommonParam(param, request);

        // reader files
        BaseResult<BaseEntity> baseResult = super.uploadFileProcess(suppProfile, FileType.EXCEL, param, request,
            response);

        super.setUploadResult(request, response, baseResult);
    }

    /**
     * Process uploaded excel.
     * 
     * @param file upload file
     * @param workbook uploaded excel
     * @param param the parameters
     * @param <T> the parameter class type
     * @param request HttpServletRequest
     * @return result message
     * @throws Exception the Exception
     */
    @Override
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook,
        T param, HttpServletRequest request) throws Exception {

        // new
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        Sheet sheet = workbook.getSheetAt(0);

        // Do input value check
        VVPSDF11Entity supplierInfo = new VVPSDF11Entity();
        doInputCheck(messageLists, sheet, supplierInfo);

        // Do business logic check
        doLogicCheck(messageLists, supplierInfo, param);

        // Check whether has error
        if (messageLists.size() == 0) {

            // cast
            FileBaseParam cParam = (FileBaseParam) param;

            // UUID
            Integer supplierId = vvpsdf11Service.getNextSequence("SEQ_VVP_SUPPILER");
            supplierInfo.setSupplierId(supplierId);

            // check document 1
            supplierInfo.setDocument1Name(saveFile(cParam.getSupportDoc1(), supplierId, NumberConst.IntDef.INT_ONE));
            // check document 2
            supplierInfo.setDocument2Name(saveFile(cParam.getSupportDoc2(), supplierId, NumberConst.IntDef.INT_TWO));
            // check document 3
            supplierInfo.setDocument3Name(saveFile(cParam.getSupportDoc3(), supplierId, NumberConst.IntDef.INT_THREE));
            // check document 4
            supplierInfo.setDocument4Name(saveFile(cParam.getSupportDoc4(), supplierId, NumberConst.IntDef.INT_FOUR));
            // check document 5
            supplierInfo.setDocument5Name(saveFile(cParam.getSupportDoc5(), supplierId, NumberConst.IntDef.INT_FIVE));

            // Save upload data to DB
            vvpsdf11Service.doVVPSupplierInfo(supplierInfo);
        }

        return messageLists;
    }

    /**
     * Do input value check.
     * 
     * @param messageLists message list
     * @param sheet excel work sheet
     * @param suppInfo upload supplier Information
     */
    private void doInputCheck(List<BaseMessage> messageLists, Sheet sheet, VVPSDF11Entity suppInfo) {

        // define
        int dataRow = IntDef.INT_FOUR;
        int dataCol = IntDef.INT_FOUR;

        // detail information
        // Export Office
        String cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        // do check
        if (StringUtil.isEmpty(cellValue)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W2002_001);
            message.setMessageArgs(new String[] { "Export Office" });
            messageLists.add(message);
        } else {
            // check office is exist or not
            Integer officeId = vvpsdf11Service.getOfficeIdByOfficeCode(cellValue);
            if (officeId == null) {
                // Error Message
                BaseMessage message = new BaseMessage(MessageCodeConst.W2002_002);
                message.setMessageArgs(new String[] { "Export Office" });
                messageLists.add(message);
            } else {
                suppInfo.setExpOffice(officeId);
            }
        }
        // get from database
        // Created By
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setCreateBy(cellValue);
        // Creation Date
        Date dateCellValue = PoiUtil.getDateCellValue(sheet, dataRow++, dataCol);
        suppInfo.setCreateDate(dateCellValue);

        // Basic Supplier's Information
        dataRow += IntDef.INT_THREE;
        // Full Company Name
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        if (StringUtil.isEmpty(cellValue)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W2002_001);
            message.setMessageArgs(new String[] { "Full Company Name" });
            messageLists.add(message);
        } else {
            suppInfo.setCompanyName(cellValue);
        }
        // Local Address
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setLocalAddress(cellValue);
        // Capital
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setCapital(cellValue);
        // Shareholder
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setShareHolder(cellValue);
        // No of Employee
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setNoOfEmployee(cellValue);
        // Head quarter
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        if (!StringUtil.isEmpty(cellValue)) {
            // check office is exist or not
            Integer regionId = vvpsdf11Service.getRegionIdByRegionCode(cellValue);
            if (regionId == null) {
                // Error Message
                BaseMessage message = new BaseMessage(MessageCodeConst.W2002_002);
                message.setMessageArgs(new String[] { "Headquarter Office" });
                messageLists.add(message);
            } else {
                suppInfo.setHeadquarter(regionId);
            }
        }
        // Office Branch
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        if (!StringUtil.isEmpty(cellValue)) {
            // check office is exist or not
            Integer regionId = vvpsdf11Service.getRegionIdByRegionCode(cellValue);
            if (regionId == null) {
                // Error Message
                BaseMessage message = new BaseMessage(MessageCodeConst.W2002_002);
                message.setMessageArgs(new String[] { "Office Branch" });
                messageLists.add(message);
            } else {
                suppInfo.setOfficeBranch(regionId);
            }
        }
        // Business Activity
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setBusinessActivity(cellValue);
        // Company Website
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setCompanyWebsite(cellValue);
        // Main Customer
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setMainCustomer(cellValue);
        // End User/ OEM
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setEndUserOem(cellValue);

        // Product Information
        dataRow += IntDef.INT_THREE;
        // Product Material
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setProductMateial(CodeCategoryManager.getCodeValue(CategoryLanguage.ENGLISH,
            CodeConst.CodeMasterCategory.PRODUCT_MATERIAL, cellValue));
        if (!StringUtil.isEmpty(cellValue) && suppInfo.getProductMateial() == null) {
            // Error Message
            BaseMessage message = new BaseMessage(MessageCodeConst.W2002_002);
            message.setMessageArgs(new String[] { "Product Mateial" });
            messageLists.add(message);
        } 
        // Section of the car
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setSectionOfTheCar(CodeCategoryManager.getCodeValue(CategoryLanguage.ENGLISH,
            CodeConst.CodeMasterCategory.SECTION_OF_CAR, cellValue));
        if (!StringUtil.isEmpty(cellValue) && suppInfo.getSectionOfTheCar() == null) {
            // Error Message
            BaseMessage message = new BaseMessage(MessageCodeConst.W2002_002);
            message.setMessageArgs(new String[] { "Section of the car" });
            messageLists.add(message);
        } 
        // Production Process
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setProductionProcess(CodeCategoryManager.getCodeValue(CategoryLanguage.ENGLISH,
            CodeConst.CodeMasterCategory.PRODUCTION_PROCESS, cellValue));
        if (!StringUtil.isEmpty(cellValue) && suppInfo.getProductionProcess() == null) {
            // Error Message
            BaseMessage message = new BaseMessage(MessageCodeConst.W2002_002);
            message.setMessageArgs(new String[] { "Production Process" });
            messageLists.add(message);
        } 
        // Remarks if any
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setRemarks1(cellValue);

        // Product Information
        dataRow += IntDef.INT_THREE;
        // Current business with local TTC?
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setBusinessWithLocalTTC(CodeCategoryManager.getCodeValue(CategoryLanguage.ENGLISH,
            CodeConst.CodeMasterCategory.BUSINESS_WITH_TTC_FLAG, cellValue));
        if (!StringUtil.isEmpty(cellValue) && suppInfo.getBusinessWithLocalTTC() == null) {
            // Error Message
            BaseMessage message = new BaseMessage(MessageCodeConst.W2002_002);
            message.setMessageArgs(new String[] { "Current business with local TTC?" });
            messageLists.add(message);
        } 
        // NDA Agreement
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setNdaAgreement(CodeCategoryManager.getCodeValue(CategoryLanguage.ENGLISH,
            CodeConst.CodeMasterCategory.NDA_AGREEMENT_FLAG, cellValue));
        if (!StringUtil.isEmpty(cellValue) && suppInfo.getNdaAgreement() == null) {
            // Error Message
            BaseMessage message = new BaseMessage(MessageCodeConst.W2002_002);
            message.setMessageArgs(new String[] { "NDA Agreement" });
            messageLists.add(message);
        } 
        // Suppiler's target
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setSuppilerTarget(cellValue);
        // Appealing point of the Product
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setAppealingPoint(cellValue);

        // Product Information
        dataRow += IntDef.INT_THREE;
        // Overall Evaluation
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setOverallEvaluation(CodeCategoryManager.getCodeValue(CategoryLanguage.ENGLISH,
            CodeConst.CodeMasterCategory.OVERALL_EVALUATION, cellValue));
        if (!StringUtil.isEmpty(cellValue) && suppInfo.getOverallEvaluation() == null) {
            // Error Message
            BaseMessage message = new BaseMessage(MessageCodeConst.W2002_002);
            message.setMessageArgs(new String[] { "Overall Evaluation" });
            messageLists.add(message);
        } 
        // Risk/ Concern if any
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setRiskConcernIfAny(cellValue);
        // Remarks if any
        cellValue = PoiUtil.getStringCellValue(sheet, dataRow++, dataCol);
        suppInfo.setRemarks2(cellValue);
    }

    /**
     * Do check current supplier is exist or not.
     * 
     * @param messageLists message list
     * @param supplierInfo upload supplier information
     * @param param parameter
     */
    private void doLogicCheck(List<BaseMessage> messageLists, VVPSDF11Entity supplierInfo, BaseParam param) {

        // if primary key is exist, check with database
        if (supplierInfo.getExpOffice() != null && !StringUtil.isEmpty(supplierInfo.getCompanyName())) {

            // make parameter
            param.setFilter("expOffice", supplierInfo.getExpOffice());
            param.setFilter("companyName", supplierInfo.getCompanyName());

            // get count form database
            Integer dataCount = vvpsdf11Service.checkPrimaryKeyExist(param);
            if (dataCount != null && dataCount.intValue() > 0) {
                // already exists.
                BaseMessage message = new BaseMessage(MessageCodeConst.W2002_003);
                messageLists.add(message);
            }
        }
    }

    /**
     * save file.
     * 
     * @param file file
     * @param supplierId supplier Id
     * @param docNo docNo
     * 
     * @return saved file name
     */
    private String saveFile(MultipartFile file, Integer supplierId, Integer docNo) {

        // check file
        if (file == null || file.isEmpty()) {
            return null;
        }

        // check path
        String filePath = "/investigate/upload/profile/" + supplierId + "/" + docNo + "/";
        File fileFolder = new File(filePath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }

        try {

            // get file name
            String fullFileName = file.getOriginalFilename();

            // create
            FileOutputStream ops = new FileOutputStream(new File(filePath + fullFileName));
            // copy
            FileCopyUtils.copy(file.getBytes(), ops);

            // if OK, return new file name
            return fullFileName;
        } catch (IOException e) {
            System.out.println("File does not exists.");
        }

        return null;
    }

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {

        return "VVPSDF11";
    }

}
