/**
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.util;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.chinaplus.common.util.MasterManager;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.consts.CoreConst.Direction;
import com.chinaplus.core.consts.CoreConst.LikeMode;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;

/**
 * The common utils for string.
 * 
 */
public class StringUtil {

    /** _100 */
    private static final int PERC_100 = 100;

    private static final String DIGIT_CHAR = "ABCDEFGHJKLMNPQRSTUVWXYZ";

    private static final int ORACLE_SQL_IN_MAX_LENGTH = 1000;

    /** NUM_FORMAT */
    private static final DecimalFormat NUM_FORMAT = new DecimalFormat("#,##0");

    /** QTY_FORMAT */
    private static final DecimalFormat QTY_FORMAT = new DecimalFormat("#,##0.000000");

    /** WEIGHT_FORMAT */
    private static final DecimalFormat WEIGHT_FORMAT = new DecimalFormat("#,##0.000");

    /** LENTH_FORMAT */
    private static final DecimalFormat LENGTH_FORMAT = new DecimalFormat("#,##0.00");

    /** DECIMAL0_FORMAT */
    private static final DecimalFormat DECIMAL0_FORMAT = new DecimalFormat("#,##0");

    /** DECIMAL1_FORMAT */
    private static final DecimalFormat DECIMAL1_FORMAT = new DecimalFormat("#,##0.0");

    /** DECIMAL2_FORMAT */
    private static final DecimalFormat DECIMAL2_FORMAT = new DecimalFormat("#,##0.00");

    /** DECIMAL3_FORMAT */
    private static final DecimalFormat DECIMAL3_FORMAT = new DecimalFormat("#,##0.000");

    /** DECIMAL4_FORMAT */
    private static final DecimalFormat DECIMAL4_FORMAT = new DecimalFormat("#,##0.0000");

    /** DECIMAL5_FORMAT */
    private static final DecimalFormat DECIMAL5_FORMAT = new DecimalFormat("#,##0.00000");

    /** DECIMAL6_FORMAT */
    private static final DecimalFormat DECIMAL6_FORMAT = new DecimalFormat("#,##0.000000");

    /** Default Trim Comparer. */
    private static final ITrimComparer DEFAULT_TRIM_COMPARER = new ITrimComparer() {

        /**
         * check if need trim.
         * 
         * @param ch char
         */
        public boolean isNeedTrim(char ch) {
            return ch <= ' ';
        }
    };

    /**
     * Check empty string
     * 
     * <pre>
     *   null: true
     *   "": true
     *   " ":true
     * </>
     * 
     * @param value a string value
     * @return true is a empty string
     */
    public static boolean isEmpty(String value) {
        boolean emptyFlg = false;
        if (null == value || value.trim().length() <= 0) {
            emptyFlg = true;
        }
        return emptyFlg;
    }

    /**
     * Check numeric string. If the string is numeric then return true.
     * 
     * @param value the string will be checked
     * @return true is numeric
     */
    public static boolean isNumeric(String value) {
        boolean isNumeric = false;

        String strValue = value;
        if (!isEmpty(strValue)) {
            if (strValue.startsWith("+") || strValue.startsWith("-")) {
                strValue = strValue.substring(1);
            }

            for (int i = strValue.length(); --i >= 0;) {
                if (!Character.isDigit(strValue.charAt(i))) {
                    return false;
                }
            }
            isNumeric = true;
        }

        return isNumeric;
    }

    /**
     * pad the pad to a string's right until full length.
     * 
     * @param value a string value
     * @param length pad to length
     * @param pad the pad string
     * @return a string
     */
    public static String PadRight(String value, int length, String pad) {

        String tmpStr = value;
        if (tmpStr == null) {
            tmpStr = "";
        }

        String tmpPad = "";

        if (tmpStr.length() < length) {
            for (int i = 1; i <= length - tmpStr.length(); i++) {
                tmpPad = tmpPad.concat(pad);
            }
        }

        return tmpStr.concat(tmpPad);
    }

    /**
     * pad the pad to a string's left until full length.
     * 
     * @param value a string value
     * @param length pad to length
     * @param pad the pad string
     * @return a string
     */
    public static String PadLeft(String value, int length, String pad) {

        String tmpStr = value;
        if (tmpStr == null) {
            tmpStr = "";
        }

        String tmpPad = "";

        if (tmpStr.length() < length) {
            for (int i = 1; i <= length - tmpStr.length(); i++) {
                tmpPad = pad.concat(tmpPad);
            }
        }

        return tmpPad.concat(tmpStr);
    }

    /**
     * <p>
     * Escapes the characters in a <code>String</code> using HTML entities.
     * </p>
     * 
     * <p>
     * For example:
     * </p>
     * <p>
     * <code>"bread" & "butter"</code>
     * </p>
     * becomes:
     * <p>
     * <code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code>.
     * </p>
     * 
     * <p>
     * Supports all known HTML 4.0 entities, including funky accents. Note that the commonly used apostrophe escape
     * character (&amp;apos;) is not a legal entity and so is not supported).
     * </p>
     * 
     * @param str the <code>String</code> to escape, may be null
     * @return a new escaped <code>String</code>, <code>null</code> if null string input
     * 
     * @see #unescapeHtml(String)
     * @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO Entities</a>
     * @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO Latin-1</a>
     * @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity references</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character References</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code positions</a>
     * 
     */
    public static String escapeHtml(String str) {
        return StringEscapeUtils.escapeHtml4(str);
        // HtmlUtils.htmlEscape(str);
    }

