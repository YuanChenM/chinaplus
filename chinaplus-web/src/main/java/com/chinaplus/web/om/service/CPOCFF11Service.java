/**
 * CPOCSF11Service.java
 * 
 * @screen CPOCSF11
 * @author li_feng
 */
package com.chinaplus.web.om.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.entity.TnmPartsMaster;
import com.chinaplus.common.entity.TntCfcDay;
import com.chinaplus.common.entity.TntCfcMonth;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOCFF11Entity;
import com.chinaplus.web.om.entity.DBTEMPEntity;

/**
 * Customer Stock Upload Screen Service.
 */
@Service
public class CPOCFF11Service extends BaseService {

    /** upload type byDay */
    private static final String UPLOAD_TYPE_BYDAY = "byDay";

    /**
     * Get Customer Calendar.
     * 
     * @param param BaseParam
     * @return customer calendar list
     */
    public Map<String, List<CPOCFF11Entity>> getCalendarInfo(BaseParam param) {

        List<CPOCFF11Entity> list = this.baseMapper.select(this.getSqlId("getCalendarInfoLst"), param);

        Map<String, List<CPOCFF11Entity>> calendarMap = new LinkedHashMap<String, List<CPOCFF11Entity>>();
        List<CPOCFF11Entity> cusCalLst = null;

        if (null != list && !list.isEmpty()) {

            for (CPOCFF11Entity info : list) {

                if (null == calendarMap.get(info.getCustomerCode())) {

                    cusCalLst = new ArrayList<CPOCFF11Entity>();
                    cusCalLst.add(info);
                    calendarMap.put(info.getCustomerCode(), cusCalLst);
                } else {

                    cusCalLst.add(info);
                }
            }
        }

        return calendarMap;
    }

    /**
     * Get customer last forecast month.
     * 
     * @param param BaseParam
     * @return customer calendar list
     */
    public Map<String, String> getLastFcMonth(BaseParam param) {

        List<CPOCFF11Entity> list = this.baseMapper.select(this.getSqlId("getLastFcMonth"), param);

        Map<String, String> fcMonthMap = new HashMap<String, String>();

        if (null != list && !list.isEmpty()) {

            for (CPOCFF11Entity info : list) {

                fcMonthMap.put(info.getCustomerCode(), info.getLastFcMonth());
            }
        }

        return fcMonthMap;
    }

    /**
     * Get customer first forecast month.
     * 
     * @param param BaseParam
     * @return customer calendar list
     */
    public Map<String, String> getFirstFcMonth(BaseParam param) {

        List<CPOCFF11Entity> list = this.baseMapper.select(this.getSqlId("getFirstFcMonth"), param);

        Map<String, String> fcMonthMap = new HashMap<String, String>();

        if (null != list && !list.isEmpty()) {

            for (CPOCFF11Entity info : list) {

                fcMonthMap.put(info.getCustomerCode(), info.getFirstFcMonth());
            }
        }

        return fcMonthMap;
    }

