package com.bsoft.nis.domain.adviceexecute;

import java.util.List;

/**
 * Description: 医嘱系统参数对象
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-20
 * Time: 17:35
 * Version:
 */
public class Parameter {

    /**
     * 时间戳
     */
    public long TimeStamp;

    /**
     * 儿童年龄限制
     */
    public Integer ChildAge;

    /**
     * 注射医嘱执行时判断加药核对
     */
    public boolean InjectionDosingCheck;

    /**
     * 输液医嘱执行时判断加药核对
     */
    public boolean TransfusionDosingCheck;

    /**
     * 前辍是否大小写敏感
     */
    public boolean PrefixCaseSensitive;

    /**
     * 口服扫描执行使用条码前缀
     */
    public boolean OralUsePrefix;

    /**
     * 输液扫描执行使用条码前缀
     */
    public boolean TransfusionUsePrefix;

    /**
     * 注射扫描执行使用条码前缀
     */
    public boolean InjectionUsePrefix;

    /**
     * 口服类与计划是否通过时间关联
     * 0 时间编号 1 时间
     */
    public boolean OralJoinPlanByTime;

    /**
     * 输液类与计划是否通过时间关联
     */
    public boolean TransfusionJoinPlanByTime;

    /**
     * 注射类与计划是否通过时间编号关联
     */
    public boolean InjectionJoinPlanByTime;

    /**
     * 口服类手动执行核对口服单
     */
    public boolean OralHandExcuteCheckKfd;

    /**
     * 输液类手动执行核对输液单
     */
    public boolean TransfusionHandExcuteCheckSyd;

    /**
     * 注射类手动执行核对注射单
     */
    public boolean InjectionHandExcuteCheckZsd;

    /**
     * 是否核对医嘱本
     */
    public boolean CheckYzb;

    /**
     * 更新医嘱本
     */
    public boolean UpdateYzb;

    /**
     * 更新病区医嘱
     */
    public boolean UpdateBqyz;

    /**
     * 是否双人核对
     */
    public boolean DoubleCheck;

    /**
     * 双人核对超时时间
     */
    public Integer DoubleCheckTimeOut;

    /**
     * 医嘱执行时启用计费
     */
    public boolean OpenCharge;

    /**
     * 执行时是否更新注射单
     */
    public boolean InjectionUpdate;

    /**
     * 执行时是否更新输液单
     */
    public boolean TransfusionUpdate;

    /**
     * 执行时是否更新口服单
     */
    public boolean OralUpdate;

    /**
     * 医嘱计划生成方式
     * 0:自己拆分 1:同步计划 2:合并计划
     */
    public String Integrated;

    /**
     * 更新病区医嘱计划
     */
    public boolean UpdateBQyzjh;

    /**
     * 允许口服药手动执行
     */
    public boolean OralAllowHandExcute;

    /**
     * 允许注射药手动执行
     */
    public boolean InjectionAllowHandExcute;

    /**
     * 允许输液手动执行
     */
    public boolean TransfusionAllowHandExcute;

    /**
     * 滴数计算换算
     */
    public float DropSpeedConversion;

    /**
     * 输液滴速计算单位
     */
    public List<String> DropSpeedUnit;

    /**
     * 医嘱执行时医嘱内容是否写入护理记录
     */
    public boolean SyncNuserRecord;

}
