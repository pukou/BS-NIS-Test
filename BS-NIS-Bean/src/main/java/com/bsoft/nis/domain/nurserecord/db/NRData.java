package com.bsoft.nis.domain.nurserecord.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:护理记录(ENR_JL01)
 * Created: dragon
 * Date： 2016/11/24.
 */
public class NRData implements Serializable{

    private static final long serialVersionUID = -5813189211337284188L;

    /// <summary>
    /// 记录编号
    /// </summary>
    public Long JLBH;

    /// <summary>
    /// 住院号
    /// </summary>
    public String ZYH;

    /// <summary>
    /// 结构编号
    /// </summary>
    public String JGBH;

    /// <summary>
    /// 记录名称
    /// </summary>
    public String JLMC;

    /// <summary>
    /// 病历类型
    /// </summary>
    public String BLLX;

    /// <summary>
    /// 病历类别
    /// </summary>
    public String BLLB;

    /// <summary>
    /// 模板类别
    /// </summary>
    public String MBLB;

    /// <summary>
    /// 模板编号
    /// </summary>
    public String MBBH;

    /// <summary>
    /// 段落类别
    /// </summary>
    public String DLLB;

    /// <summary>
    /// 段落键
    /// </summary>
    public String DLJ;

    /// <summary>
    /// 记录行数
    /// </summary>
    public String JLHS;

    /// <summary>
    /// 换页标志
    /// </summary>
    public String HYBZ;

    /// <summary>
    /// 记录时间
    /// </summary>
    public String JLSJ;

    /// <summary>
    /// 书写时间
    /// </summary>
    public String SXSJ;

    /// <summary>
    /// 系统时间
    /// </summary>
    public String XTSJ;

    /// <summary>
    /// 书写病区
    /// </summary>
    public String SXBQ;

    /// <summary>
    /// 书写护士
    /// </summary>
    public String SXHS;

    /// <summary>
    /// 完成签名
    /// </summary>
    public String WCQM;

    /// <summary>
    /// 完成时间
    /// </summary>
    public String WCSJ;

    /// <summary>
    /// 审阅标志
    /// </summary>
    public String SYBZ;

    /// <summary>
    /// 审阅时间
    /// </summary>
    public String SYSJ;

    /// <summary>
    /// 审阅护士
    /// </summary>
    public String SYHS;

    /// <summary>
    /// 审阅签名
    /// </summary>
    public String SYQM;

    /// <summary>
    /// 打印标志
    /// </summary>
    public String DYBZ;

    /// <summary>
    /// 记录状态
    /// </summary>
    public String JLZT;

    /// <summary>
    /// 总结编号
    /// </summary>
    public String ZJBH;

    /// <summary>
    /// 总结名称
    /// </summary>
    public String ZJMC;

    /// <summary>
    /// 总结类型
    /// </summary>
    public String ZJLX;

    /// <summary>
    /// 开始时间
    /// </summary>
    public String KSSJ;

    /// <summary>
    /// 结束时间
    /// </summary>
    public String JSSJ;

    /// <summary>
    /// 独立换行标示
    /// </summary>
    public String DLHHBZ;

    /// <summary>
    /// 机构ID
    /// </summary>
    public String JGID;

    /// <summary>
    /// 项目列表
    /// </summary>
    public List<NRItem> XMLB = new ArrayList<>(); //项目列表
    @JsonIgnore
    public String dbtype;
    public NRData(){
        BLLX = "0";
        MBBH = "0";
        DLLB = "0";
        DLJ = "";
        JLHS = "0";
        HYBZ = "0";
        WCQM = "0";
        SYBZ = "0";
        SYHS = "";
        SYQM = "0";
        DYBZ = "0";
        JLZT = "0";
        ZJBH = "";
        ZJMC = "";
        ZJLX = "0";
        DLHHBZ = "0";
        JGID = "1";
    }
}
