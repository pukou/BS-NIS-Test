package com.bsoft.nis.adviceexecute.ModelManager;

import org.springframework.stereotype.Component;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-22
 * Time: 13:32
 * Version:
 */
@Component
public class MessageManager {

    /**
     * 执行结果类型
     * -1 未作任何操作
     * 0 成功
     * 1 不存在
     * 2 作废
     * 3 不属于病人
     * 4 重复执行
     * 5 时间错误
     * 6 医嘱不存在
     * 7 医嘱作废
     * 8 停瞩
     * 9 WDW医嘱作废
     * 10 WDW停瞩
     * 11 WDW错误
     * 12 输液错误
     * 13 输液单中缺少详细记录
     * 14 没有输液单
     * 15 口服药错误
     * 16 注射错误
     * 17 失败
     * 18 执行出错
     * 19 时间禁止
     * 20 时间提醒
     * 21 不符合医嘱
     * 22 医嘱错误
     * 23 需要双人核对
     * 24 口服药不存在
     * 25 注射单不存在
     * 26 皮试未知
     * 27 皮试阳性
     * 28 皮试阴性
     * 29 上次皮试未超过72小时
     * 30 不需要皮试
     * 31 注射单没有加药核对
     * 32 输液单没有加药核对
     */
    public String Create(String resultType) {
        switch (resultType) {
            case "-1":
                break;
            case "0":
                return "执行成功";
            case "1":
                return "计划不存在";
            case "2":
                return "计划已作废";
            case "3":
                return "该瓶输液不属于该病人";
            case "4":
                return "计划重复执行";
            case "5":
                return "时间错误";
            case "6":
                return "该医嘱无效(停嘱或作废)";
            case "7":
                return "该医嘱已作废";
            case "8":
                return "该医嘱已停嘱";
            case "9":
                return "WDW该医嘱已作废";
            case "10":
                return "WDW该医嘱已停嘱";
            case "11":
                return "WDW错误";
            case "12":
                return "输液执行失败";
            case "13":
                return "无法找到该瓶输液（输液单中缺少详细记录）,是否还未配液？";
            case "14":
                return "无法找到该瓶输液（没有输液单）,是否还未配液或者医生作废医嘱导致输液单作废,请核对？";
            case "15":
                return "口服单与医嘱不匹配";
            case "16":
                return "注射单与医嘱不匹配";
            case "17":
                return "执行失败";
            case "18":
                return "执行错误";
            case "19":
                return "该医嘱已超过执行时间";
            case "20":
                return "该医嘱已超过执行时间";
            case "21":
                return "计划与医嘱不匹配";
            case "22":
                return "该医嘱无效(停嘱/作废)";
            case "23":
                return "需要双人核对";
            case "24":
                return "无法找到该口服药，是否未进行口服包药？";
            case "25":
                return "无法找到该注射单,是否还未配液？";
            case "26":
                return "皮试未知";
            case "27":
                return "皮实阳性";
            case "28":
                return "皮试阴性";
            case "29":
                return "上次皮试未超过72小时";
            case "30":
                return "不需要皮试";
            case "31":
                return "注射单未进行加药核对";
            case "32":
                return "输液单未进行加药核对";
            case "33":
                return "该包口服药已作废";
            case "34":
                return "该注射单不属于该病人";
            case "35":
                return "该注射单已执行";
            case "36":
                return "无法找到该注射单对应的计划";
            case "37":
                return "该包口服药不属于该病人";
            case "38":
                return "该包口服药已执行";
            case "39":
                return "无法找到该口服药对应的计划";
            case "40":
                return "无法找到该输液单对应的计划";
            case "41":
                return "该瓶输液已执行";
            case "42":
                return "请下一位护士确认执行";
            case "43":
                return "核对人和执行人不能是同一个人";
            case "44":
                return "上次核对过期,请重新扫描核对";
            default:
                return "未知错误";
        }
        return "未知错误";
    }
}
