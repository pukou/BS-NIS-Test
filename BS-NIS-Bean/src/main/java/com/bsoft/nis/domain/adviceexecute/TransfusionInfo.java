package com.bsoft.nis.domain.adviceexecute;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 输液单
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-19
 * Time: 17:23
 * Version:
 */
public class TransfusionInfo implements IRecordInfo {

    /**
     * 输液单号
     */
    public String SYDH;

    /**
     * 药品用法
     */
    public String YPYF;

    /**
     * 使用频次
     */
    public String SYPC;

    /**
     * 医嘱组号
     */
    public String YZZH;

    /**
     * 输液时间
     */
    public String SYSJ;

    /**
     * 来源标识
     */
    public String LYBS;

    /**
     * 开始时间
     */
    public String KSSJ;

    /**
     * 开始工号
     */
    public String KSGH;

    /**
     * 结束时间
     */
    public String JSSJ;

    /**
     * 结束工号
     */
    public String JSGH;

    /**
     * 执行单号
     */
    public String ZXDH;

    /**
     * 首瓶标志
     */
    public String SPBZ;

    /**
     * 末瓶标志
     */
    public String MPBZ;

    /**
     * 开始核对时间
     */
    public String KSHDSJ;

    /**
     * 开始核对工号
     */
    public String KSHDGH;

    /**
     * 结束核对时间
     */
    public String JSHDSJ;

    /**
     * 结束核对工号
     */
    public String JSHDGH;

    /**
     * 输液状态
     * 0 未执行
     * 1 已经执行
     * 2 正在执行
     * 4 暂停
     */
    public String SYZT;

    /**
     * 住院号
     */
    public String ZYH;

    /**
     * 时间编号
     */
    public String SJBH;

    /**
     * 时间名称
     */
    public String SJMC;

    /**
     * 摆药核对标志
     */
    public String BYHDBZ;

    /**
     * 加药核对标志
     */
    public String JYHDBZ;

    /**
     * 输液明细列表
     */
    public List<TransfusionDetailInfo> Details = new ArrayList<>();

    /**
     * 机构id
     */
    public String JGID;

    /**
     * 数据库类型
     */
    public String dbtype;

	/**
	 * 平均滴速
	 */
	public String PJDS;
}
