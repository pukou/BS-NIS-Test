package com.bsoft.nis.adviceexecute.Operates;

import com.bsoft.nis.adviceexecute.ModelManager.*;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.UserConfigService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.adviceexecute.*;
import com.bsoft.nis.domain.adviceqyery.db.TransfusionInfoVoTemp;
import com.bsoft.nis.mapper.patient.PatientMapper;
import com.bsoft.nis.util.date.DateConvert;
import com.bsoft.nis.util.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 输液类执行器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-28
 * Time: 16:52
 * Version:
 */
@Component
public class TransfuseDBOperate {

    @Autowired
    MessageManager messageManager;//消息生成管理器
    @Autowired
    TransfuseInfoManager transfuseInfoManager; // 输液操作管理器
    @Autowired
    ParameterManager parameterManager; // 用户参数管理器
    @Autowired
    UserConfigService userConfigService; // 用户参数管理器
    @Autowired
    PlanInfoManager planInfoManager; // 医嘱计划操作管理器
    @Autowired
    AdviceYzbInfoManager adviceYzbInfoManager; // 医嘱本操作管理器
    @Autowired
    PatientMapper patientMapper;

    @Autowired
    DateTimeService timeService; // 日期时间服务

    /*
        升级编号【56010053】============================================= start
        多瓶超过2瓶转接瓶后提示选择接哪瓶的问题
        ================= Classichu 2017/11/14 16:25

        */

    /**
     * 医嘱执行  路数
     * *
     **/
    public void Operate(ExecuteArg ea, String jp_sydh)
            throws SQLException, DataAccessException, ParseException {
        if (ea.GSLX.equals("4")) {
            String now = timeService.now(DataSource.MOB);
            //
            if (ea.RecordInfo != null && ea.RecordInfo instanceof TransfusionInfo) {
                TransfusionInfo transfusionInfo = (TransfusionInfo) ea.RecordInfo;
                //输液（静推） 处理
                String jgid = ea.JGID;
                String jingTuiYF = userConfigService.getUserConfig(jgid).jingTui_YaoPinYongFa;
                if (jingTuiYF.equals(transfusionInfo.YPYF)) {
//                if ("6".equals(transfusionInfo.YPYF)) {
                    //开始即结束
                    String bp_sydh = ea.QRDH;
//                   Start(ea,bp_sydh,now);
                    End(ea, bp_sydh, now, true);
//                   ea.ExecutResult.add()
                    System.out.println("测试：test==0==");
                    return;
                }
            }
            //前端干预的接瓶逻辑处理  重点是 jp_sydh
            if (!StringUtils.isBlank(ea.QRDH)) {
                String bp_sydh = ea.QRDH;//本瓶单号
                if (!StringUtils.isBlank(jp_sydh)) {
                    //暂停——>继续 直接开始
                    if ("[ztsydh]".equals(jp_sydh)) {
                        System.out.println("测试：test==1==ztsydh");
                        Start(ea, bp_sydh, now);
                        return;
                    }
                    //暂停——>结束 直接结束
                    if ("[jssydh]".equals(jp_sydh)) {
                        System.out.println("测试：test==2==jssydh");
                        End(ea, bp_sydh, now);
                        return;
                    }
                    //上瓶==本瓶 直接结束
                    if (jp_sydh.equals(bp_sydh)) {
                        End(ea, bp_sydh, now);
                        System.out.println("测试：test==3==");
                        return;
                    }
                    //其他情况做接瓶操作
                    //==========
                    setupLSBS_JP(ea, jp_sydh);
                    //======
                    //结束上瓶，开始本瓶
                    Continue(ea, bp_sydh, jp_sydh, now);
                    System.out.println("测试：test==4==");
                    return;
                }
                boolean isbx = false;//并行标志
                String sp_sydh = "";
                ///####2017-11-13 18:38:01  String sp_sydh = null;//上瓶输液单号
                /* =============================================================== end */
                boolean spzt = false;//上瓶是暂停状态
                if (ea.Args != null && ea.Args.getClass().equals(TransfuseArgs.class)) {
                    TransfuseArgs transfuseArgs = (TransfuseArgs) ea.Args;
                    isbx = transfuseArgs.IsParallel;
                    if (transfuseArgs.Qzjs) {//强制结束
                        End(ea, bp_sydh, now);
                        System.out.println("测试：test==5==");
                        return;
                    }
                }
                if (StringUtils.isBlank(sp_sydh)) {
                    String syrq = null;
                    if (ea.RecordInfo != null && ea.RecordInfo instanceof TransfusionInfo) {
                        TransfusionInfo transfusionInfo = (TransfusionInfo) ea.RecordInfo;
                        syrq = transfusionInfo.SYSJ == null ? null : transfusionInfo.SYSJ.substring(0, 10);
                    }
                    List<TransfusionInfo> list = transfuseInfoManager.getTransfusionInfoListByZyh(ea.ZYH, ea.JGID, syrq);
                    if (list != null && list.size() > 0) {
                        for (TransfusionInfo info : list) {
                            if (!info.SYDH.equals(ea.QRDH)) {
                                sp_sydh = info.SYDH;
                                System.out.println("测试：sp_sydh:" + sp_sydh);
                                break;
                            }
                        }
                    }
                }

                TransfusionInfo info = (TransfusionInfo) ea.RecordInfo;
                if (isbx) {//多路输液，重心：只关注本瓶，不关注其他瓶
                    if (info.SYZT.equals("0") || info.SYZT.equals("4")) {
                        //只需开始本瓶即可
                        //=======
                        setupLSBS(ea, info);
                        //=======
                        Start(ea, bp_sydh, now);
                        //
                        System.out.println("测试：test==6==");
                    } else if (info.SYZT.equals("2")) {
                        //只需结束本瓶
                        End(ea, bp_sydh, now);
                        System.out.println("测试：test==7==");
                    } else {
                        System.out.println("测试：test==15==");
                    }
                } else {//非多路输液，重心：不仅要关注本瓶，还要关注其他瓶
                    if (StringUtils.isBlank(sp_sydh)) {//不存在上瓶
                        if (info.SYZT.equals("0") || info.SYZT.equals("4")) {
                            //只需开始本瓶即可
                            //=======
                            setupLSBS(ea, info);
                            //=======
                            Start(ea, bp_sydh, now);
                            System.out.println("测试：test==8==");
                        } else if (info.SYZT.equals("2")) {
                            //只需结束本瓶
                            End(ea, bp_sydh, now);
                            System.out.println("测试：test==9==");
                        } else {
                            System.out.println("测试：test==13==");
                        }
                    } else {//存在上瓶，且非多路输液
                        if (info.SYZT.equals("0")) {
                            //结束上瓶，开始本瓶
                            //==========
                            setupLSBS_JP(ea, jp_sydh);
                            //======
                            Continue(ea, bp_sydh, sp_sydh, now);
                            System.out.println("测试：test==10==");
                        } else if (info.SYZT.equals("4")) {
                            System.out.println("测试：test==11==");
                            //暂停 只需开始本瓶即可
                            Start(ea, bp_sydh, now);
                        } else if (info.SYZT.equals("2")) {
                            System.out.println("测试：test==12==");
                            //结束本瓶
                            End(ea, bp_sydh, now);
                        } else {
                            System.out.println("测试：test==14==");
                        }
                    }

                }
//                if (StringUtils.isBlank(sp_sydh)) {
//                    //开始(在并行条件下或者没有上一瓶在输液)
//                    Start(ea, bp_sydh, now);
//                } else {//存在上一瓶没有结束
//                    if (bp_sydh.equals(sp_sydh) && spzt) {
//                        //如果上瓶就是本瓶 并且本瓶是暂停状态
//                        Continue(ea, bp_sydh, sp_sydh, now);
//                    } else if (bp_sydh.equals(sp_sydh)) {
//                        //结束
//                        End(ea, bp_sydh, now);
//                    } else {
//                        //接瓶
//                        if (isbx) {
//                            Start(ea, bp_sydh, now);
//                        } else {
//                            Continue(ea, bp_sydh, sp_sydh, now);
//                        }
//                    }
//                }
            }
        }
    }

