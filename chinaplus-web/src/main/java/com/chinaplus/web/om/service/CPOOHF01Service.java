/**
 * CPVIVF01Service.java
 * 
 * @screen CPVIVF01
 * @author gu_chengchen
 */
package com.chinaplus.web.om.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataType;
import com.chinaplus.common.entity.TntSyncTime;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.exception.DownloadException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOOHF01Entity;

/**
 * Download Invoice Supplementary Data File Service.
 */
@Service
public class CPOOHF01Service extends BaseService {

    /** SQL ID: select Order History */
    private static final String SQLID_SELECT_ORDERHISTORY_LIST = "getOrderHistoryList";

    /** SQL ID: select TransferInfo */
    private static final String SQLID_SELECT_TRANSFERINFO_LIST = "getTransferInfo";

    /** SQL ID: select onShippingInfo */
    private static final String SQLID_SELECT_ONSHIPPING_LIST = "getShippingInfoList";

    /**
     * Query all order history data.
     * 
     * @param param
     *        the query order status id
     * @param <T> BaseParam
     * @return the query result
     * @exception BusinessException BusinessException
     * @exception DownloadException DownloadException
     */
    public <T extends BaseParam> List<CPOOHF01Entity> getOrderHistory(T param) throws BusinessException,
        DownloadException {
        List<CPOOHF01Entity> orderHistorylist = super.baseMapper.select(SQLID_SELECT_ORDERHISTORY_LIST, param);
        List<Integer> orderStatusIdList = new ArrayList<Integer>();
        if (orderHistorylist != null && orderHistorylist.size() > 0) {
            for (CPOOHF01Entity entity : orderHistorylist) {
                Integer orderStatusId = entity.getOrderStatusId();
                orderStatusIdList.add(orderStatusId);
            }
        }
        param.getSwapData().put("selectedDatas", orderStatusIdList);
        List<CPOOHF01Entity> transferInfoList = super.baseMapper.select(SQLID_SELECT_TRANSFERINFO_LIST, param);
        Map<Integer, Map<String, StringBuffer>> transferInfoMap = new HashMap<Integer, Map<String, StringBuffer>>();
        if (transferInfoList != null && transferInfoList.size() > 0) {
            for (CPOOHF01Entity entity : transferInfoList) {
            	int digits = MasterManager.getUomDigits(entity.getUomCode());
                Integer orderStatusId = entity.getOrderStatusId();
                String toCode = entity.getTransferToCode();
                String fromCode = entity.getTransferFromCode();
                BigDecimal toQty = entity.getTransferToQty();
                Map<String, StringBuffer> transferMap = transferInfoMap.get(orderStatusId);
                if (transferMap == null) {
                    transferMap = new HashMap<String, StringBuffer>();
                    StringBuffer transferToDetails = new StringBuffer();
                    StringBuffer transferFrom = new StringBuffer();
                    transferMap.put("transferToDetails", transferToDetails);
                    transferMap.put("transferFrom", transferFrom);
                    transferInfoMap.put(orderStatusId, transferMap);
                }
                StringBuffer transferToDetails = transferMap.get("transferToDetails");
                StringBuffer transferFrom = transferMap.get("transferFrom");
                if (!StringUtil.isNullOrEmpty(toCode) && toQty != null) {
                    if (transferToDetails.length() > 0) {
                        transferToDetails.append(",");
                    }
                    transferToDetails.append(toCode);
                    transferToDetails.append("(");
                    transferToDetails.append(toQty.setScale(digits));
                    transferToDetails.append(")");
                }
                if (!StringUtil.isNullOrEmpty(fromCode)) {
                    if (transferFrom.length() > 0) {
                        if (transferFrom.toString().contains(fromCode)) {
                            continue;
                        }
                        transferFrom.append(",");
                    }
                    transferFrom.append(fromCode);
                }
            }
        }
        if (transferInfoMap.size() > 0) {
            for (CPOOHF01Entity entity : orderHistorylist) {
                Integer orderStatusId = entity.getOrderStatusId();
                if (transferInfoMap.containsKey(orderStatusId)) {
                    entity.setTransferToDetails(transferInfoMap.get(orderStatusId).get("transferToDetails").toString());
                    entity.setTransferFrom(transferInfoMap.get(orderStatusId).get("transferFrom").toString());
                }
            }
        }
        return orderHistorylist;
    }
    
