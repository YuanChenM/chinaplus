/**
 * CPKKPF02Service.java
 * 
 * @screen CPKKPF02
 * @author shiyang
 */
package com.chinaplus.web.kbp.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TntSyncTime;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.web.kbp.entity.CPKKPF02Entity;

/**
 * Download Kanban Plan for Revision History(doc2) Service.
 */
@Service
public class CPKKPF02Service extends BaseService {

    /**
     * get SyncTime List
     * 
     * @param param param
     * @return syncTimeList syncTimeList
     */
    public Timestamp getSyncTimeList(BaseParam param) {
        List<TntSyncTime> syncTimeList = baseMapper.select(getSqlId("findOfferCodeDateList"), param);
        if (syncTimeList != null && syncTimeList.size() > 0) {
            return syncTimeList.get(0).getImpSyncTime();
        }
        return null;
    }

    /**
     * Get Invoice Information.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPKKPF02Entity> getInvoiceInfo(CPKKPF02Entity condition) {

        return super.baseMapper.select(getSqlId("getInvoiceInfo"), condition);
    }

    /**
     * Get Shipping Plan Information.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPKKPF02Entity> getShippingPlanInfo(CPKKPF02Entity condition) {

        return super.baseMapper.select(getSqlId("getShippingPlanInfo"), condition);
    }

    /**
     * Get Shipping Plan History Information.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPKKPF02Entity> getShippingPlanHistoryInfo(CPKKPF02Entity condition) {

        return super.baseMapper.select(getSqlId("getShippingPlanHistoryInfo"), condition);
    }

    /**
     * Get Parts Base Information.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPKKPF02Entity> getPartsBaseInfo(CPKKPF02Entity condition) {

        return super.baseMapper.select(getSqlId("getPartsBaseInfo"), condition);
    }

    /**
     * Get Parts Box Information.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPKKPF02Entity> getPartsBoxInfo(CPKKPF02Entity condition) {

        return super.baseMapper.select(getSqlId("getPartsBoxInfo"), condition);
    }

    /**
     * Get Parts QTY Information.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPKKPF02Entity> getPartsQtyInfo(CPKKPF02Entity condition) {

        return super.baseMapper.select(getSqlId("getPartsQtyInfo"), condition);
    }
}
