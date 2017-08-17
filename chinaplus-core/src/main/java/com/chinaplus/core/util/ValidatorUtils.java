/**
 * ValidatorUtils.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Sheet;

import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;

/**
 * The utils for validator.
 */
public final class ValidatorUtils {

    /** The regex for mail */
    private static String MAIL_REGEX = "^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /** The regex for number */
    private static String NUMBER_REGEX = "^(0|[1-9][0-9]*)$";

    /** The regex for decimal */
    private static String DECIMAL_REGEX = "^[-+]?[0-9]+(\\.[0-9]+)?$";

    /** The regex for phone */
    private static String PHONE_REGEX = "^(\\+[0-9]+|[0-9]+)([0-9]|-)+[0-9]$";

    /** filters */
    private static final String[] FILTERS = new String[] { ":", "：", ",", "，", "/", "\'", " ", "\\", "*", "[", "]", "?" };

    /** regex key words */
    private static final List<String> REGEX_KEY = Arrays.asList(new String[] { "\\", "$", "(", ")", "*", "+", ".", "[",
        "]", "?", "^", "{", "}", "|" });

    /** max qty */
    private static final String MAX_QTY = "9999999999.999999";

    /** max qty scale */
    private static final int MAX_QTY_SCALE = 6;

    /** default check rows */
    private static final int DEFAULT_CHECK_ROWS = 10;

    /**
     * Check the mail address.
     * 
     * @param mail mail address
     * @return check result (true is check ok)
     */
    public static boolean checkMail(String mail) {
        return RegexUtils.checkRegex(MAIL_REGEX, mail);
    }

    /**
     * Check is this a number.
     * 
     * @param num check target
     * @return check result (true is check ok)
     */
    public static boolean checkNumber(String num) {
        return RegexUtils.checkRegex(NUMBER_REGEX, num);
    }

    /**
     * Check is this a decimal.
     * 
     * @param value check target
     * @return check result (true is check ok)
     */
    public static boolean checkDecimal(String value) {
        return RegexUtils.checkRegex(DECIMAL_REGEX, value);
    }

    /**
     * Check is this a phone number.
     * 
     * @param value check target
     * @return check result (true is check ok)
     */
    public static boolean checkPhone(String value) {
        return RegexUtils.checkRegex(PHONE_REGEX, value);
    }

