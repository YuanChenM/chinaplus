/**
 * Common logic for prepare Supply Chain.
 * 
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ShippingRouteInfoEntity;
import com.chinaplus.common.bean.SupplyChainEntity;
import com.chinaplus.common.bean.TnmCalendarDetailEx;
import com.chinaplus.common.bean.TnmCalendarPartyEx;
import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CalendarParty;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.consts.CodeConst.ImpStockFlag;
import com.chinaplus.common.consts.CodeConst.ShippingRouteType;
import com.chinaplus.common.consts.CodeConst.TransportMode;
import com.chinaplus.common.util.CalendarManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;

/**
 * Common logic for prepare Supply Chain.
 */
@Service
public class SupplyChainService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(SupplyChainService.class);

    /**
     * Prepare Supply Chain.
     *
     * @param chainList Chain Detail Information<As List>
     * @param step Chain Start Step
     * @param busiPat Business Pattern
     * @param <T> extends SupplyChainEntity
     */
    public <T extends SupplyChainEntity> void prepareSupplyChain(List<T> chainList, int step, int busiPat) {
        prepareSupplyChain(chainList, step, busiPat, true);
    }

    /**
     * Prepare Supply Chain.
     *
     * @param chainList Chain Detail Information<As List>
     * @param step Chain Start Step
     * @param busiPat Business Pattern
     * @param isHasInfo is has parts master information
     * @param <T> extends SupplyChainEntity
     */
    public <T extends SupplyChainEntity> void prepareSupplyChain(List<T> chainList, int step, int busiPat,
        boolean isHasInfo) {

        // Check Parameter
        if (chainList == null || chainList.isEmpty()) {
            return;
        }

        // check step
        if (!Arrays.asList(ChainStep.ALL).contains(step)) {
            // throw new BusinessException("Chain Start Step must be vaild.");
            return;
        }

        // Check Business Pattern
        if (busiPat != BusinessPattern.V_V && busiPat != BusinessPattern.AISIN) {
            // throw new BusinessException("Business Pattern must be V-V or Aisin.");
            return;
        }

        // get the smaller date from list
        Date minStartDate = null;
        boolean hasSea = false;
        List<Integer> officeList = new ArrayList<Integer>();
        for (SupplyChainEntity supplyChain : chainList) {

            // set effective flag
            supplyChain.setEffective(true);

            // if start date is null, return
            if (supplyChain.getChainStartDate() == null) {
                // throw new BusinessException("Chain Start Date can not be empty.");
                supplyChain.setEffective(false);
                continue;
            }

            // Check Chain Detail.Transport Mode
            if (supplyChain.getTansportMode() == null) {
                // throw new BusinessException("Transport Mode can not be empty.");
                supplyChain.setEffective(false);
                continue;
            }

            // AISIN only sea
            if (busiPat == BusinessPattern.AISIN) {
                if (supplyChain.getTansportMode().intValue() != TransportMode.SEA) {
                    // throw new BusinessException("If Aisin, Transport Mode must be sea.");
                    supplyChain.setEffective(false);
                    continue;
                }
            }

            // compare
            if (minStartDate == null || minStartDate.after(supplyChain.getChainStartDate())) {
                minStartDate = supplyChain.getChainStartDate();
            }

            // need get shipping route?
            if (TransportMode.SEA == supplyChain.getTansportMode().intValue()) {
                hasSea = true;
            }

            // if need pick up parts information, pick it up
            if (!isHasInfo) {

                // check exp parts id
                if (supplyChain.getExpPartsId() == null) {
                    // throw new BusinessException("If need pick up parts information, Exp Parts Id can not be empty.");
                    supplyChain.setEffective(false);
                    continue;
                }

                // pick up
                SupplyChainEntity partInfo = baseMapper.findOne(this.getSqlId("getPartsInformation"), supplyChain);

                // if no parts information
                if (partInfo == null) {
                    // throw new BusinessException("Exp Parts Id does not exist in Parts Master.");
                    supplyChain.setEffective(false);
                    continue;
                }

                // copy
                supplyChain.setOfficeId(partInfo.getOfficeId());
                supplyChain.setPartsId(partInfo.getPartsId());
                supplyChain.setSupplierId(partInfo.getSupplierId());
                supplyChain.setCustomerId(partInfo.getCustomerId());
                supplyChain.setImpWhsId(partInfo.getImpWhsId());
                supplyChain.setImpWhsCode(partInfo.getImpWhsCode());
                supplyChain.setImpStockFlag(partInfo.getImpStockFlag());
                supplyChain.setShippingRouteCode(partInfo.getShippingRouteCode());
                supplyChain.setAirEtdLt(partInfo.getAirEtdLt());
                supplyChain.setAirEtaLt(partInfo.getAirEtaLt());
                supplyChain.setAirInboundLt(partInfo.getAirInboundLt());
                supplyChain.setSeaEtaLt(partInfo.getSeaEtaLt());
                supplyChain.setSeaInboundLt(partInfo.getSeaInboundLt());
            }

            // set office
            if (!officeList.contains(supplyChain.getOfficeId())) {
                officeList.add(supplyChain.getOfficeId());
            }
        }

        if (minStartDate == null) {
            return;
        }

        // get shipping route data information
        List<ShippingRouteInfoEntity> shippingRouteList = null;
        if (hasSea) {

            // Prepare Shipping Route List
            ShippingRouteInfoEntity routeParam = new ShippingRouteInfoEntity();

            // prepare date
            if (busiPat == BusinessPattern.V_V) {
                if (step == ChainStep.PLAN || step == ChainStep.FORECAST) {
                    routeParam.setDeliveryEnd(minStartDate);
                } else {
                    routeParam.setEtd(minStartDate);
                }
            } else {
                if (step == ChainStep.PLAN) {
                    routeParam.setExpInboundDate(minStartDate);
                } else if (step == ChainStep.FORECAST) {
                    routeParam.setKanbanIssueDate(minStartDate);
                } else {
                    routeParam.setEtd(minStartDate);
                }
            }

            // prepare shipping route type
            List<Integer> routeType = new ArrayList<Integer>();
            if (busiPat == BusinessPattern.V_V) {
                routeType.add(ShippingRouteType.VV);
            } else {
                routeType.add(ShippingRouteType.AISIN_TTTJ);
                routeType.add(ShippingRouteType.AISIN_TTSH);
            }
            routeParam.setRouteTypeList(routeType);

            // set office list
            routeParam.setOfficeList(officeList);

            // get shipping route code
            shippingRouteList = baseMapper.select(this.getSqlId("getShippingRouteList"), routeParam);
        }

        // Prepare Calendar List
        TnmCalendarDetailEx calendarParam = new TnmCalendarDetailEx();
        calendarParam.setCalendarDate(minStartDate);
        calendarParam.setOfficeList(officeList);
        List<TnmCalendarDetailEx> calendarList = baseMapper.select(this.getSqlId("getCalendarList"), calendarParam);

        // get Party information
        TnmCalendarPartyEx partyParam = new TnmCalendarPartyEx();
        partyParam.setOfficeList(officeList);
        List<TnmCalendarPartyEx> partyInfoList = baseMapper.select(this.getSqlId("getCalendarPartyInfo"), partyParam);

        // prepare supply chain
        if (busiPat == BusinessPattern.V_V) {

            // Do Supply Chain Logic for 1:V-V
            prepareSupplyChainForVV(chainList, step, shippingRouteList, calendarList, partyInfoList);
        } else {

            // Do Supply Chain Logic for 2:AISIN
            prepareSupplyChainForAISIN(chainList, step, shippingRouteList, calendarList, partyInfoList);
        }
    }

    /**
     * Prepare Supply Chain for V-V.
     *
     * @param chainList Chain Detail Information<As List>
     * @param step Chain Start Step
     * @param shippingRouteList shipping route data information
     * @param calendarList Calendar List
     * @param partyInfoList Party information
     * @param <T> extends SupplyChainEntity
     */
    private <T extends SupplyChainEntity> void prepareSupplyChainForVV(List<T> chainList, int step,
        List<ShippingRouteInfoEntity> shippingRouteList, List<TnmCalendarDetailEx> calendarList,
        List<TnmCalendarPartyEx> partyInfoList) {

        // create map
        Map<String, List<ShippingRouteInfoEntity>> shippingRouteMap = new HashMap<String, List<ShippingRouteInfoEntity>>();
        Map<Integer, List<TnmCalendarDetailEx>> calendarMap = new HashMap<Integer, List<TnmCalendarDetailEx>>();
        Map<String, Integer> partyMap = new HashMap<String, Integer>();

        // Loop each Chain Detail in Chain Detail Information List
        for (SupplyChainEntity chain : chainList) {

            // if is not effective
            if (!chain.isEffective()) {
                continue;
            }

            // check transport mode
            if (TransportMode.SEA == chain.getTansportMode().intValue()) {

                // Check Shipping Route
                if (chain.getShippingRouteCode() == null) {
                    // throw new BusinessException("Shipping Route Code can not be empty.");
                    chain.setEffective(false);
                    continue;
                }

                // set step into chain
                chain.setChainStep(step);
                chain.setBusinessPattern(BusinessPattern.V_V);

                // get shipping route information
                ShippingRouteInfoEntity shippingRoute = getShippingRouteInfo(chain, shippingRouteMap, shippingRouteList);

                // if no effective shipping route then do not need do prepare supply chain date(Exclude for invoice)
                if (shippingRoute == null && step != ChainStep.INVOICE) {
                    continue;
                }

                // Prepare Import Date
                prepareImportDates(chain, shippingRoute, calendarMap, calendarList, partyMap, partyInfoList);
            } else {

                // Check start step
                if (step != ChainStep.INVOICE) {
                    // Prepare ETD
                    chain.setEtd(DateTimeUtil.addDay(chain.getChainStartDate(), chain.getAirEtdLt()));
                    // Prepare ETA for Air Shipment
                    chain.setEta(DateTimeUtil.addDay(chain.getEtd(), chain.getAirEtaLt()));
                } else {
                    // Prepare ETA for Air Shipment
                    if (chain.getEta() == null) {
                        // set eta
                        chain.setEta(DateTimeUtil.addDay(chain.getChainStartDate(), chain.getAirEtaLt()));
                    }
                }

                // Prepare Import inbound for Air Shipment from ETA
                prepareImportDates(chain, null, calendarMap, calendarList, partyMap, partyInfoList);
            }
        }
    }

    /**
     * Prepare Supply Chain for AISIN.
     *
     * @param chainList Chain Detail Information<As List>s
     * @param step Chain Start Step
     * @param shippingRouteList shipping route data information
     * @param calendarList Calendar List
     * @param partyInfoList Party information
     * @param <T> extends SupplyChainEntity
     */
    private <T extends SupplyChainEntity> void prepareSupplyChainForAISIN(List<T> chainList, int step,
        List<ShippingRouteInfoEntity> shippingRouteList, List<TnmCalendarDetailEx> calendarList,
        List<TnmCalendarPartyEx> partyInfoList) {

        // create map
        Map<String, List<ShippingRouteInfoEntity>> shippingRouteMap = new HashMap<String, List<ShippingRouteInfoEntity>>();
        Map<Integer, List<TnmCalendarDetailEx>> calendarMap = new HashMap<Integer, List<TnmCalendarDetailEx>>();
        Map<String, Integer> partyMap = new HashMap<String, Integer>();

        logger.info("---------------prepareSupplyChainForAISIN start----------");

        // Loop each Chain Detail in Chain Detail Information List
        for (SupplyChainEntity chain : chainList) {

            // if is not effective
            if (!chain.isEffective()) {
                continue;
            }

            // Check Shipping Route
            if (chain.getShippingRouteCode() == null) {
                // throw new BusinessException("Shipping Route Code can not be empty.");
                chain.setEffective(false);
                continue;
            }

            // set step into chain
            chain.setChainStep(step);
            chain.setBusinessPattern(BusinessPattern.AISIN);

            // get shipping route information
            ShippingRouteInfoEntity shippingRoute = getShippingRouteInfo(chain, shippingRouteMap, shippingRouteList);

            // if no effective shipping route then do not need do prepare supply chain date
            if (shippingRoute == null) {
                continue;
            }

            // Prepare Import Date
            prepareImportDates(chain, shippingRoute, calendarMap, calendarList, partyMap, partyInfoList);
        }

        logger.info("---------------prepareSupplyChainForAISIN end----------");
    }

    /**
     * Get information list by shipping route code.
     *
     * @param chain supply chain information
     * @param shippingRouteMap shipping route list map
     * @param shippingRouteList shipping route list from database
     * @return could get shipping route or not
     */
    private ShippingRouteInfoEntity getShippingRouteInfo(SupplyChainEntity chain,
        Map<String, List<ShippingRouteInfoEntity>> shippingRouteMap, List<ShippingRouteInfoEntity> shippingRouteList) {

        // If Chain Detail.Transport Mode is 1:Sea
        // String routeCode = chain.getShippingRouteCode();
        StringBuffer keySb = new StringBuffer();
        keySb.append(chain.getShippingRouteCode());
        if (chain.getBusinessPattern().intValue() == BusinessPattern.AISIN) {
            keySb.append(StringConst.UNDERLINE);
            keySb.append(chain.getSupplierId());
        }
        List<ShippingRouteInfoEntity> currSpRouteLst = null;

        // check is exist or not
        if (shippingRouteMap.containsKey(keySb.toString())) {

            // get from map
            currSpRouteLst = shippingRouteMap.get(keySb.toString());
        } else {

            // create new
            currSpRouteLst = new ArrayList<ShippingRouteInfoEntity>();

            // Get Effective Shipping route from Shipping Route List
            for (ShippingRouteInfoEntity shippingRoute : shippingRouteList) {

                // get compare
                // int comp = shippingRoute.getShippingRouteCode().compareTo(keySb.toString());
                StringBuffer newSb = new StringBuffer();
                newSb.append(shippingRoute.getShippingRouteCode());
                if (chain.getBusinessPattern().intValue() == BusinessPattern.AISIN) {
                    newSb.append(StringConst.UNDERLINE);
                    newSb.append(shippingRoute.getSupplierId());
                }

                // before
                int comp = newSb.toString().compareTo(keySb.toString());
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }

                // set into list
                currSpRouteLst.add(shippingRoute);
            }

            // set into map
            shippingRouteMap.put(keySb.toString(), currSpRouteLst);
        }

        // get effective information from shipping route list
        int step = chain.getChainStep().intValue();
        Date startDate = chain.getChainStartDate();
        for (ShippingRouteInfoEntity shippingRoute : currSpRouteLst) {

            // if from next ETD
            if (step == ChainStep.NEXT_ETD) {
                // if same return this route
                if (shippingRoute.getEtd().after(startDate)) {

                    // set eta
                    chain.setEtd(shippingRoute.getEtd());
                    chain.setEta(shippingRoute.getEta());

                    // return
                    return shippingRoute;
                }
            } else if (step == ChainStep.INVOICE) {

                // if from invoice, same return this route
                if (shippingRoute.getEtd().equals(startDate)) {

                    // set into chain
                    if (chain.getEta() == null) {
                        chain.setEta(shippingRoute.getEta());
                    }

                    // return
                    return shippingRoute;
                }
            } else {

                // if vv, compare with delivery date
                if (BusinessPattern.V_V == chain.getBusinessPattern().intValue()) {

                    // If DELIVERY_END < Chain Detail.Chain Start Date, go to next
                    if (shippingRoute.getDeliveryEnd().before(startDate)) {
                        continue;
                    }
                } else {

                    // get compare date
                    Date compareDate = null;
                    if (step == ChainStep.FORECAST) {
                        // use kanban issue date
                        compareDate = shippingRoute.getKanbanIssueDate();
                    } else {
                        // use Deliver Date to Obu
                        compareDate = shippingRoute.getExpInboundDate();
                    }

                    // use ou
                    if (compareDate.before(startDate)) {
                        continue;
                    } else if (compareDate.after(startDate)) {
                        break;
                    }
                }

                // set into chain
                chain.setDeliverToObuDate(shippingRoute.getExpInboundDate());
                chain.setVanningDate(shippingRoute.getLastVanning());
                chain.setEtd(shippingRoute.getEtd());
                chain.setEta(shippingRoute.getEta());

                // return
                return shippingRoute;
            }
        }

        // if no effective shipping route
        return null;
    }

    /**
     * Prepare Import Dates.
     *
     * @param chain supply chain
     * @param shippingRoute shipping route information
     * @param calendarMap Calendar Map
     * @param calendarList Calendar List
     * @param partyMap Calendar Party Map
     * @param partyInfoList Party Information List
     */
    private void prepareImportDates(SupplyChainEntity chain, ShippingRouteInfoEntity shippingRoute,
        Map<Integer, List<TnmCalendarDetailEx>> calendarMap, List<TnmCalendarDetailEx> calendarList,
        Map<String, Integer> partyMap, List<TnmCalendarPartyEx> partyInfoList) {

        // defined calendar detail
        List<TnmCalendarDetailEx> calDdetail = null;

        // process by each transport mode
        if (TransportMode.SEA == chain.getTansportMode().intValue()) {

            // if has shipping route
            if (shippingRoute != null) {

                // Prepare Custom Clearance Date
                // Get Calendar detail Information form Calendar List of Imp Sea Port Customs
                calDdetail = CalendarManager.findCalendarInfo(CalendarParty.IMP_SEAPORT_CUSTOMS, chain.getOfficeId(),
                    chain.getOfficeId(), calendarMap, calendarList, partyMap, partyInfoList);

                // Get Customer Clearance Date by Customs Calendar detail
                chain.setImpPlanCustomDate(CalendarManager.getDateByWorkingDay(shippingRoute.getImpCcLt(),
                    chain.getEta(), calDdetail));

                // Prepare Imp Inbound Plan Date
                if (chain.getImpStockFlag().intValue() == ImpStockFlag.WITH_CLEARANCE) {

                    // means customs clearance is inbound
                    chain.setImpPlanInboundDate(chain.getImpPlanCustomDate());
                } else {
                    // Get Calendar detail Information form Calendar List of TTC Imp Warehouse
                    if (!ChinaPlusConst.IMP_WH_CODE_DIRECT_SENDING.equals(chain.getImpWhsCode())) {
                        calDdetail = CalendarManager.findCalendarInfo(CalendarParty.TTC_IMP_WAREHOUSE,
                            chain.getOfficeId(), chain.getImpWhsId(), calendarMap, calendarList, partyMap,
                            partyInfoList);
                    } else {
                        calDdetail = CalendarManager.findCalendarInfo(CalendarParty.CUSTOMER, chain.getOfficeId(),
                            chain.getCustomerId(), calendarMap, calendarList, partyMap, partyInfoList);
                    }

                    // Get Customer Clearance Date by Customs Calendar detail
                    chain.setImpPlanInboundDate(CalendarManager.getDateByWorkingDay(shippingRoute.getImpInboundLt(),
                        chain.getImpPlanCustomDate(), calDdetail));
                }
            } else {

                // Get Calendar detail Information form Calendar List of Imp Sea Port Customs
                calDdetail = CalendarManager.findCalendarInfo(CalendarParty.IMP_SEAPORT_CUSTOMS, chain.getOfficeId(),
                    chain.getOfficeId(), calendarMap, calendarList, partyMap, partyInfoList);

                // if no ETD
                if (chain.getEta() == null) {
                    // get ETA
                    chain.setEta(DateTimeUtil.addDay(chain.getChainStartDate(), chain.getSeaEtaLt()));
                }

                // Get Customer Clearance Date by Customs Calendar detail
                chain.setImpPlanInboundDate(CalendarManager.getDateByWorkingDay(chain.getSeaInboundLt(),
                    chain.getEta(), calDdetail));

                // if need custom Clearance date
                if (chain.getImpStockFlag().intValue() == ImpStockFlag.WITH_CLEARANCE) {
                    // set imp custom clear date as imp inbound date
                    chain.setImpPlanCustomDate(chain.getImpPlanInboundDate());
                }
            }

        } else if (TransportMode.AIR == chain.getTansportMode().intValue()) {

            // Get Calendar detail Information form Calendar List of Imp Airport Customs
            calDdetail = CalendarManager.findCalendarInfo(CalendarParty.IMP_AIRPORT_CUSTOMS, chain.getOfficeId(),
                chain.getOfficeId(), calendarMap, calendarList, partyMap, partyInfoList);

            // Get Customer Clearance Date by Customs Calendar Detail
            chain.setImpPlanInboundDate(CalendarManager.getDateByWorkingDay(chain.getAirInboundLt(), chain.getEta(),
                calDdetail));

            // if need custom Clearance date
            if (chain.getImpStockFlag().intValue() == ImpStockFlag.WITH_CLEARANCE) {
                // set imp custom clear date as imp inbound date
                chain.setImpPlanCustomDate(chain.getImpPlanInboundDate());
            }
        }
    }

}
