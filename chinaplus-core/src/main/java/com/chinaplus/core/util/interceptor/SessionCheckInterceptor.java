/**
 * SessionCheckInterceptor.java
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

import com.chinaplus.common.util.ScreenInfoManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.exception.AuthenticationException;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.exception.TimeoutException;

/**
 * The interceptor for request access.
 */
public class SessionCheckInterceptor implements HandlerInterceptor {

    /** logger. */
    private static Logger logger = LoggerFactory.getLogger(SessionCheckInterceptor.class);

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

        // check login, check session timeout
        checkLogin(request, response);

        // check token and screen
        checkTokenAndScreen(request);

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
        // nothing to do
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
    }

    /**
     * 
     * <p>
     * Check login status and session status.
     * </p>
     * 
     * @param request current HTTP request
     * @param response current HTTP response
     * @throws BusinessException the exception
     */
    private void checkLogin(HttpServletRequest request, HttpServletResponse response) throws BusinessException {

        // check login
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        if (!context.isLogined()) {
            logger.warn("Not loin or session is time out.");
            throw new TimeoutException();
        }
    }

    /**
     * Check token.
     * 
     * @param request the request
     * 
     */
    protected void checkTokenAndScreen(HttpServletRequest request) {

        // check login
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);

        // get token
        String token = request.getParameter(CoreConst.KEY_SESSION_TOKEN);
        String screenId = request.getParameter(CoreConst.KEY_SESSION_SCREEN);

        // check token
        if (token == null || !token.equals(context.getToken())) {
            logger.warn("Not loin or session is time out.");
            throw new TimeoutException();
        }
        
        // check screen
        if (!ScreenInfoManager.checkScreenAuth(context.getMainMenuResource(), screenId)) {

            logger.warn("Not loin or session is time out.");
            throw new AuthenticationException();
        }
    }

}
