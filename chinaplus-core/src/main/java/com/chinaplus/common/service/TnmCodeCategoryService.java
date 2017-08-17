/**
 * TnmCodeCategoryService.java
 * 
 * @screen common
 * @author zhang_pingwu
 */
package com.chinaplus.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmCodeCategory;
import com.chinaplus.core.base.BaseService;

/**
 * CodeCategoryService.
 * 
 */
@Service("common.TnmCodeCategoryService")
public class TnmCodeCategoryService extends BaseService {
    
    /**
     * Query the data list by default sql.
     *
     * @return List<TnmCodeCategory> the result datas
     */
    public List<TnmCodeCategory> findAll() {
        String hql = "FROM TNM_CODE_CATEGORY ORDER BY LANGUAGE_FLAG, CODE_CATEGORY, CODE_VALUE";
        return baseDao.select(hql);
    }
 
}
