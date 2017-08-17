/**
 * Controller of Stock status report screen
 * 
 * @screen CPSSSS01
 * @author liu_yinchuan
 */
package com.chinaplus.web.sa.control;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.batch.common.command.OnlineBatchMain;
import com.chinaplus.batch.common.consts.BatchConst.BatchStatus;
import com.chinaplus.batch.common.consts.BatchConst.OnlineBatch;
import com.chinaplus.common.bean.TntStockStatusEx;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.service.StockStatusService;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;

/**
 * Stock status screen
 */
@Controller
public class CPSSSS01Controller extends BaseController {

    @Autowired
    private StockStatusService service;

    @Autowired
    private OnlineBatchMain onlineBatch;

    /**
     * Load current office customers.
     * 
     * @param param parameter
     * @param request the request
     * @param response the response
     * @return the combo data of current office customers
     */
    @RequestMapping(value = "sa/CPSSSS01/getStockStatusList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<TntStockStatusEx> getStockStatusList(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {

        // set common information
        this.setCommonParam(param, request);

        // get language
        UserInfo ui = super.getLoginUser(request);
        Language lang = ui.getLanguage();

        // set end
        param.setSwapData("end", param.getStart() + param.getLimit());

        // check vv and aisin flag
        UserOffice office = super.getLoginUser(request).getCurrentOffice();
        // prepare picked up status
        List<Integer> buiPattList = new ArrayList<Integer>();
        // if has vv data
        if (office.getVvFlag()) {
            // add vv parts
            buiPattList.add(BusinessPattern.V_V);
        }
        // if has aisin
        if (office.getAisinFlag()) {
            // add AISIN parts
            buiPattList.add(BusinessPattern.AISIN);
        }
        // set into list
        param.setSwapData("BusinessPatternList", buiPattList);

        // make like condition
        service.makeLikeCondition(param);

        // pick up from database
        PageResult<TntStockStatusEx> result = service.getPageList(param);

        // reset
        result.setDatas(service.prepareStockStatusList(result.getDatas(), lang));

        // return
        return result;
    }

    /**
     * refresh Stock Status batch.
     * 
     * @param param parameter
     * @param request request
     * @param response response
     * @return batch execute success or not
     * @throws Exception I/O exception
     */
    @RequestMapping(value = "sa/CPSSSS01/refreshStockStatus")
    @ResponseBody
    public BaseResult<String> refreshStockStatus(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        // get user
        UserInfo ui = super.getLoginUser(request);

        // check batch is exist or not ?
        if (service.checkBatchIsRun(ui.getOfficeId())) {
            // throw
            throw new BusinessException(MessageCodeConst.W1044);
        }

        // GET CURRENT OFFICE
        UserOffice office = ui.getCurrentOffice();

        // GET DATA FROM DATABASE
        Timestamp officeTime = service.getDBDateTime(office.getTimezone());
        String dateStr = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, officeTime);

        // prepare shell parameter
        String[] shellParam = new String[] { OnlineBatch.CPSSSB00, ui.getCurrentOffice().getOfficeCode(), dateStr };

        // execute shell
        // RunShellManager.runShell(ConfigUtil.get(ChinaPlusConst.Properties.RUN_SHELL_STOCKSTATUS_NOW_DATA), shellParam);
        int result = onlineBatch.doProcess(OnlineBatch.CPSSSB00, shellParam);
        // execute fail
        if (result == BatchStatus.FAIL) {
            // set entity list
            BaseMessage message = new BaseMessage(MessageCodeConst.W1046);
            throw new BusinessException(message);
        }

        // return
        return new BaseResult<String>();
    }

}
