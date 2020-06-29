package com.bsoft.nis.advicesplit.split;

import com.bsoft.nis.advicesplit.AdviceSplitExcutor;
import com.bsoft.nis.advicesplit.args.PatientArgs;
import com.bsoft.nis.advicesplit.args.PlanTypeArgs;
import com.bsoft.nis.advicesplit.exception.SplitException;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.advicesplit.advice.AdviceCom;
import com.bsoft.nis.domain.advicesplit.advice.PlanTime;
import com.bsoft.nis.domain.advicesplit.advice.db.AdvicePlan;
import com.bsoft.nis.domain.advicesplit.advice.db.MonitoreAdvice;
import com.bsoft.nis.domain.advicesplit.ratetime.db.ExcuteRate;
import com.bsoft.nis.domain.advicesplit.ratetime.db.ExcuteTime;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.advicesplit.support.AdviceSplitServiceSup;
import com.bsoft.nis.util.date.DateConvert;
import com.bsoft.nis.util.date.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Describtion:医嘱拆分器
 * Created: dragon
 * Date： 2016/12/8.
 */
@Component
@Scope(value = "prototype")
public class AdviceSplitControll implements AdviceSplitControllInterface{
    private Log logger = LogFactory.getLog(AdviceSplitControll.class);

    private final int maxSplitDays = 1000;  // 最大拆分天数

    AdviceSplitExcutor excutor; // 医嘱拆分执行器

    @Autowired
    IdentityService identityService; // 主键服务

    @Autowired
    DateTimeService dateTimeService; // 日期服务

    /**
     * 56010004 临时医嘱 医嘱计划重复产生
     */
    @Autowired
    AdviceSplitServiceSup adviceSplitServiceSup; // 医嘱拆分子服务

    @Override
    public void init(AdviceSplitExcutor excutor) {
        this.excutor = excutor;
    }

    /**
     * 单病人拆分
     * @param patientWrapper
     * @throws SplitException
     */
    @Override
    public void split(PatientArgs patientWrapper) throws SplitException{
        List<AdviceCom> avalibleAdvices = patientWrapper.avalibleAdvices;
        if (avalibleAdvices.size() <=0 && patientWrapper.excuteResult.delPlans.size() <=0) throw new SplitException(patientWrapper.ZYH +"-"+patientWrapper.BRXM + ":无拆分医嘱!");
        List<PlanTypeArgs> planTypeArgses = patientWrapper.planTypes;
        if (planTypeArgses.size()<=0) throw new SplitException(patientWrapper.ZYH +"-"+patientWrapper.BRXM + ":病人所在科室未定义计划类型!");

        for (PlanTypeArgs planTypeArgs:planTypeArgses){
            List<AdviceCom> advices = planTypeArgs.advices;
            if (advices.size() <=0) continue;

            for (AdviceCom adviceCom:advices){
                // 长嘱且未维护频次或频次执行时间的不执行拆分
                if (adviceCom.LSYZ.equals("0") && (adviceCom.rates.size()<=0)){
                    logger.error("住院号:"+patientWrapper.ZYH +"-姓名:"+patientWrapper.BRXM + "-病区医嘱序号：" + adviceCom.JLXH + "-" +adviceCom.YZMC +":未拆分原因：未设置"+adviceCom.SYPC+"对应执行频次");
                    continue;
                }
                if (adviceCom.LSYZ.equals("0") && (adviceCom.rates.size() <= 0 || adviceCom.rates.get(0).excuteTimes.size()<=0)){
                    logger.error("住院号:"+patientWrapper.ZYH +"-姓名:"+patientWrapper.BRXM + "-病区医嘱序号：" + adviceCom.JLXH + "-" +adviceCom.YZMC +":未拆分原因：未设置"+adviceCom.SYPC+"频次对应执行时间");
                    continue;
                }

                try {
                    split(adviceCom,planTypeArgs,patientWrapper);
                }catch (SplitException e){
                    logger.error("住院号:"+patientWrapper.ZYH +"-姓名:"+patientWrapper.BRXM + "-病区医嘱序号：" + adviceCom.JLXH + "-" +adviceCom.YZMC + ":未拆分原因：" + e.getMessage());
                    continue;
                }
            }
        }
        return ;
    }

    /**
     * 单医嘱拆分,真正拆分的方法，生成医嘱计划
     * @param adviceCom
     * @param planTypeArgs
     */
    public void split(AdviceCom adviceCom,PlanTypeArgs planTypeArgs,PatientArgs patientArgs)throws SplitException{

        try {
            // 问题描述：确定医嘱赋空操作，停嘱赋空之后会导致我们的医嘱无法拆分且导致监控表插入重复数据的主索引冲突问题
            // 解决方案：比较符合医嘱和病区医嘱的停医嘱时间，若医嘱停医嘱时间为空，复核医嘱停嘱时间不为空，将复核医嘱停嘱时间置空
            checkStopCancel(adviceCom);
            // 确定医嘱拆分开始和结束时间
            comfireAdviceSplitStartAndEndTime(adviceCom,planTypeArgs,patientArgs);
            // 生成医嘱计划时点
            buildAdvicePlanTime(adviceCom, planTypeArgs, patientArgs);
            // 生成医嘱计划
            buildAdvicePlan(adviceCom,planTypeArgs,patientArgs);
            // 生成医嘱计划
            monitorAdvicesHandler(adviceCom,planTypeArgs,patientArgs);
        }catch (SplitException e){
            throw e;
        }
    }

