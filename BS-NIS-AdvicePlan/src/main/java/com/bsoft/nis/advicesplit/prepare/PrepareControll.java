package com.bsoft.nis.advicesplit.prepare;

import com.bsoft.nis.advicesplit.AdviceSplitExcutor;
import com.bsoft.nis.advicesplit.args.PatientArgs;
import com.bsoft.nis.advicesplit.args.PlanTypeArgs;
import com.bsoft.nis.advicesplit.exception.NoPatientException;
import com.bsoft.nis.advicesplit.exception.WrapperException;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.domain.advicesplit.advice.AdviceCom;
import com.bsoft.nis.domain.advicesplit.advice.db.AdvicePlan;
import com.bsoft.nis.domain.advicesplit.advice.db.DoubleCheckMedical;
import com.bsoft.nis.domain.advicesplit.advice.db.MonitoreAdvice;
import com.bsoft.nis.domain.advicesplit.plantype.DeptPlanTypes;
import com.bsoft.nis.domain.advicesplit.plantype.db.PlanTypeDetail;
import com.bsoft.nis.domain.advicesplit.ratetime.db.ExcuteRate;
import com.bsoft.nis.domain.advicesplit.ratetime.db.ExcuteTime;
import com.bsoft.nis.domain.advicesplit.ratetime.db.UsingRate;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.advicesplit.AdviceSplitService;
import com.bsoft.nis.service.patient.PatientMainService;
import com.bsoft.nis.util.date.DateUtil;
import com.bsoft.nis.util.date.pojo.OffCycle;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:医嘱拆分准备器
 * Created: dragon
 * Date： 2016/12/8.
 */
@Component
@Scope(value = "prototype")
public class PrepareControll implements PrepareControllInterface{
    private Log logger = LogFactory.getLog(PrepareControll.class);

    private AdviceSplitExcutor excutor ;  //医嘱拆分执行器
    @Autowired
    PatientMainService patientService;    //病人服务
    @Autowired
    AdviceSplitService splitService;    //拆分服务
    @Autowired
    DictCachedHandler cacheService;  //缓存服务

    @Override
    public void init(AdviceSplitExcutor excutor) {
        this.excutor = excutor;
    }

    /**
     * 拆分必备数据组装
     * 1.科室相关数据(归属计划类型、频次、执行时间、科室病人)
     * 2.病人相关数据(归属计划类型、频次、执行日期、医嘱)
     * @param id
     * @param isPatient
     * @param jgid
     * @param ksrq
     * @param jsrq
     * @return
     */
    @Override
    public void prepareWrapper(String id, Boolean isPatient,String jgid,String ksrq,String jsrq) throws WrapperException,NoPatientException{
        try{
            prepareDeptWrapper(id, isPatient, jgid, ksrq, jsrq);
            preparePatientWrapper();
        }catch (WrapperException ex){
            throw ex;
        }
    }

    /**
     * 拆分必备的科室相关数据组装
     * 1.科室病人
     * 2.科室计划类型
     * 3.执行频次和时间
     * @param id
     * @param isPatient
     * @param jgid
     * @param ksrq
     * @param jsrq
     * @throws WrapperException
     */
    private void prepareDeptWrapper(String id, Boolean isPatient,String jgid,String ksrq,String jsrq) throws WrapperException{
        try {
            prepareDeptPatient(id, isPatient, jgid, ksrq, jsrq);
            prepareDeptPlanType(id, isPatient, jgid);
            prepareDeptExcuteRate();
        }catch (WrapperException ex){
            throw ex;
        }
    }

    /**
     * 拆分必备的病人相关数据组装
     * 1.病人计划和医嘱信息
     * 2.病人医嘱信息归类
     */
    private void preparePatientWrapper() throws NoPatientException,WrapperException{
        try {
            wrapperPatientsPlanTypes();
            wrapperPatientsAdvices();
            wrapperPatientsPlans();
        }catch (NoPatientException e){
            throw e;
        }catch (WrapperException e){
            throw e;
        }

    }

