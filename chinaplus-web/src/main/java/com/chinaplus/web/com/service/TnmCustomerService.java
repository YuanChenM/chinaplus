/**
 * TnmCustomerService.java
 * 
 * @screen common
 * @author zhang_pingwu
 */
package com.chinaplus.web.com.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.core.base.BaseService;


/**
 * TnmCustomerService.
 * 
 */
@Service("common.TnmCustomerService")
public class TnmCustomerService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(TnmCustomerService.class);

    /**
     * Load customer data by officeId for combo box.
     * 
     * @param officeId the office id
     * @return customer combo data
     * 
     */
    public List<ComboData> loadCustomerComboByOfficeId(Integer officeId) {
        logger.debug("method loadCustomerComboByOfficeId is start");
        //TODO
        //String hql = "FROM TNM_CUSTOMER WHERE OFFICE_ID = ? ORDER BY CUSTOMER_CODE";
        //Object[] param = new Object[] { officeId };
        
        //List<TnmCustomer> resultList = baseDao.select(hql, param);
        //List<ComboData> customerList = generateOptionsForCombo(resultList);
        logger.debug("method loadCustomerComboByOfficeId is end");
        //return customerList;
        return null;
    }

    /**
     * Generate options for combo box from office list.
     * 
     * @param customerList List<TnmCustomer>
     * @return List<ComboData>
     * 
     */
   /* private List<ComboData> generateOptionsForCombo(List<TnmCustomer> customerList) {
        logger.debug("method generateOptionsForCombo is start");
        List<ComboData> optionList = new ArrayList<ComboData>();
        for (TnmCustomer tnmCustomer : customerList) {
            ComboData entity = new ComboData();
            entity.setId(tnmCustomer.getCustomerCode());
            entity.setText(tnmCustomer.getCustomerCode());
            optionList.add(entity);
        }
        logger.debug("method generateOptionsForCombo is end");
        return optionList;
    }*/


}
