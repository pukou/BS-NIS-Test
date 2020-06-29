package com.bsoft.nis.service.visit;


import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.visit.CheckState;
import com.bsoft.nis.domain.visit.PatrolInterval;
import com.bsoft.nis.domain.visit.VisitCount;
import com.bsoft.nis.domain.visit.VisitHistory;
import com.bsoft.nis.domain.visit.VisitPerson;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.patient.PatientMainService;
import com.bsoft.nis.service.visit.support.VisitBaseServiceSup;
import com.bsoft.nis.util.date.DateConvert;
import com.bsoft.nis.util.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by king on 2016/11/18.
 */
@Service
public class VisitBaseService extends RouteDataSourceService {

    @Autowired
    VisitBaseServiceSup serviceSup;

    @Autowired
    IdentityService identityService;

    @Autowired
    PatientMainService patientMainService;

    @Autowired
    DateTimeService timeService; // 日期时间服务

    public static List<PatrolInterval> pIList = new ArrayList<>();

    public static void init() {
        if (pIList.size() == 0) {
            PatrolInterval p1 = new PatrolInterval();
            p1.HLJB = "0";
            p1.Interval = 30;
            pIList.add(p1);
            PatrolInterval p2 = new PatrolInterval();
            p2.HLJB = "1";
            p2.Interval = 60;
            pIList.add(p2);
            PatrolInterval p3 = new PatrolInterval();
            p3.HLJB = "2";
            p3.Interval = 120;
            pIList.add(p3);
            PatrolInterval p4 = new PatrolInterval();
            p4.HLJB = "3";
            p4.Interval = 180;
            pIList.add(p4);
        }
    }

    /*升级编号【56010027】============================================= start
                         处理房间条码、获取房间病人处理
            ================= classichu 2018/3/22 10:41
            */
    public BizResponse<VisitCount> getRoomPatientList(String ksdm,String fjhm, String jgid) {
        BizResponse<VisitCount> response = new BizResponse<>();

        List<VisitPerson> xylistNowRoom =new ArrayList<>();
        try {
            keepOrRoutingDateSource(DataSource.HRP);
            List<String> ZyhList= serviceSup.getRoomPatientList(fjhm, jgid);
            if (ZyhList!=null&&ZyhList.size()>0){
                //所有需要巡视的病人
                List<VisitPerson> xylistAll = getPatrolPatitent(ksdm, jgid);
                for (VisitPerson visitPerson : xylistAll) {
                    if (ZyhList.contains(visitPerson.ZYH)){
                        xylistNowRoom.add(visitPerson);
                    }
                }
            }
            List<CheckState> xsqk = getPatrolTypeInfo(jgid);
            VisitCount count = new VisitCount();
            count.xsqk = xsqk;
            count.xyxs = xylistNowRoom;
            count.yjxs = null;

            response.data = count;
            response.isSuccess = true;
            response.message = "【巡视病人】获取病人列表!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【巡视病人】数据库查询错误!";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【巡视病人】服务内部错误!";
        }
        return response;
    }
    /* =============================================================== end */

    public BizResponse<VisitHistory> getPatrolHistory(String zyh, String xsrq, String jgid) {

        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<VisitHistory> response = new BizResponse<>();
        try {
            response.datalist = serviceSup.getPatrolHistory(zyh, xsrq, jgid);
            response.isSuccess = true;
            response.message = "巡视记录获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【巡视记录】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【巡视记录】服务内部错误";
        }

        return response;
    }

    /**
     * 删除巡视记录
     * @param xsbs
     * @param urid
     * @param jgid
     * @return
     */
    public BizResponse<String> delPatrol(String xsbs, String urid, String jgid){
        BizResponse<String> response = new BizResponse<>();
        try {
            String xssj = timeService.now(DataSource.MOB);
            Date date = DateConvert.toDateTime(xssj, "yyyy-MM-dd HH:mm:ss");

            int nums = serviceSup.delPatrol(xsbs, urid, date, jgid);

            response.isSuccess = true;
            response.data = "OK";

        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【巡视病人】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【巡视病人】服务内部错误";
        }
        return response;
    }

    public BizResponse<VisitCount> getPatrol(String urid, String ksdm, String jgid) {

        BizResponse<VisitCount> response = new BizResponse<>();

        try {
            keepOrRoutingDateSource(DataSource.HRP);
            List<VisitPerson> listPerson = serviceSup.getPatrol(ksdm, jgid);
            String xssj = timeService.now(DataSource.MOB);
            Date date = DateConvert.toDateTime(xssj, "yyyy-MM-dd HH:mm:ss");
            xssj = DateConvert.getDateYear(date) + "-" + DateConvert.getDateMonth(date) + "-" + DateConvert.getDateDay(date);
            List<String> ZYHCount = new ArrayList<>();
            for (int i = 0; i < listPerson.size(); i++) {
                ZYHCount.add(listPerson.get(i).ZYH);
            }
            if (ZYHCount.size() == 0) {
                ZYHCount.add("-1");
            }
            keepOrRoutingDateSource(DataSource.MOB);
            List<String> patitent = serviceSup.getPatrolZyh(urid, xssj, jgid, ZYHCount);

            List<VisitPerson> yjlist = new ArrayList<>();
            for (VisitPerson person : listPerson) {
                int k = 0;
                for (String zyh : patitent) {
                    if (person.ZYH.equals(zyh)) {
                        break;
                    }
                    k++;
                }
                if (k < patitent.size()) {
                    yjlist.add(person);
                }
            }
            List<VisitPerson> xylist = getPatrolPatitent(ksdm, jgid);
            List<CheckState> xsqk = getPatrolTypeInfo(jgid);

            VisitCount count = new VisitCount();
            count.xsqk = xsqk;
            count.xyxs = xylist;
            count.yjxs = yjlist;

            response.data = count;
            response.isSuccess = true;
            response.message = "巡视病人查询成功！";


        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【巡视病人】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【巡视病人】服务内部错误";
        }
        return response;

    }

