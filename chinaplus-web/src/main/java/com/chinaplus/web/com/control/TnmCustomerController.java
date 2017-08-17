/**
 * TnmCustomerController.java
 * 
 * @screen common
 * @author zhang_pingwu
 */
package com.chinaplus.web.com.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.web.com.service.TnmCustomerService;

/**
 * TnmCustomerController.
 * 
 */
@Controller
public class TnmCustomerController extends BaseController {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(TnmCustomerController.class);

    @Autowired
    TnmCustomerService customerService;

    /**
     * Load customer Combo data by user imp info for combo box.
     * 
     * @param param the parameters from screen 
     * @param request the request
     * @return List<ComboData>
     * 
     */
    @RequestMapping(value = "/loadCustomerComboByLoginUser", method = RequestMethod.POST)
    @ResponseBody
    public List<ComboData> loadCustomerComboByLoginUser(BaseParam param, HttpServletRequest request) {
        logger.debug("method loadCustomerComboByLoginUser is start");
        Integer officeId = getLoginUser(request).getOfficeId();

        List<ComboData> optionList = customerService.loadCustomerComboByOfficeId(officeId);
        logger.debug("method loadCustomerComboByLoginUser is end");
        return optionList;
    }


}
