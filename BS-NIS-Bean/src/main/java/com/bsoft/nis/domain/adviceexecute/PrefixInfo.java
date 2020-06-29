package com.bsoft.nis.domain.adviceexecute;

/**
 * Description: 条码前缀
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-20
 * Time: 17:07
 * Version:
 */
public class PrefixInfo {

    public String Prefix;

    public String Agency;

    /*升级编号【56010043】============================================= start
                   条码扫描支持长度
            ================= Classichu 2018/03/07 9:34
            */
    public String TMFL;
    public String GZNR;

    public PrefixInfo(String prefix, String agency,String tmfl, String gznr)
    {
        Prefix = prefix;
        Agency = agency;
        TMFL = tmfl;
        GZNR = gznr;
    }
    /* =============================================================== end */

}
