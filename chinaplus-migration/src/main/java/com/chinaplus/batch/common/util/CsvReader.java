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
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.batch.common.bean.BaseCsvFileEntity;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * TextReader.
 */
public class CsvReader {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CsvReader.class);
    
    /**
     * read File.
     * 
     * @param file file
     * @param <T> BaseEntity
     * @param clsType clsType
     * @param fileType fileType
     * @param header data header
     * @return List
     * @throws BatchException e
     */
    public static <T extends BaseCsvFileEntity> List<T> readFile(File file, Class<T> clsType,
        String fileType, String[] header) throws BatchException {
       
        logger.info(String.format("Read file [%s] start.", file.getName()));

        // read file
        BufferedReader reader = null;
        FileInputStream inputStream = null;
        List<T> entityLst = new ArrayList<T>();

        try {
            
            // file input stream
            inputStream = new FileInputStream(file);
            
            // reader file // user defulat UTF-8?
            reader = new BufferedReader(new FileReader(file));
            
            // get file Date
            Timestamp ifDateTime = null;
            
            // read
            String tempString = null;
            //int dataCount = -1;
            int line = 0;
            //boolean hasFooter = false;
            while ((tempString = reader.readLine()) != null ) {
                
                // next line
                line ++;
                
                // if blank, next
                if(StringUtil.isEmpty(tempString)) {
                    continue;
                }
                
                // for split bug-- if last one is ",", split length will -1
                if (tempString.endsWith(StringConst.COMMA)) {
                    tempString = tempString + StringConst.BLANK;
                }
                
                // first line must be header
               /* if (dataCount == -1) {

                    // if not start with "H"
                    if (!tempString.startsWith("H,")) {
                        // logger
                        logger.error(String.format(
                            "File contents is incorrect. Indicator of Header section is not 'H' or no Header."));
                        // throw
                        throw new BatchException();
                    }
                    
                    // if header length is not 26.
                    if (tempString.length() != NumberConst.IntDef.INT_TWENTY_FIVE) {
                        // logger
                        logger.error(String.format(
                            "Header section is incorrect. Totoal length of header is not 26."));
                        // throw
                        throw new BatchException();
                    }
                    
                    // split
                    String[] dataColumn = tempString.split(StringConst.VERTICAL_REX);
                    if (dataColumn.length != NumberConst.IntDef.INT_THREE) {
                        // logger
                        logger.error(String.format(
                            "Header section is incorrect. Number of header items is incorrect, number of items is not 3."));
                        // throw
                        throw new BatchException();
                    }
                    
                    // check file type
                    if (!dataColumn[NumberConst.IntDef.INT_ONE].trim().equals(fileType)) {
                        // logger
                        logger.error(String.format("Header section is incorrect. File type is incorrect, must be [%s]",
                                    new Object[] { fileType }));
                        // throw
                        throw new BatchException();
                    }
                    
                    // format
                    ifDateTime = DateTimeUtil.parseDateTime(dataColumn[NumberConst.IntDef.INT_TWO],
                        DateTimeUtil.FORMAT_YYYYMMDD_HHMMSS);
                    if (ifDateTime == null) {
                        logger.error("File Created DateTime in Header section is incorrect.");
                        throw new BatchException();
                    }

                    // start line
                    dataCount = 0;
                    continue;
                }*/
                
                // reader data column
                if (tempString.startsWith("I,")) {
                    
                    // count up
                    //dataCount ++;
                    
                    // split
                    String[] dataColumn = tempString.split(StringConst.COMMA);
                    
                    // check length
                    if (dataColumn.length != header.length) {
                        // logger
                        logger.warn(String.format("Record is incorrect. Number of data items is incorrect.(line: %d)", line));
                        
                        continue;
                    }
                    
                    // put into entity and check
                    T entity = refectToBean(clsType, header, dataColumn, file.getName(), line);
                    
                    // if not null, add into list
                    if (entity != null) {
                        
                        // set ifDateTime
                        entity.setIfDateTime(ifDateTime);

                        // set into entity
                        entityLst.add(entity);
                    }

                    // next
                    continue;
                }
                
                // read footer
               /* if(tempString.startsWith("F,")) {
                    
                    // has footer
                    hasFooter = true;

                    // Row count check, must same as item section data count.
                    String[] strLst = tempString.split(StringConst.VERTICAL_REX);
                    if(strLst.length != NumberConst.IntDef.INT_TWO) {
                        // logger
                        logger.error(String.format("Footer section is incorrect."));
                        // throw
                        throw new BatchException();
                    // check count
                    } else if(StringUtil.toInteger(strLst[1]) == null) {
                        // logger
                        logger.warn(String.format("Footer section is incorrect. Row Count is not number."));
                    // check count
                    } else if(!StringUtil.toInteger(strLst[1]).equals(dataCount)) {
                        // logger
                        logger.warn(String.format("Footer section is incorrect. Row Count is not equal with data count."));
                    }
                    
                    break;
                }*/
                
                // logger
                logger.error(String.format("The indicator of record is not 'I' or 'F'.(line: %d)",
                    new Object[] { line }));
                
                // throw
                throw new BatchException();
            }
            
            // does not has footer
            /*if (!hasFooter) {
                logger.error("There is no footer section in file.");
                // change to BatchException
                throw new BatchException();
            }*/
        } catch(Exception e) {
            logger.error(String.format("Read File [%s] failtrue.", new Object[] { file.getName() }));
            logger.error(e.getMessage());
            // change to BatchException
            throw new BatchException();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            
            logger.info(String.format("Read file [%s] end.", file.getName()));
        }
        
        return entityLst;
    }
  
    /**
     * refect all datas into bean.
     * 
     * @param clsType return bean
     * @param headers headers
     * @param datas datas
     * @param fileName fileName
     * @param line line
     * @param <T> BasePartsInfoEntity
     * 
     * @return clsType
     */
    private static <T extends BaseEntity> T refectToBean(Class<T> clsType,
        String[] headers, String[] datas, String fileName, int line) {
        
        // new entity
        T entity = null;
        
        try {
            
            // create
            entity = clsType.newInstance();
        
            // loop
            for (int i = 0; i < headers.length; i++) {
                String header = headers[i].trim();
                String data = datas[i].trim();
                
                // get filed 
                String getMethodName = prepareGetSetMethod(header, false);
                String setMethodName = prepareGetSetMethod(header, true);
                
                // get set method(must get super class method) and then only public method
                Method setMethod = null;
                Method getMethod = null;
                
                // try get setmethod
                getMethod = clsType.getMethod(getMethodName);
                setMethod = clsType.getMethod(setMethodName, getMethod.getReturnType());
                
                
                // check parameter types
                Class<?>[] types = setMethod.getParameterTypes();
                if(types.length != 1) {
                    continue;
                }
                
                // cast
                Object[] objects = castDataToDataType(data , types[0], header, line);
                if(objects == null) {
                    //throw new BatchException();
                    entity = null;
                }
                
                // invoke
                if (entity != null) {
                    setMethod.invoke(entity, objects);
                }
            }
        } catch (Exception e) {
            // return 
            entity = null;
        } 
        
        return entity;
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
        if(isSet) {
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

        Object[] objects = null;

        // append
        try {

            // check type
            if (type.getName().equals(String.class.getName())) {
                if (!StringUtil.isEmpty(data)) {
                    objects = new Object[] { StringUtil.toString(data) };
                } else {
                    objects = new Object[] { null };
                }
            } else if (type.getName().equals(Integer.class.getName())) {
                if (!StringUtil.isEmpty(data)) {
                    objects = new Object[] { Integer.valueOf(data) };
                } else {
                    objects = new Object[] { null };
                }
            } else if (type.getName().equals(BigDecimal.class.getName())) {
                if (!StringUtil.isEmpty(data)) {

                    // cast
                    BigDecimal dataVal = new BigDecimal(data).stripTrailingZeros();

                    // compare
                    if (dataVal.compareTo(BatchConst.MAX_QTY) > 0) {
                        logger.error("Value of " + itemName + "(value:" + data
                                + ") is over limit(max value: 9999999999999.999)(line: " + line +  ").");
                        throw new BatchException();
                    }

                    // check scale
                    if (dataVal.scale() > NumberConst.IntDef.INT_THREE) {
                        logger.error("Scale of " + itemName + "(value:" + data
                                + ") is over limit(max scale: 3)(line: " + line +  ").");
                        throw new BatchException();
                    }

                    objects = new Object[] { dataVal };

                } else {
                    objects = new Object[] { BigDecimal.ZERO };
                }
            } else if (type.getName().equals(Date.class.getName())
                    || type.getName().equals(Timestamp.class.getName())) {
                if (!StringUtil.isEmpty(data)) {
                    // parse
                    Date date = null;
                    
                    // check date
                    if(StringUtil.isNumeric(data)) {
                        date = DateTimeUtil.parseDate(data, "yyyyMMdd");
                    }
                    // check scale
                    if (date == null) {
                        logger.error("Item " + itemName + "(value:" + data
                                + ") is not a invaild date string.(format: yyyyMMdd)(line: " + line +  ").");
                        throw new BatchException();
                    }
                    
                    // date
                    objects = new Object[] { date };
                } else {
                    objects = new Object[] { null };
                }
            }
        } catch (Exception e) {
            logger.error("Can not cast data(" + data + ") for " + itemName + ", please check.(line: " + line + ")");
            // do nothing
            throw new BatchException();
        }

        return objects;
    }
    
}
