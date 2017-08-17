/**
 * ifTableSync from TT-logic
 * 
 * @screen CPIIFB16
 * @author yang_jia1
 */
package com.chinaplus.batch.migration.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.util.CsvReader;
import com.chinaplus.batch.migration.bean.MigrationComParam;
import com.chinaplus.batch.migration.bean.TntMgImpIpEx;
import com.chinaplus.batch.migration.service.IPFileReaderService;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.FileUtil;

/**
 * ifTableSync from TT-logic
 * 
 * @author yang_jia1
 */
@Component("IPReader")
public class IPFileReaderBatch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(IPFileReaderBatch.class);

    /** data header */
    private static String[] header = { "indicator", "seqNo", "officeCode", "sourceIpNo", "pidNo", "parentPidNo",
        "whsCode", "originalCustCode", "customerCode", "ttcPartsNo", "originalQty", "qty", "invoiceNo", "moduleNo",
        "status", "inboundDate", "sysObDateTime", "actualObDateTime", "adjustmentDate" };
    
    @Autowired
    private IPFileReaderService readerService;

    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        // prepare parameter
        MigrationComParam param = null;

        // parameter check
        // Check batch arguments
        if (args == null || args.length != BatchConst.INT_TWO) {

            logger.error("The arguments number of batch is incorrect.");
            throw new BatchException();
        }

        // get file path
        File file = new File(args[BatchConst.INT_ZERO]);
        // check file path
        if (!file.exists() && !file.isDirectory()) {
            logger.error("The Data File Path is incorrect.");
            throw new BatchException();
        }

        // set parameters
        param = new MigrationComParam();
        param.setFilePath(args[BatchConst.INT_ZERO]);
        param.setOfficeCode(args[BatchConst.INT_ONE]);
        
        // get date
        // check
        TnmOffice office = baseService.getOfficeInfo(param.getOfficeCode());
        if (office == null) {
            logger.error("Office code does not an effective office.");
            throw new BusinessException();
        }
        // set office id
        param.setOfficeId(office.getOfficeId());
        
        param.setProcessDate(baseService.getDBDateTime(office.getTimeZone()));

        // return
        return param;
    }

    /**
     * doOperate
     *
     * @param param
     * @return
     * @throws Exception
     * @see com.chinaplus.batch.common.base.BaseBatch#doOperate(com.chinaplus.batch.common.bean.BaseBatchParam)
     */
    @Override
    public boolean doOperate(BaseBatchParam param) throws Exception {
        logger.info("doOperate", "batch CPIIFB16Batch start......");

        // if no parameter, return
        if (param == null) {
            logger.error("The batch parameters is incorrect, please at least have one subBatchId.");
            throw new RuntimeException();
        }

        // cast
        MigrationComParam castPara = (MigrationComParam) param;
        // get files
        List<File> effFiles = this.getEffectiveFiles(castPara);
        if (effFiles.isEmpty()) {
            logger.warn("TTLogix file does not exist.");
            return false;
        }

        // sort files
        Collections.sort(effFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                // sort by name
                return o1.getName().compareTo(o2.getName());
            }
        });

        // do file read and save
        List<TntMgImpIpEx> ipList = null;
        for (File file : effFiles) {
            // get content list
            ipList = CsvReader.readFile(file, TntMgImpIpEx.class, "MM15X4V9", header);

            // check file content size
            readerService.doSyncFilesToIfTable(ipList, castPara.getProcessDate());

            // delete
            FileUtil.deleteFile(file);
        }

        logger.info("doOperate", "batch CPIIFB16Batch end......");
        return true;
    }
   
    /**
     * get effective files.
     * 
     * @param param parameter
     * @return boolean boolean
     */
    private List<File> getEffectiveFiles(MigrationComParam param) {

        // check file exist
        File filePath = new File(param.getFilePath());
        List<File> listFiles = FileUtil.getFilesFromPath(filePath, "MM15X4V9", FileType.CSV.getSuffix());
        List<File> activeFiles = new ArrayList<File>();
        // check start with
        for (int i = 0; i < listFiles.size(); i++) {
            // get file
            File file = listFiles.get(i);
            // check file name
            if (file.getName().startsWith("MM15X4V9")) {
                activeFiles.add(file);
            }
        }

        return activeFiles;
    }
}
