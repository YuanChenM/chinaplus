/**
 * @screen Run-Down Batch main process
 * @author liu_yinchuan
 */
package com.chinaplus.batch.online.command;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchId;
import com.chinaplus.batch.common.consts.BatchConst.OnlineBatch;
import com.chinaplus.batch.online.bean.CPIIFB00Param;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Main Batch process for run-down batch.
 * 
 * @author liu_yinchuan
 */
@Component(OnlineBatch.CPIIFB00)
public class CPIIFB00Batch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB00Batch.class);

    /**
     * Prepare run-down parameter.
     * 
     * @param args batch parameter
     */
    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        // parameter object initialization
        CPIIFB00Param param = new CPIIFB00Param();

        // check args
        if (args == null || (args.length != BatchConst.INT_THREE)) {
            logger.error("The arguments number of batch is incorrect.");
            throw new BusinessException();
        }

        // get business pattern
        String businessPattern = args[BatchConst.INT_ZERO];
        // If Office Code is blank, write error log("Office Code can not be empty."), and stop batch
        if (StringUtil.isNullOrEmpty(businessPattern)) {
            logger.error("BusinessPattern can not be empty.");
            throw new BusinessException();
        }

        String officeCode = args[BatchConst.INT_ONE];
        // If Office Code is blank, write error log("Office Code can not be empty."), and stop batch
        if (StringUtil.isNullOrEmpty(officeCode)) {
            logger.error("Office Code can not be empty.");
            throw new BusinessException();
        }

        // If office Code does not exist in TNM_OFFICE, write error log("Office Code does not exist."), and stop batch
        TnmOffice office = baseService.getOfficeInfo(officeCode);
        if (office == null) {
            logger.error("Office Code does not exist.");
            throw new BusinessException();
        }

        // process batchDate
        String batchDate = null;
        if (args.length == BatchConst.INT_THREE) {
            // prepare date
            batchDate = args[BatchConst.INT_TWO];
        } else {
            // get date from database
            Timestamp officeTime = baseService.getDBDateTime(office.getTimeZone());
            batchDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, officeTime);
        }

        // create param
        param.setOfficeCode(officeCode);
        param.setBusinessPattern(businessPattern);
        param.setBatchDate(batchDate);
        param.setProcessDate(baseService.getDBDateTimeByDefaultTimezone());

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
        logger.info("doOperate", "batch CPIIFB00Batch start......");

        // cast
        CPIIFB00Param castParam = (CPIIFB00Param) baseParam;

        // get result
        boolean result = true;

        // If contains "CPIIFB01", will do SSMS batch
        try {

            // set arguments
            String[] args = new String[IntDef.INT_ONE];
            args[IntDef.INT_ZERO] = IFBatchId.SSMS_MAIN;

            // execute
            boolean runRe = ((BaseBatch) super.getApplicationContext().getBean(IFBatchId.SSMS_MAIN)).execution(args);

            // check
            if (!runRe) {
                logger.error("Process of SSMS logic batch has been failed.");
                result = false;
            }
        } catch (Exception e) {
            logger.error("Process of SSMS logic batch has been failed.");
            result = false;
        }

        // If contains "CPIIFB02", will do TT-LOGIX batch
        try {

            // set arguments
            String[] args = new String[IntDef.INT_FOUR];
            args[IntDef.INT_ZERO] = IFBatchId.TTLOGIC_MAIN;
            args[IntDef.INT_ONE] = castParam.getBusinessPattern();
            args[IntDef.INT_TWO] = castParam.getOfficeCode();
            args[IntDef.INT_THREE] = castParam.getBatchDate();

            // execute
            boolean runRe = ((BaseBatch) super.getApplicationContext().getBean(IFBatchId.TTLOGIC_MAIN)).execution(args);

            // check
            if (!runRe) {
                logger.error("Process of TT-LOGIX batch has been failed.");
                result = false;
            }
        } catch (Exception e) {
            logger.error("Process of TT-LOGIX batch has been failed.");
            result = false;
        }

        // batch process logic end
        logger.info("doOperate", "batch CPIIFB00Batch end......");

        // return OK.
        return result;
    }

}
