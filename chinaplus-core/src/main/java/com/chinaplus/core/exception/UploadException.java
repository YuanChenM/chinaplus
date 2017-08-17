/**
 * UploadException.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.exception;

import java.util.List;

import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.consts.MessageCodeConst;

/**
 * The exception when download file is fail.
 */
public class UploadException extends BusinessException {

    /** serialVersionUID */
    private static final long serialVersionUID = -3458922853154805745L;

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     */
    public UploadException() {
        super(MessageCodeConst.W1001);
    }

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     * 
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *        method.
     */
    public UploadException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     * 
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *        value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.4
     */
    public UploadException(Throwable cause) {
        super(MessageCodeConst.W1001, cause);
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
    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the detail message list. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     * 
     * @param message the detail message. The message list is saved for later retrieval by the {@link #getMessageList()}
     *        method.
     */
    public UploadException(BaseMessage message) {
        super(message);
    }

    /**
     * Constructs a new exception with the detail message list. The cause is not initialized, and may subsequently
     * be initialized by a call to {@link #initCause}.
     * 
     * @param messageList the detail message list. The message list is saved for later retrieval by the
     *        {@link #getMessageList()} method.
     */
    public UploadException(List<BaseMessage> messageList) {
        super(messageList);
    }
}
