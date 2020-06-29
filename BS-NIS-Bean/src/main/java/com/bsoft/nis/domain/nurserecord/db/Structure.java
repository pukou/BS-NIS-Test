package com.bsoft.nis.domain.nurserecord.db;

import java.io.Serializable;

/**
 * Describtion: 护理记录-病历类别
 * Created: dragon
 * Date： 2016/10/20.
 */
public class Structure implements Serializable{
    private static final long serialVersionUID = -9079728156542371880L;

    /**
     * 类别编号
     */
    public String LBBH;// 类别编号

    /**
     * 约定类别编码
     */
    public String YDLBBM;

    /**
     * 类别名称
     */
    public String LBMC;// 类别名称

    /**
     * 类别级别
     */
    public String LBJB;// 类别级别

    /**
     * 上级类别编号
     */
    public String SJLBBH ;

    /**
     * 显示名称
     */
    public String XSMC;
}
