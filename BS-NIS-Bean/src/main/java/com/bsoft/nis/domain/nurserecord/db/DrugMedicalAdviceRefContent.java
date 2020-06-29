package com.bsoft.nis.domain.nurserecord.db;

import java.io.Serializable;

/**
 * Describtion:过敏药物医嘱引用内容
 * Created: dragon
 * Date： 2016/11/28.
 */
public class DrugMedicalAdviceRefContent extends RefContent implements Serializable{

    private static final long serialVersionUID = -479125943133075048L;

    /**
     * 开嘱时间
     */
    public String KZSJ;

    /**
     * 停嘱时间
     */
    public String TZSJ;

    /**
     * 使用频次
     */
    public String SYPC;

    /**
     * 一次剂量
     */
    public String YCJL;

    /**
     * 药品用法
     */
    public String YPYF;

    /**
     * 医嘱名称
     */
    public String YZMC;

    /**
     * 医嘱组号
     */
    public String YZZH;

    /**
     * 剂量单位
     */
    public String JLDW;

    /**
     * 药品序号
     */
    public String YPXH;
    //add 2018-6-21 15:39:53
    public String LSYZ;
    //

}
