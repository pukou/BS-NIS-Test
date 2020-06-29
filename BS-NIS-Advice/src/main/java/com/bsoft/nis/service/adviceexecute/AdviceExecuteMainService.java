package com.bsoft.nis.service.adviceexecute;

import com.bsoft.nis.adviceexecute.FlowControl.IFlowControl;
import com.bsoft.nis.adviceexecute.ModelManager.*;
import com.bsoft.nis.adviceexecute.Operates.FinalOperate;
import com.bsoft.nis.adviceexecute.Operates.InjectionDBOperate;
import com.bsoft.nis.adviceexecute.Operates.OralMedicationDBOperate;
import com.bsoft.nis.adviceexecute.Operates.TransfuseDBOperate;
import com.bsoft.nis.adviceexecute.Preprocessing.FinalPreprocessing;
import com.bsoft.nis.adviceexecute.Preprocessing.InjectionPreprocessing;
import com.bsoft.nis.adviceexecute.Preprocessing.OralMedicationPreprocessing;
import com.bsoft.nis.adviceexecute.Preprocessing.TransfusePreprocessing;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.common.service.UserConfigService;
import com.bsoft.nis.controller.adviceexecute.NumberCharParser;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.config.ConfigHandler;
import com.bsoft.nis.core.config.bean.flow.Flow;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.*;
import com.bsoft.nis.domain.adviceexecute.ResponseBody.*;
import com.bsoft.nis.domain.adviceexecute.ResponseModel.AdvicePlanData;
import com.bsoft.nis.domain.adviceqyery.TransfusionPatrolRecord;
import com.bsoft.nis.domain.adviceqyery.db.TransfusionPatrolRecordVo;
import com.bsoft.nis.domain.synchron.InArgument;
import com.bsoft.nis.domain.synchron.Project;
import com.bsoft.nis.domain.synchron.SelectResult;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.adviceSync.AdviceSyncMainService;
import com.bsoft.nis.service.adviceexecute.support.AdviceExecuteServiceSup;
import com.bsoft.nis.util.date.DateUtil;
import com.bsoft.nis.util.string.StringTool;
import ctd.net.rpc.Client;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description: 医嘱执行(执行)主服务
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 16:58
 * Version:
 */
