/**
 * MessageEntity.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.bean;

import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseEntity;

/**
 * MessageEntity.
 */
public class BaseMessage extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 930910777431216057L;

    /** [required] the message code */
    private String messageCode;
    /** [required] the message type, default is error */
    private Type type;
    /** [optional] the message parameters */
    private String[] messageArgs;
    /** [optional] the data row number */
    private int lineNumber;
    /** [optional] the data field name */
    private String fieldName;
    /** [optional] the file that be checked */
    private String checkFile;
    /** [optional] the check result file */
    private String resultFile;

    /**
     * The message type
     */
    public static enum Type {
        /** error message */
        ERROR("Error"),
        /** warning message */
        WARN("Warning"),
        /** information message */
        INFO("Info");

        /** type name */
        private String name;

        /**
         * The Constructors Method.
         * 
         * @param name type name
         */
        Type(String name) {
            this.name = name;
        }

        /**
         * Get the type name.
         * 
         * @return the type name
         */
        public String getName() {
            return this.name;
        }
    }

    /**
     * The Constructors Method.
     */
    public BaseMessage() {
        this.type = Type.ERROR;
    }

    /**
     * The Constructors Method.
     *
     * @param messageCode the message code
     */
    public BaseMessage(String messageCode) {
        this.type = Type.ERROR;
        this.messageCode = messageCode;
    }

    /**
     * The Constructors Method.
     *
     * @param messageCode the message code
     * @param messageArgs the message parameters
     */
    public BaseMessage(String messageCode, String[] messageArgs) {
        this.type = Type.ERROR;
        this.messageCode = messageCode;
        this.messageArgs = messageArgs;
    }

    /**
     * The Constructors Method.
     *
     * @param fieldName the message code
     * @param messageCode the message code
     */
    public BaseMessage(String fieldName, String messageCode) {
        this.type = Type.ERROR;
        this.fieldName = fieldName;
        this.messageCode = messageCode;
    }

    /**
     * The Constructors Method.
     *
     * @param fieldName the message code
     * @param messageCode the message code
     * @param messageArgs the message parameters
     */
    public BaseMessage(String fieldName, String messageCode, String[] messageArgs) {
        this.type = Type.ERROR;
        this.fieldName = fieldName;
        this.messageCode = messageCode;
        this.messageArgs = messageArgs;
    }

    /**
     * The Constructors Method.
     *
     * @param lineNumber the data row number
     * @param fieldName the message code
     * @param messageCode the message code
     */
    public BaseMessage(int lineNumber, String fieldName, String messageCode) {
        this.type = Type.ERROR;
        this.lineNumber = lineNumber;
        this.fieldName = fieldName;
        this.messageCode = messageCode;
    }

    /**
     * The Constructors Method.
     *
     * @param lineNumber the data row number
     * @param fieldName the message code
     * @param messageCode the message code
     * @param messageArgs the message parameters
     */
    public BaseMessage(int lineNumber, String fieldName, String messageCode, String[] messageArgs) {
        this.type = Type.ERROR;
        this.lineNumber = lineNumber;
        this.fieldName = fieldName;
        this.messageCode = messageCode;
        this.messageArgs = messageArgs;
    }

    /**
     * Get the lineNumber.
     * 
     * @return lineNumber
     */
    public int getLineNumber() {
        return this.lineNumber;
    }

    /**
     * Set the lineNumber.
     * 
     * @param lineNumber lineNumber
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Get the fieldName.
     * 
     * @return fieldName
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * Set the fieldName.
     * 
     * @param fieldName fieldName
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Get the messageCode.
     * 
     * @return messageCode
     */
    public String getMessageCode() {
        return this.messageCode;
    }

    /**
     * Set the messageCode.
     * 
     * @param messageCode messageCode
     */
    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    /**
     * Get the type.
     * 
     * @return type
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Set the type.
     * 
     * @param type message type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Get the messageArgs.
     * 
     * @return messageArgs
     */
    public String[] getMessageArgs() {
        return this.messageArgs;
    }

    /**
     * Set the messageArgs.
     * 
     * @param messageArgs messageArgs
     */
    public void setMessageArgs(String[] messageArgs) {
        this.messageArgs = messageArgs;
    }

    /**
     * Get the checkFile.
     * 
     * @return checkFile
     */
    public String getCheckFile() {
        return this.checkFile;
    }

    /**
     * Set the checkFile.
     * 
     * @param checkFile checkFile
     */
    public void setCheckFile(String checkFile) {
        this.checkFile = checkFile;
    }

    /**
     * Get the resultFile.
     * 
     * @return resultFile
     */
    public String getResultFile() {
        return this.resultFile;
    }

    /**
     * Set the resultFile.
     * 
     * @param resultFile resultFile
     */
    public void setResultFile(String resultFile) {
        this.resultFile = resultFile;
    }

    /**
     * Return the international message.
     * 
     * @return the international message
     */
    public String getI18nMessage() {
        String message = "";
        if (this.messageArgs != null && this.messageArgs.length > 0) {
            String[] params = new String[this.messageArgs.length];
            for (int i = 0; i < this.messageArgs.length; i++) {
                params[i] = MessageManager.getMessage(this.messageArgs[i]);
            }
            message = MessageManager.getMessage(this.messageCode, params);
        } else {
            message = MessageManager.getMessage(this.messageCode);
        }

        return message;
    }

}
