/**
 * Controller of Customer Data in SSMS & ORION PLUS data Upload
 * 
 * @screen CPMSRF11
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.ShipType;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.common.service.SendEmailService;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.MailUtilForOnline;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.mm.entity.CPMSMF11Entity;
import com.chinaplus.web.mm.service.CPMSMF11Service;

/**
 * Controller of Customer Data in SSMS & ORION PLUS data Upload
 */
@Controller
public class CPMSMF11Controller extends BaseFileController {

    private static final String SSMSCUSTOMER = "SSMSCustomer";

    /** DOWNLOAD_ZIP_FILE_NAME */
    private static final String DOWNLOAD_ZIP_FILE_NAME = "PartsMasterFile_{0}.zip";

    /** PARTS_MASTER_FILE_NAME */
    private static final String PARTS_MASTER_FILE_NAME = "PartsMasterFile_{0}.xlsx";

    /**
     * cpmsmf11Service.
     */
    @Autowired
    private CPMSMF11Service cpmsmf11Service;

    /**
     * SendEmailService.
     */
    @Autowired
    private SendEmailService sendEmailService;

    /**
     * cpmpmf01Controller.
     */
    @Autowired
    private CPMPMF01Controller cpmpmf01Controller;

    @Override
    protected String getFileId() {
        return FileId.CPMSMF11;
    }

