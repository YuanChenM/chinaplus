/**
 * ExcelFileOperation.java
 * 
 * @screen
 * @author tang_jianxin
 */
package com.chinaplus.core.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.exception.BusinessException;

/**
 * ExcelFileOperation.
 */
public class ExcelUtil {

    /** cellIndex */
    public static final String CELL_INDEX = "cellIndex";
    /** cellValue */
    public static final String CELL_VALUE = "cellValue";
    /** cellNumberValue */
    public static final String CELL_NUMBER_VALUE = "cellNumberValue";
    /** cellDateValue */
    public static final String CELL_DATE_VALUE = "cellDateValue";
    /** cellType */
    public static final String CELL_TYPE = "cellType";
    /** date */
    public static final String CELL_TYPE_DATE = "date";
    /** string */
    public static final String CELL_TYPE_STRING = "string";
    /** rowIndex */
    public static final String ROW_INDEX = "rowIndex";
    /** rowValue */
    public static final String ROW_VALUE = "rowValue";
    /** sheetName */
    public static final String SHEET_NAME = "sheetName";
    /** sheetValue */
    public static final String SHEET_VALUE = "sheetValue";

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    Workbook wb;

    List<String[]> dataList = new ArrayList<String[]>();

    // /**
    // * Main
    // *
    // * @param args param
    // */
    // public static void main(String[] args) {
    // System.out.println(colIndexToStr(1));
    // System.out.println(colStrToNum("AA"));
    // }

    /**
     * The Constructors Method.
     *
     * @param inp input stream
     */
    public ExcelUtil(InputStream inp) {

        try {

            // create new work book.
            wb = WorkbookFactory.create(inp);
        } catch (FileNotFoundException e) {
            throw new BusinessException(MessageCodeConst.W1001_002, e);
        } catch (InvalidFormatException e) {
            throw new BusinessException(MessageCodeConst.W1001_003);
        } catch (IllegalArgumentException e) {
            // Your InputStream was neither an OLE2 stream, nor an OOXML stream
            throw new BusinessException(MessageCodeConst.W1001_003);
        } catch (Exception e) {
            throw new BusinessException(MessageCodeConst.W1001_003);
        }
    }

    // public Map<String, String> readExcelContent(int sheetCount) {
    // Map<String, String> content = new HashMap<String, String>();
    // String str = "";
    // Sheet sheet = wb.getSheetAt(sheetCount);
    // int rowNum = sheet.getLastRowNum();
    // Row row = sheet.getRow(0);
    // int colNum = row.getPhysicalNumberOfCells();
    //
    // content.put("0", rowNum + "," + colNum);
    //
    // for (int i = 0; i <= rowNum; i++) {
    // row = sheet.getRow(i);
    // int j = 0;
    // while (j < colNum) {
    // str = getCellFormatValue(row.getCell(j)).trim();
    // j++;
    // if (str != null && !str.equals(""))
    // content.put(i + "," + j, str);
    // }
    //
    // str = "";
    // }
    // return content;
    // }
    //
    // public Map<String, String> readExcelContent(String sheetName, int colms, int rows) {
    // Map<String, String> content = new HashMap<String, String>();
    // String str = "";
    // Sheet sheet = wb.getSheet(sheetName);
    // if (sheet != null) {
    // int rowNum = sheet.getLastRowNum();
    // Row row = sheet.getRow(0);
    // int colNum = colms;
    // if (colms == 0)
    // colNum = row.getPhysicalNumberOfCells();
    // content.put("0", rowNum + "," + colNum);
    // int rowCount = 0;
    // for (int i = 0; i <= rowNum; i++) {
    // int columCount = 0;
    // row = sheet.getRow(i);
    // if (row != null) {
    // int j = 0;
    // while (j < colNum) {
    // if (row.getCell(j) != null && !row.getCell(j).toString().equals("")) {
    // rowCount = 0;
    // str = getCellFormatValue(row.getCell(j)).trim();
    // } else if (i > rows)
    // columCount++;
    // j++;
    // if (str != null && !str.equals("")) {
    // if (str.equals(YanmarConst.TIPS.LEGENDS))
    // return content;
    // else
    // content.put(i + "," + j, str);
    // }
    // if (i > rows && columCount == colms)
    // rowCount++;
    // str = "";
    // }
    // }
    //
    // if (i > rows) {
    // if (row == null)
    // rowCount++;
    // }
    // if (rowCount >= 10)
    // break;
    // }
    // }
    // return content;
    // }
    //
    // public Map<String, String> readExcelContent(String sheetName, int colms, int rows, int endColms, int endRows) {
    // Map<String, String> content = new HashMap<String, String>();
    // String str = "";
    // Sheet sheet = wb.getSheet(sheetName);
    // if (sheet != null) {
    // int rowNum = endRows;
    // Row row = sheet.getRow(0);
    // int colNum = endColms;
    // content.put("0", rowNum + "," + colNum);
    // for (int i = 0; i <= rowNum; i++) {
    // row = sheet.getRow(i);
    // if (row != null) {
    // for (int j = 0; j < colNum; j++) {
    // if (row.getCell(j) != null && !row.getCell(j).toString().equals("")) {
    // str = getCellFormatValue(row.getCell(j)).trim();
    // }
    // if (str != null && !str.equals("")) {
    // if (str.equals(YanmarConst.TIPS.LEGENDS))
    // return content;
    // else
    // content.put(i + "," + (j + 1), str);
    // }
    // str = "";
    // }
    // }
    // }
    // }
    // return content;
    // }

