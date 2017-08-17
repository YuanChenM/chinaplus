/**
 * CPVIVF02Service.java
 * 
 * @screen CPVIVF02
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.web.inv.entity.CPVIVF02Entity;

/**
 * Download For WEST Service.
 */
@Service
public class CPVIVF02Service extends BaseService {

    /**
     * Query all WEST invoice.
     *
     * @param condition the query condition
     * @return the query result
     */
    public List<CPVIVF02Entity> getWestInvoiceList(CPVIVF02Entity condition) {

        return super.baseMapper.select(getSqlId(SQLID_FIND_ALL), condition);
    }

}
