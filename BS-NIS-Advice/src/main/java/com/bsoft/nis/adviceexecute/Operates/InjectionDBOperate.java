package com.bsoft.nis.adviceexecute.Operates;

import com.bsoft.nis.adviceexecute.ModelManager.ParameterManager;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.adviceexecute.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 注射类执行器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-28
 * Time: 17:20
 * Version:
 */
@Component
public class InjectionDBOperate {

    @Autowired
    ParameterManager parameterManager;//用户参数管理器

    @Autowired
    DateTimeService timeService; // 日期时间服务

    public void Operate(ExecuteArg ea) {
        if (ea.GSLX.equals("5")) {
            InjectionInfo injectionInfo = (InjectionInfo) ea.RecordInfo;
            String now = timeService.now(DataSource.MOB);
            if (!StringUtils.isBlank(ea.QRDH)) {
                List<PlanInfo> planInfoList = new ArrayList<>();
                AdviceArg adviceArg = new AdviceArg();
                adviceArg.ClassName = "InjectionDBOperate";
                if (!parameterManager.getParameterMap().get(ea.JGID).InjectionUpdate) {
                    if (ea.PlanInfoList != null && ea.PlanInfoList.size() > 0) {
                        PlanInfo plan = new PlanInfo();
                        plan.KSGH = ea.YHID;
                        plan.KSSJ = now;
                        plan.JSGH = ea.YHID;
                        plan.JSSJ = now;
                        plan.ZYH = ea.ZYH;
                        plan.ZDLX = "1";
                        //add 已执行 2018-7-25 17:05:40
                        plan.ZXZT = "1";
                        plan.JGID = ea.JGID;
                        plan.JHHList = new ArrayList<>();
                        for (PlanInfo planInfo : ea.PlanInfoList) {
                            plan.JHHList.add(planInfo.JHH);
                        }
                        planInfoList.add(plan);
                        adviceArg.PlanInfoList = planInfoList;
                    }
                } else {
                    if (injectionInfo.ZSZT.equals("1")) {//已执行
                        InjectionInfo recordInfo = new InjectionInfo();
                        recordInfo.JSGH = ea.YHID;
                        recordInfo.JSSJ = now;
                        recordInfo.ZSDH = ea.QRDH;
                        recordInfo.ZSZT = "1";
                        recordInfo.JGID = ea.JGID;
                        adviceArg.RecordInfo = recordInfo;
                        PlanInfo planInfo = new PlanInfo();
                        planInfo.JSGH = ea.YHID;
                        planInfo.JSSJ = now;
                        planInfo.ZYH = ea.ZYH;
                        planInfo.ZDLX = "1";
                        planInfo.QRDH = ea.QRDH;
                        planInfo.JGID = ea.JGID;
                        //add 已执行 2018-7-25 17:05:40
                        planInfo.ZXZT = "1";
                        //add 2018-4-26 16:11:58
                        planInfo.JHHList = new ArrayList<>();
                            for (PlanInfo tempPlanInfo : ea.PlanInfoList) {
                                if (tempPlanInfo.JHH != null && !"".equals(tempPlanInfo.JHH)) {
                                    planInfo.JHHList.add(tempPlanInfo.JHH);
                                }
                            }
                        //
                        planInfoList.add(planInfo);
                        adviceArg.PlanInfoList = planInfoList;
                    } else {
                        InjectionInfo recordInfo = new InjectionInfo();
                        recordInfo.JSGH = ea.YHID;
                        recordInfo.JSSJ = now;
                        //change 2018-4-27 11:25:21
                        recordInfo.ZXSJ = now;
                        recordInfo.ZXGH = ea.YHID;
                        //change 2018-4-27 11:25:21
                        recordInfo.ZSDH = ea.QRDH;
                        //change 为了更新注射状态
                        recordInfo.ZSZT = "1";
                        recordInfo.JGID = ea.JGID;
                        adviceArg.RecordInfo = recordInfo;
                        PlanInfo planInfo = new PlanInfo();
                        planInfo.JSGH = ea.YHID;
                        planInfo.JSSJ = now;
                        //add 2018-4-26 16:40:41
                        planInfo.KSSJ = now;
                        planInfo.KSGH = ea.YHID;
                        //
                        planInfo.ZYH = ea.ZYH;
                        planInfo.ZDLX = "1";
                        planInfo.QRDH = ea.QRDH;
                        planInfo.JGID = ea.JGID;
                        //add 已执行 2018-7-25 17:05:40
                        planInfo.ZXZT = "1";
                        //add 2018-4-26 16:11:58
                        planInfo.JHHList = new ArrayList<>();
                        for (PlanInfo tempPlanInfo : ea.PlanInfoList) {
                            if (tempPlanInfo.JHH != null && !"".equals(tempPlanInfo.JHH)) {
                                planInfo.JHHList.add(tempPlanInfo.JHH);
                            }
                        }
                        //
                        planInfoList.add(planInfo);
                        adviceArg.PlanInfoList = planInfoList;
                    }
                }
                ea.AdviceArgList.add(adviceArg);

                PlanInfo info = null;
                if (ea.PlanInfoList != null && ea.PlanInfoList.size() > 0) {
                    for (PlanInfo planInfo : ea.PlanInfoList) {
                        if (planInfo.GSLX.equals("5") && !StringUtils.isBlank(planInfo.SRHDBZ) && planInfo.SRHDBZ.equals("1") && !StringUtils.isBlank(planInfo.KSHDGH)) {
                            info = planInfo;
                            break;
                        }
                    }
                }
                String checkUrid = null;
                boolean doubleCheck = false;
                if (info != null) {
                    checkUrid = info.KSHDGH;
                    doubleCheck = true;
                }
                updateYzb(ea, now, checkUrid, doubleCheck);
                updateBqyz(ea, now, checkUrid, doubleCheck);
                updateBqyzjh(ea, now, checkUrid, doubleCheck);
            }

        }
    }