    /**
     * Query all order's onshipping data.
     * 
     * @param <T> BaseParam
     * @param param the query condition
     * @param entityList List<CPOOHF01Entity>
     * @return the query result
     */
    public <T extends BaseParam> Map<String, List<Date>> getOnShippingInfoList(T param, List<CPOOHF01Entity> entityList) {
        Map<String, List<Date>> onShippingHeader = new HashMap<String, List<Date>>();
        List<Date> etdList = new ArrayList<Date>();
        List<Date> etaList = new ArrayList<Date>();
        Date officeTime = new Date(super.getDBDateTime(param.getOfficeTimezone()).getTime());
        param.setSwapData("officeTime", officeTime);
        List<CPOOHF01Entity> onShippingInfoList = super.baseMapper.select(SQLID_SELECT_ONSHIPPING_LIST, param);
        if (onShippingInfoList != null && onShippingInfoList.size() > 0) {
            for (CPOOHF01Entity entity : onShippingInfoList) {
                Date etd = entity.getEtd();
                Date eta = entity.getEta();
                if (!etdList.contains(etd)) {
                    etdList.add(etd);
                    etaList.add(eta);
                } else {
                    boolean flag = true;
                    for (int i = 0; i < etdList.size(); i++) {
                        Date etdi = etdList.get(i);
                        Date etai = etaList.get(i);
                        if (etd.equals(etdi) && ((eta == null && etai == null) || (eta != null && eta.equals(etai)))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        etdList.add(etd);
                        etaList.add(eta);
                    }
                }
            }
            onShippingHeader.put("ETD", etdList);
            onShippingHeader.put("ETA", etaList);
            int shippingSize = onShippingHeader.get("ETD").size();
            Map<String, BigDecimal[]> onShippingQtyByOrderStatusId = new HashMap<String, BigDecimal[]>();
            for (CPOOHF01Entity entity : onShippingInfoList) {
                Integer partsId = entity.getPartsId();
                String impPoNo = entity.getImpPONo();
                String expPoNo = entity.getExpPONo();
                String key = impPoNo + expPoNo + partsId;
                Date etd = entity.getEtd();
                Date eta = entity.getEta();
                BigDecimal qty = entity.getQty();
                int headerIndex = etdList.indexOf(etd) >= etaList.indexOf(eta) ? etdList.indexOf(etd) : etaList
                    .indexOf(eta);
                if (!onShippingQtyByOrderStatusId.containsKey(key)) {
                	BigDecimal[] onShippingQtys = new BigDecimal[shippingSize];
                    onShippingQtys[headerIndex] = qty;
                    onShippingQtyByOrderStatusId.put(key, onShippingQtys);
                } else {
                	BigDecimal[] array = onShippingQtyByOrderStatusId.get(key);
                	BigDecimal oldQty = array[headerIndex] == null ? new BigDecimal(0) : array[headerIndex];
                	array[headerIndex] = qty.add(oldQty);
                }
            }

            for (CPOOHF01Entity entity : entityList) {
                // Integer orderStatusId = entity.getOrderStatusId();
                Integer partsId = entity.getPartsId();
                String impPoNo = entity.getImpPONo();
                String expPoNo = entity.getExpPONo();
                String key = impPoNo + expPoNo + partsId;
                if (onShippingQtyByOrderStatusId.containsKey(key)) {
                    entity.setOnShippingQtyArray(onShippingQtyByOrderStatusId.get(key));
                } else {
                    entity.setOnShippingQtyArray(new BigDecimal[shippingSize]);
                }

            }
        }
        return onShippingHeader;
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
