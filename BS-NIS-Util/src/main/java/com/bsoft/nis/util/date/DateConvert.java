package com.bsoft.nis.util.date;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Describtion:日期转换工具
 * Created: dragon
 * Date： 2016/11/21.
 */
public class DateConvert {
    /**
     * Java Date 类型转换成LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 从LocalDateTime获取LocalDate日期
     *
     * @param dateTime
     * @return
     */
    public static LocalDate toLocalDate(LocalDateTime dateTime) {
        return LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
    }

    /**
     * 从LocalDateTime获取LocalTime时间
     *
     * @param dateTime
     * @return
     */
    public static LocalTime toLocalTime(LocalDateTime dateTime) {
        return LocalTime.of(dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
    }

    /**
     * java8 LocalDateTime转换java Date
     *
     * @param localDateTime
     * @return
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将日期格式的字符串转换成date
     * 格式：yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date toDateTime(String dateStr) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm";
        return DateConvert.toDateTime(dateStr, pattern);
    }

    /**
     * 将日期格式的字符串转换成date,可自定义格式
     *
     * @param dateStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date toDateTime(String dateStr, String pattern) throws ParseException {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        String _pattern;
        if (StringUtils.isEmpty(pattern)) {
            _pattern = "yyyy-MM-dd HH:mm";
        } else {
            _pattern = pattern;
        }
        SimpleDateFormat format = new SimpleDateFormat(_pattern);

        //字符串转日期
        Date dateParse = format.parse(dateStr);
        return dateParse;

    }

    /**
     * 获取日期的年份
     *
     * @param date
     * @return
     */
    public static String getDateYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return year;
    }

    /**
     * 获取日期月份
     *
     * @param date
     * @return
     */
    public static String getDateMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        return month;
    }

    /**
     * 获取日期当前月第几天
     *
     * @param date
     * @return
     */
    public static String getDateDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        return day;
    }

    /**
     * 获取日期当前天第几小时
     *
     * @param date
     * @return
     */
    public static String getDateHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        return hour;
    }

    public static String getStandardString(String dateStr) throws ParseException {
        Date date= DateConvert.toDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String standardString = simpleDateFormat.format(date);
        return standardString;
    }
    public static String getDateString(String dateStr) throws ParseException {
        Date date= DateConvert.toDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String s = simpleDateFormat.format(date);
        return s;
    }
 /*   public static String getStandardString(String dateStr) throws ParseException {
        LocalDateTime datetime = DateConvert.toLocalDateTime(DateConvert.toDateTime(dateStr));
        String standardString = datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return standardString;
    }*/

    /**
     * 将日期类型字符串转换yyyy-MM-dd HH:mm:ss格式字符串
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static String toString(String dateStr) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm";
        return toString(dateStr, pattern);
    }

    /**
     * 将日期类型字符串转换成自定义格式字符串
     *
     * @param dateStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static String toString(String dateStr, String pattern) throws ParseException {
        String _pattern, ret;
        if (StringUtils.isEmpty(pattern)) {
            _pattern = "yyyy-MM-dd HH:mm";
        } else {
            _pattern = pattern;
        }
        SimpleDateFormat format = new SimpleDateFormat(_pattern);

        if (dateStr == null) {
            return "";
        }
        //字符串转日期
        Date dateParse = format.parse(dateStr);
        //日期转字符串
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateParse);
        Date date = calendar.getTime();
        ret = format.format(date);
        return ret;
    }
}
