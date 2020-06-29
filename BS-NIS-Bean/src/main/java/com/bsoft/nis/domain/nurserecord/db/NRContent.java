package com.bsoft.nis.domain.nurserecord.db;

import java.io.Serializable;

/**
 * Describtion:护理记录目录 ENR_JLML
 * Created: dragon
 * Date： 2016/11/25.
 */
public class NRContent implements Serializable{

    private static final long serialVersionUID = 5614736350353420237L;
    /**
     * 记录序号
     */
    public Long JLXH;
    /**
     * 住院号
     */
    public Long ZYH;
    /**
     * 病历类型
     */
    public Integer BLLX;
    /**
     * 病例类别
     */
    public Long BLLB;
    /**
     * 结构编号
     */
    public Long JGBH;
    /**
     * 记录页数
     */
    public Integer JLYS;
    /**
     * 排列次序
     */
    public Integer PLCX;
    /**
     * 机构ID
     */
    public String JGID;
}
