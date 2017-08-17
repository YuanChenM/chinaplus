/**
 * @screen login
 * @author liu_yinchuan
 */
package com.chinaplus.web.com.control;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.util.EncryptManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.CookieUtils;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.com.service.LoginService;

/**
 * 
 * LoginController.
 */
@Controller
public class LoginController {

    /** logger. */
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    /** login check service. */
    @Autowired
    private LoginService loginService;

    /**
     * System automatically obtains the Login ID value from local host cookies file.
     * 
     * @param model the form bean
     * @param request request
     * @param response response
     * @return the return page
     */
    @RequestMapping(value = { "/login" })
    public String init(Model model, HttpServletRequest request, HttpServletResponse response) {

        return "login";
    }

    /**
     * <p>
     * Control the URL which only contain web APP name for Websphere.
     * </p>
     * 
     * 
     * @return the view
     * 
     */
    @RequestMapping(value = "")
    public String index() {
        return "index";
    }

    /**
     * do user login check.
     * 
     * @param userInfo userInfo
     * @param request request
     * @param response response
     * @return checkLoginInfo
     * 
     * @throws Exception e
     */
    @RequestMapping(value = "/login/checklogin")
    @ResponseBody
    public BaseResult<String> checkLoginInfo(@RequestBody UserInfo userInfo, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        logger.debug("the method loginCheck is start");
        
        // ecode pwd
        if(!StringUtil.isEmpty(userInfo.getPwd())) {
            userInfo.setPwd(EncryptManager.encrypt(userInfo.getPwd()));
        }

        // do check
        BaseResult<String> ret = doLoginCheck(userInfo, request);

        // set cookie 
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        CookieUtils utils = new CookieUtils(request, response);
        utils.addCookie(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, context.getLoginInfo().getDefaultLang().toString());

        logger.debug("the method loginCheck is end");
        return ret;
    }
    
    /**
     * do user login check.
     * 
     * @param userInfo userInfo
     * @param request request
     * @return checkLoginInfo
     * 
     * @throws Exception e
     */
    private BaseResult<String> doLoginCheck(UserInfo userInfo, HttpServletRequest request) throws Exception {

        logger.debug("the method loginCheck is start");

        // result
        BaseResult<String> ret = new BaseResult<String>();

        // message entity
        BaseMessage messageEntity = new BaseMessage();

        // check login
        boolean checkFlag = loginService.doCheckLoginInfo(userInfo, messageEntity);

        if (checkFlag) {
            // check success
            loginService.setLoginUserInfo(userInfo);
            // set session
            SessionInfoManager context = SessionInfoManager.getContextInstance(request);
            context.clear();
            context.setLoginInfo(userInfo);

            // get days to expire date
            Date expiryDate = userInfo.getExpiryDate();

            // check expire date range
            if (expiryDate != null) {

                // get today
                Timestamp now = null;
                if (null == userInfo.getUserOffice()  || userInfo.getUserOffice() .size() == NumberConst.IntDef.INT_ZERO) {
                    now = loginService.getDBDateTime(null);
                } else {
                    now = loginService.getDBDateTime(userInfo.getUserOffice() .get(NumberConst.IntDef.INT_ZERO).getTimezone());
                }
                    
                // change to date
                Date expiryDateOnly = DateTimeUtil.parseDate(
                    DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, expiryDate),
                    DateTimeUtil.FORMAT_DATE_YYYYMMDD);
                Date nowDateOnly = DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, now),
                    DateTimeUtil.FORMAT_DATE_YYYYMMDD);
                
                // get date diff
                long days = DateTimeUtil.getDayDifferent(nowDateOnly, expiryDateOnly);

                // if days <= 10, then confirm
                if (days <= NumberConst.IntDef.INT_TEN) {

                    // set SwapData
                    ret.setData(String.valueOf(days));
                }
            }
        } else {

            // set into DataStoreEntity
            throw new BusinessException(messageEntity);
        }

        logger.debug("the method loginCheck is end");
        return ret;
    }

}
