/**
 * AccessControlInterceptor.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.util.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.util.StringUtil;

/**
 * The interceptor for request access.
 */
public class DownLoadControlInterceptor implements HandlerInterceptor {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(DownLoadControlInterceptor.class);

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

        // start to cache download information
        logger.info("start to cache download information");
        // save information into session
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);

        // set session data
        String sessionData = StringUtil.toSafeString(request.getParameter(CoreConst.JSONDATA));
        String token = StringUtil.toSafeString(request.getParameter(CoreConst.KEY_SESSION_TOKEN));
        String screenId = StringUtil.toSafeString(request.getParameter(CoreConst.KEY_SESSION_SCREEN));
        String clientTime = StringUtil.toSafeString(request.getParameter(CoreConst.KEY_SESSION_CLIENTTIME));

        // prepare keys
        String downloadKey = StringUtil.arrayToString(new String[] { sessionData, token, screenId, clientTime });

        // put into session data
        logger.info("cache download key: " + sessionData);
        sm.put(downloadKey, "0");

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
        logger.info("start to remove download information");

        // remove information into session
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);

        // set session data
        String sessionData = StringUtil.toSafeString(request.getParameter(CoreConst.JSONDATA));
        String token = StringUtil.toSafeString(request.getParameter(CoreConst.KEY_SESSION_TOKEN));
        String screenId = StringUtil.toSafeString(request.getParameter(CoreConst.KEY_SESSION_SCREEN));
        String clientTime = StringUtil.toSafeString(request.getParameter(CoreConst.KEY_SESSION_CLIENTTIME));
        
        // prepare keys
        String downloadKey = StringUtil.arrayToString(new String[]{sessionData, token, screenId, clientTime});

        // put into session data
        logger.info("remove download key: " + sessionData);
        sm.remove(downloadKey);
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

        // nothing to do
        // remove information into session
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);

        // set session data
        String sessionData = StringUtil.toSafeString(request.getParameter(CoreConst.JSONDATA));
        String token = StringUtil.toSafeString(request.getParameter(CoreConst.KEY_SESSION_TOKEN));
        String screenId = StringUtil.toSafeString(request.getParameter(CoreConst.KEY_SESSION_SCREEN));
        String clientTime = StringUtil.toSafeString(request.getParameter(CoreConst.KEY_SESSION_CLIENTTIME));
        // prepare keys
        String downloadKey = StringUtil.arrayToString(new String[] { sessionData, token, screenId, clientTime });

        // put into session data
        logger.info("set as download key: " + sessionData);
        if (sm.get(downloadKey) != null) {
            sm.put(downloadKey, "9");
        }
    }
}
