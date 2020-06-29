package com.bsoft.nis.domain.advicesplit.advice;

import com.bsoft.nis.domain.advicesplit.advice.db.Advice;
import com.bsoft.nis.domain.advicesplit.advice.db.AdvicePlan;
import com.bsoft.nis.domain.advicesplit.advice.db.MonitoreAdvice;
import com.bsoft.nis.domain.advicesplit.ratetime.db.ExcuteRate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Describtion:医嘱信息组合类
 * 1.该医嘱对应的监控医嘱
 * 2.该医嘱对应的执行频次
 * 3.该医嘱对应的计划列表 已拆分的
 * 4.该医嘱本次拆分生成的时间点，由医嘱和时间点组合生成计划
 * Created: dragon
 * Date： 2016/12/12.
 */
public class AdviceCom extends Advice{
    /**
     * 拆分开始
     */
    public Date cfks;
    /**
     * 拆分结束
     */
    public Date cfjs;
    /**
     * 监控医嘱
     */
    public List<MonitoreAdvice> monitoreAdvices = new ArrayList<>();
    /**
     * 执行频次
     */
    public List<ExcuteRate> rates = new ArrayList<>();
    /**
     * 医嘱已产生的计划列表
     */
    public List<AdvicePlan> plans = new ArrayList<>();
    /**
     * 将生成计划的时间点（由医嘱和时点组合生成计划）
     */
    public List<PlanTime> planTimes = new ArrayList<>();
}
