package com.bsoft.nis.domain.nurseplan;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 护理计划
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-11-14
 * Time: 15:31
 * Version:
 */
public class Plan implements Serializable {
    private static final long serialVersionUID = 564751681705080572L;

    public String XH;//序号

    public String LXBH;

    public String GLLX;

    public String MS;

    public String WTLX;//1 计划 2焦点  问题类型

    public List<SimpleRecord> SimpleRecord;
}
