package com.bsoft.nis.domain.adviceexecute;

/**
 * Description: 执行结果类
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-20
 * Time: 10:13
 * Version:
 */
public class ExecuteResult {

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
    public String ResultType;

    /**
     * 异常信息
     */
    public String Msg;

    /**
     * 执行人
     */
    public String ZXGH;

    /**
     * 计划执行包
     */
    public ExecuteArg ExecutArg;

    /**
     * 计划号
     */
    public String JHH;

    /**
     * 医嘱序号
     */
    public String YZXH;

    /**
     * 计划时间
     */
    public String ZXSJ;

    /**
     * 医嘱名称
     */
    public String YZMC;

    /**
     * 时间名称
     */
    public String SJMC;

    /**
     * 包的执行状态
     * -1 空
     * 0 未执行
     * 1 已执行
     * 2 执行中
     * 3 作废
     * 4 暂停中
     * 5 已拒绝
     */
    public String ZXZT;
    //add 2018-4-25 18:47:59
    public String JHSJ;
    public String YZZH;

    /**
     * 构造方法
     */
    public ExecuteResult(ExecuteArg ea, String jhh, String zxsj, String sjmc, String yzmc, String yzxh,
                         String zxgh, String msg, String resultType, String zxzt,String jhsj, String yzzh) {
        ExecutArg = ea;
        JHH = jhh;
        ZXSJ = zxsj;
        YZXH = yzxh;
        YZMC = yzmc;
        SJMC = sjmc;
        ZXGH = zxgh;
        Msg = msg;
        ResultType = resultType;
        ZXZT = zxzt;
        //add 2018-4-25 18:47:59
        JHSJ = jhsj;
        YZZH = yzzh;
    }
}
