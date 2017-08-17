/**
 * CPMCLS03Control.
 * 
 * @author shi_yuxi
 * @screen CPMCLS03
 */
package com.chinaplus.web.mm.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.entity.TnmNonTtcCustomer;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.web.mm.service.CPMCLS03Service;

/**
 * non aisin Customer List Screen service
 */
@Controller
public class CPMCLS03Controller extends BaseController {

    @Autowired
    private CPMCLS03Service service;

    /**
     * warehouse list
     * 
     * @param param param
     * @param request request
     * @param response response
     * @return List<ComboData>
     */
    @RequestMapping(value = { "mm/CPMCLS03/getNonList" })
    @ResponseBody
    public PageResult<TnmNonTtcCustomer> getWHSList(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        PageResult<TnmNonTtcCustomer> result = new PageResult<TnmNonTtcCustomer>();
        result.setDatas(service.getNonList());
        return result;
    }

    /**
     * save
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = { "mm/CPMCLS03/save" })
    @ResponseBody
    public void save(@RequestBody ObjectParam<TnmNonTtcCustomer> param, HttpServletRequest request,
        HttpServletResponse response) {

        this.setCommonParam(param, request);

        List<TnmNonTtcCustomer> ttcList = param.getDatas();
        List<TnmNonTtcCustomer> dbList = service.getNonList();
        for (TnmNonTtcCustomer dbCus : dbList) {
            if (!ttcList.contains(dbCus)) {
                service.doDelete(dbCus);
            }
        }
        for (TnmNonTtcCustomer ttcCus : ttcList) {
            if (ttcCus.getNonTtcCustId().equals(-1)) {
                ttcCus.setCreatedBy(param.getLoginUserId());
                ttcCus.setCreatedDate(service.getDBDateTimeByDefaultTimezone());
                ttcCus.setUpdatedBy(param.getLoginUserId());
                ttcCus.setUpdatedDate(service.getDBDateTimeByDefaultTimezone());
                ttcCus.setVersion(IntDef.INT_ONE);
                service.doInsert(ttcCus);
            }
        }
    }
}
