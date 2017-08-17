package com.chinaplus.common.entity;

import java.io.Serializable;

/**
 * The persistent class for the TNM_MAIL_CONFIG database table.
 * 
 */
public class TnmMailConfig extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer mailSendFlag;

    public TnmMailConfig() {}

    public Integer getMailSendFlag() {
        return this.mailSendFlag;
    }

    public void setMailSendFlag(Integer mailSendFlag) {
        this.mailSendFlag = mailSendFlag;
    }
}