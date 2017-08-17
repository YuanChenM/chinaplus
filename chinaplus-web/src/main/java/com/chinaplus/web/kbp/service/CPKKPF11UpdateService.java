/**
 * CPKKPF11UpdateService.java
 * 
 * @screen CPKKPF11
 * @author shiyang
 */
package com.chinaplus.web.kbp.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.entity.TnfOrderStatus;
import com.chinaplus.common.entity.TntKanban;
import com.chinaplus.common.entity.TntKanbanPart;
import com.chinaplus.common.entity.TntKanbanPlan;
import com.chinaplus.common.entity.TntKanbanPlanPart;
import com.chinaplus.common.entity.TntKanbanShipping;
import com.chinaplus.common.entity.TntKanbanShippingPart;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.FileUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.kbp.entity.CPKKPF11Entity;

/**
 * Upload Monthly Kanban Plan Service.
 */
@Service
public class CPKKPF11UpdateService extends BaseService {

    /** SESSION_KEY_FILE */
    private static final String SESSION_KEY_FILE = "CPKKPF11_File";

    // /** logger */
    // private static Logger logger = LoggerFactory.getLogger(CPKKPF11UpdateService.class);

    /** Upload Monthly Kanban Plan Service */
    @Autowired
    private CPKKPF11Service cpkkpf11service;

    /**
     * Row data list -> Row data map (key: File.Row.Customer Parts No.)
     * 
     * @param rowDataList row data list
     * @param rowDataMap row data map
     */
    private void getRowDataMap(List<CPKKPF11Entity> rowDataList, HashMap<String, CPKKPF11Entity> rowDataMap) {
        for (CPKKPF11Entity rowData : rowDataList) {
            rowDataMap.put(cutMiddleLineWithFront(rowData.getSuppPartsNo()), rowData);
        }
    }

    /**
     * Cut middle line if it is exist in the front.
     * 
     * @param value value
     * @return value of after cut
     */
    public String cutMiddleLineWithFront(String value) {
        String valueTemp = value;
        // if (valueTemp.startsWith(StringConst.MIDDLE_LINE)) {
        // valueTemp = valueTemp.substring(1);
        // }
        valueTemp = valueTemp.replaceAll(StringConst.MIDDLE_LINE, "");
        return valueTemp;
    }

    /**
     * Update logic.
     * 
     * @param param base param
     * @param entity header information
     * @param issuedPlanDateList Issued plan date list ([0]:colNo; [1]:Issued plan date)
     * @param deliverDateToObuList Deliver date to Obu list ([0]:colNo; [1]:Deliver date to Obu)
     * @param rowDataList row data list
     * @param partsMasterInfoList parts master info list
     * @param supplyChainList supply chain list
     * @param request HttpServletRequest
     */
    public void doUpdateLogic(BaseParam param, CPKKPF11Entity entity, List<String[]> issuedPlanDateList,
        List<String[]> deliverDateToObuList, List<CPKKPF11Entity> rowDataList,
        List<CPKKPF11Entity> partsMasterInfoList, List<CPKKPF11Entity> supplyChainList, HttpServletRequest request) {

        param.setSwapData("DATE_TIME", getDBDateTimeByDefaultTimezone());

        // 1. Save Kanban Plan main data.
        TntKanban tntKanbanNew = saveKanban(param, entity);

        // 2. Save the Kanban Plan's parts information.
        HashMap<String, CPKKPF11Entity> rowDataMap = new HashMap<String, CPKKPF11Entity>();
        getRowDataMap(rowDataList, rowDataMap);
        HashMap<Integer, TntKanbanPart> kanbanPartsInfoMap = saveKanbanParts(param, entity, rowDataList, rowDataMap,
            partsMasterInfoList, tntKanbanNew);

        // 3. Save shipping plan data for the parts in the exist Kanban plan.
        saveShippingPlanForExistKanbanPlan(param, entity, kanbanPartsInfoMap, tntKanbanNew);

        // 4. Save shipping plan data for the parts in the input file.
        HashMap<Integer, BigDecimal> spqMap = new HashMap<Integer, BigDecimal>();
        HashMap<String, List<Integer>> shippingRouteCodeMap = new HashMap<String, List<Integer>>();
        getPartsMasterSpq(partsMasterInfoList, spqMap, shippingRouteCodeMap);
        saveShippingPlanForUploadFile(param, rowDataList, rowDataMap, supplyChainList, tntKanbanNew, spqMap,
            shippingRouteCodeMap);

        entity.setKanbanId(String.valueOf(tntKanbanNew.getKanbanId()));

        // Save the uploaded file on the server.
        saveUploadFileToServer(entity, request);
    }

