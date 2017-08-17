/**
 * Common logic for prepare Supply Chain.
 * 
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chinaplus.common.bean.TnmCalendarDetailEx;
import com.chinaplus.common.bean.TnmCalendarPartyEx;
import com.chinaplus.common.consts.CodeConst.CalendarParty;
import com.chinaplus.common.consts.CodeConst.WorkingDay;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;

/**
 * Common logic for prepare Supply Chain.
 */
public class CalendarManager {

    /**
     * Find Calendar Id by Calendar Party Information.
     *
     * @param tgPartyType Target Party Type
     * @param officeId office id
     * @param tgPartyId Target Party Id
     * @param calendarPartyMap Calendar party Map
     * @param partyInfoList Party Information List
     * @return calendar list
     */
    public static Integer findCalendarId(Integer tgPartyType, Integer officeId, Integer tgPartyId,
        Map<String, Integer> calendarPartyMap, List<TnmCalendarPartyEx> partyInfoList) {

        // check
        if (tgPartyType == null || tgPartyId == null || officeId == null) {
            throw new BusinessException(
                "Parameters are incorrect.Target Party Type and Target Party Id can not be empty.");
        }

        // define
        Integer calendarId = null;

        // prepare keys
        String key = new StringBuffer().append(officeId).append(StringConst.UNDERLINE).append(tgPartyId)
            .append(StringConst.UNDERLINE).append(tgPartyType).toString();

        // check with map
        if (calendarPartyMap != null && calendarPartyMap.containsKey(key)) {
            return calendarPartyMap.get(key);
        }

        // if no data
        if (partyInfoList == null || partyInfoList.isEmpty()) {
            return calendarId;
        }

        // get calendar Id from partyInfoList
        for (TnmCalendarPartyEx party : partyInfoList) {

            // check party type
            int comp = party.getPartyType().compareTo(tgPartyType);

            // if over
            if (comp > IntDef.INT_ZERO) {
                break;
            } else if (comp < IntDef.INT_ZERO) {
                // if smaller, do next
                continue;
            }

            // check party type
            comp = party.getOfficeId().compareTo(officeId);

            // if over
            if (comp > IntDef.INT_ZERO) {
                break;
            } else if (comp < IntDef.INT_ZERO) {
                // if smaller, do next
                continue;
            }

            // define
            Integer partyId = null;

            // get party id
            switch (tgPartyType) {
                case CalendarParty.SUPPLIER:
                    partyId = party.getSupplierId();
                    break;
                case CalendarParty.IMP_SEAPORT_CUSTOMS:
                case CalendarParty.IMP_AIRPORT_CUSTOMS:
                case CalendarParty.TTC_IMPORT_OFFICE:
                    partyId = party.getOfficeId();
                    break;
                case CalendarParty.CUSTOMER:
                    partyId = party.getCustomerId();
                    break;
                case CalendarParty.EXP_WAREHOUSE:
                case CalendarParty.TTC_IMP_WAREHOUSE:
                    partyId = party.getWhsId();
                    break;
                default:
                    throw new BusinessException("Party Type is not exists.");
            }

            // check
            if (partyId == null) {
                continue;
            }

            // check
            comp = partyId.compareTo(tgPartyId);

            // if smaller, do next
            if (comp < IntDef.INT_ZERO) {
                continue;
            } else if (comp == IntDef.INT_ZERO) {
                // set calendar id
                calendarId = party.getCalendarId();
            }

            break;
        }

        // set into map
        if (calendarPartyMap != null) {
            calendarPartyMap.put(key, calendarId);
        }

        // if no calendar
        return calendarId;
    }

