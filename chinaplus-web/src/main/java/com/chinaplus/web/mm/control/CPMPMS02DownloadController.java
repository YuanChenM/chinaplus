/**
 * Controller of Master Download
 * 
 * @screen CPMPMS02
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst.ShippingRouteType;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.service.CPMSRF01Service;

/**
 * CPMPMS02DownloadController.
 */
@Controller
public class CPMPMS02DownloadController extends BaseFileController {

    /** ZIP file content type */
    private static final String ZIP_CONTENT_TYPE = "application/x-zip-compressed;charset=UTF-8";

    /** Download zip file name */
    private static final String DOWNLOAD_ZIP_FILE_NAME = "MasterDataDownload_{0}.zip";

    /** PARTSMASTERDOWNLOADFILE_XLSX */
    private static final String PARTSMASTERDOWNLOADFILE_XLSX = "PartsMasterDownloadFile_{0}.xlsx";

    /** VVSHIPPINGROUTEDOWNLOADFILE_XLSX */
    private static final String VVSHIPPINGROUTEDOWNLOADFILE_XLSX = "VVShippingRouteDownloadFile_{0}.xlsx";

    /** TIANJINAISINSHIPPINGROUTEDOWNLOADFILE_XLSX */
    private static final String TIANJINAISHINSIPPINGROUTEDOWNLOADFILE_XLSX = "TIANJINAISINShippingRouteDownloadFile_{0}.xlsx";

    /** SHANGHAIAISINSHIPPINGROUTEDOWNLOADFILE_XLSX */
    private static final String SHANGHAIAISINSHIPPINGROUTEDOWNLOADFILE_XLSX = "SHANGHAIAISINShippingRouteDownloadFile_{0}.xlsx";

    /**
     * cpmsrf01Service.
     */
    @Autowired
    private CPMSRF01Service cpmsrf01Service;

    /**
     * cpmpmf01Controller.
     */
    @Autowired
    private CPMPMF01Controller cpmpmf01Controller;

    /**
     * cpmsrf01Controller.
     */
    @Autowired
    private CPMSRF01Controller cpmsrf01Controller;

    /**
     * cpmsrf02Controller.
     */
    @Autowired
    private CPMSRF02Controller cpmsrf02Controller;

    /**
     * cpmsrf03Controller.
     */
    @Autowired
    private CPMSRF03Controller cpmsrf03Controller;

    /**
     * CPMCMF01Controller.
     */
    @Autowired
    private CPMCMF01Controller cpmcmf01Controller;

    /**
     * CPMKBF01Controller.
     */
    @Autowired
    private CPMKBF01Controller cpmkbf01Controller;

    /**
     * CPMMAF01Controller.
     */
    @Autowired
    private CPMMAF01Controller cpmmaf01Controller;

    /**
     * CPMSPF01Controller.
     */
    @Autowired
    private CPMSPF01Controller cpmspf01Controller;

    @Override
    protected String getFileId() {
        return FileId.CPMPMS02;
    }

    /**
     * Download Master check
     * 
     * @param param param
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     * @throws Exception e
     */
    @RequestMapping(value = "/mm/CPMPMS02/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        return new BaseResult<BaseEntity>();
    }

