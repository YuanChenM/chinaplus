/**
 * CPOOFS01Service.
 * 
 * @author shi_yuxi
 * @screen CPOOFF11
 */
package com.chinaplus.web.om.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.SupplyChainEntity;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.ActiveFlag;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.TransportMode;
import com.chinaplus.common.consts.CodeConst.WorkingDay;
import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.common.entity.TnmSupplier;
import com.chinaplus.common.entity.TntPfcDetail;
import com.chinaplus.common.entity.TntPfcMaster;
import com.chinaplus.common.entity.TntPfcShipping;
import com.chinaplus.common.service.SupplyChainService;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.ExcelUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOOFF11Entity;
import com.chinaplus.web.om.entity.CalculateDrEntity;
import com.chinaplus.web.om.entity.MainRouteEntity;

/**
 * Upload Order Forecast File
 */
@Service
public class CPOOFF11Service extends BaseService {
    /** DETAIL_START_LINE */
    private static final int DETAIL_START_LINE = 8;

    @Autowired
    private CalculateDrService service;

    @Autowired
    private SupplyChainService commonService;

    /**
     * get forcast info
     * 
     * @param param param
     * @return List<CPOOFF11Entity>
     */
    public List<MainRouteEntity> getForcast(BaseParam param) {
        return baseMapper.select(getSqlId("getForcast"), param);
    }

