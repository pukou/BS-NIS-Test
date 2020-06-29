package com.bsoft.nis.domain.lifesign;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-26
 * Time: 14:32
 * Version:
 */
public class LifeSignControlItem implements Serializable {
    private static final long serialVersionUID = 564751681705080572L;

    //控件号
    public String KJH;

    //输入项号
    public String SRXH;

    //控件类型(1：显示控件；2：输入控件；3：活动控件；4：下拉控件 5：特殊控件)
    public String KJLX;

    //控件长度-字符长度
    public String KJCD;

    //控件内容
    public String KJNR;

    //数字输入
    public String SZSR;

    //其它输入
    public String QTSR;

    //正常下线
    public String ZCXX;

    //正常上线
    public String ZCSX;

    //监控下线
    public String JKXX;

    //监控上线
    public String JKSX;

    //非法下线
    public String FFXX;

    //非法上线
    public String FFSX;

    //顺序号
    public String SXH;

    //显示类别
    public String XSLB;

    //体征项目号
    public String TZXM;

    //控件说明
    public String KJSM;

    //特殊标识
    public String TSBZ;

    //下拉选择项目
    public List<LifeSignOptionItem> LifeSignOptionItemList;
    //2018-4-24 12:36:09
    public String XMMC;
    public String XMDW;
}
