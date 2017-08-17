/**
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * The utility class for date and time.
 */
public class DateTimeUtil {

    /** The Constant FORMAT_DATE_yymmddHHMM. */
    public static final String FORMAT_YYMMDD_HHMMSS = "yyMMddHHmmss";

    /** The Constant FORMAT_DATE_yymmddHHMM. */
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";

    /** The Constant format for a date. */
    public static final String FORMAT_DATE_YYYYMMDD_SOLIDUS = "yyyy/MM/dd";

    /** The Constant format for a date. */
    public static final String FORMAT_DATE_YYYYMD_SOLIDUS = "yyyy/M/d";

    /** The Constant FORMAT_DATE_yymmddHHMM. */
    public static final String FORMAT_YYYYMMDD_HHMMSS = "yyyyMMddHHmmss";

    /** The Constant FORMAT_DATE_yymmddHHMM. */
    public static final String FORMAT_DATE_FULL = "yyyyMMddHHmmssSSS";

    /** The Constant FORMAT_DATE_yymmddHHMM. */
    public static final String FORMAT_YYYYMMDD_HHMM = "yyyyMMddHHmm";

    /** The Constant FORMAT_DATE_yyMMddHHmm. */
    public static final String FORMAT_YYMMDDHHMM = "yyMMddHHmm";

    /** The Constant FORMAT_DATE_YYYYMM. */
    public static final String FORMAT_YEAR_MONTH = "yyyyMM";

    /** The Constant FORMAT_DATE_YY_MM. */
    public static final String FORMAT_YY_MM = "yyMM";

    /** The Constant FORMAT_MMM_YYYY. */
    public static final String FORMAT_MMMYYYY = "MMM yyyy";

    /** The Constant FORMAT_MMM_YYYY. */
    public static final String FORMAT_DDMMM = "dd MMM";

    /** The Constant FORMAT_MMDD. */
    public static final String FORMAT_MMDD = "MM-dd";

    /** The Constant FORMAT_MMM_YYYY. */
    public static final String FORMAT_D_MMM = "d-MMM";

    /** The Constant FORMAT_MMM_YYYY. */
    public static final String FORMAT_MMM = "MMM";

    /** The Constant FORMAT_MMMYYYY. */
    public static final String FORMAT_DDMMMYYYY = "dd MMM yyyy";

    /** The Constant format for a date. */
    public static final String FORMAT_DATE_YYYYMMDD = "yyyy-MM-dd";

    /** The Constant format for a date. */
    public static final String FORMAT_DATE_YYYYMD = "yyyy-M-d";

    /** The Constant FORMAT_MMMYYYY. */
    public static final String FORMAT_DDMMMYYYYHHMMSS = "dd MMM yyyy HH:mm:ss";

    /** The Constant FORMAT_MMMYYYY. */
    public static final String FORMAT_DDMMMYYYYHHMM = "dd MMM yyyy HH:mm";

    /** The Constant FORMAT_DATE_DDMMMYYYYHHMMSS. */
    public static final String FORMAT_DATE_DDMMMYYYYHHMMSS = "yyyy-MM-dd HH:mm:ss";

    /** The Constant FORMAT_DATE_DDMMMYYYYHHMMSS. */
    public static final String FORMAT_DATE_DDMMMYYYYHHMM = "yyyy-MM-dd HH:mm";

    /** The Constant FORMAT_DATE_DD. */
    public static final String FORMAT_DD = "dd";

    /** The Constant FORMAT_DATE_DD. */
    public static final String FORMAT_MM_DD = "MM/dd";

    /** The Constant FORMAT_DATE_YYYYMM. */
    public static final String FORMAT_YYYYMM = "yyyy-MM";

    /** The Constant FORMAT_DATE_YYYYMM. */
    public static final String FORMAT_YYYYM = "yyyy-M";

    /** The Constant FORMAT_DATE_YYYYMM. */
    public static final String FORMAT_YYYYMM_SOLIDUS = "yyyy/MM";

    /** The Constant FORMAT_DATE_YYYYMM. */
    public static final String FORMAT_YYYYM_SOLIDUS = "yyyy/M";

    /** The Constant FORMAT_DATE_HHmmss. */
    public static final String FORMAT_HHMMSS = "HHmmss";

    /** The Constant FORMAT_DATE_HHmmss. */
    public static final String FORMAT_IP_DATE = "yyyyMMdd_HHmmss";

    /** the pattern of dd-MMM-yyyy */
    public static final Pattern PATTERN_DATE = Pattern
        .compile("^([0-2]\\d|3[0,1])-\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\b-(\\d{2}|\\d{4})$");

    /** the pattern of dd-MMM-yyyy */
    public static final Pattern PATTERN_DATE_DDMMYYYY = Pattern
        .compile("^([0-2]\\d|3[0,1])-([0-1]?\\d)-(\\d{2}|\\d{4})$");

    /** the pattern of dd-MMM-yyyy HH:mm:ss */
    public static final Pattern PATTERN_TIME = Pattern
        .compile("^([0-2]\\d|3[0,1])-\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\b-(\\d{2}|\\d{4}) ([0-1]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$");

    /** Daylight Saving Time Offset. */
    public static final int DAYLIGHT_SAVING_TIME_OFFSET = 3600000;

    /**
     * 
     * Datetime Type(MAX:yyyy-MM-dd 23:59:59.99999,MIN:yyyy-MM-dd 00:00:00.99999).
     * 
     */
    public static enum DatetimeType {

        /**
         * yyyy-MM-dd 23:59:59.99999
         */
        MAX,

        /**
         * yyyy-MM-dd 00:00:00.00000
         */
        MIN,

        /**
         * yyyy-MM-dd hh:mm:ss.SSSSS
         */
        CURRENT
    }

    // /** logger */
    // private static Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);