    /**
     * Find Calendar Info from Calendar list.
     *
     * @param tgPartyType Target Party Type
     * @param officeId office id
     * @param tgPartyId Target Party Id
     * @param calendarMap Calendar Map
     * @param allCalendarList Calendar List
     * @param calendarPartyMap calendar party map
     * @param partyInfoList Party Information List
     * @return calendar list
     */
    public static List<TnmCalendarDetailEx> findCalendarInfo(Integer tgPartyType, Integer officeId, Integer tgPartyId,
        Map<Integer, List<TnmCalendarDetailEx>> calendarMap, List<TnmCalendarDetailEx> allCalendarList,
        Map<String, Integer> calendarPartyMap, List<TnmCalendarPartyEx> partyInfoList) {

        // get calendar id
        Integer calendarId = findCalendarId(tgPartyType, officeId, tgPartyId, calendarPartyMap, partyInfoList);

        // find calendar inforamtion
        return findCalendarInfo(calendarId, calendarMap, allCalendarList);
    }

    /**
     * Find Calendar Info from Calendar list.
     *
     * @param calendarId calendar id
     * @param calendarMap Calendar Map
     * @param allCalendarList Calendar List
     * @return calendar list
     */
    public static List<TnmCalendarDetailEx> findCalendarInfo(Integer calendarId,
        Map<Integer, List<TnmCalendarDetailEx>> calendarMap, List<TnmCalendarDetailEx> allCalendarList) {

        // if no calendar
        if (calendarId == null) {
            return new ArrayList<TnmCalendarDetailEx>();
        }

        // if exists
        if (calendarMap != null && calendarMap.containsKey(calendarId)) {
            return calendarMap.get(calendarId);
        } else {
            // get calendar
            List<TnmCalendarDetailEx> calendarLst = new ArrayList<TnmCalendarDetailEx>();

            // if no data, return empty list
            if(allCalendarList == null || allCalendarList.isEmpty()) {
                return calendarLst;
            }

            Date currentDate = null;
            for (TnmCalendarDetailEx calendar : allCalendarList) {

                // get compare
                int compare = calendar.getCalendarId().compareTo(calendarId);

                // if calendar id is low
                if (compare < IntDef.INT_ZERO) {
                    continue;
                } else if (compare > IntDef.INT_ZERO) {
                    // if calendar id is high
                    break;
                }

                // get diff
                long diff = DateTimeUtil.getDayDifferent(currentDate, calendar.getCalendarDate());

                // add
                while (diff > IntDef.INT_ONE) {

                    // get date
                    currentDate = DateTimeUtil.addDay(currentDate, IntDef.INT_ONE);

                    // reset new newCalender
                    TnmCalendarDetailEx newCalender = new TnmCalendarDetailEx();
                    newCalender.setCalendarId(calendarId);
                    newCalender.setCalendarDate(currentDate);

                    // set default
                    if (chkWorkingDayByDef(currentDate)) {
                        newCalender.setWorkingFlag(WorkingDay.WORKING_DAY);
                    } else {
                        newCalender.setWorkingFlag(WorkingDay.REST_DAY);
                    }

                    // set into calendar
                    calendarLst.add(newCalender);

                    // next
                    diff--;
                }

                // reset date
                currentDate = calendar.getCalendarDate();
                // set into calendarLst
                calendarLst.add(calendar);
            }

            // set into map
            if (calendarMap != null) {
                calendarMap.put(calendarId, calendarLst);
            }

            // return calendar list
            return calendarLst;
        }
    }

    /**
     * Get date by working days.
     *
     * @param workingDays count working days
     * @param sourceDate start date
     * @param calendarInfo calendar information
     * @return date
     */
    public static Date getDateByWorkingDay(int workingDays, Date sourceDate, List<TnmCalendarDetailEx> calendarInfo) {

        // return current date
        return getDateByWorkingDay(workingDays, sourceDate, calendarInfo, IntDef.INT_ONE, true);
    }

    /**
     * Get date by working days.
     *
     * @param workingDays count working days
     * @param sourceDate start date
     * @param calendarInfo calendar information
     * @param withNext with next date or not?
     * @return date
     */
    public static Date getDateByWorkingDay(int workingDays, Date sourceDate, List<TnmCalendarDetailEx> calendarInfo, boolean withNext) {

        // return current date
        return getDateByWorkingDay(workingDays, sourceDate, calendarInfo, IntDef.INT_ONE, withNext);
    }

