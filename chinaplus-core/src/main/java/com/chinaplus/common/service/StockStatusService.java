/**
 * Common logic for prepare Stock Status.
 * 
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.TntRdDetailAttachEx;
import com.chinaplus.common.bean.TntStockStatusEx;
import com.chinaplus.common.bean.TntStockStatusHeader;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataType;
import com.chinaplus.common.entity.TntIfBatch;
import com.chinaplus.common.entity.TntRdAttachShipping;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Common logic for Stock Status File.
 */
@Service
public class StockStatusService extends BaseService {

    /**
     * Find the datas count by condition.
     * 
     * @param param the query parameter
     * @return the datas count
     */
    public int getSelectDatasCount(BaseParam param) {
        return super.getDatasCount(SQLID_FIND_COUNT, param);
    }

    /**
     * Get Stock Status list by condition
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntStockStatusEx> getStockStatusByCondition(BaseParam param) {

        return super.baseMapper.select(getSqlId("findAllStockStatusList"), param);
    }

    /**
     * Get max forecast number by condition.
     *
     * @param param filters
     * @return max forecast number
     */
    public Integer findMaxForecastNum(BaseParam param) {

        // get max forecast number
        Integer maxFcNum = super.baseMapper.getSqlSession().selectOne(this.getSqlId("findMaxForecastNum"), param);

        // if no max fc number
        if (maxFcNum == null) {
            maxFcNum = IntDef.INT_ZERO;
        }

        return maxFcNum;
    }

    /**
     * Get On-shipping header by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntRdDetailAttachEx> findOnShippingHeader(BaseParam param) {

        return super.baseMapper.select(getSqlId("findOnShippingHeader"), param);
    }

    /**
     * Get On-Shipping information list by condition
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntRdDetailAttachEx> findOnShippingList(BaseParam param) {

        return super.baseMapper.select(getSqlId("findOnShippingList"), param);
    }

    /**
     * Get Stock Status list by condition
     *
     * @param officeId filters
     * @param dateList dateList
     */
    public void prepareSyncDateTime(Integer officeId, Date[] dateList) {

        // HQL
        String hql = "FROM TNT_IF_BATCH A WHERE A.officeId = ? OR (A.officeId IS NULL AND A.batchType = ?)";
        Object[] param = new Object[] { officeId, SyncTimeDataType.SSMS };
        // get sync date time from database
        List<TntIfBatch> ifBatchList = super.baseDao.select(hql, param);
        if (ifBatchList != null) {
            for (TntIfBatch ifBatch : ifBatchList) {
                if (ifBatch.getOfficeId() == null) {
                    dateList[IntDef.INT_ONE] = ifBatch.getIfDateTime();
                } else if (ifBatch.getBatchType().equals(SyncTimeDataType.TT_LOGIX_VV)) {
                    dateList[IntDef.INT_TWO] = ifBatch.getIfDateTime();
                } else if (ifBatch.getBatchType().equals(SyncTimeDataType.TT_LOGIX_AISIN)) {
                    dateList[IntDef.INT_THREE] = ifBatch.getIfDateTime();
                }
            }
        }
    }

    /**
     * Get Stock Status header information list by condition
     *
     * @param param filters
     * @return Stock Status list
     */
    public TntStockStatusHeader getStockStatusHeader(BaseParam param) {

        // defined
        TntStockStatusHeader header = new TntStockStatusHeader();

        // set max forecast number
        header.setMaxFCNum(this.findMaxForecastNum(param));

        // set on-shipping header information list
        header.setOnShippingDetailList(this.findOnShippingHeader(param));

        // return
        return header;
    }

