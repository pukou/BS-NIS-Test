package com.bsoft.nis.service.advicesplit.support;

import com.bsoft.nis.advicesplit.args.ExcuteResults;
import com.bsoft.nis.advicesplit.args.PatientArgs;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.advicesplit.advice.AdviceCom;
import com.bsoft.nis.domain.advicesplit.advice.db.Advice;
import com.bsoft.nis.domain.advicesplit.advice.db.AdvicePlan;
import com.bsoft.nis.domain.advicesplit.advice.db.DoubleCheckMedical;
import com.bsoft.nis.domain.advicesplit.advice.db.MonitoreAdvice;
import com.bsoft.nis.domain.advicesplit.plantype.DeptPlanTypes;
import com.bsoft.nis.domain.advicesplit.ratetime.db.ExcuteRate;
import com.bsoft.nis.domain.advicesplit.ratetime.db.UsingRate;
import com.bsoft.nis.mapper.advicesplit.AdviceSplitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Describtion:医嘱拆分子服务
 * Created: dragon
 * Date： 2016/12/9.
 */
@Service
public class AdviceSplitServiceSup extends RouteDataSourceService{
    @Autowired
    AdviceSplitMapper mapper;

    public List<DeptPlanTypes> getDeptPlanTypeByDept(String bqdm, String jgid) throws SQLException,DataAccessException{
        return mapper.getDeptPlanTypeByDept(bqdm,jgid);
    }

    public List<ExcuteRate> getDeptExcuteRate(String bqdm, String jgid) throws SQLException,DataAccessException{
        return mapper.getDeptExcuteRate(bqdm, jgid);
    }

    public List<AdviceCom> getPatientAdvices(String zyh, String jgid, String ksrq) throws SQLException,DataAccessException{
        String dbtype = this.getCurrentDataSourceDBtype();
        return mapper.getPatientAdvices(zyh, jgid, ksrq, dbtype);
    }

    public List<MonitoreAdvice> getMonitoreAdvices(String zyh, String jgid, String ksrq) throws SQLException,DataAccessException{
        String dbtype = this.getCurrentDataSourceDBtype();
        return mapper.getMonitoreAdvices(zyh, jgid, ksrq, dbtype);
    }

    public List<DoubleCheckMedical> getDoubleCheckMedicals() throws SQLException,DataAccessException{
        return mapper.getDoubleCheckMedicals();
    }

    public List<AdvicePlan> getPatientAdvicePlans(String zyh, String kssj, String jssj, String jgid) throws SQLException,DataAccessException{
        String dbtype = this.getCurrentDataSourceDBtype();
        return mapper.getPatientAdvicePlans(zyh, kssj, jssj, jgid, dbtype);
    }

    public List<UsingRate> getHISUsingRate(String jgid)throws SQLException,DataAccessException {
        return mapper.getHISUsingRate(jgid);
    }

