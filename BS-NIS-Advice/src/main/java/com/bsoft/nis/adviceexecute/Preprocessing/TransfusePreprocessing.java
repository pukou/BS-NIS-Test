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
 * Description: 输液用药预处理
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-21
 * Time: 15:19
 * Version:
 */
@Component
public class TransfusePreprocessing {

    @Autowired
    ParameterManager parameterManager;//用户参数管理器

    @Autowired
    PlanInfoManager planInfoManager;//医嘱计划管理器

    @Autowired
    AdviceBqyzInfoManager adviceBqyzInfoManager;//病区医嘱管理器

    @Autowired
    AdviceYzbInfoManager adviceYzbInfoManager;//医嘱本管理器

    @Autowired
    TransfuseInfoManager transfuseInfoManager;//输液单管理器

    @Autowired
    MessageManager messageManager;//消息生成管理器

    @Autowired
    DateTimeService timeService; // 日期时间服务

    private Log logger = LogFactory.getLog(TransfusePreprocessing.class);

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
                    boolean isSelectQrdh = false;
                    boolean isSelectYzzh = false;
                    for (ExecuteArg ea : ealist) {
                        if (ea.QRDH.equals(planInfo.QRDH)) {
                            isSelectQrdh = true;
                        }
                        if (ea.AdviceBqyzInfoList.get(0).YZZH.equals(planInfo.YZZH)) {
                            isSelectYzzh = true;
                        }
                    }
                    List<AdviceBqyzInfo> adviceBqyzInfoList = adviceBqyzInfoManager.getAdviceBqyzInfoList(zyh, planInfo.YZZH, jgid);
                    List<AdviceYzbInfo> adviceYzbInfoList = adviceYzbInfoManager.getAdviceYzbInfoList(zyh, planInfo.YZZH, jgid);
                    if (parameterManager.getParameterMap().get(jgid).TransfusionHandExcuteCheckSyd) {
                        List<PlanInfo> planInfoList;
                        boolean transfusionJoinPlanByTime = parameterManager.getParameterMap().get(jgid).TransfusionJoinPlanByTime;
                        TransfusionInfo transfusionInfo = transfuseInfoManager.getTransfusionInfoByJhh(planInfo.JHH, transfusionJoinPlanByTime, jgid);
                        if (null != transfusionInfo) {
                            if (transfusionJoinPlanByTime) {
                                planInfoList = planInfoManager.getPlanInfoListByYzzhAndJhsj(planInfo.YZZH, DateConvert.getStandardString(planInfo.JHSJ), jgid);
                            } else {
                                String jhrq = String.valueOf(DateConvert.toLocalDateTime(DateConvert.toDateTime(planInfo.JHSJ)).toLocalDate());
                                planInfoList = planInfoManager.getPlanInfoListByYzzhAndSjbh(planInfo.YZZH, planInfo.SJBH, jhrq, jgid);
                            }
                            if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                                if (!isSelectYzzh) {
                                    ealist.add(new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, transfusionInfo, planArgInfo.FYXH, transfusionInfo.SYDH, yhid, true, jgid));
                                }

                            } else {
                                if (!isSelectYzzh) {
                                    ealist.add(new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, transfusionInfo, planArgInfo.FYXH, transfusionInfo.SYDH, yhid, true, jgid));
                                }
                            }
                            if (planInfoList != null && planInfoList.size() > 0) {
                                String jhh = "";
                                for (PlanInfo info : planInfoList) {
                                    if (info.JHH.equals(planInfo.JHH)) {
                                        jhh = info.JHH;
                                        break;
                                    }
                                }
                                del.add(jhh);
                            }
                        } else {
                            del.add(planInfo.JHH);
                            if (transfusionJoinPlanByTime) {
                                planInfoList = planInfoManager.getPlanInfoListByYzzhAndJhsj(planInfo.YZZH, DateConvert.getStandardString(planInfo.JHSJ), jgid);
                            } else {
                                String jhrq = String.valueOf(DateConvert.toLocalDateTime(DateConvert.toDateTime(planInfo.JHSJ)).toLocalDate());
                                planInfoList = planInfoManager.getPlanInfoListByYzzhAndSjbh(planInfo.YZZH, planInfo.SJBH, jhrq, jgid);
                            }
                            if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                                ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, null, null, "0", yhid, true, jgid);
                                for (PlanInfo info : planInfoList) {
                                    ea.ExecutResult.add(new ExecuteResult(ea, info.JHH, timeService.now(DataSource.MOB), info.SJMC,
                                            info.YZMC, info.YZXH, yhid, messageManager.Create("14"), "14", info.ZXZT, info.JHSJ, info.YZZH));
                                }
                                if (!isSelectQrdh) {
                                    ealist.add(ea);
                                }
                            } else {
                                ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, null, planArgInfo.FYXH, "0", yhid, true, jgid);
                                for (PlanInfo info : planInfoList) {
                                    ea.ExecutResult.add(new ExecuteResult(ea, info.JHH, info.JHSJ, info.SJMC,
                                            info.YZMC, info.YZXH, yhid, messageManager.Create("14"), "14", info.ZXZT, info.JHSJ, info.YZZH));
                                }
                                if (!isSelectQrdh) {
                                    ealist.add(ea);
                                }
                            }
                        }
                    } else {
                        List<PlanInfo> planInfoList;
                        boolean transfusionJoinPlanByTime = parameterManager.getParameterMap().get(jgid).TransfusionJoinPlanByTime;
                        //add 2018-4-27 12:29:15
                        TransfusionInfo transfusionInfo = transfuseInfoManager.getTransfusionInfoByJhh(planInfo.JHH, transfusionJoinPlanByTime, jgid);
                        //add 2018-4-27 12:29:15
                        if (transfusionJoinPlanByTime) {
                            planInfoList = planInfoManager.getPlanInfoListByYzzhAndJhsj(planInfo.YZZH, DateConvert.getStandardString(planInfo.JHSJ), jgid);
                        } else {
                            String jhrq = String.valueOf(DateConvert.toLocalDateTime(DateConvert.toDateTime(planInfo.JHSJ)).toLocalDate());
                            planInfoList = planInfoManager.getPlanInfoListByYzzhAndSjbh(planInfo.YZZH, planInfo.SJBH, jhrq, jgid);
                        }
                        if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                            ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, transfusionInfo, null, transfusionInfo == null ? "0" : transfusionInfo.SYDH, yhid, true, jgid);
                            for (PlanInfo info : planInfoList) {
                                ea.ExecutResult.add(new ExecuteResult(ea, info.JHH, timeService.now(DataSource.MOB), info.SJMC,
                                        info.YZMC, info.YZXH, yhid, messageManager.Create("13"), "13", info.ZXZT, info.JHSJ, info.YZZH));
                            }
                            if (!isSelectQrdh) {
                                ealist.add(ea);
                            }
                        } else {
                            ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, transfusionInfo, planArgInfo.FYXH, transfusionInfo == null ? "0" : transfusionInfo.SYDH, yhid, true, jgid);
                            for (PlanInfo info : planInfoList) {
                                ea.ExecutResult.add(new ExecuteResult(ea, info.JHH, info.JHSJ, info.SJMC,
                                        info.YZMC, info.YZXH, yhid, messageManager.Create("13"), "13", info.ZXZT, info.JHSJ, info.YZZH));
                            }
                            if (!isSelectQrdh) {
                                ealist.add(ea);
                            }
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
            TransfusionInfo transfusionInfo = transfuseInfoManager.getTransfusionInfoByBarcode(barcode, prefix,
                    parameterManager.getParameterMap().get(jgid).TransfusionUsePrefix, jgid);
            if (transfusionInfo != null) {
                List<PlanInfo> planInfoList = null;
                if (parameterManager.getParameterMap().get(jgid).TransfusionJoinPlanByTime) {
                    planInfoList = planInfoManager.getPlanInfoListByYzzhAndJhsj(transfusionInfo.YZZH, DateConvert.getStandardString(transfusionInfo.SYSJ), jgid);
                } else {
                    String jhrq = String.valueOf(DateConvert.toLocalDateTime(DateConvert.toDateTime(transfusionInfo.SYSJ)).toLocalDate());
                    planInfoList = planInfoManager.getPlanInfoListByYzzhAndSjbh(transfusionInfo.YZZH, transfusionInfo.SJBH, jhrq, jgid);
                }
                if (planInfoList == null || planInfoList.size() == 0) {
                    preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("40"));
                } else {
                    if (!planInfoList.get(0).ZYH.equals(zyh)) {
                        preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("3"));
                    } else if (transfusionInfo.SYZT.equals("1")) {
                        preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("41"));
                    } else {
                        List<AdviceBqyzInfo> adviceBqyzInfoList = adviceBqyzInfoManager.getAdviceBqyzInfoList(zyh, transfusionInfo.YZZH, jgid);
                        List<ExecuteArg> eaList = new ArrayList<>();
                        if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                            List<AdviceYzbInfo> adviceYzbInfoList = adviceYzbInfoManager.getAdviceYzbInfoList(zyh, transfusionInfo.YZZH, jgid);
                            ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, transfusionInfo, transfusionInfo.SYDH, yhid, jgid);
                            eaList.add(ea);
                        } else {
                            ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, transfusionInfo, transfusionInfo.SYDH, yhid, jgid);
                            eaList.add(ea);
                        }
                        preprocessingScannResult = new PreprocessingScannResult(eaList);
                    }
                }
            } else {
                preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("14"));
            }

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            preprocessingScannResult = new PreprocessingScannResult("[扫描执行计划失败]数据库执行错误!");
        } catch (Exception e) {
            logger.error(e.getMessage());
            preprocessingScannResult = new PreprocessingScannResult("[扫描执行计划失败]服务内部错误!");
        }

        return preprocessingScannResult;
    }

    /**
     * 计划提交预处理
     *
     * @param zyh  住院号
     * @param yhid 用户id
     * @param sydh 输液单号
     * @param qrdh 确认单号
     * @param jgid 机构id
     * @return
     */
    public ExecuteArg Preprocessing(String zyh, String yhid, String sydh, String qrdh, String jgid) {
        return  Preprocessing(zyh, yhid, sydh, qrdh, jgid, false);
    }
    public ExecuteArg Preprocessing(String zyh, String yhid, String sydh, String qrdh, String jgid,boolean isCancel) {
        ExecuteArg ea = null;

        try {
            TransfusionInfo transfusionInfo = transfuseInfoManager.getTransfusionInfoBySydh(sydh, jgid);
            if (transfusionInfo != null) {
                List<PlanInfo> planInfoList = null;
                if (parameterManager.getParameterMap().get(jgid).TransfusionJoinPlanByTime) {
                    planInfoList = planInfoManager.getPlanInfoListByYzzhAndJhsj(transfusionInfo.YZZH, DateConvert.getStandardString(transfusionInfo.SYSJ), jgid);
                } else {
                    String jhrq = String.valueOf(DateConvert.toLocalDateTime(DateConvert.toDateTime(transfusionInfo.SYSJ)).toLocalDate());
                    planInfoList = planInfoManager.getPlanInfoListByYzzhAndSjbh(transfusionInfo.YZZH, transfusionInfo.SJBH, jhrq, jgid);
                }
                if (planInfoList != null && planInfoList.size() > 0) {
                    if (!planInfoList.get(0).ZYH.equals(zyh)) {
                        ea = new ExecuteArg(planInfoList, null, zyh, transfusionInfo, null, qrdh, yhid, true, jgid);
                        for (PlanInfo info : planInfoList) {
                            ea.ExecutResult.add(new ExecuteResult(ea, info.JHH, info.JHSJ, info.SJMC, info.YZMC, info.YZXH, yhid, messageManager.Create("3"), "3", info.ZXZT, info.JHSJ, info.YZZH));
                        }
                    } else if (transfusionInfo.SYZT.equals("1")&&!isCancel) {
                        ea = new ExecuteArg(planInfoList, null, zyh, transfusionInfo, null, qrdh, yhid, true, jgid);
                        for (PlanInfo info : planInfoList) {
                            ea.ExecutResult.add(new ExecuteResult(ea, info.JHH, info.JHSJ, info.SJMC, info.YZMC, info.YZXH, yhid, messageManager.Create("41"), "41", info.ZXZT, info.JHSJ, info.YZZH));
                        }
                    } else {
                        List<AdviceBqyzInfo> adviceBqyzInfoList = adviceBqyzInfoManager.getAdviceBqyzInfoList(zyh, transfusionInfo.YZZH, jgid);
                        if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                            List<AdviceYzbInfo> adviceYzbInfoList = adviceYzbInfoManager.getAdviceYzbInfoList(zyh, transfusionInfo.YZZH, jgid);
                            ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, transfusionInfo, null, transfusionInfo.SYDH, yhid, true, jgid);
                        } else {
                            ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, transfusionInfo, null, transfusionInfo.SYDH, yhid, true, jgid);
                        }
                    }
                }
            } else {
                ea = new ExecuteArg(null, null, zyh, null, null, qrdh, yhid, true, jgid);
                ea.ExecutResult.add(new ExecuteResult(ea, "0", timeService.now(DataSource.MOB), "", "", "", yhid, messageManager.Create("14"), "14", "-1", null, null));
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
            if (ea.GSLX.equals("4") && !StringUtils.isBlank(ea.QRDH) && ea.Args != null) {
                TransfusionInfo transfusionInfo = (TransfusionInfo) ea.RecordInfo;
                if (((TransfuseArgs) ea.Args).Ztpd) {//暂停处理
                    if (transfusionInfo.SYZT.equals("4")) {
                        FlowRecordInfo flowRecordInfo = new FlowRecordInfo();
                        flowRecordInfo.TableName = "SYZT";
                        flowRecordInfo.list = new ArrayList<>();
                        //change 2018-5-17 15:25:09
                        if (ea.PlanInfoList!=null&&ea.PlanInfoList.size()>0) {
                            for (PlanInfo planInfo : ea.PlanInfoList) {
                                FlowRecordDetailInfo info = new FlowRecordDetailInfo();
                                info.QRDH = ea.QRDH;
                                /////
                                info.JHSJ = planInfo.JHSJ;
                                info.YZZH = planInfo.YZZH;
                                flowRecordInfo.list.add(info);
                            }
                        }else {
                            //原有逻辑
                            FlowRecordDetailInfo info = new FlowRecordDetailInfo();
                            info.QRDH = ea.QRDH;
                            flowRecordInfo.list.add(info);
                        }
                        ///
                        ea.FlowRecordInfo = flowRecordInfo;
                    }
                } else if (!((TransfuseArgs) ea.Args).IsParallel && !((TransfuseArgs) ea.Args).Qzjs) {//多瓶控制
                    List<String> sydhList = transfuseInfoManager.getSydhListForDp(ea.ZYH, ea.JGID);
                    if (sydhList != null && sydhList.size() > 1) {//有多瓶药在执行
                        boolean isSelect = false;
                        for (String sydh : sydhList) {
                            if (sydh.equals(ea.QRDH)) {
                                ((TransfuseArgs) ea.Args).Qzjs = true;
                                isSelect = true;
                                break;
                            }
                        }
                        if (!isSelect) {
                            FlowRecordInfo flowRecordInfo = new FlowRecordInfo();
                            flowRecordInfo.TableName = "SY";
                            List<FlowRecordDetailInfo> list = transfuseInfoManager.getFlowRecordDetailInfoListForTran(ea.QRDH, ea.JGID);
                            if (list == null || list.size() == 0) {
                                /***********更改start***********/
//                                list = new ArrayList<>();
//                                FlowRecordDetailInfo flowRecordDetailInfo = new FlowRecordDetailInfo();
//                                flowRecordDetailInfo.QRDH = ea.QRDH;
//                                list.add(flowRecordDetailInfo);
                                ea.FlowRecordInfo = null;
                                /***********更改end**********/
                            } else {
                                for (FlowRecordDetailInfo info : list) {
                                    info.JLXX = info.YCJL + info.JLDW;
                                    info.SLXX = info.YCSL + info.SLDW;
                                    info.QRDH = ea.QRDH;
                                }
                                /***********更改start***********/
                                flowRecordInfo.list = list;
                                ea.FlowRecordInfo = flowRecordInfo;
                                /***********更改end***********/
                            }
                        }
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
