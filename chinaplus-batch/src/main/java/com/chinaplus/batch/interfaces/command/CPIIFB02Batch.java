/**
 * CPIIFB02Batch.java
 * 
 * @screen CPIIFB02
 * @author shi_xf
 */
package com.chinaplus.batch.interfaces.command;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.BatchStatus;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchId;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchStatus;
import com.chinaplus.batch.interfaces.bean.CPIIFB02Param;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TntIfBatch;
import com.chinaplus.common.entity.TntIfImpIp;
import com.chinaplus.common.service.ReceivedIpService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * CPIIFB02Batch.
 * Receive Data File from TT-LOGIX batch start class.
 * 
 * @author shi_xf
 */
@Component(IFBatchId.TTLOGIC_MAIN)
public class CPIIFB02Batch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB02Batch.class);

    /** The CPIIFB02Batch service */
    @Autowired
    private ReceivedIpService c02Service;

    @Override
    public BaseBatchParam createBatchParam(String[] args) {
        logger.info("batch CPIIFB02Batch start......");
        logger.info("parameter:" + args);

        TnmOffice officeInfo = paramsCheck(args);
        
        // get business pattern
        String businessPattern = args[BatchConst.INT_ZERO];

        // process batchDate
        Date batchDate = DateTimeUtil.parseDate(args[BatchConst.INT_TWO], DateTimeUtil.FORMAT_YYYYMMDD);

        // if batch date is null, then set office date
        if (batchDate == null) {
            batchDate = c02Service.getDBDateTime(officeInfo.getTimeZone());
        }

        // add flag for data migration
        Integer migrationFlag = null;
        if (args.length == BatchConst.INT_FOUR) {
            migrationFlag = StringUtil.toInteger(args[BatchConst.INT_THREE]);
        }

        // create param
        CPIIFB02Param param = new CPIIFB02Param();
        param.setOfficeCode(officeInfo.getOfficeCode());
        param.setOfficeId(officeInfo.getOfficeId());
        param.setBusinessPattern(businessPattern);
        param.setMigrationFlag(migrationFlag);
        // param.setProcessDate(batchDate);
        // param.setImpStockFlag(officeInfo.getImpStockFlag());
        param.setProcessDate(c02Service.getDBDateTimeByDefaultTimezone());
        param.setBatchDate(batchDate);

        return param;
    }

    @Override
    public boolean doOperate(BaseBatchParam param) throws Exception {
        logger.info("doOperate start......");
        int result = BatchConst.INT_ZERO;
        int totalResult = BatchConst.INT_ZERO;
        
        // get
        CPIIFB02Param castParam  = (CPIIFB02Param)param;
        
        // split business pattern
        List<String> patterns = Arrays.asList(castParam.getBusinessPattern().split(StringConst.COMMA));

        // Execute analysis batch for VV IP
        if (patterns.contains(String.valueOf(BusinessPattern.V_V))) {
            try {
                result = doReceiveData(param, ReceivedIpService.PROCESS_TYPE_VV);
                totalResult += result;
            } catch (Exception e) {
                totalResult += IntDef.INT_ONE;
                e.printStackTrace();
            }
        }

        // Execute analysis batch for AISIN IP
        if (patterns.contains(String.valueOf(BusinessPattern.AISIN))) {
            try {
                result = doReceiveData(param, ReceivedIpService.PROCESS_TYPE_AISIN);
                totalResult += result;
            } catch (Exception e) {
                totalResult += IntDef.INT_ONE;
                e.printStackTrace();
            }
        }

        // return result
        return totalResult == BatchStatus.SUCCESS;
    }

    /**
     * Batch for ReceiveData.
     * 
     * @param param parameter
     * @param processType processType
     * 
     * @return data count
     * @throws Exception e 
     */
    private int doReceiveData(BaseBatchParam param, int processType) throws Exception  {

        // CAST
        CPIIFB02Param castParam = (CPIIFB02Param) param;

        // Check last process is success or failure
        int result = BatchStatus.SUCCESS;
        TntIfBatch ifBatchInfo = c02Service.getBatchProcessTime(processType, castParam.getOfficeId());

        // if has data time
        if (ifBatchInfo != null) {
            logger.info("Last process is success.");
            // Prepare all IF_DATE_TIME from TNT_IF_IMP_IP
            List<TntIfImpIp> ifIpDateList = c02Service.getIfIpDateList(castParam.getOfficeId(), ifBatchInfo,
                processType);
            // loop by each IF_DATE_TIME
            if (ifIpDateList != null) {
                logger.info("IF_DATE_TIME count: " + ifIpDateList.size());
                for (TntIfImpIp ifImpIpInfo : ifIpDateList) {
                    // get time
                    Timestamp ifDateTime = ifImpIpInfo.getIfDateTime();
                    Integer officeId = castParam.getOfficeId();
                    logger.info("Start of process IF_DATE_TIME: "
                            + DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMMSS, ifDateTime));
                    try {
                        // save result
                        baseService.doInsertOrUpdateIfBatch(officeId, ifDateTime, IFBatchStatus.INCOMPLETE, processType);
                        // do batch process
                        c02Service.doReceiveIpForBatch(castParam.getOfficeId(), ifDateTime, processType,
                            castParam.getMigrationFlag());
                        // save result
                        baseService.doInsertOrUpdateIfBatch(officeId, ifDateTime, IFBatchStatus.SUCCESS, processType);
                    } catch (Exception e) {
                        // set fail
                        baseService.doInsertOrUpdateIfBatch(officeId, ifDateTime, IFBatchStatus.FAIL, processType);
                        break;
                    }
                    logger.info("End of process IF_DATE_TIME: "
                            + DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMMSS, ifDateTime));
                }
            }
        } else {
            // logger
            logger.info("Last process is failture.");
        }

        // Prepare actual outbound information
        c02Service.doPrepareActualOutboundInfo(castParam.getBatchDate(), castParam.getOfficeId(), null, processType);

        // return
        return result;
    }

    /**
     * Parameter check.
     * 
     * @param args args
     * @return time zone
     */
    private TnmOffice paramsCheck(String[] args) {
        // Check batch arguments
        // If size of parameter is not 1 or 2, write error log("Number of parameters is not vaild."), and stop batch
        if (args == null || (args.length != BatchConst.INT_THREE && args.length != BatchConst.INT_FOUR)) {

            logger.error("The arguments number of batch is incorrect.");
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

        return office;
    }

}
