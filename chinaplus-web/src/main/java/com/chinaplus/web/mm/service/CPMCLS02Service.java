/**
 * CPMCLS02Service.
 * 
 * @author shi_yuxi
 * @screen CPMCLS02
 */
package com.chinaplus.web.mm.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TnmUserCustomer;
import com.chinaplus.common.entity.TnmWarehouse;
import com.chinaplus.common.entity.TnmWhsCustomer;
import com.chinaplus.common.service.ReceivedIpService;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.web.mm.entity.CPMCLS02Entity;
import com.chinaplus.web.mm.entity.TTCLogixCustomer;

/**
 * customer detail screen service
 */
@Service
public class CPMCLS02Service extends BaseService {
    
    @Autowired
    private ReceivedIpService ipService;

    /**
     * getCustomerByCode
     * 
     * @param param BaseParam
     * @return CPMCLS02Entity
     */
    public CPMCLS02Entity getCustomerById(BaseParam param) {
        PageResult<CPMCLS02Entity> result = this.getAllList("getCustomerByCode", param);
        if (null != result && null != result.getDatas() && 0 < result.getDatas().size()) {
            return result.getDatas().get(0);
        } else {
            return new CPMCLS02Entity();
        }
    }

    /**
     * getCustCodeCount
     * 
     * 
     * @param param BaseParam
     * @return 0/1 or more
     */
    public int getCustCodeCount(BaseParam param) {
        return baseMapper.count("getCustCodeCount", param);
    }

    /**
     * daoAdd
     * 
     * @param customer TnmCustomer
     */
    public void daoAdd(TnmCustomer customer) {
        baseDao.saveOrUpdate(customer);
    }

    /**
     * insert
     * 
     * @param entity CPMCLS02Entity
     * @param param param
     * @return 0/1
     */
    public int doaddCustomer(CPMCLS02Entity entity, ObjectParam<CPMCLS02Entity> param) {
        Integer customerId = getNextSequence("SEQ_TNM_CUSTOMER");
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();
        entity.setCustomerId(customerId);
        entity.setCreatedDate(dbTime);
        entity.setUpdatedDate(dbTime);
        int i = baseMapper.insert("addCustomer", entity);
        addWHSCustomer(param);
        baseDao.flush();
        this.updateIpInformation(entity);
        return i;

    }

    /**
     * update
     * 
     * @param entity CPMCLS02Entity
     * @param param param
     * @return 0/1
     */
    public int domodifyCustomer(CPMCLS02Entity entity, ObjectParam<CPMCLS02Entity> param) {
        deleteWHSCustomer(param);
        addWHSCustomer(param);
        baseDao.flush();
        entity.setUpdatedDate(super.getDBDateTimeByDefaultTimezone());
        int i = baseMapper.update("modifyCustomer", entity);
        this.updateIpInformation(entity);
        return i;
    }

    /**
     * get ware house code list
     * 
     * @param param param
     * @return List<ComboData>
     */
    public List<ComboData> getWHSCodeList(BaseParam param) {
        TnmOffice office = baseMapper.findOne(getSqlId("getRegionCode"), param);
        String regionCode = office.getRegionCode();
        param.getSwapData().put("whsRegionCode", regionCode);
        return baseMapper.select(getSqlId("getWHSCodeList"), param);
    }
    
    /**
     * 
     * do update Ip information.
     * 
     * @param entity entity
     */
    private void updateIpInformation(CPMCLS02Entity entity) {
        
        // get customer information
        TnmCustomer param = new TnmCustomer();
        param.setCustomerCode(entity.getCustomerCode());
        param.setOfficeId(entity.getOfficeId());
        
        // get from database
        TnmCustomer custInfo = super.baseDao.findOne(param);
        if (custInfo != null) {
            Integer processType = null;
            if (custInfo.getBusinessPattern().equals(BusinessPattern.V_V)) {
                processType = ReceivedIpService.PROCESS_TYPE_VV;
            } else {
                processType = ReceivedIpService.PROCESS_TYPE_AISIN;
            }
            ipService.doReceiveIpForIrregular(custInfo.getOfficeId(), processType, null);
        }
    }

