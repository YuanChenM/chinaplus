/**
 * CPOCFF06Service.java
 * 
 * @screen CPOCSF06
 * @author li_feng
 */
package com.chinaplus.web.om.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.web.om.entity.CPOCFF04Entity;
import com.chinaplus.web.om.entity.CPOCFFComDailyEntity;
import com.chinaplus.web.om.entity.CPOCFFComEntity;
import com.chinaplus.web.om.entity.CPOCFFComMonthlyEntity;

/**
 *CPOCFF06 Download Customer Forecast Service.
 */
@Service
public class CPOCFF06Service extends BaseService {
    
    /**
     * Get Parts Info
     * 
     * @param param BaseParam
     * @return CPOCFFComEntitylist
     */
    public List<CPOCFFComEntity> getPartsInfoByMonth(BaseParam param) {
        
        List<CPOCFFComEntity> list = new ArrayList<CPOCFFComEntity>();

        // get parts info
        List<CPOCFF04Entity> tempList = this.baseMapper.select(this.getSqlId("getPartsInfoByMonth"), param);
        LinkedHashMap<String, CPOCFFComEntity> entityMap = new LinkedHashMap<String, CPOCFFComEntity>();
        LinkedHashMap<String, List<CPOCFFComMonthlyEntity>> listMap = new LinkedHashMap<String, List<CPOCFFComMonthlyEntity>>();
        List<CPOCFFComMonthlyEntity> comList = new ArrayList<CPOCFFComMonthlyEntity>();

        
        for (CPOCFF04Entity cpocff04Entity : tempList) {
            CPOCFFComEntity conEntity = new CPOCFFComEntity();
            CPOCFFComMonthlyEntity comMonthlyEntity = new CPOCFFComMonthlyEntity();
            
            if(!listMap.containsKey(cpocff04Entity.getTtcPartsNo())){
                comList = new ArrayList<CPOCFFComMonthlyEntity>();
            }

            conEntity.setCfcId(cpocff04Entity.getCfcId());
            conEntity.setTtcPartsNo(cpocff04Entity.getTtcPartsNo());
            conEntity.setCustPartsNo(cpocff04Entity.getCustPartsNo());
            conEntity.setCustomerCode(cpocff04Entity.getCustomerCode());
            conEntity.setPartsType(cpocff04Entity.getPartsType());
            conEntity.setCarModel(cpocff04Entity.getCarModel());
            conEntity.setOrderLot(cpocff04Entity.getOrderLot());
            conEntity.setUomCode(cpocff04Entity.getUomCode());
            conEntity.setOldTtcPartsNo(cpocff04Entity.getOldTtcPartsNo());
            conEntity.setPartsNameEn(cpocff04Entity.getPartsNameEn());
            conEntity.setPartsNameCn(cpocff04Entity.getPartsNameCn());
            conEntity.setPartsId(cpocff04Entity.getPartsId());
            conEntity.setFirstFcMonth(cpocff04Entity.getFirstFcMonth());
            conEntity.setLastFcMonth(cpocff04Entity.getLastFcMonth());
            conEntity.setBusinessPattern(cpocff04Entity.getBusinessPattern());
            conEntity.setCfcNo(cpocff04Entity.getCfcNo());
            conEntity.setFcDate(cpocff04Entity.getFcDate());
            conEntity.setUploadedTime(cpocff04Entity.getUploadedTime());
            
            comMonthlyEntity.setCfcMonth(cpocff04Entity.getCfcMonth());
            comMonthlyEntity.setReceiveDate(cpocff04Entity.getFcDate());
            comMonthlyEntity.setCfcQty(cpocff04Entity.getCfcQty());
            
            comList.add(comMonthlyEntity);
            listMap.put(conEntity.getTtcPartsNo(), comList);
            

            
            if(!entityMap.containsKey(conEntity.getTtcPartsNo())){
                entityMap.put(conEntity.getTtcPartsNo(), conEntity);
            }
            
        }
        
        for(Map.Entry<String, CPOCFFComEntity> entry : entityMap.entrySet()){
            CPOCFFComEntity comEntity =  entry.getValue();
            comEntity.setCfcMonthlyLst(listMap.get(entry.getKey()));

            list.add(comEntity);
        }

        return list;
    }
    
    
    /**
     * Get Parts Info
     * 
     * @param param BaseParam
     * @return CPOCFFComEntitylist
     */
    public List<CPOCFFComEntity> getPartsInfoByDaily(BaseParam param) {
        
        List<CPOCFFComEntity> list = new ArrayList<CPOCFFComEntity>();

        // get parts info
        List<CPOCFF04Entity> tempList = this.baseMapper.select(this.getSqlId("getPartsInfoByDaily"), param);
        LinkedHashMap<String, CPOCFFComEntity> entityMap = new LinkedHashMap<String, CPOCFFComEntity>();
        LinkedHashMap<String, List<CPOCFFComDailyEntity>> listMap = new LinkedHashMap<String, List<CPOCFFComDailyEntity>>();
        List<CPOCFFComDailyEntity> comList = new ArrayList<CPOCFFComDailyEntity>();
        
        for (CPOCFF04Entity cpocff04Entity : tempList) {
            CPOCFFComEntity conEntity = new CPOCFFComEntity();
            CPOCFFComDailyEntity comDailyEntity = new CPOCFFComDailyEntity();

            if(!listMap.containsKey(cpocff04Entity.getTtcPartsNo())){
                comList = new ArrayList<CPOCFFComDailyEntity>();
            }

            conEntity.setCfcId(cpocff04Entity.getCfcId());
            conEntity.setTtcPartsNo(cpocff04Entity.getTtcPartsNo());
            conEntity.setCustPartsNo(cpocff04Entity.getCustPartsNo());
            conEntity.setCustomerCode(cpocff04Entity.getCustomerCode());
            conEntity.setPartsType(cpocff04Entity.getPartsType());
            conEntity.setCarModel(cpocff04Entity.getCarModel());
            conEntity.setOrderLot(cpocff04Entity.getOrderLot());
            conEntity.setUomCode(cpocff04Entity.getUomCode());
            conEntity.setOldTtcPartsNo(cpocff04Entity.getOldTtcPartsNo());
            conEntity.setPartsNameEn(cpocff04Entity.getPartsNameEn());
            conEntity.setPartsNameCn(cpocff04Entity.getPartsNameCn());
            conEntity.setPartsId(cpocff04Entity.getPartsId());
            conEntity.setFirstFcMonth(cpocff04Entity.getFirstFcMonth());
            conEntity.setLastFcMonth(cpocff04Entity.getLastFcMonth());
            conEntity.setBusinessPattern(cpocff04Entity.getBusinessPattern());
            conEntity.setCfcNo(cpocff04Entity.getCfcNo());
            conEntity.setFcDate(cpocff04Entity.getFcDate());
            conEntity.setUploadedTime(cpocff04Entity.getUploadedTime());
            
            comDailyEntity.setCfcDate(cpocff04Entity.getCfcDate());
            comDailyEntity.setWorkingFlag(cpocff04Entity.getWorkingFlag());
            comDailyEntity.setCfcQty(cpocff04Entity.getCfcQty());
            
            comList.add(comDailyEntity);
            listMap.put(conEntity.getTtcPartsNo(), comList);
            

            
            if(!entityMap.containsKey(conEntity.getTtcPartsNo())){
                entityMap.put(conEntity.getTtcPartsNo(), conEntity);
            }
            
        }
        
        for(Map.Entry<String, CPOCFFComEntity> entry : entityMap.entrySet()){
            CPOCFFComEntity comEntity =  entry.getValue();
            comEntity.setAdjustDateLst(listMap.get(entry.getKey()));

            list.add(comEntity);
        }

        return list;
    }
    
    
    /**
     * Get Calendar List
     * 
     * @param param BaseParam
     * @return CPOCFFComEntitylist
     */
    public LinkedHashMap<Date, Integer> getCalendarList(BaseParam param) {
        
        LinkedHashMap<Date, Integer> workingDayMap = new LinkedHashMap<Date, Integer>();
        List<CPOCFFComDailyEntity> dailyList = new ArrayList<CPOCFFComDailyEntity>();
        dailyList = this.baseMapper.select(this.getSqlId("getCalendar"), param);
        for (CPOCFFComDailyEntity cpocffComDailyEntity : dailyList) {
            workingDayMap.put(cpocffComDailyEntity.getAdjustDate(), cpocffComDailyEntity.getWorkingFlag());
        }
        return workingDayMap;
    }
    
    
    /**
     * get cfc month
     * 
     * @param custStartMonth String
     * @param custEndMonth String
     * @return List<String>
     */
    public List<String> getCfcMonth (String custStartMonth, String custEndMonth){
        List<String> lst = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(DateTimeUtil.parseMonth(custEndMonth));
        Date end = c.getTime();
        c.clear();
        c.setTime(DateTimeUtil.parseMonth(custStartMonth));
        Date start = c.getTime();
        if(start.getTime() == end.getTime()){
            lst.add(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, start)); 
        } else {
            for(Date date = start; date.getTime() < end.getTime();){
                
                date = c.getTime();  
                c.add(Calendar.MONTH, 1);
                lst.add(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, date));  
            }
        }
        return lst;
    }
    
}