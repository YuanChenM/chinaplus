/**
 * CPIIFB16Service.java
 * 
 * @screen CPIIFB16
 * @author yang_jia1
 */
package com.chinaplus.batch.migration.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.batch.migration.bean.DevanPatchEntity;
import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.CodeConst.IpKbDataType;
import com.chinaplus.common.entity.TntIpKanban;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DecimalUtil;

/**
 * 
 * CPIIFB16Service.
 * 
 * @author yang_jia1
 */
public class DevanPatchService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(DevanPatchService.class);

    /**
     * Get max RundowMaster id.
     * 
     * @param officeCode officeCode
     * @throws InvocationTargetException IllegalAccessException
     * @throws IllegalAccessException IllegalAccessException
     * 
     */
    public void doDevanPatchSync(String officeCode) throws IllegalAccessException, InvocationTargetException {

        logger.info("Start of do imp Ip patch");

        // get all need do patched Ip information
        List<DevanPatchEntity> devanInfoList = this.getNeedReviseDevanInfoList(officeCode);
        Timestamp dataTime = super.getDBDateTimeByDefaultTimezone();

        // get process size
        logger.info("Size of Error Devan Information List: " + devanInfoList.size());

        // for each
        int count = 0;
        for (DevanPatchEntity devanInfo : devanInfoList) {

            // count up
            count++;

            // update
            this.reviseQtyForDevan(devanInfo);

            // check has inbound information
            if (DecimalUtil.isGreater(devanInfo.getQty(), BigDecimal.ZERO)) {

                // check has inbound or not
                int cnt = this.reviseQtyForImpInbound(devanInfo);
                logger.info("Update inbound count: " + cnt);

                // check outbound information or not
                List<DevanPatchEntity> otherInfo = this.getPidInfoForInboundList(devanInfo);

                // check size
                if (otherInfo != null && !otherInfo.isEmpty()) {
                    // logger
                    logger.info("Outbound count: " + otherInfo.size());
                    // loop
                    for (DevanPatchEntity outboundInfo : otherInfo) {

                        // other information (now only has outbound)
                        TntIpKanban param = new TntIpKanban();
                        param.setPidNo(outboundInfo.getPidNo());
                        param.setDataType(IpKbDataType.INBOUND);

                        // insert into
                        List<TntIpKanban> ipkanbanList = baseDao.select(param);

                        // new next one
                        for (TntIpKanban kanban : ipkanbanList) {

                            // check QTY
                            if (DecimalUtil.isZero(kanban.getQty())) {
                                continue;
                            }

                            // data
                            TntIpKanban obKanban = new TntIpKanban();

                            // copy
                            // BeanUtils.copyProperties(kanban, obKanban);

                            // reset
                            obKanban.setIfIpId(outboundInfo.getIfIpId());
                            obKanban.setDataType(IpKbDataType.OUTBOUND);

                            // set
                            obKanban.setPidNo(kanban.getPidNo());
                            obKanban.setKanbanPlanNo(kanban.getKanbanPlanNo());
                            obKanban.setInvoiceNo(kanban.getInvoiceNo());
                            obKanban.setContainerNo(kanban.getContainerNo());
                            obKanban.setModuleNo(kanban.getModuleNo());
                            obKanban.setPartsId(kanban.getPartsId());
                            obKanban.setSupplierId(kanban.getSupplierId());
                            obKanban.setExpPartsId(kanban.getExpPartsId());
                            obKanban.setQty(kanban.getQty());
                            obKanban.setSaQty(kanban.getSaQty());

                            // set information
                            obKanban.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                            obKanban.setCreatedDate(dataTime);
                            obKanban.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                            obKanban.setUpdatedDate(dataTime);
                            obKanban.setVersion(IntDef.INT_ONE);

                            // save
                            baseDao.insert(obKanban);
                        }
                    }
                    // set
                    baseDao.flush();
                    baseDao.clear();
                }
            }

            // logger
            logger.info("Already process: " + count);
        }
        
        // update tnf_imp_stock
        this.reviseImpStockQty();

        // check imp stock by day
        List<DevanPatchEntity> dayImpStockList = this.getImpStockByDayList();
        if (dayImpStockList != null && !dayImpStockList.isEmpty()) {
            // update
            for (DevanPatchEntity dayImpStock : dayImpStockList) {
                this.reviseImpStockByDay(dayImpStock);
            }

            // delete
            this.deleteAllDayQty();
        }

        // end logger
        logger.info("End of do Error Devan Information patch");
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param officeCode officeCode
     * @return list
     */
    private List<DevanPatchEntity> getNeedReviseDevanInfoList(String officeCode) {

        // parameter
        DevanPatchEntity mgImpIpInfo = new DevanPatchEntity();
        mgImpIpInfo.setOfficeCode(officeCode);

        return baseMapper.selectList(this.getSqlId("getNeedReviseDevanInfo"), mgImpIpInfo);
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @param devanInfo devanInfo
     * @return list
     */
    private List<DevanPatchEntity> getPidInfoForInboundList(DevanPatchEntity devanInfo) {

        // parameter
        return baseMapper.selectList(this.getSqlId("getPidInfoForInbound"), devanInfo);
    }

    /**
     * Get Invoice Parts Information By Invoice No.
     * 
     * @return list
     */
    private List<DevanPatchEntity> getImpStockByDayList() {
        DevanPatchEntity devanInfo = new DevanPatchEntity();
        // parameter
        return baseMapper.selectList(this.getSqlId("getImpStockByDayList"), devanInfo);
    }

    /**
     * Save into if imp ip information.
     * 
     * @param devanInfo devanInfo
     * @return index
     */
    private int reviseQtyForDevan(DevanPatchEntity devanInfo) {

        // return
        return this.baseMapper.update(this.getSqlId("reviseQtyForDevan"), devanInfo);
    }

    /**
     * Save into if imp ip information.
     * 
     * @param devanInfo devanInfo
     * @return index
     */
    private int reviseQtyForImpInbound(DevanPatchEntity devanInfo) {

        // return
        return this.baseMapper.update(this.getSqlId("reviseQtyForImpInbound"), devanInfo);
    }

    /**
     * Save into if imp ip information.
     * 
     * @return index
     */
    private int reviseImpStockQty() {
        DevanPatchEntity devanInfo = new DevanPatchEntity();

        // return
        return this.baseMapper.update(this.getSqlId("reviseImpStockQty"), devanInfo);
    }

    /**
     * Save into if imp ip information.
     * 
     * @param devanInfo devanInfo
     * @return index
     */
    private int reviseImpStockByDay(DevanPatchEntity devanInfo) {

        // return
        return this.baseMapper.update(this.getSqlId("reviseImpStockByDay"), devanInfo);
    }

    /**
     * Save into if imp ip information.
     * 
     * @return index
     */
    private int deleteAllDayQty() {
        DevanPatchEntity devanInfo = new DevanPatchEntity();
        // return
        return this.baseMapper.delete(this.getSqlId("deleteAllDayQty"), devanInfo);
    }

}
