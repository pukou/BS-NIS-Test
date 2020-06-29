package com.bsoft.nis.domain.advicesplit.plantype.db;

import java.io.Serializable;

/**
 * Describtion:计划类型明细
 * Created: dragon
 * Date： 2016/12/8.
 */
public class PlanTypeDetail implements Serializable{
    private static final long serialVersionUID = 3626655816340817493L;
    /**
     * 明细号
     */
    public Long MXH;
    /**
     * 类型号
     */
    public Long LXH;
    /**
     * 明细类型 1：收费项目；2：医疗收费；3 ：特殊医嘱；4：药品用法
     */
    public Integer MXLX;
    /**
     * 明细序号
     * 当明细类型为1：收费项目时，保存收费项目号
     * 当明细类型为2：医疗收费时，保存费用序号
     * 当明细类型为3：特殊医嘱时，保存特殊医嘱的医嘱序号
     * 当明细类型为4：医嘱的药品用法
     */
    public Long MXXH;
}
