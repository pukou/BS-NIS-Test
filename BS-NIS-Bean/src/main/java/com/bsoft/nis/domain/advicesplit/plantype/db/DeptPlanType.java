package com.bsoft.nis.domain.advicesplit.plantype.db;

import java.io.Serializable;

/**
 * Describtion:病区计划类型(ENR_KSJH)
 * Created: dragon
 * Date： 2016/12/8.
 */
public class DeptPlanType implements Serializable{
    private static final long serialVersionUID = 5733429861351833746L;
    /**
     * 标识号
     */
    public Long BSH;
    /**
     * 科室代码
     */
    public String KSDM;
    /**
     * 类型号
     */
    public Long LXH;
    /**
     * 是否拆分
     */
    public Integer SFCF;
    /**
     * 周期起始
     */
    public String ZQQS;
    /**
     * 周期结束
     */
    public String ZQJS;
    /**
     * 首日规则
     */
    public Integer SRGZ;
    /**
     * 挺住截止
     */
    public String TZJZ;
    /**
     * 挺住延迟
     */
    public Integer TZYC;
    /**
     * 延迟单位
     */
    public String YCDW;
    /**
     * 监控提前
     */
    public Integer JKTQ;
    /**
     * 监控延后
     */
    public Integer JKYH;
    /**
     * 禁止提前
     */
    public Integer JZTQ;
    /**
     * 禁止延后
     */
    public Integer JZYH;
    /**
     * 机构ID
     */
    public String JGID;
}
