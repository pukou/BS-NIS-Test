package com.bsoft.nis.domain.adviceexecute;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 口服相关所有信息
 *  为了方便读取数据库的数据-减少读取次数
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-19
 * Time: 17:48
 * Version:
 */
public class OralModelInfo {

    /**
     * 口服单号
     */
    public String KFDH;

    /**
     * 住院号
     */
    public String ZYH;

    /**
     * 药品用法
     */
    public String YPYF;

    /**
     * 口服时间
     */
    public String KFSJ;

    /******************口服包装属性******************/

    /**
     * 口服明细
     */
    public String KFMX;

    /**
     * 条码编号
     */
    public String TMBH;

    /**
     * 药袋描述
     */
    public String YDMS;

    /**
     * 口服状态
     */
    public String KFZT;

    /**
     * 执行时间
     */
    public String ZXSJ;

    /**
     * 执行工号
     */
    public String ZXGH;

    /**
     * 核对时间
     */
    public String HDSJ;

    /**
     * 核对工号
     */
    public String HDGH;

    /**
     * 作废标志
     */
    public String ZFBZ;

    /******************包装明细属性******************/

    /**
     * 包装明细
     */
    public String BZMX;

    /**
     * 医嘱序号
     */
    public String YZXH;

    /**
     * 药品序号
     */
    public String YPXH;

    /**
     * 包装剂量
     */
    public String BZJL;

    /**
     * 剂量单位
     */
    public String JLDW;

    /**
     * 包装数量
     */
    public String BZSL;

    /**
     * 数量单位
     */
    public String SLDW;

    /**
     * 时间编号
     */
    public String SJBH;

    /**
     * 时间名称
     */
    public String SJMC;
}
