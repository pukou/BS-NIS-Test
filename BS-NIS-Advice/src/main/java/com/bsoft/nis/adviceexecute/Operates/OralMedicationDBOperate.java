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
 * Description: 口服类执行器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-28
 * Time: 16:52
 * Version:
 */
@Component
public class OralMedicationDBOperate {

    @Autowired
    ParameterManager parameterManager;//用户参数管理器

    @Autowired
    DateTimeService timeService; // 日期时间服务

    public void Operate(ExecuteArg ea) {
         if (ea.GSLX.equals("3")) {
            OralInfo oralInfo = (OralInfo) ea.RecordInfo;
            String now = timeService.now(DataSource.MOB);
            if (!StringUtils.isBlank(ea.QRDH)) {
                List<PlanInfo> planInfoList = new ArrayList<>();
                AdviceArg adviceArg = new AdviceArg();
                adviceArg.ClassName = "OralMedicationDBOperate";
                if (ea.PlanInfoList != null && ea.PlanInfoList.size() > 0) {
                    PlanInfo plan = new PlanInfo();
                    plan.KSGH = ea.YHID;
                    plan.KSSJ = now;
                    plan.JSGH = ea.YHID;
                    plan.JSSJ = now;
                    plan.ZYH = ea.ZYH;
                    plan.ZDLX = "1";
                    //add 已执行
                    plan.ZXZT = "1";
                    plan.JHHList = new ArrayList<>();
                    for (PlanInfo planInfo : ea.PlanInfoList) {
                        plan.JHHList.add(planInfo.JHH);
                    }
                    plan.JGID = ea.JGID;
                    planInfoList.add(plan);
                    adviceArg.PlanInfoList = planInfoList;
                }
                if (parameterManager.getParameterMap().get(ea.JGID).OralUpdate) {
                    OralInfo oral = new OralInfo();
                    for(OralPackageInfo item : oralInfo.Packages) {
                        OralPackageInfo packageInfo = new OralPackageInfo();
                        packageInfo.ZXGH = ea.YHID;
                        packageInfo.ZXSJ = now;
                        packageInfo.KFDH = oralInfo.KFDH;
                        packageInfo.JGID = ea.JGID;
                        packageInfo.KFMX = (!StringUtils.isBlank(item.KFMX) ? item.KFMX : "-999999");
                        //change 2018-4-27 11:25:21
                        packageInfo.KFZT = "1";
                        oral.KFSJ = now;
                        oral.KFDH = oralInfo.KFDH;
                        //change 2018-4-27 11:25:21
                        oral.Packages.add(packageInfo);
                    }
                    adviceArg.RecordInfo = oral;
                }
                ea.AdviceArgList.add(adviceArg);
                PlanInfo info = null;
                if (ea.PlanInfoList != null && ea.PlanInfoList.size() > 0) {
                    for (PlanInfo planInfo : ea.PlanInfoList) {
                        if (planInfo.GSLX.equals("3") && !StringUtils.isBlank(planInfo.SRHDBZ) && planInfo.SRHDBZ.equals("1") && !StringUtils.isBlank(planInfo.KSHDGH)) {
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
        if (ea.GSLX.equals("3")) {
            OralInfo oralInfo = (OralInfo) ea.RecordInfo;
            String now = timeService.now(DataSource.MOB);
            if (!StringUtils.isBlank(ea.QRDH)) {
                List<PlanInfo> planInfoList = new ArrayList<>();
                AdviceArg adviceArg = new AdviceArg();
                adviceArg.ClassName = "OralMedicationDBOperate_CancelStart";
                if (ea.PlanInfoList != null && ea.PlanInfoList.size() > 0) {
                    PlanInfo plan = new PlanInfo();
                    plan.KSGH = null;
                    plan.KSSJ = null;
                    plan.JSGH = null;
                    plan.JSSJ = null;
                    plan.ZYH = null;
                    plan.ZDLX = "1";
                    plan.JHHList = new ArrayList<>();
                    for (PlanInfo planInfo : ea.PlanInfoList) {
                        plan.JHHList.add(planInfo.JHH);
                    }
                    plan.JGID = ea.JGID;
                    //add 2018-7-25 14:29:42
                    plan.ZXZT = "0";//未执行
                    planInfoList.add(plan);
                    adviceArg.PlanInfoList = planInfoList;
                }
                if (parameterManager.getParameterMap().get(ea.JGID).OralUpdate) {
                    OralInfo oral = new OralInfo();
                    for(OralPackageInfo item : oralInfo.Packages) {
                        OralPackageInfo packageInfo = new OralPackageInfo();
                        packageInfo.ZXGH = null;
                        packageInfo.ZXSJ = null;
                        packageInfo.KFDH = oralInfo.KFDH;
                        packageInfo.KFMX = (!StringUtils.isBlank(item.KFMX) ? item.KFMX : "-999999");
                        packageInfo.JGID = ea.JGID;
                        //change 2018-7-27 11:25:21
                        packageInfo.KFZT = "0";//未执行
                        oral.KFSJ = null;
                        oral.KFDH = oralInfo.KFDH;
                        //change 2018-7-27 11:25:21
                        oral.Packages.add(packageInfo);
                    }
                    adviceArg.RecordInfo = oral;
                }
                ea.AdviceArgList.add(adviceArg);
                PlanInfo info = null;
                if (ea.PlanInfoList != null && ea.PlanInfoList.size() > 0) {
                    for (PlanInfo planInfo : ea.PlanInfoList) {
                        if (planInfo.GSLX.equals("3") && !StringUtils.isBlank(planInfo.SRHDBZ) && planInfo.SRHDBZ.equals("1") && !StringUtils.isBlank(planInfo.KSHDGH)) {
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
                    if (arg.ClassName.equals("OralMedicationDBOperate")) {
                        adviceArg = arg;
                    }
                }
            }
            if (adviceArg != null) {
                adviceArg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
            } else {
                adviceArg = new AdviceArg();
                adviceArg.ClassName = "OralMedicationDBOperate";
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
                adviceYzbInfo.HSZXGH = null;
                if (doubleCheck) {
                    adviceYzbInfo.HSZXGH2 = checkUrid;// FIXME: 2018/7/25
                }
                adviceYzbInfo.HSZXSJ = null;
                adviceYzbInfoList.add(adviceYzbInfo);
            }
            AdviceArg adviceArg = null;
            if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
                for (AdviceArg arg : ea.AdviceArgList) {
                    if (arg.ClassName.equals("OralMedicationDBOperate")) {
                        adviceArg = arg;
                    }
                }
            }
            if (adviceArg != null) {
                adviceArg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
            } else {
                adviceArg = new AdviceArg();
                adviceArg.ClassName = "OralMedicationDBOperate";
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
                    if (arg.ClassName.equals("OralMedicationDBOperate")) {
                        adviceArg = arg;
                    }
                }
            }
            if (adviceArg != null) {
                adviceArg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
            } else {
                adviceArg = new AdviceArg();
                adviceArg.ClassName = "OralMedicationDBOperate";
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
                    if (arg.ClassName.equals("OralMedicationDBOperate")) {
                        adviceArg = arg;
                    }
                }
            }
            if (adviceArg != null) {
                adviceArg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
            } else {
                adviceArg = new AdviceArg();
                adviceArg.ClassName = "OralMedicationDBOperate";
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
                if (arg.ClassName.equals("OralMedicationDBOperate")) {
                    adviceArg = arg;
                }
            }
        }
        if (adviceArg != null) {
            adviceArg.BQPlanInfoList.addAll(bqPlanInfoList);
        } else {
            adviceArg = new AdviceArg();
            adviceArg.ClassName = "OralMedicationDBOperate";
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
            //add
            bqPlanInfo.ZXZT = "0";//未执行
            bqPlanInfo.GLJHH = planInfo.GLJHH;
            bqPlanInfoList.add(bqPlanInfo);
        }
        AdviceArg adviceArg = null;
        if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
            for (AdviceArg arg : ea.AdviceArgList) {
                if (arg.ClassName.equals("OralMedicationDBOperate")) {
                    adviceArg = arg;
                }
            }
        }
        if (adviceArg != null) {
            adviceArg.BQPlanInfoList.addAll(bqPlanInfoList);
        } else {
            adviceArg = new AdviceArg();
            adviceArg.ClassName = "OralMedicationDBOperate";
            adviceArg.BQPlanInfoList.addAll(bqPlanInfoList);
        }

    }
}
