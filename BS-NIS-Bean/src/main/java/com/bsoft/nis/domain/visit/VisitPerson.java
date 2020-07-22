package com.bsoft.nis.domain.visit;

/**
 *巡视病人
 * Created by king on 2016/11/18.
 */
public class VisitPerson {

    /**
     * 住院号
     */
    public String ZYH;
    /**
     * 病人姓名
     */
    public String BRXM;

    /**
     * 病人床位
     */
    public String BRCH;

    /**
     * 护理级别
     */
    public String HLJB;

    /**
     * 巡视标识
     */
    public String XSBS;
    /**
     * 巡视时间
     */
    public String XSSJ;
    /**
     * 类型标识
     */
    public int XSQK;

    /**
     * true为扫描选中，false是手动点击
     */
    public boolean isScanSlt;

    /**
     * 显示床号
     */
    public String XSCH;
}
