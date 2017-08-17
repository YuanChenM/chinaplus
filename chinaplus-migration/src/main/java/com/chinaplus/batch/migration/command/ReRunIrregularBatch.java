/**
 * @screen ReRunShippingPlan Batch main process
 * @author cheng_xingfei
 */
package com.chinaplus.batch.migration.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.migration.bean.MigrationComParam;
import com.chinaplus.batch.migration.service.ReRunIrregularService;
import com.chinaplus.common.consts.CodeConst.HandleFlag;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.service.ReceivedIpService;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.exception.BusinessException;

/**
 * Main Batch process for ReRunShippingPlan batch.
 * 
 * @author liu_yinchuan
 */
@Component("ReRunIrreg")
public class ReRunIrregularBatch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ReRunIrregularBatch.class);

    @Autowired
    private ReRunIrregularService service;

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
        if (args == null || args.length != BatchConst.INT_ONE) {

            logger.error("The arguments number of batch is incorrect.");
            throw new BatchException();
        }

        // set parameters
        param = new MigrationComParam();
        param.setOfficeCode(args[BatchConst.INT_ZERO]);

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
        logger.info("doOperate", "batch ReRunIrregularInfo start......");

        // rerun
        service.doReceiveIpForIrregular(baseParam.getOfficeId(), ReceivedIpService.PROCESS_TYPE_VV,
            HandleFlag.MAIN_INFORMATION_DOESNOT_MATCHED);

        logger.info("doOperate", "batch ReRunIrregularInfo end......");
        return true;
    }
}
