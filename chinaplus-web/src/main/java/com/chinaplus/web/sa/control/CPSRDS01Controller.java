/**
 * Controller of History Download Screen
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.sa.control;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ZipCompressorUtil;
import com.chinaplus.web.sa.entity.CPSRDS01Entity;
import com.chinaplus.web.sa.service.CPSRDS01Service;

/**
 * Controller of History Download Screen
 */
@Controller
public class CPSRDS01Controller extends BaseFileController {

    /** Rundown History */
    protected static final String RUNDOWN_HISTORY = "RundownHistory";

    /** cpsrds01Service */
    @Autowired
    private CPSRDS01Service cpsrds01Service;

    /**
     * Get Eff From Etd AND Effective to ETD
     * 
     * @param param param
     * @param request request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "/sa/CPSRDS01/loadFromToDate",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<CPSRDS01Entity> loadFromToDate(@RequestBody BaseParam param, HttpServletRequest request)
        throws Exception {

        UserInfo loginUser = getLoginUser(request);
        BaseResult<CPSRDS01Entity> result = new BaseResult<CPSRDS01Entity>();
        CPSRDS01Entity ce = cpsrds01Service.getFromToDate(param.getOfficeTimezone(), loginUser.getLanguage().getCode());
        result.setData(ce);

        return result;
    }

    /**
     * download zip file check
     * 
     * @param param param
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     * @throws Exception e
     */
    @RequestMapping(value = "/sa/CPSRDS01/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        
        boolean byMultiParts = (boolean) param.getSwapData().get("byMultiParts");
        boolean bySinglePart = (boolean) param.getSwapData().get("bySinglePart");
        String from = (String) param.getSwapData().get("from");
        String to = (String) param.getSwapData().get("to");

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        if (!byMultiParts && !bySinglePart) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_025);
            messageLists.add(message);
            //throw new BusinessException(messageLists);
        }

        if (null == from) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
            message.setMessageArgs(new String[] { "CPSRDS01_Label_From" });
            messageLists.add(message);
            //throw new BusinessException(messageLists);
        }

        if (null == to) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
            message.setMessageArgs(new String[] { "CPSRDS01_Label_To" });
            messageLists.add(message);
            //throw new BusinessException(messageLists);
        }
        
        // check others
        Date fromDate = DateTimeUtil.parseDate(from, DateTimeUtil.FORMAT_DATE_YYYYMMDD);
        Date toDate = DateTimeUtil.parseDate(to, DateTimeUtil.FORMAT_DATE_YYYYMMDD);
        if (null != fromDate && null != toDate) {
            if (fromDate.after(toDate)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { "CPSRDS01_Label_From", "CPSRDS01_Label_To" });
                messageLists.add(message);
                throw new BusinessException(messageLists);
            }

            // get date
            Date bDate = DateTimeUtil.addDay(toDate, IntDef.INT_N_TEN);
            if (fromDate.before(bDate)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_026);
                messageLists.add(message);
                throw new BusinessException(messageLists);
            }
        }
        
        // if has error message
        if(!messageLists.isEmpty()) {
            throw new BusinessException(messageLists);
        }
        
        return new BaseResult<BaseEntity>();
    }

    /**
     * download zip file
     * 
     * @param param param
     * @param request request
     * @param response response
     * @throws IOException IOException
     */
    @RequestMapping(value = "/sa/CPSRDS01/download",
        method = RequestMethod.POST)
    public void downloadFile(BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        boolean byMultiParts = (boolean) param.getSwapData().get("byMultiParts");
        boolean bySinglePart = (boolean) param.getSwapData().get("bySinglePart");
        String from = StringUtil.toString(param.getSwapData().get("from"));
        String to = StringUtil.toString(param.getSwapData().get("to"));

        // check others
        Date fromDate = DateTimeUtil.parseDate(from, DateTimeUtil.FORMAT_DATE_YYYYMMDD);
        Date toDate = DateTimeUtil.parseDate(to, DateTimeUtil.FORMAT_DATE_YYYYMMDD);

        // get user information
        UserInfo userInfo = super.getLoginUser(request);
        Language lang = userInfo.getLanguage();
        
        // check vv and aisin flag
        UserOffice office = userInfo.getCurrentOffice();

        // prepare picked up status
        List<String> fileNameList = new ArrayList<String>();
        // if has vv data
        if (office.getVvFlag()) {

            // add vv parts
            fileNameList.addAll(cpsrds01Service.appendFileName(office.getOfficeCode(), BusinessPattern.V_V,
                byMultiParts, bySinglePart, fromDate, toDate, lang));
        }
        // if has aisin
        if (office.getAisinFlag()) {

            // add vv parts
            fileNameList.addAll(cpsrds01Service.appendFileName(office.getOfficeCode(), BusinessPattern.AISIN,
                byMultiParts, bySinglePart, fromDate, toDate, lang));
        }

        // set file name
        StringBuffer zipFileName = new StringBuffer();
        zipFileName.append(RUNDOWN_HISTORY);
        zipFileName.append(StringConst.UNDERLINE);
        zipFileName.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, fromDate));
        zipFileName.append(StringConst.UNDERLINE);
        zipFileName.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, toDate));
        zipFileName.append(StringConst.UNDERLINE);
        zipFileName.append(param.getClientTime());
        zipFileName.append(StringConst.DOT);
        zipFileName.append(FileType.ZIP.getSuffix());
        
        // response content type
        response.setContentType(DEFAULT_CONTENT_TYPE);

        // response character encoding
        response.setCharacterEncoding(ENCODE);

        // response Content-disposition
        response.setHeader("Content-disposition",
            StringUtil.formatMessage("attachment; filename=\"{0}\"",
                encodeClientFileName(zipFileName.toString(), request)));
        
        // get out put stream
        OutputStream ouputStream = response.getOutputStream();
        
        // compress
        ZipCompressorUtil.compressList(fileNameList, ouputStream);
    }

    @Override
    protected String getFileId() {
        return null;
    }

}
