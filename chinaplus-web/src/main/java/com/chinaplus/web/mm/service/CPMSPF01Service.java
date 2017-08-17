/**
 * CPMSPF01Service.java
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmReason;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;

/**
 * Shipping Plan Rev Reason Download
 */
@Service
public class CPMSPF01Service extends BaseService {

    /**
     * get getReasonList
     * 
     * @param param param
     * @return List<TnmReason>
     */
    public List<TnmReason> getReasonList(BaseParam param) {
        return baseMapper.select(getSqlId("getReasonList"), param);
    }
}
