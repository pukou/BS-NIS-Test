package com.bsoft.nis.util.date;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Describtion:日期比较
 * Created: dragon
 * Date： 2016/11/21.
 */
public class DateCompare {

    /**
     * 俩个日期字符串比较大小
     * @param firstStr
     * @param nextStr
     * @return
     * @throws ParseException
     */
    public static Integer compare(String firstStr,String nextStr) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return compare(firstStr,nextStr,pattern);
    }
    /**
     * 俩个日期字符串比较大小
     * @param firstStr
     * @param nextStr
     * @return  0 相等（俩者相等） 1 大于（前者大于后者） -1 小于（前者小于后者）
     */
    public static Integer compare(String firstStr,String nextStr,String pattern) throws ParseException {
        String _pattern ;
        if (StringUtils.isEmpty(pattern)){
            _pattern = "yyyy-MM-dd HH:mm:ss";
        }else{
            _pattern = pattern;
        }
        SimpleDateFormat format = new SimpleDateFormat(_pattern);
        //字符串转日期
        Date date1 = format.parse(firstStr);
        Date date2 = format.parse(nextStr);

        if (date1.getTime()> date2.getTime()){
            return 1;
        }else if (date1.getTime() == date2.getTime()){
            return 0;
        }else if (date1.getTime() < date2.getTime()){
            return -1;
        }else{
            return null;
        }
    }

    /**
     * 比较俩个日期字符串是否相等
     * @param firstStr
     * @param nextStr
     * @return
     * @throws ParseException
     */
    public static Boolean isEqual(String firstStr,String nextStr) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return isEqual(firstStr,nextStr,pattern);
    }
    /**
     * 比较俩个日期字符串是否相等
     * @param firstStr
     * @param nextStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Boolean isEqual(String firstStr,String nextStr,String pattern) throws ParseException {
        if (StringUtils.isEmpty(firstStr) || StringUtils.isEmpty(nextStr)){
            return false;
        }
        return compare(firstStr, nextStr, pattern) == 0;
    }

    /**
     * 比较前者日期是否小于后者日期
     * @param firstStr
     * @param nextStr
     * @return
     * @throws ParseException
     */
    public static Boolean isLessThan(String firstStr,String nextStr) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return isLessThan(firstStr,nextStr,pattern);
    }
    /**
     * 比较前者日期是否小于后者日期
     * @param firstStr
     * @param nextStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Boolean isLessThan(String firstStr,String nextStr,String pattern) throws ParseException {
        if (StringUtils.isEmpty(firstStr) || StringUtils.isEmpty(nextStr)){
            return false;
        }
        return compare(firstStr, nextStr, pattern) == -1;
    }

    /**
     * 比较前者日期是否大于后者日期
     * @param firstStr
     * @param nextStr
     * @return
     * @throws ParseException
     */
    public static Boolean isGreaterThan(String firstStr,String nextStr) throws ParseException {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return isGreaterThan(firstStr,nextStr,pattern);
    }

    /**
     * 比较前者日期是否大于后者日期
     * @param firstStr
     * @param nextStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Boolean isGreaterThan(String firstStr,String nextStr,String pattern) throws ParseException {
        if (StringUtils.isEmpty(firstStr) || StringUtils.isEmpty(nextStr)){
            return false;
        }
        return compare(firstStr, nextStr, pattern) == 1;
    }
}
