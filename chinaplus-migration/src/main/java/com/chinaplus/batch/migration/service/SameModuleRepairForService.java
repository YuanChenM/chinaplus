/**
 * FixSameModuleNoService.java
 * 
 * @screen FixSameModuleNoService
 * @author cheng_xingfei
 */
package com.chinaplus.batch.migration.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.batch.migration.bean.MigrationComParam;
import com.chinaplus.batch.migration.bean.SameModuleRepairForEntity;
import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.util.DecimalUtil;

/**
 * 
 * FixSameModuleNoService.
 * 
 * @author cheng_xingfei
 */
public class SameModuleRepairForService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(SameModuleRepairForService.class);

    /**
     * Get max RundowMaster id.
     * 
     * @param param MigrationComParam
     * @throws InvocationTargetException IllegalAccessException
     * @throws IllegalAccessException IllegalAccessException
     * 
     */
    public void doFixSameModuleNo(MigrationComParam param) throws IllegalAccessException, InvocationTargetException {

        this.doFixSameModuleNoWithInvList(param);
    }
    

    /**
     * Get max RundowMaster id.
     * 
     * @param param MigrationComParam
     * @throws InvocationTargetException IllegalAccessException
     * @throws IllegalAccessException IllegalAccessException
     * @return invoice list
     */
    public List<SameModuleRepairForEntity> doFixSameModuleNoWithInvList(MigrationComParam param)
        throws IllegalAccessException, InvocationTargetException {

        logger.info("Start of fix same moduleNo patch");
        
        // get affected moduleNo
        List<SameModuleRepairForEntity> sameModuleNoList = this.getSameModuleNoList();
        // get same moduleNo invoiceNo
        List<SameModuleRepairForEntity> invoiceNoList = null;
        
        if (sameModuleNoList != null && sameModuleNoList.size() >= 0) {
            // refresh TNT_IP table
            this.doUpdateIp(sameModuleNoList);

            // get same moduleNo invoiceNo
            invoiceNoList = this.getInvoiceNoList(sameModuleNoList);
            
            if (invoiceNoList != null && invoiceNoList.size() > 0) {
                List<String> moduleNoList = new ArrayList<String>();
                for (SameModuleRepairForEntity entity : sameModuleNoList) {
                    moduleNoList.add(entity.getModuleNo());
                }
                
                List<String> invoiceNoForNoProcessList = new ArrayList<String>();
                for (SameModuleRepairForEntity entity : invoiceNoList) {
                    invoiceNoForNoProcessList.add(entity.getInvoiceNo());
                }
                
                SameModuleRepairForEntity ifInvoiceParam = new SameModuleRepairForEntity();
                ifInvoiceParam.setInvoiceNoList(invoiceNoForNoProcessList);
                ifInvoiceParam.setModuleNoList(moduleNoList);
                this.doUpdateIfExpInvoice(ifInvoiceParam);
            }
        }
        
        // get all InvoiceNo for qty error
        SameModuleRepairForEntity queryParam = new SameModuleRepairForEntity();
        queryParam.setOfficeId(param.getOfficeId());
        List<SameModuleRepairForEntity> invoiceNoListOfQtyError = this.getAllInvoiceOfQtyError(queryParam);
        List<SameModuleRepairForEntity> containerAndPartsNotMatched = this.getContainerAndPartsNotMatched(queryParam);
        
        if (invoiceNoList == null) {
            invoiceNoList = invoiceNoListOfQtyError;
        } else if (invoiceNoListOfQtyError != null && invoiceNoListOfQtyError.size() > 0){
            invoiceNoList.addAll(invoiceNoListOfQtyError);
        }
        
        if (invoiceNoList == null) {
            invoiceNoList = containerAndPartsNotMatched;
        } else if (containerAndPartsNotMatched != null && containerAndPartsNotMatched.size() > 0){
            invoiceNoList.addAll(containerAndPartsNotMatched);
        }

        if (invoiceNoList == null || invoiceNoList.size() == 0) {
            return new ArrayList<SameModuleRepairForEntity>();
        }
        
        List<SameModuleRepairForEntity> allInvoiceNoList = new ArrayList<SameModuleRepairForEntity>();
        Map<String, String> invoiceNoMap = new HashMap<String, String>();
        for (SameModuleRepairForEntity entity : invoiceNoList) {
            if (invoiceNoMap.containsKey(entity.getInvoiceNo())) {
                continue;
            } else {
                allInvoiceNoList.add(entity);
                invoiceNoMap.put(entity.getInvoiceNo(), entity.getInvoiceNo());
            }
        }
        
        Map<String, TntInvoiceSummary> invoiceSummaryMap = new HashMap<String, TntInvoiceSummary>();
        Map<String, List<SameModuleRepairForEntity>> invoiceContainerMap = new HashMap<String, List<SameModuleRepairForEntity>>();
        Map<String, List<SameModuleRepairForEntity>> invoicePartsMap = new HashMap<String, List<SameModuleRepairForEntity>>();
        
        Map<String, Integer> InvoiceSummaryIdMap = new HashMap<String, Integer>();
        
        logger.info("Processing total count : " + allInvoiceNoList.size());
        
        this.getAndMergeInvoiceData(allInvoiceNoList, invoiceSummaryMap, invoiceContainerMap, invoicePartsMap, InvoiceSummaryIdMap);
        
        this.doUpdateInvoiceTable(allInvoiceNoList, invoiceSummaryMap, invoiceContainerMap, invoicePartsMap, InvoiceSummaryIdMap);

        // end logger
        logger.info("End of fix same moduleNo patch");
        
        // return
        return invoiceNoList;
    }

    /**
     * doUpdateIfExpInvoice
     * 
     * @param ifInvoiceParam FixSameModuleNoEntity
     */
    private void doUpdateIfExpInvoice(SameModuleRepairForEntity ifInvoiceParam) {
        this.baseMapper.update(this.getSqlId("updateIfInvoiceNoFlag"), ifInvoiceParam);
    }

    /**
     * Get same moduleNo list
     * 
     * @return list
     */
    private List<SameModuleRepairForEntity> getSameModuleNoList() {

        // parameter
        SameModuleRepairForEntity entity = new SameModuleRepairForEntity();

        return baseMapper.selectList(this.getSqlId("getSameModuleNoList"), entity);
    }
    
    /**
     * doUpdateIp
     * 
     * @param moduleNoList List<FixSameModuleNoEntity>
     */
    private void doUpdateIp(List<SameModuleRepairForEntity> moduleNoList) {
        
        for (SameModuleRepairForEntity entity : moduleNoList) {
            
            // get all of the IP data for current moduleNo
            List<SameModuleRepairForEntity> errorIpDataList = baseMapper.selectList(this.getSqlId("getAllErrorIpDataByModuleNo"), entity);
            if (errorIpDataList == null || errorIpDataList.size() == 0) {
                continue;
            }
            
            for (SameModuleRepairForEntity errorIpData : errorIpDataList) {
                SameModuleRepairForEntity invoiceInfo = baseMapper.findOne(this.getSqlId("getInvoiceNo"), errorIpData);
                
                invoiceInfo.setIpId(errorIpData.getIpId());
                this.baseMapper.update(this.getSqlId("updateIpInvoiceNo"), invoiceInfo);
            }
        }
    }

    /**
     * getInvoiceNoList
     * 
     * @param sameModuleNoList List<FixSameModuleNoEntity>
     * @return List<FixSameModuleNoEntity>
     */
    private List<SameModuleRepairForEntity> getInvoiceNoList(List<SameModuleRepairForEntity> sameModuleNoList) {
        SameModuleRepairForEntity param = new SameModuleRepairForEntity();
        List<String> list = new ArrayList<String>();
        if (sameModuleNoList == null || sameModuleNoList.size() == 0) {
            return null;
        }
        for (SameModuleRepairForEntity entity : sameModuleNoList) {
            list.add(entity.getModuleNo());
        }
        param.setModuleNoList(list);
        
        return baseMapper.selectList(this.getSqlId("getAllInvoiceNoOfSameModule"), param);
    }

    /**
     * getAllInvoiceOfQtyError
     * 
     * @param param FixSameModuleNoEntity
     * @return List<FixSameModuleNoEntity>
     */
    private List<SameModuleRepairForEntity> getAllInvoiceOfQtyError(SameModuleRepairForEntity param) {
        
        return baseMapper.selectList(this.getSqlId("getAllInvoiceOfQtyError"), param);
    }

    /**
     * getAllInvoiceOfQtyError
     * 
     * @param param FixSameModuleNoEntity
     * @return List<FixSameModuleNoEntity>
     */
    private List<SameModuleRepairForEntity> getContainerAndPartsNotMatched(SameModuleRepairForEntity param) {
        
        return baseMapper.selectList(this.getSqlId("getContainerAndPartsNotMatched"), param);
    }

    /**
     * getAndMergeInvoiceData
     * 
     * @param invoiceNoList List<FixSameModuleNoEntity>
     * @param invoiceSummaryMap key: INVOICE_NO
     * @param invoiceContainerMap key: INVOICE_SUMMARY_ID + CONTAINER_NO + MODULE_NO + PARTS_ID
     * @param invoicePartsMap key: INVOICE_ID + PARTS_ID + SUPPLIER_ID
     * @param InvoiceSummaryIdMap key: invoiceNo, value: invoiceSummaryId
     */
    private void getAndMergeInvoiceData(List<SameModuleRepairForEntity> invoiceNoList,
        Map<String, TntInvoiceSummary> invoiceSummaryMap,
        Map<String, List<SameModuleRepairForEntity>> invoiceContainerMap, Map<String, List<SameModuleRepairForEntity>> invoicePartsMap, Map<String, Integer> InvoiceSummaryIdMap) {
        
        SameModuleRepairForEntity invoiceNoParam = new SameModuleRepairForEntity();
        
        List<String> invoiceNoListForString = new ArrayList<String>();
        for (SameModuleRepairForEntity entity : invoiceNoList) {
            invoiceNoListForString.add(entity.getInvoiceNo());
        }
        invoiceNoParam.setInvoiceNoList(invoiceNoListForString);
        List<SameModuleRepairForEntity> containerList = baseMapper.selectList(this.getSqlId("getInvoiceContainerQty"), invoiceNoParam);
        
        List<SameModuleRepairForEntity> invoicePartsList = baseMapper.selectList(this.getSqlId("getInvoicePartsQty"), invoiceNoParam);
        int index = 0;
        for (SameModuleRepairForEntity entity : invoiceNoList) {
            index++;
            logger.info("Get and calculation data : " + String.valueOf(index) + " : " + entity.getInvoiceNo());
            
            TntInvoiceSummary summaryParam = new TntInvoiceSummary();
            summaryParam.setInvoiceNo(entity.getInvoiceNo());
            
            TntInvoiceSummary summary = baseMapper.findOne(this.getSqlId("checkInvoiceSummaryExist"), summaryParam);
            if (summary == null) {
                logger.error("Find untreated invoice : [" + entity.getInvoiceNo() + "]!!!");
                continue;
            } else {
                InvoiceSummaryIdMap.put(summary.getInvoiceNo(), summary.getInvoiceSummaryId());
                
                // // prepare data for invoice summary
                // TntInvoiceSummary summaryQtyParam = new TntInvoiceSummary();
                // summaryQtyParam.setInvoiceNo(summary.getInvoiceNo());
                // TntInvoiceSummary invoiceSummaryQtyEntity = baseMapper.findOne(this.getSqlId("getInvoiceSummaryQty"), summaryQtyParam);
                // invoiceSummaryQtyEntity.setInvoiceSummaryId(summary.getInvoiceSummaryId());
                // invoiceSummaryQtyEntity.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                // invoiceSummaryMap.put(summary.getInvoiceNo(), invoiceSummaryQtyEntity);
                
                // prepare data for invoice container
                BigDecimal invoiceSummayQty = BigDecimal.ZERO;
//                List<TntInvoiceContainer> containerList = baseMapper.selectList(this.getSqlId("getInvoiceContainerQty"), summary);
                List<SameModuleRepairForEntity> containerListOfCurrentInvoice = new ArrayList<SameModuleRepairForEntity>();
                
                if (containerList != null && containerList.size() > 0) {
                    for (SameModuleRepairForEntity container : containerList) {
                        if (container.getInvoiceNo().equals(entity.getInvoiceNo())) {
                            container.setInvoiceSummaryId(summary.getInvoiceSummaryId());
                            if (entity.getTransportMode() != null && !"".equals(entity.getTransportMode())) {
                                if ("S".equals(entity.getTransportMode()) || "1".equals(String.valueOf(entity.getTransportMode()))) {
                                    container.setSealNo("a");
                                }

                                if ("A".equals(entity.getTransportMode()) || "2".equals(String.valueOf(entity.getTransportMode()))) {
                                    container.setSealNo("o");
                                }
                            }
                            container.setStatus(1);
                            container.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                            container.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                            invoiceSummayQty = DecimalUtil.add(invoiceSummayQty, container.getQty());
                            
                            containerListOfCurrentInvoice.add(container);
                        }
                    }
                    invoiceContainerMap.put(summary.getInvoiceNo(), containerListOfCurrentInvoice);
                }

                // prepare data for invoice summary
                TntInvoiceSummary invoiceSummaryQtyEntity = new TntInvoiceSummary();
                invoiceSummaryQtyEntity.setInvoiceQty(invoiceSummayQty);
                invoiceSummaryQtyEntity.setInvoiceSummaryId(summary.getInvoiceSummaryId());
                invoiceSummaryQtyEntity.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                invoiceSummaryMap.put(summary.getInvoiceNo(), invoiceSummaryQtyEntity);
                
                // prepare data for invoice parts
//                List<TntInvoicePart> invoicePartsList = baseMapper.selectList(this.getSqlId("getInvoicePartsQty"), summary);
                if (invoicePartsList != null && invoicePartsList.size() > 0) {
                    // get all invoice_id of the invoice
                    List<TntInvoice> invoiceList = baseMapper.selectList(this.getSqlId("getAllInvoiceId"), summary);
                    
                    List<SameModuleRepairForEntity> newInvoicePartsList = new ArrayList<SameModuleRepairForEntity>();
                    if (invoiceList != null && invoiceList.size() > 0) {
                        int invoiceCnt = invoiceList.size();
                        for (int i = 0; i < invoiceCnt; i++) {
                            Integer invoiceId = invoiceList.get(i).getInvoiceId();
                            for (SameModuleRepairForEntity invoiceParts : invoicePartsList) {
                                if (invoiceParts.getInvoiceNo().equals(summary.getInvoiceNo())) {
                                    SameModuleRepairForEntity part = new SameModuleRepairForEntity();
                                    part.setInvoiceId(invoiceId);
                                    part.setPartsId(invoiceParts.getPartsId());
                                    part.setSupplierId(invoiceParts.getSupplierId());
                                    part.setImpPoNo(invoiceParts.getImpPoNo());
                                    part.setCustomerOrderNo(invoiceParts.getCustomerOrderNo());
                                    part.setExpPoNo(invoiceParts.getExpPoNo());
                                    part.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    part.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    if (invoiceCnt == 1 || i == invoiceCnt - 1) {
                                        part.setOriginalQty(invoiceParts.getQty());
                                        part.setQty(invoiceParts.getQty());
                                    } else {
                                        part.setOriginalQty(invoiceParts.getQty());
                                        part.setQty(BigDecimal.ZERO);
                                    }
                                    newInvoicePartsList.add(part);
                                }
                            }
                        }
                    }
                    invoicePartsMap.put(summary.getInvoiceNo(), newInvoicePartsList);
                }
            }
        }
    }
    
    /**
     * doUpdateInvoiceTable
     * 
     * @param invoiceNoList List<FixSameModuleNoEntity>
     * @param invoiceSummaryMap key: INVOICE_NO
     * @param invoiceContainerMap key: INVOICE_SUMMARY_ID + CONTAINER_NO + MODULE_NO + PARTS_ID
     * @param invoicePartsMap key: INVOICE_ID + PARTS_ID + SUPPLIER_ID
     * @param InvoiceSummaryIdMap key: invoiceNo, value: invoiceSummaryId
     */
    private void doUpdateInvoiceTable(List<SameModuleRepairForEntity> invoiceNoList,
        Map<String, TntInvoiceSummary> invoiceSummaryMap,
        Map<String, List<SameModuleRepairForEntity>> invoiceContainerMap, Map<String, List<SameModuleRepairForEntity>> invoicePartsMap, Map<String, Integer> InvoiceSummaryIdMap) {
        int index = 0;
        for (SameModuleRepairForEntity entity : invoiceNoList) {
            index++;
            logger.info("Processing data : " + String.valueOf(index) + " : " + entity.getInvoiceNo());
            // update Tnt_Invoice_summary
            TntInvoiceSummary invoiceSummary = invoiceSummaryMap.get(entity.getInvoiceNo());
            if (invoiceSummary != null) {
                this.baseMapper.update(this.getSqlId("updateInvoiceSummary"), invoiceSummary);
            }
            
            // update Tnt_Invoice_container
            List<SameModuleRepairForEntity> invoiceContainerList = invoiceContainerMap.get(entity.getInvoiceNo());
            if (invoiceContainerList != null && invoiceContainerList.size() > 0) {
                this.insertOrUpdateInvoiceContainer(invoiceContainerList);
            }
            
            // update Tnt_Invoice_parts
            List<SameModuleRepairForEntity> invoicePartsList = invoicePartsMap.get(entity.getInvoiceNo());
            if (invoicePartsList != null && invoicePartsList.size() > 0) {
                this.insertOrUpdateInvoiceParts(invoicePartsList);
            }
        }
    }

    /**
     * insertOrUpdateInvoiceContainer
     * 
     * @param invoiceContainerList List<FixSameModuleNoEntity>
     */
    private void insertOrUpdateInvoiceContainer(List<SameModuleRepairForEntity> invoiceContainerList) {
        for (SameModuleRepairForEntity invoiceContainer : invoiceContainerList) {
            List<SameModuleRepairForEntity> containers = baseMapper.selectList(this.getSqlId("checkInvoiceContainerExist"), invoiceContainer);
            if (containers == null || containers.size() == 0) {
                // insert invoice container
                this.baseMapper.insert(this.getSqlId("insertTntInvoiceContainer"), invoiceContainer);
            } else if (containers.size() > 1){
                // delete invoice container
                this.baseMapper.delete(this.getSqlId("deleteTntInvoiceContainer"), invoiceContainer);
                // insert invoice container
                this.baseMapper.insert(this.getSqlId("insertTntInvoiceContainer"), invoiceContainer);
            } else if (containers.size() == 1) {
                // update invoice container
                this.baseMapper.update(this.getSqlId("updateTntInvoiceContainer"), invoiceContainer);
            }
        }
    }

    /**
     * insertOrUpdateInvoiceParts
     * 
     * @param invoicePartsList List<FixSameModuleNoEntity>
     */
    private void insertOrUpdateInvoiceParts(List<SameModuleRepairForEntity> invoicePartsList) {
        for (SameModuleRepairForEntity invoiceParts : invoicePartsList) {
            SameModuleRepairForEntity part = this.baseMapper.findOne(this.getSqlId("checkInvoicePartsExists"), invoiceParts);

            if (part == null) {
                this.baseMapper.insert(this.getSqlId("insertTntInvoiceParts"), invoiceParts);
            } else {
                this.baseMapper.update(this.getSqlId("updateTntInvoiceParts"), invoiceParts);
            }
        }
    }
}
