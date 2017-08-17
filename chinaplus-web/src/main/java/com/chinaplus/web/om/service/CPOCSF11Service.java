/**
 * CPOCSF11Service.java
 * 
 * @screen CPOCSF11
 * @author li_feng
 */
package com.chinaplus.web.om.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOCSF11Entity;
import com.chinaplus.web.om.entity.DBTEMPEntity;

;

/**
 * Customer Stock Upload Screen Service.
 */
@Service
public class CPOCSF11Service extends BaseService {

    private static final String KEY_SEPARATOR = "***separator***";

    /**
     * Get business patterns.
     * 
     * @return all roles
     */
    public List<CPOCSF11Entity> getBusinessPatterns() {

        CPOCSF11Entity cpocsf11Entity = new CPOCSF11Entity();
        cpocsf11Entity.setCodeCategory("BUSINESS_PATTERN");
        BaseParam param = new BaseParam();
        param.setSwapData("codeCategory", cpocsf11Entity.getCodeCategory());
        List<CPOCSF11Entity> list = this.baseMapper.select(this.getSqlId("getBusinessPatterns"), param);
        return list;
    }

    /**
     * Get Check Info.
     * 
     * @param param BaseParam
     * @return all info
     */
    public List<CPOCSF11Entity> getCheckInfo(BaseParam param) {
        List<CPOCSF11Entity> list = this.baseMapper.select(this.getSqlId("getAllcheckInfo"), param);
        return list;
    }

    /**
     * Get uomMap.
     * 
     * @param param BaseParam
     * @return AllUom
     */
    public Map<String, Integer> getAllUom(BaseParam param) {
        Map<String, Integer> uomMap = new HashMap<String, Integer>();

        Integer bsPtn = (Integer) param.getSwapData().get("businessPattern");
        String uomMapKey = null;
        List<CPOCSF11Entity> list = this.baseMapper.select(this.getSqlId("getAllcheckInfo"), param);

        if (BusinessPattern.V_V == bsPtn) {
            for (CPOCSF11Entity entity : list) {
                uomMapKey = new StringBuilder().append(entity.getTtcPartsNo()).append(KEY_SEPARATOR)
                    .append(entity.getCustomerCode()).toString();
                uomMap.put(uomMapKey, entity.getDecimalDigits());
            }
        } else if (BusinessPattern.AISIN == bsPtn) {
            for (CPOCSF11Entity entity : list) {
                uomMapKey = new StringBuilder().append(entity.getCustPartsNo()).append(KEY_SEPARATOR)
                    .append(entity.getCustomerCode()).toString();
                uomMap.put(uomMapKey, entity.getDecimalDigits());
            }
        }

        return uomMap;
    }

    /**
     * Get customer code.
     * 
     * @param param BaseParam
     * @return UserCustomerCode
     */
    public Map<String, String> getUserCustomerCode(BaseParam param) {
        Map<String, String> customerCodeMap = new HashMap<String, String>();

        List<CPOCSF11Entity> list = this.baseMapper.select(this.getSqlId("getUserCustomerCode"), param);

        for (CPOCSF11Entity entity : list) {
            customerCodeMap.put(entity.getCustomerCode(), entity.getCustomerCode());
        }

        return customerCodeMap;
    }

    /**
     * Get Registered PartNo.
     * 
     * @param param BaseParam
     * @return RegisteredPartNo
     */
    public Map<String, String> getRegisteredPartNo(BaseParam param) {
        Map<String, String> registeredPartNoMap = new HashMap<String, String>();

        Integer bsPtn = (Integer) param.getSwapData().get("businessPattern");
        String registeredPartNoMapMapKey = null;
        List<CPOCSF11Entity> list = this.baseMapper.select(this.getSqlId("getRegisteredPartNo"), param);

        if (BusinessPattern.V_V == bsPtn) {
            for (CPOCSF11Entity entity : list) {
                registeredPartNoMapMapKey = new StringBuilder().append(entity.getTtcPartsNo()).append(KEY_SEPARATOR)
                    .append(entity.getCustomerCode()).toString();
                registeredPartNoMap.put(registeredPartNoMapMapKey, entity.getTtcPartsNo());
            }
        } else if (BusinessPattern.AISIN == bsPtn) {
            for (CPOCSF11Entity entity : list) {
                registeredPartNoMapMapKey = new StringBuilder().append(entity.getCustPartsNo()).append(KEY_SEPARATOR)
                    .append(entity.getCustomerCode()).toString();
                registeredPartNoMap.put(registeredPartNoMapMapKey, entity.getCustPartsNo());
            }
        }

        return registeredPartNoMap;
    }

