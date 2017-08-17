/**
 * TnmUomService.java
 * 
 * @screen common
 * @author cheng_xingfei
 */
package com.chinaplus.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.UomData;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;

/**
 * TnmUomService.
 * 
 */
@Service("common.TnmUomService")
public class TnmUomService extends BaseService {

    /**
     * get all decimal digits.
     * 
     * @return uom digits list
     */
    public List<UomData> selectAllDigits() {
        BaseParam param = new BaseParam();
        List<UomData> list = baseMapper.select("com.chinaplus.common.service.CommonService.getDecimalDigits", param);
        return list;
    }
}