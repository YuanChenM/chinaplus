/**
 * BaseController.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.base;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.util.StringUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * The base class of the controller class.
 * </p>
 */
public abstract class BaseController {

    // /** logger */
    // private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /** the request. */
    @Autowired
    private HttpServletRequest request;

    /**
     * Get login user information from session.
     * 
     * @param request the request
     * @return login user information
     */
    protected UserInfo getLoginUser(HttpServletRequest request) {
        return SessionInfoManager.getContextInstance(this.request).getLoginInfo();
    }

    /**
     * Set common parameters to parameter object by session information.
     * 
     * @param param the param from screen
     * @param request the request
     */
    protected void setCommonParam(BaseParam param, HttpServletRequest request) {
        if (param != null) {
            // set login user informations by session
            UserInfo loginUser = getLoginUser(this.request);
            SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
            UserManager userManager = UserManager.getLocalInstance(loginUser);
            if (loginUser != null) {
                param.setLoginUserId(loginUser.getUserId());
                param.setLoginId(loginUser.getLoginId());
                param.setCurrentOfficeId(loginUser.getOfficeId());
                // param.setOfficeTimezone("+0800"); // TODO
                param.setLanguage(loginUser.getLanguage().getCode());

                // access level
                if (!StringUtil.isEmpty(param.getScreenId())) {
                    param.setCurrentAccessLevel(userManager.getAccessLevel(sm.getMainMenuResource()));
                }

                // user office
                if (loginUser.getUserOffice() != null && !loginUser.getUserOffice().isEmpty()) {
                    for (UserOffice office : loginUser.getUserOffice()) {
                        if (office != null) {
                            param.addUserOfficeIds(office.getOfficeId());
                        }
                    }
                }

                // user office
                if (userManager.getCurrentOfficeInfo() != null) {
                    param.setOfficeTimezone(userManager.getCurrentOfficeInfo().getTimezone());
                    param.setCurrentOfficeCode(userManager.getCurrentOfficeInfo().getOfficeCode());
                    param.setOfficeSyncTime(userManager.getCurrentOfficeInfo().getImpSyncTime());
                }
            }
            // set filters and swapdata, avoid null point exception
            if (param.getFilters() == null) {
                param.setFilters(new HashMap<String, Object>());
            }
            if (param.getSwapData() == null) {
                param.setSwapData(new HashMap<String, Object>());
            }

            // set upload process and session key
            param.setUploadProcess(StringUtil.toSafeString(param.getSwapData()
                .get(UploadConst.PARAM_KEY_UPLOAD_PROCESS)));
            param.setSessionKey(StringUtil.toSafeString(param.getSwapData().get(UploadConst.PARAM_KEY_SESSION_KEY)));
        }
    }

    /**
     * Convert to entity.
     * 
     * @param valueType valueType
     * @param <T> BaseParam
     * @return BaseParam
     */
    protected <T extends BaseParam> T convertJsonDataForForm(Class<T> valueType) {
        return convertJsonDataForForm(valueType, this.request);
    }

    /**
     * Convert to entity.
     * 
     * @param valueType valueType
     * @param request request
     * @param <T> BaseParam
     * @return BaseParam
     */
    protected <T extends BaseParam> T convertJsonDataForForm(Class<T> valueType, HttpServletRequest request) {

        // get jsonData
        String jsonData = request.getParameter(CoreConst.JSONDATA);

        try {

            // if json data is null
            if (jsonData == null) {
                // Exception
                throw new Exception();
            }

            // get mapper
            ObjectMapper mapper = new ObjectMapper();
            // ignore
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // return json entity
            return mapper.readValue(jsonData, valueType);

        } catch (Exception e) {

            // do nothing
            try {
                return valueType.newInstance();
            } catch (Exception e1) {
                return null;
            }
        }

    }
}
