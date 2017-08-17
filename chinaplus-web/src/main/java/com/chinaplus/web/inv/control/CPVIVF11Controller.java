/**
 * CPVIVF11Controller.java
 * 
 * @screen CPVIVF11
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.util.MasterManager;
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
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.FileUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.inv.entity.CPVIVF11ContEntity;
import com.chinaplus.web.inv.entity.CPVIVF11KanbEntity;
import com.chinaplus.web.inv.entity.CPVIVF11MailInvoiceEntity;
import com.chinaplus.web.inv.entity.CPVIVF11PartEntity;
import com.chinaplus.web.inv.entity.CPVIVF11ResultEntity;
import com.chinaplus.web.inv.entity.CPVIVF11SessionEntity;
import com.chinaplus.web.inv.entity.CPVIVF11SupportEntity;
import com.chinaplus.web.inv.service.CPVIVF11Service;
import com.chinaplus.web.inv.service.CPVIVF13Service;

/**
 * Invoice Upload Controller.
 */
@Controller
public class CPVIVF11Controller extends BaseFileController {

    /** Invoice Upload Session Key */
    public static final String SESSION_KEY_INVOICE_UPLOAD = "SESSION_KEY_INVOICE_UPLOAD";

    /** separator */
    public static final String SEPARATOR = "#;!";

    /** KANB Data Length */
    private static final int KANB_DATA_LENGTH = 156;

    /** CONT Data Length */
    private static final int CONT_DATA_LENGTH = 161;

    /** Mail Invoice Data Count */
    private static final int MAIL_INVOICE_DATA_COUNT = 15;

    /** File Encode */
    private static final String FILE_ENCODE = "UTF-8";

    /** KANB File Name Fixed Text */
    private static final String KANB_NAME_FIXED_TEXT = "KANB";

    /** CONT File Name Fixed Text */
    private static final String CONT_NAME_FIXED_TEXT = "CONT";

    /** Seal No.: Sea */
    private static final String SEAL_NO_SEA = "a";

    /** Seal No.: Air */
    private static final String SEAL_NO_AIR = "o";

    /** Upload Process:First */
    private static final String UPLOAD_PROCESS_FIRST = "0";

    /** Upload Process:Supplier */
    private static final String UPLOAD_PROCESS_SUPPLIER = "1";

    /** Upload Process:Irregular */
    private static final String UPLOAD_PROCESS_IRREGULAR = "2";

    /** Upload Process:Delete */
    private static final String UPLOAD_PROCESS_DELETE = "3";

    /** Invoice Upload Service */
    @Autowired
    private CPVIVF11Service cpvivf11Service;

    /** New Invoice Upload Service */
    @Autowired
    private CPVIVF13Service cpvivf13Service;

    /**
     * Invoice Upload.
     * 
     * @param file upload file
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the Exception
     */
    @RequestMapping(value = "/inv/CPVIVF11/upload",
        method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam(value = "fileData",
        required = true) MultipartFile file, BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        super.setCommonParam(param, request);
        BaseResult<BaseEntity> baseResult = new BaseResult<BaseEntity>();
        if (UPLOAD_PROCESS_FIRST.equals(param.getUploadProcess())) {
            super.uploadFileProcess(file, FileType.ZIP, param, request, response);
        }
        CPVIVF11ResultEntity uploadResult = doUploadProcess(file, param, request);
        baseResult.setData(uploadResult);
        super.setUploadResult(request, response, baseResult);
    }

