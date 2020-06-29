package com.bsoft.nis.domain.adviceexecute;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 口服单
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-19
 * Time: 17:48
 * Version:
 */
public class OralInfo implements IRecordInfo {

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

    /**
     * 口服包装列表
     */
    public List<OralPackageInfo> Packages = new ArrayList<>();

    /**
     * 机构id
     */
    public String JGID;

    /**
     * 数据库类型
     */
    public String dbtype;
}
