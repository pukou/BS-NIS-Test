package com.bsoft.nis.advicesplit.args;

import com.bsoft.nis.domain.advicesplit.advice.db.AdvicePlan;
import com.bsoft.nis.domain.advicesplit.advice.db.MonitoreAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:本次需提交的数据，医嘱拆分对应的提交数据，目前按病人保存并提交
 * Created: dragon
 * Date： 2016/12/1.
 */
public class ExcuteResults {
    /**
     * 需生成的计划
     */
    public List<AdvicePlan> insertPlans = new ArrayList<>();
    /**
     * 根据医嘱序号和计划日期删除的计划
     */
    public List<AdvicePlan> delPlansByYzxh = new ArrayList<>();
    /**
     * 需删除的计划
     */
    public List<AdvicePlan> delPlans = new ArrayList<>();
    /**
     * 更新监控表
     */
    public List<MonitoreAdvice> updateMonitors = new ArrayList<>();
    /**
     * 新增监控表
     */
    public List<MonitoreAdvice> insertMonitors = new ArrayList<>();
    /**
     * 异常日志
     */
    public List<String> exceptionLogs = new ArrayList<>();

}