    /**
     * Get Ending Stock Date.
     * 
     * @param param BaseParam
     * @return EndingStockDate
     */
    public Map<String, Date> getEndingStockDate(BaseParam param) {
        Map<String, Date> endingStockDateMap = new HashMap<String, Date>();

        Integer bsPtn = (Integer) param.getSwapData().get("businessPattern");
        String registeredPartNoMapMapKey = null;
        List<CPOCSF11Entity> list = this.baseMapper.select(this.getSqlId("getEndingStockDate"), param);

        if (BusinessPattern.V_V == bsPtn) {
            for (CPOCSF11Entity entity : list) {
                registeredPartNoMapMapKey = new StringBuilder().append(entity.getTtcPartsNo()).append(KEY_SEPARATOR)
                    .append(entity.getCustomerCode()).toString();
                endingStockDateMap.put(registeredPartNoMapMapKey, entity.getEndingStockDate());
            }
        } else if (BusinessPattern.AISIN == bsPtn) {
            for (CPOCSF11Entity entity : list) {
                registeredPartNoMapMapKey = new StringBuilder().append(entity.getCustPartsNo()).append(KEY_SEPARATOR)
                    .append(entity.getCustomerCode()).toString();
                endingStockDateMap.put(registeredPartNoMapMapKey, entity.getEndingStockDate());
            }
        }

        return endingStockDateMap;
    }

    /**
     * do upload
     * 
     * @param param BaseParam
     * @param list UploadDataList
     */
    public void doUpdateList(BaseParam param, List<CPOCSF11Entity> list) {

        int bsPtn = StringUtil.toSafeInteger(param.getSwapData().get("businessPattern").toString());
        List<DBTEMPEntity> dbTempList = new ArrayList<DBTEMPEntity>();

        for (CPOCSF11Entity entity : list) {
            if (entity.getDataEffectiveFlg()) {

                if (BusinessPattern.V_V == bsPtn) {
                    param.setSwapData("BSPTN", BusinessPattern.V_V);
                    param.setSwapData("ENTITY", entity);
                    // findOldData
                    dbTempList = this.baseMapper.select(this.getSqlId("findOldData"), param);
                    if (dbTempList.size() == 0) {
                        dbTempList = this.baseMapper.select(this.getSqlId("selectDataValue"), param);
                        param.setSwapData("PARTS_ID", dbTempList.get(0).getPARTSID());
                        param.setSwapData("CUSTOMER_ID", dbTempList.get(0).getCUSTOMERID());
                        param.setSwapData("CREATED_DATE", super.getDBDateTimeByDefaultTimezone());
                        param.setSwapData("UPDATED_DATE", super.getDBDateTimeByDefaultTimezone());
                        // updateOldStatus
                        this.baseMapper.update(this.getSqlId("updateOldStatus"), param);
                        // InsertData
                        this.baseMapper.insert(this.getSqlId("InsertData"), param);
                    } else {
                        dbTempList = this.baseMapper.select(this.getSqlId("selectDataValue"), param);
                        param.setSwapData("PARTS_ID", dbTempList.get(0).getPARTSID());
                        param.setSwapData("CUSTOMER_ID", dbTempList.get(0).getCUSTOMERID());
                        param.setSwapData("UPDATED_DATE", super.getDBDateTimeByDefaultTimezone());
                        // updateOldCustomerStock
                        this.baseMapper.update(this.getSqlId("updateOldCustomerStock"), param);
                    }

                } else if (BusinessPattern.AISIN == bsPtn) {
                    param.setSwapData("BSPTN", BusinessPattern.AISIN);
                    param.setSwapData("ENTITY", entity);
                    // findOldData
                    dbTempList = this.baseMapper.select(this.getSqlId("findOldData"), param);
                    if (dbTempList.size() == 0) {
                        dbTempList = this.baseMapper.select(this.getSqlId("selectDataValue"), param);
                        param.setSwapData("PARTS_ID", dbTempList.get(0).getPARTSID());
                        param.setSwapData("CUSTOMER_ID", dbTempList.get(0).getCUSTOMERID());
                        param.setSwapData("CREATED_DATE", super.getDBDateTimeByDefaultTimezone());
                        param.setSwapData("UPDATED_DATE", super.getDBDateTimeByDefaultTimezone());
                        // updateOldStatus
                        this.baseMapper.update(this.getSqlId("updateOldStatus"), param);
                        // InsertData
                        this.baseMapper.insert(this.getSqlId("InsertData"), param);
                    } else {
                        dbTempList = this.baseMapper.select(this.getSqlId("selectDataValue"), param);
                        param.setSwapData("PARTS_ID", dbTempList.get(0).getPARTSID());
                        param.setSwapData("CUSTOMER_ID", dbTempList.get(0).getCUSTOMERID());
                        param.setSwapData("UPDATED_DATE", super.getDBDateTimeByDefaultTimezone());
                        // updateOldCustomerStock
                        this.baseMapper.update(this.getSqlId("updateOldCustomerStock"), param);
                    }
                }
            }
        }
    }

}