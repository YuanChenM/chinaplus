/**
 * @screen CPMPMF11
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.BusinessPattern;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.CodeConst.CalendarParty;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.consts.CodeConst.PartsStatus;
import com.chinaplus.common.consts.CodeConst.ShipType;
import com.chinaplus.common.entity.TnmExpPart;
import com.chinaplus.common.service.ReceivedIpService;
import com.chinaplus.common.service.SsmsCommonService;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.AisinCommonEntity;
import com.chinaplus.web.mm.entity.CPMPMF11Entity;

/**
 * CPMPMF11Service.
 */
@Service
public class CPMPMF11DBService extends BaseService {

    /**
     * aisinCommonService.
     */
    @Autowired
    private AisinCommonService aisinCommonService;

    /**
     * ssmsCommonService.
     */
    @Autowired
    private SsmsCommonService ssmsCommonService;

    /**
     * receivedIpService.
     */
    @Autowired
    private ReceivedIpService receivedIpService;

    /**
     * get BusinessPattern Map
     * 
     * @param userInfo userInfo
     * @return maps maps
     */
    public Map<String, Object> getBusinessPatternMap(UserInfo userInfo) {
        Map<String, Object> maps = new HashMap<String, Object>();
        Map<String, Integer> officeCodeMap = new HashMap<String, Integer>();
        // Map<String, String> customerCodeMap = new HashMap<String, String>();
        Map<String, String> officeCustCodeMap = new HashMap<String, String>();

        List<UserOffice> userOfficeList = userInfo.getUserOffice();
        if (userOfficeList != null && userOfficeList.size() > 0) {
            for (UserOffice uo : userOfficeList) {
                String officeCode = uo.getOfficeCode();
                officeCodeMap.put(officeCode, uo.getOfficeId());
                List<BusinessPattern> busList = uo.getBusinessPatternList();
                if (busList != null && busList.size() > 0) {
                    for (BusinessPattern bp : busList) {
                        String customerCode = bp.getCustomerCode();
                        // customerCodeMap.put(customerCode,
                        // bp.getCustomerId() + StringConst.UNDERLINE + bp.getInactiveFlag());
                        officeCustCodeMap.put(officeCode + StringConst.UNDERLINE + customerCode,
                            bp.getBusinessPattern() + StringConst.UNDERLINE + bp.getCustomerId()
                                    + StringConst.UNDERLINE + bp.getInactiveFlag());
                    }
                }
            }
        }
        maps.put("officeCodeMap", officeCodeMap);
        // maps.put("customerCodeMap", customerCodeMap);
        maps.put("officeCustCodeMap", officeCustCodeMap);
        return maps;
    }

