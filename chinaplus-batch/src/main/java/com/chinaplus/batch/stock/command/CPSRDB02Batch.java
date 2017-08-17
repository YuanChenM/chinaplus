/**
 * @screen Run-Down Batch main process
 * @author liu_yinchuan
 */
package com.chinaplus.batch.stock.command;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseFileBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.StockBatchId;
import com.chinaplus.batch.common.consts.BatchConst.StockCommonParam;
import com.chinaplus.batch.stock.bean.BasePartsInfoEntity;
import com.chinaplus.batch.stock.bean.StockComParam;
import com.chinaplus.common.bean.TntRundownHeader;
import com.chinaplus.common.bean.TntRundownMasterEx;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.RundownType;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.service.RundownService;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.common.util.RunDownAllPartsManager;
import com.chinaplus.common.util.RunDownOnePartsManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.FileUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Main Batch process for run-down batch.
 * 
 * @author liu_yinchuan
 */
@Component(StockBatchId.CPSRDB02)
public class CPSRDB02Batch extends BaseFileBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPSRDB02Batch.class);

    /** Stock status report file id */
    private static String ALL_PARTS_FILE_ID = "CPSRDF01";

    /** Stock status report file name */
    private static String ALL_PARTS_FILE_NAME = "Rundown_All";

    /** Stock status report file id */
    private static String SINGLE_PARTS_FILE_ID = "CPSRDF02";

    /** Stock status report file name */
    private static String SINGLE_PARTS_FILE_NAME = "Rundown";

    /** The Service of Run-Down Batch */
    @Autowired
    private RundownService rundownService;

    /** head map */
    private Map<Integer, String> headMap;

    /** styleMap */
    private Map<String, CellStyle> styleMap;

    /**
     * Prepare run-down parameter.
     * 
     * @param args batch parameter
     */
    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        // parameter object initialization
        StockComParam param = null;

        // Check batch arguments
        // if (args == null || (args.length != BatchConst.INT_TWO && args.length != BatchConst.INT_THREE)) {
        if (args == null || args.length != BatchConst.INT_THREE) {
            // error
            logger.error("The arguments number of batch is incorrect.");
            throw new BusinessException();
        } else {
            // set parameters
            param = new StockComParam();
            param.setOnlineFlag(StringUtil.toInteger(args[StockCommonParam.IS_ONLINE]));
            // if date is null, error
            if (param.getOnlineFlag() == null) {
                logger.error("Online Flag is incorrect(Please set On-Line Flag as 1: On-Line or 2: Off-Line).");
                throw new BusinessException();
            }
            // set parameters
            param.setStockDate(DateTimeUtil.parseDate(args[StockCommonParam.STOCK_DATE], DateTimeUtil.FORMAT_YYYYMMDD));
            // if date is null, error
            if (param.getStockDate() == null) {
                logger.error("Stock date is incorrect(Please set Stock Date like:2016-02-23).");
                throw new BusinessException();
            }
            // if office exist, set office code
            if (!StringUtil.isEmpty(args[StockCommonParam.OFFICE_CODE])) {
                param.setOfficeCode(args[StockCommonParam.OFFICE_CODE]);
                // check
                TnmOffice office = baseService.getOfficeInfo(param.getOfficeCode());
                if (office == null) {
                    logger.error("Office code does not an effective office.");
                    throw new BusinessException();
                }
                // set office id
                param.setOfficeId(office.getOfficeId());
                param.setTimeZone(office.getTimeZone());
            } else {
                logger.error("Office Code is incorrect, can not be empty.");
                throw new BusinessException();
            }

            // set process date
            param.setProcessDate(baseService.getDBDateTime(param.getTimeZone()));
        }

        // return
        return param;
    }

    /**
     * Main process logic for rundown batch.
     * 
     * @param BaseBatchParam.
     * @return the result of current operation
     */
    @Override
    public boolean doOperate(BaseBatchParam baseParam) throws Exception {

        // batch process logic start
        logger.info("batch CPSRDB01Batch start......");

        // cast to StockComParam
        StockComParam param = (StockComParam) baseParam;

        // pick up all office, and then loop by each office
        BasePartsInfoEntity office = new BasePartsInfoEntity();
        // set stock date
        office.setStockDate(param.getStockDate());
        office.setOfficeId(param.getOfficeId());
        office.setOfficeCode(param.getOfficeCode());
        office.setTimeZone(param.getTimeZone());

        // Back up Run-down Data
        doHistoryBackUp(office);
            
        logger.info("batch CPSRDB01Batch end......");

        // return OK.
        return true;
    }

    /**
     * Back up Run-down history Data
     * 
     * @param office office information
     * @throws Exception Exception
     */
    private void doHistoryBackUp(BasePartsInfoEntity office) throws Exception {
        
        // make directory
        FileUtil.createDirectory(new File(ConfigManager.getBatchFileSavePath()));

        // Create Run-down All Parts File by each Office and Business Type
        this.doMakeRundownAllParts(office);

        // Create Run-down Single Parts File by each Office and Business Type
        this.doMakeRundownSingleParts(office);
    }

    /**
     * Create Run-down All Parts File by each Office and Business Type
     * 
     * @param office office information
     * @throws Exception Exception
     */
    private void doMakeRundownAllParts(BasePartsInfoEntity office) throws Exception {

        // do create VV Parts
        office.setBusinessPattern(BusinessPattern.V_V);
        this.doMakerRundownAllPartsWithZip(office, Language.ENGLISH);
        this.doMakerRundownAllPartsWithZip(office, Language.CHINESE);

        // do create AISIN Parts
        office.setBusinessPattern(BusinessPattern.AISIN);
        this.doMakerRundownAllPartsWithZip(office, Language.ENGLISH);
        this.doMakerRundownAllPartsWithZip(office, Language.CHINESE);
    }

    /**
     * Prepare rundown all parts.
     * 
     * @param office office
     * @param lang Language
     * @throws Exception Exception
     */
    private void doMakerRundownAllPartsWithZip(BasePartsInfoEntity office, Language lang) throws Exception {

        // zip out put stream
        ZipOutputStream zos = null;
        FileOutputStream outPut = null;
        try {

            // check
            BaseParam bParam = new BaseParam();
            bParam.setCurrentOfficeId(office.getOfficeId());
            bParam.setSwapData("businessPattern", office.getBusinessPattern());
            // get header
            TntRundownHeader header = rundownService.getRundownTitleForAll(bParam);
            if (header == null) {
                return;
            }
            
            // create zip files
            String zipName = getZipFileName(office, RundownType.ALL_PARTS, lang);
            File zipedFile = new File(zipName);
            zipedFile.setWritable(true);
            zipedFile.setReadable(true);
            outPut = new FileOutputStream(zipedFile);
            zos = new ZipOutputStream(outPut);

            // do create
            super.createSXSSExcelWithTemplate(ALL_PARTS_FILE_ID, office, lang, zos);
        } finally {
            if (outPut != null) {
                try {
                    outPut.close();
                } catch (Exception e) {
                    ;// do nothing
                }
            }
            if (zos != null) {
                try {
                    zos.closeEntry();
                    zos.close();
                } catch (Exception e) {
                    ;// do nothing
                }
            }
        }
    }

    /**
     * Create Run-down Single Parts File by each Office and Business Type
     * 
     * @param office office information
     * @throws Exception Exception
     */
    private void doMakeRundownSingleParts(BasePartsInfoEntity office) throws Exception {

        // do create VV Parts
        office.setBusinessPattern(BusinessPattern.V_V);
        this.doMakerRundownSinglePartsWithZip(office, Language.ENGLISH);
        this.doMakerRundownSinglePartsWithZip(office, Language.CHINESE);

        // do create AISIN Parts
        office.setBusinessPattern(BusinessPattern.AISIN);
        this.doMakerRundownSinglePartsWithZip(office, Language.ENGLISH);
        this.doMakerRundownSinglePartsWithZip(office, Language.CHINESE);
    }

    /**
     * Prepare rundown Single parts.
     * 
     * @param office office
     * @param lang Language
     * @throws Exception Exception
     */
    private void doMakerRundownSinglePartsWithZip(BasePartsInfoEntity office, Language lang) throws Exception {

        byte[] buffer = new byte[NumberConst.IntDef.CUSTOMER_AUTO_GROW_COLLECTION_LIMIT];
        int length = 0;

        BufferedInputStream bis = null;

        // zip out put stream
        ZipOutputStream zos = null;
        FileOutputStream outPut = null;
        try {

            // prepare parameter
            BaseParam bParam = new BaseParam();
            bParam.setCurrentOfficeId(office.getOfficeId());
            bParam.setSwapData("businessPattern", office.getBusinessPattern());
            // for English
            List<TntRundownMasterEx> rundownList = rundownService.getRundownInfoListForSingle(bParam, lang);
            // no data
            if (rundownList == null || rundownList.isEmpty()) {
                return;
            }

            // clear files
            String tempFilePath = this.getTempFolder(UUID.randomUUID().toString());
            File file = new File(tempFilePath);
            FileUtil.createDirectory(file);

            // loop each parts
            for (TntRundownMasterEx rundownInfo : rundownList) {
                
                // set
                rundownInfo.setTempFilePath(tempFilePath);
                
                // reset some values
                rundownInfo.setTimeZone(office.getTimeZone());

                // create excel
                super.createXSSFExcelWithTemplate(SINGLE_PARTS_FILE_ID, rundownInfo, lang);
            }

            // create zip files
            String zipName = getZipFileName(office, RundownType.SINGLE_PARTS, lang);
            File zipedFile = new File(zipName);
            zipedFile.setWritable(true);
            zipedFile.setReadable(true);
            outPut = new FileOutputStream(zipedFile);
            zos = new ZipOutputStream(outPut);
            // save
            for (File zipFile : file.listFiles()) {
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
            FileUtil.deleteAllFile(file);
        } finally {
            try {
                outPut.close();
            } catch (Exception e) {
                ;// do nothing
            }
            try {
                bis.close();
            } catch (Exception e) {
                ;// do nothing
            }
            try {
                zos.close();
            } catch (Exception e) {
                ;// do nothing
            }
        }
    }

    /**
     * Get the excel template file path by file id.
     * 
     * @param workbook the template excel
     * @param param the parameter
     * @param <T> the parameter class type
     * @param lang Language
     */
    @Override
    protected <T extends BaseEntity> void writeDynamicTemplate(T param, Workbook workbook, Language lang) {

        // means all parts
        if (param instanceof BasePartsInfoEntity) {
            // cast parameter
            BasePartsInfoEntity castOffice = (BasePartsInfoEntity) param;

            // new map
            headMap = new HashMap<Integer, String>();
            styleMap = new HashMap<String, CellStyle>();

            // set values
            BaseParam bParam = new BaseParam();
            bParam.setCurrentOfficeId(castOffice.getOfficeId());
            bParam.setSwapData("businessPattern", castOffice.getBusinessPattern());

            // get header
            TntRundownHeader header = rundownService.getRundownTitleForAll(bParam);

            // prepare header and get style sheet
            RunDownAllPartsManager.prepStyleMapForAllParts(workbook, styleMap, lang);
            RunDownAllPartsManager.setHeaderAllPart(workbook, lang, styleMap, headMap, header);
        }
    }

    /**
     * Write content to excel.
     *
     * @param param parameter
     * @param wbTemplate workbook template
     * @param wbOutput workbook out put
     * @param fieldId field Id
     * @param lang Language
     */
    @Override
    protected <T extends BaseEntity> void writeContentToExcel(T param, Workbook wbTemplate, Workbook wbOutput,
        Language lang) {

        // for all parts
        if (param instanceof BasePartsInfoEntity) {
            // cast parameter
            BasePartsInfoEntity castOffice = (BasePartsInfoEntity) param;

            // get header map
            Date serverTime = rundownService.getDBDateTime(castOffice.getTimeZone());

            // set values
            BaseParam bParam = new BaseParam();
            bParam.setCurrentOfficeId(castOffice.getOfficeId());
            bParam.setSwapData("businessPattern", castOffice.getBusinessPattern());

            // get run down detail list
            List<TntRundownMasterEx> stockStatusList = rundownService.getRundownInfoListForAll(bParam, lang);

            // do cast
            SXSSFWorkbook castWorkBoook = (SXSSFWorkbook) wbOutput;

            // prepare date list
            Date[] dateList = new Date[IntDef.INT_FOUR];
            dateList[IntDef.INT_ZERO] = serverTime;
            // prepare Sync time
            rundownService.prepareSyncDateTime(castOffice.getOfficeId(), dateList);

            // prepare datas
            RunDownAllPartsManager.setRundownDetailInfo(castWorkBoook, stockStatusList, lang, styleMap, headMap,
                castOffice.getOfficeCode(), dateList);
        } else {

            // cast parameter
            TntRundownMasterEx castRundown = (TntRundownMasterEx) param;

            // get header map
            Date serverTime = rundownService.getDBDateTime(castRundown.getTimeZone());

            // prepare date list
            Date[] dateList = new Date[IntDef.INT_FOUR];
            dateList[IntDef.INT_ZERO] = serverTime;
            // prepare Sync time
            rundownService.prepareSyncDateTime(castRundown.getOfficeId(), dateList);

            // prepare datas
            RunDownOnePartsManager.makeRDSinglePart((XSSFWorkbook) wbOutput, castRundown, lang, dateList);
        }
    }

    /**
     * Prepare file save path.
     * 
     * @param fileId the fileId
     * @param param parameter
     * @param <T> as BaseEntity
     * @param lang Language
     * @return file path
     */
    @Override
    protected <T extends BaseEntity> String getFileSavePathAndName(String fileId, T param, Language lang) {
        // do nothing in base
        logger.debug("getFileSavePathAndName in BaseFileBatch");
        // get file path
        StringBuffer fullName = new StringBuffer();
        // fullName.append(File.separatorChar);
        if (param instanceof BasePartsInfoEntity) {

            // cast parameter
            BasePartsInfoEntity castOffice = (BasePartsInfoEntity) param;

            fullName.append(ALL_PARTS_FILE_NAME);
            fullName.append(StringConst.UNDERLINE);
            fullName.append(castOffice.getOfficeCode().replaceAll(StringConst.COLON, StringConst.EMPTY));
            fullName.append(StringConst.UNDERLINE);
            fullName.append(CodeCategoryManager.getCodeName(Language.ENGLISH.getCode(),
                CodeMasterCategory.BUSINESS_PATTERN, castOffice.getBusinessPattern()));
            fullName.append(StringConst.UNDERLINE);
            fullName.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, castOffice.getStockDate()));
            fullName.append(StringConst.DOT);
            fullName.append(CoreConst.SUFFIX_EXCEL);
        } else {

            // cast parameter
            TntRundownMasterEx castRundown = (TntRundownMasterEx) param;
            fullName.append(castRundown.getTempFilePath());
            fullName.append(File.separatorChar);
            fullName.append(SINGLE_PARTS_FILE_NAME);
            fullName.append(StringConst.UNDERLINE);
            fullName.append(super.urlEncodeString(castRundown.getCustomerCode()));
            fullName.append(StringConst.UNDERLINE);
            fullName.append(super.urlEncodeString(castRundown.getTtcPartsNo()));
            fullName.append(StringConst.UNDERLINE);
            fullName.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, castRundown.getStockDate()));
            fullName.append(StringConst.DOT);
            fullName.append(CoreConst.SUFFIX_EXCEL_M);
        }

        // return full name
        return fullName.toString();
    }

    /**
     * Prepare file save path.
     * 
     * @param office the office
     * @param rdType rudnown type
     * @param lang Language
     * @return zip file path
     */
    private String getZipFileName(BasePartsInfoEntity office, String rdType, Language lang) {
        // do nothing in base
        logger.debug("getZipFileName in BaseFileBatch");
        // get file path
        StringBuffer fullName = new StringBuffer();
        fullName.append(ConfigManager.getBatchFileSavePath());
        fullName.append(File.separatorChar);
        fullName.append(office.getOfficeCode().replaceAll(StringConst.COLON, StringConst.EMPTY));
        fullName.append(StringConst.UNDERLINE);
        fullName.append(CodeCategoryManager.getCodeName(Language.ENGLISH.getCode(),
            CodeMasterCategory.BUSINESS_PATTERN, office.getBusinessPattern()));
        fullName.append(StringConst.UNDERLINE);
        fullName.append(rdType);
        fullName.append(StringConst.UNDERLINE);
        fullName.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, office.getStockDate()));
        fullName.append(StringConst.UNDERLINE);
        fullName.append(lang.getName());
        fullName.append(StringConst.DOT);
        fullName.append(FileType.ZIP.getSuffix());

        // return full name
        return fullName.toString();
    }

    /**
     * Get the excel template file path by file id.
     * 
     * @param fileId the file id
     * @return template file path
     */
    @Override
    protected String getExcelTemplateFilePath(String fileId) {
        if (SINGLE_PARTS_FILE_ID.equals(fileId)) {
            return StringUtil.formatMessage("{0}/{1}.xlsm", DOWNLOAD_TEMPLATES_PATH, fileId);
        } else {
            return StringUtil.formatMessage("{0}/{1}.xlsx", DOWNLOAD_TEMPLATES_PATH, fileId);
        }
    }

    /**
     * Prepare file save path.
     * 
     * @param tempFilePath tempFilePath
     * @return temp file path
     */
    private String getTempFolder(String tempFilePath) {
        // do nothing in base
        logger.debug("getFileSavePathAndName in BaseFileBatch");

        StringBuffer fullName = new StringBuffer();
        fullName.append(ConfigManager.getSystemTempPath());
        fullName.append(File.separatorChar);
        fullName.append(tempFilePath);

        // return full name
        return fullName.toString();
    }
}
