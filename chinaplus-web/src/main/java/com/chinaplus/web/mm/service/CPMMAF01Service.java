/**
 * CPMMAF01Service.java
 * 
 * @screen CPMMAF01
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.web.mm.entity.CPMMAF01Entity;


/** 
 * email alert download
 */ 
@Service
public class CPMMAF01Service extends BaseService {
    
    /**
     * get email alertList
     * 
     * @param param param
     * @return List<CPMMAF01Entity>
     */
    public List<CPMMAF01Entity> getEmailAlertList(BaseParam param)
    {
        return baseMapper.select(getSqlId("getEmailAlertList"), param);
    }
}
