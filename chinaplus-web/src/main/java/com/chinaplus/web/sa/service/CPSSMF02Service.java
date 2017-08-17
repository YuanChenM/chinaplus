/**
 * CPSSMF02Service.java
 * 
 * @screen CPSSMF02
 * @author ma_chao
 */
package com.chinaplus.web.sa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.NirdFlag;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataType;
import com.chinaplus.common.entity.TntOrder;
import com.chinaplus.common.entity.TntSsPlan;
import com.chinaplus.common.entity.TntSyncTime;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.web.sa.control.CPSSMFXXUtil;
import com.chinaplus.web.sa.entity.CPSSMF01ColEntity;
import com.chinaplus.web.sa.entity.CPSSMF01Entity;

/**
 * Revised Shipping Status Doc2 Download Service.
 */
@Service
public class CPSSMF02Service extends BaseService {

    /**
     * search order info
     * 
     * @param param param
     * @return order list
     */
    public List<TntOrder> searchOrderList(BaseParam param) {
        return this.baseMapper.select(this.getSqlId("searchOrderList"), param);
    }

    /**
     * search ss plan info
     * 
     * @param param param
     * @return ss plan list
     */
    public List<TntSsPlan> searchSsPlanList(BaseParam param) {
        return this.baseMapper.select(this.getSqlId("searchSsPlanList"), param);
    }

    /**
     * search order info
     * 
     * @param param param
     * @return order list
     */
    public List<TntOrder> searchOrderListByInvoice(BaseParam param) {
        return this.baseMapper.select(this.getSqlId("searchOrderListByInvoice"), param);
    }

    /**
     * search order info
     * 
     * @param param param
     * @return order list
     */
    public List<TntOrder> searchOrderListByPlan(BaseParam param) {
        return this.baseMapper.select(this.getSqlId("searchOrderListByPlan"), param);
    }

    /**
     * Search the shiiping plan infos by condition form screen
     * 
     * @param param search confition form screen CPSSMS01
     * @param orderList orderList
     * @param orderListByInvoice orderListByInvoice
     * @param planList planList
     * @return search result
     */
    public List<CPSSMF01Entity> searchShippingPlanInfo(BaseParam param, List<TntOrder> orderList,
        List<TntOrder> orderListByInvoice, List<TntSsPlan> planList) {

        List<TntOrder> list = new ArrayList<TntOrder>();
        if (null != orderList && !orderList.isEmpty()) {
            list.addAll(orderList);
        }
        if (null != orderListByInvoice && !orderListByInvoice.isEmpty()) {
            list.addAll(orderListByInvoice);
        }

        // search plan datas
        param.setFilter("orderList", list);
        param.setFilter("planList", planList);
        param.setFilter("orderListByInvoice", orderListByInvoice);

        List<CPSSMF01Entity> dataList = this.baseMapper.select(this.getSqlId("searchShippingPlanInfo"), param);
        if (null != dataList && !dataList.isEmpty()) {
            for (CPSSMF01Entity entity : dataList) {
                BigDecimal forceCompletedQty = entity.getForceCompletedQty();
                // 2016/07/04 shiyang mod start
                // if (DecimalUtil.isGreater(forceCompletedQty, BigDecimal.ZERO)) {
                if (DecimalUtil.isGreater(forceCompletedQty, BigDecimal.ZERO)
                        && entity.getCompletedFlag() == CodeConst.CompletedFlag.NORMAL) {
                    // 2016/07/04 shiyang mod end
                    entity.setQty(BigDecimal.ZERO);
                }
            }
        }
        return dataList;
    }

    /**
     * Search the invoice info by condition
     * 
     * @param param search condition
     * @param planInfoList planInfoList
     * @return search result
     */
    public List<CPSSMF01Entity> searchInvoiceInfo(BaseParam param, List<CPSSMF01Entity> planInfoList) {

        param.setFilter("planInfoList", planInfoList);

        List<CPSSMF01Entity> dataList = null;
        if (null != planInfoList && !planInfoList.isEmpty()) {

            dataList = this.baseMapper.select(this.getSqlId("searchInvoiceInfo"), param);
            if (null != dataList && !dataList.isEmpty()) {
                for (CPSSMF01Entity entity : dataList) {
                    BigDecimal forceCompletedQty = entity.getForceCompletedQty();
                    if (DecimalUtil.isGreater(forceCompletedQty, BigDecimal.ZERO)) {
                        entity.setQty(BigDecimal.ZERO);
                    }
                }
            }
        }

        return dataList;
    }

    /**
     * sort the plan and actual info by etd,invoiceNo,eta,impInbPlanDate
     * 
     * @param planList planList
     * @param invoiceList invoiceList
     * @param planAndActualColList planAndActualColList
     * @param officeTime officeTime
     */
    public void sortPlanAndActualInfo(List<CPSSMF01Entity> planList, List<CPSSMF01Entity> invoiceList,
        List<CPSSMF01ColEntity> planAndActualColList, Date officeTime) {
        List<CPSSMF01Entity> list = new ArrayList<CPSSMF01Entity>();
        if (null != planList && !planList.isEmpty()) {
            for (CPSSMF01Entity entity : planList) {
                int nirdFlag = NirdFlag.NORMAL;
                if (null != entity.getNirdFlag()) {
                    nirdFlag = entity.getNirdFlag();
                }
                if (nirdFlag != NirdFlag.NOT_IN_RUNDOWN) {
                    list.add(entity);
                }
            }
        }
        if (null != invoiceList && !invoiceList.isEmpty()) {
            for (CPSSMF01Entity entity : invoiceList) {
                int nirdFlag = NirdFlag.NORMAL;
                if (null != entity.getNirdFlag()) {
                    nirdFlag = entity.getNirdFlag();
                }
                if (nirdFlag != NirdFlag.NOT_IN_RUNDOWN) {
                    list.add(entity);
                }
            }
        }
        CPSSMFXXUtil.combinePlanOrInvoice(list, planAndActualColList);

        CPSSMFXXUtil.sort(planAndActualColList);

        if (null != planAndActualColList && !planAndActualColList.isEmpty()) {
            int actualVersion = IntDef.INT_ONE;
            for (CPSSMF01ColEntity item : planAndActualColList) {
                Date etd = item.getEtd();
                if (officeTime.after(etd)) {
                    item.setBeforeToday(true);
                }
                Integer invoiceId = item.getInvoiceId();
                if (null != invoiceId) {
                    item.setOriginalVersion(actualVersion);
                    item.setRevisionVersion(IntDef.INT_ZERO);
                    actualVersion++;
                }
            }
        }

    }

    /**
     * search data sync time
     * 
     * @param param BaseParam
     * @param dateTimeFormat dateTimeFormat
     * @param lang lang
     * @return data sync time map
     */
    public Map<Integer, String> searchDataSyncTime(BaseParam param, String dateTimeFormat, Language lang) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(SyncTimeDataType.SSMS, StringConst.EMPTY);
        map.put(SyncTimeDataType.TT_LOGIX_VV, StringConst.EMPTY);
        map.put(SyncTimeDataType.TT_LOGIX_AISIN, StringConst.EMPTY);
        List<TntSyncTime> list = this.baseMapper.select(this.getSqlId("searchDataSyncTime"), param);
        if (null != list && !list.isEmpty()) {
            for (TntSyncTime item : list) {
                if (null != item.getImpSyncTime()) {
                    map.put(item.getDataType(), DateTimeUtil.formatDate(dateTimeFormat, item.getImpSyncTime(), lang));
                }
            }
        }
        return map;
    }
}
