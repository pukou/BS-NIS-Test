package com.bsoft.nis.domain.timingserver.transferdata;


import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:转移数据结果
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/4/11.
 * Time:  10:10.
 */
public class ResultData {
    // 医嘱计划
    public List<AdvicePlanTrans> plans = new ArrayList<>();
    // 监控医嘱
    public List<MonitoreAdviceTrans> monitoreAdvices = new ArrayList<>();
    // 口服单
    public List<OralSheet> oralSheets = new ArrayList<>();
    // 注射单
    public List<InjectSheet> injectSheets = new ArrayList<>();
    // 输液单
    public  List<TransfusionSheet> transfusionSheets = new ArrayList<>();
}