    /**
     * add whsCustomerr
     * 
     * @param entity entity
     * @param whsCode whsCode
     * @param param param
     * @return 0/1
     */
    public int addWHSCustomer(TnmWhsCustomer entity, String whsCode, BaseParam param) {

        String hql2 = "FROM TNM_WAREHOUSE WHERE WHS_CODE = ?";
        Object[] param3 = new Object[] { whsCode };
        List<TnmWarehouse> wareHouses = baseDao.select(hql2, param3);
        Boolean flag = (Boolean) param.getSwapData().get("isRegionCodeChanged");
        // Whether or not the judgment exists in the tnm_house table
        if (wareHouses != null && wareHouses.size() != 0) {
            entity.setWhsId(wareHouses.get(0).getWhsId());
            if (flag != null && flag) {
                for (TnmWarehouse whs : wareHouses) {
                    whs.setRegionCode((String) param.getSwapData().get("regionCode"));
                    baseDao.update(whs);
                }
            }
        } else {
            TnmWarehouse warehouse = new TnmWarehouse();
            warehouse.setWhsCode(whsCode);
            warehouse.setRegionCode((String) param.getSwapData().get("regionCode"));
            warehouse.setInactiveFlag(CodeConst.ActiveFlag.N);
            warehouse.setCreatedBy(param.getLoginUserId());
            warehouse.setCreatedDate(getDBDateTimeByDefaultTimezone());
            warehouse.setUpdatedBy(param.getLoginUserId());
            warehouse.setUpdatedDate(getDBDateTimeByDefaultTimezone());
            warehouse.setVersion(IntDef.INT_ONE);
            baseDao.insert(warehouse);
            return addWHSCustomer(entity, whsCode, param);
        }
        return baseMapper.insert(getSqlId("insertWHS"), entity);
    }

    /**
     * delete whsCustomerr
     * 
     * @param param param
     */
    public void deleteWHSCustomer(ObjectParam<CPMCLS02Entity> param) {
        param.getSwapData().put("officeId", param.getData().getOfficeId());
        param.getSwapData().put("customerId", param.getData().getCustomerId());
        List<TTCLogixCustomer> ttcDbList = baseMapper.select(getSqlId("getTTCLogixList"), param);
        List<TTCLogixCustomer> ttcList = param.getData().getTtcCus();
        if (ttcDbList != null && ttcDbList.size() != 0) {
            for (TTCLogixCustomer ttcDb : ttcDbList) {
                if (!ttcList.contains(ttcDb)) {

                    TnmWhsCustomer entity = baseDao.findOne(TnmWhsCustomer.class,
                        Integer.parseInt(ttcDb.getWhsCustomerId()));
                    baseDao.delete(entity);
                }
            }

        }
    }

