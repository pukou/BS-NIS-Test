package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.*;

/**
 * Description: 注射类 加药核对
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 13:25
 * Version:
 */
public class InjectionDosingCheck extends FlowControl {
    public InjectionDosingCheck() {
        super("5");
    }

    @Override
    public ExecuteArg BranchCall(ExecuteArg ea, Object[] objs) {
        if (ea.GSLX.equals(super.GSLX) && super.ParameterManager.getParameterMap().get(ea.JGID).InjectionDosingCheck) {
            if (ea.HandExcute && !super.ParameterManager.getParameterMap().get(ea.JGID).InjectionHandExcuteCheckZsd) {
                return null;
            }
            InjectionInfo injectionInfo = (InjectionInfo) ea.RecordInfo;
            if (injectionInfo.JYHDBZ.equals("1")) {
                return null;
            } else {
                for (InjectionDetailInfo detailInfo : injectionInfo.Details) {
                    AdviceBqyzInfo adviceBqyzInfo = null;
                    for (AdviceBqyzInfo info : ea.AdviceBqyzInfoList) {
                        if (detailInfo.YZXH.equals(info.YZXH)) {
                            adviceBqyzInfo = info;
                            break;
                        }
                    }
                    ea.ExecutResult.add(new ExecuteResult(ea, "0", injectionInfo.ZXSJ, injectionInfo.SJMC, adviceBqyzInfo.YZMC, detailInfo.YZXH, ea.YHID,
                            super.MessageManager.Create("31"), "31", "0",injectionInfo.ZXSJ,injectionInfo.YZZH));

                }
                return ea;
            }
        }

        return null;
    }
}
