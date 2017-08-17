/**
 * CfcRundownService.java
 * 
 * @screen CPOCSF05
 * @author li_feng
 */
package com.chinaplus.common.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.CPOCFFComDailyEntity;
import com.chinaplus.common.entity.CPOCFFComEntity;
import com.chinaplus.common.entity.CPOCFFRunDownEntity;
import com.chinaplus.common.entity.CPOCFFTEMPEntity;
import com.chinaplus.common.entity.TnmPartsMaster;
import com.chinaplus.common.entity.TntRundownCfc;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;

/**
 * Latest Customer Forecast In Rundown
 */
@Service
public class CfcRundownService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CfcRundownService.class);

    /** cfcAdjustmentType1 */
    private static final String CFC_ADJUSTMENT_TYPE1 = "Type1";
    /** cfcAdjustmentType2 */
    private static final String CFC_ADJUSTMENT_TYPE2 = "Type2";
    /** select from */
    private static final String BATCH = "BATCH";

    /**
     * check CfcMonth
     * 
     * @param param BaseParam
     * @return CPOCFFComEntity
     */
    public List<CPOCFFComEntity> checkCfcMonth(BaseParam param) {
        List<CPOCFFComEntity> tempList = this.baseMapper.select(this.getSqlId("getPartsInfo"), param);
        return tempList;
    }
    
    /**
     * Do daily cfc rundown
     *    
     * @param officeId Integer
     * @param officeDate Date
     */
    public void doDailyCfcRundown(Integer officeId, Date officeDate) {
        List<CPOCFFComEntity> list = new ArrayList<CPOCFFComEntity>();
        LinkedHashMap<Integer, TnmPartsMaster> partsMasterMap =  new LinkedHashMap<Integer, TnmPartsMaster>();
        LinkedHashMap<Integer, List<CPOCFFComDailyEntity>> partsDailyMap =  new LinkedHashMap<Integer, List<CPOCFFComDailyEntity>>();
        BaseParam params = new BaseParam();
        
        if(officeId != null && officeDate != null){
            params.setSwapData("SelectFrom", BATCH);
            params.setSwapData("officeId", officeId);
            params.setSwapData("cfcMonth", DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, officeDate));
            params.setSwapData("startDate", DateTimeUtil.addMonth(DateTimeUtil.firstDay(officeDate), IntDef.INT_N_ONE));
            params.setSwapData("endDate", officeDate);
        }
        logger.info("Start to get rundown parts information.");
        list = this.baseMapper.select(this.getSqlId("getRundownPartsInfo"), params);
        for(CPOCFFComEntity entity : list){
            // made partsMasterMap
            if(!partsMasterMap.containsKey(entity.getPartsId())){
                TnmPartsMaster partsMaster = new TnmPartsMaster();
                partsMaster.setPartsId(entity.getPartsId());
                partsMaster.setTtcPartsNo(entity.getTtcPartsNo());
                partsMaster.setCustomerCode(entity.getCustomerCode());
                partsMaster.setCustPartsNo(entity.getCustPartsNo());
                partsMaster.setPartsType(entity.getPartsType());
                partsMaster.setCarModel(entity.getCarModel());
                partsMaster.setOrderLot(entity.getOrderLot());
                partsMaster.setOldTtcPartsNo(entity.getOldTtcPartsNo());
                partsMaster.setPartsNameEn(entity.getPartsNameEn());
                partsMaster.setPartsNameCn(entity.getPartsNameCn());
                partsMaster.setCfcAdjustmentType1(entity.getCfcAdjustmentType1());
                partsMaster.setCfcAdjustmentType2(entity.getCfcAdjustmentType2());
                partsMaster.setSpq(entity.getSpq());
                
                partsMasterMap.put(entity.getPartsId(), partsMaster);
            }
            // made partsDailyMap
            if(partsDailyMap.containsKey(entity.getPartsId())){
                List<CPOCFFComDailyEntity> partsDailyEntityList = partsDailyMap.get(entity.getPartsId());
                CPOCFFComDailyEntity partsDailyEntity = new CPOCFFComDailyEntity();
                partsDailyEntity.setCfcMonthId(entity.getCfcMonthId());
                partsDailyEntity.setPartsId(entity.getPartsId());
                partsDailyEntity.setCustomerId(entity.getCustomerId());
                partsDailyEntity.setCfcDate(entity.getCfcDate());
                partsDailyEntity.setWorkingFlag(entity.getWorkingFlg());
                partsDailyEntity.setCfcQty(entity.getCfcQty());
                partsDailyEntity.setCfcMonth(entity.getCfcMonth());
                partsDailyEntity.setFcDate(entity.getFcDate());
                partsDailyEntity.setCfcMonthQty(entity.getCfcMonthQty());
                
                partsDailyEntityList.add(partsDailyEntity);
                partsDailyMap.put(entity.getPartsId(), partsDailyEntityList);
            } else {
                List<CPOCFFComDailyEntity> partsDailyEntityList = new ArrayList<CPOCFFComDailyEntity>();
                CPOCFFComDailyEntity partsDailyEntity = new CPOCFFComDailyEntity();
                partsDailyEntity.setCfcMonthId(entity.getCfcMonthId());
                partsDailyEntity.setPartsId(entity.getPartsId());
                partsDailyEntity.setCustomerId(entity.getCustomerId());
                partsDailyEntity.setCfcDate(entity.getCfcDate());
                partsDailyEntity.setWorkingFlag(entity.getWorkingFlg());
                partsDailyEntity.setCfcQty(entity.getCfcQty());
                partsDailyEntity.setCfcMonth(entity.getCfcMonth());
                partsDailyEntity.setFcDate(entity.getFcDate());
                partsDailyEntity.setCfcMonthQty(entity.getCfcMonthQty());
                
                partsDailyEntityList.add(partsDailyEntity);
                partsDailyMap.put(entity.getPartsId(), partsDailyEntityList);
            }   
        }
        
        // made actualQtyMap
        logger.info("Start to get actual qty for batch.");
        LinkedHashMap<Integer, List<CPOCFFComDailyEntity>> actualListMap = new LinkedHashMap<Integer, List<CPOCFFComDailyEntity>>();
        List<CPOCFFComDailyEntity> actualListAll = this.baseMapper.select(this.getSqlId("getActualQtyBatch"),
            params);
        for(CPOCFFComDailyEntity tempActualEntity: actualListAll) {
            if(actualListMap.containsKey(tempActualEntity.getPartsId())){
                actualListMap.get(tempActualEntity.getPartsId()).add(tempActualEntity);     
            }else{
                List<CPOCFFComDailyEntity> tempActualList = new ArrayList<CPOCFFComDailyEntity>();
                tempActualList.add(tempActualEntity);
                actualListMap.put(tempActualEntity.getPartsId(), tempActualList);
            }
        }
        
        // loop parts 
        if(partsMasterMap.size() > 0){
            logger.info("Start to process each cfc by each parts.");
            int count = 1;
            int size = partsMasterMap.size();
            for (Map.Entry<Integer, TnmPartsMaster> entry : partsMasterMap.entrySet()) {
                //get partsMaster
                TnmPartsMaster partsMaster = entry.getValue();
                Integer partsId = entry.getKey();
                //get partsDailyInfo
                List<CPOCFFComDailyEntity> partsDailyEntityList = partsDailyMap.get(partsId);
                //get actualQty
                Date startDate = DateTimeUtil.parseDate(DateTimeUtil.formatDate(
                    DateTimeUtil.FORMAT_DATE_YYYYMMDD, DateTimeUtil.firstDay(officeDate)));
                Date endDate = officeDate;
                List<CPOCFFComDailyEntity> actualList = new ArrayList<CPOCFFComDailyEntity>();
                List<CPOCFFComDailyEntity> tempActualList = actualListMap.get(partsId);
                if (tempActualList != null) {
                    for (CPOCFFComDailyEntity entity : tempActualList) {
                        if (entity.getEndDate() == null) {
                            actualList.add(entity);
                        } else if (entity.getEndDate().getTime() >= startDate.getTime()
                                && entity.getEndDate().getTime() <= endDate.getTime()) {
                            actualList.add(entity);
                        }
                    }
                }
                doRunDownBatch(partsMaster, partsDailyEntityList, actualList, officeDate, params); 
                logger.info("already process: " + (count++) + "/" + size);
            }
            
        }
    }
    
    /**
     * made CfcRundown & set to DB(TNT_RUNDOWN_CFC)
     * 
     * @param partsMasterEntity TnmPartsMaster
     * @param partsDailyEntityList List<CPOCFFComDailyEntity>
     * @param actualList List<CPOCFFComDailyEntity>
     * @param officeTime Date
     * @param param BaseParam
     * 
     */
    public void doRunDownBatch(TnmPartsMaster partsMasterEntity, List<CPOCFFComDailyEntity> partsDailyEntityList,
        List<CPOCFFComDailyEntity> actualList, Date officeTime, BaseParam param) {
        List<TntRundownCfc> tntRundownCfcList = new ArrayList<TntRundownCfc>();
        Timestamp officeTimeNow = super.getDBDateTimeByDefaultTimezone();
        BigDecimal spq = partsMasterEntity.getSpq();
        Integer partsId = partsMasterEntity.getPartsId();
        BigDecimal cfcQtySum = new BigDecimal(0);
        BigDecimal actualQtySum = new BigDecimal(0);
        
        // get daily cfc qty
        for (CPOCFFComDailyEntity dailyEntity : partsDailyEntityList) {
            if (dailyEntity.getPartsId().equals(partsId)
                    && dailyEntity.getCfcDate().getTime() < DateTimeUtil.parseDate(
                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()) {
                cfcQtySum = cfcQtySum.add(dailyEntity.getCfcQty());
            }
        }

        // get actual qty
        for (CPOCFFComDailyEntity actualEntity : actualList) {
            if (actualEntity.getPartsId().equals(partsId)
                    && actualEntity.getEndDate() != null
                    && actualEntity.getEndDate().getTime() < DateTimeUtil.parseDate(
                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()) {
                actualQtySum = actualQtySum.add(actualEntity.getActualQty());
            }
        }
        
        // calculation
        if (!DecimalUtil.isGreater(actualQtySum, cfcQtySum)) {
            Integer calculationFlg = partsMasterEntity.getCfcAdjustmentType1();
            String calculationType = CFC_ADJUSTMENT_TYPE1;
            BigDecimal difference = cfcQtySum.subtract(actualQtySum);
            tntRundownCfcList = calculation(difference, partsId, spq, partsDailyEntityList, actualList, officeTime,
                calculationFlg, calculationType);
        } else {
            Integer calculationFlg = partsMasterEntity.getCfcAdjustmentType2();
            String calculationType = CFC_ADJUSTMENT_TYPE2;
            BigDecimal difference = actualQtySum.subtract(cfcQtySum);
            tntRundownCfcList = calculation(difference, partsId, spq, partsDailyEntityList, actualList, officeTime,
                calculationFlg, calculationType);
        }
        
        // insert or update TNTRUNDOWNCFC data
        TntRundownCfc tntRundownCfcEntity = new TntRundownCfc();
        tntRundownCfcEntity.setPartsId(partsId);
        tntRundownCfcEntity.setCfcDate(DateTimeUtil.parseDate(DateTimeUtil.formatDate(
            DateTimeUtil.FORMAT_DATE_YYYYMMDD, DateTimeUtil.firstDay(officeTime))));
        List<TntRundownCfc> runDownCfcDatas = super.baseDao.select(tntRundownCfcEntity);

        if (runDownCfcDatas != null && runDownCfcDatas.size() > 0) {
            logger.info("Do update: ");
            for (TntRundownCfc tntRundownCfcentity : tntRundownCfcList) {
                tntRundownCfcentity.setUpdatedBy(1);
                tntRundownCfcentity.setUpdatedDate(officeTimeNow);
                // updateOldStatus
                this.baseMapper.update(this.getSqlId("UpdateRunDownCfc"), tntRundownCfcentity);
            }
        } else {
            logger.info("Do Insert: ");
            for (TntRundownCfc tntRundownCfcentity : tntRundownCfcList) {
                tntRundownCfcentity.setRundownCfcId(getNextSequence("SEQ_TNT_RUNDOWN_CFC"));
                tntRundownCfcentity.setCreatedBy(1);
                tntRundownCfcentity.setUpdatedBy(1);
                tntRundownCfcentity.setCreatedDate(officeTimeNow);
                tntRundownCfcentity.setUpdatedDate(officeTimeNow);
                // InsertData
                this.baseMapper.insert(this.getSqlId("InsertRunDownCfc"), tntRundownCfcentity);
            }
        }
    }

    /**
     * Get Parts Info
     * 
     * @param param BaseParam
     * @return CPOCFFComEntitylist
     */
    public List<CPOCFFComEntity> doPreparePartsInfo(BaseParam param) {

        List<CPOCFFComEntity> list = new ArrayList<CPOCFFComEntity>();
        
        // get parts info
        List<CPOCFFComEntity> tempList = this.baseMapper.select(this.getSqlId("getPartsInfo"), param);

        // get month id
        Date time = getDBDateTime(param.getOfficeTimezone());
        List<Integer> cfcMonthList = getMonthId(tempList,
            DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, time));
        // get dailyList
        param.setSwapData("cfcMonthList", cfcMonthList);
        List<CPOCFFComDailyEntity> dailyList = this.baseMapper.select(this.getSqlId("getDailyInfo"), param);
        // get actual qty
        CPOCFFTEMPEntity temp = new CPOCFFTEMPEntity();
        temp.setStartDate(DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
            DateTimeUtil.firstDay(time))));
        temp.setEndDate(time);
        param.setSwapData("dbtempEntity", temp);
        List<CPOCFFComDailyEntity> actualList = this.baseMapper.select(this.getSqlId("getActualQty"), param);

        LinkedHashMap<Integer, CPOCFFComEntity> entityMap = new LinkedHashMap<Integer, CPOCFFComEntity>();
        LinkedHashMap<Integer, List<CPOCFFTEMPEntity>> listMap = new LinkedHashMap<Integer, List<CPOCFFTEMPEntity>>();
        List<CPOCFFTEMPEntity> comList = new ArrayList<CPOCFFTEMPEntity>();

        for (CPOCFFComEntity entity : tempList) {

            CPOCFFComEntity cpocffComEntity = new CPOCFFComEntity();
            CPOCFFTEMPEntity tempEntity = new CPOCFFTEMPEntity();

            if (!listMap.containsKey(entity.getPartsId())) {
                comList = new ArrayList<CPOCFFTEMPEntity>();
            }

            cpocffComEntity.setPartsId(entity.getPartsId());
            cpocffComEntity.setTtcPartsNo(entity.getTtcPartsNo());
            cpocffComEntity.setCustomerCode(entity.getCustomerCode());
            cpocffComEntity.setCustPartsNo(entity.getCustPartsNo());
            cpocffComEntity.setPartsType(entity.getPartsType());
            cpocffComEntity.setCarModel(entity.getCarModel());
            cpocffComEntity.setOrderLot(entity.getOrderLot());
            cpocffComEntity.setOldTtcPartsNo(entity.getOldTtcPartsNo());
            cpocffComEntity.setPartsNameEn(entity.getPartsNameEn());
            cpocffComEntity.setPartsNameCn(entity.getPartsNameCn());
            cpocffComEntity.setCfcAdjustmentType1(entity.getCfcAdjustmentType1());
            cpocffComEntity.setCfcAdjustmentType2(entity.getCfcAdjustmentType2());
            cpocffComEntity.setSpq(entity.getSpq());

            tempEntity.setCfcMonth(entity.getCfcMonth());
            tempEntity.setCfcQty(entity.getCfcQty());

            comList.add(tempEntity);
            listMap.put(cpocffComEntity.getPartsId(), comList);

            if (!entityMap.containsKey(cpocffComEntity.getPartsId())) {
                entityMap.put(cpocffComEntity.getPartsId(), cpocffComEntity);
            }
        }

        for (Map.Entry<Integer, CPOCFFComEntity> entry : entityMap.entrySet()) {
            CPOCFFComEntity comEntity = entry.getValue();
            comEntity.setCpocffTEMPEntityLst(listMap.get(comEntity.getPartsId()));
            list.add(comEntity);
        }
        doRunDown(list, dailyList, actualList, time, param);
        List<CPOCFFRunDownEntity> tntRundownCfcList = getTntRundownCfcInfo(param);

        if (tntRundownCfcList.size() == 0) {
            doRunDown(list, dailyList, actualList, time, param);
        }
        return list;
    }

    /**
     * Get MonthId
     * 
     * @param tempList List<CPOCFFTEMPEntity>
     * @param cfcMonth String
     * @return List<Integer>
     */
    public List<Integer> getMonthId(List<CPOCFFComEntity> tempList, String cfcMonth) {

        List<Integer> lst = new ArrayList<Integer>();

        for (CPOCFFComEntity entity : tempList) {
            if (entity.getCfcMonth().equals(cfcMonth)) {
                lst.add(entity.getCfcMonthId());
            }
        }
        return lst;
    }

    /**
     * made CfcRundown & set to DB(TNT_RUNDOWN_CFC)
     * 
     * @param tempList List<CPOCFFTEMPEntity>
     * @param dailyList List<CPOCFFComDailyEntity>
     * @param actualList List<CPOCFFComDailyEntity>
     * @param officeTime Date
     * @param param BaseParam
     * 
     */
    public void doRunDown(List<CPOCFFComEntity> tempList, List<CPOCFFComDailyEntity> dailyList,
        List<CPOCFFComDailyEntity> actualList, Date officeTime, BaseParam param) {
        String cfcMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, officeTime);
        List<TntRundownCfc> tntRundownCfcList = new ArrayList<TntRundownCfc>();
        /* Timestamp systemTime = super.getDBDateTimeByDefaultTimezone(); */
        Timestamp officeTimeNow = super.getDBDateTime(param.getOfficeTimezone());

        for (CPOCFFComEntity entity : tempList) {
            BigDecimal spq = entity.getSpq();

            if (entity.getCpocffTEMPEntityLst().get(0).getCfcMonth().equals(cfcMonth)) {    
                Integer partsId = entity.getPartsId();
                BigDecimal cfcQtySum = new BigDecimal(0);
                BigDecimal actualQtySum = new BigDecimal(0);

                // get daily cfc qty
                for (CPOCFFComDailyEntity dailyEntity : dailyList) {
                    if (dailyEntity.getPartsId().equals(partsId)
                            && dailyEntity.getCfcDate().getTime() < DateTimeUtil.parseDate(
                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()) {
                        cfcQtySum = cfcQtySum.add(dailyEntity.getCfcQty());
                    }
                }
                // get actual qty
                for (CPOCFFComDailyEntity actualEntity : actualList) {
                    if (actualEntity.getPartsId().equals(partsId)
                            && actualEntity.getEndDate() != null
                            && actualEntity.getEndDate().getTime() < DateTimeUtil.parseDate(
                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()) {
                        actualQtySum = actualQtySum.add(actualEntity.getActualQty());
                    }
                }

                // calculation
                if (!DecimalUtil.isGreater(actualQtySum, cfcQtySum)) {
                    Integer calculationFlg = entity.getCfcAdjustmentType1();
                    String calculationType = CFC_ADJUSTMENT_TYPE1;
                    BigDecimal difference = cfcQtySum.subtract(actualQtySum);
                    tntRundownCfcList = calculation(difference, partsId, spq, dailyList, actualList, officeTime,
                       calculationFlg, calculationType);
                } else {
                    Integer calculationFlg = entity.getCfcAdjustmentType2();
                    String calculationType = CFC_ADJUSTMENT_TYPE2;
                    BigDecimal difference = actualQtySum.subtract(cfcQtySum);
                    tntRundownCfcList = calculation(difference, partsId, spq, dailyList, actualList, officeTime,
                        calculationFlg, calculationType);
                }

                // insert or update TNTRUNDOWNCFC data
                TntRundownCfc tntRundownCfcEntity = new TntRundownCfc();
                tntRundownCfcEntity.setPartsId(partsId);
                tntRundownCfcEntity.setCfcDate(DateTimeUtil.parseDate(DateTimeUtil.formatDate(
                    DateTimeUtil.FORMAT_DATE_YYYYMMDD, DateTimeUtil.firstDay(officeTime))));
                List<TntRundownCfc> runDownCfcDatas = super.baseDao.select(tntRundownCfcEntity);
                if (runDownCfcDatas != null && runDownCfcDatas.size() > 0) {
                    for (TntRundownCfc tntRundownCfcentity : tntRundownCfcList) {
                        tntRundownCfcentity.setUpdatedBy(param.getLoginUserId());
                        tntRundownCfcentity.setUpdatedDate(officeTimeNow);
                        // updateOldStatus
                        this.baseMapper.update(this.getSqlId("UpdateRunDownCfc"), tntRundownCfcentity);
                    }
                } else {
                    for (TntRundownCfc tntRundownCfcentity : tntRundownCfcList) {
                        tntRundownCfcentity.setRundownCfcId(getNextSequence("SEQ_TNT_RUNDOWN_CFC"));
                        tntRundownCfcentity.setCreatedBy(param.getLoginUserId());
                        tntRundownCfcentity.setUpdatedBy(param.getLoginUserId());
                        tntRundownCfcentity.setCreatedDate(officeTimeNow);
                        tntRundownCfcentity.setUpdatedDate(officeTimeNow);
                        // InsertData
                        this.baseMapper.insert(this.getSqlId("InsertRunDownCfc"), tntRundownCfcentity);
                    }
                }
            }
        }
    }

    /**
     * calculation
     * 
     * @param difference BigDecimal
     * @param partsId Integer
     * @param spq BigDecimal
     * @param officeTime Date
     * @param dailyList List<CPOCFFComDailyEntity>
     * @param actualList List<CPOCFFComDailyEntity>
     * @param calculationFlg Integer
     * @param calculationType String
     * @return List<TntRundownCfc>
     * 
     */
    public List<TntRundownCfc> calculation(BigDecimal difference, Integer partsId, BigDecimal spq,
        List<CPOCFFComDailyEntity> dailyList, List<CPOCFFComDailyEntity> actualList, Date officeTime,
        Integer calculationFlg, String calculationType) {

        List<TntRundownCfc> list = new ArrayList<TntRundownCfc>();
        TntRundownCfc tntRundownCfcEntity = new TntRundownCfc();
        BigDecimal zero = new BigDecimal(0);

        // set TNT_RUNDOWN_CFC info
        for (CPOCFFComDailyEntity dailyListEntity : dailyList) {
            if (dailyListEntity.getPartsId().equals(partsId)) {
                tntRundownCfcEntity = new TntRundownCfc();
                tntRundownCfcEntity.setPartsId(partsId);
                tntRundownCfcEntity.setCfcAdjustmentType(calculationFlg);
                tntRundownCfcEntity.setCfcDate(dailyListEntity.getCfcDate());
                tntRundownCfcEntity.setOriginalQty(dailyListEntity.getCfcQty());
                tntRundownCfcEntity.setDeliveredQty(null);
                for (CPOCFFComDailyEntity actualListEntity : actualList) {
                    Date actualEndDate = actualListEntity.getEndDate();
                    if (actualListEntity.getPartsId().equals(partsId)
                            && actualEndDate != null
                            && actualEndDate.equals(dailyListEntity.getCfcDate())
                            && actualEndDate.getTime() < DateTimeUtil.parseDate(
                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()) {
                        tntRundownCfcEntity.setDeliveredQty(actualListEntity.getActualQty());
                        break;
                    }
                }

                list.add(tntRundownCfcEntity);
            }

        }

        // get date
        Integer before = 0;
        Integer after = 0;
        for (TntRundownCfc tntRundownCfc : list) {
            if (!DecimalUtil.isEquals(tntRundownCfc.getOriginalQty(), zero)
                    && (tntRundownCfc.getCfcDate().getTime() < DateTimeUtil.parseDate(
                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime())) {
                before++;
            } else if (!DecimalUtil.isEquals(tntRundownCfc.getOriginalQty(), zero)
                    && (tntRundownCfc.getCfcDate().getTime() >= DateTimeUtil.parseDate(
                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime())) {
                after++;
            }
        }

        // rundown calculation
        if (calculationFlg.equals(1)) {
            // A:Remain the original plan
            for (TntRundownCfc tntRundownCfc : list) {
                for (CPOCFFComDailyEntity dailyListEntity : dailyList) {
                    if (tntRundownCfc.getCfcDate().equals(dailyListEntity.getCfcDate())
                            && tntRundownCfc.getPartsId().equals(dailyListEntity.getPartsId())
                            && tntRundownCfc.getCfcDate().getTime() >= DateTimeUtil.parseDate(
                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()) {
                        tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty());
                    }
                }
            }

        } else if (calculationType.equals(CFC_ADJUSTMENT_TYPE1) && calculationFlg.equals(IntDef.INT_TWO)) {
            // B:put all less qty on the 1st following day
            boolean addFlg = true;
            for (TntRundownCfc tntRundownCfc : list) {
                for (CPOCFFComDailyEntity dailyListEntity : dailyList) {
                    if (tntRundownCfc.getCfcDate().equals(dailyListEntity.getCfcDate())
                            && tntRundownCfc.getPartsId().equals(dailyListEntity.getPartsId())
                            && tntRundownCfc.getCfcDate().getTime() >= DateTimeUtil.parseDate(
                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()) {

                        if (addFlg && !DecimalUtil.isEquals(dailyListEntity.getCfcQty(), zero)) {
                            tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty().add(difference));

                            addFlg = false;
                        } else {
                            tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty());
                        }
                    }
                }
            }

            // if have no Planning day after officeTime now
            if (addFlg) {
                boolean flg = true;
                for (TntRundownCfc tntRundownCfc : list) {
                    if (flg) {
                        for (CPOCFFComDailyEntity dailyListEntity : dailyList) {
                            if (tntRundownCfc.getCfcDate().equals(dailyListEntity.getCfcDate())
                                    && tntRundownCfc.getPartsId().equals(dailyListEntity.getPartsId())
                                    && tntRundownCfc.getCfcDate().getTime() >= DateTimeUtil.parseDate(
                                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()
                                    && dailyListEntity.getWorkingFlag() == 1) {

                                tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty().add(difference));
                                flg = false;
                                break;
                            }
                        }
                    }
                }
            }

        } else if (calculationType.equals(CFC_ADJUSTMENT_TYPE1) && calculationFlg.equals(IntDef.INT_THREE)) {
            // C:Put less qty equally on the following days

            BigDecimal numOfBoxes = DecimalUtil.divide(difference, spq, 0, RoundingMode.UP);
            BigDecimal aveAllocationBoxes = DecimalUtil.divide(numOfBoxes, new BigDecimal(after), 0, RoundingMode.UP);
            BigDecimal day = DecimalUtil.divide(difference, aveAllocationBoxes.multiply(spq), 0, RoundingMode.DOWN);
            BigDecimal reallocateQty = difference.subtract((new BigDecimal(day.intValue()).multiply(spq))
                .multiply(aveAllocationBoxes));

            int i = 0;
            boolean addFlg = true;
            for (TntRundownCfc tntRundownCfc : list) {
                for (CPOCFFComDailyEntity dailyListEntity : dailyList) {

                    if (tntRundownCfc.getCfcDate().equals(dailyListEntity.getCfcDate())
                            && tntRundownCfc.getPartsId().equals(dailyListEntity.getPartsId())
                            && tntRundownCfc.getCfcDate().getTime() >= DateTimeUtil.parseDate(
                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()) {

                        if (!DecimalUtil.isEquals(dailyListEntity.getCfcQty(), zero)) {

                            if (i < day.intValue()) {
                                tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty().add(
                                    aveAllocationBoxes.multiply(spq)));
                                addFlg = false;
                            } else if (i == day.intValue()) {
                                tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty().add(reallocateQty));
                                addFlg = false;
                            } else {
                                tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty().add(zero));
                                addFlg = false;
                            }

                            i++;

                        } else {
                            tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty());
                        }

                    }

                }
            }

            // if have no Planning day after officeTime now
            if (addFlg) {
                boolean flg = true;
                for (TntRundownCfc tntRundownCfc : list) {
                    if (flg) {
                        for (CPOCFFComDailyEntity dailyListEntity : dailyList) {
                            if (tntRundownCfc.getCfcDate().equals(dailyListEntity.getCfcDate())
                                    && tntRundownCfc.getPartsId().equals(dailyListEntity.getPartsId())
                                    && tntRundownCfc.getCfcDate().getTime() >= DateTimeUtil.parseDate(
                                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()
                                    && dailyListEntity.getWorkingFlag() == 1) {

                                tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty().add(reallocateQty));
                                flg = false;
                                break;
                            }
                        }
                    }
                }
            }

        } else if (calculationType.equals(CFC_ADJUSTMENT_TYPE2) && calculationFlg.equals(IntDef.INT_TWO)) {
            // B:Put excess qty equally on the following days

            BigDecimal numOfBoxes = DecimalUtil.divide(difference, spq, 0, RoundingMode.UP);
            BigDecimal aveAllocationBoxes = DecimalUtil.divide(numOfBoxes, new BigDecimal(before), 0, RoundingMode.UP);
            BigDecimal aveQty = aveAllocationBoxes.multiply(spq);

            for (TntRundownCfc tntRundownCfc : list) {
                for (CPOCFFComDailyEntity dailyListEntity : dailyList) {

                    if (tntRundownCfc.getCfcDate().equals(dailyListEntity.getCfcDate())
                            && tntRundownCfc.getPartsId().equals(dailyListEntity.getPartsId())
                            && tntRundownCfc.getCfcDate().getTime() >= DateTimeUtil.parseDate(
                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()) {

                        if (!DecimalUtil.isEquals(dailyListEntity.getCfcQty(), zero)) {
                            tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty().add(aveQty));

                        } else {
                            tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty());
                        }

                    }

                }
            }

        } else if (calculationType.equals(CFC_ADJUSTMENT_TYPE2) && calculationFlg.equals(IntDef.INT_THREE)) {
            // C:For excess qty, deduct equally on the following days

            BigDecimal numOfBoxes = DecimalUtil.divide(difference, spq, 0, RoundingMode.UP);
            BigDecimal aveAllocationBoxes = DecimalUtil.divide(numOfBoxes, new BigDecimal(after), 0, RoundingMode.UP);
            BigDecimal aveQty = aveAllocationBoxes.multiply(spq);
            BigDecimal subtracted = new BigDecimal(0);
            BigDecimal allQty = difference;

            for (TntRundownCfc tntRundownCfc : list) {
                for (CPOCFFComDailyEntity dailyListEntity : dailyList) {
                    if (tntRundownCfc.getCfcDate().equals(dailyListEntity.getCfcDate())
                            && tntRundownCfc.getPartsId().equals(dailyListEntity.getPartsId())
                            && tntRundownCfc.getCfcDate().getTime() >= DateTimeUtil.parseDate(
                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()) {

                        if (!DecimalUtil.isEquals(dailyListEntity.getCfcQty(), zero)) {

                            if (DecimalUtil.isGreater(allQty, zero)) {

                                if (DecimalUtil.isGreaterEquals(dailyListEntity.getCfcQty(), aveQty.add(subtracted))) {
                                    if (DecimalUtil.isGreater(aveQty, allQty)) {
                                        tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty().subtract(allQty));
                                    } else {
                                        tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty().subtract(
                                            aveQty.add(subtracted)));
                                    }
                                    allQty = allQty.subtract(aveQty.add(subtracted));
                                } else {
                                    subtracted = (aveQty.add(subtracted)).subtract(dailyListEntity.getCfcQty());
                                    tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty().subtract(
                                        dailyListEntity.getCfcQty()));
                                    allQty = allQty.subtract(dailyListEntity.getCfcQty());
                                }

                            } else {
                                tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty());
                            }

                        } else {
                            tntRundownCfc.setCfcQty(dailyListEntity.getCfcQty());
                        }

                    }
                }
            }

        } else if (calculationType.equals(CFC_ADJUSTMENT_TYPE2) && calculationFlg.equals(IntDef.INT_FOUR)) {
            // D:For excess qty, deduct since last working date in this month.
            BigDecimal subtracted = difference;
            for (int i = list.size() - 1; i > 0; i--) {
                for (int j = dailyList.size() - 1; j > 0; j--) {
                    if (list.get(i).getCfcDate().equals(dailyList.get(j).getCfcDate())
                            && list.get(i).getPartsId().equals(dailyList.get(j).getPartsId())
                            && list.get(i).getCfcDate().getTime() >= DateTimeUtil.parseDate(
                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, officeTime)).getTime()) {

                        if (!DecimalUtil.isEquals(dailyList.get(j).getCfcQty(), zero)) {
                            if (DecimalUtil.isGreaterEquals(subtracted, dailyList.get(j).getCfcQty())) {
                                list.get(i).setCfcQty(
                                    dailyList.get(j).getCfcQty().subtract(dailyList.get(j).getCfcQty()));
                                subtracted = subtracted.subtract(dailyList.get(j).getCfcQty());
                            } else {
                                if (!DecimalUtil.isEquals(subtracted, zero)) {
                                    list.get(i).setCfcQty(dailyList.get(j).getCfcQty().subtract(subtracted));
                                    subtracted = zero;
                                } else {
                                    list.get(i).setCfcQty(dailyList.get(j).getCfcQty());
                                }
                            }
                        } else {
                            list.get(i).setCfcQty(dailyList.get(j).getCfcQty());
                        }

                    }
                }
            }

        }

        return list;

    }
    
    /**
     * get tntRundownCfc info
     * 
     * @param param BaseParam
     *
     * @return List<TntRundownCfc>
     */
    public List<CPOCFFRunDownEntity> getTntRundownCfcInfo(BaseParam param) {

        List<CPOCFFRunDownEntity> cpocffRunDownEntity = new ArrayList<CPOCFFRunDownEntity>();
        Date time = getDBDateTime(param.getOfficeTimezone());

        Date firstDay = DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
            DateTimeUtil.firstDay(time)));
        param.setSwapData("firstDay", firstDay);
        param.setSwapData("lastDay", DateTimeUtil.lastDay(time));
        cpocffRunDownEntity = this.baseMapper.select(this.getSqlId("getTntRundownCfcInfo"), param);
        return cpocffRunDownEntity;
    }

}