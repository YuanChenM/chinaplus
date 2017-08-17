/**
 * @screen CPMSRF13Service
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
import com.chinaplus.web.mm.entity.CPMSRF13Entity;

/**
 * CPMSRF13Service.
 */
public class CPMSRF13Service extends BaseService {

    /**
     * Get New shippingRouteCode,firstEtd,lastEtd,vanningDate,deliveryStartDate
     * 
     * @param newList newList
     * @return dataList dataList
     */
    public List<CPMSRF13Entity> getNewShipRCodeFLV(List<CPMSRF13Entity> newList) {

        List<CPMSRF13Entity> dataList = new ArrayList<CPMSRF13Entity>();
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
    public List<CPMSRF13Entity> getModShipRCodeFLV(List<CPMSRF13Entity> modList) {
        List<CPMSRF13Entity> dataList = new ArrayList<CPMSRF13Entity>();
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
    public List<CPMSRF13Entity> getAllShipRCodeWEI(List<CPMSRF13Entity> allList) {
        List<CPMSRF13Entity> dataList = new ArrayList<CPMSRF13Entity>();
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
     * @param cpmsrf13EntityList cpmsrf13EntityList
     * @param modChangeDetailList modChangeDetailList
     * @param modChangeList modChangeList
     */
    public void doNewAndModData(List<CPMSRF13Entity> cpmsrf13EntityList, List<AisinCommonEntity> modChangeDetailList,
        List<AisinCommonEntity> modChangeList) {
        Map<String, Integer> maps = new HashMap<String, Integer>();
        
        // get db time stamp
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();

        for (CPMSRF13Entity ce : cpmsrf13EntityList) {
            
            // set date
            ce.setCreatedDate(dbTime);
            ce.setUpdatedDate(dbTime);
            
            List<Integer> vdlists = ce.getVanningDateList();
            for (Integer vanningDate : vdlists) {
                ce.setVanningDate(vanningDate);
                String type = ce.getType();
                if (type.equals(ShipType.NEW)) {
                    String key = ce.getShippingRouteCode() + StringConst.DOUBLE_UNDERLINE + ce.getFirstEtd().getTime()
                            + StringConst.DOUBLE_UNDERLINE + ce.getLastEtd().getTime() + StringConst.DOUBLE_UNDERLINE
                            + vanningDate + StringConst.DOUBLE_UNDERLINE + ce.getDeliveryStartDate().getTime();
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
        }

        if (modChangeDetailList != null && modChangeDetailList.size() > 0) {
            // delete TNM_SR_DETAIL
            BaseParam param = new BaseParam();
            for (AisinCommonEntity ce : modChangeDetailList) {
                Integer srId = ce.getSrId();
                if (null == srId) {
                    String key = ce.getShippingRouteCode() + StringConst.DOUBLE_UNDERLINE + ce.getFirstEtd().getTime()
                            + StringConst.DOUBLE_UNDERLINE + ce.getLastEtd().getTime() + StringConst.DOUBLE_UNDERLINE
                            + ce.getVanningDay() + StringConst.DOUBLE_UNDERLINE + ce.getDeliveryStartDate().getTime();
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
                // insert
                this.baseMapper.insert(this.getSqlId("insertDataList"), ce);
            }
        }
    }

}