    /**
     * Download Master
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception e
     */
    @RequestMapping(value = "/mm/CPMPMS02/download",
        method = RequestMethod.POST)
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {

        BaseParam param = this.convertJsonDataForForm(BaseParam.class);
        // set common parameters by session
        this.setCommonParam(param, request);

        String clientTime = param.getClientTime();
        OutputStream os = null;
        ZipOutputStream zos = null;
        try {
            super.setCommonParam(param, request);

            response.setContentType(ZIP_CONTENT_TYPE);
            response.setCharacterEncoding(ENCODE);
            response.setHeader(
                "Content-disposition",
                StringUtil.formatMessage("attachment; filename=\"{0}\"",
                    StringUtil.formatMessage(DOWNLOAD_ZIP_FILE_NAME, clientTime)));
            os = response.getOutputStream();
            zos = new ZipOutputStream(os);

            // Generate temporary folder path
            String tempFolderPath = ConfigUtil.get(Properties.TEMPORARY_PATH) + UUID.randomUUID().toString();
            // Create temporary folder
            File tempFolder = new File(tempFolderPath);
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }

            Map<String, Object> fileMap = getFileNameId(param, clientTime);
            String[] fileNames = (String[]) fileMap.get("fileNames");
            String[] fileIds = (String[]) fileMap.get("fileIds");

            super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), fileNames, fileIds, param, zos);

            // Delete temporary folder
            tempFolder.delete();
            zos.close();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {

                }
            }
            if (zos != null) {
                try {
                    zos.close();
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * get fileName and fileId
     * 
     * @param param param
     * @param clientTime clientTime
     * @return maps maps
     */
    private Map<String, Object> getFileNameId(BaseParam param, String clientTime) {
        Map<String, Object> maps = new HashMap<String, Object>();

        boolean partsMasterDownUp = (boolean) param.getSwapData().get("partsMasterDownUp");
        boolean blankFormatDownP = (boolean) param.getSwapData().get("blankFormatDownP");
        boolean onlyDownNoRegParts = (boolean) param.getSwapData().get("onlyDownNoRegParts");

        boolean shippingRouteMastDownUp = (boolean) param.getSwapData().get("shippingRouteMastDownUp");
        boolean blankFormatDownSR = (boolean) param.getSwapData().get("blankFormatDownSR");

        boolean kanbIssuedPDateDownUp = (boolean) param.getSwapData().get("kanbIssuedPDateDownUp");
        boolean blankFormatDownK = (boolean) param.getSwapData().get("blankFormatDownK");

        boolean calendarDownload = (boolean) param.getSwapData().get("calendarDownload");

        boolean emailAlertMasterDownUp = (boolean) param.getSwapData().get("emailAlertMasterDownUp");
        boolean blankFormatDownE = (boolean) param.getSwapData().get("blankFormatDownE");

        boolean sprevReasonDownUp = (boolean) param.getSwapData().get("sprevReasonDownUp");
        boolean blankFormatDownSP = (boolean) param.getSwapData().get("blankFormatDownSP");

        List<String> fileList = new ArrayList<String>();
        List<String> fileIdList = new ArrayList<String>();

        // do Parts Master Download
        if (partsMasterDownUp || blankFormatDownP || onlyDownNoRegParts) {
            String fileName = StringUtil.formatMessage(PARTSMASTERDOWNLOADFILE_XLSX, clientTime);
            fileList.add(fileName);
            fileIdList.add(FileId.CPMPMF01);
        }
        // do Shipping Route Download
        if (shippingRouteMastDownUp || blankFormatDownSR) {
            String shipRouteType = (String) param.getSwapData().get("shipRouteType");
            String[] shipRouteTypes = shipRouteType.split(StringConst.COMMA);
            for (String str : shipRouteTypes) {
                // shipRouteType is V-V
                if ((ShippingRouteType.VV + "").equals(str)) {
                    String fileName = StringUtil.formatMessage(VVSHIPPINGROUTEDOWNLOADFILE_XLSX, clientTime);
                    fileList.add(fileName);
                    fileIdList.add(FileId.CPMSRF01);
                }
                // shipRouteType is tianjing Aisin
                else if ((ShippingRouteType.AISIN_TTTJ + "").equals(str)) {
                    String fileName = StringUtil.formatMessage(TIANJINAISHINSIPPINGROUTEDOWNLOADFILE_XLSX, clientTime);
                    fileList.add(fileName);
                    fileIdList.add(FileId.CPMSRF02);
                }
                // shipRouteType is shanghai Aisin
                else if ((ShippingRouteType.AISIN_TTSH + "").equals(str)) {
                    String fileName = StringUtil.formatMessage(SHANGHAIAISINSHIPPINGROUTEDOWNLOADFILE_XLSX, clientTime);
                    fileList.add(fileName);
                    fileIdList.add(FileId.CPMSRF03);
                }
            }
        }
        // kanban download
        if (kanbIssuedPDateDownUp || blankFormatDownK) {
            String fileName = StringUtil.formatMessage(CPMKBF01Controller.DOWNLOAD_NAME, clientTime);
            fileList.add(fileName);
            fileIdList.add(FileId.CPMKBF01);
        }
        // calendar
        if (calendarDownload) {
            String fileName = "CP_WorkingCalendar_" + param.getClientTime() + ".xlsx";
            fileList.add(fileName);
            fileIdList.add(FileId.CPMCMF01);
        }

        // emailAlertMaster
        if (emailAlertMasterDownUp || blankFormatDownE) {
            String fileName = StringUtil.formatMessage(CPMMAF01Controller.XLSX, clientTime);
            fileList.add(fileName);
            fileIdList.add(FileId.CPMMAF01);
        }

        // ReasonDown
        if (sprevReasonDownUp || blankFormatDownSP) {
            String fileName = StringUtil.formatMessage(CPMSPF01Controller.XLSX, clientTime);
            fileList.add(fileName);
            fileIdList.add(FileId.CPMSPF01);
        }

        // list<String> to String[]
        String[] fileNames = fileList.toArray(new String[fileList.size()]);
        String[] fileIds = fileIdList.toArray(new String[fileIdList.size()]);
        maps.put("fileNames", fileNames);
        maps.put("fileIds", fileIds);
        return maps;
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
        if (fileId.equals(FileId.CPMPMF01)) {
            cpmpmf01Controller.writeContentToExcel(param, wbTemplate, wbOutput);
        } else if (fileId.equals(FileId.CPMSRF01)) {
            cpmsrf01Controller.writeContentToExcel(param, wbTemplate, wbOutput);
        } else if (fileId.equals(FileId.CPMSRF02)) {
            cpmsrf02Controller.writeContentToExcel(param, wbTemplate, wbOutput);
        } else if (fileId.equals(FileId.CPMSRF03)) {
            cpmsrf03Controller.writeContentToExcel(param, wbTemplate, wbOutput);
        } else if (fileId.equals(FileId.CPMCMF01)) {
            cpmcmf01Controller.writeDynamicTemplate(param, wbTemplate);
        } else if (fileId.equals(FileId.CPMKBF01)) {
            cpmkbf01Controller.writeContentToExcel(param, wbTemplate, wbOutput);
        } else if (fileId.equals(FileId.CPMMAF01)) {
            cpmmaf01Controller.writeDynamicTemplate(param, wbTemplate);
        } else if (fileId.equals(FileId.CPMSPF01)) {
            cpmspf01Controller.writeDynamicTemplate(param, wbTemplate);
        }

    }
}
