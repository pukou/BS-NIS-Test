package com.bsoft.nis.domain.healthguid;

import com.bsoft.nis.domain.SyncRecord;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-11-08
 * Time: 09:44
 * Version:
 */
public class HealthGuidEvaluateData implements Serializable {
    private static final long serialVersionUID = 564751681705080572L;

    //宣教记录表主键（JLXH）
    public String XH;

    //宣教样式序号/宣教项目归类子项序号
    public String GLXH = "0";

    //数据类型 1：表单 2：分类
    public String GLLX = "1";

    //宣教类别列表
    public List<HealthGuidType> HealthGuidTypes;

    //宣教的默认操作（对象，方式，评价等信息）
    public List<HealthGuidOper> HealthGuidDefaultOpers;


}
