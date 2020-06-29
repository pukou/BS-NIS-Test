package com.bsoft.nis.service.dailywork;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.dailywork.DailyWork;
import com.bsoft.nis.domain.dailywork.DailyWorkCount;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.dailywork.support.DailyWorkBaseServiceSup;
import com.bsoft.nis.service.patient.PatientMainService;
import com.bsoft.nis.util.date.DateCompare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by king on 2016/11/17.
 */
@Service
public class DailyWorkBaseService extends RouteDataSourceService {

    @Autowired
    DailyWorkBaseServiceSup serviceSup;
    @Autowired
    PatientMainService patientMainService;

    public BizResponse<DailyWorkCount> getMission(String brbq, String hsgh, String gzsj, String jgid) {
        BizResponse<DailyWorkCount> response = new BizResponse<>();

        try {
            List<DailyWork> plan = getPlan(brbq, hsgh, gzsj, jgid);
            List<DailyWork> changeAdvice = getChangeAdvice(brbq, hsgh, gzsj, jgid);
            List<DailyWork> inspection = getInspection(brbq, hsgh, gzsj, jgid);
            List<DailyWork> risk = getRisk(brbq, hsgh, gzsj, jgid);

            DailyWorkCount count = new DailyWorkCount();
            count.plan = plan;
            count.changeAdvice = changeAdvice;
            count.inspection = inspection;
            count.risk = risk;

            response.data = count;
            response.isSuccess = true;
            response.message = "今日工作获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【今日工作】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【今日工作】服务内部错误";
        }


        return response;
    }

    public List<DailyWork> getPlan(String brbq, String hsgh, String jhrq, String jgid) throws SQLException, DataAccessException, IOException, ClassNotFoundException {


        String rq = jhrq.substring(0, 10);
        List<DailyWork> planList = new ArrayList<>();
        BizResponse<SickPersonVo> patientList = getPatientList(brbq, hsgh, jgid);
        List<SickPersonVo> personVos = patientList.datalist;

        List<String> data = new ArrayList<>();
        List<DailyWork> personList = new ArrayList<>();

        if (personVos != null && personVos.size() > 0) {
            for (int i = 0; i < personVos.size(); i++) {
                SickPersonVo personVo = personVos.get(i);
                data.add(personVo.ZYH);

                DailyWork dailyWork = new DailyWork();
                dailyWork.ZYHM = personVo.ZYHM;
                dailyWork.ZYH = personVo.ZYH;
                dailyWork.BRCH = personVo.BRCH;
                dailyWork.BRXM = personVo.BRXM;

                personList.add(dailyWork);
            }
        }
        if (data.size() == 0) {
            data.add("-1");
        }
        keepOrRoutingDateSource(DataSource.MOB);
        List<DailyWork> planListTemp = serviceSup.getPlan(data, rq, jgid);

        for (int i = 0; i < planListTemp.size(); i++) {
            for (DailyWork patitent : personList) {
                if (planListTemp.get(i).ZYH.equals(patitent.ZYH)) {
                    planListTemp.get(i).ZYHM = patitent.ZYHM;
                    planListTemp.get(i).BRXM = patitent.BRXM;
                    planListTemp.get(i).BRCH = patitent.BRCH;
                    break;
                }
            }
        }
        //去重复
        for (DailyWork plan : planListTemp) {
            boolean hasAdd = hasAdded(plan.ZYH, planList);
            if (!hasAdd) {
                planList.add(plan);
            }
        }
        List<DailyWork> planListNew = new ArrayList<>();
        for (int i = 0; i < personVos.size(); i++) {
            SickPersonVo personVo = personVos.get(i);
            for (DailyWork dailyWork : planList) {
                if (personVo.ZYH.equals(dailyWork.ZYH)) {
                    planListNew.add(dailyWork);
                    break;
                }
            }
        }


        return planListNew;

    }

    private boolean hasAdded(String zyh, List<DailyWork> needBackDailyWork) {
        for (DailyWork dailyWork : needBackDailyWork) {
            if (dailyWork.ZYH.equals(zyh)) {
                return true;
            }
        }
        return false;
    }

    public List<DailyWork> getChangeAdvice(String brbq, String hsgh, String kzsj, String jgid) throws SQLException, DataAccessException, IOException, ClassNotFoundException {


        String rq = kzsj.substring(0, 10);
        List<DailyWork> changeList = new ArrayList<>();
        BizResponse<SickPersonVo> patientList = getPatientList(brbq, hsgh, jgid);
        List<SickPersonVo> personVos = patientList.datalist;

        List<String> data = new ArrayList<>();
        List<DailyWork> personList = new ArrayList<>();
        if (personVos != null && personVos.size() > 0) {
            for (int i = 0; i < personVos.size(); i++) {
                SickPersonVo personVo = personVos.get(i);
                data.add(personVo.ZYH);

                DailyWork dailyWork = new DailyWork();
                dailyWork.ZYHM = personVo.ZYHM;
                dailyWork.ZYH = personVo.ZYH;
                dailyWork.BRCH = personVo.BRCH;
                dailyWork.BRXM = personVo.BRXM;

                personList.add(dailyWork);
            }
        }
        if (data.size() == 0) {
            data.add("-1");
        }
        keepOrRoutingDateSource(DataSource.HRP);
        changeList = serviceSup.getChangeAdvice(data, rq, jgid);
        for (int i = 0; i < changeList.size(); i++) {
            for (DailyWork patitent : personList) {
                if (changeList.get(i).ZYH.equals(patitent.ZYH)) {
                    changeList.get(i).ZYHM = patitent.ZYHM;
                    changeList.get(i).BRXM = patitent.BRXM;
                    changeList.get(i).BRCH = patitent.BRCH;
                    break;
                }
            }
        }
        List<DailyWork> changeListNew = new ArrayList<>();
        for (int i = 0; i < personVos.size(); i++) {
            SickPersonVo personVo = personVos.get(i);
            for (DailyWork dailyWork : changeList) {
                if (personVo.ZYH.equals(dailyWork.ZYH)) {
                    changeListNew.add(dailyWork);
                    break;
                }
            }
        }
        return changeListNew;
    }


