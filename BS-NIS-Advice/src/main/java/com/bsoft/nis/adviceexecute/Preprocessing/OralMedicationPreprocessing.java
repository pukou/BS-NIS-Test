package com.bsoft.nis.adviceexecute.Preprocessing;

import com.bsoft.nis.adviceexecute.ModelManager.*;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.adviceexecute.*;
import com.bsoft.nis.util.date.DateConvert;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 口服用药预处理
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-21
 * Time: 15:19
 * Version:
 */
@Component
public class OralMedicationPreprocessing {

    @Autowired
    ParameterManager parameterManager;//用户参数管理器

    @Autowired
    PlanInfoManager planInfoManager;//医嘱计划管理器

    @Autowired
    AdviceBqyzInfoManager adviceBqyzInfoManager;//病区医嘱管理器

    @Autowired
    AdviceYzbInfoManager adviceYzbInfoManager;//医嘱本管理器

    @Autowired
    OralInfoManager oralInfoManager;//口服单管理器

    @Autowired
    MessageManager messageManager;//消息生成管理器

    @Autowired
    DateTimeService timeService; // 日期时间服务

    private Log logger = LogFactory.getLog(OralMedicationPreprocessing.class);

    /**
     * 计划提交预处理
     *
     * @param gslx            归属类型
     * @param zyh             住院号
     * @param yhid            用户id
     * @param planArgInfoList
     * @param jgid            机构id
     * @return
     */
    public List<ExecuteArg> Preprocessing(String gslx, String zyh, String yhid, List<PlanArgInfo> planArgInfoList, String jgid) {
        List<ExecuteArg> ealist = null;
        try {
            ealist = new ArrayList<>();
            List<String> del = new ArrayList<>();
            for (PlanArgInfo planArgInfo : planArgInfoList) {
                PlanInfo planInfo = planInfoManager.getPlanInfoByJhh(planArgInfo.JHH, jgid);
                if (planInfo.GSLX.equals(gslx) && !del.contains(planInfo.JHH)) {
                    AdviceBqyzInfo adviceBqyzInfo = adviceBqyzInfoManager.getAdviceBqyzInfo(zyh, planInfo.YZXH, jgid);
                    List<PlanInfo> planInfoList = new ArrayList<>();
                    planInfoList.add(planInfo);
                    List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
                    adviceBqyzInfoList.add(adviceBqyzInfo);
                    if (parameterManager.getParameterMap().get(jgid).OralHandExcuteCheckKfd) {
                        boolean oralJoinPlanByTime = parameterManager.getParameterMap().get(jgid).OralJoinPlanByTime;
                        OralInfo oralInfo = oralInfoManager.getOralInfoByJhh(planInfo.JHH, oralJoinPlanByTime, jgid);
                        //执行包
                        if (null != oralInfo) {
                            if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                                AdviceYzbInfo adviceYzbInfo = adviceYzbInfoManager.getAdviceYzbInfo(zyh, planInfo.YSYZBH, jgid);
                                List<AdviceYzbInfo> adviceYzbInfoList = new ArrayList<>();
                                adviceYzbInfoList.add(adviceYzbInfo);
                                ealist.add(new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, oralInfo, planArgInfo.FYXH, oralInfo.KFDH, yhid, true, jgid));

                            } else {
                                ealist.add(new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, oralInfo, planArgInfo.FYXH, oralInfo.KFDH, yhid, true, jgid));
                            }
                            List<PlanInfo> planInfoListByQrdh = planInfoManager.getPlanInfoListByQrdh(oralInfo.KFDH, gslx, jgid);
                            if (planInfoListByQrdh != null && planInfoListByQrdh.size() > 0) {
                                String jhh = "";
                                for (PlanInfo info : planInfoListByQrdh) {
                                    if (info.JHH.equals(planInfo.JHH)) {
                                        jhh = info.JHH;
                                        break;
                                    }
                                }
                                del.add(jhh);
                            }

                        } else {
                            del.add(planInfo.JHH);
                            ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, null, planArgInfo.FYXH, "0", yhid, true, jgid);
                            ea.ExecutResult.add(new ExecuteResult(ea, planInfo.JHH, timeService.now(DataSource.MOB), "", adviceBqyzInfo.YZMC,
                                    adviceBqyzInfo.YZXH, yhid, messageManager.Create("24"), "24", "-1", planInfo.JHSJ, planInfo.YZZH));
                            ealist.add(ea);
                        }
                    } else {
                        //add 2018-4-27 12:24:59
                        boolean oralJoinPlanByTime = parameterManager.getParameterMap().get(jgid).OralJoinPlanByTime;
                        OralInfo oralInfo = oralInfoManager.getOralInfoByJhh(planInfo.JHH, oralJoinPlanByTime, jgid);
                        //add 2018-4-27 12:24:59
                        if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                            AdviceYzbInfo adviceYzbInfo = adviceYzbInfoManager.getAdviceYzbInfo(zyh, planInfo.YSYZBH, jgid);
                            List<AdviceYzbInfo> adviceYzbInfoList = new ArrayList<>();
                            adviceYzbInfoList.add(adviceYzbInfo);
                            ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, oralInfo, planArgInfo.FYXH, oralInfo == null ? "0" : oralInfo.KFDH, yhid, true, jgid);
                            ealist.add(ea);

                        } else {
                            ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, oralInfo, planArgInfo.FYXH, oralInfo == null ? "0" : oralInfo.KFDH, yhid, true, jgid);
                            ealist.add(ea);

                        }
                        del.add(planInfo.JHH);
                    }

                }
            }
            //清理
            for (int i = 0; i < planArgInfoList.size(); i++) {
                if (del.contains(planArgInfoList.get(i).JHH)) {
                    planArgInfoList.remove(i);
                    i--;
                    continue;
                }
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ealist;
    }

    /**
     * 扫描预处理
     *
     * @param zyh     住院号
     * @param yhid    用户id
     * @param barcode 条码内容
     * @param prefix  条码前缀
     * @param jgid    机构id
     * @return
     */
    public PreprocessingScannResult ScanPreprocessing(String zyh, String yhid, String barcode, String prefix, String jgid) {
        PreprocessingScannResult preprocessingScannResult = null;
        try {
            OralInfo oralInfo = oralInfoManager.getOralInfoByBarcode(barcode, prefix,
                    parameterManager.getParameterMap().get(jgid).OralUsePrefix, jgid);
            if (oralInfo != null) {
                if (!oralInfo.ZYH.equals(zyh)) {
                    preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("37"));
                } else if (oralInfo.Packages.size() > 0 && !oralInfo.Packages.get(0).KFZT.equals("0")) {
                    preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("38"));
                } else if (oralInfo.Packages.size() > 0 && oralInfo.Packages.get(0).ZFBZ.equals("1")) {
                    preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("33"));
                } else {
                    List<PlanInfo> planInfoList = new ArrayList<>();
                    if (parameterManager.getParameterMap().get(jgid).OralJoinPlanByTime) {
                        for (OralPackageInfo packageInfo : oralInfo.Packages) {
                            for (OralDetailInfo info : packageInfo.Details) {
                                PlanInfo planInfo = planInfoManager.getPlanInfoByYzxhAndJhsj(info.YZXH, DateConvert.getStandardString(oralInfo.KFSJ), jgid);
                                planInfoList.add(planInfo);
                            }
                        }
                    } else {
                        String jhrq = String.valueOf(DateConvert.toLocalDateTime(DateConvert.toDateTime(oralInfo.KFSJ)).toLocalDate());
                        for (OralPackageInfo packageInfo : oralInfo.Packages) {
                            for (OralDetailInfo info : packageInfo.Details) {
                                PlanInfo planInfo = planInfoManager.getPlanInfoByYzxhAndSjbh(info.YZXH, info.SJBH, jhrq, jgid);
                                planInfoList.add(planInfo);
                            }
                        }
                    }
                    if (planInfoList.size() == 0) {
                        preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("39"));
                    } else {
                        List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
                        for (PlanInfo info : planInfoList) {
                            AdviceBqyzInfo adviceBqyzInfo = adviceBqyzInfoManager.getAdviceBqyzInfo(zyh, info.YZXH, jgid);
                            adviceBqyzInfoList.add(adviceBqyzInfo);
                        }
                        List<ExecuteArg> eaList = new ArrayList<>();
                        if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                            List<AdviceYzbInfo> adviceYzbInfoList = new ArrayList<>();
                            for (PlanInfo info : planInfoList) {
                                AdviceYzbInfo adviceYzbInfo = adviceYzbInfoManager.getAdviceYzbInfo(zyh, info.YSYZBH, jgid);
                                adviceYzbInfoList.add(adviceYzbInfo);
                            }
                            ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, oralInfo, oralInfo.KFDH, yhid, jgid);
                            eaList.add(ea);
                        } else {
                            ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, oralInfo, oralInfo.KFDH, yhid, jgid);
                            eaList.add(ea);
                        }
                             preprocessingScannResult = new PreprocessingScannResult(eaList);
                    }
                }
            } else {
                preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("24"));
            }

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            preprocessingScannResult = new PreprocessingScannResult("[扫描执行计划失败]数据库执行错误!");
        } catch (Exception e) {
            logger.error(e.getMessage());
            preprocessingScannResult = new PreprocessingScannResult("[扫描执行计划失败]内部错误!");
        }

        return preprocessingScannResult;
    }

    /**
     * 计划提交预处理
     *
     * @param kfdh 口服单号
     * @param zyh  住院号
     * @param yhid 用户id
     * @param jgid 机构id
     * @return
     */
    public ExecuteArg Preprocessing(String zyh, String yhid, String kfdh, String jgid) {
        return Preprocessing(zyh, yhid, kfdh, jgid, false);
    }

    public ExecuteArg Preprocessing(String zyh, String yhid, String kfdh, String jgid, boolean isCancel) {
        ExecuteArg ea = null;
        try {
            OralInfo oralInfo = oralInfoManager.getOralInfoByKfdh(kfdh, jgid);
            if (oralInfo != null) {
                if (!oralInfo.ZYH.equals(zyh)) {
                    ea = new ExecuteArg(null, null, zyh, oralInfo, null, kfdh, yhid, true, jgid);
                    ea.ExecutResult.add(new ExecuteResult(ea, "0", timeService.now(DataSource.MOB), "", "", "", yhid, messageManager.Create("37"), "37", "-1", null, null));
                } else if (oralInfo.Packages.size() > 0 && !oralInfo.Packages.get(0).KFZT.equals("0")&&!isCancel) {
                    ea = new ExecuteArg(null, null, zyh, oralInfo, null, kfdh, yhid, true, jgid);
                    ea.ExecutResult.add(new ExecuteResult(ea, "0", timeService.now(DataSource.MOB), "", "", "", yhid, messageManager.Create("38"), "38", "-1", null, null));
                } else if (oralInfo.Packages.size() > 0 && oralInfo.Packages.get(0).ZFBZ.equals("1")) {
                    ea = new ExecuteArg(null, null, zyh, oralInfo, null, kfdh, yhid, true, jgid);
                    ea.ExecutResult.add(new ExecuteResult(ea, "0", timeService.now(DataSource.MOB), "", "", "", yhid, messageManager.Create("33"), "33", "-1", null, null));
                } else {
                    List<PlanInfo> planInfoList = new ArrayList<>();
                    if (parameterManager.getParameterMap().get(jgid).OralJoinPlanByTime) {
                        for (OralPackageInfo packageInfo : oralInfo.Packages) {
                            for (OralDetailInfo info : packageInfo.Details) {
                                PlanInfo planInfo = planInfoManager.getPlanInfoByYzxhAndJhsj(info.YZXH, DateConvert.getStandardString(oralInfo.KFSJ), jgid);
                                planInfoList.add(planInfo);
                            }
                        }
                    } else {
                        String jhrq = String.valueOf(DateConvert.toLocalDateTime(DateConvert.toDateTime(oralInfo.KFSJ)).toLocalDate());
                        for (OralPackageInfo packageInfo : oralInfo.Packages) {
                            for (OralDetailInfo info : packageInfo.Details) {
                                PlanInfo planInfo = planInfoManager.getPlanInfoByYzxhAndSjbh(info.YZXH, info.SJBH, jhrq, jgid);
                                planInfoList.add(planInfo);
                            }
                        }
                    }
                    if (planInfoList.size() == 0) {
                        ea.ExecutResult.add(new ExecuteResult(ea, "0", timeService.now(DataSource.MOB), "", "", "", yhid, messageManager.Create("39"), "39", "-1", null, null));
                    } else {
                        List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
                        for (PlanInfo info : planInfoList) {
                            AdviceBqyzInfo adviceBqyzInfo = adviceBqyzInfoManager.getAdviceBqyzInfo(zyh, info.YZXH, jgid);
                            adviceBqyzInfoList.add(adviceBqyzInfo);
                        }
                        if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                            List<AdviceYzbInfo> adviceYzbInfoList = new ArrayList<>();
                            for (PlanInfo info : planInfoList) {
                                AdviceYzbInfo adviceYzbInfo = adviceYzbInfoManager.getAdviceYzbInfo(zyh, info.YSYZBH, jgid);
                                adviceYzbInfoList.add(adviceYzbInfo);
                            }
                            ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, oralInfo, null, oralInfo.KFDH, yhid, true, jgid);
                        } else {
                            ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, oralInfo, null, oralInfo.KFDH, yhid, true, jgid);
                        }
                    }
                }
            } else {
                ea = new ExecuteArg(null, null, zyh, null, null, kfdh, yhid, true, jgid);
                ea.ExecutResult.add(new ExecuteResult(ea, "0", timeService.now(DataSource.MOB), "", "", "", yhid, messageManager.Create("24"), "24", "-1", null, null));
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ea;
    }

    /**
     * 给ea.FlowRecordInfo赋值
     *
     * @param ea
     * @return
     */
    public void getRealExecuteArg(ExecuteArg ea) {
        try {
            //类型相同，在包中,不是手动执行
            if (ea.GSLX.equals("3") && !StringUtils.isBlank(ea.QRDH) && !ea.HandExcute) {
                OralInfo oralInfo = (OralInfo) ea.RecordInfo;
                if (oralInfo.Packages.size() > 1) {//多条时返回明细数据
                    FlowRecordInfo flowRecordInfo = new FlowRecordInfo();
                    flowRecordInfo.TableName = "KF";
                    List<FlowRecordDetailInfo> list = oralInfoManager.getFlowRecordDetailInfoListForOral(ea.QRDH, ea.JGID);
                    if (list == null || list.size() == 0) {
                        list = new ArrayList<>();
                        FlowRecordDetailInfo flowRecordDetailInfo = new FlowRecordDetailInfo();
                        flowRecordDetailInfo.QRDH = ea.QRDH;
                        list.add(flowRecordDetailInfo);
                    } else {
                        for (FlowRecordDetailInfo info : list) {
                            info.JLXX = info.BZJL + info.JLDW;
                            info.SLXX = info.BZSL + info.SLDW;
                            info.QRDH = ea.QRDH;
                        }
                    }
                    flowRecordInfo.list = list;
                    ea.FlowRecordInfo = flowRecordInfo;
                }
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
