package com.bsoft.nis.service.advicesplit;

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
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.advicesplit.support.AdviceSplitServiceSup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Describtion:医嘱拆分主服务
 * Created: dragon
 * Date： 2016/12/9.
 */
@Service
public class AdviceSplitService extends RouteDataSourceService{

    private Log logger = LogFactory.getLog(AdviceSplitService.class);
    @Autowired
    AdviceSplitServiceSup service;

    /**
     * 病区计划类型
     * @param bqdm
     * @param jgid
     * @return
     */
    public BizResponse<DeptPlanTypes> getDeptPlanTypeByDept(String bqdm,String jgid){
        BizResponse<DeptPlanTypes> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            response.datalist = service.getDeptPlanTypeByDept(bqdm,jgid);
            response.isSuccess = true;
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }
        return response;
    }

    /**
     * 病区执行频次
     * @param bqdm
     * @param jgid
     * @return
     */
    public BizResponse<ExcuteRate> getDeptExcuteRate(String bqdm,String jgid){
        BizResponse<ExcuteRate> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            response.datalist = service.getDeptExcuteRate(bqdm, jgid);
            response.isSuccess = true;
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }
        return response;
    }

    /**
     * 病人有效待拆分医嘱
     * @param zyh
     * @param jgid
     * @param ksrq
     * @return
     */
    public BizResponse<AdviceCom> getPatientAdvices(String zyh, String jgid, String ksrq) {
        BizResponse<AdviceCom> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.HRP);
        try {
            response.datalist = service.getPatientAdvices(zyh, jgid, ksrq);
            response.isSuccess = true;
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }
        return response;
    }

    /**
     * 病人医嘱监控
     * @param zyh
     * @param jgid
     * @return
     */
    public BizResponse<MonitoreAdvice> getMonitoreAdvices(String zyh, String jgid,String ksrq) {
        BizResponse<MonitoreAdvice> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try{
            response.datalist = service.getMonitoreAdvices(zyh, jgid, ksrq);
            response.isSuccess = true;
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }
        return response;
    }

    /**
     * 双签药品列表
     * @return
     */
    public BizResponse<DoubleCheckMedical> getDoubleCheckMedicals(){
        BizResponse<DoubleCheckMedical> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.HRP);
        try{
            response.datalist = service.getDoubleCheckMedicals();
            response.isSuccess = true;
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }
        return response;
    }

    /**
     * 获取病人医嘱计划
     * @param zyh
     * @param kssj
     * @param jssj
     * @param jgid
     * @return
     */
    public BizResponse<AdvicePlan> getPatientAdvicePlans(String zyh, String kssj, String jssj, String jgid) {
        BizResponse<AdvicePlan> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try{
            response.datalist = service.getPatientAdvicePlans(zyh, kssj, jssj, jgid);
            response.isSuccess = true;
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }
        return response;
    }

    /**
     * 获取使用频次
     * @param jgid
     * @return
     */
    public BizResponse<UsingRate> getHISUsingRate(String jgid) {
        BizResponse<UsingRate> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.HRP);
        try {
            response.datalist = service.getHISUsingRate(jgid);
            response.isSuccess = true;
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }
        return response;
    }

}
