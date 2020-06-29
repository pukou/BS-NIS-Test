package com.bsoft.nis.adviceexecute.Operates;

import com.bsoft.nis.adviceexecute.ModelManager.*;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.adviceexecute.*;
import com.bsoft.nis.util.date.DateConvert;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Description: 医嘱真正的执行
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-28
 * Time: 14:58
 * Version:
 */
@Component
public class FinalOperate {

    @Autowired
    ParameterManager parameterManager;//用户参数管理器

    @Autowired
    MessageManager messageManager;//消息生成管理器

    @Autowired
    PlanInfoManager planInfoManager;//医嘱计划管理器

    @Autowired
    AdviceYzbInfoManager adviceYzbInfoManager;//医嘱本管理器

    @Autowired
    AdviceBqyzInfoManager adviceBqyzInfoManager;//病区医嘱管理器

    @Autowired
    OralInfoManager oralInfoManager;//口服单管理器

    @Autowired
    TransfuseInfoManager transfuseInfoManager;//输液单管理器

    @Autowired
    InjectionInfoManager injectionInfoManager;//注射单管理器

    @Autowired
    NurseDBOperate nurseDBOperate;//护理治疗类医嘱执行器

    @Autowired
    OralMedicationDBOperate oralMedicationDBOperate;//口服类医嘱执行器

    @Autowired
    TransfuseDBOperate transfuseDBOperate;//输液类医嘱执行器

    @Autowired
    InjectionDBOperate injectionDBOperate;//注射类医嘱执行器

    @Autowired
    DateTimeService timeService; // 日期时间服务
    /*
        升级编号【56010053】============================================= start
        多瓶超过2瓶转接瓶后提示选择接哪瓶的问题
        ================= Classichu 2017/11/14 16:25

        */
    public void Operate(ExecuteArg ea)
            throws SQLException, DataAccessException, ParseException {
        Operate(ea,null);
    }
    public void Operate(ExecuteArg ea,String transfuse_sp_sydh)
            throws SQLException, DataAccessException, ParseException {
        ExecuteResult(ea);
        nurseDBOperate.Operate(ea);
        oralMedicationDBOperate.Operate(ea);
        transfuseDBOperate.Operate(ea,transfuse_sp_sydh);
        injectionDBOperate.Operate(ea);
        FinalOperate(ea);
    }
    /* =============================================================== end */

