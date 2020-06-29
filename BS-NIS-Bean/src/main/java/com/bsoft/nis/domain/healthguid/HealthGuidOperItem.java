package com.bsoft.nis.domain.healthguid;

import java.io.Serializable;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-11-08
 * Time: 09:50
 * Version:
 */
public class HealthGuidOperItem implements Serializable {

    /// 序号
    public String XH;

    //描述
    public String MS;

    //是否选中 默认不选中
    public String ISCHECK = "0";
}
