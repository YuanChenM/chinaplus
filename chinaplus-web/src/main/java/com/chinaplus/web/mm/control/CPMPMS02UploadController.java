/**
 * Controller of Master upload
 * 
 * @screen CPMPMS02
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.consts.CodeConst.AccessLevel;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.exception.UploadException;
import com.chinaplus.core.util.ExcelUtil;

/**
 * CPMPMS02DownloadController.
 */
@Controller
public class CPMPMS02UploadController extends BaseFileController {

    /** business pattern V-V */
    private static final String BSPTN_VV = "V-V";

    /** business pattern AISIN */
    private static final String BSPTN_AISIN = "AISIN";

    /** V-V_Shipping_Route */
    private static final String VV_SHIPPING_ROUTE = "V-V_Shipping_Route";

    /** TIANJINAISIN_SHIPPING_ROUTE */
    private static final String TIANJINAISIN_SHIPPING_ROUTE = "TIANJINAISIN_Shipping_Route";

    /** TIANJINAISIN_SHIPPING_ROUTE */
    private static final String SHANGHAIAISIN_SHIPPING_ROUTE = "SHANGHAIAISIN_Shipping_Route";

    /** VV_PARTS_MASTER */
    private static final String VV_PARTS_MASTER = "V-V_Parts_Master";

    /** AISIN_PARTS_MASTER */
    private static final String AISIN_PARTS_MASTER = "AISIN_Parts_Master";

    /**
     * cpmsrf11Controller.
     */
    @Autowired
    private CPMSRF11Controller cpmsrf11Controller;

    /**
     * cpmsrf12Controller.
     */
    @Autowired
    private CPMSRF12Controller cpmsrf12Controller;

    /**
     * cpmsrf13Controller.
     */
    @Autowired
    private CPMSRF13Controller cpmsrf13Controller;

    /**
     * cpmpmf11Controller.
     */
    @Autowired
    private CPMPMF11Controller cpmpmf11Controller;

    /**
     * CPMKBF11Controller.
     */
    @Autowired
    private CPMKBF11Controller cpmkbf11Controller;

    /**
     * CPMMAF11Controller.
     */
    @Autowired
    private CPMMAF11Controller cpmmaf11Controller;

    /**
     * CPMSPF11Controller.
     */
    @Autowired
    private CPMSPF11Controller cpmspf11Controller;

    @Override
    protected String getFileId() {
        return FileId.CPMPMS02;
    }

