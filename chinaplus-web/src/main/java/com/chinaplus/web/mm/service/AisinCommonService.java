/**
 * AisinCommonService.java
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.CalendarParty;
import com.chinaplus.common.consts.CodeConst.ShippingRouteType;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.UploadException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.AisinCommonEntity;
import com.chinaplus.web.mm.entity.CalendarDateEntity;

/**
 * AisinCommonService.
 */
/**
 * AisinCommonService.
 */
@Service
public class AisinCommonService extends BaseService {

    /**
     * deal tianjing and shanghai
     * 
     * @param modChangeLists modChangeLists
     * @param minFirstEtd minFirstEtd
     * @param maxLastEtd maxLastEtd
     * @param type type
     * @throws UploadException UploadException
     * @return dataAllList dataAllList
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> dealUpdateData(List<AisinCommonEntity> modChangeLists, long minFirstEtd,
        long maxLastEtd, int type) throws UploadException {

        Map<String, Object> maps = new HashMap<String, Object>();

        BaseParam param = new BaseParam();
        param.setSwapData("modlists", modChangeLists);
        param.setSwapData("type", type);
        List<AisinCommonEntity> modDetaliList = baseMapper.select(this.getSqlId("getModList"), param);

        Map<String, Object> modDetalMaps = new HashMap<String, Object>();
        for (AisinCommonEntity ace : modDetaliList) {

            String shippingRouteCode = ace.getShippingRouteCode();
            List<AisinCommonEntity> strList = (List<AisinCommonEntity>) modDetalMaps.get(shippingRouteCode);
            if (strList != null && strList.size() > 0) {
                strList.add(ace);
                modDetalMaps.put(shippingRouteCode, strList);
            } else {
                List<AisinCommonEntity> strLists = new ArrayList<AisinCommonEntity>();
                strLists.add(ace);
                modDetalMaps.put(shippingRouteCode, strLists);
            }
        }

        List<AisinCommonEntity> modList = new ArrayList<AisinCommonEntity>();
        for (AisinCommonEntity ace : modChangeLists) {
            List<AisinCommonEntity> strList = (List<AisinCommonEntity>) modDetalMaps.get(ace.getShippingRouteCode());
            if (strList != null && strList.size() > 0) {
                for (AisinCommonEntity str : strList) {
                    AisinCommonEntity preAce = ace.cloneBean();
                    preAce.setSupplierId(str.getSupplierId());
                    preAce.setSupplierCode(str.getSupplierCode());
                    modList.add(preAce);
                }
            }
        }
        maps = dealCommomList(modList, minFirstEtd, maxLastEtd, type);
        return maps;
    }

    /**
     * deal commom
     * 
     * @param modList modList
     * @param minFirstEtd minFirstEtd
     * @param maxLastEtd maxLastEtd
     * @param type type
     * @return maps maps
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> dealCommomList(List<AisinCommonEntity> modList, long minFirstEtd, long maxLastEtd,
        int type) {

        Map<String, Object> maps = new HashMap<String, Object>();
        Map<String, String> msgCalMaps = new HashMap<String, String>();
        Map<String, String> msgVanMaps = new HashMap<String, String>();

        if (modList != null && modList.size() > 0) {

            BaseParam param = new BaseParam();
            // query CALENDAR_DATE
            param.setSwapData("dataAllList", modList);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String maxYear = sdf.format(maxLastEtd);
            param.setSwapData("maxYear", maxYear);
            String minYear = sdf.format(minFirstEtd);
            param.setSwapData("minYear", StringUtil.toSafeString(StringUtil.toSafeInteger(minYear).intValue() - 1));
            param.setSwapData("partyType", CalendarParty.SUPPLIER);
            // get supplier calendar info
            List<CalendarDateEntity> newCalendarDateList = baseMapper.select(this.getSqlId("getNewCalendarDateList"),
                param);
            List<CalendarDateEntity> calendarDateList = baseMapper.select(this.getSqlId("getCalendarDateList"), param);

            Map<String, Date> cdateCheckMaps = new HashMap<String, Date>();
            if (newCalendarDateList != null && newCalendarDateList.size() > 0) {

                for (CalendarDateEntity cde : calendarDateList) {

                    Date calendarDate = cde.getCalendarDate();
                    Integer supplierId = cde.getSupplierId();
                    Integer officeId = cde.getOfficeId();
                    String key = officeId + StringConst.COMMA + supplierId;
                    cdateCheckMaps.put(key, calendarDate);
                }

                for (AisinCommonEntity ace : modList) {

                    String checkKey = ace.getOfficeId() + StringConst.COMMA + ace.getSupplierId();
                    if (null == cdateCheckMaps.get(checkKey)) {

                        String key = ace.getShippingRouteCode() + StringConst.COMMA + ace.getSupplierCode();
                        msgCalMaps.put(key, ace.getShippingRouteCode());
                    }
                }
                if (msgCalMaps.isEmpty()) {

                    Map<String, List<Date>> cdateMaps = new HashMap<String, List<Date>>();
                    if (calendarDateList != null && calendarDateList.size() > 0) {

                        List<Date> calLst = null;
                        for (CalendarDateEntity cde : calendarDateList) {

                            Date calendarDate = cde.getCalendarDate();
                            Integer supplierId = cde.getSupplierId();
                            Integer officeId = cde.getOfficeId();
                            String key = officeId + StringConst.COMMA + supplierId;
                            if (null != cdateMaps.get(key)) {

                                calLst.add(calendarDate);
                                cdateMaps.put(key, calLst);
                            } else {

                                calLst = new ArrayList<>();
                                calLst.add(calendarDate);
                                cdateMaps.put(key, calLst);
                            }
                        }
                        // deal tian jing change list
                        if (type == ShippingRouteType.AISIN_TTTJ) {
                            maps = dealTJModList(modList, cdateMaps);
                        }
                        // deal shang hai change list
                        else if (type == ShippingRouteType.AISIN_TTSH) {
                            maps = dealSHModList(modList, cdateMaps);
                        }
                        msgCalMaps = (Map<String, String>) maps.get("msgCalMaps");
                        msgVanMaps = (Map<String, String>) maps.get("msgVanMaps");
                    } else {

                        for (AisinCommonEntity ace : modList) {

                            String key = ace.getShippingRouteCode() + StringConst.COMMA + ace.getSupplierCode();
                            msgCalMaps.put(key, ace.getShippingRouteCode());
                        }
                    }
                }
            } else {

                for (AisinCommonEntity ace : modList) {

                    String key = ace.getShippingRouteCode() + StringConst.COMMA + ace.getSupplierCode();
                    msgCalMaps.put(key, ace.getShippingRouteCode());
                }

            }

            List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
            if (null != msgCalMaps && !msgCalMaps.isEmpty()) {

                for (Map.Entry<String, String> entry : msgCalMaps.entrySet()) {

                    String key = entry.getKey();
                    String[] idCode = key.split(StringConst.COMMA);
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_143);
                    message.setMessageArgs(new String[] { idCode[0], idCode[1] });
                    messageLists.add(message);
                }
            }
            if (null != msgVanMaps && !msgVanMaps.isEmpty()) {

                for (Map.Entry<String, String> entry : msgVanMaps.entrySet()) {

                    String key = entry.getKey();
                    String[] idCode = key.split(StringConst.COMMA);
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_178);
                    message.setMessageArgs(new String[] { idCode[0], idCode[1] });
                    messageLists.add(message);
                }
            }
            maps.put("modChangeMessageList", messageLists);
        }
        return maps;
    }

    /**
     * deal shang hai change list
     * 
     * @param modList modList
     * @param cdateMaps cdateMaps
     * @return maps maps
     */
    protected Map<String, Object> dealSHModList(List<AisinCommonEntity> modList, Map<String, List<Date>> cdateMaps) {

        Map<String, Object> maps = new HashMap<String, Object>();
        // no supplier alendar w1004_143
        Map<String, String> msgCalMaps = new HashMap<String, String>();
        // no vanningDay w1004_178
        Map<String, String> msgVanMaps = new HashMap<String, String>();
        List<AisinCommonEntity> rstLst = new ArrayList<AisinCommonEntity>();

        BaseParam param = new BaseParam();
        param.setSwapData("modlists", modList);
        // get shippingRouteCode,firstEtd,lastEtd,deliveryStartDate by shippingRouteCode,firstEtd,lastEtd
        List<AisinCommonEntity> modSFLDlList = baseMapper.select(this.getSqlId("getModSFLDList"), param);

        Map<String, List<Date>> sfldMaps = new HashMap<String, List<Date>>();
        Map<String, AisinCommonEntity> etdMaps = new HashMap<String, AisinCommonEntity>();
        Map<String, Integer> sridMaps = new HashMap<String, Integer>();
        for (AisinCommonEntity ace : modSFLDlList) {

            String delStDatekey = ace.getShippingRouteCode() + StringConst.COMMA + ace.getFirstEtd()
                    + StringConst.COMMA + ace.getLastEtd();
            List<Date> datelists = sfldMaps.get(delStDatekey);

            if (null != datelists && !datelists.isEmpty()) {

                if (!datelists.contains(ace.getDeliveryStartDate())) {

                    datelists.add(ace.getDeliveryStartDate());
                    sfldMaps.put(delStDatekey, datelists);
                }
            } else {

                datelists = new ArrayList<Date>();
                datelists.add(ace.getDeliveryStartDate());
                sfldMaps.put(delStDatekey, datelists);
            }

            // set ETD Info
            StringBuffer etdKey = new StringBuffer();
            etdKey.append(ace.getShippingRouteCode()).append(StringConst.COMMA);
            etdKey.append(ace.getFirstEtd()).append(StringConst.COMMA);
            etdKey.append(ace.getLastEtd()).append(StringConst.COMMA);
            etdKey.append(ace.getDeliveryStartDate()).append(StringConst.COMMA);
            etdKey.append(ace.getVanningDay());
            etdMaps.put(etdKey.toString(), ace);

            // set SRID Info
            StringBuffer sridKey = new StringBuffer();
            sridKey.append(ace.getShippingRouteCode()).append(StringConst.COMMA);
            sridKey.append(ace.getFirstEtd()).append(StringConst.COMMA);
            sridKey.append(ace.getLastEtd()).append(StringConst.COMMA);
            sridKey.append(ace.getDeliveryStartDate());
            sridMaps.put(sridKey.toString(), ace.getSrId());

        }
        List<AisinCommonEntity> objLoopLst = new ArrayList<AisinCommonEntity>();
        Map<String, AisinCommonEntity> diffDelStDateMaps = new HashMap<String, AisinCommonEntity>();
        List<AisinCommonEntity> newdelStDateLst = new ArrayList<AisinCommonEntity>();
        // get all deliveryStartDate
        for (int i = 0; i < modList.size(); i++) {

            AisinCommonEntity ace = modList.get(i);
            String delStDatekey = ace.getShippingRouteCode() + StringConst.COMMA + ace.getFirstEtd()
                    + StringConst.COMMA + ace.getLastEtd();
            List<Date> delStDateLst = sfldMaps.get(delStDatekey);

            Date deliveryStartDate = ace.getDeliveryStartDate();
            // exist add new deliveryStartDate
            if (null != delStDateLst && !delStDateLst.isEmpty()) {

                if (!delStDateLst.contains(deliveryStartDate)) {

                    newdelStDateLst.add(ace);
                    delStDateLst.add(deliveryStartDate);
                    Collections.sort(delStDateLst, new Comparator<Date>() {
                        @Override
                        public int compare(Date obj1, Date obj2) {

                            int result = DateTimeUtil.getDayDifferent(obj2, obj1).intValue();
                            return result;
                        }
                    });
                    sfldMaps.put(delStDatekey, delStDateLst);
                }
            } else {

                newdelStDateLst.add(ace);
                delStDateLst = new ArrayList<Date>();
                delStDateLst.add(ace.getDeliveryStartDate());
                sfldMaps.put(delStDatekey, delStDateLst);
            }

            // add ETD info
            StringBuffer etdKey = new StringBuffer();
            etdKey.append(ace.getShippingRouteCode());
            etdKey.append(StringConst.COMMA).append(ace.getFirstEtd());
            etdKey.append(StringConst.COMMA).append(ace.getLastEtd());
            etdKey.append(StringConst.COMMA).append(ace.getDeliveryStartDate());
            etdKey.append(StringConst.COMMA).append(ace.getVanningDay());
            etdMaps.put(etdKey.toString(), ace);

            // get diffDelStDate info (loop object)
            StringBuffer diffDelStDateKey = new StringBuffer();
            diffDelStDateKey.append(ace.getShippingRouteCode()).append(StringConst.COMMA);
            diffDelStDateKey.append(ace.getFirstEtd()).append(StringConst.COMMA);
            diffDelStDateKey.append(ace.getLastEtd()).append(StringConst.COMMA);
            diffDelStDateKey.append(ace.getDeliveryStartDate()).append(StringConst.COMMA);
            diffDelStDateKey.append(ace.getSupplierId());

            if (!diffDelStDateMaps.containsKey(diffDelStDateKey.toString())) {

                diffDelStDateMaps.put(diffDelStDateKey.toString(), ace);
                objLoopLst.add(ace);
            }
        }

        // get related SR_Detail info
        for (int i = 0; i < newdelStDateLst.size(); i++) {

            AisinCommonEntity ace = newdelStDateLst.get(i);

            String DelStDatekey = ace.getShippingRouteCode() + StringConst.COMMA + ace.getFirstEtd()
                    + StringConst.COMMA + ace.getLastEtd();
            List<Date> delStDateLst = sfldMaps.get(DelStDatekey);

            int pos = delStDateLst.indexOf(ace.getDeliveryStartDate());
            if (pos > 0) {

                String preKey = DelStDatekey + StringConst.COMMA + delStDateLst.get(pos - 1) + StringConst.COMMA
                        + ace.getSupplierId();
                if (!diffDelStDateMaps.containsKey(preKey)) {

                    AisinCommonEntity preAce = ace.cloneBean();
                    preAce.setDeliveryStartDate(delStDateLst.get(pos - 1));

                    // set SRID
                    StringBuffer sridKey = new StringBuffer();
                    sridKey.append(preAce.getShippingRouteCode()).append(StringConst.COMMA);
                    sridKey.append(preAce.getFirstEtd()).append(StringConst.COMMA);
                    sridKey.append(preAce.getLastEtd()).append(StringConst.COMMA);
                    sridKey.append(preAce.getDeliveryStartDate());
                    preAce.setSrId(sridMaps.get(sridKey.toString()));
                    objLoopLst.add(preAce);

                    diffDelStDateMaps.put(preKey, preAce);
                }
            }
        }

        for (int i = 0; i < objLoopLst.size(); i++) {

            AisinCommonEntity ace = objLoopLst.get(i);

            Date deliveryStartDate = ace.getDeliveryStartDate();
            Date lastEtd = ace.getLastEtd();

            String DelStDatekey = ace.getShippingRouteCode() + StringConst.COMMA + ace.getFirstEtd()
                    + StringConst.COMMA + ace.getLastEtd();
            List<Date> delStDateLst = sfldMaps.get(DelStDatekey);
            int pos = delStDateLst.indexOf(deliveryStartDate);
            // next deliveryStartDate(end date)
            Date nextDelStDate = null;
            if (pos != -1 && pos < delStDateLst.size() - 1) {
                nextDelStDate = delStDateLst.get(pos + 1);
            } else {
                nextDelStDate = lastEtd;
            }
            long dayCnt = DateTimeUtil.getDayDifferent(deliveryStartDate, nextDelStDate);

            Integer supplierId = ace.getSupplierId();
            Integer officeId = ace.getOfficeId();
            String calKey = officeId + StringConst.COMMA + supplierId;
            List<Date> workingCalLst = cdateMaps.get(calKey);

            if (null == workingCalLst || workingCalLst.isEmpty()) {

                String shippingRouteCode = ace.getShippingRouteCode();
                String supplierCode = ace.getSupplierCode();
                String key = shippingRouteCode + StringConst.COMMA + supplierCode;
                if (!msgCalMaps.containsKey(key)) {
                    msgCalMaps.put(key, shippingRouteCode);
                }
            } else {

                // van lead time
                Integer expVanningLeadtime = ace.getExpVanningLeadtime();
                int vanleadTimeCnt = 0;
                // Vanning Date
                Date vanningDate = null;
                Date deliveryToObuDate = null;
                Integer vday = null;

                for (int j = 0; j < dayCnt; j++) {

                    deliveryToObuDate = getAfterDate(deliveryStartDate, vanleadTimeCnt);
                    if (vanleadTimeCnt % expVanningLeadtime == 0) {

                        for (int vanLdCnt = expVanningLeadtime; vanLdCnt >= 0; vanLdCnt--) {

                            // Vanning Date
                            vanningDate = getAfterDate(deliveryToObuDate, vanLdCnt);
                            if (workingCalLst.contains(vanningDate)) {
                                break;
                            }
                        }

                        vday = getWhichDay(vanningDate);
                    }

                    // check whether deliveryToObuDate is a working day
                    if (workingCalLst.contains(deliveryToObuDate)) {

                        // Deliver Date to Obu Start Date
                        AisinCommonEntity rstAce = ace.cloneBean();
                        rstAce.setDeliveryToObuDate(deliveryToObuDate);
                        // set expInboundDate
                        rstAce.setExpInboundDate(deliveryToObuDate);

                        // Kanban Issue Plan Date
                        Integer workingDays = ace.getWorkingDays();
                        Date kanbanIssueDate = getBeforeWKDate(deliveryToObuDate, workingDays, cdateMaps, supplierId,
                            officeId);
                        if (kanbanIssueDate == null) {
                            String shippingRouteCode = ace.getShippingRouteCode();
                            String supplierCode = ace.getSupplierCode();
                            String key = shippingRouteCode + StringConst.COMMA + supplierCode;
                            if (!msgCalMaps.containsKey(key)) {
                                msgCalMaps.put(key, shippingRouteCode);
                            }
                        }
                        rstAce.setKanbanIssueDate(kanbanIssueDate);

                        // Vanning Date
                        rstAce.setLastVanning(vanningDate);

                        // calculation ETD
                        StringBuffer etdKey = new StringBuffer();
                        etdKey.append(ace.getShippingRouteCode()).append(StringConst.COMMA);
                        etdKey.append(ace.getFirstEtd()).append(StringConst.COMMA);
                        etdKey.append(ace.getLastEtd()).append(StringConst.COMMA);
                        etdKey.append(ace.getDeliveryStartDate()).append(StringConst.COMMA);
                        etdKey.append(vday);

                        if (etdMaps.containsKey(etdKey.toString())) {

                            AisinCommonEntity etdEntity = etdMaps.get(etdKey.toString());
                            rstAce.setSrId(etdEntity.getSrId());
                            int period = (etdEntity.getEtdWeek() - 1) * 7 + (etdEntity.getEtdDate() - vday);
                            // ETD
                            Date etd = getAfterDate(vanningDate, period);
                            if (!etd.after(etdEntity.getLastEtd())) {
                                rstAce.setEtd(etd);

                                // ETA
                                Integer deliveryLeadtime = etdEntity.getDeliveryLeadtime();
                                Date eta = getAfterDate(etd, deliveryLeadtime);
                                rstAce.setEta(eta);

                                rstLst.add(rstAce);
                            } else {
                                break;
                            }

                        } else {
                            // no registered vanningDate.
                            /*
                             * String key = ace.getShippingRouteCode() + StringConst.COMMA + vday;
                             * if (!msgVanMaps.containsKey(key)) {
                             * msgVanMaps.put(key, key);
                             * }
                             */
                        }
                    }

                    vanleadTimeCnt++;
                }
            }
        }

        maps.put("modChangeDetailList", rstLst);
        maps.put("msgCalMaps", msgCalMaps);
        maps.put("msgVanMaps", msgVanMaps);
        return maps;
    }

