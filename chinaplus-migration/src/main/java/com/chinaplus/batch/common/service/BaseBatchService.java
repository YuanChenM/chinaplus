package com.chinaplus.batch.common.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.BatchStatus;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataType;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataTypeName;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TntBatchJob;
import com.chinaplus.common.entity.TntIfBatch;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.StringUtil;

/**
 * The base batch service class.
 * @author yang_jia1
 */
@Service
@Component("BaseBatchService")
public class BaseBatchService extends BaseService {

    /**
     * Check the batch is already running or not.
     * 
     * @param batchId batch ID
     * @return check result
     */
    public boolean isBatchRunning(String batchId) {

        // Get the relation batch IDs
        List<String> relationIds = new ArrayList<String>();
        relationIds.add(batchId);
        String relationBatchs = ConfigManager.getProperty(BatchConst.BATCH_RELATION_PREFIX + batchId);
        if (!StringUtil.isEmpty(relationBatchs)) {
            String[] batchIds = relationBatchs.split(BatchConst.COMMA);
            for (String id : batchIds) {
                relationIds.add(id);
            }
        }

        // Search the running relation batch
        PageParam param = new PageParam();
        param.setSelectedDatas(relationIds);
        int count = super.getDatasCount(SQLID_FIND_COUNT, param);

        return count > 0 ? true : false;
    }

    /**
     * Batch start operation.
     * 
     * @param batchId batch ID
     * @param officeId office ID
     * @param processDate process date
     * @return batch job ID
     */
    public Integer doBatchStart(String batchId, Integer officeId, Timestamp processDate) {

        Timestamp currentTime = super.getDBDateTimeByDefaultTimezone();
        TntBatchJob job = new TntBatchJob();
        job.setBatchId(batchId);
        job.setStartTime(currentTime);
        job.setEndTime(null);
        job.setStatus(BatchStatus.RUNNING);
        job.setOfficeId(officeId);
        job.setProcessDate(processDate);
        job.setCreatedBy(BatchConst.BATCH_USER_ID);
        job.setCreatedDate(currentTime);
        job.setUpdatedBy(BatchConst.BATCH_USER_ID);
        job.setUpdatedDate(currentTime);
        job.setVersion(1);
        
        //return this.baseMapper.update(this.getSqlId("addBatchJob"), job);
        return (Integer) super.doInsert(job);
    }

    /**
     * Batch success end operation.
     * 
     * @param batchJobId batch job ID
     */
    public void doBatchEndSuccess(Integer batchJobId) {

        Timestamp currentTime = getDBDateTimeByDefaultTimezone();
        TntBatchJob job = super.getOneById(TntBatchJob.class, batchJobId);
        job.setEndTime(currentTime);
        job.setStatus(BatchStatus.SUCCESS);
        job.setUpdatedBy(BatchConst.BATCH_USER_ID);
        job.setUpdatedDate(currentTime);
        super.doUpdate(job);
    }

    /**
     * Batch fail end operation.
     * 
     * @param batchJobId batch job ID
     */
    public void doBatchEndFail(Integer batchJobId) {

        Timestamp currentTime = getDBDateTimeByDefaultTimezone();
        TntBatchJob job = super.getOneById(TntBatchJob.class, batchJobId);
        job.setEndTime(currentTime);
        job.setStatus(BatchStatus.FAIL);
        job.setUpdatedBy(BatchConst.BATCH_USER_ID);
        job.setUpdatedDate(currentTime);
        super.doUpdate(job);
    }

    /**
     * get office id by office code.
     * 
     * @param officeCode office code
     * @return office information
     */
    public TnmOffice getOfficeInfo(String officeCode) {

        // get param
        TnmOffice param = new TnmOffice();
        param.setOfficeCode(officeCode);

        return super.getOneByEntity(param);
    }

    /**
     * Insert a new record into TNT_IF_BATCH.
     * 
     * @param officeId officeId
     * @param ifDateTime IF Date Time
     * @param status Status
     * @param batchType Batch Type
     */
    public void doInsertOrUpdateIfBatch(Integer officeId, Timestamp ifDateTime, int status, int batchType) {
        // parameter
        TntIfBatch param = new TntIfBatch();
        if (officeId != null) {
            param.setOfficeId(officeId);
        }
        param.setBatchType(batchType);

        // check exist
        TntIfBatch ifBatch = super.baseDao.findOne(param);
        Timestamp dbTime = getDBDateTimeByDefaultTimezone();
        // add new
        if (ifBatch == null) {
            // reset
            ifBatch = new TntIfBatch();
            ifBatch.setOfficeId(officeId);
            ifBatch.setBatchType(batchType);
            String typeName = null;
            if (batchType == SyncTimeDataType.SSMS) {
                typeName = SyncTimeDataTypeName.SSMS;
            } else if (batchType == SyncTimeDataType.TT_LOGIX_VV) {
                typeName = SyncTimeDataTypeName.TT_LOGIX_VV;
            } else {
                typeName = SyncTimeDataTypeName.TT_LOGIX_AISIN;
            }
            ifBatch.setBatchTypeName(typeName);
            ifBatch.setCreatedBy(BatchConst.BATCH_USER_ID);
            ifBatch.setCreatedDate(dbTime);
            ifBatch.setVersion(IntDef.INT_ONE);
        }
        // set status
        ifBatch.setIfDateTime(ifDateTime);
        ifBatch.setStatus(status);
        ifBatch.setUpdatedBy(BatchConst.BATCH_USER_ID);
        ifBatch.setUpdatedDate(dbTime);
        baseDao.saveOrUpdate(ifBatch);
    }
}
