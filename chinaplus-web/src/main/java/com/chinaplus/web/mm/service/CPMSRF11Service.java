/**
 * @screen CPMSRF11
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.consts.CodeConst.ShipType;
import com.chinaplus.common.consts.CodeConst.WeekDay;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMSRF11DateEntity;
import com.chinaplus.web.mm.entity.CPMSRF11Entity;

/**
 * CPMSRF11Service.
 */
@Service
public class CPMSRF11Service extends BaseService {

    /**
     * aisinCommonService.
     */
    @Autowired
    private AisinCommonService aisinCommonService;

    /**
     * Get new getInactiveFlag
     * 
     * @return maps maps
     */
    public Map<String, Object> getNewInactiveFlag() {
        Map<String, Object> maps = new HashMap<String, Object>();
        BaseParam param = new BaseParam();
        List<ComboData> dataList = baseMapper.select(this.getSqlId("getNewInactiveFlag"), param);
        for (ComboData cd : dataList) {
            maps.put(cd.getText(), cd.getId());
        }
        return maps;
    }

    /**
     * Get mod getInactiveFlag
     * 
     * @return maps maps
     */
    public Map<String, Object> getModInactiveFlag() {
        Map<String, Object> maps = new HashMap<String, Object>();
        BaseParam param = new BaseParam();
        List<ComboData> dataList = baseMapper.select(this.getSqlId("getModInactiveFlag"), param);
        for (ComboData cd : dataList) {
            maps.put(cd.getText(), cd.getId());
        }
        return maps;
    }

    /**
     * Get New shippingRouteCode,deliveryStart,deliveryEnd,shippingRouteType
     * 
     * @param cpmsrf11EntityList cpmsrf11EntityList
     * @return dataList dataList
     */
    public List<CPMSRF11Entity> getNewShipRCodeWSEMaps(List<CPMSRF11Entity> cpmsrf11EntityList) {
        List<CPMSRF11Entity> dataList = new ArrayList<CPMSRF11Entity>();
        if (cpmsrf11EntityList != null && cpmsrf11EntityList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("cpmsrf11EntityList", cpmsrf11EntityList);
            dataList = baseMapper.select(this.getSqlId("getNewShipRCodeWSE"), param);
        }
        return dataList;
    }

    /**
     * Get mod shippingRouteCode,deliveryStart,deliveryEnd
     * 
     * @param cpmsrf11EntityList cpmsrf11EntityList
     * @return maps maps
     */
    public Map<String, Object> getModShipRCodeWSEMaps(List<CPMSRF11Entity> cpmsrf11EntityList) {

        Map<String, Object> maps = new HashMap<String, Object>();

        Map<String, Object> shipRCodeWSEMapsMDB = new HashMap<String, Object>();
        Map<String, String> modDataYNMapsDB = new HashMap<String, String>();

        if (cpmsrf11EntityList != null && cpmsrf11EntityList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("cpmsrf11EntityList", cpmsrf11EntityList);
            List<CPMSRF11Entity> dataList = baseMapper.select(this.getSqlId("getModShipRCodeWSE"), param);

            for (CPMSRF11Entity entity : dataList) {
                String key = entity.getShippingRouteCode() + StringConst.DOUBLE_UNDERLINE
                        + entity.getDeliveryStart().getTime() + StringConst.DOUBLE_UNDERLINE
                        + entity.getDeliveryEnd().getTime();
                shipRCodeWSEMapsMDB.put(key, entity.getShippingRouteCode());

                String discontinueIndicator = entity.getInactiveFlag();
                modDataYNMapsDB.put(key, discontinueIndicator);
            }
        }
        maps.put("shipRCodeWSEMapsMDB", shipRCodeWSEMapsMDB);
        maps.put("modDataYNMapsDB", modDataYNMapsDB);
        return maps;
    }

    /**
     * Get shippingRouteCode + impCcLeadtime + impInboundLeadtime + etd + officeId
     * 
     * @param cpmsrf11EntityList cpmsrf11EntityList
     * @return maps maps
     */
    public List<CPMSRF11Entity> getSice(List<CPMSRF11Entity> cpmsrf11EntityList) {
        BaseParam param = new BaseParam();
        param.setSwapData("cpmsrf11EntityList", cpmsrf11EntityList);
        List<CPMSRF11Entity> dataList = baseMapper.select(this.getSqlId("getSice"), param);
        return dataList;
    }

    /**
     * Get shippingRouteCode + eta + deliveryStart + deliveryEnd + inactiveFlag
     * 
     * @param cpmsrf11EntityList cpmsrf11EntityList
     * @return maps maps
     */
    public List<CPMSRF11Entity> getShipRCodeEta(List<CPMSRF11Entity> cpmsrf11EntityList) {
        BaseParam param = new BaseParam();
        param.setSwapData("cpmsrf11EntityList", cpmsrf11EntityList);
        List<CPMSRF11Entity> dataList = baseMapper.select(this.getSqlId("getShipRCodeEta"), param);

        return dataList;
    }

