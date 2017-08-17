/**
 * SendEmailService.
 * 
 * @screen common
 * @author shiyang
 */
package com.chinaplus.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmMailConfig;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;

/**
 * SendEmailService.
 */
@Service
public class SendEmailService extends BaseService {

    /**
     * 
     * The Constructors Method.
     *
     */
    public SendEmailService() {}

    /**
     * Get MailSendFlag.<br>
     * 
     * @return true: mailSendFlag = 1
     */
    public boolean getMailSendFlag() {
        BaseParam param = new BaseParam();
        List<TnmMailConfig> list = baseMapper.select(this.getSqlId("getMailSendFlag"), param);
        if (list != null && list.size() > 0) {
            Integer mailSendFlag = list.get(0).getMailSendFlag();
            if (mailSendFlag != null && mailSendFlag.intValue() == 1) {
                return true;
            }
        }
        return false;
    }
}
