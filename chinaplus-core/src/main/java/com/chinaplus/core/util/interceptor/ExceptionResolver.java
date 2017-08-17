/**
 * ExceptionResolver.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.util.interceptor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.util.WebUtils;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.exception.ScreenInitException;
import com.chinaplus.core.exception.TimeoutException;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * <p>
 * {@link org.springframework.web.servlet.handler.SimpleMappingExceptionResolver} implementation that allows for mapping
 * exception class names to view names, either for a set of given handlers or for all handlers in the DispatcherServlet.
 * </p>
 */
public class ExceptionResolver extends SimpleMappingExceptionResolver {

    /** suffix -- .json */
    private final static String JSON_EXT = "json";
    // /** suffix -- .script */
    // private final static String SCRIPT_EXT = "script";

    /** status error */
    private final static int STATUS_ERR = 201;
    /** max trace length for exception message */
    private final static int MAX_TRACE_LEN = 6;

    private final Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

    private Map<String, Integer> errorCodes = new HashMap<String, Integer>();

    private Integer defaultErrorCode;

    // /**
    // * <p>
    // * The response status.
    // * </p>
    // */
    // static class JsonResponse {
    // /** error message array */
    // public String[] message;
    // /** error code */
    // public int errorCode;
    // /** status code */
    // public int statusCode;
    //
    // /**
    // * <p>
    // * The Constructors Method.
    // * </p>
    // *
    // * @param message error message
    // * @param errorCode error code
    // */
    // public JsonResponse(String[] message, int errorCode) {
    // super();
    // // error message array
    // this.message = message;
    // // error code
    // this.errorCode = errorCode;
    // // status code
    // this.statusCode = STATUS_ERR;
    // }
    // }

    /**
     * Actually resolve the given exception that got thrown during on handler execution, returning a ModelAndView that
     * represents a specific error page if appropriate.
     * <p>
     * May be overridden in subclasses, in order to apply specific exception checks. Note that this template method will
     * be invoked <i>after</i> checking whether this resolved applies ("mappedHandlers" etc), so an implementation may
     * simply proceed with its actual exception handling.
     * 
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the executed handler, or <code>null</code> if none chosen at the time of the exception (for
     *        example, if multipart resolution failed)
     * @param ex the exception that got thrown during handler execution
     * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
     * @see org.springframework.web.servlet.handler.SimpleMappingExceptionResolver#doResolveException(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception ex) {

        // Exception error = getCauseOfException(ex);
        // Exception nestedException = getCauseOfException(ex);
        Exception error = ex;
        // if (nestedException instanceof javax.validation.ConstraintViolationException) {
        // error = nestedException;
        // }
        // if (request.getHeader("x-requested-with") != null) {
        // Expose ModelAndView for chosen error view.
        String viewName = determineViewName(error, request);
        if (viewName != null) {
            // Apply HTTP status code for error views, if specified.
            // Only apply it if we're processing a top-level request.
            Integer statusCode = determineStatusCode(request, viewName);
            if (statusCode != null) {
                applyStatusCodeIfPossible(request, response, statusCode);
            }

            return getModelAndView(viewName, error, request, response);
        } else {
            return null;
        }
    }

    /**
     * Log the given exception at warn level, provided that warn logging has been activated through the
     * {@link #setWarnLogCategory "warnLogCategory"} property.
     * <p>
     * Calls {@link #buildLogMessage} in order to determine the concrete message to log. Always passes the full
     * exception to the logger.
     * 
     * @param ex the exception that got thrown during handler execution
     * @param request current HTTP request (useful for obtaining metadata)
     * @see #setWarnLogCategory
     * @see #buildLogMessage
     * @see org.apache.commons.logging.Log#warn(Object, Throwable)
     * @see org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver#logException(java.lang.Exception,
     *      javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected void logException(Exception ex, HttpServletRequest request) {
        if (this.logger != null && this.logger.isErrorEnabled()) {
            BaseResult<String> result = buildLogMessages(ex);
            List<BaseMessage> messages = result.getMessages();
            StringBuilder messageStr = new StringBuilder();
            if (messages != null && !messages.isEmpty()) {
                for (BaseMessage message : messages) {
                    if (!StringUtil.isEmpty(messageStr.toString())) {
                        messageStr.append(StringConst.SEMICOLON);
                    }
                    messageStr.append(message.getI18nMessage());
                }
            }

            if (ex instanceof TimeoutException) {
                this.logger.error(messageStr.toString());
            } else {
                this.logger.error(messageStr.toString(), ex);
            }
        }
    }

    // /**
    // *
    // * <p>
    // * Get the cause of this exception. if it is a validation exception, then return the base casue. if it is not a
    // * validation exception, then return itself. Return true when this exception is caused by a validation.
    // * </p>
    // *
    // * @param ex the exception
    // * @return true when cause by a validation.
    // */
    // private static Exception getCauseOfException(Exception ex) {
    //
    // // if the exception is a validation exception
    // // if (causeBy instanceof org.springframework.transaction.TransactionSystemException) {
    // Throwable causeEx = ex;
    // while (causeEx != null) {
    // // if (causeEx instanceof javax.validation.ConstraintViolationException) {
    // // // validation exception
    // // return (javax.validation.ConstraintViolationException) causeEx;
    // // } else {
    // // causeEx = causeEx.getCause();
    // // }
    // causeEx = causeEx.getCause();
    // }
    // // }
    //
    // return ex;
    // }

