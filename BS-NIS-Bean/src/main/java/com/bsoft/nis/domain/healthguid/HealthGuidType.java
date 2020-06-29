package com.bsoft.nis.domain.healthguid;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-11-08
 * Time: 09:51
 * Version:
 */
public class HealthGuidType implements Serializable {

    //类型编号
    public String LXBH;

    //样式序号 表单类型特有字段
    public String YSXH;

    //描述
    public String MS;

    //是否选中 默认不选中
    public String ISCHECK = "0";

    //宣教项目
    public List<HealthGuidDetail> HealthGuidDetails;
}