    public BizResponse<VisitPerson> setPatrolForScan(String brbq, String urid, String sanStr, String xsqk, String jgid) {

        BizResponse<VisitPerson> response = new BizResponse<>();
        //根据扫描的腕带获取住院号
        BizResponse<String> zyh = patientMainService.getPatientZyhByScan(sanStr);

        try {

            response.datalist = savePatrol(brbq, urid, zyh.data, xsqk, "1", jgid);
            response.isSuccess = true;
            response.message = "扫描巡视成功！";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【扫描巡视】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【扫描巡视】服务内部错误";
        }

        return response;
    }
    public BizResponse<VisitPerson> setPatrol_Some(String brbq, String urid, String zyh, String xsqk, String jgid) {

        BizResponse<VisitPerson> response = new BizResponse<>();

        try {
            response.datalist = savePatrol(brbq, urid, zyh, xsqk, "1", jgid);
            response.isSuccess = true;
            response.message = "房间巡视成功！";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【房间巡视】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【房间巡视】服务内部错误";
        }

        return response;
    }
    public BizResponse<VisitPerson> setPatrol(String isScan,String brbq, String urid, String zyh, String xsqk, String jgid) {

        BizResponse<VisitPerson> response = new BizResponse<>();

        try {
            response.datalist = savePatrol(brbq, urid, zyh, xsqk, isScan, jgid);
            response.isSuccess = true;
            response.message = "日常巡视成功！";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【日常巡视】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【日常巡视】服务内部错误";
        }

        return response;
    }

    public List<VisitPerson> getPatrolPatitent(String ksdm, String jgid) throws SQLException, DataAccessException, IOException, ClassNotFoundException {

        init();
        List<VisitPerson> listPerson2 = null;
        if (pIList.size() > 0) {
            keepOrRoutingDateSource(DataSource.HRP);
            List<VisitPerson> listPerson = serviceSup.getPatrol(ksdm, jgid);
            listPerson2 = new ArrayList<>(listPerson);
            for (int i = 0; i < listPerson.size(); i++) {
                int k = 0;
                for (PatrolInterval pI : pIList) {
                    if (Objects.equals(listPerson.get(i).HLJB, pI.HLJB)) {
                        break;
                    }
                    k++;
                }
                if (k < pIList.size()) {
                    PatrolInterval index = pIList.get(k);
                    keepOrRoutingDateSource(DataSource.MOB);
                    List<String> patitent = serviceSup.getPatrolPatitent(listPerson.get(i).ZYH, jgid, DateUtil.getApplicationDateTime2(), DateUtil.getApplicationDateTimeAfterInterval(index.Interval));
                    if (patitent.size() > 0) {
                        listPerson2.remove(listPerson.get(i));
                    }
                } else {
                    listPerson2.remove(listPerson.get(i));
                }
            }
        }
        return listPerson2;
    }

    public List<CheckState> getPatrolTypeInfo(String jgid) throws SQLException, DataAccessException, IOException, ClassNotFoundException {

        keepOrRoutingDateSource(DataSource.MOB);
        return serviceSup.getPatrolTypeInfo(jgid);
    }

    private List<VisitPerson> savePatrol(String brbq, String urid, String zyh, String xsqk, String smbz, String jgid) throws SQLException, DataAccessException, IOException, ClassNotFoundException {

        Long xsbs = identityService.getIdentityMax("IENR_BRXS",DataSource.MOB);
        String xssj = timeService.now(DataSource.MOB);
        keepOrRoutingDateSource(DataSource.MOB);
        Boolean bz = serviceSup.savePatrol(xsbs, zyh, urid, xssj, xssj, xsqk, smbz, brbq, jgid);
        List<VisitPerson> litDetail = null;
        if (bz) {
            keepOrRoutingDateSource(DataSource.HRP);
            List<VisitPerson> listPatrol = serviceSup.getPatrol2(zyh, jgid);
            keepOrRoutingDateSource(DataSource.MOB);
            litDetail = serviceSup.getPatrolDetail(xsbs, zyh, jgid);
            int k = 0;
            for (VisitPerson detail : litDetail) {
                for (VisitPerson patrol : listPatrol) {
                    if (detail.ZYH.equals(patrol.ZYH)) {
                        litDetail.get(k).BRCH = patrol.BRCH;
                        litDetail.get(k).BRXM = patrol.BRXM;
                    }
                }
                k++;
            }
        }
        return litDetail;
    }
}