    public void CancelStart(ExecuteArg ea) {
        if (ea.GSLX.equals("5")) {
            InjectionInfo injectionInfo = (InjectionInfo) ea.RecordInfo;
            String now = timeService.now(DataSource.MOB);
            if (!StringUtils.isBlank(ea.QRDH)) {
                List<PlanInfo> planInfoList = new ArrayList<>();
                AdviceArg adviceArg = new AdviceArg();
                adviceArg.ClassName = "InjectionDBOperate_CancelStart";

                if (ea.PlanInfoList != null && ea.PlanInfoList.size() > 0) {
                    PlanInfo plan = new PlanInfo();
                    plan.KSGH = null;
                    plan.KSSJ = null;
                    plan.JSGH = null;
                    plan.JSSJ = null;
                    plan.ZYH = null;
                    plan.ZDLX = "1";
                    plan.JGID = ea.JGID;
                    plan.ZXZT = "0";//未执行
                    plan.JHHList = new ArrayList<>();
                    for (PlanInfo planInfo : ea.PlanInfoList) {
                        plan.JHHList.add(planInfo.JHH);
                    }
                    planInfoList.add(plan);
                    adviceArg.PlanInfoList = planInfoList;
                }

                if (parameterManager.getParameterMap().get(ea.JGID).InjectionUpdate) {

                    InjectionInfo recordInfo = new InjectionInfo();
                    recordInfo.JSGH = null;
                    recordInfo.JSSJ = null;
                    //change 2018-4-27 11:25:21
                    recordInfo.ZXSJ = null;
                    recordInfo.ZXGH = null;
                    //change 2018-4-27 11:25:21
                    recordInfo.ZSDH = injectionInfo.ZSDH;
                    //
                    recordInfo.ZSZT = "0";
                    recordInfo.JGID = ea.JGID;
                    adviceArg.RecordInfo = recordInfo;
                    //////
                    PlanInfo planInfo = new PlanInfo();
                    planInfo.JSGH = null;
                    planInfo.JSSJ = null;
                    //add 2018-4-26 16:40:41
                    planInfo.KSSJ = null;
                    planInfo.KSGH = null;
                    //
                    planInfo.ZYH = null;
                    planInfo.ZDLX = "1";
                    planInfo.QRDH = injectionInfo.ZSDH;
                    planInfo.JGID = ea.JGID;
                    //add 2018-4-26 16:11:58
                    planInfo.JHHList = new ArrayList<>();
                    for (PlanInfo tempPlanInfo : ea.PlanInfoList) {
                        if (tempPlanInfo.JHH != null && !"".equals(tempPlanInfo.JHH)) {
                            planInfo.JHHList.add(tempPlanInfo.JHH);
                        }
                    }
                    //
                    planInfoList.add(planInfo);
                    adviceArg.PlanInfoList = planInfoList;
                }
                ea.AdviceArgList.add(adviceArg);

                PlanInfo info = null;
                if (ea.PlanInfoList != null && ea.PlanInfoList.size() > 0) {
                    for (PlanInfo planInfo : ea.PlanInfoList) {
                        if (planInfo.GSLX.equals("5") && !StringUtils.isBlank(planInfo.SRHDBZ) && planInfo.SRHDBZ.equals("1") && !StringUtils.isBlank(planInfo.KSHDGH)) {
                            info = planInfo;
                            break;
                        }
                    }
                }
                String checkUrid = null;
                boolean doubleCheck = false;
                if (info != null) {
                    checkUrid = info.KSHDGH;
                    doubleCheck = true;
                }
                updateYzbCancel(ea, now, checkUrid, doubleCheck);
                updateBqyzCancel(ea, now, checkUrid, doubleCheck);
                updateBqyzjhCancel(ea, now, checkUrid, doubleCheck);
            }

        }
    }