    /**
     * Get date by working days.
     *
     * @param workingDays count working days
     * @param sourceDate start date
     * @param calendarInfo calendar information
     * @param step step
     * @return date
     */
    public static Date getDateByWorkingDay(int workingDays, Date sourceDate, List<TnmCalendarDetailEx> calendarInfo, int step) {

        // return current date
        return getDateByWorkingDay(workingDays, sourceDate, calendarInfo, step, true);
    }

    /**
     * Get date by working days.
     *
     * @param workingDays count working days
     * @param sourceDate start date
     * @param calendarInfo calendar information
     * @param step date up step
     * @param withNext with next date or not?
     * @return date
     */
    public static Date getDateByWorkingDay(int workingDays, Date sourceDate, List<TnmCalendarDetailEx> calendarInfo,
        int step, boolean withNext) {

        // is working day
        boolean isWorkingDay = true;
        Date loopDate = sourceDate;
        int loopDays = workingDays;

        // get from database
        while (loopDays > IntDef.INT_ZERO) {

            // check working day
            isWorkingDay = checkWorkingDay(calendarInfo, loopDate);
            
            // loop down
            if (isWorkingDay) {
                loopDays --;
            }
            
            // break
            if (loopDays > IntDef.INT_ZERO) {
                // next date
                loopDate = DateTimeUtil.addDay(loopDate, step);
            }
        }
        
        // get next date
        if (withNext) {
            
            // next date
            loopDate = getNextWorkingDay(DateTimeUtil.addDay(loopDate, step), calendarInfo, step);
        }

        // return current date
        return loopDate;
    }

    /**
     * Get date by working days.
     *
     * @param targetDate start date
     * @param calendarInfo calendar information
     * @param step step
     * @return date
     */
    public static Date getNextWorkingDay(Date targetDate, List<TnmCalendarDetailEx> calendarInfo, int step) {

        // is working day
        boolean isWorkingDay = true;
        Date loopDate = targetDate;

        // get from database
        while (true) {

            // check working day
            isWorkingDay = checkWorkingDay(calendarInfo, loopDate);
            if (isWorkingDay) {
                break;
            }

            // next date
            loopDate = DateTimeUtil.addDay(loopDate, step);
        }

        // return current date
        return loopDate;
    }

    /**
     * Check Working Day use calendar.
     *
     * @param calendarList calendar information list
     * @param targetDate target date
     * @return is working day or not
     */
    public static boolean checkWorkingDay(List<TnmCalendarDetailEx> calendarList, Date targetDate) {

        // check date
        TnmCalendarDetailEx currDateCal = null;

        // define
        int i = IntDef.INT_ZERO;
        int size = calendarList.size();
        // loop
        while (i < size) {

            // get date
            TnmCalendarDetailEx calendar = calendarList.get(i);
            
            // check date diff
            if (calendar.getCalendarDate().before(targetDate)) {

                // get diff
                long dayDiff = DateTimeUtil.getDayDifferent(calendar.getCalendarDate(), targetDate);

                // step up
                if (dayDiff < IntDef.INT_TWO) {
                    // loop start
                    i++;
                } else {
                    // loop start
                    i = (int) (i + dayDiff);
                }
                // next
                continue;
            }

            // cause if has date must like 1,2,3
            if (calendar.getCalendarDate().equals(targetDate)) {
                currDateCal = calendar;
                break;
            }

            // if after
            if (calendar.getCalendarDate().after(targetDate)) {

                // add new
                break;
            }
        }
        
        // if no calendar
        if (currDateCal == null) {

            // check working day by default
            return chkWorkingDayByDef(targetDate);
        } else {

            // if non working day
            if (!currDateCal.getWorkingFlag().equals(WorkingDay.WORKING_DAY)) {
                return false;
            }

            return true;
        }
    }

    /**
     * Check working day by default.(Saturday and Sunday)
     * 
     * @param date check date
     * @return is working day
     */
    public static boolean chkWorkingDayByDef(Date date) {

        // set default
        int dayOfWeek = DateTimeUtil.getDayOfWeek(date);

        // weekly date
        if (dayOfWeek == IntDef.INT_ONE || dayOfWeek == IntDef.INT_SEVEN) {

            // set as non working day
            return false;
        }

        return true;
    }
}
