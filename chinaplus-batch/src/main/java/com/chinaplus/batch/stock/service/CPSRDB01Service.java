/**
 * 
 * Service of Run-Down Batch.
 * 
 * @author liu_yinchuan
 */
package com.chinaplus.batch.stock.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.stock.bean.BasePartsInfoEntity;
import com.chinaplus.common.bean.TnmCalendarDetailEx;
import com.chinaplus.common.bean.TnmCalendarPartyEx;
import com.chinaplus.common.bean.TntRdDetailAttachEx;
import com.chinaplus.common.entity.TntCfcDay;
import com.chinaplus.common.entity.TntNotInRundown;
import com.chinaplus.common.entity.TntRdAttachCfc;
import com.chinaplus.common.entity.TntRdAttachShipping;
import com.chinaplus.common.entity.TntRdDetail;
import com.chinaplus.common.entity.TntRdMaster;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * 
 * Service of Run-Down Batch.
 * 
 * @author liu_yinchuan
 */
@Service
public class CPSRDB01Service extends BaseService {

    /**
     * get office information from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<BasePartsInfoEntity> getOfficesFromDatabase(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.select(this.getSqlId("getOfficesFromDatabase"), partsInfo);
    }

    /**
     * get Basic Part Information from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<BasePartsInfoEntity> getPartsMasterInfo(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.select(this.getSqlId("getPartsMasterInfo"), partsInfo);
    }

    /**
     * get Order Forecast Detail from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntRdDetailAttachEx> getOrderForecastDetail(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getOrderForecastDetail"), partsInfo);
    }
    
    /**
     * get Order Forecast Detail from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntRdDetailAttachEx> getEffectiveOrderForecast(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getEffectiveOrderForecast"), partsInfo);
    }
    
    /**
     * get Order Plan Detail from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntRdDetailAttachEx> getVVOrderDetail(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getVVOrderDetail"), partsInfo);
    }

    /**
     * get Kanban Plan Detail from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntRdDetailAttachEx> getKanbanPlanDetail(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getKanbanPlanDetail"), partsInfo);
    }

    /**
     * get On-Shipping Detail from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntRdDetailAttachEx> getOnShippingDetail(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getOnShippingDetail"), partsInfo);
    }

    /**
     * get Customer Daily Usage List from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntCfcDay> getCustomerDailyUsageList(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getCustomerDailyUsageList"), partsInfo);
    }

    /**
     * get ETD Delay for V-V from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntNotInRundown> getEtdDelayForVV(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getEtdDelayForVV"), partsInfo);
    }

    /**
     * get ETD Delay for AISIN from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntNotInRundown> getEtdDelayForAISIN(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getEtdDelayForAISIN"), partsInfo);
    }

    /**
     * get Inbound Delay from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntNotInRundown> getInboundDelay(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getInboundDelay"), partsInfo);
    }

    /**
     * get TT-Logix NG on hold from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntNotInRundown> getNgOnHold(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getNgOnHold"), partsInfo);
    }

    /**
     * get all Calendar information from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TnmCalendarDetailEx> getCalendarList(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getCalendarList"), partsInfo);
    }

    /**
     * get all Party Information from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TnmCalendarPartyEx> getCalendarPartyInfo(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getCalendarPartyInfo"), partsInfo);
    }

    /**
     * get Customer Usage Share List from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntRdAttachCfc> getCustomerUsageShareList(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getCustomerUsageShareList"), partsInfo);
    }

    /**
     * do rundown information update.
     * 
     * @param partsInfo select parameter
     * @param rundownList rundown information
     * @return update count
     */
    public int doUpdateRundownInfo(BasePartsInfoEntity partsInfo, List<TntRdMaster> rundownList) {

        // delete from TNT_RUNDOWN_MASTER
        this.baseMapper.delete(this.getSqlId("deleteRundownMaster"), partsInfo);

        // delete from TNT_RUNDOWN_DETAIL
        this.baseMapper.delete(this.getSqlId("deleteRundownDetail"), partsInfo);

        // delete from TNT_RD_DETAIL_ATTACH
        this.baseMapper.delete(this.getSqlId("deleteRdDetailAttach"), partsInfo);

        // delete from TNT_RUNDOWN_ATTACH
        this.baseMapper.delete(this.getSqlId("deleteRundownAttach"), partsInfo);

        // delete from TNT_NOT_IN_RUNDOWN
        this.baseMapper.delete(this.getSqlId("deleteNotInRundown"), partsInfo);

        // get current date
        Timestamp currentTime = this.baseMapper.getCurrentTime();

        // define
        final int MAX_COMMIT = 100;
        int maxMasterId = getMaxRundowMasterId();
        int maxRundowDetailId = getMaxRundowDetailId();
        int maxRundowAttachId = getMaxRundowAttachId();
        int maxRdDetailAttachId = getMaxRdDetailAttachId();
        int maxNotInRundownId = getMaxNotInRundownId();

        // save rundown information into database
        for (TntRdMaster rundownMaster : rundownList) {

            // insert into TNT_RUNDOWN_MASTER
            rundownMaster.setRundownMasterId(maxMasterId++);
            rundownMaster.setCreatedBy(BatchConst.BATCH_USER_ID);
            rundownMaster.setCreatedDate(currentTime);
            rundownMaster.setUpdatedBy(BatchConst.BATCH_USER_ID);
            rundownMaster.setUpdatedDate(currentTime);
            rundownMaster.setVersion(IntDef.INT_ONE);
            this.baseMapper.insert(this.getSqlId("saveIntoRundownMaster"), rundownMaster);

            // insert into TNT_RUNDOWN_ATTACH
            if (rundownMaster.getTntRundownAttachs() != null) {

                // commit list
                List<TntRdAttachCfc> commitArray = new ArrayList<TntRdAttachCfc>();
                // loop insert
                for (TntRdAttachCfc detail : rundownMaster.getTntRundownAttachs()) {

                    // set id
                    detail.setRundownAttachId(maxRundowAttachId++);
                    detail.setTntRundownMaster(rundownMaster);
                    detail.setCreatedBy(BatchConst.BATCH_USER_ID);
                    detail.setCreatedDate(currentTime);
                    detail.setUpdatedBy(BatchConst.BATCH_USER_ID);
                    detail.setUpdatedDate(currentTime);
                    detail.setVersion(IntDef.INT_ONE);
                    commitArray.add(detail);

                    // commit
                    if (commitArray.size() >= MAX_COMMIT) {

                        // insert
                        this.baseMapper.insert(this.getSqlId("saveIntoRundownAttach"), commitArray);

                        // reset
                        commitArray = new ArrayList<TntRdAttachCfc>();
                    }
                }

                // commit
                if (commitArray.size() >= IntDef.INT_ONE) {

                    // insert
                    this.baseMapper.insert(this.getSqlId("saveIntoRundownAttach"), commitArray);
                }
            }

            // insert into TNT_NOT_IN_RUNDOWN
            if (rundownMaster.getTntNotInRundowns() != null) {

                // commit list
                List<TntNotInRundown> commitArray = new ArrayList<TntNotInRundown>();
                // loop insert
                for (TntNotInRundown detail : rundownMaster.getTntNotInRundowns()) {

                    // set id
                    detail.setNotInRundownId(maxNotInRundownId++);
                    detail.setTntRundownMaster(rundownMaster);
                    detail.setCreatedBy(BatchConst.BATCH_USER_ID);
                    detail.setCreatedDate(currentTime);
                    detail.setUpdatedBy(BatchConst.BATCH_USER_ID);
                    detail.setUpdatedDate(currentTime);
                    detail.setVersion(IntDef.INT_ONE);
                    commitArray.add(detail);

                    // commit
                    if (commitArray.size() >= MAX_COMMIT) {

                        // insert
                        this.baseMapper.insert(this.getSqlId("saveIntoNotInRundown"), commitArray);

                        // reset
                        commitArray = new ArrayList<TntNotInRundown>();
                    }
                }

                // commit
                if (commitArray.size() >= IntDef.INT_ONE) {

                    // insert
                    this.baseMapper.insert(this.getSqlId("saveIntoNotInRundown"), commitArray);
                }
            }

            // if exists
            if (rundownMaster.getTntRundownDetails() != null) {

                // loop insert
                for (TntRdDetail rundownDetail : rundownMaster.getTntRundownDetails()) {

                    // set into database
                    rundownDetail.setRundownDetailId(maxRundowDetailId++);
                    rundownDetail.setTntRundownMaster(rundownMaster);
                    rundownDetail.setCreatedBy(BatchConst.BATCH_USER_ID);
                    rundownDetail.setCreatedDate(currentTime);
                    rundownDetail.setUpdatedBy(BatchConst.BATCH_USER_ID);
                    rundownDetail.setUpdatedDate(currentTime);
                    rundownDetail.setVersion(IntDef.INT_ONE);

                    // insert into TNT_RUNDOWN_DETAIL
                    this.baseMapper.insert(this.getSqlId("saveIntoRundownDetail"), rundownDetail);

                    // insert into TNT_RD_DETAIL_ATTACH
                    if (rundownDetail.getTntRdDetailAttachs() != null) {

                        // commit list
                        List<TntRdAttachShipping> commitArray = new ArrayList<TntRdAttachShipping>();
                        // loop insert
                        for (TntRdAttachShipping detail : rundownDetail.getTntRdDetailAttachs()) {

                            // set id
                            detail.setRdDetailAttachId(maxRdDetailAttachId++);
                            detail.setTntRundownDetail(rundownDetail);
                            detail.setCreatedBy(BatchConst.BATCH_USER_ID);
                            detail.setCreatedDate(currentTime);
                            detail.setUpdatedBy(BatchConst.BATCH_USER_ID);
                            detail.setUpdatedDate(currentTime);
                            detail.setVersion(IntDef.INT_ONE);
                            commitArray.add(detail);

                            // commit
                            if (commitArray.size() >= MAX_COMMIT) {

                                // insert
                                this.baseMapper.insert(this.getSqlId("saveIntoRdDetailAttach"), commitArray);

                                // reset
                                commitArray = new ArrayList<TntRdAttachShipping>();
                            }
                        }

                        // commit
                        if (commitArray.size() >= IntDef.INT_ONE) {

                            // insert
                            this.baseMapper.insert(this.getSqlId("saveIntoRdDetailAttach"), commitArray);
                        }
                    }
                }
            }
        }

        // return size
        return rundownList.size();
    }