    /**
     * deal tian jing change list
     * 
     * @param modList modList
     * @param cdateMaps cdateMaps
     * @return maps maps
     */
    protected Map<String, Object> dealTJModList(List<AisinCommonEntity> modList, Map<String, List<Date>> cdateMaps) {

        Map<String, Object> maps = new HashMap<String, Object>();

        Map<String, String> msgMaps = new HashMap<String, String>();

        List<AisinCommonEntity> dataAllList = new ArrayList<AisinCommonEntity>();
        // get detail Etd ,VanningDay, EtdWeek
        for (AisinCommonEntity cf : modList) {
            // get effective etd date list
            List<AisinCommonEntity> dataList = getAllEffEtdDate(cf);
            if (dataList != null && dataList.size() > 0) {
                dataAllList.addAll(dataList);
            }
        }

        List<AisinCommonEntity> dataResultList = new ArrayList<AisinCommonEntity>();
        if (dataAllList != null && dataAllList.size() > 0) {
            for (AisinCommonEntity ace : dataAllList) {

                Integer supplierId = ace.getSupplierId();
                Integer officeId = ace.getOfficeId();

                // set datail lastVanning
                Date etd = ace.getEtd();
                // get which day
                Integer whichDay = getWhichDay(etd);
                int period = (ace.getEtdWeek() - 1) * 7 + (whichDay - ace.getVanningDay());
                Date lastVanning = DateTimeUtil.addDay(etd, -period);
                String calKey = officeId + StringConst.COMMA + supplierId;
                boolean flag = false;
                if (null != cdateMaps && !cdateMaps.isEmpty()) {

                    List<Date> calLst = cdateMaps.get(calKey);
                    if (null != calLst && !calLst.isEmpty()) {

                        if (!calLst.contains(lastVanning)) {
                            continue;
                        }
                    } else {
                        flag = true;
                    }
                } else {
                    flag = true;
                }
                ace.setLastVanning(lastVanning);

                // set datail expInboundDate
                String shippingRouteCode = ace.getShippingRouteCode();
                String supplierCode = ace.getSupplierCode();
                Integer expVanningLeadtime = ace.getExpVanningLeadtime();
                Date expInboundDate = getBeforeWKDate(lastVanning, expVanningLeadtime, cdateMaps, supplierId, officeId);
                if (expInboundDate == null) {
                    flag = true;
                } else {
                    ace.setExpInboundDate(expInboundDate);
                }

                // set datail kanbanIssueDate
                Integer workingDays = ace.getWorkingDays();
                Date kanbanIssueDate = getBeforeWKDate(expInboundDate, workingDays, cdateMaps, supplierId, officeId);
                if (kanbanIssueDate == null) {
                    flag = true;
                } else {
                    ace.setKanbanIssueDate(kanbanIssueDate);
                }

                // set detail eta
                Integer deliveryLeadtime = ace.getDeliveryLeadtime();
                Date eta = DateTimeUtil.addDay(etd, deliveryLeadtime);
                ace.setEta(eta);

                if (flag) {
                    String key = shippingRouteCode + StringConst.COMMA + supplierCode;
                    msgMaps.put(key, shippingRouteCode);
                } else {
                    dataResultList.add(ace);
                }
            }

            maps.put("modChangeDetailList", dataResultList);
            maps.put("msgCalMaps", msgMaps);
        }
        return maps;
    }

