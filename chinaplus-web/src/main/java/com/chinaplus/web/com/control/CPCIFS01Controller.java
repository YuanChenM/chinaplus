/**
 * CPCIFS01Controller.java
 * 
 * @screen CPCIFS01
 * @author zhang_chi
 */
package com.chinaplus.web.com.control;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.IFBatchStatus;
import com.chinaplus.common.consts.CodeConst.SyncTimeDataType;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.com.entity.CPCIFS01Entity;
import com.chinaplus.web.com.service.CPCIFS01Service;

/**
 * Information Screen Controller.
 */
@Controller
public class CPCIFS01Controller extends BaseController {

    /**
     * Information Screen Service.
     */
    @Autowired
    private CPCIFS01Service cpcifs01Service;

    /**
     * Load Banner Sync Time.
     * 
     * @param request request
     * @return Banner Sync Time
     * @throws Exception the Exception
     */
    @RequestMapping(value = "/com/CPCIFS01/loadBannerSyncTime",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> loadBannerSyncTime(HttpServletRequest request) throws Exception {

        BaseResult<String> result = new BaseResult<String>();
        UserInfo userInfo = getLoginUser(request);
        if (userInfo != null && userInfo.getCurrentOffice() != null) {
            Integer officeId = userInfo.getCurrentOffice().getOfficeId();
            String officeCode = userInfo.getCurrentOffice().getOfficeCode();
            Integer lang = userInfo.getLanguage().getCode();
            Locale locale = userInfo.getLanguage().getLocale();
            StringBuffer timelabel = new StringBuffer();
            Map<String, CPCIFS01Entity> syncTimeMap = cpcifs01Service.getSyncTimeData();
            List<Integer> failOfficeList = cpcifs01Service.getSsRdBatchResult();
            String dateFormat = DateTimeUtil.FORMAT_DDMMMYYYYHHMM;
            if (Language.CHINESE.getCode() == userInfo.getLanguage().getCode()) {
                dateFormat = DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMM;
            }

            // SSMS Time
            String ssmsLabel = MessageManager.getMessage("CPCIFS01_Label_SsmsDataTime", locale);
            CPCIFS01Entity ssmsData = syncTimeMap.get(SyncTimeDataType.SSMS + StringConst.UNDERLINE);
            if (failOfficeList.size() > 0) {
                timelabel.append("<div style=\"color:red;\">");
            } else {
                timelabel.append("<div>");
            }
            timelabel.append(ssmsLabel).append(StringConst.BLANK);
            if (ssmsData != null) {
                timelabel.append(DateTimeUtil.formatDate(dateFormat, ssmsData.getIfDateTime()));
                if (IFBatchStatus.SUCCESS != ssmsData.getIfBatchStatus()) {
                    timelabel.append(getStatusLabel(lang, ssmsData.getIfBatchStatus()));
                }
            } else {
                timelabel.append("N/A");
            }
            timelabel.append("</div>");

            // LOGISTICS Time
            String lastLabel = MessageManager.getMessage("CPCIFS01_Label_Last", locale);
            String vvLabel = MessageManager.getMessage("CPCIFS01_Label_LogisticsVV", locale);
            String aisinLabel = MessageManager.getMessage("CPCIFS01_Label_LogisticsAisin", locale);
            if (failOfficeList.contains(officeId)) {
                timelabel.append("<div style=\"color:red;\">");
            } else {
                timelabel.append("<div>");
            }
            Integer[] batchTypes = new Integer[] { SyncTimeDataType.TT_LOGIX_VV, SyncTimeDataType.TT_LOGIX_AISIN };
            for (int j = 0; j < batchTypes.length; j++) {
                Integer batchType = batchTypes[j];
                if (officeId == IntDef.INT_ONE && batchType == SyncTimeDataType.TT_LOGIX_AISIN) {
                    continue;
                }
                if (batchType == SyncTimeDataType.TT_LOGIX_AISIN) {
                    timelabel.append("&nbsp;&nbsp;");
                }

                timelabel.append(lastLabel);
                timelabel.append(officeCode);
                timelabel.append(StringConst.BLANK);
                if (SyncTimeDataType.TT_LOGIX_VV == batchType) {
                    timelabel.append(vvLabel);
                } else {
                    timelabel.append(aisinLabel);
                }
                timelabel.append(StringConst.BLANK);
                CPCIFS01Entity ttlogicData = syncTimeMap.get(batchType + StringConst.UNDERLINE + officeId);
                if (ttlogicData != null) {
                    timelabel.append(DateTimeUtil.formatDate(dateFormat, ttlogicData.getIfDateTime()));
                    if (IFBatchStatus.SUCCESS != ttlogicData.getIfBatchStatus()) {
                        timelabel.append(getStatusLabel(lang, ttlogicData.getIfBatchStatus()));
                    }
                } else {
                    timelabel.append("N/A");
                }
            }
            timelabel.append("</div>");

            result.setData(timelabel.toString());
        }
        return result;
    }

    /**
     * Load Office Sync Time.
     * 
     * @param request request
     * @return Office Sync Time
     * @throws Exception the Exception
     */
    @RequestMapping(value = "/com/CPCIFS01/loadOfficeSyncTime",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> loadOfficeSyncTime(HttpServletRequest request) throws Exception {

        BaseResult<String> result = new BaseResult<String>();
        UserInfo userInfo = getLoginUser(request);
        Integer lang = userInfo.getLanguage().getCode();
        Locale locale = userInfo.getLanguage().getLocale();
        StringBuffer timelabel = new StringBuffer();
        Map<String, CPCIFS01Entity> syncTimeMap = cpcifs01Service.getSyncTimeData();
        List<Integer> failOfficeList = cpcifs01Service.getSsRdBatchResult();
        String dateFormat = DateTimeUtil.FORMAT_DDMMMYYYYHHMM;
        if (Language.CHINESE.getCode() == userInfo.getLanguage().getCode()) {
            dateFormat = DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMM;
        }

        // SSMS Time
        String ssmsLabel = MessageManager.getMessage("CPCIFS01_Label_SsmsDataTime", locale);
        CPCIFS01Entity ssmsData = syncTimeMap.get(SyncTimeDataType.SSMS + StringConst.UNDERLINE);
        if (failOfficeList.size() > 0) {
            timelabel.append("<div style=\"color:red;\">");
        } else {
            timelabel.append("<div>");
        }
        timelabel.append(ssmsLabel).append(StringConst.BLANK);
        if (ssmsData != null) {
            timelabel.append(DateTimeUtil.formatDate(dateFormat, ssmsData.getIfDateTime()));
            if (IFBatchStatus.SUCCESS != ssmsData.getIfBatchStatus()) {
                timelabel.append(getStatusLabel(lang, ssmsData.getIfBatchStatus()));
            }
        } else {
            timelabel.append("N/A");
        }
        timelabel.append("</div>");

        // LOGISTICS Time
        String lastLabel = MessageManager.getMessage("CPCIFS01_Label_Last", locale);
        String vvLabel = MessageManager.getMessage("CPCIFS01_Label_LogisticsVV", locale);
        String aisinLabel = MessageManager.getMessage("CPCIFS01_Label_LogisticsAisin", locale);
        Integer[] batchTypes = new Integer[] { SyncTimeDataType.TT_LOGIX_VV, SyncTimeDataType.TT_LOGIX_AISIN };
        Integer[] officeIds = new Integer[] { IntDef.INT_THREE, IntDef.INT_TWO, IntDef.INT_ONE };
        Map<Integer, String> officeMap = cpcifs01Service.getOfficeMap();
        for (int i = 0; i < officeIds.length; i++) {
            Integer officeId = officeIds[i];
            for (int j = 0; j < batchTypes.length; j++) {
                Integer batchType = batchTypes[j];
                if (officeId == IntDef.INT_ONE && batchType == SyncTimeDataType.TT_LOGIX_AISIN) {
                    continue;
                }
                if (failOfficeList.contains(officeId)) {
                    timelabel.append("<div style=\"color:red;\">");
                } else {
                    timelabel.append("<div>");
                }
                timelabel.append(lastLabel);
                timelabel.append(StringUtil.toSafeString(officeMap.get(officeId)));
                timelabel.append(StringConst.BLANK);
                if (SyncTimeDataType.TT_LOGIX_VV == batchType) {
                    timelabel.append(vvLabel);
                } else {
                    timelabel.append(aisinLabel);
                }
                timelabel.append(StringConst.BLANK);
                CPCIFS01Entity ttlogicData = syncTimeMap.get(batchType + StringConst.UNDERLINE + officeId);
                if (ttlogicData != null) {
                    timelabel.append(DateTimeUtil.formatDate(dateFormat, ttlogicData.getIfDateTime()));
                    if (IFBatchStatus.SUCCESS != ttlogicData.getIfBatchStatus()) {
                        timelabel.append(getStatusLabel(lang, ttlogicData.getIfBatchStatus()));
                    }
                } else {
                    timelabel.append("N/A");
                }
                timelabel.append("</div>");
            }
        }

        result.setData(timelabel.toString());
        return result;
    }

    /**
     * Get Status Label.
     * 
     * @param lang language
     * @param ifBatchStatus IF batch status
     * @return status label
     */
    private String getStatusLabel(Integer lang, Integer ifBatchStatus) {

        String status = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.IF_BATCH_STATUS, ifBatchStatus);
        return " <label style=\"color:red;font-weight:bold;\">(" + status + ")</label>";
    }

}