    // /**
    // * Encode timestamp for exclusive check.
    // *
    // * @param timestamp the timestamp
    // * @return Timestamp the timestamp
    // */
    // public static Timestamp encodeForExclusiveCheck(Timestamp timestamp) {
    // if (timestamp == null) {
    // return null;
    // }
    // return new Timestamp(timestamp.getTime() * NumberConst.IntDef.INT_THOUSAND * NumberConst.IntDef.INT_THOUSAND
    // + timestamp.getNanos() % (NumberConst.IntDef.INT_THOUSAND * NumberConst.IntDef.INT_THOUSAND));
    // }

    // /**
    // * Decode timestamp for exclusive check.
    // *
    // * @param timestamp the timestamp
    // * @return Timestamp the timestamp
    // */
    // public static Timestamp decodeForExclusiveCheck(Timestamp timestamp) {
    // if (timestamp == null) {
    // return null;
    // }
    // long originalTimeInMils = timestamp.getTime();
    // long timeInMils = originalTimeInMils / NumberConst.IntDef.INT_THOUSAND / NumberConst.IntDef.INT_THOUSAND;
    // Timestamp actualTimestamp = new Timestamp(timeInMils);
    // actualTimestamp.setNanos((int) (originalTimeInMils % (NumberConst.IntDef.INT_THOUSAND
    // * NumberConst.IntDef.INT_THOUSAND * NumberConst.IntDef.INT_THOUSAND)));
    // return actualTimestamp;
    // }

    /**
     * format Date.
     * 
     * @param targetFormat the target format string
     * @param originalFormat the original format string
     * @param date the date to format
     * @return formated date
     */
    public static String formatDate(String targetFormat, String originalFormat, String date) {
        if (date == null || date.toString().length() == 0) {
            return "";
        }

        if (targetFormat == null || targetFormat.length() == 0) {
            return date;
        }

        if (originalFormat == null || originalFormat.length() == 0) {
            return date;
        }

        String retDate = "";

        try {
            retDate = new SimpleDateFormat(targetFormat, ChinaPlusConst.DEFALUT_LANG_LOCALE)
                .format(new SimpleDateFormat(originalFormat, ChinaPlusConst.DEFALUT_LANG_LOCALE).parse(date.toString()));
        } catch (ParseException e) {
            retDate = "";
        }
        return retDate;
    }

    /**
     * format Date.
     * 
     * @param targetFormat the target format string
     * @param originalFormat the original format string
     * @param date the date to format
     * @param lang language
     * @return formated date
     */
    public static String formatDate(String targetFormat, String originalFormat, String date, Language lang) {
        if (date == null || date.toString().length() == 0) {
            return "";
        }

        if (targetFormat == null || targetFormat.length() == 0) {
            return date;
        }

        if (originalFormat == null || originalFormat.length() == 0) {
            return date;
        }

        String retDate = "";

        try {
            retDate = new SimpleDateFormat(targetFormat, lang.getLocale()).format(new SimpleDateFormat(originalFormat,
                lang.getLocale()).parse(date.toString()));
        } catch (ParseException e) {
            retDate = "";
        }
        return retDate;
    }

    /**
     * Format data(String) to yyyy-MM-dd.
     * 
     * @param date the date to format
     * @return formated date(yyyy-MM-dd)
     */
    public static String formatDate(String date) {
        if (date == null || date.length() == 0) {
            return "";
        }
        Date dDate = parseDate(date);
        if (dDate == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_DATE_YYYYMMDD).format(dDate);
    }

