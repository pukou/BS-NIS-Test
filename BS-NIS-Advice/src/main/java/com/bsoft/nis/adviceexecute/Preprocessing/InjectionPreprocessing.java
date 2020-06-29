package com.bsoft.nis.adviceexecute.Preprocessing;

import com.bsoft.nis.adviceexecute.ModelManager.*;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.adviceexecute.*;
import com.bsoft.nis.util.date.DateConvert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 注射用药预处理
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-21
 * Time: 15:19
 * Version:
 */
@Component
public class InjectionPreprocessing {

    @Autowired
    ParameterManager parameterManager;//用户参数管理器

    @Autowired
    PlanInfoManager planInfoManager;//医嘱计划管理器

    @Autowired
    AdviceBqyzInfoManager adviceBqyzInfoManager;//病区医嘱管理器

    @Autowired
    AdviceYzbInfoManager adviceYzbInfoManager;//医嘱本管理器

    @Autowired
    InjectionInfoManager injectionInfoManager;//注射单管理器

    @Autowired
    MessageManager messageManager;//消息生成管理器

    @Autowired
    DateTimeService timeService; // 日期时间服务

    private Log logger = LogFactory.getLog(InjectionPreprocessing.class);

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
                    AdviceBqyzInfo adviceBqyzInfo = adviceBqyzInfoManager.getAdviceBqyzInfo(zyh,planInfo.YZXH, jgid);
                    List<AdviceBqyzInfo> adviceBqyzInfoList = adviceBqyzInfoManager.getAdviceBqyzInfoList(zyh,planInfo.YZZH, jgid);
                    //放到后面启用CheckYzb情况取值 提升性能
                    List<AdviceYzbInfo> adviceYzbInfoList = null;
                    if (parameterManager.getParameterMap().get(jgid).InjectionHandExcuteCheckZsd) {
                        List<PlanInfo> planInfoList;
                        boolean injectionJoinPlanByTime = parameterManager.getParameterMap().get(jgid).InjectionJoinPlanByTime;
                        boolean isSelect = false;
                        for (ExecuteArg ea : ealist) {
                            if (ea.QRDH.equals(planInfo.QRDH)) {
                                isSelect = true;
                                break;
                            }
                        }
                        InjectionInfo injectionInfo = injectionInfoManager.getInjectionInfoByJhh(planInfo.JHH, injectionJoinPlanByTime, jgid);
                        //执行包
                        if (null != injectionInfo) {
                            if (injectionJoinPlanByTime) {
                                planInfoList = planInfoManager.getPlanInfoListByYzzhAndJhsj(injectionInfo.YZZH, DateConvert.getStandardString(injectionInfo.ZSSJ), jgid);
                            } else {
                                String jhrq = String.valueOf(DateConvert.toLocalDateTime(DateConvert.toDateTime(injectionInfo.ZSSJ)).toLocalDate());
                                planInfoList = planInfoManager.getPlanInfoListByYzzhAndSjbh(injectionInfo.YZZH, injectionInfo.SJBH, jhrq, jgid);
                            }
                            if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                                if (!isSelect) {
                                    //初始化
                                     adviceYzbInfoList = adviceYzbInfoManager.getAdviceYzbInfoList(zyh,planInfo.YZZH, jgid);
                                    ealist.add(new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, injectionInfo, planArgInfo.FYXH, injectionInfo.ZSDH, yhid, true, jgid));
                                }

                            } else {
                                if (!isSelect) {
                                    ealist.add(new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, injectionInfo, planArgInfo.FYXH, injectionInfo.ZSDH, yhid, true, jgid));
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
                            if (injectionJoinPlanByTime) {
                                planInfoList = planInfoManager.getPlanInfoListByYzzhAndJhsj(planInfo.YZZH, DateConvert.getStandardString(planInfo.JHSJ), jgid);
                            } else {
                                String jhrq = String.valueOf(DateConvert.toLocalDateTime(DateConvert.toDateTime(planInfo.JHSJ)).toLocalDate());
                                planInfoList = planInfoManager.getPlanInfoListByYzzhAndSjbh(planInfo.YZZH, planInfo.SJBH, jhrq, jgid);
                            }
                            if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                                //初始化
                                 adviceYzbInfoList = adviceYzbInfoManager.getAdviceYzbInfoList(zyh,planInfo.YZZH, jgid);
                                ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, null, planArgInfo.FYXH, "0", yhid, true, jgid);
                                for (PlanInfo info : planInfoList) {
                                    ea.ExecutResult.add(new ExecuteResult(ea, info.JHH, timeService.now(DataSource.MOB), info.SJMC,
                                            info.YZMC, info.YZXH, yhid, messageManager.Create("25"), "25", info.ZXZT,info.JHSJ,info.YZZH));
                                }
                                if (!isSelect) {
                                    ealist.add(ea);
                                }
                            } else {
                                List<PlanInfo> tempPlanInfoList = new ArrayList<>();
                                tempPlanInfoList.add(planInfo);
                                List<AdviceBqyzInfo> tempAdviceBqyzInfoList = new ArrayList<>();
                                tempAdviceBqyzInfoList.add(adviceBqyzInfo);
                                ExecuteArg ea = new ExecuteArg(tempPlanInfoList, tempAdviceBqyzInfoList, zyh, null, planArgInfo.FYXH, "0", yhid, true, jgid);
                                ea.ExecutResult.add(new ExecuteResult(ea, planInfo.JHH, timeService.now(DataSource.MOB), planInfo.SJMC, planInfo.YZMC, planInfo.YZXH,
                                        yhid, messageManager.Create("25"), "25", planInfo.ZXZT,planInfo.JHSJ,planInfo.YZZH));
                                if (!isSelect) {
                                    ealist.add(ea);
                                }

                            }
                        }
                    } else {
                        //add 2018-4-27 11:25:21
                        boolean injectionJoinPlanByTime = parameterManager.getParameterMap().get(jgid).InjectionJoinPlanByTime;
                        InjectionInfo injectionInfo = injectionInfoManager.getInjectionInfoByJhh(planInfo.JHH, injectionJoinPlanByTime, jgid);
                        //add 2018-4-27 11:25:21
                        List<PlanInfo> tempPlanInfoList = new ArrayList<>();
                        tempPlanInfoList.add(planInfo);
                        List<AdviceBqyzInfo> tempAdviceBqyzInfoList = new ArrayList<>();
                        tempAdviceBqyzInfoList.add(adviceBqyzInfo);
                        if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                            AdviceYzbInfo adviceYzbInfo = adviceYzbInfoManager.getAdviceYzbInfo(zyh,planInfo.YSYZBH, jgid);
                            List<AdviceYzbInfo> tempAdviceYzbInfoList = new ArrayList<>();
                            tempAdviceYzbInfoList.add(adviceYzbInfo);
                            //change 2018-4-27 11:25:21
                            ExecuteArg ea = new ExecuteArg(tempPlanInfoList, tempAdviceBqyzInfoList, tempAdviceYzbInfoList, zyh, injectionInfo, planArgInfo.FYXH, injectionInfo==null?"0":injectionInfo.ZSDH, yhid, true, jgid);
                            //change 2018-4-27 11:25:21
                            ealist.add(ea);

                        } else {
                            //change 2018-4-27 11:25:21
                            ExecuteArg ea = new ExecuteArg(tempPlanInfoList, tempAdviceBqyzInfoList, zyh, injectionInfo, planArgInfo.FYXH, injectionInfo==null?"0":injectionInfo.ZSDH, yhid, true, jgid);
                            //change 2018-4-27 11:25:21
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

    public ExecuteArg Preprocessing(String jhh,String zyh, String yhid,String qrdh, String jgid) {
        return Preprocessing(jhh,zyh, yhid, qrdh, jgid, false);
    }
    public ExecuteArg Preprocessing(String jhh,String zyh, String yhid,String qrdh, String jgid,boolean isCancel) {
        ExecuteArg ea = null;

        try {
            boolean injectionJoinPlanByTime = parameterManager.getParameterMap().get(jgid).InjectionJoinPlanByTime;
            InjectionInfo injectionInfo = injectionInfoManager.getInjectionInfoByJhh(jhh,injectionJoinPlanByTime, jgid);
            if (injectionInfo != null) {
                List<PlanInfo> planInfoList = null;
                if (parameterManager.getParameterMap().get(jgid).TransfusionJoinPlanByTime) {
                    planInfoList = planInfoManager.getPlanInfoListByYzzhAndJhsj(injectionInfo.YZZH, DateConvert.getStandardString(injectionInfo.ZSSJ), jgid);
                } else {
                    String jhrq = String.valueOf(DateConvert.toLocalDateTime(DateConvert.toDateTime(injectionInfo.ZSSJ)).toLocalDate());
                    planInfoList = planInfoManager.getPlanInfoListByYzzhAndSjbh(injectionInfo.YZZH, injectionInfo.SJBH, jhrq, jgid);
                }
                if (planInfoList != null && planInfoList.size() > 0) {
                    if (!planInfoList.get(0).ZYH.equals(zyh)) {
                        ea = new ExecuteArg(planInfoList, null, zyh, injectionInfo, null, qrdh, yhid, true, jgid);
                        for (PlanInfo info : planInfoList) {
                            ea.ExecutResult.add(new ExecuteResult(ea, info.JHH, info.JHSJ, info.SJMC, info.YZMC, info.YZXH, yhid, messageManager.Create("3"), "3", info.ZXZT, info.JHSJ, info.YZZH));
                        }
                    } else if (injectionInfo.ZSZT.equals("1")&&!isCancel) {
                        ea = new ExecuteArg(planInfoList, null, zyh, injectionInfo, null, qrdh, yhid, true, jgid);
                        for (PlanInfo info : planInfoList) {
                            ea.ExecutResult.add(new ExecuteResult(ea, info.JHH, info.JHSJ, info.SJMC, info.YZMC, info.YZXH, yhid, messageManager.Create("41"), "41", info.ZXZT, info.JHSJ, info.YZZH));
                        }
                    } else {
                        List<AdviceBqyzInfo> adviceBqyzInfoList = adviceBqyzInfoManager.getAdviceBqyzInfoList(zyh, injectionInfo.YZZH, jgid);
                        if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                            List<AdviceYzbInfo> adviceYzbInfoList = adviceYzbInfoManager.getAdviceYzbInfoList(zyh, injectionInfo.YZZH, jgid);
                            ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, injectionInfo, null, injectionInfo.ZSDH, yhid, true, jgid);
                        } else {
                            ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, injectionInfo, null, injectionInfo.ZSDH, yhid, true, jgid);
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
            InjectionInfo injectionInfo = injectionInfoManager.getInjectionInfoByBarcode(barcode, prefix,
                    parameterManager.getParameterMap().get(jgid).InjectionUsePrefix, jgid);
            if (injectionInfo != null) {
                if (!injectionInfo.ZYH.equals(zyh)) {
                    preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("34"));
                } else if (injectionInfo.ZSZT.equals("1")) {
                    preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("35"));
                } else {
                    List<PlanInfo> planInfoList = null;
                    if (parameterManager.getParameterMap().get(jgid).InjectionJoinPlanByTime) {
                        planInfoList = planInfoManager.getPlanInfoListByYzzhAndJhsj(injectionInfo.YZZH, DateConvert.getStandardString(injectionInfo.ZSSJ), jgid);
                    } else {
                        String jhrq = String.valueOf(DateConvert.toLocalDateTime(DateConvert.toDateTime(injectionInfo.ZSSJ)).toLocalDate());
                        planInfoList = planInfoManager.getPlanInfoListByYzzhAndSjbh(injectionInfo.YZZH, injectionInfo.SJBH, jhrq, jgid);
                    }
                    if (planInfoList.size() == 0) {
                        preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("36"));
                    } else {
                        List<AdviceBqyzInfo> adviceBqyzInfoList = adviceBqyzInfoManager.getAdviceBqyzInfoList(zyh,injectionInfo.YZZH, jgid);
                        List<ExecuteArg> eaList = new ArrayList<>();
                        if (parameterManager.getParameterMap().get(jgid).CheckYzb) {
                            List<AdviceYzbInfo> adviceYzbInfoList = adviceYzbInfoManager.getAdviceYzbInfoList(zyh,injectionInfo.YZZH, jgid);
                            ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, injectionInfo, injectionInfo.ZSDH, yhid, jgid);
                            eaList.add(ea);
                        } else {
                            ExecuteArg ea = new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, injectionInfo, injectionInfo.ZSDH, yhid, jgid);
                            eaList.add(ea);
                        }
                        preprocessingScannResult = new PreprocessingScannResult(eaList);
                    }
                }
            } else {
                preprocessingScannResult = new PreprocessingScannResult(messageManager.Create("25"));
            }

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            preprocessingScannResult = new PreprocessingScannResult("[扫描执行计划失败]数据库执行错误!");
        } catch (Exception e) {
            logger.error(e.getMessage());
            preprocessingScannResult = new PreprocessingScannResult("[扫描执行计划失败]数据库执行错误!");
        }

        return preprocessingScannResult;
    }
}
