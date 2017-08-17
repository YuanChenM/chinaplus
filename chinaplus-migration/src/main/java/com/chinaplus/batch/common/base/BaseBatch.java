package com.chinaplus.batch.common.base;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.service.BaseBatchService;

/**
 * The base batch class.
 */
public abstract class BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(BaseBatch.class);

    /** Base batch service */
    @Autowired
    protected BaseBatchService baseService;

    /** logger */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * get  ApplicationContext.
     * 
     * @return ApplicationContext
     */
    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }
    
    /**
     * Create the batch parameter object.
     * 
     * @param args parameters
     * @return parameter object
     */
    public abstract BaseBatchParam createBatchParam(String[] args);

    /**
     * Execution.
     * 
     * @param args parameters
     * @return execute result
     * @throws Exception if has error
     */
    public boolean execution(String[] args) throws Exception {

        Integer batchJobId = null;
        try {
            // Check the batch is already running or not
            String batchId = args[0];
            boolean isRunning = baseService.isBatchRunning(batchId);
            if (isRunning) {
                logger.error(String.format("Batch[%s] is already running.", batchId));
                return false;
            }

            // Create the batch parameter object
            int length = args.length;
            String[] data = null;
            if (length != 1) {
                data = new String[length - 1];
                length = data.length;
                for (int i = 0; i < length; i++) {
                    data[i] = args[i + 1];
                }
            }
            BaseBatchParam param = this.createBatchParam(data);

            // Batch start
            logger.info(String.format("========== Batch[%s] start. ==========", batchId));
            long start = System.currentTimeMillis();
            Integer officeId = param == null ? null : param.getOfficeId();
            Timestamp processDate = param == null ? null : param.getProcessDate();
            batchJobId = baseService.doBatchStart(batchId, officeId, processDate);
            
            // Execute main operation
            boolean success = this.doOperate(param);

            // Batch end
            baseService.doBatchEndSuccess(batchJobId);
            logger.info(String.format("Process end. Time:%sms", (System.currentTimeMillis() - start)));
            logger.info(String.format("========== Batch[%s] end. ==========", batchId));
            return success;
        } catch (Exception e) {
            if (batchJobId != null) {
                baseService.doBatchEndFail(batchJobId);
            }
            
            // not runtime exception and not business exception, we need print all exception message
            //if(!(e instanceof RuntimeException || e instanceof BusinessException)) {
            logger.error("========== Batch execution failed. ==========", e);
            //}
            //return false;
            throw e;
        }
    }

    /**
     * Main operation.
     * 
     * @param param Batch parameters
     * @throws Exception if has error
     * 
     * @return operate success
     */
    public abstract boolean doOperate(BaseBatchParam param) throws Exception;
    
}
