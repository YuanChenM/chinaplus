/**
 * Common logic for prepare Run down.
 * 
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.TntNotInRundownEx;
import com.chinaplus.common.bean.TntRdDetailAttachEx;
import com.chinaplus.common.bean.TntRundownDetailEx;
import com.chinaplus.common.bean.TntRundownHeader;
import com.chinaplus.common.bean.TntRundownMasterEx;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataType;
import com.chinaplus.common.entity.TntIfBatch;
import com.chinaplus.common.entity.TntRdAttachCfc;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;

/**
 * Common logic for Run down File.
 */
@Service
public class RundownService extends BaseService {

    /**
     * Get Run down list by condition
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntRundownMasterEx> findRundownMasterList(BaseParam param) {

        return super.baseMapper.select(getSqlId("findRundownMasterList"), param);
    }

    /**
     * Get Run down list by condition
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<Integer> findAllRundownMasterIds(BaseParam param) {

        return super.baseMapper.getSqlSession().selectList(getSqlId("findAllRundownMasterIds"), param);
    }

    /**
     * Get rundown header by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public TntRundownHeader findRundownMasterHeader(BaseParam param) {

        return super.baseMapper.findOne(getSqlId("findRundownMasterHeader"), param);
    }

    /**
     * Get not in run down list by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntNotInRundownEx> findNotInRundownList(BaseParam param) {

        return super.baseMapper.select(getSqlId("findNotInRundownList"), param);
    }

    /**
     * Get not in run down list by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntNotInRundownEx> findYetEtdListForSingle(BaseParam param) {

        return super.baseMapper.select(getSqlId("findYetEtdListForSingle"), param);
    }

    /**
     * Get not in run down list by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntNotInRundownEx> findYetInboundListForSingle(BaseParam param) {

        return super.baseMapper.select(getSqlId("findYetInboundListForSingle"), param);
    }

    /**
     * Get not in run down list by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntNotInRundownEx> findImpWhOnHoldListForSingle(BaseParam param) {

        return super.baseMapper.select(getSqlId("findImpWhOnHoldListForSingle"), param);
    }

    /**
     * Get not in run down list header by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntNotInRundownEx> findNotInRundownListForHeader(BaseParam param) {

        return super.baseMapper.select(getSqlId("findNotInRundownListForHeader"), param);
    }

    /**
     * Get share information list by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntRdAttachCfc> findShareInfoList(BaseParam param) {

        return super.baseMapper.select(getSqlId("findShareInfoList"), param);
    }

    /**
     * Get share information list header by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntRdAttachCfc> findShareInfoListForHeader(BaseParam param) {

        return super.baseMapper.select(getSqlId("findShareInfoListForHeader"), param);
    }

    /**
     * Get rundown detail by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntRundownDetailEx> findRundownDetailList(BaseParam param) {

        return super.baseMapper.select(getSqlId("findRundownDetailList"), param);
    }

    /**
     * Get shipping plan list by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntRdDetailAttachEx> findShippingPlanList(BaseParam param) {

        return super.baseMapper.select(getSqlId("findShippingPlanList"), param);
    }

    /**
     * Get shipping plan list header by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntRdDetailAttachEx> findShippingPlanListForHeader(BaseParam param) {

        return super.baseMapper.select(getSqlId("findShippingPlanListForHeader"), param);
    }

    /**
     * Get shipping plan list header by condition.
     *
     * @param param filters
     * @return Stock Status list
     */
    public List<TntRdDetailAttachEx> findShippingPlanListForSingle(BaseParam param) {

        return super.baseMapper.select(getSqlId("findShippingPlanListForSingle"), param);
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
     * get Rundown Title For All Parts.
     *
     * @param param filters
     * @return rundown header
     */
    public TntRundownHeader getRundownTitleForAll(BaseParam param) {

        // define
        TntRundownHeader header = this.findRundownMasterHeader(param);

        // has title
        if (header != null) {

            // get share list
            header.setShareList(this.findShareInfoListForHeader(param));
            // get not In Run-down List
            header.setNotInRundownList(this.findNotInRundownListForHeader(param));
            // get on-shipping information
            header.setRdDetailAttachList(this.findShippingPlanListForHeader(param));
        }

        // return
        return header;
    }

    /**
     * get run down detail information list For All Parts.
     *
     * @param param filters
     * @param lang Language
     * @return run down header
     */
    public List<TntRundownMasterEx> getRundownInfoListForAll(BaseParam param, Language lang) {

        // define
        List<TntRundownMasterEx> rundownList = new ArrayList<TntRundownMasterEx>();

        // get database data
        List<TntRundownMasterEx> dbRundownList = this.findRundownMasterList(param);

        // has title
        if (dbRundownList != null && !dbRundownList.isEmpty()) {

            // prepare rundown list
            rundownList = this.pickUpSupplierInfo(dbRundownList, lang);

            // get share list
            List<TntRdAttachCfc> shareInfoList = this.findShareInfoList(param);
            // get not In Run-down List
            List<TntNotInRundownEx> notInRunList = this.findNotInRundownList(param);
            // get rundown detail
            // set all parts flag
            param.setSwapData("allPartsFlag", true);
            List<TntRundownDetailEx> rdDetailList = this.findRundownDetailList(param);
            // get on-shipping information
            List<TntRdDetailAttachEx> onShippingList = this.findShippingPlanList(param);

            // defined index of each list
            int idxSi = IntDef.INT_ZERO;
            int idxNi = IntDef.INT_ZERO;
            int idxRd = IntDef.INT_ZERO;
            int idxOs = IntDef.INT_ZERO;
            int comp = IntDef.INT_ZERO;

            // set into list
            for (TntRundownMasterEx rundown : rundownList) {

                // set share list
                List<TntRdAttachCfc> newShareInfoList = new ArrayList<TntRdAttachCfc>();
                rundown.setShareInfoList(newShareInfoList);
                for (; idxSi < shareInfoList.size(); idxSi++) {

                    // get list
                    TntRdAttachCfc shareInfo = shareInfoList.get(idxSi);

                    // compare
                    comp = shareInfo.getRundownMasterId().compareTo(rundown.getRundownMasterId());
                    if (comp > IntDef.INT_ZERO) {
                        break;
                    } else if (comp < IntDef.INT_ZERO) {
                        continue;
                    }

                    // set into rundown
                    newShareInfoList.add(shareInfo);
                }

                // set not In Run-down List
                List<TntNotInRundownEx> newNotInRunList = new ArrayList<TntNotInRundownEx>();
                rundown.setNotInRundownList(newNotInRunList);
                for (; idxNi < notInRunList.size(); idxNi++) {

                    // get list
                    TntNotInRundownEx notInRun = notInRunList.get(idxNi);

                    // compare
                    comp = notInRun.getRundownMasterId().compareTo(rundown.getRundownMasterId());
                    if (comp > IntDef.INT_ZERO) {
                        break;
                    } else if (comp < IntDef.INT_ZERO) {
                        continue;
                    }

                    // set into rundown
                    newNotInRunList.add(notInRun);
                }

                // set not In Run-down List
                List<TntRundownDetailEx> newRdDetailList = new ArrayList<TntRundownDetailEx>();
                rundown.setRundownDetailList(newRdDetailList);
                // set run-down detail
                for (; idxRd < rdDetailList.size(); idxRd++) {

                    // get list
                    TntRundownDetailEx rdDetail = rdDetailList.get(idxRd);

                    // compare
                    comp = rdDetail.getRundownMasterId().compareTo(rundown.getRundownMasterId());
                    if (comp > IntDef.INT_ZERO) {
                        break;
                    } else if (comp < IntDef.INT_ZERO) {
                        continue;
                    }

                    // set into rundown
                    newRdDetailList.add(rdDetail);
                }

                // set on-shipping information
                List<TntRdDetailAttachEx> newOnShippingList = new ArrayList<TntRdDetailAttachEx>();
                rundown.setRdDetailAttachList(newOnShippingList);
                // set on-shipping detail
                for (; idxOs < onShippingList.size(); idxOs++) {

                    // get list
                    TntRdDetailAttachEx onShipping = onShippingList.get(idxOs);

                    // compare
                    comp = onShipping.getRundownMasterId().compareTo(rundown.getRundownMasterId());
                    if (comp > IntDef.INT_ZERO) {
                        break;
                    } else if (comp < IntDef.INT_ZERO) {
                        continue;
                    }

                    // set into rundown
                    newOnShippingList.add(onShipping);
                }
            }
        }

        // return
        return rundownList;
    }

    /**
     * get run down detail information list For Single Parts.
     *
     * @param param filters
     * @param lang Language
     * @return run down header
     */
    public List<TntRundownMasterEx> getRundownInfoListForSingle(BaseParam param, Language lang) {

        // define
        List<TntRundownMasterEx> rundownList = new ArrayList<TntRundownMasterEx>();

        // define
        List<TntRundownMasterEx> dbRundownList = this.findRundownMasterList(param);

        // has title
        if (dbRundownList != null && !dbRundownList.isEmpty()) {

            // prepare rundown list
            rundownList = this.pickUpSupplierInfo(dbRundownList, lang);

            // get not In Run-down List
            List<TntNotInRundownEx> yetEtdList = this.findYetEtdListForSingle(param);
            // get not In Run-down List
            List<TntNotInRundownEx> yetInboundList = this.findYetInboundListForSingle(param);
            // get not In Run-down List
            List<TntNotInRundownEx> impOnHoldList = this.findImpWhOnHoldListForSingle(param);
            // get rundown detail
            List<TntRundownDetailEx> rdDetailList = this.findRundownDetailList(param);
            // get on-shipping information
            List<TntRdDetailAttachEx> onShippingList = this.findShippingPlanListForSingle(param);

            // defined index of each list
            int idxYe = IntDef.INT_ZERO;
            int idxYi = IntDef.INT_ZERO;
            int idxIo = IntDef.INT_ZERO;
            int idxRd = IntDef.INT_ZERO;
            int idxOs = IntDef.INT_ZERO;
            int comp = IntDef.INT_ZERO;

            // set into list
            for (TntRundownMasterEx rundown : rundownList) {
                // set not In Run-down List
                List<TntNotInRundownEx> newYetEtdList = new ArrayList<TntNotInRundownEx>();
                rundown.setYetEtdList(newYetEtdList);
                for (; idxYe < yetEtdList.size(); idxYe++) {

                    // get list
                    TntNotInRundownEx notInRun = yetEtdList.get(idxYe);

                    // compare
                    comp = notInRun.getRundownMasterId().compareTo(rundown.getRundownMasterId());
                    if (comp > IntDef.INT_ZERO) {
                        break;
                    } else if (comp < IntDef.INT_ZERO) {
                        continue;
                    }

                    // set into rundown
                    newYetEtdList.add(notInRun);
                }

                // set not In Run-down List
                List<TntNotInRundownEx> newYetInboundList = new ArrayList<TntNotInRundownEx>();
                rundown.setYetInboundList(newYetInboundList);
                for (; idxYi < yetInboundList.size(); idxYi++) {

                    // get list
                    TntNotInRundownEx notInRun = yetInboundList.get(idxYi);

                    // compare
                    comp = notInRun.getRundownMasterId().compareTo(rundown.getRundownMasterId());
                    if (comp > IntDef.INT_ZERO) {
                        break;
                    } else if (comp < IntDef.INT_ZERO) {
                        continue;
                    }

                    // set into rundown
                    newYetInboundList.add(notInRun);
                }

                // set not In Run-down List
                List<TntNotInRundownEx> newImpOnHoldList = new ArrayList<TntNotInRundownEx>();
                rundown.setImpWhOnHoldList(newImpOnHoldList);
                for (; idxIo < impOnHoldList.size(); idxIo++) {

                    // get list
                    TntNotInRundownEx notInRun = impOnHoldList.get(idxIo);

                    // compare
                    comp = notInRun.getRundownMasterId().compareTo(rundown.getRundownMasterId());
                    if (comp > IntDef.INT_ZERO) {
                        break;
                    } else if (comp < IntDef.INT_ZERO) {
                        continue;
                    }

                    // set into rundown
                    newImpOnHoldList.add(notInRun);
                }

                // set not In Run-down List
                List<TntRundownDetailEx> newRdDetailList = new ArrayList<TntRundownDetailEx>();
                rundown.setRundownDetailList(newRdDetailList);
                // set run-down detail
                for (; idxRd < rdDetailList.size(); idxRd++) {

                    // get list
                    TntRundownDetailEx rdDetail = rdDetailList.get(idxRd);

                    // compare
                    comp = rdDetail.getRundownMasterId().compareTo(rundown.getRundownMasterId());
                    if (comp > IntDef.INT_ZERO) {
                        break;
                    } else if (comp < IntDef.INT_ZERO) {
                        continue;
                    }

                    // set on-shipping information
                    List<TntRdDetailAttachEx> newOnShippingList = new ArrayList<TntRdDetailAttachEx>();
                    rdDetail.setAttachedInfoList(newOnShippingList);
                    // set on-shipping detail
                    for (; idxOs < onShippingList.size(); idxOs++) {

                        // get list
                        TntRdDetailAttachEx onShipping = onShippingList.get(idxOs);

                        // compare
                        comp = onShipping.getRundownDetailId().compareTo(rdDetail.getRundownDetailId());
                        if (comp > IntDef.INT_ZERO) {
                            break;
                        } else if (comp < IntDef.INT_ZERO) {
                            continue;
                        }

                        // set into rundown
                        newOnShippingList.add(onShipping);
                    }

                    // set into rundown
                    newRdDetailList.add(rdDetail);
                }
            }
        }

        // return
        return rundownList;
    }

    /**
     * prepare supplier information for each parts.
     *
     * @param dbRundownList rundown list
     * @param lang language
     * @return the newest rundown list
     */
    private List<TntRundownMasterEx> pickUpSupplierInfo(List<TntRundownMasterEx> dbRundownList, Language lang) {

        // define
        List<TntRundownMasterEx> rundownList = new ArrayList<TntRundownMasterEx>();
        String formatStr = lang.equals(Language.CHINESE) ? DateTimeUtil.FORMAT_YYYYMM : DateTimeUtil.FORMAT_MMMYYYY;

        // set into list
        TntRundownMasterEx newRundown = null;
        StringBuffer keySb = new StringBuffer();
        for (TntRundownMasterEx rundown : dbRundownList) {

            // get flags
            String buildOutFlag = CodeCategoryManager.getCodeName(lang.getCode(),
                CodeMasterCategory.BUILD_OUT_INDICATOR, rundown.getBuildoutFlag());

            // string key
            keySb.setLength(IntDef.INT_ZERO);
            keySb.append(StringConst.COMMA);
            keySb.append(rundown.getExpPartsId());
            keySb.append(StringConst.COMMA);

            // merge supplier
            if (newRundown != null) {
                // set same information
                if (rundown.getRundownMasterId().equals(newRundown.getRundownMasterId())) {
                    // we need add
                    if (rundown.getExpPartsSet().indexOf(keySb.toString()) >= IntDef.INT_ZERO) {

                        // set supper code
                        if (newRundown.getTtcSupplierCode().indexOf(rundown.getTtcSupplierCode()) < IntDef.INT_ZERO) {
                            keySb.setLength(IntDef.INT_ZERO);
                            keySb.append(newRundown.getTtcSupplierCode());
                            keySb.append(StringConst.COMMA);
                            keySb.append(rundown.getTtcSupplierCode());
                            newRundown.setTtcSupplierCode(keySb.toString());
                        }

                        // EXP REGION
                        if (newRundown.getExpRegion().indexOf(rundown.getExpRegion()) < IntDef.INT_ZERO) {
                            keySb.setLength(IntDef.INT_ZERO);
                            keySb.append(newRundown.getExpRegion());
                            keySb.append(StringConst.COMMA);
                            keySb.append(rundown.getExpRegion());
                            newRundown.setExpRegion(keySb.toString());
                        }

                        // supplier parts no.
                        if (newRundown.getSupplierPartsNo().indexOf(rundown.getSupplierPartsNo()) < IntDef.INT_ZERO) {
                            keySb.setLength(IntDef.INT_ZERO);
                            keySb.append(newRundown.getSupplierPartsNo());
                            keySb.append(StringConst.COMMA);
                            keySb.append(rundown.getSupplierPartsNo());
                            newRundown.setSupplierPartsNo(keySb.toString());
                        }

                        // buildOutFlag
                        if (newRundown.getBuildoutFlagName().indexOf(buildOutFlag) < IntDef.INT_ZERO) {
                            keySb.setLength(IntDef.INT_ZERO);
                            keySb.append(newRundown.getBuildoutFlagName());
                            keySb.append(StringConst.COMMA);
                            keySb.append(buildOutFlag);
                            newRundown.setBuildoutFlagName(keySb.toString());
                        }

                        // buildOutMonth
                        if (rundown.getBuildoutMonth() != null) {
                            String strDate = DateTimeUtil.formatDate(formatStr, DateTimeUtil.FORMAT_YEAR_MONTH,
                                rundown.getBuildoutMonth(), lang);
                            if (newRundown.getBuildoutMonth().indexOf(strDate) < IntDef.INT_ZERO) {
                                keySb.setLength(IntDef.INT_ZERO);
                                keySb.append(newRundown.getBuildoutMonth());
                                keySb.append(StringConst.COMMA);
                                keySb.append(strDate);
                                newRundown.setBuildoutMonth(keySb.toString());
                            }
                        }

                        // lastOrderMonth
                        if (rundown.getLastPoMonth() != null) {
                            String strDate = DateTimeUtil.formatDate(formatStr, DateTimeUtil.FORMAT_YEAR_MONTH,
                                rundown.getLastPoMonth(), lang);
                            if (newRundown.getLastPoMonth().indexOf(strDate) < IntDef.INT_ZERO) {
                                keySb.setLength(IntDef.INT_ZERO);
                                keySb.append(newRundown.getLastPoMonth());
                                keySb.append(StringConst.COMMA);
                                keySb.append(strDate);
                                newRundown.setLastPoMonth(keySb.toString());
                            }
                        }

                        // remark
                        if (rundown.getPartsRemark() != null) {
                            keySb.setLength(IntDef.INT_ZERO);
                            keySb.append(newRundown.getPartsRemark());
                            keySb.append(StringConst.COMMA);
                            keySb.append(rundown.getPartsRemark());
                            newRundown.setPartsRemark(keySb.toString());
                        }
                    }
                    continue;
                }
            }

            // reset
            if (newRundown != null) {
                rundownList.add(newRundown);
            }

            // we need add
            if (rundown.getExpPartsSet().indexOf(keySb.toString()) >= IntDef.INT_ZERO) {

                // reset
                newRundown = rundown;

                // buildOutFlag
                newRundown.setBuildoutFlagName(buildOutFlag);

                // buildOutMonth
                newRundown.setBuildoutMonth(DateTimeUtil.formatDate(formatStr, DateTimeUtil.FORMAT_YEAR_MONTH,
                    rundown.getBuildoutMonth(), lang));

                // lastOrderMonth
                newRundown.setLastPoMonth(DateTimeUtil.formatDate(formatStr, DateTimeUtil.FORMAT_YEAR_MONTH,
                    rundown.getLastPoMonth(), lang));
            } else {
                newRundown = null;
                continue;
            }
        }

        // set
        if (newRundown != null) {
            rundownList.add(newRundown);
        }

        // return list
        return rundownList;
    }

}