    /**
     * Get Registered Parts.
     * 
     * @param param BaseParam
     * @return Registered Parts
     */
    public Map<String, TnmPartsMaster> getRegisteredParts(BaseParam param) {

        List<TnmPartsMaster> list = this.baseMapper.select(this.getSqlId("getRegisteredParts"), param);
        Map<String, TnmPartsMaster> psrtsMap = new HashMap<String, TnmPartsMaster>();

        if (null != list && !list.isEmpty()) {

            for (TnmPartsMaster parts : list) {

                StringBuffer bfKey = new StringBuffer();
                bfKey.append(parts.getCustomerCode()).append(StringConst.COMMA);

                if (BusinessPattern.V_V == parts.getBusinessPattern().intValue()) {

                    psrtsMap.put(bfKey.append(parts.getTtcPartsNo()).toString(), parts);
                } else {

                    psrtsMap.put(bfKey.append(parts.getCustPartsNo()).toString(), parts);
                }
            }
        }

        return psrtsMap;
    }

    
    /**
     * Get Registered Parts.
     * 
     * @param param BaseParam
     * @return Registered Parts
     */
    public Map<String, TnmPartsMaster> getCfcMonth(BaseParam param) {

        List<TnmPartsMaster> list = this.baseMapper.select(this.getSqlId("getCfcMonthParts"), param);
        Map<String, TnmPartsMaster> psrtsMap = new HashMap<String, TnmPartsMaster>();

        if (null != list && !list.isEmpty()) {

            for (TnmPartsMaster parts : list) {

                StringBuffer bfKey = new StringBuffer();
                bfKey.append(parts.getCustomerCode()).append(StringConst.COMMA);

                if (BusinessPattern.V_V == parts.getBusinessPattern().intValue()) {

                    psrtsMap.put(bfKey.append(parts.getTtcPartsNo()).toString(), parts);
                } else {

                    psrtsMap.put(bfKey.append(parts.getCustPartsNo()).toString(), parts);
                }
            }
        }

        return psrtsMap;
    }
    
    /**
     * Get last received date.
     * 
     * @param param BaseParam
     * @return ReceivedDateMap
     */
    public List<CPOCFF11Entity> getLastReceivedDate(BaseParam param) {

        List<CPOCFF11Entity> list = this.baseMapper.select(this.getSqlId("getLastReceivedDate"), param);

        return list;
    }

    /**
     * do upload
     * 
     * @param param BaseParam
     * @return dbTempList List<DBTEMPEntity>
     */
    public List<DBTEMPEntity> getOldReceivedDateData(BaseParam param) {
        List<DBTEMPEntity> dbTempList = this.baseMapper.select(this.getSqlId("findDiffReceivedDateData"), param);
        return dbTempList;
    }

    /**
     * get diff codeCalendar
     * 
     * @param param BaseParam
     * @return codeCalendarMap LinkedHashMap<String, List<CPOCFF11Entity>>
     */
    public LinkedHashMap<String, List<CPOCFF11Entity>> getDiffCodeCalendar(BaseParam param) {

        LinkedHashMap<String, List<CPOCFF11Entity>> codeCalendarMap = new LinkedHashMap<String, List<CPOCFF11Entity>>();
        List<CPOCFF11Entity> dbTempList = this.baseMapper.select(this.getSqlId("findDiffCalendar"), param);
        @SuppressWarnings("unchecked")
        List<String> customerCodeLst = (List<String>) param.getSwapData().get("CustomerCodeLst");
        for (String list : customerCodeLst) {
            List<CPOCFF11Entity> calendarList = new ArrayList<CPOCFF11Entity>();
            for (CPOCFF11Entity entity : dbTempList) {
                if (entity.getCustomerCode().equals(list)) {
                    calendarList.add(entity);
                }
            }
            codeCalendarMap.put(list, calendarList);
        }
        return codeCalendarMap;
    }

