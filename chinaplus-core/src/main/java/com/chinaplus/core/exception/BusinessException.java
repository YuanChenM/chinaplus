/**
 * BusinessException.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.exception;

import java.util.ArrayList;
import java.util.List;

import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.StringUtil;

/**
 * The base class of the exception.
 * 
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -2033758305426780486L;

    /** message list */
    private List<BaseMessage> messageList;

    /**
     * Constructs a new exception with <code>null</code> as its detail message. The cause is not initialized, and may
     * subsequently be initialized by a call to {@link #initCause}.
     */
    public BusinessException() {
        super(MessageCodeConst.E0004);
        messageList = new ArrayList<BaseMessage>();
        messageList.add(new BaseMessage(MessageCodeConst.E0004)); // TODO messaeg code define
    }

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     * 
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *        method.
     */
    public BusinessException(String message) {
        super(message);
        messageList = new ArrayList<BaseMessage>();
        messageList.add(new BaseMessage(message));
    }

    /**
     * Constructs a new exception with the detail message list. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     * 
     * @param message the detail message. The message list is saved for later retrieval by the {@link #getMessageList()}
     *        method.
     */
    public BusinessException(BaseMessage message) {
        super();
        messageList = new ArrayList<BaseMessage>();
        messageList.add(message);
    }

    /**
     * Constructs a new exception with the detail message list. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     * 
     * @param messageList the detail message list. The message list is saved for later retrieval by the
     *        {@link #getMessageList()} method.
     */
    public BusinessException(List<BaseMessage> messageList) {
        super();
        this.messageList = messageList;
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *        value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.4
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        messageList = new ArrayList<BaseMessage>();
        messageList.add(new BaseMessage(message));
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of
     * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the class and detail message of
     * <tt>cause</tt>). This constructor is useful for exceptions that are little more than wrappers for other
     * throwables (for example, {@link java.security.PrivilegedActionException}).
     * 
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *        value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.4
     */
    public BusinessException(Throwable cause) {
        super(cause);
        messageList = new ArrayList<BaseMessage>();
        messageList.add(new BaseMessage(this.getMessage())); // TODO messaeg code defineF
    }

    /**
     * get the messageList.
     * 
     * @return messageList
     */
    public List<BaseMessage> getMessageList() {
        return messageList;
    }

    /**
     * set the messageList.
     * 
     * @param messageList messageList
     */
    public void setMessageList(List<BaseMessage> messageList) {
        this.messageList = messageList;
    }

    /**
     * add a message.
     * 
     * @param message message
     */
    public void addMessageList(BaseMessage message) {
        if (messageList == null) {
            messageList = new ArrayList<BaseMessage>();
        }
        messageList.add(message);
    }

    /**
     * Returns the detail message string of this exception.
     *
     * @return the detail message string
     */
    @Override
    public String getMessage() {
        StringBuffer detailMessage = new StringBuffer("");
        String detailMessageStr = "";

        if (messageList != null && !messageList.isEmpty()) {
            for (BaseMessage message : messageList) {
                if (message != null) {
                    if (!StringUtil.isEmpty(detailMessage.toString())) {
                        detailMessage.append(StringConst.COMMA);
                    }
                    detailMessage.append(message.getI18nMessage());
                }
            }
            if (messageList.size() > 1) {
                detailMessageStr = StringUtil.formatMessage("There are many messages.[{0}]", detailMessage.toString());
            } else {
                detailMessageStr = detailMessage.toString();
            }
        }

        if (StringUtil.isEmpty(detailMessageStr)) {
            detailMessageStr = super.getMessage();
        }

        return detailMessageStr;
    }
}