    /**
     * Process uploaded file.
     * 
     * @param file upload file
     * @param param the parameters
     * @param request HttpServletRequest
     * @return upload result
     * @throws Exception the Exception
     */
    private CPVIVF11ResultEntity doUploadProcess(MultipartFile file, BaseParam param, HttpServletRequest request)
        throws Exception {

        CPVIVF11ResultEntity uploadResult = new CPVIVF11ResultEntity();
        SessionInfoManager session = SessionInfoManager.getContextInstance(request);
        String uploadProcess = param.getUploadProcess();
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        List<CPVIVF11KanbEntity> kanbDataList = null;
        List<CPVIVF11KanbEntity> kanbTotalList = null;
        List<CPVIVF11MailInvoiceEntity> mailInvoiceList = null;
        String tempPath = null;
        String uploadId = null;
        if (UPLOAD_PROCESS_FIRST.equals(uploadProcess)) {
            // Generate the Upload ID for this upload.
            uploadId = StringUtil.genUploadId(param.getLoginId());

            // Extract the upload file to server temporary folder.
            tempPath = ConfigUtil.get(Properties.TEMPORARY_PATH) + uploadId;
            FileUtil.extract(file.getInputStream(), tempPath);
            File tempFolder = new File(tempPath);
            List<File> uploadFiles = FileUtil.getFilesFromPath(tempFolder, null);

            // Get non TTC customer list
            List<String> nonTtcCustomerList = cpvivf11Service.getNonTtcCustomerList();

            // Check the uploaded files
            kanbDataList = new ArrayList<CPVIVF11KanbEntity>();
            mailInvoiceList = new ArrayList<CPVIVF11MailInvoiceEntity>();
            List<CPVIVF11MailInvoiceEntity> originalMailInvoiceList = new ArrayList<CPVIVF11MailInvoiceEntity>();
            List<CPVIVF11ContEntity> contDataList = new ArrayList<CPVIVF11ContEntity>();
            List<String> kanbNameList = new ArrayList<String>();
            List<String> contNameList = new ArrayList<String>();
            List<String> etdContainerList = new ArrayList<String>();
            List<String> expCustomerCodeList = new ArrayList<String>();
            List<String> invCustomerCodeList = new ArrayList<String>();
            List<String> invoiceNoList = new ArrayList<String>();
            Map<String, List<String>> contMap = new HashMap<String, List<String>>();
            Map<String, String> vesselMap = new HashMap<String, String>();
            boolean hasKanbFile = false;
            boolean hasContFile = false;
            boolean hasMailInvoiceFile = false;
            if (uploadFiles != null && uploadFiles.size() > 0) {
                for (File uploadFile : uploadFiles) {
                    // Check KANB file
                    boolean isKanbFile = checkKanbFile(uploadFile, messageLists, kanbDataList, nonTtcCustomerList,
                        kanbNameList, expCustomerCodeList);
                    // Check CONT file
                    boolean isContFile = checkContFile(uploadFile, messageLists, contDataList, contNameList, contMap,
                        vesselMap);
                    // Check Mail Invoice file
                    boolean isMailInvoice = checkMailInvoiceFile(uploadFile, messageLists, originalMailInvoiceList,
                        etdContainerList, invCustomerCodeList, invoiceNoList);

                    // Check whether the file is not a KANBA, CONT or Mail Invoice file
                    if (isKanbFile) {
                        hasKanbFile = true;
                    } else if (isContFile) {
                        hasContFile = true;
                    } else if (isMailInvoice) {
                        hasMailInvoiceFile = true;
                    } else {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_102);
                        message.setMessageArgs(new String[] { uploadFile.getName() });
                        messageLists.add(message);
                    }
                }
            }

            // Check whether these files match each other
            boolean isFileMath = true;
            // Whether there is no KANB file or no CONT file or no Mail Invoice file in upload file
            if (!hasKanbFile || !hasContFile || !hasMailInvoiceFile) {
                isFileMath = false;
            }
            // Whether each KANB file has a corresponding CONT file
            if (isFileMath) {
                for (String kanbName : kanbNameList) {
                    if (!contNameList.contains(kanbName)) {
                        isFileMath = false;
                        break;
                    }
                }
            }
            // Whether each CONT file has a corresponding KANB file
            if (isFileMath) {
                for (String contName : contNameList) {
                    if (!kanbNameList.contains(contName)) {
                        isFileMath = false;
                        break;
                    }
                }
            }
            // Whether all CONT file's ETD&Container No. are included in the Mail Invoice file
            if (isFileMath) {
                for (CPVIVF11ContEntity contData : contDataList) {
                    String etdContainer = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, contData.getEtd())
                            + SEPARATOR + contData.getContainerNo();
                    if (!etdContainerList.contains(etdContainer)) {
                        isFileMath = false;
                        break;
                    }
                }
            }
            if (!isFileMath) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_097);
                messageLists.add(message);
                FileUtil.deleteAllFile(tempFolder);
                throw new BusinessException(messageLists);
            }

            // Check role
            List<String> customerCodes = new ArrayList<String>();
            SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
            UserManager um = UserManager.getLocalInstance(sm);
            List<com.chinaplus.common.bean.BusinessPattern> currentCustoemrs = um.getCurrentBusPattern();
            for (com.chinaplus.common.bean.BusinessPattern currentCustoemr : currentCustoemrs) {
                if (BusinessPattern.AISIN == currentCustoemr.getBusinessPattern()) {
                    customerCodes.add(currentCustoemr.getCustomerCode());
                }
            }
            if (expCustomerCodeList.size() > 0 || invCustomerCodeList.size() > 0) {
                List<String> ttcCustCodes = cpvivf11Service.getTtcCustCode(param.getCurrentOfficeId(),
                    expCustomerCodeList, invCustomerCodeList);
                for (String ttcCustCode : ttcCustCodes) {
                    if (!StringUtil.isEmpty(ttcCustCode) && !customerCodes.contains(ttcCustCode)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_023);
                        message.setMessageArgs(new String[] { ttcCustCode });
                        messageLists.add(message);
                    }
                }
            }

            // Find part information by customer code
            List<Integer> invCustomerIds = new ArrayList<Integer>();
            Map<String, Integer> expCustMap = new HashMap<String, Integer>();
            Map<String, Integer> invCustMap = new HashMap<String, Integer>();
            Map<String, List<CPVIVF11SupportEntity>> partsMap = new HashMap<String, List<CPVIVF11SupportEntity>>();
            if (expCustomerCodeList.size() > 0 || invCustomerCodeList.size() > 0) {
                List<CPVIVF11SupportEntity> allParts = cpvivf11Service.getAllPartsByCustCode(
                    param.getCurrentOfficeId(), expCustomerCodeList, invCustomerCodeList);
                if (allParts != null && allParts.size() > 0) {
                    for (CPVIVF11SupportEntity part : allParts) {
                        addDistinct(invCustomerIds, part.getCustomerId());
                        expCustMap.put(part.getExpCustCode(), part.getCustomerId());
                        if (!StringUtil.isEmpty(part.getInvCustCode())
                                && part.getInvCustCode().contains(StringConst.COMMA)) {
                            String[] invCustCodeArray = part.getInvCustCode().split(StringConst.COMMA);
                            for (String code : invCustCodeArray) {
                                invCustMap.put(code, part.getCustomerId());
                            }
                        } else {
                            invCustMap.put(part.getInvCustCode(), part.getCustomerId());
                        }
                        String mapKey = part.getCustomerId() + SEPARATOR + part.getSuppPartsNo();
                        List<CPVIVF11SupportEntity> partsList = partsMap.get(mapKey);
                        if (partsList == null) {
                            partsList = new ArrayList<CPVIVF11SupportEntity>();
                            partsMap.put(mapKey, partsList);
                        }
                        partsList.add(part);
                    }
                }
            }

            // Find customer order month by customer IDs
            Map<Integer, List<CPVIVF11SupportEntity>> allOrderMonthMap = cpvivf11Service
                .getAllOrderMonth(invCustomerIds);

            // Check KANB part exist
            boolean needPopupSupplementary = false;
            for (CPVIVF11KanbEntity kanbData : kanbDataList) {
                Integer ttcCustId = expCustMap.get(kanbData.getExpCustCode());
                if (ttcCustId != null) {
                    List<CPVIVF11SupportEntity> partList = partsMap.get(ttcCustId + SEPARATOR
                            + kanbData.getSuppPartsNo());
                    if (partList != null && partList.size() > 0) {
                        copyPartInfo(kanbData, partList.get(0));
                        List<CPVIVF11SupportEntity> suppliers = new ArrayList<CPVIVF11SupportEntity>();
                        for (CPVIVF11SupportEntity part : partList) {
                            CPVIVF11SupportEntity supplierInfo = new CPVIVF11SupportEntity();
                            supplierInfo.setSupplierId(part.getSupplierId());
                            supplierInfo.setExpPartsId(part.getExpPartsId());
                            supplierInfo.setExpRegion(part.getExpRegion());
                            supplierInfo.setTtcSuppCode(part.getTtcSuppCode());
                            suppliers.add(supplierInfo);
                        }
                        if (suppliers.size() > 1) {
                            needPopupSupplementary = true;
                            kanbData.setSuppliers(suppliers);
                        }
                    }
                }
                if (kanbData.getPartsId() == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_098);
                    message.setMessageArgs(new String[] { kanbData.getFileName(),
                        StringUtil.toSafeString(kanbData.getLineNum()) });
                    messageLists.add(message);
                } else {
                    int digits = MasterManager.getUomDigits(kanbData.getUomCode());
                    BigDecimal kanbQty = kanbData.getQty();
                    if (kanbQty != null && kanbQty.scale() > digits) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_162);
                        message
                            .setMessageArgs(new String[] { kanbData.getFileName(),
                                StringUtil.toSafeString(kanbData.getLineNum()), "CPVIVF11_Grid_Qty",
                                String.valueOf(digits) });
                        messageLists.add(message);
                    }

                    // check order month
                    String orderMonth = getOrderMonth(allOrderMonthMap, kanbData.getCustomerId(),
                        kanbData.getIssuedDate());
                    if (StringUtil.isEmpty(orderMonth)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_122);
                        message.setMessageArgs(new String[] { kanbData.getFileName(),
                            StringUtil.toSafeString(kanbData.getLineNum()) });
                        messageLists.add(message);
                    } else {
                        kanbData.setOrderMonth(orderMonth);
                    }
                }
            }

            // Check Mail Invoice part exist
            Map<String, Integer> mailInvoiceTotalMap = new HashMap<String, Integer>();
            for (CPVIVF11MailInvoiceEntity mailInvoice : originalMailInvoiceList) {
                Integer ttcCustId = invCustMap.get(mailInvoice.getInvCustCode());
                if (ttcCustId != null) {
                    List<CPVIVF11SupportEntity> partList = partsMap.get(ttcCustId + SEPARATOR
                            + mailInvoice.getSuppPartsNo());
                    if (partList != null && partList.size() > 0) {
                        copyPartInfo(mailInvoice, partList.get(0));
                        List<String> supplierCodes = new ArrayList<String>();
                        mailInvoice.setSupplierCodes(supplierCodes);
                        for (CPVIVF11SupportEntity part : partList) {
                            if (!supplierCodes.contains(part.getTtcSuppCode())) {
                                supplierCodes.add(part.getTtcSuppCode());
                            }
                        }
                    }
                }
                if (mailInvoice.getPartsId() == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_098);
                    message.setMessageArgs(new String[] { mailInvoice.getFileName(),
                        StringUtil.toSafeString(mailInvoice.getLineNum()) });
                    messageLists.add(message);
                } else {
                    int digits = MasterManager.getUomDigits(mailInvoice.getUomCode());
                    BigDecimal mailQty = mailInvoice.getQty();
                    if (mailQty != null && mailQty.scale() > digits) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_162);
                        message.setMessageArgs(new String[] { mailInvoice.getFileName(),
                            StringUtil.toSafeString(mailInvoice.getLineNum()), "CPVIVF11_Grid_Qty",
                            String.valueOf(digits) });
                        messageLists.add(message);
                    }

                    // Mail Invoice Sum
                    String totalMapKey = mailInvoice.getInvoiceNo() + SEPARATOR + mailInvoice.getContainerNo()
                            + SEPARATOR + mailInvoice.getStartPalletNo() + SEPARATOR + mailInvoice.getEndPalletNo()
                            + SEPARATOR + mailInvoice.getSuppPartsNo();
                    Integer totalIndex = mailInvoiceTotalMap.get(totalMapKey);
                    CPVIVF11MailInvoiceEntity mailInvoiceTotal = null;
                    if (totalIndex == null) {
                        mailInvoiceTotal = new CPVIVF11MailInvoiceEntity();
                        BeanUtils.copyProperties(mailInvoice, mailInvoiceTotal);
                        mailInvoiceList.add(mailInvoiceTotal);
                        mailInvoiceTotalMap.put(totalMapKey, mailInvoiceList.size() - 1);
                    } else {
                        mailInvoiceTotal = mailInvoiceList.get(totalIndex);
                        mailInvoiceTotal.setQty(DecimalUtil.add(mailInvoiceTotal.getQty(), mailInvoice.getQty()));
                        mailInvoiceTotal.setExcessQty(mailInvoiceTotal.getQty());
                    }
                }
            }

            // Check whether KANB's parts are more than Mail Invoice's parts
            kanbTotalList = new ArrayList<CPVIVF11KanbEntity>();
            Map<String, Integer> kanbTotalMap = new HashMap<String, Integer>();
            for (CPVIVF11KanbEntity kanbData : kanbDataList) {
                if (kanbData.getPartsId() == null) {
                    continue;
                }
                boolean isKanbDataError = true;
                Integer partsId = kanbData.getPartsId();
                Integer palletNo = kanbData.getPalletNo();
                BigDecimal kanbQty = kanbData.getQty();
                String fileName = kanbData.getFileName();
                String nameKey = fileName.substring(IntDef.INT_ZERO, IntDef.INT_SEVENTEEN);
                String vesselName = vesselMap.get(nameKey);
                kanbData.setVesselName(vesselName);
                for (CPVIVF11MailInvoiceEntity mailInvoice : mailInvoiceList) {
                    if (mailInvoice.getPartsId() == null) {
                        continue;
                    }
                    if (SEAL_NO_AIR.equals(mailInvoice.getSealNo())) {
                        continue;
                    }
                    BigDecimal invoiceQty = mailInvoice.getExcessQty();
                    if (partsId.equals(mailInvoice.getPartsId()) && palletNo >= mailInvoice.getStartPalletNo()
                            && palletNo <= mailInvoice.getEndPalletNo() && kanbQty.compareTo(invoiceQty) <= 0) {
                        List<String> contList = contMap.get(nameKey);
                        String etdContainer = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD,
                            mailInvoice.getEtd())
                                + SEPARATOR + mailInvoice.getContainerNo();
                        if (contList != null && contList.contains(etdContainer)) {
                            isKanbDataError = false;
                            mailInvoice.setExcessQty(DecimalUtil.subtract(invoiceQty, kanbQty));
                            kanbData.setInvoiceNo(mailInvoice.getInvoiceNo());
                            kanbData.setMailInvoice(mailInvoice);
                            break;
                        }
                    }
                }
                if (isKanbDataError) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_099);
                    message.setMessageArgs(new String[] { fileName, kanbData.getOriginalPartNo() });
                    messageLists.add(message);
                } else {
                    // Set data to total list
                    String orderMonth = kanbData.getOrderMonth();
                    if (!StringUtil.isEmpty(orderMonth)) {
                        String totalMapKey = kanbData.getInvoiceNo() + SEPARATOR + orderMonth + SEPARATOR + partsId;
                        Integer totalIndex = kanbTotalMap.get(totalMapKey);
                        CPVIVF11KanbEntity kanbTotal = null;
                        if (totalIndex == null) {
                            kanbTotal = new CPVIVF11KanbEntity();
                            BeanUtils.copyProperties(kanbData, kanbTotal);
                            kanbTotalList.add(kanbTotal);
                            kanbTotalMap.put(totalMapKey, kanbTotalList.size() - 1);
                        } else {
                            kanbTotal = kanbTotalList.get(totalIndex);
                            kanbTotal.setQty(DecimalUtil.add(kanbTotal.getQty(), kanbQty));
                        }
                    }
                }
            }

            // Check whether the invoice duplicate upload
            for (String invoiceNo : invoiceNoList) {
                if (cpvivf13Service.isInvoiceExist(invoiceNo)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_100);
                    message.setMessageArgs(new String[] { invoiceNo });
                    messageLists.add(message);
                }
            }

            if (messageLists.size() != 0) {
                FileUtil.deleteAllFile(tempFolder);
                throw new BusinessException(messageLists);
            } else {
                // Find the KANB parts that a Supplier Part No. corresponding to multiple suppliers
                if (needPopupSupplementary) {
                    CPVIVF11SessionEntity sessionEntity = new CPVIVF11SessionEntity();
                    sessionEntity.setKanbTotalList(kanbTotalList);
                    sessionEntity.setKanbDataList(kanbDataList);
                    sessionEntity.setMailInvoiceList(mailInvoiceList);
                    sessionEntity.setTempFolderPath(tempPath);
                    sessionEntity.setUploadId(uploadId);
                    session.put(SESSION_KEY_INVOICE_UPLOAD, sessionEntity);
                    uploadResult.setUploadResult(IntDef.INT_ONE);
                    return uploadResult;
                }
            }
        } else if (UPLOAD_PROCESS_SUPPLIER.equals(uploadProcess) || UPLOAD_PROCESS_DELETE.equals(uploadProcess)) {
            CPVIVF11SessionEntity sessionEntity = (CPVIVF11SessionEntity) session.get(SESSION_KEY_INVOICE_UPLOAD);
            kanbTotalList = sessionEntity.getKanbTotalList();
            kanbDataList = sessionEntity.getKanbDataList();
            mailInvoiceList = sessionEntity.getMailInvoiceList();
            tempPath = sessionEntity.getTempFolderPath();
            uploadId = sessionEntity.getUploadId();
            session.remove(SESSION_KEY_INVOICE_UPLOAD);
            if (UPLOAD_PROCESS_DELETE.equals(uploadProcess)) {
                // Delete temp file and upload file
                FileUtil.deleteAllFile(new File(tempPath));
                uploadResult.setUploadResult(IntDef.INT_FOUR);
                return uploadResult;
            }
        }
        if (UPLOAD_PROCESS_FIRST.equals(uploadProcess) || UPLOAD_PROCESS_SUPPLIER.equals(uploadProcess)) {
            // Find the KANBAN Plan
            Map<String, String> kanbanPlanMap = new HashMap<String, String>();
            List<String> errorKeyList = new ArrayList<String>();
            Map<String, List<String>> invoiceSupplierMap = new HashMap<String, List<String>>();
            for (CPVIVF11KanbEntity kanbTotalData : kanbTotalList) {
                Integer customerId = kanbTotalData.getCustomerId();
                Integer supplierId = kanbTotalData.getSupplierId();
                String orderMonth = kanbTotalData.getOrderMonth();
                String mapKey = customerId + SEPARATOR + supplierId + SEPARATOR + orderMonth;
                String kanbanPlanNo = kanbanPlanMap.get(mapKey);
                if (StringUtil.isEmpty(kanbanPlanNo)) {
                    if (!errorKeyList.contains(mapKey)) {
                        kanbanPlanNo = cpvivf11Service.getKanbanPlanNo(param.getCurrentOfficeId(), customerId,
                            supplierId, orderMonth);
                    }
                    if (StringUtil.isEmpty(kanbanPlanNo)) {
                        if (!errorKeyList.contains(mapKey)) {
                            errorKeyList.add(mapKey);
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_101);
                            message.setMessageArgs(new String[] {
                                kanbTotalData.getTtcCustCode(),
                                DateTimeUtil.getDisOrderMonth(kanbTotalData.getOrderMonth(),
                                    DateTimeUtil.FORMAT_YEAR_MONTH), kanbTotalData.getTtcSuppCode() });
                            messageLists.add(message);
                        }
                    } else {
                        kanbanPlanMap.put(mapKey, kanbanPlanNo);
                    }
                }
                kanbTotalData.setKanbanPlanNo(kanbanPlanNo);

                // Set supplier code set
                String invoiceNo = kanbTotalData.getInvoiceNo();
                String supplierCode = kanbTotalData.getTtcSuppCode();
                List<String> supplierList = invoiceSupplierMap.get(invoiceNo);
                if (supplierList == null) {
                    supplierList = new ArrayList<String>();
                    invoiceSupplierMap.put(invoiceNo, supplierList);
                }
                if (!supplierList.contains(supplierCode)) {
                    supplierList.add(supplierCode);
                }
            }
            if (messageLists.size() != 0) {
                FileUtil.deleteAllFile(new File(tempPath));
                throw new BusinessException(messageLists);
            }

            // Upload Logic
            cpvivf11Service.doUpload(param, uploadId, kanbDataList, mailInvoiceList, kanbTotalList, invoiceSupplierMap);

            // Save the uploaded file on the server
            File invoiceFolder = new File(ConfigUtil.get(Properties.UPLOAD_PATH_INVOICE));
            if (!invoiceFolder.exists()) {
                invoiceFolder.mkdirs();
            }
            File tempFolder = new File(tempPath);
            FileUtil.compress(new File(invoiceFolder, uploadId + StringConst.DOT + FileType.ZIP.getSuffix()),
                tempFolder);

            // Delete temp file
            FileUtil.deleteAllFile(tempFolder);

            // Check whether pop up Irregular Shipping Schedule screen
            boolean hasIrregular = cpvivf11Service.isIrregularExist(uploadId);
            if (hasIrregular) {
                CPVIVF11SessionEntity sessionEntity = new CPVIVF11SessionEntity();
                sessionEntity.setUploadId(uploadId);
                session.put(SESSION_KEY_INVOICE_UPLOAD, sessionEntity);
                uploadResult.setUploadId(uploadId);
                uploadResult.setUploadResult(IntDef.INT_TWO);
                return uploadResult;
            }
        } else if (UPLOAD_PROCESS_IRREGULAR.equals(uploadProcess)) {
            CPVIVF11SessionEntity sessionEntity = (CPVIVF11SessionEntity) session.get(SESSION_KEY_INVOICE_UPLOAD);
            uploadId = sessionEntity.getUploadId();
        }

        // Check whether pop up supplementary data message
        boolean hasSupplementary = cpvivf11Service.isSupplementaryExist(uploadId);
        if (hasSupplementary) {
            uploadResult.setUploadId(uploadId);
            uploadResult.setUploadResult(IntDef.INT_THREE);
            return uploadResult;
        }

        uploadResult.setUploadResult(IntDef.INT_ZERO);
        return uploadResult;
    }

    /**
     * Check KANB file.
     * 
     * @param file the file
     * @param messageLists the error message list
     * @param kanbDataList the KANB data list
     * @param nonTtcCustomerList non TTC customer list
     * @param kanbNameList the KANB file name list
     * @param expCustomerCodeList the export customer code list
     * @return check result
     * @throws Exception the Exception
     */
    private boolean checkKanbFile(File file, List<BaseMessage> messageLists, List<CPVIVF11KanbEntity> kanbDataList,
        List<String> nonTtcCustomerList, List<String> kanbNameList, List<String> expCustomerCodeList) throws Exception {

        // Check whether a KANB file
        String fileName = file.getName();
        String fileSuffix = FileUtil.getFileSuffix(fileName);
        if (!FileType.TXT.getSuffix().equalsIgnoreCase(fileSuffix) || fileName.length() != IntDef.INT_TWENTY_FIVE) {
            return false;
        }
        String underLine = fileName.substring(IntDef.INT_TWO, IntDef.INT_THREE);
        String dataTime = fileName.substring(IntDef.INT_THREE, IntDef.INT_SEVENTEEN);
        String strVanDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.FORMAT_YYYYMMDD_HHMMSS,
            dataTime);
        String kanbText = fileName.substring(IntDef.INT_SEVENTEEN, IntDef.INT_TWENTY_ONE);
        if (!StringConst.UNDERLINE.equals(underLine) || StringUtil.isEmpty(strVanDate)
                || !KANB_NAME_FIXED_TEXT.equals(kanbText)) {
            return false;
        }
        kanbNameList.add(fileName.substring(IntDef.INT_ZERO, IntDef.INT_SEVENTEEN));
        if (file.length() == 0) {
            return true;
        }

        // Do KANB data input check
        boolean isKanbFile = true;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), FILE_ENCODE));
        String line = null;
        int lineIndex = 0;
        while ((line = br.readLine()) != null) {
            line = StringUtil.toSafeString(line);
            if (line.length() != KANB_DATA_LENGTH) {
                isKanbFile = false;
                break;
            }
            boolean hasError = false;
            CPVIVF11KanbEntity kanbData = new CPVIVF11KanbEntity();

            // File Name
            kanbData.setFileName(fileName);

            // Line Number
            int lineNum = ++lineIndex;
            kanbData.setLineNum(lineNum);

            // Pallet No.
            Integer palletNo = StringUtil.toInteger(line.substring(IntDef.INT_ZERO, IntDef.INT_FIVE));
            if (palletNo == null || palletNo <= 0) {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_103);
                message.setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_PalletNo",
                    "Common_ItemType_PositiveInteger" });
                messageLists.add(message);
            } else {
                kanbData.setPalletNo(palletNo);
            }

            // Original Part No.
            String originalPartNo = StringUtil.trim(line.substring(IntDef.INT_TWENTY_EIGHT, IntDef.INT_FOURTY_EIGTH));
            kanbData.setOriginalPartNo(originalPartNo);

            // Supplier Part No.
            String supplierPartNo = StringUtil.trim(originalPartNo.replaceAll(StringConst.MIDDLE_LINE,
                StringConst.EMPTY));
            kanbData.setSuppPartsNo(supplierPartNo);

            // Qty
            BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(line.substring(IntDef.INT_ONE_HUNDRED_AND_TWENTY_ONE,
                IntDef.INT_ONE_HUNDRED_AND_THIRTY));
            if (qty == null || DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_103);
                message.setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_Qty",
                    "Common_ItemType_PositiveDecimal" });
                messageLists.add(message);
            } else if (!ValidatorUtils.checkMaxDecimal(qty)) {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_104);
                message
                    .setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_Qty", "10", "6" });
                messageLists.add(message);
            } else {
                kanbData.setQty(qty);
            }

            // Issued Date
            Date issuedDate = DateTimeUtil.parseDate(line.substring(IntDef.INT_FIVE, IntDef.INT_THIRTEEN),
                DateTimeUtil.FORMAT_YYYYMMDD);
            if (issuedDate == null) {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_103);
                message.setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_IssuedDate",
                    "Common_ItemType_Date" });
                messageLists.add(message);
            } else {
                kanbData.setIssuedDate(issuedDate);
            }

            // Exp Customer Code
            String expCustomerCode = line.substring(IntDef.INT_TWENTY_ONE, IntDef.INT_TWENTY_FIVE);
            kanbData.setExpCustCode(expCustomerCode);

            // Vanning Date
            Date vanningDate = DateTimeUtil.parseDate(strVanDate, DateTimeUtil.FORMAT_YYYYMMDD);
            kanbData.setVanningDate(vanningDate);

            // Add no error data to KANB data list
            if (!hasError && !nonTtcCustomerList.contains(expCustomerCode)) {
                if (!expCustomerCodeList.contains(expCustomerCode)) {
                    expCustomerCodeList.add(expCustomerCode);
                }
                kanbDataList.add(kanbData);
            }
        }
        br.close();

        return isKanbFile;
    }

    /**
     * Check CONT file.
     * 
     * @param file the file
     * @param messageLists the error message list
     * @param contDataList the CONT data list
     * @param contNameList the CONT file name list
     * @param contMap the CONT map
     * @param vesselMap the vessel map
     * @return check result
     * @throws Exception the Exception
     */
    private boolean checkContFile(File file, List<BaseMessage> messageLists, List<CPVIVF11ContEntity> contDataList,
        List<String> contNameList, Map<String, List<String>> contMap, Map<String, String> vesselMap) throws Exception {

        // Check whether a CONT file
        String fileName = file.getName();
        String fileSuffix = FileUtil.getFileSuffix(fileName);
        if (!FileType.TXT.getSuffix().equalsIgnoreCase(fileSuffix) || fileName.length() != IntDef.INT_TWENTY_FIVE) {
            return false;
        }
        String underLine = fileName.substring(IntDef.INT_TWO, IntDef.INT_THREE);
        String dataTime = fileName.substring(IntDef.INT_THREE, IntDef.INT_SEVENTEEN);
        String strVanDate = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.FORMAT_YYYYMMDD_HHMMSS,
            dataTime);
        String contText = fileName.substring(IntDef.INT_SEVENTEEN, IntDef.INT_TWENTY_ONE);
        if (!StringConst.UNDERLINE.equals(underLine) || StringUtil.isEmpty(strVanDate)
                || !CONT_NAME_FIXED_TEXT.equals(contText)) {
            return false;
        }
        String nameKey = fileName.substring(IntDef.INT_ZERO, IntDef.INT_SEVENTEEN);
        contNameList.add(nameKey);
        if (file.length() == 0) {
            return true;
        }

        // Do CONT data input check
        boolean isContFile = true;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), FILE_ENCODE));
        String line = null;
        int lineIndex = 0;
        while ((line = br.readLine()) != null) {
            line = StringUtil.toSafeString(line);
            if (line.length() != CONT_DATA_LENGTH) {
                isContFile = false;
                break;
            }
            boolean hasError = false;
            CPVIVF11ContEntity contData = new CPVIVF11ContEntity();

            // File Name
            contData.setFileName(fileName);

            // Line Number
            int lineNum = ++lineIndex;
            contData.setLineNum(lineNum);

            // Container No.
            String containerNo = StringUtil.trim(line.substring(IntDef.INT_TWO, IntDef.INT_SEVENTEEN));
            contData.setContainerNo(containerNo);

            // ETD
            Date etd = DateTimeUtil.parseDate(line.substring(IntDef.INT_FIFTY_SEVEN, IntDef.INT_SIXTYFIVE),
                DateTimeUtil.FORMAT_YYYYMMDD);
            if (etd == null) {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_103);
                message.setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_ETD",
                    "Common_ItemType_Date" });
                messageLists.add(message);
            } else {
                contData.setEtd(etd);
            }

            // Vessel Name
            String vesselName = StringUtil.trim(line.substring(IntDef.INT_THIRTY_SEVEN, IntDef.INT_FIFTY_SEVEN));
            contData.setVesselName(vesselName);

            // Vanning Date
            Date vanningDate = DateTimeUtil.parseDate(strVanDate, DateTimeUtil.FORMAT_YYYYMMDD);
            contData.setVanningDate(vanningDate);

            // Add no error data to CONT data list
            if (!hasError) {
                vesselMap.put(nameKey, vesselName);
                contDataList.add(contData);
                List<String> etdContainerList = contMap.get(nameKey);
                if (etdContainerList == null) {
                    etdContainerList = new ArrayList<String>();
                    contMap.put(nameKey, etdContainerList);
                }
                etdContainerList.add(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, etd) + SEPARATOR
                        + containerNo);
            }
        }
        br.close();

        return isContFile;
    }

    /**
     * Check mail invoice file.
     * 
     * @param file the file
     * @param messageLists the error message list
     * @param mailInvoiceList the Mail Invoice list
     * @param etdContainerList the ETD Container list
     * @param invCustomerCodeList the invoice customer code list
     * @param invoiceNoList the invoice No. list
     * @return check result
     * @throws Exception the Exception
     */
    private boolean checkMailInvoiceFile(File file, List<BaseMessage> messageLists,
        List<CPVIVF11MailInvoiceEntity> mailInvoiceList, List<String> etdContainerList,
        List<String> invCustomerCodeList, List<String> invoiceNoList) throws Exception {

        // Check whether a Mail Invoice file
        String fileName = file.getName();
        String fileSuffix = FileUtil.getFileSuffix(fileName);
        if (!FileType.CSV.getSuffix().equalsIgnoreCase(fileSuffix)) {
            return false;
        }
        if (file.length() == 0) {
            return false;
        }

        // Do Mail Invoice input check
        boolean isMailInvoice = true;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), FILE_ENCODE));
        String line = null;
        int lineIndex = 0;
        while ((line = br.readLine()) != null) {
            line = StringUtil.toSafeString(line);
            String[] dataArray = line.split(StringConst.TAB);
            if (dataArray == null || dataArray.length < MAIL_INVOICE_DATA_COUNT) {
                isMailInvoice = false;
                break;
            }
            boolean hasError = false;
            CPVIVF11MailInvoiceEntity mailInvoiceData = new CPVIVF11MailInvoiceEntity();

            // File Name
            mailInvoiceData.setFileName(fileName);

            // Line Number
            int lineNum = ++lineIndex;
            mailInvoiceData.setLineNum(lineNum);

            // Invoice No.
            String invoiceNo = StringUtil.trim(dataArray[IntDef.INT_ONE]);
            mailInvoiceData.setInvoiceNo(invoiceNo);

            // ETD
            Date etd = DateTimeUtil.parseDate(dataArray[IntDef.INT_THREE], DateTimeUtil.FORMAT_DATE_YYYYMMDD_SOLIDUS);
            if (etd == null) {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_103);
                message.setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_ETD",
                    "Common_ItemType_Date" });
                messageLists.add(message);
            } else {
                mailInvoiceData.setEtd(etd);
            }

            // Container No.
            String containerNo = StringUtil.trim(dataArray[IntDef.INT_FOUR]);
            if (containerNo.length() > IntDef.INT_THIRTY) {
                containerNo = containerNo.substring(IntDef.INT_ZERO, IntDef.INT_THIRTY);
            }
            mailInvoiceData.setContainerNo(containerNo);

            // Start Pallet No.
            Integer startPalletNo = StringUtil.toInteger(StringUtil.trim(dataArray[IntDef.INT_FIVE]));
            if (startPalletNo == null || startPalletNo <= 0) {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_103);
                message.setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_StartPalletNo",
                    "Common_ItemType_PositiveInteger" });
                messageLists.add(message);
            } else {
                mailInvoiceData.setStartPalletNo(startPalletNo);
            }

            // End Pallet No.
            Integer endPalletNo = StringUtil.toInteger(StringUtil.trim(dataArray[IntDef.INT_SIX]));
            if (endPalletNo == null || endPalletNo <= 0) {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_103);
                message.setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_EndPalletNo",
                    "Common_ItemType_PositiveInteger" });
                messageLists.add(message);
            } else if (startPalletNo != null && endPalletNo != null && endPalletNo < startPalletNo) {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_105);
                message.setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_EndPalletNo",
                    "CPVIVF11_Grid_StartPalletNo" });
                messageLists.add(message);
            } else {
                mailInvoiceData.setEndPalletNo(endPalletNo);
            }

            // Original Part No.
            String originalPartNo = StringUtil.trim(dataArray[IntDef.INT_SEVEN]);
            mailInvoiceData.setOriginalPartNo(originalPartNo);

            // Supplier Part No.
            String supplierPartNo = originalPartNo.replaceAll(StringConst.BLANK, StringConst.EMPTY);
            mailInvoiceData.setSuppPartsNo(supplierPartNo);

            // Invoice Part No.
            String invoicePartNo = supplierPartNo;
            if (invoicePartNo.length() > IntDef.INT_THIRTY) {
                invoicePartNo = invoicePartNo.substring(IntDef.INT_ZERO, IntDef.INT_THIRTY);
            }
            mailInvoiceData.setInvoicePartNo(invoicePartNo);

            // Qty
            BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(StringUtil.trim(dataArray[IntDef.INT_NINE]));
            if (qty == null || DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_103);
                message.setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_Qty",
                    "Common_ItemType_PositiveDecimal" });
                messageLists.add(message);
            } else if (!ValidatorUtils.checkMaxDecimal(qty)) {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_104);
                message
                    .setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_Qty", "10", "6" });
                messageLists.add(message);
            } else {
                mailInvoiceData.setQty(qty);
                mailInvoiceData.setExcessQty(qty);
            }

            // Price
            String price = StringUtil.trim(dataArray[IntDef.INT_TEN]);
            if (price.length() > IntDef.INT_THIRTY) {
                price = price.substring(IntDef.INT_ZERO, IntDef.INT_THIRTY);
            }
            mailInvoiceData.setPrice(price);

            // Currency
            String currency = StringUtil.trim(dataArray[IntDef.INT_ELEVEN]);
            if (currency.length() > IntDef.INT_FOUR) {
                currency = currency.substring(IntDef.INT_ZERO, IntDef.INT_FOUR);
            }
            mailInvoiceData.setCurrency(currency);

            // Transport Mode
            String transportMode = StringUtil.trim(dataArray[IntDef.INT_TWELVE]);
            if (String.valueOf(IntDef.INT_ONE).equals(transportMode)) {
                mailInvoiceData.setTransportMode(transportMode);
                mailInvoiceData.setSealNo(SEAL_NO_SEA);
            } else if (String.valueOf(IntDef.INT_TWO).equals(transportMode)) {
                mailInvoiceData.setTransportMode(transportMode);
                mailInvoiceData.setSealNo(SEAL_NO_AIR);
            } else {
                hasError = true;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_106);
                message
                    .setMessageArgs(new String[] { fileName, String.valueOf(lineNum), "CPVIVF11_Grid_TransportMode" });
                messageLists.add(message);
            }

            // Invoice Customer Code
            String invCustomerCode = StringUtil.trim(dataArray[IntDef.INT_FOURTEEN]);
            mailInvoiceData.setInvCustCode(invCustomerCode);

            // Add no error data to Mail Invoice data list
            if (!hasError) {
                etdContainerList.add(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, etd) + SEPARATOR
                        + containerNo);
                if (!invCustomerCodeList.contains(invCustomerCode)) {
                    invCustomerCodeList.add(invCustomerCode);
                }
                if (!invoiceNoList.contains(invoiceNo)) {
                    invoiceNoList.add(invoiceNo);
                }
                mailInvoiceList.add(mailInvoiceData);
            }
        }
        br.close();

        return isMailInvoice;
    }

    /**
     * Copy part information.
     * 
     * @param partEntity the part data
     * @param supportEntity the support data
     */
    private void copyPartInfo(CPVIVF11PartEntity partEntity, CPVIVF11SupportEntity supportEntity) {

        partEntity.setPartsId(supportEntity.getPartsId());
        partEntity.setTtcPartsNo(supportEntity.getTtcPartsNo());
        partEntity.setUomCode(supportEntity.getUomCode());
        partEntity.setCustomerId(supportEntity.getCustomerId());
        partEntity.setTtcCustCode(supportEntity.getTtcCustCode());
        partEntity.setExpCustCode(supportEntity.getExpCustCode());
        partEntity.setInvCustCode(supportEntity.getInvCustCode());
        partEntity.setSupplierId(supportEntity.getSupplierId());
        partEntity.setExpPartsId(supportEntity.getExpPartsId());
        partEntity.setTtcSuppCode(supportEntity.getTtcSuppCode());
        partEntity.setSuppPartsNo(supportEntity.getSuppPartsNo());
        partEntity.setImpRegion(supportEntity.getImpRegion());
        partEntity.setExpRegion(supportEntity.getExpRegion());
    }

    /**
     * Add distinct value to list.
     * 
     * @param <T> the object
     * @param list the list
     * @param value the add value
     */
    private <T extends Object> void addDistinct(List<T> list, T value) {

        if (!list.contains(value)) {
            list.add(value);
        }
    }

    /**
     * Get order month by issued date.
     * 
     * @param allOrderMonthMap all order month map
     * @param customerId customer ID
     * @param issuedDate issued date
     * @return order month
     */
    private String getOrderMonth(Map<Integer, List<CPVIVF11SupportEntity>> allOrderMonthMap, Integer customerId,
        Date issuedDate) {

        List<CPVIVF11SupportEntity> orderMonthList = allOrderMonthMap.get(customerId);
        if (orderMonthList != null && orderMonthList.size() > 0) {
            for (CPVIVF11SupportEntity data : orderMonthList) {
                if (data.getFromDate().compareTo(issuedDate) <= 0 && data.getToDate().compareTo(issuedDate) >= 0) {
                    return data.getOrderMonth();
                }
            }
        }

        return null;
    }

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {

        return FileId.CPVIVF11;
    }

}
