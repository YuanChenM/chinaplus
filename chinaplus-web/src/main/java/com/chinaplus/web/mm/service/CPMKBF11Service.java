/**
 * CPMKBF11Service.java
 * 
 * @screen CPMKBF11
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.web.mm.control.CPMKBF11Controller;
import com.chinaplus.web.mm.entity.CPMKBF01Entity;
import com.chinaplus.web.mm.entity.OfficeAndCustmorEntity;
import com.chinaplus.web.mm.entity.UserCustmorEntity;
import com.chinaplus.web.mm.entity.UserOfficeCodesEntity;

/**
 * CPMKBF11Service.
 * Kanban Issued Plan Date Master Upload
 */
@Service
public class CPMKBF11Service extends BaseService {
    /**
     * 
     * The current user query Kanban data already exist
     * 
     * @param param the parameters
     * @return List<CPMKBF01Entity>
     */
    public List<CPMKBF01Entity> getList(BaseParam param) {
        return this.baseMapper.select(this.getSqlId("findAllList"), param);
    }

    /**
     * 
     * 查询当前用户下office下的custmorcode
     * @param param the parameters
     * @return List<CPMKBF01Entity>
     */
    public List<OfficeAndCustmorEntity> getOfficeAndCustmor(BaseParam param) {
        return this.baseMapper.select(this.getSqlId("findAllCustmor"), param);
    }

    /**
     * Query the current user under the officecode
     * userofficecodes
     * 
     * @param param the parameters
     * @return List<UserOfficeCodesEntity>
     */
    public List<UserOfficeCodesEntity> getUserofficeCodes(BaseParam param) {
        return this.baseMapper.select(this.getSqlId("findUserOfficeCodes"), param);
    }

    /**
     * Query the current user under all the bussinesspattern custmor
     * userofficecodes
     * 
     * @param param the parameters
     * @return List<UserOfficeCodesEntity>
     */
    public List<UserCustmorEntity> getCustmors(BaseParam param) {
        return this.baseMapper.select(this.getSqlId("findUserCustmors"), param);
    }

    /**
     * query customerid
     * userofficecodes
     * 
     * @param param the parameters
     * @return List<UserOfficeCodesEntity>
     */
    public CPMKBF01Entity getCustomerIdByCode(BaseParam param) {
        return this.baseMapper.findOne(getSqlId("findCustomerIdByCode"), param);
    }

    /**
     * Query Kanban key
     * userofficecodes
     * 
     * @param param the parameters
     * @return List<UserOfficeCodesEntity>
     */
    public CPMKBF01Entity getKbIssuedId(BaseParam param) {
        return this.baseMapper.findOne(getSqlId("findKbIssuedId"), param);
    }

    /**
     * 
     * @param list save data
     * @param param the parameters
     */
    public void doSaveKbData(List<CPMKBF01Entity> list, BaseParam param) {

        for (CPMKBF01Entity entityTo : list) {
            param.setSwapData("customerCode", entityTo.getCustomerCode());
            param.setSwapData("officeCode", entityTo.getOfficeCode());
            CPMKBF01Entity entityAdd = getCustomerIdByCode(param);
            entityTo.setCreatedDate(getDBDateTimeByDefaultTimezone());
            entityTo.setUpdatedDate(getDBDateTimeByDefaultTimezone());
            entityTo.setCustomerId(entityAdd.getCustomerId());
            if (CPMKBF11Controller.NEW.equals(entityTo.getNewMod())) {
                entityTo.setVersion(IntDef.INT_ONE);
                param.setSwapData("entity", entityTo);
                this.baseMapper.insert(getSqlId("addKbData"), param);
            } else if (CPMKBF11Controller.MOD.equals(entityTo.getNewMod())) {
                param.setSwapData("entity", entityTo);
                entityTo.setKbIssuedId(getKbIssuedId(param).getKbIssuedId());
                this.baseMapper.update(getSqlId("updateKbData"), param);
            }
        }
    }
}