    /**
     * Get Stock Status list by condition
     *
     * @param param filters
     * @param lang language
     * @return Stock Status list
     */
    public List<TntStockStatusEx> getStockStatusInfoList(BaseParam param, Language lang) {

        // get stock status list by condition
        List<TntStockStatusEx> stockStatusList = this.getStockStatusByCondition(param);

        // get on-shipping list
        List<TntRdDetailAttachEx> onShippingList = this.findOnShippingList(param);

        // return to onShippingList
        if (stockStatusList != null && !stockStatusList.isEmpty()) {

            // reset
            stockStatusList = prepareStockStatusList(stockStatusList, lang);

            // loop current list
            TntStockStatusEx preStockStatus = null;
            for (TntStockStatusEx stockStatus : stockStatusList) {
                // defined
                List<TntRdAttachShipping> curOnShippingList = new ArrayList<TntRdAttachShipping>();

                // if exist
                if (onShippingList != null && !onShippingList.isEmpty()) {

                    // loop
                    for (TntRdDetailAttachEx onShipping : onShippingList) {

                        // if equals
                        if (onShipping.getStockStatusId().equals(stockStatus.getStockStatusId())) {
                            curOnShippingList.add(onShipping);
                        }
                    }
                }

                // set into current object
                stockStatus.setOnShippingList(curOnShippingList);

                // set has more supplier
                stockStatus.setHasMoreSupplier(false);
                // adjust
                if (preStockStatus != null && preStockStatus.getPartsId().equals(stockStatus.getPartsId())
                        && !preStockStatus.getSupplierId().equals(stockStatus.getSupplierId())) {

                    // same supplier parts
                    preStockStatus.setHasMoreSupplier(true);
                    stockStatus.setHasMoreSupplier(true);
                }

                // reset
                preStockStatus = stockStatus;
            }
        }

        // return
        return stockStatusList;
    }

