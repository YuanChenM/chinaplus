package com.chinaplus.batch.common.command;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.chinaplus.batch.common.base.BaseBatch;

/**
 * The batch start class.
 */
public class StartBatchMain {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(StartBatchMain.class);

    /**
     * The batch start main function.
     * 
     * @param args batch arguments
     */
    @SuppressWarnings("resource")
    public static void main(String[] args) {

        try {

            // Initialization logger 
            DOMConfigurator.configure(StartBatchMain.class.getClassLoader().getResource("log4j.xml"));
        
            // Check batch arguments
            if (args.length <= 0) {
                logger.error("The arguments of batch is not correct.");
                System.exit(1);
            }
            
            // Load configuration file
            logger.debug("Load root-context.xml start.");
            ApplicationContext context = new FileSystemXmlApplicationContext(new String[] {
                "classpath:/spring-batch/root-context.xml" });
            logger.debug("Load root-context.xml end.");

            // Get the batch to execute
            String batchId = args[0];
            BaseBatch batch = context.getBean(batchId, BaseBatch.class);
            if (batch == null) {
                logger.error(String.format("The batch ID[%s] is not correct.", batchId));
                System.exit(1);
            }

            // Execute the batch
            if (batch.execution(args)) {
                System.exit(0);
            } else {
                System.exit(1);
            }
        } catch (Exception e) {
            logger.error("========== Batch execution failed. ==========", e);
            System.exit(1);
        }
    }

}