    // private String getCellFormatValue(Cell cell) {
    // String cellvalue = "";
    // if (cell != null) {
    // switch (cell.getCellType()) {
    // case Cell.CELL_TYPE_NUMERIC:
    // try {
    // if (HSSFDateUtil.isCellDateFormatted(cell)) {
    // Date date = cell.getDateCellValue();
    // SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.FORMAT_DATE_DDMMYYYY);
    // cellvalue = sdf.format(date);
    // } else {
    // // cell.setCellType(Cell.CELL_TYPE_NUMERIC);
    // cellvalue = String.valueOf(cell.getNumericCellValue());
    // }
    // } catch (Exception e) {
    //
    // // will exception
    // // cellvalue = String.valueOf(cell.getNumericCellValue());
    // cellvalue = "";
    // }
    // break;
    // case Cell.CELL_TYPE_FORMULA:
    // try {
    // cellvalue = String.valueOf(cell.getStringCellValue());
    // } catch (IllegalStateException e) {
    // try {
    // if (HSSFDateUtil.isCellDateFormatted(cell)) {
    // Date date = cell.getDateCellValue();
    // SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.FORMAT_DATE_DDMMYYYY);
    // cellvalue = sdf.format(date);
    // } else {
    // cell.setCellType(Cell.CELL_TYPE_NUMERIC);
    // cellvalue = String.valueOf(cell.getNumericCellValue());
    // }
    // } catch (Exception ex) {
    //
    // // if FORMULA is others or error, set blank.
    // cellvalue = "";
    // }
    // }
    // break;
    //
    // case Cell.CELL_TYPE_STRING:
    // cellvalue = cell.getRichStringCellValue().getString();
    // break;
    // default:
    // cellvalue = "";
    // }
    // } else {
    // cellvalue = "";
    // }
    // return cellvalue;
    //
    // }
    //
    // /**
    // *
    // * <p>
    // * read a CSV.
    // * </p>
    // *
    // * @param filePath file Path
    // * @return CSV
    // */
    // public static List<String[]> readCSV(String filePath) {
    // List<String[]> csvData = new ArrayList<String[]>();
    // try {
    // // BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "GBK"));
    // BufferedReader br = new BufferedReader(new FileReader(filePath));
    // while (br.ready()) {
    // String line = br.readLine();
    // logger.info("read line: " + line);
    // if (line != null) {
    // String[] lineArray = line.split(",");
    // csvData.add(lineArray);
    // }
    // }
    // br.close();
    // } catch (FileNotFoundException e) {
    // logger.error("Not Found File:" + filePath);
    // } catch (Exception e) {
    // logger.error(e.getMessage());
    // }
    // return csvData;
    // }

    /**
     * 
     * <p>
     * read a Excel.
     * </p>
     * 
     * @param filePath file Path
     * @return Excel
     */
    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        try {
            InputStream inputStream = null;
            // InputStream inputStream = BaseFileController.getDownloadInputStream(filePath, null, "");
            wb = WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException e) {
            logger.error(e.getMessage());
        } catch (FileNotFoundException e) {
            logger.error("Not Found File:" + filePath);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return wb;
    }

