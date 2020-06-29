package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.*;
import com.bsoft.nis.util.date.DateUtil;

/**
 * Description: 皮试相关流程控制
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 14:35
 * Version:
 */
public class SkinTestCheck extends FlowControl {
    public SkinTestCheck() {
        super("0");
    }

    @Override
    public ExecuteArg BranchCall(ExecuteArg ea, Object[] objs) {
        String now = DateUtil.getApplicationDateTime();
        for (int i = 0; i < ea.PlanInfoList.size(); i++) {
            PlanInfo planInfo = ea.PlanInfoList.get(i);
            String resultType;
            AdviceBqyzInfo adviceBqyzInfo = null;
            for (AdviceBqyzInfo info : ea.AdviceBqyzInfoList) {
                if (info.YZXH.equals(planInfo.YZXH)) {
                    adviceBqyzInfo = info;
                    break;
                }
            }
            if (adviceBqyzInfo != null) {
                /*switch (adviceBqyzInfo.PSPB) {
                    case "0":
                        resultType = "26";
                        break;
                    case "1":
                        resultType = "27";
                        break;
                    case "2":
                        resultType = "28";
                        break;
                    case "3":
                        resultType = "29";
                        break;
                    default:
                        resultType = "30";
                }*/
                resultType = "30";
                if("1".equals(adviceBqyzInfo.PSPB)) {
                    //需要皮试，执行时判断皮试结果
                    if ("-1".equals(adviceBqyzInfo.PSJG) || "2".equals(adviceBqyzInfo.PSJG)) {
                        //皮试结果为阴性或者续注的允许执行
                        resultType = "30";
                    } else if ("1".equals(adviceBqyzInfo.PSJG)) {
                        //皮试结果为阳性，则不允许执行相应医嘱
                        resultType = "27";
                    } else {
                        // 如果值为0或者其他无效值，都默认皮试未知
                        resultType = "26";
                    }
                }else{
                    //不需要皮试
                    resultType = "30";
                }
            } else {
                resultType = "26";
            }
            if (!resultType.equals("30")) {
                i = i - ea.RemovePlan(planInfo);
                ea.ExecutResult.add(new ExecuteResult(ea, planInfo.JHH, now, planInfo.SJMC, planInfo.YZMC, planInfo.YZXH, ea.YHID,
                        super.MessageManager.Create(resultType), resultType, planInfo.ZXZT,planInfo.JHSJ,planInfo.YZZH));
                return ea;
            }

        }

        return null;
    }
}
