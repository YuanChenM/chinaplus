/**
 * ContentsController.java
 * 
 * @screen M01TCOAS04
 * @author Common
 */
package com.chinaplus.web.com.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.bean.BaseResult;

/**
 * ContentsController.
 * 
 * 
 * @author COMMON
 */
@Controller
public class LogoutController {

    /**
     * 
     * <p>
     * the initialization method main page.
     * </p>
     * 
     * @param request request
     * @param response response
     * @param status status
     * @return main.jsp
     */
    @RequestMapping(value = "/userLogout")
    @ResponseBody
    public BaseResult<String> logout(HttpServletRequest request,
        HttpServletResponse response, SessionStatus status) {

        // get session manager
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);

        // Clear Application Map.
        context.clear();

        // Invalidate.
        context.destoryContext();

        // set session status
        status.setComplete();

        return new BaseResult<String>();
    }

}
