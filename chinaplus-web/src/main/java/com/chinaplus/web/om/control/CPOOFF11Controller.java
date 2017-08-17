/**
 * CPOCSF11Controller.java
 * 
 * @screen CPOOFF11
 * @author shi_yuxi
 */
package com.chinaplus.web.om.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.bean.BusinessPattern;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.consts.CodeConst.PartsStatus;
import com.chinaplus.common.consts.CodeConst.WorkingDay;
import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.ExcelUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.om.entity.CPOOFF11Entity;
import com.chinaplus.web.om.entity.CalculateDrEntity;
import com.chinaplus.web.om.entity.MainRouteEntity;
import com.chinaplus.web.om.service.CPOOFF11Service;

/**
 * Upload Order Forecast File
 */
@Controller
public class CPOOFF11Controller extends BaseFileController {

    /** BLANK_LINE_NUM */
    private static final int BLANK_LINE_NUM = 10;

    /** DETAIL_START_LINE */
    private static final int DETAIL_START_LINE = 8;

    /** TOTAL_COL_NUM */
    private static final int DETAIL_START_COL_NO = 1;

    /** Download file name */
    private static final int TOTAL_COL_NUM = 30;

    /** POPUP */
    private static final String POPUP = "popup";

    /** Download file name */
    private static final String DOWNLOAD_FILE_NAME = "OrderForecast_{0}.zip";

    /** CSV file encode */
    private static final String CSV_ENCODE = "UTF-8";

    /** ZIP file content type */
    private static final String ZIP_CONTENT_TYPE = "application/x-zip-compressed;charset=UTF-8";

    /** Buffer size */
    private static final int BUFFER_SIZE = 1024;

    @Autowired
    private CPOOFF11Service service;

    @Override
    protected String getFileId() {
        return FileId.CPOOFF11;
    }