    // /**
    // *
    // * <p>
    // * read a Excel Data.
    // * </p>
    // *
    // * @param filePath file Path
    // * @param detailBeginIndex the detail begin index
    // * @return Excel Data
    // */
    // public static List<Map<String, Object>> readExcelData(String filePath, int detailBeginIndex) {
    //
    // List<Map<String, Object>> sheets = new ArrayList<Map<String, Object>>();
    //
    // Workbook wb = readExcel(filePath);
    //
    // if (wb == null) {
    // throw new BusinessException();
    // } else {
    // sheets = readExcelDataDo(wb, detailBeginIndex);
    // }
    //
    // return sheets;
    // }

    // /**
    // *
    // * <p>
    // * read a Excel Data.
    // * </p>
    // *
    // * @param wb Workbook
    // * @param detailBeginIndex the detail begin index
    // * @return sheets
    // */
    // private static List<Map<String, Object>> readExcelDataDo(Workbook wb, int detailBeginIndex) {
    //
    // List<Map<String, Object>> sheets = new ArrayList<Map<String, Object>>();
    //
    // for (int i = 0; i < wb.getNumberOfSheets(); i++) {
    //
    // Sheet sheet = wb.getSheetAt(i);
    //
    // int rowLen = sheet.getLastRowNum() + 1;// sheet.getPhysicalNumberOfRows();
    //
    // logger.info("Sheet {} \"{}\" has {} row(s).", new Object[] { i, wb.getSheetName(i), rowLen });
    //
    // List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
    //
    // for (int j = 0; j < rowLen; j++) {
    // Row row = sheet.getRow(j);
    // if (row == null) {
    // if (j < detailBeginIndex) {
    // continue;
    // } else {
    // break;
    // }
    // }
    // int cellLen = row.getLastCellNum();// row.getPhysicalNumberOfCells();
    //
    // logger.info("ROW {} has {} cell(s).", row.getRowNum(), cellLen);
    //
    // List<Map<String, Object>> cells = new ArrayList<Map<String, Object>>();
    //
    // for (int k = 0; k < cellLen; k++) {
    // Cell cell = row.getCell(k);
    // if (cell == null) {
    // continue;
    // }
    //
    // String value = getStringCellValue(cell);
    //
    // String dateValue = getDateCellValue(cell);
    //
    // logger.info("CELL col={} VALUE={}", cell.getColumnIndex(), value);
    //
    // Map<String, Object> cellMap = new HashMap<String, Object>();
    // cellMap.put(CELL_INDEX, cell.getColumnIndex());
    // cellMap.put(CELL_VALUE, value);
    // cellMap.put(CELL_DATE_VALUE, dateValue);
    // cellMap.put(CELL_TYPE, CELL_TYPE_STRING);
    // cells.add(cellMap);
    // }
    // Map<String, Object> rowMap = new HashMap<String, Object>();
    // rowMap.put(ROW_INDEX, row.getRowNum());
    // rowMap.put(ROW_VALUE, cells);
    // rows.add(rowMap);
    // }
    // Map<String, Object> sheetMap = new HashMap<String, Object>();
    // sheetMap.put(SHEET_NAME, sheet.getSheetName());
    // sheetMap.put(SHEET_VALUE, rows);
    // sheets.add(sheetMap);
    // }
    // return sheets;
    // }

    // /**
    // *
    // * <p>
    // * get a String by Cell Value.
    // * </p>
    // *
    // * @param cell cell
    // * @return Cell Value
    // */
    // private static String getStringCellValue(Cell cell) {
    // if (cell == null) {
    // return "";
    // }
    // switch (cell.getCellType()) {
    // case Cell.CELL_TYPE_BLANK:
    // return "";
    // case Cell.CELL_TYPE_BOOLEAN:
    // if (cell.getBooleanCellValue()) {
    // return "TRUE";
    // } else {
    // return "FALSE";
    // }
    // case Cell.CELL_TYPE_STRING: {
    // if (cell.getStringCellValue() != null) {
    // return cell.getStringCellValue().trim();
    // } else {
    // return "";
    // }
    // }
    // case Cell.CELL_TYPE_NUMERIC:
    // if (DateUtil.isCellDateFormatted(cell)) {
    // SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    // return sdf.format(cell.getDateCellValue());
    // }
    // return NumberToTextConverter.toText(cell.getNumericCellValue());
    // case Cell.CELL_TYPE_FORMULA: {
    // if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
    // if (DateUtil.isCellDateFormatted(cell)) {
    // SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    // return sdf.format(cell.getDateCellValue());
    // }
    // return NumberToTextConverter.toText(cell.getNumericCellValue());
    // } else if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING) {
    // return cell.getStringCellValue();
    // } else {
    // return cell.getCellFormula();
    // }
    // }
    // default:
    // return "";
    // }
    // }

