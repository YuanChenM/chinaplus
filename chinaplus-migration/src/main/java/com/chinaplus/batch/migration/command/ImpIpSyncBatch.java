/**
 * @screen Run-Down Batch main process
 * @author liu_yinchuan
 */
package com.chinaplus.batch.migration.command;

import java.sql.Timestamp;
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
import com.chinaplus.batch.migration.bean.TntMgImpIpEx;
import com.chinaplus.batch.migration.service.ImpIpSyncService;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;

/**
 * Main Batch process for run-down batch.
 * 
 * @author liu_yinchuan
 */
@Component("ImpIpSync")
public class ImpIpSyncBatch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ImpIpSyncBatch.class);

    /** default limit  */
    private static final Integer DEF_LIMIT = 400000;

    /** logger */
    private List<String> decantParentIPs = new ArrayList<String>();

    /** logger */
    private Map<String, String> devanWhsCodeMap = new HashMap<String, String>();

    /** logger */
    private Map<String, String> decantWhsCodeMap = new HashMap<String, String>();

    @Autowired
    private ImpIpSyncService impIpSyncService;

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
        if (args == null || args.length != BatchConst.INT_THREE) {

            logger.error("The arguments number of batch is incorrect.");
            throw new BatchException();
        }

        // set parameters
        param = new MigrationComParam();
        param.setOfficeCode(args[BatchConst.INT_ZERO]);
        param.setLimit(StringUtil.toInteger(args[BatchConst.INT_ONE]));
        if (param.getLimit() == null || param.getLimit().compareTo(IntDef.INT_ONE) < IntDef.INT_ZERO) {
            param.setLimit(DEF_LIMIT);
        }
        param.setBusinessPattern(StringUtil.toInteger(args[BatchConst.INT_TWO]));

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
        logger.info("doOperate", "batch CPSRDB01Batch start......");

        // cast
        MigrationComParam castParam = (MigrationComParam) baseParam;

        // set office code and office id
        TntMgImpIpEx mgImpIpInfo = new TntMgImpIpEx();
        mgImpIpInfo.setOfficeCode(castParam.getOfficeCode());
        mgImpIpInfo.setOfficeId(castParam.getOfficeId());

        // for VV
        if (castParam.getBusinessPattern() != null && castParam.getBusinessPattern().equals(BusinessPattern.V_V)) {
            mgImpIpInfo.setBusinessPattern(BusinessPattern.V_V);
            this.doUpdateIpInformation(mgImpIpInfo, castParam.getLimit());
        }

        // for aisin
        if (castParam.getBusinessPattern() != null && castParam.getBusinessPattern().equals(BusinessPattern.AISIN)) {
            mgImpIpInfo.setBusinessPattern(BusinessPattern.AISIN);
            this.doUpdateIpInformation(mgImpIpInfo, castParam.getLimit());
        }

        // batch process logic end
        logger.info("doOperate", "batch CPSRDB01Batch end......");

        // return OK.
        return true;
    }
    
    /**
     * update import IP information.
     * 
     * @param mgImpIpInfo mgImpIpInfo
     * @param limit limit
     */
    private void doUpdateIpInformation(TntMgImpIpEx mgImpIpInfo, Integer limit) {

        logger.info("----------------- doUpdateIpInformation Start ------------------");
        // database time
        Timestamp dbTime = baseService.getDBDateTimeByDefaultTimezone();
        // get businessPattern
        Integer businessPattern = mgImpIpInfo.getBusinessPattern();
        // get list
        List<TntMgImpIpEx> ipInfoList = null;
        // check
        if (businessPattern.equals(BusinessPattern.V_V)) {
            
            // prepare whs list
            this.prepareDevanWhsCodeList(mgImpIpInfo);
            this.prepareDecanWhsCodeList(mgImpIpInfo);
            
            // get row count
            int row = impIpSyncService.getPartsStockIPInfoCount(mgImpIpInfo);
            logger.info("----------------- Total Ip count: " + row + " ------------------");
            int start = 0;
            // loop
            while (start <= row) {
                // logger
                logger.info("-------- Process from: " + (start + 1) + " to: " + (start + limit) + " --------");
                
                // set start row
                mgImpIpInfo.setStartRow(start);
                mgImpIpInfo.setLimit(limit);

                // get
                ipInfoList = impIpSyncService.getPartsStockIPInfoList(mgImpIpInfo, businessPattern);
                if (ipInfoList != null && ipInfoList.size() > 0) {
                    logger.info("Start row number: " + ipInfoList.get(0).getRownum());
                }
                // process
                impIpSyncService.doProcessImpIp(ipInfoList, businessPattern, dbTime, decantParentIPs, devanWhsCodeMap, decantWhsCodeMap);
                ipInfoList = null;

                // next
                start += limit;
            }
        } else {
            // do process aisin
            ipInfoList = impIpSyncService.getPartsStockIPInfoList(mgImpIpInfo, businessPattern);
            impIpSyncService.doProcessImpIp(ipInfoList, businessPattern, dbTime, decantParentIPs, devanWhsCodeMap, decantWhsCodeMap);
            ipInfoList = null;
        }
        logger.info("----------------- doUpdateIpInformation End ------------------");
    }
    
    /**
     * prepare Decan WhsCode List.
     * 
     * @param mgImpIpInfo mgImpIpInfo
     */
    private void prepareDevanWhsCodeList(TntMgImpIpEx mgImpIpInfo) {

        // get devanWhsCodeList
        List<TntMgImpIpEx> devanWhsCodeList = impIpSyncService.getModuleDevanWhsCodeList(mgImpIpInfo);

        // check is nill or not.
        if (devanWhsCodeList != null && !devanWhsCodeList.isEmpty()) {
            // loop prepare map
            for (TntMgImpIpEx devanWhsCode : devanWhsCodeList) {
                devanWhsCodeMap.put(impIpSyncService.parepareDevanModuleKey(devanWhsCode), devanWhsCode.getWhsCode());
            }
        }
    }
    
    /**
     * prepare Decan WhsCode List.
     * 
     * @param mgImpIpInfo mgImpIpInfo
     */
    private void prepareDecanWhsCodeList(TntMgImpIpEx mgImpIpInfo) {

        // get devanWhsCodeList
        List<TntMgImpIpEx> decanWhsCodeList = impIpSyncService.getParentDecanWhsCodeList(mgImpIpInfo);

        // check is nill or not.
        if (decanWhsCodeList != null && !decanWhsCodeList.isEmpty()) {
            // loop prepare map
            for (TntMgImpIpEx decanWhsCode : decanWhsCodeList) {
                decantWhsCodeMap.put(decanWhsCode.getPidNo(), decanWhsCode.getWhsCode());
            }
        }
    }

}
