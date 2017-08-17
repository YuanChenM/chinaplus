/**
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.base;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.common.util.ExcelConfigManager;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.FileConst.CellStyleConst;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.exception.DownloadException;
import com.chinaplus.core.exception.UploadException;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.ExcelUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * The base class of the upload and download controller class.
 */
@SuppressWarnings("deprecation")
public abstract class BaseFileController extends BaseController {

    /** the default content type */
    protected static final String DEFAULT_CONTENT_TYPE = "application/octet-stream;charset=UTF-8";

    /** the Character Encoding */
    protected static final String ENCODE = "UTF-8";

    // /** the download buffer size */
    // private static final int BUFFER_SIZE = 2048;
    /** the default description for result file */
    protected static final String DOWNLOAD_TEMPLATES_PATH = "/templates";

    /** the default buffer data count for download */
    protected static final int BUFFER_COUNT = 500;

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(BaseFileController.class);

    /**
     * Get current file id.
     * 
     * @return file id
     */
    abstract protected String getFileId();

    /**
     * Read the excel template file, then create download file and download it.
     * 
     * @param param the parameter
     * @param <T> the parameter class type
     * @param request the request
     * @param response the response
     */
    protected <T extends BaseParam> void downloadExcelWithTemplate(T param, HttpServletRequest request,
        HttpServletResponse response) {
        downloadExcelWithTemplate(null, param, request, response);
    }

