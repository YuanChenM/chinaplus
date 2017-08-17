/**
 * PoiUtil.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.consts.NumberConst;

/**
 * POI operation util
 */
public class PoiUtil {

    // /** Amount 0 decimal */
    // private static final int AMOUNT_0 = 2;
    // /** Amount 1 decimal */
    // private static final int AMOUNT_1 = 3;
    // /** Amount 2 decimal */
    // private static final int AMOUNT_2 = 4;
    // /** Amount 3 decimal */
    // private static final int AMOUNT_3 = 5;
    // /** Amount 4 decimal */
    // private static final int AMOUNT_4 = 6;
    // /** Amount 5 decimal */
    // private static final int AMOUNT_5 = 7;
    // /** Amount 6 decimal */
    // private static final int AMOUNT_6 = 8;
    // /** Financial Amount 0 decimal */
    // private static final int FINANCIAL_AMOUNT_0 = 9;
    // /** Financial Amount 1 decimal */
    // private static final int FINANCIAL_AMOUNT_1 = 10;
    // /** Financial Amount 2 decimal */
    // private static final int FINANCIAL_AMOUNT_2 = 11;
    // /** Financial Amount 3 decimal */
    // private static final int FINANCIAL_AMOUNT_3 = 12;
    // /** Financial Amount 4 decimal */
    // private static final int FINANCIAL_AMOUNT_4 = 13;
    // /** Financial Amount 5 decimal */
    // private static final int FINANCIAL_AMOUNT_5 = 14;
    // /** Financial Amount 6 decimal */
    // private static final int FINANCIAL_AMOUNT_6 = 15;

    /**
     * replace message for the whole workbook
     * 
     * @param workbook workbook
     * @param keyPrefix the prefix of resource key
     */
    public static void replaceI18nMessage(Workbook workbook, String keyPrefix) {
        int sheetCount = workbook.getNumberOfSheets();
        for (int index = 0; index < sheetCount; index++) {
            Sheet sheet = workbook.getSheetAt(index);
            replaceI18nMessage(sheet, keyPrefix, null);
        }
    }

    /**
     * replace message for the whole workbook
     * 
     * @param workbook workbook
     * @param keyPrefix the prefix of resource key
     * @param locale locale
     */
    public static void replaceI18nMessage(Workbook workbook, String keyPrefix, Locale locale) {
        int sheetCount = workbook.getNumberOfSheets();
        for (int index = 0; index < sheetCount; index++) {
            Sheet sheet = workbook.getSheetAt(index);
            replaceI18nMessage(sheet, keyPrefix, locale);
        }
    }

    /**
     * replace message of the whole sheet
     * 
     * @param sheet sheet
     * @param keyPrefix the prefix of resource key
     * @param locale locale
     */
    public static void replaceI18nMessage(Sheet sheet, String keyPrefix, Locale locale) {
        for (int row = sheet.getFirstRowNum(); row <= sheet.getLastRowNum(); row++) {
            Row r = sheet.getRow(row);
            if (r != null) {
                for (int col = r.getFirstCellNum(); col <= r.getLastCellNum(); col++) {
                    replaceI18nMessage(sheet, row + 1, col + 1, keyPrefix, locale);
                }
            }
        }
    }

    /**
     * 
     * <p>
     * If cell value is not empty,replace cell value with message code.
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param keyPrefix the prefix of resource key
     * @param locale locale
     */
    public static void replaceI18nMessage(Sheet sheet, int x, int y, String keyPrefix, Locale locale) {
        String cellValue = getStringCellValue(sheet, x, y);
        String regex = "(^#\\{)(.*)(\\}$)";
        Locale cLocale = locale == null ? MessageManager.getLocale() : locale;
        if (!StringUtil.isEmpty(cellValue) && StringUtil.matchs(cellValue, regex)) {
            String resouceKey = StringUtil.formatMessage("{0}_{1}", keyPrefix, getMatchedContent(cellValue, regex));
            if (!MessageManager.containsKey(resouceKey, cLocale)) {
                resouceKey = StringUtil.formatMessage("{0}.{1}", keyPrefix, getMatchedContent(cellValue, regex));
            }
            
            // get values
            String messageValue = MessageManager.getMessage(resouceKey, cLocale);
            messageValue = messageValue.replaceAll("<br>", "\n");
            messageValue = messageValue.replaceAll("&nbsp;", " ");
            setCellValue(sheet, x, y, messageValue);
        }
    }

    /**
     * Get key pattern by regex.
     * 
     * @param str input string
     * @param regex the check regex
     * @return key pattern
     */
    private static String getMatchedContent(String str, String regex) {
        final int outIndex = 2;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        m.find();
        return m.group(outIndex);
    }