    /**
     * get After Date
     * 
     * @param initDate initDate
     * @param period period
     * @return Date Date
     */
    public Date getAfterDate(Date initDate, int period) {
        Calendar c = Calendar.getInstance();
        c.setTime(initDate);
        c.add(Calendar.DAY_OF_YEAR, period);
        return c.getTime();
    }

    /**
     * get before WorkingDay Day.
     * 
     * @param initDate initDate
     * @param period period
     * @param calMaps calendar info
     * @param supplierId supplierId
     * @param officeId officeId
     * @return Date Date
     */
    private Date getBeforeWKDate(Date initDate, int period, Map<String, List<Date>> calMaps, Integer supplierId,
        Integer officeId) {

        if (null != initDate && null != calMaps && !calMaps.isEmpty()) {

            String key = officeId + StringConst.COMMA + supplierId;
            List<Date> calLst = calMaps.get(key);
            if (null != calLst && !calLst.isEmpty()) {
                Calendar c = Calendar.getInstance();
                int cnt = 0;
                for (int i = 1; i < period * 10; i++) {

                    c.setTime(initDate);
                    c.add(Calendar.DAY_OF_YEAR, -i);
                    if (calLst.contains(c.getTime())) {
                        cnt++;
                    }
                    if (cnt == period) {
                        break;
                    }
                }
                if (cnt == 0) {
                    return null;
                }
                return c.getTime();
            }
        }
        return null;

    }

