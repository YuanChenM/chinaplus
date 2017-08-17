/**
 * @screen ReRunShippingPlan Batch main process
 * @author cheng_xingfei
 */
package com.chinaplus.batch.migration.command;

import java.util.ArrayList;
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
import com.chinaplus.batch.migration.bean.MigrationComParam;
import com.chinaplus.batch.migration.bean.ReRunShippingPlanEntity;
import com.chinaplus.batch.migration.bean.TntMgImpIpEx;
import com.chinaplus.batch.migration.service.ReRunShippingPlanService;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.exception.BusinessException;

/**
 * Main Batch process for ReRunShippingPlan batch.
 * 
 * @author cheng_xingfei
 */
@Component("ReRunSPlan")
public class ReRunShippingPlan extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ReRunShippingPlan.class);

    @Autowired
    private ReRunShippingPlanService service;

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
        logger.info("doOperate", "batch ReRunShippingPlan start......");

        // cast
        MigrationComParam castParam = (MigrationComParam) baseParam;

        // set office code and office id
        TntMgImpIpEx mgImpIpInfo = new TntMgImpIpEx();
        mgImpIpInfo.setOfficeCode(castParam.getOfficeCode());
        mgImpIpInfo.setOfficeId(castParam.getOfficeId());

        // for VV
        List<ReRunShippingPlanEntity> expPartsList = this.service.getExpPartsIdList(castParam.getOfficeCode(),
            BusinessPattern.V_V);

        if (expPartsList == null || expPartsList.size() == 0) {
            // batch process logic end
            logger.info("doOperate", "batch ReRunShippingPlan end......");
            return true;
        } else {
            List<Integer> expPartsIdList = new ArrayList<Integer>();
            for (ReRunShippingPlanEntity entity : expPartsList) {
                expPartsIdList.add(entity.getExpPartsId());
            }
            Map<String, Integer> resultMap = new HashMap<String, Integer>();
            resultMap = this.service.doSSMSLogic(expPartsIdList, castParam.getOfficeCode(), BusinessPattern.V_V);

            if (resultMap.get("executeResult") == 1) {
                // batch process logic end
                logger.info("doOperate", "batch ReRunShippingPlan end......");
                return true;
            } else {
                // batch process logic end
                logger.info("doOperate", "batch ReRunShippingPlan end......");
                return false;
            }
        }
    }
}