    /**
     * Get PFCNO.
     * 
     * @param firstDate start target month
     * @param lastDate end target month
     * @param orderMonth order month
     * @param customer customer
     * @param No No.
     * @return PFCNO
     */
    private String getPfcNo(Date firstDate, Date lastDate, String orderMonth, TnmCustomer customer, String No) {

        StringBuffer bfPfcNo = new StringBuffer();
        bfPfcNo.append(customer.getCustomerCode()).append(StringConst.UNDERLINE);
        bfPfcNo.append(orderMonth).append(StringConst.UNDERLINE);
        bfPfcNo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, firstDate));
        if (!firstDate.equals(lastDate)) {
            bfPfcNo.append(StringConst.MIDDLE_LINE);
            bfPfcNo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, lastDate));
        }
        bfPfcNo.append(StringConst.UNDERLINE);
        bfPfcNo.append(StringUtil.PadLeft(No, IntDef.INT_THREE, String.valueOf(IntDef.INT_ZERO)));

        return bfPfcNo.toString();
    }

    /**
     * save data
     * 
     * @param list datas
     * @param baseParam baseParam
     * @throws Exception Exception
     */
    public void doSaveData(List<CPOOFF11Entity> list, BaseParam baseParam) throws Exception {

        Map<String, List<CPOOFF11Entity>> map = new HashMap<String, List<CPOOFF11Entity>>();
        Map<Integer, CPOOFF11Entity> mapGetEntity = new HashMap<Integer, CPOOFF11Entity>();
        for (CPOOFF11Entity entity : list) {
            if (map.containsKey(entity.getRoute().getCustomerId())) {
                map.get(entity.getRoute().getCustomerId()).add(entity);
            } else {
                List<CPOOFF11Entity> newList = new ArrayList<CPOOFF11Entity>();
                newList.add(entity);
                map.put(entity.getRoute().getCustomerId(), newList);
            }
            mapGetEntity.put(StringUtil.toSafeInteger(entity.getRoute().getPartsId()), entity);
        }

        String orderMonth = (String) baseParam.getSwapData().get("orderMonth");
        Date orderMonthDate = DateTimeUtil.parseDate(orderMonth);
        orderMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, orderMonthDate);
        String remark = (String) baseParam.getSwapData().get("remark");

        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
        Timestamp officeTime = super.getDBDateTime(baseParam.getOfficeTimezone());

        for (Map.Entry<String, List<CPOOFF11Entity>> entry : map.entrySet()) {

            List<TntPfcMaster> checkList = getPfcMater(baseParam.getCurrentOfficeId(), orderMonth, entry.getKey());
            Date firstDate = null;
            Date lastDate = null;
            Date[] dates = getFirstAndLast(entry.getValue(), orderMonthDate, firstDate, lastDate);
            firstDate = dates[0];
            lastDate = dates[1];
            TntPfcMaster pfcMater = new TntPfcMaster();
            TnmCustomer customer = baseDao.findOne(TnmCustomer.class, StringUtil.toSafeInteger(entry.getKey()));

            if (checkList.size() == 0) {

                pfcMater.setPfcNo(getPfcNo(firstDate, lastDate, orderMonth, customer, String.valueOf(IntDef.INT_ONE)));
                pfcMater.setOfficeId(baseParam.getCurrentOfficeId());
                pfcMater.setBusinessPattern(CodeConst.BusinessPattern.V_V);
                pfcMater.setImpRegion(entry.getValue().get(0).getRoute().getImpRegion());
                pfcMater.setCustomerId(StringUtil.toSafeInteger(entry.getKey()));
                pfcMater.setOrderMonth(orderMonth);
                pfcMater.setFirstFcMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, firstDate));
                pfcMater.setLastFcMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, lastDate));
                pfcMater.setRemark(remark);
                pfcMater.setReviseVersion(IntDef.INT_ZERO);
                pfcMater.setStatus(CodeConst.OrderForecastStatus.NORMAL);
                pfcMater.setUploadedBy(baseParam.getLoginUserId());
                pfcMater.setUploadedDate(officeTime);
                pfcMater.setCreatedBy(baseParam.getLoginUserId());
                pfcMater.setUpdatedBy(baseParam.getLoginUserId());
                pfcMater.setCreatedDate(systemTime);
                pfcMater.setUpdatedDate(systemTime);
                pfcMater.setVersion(IntDef.INT_ONE);
                baseDao.insert(pfcMater);
                baseDao.flush();
                pfcMater = getPfcMater(baseParam.getCurrentOfficeId(), orderMonth, entry.getKey()).get(0);
                int ver = 1;
                for (CPOOFF11Entity entity : entry.getValue()) {
                    TntPfcDetail copy = new TntPfcDetail();
                    BeanUtils.copyProperties(entity, copy);
                    copy.setPfcId(pfcMater.getPfcId());
                    copy.setPartsId(StringUtil.toSafeInteger(entity.getRoute().getPartsId()));
                    copy.setSupplierId(StringUtil.toSafeInteger(entity.getRoute().getSupplierId()));
                    copy.setShippingRouteCode(entity.getRoute().getShippingRouteCode());
                    copy.setRedoShippingFlag(IntDef.INT_ZERO);
                    copy.setCreatedBy(baseParam.getLoginUserId());
                    copy.setUpdatedBy(baseParam.getLoginUserId());
                    copy.setCreatedDate(systemTime);
                    copy.setUpdatedDate(systemTime);
                    copy.setVersion(ver++);
                    baseDao.insert(copy);

                }
            } else {

                TntPfcMaster dbMaster = checkList.get(0);
                Integer pfcId = dbMaster.getPfcId();
                TntPfcMaster dbMasterInto = new TntPfcMaster();
                BeanUtils.copyProperties(dbMaster, dbMasterInto);
                dbMasterInto.setPfcId(null);
                dbMasterInto.setReviseVersion(dbMaster.getReviseVersion() + 1);
                baseDao.insert(dbMasterInto);
                int i = StringUtil.toSafeInteger(dbMaster.getPfcNo().substring(dbMaster.getPfcNo().length() - IntDef.INT_THREE)) + 1;
                dbMaster.setPfcNo(getPfcNo(firstDate, lastDate, orderMonth, customer, String.valueOf(i)));
                dbMaster.setOfficeId(baseParam.getCurrentOfficeId());
                dbMaster.setBusinessPattern(CodeConst.BusinessPattern.V_V);
                dbMaster.setImpRegion(entry.getValue().get(0).getRoute().getImpRegion());
                dbMaster.setCustomerId(StringUtil.toSafeInteger(entry.getKey()));
                dbMaster.setOrderMonth(orderMonth);
                dbMaster.setFirstFcMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, firstDate));
                dbMaster.setLastFcMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, lastDate));
                dbMaster.setRemark(remark);
                dbMaster.setStatus(CodeConst.OrderForecastStatus.NORMAL);
                dbMaster.setUploadedBy(baseParam.getLoginUserId());
                dbMaster.setUploadedDate(officeTime);
                dbMaster.setCreatedBy(baseParam.getLoginUserId());
                dbMaster.setUpdatedBy(baseParam.getLoginUserId());
                dbMaster.setCreatedDate(systemTime);
                dbMaster.setUpdatedDate(systemTime);
                dbMaster.setVersion(dbMaster.getVersion() + 1);
                dbMaster.setPfcId(pfcId);
                baseDao.update(dbMaster);

                for (CPOOFF11Entity entity : entry.getValue()) {

                    String hql = "FROM TNT_PFC_DETAIL WHERE PFC_ID = ? AND PARTS_ID = ? AND SUPPLIER_ID = ?";
                    Object[] param = new Object[] { dbMaster.getPfcId(), entity.getRoute().getPartsId(),
                        entity.getRoute().getSupplierId() };
                    TntPfcDetail detail = baseDao.findOne(hql, param);

                    if (detail == null) {

                        TntPfcDetail copy = new TntPfcDetail();
                        BeanUtils.copyProperties(entity, copy);
                        copy.setPfcId(dbMaster.getPfcId());
                        copy.setPartsId(StringUtil.toSafeInteger(entity.getRoute().getPartsId()));
                        copy.setSupplierId(StringUtil.toSafeInteger(entity.getRoute().getSupplierId()));
                        copy.setShippingRouteCode(entity.getRoute().getShippingRouteCode());
                        copy.setRedoShippingFlag(IntDef.INT_ZERO);
                        copy.setCreatedBy(baseParam.getLoginUserId());
                        copy.setUpdatedBy(baseParam.getLoginUserId());
                        copy.setCreatedDate(systemTime);
                        copy.setUpdatedDate(systemTime);
                        copy.setVersion(IntDef.INT_ONE);
                        baseDao.insert(copy);
                    } else {
                        TntPfcDetail detailWill = new TntPfcDetail();
                        BeanUtils.copyProperties(entity, detailWill);
                        detailWill.setPfcDetailId(detail.getPfcDetailId());
                        detailWill.setPfcId(dbMaster.getPfcId());
                        detailWill.setPartsId(StringUtil.toSafeInteger(entity.getRoute().getPartsId()));
                        detailWill.setSupplierId(StringUtil.toSafeInteger(entity.getRoute().getSupplierId()));
                        detailWill.setShippingRouteCode(entity.getRoute().getShippingRouteCode());
                        detailWill.setRedoShippingFlag(IntDef.INT_ZERO);
                        if (entity.getFcQty1() == null) {
                            detailWill.setFcQty1(detail.getFcQty1());
                        }
                        if (entity.getFcQty2() == null) {
                            detailWill.setFcQty2(detail.getFcQty2());
                        }
                        if (entity.getFcQty3() == null) {
                            detailWill.setFcQty3(detail.getFcQty3());
                        }
                        if (entity.getFcQty4() == null) {
                            detailWill.setFcQty4(detail.getFcQty4());
                        }
                        if (entity.getFcQty5() == null) {
                            detailWill.setFcQty5(detail.getFcQty5());
                        }
                        if (entity.getFcQty6() == null) {
                            detailWill.setFcQty6(detail.getFcQty6());
                        }
                        if (entity.getFcQty7() == null) {
                            detailWill.setFcQty7(detail.getFcQty7());
                        }
                        if (entity.getFcQty8() == null) {
                            detailWill.setFcQty8(detail.getFcQty8());
                        }
                        if (entity.getFcQty9() == null) {
                            detailWill.setFcQty9(detail.getFcQty9());
                        }
                        if (entity.getFcQty10() == null) {
                            detailWill.setFcQty10(detail.getFcQty10());
                        }
                        if (entity.getFcQty11() == null) {
                            detailWill.setFcQty11(detail.getFcQty11());
                        }
                        if (entity.getFcQty12() == null) {
                            detailWill.setFcQty12(detail.getFcQty12());
                        }
                        detailWill.setUpdatedBy(baseParam.getLoginUserId());
                        detailWill.setUpdatedDate(systemTime);
                        detailWill.setVersion(detail.getVersion() + 1);
                        detailWill.setCreatedBy(detail.getCreatedBy());
                        detailWill.setCreatedDate(detail.getCreatedDate());
                        BeanUtils.copyProperties(detailWill, detail);
                        baseDao.update(detail);
                    }
                }
            }
        }

        List<SupplyChainEntity> listSuppLast = new ArrayList<SupplyChainEntity>();
        List<TntPfcShipping> dbList = new ArrayList<TntPfcShipping>();
        for (int j = 1; j <= IntDef.INT_TWELVE; j++) {
            Map<String, List<CalculateDrEntity>> mapCalculate = new HashMap<String, List<CalculateDrEntity>>();
            Map<String, List<CPOOFF11Entity>> mapEntity = new HashMap<String, List<CPOOFF11Entity>>();
            for (CPOOFF11Entity entity : list) {

                if (getQty(entity, j) == null || entity.getRoute().getForecastNum() < j) {
                    continue;
                }
                BigDecimal qty = getQty(entity, j);

                CalculateDrEntity cal = new CalculateDrEntity();
                cal.setPart(StringUtil.toSafeInteger(entity.getRoute().getPartsId()));
                cal.setPartNo(entity.getRoute().getTtcPartsNo());
                cal.setSrbq(entity.getRoute().getSrbq());
                cal.setPartQty(qty);
                if (mapCalculate.containsKey(entity.getRoute().getCustomerId() + StringConst.COMMA
                        + entity.getRoute().getExpCalendarCode())) {
                    mapCalculate.get(
                        entity.getRoute().getCustomerId() + StringConst.COMMA + entity.getRoute().getExpCalendarCode())
                        .add(cal);
                    mapEntity.get(
                        entity.getRoute().getCustomerId() + StringConst.COMMA + entity.getRoute().getExpCalendarCode())
                        .add(entity);
                } else {
                    List<CalculateDrEntity> newList = new ArrayList<CalculateDrEntity>();
                    newList.add(cal);
                    mapCalculate.put(entity.getRoute().getCustomerId() + StringConst.COMMA
                            + entity.getRoute().getExpCalendarCode(), newList);

                    List<CPOOFF11Entity> newList1 = new ArrayList<CPOOFF11Entity>();
                    newList1.add(entity);
                    mapEntity.put(entity.getRoute().getCustomerId() + StringConst.COMMA
                            + entity.getRoute().getExpCalendarCode(), newList1);
                }
            }
            for (Map.Entry<String, List<CalculateDrEntity>> entry : mapCalculate.entrySet()) {
                BaseParam calParam = new BaseParam();
                Integer expCalCode = StringUtil.toSafeInteger(entry.getKey().split(StringConst.COMMA)[1]);
                String calendarCode = CodeCategoryManager.getCodeName(baseParam.getLanguage(),
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
                List<CalculateDrEntity> listCal = getCalendarList(calParam);
                Map<Integer, List<CalculateDrEntity>> listResult = service.calcDr(entry.getValue(), listCal,
                    CodeConst.OrderForecastType.DAILY);
                for (Map.Entry<Integer, List<CalculateDrEntity>> out : listResult.entrySet()) {

                    Calendar c = Calendar.getInstance();
                    c.setTime(orderMonthDate);
                    c.add(Calendar.MONTH, j);
                    String fcMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, c.getTime());

                    List<TntPfcMaster> checkList = getPfcMater(baseParam.getCurrentOfficeId(), orderMonth, mapGetEntity
                        .get(out.getKey()).getRoute().getCustomerId());
                    TntPfcDetail detail = null;
                    if (checkList.size() != 0) {
                        Integer pfcId = checkList.get(0).getPfcId();
                        String hql = "FROM TNT_PFC_DETAIL WHERE PFC_ID = ? AND PARTS_ID = ? AND SUPPLIER_ID = ?";
                        Object[] param = new Object[] { pfcId, out.getKey(),
                            mapGetEntity.get(out.getKey()).getRoute().getSupplierId() };
                        detail = baseDao.findOne(hql, param);
                    }
                    if (detail != null) {
                        String hql2 = "FROM TNT_PFC_SHIPPING WHERE PFC_DETAIL_ID = ? AND OFFICE_ID = ? AND BUSINESS_PATTERN = ? AND FC_MONTH = ? AND PARTS_ID = ? AND VALID_FLAG = ?";
                        Object[] param2 = new Object[] { detail.getPfcDetailId(), baseParam.getCurrentOfficeId(),
                            BusinessPattern.V_V, fcMonth, out.getKey(), CodeConst.ActiveFlag.Y };
                        List<TntPfcShipping> shippingDb = baseDao.select(hql2, param2);

                        for (TntPfcShipping tps : shippingDb) {
                            tps.setValidFlag(ActiveFlag.N);
                            baseDao.update(tps);
                        }
                    }
                    for (CalculateDrEntity cal : out.getValue()) {
                        SupplyChainEntity sce = new SupplyChainEntity();
                        sce.setTansportMode(TransportMode.SEA);
                        sce.setChainStartDate(cal.getCalendarDate());
                        sce.setExpPartsId(StringUtil.toSafeInteger(mapGetEntity.get(out.getKey()).getRoute().getExpPartsId()));
                        // pick up
                        SupplyChainEntity partInfo = new SupplyChainEntity();
                        partInfo.setTansportMode(TransportMode.SEA);
                        partInfo.setChainStartDate(cal.getCalendarDate());
                        partInfo.setExpPartsId(StringUtil.toSafeInteger(mapGetEntity.get(out.getKey()).getRoute()
                            .getExpPartsId()));
                        listSuppLast.add(partInfo);

                        TntPfcShipping shipping = new TntPfcShipping();
                        shipping.setBusinessPattern(BusinessPattern.V_V);

                        shipping.setOfficeId(baseParam.getCurrentOfficeId());
                        shipping.setFcMonth(fcMonth);
                        shipping.setPartsId(out.getKey());
                        shipping.setTransportMode(TransportMode.SEA);
                        shipping.setQty(cal.getDrQty());
                        shipping.setValidFlag(CodeConst.ActiveFlag.Y);
                        shipping.setCreatedBy(baseParam.getLoginUserId());
                        shipping.setUpdatedBy(baseParam.getLoginUserId());
                        shipping.setCreatedDate(systemTime);
                        shipping.setUpdatedDate(systemTime);
                        dbList.add(shipping);
                        if (detail != null) {
                            shipping.setPfcDetailId(detail.getPfcDetailId());
                        }
                    }
                }
            }
        }
        commonService.prepareSupplyChain(listSuppLast, ChainStep.FORECAST, BusinessPattern.V_V, false);
        Map<String, TntPfcShipping> shippings = new HashMap<String, TntPfcShipping>();
        for (int i = 0; i < listSuppLast.size(); i++) {
            TntPfcShipping shippingTo = dbList.get(i);
            if (shippings.containsKey(shippingTo.getPfcDetailId() + StringConst.COMMA
                    + listSuppLast.get(i).getEtd().getTime() + shippingTo.getFcMonth())) {
                TntPfcShipping s = shippings.get(shippingTo.getPfcDetailId() + StringConst.COMMA
                        + listSuppLast.get(i).getEtd().getTime() + shippingTo.getFcMonth());
                s.setQty(s.getQty().add(dbList.get(i).getQty()));
            } else {
                shippingTo.setPfcShippingId(null);
                shippingTo.setEta(listSuppLast.get(i).getEta());
                shippingTo.setEtd(listSuppLast.get(i).getEtd());
                shippingTo.setCcDate(listSuppLast.get(i).getImpPlanCustomDate());
                shippingTo.setImpInbPlanDate(listSuppLast.get(i).getImpPlanInboundDate());
                shippingTo.setVanningDate(listSuppLast.get(i).getVanningDate());
                shippingTo.setVersion(IntDef.INT_ONE);
                shippings.put(shippingTo.getPfcDetailId() + StringConst.COMMA + listSuppLast.get(i).getEtd().getTime()
                        + shippingTo.getFcMonth(), shippingTo);
            }
        }
        for (Map.Entry<String, TntPfcShipping> e : shippings.entrySet()) {
            baseDao.insert(e.getValue());
        }
        // split file
        @SuppressWarnings("resource")
        XSSFWorkbook newWorkbook = new XSSFWorkbook();

        Map<String, String> listWestCus = new HashMap<String, String>();
        for (CPOOFF11Entity entity : list) {
            if (!listWestCus.containsKey(entity.getRoute().getCustomerId())) {
                listWestCus.put(entity.getRoute().getCustomerId(), entity.getCustomerCode());
            }
        }

        for (Map.Entry<String, String> data : listWestCus.entrySet()) {
            String oldFileName = "temp.xlsx";
            String path = ConfigUtil.get(Properties.UPLOAD_PATH_PFC);
            // path = "E:/common/cfc";
            File newFile = new File(path, oldFileName);
            FileInputStream inputStream = new FileInputStream(newFile);
            newWorkbook = (XSSFWorkbook) ExcelUtil.getWorkBook(inputStream);
            Sheet newSheet = newWorkbook.getSheetAt(0);
            int maxRow = newSheet.getLastRowNum() + 1;

            // remove other customerCode
            for (int i = DETAIL_START_LINE; i <= newSheet.getLastRowNum() + 1; i++) {
                if (!StringUtil.isEmpty(PoiUtil.getStringCellValue(newSheet, i, IntDef.INT_THREE))
                        && !StringUtil.isEmpty(PoiUtil.getStringCellValue(newSheet, i, IntDef.INT_FIVE))) {
                    if (!data.getValue().equals(PoiUtil.getStringCellValue(newSheet, i, IntDef.INT_THREE))) {
                        // newSheet.shiftRows(i, i, -1);
                        if (newSheet.getRow(i - 1) != null) {
                            newSheet.removeRow(newSheet.getRow(i - 1));
                        }
                    }
                }
            }
            int j = IntDef.INT_EIGHT;
            for (int i = DETAIL_START_LINE; i <= maxRow; i++) {

                if (StringUtil.isEmpty(PoiUtil.getStringCellValue(newSheet, j, IntDef.INT_THREE))) {
                    newSheet.shiftRows(j, maxRow, -1);
                } else {
                    j++;
                }
            }
            // save file
            orderMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, orderMonthDate);
            List<TntPfcMaster> listMaster = getPfcMater(baseParam.getCurrentOfficeId(), orderMonth, data.getKey());

            String fileName = listMaster.get(0).getPfcNo() + ".xlsx";
            newFile = new File(path);
            if (!newFile.exists()) {
                newFile.mkdirs();
                newFile = new File(path, fileName);
            } else {
                newFile = new File(path, fileName);
            }
            OutputStream outputStream = null;
            outputStream = new FileOutputStream(newFile);
            newWorkbook.write(outputStream);
            outputStream.close();
        }

    }

    /**
     * getPfcMater
     * 
     * @param officeId officeId
     * @param orderMonth orderMonth
     * @param customerId customerId
     * @return List<TntPfcMaster>
     */
    public List<TntPfcMaster> getPfcMater(Integer officeId, String orderMonth, String customerId) {
//        String hql = "FROM TNT_PFC_MASTER WHERE officeId = ? AND orderMonth = ? AND customerId = ? AND reviseVersion = ?";
//        Object[] param = new Object[] { officeId, orderMonth, StringUtil.toInteger(customerId), 0 };
//        List<TntPfcMaster> checkList = baseDao.select(hql, param);
        
        TntPfcMaster conditon = new TntPfcMaster();
        conditon.setOfficeId(officeId);
        conditon.setOrderMonth(orderMonth);
        conditon.setCustomerId(StringUtil.toInteger(customerId));
        conditon.setReviseVersion(0);
        List<TntPfcMaster> checkList = baseDao.select(conditon);
        
//        BaseParam param = new BaseParam();
//        param.getSwapData().put("officeId", officeId);
//        param.getSwapData().put("orderMonth", orderMonth);
//        param.getSwapData().put("customerId", customerId);
//        param.getSwapData().put("reviseVersion", 0);
//        return baseMapper.select(getSqlId("getMaster"), param);
        return checkList;
    }

    /**
     * getQty
     * 
     * @param entity entity
     * @param j j
     * @return BigDecimal
     */
    private BigDecimal getQty(CPOOFF11Entity entity, int j) {
        BigDecimal qty = new BigDecimal(0);
        switch (j) {
            case IntDef.INT_ONE:
                qty = entity.getFcQty1();
                break;
            case IntDef.INT_TWO:
                qty = entity.getFcQty2();
                break;
            case IntDef.INT_THREE:
                qty = entity.getFcQty3();
                break;
            case IntDef.INT_FOUR:
                qty = entity.getFcQty4();
                break;
            case IntDef.INT_FIVE:
                qty = entity.getFcQty5();
                break;
            case IntDef.INT_SIX:
                qty = entity.getFcQty6();
                break;
            case IntDef.INT_SEVEN:
                qty = entity.getFcQty7();
                break;
            case IntDef.INT_EIGHT:
                qty = entity.getFcQty8();
                break;
            case IntDef.INT_NINE:
                qty = entity.getFcQty9();
                break;
            case IntDef.INT_TEN:
                qty = entity.getFcQty10();
                break;
            case IntDef.INT_ELEVEN:
                qty = entity.getFcQty11();
                break;
            case IntDef.INT_TWELVE:
                qty = entity.getFcQty12();
                break;
        }
        return qty;
    }

    /**
     * getMainRouteDetail
     * 
     * @param routeList routeList
     */
    public void getMainRouteDetail(List<MainRouteEntity> routeList) {
        for (int i = 0; i < routeList.size(); i++) {

            routeList.get(i).setCustomerCode(
                baseDao.findOne(TnmCustomer.class, StringUtil.toSafeInteger(routeList.get(i).getCustomerId()))
                    .getCustomerCode());
            routeList.get(i).setSupplierCode(
                baseDao.findOne(TnmSupplier.class, StringUtil.toSafeInteger(routeList.get(i).getSupplierId()))
                    .getSupplierCode());

            if (i == IntDef.INT_ZERO) {
                routeList.get(i).setShowFlag(IntDef.INT_ZERO);
                continue;
            }
            MainRouteEntity route = routeList.get(i - 1);
            if (route.getWestCustCode().equals(routeList.get(i).getWestCustCode())
                    && route.getWestPartsNo().equals(routeList.get(i).getWestPartsNo())) {
                routeList.get(i).setShowFlag(IntDef.INT_ONE);
            } else {
                routeList.get(i).setShowFlag(IntDef.INT_ZERO);
            }

        }
    }

    /**
     * Date[]
     * 
     * @param list list
     * @param orderMonthDate orderMonthDate
     * @param firstDate firstDate
     * @param lastDate lastDate
     * @return Date[]
     */
    public Date[] getFirstAndLast(List<CPOOFF11Entity> list, Date orderMonthDate, Date firstDate, Date lastDate) {
        for (CPOOFF11Entity entity : list) {
            int count = 0;
            for (int j = 1; j <= entity.getRoute().getForecastNum(); j++) {
                BigDecimal qty = null;
                switch (j) {
                    case IntDef.INT_ONE:
                        qty = entity.getFcQty1();
                        break;
                    case IntDef.INT_TWO:
                        qty = entity.getFcQty2();
                        break;
                    case IntDef.INT_THREE:
                        qty = entity.getFcQty3();
                        break;
                    case IntDef.INT_FOUR:
                        qty = entity.getFcQty4();
                        break;
                    case IntDef.INT_FIVE:
                        qty = entity.getFcQty5();
                        break;
                    case IntDef.INT_SIX:
                        qty = entity.getFcQty6();
                        break;
                    case IntDef.INT_SEVEN:
                        qty = entity.getFcQty7();
                        break;
                    case IntDef.INT_EIGHT:
                        qty = entity.getFcQty8();
                        break;
                    case IntDef.INT_NINE:
                        qty = entity.getFcQty9();
                        break;
                    case IntDef.INT_TEN:
                        qty = entity.getFcQty10();
                        break;
                    case IntDef.INT_ELEVEN:
                        qty = entity.getFcQty11();
                        break;
                    case IntDef.INT_TWELVE:
                        qty = entity.getFcQty12();
                        break;
                }
                if (qty != null && !DecimalUtil.isEquals(qty, BigDecimal.ZERO)) {
                    count++;
                }
            }
            Calendar c = Calendar.getInstance();
            c.setTime(orderMonthDate);
            c.add(Calendar.MONTH, count + entity.getRoute().getTargetMonth());
            Date getLastDate = c.getTime();
            if (lastDate != null && lastDate.getTime() < getLastDate.getTime()) {
                lastDate = getLastDate;
            } else if (lastDate == null) {
                lastDate = getLastDate;
            }
            c.setTime(orderMonthDate);
            c.add(Calendar.MONTH, IntDef.INT_ONE + entity.getRoute().getTargetMonth());

            Date getFirstDate = c.getTime();
            if (firstDate != null && firstDate.getTime() > getFirstDate.getTime()) {
                firstDate = getFirstDate;
            } else if (firstDate == null) {
                firstDate = getFirstDate;
            }
        }
        return new Date[] { firstDate, lastDate };

    }

    /**
     * getFirstAndLastForcast
     * 
     * @param list list
     * @param orderMonthDate orderMonthDate
     * @param firstDate firstDate
     * @param lastDate lastDate
     * @return Date[]
     */
    public Date[] getFirstAndLastForcast(List<CPOOFF11Entity> list, Date orderMonthDate, Date firstDate, Date lastDate) {
        Date a = null;
        Date b = null;
        for (CPOOFF11Entity entity : list) {
            int count = 0;
            for (int j = 1; j <= entity.getRoute().getForecastNum(); j++) {
                BigDecimal qty = null;
                switch (j) {
                    case IntDef.INT_ONE:
                        qty = entity.getFcQty1();
                        break;
                    case IntDef.INT_TWO:
                        qty = entity.getFcQty2();
                        break;
                    case IntDef.INT_THREE:
                        qty = entity.getFcQty3();
                        break;
                    case IntDef.INT_FOUR:
                        qty = entity.getFcQty4();
                        break;
                    case IntDef.INT_FIVE:
                        qty = entity.getFcQty5();
                        break;
                    case IntDef.INT_SIX:
                        qty = entity.getFcQty6();
                        break;
                    case IntDef.INT_SEVEN:
                        qty = entity.getFcQty7();
                        break;
                    case IntDef.INT_EIGHT:
                        qty = entity.getFcQty8();
                        break;
                    case IntDef.INT_NINE:
                        qty = entity.getFcQty9();
                        break;
                    case IntDef.INT_TEN:
                        qty = entity.getFcQty10();
                        break;
                    case IntDef.INT_ELEVEN:
                        qty = entity.getFcQty11();
                        break;
                    case IntDef.INT_TWELVE:
                        qty = entity.getFcQty12();
                        break;
                }
                if (qty != null && !DecimalUtil.isEquals(qty, BigDecimal.ZERO)) {
                    count++;
                }
            }
            Calendar c = Calendar.getInstance();
            c.setTime(orderMonthDate);
            c.add(Calendar.MONTH, count);
            Date getLastDate = c.getTime();
            if (lastDate != null && lastDate.getTime() < getLastDate.getTime()) {
                b = getLastDate;
            } else if (lastDate == null) {
                b = getLastDate;
            }
            c.setTime(orderMonthDate);
            c.add(Calendar.MONTH, IntDef.INT_ONE);

            Date getFirstDate = c.getTime();
            if (firstDate != null && firstDate.getTime() > getFirstDate.getTime()) {
                a = getFirstDate;
            } else if (firstDate == null) {
                a = getFirstDate;
            }
        }
        return new Date[] { a, b };

    }

    /**
     * getCalendarList
     * 
     * @param calParam calParam
     * @return List<CalculateDrEntity>
     */
    public List<CalculateDrEntity> getCalendarList(BaseParam calParam) {
        return baseMapper.select(getSqlId("getCalendarList"), calParam);
    }

    /**
     * checkShippingRoute
     * 
     * @param calParam calParam
     * @return int
     */
    public int checkShippingRoute(BaseParam calParam) {
        return baseMapper.count(getSqlId("checkShippingRoute"), calParam);
    }

}
