package com.bsoft.nis.util.date;

import com.bsoft.nis.util.date.pojo.OffCycle;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具（依赖java8）
 * Created by dragon on 2016/10/12.
 */
public class DateUtil {

    /**
     * 获取当前应用服务器日期
     * @return
     */
    public static String getApplicationDate(){
        String ret1 = "1900-01-01";
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ret1 = format.format(date);
        return ret1;
    }

    /**
     * 获取当前应用服务器时间
     * @return
     */
    public static String getApplicationDateTime(){
        String ret1 = "1900-01-01 00:00:00";
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ret1 = format.format(date);
        return ret1;
    }

    /**
     * 获取当前应用服务器整点时间
     * @return
     */
    public static String getApplicationDateTime2(){
        String ret1 = "1900-01-01 00:00:00";
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        ret1 = format.format(date);
        return ret1;
    }

    /**
     * 获取当前应用服务器间隔一段时间后的时间
     * @return
     */
    public static String getApplicationDateTimeAfterInterval(int interval){
        String ret1 = "1900-01-01 00:00:00";
        Date date = new Date();
        long time = date.getTime()+interval*60*1000;
        Date data1 = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        ret1 = format.format(data1);
        return ret1;
    }

    /**
     * 日期加减-年数
     * @param dateStr
     * @param off
     * @return
     */
    public static String dateoffYears(String dateStr,String off)throws ParseException{
        String pattern = "yyyy-MM-dd";
        return dateoffYears(dateStr, off, pattern);
    }

    /**
     * 日期加减-年数
     * @param dateStr
     * @param off
     * @return
     */
    public static String dateoffYears(String dateStr,String off,String pattern)throws ParseException{
        return dateOff(OffCycle.YEAR, dateStr, off, pattern);
    }

    /**
     * 日期加减-月数
     * @param dateStr
     * @param off
     * @return
     */
    public static String dateoffMonths(String dateStr,String off)throws ParseException{
        String pattern = "yyyy-MM-dd";
        return dateoffMonths(dateStr, off, pattern);
    }

    /**
     * 日期加减-月数
     * @param dateStr
     * @param off
     * @param pattern
     * @return
     */
    public static String dateoffMonths(String dateStr,String off,String pattern)throws ParseException{
        return dateOff(OffCycle.MONTH, dateStr, off, pattern);
    }

    /**
     * 日期加减-天数
     * @param dateStr
     * @param off
     * @return
     */
    public static String dateoffDays(String dateStr,String off)throws ParseException{
        String pattern = "yyyy-MM-dd";
        return dateoffDays(dateStr, off, pattern);
    }

    /**
     * 日期加减-天数
     * @param dateStr
     * @param off
     * @param pattern
     * @return
     */
    public static String dateoffDays(String dateStr,String off,String pattern)throws ParseException{
        return dateOff(OffCycle.DAY, dateStr, off, pattern);
    }
    /**
     * 日期加减
     * @param cycle   加减周期：DAY,MONTH,YEAR
     * @param dateStr 执行加减的日期："2016-11-01"
     * @param off     日期偏移量："-1"
     * @return
     */
    public static String dateoff(OffCycle cycle,String dateStr,String off)throws ParseException{
        String pattern = "yyyy-MM-dd";
        return dateOff(cycle,dateStr,off,pattern);
    }

    /**
     * 日期加减
     * @param cycle    加减周期：DAY,MONTH,YEAR
     * @param dateStr  执行加减的日期："2016-11-01"
     * @param off      日期偏移量："-1"
     * @param pattern  返回加减后日期的格式："yyyy-MM-dd"
     * @return
     */
    public static String dateOff(OffCycle cycle,String dateStr,String off,String pattern) throws ParseException {
        String pattern1;
        String retDateStr = null;
        String formatStr;
        LocalDate d1,d2;
        Boolean isPosNum;  // 是否为正数
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(off)){
            return retDateStr;
        }

        if (StringUtils.isBlank(pattern)){
            pattern1 = "yyyy-MM-dd";
        }else{
            pattern1 = pattern;
        }

