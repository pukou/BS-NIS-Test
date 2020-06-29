package com.bsoft.nis.domain.nurserecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:记录目录树
 * Created: dragon
 * Date： 2016/11/21.
 */
public class NRTree implements Serializable{

    private static final long serialVersionUID = -4206118002295930547L;

    /**
     * 记录编号
     */
    public String JLBH;

    /**
     * 结构编号
     */
    public String JGBH;

    /**
     * 类别编号
     */
    public String LBBH;

    /**
     * 显示内容
     */
    public String XSNR;

    /**
     * 记录时间，格式2014-04-17 15:07:00
     */
    public String JLSJ;

    /**
     * 完成标志 1标志已签名
     */
    public Boolean WCZT;

    /**
     * 审核标志
     */
    public Boolean SHZT;

    /**
     * 是否选中
     */
    public Boolean SFXZ;

    /**
     * 是否一天中的第一条记录
     */
    public Boolean DOFR;
    //ADD
    public String SXHS;
    /**
     * 子记录列表(多层)
     */
    public List<NRTree> ZML = new ArrayList<>();
}