    /**
     * 
     * escapeHtmlWithSpace
     * 
     * @param str str
     * @return String
     * 
     */
    public static String escapeHtmlWithSpace(String str) {
        return StringEscapeUtils.escapeHtml4(str).replaceAll(" ", "&nbsp;");
    }

    /**
     * <p>
     * Escapes the characters in a <code>String</code> using JavaScript String rules.
     * </p>
     * <p>
     * Escapes any values it finds into their JavaScript String form. Deals correctly with quotes and control-chars
     * (tab, backslash, cr, ff, etc.)
     * </p>
     * 
     * <p>
     * So a tab becomes the characters <code>'\\'</code> and <code>'t'</code>.
     * </p>
     * 
     * <p>
     * The only difference between Java strings and JavaScript strings is that in JavaScript, a single quote must be
     * escaped.
     * </p>
     * 
     * <p>
     * Example:
     * 
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn\'t say, \"Stop!\"
     * </pre>
     * 
     * </p>
     * 
     * @param str String to escape values in, may be null
     * @return String with escaped values, <code>null</code> if null string input
     * 
     */
    public static String escapeJavaScript(String str) {
        return StringEscapeUtils.escapeEcmaScript(str);
    }

    /**
     * <pre>
     * Creates a MessageFormat with the given pattern and uses it to format the given arguments. 
     * This is equivalent to
     * <blockquote>
     * <code>(new {@link #MessageFormat(String) MessageFormat}(pattern)).{@link #format(java.lang.Object[], java.lang.StringBuffer, java.text.FieldPosition) format}(arguments, new StringBuffer(), null).toString()</code>
     * </blockquote>
     * ex:StringUtil.formatMessage("id:{0} name:{1} count:{2} flag:{3}", 1, "test", BigDecimal.TEN, true));
     *    StringUtil.formatMessage("''{0}''", "test"); => 'test'
     *    StringUtil.formatMessage("'{'{0}'}'", "test"); => {test}
     * </pre>
     * 
     * @param pattern the message pattern
     * @param arguments the arguments for message pattern
     * @return the message with
     * 
     */
    public static String formatMessage(String pattern, Object... arguments) {
        String message = "";

        if (arguments instanceof Object[]) {
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i] == null) {
                    arguments[i] = "";
                }
            }
        }

        try {
            message = MessageFormat.format(pattern.replaceAll("(?<!('|\\{|\\}))'(?!('|\\{|\\}))", "''"), arguments);
        } catch (Exception ex) {
            throw new BusinessException(MessageFormat.format("Format message[{0}] is fail.", pattern), ex);
        }

        return message;
    }

    /**
     * 
     * <p>
     * Convert parameter to String. When parameter is null, return null.
     * </p>
     * 
     * @param param any object value
     * @return a string value (null is possible)
     * 
     */
    public static String toString(Object param) {
        String value = null;

        if (param != null) {
            value = param.toString();
        }

        return value;
    }

    /**
     * 
     * <p>
     * Convert parameter to String. When parameter is null, return null.
     * </p>
     * 
     * @param param any object value
     * @return a string value (null is possible)
     * 
     */
    public static String toDecimalString(BigDecimal param) {
        String value = null;

        if (param != null) {
            value = param.toPlainString();
        }

        return value;
    }

    /**
     * 
     * <p>
     * Convert parameter to String. When parameter is null, return "".
     * </p>
     * 
     * @param param any object value
     * @return a safe string value (null is impossible)
     * 
     */
    public static String toSafeString(Object param) {
        String value = "";

        if (param != null) {
            value = param.toString();
        }

        return value;
    }

    /**
     * Convert a string array to String.
     * 
     * 
     * @param strArray a string array
     * @return the string of string array, split by comma.
     * 
     */
    public static String arrayToString(String[] strArray) {
        StringBuffer strReturn = new StringBuffer("");

        if (strArray != null) {
            for (String str : strArray) {
                if (strReturn.length() > 0) {
                    strReturn.append(StringConst.COMMA);
                    strReturn.append(" ");
                }
                strReturn.append(str);
            }
        }

        return strReturn.toString();
    }

    /**
     * 
     * <p>
     * Format amount base on decimal point of currency.
     * </p>
     * 
     * 
     * @param decimalPoint Integer
     * @param amount BigDecimal
     * @return amount
     */
    public static BigDecimal formatAmount(Integer decimalPoint, BigDecimal amount) {
        // if (decimalPoint == MosConst.IntDef.INT_ZERO) {
        // return amount.setScale(decimalPoint, BigDecimal.ROUND_HALF_UP);
        // } else {
        // double d = amount.doubleValue();
        // return BigDecimal.valueOf((long) (d * Math.pow(10d, decimalPoint) + 0.5d) / Math.pow(10d, decimalPoint));
        // }
        return amount.setScale(decimalPoint, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Format number as #,###
     * 
     * @param num a number
     * @return formated number string
     */
    public static String formatNum(BigDecimal num) {
        if (num == null) {
            return StringConst.EMPTY;
        }
        return NUM_FORMAT.format(num);
    }

    /**
     * Format number as #,###
     * 
     * @param num a number
     * @return formated number string
     */
    public static String formatNum(Integer num) {
        if (num == null) {
            return StringConst.EMPTY;
        }
        return NUM_FORMAT.format(num);
    }

    /**
     * Format quantity number as #,###.000000
     * 
     * @param qty a qty number
     * @return formated qty string
     */
    public static String formatQty(BigDecimal qty) {
        if (qty == null) {
            return StringConst.EMPTY;
        }
        return QTY_FORMAT.format(qty);
    }

    /**
     * Format weight number as #,###.000
     * 
     * @param weight a weight numuber
     * @return formated weight string
     */
    public static String formatWeight(BigDecimal weight) {
        if (weight == null) {
            return StringConst.EMPTY;
        }
        return WEIGHT_FORMAT.format(weight);
    }

    /**
     * Format length number as #,##0.00
     * 
     * @param length a length numuber
     * @return formated length string
     */
    public static String formatLength(BigDecimal length) {
        if (length == null) {
            return StringConst.EMPTY;
        }
        return LENGTH_FORMAT.format(length);
    }

    /**
     * Format a number by UOM
     * 
     * @param uomCode UOM code
     * @param number a number
     * @return string
     */
    public static String formatBigDecimal(String uomCode, BigDecimal number) {
        return formatBigDecimal(MasterManager.getUomDigits(uomCode), number);
    }

    /**
     * Format a number by UOM
     * 
     * @param uomCode UOM code
     * @param number a number
     * @return bigDecimal
     */
    public static BigDecimal formatBigDecimalRetBigDecimal(String uomCode, BigDecimal number) {
        return new BigDecimal(formatBigDecimal(MasterManager.getUomDigits(uomCode), number));
    }

    /**
     * Format a number
     * 
     * @param decimalDigit decimaldigit
     * @param number a number
     * @return string
     */
    public static String formatBigDecimal(int decimalDigit, BigDecimal number) {
        DecimalFormat df = null;
        if (number == null) {
            return StringConst.EMPTY;
        } else if (decimalDigit <= 0) {
            df = DECIMAL0_FORMAT;
        } else if (decimalDigit == NumberConst.IntDef.INT_ONE) {
            df = DECIMAL1_FORMAT;
        } else if (decimalDigit == NumberConst.IntDef.INT_TWO) {
            df = DECIMAL2_FORMAT;
        } else if (decimalDigit == NumberConst.IntDef.INT_THREE) {
            df = DECIMAL3_FORMAT;
        } else if (decimalDigit == NumberConst.IntDef.INT_FOUR) {
            df = DECIMAL4_FORMAT;
        } else if (decimalDigit == NumberConst.IntDef.INT_FIVE) {
            df = DECIMAL5_FORMAT;
        } else if (decimalDigit == NumberConst.IntDef.INT_SIX) {
            df = DECIMAL6_FORMAT;
        } else {
            df = DECIMAL6_FORMAT;
        }
        return df.format(number);
    }

    /**
     * 
     * <p>
     * BigDecimal format to '#,###.00'
     * </p>
     * 
     * @param decimalLen decimalLen
     * @param val val
     * @return String
     */
    public static String formatCurrency(int decimalLen, BigDecimal val) {
        StringBuilder deciaml = new StringBuilder();

        if (decimalLen > 0) {
            deciaml.append(".");
            for (int i = 0; i < decimalLen; i++) {
                deciaml.append("0");
            }
        }
        DecimalFormat fm = new DecimalFormat("#,##0" + deciaml.toString());
        fm.setRoundingMode(RoundingMode.HALF_UP);
        return fm.format(val.divide(new BigDecimal("1"), decimalLen, RoundingMode.HALF_UP));
    }

    // public static void main(String[] args) {
    // System.out.println(formatCurrency(2, new BigDecimal("900.175")));
    // }

    /**
     * 
     * formatPercent
     * 
     * @param val val
     * @return String
     * 
     */
    public static String formatPercent(BigDecimal val) {
        BigDecimal value = val.multiply(new BigDecimal(PERC_100));
        Format fm = new DecimalFormat("#0.00");
        return fm.format(value) + StringConst.PRE;
    }

    /**
     * 
     * <p>
     * append string
     * </p>
     * 
     * @param strs String[]
     * @param delimiter delimiter
     * @return String
     */
    public static String append(String[] strs, String delimiter) {
        StringBuffer sb = new StringBuffer();
        for (String str : strs) {
            if (!StringUtil.isEmpty(str)) {
                sb.append(str + delimiter);
            }
        }
        return sb.toString();
    }

    /**
     * escape comma
     * 
     * 
     * @param original the original string
     * @return the escaped string
     * 
     */
    public static String escapeComma(String original) {
        if (original == null) {
            return null;
        }
        return original.replaceAll(",", "\\\\,");
    }

    /**
     * unescape comma
     * 
     * 
     * @param original the original string
     * @return the unescaped string
     * 
     */
    public static String unescapeComma(String original) {
        if (original == null) {
            return null;
        }
        return original.replaceAll("\\\\,", ",");
    }

    /**
     * 
     * replace \r \n \0 to space
     * 
     * @param original original str
     * @return String
     * 
     */
    public static String replaceRETURN(String original) {
        if (original == null) {
            return "";
        }
        String out = "";

        out = original.replace('\n', ' ').replace('\r', ' ').replace('\0', ' ');
        return out;
    }

    /**
     * 
     * replace lower char to upper char
     * 
     * @param original the original string
     * @return String the upper string
     * 
     */
    public static String toUpper(String original) {
        if (isEmpty(original)) {
            return original;
        }

        return original.toUpperCase();
    }

    /**
     * Trim right space.
     * 
     * @param original original string
     * @return the trimed string
     * 
     */
    public static String rightTrim(String original) {
        if (!isEmpty(original)) {
            String regex = "(.*\\S+)(\\s+$)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(original);
            if (m.matches()) {
                return m.group(1);
            }
            return original;
        } else {
            return StringConst.EMPTY;
        }
    }

    // /**
    // * <p>
    // * support fuzzy search,and ignore case and splice the SQL.
    // * </p>
    // *
    // * @param paramName the search key
    // * @param paramValue the search value
    // * @param sql the SQL to find data
    // */
    // public static void appendLikeIgnoreCase(String paramName, String paramValue, StringBuffer sql) {
    // sql.append("and ").append("upper(").append(paramName).append(") ");
    // sql.append("like upper('");
    // sql.append(StringUtil.changeToFuzzyCondition(escapeSql(paramValue)));
    // sql.append("')");
    // sql.append(" escape '" + MosConst.EXCLAMATION);
    // sql.append("' ");
    // }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * Escapes the characters in a <code>String</code> to be suitable to pass to an SQL query.
     * </p>
     *
     * <p>
     * For example,
     * 
     * <pre>
     * statement.executeQuery(&quot;SELECT * FROM MOVIES WHERE TITLE='&quot; + StringEscapeUtils.escapeSql(&quot;McHale's Navy&quot;) + &quot;'&quot;);
     * </pre>
     * 
     * </p>
     *
     * <p>
     * At present, this method only turns single-quotes into doubled single-quotes (<code>"McHale's Navy"</code> =>
     * <code>"McHale''s Navy"</code>). It does not handle the cases of percent (%) or underscore (_) for use in LIKE
     * clauses.
     * </p>
     *
     * see http://www.jguru.com/faq/view.jsp?EID=8881
     * 
     * @param str the string to escape, may be null
     * @return a new String, escaped for SQL, <code>null</code> if null string input
     */
    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return StringUtils.replace(str, "'", "''");
    }

    /**
     * Calculate next sequence string.
     * <p>
     * For example: A->B, B->C, AA->AB, AZ->BA
     * </p>
     * 
     * @param value current value
     * @return next value
     */
    public static String nextSequenceString(String value) {
        int len = value.length();
        StringBuilder sb = new StringBuilder(len);
        boolean addFlag = true;
        for (int i = len - 1; i >= 0; i--) {
            if (addFlag) {
                int index = DIGIT_CHAR.indexOf(value.charAt(i));
                index++;
                if (index >= DIGIT_CHAR.length()) {
                    index = 0;
                    if (i == 0) {
                        throw new IllegalArgumentException("Overflow for parameter:" + value);
                    }
                } else {
                    addFlag = false;
                }
                sb.insert(0, DIGIT_CHAR.charAt(index));
            } else {
                sb.insert(0, value.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * The create transaction history order no.<br/>
     * The orderNo=no+"/"+suNo.
     * 
     * @param no the poNo or the coNo
     * @param suNo the poSubNo or the coSubNo
     * @return the orderNo
     * 
     */
    public static String createOrderNo(String no, String suNo) {
        StringBuffer orderNo = new StringBuffer();
        orderNo.append(no);
        if (!isEmpty(suNo)) {
            orderNo.append("/");
            orderNo.append(suNo);
        }
        return orderNo.toString();
    }

    /**
     * Split the array to array list.
     * 
     * 
     * @param array the array
     * @return the two-dimensional array
     * 
     */
    public static List<Long[]> splitArrayForSQLIn(Long[] array) {
        List<Long[]> arrayList = new ArrayList<Long[]>();
        if (null != array && array.length > 0) {

            int arrayLength = array.length;
            // get the list size
            int arrayCount = arrayLength / IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT;
            if (arrayLength % IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT != 0) {
                arrayCount += 1;
            }
            // split array to array list
            for (int i = 0; i < arrayCount; i++) {
                int defaultArraySize = IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT;
                // if the last array, set the array size by parameter length
                if (i == arrayCount - 1) {
                    defaultArraySize = arrayLength - i * IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT;
                }

                Long[] arrayTemp = new Long[defaultArraySize];
                for (int j = 0; j < defaultArraySize; j++) {
                    arrayTemp[j] = array[i * IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT + j];
                }
                arrayList.add(arrayTemp);
            }
        }

        return arrayList;
    }

    /**
     * Split the string array to array list.
     * 
     * 
     * @param array the array
     * @return the two-dimensional array
     * 
     */
    public static List<String[]> splitStringArrayForSQLIn(String[] array) {
        List<String[]> arrayList = new ArrayList<String[]>();
        if (null != array && array.length > 0) {

            int arrayLength = array.length;
            // get the list size
            int arrayCount = arrayLength / IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT;
            if (arrayLength % IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT != 0) {
                arrayCount += 1;
            }
            // split array to array list
            for (int i = 0; i < arrayCount; i++) {
                int defaultArraySize = IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT;
                // if the last array, set the array size by parameter length
                if (i == arrayCount - 1) {
                    defaultArraySize = arrayLength - i * IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT;
                }

                String[] arrayTemp = new String[defaultArraySize];
                for (int j = 0; j < defaultArraySize; j++) {
                    arrayTemp[j] = array[i * IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT + j];
                }
                arrayList.add(arrayTemp);
            }
        }

        return arrayList;
    }

    /**
     * Get a sub array.
     * 
     * @param arrayType Class of array element
     * @param originalArray Original array
     * @param start start index
     * @param length length of sub array
     * @param <CT> Type of array element
     * @return sub array
     */
    @SuppressWarnings("unchecked")
    private static <CT> CT[] getSubArray(Class<CT> arrayType, CT[] originalArray, int start, int length) {
        CT[] resultArray = (CT[]) Array.newInstance(arrayType, length);
        for (int index = 0; index < length; index++) {
            resultArray[index] = originalArray[start + index];
        }
        return resultArray;
    }

    /**
     * Split too big array into multiple for the limitation of oracle, it doesn't allow more than 1000 items in IN
     * clause.
     * 
     * @param arrayType Class of array element
     * @param originalArray Original array
     * @param <CT> Type of array element
     * @return A list contains split result
     * 
     */
    public static <CT extends Comparable<? super CT>> List<CT[]> splitArrayForOracle(Class<CT> arrayType,
        CT[] originalArray) {
        List<CT[]> subColumnsList = new ArrayList<CT[]>();
        CT[] squeezedArray = squeezeArray(arrayType, originalArray);
        Arrays.sort(squeezedArray);
        int index = 0;
        while (index < squeezedArray.length) {
            int length = squeezedArray.length - index;
            if (length > ORACLE_SQL_IN_MAX_LENGTH) {
                length = ORACLE_SQL_IN_MAX_LENGTH;
            }
            CT[] subArray = getSubArray(arrayType, squeezedArray, index, length);
            subColumnsList.add(subArray);
            index += length;
        }
        return subColumnsList;
    }

    /**
     * 
     * Split too big list into multiple for the limitation of oracle, it doesn't allow more than 1000 items in IN
     * clause.
     * 
     * @param listItemType Class of list element
     * @param originalList Original list
     * @param <CT> Type of list element
     * @return A list contains split result
     * 
     */
    public static <CT extends Comparable<? super CT>> List<List<CT>> splitListForOracle(Class<CT> listItemType,
        List<CT> originalList) {
        List<List<CT>> subColumnsList = new ArrayList<List<CT>>();
        List<CT> squeezedList = squeezeList(listItemType, originalList);
        Collections.sort(squeezedList);
        int index = 0;
        while (index < squeezedList.size()) {
            int length = squeezedList.size() - index;
            if (length > ORACLE_SQL_IN_MAX_LENGTH) {
                length = ORACLE_SQL_IN_MAX_LENGTH;
            }
            List<CT> subArray = squeezedList.subList(index, index + length);
            subColumnsList.add(subArray);
            index += length;
        }
        return subColumnsList;
    }

    /**
     * Remove duplicate items in List.
     * 
     * @param listType item type of element
     * @param originalList original list before squeeze
     * @param <CT> item type of element
     * @return List without duplicate item
     */
    private static <CT extends Comparable<? super CT>> List<CT> squeezeList(Class<CT> listType, List<CT> originalList) {
        Set<CT> tmpSet = new HashSet<CT>(originalList.size());
        for (CT ct : originalList) {
            tmpSet.add(ct);
        }
        List<CT> result = new ArrayList<CT>(tmpSet.size());
        result.addAll(tmpSet);
        return result;
    }

    /**
     * Remove duplicate items in array.
     * 
     * @param arrayType item type of element
     * @param originalArray original array before squeeze
     * @param <CT> item type of element
     * @return Array without duplicate item
     */
    @SuppressWarnings("unchecked")
    private static <CT extends Comparable<? super CT>> CT[] squeezeArray(Class<CT> arrayType, CT[] originalArray) {
        Set<CT> tmpSet = new HashSet<CT>(originalArray.length);
        for (CT ct : originalArray) {
            tmpSet.add(ct);
        }
        CT[] resultArray = (CT[]) Array.newInstance(arrayType, tmpSet.size());
        return tmpSet.toArray(resultArray);
    }

    /**
     * Escape wildcard characters by escape character.
     * 
     * @param input the input parameter
     * @param escapeCharacters the escape character array
     * @param escapeCharacter the escape character
     * @return the string after escape wildcard characters
     */
    public static String escape(String input, String[] escapeCharacters, String escapeCharacter) {

        String ret = input;
        String escapeChar = escapeCharacter;
        if (escapeChar == null || escapeChar.length() != 1) {
            escapeChar = StringConst.EXCLAMATION;
        }
        if (input != null && escapeCharacters != null) {
            List<String> al = Arrays.asList(escapeCharacters);
            HashSet<String> set = new HashSet<String>();
            set.addAll(al);
            // resolve the escape character first
            if (set.contains(escapeChar)) {
                ret = ret.replaceAll(escapeChar, escapeChar + escapeChar);
                set.remove(escapeChar);
            }

            Iterator<String> iter = set.iterator();
            while (iter.hasNext()) {
                String cur = iter.next();
                if (input.contains(cur)) {
                    ret = ret.replaceAll("\\" + cur, escapeChar + cur);
                }

            }

        }
        // ' is a exceptional case
        if (ret != null && ret.contains("!'")) {
            ret = ret.replaceAll("!\\'", "'");
        }
        return ret;
    }

    /**
     * Escape.
     * 
     * @param input the input
     * @param escapeCharacters the escape characters
     * @param escapeCharacter the escape character
     * 
     * @return the string
     */
    public static String escape(String input, HashSet<String> escapeCharacters, String escapeCharacter) {
        if (input == null || escapeCharacters == null) {
            return input;
        } else {
            String escapeChar = escapeCharacter;
            if (escapeChar == null || escapeChar.length() != 1) {
                escapeChar = StringConst.EXCLAMATION;
            }
            return escape(input, escapeCharacters.toArray(new String[] {}), escapeChar);
        }
    }

    /**
     * Escape.
     * 
     * @param input the input
     * 
     * @return the string
     */
    public static String escape(String input) {
        final String[] escapeCharacters = { "_", "%", "'", "!" };
        final String escapeCharacter = "!";
        if (input == null) {
            return input;
        } else {
            return escape(input, escapeCharacters, escapeCharacter);
        }
    }

    /**
     * Indicates whether the specified String object is <b>null</b> or an Empty string.
     * 
     * @param str the string to check
     * @return <b>true</b> if the parameter is <b>null</b> or an empty string (""); otherwise, <b>false</b>.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Transfer the <b>null</b> to an empty string ("").
     * 
     * @param str the specified String
     * @return empty string if the parameter is <b>null</b>; otherwise, the original string.
     */
    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    /**
     * Transfer the empty string to an <b>null<b>.
     * 
     * @param str the specified String
     * @return <b>null</b> if the parameter is empty string; otherwise, the original string.
     */
    public static String emptyToNull(String str) {
        return isEmpty(str) ? null : str;
    }

    /**
     * Change String to Integer.
     * 
     * @param str String
     * @return Integer
     */
    public static Integer toSafeInteger(String str) {

        Integer nResult = null;
        if (!isNullOrEmpty(str)) {

            try {
                nResult = Integer.valueOf(str);
            } catch (NumberFormatException e) {
                nResult = null;
            }
        } else {
            return 0;
        }

        return nResult;
    }

    /**
     * Change String to Integer.
     * 
     * @param str String
     * @return Integer
     */
    public static Integer toInteger(String str) {

        Integer nResult = null;
        if (!isNullOrEmpty(str)) {

            try {
                nResult = Integer.valueOf(str);
            } catch (NumberFormatException e) {
                nResult = null;
            }
        }

        return nResult;
    }

    /**
     * Change String to BigDecimal.
     * 
     * @param value String
     * @return BigDecimal
     */
    public static BigDecimal toBigDecimal(Object value) {
        BigDecimal result = null;
        try {
            result = new BigDecimal(value.toString());
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    /**
     * Change Object to Integer.
     * 
     * @param str Object
     * @return Integer
     */
    public static Integer toInteger(Object str) {

        Integer nResult = null;
        if (str != null) {

            try {
                nResult = Integer.valueOf(str.toString());
            } catch (NumberFormatException e) {
                nResult = null;
            }
        }

        return nResult;
    }

    /**
     * Get toke content.
     * 
     * @return toke content
     */
    public static String getTokenContent() {
        final int randomNumLength = 32;
        final String token = RandomStringUtils.randomAlphanumeric(randomNumLength);
        return token;
    }

    /**
     * Return a copy of the string, with leading and trailing whitespace omitted.
     * 
     * @param str the specified String
     * @return result
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * Return a copy of the string, with leading and trailing whitespace omitted.
     * 
     * @param str the specified String
     * @param d direction
     * @return result
     */
    public static String trim(String str, Direction d) {
        return Direction.Both == d ? trim(str) : trim(str, d, DEFAULT_TRIM_COMPARER);
    }

    /**
     * Return a copy of the string, with leading and trailing whitespace omitted.
     * 
     * @param str the specified String
     * @param c char
     * @return result
     */
    public static String trim(String str, final char c) {
        return trim(str, Direction.Both, c);
    }

    /**
     * Return a copy of the string, with leading and trailing whitespace omitted.
     * 
     * @param str the specified String
     * @param d direction
     * @param c char
     * @return result
     */
    public static String trim(String str, Direction d, final char c) {
        return trim(str, d, new ITrimComparer() {
            public boolean isNeedTrim(char ch) {
                return ch == c;
            }
        });
    }

    /**
     * Return a copy of the string, with leading and trailing whitespace omitted.
     * 
     * @param str the specified String
     * @param c char
     * @return result
     */
    public static String trim(String str, final char[] c) {
        return trim(str, Direction.Both, c);
    }

    /**
     * Return a copy of the string, with leading and trailing whitespace omitted.
     * 
     * @param str the specified String
     * @param d direction
     * @param c char
     * @return result
     */
    public static String trim(String str, Direction d, final char[] c) {
        return trim(str, d, new ITrimComparer() {
            public boolean isNeedTrim(char ch) {
                for (char cc : c) {
                    if (cc == ch) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    /**
     * Padding string by char.
     * 
     * @param str string
     * @param d Direction
     * @param tc Trim Comparer
     * @return result
     */
    private static String trim(String str, Direction d, ITrimComparer tc) {
        if (str == null) {
            return null;
        }
        if (Direction.Both == d) {
            return trim(trim(str, Direction.Left, tc), Direction.Right, tc);
        }
        int offset = Direction.Left == d ? 1 : -1;
        int st = Direction.Left == d ? 0 : str.length() - 1;
        int p = 0, len = str.length();

        while ((p < len && p > -len) && tc.isNeedTrim(str.charAt(st + p))) {
            p += offset;
        }

        return Direction.Left == d ? str.substring(st + p, len) : str.substring(0, len + p);
    }

    /**
     * ITrimComparer.
     */
    private interface ITrimComparer {

        /**
         * Checks if is need trim.
         * 
         * @param ch the char
         * 
         * @return true, if is need trim
         */
        boolean isNeedTrim(char ch);
    }

    /**
     * 
     * Count length of string by byte.
     * 
     * 
     * @param str a string
     * @return string's byte length
     */
    public static int countLengthByByte(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return countLengthByByte(str, null);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    /**
     * 
     * Count length of string by byte.
     * 
     * @param str a string
     * @param pstrEncode encode
     * @return string's byte length
     */
    public static int countLengthByByte(String str, String pstrEncode) {
        if (str == null) {
            return 0;
        }

        final char chrLow = 65377;
        final char chrHi = 65439;
        final char maxInt = 65535;
        final int interval = 2;

        int intByteLen = 0;
        CharacterIterator ite = new StringCharacterIterator(str);
        for (char c = ite.first(); c != maxInt; c = ite.next()) {
            if (((c >= ' ') && (c <= '~')) || ((c >= chrLow) && (c <= chrHi))) {
                intByteLen++;
            } else {
                intByteLen += interval;
            }
        }

        return intByteLen;
    }

    /**
     * Check string by regex.
     * 
     * @param str input string
     * @param regex the check regex
     * @return true is matched
     */
    public static boolean matchs(String str, String regex) {
        return Pattern.matches(regex, str);
    }

    /**
     * <p>
     * Checks if String contains a search String, handling <code>null</code>. This method uses
     * {@link String#indexOf(String)}.
     * </p>
     *
     * <p>
     * A <code>null</code> String will return <code>false</code>.
     * </p>
     *
     * <pre>
     * StringUtils.contains(null, *)     = false
     * StringUtils.contains(*, null)     = false
     * StringUtils.contains("", "")      = true
     * StringUtils.contains("abc", "")   = true
     * StringUtils.contains("abc", "a")  = true
     * StringUtils.contains("abc", "z")  = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @param searchStr the String to find, may be null
     * @return true if the String contains the search String,
     *         false if not or <code>null</code> string input
     * @since 2.0
     */
    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.indexOf(searchStr) >= 0;
    }

    /**
     * Compares this string to the specified string.
     * If string is null, then convert to empty to compare.
     * 
     * @param str1 compare string 1
     * @param str2 compare string 2
     * @return true is same
     */
    public static boolean equals(String str1, String str2) {
        return toSafeString(str1).equals(toSafeString(str2));
    }

    /**
     * Replace the like conditon in parameter.
     * Escape "!", "%", "_" in condition and add "%" by match mode.
     * 
     * @param param the parameter for query
     * @param itemName query condition item name
     * @param matchMode <code>CoreConst.LikeMode</code>
     * 
     */
    public static void buildLikeCondition(BaseParam param, String itemName, LikeMode matchMode) {
        if (param != null) {
            // filter parameter
            if (param.getFilters() != null && !StringUtil.isEmpty(itemName) && param.getFilters().containsKey(itemName)) {
                String paramValue = StringUtil.toSafeString(param.getFilters().get(itemName));
                param.getFilters().put(itemName, StringUtil.buildLikeCondition(paramValue, matchMode));
            }

            // // swarp parameter
            // if (param.getSwapData() != null && !StringUtil.isEmpty(itemName)
            // && param.getSwapData().containsKey(itemName)) {
            // String paramValue = StringUtil.toSafeString(param.getSwapData().get(itemName));
            // param.getSwapData().put(itemName, StringUtil.buildLikeCondition(paramValue, matchMode));
            // }
        }
    }

    /**
     * Replace the like conditon in parameter.
     * Escape "!", "%", "_" in condition and add "%" at the end.
     * 
     * @param param the parameter for query
     * @param itemName query condition item name
     * 
     */
    public static void buildLikeCondition(BaseParam param, String itemName) {
        buildLikeCondition(param, itemName, CoreConst.LikeMode.FRONT);
    }

    /**
     * 
     * <p>
     * Escape "!", "%", "_" in condition and add "%" at the end.
     * </p>
     * 
     * 
     * @param condition query condition to be changed
     * @return query condition
     * 
     */
    public static String buildLikeCondition(String condition) {
        return buildLikeCondition(condition, CoreConst.LikeMode.FRONT);
    }

    /**
     * 
     * <p>
     * Escape "!", "%", "_" in condition and add "%" by match mode.
     * </p>
     * 
     * 
     * @param condition query condition to be changed
     * @param matchMode <code>CoreConst.LikeMode</code>
     * @return query condition
     * 
     */
    public static String buildLikeCondition(String condition, LikeMode matchMode) {
        String changedCond = condition;
        if (!StringUtil.isEmpty(condition)) {
            // escape "!"
            if (condition.indexOf(StringConst.EXCLAMATION) != -1) {
                changedCond = changedCond.replace(StringConst.EXCLAMATION, StringConst.EXCLAMATION
                        + StringConst.EXCLAMATION);
            }
            // escape "%"
            if (condition.indexOf(StringConst.PRE) != -1) {
                changedCond = changedCond.replace(StringConst.PRE, StringConst.EXCLAMATION + StringConst.PRE);
            }
            // escape "_"
            if (condition.indexOf(StringConst.UNDERLINE) != -1) {
                changedCond = changedCond.replace(StringConst.UNDERLINE, StringConst.EXCLAMATION
                        + StringConst.UNDERLINE);
            }
            if (matchMode == CoreConst.LikeMode.FRONT || matchMode == CoreConst.LikeMode.PARTIAL) {
                changedCond = changedCond + StringConst.PRE;
            }
            if (matchMode == CoreConst.LikeMode.BEHIND || matchMode == CoreConst.LikeMode.PARTIAL) {
                changedCond = StringConst.PRE + changedCond;
            }
        }
        return changedCond;
    }

    /**
     * 
     * <p>
     * deReplicate String
     * </p>
     * 
     * 
     * @param condition query condition to be changed
     * @return query condition
     * 
     */
    public static String deReplicateString(String condition) {
        List<String> lstResult = new ArrayList<String>();
        if (!condition.isEmpty()) {
            String[] arr = condition.split(",");
            Arrays.sort(arr);
            for (String string : arr) {
                if (!lstResult.contains(string)) {
                    lstResult.add(string);
                }
            }
        }
        return arrayToString((String[]) lstResult.toArray(new String[lstResult.size()]));
    }

    /**
     * Set date time condition for query.
     * 
     * @param param the parameter for query
     * @param itemName query condition item name
     */
    public static void buildDateTimeCondition(BaseParam param, String itemName) {

        if (param != null && param.getFilters() != null && !StringUtil.isEmpty(itemName)) {
            String fromKey = itemName + "From";
            String toKey = itemName + "To";
            if (param.getFilters().containsKey(fromKey)) {
                String fromValue = (String) param.getFilters().get(fromKey) + " 00:00:00";
                param.getFilters().put(fromKey, fromValue);
            }
            if (param.getFilters().containsKey(toKey)) {
                Object toValue = param.getFilters().get(toKey) + " 23:59:59";
                param.getFilters().put(toKey, toValue);
            }
        }
    }

    /**
     * get session key
     * 
     * @param dateTime dateTime
     * @return the session key
     */
    public static String getSessionKey(Long dateTime) {

        int[] array = { IntDef.INT_ZERO, IntDef.INT_ONE, IntDef.INT_TWO, IntDef.INT_THREE, IntDef.INT_FOUR,
            IntDef.INT_FIVE, IntDef.INT_SIX, IntDef.INT_SEVEN, IntDef.INT_EIGHT, IntDef.INT_NINE };
        Random rand = new Random();
        for (int i = IntDef.INT_TEN; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < IntDef.INT_SIX; i++) {
            result = result * IntDef.INT_TEN + array[i];
        }

        StringBuilder sb = new StringBuilder();
        sb.append(dateTime.toString()).append(result);
        return sb.toString();
    }

    /**
     * Generate upload ID
     * 
     * @param loginId login ID
     * @return the upload ID
     */
    public static String genUploadId(String loginId) {

        StringBuffer id = new StringBuffer();
        id.append(loginId);
        id.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_HHMMSS, new Date()));
        long random = Math.round(Math.random() * IntDef.INT_THOUSAND);
        id.append(PadLeft(String.valueOf(random), IntDef.INT_THREE, String.valueOf(IntDef.INT_ZERO)));

        return id.toString();
    }

    /**
     * intercept left by byte.
     *
     * @param subStr need do string
     * @param len length
     * @return intercepted string
     */
    public static String leftSubByByte(String subStr, int len) {

        // if is null, return
        if (isNullOrEmpty(subStr)) {
            return subStr;
        }
        // check length
        int charLen = subStr.getBytes().length;
        // if length is not over limit
        if (charLen <= len) {
            return subStr;
        }

        // do substring
        char[] oldChar = subStr.toCharArray();
        char[] newChar = new char[oldChar.length];
        int tLen = 0;
        int offset = 0;
        for (char sChar : oldChar) {
            tLen += String.valueOf(sChar).getBytes().length;
            if (tLen <= len) {
                newChar[offset++] = sChar;
            } else {
                break;
            }
        }

        // return
        return new String(newChar).trim();
    }
    
}