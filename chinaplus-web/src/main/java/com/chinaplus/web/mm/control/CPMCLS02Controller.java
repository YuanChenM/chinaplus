/**
 * CPMCLS02Control.
 * 
 * @author shi_yuxi
 * 
 * @screen CPMCLS02
 */
package com.chinaplus.web.mm.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMCLS02Entity;
import com.chinaplus.web.mm.entity.TTCLogixCustomer;
import com.chinaplus.web.mm.service.CPMCLS02Service;

/**
 * CPMCLS02Controller.
 */
@Controller
public class CPMCLS02Controller extends BaseController {

    private static final String CUSTOMER_CODE = "CPMCLS02_Label_CustomerCode";

    private static final String CUSTOMER = "CPMCLS02_Label_Customer";

    private static final String WHS_CUSTOMER_CODE = "CPMCLS02_Label_WhsCustCode";

    private static final String OTHER_WHS_CUSTOMER = "CPMCLS02_Label_OtherWhsCust";

    @Autowired
    CPMCLS02Service service;

    /**
     * RequestMapping
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @return BaseResult<CPMCLS02Entity>
     */
    @RequestMapping(value = { "/master/CPMCLS02/init" })
    @ResponseBody
    public BaseResult<CPMCLS02Entity> init(@RequestBody BaseParam param, HttpServletRequest request) {
        setCommonParam(param, request);
        // login user has customer check
        boolean flag = service.checkUserCustomer(param);
        String customerCode = StringUtil.toSafeString(param.getSwapData().get("customerCode"));
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        String mainResourceId = sm.getMainMenuResource();
        UserManager.getLocalInstance(sm).isHasAuthByOffice(mainResourceId, "CPMCLS02", param.getCurrentOfficeId(),
            "_btnModify");

        if (StringUtil.isEmpty(customerCode)) {
            return new BaseResult<CPMCLS02Entity>();
        }
        param.setFilters(param.getSwapData());
        BaseResult<CPMCLS02Entity> result = new BaseResult<CPMCLS02Entity>();
        CPMCLS02Entity entity = service.getCustomerById(param);
        entity.setModifyAuth(flag);
        result.setData(entity);
        return result;
    }

    /**
     * save
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IOException
     * @return BaseResult<BaseEntity>
     */
    @RequestMapping(value = { "master/CPMCLS02/save" })
    @ResponseBody
    public BaseResult<BaseEntity> save(@RequestBody ObjectParam<CPMCLS02Entity> param, HttpServletRequest request,
        HttpServletResponse response) throws IOException {

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        BaseResult<BaseEntity> result = new BaseResult<BaseEntity>();
        this.setCommonParam(param, request);
        validInputData(param, request, messageLists);
        CPMCLS02Entity entity = param.getData();
        List<TTCLogixCustomer> ttcList = param.getData().getTtcCus();
        if (messageLists.size() != 0) {
            BusinessException e = new BusinessException(messageLists);
            throw e;
        }
        /** delete at 20161025 start */
        // for (TTCLogixCustomer ttc : ttcList) {
        //
        // if ("-1".equals(ttc.getWhsCustomerId()) && service.isExistWHSCustomer(ttc, entity.getCustomerId(),
        // entity.getBusinessPattern())) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1003_015);
        // message.setMessageArgs(new String[] { WHS_CUSTOMER_CODE, OTHER_WHS_CUSTOMER });
        // messageLists.add(message);
        // break;
        // }
        //
        // }
        /** delete at 20161025 end */

        Integer activeFlagInt = entity.getActiveFlag();

        boolean addNew = true;// default add
        if (entity.getCustomerId() != null) {
            addNew = false;
        }

        if (entity.getCustomerId() == null && service.isExistCustomer(param)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_015);
            message.setMessageArgs(new String[] { CUSTOMER_CODE, CUSTOMER });
            messageLists.add(message);
        }
        if (messageLists.size() != 0) {
            BusinessException e = new BusinessException(messageLists);
            throw e;
        }
        if (addNew) {
            Integer officeIdInt = entity.getOfficeId();
            entity.setOfficeId(null == officeIdInt ? param.getCurrentOfficeId() : officeIdInt);
        }
        entity.setActiveFlag(null == activeFlagInt ? IntDef.INT_ZERO : activeFlagInt);
        entity.setCreatedBy(param.getLoginUserId());
        // entity.setCreatedDate(time);
        entity.setUpdatedBy(param.getLoginUserId());
        // entity.setUpdatedDate(time);

        if (addNew) {
            entity.setVersion(IntDef.INT_ONE);
            service.doaddCustomer(entity, param);
        } else {
            Integer versionInt = IntDef.INT_ZERO;
            versionInt = entity.getVersion();
            versionInt++;
            entity.setVersion(versionInt);
            service.domodifyCustomer(entity, param);
        }