    // /**
    // *
    // * <p>
    // * set cell amount style by currency
    // * </p>
    // *
    // * @param from row
    // * @param to sheet
    // * @param x row
    // * @param y col
    // * @param financial boolean
    // * @param decimal int
    // * @return cell
    // */
    // public static Cell setCellAmountStyle(Row from, Sheet to, int x, int y, boolean financial, Integer decimal) {
    // if (decimal == null) {
    // return null;
    // }
    // Cell cell = getOrCreateCell(to, x, y);
    // if (financial) {
    // switch (decimal) {
    // case YanmarConst.IntDef.INT_ZERO:
    // copyCellStyle(from.getCell(FINANCIAL_AMOUNT_0), cell);
    // break;
    // case YanmarConst.IntDef.INT_ONE:
    // copyCellStyle(from.getCell(FINANCIAL_AMOUNT_1), cell);
    // break;
    // case YanmarConst.IntDef.INT_TWO:
    // copyCellStyle(from.getCell(FINANCIAL_AMOUNT_2), cell);
    // break;
    // case YanmarConst.IntDef.INT_THREE:
    // copyCellStyle(from.getCell(FINANCIAL_AMOUNT_3), cell);
    // break;
    // case YanmarConst.IntDef.INT_FOUR:
    // copyCellStyle(from.getCell(FINANCIAL_AMOUNT_4), cell);
    // break;
    // case YanmarConst.IntDef.INT_FIVE:
    // copyCellStyle(from.getCell(FINANCIAL_AMOUNT_5), cell);
    // break;
    // case YanmarConst.IntDef.INT_SIX:
    // copyCellStyle(from.getCell(FINANCIAL_AMOUNT_6), cell);
    // break;
    // }
    // } else {
    // switch (decimal) {
    // case YanmarConst.IntDef.INT_ZERO:
    // copyCellStyle(from.getCell(AMOUNT_0), cell);
    // break;
    // case YanmarConst.IntDef.INT_ONE:
    // copyCellStyle(from.getCell(AMOUNT_1), cell);
    // break;
    // case YanmarConst.IntDef.INT_TWO:
    // copyCellStyle(from.getCell(AMOUNT_2), cell);
    // break;
    // case YanmarConst.IntDef.INT_THREE:
    // copyCellStyle(from.getCell(AMOUNT_3), cell);
    // break;
    // case YanmarConst.IntDef.INT_FOUR:
    // copyCellStyle(from.getCell(AMOUNT_4), cell);
    // break;
    // case YanmarConst.IntDef.INT_FIVE:
    // copyCellStyle(from.getCell(AMOUNT_5), cell);
    // break;
    // case YanmarConst.IntDef.INT_SIX:
    // copyCellStyle(from.getCell(AMOUNT_6), cell);
    // break;
    // }
    // }
    // return cell;
    // }

    // /**
    // *
    // * <p>
    // * copy one cell
    // * </p>
    // *
    // * @param from cell
    // * @param to cell
    // */
    // private static void copyCell(Cell from, Cell to) {
    // if (from == null || to == null) {
    // return;
    // }
    // to.setCellType(from.getCellType());
    // to.setCellStyle(from.getCellStyle());
    // if (from.getCellType() == Cell.CELL_TYPE_NUMERIC) {
    // to.setCellValue(from.getNumericCellValue());
    // } else if (from.getCellType() == Cell.CELL_TYPE_STRING) {
    // RichTextString str = from.getRichStringCellValue();
    // if (str.length() > 0) {
    // to.setCellValue(str);
    // }
    // } else if (from.getCellType() == Cell.CELL_TYPE_FORMULA) {
    // to.setCellFormula(from.getCellFormula());
    // }
    // }
    //
    // /**
    // *
    // * <p>
    // * copy one cell
    // * </p>
    // *
    // * @param fromSheet sheet
    // * @param fromX fromRow
    // * @param fromY fromCol
    // * @param toSheet sheet
    // * @param toX toRow
    // * @param toY toCol
    // */
    // public static void copyCell(Sheet fromSheet, int fromX, int fromY, Sheet toSheet, int toX, int toY) {
    // copyCell(getCell(fromSheet, fromX, fromY), getOrCreateCell(toSheet, toX, toY));
    // }
    //
    // /**
    // *
    // * <p>
    // * copy one cell style without value
    // * </p>
    // *
    // * @param from cell
    // * @param to cell
    // */
    // private static void copyCellStyle(Cell from, Cell to) {
    // if (from == null || to == null) {
    // return;
    // }
    // // to.setCellType(from.getCellType());
    // to.setCellStyle(from.getCellStyle());
    // }
    //
    // /**
    // *
    // * <p>
    // * copy one cell style without value
    // * </p>
    // *
    // * @param fromSheet sheet
    // * @param fromX fromRow
    // * @param fromY fromCol
    // * @param toSheet sheet
    // * @param toX toRow
    // * @param toY toCol
    // */
    // public static void copyCellStyle(Sheet fromSheet, int fromX, int fromY, Sheet toSheet, int toX, int toY) {
    // copyCellStyle(getCell(fromSheet, fromX, fromY), getOrCreateCell(toSheet, toX, toY));
    // }
    //
    // /**
    // *
    // * <p>
    // * copy one row
    // * </p>
    // *
    // * @param from row
    // * @param to row
    // */
    // public static void copyRow(Row from, Row to) {
    // if (from == null || to == null) {
    // return;
    // }
    // // set Row Height
    // to.setHeight(from.getHeight());
    // // set cell
    // if (from instanceof HSSFRow) {
    // for (int i = 0; i < from.getLastCellNum() && i <= YanmarConst.EXCEL_2003_MAX_COLUMN_COUNT; i++) {
    // copyCell(from.getCell(i), to.createCell(i));
    // }
    // }
    // }
    //
    // /**
    // *
    // * <p>
    // * copy rows from one sheet to another
    // * </p>
    // *
    // * @param from sheet
    // * @param fromRowStart from start row
    // * @param fromRowEnd from end row
    // * @param to sheet
    // * @param toRowStart to start row
    // */
    // public static void copyRows(Sheet from, int fromRowStart, int fromRowEnd, Sheet to, int toRowStart) {
    // int startIndex = fromRowStart - 1;
    // int endIndex = fromRowEnd - 1;
    // int toIndex = toRowStart - 1;
    // for (int index = startIndex; index <= endIndex; index++) {
    //
    // Row fromRow = from.getRow(index);
    // Row fromTo = to.createRow(toIndex);
    //
    // copyRow(fromRow, fromTo);
    // int num = from.getNumMergedRegions();
    // for (int mergeIndex = 0; mergeIndex < num; mergeIndex++) {
    // CellRangeAddress r = from.getMergedRegion(mergeIndex);
    // if (index == r.getFirstRow()) {
    // int add = toIndex - r.getFirstRow();
    // CellRangeAddress r2 = new CellRangeAddress(r.getFirstRow() + add, r.getLastRow() + add,
    // r.getFirstColumn(), r.getLastColumn());
    // to.addMergedRegion(r2);
    // }
    // }
    // toIndex++;
    // }
    // }

