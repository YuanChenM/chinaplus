/**
 * CPIIFB16Service.java
 * 
 * @screen CPIIFB16
 * @author yang_jia1
 */
package com.chinaplus.batch.migration.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.migration.bean.TntMgInvoiceEx;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.InvoiceContainerStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceGroupStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceStatus;
import com.chinaplus.common.consts.CodeConst.InvoiceType;
import com.chinaplus.common.consts.CodeConst.InvoiceUploadStatus;
import com.chinaplus.common.consts.CodeConst.PostRiFlag;
import com.chinaplus.common.consts.CodeConst.TransportMode;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceContainer;
import com.chinaplus.common.entity.TntInvoiceGroup;
import com.chinaplus.common.entity.TntInvoicePart;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * CPIIFB16Service.
 * 
 * @author yang_jia1
 */
@Service
public class InvoiceSyncService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(InvoiceSyncService.class);

    /** logger */
    private Map<String, Integer> summaryIdMaps = new HashMap<String, Integer>();

    /** Map */
    private Map<String, Integer> groupIdMaps = new HashMap<String, Integer>();

    /** logger */
    private Map<String, Integer> invMainMaps = new HashMap<String, Integer>();

    /** logger */
    private Map<String, Integer> invPartsMaps = new HashMap<String, Integer>();
    
    /** The InvoiceSyncService service */
    @Autowired
    private CPVIVF11Service cpvivf11Service;

    /**
     * do prepare invoice main information for all invoice.
     * 
     * @param officeId officeId
     */
    public void doPrepareInvoiceInformation(Integer officeId) {

        // get office information
        TnmOffice office = super.baseDao.findOne(TnmOffice.class, officeId);
        // get all times
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();
        Timestamp offTime = super.getDBDateTime(office.getTimeZone());

        // get invoice main information
        List<TntMgInvoiceEx> invPartsMainInfoList = this.getInvociePartsInfoList(officeId);

        // defined each list
        List<TntInvoiceSummary> invSummaryList = new ArrayList<TntInvoiceSummary>();
        List<TntInvoiceGroup> invGroupList = new ArrayList<TntInvoiceGroup>();
        List<TntInvoiceContainer> invContainerList = new ArrayList<TntInvoiceContainer>();
        List<TntInvoice> invMainInfoList = new ArrayList<TntInvoice>();
        List<TntInvoicePart> invPartsInfoList = new ArrayList<TntInvoicePart>();

        // loop each parts, and then set invoice table
        for (TntMgInvoiceEx selInvMainInfo : invPartsMainInfoList) {

            // TNT_INVOICE_SUMMARY
            this.doPrepareInvoiceSummayInfo(invSummaryList, selInvMainInfo);

            // TNT_INVOICE_GROUP
            this.doPrepareInvoiceGroupInfo(invGroupList, selInvMainInfo);

            // TNT_INVOICE_CONTAINER
            this.doPrepareInvoiceContInfo(invContainerList, selInvMainInfo);

            // TNT_INVOICE
            this.doPrepareInvoiceMainInfo(invMainInfoList, selInvMainInfo);

            // TNT_INVOICE_PARTS
            this.doPrepareInvoicePartsInfo(invPartsInfoList, selInvMainInfo);
        }

        // save datas
        // TNT_INVOICE_SUMMARY
        this.doInvoiceSummarySave(invSummaryList, offTime, dbTime);
        // TNT_INVOICE_GROUP
        this.doInvoiceGroupSave(invGroupList, dbTime);
        // TNT_INVOICE_CONTAINER
        this.doInvoiceContainerSave(invSummaryList, invContainerList, dbTime);
        // TNT_INVOICE
        this.doInvoiceMainSave(invSummaryList, invGroupList, invMainInfoList, dbTime);
        // TNT_INVOICE_PARTS
        this.doInvoicePartsSave(invMainInfoList, invPartsInfoList, dbTime);
        
        // flush
        super.baseDao.flush();

        // do update TNF_ORDER_STATUS
        /*this.doPrepareOrderStatusInfo(officeId, dbTime); */
        cpvivf11Service.doPlanAdjust(officeId, dbTime);

    }

    /**
     * do prepare order status information.
     * 
     * @param officeId officeId
     * @param dbTime dbTime
     */
   /* private void doPrepareOrderStatusInfo(Integer officeId, Timestamp dbTime) {

        // get all QTY from invoice parts
        List<TntMgInvoiceEx> orderStatusInfoList = this.getOrderStatusInfoList(officeId);

        // loop update
        for (TntMgInvoiceEx orderStatusInfo : orderStatusInfoList) {
            
            // set update information
            orderStatusInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
            orderStatusInfo.setUpdatedDate(dbTime);
            
            // update
            super.baseMapper.update(this.getSqlId("doUpdateOrderStatus"), orderStatusInfo);
        }
    }*/

    /**
     * do prepare invoice summary information list.
     * 
     * @param invSummaryList invSummaryList
     * @param selInvMainInfo selInvMainInfo
     */
    private void doPrepareInvoiceSummayInfo(List<TntInvoiceSummary> invSummaryList, TntMgInvoiceEx selInvMainInfo) {

        // define
        TntInvoiceSummary invSummaryInfo = null;
        if (invSummaryList.size() > IntDef.INT_ZERO) {
            invSummaryInfo = invSummaryList.get(invSummaryList.size() - IntDef.INT_ONE);
        }
        // check
        if (invSummaryInfo == null || !invSummaryInfo.getInvoiceNo().equalsIgnoreCase(selInvMainInfo.getInvoiceNo())) {

            // add new invSummaryIfo
            invSummaryInfo = new TntInvoiceSummary();
            // copy to invoice summary information
            this.copyAndSetInvSummaryInfo(selInvMainInfo, invSummaryInfo);
            // set into list
            invSummaryList.add(invSummaryInfo);
            // set values
            summaryIdMaps.put(invSummaryInfo.getInvoiceNo(), invSummaryList.size() - IntDef.INT_ONE);
        } else {
            
            // check is contains or not
            if(invSummaryInfo.getSupplierCodeSet().indexOf(selInvMainInfo.getSupplierCode()) < 0) {
                // summary qty and supplier code
                StringBuffer codeSet = new StringBuffer();
                codeSet.append(invSummaryInfo.getSupplierCodeSet());
                codeSet.append(StringConst.COMMA);
                codeSet.append(selInvMainInfo.getSupplierCode());
                // set into entity
                invSummaryInfo.setSupplierCodeSet(codeSet.toString());
            }
            // summary information
            invSummaryInfo.setInvoiceQty(DecimalUtil.add(invSummaryInfo.getInvoiceQty(), selInvMainInfo.getQty()));
        }
    }

    /**
     * do prepare invoice container information list.
     * 
     * @param invContainerList invContainerList
     * @param selInvMainInfo selInvMainInfo
     */
    private void doPrepareInvoiceContInfo(List<TntInvoiceContainer> invContainerList, TntMgInvoiceEx selInvMainInfo) {

        // define
        TntInvoiceContainer invContInfo = null;
        if (invContainerList.size() > IntDef.INT_ZERO) {
            invContInfo = invContainerList.get(invContainerList.size() - IntDef.INT_ONE);
        }
        // check
        if (invContInfo == null
                || !this.makeInvContainerKey(invContInfo).equals(this.makeInvContainerKey(selInvMainInfo))) {

            // add new invSummaryIfo
            invContInfo = new TntInvoiceContainer();
            // copy to invoice container information
            this.copyAndSetInvContainerInfo(selInvMainInfo, invContInfo);
            // set into list
            invContainerList.add(invContInfo);
        } else {

            // summary information
            invContInfo.setQty(DecimalUtil.add(invContInfo.getQty(), selInvMainInfo.getQty()));
        }
    }

    /**
     * do prepare invoice group information list.
     * 
     * @param invGroupList invGroupList
     * @param selInvMainInfo selInvMainInfo
     */
    private void doPrepareInvoiceGroupInfo(List<TntInvoiceGroup> invGroupList, TntMgInvoiceEx selInvMainInfo) {
        // check
        String groupKey = this.makeInvoiceGroupKey(selInvMainInfo);
        if (!groupIdMaps.containsKey(groupKey)) {

            // add new invSummaryIfo
            TntInvoiceGroup invGroupInfo = new TntInvoiceGroup();
            // copy to invoice group information
            this.copyAndSetInvGroupInfo(selInvMainInfo, invGroupInfo);
            // set into list
            invGroupList.add(invGroupInfo);
            // set values
            groupIdMaps.put(groupKey, invGroupList.size() - IntDef.INT_ONE);
        }
    }

    /**
     * do prepare invoice main information list.
     * 
     * @param invMainInfoList invMainInfoList
     * @param selInvMainInfo selInvMainInfo
     */
    private void doPrepareInvoiceMainInfo(List<TntInvoice> invMainInfoList, TntMgInvoiceEx selInvMainInfo) {
        // check
        String mainKey = this.makeInvoiceMainInfoKey(selInvMainInfo);
        if (!invMainMaps.containsKey(mainKey)) {

            // add new invSummaryIfo
            TntInvoice invMainInfo = new TntInvoice();
            // copy to invoice main information
            this.copyAndSetInvMainInfo(selInvMainInfo, invMainInfo);
            // set into list
            invMainInfoList.add(invMainInfo);
            // set values
            invMainMaps.put(mainKey, invMainInfoList.size() - IntDef.INT_ONE);
        }
    }

    /**
     * do prepare invoice parts information list.
     * 
     * @param invPartsInfoList invPartsInfoList
     * @param selInvMainInfo selInvMainInfo
     */
    private void doPrepareInvoicePartsInfo(List<TntInvoicePart> invPartsInfoList, TntMgInvoiceEx selInvMainInfo) {

        // define
        TntInvoicePart invPartsInfo = null;
        // get from map
        String key = this.makeInvoicePartsInfoKey(selInvMainInfo);

        if (invPartsMaps.containsKey(key)) {
            int index = invPartsMaps.get(key);
            invPartsInfo = invPartsInfoList.get(index);
            // summary qty information
            invPartsInfo.setQty(DecimalUtil.add(invPartsInfo.getQty(), selInvMainInfo.getQty()));
            invPartsInfo.setOriginalQty(invPartsInfo.getQty());
        } else {
            // add new invSummaryIfo
            invPartsInfo = new TntInvoicePart();
            // copy to invoice parts information
            this.copyAndSetInvPartsInfo(selInvMainInfo, invPartsInfo);
            // set into list
            invPartsInfoList.add(invPartsInfo);
            // key
            invPartsMaps.put(key, (invPartsInfoList.size() - IntDef.INT_ONE));
        }
    }

    /**
     * copy and create a new invoice information.
     * 
     * @param selInvPartsMainInfo selInvPartsMainInfo
     * @param invSummaryInfo invSummaryInfo
     */
    private void copyAndSetInvSummaryInfo(TntMgInvoiceEx selInvPartsMainInfo, TntInvoiceSummary invSummaryInfo) {

        // copy and set information
        // OFFICE_ID
        invSummaryInfo.setOfficeId(selInvPartsMainInfo.getOfficeId());
        // BUSINESS_PATTERN
        invSummaryInfo.setBusinessPattern(BusinessPattern.AISIN);
        // INVOICE_NO
        invSummaryInfo.setInvoiceNo(selInvPartsMainInfo.getInvoiceNo());
        // INVOICE_TYPE
        invSummaryInfo.setInvoiceType(InvoiceType.AISIN);
        // VESSEL_NAME
        invSummaryInfo.setVesselName(selInvPartsMainInfo.getVesselName());
        // EXP_REGION
        invSummaryInfo.setExpRegion(selInvPartsMainInfo.getExpRegion());
        // IMP_REGION
        invSummaryInfo.setImpRegion(selInvPartsMainInfo.getImpRegion());
        // TRANSPORT_MODE
        invSummaryInfo.setTransportMode(selInvPartsMainInfo.getTransportMode());
        // SUPPLIER_CODE_SET
        invSummaryInfo.setSupplierCodeSet(selInvPartsMainInfo.getSupplierCode());
        // ETD
        invSummaryInfo.setEtd(selInvPartsMainInfo.getEtd());
        // ETA
        invSummaryInfo.setEta(selInvPartsMainInfo.getEta());
        // VANNING_DATE
        invSummaryInfo.setVanningDate(null);
        // INVOICE_QTY
        invSummaryInfo.setInvoiceQty(selInvPartsMainInfo.getQty());
        // INBOUND_QTY
        invSummaryInfo.setInboundQty(BigDecimal.ZERO);
        // GR_DATE
        invSummaryInfo.setGrDate(null);
        // GI_DATE
        invSummaryInfo.setGiDate(null);
        // POST_RI_FLAG
        invSummaryInfo.setPostRiFlag(PostRiFlag.N);
        // INVOICE_STATUS
        invSummaryInfo.setInvoiceStatus(InvoiceStatus.PENDING);
        // NON_SHIPPING_ROUTE
        // UPLOAD_ID
        invSummaryInfo.setUploadId(StringUtil.genUploadId("InvoiceBatch"));
        // UPLOADED_BY
        invSummaryInfo.setUploadedBy(BatchConst.BATCH_USER_ID);
        // UPLOAD_STATUS
        invSummaryInfo.setUploadStatus(InvoiceUploadStatus.DONE);
        // ISSUE_TYPE
        invSummaryInfo.setIssueType(null);
    }

    /**
     * copy and create a new invoice information.
     * 
     * @param selInvPartsInfo selInvPartsInfo
     * @param invContainerInfo invContainerInfo
     */
    private void copyAndSetInvContainerInfo(TntMgInvoiceEx selInvPartsInfo, TntInvoiceContainer invContainerInfo) {

        // copy and set information
        // INVOICE_SUMMARY_ID
        invContainerInfo.setInvoiceSummaryId(summaryIdMaps.get(selInvPartsInfo.getInvoiceNo()));
        // CONTAINER_NO
        invContainerInfo.setContainerNo(selInvPartsInfo.getContainerNo());
        // SEAL_NO
        if (selInvPartsInfo.getTransportMode().equals(TransportMode.AIR)) {
            invContainerInfo.setSealNo("o");
        } else {
            invContainerInfo.setSealNo("a");
        }
        // MODULE_NO
        invContainerInfo.setModuleNo(selInvPartsInfo.getModuleNo());
        // PARTS_ID
        invContainerInfo.setPartsId(selInvPartsInfo.getPartsId());
        // INVOICE_PARTS_NO
        invContainerInfo.setInvoicePartsNo(selInvPartsInfo.getSuppPartsNo());
        // QTY
        invContainerInfo.setQty(selInvPartsInfo.getQty());
        // BUYING_PRICE
        invContainerInfo.setBuyingPrice(selInvPartsInfo.getBuyingPrice());
        // BUYING_CURRENCY
        invContainerInfo.setBuyingCurrency(selInvPartsInfo.getBuyingCurrency());
        // CC_DATE
        invContainerInfo.setCcDate(null);
        // DEVANNED_DATE
        invContainerInfo.setDevannedDate(null);
        // STOCK_QTY ==> null
        // STATUS
        invContainerInfo.setStatus(InvoiceContainerStatus.ON_SHIPPING);
    }

    /**
     * copy and create a new invoice information.
     * 
     * @param selInvPartsInfo selInvPartsInfo
     * @param invGroupInfo invGroupInfo
     */
    private void copyAndSetInvGroupInfo(TntMgInvoiceEx selInvPartsInfo, TntInvoiceGroup invGroupInfo) {

        // copy and set information
        // KANBAN_PLAN_NO
        invGroupInfo.setKanbanPlanNo(selInvPartsInfo.getKanbanPlanNo());
        // TRANSPORT_MODE
        invGroupInfo.setTransportMode(selInvPartsInfo.getTransportMode());
        // VESSEL_NAME
        invGroupInfo.setVesselName(selInvPartsInfo.getVesselName());
        // ETD
        invGroupInfo.setEtd(selInvPartsInfo.getEtd());
        // ETA
        invGroupInfo.setEta(selInvPartsInfo.getEta());
        // IMP_INB_PLAN_DATE
        invGroupInfo.setImpInbPlanDate(selInvPartsInfo.getPlanIbDate());
        // STATUS
        invGroupInfo.setStatus(InvoiceGroupStatus.NORMAL);
    }

    /**
     * copy and create a new invoice information.
     * 
     * @param selInvPartsInfo selInvPartsInfo
     * @param invMainInfo invMainInfo
     */
    private void copyAndSetInvMainInfo(TntMgInvoiceEx selInvPartsInfo, TntInvoice invMainInfo) {

        // copy and set information
        // INVOICE_GROUP_ID
        invMainInfo.setInvoiceGroupId(groupIdMaps.get(this.makeInvoiceGroupKey(selInvPartsInfo)));
        // INVOICE_SUMMARY_ID
        invMainInfo.setInvoiceSummaryId(summaryIdMaps.get(selInvPartsInfo.getInvoiceNo()));
        // OFFICE_ID
        invMainInfo.setOfficeId(selInvPartsInfo.getOfficeId());
        // INVOICE_NO
        invMainInfo.setInvoiceNo(selInvPartsInfo.getInvoiceNo());
        // ORIGINAL_VERSION
        invMainInfo.setOriginalVersion(null);
        // REVISION_VERSION
        invMainInfo.setRevisionVersion(IntDef.INT_ONE);
        // REVISION_REASON
        invMainInfo.setRevisionReason(null);
        // VANNING_DATE
        invMainInfo.setVanningDate(null);
        // ETD
        invMainInfo.setEtd(selInvPartsInfo.getEtd());
        // ETA
        invMainInfo.setEta(selInvPartsInfo.getEta());
        // CC_DATE
        invMainInfo.setCcDate(null);
        // IMP_INB_PLAN_DATE
        invMainInfo.setImpInbPlanDate(selInvPartsInfo.getPlanIbDate());
        // IMP_CC_ACTUAL_DATE
        invMainInfo.setImpCcActualDate(null);
        // IMP_INB_ACTUAL_DATE
        invMainInfo.setImpInbActualDate(null);
    }

    /**
     * copy and create a new invoice information.
     * 
     * @param selInvPartsInfo selInvPartsInfo
     * @param invPartsInfo invPartsInfo
     */
    private void copyAndSetInvPartsInfo(TntMgInvoiceEx selInvPartsInfo, TntInvoicePart invPartsInfo) {

        // copy and set information
        // INVOICE_ID
        invPartsInfo.setInvoiceId(invMainMaps.get(this.makeInvoiceMainInfoKey(selInvPartsInfo)));
        // PARTS_ID
        invPartsInfo.setPartsId(selInvPartsInfo.getPartsId());
        // SUPPLIER_ID
        invPartsInfo.setSupplierId(selInvPartsInfo.getSupplierId());
        // SUPPLIER_PARTS_NO
        invPartsInfo.setSupplierPartsNo(selInvPartsInfo.getSuppPartsNo());
        // IMP_PO_NO
        invPartsInfo.setImpPoNo(null);
        // CUSTOMER_ORDER_NO
        invPartsInfo.setCustomerOrderNo(null);
        // EXP_PO_NO
        invPartsInfo.setExpPoNo(null);
        // INV_CUST_CODE
        invPartsInfo.setInvCustCode(selInvPartsInfo.getInvCustCode());
        // ORIGINAL_QTY
        invPartsInfo.setOriginalQty(selInvPartsInfo.getQty());
        // QTY
        invPartsInfo.setQty(selInvPartsInfo.getQty());
    }

    /**
     * prepare invoice container information.
     * 
     * @param invContInfo invContInfo
     * @return key
     */
    private String makeInvContainerKey(BaseEntity invContInfo) {

        // define
        StringBuffer contBuff = new StringBuffer();

        // make key
        if (invContInfo instanceof TntInvoiceContainer) {
            // cast
            TntInvoiceContainer castInvContInfo = (TntInvoiceContainer) invContInfo;
            contBuff.append(castInvContInfo.getInvoiceSummaryId());
            contBuff.append(StringConst.NEW_COMMA);
            contBuff.append(castInvContInfo.getContainerNo());
            contBuff.append(StringConst.NEW_COMMA);
            contBuff.append(castInvContInfo.getModuleNo());
            contBuff.append(StringConst.NEW_COMMA);
            contBuff.append(castInvContInfo.getPartsId());
        } else {
            // cast
            TntMgInvoiceEx castInvContInfo = (TntMgInvoiceEx) invContInfo;
            contBuff.append(summaryIdMaps.get(castInvContInfo.getInvoiceNo()));
            contBuff.append(StringConst.NEW_COMMA);
            contBuff.append(castInvContInfo.getContainerNo());
            contBuff.append(StringConst.NEW_COMMA);
            contBuff.append(castInvContInfo.getModuleNo());
            contBuff.append(StringConst.NEW_COMMA);
            contBuff.append(castInvContInfo.getPartsId());
        }

        return contBuff.toString();
    }

    /**
     * prepare invoice container information.
     * 
     * @param invGroupInfo invGroupInfo
     * @return key
     */
    private String makeInvoiceGroupKey(TntMgInvoiceEx invGroupInfo) {

        // define
        StringBuffer contBuff = new StringBuffer();

        // make key
        contBuff.append(invGroupInfo.getKanbanPlanNo());
        contBuff.append(StringConst.NEW_COMMA);
        contBuff.append(invGroupInfo.getTransportMode());
        contBuff.append(StringConst.NEW_COMMA);
        if (!StringUtil.isEmpty(invGroupInfo.getVesselName())) {
            contBuff.append(invGroupInfo.getVesselName());
        } else {
            contBuff.append(StringConst.EMPTY);
        }
        contBuff.append(StringConst.NEW_COMMA);
        contBuff.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMM, invGroupInfo.getEtd()));
        contBuff.append(StringConst.NEW_COMMA);
        contBuff.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMM, invGroupInfo.getEta()));
        contBuff.append(StringConst.NEW_COMMA);
        contBuff.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, invGroupInfo.getPlanIbDate()));

        // return
        return contBuff.toString();
    }

    /**
     * prepare invoice container information.
     * 
     * @param invMainInfo invMainInfo
     * @return key
     */
    private String makeInvoiceMainInfoKey(TntMgInvoiceEx invMainInfo) {

        // define
        StringBuffer contBuff = new StringBuffer();

        // make key
        contBuff.append(summaryIdMaps.get(invMainInfo.getInvoiceNo()));
        contBuff.append(StringConst.NEW_COMMA);
        contBuff.append(groupIdMaps.get(this.makeInvoiceGroupKey(invMainInfo)));

        // return
        return contBuff.toString();
    }

    /**
     * prepare invoice container information.
     * 
     * @param invPartsInfo invPartsInfo
     * @return key
     */
    private String makeInvoicePartsInfoKey(BaseEntity invPartsInfo) {

        // define
        StringBuffer contBuff = new StringBuffer();
        
        // just
        if (invPartsInfo instanceof TntMgInvoiceEx) {
            // cast
            TntMgInvoiceEx invMainInfo = (TntMgInvoiceEx) invPartsInfo;
            // make key(INV_ID + PARTS_ID + SUPPLIER_ID)
            contBuff.append(invMainMaps.get(this.makeInvoiceMainInfoKey(invMainInfo)));
            contBuff.append(StringConst.NEW_COMMA);
            contBuff.append(invMainInfo.getPartsId());
            contBuff.append(StringConst.NEW_COMMA);
            contBuff.append(invMainInfo.getSupplierId());
        } else {
            // cast
            TntInvoicePart invMainInfo = (TntInvoicePart) invPartsInfo;
            // make key(INV_ID + PARTS_ID + SUPPLIER_ID)
            contBuff.append(invMainInfo.getInvoiceId());
            contBuff.append(StringConst.NEW_COMMA);
            contBuff.append(invMainInfo.getPartsId());
            contBuff.append(StringConst.NEW_COMMA);
            contBuff.append(invMainInfo.getSupplierId());
        }

        // return
        return contBuff.toString();
    }

    /**
     * do invoice summary table insert.
     * 
     * @param invSummaryList invSummaryList
     * @param offTime offTime
     * @param dbTime dbTime
     */
    private void doInvoiceSummarySave(List<TntInvoiceSummary> invSummaryList, Timestamp offTime, Timestamp dbTime) {
        // loop save
        for (TntInvoiceSummary invSummaryInfo : invSummaryList) {

            // UPLOADED_DATE
            invSummaryInfo.setUploadedDate(offTime);
            // set update information
            // CREATED_BY
            invSummaryInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
            // CREATED_DATE
            invSummaryInfo.setCreatedDate(dbTime);
            // UPDATED_BY
            invSummaryInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
            // UPDATED_DATE
            invSummaryInfo.setUpdatedDate(dbTime);
            // VERSION
            invSummaryInfo.setVersion(IntDef.INT_ONE);

            // insert
            super.baseDao.insert(invSummaryInfo);
        }
    }

    /**
     * do invoice container table insert.
     * 
     * @param invSummaryList invSummaryList
     * @param invContainerList invContainerList
     * @param dbTime dbTime
     */
    private void doInvoiceContainerSave(List<TntInvoiceSummary> invSummaryList,
        List<TntInvoiceContainer> invContainerList, Timestamp dbTime) {
        // loop save
        for (TntInvoiceContainer invContInfo : invContainerList) {

            // get summary id
            TntInvoiceSummary invSummaryInfo = invSummaryList.get(invContInfo.getInvoiceSummaryId());

            // set update information
            // INVOICE_SUMMARY_ID
            invContInfo.setInvoiceSummaryId(invSummaryInfo.getInvoiceSummaryId());
            // CREATED_BY
            invContInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
            // CREATED_DATE
            invContInfo.setCreatedDate(dbTime);
            // UPDATED_BY
            invContInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
            // UPDATED_DATE
            invContInfo.setUpdatedDate(dbTime);
            // VERSION
            invContInfo.setVersion(IntDef.INT_ONE);

            // insert
            super.baseDao.insert(invContInfo);
        }
    }

    /**
     * do invoice group table insert.
     * 
     * @param invGroupList invGroupList
     * @param dbTime dbTime
     */
    private void doInvoiceGroupSave(List<TntInvoiceGroup> invGroupList, Timestamp dbTime) {
        // loop save
        for (TntInvoiceGroup invGroupInfo : invGroupList) {

            // set update information
            // CREATED_BY
            invGroupInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
            // CREATED_DATE
            invGroupInfo.setCreatedDate(dbTime);
            // UPDATED_BY
            invGroupInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
            // UPDATED_DATE
            invGroupInfo.setUpdatedDate(dbTime);
            // VERSION
            invGroupInfo.setVersion(IntDef.INT_ONE);

            // insert
            super.baseDao.insert(invGroupInfo);
        }
    }

    /**
     * do invoice main information table insert.
     * 
     * @param invSummaryList invSummaryList
     * @param invGroupList invGroupList
     * @param invMainInfoList invMainInfoList
     * @param dbTime dbTime
     */
    private void doInvoiceMainSave(List<TntInvoiceSummary> invSummaryList, List<TntInvoiceGroup> invGroupList,
        List<TntInvoice> invMainInfoList, Timestamp dbTime) {

        // loop save
        for (TntInvoice invMainInfo : invMainInfoList) {

            // get invoice main information
            TntInvoiceSummary invSummaryInfo = invSummaryList.get(invMainInfo.getInvoiceSummaryId());
            // get invoice main information
            TntInvoiceGroup invGroupInfo = invGroupList.get(invMainInfo.getInvoiceGroupId());

            // set update information
            // INVOICE_GROUP_ID
            invMainInfo.setInvoiceGroupId(invGroupInfo.getInvoiceGroupId());
            // INVOICE_SUMMARY_ID
            invMainInfo.setInvoiceSummaryId(invSummaryInfo.getInvoiceSummaryId());
            // CREATED_BY
            invMainInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
            // CREATED_DATE
            invMainInfo.setCreatedDate(dbTime);
            // UPDATED_BY
            invMainInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
            // UPDATED_DATE
            invMainInfo.setUpdatedDate(dbTime);
            // VERSION
            invMainInfo.setVersion(IntDef.INT_ONE);

            // insert
            super.baseDao.insert(invMainInfo);
        }
    }

    /**
     * do invoice parts table insert.
     * 
     * @param invMainInfoList invMainInfoList
     * @param invPartsInfoList invPartsInfoList
     * @param dbTime dbTime
     */
    private void doInvoicePartsSave(List<TntInvoice> invMainInfoList, List<TntInvoicePart> invPartsInfoList,
        Timestamp dbTime) {

        // loop save
        for (TntInvoicePart invPartsInfo : invPartsInfoList) {

            // get invoice main information
            TntInvoice invMainInfo = invMainInfoList.get(invPartsInfo.getInvoiceId());

            // set update information
            // INVOICE_ID
            invPartsInfo.setInvoiceId(invMainInfo.getInvoiceId());
            // CREATED_BY
            invPartsInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
            // CREATED_DATE
            invPartsInfo.setCreatedDate(dbTime);
            // UPDATED_BY
            invPartsInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
            // UPDATED_DATE
            invPartsInfo.setUpdatedDate(dbTime);
            // VERSION
            invPartsInfo.setVersion(IntDef.INT_ONE);

            // insert
            super.baseDao.insert(invPartsInfo);
        }
    }

    /**
     * Get Invoice summary Information list from database.
     * 
     * @param officeId officeId
     * @return invoice summary information List
     */
    private List<TntMgInvoiceEx> getInvociePartsInfoList(Integer officeId) {

        // prepare parameter
        TntMgInvoiceEx param = new TntMgInvoiceEx();
        param.setOfficeId(officeId);

        // return got list
        logger.debug("----------Start to getInvociePartsInfoList----------");
        return super.baseMapper.select(this.getSqlId("getInvociePartsInfoList"), param);
    }

    /**
     * Get Invoice summary Information list from database.
     * 
     * @param officeId officeId
     * @return invoice summary information List
     */
    /*private List<TntMgInvoiceEx> getOrderStatusInfoList(Integer officeId) {

        // prepare parameter
        TntMgInvoiceEx param = new TntMgInvoiceEx();
        param.setOfficeId(officeId);

        // return got list
        logger.debug("----------Start to getOrderStatusInfoList----------");
        return super.baseMapper.select(this.getSqlId("getOrderStatusInfoList"), param);
    }*/

    /**
     * Get Invoice summary Information list from database from check.
     * 
     * @param officeId officeId
     * @return invoice summary information List
     */
    public List<TntMgInvoiceEx> getInvociePartsInfoListForCheck(Integer officeId) {

        // prepare parameter
        TntMgInvoiceEx param = new TntMgInvoiceEx();
        param.setOfficeId(officeId);

        // return got list
        logger.debug("----------Start to getInvociePartsInfoListForCheck----------");
        return super.baseMapper.select(this.getSqlId("getInvociePartsInfoListForCheck"), param);
    }
}
