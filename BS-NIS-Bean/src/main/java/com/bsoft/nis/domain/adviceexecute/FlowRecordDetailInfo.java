package com.bsoft.nis.domain.adviceexecute;

/**
 * Description: 对应单子明细的信息
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-20
 * Time: 11:22
 * Version:
 */
public class FlowRecordDetailInfo {

    /**
     *  医嘱序号
     */
    public String YZXH;

    /**
     *  医嘱名称
     */
    public String YZMC;

    /**
     * 条码编号
     */
    public String TMBH;

    /**
     *  剂量信息
     */
    public String JLXX;

    /**
     * 数量信息
     */
    public String SLXX;

    /**
     * 确认单号
     */
    public String QRDH;
    //add  2018-5-17 15:25:36
    public String JHSJ;
    public String YZZH;

    /**
     * 剂量单位
     */
    public String JLDW;

    /**
     * 数量单位
     */
    public String SLDW;

    /********************口服特有属性*******************/

    /**
     *  药袋描述
     */
    public String YDMS;

    /**
     * 口服明细
     */
    public String KFMX;

    /**
     * 包装剂量
     */
    public String BZJL;

    /**
     * 包装数量
     */
    public String BZSL;

    /********************输液特有属性*******************/

    /**
     *  输液时间
     */
    public String SYSJ;

    /**
     * 一次剂量
     */
    public String YCJL;

    /**
     * 一次数量
     */
    public String YCSL;


}
