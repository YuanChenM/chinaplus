/**
 * CPKKPS02Controller.java
 * 
 * @screen CPKKPS02
 * @author shiyang
 */
package com.chinaplus.web.kbp.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.entity.TntKanban;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.kbp.entity.CPKKPS02Entity;
import com.chinaplus.web.kbp.service.CPKKPS02Service;
import com.chinaplus.web.kbp.service.CPKKPS02UpdateService;

/**
 * Kanban Plan Detailed Information Screen Controller.
 */
@Controller
public class CPKKPS02Controller extends BaseController {

    /** Uploaded excel file name ({Kanban Plan No. on the screen} + "_" + {Kanban Id}+".xlsx") */
    private static final String UPLOAD_FILE_NAME = "{0}_{1}.xlsx";

    /** Download zip file name ({TNT_KANBAN.KANBAN_PLAN_NO} + "_" + yyyyMMddHHmmssSSS + ".zip") */
    private static final String ZIP_FILE_NAME = "Kanban Plan History_{0}.zip";

    /** File encode */
    private static final String ENCODE = "UTF-8";

    /** ZIP file content type */
    private static final String ZIP_CONTENT_TYPE = "application/x-zip-compressed;charset=UTF-8";

    /** Buffer size */
    private static final int BUFFER_SIZE = 1024;

    /** Kanban Plan Detailed Information Screen Service */
    @Autowired
    private CPKKPS02Service cpkkps02Service;

    /** Kanban Plan Detailed Information Screen Service */
    @Autowired
    private CPKKPS02UpdateService cpkkps02UpdateService;

