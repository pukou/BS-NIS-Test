package com.bsoft.nis.domain.adviceexecute;

import java.util.List;

/**
 * Description: 对应单子的信息
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-20
 * Time: 11:19
 * Version:
 */
public class FlowRecordInfo {

    /**
     * 表名称
     * KF 口服 SY 输液 SYZT 输液暂停
     * 仿照DataTable的表名称
     */
    public String TableName;

    public List<FlowRecordDetailInfo> list;

}
