/**
 * @screen CPCUMS03Service
 * @author zhang_chi
 */
package com.chinaplus.web.com.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmRole;
import com.chinaplus.core.base.BaseService;

/**
 * Controller of Role select Screen.
 */
@Service
public class CPCUMS03Service extends BaseService {

    /**
     * Get all roles.
     * 
     * @return all roles
     */
    public List<TnmRole> getAllRoles() {
        TnmRole param = new TnmRole();
        return baseDao.select(param);
    }
}
