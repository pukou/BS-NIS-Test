package com.bsoft.nis.domain.healthguid;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-11-07
 * Time: 13:47
 * Version:
 */
public class HealthGuid implements Serializable {
    private static final long serialVersionUID = 564751681705080572L;

    //序号
    public String XH;

    //数量
    public String SL;

    //描述
    public String MS;

    //数据类型 1：表单 2：归类
    public String GLLX;

    //宣教项目
    public List<HealthGuidItem> HealthGuidItems;


}