    /**
     * calculation all effective ETD.
     * 
     * @param aisinCommonEntity aisinCommonEntity
     * @return dataList dataList
     * @throws UploadException UploadException
     * 
     */
    private List<AisinCommonEntity> getAllEffEtdDate(AisinCommonEntity aisinCommonEntity) throws UploadException {

        List<AisinCommonEntity> dataList = new ArrayList<AisinCommonEntity>();

        Date firstEtd = aisinCommonEntity.getFirstEtd();
        Date lastEtd = aisinCommonEntity.getLastEtd();
        Integer etdDate = aisinCommonEntity.getEtdDate();

        // get which day
        Integer whichDay = getWhichDay(firstEtd);

        long periodDay = DateTimeUtil.getDayDifferent(firstEtd, lastEtd);
        long periodWeek = periodDay / 7 + 1;

        int etdDateInt = 0;
        if (whichDay <= etdDate) {
            etdDateInt += (etdDate - whichDay);
        } else {
            etdDateInt += (etdDate + 7 - whichDay);
        }

        for (int i = 0; i < periodWeek; i++) {

            Calendar c = Calendar.getInstance();
            c.setTime(firstEtd);
            c.add(Calendar.DAY_OF_YEAR, etdDateInt + 7 * i);
            Date etdDateDate = c.getTime();

            if (etdDateDate.getTime() <= lastEtd.getTime()) {

                AisinCommonEntity ace = aisinCommonEntity.cloneBean();
                if (ace != null) {
                    ace.setEtd(etdDateDate);
                    dataList.add(ace);
                }
            } else {
                break;
            }
        }
        return dataList;
    }

