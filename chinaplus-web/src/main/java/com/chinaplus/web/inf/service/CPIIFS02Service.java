/**
 * @screen CPIIFS02
 * @author zhang_chi
 */
package com.chinaplus.web.inf.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.InvoiceMatchStatus;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TntMatchInvoice;
import com.chinaplus.common.service.ReceivedIpService;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * CPIIFS02Service.
 */
@Service
public class CPIIFS02Service extends BaseService {
    
    /**
     * cpiifs02Service.
     */
    @Autowired
    private ReceivedIpService ipService;
    
    
    /**
     * Get Office Code
     * 
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getOfficeCode() {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getOfficeCode"), param);
        return comboDataList;
    }

    
    /**
     * Get Matched InvoiceNo
     * 
     * @param lang lang
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getMatchedInvoice(String lang) {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getMatchedInvoice"), param);
        String id = MessageManager.getMessage("CPMPMS01_Grid_Blank", Language.ENGLISH.getLocale());
        String text = MessageManager.getMessage("CPMPMS01_Grid_Blank", lang);
        ComboData cd = new ComboData();
        cd.setId(id);
        cd.setText(text);
        if (comboDataList != null && comboDataList.size() > 0) {
            comboDataList.add(0, cd);
        } else {
            comboDataList = new ArrayList<ComboData>();
            comboDataList.add(cd);
        }
        return comboDataList;
    }
    
    /**
     * Get wrong InvoiceNo
     * 
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getWrongInvocie() {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getWrongInvocie"), param);
        return comboDataList;
    }

    /**
     * get match_invoice_id list
     * 
     * @param param param
     * @return list
     */
    public List<TntMatchInvoice> getMatchInvoiceIdList(PageParam param) {
        return baseMapper.select(this.getSqlId("getMatchInvoiceList"), param);
    }
   
    /**
     * do invoice matching process.
     * 
     * @param param param
     */
    public void doMatchInvoice(PageParam param) {
        
        // get all match invoice information
        List<TntMatchInvoice> matchInvoiceList = this.getMatchInvoiceIdList(param);
        List<Integer> matchInvIdList = new ArrayList<Integer>();
        Map<Integer, Timestamp> officeDateMap = new HashMap<Integer, Timestamp>();
        Timestamp officeDate = null;
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();

        // if not null or empty
        if (matchInvoiceList != null && !matchInvoiceList.isEmpty()) {
            
            // do update TNT_MATCH_INVOICE
            for(TntMatchInvoice matchInv : matchInvoiceList) {
                
                // get office date
                if(officeDateMap.containsKey(matchInv.getOfficeId())) {
                    officeDate = officeDateMap.get(matchInv.getOfficeId());
                } else {
                    TnmOffice office = super.baseDao.findOne(TnmOffice.class, matchInv.getOfficeId());
                    officeDate = super.getDBDateTime(office.getTimeZone());
                    officeDateMap.put(office.getOfficeId(), officeDate);
                }
                
                // set Mathced date
                matchInv.setMatchedDate(officeDate);
                matchInv.setStatus(InvoiceMatchStatus.MATCHED);
                matchInv.setUpdatedBy(param.getLoginUserId());
                matchInv.setUpdatedDate(dbTime);
                matchInv.setVersion(matchInv.getVersion() + IntDef.INT_ONE);
                
                // do update
                super.baseDao.update(matchInv);
                
                // set
                if (!matchInvIdList.contains(matchInv.getMatchInvoiceId())) {
                    matchInvIdList.add(matchInv.getMatchInvoiceId());
                }
            }
            
            // FULSH AND CLEAR
            baseDao.flush();
            baseDao.clear();
            
            // call interface
            ipService.doReceiveIpForMatchInvoice(matchInvIdList);
        }
    }
    
}
