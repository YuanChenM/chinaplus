/**
 * AccessControlInterceptor.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.util.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.util.StringUtil;

/**
 * The interceptor for request access.
 */
public class AccessControlInterceptor implements HandlerInterceptor {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(AccessControlInterceptor.class);
    /** index.html URL */
    private static final String INDEX_HTML = "/index.html";
    /** logout URL */
    private static final String LOGOUT_URL = "/userLogout";

    /**
     * Intercept the execution of a handler. Called after HandlerMapping determined an appropriate handler object, but
     * before HandlerAdapter invokes the handler.
     * <p>
     * DispatcherServlet processes a handler in an execution chain, consisting of any number of interceptors, with the
     * handler itself at the end. With this method, each interceptor can decide to abort the execution chain, typically
     * sending a HTTP error or writing a custom response.
     * 
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return <code>true</code> if the execution chain should proceed with the next interceptor or the handler itself.
     *         Else, DispatcherServlet assumes that this interceptor has already dealt with the response itself.
     * @throws Exception in case of errors
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isInfoEnabled()) {
            String url = request.getRequestURI();
            if (needInfoLog(url)) {
                UserInfo loginInfo = getLoginInfo(request);
                String sessionid = "";
                if (request.getSession() != null) {
                    sessionid = request.getSession().getId();
                }
                logger.info("[preHandle]before action execute : {} [user:{}, sessionid:{}]", url,
                    loginInfo.getLoginId(), sessionid);
            }
        }
        // if (logger.isDebugEnabled()) {
        // String url = request.getRequestURI();
        // if (!INDEX_HTML.equalsIgnoreCase(url) && !needInfoLog(url)) {
        // logger.debug("【preHandle】before action execute : {}", url);
        // }
        // }

        // ServletWebRequest webRequest = new ServletWebRequest(request);
        // SessionInfoManager.checkToken(request, webRequest.getParameter(SessionInfoManager.TOKEN));

        return true;
    }

    /**
     * Intercept the execution of a handler. Called after HandlerAdapter actually invoked the handler, but before the
     * DispatcherServlet renders the view. Can expose additional model objects to the view via the given ModelAndView.
     * <p>
     * DispatcherServlet processes a handler in an execution chain, consisting of any number of interceptors, with the
     * handler itself at the end. With this method, each interceptor can post-process an execution, getting applied in
     * inverse order of the execution chain.
     * 
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance examination
     * @param modelAndView the <code>ModelAndView</code> that the handler returned (can also be <code>null</code>)
     * @throws Exception in case of errors
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        // set parameter
        Enumeration<?> en = request.getParameterNames();
        StringBuilder strlog = new StringBuilder();
        while (en.hasMoreElements()) {
            String parameterKey = en.nextElement().toString();
            String value = request.getParameter(parameterKey);
            strlog.append(StringUtil.formatMessage("'{'{0}:{1}'}',", parameterKey, value));
            request.setAttribute(parameterKey, value);
        }
        if (logger.isDebugEnabled()) {
            String url = request.getRequestURI();
            if (needInfoLog(url)) {
                logger.debug("Request parameters：[{}]", strlog.toString());
            }
        }

        String viewName = "noModelView";
        // set code define
        if (modelAndView != null) {
            viewName = modelAndView.getViewName();
            // // language
            // Locale locale = (Locale) SessionInfoManager.getContextInstance(request).getLoginLocale();
            // modelAndView.addObject("language", locale);
        }
        if (logger.isInfoEnabled()) {
            String url = request.getRequestURI();
            if (needInfoLog(url)) {
                String loginId = null;
                if (!url.endsWith(LOGOUT_URL)) {
                    UserInfo loginInfo = getLoginInfo(request);
                    loginId = loginInfo.getLoginId();
                }
                logger.info("[postHandle]before view({}) execute : {} [user:{}]", viewName, request.getRequestURI(),
                    loginId);
            }
        }
        // if (logger.isDebugEnabled()) {
        // String url = request.getRequestURI();
        // if (!INDEX_HTML.equalsIgnoreCase(url) && !needInfoLog(url)) {
        // logger.debug("【postHandle】before view({}) execute : {}", viewName, request.getRequestURI());
        // }
        // }
    }

    /**
     * Callback after completion of request processing, that is, after rendering the view. Will be called on any outcome
     * of handler execution, thus allows for proper resource cleanup.
     * <p>
     * Note: Will only be called if this interceptor's <code>preHandle</code> method has successfully completed and
     * returned <code>true</code>!
     * <p>
     * As with the {@code postHandle} method, the method will be invoked on each interceptor in the chain in reverse
     * order, so the first interceptor will be the last to be invoked.
     * 
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance examination
     * @param ex exception thrown on handler execution, if any
     * @throws Exception in case of errors
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {

        if (logger.isInfoEnabled()) {
            String url = request.getRequestURI();
            if (needInfoLog(url)) {
                String loginId = null;
                if (!url.endsWith(LOGOUT_URL)) {
                    UserInfo loginInfo = getLoginInfo(request);
                    loginId = loginInfo.getLoginId();
                }
                logger.info("[afterCompletion]finally execute GC resource : {} [user:{}]", request.getRequestURI(),
                    loginId);
            }
        }
        // if (logger.isDebugEnabled()) {
        // String url = request.getRequestURI();
        // if (!INDEX_HTML.equalsIgnoreCase(url) && !needInfoLog(url)) {
        // logger.debug("【afterCompletion】finally execute GC resource : {}", request.getRequestURI());
        // }
        // }

        // // process for upload
        // if ("uploadCommon".equals(request.getParameter("actionType"))) {
        // response.setContentType("text/html; charset=utf-8");
        // PrintWriter writer = response.getWriter();
        // writer.write("<script type=\"text/javascript\">");
        // writer.write("window.parent.HDF.closeLoadingMask();");
        // writer.write("</script>");
        // }
    }

    /**
     * Get login user information from session
     * 
     * @param request the request
     * @return login user information
     */
    private static UserInfo getLoginInfo(HttpServletRequest request) {
        UserInfo loginInfo = null;
        // get login user information from session
        SessionInfoManager sim = SessionInfoManager.getContextInstance(request);
        loginInfo = sim.getLoginInfo();
        if (loginInfo == null) {
            loginInfo = new UserInfo();
        }
        return loginInfo;
    }

    /**
     * Check if there need to log
     * 
     * @param url request url
     * @return true： need log
     */
    private static boolean needInfoLog(String url) {
        boolean result = true;

        // if index.html, then do not need log
        if (INDEX_HTML.equalsIgnoreCase(url)) {
            result = false;
        }
        // if resource files, then do not need log
        else if (!StringUtil.isEmpty(url)
                && (url.endsWith(".png") || url.endsWith(".gif") || url.endsWith(".wmv") || url.endsWith(".ico")
                        || url.endsWith(".css") || url.endsWith(".js"))) {
            result = false;
        }

        return result;
    }
}
