package com.bsoft.nis.domain.nurserecord.db;

import java.io.Serializable;

/**
 * Describtion:护理记录明细表(ENR_JL02)
 * Created: dragon
 * Date： 2016/11/24.
 */
public class NRItem implements Serializable{

    private static final long serialVersionUID = 6125654035140495950L;

    public Long MXBH;//明细编号

    public Long JLBH;//记录编号

    public String XMBH;//项目编号

    public String XSMC;//显示名称

    public String XMMC;//项目名称

    public String XMQZ;//项目取值

    public Integer KSLH;//开始列号

    public Integer JSLH;//结束列号

    public String HDBZ;//活动标志

    public String XGBZ;//修改标志

    public String HHJG;//换行间隔

    public String YMCLFS;//页面处理方式

    public String JGID ; // 机构ID

    /**
     * 来源表单
     */
    public String LYBD = "0";
    /**
     * 来源标号
     */
    public String LYBH = "0";
    /**
     * 来源明细
     */
    public String LYMX = "0";
    /**
     * 来源明细类型
     */
    public String LYMXLX = "0";
}
