/**
 * CPVIVS01Controller.java
 * 
 * @screen CPVIVS01
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.consts.CodeConst.InvoiceType;
import com.chinaplus.common.service.SupplyChainService;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inv.entity.CPVIVS01Entity;
import com.chinaplus.web.inv.entity.CPVIVS01IrregularEntity;
import com.chinaplus.web.inv.service.CPVIVS01Service;
import com.chinaplus.web.inv.service.CPVIVS04Service;

/**
 * Invoice Screen Controller.
 */
@Controller
public class CPVIVS01Controller extends BaseController {

    /** Invoice Resource Id */
    private static final String INVOICE_RESOURCE_ID = "CPVIVS01";

    /** File encode */
    private static final String FILE_ENCODE = "UTF-8";

    /** Download file name */
    private static final String DOWNLOAD_FILE_NAME = "Invoices_{0}.zip";

    /** ZIP file content type */
    private static final String ZIP_CONTENT_TYPE = "application/x-zip-compressed;charset=UTF-8";

    /** Buffer size */
    private static final int BUFFER_SIZE = 1024;

    /** Invoice Screen Service */
    @Autowired
    private CPVIVS01Service cpvivs01Service;

    /** Irregular Shipping Schedule Service */
    @Autowired
    private CPVIVS04Service cpvivs04Service;

    /** Supply Chain Service */
    @Autowired
    private SupplyChainService supplyChainService;

