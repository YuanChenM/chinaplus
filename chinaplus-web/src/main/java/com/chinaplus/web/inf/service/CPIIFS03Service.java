/**
 * @screen CPIIFS03
 * @author zhang_chi
 */
package com.chinaplus.web.inf.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.consts.CodeConst.InvoiceMatchStatus;
import com.chinaplus.common.entity.TntMatchInvoice;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * CPIIFS03Service.
 */
@Service
public class CPIIFS03Service extends BaseService {

    /**
     * get InvoiceNo
     * 
     * @param param param
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getInvoiceNo(BaseParam param) {
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getInvoiceNo"), param);
        return comboDataList;
    }
    
    /**
     * get RegionCode
     * 
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getRegionCode() {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getRegionCode"), param);
        return comboDataList;
    }

    /**
     * Get UploadedBy
     * 
     * @param param param
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getUploadedBy(BaseParam param) {
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getUploadedBy"), param);
        return comboDataList;
    }
    
    /**
     * save Data
     * 
     * @param param param
     */
    public void doSaveData(BaseParam param) {
        
        // get data
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();
        
        // prepare parameter
        TntMatchInvoice invParam = new TntMatchInvoice();
        invParam.setOfficeId(StringUtil.toInteger(param.getSwapData().get("officeId")));
        invParam.setWhsInvoiceNo(StringUtil.toString(param.getSwapData().get("whsInvoiceNo")));
        
        // check is exit or not
        TntMatchInvoice matchedInfo = super.baseDao.findOne(invParam);
        // check
        if (matchedInfo == null) {
            // new
            matchedInfo = new TntMatchInvoice();
            
            // set 
            matchedInfo.setOfficeId(invParam.getOfficeId());
            matchedInfo.setInvoiceNo(StringUtil.toString(param.getSwapData().get("invoiceNo")));
            matchedInfo.setWhsInvoiceNo(invParam.getWhsInvoiceNo());
            matchedInfo.setStatus(InvoiceMatchStatus.MISMATCH);
            matchedInfo.setDataDate(DateTimeUtil.parseDate(StringUtil.toString(param.getSwapData().get("dataDate"))));
            matchedInfo.setCreatedBy(param.getLoginUserId());
            matchedInfo.setCreatedDate(dbTime);
            matchedInfo.setUpdatedBy(param.getLoginUserId());
            matchedInfo.setUpdatedDate(dbTime);
            matchedInfo.setVersion(IntDef.INT_ONE);
            
            // insert
            super.baseDao.insert(matchedInfo);
        } else {

            // check status
            if (!matchedInfo.getStatus().equals(InvoiceMatchStatus.MISMATCH)) {
                throw new BusinessException(MessageCodeConst.W1022);
            }
            
            // set values
            matchedInfo.setInvoiceNo(StringUtil.toString(param.getSwapData().get("invoiceNo")));
            matchedInfo.setUpdatedBy(param.getLoginUserId());
            matchedInfo.setUpdatedDate(dbTime);
            matchedInfo.setVersion(matchedInfo.getVersion() + IntDef.INT_ONE);
            
            // insert
            super.baseDao.update(matchedInfo);
        }
        
    }

}
