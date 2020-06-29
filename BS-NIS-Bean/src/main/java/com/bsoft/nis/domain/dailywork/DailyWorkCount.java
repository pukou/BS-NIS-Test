package com.bsoft.nis.domain.dailywork;

import java.util.List;

/**
 * Created by king on 2016/11/23.
 */
public class DailyWorkCount {

    /**
     * 当前需执行医嘱计划
     */
    public List<DailyWork> plan;

    /**
     * 当前变动医嘱
     */
    public List<DailyWork> changeAdvice;

    /**
     * 当前检验采集
     */
    public List<DailyWork> inspection;

    /**
     * 风险提醒
     */
    public List<DailyWork> risk;
}
