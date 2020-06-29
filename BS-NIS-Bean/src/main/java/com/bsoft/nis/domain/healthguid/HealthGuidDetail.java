package com.bsoft.nis.domain.healthguid;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-11-08
 * Time: 09:53
 * Version:
 */
public class HealthGuidDetail implements Serializable {

    //宣教项目表主键（XMXH）序号
    public String XH;

    //宣教记录项目表主键（JLXM）
    public String JLXM;

    //宣教记录序号
    public String JLXH;

    //归类序号
    public String GLXH;

    //自定义标志
    public String ZDYBZ;

    //描述
    public String MS;

    //是否选中 默认不选中
    public String ISCHECK = "0";

    //宣教时间
    public String XJSJ;

    //宣教工号
    public String XJGH;

    //宣教对象
    public String XJDX;

    //宣教方式
    public String XJFS;

    //宣教评价
    public String XJPJ;

    //独立标志 0：不独立 1：独立
    public String DLBZ;

    //项目组号 独立项目组号：0 不独立项目组号：自定义
    public String XMZH;

    //评价时间
    public String PJSJ;

    //评价工号
    public String PJGH;

    //是否允许操作 0：允许操作 1：不允许操作
    public String ISOPER = "0";

    //宣教的操作（对象，方式，评价等信息）
    public List<HealthGuidOper> HealthGuidOpers;
}
