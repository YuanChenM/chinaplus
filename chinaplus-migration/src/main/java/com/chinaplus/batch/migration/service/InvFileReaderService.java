/**
 * CPIIFB16Service.java
 * 
 * @screen CPIIFB16
 * @author yang_jia1
 */
package com.chinaplus.batch.migration.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.migration.bean.TntMgInvoiceEx;
import com.chinaplus.common.consts.CodeConst.HandleFlag;
import com.chinaplus.common.consts.CodeConst.TransportMode;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * 
 * CPIIFB16Service.
 * @author yang_jia1
 */
public class InvFileReaderService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(InvFileReaderService.class);
    
    /**
     * doSyncFilesToIfTable
     * 
     * @param invList invoice information list
     * @param officeId officeId
     */
    public void doSyncFilesToIfTable(List<TntMgInvoiceEx> invList, Integer officeId) {

        logger.debug("---------------batch doSyncFilesToIfTable start-------------");
        // get current time stamp
        Timestamp nowTime = super.getDBDateTimeByDefaultTimezone();
        // get max values
        int mgInvoiceId = this.getMaxMgInvoiceId();
        // loop
        for (TntMgInvoiceEx invInfo : invList) {

            // set into entity
            invInfo.setMgInvoiceId(mgInvoiceId++);
            invInfo.setOfficeId(officeId);
            
            // reset qty
            if (invInfo.getTransportModeCode() != null
                    && (invInfo.getTransportModeCode().equalsIgnoreCase("A") || invInfo.getTransportModeCode()
                        .equalsIgnoreCase("AIR"))) {
                invInfo.setTransportMode(TransportMode.AIR);
            } else {
                invInfo.setTransportMode(TransportMode.SEA);
            }
            
            // set values
            invInfo.setHandleFlag(HandleFlag.UNPROCESS);
            invInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
            invInfo.setCreatedDate(nowTime);
            invInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
            invInfo.setUpdatedDate(nowTime);
            invInfo.setVersion(NumberConst.IntDef.INT_ONE);

            // save
            this.baseMapper.insert(this.getSqlId("addIntoMigrationInvoice"), invInfo);
        }
        logger.debug("---------------batch doSyncFilesToIfTable end-------------");
    }
    
    /**
     * Get max RundowMaster id.
     * 
     * @return RundowMaster id.
     */
    public int getMaxMgInvoiceId() {

        // get maxRundowId
        Integer maxMgIpId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxMgInvoiceId"));

        // get form databse
        return maxMgIpId == null ? IntDef.INT_ONE : maxMgIpId.intValue();
    }
}
