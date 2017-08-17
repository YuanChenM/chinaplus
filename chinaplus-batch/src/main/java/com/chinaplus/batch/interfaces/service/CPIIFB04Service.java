/**
 * CPIIFB04Service.java
 * 
 * @screen CPIIFB04
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.IFServiceId;
import com.chinaplus.batch.common.consts.BatchConst.ValidFlag;
import com.chinaplus.batch.interfaces.bean.CPIIFB01Param;
import com.chinaplus.batch.interfaces.bean.Parts;
import com.chinaplus.batch.interfaces.bean.PartsMasterEntity;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.HandleFlag;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.common.service.SendEmailService;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.exception.DownloadException;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.ExcelUtil;
import com.chinaplus.core.util.MailUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * CPIIFB04Service.
 * ssms parts logic
 * 
 * @author yang_jia1
 */
@Service
@Component(IFServiceId.SMSS_PARTS)
public class CPIIFB04Service extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB04Service.class);

    /** DOWNLOAD_ZIP_FILE_NAME */
    private static final String DOWNLOAD_ZIP_FILE_NAME = "PartsMasterFile_{0}.zip";

    /** PARTS_MASTER_FILE_NAME */
    private static final String PARTS_MASTER_FILE_NAME = "PartsMasterFile_{0}.xlsx";

    private static final String STYLE = "style";

    private static final String VV_PARTS_MASTER = "V-V_Parts_Master";

    private static final String AISIN_PARTS_MASTER = "AISIN_Parts_Master";

    /**
     * SendEmailService.
     */
    @Autowired
    private SendEmailService sendEmailService;

    /**
     * doPartsLogic
     * 
     * @param param param
     * @return excuteResult
     */
    public boolean doPartsLogic(CPIIFB01Param param) {
        logger.debug("---------------batch doPartsLogic start-------------");

        logger.debug("doPartsLogic : update table TNM_IF_PARTS");
        Parts partsParam = new Parts();
        if (param.getIfDateTime() != null) {
            partsParam.setIfDateTime(param.getIfDateTime());
        }

        partsParam.setVaildFlag(ValidFlag.SYSTEM_DATA);
        this.baseMapper.update(this.getSqlId("modPartsIfValidFlag"), partsParam);

        logger.debug("doPartsLogic : insert table TNM_EXP_PARTS");

        List<Integer> handleFlagParam = new ArrayList<Integer>();
        handleFlagParam.add(HandleFlag.UNPROCESS);
        partsParam.setHandleFlagParam(handleFlagParam);
        partsParam.setExpPartsIdList(param.getExpPartsId());

        List<Parts> ifList = this.doQeuryPartsIf(partsParam);
        List<Parts> mailList = new ArrayList<Parts>();

        if (ifList != null && ifList.size() > 0) {
            for (int i = 0; i < ifList.size(); i++) {
                Parts parts = ifList.get(i);
                int result = 0;
                boolean hasError = false;

                // inactiveFlag
                if (parts.getBuildOutFlag() != null && "ZG".equals(parts.getBuildOutFlag())) {
                    parts.setInactiveFlag(InactiveFlag.INACTIVE);
                    parts.setBuildOutFlagInt(InactiveFlag.INACTIVE);
                } else {
                    parts.setInactiveFlag(InactiveFlag.ACTIVE);
                    parts.setBuildOutFlagInt(InactiveFlag.ACTIVE);
                }

                // expPartsId
                Parts existParts = this.doQeuryPartsIfExist(parts);

                if (existParts != null && existParts.getExpPartsId() != null) {
                    parts.setExpPartsId(existParts.getExpPartsId());
                    parts.setUpdatedBy(BatchConst.BATCH_USER_ID);
                    result = this.doUpdatePartsSSMS(parts);
                    if (result <= 0) {
                        hasError = true;
                    }
                } else {
                    Integer expPartsId = this.baseMapper.getNextSequence("SEQ_TNM_EXP_PARTS");
                    parts.setExpPartsId(expPartsId);
                    result = this.doInsertPartsSSMS(parts);
                    mailList.add(parts);
                    if (result <= 0) {
                        hasError = true;
                    }
                }

                if (existParts != null && existParts.getPartsId() != null) {
                    // parts_master. Select parts info from TNM_PARTS_MASTER table by PARTS_ID.
                    Parts partsMaster = this.doQeuryPartsMaster(existParts);

                    // generate param inactiveFlag and expPartsId.
                    List<Parts> expPartsList = new ArrayList<Parts>();

                    // Select newest info of TNM_EXP_PARTS by PARTS_ID, and return list.
                    expPartsList = this.doQeuryInacticeFlag(existParts);

                    // update inactiveFlag
                    if (partsMaster != null && expPartsList != null) {
                        Integer inactiveFlag = 1;
                        for (Parts parts2 : expPartsList) {
                            if (parts2.getInactiveFlag() != null && parts2.getInactiveFlag() == 0) {
                                inactiveFlag = 0;
                                break;
                            }
                        }

                        parts.setInactiveFlag(inactiveFlag);
                        parts.setUpdatedBy(BatchConst.BATCH_USER_ID);
                        parts.setPartsId(partsMaster.getPartsId());
                        result = this.doUpdatePartsMaster(parts);
                        if (result <= 0) {
                            hasError = true;
                        }
                    }
                }

                if (hasError) {
                    // update handle flag
                    parts.setHandleFlag(HandleFlag.PROCESS_FAILURE);
                } else {
                    // update handle flag
                    parts.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
                }

                this.doUpdateHandleFlag(parts);
            }

            // send mail
            if (mailList.size() > 0 && sendEmailService.getMailSendFlag()) {
                sendMail(mailList);
            }
        }

        logger.debug("---------------batch doPartsLogic end-------------");
        return true;
    }

    /**
     * Send mail.<br>
     * 
     * @param dataList List<Parts> dataList
     */
    private void sendMail(List<Parts> dataList) {
        ZipOutputStream zos = null;
        File tempFolder = null;
        File zipFile = null;
        try {
            // Get office and ssmsCustomerCode map (key: officeId,officeCode; value: ssmsCustomerCode list)
            Map<String, List<String>> sentOfficeMap = new HashMap<String, List<String>>();
            for (Parts entity : dataList) {
                int officeId = entity.getOfficeId();
                String officeCode = entity.getOfficeCode();
                String ssmsCustomerCode = entity.getCustomerCode();
                String timeZone = entity.getTimeZone();
                String key = new StringBuffer().append(officeId).append(StringConst.COMMA).append(officeCode)
                    .append(StringConst.COMMA).append(timeZone).toString();
                List<String> list = sentOfficeMap.get(key);
                if (list == null) {
                    List<String> ssmsCustomerCodeList = new ArrayList<String>();
                    ssmsCustomerCodeList.add(ssmsCustomerCode);
                    sentOfficeMap.put(key, ssmsCustomerCodeList);
                } else {
                    list.add(ssmsCustomerCode);
                    sentOfficeMap.put(key, list);
                }
            }

            // Loop office and ssmsCustomerCode map and send mail
            for (Map.Entry<String, List<String>> entry : sentOfficeMap.entrySet()) {
                String[] officeInfo = entry.getKey().split(StringConst.COMMA);
                int officeId = Integer.parseInt(officeInfo[IntDef.INT_ZERO]);
                String officeCode = officeInfo[IntDef.INT_ONE];
                String timeZone = officeInfo[IntDef.INT_TWO];
                List<String> ssmsCustomerCodeList = entry.getValue();
                Set<String> ssmsCustomerCodeSet = new HashSet<String>();
                ssmsCustomerCodeSet.addAll(ssmsCustomerCodeList);

                // Get mail to address
                BaseParam paramForUser = new BaseParam();
                paramForUser.setSwapData("officeId", officeId);
                List<TnmUser> userList = baseMapper.select(this.getSqlId("getSendMailObject"), paramForUser);

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
                String title = MessageManager.getMessage("CPIIFB01_Batch_MailTitle_NotRegisteredParts",
                    Language.CHINESE.getLocale());
                String mailBodySsmsCustomerCode = MessageManager.getMessage("CPIIFB01_Batch_MailBody_SsmsCustomerCode",
                    Language.CHINESE.getLocale());
                Timestamp tDate = this.getDBDateTime(timeZone);
                String date = DateTimeUtil.formatDate(tDate);
                title = title.replaceAll("\\{0\\}", date);

                // Get mail body
                String templatePath = StringUtil.formatMessage("{0}/{1}.txt", "/templates-batch",
                    MailUtil.MAIL_TEMPLATE_NOT_REGISTERED_PARTS);
                String body = MailUtil.getTemplate(templatePath);
                body = body.replaceAll("\\{0\\}", officeCode);
                StringBuffer sbSsmsCustomerCode = new StringBuffer();
                sbSsmsCustomerCode.append("<table border='1' style='border-collapse:collapse;text-align:center'>");
                sbSsmsCustomerCode.append("<tr style='background-color:#FFFF00'><td>");
                sbSsmsCustomerCode.append(mailBodySsmsCustomerCode);
                sbSsmsCustomerCode.append("</td></tr>");
                for (String ssmsCustomerCode : ssmsCustomerCodeSet) {
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
                BaseParam param = new BaseParam();
                String clientTime = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD_HHMMSS, tDate);
                String zipName = StringUtil.formatMessage(DOWNLOAD_ZIP_FILE_NAME, clientTime);
                String excelName = StringUtil.formatMessage(PARTS_MASTER_FILE_NAME, clientTime);
                zipFile = new File(tempFolderPath + File.separator + zipName);
                zos = new ZipOutputStream(new FileOutputStream(zipFile));
                // download PartsMaster to zip file
                param.setSwapData("dataLists", dataList);

                makeZipExcelWithMultiTemplate(tempFolder.getPath(), new String[] { excelName },
                    new String[] { FileId.CPIIFB01 }, param, zos);
                zos.close();
                // set zip file to mail attachment
                Vector<File> files = new Vector<File>();
                files.add(zipFile);

                // Send mail
                MailUtil mail = new MailUtil();
                mail.sendMailForBatch(to, title, body, files, true, tempFolder);
            }
        } catch (Exception e) {
            logger.error("get mail template failed!");
            e.getStackTrace();
        } finally {
            try {
                if (zos != null) {
                    try {
                        zos.close();
                    } catch (IOException ex) {
                        logger.warn("OutputStream close fail.", ex);
                    }
                }
            } catch (Exception e) {
                logger.error("close ZipOutputStream failed!");
                e.getStackTrace();
            }
        }
    }

    /**
     * Read the excel template file, then create download file and download it.
     * 
     * @param tempFolder temporary file path
     * @param fileNames the file name for client
     * @param fileIds the file id of template
     * @param param the parameter
     * @param zos zip output stream
     * @param <T> the parameter class type
     */
    private <T extends BaseParam> void makeZipExcelWithMultiTemplate(String tempFolder, String[] fileNames,
        String[] fileIds, T param, ZipOutputStream zos) {
        byte[] buffer = new byte[NumberConst.IntDef.CUSTOMER_AUTO_GROW_COLLECTION_LIMIT];
        int length = 0;

        OutputStream os = null;
        BufferedInputStream bis = null;

        Workbook wbTemplate = null;
        SXSSFWorkbook wbOutput = null;
        try {

            for (int i = 0; i < fileNames.length; i++) {
                // get workbook by file id
                wbTemplate = ExcelUtil.getWorkBook(StringUtil.formatMessage("{0}/{1}.xlsx", "/templates-batch",
                    fileIds[i]));
                // set dynamic part
                writeDynamicTemplate(param, wbTemplate, fileIds[i]);

                // write content to excel file
                wbOutput = new SXSSFWorkbook((XSSFWorkbook) wbTemplate, -1);
                writeContentToExcel(param, wbTemplate, wbOutput);

                // replace i18n items
                // PoiUtil.replaceI18nMessage(wbTemplate, fileIds[i]);

                // output excel to response
                String downloadFileName = fileNames[i];

                // Create temporary file (download object file)
                File tempFile = new File(tempFolder, downloadFileName);

                // Write download to temporary file
                os = new FileOutputStream(tempFile);
                wbOutput.write(os);
                os.close();

                // Compress temporary file into ZIP file
                ZipEntry ze = new ZipEntry(downloadFileName);
                zos.putNextEntry(ze);
                bis = new BufferedInputStream(new FileInputStream(tempFile));
                while ((length = bis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                bis.close();

                // Delete temporary file
                tempFile.delete();
            }

        } catch (Exception e) {
            // logger.debug("Download excel fail.", e);
            throw new BusinessException("Download excel fail.", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    logger.warn("OutputStream close fail.", ex);
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {
                    logger.warn("BufferedInputStream close fail.", ex);
                }
            }
            if (wbTemplate != null) {
                try {
                    wbTemplate.close();
                } catch (IOException ex) {
                    logger.warn("Workboox close fail.", ex);
                }
            }
            if (wbOutput != null) {
                try {
                    wbOutput.close();
                } catch (IOException ex) {
                    logger.warn("Workboox close fail.", ex);
                }
            }
        }
    }

    /**
     * Get the excel template file path by file id.
     * 
     * @param workbook the template excel
     * @param param the parameter
     * @param <T> the parameter class type
     * @param fileId fileId
     */
    private <T extends BaseParam> void writeDynamicTemplate(T param, Workbook workbook, String fileId) {}

    /**
     * Parts Master Download
     *
     * @param <T> BaseParam
     * @param param T
     * @param wbTemplate Workbook
     * @param wbOutput SXSSFWorkbook
     * @see com.chinaplus.core.base.BaseFileController#writeContentToExcel(com.chinaplus.core.bean.BaseParam,
     *      org.apache.poi.ss.usermodel.Workbook, org.apache.poi.xssf.streaming.SXSSFWorkbook)
     */
    @SuppressWarnings("unchecked")
    private <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

        List<PartsMasterEntity> vvdataList = new ArrayList<PartsMasterEntity>();

        List<Parts> dataLists = (List<Parts>) param.getSwapData().get("dataLists");
        vvdataList = this.getPartsMasterEntityList(dataLists, BusinessPattern.V_V);

        Map<Integer, String> maps = new HashMap<Integer, String>();

        maps.put(IntDef.INT_NINE, "J");
        maps.put(IntDef.INT_TEN, "K");
        maps.put(IntDef.INT_ELEVEN, "L");
        maps.put(IntDef.INT_TWENTY_THREE, "X");
        maps.put(IntDef.INT_TWENTY_FOUR, "Y");
        maps.put(IntDef.INT_THIRTY_THREE, "AH");
        maps.put(IntDef.INT_THIRTY_SIX, "AK");
        maps.put(IntDef.INT_THIRTY_SEVEN, "AL");
        maps.put(IntDef.INT_FIFTY, "AY");
        maps.put(IntDef.INT_FIFTY_ONE, "AZ");
        maps.put(IntDef.INT_FIFTY_TWO, "BA");
        maps.put(IntDef.INT_FIFTY_THREE, "BB");
        maps.put(IntDef.INT_FIFTY_FOUR, "BC");
        maps.put(IntDef.INT_FIFTY_FIVE, "BD");
        maps.put(IntDef.INT_SIXTYSIX, "BO");

        maps.put(IntDef.INT_TWENTY_ONE, "V");

        maps.put(IntDef.INT_FOURTY_ONE, "AP");
        maps.put(IntDef.INT_FOURTY_TWO, "AQ");
        maps.put(IntDef.INT_FOURTYTHREE, "AR");
        maps.put(IntDef.INT_FOURTY_FOUR, "AS");
        maps.put(IntDef.INT_FOURTY_FIVE, "AT");
        maps.put(IntDef.INT_FOURTY_SIX, "AU");

        maps.put(IntDef.INT_SIXTYTHREE, "BL");
        maps.put(IntDef.INT_SIXTYFOUR, "BM");
        maps.put(IntDef.INT_SIXTYFIVE, "BN");

        dealVVDownload(wbTemplate, vvdataList, Language.CHINESE.getCode(), maps);

        wbTemplate.setForceFormulaRecalculation(true);

        // remove sheet style
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(AISIN_PARTS_MASTER));
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(STYLE));
    }

    /**
     * get PartsMasterEntity List from page
     * 
     * @param dataLists dataLists
     * @param type type
     * @return partsMasterEntityList cpmpmf01EntityList
     */
    private List<PartsMasterEntity> getPartsMasterEntityList(List<Parts> dataLists, int type) {
        List<PartsMasterEntity> partsMasterEntityList = new ArrayList<PartsMasterEntity>();
        if (dataLists != null && dataLists.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("dataLists", dataLists);
            param.setSwapData("type", type);
            partsMasterEntityList = baseMapper.select(this.getSqlId("getPartsMasterEntityListFP"), param);
        }
        return partsMasterEntityList;
    }

    /**
     * deal VV Download
     * 
     * @param wbTemplate wbTemplate
     * @param dataList dataList
     * @param lang lang
     * @param maps maps
     */
    private void dealVVDownload(Workbook wbTemplate, List<PartsMasterEntity> dataList, Integer lang,
        Map<Integer, String> maps) {

        org.apache.poi.ss.usermodel.Sheet sheet = wbTemplate.getSheet(VV_PARTS_MASTER);
        int size = 0;
        if (dataList != null && dataList.size() > 0) {
            size = dataList.size();
            Cell[] TemplateCells = getTemplateCells(STYLE, IntDef.INT_SIX, wbTemplate);
            SheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();

            for (int i = 0; i < dataList.size(); i++) {

                PartsMasterEntity entity = dataList.get(i);
                if (null == entity) {
                    continue;
                }

                if (entity.getBusinessPattern() != null) {
                    entity.setBusinessPattern(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.BUSINESS_PATTERN, Integer.valueOf(entity.getBusinessPattern())));
                }
                if (entity.getBusinessType() != null) {
                    entity.setBusinessType(CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_TYPE,
                        Integer.valueOf(entity.getBusinessType())));
                }
                if (entity.getPartsType() != null) {
                    entity.setPartsType(CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_TYPE,
                        Integer.valueOf(entity.getPartsType())));
                }
                if (entity.getOrderFcType() != null) {
                    entity.setOrderFcType(CodeCategoryManager.getCodeName(lang, CodeMasterCategory.ORDER_FORECAST_TYPE,
                        Integer.valueOf(entity.getOrderFcType())));
                }
                if (entity.getExpCalendarCode() != null) {
                    entity.setExpCalendarCode(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.EXPORT_WH_CALENDER, Integer.valueOf(entity.getExpCalendarCode())));
                }
                if (entity.getOsCustStockFlag() != null) {
                    entity.setOsCustStockFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUSTOMER_STOCK_FLAG, Integer.valueOf(entity.getOsCustStockFlag())));
                }
                if (entity.getSaCustStockFlag() != null) {
                    entity.setSaCustStockFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUSTOMER_STOCK_FLAG, Integer.valueOf(entity.getSaCustStockFlag())));
                }
                if (entity.getInventoryBoxFlag() != null) {
                    entity.setInventoryBoxFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.INVENTORY_BY_BOX, Integer.valueOf(entity.getInventoryBoxFlag())));
                }
                if (entity.getSimulationEndDatePattern() != null) {
                    entity
                        .setSimulationEndDatePattern(CodeCategoryManager.getCodeName(lang,
                            CodeMasterCategory.SIMULATION_END_DAY_P,
                            Integer.valueOf(entity.getSimulationEndDatePattern())));
                }

                if (entity.getDelayAdjustmentPattern() != null) {
                    entity.setDelayAdjustmentPattern(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P,
                        Integer.valueOf(entity.getDelayAdjustmentPattern())));
                }
                if (entity.getCfcAdjustmentType1() != null) {
                    entity.setCfcAdjustmentType1(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUST_FORECAST_ADJUST_P1, Integer.valueOf(entity.getCfcAdjustmentType1())));
                }
                if (entity.getCfcAdjustmentType2() != null) {
                    entity.setCfcAdjustmentType2(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUST_FORECAST_ADJUST_P2, Integer.valueOf(entity.getCfcAdjustmentType2())));
                }
                if (entity.getBuildoutFlag() != null) {
                    entity.setBuildoutFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.BUILD_OUT_INDICATOR, Integer.valueOf(entity.getBuildoutFlag())));
                }

                if (entity.getPartsStatus() != null) {
                    entity.setPartsStatus(CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_STATUS,
                        Integer.valueOf(entity.getPartsStatus())));
                }

                if (entity.getInactiveFlag() != null) {
                    entity.setInactiveFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.DISCONTINUE_INDICATOR, Integer.valueOf(entity.getInactiveFlag())));
                }

                String orderDay = "";
                if (null != entity.getOrderDay()) {
                    orderDay = "D-" + entity.getOrderDay();
                }

                Object[] arrayObj = new Object[] {
                    // 1-10
                    "",
                    "",
                    entity.getTtcPartsNo(),
                    entity.getTtcPartsName(),
                    entity.getPartsNameCn(),
                    entity.getOldTtcPartsNo(),
                    entity.getExpUomCode(),
                    entity.getExpRegion(),
                    entity.getTtcSuppCode(),
                    entity.getSsmsMainRoute(),

                    // 11-20
                    entity.getSsmsVendorRoute(),
                    entity.getExpSuppCode(),
                    entity.getSupplierName(),
                    entity.getSuppPartsNo(),
                    entity.getImpRegion(),
                    entity.getOfficeCode(),
                    entity.getCustomerCode(),
                    entity.getExpCustCode(),
                    entity.getCustomerName(),
                    entity.getCustPartsNo(),

                    // 21-30
                    entity.getCustBackNo(),
                    "",
                    entity.getImpWhsCode(),
                    entity.getWestCustCode(),
                    entity.getWestPartsNo(),
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getOrderLot()),
                    entity.getOrderLot(),
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getSrbq()),
                    entity.getSrbq(),
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getSpq()),
                    entity.getSpq(),
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getSpqM3()),
                    entity.getSpqM3(),
                    entity.getBusinessPattern(),

                    // 31-40
                    entity.getBusinessType(),
                    entity.getPartsType(),
                    entity.getCarModel(),
                    orderDay,
                    entity.getTargetMonth(),
                    entity.getForecastNum(),
                    entity.getOrderFcType(),
                    entity.getExpCalendarCode(),
                    entity.getOsCustStockFlag(),
                    entity.getSaCustStockFlag(),

                    // 41-50
                    entity.getInventoryBoxFlag(),
                    entity.getMinStock(),
                    entity.getMaxStock(),
                    entity.getMinBox(),
                    entity.getMaxBox(),
                    entity.getOrderSafetyStock(),
                    entity.getRundownSafetyStock(),
                    entity.getOutboundFluctuation(),
                    entity.getSimulationEndDatePattern(),
                    entity.getShippingRouteCode(),

                    // 51-60
                    entity.getDelayAdjustmentPattern(), entity.getAirEtdLeadtime(), entity.getAirEtaLeadtime(),
                    entity.getAirInboundLeadtime(), entity.getSeaEtaLeadtime(),
                    entity.getSeaInboundLeadtime(),
                    entity.getAllocationFcType(),
                    entity.getCfcAdjustmentType1(),
                    entity.getCfcAdjustmentType2(),
                    entity.getRemark1(),

                    // 61-68
                    entity.getRemark2(), entity.getRemark3(), entity.getBuildoutFlag(),
                    DateTimeUtil.getDisOrderMonth(entity.getBuildoutMonth(), "yyyyMM"),
                    DateTimeUtil.getDisOrderMonth(entity.getLastPoMonth(), "yyyyMM"),
                    DateTimeUtil.getDisOrderMonth(entity.getLastDeliveryMonth(), "yyyyMM"), entity.getPartsStatus(),
                    entity.getInactiveFlag() };
                createOneDataRowByTemplate(sheet, IntDef.INT_EIGHT + i, TemplateCells, arrayObj, lang,
                    BusinessPattern.V_V, maps, scf);
            }
        } else {
            size = IntDef.INT_EIGHT;
        }
        // set value from style sheet
        for (int i = 0; i < IntDef.INT_FIVE; i++) {
            Cell[] styleCells = getTemplateCells(STYLE, i, wbTemplate);
            Row dataRow = sheet.createRow(IntDef.INT_TEN + size + i);

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

    /**
     * Get template cells from template excel file.
     * 
     * @param sheetName the sheet name
     * @param templateRowNum the template row number
     * @param workbook the workbook (SXSSFSheet)
     * @return template row styles
     */
    private Cell[] getTemplateCells(String sheetName, int templateRowNum, Workbook workbook) {
        Cell[] cells = null;

        if (workbook != null) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                // exception when sheet is not found
                BaseMessage message = new BaseMessage("The sheet [{}] is not found.");
                message.setMessageArgs(new String[] { sheetName });
                throw new DownloadException(message);
            }

            // get cell style from each cell.
            Row templateRow = sheet.getRow(templateRowNum);
            if (null != templateRow) {
                int colCount = templateRow.getLastCellNum();
                cells = new Cell[colCount];
                for (int i = 0; i < colCount; i++) {
                    cells[i] = templateRow.getCell(i);
                }
            }
        }
        return cells;
    }

    /**
     * Create data rows from start row.
     * The start row is template row, include cells style.
     * templateCells's type is Decimal,set cell style with
     * parameter(decimalStyle)
     * 
     * @param sheet the sheet
     * @param rowNum the row number
     * @param templateCells template cells
     * @param data the data those will be output (data are formated)
     * @param lang lang
     * @param type type
     * @param maps maps
     * @param scf scf
     * @throws BusinessException the exception
     */
    private void createOneDataRowByTemplate(Sheet sheet, int rowNum, Cell[] templateCells, Object[] data, Integer lang,
        int type, Map<Integer, String> maps, SheetConditionalFormatting scf) throws BusinessException {
        if (sheet != null && data != null) {

            // output data rows
            int templateCellCount = 0;
            if (templateCells != null) {
                templateCellCount = templateCells.length;
            }

            if (data != null) {
                int rowItemCount = data.length;
                Integer uomDigits = MasterManager.getUomDigits((String) data[IntDef.INT_SIX]);
                Integer realNum = IntDef.INT_SIXTYEIGHT + uomDigits;
                // create new data row
                Row dataRow = sheet.createRow(rowNum);
                for (int j = 0; j < rowItemCount; j++) {
                    // create cell
                    Cell cell = dataRow.createCell(j);

                    // set cell format, formula, type
                    if (templateCells != null && j < templateCellCount && null != templateCells[j]) {
                        Integer jj = j;
                        if (j == IntDef.INT_TWENTY_FIVE || j == IntDef.INT_TWENTY_SIX || j == IntDef.INT_TWENTY_SEVEN
                                || j == IntDef.INT_TWENTY_EIGHT) {
                            jj = realNum;
                        }
                        cell.setCellStyle(templateCells[jj].getCellStyle());
                        int cellType = templateCells[jj].getCellType();
                        cell.setCellType(cellType);
                        if (Cell.CELL_TYPE_FORMULA == cellType) {
                            cell.setCellFormula(templateCells[jj].getCellFormula());
                        }
                    }

                    // Set excel condition style
                    String cfrRule = "";
                    int ruleNum = rowNum + 1;
                    if (j == IntDef.INT_NINE || j == IntDef.INT_TEN || j == IntDef.INT_ELEVEN
                            || j == IntDef.INT_TWENTY_THREE || j == IntDef.INT_TWENTY_FOUR
                            || j == IntDef.INT_THIRTY_THREE || j == IntDef.INT_THIRTY_SEVEN
                            || j == IntDef.INT_FIFTY_ONE || j == IntDef.INT_FIFTY_TWO || j == IntDef.INT_FIFTY_THREE
                            || j == IntDef.INT_FIFTY_FOUR || j == IntDef.INT_FIFTY_FIVE || j == IntDef.INT_SIXTYSIX) {
                        cfrRule = StringConst.DOLLAR + "AD" + StringConst.DOLLAR + ruleNum + StringConst.EQUATE + "\""
                                + "AISIN" + "\"";
                    } else if (j == IntDef.INT_TWENTY_ONE) {
                        cfrRule = StringConst.DOLLAR + "AD" + StringConst.DOLLAR + ruleNum + StringConst.EQUATE + "\""
                                + "V-V" + "\"";
                    } else if (j == IntDef.INT_FOURTY_ONE || j == IntDef.INT_FOURTY_TWO) {
                        cfrRule = StringConst.DOLLAR + "AO" + StringConst.DOLLAR + ruleNum + StringConst.EQUATE + "\""
                                + "Y" + "\"";
                    } else if (j == IntDef.INT_FOURTYTHREE || j == IntDef.INT_FOURTY_FOUR) {
                        cfrRule = StringConst.DOLLAR + "AO" + StringConst.DOLLAR + ruleNum + StringConst.EQUATE + "\""
                                + "N" + "\"";
                    } else if (j == IntDef.INT_SIXTYTHREE || j == IntDef.INT_SIXTYFOUR || j == IntDef.INT_SIXTYFIVE) {
                        cfrRule = StringConst.DOLLAR + "BK" + StringConst.DOLLAR + ruleNum + StringConst.EQUATE + "\""
                                + "N" + "\"";
                    }
                    if (!StringUtil.isNullOrEmpty(cfrRule)) {
                        String rule = maps.get(j);
                        String craRule = rule + ruleNum + StringConst.COLON + rule + ruleNum;
                        ConditionalFormattingRule cfr = scf.createConditionalFormattingRule(cfrRule);
                        PatternFormatting pf = cfr.createPatternFormatting();
                        pf.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
                        ConditionalFormattingRule[] cfRules = { cfr };
                        CellRangeAddress[] regions = { CellRangeAddress.valueOf(craRule) };
                        scf.addConditionalFormatting(regions, cfRules);
                    }

                    String[] pos = null;
                    if (j == IntDef.INT_TWENTY_NINE) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_PATTERN);
                    } else if (j == IntDef.INT_THIRTY) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_TYPE);
                    } else if (j == IntDef.INT_THIRTY_ONE) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_TYPE);
                    } else if (j == IntDef.INT_THIRTY_FIVE) {
                        pos = new String[] { "1", "2", "3", "4", "5", "6" };
                    } else if (j == IntDef.INT_THIRTY_SIX) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.ORDER_FORECAST_TYPE);
                    } else if (j == IntDef.INT_THIRTY_SEVEN) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.EXPORT_WH_CALENDER);
                    } else if (j == IntDef.INT_THIRTY_EIGTH || j == IntDef.INT_THIRTY_NINE) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.CUSTOMER_STOCK_FLAG);
                    } else if (j == IntDef.INT_FOURTY) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.INVENTORY_BY_BOX);
                    } else if (j == IntDef.INT_FOURTY_EIGTH) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.SIMULATION_END_DAY_P);
                    } else if (j == IntDef.INT_FIFTY) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P);
                    } else if (j == IntDef.INT_FIFTY_SEVEN) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.CUST_FORECAST_ADJUST_P1);
                    } else if (j == IntDef.INT_FIFTYEIGHT) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.CUST_FORECAST_ADJUST_P2);
                    } else if (j == IntDef.INT_SIXTY_TWO) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUILD_OUT_INDICATOR);
                    } else if (j == IntDef.INT_SIXTYSIX) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_STATUS);
                    } else if (j == IntDef.INT_SIXTYSEVEN) {
                        pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.DISCONTINUE_INDICATOR);
                    }

                    if (pos != null) {
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

    /**
     * getDBDateTimeByDefaultTimezone
     *
     * @return Timestamp
     * @see com.chinaplus.core.base.BaseService#getDBDateTimeByDefaultTimezone()
     */
    public Timestamp getDBDateTimeByDefaultTimezone() {
        return baseMapper.getCurrentTime();
    }

    /**
     * doQeuryPartsIf
     * 
     * @param parts parts
     * @return List<Parts>
     */
    public List<Parts> doQeuryPartsIf(Parts parts) {
        return this.baseMapper.select(this.getSqlId("getPartsIfList"), parts);
    }

    /**
     * doQeuryCustomerIfExist
     * 
     * @param parts parts
     * @return boolean
     */
    public Parts doQeuryPartsIfExist(Parts parts) {

        List<Parts> partsList = this.baseMapper.select(this.getSqlId("checkPartsExist"), parts);

        if (partsList != null && !partsList.isEmpty() && partsList.get(0) != null
                && partsList.get(0).getExpPartsId() != null) {
            return partsList.get(0);
        }
        return null;
    }

    /**
     * doQeuryCustomerIfExist
     * 
     * @param parts parts
     * @return boolean
     */
    public List<Parts> doQeuryInacticeFlag(Parts parts) {
        List<Parts> partsList = this.baseMapper.select(this.getSqlId("queryInactiveFlag"), parts);
        return partsList;
    }

    /**
     * doQeuryPartsMasterExist
     * 
     * @param parts parts
     * @return List<Parts>
     */
    public Parts doQeuryPartsMaster(Parts parts) {
        List<Parts> masterList = this.baseMapper.select(this.getSqlId("checkPartsMaster"), parts);

        if (masterList != null && !masterList.isEmpty() && masterList.get(0) != null
                && masterList.get(0).getPartsId() != null) {
            return masterList.get(0);
        }
        return null;
    }

    /**
     * doUpdateCustomerSSMS
     * 
     * @param parts parts
     * @return int
     */
    public int doUpdatePartsSSMS(Parts parts) {
        Timestamp currentDate = getDBDateTimeByDefaultTimezone();
        parts.setUpdatedDate(currentDate);
        return this.baseMapper.update(this.getSqlId("modPartsSSMS"), parts);
    }

    /**
     * doUpdatePartsMaster
     * 
     * @param parts parts
     * @return int
     */
    public int doUpdatePartsMaster(Parts parts) {
        Timestamp currentDate = getDBDateTimeByDefaultTimezone();
        parts.setUpdatedDate(currentDate);
        return this.baseMapper.update(this.getSqlId("modPartsMaster"), parts);
    }

    /**
     * doInsertCustomerSSMS
     * 
     * @param parts parts
     * @return int
     */
    public int doInsertPartsSSMS(Parts parts) {
        Timestamp currentDate = getDBDateTimeByDefaultTimezone();
        parts.setCreatedDate(currentDate);
        parts.setUpdatedDate(currentDate);
        return this.baseMapper.insert(this.getSqlId("addPartsSSMS"), parts);
    }

    /**
     * doUpdateHandleFlag
     * 
     * @param parts parts
     * @return int
     */
    public int doUpdateHandleFlag(Parts parts) {
        Timestamp currentDate = getDBDateTimeByDefaultTimezone();
        parts.setUpdatedDate(currentDate);
        return this.baseMapper.update(this.getSqlId("modPartsIfHandleFlag"), parts);
    }

}
