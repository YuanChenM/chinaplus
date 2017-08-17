package com.chinaplus.batch.common.base;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.ExcelUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * The base batch class.
 */
public abstract class BaseFileBatch extends BaseBatch {

    /** the Character Encoding */
    protected static final String ENCODE = "UTF-8";

    /** the default description for result file */
    protected static final String DOWNLOAD_TEMPLATES_PATH = "/templates-batch";

    /** the default buffer data count for download */
    private static final int BUFFER_COUNT = 500;


    /** logger */
    private static Logger logger = LoggerFactory.getLogger(BaseFileBatch.class);

    /**
     * Create SXSS excel with template.
     * 
     * @param fileId fileId
     * @param param parameter
     * @param <T> the parameter class type
     * @param lang language
     */
    protected <T extends BaseEntity> void createSXSSExcelWithTemplate(String fileId, T param, Language lang) {

        OutputStream os = null;
        Workbook wbTemplate = null;
        SXSSFWorkbook wbOutput = null;
        try {

            // get workbook by file id
            wbTemplate = ExcelUtil.getWorkBook(getExcelTemplateFilePath(fileId));

            // set dynamic part
            writeDynamicTemplate(param, wbTemplate, lang);

            // replace i18n items
            PoiUtil.replaceI18nMessage(wbTemplate, fileId, lang.getLocale());

            // write content to excel file
            wbOutput = new SXSSFWorkbook((XSSFWorkbook) wbTemplate, BUFFER_COUNT);

            // write content
            writeContentToExcel(param, wbTemplate, wbOutput, lang);

            // save
            String saveFullPath = getFileSavePathAndName(fileId, param, lang);
            // get stream
            File sFile = new File(saveFullPath);
            // Write download to temporary file
            os = new FileOutputStream(sFile);
            // write
            wbOutput.write(os);

        } catch (Exception e) {
            // logger.debug("Download excel fail.", e);
            throw new BusinessException("Create excel fail.", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    logger.warn("OutputStream close fail.", ex);
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
     * Create HSSF excel with template.
     * 
     * @param fileId fileId
     * @param param parameter
     * @param <T> the parameter class type
     * @param lang language
     */
    protected <T extends BaseEntity> void createXSSFExcelWithTemplate(String fileId, T param, Language lang) {

        OutputStream os = null;
        BufferedInputStream bis = null;

        XSSFWorkbook workBook = null;
        try {

            // get workbook by file id
            workBook = ExcelUtil.getXSSFWorkbook(getExcelTemplateFilePath(fileId));

            // set dynamic part
            writeDynamicTemplate(param, workBook, lang);

            // replace i18n items
            PoiUtil.replaceI18nMessage(workBook, fileId, lang.getLocale());

            // write content
            writeContentToExcel(param, workBook, workBook, lang);
            
            // Create temporary file (download object file)
            File tempFile = new File(getFileSavePathAndName(fileId, param, lang));
            // Write download to temporary file
            os = new FileOutputStream(tempFile);
            workBook.write(os);
            os.close();
        } catch (Exception e) {
            // logger.debug("Download excel fail.", e);
            throw new BusinessException("Create excel fail.", e);
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
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (IOException ex) {
                    logger.warn("Workboox close fail.", ex);
                }
            }
        }
    }

    /**
     * Create SXSS excel with template.
     * 
     * @param fileId fileId
     * @param param parameter
     * @param <T> the parameter class type
     * @param lang language
     * @param zos ZipOutputStream
     */
    protected <T extends BaseEntity> void createSXSSExcelWithTemplate(String fileId, T param, Language lang,
        ZipOutputStream zos) {

        OutputStream os = null;
        Workbook wbTemplate = null;
        SXSSFWorkbook wbOutput = null;
        try {

            // get workbook by file id
            wbTemplate = ExcelUtil.getWorkBook(getExcelTemplateFilePath(fileId));

            // set dynamic part
            writeDynamicTemplate(param, wbTemplate, lang);

            // replace i18n items
            PoiUtil.replaceI18nMessage(wbTemplate, fileId, lang.getLocale());

            // write content to excel file
            wbOutput = new SXSSFWorkbook((XSSFWorkbook) wbTemplate, BUFFER_COUNT);

            // write content
            writeContentToExcel(param, wbTemplate, wbOutput, lang);

            // save
            String saveFullPath = getFileSavePathAndName(fileId, param, lang);

            // Compress temporary file into ZIP file
            zos.putNextEntry(new ZipEntry(saveFullPath));

            // close entry
            wbOutput.write(zos);
        } catch (Exception e) {
            // logger.debug("Download excel fail.", e);
            e.printStackTrace();
            throw new BusinessException("Create excel fail.", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    logger.warn("OutputStream close fail.", ex);
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
     * @param fileId the file id
     * @return template file path
     */
    protected String getExcelTemplateFilePath(String fileId) {
        return StringUtil.formatMessage("{0}/{1}.xlsx", DOWNLOAD_TEMPLATES_PATH, fileId);
    }

    /**
     * Get the excel template file path by file id.
     * 
     * @param workbook the template excel
     * @param param the parameter
     * @param <T> the parameter class type
     * @param lang Language
     */
    protected <T extends BaseEntity> void writeDynamicTemplate(T param, Workbook workbook, Language lang) {
        // do nothing in base
        logger.debug("writeDynamicTemplate in base file batch.");
    }
    
    /**
     * Write content to excel.
     * 
     * @param param the parameter
     * @param <T> the parameter class type
     * @param wbOutput the excel
     * @param wbTemplate the excel
     * @param lang Language
     */
    protected <T extends BaseEntity> void writeContentToExcel(T param, Workbook wbTemplate, Workbook wbOutput, Language lang) {
        // do nothing in base
        logger.debug("writeContentToExcel in BaseFileBatch");
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
    protected <T extends BaseEntity> String getFileSavePathAndName(String fileId, T param, Language lang) {
        // do nothing in base
        logger.debug("getFileSavePath in BaseFileBatch");
        return null;
    }

    /**
     * Encode client file name by browser.
     * 
     * @param str the file name for client
     * @return encoded file name
     */
    protected String urlEncodeString(String str) {

        // temp string
        String tempStr = null;

        try {
            tempStr = URLEncoder.encode(str, ENCODE);
        } catch (Exception e) {
            tempStr = str;
        }

        return tempStr;
    }

    /**
     * Encode client file name by browser.
     * 
     * @param str the file name for client
     * @return encoded file name
     */
    protected String urlDecodeString(String str) {

        // temp string
        String tempStr = null;

        try {
            tempStr = URLDecoder.decode(str, ENCODE);
        } catch (Exception e) {
            tempStr = str;
        }

        return tempStr;
    }

}
