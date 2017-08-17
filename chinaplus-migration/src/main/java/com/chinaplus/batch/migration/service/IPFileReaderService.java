/**
 * CPIIFB16Service.java
 * 
 * @screen CPIIFB16
 * @author yang_jia1
 */
package com.chinaplus.batch.migration.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.migration.bean.TntMgImpIpEx;
import com.chinaplus.common.consts.CodeConst.HandleFlag;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.CoreConst.Direction;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * CPIIFB16Service.
 * @author yang_jia1
 */
public class IPFileReaderService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(IPFileReaderService.class);

    /** logger */
    private Map<String, String> sourceIPMaps = new HashMap<String, String>();
    
    /**
     * doSyncFilesToIfTable
     * 
     * @param ipList ip information list
     * @param ifDateTime ifDateTime
     */
    public void doSyncFilesToIfTable(List<TntMgImpIpEx> ipList, Timestamp ifDateTime) {

        logger.debug("---------------batch doSyncFilesToIfTable start-------------");
        // get current time stamp
        Timestamp nowTime = super.getDBDateTimeByDefaultTimezone();
        // get max values
        int mgIpId = getMaxMgIpId();
        int count = 0;
        // loop
        for (TntMgImpIpEx ipInfo : ipList) {
            
            count++;

            // set into entity
            ipInfo.setMgIpId(mgIpId++);
            
            // reset invoice
            ipInfo.setInvoiceNo(this.repareInvoice(ipInfo.getInvoiceNo()));
            
            // reset qty
            ipInfo.setOriginalQty(this.trimZeroForQty(ipInfo.getOriginalQty()));
            ipInfo.setQty(this.trimZeroForQty(ipInfo.getQty()));
            this.getSourceIpNo(ipInfo);
            
            // set values
            ipInfo.setIfDateTime(ifDateTime);
            ipInfo.setHandleFlag(HandleFlag.UNPROCESS);
            ipInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
            ipInfo.setCreatedDate(nowTime);
            ipInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
            ipInfo.setUpdatedDate(nowTime);
            ipInfo.setVersion(NumberConst.IntDef.INT_ONE);
            if(count % IntDef.INT_THOUSAND == 0) {
                logger.info(String.valueOf(count));
            }

            // save
            super.baseMapper.insert(this.getSqlId("addIntoMigrationIP"), ipInfo);
        }
        logger.info(String.valueOf(count));
        logger.debug("---------------batch doSyncFilesToIfTable end-------------");
    }

    /**
     * reprepare inovice no.
     * 
     * @param invoiceNo invoiceNo
     * @return invoiceNo
     */
    private String repareInvoice(String invoiceNo) {
        // if not empty
        if (StringUtil.isEmpty(invoiceNo)) {
            return invoiceNo;
        }
        // replaceAll
        return invoiceNo.replaceAll(StringConst.VERTICAL_REX, StringConst.COMMA);
    }

    /**
     * get source ip No
     * 
     * @param ipInfo ipInfo
     */
    private void getSourceIpNo(TntMgImpIpEx ipInfo) {

        // check
        if (!StringUtil.isEmpty(ipInfo.getSourceIpNo())) {
            sourceIPMaps.put(ipInfo.getPidNo(), ipInfo.getSourceIpNo());
            if (ipInfo.getParentPidNo() != null) {
                sourceIPMaps.put(ipInfo.getParentPidNo(), ipInfo.getSourceIpNo());
            }
            return;
        }
        if (StringUtil.isEmpty(ipInfo.getParentPidNo())) {
            return;
        }

        // get from database
        if (sourceIPMaps.containsKey(ipInfo.getParentPidNo())) {
            ipInfo.setSourceIpNo(sourceIPMaps.get(ipInfo.getParentPidNo()));
        } else {
            String sourceIpNo = this.getSourceIpNoByParentPidNo(ipInfo);
            ipInfo.setSourceIpNo(sourceIpNo);
            sourceIPMaps.put(ipInfo.getParentPidNo(), sourceIpNo);
        }
    }
    
    /**
     * trim zero for Qty.
     * 
     * @param val val
     * @return trimed val
     */
    private String trimZeroForQty(String val) {

        // empty
        if (StringUtil.isEmpty(val)) {
            return val;
        }

        // check has point
        if (val.indexOf(StringConst.DOT) > IntDef.INT_ZERO) {
            return StringUtil.trim(StringUtil.trim(val, Direction.Right, '0'), Direction.Right, '.');
        } else {
            return val;
        }
    }
    
    /**
     * Get max RundowMaster id.
     * 
     * @return RundowMaster id.
     */
    public int getMaxMgIpId() {

        // get maxRundowId
        Integer maxMgIpId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxMgIpId"));

        // get form databse
        return maxMgIpId == null ? IntDef.INT_ONE : maxMgIpId.intValue();
    }
    
    /**
     * getSourceIpNo.
     * 
     * @param ipInfo ipInfo
     * @return SourceIpNo
     */
    public String getSourceIpNoByParentPidNo(TntMgImpIpEx ipInfo) {

        // get maxRundowId
        List<TntMgImpIpEx> pids = baseMapper.getSqlSession().selectList(this.getSqlId("getSourceIpNo"), ipInfo);
        if (pids != null && !pids.isEmpty()) {
            if (pids.get(0) != null) {
                return pids.get(0).getSourceIpNo();
            }
        }
        // get form databse
        return null;
    }
}
