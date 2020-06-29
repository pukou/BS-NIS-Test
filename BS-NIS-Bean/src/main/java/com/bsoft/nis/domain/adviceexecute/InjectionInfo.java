package com.bsoft.nis.domain.adviceexecute;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 注射单
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-19
 * Time: 17:38
 * Version:
 */
public class InjectionInfo implements IRecordInfo {

    /**
     * 注射单号
     */
    public String ZSDH;

    /**
     * 住院号
     */
    public String ZYH;

    /**
     * 条码编号
     */
    public String TMBH;

    /**
     * 医嘱组号
     */
    public String YZZH;

    /**
     * 药品用法
     */
    public String YPYF;

    /**
     * 注射开始时间
     */
    public String ZSSJ;

    /**
     * 时间编号
     */
    public String SJBH;

    /**
     * 时间名称
     */
    public String SJMC;

    /**
     * 注射状态
     */
    public String ZSZT;

    /**
     * 注射时间
     */
    public String ZXSJ;

    /**
     * 执行工号
     */
    public String ZXGH;

    /**
     * 结束时间
     */
    public String JSSJ;

    /**
     * 结束工号
     */
    public String JSGH;

    /**
     * 注射核对时间
     */
    public String ZXHDSJ;

    /**
     * 注射核对工号
     */
    public String ZXHDGH;

    /**
     * 结束核对时间
     */
    public String JSHDSJ;

    /**
     * 结束核对工号
     */
    public String JSHDGH;

    /**
     * 摆药核对标志
     */
    public String BYHDBZ;

    /**
     * 加药核对标志
     */
    public String JYHDBZ;

    /**
     * 注射明细
     */
    public List<InjectionDetailInfo> Details = new ArrayList<>();

    /**
     * 机构id
     */
    public String JGID;

    /**
     * 数据库类型
     */
    public String dbtype;
}
