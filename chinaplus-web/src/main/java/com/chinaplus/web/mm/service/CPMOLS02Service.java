/**
 * 
 * CPMOLS02Service.
 * 
 * @author shi_yuxi
 * @screen CPMOLS02
 */
package com.chinaplus.web.mm.service;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.web.mm.entity.CPMOLS02Entity;

/**
 * Office List Screen detail
 */
@Service
public class CPMOLS02Service extends BaseService {

    /**
     * update
     * 
     * @param entity CPMOLS02Entity
     * @return 0/1
     */
    public int doModifyOffice(CPMOLS02Entity entity) {
        return baseMapper.update("modifyOffice", entity);
    }
    
    /**
     * insert
     * @param entity CPMOLS02Entity
     * @return int
     */
    public int doInsertOffice(CPMOLS02Entity entity)
    {
        return baseMapper.insert(getSqlId("insertEntity"), entity);
    }
}
