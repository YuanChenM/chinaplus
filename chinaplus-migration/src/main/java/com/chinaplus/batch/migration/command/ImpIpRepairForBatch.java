/**
 * This batch is for repair data for IP VV data
 * 
 * @screen ImpIpRepairBatch
 * @author Yinchuan LIU
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
import com.chinaplus.batch.migration.service.ImpIpRepairForAisinService;
import com.chinaplus.batch.migration.service.ImpIpRepairForVvService;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;

/**
 * This batch is for repair data for IP VV data
 * 
 * @author Yinchuan LIU
 */
@Component("IpRepair")
public class ImpIpRepairForBatch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ImpIpRepairForBatch.class);

    /** Imp IP(VV) repair service. */
    @Autowired
    private ImpIpRepairForVvService vvIpRepairService;

    /** Imp IP(AISIN) repair service. */
    @Autowired
    private ImpIpRepairForAisinService aisinIpRepairService;

    /**
     * Check parameters.
     *
     * @param args args
     * @return BaseBatchParam BaseBatchParam
     * @see com.chinaplus.batch.common.base.BaseBatch#createBatchParam(java.lang.String[])
     */
    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        // prepare parameter
        MigrationComParam param = new MigrationComParam();

        // check parameter size
        if (args.length != IntDef.INT_TWO) {
            logger.error("Batch parameters is incorrect. Please set as : Office Code, Business Pattern");
            throw new BusinessException();
        }

        // set office
        param.setOfficeCode(args[BatchConst.INT_ZERO]);

        // set office
        param.setBusinessPattern(StringUtil.toInteger(args[BatchConst.INT_ONE]));

        // check
        TnmOffice office = baseService.getOfficeInfo(param.getOfficeCode());
        if (office == null) {
            logger.error("Office code does not an effective office.");
            throw new BusinessException();
        }

        // check pattern
        if (param.getBusinessPattern() == null
                || (!param.getBusinessPattern().equals(BusinessPattern.V_V) && !param.getBusinessPattern().equals(
                    BusinessPattern.AISIN))) {
            logger.error("Business Pattern is invalid value.");
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

        // start log
        logger.info("batch ImpIpRepairForBatch start......");

        // cast
        MigrationComParam castparam = (MigrationComParam) param;

        // check business pattern
        if (param.getBusinessPattern().equals(BusinessPattern.V_V)) {

            // for VV Parts
            vvIpRepairService.doImpIpRepair(castparam.getOfficeId());
        } else {

            // for AISIN Parts
            aisinIpRepairService.doImpIpRepair(castparam.getOfficeId());
        }

        // end log
        logger.info("batch ImpIpRepairForBatch end......");

        return true;
    }

}
