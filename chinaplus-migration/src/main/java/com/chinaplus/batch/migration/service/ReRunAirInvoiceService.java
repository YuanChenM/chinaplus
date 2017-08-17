/**
 * ReRunShippingPlanService.java
 * 
 * @screen ReRunShippingPlan
 * @author cheng_xingfei
 */
package com.chinaplus.batch.migration.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.common.bean.IfIpEntity;
import com.chinaplus.common.consts.CodeConst.ActionType;
import com.chinaplus.common.consts.CodeConst.HandleFlag;
import com.chinaplus.common.consts.CodeConst.ValidFlag;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * ReRunShippingPlanService.
 * 
 * @author cheng_xingfei
 */
@Service("ReRunAirInvoiceService")
public class ReRunAirInvoiceService extends BaseService {

    /**
     * Prepare in-bound/out-bound information for air invoice.
     * 
     * @param officeCode officeCode
     * @param invoiceNo invoiceNo
     * @param time time
     */
    public void doPrepareAirInvoiceInfo(String officeCode, String invoiceNo, Timestamp time) {

        // get IP List from database
        List<IfIpEntity> ipInfoList = this.findInvoiceIPInfoList(officeCode, invoiceNo);

        // list is empty?
        if (ipInfoList != null && !ipInfoList.isEmpty()) {

            // get getMaxIfIpId
            Integer maxIfIpId = this.getMaxIfIpId();

            // loop
            for (IfIpEntity ipInfo : ipInfoList) {

                // set time
                ipInfo.setCreatedDate(time);

                // prepare inbound information
                maxIfIpId = this.doSaveIntoDataBase(this.makeInboundInfo(ipInfo), maxIfIpId);

                // prepare outbound information
                maxIfIpId = this.doSaveIntoDataBase(this.makeOutboundInfo(ipInfo), maxIfIpId);
            }
        }
    }

    /**
     * get inner packing list for invoice information.
     * 
     * @param officeCode officeCode
     * @param invoiceNo invoiceNo
     * @return ifipInfo list
     */
    private List<IfIpEntity> findInvoiceIPInfoList(String officeCode, String invoiceNo) {

        // set parameter
        IfIpEntity param = new IfIpEntity();
        param.setOfficeCode(officeCode);
        param.setInvoiceNo(invoiceNo);

        // find IP information
        return super.baseMapper.select(this.getSqlId("findInvoiceIPInfoList"), param);
    }

    /**
     * make inbound information.
     * 
     * @param ipInfo ipInfo
     * @return ifipInfo
     */
    private IfIpEntity makeInboundInfo(IfIpEntity ipInfo) {

        // new object
        IfIpEntity inboundInfo = new IfIpEntity();

        // reset
        inboundInfo.setActionType(StringUtil.toString(ActionType.IMP_INBOUND));
        inboundInfo.setOfficeCode(ipInfo.getOfficeCode());
        inboundInfo.setSourceIpNo(ipInfo.getSourceIpNo());
        inboundInfo.setWhsCode(ipInfo.getWhsCode());
        inboundInfo.setWhsCustomerCode(ipInfo.getWhsCustomerCode());
        inboundInfo.setTtcPartsNo(ipInfo.getTtcPartsNo());
        inboundInfo.setQty(StringUtil.toDecimalString(ipInfo.getOriginalQty().stripTrailingZeros()));
        inboundInfo.setInbInvoiceNo(ipInfo.getInbInvoiceNo());
        inboundInfo.setInbModuleNo(ipInfo.getInbModuleNo());

        // get process date
        Date processDate = null;
        if (ipInfo.getInvDevanDate() != null) {
            processDate = ipInfo.getInvDevanDate();
        } else if (ipInfo.getCcDate() != null) {
            processDate = ipInfo.getCcDate();
        } else if (ipInfo.getExpObActualDate() != null) {
            processDate = ipInfo.getExpObActualDate();
        }

        // set
        inboundInfo.setPidNo(inboundInfo.getSourceIpNo().concat(
            DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, processDate)));
        inboundInfo.setInbDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_IP_DATE, processDate));

        // set process date
        inboundInfo.setProcessDate(new Timestamp(processDate.getTime()));
        inboundInfo.setIfDateTime(ipInfo.getCreatedDate());

        // set other information
        inboundInfo.setValidFlag(ValidFlag.OTHER);
        inboundInfo.setHandleFlag(HandleFlag.UNPROCESS);
        inboundInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
        inboundInfo.setCreatedDate(ipInfo.getCreatedDate());
        inboundInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
        inboundInfo.setUpdatedDate(ipInfo.getCreatedDate());
        inboundInfo.setVersion(IntDef.INT_ZERO);

        // reset
        ipInfo.setProcessDate(inboundInfo.getProcessDate());
        ipInfo.setPidNo(inboundInfo.getPidNo());
        ipInfo.setOutboundDatetime(inboundInfo.getInbDate());

        // return
        return inboundInfo;
    }

    /**
     * make outbound information.
     * 
     * @param ipInfo ipInfo
     * @return ifipInfo
     */
    private IfIpEntity makeOutboundInfo(IfIpEntity ipInfo) {

        // new object
        IfIpEntity outboundInfo = new IfIpEntity();

        // reset
        outboundInfo.setActionType(StringUtil.toString(ActionType.IMP_OUTBOUND));
        outboundInfo.setOfficeCode(ipInfo.getOfficeCode());
        outboundInfo.setSourceIpNo(ipInfo.getSourceIpNo());
        outboundInfo.setPidNo(ipInfo.getPidNo());
        outboundInfo.setWhsCode(ipInfo.getWhsCode());
        outboundInfo.setWhsCustomerCode(ipInfo.getWhsCustomerCode());
        outboundInfo.setTtcPartsNo(ipInfo.getTtcPartsNo());
        outboundInfo.setQty(StringUtil.toDecimalString(ipInfo.getOriginalQty().stripTrailingZeros()));
        outboundInfo.setOutboundDatetime(ipInfo.getOutboundDatetime());
        outboundInfo.setDispatchedDatetime(ipInfo.getOutboundDatetime());

        // set process date
        outboundInfo.setProcessDate(ipInfo.getProcessDate());
        outboundInfo.setIfDateTime(ipInfo.getCreatedDate());

        // set other information
        outboundInfo.setValidFlag(ValidFlag.OTHER);
        outboundInfo.setHandleFlag(HandleFlag.UNPROCESS);
        outboundInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
        outboundInfo.setCreatedDate(ipInfo.getCreatedDate());
        outboundInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
        outboundInfo.setUpdatedDate(ipInfo.getCreatedDate());
        outboundInfo.setVersion(IntDef.INT_ZERO);

        return outboundInfo;
    }

    /**
     * save if ip information into database.
     * 
     * @param ifIpInfo ifIpInfo
     * @param maxIfIpId maxIfIpId
     * @return update count
     */
    private int doSaveIntoDataBase(IfIpEntity ifIpInfo, Integer maxIfIpId) {

        // set ip id
        ifIpInfo.setIfIpId(maxIfIpId);

        // save
        super.baseMapper.update(this.getSqlId("saveIfIpInformation"), ifIpInfo);

        // return
        return maxIfIpId.intValue() + IntDef.INT_ONE;
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
