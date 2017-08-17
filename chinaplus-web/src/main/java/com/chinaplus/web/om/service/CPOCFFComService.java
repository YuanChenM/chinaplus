/**
 * CPOCFFComService.java
 * 
 * @screen CPOCSF11
 * @author li_feng
 */
package com.chinaplus.web.om.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.web.om.entity.CPOCFF04Entity;
import com.chinaplus.web.om.entity.CPOCFF05Entity;
import com.chinaplus.web.om.entity.CPOCFFComEntity;
import com.chinaplus.web.om.entity.CPOCFFComMonthlyEntity;
import com.chinaplus.web.om.entity.DBTEMPEntity;

/**
 *CPOCFF Download Common Service.
 */
@Service
public class CPOCFFComService extends BaseService {
    
    
    /**
     * Get Parts Info
     * 
     * @param param BaseParam
     * @return CPOCFFComEntitylist
     */
    public List<CPOCFFComEntity> getPartsInfo(BaseParam param) {
        
        List<CPOCFFComEntity> list = new ArrayList<CPOCFFComEntity>();
        LinkedHashMap<String, List<CPOCFF04Entity>> actualQtyMap = new LinkedHashMap<String, List<CPOCFF04Entity>>();
        
        String custStartMonth =  (String) param.getSwapData().get("custStartMonth");
        String custEndMonth =  (String) param.getSwapData().get("custEndMonth");
        // get month beginning and end
        List<DBTEMPEntity> lst = getMonthBeginningAndEnd(custStartMonth, custEndMonth);
        if(lst != null) {
            if(!"CPOCFF04".equals(param.getSwapData().get("SelectFrom").toString())){
                param.setSwapData("customerCode", param.getSwapData().get("file1CfcNo").toString().substring(0, param.getSwapData().get("file1CfcNo").toString().indexOf("_")));
            }          
            for (DBTEMPEntity dbtempEntity : lst) {
                param.setSwapData("dbtempEntity", dbtempEntity);
                List<CPOCFF04Entity> actualQtyLst = getActualQty(param);
                if(actualQtyLst != null) {
                    actualQtyMap.put(dbtempEntity.getCfcMonth(), actualQtyLst);
                }
            }
        }
        
        // get parts info
        List<CPOCFF04Entity> tempList = this.baseMapper.select(this.getSqlId("getPartsInfo"), param);
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
            

            
            comMonthlyEntity.setCfcMonth(cpocff04Entity.getCfcMonth());
            comMonthlyEntity.setReceiveDate(cpocff04Entity.getFcDate());
            comMonthlyEntity.setCfcQty(cpocff04Entity.getCfcQty());
            
            List<CPOCFF04Entity> actualQtyLst = actualQtyMap.get(cpocff04Entity.getCfcMonth());
            if(actualQtyLst != null){
                for (CPOCFF04Entity cpocff04Entity2 : actualQtyLst) {
                    if(cpocff04Entity2.getPartsId().equals(cpocff04Entity.getPartsId())){
                        comMonthlyEntity.setActualQty(cpocff04Entity2.getActualQty());
                    }
                }
            }
            
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
     * get Month Beginning And End
     * 
     * @param custStartMonth String
     * @param custEndMonth String
     * @return List<DBTEMPEntity>
     */
    public List<DBTEMPEntity> getMonthBeginningAndEnd (String custStartMonth, String custEndMonth){
        List<DBTEMPEntity> list = new ArrayList<DBTEMPEntity>();
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(DateTimeUtil.parseMonth(custEndMonth));
        Date end = c.getTime();
        c.clear();
        c.setTime(DateTimeUtil.parseMonth(DateTimeUtil.formatDate("yyyyMM", super.getDBDateTimeByDefaultTimezone())));
        Date now = c.getTime(); 
        c.clear();
        c.setTime(DateTimeUtil.parseMonth(custStartMonth));
        Date start = c.getTime();
        if(now.getTime() > start.getTime()){
            if(now.getTime() <= end.getTime()){
                c.clear();
                c.setTime(now);
                c.add(Calendar.MONTH, -1);
                end = c.getTime();
            }
            c.clear();
            c.setTime(DateTimeUtil.parseMonth(custStartMonth));
            for(Date date = start; date.getTime() <= end.getTime();){
                DBTEMPEntity entity = new DBTEMPEntity();
                
                entity.setCfcMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, c.getTime()));
                entity.setFirstDayOfMonth(DateTimeUtil.firstDay(c.getTime()));
                entity.setLastDayOfMonth(DateTimeUtil.lastDay(c.getTime()));
                c.add(Calendar.MONTH, 1);
                date = c.getTime();
                list.add(entity);
            }
            return list; 
        }
        return null;
    }
    
    
    /**
     * get Actual Qty
     * 
     * @param param BaseParam
     * @return List<CPOCFF04Entity>
     */
    public List<CPOCFF04Entity> getActualQty (BaseParam param){
        List<CPOCFF04Entity> actualQtyLst = this.baseMapper.select(this.getSqlId("getActualQty"), param);
        return actualQtyLst;
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
    
    
    /**
     * get actual flag
     * 
     * @param cfcMonth String
     * @return boolean
     */
    public boolean getActualFlg (String cfcMonth) {
        
        boolean actualFlg = false;
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(DateTimeUtil.parseMonth(cfcMonth));
        Date cfcMonthDate = c.getTime();
        c.clear();
        c.setTime(DateTimeUtil.parseMonth(DateTimeUtil.formatDate("yyyyMM", super.getDBDateTimeByDefaultTimezone())));
        Date now = c.getTime(); 
        if(cfcMonthDate.getTime() < now.getTime()){
            actualFlg = true;
        }
        return actualFlg;
    }
    
    
    /**
     * get Month difference
     * 
     * @param startMonth String
     * @param endMonth String
     * @return Integer
     */
    public int getMonthDiff (String startMonth, String endMonth) {
        
        int difference = 0;
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(DateTimeUtil.parseMonth(endMonth));
        Date endMonthDate = c.getTime(); 
        c.clear();
        c.setTime(DateTimeUtil.parseMonth(startMonth));
        c.add(Calendar.MONTH, 1);
        Date nextMonthDate = c.getTime();
        
        if(nextMonthDate.getTime() == endMonthDate.getTime()){
            difference = 1;
        } else if(nextMonthDate.getTime() > endMonthDate.getTime()){ 
            difference = 0;
        } else {
            difference = IntDef.INT_TWO;
        }
        
        return difference;
    }
    
    
    /**
     * get last second month
     * 
     * @param endMonth String
     * @return Integer
     */
    public String getLastSecondMonth (String endMonth) {  
        String lastSecondMonth = "";
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(DateTimeUtil.parseMonth(endMonth));
        c.add(Calendar.MONTH, -1);
        Date nextMonthDate = c.getTime();
        lastSecondMonth = DateTimeUtil.formatDate("yyyyMM", nextMonthDate);
        return lastSecondMonth;
    }
    
    /**
     * get last second month
     * 
     * @param ttcPartsNo String
     * @param custPartsNo String
     * @param customerCode String
     * @param receiveDate Date
     * @param cfcMonth String
     * @param fileListEntity String
     * @return Object
     */
    public Object getCfcQty(String ttcPartsNo, String custPartsNo, String customerCode, Date receiveDate,
        String cfcMonth, List<CPOCFFComEntity> fileListEntity) {
        BigDecimal cfcQty = null;
        for (CPOCFFComEntity cpocffComEntity : fileListEntity) {
            if(cpocffComEntity.getTtcPartsNo().equals(ttcPartsNo)
                    && cpocffComEntity.getCustPartsNo().equals(custPartsNo)
                    && cpocffComEntity.getCustomerCode().equals(customerCode)){   
                List<CPOCFFComMonthlyEntity> comMonthlyEntitylist = cpocffComEntity.getCfcMonthlyLst();
                for (CPOCFFComMonthlyEntity cpocffComMonthlyEntity : comMonthlyEntitylist) {
                    if(cpocffComMonthlyEntity.getReceiveDate().equals(receiveDate)
                            && cpocffComMonthlyEntity.getCfcMonth().equals(
                                cfcMonth)){
                        cfcQty = cpocffComMonthlyEntity.getCfcQty();
                    }
                }
            
            }
        }
        if(cfcQty == null){
            return "NA";
        }
        return cfcQty;
    }
    
    
    /**
     * get tntRundownCfc info
     * 
     * @param param BaseParam
     *
     * @return List<TntRundownCfc>
     */
    public List<CPOCFF05Entity> getTntRundownCfcInfo(BaseParam param) {
       
        List<CPOCFF05Entity> cpocff05EntityList = new ArrayList<CPOCFF05Entity>();
        Date time = getDBDateTime(param.getOfficeTimezone());
        
        Date firstDay = DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, DateTimeUtil.firstDay(time)));
        param.setSwapData("firstDay", firstDay);
        param.setSwapData("lastDay", DateTimeUtil.lastDay(time));
        cpocff05EntityList = this.baseMapper.select(this.getSqlId("getTntRundownCfcInfo"), param);
        return cpocff05EntityList;
    }
    
    

    
    
    
}