    /**
     * 获取病人计划和组装计划
     * @throws NoPatientException
     * @throws WrapperException
     */
    private void wrapperPatientsPlans() throws NoPatientException,WrapperException{
        if (excutor.papareExcutorParams.getPatientWrappers().size()<=0) throw new NoPatientException("无拆分病人");
        BizResponse<AdvicePlan> planBizResponse;
        List<AdvicePlan> plans = new ArrayList<>();
        List<PatientArgs> patientArgses = excutor.papareExcutorParams.getPatientWrappers();

        String kssj = excutor.ksrq;
        String jssj = excutor.jsrq;
        try {
            //考虑到如果不是按自然天拆分的话,会多拆一天数据,所以要结束时间要+1
            jssj = DateUtil.dateoff(OffCycle.DAY, jssj, "1");
            for (PatientArgs patientArgs:patientArgses){
                try{
                    planBizResponse = splitService.getPatientAdvicePlans(patientArgs.ZYH, kssj, jssj, excutor.jgid);
                    if (!planBizResponse.isSuccess){
                        throw new WrapperException("获取病人医嘱计划失败!");
                    }

                }catch (WrapperException e){
                    continue;
                }
                // 将医嘱计划归类到相应的医嘱对象里
                plans = planBizResponse.datalist;
                List<PlanTypeArgs> planTypeArgses = patientArgs.planTypes;
                for (PlanTypeArgs planTypeArgs:planTypeArgses){
                    List<AdviceCom> adviceComs = planTypeArgs.advices;
                    for (AdviceCom adviceCom:adviceComs){
                        List<AdvicePlan> advicePlans = (List<AdvicePlan>)CollectionUtils.select(plans,new AdvicePlanPredicate(adviceCom.JLXH));
                        adviceCom.plans = advicePlans;
                    }
                }

                // 记录需删除的计划，剩下的计划列表是要考虑全部删除,因为是医嘱不存在计划还存在
                List<AdvicePlan> delPlans = (List<AdvicePlan>)CollectionUtils.select(plans,new AdvicePlanDelPredicate());
                patientArgs.excuteResult.delPlans.addAll(delPlans);
            }
        } catch (ParseException e) {
            logger.error(e.getMessage(),e);
        }

    }

    /**
     * 组装病人计划和有效医嘱
     * 1.包装病人
     * 2.包装病人计划
     * 3.获取待拆分医嘱
     * 4.监控医嘱
     */
    private void wrapperPatientsPlanTypes() throws NoPatientException{
        if (excutor.papareExcutorParams.getPatients().size()<=0) throw new NoPatientException("无拆分病人");
        List<Patient> patients = excutor.papareExcutorParams.getPatients();
        List<DeptPlanTypes> planTypes = excutor.papareExcutorParams.getPlantypes();
        List<ExcuteRate> rates = excutor.papareExcutorParams.getExcuteRates();

        for (Patient patient:patients){
            PatientArgs patientArgs = new PatientArgs(patient);
            for (DeptPlanTypes planTypes1:planTypes){
                // 病人归属计划类型
                PlanTypeArgs planTypeArgs = new PlanTypeArgs(planTypes1);
                patientArgs.planTypes.add(planTypeArgs);
                for (ExcuteRate rate:rates){
                    // 计划类型相关的频次：只保留关联类别为1的频次
                    if (rate.GLLB.equals(1) && planTypeArgs.LXH.equals(rate.GLBS)) planTypeArgs.addRate(rate);
                }
            }
            excutor.papareExcutorParams.addPatientWrapper(patientArgs);
            try{
                this.getPatientAdvices(patientArgs);
            }catch (WrapperException e){
                logger.error(e.getMessage(),e);
                continue;
            }
        }
    }