    // 问题描述：确定医嘱赋空操作，停嘱赋空之后会导致我们的医嘱无法拆分且导致监控表插入重复数据的主索引冲突问题
    // 解决方案：比较符合医嘱和病区医嘱的停医嘱时间，若医嘱停医嘱时间为空，复核医嘱停嘱时间不为空，将复核医嘱停嘱时间置空
    public void checkStopCancel(AdviceCom adviceCom){
        Date tzsj = adviceCom.TZSJ;
        List<MonitoreAdvice> monitoreAdvices = adviceCom.monitoreAdvices;
        if (monitoreAdvices.size() > 0){
            Date tzfhsj = monitoreAdvices.get(0).TZFHSJ;
            if (tzfhsj != null && tzsj == null){
                // 更新复核医嘱停嘱时间
                try{
                    adviceSplitServiceSup.updateMonitorAdviceStopTime(adviceCom.ZYH,String.valueOf(monitoreAdvices.get(0).YZXH));
                    adviceCom.monitoreAdvices.get(0).TZFHSJ = null;
                }catch (SQLException e){
                    logger.error("医嘱停嘱复核时间赋空失败:"+e.getMessage(),e );
                }
            }
        }
    }

    /**
     * 确定医嘱拆分起始日期
     * @param adviceCom
     * @param planTypeArgs
     */
    public void comfireAdviceSplitStartAndEndTime(AdviceCom adviceCom,PlanTypeArgs planTypeArgs,PatientArgs patientArgs) throws SplitException{
        String cfks = null,cfjs = null,jzsj = null;
        Date monitorJzsj = null;

        LocalDateTime cfksD = LocalDateTime.MIN; // 记录实际拆分开始日期
        LocalDateTime cfjsD = LocalDateTime.MIN; // 记录实际拆分结束日期
        LocalDateTime jzsjD = LocalDateTime.MIN; // 截止时间
        LocalDate ksrqD = LocalDate.MIN; // 拆分开始日期
        LocalDate jsrqD = LocalDate.MIN; // 拆分结束日期
        LocalDateTime kzsjD = LocalDateTime.MIN; // 开嘱时间
        LocalDateTime tzsjD = LocalDateTime.MIN; // 停嘱时间
        LocalDateTime monitorJzsjD = LocalDateTime.MIN;// 监控记录截止时间
        LocalTime cycleStartTime = LocalTime.MIN;     // 周期开始时间
        LocalTime cycleEndTime = LocalTime.MAX;       // 周期结束时间

        // 监控医嘱确定拆分开始和结束时间
        // 拆分过的医嘱截止时间：监控医嘱的拆分截止时间
        try{
            ksrqD = LocalDate.parse(excutor.ksrq, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            jsrqD = LocalDate.parse(excutor.jsrq, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            kzsjD = adviceCom.KZSJ.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            if (adviceCom.TZSJ != null){
                tzsjD = adviceCom.TZSJ.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }

            cycleStartTime = LocalTime.parse(planTypeArgs.ZQQS);
            cycleEndTime = LocalTime.parse(planTypeArgs.ZQJS);

            LocalDateTime trueStartDateTime = LocalDateTime.MIN ;
            LocalDateTime trueEndDateTime = LocalDateTime.MIN;
            trueStartDateTime = LocalDateTime.of(ksrqD,cycleStartTime);
            trueEndDateTime = LocalDateTime.of(jsrqD,cycleEndTime);

            // 一、监控医嘱和计划类型拆分周期，确定拆分开始和结束时间
            if (adviceCom.monitoreAdvices.size() >0) {
                monitorJzsj = adviceCom.monitoreAdvices.get(0).JZSJ;
                if (monitorJzsjD != null)
                    jzsjD = DateConvert.toLocalDateTime(monitorJzsj);
            }
            // 监控医嘱没有截止时间，从医嘱开嘱时间开始拆分
            // 确定拆分开始时间
            if (jzsjD.isEqual(LocalDateTime.MIN)){
                cfksD = kzsjD;
            }
            if (jzsjD.isAfter(LocalDateTime.MIN) && jzsjD.isBefore(trueStartDateTime)){
                cfksD = jzsjD;
            }
            if (jzsjD.isEqual(trueStartDateTime))
                cfksD = trueStartDateTime;

            // 如果监控医嘱表中截止日期 大于等于 本次请求的拆分结束时间 不再拆分
            //if (jzsjD.isAfter(trueEndDateTime) || jzsjD.isEqual(trueEndDateTime)) return;
            if (!cfksD.isEqual(LocalDateTime.MIN)){
                adviceCom.cfks = Date.from(cfksD.atZone(ZoneId.systemDefault()).toInstant());
            }

            // 拆分开始为null或者min时，不再拆分
            //if (cfksD == null) throw new SplitException("本条医嘱没有拆分开始日期，不拆分!");
            //if (cfksD.isEqual(LocalDateTime.MIN)) throw new SplitException("本条医嘱没有拆分开始日期，不拆分!");

            // 确定拆分结束时间
            if (!cfksD.isEqual(LocalDateTime.MIN)){
                if (tzsjD.isEqual(LocalDateTime.MIN)){
                    cfjsD = trueEndDateTime;
                }else{
                    if (tzsjD.isBefore(trueEndDateTime) || tzsjD.isEqual(trueEndDateTime))
                        cfjsD = tzsjD;
                    else
                        cfjsD = trueEndDateTime;
                }
                adviceCom.cfjs = Date.from(cfjsD.atZone(ZoneId.systemDefault()).toInstant());
            }

            // 二、监控医嘱变动，确定拆分开始和结束时间
            changeAdiveComfireSplitStartAndEndTime(adviceCom, planTypeArgs, patientArgs, trueStartDateTime, trueEndDateTime,jzsjD,tzsjD);
            // 三、开嘱当天，确定拆分开始和结束时间
            openAdviceComfireSplitStartAndEndTime(adviceCom,planTypeArgs,patientArgs,kzsjD);
            // 四、停嘱当天，确定拆分开始和结束时间
            stopAdviceComfireSplitStartAndEndTime(adviceCom,planTypeArgs,patientArgs,tzsjD,jsrqD);

        }catch (Exception e){
            throw new SplitException(e.getMessage());
        }
    }

    /**
     * 变动医嘱确定开始和结束时间
     * @param adviceCom
     * @param planTypeArgs
     * @throws SplitException
     */
    public void changeAdiveComfireSplitStartAndEndTime(AdviceCom adviceCom,PlanTypeArgs planTypeArgs,PatientArgs patientArgs,LocalDateTime trueStartDateTime,LocalDateTime trueEndDateTime,
                                                       LocalDateTime jzsjDateTime,LocalDateTime tzsjDateTime) throws SplitException{
        if (adviceCom.monitoreAdvices.size() <=0) return ;
        LocalDateTime cfksD = LocalDateTime.MIN;
        LocalDateTime fhsjD = LocalDateTime.MIN; // 复核时间
        LocalDateTime tzfhsjD = LocalDateTime.MIN;// 停嘱复核时间
        LocalDateTime monitorFhsjD = LocalDateTime.MIN;// 监控记录复核时间
        LocalDateTime monitorTzfhsjD = LocalDateTime.MIN; // 监控记录停嘱复核时间

        // 判断是否是存在医嘱变动情况：监控医嘱表复核时间和现医嘱表复核时间是否相等/停嘱复核时间和现医嘱表停嘱复核时间是否相等
        if ( adviceCom.FHSJ != null)
            fhsjD = DateConvert.toLocalDateTime(adviceCom.FHSJ);

        if (adviceCom.monitoreAdvices.get(0).FHSJ != null)
            monitorFhsjD = DateConvert.toLocalDateTime(adviceCom.monitoreAdvices.get(0).FHSJ);

        if (adviceCom.TZFHSJ != null)
            tzfhsjD = DateConvert.toLocalDateTime(adviceCom.TZFHSJ);

        if (adviceCom.monitoreAdvices.get(0).TZFHSJ != null)
            monitorTzfhsjD = DateConvert.toLocalDateTime(adviceCom.monitoreAdvices.get(0).TZFHSJ);

        if (fhsjD.toLocalTime().equals(monitorFhsjD.toLocalTime())) {
            if (tzfhsjD.isEqual(LocalDateTime.MIN) && monitorTzfhsjD.isEqual(LocalDateTime.MIN)){
                return ;
            }else{
                if (!tzfhsjD.isEqual(LocalDateTime.MIN) && !monitorTzfhsjD.isEqual(LocalDateTime.MIN)){
                    if (tzfhsjD.toLocalTime().equals(monitorTzfhsjD.toLocalTime())){
                        return ;
                    }
                }
            }
        }

        // 当医嘱变动，重新确定医嘱拆分的开始和结束日期
        // 1.需拆分开始日期(2017-1-22 9:00) < 截止日期:监控医嘱表截止日期 (2017-1-23 9:00)
        if (trueStartDateTime.isBefore(jzsjDateTime)){
            adviceCom.cfks = DateConvert.toDate(trueStartDateTime);
            cfksD = trueStartDateTime;
        }
        else{
            adviceCom.cfks = DateConvert.toDate(jzsjDateTime);
            cfksD = jzsjDateTime;
        }

        // 拆分开始日期<停嘱时间
        if(!tzsjDateTime.equals(LocalDateTime.MIN)) {
            if (cfksD.isBefore(tzsjDateTime)) {
                adviceCom.cfks = adviceCom.cfks;
            } else {
                adviceCom.cfks = adviceCom.TZSJ;
            }

            adviceCom.cfjs = adviceCom.TZSJ;
        }

        /**
         * 56010005 医嘱拆分相关BUG修复
         * 不应该把拆分开始之后的计划全部删除，会导致部分数据没有产生
         */
        // 删除开分拆开始时间之后的所有已产生的计划
        /*AdvicePlan plan = new AdvicePlan();
        plan.YZXH = adviceCom.JLXH;
        plan.JGID = excutor.jgid;
        // 拆分开始日期，不是日期时间
        plan.JHSJ = LocalDateTime.of(DateConvert.toLocalDateTime(adviceCom.cfks).toLocalDate(),LocalTime.parse("00:00:00")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        patientArgs.excuteResult.delPlansByYzxh.add(plan);*/
    }

    /**
     * 开嘱当天拆分开始时间和结束时间处理
     * @param adviceCom
     * @param planTypeArgs
     * @param patientArgs
     */
    public void openAdviceComfireSplitStartAndEndTime(AdviceCom adviceCom,PlanTypeArgs planTypeArgs,PatientArgs patientArgs,LocalDateTime kzsjD) throws SplitException{
        LocalDateTime cfksNew = LocalDateTime.MIN;
        LocalTime timePoint = LocalTime.MIN;

        if (adviceCom.cfks == null || adviceCom.cfjs == null) return ;
        LocalDate kzsjDate = DateConvert.toLocalDate(kzsjD); // 开嘱日期（不包含时间）
        LocalDateTime cfksD = DateConvert.toLocalDateTime(adviceCom.cfks);  // 拆分日期时间
        if (!DateConvert.toLocalDate(cfksD).isEqual(DateConvert.toLocalDate(kzsjD)) || !adviceCom.LSYZ.equals("0")) return ;

        Collections.sort(adviceCom.rates.get(0).excuteTimes);
        Integer srgz = planTypeArgs.SRGZ;
        try{
            //1.开嘱时间(根据开嘱时间及频次时间匹配拆分)
            //2.首日次数(根据首日次数确定首日的拆分次数)
            //3.首日全部
            switch (srgz){
                case 1:
                    cfksNew =kzsjD;
                    break;
                case 2:
                    if (!adviceCom.SRCS.equals("0")){
                        if (adviceCom.rates.get(0).excuteTimes.size() - Integer.parseInt(adviceCom.SRCS) < 0) throw new SplitException("当前医嘱的首日次数超过执行频次中的对应执行次数!");
                        timePoint = LocalTime.parse(adviceCom.rates.get(0).excuteTimes.get(Integer.parseInt(adviceCom.SRCS)-1).KSSJ);
                        //timePoint = LocalTime.parse(planTypeArgs.getRates().get(0).excuteTimes.get(planTypeArgs.getRates().get(0).excuteTimes.size() - Integer.parseInt(adviceCom.SRCS)).KSSJ);
                        cfksNew=LocalDateTime.of(kzsjDate,timePoint);
                    }else
                        cfksNew=LocalDateTime.of(kzsjDate.plusDays(1),LocalTime.parse("00:00:00"));
                    break;
                case 3:
                    timePoint = LocalTime.parse(adviceCom.rates.get(0).excuteTimes.get(adviceCom.rates.get(0).excuteTimes.size()-1).KSSJ);
                    cfksNew = LocalDateTime.of(kzsjDate,timePoint);
                    break;
            }
            if (!cfksNew.isEqual(LocalDateTime.MIN)){
                adviceCom.cfks = DateConvert.toDate(cfksNew);
            }

        }catch (Exception e){
            throw new SplitException("开嘱当天拆分开始和结束时间确定处理失败:"+e.getMessage());
        }
    }

    /**
     * 停嘱当天拆分开始时间和结束时间处理
     * @param adviceCom
     * @param planTypeArgs
     * @param patientArgs
     */
    public void stopAdviceComfireSplitStartAndEndTime(AdviceCom adviceCom,PlanTypeArgs planTypeArgs,PatientArgs patientArgs,LocalDateTime tzsjDT,LocalDate cfjssjD)throws SplitException{
        LocalDateTime endLimit = LocalDateTime.MIN;
        if (adviceCom.cfks == null || adviceCom.cfjs == null) return ;
        try{
            LocalTime endTimePoint = LocalTime.parse(planTypeArgs.TZJZ);
            LocalDate tzsjD = DateConvert.toLocalDate(tzsjDT);

            if (!tzsjDT.isEqual(LocalDateTime.MIN) &&
                    DateConvert.toLocalDate(DateConvert.toLocalDateTime(adviceCom.cfjs)).isEqual(tzsjD) &&
                    adviceCom.LSYZ.equals("0")){
                if (tzsjDT.isBefore(LocalDateTime.of(cfjssjD, LocalTime.parse("00:00:00"))))
                    endLimit = tzsjDT;
                else
                    endLimit = LocalDateTime.of(cfjssjD,LocalTime.parse("00:00:00"));

                // 修改拆分结束时间
                if (!endTimePoint.equals(LocalTime.parse("00:00:00"))){
                    endLimit = LocalDateTime.of(DateConvert.toLocalDate(endLimit),endTimePoint);
                    adviceCom.cfjs = DateConvert.toDate(endLimit);
                }

                /**
                 * 56010005 医嘱拆分相关BUG修复
                 * 当天停嘱，若之前的医嘱未拆分，会导致之前的未拆分的医嘱不拆分
                 * 注释一下语句
                 */
                // 变动医嘱时，变动医嘱拆分开始以后的计划全删除，停嘱当天的医嘱需重新产生,需重新定义拆分开始日期
                //adviceCom.cfks = DateConvert.toDate(LocalDateTime.of(endLimit.toLocalDate(), LocalTime.parse("00:00:00")));

                /**
                 * 56010005 医嘱拆分相关BUG修复
                 * 删除停嘱结束以后所有已产生的计划
                 */
                AdvicePlan plan = new AdvicePlan();
                plan.ZYH = adviceCom.ZYH;
                plan.YZXH = adviceCom.JLXH;
                plan.JGID = excutor.jgid;
                plan.JHSJ = DateConvert.toLocalDateTime(adviceCom.cfjs).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                patientArgs.excuteResult.delPlansByYzxh.add(plan);

            }
        }catch (Exception e){
            throw new SplitException("停嘱当天拆分开始和结束时间确定处理失败:"+e.getMessage());
        }
    }

    /**
     * 生成医嘱计划
     * @param adviceCom
     * @param planTypeArgs
     */
    public void buildAdvicePlanTime(AdviceCom adviceCom,PlanTypeArgs planTypeArgs,PatientArgs patientArgs) throws SplitException{
        List<PlanTime> planTimes = new ArrayList<>();  // 计划时点
        List<ExcuteRate> rates = new ArrayList<>();
        Integer splitDays = 0 ;  // 拆分天数
        LocalDate tzsjD = LocalDate.MIN;
        LocalDate kzsjD = LocalDate.MIN;
        LocalDateTime tzsjDT = LocalDateTime.MIN;
        LocalDateTime kzsjDT = LocalDateTime.MIN;

        try{
            if (adviceCom.cfks == null) throw new SplitException("该医嘱没有拆分开始日期,已经拆分的医嘱没有开始日期!");
            if (adviceCom.cfjs == null) throw new SplitException("该遗嘱没有拆分结束日期吗，已经拆分的医嘱没有结束日期!");
            LocalDateTime cfksDT = DateConvert.toLocalDateTime(adviceCom.cfks);
            LocalDateTime cfjsDT = DateConvert.toLocalDateTime(adviceCom.cfjs);
            LocalDate cfksD = DateConvert.toLocalDate(cfksDT);
            LocalDate cfjsD = DateConvert.toLocalDate(cfjsDT);

            kzsjDT = DateConvert.toLocalDateTime(adviceCom.KZSJ);
            kzsjD = DateConvert.toLocalDate(kzsjDT);
            if (adviceCom.TZSJ !=null){
                tzsjDT = DateConvert.toLocalDateTime(adviceCom.TZSJ);
                tzsjD = DateConvert.toLocalDate(tzsjDT);

            }
            LocalTime cycleEndTimePoint = LocalTime.parse(planTypeArgs.ZQJS);
            LocalTime cycleStartTimePoint = LocalTime.parse(planTypeArgs.ZQQS);
            splitDays = Integer.valueOf(String.valueOf(DateUtil.between(cfksD,cfjsD)));

            //1.周期不是00:00 到 00:00
            //2.拆分结束时间和停嘱时间相同的
            if (cycleEndTimePoint.getHour() > 0 || (!tzsjD.isEqual(LocalDate.MIN) && tzsjD.isEqual(cfjsD)))
                splitDays++;
            if (cfksD.isEqual(LocalDate.MIN))
                throw new SplitException("拆分开始日期太长，请检查拆分开始日期!");

            // 一.长期医嘱拆分
            if (adviceCom.LSYZ.equals("0")){
                if (adviceCom.rates.size()<=0)
                    throw new SplitException("当前医嘱对应的执行频次未维护!");
                if (adviceCom.rates.get(0).excuteTimes.size()<=0)
                    throw new SplitException("当前医嘱对应的执行时间未维护!");
                List<ExcuteTime> timePoints = adviceCom.rates.get(0).excuteTimes;

                rates = adviceCom.rates;
                for (int i = (splitDays-1);i>=0;i--){
                    // 控制最长拆分天数，目前支持拆分十天内的，超过十天的，拆分最近十天的数据
                    if ((splitDays-i+1) > maxSplitDays) continue;
                    LocalDate tempDate = cfksD.plusDays(i);
                    int yzjgts = Integer.parseInt(String.valueOf(DateUtil.between(kzsjD, tempDate))); // 间隔天数
                    int pyl = adviceCom.rates.get(0).excuteTimes.get(0).ZXZQ; // 偏移量
                    int ksWeek = kzsjD.getDayOfWeek().getValue(); // 周几

                    ExcuteRate execRate = adviceCom.rates.get(0); //新增频次对象引用

                    for (ExcuteTime excuteTime:timePoints){
                        int zqrq = 0;
                        LocalTime timepoint = LocalTime.parse(excuteTime.KSSJ);
                        /*
                          按天发药，一发就是一天的药
                          安顿发药，一天的药可分成几顿发
                         */
                        if (excuteTime.ATFY == 1){  // 按天发药，相对于按顿发药
                            LocalDateTime jhsj = LocalDateTime.of(tempDate,timepoint);
                            //if (excuteTime.ZXZQ == 1){  // 执行周期为1，天天用药的情况
                            if(execRate.PCZQ == 1){
                                if ((jhsj.isAfter(cfksDT) || jhsj.isEqual(cfksDT)) && jhsj.isBefore(cfjsDT)){
                                    if (timepoint.isAfter(cycleEndTimePoint) || timepoint.equals(cycleEndTimePoint))
                                        zqrq = 0;
                                    else
                                        zqrq = -1;

                                    PlanTime planTime = new PlanTime();
                                    planTime.JHSJ = jhsj.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                    planTime.JHRQ = jhsj.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    planTime.SJBH = String.valueOf(excuteTime.SJBH);
                                    planTime.SJMC = excuteTime.SJMC;
                                    planTime.ZQRQ = zqrq;
                                    adviceCom.planTimes.add(planTime);
                                }
                            }else{ //当天吃一次，后隔执行周期（enr_zxsj.zxzq)一次。
                                //增加BIW类型拆分
                                //这里会有个BUG如果是星期天的话就没办法很好的区分
                                //星期天ksWeek = 0;
                                //这个可考虑 1,4和4,1   2,5和5,2   3,6和6,3
                                Boolean needSplit = false;
                                if (rates.get(0).PCBM.toLowerCase().equals("biw")){
                                    needSplit = Math.abs(ksWeek - Integer.valueOf(tempDate.getDayOfWeek().getValue())) == Math.abs(Integer.valueOf(excuteTime.ZXZQ - pyl));
                                    if (tempDate.getDayOfWeek().getValue() == 7)
                                        needSplit = false;

                                    //以下为湘潭中心医院biw拆分规则
                                    //biw,一四、二五、三六、四一、五二、六三、日四成对
                                    //获取当前日期是星期几(星期日=7)
                                    //无论星期日是返回0或者7，数组biws都能处理
                                    //int []biws = new int[]{4,4,5,6,1,2,3,4};
                                    //int dqWeek = tempDate.getDayOfWeek().getValue();
                                    //if(ksWeek == dqWeek || biws[ksWeek] == dqWeek) needSplit = true;
                                }else{
                                    //needSplit = yzjgts%Integer.valueOf(excuteTime.ZXZQ) == Math.abs(excuteTime.ZXZQ-pyl);
                                    //needSplit = ((yzjgts % execRate.PCZQ) == Math.abs(execRate.PCZQ - pyl));//新增
                                    needSplit = ((yzjgts % execRate.PCZQ) == Math.abs(excuteTime.ZXZQ - pyl));//新增(修改PCZQ<>1如tiw,biw,qod等从第二天开始拆分Bug)
                                }

                                if (needSplit){
                                    LocalDateTime jhsjDt = LocalDateTime.of(tempDate,timepoint);
                                    LocalDate jhsjD = DateConvert.toLocalDate(jhsjDt);
                                    if (jhsjD.isEqual(kzsjD) && jhsjDt.isBefore(cfjsDT)){
                                        if (jhsjDt.isBefore(cfksDT)){
                                            PlanTime planTime = new PlanTime();
                                            planTime.JHSJ = kzsjD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                            planTime.JHRQ = kzsjD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                            planTime.SJBH = String.valueOf(excuteTime.SJBH);
                                            planTime.SJMC = excuteTime.SJMC;
                                            planTime.ZQRQ = 0;
                                            adviceCom.planTimes.add(planTime);
                                        }else{
                                            PlanTime planTime = new PlanTime();
                                            planTime.JHSJ = jhsjDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                            planTime.JHRQ = jhsjDt.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                            planTime.SJBH = String.valueOf(excuteTime.SJBH);
                                            planTime.SJMC = excuteTime.SJMC;
                                            planTime.ZQRQ = 0;
                                            adviceCom.planTimes.add(planTime);
                                        }
                                    }else if (jhsjD.isAfter(kzsjD) && jhsjDt.isBefore(cfjsDT)){
                                        PlanTime planTime = new PlanTime();
                                        planTime.JHSJ = jhsjDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                        planTime.JHRQ = jhsjDt.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                        planTime.SJBH = String.valueOf(excuteTime.SJBH);
                                        planTime.SJMC = excuteTime.SJMC;
                                        planTime.ZQRQ = 0;
                                        adviceCom.planTimes.add(planTime);
                                    }
                                }
                            }
                        }else{
                            LocalDateTime jhsjDT2 = LocalDateTime.of(tempDate,timepoint);
                            if ((jhsjDT2.isAfter(cfksDT) || jhsjDT2.isEqual(cfksDT)) && jhsjDT2.isBefore(cfjsDT)){
                                PlanTime planTime = new PlanTime();
                                planTime.JHSJ = jhsjDT2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                planTime.JHRQ = jhsjDT2.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                planTime.SJBH = String.valueOf(excuteTime.SJBH);
                                planTime.SJMC = excuteTime.SJMC;
                                planTime.ZQRQ = 0;
                                adviceCom.planTimes.add(planTime);
                            }

                        }
                    }
                }

                // 二.临时医嘱拆分
            }else{
                LocalDateTime jhsjDT3 = LocalDateTime.MIN;
                LocalDate ksrqD = LocalDate.parse(excutor.ksrq);
                LocalDate jsrqD = LocalDate.parse(excutor.jsrq);
                LocalDateTime ksrqDT = LocalDateTime.of(ksrqD, LocalTime.parse("00:00:00"));
                LocalDateTime jsrqDT = LocalDateTime.of(jsrqD, LocalTime.parse("00:00:00"));
                if (adviceCom.rates.size()>0){
                    List<ExcuteTime> timesPoints = adviceCom.rates.get(0).excuteTimes;
                    if (timesPoints.size() >0){
                        for (int i = 0;i<splitDays;i++){
                            LocalDate current = ksrqD.plusDays(i);
                            for (ExcuteTime excuteTime:timesPoints){
                                LocalTime localTime = LocalTime.parse(excuteTime.KSSJ);
                                jhsjDT3 = LocalDateTime.of(current,localTime);

                                //jhsjDT3 >= kzsjDT 用 (!jhsjDT3.isBefore(kzsjDT)) 表示
                                if ((jhsjDT3.isAfter(ksrqDT) || jhsjDT3.isEqual(ksrqDT)) && jhsjDT3.isBefore(jsrqDT) && (!jhsjDT3.isBefore(kzsjDT))){
                                    PlanTime planTime = new PlanTime();
                                    planTime.JHSJ = jhsjDT3.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                    planTime.JHRQ = jhsjDT3.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    planTime.SJBH = String.valueOf(excuteTime.SJBH);
                                    planTime.SJMC = excuteTime.SJMC;
                                    planTime.ZQRQ = 0;
                                    adviceCom.planTimes.add(planTime);
                                }
                            }
                        }
                    }
                }else{
                    if (tzsjDT.isEqual(LocalDateTime.MIN)){
                        jhsjDT3 = tzsjDT;
                    }else
                        jhsjDT3 = kzsjDT;

                    LocalDate jhsjD3 = jhsjDT3.toLocalDate();
                    if ((jhsjD3.isAfter(ksrqD) || jhsjD3.isEqual(ksrqD)) && jhsjD3.isBefore(jsrqD)){
                        PlanTime planTime = new PlanTime();
                        planTime.JHSJ = jhsjDT3.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        planTime.JHRQ = jhsjD3.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        planTime.SJBH = "1";
                        planTime.SJMC = adviceCom.SYPC;
                        planTime.ZQRQ = 0;
                        adviceCom.planTimes.add(planTime);
                    }
                }
            }


        }catch (Exception e){
            throw new SplitException("生成医嘱计划失败:" + e.getMessage());
        }

    }

    /**
     * 生成医嘱计划
     * @param adviceCom
     */
    public void buildAdvicePlan(AdviceCom adviceCom,PlanTypeArgs planTypeArgs,PatientArgs patientArgs) throws SplitException{
        List<PlanTime> planTimes = adviceCom.planTimes;
        List<AdvicePlan> exsitPlans = adviceCom.plans;

        if (planTimes.size() <=0) return ;

        // TODO:判断医嘱计划是否已经存在，不存在重新生成
        Integer index = 0;
        try{
            List<AdvicePlan> list = new ArrayList<>();
            String nowStr = dateTimeService.now(DataSource.PORTAL);
            for (PlanTime planTime:planTimes){
                AdvicePlan addPlan = new AdvicePlan();

                addPlan.GSLX = String.valueOf(planTypeArgs.GSLX);
                addPlan.JHSJ = String.valueOf(planTime.JHSJ);
                addPlan.JHRQ = planTime.JHRQ;
                addPlan.KSSJ = DateConvert.toLocalDateTime(adviceCom.KZSJ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                addPlan.LXH = String.valueOf(planTypeArgs.LXH);
                addPlan.SYPC = adviceCom.SYPC;
                addPlan.YPYF = adviceCom.YPYF;
                addPlan.YSYZBH = adviceCom.YSYZBH;
                addPlan.YZXH = adviceCom.JLXH;
                addPlan.YZZH = adviceCom.YZZH;
                addPlan.ZXZT = "0";
                addPlan.ZYH = adviceCom.ZYH;
                addPlan.XMXH = adviceCom.YPXH;
                addPlan.YCJL = adviceCom.YCJL;
                addPlan.JLDW = adviceCom.JLDW;
                addPlan.YCSL = adviceCom.YCSL;
                addPlan.SLDW = adviceCom.SLDW;
                addPlan.YZMC = adviceCom.YZMC;
                addPlan.YZZH = adviceCom.YZZH;
                addPlan.SJBH = planTime.SJBH;
                addPlan.SJMC = planTime.SJMC;
                // 周期日期转换
                addPlan.ZQRQ = LocalDateTime.of(LocalDate.parse(planTime.JHRQ).plusDays(Long.valueOf(String.valueOf(planTime.ZQRQ))),LocalTime.parse("00:00:00")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                //使用ENR_YZJH.BRBQ与ZY_BQYZ.BRBQ保持一致,以避免发生转科时 ENR_YZJH.BRBQ与ZY_BQYZ.BRBQ不一致问题，造成医嘱卡片打印转科前的医嘱信息
                //addPlan.BRBQ = patientArgs.BRBQ;
                addPlan.BRBQ = (adviceCom.BRBQ == null || "".equals(adviceCom.BRBQ)) ? patientArgs.BRBQ : adviceCom.BRBQ;
                addPlan.JHSD = "0";
                addPlan.CSSJ = nowStr;
                addPlan.LSYZ = adviceCom.LSYZ;
                addPlan.JGID = excutor.jgid;
                addPlan.SRHDBZ = adviceCom.SRHDBZ;
                addPlan.YZZX = adviceCom.YZZX;
                addPlan.XMLX = adviceCom.XMLX;
                index++;
                /**
                 * 56010004 临时医嘱 医嘱计划重复产生
                 */
                //判断医嘱计划是否已经存在，不存在重新生成
                List<AdvicePlan> advicePlans = adviceSplitServiceSup.getAdvicePlanByAdviceAndPlanTime(addPlan.ZYH, addPlan.YZXH, addPlan.JHSJ);
                if (advicePlans.size() > 0){
                    continue;
                }
                /*******************************************/
                list.add(addPlan);

            }

            BizResponse bizResponse = identityService.getIdentityMax("IENR_YZJH",index,DataSource.MOB);
            if (!bizResponse.isSuccess){
                throw new SplitException("获取医嘱计划主键失败:" + bizResponse.message);
            }

            for (int i = 0 ; i < list.size(); i++){
                AdvicePlan plan = list.get(i);
                plan.JHH = bizResponse.datalist.get(i).toString();
            }
            patientArgs.excuteResult.insertPlans.addAll(list);
        }catch (Exception e){
            throw new SplitException("生成医嘱计划失败!");
        }
    }

    /**
     * 监控医嘱处理
     * @param adviceCom
     * @param planTypeArgs
     * @param patientArgs
     * @throws SplitException
     */
    public void monitorAdvicesHandler(AdviceCom adviceCom,PlanTypeArgs planTypeArgs,PatientArgs patientArgs) throws SplitException{
        try {
            if (adviceCom.monitoreAdvices.size() <= 0){
                MonitoreAdvice monitoreAdvice = new MonitoreAdvice();
                monitoreAdvice.FHSJ = adviceCom.FHSJ;
                monitoreAdvice.TZFHSJ = adviceCom.TZFHSJ;
                monitoreAdvice.CZGH = "1";
                monitoreAdvice.CZSJ = dateTimeService.now(DataSource.PORTAL);
                monitoreAdvice.ZFBZ = 0;
                monitoreAdvice.YZXH = Long.valueOf(adviceCom.JLXH);
                monitoreAdvice.JZSJ = adviceCom.cfjs;
                monitoreAdvice.JGID = excutor.jgid;
                monitoreAdvice.ZYH = Long.valueOf(adviceCom.ZYH);
                monitoreAdvice.JGID = String.valueOf(excutor.jgid);
                monitoreAdvice.BRBQ = Long.valueOf(patientArgs.BRBQ);

                patientArgs.excuteResult.insertMonitors.add(monitoreAdvice);
            }else{
                MonitoreAdvice monitoreAdvice = new MonitoreAdvice();
                monitoreAdvice.FHSJ = adviceCom.FHSJ;
                monitoreAdvice.TZFHSJ = adviceCom.TZFHSJ;
                monitoreAdvice.CZGH = "1";
                monitoreAdvice.CZSJ = dateTimeService.now(DataSource.PORTAL);
                monitoreAdvice.ZFBZ = 0;
                monitoreAdvice.YZXH = Long.valueOf(adviceCom.JLXH);
                monitoreAdvice.JZSJ = adviceCom.cfjs;
                monitoreAdvice.JGID = excutor.jgid;
                monitoreAdvice.ZYH = Long.valueOf(adviceCom.ZYH);
                monitoreAdvice.BRBQ = Long.valueOf(patientArgs.BRBQ);
                patientArgs.excuteResult.updateMonitors.add(monitoreAdvice);
            }
        }catch (Exception e){
            throw new SplitException("监控医嘱处理失败:"+e.getMessage());
        }

    }
}