    /**
     * 需求：曾经有过 2 路了（执行过多路过了） ，前台 1 路才显示，后台同步包含 1 路，否则后台不显示，也不同步 1 路
     * //
     * 采用 -1 处理一开始的 1 路，有过 2 路了（执行过多路过了），用 1 标示 1 路， 而计算等操作时候默认都采用取绝对值的方式，
     * 保证计算队列正确，也方便前台和后台的处理
     *
     * @param ea
     * @param jp_sydh
     * @throws ParseException
     */
    private void setupLSBS_JP(ExecuteArg ea, String jp_sydh) throws ParseException {
        if (TextUtils.isEmpty(jp_sydh)) {
            return;
        }
        //取得上瓶的路数
        String zyh = ea.ZYH;
        String jgid = ea.JGID;
        TransfusionInfo info = (TransfusionInfo) ea.RecordInfo;
        String startTime = DateConvert.getDateString(info.SYSJ);
        //获取上一路
//        int lsbs = getJP_SY_LSBS(startTime, zyh, jgid, jp_sydh);
        //获取上一路、协和：特殊处理 1 路
        int lsbs = getJP_SY_LSBS_XH(startTime, zyh, jgid, jp_sydh);
        System.out.println("setupLSBS_JP 路数LS:" + lsbs);
        //update
        ea.LSBS_Temp = String.valueOf(lsbs);
    }

    private void setupLSBS(ExecuteArg ea, TransfusionInfo info) throws ParseException {
        //新开始的多路
        if (info.SYZT.equals("0")) {
            //取得现在的路数 ++
            String zyh = ea.ZYH;
            String jgid = ea.JGID;
            String startTime = DateConvert.getDateString(info.SYSJ);
            //取最大值
//            int lsbs = getTodayMaxLSBS(startTime, zyh, jgid);
//            lsbs++;//累加 1
            //自定获取最小没有被执行中使用的路数
//            int lsbs = getTodaySmartLSBS(startTime, zyh, jgid);
            int lsbs = getTodaySmartLSBS_XH(startTime, zyh, jgid);
            System.out.println("setupLSBS 路数LS:" + lsbs);
            //update
            ea.LSBS_Temp = String.valueOf(lsbs);
        }
        //路数逻辑
    }

    /**
     * 取到上瓶的输液的路数
     *
     * @param startTime
     * @param zyh
     * @param jgid
     * @param jp_sydh
     * @return
     */
    @Deprecated
    private int getJP_SY_LSBS(String startTime, String zyh, String jgid, String jp_sydh) {
        int max = 0;
        String today = startTime;
        String tomorrow = null;
        try {
            tomorrow = DateUtil.dateoffDays(today, "1");
        } catch (ParseException e) {
            e.printStackTrace();
            return max;
        }
        List<PlanInfo> planInfoList = null;
        try {
//            planInfoList = planInfoManager.getPlanInfoList(zyh, today, tomorrow, "4", jgid);
            planInfoList = planInfoManager.getPlanInfoListByQrdh(jp_sydh, "4", jgid);
        } catch (SQLException e) {
            e.printStackTrace();
            return max;
        }
        for (PlanInfo planInfo : planInfoList) {
            int lsbsInt = TextUtils.isEmpty(planInfo.LSBS) ? 0 : Integer.valueOf(planInfo.LSBS);
            max = Math.max(max, lsbsInt);
        }
        return max;
    }