    /**
     * 
     * <p>
     * Return a ModelAndView for the given request, view name and exception.
     * </p>
     * 
     * @param viewName the name of the error view
     * @param ex the exception that got thrown during handler execution
     * @param request current HTTP request (useful for obtaining metadata)
     * @param response current HTTP response (useful for transform jsondata)
     * @return the ModelAndView instance
     */
    protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request,
        HttpServletResponse response) {
        ModelAndView modelView = super.getModelAndView(viewName, ex, request);

        String filename = WebUtils.extractFullFilenameFromUrlPath(viewName);
        String ext = StringUtils.getFilenameExtension(filename);

        if (JSON_EXT.equalsIgnoreCase(ext)) {
            try {
                BaseResult<String> result = buildLogMessages(ex);
                result.setErrorCode(determineErrorCode(request, ex));
                result.setStatusCode(STATUS_ERR);

                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(result);
                if (!StringUtil.isEmpty(json)) {
                    modelView.addObject("json", json);
                }
            } catch (Exception e) {
                logger.error("Exception process fail.", e);
            }
            // } else if (SCRIPT_EXT.equalsIgnoreCase(ext)) {
            // if (ex instanceof DownloadFailException) {
            // modelView.addObject("script", ((DownloadFailException) ex).getCallback());
            // }
        } else {
            modelView.addObject("messages", buildLogMessages(ex));
        }

        return modelView;
    }

    /**
     * 
     * <p>
     * Determine the error code to apply for the given exception.
     * </p>
     * 
     * @param request request current HTTP request (useful for obtaining metadata)
     * @param ex the exception that got thrown during handler execution
     * @return error code
     */
    protected Integer determineErrorCode(HttpServletRequest request, Exception ex) {
        String exName = ex.getClass().getName();
        if (this.errorCodes.containsKey(exName)) {
            return this.errorCodes.get(exName);
        }
        return this.defaultErrorCode;
    }

    /**
     * Build a log message array for the given exception, occured during processing the given request.
     * 
     * @param ex the exception that got thrown during handler execution
     * @return the log message to use
     */
    private static BaseResult<String> buildLogMessages(Exception ex) {
        BaseResult<String> result = new BaseResult<String>();
        List<BaseMessage> messages = null;

        if (ex instanceof MaxUploadSizeExceededException) {
            messages = setMessage(MessageCodeConst.W1001_004,
                new String[] { String.valueOf(ChinaPlusConst.UPLOAD_MAX_SIZE) });
        } else if (ex instanceof ScreenInitException) {
            if (ConfigManager.isDebug()) {
                messages = setMessage(MessageCodeConst.E0005, new String[] { getExceptionMessage(ex) });
            } else {
                messages = setMessage(MessageCodeConst.E0005);
            }

        } else if (ex instanceof FileNotFoundException) {
            if (ConfigManager.isDebug()) {
                messages = setMessage("MessageCodeConst.E0009_001", new String[] { getExceptionMessage(ex) }); // TODO
            } else {
                messages = setMessage("MessageCodeConst.E0009"); // TODO
            }
        } else if (ex instanceof BusinessException) {
            // Add Application Exception
            BusinessException be = (BusinessException) ex;
            messages = be.getMessageList();
            // if (messages != null && !messages.isEmpty()) {
            // messages = new ArrayList<BaseMessage>();
            // int i = 0;
            // for (BaseMessage msg : messages) {
            // JSONObject node = JSONObject.fromObject(msg);
            // messages.add(node.toString());
            // }
            // }
        } else {
            if (ConfigManager.isDebug()) {
                messages = setMessage(MessageCodeConst.E0004, new String[] { getExceptionMessage(ex) });
            } else {
                messages = setMessage(MessageCodeConst.E0004);
            }
        }

        result.setMessages(messages);

        return result;
    }

    /**
     * Set message with parameters
     * 
     * @param messageCode the message code
     * @param args the message paramters
     * @return the message array
     */
    private static List<BaseMessage> setMessage(String messageCode, String... args) {
        BaseMessage messageEntity = new BaseMessage();
        messageEntity.setMessageCode(messageCode);
        if (args != null) {
            messageEntity.setMessageArgs(args);
        }
        // JSONObject node = JSONObject.fromObject(messageEntity);
        // messageList.add(node.toString());
        List<BaseMessage> messageList = new ArrayList<BaseMessage>();
        messageList.add(messageEntity);
        return messageList;
    }

    // /**
    // * <pre>
    // * Get a message array from the given exception.
    // * If this is a validation exception, more than one message is possible,
    // * otherwise return one message.
    // * </pre>
    // *
    // * @param ex an exception
    // * @return the error message array
    // */
    // public static String[] getExceptionMessages(Exception ex) {
    // return buildLogMessages(getCauseOfException(ex));
    // }

    /**
     * <p>
     * get the errorCodes.
     * </p>
     * 
     * @return errorCodes
     */
    public Map<String, Integer> getErrorCodes() {
        return this.errorCodes;
    }

    /**
     * <p>
     * set the errorCodes.
     * </p>
     * 
     * @param errorCodes errorCodes
     */
    public void setErrorCodes(Map<String, Integer> errorCodes) {
        this.errorCodes = errorCodes;
    }

    /**
     * <p>
     * get the defaultErrorCode.
     * </p>
     * 
     * @return defaultErrorCode
     */
    public Integer getDefaultErrorCode() {
        return this.defaultErrorCode;
    }

    /**
     * <p>
     * set the defaultErrorCode.
     * </p>
     * 
     * @param defaultErrorCode defaultErrorCode
     */
    public void setDefaultErrorCode(Integer defaultErrorCode) {
        this.defaultErrorCode = defaultErrorCode;
    }

    /**
     * Get messageInfo from exception.
     * 
     * @param ex exception
     * @return the string of exception
     */
    private static String getExceptionMessage(Exception ex) {
        StringBuffer result = new StringBuffer(ex.toString());
        StackTraceElement[] traceList = ex.getStackTrace();
        int maxSize = traceList.length > MAX_TRACE_LEN ? MAX_TRACE_LEN : traceList.length;

        for (int i = 0; i < maxSize; i++) {
            StackTraceElement trace = traceList[i];
            result.append("\r");
            result.append(trace.toString());
        }

        return result.toString();
    }

}