    /**
     * Get Parts Master Spq
     * 
     * @param partsMasterInfoList parts master info list
     * @param spqMap (key:PartsId; value:parts master's Srbq)
     * @param shippingRouteCodeMap (shippingRouteCode; value:parts master's PartsId list)
     */
    private void getPartsMasterSpq(List<CPKKPF11Entity> partsMasterInfoList, HashMap<Integer, BigDecimal> spqMap,
        HashMap<String, List<Integer>> shippingRouteCodeMap) {
        for (CPKKPF11Entity entity : partsMasterInfoList) {
            spqMap.put(entity.getPartsId(), entity.getSrbq());

            List<Integer> partsIdList = shippingRouteCodeMap.get(entity.getShippingRouteCode());
            if (partsIdList == null) {
                partsIdList = new ArrayList<Integer>();
            }
            partsIdList.add(entity.getPartsId());
            shippingRouteCodeMap.put(entity.getShippingRouteCode(), partsIdList);
        }
    }

    /**
     * Save the uploaded file on the server.
     * 
     * @param entity header information
     * @param request HttpServletRequest
     */
    private void saveUploadFileToServer(CPKKPF11Entity entity, HttpServletRequest request) {
        String kanbanFolderPath = ConfigUtil.get(Properties.UPLOAD_PATH_KANBAN);

        String kanbanId = entity.getKanbanId();
        // modify for UAT
        // String kanbanPlanNo = entity.getKanbanPlanNo();
        // String fileName = kanbanPlanNo + StringConst.UNDERLINE + kanbanId + CoreConst.SUFFIX_EXCEL;
        // if (!StringUtils.isBlank(entity.getRevisionVersion()) &&
        // !"null".equalsIgnoreCase(entity.getRevisionVersion())) {
        // DecimalFormat df = new DecimalFormat("00");
        // fileName = kanbanPlanNo + StringConst.UNDERLINE + StringConst.ALPHABET_R
        // + df.format(Integer.parseInt(entity.getRevisionVersion())) + StringConst.UNDERLINE + kanbanId
        // + CoreConst.SUFFIX_EXCEL;
        // }
        String fileName = kanbanId + CoreConst.SUFFIX_EXCEL;
        // modify end
        try {
            SessionInfoManager context = SessionInfoManager.getContextInstance(request);
            FileUtil.saveFileToPath((InputStream) context.get(SESSION_KEY_FILE), kanbanFolderPath, fileName);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Save shipping plan data for the parts in the input file.
     * 
     * @param param base param
     * @param rowDataList row data list
     * @param rowDataMap row data map
     * @param supplyChainList supply chain list
     * @param tntKanbanNew tntKanbanNew
     * @param spqMap (key:PartsId; value:parts master's Srbq)
     * @param shippingRouteCodeMap (key:shippingRouteCode; value:parts master's PartsId list)
     */
    private void saveShippingPlanForUploadFile(BaseParam param, List<CPKKPF11Entity> rowDataList,
        HashMap<String, CPKKPF11Entity> rowDataMap, List<CPKKPF11Entity> supplyChainList, TntKanban tntKanbanNew,
        HashMap<Integer, BigDecimal> spqMap, HashMap<String, List<Integer>> shippingRouteCodeMap) {
        // Set supplyChainEntity into supplyChainHeaderMap if ETD+;+ETA+;+ImpPlanInboundDate is not exist in the
        // supplyChainHeaderMap
        HashMap<String, CPKKPF11Entity> supplyChainHeaderMap = new HashMap<String, CPKKPF11Entity>();
        List<CPKKPF11Entity> supplyChainHeaderList = new ArrayList<CPKKPF11Entity>();

        // logger.info("output start===============================================================");
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (CPKKPF11Entity supplyChainEntity : supplyChainList) {
            // logger.info(sdf.format(supplyChainEntity.getChainStartDate()) + "; "
            // + sdf.format(supplyChainEntity.getIssuedDate()) + "; "
            // + sdf.format(supplyChainEntity.getDeliveredDate()) + "; "
            // + sdf.format(supplyChainEntity.getVanningDate()) + "; " + sdf.format(supplyChainEntity.getEtd())
            // + "; " + sdf.format(supplyChainEntity.getEta()) + "; "
            // + sdf.format(supplyChainEntity.getImpPlanInboundDate()) + "; "
            // + supplyChainEntity.getShippingRouteCode());

            String key = supplyChainEntity.getEtd() + StringConst.SEMICOLON + supplyChainEntity.getEta()
                    + StringConst.SEMICOLON + supplyChainEntity.getImpPlanInboundDate();
            if (!supplyChainHeaderMap.containsKey(key)) {
                supplyChainHeaderMap.put(key, supplyChainEntity);
                supplyChainHeaderList.add(supplyChainEntity);
            }
        }
        // logger.info("output end===============================================================");

        // Sort supplyChainHeaderList by ETD, ETA, ImpPlanInboundDate
        Collections.sort(supplyChainHeaderList, new Comparator<Object>() {
            @Override
            public int compare(Object arg0, Object arg1) {
                CPKKPF11Entity entity1 = (CPKKPF11Entity) arg0;
                CPKKPF11Entity entity2 = (CPKKPF11Entity) arg1;
                int compareResult = entity1.getEtd().compareTo(entity2.getEtd());
                if (compareResult == 0) {
                    compareResult = entity1.getEta().compareTo(entity2.getEta());
                    if (compareResult == 0) {
                        compareResult = entity1.getImpPlanInboundDate().compareTo(entity2.getImpPlanInboundDate());
                    }
                }
                return compareResult;
            }
        });

        // loop each ETD&ETA&IMP_INB_PLAN_DATE in process 1-1 (4-10)'s all shipping route.
        for (CPKKPF11Entity supplyChainEntity : supplyChainHeaderList) {
            String key = supplyChainEntity.getEtd() + StringConst.SEMICOLON + supplyChainEntity.getEta()
                    + StringConst.SEMICOLON + supplyChainEntity.getImpPlanInboundDate();

            CPKKPF11Entity entity = new CPKKPF11Entity();
            entity.setKanbanId(String.valueOf(tntKanbanNew.getKanbanId()));
            entity.setOrderMonth(tntKanbanNew.getOrderMonth());

            // 1. Save new shipping plan.
            // 1.1 Find the exist shipping plan.
            entity.setEtd(supplyChainEntity.getEtd());
            entity.setEta(supplyChainEntity.getEta());
            entity.setImpPlanInboundDate(supplyChainEntity.getImpPlanInboundDate());
            CPKKPF11Entity existShippingPlan = cpkkpf11service.getExistShippingPlan(entity);

            // 1.2 If shipping plan is not exist, Insert into TNT_KANBAN_SHIPPING.
            int kanbanShippingId = 0;
            if (existShippingPlan == null) {
                kanbanShippingId = cpkkpf11service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING);
                String shippingUuid = UUID.randomUUID().toString();
                entity.setShippingUuid(shippingUuid);

                TntKanbanShipping tntKanbanShipping = new TntKanbanShipping();
                tntKanbanShipping.setKanbanShippingId(kanbanShippingId);
                tntKanbanShipping.setKanbanId(Integer.parseInt(entity.getKanbanId()));
                tntKanbanShipping.setShippingUuid(shippingUuid);
                tntKanbanShipping.setTransportMode(CodeConst.TransportMode.SEA);
                tntKanbanShipping.setEtd(supplyChainEntity.getEtd());
                tntKanbanShipping.setEta(supplyChainEntity.getEta());
                tntKanbanShipping.setImpInbPlanDate(supplyChainEntity.getImpPlanInboundDate());
                tntKanbanShipping.setOriginalVersion(cpkkpf11service.getMaxOriginalVersion(entity));
                tntKanbanShipping.setRevisionVersion(null);
                tntKanbanShipping.setRevisionReason(null);
                tntKanbanShipping.setCompletedFlag(CodeConst.CompletedFlag.NORMAL);
                tntKanbanShipping.setNirdFlag(CodeConst.NirdFlag.NORMAL);
                cpkkpf11service.insertKanbanShipping(param, tntKanbanShipping);
            } else {
                kanbanShippingId = existShippingPlan.getKanbanShippingId();
                entity.setShippingUuid(existShippingPlan.getShippingUuid());
            }

            // loop each Issued plan date&Deliver date to Obu&VAN in each ETD&ETA&IMP_INB_PLAN_DATE.
            HashMap<Integer, BigDecimal> kanbanQtyUpdateMap = new HashMap<Integer, BigDecimal>();
            List<String> keyBoxList = new ArrayList<String>();
            for (CPKKPF11Entity supplyChainDetailEntity : supplyChainList) {

                String keyDetail = supplyChainDetailEntity.getEtd() + StringConst.SEMICOLON
                        + supplyChainDetailEntity.getEta() + StringConst.SEMICOLON
                        + supplyChainDetailEntity.getImpPlanInboundDate();
                String keyBox = supplyChainDetailEntity.getIssuedDate() + StringConst.SEMICOLON
                        + supplyChainDetailEntity.getDeliveredDate() + StringConst.SEMICOLON
                        + supplyChainDetailEntity.getVanningDate();
                if (key.equals(keyDetail) && !keyBoxList.contains(keyBox)) {
                    keyBoxList.add(keyBox);

                    // 2. Save new kanban plan.
                    // 2.1 If shipping plan data is exist, then find the exist kanban plan data.
                    int kanbanPlanId = 0;
                    entity.setIssuedDate(supplyChainDetailEntity.getIssuedDate());
                    entity.setDeliveredDate(supplyChainDetailEntity.getDeliveredDate());
                    entity.setVanningDate(supplyChainDetailEntity.getVanningDate());
                    CPKKPF11Entity existKanbanPlan = cpkkpf11service.getExistKanbanPlan(entity);
                    if (existKanbanPlan == null) {
                        // 2.2 If kanban plan is not exist, Insert into TNT_KANBAN_PLAN.
                        kanbanPlanId = cpkkpf11service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
                        TntKanbanPlan tntKanbanPlan = new TntKanbanPlan();
                        tntKanbanPlan.setKanbanPlanId(kanbanPlanId);
                        tntKanbanPlan.setKanbanId(Integer.parseInt(entity.getKanbanId()));
                        tntKanbanPlan.setShippingUuid(entity.getShippingUuid());
                        tntKanbanPlan.setOrderMonth(entity.getOrderMonth());
                        tntKanbanPlan.setPlanType(CodeConst.PlanType.BOX);
                        tntKanbanPlan.setIssuedDate(supplyChainDetailEntity.getIssuedDate());
                        tntKanbanPlan.setIssueRemark(null);
                        tntKanbanPlan.setDeliveredDate(supplyChainDetailEntity.getDeliveredDate());
                        tntKanbanPlan.setDelivereRemark(null);
                        tntKanbanPlan.setVanningDate(supplyChainDetailEntity.getVanningDate());
                        tntKanbanPlan.setVanningRemark(null);
                        tntKanbanPlan.setRevisionReason(null);
                        cpkkpf11service.insertKanbanPlan(param, tntKanbanPlan);
                    } else {
                        kanbanPlanId = existKanbanPlan.getKanbanPlanId();
                    }

                    // 2.3 loop each part in the input file, Insert into TNT_KANBAN_PLAN_PARTS
                    List<Integer> partsIds = shippingRouteCodeMap.get(supplyChainDetailEntity.getShippingRouteCode());
                    for (CPKKPF11Entity rowData : rowDataList) {
                        int partsId = rowDataMap.get(cutMiddleLineWithFront(rowData.getSuppPartsNo())).getPartsId();
                        if (partsIds.contains(partsId)) {
                            BigDecimal kanbanQty = DecimalUtil.getBigDecimal(rowData.getRowKanbanQty().get(
                                supplyChainDetailEntity.getIssuedPlanDateIndex())[1]);
                            // If input kanbanQty is empty or 0, then do not insert.
                            if (kanbanQty.compareTo(BigDecimal.ZERO) != 0) {
                                int kppId = cpkkpf11service
                                    .getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS);
                                TntKanbanPlanPart tntKanbanPlanPart = new TntKanbanPlanPart();
                                tntKanbanPlanPart.setKbPlanPartsId(kppId);
                                tntKanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                                tntKanbanPlanPart.setPartsId(partsId);
                                tntKanbanPlanPart.setQty(null);
                                tntKanbanPlanPart.setKanbanQty(kanbanQty);
                                cpkkpf11service.insertKanbanPlanParts(param, tntKanbanPlanPart);
                            }
                            BigDecimal kanbanQtyTemp = kanbanQtyUpdateMap.get(rowData.getRowNo());
                            if (kanbanQtyTemp != null) {
                                kanbanQty = kanbanQty.add(kanbanQtyTemp);
                            }
                            kanbanQtyUpdateMap.put(rowData.getRowNo(), kanbanQty);
                        }
                    }
                }
            }

            // 3. loop each part in the input file, Insert into TNT_KANBAN_SHIPPING_PARTS
            for (CPKKPF11Entity rowData : rowDataList) {
                BigDecimal qty = DecimalUtil.getBigDecimal(kanbanQtyUpdateMap.get(rowData.getRowNo())).multiply(
                    DecimalUtil.getBigDecimal(rowData.getSpq()));
                if (qty.compareTo(BigDecimal.ZERO) != 0) {
                    int kspId = cpkkpf11service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING_PARTS);
                    int partsId = rowDataMap.get(cutMiddleLineWithFront(rowData.getSuppPartsNo())).getPartsId();
                    TntKanbanShippingPart tntKanbanShippingPart = new TntKanbanShippingPart();
                    tntKanbanShippingPart.setKspId(kspId);
                    tntKanbanShippingPart.setKanbanShippingId(kanbanShippingId);
                    tntKanbanShippingPart.setPartsId(partsId);
                    tntKanbanShippingPart.setQty(qty);
                    tntKanbanShippingPart.setKanbanQty(null);
                    cpkkpf11service.insertKanbanShippingParts(param, tntKanbanShippingPart);
                }
            }
        }
    }

    /**
     * Save shipping plan data for the parts in the exist Kanban plan.
     * 
     * @param param base param
     * @param entity header information
     * @param kanbanPartsInfoMap kanban parts info
     * @param tntKanbanNew the data which is insert into TNT_KANBAN
     */
    private void saveShippingPlanForExistKanbanPlan(BaseParam param, CPKKPF11Entity entity,
        HashMap<Integer, TntKanbanPart> kanbanPartsInfoMap, TntKanban tntKanbanNew) {
        if (StringUtils.isBlank(entity.getKanbanId())) {
            return;
        }

        param.setFilter("kanbanId", entity.getKanbanId());
        List<Integer> partsId = new ArrayList<Integer>();
        Iterator<Entry<Integer, TntKanbanPart>> iter = kanbanPartsInfoMap.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Integer, TntKanbanPart> e = (Entry<Integer, TntKanbanPart>) iter.next();
            partsId.add(e.getKey());
        }
        param.setFilter("partsId", partsId);

        if (partsId.size() < 1) {
            return;
        }

        // 1. Find shipping plan and parts with invoice in the exist Kanban Plan.
        List<CPKKPF11Entity> kspInfo = cpkkpf11service.getKanbanShippingAndShippingPartsInfo(param);

        // 2. Find kanban plan and parts with invoice in the exist Kanban Plan.
        List<CPKKPF11Entity> kkpInfo = cpkkpf11service.getKanbanPlanAndPlanPartsInfo(param);

        int kanbanShippingId = 0;
        String keyPre = StringConst.NEW_COMMA;
        for (int i = 0; i < kspInfo.size(); i++) {
            CPKKPF11Entity kspEntity = kspInfo.get(i);
            String key = StringUtil.nullToEmpty(kspEntity.getShippingUuid()) + StringConst.NEW_COMMA
                    + String.valueOf(kspEntity.getOriginalVersion()) + StringConst.NEW_COMMA
                    + StringUtil.nullToEmpty(kspEntity.getRevisionVersion());
            if (!key.equals(keyPre)) {
                String revisionVersion = kspEntity.getRevisionVersion();
                // 3. Insert into TNT_KANBAN_SHIPPING
                kanbanShippingId = cpkkpf11service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING);
                TntKanbanShipping tntKanbanShipping = new TntKanbanShipping();
                tntKanbanShipping.setKanbanShippingId(kanbanShippingId);
                tntKanbanShipping.setKanbanId(tntKanbanNew.getKanbanId());
                tntKanbanShipping.setShippingUuid(kspEntity.getShippingUuid());
                tntKanbanShipping.setTransportMode(kspEntity.getTansportMode());
                tntKanbanShipping.setEtd(kspEntity.getEtd());
                tntKanbanShipping.setEta(kspEntity.getEta());
                tntKanbanShipping.setImpInbPlanDate(kspEntity.getImpPlanInboundDate());
                tntKanbanShipping.setOriginalVersion(kspEntity.getOriginalVersion());
                if (StringUtils.isBlank(revisionVersion)) {
                    tntKanbanShipping.setRevisionVersion(null);
                } else {
                    tntKanbanShipping.setRevisionVersion(Integer.parseInt(revisionVersion));
                }
                tntKanbanShipping.setRevisionReason(kspEntity.getRevisionReason());
                tntKanbanShipping.setCompletedFlag(kspEntity.getCompletedFlag());
                tntKanbanShipping.setNirdFlag(kspEntity.getNirdFlag());
                cpkkpf11service.insertKanbanShipping(param, tntKanbanShipping);
            }
            keyPre = key;

            // 4. Insert into TNT_KANBAN_SHIPPING_PARTS
            int kanbanShippingPartsId = cpkkpf11service
                .getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING_PARTS);
            TntKanbanShippingPart tntKanbanShippingPart = new TntKanbanShippingPart();
            tntKanbanShippingPart.setKspId(kanbanShippingPartsId);
            tntKanbanShippingPart.setKanbanShippingId(kanbanShippingId);
            tntKanbanShippingPart.setPartsId(kspEntity.getPartsId());
            tntKanbanShippingPart.setQty(kspEntity.getQty());
            tntKanbanShippingPart.setKanbanQty(null);
            cpkkpf11service.insertKanbanShippingParts(param, tntKanbanShippingPart);
        }

        int kanbanPlanId = 0;
        keyPre = StringConst.NEW_COMMA;
        for (int i = 0; i < kkpInfo.size(); i++) {
            CPKKPF11Entity kkpEntity = kkpInfo.get(i);
            String key = StringUtil.nullToEmpty(kkpEntity.getShippingUuid()) + StringConst.NEW_COMMA
                    + DateTimeUtil.formatDate(kkpEntity.getIssuedDate()) + StringConst.NEW_COMMA
                    + DateTimeUtil.formatDate(kkpEntity.getDeliveredDate()) + StringConst.NEW_COMMA
                    + DateTimeUtil.formatDate(kkpEntity.getVanningDate());
            if (!key.equals(keyPre)) {
                // 5. Insert into TNT_KANBAN_PLAN
                kanbanPlanId = cpkkpf11service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
                TntKanbanPlan tntKanbanPlan = new TntKanbanPlan();
                tntKanbanPlan.setKanbanPlanId(kanbanPlanId);
                tntKanbanPlan.setKanbanId(tntKanbanNew.getKanbanId());
                tntKanbanPlan.setShippingUuid(kkpEntity.getShippingUuid());
                tntKanbanPlan.setOrderMonth(kkpEntity.getOrderMonth());
                tntKanbanPlan.setPlanType(kkpEntity.getPlanType());
                tntKanbanPlan.setIssuedDate(kkpEntity.getIssuedDate());
                tntKanbanPlan.setIssueRemark(kkpEntity.getIssueRemark());
                tntKanbanPlan.setDeliveredDate(kkpEntity.getDeliveredDate());
                tntKanbanPlan.setDelivereRemark(kkpEntity.getDelivereRemark());
                tntKanbanPlan.setVanningDate(kkpEntity.getVanningDate());
                tntKanbanPlan.setVanningRemark(kkpEntity.getVanningRemark());
                tntKanbanPlan.setRevisionReason(kkpEntity.getRevisionReason());
                cpkkpf11service.insertKanbanPlan(param, tntKanbanPlan);
            }
            keyPre = key;

            // 6. Insert into TNT_KANBAN_PLAN_PARTS
            int kanbanPlanPartsId = cpkkpf11service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS);
            TntKanbanPlanPart tntKanbanPlanPart = new TntKanbanPlanPart();
            tntKanbanPlanPart.setKbPlanPartsId(kanbanPlanPartsId);
            tntKanbanPlanPart.setKanbanPlanId(kanbanPlanId);
            tntKanbanPlanPart.setPartsId(kkpEntity.getPartsId());
            tntKanbanPlanPart.setQty(kkpEntity.getQty());
            tntKanbanPlanPart.setKanbanQty(DecimalUtil.getBigDecimal(kkpEntity.getKanbanQty()));
            cpkkpf11service.insertKanbanPlanParts(param, tntKanbanPlanPart);
        }
    }

    /**
     * Save the Kanban Plan's parts information.
     * 
     * @param param base param
     * @param entity header information
     * @param rowDataList row data list
     * @param rowDataMap row data map
     * @param partsMasterInfo parts master info
     * @param tntKanbanNew the data which is insert into TNT_KANBAN
     * @return Kanban Parts Info
     */
    private HashMap<Integer, TntKanbanPart> saveKanbanParts(BaseParam param, CPKKPF11Entity entity,
        List<CPKKPF11Entity> rowDataList, HashMap<String, CPKKPF11Entity> rowDataMap,
        List<CPKKPF11Entity> partsMasterInfo, TntKanban tntKanbanNew) {

        // 1. Find parts with invoice in the exist Kanban Plan.
        HashMap<Integer, TntKanbanPart> kanbanPartsInfoMap = new HashMap<Integer, TntKanbanPart>();
        if (!StringUtils.isBlank(entity.getKanbanId())) {
            TntKanbanPart kanbanPart = new TntKanbanPart();
            kanbanPart.setKanbanId(Integer.parseInt(entity.getKanbanId()));
            kanbanPartsInfoMap = cpkkpf11service.getKanbanPartsInfo(kanbanPart);
        }

        // 2. Find order status in the Kanban Plan.
        TnfOrderStatus orderStatus = new TnfOrderStatus();
        orderStatus.setKanbanPlanNo(tntKanbanNew.getKanbanPlanNo());
        HashMap<Integer, TnfOrderStatus> orderStatusInfoMap = cpkkpf11service.getOrderStatusInfo(orderStatus);

        // 3. Insert into TNT_KANBAN_PARTS.
        // 4. Save TNF_ORDER_STATUS.
        List<Integer> partsIdNotDel = new ArrayList<Integer>();
        for (CPKKPF11Entity partsMaster : partsMasterInfo) {
            int inactiveFlag = partsMaster.getInactiveFlag();
            if (inactiveFlag == CodeConst.InactiveFlag.INACTIVE) {
                continue;
            }

            BigDecimal orderQty = BigDecimal.ZERO;
            int kanbanPartsId = cpkkpf11service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PARTS);
            TntKanbanPart kanbanPart = new TntKanbanPart();
            kanbanPart.setKanbanPartsId(kanbanPartsId);
            kanbanPart.setKanbanId(tntKanbanNew.getKanbanId());

            if (rowDataMap.containsKey(partsMaster.getSuppPartsNo())) {
                partsIdNotDel.add(partsMaster.getPartsId());
                // 3.1 If the part is exist in input file, insert into TNT_KANBAN_PARTS.
                CPKKPF11Entity rowData = rowDataMap.get(partsMaster.getSuppPartsNo());
                kanbanPart.setSeqNo(rowData.getSeqNo());
                kanbanPart.setPlant(rowData.getPlant());
                kanbanPart.setDock(rowData.getDock());
                kanbanPart.setBoxNo(rowData.getBoxNo());
                kanbanPart.setBoxType(rowData.getBoxType());
                kanbanPart.setOrderMonth(entity.getOrderMonth());
                kanbanPart.setSupplierId(entity.getSupplierId());
                kanbanPart.setCustomerId(entity.getCustomerId());
                kanbanPart.setPartsId(partsMaster.getPartsId());
                kanbanPart.setSpq(DecimalUtil.getBigDecimal(rowData.getSpq()));
                kanbanPart.setQty(DecimalUtil.getBigDecimal(rowData.getOrder()));
                kanbanPart.setKanbanQty(DecimalUtil.getBigDecimal(rowData.getKanbanQty()));
                kanbanPart.setFcQty1(null);
                kanbanPart.setFcQty2(null);
                kanbanPart.setFcQty3(null);
                kanbanPart.setFcQty4(null);
                kanbanPart.setFcQty5(null);
                kanbanPart.setFcQty6(null);
                kanbanPart.setRemark(null);
                kanbanPart.setForceCompletedBy(null);
                kanbanPart.setForceCompletedDate(null);
                kanbanPart.setStatus(1);
                orderQty = DecimalUtil.getBigDecimal(rowData.getOrder());
            } else if (kanbanPartsInfoMap.containsKey(partsMaster.getPartsId())) {
                partsIdNotDel.add(partsMaster.getPartsId());
                // 3.2 If the part is exist in KanbanPartsInfo , insert into TNT_KANBAN_PARTS.
                TntKanbanPart kanbanPartsInfo = kanbanPartsInfoMap.get(partsMaster.getPartsId());
                kanbanPart.setSeqNo(kanbanPartsInfo.getSeqNo());
                kanbanPart.setPlant(kanbanPartsInfo.getPlant());
                kanbanPart.setDock(kanbanPartsInfo.getDock());
                kanbanPart.setBoxNo(kanbanPartsInfo.getBoxNo());
                kanbanPart.setBoxType(kanbanPartsInfo.getBoxType());
                kanbanPart.setOrderMonth(entity.getOrderMonth());
                kanbanPart.setSupplierId(entity.getSupplierId());
                kanbanPart.setCustomerId(entity.getCustomerId());
                kanbanPart.setPartsId(partsMaster.getPartsId());
                kanbanPart.setSpq(kanbanPartsInfo.getSpq());
                kanbanPart.setQty(kanbanPartsInfo.getQty());
                kanbanPart.setKanbanQty(kanbanPartsInfo.getKanbanQty());
                kanbanPart.setFcQty1(kanbanPartsInfo.getFcQty1());
                kanbanPart.setFcQty2(kanbanPartsInfo.getFcQty2());
                kanbanPart.setFcQty3(kanbanPartsInfo.getFcQty3());
                kanbanPart.setFcQty4(kanbanPartsInfo.getFcQty4());
                kanbanPart.setFcQty5(kanbanPartsInfo.getFcQty5());
                kanbanPart.setFcQty6(kanbanPartsInfo.getFcQty6());
                kanbanPart.setRemark(kanbanPartsInfo.getRemark());
                kanbanPart.setForceCompletedBy(kanbanPartsInfo.getForceCompletedBy());
                kanbanPart.setForceCompletedDate(kanbanPartsInfo.getForceCompletedDate());
                kanbanPart.setStatus(kanbanPartsInfo.getStatus());
                orderQty = kanbanPartsInfo.getQty();
            } else {
                partsIdNotDel.add(partsMaster.getPartsId());
                // 3.3 If the part is not exist in input file and not with invoice, insert into TNT_KANBAN_PARTS.
                kanbanPart.setSeqNo(null);
                kanbanPart.setPlant(null);
                kanbanPart.setDock(null);
                kanbanPart.setBoxNo(null);
                kanbanPart.setBoxType(null);
                kanbanPart.setOrderMonth(entity.getOrderMonth());
                kanbanPart.setSupplierId(entity.getSupplierId());
                kanbanPart.setCustomerId(entity.getCustomerId());
                kanbanPart.setPartsId(partsMaster.getPartsId());
                kanbanPart.setSpq(DecimalUtil.getBigDecimal(partsMaster.getSrbq()));
                kanbanPart.setQty(BigDecimal.ZERO);
                kanbanPart.setKanbanQty(BigDecimal.ZERO);
                kanbanPart.setFcQty1(null);
                kanbanPart.setFcQty2(null);
                kanbanPart.setFcQty3(null);
                kanbanPart.setFcQty4(null);
                kanbanPart.setFcQty5(null);
                kanbanPart.setFcQty6(null);
                kanbanPart.setRemark(null);
                kanbanPart.setForceCompletedBy(null);
                kanbanPart.setForceCompletedDate(null);
                kanbanPart.setStatus(1);
                orderQty = BigDecimal.ZERO;
            }
            cpkkpf11service.insertKanbanParts(param, kanbanPart);

            TnfOrderStatus tnfOrderStatus = new TnfOrderStatus();
            if (orderStatusInfoMap.containsKey(partsMaster.getPartsId())) {
                // 4.2 If the part is exist in order status, then update TNF_ORDER_STATUS.
                tnfOrderStatus.setOrderStatusId(orderStatusInfoMap.get(partsMaster.getPartsId()).getOrderStatusId());
                tnfOrderStatus.setOrderQty(orderQty);
                cpkkpf11service.updateOrderStatus(param, tnfOrderStatus);
            } else {
                // 4.1 If the part is not exist in order status, then insert into TNF_ORDER_STATUS.
                tnfOrderStatus.setOrderStatusId(cpkkpf11service
                    .getNextSequence(ChinaPlusConst.Sequence.TNF_ORDER_STATUS));
                tnfOrderStatus.setKanbanPlanNo(entity.getKanbanPlanNo());
                tnfOrderStatus.setPartsId(partsMaster.getPartsId());
                tnfOrderStatus.setOrderMonth(entity.getOrderMonth());
                tnfOrderStatus.setSupplierId(entity.getSupplierId());
                tnfOrderStatus.setOrderQty(orderQty);
                cpkkpf11service.insertOrderStatus(param, tnfOrderStatus);
            }
        }

        param.setSwapData("KANBAN_PLAN_NO", entity.getKanbanPlanNo());
        param.setSwapData("PARTS_ID", partsIdNotDel);
        // 5. Delete dummy order status data.
        cpkkpf11service.deleteOrderStatus(param, entity);
        // 7. Delete dummy pfc shipping data.
        cpkkpf11service.deletePfcShipping(param, entity);
        // 8. Delete dummy pfc detail data.
        cpkkpf11service.deletePfcDetail(param, entity);
        return kanbanPartsInfoMap;
    }

    /**
     * Save Kanban Plan main data.
     * 
     * @param param base param
     * @param entity header information
     * @return tntKanbanNew
     */
    private TntKanban saveKanban(BaseParam param, CPKKPF11Entity entity) {
        TntKanban tntKanbanNew = new TntKanban();
        tntKanbanNew.setOfficeId(entity.getOfficeId());
        tntKanbanNew.setKanbanPlanNo(entity.getCustomerCode() + StringConst.MIDDLE_LINE + entity.getTtcSuppCode()
                + StringConst.MIDDLE_LINE + entity.getOrderMonth());
        tntKanbanNew.setCustomerId(entity.getCustomerId());
        tntKanbanNew.setSupplierId(entity.getSupplierId());
        tntKanbanNew.setOrderMonth(entity.getOrderMonth());
        tntKanbanNew.setUploadedBy(param.getLoginUserId());
        tntKanbanNew.setUploadedDate(cpkkpf11service.getDBDateTime(param.getOfficeTimezone()));
        tntKanbanNew.setSeaFlag(1);
        tntKanbanNew.setTotalOrderQty(null);
        tntKanbanNew.setTotalOnshippingQty(null);
        tntKanbanNew.setTotalInboundQty(null);
        tntKanbanNew.setTotalBalanceQty(null);
        tntKanbanNew.setUploadFileType(CodeConst.UploadFileType.ORIGINAL_KANBAN_FILE);
        tntKanbanNew.setStatus(1);

        if (StringUtils.isBlank(entity.getKanbanId())) {
            // 1. If Kanban Plan is not exist, insert into TNT_KANBAN.
            int kanbanId = cpkkpf11service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN);
            tntKanbanNew.setKanbanId(kanbanId);
            tntKanbanNew.setRevisionVersion(null);
            tntKanbanNew.setRevisionCodeSet(null);
            tntKanbanNew.setRevisionReason(null);
            tntKanbanNew.setAirFlag(0);
        } else {
            // 2. If Kanban Plan is already exist.
            // 2.1 Find total informations for the old Kanban Plan.
            CPKKPF11Entity entityQty = cpkkpf11service.getQtyInfo(entity);

            // 2.2 Update old TNT_KANBAN.
            if (entityQty != null) {
                cpkkpf11service.updateKanban(param, entityQty);
            }

            // 2.3 Insert into new TNT_KANBAN.
            int kanbanId = cpkkpf11service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN);
            tntKanbanNew.setKanbanId(kanbanId);
            tntKanbanNew.setRevisionVersion(Integer.parseInt(entity.getRevisionVersion()) + 1);
            tntKanbanNew.setRevisionCodeSet(entity.getRevisionCodeSet());
            tntKanbanNew.setRevisionReason(entity.getRevisionReason());
            tntKanbanNew.setAirFlag(entity.getAirFlag());
        }

        entity.setRevisionVersion(String.valueOf(tntKanbanNew.getRevisionVersion()));
        entity.setKanbanPlanNo(tntKanbanNew.getKanbanPlanNo());
        cpkkpf11service.insertKanban(param, tntKanbanNew);
        return tntKanbanNew;
    }
}