    /**
     * 组装病人医嘱
     * @throws NoPatientException
     */
    private void wrapperPatientsAdvices()throws NoPatientException{
        if (excutor.papareExcutorParams.getPatientWrappers().size() <=0) throw new NoPatientException("无拆分病人!");
        List<PatientArgs> patients = excutor.papareExcutorParams.getPatientWrappers();

        for(PatientArgs patient:patients){
            classificationOfmedicaAdvice(patient);
        }
    }
    /**
     * 病人医嘱归类
     */
    private void classificationOfmedicaAdvice(PatientArgs patient){
        List<AdviceCom> advices = patient.avalibleAdvices;
        if (advices.size()<=0) return ;

        // 按照计划类型归类医嘱
        Integer notPayProjectSplitNum = 0;
        for (PlanTypeArgs plantype:patient.planTypes){
            List<PlanTypeDetail> details = plantype.planDetails;
            if (details.size() <=0) continue;

            Boolean isNotPayProject = false ;
            for (PlanTypeDetail detail:details){
                if (detail.MXLX.equals(4)){        // 药品用法
                    List<AdviceCom> _advice =(List<AdviceCom>)CollectionUtils.select(advices,new DrugUsePredicate(String.valueOf(detail.MXXH)));
                    plantype.advices.addAll(_advice);
                }else if (detail.MXLX.equals(3)){  // 特殊医嘱:
                    isNotPayProject = true;
                }else if (detail.MXLX.equals(2)){  // 医疗收费
                    List<AdviceCom> _advice =(List<AdviceCom>)CollectionUtils.select(advices,new HospitalPayPredicate(String.valueOf(detail.MXXH)));
                    plantype.advices.addAll(_advice);
                }else if (detail.MXLX.equals(1)){  // 收费项目
                    List<AdviceCom> _advice =(List<AdviceCom>)CollectionUtils.select(advices,new PayProjectPredicate(String.valueOf(detail.MXXH)));
                    plantype.advices.addAll(_advice);
                }
            }

            // 特殊医嘱，独立处理：特殊医嘱太多，不可能全部维护，固采用如果计划明细中维护一条特殊医嘱的病区，特殊医嘱全部拆分
            //         拆分条件：病区定制中有护理治疗类，且护理治疗类中维护了至少一条xmlx = 3的明细
            if (isNotPayProject && notPayProjectSplitNum == 0){
                List<AdviceCom> _advice =(List<AdviceCom>)CollectionUtils.select(advices,new SpecialAdvicePredicate());
                plantype.advices.addAll(_advice);
                notPayProjectSplitNum++;
            }


            // 确定当前计划所属医嘱属性频次 所对应的执行时间 如：tid  8:00 12:00 16:00
            for (AdviceCom adviceCom:plantype.advices){
                for (ExcuteRate rate:plantype.getRates()){
                    if (StringUtils.isEmpty(adviceCom.SYPC)) continue;
                    if (adviceCom.SYPC.equals(rate.PCBM)){
                        adviceCom.rates.add(rate);
                    }
                }
            }
        }
    }

    /**
     * 组装病人医嘱、监控医嘱
     * @param patientArgs
     * @throws WrapperException
     */
    private void getPatientAdvices(PatientArgs patientArgs) throws WrapperException{
        BizResponse<AdviceCom> response = splitService.getPatientAdvices(patientArgs.ZYH, excutor.jgid, excutor.ksrq);
        if (!response.isSuccess){
            throw new WrapperException("病人医嘱获取失败!");
        }
        patientArgs.avalibleAdvices = response.datalist;
        BizResponse<MonitoreAdvice> monitoreAdviceBizResponse = splitService.getMonitoreAdvices(patientArgs.ZYH,excutor.jgid,excutor.ksrq);
        if (!monitoreAdviceBizResponse.isSuccess){
            throw new WrapperException("病人监控医嘱获取失败!");
        }
        patientArgs.monitoreAdvices = monitoreAdviceBizResponse.datalist;
        BizResponse<DoubleCheckMedical> doubleCheckMedicals = splitService.getDoubleCheckMedicals();
        if (!doubleCheckMedicals.isSuccess){
            throw new WrapperException("双签药品数据获取失败");
        }
        // 完善医嘱信息
        for (final AdviceCom advice:response.datalist){
            advice.SLDW = cacheService.getValueByKeyFromCached(CachedDictEnum.MOB_TYPK,excutor.jgid,"YPXH",String.valueOf(advice.YPXH),"BFDW");
            advice.JLDW = cacheService.getValueByKeyFromCached(CachedDictEnum.MOB_TYPK,excutor.jgid,"YPXH",String.valueOf(advice.YPXH),"JLDW");
            // 医嘱的双签信息
            List<DoubleCheckMedical> exsitList = (List)CollectionUtils.select(doubleCheckMedicals.datalist, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    DoubleCheckMedical checkMedical = (DoubleCheckMedical)o;
                    return checkMedical.YPXH.equals(advice.YPXH);
                }
            });
            if (exsitList.size() > 0)
                advice.SRHDBZ = "1";
            else
                advice.SRHDBZ = "0";

