package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 验证医嘱跟输液明细数据是否匹配
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 14:27
 * Version:
 */
public class TransfusionGeneralCheck extends FlowControl {
    public TransfusionGeneralCheck() {
        super("4");
    }

    @Override
    public ExecuteArg BranchCall(ExecuteArg ea, Object[] objs) {
        if (ea.GSLX.equals(super.GSLX) && ea.HandExcute &&
                super.ParameterManager.getParameterMap().get(ea.JGID).TransfusionHandExcuteCheckSyd) {
            TransfusionInfo transfusionInfo = (TransfusionInfo) ea.RecordInfo;
            if (transfusionInfo != null) {
                if (transfusionInfo.Details.size() > ea.AdviceBqyzInfoList.size()) {
                    List<TransfusionDetailInfo> details = new ArrayList<>();
                    for (TransfusionDetailInfo detail : transfusionInfo.Details) {
                        boolean isSelect = false;
                        for (AdviceBqyzInfo info : ea.AdviceBqyzInfoList) {
                            if (detail.YZXH.equals(info.YZXH)) {
                                isSelect = true;
                                break;
                            }
                        }
                        if (!isSelect) {
                            details.add(detail);
                        }
                    }
                    for (TransfusionDetailInfo detail : details) {
                        ea.ExecutResult.add(new ExecuteResult(ea, "0", transfusionInfo.SYSJ, transfusionInfo.SJMC, null, detail.YZXH, ea.YHID,
                                super.MessageManager.Create("6"), "6", "-1",transfusionInfo.SYSJ,transfusionInfo.YZZH));

                    }
                    return ea;

                } else if (transfusionInfo.Details.size() < ea.AdviceBqyzInfoList.size()) {
                    List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
                    for (AdviceBqyzInfo info : ea.AdviceBqyzInfoList) {
                        boolean isSelect = false;
                        for (TransfusionDetailInfo detail : transfusionInfo.Details) {
                            if (detail.YZXH.equals(info.YZXH)) {
                                isSelect = true;
                                break;
                            }
                        }
                        if (!isSelect) {
                            adviceBqyzInfoList.add(info);
                        }
                    }
                    for (AdviceBqyzInfo info : adviceBqyzInfoList) {
                        ea.ExecutResult.add(new ExecuteResult(ea, "0", transfusionInfo.SYSJ, transfusionInfo.SJMC, info.YZMC, info.YZXH, ea.YHID,
                                super.MessageManager.Create("6"), "6", "-1",transfusionInfo.SYSJ,transfusionInfo.YZZH));
                    }
                    return ea;

                }


            } else {
                ea.ExecutResult.add(new ExecuteResult(ea, "0", null, null, null, null, ea.YHID,
                        super.MessageManager.Create("14"), "14", "-1",null,null));
                return ea;
            }
        }

        return null;
    }
}
