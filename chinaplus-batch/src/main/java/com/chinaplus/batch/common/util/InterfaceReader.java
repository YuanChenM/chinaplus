/**
 * YMIRDB012Batch.java
 *
 * @author liu_yinchuan
 */
package com.chinaplus.batch.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.batch.interfaces.bean.Customer;
import com.chinaplus.common.entity.BaseInterfaceEntity;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * TextReader.
 * @author yang_jia1
 */
public abstract class InterfaceReader {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(InterfaceReader.class);

    /** YYYYMMDD */
    private static String FORMAT_YYYYMMDD = "YYYYMMDD";

    /**
     * read File.
     * 
     * @param file file
     * @param <T> BaseEntity
     * @param clsType clsType
     * @param fileType fileType
     * @return List
     * @throws BatchException e
     */
    public static <T extends BaseInterfaceEntity> List<T> readFile(File file, Class<T> clsType, String fileType)
        throws BatchException {

        // check
        checkFileName(file, fileType);

        logger.info(String.format("Read file [%s] start.", file.getName()));

        // read file
        BufferedReader reader = null;
        FileInputStream inputStream = null;
        Date fileCreateDate = null;
        List<T> entityLst = new ArrayList<T>();

        try {

            // file input stream
            inputStream = new FileInputStream(file);

            // reader file // user defulat UTF-8
            reader = new BufferedReader(new FileReader(file));

            // read
            String tempString = null;
            int dataCount = -1;
            int line = 0;
            while ((tempString = reader.readLine()) != null) {

                // next line
                line++;

                // if blank, next
                if (StringUtil.isNullOrEmpty(tempString)) {
                    continue;
                }

                // first line must be header
                if (dataCount == -1) {

                    // trim
                    tempString = tempString.trim();

                    // if header length is not 26.
                    if (tempString.length() != NumberConst.IntDef.INT_TWENTY_THREE) {
                        // logger
                        logger.error(String.format("Header section is incorrect. Totoal length of header is not 23."));
                        // throw
                        throw new BatchException();
                    }

                    // if not start with "H"
                    if (!tempString.startsWith("1")) {
                        // logger
                        logger.error(String
                            .format("File contents is incorrect. Data Id of Header section is not '1' or no Header."));
                        // throw
                        throw new BatchException();
                    }

                    // if not start with "H"
                    if (!tempString.startsWith("1" + fileType)) {
                        // logger
                        logger.error(String.format("File contents is incorrect. File Id of Header section is not "
                                + fileType));
                        // throw
                        throw new BatchException();
                    }

                    // get file create date
                    fileCreateDate = DateTimeUtil.parseDate(tempString.substring(NumberConst.IntDef.INT_NINE),
                        DateTimeUtil.FORMAT_YYYYMMDD_HHMMSS);
                    if (fileCreateDate == null) {
                        // logger
                        logger.error(String.format("File contents is incorrect. File Create Date time is incorrect."));
                        // throw
                        throw new BatchException();
                    }

                    // start line
                    dataCount = 0;
                    continue;
                }

                // reader data column
                if (tempString.startsWith("2")) {

                    // count up
                    dataCount++;

                    // get new instance
                    T entity = clsType.newInstance();

                    // check length
                    if (tempString.length() != entity.getTotalLength()) {

                        // logger
                        logger.warn(String.format("Record is incorrect. Length of data record is incorrect.(line: %d)",
                            line));

                        throw new BatchException();
                    }

                    // put into entity and check
                    reflectToBean(entity, tempString, line);
                    entity.setFileCreateDate(new Timestamp(fileCreateDate.getTime()));
                    // set into entity
                    if (entity != null && entity.getDataId() != null) {
                        entityLst.add(entity);
                    }

                    // next
                    continue;
                }

                // read footer
                if (tempString.startsWith("8")) {

                    // data length.
                    tempString = tempString.trim();
                    if (tempString.length() != NumberConst.IntDef.INT_FIFTEEN) {
                        // logger
                        logger.error(String.format("Footer section is incorrect."));
                        // throw
                        throw new BatchException();
                        // check count
                    } else if (!tempString.startsWith("8" + fileType)) {
                        // logger
                        logger.error(String.format("Footer section is incorrect. File Id of Footer section is not "
                                + fileType));
                        // throw
                        throw new BatchException();
                    } else {

                        // get data count
                        Integer rowCount = StringUtil.toInteger(tempString.substring(NumberConst.IntDef.INT_NINE));
                        if (rowCount == null || !rowCount.equals(dataCount)) {
                            // logger
                            logger
                                .warn(String
                                    .format("Footer section is incorrect. NO. OF RECORDS is not equal with actual data count."));
                        }
                    }
                    // next
                    continue;
                }

                // read end
                if ("9".equals(tempString.trim())) {
                    break;
                }

                // logger
                logger.error(String.format("The data id of record is not '2' or '8' or '9'.(line: %d)",
                    new Object[] { line }));
                throw new BatchException();
            }
        } catch (Exception e) {
            logger.error(String.format("Read File [%s] failtrue.", new Object[] { file.getName() }));
            logger.error(e.getMessage());
            // change to BatchException
            throw new BatchException();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {}
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }

            // end
            logger.info(String.format("Read file [%s] end.", file.getName()));
        }

