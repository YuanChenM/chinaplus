/**
 * @screen CPCUMS03Controller
 * @author zhang_chi
 */
package com.chinaplus.web.com.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.entity.TnmRole;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.web.com.service.CPCUMS03Service;

/**
 * Controller of Role List Screen.
 */
@Controller
public class CPCUMS03Controller extends BaseController {


    /**
     * cpcums03Service.
     */
    @Autowired
    private CPCUMS03Service cpcums03Service;

    /**
     * Get role information from database.
     * 
     * @param request request
     * @return TnmRole
     * @throws Exception e
     */
    @RequestMapping(value = "com/CPCUMS03/loadRoles", method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadRoleInfos(HttpServletRequest request)
        throws Exception {
        
        PageResult<ComboData> result = new PageResult<ComboData>();

        // query all suppliers
        List<TnmRole> roles = cpcums03Service.getAllRoles();

        // convert to combo data
        // id : Role Id, text : Role Name
        List<ComboData> comboRoles = null;
        if (roles != null) {
            comboRoles = new ArrayList<ComboData>();
            for (TnmRole role : roles) {
                ComboData combo = new ComboData();
                combo.setId(String.valueOf(role.getRoleId()));
                combo.setText(role.getRoleName());
                comboRoles.add(combo);
            }
        }
        result.setDatas(comboRoles);
        
        return result;
    }
}
