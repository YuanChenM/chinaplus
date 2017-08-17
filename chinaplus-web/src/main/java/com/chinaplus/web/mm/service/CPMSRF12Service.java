/**
 * @screen CPMSRF12Service
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinaplus.common.consts.CodeConst.ShipType;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.web.mm.entity.AisinCommonEntity;
import com.chinaplus.web.mm.entity.CPMSRF12Entity;

/**
 * CPMSRF12Service.
 */
public class CPMSRF12Service extends BaseService {

    /**
     * Get New shippingRouteCode,firstEtd,lastEtd,vanningDate
     * 
     * @param newList newList
     * @return dataList dataList
     */
    public List<CPMSRF12Entity> getNewShipRCodeFLV(List<CPMSRF12Entity> newList) {

        List<CPMSRF12Entity> dataList = new ArrayList<CPMSRF12Entity>();
        if (newList != null && newList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("newList", newList);
            dataList = baseMapper.select(this.getSqlId("getNewShipRCodeFLV"), param);

        }
        return dataList;
    }

    /**
     * Get mod shippingRouteCode,firstEtd,lastEtd,vanningDate
     * 
     * @param modList modList
     * @return dataList dataList
     */
    public List<CPMSRF12Entity> getModShipRCodeFLV(List<CPMSRF12Entity> modList) {

        List<CPMSRF12Entity> dataList = new ArrayList<CPMSRF12Entity>();

        if (modList != null && modList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("modList", modList);
            dataList = baseMapper.select(this.getSqlId("getModShipRCodeFLV"), param);
        }

        return dataList;
    }

    /**
     * Get shippingRouteCode,workingDays,expVanningLeadtime,impCcLeadtime,firstEtd,lastEtd,discontinueIndicator,officeId
     * 
     * @param allList allList
     * @return maps maps
     */
    public List<CPMSRF12Entity> getAllShipRCodeWEI(List<CPMSRF12Entity> allList) {
        List<CPMSRF12Entity> dataList = new ArrayList<CPMSRF12Entity>();
        if (allList != null && allList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("allList", allList);
            dataList = baseMapper.select(this.getSqlId("getAllShipRCodeWEI"), param);
        }
        return dataList;

    }

    /**
     * do new and mod data
     * 
     * @param cpmsrf12EntityList cpmsrf12EntityList
     * @param modChangeDetailList modChangeDetailList
     * @param modChangeList modChangeList
     */
    public void doNewAndModData(List<CPMSRF12Entity> cpmsrf12EntityList, List<AisinCommonEntity> modChangeDetailList,
        List<AisinCommonEntity> modChangeList) {

        Map<String, Integer> maps = new HashMap<String, Integer>();
        
        // get db time
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();

        for (CPMSRF12Entity ce : cpmsrf12EntityList) {
            
            // set date
            ce.setCreatedDate(dbTime);
            ce.setUpdatedDate(dbTime);
            
            String type = ce.getType();
            if (type.equals(ShipType.NEW)) {
                String key = ce.getShippingRouteCode() + StringConst.DOUBLE_UNDERLINE + ce.getFirstEtd().getTime()
                        + StringConst.DOUBLE_UNDERLINE + ce.getLastEtd().getTime() + StringConst.DOUBLE_UNDERLINE
                        + ce.getVanningDate();
                // inert TNM_SR_MASTER
                if (null == ce.getOfficeId()) {
                    this.baseMapper.insert(this.getSqlId("inertNewData"), ce);
                } else {
                    this.baseMapper.insert(this.getSqlId("inertNewDataO"), ce);
                }
                maps.put(key, ce.getSrId());
            } else if (type.equals(ShipType.MOD)) {
                // update TNM_SR_MASTER
                this.baseMapper.update(this.getSqlId("updateModData"), ce);
                // update TNM_SR_DETAIL for discontinue flag
                this.baseMapper.update(this.getSqlId("updateModDetaiData"), ce);
            }
        }

        if (modChangeDetailList != null && modChangeDetailList.size() > 0) {
            // delete TNM_SR_DETAIL
            BaseParam param = new BaseParam();
            for (AisinCommonEntity ce : modChangeDetailList) {
                Integer srId = ce.getSrId();
                if (null == srId) {
                    String key = ce.getShippingRouteCode() + StringConst.DOUBLE_UNDERLINE + ce.getFirstEtd().getTime()
                            + StringConst.DOUBLE_UNDERLINE + ce.getLastEtd().getTime() + StringConst.DOUBLE_UNDERLINE
                            + ce.getVanningDay();
                    srId = maps.get(key);
                    ce.setSrId(srId);
                }
            }
            param.setSwapData("dataLists", modChangeList);
            this.baseMapper.delete(this.getSqlId("deleteDataList"), param);

            // insert TNM_SR_DETAIL
            for (AisinCommonEntity ce : modChangeDetailList) {
                // set date
                ce.setCreatedDate(dbTime);
                ce.setUpdatedDate(dbTime);
                
                // update
                this.baseMapper.insert(this.getSqlId("insertDataList"), ce);
            }
        }
    }

}
