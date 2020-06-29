package com.bsoft.nis.domain.bloodtransfusion;

import java.io.Serializable;

/**
 * Description:输血相关信息
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-11-29
 * Time: 14:43
 * Version:
 */
public class BloodTransfusionInfo implements Serializable {
    private static final long serialVersionUID = 564751681705080572L;

    //申请单号
    public String SQDH;

    //输血单号
    public String SXDH;

    //血液类型
    public String XYLX;

    //名称
    public String MC;

    //血袋号
    public String XDH;

    //血袋序号
    public String XDXH;

    //ABO血型
    public String ABO;

    //RH血型
    public String RH;

    //血量
    public String XL;

    //包装
    public String BAOZHUANG;

    //预约日期
    public String YYRQ;

    //到期日期
    public String DQRQ;

    //配血结果
    public String PXFF;

    //住院号吗
    public String ZYHM;

    //病人姓名
    public String BRXM;

    //病人性别
    public String BRXB;

    //病人年龄
    public String BRNL;

    //病人床号
    public String BRCH;

    //输血人1- 执行人
    public String SXR1;

    //输血人2- 核对人
    public String SXR2;

    //输血时间
    public String SXSJ;

    //结束人
    public String JSR;

    //结束时间
    public String JSSJ;

    //结束时间
    public String KSSJ;

    /**
     * 输血判别
     */
    public String SXPB;

    /**
     * 上交判别
     */
    public String SJPB;

    /**
     * 回收判别
     */
    public String HSPB;
}
