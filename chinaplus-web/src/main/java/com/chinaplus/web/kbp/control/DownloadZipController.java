/**
 * KanbanPlanDownloadZipController.java
 * 
 * @screen CPKKPF01&CPKKPF02
 * @author shiyang
 */
package com.chinaplus.web.kbp.control;

import java.io.File;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.service.SupplyChainService;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.kbp.entity.DownloadZipEntity;
import com.chinaplus.web.kbp.service.CPKKPF01Service;
import com.chinaplus.web.kbp.service.CPKKPF02Service;
import com.chinaplus.web.kbp.service.DownloadZipService;

/**
 * Download Latest Kanban Plan File(doc1) & Kanban Plan for Revision History(doc2) Controller.
 */
@Controller
public class DownloadZipController extends BaseFileController {

    /** Download zip file name */
    private static final String DOWNLOAD_ZIP_FILE_NAME = "Kanban Plan_doc1&doc2_{0}.zip";

    /** Download excel file name */
    private static final String DOWNLOAD_EXCEL_FILE1_NAME = "Kanban Plan_{0}_doc1_{1}.xlsx";

    /** Download excel file name */
    private static final String DOWNLOAD_EXCEL_FILE2_NAME = "Kanban Plan_{0}_doc2_{1}.xlsx";

    /** ZIP file content type */
    private static final String ZIP_CONTENT_TYPE = "application/x-zip-compressed;charset=UTF-8";

    /** Download Latest Kanban Plan File(doc1) Service */
    @Autowired
    private CPKKPF01Service cpkkpf01service;

    /** Download Kanban Plan for Revision History(doc2) Service */
    @Autowired
    private CPKKPF02Service cpkkpf02service;

    /** Download Latest Kanban Plan File(doc1) & Kanban Plan for Revision History(doc2) Service */
    @Autowired
    private DownloadZipService downloadzipservice;

    /** Supply Chain Service */
    @Autowired
    private SupplyChainService supplyChainService;

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {
        return null;
    }

    /**
     * Download check for blank invoice file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return check result
     */
    @RequestMapping(value = "/kbp/CPKKPF01_CPKKPF02/downloadcheck")
    @ResponseBody
    public BaseResult<String> downloadCheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {

        return new BaseResult<String>();
    }