    /**
     * Customer Stock Upload
     * 
     * @param file MultipartFile
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @RequestMapping(value = "/om/CPOOFF11/upload",
        method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam(value = "fileData",
        required = true) MultipartFile file, BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        this.setCommonParam(param, request);
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        BaseResult<CPOOFF11Entity> result = new PageResult<CPOOFF11Entity>();
        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {
            String orderMonth = (String) param.getSwapData().get("orderMonth");
            if (StringUtil.isEmpty(orderMonth)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
                message.setMessageArgs(new String[] { "CPOOFS02_Label_OrderMonth" });
                messageLists.add(message);
            }
            if (0 == file.getSize()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1001_005);
                message.setMessageArgs(new String[] { "CPOOFS02_Label_FileName" });
                messageLists.add(message);
            }
            String remark = (String) param.getSwapData().get("remark");
            if (!ValidatorUtils.maxLengthValidator(remark, IntDef.INT_TWO_HUNDRED_FIFTY_FIVE)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1001_006);
                message.setMessageArgs(new String[] { "CPOOFS02_Label_UploadRemark",
                    String.valueOf(IntDef.INT_TWO_HUNDRED_FIFTY_FIVE) });
                messageLists.add(message);
            }
            
            if (messageLists.size() > 0) {
                throw new BusinessException(messageLists);
            }
            uploadFileProcess(file, FileType.EXCEL, param, request, response);
        }

        CPOOFF11Entity entity = doUploadProcess(file, param, request);
        result.setData(entity);
        this.setUploadResult(request, response, result);
    }

    /**
     * Process uploaded excel.
     * 
     * @param file file
     * @param param the parameters
     * @param <T> the parameter class type
     * @param request the HttpServletRequest
     * @return result message
     * @throws Exception Exception
     */
    @SuppressWarnings("unchecked")
    protected <T extends BaseParam> CPOOFF11Entity doUploadProcess(MultipartFile file, T param,
        HttpServletRequest request) throws Exception {
        CPOOFF11Entity resultEntity = new CPOOFF11Entity();

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        List<MainRouteEntity> listToPageRoute = new ArrayList<MainRouteEntity>();
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        List<CPOOFF11Entity> listData = new ArrayList<CPOOFF11Entity>();
        // get and check data from excel file
        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {
            Workbook workbook = ExcelUtil.getWorkBook(file.getInputStream());
            // ---------------------------------------------------------------
            // save original File
            OutputStream outputStream = null;
            String path = ConfigUtil.get(Properties.UPLOAD_PATH_PFC);
            // path = "E:/common/cfc";
            File fileSave = new File(path);
            if (!fileSave.exists()) {
                fileSave.mkdirs();
                fileSave = new File(path, "temp.xlsx");
            } else {
                fileSave = new File(path, "temp.xlsx");
            }
            outputStream = new FileOutputStream(fileSave);
            workbook.write(outputStream);
            outputStream.close();
            // workbook.close();
            // -----------------------------------------------------------------------------
            messageLists = checkFile(file, param, request, listData);
            if (messageLists.size() != 0) {
                throw new BusinessException(messageLists);
            }
        }
        // upload logic
        if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())
                || UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())
                || POPUP.equals(param.getUploadProcess())) {
            // get file content from session.
            if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())
                    || UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {

                String mapKey = param.getSessionKey();
                listData = (List<CPOOFF11Entity>) context.get(mapKey);
                if (listData != null && listData.size() != 0) {
                    for (CPOOFF11Entity entity : listData) {
                        if (entity.getRouteList().size() > IntDef.INT_ONE) {
                            listToPageRoute.addAll(entity.getRouteList());
                        }
                    }
                }
                if (listToPageRoute.size() == 0) {
                    param.setUploadProcess(POPUP);
                } else {
                    context.put(param.getSessionKey() + "_popup", listToPageRoute);
                    resultEntity.setUploadFlag(String.valueOf(IntDef.INT_ONE));
                    return resultEntity;
                }
            }
            if (POPUP.equals(param.getUploadProcess())) {
                String mapKey = param.getSessionKey();
                listData = (List<CPOOFF11Entity>) context.get(mapKey);
                BaseParam needParam = (BaseParam) param;
                needParam.getSwapData().put("orderMonth", context.get(mapKey + "_orderMonth"));
                needParam.getSwapData().put("remark", context.get(mapKey + "_remark"));
                context.remove(mapKey);
                context.remove(mapKey + "_orderMonth");
                context.remove(mapKey + "_remark");
                String getMainRoute = (String) param.getSwapData().get("mainRoute");
                int routeListCount = 0;
                int routeSelect = 0;
                for (CPOOFF11Entity entity : listData) {
                    if (entity.getRouteList().size() != IntDef.INT_ONE) {
                        routeListCount++;
                        if (!StringUtil.isEmpty(getMainRoute)) {
                            String[] mainRouteSelect = getMainRoute.split(StringConst.COMMA);
                            if (mainRouteSelect != null && mainRouteSelect.length != 0) {
                                for (MainRouteEntity route : entity.getRouteList()) {
                                    for (String s : mainRouteSelect) {
                                        if (route.getExpPartsId().equals(s)) {
                                            routeSelect++;
                                            entity.setRoute(route);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (routeListCount != 0 && routeListCount != routeSelect) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_023);
                    messageLists.add(message);
                    throw new BusinessException(message);
                }
                service.doSaveData(listData, needParam);

            }
        } else if (UploadConst.UPLOAD_PROCESS_UNCONFIRMED.equals(param.getUploadProcess())) {
            String mapKey = param.getSessionKey();
            context.remove(mapKey);
            context.remove(mapKey + "_orderMonth");
            context.remove(mapKey + "_remark");
        }
        return resultEntity;
    }

    /**
     * check file
     * 
     * @param file file
     * @param param param
     * @param request request
     * @param listData listData
     * @return List<BaseMessage>
     * @throws IOException IOException
     */
    private List<BaseMessage> checkFile(MultipartFile file, BaseParam param, HttpServletRequest request,
        List<CPOOFF11Entity> listData) throws IOException {
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        List<BaseMessage> confirm = new ArrayList<BaseMessage>();
        Workbook workbook = ExcelUtil.getWorkBook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(IntDef.INT_ZERO);
        int maxRowNum = sheet.getLastRowNum() + 1;
        int noDataRowcnt = 0;
        List<String> partsNo1021 = new ArrayList<String>();
        List<String> partsNo1022 = new ArrayList<String>();
        List<String> repeat = new ArrayList<String>();
        List<String> notExistCalendar = new ArrayList<String>();
        List<String> shippingMsterData = new ArrayList<String>();
        List<CPOOFF11Entity> errorData = new ArrayList<CPOOFF11Entity>();
        String orderMonth = (String) param.getSwapData().get("orderMonth");
        Date orderMonthDate = DateTimeUtil.parseDate(orderMonth);
        orderMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, orderMonthDate);
        String remark = (String) param.getSwapData().get("remark");
        for (int i = DETAIL_START_LINE; i <= maxRowNum; i++) {
            List<BigDecimal> qtys = new ArrayList<BigDecimal>();
            CPOOFF11Entity fileEntity = new CPOOFF11Entity();
            int count = IntDef.INT_TWO;
            if (ValidatorUtils.isBlankRow(sheet, i, DETAIL_START_COL_NO, TOTAL_COL_NUM)) {
                noDataRowcnt++;
                if (noDataRowcnt > BLANK_LINE_NUM - 1) {
                    break;
                }
                continue;
            } else {
                noDataRowcnt = 0;
            }
            listData.add(fileEntity);
            
            fileEntity.setOrderMonth(orderMonth);
            fileEntity.setRemark(remark);
            String saleContractNo = PoiUtil.getStringCellValue(sheet, i, count);
            fileEntity.setSaleContractNo(StringUtil.leftSubByByte(saleContractNo, IntDef.INT_TEN));
            count++;
            String customerCode = PoiUtil.getStringCellValue(sheet, i, IntDef.INT_THREE);
            if (StringUtil.isEmpty(customerCode)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_CustomerCode" });
                messageLists.add(message);
            } else {
                fileEntity.setCustomerCode(customerCode);
            }
            count++;
            fileEntity.setCustomerName(PoiUtil.getStringCellValue(sheet, i, count));
            count++;
            String partsNo = PoiUtil.getStringCellValue(sheet, i, IntDef.INT_FIVE);
            if (StringUtil.isEmpty(partsNo)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_PartsNo" });
                messageLists.add(message);
            } else {
                fileEntity.setPartNo(partsNo);
            }
            count++;
            fileEntity.setPartDescription(PoiUtil.getStringCellValue(sheet, i, count));
            count++;
            fileEntity.setCpn(StringUtil.leftSubByByte(PoiUtil.getStringCellValue(sheet, i, count),
                IntDef.INT_THIRTY_FIVE));
            count++;
            fileEntity.setSuppRegion(StringUtil.leftSubByByte(PoiUtil.getStringCellValue(sheet, i, count),
                IntDef.INT_TEN));
            count++;
            fileEntity.setCpo(StringUtil.leftSubByByte(PoiUtil.getStringCellValue(sheet, i, count),
                IntDef.INT_THIRTY_FIVE));
            count++;
            fileEntity
                .setZeroFlag(StringUtil.leftSubByByte(PoiUtil.getStringCellValue(sheet, i, count), IntDef.INT_ONE));
            count++;
            fileEntity.setSaleQty(makeDecimalValue(PoiUtil.getStringCellValue(sheet, i, count)));
            count++;
            fileEntity.setPurchaseQty(makeDecimalValue(PoiUtil.getStringCellValue(sheet, i, count)));
            count++;
            fileEntity.setOrderLot(makeDecimalValue(PoiUtil.getStringCellValue(sheet, i, count)));
            count++;
            fileEntity.setUom(StringUtil.leftSubByByte(PoiUtil.getStringCellValue(sheet, i, count), IntDef.INT_THREE));
            count++;
            fileEntity
                .setTradeNo(StringUtil.leftSubByByte(PoiUtil.getStringCellValue(sheet, i, count), IntDef.INT_TEN));
            count++;
            fileEntity.setTradeName(StringUtil.leftSubByByte(PoiUtil.getStringCellValue(sheet, i, count),
                IntDef.INT_SIXTY));
            count++;
            fileEntity.setQtySum(BigDecimal.ZERO);
            String qry1 = PoiUtil.getStringCellValue(sheet, i, count);
            
            if (!StringUtil.isEmpty(qry1) && !ValidatorUtils.checkDecimal(qry1)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast1",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry1);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast1", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast1" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty1(DecimalUtil.getBigDecimalWithNUll(qry1));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(qry1));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(), DecimalUtil.getBigDecimalWithNUll(qry1)));
            }
            count++;

            String qry2 = PoiUtil.getStringCellValue(sheet, i, count);
            if (!StringUtil.isEmpty(qry2) && !ValidatorUtils.checkDecimal(qry2)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast2",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry2);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast2", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast2" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty2(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(),
                    DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count))));
            }
            count++;

            String qry3 = PoiUtil.getStringCellValue(sheet, i, count);
            if (!StringUtil.isEmpty(qry3) && !ValidatorUtils.checkDecimal(qry3)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast3",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry3);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast3", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast3" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty3(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(),
                    DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count))));
            }
            count++;
            String qry4 = PoiUtil.getStringCellValue(sheet, i, count);
            if (!StringUtil.isEmpty(qry4) && !ValidatorUtils.checkDecimal(qry4)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast4",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry4);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast4", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast4" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty4(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(),
                    DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count))));
            }
            count++;
            String qry5 = PoiUtil.getStringCellValue(sheet, i, count);
            if (!StringUtil.isEmpty(qry5) && !ValidatorUtils.checkDecimal(qry5)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast5",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry5);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast5", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast5" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty5(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(),
                    DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count))));
            }
            count++;
            String qry6 = PoiUtil.getStringCellValue(sheet, i, count);
            if (!StringUtil.isEmpty(qry6) && !ValidatorUtils.checkDecimal(qry6)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast6",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry6);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast6", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast6" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty6(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(),
                    DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count))));
            }
            count++;
            String qry7 = PoiUtil.getStringCellValue(sheet, i, count);
            if (!StringUtil.isEmpty(qry7) && !ValidatorUtils.checkDecimal(qry7)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast7",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry7);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast7", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast7" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty7(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(),
                    DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count))));
            }
            count++;
            String qry8 = PoiUtil.getStringCellValue(sheet, i, count);
            if (!StringUtil.isEmpty(qry8) && !ValidatorUtils.checkDecimal(qry8)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast8",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry8);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast8", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast8" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty8(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(),
                    DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count))));
            }
            count++;
            String qry9 = PoiUtil.getStringCellValue(sheet, i, count);
            if (!StringUtil.isEmpty(qry9) && !ValidatorUtils.checkDecimal(qry9)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast9",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry9);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast9", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast9" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty9(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(),
                    DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count))));
            }
            count++;
            String qry10 = PoiUtil.getStringCellValue(sheet, i, count);
            if (!StringUtil.isEmpty(qry10) && !ValidatorUtils.checkDecimal(qry10)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast10",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry10);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast10", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast10" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty10(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(),
                    DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count))));
            }
            count++;
            String qry11 = PoiUtil.getStringCellValue(sheet, i, count);
            if (!StringUtil.isEmpty(qry11) && !ValidatorUtils.checkDecimal(qry11)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast11",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry11);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast11", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast11" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty11(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(),
                    DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count))));
            }
            count++;
            String qry12 = PoiUtil.getStringCellValue(sheet, i, count);
            if (!StringUtil.isEmpty(qry12) && !ValidatorUtils.checkDecimal(qry12)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast12",
                    String.valueOf(IntDef.INT_TEN), String.valueOf(IntDef.INT_SIX) });
                messageLists.add(message);
            } else {
                BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(qry12);
                if (!ValidatorUtils.checkMaxDecimal(qty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast12", "10", "6" });
                    messageLists.add(message);
                }
                if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPOOFF11_Grid_Forecast12" });
                    messageLists.add(message);
                }
                fileEntity.setFcQty12(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                qtys.add(DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count)));
                fileEntity.setQtySum(DecimalUtil.add(fileEntity.getQtySum(),
                    DecimalUtil.getBigDecimalWithNUll(PoiUtil.getStringCellValue(sheet, i, count))));
            }
            count++;
            fileEntity.setBuyingPrice(makeDecimalValue(PoiUtil.getStringCellValue(sheet, i, count)));
            count++;
            fileEntity.setBuyingCurrency(StringUtil.leftSubByByte(PoiUtil.getStringCellValue(sheet, i, count),
                IntDef.INT_FIVE));
            count++;
            fileEntity.setPriceUnit(makeDecimalValue(PoiUtil.getStringCellValue(sheet, i, count)));
            BigDecimal temp = BigDecimal.ZERO;
            Boolean qtyFlg = true;
            for (int j = 1; j <= qtys.size(); j++) {
                temp = DecimalUtil.add(temp, qtys.get(j - 1));
                if(qtys.get(j - 1) != null && qtyFlg == true){
                    qtyFlg = false;
                }
                if (DecimalUtil.isLess(temp, fileEntity.getQtySum()) && qtys.get(j - 1) == null) {
                    switch (j) {
                        case IntDef.INT_ONE:
                            fileEntity.setFcQty1(BigDecimal.ZERO);
                            break;
                        case IntDef.INT_TWO:
                            fileEntity.setFcQty2(BigDecimal.ZERO);
                            break;
                        case IntDef.INT_THREE:
                            fileEntity.setFcQty3(BigDecimal.ZERO);
                            break;
                        case IntDef.INT_FOUR:
                            fileEntity.setFcQty4(BigDecimal.ZERO);
                            break;
                        case IntDef.INT_FIVE:
                            fileEntity.setFcQty5(BigDecimal.ZERO);
                            break;
                        case IntDef.INT_SIX:
                            fileEntity.setFcQty6(BigDecimal.ZERO);
                            break;
                        case IntDef.INT_SEVEN:
                            fileEntity.setFcQty7(BigDecimal.ZERO);
                            break;
                        case IntDef.INT_EIGHT:
                            fileEntity.setFcQty8(BigDecimal.ZERO);
                            break;
                        case IntDef.INT_NINE:
                            fileEntity.setFcQty9(BigDecimal.ZERO);
                            break;
                        case IntDef.INT_TEN:
                            fileEntity.setFcQty10(BigDecimal.ZERO);
                            break;
                        case IntDef.INT_ELEVEN:
                            fileEntity.setFcQty11(BigDecimal.ZERO);
                            break;
                        case IntDef.INT_TWELVE:
                            fileEntity.setFcQty12(BigDecimal.ZERO);
                            break;
                    }
                }
            }
            // if all of the forecast Qty is blank, show the error message(w1004_154).
            if (DecimalUtil.isEquals(fileEntity.getQtySum(), BigDecimal.ZERO)) {
                if(qtyFlg){
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_154);
                    message.setMessageArgs(new String[] { Integer.toString(i) });
                    messageLists.add(message);
                }
            }
            if (!StringUtil.isEmpty(customerCode) && !StringUtil.isEmpty(partsNo)) {
                // if the date detail row that is not blank, check the column Customer Code and Part No is not repeat
                // with other rows, otherwise, show the error message(w1004_046).
                if (repeat.contains(customerCode + StringConst.COMMA + partsNo)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_046);
                    message.setMessageArgs(new String[] { "CPOOFF11_Grid_CustomerCode", customerCode,
                        "CPOOFF11_Grid_PartsNo", partsNo });
                    messageLists.add(message);
                } else {
                    repeat.add(customerCode + StringConst.COMMA + partsNo);
                }

                param.getSwapData().put("westCustCode", customerCode);
                param.getSwapData().put("westPartsNo", partsNo);
                param.getSwapData().put("officeId", param.getCurrentOfficeId());
                List<MainRouteEntity> listEntity = service.getForcast(param);

                if (listEntity != null && listEntity.size() != 0) {
                    boolean flag = false;
                    // set login user informations by session
                    // UserInfo loginUser = getLoginUser(request);
                    
                    UserManager um = UserManager.getLocalInstance(context);
                    List<BusinessPattern> list = um.getCurrentBusPattern();
                    for (int k = 0; k < listEntity.size(); k++) {

                        if (null != list && !list.isEmpty()) {

                            for (BusinessPattern bp : list) {
                                if (listEntity != null
                                        && (String.valueOf(bp.getCustomerId())).equals(listEntity.get(k)
                                            .getCustomerId())) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
//                        for (UserOffice uo : loginUser.getUserOffice()) {
//                            List<BusinessPattern> list = uo.getBusinessPatternList();
//                            if (list != null) {
//                                for (BusinessPattern bp : list) {
//                                    if (listEntity != null
//                                            && (String.valueOf(bp.getCustomerId())).equals(listEntity.get(k)
//                                                .getCustomerId())) {
//                                        flag = true;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
                        // if the customer code in upload file is not belong to login user, show the error
                        // message(w1004_121), and logic processing is discontinue.
                        if (!flag) {
                            String customerId = listEntity.get(k).getCustomerId();
                            TnmCustomer customer = service.getOneById(TnmCustomer.class, StringUtil.toSafeInteger(customerId));
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_121);
                            message.setMessageArgs(new String[] { partsNo, customer.getCustomerCode() });
                            messageLists.add(message);
                        }
                        
                        Integer forcastNum = listEntity.get(k).getForecastNum();
                        for (int j = 1; j <= forcastNum; j++) {
                            BigDecimal qty = null;
                            switch (j) {
                                case IntDef.INT_ONE:
                                    qty = fileEntity.getFcQty1();
                                    break;
                                case IntDef.INT_TWO:
                                    qty = fileEntity.getFcQty2();
                                    break;
                                case IntDef.INT_THREE:
                                    qty = fileEntity.getFcQty3();
                                    break;
                                case IntDef.INT_FOUR:
                                    qty = fileEntity.getFcQty4();
                                    break;
                                case IntDef.INT_FIVE:
                                    qty = fileEntity.getFcQty5();
                                    break;
                                case IntDef.INT_SIX:
                                    qty = fileEntity.getFcQty6();
                                    break;
                                case IntDef.INT_SEVEN:
                                    qty = fileEntity.getFcQty7();
                                    break;
                                case IntDef.INT_EIGHT:
                                    qty = fileEntity.getFcQty8();
                                    break;
                                case IntDef.INT_NINE:
                                    qty = fileEntity.getFcQty9();
                                    break;
                                case IntDef.INT_TEN:
                                    qty = fileEntity.getFcQty10();
                                    break;
                                case IntDef.INT_ELEVEN:
                                    qty = fileEntity.getFcQty11();
                                    break;
                                case IntDef.INT_TWELVE:
                                    qty = fileEntity.getFcQty12();
                                    break;
                            }
                            if (qty == null) {
                                partsNo1022.add(partsNo);
                                break;
                            }
                            BaseParam calParam = new BaseParam();
                            Integer expCalCode = StringUtil.toSafeInteger(listEntity.get(k).getExpCalendarCode());
                            String calendarCode = CodeCategoryManager.getCodeName(param.getLanguage(),
                                CodeMasterCategory.EXPORT_WH_CALENDER, expCalCode);
                            Calendar calFor = Calendar.getInstance();
                            calFor.setTime(orderMonthDate);
                            // calFor.add(Calendar.MONTH, j);
                            // order month + 1 = Exp Inbound plan date
                            calFor.add(Calendar.MONTH, j + 1);
                            Date forcastDate = calFor.getTime();
                            calParam.getSwapData().put("workingFlag", WorkingDay.WORKING_DAY);
                            calParam.getSwapData().put("partyType", IntDef.INT_SEVEN);
                            calParam.getSwapData().put(
                                "firstDate",
                                DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                                    DateTimeUtil.firstDay(forcastDate))));
                            calParam.getSwapData().put(
                                "lastDate",
                                DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                                    DateTimeUtil.lastDay(forcastDate))));
                            calParam.getSwapData().put("calendarCode", calendarCode);
                            List<CalculateDrEntity> listCal = service.getCalendarList(calParam);
                            if (listCal == null || listCal.size() == 0) {
                                if (!notExistCalendar.contains(calendarCode)) {
                                    notExistCalendar.add(calendarCode);
                                }
                            }
    
                            BaseParam shippingParam = new BaseParam();
                            shippingParam.getSwapData().put("inactiveFlag", InactiveFlag.ACTIVE);
                            Integer shippingRouteType = CodeCategoryManager.getCodeValue(param.getLanguage(),
                                CodeMasterCategory.SHIPPING_ROUTE_TYPE, "V-V");
                            shippingParam.getSwapData().put("shippingRouteType", shippingRouteType);
                            shippingParam.getSwapData().put("lastDay", DateTimeUtil.lastDay(forcastDate));
                            shippingParam.getSwapData().put("shippingRouteCode", listEntity.get(k).getShippingRouteCode());
                            int countData = service.checkShippingRoute(shippingParam);
                            if (countData <= 0 && !shippingMsterData.contains(listEntity.get(k).getShippingRouteCode())) {
                                shippingMsterData.add(listEntity.get(k).getShippingRouteCode());
                            }
                        }
                        // set discontinue parts as not completed
                        if (listEntity.get(k).getPartsStatus() != PartsStatus.COMPLETED
                                || listEntity.get(k).getInActiveFlag().equals(InactiveFlag.INACTIVE)) {
                            partsNo1021.add(partsNo);
                            listData.remove(fileEntity);
                            errorData.add(fileEntity);
                        }
                        if (listEntity.size() == IntDef.INT_ONE) {
                            fileEntity.setRoute(listEntity.get(k));
                            fileEntity.setRouteList(listEntity);
                        } else {
                            fileEntity.setRouteList(listEntity);
                        }
                    
                    }

                    

                } else {
                    partsNo1021.add(partsNo);
                    listData.remove(fileEntity);
                    errorData.add(fileEntity);
                }

            }
        }
        if (listData.size() == 0 && errorData.size() == 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
            messageLists.add(message);
        }
        if (notExistCalendar.size() != 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_152);
            message.setMessageArgs(new String[] { notExistCalendar.toString().substring(1,
                notExistCalendar.toString().length() - 1) });
            messageLists.add(message);
        }
        if (shippingMsterData.size() != 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_153);
            message.setMessageArgs(new String[] { shippingMsterData.toString().substring(1,
                shippingMsterData.toString().length() - 1) });
            messageLists.add(message);
        }
        if (messageLists.size() != 0) {
            return messageLists;
        } else {
            if (partsNo1021.size() != 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.C1021);
                message.setMessageArgs(new String[] { partsNo1021.toString().substring(IntDef.INT_ONE,
                    partsNo1021.toString().length() - 1) });
                confirm.add(message);
            }
            if (partsNo1022.size() != 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.C1022);
                message.setMessageArgs(new String[] { partsNo1022.toString().substring(IntDef.INT_ONE,
                    partsNo1022.toString().length() - 1) });
                confirm.add(message);
            }
            context.put(param.getSessionKey(), listData);
            context.put(param.getSessionKey() + "_orderMonth", (String) param.getSwapData().get("orderMonth"));
            context.put(param.getSessionKey() + "_remark", (String) param.getSwapData().get("remark"));
            return confirm;
        }
    }

    /**
     * get customer forecast for screen CPOCFS01 by filter.
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @return PageResult
     * @throws Exception e
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/om/CPOOFF11/getMainRoute",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<MainRouteEntity> getUserDetailsList(@RequestBody PageParam param, HttpServletRequest request)
        throws Exception {
        super.setCommonParam(param, request);
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        String sessionKey = (String) param.getSwapData().get("sessionKey");
        List<MainRouteEntity> result = (List<MainRouteEntity>) context.get(sessionKey + "_popup");
        service.getMainRouteDetail(result);
        PageResult<MainRouteEntity> pageResult = new PageResult<MainRouteEntity>();
        pageResult.setDatas(result);
        return pageResult;
    }

    /**
     * Download check file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return check result
     */
    @RequestMapping(value = "/om/CPOOFF11/downloadcheck")
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
    @RequestMapping(value = "/om/CPOOFF11/download")
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Create the download ZIP file
        String fileName = StringUtil.formatMessage(DOWNLOAD_FILE_NAME, param.getClientTime());
        response.setContentType(ZIP_CONTENT_TYPE);
        response.setCharacterEncoding(CSV_ENCODE);
        response.setHeader("Content-disposition", StringUtil.formatMessage("attachment; filename=\"{0}\"", fileName));
        OutputStream os = response.getOutputStream();
        ZipOutputStream zos = new ZipOutputStream(os);
        byte[] buf = new byte[BUFFER_SIZE];
        int len = 0;

        // Generate the WEST invoice files
        List<String> selectedDatas = param.getSelectedDatas();
        if (selectedDatas != null && selectedDatas.size() > 0) {
            for (String fileNameFromPage : selectedDatas) {
                // get path
                String path = ConfigUtil.get(Properties.UPLOAD_PATH_PFC);
                // path = "E:/common/cfc";
                File file = new File(path, fileNameFromPage + ".xlsx");
                if (file.exists()) {
                    // Compress the invoice file into ZIP file
                    ZipEntry ze = new ZipEntry(file.getName());
                    zos.putNextEntry(ze);
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                    while ((len = bis.read(buf)) > 0) {
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                    bis.close();
                }

            }
        }
        // Download the ZIP file
        zos.close();
        os.flush();
        os.close();
    }

    /**
     * make Decimal Value.
     * 
     * @param value need do cast value
     * @return cast value
     */
    private BigDecimal makeDecimalValue(String value) {
        
        // cast string to big decimal
        BigDecimal castValue =  DecimalUtil.getBigDecimalWithNUll(value);
        
        // do check
        if(!ValidatorUtils.checkMaxDecimal(castValue)); {
            castValue = BigDecimal.ZERO;
        }
        
        return castValue;
    }

}