    /**
     * Prepare Stock Status.
     *
     * @param stockStatusList Stock Status Information<As List>
     * @param lang Language
     * @return prepared stock status list
     */
    public List<TntStockStatusEx> prepareStockStatusList(List<TntStockStatusEx> stockStatusList, Language lang) {

        // define
        List<TntStockStatusEx> newStockList = new ArrayList<TntStockStatusEx>();
        String formatStr = lang.equals(Language.CHINESE) ? DateTimeUtil.FORMAT_YYYYMM : DateTimeUtil.FORMAT_MMMYYYY;

        // if not null
        if (stockStatusList != null) {

            // loop data and merge
            TntStockStatusEx newStockStatus = null;
            StringBuffer keySb = new StringBuffer();
            for (TntStockStatusEx stockStatus : stockStatusList) {

                // string key
                keySb.setLength(IntDef.INT_ZERO);
                keySb.append(StringConst.COMMA);
                keySb.append(stockStatus.getExpPartsId());
                keySb.append(StringConst.COMMA);

                // get flags
                String buildOutFlag = CodeCategoryManager.getCodeName(lang.getCode(),
                    CodeMasterCategory.BUILD_OUT_INDICATOR, stockStatus.getBuildOutFlagOrg());
                String inActiveFlag = CodeCategoryManager.getCodeName(lang.getCode(),
                    CodeMasterCategory.DISCONTINUE_INDICATOR, stockStatus.getInActiveFlagOrg());

                // compare
                if (newStockStatus != null && stockStatus.getStockStatusId().equals(newStockStatus.getStockStatusId())) {

                    // merge
                    if (stockStatus.getExpPartsSet().indexOf(keySb.toString()) >= IntDef.INT_ZERO) {

                        // supplier parts no.
                        if (newStockStatus.getSupplierPartsNo().indexOf(stockStatus.getSupplierPartsNo()) < IntDef.INT_ZERO) {
                            keySb.setLength(IntDef.INT_ZERO);
                            keySb.append(newStockStatus.getSupplierPartsNo());
                            keySb.append(StringConst.COMMA);
                            keySb.append(stockStatus.getSupplierPartsNo());
                            newStockStatus.setSupplierPartsNo(keySb.toString());
                        }

                        // buildOutFlag
                        if (newStockStatus.getBuildOutFlag().indexOf(buildOutFlag) < IntDef.INT_ZERO) {
                            keySb.setLength(IntDef.INT_ZERO);
                            keySb.append(newStockStatus.getBuildOutFlag());
                            keySb.append(StringConst.COMMA);
                            keySb.append(buildOutFlag);
                            newStockStatus.setBuildOutFlag(keySb.toString());
                        }

                        // buildOutMonth
                        if (stockStatus.getBuildOutMonth() != null) {
                            // get string
                            String buildOutMonth = DateTimeUtil.formatDate(formatStr, DateTimeUtil.FORMAT_YEAR_MONTH,
                                stockStatus.getBuildOutMonth(), lang);
                            if (newStockStatus.getBuildOutMonth().indexOf(buildOutMonth) < IntDef.INT_ZERO) {
                                keySb.setLength(IntDef.INT_ZERO);
                                keySb.append(newStockStatus.getBuildOutMonth());
                                keySb.append(StringConst.COMMA);
                                keySb.append(buildOutMonth);
                                newStockStatus.setBuildOutMonth(keySb.toString());
                            }
                        }

                        // lastOrderMonth
                        if (stockStatus.getLastOrderMonth() != null) {
                            // get string
                            String lastMonth = DateTimeUtil.formatDate(formatStr, DateTimeUtil.FORMAT_YEAR_MONTH,
                                stockStatus.getLastOrderMonth(), lang);
                            if (newStockStatus.getLastOrderMonth().indexOf(lastMonth) < IntDef.INT_ZERO) {
                                keySb.setLength(IntDef.INT_ZERO);
                                keySb.append(newStockStatus.getLastOrderMonth());
                                keySb.append(StringConst.COMMA);
                                keySb.append(lastMonth);
                                newStockStatus.setLastOrderMonth(keySb.toString());
                            }
                        }

                        // lastDeliveryMonth
                        if (stockStatus.getLastDeliveryMonth() != null) {
                            // get string
                            String deliMonth = DateTimeUtil.formatDate(formatStr, DateTimeUtil.FORMAT_YEAR_MONTH,
                                stockStatus.getLastDeliveryMonth(), lang);
                            // get
                            if (newStockStatus.getLastDeliveryMonth().indexOf(deliMonth) < IntDef.INT_ZERO) {
                                keySb.setLength(IntDef.INT_ZERO);
                                keySb.append(newStockStatus.getLastDeliveryMonth());
                                keySb.append(StringConst.COMMA);
                                keySb.append(deliMonth);
                                newStockStatus.setLastDeliveryMonth(keySb.toString());
                            }
                        }

                        // inActiveFlag
                        if (newStockStatus.getInActiveFlag().indexOf(inActiveFlag) < IntDef.INT_ZERO) {
                            keySb.setLength(IntDef.INT_ZERO);
                            keySb.append(newStockStatus.getInActiveFlag());
                            keySb.append(StringConst.COMMA);
                            keySb.append(inActiveFlag);
                            newStockStatus.setInActiveFlag(keySb.toString());
                        }

                        // remark
                        if (stockStatus.getPartsRemark() != null) {
                            keySb.setLength(IntDef.INT_ZERO);
                            keySb.append(newStockStatus.getPartsRemark());
                            keySb.append(StringConst.COMMA);
                            keySb.append(stockStatus.getPartsRemark());
                            newStockStatus.setPartsRemark(keySb.toString());
                        }
                    } else {
                        continue;
                    }

                } else {

                    // set into list
                    if (newStockStatus != null) {
                        newStockList.add(newStockStatus);
                    }

                    // reset
                    if (stockStatus.getExpPartsSet().indexOf(keySb.toString()) >= IntDef.INT_ZERO) {
                        // reset
                        newStockStatus = stockStatus;
                        // reset
                        newStockStatus.setBuildOutFlag(buildOutFlag);
                        newStockStatus.setInActiveFlag(inActiveFlag);
                        newStockStatus.setBuildOutMonth(DateTimeUtil.formatDate(formatStr,
                            DateTimeUtil.FORMAT_YEAR_MONTH, stockStatus.getBuildOutMonth(), lang));
                        newStockStatus.setLastOrderMonth(DateTimeUtil.formatDate(formatStr,
                            DateTimeUtil.FORMAT_YEAR_MONTH, stockStatus.getLastOrderMonth(), lang));
                        newStockStatus.setLastDeliveryMonth(DateTimeUtil.formatDate(formatStr,
                            DateTimeUtil.FORMAT_YEAR_MONTH, stockStatus.getLastDeliveryMonth(), lang));

                        // reset expbalance qty
                        if (newStockStatus.getBusinessPattern().equals(BusinessPattern.V_V)) {
                            newStockStatus.setExpBalanceQty(DecimalUtil.add(stockStatus.getExpPlanDelayQty(),
                                stockStatus.getExpFuturePlanQty()));
                        } else {
                            newStockStatus.setExpBalanceQty(stockStatus.getExpFuturePlanQty());
                        }

                        // totalImportStockQty
                        BigDecimal totalImportStockQty = DecimalUtil.add(stockStatus.getEciOnholdQty(),
                            stockStatus.getImpWhsQty());
                        totalImportStockQty = DecimalUtil.add(totalImportStockQty, stockStatus.getPreparedObQty());
                        newStockStatus.setTotalImportStockQty(totalImportStockQty);

                        // totalSupplyChainQty
                        BigDecimal totalSupplyChainQty = DecimalUtil.add(newStockStatus.getExpBalanceQty(),
                            newStockStatus.getExpStockQty());
                        totalSupplyChainQty = DecimalUtil.add(totalSupplyChainQty, stockStatus.getExpOnshippingQty());
                        totalSupplyChainQty = DecimalUtil.add(totalSupplyChainQty, stockStatus.getInRackQty());
                        totalSupplyChainQty = DecimalUtil.add(totalSupplyChainQty, totalImportStockQty);
                        totalSupplyChainQty = DecimalUtil.add(totalSupplyChainQty, stockStatus.getCustStockQty());
                        newStockStatus.setTotalSupplyChainQty(totalSupplyChainQty);

                        // totalImportStockQty
                        BigDecimal totalNotInRdQty = DecimalUtil.add(stockStatus.getEtdDelayQty(),
                            stockStatus.getInboundDelayQty());
                        totalNotInRdQty = DecimalUtil.add(totalNotInRdQty, stockStatus.getNgOnholdQty());
                        newStockStatus.setTotalNotInRdQty(totalNotInRdQty);
                    } else {
                        newStockStatus = null;
                        continue;
                    }
                }
            }

            // has data
            if (newStockStatus != null) {
                newStockList.add(newStockStatus);
            }
        }

        // return new stock list
        return newStockList;
    }

    /**
     * Replace the like conditon in parameter.
     *
     * @param param PageParam
     */
    public void makeLikeCondition(BaseParam param) {

        // build like condition
        StringUtil.buildLikeCondition(param, "ttcPartsNo");
        StringUtil.buildLikeCondition(param, "partsNameEn");
        StringUtil.buildLikeCondition(param, "partsNameCn");
        StringUtil.buildLikeCondition(param, "supplierPartsNo");
        StringUtil.buildLikeCondition(param, "oldPartsNo");
        StringUtil.buildLikeCondition(param, "custPartsNo");
        StringUtil.buildLikeCondition(param, "custBackNo");
        StringUtil.buildLikeCondition(param, "carModel");
        StringUtil.buildLikeCondition(param, "nextInvoice");
        StringUtil.buildLikeCondition(param, "rundownRemark");
        StringUtil.buildLikeCondition(param, "partsRemark");
    }
}
