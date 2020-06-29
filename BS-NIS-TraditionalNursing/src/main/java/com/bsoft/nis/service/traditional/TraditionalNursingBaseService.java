package com.bsoft.nis.service.traditional;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.domain.traditional.JSJL;
import com.bsoft.nis.domain.traditional.Traditional_HLFA;
import com.bsoft.nis.domain.traditional.Traditional_SHJS;
import com.bsoft.nis.domain.traditional.Traditional_XGBZ;
import com.bsoft.nis.domain.traditional.Traditional_ZYZZ;
import com.bsoft.nis.domain.traditional.Traditional_ZZFJ;
import com.bsoft.nis.domain.traditional.Traditional_ZZFJJL;
import com.bsoft.nis.domain.traditional.Traditional_ZZJL;
import com.bsoft.nis.domain.traditional.ZZJL_PF;
import com.bsoft.nis.domain.traditional._HLJS;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.patient.PatientMainService;
import com.bsoft.nis.service.traditional.support.TraditionalNursingBaseServiceSup;
import com.bsoft.nis.util.date.birthday.BirthdayUtil;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Classichu on 2017/11/16.
 * 中医护理
 */
@Service
public class TraditionalNursingBaseService extends RouteDataSourceService {

    @Autowired
    TraditionalNursingBaseServiceSup serviceSup;

    @Autowired
    IdentityService identityService;

    @Autowired
    PatientMainService patientService;

    @Autowired
    DateTimeService timeService; // 日期时间服务

    public BizResponse<Traditional_ZZJL> getZYZZJL(String zyh, String brbq, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<Traditional_ZZJL> response = new BizResponse<>();
        try {
            List<Traditional_ZZJL> traditional_zyzzList = serviceSup.getZYZZJL(zyh, brbq, jgid);
            String brnl = "";
            BizResponse<Patient> bizResponse = patientService.getPatientByZyh(zyh);
            String nowDateStr = timeService.getNowDateTimeStr(DataSource.PORTAL);
            if (bizResponse.isSuccess) {
                if (bizResponse.data != null && !TextUtils.isEmpty(bizResponse.data.CSNY)) {
//                    brnl = BirthdayUtil.getAgesCommon(bizResponse.data.CSNY,nowDateStr);
                    brnl = BirthdayUtil.getAgesPairCommonStrSimple(bizResponse.data.CSNY, nowDateStr);
                }
            }
            for (Traditional_ZZJL zzjl : traditional_zyzzList) {
                keepOrRoutingDateSource(DataSource.HRP);
                List<String> zyzdList = serviceSup.getZY_RYZD(zzjl.ZYH);
                StringBuilder stringBuilder = new StringBuilder();
                for (String zyzd : zyzdList) {
                    stringBuilder.append(zyzd);
                    stringBuilder.append(",");
                }
                String result = stringBuilder.toString();
                result = result.endsWith(",") ? result.substring(0, result.length() - 1) : result;
                zzjl.ZYZDMC = result;
                //
                zzjl.BRNL = brnl;
            }
            response.datalist = traditional_zyzzList;
            response.isSuccess = true;
            response.message = "中医护理主要症状记录列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【中医护理主要症状记录】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【中医护理主要症状记录】服务内部错误";
        }
        return response;
    }


    public BizResponse<JSJL> getZYSHJSJL(String zyh, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<JSJL> response = new BizResponse<>();
        try {
            List<JSJL> traditional_zyzzList = serviceSup.getZYSHJSJL(zyh, jgid);
            response.datalist = traditional_zyzzList;
            response.isSuccess = true;
            response.message = "中医护理施护技术记录列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【中医护理施护技术记录列表】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【中医护理施护技术记录列表】服务内部错误";
        }
        return response;
    }