    /**
     * V-V Business Shipping Route Master Upload
     * 
     * @param file MultipartFile
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @RequestMapping(value = "/mm/CPMSMF11/upload",
        method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam(value = "fileData",
        required = true) MultipartFile file, BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        this.setCommonParam(param, request);
        BaseResult<BaseEntity> baseResult = uploadFileProcess(file, FileType.EXCEL, param, request, response);
        this.setUploadResult(request, response, baseResult);
    }

    /**
     * Process uploaded excel.
     * 
     * @param file uploaded file
     * @param workbook uploaded excel
     * @param param the parameters
     * @param <T> the parameter class type
     * @param request the HttpServletRequest
     * @return result message
     */
    @Override
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook,
        T param, HttpServletRequest request) {
        UserInfo userInfo = getLoginUser(request);
        Integer userId = userInfo.getUserId();
        // get work sheet sheet
        Sheet sheet = workbook.getSheet(SSMSCUSTOMER);
        if (null == sheet) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_006);
            throw new BusinessException(message);
        }
        readDataXlsx(sheet, param, userId);
        return new ArrayList<BaseMessage>();
    }

    /**
     * read Data Xlsx
     * 
     * @param sheet sheet
     * @param param param
     * @param userId userId
     */
    private void readDataXlsx(Sheet sheet, BaseParam param, Integer userId) {

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();

        List<CPMSMF11Entity> dataList = new ArrayList<CPMSMF11Entity>();

        boolean dataFlag = false;

        // read excel
        int sheetMaxRow = sheet.getLastRowNum() + 1;
        String legend = MessageManager.getMessage("CPMSRF02_Grid_Legend", Language.CHINESE.getLocale());
        int READ_START_COL = IntDef.INT_ONE;
        int READ_TOTAL_COL = IntDef.INT_SIX;

        for (int startRow = IntDef.INT_SIX; startRow <= sheetMaxRow; startRow++) {
            if (!ValidatorUtils.isBlankRow(sheet, startRow, READ_START_COL, READ_TOTAL_COL)) {
                int startCol = IntDef.INT_ONE;
                String type = StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, startRow, startCol));

                if (!ValidatorUtils.requiredValidator(type)) {
                    continue;
                }
                if (legend.equals(type)) {
                    break;
                }
                dataFlag = true;
                String startRowNum = StringUtil.toSafeString(startRow);
                // 2016/07/21 shiyang mod start (NEW|MOD -> NEW)
                // if (!type.equals(ShipType.NEW) && !type.equals(ShipType.MOD)) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_094);
                if (!type.equals(ShipType.NEW)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_183);
                    // 2016/07/21 shiyang mod end
                    message.setMessageArgs(new String[] { startRowNum });
                    messageLists.add(message);
                    continue;
                }

                CPMSMF11Entity entity = new CPMSMF11Entity();
                boolean conFlag = true;
                // set ssmsCustomerCode
                startCol += IntDef.INT_TWO;
                String ssmsCustomerCode = StringUtil
                    .toSafeString(PoiUtil.getStringCellValue(sheet, startRow, startCol));
                if (!ValidatorUtils.requiredValidator(ssmsCustomerCode)) {
                    conFlag = false;
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSMS01_Grid_SSMSCustomerCode" });
                    messageLists.add(message);
                } else {
                    entity.setSsmsCustomerCode(ssmsCustomerCode);
                }

                // get officeCode
                startCol += IntDef.INT_THREE;
                String officeCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);

                // set orionPlusFlag
                startCol += IntDef.INT_ONE;
                String orionPlusFlag = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(orionPlusFlag)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSMF01_Grid_ManageInORIONPLUS" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(orionPlusFlag, IntDef.INT_ONE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSMF01_Grid_ManageInORIONPLUS",
                        StringUtil.toSafeString(IntDef.INT_ONE) });
                    messageLists.add(message);
                } else {
                    entity.setOrionPlusFlag(orionPlusFlag);
                }

                // check officeCode
                String orionPlusFlagY = CodeCategoryManager.getCodeName(param.getLanguage(),
                    CodeMasterCategory.ORION_PLUS_FLAG, IntDef.INT_ONE);
                if (orionPlusFlagY.equals(orionPlusFlag) && !ValidatorUtils.requiredValidator(officeCode)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSMS01_Grid_ImpOfficeCode" });
                    messageLists.add(message);
                } else {
                    entity.setOfficeCode(officeCode);
                }

                if (conFlag) {
                    // set row number
                    entity.setRowNum(startRow);

                    // add data
                    dataList.add(entity);
                }

            } else {
                if (ValidatorUtils.isExcelEnd(sheet, startRow, READ_START_COL, READ_TOTAL_COL)) {
                    break;
                }
            }
        }

        // check data all blank
        if (!dataFlag) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
            messageLists.add(message);
            throw new BusinessException(messageLists);
        }

        if (dataList != null && dataList.size() > 0) {
            // deal data detail
            dealDataDetail(dataList, param, param.getLanguage(), userId, messageLists);
        } else {
            // throw exception
            throw new BusinessException(messageLists);
        }

    }

    /**
     * deal data detail
     * 
     * @param dataList dataList
     * @param param param
     * @param lang lang
     * @param userId userId
     * @param messageLists messageLists
     */
    private void dealDataDetail(List<CPMSMF11Entity> dataList, BaseParam param, Integer lang, Integer userId,
        List<BaseMessage> messageLists) {

        // all DB query ssmsCustomerCode
        Map<String, String> ssmsCustomerCodeMapDB = cpmsmf11Service.getSsmsCustomerCode(dataList);

        // all office
        Map<String, Integer> officeMap = cpmsmf11Service.getAllOffice();

        // codegory ORION_PLUS_FLAG 1 Y
        String orionPlusFlagY = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.ORION_PLUS_FLAG,
            IntDef.INT_ONE);

        //get old customer-office
        Map<String, Integer> customerOfficeMapOld = new HashMap<String, Integer>();
        customerOfficeMapOld = cpmsmf11Service.getCustomerOfficeMap(dataList);
        
        // check all data
        Map<String, Integer> customerOfficeMap = new HashMap<String, Integer>();
        for (CPMSMF11Entity ce : dataList) {
            String rowNum = StringUtil.toSafeString(ce.getRowNum());

            // check ssmsCustomerCode
            String ssmsCustomerCode = ce.getSsmsCustomerCode();
            String valueSsmsCCode = ssmsCustomerCodeMapDB.get(ssmsCustomerCode);
            if (StringUtil.isNullOrEmpty(valueSsmsCCode)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_075);
                message.setMessageArgs(new String[] { rowNum, "CPMSMS01_Grid_SSMSCustomerCode",
                    "CPMSMF01_Grid_SSMSCustomerMaster" });
                messageLists.add(message);
            }

            // check orionPlusFlag
            String orionPlusFlag = ce.getOrionPlusFlag();
            if (!StringUtil.isNullOrEmpty(orionPlusFlag) && !orionPlusFlagY.equals(orionPlusFlag)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_076);
                message.setMessageArgs(new String[] { rowNum, "CPMSMF01_Grid_ManageInORIONPLUS", orionPlusFlagY });
                messageLists.add(message);
            }

            // check office code
            Integer officeIdOld = null;
            if (customerOfficeMapOld.containsKey(ssmsCustomerCode)){
            	officeIdOld = customerOfficeMapOld.get(ssmsCustomerCode);
            }
            if (orionPlusFlagY.equals(orionPlusFlag)) {
                String officeCode = ce.getOfficeCode();
                if (!StringUtil.isEmpty(officeCode)) {
                    Integer officeId = officeMap.get(officeCode);
                    if (officeId == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_075);
                        message.setMessageArgs(new String[] { rowNum, "CPMSMS01_Grid_ImpOfficeCode",
                            "CPMSMF01_Grid_OfficeMaster" });
                        messageLists.add(message);
                    } else if (officeIdOld != null && !officeId.equals(officeIdOld)){
                    	BaseMessage message = new BaseMessage(MessageCodeConst.W1004_186);
                        message.setMessageArgs(new String[] { rowNum, "CPMSMS01_Grid_SSMSCustomerCode",
                            "CPMSMS01_Grid_ImpOfficeCode" });
                        messageLists.add(message);
                    } else {
                        ce.setOfficeId(officeId);
                        customerOfficeMap.put(ssmsCustomerCode, officeId);
                    }
                }
            }

            // set updatedBy
            ce.setUpdatedBy(userId);
        }

        // throw exception
        if (messageLists != null && messageLists.size() > 0) {
            throw new BusinessException(messageLists);
        }

        // deal TNM_EXP_PARTS data
        cpmsmf11Service.doExpPartsData(dataList, ssmsCustomerCodeMapDB, customerOfficeMap, userId);

        // shiyang add start
        // send mail
        if (sendEmailService.getMailSendFlag()) {
            sendMail(dataList, param, lang);
        }
        // shiyang add end
    }

    /**
     * Send mail.<br>
     * 
     * @param dataList input data list
     * @param param param
     * @param lang language
     * @author shiyang
     */
    private void sendMail(List<CPMSMF11Entity> dataList, BaseParam param, Integer lang) {
        ZipOutputStream zos = null;
        File tempFolder = null;
        File zipFile = null;
        try {
            // Get office and ssmsCustomerCode map (key: officeId,officeCode; value: ssmsCustomerCode list)
            Map<String, List<String>> sentOfficeMap = new HashMap<String, List<String>>();
            for (CPMSMF11Entity entity : dataList) {
                int officeId = entity.getOfficeId();
                String officeCode = entity.getOfficeCode();
                String ssmsCustomerCode = entity.getSsmsCustomerCode();
                String key = officeId + StringConst.COMMA + officeCode;
                List<String> list = sentOfficeMap.get(key);
                if (list == null) {
                    List<String> ssmsCustomerCodeList = new ArrayList<String>();
                    ssmsCustomerCodeList.add(ssmsCustomerCode);
                    sentOfficeMap.put(key, ssmsCustomerCodeList);
                } else {
                    if (!list.contains(ssmsCustomerCode)) {
                        list.add(ssmsCustomerCode);
                        sentOfficeMap.put(key, list);
                    }
                }
            }

            // Loop office and ssmsCustomerCode map and send mail
            for (Map.Entry<String, List<String>> entry : sentOfficeMap.entrySet()) {
                String[] officeInfo = entry.getKey().split(StringConst.COMMA);
                int officeId = Integer.parseInt(officeInfo[0]);
                String officeCode = officeInfo[1];
                List<String> ssmsCustomerCodeList = entry.getValue();

                // Get mail to address
                List<TnmUser> userList = cpmsmf11Service.getSendMailObject(officeId);
                StringBuffer sbTo = new StringBuffer();
                for (TnmUser user : userList) {
                    String mailAddr = user.getMailAddr();
                    if (!StringUtils.isBlank(mailAddr)) {
                        if (sbTo.length() != 0) {
                            sbTo.append(StringConst.COMMA);
                        }
                        sbTo.append(user.getMailAddr());
                    }
                }
                String to = sbTo.toString();

                // Get mail title
                String title = MessageManager.getMessage("CPMSMF01_Grid_MailTitle_NotRegisteredParts",
                    Language.CHINESE.getLocale());
                String mailBodySsmsCustomerCode = MessageManager.getMessage("CPMSMF01_Grid_MailBody_SsmsCustomerCode",
                    Language.CHINESE.getLocale());
                if (lang.intValue() == CodeConst.CategoryLanguage.ENGLISH) {
                    title = MessageManager.getMessage("CPMSMF01_Grid_MailTitle_NotRegisteredParts",
                        Language.ENGLISH.getLocale());
                    mailBodySsmsCustomerCode = MessageManager.getMessage("CPMSMF01_Grid_MailBody_SsmsCustomerCode",
                        Language.ENGLISH.getLocale());
                }
                Timestamp tDate = cpmsmf11Service.getDBDateTime(param.getOfficeTimezone());
                String date = DateTimeUtil.formatDate(tDate);
                title = title.replaceAll("\\{0\\}", date);

                // Get mail body
                String templatePath = getTextTemplateFilePath(MailUtilForOnline.MAIL_TEMPLATE_NOT_REGISTERED_PARTS);
                String body = MailUtilForOnline.getTemplate(templatePath);
                body = body.replaceAll("\\{0\\}", officeCode);
                StringBuffer sbSsmsCustomerCode = new StringBuffer();
                sbSsmsCustomerCode.append("<table border='1' style='border-collapse:collapse;text-align:center'>");
                sbSsmsCustomerCode.append("<tr style='background-color:#FFFF00'><td>");
                sbSsmsCustomerCode.append(mailBodySsmsCustomerCode);
                sbSsmsCustomerCode.append("</td></tr>");
                for (String ssmsCustomerCode : ssmsCustomerCodeList) {
                    sbSsmsCustomerCode.append("<tr><td>");
                    sbSsmsCustomerCode.append(ssmsCustomerCode);
                    sbSsmsCustomerCode.append("</td></tr>");
                }
                sbSsmsCustomerCode.append("</table>");
                body = body.replaceAll("\\{1\\}", sbSsmsCustomerCode.toString());

                // create temp folder
                String tempFolderPath = ConfigUtil.get(Properties.TEMPORARY_PATH) + UUID.randomUUID().toString();
                tempFolder = new File(tempFolderPath);
                if (!tempFolder.exists()) {
                    tempFolder.mkdirs();
                }
                // create zip file for mail
                String clientTime = param.getClientTime();
                String zipName = StringUtil.formatMessage(DOWNLOAD_ZIP_FILE_NAME, clientTime);
                String excelName = StringUtil.formatMessage(PARTS_MASTER_FILE_NAME, clientTime);
                zipFile = new File(tempFolderPath + File.separator + zipName);
                zos = new ZipOutputStream(new FileOutputStream(zipFile));
                // download CPMPMF01 to zip file
                param.setSwapData("expCustCodes", ssmsCustomerCodeList);
                param.setSwapData("inactiveFlag", false);
                param.setSwapData("includingNotReqParts", false);
                param.setSwapData("blankFormatDownP", false);
                param.setSwapData("onlyDownNoRegParts", true);
                super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), new String[] { excelName },
                    new String[] { FileId.CPMPMF01 }, param, zos);
                zos.close();
                // set zip file to mail attachment
                Vector<File> files = new Vector<File>();
                files.add(zipFile);

                // Send mail
                MailUtilForOnline mailForOnline = new MailUtilForOnline(to, title, body, files, true, tempFolder);
                mailForOnline.run();

                // // delete temp folder
                // zipFile.delete();
                // tempFolder.delete();
            }
        } catch (Exception e) {} finally {
            try {
                if (zos != null) {
                    try {
                        zos.close();
                    } catch (Exception ex) {
                        // logger.warn("OutputStream close fail.", ex);
                    }
                }
                // if (zipFile != null && zipFile.exists()) {
                // zipFile.delete();
                // }
                // if (tempFolder != null && tempFolder.exists()) {
                // tempFolder.delete();
                // }
            } catch (Exception e) {}
        }
    }

    /**
     * Write content to excel.
     * 
     * @param param page parameter
     * @param wbTemplate the template excel
     * @param wbOutput the output excel
     * @param fileId fileId
     * @author shiyang
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput,
        String fileId) {
        cpmpmf01Controller.writeContentToExcel(param, wbTemplate, wbOutput);
    }
}
