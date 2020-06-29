package com.bsoft.nis.domain.nurserecord.db;

import java.io.Serializable;

/**
 * Describtion:病历目录(EMR_MLLB)
 * Created: dragon
 * Date： 2016/11/23.
 */
public class ContentCategory implements Serializable{
    /**
     * 目录标号
     */
    public String MLBH;
    /**
     * 目录编码
     */
    public String MLBM;
    /**
     * 目录类别
     */
    public String MLLB;
    /**
     * 目录名称
     */
    public String MLMC;
    /**
     * 执行标识
     */
    public Integer ZXBZ;
}
