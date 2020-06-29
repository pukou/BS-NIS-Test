package com.bsoft.nis.service.adviceSync;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.PlanInfo;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.adviceSync.support.AdviceSyncServiceSup;
import com.bsoft.nis.service.patient.PatientMainService;
import com.bsoft.nis.util.date.DateConvert;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 医嘱同步（病区医嘱）主服务
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-03-01
 * Time: 16:58
 * Version:
 */
@Service
public class AdviceSyncMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(AdviceSyncMainService.class);

    @Autowired
    AdviceSyncServiceSup service;
    @Autowired
    DateTimeService timeService; // 日期时间服务
    @Autowired
    PatientMainService patientService; // 病人服务
    @Autowired
    IdentityService identityService;//种子表服务

    /**
     * 同步 - 按住院号
     *
     * @param zyh
     * @param kssj
     * @param jssj
     * @param jgid
     * @return
     */
    public BizResponse<String> syncPlanByZyh(String zyh, String kssj, String jssj, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (StringUtils.isBlank(zyh) || StringUtils.isBlank(jgid)
                    || StringUtils.isBlank(kssj)) {
                response.isSuccess = false;
                response.message = "传入参数错误";
                return response;
            }
            BizResponse<Patient> tempResponse = patientService.getPatientByZyh(zyh);
            if (!tempResponse.isSuccess) {
                response.isSuccess = false;
                response.message = "获取病人信息失败";
                return response;
            }
            Patient patient = tempResponse.data;
            List<String> gslxList = service.getGslxListByBqdm(patient.BRBQ);
            if (gslxList == null || gslxList.size() == 0) {
                response.isSuccess = false;
                response.message = "暂无对应归属类型数据";
                return response;
            }
            List<PlanInfo> bqPlanInfoList = service.getBQPlanInfoListByZyh(zyh, gslxList, kssj, jgid);
            if (bqPlanInfoList == null || bqPlanInfoList.size() == 0) {
                response.isSuccess = false;
                response.message = "获取病区医嘱计划数据失败";
                return response;

            }
            List<PlanInfo> planInfoList = service.getPlanInfoListByZyh(zyh, gslxList, kssj, jgid);
            //需要作废的医嘱列表
            List<PlanInfo> delList = getDelPlanInfoList(bqPlanInfoList, planInfoList);
            //需要新增的医嘱
            List<PlanInfo> addList = getAddPlanInfoList(bqPlanInfoList, patient.BRBQ, patient.JGID);
            //需要更新的病区医嘱
            List<PlanInfo> editList = bqPlanInfoList;

            keepOrRoutingDateSource(DataSource.MOB);
            boolean isSucess = operPlanInfo(addList, delList);
            if (!isSucess) {
                response.isSuccess = false;
                response.message = "[同步失败]数据库执行错误";
                return response;
            }
            keepOrRoutingDateSource(DataSource.HRP);
            isSucess = operBqPlanInfo(editList);
            if (!isSucess) {
                response.isSuccess = false;
                response.message = "[同步失败]数据库执行错误";
                return response;
            }
            response.isSuccess = true;
            response.data = "";
            response.message = "同步成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[同步失败]数据库执行错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[同步失败]服务内部错误";
        }
        return response;
    }

    /**
     * 同步 - 按计划号
     *
     * @param jhh
     * @param jgid
     * @return
     */
    public BizResponse<String> syncPlanByJhh(String jhh, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (StringUtils.isBlank(jhh) || StringUtils.isBlank(jgid)) {
                response.isSuccess = false;
                response.message = "传入参数错误";
                return response;
            }
            String[] jhhArray = jhh.split(",");
            List<String> jhhList = new ArrayList<>();
            for (String item : jhhArray) {
                jhhList.add(item);
            }
            List<PlanInfo> bqPlanInfoList = service.getBQPlanInfoListByJhhList(jhhList);
            if (bqPlanInfoList == null || bqPlanInfoList.size() == 0) {
                response.isSuccess = false;
                response.message = "暂无对应计划数据";
                return response;
            }
            List<String> gslxList = service.getGslxListByBqdm(bqPlanInfoList.get(0).BRBQ);
            if (gslxList == null || gslxList.size() == 0) {
                response.isSuccess = false;
                response.message = "暂无对应归属类型数据";
                return response;
            }
            List<PlanInfo> planInfoList = service.getPlanInfoListByGljhhList(jhhList);
            //需要作废的医嘱列表
            List<PlanInfo> delList = getDelPlanInfoList(bqPlanInfoList, planInfoList);
            //需要新增的医嘱
            List<PlanInfo> addList = getAddPlanInfoList(bqPlanInfoList, bqPlanInfoList.get(0).BRBQ, bqPlanInfoList.get(0).JGID);
            //需要更新的病区医嘱
            List<PlanInfo> editList = bqPlanInfoList;

            keepOrRoutingDateSource(DataSource.MOB);
            boolean isSucess = operPlanInfo(addList, delList);
            if (!isSucess) {
                response.isSuccess = false;
                response.message = "[同步失败]数据库执行错误";
                return response;
            }
            keepOrRoutingDateSource(DataSource.HRP);
            isSucess = operBqPlanInfo(editList);
            if (!isSucess) {
                response.isSuccess = false;
                response.message = "[同步失败]数据库执行错误";
                return response;
            }
            response.isSuccess = true;
            response.data = "";
            response.message = "同步成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[同步失败]数据库执行错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[同步失败]服务内部错误";
        }
        return response;
    }

    /**
     * 同步 - 按病区
     *
     * @param bqdm
     * @param kssj
     * @param jssj
     * @param jgid
     * @return
     */
    public BizResponse<String> syncPlanByBq(String bqdm, String kssj, String jssj, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (StringUtils.isBlank(bqdm) || StringUtils.isBlank(jgid)
                    || StringUtils.isBlank(kssj)) {
                response.isSuccess = false;
                response.message = "传入参数错误";
                return response;
            }
            List<String> gslxList = service.getGslxListByBqdm(bqdm);
            if (gslxList == null || gslxList.size() == 0) {
                response.isSuccess = false;
                response.message = "暂无对应归属类型数据";
                return response;
            }
            List<PlanInfo> bqPlanInfoList = service.getBQPlanInfoListByBqdm(bqdm, gslxList, kssj, jgid);
            if (bqPlanInfoList == null || bqPlanInfoList.size() == 0) {
                response.isSuccess = false;
                response.message = "获取病区医嘱计划数据失败";
                return response;
            }
            List<PlanInfo> planInfoList = service.getPlanInfoListByBqdm(bqdm, gslxList, kssj, jgid);
            //需要作废的医嘱列表
            List<PlanInfo> delList = getDelPlanInfoList(bqPlanInfoList, planInfoList);
            //需要新增的医嘱
            List<PlanInfo> addList = getAddPlanInfoList(bqPlanInfoList, bqdm, jgid);
            //需要更新的病区医嘱
            List<PlanInfo> editList = bqPlanInfoList;

            keepOrRoutingDateSource(DataSource.MOB);
            boolean isSucess = operPlanInfo(addList, delList);
            if (!isSucess) {
                response.isSuccess = false;
                response.message = "[同步失败]数据库执行错误";
                return response;
            }
            keepOrRoutingDateSource(DataSource.HRP);
            isSucess = operBqPlanInfo(editList);
            if (!isSucess) {
                response.isSuccess = false;
                response.message = "[同步失败]数据库执行错误";
                return response;
            }
            response.isSuccess = true;
            response.data = "";
            response.message = "同步成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[同步失败]数据库执行错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[同步失败]服务内部错误";
        }
        return response;
    }


    private List<PlanInfo> getAddPlanInfoList(List<PlanInfo> bqPlanInfoList, String brbq, String jgid)
            throws ParseException {
        //需要新增的医嘱
        List<PlanInfo> addList = new ArrayList<>();
        for (PlanInfo bqPlanInfo : bqPlanInfoList) {
            bqPlanInfo.JHH = String.valueOf(identityService.getIdentityMax("IENR_YZJH",DataSource.MOB));
            bqPlanInfo.BRBQ = brbq;
            Date date = DateConvert.toDateTime(bqPlanInfo.JHSJ, "yyyy-MM-dd HH:mm:ss.S");
            bqPlanInfo.JHRQ = DateConvert.getDateYear(date) + "-" + DateConvert.getDateMonth(date) + "-" + DateConvert.getDateDay(date);
            bqPlanInfo.JHSD = "0";
            bqPlanInfo.CSSJ = timeService.now(DataSource.MOB);
            bqPlanInfo.LSYZ = "0";
            bqPlanInfo.SJMC = bqPlanInfo.SYPC + "-" + DateConvert.getDateHour(date);
            bqPlanInfo.ZQRQ = bqPlanInfo.JHRQ;
            bqPlanInfo.JGID = jgid;
            addList.add(bqPlanInfo);
        }
        return addList;
    }

    private List<PlanInfo> getDelPlanInfoList(List<PlanInfo> bqPlanInfoList, List<PlanInfo> planInfoList) {
        //需要作废的医嘱列表
        List<PlanInfo> delList = new ArrayList<>();
        for (PlanInfo planInfo : planInfoList) {
            boolean isFind = false;
            for (PlanInfo bqPlanInfo : bqPlanInfoList) {
                if (planInfo.JHH.equals(bqPlanInfo.GLJHH)) {
                    isFind = true;
                    break;
                }
            }
            if (!isFind) {
                delList.add(planInfo);
            }
        }
        return delList;
    }

    /**
     * 注意：调用此方法之前必须显式指定启用哪个事务
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean operPlanInfo(List<PlanInfo> addList, List<PlanInfo> delList)
            throws SQLException, DataAccessException {
        boolean isSucess = service.addPlanInfoList(addList) > 0;
        if (!isSucess) {
            return isSucess;
        }
        for (PlanInfo info : delList) {
            isSucess = service.editPlanInfo(info.GLJHH) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }

        return isSucess;
    }

    /**
     * 注意：调用此方法之前必须显式指定启用哪个事务
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean operBqPlanInfo(List<PlanInfo> editList)
            throws SQLException, DataAccessException {
        boolean isSucess = false;
        for (PlanInfo info : editList) {
            isSucess = service.editBQPlanInfo(info.GLJHH) > 0;
            if (!isSucess) {
                return isSucess;
            }
        }

        return isSucess;
    }
}