        return entityLst;
    }
    
    /**
     * read File.
     * 
     * @param file file
     * @param <T> BaseEntity
     * @param clsType clsType
     * @param fileType fileType
     * @return List
     * @throws BatchException e
     */
    public static <T extends BaseInterfaceEntity> List<T> readFileForCustomer(File file, Class<T> clsType, String fileType)
        throws BatchException {
        // check
        checkFileName(file, fileType);

        logger.info(String.format("Read file [%s] start.", file.getName()));

        // read file
        BufferedReader reader = null;
        FileInputStream inputStream = null;
        Date fileCreateDate = null;
        List<T> entityLst = new ArrayList<T>();

        try {

            // file input stream
            inputStream = new FileInputStream(file);

            // reader file using Shift-JIS
            reader = new BufferedReader(new InputStreamReader(inputStream, "Shift-JIS"));

            // read
            String tempString = null;
            int dataCount = -1;
            int line = 0;
            while ((tempString = reader.readLine()) != null) {

                // next line
                line++;

                // if blank, next
                if (StringUtil.isNullOrEmpty(tempString)) {
                    continue;
                }

                // first line must be header
                if (dataCount == -1) {

                    // trim
                    tempString = tempString.trim();

                    // if header length is not 26.
                    if (tempString.length() != NumberConst.IntDef.INT_TWENTY_THREE) {
                        // logger
                        logger.error(String.format("Header section is incorrect. Totoal length of header is not 23."));
                        // throw
                        throw new BatchException();
                    }

                    // if not start with "H"
                    if (!tempString.startsWith("1")) {
                        // logger
                        logger.error(String
                            .format("File contents is incorrect. Data Id of Header section is not '1' or no Header."));
                        // throw
                        throw new BatchException();
                    }

                    // if not start with "H"
                    if (!tempString.startsWith("1" + fileType)) {
                        // logger
                        logger.error(String.format("File contents is incorrect. File Id of Header section is not "
                                + fileType));
                        // throw
                        throw new BatchException();
                    }

                    // get file create date
                    fileCreateDate = DateTimeUtil.parseDate(tempString.substring(NumberConst.IntDef.INT_NINE),
                        DateTimeUtil.FORMAT_YYYYMMDD_HHMMSS);
                    if (fileCreateDate == null) {
                        // logger
                        logger.error(String.format("File contents is incorrect. File Create Date time is incorrect."));
                        // throw
                        throw new BatchException();
                    }

                    // start line
                    dataCount = 0;
                    continue;
                }

                // reader data column
                if (tempString.startsWith("2")) {

                    // count up
                    dataCount++;

                    // get new instance
                    T entity = clsType.newInstance();
                    
                    if (clsType.getName().equals(Customer.class.getName())) {
                        if (tempString.length() != entity.getTotalLength()) {
                            if (isChinese(tempString) || isJapanese(tempString)) {
                                if (tempString.getBytes().length > IntDef.INT_ONE_HUNDRED_AND_TWENTY) {
                                    // logger
                                    logger.warn(String.format("Record is incorrect. Length of data record is incorrect.(line: %d)",
                                        line));

                                    throw new BatchException();
                                }
                            } else {
                                // Convenient extension
                                if (tempString.getBytes().length > IntDef.INT_ONE_HUNDRED_AND_TWENTY) {
                                    // logger
                                    logger.warn(String.format("Record is incorrect. Length of data record is incorrect.(line: %d)",
                                        line));

                                    throw new BatchException();
                                }
                            }
                            Customer customer = (Customer) entity;

                            customer.setDataId(Integer.valueOf(tempString.substring(IntDef.INT_ZERO, IntDef.INT_ONE)));
                            customer.setExpCode(tempString.substring(IntDef.INT_ONE, IntDef.INT_THREE));
                            customer.setVendorRoute(tempString.substring(IntDef.INT_THREE, IntDef.INT_FIVE));
                            customer.setCustomerCode(tempString.substring(IntDef.INT_FIVE, IntDef.INT_FIFTEEN));
                            customer.setCustomerName(tempString.substring(IntDef.INT_FIFTEEN));
                            customer.setFileCreateDate(new Timestamp(fileCreateDate.getTime()));
                            
                            // set into entity
                            if (entity != null && entity.getDataId() != null) {
                                entityLst.add(entity);
                            }
                            
                        } else {
                            reflectToBean(entity, tempString, line);
                            entity.setFileCreateDate(new Timestamp(fileCreateDate.getTime()));
                            // set into entity
                            if (entity != null && entity.getDataId() != null) {
                                entityLst.add(entity);
                            }
                        }

                        // next
                        continue;
                    }

                    // check length
                    if (tempString.length() != entity.getTotalLength()) {

                        // logger
                        logger.warn(String.format("Record is incorrect. Length of data record is incorrect.(line: %d)",
                            line));

                        throw new BatchException();
                    }

                    // put into entity and check
                    reflectToBean(entity, tempString, line);
                    entity.setFileCreateDate(new Timestamp(fileCreateDate.getTime()));
                    // set into entity
                    if (entity != null && entity.getDataId() != null) {
                        entityLst.add(entity);
                    }

                    // next
                    continue;
                }

                // read footer
                if (tempString.startsWith("8")) {

                    // data length.
                    tempString = tempString.trim();
                    if (tempString.length() != NumberConst.IntDef.INT_FIFTEEN) {
                        // logger
                        logger.error(String.format("Footer section is incorrect."));
                        // throw
                        throw new BatchException();
                        // check count
                    } else if (!tempString.startsWith("8" + fileType)) {
                        // logger
                        logger.error(String.format("Footer section is incorrect. File Id of Footer section is not "
                                + fileType));
                        // throw
                        throw new BatchException();
                    } else {

                        // get data count
                        Integer rowCount = StringUtil.toInteger(tempString.substring(NumberConst.IntDef.INT_NINE));
                        if (rowCount == null || !rowCount.equals(dataCount)) {
                            // logger
                            logger
                                .warn(String
                                    .format("Footer section is incorrect. NO. OF RECORDS is not equal with actual data count."));
                        }
                    }
                    // next
                    continue;
                }

                // read end
                if ("9".equals(tempString.trim())) {
                    break;
                }

                // logger
                logger.error(String.format("The data id of record is not '2' or '8' or '9'.(line: %d)",
                    new Object[] { line }));
                throw new BatchException();
            }
        } catch (Exception e) {
            logger.error(String.format("Read File [%s] failtrue.", new Object[] { file.getName() }));
            logger.error(e.getMessage());
            // change to BatchException
            throw new BatchException();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {}
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }

            // end
            logger.info(String.format("Read file [%s] end.", file.getName()));
        }

        return entityLst;
    }

    /**
     * reflect all data into bean.
     * 
     * @param entity entity
     * @param tempString tempString
     * @param line line
     * @param <T> BasePartsInfoEntity
     */
    private static <T extends BaseInterfaceEntity> void reflectToBean(T entity, String tempString, int line) {

        try {

            // if not has FieldsPosition or FieldsName
            if (entity.getFieldsPosition() == null || entity.getFieldsName() == null) {
                return;
            }

            // get field information
            int[] filedsPi = entity.getFieldsPosition();
            String[] filedsNm = entity.getFieldsName();

            // loop
            int startIndex = 0;
            for (int i = 0; i < filedsPi.length; i++) {
                // field name
                String header = filedsNm[i].trim();
                String data = tempString.substring(startIndex, startIndex + filedsPi[i]);

                // get filed
                String getMethodName = prepareGetSetMethod(header, false);
                String setMethodName = prepareGetSetMethod(header, true);

                // get set method(must get super class method) and then only public method
                Method getMethod = entity.getClass().getMethod(getMethodName);
                Method setMethod = entity.getClass().getMethod(setMethodName, getMethod.getReturnType());

                // check parameter types
                Class<?>[] types = setMethod.getParameterTypes();
                if (types.length != 1) {
                    continue;
                }

                // cast
                Object[] objects = castDataToDataType(data, types[0], header, line);

                // invoke
                if (objects != null) {
                    setMethod.invoke(entity, objects);
                }

                // reset start index
                startIndex = startIndex + filedsPi[i];
            }

            // set data id
            entity.setDataId(NumberConst.IntDef.INT_TWO);

        } catch (Exception e) {
            // return
            //entity.setDataId(null);
            throw new BatchException();
        }
    }

    /**
     * Prepare set Method name.
     * 
     * @param filedName filedName
     * @param isSet true : set, false : get
     * @return Method name
     */
    private static String prepareGetSetMethod(String filedName, boolean isSet) {

        // append
        StringBuffer sb = new StringBuffer();
        if (isSet) {
            sb.append("set");
        } else {
            sb.append("get");
        }
        sb.append(filedName.substring(0, 1).toUpperCase());
        sb.append(filedName.substring(1));

        return sb.toString();
    }

    /**
     * Prepare set Method name.
     * 
     * @param data data
     * @param type type
     * @param itemName item name
     * @param line line
     * @param <T> return type
     * @return Method name
     */
    private static <T> Object[] castDataToDataType(String data, Class<T> type, String itemName, int line) {

        // define
        Object[] objects = null;

        // append
        try {

            // if null, then go return null Object
            if (StringUtil.isNullOrEmpty(data.trim())) {
                return new Object[] { null };
            }

            // check type
            if (type.getName().equals(String.class.getName())) {
                // cast to String
                objects = new Object[] { StringUtil.toString(data.trim()) };
            } else if (type.getName().equals(Integer.class.getName())) {
                // cast to Integer
                objects = new Object[] { Integer.valueOf(data) };
            } else if (type.getName().equals(BigDecimal.class.getName())) {
                // cast to BigDecimal
                objects = new Object[] { new BigDecimal(data).stripTrailingZeros() };
            } else if (type.getName().equals(Date.class.getName()) || type.getName().equals(Timestamp.class.getName())) {
                // cast to Date
                objects = new Object[] { DateTimeUtil.parseDate(data, FORMAT_YYYYMMDD) };
            }
        } catch (Exception e) {
            logger.error("Can not cast data(" + data + ") for " + itemName + ", please check.(line: " + line + ")");
            // do nothing
            throw new BatchException();
        }

        return objects;
    }

    /**
     * check File Name.
     * 
     * @param file file
     * @param fileType fileType
     * @return check result
     */
    public static boolean checkFileName(File file, String fileType) {

        // get name
        String fileName = file.getName();

        // if no file type, then error
        if (fileType == null) {
            throw new BatchException();
        }

        // if not start with fileType, then error
        if (!fileName.startsWith(fileType)) {
            throw new BatchException();
        }

        return true;
    }
    
    /**
     * check char if chinese
     * 
     * @param c char
     * @return boolean
     */
    private static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {

            return true;

        }
        return false;
    }

    /**
     * check char if japanese
     * 
     * @param c char
     * @return boolean
     */
    private static boolean isJapanese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.HIRAGANA || ub == Character.UnicodeBlock.KATAKANA

                || ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS) {

            return true;

        }
        return false;
    }

    /**
     * check string if chinese
     * 
     * @param strName String
     * @return boolean
     */
    public static boolean isChinese(String strName) {

        char[] ch = strName.toCharArray();

        for (int i = 0; i < ch.length; i++) {

            char c = ch[i];

            if (isChinese(c)) {

                return true;

            }
        }
        return false;
    }

    /**
     * check string if japanese
     * 
     * @param strName String
     * @return boolean
     */
    public static boolean isJapanese(String strName) {

        char[] ch = strName.toCharArray();

        for (int i = 0; i < ch.length; i++) {

            char c = ch[i];

            if (isJapanese(c)) {

                return true;

            }
        }
        return false;
    }
}
