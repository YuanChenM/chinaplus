/**
 * Customer Data in SSMS & ORION PLUS Download/Upload Screen
 * 
 * @screen CPMSMS01
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.web.mm.service.CPMSMS01Service;

/**
 * Customer Data in SSMS & ORION PLUS Download/Upload Screen
 */
@Controller
public class CPMSMS01Controller extends BaseController {

    /**
     * cpmsms01Service.
     */
    @Autowired
    private CPMSMS01Service cpmsms01Service;
    
    /**
     * load  Vendor   Route
     * 
     * @param request the request
     * @param response the response
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMSMS01/loadVendorRoute",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadVendorRoute(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        List<ComboData> comboOffices = cpmsms01Service.getVendorRoute();
        result.setDatas(comboOffices);
        return result;
    }
  
}
