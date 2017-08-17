/**
 * CPVIVF02Controller.java
 * 
 * @screen CPVIVF02
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.control;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inv.entity.CPVIVF02Entity;
import com.chinaplus.web.inv.service.CPVIVF02Service;

/**
 * Download For WEST Controller.
 */
@Controller
public class CPVIVF02Controller extends BaseController {

    /** CSV file title */
    private static final String[] CSV_TITLE = { "Grid_ContainerNo", "Grid_ShipmentType", "Grid_PalletNo",
        "Grid_PartNo", "Grid_Qty", "Grid_Price", "Grid_Currency" };

    /** CSV file encode */
    private static final String CSV_ENCODE = "UTF-8";

    /** ZIP file content type */
    private static final String ZIP_CONTENT_TYPE = "application/x-zip-compressed;charset=UTF-8";

    /** Download file name */
    private static final String DOWNLOAD_FILE_NAME = "WEST Invoices_{0}.zip";

    /** Invoice separator */
    private static final String INVOICE_SEPARATOR = "#;!";

    /** Buffer size */
    private static final int BUFFER_SIZE = 1024;

    /** Download For WEST Service */
    @Autowired
    private CPVIVF02Service cpvivf02Service;

    /**
     * Download check for WEST invoice file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return check result
     */
    @RequestMapping(value = "/inv/CPVIVF02/downloadcheck")
    @ResponseBody
    public BaseResult<String> downloadCheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {

        return new BaseResult<String>();
    }

    /**
     * Download WEST invoice file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the exception
     */
    @RequestMapping(value = "/inv/CPVIVF02/download")
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {

        OutputStream os = null;
        ZipOutputStream zos = null;
        BufferedInputStream bis = null;
        try {
            // Create the download ZIP file
            String fileName = StringUtil.formatMessage(DOWNLOAD_FILE_NAME, param.getClientTime());
            response.setContentType(ZIP_CONTENT_TYPE);
            response.setCharacterEncoding(CSV_ENCODE);
            response.setHeader("Content-disposition",
                StringUtil.formatMessage("attachment; filename=\"{0}\"", fileName));
            os = response.getOutputStream();
            zos = new ZipOutputStream(os);
            byte[] buf = new byte[BUFFER_SIZE];
            int len = 0;

            // Create temporary path
            File tempFolder = new File(ConfigUtil.get(Properties.TEMPORARY_PATH) + UUID.randomUUID().toString());
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }

            // Generate the WEST invoice files
            List<String> selectedDatas = param.getSelectedDatas();
            if (selectedDatas != null && selectedDatas.size() > 0) {
                for (String invoiceInfo : selectedDatas) {
                    // Get invoice ID and No.
                    String[] invoiceKeyArray = invoiceInfo.split(INVOICE_SEPARATOR);
                    String invoiceSummaryId = invoiceKeyArray[0];
                    String invoiceNo = invoiceKeyArray[1];

                    // Create a WSET invoice file
                    File invoiceFile = new File(tempFolder, invoiceNo + StringConst.DOT + FileType.CSV.getSuffix());
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(invoiceFile),
                        CSV_ENCODE));

                    // Output the header title
                    StringBuffer sbTitle = new StringBuffer();
                    for (String title : CSV_TITLE) {
                        sbTitle.append(StringConst.COMMA);
                        sbTitle.append(MessageManager.getMessage(FileId.CPVIVF02 + StringConst.UNDERLINE + title));
                    }
                    bw.write(sbTitle.toString().substring(1));

                    // Output the detail data
                    CPVIVF02Entity conditon = new CPVIVF02Entity();
                    conditon.setInvoiceSummaryId(StringUtil.toInteger(invoiceSummaryId));
                    List<CPVIVF02Entity> invoiceList = cpvivf02Service.getWestInvoiceList(conditon);
                    if (invoiceList != null && invoiceList.size() > 0) {
                        for (CPVIVF02Entity detail : invoiceList) {
                            bw.newLine();

                            // Container No.
                            StringBuffer sbDetail = new StringBuffer();
                            sbDetail.append(StringConst.COMMA);
                            sbDetail.append(StringUtil.toSafeString(detail.getContainerNo()));
                            // Shipment Type
                            sbDetail.append(StringConst.COMMA);
                            sbDetail.append(StringUtil.toSafeString(detail.getSealNo()));
                            // Pallet No.
                            sbDetail.append(StringConst.COMMA);
                            sbDetail.append(StringUtil.toSafeString(detail.getModuleNo()));
                            // Part No.
                            String partNo = StringUtil.toSafeString(detail.getPartNo());
                            partNo = partNo.replaceAll(StringConst.BLANK, StringConst.EMPTY);
                            sbDetail.append(StringConst.COMMA);
                            sbDetail.append(partNo);
                            // Qty
                            sbDetail.append(StringConst.COMMA);
                            sbDetail.append(DecimalUtil.format(detail.getQty(), detail.getUomCode()).replaceAll(
                                StringConst.COMMA, StringConst.EMPTY));
                            // Price
                            sbDetail.append(StringConst.COMMA);
                            sbDetail.append(StringUtil.toSafeString(detail.getPrice()).replaceAll(StringConst.COMMA,
                                StringConst.EMPTY));
                            // Currency
                            sbDetail.append(StringConst.COMMA);
                            sbDetail.append(StringUtil.toSafeString(detail.getCurrency()));

                            bw.write(sbDetail.toString().substring(1));
                        }
                    }
                    bw.close();

                    // Compress the invoice file into ZIP file
                    ZipEntry ze = new ZipEntry(invoiceFile.getName());
                    zos.putNextEntry(ze);
                    bis = new BufferedInputStream(new FileInputStream(invoiceFile));
                    while ((len = bis.read(buf)) > 0) {
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                    bis.close();

                    // Delete the temporary file
                    invoiceFile.delete();
                }
            }

            // Download the ZIP file
            tempFolder.delete();
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

}
