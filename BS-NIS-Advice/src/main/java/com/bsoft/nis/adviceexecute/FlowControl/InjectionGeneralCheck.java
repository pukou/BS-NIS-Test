package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 验证医嘱跟注射明细数据是否匹配
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 13:50
 * Version:
 */
public class InjectionGeneralCheck extends FlowControl {
    public InjectionGeneralCheck() {
        super("5");
    }

    @Override
    public ExecuteArg BranchCall(ExecuteArg ea, Object[] objs) {
        if (ea.GSLX.equals(super.GSLX) && ea.HandExcute &&
                super.ParameterManager.getParameterMap().get(ea.JGID).InjectionHandExcuteCheckZsd) {
            InjectionInfo injectionInfo = (InjectionInfo) ea.RecordInfo;
            if (injectionInfo != null) {
                if (injectionInfo.Details.size() > ea.AdviceBqyzInfoList.size()) {
                    List<InjectionDetailInfo> details = new ArrayList<>();
                    for (InjectionDetailInfo detail : injectionInfo.Details) {
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
                    for (InjectionDetailInfo detail : details) {
                        ea.ExecutResult.add(new ExecuteResult(ea, "0", injectionInfo.ZSSJ, injectionInfo.SJMC, null, detail.YZXH, ea.YHID,
                                super.MessageManager.Create("6"), "6", "-1",injectionInfo.ZSSJ,injectionInfo.YZZH));

                    }
                    return ea;

                } else if (injectionInfo.Details.size() < ea.AdviceBqyzInfoList.size()) {
                    List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
                    for (AdviceBqyzInfo info : ea.AdviceBqyzInfoList) {
                        boolean isSelect = false;
                        for (InjectionDetailInfo detail : injectionInfo.Details) {
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
                        ea.ExecutResult.add(new ExecuteResult(ea, "0", injectionInfo.ZSSJ, injectionInfo.SJMC, info.YZMC, info.YZXH, ea.YHID,
                                super.MessageManager.Create("6"), "6", "-1",injectionInfo.ZSSJ,injectionInfo.YZZH));
                    }
                    return ea;

                }


            } else {
                ea.ExecutResult.add(new ExecuteResult(ea, "0", null, null, null, null, ea.YHID,
                        super.MessageManager.Create("25"), "25", "-1",null,null));
                return ea;
            }
        }

        return null;
    }
}