    /**
     *
     * <p>
     * get row
     * </p>
     *
     * @param sheet sheet
     * @param row row(1,65536)
     * @return row
     */
    public static Row getRow(Sheet sheet, int row) {
        if (sheet instanceof HSSFSheet) {
            if (row < 1 || row > ChinaPlusConst.EXCEL_2003_MAX_ROW_COUNT) {
                return null;
            }
        } else {
            if (row < 1 || row > ChinaPlusConst.EXCEL_2007_MAX_ROW_COUNT) {
                return null;
            }
        }
        return sheet.getRow(row - 1);
    }

    /**
     * 
     * <p>
     * get cell
     * </p>
     * 
     * @param sheet sheet
     * @param x row(1,65536)
     * @param y column(1,256)
     * @return cell
     */
    public static Cell getCell(Sheet sheet, int x, int y) {
        if (sheet instanceof HSSFSheet) {
            if (x < 1 || x > ChinaPlusConst.EXCEL_2003_MAX_ROW_COUNT || y < 1
                    || y > ChinaPlusConst.EXCEL_2003_MAX_COLUMN_COUNT) {
                return null;
            }
        } else {
            if (x < 1 || x > ChinaPlusConst.EXCEL_2007_MAX_ROW_COUNT || y < 1
                    || y > ChinaPlusConst.EXCEL_2007_MAX_COLUMN_COUNT) {
                return null;
            }
        }

        Row row = sheet.getRow(x - 1);
        if (row == null) {
            return null;
        }
        Cell cell = row.getCell(y - 1);
        if (cell == null) {
            return null;
        }
        return cell;
    }

    /**
     * 
     * <p>
     * get or create cell
     * </p>
     * 
     * @param sheet sheet
     * @param x row(1,65536)
     * @param y column(1,256)
     * @return cell
     */
    public static Cell getOrCreateCell(Sheet sheet, int x, int y) {
        if (sheet instanceof HSSFSheet) {
            if (x < 1 || x > ChinaPlusConst.EXCEL_2003_MAX_ROW_COUNT || y < 1
                    || y > ChinaPlusConst.EXCEL_2003_MAX_COLUMN_COUNT) {
                return null;
            }
        } else {
            if (x < 1 || x > ChinaPlusConst.EXCEL_2007_MAX_ROW_COUNT || y < 1
                    || y > ChinaPlusConst.EXCEL_2007_MAX_COLUMN_COUNT) {
                return null;
            }
        }

        Row row = sheet.getRow(x - 1);
        if (row == null) {
            row = sheet.createRow(x - 1);
        }
        Cell cell = row.getCell(y - 1);
        if (cell == null) {
            cell = row.createCell(y - 1);
        }
        return cell;
    }