    public BizResponse<Traditional_ZZFJ> getZYZZFJ(String zzbh) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<Traditional_ZZFJ> response = new BizResponse<>();
        try {
            List<Traditional_ZZFJ> traditional_zyzzList = serviceSup.getZYZZFJ(zzbh);
            response.datalist = traditional_zyzzList;
            response.isSuccess = true;
            response.message = "中医护理症状分级列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【中医护理症状分级列表】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【中医护理症状分级列表】服务内部错误";
        }
        return response;
    }


    public BizResponse<Traditional_HLFA> getZY_HLFA(String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<Traditional_HLFA> response = new BizResponse<>();
        try {
            List<Traditional_HLFA> traditional_zyzzList = serviceSup.getZY_HLFA(jgid);
            response.datalist = traditional_zyzzList;
            response.isSuccess = true;
            response.message = "中医护理护理方案列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【中医护理护理方案列表】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【中医护理护理方案列表】服务内部错误";
        }
        return response;
    }

    public BizResponse<Traditional_ZYZZ> getZY_ZYZZ(String fabh) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<Traditional_ZYZZ> response = new BizResponse<>();
        try {
            List<Traditional_ZYZZ> traditional_zyzzList = serviceSup.getZY_ZYZZ(fabh);
            response.datalist = traditional_zyzzList;
            response.isSuccess = true;
            response.message = "中医护理主要症状列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【中医护理主要症状列表】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【中医护理主要症状列表】服务内部错误";
        }
        return response;
    }

    public BizResponse<Traditional_SHJS> getZY_SHJS(String zzbh) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<Traditional_SHJS> response = new BizResponse<>();
        try {
            List<Traditional_SHJS> traditional_zyzzList = serviceSup.getZY_SHJS(zzbh);
            response.datalist = traditional_zyzzList;
            response.isSuccess = true;
            response.message = "中医护理施护技术列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【中医护理施护技术列表】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【中医护理施护技术列表】服务内部错误";
        }
        return response;
    }


    public BizResponse<_HLJS> getZY_YZJHbyZZBH(String zyh, String jgid, String zzbh) {
        String nowDateStr = timeService.getNowDateStr(DataSource.PORTAL);
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<_HLJS> response = new BizResponse<>();
        try {
            // List<_HLJS> hljsList = serviceSup.getZY_YZJHbyZZBH(zyh, jgid, zzbh,nowDateStr);
            List<_HLJS> hljsList = serviceSup.getZY_YZJHbyZZBH(zyh, jgid, zzbh, null);
            response.datalist = hljsList;
            response.isSuccess = true;
            response.message = "中医护理施护技术列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【中医护理施护技术列表】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【中医护理施护技术列表】服务内部错误";
        }
        return response;
    }

    public BizResponse<String> saveZY_JSJL(JSJL jsjl) {

        BizResponse<String> response = new BizResponse<>();
        try {

            /**插入施护技术记录表 */
            String jlxh = String.valueOf(identityService.getIdentityMax("IENR_ZY_JSJL", DataSource.MOB));
            String dateTimeStr = timeService.getNowDateTimeStr(DataSource.PORTAL);
            String dateStr = timeService.getNowDateStr(DataSource.PORTAL);
            keepOrRoutingDateSource(DataSource.MOB);
            jsjl.JSJL = jlxh;
            jsjl.CZSJ = dateTimeStr;
            jsjl.CZRQ = dateStr;
            Integer add = serviceSup.insertZY_JSJL(jsjl);

            response.data = "保存成功";
            response.isSuccess = true;
            response.message = "保存成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【中医施护技术保存】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【中医施护技术保存】服务内部错误";
        }
        return response;
    }

    public BizResponse<String> saveZYPJInfo(ZZJL_PF zzjl_pf) {

        BizResponse<String> response = new BizResponse<>();
        try {
            /**插入症状分级记录表*/
            String jlxh = String.valueOf(identityService.getIdentityMax("IENR_ZY_ZZFJJL", DataSource.MOB));
            //String cjsj = timeService.now(DataSource.MOB);
            keepOrRoutingDateSource(DataSource.MOB);
            Traditional_ZZFJJL zzfjjl = new Traditional_ZZFJJL();
            zzfjjl.JLXH = jlxh;
            zzfjjl.ZZJL = zzjl_pf.zzjl;
            zzfjjl.ZZFJ = zzjl_pf.zzfj;
            zzfjjl.FJPF = zzjl_pf.fjpf;
            zzfjjl.PFGH = zzjl_pf.pfgh;
            // zzfjjl.ZFBZ
            zzfjjl.PFSJ = zzjl_pf.pfsj;
            Integer add = serviceSup.insertZY_ZZFJJL(zzfjjl);

            String ssqpf = zzjl_pf.ssqpf;

            /**更新症状分级记录表*/
            String sshjl = jlxh;
            String sshfj = zzjl_pf.zzfj;
            String sshpf = zzjl_pf.fjpf;
            String sshsj = zzjl_pf.pfsj;
            String zzbh = zzjl_pf.zzbh;
            String fajl = zzjl_pf.fajl;
            String hlxg = null;
            int fscz = Integer.valueOf(ssqpf) - Integer.valueOf(sshpf);
            List<Traditional_XGBZ> xgbzList = serviceSup.getZY_XGBZ(zzjl_pf.zzbh, String.valueOf(fscz));
            if (xgbzList != null && !xgbzList.isEmpty()) {
                hlxg = xgbzList.get(0).XGDM;
            }
            Integer edit = serviceSup.updateZY_ZZJL_PF(sshjl, sshfj, sshpf, sshsj, zzbh, fajl, hlxg);

            response.data = "评价成功";
            response.isSuccess = true;
            response.message = "保存成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【中医评价保存】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【中医评价保存】服务内部错误";
        }
        return response;
    }

}
