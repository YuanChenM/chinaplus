/**
 * CPIIFB16Service.java
 * 
 * @screen CPIIFB16
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.interfaces.bean.ttlogic.InnerPackage;
import com.chinaplus.common.consts.CodeConst.ActionType;
import com.chinaplus.common.consts.CodeConst.OnHoldFlag;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * CPIIFB16Service.
 * @author yang_jia1
 */
public class CPIIFB16Service extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB16Service.class);

    /** logger */
    private Map<String, String> sourceIPMaps = new HashMap<String, String>();
    
    /**
     * doSyncFilesToIfTable
     * 
     * @param ipList ip information list
     */
    public void doSyncFilesToIfTable(List<InnerPackage> ipList) {

        logger.debug("---------------batch doSyncFilesToIfTable start-------------");
        // define
        List<InnerPackage> ajdustList = new ArrayList<InnerPackage>();
        // get current time stamp
        Timestamp nowTime = super.getDBDateTimeByDefaultTimezone();
        // get start index
        int ifIpId = this.getMaxIfIpId();
        int count = 0;
        // loop
        logger.info("ipList size: " + ipList.size());
        for (InnerPackage ipInfo : ipList) {
            
            count ++;
            
            // set id 
            ipInfo.setIfIpId(ifIpId++);
            
            // set values
            ipInfo.setValidFlag(BatchConst.INT_ZERO);
            ipInfo.setHandleFlag(BatchConst.INT_ZERO);
            ipInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
            ipInfo.setCreatedDate(nowTime);
            ipInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
            ipInfo.setUpdatedDate(nowTime);
            ipInfo.setVersion(NumberConst.IntDef.INT_ONE);
            
            // set decant
            this.getSourceIpNo(ipInfo);

            // save
            this.baseMapper.insert(this.getSqlId("addTTLogicIPIf"), ipInfo);
            
            // keep stock adjust
            if (ipInfo.getActionType().equals(ActionType.STOCK_ADJUST)) {
                ajdustList.add(ipInfo);
            }
            
            // log
            if (count % IntDef.INT_THOUSAND == 0) {
                logger.info("Already process: " + count);
            }
        }
        
        // process adjust
        this.prepareOnHoldInfoForAdjust(ajdustList, ifIpId);
        logger.debug("---------------batch doSyncFilesToIfTable end-------------");
    }

    /**
     * get source IP No.
     * 
     * @param ipInfo ipInfo
     */
    private void getSourceIpNo(InnerPackage ipInfo) {

        // check from west or not
        if (!"WEST".equalsIgnoreCase(ipInfo.getDataSource())
                || ipInfo.getActionType().intValue() < ActionType.IMP_INBOUND) {
            return;
        }

        if (StringUtil.isEmpty(ipInfo.getSourceIPNo())) {
            // get from database
            if (sourceIPMaps.containsKey(ipInfo.getTtlogicPidNo())) {
                ipInfo.setSourceIPNo(sourceIPMaps.get(ipInfo.getTtlogicPidNo()));
            } else if (sourceIPMaps.containsKey(ipInfo.getFromIpNo())) {
                ipInfo.setSourceIPNo(sourceIPMaps.get(ipInfo.getFromIpNo()));
            } else {
                String sourceIpNo = this.getSourceIpNoByParentPidNo(ipInfo);
                ipInfo.setSourceIPNo(sourceIpNo);
            }
        }

        // set into map
        sourceIPMaps.put(ipInfo.getTtlogicPidNo(), ipInfo.getSourceIPNo());
        // if has from ip no
        if (!StringUtil.isEmpty(ipInfo.getFromIpNo())) {
            sourceIPMaps.put(ipInfo.getFromIpNo(), ipInfo.getSourceIPNo());
        }
    }

    /**
     * prepare OnHold Information For Adjust.
     * 
     * @param ajdustList ajdustList
     * @param ifIpId ifIpId
     */
    private void prepareOnHoldInfoForAdjust(List<InnerPackage> ajdustList, Integer ifIpId) {

        // check latest process is
        if (ajdustList == null || ajdustList.isEmpty()) {
            return;
        }

        // do prepare
        Integer maxIpId = ifIpId;
        // loop
        for (InnerPackage ipInfo : ajdustList) {

            // check is NG/On-hold or not
            InnerPackage holdInfo = baseMapper.findOne(this.getSqlId("getLastPidInformation"), ipInfo);

            // if is NG/On-hold then
            if (holdInfo != null
                    && holdInfo.getActionType() != null
                    && (holdInfo.getActionType().equals(ActionType.NG) || holdInfo.getActionType().equals(
                        ActionType.ECI_ONHOLD))) {
                // make a new NG info
                maxIpId = this.saveOnHoldInfoForAdjust(ipInfo, holdInfo, maxIpId);
            }

        }
    }

    /**
     * Make on-hold information for stock ajust.
     * 
     * @param ipInfo ipInfo
     * @param holdInfo holdInfo
     * @param maxIpId maxIpId
     * @return made information
     */
    private Integer saveOnHoldInfoForAdjust(InnerPackage ipInfo, InnerPackage holdInfo, Integer maxIpId) {

        Integer loopId = maxIpId;
        String onholdFlag = holdInfo.getOnholdFlag();
        Integer actionType = holdInfo.getActionType();

        // set id
        holdInfo.setIfIpId(loopId++);

        // set values
        holdInfo.setValidFlag(BatchConst.INT_ZERO);
        holdInfo.setHandleFlag(BatchConst.INT_ZERO);
        holdInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
        holdInfo.setCreatedDate(ipInfo.getCreatedDate());
        holdInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
        holdInfo.setUpdatedDate(ipInfo.getUpdatedDate());
        holdInfo.setVersion(NumberConst.IntDef.INT_ONE);

        // reset release on-hold information
        holdInfo.setActionType(ActionType.RELEASE_ONHOLD);
        holdInfo.setOnholdFlag(StringUtil.toString(OnHoldFlag.NORMAL));
        holdInfo.setProcessDate(new Timestamp(DateTimeUtil.add(ipInfo.getProcessDate(), IntDef.INT_ZERO,
            IntDef.INT_ZERO, IntDef.INT_ZERO, IntDef.INT_ZERO, IntDef.INT_ZERO, IntDef.INT_N_ONE).getTime()));
        holdInfo.setOnholdDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_IP_DATE, holdInfo.getProcessDate()));
        holdInfo.setIfDateTime(ipInfo.getIfDateTime());

        // save
        this.baseMapper.insert(this.getSqlId("addTTLogicIPIf"), holdInfo);

        // set id
        holdInfo.setIfIpId(loopId++);
        
        // reset on-hold information
        holdInfo.setActionType(actionType);
        holdInfo.setOnholdFlag(onholdFlag);
        holdInfo.setProcessDate(new Timestamp(DateTimeUtil.add(ipInfo.getProcessDate(), IntDef.INT_ZERO,
            IntDef.INT_ZERO, IntDef.INT_ZERO, IntDef.INT_ZERO, IntDef.INT_ZERO, IntDef.INT_ONE).getTime()));
        holdInfo.setOnholdDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_IP_DATE, holdInfo.getProcessDate()));
        holdInfo.setQty(ipInfo.getQty());

        // save
        this.baseMapper.insert(this.getSqlId("addTTLogicIPIf"), holdInfo);

        // return
        return loopId;
    }

    /**
     * Get max NotInRundown id.
     * 
     * @return NotInRundown id
     */
    private int getMaxIfIpId() {

        // get maxRundowId
        Integer maxIfIpId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxIfIpId"));

        // get form databse
        return maxIfIpId == null ? IntDef.INT_ONE : maxIfIpId.intValue();
    } 
    
    /**
     * getSourceIpNo.
     * 
     * @param ipInfo ipInfo
     * @return SourceIpNo
     */
    private String getSourceIpNoByParentPidNo(InnerPackage ipInfo) {

        // get pid no.
        String pidNo = StringUtil.isEmpty(ipInfo.getFromIpNo()) ? ipInfo.getTtlogicPidNo() : ipInfo.getFromIpNo();
        if (StringUtil.isEmpty(pidNo)) {
            return null;
        }

        // parameter
        InnerPackage ipParam = new InnerPackage();
        ipParam.setTtlogicPidNo(pidNo);

        // get maxRundowId
        List<InnerPackage> pids = baseMapper.getSqlSession().selectList(this.getSqlId("getSourceIpNo"), ipParam);
        if (pids != null && !pids.isEmpty()) {
            if (pids.get(0) != null) {
                return pids.get(0).getSourceIPNo();
            }
        }
        // get form databse
        return null;
    }
}
