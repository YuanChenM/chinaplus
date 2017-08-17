/**
 * CPIIFB01Service.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.chinaplus.batch.interfaces.bean.BatchLog;
import com.chinaplus.batch.interfaces.common.BatchUtils;
import com.chinaplus.core.base.BaseService;

/**
 * 
 * CPIIFB01Service.
 * The SSMS logic batch start base service.
 * @author yang_jia1
 */
public class CPIIFB01Service extends BaseService {

    /**
     * checkBatchLogStatus
     * 
     * @param param BatchLog
     * @return Map<String, Object>
     */
    public BatchLog batchLogStatusIsNormal(BatchLog param) {
        List<BatchLog> batchLogStatus = null;
        batchLogStatus = this.baseMapper.select(this.getSqlId("queryBatchLogStatus"), param);
        if (batchLogStatus != null && !batchLogStatus.isEmpty() && batchLogStatus.get(0) != null) {
            return batchLogStatus.get(0);
        } else {
            return null;
        }
    }
    
    /**
     * queryBatchLogTimes
     * 
     * @param param BatchLog
     * @return Map<String, Object>
     */
    public List<Timestamp> querySSMSBatchLogTimes(BatchLog param) {
        List<BatchLog> batchLogs = new ArrayList<BatchLog>();
        List<Timestamp> ifDateTimes = new ArrayList<Timestamp>();
        List<BatchLog> batchLogs1 = this.baseMapper.select(this.getSqlId("queryIfDateTimes1"), param);
        List<BatchLog> batchLogs2 = this.baseMapper.select(this.getSqlId("queryIfDateTimes2"), param);
        List<BatchLog> batchLogs3 = this.baseMapper.select(this.getSqlId("queryIfDateTimes3"), param);
        List<BatchLog> batchLogs4 = this.baseMapper.select(this.getSqlId("queryIfDateTimes4"), param);
        List<BatchLog> batchLogs5 = this.baseMapper.select(this.getSqlId("queryIfDateTimes5"), param);
        
        batchLogs.addAll(batchLogs1);
        batchLogs.addAll(batchLogs2);
        batchLogs.addAll(batchLogs3);
        batchLogs.addAll(batchLogs4);
        batchLogs.addAll(batchLogs5);
        
        for (BatchLog batchLog : batchLogs) {
            ifDateTimes.add(batchLog.getIfDateTime());
        }
        
        return BatchUtils.distinctList(ifDateTimes);
    }
    
    /**
     * queryBatchLogTimes
     * 
     * @param param BatchLog
     * @return Map<String, Object>
     */
    public List<Timestamp> queryCustomerLogTimes(BatchLog param) {
        List<Timestamp> ifDateTimes = new ArrayList<Timestamp>();
        List<BatchLog> batchLogs = this.baseMapper.select(this.getSqlId("queryIfDateTimes6"), param);
        
        for (BatchLog batchLog : batchLogs) {
            ifDateTimes.add(batchLog.getIfDateTime());
        }
        
        return BatchUtils.distinctList(ifDateTimes);
    }
    
    /**
     * queryBatchLogTimes
     * 
     * @param param BatchLog
     * @return Map<String, Object>
     */
    public List<Timestamp> queryPartsLogTimes(BatchLog param) {
        List<Timestamp> ifDateTimes = new ArrayList<Timestamp>();
        List<BatchLog> batchLogs = this.baseMapper.select(this.getSqlId("queryIfDateTimes7"), param);
        
        for (BatchLog batchLog : batchLogs) {
            ifDateTimes.add(batchLog.getIfDateTime());
        }
        
        return BatchUtils.distinctList(ifDateTimes);
    }
}
