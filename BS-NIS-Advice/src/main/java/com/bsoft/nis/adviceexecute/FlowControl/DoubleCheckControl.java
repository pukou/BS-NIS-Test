package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.*;
import com.bsoft.nis.util.date.DateConvert;
import com.bsoft.nis.util.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 双核对流程控制
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-26
 * Time: 13:55
 * Version:
 */
public class DoubleCheckControl extends FlowControl {
    public DoubleCheckControl() {
        super("0");
    }

    @Override
    public ExecuteArg BranchCall(ExecuteArg ea, Object[] objs) {
        boolean isdouble = false;
        if (super.ParameterManager.getParameterMap().get(ea.JGID).DoubleCheck) {
            String now = DateUtil.getApplicationDateTime();
            List<PlanInfo> planInfoList = new ArrayList<>();
            for (PlanInfo planInfo : ea.PlanInfoList) {
                if (!StringUtils.isBlank(planInfo.SRHDBZ) && planInfo.SRHDBZ.equals("1")) {
                    if (planInfo.ZXZT.equals("0")) {
                        if (StringUtils.isBlank(planInfo.KSHDSJ) && StringUtils.isBlank(planInfo.KSHDGH)) {
                            planInfo.KSHDGH = ea.YHID;
                            planInfo.KSHDSJ = now;
                            planInfoList.add(planInfo);
                            ea.ExecutResult.add(new ExecuteResult(ea, planInfo.JHH, planInfo.JHSJ, planInfo.SJMC, planInfo.YZMC, planInfo.YZXH, ea.YHID,
                                    super.MessageManager.Create("42"), "23", planInfo.ZXZT,planInfo.JHSJ,planInfo.YZZH));
                            isdouble = true;
                        } else {
                            //核对人与执行人是同一个
                            if (planInfo.KSHDGH.equals(ea.YHID)) {
                                planInfo.KSHDGH = ea.YHID;
                                planInfo.KSHDSJ = now;
                                planInfoList.add(planInfo);
                                ea.ExecutResult.add(new ExecuteResult(ea, planInfo.JHH, planInfo.JHSJ, planInfo.SJMC, planInfo.YZMC, planInfo.YZXH, ea.YHID,
                                        super.MessageManager.Create("43"), "23", planInfo.ZXZT,planInfo.JHSJ,planInfo.YZZH));
                                isdouble = true;
                            } else {
                                long interval = 0;//单位是秒
                                try {
                                    interval = (DateConvert.toDateTime(now, "yyyy-MM-dd HH:mm:SS").getTime() -
                                            DateConvert.toDateTime(planInfo.KSHDSJ, "yyyy-MM-dd HH:mm:SS").getTime()) / 1000;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (super.ParameterManager.getParameterMap().get(ea.JGID).DoubleCheckTimeOut < interval) {
                                    planInfo.KSHDGH = ea.YHID;
                                    planInfo.KSHDSJ = now;
                                    planInfoList.add(planInfo);
                                    ea.ExecutResult.add(new ExecuteResult(ea, planInfo.JHH, planInfo.JHSJ, planInfo.SJMC, planInfo.YZMC, planInfo.YZXH, ea.YHID,
                                            super.MessageManager.Create("44"), "23", planInfo.ZXZT,planInfo.JHSJ,planInfo.YZZH));
                                    isdouble = true;

                                }
                            }
                        }
                    }
                }
            }

            if (planInfoList.size() > 0) {
                AdviceArg adviceArg = new AdviceArg();
                adviceArg.ClassName = "DoubleCheckControl";
                adviceArg.PlanInfoList = planInfoList;
                adviceArg.RecordInfo = ea.RecordInfo;
                ea.AdviceArgList.add(adviceArg);
            }
        }

        return isdouble ? ea : null;
    }
}