            // 医嘱的监控信息
            for (MonitoreAdvice monitorAdvice:monitoreAdviceBizResponse.datalist){
                if (String.valueOf(monitorAdvice.YZXH).equals(advice.JLXH)){
                    advice.monitoreAdvices.add(monitorAdvice);
                }
            }
        }
    }

    /**
     * 准备科室病人
     * @param id
     * @param isPatient
     * @param jgid
     * @param ksrq
     * @param jsrq
     * @return
     */
    private void prepareDeptPatient(String id,Boolean isPatient,String jgid,String ksrq,String jsrq) throws WrapperException{
        BizResponse<Boolean> response = new BizResponse<>();
        BizResponse<Patient> patientBizResponse ;

        // 1.准备病人列表
        // 单病人
        if (isPatient){
            patientBizResponse = patientService.getPatientByZyh(id);
            if (patientBizResponse.isSuccess){
                if (excutor.papareExcutorParams.getPatients().size()>0) excutor.papareExcutorParams.clearPatients();
                excutor.papareExcutorParams.addPatient(patientBizResponse.data);
            }else{
                throw new WrapperException(patientBizResponse.message);
            }
        }else{ // 病区病人
            patientBizResponse = patientService.getSimplePatientsForDept(id, jgid);
            if (patientBizResponse.isSuccess){
                if (excutor.papareExcutorParams.getPatients().size()>0) excutor.papareExcutorParams.clearPatients();
                excutor.papareExcutorParams.addPatients(patientBizResponse.datalist);
            }else{
                throw new WrapperException(patientBizResponse.message);
            }
        }
    }

    /**
     * 准备科室计划类型
     * @param id
     * @param isPatient
     * @param jgid
     * @return
     */
    private void prepareDeptPlanType(String id,Boolean isPatient,String jgid) throws WrapperException{
        BizResponse<DeptPlanTypes> planResponse = new BizResponse<>();
        String bqdm = "";
        // 2.科室计划相关信息
        if (excutor.papareExcutorParams.getPatients().size() <= 0) throw  new WrapperException("无拆分病人!");
        bqdm = !isPatient?id:excutor.papareExcutorParams.getPatients().get(0).BRBQ;
        excutor.papareExcutorParams.setBQDM(bqdm);

        planResponse = splitService.getDeptPlanTypeByDept(bqdm,jgid);
        if (!planResponse.isSuccess)
            throw new WrapperException(planResponse.message);

        excutor.papareExcutorParams.setPlantypes(planResponse.datalist);
    }

    /**
     * 准备科室执行频次
     * @return
     */
    private void prepareDeptExcuteRate() throws WrapperException{
        BizResponse<ExcuteRate> response ;
        BizResponse<UsingRate> hisExcuteRateBizResponse;
        String ksdm,jgid ;
        ksdm = excutor.papareExcutorParams.getBQDM();
        jgid = excutor.jgid;

        response = splitService.getDeptExcuteRate(ksdm,jgid);
        hisExcuteRateBizResponse = splitService.getHISUsingRate(jgid);

        if (!response.isSuccess) throw new WrapperException(response.message);
        if (!hisExcuteRateBizResponse.isSuccess) throw new WrapperException(hisExcuteRateBizResponse.message);
        List<ExcuteRate> excuteRates = response.datalist;
        List<UsingRate> usingRates = hisExcuteRateBizResponse.datalist;
        for (ExcuteRate excuteRate:excuteRates){
            for (UsingRate usingRate:usingRates){
                if (excuteRate.PCBM.toUpperCase().equals(usingRate.PCBM.toUpperCase())){
                    for (ExcuteTime excuteTime:excuteRate.excuteTimes){
                        excuteTime.ATFY = Integer.valueOf(usingRate.ATFY);
                    }
                    continue;
                }
            }
        }
        excutor.papareExcutorParams.setExcuteRates(response.datalist);
    }

    /**
     * 组装病人数据
     * @param id
     * @param isPatient
     * @param jgid
     * @param ksrq
     * @param jsrq
     * @return
     */
    private BizResponse<Boolean> preparePatientWrapper(String id,Boolean isPatient,String jgid,String ksrq,String jsrq) {
        return null;
    }


    public BizResponse<Boolean> ProcessFalse(BizResponse<Boolean> response,String message){
        response.isSuccess = false;
        response.message = message;
        return response;
    }

    /**
     * 药品用法过滤器
     */
    class DrugUsePredicate implements Predicate{
        String drugUse = null;
        public DrugUsePredicate(String drugUse){
            this.drugUse = drugUse;
        }
        @Override
        public boolean evaluate(Object o) {
            AdviceCom ad = (AdviceCom)o;
            if (StringUtils.isEmpty(ad.YPYF))
                return false;
            if (StringUtils.isEmpty(ad.YPLX))
                return false;
            if (Long.valueOf(ad.YPLX) <=0)
                return false;
            if (ad.YPYF.equals(String.valueOf(drugUse))){
                return true;
            }
            return false;
        }
    }

    /**
     * 收费项目医嘱过滤器
     */
    class PayProjectPredicate implements Predicate{
        String xmxh = null;
        public PayProjectPredicate(String xmxh){
            this.xmxh = xmxh;
        }

        @Override
        public boolean evaluate(Object o) {
            AdviceCom ad = (AdviceCom)o;
            if (StringUtils.isEmpty(ad.FYGB)){
                return false;
            }

            return ad.FYGB.equals(xmxh);
        }
    }

    /**
     * 医疗收费项目医嘱过滤器
     */
    class HospitalPayPredicate implements Predicate{
        String xmxh = null;
        public HospitalPayPredicate(String xmxh){
            this.xmxh = xmxh;
        }
        @Override
        public boolean evaluate(Object o) {
            AdviceCom ad = (AdviceCom)o;
            if (StringUtils.isEmpty(ad.YPXH)){
                return false;
            }

            return ad.YPXH.equals(xmxh);
        }
    }

    /**
     * 特殊医嘱过滤器
     */
    class SpecialAdvicePredicate implements Predicate{

        @Override
        public boolean evaluate(Object o) {
            AdviceCom ad = (AdviceCom)o;
            return ad.JFBZ.equals("3");
        }
    }

    /**
     * 医嘱计划过滤器
     */
    class AdvicePlanPredicate implements Predicate{
        String yzxh = null;
        public AdvicePlanPredicate(String yzxh){
            this.yzxh = yzxh;
        }

        @Override
        public boolean evaluate(Object o) {
            AdvicePlan plan = (AdvicePlan)o;
            if (plan.YZXH.equals(yzxh)){
                plan.PPBZ = 1;
                return true;
            }
            return false;
        }
    }

    /**
     * 待删除医嘱计划过滤器
     */
    class AdvicePlanDelPredicate implements Predicate{

        @Override
        public boolean evaluate(Object o) {
            AdvicePlan plan = (AdvicePlan)o;
            return plan.PPBZ == 0;
        }
    }
}
