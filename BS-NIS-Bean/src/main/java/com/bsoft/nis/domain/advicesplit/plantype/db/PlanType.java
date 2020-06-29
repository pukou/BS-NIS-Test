package com.bsoft.nis.domain.advicesplit.plantype.db;

import java.io.Serializable;

/**
 * Describtion:计划类型(ENR_JHLX)
 * Created: dragon
 * Date： 2016/12/8.
 */
public class PlanType implements Serializable{
    private static final long serialVersionUID = 9024051514660599585L;
    /**
     * 类型号
     */
    public Long LXH;
    /**
     * 归属类型
     */
    public Long GSLX;
    /**
     * 类型名称
     */
    public String LXMC;
    /**
     * 类型顺序
     */
    public Integer LXSX;
    /**
     * 注销标识
     */
    public Integer ZXBZ;
    /**
     * 机构ID
     */
    public String JGID;
}
