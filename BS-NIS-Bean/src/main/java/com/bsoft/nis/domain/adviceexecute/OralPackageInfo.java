package com.bsoft.nis.domain.adviceexecute;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 口服包装
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-19
 * Time: 17:48
 * Version:
 */
public class OralPackageInfo {

    /**
     * 口服单号
     */
    public String KFDH;

    /**
     * 口服明细
     */
    public String KFMX;

    /**
     * 条码编号
     */
    public String TMBH;

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

    /**
     * 口服明细
     */
    public List<OralDetailInfo> Details=new ArrayList<>();

    /**
     * 机构id
     */
    public String JGID;

    /**
     * 数据库类型
     */
    @JsonIgnore
    public String dbtype;
}
