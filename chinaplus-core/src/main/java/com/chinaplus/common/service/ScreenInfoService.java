/**
 * TnmUomService.java
 * 
 * @screen common
 * @author cheng_xingfei
 */
package com.chinaplus.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.TnmAuthorizationEx;
import com.chinaplus.core.base.BaseService;

/**
 * TnmUomService.
 * 
 */
@Service("common.ScreenInfoService")
public class ScreenInfoService extends BaseService {

    /**
     * get all screen information.
     * 
     * @return uom digits list
     */
    public List<TnmAuthorizationEx> selectAllScreenAuthInfo() {
        TnmAuthorizationEx screenParam = new TnmAuthorizationEx();
        List<TnmAuthorizationEx> list = baseMapper.select(this.getSqlId("getScreenInfo"), screenParam);
        return list;
    }
}