    public List<DailyWork> getInspection(String brbq, String hsgh, String kzsj, String jgid) throws SQLException, DataAccessException, IOException, ClassNotFoundException {


        String rq = kzsj.substring(0, 10);
        List<DailyWork> inspection = new ArrayList<>();
        BizResponse<SickPersonVo> patientList = getPatientList(brbq, hsgh, jgid);
        List<SickPersonVo> personVos = patientList.datalist;

        List<String> data = new ArrayList<>();
        List<DailyWork> personList = new ArrayList<>();
        if (personVos != null && personVos.size() > 0) {
            for (int i = 0; i < personVos.size(); i++) {
                SickPersonVo personVo = personVos.get(i);
                data.add(personVo.ZYHM);

                DailyWork dailyWork = new DailyWork();
                dailyWork.ZYHM = personVo.ZYHM;
                dailyWork.ZYH = personVo.ZYH;
                dailyWork.BRCH = personVo.BRCH;
                dailyWork.BRXM = personVo.BRXM;

                personList.add(dailyWork);
            }
        }
        if (data.size() == 0) {
            data.add("-1");
        }
        keepOrRoutingDateSource(DataSource.LIS);
        List<DailyWork> inspectionTemp = serviceSup.getInspection(data, jgid, null);
        for (int i = 0; i < inspectionTemp.size(); i++) {
            for (DailyWork patitent : personList) {
                if (inspectionTemp.get(i).ZYHM.equals(patitent.ZYHM)) {
                    inspectionTemp.get(i).ZYH = patitent.ZYH;
                    inspectionTemp.get(i).BRXM = patitent.BRXM;
                    inspectionTemp.get(i).BRCH = patitent.BRCH;
                    break;
                }
            }
        }
        //去重复
        for (DailyWork inspectionXX : inspectionTemp) {
            boolean hasAdd = hasAdded(inspectionXX.ZYH, inspection);
            if (!hasAdd) {
                inspection.add(inspectionXX);
            }
        }
        //
        List<DailyWork> inspectionNew = new ArrayList<>();
        for (int i = 0; i < personVos.size(); i++) {
            SickPersonVo personVo = personVos.get(i);
            for (DailyWork dailyWork : inspection) {
                if (personVo.ZYH.equals(dailyWork.ZYH)) {
                    //补充 RYRQ 条件  (KDSJ > RYRQ)
                    boolean cha = false;
                    try {
                        cha = DateCompare.isGreaterThan(dailyWork.KDSJ, personVo.RYRQ);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (cha) {
                        inspectionNew.add(dailyWork);
                    }
                    break;
                }
            }
        }
        return inspectionNew;

    }

    public List<DailyWork> getRisk(String bqid, String hsgh, String jhrq, String jgid) throws SQLException, DataAccessException, IOException, ClassNotFoundException {


        String rq = jhrq.substring(0, 10);
        List<DailyWork> riskList = new ArrayList<>();
        BizResponse<SickPersonVo> patientList = getPatientList(bqid, hsgh, jgid);
        List<SickPersonVo> personVos = patientList.datalist;

        List<String> data = new ArrayList<>();
        List<DailyWork> personList = new ArrayList<>();
        if (personVos != null && personVos.size() > 0) {
            for (int i = 0; i < personVos.size(); i++) {
                SickPersonVo personVo = personVos.get(i);
                data.add(personVo.ZYH);

                DailyWork dailyWork = new DailyWork();
                dailyWork.ZYHM = personVo.ZYHM;
                dailyWork.ZYH = personVo.ZYH;
                dailyWork.BRCH = personVo.BRCH;
                dailyWork.BRXM = personVo.BRXM;

                personList.add(dailyWork);
            }
        }
        if (data.size() == 0) {
            data.add("-1");
        }
        keepOrRoutingDateSource(DataSource.MOB);
        riskList = serviceSup.getRisk(data, rq, jgid);
        for (int i = 0; i < riskList.size(); i++) {
            for (DailyWork patitent : personList) {
                if (riskList.get(i).ZYH.equals(patitent.ZYH)) {
                    riskList.get(i).ZYHM = patitent.ZYHM;
                    riskList.get(i).BRXM = patitent.BRXM;
                    riskList.get(i).BRCH = patitent.BRCH;
                    break;
                }
            }
        }
        return riskList;

    }


    public List<DailyWork> getPatientsByBq(String brbq, String jgid) throws SQLException, DataAccessException {


        keepOrRoutingDateSource(DataSource.HRP);
        return serviceSup.getPatientsByBq(brbq, jgid);

    }

    public BizResponse<SickPersonVo> getPatientList(String bqid, String hsgh, String jgid) throws DataAccessException {
        BizResponse<SickPersonVo> patients = patientMainService.getDeptPatients(bqid, hsgh, jgid);
        return patients;

    }
}
