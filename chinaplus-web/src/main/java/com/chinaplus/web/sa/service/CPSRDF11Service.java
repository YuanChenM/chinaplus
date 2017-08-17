/**
 * @screen CPSRDF11Service
 * @author zhang_chi
 */
package com.chinaplus.web.sa.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.BusinessPattern;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.web.sa.entity.CPSRDF11Entity;

/**
 * CPSRDF11Service.
 */
@Service
public class CPSRDF11Service extends BaseService {

    /**
     * query query ttcPartNo + impOfficeCode + ttcCustomerCode ,partsId,businessPattern
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, String> getTITPMap(List<CPSRDF11Entity> initDataList) {
        Map<String, String> maps = new HashMap<String, String>();
        BaseParam param = new BaseParam();
        param.setSwapData("initDataList", initDataList);
        List<CPSRDF11Entity> dataList = baseMapper.select(this.getSqlId("getTITP"), param);
        if (dataList != null && dataList.size() > 0) {
            for (CPSRDF11Entity ce : dataList) {
                String key = ce.getTtcPartNo() + StringConst.UNDERLINE + ce.getImpOfficeCode() + StringConst.UNDERLINE
                        + ce.getTtcCustomerCode();
                String value = ce.getPartsId() + StringConst.UNDERLINE + ce.getBusinessPattern();
                maps.put(key, value);
            }
        }
        return maps;
    }

    /**
     * get current BusinessPattern Map
     * 
     * @param userOffice userOffice
     * @return maps maps
     */
    public Map<String, Object> getBusinessPatternMap(UserOffice userOffice) {
        Map<String, Object> maps = new HashMap<String, Object>();
        Map<String, Integer> officeCodeMap = new HashMap<String, Integer>();
        // Map<String, Integer> customerCodeMap = new HashMap<String, Integer>();
        Map<String, String> officeCustCodeMap = new HashMap<String, String>();

        if (userOffice != null) {
            String officeCode = userOffice.getOfficeCode();
            officeCodeMap.put(officeCode, userOffice.getOfficeId());
            List<BusinessPattern> busList = userOffice.getBusinessPatternList();
            if (busList != null && busList.size() > 0) {
                for (BusinessPattern bp : busList) {
                    String customerCode = bp.getCustomerCode();
                    // if (InactiveFlag.ACTIVE == bp.getInactiveFlag()) {
                    // customerCodeMap.put(customerCode, bp.getCustomerId());
                    // }
                    officeCustCodeMap.put(officeCode + StringConst.UNDERLINE + customerCode, bp.getBusinessPattern()
                            + StringConst.UNDERLINE + bp.getInactiveFlag());
                }
            }
        }
        maps.put("officeCodeMap", officeCodeMap);
        // maps.put("customerCodeMap", customerCodeMap);
        maps.put("officeCustCodeMap", officeCustCodeMap);
        return maps;
    }

    /**
     * deal DB Data
     * 
     * @param dataList dataList
     */
    public void doDealDBData(List<CPSRDF11Entity> dataList) {

        // get database time
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();
        
        for (CPSRDF11Entity cf : dataList) {
            
            // set update date
            cf.setUpdatedDate(dbTime);
            // save
            baseMapper.update(this.getSqlId("updateData"), cf);
        }
    }

}
