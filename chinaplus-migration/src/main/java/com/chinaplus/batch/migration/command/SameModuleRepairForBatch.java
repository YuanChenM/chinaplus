/**
 * FixSameModuleNo
 * 
 * @screen FixSameModuleNo
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
import com.chinaplus.batch.migration.service.SameModuleRepairForService;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;

/**
 * Fix Same ModuleNo
 * 
 * @author cheng_xingfei
 */
@Component("SameModule")
public class SameModuleRepairForBatch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(SameModuleRepairForBatch.class);

    @Autowired
    private SameModuleRepairForService service;

    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        // prepare parameter
        MigrationComParam param = new MigrationComParam();

        // check parameter size
        if (args == null || args.length != IntDef.INT_ONE) {
            logger.error("Batch parameters is incorrect. Please set as : Office Code");
            throw new BusinessException();
        }

        // set office
        param.setOfficeCode(args[BatchConst.INT_ZERO]);

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
     * doOperate
     *
     * @param param
     * @return
     * @throws Exception
     * @see com.chinaplus.batch.common.base.BaseBatch#doOperate(com.chinaplus.batch.common.bean.BaseBatchParam)
     */
    @Override
    public boolean doOperate(BaseBatchParam param) throws Exception {
        logger.info("doOperate", "batch IPTestTimeBatch start......");
        
        // cast
        MigrationComParam castparam = (MigrationComParam) param;

        // do test
        service.doFixSameModuleNo(castparam);

        logger.info("doOperate", "batch IPTestTimeBatch end......");
        return true;
    }

}
