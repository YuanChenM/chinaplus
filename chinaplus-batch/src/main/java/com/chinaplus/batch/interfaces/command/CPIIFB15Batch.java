/**
 * CPIIFB15Batch.java
 * 
 * @screen CPIIFB15
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchFileName;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchId;
import com.chinaplus.batch.interfaces.bean.CPIIFB01Param;
import com.chinaplus.batch.interfaces.service.CPIIFB15Service;
import com.chinaplus.core.util.FileUtil;

/**
 * 
 * CPIIFB15Batch. ifTableSync from SSMS
 * 
 * @author yang_jia1
 */
@Component(IFBatchId.SSMS_IFTABLESYNC)
public class CPIIFB15Batch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB15Batch.class);

    /** The CPIIFB01Batch service */
    @Autowired
    private CPIIFB15Service c15Service;

    /**
     * 
     * createBatchParam
     * 
     * @param args
     * @return
     * @see com.chinaplus.batch.common.base.BaseBatch#createBatchParam(java.lang.String[])
     */
    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        CPIIFB01Param para = null;
        
        // parameter check
        if (args == null || args.length != BatchConst.INT_ONE) {
            logger.error("The arguments number of batch is incorrect.");
            throw new RuntimeException();
        } else {
            File file = new File(args[BatchConst.INT_ZERO]);
            if (!file.exists() && !file.isDirectory()) {
                logger.error("The Data File Path is not correct.");
                throw new RuntimeException();
            }
        }

        // set parameters
        para = new CPIIFB01Param();
        para.setFilePath(args[BatchConst.INT_ZERO]);

        // set process date
        para.setProcessDate(c15Service.getDBDateTimeByDefaultTimezone());

        return para;
    }

    /**
     * doOperate
     *
     * @param param
     * @return
     * @throws Exception
     * @see com.chinaplus.batch.common.base.BaseBatch#doOperate(com.chinaplus.batch.common.bean.BaseBatchParam)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean doOperate(BaseBatchParam param) throws Exception {
        logger.info("doOperate", "batch CPIIFB01Batch start......");

        // if not ok, throw
        if (param == null) {
            throw new RuntimeException("The batch parameters is incorrect, please at least have one subBatchId.");
        }

        // cast
        CPIIFB01Param para = (CPIIFB01Param) param;

        /** ———————————————————————— first step ———————————————————————— **/
        List<String> datetimeList = new ArrayList<String>();
        Map<String, List<File>> groupFileByDatetimeMap = fileGrouping(para, datetimeList);
        
        if(groupFileByDatetimeMap.isEmpty()) {
            /** update at 20161026 start */
            // logger.warn("No file in Working Path!");
            // return true;
            logger.warn("SSMS file does not exist.");
            return false;
            /** update at 20161026 end */
        }

        /** ———————————————————————— second step ———————————————————————— **/
        for (String datetimeKey : datetimeList) {
            List<File> files = groupFileByDatetimeMap.get(datetimeKey);
            
            if (!fileCheck(files)) {
                throw new RuntimeException("Order Cancel, Order, Inbound, Outbound, Invoice File quantity is not the same.(" + datetimeKey + ")");
            }

            Map<String, Object> resultMap = new HashMap<String, Object>();
            
            try {
                resultMap = c15Service.doSyncFilesToIfTable(files);
            } catch (Exception e) {
                logger.warn("doSyncFilesToIfTable fail.");
                logger.error(e.getMessage());
                throw e;
            }

            List<File> delFiles = (List<File>) resultMap.get("delFiles");
            if ("success".equals(resultMap.get("result")) && delFiles != null && !delFiles.isEmpty()) {
                if (delFiles != null && !delFiles.isEmpty()) {
                    for (File file : delFiles) {
                        FileUtil.deleteFile(file);
                    }
                }
            }
        }

        logger.info("doOperate", "batch CPIIFB01Batch end......");
        return true;
    }

    /**
     * file grouping
     * 
     * @param param CPIIFB01Param
     * @param keyList List<String>
     * @return Map<String, List<File>>
     */
    private Map<String, List<File>> fileGrouping(CPIIFB01Param param, List<String> keyList) {
        Map<String, List<File>> groupFileByDatetime = new HashMap<String, List<File>>();
        
        File workingDirectory = new File(param.getFilePath());
        
        if (!workingDirectory.exists() || !workingDirectory.isDirectory()) {
            return groupFileByDatetime;
        }

        List<File> listFiles = getEffectiveFile(param.getFilePath());
        
        if (listFiles == null || listFiles.size() == 0) {
            return groupFileByDatetime;
        }
        
        for (File file : listFiles) {
            if (file.isFile()) {
                String key = c15Service.getDateTimeKey(file.getName());
                
                if(!keyList.contains(key)) {
                    keyList.add(key);
                }
                
                if (groupFileByDatetime.containsKey(key)) {
                    groupFileByDatetime.get(key).add(file);
                } else {
                    List<File> fileList = new ArrayList<File>();
                    fileList.add(file);
                    groupFileByDatetime.put(key, fileList);
                }
            }
        }
        
        Collections.sort(keyList);
        
        return groupFileByDatetime;
    }

    /**
     * get effective files.
     * 
     * @param filePath String
     * @return List<File>
     */
    private List<File> getEffectiveFile(String filePath) {
        // check file exist
        File path = new File(filePath);
        List<File> listFiles = new ArrayList<File>();
        List<File> customerListFiles = FileUtil.getFilesFromPath(path, IFBatchFileName.SSMS_CUSTOMER, null);
        listFiles.addAll(customerListFiles);
        
        List<File> partsListFiles = FileUtil.getFilesFromPath(path, IFBatchFileName.SSMS_PARTS, null);
        listFiles.addAll(partsListFiles);
        
        List<File> orderListFiles = FileUtil.getFilesFromPath(path, IFBatchFileName.SSMS_ORDER, null);
        listFiles.addAll(orderListFiles);
        
        List<File> orderCancelListFiles = FileUtil.getFilesFromPath(path, IFBatchFileName.SSMS_ORDERCANCEL, null);
        listFiles.addAll(orderCancelListFiles);
        
        List<File> inboundListFiles = FileUtil.getFilesFromPath(path, IFBatchFileName.SSMS_EXP_INBOUND, null);
        listFiles.addAll(inboundListFiles);
        
        List<File> outboundListFiles = FileUtil.getFilesFromPath(path, IFBatchFileName.SSMS_EXP_OUTBOUND, null);
        listFiles.addAll(outboundListFiles);
        
        List<File> invoiceListFiles = FileUtil.getFilesFromPath(path, IFBatchFileName.SSMS_INVOICE, null);
        listFiles.addAll(invoiceListFiles);
        
        List<File> activeFiles = new ArrayList<File>();
        // check start with
        for (int i = 0; i < listFiles.size(); i++) {
            // get file
            File file = listFiles.get(i);
            // check file name
            if (file.getName().startsWith(IFBatchFileName.SSMS_CUSTOMER)) {
                activeFiles.add(file);
            }
            if (file.getName().startsWith(IFBatchFileName.SSMS_PARTS)) {
                activeFiles.add(file);
            }
            if (file.getName().startsWith(IFBatchFileName.SSMS_ORDER)) {
                activeFiles.add(file);
            }
            if (file.getName().startsWith(IFBatchFileName.SSMS_ORDERCANCEL)) {
                activeFiles.add(file);
            }
            if (file.getName().startsWith(IFBatchFileName.SSMS_EXP_INBOUND)) {
                activeFiles.add(file);
            }
            if (file.getName().startsWith(IFBatchFileName.SSMS_EXP_OUTBOUND)) {
                activeFiles.add(file);
            }
            if (file.getName().startsWith(IFBatchFileName.SSMS_INVOICE)) {
                activeFiles.add(file);
            }
        }

        return activeFiles;
    }

    /**
     * check file if is correct
     * 
     * @param files List<File>
     * @return boolean
     */
    private boolean fileCheck(List<File> files) {
        int orderNumber = 0;
        int orderCancelNumber = 0;
        int inboundNumber = 0;
        int outboundNumber = 0;
        int invoiceNumber = 0;
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.startsWith(IFBatchFileName.SSMS_ORDER)) {
                orderNumber++;
            }
            if (fileName.startsWith(IFBatchFileName.SSMS_ORDERCANCEL)) {
                orderCancelNumber++;
            }
            if (fileName.startsWith(IFBatchFileName.SSMS_EXP_INBOUND)) {
                inboundNumber++;
            }
            if (fileName.startsWith(IFBatchFileName.SSMS_EXP_OUTBOUND)) {
                outboundNumber++;
            }
            if (fileName.startsWith(IFBatchFileName.SSMS_INVOICE)) {
                invoiceNumber++;
            }
        }
        
        if (orderNumber != orderCancelNumber || orderCancelNumber != inboundNumber || inboundNumber != outboundNumber
                || outboundNumber != invoiceNumber) {
            return false;
        }
        
        return true;
    }
}