        DateFormat format = new SimpleDateFormat(pattern1);
        formatStr = format.format(format.parse(dateStr));
        d1 = LocalDate.parse(formatStr);
        // 判断正负数
        isPosNum = com.bsoft.nis.util.string.StringUtils.isPositiveNumber(off);
        // 执行日期加减
        switch (cycle){
            case DAY:
                if (isPosNum){
                    d2 = d1.plusDays(Integer.valueOf(off));
                }else{
                    d2 = d1.minusDays(Math.abs(Integer.valueOf(off)));
                }
                break;
            case MONTH:
                if (isPosNum){
                    d2 = d1.plusMonths(Integer.valueOf(off));
                }else{
                    d2 = d1.minusMonths(Math.abs(Integer.valueOf(off)));
                }
                break;
            case YEAR:
                if (isPosNum){
                    d2 = d1.plusYears(Integer.valueOf(off));
                }else{
                    d2 = d1.minusYears(Math.abs(Integer.valueOf(off)));
                }
                break;
            default:
                d2 = null;
                break;
        }

        // 格式化
        retDateStr = d2.toString();
        return retDateStr;
    }

    /**
     * 计算日期差
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Long between(String beginDate,String endDate) throws ParseException {
        String pattern = "yyyy-MM-dd";
        if (StringUtils.isEmpty(beginDate) || StringUtils.isEmpty(endDate)){
            return -1L;
        }
        DateFormat format = new SimpleDateFormat(pattern);
        //字符串转日期
        Date begin = format.parse(beginDate);
        Date end = format.parse(endDate);
        Long diff = end.getTime() - begin.getTime();

        return diff/(1000*60*60*24);
    }

    /**
     * 计算日差
     * @param d1
     * @param d2
     * @return
     */
    public static Long between(java.time.LocalDate d1, java.time.LocalDate d2){
        return d2.toEpochDay() - d1.toEpochDay();
    }

    /**
     * 计算俩日期差，Java8日期处理函数
     * @param start
     * @param end
     * @param betweenEnum
     * @return
     */
    public static Long between(java.time.LocalDateTime start,java.time.LocalDateTime end,DateBetweenEnum betweenEnum)throws IllegalArgumentException{
        Long diff = 0L;
        if (start == null || start.toLocalDate().isEqual(java.time.LocalDate.parse("1900-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            throw new IllegalArgumentException("开始日期无效!");
        if (end == null || end.toLocalDate().isEqual(java.time.LocalDate.parse("1900-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            throw new IllegalArgumentException("结束日期无效!");
        switch (betweenEnum){
            case YEAR:
                diff = betweenOfYear(start,end);
                break;
            case MONTH:
                diff = betweenOfMonth(start,end);
                break;
            case DAY:
                diff = betweenOfDay(start,end);
                break;
            case HOUR:
                diff = betweenOfHour(start,end);
                break;
            case MUNITE:
                break;
        }
        return diff;
    }

    /**
     * 计算俩日期年份差值,Java8日期处理函数
     * @param start
     * @param end
     * @return
     */
    public static Long betweenOfYear(java.time.LocalDateTime start,java.time.LocalDateTime end){
        java.time.LocalDate startD = start.toLocalDate();
        java.time.LocalDate endD = end.toLocalDate();
        return Long.valueOf(endD.getYear() - start.getYear());
    }

    /**
     * 计算俩日期月份差值，Java8日期处理函数
     * @param start
     * @param end
     * @return
     */
    public static Long betweenOfMonth(java.time.LocalDateTime start,java.time.LocalDateTime end){
        return Long.valueOf(end.getYear()*12) + Long.valueOf(end.getMonthValue()) - Long.valueOf(start.getYear()*12) - Long.valueOf(start.getMonthValue());
    }

    /**
     * 计算俩日期天数差值，Java8日期处理函数
     * @param start
     * @param end
     * @return
     */
    public static Long betweenOfDay(java.time.LocalDateTime start,java.time.LocalDateTime end){
        java.time.LocalDate startD = start.toLocalDate();
        java.time.LocalDate endD = end.toLocalDate();
        return Long.valueOf(endD.toEpochDay() - startD.toEpochDay());
    }

    /**
     * 计算俩日期小时差，Java8日期处理函数
     * @param start
     * @param end
     * @return
     */
    public static Long betweenOfHour(java.time.LocalDateTime start,java.time.LocalDateTime end){
        Long startSec = start.toEpochSecond(ZoneOffset.MIN);
        Long endSec = end.toEpochSecond(ZoneOffset.MIN);

        Double difDouble = Math.ceil(Double.valueOf((Double.valueOf(endSec) - Double.valueOf(startSec))/(3600)));
        Long hours = Math.round(difDouble);
        return hours;
    }
}