    /**
     * Master Upload
     * 
     * @param file MultipartFile
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @RequestMapping(value = "/mm/CPMPMS02/upload",
        method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam(value = "fileData",
        required = true) MultipartFile file, BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        this.setCommonParam(param, request);
        BaseResult<BaseEntity> baseResult = uploadFileCheck(file, FileType.EXCEL, param, request, response);
        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {
            // open workbook
            Workbook workbook = null;
            try {
                workbook = ExcelUtil.getWorkBook(file.getInputStream());
                // get fileId
                String fileId = getFileIdBySheetName(workbook, request);

                // check static items by configuration
                List<BaseMessage> messages = this.checkStaticItems(fileId, workbook, param);
                if (messages != null && !messages.isEmpty()) {
                    UploadException ex = new UploadException(MessageCodeConst.W1004);
                    ex.setMessageList(messages);
                    throw ex;
                }

                SessionInfoManager context = SessionInfoManager.getContextInstance(request);
                context.put("fileId", fileId);
                // do upload check
                uploadFileCheck(fileId, file, workbook, param, request);

            } catch (BusinessException e) {
                throw e;
            } catch (Exception e) {
                throw new UploadException(MessageCodeConst.E0004, e);
            } finally {
                if (workbook != null) {
                    try {
                        workbook.close();
                    } catch (IOException ex) {}
                }
            }
        }
        this.setUploadResult(request, response, baseResult);
    }

    /**
     * Process uploaded excel.
     * 
     * @param file uploaded file
     * @param workbook uploaded excel
     * @param param the parameters
     * @param <T> the parameter class type
     * @param request the HttpServletRequest
     * @return result message
     */
    @Override
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook,
        T param, HttpServletRequest request) {

        if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())) {
            SessionInfoManager context = SessionInfoManager.getContextInstance(request);
            // get fileId
            String fileId = (String) context.get("fileId");
            // do upload DB
            uploadFileCheck(fileId, file, workbook, param, request);
        }
        return new ArrayList<BaseMessage>();
    }

    /**
     * get FileId By SheetName
     * 
     * @param workbook workbook
     * @param request request
     * @return maps maps
     */
    private String getFileIdBySheetName(Workbook workbook, HttpServletRequest request) {

        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);
        boolean vvflasg = um.getAllVVFlag();
        boolean aisinflasg = um.getAllAisinFlag();
        Integer accessLevel = um.getMaxAccessLevel("CPMPMS02");

        int sheetNum = workbook.getNumberOfSheets();

        if (1 == sheetNum) {
            Sheet sheet = workbook.getSheetAt(0);
            String sheetName = sheet.getSheetName();
            // vv
            if (VV_SHIPPING_ROUTE.equals(sheetName)) {
                if (!vvflasg) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_140);
                    message.setMessageArgs(new String[] { BSPTN_VV, "CPMSRF11_Grid_ShippingRouteData" });
                    throw new BusinessException(message);
                }
                return FileId.CPMSRF11;
            }
            // aisin tianjing
            else if (TIANJINAISIN_SHIPPING_ROUTE.equals(sheetName)) {
                if (!aisinflasg) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_140);
                    message.setMessageArgs(new String[] { BSPTN_AISIN, "CPMSRF11_Grid_ShippingRouteData" });
                    throw new BusinessException(message);
                }
                return FileId.CPMSRF12;
            }
            // aisin shanghai
            else if (SHANGHAIAISIN_SHIPPING_ROUTE.equals(sheetName)) {
                if (!aisinflasg) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_140);
                    message.setMessageArgs(new String[] { BSPTN_AISIN, "CPMSRF11_Grid_ShippingRouteData" });
                    throw new BusinessException(message);
                }
                return FileId.CPMSRF13;
            }
            // Parts Master
            else if (VV_PARTS_MASTER.equals(sheetName) || AISIN_PARTS_MASTER.equals(sheetName)) {
                // if (accessLevel < AccessLevel.MAINTAINERS) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_179);
                // message.setMessageArgs(new String[] { "CPMPMS02_Grid_PartsMasterDownload" });
                // throw new BusinessException(message);
                // }
                return FileId.CPMPMF11;
            }
            // kanban
            else if (CPMKBF01Controller.SHEET_KANBAN.equals(sheetName)) {
                return FileId.CPMKBF11;
            }
            // email alert
            else if (CPMMAF01Controller.SHEET_EMAIL.equals(sheetName)) {
                return FileId.CPMMAF11;
            }
            // reason
            else if (CPMSPF01Controller.SHEET_NAME.equals(sheetName)) {
                // if (accessLevel < AccessLevel.MAINTAINERS) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_179);
                // message.setMessageArgs(new String[] { "CPMPMS02_Grid_SPRevReasonMaster" });
                // throw new BusinessException(message);
                // }
                return FileId.CPMSPF11;
            }

            // none throw
            else {
                throwMessage();
            }
        } else if (IntDef.INT_TWO == sheetNum) {
            String sheetName = workbook.getSheetAt(0).getSheetName();
            String sheetName2 = workbook.getSheetAt(1).getSheetName();
            if (VV_PARTS_MASTER.equals(sheetName) && AISIN_PARTS_MASTER.equals(sheetName2)) {
                // if (accessLevel < AccessLevel.MAINTAINERS) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_179);
                // message.setMessageArgs(new String[] { "CPMPMS02_Grid_PartsMasterDownload" });
                // throw new BusinessException(message);
                // }
                return FileId.CPMPMF11;
            } else if (VV_PARTS_MASTER.equals(sheetName2) && AISIN_PARTS_MASTER.equals(sheetName)) {
                // if (accessLevel < AccessLevel.MAINTAINERS) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_179);
                // message.setMessageArgs(new String[] { "CPMPMS02_Grid_PartsMasterDownload" });
                // throw new BusinessException(message);
                // }
                return FileId.CPMPMF11;
            } else {
                throwMessage();
            }
        } else {
            throwMessage();
        }
        return null;
    }

    /**
     * throw Message
     */
    private void throwMessage() {
        List<BaseMessage> messagesList = new ArrayList<BaseMessage>();
        BaseMessage messageF = new BaseMessage(MessageCodeConst.W1002);
        messageF.setMessageArgs(new String[] { "CPMSRF11_Grid_UploadFile" });
        BaseMessage message = new BaseMessage(MessageCodeConst.W1002_003);
        messagesList.add(messageF);
        messagesList.add(message);
        throw new BusinessException(messagesList);
    }

    /**
     * upload File Check
     * 
     * @param fileId fileId
     * @param file file
     * @param workbook workbook
     * @param param param
     * @param request request
     */
    private void uploadFileCheck(String fileId, MultipartFile file, Workbook workbook, BaseParam param,
        HttpServletRequest request) {
        List<BaseMessage> messages = new ArrayList<BaseMessage>();
        // vv
        if (FileId.CPMSRF11.equals(fileId)) {
            messages = cpmsrf11Controller.doExcelUploadProcess(file, workbook, param, request);
        }
        // aisin tianjing
        else if (FileId.CPMSRF12.equals(fileId)) {
            messages = cpmsrf12Controller.doExcelUploadProcess(file, workbook, param, request);
        }
        // aisin shanghai
        else if (FileId.CPMSRF13.equals(fileId)) {
            messages = cpmsrf13Controller.doExcelUploadProcess(file, workbook, param, request);
        }
        // Parts Master
        else if (FileId.CPMPMF11.equals(fileId)) {
            messages = cpmpmf11Controller.doExcelUploadProcess(file, workbook, param, request);
        }
        // kanban issue
        else if (FileId.CPMKBF11.equals(fileId)) {
            messages = cpmkbf11Controller.doExcelUploadProcess(file, workbook, param, request);
        }
        // email alert
        else if (FileId.CPMMAF11.equals(fileId)) {
            messages = cpmmaf11Controller.doExcelUploadProcess(file, workbook, param, request);
        }
        // reason
        else if (FileId.CPMSPF11.equals(fileId)) {
            messages = cpmspf11Controller.doExcelUploadProcess(file, workbook, param, request);
        }
        if (messages != null && !messages.isEmpty()) {
            UploadException ex = new UploadException(MessageCodeConst.W1004);
            ex.setMessageList(messages);
            throw ex;
        }
    }

}