    // /**
    // *
    // * <p>
    // * get a Date by Cell Value.
    // * </p>
    // *
    // * @param cell cell
    // * @return cell value
    // */
    // private static String getDateCellValue(Cell cell) {
    // if (cell == null) {
    // return "";
    // }
    // String strCell = "";
    // switch (cell.getCellType()) {
    // case Cell.CELL_TYPE_STRING:
    // strCell = cell.getRichStringCellValue().getString();
    // break;
    // case Cell.CELL_TYPE_NUMERIC:
    // Calendar dateCell = Calendar.getInstance();
    // /*
    // * dateCell.setTime(cell.getDateCellValue()); strCell = (dateCell.get(Calendar.YEAR)) + "-" +
    // * (dateCell.get(Calendar.MONTH) + 1) + "-" + dateCell.get(Calendar.DATE);
    // * 2013/4/24 for NullpointException when value is too long
    // */
    // Date dateCellValue = cell.getDateCellValue();
    // if (dateCellValue != null) {
    // dateCell.setTime(cell.getDateCellValue());
    // strCell = (dateCell.get(Calendar.YEAR)) + "-" + (dateCell.get(Calendar.MONTH) + 1) + "-"
    // + dateCell.get(Calendar.DATE);
    // }
    // break;
    // default:
    // strCell = "";
    // break;
    // }
    //
    // if (StringUtil.isEmpty(strCell)) {
    // return "";
    // } else {
    // return strCell;
    // }
    // }
    //
    // /**
    // *
    // * <p>
    // * create a CSV.
    // * </p>
    // *
    // * @param csvData csv Data
    // * @param filePath file Path
    // */
    // public static void createCSV(List<String[]> csvData, String filePath) {
    // try {
    // // BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "GBK"));
    // BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
    //
    // if (csvData != null && !csvData.isEmpty()) {
    // for (String[] lineArray : csvData) {
    // StringBuffer line = new StringBuffer(YanmarConst.LINE_NUM);
    // for (String lineStr : lineArray) {
    // line.append(lineStr).append(",");
    // }
    // bw.write(line.toString());
    // logger.info("line: " + line);
    // bw.newLine();
    // }
    // }
    // bw.close();
    // } catch (Exception e) {
    // logger.error(e.getMessage());
    // }
    // }
    //
    // /**
    // *
    // * <p>
    // * create a Excel.
    // * </p>
    // *
    // * @param wb Workbook
    // * @param filePath file Path
    // */
    // public static void createExcel(Workbook wb, String filePath) {
    // FileOutputStream out = null;
    // try {
    // out = new FileOutputStream(filePath);
    // wb.write(out);
    // if (out != null) {
    // out.close();
    // }
    // } catch (FileNotFoundException e) {
    // logger.error("Not Found File:" + filePath);
    // } catch (Exception e) {
    // logger.error(e.getMessage());
    // }
    // }
    //
    // /**
    // *
    // * <p>
    // * create a Excel.
    // * </p>
    // *
    // * @param data data
    // * @param filePath file Path
    // */
    // public static void createExcel(List<Map<String, Object>> data, String filePath) {
    // HSSFWorkbook wb = new HSSFWorkbook();
    // FileOutputStream out = null;
    //
    // for (Map<String, Object> sheetMap : data) {
    // logger.info("Sheet: " + sheetMap.get(SHEET_NAME));
    // HSSFSheet sheet = wb.createSheet(sheetMap.get(SHEET_NAME).toString());
    // @SuppressWarnings("unchecked")
    // List<Map<String, Object>> rows = (List<Map<String, Object>>) sheetMap.get(SHEET_VALUE);
    // for (Map<String, Object> rowMap : rows) {
    // logger.info("row: " + rowMap.get(ROW_INDEX));
    // int rowIndex = Integer.parseInt(rowMap.get(ROW_INDEX).toString());
    // HSSFRow row = sheet.createRow(rowIndex);
    // @SuppressWarnings("unchecked")
    // List<Map<String, Object>> cells = (List<Map<String, Object>>) rowMap.get(ROW_VALUE);
    // for (Map<String, Object> cellMap : cells) {
    // logger.info("cell: {} value:{}", cellMap.get(CELL_INDEX), cellMap.get(CELL_VALUE));
    // int cellIndex = Integer.parseInt(cellMap.get(CELL_INDEX).toString());
    // HSSFCell newCell = row.createCell(cellIndex, Cell.CELL_TYPE_STRING);
    // if (CELL_TYPE_STRING.equals((String) cellMap.get(CELL_TYPE))) {
    // newCell.setCellValue(cellMap.get(CELL_VALUE).toString());
    // } else if (CELL_TYPE_DATE.equals((String) cellMap.get(CELL_TYPE))) {
    // newCell.setCellValue(cellMap.get(CELL_DATE_VALUE).toString());
    // }
    // }
    // }
    // }
    //
    // try {
    // out = new FileOutputStream(filePath);
    // wb.write(out);
    //
    // } catch (FileNotFoundException e) {
    // logger.error("Not Found File:" + filePath);
    // } catch (Exception e) {
    // logger.error(e.getMessage());
    // } finally {
    // if (out != null) {
    // try {
    // out.close();
    // } catch (Exception e) {
    // logger.error(e.getMessage());
    // }
    // }
    // }
    // }

