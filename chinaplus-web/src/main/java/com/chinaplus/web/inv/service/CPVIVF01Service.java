/**
 * CPVIVF01Service.java
 * 
 * @screen CPVIVF01
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.web.inv.entity.CPVIVF01Entity;

/**
 * Download Invoice Supplementary Data File Service.
 */
@Service
public class CPVIVF01Service extends BaseService {

    /**
     * Query all invoice supplementary data.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPVIVF01Entity> getSupplementaryData(CPVIVF01Entity condition) {

        return super.baseMapper.select(getSqlId(SQLID_FIND_ALL), condition);
    }

}