        return result;
    }

    /**
     * combo ware house code
     * 
     * @param param param
     * @param request request
     * @param response response
     * @return List<ComboData>
     */
    @RequestMapping(value = { "mm/CPMCLS02/getWHSCodeList" })
    @ResponseBody
    public List<ComboData> getWHSCodeList(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        return service.getWHSCodeList(param);
    }

    /**
     * warehouse list
     * 
     * @param param param
     * @param request request
     * @param response response
     * @return List<ComboData>
     */
    @RequestMapping(value = { "mm/CPMCLS02/getWHSList" })
    @ResponseBody
    public PageResult<TTCLogixCustomer> getWHSList(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        return service.getAllList("getTTCLogixList", param);
    }

    /**
     * valid input data
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param messageList List<BaseMessage>
     */
    public void validInputData(ObjectParam<CPMCLS02Entity> param, HttpServletRequest request,
        List<BaseMessage> messageList) {
        Integer officeId = param.getData().getOfficeId();
        String customerId = null;
        if (param.getData().getCustomerId() == null) {
            customerId = StringConst.EMPTY;

        } else {
            customerId = param.getData().getCustomerId().toString();
        }
        String customerCode = param.getData().getCustomerCode();
        String customerName = param.getData().getCustomerName();
        String region = param.getData().getRegion();
        String address1 = param.getData().getAddress1();
        String contact1 = param.getData().getContact1();
        String email1 = param.getData().getEmail1();
        String fax1 = param.getData().getFax1();
        String telephone1 = param.getData().getTelephone1();
        String contact2 = param.getData().getContact2();
        String email2 = param.getData().getEmail2();
        String fax2 = param.getData().getFax2();
        String telephone2 = param.getData().getTelephone2();
        String postalCode = param.getData().getPostalCode();
        String address2 = param.getData().getAddress2();
        String address3 = param.getData().getAddress3();
        String address4 = param.getData().getAddress4();
        String activeFlag = param.getData().getActiveFlag().toString();
        List<TTCLogixCustomer> ttcList = param.getData().getTtcCus();
        for (TTCLogixCustomer cus : ttcList) {
            String whsCode = cus.getWhsCode();
            String whsCusCode = cus.getWhsCustomerCode();
            validData(whsCode, true, IntDef.INT_TWENTY, "CPMCLS02_Grid_Office", messageList);
            validData(whsCusCode, true, IntDef.INT_TWENTY, "CPMCLS02_Grid_Role", messageList);
        }

        boolean addNew = true;// default add
        if (!StringUtil.isEmpty(customerId)) {
            addNew = false;
        }
        /** check them when add new */
        if (addNew) {
            validData(StringUtil.toSafeString(officeId), true, IntDef.INT_THIRTY_TWO, "CPMCLS02_Label_OfficeCode",
                messageList);
            validData(customerCode, true, IntDef.INT_FIFTEEN, "CPMCLS02_Label_CustomerCode", messageList);
        }

        validData(customerName, true, IntDef.INT_HUNDRED, "CPMCLS02_Label_CustomerName", messageList);
        // validData(whsCode, true, IntDef.INT_TWENTY, "CPMCLS02_Label_WhsCustCode", messageList);
        validData(region, true, IntDef.INT_THREE, "CPMCLS02_Label_Region", messageList);
        validData(address1, true, IntDef.INT_HUNDRED, "CPMCLS02_Label_Address1", messageList);
        validData(postalCode, true, IntDef.INT_TEN, "CPMCLS02_Label_PostalCode", messageList);
        validData(activeFlag, true, IntDef.INT_TEN, "CPMCLS02_Label_DiscontinueIndicator", messageList);
        validData(contact1, true, IntDef.INT_THIRTY, "CPMCLS02_Label_Contact1", messageList);
        validData(telephone1, true, IntDef.INT_THIRTY, "CPMCLS02_Label_Telephone1", messageList);
        validData(fax1, true, IntDef.INT_THIRTY, "CPMCLS02_Label_Fax1", messageList);
        validData(email1, true, IntDef.INT_HUNDRED, "CPMCLS02_Label_Email1", messageList);
        validData(address2, false, IntDef.INT_HUNDRED, "CPMCLS02_Label_Address2", messageList);
        validData(address3, false, IntDef.INT_HUNDRED, "CPMCLS02_Label_Address3", messageList);
        validData(address4, false, IntDef.INT_HUNDRED, "CPMCLS02_Label_Address4", messageList);
        validData(contact2, false, IntDef.INT_THIRTY, "CPMCLS02_Label_Contact2", messageList);
        validData(telephone2, false, IntDef.INT_THIRTY, "CPMCLS02_Label_Telephone2", messageList);
        validData(fax2, false, IntDef.INT_THIRTY, "CPMCLS02_Label_Fax2", messageList);
        validData(email2, false, IntDef.INT_HUNDRED, "CPMCLS02_Label_Email2", messageList);
    }

    /**
     * validate input data
     * 
     * @param value String
     * @param notEmpty boolean(true=not empty)
     * @param dbLength dbLength
     * @param labelKey String
     * @param messageList List<BaseMessage>
     * @return boolean
     */
    private boolean validData(String value, boolean notEmpty, int dbLength, String labelKey,
        List<BaseMessage> messageList) {
        if (StringUtil.isEmpty(value)) {
            if (notEmpty) {
                BaseMessage msg = new BaseMessage(MessageCodeConst.W1003_001, new String[] { labelKey });
                messageList.add(msg);
                return false;
            }
        } else {
            if (value.getBytes().length > dbLength) {
                BaseMessage msg = new BaseMessage(MessageCodeConst.W1003_004, new String[] { labelKey,
                    StringUtil.toSafeString(dbLength) });
                messageList.add(msg);
                return false;
            }
        }
        return true;
    }
}
