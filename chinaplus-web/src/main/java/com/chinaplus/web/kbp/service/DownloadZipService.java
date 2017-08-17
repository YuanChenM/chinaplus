/**
 * DownloadZipService.java
 * 
 * @screen CPKKPF01&CPKKPF02
 * @author shiyang
 */
package com.chinaplus.web.kbp.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.web.kbp.entity.DownloadZipEntity;

/**
 * Download Latest Kanban Plan File(doc1) & Kanban Plan for Revision History(doc2) Service.
 */
@Service
public class DownloadZipService extends BaseService {

    /**
     * Get Not In Rundown Data.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<DownloadZipEntity> getNirdData(DownloadZipEntity condition) {

        return super.baseMapper.select(getSqlId("getNirdData"), condition);
    }

    /**
     * Get Last Shipping Plan.
     *
     * @param condition the query condition
     * @return max etd
     */
    public Timestamp getLastShippingPlan(DownloadZipEntity condition) {

        List<DownloadZipEntity> result = super.baseMapper.select(getSqlId("getLastShippingPlan"), condition);
        if (result != null && result.size() > 0) {
            return result.get(0).getMaxEtd();
        }
        return null;
    }

    /**
     * Get New Shipping Route.
     *
     * @param condition the query condition
     * @return new etds
     */
    public Timestamp getNewShippingRoute(DownloadZipEntity condition) {

        List<DownloadZipEntity> result = super.baseMapper.select(getSqlId("getNewShippingRoute"), condition);
        if (result != null && result.size() > 0) {
            return result.get(0).getNewEtd();
        }
        return null;
    }

    /**
     * Insert Kanban Shipping.
     *
     * @param param BaseParam
     * @param entity DownloadZipEntity
     * @return insert count
     */
    public int insertKanbanShipping(BaseParam param, DownloadZipEntity entity) {
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.insert(getSqlId("insertKanbanShipping"), entity);
    }

    /**
     * Insert Kanban Shipping Parts.
     *
     * @param param BaseParam
     * @param entity DownloadZipEntity
     * @return insert count
     */
    public int insertKanbanShippingParts(BaseParam param, DownloadZipEntity entity) {
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.insert(getSqlId("insertKanbanShippingParts"), entity);
    }

    /**
     * Insert Kanban Plan.
     *
     * @param param BaseParam
     * @param entity DownloadZipEntity
     * @return insert count
     */
    public int insertKanbanPlan(BaseParam param, DownloadZipEntity entity) {
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.insert(getSqlId("insertKanbanPlan"), entity);
    }

    /**
     * Insert Kanban Plan Parts.
     *
     * @param param BaseParam
     * @param entity DownloadZipEntity
     * @return insert count
     */
    public int insertKanbanPlanParts(BaseParam param, DownloadZipEntity entity) {
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.insert(getSqlId("insertKanbanPlanParts"), entity);
    }

    /**
     * Delete not in rundown data (TNT_KANBAN_SHIPPING).
     *
     * @param entity DownloadZipEntity
     * @return delete count
     */
    public int deleteKanbanShipping(DownloadZipEntity entity) {

        return super.baseMapper.delete(getSqlId("deleteKanbanShipping"), entity);
    }

    /**
     * Delete not in rundown data (TNT_KANBAN_SHIPPING_PARTS).
     *
     * @param entity DownloadZipEntity
     * @return delete count
     */
    public int deleteKanbanShippingParts(DownloadZipEntity entity) {

        return super.baseMapper.delete(getSqlId("deleteKanbanShippingParts"), entity);
    }
}
