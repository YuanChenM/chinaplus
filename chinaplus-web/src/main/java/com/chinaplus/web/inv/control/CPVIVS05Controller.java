/**
 * CPVIVS05Controller.java
 * 
 * @screen CPVIVS05
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.inv.entity.CPVIVF11KanbEntity;
import com.chinaplus.web.inv.entity.CPVIVF11SessionEntity;
import com.chinaplus.web.inv.entity.CPVIVF11SupportEntity;

/**
 * Supplementary Data:the part qty in invoice of each supplier Controller.
 */
@Controller
public class CPVIVS05Controller extends BaseController {

    /** separator */
    private static final String SEPARATOR = "#;!";

    /**
     * Load supplementary data list.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @return supplementary data list
     */
    @RequestMapping(value = "/inv/CPVIVS05/loadSupplementaryData",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPVIVF11KanbEntity> getSupplementaryData(@RequestBody PageParam param, HttpServletRequest request) {

        PageResult<CPVIVF11KanbEntity> result = new PageResult<CPVIVF11KanbEntity>();
        List<CPVIVF11KanbEntity> supplementaryDatas = new ArrayList<CPVIVF11KanbEntity>();
        result.setDatas(supplementaryDatas);

        // Get all KANB data from session
        SessionInfoManager session = SessionInfoManager.getContextInstance(request);
        CPVIVF11SessionEntity sessionEntity = (CPVIVF11SessionEntity) session
            .get(CPVIVF11Controller.SESSION_KEY_INVOICE_UPLOAD);
        List<CPVIVF11KanbEntity> kanbTotalList = sessionEntity.getKanbTotalList();
        if (kanbTotalList != null && kanbTotalList.size() > 0) {
            for (CPVIVF11KanbEntity kanbTotal : kanbTotalList) {
                List<CPVIVF11SupportEntity> suppliers = kanbTotal.getSuppliers();
                if (suppliers != null) {
                    for (CPVIVF11SupportEntity supplier : suppliers) {
                        CPVIVF11KanbEntity supplementaryData = new CPVIVF11KanbEntity();
                        supplementaryData.setInvoiceNo(kanbTotal.getInvoiceNo());
                        supplementaryData.setPartsId(kanbTotal.getPartsId());
                        supplementaryData.setTtcPartsNo(kanbTotal.getTtcPartsNo());
                        supplementaryData.setOrderMonth(kanbTotal.getOrderMonth());
                        supplementaryData.setDisOrderMonth(kanbTotal.getOrderMonth());
                        supplementaryData.setUomCode(kanbTotal.getUomCode());
                        supplementaryData.setQty(kanbTotal.getQty());
                        supplementaryData.setSupplierId(supplier.getSupplierId());
                        supplementaryData.setExpPartsId(supplier.getExpPartsId());
                        supplementaryData.setTtcSuppCode(supplier.getTtcSuppCode());
                        supplementaryData.setExpRegion(supplier.getExpRegion());
                        supplementaryDatas.add(supplementaryData);
                    }
                }
            }
        }

        // Sort supplementary data list
        Collections.sort(supplementaryDatas, new Comparator<CPVIVF11KanbEntity>() {
            @Override
            public int compare(CPVIVF11KanbEntity kanb1, CPVIVF11KanbEntity kanb2) {

                int result = kanb1.getInvoiceNo().compareTo(kanb2.getInvoiceNo());
                if (result == 0) {
                    result = kanb1.getTtcPartsNo().compareTo(kanb2.getTtcPartsNo());
                    if (result == 0) {
                        result = kanb1.getOrderMonth().compareTo(kanb2.getOrderMonth());
                        if (result == 0) {
                            result = kanb1.getTtcSuppCode().compareTo(kanb2.getTtcSuppCode());
                        }
                    }
                }
                return result;
            }
        });

        // Merge cells process
        for (int i = 0; i < supplementaryDatas.size(); i++) {
            CPVIVF11KanbEntity supplementaryData = supplementaryDatas.get(i);
            if (i == 0) {
                supplementaryData.setShowFlag(IntDef.INT_ZERO);
                continue;
            }
            CPVIVF11KanbEntity preData = supplementaryDatas.get(i - 1);
            if (supplementaryData.getInvoiceNo().equals(preData.getInvoiceNo())) {
                if (supplementaryData.getPartsId().equals(preData.getPartsId())) {
                    if (supplementaryData.getOrderMonth().equals(preData.getOrderMonth())) {
                        supplementaryData.setShowFlag(IntDef.INT_THREE);
                    } else {
                        supplementaryData.setShowFlag(IntDef.INT_TWO);
                    }
                } else {
                    supplementaryData.setShowFlag(IntDef.INT_ONE);
                }
            } else {
                supplementaryData.setShowFlag(IntDef.INT_ZERO);
            }
        }

        return result;
    }

    /**
     * Confirm supplier qty.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return process result
     * @throws Exception the Exception
     */
    @RequestMapping(value = "/inv/CPVIVS05/confirm",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> confrim(@RequestBody ObjectParam<CPVIVF11KanbEntity> param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        List<CPVIVF11KanbEntity> supplementaryDatas = param.getDatas();
        Map<String, BigDecimal[]> supplementartMap = new HashMap<String, BigDecimal[]>();
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        for (int i = 0; i < supplementaryDatas.size(); i++) {
            CPVIVF11KanbEntity data = supplementaryDatas.get(i);
            // Check input qty
            BigDecimal inputQty = data.getInputQty();
            if (inputQty == null) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_010);
                message.setMessageArgs(new String[] { String.valueOf(i + 1), "CPVIVS05_Grid_Qty" });
                messageLists.add(message);
            } else if (!ValidatorUtils.checkMaxDecimal(inputQty)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_013);
                message.setMessageArgs(new String[] { String.valueOf(i + 1), "CPVIVS05_Grid_Qty", "10", "6" });
                messageLists.add(message);
            } else if (DecimalUtil.isLess(inputQty, BigDecimal.ZERO)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_012);
                message.setMessageArgs(new String[] { String.valueOf(i + 1), "CPVIVS05_Grid_Qty" });
                messageLists.add(message);
            }

