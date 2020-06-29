package com.bsoft.nis.domain.lifesign;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 15:13
 * Version:
 */
public class LifeSignQualityInfo {

    public String BRLX;

    public String TZXM;

    public String NLXX;

    public String NLSX;

    public String ZCSX;

    public String ZCXX;

    public String FFXX;

    public String FFSX;

    public boolean Compare(String tzxm, String brlx, String brnl) {
        /*if (TZXM != tzxm) {
            return false;
        }
        if (brlx != BRLX) {
            return false;
        }
        if (brlx == "2") {
            return true;
        }
        brnl = brnl.substring(0, brnl.length() - 1);*/
        //======字符串相等判断要使用用equals mod by mdw at 2018.10.09
        if(!(TZXM == null ? "" : TZXM).trim().equals(tzxm)) return false;
        if(!(BRLX == null ? "" : BRLX).trim().equals(brlx)) return false;
        if("2".equals(brlx)) return true;

        //年龄为null及带有汉字时处理 mod by mdw at 2019.7.25
        //brnl = brnl.trim();
        if(brnl == null) brnl = "0";
        brnl = brnl.replaceAll("(\\d{1,3})[\" \"]*岁", "$1").trim();

        //==========================================================================
        return Integer.parseInt(NLXX) <= Integer.parseInt(brnl) && Integer.parseInt(brnl) <= Integer.parseInt(NLSX);

    }
}
