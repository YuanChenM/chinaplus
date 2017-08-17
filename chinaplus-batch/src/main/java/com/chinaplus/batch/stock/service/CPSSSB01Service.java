/**
 * 
 * Service of Stock status Batch.
 * 
 * @author liu_yinchuan
 */
package com.chinaplus.batch.stock.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.stock.bean.BasePartsInfoEntity;
import com.chinaplus.common.bean.TnmCalendarDetailEx;
import com.chinaplus.common.bean.TnmCalendarPartyEx;
import com.chinaplus.common.bean.TntRdDetailAttachEx;
import com.chinaplus.common.bean.TntStockStatusEx;
import com.chinaplus.common.entity.TnfImpStockByDay;
import com.chinaplus.common.entity.TntCfcDay;
import com.chinaplus.common.entity.TntRdDetail;
import com.chinaplus.common.entity.TntStockStatus;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * 
 * Service of Stock Status Batch.
 * 
 * @author liu_yinchuan
 */
@Service
public class CPSSSB01Service extends BaseService {

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
    public List<TntStockStatusEx> getPartsMasterInfo(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getPartsMasterInfo"), partsInfo);
    }

    /**
     * get Imp Actual Outbound Detail In Current Month.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TnfImpStockByDay> getImpActualOutboundDetail(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getImpActualOutboundDetail"), partsInfo);
    }

    /**
     * get Run-down Detail Information.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntRdDetail> getRundownDetailInfo(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getRundownDetailInfo"), partsInfo);
    }

    /**
     * get Kanban Plan Detail from database.
     * 
     * @param partsInfo select parameter
     * @return partsInfo list
     */
    public List<TntRdDetailAttachEx> getNextInboundPlanList(BasePartsInfoEntity partsInfo) {

        // get form databse
        return this.baseMapper.selectList(this.getSqlId("getNextInboundPlanList"), partsInfo);
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
     * do stock status information update.
     * 
     * @param officeInfo select parameter
     * @param stockStatusList stock status information list
     * @return update count
     * @throws Exception e
     */
    public int doUpdateStockStatusInfo(BasePartsInfoEntity officeInfo, List<TntStockStatusEx> stockStatusList)
        throws Exception {

        // delete from TNT_RUNDOWN_MASTER
        this.baseMapper.delete(this.getSqlId("deleteStockStatusInfo"), officeInfo);

        // get current date
        Timestamp currentTime = this.baseMapper.getCurrentTime();

        // delete from TNT_RUNDOWN_MASTER
        int maxStockId = getMaxStockStatusId();

        // loop insert
        for (TntStockStatus stockStatus : stockStatusList) {

            // get
            TntStockStatus newStockStatus = new TntStockStatus();

            // save
            BeanUtils.copyProperties(stockStatus, newStockStatus);

            // set stock id
            newStockStatus.setStockStatusId(maxStockId++);

            // set into create by and update by
            newStockStatus.setCreatedBy(BatchConst.BATCH_USER_ID);
            newStockStatus.setCreatedDate(currentTime);
            newStockStatus.setUpdatedBy(BatchConst.BATCH_USER_ID);
            newStockStatus.setUpdatedDate(currentTime);
            newStockStatus.setVersion(IntDef.INT_ONE);

            // save
            baseDao.insert(newStockStatus);
        }

        // return size
        return stockStatusList.size();
    }

    /**
     * Get max stock status id.
     * 
     * @return RundowMaster id.
     */
    public int getMaxStockStatusId() {

        // get maxRundowId
        Integer maxRundowId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxStockStatusId"));

        // get form databse
        return maxRundowId == null ? Integer.MIN_VALUE : maxRundowId.intValue();
    }

    /**
     * get user mail information from database.
     * 
     * @param office select office information
     * @return partsInfo list
     */
    public List<BasePartsInfoEntity> getUserMailInfoList(BasePartsInfoEntity office) {

        // get form databse
        return this.baseMapper.select(this.getSqlId("getUserMailInfoList"), office);
    }

    /**
     * get user mail alarm information from database.
     * 
     * @param userInfo select user information
     * @return partsInfo list
     */
    public List<BasePartsInfoEntity> getUserMailAlarmInfoList(BasePartsInfoEntity userInfo) {

        // get form databse
        return this.baseMapper.select(this.getSqlId("getUserMailAlarmInfoList"), userInfo);
    }
}
