/**
 * CPIIFB18Batch.java
 * 
 * @screen CPIIFB18
 * @author Cheng xingfei
 */
package com.chinaplus.batch.interfaces.command;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchFileName;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchId;
import com.chinaplus.batch.interfaces.bean.CPIIFB18Param;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.FileUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * CPIIFB18Batch.
 * Copy Data File from SSMS
 * 
 * @author Cheng xingfei
 */
@Component(IFBatchId.TTLOGIC_COPYFILE)
public class CPIIFB18Batch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB18Batch.class);

    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        CPIIFB18Param para = new CPIIFB18Param();

        if (args == null || args.length != BatchConst.INT_FOUR) {
            throw new RuntimeException("The arguments number of batch is incorrect!");
        }

        para.setInterfaceFilePath(args[BatchConst.INT_ZERO]);
        para.setWorkingFilePath(args[BatchConst.INT_ONE]);
        para.setBackupFilePath(args[BatchConst.INT_TWO]);
        para.setFileNamePrefix(args[BatchConst.INT_THREE]);

        // set process date
        para.setProcessDate(baseService.getDBDateTimeByDefaultTimezone());

        return para;
    }

    /**
     * doOperate
     */
    @Override
    public boolean doOperate(BaseBatchParam para) throws Exception {
        logger.debug("doOperate", "batch CPIIFB18Batch start......");

        CPIIFB18Param param = null;
        if (para == null) {
            throw new RuntimeException("The batch parameters is incorrect!");
        } else {
            param = (CPIIFB18Param) para;
        }

        try {

            // prepare folder
            File srcFolder = new File(param.getInterfaceFilePath());
            File destFolder = new File(param.getWorkingFilePath());
            File backupFolder = new File(param.getBackupFilePath());
            String fileNamePrefix = param.getFileNamePrefix();

            // check srcFolder
            if (!srcFolder.isDirectory()) {
                throw new RuntimeException("Interface Path is not a valid path!");
            }

            // check destFolder
            if (!destFolder.isDirectory()) {
                throw new RuntimeException("Working Path is not a valid path!");
            }

            // check backupFolder
            if (!backupFolder.isDirectory()) {
                throw new RuntimeException("Backup Path is not a valid path!");
            }

            // check fileName
            if (StringUtil.isEmpty(fileNamePrefix)) {
                throw new RuntimeException("fileName prefix is not be empty!");
            }

            // get file from srcFolder
            List<File> fileList = FileUtil.getBatchFilesFromPath(srcFolder, fileNamePrefix);

            // compare
            Comparator<File> comparator = new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    // sort by name
                    return o1.getName().compareTo(o2.getName());
                }
            };

            // sort
            Collections.sort(fileList, comparator);

            // get time
            Date dbDate = super.baseService.getDBDateTimeByDefaultTimezone();
            String time = null;

            // get time
            StringBuffer newFileName = new StringBuffer();

            for (File srcFile : fileList) {

                // get date
                dbDate = DateTimeUtil.add(dbDate, 0, 0, 0, 0, 0, 1);
                time = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD_HHMMSS, dbDate);

                // copy to working file
                newFileName.setLength(IntDef.INT_ZERO);
                newFileName.append(destFolder.getPath());
                newFileName.append(File.separatorChar);
                newFileName.append(IFBatchFileName.TTLOGIC);
                newFileName.append(StringConst.DOT);
                newFileName.append(time);
                newFileName.append(StringConst.DOT);
                newFileName.append(FileType.CSV.getSuffix());
                FileUtil.removeAndDeleteFile(srcFile, new File(newFileName.toString()), false);

                // copy to working file
                newFileName.setLength(IntDef.INT_ZERO);
                newFileName.append(backupFolder.getPath());
                newFileName.append(File.separatorChar);
                newFileName.append(IFBatchFileName.TTLOGIC);
                newFileName.append(StringConst.DOT);
                newFileName.append(time);
                FileUtil.removeAndDeleteFile(srcFile, new File(newFileName.toString()));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // return normal code
        return true;
    }

}