    /**
     * Get max RundowMaster id.
     * 
     * @return RundowMaster id.
     */
    public int getMaxRundowMasterId() {

        // get maxRundowId
        Integer maxRundowId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxRundowMasterId"));

        // get form databse
        return maxRundowId == null ? Integer.MIN_VALUE : maxRundowId.intValue();
    }

    /**
     * Get max RundowDetail id.
     * 
     * @return RundowDetail id.
     */
    public int getMaxRundowDetailId() {

        // get maxRundowId
        Integer maxRundowId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxRundowDetailId"));

        // get form databse
        return maxRundowId == null ? Integer.MIN_VALUE : maxRundowId.intValue();
    }

    /**
     * Get max RundowAttach id.
     * 
     * @return RundowAttach id.
     */
    public int getMaxRundowAttachId() {

        // get maxRundowId
        Integer maxRundowId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxRundowAttachId"));

        // get form databse
        return maxRundowId == null ? Integer.MIN_VALUE : maxRundowId.intValue();
    }

    /**
     * Get max RdDetailAttach id.
     * 
     * @return RdDetailAttach id.
     */
    public int getMaxRdDetailAttachId() {

        // get maxRundowId
        Integer maxRundowId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxRdDetailAttachId"));

        // get form databse
        return maxRundowId == null ? Integer.MIN_VALUE : maxRundowId.intValue();
    }

    /**
     * Get max NotInRundown id.
     * 
     * @return NotInRundown id
     */
    public int getMaxNotInRundownId() {

        // get maxRundowId
        Integer maxRundowId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxNotInRundownId"));

        // get form databse
        return maxRundowId == null ? Integer.MIN_VALUE : maxRundowId.intValue();
    }

}
