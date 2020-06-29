package com.bsoft.nis.domain.core.synchron.rules;

import java.io.Serializable;

/**
 * Describtion:同步明细(IENR_TBMBLY)
 * Created: dragon
 * Date： 2016/12/28.
 */
public class SynchronSource implements Serializable{
    private static final long serialVersionUID = 156207512466419164L;
    /**
     * 明细序号
     */
    public Long MXXH  ;
    /**
     * 目标序号
     */
    public Long MBXH  ;
    /**
     * 同步序号
     */
    public Long TBXH  ;
    /**
     * 数据类型
     1: 静态
     2: 动态
     3: 特殊 （病人姓名，护士姓名，记录时间，病区名称）
     */
    public Integer SJLX  ;
    /**
     * 项目类型
     */
    public Integer XMLX  ;
    /**
     * 来源项目
     */
    public Long LYXM  ;
    /**
     * 来源项目名称
     */
    public String LYXMMC;
    /**
     * 静态内容
     */
    public String JTNR  ;
    /**
     * 排列顺序
     */
    public Integer PLSX  ;
}
