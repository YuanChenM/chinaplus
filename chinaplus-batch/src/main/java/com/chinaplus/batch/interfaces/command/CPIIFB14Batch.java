/**
 * CPIIFB11Batch.java
 * 
 * @screen CPIIFB11
 * @author liu_yinchuan
 */

package com.chinaplus.batch.interfaces.command;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
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
import com.chinaplus.batch.common.consts.BatchConst.BatchStatus;
import com.chinaplus.batch.common.consts.BatchConst.InvenByDayParam;
import com.chinaplus.batch.common.consts.BatchConst.SubBatchOfByDay;
import com.chinaplus.batch.interfaces.bean.CPIIFB11Param;
import com.chinaplus.batch.interfaces.bean.TnfExpPartsStatusEx;
import com.chinaplus.batch.interfaces.service.CPIIFB11Service;
import com.chinaplus.common.entity.TnfExpPartsStatus;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TntBatchJob;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Main process of inventory by day batch.
 * 
 * @author liu_yinchuan
 */
@Component(SubBatchOfByDay.PARTS_STATUS)
public class CPIIFB14Batch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB14Batch.class);

    /** The Service of inventory by day batch */
    @Autowired
    private CPIIFB11Service byDayService;

    /**
     * create the Batch Parameter.
     * 
     * @param args.
     */
    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        // defined
        CPIIFB11Param param = new CPIIFB11Param();

        // Check batch arguments
        if (args == null || args.length != BatchConst.INT_TWO) {

            // error
            logger.error("The arguments number of batch is incorrect.");
            throw new BusinessException();
        } else {

            // set parameters
            param = new CPIIFB11Param();

            // set parameters
            param.setDate(DateTimeUtil.parseDate(args[InvenByDayParam.DATE], DateTimeUtil.FORMAT_YYYYMMDD));
            // if date is null, error
            if (param.getDate() == null) {
                logger.error("Date is incorrect(Please set Date like:20160223).");
                throw new BusinessException();
            }

            // if office exist, set office code
            if (!StringUtil.isEmpty(args[InvenByDayParam.OFFICE_CODE])) {
                // set office code
                param.setOfficeCode(args[InvenByDayParam.OFFICE_CODE]);
                // check
                TnmOffice office = baseService.getOfficeInfo(param.getOfficeCode());
                if (office == null) {
                    logger.error("Office code does not an effective office.");
                    throw new BusinessException();
                }
                // set office id
                param.setOfficeId(office.getOfficeId());
            } else {
                logger.error("Office code can not be empty.");
                throw new BusinessException();
            }

            // set process date
            param.setProcessDate(baseService.getDBDateTimeByDefaultTimezone());
        }

        // return
        return param;
    }

    /**
     * the batch operate.
     * 
     * @param BaseBatchParam.
     * @return
     */
    @Override
    public boolean doOperate(BaseBatchParam param) throws Exception {
        logger.info("batch CPIIFB11Batch start......");
        // get result
        // cast
        CPIIFB11Param baseParam = (CPIIFB11Param) param;

        // do Exp Parts Status information save
        boolean result = this.doPrepareExpPartsStatus(baseParam);

        // check
        if (!result) {
            logger.error("Process of Exp Parts Status has been failed.");
            return result;
        }

        logger.info("batch CPIIFB11Batch end......");

        return result;
    }

    /**
     * Do prepare Export Parts Status for table TNF_EXP_PARTS_STATUS
     * 
     * @param baseParam base param
     * @throws BusinessException exception
     * 
     * @return successful or not
     */
    private boolean doPrepareExpPartsStatus(CPIIFB11Param baseParam) throws BusinessException {

        // get Handle Flag is 2, and then set as 98(already process by inventory by day)
        TntBatchJob jobParam = new TntBatchJob();
        jobParam.setBatchId(SubBatchOfByDay.PARTS_STATUS);
        jobParam.setOfficeId(baseParam.getOfficeId());
        jobParam.setStatus(BatchStatus.SUCCESS);

        // get Latest Batch Time.
        Timestamp lastBatchTime = byDayService.getLatestBatchTime(jobParam);
        Timestamp thisBatchTime = baseParam.getProcessDate();
        Timestamp currentTime = byDayService.getDBDateTimeByDefaultTimezone();

        // prepare parameter
        TnfExpPartsStatusEx statusParam = new TnfExpPartsStatusEx();
        statusParam.setLastBatchTime(lastBatchTime);
        statusParam.setCurrBatchTime(thisBatchTime);
        statusParam.setOfficeId(baseParam.getOfficeId());

        // get current date
        Date endDate = baseParam.getDate();

        // get last end date
        Date lastEndDate = byDayService.getLastPartsStatusDate(statusParam);
        // get the updated end date for parts status
        Date minEndDate = byDayService.getLastDateOfPartStatus(statusParam);
        // get data from database
        if (minEndDate == null) {
            // check date
            if (lastEndDate == null) {
                // check date
                minEndDate = endDate;
            } else {
                // if end date before
                if (endDate.before(lastEndDate)) {
                    // get from last end date
                    minEndDate = endDate;
                } else {
                    // get from last end date
                    minEndDate = lastEndDate;
                }
            }
        } else if (lastEndDate != null && minEndDate.after(lastEndDate)) {
            minEndDate = lastEndDate;
        }
        
        // force refresh to last date
        if (lastEndDate != null && endDate.before(lastEndDate)) {
            endDate = lastEndDate;
        }
        
        // adjust min end date inf
        Date bsDate = DateTimeUtil.parseDate(ConfigManager.getBatchStartDate());
        if (bsDate != null && bsDate.after(minEndDate)) {
            minEndDate = bsDate;
        }

        // define
        Map<String, Map<Date, Map<String, TnfExpPartsStatusEx>>> partsOrderStatusMap = new HashMap<String, Map<Date, Map<String, TnfExpPartsStatusEx>>>();
        Map<String, Map<Date, TnfExpPartsStatusEx>> partsStatusMap = new HashMap<String, Map<Date, TnfExpPartsStatusEx>>();
        Map<String, Map<String, BigDecimal>> directInvoiceMap = new HashMap<String, Map<String, BigDecimal>>();
        Map<String, Map<String, BigDecimal>> preInvoiceMap = new HashMap<String, Map<String, BigDecimal>>();
        Map<String, Map<String, BigDecimal>> expInboundMap = new HashMap<String, Map<String, BigDecimal>>();
        Map<String, Map<String, BigDecimal>> expPlanInboundMap = new HashMap<String, Map<String, BigDecimal>>();
        Map<String, Map<Date, TnfExpPartsStatusEx>> existPartsStatusMap = new HashMap<String, Map<Date, TnfExpPartsStatusEx>>();

        // get data from database
        // set end date
        statusParam.setEndDate(minEndDate);
        
        // loop
        Date runStartDate = minEndDate;
        Date runEndDate = null;
        Integer tansCount = StringUtil.toInteger(ConfigManager.getBatchTransCount());
        // split when 10 days will as a transaction
        while (!runStartDate.after(endDate)) {
            
            // set run end date
            if (tansCount != null) {
                // add step
                runEndDate = DateTimeUtil.addDay(runStartDate, tansCount);
                // if after end date
                if (runEndDate.after(endDate)) {
                    runEndDate = endDate;
                }
            } else {
                runEndDate = endDate;
            }

            // set min end date
            statusParam.setEndDate(DateTimeUtil.addDay(runEndDate, IntDef.INT_ONE));
            statusParam.setLastEndDate(runStartDate);

            // get export balance qty
            List<TnfExpPartsStatusEx> expBalanceList = byDayService.getExpBalanceformation(statusParam);
            byDayService.prepareDayPartsStatusList(expBalanceList, partsOrderStatusMap);
            expBalanceList = null;

            // day plan in-bound
            List<TnfExpPartsStatusEx> planIbInfoList = byDayService.getExpInboundPlanInformation(statusParam);
            byDayService.prepareDayPartsStatusList(planIbInfoList, partsOrderStatusMap);
            planIbInfoList = null;

            // day export in-bound
            List<TnfExpPartsStatusEx> inboundInfoList = byDayService.getExpInboundInformation(statusParam);
            byDayService.prepareDayPartsStatusList(inboundInfoList, partsOrderStatusMap);
            inboundInfoList = null;

            // day out-bound(invoice)
            List<TnfExpPartsStatusEx> invoiceInfoList = byDayService.getInvoiceInformation(statusParam);
            byDayService.prepareDayPartsStatusList(invoiceInfoList, partsOrderStatusMap);
            invoiceInfoList = null;

            // day direct received Qty
            List<TnfExpPartsStatusEx> preExpPlanInboundInfoList = byDayService.getPreExpInboundPlanInformation(statusParam);
            byDayService.prepareAreadyInboundInfoList(preExpPlanInboundInfoList, expPlanInboundMap);
            preExpPlanInboundInfoList = null;

            // day direct received Qty
            List<TnfExpPartsStatusEx> preExpInboundInfoList = byDayService.getPreExpInboundInformation(statusParam);
            byDayService.prepareAreadyInboundInfoList(preExpInboundInfoList, expInboundMap);
            preExpInboundInfoList = null;

            // day direct received Qty
            List<TnfExpPartsStatusEx> dicReceivedInfoList = byDayService.getAreadyDirectInformation(statusParam);
            byDayService.prepareAreadyInboundInfoList(dicReceivedInfoList, directInvoiceMap);
            dicReceivedInfoList = null;

            // if bs start
            if (bsDate != null && bsDate.equals(runStartDate)) {
                // get qty
                List<TnfExpPartsStatusEx> preInvoiceInfoList = byDayService.getAreadyInvoiceformation(statusParam);
                byDayService.prepareAreadyInboundInfoList(preInvoiceInfoList, preInvoiceMap);
                preInvoiceInfoList = null;
            }

            // prepare all data
            byDayService.mergeDayPartsStatusList(partsOrderStatusMap, partsStatusMap, directInvoiceMap, expInboundMap,
                expPlanInboundMap, preInvoiceMap);

            // get all data from database
            statusParam.setLastEndDate(DateTimeUtil.addDay(runStartDate, IntDef.INT_N_ONE));
            List<TnfExpPartsStatusEx> allPartsStatusList = byDayService.getExistPartsStatusList(statusParam);
            byDayService.prepareExistPartsStatusList(allPartsStatusList, existPartsStatusMap, runEndDate);
            allPartsStatusList = null;

            // prepare all data
            List<TnfExpPartsStatus> preparedPartsStatusList = byDayService.prepareExpPartsStatusList(partsStatusMap,
                existPartsStatusMap, currentTime, runStartDate, runEndDate);

            // save into database
            byDayService.doSavePartsStatusListIntoDb(preparedPartsStatusList);
            preparedPartsStatusList = null;

            // set next loop
            runStartDate = DateTimeUtil.addDay(runEndDate, IntDef.INT_ONE);
        }

        // return
        return true;
    }

}
