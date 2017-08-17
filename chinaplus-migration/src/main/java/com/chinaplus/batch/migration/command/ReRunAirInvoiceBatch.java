/**
 * @screen ReRunAirInvoice Batch main process
 * @author liu_yinchuan
 */
package com.chinaplus.batch.migration.command;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.util.TxtReader;
import com.chinaplus.batch.migration.bean.MigrationComParam;
import com.chinaplus.batch.migration.service.ReRunAirInvoiceService;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.exception.BusinessException;

/**
 * Main Batch process for ReRunAirInvoice batch.
 * 
 * @author liu_yinchuan
 */
@Component("AirInvoice")
public class ReRunAirInvoiceBatch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ReRunAirInvoiceBatch.class);

    @Autowired
    private ReRunAirInvoiceService service;

    /**
     * Prepare run-down parameter.
     * 
     * @param args batch parameter
     */
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

        // set parameters
        param = new MigrationComParam();
        param.setOfficeCode(args[BatchConst.INT_ZERO]);
        param.setFilePath(args[BatchConst.INT_ONE]);

        // get date
        // check
        TnmOffice office = baseService.getOfficeInfo(param.getOfficeCode());
        if (office == null) {
            logger.error("Office code does not an effective office.");
            throw new BusinessException();
        }
        // set office id
        param.setOfficeId(office.getOfficeId());
        // set porcess date
        param.setProcessDate(baseService.getDBDateTime(office.getTimeZone()));

        // return
        return param;
    }

    /**
     * Main process logic for rundown batch.
     * 
     * @param BaseBatchParam.
     * @return the result of current operation
     */
    @Override
    public boolean doOperate(BaseBatchParam baseParam) throws Exception {

        // batch process logic start
        logger.info("doOperate", "batch ReRunShippingPlan start......");

        // cast
        MigrationComParam castParam = (MigrationComParam) baseParam;

        // get Invoice List
        List<String> invoiceList = TxtReader.readInvoiceFile(castParam.getFilePath());
        
        // get timestamp
        Timestamp time = service.getDBDateTimeByDefaultTimezone();

        // loop process
        for (String invoiceNo : invoiceList) {
            
            // start log
            logger.info(String.format("Start of process Invoice: [%s].", invoiceNo));

            // do process logic
            service.doPrepareAirInvoiceInfo(castParam.getOfficeCode(), invoiceNo, time);

            // end log
            logger.info(String.format("End of process Invoice: [%s].", invoiceNo));
        }

        // end log
        logger.info("doOperate", "batch ReRunShippingPlan end......");

        // return
        return true;
    }
}
