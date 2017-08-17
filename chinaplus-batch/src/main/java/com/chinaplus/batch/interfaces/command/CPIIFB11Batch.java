/**
 * CPIIFB11Batch.java
 * 
 * @screen CPIIFB11
 * @author liu_yinchuan
 */

package com.chinaplus.batch.interfaces.command;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.InvenByDayParam;
import com.chinaplus.batch.common.consts.BatchConst.OnlineFlag;
import com.chinaplus.batch.common.consts.BatchConst.SubBatchOfByDay;
import com.chinaplus.batch.interfaces.bean.CPIIFB11Param;
import com.chinaplus.batch.interfaces.service.CPIIFB11Service;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.service.CfcRundownService;
import com.chinaplus.common.service.DailyAdjustKanbanService;
import com.chinaplus.common.service.InvoiceAdjService;
import com.chinaplus.common.service.PostGrGiService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Main process of inventory by day batch.
 * 
 * @author liu_yinchuan
 */
@Component(SubBatchOfByDay.INVENTORY_BY_DATE)
public class CPIIFB11Batch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB11Batch.class);

    /** The Service of inventory by day batch */
    @Autowired
    private CPIIFB11Service cpiifb11Service;

    /** The PostGrGiService service */
    @Autowired
    private PostGrGiService postGrGiService;

    /** The DailyAdjustKanbanService service */
    @Autowired
    private DailyAdjustKanbanService dailyAdjustKanbanService;

    /** The CfcRundownService service */
    @Autowired
    private CfcRundownService cfcRundownService;

    /** The InvoiceAdjService service */
    @Autowired
    private InvoiceAdjService invoiceAdjService;
    
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
        // if (args == null || args.length != BatchConst.INT_FOUR) {
        if (args == null || (args.length != BatchConst.INT_THREE && args.length != BatchConst.INT_FOUR) ) {

            // error
            logger.error("The arguments number of batch is incorrect.");
            throw new BusinessException();
        } else {

            // set parameters
            param = new CPIIFB11Param();
            /*
             * param.setOnlineFlag(StringUtil.toInteger(args[InvenByDayParam.IS_ONLINE]));
             * // if date is null, error
             * if (param.getOnlineFlag() == null) {
             * logger.error("Online Flag is incorrect(Please set On-Line Flag as 1: On-Line or 2: Off-Line).");
             * throw new BusinessException();
             * }
             */

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

            // if get sub process
            if (!StringUtil.isEmpty(args[InvenByDayParam.SUB_PROCESS])) {

                // set into process
                param.setSubProcess(args[InvenByDayParam.SUB_PROCESS].split(StringConst.COMMA));
            } else {
                logger.error("Sub process can not be empty.");
                throw new BusinessException();
            }
            
            // online
            if (args.length == BatchConst.INT_FOUR) {
                // set as online flag
                param.setOnlineFlag(StringUtil.toInteger(args[InvenByDayParam.IS_ONLINE]));
                if (param.getOnlineFlag() == null) {
                    param.setOnlineFlag(OnlineFlag.OFFLINE);
                }
            } else {
                // set as off line
                param.setOnlineFlag(OnlineFlag.OFFLINE);
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
        boolean result = true;
        // cast
        CPIIFB11Param baseParam = (CPIIFB11Param) param;
        
        // Execute post GR/GI.
        try {
            logger.info("Execute post GR/GI start......");
            postGrGiService.doGrGiPost(param.getOfficeId());
        } catch (Exception e) {
            result = false;
            logger.error("Execute post GR/GI has been failed.");
            e.printStackTrace();
        }

        // only off line will do adjust
        if (baseParam.getOnlineFlag().equals(OnlineFlag.OFFLINE)) {

            // do Daily Adjust Logic
            try {
                logger.info("do Daily Adjust Logic start......");
                dailyAdjustKanbanService.doAdjustment(param.getOfficeId());
            } catch (Exception e) {
                result = false;
                logger.error("do Daily Adjust Logic has been failed.");
                e.printStackTrace();

            }

            // doInvoiceAdj For DailyBatchLogic
            try {
                logger.info("do Invoice Daily Adjust Logic start......");
                invoiceAdjService.doInvoiceAdjForDailyBatchLogic(param.getOfficeId(), baseParam.getDate());
            } catch (Exception e) {
                result = false;
                logger.error("do Invoice Daily Adjust Logic has been failed.");
                e.printStackTrace();

            }

        }
        
        // do by day batch
        result = doByDayBatch(param);

        // only off line will do adjust
        if (baseParam.getOnlineFlag().equals(OnlineFlag.OFFLINE)) {

            // do customer forecast adjust
            try {
                logger.info("do customer forecast adjust start......");
                cfcRundownService.doDailyCfcRundown(param.getOfficeId(), baseParam.getDate());
            } catch (Exception e) {
                result = false;
                logger.error("do customer forecast adjust has been failed.");
                e.printStackTrace();
            }
        }

        logger.info("batch CPIIFB11Batch end......");
        return result;
    }

    /**
     * do batch of by date.
     * 
     * @param param parameter
     * @return result
     */
    private boolean doByDayBatch(BaseBatchParam param) {
        // get result
        boolean result = true;

        // cast
        CPIIFB11Param baseParam = (CPIIFB11Param) param;

        // get sub process
        String[] subProcess = baseParam.getSubProcess();
        List<String> subArr = Arrays.asList(subProcess);

        // prepare
        String[] args = new String[IntDef.INT_THREE];
        args[IntDef.INT_ONE] = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, baseParam.getDate());
        args[IntDef.INT_TWO] = baseParam.getOfficeCode();

        // If contains "CPIIFB12", will do Imp Stock By Day information save
        if (subArr.contains(SubBatchOfByDay.IMP_STOCK)) {
            try {
                // set arguments
                args[IntDef.INT_ZERO] = SubBatchOfByDay.IMP_STOCK;
                // execute
                boolean exeResult = ((BaseBatch) super.getApplicationContext().getBean(SubBatchOfByDay.IMP_STOCK))
                    .execution(args);
                // check
                if (!exeResult) {
                    logger.error("Process of Import Stock has been failed.");
                    result = false;
                }
            } catch (Exception e) {
                logger.error("Process of Import Stock has been failed.");
                result = false;
            }
        }

        // If contains "CPIIFB13", will do Imp Balance By Day information save
        if (subArr.contains(SubBatchOfByDay.IMP_BALANCE)) {
            try {
                // set arguments
                args[IntDef.INT_ZERO] = SubBatchOfByDay.IMP_BALANCE;
                // execute
                boolean exeResult = ((BaseBatch) super.getApplicationContext().getBean(SubBatchOfByDay.IMP_BALANCE))
                    .execution(args);
                // check
                if (!exeResult) {
                    logger.error("Process of Import Balance has been failed.");
                    result = false;
                }
            } catch (Exception e) {
                logger.error("Process of Import Balance has been failed.");
                result = false;
            }
        }

        // get last effective
        // If contains "CPIIFB14", will do Exp Parts Status information save
        if (subArr.contains(SubBatchOfByDay.PARTS_STATUS)) {
            // do service
            try {
                // set arguments
                args[IntDef.INT_ZERO] = SubBatchOfByDay.PARTS_STATUS;
                // execute
                boolean exeResult = ((BaseBatch) super.getApplicationContext().getBean(SubBatchOfByDay.PARTS_STATUS))
                    .execution(args);
                // check
                if (!exeResult) {
                    logger.error("Process of Exp Parts Status has been failed.");
                    result = false;
                }
            } catch (Exception e) {
                logger.error("Process of Exp Parts Status has been failed.");
                result = false;
            }
        }

        return result;
    }

}