@Service
public class AdviceExecuteMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(AdviceExecuteMainService.class);

    List<IFlowControl> flowControlList; // 流控制器

    @Autowired
    AdviceExecuteServiceSup service;
    @Autowired
    DictCachedHandler handler; // 缓存处理器
    @Autowired
    FinalPreprocessing finalPreprocessing; // 预处理
    @Autowired
    OralMedicationPreprocessing oralMedicationPreprocessing; // 口服药预处理
    @Autowired
    TransfusePreprocessing transfusePreprocessing; // 输液预处理
    @Autowired
    InjectionPreprocessing injectionPreprocessing; // 注射预处理
    @Autowired
    FinalOperate finalOperate; // 执行器
    @Autowired
    TransfuseDBOperate transfuseDBOperate; // 输液执行器
    @Autowired
    OralMedicationDBOperate oralMedicationDBOperate; //口服执行器
    @Autowired
    OralInfoManager oralInfoManager;//口服单管理器
    @Autowired
    InjectionInfoManager injectionInfoManager;//注射单管理器
    @Autowired
    InjectionDBOperate injectionDBOperate; //注射执行器
    @Autowired
    DateTimeService timeService; // 日期时间服务
    @Autowired
    PlanInfoManager planInfoManager;//医嘱计划管理器
    @Autowired
    TransfuseInfoManager transfuseInfoManager; // 输液管理器
    @Autowired
    IdentityService identityService; // 种子表服务
    @Autowired
    ParameterManager parameterManager;//用户参数管理器
    @Autowired
    UserConfigService userConfigService;//用户参数服务
    @Autowired
    SystemParamService systemParamService;//用户参数服务
    @Autowired
    AdviceSyncMainService adviceSyncMainService;//同步医嘱计划服务

    /**
     * 获取医嘱计划列表
     *
     * @param zyh   住院号
     * @param today 今天
     * @param gslx  归属类型
     * @param jgid  机构id
     * @return
     */
    public BizResponse<AdvicePlanData> GetPlanList(String zyh, String today, String gslx, String jgid) {
        BizResponse<AdvicePlanData> response = new BizResponse<>();
        String tomorrow;
        try {
            tomorrow = DateUtil.dateoffDays(today, "1");
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[获取医嘱计划列表失败]传入参数有误!";
            return response;
        }
        try {
            AdvicePlanData model = new AdvicePlanData();

            String operType = parameterManager.getParameterMap().get(jgid).Integrated;

            /**
             * 56010003 修复PDA医嘱执行界面不调用拆分服务
             * 字符串比较使用不当
             * 参数意义不清
             */

            //0:自己拆分 1:同步计划 2:合并计划
            if (operType.equals("1")) {//同步计划服务
                BizResponse<String> tempResponse = adviceSyncMainService.syncPlanByZyh(zyh, today, today, jgid);
                if (!tempResponse.isSuccess) {
                    response.isSuccess = false;
                    response.message = "[获取医嘱计划列表失败]服务内部错误!";
                    return response;
                }
            } else if (operType.equals("0")) {//自己拆分服务
                BizResponse<String> tempResponse = Client.rpcInvoke("nis-core.adviceSplitRpcServerProvider", "excuteSplitForOnePatient", zyh, jgid, today, tomorrow);
                if (!tempResponse.isSuccess) {
                    response.isSuccess = false;
                    response.message = "[获取医嘱计划列表失败]服务内部错误!";
                    return response;
                }
            }
            //******************************************************************************
            List<PlanInfo> planInfoList = planInfoManager.getPlanInfoList(zyh, today, tomorrow, gslx, jgid);
            for (PlanInfo planInfo : planInfoList) {
                planInfo.JSGH = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, planInfo.JSGH);
                planInfo.KSGH = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, planInfo.KSGH);
                planInfo.YPYFMC = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YPYF, jgid, planInfo.YPYF);
                if (!StringUtils.isBlank(planInfo.YCJL)) {
                    String ycjl = planInfo.YCJL;
                    //处理 保留 2 位小数
                    ycjl = StringTool.parseDecimalStr2Fixed(ycjl);
                    planInfo.JLXX = ycjl + planInfo.JLDW;
                }
                if (!StringUtils.isBlank(planInfo.YCSL)) {
                    String ycsl = planInfo.YCSL;
                    //处理 保留 2 位小数
                    ycsl = StringTool.parseDecimalStr2Fixed(ycsl);
                    planInfo.SLXX = ycsl + planInfo.SLDW;
                }
            }
            List<PhraseModel> phraseModelList = planInfoManager.getRefuseReasonList(jgid);
            model.PhraseModelList = phraseModelList;
            model.PlanInfoList = planInfoList;
            //
            response.data = model;
            response.isSuccess = true;

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[获取医嘱计划列表失败]数据库查询错误!";
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[获取医嘱计划列表失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 执行计划（手动）
     *
     * @param requestBodyInfo
     * @return
     */
    public BizResponse<ResponseBodyInfo> HandExecutNew(RequestBodyInfo requestBodyInfo) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            List<ExecuteResult> executeResultList = new ArrayList<>();
            //提交到预处理器
            List<ExecuteArg> executeArgArrayList = finalPreprocessing.Preprocessing(requestBodyInfo.ZYH, requestBodyInfo.YHID,
                    requestBodyInfo.PlanArgInfoList, requestBodyInfo.JGID);
            ResponseBodyInfo responseBodyInfo;
            if (executeArgArrayList != null) {
                for (ExecuteArg ea : executeArgArrayList) {
                    if (ea.ExecutResult.size() > 0) {
                        responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, ea.JGID);
                        response.isSuccess = true;
                        response.data = responseBodyInfo;
                        return response;
                    } else {
                        if (ea.GSLX.equals("4")) {
                            TransfuseArgs args = new TransfuseArgs();
                            args.IsParallel = requestBodyInfo.SYBX;
                            ea.Args = args;
                            //add 2018-5-17 16:00:52 协和
                            if (ea.RecordInfo != null && ea.RecordInfo instanceof TransfusionInfo) {
                                TransfusionInfo transfusionInfo = (TransfusionInfo) ea.RecordInfo;
                                if ("4".equals(transfusionInfo.SYZT)) {
                                    //针对 TransfuseStop 暂停的输液   暂停——>继续   特殊处理
                                    //暂停——>结束  的情况 不在这里 客户端直接调用 end 方法，
//                                    requestBodyInfo.QRDH = requestBodyInfo.transfuse_sp_sydh;
                                    requestBodyInfo.QRDH = transfusionInfo.SYDH;
                                    requestBodyInfo.transfuse_sp_sydh = "[ztsydh]";
                                    return TransfuseContinue(requestBodyInfo);
                                }
                            }
                            //add 2018-5-17 16:00:52 协和
                        }
                        //特殊处理：给ea.FlowRecordInfo赋值
                        finalPreprocessing.getRealExecuteArg(ea);
                        if (ea.FlowRecordInfo != null) {
                            responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                            response.isSuccess = true;
                            response.data = responseBodyInfo;
                            return response;
                        }
                        //走流程控制
                        ExecuteArg newEa = flowControl(ea, requestBodyInfo.JYSJ);
                        if (newEa != null && newEa.ExecutResult.size() > 0) {
                            if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
                                // 提交到执行器执行-只是为了更新KSHDSJ，KSHDGH
                                finalOperate.DoubleCheckControlOperate(newEa);
                            }
                            responseBodyInfo = getResponseBodyInfo(newEa.ExecutResult, ea.JGID);
                            response.isSuccess = true;
                            response.data = responseBodyInfo;
                            return response;
                        } else {
                                /*
        升级编号【56010053】============================================= start
        多瓶超过2瓶转接瓶后提示选择接哪瓶的问题
        ================= Classichu 2017/11/14 16:25

        */
                            //提交到执行器执行
                            finalOperate.Operate(ea, requestBodyInfo.transfuse_sp_sydh);
                            /* =============================================================== end */
                            executeResultList.addAll(ea.ExecutResult);
                        }
                    }
                }
            }
            if (executeResultList.size() > 0) {
                //
                responseBodyInfo = getResponseBodyInfo(executeResultList, requestBodyInfo.JGID);
                if (parameterManager.getParameterMap().get(requestBodyInfo.JGID).SyncNuserRecord) {
                    //add
                    if (executeArgArrayList != null) {
                        Response<InArgument> inArgumentResponse = FJXH_GetSyncData(executeArgArrayList);
                        if (inArgumentResponse.ReType == 0) {
                            //获取同步数据成功 赋值 给前台
                            responseBodyInfo.inArgument = inArgumentResponse.Data;
                        }
                    }
                }
                response.isSuccess = true;
                response.data = responseBodyInfo;
            } else {
                response.isSuccess = false;
                response.message = "[手动执行计划失败]服务内部错误!";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[手动执行计划失败]服务内部错误!";
        }
        return response;
    }

    @Deprecated
    public BizResponse<ResponseBodyInfo> HandExecut(RequestBodyInfo requestBodyInfo) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            List<ExecuteResult> executeResultList = new ArrayList<>();
            //提交到预处理器
            List<ExecuteArg> executeArgArrayList = finalPreprocessing.Preprocessing(requestBodyInfo.ZYH, requestBodyInfo.YHID,
                    requestBodyInfo.PlanArgInfoList, requestBodyInfo.JGID);
            ResponseBodyInfo responseBodyInfo;
            if (executeArgArrayList != null) {
                for (ExecuteArg ea : executeArgArrayList) {
                    if (ea.ExecutResult.size() > 0) {
                        responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, ea.JGID);
                        response.isSuccess = true;
                        response.data = responseBodyInfo;
                        return response;
                    } else {
                        if (ea.GSLX.equals("4")) {
                            TransfuseArgs args = new TransfuseArgs();
                            args.IsParallel = requestBodyInfo.SYBX;
                            ea.Args = args;
                        }
                        //特殊处理：给ea.FlowRecordInfo赋值
                        finalPreprocessing.getRealExecuteArg(ea);
                        if (ea.FlowRecordInfo != null) {
                            responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                            response.isSuccess = true;
                            response.data = responseBodyInfo;
                            return response;
                        }
                        //走流程控制
                        ExecuteArg newEa = flowControl(ea, requestBodyInfo.JYSJ);
                        if (newEa != null && newEa.ExecutResult.size() > 0) {
                            if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
                                // 提交到执行器执行-只是为了更新KSHDSJ，KSHDGH
                                finalOperate.DoubleCheckControlOperate(newEa);
                            }
                            responseBodyInfo = getResponseBodyInfo(newEa.ExecutResult, ea.JGID);
                            response.isSuccess = true;
                            response.data = responseBodyInfo;
                            return response;
                        } else {
                                /*
        升级编号【56010053】============================================= start
        多瓶超过2瓶转接瓶后提示选择接哪瓶的问题
        ================= Classichu 2017/11/14 16:25

        */
                            //提交到执行器执行
                            finalOperate.Operate(ea, requestBodyInfo.transfuse_sp_sydh);
                            /* =============================================================== end */
                            executeResultList.addAll(ea.ExecutResult);
                        }
                    }
                }
            }
            if (executeResultList.size() > 0) {
                responseBodyInfo = getResponseBodyInfo(executeResultList, requestBodyInfo.JGID);
                if (parameterManager.getParameterMap().get(requestBodyInfo.JGID).SyncNuserRecord) {
                    Response<SelectResult> syncResponse = buildSyncData(executeArgArrayList);
                    if (syncResponse.ReType == 2) {
                        responseBodyInfo.IsSync = true;
                        responseBodyInfo.SyncData = syncResponse.Data;
                        //todo
                        response.message = "用户选择！";
                    } else if (syncResponse.ReType == 1) {
                        //同步失败
                        response.message = "执行成功，医嘱已写入护理记录单！";
                    } else if (syncResponse.ReType == 1111) {
                        //特殊 没有 24小时出入量 直接不同步
                        response.message = "执行成功！";
                    } else if (syncResponse.ReType == 0) {
                        //同步失败
                        response.message = "执行成功，写入护理记录单失败！";
                    }
                }
                response.isSuccess = true;
                response.data = responseBodyInfo;
            } else {
                response.isSuccess = false;
                response.message = "[手动执行计划失败]服务内部错误!";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[手动执行计划失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 输液暂停
     *
     * @param data
     * @return
     */
    public BizResponse<ResponseBodyInfo> TransfuseStop(RequestBodyInfo data) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            List<String> jhhList = new ArrayList<>();
            List<ExecuteResult> tempExecuteResult = new ArrayList<>();
            // 当前数据库时间
            String now = timeService.now(DataSource.PORTAL);
            // 提交预处理器 TODO 待确认
            ExecuteArg ea = transfusePreprocessing.Preprocessing(data.ZYH, data.YHID, data.QRDH, data.QRDH, data.JGID);
            if (ea != null && "4".equals(ea.GSLX)) { // 输液类
                if (!ea.ExecutResult.isEmpty()) {
                    responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                //特殊处理：给ea.FlowRecordInfo赋值
                finalPreprocessing.getRealExecuteArg(ea);
                if (ea.FlowRecordInfo != null) {
                    responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 走流程控制
                ExecuteArg newEa = flowControl(ea);
                if (newEa != null && newEa.ExecutResult.size() > 0) {
                    responseBodyInfo = getResponseBodyInfo(newEa.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 输液暂停处理
                transfuseDBOperate.Stop(ea, now);
                for (ExecuteResult er : ea.ExecutResult) {
                    jhhList.add(er.JHH);
                    tempExecuteResult.add(er);
                }
            }
            finalOperate.FinalOperate(ea);
            // 整理返回值
            // TODO 返回值待确认
            List<SYZTModel> sYZTModelList = new ArrayList<>();
           /* for (String jhh : jhhList) {
                SYZTModel model = new SYZTModel();
                model.QRDH = jhh;
                SYZTModelList.add(model);
            }*/
            //协和
            for (ExecuteResult executeResult : tempExecuteResult) {
                SYZTModel model = new SYZTModel();
                model.QRDH = executeResult.JHH;
                model.YZZH = executeResult.YZZH;
                model.JHSJ = executeResult.JHSJ;
                sYZTModelList.add(model);
            }
            if (sYZTModelList.isEmpty()) {
                for (PlanInfo info : ea.PlanInfoList) {
                    SYZTModel syztModel = new SYZTModel();
                    syztModel.JHSJ = info.JHSJ;
                    syztModel.YZZH = info.YZZH;
                    sYZTModelList.add(syztModel);
                }
            }
            responseBodyInfo.TableName = "SYZT";
            responseBodyInfo.SYZTModelList = sYZTModelList;
            response.data = responseBodyInfo;
            response.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[输液暂停失败]服务内部错误";
        }
        return response;
    }

    public BizResponse<ResponseBodyInfo> TransfuseEnd(RequestBodyInfo data) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            List<String> jhhList = new ArrayList<>();
            List<ExecuteResult> tempExecuteResult = new ArrayList<>();
            // 当前数据库时间
            String now = timeService.now(DataSource.PORTAL);
            // 提交预处理器 TODO 待确认
            ExecuteArg ea = transfusePreprocessing.Preprocessing(data.ZYH, data.YHID, data.QRDH, data.QRDH, data.JGID);
            if (ea != null && "4".equals(ea.GSLX)) { // 输液类
                if (!ea.ExecutResult.isEmpty()) {
                    responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                //特殊处理：给ea.FlowRecordInfo赋值
                finalPreprocessing.getRealExecuteArg(ea);
                if (ea.FlowRecordInfo != null) {
                    responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 走流程控制
                ExecuteArg newEa = flowControl(ea);
                if (newEa != null && newEa.ExecutResult.size() > 0) {
                    responseBodyInfo = getResponseBodyInfo(newEa.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 输液直接处理
                transfuseDBOperate.ForceEnd(ea, now);
                for (ExecuteResult er : ea.ExecutResult) {
                    jhhList.add(er.JHH);
                    tempExecuteResult.add(er);
                }
            }
            finalOperate.FinalOperate(ea);
            // 整理返回值
            // TODO 返回值待确认
            List<SYZTModel> sYZTModelList = new ArrayList<>();
           /* for (String jhh : jhhList) {
                SYZTModel model = new SYZTModel();
                model.QRDH = jhh;
                SYZTModelList.add(model);
            }*/
            //协和
            for (ExecuteResult executeResult : tempExecuteResult) {
                SYZTModel model = new SYZTModel();
                model.QRDH = executeResult.JHH;
                model.YZZH = executeResult.YZZH;
                model.JHSJ = executeResult.JHSJ;
                sYZTModelList.add(model);
            }
            if (sYZTModelList.isEmpty()) {
                for (PlanInfo info : ea.PlanInfoList) {
                    SYZTModel syztModel = new SYZTModel();
                    syztModel.JHSJ = info.JHSJ;
                    syztModel.YZZH = info.YZZH;
                    sYZTModelList.add(syztModel);
                }
            }
            responseBodyInfo.TableName = "SYJS";
            responseBodyInfo.SYZTModelList = sYZTModelList;
            response.data = responseBodyInfo;
            response.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[输液结束失败]服务内部错误";
        }
        return response;
    }

    /**
     * 继续输液
     *
     * @param data
     * @return
     */
    public BizResponse<ResponseBodyInfo> TransfuseContinue(RequestBodyInfo data) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            String now = timeService.now(DataSource.PORTAL);
            // 提交预处理器
            ExecuteArg ea = transfusePreprocessing.Preprocessing(data.ZYH, data.YHID, data.QRDH, data.QRDH, data.JGID);
            if (data.QZJS) { // 强制结束
                keepOrRoutingDateSource(DataSource.MOB);
                // 数据准备
                transfuseDBOperate.ForceEnd(ea, now);
                // 提交到执行器执行
                finalOperate.FinalOperate(ea);
                List<ExecuteResult> erList = ea.ExecutResult;
                if (!erList.isEmpty()) {
                    responseBodyInfo = getResponseBodyInfo(erList, data.JGID);
                }
                response.isSuccess = true;
                response.data = responseBodyInfo;
                return response;
            } else {
                if (!ea.ExecutResult.isEmpty()) {
                    responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, data.JGID);
                    response.data = responseBodyInfo;
                    response.isSuccess = true;
                    return response;
                }
                TransfuseArgs transfuseArgs = new TransfuseArgs();
                transfuseArgs.IsParallel = data.SYBX;
                transfuseArgs.Ztpd = false;
                transfuseArgs.Qzjs = data.QZJS;
                ea.Args = transfuseArgs;

                //特殊处理：给ea.FlowRecordInfo赋值
                finalPreprocessing.getRealExecuteArg(ea);
                if (ea.FlowRecordInfo != null) {
                    responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 走流程控制
                ExecuteArg newEa = flowControl(ea);
                if (newEa != null && newEa.FlowRecordInfo != null) {
                    responseBodyInfo = getResponseBodyInfo(newEa.FlowRecordInfo);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                } else {
                    // 提交到执行器执行
                    finalOperate.Operate(ea, data.transfuse_sp_sydh);
                    List<ExecuteResult> erList = ea.ExecutResult;
                    if (!erList.isEmpty()) {
                        responseBodyInfo = getResponseBodyInfo(erList, data.JGID);
                    }
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[继续输液失败]数据库执行错误!";
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[继续输液失败]数据转化错误!";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[继续输液失败]服务内部错误";
        }
        return response;
    }

    /**
     * 执行计划（扫描）
     *
     * @param requestBodyInfo
     * @return
     */
    @Deprecated
    public BizResponse<ResponseBodyInfo> ScanExecut(RequestBodyInfo requestBodyInfo) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            List<ExecuteResult> executeResultList = new ArrayList<>();
            //提交到预处理器
            PreprocessingScannResult preprocessingScannResult = finalPreprocessing.ScanPreprocessing(requestBodyInfo.ZYH, requestBodyInfo.YHID,
                    requestBodyInfo.TMNR, requestBodyInfo.TMQZ, requestBodyInfo.JGID);
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            if (preprocessingScannResult != null) {
                if (!preprocessingScannResult.Success) {
                    responseBodyInfo.TableName = "ERROR";
                    responseBodyInfo.Message = preprocessingScannResult.Message;
                    response.data = responseBodyInfo;
                    response.isSuccess = true;
                    return response;
                } else {
                    for (ExecuteArg ea : preprocessingScannResult.ExcutArg) {
                        if (ea.ExecutResult.size() > 0) {
                            responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, ea.JGID);
                            response.isSuccess = true;
                            response.data = responseBodyInfo;
                            return response;
                        }
                        if (ea.GSLX.equals("4")) {
                            TransfuseArgs args = new TransfuseArgs();
                            args.IsParallel = requestBodyInfo.SYBX;
                            ea.Args = args;
                        }
                        //特殊处理：给ea.FlowRecordInfo赋值
                        finalPreprocessing.getRealExecuteArg(ea);
                        if (ea.FlowRecordInfo != null) {
                            responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                            response.isSuccess = true;
                            response.data = responseBodyInfo;
                            return response;
                        }
                        //走流程控制
                        ExecuteArg newEa = flowControl(ea, requestBodyInfo.JYSJ);
                        if (newEa != null && newEa.ExecutResult.size() > 0) {
                            if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
                                // 提交到执行器执行-只是为了更新KSHDSJ，KSHDGH
                                finalOperate.DoubleCheckControlOperate(newEa);
                            }
                            responseBodyInfo = getResponseBodyInfo(newEa.ExecutResult, ea.JGID);
                            response.isSuccess = true;
                            response.data = responseBodyInfo;
                            return response;
                        } else {
                                /*
        升级编号【56010053】============================================= start
        多瓶超过2瓶转接瓶后提示选择接哪瓶的问题
        ================= Classichu 2017/11/14 16:25

        */
                            //提交到执行器执行
                            finalOperate.Operate(ea, requestBodyInfo.transfuse_sp_sydh);
                            /* =============================================================== end */
                            executeResultList.addAll(ea.ExecutResult);
                        }

                    }
                }
            }
            if (executeResultList.size() > 0) {
                responseBodyInfo = getResponseBodyInfo(executeResultList, requestBodyInfo.JGID);
                if (preprocessingScannResult != null && parameterManager.getParameterMap().get(requestBodyInfo.JGID).SyncNuserRecord) {
                    Response<SelectResult> syncResponse = buildSyncData(preprocessingScannResult.ExcutArg);
                    if (syncResponse.ReType == 2) {
                        responseBodyInfo.IsSync = true;
                        responseBodyInfo.SyncData = syncResponse.Data;
                        //todo
                        response.message = "用户选择！";
                    } else if (syncResponse.ReType == 1) {
                        //同步失败
                        response.message = "执行成功，医嘱已写入护理记录单！";
                    } else if (syncResponse.ReType == 1111) {
                        //特殊 没有 24小时出入量 直接不同步
                        response.message = "执行成功！";
                    } else if (syncResponse.ReType == 0) {
                        //同步失败
                        response.message = "执行成功，写入护理记录单失败！";
                    }
                }
                response.isSuccess = true;
                response.data = responseBodyInfo;
            } else {
                response.isSuccess = false;
                response.message = "[扫描执行计划失败]服务内部错误!";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[扫描执行计划失败]服务内部错误!";
        }
        return response;
    }

    public BizResponse<ResponseBodyInfo> ScanExecutNew(RequestBodyInfo requestBodyInfo) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            List<ExecuteResult> executeResultList = new ArrayList<>();
            //提交到预处理器
            PreprocessingScannResult preprocessingScannResult = finalPreprocessing.ScanPreprocessing(requestBodyInfo.ZYH, requestBodyInfo.YHID,
                    requestBodyInfo.TMNR, requestBodyInfo.TMQZ, requestBodyInfo.JGID);
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            if (preprocessingScannResult != null) {
                if (!preprocessingScannResult.Success) {
                    responseBodyInfo.TableName = "ERROR";
                    responseBodyInfo.Message = preprocessingScannResult.Message;
                    response.data = responseBodyInfo;
                    response.isSuccess = true;
                    return response;
                } else {
                    for (ExecuteArg ea : preprocessingScannResult.ExcutArg) {
                        if (ea.ExecutResult.size() > 0) {
                            responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, ea.JGID);
                            response.isSuccess = true;
                            response.data = responseBodyInfo;
                            return response;
                        }
                        if (ea.GSLX.equals("4")) {
                            TransfuseArgs args = new TransfuseArgs();
                            args.IsParallel = requestBodyInfo.SYBX;
                            ea.Args = args;
                            //add 2018-5-17 16:00:52 协和
                            if (ea.RecordInfo != null && ea.RecordInfo instanceof TransfusionInfo) {
                                TransfusionInfo transfusionInfo = (TransfusionInfo) ea.RecordInfo;
                                if ("4".equals(transfusionInfo.SYZT)) {
                                    if (!TextUtils.isEmpty(requestBodyInfo.core)) {
                                        if ("con".equals(requestBodyInfo.core)) {
                                            //暂停——>继续   特殊处理
                                            requestBodyInfo.QRDH = transfusionInfo.SYDH;
                                            requestBodyInfo.transfuse_sp_sydh = "[ztsydh]";
                                            requestBodyInfo.core = null;
                                            return TransfuseContinue(requestBodyInfo);
                                        }
                                        if ("end".equals(requestBodyInfo.core)) {
                                            //暂停——>结束   特殊处理
                                            requestBodyInfo.QRDH = transfusionInfo.SYDH;
//                                            requestBodyInfo.QRDH = requestBodyInfo.transfuse_sp_sydh;
                                            requestBodyInfo.transfuse_sp_sydh = "[jssydh]";
                                            requestBodyInfo.core = null;
                                            return TransfuseEnd(requestBodyInfo);
                                        }

                                    }

                                    responseBodyInfo.TableName = "CORE";
                                    responseBodyInfo.Message = requestBodyInfo.TMNR + "," + requestBodyInfo.TMQZ + "," + requestBodyInfo.JYSJ;
                                    response.data = responseBodyInfo;
                                    response.isSuccess = true;
                                    return response;
                                }
                            }
                            //add 2018-5-17 16:00:52 协和
                        }
                        //特殊处理：给ea.FlowRecordInfo赋值
                        finalPreprocessing.getRealExecuteArg(ea);
                        if (ea.FlowRecordInfo != null) {
                            responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                            response.isSuccess = true;
                            response.data = responseBodyInfo;
                            return response;
                        }
                        //走流程控制
                        ExecuteArg newEa = flowControl(ea, requestBodyInfo.JYSJ);
                        if (newEa != null && newEa.ExecutResult.size() > 0) {
                            if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
                                // 提交到执行器执行-只是为了更新KSHDSJ，KSHDGH
                                finalOperate.DoubleCheckControlOperate(newEa);
                            }
                            responseBodyInfo = getResponseBodyInfo(newEa.ExecutResult, ea.JGID);
                            response.isSuccess = true;
                            response.data = responseBodyInfo;
                            return response;
                        } else {
                                /*
        升级编号【56010053】============================================= start
        多瓶超过2瓶转接瓶后提示选择接哪瓶的问题
        ================= Classichu 2017/11/14 16:25

        */
                            //提交到执行器执行
                            finalOperate.Operate(ea, requestBodyInfo.transfuse_sp_sydh);
                            /* =============================================================== end */
                            executeResultList.addAll(ea.ExecutResult);
                        }

                    }
                }
            }
            if (executeResultList.size() > 0) {
                responseBodyInfo = getResponseBodyInfo(executeResultList, requestBodyInfo.JGID);
                if (parameterManager.getParameterMap().get(requestBodyInfo.JGID).SyncNuserRecord) {
                    //add
                    if (preprocessingScannResult != null) {
                        Response<InArgument> inArgumentResponse = FJXH_GetSyncData(preprocessingScannResult.ExcutArg);
                        if (inArgumentResponse.ReType == 0) {
                            //获取同步数据成功 赋值 给前台
                            responseBodyInfo.inArgument = inArgumentResponse.Data;
                        }
                    }
                }
                response.isSuccess = true;
                response.data = responseBodyInfo;
            } else {
                response.isSuccess = false;
                response.message = "[扫描执行计划失败]服务内部错误!";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[扫描执行计划失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 口服药执行
     *
     * @param requestBodyInfo
     * @return
     */
    public BizResponse<ResponseBodyInfo> OralMedicationExecut(RequestBodyInfo requestBodyInfo) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            List<ExecuteResult> executeResultList = new ArrayList<>();
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            ExecuteArg ea = finalPreprocessing.OralPreprocessing(requestBodyInfo.ZYH, requestBodyInfo.YHID, requestBodyInfo.QRDH, requestBodyInfo.JGID);
            if (ea.ExecutResult.size() > 0) {
                responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, ea.JGID);
                response.isSuccess = true;
                response.data = responseBodyInfo;
                return response;
            }
            //特殊处理：给ea.FlowRecordInfo赋值
            finalPreprocessing.getRealExecuteArg(ea);
            if (ea.FlowRecordInfo != null) {
                responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                response.isSuccess = true;
                response.data = responseBodyInfo;
                return response;
            }
            //走流程控制
            ExecuteArg newEa = flowControl(ea, requestBodyInfo.JYSJ);
            if (newEa != null && newEa.ExecutResult.size() > 0) {
                if (ea.AdviceArgList != null && ea.AdviceArgList.size() > 0) {
                    // 提交到执行器执行-只是为了更新KSHDSJ，KSHDGH
                    finalOperate.DoubleCheckControlOperate(newEa);
                }
                responseBodyInfo = getResponseBodyInfo(newEa.ExecutResult, ea.JGID);
                response.isSuccess = true;
                response.data = responseBodyInfo;
                return response;
            } else {
                //提交到执行器执行
                finalOperate.Operate(ea);
                executeResultList.addAll(ea.ExecutResult);
            }

            if (executeResultList.size() > 0) {
                responseBodyInfo = getResponseBodyInfo(executeResultList, requestBodyInfo.JGID);
                response.isSuccess = true;
                response.data = responseBodyInfo;
            } else {
                response.isSuccess = false;
                response.message = "[口服药执行失败]服务内部错误!";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[口服药执行失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 拒绝执行
     *
     * @param requestBodyInfo
     * @return
     */
    public BizResponse<ResponseBodyInfo> RefuseExecut(RequestBodyInfo requestBodyInfo) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            if (requestBodyInfo == null || requestBodyInfo.PlanArgInfoList == null || requestBodyInfo.PlanArgInfoList.size() == 0) {
                response.isSuccess = false;
                response.message = "[拒绝执行失败]传入参数有误!";
                return response;
            }
            String now = timeService.now(DataSource.MOB);
            boolean isSucess = false;
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            for (PlanArgInfo info : requestBodyInfo.PlanArgInfoList) {
                if (planInfoManager.CheckRefuse(info.JHH, info.DYXH) != 0) {
                    responseBodyInfo.TableName = "ERROR";
                    responseBodyInfo.Message = "已拒绝同样的原因不能再拒绝!";
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                PlanInfo planInfo = new PlanInfo();
                planInfo.KSSJ = now;
                planInfo.KSGH = requestBodyInfo.YHID;
                planInfo.JSSJ = now;
                planInfo.JSGH = requestBodyInfo.YHID;
                planInfo.JHH = info.JHH;
                isSucess = planInfoManager.editPlanInfoForRefuseExecut(planInfo) > 0;
                if (!isSucess) {
                    break;
                }
                isSucess = planInfoManager.addRefuseExecutReason(info.JHH, info.DYXH, now, requestBodyInfo.YHID) > 0;
                if (!isSucess) {
                    break;
                }
            }
            if (!isSucess) {
                responseBodyInfo.TableName = "ERROR";
                responseBodyInfo.Message = "[拒绝执行失败]数据库执行错误!";
                response.isSuccess = true;
                response.data = responseBodyInfo;
            } else {
                responseBodyInfo = getResponseBodyInfo(requestBodyInfo.PlanArgInfoList, requestBodyInfo.YHID, now, requestBodyInfo.JGID);
                response.isSuccess = true;
                response.data = responseBodyInfo;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[拒绝执行失败]服务内部错误!";
        }
        return response;
    }


    private ExecuteArg flowControl(ExecuteArg executeArg, Object... params) {
        try {
            flowControlList = LoadAssmblys("adviceExcuteFlow");
            ExecuteArg realExecuteArg = null;
            for (IFlowControl flowControl : flowControlList) {
                realExecuteArg = flowControl.BranchCall(executeArg, params);
                if (realExecuteArg != null) {
                    break;
                }
            }
            return realExecuteArg;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private <T> List<T> LoadAssmblys(String configname) {
        List<Flow> flowList = ConfigHandler.getFlowsByFlowGroupId(configname);
        List<T> list = null;
        try {
            list = new ArrayList<T>();
            for (Flow flow : flowList) {
                String value = flow.flowHandlerClass;
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(value);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                if (clazz == null) {
                    continue;
                }
                Constructor<?> cons[] = clazz.getConstructors();
                T t = (T) cons[0].newInstance();
                if (t != null) {
                    list.add(t);
                }
            }
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }


    private ResponseBodyInfo getResponseBodyInfo(List<ExecuteResult> list, String jgid) {
        ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
        responseBodyInfo.TableName = "RE";
        responseBodyInfo.REModelList = new ArrayList<>();
        for (ExecuteResult er : list) {
            REModel model = new REModel();
            model.JHZT = er.ZXZT;
            model.ZXR = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, er.ZXGH);
            model.ZXSJ = er.ZXSJ;
            model.YCXX = er.Msg;
            model.JHH = er.JHH;
            model.YZMC = er.YZMC;
            model.YCLX = Integer.parseInt(er.ResultType);
            model.JHSJ = er.ZXSJ;
            model.JHSJ_NEW = er.JHSJ;
            model.YZZH = er.YZZH;
            if (er.ExecutArg != null && er.ExecutArg.RecordInfo != null && er.ExecutArg.RecordInfo instanceof TransfusionInfo) {
                TransfusionInfo transfusionInfo = (TransfusionInfo) er.ExecutArg.RecordInfo;
                model.YPYF = transfusionInfo.YPYF;
            }
            responseBodyInfo.REModelList.add(model);
        }
        return responseBodyInfo;
    }

    private ResponseBodyInfo getResponseBodyInfo(List<PlanArgInfo> list, String yhid, String zxsj, String jgid) {
        ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
        responseBodyInfo.TableName = "RE";
        responseBodyInfo.REModelList = new ArrayList<>();
        for (PlanArgInfo info : list) {
            REModel model = new REModel();
            model.JHZT = "5";
            model.ZXR = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, yhid);
            model.ZXSJ = zxsj;
            model.YCXX = "";
            model.JHH = info.JHH;
            model.YZMC = "";
            model.YCLX = 0;
            model.JHSJ = info.JHSJ;
            model.JHSJ_NEW = info.JHSJ;
            model.YZZH = info.YZZH;
            responseBodyInfo.REModelList.add(model);
        }
        return responseBodyInfo;
    }

    private ResponseBodyInfo getResponseBodyInfo(FlowRecordInfo flowRecordInfo) {
        ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
        responseBodyInfo.TableName = flowRecordInfo.TableName;
        if (flowRecordInfo.TableName.equals("SY")) {
            responseBodyInfo.SYModelList = new ArrayList<>();
            for (FlowRecordDetailInfo info : flowRecordInfo.list) {
                SYModel model = new SYModel();
                model.SYDH = info.QRDH;
                model.YZMC = info.YZMC;
                model.TMBH = info.TMBH;
                model.SYSJ = info.SYSJ;
                model.JLXX = info.JLXX;
                model.SLXX = info.SLXX;
                model.QRDH = info.QRDH;
                responseBodyInfo.SYModelList.add(model);
            }

        } else if (flowRecordInfo.TableName.equals("SYZT")) {
            responseBodyInfo.SYZTModelList = new ArrayList<>();
            for (FlowRecordDetailInfo info : flowRecordInfo.list) {
                SYZTModel model = new SYZTModel();
                model.QRDH = info.QRDH;
                //add 2018-5-17 15:26:39
                model.JHSJ = info.JHSJ;
                model.YZZH = info.YZZH;
                //
                responseBodyInfo.SYZTModelList.add(model);
            }
        } else if (flowRecordInfo.TableName.equals("SYQX")) {
            responseBodyInfo.SYZTModelList = new ArrayList<>();
            for (FlowRecordDetailInfo info : flowRecordInfo.list) {
                SYZTModel model = new SYZTModel();
                model.QRDH = info.QRDH;
                model.JHSJ = info.JHSJ;
                model.YZZH = info.YZZH;
                responseBodyInfo.SYZTModelList.add(model);
            }
        } else if (flowRecordInfo.TableName.equals("KF")) {
            responseBodyInfo.KFModelList = new ArrayList<>();
            for (FlowRecordDetailInfo info : flowRecordInfo.list) {
                KFModel model = new KFModel();
                model.EQXH = info.YZXH;
                model.YZMC = info.YZMC;
                model.KFMX = info.KFMX;
                model.JLXX = info.JLXX;
                model.SLXX = info.SLXX;
                model.BZSL = info.BZSL;
                model.BZJL = info.BZJL;
                model.YDMS = info.YDMS;
                model.TMBH = info.TMBH;
                model.QRDH = info.QRDH;
                responseBodyInfo.KFModelList.add(model);
            }
        }
        return responseBodyInfo;
    }

    /**
     * 判断当前计划拒绝状态
     *
     * @param jhh
     * @param dyxh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    private Boolean checkRefuse(String jhh, String dyxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        Integer num = service.checkRefuse(jhh, dyxh);
        return num > 0;
    }

    public BizResponse<PlanAndTransfusion> getTransfusionInfoListByZyh4TransfuseExecutAll(String zyh, String jgid, String syrq) {
        BizResponse<PlanAndTransfusion> response = new BizResponse<>();
        List<PlanAndTransfusion> planAndTransfusionList = new ArrayList<>();
        try {
            List<TransfusionInfo> transfusionInfoList = transfuseInfoManager.getTransfusionInfoListByZyh4TransfuseExecutAll(zyh, jgid, syrq);
            for (TransfusionInfo transfusionInfo : transfusionInfoList) {
                PlanAndTransfusion planAndTransfusion = new PlanAndTransfusion();
                List<PlanInfo> planInfoList = planInfoManager.getPlanInfoListByQrdh(transfusionInfo.SYDH, "4", jgid);
                planAndTransfusion.SYDH = transfusionInfo.SYDH;
                planAndTransfusion.SYZT = transfusionInfo.SYZT;
                planAndTransfusion.planInfoList = planInfoList;
                planAndTransfusionList.add(planAndTransfusion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.message = e.getMessage();
            response.isSuccess = false;
        }

        response.isSuccess = true;
        response.datalist = planAndTransfusionList;
        return response;
    }

    public BizResponse<String> getOralInfoListByZyh4Cancel(String jhh, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        OralInfo oralInfo = null;
        try {
            boolean oralJoinPlanByTime = parameterManager.getParameterMap().get(jgid).OralJoinPlanByTime;
            oralInfo = oralInfoManager.getOralInfoByJhh(jhh, oralJoinPlanByTime, jgid);
//            List<PlanInfo> planInfoList = planInfoManager.getPlanInfoListByQrdh(oralInfo.KFDH, "3", jgid);
        } catch (SQLException e) {
            e.printStackTrace();
            response.message = e.getMessage();
            response.isSuccess = false;
        }

        response.isSuccess = true;
        response.data = oralInfo.KFDH;
        return response;
    }

    public BizResponse<String> getInjectionInfoListByZyh4Cancel(String jhh, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        InjectionInfo injectionInfo = null;
        try {
            boolean injectionJoinPlanByTime = parameterManager.getParameterMap().get(jgid).InjectionJoinPlanByTime;
            injectionInfo = injectionInfoManager.getInjectionInfoByJhh(jhh, injectionJoinPlanByTime, jgid);
//            List<PlanInfo> planInfoList = planInfoManager.getPlanInfoListByQrdh(injectionInfo.ZSDH, "5", jgid);
        } catch (SQLException e) {
            e.printStackTrace();
            response.message = e.getMessage();
            response.isSuccess = false;
        }

        response.isSuccess = true;
        response.data = injectionInfo.ZSDH;
        return response;
    }
    /*
        升级编号【56010053】============================================= start
        多瓶超过2瓶转接瓶后提示选择接哪瓶的问题
        ================= Classichu 2017/11/14 16:25

        */
    public BizResponse<PlanAndTransfusion> getTransfusionInfoListByZyh4TransfuseExecut(String zyh, String jgid, String syrq) {
        BizResponse<PlanAndTransfusion> response = new BizResponse<>();
        List<PlanAndTransfusion> planAndTransfusionList = new ArrayList<>();
        try {
            List<TransfusionInfo> transfusionInfoList = transfuseInfoManager.getTransfusionInfoListByZyh4TransfuseExecut(zyh, jgid, syrq);
            for (TransfusionInfo transfusionInfo : transfusionInfoList) {
                PlanAndTransfusion planAndTransfusion = new PlanAndTransfusion();
                List<PlanInfo> planInfoList = planInfoManager.getPlanInfoListByQrdh(transfusionInfo.SYDH, "4", jgid);
                planAndTransfusion.SYDH = transfusionInfo.SYDH;
                planAndTransfusion.SYZT = transfusionInfo.SYZT;
                planAndTransfusion.planInfoList = planInfoList;
                planAndTransfusionList.add(planAndTransfusion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.message = e.getMessage();
            response.isSuccess = false;
        }

        response.isSuccess = true;
        response.datalist = planAndTransfusionList;
        return response;
    }

    public BizResponse<PlanAndTransfusion> getTransfusionInfoByBarcode4TransfuseExecut(String barcode, String prefix, String jgid) {
        BizResponse<PlanAndTransfusion> response = new BizResponse<>();
        PlanAndTransfusion planAndTransfusion = new PlanAndTransfusion();
        try {
            boolean transfusionUsePrefix = parameterManager.getParameterMap().get(jgid).TransfusionUsePrefix;
            TransfusionInfo transfusionInfo = transfuseInfoManager.getTransfusionInfoByBarcode(barcode, prefix, transfusionUsePrefix, jgid);
            List<PlanInfo> planInfoList = planInfoManager.getPlanInfoListByQrdh(transfusionInfo.SYDH, "4", jgid);
            planAndTransfusion.SYDH = transfusionInfo.SYDH;
            planAndTransfusion.SYZT = transfusionInfo.SYZT;
            planAndTransfusion.planInfoList = planInfoList;
        } catch (SQLException e) {
            e.printStackTrace();
            response.message = e.getMessage();
            response.isSuccess = false;
        }

        response.isSuccess = true;
        response.data = planAndTransfusion;
        return response;
    }
    /* =============================================================== end */

    /**
     * 输液非常规执行(并行接瓶)
     *
     * @param data
     * @return
     */
    public BizResponse<ResponseBodyInfo> TransfuseExecutNew(RequestBodyInfo data) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            // 提交到预处理器
            ExecuteArg ea = transfusePreprocessing.Preprocessing(data.ZYH, data.YHID, data.QRDH, data.JGID, data.QRDH);
            if (ea.ExecutResult != null && !ea.ExecutResult.isEmpty()) {
                responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, data.JGID);
                response.isSuccess = true;
                response.data = responseBodyInfo;
                return response;
            }
            TransfuseArgs tArgs = new TransfuseArgs();
            tArgs.IsParallel = data.SYBX;
            tArgs.JP_SYDH = data.JPQRDH;
            ea.Args = tArgs;

            // 提交到执行器执行
            finalOperate.Operate(ea);
            List<ExecuteResult> erList = ea.ExecutResult;
            if (!erList.isEmpty()) {
                List<ExecuteArg> executeArgList = new ArrayList<>();
                executeArgList.add(ea);
                responseBodyInfo = getResponseBodyInfo(erList, data.JGID);
                if (parameterManager.getParameterMap().get(data.JGID).SyncNuserRecord) {
                    //add
                    if (executeArgList != null) {
                        Response<InArgument> inArgumentResponse = FJXH_GetSyncData(executeArgList);
                        if (inArgumentResponse.ReType == 0) {
                            //获取同步数据成功 赋值 给前台
                            responseBodyInfo.inArgument = inArgumentResponse.Data;
                        }
                    }
                }
                response.isSuccess = true;
                response.data = responseBodyInfo;
            } else {
                response.isSuccess = false;
                response.message = "[输液非常规执行失败]无法找到对应医嘱数据";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[输液非常规执行失败]服务内部错误!";
        }
        return response;
    }


    public BizResponse<ResponseBodyInfo> ZSExecutCancelStart(RequestBodyInfo data) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            List<ExecuteResult> executeResultList = new ArrayList<>();
            // 当前数据库时间
            String now = timeService.now(DataSource.PORTAL);
            String jhh = data.core;
            // 提交预处理器 TODO 待确认
            boolean isCancel = true;//协和  取消执行
            ExecuteArg ea = injectionPreprocessing.Preprocessing(jhh,data.ZYH, data.YHID, data.QRDH, data.JGID, isCancel);
            if (ea != null && "5".equals(ea.GSLX)) { // 注射类
                if (!ea.ExecutResult.isEmpty()) {
                    responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                //特殊处理：给ea.FlowRecordInfo赋值
                finalPreprocessing.getRealExecuteArg(ea);
                if (ea.FlowRecordInfo != null) {
                    responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 走流程控制
//                ExecuteArg newEa = flowControl(ea);
                ExecuteArg newEa = flowControl(ea, "isCancel");
                if (newEa != null && newEa.ExecutResult.size() > 0) {
                    responseBodyInfo = getResponseBodyInfo(newEa.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 输液取消结束处理
                injectionDBOperate.CancelStart(ea);

            }
            finalOperate.FinalOperate(ea);
            // 整理返回值
            // TODO 返回值待确认

           /* for (String jhh : jhhList) {
                SYZTModel model = new SYZTModel();
                model.QRDH = jhh;
                SYZTModelList.add(model);
            }*/
            //协和
            //
            List<ZSModel> ZSModelList = new ArrayList<>();
            for (PlanInfo planInfo : ea.PlanInfoList) {
                ZSModel model = new ZSModel();
                model.QRDH = planInfo.JHH;
                model.YZZH = planInfo.YZZH;
                model.JHSJ = planInfo.JHSJ;
                model.JHH = planInfo.JHH;
                ZSModelList.add(model);
            }
            responseBodyInfo.TableName = "ZSQX";
            responseBodyInfo.ZSModelList = ZSModelList;
            response.data = responseBodyInfo;
            response.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[注射取消执行失败]服务内部错误";
        }
        return response;
    }

    public BizResponse<ResponseBodyInfo> KFExecutCancelStart(RequestBodyInfo data) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            List<String> jhhList = new ArrayList<>();
            List<ExecuteResult> tempExecuteResult = new ArrayList<>();
            // 当前数据库时间
            String now = timeService.now(DataSource.PORTAL);
            // 提交预处理器 TODO 待确认
            boolean isCancel = true;//协和  取消执行
            ExecuteArg ea = oralMedicationPreprocessing.Preprocessing(data.ZYH, data.YHID, data.QRDH, data.JGID, isCancel);
            if (ea != null && "3".equals(ea.GSLX)) { // 口服类
                if (!ea.ExecutResult.isEmpty()) {
                    responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                //特殊处理：给ea.FlowRecordInfo赋值
                finalPreprocessing.getRealExecuteArg(ea);
                if (ea.FlowRecordInfo != null) {
                    responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 走流程控制
//                ExecuteArg newEa = flowControl(ea);
                ExecuteArg newEa = flowControl(ea, "isCancel");
                if (newEa != null && newEa.ExecutResult.size() > 0) {
                    responseBodyInfo = getResponseBodyInfo(newEa.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 输液取消结束处理
                oralMedicationDBOperate.CancelStart(ea);

            }
            finalOperate.FinalOperate(ea);
            // 整理返回值
            // TODO 返回值待确认

           /* for (String jhh : jhhList) {
                SYZTModel model = new SYZTModel();
                model.QRDH = jhh;
                SYZTModelList.add(model);
            }*/
            //协和
            //
            List<KFModel> KFModelList = new ArrayList<>();
            for (PlanInfo planInfo : ea.PlanInfoList) {
                KFModel model = new KFModel();
                model.QRDH = planInfo.JHH;
                model.YZZH = planInfo.YZZH;
                model.JHSJ = planInfo.JHSJ;
                model.JHH = planInfo.JHH;
                KFModelList.add(model);
            }
            responseBodyInfo.TableName = "KFQX";
            responseBodyInfo.KFModelList = KFModelList;
            response.data = responseBodyInfo;
            response.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[口服药取消执行失败]服务内部错误";
        }
        return response;
    }


    public BizResponse<ResponseBodyInfo> TransfuseExecutCancelStart(RequestBodyInfo data) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            List<String> jhhList = new ArrayList<>();
            List<ExecuteResult> tempExecuteResult = new ArrayList<>();
            // 当前数据库时间
            String now = timeService.now(DataSource.PORTAL);
            // 提交预处理器 TODO 待确认
            boolean isCancel = true;//协和  取消执行
            ExecuteArg ea = transfusePreprocessing.Preprocessing(data.ZYH, data.YHID, data.QRDH, data.QRDH, data.JGID, isCancel);
            if (ea != null && "4".equals(ea.GSLX)) { // 输液类
                if (!ea.ExecutResult.isEmpty()) {
                    responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                //特殊处理：给ea.FlowRecordInfo赋值
                finalPreprocessing.getRealExecuteArg(ea);
                if (ea.FlowRecordInfo != null) {
                    responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 走流程控制
//                ExecuteArg newEa = flowControl(ea);
                ExecuteArg newEa = flowControl(ea, "isCancel");
                if (newEa != null && newEa.ExecutResult.size() > 0) {
                    responseBodyInfo = getResponseBodyInfo(newEa.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 输液取消结束处理

                transfuseDBOperate.CancelStart(ea, ea.QRDH, now);

            }
            finalOperate.FinalOperate(ea);
            // 整理返回值
            // TODO 返回值待确认

           /* for (String jhh : jhhList) {
                SYZTModel model = new SYZTModel();
                model.QRDH = jhh;
                SYZTModelList.add(model);
            }*/
            //协和
            //
            List<SYZTModel> SYZTModelList = new ArrayList<>();
            for (PlanInfo planInfo : ea.PlanInfoList) {
                SYZTModel model = new SYZTModel();
                model.QRDH = planInfo.JHH;
                model.YZZH = planInfo.YZZH;
                model.JHSJ = planInfo.JHSJ;
                SYZTModelList.add(model);
            }
            responseBodyInfo.TableName = "SYQX";
            responseBodyInfo.SYZTModelList = SYZTModelList;
            response.data = responseBodyInfo;
            response.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[输液取消执行失败]服务内部错误";
        }
        return response;
    }


    public BizResponse<ResponseBodyInfo> TransfuseExecutCancelEnd(RequestBodyInfo data) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            List<String> jhhList = new ArrayList<>();
            List<ExecuteResult> tempExecuteResult = new ArrayList<>();
            // 当前数据库时间
            String now = timeService.now(DataSource.PORTAL);
            // 提交预处理器 TODO 待确认
            boolean isCancel = true;//协和  取消执行
            ExecuteArg ea = transfusePreprocessing.Preprocessing(data.ZYH, data.YHID, data.QRDH, data.QRDH, data.JGID, isCancel);
            if (ea != null && "4".equals(ea.GSLX)) { // 输液类
                if (!ea.ExecutResult.isEmpty()) {
                    responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                //特殊处理：给ea.FlowRecordInfo赋值
                finalPreprocessing.getRealExecuteArg(ea);
                if (ea.FlowRecordInfo != null) {
                    responseBodyInfo = getResponseBodyInfo(ea.FlowRecordInfo);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 走流程控制
//                ExecuteArg newEa = flowControl(ea);
                ExecuteArg newEa = flowControl(ea, "isCancel");
                if (newEa != null && newEa.ExecutResult.size() > 0) {
                    responseBodyInfo = getResponseBodyInfo(newEa.ExecutResult, data.JGID);
                    response.isSuccess = true;
                    response.data = responseBodyInfo;
                    return response;
                }
                // 输液取消结束处理

                transfuseDBOperate.CancelEnd(ea, ea.QRDH, now);

            }
            finalOperate.FinalOperate(ea);
            // 整理返回值
            // TODO 返回值待确认

           /* for (String jhh : jhhList) {
                SYZTModel model = new SYZTModel();
                model.QRDH = jhh;
                SYZTModelList.add(model);
            }*/
            //协和
            //
            List<SYZTModel> SYZTModelList = new ArrayList<>();
            for (PlanInfo planInfo : ea.PlanInfoList) {
                SYZTModel model = new SYZTModel();
                model.QRDH = planInfo.JHH;
                model.YZZH = planInfo.YZZH;
                model.JHSJ = planInfo.JHSJ;
                model.JHH = planInfo.JHH;
                SYZTModelList.add(model);
            }
            responseBodyInfo.TableName = "SYQX";
            responseBodyInfo.SYZTModelList = SYZTModelList;
            response.data = responseBodyInfo;
            response.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[输液取消执行失败]服务内部错误";
        }
        return response;
    }

    @Deprecated
    public BizResponse<ResponseBodyInfo> TransfuseExecut(RequestBodyInfo requestBodyInfo) {
        BizResponse<ResponseBodyInfo> response = new BizResponse<>();
        try {
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            // 提交到预处理器
            ExecuteArg ea = transfusePreprocessing.Preprocessing(requestBodyInfo.ZYH, requestBodyInfo.YHID, requestBodyInfo.QRDH, requestBodyInfo.JGID, requestBodyInfo.QRDH);
            if (ea.ExecutResult != null && !ea.ExecutResult.isEmpty()) {
                responseBodyInfo = getResponseBodyInfo(ea.ExecutResult, requestBodyInfo.JGID);
                response.isSuccess = true;
                response.data = responseBodyInfo;
                return response;
            }
            TransfuseArgs tArgs = new TransfuseArgs();
            tArgs.IsParallel = requestBodyInfo.SYBX;
            tArgs.JP_SYDH = requestBodyInfo.JPQRDH;
            ea.Args = tArgs;

            // 提交到执行器执行
            finalOperate.Operate(ea);
            List<ExecuteResult> erList = ea.ExecutResult;
            if (!erList.isEmpty()) {
                responseBodyInfo = getResponseBodyInfo(erList, requestBodyInfo.JGID);
                List<ExecuteArg> executeArgList = new ArrayList<>();
                executeArgList.add(ea);
                if (parameterManager.getParameterMap().get(requestBodyInfo.JGID).SyncNuserRecord) {
                    Response<SelectResult> syncResponse = buildSyncData(executeArgList);
                    //执行成功
                    if (syncResponse.ReType == 2) {
                        //同步成功
                        responseBodyInfo.IsSync = true;
                        responseBodyInfo.SyncData = syncResponse.Data;
                        //todo
                        response.message = "用户选择！";
                    } else if (syncResponse.ReType == 1) {
                        //同步失败
                        response.message = "执行成功，医嘱已写入护理记录单！";
                    } else if (syncResponse.ReType == 1111) {
                        //特殊 没有 24小时出入量 直接不同步
                        response.message = "执行成功！";
                    } else if (syncResponse.ReType == 0) {
                        //同步失败
                        response.message = "执行成功，写入护理记录单失败！";
                    }
                }
                response.isSuccess = true;
                response.data = responseBodyInfo;
            } else {
                response.isSuccess = false;
                response.message = "[输液非常规执行失败]无法找到对应医嘱数据";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[输液非常规执行失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 输液滴速记录
     *
     * @param record
     * @return
     */
    public BizResponse<String> DropSpeedInput(TransfusionPatrolRecord record) {
        BizResponse<String> response = new BizResponse<>();
        try {
            keepOrRoutingDateSource(DataSource.MOB);
            // 获取输液状态
            TransfusionInfo tInfo = transfuseInfoManager.transfuseStopCheck(record.SYDH, record.JGID);
            if (tInfo == null) {
                response.isSuccess = false;
                response.message = "[输液滴速记录失败]未找到该输液单";
                return response;
            }
            String syzt = tInfo.SYZT;
            if (!"2".equals(syzt)) {
                response.isSuccess = false;
                response.message = "[输液滴速记录失败]只能记录执行中输液滴速";
                return response;
            }
            TransfusionPatrolRecordVo recordVo = new TransfusionPatrolRecordVo(record);
            recordVo.XSBS = String.valueOf(identityService.getIdentityMax("IENR_SYXS", DataSource.MOB));
            recordVo.XSSJ = timeService.now(DataSource.PORTAL);
            keepOrRoutingDateSource(DataSource.MOB);
            Integer num = transfuseInfoManager.insertTransfusionPatrol(recordVo);
            if (num > 0) {
                response.isSuccess = true;
            } else {
                response.isSuccess = false;
                response.message = "[输液滴速记录失败]提交失败";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[输液滴速记录失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "[输液滴速记录失败]服务内部错误!";
        }
        return response;
    }


    private Response<InArgument> FJXH_GetSyncData(List<ExecuteArg> executeArgList) throws SQLException, DataAccessException {
        Response<InArgument> response = new Response<>();
        if (executeArgList == null || executeArgList.isEmpty()) {
            response.ReType = -999;
            response.Msg = "executeArgList is null";
            return response;
        }
        ExecuteArg executeArg = executeArgList.get(0);
        String now = timeService.now(DataSource.MOB);
        boolean isJingTui = false;
        if (executeArg.PlanInfoList != null && !executeArg.PlanInfoList.isEmpty()) {
            PlanInfo planInfo = executeArg.PlanInfoList.get(0);
//        isJingTui="6".equals(planInfo.YPYF);
            String jingTuiYF = userConfigService.getUserConfig(executeArg.JGID).jingTui_YaoPinYongFa;
            isJingTui = jingTuiYF.equals(planInfo.YPYF);
        }
        InArgument inArgument = new InArgument();
        inArgument.zyh = executeArg.ZYH;
        inArgument.bqdm = executeArg.BRBQ;
        inArgument.hsgh = executeArg.YHID;
        inArgument.bdlx = "9";
        inArgument.lybd = "0";
        inArgument.flag = "0";
        //赋值在后面
        //inArgument.jlxh = executeArg.QRDH;
        inArgument.jgid = executeArg.JGID;
        inArgument.lymx = "0";
        inArgument.lymxlx = "0";
        inArgument.jlsj = now;

        boolean only_Sync_24 = userConfigService.getUserConfig(executeArg.JGID).qiYong_Only_Sync_24;
        if (only_Sync_24) {
            //福建协和 是否有 24小时出入量 有—同步  无—不同步
            List<String> xmidList = systemParamService.getUserParams("1", "IENR", "IENR_YZZX_ZLXMID", inArgument.jgid, DataSource.MOB).datalist;
            if (xmidList != null && !xmidList.isEmpty()) {
                String xmid = xmidList.get(0);
                //DateUtil.getApplicationDate() 医嘱执行得时间
                Integer dataCount = planInfoManager.getNeedSyncDataCount(inArgument.zyh, xmid, DateUtil.getApplicationDate());
                if (dataCount <= 0) {
                    //无
                    response.ReType = -888;
                    response.Msg = "没有24小时出入量，不同步";
                    return response;
                }
            } else {
                response.ReType = -888;
                response.Msg = "没有24小时出入量，不同步";
                return response;
            }
        }
        //同步
        //
        String jlxh = "";//同步保存的来源编号
        keepOrRoutingDateSource(DataSource.MOB);
        for (ExecuteArg ea : executeArgList) {
            //福建协和客户化：保存医嘱计划号，不保存确认单号
            boolean syncSaveJHH = userConfigService.getUserConfig(executeArg.JGID).syncSaveJHH;
            if (syncSaveJHH) {
                if (ea.PlanInfoList != null && ea.PlanInfoList.size() > 0) {
                    jlxh = ea.PlanInfoList.get(0).JHH;//保存医嘱计划号
                }
            } else {
                jlxh = ea.QRDH;//保存确认单号
            }
            if (!StringUtils.isBlank(jlxh)) {
                inArgument.jlxh = jlxh;
            }

            // 医嘱类型判断(输液和注射需要同步)
            String gslx = ea.GSLX;
            //福建协和客户化：医嘱名称特殊处理
            if (!"4".equals(gslx) && !"5".equals(gslx)) {
                //输液 4  注射 5 直接继续
                continue;
            }
            // 输液医嘱，执行前的执行状态不是未执行(0)时，flag为1 修改
            if ("4".equals(gslx)) {
                String zxzt = ea.PlanInfoList.get(0).ZXZT;
                if (!"0".equals(zxzt) && !isJingTui) {
                    inArgument.flag = "1";//不同步
                } else if (isJingTui) {
                    inArgument.flag = "0";//同步
                }
            }
            Float zjl = 0f;
            Project project1_jlxh = new Project("1", jlxh);
            boolean syncDealYZMC = userConfigService.getUserConfig(executeArg.JGID).syncDealYZMC;
            if (syncDealYZMC) {
                //福建协和客户化：医嘱名称特殊处理
                String yzmc = "";
                String lsbs = "";
                for (PlanInfo planInfo : ea.PlanInfoList) {
                    String ypyf = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YPYF, planInfo.JGID, planInfo.YPYF);
                    //
                    List<PlanInfo> palsit = planInfoManager.getPlanInfoListByQrdh(planInfo.QRDH, planInfo.GSLX, planInfo.JGID);
                    if (palsit != null && !palsit.isEmpty()) {
                        lsbs = palsit.get(0).LSBS;
                        if (!TextUtils.isEmpty(lsbs)) {
//                    lsbs = ("0".equals(lsbs)||"-1".equals(lsbs)) ? "" : lsbs;
                            lsbs = String.valueOf(NumberCharParser.parserNumWithCircle(Integer.valueOf(lsbs)));
                        }
                    }
                    //
                    String ycjl = planInfo.YCJL;
                    if (!StringUtils.isBlank(ycjl)) {
                        //处理 保留 2 位小数
                        ycjl = StringTool.parseDecimalStr2Fixed(ycjl);
                    }
                    if (!TextUtils.isEmpty(lsbs) && !yzmc.contains(lsbs)) {
                        yzmc += lsbs + planInfo.YZMC + "|||" + ycjl + "|||" + planInfo.JLDW + "|||" + ypyf + "$$$";
                    } else {
                        yzmc += planInfo.YZMC + "|||" + ycjl + "|||" + planInfo.JLDW + "|||" + ypyf + "$$$";
                    }
              /*  //获取最小剂量，判断是否需要同步
                String lxh = planInfo.LXH;
                Float ycjl = Float.parseFloat(planInfo.YCJL);
                if (planInfo.JLDW.toLowerCase().equals("ml")) {
                    Float tbzxjl = planInfoManager.getTbzxjlByLxh(lxh, ea.JGID);
                    if (tbzxjl == null || ycjl >= tbzxjl) {
                        zjl += ycjl;
                        Project newProject1 = new Project(ea.QRDH, planInfo.YZMC);
                        project1.saveProjects.add(newProject1);
                    }
                } else if (planInfo.JLDW.toLowerCase().equals("l")) {
                    Float tbzxjl = planInfoManager.getTbzxjlByLxh(lxh, ea.JGID);
                    if (tbzxjl == null || ycjl >= tbzxjl) {
                        zjl += ycjl * 1000;
                        Project newProject1 = new Project(ea.QRDH, planInfo.YZMC);
                        project1.saveProjects.add(newProject1);
                    }
                }*/
                    //
                }
                if (!StringUtils.isBlank(yzmc)) {
                    if (yzmc.contains("$$$")) {
                        //去掉最后的三个$符号
                        yzmc = yzmc.substring(0, yzmc.length() - 3);
                    }
                    Project projectJlxh_yzmc = new Project(jlxh, yzmc);
                    project1_jlxh.saveProjects.add(projectJlxh_yzmc);
                }
            } else {
                //标准版
                // 获取最小剂量，判断是否需要同步
                for (PlanInfo planInfo : ea.PlanInfoList) {
                    String lxh = planInfo.LXH;
                    String yzmc = "";
                    String lsbs = "";
                    //
                    List<PlanInfo> palsit = planInfoManager.getPlanInfoListByQrdh(planInfo.QRDH, planInfo.GSLX, planInfo.JGID);
                    if (palsit != null && !palsit.isEmpty()) {
                        lsbs = palsit.get(0).LSBS;
                        if (!TextUtils.isEmpty(lsbs)) {
//                    lsbs = ("0".equals(lsbs)||"-1".equals(lsbs)) ? "" : lsbs;
                            lsbs = String.valueOf(NumberCharParser.parserNumWithCircle(Integer.valueOf(lsbs)));
                        }
                    }
                    //
                    if (!TextUtils.isEmpty(lsbs) && !yzmc.contains(lsbs)) {
                        yzmc = lsbs + planInfo.YZMC;
                    } else {
                        yzmc = planInfo.YZMC;
                    }
                    Float ycjl = Float.parseFloat(planInfo.YCJL);
                    if (planInfo.JLDW.toLowerCase().equals("ml")) {
                        Float tbzxjl = planInfoManager.getTbzxjlByLxh(lxh, ea.JGID);
                        if (tbzxjl == null || ycjl >= tbzxjl) {
                            zjl += ycjl;
                            Project projectJlxh_yzmc = new Project(jlxh, yzmc);
                            project1_jlxh.saveProjects.add(projectJlxh_yzmc);
                        }
                    } else if (planInfo.JLDW.toLowerCase().equals("l")) {
                        Float tbzxjl = planInfoManager.getTbzxjlByLxh(lxh, ea.JGID);
                        if (tbzxjl == null || ycjl >= tbzxjl) {
                            zjl += ycjl * 1000;
                            Project projectJlxh_yzmc = new Project(jlxh, yzmc);
                            project1_jlxh.saveProjects.add(projectJlxh_yzmc);
                        }
                    } else {
                        //其他不同步剂量
                        Project projectJlxh_yzmc = new Project(jlxh, yzmc);
                        project1_jlxh.saveProjects.add(projectJlxh_yzmc);
                    }
                }
            }
            inArgument.projects.add(project1_jlxh);
            if (zjl > 0) {
                Project project2_jlxh = new Project("2", jlxh);
                Project projectJlxh_zjl = new Project(jlxh, zjl.toString());
                project2_jlxh.saveProjects.add(projectJlxh_zjl);
                inArgument.projects.add(project2_jlxh);
            }
        }
        if (inArgument.projects.isEmpty()) {
            response.ReType = -998;
            response.Msg = "projects isEmpty";
            return response;
        }
        response.ReType = 0;
        response.Data = inArgument;
        response.Msg = "获取同步数据成功";
        return response;

    }

    public Response<ResponseBodyInfo> FJXH_RealDoSync(InArgument inArgument) {
        Response<ResponseBodyInfo> response = new Response();
        response.Data = new ResponseBodyInfo();
        Response<SelectResult> responseTemp = new Response();
        if (inArgument == null) {
            response.ReType = 0;
            response.Msg = "同步目标失败:没有需要同步的数据";
        }
        try {
            // ReType 0 失败 1 成功  2 用户选择
            responseTemp = Client
                    .rpcInvoke("nis-synchron.synchronRpcServerProvider", "synchron", inArgument);
        } catch (Throwable throwable) {
            response.ReType = 0;
            response.Msg = "同步目标失败：" + throwable.getMessage();
            logger.error(throwable.getMessage(), throwable);
        }
        if (responseTemp.Data == null) {
            SelectResult selectResult = new SelectResult();
            //让 SelectResult 不为空  前台使用
            selectResult.UUID = String.valueOf(UUID.randomUUID());
            responseTemp.Data = new SelectResult();
        }
        if (responseTemp.Data.UUID == null || "".equals(responseTemp.Data.UUID)) {
            //让 SelectResult 不为空  前台使用
            responseTemp.Data.UUID = String.valueOf(UUID.randomUUID());
        }
        response.Data.SyncData = responseTemp.Data;
        //前台传过来 再传回去
        List<REModel> reModelList = new ArrayList<>();
        REModel reModel = new REModel();
        reModel.YZZH = inArgument.YZZH4TB;
        reModel.JHSJ_NEW = inArgument.JHSJ4TB;
        reModelList.add(reModel);
        //
        response.Data.REModelList = reModelList;
        response.Msg = responseTemp.Msg;
        response.ReType = responseTemp.ReType;
        return response;
    }

    /**
     * 医嘱执行的同步操作
     *
     * @param executeArgList
     * @return
     */
    @Deprecated
    private Response<SelectResult> buildSyncData(List<ExecuteArg> executeArgList) throws
            SQLException, DataAccessException {
        Response<SelectResult> response = new Response<>();
        if (executeArgList == null || executeArgList.isEmpty()) {
            return response;
        }
        ExecuteArg executeArg = executeArgList.get(0);
        String now = timeService.now(DataSource.MOB);

        InArgument inArgument = new InArgument();
        inArgument.zyh = executeArg.ZYH;
        inArgument.bqdm = executeArg.BRBQ;
        inArgument.hsgh = executeArg.YHID;
        inArgument.bdlx = "9";
        inArgument.lybd = "0";
        inArgument.flag = "0";
        inArgument.jlxh = executeArg.QRDH;
        inArgument.jgid = executeArg.JGID;
        inArgument.lymx = "0";
        inArgument.lymxlx = "0";
        inArgument.jlsj = now;

        keepOrRoutingDateSource(DataSource.MOB);
        for (ExecuteArg ea : executeArgList) {
            // 医嘱类型判断(输液和注射需要同步)
            String gslx = ea.GSLX;
            if (!"4".equals(gslx) && !"5".equals(gslx)) {
                //输液 4  注射 5 直接继续
                continue;
            }
            // 输液医嘱，执行前的执行状态不是未执行(0)时，flag为1 修改
            if ("4".equals(gslx)) {
                String zxzt = ea.PlanInfoList.get(0).ZXZT;
                if (!"0".equals(zxzt)) {
                    inArgument.flag = "1";
                }
            }

            Float zjl = 0f;
            Project project1 = new Project("1", ea.QRDH);
            // 获取最小剂量，判断是否需要同步
            for (PlanInfo planInfo : ea.PlanInfoList) {
                String lxh = planInfo.LXH;
                Float ycjl = Float.parseFloat(planInfo.YCJL);
                if (planInfo.JLDW.toLowerCase().equals("ml")) {
                    Float tbzxjl = planInfoManager.getTbzxjlByLxh(lxh, ea.JGID);
                    if (tbzxjl == null || ycjl >= tbzxjl) {
                        zjl += ycjl;
                        Project newProject1 = new Project(ea.QRDH, planInfo.YZMC);
                        project1.saveProjects.add(newProject1);
                    }
                } else if (planInfo.JLDW.toLowerCase().equals("l")) {
                    Float tbzxjl = planInfoManager.getTbzxjlByLxh(lxh, ea.JGID);
                    if (tbzxjl == null || ycjl >= tbzxjl) {
                        zjl += ycjl * 1000;
                        Project newProject1 = new Project(ea.QRDH, planInfo.YZMC);
                        project1.saveProjects.add(newProject1);
                    }
                } else if (planInfo.JLDW.toLowerCase().equals("g")){


                } else if (planInfo.JLDW.toLowerCase().equals("kg")){

                }
            }

            inArgument.projects.add(project1);
            Project project2 = new Project("2", ea.QRDH);
            Project newProject2 = new Project(ea.QRDH, zjl.toString());
            project2.saveProjects.add(newProject2);
            inArgument.projects.add(project2);
        }

        if (inArgument.projects.isEmpty()) {
            return response;
        }

        try {
            response = Client
                    .rpcInvoke("nis-synchron.synchronRpcServerProvider", "synchron", inArgument);
        } catch (Throwable throwable) {
            response.ReType = 0;
            response.Msg = "同步目标失败";
            logger.error(throwable.getMessage(), throwable);
        }
        return response;
    }

}
