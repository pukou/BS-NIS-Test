package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.adviceexecute.ModelManager.MessageManager;
import com.bsoft.nis.adviceexecute.ModelManager.ParameterManager;
import com.bsoft.nis.domain.adviceexecute.*;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 流程控制器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-26
 * Time: 10:43
 * Version:
 */
public abstract class FlowControl implements IFlowControl {

    public String GSLX;

    public MessageManager MessageManager;

    public ParameterManager ParameterManager;

    public FlowControl(String gslx) {
        this.GSLX = gslx;
        this.MessageManager = new MessageManager();
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        this.ParameterManager = context.getBean(ParameterManager.class);
    }

    /**
     * 分支调用:数据合法性较验
     *
     * @param ea   医嘱组号
     * @param objs 机构id
     * @return
     */
    public ExecuteArg BranchCall(ExecuteArg ea, Object[] objs) {
        if (ea.PlanInfoList.size() == 0) {
            ea.ExecutResult.add(new ExecuteResult(ea, "0", null, null, null, null, ea.YHID,
                    MessageManager.Create("2"), "2", "-1",null,null));
            return ea;
        }

        if (ea.AdviceBqyzInfoList.size() == 0) {
            ea.ExecutResult.add(new ExecuteResult(ea, "0", null, null, null, null, ea.YHID,
                    MessageManager.Create("6"), "6", "-1",null,null));
            return ea;
        }
        if (ParameterManager.getParameterMap().get(ea.JGID).CheckYzb && ea.AdviceYzbInfoList.size() == 0) {
            ea.ExecutResult.add(new ExecuteResult(ea, "0", null, null, null, null, null,
                    MessageManager.Create("6"), "6", "-1",null,null));
            return ea;
        }

//        if (ea.PlanInfoList.size() != ea.AdviceYzbInfoList.size()) {
        if (ea.PlanInfoList.size() != ea.AdviceBqyzInfoList.size()) {
            //计划多于医嘱
//            if (ea.PlanInfoList.size() > ea.AdviceYzbInfoList.size()) {
            if (ea.PlanInfoList.size() > ea.AdviceBqyzInfoList.size()) {
                List<PlanInfo> planInfoList = new ArrayList<>();
                for (PlanInfo planInfo : ea.PlanInfoList) {
                    boolean isSelect = false;
                    for (AdviceBqyzInfo info : ea.AdviceBqyzInfoList) {
                        if (info.YZXH.equals(planInfo.YZXH)) {
                            isSelect = true;
                            break;
                        }
                    }
                    if (!isSelect) {
                        planInfoList.add(planInfo);
                    }
                }
                for (PlanInfo planInfo : planInfoList) {
                    ea.ExecutResult.add(new ExecuteResult(ea, "0", planInfo.JHSJ, planInfo.SJMC, planInfo.YZMC, null, ea.YHID,
                            MessageManager.Create("6"), "6", planInfo.ZXZT,planInfo.JHSJ,planInfo.YZZH));

                }
                return ea;
            } else {//医嘱多于计划
                List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
                for (AdviceBqyzInfo info : ea.AdviceBqyzInfoList) {
                    boolean isSelect = false;
                    for (PlanInfo planInfo : ea.PlanInfoList) {
                        if (planInfo.YZXH.equals(info.YZXH) && info.YZZT.equals("5")) {
                            isSelect = true;
                            break;
                        }
                    }
                    if (!isSelect) {
                        adviceBqyzInfoList.add(info);
                    }
                }
                for (AdviceBqyzInfo info : adviceBqyzInfoList) {
                    ea.ExecutResult.add(new ExecuteResult(ea, "0", null, null, info.YZMC, info.YZXH, ea.YHID,
                            MessageManager.Create("1"), "1", "-1",null,null));
                }

                return ea;
            }
        }

        return null;
    }
}
