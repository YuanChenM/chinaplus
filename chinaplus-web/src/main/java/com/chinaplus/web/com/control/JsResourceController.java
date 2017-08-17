package com.chinaplus.web.com.control;

import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.ResourceType;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.ResourceManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.util.CookieUtils;
import com.chinaplus.core.util.StringUtil;

/**
 * JsResourceController.
 */
@Controller
public class JsResourceController {

    /**
     * Get the screen label resource file.
     * 
     * 
     * @param request request
     * @param response response
     * @throws Exception e
     * 
     * @author COMMON
     */
    @RequestMapping(value = { "labelResource.js" })
    protected void prepareLabelResource(HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        // locale
        Locale locale = getLocale(request, response);
        
        StringBuffer jsResource = new StringBuffer();
        // js header
        jsResource.append("Ext.namespace(\"chinaplus.label\");\n");
        jsResource.append("chinaplus.label = ");
        // append js label
        
        ResourceType[] resourceTypes = new ResourceType[] {ResourceType.label, ResourceType.file, ResourceType.validator };
        jsResource.append(ResourceManager.getResourceStringByType(locale, resourceTypes));

        // reset response
        response.setContentType("text/javascript; charset=UTF-8");
        response.setContentLength(jsResource.toString().getBytes().length);
        PrintWriter writer = response.getWriter();
        writer.write(jsResource.toString());
        writer.flush();
    }

    /**
     * Get the message resource file.
     * 
     * 
     * @param request request
     * @param response response
     * @throws Exception e
     * 
     * @author COMMON
     */
    @RequestMapping(value = { "msgResource.js" })
    protected void prepareMsgResource(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // get session
        Locale locale = getLocale(request, response);
        
        StringBuffer jsResource = new StringBuffer();
        // js header
        jsResource.append("Ext.namespace(\"chinaplus.message\");\n");
        jsResource.append("chinaplus.message = ");
        // append js label
        
        jsResource.append(ResourceManager.getResourceStringByType(locale, new ResourceType[] {ResourceType.message}));

        // reset response
        response.setContentType("text/javascript; charset=UTF-8");
        response.setContentLength(jsResource.toString().getBytes().length);
        PrintWriter writer = response.getWriter();
        writer.write(jsResource.toString());
        writer.flush();
    }
    
    /**
     * Get locale form session or cookie.
     * 
     * @param request request
     * @param response response
     * @return locale
     */
    private Locale getLocale(HttpServletRequest request, HttpServletResponse response) {
        
        // SessionInfoManager
        SessionInfoManager session = SessionInfoManager.getContextInstance(request);

        // Locale
        Locale locale = null;

        // check login
        if (session.isLogined()) {
            // get locale
            locale = session.getLoginLocale();
        } else {

            // set as defulat
            locale = session.getLoginLocale();

            // set as
            CookieUtils utils = new CookieUtils(request, response);
            String value = utils.getCookie(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);

            // if no cookie
            if (!StringUtil.isNumeric(value)) {
                // default
                // get locale from request
                if (request.getLocale() != null) {
                    // if belongs to our system
                    if (Language.ENGLISH.getLocale().equals(request.getLocale())) {
                        locale = request.getLocale();
                    }
                }
            } else {
                // get locale
                locale = MessageManager.getLanguage(StringUtil.toInteger(value)).getLocale();
            }
        }

        return locale;
    }
}