    /**
     * check userOffIds is have officeId
     * 
     * @param userOffIds userOffIds
     * @param officeId officeId
     * @return flag
     */
    public boolean checkOfficeId(List<Integer> userOffIds, Integer officeId) {
        boolean flag = false;
        if (userOffIds == null) {
            return flag;
        }
        for (Integer oid : userOffIds) {
            if (officeId.equals(oid)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * get DB time
     * 
     * @return long long
     */
    public long getDBtime() {

        Timestamp now = this.getDBDateTimeByDefaultTimezone();

        return now.getTime();
    }

    /**
     * Intervals deliveryStart deliveryEnd
     * 
     * @param lists lists
     * @param start start
     * @param end end
     * @return boolean boolean
     */
    // public boolean intervals(List<CPMSRF11DateEntity> lists, long start, long end, List<CPMSRF11DateEntity>
    // modYLists) {
    public boolean intervals(List<CPMSRF11DateEntity> lists, long start, long end) {
        boolean flag = true;

        if (lists != null && lists.size() > 0) {

            for (CPMSRF11DateEntity cf : lists) {
                long startCf = cf.getStart().getTime();
                long endCf = cf.getEnd().getTime();

                if (startCf == start && endCf == end) {
                    continue;
                } else if (startCf >= start && startCf <= end) {
                    // if (!checkModYData(start, end, modYLists) && !checkModYData(startCf, endCf, modYLists)) {
                    flag = false;
                    break;
                    // }
                } else if (endCf >= start && endCf <= end) {
                    // if (!checkModYData(start, end, modYLists) && !checkModYData(startCf, endCf, modYLists)) {
                    flag = false;
                    break;
                    // }
                } else if (startCf <= start && endCf >= end) {
                    // if (!checkModYData(start, end, modYLists) && !checkModYData(startCf, endCf, modYLists)) {
                    flag = false;
                    break;
                    // }
                }
            }
        }
        return flag;
    }

    // /**
    // * Intervals db deliveryStart deliveryEnd
    // *
    // * @param lists lists
    // * @param start start
    // * @param end end
    // * @param modYLists modYLists
    // * @return boolean boolean
    // */
    // public boolean intervalsDB(List<CPMSRF11DateEntity> lists, long start, long end, List<CPMSRF11DateEntity>
    // modYLists) {
    //
    // boolean flag = true;
    //
    // if (lists != null && lists.size() > 0) {
    //
    // for (CPMSRF11DateEntity cf : lists) {
    //
    // long startCf = cf.getStart().getTime();
    // long endCf = cf.getEnd().getTime();
    //
    // if (startCf == start && endCf == end) {
    // continue;
    // } else if (startCf >= start && startCf <= end) {
    // if (!checkModYData(start, end, modYLists)) {
    // flag = false;
    // break;
    // }
    // } else if (endCf >= start && endCf <= end) {
    // if (!checkModYData(start, end, modYLists)) {
    // flag = false;
    // break;
    // }
    // } else if (startCf <= start && endCf >= end) {
    // if (!checkModYData(start, end, modYLists)) {
    // flag = false;
    // break;
    // }
    // }
    // }
    // }
    // return flag;
    // }

    // /**
    // * Intervals deliveryStart deliveryEnd
    // *
    // * @param lists lists
    // * @param start start
    // * @param end end
    // * @param modYLists modYLists
    // * @return boolean boolean
    // */
    // public boolean intervalsNewDB(List<CPMSRF11DateEntity> lists, long start, long end,
    // List<CPMSRF11DateEntity> modYLists) {
    //
    // boolean flag = true;
    //
    // if (lists != null && lists.size() > 0) {
    //
    // for (CPMSRF11DateEntity cf : lists) {
    //
    // long startCf = cf.getStart().getTime();
    // long endCf = cf.getEnd().getTime();
    //
    // if (startCf == start && endCf == end) {
    // continue;
    // } else if (startCf >= start && startCf <= end) {
    // if (!checkModYData(startCf, endCf, modYLists)) {
    // flag = false;
    // break;
    // }
    // } else if (endCf >= start && endCf <= end) {
    // if (!checkModYData(startCf, endCf, modYLists)) {
    // flag = false;
    // break;
    // }
    // } else if (startCf <= start && endCf >= end) {
    // if (!checkModYData(startCf, endCf, modYLists)) {
    // flag = false;
    // break;
    // }
    // }
    // }
    // }
    // return flag;
    // }

    // /**
    // * check type Mod flag is Y Data
    // *
    // * @param startDB startDB
    // * @param endDB endDB
    // * @param modYLists modYLists
    // * @return boolean boolean
    // */
    // private boolean checkModYData(long startDB, long endDB, List<CPMSRF11DateEntity> modYLists) {
    // boolean flag = false;
    // if (modYLists != null && modYLists.size() > 0) {
    // for (CPMSRF11DateEntity cf : modYLists) {
    //
    // long startY = cf.getStart().getTime();
    // long endY = cf.getEnd().getTime();
    //
    // if (startDB == startY && endDB == endY) {
    // flag = true;
    // break;
    // }
    // }
    // }
    // return flag;
    // }

    /**
     * check StartDate + EndDate is or not continuous
     * 
     * @param dateList dateList
     * @param dateListDB dateListDB
     * @return flag flag
     */
    public boolean checkDateContinuous(List<CPMSRF11DateEntity> dateList,List<CPMSRF11DateEntity> dateListDB) {

        if(dateListDB!=null && dateListDB.size()>0){
            dateList.addAll(dateListDB);
        }
    
        Collections.sort(dateList);
        
        for (int i = 1; i <= dateList.size(); i++) {
            if (i == dateList.size()) {
                break;
            }
            Date endf = dateList.get(i - 1).getEnd();
            Date end = dateList.get(i).getEnd();
            
            if(endf.equals(end)){
                continue;
            }
                     
            Date starts = dateList.get(i).getStart();            
          
            for (int j = 1; j <= IntDef.INT_THREE; j++) {
                Date date = aisinCommonService.getAfterDate(endf, j);
                if (date.equals(starts)) {
                    break;
                } else {
                    Integer days = aisinCommonService.getWhichDay(date);
                    if (!WeekDay.ON_SATURDAY.equals(days + "") && !WeekDay.ON_SUNDAY.equals(days + "")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * do new and mod data
     * 
     * 
     * @param cpmsrf11EntityList cpmsrf11EntityList
     * @param shipRCodelist shipRCodelist
     */
    public void doNewAndModData(List<CPMSRF11Entity> cpmsrf11EntityList, List<String> shipRCodelist) {

        Map<String, String> maps = new HashMap<String, String>();
        Map<String, String> modMaps = new HashMap<String, String>();
        
        // get db time 
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();

        for (CPMSRF11Entity cf : cpmsrf11EntityList) {

            String shippingRouteCode = cf.getShippingRouteCode();
            String modId = modMaps.get(shippingRouteCode);
            
            // set date
            cf.setCreatedDate(dbTime);
            cf.setUpdatedDate(dbTime);

            // do new
            if (cf.getType().equals(ShipType.NEW)) {
                // do new insert
                if (null == cf.getSrId()) {
                    String srid = maps.get(shippingRouteCode);
                    if (StringUtil.isNullOrEmpty(srid)) {
                        // inert TNM_SR_MASTER
                        this.baseMapper.insert(this.getSqlId("inertNewData"), cf);   
                        // insert TNM_SR_DETAIL
                        this.baseMapper.insert(this.getSqlId("inertNewDataDetail"), cf);
                        maps.put(shippingRouteCode, cf.getSrId() + "");
                    } else {
                        String id = maps.get(shippingRouteCode);
                        cf.setSrId(Integer.valueOf(id));
                        // insert TNM_SR_DETAIL
                        this.baseMapper.insert(this.getSqlId("inertNewDataDetail"), cf);
                    }
                }
                // do new update
                else {
                    if (StringUtil.isNullOrEmpty(modId)) {
                        // update TNM_SR_MASTER
                        this.baseMapper.update(this.getSqlId("updateNewData"), cf);
                        modMaps.put(shippingRouteCode, shippingRouteCode);
                    }
                    // insert TNM_SR_DETAIL
                    this.baseMapper.insert(this.getSqlId("updateNewDataDetail"), cf);

                }
            }
            // do mod
            else if (cf.getType().equals(ShipType.MOD)) {
                if (StringUtil.isNullOrEmpty(modId)) {
                    // update TNM_SR_MASTER
                    this.baseMapper.update(this.getSqlId("updateModDataYN"), cf);
                    modMaps.put(shippingRouteCode, shippingRouteCode);
                }
                // update TNM_SR_DETAIL
                this.baseMapper.update(this.getSqlId("updateModDataDetailYN"), cf);
            }
        }

        // delete TNM_SR_MASTER
        if (shipRCodelist != null && shipRCodelist.size() > 0) {
            CPMSRF11Entity cf = new CPMSRF11Entity();
            cf.setUpdatedBy(cpmsrf11EntityList.get(0).getUpdatedBy());
            cf.setUpdatedDate(dbTime);
            for (String s : shipRCodelist) {
                cf.setShippingRouteCode(s);
                this.baseMapper.update(this.getSqlId("deleteMaster"), cf);
            }
        }

    }
}