    /**
     * do upload
     * 
     * @param param BaseParam
     * @param allDatalst List<CPOCFCOMEntity>
     */
    @SuppressWarnings("unchecked")
    public void doDataUpdate(BaseParam param, List<CPOCFF11Entity> allDatalst,
        List<List<CPOCFF11Entity>> allCalendarInfoLst) {

        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
        Timestamp officeTime = super.getDBDateTime(param.getOfficeTimezone());

        for (CPOCFF11Entity list : allDatalst) {

            List<String> customerCodelist = list.getCustomerCodeLst();
            if (null != customerCodelist && !customerCodelist.isEmpty()) {
                Map<String, Boolean> insertMasterMap = new LinkedHashMap<String, Boolean>();
                for (String codelist : customerCodelist) {
                    insertMasterMap.put(codelist, true);
                }
                param.setSwapData("FirstFcMonth", list.getFirstFcMonth());
                param.setSwapData("LastFcMonth", list.getLastFcMonth());
                param.setSwapData("ReceivedDate", list.getFcDate());
                param.setSwapData("remark", list.getRemark());
                param.setSwapData("BusinessPattern", list.getBusinessPattern());

                param.setSwapData("createdDate", systemTime);
                param.setSwapData("updatedDate", systemTime);

                Map<String, TnmPartsMaster> partsMap = list.getPartsMap();
                Map<String, List<TntCfcMonth>> cfcMonthMap = list.getCfcMonthMap();
                Map<String, List<TntCfcDay>> cfcDayMap = list.getCfcDayMap();
                String updateByFlg = list.getUploadType();
                param.setSwapData("CustomerCodeLst", customerCodelist);
                param.setSwapData("StartFcDate", list.getStartFcDate());
                param.setSwapData("EndFcDate", list.getEndFcDate());
                LinkedHashMap<String, List<CPOCFF11Entity>> codeCalendarMap = getDiffCodeCalendar(param);
                LinkedHashMap<String, Object> customerworkingDayMap = new LinkedHashMap<String, Object>();

                for (Map.Entry<String, TnmPartsMaster> mapEnty : partsMap.entrySet()) {
                    TnmPartsMaster entity = mapEnty.getValue();
                    String index = mapEnty.getKey();

                    if (entity.getInactiveFlag() == null) {

                        if ((Boolean) insertMasterMap.get(entity.getCustomerCode())) {

                            param.setSwapData("CustomerCode", entity.getCustomerCode());
                            List<DBTEMPEntity> tempList = getOldReceivedDateData(param);
                            int verNo = 1;
                            if (tempList.size() == 0) {
                                verNo = 1;
                            } else {
                                DBTEMPEntity tmpEntity = tempList.get(0);
                                verNo = Integer
                                    .parseInt(tmpEntity.getcFCNO().substring(tmpEntity.getcFCNO().length() - IntDef.INT_THREE));
                                if (tmpEntity.getFcDate().compareTo(list.getFcDate()) != 0) {
                                    verNo++;
                                }
                            }
                            String version = StringUtil.PadLeft(String.valueOf(verNo), IntDef.INT_THREE, "0");
                            StringBuffer bfCfcNo = new StringBuffer();
                            bfCfcNo.append(entity.getCustomerCode()).append(StringConst.UNDERLINE);
                            bfCfcNo.append(list.getFirstFcMonth()).append(StringConst.MIDDLE_LINE);
                            bfCfcNo.append(list.getLastFcMonth()).append(StringConst.UNDERLINE);
                            bfCfcNo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, list.getFcDate())).append(StringConst.UNDERLINE);
                            bfCfcNo.append(version);
                            // String cfcNo = list.getForecastNoMap().get(entity.getCustomerCode());
                            String cfcNo = bfCfcNo.toString();
                            
                            List<DBTEMPEntity> dbTempList = this.baseMapper.select(this.getSqlId("findOldData"), param);
                            if (dbTempList.size() != 0) {
                                Integer cfcId = dbTempList.get(0).getcFCID();
                                param.setSwapData("CFCID", cfcId);
                                // delete old data (DAY,MONTH,MASTER)
                                this.baseMapper.delete(this.getSqlId("deleteOldDataDay"), param);
                                this.baseMapper.delete(this.getSqlId("deleteOldDataMonth"), param);
                                this.baseMapper.delete(this.getSqlId("deleteOldDataMaster"), param);
                            }
                            // insert new data
                            // insert into TNT_CFC_MASTER
                            Integer seqCfcId = getNextSequence("SEQ_TNT_CFC_MASTER");
                            param.setSwapData("CFCNO", cfcNo);
                            param.setSwapData("CFCID", seqCfcId);
                            param.setSwapData("UploadDate", officeTime);
                            this.baseMapper.insert(this.getSqlId("insertIntoMaster"), param);
                            insertMasterMap.put(entity.getCustomerCode(), false);
                        }

                        // by day
                        if (updateByFlg.equals(UPLOAD_TYPE_BYDAY)) {

                            List<TntCfcDay> cfcDayLst = cfcDayMap.get(index);
                            LinkedHashMap<String, BigDecimal> dateMapByYM = new LinkedHashMap<String, BigDecimal>();
                            String monthYm = "";
                            BigDecimal zero = new BigDecimal(IntDef.INT_ZERO);
                            for (TntCfcDay cfcDayEntity : cfcDayLst) {

                                monthYm = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                                    cfcDayEntity.getCfcDate());

                                if (dateMapByYM.containsKey(monthYm)) {
                                    dateMapByYM.put(
                                        monthYm,
                                        dateMapByYM.get(monthYm).add(
                                            cfcDayEntity.getCfcQty() == null ? zero : cfcDayEntity.getCfcQty()));
                                } else {
                                    dateMapByYM.put(monthYm,
                                        cfcDayEntity.getCfcQty() == null ? zero : cfcDayEntity.getCfcQty());
                                }
                            }

                            param.setSwapData("partsId", entity.getPartsId());

                            for (Map.Entry<String, BigDecimal> ymEntry : dateMapByYM.entrySet()) {
                                Integer seqCfcMonthId = getNextSequence("SEQ_TNT_CFC_MONTH");
                                param.setSwapData("month", ymEntry.getKey());
                                param.setSwapData("qty", ymEntry.getValue());
                                param.setSwapData("CFCMONTHID", seqCfcMonthId);
                                // insert into TNT_CFC_MONTH
                                this.baseMapper.insert(this.getSqlId("insertIntoMonth"), param);
                                Date firstDay = DateTimeUtil.firstDay(DateTimeUtil.parseMonth(ymEntry.getKey()));
                                Date lastDay = DateTimeUtil.lastDay(DateTimeUtil.parseMonth(ymEntry.getKey()));
                                /*for (TntCfcDay cfcDayEntity : cfcDayLst) {
                                    if (firstDay.getTime() <= cfcDayEntity.getCfcDate().getTime()
                                            && lastDay.getTime() >= cfcDayEntity.getCfcDate().getTime()) {
                                        param.setSwapData("day", cfcDayEntity.getCfcDate());
                                        param.setSwapData("qty",
                                            cfcDayEntity.getCfcQty() == null ? zero : cfcDayEntity.getCfcQty());
                                        // insert into TNT_CFC_DAY
                                        this.baseMapper.insert(this.getSqlId("insertIntoDay"), param);
                                    }
                                }*/
                                List<TntCfcDay> cfcDayList = new ArrayList<TntCfcDay>();
                                for (TntCfcDay cfcDayEntity : cfcDayLst) {
                                    if (firstDay.getTime() <= cfcDayEntity.getCfcDate().getTime()
                                            && lastDay.getTime() >= cfcDayEntity.getCfcDate().getTime()) {
                                        TntCfcDay cfcDay = new TntCfcDay();
                                        cfcDay.setCfcDate(cfcDayEntity.getCfcDate());
                                        cfcDay.setCfcQty(cfcDayEntity.getCfcQty() == null ? zero : cfcDayEntity.getCfcQty());
                                        cfcDayList.add(cfcDay);
                                    }
                                }
                                param.setSwapData("cfcDayList", cfcDayList);
                                // insert into TNT_CFC_DAY
                                this.baseMapper.insert(this.getSqlId("insertIntoDay"), param);
                                
                                
                            }

                        } else {
                            // by month
                            List<TntCfcMonth> cfcMonthLst = cfcMonthMap.get(index);

                            List<CPOCFF11Entity> calendarList = codeCalendarMap.get(entity.getCustomerCode());
                            for (TntCfcMonth monthEntry : cfcMonthLst) {

                                Date firstDate = DateTimeUtil
                                    .firstDay(DateTimeUtil.parseMonth(monthEntry.getCfcMonth()));
                                Date lastDate = DateTimeUtil.lastDay(DateTimeUtil.parseMonth(monthEntry.getCfcMonth()));
                                LinkedHashMap<Date, Integer> workingDayMap = new LinkedHashMap<Date, Integer>();
                                LinkedHashMap<Date, Integer> allDayMap = new LinkedHashMap<Date, Integer>();
                                for (CPOCFF11Entity listEntity : calendarList) {
                                    if (listEntity.getCalendarDate().getTime() >= firstDate.getTime()
                                            && listEntity.getCalendarDate().getTime() <= lastDate.getTime()) {
                                        if (listEntity.getWorkingFlag().equals(1)) {
                                            workingDayMap
                                                .put(listEntity.getCalendarDate(), listEntity.getWorkingFlag());
                                        }
                                        allDayMap.put(listEntity.getCalendarDate(), listEntity.getWorkingFlag());
                                    }
                                }
                                customerworkingDayMap.put(monthEntry.getCfcMonth(), workingDayMap);

                                String originalFormat = "";
                                param.setSwapData("partsId", entity.getPartsId());
                                if (monthEntry.getCfcMonth().length() > IntDef.INT_SEVEN) {
                                    originalFormat = "MMM yyyy";
                                } else {
                                    originalFormat = "yyyy-MM";
                                }

                                if (monthEntry.getCfcQty() != null) {
                                    // set by month
                                    Integer seqCfcMonthId = getNextSequence("SEQ_TNT_CFC_MONTH");
                                    param.setSwapData("month", DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                                        originalFormat, monthEntry.getCfcMonth()));
                                    param.setSwapData("qty", monthEntry.getCfcQty());
                                    param.setSwapData("CFCMONTHID", seqCfcMonthId);
                                    // insert into TNT_CFC_MONTH
                                    this.baseMapper.insert(this.getSqlId("insertIntoMonth"), param);
                                    // set by day
                                    List<DBTEMPEntity> allocationTypeList = this.baseMapper.select(
                                        this.getSqlId("getallocationType"), param);

                                    String allocationType = allocationTypeList.get(0).getAllocationFcType();
                                    LinkedHashMap<Date, BigDecimal> dataMapByDay = calcCFbyAllocationType(
                                        (LinkedHashMap<Date, Integer>) allDayMap,
                                        (LinkedHashMap<Date, Integer>) customerworkingDayMap.get(monthEntry
                                            .getCfcMonth()), allocationType, monthEntry.getCfcQty(),
                                        monthEntry.getCfcMonth(), entity.getSpq());
                                    
                                    /*for (Map.Entry<Date, BigDecimal> dayEntry : dataMapByDay.entrySet()) {

                                        param.setSwapData("day", dayEntry.getKey());
                                        param.setSwapData("qty", dayEntry.getValue());
                                        // insert into TNT_CFC_DAY
                                        this.baseMapper.insert(this.getSqlId("insertIntoDay"), param);
                                    }*/
                                    List<TntCfcDay> cfcDayList = new ArrayList<TntCfcDay>();
                                    for (Map.Entry<Date, BigDecimal> dayEntry : dataMapByDay.entrySet()) {
                                        TntCfcDay cfcDayEntity = new TntCfcDay();
                                        cfcDayEntity.setCfcDate(dayEntry.getKey());
                                        cfcDayEntity.setCfcQty(dayEntry.getValue());
                                        cfcDayList.add(cfcDayEntity);
                                    }
                                    param.setSwapData("cfcDayList", cfcDayList);
                                    // insert into TNT_CFC_DAY
                                    this.baseMapper.insert(this.getSqlId("insertIntoDay"), param);
                                    
                                    
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * divide monthly Qty for every allocation day.
     * 
     * @param allDayMap LinkedHashMap
     * @param workingDaysMap LinkedHashMap
     * @param allocationType String
     * @param monthQty BigDecimal
     * @param cfcMonth String
     * @param spq BigDecimal
     * @return dataMap LinkedHashMap
     */
    private LinkedHashMap<Date, BigDecimal> calcCFbyAllocationType(LinkedHashMap<Date, Integer> allDayMap,
        LinkedHashMap<Date, Integer> workingDaysMap, String allocationType, BigDecimal monthQty, String cfcMonth,
        BigDecimal spq) {
        LinkedHashMap<Date, BigDecimal> dataMap = new LinkedHashMap<Date, BigDecimal>();
        BigDecimal zero = new BigDecimal(IntDef.INT_ZERO);
        if (StringUtil.isEmpty(allocationType)) {
            allocationType = "D";
        }
        List<Date> temp = new ArrayList<Date>();
        for (Map.Entry<Date, Integer> allDayMapEntry : allDayMap.entrySet()) {
            temp.add(allDayMapEntry.getKey());
        }
        for (int i = 0; i <= temp.size() - 1; i++) {
            dataMap.put(temp.get(i), zero);
        }

        // create the allocation List.
        List<Date> allocationLst = createAllocationList(workingDaysMap, allocationType, cfcMonth);

        // calculate the allocation Qty
        // base on given allocation type and perform heijunka distribution.If heijyunka logic results in small number,
        // it will use Roundup,
        // and the number of excess is beginning to decrease from the last forecast day.
        // case the forecsst day is a nonworking day, skip and find the next / previous(case by end of month) working
        // day as the forecast day.
        // and if the found forecast day was a forecast day, stop and do not find the next one.

        // ex. monthQty = 101, allocableDays = 22, then the allocation calculation is that
        // aveAllocationQty: roundup(101/22) = 5
        // calcTotalQty: 5*22 = 110
        // diffQty: 110-101 = 9
        // reallocateDays: roundup(9/5) = 2
        // reallocateQty = 5 - (9-5*(2-1)) = 1
        // so the allocate result is that: 5*20 + 1 + 0
        int allocableDays = allocationLst.size();
        BigDecimal numOfBoxes = DecimalUtil.divide(monthQty, spq, 0, RoundingMode.UP);
        BigDecimal aveAllocationBoxes = DecimalUtil.divide(numOfBoxes, new BigDecimal(allocableDays), 0,
            RoundingMode.UP);

        BigDecimal day = DecimalUtil.divide(monthQty, aveAllocationBoxes.multiply(spq), 0, RoundingMode.DOWN);
        BigDecimal reallocateQty = monthQty.subtract((new BigDecimal(day.intValue()).multiply(spq))
            .multiply(aveAllocationBoxes));

        // set the allocate Qty to the allocationLst.
        for (int i = 0; i < allocableDays; i++) {
            if (i < day.intValue()) {
                dataMap.put(allocationLst.get(i), aveAllocationBoxes.multiply(spq));
            } else if (i == day.intValue()) {
                dataMap.put(allocationLst.get(i), reallocateQty);
            } else {
                dataMap.put(allocationLst.get(i), zero);
            }
        }
        return dataMap;
    }

    /**
     * create the allocationDay List.
     * 
     * @param workingDaysMap working days
     * @param allocationType allocation type
     * @param cfcMonth forecast month
     * @return allocationDay list
     */
    private List<Date> createAllocationList(LinkedHashMap<Date, Integer> workingDaysMap, String allocationType,
        String cfcMonth) {

        List<Date> allocationLst = new ArrayList<Date>();

        // {D}
        Pattern patternDaily = Pattern.compile("^D$");
        Matcher matcherDaily = patternDaily.matcher(allocationType);

        // {D1},{D1,D2}
        Pattern patternFixedDay = Pattern
            .compile("(^D([1-9]\\b|[1-2][0-9]\\b|3[0-1]\\b)(,\\s{0,1}D([1-9]\\b|[1-2][0-9]\\b|3[0-1]\\b)){0,30})$");
        Matcher matcherFixedDay = patternFixedDay.matcher(allocationType);

        // {W1},{W1,W2}
        Pattern patternWeekly = Pattern.compile("(^W[1-5](,\\s{0,1}W[1-5]){0,4})$");
        Matcher matcherWeekly = patternWeekly.matcher(allocationType);

        // {W1:Mon},{W1:Mon,W2:Tue}
        Pattern patternWD = Pattern
            .compile("^W[1-5]:\\s{0,1}(Mon|Tue|Wed|Thu|Fri|Sat|Sun)(,\\s{0,1}W[1-5]:\\s{0,1}(Mon|Tue|Wed|Thu|Fri|Sat|Sun)){0,30}$");
        Matcher matcherWD = patternWD.matcher(allocationType);

        // {Mon},{Mon,Tue}
        Pattern patternEvetyWD = Pattern
            .compile("^(Mon|Tue|Wed|Thu|Fri|Sat|Sun)(,\\s{0,1}(Mon|Tue|Wed|Thu|Fri|Sat|Sun)){0,6}$");
        Matcher matcherEvetyWD = patternEvetyWD.matcher(allocationType);

        // set temp list
        List<Date> temp = new ArrayList<Date>();
        for (Map.Entry<Date, Integer> workingDaysMapEntry : workingDaysMap.entrySet()) {
            temp.add(workingDaysMapEntry.getKey());
        }
        Map<Integer, Date> dateMap = new LinkedHashMap<Integer, Date>();
        for (int i = 0; i < temp.size(); i++) {
            dateMap.put(DateTimeUtil.getDay(temp.get(i)), temp.get(i));
        }

        Map<Integer, Date> invertedDateMap = new LinkedHashMap<Integer, Date>();
        for (int i = temp.size() - 1; i >= 0; i--) {
            invertedDateMap.put(DateTimeUtil.getDay(temp.get(i)), temp.get(i));
        }

        // create the allocation day list.
        if (matcherDaily.matches()) {
            // {D}
            // copy workingDaysMap to allocationDaysLst.
            allocationLst.addAll(temp);

        } else if (matcherFixedDay.matches()) {
            // {D1},{D1,D2}
            // set allocationDaysLst by workingDaysLst
            String[] allocationTypes = allocationType.split(",");
            if (null != allocationTypes && allocationTypes.length > 0) {
                for (int i = 0; i < allocationTypes.length; i++) {

                    // start with 0
                    int workingDayNo = Integer.valueOf(StringUtil.trim(allocationTypes[i].replace("D", ""))) - 1;
                    if (workingDayNo < temp.size()) {
                        allocationLst.add(temp.get(workingDayNo));
                    }
                }
            }
        } else if (matcherWeekly.matches()) {
            // {W1},{W1,W2}
            // set allocationDaysLst by workingDaysLst.
            String[] allocationTypes = allocationType.split(",");
            if (null != allocationTypes && allocationTypes.length > 0) {
                for (int i = 0; i < allocationTypes.length; i++) {
                    // case W1,1-7; case W2,8-14;...
                    setAllocationLst(cfcMonth, invertedDateMap, dateMap, allocationLst, StringUtil.trim(allocationTypes[i]), "ALL");
                }
            }
        } else if (matcherWD.matches()) {
            // {W1:Mon},{W1:Mon,W2:Tue}
            // set allocationDaysLst by workingDaysLst.
            String[] allocationTypes = allocationType.split(",");
            if (null != allocationTypes && allocationTypes.length > 0) {
                for (int i = 0; i < allocationTypes.length; i++) {

                    String[] allocationDetail = allocationTypes[i].split(":");
                    if (null != allocationDetail && allocationDetail.length == IntDef.INT_TWO) {
                        // allocationDetail[0]:W1,allocationDetail[1]:Mon
                        setAllocationLst(cfcMonth, invertedDateMap, dateMap, allocationLst, StringUtil.trim(allocationDetail[0]),
                            StringUtil.trim(allocationDetail[1]));
                    }
                }
            }
        } else if (matcherEvetyWD.matches()) {
            // {Mon},{Mon,Tue}
            // set allocationDaysLst by workingDaysLst.
            String[] allocationTypes = allocationType.split(",");
            if (null != allocationTypes && allocationTypes.length > 0) {
                for (int i = 0; i < allocationTypes.length; i++) {

                    for (int j = 1; j <= 5; j++) {
                        setAllocationLst(cfcMonth, invertedDateMap, dateMap, allocationLst, "W" + j, StringUtil.trim(allocationTypes[i]));
                    }
                }
            }
        }

        Collections.sort(allocationLst, new Comparator<Date>() {
            @Override
            public int compare(Date obj1, Date obj2) {

                int result = DateTimeUtil.getDay(obj1) - (DateTimeUtil.getDay(obj2));
                return result;
            }
        });
        return allocationLst;
    }

    /**
     * set allocation list.
     * 
     * @param cfcMonth forecast month
     * @param invertedDateMap working day list
     * @param dateMap working day list
     * @param allocationLst allocation list
     * @param weekNum week No.(W1,W2,W3,W4,W5)
     * @param week week(Mon,Tue,Wed,Thu,Fri,Sat,Sun)
     */
    private void setAllocationLst(String cfcMonth, Map<Integer, Date> invertedDateMap, Map<Integer, Date> dateMap,
        List<Date> allocationLst, String weekNum, String week) {

        int start = 0;
        int end = 0;
        int j = Integer.valueOf(StringUtil.trim(weekNum.replace("W", "")));
        switch (j) {
            case (IntDef.INT_ONE):
                start = IntDef.INT_ONE;
                end = IntDef.INT_SEVEN;
                break;
            case (IntDef.INT_TWO):
                start = IntDef.INT_EIGHT;
                end = IntDef.INT_FOURTEEN;
                break;
            case (IntDef.INT_THREE):
                start = IntDef.INT_FIFTEEN;
                end = IntDef.INT_TWENTY_ONE;
                break;
            case (IntDef.INT_FOUR):
                start = IntDef.INT_TWENTY_TWO;
                end = IntDef.INT_TWENTY_EIGHT;
                break;
            case (IntDef.INT_FIVE):
                start = IntDef.INT_TWENTY_NINE;
                end = DateTimeUtil.getDay(DateTimeUtil.lastDay(DateTimeUtil.parseMonth(cfcMonth)));
                break;
        }

        boolean isMatched = false;
        for (Map.Entry<Integer, Date> entity : dateMap.entrySet()) {
            if (entity.getKey() >= start && entity.getKey() <= end) {
                if (week.equals(DateTimeUtil.getWeek(entity.getValue())) || "ALL".equals(week)) {
                    if (!allocationLst.contains(entity.getValue())) {
                        allocationLst.add(entity.getValue());
                    }
                    isMatched = true;
                }
            }
        }
        // find the first next working day
        if (!isMatched) {
            int unMatchedDay = end;
            Integer dayByWeek = DateTimeUtil.getDayByWeek(cfcMonth, weekNum, week);
            if (null != dayByWeek && !"ALL".equals(week)) {
                unMatchedDay = dayByWeek.intValue();
            }
            for (Map.Entry<Integer, Date> entity : dateMap.entrySet()) {
                if (!isMatched && entity.getKey() > unMatchedDay) {
                    if (!allocationLst.contains(entity.getValue())) {
                        allocationLst.add(entity.getValue());
                    }
                    isMatched = true;
                }
            }
            if ("ALL".equals(week)) {
                unMatchedDay = start;
            } else if (null == dayByWeek) {
                unMatchedDay = IntDef.INT_ONE;
            }
            if (!isMatched) {
                for (Map.Entry<Integer, Date> entity : invertedDateMap.entrySet()) {
                    if (!isMatched && entity.getKey() < unMatchedDay) {
                        if (!allocationLst.contains(entity.getValue())) {
                            allocationLst.add(entity.getValue());
                        }
                        isMatched = true;
                    }
                }
            }
            if (!isMatched) {
                // there is no matched working day in a whole month, show err?
            }
        }
    }
}