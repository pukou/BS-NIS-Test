package com.bsoft.nis.service.outcontrol;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.outcontrol.OutControl;
import com.bsoft.nis.domain.outcontrol.OutControlSaveData;
import com.bsoft.nis.domain.patient.db.SickPersonDetailVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.outcontrol.support.OutControllerBaseServiceSup;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by king on 2016/11/16.
 */
@Service
public class OutControllerBaseService extends RouteDataSourceService {

    @Autowired
    OutControllerBaseServiceSup serviceSup;

    @Autowired
    IdentityService identityService;

    @Autowired
    DateTimeService timeService; // 日期时间服务

    /*升级编号【56010038】============================================= start
                   外出管理PDA上只有登记功能，查询需要找到具体的人再查询，不太方便，最好能有一个查询整个病区外出病人的列表
                ================= classichu 2018/3/7 20:04
                */
    @Autowired
    PatientServiceSup mPatientServiceSup; //
    /* =============================================================== end */


    public BizResponse<OutControl> getOutPatientByZyh(String zyh, String brbq, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<OutControl> response = new BizResponse<>();

        try {
            response.datalist = serviceSup.getOutPatientByZyh(zyh, brbq, jgid);
            response.isSuccess = true;
            response.message = "外出病人列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【外出病人列表】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【外出病人列表】服务内部错误";
        }
        return response;
    }

    /*升级编号【56010038】============================================= start
                    外出管理PDA上只有登记功能，查询需要找到具体的人再查询，不太方便，最好能有一个查询整个病区外出病人的列表
                ================= classichu 2018/3/7 19:49
                */
    public BizResponse<OutControl> getAllOurPatients(String brbq, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<OutControl> response = new BizResponse<>();

        try {
            List<OutControl> outControlList = serviceSup.getAllOurPatients(brbq, jgid);
            for (OutControl outControl : outControlList) {
                SickPersonDetailVo sickPersonDetailVo = mPatientServiceSup.getPatientDetail(outControl.ZYH, jgid);
                outControl.BRXM = sickPersonDetailVo.BRXM;
                outControl.BRCH = sickPersonDetailVo.BRCH;
            }
            response.datalist = outControlList;
            response.isSuccess = true;
            response.message = "获取病区所有外出病人列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【获取病区所有外出病人列表】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【获取病区所有外出病人列表】服务内部错误";
        }
        return response;
    }
    /* =============================================================== end */

    public BizResponse<OutControl> getPatientStatus(String zyh, String brbq, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<OutControl> response = new BizResponse<>();

        try {
            response.datalist = serviceSup.getPatientStatus(zyh, brbq, jgid);
            response.isSuccess = true;
            response.message = "病人当前外出列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【病人当前外出列表】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【病人当前外出列表】服务内部错误";
        }
        return response;
    }

    public BizResponse<String> registerOutPatient(OutControlSaveData outControlSaveData) {

        BizResponse<String> response = new BizResponse<>();
        String wcdjsj = timeService.now(DataSource.MOB);

        try {
            if (outControlSaveData != null) {
                Long jlxh = identityService.getIdentityMax("IENR_WCGL", DataSource.MOB);
                keepOrRoutingDateSource(DataSource.MOB);
                serviceSup.registerOutPatient(jlxh, wcdjsj, outControlSaveData.WCSJ,
                        outControlSaveData.WCDJHS, outControlSaveData.YJHCSJ,
                        outControlSaveData.PZYS, outControlSaveData.PTRY,
                        outControlSaveData.ZYH, outControlSaveData.BRBQ,
                        outControlSaveData.JGID, outControlSaveData.WCYY);
            }
            response.isSuccess = true;
            response.message = "病人外出登记成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【病人外出登记】数据库插入错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【病人外出登记】服务内部错误";
        }

        return response;
    }

    public BizResponse<String> registerBackToBed(Long jlxh, String hcdjsj, String hcdjhs,
                                                 String jgid) {

        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<String> response = new BizResponse<>();
        String hcsj = timeService.now(DataSource.MOB);

        try {
            serviceSup.registerBackToBed(jlxh, hcdjhs, hcsj, hcdjsj, jgid);
            response.isSuccess = true;
            response.message = "病人回床登记成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【病人回床登记】数据库插入错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【病人回床登记】服务内部错误";
        }

        return response;
    }

}
