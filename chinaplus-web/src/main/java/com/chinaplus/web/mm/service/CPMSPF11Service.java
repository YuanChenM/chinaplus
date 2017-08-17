/**
 * CPMSPF11Service.java
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.entity.TnmReason;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.StringUtil;

/**
 * Shipping Plan Rev Reason upload
 */
@Service
public class CPMSPF11Service extends BaseService {

    /**
     * check ttcCustomerCode exist emailalertmaster or not
     * 
     * @param param param
     * @return count
     */
    public int checkExist(BaseParam param) {
        return baseMapper.count(getSqlId("getReasonCount"), param);
    }

    /**
     * save data
     * 
     * @param list list data
     * @param param param
     */
    public void saveList(List<TnmReason> list, BaseParam param) {
        TnmReason dbVVMax = baseMapper.findOne(getSqlId("getMaxVVReasonCode"), param);
        int maxVV = 0;
        if (null != dbVVMax) {
            maxVV = Integer.parseInt(dbVVMax.getReasonCode());
        }
        TnmReason dbAISINMax = baseMapper.findOne(getSqlId("getMaxAISINReasonCode"), param);
        int maxAISIN = 0;
        if (null != dbAISINMax) {
            maxAISIN = Integer.parseInt(dbAISINMax.getReasonCode());
        }
        for (TnmReason entity : list) {
            Integer businessPattern = entity.getBusinessPattern();
            entity.setUpdatedBy(param.getLoginUserId());
            entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
            if ("NEW".equals(entity.getNewMod())) {
                int max = 0;
                if (businessPattern == CodeConst.BusinessPattern.V_V){
                    max = maxVV;
                } else if (businessPattern == CodeConst.BusinessPattern.AISIN){
                    max = maxAISIN;
                }
                max++;
                if (max == IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT) {
                    max++;
                }
                entity.setCreatedBy(param.getLoginUserId());
                entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
                entity.setVersion(IntDef.INT_ONE);
                if ("其它".equals(entity.getReasonDesc())){
                	entity.setReasonCode("999");
                } else {                	
                entity.setReasonCode(StringUtil.PadLeft(String.valueOf(max), IntDef.INT_THREE, "0"));
                }
                baseMapper.insert(getSqlId("addReason"), entity);
                if (businessPattern == CodeConst.BusinessPattern.V_V){
                    maxVV = max;
                } else if (businessPattern == CodeConst.BusinessPattern.AISIN){
                    maxAISIN = max;
                }
            }
            if ("MOD".equals(entity.getNewMod())) {
                baseMapper.update(getSqlId("updateReason"), entity);
            }
        }
    }
}
