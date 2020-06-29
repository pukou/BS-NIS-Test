package com.bsoft.nis.domain.adviceexecute;

import java.util.ArrayList;
import java.util.List;
	/*
        升级编号【56010053】============================================= start
        多瓶超过2瓶转接瓶后提示选择接哪瓶的问题
        ================= Classichu 2017/11/14 16:25

        */
/**
 * Description: 输液单
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-19
 * Time: 17:23
 * Version:
 */
public class PlanAndTransfusion {

    /**
     * 输液单号
     */
    public String SYDH;


    /**
     * 输液状态
     * 0 未执行
     * 1 已经执行
     * 2 正在执行
     * 4 暂停
     */
    public String SYZT;

    public List<PlanInfo> planInfoList;

}
/* =============================================================== end */