            // Add relevance data
            String mapKey = data.getInvoiceNo() + SEPARATOR + data.getTtcPartsNo() + SEPARATOR + data.getOrderMonth();
            BigDecimal[] qtyArray = supplementartMap.get(mapKey);
            if (qtyArray == null) {
                qtyArray = new BigDecimal[IntDef.INT_TWO];
                qtyArray[0] = data.getQty();
                supplementartMap.put(mapKey, qtyArray);
            }
            qtyArray[1] = DecimalUtil.add(qtyArray[1], inputQty);
        }

        // Check relevance qty
        for (Map.Entry<String, BigDecimal[]> entry : supplementartMap.entrySet()) {
            String key = entry.getKey();
            BigDecimal[] qtyArray = entry.getValue();
            if (!DecimalUtil.isEquals(qtyArray[0], qtyArray[1])) {
                String[] keyData = key.split(SEPARATOR);
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_014);
                message.setMessageArgs(new String[] { keyData[IntDef.INT_ZERO], keyData[IntDef.INT_ONE],
                    keyData[IntDef.INT_TWO] });
                messageLists.add(message);
            }
        }

        if (messageLists.size() > 0) {
            throw new BusinessException(messageLists);
        }

        // Update session data
        SessionInfoManager session = SessionInfoManager.getContextInstance(request);
        CPVIVF11SessionEntity sessionEntity = (CPVIVF11SessionEntity) session
            .get(CPVIVF11Controller.SESSION_KEY_INVOICE_UPLOAD);
        List<CPVIVF11KanbEntity> kanbTotalList = sessionEntity.getKanbTotalList();
        List<CPVIVF11KanbEntity> excessDataList = new ArrayList<CPVIVF11KanbEntity>();
        if (kanbTotalList != null && kanbTotalList.size() > 0) {
            for (int i = 0; i < kanbTotalList.size(); i++) {
                CPVIVF11KanbEntity kanbTotal = kanbTotalList.get(i);
                List<CPVIVF11SupportEntity> suppliers = kanbTotal.getSuppliers();
                if (suppliers != null) {
                    for (CPVIVF11KanbEntity data : supplementaryDatas) {
                        if (data.getInvoiceNo().equals(kanbTotal.getInvoiceNo())
                                && data.getPartsId().equals(kanbTotal.getPartsId())
                                && data.getOrderMonth().equals(kanbTotal.getOrderMonth())
                                && DecimalUtil.isGreater(data.getInputQty(), BigDecimal.ZERO)) {
                            if (kanbTotal.getSuppliers() != null) {
                                kanbTotal.setSupplierId(data.getSupplierId());
                                kanbTotal.setExpPartsId(data.getExpPartsId());
                                kanbTotal.setTtcSuppCode(data.getTtcSuppCode());
                                kanbTotal.setExpRegion(data.getExpRegion());
                                kanbTotal.setQty(data.getInputQty());
                                kanbTotal.setSuppliers(null);
                            } else {
                                CPVIVF11KanbEntity excessData = new CPVIVF11KanbEntity();
                                BeanUtils.copyProperties(kanbTotal, excessData);
                                excessData.setSupplierId(data.getSupplierId());
                                excessData.setExpPartsId(data.getExpPartsId());
                                excessData.setTtcSuppCode(data.getTtcSuppCode());
                                excessData.setExpRegion(data.getExpRegion());
                                excessData.setQty(data.getInputQty());
                                excessDataList.add(excessData);
                            }
                        }
                    }
                }
            }
        }
        if (excessDataList.size() > 0) {
            kanbTotalList.addAll(excessDataList);
        }

        return new BaseResult<String>();
    }

}
