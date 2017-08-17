/**
 * 
 * CPMOLS02Controller.
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.control;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMOLS02Entity;
import com.chinaplus.web.mm.service.CPMOLS02Service;

/**
 * Office Detail Screen Controller.
 */
@Controller
public class CPMOLS02Controller extends BaseController {

    /** officeCode */
    private static final String OFFICE_CODE = "CPMOLS02_Label_OfficeCode";

    /** office */
    private static final String OFFICE = "CPMOLS02_Label_Office";

    /** CPMOLS02Service */
    @Autowired
    private CPMOLS02Service service;

    /**
     * RequestMapping
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @return BaseResult<CPMOLS02Entity>
     */
    @RequestMapping(value = { "/mm/CPMOLS02/init" })
    @ResponseBody
    public BaseResult<CPMOLS02Entity> init(@RequestBody BaseParam param, HttpServletRequest request) {

        List<String> allOfficeIds = new ArrayList<String>();
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);
        List<com.chinaplus.common.bean.BusinessPattern> allCustoemrs = um.getCurrentForAll();
        for (com.chinaplus.common.bean.BusinessPattern custoemr : allCustoemrs) {
            allOfficeIds.add(String.valueOf(custoemr.getOfficeId()));
        }

        String officeId = StringUtil.toSafeString(param.getSwapData().get("officeId"));
        if (StringUtil.isEmpty(officeId)) {
            return new BaseResult<CPMOLS02Entity>();
        }
        param.setFilters(param.getSwapData());
        BaseResult<CPMOLS02Entity> result = new PageResult<CPMOLS02Entity>();
        CPMOLS02Entity entity = service.getOneByParam("getOfficeById", param);
        if (null != entity) {
            if (allOfficeIds.contains(officeId)) {
                entity.setHasOfficeRole("1");
            } else {
                entity.setHasOfficeRole("0");
            }
            result.setData(entity);
        } else {
            result.setData(new CPMOLS02Entity());
        }
        return result;
    }

    /**
     * validatorParam
     * 
     * @param result BaseResult<BaseEntity>
     * @param value String
     * @param notEmpty boolean(true=not empty)
     * @param dbLength dbLength
     * @param labelKey String
     */
    private void validatorParam(List<BaseMessage> result, String value, boolean notEmpty, int dbLength, String labelKey) {
        if (StringUtil.isEmpty(value)) {
            if (notEmpty) {
                result.add(new BaseMessage(MessageCodeConst.W1003_001, new String[] { labelKey }));
            }
        } else {
            if (value.getBytes().length > dbLength) {
                result.add(new BaseMessage(MessageCodeConst.W1003_004, new String[] { labelKey,
                    dbLength + StringConst.EMPTY }));
            }
        }
    }

    /**
     * save Office
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     * @throws IOException IOException
     */
    @RequestMapping(value = { "/mm/CPMOLS02/save" })
    @ResponseBody
    public BaseResult<CPMOLS02Entity> save(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        List<BaseMessage> result = new ArrayList<BaseMessage>();
        BaseResult<CPMOLS02Entity> baseResult = new PageResult<CPMOLS02Entity>();
        Map<String, Object> map = param.getSwapData();
        this.setCommonParam(param, request);

        String officeId = StringUtil.toSafeString(map.get("officeId"));
        String officeName = StringUtil.toSafeString(map.get("officeName"));
        String regionCode = StringUtil.toSafeString(map.get("regionCode"));
        String dataDateTime = StringUtil.toSafeString(map.get("dataDateTime"));
        String timeZone = StringUtil.toSafeString(map.get("timeZone"));
        String officeCode = StringUtil.toSafeString(map.get("officeCode"));
        String impStockFlag = StringUtil.toSafeString(map.get("impStockFlag"));

        String address1 = StringUtil.toSafeString(map.get("address1"));
        String address2 = StringUtil.toSafeString(map.get("address2"));
        String address3 = StringUtil.toSafeString(map.get("address3"));
        String address4 = StringUtil.toSafeString(map.get("address4"));
        String postalCode = StringUtil.toSafeString(map.get("postalCode"));
        String contact1 = StringUtil.toSafeString(map.get("contact1"));
        String email1 = StringUtil.toSafeString(map.get("email1"));
        String fax1 = StringUtil.toSafeString(map.get("fax1"));
        String telephone1 = StringUtil.toSafeString(map.get("telephone1"));
        String contact2 = StringUtil.toSafeString(map.get("contact2"));
        String email2 = StringUtil.toSafeString(map.get("email2"));
        String fax2 = StringUtil.toSafeString(map.get("fax2"));
        String telephone2 = StringUtil.toSafeString(map.get("telephone2"));
        String version = StringUtil.toSafeString(map.get("version"));

        validatorParam(result, regionCode, true, IntDef.INT_THREE, "CPMOLS02_Label_Region");
        validatorParam(result, timeZone, true, IntDef.INT_TEN, "CPMOLS02_Label_TimeZone");
        validatorParam(result, officeCode, true, IntDef.INT_TEN, "CPMOLS02_Label_OfficeCode");
        validatorParam(result, officeName, true, IntDef.INT_HUNDRED, "CPMOLS02_Label_ImpStock");

        validatorParam(result, impStockFlag, true, IntDef.INT_TEN, "CPMOLS02_Label_ImpStock");
        validatorParam(result, address1, true, IntDef.INT_HUNDRED, "CPMOLS02_Label_Address1");
        validatorParam(result, address2, false, IntDef.INT_HUNDRED, "CPMOLS02_Label_Address2");
        validatorParam(result, address3, false, IntDef.INT_HUNDRED, "CPMOLS02_Label_Address3");
        validatorParam(result, address4, false, IntDef.INT_HUNDRED, "CPMOLS02_Label_Address4");
        validatorParam(result, postalCode, true, IntDef.INT_TEN, "CPMOLS02_Label_PostalCode");
        validatorParam(result, contact1, true, IntDef.INT_THIRTY, "CPMOLS02_Label_Contact1");
        validatorParam(result, email1, true, IntDef.INT_HUNDRED, "CPMOLS02_Label_Telephone1");
        validatorParam(result, fax1, true, IntDef.INT_THIRTY, "CPMOLS02_Label_FAX1");
        validatorParam(result, telephone1, true, IntDef.INT_THIRTY, "CPMOLS02_Label_Email1");
        validatorParam(result, contact2, false, IntDef.INT_THIRTY, "CPMOLS02_Label_Contact2");
        validatorParam(result, email2, false, IntDef.INT_HUNDRED, "CPMOLS02_Label_Telephone2");
        validatorParam(result, fax2, false, IntDef.INT_THIRTY, "CPMOLS02_Label_FAX2");
        validatorParam(result, telephone2, false, IntDef.INT_THIRTY, "CPMOLS02_Label_Email2");

        Timestamp time = service.getDBDateTimeByDefaultTimezone();
        CPMOLS02Entity entity = new CPMOLS02Entity();
        entity.setAddress1(address1);
        entity.setAddress2(address2);
        entity.setAddress3(address3);
        entity.setAddress4(address4);
        entity.setContact1(contact1);
        entity.setContact2(contact2);
        entity.setFax1(fax1);
        entity.setFax2(fax2);
        entity.setTelephone1(telephone1);
        entity.setTelephone2(telephone2);
        entity.setEmail1(email1);
        entity.setEmail2(email2);
        entity.setPostalCode(postalCode);

        entity.setOfficeName(officeName);
        entity.setRegionCode(regionCode);
        entity.setDataDateTime(dataDateTime);
        entity.setTimeZone(timeZone);
        entity.setInActiveFlag(String.valueOf(IntDef.INT_ZERO));

        entity.setUpdatedDate(time);
        entity.setUpdatedBy(param.getLoginUserId() + StringConst.EMPTY);
        entity.setImpStockFlag(impStockFlag);
        if (StringUtil.isEmpty(officeId)) {
            entity.setOfficeCode(officeCode);
            entity.setVersion(IntDef.INT_ONE);
            entity.setCreatedBy(param.getLoginUserId() + StringConst.EMPTY);
            entity.setCreatedDate(time);
            if (!StringUtil.isEmpty(officeCode)) {
                Map<String, Object> fmap = new Hashtable<String, Object>();
                fmap.put("officeCode", officeCode);
                param.setFilters(fmap);
                if (result.size() != 0) {
                    throw new BusinessException(result);
                }
                int count = service.getDatasCount("getCountInfo", param);
                if (count != 0) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_015);
                    message.setMessageArgs(new String[] { OFFICE_CODE, OFFICE });
                    result.add(message);
                }

            }
            if (result.size() != 0) {
                throw new BusinessException(result);
            }

            service.doInsertOffice(entity);
            // get office info
            CPMOLS02Entity resultEntity = service.getOneByParam("getOfficeById", param);
            baseResult.setData(resultEntity);
            return baseResult;
        }
        Integer versionInt = Integer.parseInt(version);
        entity.setOfficeId(officeId);

        Map<String, Object> fmap = new Hashtable<String, Object>();
        fmap.put("officeId", officeId);
        fmap.put("version", versionInt);
        param.setFilters(fmap);
        if (result.size() != 0) {
            throw new BusinessException(result);
        }
        int count = service.getDatasCount("getCountInfo", param);
        if (count == 0) {
            result.add(new BaseMessage(MessageCodeConst.W1022));
        }
        if (result.size() != 0) {
            throw new BusinessException(result);
        }
        versionInt = versionInt + 1;
        entity.setVersion(versionInt);
        service.doModifyOffice(entity);
        baseResult.setData(entity);
        return baseResult;
    }
}
