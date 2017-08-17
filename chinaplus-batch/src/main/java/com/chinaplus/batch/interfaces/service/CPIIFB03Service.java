/**
 * CPIIFB03Service.java
 * 
 * @screen CPIIFB03
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.IFServiceId;
import com.chinaplus.batch.common.consts.BatchConst.OrionPlusFlag;
import com.chinaplus.batch.interfaces.bean.CPIIFB01Param;
import com.chinaplus.batch.interfaces.bean.Customer;
import com.chinaplus.batch.interfaces.bean.Parts;
import com.chinaplus.common.consts.CodeConst.HandleFlag;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.util.DateTimeUtil;

/**
 * 
 * SSMSCustomer Service.
 * ssms customer logic
 * 
 * @author yang_jia1
 */
@Service
@Component(IFServiceId.SMSS_CUSTOMER)
public class CPIIFB03Service extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB03Service.class);

    /**
     * doCustomerLogic
     * 
     * @param param param
     * @return excuteResult
     */
    public boolean doCustomerLogic(CPIIFB01Param param) {
        logger.debug("---------------batch doCustomerLogic start-------------");

        logger.debug("doCustomerLogic : update table TNM_IF_CUSTOMER");

        Customer mapedParam = new Customer();

        if (param.getIfDateTime() != null) {
            mapedParam.setIfDateTime(param.getIfDateTime());
        }

        this.baseMapper.update(this.getSqlId("modCustomerIfValidFlag"), mapedParam);

        logger.debug("doCustomerLogic : insert table TNM_SSMS_CUSTOMER");
        List<Integer> handleFlagParam = new ArrayList<Integer>();
        handleFlagParam.add(HandleFlag.UNPROCESS);
        mapedParam.setHandleFlagParam(handleFlagParam);
        List<Customer> ifList = this.doQeuryCustomerIf(mapedParam);

        for (Customer ssmsCustomer : ifList) {
            ssmsCustomer.setOrionPlusFlag(OrionPlusFlag.NO);
            Customer oldCustomer = this.doQeuryCustomerSSMS(ssmsCustomer);
            int result = 0;
            if (null != oldCustomer && oldCustomer.getCustomerCode() != null) {

                if (oldCustomer.getVendorRouteSet() != null) {

                    boolean isNewVender = true;
                    String[] vendorRouteSet = oldCustomer.getVendorRouteSet().split(",");
                    if (vendorRouteSet != null) {
                        for (int i = 0; i < vendorRouteSet.length; i++) {
                            if (ssmsCustomer.getVendorRoute().equals(vendorRouteSet[i])) {
                                isNewVender = false;
                            }
                        }
                    }

                    if (isNewVender) {
                        ssmsCustomer.setVendorRouteSet(new StringBuffer().append(oldCustomer.getVendorRouteSet())
                            .append(",").append(ssmsCustomer.getVendorRoute()).toString());
                    } else {
                        ssmsCustomer.setVendorRouteSet(oldCustomer.getVendorRouteSet());
                    }

                } else {
                    ssmsCustomer.setVendorRouteSet(ssmsCustomer.getVendorRoute());
                }

                result = this.doUpdateCustomerSSMS(ssmsCustomer);

                // 2016/07/22 shiyang add start (Update TNM_EXP_PARTS.SSMS_VENDOR_ROUTE)
                this.doUpdatePartsSSMS(ssmsCustomer);
                // 2016/07/22 shiyang add end

            } else {

                ssmsCustomer.setVendorRouteSet(ssmsCustomer.getVendorRoute());
                result = this.doInsertCustomerSSMS(ssmsCustomer);
            }

            if (result >= BatchConst.INT_ONE) {
                // update handle flag 0未处理 1 已处理
                ssmsCustomer.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
            } else {
                // update handle flag 0未处理 1 已处理
                ssmsCustomer.setHandleFlag(HandleFlag.PROCESS_FAILURE);
            }

            this.doUpdateHandleFlag(ssmsCustomer);
        }

        logger.debug("---------------batch doCustomerLogic end-------------");
        return true;
    }

    /**
     * ifContainsDateTime
     * 
     * @param dateTimes dateTimes
     * @return boolean
     */
    public boolean ifContainsDateTime(List<String> dateTimes) {

        List<Integer> handleFlagParam = new ArrayList<Integer>();
        handleFlagParam.add(HandleFlag.UNPROCESS);
        handleFlagParam.add(HandleFlag.PROCESS_FAILURE);
        handleFlagParam.add(HandleFlag.PROCESS_SUCCESS);

        List<Customer> checkList = new ArrayList<Customer>();

        for (int i = 0; i < dateTimes.size(); i++) {
            Customer customer = new Customer();
            Date ifDateTime = DateTimeUtil.parseDate(dateTimes.get(i), DateTimeUtil.FORMAT_YYYYMMDD_HHMM);
            Timestamp stamp = new Timestamp(ifDateTime.getTime());

            customer.setIfDateTime(stamp);
            customer.setHandleFlagParam(handleFlagParam);
            checkList = this.baseMapper.select("getCustomerIfList", customer);
            if (checkList != null && !checkList.isEmpty()) {
                checkList.clear();
                return true;
            }
        }

        return false;

    }

    /**
     * doQeuryCustomerIf
     * 
     * @param customer customer
     * @return List<Customer>
     */
    public List<Customer> doQeuryCustomerIf(Customer customer) {
        return this.baseMapper.select(this.getSqlId("getCustomerIfList"), customer);
    }

    /**
     * doQeuryCustomerSSMS
     * 
     * @param customer customer
     * @return List<Customer>
     */
    public Customer doQeuryCustomerSSMS(Customer customer) {
        List<Customer> customerList = null;
        customerList = this.baseMapper.select(this.getSqlId("checkCustomerSSMSExist"), customer);

        if (customerList != null && !customerList.isEmpty() && customerList.get(0) != null) {
            return customerList.get(0);
        }

        return null;
    }

    /**
     * doUpdateCustomerSSMS
     * 
     * @param customer customer
     * @return int
     */
    public int doUpdateCustomerSSMS(Customer customer) {
        Timestamp currentDate = getDBDateTimeByDefaultTimezone();
        customer.setCreatedDate(currentDate);
        customer.setUpdatedDate(currentDate);
        return this.baseMapper.update(this.getSqlId("modCustomerSSMS"), customer);
    }

    /**
     * doUpdatePartsSSMS
     * 
     * @param customer customer
     * @return int
     */
    public int doUpdatePartsSSMS(Customer customer) {
        Parts parts = new Parts();
        Timestamp currentDate = getDBDateTimeByDefaultTimezone();
        parts.setVendorRoute(customer.getVendorRoute());
        parts.setUpdatedDate(currentDate);
        parts.setUpdatedBy(customer.getUpdatedBy());
        parts.setCustomerCode(customer.getCustomerCode());
        parts.setExpCode(customer.getExpCode());
        return this.baseMapper.update(this.getSqlId("modPartsSSMS"), parts);
    }

    /**
     * doInsertCustomerSSMS
     * 
     * @param customer customer
     * @return int
     */
    public int doInsertCustomerSSMS(Customer customer) {
        Timestamp currentDate = getDBDateTimeByDefaultTimezone();
        customer.setCreatedDate(currentDate);
        customer.setUpdatedDate(currentDate);
        return this.baseMapper.insert(this.getSqlId("addCustomerSSMS"), customer);
    }

    /**
     * doUpdateHandleFlag
     * 
     * @param customer customer
     * @return int
     */
    public int doUpdateHandleFlag(Customer customer) {
        Timestamp currentDate = getDBDateTimeByDefaultTimezone();
        customer.setUpdatedDate(currentDate);
        return this.baseMapper.update(this.getSqlId("modCustomerIfHandleFlag"), customer);
    }
}
