package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.*;

/**
 * Description: 计划执行基本校验 - 此流程必须用
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-28
 * Time: 14:25
 * Version:
 */
public class PlanGeneralCheck extends FlowControl {
    public PlanGeneralCheck() {
        super("0");
    }

    @Override
    public ExecuteArg BranchCall(ExecuteArg ea, Object[] objs) {
        for (int i = 0; i < ea.PlanInfoList.size(); i++) {
            PlanInfo planInfo = ea.PlanInfoList.get(i);
            boolean isSelect = false;
            for (ExecuteResult info : ea.ExecutResult) {
                if (info.JHH.equals(planInfo.JHH)) {
                    isSelect = true;
                    break;
                }
            }
            //add 2018-7-5 10:01:47 结束输液 需要
            if(objs != null && objs.length > 0 && String.valueOf(objs[0]).equals("isCancel")) {
                //不提示重复执行
                return null;
            }else{
                //原有逻辑
                if (!isSelect && planInfo.ZXZT.equals("1")) {
                    ea.ExecutResult.add(new ExecuteResult(ea, planInfo.JHH, planInfo.JHSJ, planInfo.SJMC, planInfo.YZMC, planInfo.YZXH, ea.YHID,
                            super.MessageManager.Create("4"), "4", planInfo.ZXZT,planInfo.JHSJ,planInfo.YZZH));
                    return ea;
                }
                //======
            }

        }

        return null;
    }
}