    private boolean getHasExecueDL(String zyh, String startTime, String jgid) {
        String today = startTime;
        String tomorrow = null;
        try {
            tomorrow = DateUtil.dateoffDays(today, "1");
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        List<PlanInfo> planInfoList = null;
        try {
            planInfoList = planInfoManager.getPlanInfoList(zyh, today, tomorrow, "4", jgid);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        //
        for (PlanInfo planInfo : planInfoList) {
            int lsbsInt = TextUtils.isEmpty(planInfo.LSBS) ? 0 : Integer.valueOf(planInfo.LSBS);
            if (lsbsInt >= 2) {
                //曾经有过 2 路
                return true;
            }
        }
        return false;
    }

    /***
     * 获取上瓶的 LSBS
     * @param startTime
     * @param zyh
     * @param jgid
     * @param jp_sydh
     * @return
     */
    private int getJP_SY_LSBS_XH(String startTime, String zyh, String jgid, String jp_sydh) {
        int max = 0;
        String today = startTime;
        String tomorrow = null;
        try {
            tomorrow = DateUtil.dateoffDays(today, "1");
        } catch (ParseException e) {
            e.printStackTrace();
            return max;
        }
        List<PlanInfo> planInfoList = null;
        try {
//            planInfoList = planInfoManager.getPlanInfoList(zyh, today, tomorrow, "4", jgid);
            planInfoList = planInfoManager.getPlanInfoListByQrdh(jp_sydh, "4", jgid);
        } catch (SQLException e) {
            e.printStackTrace();
            return max;
        }
        //
        boolean has1 = false;
        boolean has__1 = false;
        for (PlanInfo planInfo : planInfoList) {
            int lsbsInt = TextUtils.isEmpty(planInfo.LSBS) ? 0 : Integer.valueOf(planInfo.LSBS);
            max = Math.max(max, Math.abs(lsbsInt));//取绝对值计算
            //有1
            if (lsbsInt == 1) {
                has1 = true;
            }
            //有-1
            if (lsbsInt == -1) {
                has__1 = true;
            }
        }
        //特殊处理 最大值是 1 时候
        if (max == 1) {
            //有 1
            if (has1) {
                return 1;//直接返回 1
            }
            //没有 1,、有 -1
            if (has__1) {
                boolean hasExecueDL = getHasExecueDL(zyh, startTime, jgid);
                if (hasExecueDL) {
                    return 1;//有过多路了 返回 1
                } else {
                    return -1;//返回 -1
                }
            }
        }
        return max;
    }

    /**
     * 每次取最大值
     *
     * @param startTime
     * @param zyh
     * @param jgid
     * @return
     */
    @Deprecated
    private int getTodayMaxLSBS(String startTime, String zyh, String jgid) {
        int max = 0;
        String today = startTime;
        String tomorrow = null;
        try {
            tomorrow = DateUtil.dateoffDays(today, "1");
        } catch (ParseException e) {
            e.printStackTrace();
            return max;
        }
        List<PlanInfo> planInfoList = null;
        try {
            planInfoList = planInfoManager.getPlanInfoList(zyh, today, tomorrow, "4", jgid);
        } catch (SQLException e) {
            e.printStackTrace();
            return max;
        }
        for (PlanInfo planInfo : planInfoList) {
            //只考虑执行中的
            if (!"2".equals(planInfo.ZXZT)) {
                continue;
            }
            int lsbsInt = TextUtils.isEmpty(planInfo.LSBS) ? 0 : Integer.valueOf(planInfo.LSBS);
            max = Math.max(max, lsbsInt);
        }
        return max;
    }

    /**
     * 每次取最小没在执行中使用的路数
     *
     * @param startTime
     * @param zyh
     * @param jgid
     * @return
     */
    @Deprecated
    private int getTodaySmartLSBS(String startTime, String zyh, String jgid) {
        int lsbs = 0;
        String today = startTime;
        String tomorrow = null;
        try {
            tomorrow = DateUtil.dateoffDays(today, "1");
        } catch (ParseException e) {
            e.printStackTrace();
            return lsbs;
        }
        List<PlanInfo> planInfoList = null;
        try {
            planInfoList = planInfoManager.getPlanInfoList(zyh, today, tomorrow, "4", jgid);
        } catch (SQLException e) {
            e.printStackTrace();
            return lsbs;
        }
        int unit = 0;
        List<Integer> has_LS_List = new ArrayList<>();
        for (PlanInfo planInfo : planInfoList) {
            //只考虑执行中的
            if (!"2".equals(planInfo.ZXZT)) {
                continue;
            }
            int lsbsInt = TextUtils.isEmpty(planInfo.LSBS) ? 0 : Integer.valueOf(planInfo.LSBS);
            if (!has_LS_List.contains(lsbsInt)) {
                has_LS_List.add(lsbsInt);
            }
        }
        //从小到大排序
//        Collections.sort(has_LS_List);
        do {
            unit++;
        } while (has_LS_List.contains(unit));
        //当前不在这个列表中 结束
//        logger.error("路数LS:"+unit);
        return unit;
    }

    /**
     * @param startTime
     * @param zyh
     * @param jgid
     * @return
     */
    private int getTodaySmartLSBS_XH(String startTime, String zyh, String jgid) {
        int lsbs = 0;
        String today = startTime;
        String tomorrow = null;
        try {
            tomorrow = DateUtil.dateoffDays(today, "1");
        } catch (ParseException e) {
            e.printStackTrace();
            return lsbs;
        }
        List<PlanInfo> planInfoList = null;
        try {
            planInfoList = planInfoManager.getPlanInfoList(zyh, today, tomorrow, "4", jgid);
        } catch (SQLException e) {
            e.printStackTrace();
            return lsbs;
        }
        //默认 0 路
        int unit = 0;
        List<Integer> has_LS_List = new ArrayList<>();
        for (PlanInfo planInfo : planInfoList) {
            //只考虑执行中的
            if (!"2".equals(planInfo.ZXZT)) {
                continue;
            }
            int lsbsInt = TextUtils.isEmpty(planInfo.LSBS) ? 0 : Integer.valueOf(planInfo.LSBS);
            if (!has_LS_List.contains(lsbsInt)) {
                has_LS_List.add(lsbsInt);
            }
        }
        //从小到大排序
//        Collections.sort(has_LS_List);
        do {
            unit++;
//        }while (has_LS_List.contains(unit));
        } while (has_LS_List.contains(unit) || has_LS_List.contains(-unit));//list 可能存在 -1
        //当前不在这个列表中 结束
//        logger.error("路数LS:"+unit);
        //针对 1 路 特殊处理
        if (unit == 1) {
            //如果是 1 路
            boolean hasExecueDL = false;
            for (PlanInfo planInfo : planInfoList) {
                int lsbsInt = TextUtils.isEmpty(planInfo.LSBS) ? 0 : Integer.valueOf(planInfo.LSBS);
                if (lsbsInt >= 2) {
                    //曾经有过 2 路
                    hasExecueDL = true;
                    break;
                }
            }
            //已执行过多路
            if (hasExecueDL) {
                unit = 1;
            } else {
                unit = -1;
            }
        }
        return unit;
    }

    /**
     * 输液开始
     *
     * @param ea
     * @param bp_sydh
     * @param now
     */
    private void Start(ExecuteArg ea, String bp_sydh, String now)
            throws SQLException, DataAccessException {
        AdviceArg arg = new AdviceArg();
        arg.ClassName = "TransfuseDBOperate_Start";
        TransfusionInfo trans = (TransfusionInfo) ea.RecordInfo;
        String sydh = trans.SYDH;
        String checkId = null;
        Boolean doubleCheck = false;
        PlanInfo planInfo = null;
        for (PlanInfo p : ea.PlanInfoList) {
            if ("1".equals(p.SRHDBZ) && p.KSHDGH != null && !"".equals(p.KSHDGH) && "4"
                    .equals(p.GSLX)) {
                planInfo = p;
                break;
            }
        }
        if (planInfo != null) {
            checkId = planInfo.KSHDGH;
            doubleCheck = true;
        }
        TransfusionInfo tInfo = new TransfusionInfo();
        if (parameterManager.getParameterMap().get(ea.JGID).TransfusionUpdate) {
            tInfo.KSSJ = now;
            tInfo.KSGH = ea.YHID;
            tInfo.ZXDH = sydh;
            tInfo.SYZT = "2";
            tInfo.SYDH = bp_sydh;
            tInfo.JGID = ea.JGID;
            tInfo.SPBZ = "1";

            trans.SPBZ = "1";
            trans.MPBZ = "0";
            ea.RecordInfo = trans;
        }
        PlanInfo pInfo = new PlanInfo();
        pInfo.KSSJ = now;
        pInfo.KSGH = ea.YHID;
        pInfo.QRDH = sydh;
        pInfo.ZXLX = (bp_sydh == null || "".equals(bp_sydh)) ? "1" : "2";
        pInfo.ZXZT = "2";
        pInfo.ZDLX = "1";
        //add  路数逻辑
        pInfo.LSBS = ea.LSBS_Temp;
        pInfo.JGID = ea.JGID;

        arg.RecordInfo = tInfo;
        arg.PlanInfoList.add(pInfo);
        ea.AdviceArgList.add(arg);

        // 更新医嘱本
        UpdateYZB(ea, bp_sydh, ea.YHID, now, checkId, doubleCheck, arg.ClassName);
        // 更新病区医嘱
        updateBQYZ(ea, now, checkId, doubleCheck, arg.ClassName);
        // 更新病区医嘱计划
        UpdateBQYZJH(ea, now, true, arg.ClassName);
        // 计费 .......
    }

    /**
     * 输液接瓶
     *
     * @param ea
     * @param bp_sydh
     * @param sp_sydh
     * @param now
     */
    private void Continue(ExecuteArg ea, String bp_sydh, String sp_sydh, String now)
            throws SQLException, DataAccessException, ParseException {
        AdviceArg arg = new AdviceArg();
        arg.ClassName = "TransfuseDBOperate_Continue";
        TransfusionInfo trans = (TransfusionInfo) ea.RecordInfo;
        String sydh = trans.SYDH;
        String checkId = null;
        Boolean doubleCheck = false;

        PlanInfo planInfo = null;
        for (PlanInfo p : ea.PlanInfoList) {
            if ("1".equals(p.SRHDBZ) && p.KSHDGH != null && !"".equals(p.KSHDGH) && "4"
                    .equals(p.GSLX)) {
                planInfo = p;
                break;
            }
        }
        if (planInfo != null) {
            checkId = planInfo.KSHDGH;
            doubleCheck = true;
        }

        TransfusionInfo tInfo = new TransfusionInfo();
        if (parameterManager.getParameterMap().get(ea.JGID).TransfusionUpdate) {
            // 本瓶 写开始时间 打执行中标志 首瓶标志=0 末瓶标志=0  transfuseInfoManager.editTransfusion(tInfo)
            tInfo.KSSJ = now;
            tInfo.KSGH = ea.YHID;
            tInfo.ZXDH = bp_sydh;
            tInfo.SYZT = "2";
            tInfo.SYDH = bp_sydh;
            tInfo.JGID = ea.JGID;

            trans.SPBZ = "0";
            trans.MPBZ = "0";
            ea.RecordInfo = trans;
        }
        // 本组计划更新为执行中  planInfoManager.updatePlanInfoForSYDByTime(pInfo) / planInfoManager.updatePlanInfoForSYD(pInfo)
        PlanInfo pInfo = new PlanInfo();
        pInfo.KSSJ = now;
        pInfo.KSGH = ea.YHID;
        pInfo.ZXLX = "2";
        pInfo.QRDH = bp_sydh;
        pInfo.ZXZT = "2";
        pInfo.ZDLX = "1";
        pInfo.JGID = ea.JGID;
        //add
        pInfo.LSBS = ea.LSBS_Temp;
        /* 该处需判断
        ((Parameter) parameterManager.ParameterMap.get(ea.JGID)).TransfusionJoinPlanByTime
		*/
        // 计费 ......

        // 上一瓶是否处于暂停
        Boolean ztbz = stopCheck(sp_sydh, ea.JGID);
        if (!ztbz) {
            TransfusionInfo tInfo2 = new TransfusionInfo();
            if (parameterManager.getParameterMap().get(ea.JGID).TransfusionUpdate) {
                // 上一瓶写结束时间，打完成标志  transfuseInfoManager.editTransfusion(tInfo)
                tInfo2.JSSJ = now;
                tInfo2.JSGH = ea.YHID;
                tInfo2.PJDS = String.valueOf(dropSpeedCompute(sp_sydh, ea.JGID, now));
                tInfo2.SYZT = "1";
                tInfo2.SYDH = sp_sydh;
                tInfo2.JGID = ea.JGID;
            }
            // 上组计划更新为完成  planInfoManager.updatePlanInfoForSYDByTime(pInfo) / planInfoManager.updatePlanInfoForSYD(pInfo)
            PlanInfo pInfo2 = new PlanInfo();
            pInfo2.JSGH = ea.YHID;
            pInfo2.JSSJ = now;
            pInfo2.ZXZT = "1";
            pInfo2.ZDLX = "1";
            pInfo2.QRDH = sp_sydh;
            pInfo2.JGID = ea.JGID;
            /* 该处需判断
            ((Parameter) parameterManager.ParameterMap.get(ea.JGID)).TransfusionJoinPlanByTime
			*/

            arg.TransfusionInfoList.add(tInfo2);
            arg.PlanInfoList.add(pInfo2);
        }

        arg.TransfusionInfoList.add(tInfo);
        arg.PlanInfoList.add(pInfo);
        ea.AdviceArgList.add(arg);

        // 更新医嘱本
        UpdateYZB(ea, sydh, ea.YHID, now, checkId, doubleCheck, arg.ClassName);
        // 更新病区医嘱
        updateBQYZ(ea, now, checkId, doubleCheck, arg.ClassName);
        // 更新病区医嘱计划
        UpdateBQYZJH(ea, now, false, arg.ClassName);

        if (!ztbz) {
            //取上组数据
            List<PlanInfo> list = planInfoManager.getPlanInfoListByQrdh(sp_sydh, "4", ea.JGID);
            for (PlanInfo info : list) {
                ea.ExecutResult.add(new ExecuteResult(ea, info.JHH, now, info.SJMC, info.YZMC, info.YZXH, ea.YHID, messageManager.Create("0"), "0", info.ZXZT, info.JHSJ, info.YZZH));
            }
        }
    }

    /**
     * 输液暂停
     *
     * @param ea
     * @param now
     * @return
     */
    public void Stop(ExecuteArg ea, String now) {
        if ("4".equals(ea.GSLX)) {
            TransfusionInfo info = (TransfusionInfo) ea.RecordInfo;
            AdviceArg arg = new AdviceArg();
            arg.ClassName = "TransfuseDBOperate_Stop";
            TransfusionInfo tInfo = new TransfusionInfo();
            tInfo.SYDH = info.SYDH;
            tInfo.JSSJ = now;
            tInfo.JSGH = ea.YHID;
            tInfo.JGID = ea.JGID;

            tInfo.SYDH = info.SYDH;
            tInfo.SYZT = "4";
            tInfo.JGID = ea.JGID;

            PlanInfo planInfo = new PlanInfo();
            planInfo.ZXZT = "4";
            planInfo.ZDLX = "1"; // 终端类型  1 PDA  2 PC
            planInfo.QRDH = info.SYDH;
            planInfo.JGID = ea.JGID;

            arg.RecordInfo = tInfo;
            arg.PlanInfoList.add(planInfo);
            ea.AdviceArgList.add(arg);
        }
    }

    /**
     * 强制结束
     *
     * @param ea
     * @param now
     * @return
     * @throws SQLException
     * @throws DataAccessException
     * @throws ParseException
     */
    public void ForceEnd(ExecuteArg ea, String now) throws SQLException, DataAccessException, ParseException {
        if ("4".equals(ea.GSLX)) {
            TransfusionInfo info = (TransfusionInfo) ea.RecordInfo;

            /*
            // 改步与End中重复，且参数缺少，省略
            AdviceArg arg = new AdviceArg();
            arg.ClassName = "TransfuseDBOperate_ForceEnd";
            // 结束输液暂停  transfuseInfoManager.transfuseStopEnd(tInfo.SYDH, tInfo.JSSJ, tInfo.JSGH, tInfo.JGID)
            TransfusionInfo tInfo = new TransfusionInfo();
            tInfo.SYDH = info.SYDH;
            tInfo.JSSJ = now;
            tInfo.JSGH = ea.YHID;
            tInfo.JGID = ea.JGID;
            arg.RecordInfo = tInfo;
            ea.AdviceArgList.add(arg);
            */

            // 结束输液
            End(ea, info.SYDH, now);
            if (ea.ExecutResult == null || ea.ExecutResult.isEmpty()) {
                for (PlanInfo planInfo : ea.PlanInfoList) {
                    ExecuteResult newEr = new ExecuteResult(ea, planInfo.JHH, now,
                            planInfo.SJMC, planInfo.YZMC, planInfo.YZXH, ea.YHID,
                            messageManager.Create("12"), "12", planInfo.ZXZT, planInfo.JHSJ, planInfo.YZZH);
                    ea.ExecutResult.add(newEr);
                }
            } else {
                List<String> jhhList = new ArrayList<>();
                for (ExecuteResult er : ea.ExecutResult) {
                    jhhList.add(er.JHH);
                }
                for (PlanInfo planInfo : ea.PlanInfoList) {
                    if (!jhhList.contains(planInfo.JHH)) {
                        ExecuteResult newEr = new ExecuteResult(ea, planInfo.JHH, now,
                                planInfo.SJMC, planInfo.YZMC, planInfo.YZXH, ea.YHID,
                                messageManager.Create("12"), "12", planInfo.ZXZT, planInfo.JHSJ, planInfo.YZZH);
                        ea.ExecutResult.add(newEr);
                    }
                }
            }
        }
    }

    /**
     * 输液结束
     *
     * @param ea
     * @param sydh
     * @param now
     * @return
     */
    private void End(ExecuteArg ea, String sydh, String now) throws SQLException, DataAccessException, ParseException {
        End(ea, sydh, now, false);
    }

    private void End(ExecuteArg ea, String sydh, String now, boolean isJT) throws SQLException, DataAccessException, ParseException {
        PlanInfo info = null;
        String checkId = null;
        Boolean doubleCheck = false;
        AdviceArg arg = new AdviceArg();
        arg.ClassName = "TransfuseDBOperate_End";
        TransfusionInfo trans = (TransfusionInfo) ea.RecordInfo;
        for (PlanInfo p : ea.PlanInfoList) {
            if ("1".equals(p.SRHDBZ) && p.KSHDGH != null && !"".equals(p.KSHDGH) && "4"
                    .equals(p.GSLX)) {
                info = p;
                break;
            }
        }
        if (info != null) {
            checkId = info.KSHDGH;
            doubleCheck = true;
        }
        TransfusionInfo tInfo = new TransfusionInfo();
        if (parameterManager.getParameterMap().get(ea.JGID).TransfusionUpdate) {
            tInfo.SYDH = sydh;
            tInfo.JSSJ = now;
            tInfo.JSGH = ea.YHID;
            if (isJT) {
                tInfo.KSSJ = now;
                tInfo.KSGH = ea.YHID;
            }
            tInfo.PJDS = isJT ? "0" : String.valueOf(dropSpeedCompute(sydh, ea.JGID, now));
            tInfo.SYZT = "1";
            tInfo.JGID = ea.JGID;
            tInfo.MPBZ = "1";
            ea.RecordInfo = trans;
        }
        PlanInfo pInfo = new PlanInfo();
        pInfo.QRDH = sydh;
        pInfo.ZDLX = "1";
        pInfo.ZXZT = "1";
        if (isJT) {
            pInfo.KSSJ = now;
            pInfo.KSGH = ea.YHID;
            pInfo.ZXLX = "2";
        }
        pInfo.JSSJ = now;
        pInfo.JSGH = ea.YHID;
        pInfo.JGID = ea.JGID;

        arg.RecordInfo = tInfo;
        arg.PlanInfoList.add(pInfo);
        ea.AdviceArgList.add(arg);

        // 更新医嘱本
        UpdateYZB(ea, sydh, ea.YHID, now, checkId, doubleCheck, arg.ClassName);
        // 更新病区医嘱
        updateBQYZ(ea, now, checkId, doubleCheck, arg.ClassName);
        // 更新病区医嘱计划
        UpdateBQYZJH(ea, now, false, arg.ClassName);

        for (TransfusionDetailInfo detail : trans.Details) {
            for (PlanInfo p : ea.PlanInfoList) {
                if (!StringUtils.isBlank(trans.SJBH) && !StringUtils.isBlank(p.SJBH)) {
                    if (detail.YZXH.equals(p.YZXH) && trans.SJBH.equals(p.SJBH) && trans.SYSJ.equals(p.JHSJ)) {
                        p.ZXZT = "1"; // 已执行
                    }
                } else {
                    if (detail.YZXH.equals(p.YZXH) && trans.SYSJ.equals(p.JHSJ)) {
                        p.ZXZT = "1"; // 已执行
                    }
                }
            }
        }
        // 计费 ........
    }

    /**
     * 取消结束、回到执行中
     *
     * @param ea
     * @param sydh
     * @param now
     * @throws SQLException
     * @throws DataAccessException
     */
    public void CancelEnd(ExecuteArg ea, String sydh, String now)
            throws SQLException, DataAccessException {
        PlanInfo info = null;
        String checkId = null;
        Boolean doubleCheck = false;
        AdviceArg arg = new AdviceArg();
        arg.ClassName = "TransfuseDBOperate_CancelEnd";
        TransfusionInfo trans = (TransfusionInfo) ea.RecordInfo;
        for (PlanInfo p : ea.PlanInfoList) {
            //启用双人核对&&开始核对工号不为空&&输液
            if ("1".equals(p.SRHDBZ) && p.KSHDGH != null && !"".equals(p.KSHDGH) && "4"
                    .equals(p.GSLX)) {
                info = p;
                break;
            }
        }
        if (info != null) {
            checkId = info.KSHDGH;
            doubleCheck = true;
        }
        TransfusionInfo tInfo = new TransfusionInfo();
        if (parameterManager.getParameterMap().get(ea.JGID).TransfusionUpdate) {
            tInfo.SYDH = sydh;
            tInfo.JSSJ = null;
            tInfo.JSGH = null;
            tInfo.PJDS = null;//平均滴速
            tInfo.SYZT = "2";//正在执行
            //add 2018-5-22 21:14:25
            if (ea.RecordInfo != null && ea.RecordInfo instanceof TransfusionInfo) {
                tInfo.KSSJ = ((TransfusionInfo) ea.RecordInfo).KSSJ;//
                tInfo.KSGH = ((TransfusionInfo) ea.RecordInfo).KSGH;//
                //简单处理一下
                tInfo.KSSJ = tInfo.KSSJ != null ? tInfo.KSSJ.replace(".0", "") : tInfo.KSSJ;
            }
            //add 2018-5-22 21:14:29
            tInfo.JGID = ea.JGID;
            tInfo.MPBZ = "1";// FIXME: 2018/5/19
            ea.RecordInfo = trans;
        }
        PlanInfo pInfo = new PlanInfo();
        pInfo.QRDH = sydh;
        pInfo.ZDLX = "1";
        pInfo.ZXLX = "2";//add 2018-5-22 22:16:56
        pInfo.ZXZT = "2";//正在执行
        //add 2018-5-22 21:14:25
        if (ea.RecordInfo != null && ea.RecordInfo instanceof TransfusionInfo) {
            pInfo.KSSJ = ((TransfusionInfo) ea.RecordInfo).KSSJ;//
            pInfo.KSGH = ((TransfusionInfo) ea.RecordInfo).KSGH;//
            //简单处理一下
            pInfo.KSSJ = pInfo.KSSJ != null ? pInfo.KSSJ.replace(".0", "") : pInfo.KSSJ;
        }
        //add 2018-5-22 21:14:29
        pInfo.JSSJ = null;
        pInfo.JSGH = null;
        pInfo.JGID = ea.JGID;

        arg.RecordInfo = tInfo;
        arg.PlanInfoList.add(pInfo);
        ea.AdviceArgList.add(arg);

        // 更新医嘱本
        UpdateYZBCancel(ea, sydh, ea.YHID, now, checkId, doubleCheck, arg.ClassName);
        // 更新病区医嘱
        updateBQYZCancel(ea, now, checkId, doubleCheck, arg.ClassName);
        // 更新病区医嘱计划
        UpdateBQYZJHCancel(ea, now, false, arg.ClassName);

        for (TransfusionDetailInfo detail : trans.Details) {
            for (PlanInfo p : ea.PlanInfoList) {
                if (!StringUtils.isBlank(trans.SJBH) && !StringUtils.isBlank(p.SJBH)) {
                    if (detail.YZXH.equals(p.YZXH) && trans.SJBH.equals(p.SJBH) && trans.SYSJ.equals(p.JHSJ)) {
                        p.ZXZT = "2"; // 执行中
                    }
                } else {
                    if (detail.YZXH.equals(p.YZXH) && trans.SYSJ.equals(p.JHSJ)) {
                        p.ZXZT = "2"; // 执行中
                    }
                }
            }
        }
        // 计费 ........
    }

    public void CancelStart(ExecuteArg ea, String sydh, String now)
            throws SQLException, DataAccessException {
        PlanInfo info = null;
        String checkId = null;
        Boolean doubleCheck = false;
        AdviceArg arg = new AdviceArg();
        arg.ClassName = "TransfuseDBOperate_CancelStart";
        TransfusionInfo trans = (TransfusionInfo) ea.RecordInfo;
        for (PlanInfo p : ea.PlanInfoList) {
            //启用双人核对&&开始核对工号不为空&&输液
            if ("1".equals(p.SRHDBZ) && p.KSHDGH != null && !"".equals(p.KSHDGH) && "4"
                    .equals(p.GSLX)) {
                info = p;
                break;
            }
        }
        if (info != null) {
            checkId = info.KSHDGH;
            doubleCheck = true;
        }
        TransfusionInfo tInfo = new TransfusionInfo();
        if (parameterManager.getParameterMap().get(ea.JGID).TransfusionUpdate) {
            tInfo.SYDH = sydh;
            tInfo.JSSJ = null;
            tInfo.JSGH = null;
            tInfo.PJDS = null;//平均滴速
            tInfo.SYZT = "0";//未执行
            //add 2018-5-22 21:14:25
            tInfo.KSSJ = null;
            tInfo.KSGH = null;
            //add 2018-5-22 21:14:29
            tInfo.JGID = ea.JGID;
            tInfo.MPBZ = "1";// FIXME: 2018/5/19
            ea.RecordInfo = trans;
        }
        PlanInfo pInfo = new PlanInfo();
        pInfo.QRDH = sydh;
        pInfo.ZDLX = "1";
        pInfo.ZXLX = "0";//add 2018-5-22 22:16:56
        pInfo.ZXZT = "0";//未执行
        //add 2018-5-22 21:14:25
        pInfo.KSSJ = null;
        pInfo.KSGH = null;
        //add 2018-5-22 21:14:29
        pInfo.JSSJ = null;
        pInfo.JSGH = null;
        pInfo.JGID = ea.JGID;
        //add 2018-6-1 12:12:30
        pInfo.LSBS = "";//置空字符串


        arg.RecordInfo = tInfo;
        arg.PlanInfoList.add(pInfo);
        ea.AdviceArgList.add(arg);

        // 更新医嘱本
        UpdateYZBCancel(ea, sydh, ea.YHID, now, checkId, doubleCheck, arg.ClassName);
        // 更新病区医嘱
        updateBQYZCancel(ea, now, checkId, doubleCheck, arg.ClassName);
        // 更新病区医嘱计划
        UpdateBQYZJHCancel(ea, now, false, arg.ClassName);

        for (TransfusionDetailInfo detail : trans.Details) {
            for (PlanInfo p : ea.PlanInfoList) {
                if (!StringUtils.isBlank(trans.SJBH) && !StringUtils.isBlank(p.SJBH)) {
                    if (detail.YZXH.equals(p.YZXH) && trans.SJBH.equals(p.SJBH) && trans.SYSJ.equals(p.JHSJ)) {
                        p.ZXZT = "0"; // 未执行
                    }
                } else {
                    if (detail.YZXH.equals(p.YZXH) && trans.SYSJ.equals(p.JHSJ)) {
                        p.ZXZT = "0"; // 未执行
                    }
                }
            }
        }
        // 计费 ........
    }

    /**
     * 滴速计算
     *
     * @param sydh
     * @param jgid
     * @param now
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    private Integer dropSpeedCompute(String sydh, String jgid, String now)
            throws SQLException, DataAccessException, ParseException {
        List<TransfusionInfoVoTemp> infoTempList = transfuseInfoManager.getDropSpeedInfo(sydh, jgid);
        if (infoTempList != null && !infoTempList.isEmpty()) {
            // 计算输液时间
            String kssj = infoTempList.get(0).KSSJ;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date _kssj = sdf.parse(kssj);
            Date _jssj = sdf.parse(now);
            Long diff = _jssj.getTime() - _kssj.getTime();
            Double time = (double) (diff / (1000 * 60)); // 分钟数

            Double total = 0d; // 总剂量
            Parameter parameter = parameterManager.getParameterMap().get(jgid);
            if (time > 0 && parameter.DropSpeedConversion > 0) {
                for (TransfusionInfoVoTemp temp : infoTempList) {
                    String dw = temp.JLDW;
                    String jl = temp.YCJL;
                    try {
                        if (parameter.DropSpeedUnit.contains(dw.toLowerCase()))
                            total += Double.valueOf(jl);
                    } catch (NumberFormatException ignored) {
                    }
                }
                Integer i = (int) (total / time * parameter.DropSpeedConversion);
                return i > 1000 ? 1000 : i;
            }
        }
        return 0;
    }

    /**
     * 更新医嘱本的数据准备
     *
     * @param ea
     * @param sydh
     * @param yhid
     * @param now
     * @param checkId
     * @param doubleCheck
     */
    private void UpdateYZB(ExecuteArg ea, String sydh, String yhid, String now, String checkId,
                           Boolean doubleCheck, String className) throws SQLException, DataAccessException {
        // 不更新医嘱时
        if (!parameterManager.getParameterMap().get(ea.JGID).UpdateYzb) {
            return;
        }
        TransfusionInfo trans = (TransfusionInfo) ea.RecordInfo;
        List<AdviceYzbInfo> adviceYzbInfoList = new ArrayList<>();
        if (trans != null && sydh.equals(trans.SYDH)) {
            for (TransfusionDetailInfo detail : trans.Details) {
                PlanInfo plan = null;
                for (PlanInfo planInfo : ea.PlanInfoList) {
                    if (detail.YZXH.equals(planInfo.YZXH)) {
                        plan = planInfo;
                        break;
                    }
                }
                if (plan != null) {
                    // 获取医嘱执行时间和医嘱期效
                    String yzbxh = plan.YSYZBH;
                    AdviceYzbInfo adviceYzbInfo = adviceYzbInfoManager.getZxsjOfAdviceYzb(yzbxh);
                    if (adviceYzbInfo != null) {
                        // 医嘱本执行时间
                        String hszxsj = adviceYzbInfo.HSZXSJ;
                        String yzqx = adviceYzbInfo.YZQX;

                        // 医嘱本更新内容
                        if (!"2".equals(yzqx) || hszxsj == null) {
                            AdviceYzbInfo aInfo = new AdviceYzbInfo();
                            aInfo.HSZXSJ = now;
                            aInfo.HSZXGH = yhid;
                            aInfo.YZXH = yzbxh;
                            if (doubleCheck) {
                                aInfo.HSZXGH2 = checkId;
                            }
                            adviceYzbInfoList.add(aInfo);
                        }
                    }
                }
            }
        }
        AdviceArg arg = null;
        if (ea.AdviceArgList != null && !ea.AdviceArgList.isEmpty()) {
            for (AdviceArg aa : ea.AdviceArgList) {
                if (className.equals(aa.ClassName)) {
                    arg = aa;
                    break;
                }
            }
        }
        if (arg != null) {
            arg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
        } else {
            arg = new AdviceArg();
            arg.ClassName = className;
            arg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
            ea.AdviceArgList.add(arg);
        }
    }

    private void UpdateYZBCancel(ExecuteArg ea, String sydh, String yhid, String now, String checkId,
                                 Boolean doubleCheck, String className) throws SQLException, DataAccessException {
        // 不更新医嘱时
        if (!parameterManager.getParameterMap().get(ea.JGID).UpdateYzb) {
            return;
        }
        TransfusionInfo trans = (TransfusionInfo) ea.RecordInfo;
        List<AdviceYzbInfo> adviceYzbInfoList = new ArrayList<>();
        if (trans != null && sydh.equals(trans.SYDH)) {
            for (TransfusionDetailInfo detail : trans.Details) {
                PlanInfo plan = null;
                for (PlanInfo planInfo : ea.PlanInfoList) {
                    if (detail.YZXH.equals(planInfo.YZXH)) {
                        plan = planInfo;
                        break;
                    }
                }
                if (plan != null) {
                    // 获取医嘱执行时间和医嘱期效
                    String yzbxh = plan.YSYZBH;
                    AdviceYzbInfo adviceYzbInfo = adviceYzbInfoManager.getZxsjOfAdviceYzb(yzbxh);
                    if (adviceYzbInfo != null) {
                        // 医嘱本执行时间
                        String hszxsj = adviceYzbInfo.HSZXSJ;
                        String yzqx = adviceYzbInfo.YZQX;

                        // 医嘱本更新内容
                        if (!"2".equals(yzqx) || hszxsj == null) {
                            AdviceYzbInfo aInfo = new AdviceYzbInfo();
                            aInfo.HSZXSJ = null;
                            aInfo.HSZXGH = null;
                            aInfo.YZXH = yzbxh;// FIXME: 2018/5/19
                            if (doubleCheck) {
                                aInfo.HSZXGH2 = checkId;// FIXME: 2018/5/19
                            }
                            adviceYzbInfoList.add(aInfo);
                        }
                    }
                }
            }
        }
        AdviceArg arg = null;
        if (ea.AdviceArgList != null && !ea.AdviceArgList.isEmpty()) {
            for (AdviceArg aa : ea.AdviceArgList) {
                if (className.equals(aa.ClassName)) {
                    arg = aa;
                    break;
                }
            }
        }
        if (arg != null) {
            arg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
        } else {
            arg = new AdviceArg();
            arg.ClassName = className;
            arg.AdviceYzbInfoList.addAll(adviceYzbInfoList);
            ea.AdviceArgList.add(arg);
        }
    }

    /**
     * 更新病区医嘱的数据准备
     *
     * @param ea
     * @param now
     * @param checkId
     * @param doubleCheck
     * @param className
     */
    private void updateBQYZ(ExecuteArg ea, String now, String checkId,
                            Boolean doubleCheck, String className) {
        if (!parameterManager.getParameterMap().get(ea.JGID).UpdateBqyz) {
            return;
        }
        List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
        for (PlanInfo plan : ea.PlanInfoList) {
            AdviceBqyzInfo info = new AdviceBqyzInfo();
            info.HSZXSJ = now;
            info.HSZXGH = ea.YHID;
            info.YZXH = plan.YZXH;
            if (doubleCheck) {
                info.HSZXGH2 = checkId;
            }
            adviceBqyzInfoList.add(info);
        }
        AdviceArg arg = null;
        if (ea.AdviceArgList != null && !ea.AdviceArgList.isEmpty()) {
            for (AdviceArg aa : ea.AdviceArgList) {
                if (className.equals(aa.ClassName)) {
                    arg = aa;
                    break;
                }
            }
        }
        if (arg != null) {
            arg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
        } else {
            arg = new AdviceArg();
            arg.ClassName = className;
            arg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
            ea.AdviceArgList.add(arg);
        }
    }

    private void updateBQYZCancel(ExecuteArg ea, String now, String checkId,
                                  Boolean doubleCheck, String className) {
        if (!parameterManager.getParameterMap().get(ea.JGID).UpdateBqyz) {
            return;
        }
        List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
        for (PlanInfo plan : ea.PlanInfoList) {
            AdviceBqyzInfo info = new AdviceBqyzInfo();
            info.HSZXSJ = null;
            info.HSZXGH = null;
            info.YZXH = plan.YZXH;// FIXME: 2018/5/19
            if (doubleCheck) {
                info.HSZXGH2 = checkId;// FIXME: 2018/5/19
            }
            adviceBqyzInfoList.add(info);
        }
        AdviceArg arg = null;
        if (ea.AdviceArgList != null && !ea.AdviceArgList.isEmpty()) {
            for (AdviceArg aa : ea.AdviceArgList) {
                if (className.equals(aa.ClassName)) {
                    arg = aa;
                    break;
                }
            }
        }
        if (arg != null) {
            arg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
        } else {
            arg = new AdviceArg();
            arg.ClassName = className;
            arg.AdviceBqyzInfoList.addAll(adviceBqyzInfoList);
            ea.AdviceArgList.add(arg);
        }
    }

    /**
     * 更新病区医嘱计划的数据准备
     *
     * @param ea
     * @param now
     * @param isStart
     * @throws SQLException
     * @throws DataAccessException
     */
    private void UpdateBQYZJH(ExecuteArg ea, String now, boolean isStart, String className)
            throws SQLException, DataAccessException {

        // 不更新病区医嘱计划
        if (!parameterManager.getParameterMap().get(ea.JGID).UpdateBQyzjh || parameterManager.getParameterMap().get(ea.JGID).Integrated.equals("2")) {
            return;
        }
        // 通过JHH获取GLJHH
        List<String> jhhList = new ArrayList<>();
        for (PlanInfo p : ea.PlanInfoList) {
            jhhList.add(p.JHH);
        }
        if (jhhList.isEmpty()) {
            jhhList.add("0");
        }
        List<String> gljhhList = planInfoManager.getGLJHHByJHH(jhhList);//todo 是否有必要再获取

        List<PlanInfo> BQPlanList = new ArrayList<>();
        if (gljhhList != null && !gljhhList.isEmpty()) {
            for (String gljhh : gljhhList) {
                PlanInfo planInfo = new PlanInfo();
                planInfo.GLJHH = gljhh;
//                planInfo.JSGH = ea.YHID;
                // 开始执行判断
                if (isStart) {
                    planInfo.ZXZT = "2";
                    planInfo.KSSJ = now;
                    planInfo.KSGH = ea.YHID;
                } else {
                    planInfo.ZXZT = "1";
                    planInfo.JSSJ = now;
                    planInfo.JSGH = ea.YHID;
                }
                BQPlanList.add(planInfo);
            }
        }
        AdviceArg arg = null;
        if (ea.AdviceArgList != null && !ea.AdviceArgList.isEmpty()) {
            for (AdviceArg aa : ea.AdviceArgList) {
                if (className.equals(aa.ClassName)) {
                    arg = aa;
                    break;
                }
            }
        }
        if (arg != null) {
            arg.BQPlanInfoList.addAll(BQPlanList);
        } else {
            arg = new AdviceArg();
            arg.ClassName = className;
            arg.BQPlanInfoList.addAll(BQPlanList);
            ea.AdviceArgList.add(arg);
        }
    }

    private void UpdateBQYZJHCancel(ExecuteArg ea, String now, boolean isCancelEnd2Ing, String className)
            throws SQLException, DataAccessException {

        // 不更新病区医嘱计划
        if (!parameterManager.getParameterMap().get(ea.JGID).UpdateBQyzjh || parameterManager.getParameterMap().get(ea.JGID).Integrated.equals("2")) {
            return;
        }
        // 通过JHH获取GLJHH
        List<String> jhhList = new ArrayList<>();
        for (PlanInfo p : ea.PlanInfoList) {
            jhhList.add(p.JHH);
        }
        if (jhhList.isEmpty()) {
            jhhList.add("0");
        }
        List<String> gljhhList = planInfoManager.getGLJHHByJHH(jhhList);//todo 是否有必要再获取

        List<PlanInfo> BQPlanList = new ArrayList<>();
        if (gljhhList != null && !gljhhList.isEmpty()) {
            for (String gljhh : gljhhList) {
                PlanInfo planInfo = new PlanInfo();
                planInfo.GLJHH = gljhh;// FIXME: 2018/5/19
                String kssj = null;
                String ksgh = null;
                if (isCancelEnd2Ing) {
                    for (PlanInfo info : ea.PlanInfoList) {
                        if (gljhh.equals(info.GLJHH)) {
                            ksgh = info.KSGH;
                            //简单处理一下
                            kssj = info.KSSJ != null ? info.KSSJ.replace(".0", "") : info.KSSJ;
                            break;
                        }
                    }
                }
                //addd
                planInfo.KSGH = ksgh;
                planInfo.KSSJ = kssj;
                //add
                planInfo.ZXZT = isCancelEnd2Ing ? "2" : "0";//0 未执行 2 执行中
                planInfo.JSSJ = null;
                planInfo.JSGH = null;
                BQPlanList.add(planInfo);
            }
        }
        AdviceArg arg = null;
        if (ea.AdviceArgList != null && !ea.AdviceArgList.isEmpty()) {
            for (AdviceArg aa : ea.AdviceArgList) {
                if (className.equals(aa.ClassName)) {
                    arg = aa;
                    break;
                }
            }
        }
        if (arg != null) {
            arg.BQPlanInfoList.addAll(BQPlanList);
        } else {
            arg = new AdviceArg();
            arg.ClassName = className;
            arg.BQPlanInfoList.addAll(BQPlanList);
            ea.AdviceArgList.add(arg);
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

}
