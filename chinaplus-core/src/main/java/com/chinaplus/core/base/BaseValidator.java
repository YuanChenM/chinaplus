/**
 * BaseValidator.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Sheet;

import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;

/**
 * The base class for validator.
 * 
 * @param <T> entity
 */
public abstract class BaseValidator<T> {

    /** Regexp for forbidden char */
    protected static final String FORBIDDEN_CHAR = "^[\\u0020-\\u007E]*$";

    /** filters */
    protected static final String[] FILTERS = new String[] { ":", "：", ",", "，", "/", "\'", " ", "\\", "*", "[", "]",
        "?" };

    /** regex key words */
    protected static final List<String> REGEX_KEY = Arrays
        .asList(new String[] { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" });

    /** the message list */
    private List<BaseMessage> messageList = new ArrayList<BaseMessage>();

    /**
     * The interface method to validate datas.
     * If there has some validate error, then throw exception with default group message.
     * 
     * @param entity the data that will be validated
     * @param param the parameter
     * @throws BusinessException the validate exception
     */
    public void validate(T entity, BaseParam param) throws BusinessException {
        // clear old message before new validate
        // TODO Remove by Yinchuan LIU
        /*if (this.messageList == null) {
            this.messageList = new ArrayList<BaseMessage>();
        } else {
            this.messageList.clear();
        }
        // do validate
        this.validateData(entity, param);
        // check validate result
        if (this.messageList.size() > 0) {
            List<BaseMessage> messages = new ArrayList<BaseMessage>();
            // add group messaeg w1009 for detail validate
            messages.add(new BaseMessage(MessageCodeConst.W1009));
            // add all validate messages
            messages.addAll(this.messageList);
            // throw exception
            throw new BusinessException(messages);
        }*/
    }

    /**
     * The interface method to validate uploaded datas.
     * If there has some validate error, then throw exception with default group message.
     * 
     * @param entity the data that will be validated
     * @param param the parameter
     * @throws BusinessException the validate exception
     */
    public void validateUpload(T entity, BaseParam param) throws BusinessException {
        
        // TODO Remove by Yinchuan LIU
        /*
        // clear old message before new validate
        if (this.messageList == null) {
            this.messageList = new ArrayList<BaseMessage>();
        } else {
            this.messageList.clear();
        }
        // do validate
        this.validateData(entity, param);
        // check validate result
        if (this.messageList.size() > 0) {
            List<BaseMessage> messages = new ArrayList<BaseMessage>();
            // add group messaeg w1008 for upload validate
            messages.add(new BaseMessage(MessageCodeConst.W1008));
            // add all validate messages
            messages.addAll(this.messageList);
            // throw exception
            throw new UploadException(this.messageList);
        }
        */
    }

    /**
     * The interface method to validate datas, return messages without group message.
     * 
     * @param entity the data that will be validated
     * @param param the parameter
     * @return validate messages
     */
    public List<BaseMessage> validateWithoutException(T entity, BaseParam param) {
        List<BaseMessage> messages = null;
        this.validateData(entity, param);
        if (this.messageList.size() > 0) {
            // add all validate messages
            messages = this.messageList;
        }
        return messages;
    }

    /**
     * The interface method to validate uploaded datas, return messages without group message.
     * 
     * @param entity the data that will be validated
     * @param param the parameter
     * @return validate messages
     */
    public List<BaseMessage> validateUploadWithoutException(T entity, BaseParam param) {
        List<BaseMessage> messages = null;
        this.validateData(entity, param);
        if (this.messageList.size() > 0) {
            // add all validate messages
            messages = this.messageList;
        }
        return messages;
    }

    /**
     * Validata data.
     * 
     * @param entity the data that will be validated
     * @param param the parameter
     */
    protected abstract void validateData(T entity, BaseParam param);

    /**
     * Get the error message list.
     * 
     * @return the errorMessageList
     */
    public List<BaseMessage> getErrorMessageList() {
        return messageList;
    }

    /**
     * Add message to message list.
     * 
     * @param fieldName validate fieldName
     * @param message the message
     */
    protected void addErrorMessage(String fieldName, String message) {
        BaseMessage validatorMessage = new BaseMessage(fieldName, message);
        this.messageList.add(validatorMessage);
    }

    /**
     * Add message to message list.
     * 
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @param message the message
     */
    protected void addErrorMessage(int rowNum, String fieldName, String message) {
        BaseMessage validatorMessage = new BaseMessage(rowNum, fieldName, message);
        this.messageList.add(validatorMessage);
    }

    /**
     * Add message to message list.
     * 
     * @param message the message
     */
    protected void addErrorMessage(BaseMessage message) {
        this.messageList.add(message);
    }

    /**
     * Do required validation.
     * 
     * @param value validate value
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @param sheetName validate sheetName
     * @return true is check ok
     */
    protected boolean requiredValidator(Object value, int rowNum, String fieldName, String sheetName) {
        boolean result = true;
        if (value == null) {
            result = false;
        } else if (value instanceof String && StringUtil.isEmpty((String) value)) {
            result = false;
        }
        if (!result) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1008_089);
            message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), sheetName, fieldName });
            this.addErrorMessage(message);
        }
        return result;
    }

    /**
     * Do required validation.
     * 
     * @param value validate value
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @return true is check ok
     */
    protected boolean requiredValidator(Object value, int rowNum, String fieldName) {
        boolean result = true;
        if (value == null) {
            result = false;
        } else if (value instanceof String && StringUtil.isEmpty((String) value)) {
            result = false;
        }
        if (!result) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1008_007);
            message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), fieldName });
            this.addErrorMessage(message);
        }
        return result;
    }

    /**
     * Do required validation.
     * 
     * @param value validate value
     * @param fieldName validate fieldName
     * @return true is check ok
     */
    protected boolean requiredValidator(Object value, String fieldName) {
        boolean result = true;
        if (value == null) {
            result = false;
        } else if (value instanceof String && StringUtil.isEmpty((String) value)) {
            result = false;
        }
        if (!result) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1009_001);
            message.setMessageArgs(new String[] { fieldName });
            this.addErrorMessage(message);
        }
        return result;
    }

    /**
     * Check number.
     * 
     * @param value validate value
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @param required true is required
     * @return true is check ok
     */
    protected boolean numberValidator(String value, int rowNum, String fieldName, boolean required) {
        boolean result = true;
        if (StringUtil.isEmpty(value) && !required) {
            return true;
        } else if (required) {
            result = this.requiredValidator(value, rowNum, fieldName);
        }
        if (result && !ValidatorUtils.checkNumber(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1008_005);
            message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), fieldName, "item_type_number" });
            this.addErrorMessage(message);
            result = false;
        }
        return result;
    }

    /**
     * Check number.
     * 
     * @param value validate value
     * @param fieldName validate fieldName
     * @param required true is required
     * @return true is check ok
     */
    protected boolean numberValidator(String value, String fieldName, boolean required) {
        boolean result = true;
        if (StringUtil.isEmpty(value) && !required) {
            return true;
        } else if (required) {
            result = this.requiredValidator(value, fieldName);
        }
        if (result && !ValidatorUtils.checkNumber(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1009_002);
            message.setMessageArgs(new String[] { fieldName });
            this.addErrorMessage(message);
            result = false;
        }
        return result;
    }

    /**
     * Check decimal.
     * 
     * @param value validate value
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @param sheetName validate sheetName
     * @param required true is required
     * @return true is check ok
     */
    protected boolean decimalValidator(String value, int rowNum, String fieldName, String sheetName, boolean required) {
        boolean result = true;
        if (StringUtil.isEmpty(value) && !required) {
            return true;
        } else if (required) {
            result = this.requiredValidator(value, rowNum, fieldName);
        }
        if (result && !ValidatorUtils.checkDecimal(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1008_092);
            message.setMessageArgs(
                new String[] { StringUtil.toSafeString(rowNum), sheetName, fieldName, "item_type_decimal" });
            this.addErrorMessage(message);
            result = false;
        }
        return result;
    }

    /**
     * Check decimal.
     * 
     * @param value validate value
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @param required true is required
     * @return true is check ok
     */
    protected boolean decimalValidator(String value, int rowNum, String fieldName, boolean required) {
        boolean result = true;
        if (StringUtil.isEmpty(value) && !required) {
            return true;
        } else if (required) {
            result = this.requiredValidator(value, rowNum, fieldName);
        }
        if (result && !ValidatorUtils.checkDecimal(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1008_005);
            message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), fieldName, "item_type_decimal" });
            this.addErrorMessage(message);
            result = false;
        }
        return result;
    }

    /**
     * Check decimal.
     * 
     * @param value validate value
     * @param fieldName validate fieldName
     * @param required true is required
     * @return true is check ok
     */
    protected boolean decimalValidator(String value, String fieldName, boolean required) {
        boolean result = true;
        if (StringUtil.isEmpty(value) && !required) {
            return true;
        } else if (required) {
            result = this.requiredValidator(value, fieldName);
        }
        if (result && !ValidatorUtils.checkDecimal(value)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1009_002);
            message.setMessageArgs(new String[] { fieldName });
            this.addErrorMessage(message);
            result = false;
        }
        return result;
    }

    /**
     * Check euqals with contrast value.
     * 
     * @param value validate value
     * @param contrastValue the value to contrast
     * @param rowNum the row number
     * @param fieldName the from field name
     * @return true is check ok
     */
    protected boolean compareValidator(BigDecimal value, BigDecimal contrastValue, int rowNum, String fieldName) {
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

        if (!result) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1008_051);
            message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), fieldName });
            this.addErrorMessage(message);
            result = false;
        }
        return result;
    }

    /**
     * Check euqals with contrast value.
     * 
     * @param value validate value
     * @param contrastValue the value to contrast
     * @param rowNum the row number
     * @param fieldName the from field name
     * @param sheetName the from sheet name
     * @return true is check ok
     */
    protected boolean euqalsValidator(Object value, Object contrastValue, int rowNum, String fieldName,
        String sheetName) {
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

        if (!result) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1008_097);
            message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), sheetName, fieldName });
            this.addErrorMessage(message);
            result = false;
        }
        return result;
    }

    /**
     * Check euqals with contrast value.
     * 
     * @param value validate value
     * @param contrastValue the value to contrast
     * @param rowNum the row number
     * @param fieldName the from field name
     * @return true is check ok
     */
    protected boolean euqalsValidator(Object value, Object contrastValue, int rowNum, String fieldName) {
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

        if (!result) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1008_011);
            message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), fieldName });
            this.addErrorMessage(message);
            result = false;
        }
        return result;
    }

    /**
     * Check maximum length.
     * 
     * @param value validate value
     * @param maxLength the maxinum length
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @return true is check ok
     */
    protected boolean maxLengthValidator(String value, int maxLength, int rowNum, String fieldName) {
        if (value == null) {
            return true;
        } else {
            if (value.getBytes().length > maxLength) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1008_004);
                message.setMessageArgs(
                    new String[] { StringUtil.toSafeString(rowNum), fieldName, StringUtil.toSafeString(maxLength) });
                this.addErrorMessage(message);
                return false;
            }
        }
        return true;
    }

    /**
     * Check maximum length.
     * 
     * @param value validate value
     * @param maxLength the maxinum length
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @param sheetName validate sheetName
     * @return true is check ok
     */
    protected boolean maxLengthValidator(String value, int maxLength, int rowNum, String fieldName, String sheetName) {
        if (value == null) {
            return true;
        } else {
            if (value.getBytes().length > maxLength) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1008_094);
                message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), sheetName, fieldName,
                    StringUtil.toSafeString(maxLength) });
                this.addErrorMessage(message);
                return false;
            }
        }
        return true;
    }

    /**
     * Check maximum length.
     * 
     * @param value validate value
     * @param maxLength the maxinum length
     * @param fieldName validate fieldName
     * @param required true is required
     * @return true is check ok
     */
    protected boolean maxLengthValidator(String value, int maxLength, String fieldName, boolean required) {
        if (value == null) {
            return true;
        } else {
            if (value.getBytes().length > maxLength) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1009_005);
                message.setMessageArgs(new String[] { fieldName, StringUtil.toSafeString(maxLength) });
                this.addErrorMessage(message);
                return false;
            }
        }
        return true;
    }

    /**
     * Check the maximum value.
     * 
     * @param value validate value
     * @param maxValue the maxinum value
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @param sheetName validate sheetName
     * @return true is check ok
     */
    protected boolean maxValidator(BigDecimal value, BigDecimal maxValue, int rowNum, String fieldName,
        String sheetName) {
        boolean ret = true;

        if (value == null) {
            return true;
        } /*else {
         // TODO
            if (maxValue == null) {
                // TODO
                if (value.scale() > UploadOrderConst.QTY_SCALE) {
                    ret = false;
                } else {
                    return true;
                }
            } else if (value.compareTo(maxValue) == 1 || value.scale() > UploadOrderConst.QTY_SCALE) {
                ret = false;
            }
        }*/
        if (!ret) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1008_093);
            message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), sheetName, fieldName,
                StringUtil.toSafeString(maxValue) });
            this.addErrorMessage(message);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check the maximum value.
     * 
     * @param value validate value
     * @param maxValue the maxinum value
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @return true is check ok
     */
    protected boolean maxValidator(BigDecimal value, BigDecimal maxValue, int rowNum, String fieldName) {
        boolean ret = true;

        if (value == null) {
            return true;
        } /*else {
            // TODO
            if (maxValue == null) {
                if (value.scale() > UploadOrderConst.QTY_SCALE) {
                    ret = false;
                } else {
                    return true;
                }
            } else if (value.compareTo(maxValue) == 1 || value.scale() > UploadOrderConst.QTY_SCALE) {
                ret = false;
            }
        }*/
        if (!ret) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1008_009);
            message.setMessageArgs(
                new String[] { StringUtil.toSafeString(rowNum), fieldName, StringUtil.toSafeString(maxValue) });
            this.addErrorMessage(message);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check the maximum value.
     * 
     * @param value validate value
     * @param maxValue the maxinum value
     * @param fieldName validate fieldName
     * @return true is check ok
     */
    protected boolean maxValidator(BigDecimal value, BigDecimal maxValue, String fieldName) {
        boolean ret = true;

        if (value == null) {
            return true;
        } /*else {
            // TODO
            if (maxValue == null) {
                if (value.scale() > UploadOrderConst.QTY_SCALE) {
                    ret = false;
                } else {
                    return true;
                }
            } else if (value.compareTo(maxValue) == 1 || value.scale() > UploadOrderConst.QTY_SCALE) {
                ret = false;
            }
           
        } */
        if (!ret) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1009_006);
            message.setMessageArgs(new String[] { fieldName, StringUtil.toSafeString(maxValue) });
            this.addErrorMessage(message);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check the minimum value.
     * 
     * @param value validate value
     * @param minValue the minimum value
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @param sheetName validate sheetName
     * @return true is check ok
     */
    protected boolean minValidator(BigDecimal value, BigDecimal minValue, int rowNum, String fieldName,
        String sheetName) {

        if (value == null) {
            return true;
        } else {
            if (minValue == null) {
                return true;
            } else if (value.compareTo(minValue) == -1) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1008_091);
                message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), sheetName, fieldName });
                this.addErrorMessage(message);
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
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @return true is check ok
     */
    protected boolean minValidator(BigDecimal value, BigDecimal minValue, int rowNum, String fieldName) {

        if (value == null) {
            return true;
        } else {
            if (minValue == null) {
                return true;
            } else if (value.compareTo(minValue) == -1) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1008_030);
                message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), fieldName });
                this.addErrorMessage(message);
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
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @return true is check ok
     */
    protected boolean minValidator(Integer value, Integer minValue, int rowNum, String fieldName) {

        if (value == null) {
            return true;
        } else {
            if (minValue == null) {
                return true;
            } else if (value.compareTo(minValue) == -1) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1008_030);
                message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), fieldName });
                this.addErrorMessage(message);
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
     * @param fieldName validate fieldName
     * @return true is check ok
     */
    protected boolean minValidator(BigDecimal value, BigDecimal minValue, String fieldName) {

        if (value == null) {
            return true;
        } else {
            if (minValue == null) {
                return true;
            } else if (value.compareTo(minValue) == -1) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1009_010);
                message.setMessageArgs(new String[] { fieldName });
                this.addErrorMessage(message);
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
     * @param rowNum the row number
     * @param sheetName validate sheetName
     * @param fieldName validate fieldName
     * @return true is check ok
     */
    protected boolean betweenValidator(BigDecimal value, BigDecimal maxValue, BigDecimal minValue, int rowNum,
        String fieldName, String sheetName) {
        if (maxValue == null || minValue == null) {
            return true;
        } else if (value == null) {
            return true;
        } else {
            if (value.compareTo(minValue) == -1 || value.compareTo(maxValue) == 1) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1008_090);
                message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), sheetName });
                this.addErrorMessage(message);
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * Check tha value section with the maximum and the minimum.
     * 
     * @param value validate value
     * @param maxValue the minimum value
     * @param minValue the minimum value
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @return true is check ok
     */
    protected boolean betweenValidator(BigDecimal value, BigDecimal maxValue, BigDecimal minValue, int rowNum,
        String fieldName) {
        if (maxValue == null || minValue == null) {
            return true;
        } else if (value == null) {
            return true;
        } else {
            if (value.compareTo(minValue) == -1 || value.compareTo(maxValue) == 1) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1008_100);
                message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum) });
                this.addErrorMessage(message);
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * check detail has Datas.
     * 
     * @param workSheet workSheet
     * @param detailStartRow the detail start row num
     * @param newModCol the newMod col num, if no newMod, please set it 0.
     * @param detailStartCol the detail start col num
     * @param detailTotalCol the detail total col num
     * @param isOneSheet upload file has one sheet: true, else false
     * @return boolean
     */
    public boolean checkHasData(Sheet workSheet, int detailStartRow, int newModCol, int detailStartCol,
        int detailTotalCol, boolean isOneSheet) {
        //TODO
        //int noDataRowcnt = 0;
        //int lastRowNum = workSheet.getLastRowNum();

        /*for (int rowIndex = detailStartRow; rowIndex <= detailStartRow + UploadOrderConst.BLANK_LINE_NUM
                - 1; rowIndex++) {
            Row row = workSheet.getRow(rowIndex - 1);

            // if no data of a row, loop to the next row.
            if (null == row || isBlackRow(workSheet, rowIndex, detailStartCol, detailTotalCol)) {
                noDataRowcnt++;
                if (noDataRowcnt > UploadOrderConst.BLANK_LINE_NUM - 1) {
                    BaseMessage message = null;
                    if (isOneSheet) {
                        message = new BaseMessage(MessageCodeConst.W1008_083);
                        message.setMessageArgs(new String[] { workSheet.getSheetName() });
                    } else {
                        message = new BaseMessage(MessageCodeConst.W1008_003);
                    }
                    this.addErrorMessage(message);
                    return false;
                }
                continue;
            } else {
                noDataRowcnt = 0;
                if (newModCol > 0) {
                    continue;
                } else {
                    return true;
                }
            }
        }

        if (newModCol > 0) {
            for (int rowIndex = detailStartRow; rowIndex <= lastRowNum + 1; rowIndex++) {
                if (!StringUtil.isEmpty(PoiUtil.getStringCellValue(workSheet, rowIndex, newModCol))) {
                    return true;
                }
            }
        }
        */

        BaseMessage message = null;
        if (isOneSheet) {
            message = new BaseMessage(MessageCodeConst.W1008_003);
        } else {
            message = new BaseMessage(MessageCodeConst.W1008_083);
            message.setMessageArgs(new String[] { workSheet.getSheetName() });
        }
        this.addErrorMessage(message);
        return false;
    }

    /**
     * check current row whether is black row.
     * 
     * @param workSheet workSheet
     * @param rowIndex rowIndex
     * @param detailStartCol the detail start column num
     * @param detailTotalCol the detail total column num
     * @return boolean
     */
    public static boolean isBlackRow(Sheet workSheet, int rowIndex, int detailStartCol, int detailTotalCol) {

        for (int colIndex = detailStartCol; colIndex <= detailStartCol + detailTotalCol; colIndex++) {
            if (!StringUtil.isEmpty(PoiUtil.getStringCellValue(workSheet, rowIndex, colIndex))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check forbidden char in string.
     * 
     * @param value validate value
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @param sheetName validate sheetName
     * @return true is check ok
     */
    protected boolean forbiddenCharValidator(String value, int rowNum, String fieldName, String sheetName) {
        if (StringUtil.isEmpty(value)) {
            return true;
        }
        // if (false) { RegexUtils.checkRegex(FORBIDDEN_CHAR, value)
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1008_095);
        // message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), sheetName, fieldName });
        // this.addErrorMessage(message);
        // return false;
        // }
        return true;
    }

    /**
     * Check forbidden char in string.
     * 
     * @param value validate value
     * @param rowNum the row number
     * @param fieldName validate fieldName
     * @return true is check ok
     */
    protected boolean forbiddenCharValidator(String value, int rowNum, String fieldName) {
        if (StringUtil.isEmpty(value)) {
            return true;
        }
        // if (false) { RegexUtils.checkRegex(FORBIDDEN_CHAR, value)
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1008_008);
        // message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), fieldName });
        // this.addErrorMessage(message);
        // return false;
        // }
        return true;
    }

    // /**
    // * Compare two value.
    // *
    // * @param fromValue the from value
    // * @param toValue the to value
    // * @param rowNum the row number
    // * @param fromFieldName the from field name
    // * @param toFieldName the to field name
    // * @param required true is required
    // * @return true is check ok
    // */
    // protected boolean fromToValidator(BigDecimal fromValue, BigDecimal toValue, int rowNum, String fromFieldName,
    // String toFieldName, boolean required) {
    // boolean result = true;
    // if (!required && (fromValue == null || toValue == null)) {
    // return true;
    // } else if (required) {
    // result = this.requiredValidator(fromValue, fromFieldName);
    // result = this.requiredValidator(toValue, toFieldName) & result;
    // }
    //
    // // from > to => error
    // if (result && fromValue.compareTo(toValue) == 1) {
    // this.addErrorMessage(rowNum, fromFieldName,
    // MessageManager.getMessage("validator.message.compare", new String[] { fromFieldName, toFieldName }));
    // result = false;
    // }
    // return result;
    // }

    // /**
    // * Compare two value.
    // *
    // * @param fromValue the from value
    // * @param toValue the to value
    // * @param fromFieldName the from field name
    // * @param toFieldName the to field name
    // * @param required true is required
    // * @return true is check ok
    // */
    // protected boolean fromToValidator(BigDecimal fromValue, BigDecimal toValue, String fromFieldName,
    // String toFieldName, boolean required) {
    // boolean result = true;
    // if (!required && (fromValue == null || toValue == null)) {
    // return true;
    // } else if (required) {
    // result = this.requiredValidator(fromValue, fromFieldName);
    // result = this.requiredValidator(toValue, toFieldName) & result;
    // }
    //
    // // from > to => error
    // if (result && fromValue.compareTo(toValue) == 1) {
    // this.addErrorMessage(fromFieldName,
    // MessageManager.getMessage("validator.message.compare", new String[] { fromFieldName, toFieldName }));
    // result = false;
    // }
    // return result;
    // }
    //
    // /**
    // * Check the date time.
    // *
    // * @param value validate value
    // * @param dateFormat the date format
    // * @param rowNum the row number
    // * @param fieldName validate fieldName
    // * @param required true is required
    // * @return true is check ok
    // */
    // protected boolean dateValidator(String value, String dateFormat, int rowNum, String fieldName, boolean required)
    // {
    // boolean result = true;
    // if (StringUtil.isEmpty(value) && !required) {
    // return true;
    // } else if (required) {
    // result = this.requiredValidator(value, rowNum, fieldName);
    // }
    // if (result && !ValidatorUtils.checkDateStr(value, dateFormat)) {
    // this.addErrorMessage(rowNum, fieldName, MessageManager.getMessage("validator.message.date"));
    // result = false;
    // }
    // return result;
    // }
    //
    // /**
    // * Check the date time.
    // *
    // * @param value validate value
    // * @param dateFormat the date format
    // * @param fieldName validate fieldName
    // * @param required true is required
    // * @return true is check ok
    // */
    // protected boolean dateValidator(String value, String dateFormat, String fieldName, boolean required) {
    // boolean result = true;
    // if (StringUtil.isEmpty(value) && !required) {
    // return true;
    // } else if (required) {
    // result = this.requiredValidator(value, fieldName);
    // }
    // if (result && !ValidatorUtils.checkDateStr(value, dateFormat)) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.date"));
    // result = false;
    // }
    // return result;
    // }
    //
    // /**
    // * Check email address.
    // *
    // * @param value validate value
    // * @param rowNum the row number
    // * @param fieldName validate fieldName
    // * @param required true is required
    // * @return true is check ok
    // */
    // protected boolean mailValidator(String value, int rowNum, String fieldName, boolean required) {
    // boolean result = true;
    // if (StringUtil.isEmpty(value) && !required) {
    // return true;
    // } else if (required) {
    // result = this.requiredValidator(value, rowNum, fieldName);
    // }
    // if (result && !ValidatorUtils.checkMail(value)) {
    // this.addErrorMessage(rowNum, fieldName, MessageManager.getMessage("validator.message.mail"));
    // result = false;
    // }
    // return result;
    // }
    //
    // /**
    // * Check email address.
    // *
    // * @param value validate value
    // * @param fieldName validate fieldName
    // * @param required true is required
    // * @return true is check ok
    // */
    // protected boolean mailValidator(String value, String fieldName, boolean required) {
    // boolean result = true;
    // if (StringUtil.isEmpty(value) && !required) {
    // return true;
    // } else if (required) {
    // result = this.requiredValidator(value, fieldName);
    // }
    // if (result && !ValidatorUtils.checkMail(value)) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.mail"));
    // result = false;
    // }
    // return result;
    // }
    //
    // /**
    // * Check phone number.
    // *
    // * @param value validate value
    // * @param rowNum the row number
    // * @param fieldName validate fieldName
    // * @param required true is required
    // * @return true is check ok
    // */
    // protected boolean phoneValidator(String value, int rowNum, String fieldName, boolean required) {
    // boolean result = true;
    // if (StringUtil.isEmpty(value) && !required) {
    // return true;
    // } else if (required) {
    // result = this.requiredValidator(value, rowNum, fieldName);
    // }
    // if (result && !ValidatorUtils.checkPhone(value)) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.phone"));
    // result = false;
    // }
    // return result;
    // }
    //
    // /**
    // * Check phone number.
    // *
    // * @param value validate value
    // * @param fieldName validate fieldName
    // * @param required true is required
    // * @return true is check ok
    // */
    // protected boolean phoneValidator(String value, String fieldName, boolean required) {
    // boolean result = true;
    // if (StringUtil.isEmpty(value) && !required) {
    // return true;
    // } else if (required) {
    // result = this.requiredValidator(value, fieldName);
    // }
    // if (result && !ValidatorUtils.checkPhone(value)) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.phone"));
    // result = false;
    // }
    // return result;
    // }
    //
    // /**
    // * Check string with regex.
    // *
    // * @param value validate value
    // * @param regex check rule regex
    // * @param rowNum the row number
    // * @param fieldName validate fieldName
    // * @param required true is required
    // * @return true is check ok
    // */
    // protected boolean regexValidator(String value, String regex, int rowNum, String fieldName, boolean required) {
    // boolean result = true;
    // if (StringUtil.isEmpty(value) && !required) {
    // return true;
    // } else if (required) {
    // result = this.requiredValidator(value, rowNum, fieldName);
    // }
    // if (result && !RegexUtils.checkRegex(regex, value)) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.regex"));
    // result = false;
    // }
    // return result;
    // }
    //
    // /**
    // * Check string with regex.
    // *
    // * @param value validate value
    // * @param regex check rule regex
    // * @param fieldName validate fieldName
    // * @param required true is required
    // * @return true is check ok
    // */
    // protected boolean regexValidator(String value, String regex, String fieldName, boolean required) {
    // boolean result = true;
    // if (StringUtil.isEmpty(value) && !required) {
    // return true;
    // } else if (required) {
    // result = this.requiredValidator(value, fieldName);
    // }
    // if (result && !RegexUtils.checkRegex(regex, value)) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.regex"));
    // result = false;
    // }
    // return result;
    // }

    // /**
    // * check 文件大小
    // *
    // * @param rowNum 行号
    // * @param fieldName 字段名称
    // * @param file 上传文件
    // * @param fileMaxSize 文件最大值
    // * @param required 是否必须输入
    // */
    // protected void fileSizeValidator(int rowNum, String fieldName, MultipartFile file, long fileMaxSize,
    // boolean required) {
    // if (file == null && required) {
    // this.addErrorMessage(rowNum, fieldName, MessageManager.getMessage("validator.message.empty"));
    // return;
    // }
    // long fileSize = file.getSize();
    // if (fileSize == NumberConst.IntDef.INT_ZERO) {
    // if (required) {
    // this.addErrorMessage(rowNum, fieldName, MessageManager.getMessage("validator.message.empty"));
    // }
    // return;
    // }
    // if (fileSize > fileMaxSize) {
    // this.addErrorMessage(rowNum, fieldName, MessageManager.getMessage("validator.message.fileMaxSize",
    // new Object[] { String.valueOf(fileMaxSize) }));
    // }
    // }

    // /**
    // * check 文件大小
    // *
    // * @param fieldName 字段名称
    // * @param file 上传文件
    // * @param fileMaxSize 文件最大值
    // * @param required 是否必须输入
    // */
    // protected void fileSizeValidator(String fieldName, MultipartFile file, long fileMaxSize, boolean required) {
    // if (file == null && required) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.empty"));
    // return;
    // }
    // long fileSize = file.getSize();
    // if (fileSize == NumberConst.IntDef.INT_ZERO) {
    // if (required) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.empty"));
    // }
    // return;
    // }
    // if (fileSize > fileMaxSize) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.fileMaxSize",
    // new Object[] { String.valueOf(fileMaxSize) }));
    // }
    // }

    // /**
    // * check 文件类型
    // *
    // * @param rowNum 行号
    // * @param fieldName 字段名称
    // * @param file 上传文件
    // * @param required 是否必须输入
    // * @param fileTypeList 文件类型
    // */
    // protected void fileTypeValidator(int rowNum, String fieldName, MultipartFile file, boolean required,
    // String[] fileTypeList) {
    // if (file == null && required) {
    // this.addErrorMessage(rowNum, fieldName, MessageManager.getMessage("validator.message.empty"));
    // return;
    // }
    // long fileSize = file.getSize();
    // if (fileSize == NumberConst.IntDef.INT_ZERO) {
    // if (required) {
    // this.addErrorMessage(rowNum, fieldName, MessageManager.getMessage("validator.message.empty"));
    // }
    // return;
    // }
    // String fileName = file.getOriginalFilename();
    // int indexOf = fileName.lastIndexOf(".");
    // String filePrefix = fileName.substring(indexOf + 1, fileName.length());
    // boolean flag = true;
    // for (String fileType : fileTypeList) {
    // if (filePrefix.equalsIgnoreCase(fileType)) {
    // flag = false;
    // break;
    // }
    // }
    // if (flag) {
    // this.addErrorMessage(rowNum, fieldName, MessageManager.getMessage("validator.message.fileType"));
    // }
    // }
    //
    // /**
    // * check 文件类型
    // *
    // * @param fieldName 字段名称
    // * @param file 上传文件
    // * @param required 是否必须输入
    // * @param fileTypeList 文件类型
    // */
    // protected void fileTypeValidator(String fieldName, MultipartFile file, boolean required, String[] fileTypeList) {
    // if (file == null && required) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.empty"));
    // return;
    // }
    // long fileSize = file.getSize();
    // if (fileSize == NumberConst.IntDef.INT_ZERO) {
    // if (required) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.empty"));
    // }
    // return;
    // }
    // String fileName = file.getOriginalFilename();
    // int indexOf = fileName.lastIndexOf(".");
    // String filePrefix = fileName.substring(indexOf + 1, fileName.length());
    // boolean flag = true;
    // for (String fileType : fileTypeList) {
    // if (filePrefix.equalsIgnoreCase(fileType)) {
    // flag = false;
    // break;
    // }
    // }
    // if (flag) {
    // this.addErrorMessage(fieldName, MessageManager.getMessage("validator.message.fileType"));
    // }
    // }

    // add for #0213552 begin
    /**
     * While the input string contains special character (, \ / [ ] : * etc) will add error message.
     * When rowNum is 0 and sheetName is empty then add W1009_004 else add W1008_008.
     * 
     * @param value the input String
     * @param fieldName the fieldName
     * @param rowNum the rowNum 
     * @param sheetName the sheet name
     * @return boolean
     */
    protected boolean validSpecialCharacterOfMasterData(String value, String fieldName, int rowNum, String sheetName) {
        boolean result = true;
        String containsChars = isContainsFilter(value);
        if (!StringUtil.isEmpty(containsChars)) {
            BaseMessage message = new BaseMessage();
            if (rowNum == 0 && StringUtil.isEmpty(sheetName)) {
                message.setMessageCode(MessageCodeConst.W1009_004);
                message.setMessageArgs(new String[] { fieldName });
            } else {
                // message .setMessageCode(MessageCodeConst.W1008_120);
                // message
                // .setMessageArgs(new String[] { sheetName, StringUtil.toSafeString(rowNum), fieldName, containsChars
                // });
                message.setMessageCode(MessageCodeConst.W1008_008);
                message.setMessageArgs(new String[] { StringUtil.toSafeString(rowNum), fieldName });
            }
            this.addErrorMessage(message);
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
    // add for #0213552 end
}
