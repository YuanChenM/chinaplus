/**
 * CPPSPF11Controller.java
 * 
 * @screen CPPSPF11
 * @author xing_ming
 */
package com.chinaplus.web.vvp.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.FileBaseParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.vvp.entity.VVPDS01Entity;
import com.chinaplus.web.vvp.service.VVPDS01Service;

/**
 * Upload Sample Data File Controller.
 */
@Controller
public class VVPDS01Controller extends BaseFileController {

    /**
     * VVPDS01Service.
     */
    @Autowired
    private VVPDS01Service vvpds01Service;

    /**
     * Sample data file upload.
     * 
     * @param param parameter
     * @param request request
     * @param response response
     * @throws Exception Exception
     * @return ResponseBody
     */
    @RequestMapping(value = "/vvp/VVPDS01/detail", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<VVPDS01Entity> getSupplierDetailInfo(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        // get supplier Id
        Integer supplierId = StringUtil.toInteger(param.getSwapData().get("supplierId"));

        // if is empty, create new user
        if (supplierId == null) {
            return new BaseResult<VVPDS01Entity>();
        }

        // get user information by roleId
        VVPDS01Entity supplierInfo = vvpds01Service.getVVPSupplierInfo(supplierId);

        // user is not exist
        if (supplierInfo == null) {
            throw new BusinessException(MessageCodeConst.W1022);
        }

        // set user info
        BaseResult<VVPDS01Entity> result = new BaseResult<VVPDS01Entity>();
        result.setData(supplierInfo);

        return result;
    }
    
    /**
     * update detail for screen CPCUMS02.
     * 
     * @param param param
     * @param request request
     * @param response response
     * @throws Exception e
     */
    @RequestMapping(value = "/vvp/VVPDS01/updateDetail", method = RequestMethod.POST)
    @ResponseBody
    public void updateDetail(FileBaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        // set detail
        setCommonParam(param, request);
        
        // get data
        VVPDS01Entity supplierInfo = this.setValuesIntoEntity(param);
        
        // check is new or update
        boolean updateFlag = true;
        if(supplierInfo.getSupplierId() == null) {
            supplierInfo.setSupplierId(vvpds01Service.getNextSequence("SEQ_VVP_SUPPILER"));
            updateFlag = false;
        }
                
        // set attached file.
        this.prepareAttachedFiles(supplierInfo, param);

        // do update
        VVPDS01Entity saveInfo = vvpds01Service.doUpdateSupplierInfo(supplierInfo, updateFlag);
        
        // result
        BaseResult<Integer> result = new BaseResult<Integer>();
        result.setData(saveInfo.getSupplierId());

        super.setUploadResult(request, response, result);;
    }
    
    /**
     * Prepare attached files.
     * 
     * @param supplierInfo supplierInfo
     * @param files files
     */
    private void prepareAttachedFiles(VVPDS01Entity supplierInfo, FileBaseParam files) {
        
        // prepare supporting document 1
        if (StringUtil.isEmpty(supplierInfo.getSupportingDocument1())) {
            // check
            supplierInfo.setSupportingDocument1(this.saveFile(files.getSupportDoc1(), supplierInfo.getSupplierId(),
                IntDef.INT_ONE));
        }

        // prepare supporting document 2
        if (StringUtil.isEmpty(supplierInfo.getSupportingDocument2())) {
            // check
            supplierInfo.setSupportingDocument2(this.saveFile(files.getSupportDoc2(), supplierInfo.getSupplierId(),
                IntDef.INT_TWO));
        }

        // prepare supporting document 3
        if (StringUtil.isEmpty(supplierInfo.getSupportingDocument3())) {
            // check
            supplierInfo.setSupportingDocument3(this.saveFile(files.getSupportDoc3(), supplierInfo.getSupplierId(),
                IntDef.INT_THREE));
        }

        // prepare supporting document 4
        if (StringUtil.isEmpty(supplierInfo.getSupportingDocument4())) {
            // check
            supplierInfo.setSupportingDocument4(this.saveFile(files.getSupportDoc4(), supplierInfo.getSupplierId(),
                IntDef.INT_FOUR));
        }

        // prepare supporting document 5
        if (StringUtil.isEmpty(supplierInfo.getSupportingDocument5())) {
            // check
            supplierInfo.setSupportingDocument5(this.saveFile(files.getSupportDoc5(), supplierInfo.getSupplierId(),
                IntDef.INT_FIVE));
        }
    }

    /**
     * reflect all data into bean.
     * 
     * @param param entity
     * @return VVPDS01Entity VVPDS01Entity
     */
    private VVPDS01Entity setValuesIntoEntity(FileBaseParam param) {
        
        // get map
        Map<String, Object> swapData = param.getSwapData();
        VVPDS01Entity entity = new VVPDS01Entity();

        try {
            
            // get key
            Iterator<String> keySet = swapData.keySet().iterator();
            Class<VVPDS01Entity> cls = VVPDS01Entity.class;

            // loop
            while (keySet.hasNext()) {

                // field name
                String header = keySet.next();
                String value = StringUtil.toString(swapData.get(header));

                // get filed
                String getMethodName = prepareGetSetMethod(header, false);
                String setMethodName = prepareGetSetMethod(header, true);

                // get set method(must get super class method) and then only public method
                Method getMethod = null;
                try {
                    getMethod = cls.getMethod(getMethodName);
                } catch (Exception e) {
                    continue;
                }
                Method setMethod = cls.getMethod(setMethodName, getMethod.getReturnType());

                // check parameter types
                Class<?>[] types = setMethod.getParameterTypes();
                if (types.length != 1) {
                    continue;
                }

                // cast
                Object[] objects = castDataToDataType(value, types[0], header);

                // invoke
                if (objects != null) {
                    setMethod.invoke(entity, objects);
                }
            }

        } catch (Exception e) {
            // return
            //entity.setDataId(null);
            throw new BatchException();
        }
        
        return entity;
    }

    /**
     * Prepare set Method name.
     * 
     * @param filedName filedName
     * @param isSet true : set, false : get
     * @return Method name
     */
    private static String prepareGetSetMethod(String filedName, boolean isSet) {

        // append
        StringBuffer sb = new StringBuffer();
        if (isSet) {
            sb.append("set");
        } else {
            sb.append("get");
        }
        sb.append(filedName.substring(0, 1).toUpperCase());
        sb.append(filedName.substring(1));

        return sb.toString();
    }

    /**
     * Prepare set Method name.
     * 
     * @param data data
     * @param type type
     * @param itemName item name
     * @param <T> return type
     * @return Method name
     */
    private static <T> Object[] castDataToDataType(String data, Class<T> type, String itemName) {

        // define
        Object[] objects = null;

        // append
        try {

            // if null, then go return null Object
            if (data == null || StringUtil.isNullOrEmpty(data.trim())) {
                return new Object[] { null };
            }

            // check type
            if (type.getName().equals(String.class.getName())) {
                // cast to String
                objects = new Object[] { StringUtil.toString(data.trim()) };
            } else if (type.getName().equals(Integer.class.getName())) {
                // cast to Integer
                objects = new Object[] { Integer.valueOf(data) };
            } else if (type.getName().equals(BigDecimal.class.getName())) {
                // cast to BigDecimal
                objects = new Object[] { new BigDecimal(data).stripTrailingZeros() };
            } else if (type.getName().equals(Date.class.getName()) || type.getName().equals(Timestamp.class.getName())) {
                String dataStr = data.substring(0, IntDef.INT_TEN);
                // cast to Date
                objects = new Object[] { new java.sql.Date(DateTimeUtil.parseDate(dataStr, "yyyy-MM-dd").getTime()) };
            }
        } catch (Exception e) {
            // do nothing
            throw new BatchException();
        }

        return objects;
    }

    /**
     * save file.
     * 
     * @param file file
     * @param supplierId supplier Id
     * @param docNo docNo
     * 
     * @return saved file name
     */
    private String saveFile(MultipartFile file, Integer supplierId, Integer docNo) {

        // check file
        if (file == null || file.isEmpty()) {
            return null;
        }

        // check path
        String filePath = "/investigate/upload/profile/" + supplierId + "/" + docNo + "/";
        File fileFolder = new File(filePath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }

        try {

            // check file name exist or not
            String fullFileName = file.getOriginalFilename();

            // delete from path
            FileUtils.cleanDirectory(fileFolder);

            // create
            FileOutputStream ops = new FileOutputStream(new File(filePath + fullFileName));
            // copy
            FileCopyUtils.copy(file.getBytes(), ops);

            // if OK, return new file name
            return fullFileName;
        } catch (IOException e) {
            System.out.println("File does not exists.");
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

        return "VVPSDF11";
    }
}
