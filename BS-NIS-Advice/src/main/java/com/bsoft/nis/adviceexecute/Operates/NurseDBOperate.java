package com.bsoft.nis.adviceexecute.Operates;

import com.bsoft.nis.adviceexecute.ModelManager.ParameterManager;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.adviceexecute.AdviceArg;
import com.bsoft.nis.domain.adviceexecute.AdviceBqyzInfo;
import com.bsoft.nis.domain.adviceexecute.AdviceYzbInfo;
import com.bsoft.nis.domain.adviceexecute.ExecuteArg;
import com.bsoft.nis.domain.adviceexecute.PlanInfo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 护理类执行器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-28
 * Time: 17:20
 * Version:
 */
@Component
public class NurseDBOperate {

    @Autowired
    ParameterManager parameterManager;//用户参数管理器

    @Autowired
    DateTimeService timeService; // 日期时间服务

    public void Operate(ExecuteArg ea) {
        if (ea.GSLX.equals("1")) {
            String now = timeService.now(DataSource.MOB);
            List<PlanInfo> planInfoList = new ArrayList<>();
            PlanInfo info = null;
            if (ea.PlanInfoList != null && ea.PlanInfoList.size() > 0) {
                for (PlanInfo planInfo : ea.PlanInfoList) {
                    planInfo.KSGH = ea.YHID;
                    planInfo.KSSJ = now;
                    planInfo.JSGH = ea.YHID;
                    planInfo.JSSJ = now;
                    planInfo.ZYH = ea.ZYH;
                    //add 已执行 2018-7-25 17:05:40
                    planInfo.ZXZT = "1";
                    planInfo.ZDLX = "1";
                    planInfoList.add(planInfo);
                    if (planInfo.GSLX.equals("1") && !StringUtils.isBlank(planInfo.SRHDBZ) && planInfo.SRHDBZ.equals("1") && !StringUtils.isBlank(planInfo.KSHDGH)) {
                        info = planInfo;
                    }
                }
            }
            AdviceArg adviceArg = new AdviceArg();
            adviceArg.ClassName = "NurseDBOperate";
            adviceArg.PlanInfoList = planInfoList;
            ea.AdviceArgList.add(adviceArg);
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
                    if (arg.ClassName.equals("NurseDBOperate")) {
                        adviceArg = arg;
                    }
                }
            }
            if (adviceArg != null) {
                adviceArg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
            } else {
                adviceArg = new AdviceArg();
                adviceArg.ClassName = "NurseDBOperate";
                adviceArg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
            }
        }
    }

    private void updateBqyz(ExecuteArg ea, String time, String checkUrid, boolean doubleCheck) {
        if (parameterManager.getParameterMap().get(ea.JGID).UpdateBqyz) {
            List<AdviceBqyzInfo> adviceBqyzInfoList   = new ArrayList<>();
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
                    if (arg.ClassName.equals("NurseDBOperate")) {
                        adviceArg = arg;
                    }
                }
            }
            if (adviceArg != null) {
                adviceArg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
            } else {
                adviceArg = new AdviceArg();
                adviceArg.ClassName = "NurseDBOperate";
                adviceArg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
            }
        }

    }

    private void updateBqyzjh(ExecuteArg ea, String time, String checkUrid, boolean doubleCheck) {
        // 不更新病区医嘱计划
        if (!parameterManager.getParameterMap().get(ea.JGID).UpdateBQyzjh || parameterManager.getParameterMap().get(ea.JGID).Integrated.equals("2")) {
            return;
        }
        List<PlanInfo> bqPlanInfoList =  new ArrayList<>();
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
                if (arg.ClassName.equals("NurseDBOperate")) {
                    adviceArg = arg;
                }
            }
        }
        if (adviceArg != null) {
            adviceArg.BQPlanInfoList.addAll(bqPlanInfoList);
        } else {
            adviceArg = new AdviceArg();
            adviceArg.ClassName = "NurseDBOperate";
            adviceArg.BQPlanInfoList.addAll(bqPlanInfoList);
        }

    }
}