    /**
     * Format data(Date) to yyyy-MM-dd.
     * 
     * @param date the date to format
     * @return formated date(yyyy-MM-dd)
     */
    public static String formatDate(Date date) {
        if (date == null || date.toString().length() == 0) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_DATE_YYYYMMDD).format(date);
    }

    /**
     * Format data(Timestamp) to yyyy-MM-dd.
     * 
     * @param date the date to format
     * @return formated date(yyyy-MM-dd)
     */
    public static String formatDate(Timestamp date) {
        if (date == null || date.toString().length() == 0) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_DATE_YYYYMMDD).format(date);
    }

    /**
     * format Date from default format(<code>DateTimeUtil.FORMAT_DDMMMYYYY</code>).
     * 
     * @param targetFormat the target format string
     * @param date the date to format
     * @return formated date
     */
    public static String formatDate(String targetFormat, Date date) {
        if (date == null || date.toString().length() == 0) {
            return "";
        }

        if (targetFormat == null || targetFormat.length() == 0) {
            return new SimpleDateFormat(DateTimeUtil.FORMAT_DDMMMYYYY, ChinaPlusConst.DEFALUT_LANG_LOCALE).format(date);
        }
        return new SimpleDateFormat(targetFormat, ChinaPlusConst.DEFALUT_LANG_LOCALE).format(date);
    }

    /**
     * format Date from default format(<code>DateTimeUtil.FORMAT_DDMMMYYYY</code>).
     * 
     * @param targetFormat the target format string
     * @param date the date to format
     * @param lang language
     * @return formated date
     */
    public static String formatDate(String targetFormat, Date date, Language lang) {
        if (date == null || date.toString().length() == 0) {
            return "";
        }

        if (targetFormat == null || targetFormat.length() == 0) {
            return new SimpleDateFormat(DateTimeUtil.FORMAT_DDMMMYYYY, lang.getLocale()).format(date);
        }
        return new SimpleDateFormat(targetFormat, lang.getLocale()).format(date);
    }

    /**
     * convert date to timestamp.
     * 
     * 
     * @param date date
     * @param timpType date time Type
     * @return timestamp
     */
    public static java.sql.Timestamp convertDateToTimestamp(java.sql.Date date, DatetimeType timpType) {

        if (date == null) {
            return null;
        }
        if (DatetimeType.CURRENT.equals(timpType)) {
            return new Timestamp(date.getTime());
        }

        String format = new SimpleDateFormat(FORMAT_DATE_YYYYMMDD).format(date);
        java.sql.Date newDate = java.sql.Date.valueOf(format);
        long newTime = newDate.getTime();
        if (DatetimeType.MAX.equals(timpType)) {
            newTime = newDate.getTime()
                    + (NumberConst.IntDef.INT_THOUSAND * NumberConst.IntDef.INT_SIXTY * NumberConst.IntDef.INT_SIXTY
                            * NumberConst.IntDef.INT_TWENTY_FOUR - 1);
        }
        Timestamp timeStamp = new Timestamp(newTime);

        return timeStamp;
    }

    // /**
    // * <p>
    // * format data for return "",not current Time
    // * </p>
    // *
    // * @param date date
    // * @param format format
    // * @return dateString dateString
    // */
    // public static String formatDateAllowsNull(Date date, String format) {
    // String dateString = "";
    // if (null != date) {
    // dateString = new SimpleDateFormat(format, ChinaPlusConst.DEFALUT_LANG_LOCALE).format(date);
    // }
    // return dateString;
    // }

    // /**
    // *
    // * None time zone to get date
    // *
    // * @param date date
    // * @param format format
    // * @param timezone the time zone
    // * @return date time
    // */
    // public static String nonLocalformatDateTime(Date date, String format, String timezone) {
    // String time = "";
    // SimpleDateFormat d = null;
    // if (date != null && !StringUtil.isEmpty(format)) {
    // d = new SimpleDateFormat(format);
    // if (!StringUtil.isEmpty(timezone)) {
    // TimeZone t = TimeZone.getTimeZone(timezone);
    // d.setTimeZone(t);
    // }
    // time = d.format(date);
    // } else if (date != null) {
    // time = date.toString();
    // } else {
    // time = new Date().toString();
    // }
    //
    // return time;
    // }

    /**
     * Parse string to date by format.
     * 
     * @param dateString string
     * @param format format
     * @return date object
     */
    public static Date parseDate(String dateString, String format) {
        if (dateString == null) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, ChinaPlusConst.DEFALUT_LANG_LOCALE);
            df.setLenient(false);
            // db2 will not allow year > 9999
            Date date = df.parse(dateString);
            if (isValidDate(date)) {
                return date;
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Parse string to date by format.
     * 
     * @param dateString string
     * @param format format
     * @return date object
     */
    public static Timestamp parseDateTime(String dateString, String format) {
        if (dateString == null) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, ChinaPlusConst.DEFALUT_LANG_LOCALE);
            df.setLenient(false);
            // db2 will not allow year > 9999
            Date date = df.parse(dateString);
            if (isValidDate(date)) {
                return new Timestamp(date.getTime());
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Is valid Date.
     * 
     * @param date the date
     * @return boolean true is valid date
     */
    public static boolean isValidDate(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final int maxYear = 9999;
        final int minYear = 1900;
        if (calendar.get(Calendar.YEAR) > maxYear || calendar.get(Calendar.YEAR) < minYear) {

            return false;
        }
        return true;
    }

    // /**
    // * Format timeStamp to string.
    // *
    // * @param timestamp timeStamp
    // * @param format date format
    // * @return string
    // */
    // public static String formatTimestamp(Timestamp timestamp, String format) {
    // if (timestamp == null) {
    // return "";
    // }
    // SimpleDateFormat formatter = new SimpleDateFormat(format, ChinaPlusConst.DEFALUT_LANG_LOCALE);
    // if (format.equals(DateTimeUtil.FORMAT_TIMESTAMP_1)) {
    // SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATE_TIME_1, ChinaPlusConst.DEFALUT_LANG_LOCALE);
    // return df.format(timestamp) + "." + StringUtil.PadLeft(String.valueOf(timestamp.getNanos() / 1000), 6, "0");
    // } else if (format.equals(DateTimeUtil.FORMAT_TIMESTAMP)) {
    // SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATE_TIME_1, ChinaPlusConst.DEFALUT_LANG_LOCALE);
    // return df.format(timestamp) + "." + StringUtil.PadLeft(String.valueOf(timestamp.getNanos()), 9, "0");
    // } else {
    // return formatter.format(timestamp);
    // }
    // }

    // /**
    // * Get time zone string by time zone and format.
    // *
    // * @param timeZone time zone
    // * @param format time format
    // * @return time zone string
    // */
    //
    // public static String getTimeZoneString(String timeZone, String format) {
    // String time = "";
    //
    // String specTimeZone = timeZone;
    // String specFormat = format;
    //
    // if (StringUtil.isEmpty(specTimeZone)) {
    // specTimeZone = DEFAULT_TIMEZONE;
    // }
    // if (StringUtil.isEmpty(specFormat)) {
    // specFormat = DEFAULT_FORMAT;
    // }
    //
    // // data time format
    // SimpleDateFormat d = new SimpleDateFormat(specFormat, ChinaPlusConst.DEFALUT_LANG_LOCALE);
    // // set time zone +8
    // TimeZone t = TimeZone.getTimeZone(specTimeZone);
    // d.setTimeZone(t);
    // time = d.format(new Date());
    //
    // return time;
    // }

    /**
     * Convert timeZone to GMT+ format.
     * 
     * @param timezone time zone
     * @return the string
     */
    public static String convertTimezoneToGMT(String timezone) {

        if (StringUtil.isEmpty(timezone)) {
            return timezone;
        }

        String standardTimezone = timezone;
        if (StringUtil.isNumeric(standardTimezone)) {
            if (standardTimezone.indexOf('+') == 0) {
                standardTimezone = standardTimezone.substring(1);
            }

            int timezoneValue = Integer.valueOf(standardTimezone);
            int prefix = Math.abs(timezoneValue) / NumberConst.IntDef.INT_HUNDRED;
            if (timezoneValue / NumberConst.IntDef.INT_HUNDRED > NumberConst.IntDef.INT_TWELVE
                    || timezoneValue / NumberConst.IntDef.INT_HUNDRED < -NumberConst.IntDef.INT_THIRTEEN) {

                return null;
            }

            int remainder = Math.abs(timezoneValue) % NumberConst.IntDef.INT_HUNDRED;
            String remainderStr = remainder < NumberConst.IntDef.INT_TEN ? "0" + remainder : "" + remainder;
            standardTimezone = (timezoneValue > 0 ? "GMT+" : "GMT-") + prefix + ":" + remainderStr;
        }

        return standardTimezone;
    }

    /**
     * convert timestamp from the source time zone to the target time zone.
     * 
     * @author wang_jinghui
     * @param timestamp a timestamp
     * @param sourceTimeZone the source time zone
     * @param targetTimeZone the target time zone
     * @return the date that has converted
     */
    public static Timestamp convertTimezone(Timestamp timestamp, String sourceTimeZone, String targetTimeZone) {

        Long time = timestamp.getTime();
        Long sourceRelativelyGMT = time - TimeZone.getTimeZone(convertTimezoneToGMT(sourceTimeZone)).getRawOffset();
        Long targetTime = sourceRelativelyGMT
                + TimeZone.getTimeZone(convertTimezoneToGMT(targetTimeZone)).getRawOffset();

        Timestamp result = new Timestamp(targetTime);
        result.setNanos(timestamp.getNanos());
        return result;
    }

    /**
     * convert date from the source time zone to the target time zone.
     * 
     * @param dateTime a time
     * @param sourceTimeZone the source time zone
     * @param targetTimeZone the target time zone
     * @return the date that has converted
     */
    public static Date convertTimezone(Date dateTime, String sourceTimeZone, String targetTimeZone) {

        Long time = dateTime.getTime();
        Long sourceRelativelyGMT = time - TimeZone.getTimeZone(sourceTimeZone).getRawOffset();
        Long targetTime = sourceRelativelyGMT + TimeZone.getTimeZone(targetTimeZone).getRawOffset();

        return new Date(targetTime);
    }

    // /**
    // *
    // * <p>
    // * convert timestamp from source time zone to database's time zone.
    // * </p>
    // *
    // * @author wang_jinghui
    // * @param srcTimestamp a timestamp
    // * @param sourceTimeZone source time zone
    // * @return the time with database's time zone
    // */
    // public static Timestamp convertToDbTimezone(Timestamp srcTimestamp, String sourceTimeZone) {
    // Timestamp dbTime = srcTimestamp;
    // try {
    // dbTime = convertTimezone(srcTimestamp, sourceTimeZone, DB_TIMEZONE);
    // } catch (Exception e) {
    // logger.error("convert time to database time zone is failed.", e);
    // }
    // return dbTime;
    // }

    // /**
    // *
    // * <p>
    // * convert a timestamp with database time zone to the target time zone.
    // * </p>
    // *
    // * @author wang_jinghui
    // * @param Timestamp the timestamp in database
    // * @param targetTimeZone the target time zone
    // * @return timestamp with target time zone
    // */
    // public static Timestamp convertFromDbTimezone(Timestamp dbTimestamp, String targetTimeZone) {
    // Timestamp timestamp = null;
    //
    // try {
    // timestamp = convertTimezone(dbTimestamp, DB_TIMEZONE, targetTimeZone);
    // } catch (Exception e) {
    // logger.error("convert database timestamp to target time zone is failed.", e);
    // }
    //
    // return timestamp;
    // }

    // /**
    // * <p>
    // * convert a timestamp with database time zone to the target time zone.
    // * </p>
    // *
    // * @param Timestamp the timestamp in database
    // * @param targetTimeZone the target time zone
    // * @return timestamp with target time zone
    // * @author zhu_xiaoquan
    // */
    // public static Timestamp convertFromDbTimezone(Timestamp dbTimestamp, String targetTimeZone, Date dstFrom, Date
    // dstTo) {
    // Timestamp timestamp = null;
    // try {
    // if (StringUtil.isEmpty(targetTimeZone)) {
    // return dbTimestamp;
    // }
    // String newTimeZone = StringUtil.convertTimezoneToGMT(targetTimeZone);
    // timestamp = convertTimezone(dbTimestamp, DB_TIMEZONE, newTimeZone);
    // int nanos = timestamp.getNanos();
    // if (dstFrom != null && dstTo != null) {
    // if (timestamp.getTime() >= dstFrom.getTime() && timestamp.getTime() <= dstTo.getTime()) {
    // Calendar c = Calendar.getInstance();
    // c.setTime(timestamp);
    // c.add(Calendar.HOUR_OF_DAY, 1);
    // timestamp = new Timestamp(c.getTimeInMillis());
    // timestamp.setNanos(nanos);
    // }
    // }
    //
    // } catch (Exception e) {
    // logger.error("convert database timestamp to target time zone is failed.", e);
    // }
    //
    // return timestamp;
    // }

    // /**
    // * <p>
    // * convert a timestamp with database time zone to the target time zone.
    // * </p>
    // *
    // * @param Timestamp the timestamp in database
    // * @param targetTimeZone the target time zone
    // * @return timestamp with target time zone
    // * @author zhu_xiaoquan
    // */
    // public static Timestamp convertFromDbTimezoneByOfficeCd(Timestamp dbTimestamp, String officeCd) {
    //
    // // When the office code dose not exist
    // if (StringUtil.isEmpty(officeCd)) {
    // return dbTimestamp;
    // }
    //
    // // Office dao
    // TnmOfficeExDao tnmOfficeDao = (TnmOfficeExDao) MosPreServlet.getWebApplicationContext().getBean(
    // "TnmOfficeExDao");
    // Timestamp timestamp = null;
    //
    // try {
    // // Get office by office code.
    // TnmOffice office = tnmOfficeDao.getOfficeByCode(officeCd);
    // // When the office dose not exist
    // if (null == office) {
    // return dbTimestamp;
    // }
    // // Get GMT by timeZone
    // String officeTimeZone = StringUtil.convertTimezoneToGMT(office.getTimeZone());
    // timestamp = convertTimezone(dbTimestamp, DB_TIMEZONE, officeTimeZone);
    // int nanos = timestamp.getNanos();
    // if (office.getDstFrom() != null && office.getDstTo() != null) {
    // if (timestamp.getTime() >= office.getDstFrom().getTime()
    // && timestamp.getTime() <= office.getDstTo().getTime()) {
    // Calendar c = Calendar.getInstance();
    // c.setTime(timestamp);
    // c.add(Calendar.HOUR_OF_DAY, 1);
    // timestamp = new Timestamp(c.getTimeInMillis());
    // timestamp.setNanos(nanos);
    // }
    // }
    //
    // } catch (Exception e) {
    // logger.error("convert database timestamp to target time zone is failed.", e);
    // }
    //
    // return timestamp;
    // }

    // /**
    // * get default TimeZone ID.
    // *
    // * @return default time zone
    // */
    // public static String getDefaultTimeZone() {
    // return TimeZone.getDefault().getID();
    // }

    // /**
    // *
    // * <p>
    // * convert time from source time zone to database's time zone.
    // * </p>
    // *
    // * @param srcDateTime a data time
    // * @param sourceTimeZone source time zone
    // * @return the time with database's time zone
    // */
    // public static Date convertToDbTimezone(Date srcDateTime, String sourceTimeZone) {
    // Date dbTime = srcDateTime;
    // try {
    // dbTime = convertTimezone(srcDateTime, sourceTimeZone, DB_TIMEZONE);
    // } catch (Exception e) {
    // logger.error("convert time to database time zone is failed.", e);
    // }
    // return dbTime;
    // }

    // /**
    // *
    // * <p>
    // * convert a time with database time zone to the target time zone.
    // * </p>
    // *
    // * @param dbDateTime the time in database
    // * @param targetTimeZone the target time zone
    // * @return time with target time zone
    // */
    // public static Date convertTimezoneFromDb(Date dbDateTime, String targetTimeZone) {
    // Date date = null;
    //
    // try {
    // date = convertTimezone(dbDateTime, DB_TIMEZONE, targetTimeZone);
    // } catch (Exception e) {
    // logger.error("convert database time to target time zone is failed.", e);
    // }
    //
    // return date;
    // }

    // /**
    // * Change Japan time to sg time.
    // *
    // * @param date Japan time
    // * @param userTimeZoneInfo userTimeZoneInfo
    // * @return local time
    // */
    // public static Timestamp changeServerTimeZoneToSG(Date date) {
    // if (date == null) {
    // return null;
    // }
    // return Timestamp.valueOf(changeJPTimeZoneToLocalInternally(date).toString());
    // }

    // /**
    // * Change jp time zone to local internally.
    // *
    // * @param session the session
    // * @param date the date
    // * @param <T> date
    // *
    // * @return the t
    // */
    // @SuppressWarnings("unchecked")
    // private static <T extends Date> T changeJPTimeZoneToLocalInternally(T date) {
    //
    // // need to move system timezone setting to properties' file
    // long offset = TimeZone.getTimeZone(DB_TIMEZONE).getRawOffset()
    // - TimeZone.getTimeZone(DEFAULT_TIMEZONE).getRawOffset();
    // SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_TIME_1);
    // SimpleDateFormat timestampFormat = new SimpleDateFormat(FORMAT_TIMESTAMP_1);
    //
    // long microseconds = 0;
    // if (date instanceof Timestamp) {
    // microseconds = ((Timestamp) date).getNanos() / YanmarConst.IntDef.INT_THOUSAND;
    // }
    //
    // T tmpDate = (T) date.clone();
    // tmpDate.setTime(date.getTime() - offset);
    //
    // if (date instanceof Timestamp) {
    // tmpDate = (T) Timestamp.valueOf(dateFormat.format(tmpDate) + "." + microseconds);
    // } else {
    // tmpDate = (T) Timestamp.valueOf(timestampFormat.format(tmpDate));
    // }
    //
    // return tmpDate;
    //
    // }

    /**
     * <p>
     * Get the string specified the date at the "yyyyMM" format.
     * </p>
     * 
     * @param string string specified the date
     * @return String
     */
    public static String toNumDate(String string) {
        SimpleDateFormat strToDateFmt = new SimpleDateFormat("MMM-yyyy", ChinaPlusConst.DEFALUT_LANG_LOCALE);
        ParsePosition pos = new ParsePosition(0);
        Date date = strToDateFmt.parse(string, pos);
        if (null == date) {
            return "";
        }
        SimpleDateFormat dateToStrFmt = new SimpleDateFormat("yyyyMM", ChinaPlusConst.DEFALUT_LANG_LOCALE);
        return dateToStrFmt.format(date);
    }

    /**
     * <p>
     * Get the string specified the date.
     * </p>
     * 
     * @param string string specified the date
     * @param format the string specified the format
     * @return string of the date
     */
    public static String displayMonthDate(String string, String format) {
        SimpleDateFormat strToDateFmt = new SimpleDateFormat("yyyyMM", ChinaPlusConst.DEFALUT_LANG_LOCALE);
        ParsePosition pos = new ParsePosition(0);
        Date date = strToDateFmt.parse(string, pos);
        if (null == date) {
            return "";
        }
        SimpleDateFormat dateToStrFmt = new SimpleDateFormat(format, ChinaPlusConst.DEFALUT_LANG_LOCALE);
        return dateToStrFmt.format(date);
    }

    /**
     * 
     * <p>
     * To obtain a specified date, months or years after the specified.
     * </p>
     * 
     * @param yearMonth Specified date
     * @param dist The number of months
     * @return After computing the date
     */
    public static Date rollYearMonth(String yearMonth, int dist) {

        final String format = "yyyyMM";
        if ((yearMonth == null) || (yearMonth.length() != format.length())) {
            return null;
        }

        final Date date = parseDate(yearMonth, format);
        if (date == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, dist);

        return cal.getTime();
    }

    /**
     * <p>
     * get the first day of the month given by date
     * </p>
     * 
     * @param date date
     * @return the first day of the month given by date
     */
    public static Date firstDay(Date date) {

        Calendar curCal = Calendar.getInstance();
        curCal.setTime(date);
        curCal.set(Calendar.DATE, 1);
        return curCal.getTime();
    }

    /**
     * <p>
     * get the first day of the month.
     * </p>
     * 
     * @param date date
     * @return the last day of the month given by date
     */
    public static Date lastDay(Date date) {

        Calendar curCal = Calendar.getInstance();
        curCal.setTime(date);
        curCal.set(Calendar.DAY_OF_MONTH, 1);
        curCal.roll(Calendar.DAY_OF_MONTH, -1);
        return curCal.getTime();
    }

    /**
     * <p>
     * get the sum of the month between beginYm and endYm
     * </p>
     * 
     * @param beginYm the month begin
     * @param endYm the month end
     * @return months
     */
    public static int getDiffMonths(Date beginYm, Date endYm) {
        final Calendar begincal = Calendar.getInstance();
        begincal.setTime(beginYm);

        final Calendar endcal = Calendar.getInstance();
        endcal.setTime(endYm);

        int months = (endcal.get(Calendar.YEAR) - begincal.get(Calendar.YEAR)) * NumberConst.IntDef.INT_TWELVE
                + (endcal.get(Calendar.MONTH) - begincal.get(Calendar.MONTH));
        return months;
    }

    /**
     * 
     * <p>
     * Get difference between the number of days between two dates.
     * </p>
     * 
     * @param beginDate the date begin
     * @param endDate the date end
     * @return day
     */
    public static Long getDayDifferent(Date beginDate, Date endDate) {
        long day = 0;
        if (beginDate != null && endDate != null) {
            
            // get dst
            Calendar beginCalendar = Calendar.getInstance();
            beginCalendar.setTime(beginDate);
            long bDst = beginCalendar.get(Calendar.DST_OFFSET);
            
            // get dst
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            long eDst = endCalendar.get(Calendar.DST_OFFSET);
            
            long beginTime = beginDate.getTime();
            long endTime = endDate.getTime();

            day = (endTime - beginTime + eDst - bDst)
                    / (NumberConst.IntDef.INT_THOUSAND * NumberConst.IntDef.INT_TWENTY_FOUR
                            * NumberConst.IntDef.INT_SIXTY * NumberConst.IntDef.INT_SIXTY);
        }
        return day;
    }

    // /**
    // * Check the date string format.
    // *
    // *
    // * @param strDate the date string
    // * @return if the date string format is "dd-MM-yyyy" then return true
    // */
    // public static boolean isDateDDMMYYYYPatten(String strDate) {
    // return PATTERN_DATE_DDMMYYYY.matcher(strDate).find();
    // }

    // /**
    // * Check the date string format.
    // *
    // *
    // * @param strDate the date string
    // * @return if the date string format is "dd-MMM-yyyy" then return true
    // */
    // public static boolean isDatePatten(String strDate) {
    // return PATTERN_DATE.matcher(strDate).find();
    // }

    // /**
    // * Check the date time string format.
    // *
    // *
    // * @param strDate the date time string
    // * @return if the date string format is "dd-MMM-yyyy HH:mm:ss" then return true
    // */
    // public static boolean isTimePatten(String strDate) {
    // return PATTERN_TIME.matcher(strDate).find();
    // }

    /**
     * Add special month to a source date.
     * 
     * @param srcDate source date
     * @param month the the year to add to add
     * @return the result date that added the parameter
     */
    public static Date addMonth(Date srcDate, int month) {
        return add(srcDate, 0, month, 0, 0, 0, 0, 0);
    }

    /**
     * Add special month to a source date.
     * 
     * @param srcDate source date
     * @param day the the year to add to add
     * @return the result date that added the parameter
     */
    public static Date addDay(Date srcDate, int day) {
        return add(srcDate, 0, 0, day, 0, 0, 0, 0);
    }

    /**
     * Add special year/month/day/hour/minute/second to a source date.
     * 
     * @param srcDate source date
     * @param year the year to add
     * @param month the the year to add to add
     * @param day the day to add
     * @param hour the hour to add
     * @param minute the minute to add
     * @param second the second to add
     * @return the result date that added the parameter
     */
    public static Date add(Date srcDate, int year, int month, int day, int hour, int minute, int second) {
        return add(srcDate, year, month, day, hour, minute, second, 0);
    }

    /**
     * Add special year/month/day/hour/minute/second to a source date.
     * 
     * @param srcDate source date
     * @param year the year to add
     * @param month the the year to add to add
     * @param day the day to add
     * @param hour the hour to add
     * @param minute the minute to add
     * @param second the second to add
     * @param milli the milliseconds to add
     * @return the result date that added the parameter
     */
    public static Date add(Date srcDate, int year, int month, int day, int hour, int minute, int second, int milli) {
        Date resultDate = null;

        if (srcDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(srcDate);
            if (year != 0) {
                calendar.add(Calendar.YEAR, year);
            }
            if (month != 0) {
                calendar.add(Calendar.MONTH, month);
            }
            if (day != 0) {
                calendar.add(Calendar.DATE, day);
            }
            if (hour != 0) {
                calendar.add(Calendar.HOUR, hour);
            }
            if (minute != 0) {
                calendar.add(Calendar.MINUTE, minute);
            }
            if (second != 0) {
                calendar.add(Calendar.SECOND, second);
            }
            if (milli != 0) {
                calendar.add(Calendar.MILLISECOND, milli);
            }
            resultDate = calendar.getTime();
        }

        return resultDate;
    }

    /**
     * Get day of week
     * 
     * @param srcDate date
     * @return day of week
     */
    public static int getDayOfWeek(Date srcDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(srcDate);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Get day of week
     * 
     * @param srcDate date
     * @return day of week
     */
    public static int getDayOfMonth(Date srcDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(srcDate);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get day of week
     * 
     * @param srcDate date
     * @return day of week
     */
    public static int getWeekOfMonth(Date srcDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(srcDate);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    // /**
    // *
    // * <p>
    // * Convert time from screen select date to database's time zone.
    // * </p>
    // *
    // * @param srcDateTimeColumns screen select date
    // * @param ownerTimeZone the owner time zone
    // * @return the time with database's time zone
    // */
    // public static Date[] convertFliterTimeToDbTimezone(Date[] srcDateTimeColumns, String ownerTimeZone) {
    // Date[] dateCondition = srcDateTimeColumns;
    // if (srcDateTimeColumns != null && srcDateTimeColumns.length > 0) {
    // if (srcDateTimeColumns[0] != null) {
    // dateCondition[0] = convertToDbTimezone(srcDateTimeColumns[0], ownerTimeZone);
    // }
    // if (srcDateTimeColumns.length > 1 && srcDateTimeColumns[1] != null) {
    // dateCondition[1] = convertToDbTimezone(srcDateTimeColumns[1], ownerTimeZone);
    // }
    // }
    // return dateCondition;
    // }

    /**
     * Get the first month of the year.
     * 
     * @param date date
     * @return the the first month of the year
     */
    public static Date getFirstMonthOfYear(Date date) {

        Calendar curCal = Calendar.getInstance();
        curCal.setTime(date);
        curCal.set(Calendar.MONTH, 0);
        curCal.set(Calendar.DAY_OF_MONTH, 1);
        return curCal.getTime();
    }

    /**
     * Get the last month of the year.
     * 
     * @param date date
     * @return the the first month of the year
     */
    public static Date getLastMonthOfYear(Date date) {

        Calendar curCal = Calendar.getInstance();
        curCal.setTime(date);
        curCal.set(Calendar.MONTH, NumberConst.IntDef.INT_ELEVEN);
        curCal.set(Calendar.DAY_OF_MONTH, 1);
        return curCal.getTime();
    }

    /**
     * month add N.
     * 
     * @param originalMonth the original month
     * @param n the month to add
     * @return originalMonth + N
     */
    public static String monthAddN(String originalMonth, int n) {
        final int lenMonth = 6;
        final int lenYear = 4;
        final int maxMonth = 12;
        if (StringUtil.isEmpty(originalMonth) || !StringUtil.isNumeric(originalMonth)
                || originalMonth.length() != lenMonth) {
            return null;
        }

        int year = Integer.valueOf(originalMonth.substring(0, lenYear));
        int month = Integer.valueOf(originalMonth.substring(lenYear));

        int addMonth = n;
        if (addMonth >= maxMonth) {
            year = year + addMonth / maxMonth;
            addMonth = addMonth % maxMonth;
        }

        String monthStr;
        if ((month + addMonth) > maxMonth) {
            year++;
            month = month + addMonth - maxMonth;
            monthStr = new DecimalFormat("00").format(Integer.valueOf(month));
        } else {
            month = month + addMonth;
            monthStr = new DecimalFormat("00").format(Integer.valueOf(month));
        }
        return new StringBuilder().append(year).append(monthStr).toString();
    }

    /**
     * get targetMonth
     * 
     * @param minuend format: yyyyMM
     * @param meiosis format: yyyyMM
     * @return minuendMonth - meiosisMonth
     */
    public static Integer monthSub(String minuend, String meiosis) {
        final int lenYearMonth = 6;
        final int lenYear = 4;
        final int monthInYear = 12;

        if (StringUtil.isEmpty(minuend) || StringUtil.isEmpty(meiosis)) {
            return null;
        }

        if (!StringUtil.isNumeric(minuend) || !StringUtil.isNumeric(meiosis)) {
            return null;
        }

        if (minuend.length() != lenYearMonth || meiosis.length() != lenYearMonth) {
            return null;
        }

        Integer minuendYear = Integer.valueOf(minuend.substring(0, lenYear));
        Integer minuendMonth = Integer.valueOf(minuend.substring(lenYear, lenYearMonth));
        Integer meiosisYear = Integer.valueOf(meiosis.substring(0, lenYear));
        Integer meiosisMonth = Integer.valueOf(meiosis.substring(lenYear, lenYearMonth));

        return (minuendYear - meiosisYear) * monthInYear + minuendMonth - meiosisMonth;
    }

    /**
     * get last month.
     * 
     * @param originalMonth the original month
     * @return last month
     */
    public static String getLastMonth(String originalMonth) {
        final int lenMonth = 6;
        final int lenYear = 4;
        final int maxMonth = 12;
        if (StringUtil.isEmpty(originalMonth) || !StringUtil.isNumeric(originalMonth)
                || originalMonth.length() != lenMonth) {
            return null;
        }

        int year = Integer.valueOf(originalMonth.substring(0, lenYear));
        int month = Integer.valueOf(originalMonth.substring(lenYear));

        if (month == 1) {
            year--;
            month = maxMonth;
        } else {
            month--;
        }

        String monthStr = new DecimalFormat("00").format(Integer.valueOf(month));
        return new StringBuilder().append(year).append(monthStr).toString();
    }

    /**
     * date compare
     * 
     * @param date1 date1
     * @param date2 date2
     * @return boolean
     */
    public static boolean isEqual(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return true;
        } else {
            if (date1 == null || date2 == null) {
                return false;
            } else {
                return date1.equals(date2);
            }
        }
    }

    /**
     * Parse string to date by format.
     * 
     * @param dateString the string format date
     * @return date object
     */
    public static Date parseDate(String dateString) {

        Date result = null;
        if (StringUtil.isEmpty(dateString)) {
            return null;
        }

        result = parseDate(dateString, FORMAT_YYYYMMDD);
        if (result != null) {
            return result;
        }

        result = parseDate(dateString, FORMAT_DATE_YYYYMMDD_SOLIDUS);
        if (result != null) {
            return result;
        }

        result = parseDate(dateString, FORMAT_DATE_YYYYMD_SOLIDUS);
        if (result != null) {
            return result;
        }

        result = parseDate(dateString, FORMAT_DATE_YYYYMMDD);
        if (result != null) {
            return result;
        }

        result = parseDate(dateString, FORMAT_DATE_YYYYMD);
        if (result != null) {
            return result;
        }

        result = parseDate(dateString, FORMAT_DDMMMYYYY);
        if (result != null) {
            return result;
        }

        result = parseDate(dateString, FORMAT_DATE_DDMMMYYYYHHMM);
        if (result != null) {
            return result;
        }

        return null;
    }

    /**
     * Parse string to month by format.
     * 
     * @param monthString the string format month
     * @return date object
     */
    public static Date parseMonth(String monthString) {

        if (StringUtil.isEmpty(monthString)) {
            return null;
        }

        Date result = parseDate(monthString);
        if (result != null) {
            return result;
        }

        result = parseDate(monthString, FORMAT_YEAR_MONTH);
        if (result != null) {
            return result;
        }

        result = parseDate(monthString, FORMAT_YYYYMM);
        if (result != null) {
            return result;
        }

        result = parseDate(monthString, FORMAT_YYYYM);
        if (result != null) {
            return result;
        }

        result = parseDate(monthString, FORMAT_YYYYMM_SOLIDUS);
        if (result != null) {
            return result;
        }

        result = parseDate(monthString, FORMAT_YYYYM_SOLIDUS);
        if (result != null) {
            return result;
        }

        result = parseDate(monthString, FORMAT_MMMYYYY);
        if (result != null) {
            return result;
        }

        return null;
    }

    /**
     * Get the order month for display.
     * 
     * @param orderMonth the original order month
     * @param originalFormat the original format
     * @return display order month
     */
    public static String getDisOrderMonth(String orderMonth, String originalFormat) {

        String targetFormat = null;
        SessionInfoManager ma = SessionInfoManager.getContextInstance();
        Locale locale = ma.getLoginLocale();
        if (locale != null && ChinaPlusConst.Language.CHINESE.getLocale().equals(locale)) {
            targetFormat = DateTimeUtil.FORMAT_YYYYMM;
        } else {
            targetFormat = DateTimeUtil.FORMAT_MMMYYYY;
        }

        return DateTimeUtil.formatDate(targetFormat, originalFormat, orderMonth);
    }

    /**
     * Get the date for display.
     * 
     * @param date the original date
     * @param originalFormat the original format
     * @return display order month
     */
    public static String getDisDate(String date, String originalFormat) {

        String targetFormat = null;
        SessionInfoManager ma = SessionInfoManager.getContextInstance();
        Locale locale = ma.getLoginLocale();
        if (locale != null && ChinaPlusConst.Language.CHINESE.getLocale().equals(locale)) {
            targetFormat = DateTimeUtil.FORMAT_DATE_YYYYMMDD;
        } else {
            targetFormat = DateTimeUtil.FORMAT_DDMMMYYYY;
        }

        return DateTimeUtil.formatDate(targetFormat, originalFormat, date);
    }

    /**
     * Get the date for display.
     * 
     * @param date the original date
     * @return display order month
     */
    public static String getDisDate(Date date) {

        String targetFormat = null;
        SessionInfoManager ma = SessionInfoManager.getContextInstance();
        Locale locale = ma.getLoginLocale();
        if (locale != null && ChinaPlusConst.Language.CHINESE.getLocale().equals(locale)) {
            targetFormat = DateTimeUtil.FORMAT_DATE_YYYYMMDD;
        } else {
            targetFormat = DateTimeUtil.FORMAT_DDMMMYYYY;
        }

        return DateTimeUtil.formatDate(targetFormat, date);
    }

    /**
     * Get the week.
     * 
     * @param date the date
     * @return the week
     */
    public static String getWeek(Date date) {

        String[] weeks = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     * Get the year.
     * 
     * @param date the date
     * @return the year
     */
    public static int getYear(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * Get the month.
     * 
     * @param date the date
     * @return the month
     */
    public static int getMonth(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * Get the day.
     * 
     * @param date the date
     * @return the day
     */
    public static int getDay(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get the differ of two date.
     * 
     * @param startDate start date
     * @param endDate end date
     * @return the differ of two date
     */
    public static int getDifferMonth(Date startDate, Date endDate) {

        if (startDate == null || endDate == null) {
            return 0;
        }

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(startDate);
        int yearStart = calStart.get(Calendar.YEAR);
        int monthStart = calStart.get(Calendar.MONTH);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        int yearEnd = calEnd.get(Calendar.YEAR);
        int monthEnd = calEnd.get(Calendar.MONTH);

        return Math.abs((yearEnd - yearStart) * IntDef.INT_TWELVE + (monthEnd - monthStart));
    }

    /**
     * Get day of month by week.
     * 
     * @param month object month
     * @param weekNum W1,W2,W3,W4,W5
     * @param week Mon,Tue,Wed,Thu,Fri,Sat,Sun
     * @return day of month
     */
    public static Integer getDayByWeek(String month, String weekNum, String week) {

        int weekOfMonth = Integer.valueOf(weekNum.replace("W", ""));
        String[] weeks = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        List<String> lstWeek = Arrays.asList(weeks);
        int dayOfWeek = lstWeek.indexOf(week) + 1;

        Date date = DateTimeUtil.parseMonth(month);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int oldMonth = cal.get(Calendar.MONTH);
        cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekOfMonth);
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        int newMonth = cal.get(Calendar.MONTH);
        if (oldMonth == newMonth) {
            return cal.get(Calendar.DAY_OF_MONTH);
        }
        return null;
    }

}
