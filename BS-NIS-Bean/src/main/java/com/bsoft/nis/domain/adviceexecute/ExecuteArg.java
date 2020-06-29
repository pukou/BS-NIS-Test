package com.bsoft.nis.domain.adviceexecute;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 执行参数
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-19
 * Time: 14:11
 * Version:
 */
public class ExecuteArg {

    /**
     * 用户id
     */
    public String YHID;

    /**
     * 住院号
     */
    public String ZYH;

    /**
     * 病人病区
     */
    public String BRBQ;

    /**
     * 确认单号
     * SYDH,ZSDH,KFDH
     */
    public String QRDH;


    /**
     * 单条执行
     */
    public boolean Single = false;

    /**
     * 执行标志
     */
    public boolean ExecutAble = true;

    /**
     * 是否手动执行 否为扫描执行
     */
    public boolean HandExcute = false;

    /**
     * 参数
     */
    public Object Args;

    /**
     * 归属类型
     */
    public String GSLX;

    /**
     * 费用序号
     */
    public String FYXH;

    /**
     * 机构ID
     */
    public String JGID;
    //add  路数逻辑
    public String LSBS_Temp;

    /**
     * 电子单信息
     */
    public IRecordInfo RecordInfo;

    /**
     * 病区医嘱对象
     */
    public List<AdviceBqyzInfo> AdviceBqyzInfoList;

    /**
     * 医嘱计划对象
     */
    public List<PlanInfo> PlanInfoList;

    /**
     * 医嘱本对象
     */
    public List<AdviceYzbInfo> AdviceYzbInfoList;

    /**
     * 执行结果包
     */
    public List<ExecuteResult> ExecutResult = new ArrayList<>();

    /**
     * 流程控制过程产生的数据
     */
    public FlowRecordInfo FlowRecordInfo;

    /**
     * 所有流程里面准备好的要入库的数据对象
     */
    public List<AdviceArg> AdviceArgList = new ArrayList<>();

    /**
     * 构造方法(默认手动执行为false)-扫描执行用
     */
    public ExecuteArg(List<PlanInfo> planInfos, List<AdviceBqyzInfo> adviceBqyzInfos,
                      String zyh, IRecordInfo recordInfo, String qrdh, String yhid, String jgid) {
        if (null != planInfos && planInfos.size() > 0) {
            PlanInfoList = planInfos;
            GSLX = PlanInfoList.get(0).GSLX;
            BRBQ = PlanInfoList.get(0).BRBQ;
        } else {
            GSLX = "0";
            BRBQ = "";
        }
        if (null != adviceBqyzInfos && adviceBqyzInfos.size() > 0) {
            AdviceBqyzInfoList = adviceBqyzInfos;
        }
        AdviceYzbInfoList = new ArrayList<>();
        RecordInfo = recordInfo;
        QRDH = qrdh;
        YHID = yhid;
        ZYH = zyh;
        JGID = jgid;
    }

    /**
     * 构造方法(默认手动执行为false)-扫描执行用
     */
    public ExecuteArg(List<PlanInfo> planInfos, List<AdviceBqyzInfo> adviceBqyzInfos, List<AdviceYzbInfo> adviceYzbInfos,
                      String zyh, IRecordInfo recordInfo, String qrdh, String yhid, String jgid) {
        if (null != planInfos && planInfos.size() > 0) {
            PlanInfoList = planInfos;
            GSLX = PlanInfoList.get(0).GSLX;
            BRBQ = PlanInfoList.get(0).BRBQ;
        } else {
            GSLX = "0";
            BRBQ = "";
        }
        if (null != adviceBqyzInfos && adviceBqyzInfos.size() > 0) {
            AdviceBqyzInfoList = adviceBqyzInfos;
        }
        if (null != adviceYzbInfos && adviceYzbInfos.size() > 0) {
            AdviceYzbInfoList = adviceYzbInfos;
        } else {
            AdviceYzbInfoList = new ArrayList<>();
        }
        RecordInfo = recordInfo;
        QRDH = qrdh;
        YHID = yhid;
        ZYH = zyh;
        JGID = jgid;
    }


    /**
     * 构造方法-手动执行用
     */
    public ExecuteArg(List<PlanInfo> planInfos, List<AdviceBqyzInfo> adviceBqyzInfos,
                      String zyh, IRecordInfo recordInfo, String fyxh,
                      String qrdh, String yhid, boolean handExcute, String jgid) {
        if (null != planInfos && planInfos.size() > 0) {
            PlanInfoList = planInfos;
            GSLX = PlanInfoList.get(0).GSLX;
            BRBQ = PlanInfoList.get(0).BRBQ;
        } else {
            GSLX = "0";
            BRBQ = "";
        }
        if (null != adviceBqyzInfos && adviceBqyzInfos.size() > 0) {
            AdviceBqyzInfoList = adviceBqyzInfos;
        }
        AdviceYzbInfoList = new ArrayList<>();
        RecordInfo = recordInfo;
        QRDH = qrdh;
        YHID = yhid;
        ZYH = zyh;
        HandExcute = handExcute;
        JGID = jgid;
        FYXH = fyxh;
    }

    /**
     * 构造方法-手动执行用
     */
    public ExecuteArg(List<PlanInfo> planInfos, List<AdviceBqyzInfo> adviceBqyzInfos, List<AdviceYzbInfo> adviceYzbInfos,
                      String zyh, IRecordInfo recordInfo, String fyxh, String qrdh, String yhid, boolean handExcute, String jgid) {
        if (null != planInfos && planInfos.size() > 0) {
            PlanInfoList = planInfos;
            GSLX = PlanInfoList.get(0).GSLX;
            BRBQ = PlanInfoList.get(0).BRBQ;
        } else {
            GSLX = "0";
            BRBQ = "";
        }
        if (null != adviceBqyzInfos && adviceBqyzInfos.size() > 0) {
            AdviceBqyzInfoList = adviceBqyzInfos;
        }
        if (null != adviceYzbInfos && adviceYzbInfos.size() > 0) {
            AdviceYzbInfoList = adviceYzbInfos;
        } else {
            AdviceYzbInfoList = new ArrayList<>();
        }
        RecordInfo = recordInfo;
        QRDH = qrdh;
        YHID = yhid;
        ZYH = zyh;
        HandExcute = handExcute;
        JGID = jgid;
        FYXH = fyxh;
    }


    public int RemovePlan(PlanInfo planInfo) {
        int count = 0;
        for (int i = 0; i < PlanInfoList.size(); i++) {
            PlanInfo item = PlanInfoList.get(i);
            if (planInfo.JHH.equals(item.JHH)) {
                PlanInfoList.remove(item);
                i--;
                count++;
            }
        }
        for (int i = 0; i < AdviceBqyzInfoList.size(); i++) {
            AdviceBqyzInfo item = AdviceBqyzInfoList.get(i);
            if (planInfo.YZXH.equals(item.YZXH)) {
                AdviceBqyzInfoList.remove(item);
                i--;
            }
        }
        return count;
    }
}