    /**
     * Download file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the exception
     */
    @RequestMapping(value = "/kbp/CPKKPF01_CPKKPF02/download")
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {

        OutputStream os = null;
        ZipOutputStream zos = null;
        try {
            super.setCommonParam(param, request);

            response.setContentType(ZIP_CONTENT_TYPE);
            response.setCharacterEncoding(ENCODE);
            response.setHeader(
                "Content-disposition",
                StringUtil.formatMessage("attachment; filename=\"{0}\"",
                    StringUtil.formatMessage(DOWNLOAD_ZIP_FILE_NAME, param.getClientTime())));
            os = response.getOutputStream();
            zos = new ZipOutputStream(os);

            // Generate temporary folder path
            String tempFolderPath = ConfigUtil.get(Properties.TEMPORARY_PATH) + UUID.randomUUID().toString();
            // Create temporary folder
            File tempFolder = new File(tempFolderPath);
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }

            List<String> selected = (List<String>) param.getSelectedDatas();
            for (int i = 0; i < selected.size(); i++) {
                String[] idAndNo = selected.get(i).split(StringConst.NEW_COMMA);
                String kanbanId = idAndNo[0];
                String kanbanPlanNoDisplay = idAndNo[1];
                String fileName1 = StringUtil.formatMessage(DOWNLOAD_EXCEL_FILE1_NAME, kanbanPlanNoDisplay,
                    param.getClientTime());
                String fileName2 = StringUtil.formatMessage(DOWNLOAD_EXCEL_FILE2_NAME, kanbanPlanNoDisplay,
                    param.getClientTime());

                param.setSwapData("kanbanId", kanbanId);
                param.setSwapData("kanbanPlanNoDisplay", kanbanPlanNoDisplay);
                super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), new String[] { fileName1, fileName2 },
                    new String[] { FileId.CPKKPF01, FileId.CPKKPF02 }, param, zos);
            }

            // Delete temporary folder
            tempFolder.delete();
            zos.close();
        } finally {
            if (os != null) {
                try {
                    os.close();
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

    /**
     * Write content to excel.
     * 
     * @param param page parameter
     * @param wbTemplate the template excel
     * @param wbOutput the output excel
     * @param fileId fileId
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput,
        String fileId) {

        if (fileId.equals(FileId.CPKKPF01)) {
            // Download Latest Kanban Plan File(doc1)
            CPKKPF01Controller cpkkpf01 = new CPKKPF01Controller();
            cpkkpf01.outputMainProcess(param, wbTemplate, wbOutput, cpkkpf01service);
        } else if (fileId.equals(FileId.CPKKPF02)) {
            // Download Kanban Plan for Revision History(doc2)
            CPKKPF02Controller cpkkpf02 = new CPKKPF02Controller();
            cpkkpf02.outputMainProcess(param, wbTemplate, wbOutput, cpkkpf02service);
        }
    }

    /**
     * Adjust not in rundown plan.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return Adjust result
     */
    @RequestMapping(value = "/kbp/CPKKPF01_CPKKPF02/adjustNirdPlan")
    @ResponseBody
    public BaseResult<String> adjustNirdPlan(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {

        super.setCommonParam(param, request);

        List<String> selected = (List<String>) param.getSelectedDatas();
        for (int i = 0; i < selected.size(); i++) {
            String[] idAndNo = selected.get(i).split(StringConst.NEW_COMMA);
            String kanbanId = idAndNo[0];

            DownloadZipEntity entity = new DownloadZipEntity();
            entity.setKanbanId(kanbanId);

            // Find not in rundown data.
            List<DownloadZipEntity> nirdList = downloadzipservice.getNirdData(entity);
            // If no not in rundown data, do the next.
            if (nirdList == null || nirdList.size() < 1) {
                continue;
            }

            // Find last plan in this Kanban.
            Timestamp maxEtd = downloadzipservice.getLastShippingPlan(entity);
            if (maxEtd == null) {
                continue;
            }

            // // Find one new shipping route.
            // Timestamp newEtd = null;
            // for (int j = 0; j < nirdList.size(); j++) {
            // entity.setMaxEtd(maxEtd);
            // entity.setShippingRouteCode(nirdList.get(j).getShippingRouteCode());
            //
            // newEtd = downloadzipservice.getNewShippingRoute(entity);
            // if (newEtd != null) {
            // break;
            // }
            // }
            //
            // // If no new shipping route, do the next.
            // if (newEtd == null) {
            // break;
            // }

            entity.setCreatedBy(param.getLoginUserId());
            entity.setUpdatedBy(param.getLoginUserId());

            Integer kanbanShippingId = downloadzipservice.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING);
            Integer kanbanPlanId = downloadzipservice.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
            String shippingUuid = UUID.randomUUID().toString();

            entity.setKanbanShippingId(String.valueOf(kanbanShippingId));
            entity.setKanbanPlanId(String.valueOf(kanbanPlanId));
            entity.setShippingUuid(shippingUuid);

            // Find ETA and Inbound Plan Date.
            List<DownloadZipEntity> list = new ArrayList<DownloadZipEntity>();
            DownloadZipEntity downloadZipEntity = new DownloadZipEntity();
            downloadZipEntity.setChainStartDate(maxEtd);
            downloadZipEntity.setTansportMode(CodeConst.TransportMode.SEA);
            downloadZipEntity.setExpPartsId(nirdList.get(0).getExpPartsId());
            list.add(downloadZipEntity);
            supplyChainService.prepareSupplyChain(list, ChainStep.NEXT_ETD, BusinessPattern.AISIN, false);

            if (list.get(0).getEtd() == null || list.get(0).getEta() == null
                    || list.get(0).getImpPlanInboundDate() == null) {
                continue;
            }

            entity.setEtd(list.get(0).getEtd());
            entity.setEta(list.get(0).getEta());
            entity.setImpPlanInboundDate(list.get(0).getImpPlanInboundDate());
            entity.setRevisonReason(MessageManager.getMessage("Common_Label_SystemAutomaticAdjustment"));

            // Insert Kanban Shipping
            downloadzipservice.insertKanbanShipping(param, entity);

            // Insert Kanban Plan.
            downloadzipservice.insertKanbanPlan(param, entity);

            for (int j = 0; j < nirdList.size(); j++) {
                entity.setPartsId(nirdList.get(j).getPartsId());
                entity.setQty(nirdList.get(j).getNirdQty());

                // Insert Kanban Shipping Parts.
                entity.setKspId(downloadzipservice.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING_PARTS));
                downloadzipservice.insertKanbanShippingParts(param, entity);

                // Insert Kanban Plan Parts.
                entity.setKbPlanPartsId(downloadzipservice
                    .getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS));
                downloadzipservice.insertKanbanPlanParts(param, entity);
            }

            // Delete not in rundown data (TNT_KANBAN_SHIPPING_PARTS).
            downloadzipservice.deleteKanbanShippingParts(entity);

            // Delete not in rundown data (TNT_KANBAN_SHIPPING).
            downloadzipservice.deleteKanbanShipping(entity);
        }

        return new BaseResult<String>();
    }
}