    /**
     * 医嘱拆分计划提交
     * @param excuteResults
     */
    @Transactional(rollbackFor = Exception.class)
    public void commitAdvicePlans(ExcuteResults excuteResults) {
        List<AdvicePlan> insertPlans = excuteResults.insertPlans;
        List<AdvicePlan> deltPlans = excuteResults.delPlans;
        List<AdvicePlan> deltPlansByYzxh = excuteResults.delPlansByYzxh;
        List<MonitoreAdvice> insertMonitorAdvices = excuteResults.insertMonitors;
        List<MonitoreAdvice> updateMonitorAdvices = excuteResults.updateMonitors;

        String dbtype = getCurrentDataSourceDBtype();
        // 批量执行，每次执行70条数据 批量执行也有问题
        Integer excuteCount = 70;

        //  分批批量执行删除计划语句
        if (deltPlans.size() >0){
            //if (insertPlans.size() <= excuteCount){
            if (deltPlans.size() <= excuteCount){
                Map deleteAdvicePlansMap = new HashMap();
                deleteAdvicePlansMap.put("dbtype",dbtype);
                deleteAdvicePlansMap.put("plans",deltPlans);
                mapper.deleteAdvicePlans(deleteAdvicePlansMap);
            }else{
                Double plcs = Math.ceil(Double.valueOf(deltPlans.size())/Double.valueOf(excuteCount)) ;
                for (int i = 1 ; i <= plcs;i++){
                    //Integer endIndex = i * excuteCount-1; //当删除行数超过excuteCount时，此处会引起漏删除
                    Integer endIndex = i * excuteCount;
                    Integer startIndex = (i-1)*excuteCount;
                    //if (endIndex >= insertPlans.size()){
                    //    endIndex = insertPlans.size();
                    //}
                    if (endIndex >= deltPlans.size()){
                        endIndex = deltPlans.size();
                    }

                    Map deleteAdvicePlansMap1 = new HashMap();
                    deleteAdvicePlansMap1.put("dbtype",dbtype);
                    deleteAdvicePlansMap1.put("plans",deltPlans.subList(startIndex,endIndex));
                    mapper.deleteAdvicePlans(deleteAdvicePlansMap1);
                }
            }

           /* Map deleteAdvicePlansMap = new HashMap();
            deleteAdvicePlansMap.put("dbtype",dbtype);
            deleteAdvicePlansMap.put("plans",deltPlans);
            mapper.deleteAdvicePlans(deleteAdvicePlansMap);*/
        }

        // 根据医嘱序号删除计划语句
        if (deltPlansByYzxh.size()>0){
            Map delPlandsByYzxhMap = new HashMap();
            delPlandsByYzxhMap.put("dbtype",dbtype);
            delPlandsByYzxhMap.put("plans",deltPlansByYzxh);
            mapper.deleteAdvicePlansByYzxh(delPlandsByYzxhMap);
        }

        // 批量执行插入计划语句
        if (insertPlans.size() > 0){
            if (insertPlans.size() <= excuteCount){
                Map insertAdvicePlansMap = new HashMap();
                insertAdvicePlansMap.put("dbtype",dbtype);
                insertAdvicePlansMap.put("plans",insertPlans);
                mapper.addAdvicePlans(insertAdvicePlansMap);
            }else{
                Double plcs = Math.ceil(Double.valueOf(insertPlans.size())/Double.valueOf(excuteCount)) ;
                for (int i = 1 ; i <= plcs;i++){
                    //Integer endIndex = i * excuteCount-1; //当新增行数超过excuteCount时，此处会引起漏拆分
                    Integer endIndex = i * excuteCount;
                    Integer startIndex = (i-1)*excuteCount;
                    if (endIndex >= insertPlans.size()){
                        endIndex = insertPlans.size();
                    }

                    Map insertAdvicePlansMap1 = new HashMap();
                    insertAdvicePlansMap1.put("dbtype",dbtype);
                    insertAdvicePlansMap1.put("plans",insertPlans.subList(startIndex,endIndex));
                    mapper.addAdvicePlans(insertAdvicePlansMap1);
                }
            }

            /*Map insertAdvicePlansMap = new HashMap();
            insertAdvicePlansMap.put("dbtype",dbtype);
            insertAdvicePlansMap.put("plans",insertPlans);
            mapper.addAdvicePlans(insertAdvicePlansMap);*/
        }

        StringBuffer sb = new StringBuffer("YZXHS: ");
        for(MonitoreAdvice monitor : insertMonitorAdvices){
            sb.append(monitor.YZXH).append(",");
        }

        Logger logger = Logger.getLogger("AdviceSplitServiceSup");
        logger.info(sb.toString());

        // 插入监控医嘱
        if (insertMonitorAdvices.size()>0){
            Map insertMonitorAdviceMap = new HashMap();
            insertMonitorAdviceMap.put("dbtype",dbtype);
            insertMonitorAdviceMap.put("monitors",insertMonitorAdvices);
            mapper.addMonitorAdvice(insertMonitorAdviceMap);
        }

        // 更新监控医嘱
        if (updateMonitorAdvices.size()>0){
            Map updateMonitorAdviceMap = new HashMap();
            updateMonitorAdviceMap.put("dbtype",dbtype);
            updateMonitorAdviceMap.put("monitors",updateMonitorAdvices);
            mapper.updateMonitorAdvice(updateMonitorAdviceMap);
        }
    }

    /**
     * 56010004 临时医嘱 医嘱计划重复产生
     * 根据医嘱序号和计划时间点获取医嘱计划
     * @param zyh
     * @param yzxh
     * @param plantime
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<AdvicePlan> getAdvicePlanByAdviceAndPlanTime(String zyh,
                                                             String yzxh,
                                                             String plantime)
            throws SQLException,DataAccessException{
        keepOrRoutingDateSource(DataSource.MOB);
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getAdvicePlanByAdviceAndPlanTime(zyh,yzxh,plantime,dbtype);
    }

    // 问题描述：确定医嘱赋空操作，停嘱赋空之后会导致我们的医嘱无法拆分且导致监控表插入重复数据的主索引冲突问题
    // 解决方案：比较符合医嘱和病区医嘱的停医嘱时间，若医嘱停医嘱时间为空，复核医嘱停嘱时间不为空，将复核医嘱停嘱时间置空
    public int updateMonitorAdviceStopTime(String zyh,String yzxh)throws SQLException{
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.updateMonitorAdviceStopTime(zyh,yzxh);
    }
}
