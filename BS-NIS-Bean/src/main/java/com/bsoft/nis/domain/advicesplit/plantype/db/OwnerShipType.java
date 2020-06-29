package com.bsoft.nis.domain.advicesplit.plantype.db;

import java.io.Serializable;

/**
 * Describtion:归属类型(ENR_GSLX)
 * Created: dragon
 * Date： 2016/12/8.
 */
public class OwnerShipType implements Serializable{

    private static final long serialVersionUID = 6149617337801558095L;
    /**
     * 归属类型
     */
    public Long GSLX;
    /**
     * 归属名称
     */
    public String GSMC;
    /**
     * 执行规则
     */
    public Long ZXGZ;
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
}
