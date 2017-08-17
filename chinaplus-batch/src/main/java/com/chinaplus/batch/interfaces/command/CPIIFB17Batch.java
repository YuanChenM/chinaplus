/**
 * CPIIFB17Batch.java
 * 
 * @screen CPIIFB17
 * @author Cheng xingfei
 */
package com.chinaplus.batch.interfaces.command;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchId;
import com.chinaplus.batch.common.consts.BatchConst.SSMSFileType;
import com.chinaplus.batch.interfaces.bean.CPIIFB17Param;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.FileUtil;

/**
 * 
 * CPIIFB17Batch.
 * Copy Data File from SSMS
 * @author Cheng xingfei
 */
@Component(IFBatchId.SSMS_COPYFILE)
public class CPIIFB17Batch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB17Batch.class);
    
    /**
     * FILE_COUNT.
     */
    private final int FILE_COUNT = 7;
    
    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        CPIIFB17Param para = new CPIIFB17Param();
        
        if (args == null || args.length != BatchConst.INT_THREE) {
            throw new RuntimeException("The arguments number of batch is incorrect!");
        }

        para.setInterfaceFilePath(args[BatchConst.INT_ZERO]);
        para.setWorkingFilePath(args[BatchConst.INT_ONE]);
        para.setBackupFilePath(args[BatchConst.INT_TWO]);

        // set process date
        para.setProcessDate(baseService.getDBDateTimeByDefaultTimezone());

        return para;
    }

    /**
     * doOperate
     */
    @Override
    public boolean doOperate(BaseBatchParam para) throws Exception {
        logger.debug("doOperate", "batch CPIIFB17Batch start......");
        
        CPIIFB17Param param = null;
        if (para == null) {
            throw new RuntimeException("The batch parameters is incorrect!");
        } else {
            param = (CPIIFB17Param) para;
        }

        try {
            
            // prepare folder
            File srcFolder = new File(param.getInterfaceFilePath());
            File destFolder = new File(param.getWorkingFilePath());
            File backupFolder = new File(param.getBackupFilePath());
            
            // check srcFolder
            if (!srcFolder.isDirectory()) {
                throw new RuntimeException("Interface Path is not a valid path!");
            }

            // check destFolder
            if(!destFolder.isDirectory()) {
                throw new RuntimeException("Working Path is not a valid path!");
            }
            
            // check backupFolder
            if(!backupFolder.isDirectory()) {
                throw new RuntimeException("Backup Path is not a valid path!");
            }

            // get file from srcFolder
            List<File> fileList = FileUtil.getBatchFilesFromPath(srcFolder, SSMSFileType.ALL_FILE_TYPE);
            
            // compare
            Comparator<File> comparator = new Comparator<File>(){
                @Override
                public int compare(File o1, File o2) {
                    // sort by name
                    return o1.getName().compareTo(o2.getName());
                }
            };

            // sort
            Collections.sort(fileList, comparator);
            
            // get time
            String time = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD_HHMMSS, super.baseService.getDBDateTimeByDefaultTimezone());

            for (File srcFile : fileList) {
                
                // get file type
                String fileType = SSMSFileType.FILE_TYPE_MAP.get(srcFile.getName().substring(IntDef.INT_ZERO, IntDef.INT_EIGHT));
                
                // file name type
                Integer fileGroup = Integer.valueOf(srcFile.getName().substring(IntDef.INT_TWO, IntDef.INT_FOUR));
                
                // get new file path
                StringBuffer newFilePath = new StringBuffer(destFolder.getPath());
                newFilePath.append(File.separatorChar);
                newFilePath.append(fileType);
                newFilePath.append(srcFile.getName().substring(IntDef.INT_EIGHT));
                newFilePath.append(StringConst.DOT);
                newFilePath.append(time);
                newFilePath.append((int)(fileGroup / FILE_COUNT));
                
                // remove
                FileUtil.removeAndDeleteFile(srcFile, new File(newFilePath.toString()), false);
                
                // back up
                String backUpPath = FileUtil.getBackUpFilePathByFileType(backupFolder.getPath(), fileType);
                File backP = new File(backUpPath);
                if (!backP.exists()) {
                    backP.mkdirs();
                }
                // get file path
                newFilePath = new StringBuffer(backUpPath);
                newFilePath.append(File.separatorChar);
                newFilePath.append(fileType);
                newFilePath.append(srcFile.getName().substring(IntDef.INT_EIGHT));
                newFilePath.append(StringConst.DOT);
                newFilePath.append(time);
                newFilePath.append((int)(fileGroup / FILE_COUNT));
                
                // copy and delete
                FileUtil.removeAndDeleteFile(srcFile, new File(newFilePath.toString()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // return normal code
        return true;
    }
}