    /**
     * check exist customer
     * 
     * @param param param
     * @return boolean
     */
    public boolean isExistCustomer(ObjectParam<CPMCLS02Entity> param) {
        Integer officeId = param.getData().getOfficeId();
        String customerCode = param.getData().getCustomerCode();

        String hql = "FROM TNM_CUSTOMER WHERE office_id = ? AND customer_code = ?";
        Object[] param2 = new Object[] { officeId, customerCode };
        List<TnmCustomer> reglist = baseDao.select(hql, param2);
        if (reglist != null && reglist.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * check exist warehouse customer
     * 
     * @param ttc warehouse customer
     * @param customerId customer Id
     * @param businessPattern business Pattern
     * @return boolean
     */
    public boolean isExistWHSCustomer(TTCLogixCustomer ttc, Integer customerId, Integer businessPattern) {

        String whsCustomerCode = ttc.getWhsCustomerCode();

        /** delete at 20160927 start */
        // String hql = "FROM TNM_WHS_CUSTOMER WHERE WHS_CUSTOMER_CODE = ? AND CUSTOMER_ID <> ?";
        // Object[] param2 = new Object[] { whsCustomerCode, customerId };
        // if (customerId == null){
        // hql = "FROM TNM_WHS_CUSTOMER WHERE WHS_CUSTOMER_CODE = ?";
        // param2 = new Object[] { whsCustomerCode };
        // }
        // List<TnmWhsCustomer> reglist = baseDao.select(hql, param2);
        /** delete at 20160927 end */
        
        /** add at 20160927 start */
//        CPMCLS02Entity param = new CPMCLS02Entity();
//        param.setCustomerId(customerId);
//        param.setBusinessPattern(businessPattern);
//        List<TTCLogixCustomer> ttcCus = new ArrayList<TTCLogixCustomer>();
//        TTCLogixCustomer customer = new TTCLogixCustomer();
//        customer.setWhsCustomerCode(whsCustomerCode);
//        ttcCus.add(customer);
//        param.setTtcCus(ttcCus);
        
        BaseParam param = new BaseParam();
        param.setSwapData("customerId", customerId);
        param.setSwapData("businessPattern", businessPattern);
        param.setSwapData("whsCustomerCode", whsCustomerCode);
        
        List<TnmWhsCustomer> reglist = this.baseMapper.select(this.getSqlId("checkExistWhsCustomer"), param);
        /** add at 20160927 end */
        
        if (reglist != null && reglist.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * add warehouse customer
     * 
     * @param param param
     */
    public void addWHSCustomer(ObjectParam<CPMCLS02Entity> param) {
        List<TTCLogixCustomer> ttcList = param.getData().getTtcCus();
        Boolean flag = isRegionChange(param.getData());
        Integer officeId = param.getData().getOfficeId();
        String customerCode = param.getData().getCustomerCode();
        String hql = "FROM TNM_CUSTOMER WHERE office_id = ? AND customer_code = ?";
        Object[] param2 = new Object[] { officeId, customerCode };
        List<TnmCustomer> reglist = baseDao.select(hql, param2);

        if (ttcList != null && ttcList.size() != 0) {
            for (TTCLogixCustomer ttc : ttcList) {
                if (flag) {
                    TnmWhsCustomer oldWhsCustomer = baseDao.findOne(TnmWhsCustomer.class,
                        Integer.parseInt(ttc.getWhsCustomerId()));
                    baseDao.delete(oldWhsCustomer);
                    TnmWhsCustomer entity = new TnmWhsCustomer();
                    String whsCode = ttc.getWhsCode();
                    String whsCustomerCode = ttc.getWhsCustomerCode();
                    Integer customerId = reglist.get(0).getCustomerId();
                    param.setSwapData("regionCode", param.getData().getRegion());
                    param.setSwapData("isRegionCodeChanged", flag);
                    entity.setOfficeId(officeId);
                    entity.setWhsCustomerCode(whsCustomerCode);
                    entity.setCustomerId(customerId);
                    entity.setCreatedBy(param.getLoginUserId());
                    entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
                    entity.setUpdatedBy(param.getLoginUserId());
                    entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
                    entity.setVersion(IntDef.INT_ONE);
                    addWHSCustomer(entity, whsCode, param);
                } else {
                    if ("-1".equals(ttc.getWhsCustomerId())) {
                        TnmWhsCustomer entity = new TnmWhsCustomer();
                        String whsCode = ttc.getWhsCode();
                        String whsCustomerCode = ttc.getWhsCustomerCode();
                        Integer customerId = reglist.get(0).getCustomerId();
                        param.setSwapData("regionCode", param.getData().getRegion());
                        entity.setOfficeId(officeId);
                        entity.setWhsCustomerCode(whsCustomerCode);
                        entity.setCustomerId(customerId);
                        entity.setCreatedBy(param.getLoginUserId());
                        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
                        entity.setUpdatedBy(param.getLoginUserId());
                        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
                        entity.setVersion(IntDef.INT_ONE);
                        addWHSCustomer(entity, whsCode, param);
                    }
                }
            }
        }
    }

    /**
     * check user exist customer
     * 
     * @param param BaseParam
     * @return boolean
     */
    public boolean checkUserCustomer(BaseParam param) {
        List<TnmUserCustomer> customers = baseMapper.select(getSqlId("getUserCustomer"), param);
        if (customers != null && customers.size() > 0) {
            if (customers.get(0) != null) {
                Integer allCustomerFlag = customers.get(0).getAllCustomerFlag();
                if (allCustomerFlag == 1) {
                    return true;
                }
            }
        }
        int num = baseMapper.count(getSqlId("checkUserCustomer"), param);
        if (num > 0) {
            return true;
        }
        return false;
    }

    /**
     * check customer is changed region
     * 
     * @param entity CPMCLS02Entity
     * @return boolean
     */
    public boolean isRegionChange(CPMCLS02Entity entity) {
        Integer customerId = entity.getCustomerId();
        String newRegion = entity.getRegion();
        String oldRegion = "";
        Boolean result = false;
        String hql = "FROM TNM_CUSTOMER WHERE CUSTOMER_ID = ?";
        Object[] param2 = new Object[] { customerId };
        List<TnmCustomer> reglist = baseDao.select(hql, param2);
        if (reglist != null && reglist.size() > 0) {
            oldRegion = reglist.get(0).getRegionCode();
        }
        if (!newRegion.equals(oldRegion)) {
            result = true;
        }
        return result;
    }
}
