/**
 * BaseResult.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The result of the page request.
 * 
 * @param <T> the class type of the entity that is in result
 */
public class BaseResult<T> implements Cloneable, Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** the result */
    private Boolean success = true;
    /** error code */
    private int errorCode;
    /** status code */
    private int statusCode;
    /** the messages */
    private List<BaseMessage> messages;
    /** the data of result */
    private T data;
    /** the token */
    private String token;

    /**
     * The Constructors Method.
     */
    public BaseResult() {
        this.data = null;
    }

    /**
     * 
     * The Constructors Method.
     *
     * @param data the data of result
     *
     * @author ma_b
     */
    public BaseResult(T data) {
        this.data = data;
    }

    /**
     * Get the result.
     * 
     * @return result
     */
    public Boolean getSuccess() {
        return this.success;
    }

    /**
     * Get the messages.
     * 
     * @return messages
     */
    public List<BaseMessage> getMessages() {
        return this.messages;
    }

    /**
     * Set the messages.
     * 
     * @param messages messages
     */
    public void setMessages(List<BaseMessage> messages) {
        this.messages = messages;
    }

    /**
     * Add one message.
     * 
     * @param message message
     */
    public void addMessage(BaseMessage message) {
        if (this.messages == null) {
            this.messages = new ArrayList<BaseMessage>();
        }
        this.messages.add(message);
    }

    /**
     * Get the data.
     * 
     * @return data
     */
    public T getData() {
        return this.data;
    }

    /**
     * Set the data.
     * 
     * @param data data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Get the token.
     * 
     * @return token
     */
    public String getToken() {
        return this.token;
    }

    /**
     * Set the token.
     * 
     * @param token token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Get the errorCode.
     *
     * @return errorCode
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * Set the errorCode.
     *
     * @param errorCode errorCode
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Get the statusCode.
     *
     * @return statusCode
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * Set the statusCode.
     *
     * @param statusCode statusCode
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * 
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return super.toString();
    }

}
