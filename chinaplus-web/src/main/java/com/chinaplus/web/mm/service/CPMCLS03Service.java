/**
 * CPMCLS02Service.
 * 
 * @author shi_yuxi
 * @screen CPMCLS02
 */
package com.chinaplus.web.mm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmNonTtcCustomer;
import com.chinaplus.core.base.BaseService;

/**
 * customer detail screen service
 */
@Service
public class CPMCLS03Service extends BaseService {

    /**
     * getNonList
     * 
     * @return List<TnmNonTtcCustomer>
     */
    public List<TnmNonTtcCustomer> getNonList() {
        return baseDao.select(new TnmNonTtcCustomer());
    }
}
