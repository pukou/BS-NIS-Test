package com.bsoft.nis.util.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Describtion:字符串工具类
 * Created: dragon
 * Date： 2016/10/24.
 */
public class StringUtils {

    /**
     * 根据传入的正则字符串，判断结果
     * @param str
     * @param regex
     * @return
     */
    public static Boolean isTrue(String str,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 判断字符串是否是正数数字
     * 如：999,100 返回true
     *     -100 返回false
     * @param str
     * @return
     */
    public static Boolean isPositiveNumber(String str){
        String regex = "[0-9]*";
        Boolean is = isTrue(str, regex);
        return is;
    }

    /**
     * 判断字符串是否是负数数字
     * 如：19 返回false
     *     -1 返回ture
     * @param str
     * @return
     */
    public static Boolean isMinusNumber(String str){
        String regex = "-?[0-9]+";
        Boolean is = isTrue(str,regex);
        return is;
    }

    /**
     * 判断字符串是否为数字
     * 999 返回true
     * -10 返回true
     * @param str
     * @return
     */
    public static Boolean isNumber(String str){
        String regex = "-?[0-9]+.?[0-9]+";
        Boolean is = isTrue(str,regex);
        return is;
    }
}
