package com.chinaplus.batch.common.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.consts.BatchConst.BatchStatus;

/**
 * The batch start class.
 */ 
@Component
public class OnlineBatchMain {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(OnlineBatchMain.class);
    
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
     * The batch start main function.
     * 
     * @param batchId batch id
     * @param param parameter
     * 
     * @return result
     */
    public int doProcess(String batchId, String[] param) {

        try {

            // Initialization logger
            //DOMConfigurator.configure(OnlineBatchMain.class.getClassLoader().getResource("log4j.xml"));

            // Load configuration file
            /*ApplicationContext context = new FileSystemXmlApplicationContext(
                new String[] { "classpath:/spring-batch/root-context.xml" });*/

            // Get the batch to execute
            BaseBatch batch = applicationContext.getBean(batchId, BaseBatch.class);
            if (batch == null) {
                logger.error(String.format("The batch ID[%s] is not correct.", batchId));
                return BatchStatus.FAIL;
            }

            // Execute the batch
            if (batch.execution(param)) {
                return BatchStatus.SUCCESS;
            } else {
                return BatchStatus.FAIL;
            }
        } catch (Exception e) {
            logger.error("========== Batch execution failed. ==========", e);
            return BatchStatus.FAIL;
        }
    }

}