    /**
     * Check is this a date with format.
     * 
     * @param value check target
     * @param format date format
     * @return check result (true is check ok)
     */
    public static boolean checkDateStr(String value, String format) {

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        formatter.setLenient(false);

        try {

            formatter.format(formatter.parse(value));
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * Check is this a date with format.
     * 
     * @param value check target
     * @param format date format
     * @param local locale
     * @return check result (true is check ok)
     */
    public static boolean checkDateStr(String value, String format, Locale local) {

        SimpleDateFormat formatter = new SimpleDateFormat(format, local);

        formatter.setLenient(false);

        try {

            formatter.format(formatter.parse(value));
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * Check is this a date with year and month and day.
     * 
     * @param year year
     * @param month month
     * @param day day
     * @return check result (true is check ok)
     */
    public static boolean isLegalDate(int year, int month, int day) {

        String monthStr = Integer.toString(month);
        String dayStr = Integer.toString(day);
        if (month < IntDef.INT_TEN) {
            monthStr = "0" + monthStr;
        }
        if (day < IntDef.INT_TEN) {
            dayStr = "0" + dayStr;
        }
        String dtStr = year + "-" + monthStr + "-" + dayStr;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(dtStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = sdf.format(d);
        // check the value before convert and the value after converted
        return result.equals(dtStr);
    }

    /**
     * Do required validation.
     * 
     * @param value validate value
     * @return true is check ok
     */
    public static boolean requiredValidator(Object value) {

        boolean result = true;
        if (value == null) {
            result = false;
        } else if (value instanceof String && StringUtil.isEmpty((String) value)) {
            result = false;
        }
        return result;
    }

    /**
     * Check number.
     * 
     * @param value validate value
     * @param required true is required
     * @return true is check ok
     */
    public static boolean numberValidator(String value, boolean required) {

        boolean result = true;
        if (StringUtil.isEmpty(value) && !required) {
            return true;
        } else if (required) {
            result = requiredValidator(value);
        }
        if (result && !ValidatorUtils.checkNumber(value)) {
            result = false;
        }
        return result;
    }

    /**
     * Check decimal.
     * 
     * @param value validate value
     * @param required true is required
     * @return true is check ok
     */
    public static boolean decimalValidator(String value, boolean required) {

        boolean result = true;
        if (StringUtil.isEmpty(value) && !required) {
            return true;
        } else if (required) {
            result = requiredValidator(value);
        }
        if (result && !ValidatorUtils.checkDecimal(value)) {
            result = false;
        }
        return result;
    }

    /**
     * Check euqals with contrast value.
     * 
     * @param value validate value
     * @param contrastValue the value to contrast
     * @return true is check ok
     */
    public static boolean compareValidator(BigDecimal value, BigDecimal contrastValue) {

        boolean result = true;
        if (value == null) {
            if (contrastValue == null) {
                return true;
            } else {
                result = false;
            }
        } else {
            if (contrastValue == null) {
                result = false;
            } else if (value.compareTo(contrastValue) != 0) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Check euqals with contrast value.
     * 
     * @param value validate value
     * @param contrastValue the value to contrast
     * @return true is check ok
     */
    public static boolean euqalsValidator(Object value, Object contrastValue) {

        boolean result = true;
        if (value == null) {
            if (contrastValue == null) {
                return true;
            } else {
                result = false;
            }
        } else {
            if (contrastValue == null) {
                result = false;
            } else if (!value.equals(contrastValue)) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Check maximum length.
     * 
     * @param value validate value
     * @param maxLength the maxinum length
     * @return true is check ok
     */
    public static boolean maxLengthValidator(String value, int maxLength) {

        if (value == null) {
            return true;
        } else {
            if (value.getBytes().length > maxLength) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check the minimum value.
     * 
     * @param value validate value
     * @param minValue the minimum value
     * @return true is check ok
     */
    public static boolean minValidator(BigDecimal value, BigDecimal minValue) {

        if (value == null) {
            return true;
        } else {
            if (minValue == null) {
                return true;
            } else if (value.compareTo(minValue) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check tha value section with the maximum and the minimum.
     * 
     * @param value validate value
     * @param maxValue the minimum value
     * @param minValue the minimum value
     * @return true is check ok
     */
    public static boolean betweenValidator(BigDecimal value, BigDecimal maxValue, BigDecimal minValue) {

        if (maxValue == null || minValue == null) {
            return true;
        } else if (value == null) {
            return true;
        } else {
            if (value.compareTo(minValue) == -1 || value.compareTo(maxValue) == 1) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * While the input string contains special character (, \ / [ ] : * etc) will add error message.
     * When rowNum is 0 and sheetName is empty then add W1009_004 else add W1008_008.
     * 
     * @param value the input String
     * @return boolean
     */
    public static boolean validSpecialCharacter(String value) {

        boolean result = true;
        String containsChars = isContainsFilter(value);
        if (!StringUtil.isEmpty(containsChars)) {
            result = false;
        }
        return result;
    }

    /***
     * is the input String has key word
     * 
     * @param inputStr the input String
     * @return contains
     */
    private static String isContainsFilter(String inputStr) {

        StringBuilder result = new StringBuilder();
        Pattern pattern = null;
        Matcher matcher = null;
        int length = FILTERS.length;
        for (int i = 0; i < length; i++) {
            String key = FILTERS[i];
            if (REGEX_KEY.contains(key)) {
                key = key.replace(key, "\\" + key);
            }
            pattern = Pattern.compile(key);
            matcher = pattern.matcher(inputStr);
            if (matcher.find()) {
                result.append(FILTERS[i]).append(StringConst.BLANK);
            }
        }
        return result.toString();
    }

    /**
     * Check the decimal maximum value.
     * 
     * @param value validate value
     * @return check result
     */
    public static boolean checkMaxDecimal(BigDecimal value) {

        return checkMaxDecimal(value, new BigDecimal(MAX_QTY), MAX_QTY_SCALE);
    }

    /**
     * Check the decimal maximum value.
     * 
     * @param value validate value
     * @param maxValue the max decimal value
     * @param scale the scale length
     * @return check result
     */
    public static boolean checkMaxDecimal(BigDecimal value, BigDecimal maxValue, int scale) {

        if (value == null) {
            return true;
        }

        BigDecimal chkValue = value.abs();
        if (maxValue == null) {
            if (chkValue.scale() > scale) {
                return false;
            }
        } else if (chkValue.compareTo(maxValue) > 0 || chkValue.scale() > scale) {
            return false;
        }

        return true;
    }

    /**
     * Check current row whether is blank row.
     * 
     * @param workSheet the excel workSheet
     * @param rowIndex the check row index
     * @param detailStartCol the detail start column number
     * @param detailTotalCol the detail total column number
     * @return check result
     */
    public static boolean isBlankRow(Sheet workSheet, int rowIndex, int detailStartCol, int detailTotalCol) {

        for (int colIndex = detailStartCol; colIndex <= detailStartCol + detailTotalCol; colIndex++) {
            if (!StringUtil.isEmpty(PoiUtil.getStringCellValue(workSheet, rowIndex, colIndex))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check current column whether is blank row.
     * 
     * @param workSheet the excel workSheet
     * @param colIndex the check column index
     * @param detailStartRow the detail start row number
     * @param detailTotalRow the detail total row number
     * @return check result
     */
    public static boolean isBlankCol(Sheet workSheet, int colIndex, int detailStartRow, int detailTotalRow) {

        for (int rowIndex = detailStartRow; rowIndex <= detailStartRow + detailTotalRow; rowIndex++) {
            if (!StringUtil.isEmpty(PoiUtil.getStringCellValue(workSheet, rowIndex, colIndex))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check current row whether is blank row.
     * 
     * @param workSheet the excel workSheet
     * @param rowIndex the check row index
     * @param detailStartCol the detail start column number
     * @param detailEndCol the detail end column number
     * @return check result
     */
    public static boolean isBlankRowFromTo(Sheet workSheet, int rowIndex, int detailStartCol, int detailEndCol) {

        for (int colIndex = detailStartCol; colIndex <= detailEndCol; colIndex++) {
            if (!StringUtil.isEmpty(PoiUtil.getStringCellValue(workSheet, rowIndex, colIndex))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check current column whether is blank row.
     * 
     * @param workSheet the excel workSheet
     * @param colIndex the check column index
     * @param detailStartRow the detail start row number
     * @param detailEndRow the detail end row number
     * @param skipRowNo skip row No.
     * @return check result
     */
    public static boolean isBlankColFromTo(Sheet workSheet, int colIndex, int detailStartRow, int detailEndRow,
        int... skipRowNo) {

        for (int rowIndex = detailStartRow; rowIndex <= detailEndRow; rowIndex++) {
            boolean isSkip = false;
            for (int skipIndex : skipRowNo) {
                if (rowIndex == skipIndex) {
                    isSkip = true;
                    break;
                }
            }

            if (!isSkip && !StringUtil.isEmpty(PoiUtil.getStringCellValue(workSheet, rowIndex, colIndex))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check excel is end or not.
     * 
     * @param workSheet the excel workSheet
     * @param startRowIndex the start row index
     * @param startCol the detail start column number
     * @param totalCol the detail total column number
     * @return check result
     */
    public static boolean isExcelEnd(Sheet workSheet, int startRowIndex, int startCol, int totalCol) {

        return isExcelEnd(workSheet, startRowIndex, DEFAULT_CHECK_ROWS, startCol, totalCol);
    }

    /**
     * Check excel is end or not.
     * 
     * @param workSheet the excel workSheet
     * @param startRowIndex the start row index
     * @param checkRows the need check row number
     * @param startCol the detail start column number
     * @param totalCol the detail total column number
     * @return check result
     */
    public static boolean isExcelEnd(Sheet workSheet, int startRowIndex, int checkRows, int startCol, int totalCol) {

        for (int i = startRowIndex; i < startRowIndex + checkRows; i++) {
            if (!isBlankRow(workSheet, i, startCol, totalCol)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check half char.
     * 
     * @param str validate value
     * @return check result
     */
    public static boolean isHalf(String str) {

        boolean ret = false;
        if (StringUtil.isEmpty(str)) {
            return true;
        }
        ret = str.matches("[ -~]+");
        return ret;
    }

    /**
     * Check Alphameric.
     * 
     * @param str validate value
     * @return check result
     */
    public static boolean isAlphameric(String str) {

        boolean ret = false;
        if (StringUtil.isEmpty(str)) {
            return true;
        }
        ret = str.matches("^[a-zA-Z0-9]+$");
        return ret;
    }

    /**
     * Check Percentage.
     * 
     * @param str validate value
     * @return check result
     */
    public static boolean isPercentage(String str) {

        boolean ret = false;
        if (StringUtil.isEmpty(str)) {
            return true;
        }
        ret = str.matches("^-?\\d+\\.?\\d*\\%?$");
        return ret;
    }

}
