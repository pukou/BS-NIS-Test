package com.bsoft.nis.adviceexecute.FlowControl;

import com.bsoft.nis.domain.adviceexecute.AdviceBqyzInfo;
import com.bsoft.nis.domain.adviceexecute.ExecuteArg;
import com.bsoft.nis.domain.adviceexecute.ExecuteResult;
import com.bsoft.nis.domain.adviceexecute.PlanInfo;
import com.bsoft.nis.util.date.DateUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Description: 医嘱相关流程控制
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 11:29
 * Version:
 */
public class AdviceGeneralCheck extends FlowControl {
    public AdviceGeneralCheck() {
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
                switch (adviceBqyzInfo.YZZT) {
                    case "3":
                        resultType = "7";
                        break;
                    case "5":
                        //单独转换YCJL，避免非数字，转换异常
                        float ycjl1 = 0, ycjl2 = -1;
                        try{
                            ycjl1 = Float.parseFloat(planInfo.YCJL);
                            ycjl2 = Float.parseFloat(adviceBqyzInfo.YCJL);
                        }catch (Exception ex){}

                        if (planInfo.YZXH.equals(adviceBqyzInfo.YZXH) && planInfo.YZZH.equals(adviceBqyzInfo.YZZH) && planInfo.ZYH.equals(adviceBqyzInfo.ZYH) &&
                                //planInfo.YCJL.equals(adviceBqyzInfo.YCJL) && planInfo.JLDW.equals(adviceBqyzInfo.JLDW)) {
                                ycjl1 == ycjl2 && planInfo.JLDW.equals(adviceBqyzInfo.JLDW)) {
                            resultType = "0";
                        } else {
                            resultType = "21";
                        }
                        break;
                    case "4":
                        //resultType = "8";
                        //判断医嘱计划的计划时间是否在停嘱时间之前，在之前的允许执行，否则不允许执行
                        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        //去掉时间的毫秒(如 2019-05-08 12:33:44.555,去掉字符串的.555部分)
                        if(planInfo.JHSJ != null) planInfo.JHSJ = planInfo.JHSJ.replaceAll("(\\d{2}:\\d{2}:\\d{2})[.]\\d{1,3}", "$1");
                        if(adviceBqyzInfo.TZSJ != null) adviceBqyzInfo.TZSJ = adviceBqyzInfo.TZSJ.replaceAll("(\\d{2}:\\d{2}:\\d{2})[.]\\d{1,3}", "$1");

                        LocalDateTime dtJHSJ = LocalDateTime.parse(planInfo.JHSJ, formater);
                        LocalDateTime dtTZSJ = LocalDateTime.parse(adviceBqyzInfo.TZSJ, formater);

                        //有停嘱截止时间时处理, TZJZ格式(HH:mm)时分
                        if(!(planInfo.TZJZ == null || planInfo.TZJZ.equals("") || planInfo.TZJZ.equals("00:00"))){
                            dtTZSJ = LocalDateTime.of(dtTZSJ.toLocalDate(), LocalTime.parse(planInfo.TZJZ));
                        }

                        //只限制未开始执行的医嘱计划
                        if("0".equals(planInfo.ZXZT)) {
                            //dtJHSJ < dtTZSJ
                            if (dtJHSJ.isBefore(dtTZSJ)) {
                                //计划时间在停嘱时间之前,属预停嘱，允许执行
                                resultType = "0";
                            } else {
                                //已经停嘱
                                resultType = "8";
                            }
                        }else{
                            //执行中的医嘱计划允许结束操作
                            resultType = "0";
                        }

                        break;
                    default:
                        resultType = "22";
                }
            } else {
                resultType = "6";
            }
            if (!resultType.equals("0")) {
                i = i - ea.RemovePlan(planInfo);
                ea.ExecutResult.add(new ExecuteResult(ea, planInfo.JHH, now, planInfo.SJMC, planInfo.YZMC, planInfo.YZXH, ea.YHID,
                        super.MessageManager.Create(resultType), resultType, planInfo.ZXZT,planInfo.JHSJ,planInfo.YZZH));
                return ea;
            }

        }

        return null;
    }

}