    /**
     * Get kanban info.
     * 
     * @param param ObjectParam<String>
     * @param request HttpServletRequest
     * @return kanban info
     */
    @RequestMapping(value = "/kbp/CPKKPS02/loadKanbanInfo",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<CPKKPS02Entity> getKanbanInfo(@RequestBody ObjectParam<TntKanban> param,
        HttpServletRequest request) {

        super.setCommonParam(param, request);
        CPKKPS02Entity entity = cpkkps02Service.getKanbanInfo(param);
        BaseResult<CPKKPS02Entity> result = new BaseResult<CPKKPS02Entity>();
        result.setData(entity);
        return result;
    }

    /**
     * Load part info list.
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @return part info list
     */
    @RequestMapping(value = "/kbp/CPKKPS02/loadPartInfoList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPKKPS02Entity> getPartInfoList(@RequestBody PageParam param, HttpServletRequest request) {

        super.setCommonParam(param, request);
        StringUtil.buildLikeCondition(param, "ttcPartsNo");
        param.setFilter("kanbanId", param.getSwapData().get("kanbanId"));
        PageResult<CPKKPS02Entity> result = cpkkps02Service.getPartInfoList(param);
        return result;
    }

    /**
     * Load upload info list.
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @return upload info list
     */
    @RequestMapping(value = "/kbp/CPKKPS02/loadUploadInfoList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPKKPS02Entity> getUploadInfoList(@RequestBody PageParam param, HttpServletRequest request) {

        super.setCommonParam(param, request);
        StringUtil.buildLikeCondition(param, "kanbanPlanNoDisplay");
        StringUtil.buildDateTimeCondition(param, "lastUploadTime");

        // Selected record in CPKKPS01
        param.setFilter("kanbanPlanNo", param.getSwapData().get("kanbanPlanNo"));

        PageResult<CPKKPS02Entity> result = cpkkps02Service.getUploadInfoList(param);
        List<CPKKPS02Entity> list = result.getDatas();
        for (int i = 0; i < list.size(); i++) {
            int airFlag = list.get(i).getAirFlag().intValue();
            int seaFlag = list.get(i).getSeaFlag().intValue();
            int language = param.getLanguage();

            if (airFlag == 1 && seaFlag == 1) {
                list.get(i).setTransportMode(
                    CodeCategoryManager.getCodeName(language, CodeMasterCategory.TRANSPORT_MODE,
                        CodeConst.TransportMode.SEA)
                            + StringConst.COMMA
                            + CodeCategoryManager.getCodeName(language, CodeMasterCategory.TRANSPORT_MODE,
                                CodeConst.TransportMode.AIR));
            } else if (seaFlag == 1) {
                list.get(i).setTransportMode(
                    CodeCategoryManager.getCodeName(language, CodeMasterCategory.TRANSPORT_MODE,
                        CodeConst.TransportMode.SEA));
            } else if (airFlag == 1) {
                list.get(i).setTransportMode(
                    CodeCategoryManager.getCodeName(language, CodeMasterCategory.TRANSPORT_MODE,
                        CodeConst.TransportMode.AIR));
            }

            // Total Order Qty-latest, Total On Shipping Qty-latest, Total Inbound Qty-latest, Total Order
            // Balance-latest
            if (i == 0) {
                list.get(i).setTotalOrderQty(list.get(i).getOrderQty());
                list.get(i).setTotalOnShippingQty(list.get(i).getOnShippingQty());
                list.get(i).setTotalInboundQty(list.get(i).getInboundQty());
                list.get(i).setTotalBalanceQty(list.get(i).getOrderBalance());
            }
        }

        return result;
    }

    /**
     * Force Complete.
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @return BaseResult
     * @throws Exception exception
     */
    @RequestMapping(value = "/kbp/CPKKPS02/forceComplete",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> forceComplete(@RequestBody ObjectParam<CPKKPS02Entity> param, HttpServletRequest request)
        throws Exception {

        super.setCommonParam(param, request);

        // Check the Kanban plan is already cancelled or not.
        int countCancelledKanban = cpkkps02Service.getCancelledKanbanCount(param);
        if (countCancelledKanban > 0) {
            throw new BusinessException(MessageCodeConst.W1033);
        }

        List<CPKKPS02Entity> selectedList = param.getDatas();
        for (int i = 0; i < selectedList.size(); i++) {
            CPKKPS02Entity entity = selectedList.get(i);

            // Selected data's Status is not Pending
            if (entity.getPartsStatus() != CodeConst.KanbanPartsStatus.PENDING) {
                throw new BusinessException(MessageCodeConst.W1034);
            }

            // Selected data's On Shipping Qty is not 0
            BigDecimal partsOnShippingQty = cpkkps02Service.getPartsOnShippingQty(entity);
            if (BigDecimal.ZERO.compareTo(partsOnShippingQty) != 0) {
                throw new BusinessException(MessageCodeConst.W1027);
            }
        }

        // Update TNT_KANBAN_PARTS's status.
        // Update TNF_ORDER_STATUS's FORCE_COMPLETED_QTY.
        cpkkps02UpdateService.doForceComplete(param);

        BaseResult<String> result = new BaseResult<String>();
        return result;
    }

    /**
     * Download check for Kanban Plan Upload History File.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return check result
     */
    @RequestMapping(value = "/kbp/CPKKPS02/downloadHistoryCheck")
    @ResponseBody
    public BaseResult<String> downloadCheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {

        return new BaseResult<String>();
    }

    /**
     * Download Kanban Plan Upload History File.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the exception
     */
    @RequestMapping(value = "/kbp/CPKKPS02/download")
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Create the download ZIP file ({TNT_KANBAN.KANBAN_PLAN_NO} + yyyyMMddHHmmssSSS + ".zip")
        String zipFileName = StringUtil.formatMessage(ZIP_FILE_NAME, param.getClientTime());
        response.setContentType(ZIP_CONTENT_TYPE);
        response.setCharacterEncoding(ENCODE);
        response
            .setHeader("Content-disposition", StringUtil.formatMessage("attachment; filename=\"{0}\"", zipFileName));

        byte[] buffer = new byte[BUFFER_SIZE];
        int length = 0;

        OutputStream os = null;
        ZipOutputStream zos = null;
        BufferedInputStream bis = null;
        try {
            os = response.getOutputStream();
            zos = new ZipOutputStream(os);

            // Get uploaded folder path
            String uploadFolderPath = ConfigUtil.get(Properties.UPLOAD_PATH_KANBAN);

            // Get selected datas and copy uploaded excel from upload folder to temporary folder
            List<String> selectedDatas = (List<String>) param.getSelectedDatas();
            for (int i = 0; i < selectedDatas.size(); i++) {
                String line = selectedDatas.get(i);
                String[] data = line.split(StringConst.NEW_COMMA);
                // Kanban ID
                String kanbanId = data[0];
                // Kanban Plan No + Reason Version
                String kanbanPlanNoDisplay = data[NumberConst.IntDef.INT_TWO];

                // {Kanban Plan No. on the screen} + "_" + {Kanban Id}+".xlsx"
                String sourceFileName = kanbanId + CoreConst.SUFFIX_EXCEL;
                String downloadFileName = StringUtil.formatMessage(UPLOAD_FILE_NAME, kanbanPlanNoDisplay, kanbanId);

                // Compress uploaded file into ZIP file
                File sourceFile = new File(uploadFolderPath + sourceFileName);
                if (sourceFile.exists()) {
                    ZipEntry ze = new ZipEntry(downloadFileName);
                    zos.putNextEntry(ze);
                    bis = new BufferedInputStream(new FileInputStream(sourceFile));
                    while ((length = bis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    bis.close();
                }
            }

            zos.close();
            os.flush();
            os.close();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ex) {
                    // logger.warn("OutputStream close fail.", ex);
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception ex) {
                    // logger.warn("OutputStream close fail.", ex);
                }
            }
            if (zos != null) {
                try {
                    zos.close();
                } catch (Exception ex) {
                    // logger.warn("OutputStream close fail.", ex);
                }
            }
        }
    }
}