    public void FinalOperate(ExecuteArg ea)
            throws SQLException, DataAccessException, ParseException {
        if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
            boolean end = false;
            boolean isSucess = false;
            for (AdviceArg adviceArg : ea.AdviceArgList) {
                if (adviceArg.ClassName.equals("DoubleCheckControl")) {
                    end = true;
                    isSucess = DoubleCheckControl(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("NurseDBOperate")) {
                    end = true;
                    isSucess = NurseDBOperate(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("InjectionDBOperate")) {
                    end = true;
                    isSucess = InjectionDBOperate(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("InjectionDBOperate_CancelStart")) {
                    end = false;
                    isSucess = InjectionDBOperate_CancelStart(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("OralMedicationDBOperate")) {
                    end = true;
                    isSucess = OralMedicationDBOperate(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("OralMedicationDBOperate_CancelStart")) {
                    end = false;
                    isSucess = OralMedicationDBOperate_CancelStart(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("TransfuseDBOperate_Start")) {
                    end = false;
                    isSucess = TransfuseDBOperateStart(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("TransfuseDBOperate_End")) {
                    end = true;
                    isSucess = TransfuseDBOperateEnd(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("TransfuseDBOperate_CancelEnd")) {
                    end = false;
                    isSucess = TransfuseDBOperateCancel(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("TransfuseDBOperate_CancelStart")) {
                    end = false;
                    isSucess = TransfuseDBOperateCancel(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("TransfuseDBOperate_Stop")) {
                    end = false;
                    isSucess = TransfuseDBOperateStop(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("TransfuseDBOperate_ForceEnd")) {
                    end = true;
                    isSucess = TransfuseDBOperateForceEnd(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                } else if (adviceArg.ClassName.equals("TransfuseDBOperate_Continue")) {
                    end = true;
                    isSucess = TransfuseDBOperateContinue(adviceArg, ea);
                    if (!isSucess) {
                        break;
                    }
                }
            }
            updatePlanSuccess(ea.ExecutResult, ea.YHID, end, isSucess, timeService.now(DataSource.MOB));
        }

    }

    public void DoubleCheckControlOperate(ExecuteArg ea)
            throws SQLException, DataAccessException, ParseException {
        if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
            for (AdviceArg adviceArg : ea.AdviceArgList) {
                if (adviceArg.ClassName.equals("DoubleCheckControl")) {
                    DoubleCheckControl(adviceArg, ea);
                }
            }

        }
    }

    private boolean DoubleCheckControl(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException, ParseException {
        boolean isSucess = false;
        for (PlanInfo planInfo : adviceArg.PlanInfoList) {
            isSucess = planInfoManager.editPlanInfoForDoubleCheckControl(planInfo) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        if (isSucess) {
            if (ea.GSLX.equals("3")) {
                isSucess = oralInfoManager.updateOralInfoForDoubleCheckControl(DateConvert.getStandardString(timeService.now(DataSource.MOB)), ea.YHID,
                        (OralInfo) ea.RecordInfo, ea.JGID) > 0;
            } else if (ea.GSLX.equals("4")) {
                isSucess = transfuseInfoManager.editTransfusionInfoForDoubleCheckControl(DateConvert.getStandardString(timeService.now(DataSource.MOB)), ea.YHID,
                        (TransfusionInfo) ea.RecordInfo, ea.JGID) > 0;
            } else if (ea.GSLX.equals("5")) {
                isSucess = injectionInfoManager.editInjectionInfoForDoubleCheckControl(DateConvert.getStandardString(timeService.now(DataSource.MOB)), ea.YHID,
                        (InjectionInfo) ea.RecordInfo, ea.JGID) > 0;
            }
        }
        return isSucess;
    }

    private boolean NurseDBOperate(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException {
        boolean isSucess = false;
        for (PlanInfo planInfo : adviceArg.PlanInfoList) {
            isSucess = planInfoManager.editPlanInfo(planInfo) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        if (isSucess) {
            for (AdviceYzbInfo adviceYzbInfo : adviceArg.AdviceYzbInfoList) {
                /*升级编号【56010058】============================================= start
                                  需要核对医嘱本时候，planInfo.YSYZBH 为空 时候 导致PDA端 显示执行失败，但是刷新后已成功
                                   adviceYzbInfo.YZXH 来源于 planInfo.YSYZBH 为空 ，就直接不核对了
                            ================= classichu 2018/3/20 17:02
                            */
                if (TextUtils.isEmpty(adviceYzbInfo.YZXH)) {
                    return isSucess;//直接返回成功
                }
                /* =============================================================== end */

                isSucess = adviceYzbInfoManager.editAdviceYzbInfo(adviceYzbInfo) > 0;
                if (!isSucess) {
                    return isSucess;
                }
            }
            if (isSucess) {
                for (AdviceBqyzInfo adviceBqyzInfo : adviceArg.AdviceBqyzInfoList) {
                    isSucess = adviceBqyzInfoManager.editAdviceBqyzInfo(adviceBqyzInfo) > 0;
                    if (!isSucess) {
                        return isSucess;
                    }
                }
                if (isSucess) {
                    for (PlanInfo planInfo : adviceArg.BQPlanInfoList) {
                        isSucess = planInfoManager.editBQPlanInfo(planInfo) > 0;
                        if (!isSucess) {
                            return isSucess;
                        }
                    }
                }
            }
        }
        return isSucess;

    }

    private boolean InjectionDBOperate(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException {
        boolean isSucess = false;
        if (!parameterManager.getParameterMap().get(ea.JGID).InjectionUpdate) {
            isSucess = planInfoManager.editPlanInfoList(adviceArg.PlanInfoList.get(0)) > 0;
            if (!isSucess) {
                return isSucess;
            }
        } else {
            InjectionInfo recordInfo = (InjectionInfo) adviceArg.RecordInfo;
            if (recordInfo.ZSZT.equals("1")) {
                isSucess = injectionInfoManager.editInjectionInfoJs(recordInfo) > 0;
                if (!isSucess) {
                    return isSucess;
                }
                isSucess = planInfoManager.editPlanInfoListJs(adviceArg.PlanInfoList.get(0)) > 0;
                if (!isSucess) {
                    return isSucess;
                }
                for (AdviceYzbInfo adviceYzbInfo : adviceArg.AdviceYzbInfoList) {
                    isSucess = adviceYzbInfoManager.editAdviceYzbInfo(adviceYzbInfo) > 0;
                    if (!isSucess) {
                        return isSucess;
                    }
                }
                if (isSucess) {
                    for (AdviceBqyzInfo adviceBqyzInfo : adviceArg.AdviceBqyzInfoList) {
                        isSucess = adviceBqyzInfoManager.editAdviceBqyzInfo(adviceBqyzInfo) > 0;
                        if (!isSucess) {
                            return isSucess;
                        }
                    }
                    if (isSucess) {
                        for (PlanInfo planInfo : adviceArg.BQPlanInfoList) {
                            isSucess = planInfoManager.editBQPlanInfo(planInfo) > 0;
                            if (!isSucess) {
                                return isSucess;
                            }
                        }
                        if (!isSucess) {
                            return isSucess;
                        }
                    }
                }

            } else {
                isSucess = injectionInfoManager.editInjectionInfoKsAndJs(recordInfo) > 0;
                if (!isSucess) {
                    return isSucess;
                }
                isSucess = planInfoManager.editPlanInfoListKsAndJs(adviceArg.PlanInfoList.get(0)) > 0;
                if (!isSucess) {
                    return isSucess;
                }
                for (AdviceYzbInfo adviceYzbInfo : adviceArg.AdviceYzbInfoList) {
                    isSucess = adviceYzbInfoManager.editAdviceYzbInfo(adviceYzbInfo) > 0;
                    if (!isSucess) {
                        return isSucess;
                    }
                }
                if (isSucess) {
                    for (AdviceBqyzInfo adviceBqyzInfo : adviceArg.AdviceBqyzInfoList) {
                        isSucess = adviceBqyzInfoManager.editAdviceBqyzInfo(adviceBqyzInfo) > 0;
                        if (!isSucess) {
                            return isSucess;
                        }
                    }
                    if (isSucess) {
                        for (PlanInfo planInfo : adviceArg.BQPlanInfoList) {
                            isSucess = planInfoManager.editBQPlanInfo(planInfo) > 0;
                            if (!isSucess) {
                                return isSucess;
                            }
                        }
                    }
                }
            }
        }
        return isSucess;

    }
    private boolean InjectionDBOperate_CancelStart(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException {
        boolean isSucess = false;
        if (!parameterManager.getParameterMap().get(ea.JGID).InjectionUpdate) {
            isSucess = planInfoManager.editPlanInfoList(adviceArg.PlanInfoList.get(0)) > 0;
            if (!isSucess) {
                return isSucess;
            }
        } else {
            InjectionInfo recordInfo = (InjectionInfo) adviceArg.RecordInfo;
            if (recordInfo.ZSZT.equals("1")) {
                isSucess = injectionInfoManager.editInjectionInfoJs(recordInfo) > 0;
                if (!isSucess) {
                    return isSucess;
                }
                isSucess = planInfoManager.editPlanInfoListJs(adviceArg.PlanInfoList.get(0)) > 0;
                if (!isSucess) {
                    return isSucess;
                }
                for (AdviceYzbInfo adviceYzbInfo : adviceArg.AdviceYzbInfoList) {
                    isSucess = adviceYzbInfoManager.editAdviceYzbInfo(adviceYzbInfo) > 0;
                    if (!isSucess) {
                        return isSucess;
                    }
                }
                if (isSucess) {
                    for (AdviceBqyzInfo adviceBqyzInfo : adviceArg.AdviceBqyzInfoList) {
                        isSucess = adviceBqyzInfoManager.editAdviceBqyzInfo(adviceBqyzInfo) > 0;
                        if (!isSucess) {
                            return isSucess;
                        }
                    }
                    if (isSucess) {
                        for (PlanInfo planInfo : adviceArg.BQPlanInfoList) {
                            isSucess = planInfoManager.editBQPlanInfo(planInfo) > 0;
                            if (!isSucess) {
                                return isSucess;
                            }
                        }
                        if (!isSucess) {
                            return isSucess;
                        }
                    }
                }

            } else {
                isSucess = injectionInfoManager.editInjectionInfoJs(recordInfo) > 0;
                if (!isSucess) {
                    return isSucess;
                }
                isSucess = planInfoManager.editPlanInfoList(adviceArg.PlanInfoList.get(0)) > 0;
                if (!isSucess) {
                    return isSucess;
                }
                for (AdviceYzbInfo adviceYzbInfo : adviceArg.AdviceYzbInfoList) {
                    isSucess = adviceYzbInfoManager.editAdviceYzbInfo(adviceYzbInfo) > 0;
                    if (!isSucess) {
                        return isSucess;
                    }
                }
                if (isSucess) {
                    for (AdviceBqyzInfo adviceBqyzInfo : adviceArg.AdviceBqyzInfoList) {
                        isSucess = adviceBqyzInfoManager.editAdviceBqyzInfo(adviceBqyzInfo) > 0;
                        if (!isSucess) {
                            return isSucess;
                        }
                    }
                    if (isSucess) {
                        for (PlanInfo planInfo : adviceArg.BQPlanInfoList) {
                            isSucess = planInfoManager.editBQPlanInfo(planInfo) > 0;
                            if (!isSucess) {
                                return isSucess;
                            }
                        }
                    }
                }
            }
        }
        return isSucess;

    }
    private boolean OralMedicationDBOperate(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException {
        boolean isSucess = planInfoManager.editPlanInfoList(adviceArg.PlanInfoList.get(0)) > 0;
        if (!isSucess) {
            return isSucess;
        }
        if (parameterManager.getParameterMap().get(ea.JGID).OralUpdate) {
            //isSucess = oralInfoManager.editOralPackageInfo(((OralInfo) adviceArg.RecordInfo).Packages.get(0)) > 0;
            isSucess = oralInfoManager.editOralPackageInfo(((OralInfo) adviceArg.RecordInfo).Packages) > 0; //多条更新
            if (!isSucess) {
                return isSucess;
            }
        }
        for (AdviceYzbInfo adviceYzbInfo : adviceArg.AdviceYzbInfoList) {
            isSucess = adviceYzbInfoManager.editAdviceYzbInfo(adviceYzbInfo) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        if (isSucess) {
            for (AdviceBqyzInfo adviceBqyzInfo : adviceArg.AdviceBqyzInfoList) {
                isSucess = adviceBqyzInfoManager.editAdviceBqyzInfo(adviceBqyzInfo) > 0;
                if (!isSucess) {
                    return isSucess;
                }
            }
            if (isSucess) {
                for (PlanInfo planInfo : adviceArg.BQPlanInfoList) {
                    //此处为更新BQ_YZJH, 不一定能关联得上，故不做是否执行成功判断
                    planInfoManager.editBQPlanInfo(planInfo);
                    //isSucess = planInfoManager.editBQPlanInfo(planInfo) > 0;
                    //if (!isSucess) {
                    //    return isSucess;
                    //}
                }
            }
        }
        return isSucess;
    }

    private boolean OralMedicationDBOperate_CancelStart(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException {
        boolean isSucess = planInfoManager.editPlanInfoList(adviceArg.PlanInfoList.get(0)) > 0;
        if (!isSucess) {
            return isSucess;
        }
        if (parameterManager.getParameterMap().get(ea.JGID).OralUpdate) {
            //isSucess = oralInfoManager.editOralPackageInfo(((OralInfo) adviceArg.RecordInfo).Packages.get(0)) > 0;
            isSucess = oralInfoManager.editOralPackageInfo(((OralInfo) adviceArg.RecordInfo).Packages) > 0; //多条更新
            if (!isSucess) {
                return isSucess;
            }
        }
        for (AdviceYzbInfo adviceYzbInfo : adviceArg.AdviceYzbInfoList) {
            isSucess = adviceYzbInfoManager.editAdviceYzbInfo(adviceYzbInfo) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        if (isSucess) {
            for (AdviceBqyzInfo adviceBqyzInfo : adviceArg.AdviceBqyzInfoList) {
                isSucess = adviceBqyzInfoManager.editAdviceBqyzInfo(adviceBqyzInfo) > 0;
                if (!isSucess) {
                    return isSucess;
                }
            }
            if (isSucess) {
                for (PlanInfo planInfo : adviceArg.BQPlanInfoList) {
                    isSucess = planInfoManager.editBQPlanInfo(planInfo) > 0;
                    if (!isSucess) {
                        return isSucess;
                    }
                }
            }
        }
        return isSucess;
    }
    private boolean TransfuseDBOperateStart(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException, ParseException {
        boolean isSucess = parameterManager.getParameterMap().get(ea.JGID).TransfusionJoinPlanByTime
                ? planInfoManager.updatePlanInfoForSYDByTime(adviceArg.PlanInfoList.get(0)) > 0
                : planInfoManager.updatePlanInfoForSYD(adviceArg.PlanInfoList.get(0)) > 0;
        if (!isSucess) {
            return isSucess;
        }
        TransfusionInfo transfusionInfo = (TransfusionInfo) adviceArg.RecordInfo;
        if (parameterManager.getParameterMap().get(ea.JGID).TransfusionUpdate) {
            isSucess = transfuseInfoManager.editTransfusion(transfusionInfo) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        // 检验是否处于输液暂停状态
        if (stopCheck(transfusionInfo.SYDH, ea.JGID)) {
            // 结束输液暂停
            transfuseInfoManager.transfuseStopEnd(transfusionInfo.SYDH, DateConvert.getStandardString(transfusionInfo.JSSJ), transfusionInfo.JSGH, transfusionInfo.JGID);
        }

        for (AdviceYzbInfo adviceYzbInfo : adviceArg.AdviceYzbInfoList) {
            isSucess = adviceYzbInfoManager.editAdviceYzbInfo(adviceYzbInfo) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        if (isSucess) {
            for (AdviceBqyzInfo adviceBqyzInfo : adviceArg.AdviceBqyzInfoList) {
                isSucess = adviceBqyzInfoManager.editAdviceBqyzInfo(adviceBqyzInfo) > 0;
                if (!isSucess) {
                    return isSucess;
                }
            }
            if (isSucess) {
                for (PlanInfo planInfo : adviceArg.BQPlanInfoList) {
                    isSucess = planInfoManager.editBQPlanInfoForTrans(planInfo) > 0;
                    if (!isSucess) {
                        return isSucess;
                    }
                }
            }
        }

        return isSucess;
    }
    private boolean TransfuseDBOperateCancel(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException {
        //4Clear
        boolean isSucess = parameterManager.getParameterMap().get(ea.JGID).TransfusionJoinPlanByTime
                ? planInfoManager.updatePlanInfoForSYDByTime4Clear(adviceArg.PlanInfoList.get(0)) > 0
                : planInfoManager.updatePlanInfoForSYD4Clear(adviceArg.PlanInfoList.get(0)) > 0;
        if (!isSucess) {
            return isSucess;
        }
        TransfusionInfo transfusionInfo = (TransfusionInfo) adviceArg.RecordInfo;
        if (parameterManager.getParameterMap().get(ea.JGID).TransfusionUpdate) {
            //4Clear
            isSucess = transfuseInfoManager.editTransfusion4Clear(transfusionInfo) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        // 检验是否处于输液暂停状态
      /*  if (stopCheck(transfusionInfo.SYDH, ea.JGID)) {
            // 结束输液暂停
            transfuseInfoManager.transfuseStopEnd(transfusionInfo.SYDH, DateConvert.getStandardString(transfusionInfo.JSSJ), transfusionInfo.JSGH, transfusionInfo.JGID);
        }*/

        for (AdviceYzbInfo adviceYzbInfo : adviceArg.AdviceYzbInfoList) {
            isSucess = adviceYzbInfoManager.editAdviceYzbInfo(adviceYzbInfo) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        if (isSucess) {
            for (AdviceBqyzInfo adviceBqyzInfo : adviceArg.AdviceBqyzInfoList) {
                isSucess = adviceBqyzInfoManager.editAdviceBqyzInfo(adviceBqyzInfo) > 0;
                if (!isSucess) {
                    return isSucess;
                }
            }
            if (isSucess) {
                for (PlanInfo planInfo : adviceArg.BQPlanInfoList) {
                    isSucess = planInfoManager.editBQPlanInfoForTrans(planInfo) > 0;
                    if (!isSucess) {
                        return isSucess;
                    }
                }
            }
        }

        return isSucess;
    }


    private boolean TransfuseDBOperateEnd(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException, ParseException {
        boolean isSucess = parameterManager.getParameterMap().get(ea.JGID).TransfusionJoinPlanByTime
                ? planInfoManager.updatePlanInfoForSYDByTime(adviceArg.PlanInfoList.get(0)) > 0
                : planInfoManager.updatePlanInfoForSYD(adviceArg.PlanInfoList.get(0)) > 0;
        if (!isSucess) {
            return isSucess;
        }
        TransfusionInfo transfusionInfo = (TransfusionInfo) adviceArg.RecordInfo;
        if (parameterManager.getParameterMap().get(ea.JGID).TransfusionUpdate) {
            isSucess = transfuseInfoManager.editTransfusion(transfusionInfo) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        // 检验是否处于输液暂停状态
        if (stopCheck(transfusionInfo.SYDH, ea.JGID)) {
            // 结束输液暂停
            transfuseInfoManager.transfuseStopEnd(transfusionInfo.SYDH, DateConvert.getStandardString(transfusionInfo.JSSJ), transfusionInfo.JSGH, transfusionInfo.JGID);
        }

        for (AdviceYzbInfo adviceYzbInfo : adviceArg.AdviceYzbInfoList) {
            isSucess = adviceYzbInfoManager.editAdviceYzbInfo(adviceYzbInfo) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        if (isSucess) {
            for (AdviceBqyzInfo adviceBqyzInfo : adviceArg.AdviceBqyzInfoList) {
                isSucess = adviceBqyzInfoManager.editAdviceBqyzInfo(adviceBqyzInfo) > 0;
                if (!isSucess) {
                    return isSucess;
                }
            }
            if (isSucess) {
                for (PlanInfo planInfo : adviceArg.BQPlanInfoList) {
                    isSucess = planInfoManager.editBQPlanInfoForTrans(planInfo) > 0;
                    if (!isSucess) {
                        return isSucess;
                    }
                }
            }
        }

        return isSucess;
    }

    private boolean TransfuseDBOperateForceEnd(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException, ParseException {

        return TransfuseDBOperateEnd(adviceArg, ea);
    }

    private boolean TransfuseDBOperateStop(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException, ParseException {
        boolean isSucess = parameterManager.getParameterMap().get(ea.JGID).TransfusionJoinPlanByTime
                ? planInfoManager.updatePlanInfoForSYDByTime(adviceArg.PlanInfoList.get(0)) > 0
                : planInfoManager.updatePlanInfoForSYD(adviceArg.PlanInfoList.get(0)) > 0;
        if (!isSucess) {
            return isSucess;
        }
        TransfusionInfo transfusionInfo = (TransfusionInfo) adviceArg.RecordInfo;
        isSucess = transfuseInfoManager.updateTransfusionStatus(transfusionInfo.SYDH, transfusionInfo.SYZT, transfusionInfo.JGID) > 0;
        if (!isSucess) {
            return isSucess;
        }
        transfuseInfoManager.transfuseStopEnd(transfusionInfo.SYDH, DateConvert.getStandardString(transfusionInfo.JSSJ), transfusionInfo.JSGH, transfusionInfo.JGID);

        return isSucess;
    }

    private boolean TransfuseDBOperateContinue(AdviceArg adviceArg, ExecuteArg ea)
            throws SQLException, DataAccessException, ParseException { // 上瓶输液结束
        boolean isSucess = parameterManager.getParameterMap().get(ea.JGID).TransfusionJoinPlanByTime
                ? planInfoManager.updatePlanInfoForSYDByTime(adviceArg.PlanInfoList.get(0)) > 0
                : planInfoManager.updatePlanInfoForSYD(adviceArg.PlanInfoList.get(0)) > 0;
        if (!isSucess) {
            return isSucess;
        }
        if (adviceArg.PlanInfoList.size() == 2) { // 本瓶输液开始
            isSucess = parameterManager.getParameterMap().get(ea.JGID).TransfusionJoinPlanByTime
                    ? planInfoManager.updatePlanInfoForSYDByTime(adviceArg.PlanInfoList.get(1)) > 0
                    : planInfoManager.updatePlanInfoForSYD(adviceArg.PlanInfoList.get(1)) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        TransfusionInfo transfusionInfo_Last = null;//上一瓶
        TransfusionInfo transfusionInfo_This = null;//当前瓶
        int count = adviceArg.TransfusionInfoList.size();
        if (count == 1) {
            transfusionInfo_This = adviceArg.TransfusionInfoList.get(0);
        } else if (count == 2) {
            transfusionInfo_Last = adviceArg.TransfusionInfoList.get(0);
            transfusionInfo_This = adviceArg.TransfusionInfoList.get(1);
        }
        if (parameterManager.getParameterMap().get(ea.JGID).TransfusionUpdate) {
            if (transfusionInfo_Last != null) {
                isSucess = transfuseInfoManager.editTransfusion(transfusionInfo_Last) > 0;
                if (!isSucess) {
                    return isSucess;
                }
            }
            if (transfusionInfo_This != null) {
                isSucess = transfuseInfoManager.editTransfusion(transfusionInfo_This) > 0;
                if (!isSucess) {
                    return isSucess;
                }
            }
        }
        // 检验是否处于输液暂停状态
        if (transfusionInfo_This != null && stopCheck(transfusionInfo_This.SYDH, ea.JGID)) {
            // 结束输液暂停
            transfuseInfoManager.transfuseStopEnd(transfusionInfo_This.SYDH, DateConvert.getStandardString(transfusionInfo_This.JSSJ), transfusionInfo_This.JSGH, transfusionInfo_This.JGID);
        }

        for (AdviceYzbInfo adviceYzbInfo : adviceArg.AdviceYzbInfoList) {
            isSucess = adviceYzbInfoManager.editAdviceYzbInfo(adviceYzbInfo) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }
        if (isSucess) {
            for (AdviceBqyzInfo adviceBqyzInfo : adviceArg.AdviceBqyzInfoList) {
                isSucess = adviceBqyzInfoManager.editAdviceBqyzInfo(adviceBqyzInfo) > 0;
                if (!isSucess) {
                    return isSucess;
                }
            }
            if (isSucess) {
                for (PlanInfo planInfo : adviceArg.BQPlanInfoList) {
                    isSucess = planInfoManager.editBQPlanInfoForTrans(planInfo) > 0;
                    if (!isSucess) {
                        return isSucess;
                    }
                }
            }
        }

        return isSucess;
    }

    private void ExecuteResult(ExecuteArg ea) {
        for (int i = 0; i < ea.PlanInfoList.size(); i++) {
            PlanInfo planInfo = ea.PlanInfoList.get(i);
            boolean isSelect = false;
            for (ExecuteResult info : ea.ExecutResult) {
                if (info.JHH.equals(planInfo.JHH)) {
                    isSelect = true;
                    break;
                }
            }
            if (!isSelect && !planInfo.ZXZT.equals("1")) {
                ea.ExecutResult.add(new ExecuteResult(ea, planInfo.JHH, planInfo.JHSJ, planInfo.SJMC, planInfo.YZMC, planInfo.YZXH, ea.YHID,
                        messageManager.Create("0"), "0", planInfo.ZXZT,planInfo.JHSJ,planInfo.YZZH));
            }
        }
    }

    /**
     * 输液暂停判断
     *
     * @param sydh
     * @param jgid
     * @return
     */
    private Boolean stopCheck(String sydh, String jgid) throws SQLException, DataAccessException {
        TransfusionInfo transfusionInfo = transfuseInfoManager.transfuseStopCheck(sydh, jgid);
        String syzt = transfusionInfo.SYZT;
        return syzt != null && "4".equals(syzt);
    }

    /**
     * 更新计划成功处理
     *
     * @param erList
     * @param yhid
     * @param end
     * @param success
     * @param now
     */
    private void updatePlanSuccess(List<ExecuteResult> erList, String yhid, boolean end, boolean success, String now) {
        for (ExecuteResult er : erList) {
            if (success) {
                if (end) {
                    er.ZXGH = yhid;
                    er.ZXSJ = now;
                }
                er.ResultType = "0";
                er.Msg = messageManager.Create("0");
            } else {
                er.ResultType = "17";
                er.Msg = messageManager.Create("17");
            }
        }
    }

}
