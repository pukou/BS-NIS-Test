package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.*;

/**
 * Description: 输液类 加药核对
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 13:25
 * Version:
 */
public class TransfusionDosingCheck extends FlowControl {
    public TransfusionDosingCheck() {
        super("4");
    }

    @Override
    public ExecuteArg BranchCall(ExecuteArg ea, Object[] objs) {
        if (ea.GSLX.equals(super.GSLX) && super.ParameterManager.getParameterMap().get(ea.JGID).TransfusionDosingCheck) {
            if (ea.HandExcute && !super.ParameterManager.getParameterMap().get(ea.JGID).TransfusionHandExcuteCheckSyd) {
                return null;
            }
            TransfusionInfo transfusionInfo = (TransfusionInfo) ea.RecordInfo;
            if (transfusionInfo.JYHDBZ.equals("1")) {
                return null;
            } else {
                for (TransfusionDetailInfo detailInfo : transfusionInfo.Details) {
                    AdviceBqyzInfo adviceBqyzInfo = null;
                    for (AdviceBqyzInfo info : ea.AdviceBqyzInfoList) {
                        if (detailInfo.YZXH.equals(info.YZXH)) {
                            adviceBqyzInfo = info;
                            break;
                        }
                    }
                    ea.ExecutResult.add(new ExecuteResult(ea, "0", transfusionInfo.SYSJ, transfusionInfo.SJMC, adviceBqyzInfo.YZMC, detailInfo.YZXH, ea.YHID,
                            super.MessageManager.Create("32"), "32", "0",transfusionInfo.SYSJ,transfusionInfo.YZZH));

                }
                return ea;
            }
        }

        return null;
    }
}