    /**
     * Load invoice list.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @return invoice list
     */
    @RequestMapping(value = "/inv/CPVIVS01/loadInvoiceList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPVIVS01Entity> getInvoiceList(@RequestBody PageParam param, HttpServletRequest request) {

        super.setCommonParam(param, request);
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);
        Integer accessLevel = um.getAccessLevel(INVOICE_RESOURCE_ID);
        PageResult<CPVIVS01Entity> result = cpvivs01Service.getInvoiceList(param, accessLevel);
        return result;
    }

    /**
     * Download check for invoice file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return check result
     */
    @RequestMapping(value = "/inv/CPVIVS01/downloadcheck")
    @ResponseBody
    public BaseResult<String> downloadCheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {

        if (cpvivs01Service.hasDraftInvoice(param)) {
            throw new BusinessException(MessageCodeConst.W1028);
        }

        return new BaseResult<String>();
    }

    /**
     * Download invoice file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the exception
     */
    @RequestMapping(value = "/inv/CPVIVS01/download")
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {

        OutputStream os = null;
        ZipOutputStream zos = null;
        BufferedInputStream bis = null;
        try {
            // Create the download ZIP file
            String fileName = StringUtil.formatMessage(DOWNLOAD_FILE_NAME, param.getClientTime());
            response.setContentType(ZIP_CONTENT_TYPE);
            response.setCharacterEncoding(FILE_ENCODE);
            response.setHeader("Content-disposition",
                StringUtil.formatMessage("attachment; filename=\"{0}\"", fileName));
            os = response.getOutputStream();
            zos = new ZipOutputStream(os);
            byte[] buf = new byte[BUFFER_SIZE];
            int len = 0;

            // Find all selected invoice files on the server
            List<String> uploadIds = param.getSelectedDatas();
            for (String uploadId : uploadIds) {
                // Check file exists
                File existFile = new File(ConfigUtil.get(Properties.UPLOAD_PATH_INVOICE), uploadId + StringConst.DOT
                        + FileType.ZIP.getSuffix());
                if (existFile.exists()) {
                    // Compress the invoice file into ZIP file
                    ZipEntry ze = new ZipEntry(existFile.getName());
                    zos.putNextEntry(ze);
                    bis = new BufferedInputStream(new FileInputStream(existFile));
                    while ((len = bis.read(buf)) > 0) {
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                    bis.close();
                }
            }

            // Download the ZIP file
            zos.close();
            os.flush();
            os.close();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception ex) {}
            }
            if (zos != null) {
                try {
                    zos.close();
                } catch (Exception ex) {}
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ex) {}
            }
        }
    }

    /**
     * Cancel confirm.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return confirm message
     */
    @RequestMapping(value = "/inv/CPVIVS01/cancelConfirm")
    @ResponseBody
    public BaseResult<String> cancelConfirm(@RequestBody ObjectParam<CPVIVS01Entity> param, HttpServletRequest request,
        HttpServletResponse response) {

        List<String> invoiceNos = new ArrayList<String>();
        List<String> uploadIds = new ArrayList<String>();
        List<CPVIVS01Entity> selectDatas = param.getDatas();
        for (CPVIVS01Entity selectData : selectDatas) {
            if (selectData.getBusinessPattern() == BusinessPattern.V_V) {
                invoiceNos.add(selectData.getInvoiceNo());
            } else if (!uploadIds.contains(selectData.getUploadId())) {
                uploadIds.add(selectData.getUploadId());
            }
        }
        if (uploadIds.size() > 0) {
            List<String> aisinInvoiceNos = cpvivs01Service.getInvoiceNoByUploadId(uploadIds);
            invoiceNos.addAll(aisinInvoiceNos);
        }
        Collections.sort(invoiceNos);
        StringBuffer invoiceNoSet = new StringBuffer();
        for (String invoiceNo : invoiceNos) {
            invoiceNoSet.append(StringConst.COMMA).append(invoiceNo);
        }
        BaseMessage message = new BaseMessage(MessageCodeConst.C1014);
        message
            .setMessageArgs(new String[] { invoiceNoSet.length() > 1 ? invoiceNoSet.substring(1) : StringConst.EMPTY });
        throw new BusinessException(message);
    }

    /**
     * Cancel process.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return cancel result
     */
    @RequestMapping(value = "/inv/CPVIVS01/cancel")
    @ResponseBody
    public BaseResult<String> cancel(@RequestBody ObjectParam<CPVIVS01Entity> param, HttpServletRequest request,
        HttpServletResponse response) {

        // Check interface invoice
        super.setCommonParam(param, request);
        boolean hasInterface = false;
        List<String> summaryIds = new ArrayList<String>();
        List<String> uploadIds = new ArrayList<String>();
        List<CPVIVS01Entity> selectDatas = param.getDatas();
        for (CPVIVS01Entity selectData : selectDatas) {
            if (selectData.getInvoiceType() == InvoiceType.INTERFACE) {
                hasInterface = true;
            } else if (selectData.getBusinessPattern() == BusinessPattern.V_V) {
                summaryIds.add(StringUtil.toSafeString(selectData.getInvoiceSummaryId()));
            } else if (!uploadIds.contains(selectData.getUploadId())) {
                uploadIds.add(selectData.getUploadId());
            }
        }
        if (hasInterface) {
            throw new BusinessException(MessageCodeConst.W1030);
        }
        if (uploadIds.size() > 0) {
            List<String> aisinSummaryIds = cpvivs01Service.getSummaryIdByUploadId(uploadIds);
            summaryIds.addAll(aisinSummaryIds);
        }

        if (summaryIds.size() > 0) {
            // Check inbound
            if (cpvivs01Service.checkInbound(summaryIds)) {
                throw new BusinessException(MessageCodeConst.W1031);
            }

            // Check actual CC
            if (cpvivs01Service.checkActualCC(summaryIds)) {
                throw new BusinessException(MessageCodeConst.W1043);
            }

            // Check already cancelled
            if (cpvivs01Service.checkCancelled(summaryIds)) {
                throw new BusinessException(MessageCodeConst.W1013);
            }

            // Do cancel
            cpvivs01Service.doCancelInvoice(param.getLoginUserId(), summaryIds);
        }

        return new BaseResult<String>();
    }

    /**
     * Approve process.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return approve result
     */
    @RequestMapping(value = "/inv/CPVIVS01/approve")
    @ResponseBody
    public BaseResult<String> approve(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {

        // Check interface invoice
        super.setCommonParam(param, request);
        if (cpvivs01Service.checkApproveStatus(param)) {
            throw new BusinessException(MessageCodeConst.W1032);
        }

        // Do approve
        cpvivs01Service.doApproveInvoice(param);

        return new BaseResult<String>();
    }

    /**
     * Confirm irregular shipping.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return confirm result
     */
    @RequestMapping(value = "/inv/CPVIVS01/confirmIrregular")
    @ResponseBody
    public BaseResult<Integer> confirmIrregular(@RequestBody ObjectParam<String> param, HttpServletRequest request,
        HttpServletResponse response) {

        // Select Irregular Shipping Schedule
        BaseResult<Integer> result = new BaseResult<Integer>();
        super.setCommonParam(param, request);
        String uploadId = param.getData();
        boolean needPopup = false;
        boolean needUpdate = false;
        List<CPVIVS01IrregularEntity> irregularParts = cpvivs01Service.getIrregularParts(uploadId);
        if (irregularParts.size() > 0) {
            // Find the ETA and Plan Inbound Date
            supplyChainService.prepareSupplyChain(irregularParts, ChainStep.INVOICE, BusinessPattern.AISIN, false);

            // Check ETA and Plan Inbound Date
            Map<String, Date[]> dateMap = new HashMap<String, Date[]>();
            for (CPVIVS01IrregularEntity irregularPart : irregularParts) {
                Date eta = irregularPart.getEta();
                Date inboundDate = irregularPart.getImpPlanInboundDate();
                if (eta == null || inboundDate == null) {
                    needPopup = true;
                    continue;
                }
                needUpdate = true;
                String mapKey = irregularPart.getVesselName() + StringConst.UNDERLINE
                        + DateTimeUtil.getDisDate(irregularPart.getVesselEtd());
                Date[] dateArray = new Date[IntDef.INT_TWO];
                dateArray[0] = eta;
                dateArray[1] = inboundDate;
                dateMap.put(mapKey, dateArray);
            }

            // Update irregular shipping schedule
            if (dateMap.size() > 0) {
                cpvivs04Service.doIrregularShippingUpdate(param.getLoginUserId(), uploadId, dateMap);
            }
        }
        if (needPopup) {
            if (needUpdate) {
                result.setData(IntDef.INT_TWO);
            } else {
                result.setData(IntDef.INT_ONE);
            }
        } else {
            result.setData(IntDef.INT_ZERO);
        }

        return result;
    }

}