    /**
     * query ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode
     * 
     * @param initDataList initDataList
     * @param type type
     * @param typeNM typeNM
     * @return maps maps
     */
    public Map<String, Object> getDataMap(List<CPMPMF11Entity> initDataList, String type, String typeNM) {
        Map<String, Object> maps = new HashMap<String, Object>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            param.setSwapData("type", type);
            if (ShipType.MOD.equals(typeNM)) {
                param.setSwapData("typeNM", typeNM);
            }
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getDataList"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    String key = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getSsmsMainRoute()
                            + StringConst.UNDERLINE + cf.getExpSuppCode() + StringConst.UNDERLINE + cf.getExpCustCode();
                    if ("need".equals(type)) {
                        String value = cf.getExpInnerPartsId() + StringConst.UNDERLINE + cf.getTtcPartsNo();
                        maps.put(key, value);
                    } else if ("noNeed".equals(type)) {
                        String value = cf.getWestCustCode() + StringConst.UNDERLINE + cf.getWestPartsNo();
                        maps.put(key, value);
                    } else {
                        maps.put(key, cf.getExpPartsId());
                    }
                }
            }
        }
        return maps;
    }

    /**
     * query ttcSuppCode by ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, String> getTtcSuppCodeMap(List<CPMPMF11Entity> initDataList) {
        Map<String, String> maps = new HashMap<String, String>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getDataList"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    String key = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getSsmsMainRoute()
                            + StringConst.UNDERLINE + cf.getExpSuppCode() + StringConst.UNDERLINE + cf.getExpCustCode();
                    maps.put(key, cf.getTtcSuppCode());
                }
            }
        }
        return maps;
    }

    /**
     * query ttcPartsNo + ttcSuppCode + ssmsMainRoute + expSuppCode + customerCode + expCustCode + officeCode
     * 
     * @param initDataList initDataList
     * @param typeNM typeNM
     * @return maps maps
     */
    public Map<String, String> getKeyDataMap(List<CPMPMF11Entity> initDataList, String typeNM) {
        Map<String, String> maps = new HashMap<String, String>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            if (ShipType.MOD.equals(typeNM)) {
                param.setSwapData("typeNM", typeNM);
            }
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getKeyDataList"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    // shiyang mod start
                    // String key = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getTtcSuppCode()
                    // + StringConst.UNDERLINE + cf.getSsmsMainRoute() + StringConst.UNDERLINE
                    // + cf.getExpSuppCode() + StringConst.UNDERLINE + cf.getCustomerCode()
                    // + StringConst.UNDERLINE + cf.getExpCustCode();
                    String key = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getTtcSuppCode()
                            + StringConst.UNDERLINE + cf.getSsmsMainRoute() + StringConst.UNDERLINE
                            + cf.getExpSuppCode() + StringConst.UNDERLINE + cf.getCustomerCode()
                            + StringConst.UNDERLINE + cf.getExpCustCode() + StringConst.UNDERLINE + cf.getOfficeCode();
                    // shiyang mod end
                    String value = cf.getWestCustCode() + StringConst.UNDERLINE + cf.getWestPartsNo();
                    maps.put(key, value);
                }
            }
        }
        return maps;
    }

    /**
     * type new query expCustCode
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, String> getNewExpCustCodeMap(List<CPMPMF11Entity> initDataList) {
        Map<String, String> maps = new HashMap<String, String>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getNewExpCustCodeList"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    // shiyang mod start
                    // maps.put(cf.getExpCustCode(), cf.getInactiveFlag() + StringConst.UNDERLINE +
                    // cf.getExpCustCode());
                    maps.put(cf.getExpCustCode() + StringConst.COMMA + cf.getOfficeCode(), cf.getInactiveFlag());
                    // shiyang mod end
                }
            }
        }
        return maps;
    }

    /**
     * query ttcPartsNo + customerCode + expCustCode + officeCode
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, Integer> getTCEOMap(List<CPMPMF11Entity> initDataList) {
        Map<String, Integer> maps = new HashMap<String, Integer>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getTCEOList"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    String key = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getCustomerCode()
                            + StringConst.UNDERLINE + cf.getOfficeCode();
                    maps.put(key, cf.getPartsId());
                }
            }
        }
        return maps;
    }

    /**
     * query officeCode + ttcPartsNo + customerCode + ttcSuppCod
     * 
     * @param initDataList initDataList
     * @param typeVA typeVA
     * @return maps maps
     */
    public Map<String, Object> getOTCTMap(List<CPMPMF11Entity> initDataList, int typeVA) {
        Map<String, Object> maps = new HashMap<String, Object>();

        Map<String, Object> otctMaps = new HashMap<String, Object>();
        Map<String, Integer> otctCountMaps = new HashMap<String, Integer>();
        Map<String, String> otciYNMapsDB = new HashMap<String, String>();
        // shiyang add start
        Map<String, String> custPartOfficeCustMapDB = new HashMap<String, String>();
        // shiyang add end

        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            param.setSwapData("typeVA", typeVA + "");
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getOTCTList"), param);
            if (dataList != null && dataList.size() > 0) {

                Integer bpv = com.chinaplus.common.consts.CodeConst.BusinessPattern.V_V;
                Integer bpa = com.chinaplus.common.consts.CodeConst.BusinessPattern.AISIN;

                for (CPMPMF11Entity cf : dataList) {
                    String inactiveFlag = cf.getInactiveFlag();
                    if ((InactiveFlag.ACTIVE + "").equals(inactiveFlag)) {
                        String key = cf.getOfficeCode() + StringConst.UNDERLINE + cf.getTtcPartsNo()
                                + StringConst.UNDERLINE + cf.getCustomerCode();
                        otctMaps.put(key, cf);
                        Integer count = otctCountMaps.get(key);
                        if (count == null) {
                            otctCountMaps.put(key, 1);
                        } else {
                            count++;
                            otctCountMaps.put(key, count);
                        }
                    }
                    // vv
                    if (bpv == typeVA) {
                        String key = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getTtcSuppCode()
                                + StringConst.UNDERLINE + cf.getSsmsMainRoute() + StringConst.UNDERLINE
                                + cf.getExpSuppCode() + StringConst.UNDERLINE + cf.getCustomerCode()
                                + StringConst.UNDERLINE + cf.getExpCustCode();
                        otciYNMapsDB.put(key, inactiveFlag);
                    }
                    // aisin
                    if (bpa == typeVA) {
                        String key = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getTtcSuppCode()
                                + StringConst.UNDERLINE + cf.getCustomerCode() + StringConst.UNDERLINE
                                + cf.getSuppPartsNo() + StringConst.UNDERLINE + cf.getExpCustCode()
                                + StringConst.UNDERLINE + cf.getCustPartsNo();
                        otciYNMapsDB.put(key, inactiveFlag);

                        // shiyang add start
                        String custPartOfficeCust = cf.getCustPartsNo() + StringConst.COMMA + cf.getOfficeCode()
                                + StringConst.COMMA + cf.getCustomerCode();
                        custPartOfficeCustMapDB.put(custPartOfficeCust, cf.getTtcPartsNo());
                        // shiyang add end
                    }
                }
            }
        }
        maps.put("otctMaps", otctMaps);
        maps.put("otctCountMaps", otctCountMaps);
        maps.put("otciYNMapsDB", otciYNMapsDB);
        // shiyang add start
        maps.put("custPartOfficeCustMapDB", custPartOfficeCustMapDB);
        // shiyang add end
        return maps;
    }

    /**
     * query westCustCode + westPartsNo
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    // shiyang mod start
    // public Map<String, String> getWWMap(List<CPMPMF11Entity> initDataList) {
    // Map<String, String> maps = new HashMap<String, String>();
    public Map<String, List<String>> getWWMap(List<CPMPMF11Entity> initDataList) {
        Map<String, List<String>> maps = new HashMap<String, List<String>>();
        // shiyang mod end
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getWWList"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    String key = cf.getWestCustCode() + StringConst.UNDERLINE + cf.getWestPartsNo();
                    // shiyang mod start
                    // maps.put(key, cf.getWestCustCode());
                    String value = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getTtcSuppCode()
                            + StringConst.UNDERLINE + cf.getSsmsMainRoute() + StringConst.UNDERLINE
                            + cf.getExpSuppCode() + StringConst.UNDERLINE + cf.getCustomerCode()
                            + StringConst.UNDERLINE + cf.getExpCustCode() + StringConst.UNDERLINE + cf.getOfficeCode();
                    if (maps.containsKey(key)) {
                        List<String> list = maps.get(key);
                        list.add(value);
                        maps.put(key, list);
                    } else {
                        List<String> list = new ArrayList<String>();
                        list.add(value);
                        maps.put(key, list);
                    }
                    // shiyang mod end
                }
            }
        }
        return maps;
    }

    /**
     * query expCalendarCode
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, String> getExpCalendarCodeMap(List<CPMPMF11Entity> initDataList) {
        Map<String, String> maps = new HashMap<String, String>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            param.setSwapData("type", CalendarParty.EXP_WAREHOUSE);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getCalendarCode"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    String key = cf.getExpCalendarCode();
                    maps.put(key, key);
                }
            }
        }
        return maps;
    }

    /**
     * query ttcPartsNo + oldTtcPartsNo
     * 
     * @return maps maps
     */
    public Map<String, Object> getTtcPartsNo() {
        Map<String, Object> maps = new HashMap<String, Object>();
        Map<String, String> ttcPartsNoMaps = new HashMap<String, String>();
        Map<String, String> oldTtcPartsNoMaps = new HashMap<String, String>();

        BaseParam param = new BaseParam();
        List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getTtcPartsNo"), param);
        if (dataList != null && dataList.size() > 0) {
            for (CPMPMF11Entity cf : dataList) {
                String ttcPartsNo = cf.getTtcPartsNo();
                ttcPartsNoMaps.put(ttcPartsNo, ttcPartsNo);
                String oldTtcPartsNo = cf.getOldTtcPartsNo();
                if (!StringUtil.isNullOrEmpty(oldTtcPartsNo)) {
                    oldTtcPartsNoMaps.put(oldTtcPartsNo, ttcPartsNo);
                }
            }
        }
        maps.put("ttcPartsNoMaps", ttcPartsNoMaps);
        maps.put("oldTtcPartsNoMaps", oldTtcPartsNoMaps);
        return maps;
    }

    /**
     * query UOM
     * 
     * @return maps maps
     */
    public Map<String, String> getExpUomCode() {
        Map<String, String> maps = new HashMap<String, String>();
        BaseParam param = new BaseParam();
        List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getExpUomCode"), param);
        if (dataList != null && dataList.size() > 0) {
            for (CPMPMF11Entity cf : dataList) {
                String key = cf.getExpUomCode();
                maps.put(key, key);
            }
        }
        return maps;
    }

    /**
     * query expRegion,impRegion
     * 
     * @return maps maps
     */
    public Map<String, String> getRegion() {
        Map<String, String> maps = new HashMap<String, String>();
        BaseParam param = new BaseParam();
        List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getRegion"), param);
        if (dataList != null && dataList.size() > 0) {
            for (CPMPMF11Entity cf : dataList) {
                String key = cf.getExpRegion();
                maps.put(key, key);
            }
        }
        return maps;
    }

    /**
     * query impRegion by officeCode
     * 
     * @return maps maps
     */
    public Map<String, String> getImpRegion() {
        Map<String, String> maps = new HashMap<String, String>();
        BaseParam param = new BaseParam();
        List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getImpRegion"), param);
        if (dataList != null && dataList.size() > 0) {
            for (CPMPMF11Entity cf : dataList) {
                maps.put(cf.getOfficeCode(), cf.getImpRegion());
            }
        }
        return maps;
    }

    // shiyang mod start
    // /**
    // * query customerCode + customerName
    // *
    // * @return maps maps
    // */
    // public Map<String, String> getCustomerCodeName() {
    // Map<String, String> maps = new HashMap<String, String>();
    // BaseParam param = new BaseParam();
    // List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getCustomerCodeName"), param);
    // if (dataList != null && dataList.size() > 0) {
    // for (CPMPMF11Entity cf : dataList) {
    // maps.put(cf.getCustomerCode(), cf.getCustomerName());
    // }
    // }
    // return maps;
    // }
    /**
     * query customer
     * 
     * @param customer key: CustomerCode,OfficeCode,BusinessPattern, value: CustomerName
     */
    public void getCustomerInfo(Map<String, String> customer) {
        BaseParam param = new BaseParam();
        List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getCustomerCodeName"), param);
        if (dataList != null && dataList.size() > 0) {
            for (CPMPMF11Entity cf : dataList) {
                customer.put(
                    cf.getCustomerCode() + StringConst.COMMA + cf.getOfficeCode() + StringConst.COMMA
                            + cf.getBusinessPattern(), cf.getCustomerName());
            }
        }
    }

    // shiyang mod end

    /**
     * query officeCode + expRegion + toEtd + inactiveFlag + shippingRouteType by shippingRouteCode
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, String> getShippingRouteCode(List<CPMPMF11Entity> initDataList) {
        Map<String, String> maps = new HashMap<String, String>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getShippingRouteCode"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    String key = cf.getShippingRouteCode();
                    String officeCode = "";
                    if (!StringUtil.isNullOrEmpty(cf.getOfficeCode())) {
                        officeCode = cf.getOfficeCode();
                    }
                    String expRegion = "";
                    if (!StringUtil.isNullOrEmpty(cf.getExpRegion())) {
                        expRegion = cf.getExpRegion();
                    }
                    // shiyang mod start
                    // String value = officeCode + StringConst.DOUBLE_UNDERLINE + expRegion +
                    // StringConst.DOUBLE_UNDERLINE
                    // + cf.getToEtd().getTime() + StringConst.DOUBLE_UNDERLINE + cf.getType();
                    String value = officeCode + StringConst.COMMA + expRegion + StringConst.COMMA
                            + cf.getToEtd().getTime() + StringConst.COMMA + cf.getType();
                    // shiyang mod end
                    maps.put(key, value);
                }
            }
        }
        return maps;
    }

    /**
     * query impWhsCode + impRegion
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, String> getImpWhsCodeRegion(List<CPMPMF11Entity> initDataList) {
        Map<String, String> maps = new HashMap<String, String>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getImpWhsCodeRegion"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    maps.put(cf.getImpWhsCode(), cf.getImpRegion());
                }
            }
        }
        return maps;
    }

    /**
     * query expRegion,supplierName by ttcSuppCode
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, String> getExpReSuNameBySupCode(List<CPMPMF11Entity> initDataList) {
        Map<String, String> maps = new HashMap<String, String>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getExpReSuNameBySupCode"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    String ttcSuppCode = cf.getTtcSuppCode();
                    String value = cf.getExpRegion() + StringConst.DOUBLE_UNDERLINE + cf.getSupplierName()
                            + StringConst.DOUBLE_UNDERLINE + cf.getSupplierId();
                    maps.put(ttcSuppCode, value);
                }
            }
        }
        return maps;
    }

    /**
     * query ssmsVendorRoute by expCustCode
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, String> getSsmsVendorRoute(List<CPMPMF11Entity> initDataList) {
        Map<String, String> maps = new HashMap<String, String>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getSsmsVendorRoute"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    String expCustCode = cf.getExpCustCode();
                    String key = expCustCode + StringConst.UNDERLINE + cf.getSsmsVendorRoute();
                    maps.put(key, expCustCode);
                }
            }
        }
        return maps;
    }

    /**
     * do TNM_SUPPLIER , TNM_WAREHOUSE
     * 
     * @param dataList dataList
     * @return dataList dataList
     */
    public List<CPMPMF11Entity> doSupplierWarehouse(List<CPMPMF11Entity> dataList) {
        // shiyang add start
        if (dataList == null) {
            return null;
        }
        // shiyang add end

        Map<String, Integer> supplierMaps = new HashMap<String, Integer>();
        Map<String, String> warehouseMaps = new HashMap<String, String>();

        // get db time
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();

        for (CPMPMF11Entity cf : dataList) {

            // set date
            cf.setCreatedDate(dbTime);
            cf.setUpdatedDate(dbTime);

            // deal TNM_SUPPLIER
            String ttcSuppCodeType = cf.getTtcSuppCodeType();
            if (ShipType.NEW.equals(ttcSuppCodeType) || ShipType.MOD.equals(ttcSuppCodeType)) {
                String ttcSuppCode = cf.getTtcSuppCode();
                String supplierName = cf.getSupplierName();
                String expRegion = cf.getExpRegion();
                String keys = ttcSuppCode + StringConst.UNDERLINE + supplierName + StringConst.UNDERLINE + expRegion;
                Integer values = supplierMaps.get(keys);
                if (values == null) {
                    // insert TNM_SUPPLIER
                    if (ShipType.NEW.equals(ttcSuppCodeType)) {
                        this.baseMapper.insert(this.getSqlId("inertSupplier"), cf);
                        supplierMaps.put(keys, cf.getSupplierId());
                    }
                    // update TNM_SUPPLIER
                    else if (ShipType.MOD.equals(ttcSuppCodeType)) {
                        this.baseMapper.update(this.getSqlId("updateSupplier"), cf);
                    }
                } else {
                    cf.setSupplierId(values);
                }
            }

            // deal TNM_WAREHOUSE
            String impWhsCodeType = cf.getImpWhsCodeType();
            if (ShipType.NEW.equals(impWhsCodeType)) {
                String impWhsCode = cf.getImpWhsCode();
                String valuew = warehouseMaps.get(impWhsCode);
                if (valuew == null) {
                    warehouseMaps.put(impWhsCode, impWhsCode);
                    // insert TNM_WAREHOUSE
                    this.baseMapper.insert(this.getSqlId("inertWarehouse"), cf);
                }
            }
        }
        return dataList;
    }

    /**
     * do new and mod data
     * 
     * @param dataList dataList
     * @param idLists idLists
     * @param notRequiredPartsList notRequiredPartsList
     */
    @SuppressWarnings("unchecked")
    public void doNewAndModData(List<CPMPMF11Entity> dataList, List<Integer> idLists,
        List<CPMPMF11Entity> notRequiredPartsList) {

        Map<String, Integer> newMasterMaps = new HashMap<String, Integer>();
        Map<Integer, Integer> masterIDMaps = new HashMap<Integer, Integer>();

        Map<String, Integer> newMasterAiMaps = new HashMap<String, Integer>();
        Map<Integer, Integer> masterIdAiMaps = new HashMap<Integer, Integer>();

        String bpv = com.chinaplus.common.consts.CodeConst.BusinessPattern.V_V + "";
        String bpa = com.chinaplus.common.consts.CodeConst.BusinessPattern.AISIN + "";

        Map<String, String> srIdMaps = new HashMap<String, String>();

        // get db time
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();

        // process notRequiredPartsList
        if (notRequiredPartsList != null) {
            for (CPMPMF11Entity cf : notRequiredPartsList) {
                // set dates
                cf.setCreatedDate(dbTime);
                cf.setUpdatedDate(dbTime);
                // update
                this.baseMapper.update(this.getSqlId("updatePartsPartsStu"), cf);
            }
        }

        // check
        if (dataList == null || dataList.isEmpty()) {
            // return
            return;
        }

        // prepare others
        List<Integer> batchExpPartsIdList = new ArrayList<Integer>();
        for (CPMPMF11Entity cf : dataList) {
            // set dates
            cf.setCreatedDate(dbTime);
            cf.setUpdatedDate(dbTime);
            // deal vv
            String businessPattern = cf.getBusinessPattern();
            if (bpv.equals(businessPattern)) {

                Integer partsId = cf.getPartsId();
                String keyt = null;
                Integer valuet = null;
                if (!cf.isPartsStatusFlag()) {
                    keyt = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getCustomerCode() + StringConst.UNDERLINE
                            + cf.getOfficeCode();
                    valuet = newMasterMaps.get(keyt);
                }

                // deal V-V new data
                String type = cf.getType();
                if (ShipType.NEW.equals(type)) {
                    if (partsId == null) {
                        if (valuet == null) {
                            // insert TNM_PARTS_MASTER
                            this.baseMapper.insert(this.getSqlId("inertNewMaster"), cf);
                            // insert TNM_EXP_PARTS
                            this.baseMapper.insert(this.getSqlId("inertNewParts"), cf);
                            newMasterMaps.put(keyt, cf.getPartsId());
                            addDistinct(batchExpPartsIdList, cf.getExpPartsId());
                        } else {
                            cf.setPartsId(valuet);
                            // insert TNM_EXP_PARTS
                            this.baseMapper.insert(this.getSqlId("inertNewParts"), cf);
                            addDistinct(batchExpPartsIdList, cf.getExpPartsId());
                        }
                    } else {
                        Integer valuep = masterIDMaps.get(partsId);
                        if (valuep == null) {
                            // update TNM_PARTS_MASTER
                            this.baseMapper.update(this.getSqlId("updateNewMaster"), cf);
                            masterIDMaps.put(partsId, partsId);
                        }
                        // insert TNM_EXP_PARTS
                        this.baseMapper.insert(this.getSqlId("inertNewParts"), cf);
                        addDistinct(batchExpPartsIdList, cf.getExpPartsId());
                    }
                }
                // deal V-V mod data
                else if (ShipType.MOD.equals(type)) {
                    Integer expInnerPartsId = cf.getExpInnerPartsId();
                    Integer valuep = masterIDMaps.get(partsId);
                    if (expInnerPartsId != null) {
                        if (valuep == null) {
                            // update TNM_PARTS_MASTER
                            this.baseMapper.update(this.getSqlId("updateNewMaster"), cf);
                            masterIDMaps.put(partsId, partsId);
                        }
                        // update TNM_EXP_PARTS
                        TnmExpPart existExpPart = super.getOneById(TnmExpPart.class, cf.getExpPartsId());
                        if (existExpPart != null && existExpPart.getPartsStatus() != PartsStatus.COMPLETED
                                && StringUtil.toInteger(cf.getPartsStatus()) == PartsStatus.COMPLETED) {
                            addDistinct(batchExpPartsIdList, cf.getExpPartsId());
                        }
                        this.baseMapper.update(this.getSqlId("updateParts"), cf);
                    } else {
                        if (partsId != null) {
                            if (valuep == null) {
                                // update TNM_PARTS_MASTER
                                this.baseMapper.update(this.getSqlId("updateNewMaster"), cf);
                                masterIDMaps.put(partsId, partsId);
                            }
                            // update TNM_EXP_PARTS
                            TnmExpPart existExpPart = super.getOneById(TnmExpPart.class, cf.getExpPartsId());
                            if (existExpPart != null && existExpPart.getPartsStatus() != PartsStatus.COMPLETED
                                    && StringUtil.toInteger(cf.getPartsStatus()) == PartsStatus.COMPLETED) {
                                addDistinct(batchExpPartsIdList, cf.getExpPartsId());
                            }
                            this.baseMapper.update(this.getSqlId("updateNewParts"), cf);
                        } else {
                            boolean partsStatusFlag = cf.isPartsStatusFlag();
                            if (partsStatusFlag) {
                                // update TNM_EXP_PARTS only update PARTS_STATUS
                                this.baseMapper.update(this.getSqlId("updatePartsPartsStu"), cf);
                            } else if (valuet == null) {
                                // insert TNM_PARTS_MASTER
                                this.baseMapper.insert(this.getSqlId("inertNewMaster"), cf);
                                // update TNM_EXP_PARTS
                                TnmExpPart existExpPart = super.getOneById(TnmExpPart.class, cf.getExpPartsId());
                                if (existExpPart != null && existExpPart.getPartsStatus() != PartsStatus.COMPLETED
                                        && StringUtil.toInteger(cf.getPartsStatus()) == PartsStatus.COMPLETED) {
                                    addDistinct(batchExpPartsIdList, cf.getExpPartsId());
                                }
                                this.baseMapper.update(this.getSqlId("updateNewParts"), cf);
                                newMasterMaps.put(keyt, cf.getPartsId());
                            } else {
                                cf.setPartsId(valuet);
                                // update TNM_EXP_PARTS
                                TnmExpPart existExpPart = super.getOneById(TnmExpPart.class, cf.getExpPartsId());
                                if (existExpPart != null && existExpPart.getPartsStatus() != PartsStatus.COMPLETED
                                        && StringUtil.toInteger(cf.getPartsStatus()) == PartsStatus.COMPLETED) {
                                    addDistinct(batchExpPartsIdList, cf.getExpPartsId());
                                }
                                this.baseMapper.update(this.getSqlId("updateNewParts"), cf);
                            }
                        }
                    }
                }
            }

            // deal aisin
            if (bpa.equals(businessPattern)) {
                // deal aisin new data
                String type = cf.getType();
                Integer partsId = cf.getPartsId();
                if (ShipType.NEW.equals(type)) {
                    if (null != partsId) {
                        Integer valueam = masterIdAiMaps.get(partsId);
                        if (valueam == null) {
                            // update TNM_PARTS_MASTER
                            this.baseMapper.update(this.getSqlId("updateAisinMaster"), cf);
                            masterIdAiMaps.put(partsId, partsId);
                        }
                        // insert TNM_EXP_PARTS
                        this.baseMapper.update(this.getSqlId("insertAisinParts"), cf);
                    } else {
                        String keya = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getCustomerCode()
                                + StringConst.UNDERLINE + cf.getOfficeCode();
                        Integer valuea = newMasterAiMaps.get(keya);
                        if (valuea == null) {
                            // insert TNM_PARTS_MASTER
                            this.baseMapper.insert(this.getSqlId("insertAisinMaster"), cf);
                            newMasterAiMaps.put(keya, cf.getPartsId());
                            masterIdAiMaps.put(cf.getPartsId(), cf.getPartsId());
                        } else {
                            cf.setPartsId(valuea);
                        }
                        // insert TNM_EXP_PARTS
                        this.baseMapper.insert(this.getSqlId("insertAisinParts"), cf);
                    }
                }
                // deal aisin mod data
                else if (ShipType.MOD.equals(type)) {
                    Integer valueam = masterIdAiMaps.get(partsId);
                    if (valueam == null) {
                        // update TNM_PARTS_MASTER
                        this.baseMapper.update(this.getSqlId("updateAisinMaster"), cf);
                        masterIdAiMaps.put(valueam, partsId);
                    }
                    // update TNM_EXP_PARTS
                    this.baseMapper.update(this.getSqlId("updateAisinParts"), cf);
                }
            }

            // update TNM_SR_MASTER
            String shippingRouteCode = cf.getShippingRouteCode();
            String valueShipRC = srIdMaps.get(shippingRouteCode);
            if (null == valueShipRC) {
                this.baseMapper.update(this.getSqlId("updateDataList"), cf);
                srIdMaps.put(shippingRouteCode, shippingRouteCode);
            }

        }

        // SSMS I/F & IP I/F process
        if (batchExpPartsIdList.size() > 0) {
            ssmsCommonService.doSSMSLogic(batchExpPartsIdList);
            receivedIpService.doReceiveIpForRegParts(batchExpPartsIdList);
        }

        // change MASTER inactiveFlag is Y
        if (idLists != null && idLists.size() > 0) {
            CPMPMF11Entity cf = new CPMPMF11Entity();
            cf.setUpdatedBy(dataList.get(0).getUpdatedBy());
            cf.setUpdatedDate(dbTime);
            for (Integer s : idLists) {
                cf.setPartsId(s);
                this.baseMapper.update(this.getSqlId("deleteMaster"), cf);
            }
        }
        // 2016/07/05 shiyang add start
        // Update TNM_PARTS_MASTER set INACTIVE_FLAG = '0' if the data that TNM_PARTS_MASTER.INACTIVE_FLAG = '0' is
        // exist.
        BaseParam p = new BaseParam();
        baseMapper.update(this.getSqlId("updatePartsMasterInactiveFlag"), p);
        // 2016/07/05 shiyang add end

        // commom check
        List<AisinCommonEntity> acDatalists = new ArrayList<AisinCommonEntity>();
        Map<String, String> repMaps = new HashMap<String, String>();
        for (CPMPMF11Entity cf : dataList) {
            // add aisin data
            if (bpa.equals(cf.getBusinessPattern()) && cf.isShipRouteCodeChange()) {
                String key = cf.getShippingRouteCode() + StringConst.UNDERLINE + cf.getTtcSuppCode()
                        + StringConst.UNDERLINE + cf.getOfficeId() + StringConst.UNDERLINE + cf.getExpRegion();
                String value = repMaps.get(key);
                if (null == value) {
                    repMaps.put(key, cf.getShippingRouteCode());
                    // distinct list
                    AisinCommonEntity ac = new AisinCommonEntity();
                    ac.setShippingRouteCode(cf.getShippingRouteCode());
                    ac.setSupplierId(cf.getSupplierId());
                    ac.setSupplierCode(cf.getTtcSuppCode());
                    ac.setOfficeId(cf.getOfficeId());
                    ac.setCreatedBy(cf.getCreatedBy());
                    ac.setUpdatedBy(cf.getUpdatedBy());
                    ac.setExpRegion(cf.getExpRegion());
                    ac.setInactiveFlag(cf.getInactiveFlag());
                    acDatalists.add(ac);
                }
            }
        }
        // deal common
        Map<String, Object> aceMaps = aisinCommonService.dealUpdateDataFromParts(acDatalists);

        List<BaseMessage> messageLists = (List<BaseMessage>) aceMaps.get("messageLists");
        if (messageLists != null && messageLists.size() > 0) {
            throw new BusinessException(messageLists);
        } else {
            List<AisinCommonEntity> aceLists = (List<AisinCommonEntity>) aceMaps.get("aceLists");
            if (aceLists != null && aceLists.size() > 0) {
                List<Integer> srIds = new ArrayList<Integer>();
                List<Integer> supplierIds = new ArrayList<Integer>();
                for (AisinCommonEntity ace : aceLists) {
                    addDistinct(srIds, ace.getSrId());
                    addDistinct(supplierIds, ace.getSupplierId());
                }
                BaseParam param = new BaseParam();
                param.setSwapData("srIds", srIds);
                param.setSwapData("supplierIds", supplierIds);
                this.baseMapper.delete(this.getSqlId("deleteDataList"), param);

                for (AisinCommonEntity ace : aceLists) {
                    // set date
                    ace.setCreatedDate(dbTime);
                    ace.setUpdatedDate(dbTime);
                    // insert TNM_SR_DETAIL
                    this.baseMapper.insert(this.getSqlId("insertDataList"), ace);
                }
            }
        }
    }

    /**
     * Add distinct value to list.
     * 
     * @param <T> the object
     * @param list the list
     * @param value the add value
     */
    private <T extends Object> void addDistinct(List<T> list, T value) {

        if (!list.contains(value)) {
            list.add(value);
        }
    }

    /**
     * Get relation of Mail Invoice Customer Code and Customer Code in SSMS OR KANB invoice & Supplier Kanban Issue Plan
     * file.<br>
     * 
     * @return key: InvCustCode(split with ','), value: ExpCustCode
     * @author shiyang
     */
    public Map<String, String> getRelaInvKanbanCust() {
        Map<String, String> map = new HashMap<String, String>();
        BaseParam param = new BaseParam();
        List<CPMPMF11Entity> list = baseMapper.select(this.getSqlId("getRelaInvKanbanCust"), param);
        for (CPMPMF11Entity entity : list) {
            String expCustCode = entity.getExpCustCode();
            String invCustCode = entity.getInvCustCode();
            if (!StringUtils.isBlank(invCustCode)) {
                String[] invCustCodes = invCustCode.split(StringConst.COMMA);
                for (String value : invCustCodes) {
                    map.put(value, expCustCode);
                }
            }
        }
        return map;
    }
}
