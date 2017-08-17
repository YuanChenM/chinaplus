/**
 * CPKKPF01Service.java
 * 
 * @screen CPKKPF01
 * @author shiyang
 */
package com.chinaplus.web.kbp.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TntSyncTime;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.web.kbp.entity.CPKKPF01Entity;

/**
 * Download Latest Kanban Plan File(doc1) Service.
 */
@Service
public class CPKKPF01Service extends BaseService {

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
    public List<CPKKPF01Entity> getInvoiceInfo(CPKKPF01Entity condition) {

        return super.baseMapper.select(getSqlId("getInvoiceInfo"), condition);
    }

    /**
     * Get Shipping Plan Information.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPKKPF01Entity> getShippingPlanInfo(CPKKPF01Entity condition) {

        return super.baseMapper.select(getSqlId("getShippingPlanInfo"), condition);
    }

    /**
     * Get Parts Base Information.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPKKPF01Entity> getPartsBaseInfo(CPKKPF01Entity condition) {

        return super.baseMapper.select(getSqlId("getPartsBaseInfo"), condition);
    }

    /**
     * Get Parts Box Information.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPKKPF01Entity> getPartsBoxInfo(CPKKPF01Entity condition) {

        return super.baseMapper.select(getSqlId("getPartsBoxInfo"), condition);
    }

    /**
     * Get Parts QTY Information.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPKKPF01Entity> getPartsQtyInfo(CPKKPF01Entity condition) {

        return super.baseMapper.select(getSqlId("getPartsQtyInfo"), condition);
    }
}
