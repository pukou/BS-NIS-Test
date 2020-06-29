package com.bsoft.nis.domain.nurserecord.db;

import java.io.Serializable;

/**
 * Describtion:护理记录结构表（ENR_JG01）
 * Created: dragon
 * Date： 2016/10/20.
 */
public class Template implements Serializable{
    private static final long serialVersionUID = 685674213471916090L;
    /**
     * 结构编号
     */
    public String JGBH;

    /**
     * 结构名称
     */
    public String JGMC;

    /**
     * 所属科室
     */
    public String SSKS;

    /**
     * 病历类别
     */
    public String BLLB;

    /**
     * 病历类别名称
     */
    public String BLLBMC;

    /**
     * 模板类别
     */
    public String MBLB;

    /**
     * 模板类别名称
     */
    public String MBLBMC;

    /**
     * 是否独立页码
     */
    public String DLYM;

    /**
     * 备注信息
     */
    public String BZXX;
}