    /**
     * 
     * <p>
     * get number cell value
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @return number value
     */
    public static double getNumericCellValue(Sheet sheet, int x, int y) {
        Cell cell = getCell(sheet, x, y);
        if (cell == null) {
            return 0;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING: {
                try {
                    if (cell.getStringCellValue() != null) {
                        return Double.parseDouble(cell.getStringCellValue().trim());
                    } else {
                        return 0;
                    }
                } catch (NumberFormatException pe) {
                    return 0;
                }
            }
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_FORMULA: {
                if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
                    return cell.getNumericCellValue();
                } else if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING) {
                    try {
                        if (cell.getStringCellValue() != null) {
                            return Double.parseDouble(cell.getStringCellValue().trim());
                        } else {
                            return 0;
                        }
                    } catch (NumberFormatException pe) {
                        return 0;
                    }
                }
            }
            default:
                return 0;
        }
    }

    /**
     * 
     * <p>
     * get date cell value
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @return date value
     */
    public static Date getDateCellValue(Sheet sheet, int x, int y) {
        Cell cell = getCell(sheet, x, y);
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING: {
                // try {
                // DateFormat df = DateFormat.getDateInstance();
                // return df.parse(cell.getStringCellValue());
                // } catch (ParseException pe) {
                // return null;
                // }
                return DateTimeUtil.parseDate(cell.getStringCellValue(), DateTimeUtil.FORMAT_DDMMMYYYY);
            }
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isValidExcelDate(cell.getNumericCellValue())) {
                    return cell.getDateCellValue();
                }
            case Cell.CELL_TYPE_FORMULA:
                if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
                    if (DateUtil.isValidExcelDate(cell.getNumericCellValue())) {
                        return cell.getDateCellValue();
                    }
                }
            default:
                return null;
        }
    }

    /**
     * 
     * <p>
     * get date cell value
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param dateTimeFormat the format of date time
     * @return date value
     */
    public static Date getDateCellValue(Sheet sheet, int x, int y, String dateTimeFormat) {
        Cell cell = getCell(sheet, x, y);
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING: {
                try {
                    return DateTimeUtil.parseDate(cell.getStringCellValue(), dateTimeFormat);
                } catch (Exception e) {
                    return null;
                }
            }
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isValidExcelDate(cell.getNumericCellValue())) {
                    return cell.getDateCellValue();
                }
            case Cell.CELL_TYPE_FORMULA:
                if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
                    if (DateUtil.isValidExcelDate(cell.getNumericCellValue())) {
                        return cell.getDateCellValue();
                    }
                }
            default:
                return null;
        }
    }

    /**
     * 
     * <p>
     * get date cell value
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @return string value
     */
    public static String getStringCellValue(Sheet sheet, int x, int y) {
        Cell cell = getCell(sheet, x, y);
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                if (cell.getBooleanCellValue()) {
                    return "TRUE";
                } else {
                    return "FALSE";
                }
            case Cell.CELL_TYPE_STRING: {
                if (cell.getStringCellValue() != null) {
                    return cell.getStringCellValue().trim();
                } else {
                    return "";
                }
            }
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.FORMAT_DDMMMYYYY, Locale.ENGLISH);
                    return sdf.format(cell.getDateCellValue());
                }
                return NumberToTextConverter.toText(cell.getNumericCellValue());
            case Cell.CELL_TYPE_FORMULA: {
                if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.FORMAT_DDMMMYYYY, Locale.ENGLISH);
                        return sdf.format(cell.getDateCellValue());
                    }
                    return NumberToTextConverter.toText(cell.getNumericCellValue());
                } else if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING) {
                    return cell.getStringCellValue();
                } else {
                    return cell.getCellFormula();
                }
            }
            default:
                return "";
        }
    }

    // /**
    // * Check and get date value for upload excel value.
    // *
    // *
    // * @param messages the check result(contain error message)
    // * @param sheet the upload excel sheet
    // * @param row the row number
    // * @param col the column number
    // * @param compulsoryFalg the compulsory flag
    // * @param columnNameKey the column name key
    // * @return the date value
    // */
    // public static Date checkAndGetDateValue(List<UploadMessage> messages, HSSFSheet sheet, int row, int col,
    // boolean compulsoryFalg, String columnNameKey) {
    //
    // Cell cell = PoiUtil.getCell(sheet, row, col);
    // String dateCellValue = MasterExcelUtil.getStringCellValue(cell);
    // String columnName = SMCommonUtils.getErrMsg(columnNameKey);
    // Date dateValue = null;
    // if (StringUtil.isEmpty(dateCellValue)) {
    // if (compulsoryFalg) {
    // messages.add(new UploadMessage(row, SMCommonUtils.getErrMsg(ShipmentCodeDef.MessageCode.COMMON_E0017,
    // new Object[] { row, columnName }), UploadMessage.Type.ERROR));
    // }
    // } else {
    // Date d = MasterExcelUtil.getDateValue(dateCellValue);
    //
    // if (null == d) {
    // messages.add(new UploadMessage(row, SMCommonUtils.getErrMsg(ShipmentCodeDef.MessageCode.COMMON_E0020,
    // new Object[] { row, columnName }), UploadMessage.Type.ERROR));
    // } else {
    // Date exceldate = PoiUtil.getDateCellValue(sheet, row, col);
    // if (exceldate == null) {
    // messages.add(new UploadMessage(row, SMCommonUtils.getErrMsg(
    // ShipmentCodeDef.MessageCode.COMMON_E0020, new Object[] { row, columnName }),
    // UploadMessage.Type.ERROR));
    // } else {
    // String exceldateToStr = DateTimeUtil.formatDateTime(exceldate,
    // DateTimeUtil.FORMAT_YEAR_FOR_MONTH_DAY);
    // String regEx = "^(([1|2]\\d{3}))-([0-1]?\\d)-([0-3]?\\d)?$";
    // boolean rs = Pattern.compile(regEx).matcher(exceldateToStr).matches();
    // if (!rs) {
    // messages.add(new UploadMessage(row, SMCommonUtils.getErrMsg(
    // ShipmentCodeDef.MessageCode.COMMON_E0020, new Object[] { row, columnName }),
    // UploadMessage.Type.ERROR));
    // } else {
    // dateValue = exceldate;
    // }
    // }
    // }
    // }
    // return dateValue;
    // }

    // /**
    // * Check and get string value for upload excel value.
    // *
    // *
    // * @param messages the check result(contain error message)
    // * @param sheet the upload excel sheet
    // * @param row the row number
    // * @param col the column number
    // * @param compulsoryFalg the compulsory flag
    // * @param columnNameKey the column name key
    // * @param maxLength the max length
    // * @return the string value
    // */
    // public static String checkAndGetStringValue(List<UploadMessage> messages, HSSFSheet sheet, int row, int col,
    // boolean compulsoryFalg, String columnNameKey, int maxLength) {
    //
    // String stringValue = PoiUtil.getStringCellValue(sheet, row, col).trim();
    // String columnName = SMCommonUtils.getErrMsg(columnNameKey);
    //
    // if (StringUtil.isEmpty(stringValue)) {
    // if (compulsoryFalg) {
    // messages.add(new UploadMessage(row, SMCommonUtils.getErrMsg(ShipmentCodeDef.MessageCode.COMMON_E0017,
    // new Object[] { row, columnName }), UploadMessage.Type.ERROR));
    // }
    // } else if (stringValue.length() > maxLength) {
    // messages.add(new UploadMessage(row, SMCommonUtils.getErrMsg(ShipmentCodeDef.MessageCode.COMMON_E0019,
    // new Object[] { row, columnName, maxLength }), UploadMessage.Type.ERROR));
    // }
    // return stringValue;
    // }

    // /**
    // * Check and get number value for upload excel value.
    // *
    // *
    // * @param messages the check result(contain error message)
    // * @param sheet the upload excel sheet
    // * @param row the row number
    // * @param col the column number
    // * @param compulsoryFalg the compulsory flag
    // * @param columnNameKey the column name key
    // * @param maxLength the max length
    // * @param decimalsMaxLength the max decimal length
    // * @return the number value
    // */
    // public static BigDecimal checkAndGetBigDecimalValue(List<UploadMessage> messages, HSSFSheet sheet, int row,
    // int col, boolean compulsoryFalg, String columnNameKey, int maxLength, int decimalsMaxLength) {
    //
    // BigDecimal bigDecimalValue = null;
    // String columnName = SMCommonUtils.getErrMsg(columnNameKey);
    // String stringValue = PoiUtil.getStringCellValue(sheet, row, col).trim();
    //
    // if (StringUtil.isEmpty(stringValue)) {
    // if (compulsoryFalg) {
    // messages.add(new UploadMessage(row, SMCommonUtils.getErrMsg(ShipmentCodeDef.MessageCode.COMMON_E0017,
    // new Object[] { row, columnName }), UploadMessage.Type.ERROR));
    // }
    // } else {
    // // get cell's value
    // String strValue = stringValue.replaceAll(",", "");
    // BigDecimal value = MasterExcelUtil.getBigDecimalValue(strValue);
    //
    // if (null != value) {
    // // column's length check
    // String bigStr = String.valueOf(value);
    // if (bigStr.indexOf(".") > 0) {
    // String[] bigStrArray = bigStr.split("\\.");
    // int integerLength = maxLength - decimalsMaxLength;
    // if (bigStrArray[0].length() > integerLength || bigStrArray[1].length() > decimalsMaxLength) {
    // messages.add(new UploadMessage(row, SMCommonUtils.getErrMsg(
    // ShipmentCodeDef.MessageCode.COMMON_E0019, new Object[] { row, columnName,
    // maxLength + ", " + decimalsMaxLength }), UploadMessage.Type.ERROR));
    // } else {
    // bigDecimalValue = value;
    // }
    // } else {
    // if (bigStr.length() > maxLength) {
    // messages.add(new UploadMessage(row, SMCommonUtils.getErrMsg(
    // ShipmentCodeDef.MessageCode.COMMON_E0019, new Object[] { row, columnName,
    // maxLength + ", " + decimalsMaxLength }), UploadMessage.Type.ERROR));
    // } else {
    // bigDecimalValue = value;
    // }
    // }
    // } else {
    // messages.add(new UploadMessage(row, SMCommonUtils.getErrMsg(ShipmentCodeDef.MessageCode.COMMON_E0020,
    // new Object[] { row, columnName }), UploadMessage.Type.ERROR));
    // }
    // }
    //
    // return bigDecimalValue;
    //
    // }

    /**
     * 
     * <p>
     * set Data validation(2003 only)
     * </p>
     * 
     * @param sheet sheet
     * @param firstRow int
     * @param endRow int
     * @param firstCol int
     * @param endCol int
     * @param value Sting[]
     */
    public static void setDataValidation(Sheet sheet, int firstRow, int endRow, int firstCol, int endCol, String[] value) {
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(value);
        constraint.setExplicitListValues(value);
        CellRangeAddressList regions = new CellRangeAddressList(firstRow - 1, endRow - 1, firstCol - 1, endCol - 1);
        if (sheet instanceof HSSFSheet) {
            DataValidation dataValidation = new HSSFDataValidation(regions, constraint);
            sheet.addValidationData(dataValidation);
        }
    }

    /**
     * 
     * <p>
     * set Comment value(2003 only)
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param commetHeightInRow comment height in rows
     * @param commentWidthInCol comment width in columns
     * @param comment string
     * @return cell
     */
    public static Cell setCellComment(Sheet sheet, int x, int y, int commetHeightInRow, int commentWidthInCol,
        String comment) {
        Cell cell = getOrCreateCell(sheet, x, y);
        if (cell != null && comment != null) {
            if (sheet instanceof HSSFSheet) {
                HSSFSheet st = (HSSFSheet) sheet;
                if (st.getDrawingPatriarch() == null) {
                    st.createDrawingPatriarch();
                }
                HSSFPatriarch patr = st.getDrawingPatriarch();
                HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) y, x,
                    (short) (y + commentWidthInCol), x + commetHeightInRow);
                HSSFComment com = patr.createComment(anchor);
                com.setString(new HSSFRichTextString(comment));
                cell.setCellComment(com);
            }
        }
        return cell;
    }

    /**
     * 
     * <p>
     * set Comment value,default comment size:4rows*4cols(2003 only)
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param comment string
     * @return cell
     */
    public static Cell setCellComment(Sheet sheet, int x, int y, String comment) {
        return setCellComment(sheet, x, y, NumberConst.IntDef.INT_FOUR, NumberConst.IntDef.INT_FOUR, comment);
    }

    // /**
    // *
    // * <p>
    // * set cell number format
    // * </p>
    // *
    // * @param sheet sheet
    // * @param x row
    // * @param y column
    // * @param minus boolean
    // * @param decimal int
    // * @return cell
    // */
    // @Deprecated
    // public static Cell setCellDataFormat(Sheet sheet, int x, int y, boolean minus, Integer decimal) {
    // if (decimal == null) {
    // return setCellDataFormat(sheet, x, y, null);
    // }
    // String format = "#,##0";
    // if (decimal > 0) {
    // format = format + ".";
    // }
    // for (int i = 0; i < decimal; i++) {
    // format = format + "0";
    // }
    // if (minus) {
    // format = format + "_);[RED]\\(" + format + "\\)";
    // }
    // return setCellDataFormat(sheet, x, y, format);
    // }
    //
    // /**
    // *
    // * <p>
    // * set cell number format<br/>
    // * <b>format sample:</b> <br/>
    // * "#,##0.00;[Red]#,##0.00"<br/>
    // * "#,##0.0000_);[Red]\(#,##0.0000\)"<br/>
    // * "0.00_);\(0.00\)"<br/>
    // * "#,##0.00"<br/>
    // * "0.000"<br/>
    // * ...
    // * </p>
    // *
    // * @param sheet sheet
    // * @param x row
    // * @param y column
    // * @param format string
    // * @return cell
    // */
    // @Deprecated
    // public static Cell setCellDataFormat(Sheet sheet, int x, int y, String format) {
    // Cell cell = getOrCreateCell(sheet, x, y);
    // if (cell != null && format != null) {
    // DataFormat df = sheet.getWorkbook().createDataFormat();
    // if (cell.getCellStyle() == null) {
    // cell.setCellStyle(sheet.getWorkbook().createCellStyle());
    // }
    // CellStyle st = cell.getCellStyle();
    // st.setDataFormat(df.getFormat(format));
    // cell.setCellStyle(st);
    // }
    // return cell;
    // }

    /**
     * 
     * <p>
     * set Cell Style
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param cs CellStyle
     * @return
     */
    public static void setCellStyle(Sheet sheet, int x, int y, CellStyle cs) {
        Cell cell = getOrCreateCell(sheet, x, y);
        if (cell != null) {
            sheet.getRow(x - 1).getCell(y - 1).setCellStyle(cs);
        }
    }

    /**
     * 
     * <p>
     * set string value
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param strValue string
     * @return cell
     */
    public static Cell setCellValue(Sheet sheet, int x, int y, String strValue) {
        Cell cell = getOrCreateCell(sheet, x, y);
        if (cell != null && strValue != null) {
            if (sheet instanceof HSSFSheet) {
                RichTextString fts = new HSSFRichTextString(strValue);
                cell.setCellValue(fts);
            } else {
                RichTextString fts = new XSSFRichTextString(strValue);
                cell.setCellValue(fts);
            }
        }
        return cell;
    }

    /**
     * 
     * <p>
     * set boolean value
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param blValue boolean
     * @return cell
     * @return
     */
    public static Cell setCellValue(Sheet sheet, int x, int y, boolean blValue) {
        Cell cell = getOrCreateCell(sheet, x, y);
        if (cell != null) {
            cell.setCellValue(blValue);
        }
        return cell;
    }

    /**
     * 
     * <p>
     * set int value
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param nValue int
     * @return cell
     * @return
     */
    public static Cell setCellValue(Sheet sheet, int x, int y, int nValue) {
        Cell cell = getOrCreateCell(sheet, x, y);
        if (cell != null) {
            cell.setCellValue(nValue);
        }
        return cell;
    }

    /**
     * 
     * <p>
     * set Integer value
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param nValue Integer
     * @return cell
     * @return
     */
    public static Cell setCellValue(Sheet sheet, int x, int y, Integer nValue) {

        Cell cell = getOrCreateCell(sheet, x, y);
        if (cell != null && nValue != null) {
            cell.setCellValue(nValue);
        }
        return cell;
    }

    /**
     * 
     * <p>
     * set double value
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param dValue double
     * @return cell
     * @return
     */
    public static Cell setCellValue(Sheet sheet, int x, int y, double dValue) {
        Cell cell = getOrCreateCell(sheet, x, y);
        if (cell != null) {
            cell.setCellValue(dValue);
        }
        return cell;
    }

    /**
     * 
     * <p>
     * set date value
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param dValue date
     * @return cell
     * @return
     */
    public static Cell setCellValue(Sheet sheet, int x, int y, Date dValue) {
        Cell cell = getOrCreateCell(sheet, x, y);
        if (cell != null && dValue != null) {
            cell.setCellValue(dValue);
        }
        return cell;
    }

    /**
     * 
     * <p>
     * set BigDecimal value
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param bValue BigDecimal
     * @return cell
     * @return
     */
    public static Cell setCellValue(Sheet sheet, int x, int y, BigDecimal bValue) {
        Cell cell = getOrCreateCell(sheet, x, y);
        if (cell != null && bValue != null) {
            cell.setCellValue(bValue.doubleValue());
        }
        return cell;
    }

    /**
     * 
     * <p>
     * set formula
     * </p>
     * 
     * @param sheet sheet
     * @param x row
     * @param y column
     * @param strFormula formula
     * @return cell
     */
    public static Cell setCellFormula(Sheet sheet, int x, int y, String strFormula) {
        Cell cell = getOrCreateCell(sheet, x, y);
        if (cell != null && strFormula != null) {
            cell.setCellFormula(strFormula);
        }
        return cell;
    }

    /**
     * Set row height.
     * 
     * 
     * @param sheet the sheet
     * @param rowNo the row number
     * @param height the row height
     */
    public static void setRowHeight(Sheet sheet, int rowNo, int height) {

        if (sheet.getRow(rowNo - 1) == null) {
            sheet.createRow(rowNo - 1);
        }
        sheet.getRow(rowNo - 1).setHeight((short) (height * NumberConst.IntDef.INT_TWENTY));
    }

    // /**
    // *
    // * <p>
    // * create cell styles
    // * </p>
    // *
    // * @param wb workbook
    // * @return map<name,style>
    // */
    // @Deprecated
    // public static HashMap<String, CellStyle> createCellStyles(Workbook wb) {
    // DataFormat df = wb.createDataFormat();
    // HashMap<String, CellStyle> map = new HashMap<String, CellStyle>();
    // CellStyle stringST = wb.createCellStyle();
    // stringST.setDataFormat(df.getFormat("@"));
    // map.put("string", stringST);
    // CellStyle dateST = wb.createCellStyle();
    // dateST.setDataFormat(df.getFormat("dd-mmm-yyyy"));
    // map.put("date", dateST);
    // CellStyle qtyST = wb.createCellStyle();
    // qtyST.setDataFormat(df.getFormat("#,##0.00"));
    // map.put("qty", qtyST);
    // CellStyle amountST = wb.createCellStyle();
    // amountST.setDataFormat(df.getFormat("#,##0.00"));
    // map.put("amount", amountST);
    // CellStyle priceST = wb.createCellStyle();
    // priceST.setDataFormat(df.getFormat("#,##0.00000"));
    // map.put("price", priceST);
    // return map;
    // }

    // /**
    // * Get the COL name by COL number.
    // *
    // *
    // * @param colNo the column no.
    // * @return the COL name("A","B")
    // */
    // public static String getColNameByNo(int colNo) {
    // return CellReference.convertNumToColString(colNo - 1);
    // }
    //
    // /**
    // * Rename SheetName
    // *
    // * @param inputString
    // * @return sheetName
    // */
    // public static String renameSheetName(String inputString) {
    // StringBuffer sb = new StringBuffer(inputString);
    //
    // sb.deleteCharAt(1);
    // sb.deleteCharAt(sb.length() - 9);
    // sb.deleteCharAt(sb.length() - 4);
    // String sheetName = sb.toString();
    // return sheetName;
    // }

    // /**
    // * Copy a Sheet from another workBook's sheet. This is not a the best way to copy the Sheet, only suitable for
    // * simple sheet copy
    // *
    // * @param towb target workbook
    // * @param fromSheet copy src sheet
    // * @param toSheet copy target sheet
    // */
    // public static void copySheet(XSSFWorkbook towb, XSSFSheet fromSheet, XSSFSheet toSheet) {
    // // region row
    // int sheetMergerCount = fromSheet.getNumMergedRegions();
    // for (int i = 0; i < sheetMergerCount; i++) {
    // CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
    // toSheet.addMergedRegion(mergedRegionAt);
    // }
    // // copy row
    // for (Iterator<Row> rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
    // XSSFRow tmpRow = (XSSFRow) rowIt.next();
    // XSSFRow toRow = toSheet.createRow(tmpRow.getRowNum());
    // for (Iterator<Cell> cellIt = tmpRow.cellIterator(); cellIt.hasNext();) {
    // // copy cell
    // XSSFCell fromCell = (XSSFCell) cellIt.next();
    // XSSFCell toCell = toRow.createCell(fromCell.getColumnIndex());
    // XSSFCellStyle toStyle = towb.createCellStyle();
    // XSSFCellStyle fromStyle = fromCell.getCellStyle();
    // // copy CellStyle fromStyle, toStyle
    // toStyle.setAlignment(fromStyle.getAlignment());
    // toStyle.setBorderBottom(fromStyle.getBorderBottom());
    // toStyle.setBorderLeft(fromStyle.getBorderLeft());
    // toStyle.setBorderRight(fromStyle.getBorderRight());
    // toStyle.setBorderTop(fromStyle.getBorderTop());
    // toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
    // toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
    // toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
    // toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());
    // toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
    // toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());
    // toStyle.setDataFormat(fromStyle.getDataFormat());
    // Short f = fromStyle.getFillPattern();
    // // set the background to white,if you want to set another color ,you may be to override it
    // if (null != f && f != 0) {
    // XSSFColor color = new XSSFColor(DEFAULT_BK_COLOR);
    // toStyle.setFillBackgroundColor(color);
    // toStyle.setFillPattern(fromStyle.getFillPattern());
    // }
    //
    // // toStyle.setFont(fromStyle.getFont(null));
    // toStyle.setHidden(fromStyle.getHidden());
    // toStyle.setIndention(fromStyle.getIndention());
    // toStyle.setLocked(fromStyle.getLocked());
    // toStyle.setRotation(fromStyle.getRotation());
    // toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
    // toStyle.setWrapText(fromStyle.getWrapText());
    // toStyle.setFont(fromStyle.getFont());
    // toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());
    // toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
    // toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
    //
    // toCell.setCellStyle(toStyle);
    // if (fromCell.getCellComment() != null) {
    // toCell.setCellComment(fromCell.getCellComment());
    // }
    //
    // int srcCellType = fromCell.getCellType();
    // toCell.setCellType(srcCellType);
    // if (srcCellType == XSSFCell.CELL_TYPE_NUMERIC) {
    // toCell.setCellValue(fromCell.getNumericCellValue());
    // } else if (srcCellType == XSSFCell.CELL_TYPE_STRING) {
    // toCell.setCellValue(fromCell.getRichStringCellValue());
    // } else if (srcCellType == XSSFCell.CELL_TYPE_BLANK) {
    // // nothing to do
    // ;
    // } else if (srcCellType == XSSFCell.CELL_TYPE_BOOLEAN) {
    // toCell.setCellValue(fromCell.getBooleanCellValue());
    // } else if (srcCellType == XSSFCell.CELL_TYPE_ERROR) {
    // toCell.setCellErrorValue(fromCell.getErrorCellValue());
    // } else if (srcCellType == XSSFCell.CELL_TYPE_FORMULA) {
    // toCell.setCellFormula(fromCell.getCellFormula());
    // }
    // }
    // }
    // }

    /**
     * Copy a Sheet from another workBook's sheet. This is not a the best way to copy the Sheet, only suitable for
     * simple sheet copy
     *
     * @param workbook workbook
     * @param fromSheet copy src sheet in workbook
     * @param toSheet copy target sheet in workbook
     */
    public static void copySheet(Workbook workbook, Sheet fromSheet, Sheet toSheet) {
        // region row
        int sheetMergerCount = fromSheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
            toSheet.addMergedRegion(mergedRegionAt);
        }
        // copy row
        int lastRowNum = fromSheet.getLastRowNum();
        int maxCellNum = 0;
        for (int rowNum = 0; rowNum <= lastRowNum; rowNum++) {
            Row tmpRow = fromSheet.getRow(rowNum);
            if (tmpRow == null) {
                continue;
            }

            // create row
            Row toRow = toSheet.createRow(rowNum);
            toRow.setHeight(tmpRow.getHeight()); // row height
            int lastCellNum = tmpRow.getLastCellNum();
            // set max cell num
            if (lastCellNum > maxCellNum) {
                maxCellNum = lastCellNum;
            }
            for (int cellnum = 0; cellnum <= lastCellNum; cellnum++) {
                Cell tmpCell = tmpRow.getCell(cellnum);
                if (tmpCell == null) {
                    continue;
                }

                // create cell
                Cell toCell = toRow.createCell(cellnum);
                // set cell style
                toCell.setCellStyle(tmpCell.getCellStyle());
                if (tmpCell.getCellComment() != null) {
                    toCell.setCellComment(tmpCell.getCellComment());
                }
                // set cell type
                int srcCellType = tmpCell.getCellType();
                toCell.setCellType(srcCellType);
                // set cell value by type
                if (srcCellType == XSSFCell.CELL_TYPE_NUMERIC) {
                    toCell.setCellValue(tmpCell.getNumericCellValue());
                } else if (srcCellType == XSSFCell.CELL_TYPE_STRING) {
                    toCell.setCellValue(tmpCell.getRichStringCellValue());
                } else if (srcCellType == XSSFCell.CELL_TYPE_BLANK) {
                    // nothing to do
                    ;
                } else if (srcCellType == XSSFCell.CELL_TYPE_BOOLEAN) {
                    toCell.setCellValue(tmpCell.getBooleanCellValue());
                } else if (srcCellType == XSSFCell.CELL_TYPE_ERROR) {
                    toCell.setCellErrorValue(tmpCell.getErrorCellValue());
                } else if (srcCellType == XSSFCell.CELL_TYPE_FORMULA) {
                    toCell.setCellFormula(tmpCell.getCellFormula());
                }
            }
        }
        // set cell width
        for (int cellNum = 0; cellNum <= maxCellNum; cellNum++) {
            toSheet.setColumnWidth(cellNum, fromSheet.getColumnWidth(cellNum));
        }
    }

    /**
     * Remove specified sheet from workbook by sheet name.
     * Return true when removed success.
     * 
     * @param workbook the workbook object
     * @param sheetName the sheet name
     * @return true is removed success.
     */
    public static boolean removeSheet(Workbook workbook, String sheetName) {
        boolean result = false;

        if (workbook != null && !StringUtil.isEmpty(sheetName)) {
            int index = workbook.getSheetIndex(sheetName);
            if (index >= 0) {
                workbook.removeSheetAt(index);
                result = true;
            }
        }

        return result;
    }

}
