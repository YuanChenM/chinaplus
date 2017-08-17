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
import com.chinaplus.batch.common.consts.BatchConst.ValidFlag;
import com.chinaplus.batch.migration.bean.TntMgImpIpEx;
import com.chinaplus.common.consts.CodeConst.ActionType;
import com.chinaplus.common.consts.CodeConst.HandleFlag;
import com.chinaplus.common.entity.TntIfImpIp;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * CPIIFB16Service.
 * 
 * @author yang_jia1
 */
public class ImpIpPatchService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ImpIpPatchService.class);

    /**
     * Get max RundowMaster id.
     * 
     * @param officeCode officeCode
     * 
     */
    public void doImpIpPatchSync(String officeCode) {

        logger.info("Start of do imp Ip patch");

        // get all need do patched Ip information
        List<TntMgImpIpEx> mgIpList = this.getPartsStockIPInfoList(officeCode);

        // get process size
        logger.info("Size of Mg Ip List: " + mgIpList.size());
        
        // get max id
        int ifIpId = this.getMaxIfIpId();
        Timestamp ifDateTime = super.baseMapper.getCurrentTime();

        // for each
        int count = 0;
        for (TntMgImpIpEx mgImpIp : mgIpList) {

            // count up
            count++;

            // update
            this.doModifyTTLogicIPIf(mgImpIp);

            // create a new parts
            TntIfImpIp whsTransferIp = this.makeNewWhsTransferIfip(mgImpIp);

            // inert new one to do patch
            this.doAddTTLogicIPIf(whsTransferIp, ifDateTime, ifIpId++);

            // print for 1000 line
            if (count % IntDef.INT_THOUSAND == 0) {
                // logger
                logger.info("Already process: " + count);
            }
        }

        // logger
        logger.info("Already process: " + count);

        // end logger
        logger.info("End of do imp Ip patch");
    }

    /**
     * make new if imp ip.
     * 
     * @param mgIpInfo mgIpInfo
     * @return new if imp ip information
     */
    private TntIfImpIp makeNewWhsTransferIfip(TntMgImpIpEx mgIpInfo) {

        // prepare If IP information
        TntIfImpIp inbIp = new TntIfImpIp();

        // DATA SOURCE
        inbIp.setDataSource("WEST");
        // detail informations
        inbIp.setSeqNo(StringUtil.toString(IntDef.INT_ONE));
        // OFFICE_CODE
        inbIp.setOfficeCode(mgIpInfo.getOfficeCode());
        // source IP No
        inbIp.setSourceIpNo(mgIpInfo.getSourceIpNo());
        // PID_NO
        inbIp.setPidNo(mgIpInfo.getPidNo());
        // WHS_CODE
        inbIp.setWhsCode(mgIpInfo.getWhsCode());
        // TTC_PARTS_NO
        inbIp.setTtcPartsNo(mgIpInfo.getTtcPartsNo());
        // STATUS
        inbIp.setStatus(StringUtil.toString(mgIpInfo.getStatus()));
        // IF_DATE_TIME
        inbIp.setIfDateTime(mgIpInfo.getIfDateTime());
        // VALID_FLAG
        inbIp.setValidFlag(ValidFlag.OTHER);
        // HANDLE_FLAG
        inbIp.setHandleFlag(HandleFlag.UNPROCESS);

        // ACTION_TYPE
        inbIp.setActionType(String.valueOf(ActionType.WHS_TRANSFER));
        // set as inbound date
        inbIp.setWhsTransferDate(mgIpInfo.getInboundDate());
        // set
        inbIp.setFromWhsCode(mgIpInfo.getOriginalWhsCode());
        // set process date
        inbIp.setProcessDate(DateTimeUtil.parseDateTime(inbIp.getWhsTransferDate(), DateTimeUtil.FORMAT_IP_DATE));
        // set customer code
        if (mgIpInfo.getOriginalCustCode() != null) {
            inbIp.setWhsCustomerCode(mgIpInfo.getOriginalCustCode());
        } else {
            inbIp.setWhsCustomerCode(mgIpInfo.getCustomerCode());
        }
        // set QTY
        if (mgIpInfo.getOriginalQty() != null) {
            inbIp.setQty(mgIpInfo.getOriginalQty());
        } else {
            inbIp.setQty(mgIpInfo.getQty());
        }

        return inbIp;
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param officeCode officeCode
     * @return list
     */
    private List<TntMgImpIpEx> getPartsStockIPInfoList(String officeCode) {

        // parameter
        TntMgImpIpEx mgImpIpInfo = new TntMgImpIpEx();
        mgImpIpInfo.setOfficeCode(officeCode);

        return baseMapper.selectList(this.getSqlId("getPartsStockIPInfoListForPatch"), mgImpIpInfo);
    }

    /**
     * Save into if imp ip information.
     * 
     * @param mgImpIp mgImpIp
     * @return index
     */
    private int doModifyTTLogicIPIf(TntMgImpIpEx mgImpIp) {

        // return
        return this.baseMapper.update(this.getSqlId("modifyTTLogicIPIf"), mgImpIp);
    }

    /**
     * Save into if imp ip information.
     * 
     * @param mgImpIp mgImpIp
     * @param dbTime dbTime
     * @param ifIpId ifIpId
     * @return index
     */
    private int doAddTTLogicIPIf(TntIfImpIp mgImpIp, Timestamp dbTime, Integer ifIpId) {
        
        // set date
        mgImpIp.setIfIpId(ifIpId);
        mgImpIp.setCreatedBy(BatchConst.BATCH_USER_ID);
        mgImpIp.setCreatedDate(dbTime);
        mgImpIp.setUpdatedBy(BatchConst.BATCH_USER_ID);
        mgImpIp.setUpdatedDate(dbTime);
        mgImpIp.setVersion(NumberConst.IntDef.INT_ONE);

        // return
        return this.baseMapper.insert(this.getSqlId("addTTLogicIPIf"), mgImpIp);
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
}