    /**
     * get which day
     * 
     * @param date date
     * @return Integer Integer
     * @throws UploadException UploadException
     */
    public Integer getWhichDay(Date date) throws UploadException {

        Calendar calendar = Calendar.getInstance();
        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
        if (date != null) {
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK);
        // if the first day is sunday
        if (isFirstSunday) {
            w = w - 1;
        }
        if (w == 0) {
            w = 7;
        }
        return w;
    }

    /**
     * deal aisin from TNM_EXP_PARTS
     * 
     * @param datalists datalists
     * @throws UploadException UploadException
     * @return maps maps
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> dealUpdateDataFromParts(List<AisinCommonEntity> datalists) throws UploadException {
        Map<String, Object> maps = new HashMap<String, Object>();
        if (datalists != null && datalists.size() > 0) {
            Map<String, Object> mapsTJ = dealUpdateDataFromParts(datalists, ShippingRouteType.AISIN_TTTJ);
            Map<String, Object> mapsSH = dealUpdateDataFromParts(datalists, ShippingRouteType.AISIN_TTSH);

            List<AisinCommonEntity> aceLists = new ArrayList<AisinCommonEntity>();
            List<BaseMessage> messageLists = new ArrayList<BaseMessage>();

            if (mapsTJ != null && mapsTJ.size() > 0) {
                List<BaseMessage> msgLists = (List<BaseMessage>) mapsTJ.get("modChangeMessageList");
                if (msgLists != null && msgLists.size() > 0) {
                    messageLists.addAll(msgLists);
                }
                List<AisinCommonEntity> dalists = (List<AisinCommonEntity>) mapsTJ.get("modChangeDetailList");
                if (dalists != null && dalists.size() > 0) {
                    aceLists.addAll(dalists);
                }
            }

            if (mapsSH != null && mapsSH.size() > 0) {
                List<BaseMessage> msgLists = (List<BaseMessage>) mapsSH.get("modChangeMessageList");
                if (msgLists != null && msgLists.size() > 0) {
                    messageLists.addAll(msgLists);
                }
                List<AisinCommonEntity> dalists = (List<AisinCommonEntity>) mapsSH.get("modChangeDetailList");
                if (dalists != null && dalists.size() > 0) {
                    aceLists.addAll(dalists);
                }
            }

            maps.put("aceLists", aceLists);
            maps.put("messageLists", messageLists);
        }
        return maps;
    }

    /**
     * deal aisin from TNM_EXP_PARTS
     * 
     * @param changeLists changeLists
     * @param type type
     * @throws UploadException UploadException
     * @return dataAllList dataAllList
     */
    private Map<String, Object> dealUpdateDataFromParts(List<AisinCommonEntity> changeLists, int type)
        throws UploadException {

        Map<String, Object> map = new HashMap<String, Object>();
        BaseParam param = new BaseParam();
        param.setSwapData("changeLists", changeLists);
        param.setSwapData("type", type);
        List<AisinCommonEntity> tjshDataList = baseMapper.select(this.getSqlId("getTJSHList"), param);
        if (tjshDataList != null && tjshDataList.size() > 0) {
            Map<Integer, AisinCommonEntity> srMasterMap = new HashMap<Integer, AisinCommonEntity>();
            Map<String, List<Integer>> srSupMap = new HashMap<String, List<Integer>>();
            for (AisinCommonEntity tjData : tjshDataList) {
                srMasterMap.put(tjData.getSrId(), tjData);
                List<Integer> supplierIds = srSupMap.get(tjData.getShippingRouteCode());
                if (supplierIds == null) {
                    supplierIds = new ArrayList<Integer>();
                    srSupMap.put(tjData.getShippingRouteCode(), supplierIds);
                }
                supplierIds.add(tjData.getSupplierId());
            }

            Map<String, List<AisinCommonEntity>> masterMaps = new HashMap<String, List<AisinCommonEntity>>();
            for (AisinCommonEntity ac : changeLists) {
                List<AisinCommonEntity> acList = masterMaps.get(ac.getShippingRouteCode());
                if (acList == null) {
                    acList = new ArrayList<AisinCommonEntity>();
                    masterMaps.put(ac.getShippingRouteCode(), acList);
                }
                acList.add(ac);
            }

            List<AisinCommonEntity> dataList = new ArrayList<AisinCommonEntity>();
            long maxLastEtd = 0;
            long minFirstEtd = 0;
            for (Map.Entry<Integer, AisinCommonEntity> entry : srMasterMap.entrySet()) {
                AisinCommonEntity ace = entry.getValue();
                List<AisinCommonEntity> acList = masterMaps.get(ace.getShippingRouteCode());
                List<Integer> supplierIds = srSupMap.get(ace.getShippingRouteCode());
                if (acList != null && acList.size() > 0) {
                    for (AisinCommonEntity a : acList) {
                        if (supplierIds.contains(a.getSupplierId())) {
                            continue;
                        }
                        AisinCommonEntity newEntity = new AisinCommonEntity();
                        BeanUtils.copyProperties(ace, newEntity);
                        newEntity.setSupplierId(a.getSupplierId());
                        newEntity.setSupplierCode(a.getSupplierCode());
                        newEntity.setOfficeId(a.getOfficeId());
                        newEntity.setCreatedBy(a.getCreatedBy());
                        newEntity.setUpdatedBy(a.getUpdatedBy());
                        newEntity.setExpRegion(a.getExpRegion());
                        newEntity.setInactiveFlag(a.getInactiveFlag());
                        dataList.add(newEntity);
                    }
                    long lastEtd = ace.getLastEtd().getTime();
                    long firstEtd = ace.getFirstEtd().getTime();
                    if (maxLastEtd == 0 || maxLastEtd < lastEtd) {
                        maxLastEtd = lastEtd;
                    }
                    if (minFirstEtd == 0 || minFirstEtd > firstEtd) {
                        minFirstEtd = firstEtd;
                    }
                }
            }
            map = dealCommomList(dataList, minFirstEtd, maxLastEtd, type);
        }

        return map;
    }

}
