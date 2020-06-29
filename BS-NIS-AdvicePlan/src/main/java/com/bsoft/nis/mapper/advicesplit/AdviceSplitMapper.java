package com.bsoft.nis.mapper.advicesplit;

import com.bsoft.nis.domain.advicesplit.advice.AdviceCom;
import com.bsoft.nis.domain.advicesplit.advice.db.Advice;
import com.bsoft.nis.domain.advicesplit.advice.db.AdvicePlan;
import com.bsoft.nis.domain.advicesplit.advice.db.DoubleCheckMedical;
import com.bsoft.nis.domain.advicesplit.advice.db.MonitoreAdvice;
import com.bsoft.nis.domain.advicesplit.plantype.DeptPlanTypes;
import com.bsoft.nis.domain.advicesplit.ratetime.db.ExcuteRate;
import com.bsoft.nis.domain.advicesplit.ratetime.db.UsingRate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Describtion:医嘱拆分服务接口
 * Created: dragon
 * Date： 2016/12/9.
 */
public interface AdviceSplitMapper {
    List<DeptPlanTypes> getDeptPlanTypeByDept(@Param(value = "BQDM") String bqdm,@Param(value = "JGID") String jgid);

    List<ExcuteRate> getDeptExcuteRate(@Param(value = "BQDM") String bqdm,@Param(value = "JGID") String jgid);

    List<AdviceCom> getPatientAdvices(@Param(value = "ZYH") String zyh,@Param(value = "JGID") String jgid,@Param(value = "KSRQ") String ksrq,@Param(value = "dbtype") String dbtype);

    List<MonitoreAdvice> getMonitoreAdvices(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid,@Param(value = "KSRQ") String ksrq,@Param(value = "dbtype") String dbtype);

    List<DoubleCheckMedical> getDoubleCheckMedicals();

    List<AdvicePlan> getPatientAdvicePlans(@Param(value = "ZYH") String zyh,@Param(value = "KSRQ") String kssj,@Param(value = "JSRQ") String jsrq,@Param(value = "JGID") String jgid,@Param(value = "dbtype") String dbtype);

    List<UsingRate> getHISUsingRate(@Param(value = "JGID") String jgid);

    Integer addAdvicePlans(Map map);

    Integer deleteAdvicePlans(Map map);

    Integer deleteAdvicePlansByYzxh(Map map);

    Integer addMonitorAdvice(Map map);

    Integer updateMonitorAdvice(Map map);

    /**
     * 56010004 临时医嘱 医嘱计划重复产生
     * @param zyh
     * @param yzxh
     * @param plantime
     * @param dbtype
     * @return
     */
    List<AdvicePlan> getAdvicePlanByAdviceAndPlanTime(@Param(value = "ZYH") String zyh, @Param(value = "YZXH") String yzxh, @Param(value = "SJD") String plantime,@Param(value = "dbtype") String dbtype);

    /**
     * 问题描述：确定医嘱赋空操作，停嘱赋空之后会导致我们的医嘱无法拆分且导致监控表插入重复数据的主索引冲突问题
     // 解决方案：比较符合医嘱和病区医嘱的停医嘱时间，若医嘱停医嘱时间为空，复核医嘱停嘱时间不为空，将复核医嘱停嘱时间置空
     * @param zyh
     * @param yzxh
     * @return
     */
    int updateMonitorAdviceStopTime(@Param(value = "ZYH") String zyh,@Param(value = "YZXH") String yzxh);
}
