package com.bsoft.nis.domain.adviceexecute;

/**
 * Description: 输液处理执行参数
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 10:19
 * Version:
 */
public class TransfuseArgs {

    /**
     * 需要接瓶的输液单号
     */
    public String JP_SYDH;

    /**
     * 是否并行输液
     */
    public boolean IsParallel;

    /**
     * 是否判断暂停
     */
    public boolean Ztpd = true;

    /**
     * 是否强制结束
     */
    public boolean Qzjs = false;

    /**
     * 需要接瓶的是否要双签
     */
    public boolean JP_DoubleCheck = false;
}
