/**
 * Stock Status Report download controller
 * 
 * @screen CPSSSF01
 * @author liu_yinchuan
 */
package com.chinaplus.web.sa.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.TntRundownMasterEx;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.service.RundownService;
import com.chinaplus.common.service.StockStatusService;
import com.chinaplus.common.util.RunDownOnePartsManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.FileUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Stock Status Report download
 */
@Controller
public class CPSRDF02Controller extends BaseFileController {

    /** DOWNLOAD_NAME */
    private static final String DOWNLOAD_EXCEL_NAME = "Rundown_{0}_{1}_{2}.xlsm";

    /** Download zip file name */
    private static final String DOWNLOAD_ZIP_FILE_NAME = "Rundown_Single_{0}.zip";

    @Autowired
    private RundownService service;

    @Autowired
    private StockStatusService stockService;

    @Override
    protected String getFileId() {
        return FileId.CPSRDF02;
    }

    /**
     * Stock Status Report download check.
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/sa/CPSRDF02/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {

        // message list
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();

        // get stock status ids
        List<String> stockStatusIds = (List<String>) param.getSwapData().get("stockStatusIds");
        if (stockStatusIds != null && !stockStatusIds.isEmpty()) {

            // set entity list
            param.setFilters(new HashMap<String, Object>());
        } else {
            // build like conditions
            stockService.makeLikeCondition(param);
        }

        // set common information
        this.setCommonParam(param, request);
        // check vv and aisin flag
        UserOffice office = super.getLoginUser(request).getCurrentOffice();
        // prepare picked up status
        List<Integer> buiPattList = new ArrayList<Integer>();
        // if has vv data
        if (office.getVvFlag()) {
            // add vv parts
            buiPattList.add(BusinessPattern.V_V);
        }
        // if has aisin
        if (office.getAisinFlag()) {
            // add AISIN parts
            buiPattList.add(BusinessPattern.AISIN);
        }
        // set into list
        param.setSwapData("BusinessPatternList", buiPattList);

        // check count
        int count = stockService.getSelectDatasCount(param);

        // if no data
        if (count <= IntDef.INT_ZERO) {
            // set entity list
            BaseMessage message = new BaseMessage(MessageCodeConst.W1005_001);
            messageLists.add(message);
        }

        // if has message, throw them
        if (messageLists.size() != 0) {
            throw new BusinessException(messageLists);
        }

        // if ok
        return new BaseResult<BaseEntity>();
    }

    /**
     * 
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/sa/CPSRDF02/download",
        method = RequestMethod.POST)
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // set common parameters by session
        ObjectParam<TntRundownMasterEx> param = this.convertJsonDataForForm(ObjectParam.class);

        // do check
        List<String> stockStatusIds = (List<String>) param.getSwapData().get("stockStatusIds");
        if (stockStatusIds != null && !stockStatusIds.isEmpty()) {
            // set entity list
            param.setFilters(new HashMap<String, Object>());
        } else {
            // build like conditions
            stockService.makeLikeCondition(param);
        }

        // set common information
        this.setCommonParam(param, request);
        // check vv and aisin flag
        UserOffice office = super.getLoginUser(request).getCurrentOffice();
        // prepare picked up status
        List<Integer> buiPattList = new ArrayList<Integer>();
        // if has vv data
        if (office.getVvFlag()) {
            // add vv parts
            buiPattList.add(BusinessPattern.V_V);
        }
        // if has aisin
        if (office.getAisinFlag()) {
            // add AISIN parts
            buiPattList.add(BusinessPattern.AISIN);
        }
        // set into list
        param.setSwapData("BusinessPatternList", buiPattList);

        // zip file name
        String zipFileName = StringUtil.formatMessage(DOWNLOAD_ZIP_FILE_NAME, param.getClientTime());

        byte[] buffer = new byte[NumberConst.IntDef.CUSTOMER_AUTO_GROW_COLLECTION_LIMIT];
        int length = 0;

        // get zip out put fileOutputStream os = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ZipOutputStream zos = null;

        try {

            // prepare response
            response.setContentType(DEFAULT_CONTENT_TYPE);
            response.setCharacterEncoding(ENCODE);
            response.setHeader("Content-disposition",
                StringUtil.formatMessage("attachment; filename=\"{0}\"", StringUtil.formatMessage(zipFileName)));
            os = response.getOutputStream();
            zos = new ZipOutputStream(os);

            Language lang = null;
            // get language
            if (param.getLanguage() != null && Language.CHINESE.getCode() == param.getLanguage().intValue()) {
                lang = Language.CHINESE;
            } else {
                lang = Language.ENGLISH;
            }
            
            // get all rundown id list
            List<Integer> rdMasterIds = service.findAllRundownMasterIds(param);
            // set into parameter
            param.setSwapData("rundownMasterIds", rdMasterIds);

            // get all datas
            List<TntRundownMasterEx> rundownList = service.getRundownInfoListForSingle(param, lang);

            // for each datas
            if (rundownList != null) {

                // temp file
                String tempFolderPath = ConfigUtil.get(Properties.TEMPORARY_PATH) + UUID.randomUUID().toString();
                // Create temporary folder
                File tempFolder = new File(tempFolderPath);
                if (!tempFolder.exists()) {
                    tempFolder.mkdirs();
                }

                // loop and create
                for (TntRundownMasterEx rundown : rundownList) {

                    // file name
                    String fileName = StringUtil.formatMessage(
                        DOWNLOAD_EXCEL_NAME,
                        new Object[] { rundown.getOfficeCode().replaceAll(StringConst.COLON, StringConst.EMPTY),
                            this.urlEncodeString(rundown.getCustomerCode()),
                            this.urlEncodeString(rundown.getTtcPartsNo()) });

                    // set values
                    param.setData(rundown);

                    // do excel with template
                    super.makeZipExcelWithMultiTemplateForXSSF(tempFolderPath, fileName, param);
                }

                // save
                for (File zipFile : tempFolder.listFiles()) {
                    zos.putNextEntry(new ZipEntry(zipFile.getName()));
                    bis = new BufferedInputStream(new FileInputStream(zipFile));
                    while ((length = bis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    bis.close();
                }
                zos.close();

                // delete
                FileUtil.deleteAllFile(tempFolder);
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ex) {
                    // logger.warn("OutputStream close fail.", ex);
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception ex) {
                    // logger.warn("OutputStream close fail.", ex);
                }
            }
            if (zos != null) {
                try {
                    zos.close();
                } catch (Exception ex) {
                    // logger.warn("OutputStream close fail.", ex);
                }
            }
        }
    }

    /**
     * Write content to excel.
     *
     * @param param parameter
     * @param wbTemplate workbook template
     * @param wbOutput workbook out put
     * @param fieldId field Id
     * @see com.chinaplus.core.base.BaseFileController#writeContentToExcel(com.chinaplus.core.bean.BaseParam, org.apache.poi.ss.usermodel.Workbook,
     *      org.apache.poi.xssf.streaming.SXSSFWorkbook, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook workbook) {

        // cast
        ObjectParam<TntRundownMasterEx> castParam = (ObjectParam<TntRundownMasterEx>) param;

        // get header map
        Date clientTime = DateTimeUtil.parseDate(param.getClientTime(), DateTimeUtil.FORMAT_DATE_FULL);
        Language lang = null;
        // get language
        if (param.getLanguage() != null && Language.CHINESE.getCode() == param.getLanguage().intValue()) {
            lang = Language.CHINESE;
        } else {
            lang = Language.ENGLISH;
        }

        // prepare date list
        Date[] dateList = new Date[IntDef.INT_FOUR];
        dateList[IntDef.INT_ZERO] = clientTime;
        // prepare Sync time
        service.prepareSyncDateTime(param.getCurrentOfficeId(), dateList);

        // make run-down detail information
        RunDownOnePartsManager.makeRDSinglePart((XSSFWorkbook) workbook, castParam.getData(), lang, dateList);
    }

    /**
     * Get the excel template file path by file id.
     * 
     * @param fileId the file id
     * @return template file path
     */
    @Override
    protected String getExcelTemplateFilePath(String fileId) {
        return StringUtil.formatMessage("{0}/{1}.xlsm", DOWNLOAD_TEMPLATES_PATH, fileId);
    }
    
    /**
     * Encode client file name by browser.
     * 
     * @param str the file name for client
     * @return encoded file name
     */
    private String urlEncodeString(String str) {

        // temp string
        String tempStr = null;
        try {
            tempStr = URLEncoder.encode(str, ENCODE);
        } catch (Exception e) {
            tempStr = str;
        }

        return tempStr;
    }
}
