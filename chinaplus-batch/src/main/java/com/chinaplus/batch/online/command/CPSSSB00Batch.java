/**
 * @screen Run-Down Batch main process
 * @author liu_yinchuan
 */
package com.chinaplus.batch.online.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.base.BaseFileBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.OnlineBatch;
import com.chinaplus.batch.common.consts.BatchConst.OnlineFlag;
import com.chinaplus.batch.common.consts.BatchConst.StockBatchId;
import com.chinaplus.batch.common.consts.BatchConst.SubBatchOfByDay;
import com.chinaplus.batch.online.bean.CPSSSB00Param;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;

/**
 * Main Batch process for run-down batch.
 * 
 * @author liu_yinchuan
 */
@Component(OnlineBatch.CPSSSB00)
public class CPSSSB00Batch extends BaseFileBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPSSSB00Batch.class);

    /**
     * Prepare run-down parameter.
     * 
     * @param args batch parameter
     */
    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        // parameter object initialization
        CPSSSB00Param param = null;

        // Check batch arguments
        if (args == null || args.length != BatchConst.INT_TWO) {
            // error
            logger.error("The arguments number of batch is incorrect.");
            throw new BusinessException();
        }

        // set parameters
        param = new CPSSSB00Param();
        param.setOfficeCode(args[IntDef.INT_ZERO]);
        param.setStockDate(args[IntDef.INT_ONE]);

        // set process date
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
        logger.info("doOperate", "batch CPSSSB00Batch start......");
        
        // cast
        CPSSSB00Param castParam = (CPSSSB00Param) baseParam;

        // inventory by day
        try {
            
            // prepare sub
            StringBuffer subPro = new StringBuffer();
            subPro.append(SubBatchOfByDay.IMP_STOCK);
            subPro.append(StringConst.COMMA);
            subPro.append(SubBatchOfByDay.IMP_BALANCE);
            subPro.append(StringConst.COMMA);
            subPro.append(SubBatchOfByDay.PARTS_STATUS);

            // set arguments
            String[] args = new String[IntDef.INT_FIVE];
            args[IntDef.INT_ZERO] = SubBatchOfByDay.INVENTORY_BY_DATE;
            args[IntDef.INT_ONE] = castParam.getStockDate();
            args[IntDef.INT_TWO] = castParam.getOfficeCode();
            args[IntDef.INT_THREE] = subPro.toString();
            args[IntDef.INT_FOUR] = String.valueOf(OnlineFlag.ONLINE);
            
            // execute
            boolean runRe = ((BaseBatch) super.getApplicationContext().getBean(SubBatchOfByDay.INVENTORY_BY_DATE))
                .execution(args);
            
            // check
            if (!runRe) {
                logger.error("Process of Inventory By Day batch has been failed.");
                throw new BusinessException();
            }
        } catch (Exception e) {
            logger.error("Process of Inventory By Day batch has been failed.");
            throw new BusinessException();
        }
        
        
        // run-down
        try {
            
            // set arguments
            String[] args = new String[IntDef.INT_FOUR];
            args[IntDef.INT_ZERO] = StockBatchId.CPSRDB01;
            args[IntDef.INT_ONE] = String.valueOf(OnlineFlag.ONLINE);
            args[IntDef.INT_TWO] = castParam.getStockDate();
            args[IntDef.INT_THREE] = castParam.getOfficeCode();
            
            // execute
            boolean runRe = ((BaseBatch) super.getApplicationContext().getBean(StockBatchId.CPSRDB01)).execution(args);

            // check
            if (!runRe) {
                logger.error("Process of Rundown batch has been failed.");
                throw new BusinessException();
            }
        } catch (Exception e) {
            logger.error("Process of Rundown batch has been failed.");
            throw new BusinessException();
        }
        
        // stock status
        try {
            
            // set arguments
            String[] args = new String[IntDef.INT_FOUR];
            args[IntDef.INT_ZERO] = StockBatchId.CPSSSB01;
            args[IntDef.INT_ONE] = String.valueOf(OnlineFlag.ONLINE);
            args[IntDef.INT_TWO] = castParam.getStockDate();
            args[IntDef.INT_THREE] = castParam.getOfficeCode();
            
            // execute
            boolean runRe = ((BaseBatch) super.getApplicationContext().getBean(StockBatchId.CPSSSB01)).execution(args);

            // check
            if (!runRe) {
                logger.error("Process of Stock Status batch has been failed.");
                throw new BusinessException();
            }
        } catch (Exception e) {
            logger.error("Process of Stock Status batch has been failed.");
            throw new BusinessException();
        }
        
        // batch process logic end
        logger.info("doOperate", "batch CPSSSB00Batch end......");

        // return OK.
        return true;
    }
    
}
