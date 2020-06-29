package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 验证医嘱跟口服明细数据是否匹配
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 14:14
 * Version:
 */
public class OralGeneralCheck extends FlowControl {
    public OralGeneralCheck() {
        super("3");
    }

    @Override
    public ExecuteArg BranchCall(ExecuteArg ea, Object[] objs) {
        if (ea.GSLX.equals(super.GSLX) && ea.HandExcute &&
                super.ParameterManager.getParameterMap().get(ea.JGID).OralHandExcuteCheckKfd) {
            OralInfo oralInfo = (OralInfo) ea.RecordInfo;
            if (oralInfo != null) {
                int pcount = 0;
                for (OralPackageInfo oralPackageInfo : oralInfo.Packages) {
                    pcount += oralPackageInfo.Details.size();
                }
                if (pcount > ea.AdviceBqyzInfoList.size()) {
                    List<OralDetailInfo> details = new ArrayList<>();
                    for (OralPackageInfo oralPackageInfo : oralInfo.Packages) {
                        for (OralDetailInfo detail : oralPackageInfo.Details) {
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
                    }
                    for (OralDetailInfo detail : details) {
                        ea.ExecutResult.add(new ExecuteResult(ea, "0", oralInfo.KFSJ, detail.SJMC, null, detail.YZXH, ea.YHID,
                                super.MessageManager.Create("6"), "6", "-1",null,null));

                    }
                    return ea;

                } else if (pcount < ea.AdviceBqyzInfoList.size()) {
                    List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
                    for (AdviceBqyzInfo info : ea.AdviceBqyzInfoList) {
                        boolean isSelect = false;
                        for (OralPackageInfo oralPackageInfo : oralInfo.Packages) {
                            for (OralDetailInfo detail : oralPackageInfo.Details) {
                                if (detail.YZXH.equals(info.YZXH)) {
                                    isSelect = true;
                                    break;
                                }
                            }
                        }
                        if (!isSelect) {
                            adviceBqyzInfoList.add(info);
                        }
                    }
                    for (AdviceBqyzInfo info : adviceBqyzInfoList) {
                        String sjmc = oralInfo.Packages.size() > 0
                                ? (oralInfo.Packages.get(0).Details.size() > 0
                                ? oralInfo.Packages.get(0).Details.get(0).SJMC : null) : null;
                        ea.ExecutResult.add(new ExecuteResult(ea, "0", oralInfo.KFSJ, sjmc, info.YZMC, info.YZXH, ea.YHID,
                                super.MessageManager.Create("6"), "6", "-1",null,null));
                    }
                    return ea;

                }

            } else {
                ea.ExecutResult.add(new ExecuteResult(ea, "0", null, null, null, null, ea.YHID,
                        super.MessageManager.Create("24"), "24", "-1",null,null));
                return ea;
            }
        }

        return null;
    }
}
