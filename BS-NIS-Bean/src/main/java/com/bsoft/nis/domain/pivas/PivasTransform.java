package com.bsoft.nis.domain.pivas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjiaroro on 2017/2/15.
 * xml解析类 输液主项
 */
public class PivasTransform {


    ///  住院号
    public String patientid;

    /// 住院号码
    public String patientno;

    /// 病人病区
    public String patientareaid;

    /// 病人床号
    public String patientbedid;

    /// 条码编号
    public String barcodeno;

    /// 药品用法
    public String medusage;

    /// 使用频次
    public String medfreq;

    /// 医嘱组号
    public String advicegrpno;

    /// 输液日期
    public String plandate;

    /// 输液时间
    public String plantime;

    /// 时间编号
    public String timeno;

    /// 时间名称
    public String timedesc;

    /// 配液标志
    public String makeflag;

    /// 配液时间
    public String maketime;

    /// 配液工号
    public String makeuserno;

    /// 配液来源
    public String sourcefrom;

    /// 来源标识
    public String sourcefromid;

    /// 打印次数
    public String printnum;

    /// 打印时间
    public String printtime;

    /// 打印工号
    public String printuserno;

    /// JGID
    public String jgid;

    public List<PivasTransDetail> transdetails = new ArrayList<>();

    public Long SYDH;
}
