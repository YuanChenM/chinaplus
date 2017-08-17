/**
 * CPIIFB01Batch.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.command;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.BatchType;
import com.chinaplus.batch.common.consts.BatchConst.BatchTypeName;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchId;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchStatus;
import com.chinaplus.batch.interfaces.bean.BatchLog;
import com.chinaplus.batch.interfaces.bean.CPIIFB01Param;
import com.chinaplus.batch.interfaces.common.BatchUtils;
import com.chinaplus.batch.interfaces.service.CPIIFB01Service;
import com.chinaplus.batch.interfaces.service.CPIIFB03Service;
import com.chinaplus.batch.interfaces.service.CPIIFB04Service;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataType;
import com.chinaplus.common.entity.SsmsCommonParam;
import com.chinaplus.common.service.SsmsCommonService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BatchException;

/**
 * CPIIFB01Batch
 * The SSMS logic batch start class.
 * 
 * @author yang_jia1
 */
@Component(IFBatchId.SSMS_MAIN)
public class CPIIFB01Batch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB01Batch.class);

    /** The CPIIFB01Batch service */
    @Autowired
    private CPIIFB01Service c01Service;

    /** The CPIIFB03Batch service */
    @Autowired
    private CPIIFB03Service c03Service;

    /** The CPIIFB04Batch service */
    @Autowired
    private CPIIFB04Service c04Service;

    /** The SsmsCommonService */
    @Autowired
    private SsmsCommonService ssmsCommonService;

    /**
     * create the Batch Param.
     * 
     * @param args.
     */
    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        CPIIFB01Param para = new CPIIFB01Param();
        /*
        if (args == null || (args.length != BatchConst.INT_ONE && args.length != BatchConst.INT_TWO)) {

            logger.error("The arguments number of batch is incorrect.");
            throw new RuntimeException();
        }

        String runSourceType = args[BatchConst.INT_ZERO];
        List<Integer> expPartsIdList = new ArrayList<Integer>();
        if ("2".equals(runSourceType)) {
            if (args.length != BatchConst.INT_TWO) {
                logger.error("The arguments number of batch is incorrect.");
                throw new RuntimeException();
            } else {
                String[] expPartsIds = args[BatchConst.INT_ONE].split(",");
                for (String expPartsId : expPartsIds) {
                    expPartsIdList.add(Integer.valueOf(expPartsId));
                }
            }
        }

        para.setExpPartsId(expPartsIdList);
         */
        // set process date
        para.setProcessDate(c01Service.getDBDateTimeByDefaultTimezone());

        return para;
    }

    /**
     * the batch operate.
     * 
     * @param BaseBatchParam.
     * @return
     */
    @Override
    public boolean doOperate(BaseBatchParam para) throws Exception {
        logger.info("doOperate", "batch CPIIFB01Batch start......");

        // if not ok, throw
        CPIIFB01Param param = null;
        if (para == null) {
            logger.error("The batch parameters is incorrect, please at least have one subBatchId.");
            throw new RuntimeException();
        } else {
            param = (CPIIFB01Param) para;
        }

        Timestamp systemDateTime = c01Service.getDBDateTimeByDefaultTimezone();

        BatchLog batchLogParam = new BatchLog();

        /** ———————————————————————— second step ———————————————————————— **/
        batchLogParam.setIfDateTime(systemDateTime);
        List<Timestamp> cuDateTimes = c01Service.queryCustomerLogTimes(batchLogParam);
        if (cuDateTimes != null && !cuDateTimes.isEmpty()) {
            for (Timestamp timestamp : cuDateTimes) {
                try {
                    param.setIfDateTime(timestamp);
                    // do customer service
                    c03Service.doCustomerLogic(param);
                } catch (Exception e) {
                    logger.warn("The step doCustomerLogic executed fail.");
                    logger.error(e.getMessage(), e);
                    throw e;
                }
            }
        }

        /** ———————————————————————— third step ———————————————————————— **/
        List<Timestamp> partsDateTimes = c01Service.queryPartsLogTimes(batchLogParam);
        if (partsDateTimes != null && !partsDateTimes.isEmpty()) {

            for (Timestamp timestamp : partsDateTimes) {
                // do parts service
                try {
                    param.setIfDateTime(timestamp);
                    c04Service.doPartsLogic(param);
                } catch (Exception e) {
                    logger.warn("The step doPartsLogic executed fail.");
                    logger.error(e.getMessage(), e);
                    throw e;
                }
            }
        }

        batchLogParam.setBatchType(BatchType.SSMS);
        batchLogParam.setBatchTypeName(BatchTypeName.SSMS);
        // boolean isContinue = true;
        int lastIsSuccess = 0;
        BatchLog batchLog = c01Service.batchLogStatusIsNormal(batchLogParam);
        if (batchLog != null) {
            if (batchLog.getStatus().intValue() == IFBatchStatus.FAIL || batchLog.getStatus().intValue() == IFBatchStatus.INCOMPLETE) {
                // isContinue = false;
                lastIsSuccess = IntDef.INT_ONE;
            } else {
                lastIsSuccess = IntDef.INT_TWO;
            }
        } else {
            lastIsSuccess = IntDef.INT_THREE;
        }

        // if (!isContinue) {
        // logger.error("have fail batch log.");
        // throw new RuntimeException();
        // }

        /** ———————————————————————— fourth step ———————————————————————— **/
        batchLogParam.setBatchType(BatchType.SSMS);
        batchLogParam.setBatchTypeName(BatchTypeName.SSMS);
        batchLogParam.setLastIsSuccess(lastIsSuccess);
        if (lastIsSuccess == IntDef.INT_ONE) {
            batchLogParam.setIfDateTime(batchLog.getIfDateTime());
        } else if (lastIsSuccess == IntDef.INT_TWO) {
            batchLogParam.setIfDateTime(batchLog.getIfDateTime());
        } else if (lastIsSuccess == IntDef.INT_THREE) {
            batchLogParam.setIfDateTime(new Timestamp(0));
        }

        // do ssms business service
        List<Timestamp> ifDateTimes = c01Service.querySSMSBatchLogTimes(batchLogParam);
        BatchUtils.sortList(ifDateTimes);

        SsmsCommonParam commonParam = new SsmsCommonParam();

        if (ifDateTimes != null && !ifDateTimes.isEmpty()) {
            for (Timestamp ifDateTime : ifDateTimes) {

                super.baseService.doInsertOrUpdateIfBatch(null, ifDateTime, IFBatchStatus.INCOMPLETE,
                    SyncTimeDataType.SSMS);

                commonParam.setIfDateTime(ifDateTime);
                commonParam.setOfficeCode(param.getOfficeCode());
                commonParam.setExpPartsId(null);

                Map<String, Integer> resultMap = new HashMap<String, Integer>();
                try {
                    resultMap = ssmsCommonService.doSSMSLogic(commonParam);
                } catch (BatchException e) {
                    logger.error("SSMSLogic execute fail (BatchException) : " + e);
                    logger.error(e.getMessage(), e);

                    /**
                     * update tnt_if_batch's status to 0 ('FAIL')
                     */
                    super.baseService.doInsertOrUpdateIfBatch(null, ifDateTime, IFBatchStatus.FAIL,
                        SyncTimeDataType.SSMS);
                    throw e;

                } catch (Exception e) {
                    logger.error("SSMSLogic execute fail (Exception) : " + e);
                    logger.error(e.getMessage(), e);

                    /**
                     * update tnt_if_batch's status to 0 ('FAIL')
                     */
                    super.baseService.doInsertOrUpdateIfBatch(null, ifDateTime, IFBatchStatus.FAIL,
                        SyncTimeDataType.SSMS);
                    throw e;
                }

                Integer ssmsExecuteResult = resultMap.get("executeResult");
                Integer isSystemDataNum = resultMap.get("isSystemDataNum");

                if (isSystemDataNum == BatchConst.INT_ZERO) {
                    logger.info("no system data !");
                    /**
                     * update tnt_if_batch's status to 1 ('SUCCESS')
                     */
                    super.baseService.doInsertOrUpdateIfBatch(null, ifDateTime, IFBatchStatus.SUCCESS,
                        SyncTimeDataType.SSMS);
                    continue;
                }

                if (ssmsExecuteResult != BatchConst.INT_ONE) {
                    logger.warn("The step doSSMSLogic executed fail.");

                    /**
                     * update tnt_if_batch's status to 0 ('FAIL')
                     */
                    super.baseService.doInsertOrUpdateIfBatch(null, ifDateTime, IFBatchStatus.FAIL,
                        SyncTimeDataType.SSMS);
                    return false;
                }

                /** ———————————————————————— fivth step ———————————————————————— **/
                if (ssmsExecuteResult == BatchConst.INT_ONE) {
                    try {

                        /**
                         * update tnt_if_batch's status to 1 ('SUCCESS')
                         */
                        super.baseService.doInsertOrUpdateIfBatch(null, ifDateTime, IFBatchStatus.SUCCESS,
                            SyncTimeDataType.SSMS);
                        logger.info("ssms batch process successful. insertOrUpdate tnt_if_batch : " + ifDateTime);
                    } catch (Exception e) {
                        logger.error("insertOrUpdate tnt_if_batch exception:" + e);
                    }
                }
            }
        }

        logger.info("doOperate", "batch CPIIFB01Batch end......");
        return true;
    }
}
