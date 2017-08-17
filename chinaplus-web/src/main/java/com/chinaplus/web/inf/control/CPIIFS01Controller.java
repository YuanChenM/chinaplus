/**
 * Controller of Irregular I/F Data Handle Screen.
 * 
 * @screen CPIIFS01
 * @author zhang_chi
 */
package com.chinaplus.web.inf.control;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
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
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.CodeConst.IFBatchId;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataType;
import com.chinaplus.common.service.CommonService;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.FileUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ZipCompressorUtil;
import com.chinaplus.web.inf.entity.CPIIFS01Entity;
import com.chinaplus.web.inf.service.CPIIFS01Service;

/**
 * Controller of Irregular I/F Data Handle Screen.
 */
@Controller
public class CPIIFS01Controller extends BaseFileController {

    /** FAILURE */
    protected static final String FAILURE = "Failure";

    @Autowired
    CommonService commonService;

    @Autowired
    private OnlineBatchMain onlineBatch;

    /**
     * cpiifs01Service.
     */
    @Autowired
    private CPIIFS01Service cpiifs01Service;

    /**
     * get list for screen CPIIFS03 by filter.
     * 
     * @param param param
     * @param request request
     * @return PageResult
     * @throws Exception e
     */
    @RequestMapping(value = "inf/CPIIFS01/getCPIIFS01EntityList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPIIFS01Entity> getDetailsList(@RequestBody PageParam param, HttpServletRequest request)
        throws Exception {

        setCommonParam(param, request);
        // find data by paging
        PageResult<CPIIFS01Entity> result = cpiifs01Service.getPageList(param);
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
    @RequestMapping(value = "inf/CPIIFS01/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
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
    @RequestMapping(value = "inf/CPIIFS01/download",
        method = RequestMethod.POST)
    public void downloadFile(BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        String filespace = ConfigManager.getFailureFilePath();
        // file Path
        File filePath = new File(filespace);
        
        // prepare file name
        Integer batchType = StringUtil.toInteger(param.getSwapData().get("batchType"));
        String officeCode = StringUtil.toSafeString(param.getSwapData().get("officeCode"));

        // prepare file name
        StringBuffer fileFilter = new StringBuffer();
        if (batchType != null && batchType.equals(SyncTimeDataType.SSMS)) {
            fileFilter.append(IFBatchId.SSMS_MAIN);
        } else {
            officeCode = officeCode.replaceAll(StringConst.COLON, StringConst.EMPTY);
            fileFilter.append(IFBatchId.TTLOGIC_MAIN).append(StringConst.UNDERLINE).append(officeCode);
        }
        
        // get all list 
        List<File> listFiles = FileUtil.getFilesFromPath(filePath, fileFilter.toString(), null);

        // set file name
        String name = FAILURE + StringConst.UNDERLINE + "{0}.zip";
        String zipFileName = StringUtil.formatMessage(name, param.getClientTime());

        // response content type
        response.setContentType(DEFAULT_CONTENT_TYPE);

        // response character encoding
        response.setCharacterEncoding(ENCODE);

        // response Content-disposition
        response.setHeader("Content-disposition",
            StringUtil.formatMessage("attachment; filename=\"{0}\"", encodeClientFileName(zipFileName, request)));

        OutputStream ouputStream = response.getOutputStream();
        // compress
        ZipCompressorUtil.compressFileList(listFiles, ouputStream);
    }

    @Override
    protected String getFileId() {
        return null;
    }

    /**
     * Call shell.
     * 
     * @param param param
     * @param request request
     * @param response response
     * @return run result
     * @throws Exception Exception
     */
    @RequestMapping(value = "inf/CPIIFS01/synchronizeNowData",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> synchronizeNowData(BaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        // get user
        UserInfo ui = super.getLoginUser(request);

        // check batch is exist or not ?
        if (cpiifs01Service.checkBatchIsRun(ui.getOfficeId())) {
            // throw
            throw new BusinessException(MessageCodeConst.W1044);
        }

        // GET CURRENT OFFICE
        UserOffice office = ui.getCurrentOffice();

        // GET DATA FROM DATABASE
        Timestamp officeTime = cpiifs01Service.getDBDateTime(office.getTimezone());
        String dateStr = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, officeTime);
        String configPrefix = "batch.ttlogix.";
        String officeCodeForDelColon = office.getOfficeCode().replaceAll(StringConst.COLON, StringConst.EMPTY);
        String bussinessPattern = ConfigManager.getProperty(new StringBuffer().append(configPrefix).append(officeCodeForDelColon).toString());

        // prepare shell parameter
        String[] shellParam = new String[] { OnlineBatch.CPIIFB00, bussinessPattern, office.getOfficeCode(), dateStr };

        // execute shell
        //RunShellManager.runShell(ConfigUtil.get(ChinaPlusConst.Properties.RUN_SHELL_STOCKSTATUS_NOW_DATA), shellParam);
        int result = onlineBatch.doProcess(OnlineBatch.CPIIFB00, shellParam);
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