    /**
     * Read the excel template file, then create download file and download it.
     * 
     * @param clientFileName the file name for client
     * @param param the parameter
     * @param <T> the parameter class type
     * @param request the request
     * @param response the response
     */
    protected <T extends BaseParam> void downloadExcelWithTemplate(String clientFileName, T param,
        HttpServletRequest request, HttpServletResponse response) {

        Workbook wbTemplate = null;
        SXSSFWorkbook wbOutput = null;
        try {

            String fileId = this.getFileId();
            // get workbook by file id
            wbTemplate = ExcelUtil.getWorkBook(getExcelTemplateFilePath(fileId));
            // set dynamic part
            writeDynamicTemplate(param, wbTemplate);
            // replace i18n items
            PoiUtil.replaceI18nMessage(wbTemplate, fileId);

            // write content to excel file
            wbOutput = new SXSSFWorkbook((XSSFWorkbook) wbTemplate, BUFFER_COUNT);
            writeContentToExcel(param, wbTemplate, wbOutput);

            // output excel to response
            String downloadFileName = clientFileName;
            if (StringUtil.isEmpty(downloadFileName)) {
                downloadFileName = getClientFileName(param);
            }

            //
            outputDownloadExcel(downloadFileName, wbOutput, request, response);

        } catch (Exception e) {
            // logger.debug("Download excel fail.", e);
            throw new DownloadException("Download excel fail.", e);
        } finally {
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
     * Read the excel template file, then create download file and download it.
     * 
     * @param fileName the file name for client
     * @param param the parameter
     * @param zos zip output stream
     * @param <T> the parameter class type
     */
    protected <T extends BaseParam> void makeZipExcelWithTemplate(String fileName, T param, ZipOutputStream zos) {

        byte[] buffer = new byte[NumberConst.IntDef.CUSTOMER_AUTO_GROW_COLLECTION_LIMIT];
        int length = 0;
        BufferedInputStream bis = null;
        Workbook wbTemplate = null;
        SXSSFWorkbook wbOutput = null;
        try {

            String fileId = this.getFileId();
            // get workbook by file id
            wbTemplate = ExcelUtil.getWorkBook(getExcelTemplateFilePath(fileId));
            // set dynamic part
            writeDynamicTemplate(param, wbTemplate);
            // replace i18n items
            PoiUtil.replaceI18nMessage(wbTemplate, fileId);

            // write content to excel file
            wbOutput = new SXSSFWorkbook((XSSFWorkbook) wbTemplate, BUFFER_COUNT);
            writeContentToExcel(param, wbTemplate, wbOutput);

            // output excel to response
            String downloadFileName = fileName;
            if (StringUtil.isEmpty(downloadFileName)) {
                downloadFileName = getClientFileName(param);
            }

            // Create temporary folder
            String tempFolderPath = ConfigUtil.get(ChinaPlusConst.Properties.TEMPORARY_PATH)
                    + UUID.randomUUID().toString();
            File tempFolder = new File(tempFolderPath);
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }

            // write to temporary file
            File tempFile = new File(tempFolder, downloadFileName);
            wbOutput.write(new FileOutputStream(tempFile));

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
            tempFolder.delete();
        } catch (Exception e) {
            // logger.debug("Download excel fail.", e);
            throw new DownloadException("Download excel fail.", e);
        } finally {
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
     * Read the excel template file, then create download file and download it.
     * 
     * @param tempFolder temporary file path
     * @param fileNames the file name for client
     * @param fileIds the file id of template
     * @param param the parameter
     * @param zos zip output stream
     * @param <T> the parameter class type
     */
    protected <T extends BaseParam> void makeZipExcelWithMultiTemplate(String tempFolder, String[] fileNames,
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
                wbTemplate = ExcelUtil.getWorkBook(getExcelTemplateFilePath(fileIds[i]));
                // set dynamic part
                writeDynamicTemplate(param, wbTemplate, fileIds[i]);

                // write content to excel file
                wbOutput = new SXSSFWorkbook((XSSFWorkbook) wbTemplate, -1);
                writeContentToExcel(param, wbTemplate, wbOutput, fileIds[i]);

                // replace i18n items
                PoiUtil.replaceI18nMessage(wbTemplate, fileIds[i]);

                // output excel to response
                String downloadFileName = fileNames[i];
                if (StringUtil.isEmpty(downloadFileName)) {
                    downloadFileName = getClientFileName(param);
                }

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
            throw new DownloadException("Download excel fail.", e);
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
     * Read the excel template file, then create download file and download it.
     * 
     * @param tempFolder temporary file path
     * @param param the parameter
     * @param fileName fileName
     * @param <T> the parameter class type
     */
    protected <T extends BaseParam> void makeZipExcelWithMultiTemplateForXSSF(String tempFolder, String fileName,
        T param) {

        OutputStream os = null;
        BufferedInputStream bis = null;

        Workbook workBook = null;
        try {

            // get list
            String fileId = getFileId();

            // get workbook by file id
            workBook = ExcelUtil.getXSSFWorkbook(getExcelTemplateFilePath(fileId));

            // replace i18n items
            PoiUtil.replaceI18nMessage(workBook, fileId);

            // write content to excel file
            writeContentToExcel(param, workBook);

            // output excel to response
            String downloadFileName = fileName;
            if (StringUtil.isEmpty(downloadFileName)) {
                downloadFileName = getClientFileName(param);
            }

            // Create temporary file (download object file)
            File tempFile = new File(tempFolder, downloadFileName);

            // Write download to temporary file
            os = new FileOutputStream(tempFile);
            workBook.write(os);
            os.close();

        } catch (Exception e) {
            // logger.debug("Download excel fail.", e);
            throw new DownloadException("Download excel fail.", e);
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
     * Get the text template file path by file id.
     * 
     * @param fileId the file id
     * @return template file path
     */
    protected String getTextTemplateFilePath(String fileId) {
        return StringUtil.formatMessage("{0}/{1}.txt", DOWNLOAD_TEMPLATES_PATH, fileId);
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
     * @param fileId the file id
     * @return template file path
     */
    protected String getExcelWithMacroTemplateFilePath(String fileId) {
        return StringUtil.formatMessage("{0}/{1}.xlsm", DOWNLOAD_TEMPLATES_PATH, fileId);
    }

    /**
     * Get the excel template file path by file id.
     * 
     * @param workbook the template excel
     * @param param the parameter
     * @param <T> the parameter class type
     */
    protected <T extends BaseParam> void writeDynamicTemplate(T param, Workbook workbook) {
        // do nothing in base
        logger.debug("writeDynamicTemplate in BaseFileController");
    }

    /**
     * Get the excel template file path by file id.
     * 
     * @param workbook the template excel
     * @param param the parameter
     * @param <T> the parameter class type
     * @param fileId fileId
     */
    protected <T extends BaseParam> void writeDynamicTemplate(T param, Workbook workbook, String fileId) {
        // do nothing in base
        logger.debug("writeDynamicTemplate in BaseFileController");
    }

    /**
     * Write content to excel.
     * 
     * @param param the parameter
     * @param <T> the parameter class type
     * @param wbOutput the excel
     * @param wbTemplate the excel
     */
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {
        // do nothing in base
        logger.debug("writeContentToExcel in BaseFileController");
    }

    /**
     * Write content to excel.
     * 
     * @param param the parameter
     * @param <T> the parameter class type
     * @param wbOutput the excel
     */
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbOutput) {
        // do nothing in base
        logger.debug("writeContentToExcel in BaseFileController");
    }

    /**
     * Write content to excel.
     * 
     * @param param the parameter
     * @param <T> the parameter class type
     * @param wbOutput the excel
     * @param wbTemplate the excel
     * @param fileId fileId
     */
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput,
        String fileId) {
        // do nothing in base
        logger.debug("writeContentToExcel in BaseFileController");
    }

    /**
     * Get client file name for download.
     * 
     * @param param the parameter
     * @param <T> the parameter class type
     * @return client file name
     */
    protected <T extends BaseParam> String getClientFileName(T param) {
        logger.debug("getClientFileName in BaseFileController");
        // default client file name for download
        return StringUtil.formatMessage("{0}_{1}.xlsx", getFileId(), param.getClientTime());
    }

    /**
     * Create new sheet by template sheet.
     * 
     * @param workbook the excel file
     * @param newSheetName new sheet name
     * @param templateSheetName template sheet name
     * @throws BusinessException the exception
     */
    protected static void createSheetByTemplate(Workbook workbook, String newSheetName, String templateSheetName)
        throws BusinessException {

        // check sheet name
        if (workbook.getSheetIndex(newSheetName) >= 0) {
            // exception when sheet is not found
            BaseMessage message = new BaseMessage("The sheet name [{}] is already exists.");
            message.setMessageArgs(new String[] { newSheetName });
            throw new DownloadException(message);
        }

        // get template sheet
        Sheet templateSheet = workbook.getSheet(templateSheetName);
        if (templateSheet == null) {
            // exception when sheet is not found
            BaseMessage message = new BaseMessage("The template sheet [{}] is not found.");
            message.setMessageArgs(new String[] { templateSheetName });
            throw new DownloadException(message);
        }

        // create new sheet
        Sheet newSheet = workbook.createSheet(newSheetName);

        // copy from template sheet
        PoiUtil.copySheet(workbook, templateSheet, newSheet);
    }

    /**
     * Create new sheet by template sheet.
     * 
     * @param workbook the excel file
     * @param newSheetName new sheet name
     * @param templateSheetName template sheet name
     * @throws BusinessException the exception
     */
    protected static void cloneSheetByTemplate(Workbook workbook, String newSheetName, String templateSheetName)
        throws BusinessException {

        // check sheet name
        if (workbook.getSheetIndex(newSheetName) >= 0) {
            // exception when sheet is not found
            BaseMessage message = new BaseMessage("The sheet name [{}] is already exists.");
            message.setMessageArgs(new String[] { newSheetName });
            throw new DownloadException(message);
        }

        // get template sheet
        Sheet templateSheet = workbook.getSheet(templateSheetName);
        if (templateSheet == null) {
            // exception when sheet is not found
            BaseMessage message = new BaseMessage("The template sheet [{}] is not found.");
            message.setMessageArgs(new String[] { templateSheetName });
            throw new DownloadException(message);
        }

        // create new sheet
        Sheet newSheet = workbook.cloneSheet(workbook.getSheetIndex(templateSheetName));
        workbook.setSheetName(workbook.getSheetIndex(newSheet), newSheetName);

        // copy from template sheet
        // PoiUtil.copySheet(workbook, templateSheet, newSheet);
    }

    /**
     * get Decimal Style according digits.
     * 
     * @param wbTemplate the template workbook
     * @param decimalStyleSheetName the decimal style sheet name
     * @param digits the number digits
     * @return the cell style for this decimal
     */
    protected CellStyle getDecimalStyle(Workbook wbTemplate, String decimalStyleSheetName, int digits) {

        /** cell style define */
        HashMap<String, CellStyle> mapCellStyle = new HashMap<String, CellStyle>();
        Sheet sheetStyle = wbTemplate.getSheet(decimalStyleSheetName);

        CellStyle retStyle;

        // get cell style
        mapCellStyle.put(CellStyleConst.CS_COMMA_DECIMAL_0, sheetStyle.getRow(NumberConst.IntDef.INT_ONE).getCell(0)
            .getCellStyle());
        mapCellStyle.put(CellStyleConst.CS_COMMA_DECIMAL_1, sheetStyle.getRow(NumberConst.IntDef.INT_THREE).getCell(0)
            .getCellStyle());
        mapCellStyle.put(CellStyleConst.CS_COMMA_DECIMAL_2, sheetStyle.getRow(NumberConst.IntDef.INT_FIVE).getCell(0)
            .getCellStyle());
        mapCellStyle.put(CellStyleConst.CS_COMMA_DECIMAL_3, sheetStyle.getRow(NumberConst.IntDef.INT_SEVEN).getCell(0)
            .getCellStyle());
        mapCellStyle.put(CellStyleConst.CS_COMMA_DECIMAL_4, sheetStyle.getRow(NumberConst.IntDef.INT_NINE).getCell(0)
            .getCellStyle());
        mapCellStyle.put(CellStyleConst.CS_COMMA_DECIMAL_5, sheetStyle.getRow(NumberConst.IntDef.INT_ELEVEN).getCell(0)
            .getCellStyle());
        mapCellStyle.put(CellStyleConst.CS_COMMA_DECIMAL_6,
            sheetStyle.getRow(NumberConst.IntDef.INT_THIRTEEN).getCell(0).getCellStyle());

        if (digits == NumberConst.IntDef.INT_ONE) {
            retStyle = mapCellStyle.get(CellStyleConst.CS_COMMA_DECIMAL_1);
        } else if (digits == NumberConst.IntDef.INT_TWO) {
            retStyle = mapCellStyle.get(CellStyleConst.CS_COMMA_DECIMAL_2);
        } else if (digits == NumberConst.IntDef.INT_THREE) {
            retStyle = mapCellStyle.get(CellStyleConst.CS_COMMA_DECIMAL_3);
        } else if (digits == NumberConst.IntDef.INT_FOUR) {
            retStyle = mapCellStyle.get(CellStyleConst.CS_COMMA_DECIMAL_4);
        } else if (digits == NumberConst.IntDef.INT_FIVE) {
            retStyle = mapCellStyle.get(CellStyleConst.CS_COMMA_DECIMAL_5);
        } else if (digits == NumberConst.IntDef.INT_SIX) {
            retStyle = mapCellStyle.get(CellStyleConst.CS_COMMA_DECIMAL_6);
        } else {
            retStyle = mapCellStyle.get(CellStyleConst.CS_COMMA_DECIMAL_0);
        }
        return retStyle;
    }

    /**
     * Get template cells from template excel file.
     * 
     * @param sheetName the sheet name
     * @param templateRowNum the template row number
     * @param workbook the workbook (SXSSFSheet)
     * @return template row styles
     */
    protected Cell[] getTemplateCells(String sheetName, int templateRowNum, Workbook workbook) {
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
     * Get template cell from template excel file.
     * 
     * @param sheetName the sheet name
     * @param templateRowNum the template row number
     * @param templateColNum the template column number
     * @param workbook the workbook (SXSSFSheet)
     * @return template cell styles
     */
    protected Cell getTemplateCell(String sheetName, int templateRowNum, int templateColNum, Workbook workbook) {
        Cell cell = null;

        if (workbook != null) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                // exception when sheet is not found
                BaseMessage message = new BaseMessage("The sheet [{}] is not found.");
                message.setMessageArgs(new String[] { sheetName });
                throw new DownloadException(message);
            }

            cell = sheet.getRow(templateRowNum).getCell(templateColNum);
        }
        return cell;
    }

    /**
     * set specify style by data
     * 
     * @param cells cells
     * @param decimalStyle decimalStyle
     */
    protected void setStyleByData(Cell[] cells, CellStyle decimalStyle) {
        // set specify style by data
        ;
    };

    /**
     * Create data rows from start row.
     * The start row is template row, include cells style.
     * 
     * @param workbook the workbook (SXSSFSheet)
     * @param sheetName the sheet name
     * @param startRownum the start row number
     * @param templateCells template cells
     * @param datas the datas those will be output (datas are formated)
     * @throws BusinessException the exception
     */
    protected static void createDataRowsByTemplate(SXSSFWorkbook workbook, String sheetName, int startRownum,
        Cell[] templateCells, List<Object[]> datas) throws BusinessException {
        if (workbook != null && datas != null && datas.size() > 0) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                // exception when sheet is not found
                BaseMessage message = new BaseMessage(MessageCodeConst.W1008_002);
                message.setMessageArgs(new String[] { sheetName });
                throw new DownloadException(message);
            }

            for (int i = 0; i < datas.size(); i++) {
                int rowNum = startRownum + i;
                createOneDataRowByTemplate(sheet, rowNum, templateCells, datas.get(i));
            }

            // force formula recalculation
            workbook.setForceFormulaRecalculation(true);
        }
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
     * @throws BusinessException the exception
     */
    protected static void createOneDataRowByTemplate(Sheet sheet, int rowNum, Cell[] templateCells, Object[] data)
        throws BusinessException {
        // if (sheet != null && data != null) {
        //
        // // output data rows
        // int templateCellCount = 0;
        // if (templateCells != null) {
        // templateCellCount = templateCells.length;
        // }
        //
        // if (data != null) {
        // int rowItemCount = data.length;
        // // if (rowItemCount != templateCellCount && templateCellCount >
        // // 0) {
        // // // exception when data count is invalid
        // // BaseMessage message = new BaseMessage(
        // // "The data's column number is not sample as template's column number.[row:{}]");
        // //
        // // message.setMessageArgs(new String[] { String.valueOf(i + 1)
        // // });
        // // throw new DownloadException(message);
        // // }
        //
        // // create new data row
        // Row dataRow = sheet.createRow(rowNum);
        // for (int j = 0; j < rowItemCount; j++) {
        // // create cell
        // Cell cell = dataRow.createCell(j);
        //
        // // set cell format, formula, type
        // if (templateCells != null && j < templateCellCount && null != templateCells[j]) {
        // cell.setCellStyle(templateCells[j].getCellStyle());
        // int cellType = templateCells[j].getCellType();
        // cell.setCellType(cellType);
        // if (Cell.CELL_TYPE_FORMULA == cellType) {
        // cell.setCellFormula(templateCells[j].getCellFormula());
        // }
        // }
        //
        // // set cell value
        // Object cellValue = data[j];
        // if (cellValue == null) {
        // // nothing to set
        // ;
        // } else if (cellValue instanceof Date) {
        // cell.setCellValue((Date) cellValue);
        // } else if (cellValue instanceof Calendar) {
        // cell.setCellValue((Calendar) cellValue);
        // } else if (cellValue instanceof String) {
        // cell.setCellValue(cellValue.toString());
        // } else if (cellValue instanceof Boolean) {
        // cell.setCellValue(((Boolean) cellValue));
        // } else if (cellValue instanceof BigDecimal) {
        // cell.setCellValue(((BigDecimal) cellValue).doubleValue());
        // } else if (cellValue instanceof Integer) {
        // cell.setCellValue(((Integer) cellValue).doubleValue());
        // } else if (cellValue instanceof Double) {
        // cell.setCellValue(((Double) cellValue).doubleValue());
        // } else {
        // cell.setCellValue(String.valueOf(cellValue));
        // }
        // }
        // }
        // }

        createOneDataRowByTemplate(sheet, rowNum, 0, templateCells, data);
    }

    /**
     * Create data rows from start row.
     * The start row is template row, include cells style.
     * templateCells's type is Decimal,set cell style with
     * parameter(decimalStyle)
     * 
     * @param sheet the sheet
     * @param rowNum the row number
     * @param colNum the column number
     * @param templateCells template cells
     * @param data the data those will be output (data are formated)
     * @throws BusinessException the exception
     */
    protected static void createOneDataRowByTemplate(Sheet sheet, int rowNum, int colNum, Cell[] templateCells,
        Object[] data) throws BusinessException {
        if (sheet != null && data != null) {

            // output data rows
            int templateCellCount = 0;
            if (templateCells != null) {
                templateCellCount = templateCells.length;
            }

            if (data != null) {
                int rowItemCount = data.length;
                // if (rowItemCount != templateCellCount && templateCellCount >
                // 0) {
                // // exception when data count is invalid
                // BaseMessage message = new BaseMessage(
                // "The data's column number is not sample as template's column number.[row:{}]");
                //
                // message.setMessageArgs(new String[] { String.valueOf(i + 1)
                // });
                // throw new DownloadException(message);
                // }

                // create new data row
                Row dataRow = sheet.getRow(rowNum);
                if (dataRow == null) {
                    dataRow = sheet.createRow(rowNum);
                }
                for (int j = colNum; j < colNum + rowItemCount; j++) {
                    // create cell
                    Cell cell = dataRow.getCell(j);
                    if (cell == null) {
                        cell = dataRow.createCell(j);
                    }

                    int dataIndex = j - colNum;

                    // set cell format, formula, type
                    if (templateCells != null && dataIndex < templateCellCount && null != templateCells[dataIndex]) {
                        cell.setCellStyle(templateCells[dataIndex].getCellStyle());
                        int cellType = templateCells[dataIndex].getCellType();
                        cell.setCellType(cellType);
                        if (Cell.CELL_TYPE_FORMULA == cellType) {
                            cell.setCellFormula(templateCells[dataIndex].getCellFormula());
                        }
                    }

                    // set cell value
                    Object cellValue = data[dataIndex];
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
     * Output the excel to response.
     * 
     * @param clientFileName the file name for client
     * @param workbook the excel
     * @param request the request
     * @param response the response
     * @throws IOException IO exception
     */
    protected void outputDownloadExcel(String clientFileName, Workbook workbook, HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        // response content type
        response.setContentType(DEFAULT_CONTENT_TYPE);

        // response character encoding
        response.setCharacterEncoding(ENCODE);

        // response Content-disposition
        response.setHeader("Content-disposition",
            StringUtil.formatMessage("attachment; filename=\"{0}\"", encodeClientFileName(clientFileName, request)));

        OutputStream ouputStream = null;
        try {
            ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
        } finally {
            if (ouputStream != null) {
                try {
                    ouputStream.flush();
                    ouputStream.close();
                } catch (Exception e) {}
            }
        }
    }

    /**
     * Encode client file name by browser.
     * 
     * @param clientFileName the file name for client
     * @param request the request
     * @return encoded file name
     */
    protected String encodeClientFileName(String clientFileName, HttpServletRequest request) {
        String encodedFileName = clientFileName;

        try {
            String userAgent = request.getHeader("USER-AGENT");
            Boolean flag = userAgent.indexOf("like Gecko") > 0;
            if (StringUtil.contains(userAgent, "MSIE") || flag) {
                // IE
                encodedFileName = URLEncoder.encode(encodedFileName, "UTF8");
            } else if (StringUtil.contains(userAgent, "Mozilla")) {
                // google, firefox
                encodedFileName = new String(encodedFileName.replaceAll(" ", "").getBytes("UTF8"), "ISO8859-1");
            } else {
                // other
                encodedFileName = URLEncoder.encode(encodedFileName, "UTF8");
            }
        } catch (Exception e) {
            logger.warn("Encode file name fail.", e);
        }
        String temp = encodedFileName.replace("+", " ");
        return temp;
    }

    /**
     * <pre>
     * The process for upload file.
     * . check file size
     * . check file suffix
     * . check file format (static contents)
     * . save upload file to server
     * . save M_Uploaded_File table for upload file
     * . return the upload file
     * </pre>
     * 
     * @param file the upload file
     * @param fileType the file type
     * @param param the parameter
     * @param <T> the parameter class type
     * @param <S> the result class type
     * @param request the request
     * @param response the response
     * @return the result message
     * @exception UploadException the upload exception
     */
    protected <T extends BaseParam, S extends BaseEntity> BaseResult<S> uploadFileProcess(MultipartFile file,
        FileType fileType, T param, HttpServletRequest request, HttpServletResponse response) throws UploadException {

        BaseResult<S> baseResult = new BaseResult<S>();

        if (param.getUploadProcess() != null
                && (StringUtil.equals(param.getUploadProcess(), UploadConst.UPLOAD_PROCESS_CONFIRMED) || StringUtil
                    .equals(param.getUploadProcess(), UploadConst.UPLOAD_PROCESS_UNCONFIRMED))) {
            try {
                doExcelUploadProcess(null, null, param, request);
            } catch (BusinessException e) {
                throw e;
            } catch (Exception e) {
                throw new UploadException(MessageCodeConst.E0004, e);
            }

            return baseResult;
        }

        // check file exist
        if (file == null) {
            throw new UploadException(MessageCodeConst.W1001_002);
        }

        // check file name
        String originalFilename = file.getOriginalFilename();
        if (StringUtil.isEmpty(originalFilename)) {
            throw new UploadException(MessageCodeConst.W1001_001);
        }

        // check file empty
        if (file.getSize() == 0) {
            throw new UploadException(MessageCodeConst.W1001_003);
        }

        // check file suffix
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (fileType != null && !fileType.matchIgnoreCase(suffix)) {
            if (FileType.EXCEL.equals(fileType)) {
                throw new UploadException(MessageCodeConst.W1001_004);
            } else if (FileType.ZIP.equals(fileType)) {
                throw new UploadException(MessageCodeConst.W1001_009);
            }
        }

        // the max size of upload file
        int unit = NumberConst.IntDef.CUSTOMER_AUTO_GROW_COLLECTION_LIMIT;
        if (ConfigManager.getUploadFileMaxSize() >= 0 && file.getSize() > ConfigManager.getUploadFileMaxSize()) {
            throw new UploadException(new BaseMessage(MessageCodeConst.W1001_010,
                new String[] { Long.toString(ConfigManager.getUploadFileMaxSize() / unit / unit) + "MB" }));
        }

        // for excel file, check file format (static contents) by excel
        // configurations
        String fileId = this.getFileId();
        if (FileType.EXCEL.equals(fileType)) {
            // open workbook
            Workbook workbook = null;
            try {
                workbook = ExcelUtil.getWorkBook(file.getInputStream());

                // check static items by configuration
                List<BaseMessage> messages = this.checkStaticItems(fileId, workbook, param);
                if (messages != null && !messages.isEmpty()) {
                    UploadException ex = new UploadException(MessageCodeConst.W1004);
                    ex.setMessageList(messages);
                    throw ex;
                }

                messages = doExcelUploadProcess(file, workbook, param, request);
                if (messages != null && !messages.isEmpty()) {
                    UploadException ex = new UploadException(MessageCodeConst.W1004);
                    ex.setMessageList(messages);
                    throw ex;
                }

            } catch (BusinessException e) {
                throw e;
            } catch (Exception e) {
                throw new UploadException(MessageCodeConst.E0004, e);
            } finally {
                if (workbook != null) {
                    try {
                        workbook.close();
                    } catch (IOException e) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Excel workbook close fail.", e);
                        }
                    }
                }
            }
        }

        // save upload file to server
        // save M_Uploaded_File table for upload file
        // return the upload file

        return baseResult;
    }

    /**
     * <pre>
     * The process for upload file.
     * . check file size
     * . check file suffix
     * . check file format (static contents)
     * . save upload file to server
     * . save M_Uploaded_File table for upload file
     * . return the upload file
     * </pre>
     * 
     * @param file the upload file
     * @param fileType the file type
     * @param param the parameter
     * @param <T> the parameter class type
     * @param <S> the result class type
     * @param request the request
     * @param response the response
     * @return the result message
     * @exception UploadException the upload exception
     */
    protected <T extends BaseParam, S extends BaseEntity> BaseResult<S> uploadFileCheck(MultipartFile file,
        FileType fileType, T param, HttpServletRequest request, HttpServletResponse response) throws UploadException {

        BaseResult<S> baseResult = new BaseResult<S>();

        if (param.getUploadProcess() != null
                && (StringUtil.equals(param.getUploadProcess(), UploadConst.UPLOAD_PROCESS_CONFIRMED) || StringUtil
                    .equals(param.getUploadProcess(), UploadConst.UPLOAD_PROCESS_UNCONFIRMED))) {
            try {
                doExcelUploadProcess(null, null, param, request);
            } catch (BusinessException e) {
                throw e;
            } catch (Exception e) {
                throw new UploadException(MessageCodeConst.E0004, e);
            }

            return baseResult;
        }

        // check file exist
        if (file == null) {
            throw new UploadException(MessageCodeConst.W1001_002);
        }

        // check file name
        String originalFilename = file.getOriginalFilename();
        if (StringUtil.isEmpty(originalFilename)) {
            throw new UploadException(MessageCodeConst.W1001_001);
        }

        // check file empty
        if (file.getSize() == 0) {
            throw new UploadException(MessageCodeConst.W1001_003);
        }

        // check file suffix
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (fileType != null && !fileType.matchIgnoreCase(suffix)) {
            if (FileType.EXCEL.equals(fileType)) {
                throw new UploadException(MessageCodeConst.W1001_004);
            } else if (FileType.ZIP.equals(fileType)) {
                throw new UploadException(MessageCodeConst.W1001_009);
            }
        }

        // the max size of upload file
        int unit = NumberConst.IntDef.CUSTOMER_AUTO_GROW_COLLECTION_LIMIT;
        if (ConfigManager.getUploadFileMaxSize() >= 0 && file.getSize() > ConfigManager.getUploadFileMaxSize()) {
            throw new UploadException(new BaseMessage(MessageCodeConst.W1001_010,
                new String[] { Long.toString(ConfigManager.getUploadFileMaxSize() / unit / unit) + "MB" }));
        }

        // save upload file to server
        // save M_Uploaded_File table for upload file
        // return the upload file

        return baseResult;
    }

    /**
     * check static Items.
     * 
     * @param fileId fileId
     * @param workbook workbook
     * @param param the parameter
     * @param <T> the parameter class type
     * @return messages
     */
    protected <T extends BaseParam> List<BaseMessage> checkStaticItems(String fileId, Workbook workbook, T param) {
        return ExcelConfigManager.checkStaticItems(fileId, workbook);
    }

    /**
     * Check the static items in uploaded excel file by excel configurations.
     * 
     * @param fileId the file id
     * @exception UploadException the upload exception
     */
    protected void checkUploadedExcelStaticItems(String fileId) throws UploadException {
        // get the excel file configurations by file id.
        Properties configs = ConfigUtil.get();
        if (configs != null) {
            Enumeration<?> keys = configs.propertyNames();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                if (key.toString().startsWith(fileId + ".upload.static.")) {
                    ConfigUtil.get(key.toString());
                }
            }
        }
    }

    /**
     * Process uploaded excel.
     * * Nothing is done in base class.
     * * Implement process in sub class.
     * 
     * @param file uploaded file
     * @param workbook uploaded excel
     * @param param the parameters
     * @param <T> the parameter class type
     * @param request HttpServletRequest
     * @return result message
     * @throws Exception the Exception
     */
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook,
        T param, HttpServletRequest request) throws Exception {
        // do nothing in base
        logger.debug("readProcessExcel in BaseFileController");
        return null;
    }

    // /**
    // * Save upload file to upload temp path.
    // *
    // * @param uploadFile
    // * @param userInfo
    // * @return File
    // * @throws IOException
    // *
    // * @author wang_jinghui
    // */
    // protected File saveUploadFileToTemp(MultipartFile uploadFile, UserInfo
    // userInfo) throws IOException {
    // // save file name:refType_refNO_Result_yyyyMMddHHmmss.suffix
    // String currentTime =
    // DateTimeUtil.formatDateAllowsNull(DateTimeUtil.getSysDBDatetime(),
    // DateTimeUtil.FORMAT_IN_FILE_NAME);
    //
    // String originalFilename = uploadFile.getOriginalFilename();
    // String suffix =
    // originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    // String saveFileName = StringUtil.formatMessage("{0}_upload.{1}",
    // currentTime, suffix);
    //
    // // save file to server
    // File file = null;
    // file = getUploadFilePath(saveFileName, userInfo);
    // BufferedOutputStream output = new BufferedOutputStream(new
    // FileOutputStream(file));
    // FileCopyUtils.copy(uploadFile.getInputStream(), output);
    // return file;
    // }
    //
    // /**
    // *
    // * <p>
    // * Download the fixed file to client.
    // * </p>
    // *
    // * @param input the download file
    // * @param fileName the file name for client
    // * @param request the request
    // * @param response the response
    // * @throws Exception the exception when download
    // *
    // * @author guo_yong
    // */
    // protected void downloadFixedFile(String fullPath, String fileName,
    // HttpServletRequest request,
    // HttpServletResponse response) throws Exception {
    // File file = new File(fullPath);
    // download(new FileInputStream(file), fileName, DEFAULT_CONTENT_TYPE,
    // request, response);
    // }
    //
    // /**
    // *
    // * Compress excel file into zip file.
    // *
    // * @param zipOut zip output stream
    // * @param workbook excel workbook
    // * @param fileName file name
    // * @throws Exception Exception
    // */
    // protected void compressFile(ZipArchiveOutputStream zipOut, XSSFWorkbook
    // workbook, String fileName) throws
    // Exception {
    // // create temp file
    // File file = File.createTempFile("temp_", fileName);
    // InputStream in = null;
    // try {
    // FileOutputStream tempFile = FileUtils.openOutputStream(file);
    // workbook.write(tempFile);
    //
    // ZipArchiveEntry zipEntity = new ZipArchiveEntry(fileName);
    // zipOut.putArchiveEntry(zipEntity);
    //
    // // write excel content into zip
    // in = new FileInputStream(file);
    // IOUtils.copy(in, zipOut, 2048);
    // zipOut.closeArchiveEntry();
    // } finally {
    // if (in != null) {
    // in.close();
    // }
    // // delete temp file
    // file.delete();
    // }
    // }

    /**
     * Set Upload Result.
     * 
     * @param request the request
     * @param response the response
     * @param result the response result
     * @throws Exception the exception
     */
    protected void setUploadResult(HttpServletRequest request, HttpServletResponse response, BaseResult<?> result)
        throws Exception {
        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        if (userAgent.indexOf("msie") > 0 || userAgent.indexOf("trident") > 0) {
            response.setContentType("text/json; charset=UTF-8");
        } else {
            response.setContentType("application/json; charset=UTF-8");
        }

        JSONObject node = JSONObject.fromObject(result);
        response.getWriter().println(node);
    }

    /**
     * Copy row.
     * 
     * @param srcSheet source sheet
     * @param distSheet destination sheet
     * @param srcFromRow the from row which is copy object
     * @param rows the rows which is copy object
     * @param srcFromColumn the from column which is copy object
     * @param columns the columns which is copy object
     * @param distFromRowColumn distFromRowColumn[0]:the from row of destination, distFromRowColumn[1]: the from column
     *        of destination
     * @param copyValueFlag true:copy value/false:do not copy value
     * @return map(row number : cell[])
     */
    protected HashMap<Integer, Cell[]> copyRowByFrom(Sheet srcSheet, Sheet distSheet, int srcFromRow, int rows,
        int srcFromColumn, int columns, int[] distFromRowColumn, boolean copyValueFlag) {

        // int distFromRow = distFromRowColumn[0];
        // int distFromColumn = distFromRowColumn[1];
        //
        // for (int i = srcFromRow; i < srcFromRow + rows; i++) {
        // int distFromColumnTemp = distFromColumn;
        // for (int j = srcFromColumn; j < srcFromColumn + columns; j++) {
        // copyCell(srcSheet, distSheet, i, j, distFromRow, distFromColumnTemp, copyValueFlag);
        // distFromColumnTemp++;
        // }
        // distFromRow++;
        // }

        return copyRowByFromTo(srcSheet, distSheet, srcFromRow, srcFromRow + rows, srcFromColumn, srcFromColumn
                + columns, distFromRowColumn, copyValueFlag);
    }

    /**
     * Copy row.
     * 
     * @param srcSheet source sheet
     * @param distSheet destination sheet
     * @param srcFromRow the from row which is copy object
     * @param srcToRow the to row which is copy object
     * @param srcFromColumn the from column which is copy object
     * @param srcToColumn the to column which is copy object
     * @param distFromRowColumn distFromRowColumn[0]:the from row of destination, distFromRowColumn[1]: the from column
     *        of destination
     * @param copyValueFlag true:copy value/false:do not copy value
     * @return map(row number : cell[])
     */
    protected HashMap<Integer, Cell[]> copyRowByFromTo(Sheet srcSheet, Sheet distSheet, int srcFromRow, int srcToRow,
        int srcFromColumn, int srcToColumn, int[] distFromRowColumn, boolean copyValueFlag) {
        HashMap<Integer, Cell[]> map = new HashMap<Integer, Cell[]>();

        int distFromRow = distFromRowColumn[0];
        int distFromColumn = distFromRowColumn[1];

        for (int i = srcFromRow; i < srcToRow; i++) {
            Cell[] cells = new Cell[srcToColumn - srcFromColumn];

            int distFromColumnTemp = distFromColumn;
            for (int j = srcFromColumn; j < srcToColumn; j++) {
                cells[j - srcFromColumn] = copyCell(srcSheet, distSheet, i, j, distFromRow, distFromColumnTemp,
                    copyValueFlag);
                distFromColumnTemp++;
            }

            map.put(srcFromRow, cells);
            distFromRow++;
        }

        return map;
    }

    /**
     * Copy column.
     * 
     * @param srcSheet source sheet
     * @param distSheet destination sheet
     * @param srcFromRow the from row which is copy object
     * @param rows the rows which is copy object
     * @param srcFromColumn the from column which is copy object
     * @param columns the columns which is copy object
     * @param distFromRowColumn distFromRowColumn[0]:the from row of destination, distFromRowColumn[1]: the from column
     *        of destination
     * @param copyValueFlag true:copy value/false:do not copy value
     * @return map(column number : cell[])
     */
    protected HashMap<Integer, Cell[]> copyColumnByFrom(Sheet srcSheet, Sheet distSheet, int srcFromRow, int rows,
        int srcFromColumn, int columns, int[] distFromRowColumn, boolean copyValueFlag) {

        return copyColumnByFromTo(srcSheet, distSheet, srcFromRow, srcFromRow + rows, srcFromColumn, srcFromColumn
                + columns, distFromRowColumn, copyValueFlag);
    }

    /**
     * Copy column.
     * 
     * @param srcSheet source sheet
     * @param distSheet destination sheet
     * @param srcFromRow the from row which is copy object
     * @param srcToRow the to row which is copy object
     * @param srcFromColumn the from column which is copy object
     * @param srcToColumn the to column which is copy object
     * @param distFromRowColumn distFromRowColumn[0]:the from row of destination, distFromRowColumn[1]: the from column
     *        of destination
     * @param copyValueFlag true:copy value/false:do not copy value
     * @return map(column number : row[])
     */
    protected HashMap<Integer, Cell[]> copyColumnByFromTo(Sheet srcSheet, Sheet distSheet, int srcFromRow,
        int srcToRow, int srcFromColumn, int srcToColumn, int[] distFromRowColumn, boolean copyValueFlag) {
        HashMap<Integer, Cell[]> map = new HashMap<Integer, Cell[]>();

        int distFromRow = distFromRowColumn[0];
        int distFromColumn = distFromRowColumn[1];

        for (int i = srcFromColumn; i < srcToColumn; i++) {
            Cell[] cells = new Cell[srcToRow - srcFromRow];

            int distFromRowTemp = distFromRow;
            for (int j = srcFromRow; j < srcToRow; j++) {
                cells[j - srcFromRow] = copyCell(srcSheet, distSheet, j, i, distFromRowTemp, distFromColumn,
                    copyValueFlag);
                distFromRowTemp++;
            }

            map.put(srcFromColumn, cells);
            distFromRow++;
        }

        return map;
    }

    /**
     * Copy cell.
     * 
     * @param srcSheet source sheet
     * @param distSheet destination sheet
     * @param srcRow source row
     * @param srcColumn source column
     * @param distRow destination row
     * @param distColumn destination column
     * @param copyValueFlag true:copy value/false:do not copy value
     * @return destination cell
     */
    protected Cell copyCell(Sheet srcSheet, Sheet distSheet, int srcRow, int srcColumn, int distRow, int distColumn,
        boolean copyValueFlag) {
        Cell srcCell = srcSheet.getRow(srcRow).getCell(srcColumn);

        Row row = distSheet.getRow(distRow);
        if (row == null) {
            row = distSheet.createRow(distRow);
        }

        Cell cell = row.getCell(distColumn);
        if (cell == null) {
            cell = row.createCell(distColumn);
        }

        return copyCell(srcCell, cell, copyValueFlag);
    }

    /**
     * Copy cell.
     * 
     * @param srcCell source cell
     * @param distCell destination cell
     * @param copyValueFlag true:copy value/false:do not copy value
     * @return destination cell
     */
    protected Cell copyCell(Cell srcCell, Cell distCell, boolean copyValueFlag) {

        distCell.setCellComment(srcCell.getCellComment());
        distCell.setCellStyle(srcCell.getCellStyle());
        distCell.setCellType(srcCell.getCellType());
        if (copyValueFlag) {
            int cellType = srcCell.getCellType();
            if (cellType == Cell.CELL_TYPE_NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
                    distCell.setCellValue(srcCell.getDateCellValue());
                } else {
                    distCell.setCellValue(srcCell.getNumericCellValue());
                }
            } else if (cellType == Cell.CELL_TYPE_STRING) {
                distCell.setCellValue(srcCell.getRichStringCellValue());
            } else if (cellType == Cell.CELL_TYPE_BOOLEAN) {
                distCell.setCellValue(srcCell.getBooleanCellValue());
            } else if (cellType == Cell.CELL_TYPE_ERROR) {
                distCell.setCellErrorValue(srcCell.getErrorCellValue());
            } else if (cellType == Cell.CELL_TYPE_FORMULA) {
                distCell.setCellFormula(srcCell.getCellFormula());
            }
        }

        return distCell;
    }

    /**
     * Set data validation list
     * 
     * @param sheet Sheet
     * @param fromRow from row No.
     * @param toRow to row No.
     * @param fromCol from col No.
     * @param toCol to col No.
     * @param textlist data validation list
     * @param checkFlag true:do check/false:do not check
     */
    public static void setDataValidationList(Sheet sheet, int fromRow, int toRow, int fromCol, int toCol,
        String[] textlist, boolean checkFlag) {
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        CellRangeAddressList regions = new CellRangeAddressList(fromRow, toRow, fromCol, toCol);
        DataValidationConstraint constraint = dvHelper.createExplicitListConstraint(textlist);
        DataValidation dataValidation = dvHelper.createValidation(constraint, regions);
        if (checkFlag) {
            dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            dataValidation.createErrorBox("Error", "InValid Data.");
            dataValidation.setShowErrorBox(true);
        }
        sheet.addValidationData(dataValidation);
    }
}
