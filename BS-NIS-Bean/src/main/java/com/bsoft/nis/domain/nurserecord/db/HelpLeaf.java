package com.bsoft.nis.domain.nurserecord.db;

import java.io.Serializable;

/**
 * Describtion:助手目录树
 * Created: dragon
 * Date： 2016/11/23.
 */
public class HelpLeaf implements Serializable{
    private static final long serialVersionUID = -4143811312567415140L;
    /**
     * 助手编号
     */
    public String ZSBH;

    /**
     * 助手名称
     */
    public String ZSMC;

    /**
     * 目录名称
     */
    public String MLMC;

    /**
     * 目录编号
     */
    public String MLBH;

    /**
     * 助手内容
     */
    public String ZSNR;
}