    private void updateYzb(ExecuteArg ea, String time, String checkUrid, boolean doubleCheck) {
        if (parameterManager.getParameterMap().get(ea.JGID).UpdateYzb) {
            List<AdviceYzbInfo> adviceYzbInfoList = new ArrayList<>();
            for (PlanInfo planInfo : ea.PlanInfoList) {
                AdviceYzbInfo adviceYzbInfo = new AdviceYzbInfo();
                adviceYzbInfo.YZXH = planInfo.YSYZBH;
                adviceYzbInfo.HSZXGH = ea.YHID;
                if (doubleCheck) {
                    adviceYzbInfo.HSZXGH2 = checkUrid;
                }
                adviceYzbInfo.HSZXSJ = time;
                adviceYzbInfoList.add(adviceYzbInfo);
            }
            AdviceArg adviceArg = null;
            if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
                for (AdviceArg arg : ea.AdviceArgList) {
                    if (arg.ClassName.equals("InjectionDBOperate")) {
                        adviceArg = arg;
                    }
                }
            }
            if (adviceArg != null) {
                adviceArg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
            } else {
                adviceArg = new AdviceArg();
                adviceArg.ClassName = "InjectionDBOperate";
                adviceArg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
            }
        }
    }
    private void updateYzbCancel(ExecuteArg ea, String time, String checkUrid, boolean doubleCheck) {
        if (parameterManager.getParameterMap().get(ea.JGID).UpdateYzb) {
            List<AdviceYzbInfo> adviceYzbInfoList = new ArrayList<>();
            for (PlanInfo planInfo : ea.PlanInfoList) {
                AdviceYzbInfo adviceYzbInfo = new AdviceYzbInfo();
                adviceYzbInfo.YZXH = planInfo.YSYZBH;
                adviceYzbInfo.HSZXGH =null;
                if (doubleCheck) {
                    adviceYzbInfo.HSZXGH2 = checkUrid;// FIXME: 2018/7/25
                }
                adviceYzbInfo.HSZXSJ = null;
                adviceYzbInfoList.add(adviceYzbInfo);
            }
            AdviceArg adviceArg = null;
            if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
                for (AdviceArg arg : ea.AdviceArgList) {
                    if (arg.ClassName.equals("InjectionDBOperate")) {
                        adviceArg = arg;
                    }
                }
            }
            if (adviceArg != null) {
                adviceArg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
            } else {
                adviceArg = new AdviceArg();
                adviceArg.ClassName = "InjectionDBOperate";
                adviceArg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
            }
        }
    }

    private void updateBqyz(ExecuteArg ea, String time, String checkUrid, boolean doubleCheck) {
        if (parameterManager.getParameterMap().get(ea.JGID).UpdateBqyz) {
            List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
            for (PlanInfo planInfo : ea.PlanInfoList) {
                AdviceBqyzInfo adviceBqyzInfo = new AdviceBqyzInfo();
                adviceBqyzInfo.YZXH = planInfo.YZXH;
                adviceBqyzInfo.HSZXGH = ea.YHID;
                if (doubleCheck) {
                    adviceBqyzInfo.HSZXGH2 = checkUrid;
                }
                adviceBqyzInfo.HSZXSJ = time;
                adviceBqyzInfoList.add(adviceBqyzInfo);
            }
            AdviceArg adviceArg = null;
            if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
                for (AdviceArg arg : ea.AdviceArgList) {
                    if (arg.ClassName.equals("InjectionDBOperate")) {
                        adviceArg = arg;
                    }
                }
            }
            if (adviceArg != null) {
                adviceArg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
            } else {
                adviceArg = new AdviceArg();
                adviceArg.ClassName = "InjectionDBOperate";
                adviceArg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
            }
        }

    }
    private void updateBqyzCancel(ExecuteArg ea, String time, String checkUrid, boolean doubleCheck) {
        if (parameterManager.getParameterMap().get(ea.JGID).UpdateBqyz) {
            List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
            for (PlanInfo planInfo : ea.PlanInfoList) {
                AdviceBqyzInfo adviceBqyzInfo = new AdviceBqyzInfo();
                adviceBqyzInfo.YZXH = planInfo.YZXH;
                adviceBqyzInfo.HSZXGH = null;
                if (doubleCheck) {
                    adviceBqyzInfo.HSZXGH2 = checkUrid;// FIXME: 2018/7/25
                }
                adviceBqyzInfo.HSZXSJ = null;
                adviceBqyzInfoList.add(adviceBqyzInfo);
            }
            AdviceArg adviceArg = null;
            if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
                for (AdviceArg arg : ea.AdviceArgList) {
                    if (arg.ClassName.equals("InjectionDBOperate")) {
                        adviceArg = arg;
                    }
                }
            }
            if (adviceArg != null) {
                adviceArg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
            } else {
                adviceArg = new AdviceArg();
                adviceArg.ClassName = "InjectionDBOperate";
                adviceArg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
            }
        }

    }

    private void updateBqyzjh(ExecuteArg ea, String time, String checkUrid, boolean doubleCheck) {

        // 不更新病区医嘱计划
        if (!parameterManager.getParameterMap().get(ea.JGID).UpdateBQyzjh || parameterManager.getParameterMap().get(ea.JGID).Integrated.equals("2")) {
            return;
        }
        List<PlanInfo> bqPlanInfoList = new ArrayList<>();
        for (PlanInfo planInfo : ea.PlanInfoList) {
            PlanInfo bqPlanInfo = new PlanInfo();
            bqPlanInfo.KSGH = ea.YHID;
            bqPlanInfo.KSSJ = time;
            bqPlanInfo.JSSJ = time;
            bqPlanInfo.JSGH = ea.YHID;
            bqPlanInfo.GLJHH = planInfo.GLJHH;
            bqPlanInfoList.add(bqPlanInfo);
        }
        AdviceArg adviceArg = null;
        if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
            for (AdviceArg arg : ea.AdviceArgList) {
                if (arg.ClassName.equals("InjectionDBOperate")) {
                    adviceArg = arg;
                }
            }
        }
        if (adviceArg != null) {
            adviceArg.BQPlanInfoList.addAll(bqPlanInfoList);
        } else {
            adviceArg = new AdviceArg();
            adviceArg.ClassName = "InjectionDBOperate";
            adviceArg.BQPlanInfoList.addAll(bqPlanInfoList);
        }

    }
    private void updateBqyzjhCancel(ExecuteArg ea, String time, String checkUrid, boolean doubleCheck) {

        // 不更新病区医嘱计划
        if (!parameterManager.getParameterMap().get(ea.JGID).UpdateBQyzjh || parameterManager.getParameterMap().get(ea.JGID).Integrated.equals("2")) {
            return;
        }
        List<PlanInfo> bqPlanInfoList = new ArrayList<>();
        for (PlanInfo planInfo : ea.PlanInfoList) {
            PlanInfo bqPlanInfo = new PlanInfo();
            bqPlanInfo.KSGH = null;
            bqPlanInfo.KSSJ = null;
            bqPlanInfo.JSSJ = null;
            bqPlanInfo.JSGH = null;
            bqPlanInfo.GLJHH = planInfo.GLJHH;
            //add
            bqPlanInfo.ZXZT = "0";//未执行
            bqPlanInfoList.add(bqPlanInfo);
        }
        AdviceArg adviceArg = null;
        if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
            for (AdviceArg arg : ea.AdviceArgList) {
                if (arg.ClassName.equals("InjectionDBOperate")) {
                    adviceArg = arg;
                }
            }
        }
        if (adviceArg != null) {
            adviceArg.BQPlanInfoList.addAll(bqPlanInfoList);
        } else {
            adviceArg = new AdviceArg();
            adviceArg.ClassName = "InjectionDBOperate";
            adviceArg.BQPlanInfoList.addAll(bqPlanInfoList);
        }

    }
}