    /**
     *
     * Get a workbook by template file.
     *
     * @param templateFile template file
     * @return workbook
     * @throws FileNotFoundException
     * @throws BusinessException the exception
     */
    public static Workbook getWorkBook(String templateFile) throws BusinessException {

        InputStream is = ExcelUtil.class.getResourceAsStream(templateFile);
        try {
            return WorkbookFactory.create(is);
        } catch (Exception e) {
            throw new BusinessException("Read the excel template file fail.", e);
        }
    }

    /**
     *
     * Get a workbook by template file.
     *
     * @param templateFile template file
     * @return workbook
     * @throws FileNotFoundException
     * @throws BusinessException the exception
     */
    public static XSSFWorkbook getXSSFWorkbook(String templateFile) throws BusinessException {

        InputStream is = ExcelUtil.class.getResourceAsStream(templateFile);
        try {
            return new XSSFWorkbook(is);
        } catch (Exception e) {
            throw new BusinessException("Read the excel template file fail.", e);
        }
    }

    /**
     *
     * Get a workbook from input stream.
     *
     * @param is input stream
     * @return workbook
     * @throws FileNotFoundException
     * @throws BusinessException the exception
     */
    public static Workbook getWorkBook(InputStream is) throws BusinessException {
        try {
            return WorkbookFactory.create(is);
        } catch (Exception e) {
            throw new BusinessException("Read the excel template file fail.", e);
        }
    }

    // /**
    // * Auto generate temp file with workbook.
    // *
    // *
    // * @param workbook workbook
    // * @param clientName
    // * @param suffux
    // * @return temp file name
    // * @throws Exception
    // */
    // public static String generateDownloadFile(Workbook workbook, String clientName, String suffux) throws Exception {
    //
    // File tempFile = File.createTempFile(clientName, suffux);
    // BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
    // workbook.write(bos);
    // bos.flush();
    // bos.close();
    // return tempFile.getName();
    // }

    /**
     * Excel column index to column string.
     * 
     * @param colIndex column index
     * @return column string
     */
    public static String colIndexToStr(int colIndex) {
        return CellReference.convertNumToColString(colIndex - 1);
    }

    /**
     * Excel column index to column string.
     * 
     * @param colIndex column index
     * @return column string
     */
    public static String colIndexToStr(String colIndex) {
        return colIndexToStr(Integer.parseInt(colIndex));
    }

    /**
     * Excel column string to column index.
     * 
     * @param colStr column string
     * @return column index
     */
    public static int colStrToNum(String colStr) {
        return CellReference.convertColStringToIndex(colStr) + 1;
    }
}
