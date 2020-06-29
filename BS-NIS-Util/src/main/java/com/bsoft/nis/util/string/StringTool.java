package com.bsoft.nis.util.string;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class StringTool {
    public static String replace_zero(String src){
        //最后为 . 去掉 . 后直接返回
        if (src.endsWith(".")){
            src =  src.substring(0, src.length() - 1);
            return src;
        }
        //最后是 0  去掉 0
        if (src.endsWith("0")){
            src = src.substring(0, src.length() - 1);
            return replace_zero(src);
        }
        return src;
    }
    //保留 X 位小数 修正 尾部的 0 和整数时的 .
    public static String parseDecimalStrXFixed(String src,DecimalFormat decimalFormat) {
        String result = parseDecimalStrX(src,decimalFormat);
        return replace_zero(result);
    }
    //保留 2 位小数 修正 尾部的 0 和整数时的 .
    public static String parseDecimalStr2Fixed(String src) {
        String result = parseDecimalStr2(src);
        return replace_zero(result);
    }
    //保留 2 位小数
    public static String parseDecimalStr2(String src) {
        return parseDecimalStrX(src, null);
    }

    //保留 X 位小数
    public static String parseDecimalStrX(String src,DecimalFormat decimalFormat) {
        if (src==null||src.trim().isEmpty()){
            return "";
        }
        String result = "0.00";
        //"0.00" "0.##"    # 不存在留空 0 不存在为0
        decimalFormat =decimalFormat==null? new DecimalFormat("0.00"):decimalFormat;
        BigDecimal bigDecimal = new BigDecimal(src);
        try {
            result = decimalFormat.format(bigDecimal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
