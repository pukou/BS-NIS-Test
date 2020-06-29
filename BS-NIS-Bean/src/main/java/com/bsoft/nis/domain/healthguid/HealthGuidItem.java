package com.bsoft.nis.domain.healthguid;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-11-07
 * Time: 13:49
 * Version:
 */
public class HealthGuidItem implements Serializable {
    private static final long serialVersionUID = 564751681705080572L;

    //序号 IENR_JKXJJL主键
    public String XH;

    //类型编号：样式序号/归类序号
    public String LXBH;

    //描述
    public String MS;

    //宣教时间
    public String XJSJ;

    //数量：单位是份
    public String SL;
}
