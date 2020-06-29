package com.bsoft.nis.domain.pivas;

/**
 * Created by huangjiaroro on 2017/2/15.
 * 解析xml类 输液明细
 *
 */
public class PivasTransDetail {



    /// 医嘱序号
    public String adviceid;

    /// 药品序号
    public String medid;

    /// 医嘱主项
    public String advicemainflag;

    /// 一次剂量
    public String doseage;

    /// 剂量单位
    public String doseunit;

    /// 一次数量
    public String quantity;

    /// 数量单位
    public String quantityunit;

    public Long SYMX;

    public String SYDH;
}
