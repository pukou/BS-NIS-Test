package com.bsoft.nis.domain.nurserecord.db;

import java.io.Serializable;
import java.util.List;

/**
 * Describtion:护理记录助手
 * Created: dragon
 * Date： 2016/11/23.
 */
public class HelpTree implements Serializable{
    private static final long serialVersionUID = -5748583915975738003L;
    /**
     * 助手内容
     */
    public String ZSNR;

    /**
     * 目录编号
     */
    public String MLBH;

    /**
     * 父目录编号
     */
    public String FLBH;

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
     * 子目录项目为内容对象本身(即为多层)
     */
    public List<HelpTree> Items;
    public List<HelpLeaf> helpLeafList;

}
