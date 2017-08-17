/**
 * CPSDRF01Service.java
 * 
 * @screen CPSKSS01
 * @author shi_yuxi
 */
package com.chinaplus.web.sa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.sa.entity.CPSDRF01Entity;

/**
 * KPI download
 */
@Service
public class CPSDRF01Service extends BaseService {

    /**
     * getVvInfo
     * 
     * @param param param
     * @return List<CPSDRF01Entity>
     */
    public List<CPSDRF01Entity> getVvInfo(BaseParam param) {

        param.getSwapData().put("aisinFlag", false);
        param.getSwapData().put("vvFalg", true);
        List<CPSDRF01Entity> list = baseMapper.select(getSqlId("getvvInfo"), param);
        if (list.size() == 0) {
            return list;
        }

        List<String> imps = new ArrayList<String>();
        Map<String, CPSDRF01Entity> planCompare = new HashMap<String, CPSDRF01Entity>();
        for (CPSDRF01Entity entity : list) {
            if (!imps.contains(entity.getImpPoNo())) {
                imps.add(entity.getImpPoNo());
            }
            String key = entity.getPartsId() + StringConst.COMMA + entity.getSupplierCode() + StringConst.COMMA
                    + entity.getImpPoNo() + StringConst.COMMA + getTime(entity.getEtd());
            if (!planCompare.containsKey(key)) {
                planCompare.put(key, entity);
            }
        }
        param.getSwapData().put("imps", imps);
        List<CPSDRF01Entity> invoiceList = baseMapper.select(getSqlId("getInvoiceQty"), param);
        Map<String, CPSDRF01Entity> compare = new HashMap<String, CPSDRF01Entity>();
        for (CPSDRF01Entity entity : invoiceList) {
            String key = entity.getPartsId() + StringConst.COMMA + entity.getSupplierCode() + StringConst.COMMA
                    + entity.getImpPoNo() + StringConst.COMMA + getTime(entity.getEtd());
            if (!compare.containsKey(key)) {
                // entity.setInvoiceQty(null);
                // compare.get(key).setInvoiceQty(compare.get(key).getInvoiceQty().add(entity.getInvoiceQty()));
                compare.put(key, entity);
            }
        }
        for (CPSDRF01Entity entity : list) {
            String key = entity.getPartsId() + StringConst.COMMA + entity.getImpPoNo() + StringConst.COMMA
                    + getTime(entity.getEtd());
            if (!compare.containsKey(key)) {
                invoiceList.add(entity);
            }
        }
        if (invoiceList.size() == 0) {
            return invoiceList;
        }
        // Sort supplementary data list
        Collections.sort(invoiceList, new Comparator<CPSDRF01Entity>() {
            @Override
            public int compare(CPSDRF01Entity kanb1, CPSDRF01Entity kanb2) {

                int result = kanb1.getTtcPart().compareTo(kanb2.getTtcPart());
                if (result == 0) {
                    result = kanb1.getCustomerCode().compareTo(kanb2.getCustomerCode());
                    if (result == 0) {
                        result = kanb1.getRegion().compareTo(kanb2.getRegion());
                        if (result == 0) {
                            result = kanb1.getSupplierCode().compareTo(kanb2.getSupplierCode());
                            if (result == 0) {
                                result = kanb1.getExpSoDate().compareTo(kanb2.getExpSoDate());
                                if (result == 0) {
                                    result = kanb1.getImpPoNo().compareTo(kanb2.getImpPoNo());
                                    if (result == 0) {
                                        result = kanb1.getEtd().compareTo(kanb2.getEtd());
                                        if (result == 0) {
                                            result = StringUtil.toSafeString(kanb1.getInvoiceNo()).compareTo(
                                                StringUtil.toSafeString(kanb2.getInvoiceNo()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return result;
            }
        });
        compare.clear();
        for (CPSDRF01Entity entity : invoiceList) {
            String key = entity.getPartsId() + StringConst.COMMA + entity.getSupplierCode() + StringConst.COMMA
                    + entity.getImpPoNo() + StringConst.COMMA + getTime(entity.getEtd());
            if (entity.getInvoiceQty() != null) {
                entity.setInvoiceNoQty("(" + StringUtil.formatBigDecimal(entity.getUomCode(), entity.getInvoiceQty())
                        + ")");
            }
            if (compare.containsKey(key)) {
                if (compare.get(key).getInvoiceQty() != null) {
                    BigDecimal sumInvoiceQty = DecimalUtil
                        .add(compare.get(key).getInvoiceQty(), entity.getInvoiceQty());
                    compare.get(key).setInvoiceQty(sumInvoiceQty);
                    compare.get(key).setNeedShowQty(true);
                    entity.setInvoiceQty(null);
                    entity.setEtd(null);
                    entity.setSumQty(null);
                    entity.setNeedShowQty(true);
                }
            } else {
                if (planCompare.containsKey(key)) {
                    entity.setSumQty(planCompare.get(key).getSumQty());
                }
                compare.put(key, entity);
            }
        }
        BigDecimal delay = new BigDecimal(0);
        String ipo = invoiceList.get(0).getImpPoNo() + StringConst.COMMA + invoiceList.get(0).getTtcPart()
                + StringConst.COMMA + invoiceList.get(0).getCustomerCode();
        for (CPSDRF01Entity entity : invoiceList) {
            if (entity.getEtd() != null) {
                if (!ipo.equals(entity.getImpPoNo() + StringConst.COMMA + entity.getTtcPart() + StringConst.COMMA
                        + entity.getCustomerCode())) {
                    delay = new BigDecimal(0);
                    ipo = entity.getImpPoNo() + StringConst.COMMA + entity.getTtcPart() + StringConst.COMMA
                            + entity.getCustomerCode();
                }
                delay = DecimalUtil.add(delay, entity.getSumQty());
                delay = DecimalUtil.subtract(delay, entity.getInvoiceQty());
                entity.setDelay(delay);
            }
        }
        return invoiceList;
    }

    /**
     * getAisinInfo
     * 
     * @param param param
     * @return List<CPSDRF01Entity>
     */
    public List<CPSDRF01Entity> getAisinInfo(BaseParam param) {
        param.getSwapData().put("vvFalg", false);
        param.getSwapData().put("aisinFlag", true);
        List<CPSDRF01Entity> list = baseMapper.select(getSqlId("getaisinInfo"), param);
        if (list.size() == 0) {
            return list;
        }
        List<String> planNos = new ArrayList<String>();
        Map<String, CPSDRF01Entity> planCompare = new HashMap<String, CPSDRF01Entity>();
        for (CPSDRF01Entity entity : list) {
            if (!planNos.contains(entity.getImpPoNo())) {
                planNos.add(entity.getPlanNo());
            }
            String key = entity.getPartsId() + StringConst.COMMA + entity.getPlanNo() + StringConst.COMMA
                    + getTime(entity.getEtd());
            if (!planCompare.containsKey(key)) {
                planCompare.put(key, entity);
            }
        }
        param.getSwapData().put("planNos", planNos);
        List<CPSDRF01Entity> invoiceList = baseMapper.select(getSqlId("getInvoiceQty"), param);
        Map<String, CPSDRF01Entity> compare = new HashMap<String, CPSDRF01Entity>();
        for (CPSDRF01Entity entity : invoiceList) {
            String key = entity.getPartsId() + StringConst.COMMA + entity.getPlanNo() + StringConst.COMMA
                    + getTime(entity.getEtd());
            if (!compare.containsKey(key)) {
                // entity.setInvoiceQty(null);
                // compare.get(key).setInvoiceQty(compare.get(key).getInvoiceQty().add(entity.getInvoiceQty()));
                compare.put(key, entity);
            }
        }
        for (CPSDRF01Entity entity : list) {
            String key = entity.getPartsId() + StringConst.COMMA + entity.getPlanNo() + StringConst.COMMA
                    + getTime(entity.getEtd());
            if (!compare.containsKey(key)) {
                invoiceList.add(entity);
            }
        }
        if (invoiceList.size() == 0) {
            return invoiceList;
        }
        // Sort supplementary data list
        Collections.sort(invoiceList, new Comparator<CPSDRF01Entity>() {
            @Override
            public int compare(CPSDRF01Entity kanb1, CPSDRF01Entity kanb2) {

                int result = kanb1.getTtcPart().compareTo(kanb2.getTtcPart());
                if (result == 0) {
                    result = kanb1.getCustomerCode().compareTo(kanb2.getCustomerCode());
                    if (result == 0) {
                        result = kanb1.getRegion().compareTo(kanb2.getRegion());
                        if (result == 0) {
                            result = kanb1.getSupplierCode().compareTo(kanb2.getSupplierCode());
                            if (result == 0) {
                                result = kanb1.getOrderMonth().compareTo(kanb2.getOrderMonth());
                                if (result == 0) {
                                    result = kanb1.getPlanNo().compareTo(kanb2.getPlanNo());
                                    if (result == 0) {
                                        result = kanb1.getEtd().compareTo(kanb2.getEtd());
                                        if (result == 0) {
                                            result = StringUtil.toSafeString(kanb1.getInvoiceNo()).compareTo(
                                                StringUtil.toSafeString(kanb2.getInvoiceNo()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return result;
            }
        });
        compare.clear();
        for (CPSDRF01Entity entity : invoiceList) {
            String key = entity.getPartsId() + StringConst.COMMA + entity.getPlanNo() + StringConst.COMMA
                    + getTime(entity.getEtd());
            if (entity.getInvoiceQty() != null) {
                entity.setInvoiceNoQty("(" + StringUtil.formatBigDecimal(entity.getUomCode(), entity.getInvoiceQty())
                        + ")");
            }
            if (compare.containsKey(key)) {
                if (compare.get(key).getInvoiceQty() != null) {
                    BigDecimal sumInvoiceQty = DecimalUtil
                        .add(compare.get(key).getInvoiceQty(), entity.getInvoiceQty());
                    compare.get(key).setInvoiceQty(sumInvoiceQty);
                    compare.get(key).setNeedShowQty(true);
                    entity.setInvoiceQty(null);
                    entity.setEtd(null);
                    entity.setSumQty(null);
                    entity.setNeedShowQty(true);
                }
            } else {
                if (planCompare.containsKey(key)) {
                    entity.setSumQty(planCompare.get(key).getSumQty());
                }
                compare.put(key, entity);
            }
        }
        BigDecimal delay = new BigDecimal(0);
        String ipo = invoiceList.get(0).getPlanNo() + StringConst.COMMA + invoiceList.get(0).getTtcPart()
                + StringConst.COMMA + invoiceList.get(0).getCustomerCode();
        for (CPSDRF01Entity entity : invoiceList) {
            if (entity.getEtd() != null) {
                if (!ipo.equals(entity.getPlanNo() + StringConst.COMMA + entity.getTtcPart() + StringConst.COMMA
                        + entity.getCustomerCode())) {
                    delay = new BigDecimal(0);
                    ipo = entity.getPlanNo() + StringConst.COMMA + entity.getTtcPart() + StringConst.COMMA
                            + entity.getCustomerCode();
                }
                delay = DecimalUtil.add(delay, entity.getSumQty());
                delay = DecimalUtil.subtract(delay, entity.getInvoiceQty());
                entity.setDelay(delay);
            }
        }
        return invoiceList;
    }

    /**
     * 
     * getTime
     * 
     * @param etd etd
     * @return String
     */
    public String getTime(Date etd) {
        if (etd != null) {
            return etd.getTime() + StringConst.EMPTY;
        } else {
            return StringConst.EMPTY;
        }
    }

    // /**
    // * Remove last zero
    // *
    // * @param value value
    // * @return value after remove
    // */
    // private String removeLastZero(BigDecimal value) {
    //
    // if (value == null) {
    // return StringConst.EMPTY;
    // }
    // String ret = value.toString();
    // while (ret.endsWith(".") || (ret.contains(".") && ret.endsWith("0"))) {
    // ret = ret.substring(0, ret.length() - IntDef.INT_ONE);
    // }
    // return ret;
    // }

}
