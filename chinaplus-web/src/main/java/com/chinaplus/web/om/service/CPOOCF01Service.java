/**
 * CPOOCF01Service.
 * 
 * @author shi_yuxi
 * @screen CPOOCF01
 */
package com.chinaplus.web.om.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TntOrder;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.web.om.entity.CPOOCF01CompareByDayEntity;
import com.chinaplus.web.om.entity.CPOOCF01CurrentMonthEntity;
import com.chinaplus.web.om.entity.CPOOCF01DateEntity;
import com.chinaplus.web.om.entity.CPOOCF01Entity;
/**
 * Order Calculation Supporting Data Report Download
 */
@Service
public class CPOOCF01Service extends BaseService{
    
    /**
     * get parts info
     * 
     * @param param param
     * @return List<CPOOCF01Entity>
     */
    public List<CPOOCF01Entity> getMainList(BaseParam param){
        return this.baseMapper.select(getSqlId("findMainList"), param);
    }
    
    /**
     * get balance info
     * 
     * @param param param
     * @return List<TnfBalanceByDay>
     */
    public List<CPOOCF01CompareByDayEntity> getBalance(BaseParam param){
        return this.baseMapper.select(getSqlId("findBalance"), param);
    }
    
    /**
     * get currentmonth info
     * 
     * @param param param
     * @return List<TnfBalanceByDay>
     */
    public List<CPOOCF01CurrentMonthEntity> getCurrentQty(BaseParam param){
        return this.baseMapper.select(getSqlId("findCurrentQty"), param);
    }
    
    /**
     * get tCurrentMonthNo info
     * 
     * @param param param
     * @return List<TnfBalanceByDay>
     */
    public List<TntOrder> getCurrentMonthNo(BaseParam param){
        return this.baseMapper.select(getSqlId("findCurrentQty"), param);
    }
    
    /**
     * get currentmonth info aisin
     * 
     * @param param param
     * @return List<CPOOCF01CurrentMonthEntity>
     */
    public List<CPOOCF01CurrentMonthEntity> getCurrentQtyAisin(BaseParam param){
        return this.baseMapper.select(getSqlId("findCurrentQtyAisin"), param);
    }
    
    
    /**
     * get date info 
     * 
     * @param param param
     * @return List<CPOOCF01DateEntity>
     */
    public List<CPOOCF01DateEntity> getDateList(BaseParam param){
        return this.baseMapper.select(getSqlId("findDateList"), param);
    }
    
    /**
     * check range
     * 
     * @param param param
     * @return List<CPOOCF01Entity>
     */
    public CPOOCF01Entity checkRange(BaseParam param){
        return this.baseMapper.findOne(getSqlId("checkRange"), param);
    }
    
    
    /**
     * getNearestDate
     * 
     * @param param param
     * @return List<CPOOCF01Entity>
     */
    public List<CPOOCF01Entity> getNearestDateList(BaseParam param){
        return this.baseMapper.select(getSqlId("findNearestDate"), param);
    }
}
