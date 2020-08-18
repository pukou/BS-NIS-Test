package com.bsoft.nis.service.patient;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.servicesup.support.SystemParamServiceSup;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.patient.*;
import com.bsoft.nis.domain.patient.db.*;
import com.bsoft.nis.mapper.patient.PatientMapper;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
import com.bsoft.nis.util.date.DateUtil;
import com.bsoft.nis.util.date.birthday.BirthdayUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 病人主服务
 * Created by dragon on 2016/10/10.
 */
@Service
public class PatientMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(PatientMainService.class);

    @Autowired
    PatientMapper mapper;

    @Autowired
    PatientServiceSup service; // 病人服务
    @Autowired
    DictCachedHandler handler; // 缓存处理器
    @Autowired
    DateTimeService timeService;// 日期服务
    @Autowired
    SystemParamServiceSup systemParamService; // 用户参数服务

    @Autowired
    IdentityService identityService;//种子表服务

    /**
     * 测试使用
     *
     * @return
     */
    public List<Patient> getPatientList() {
        List list = new ArrayList();

        Patient patient = new Patient();
        patient.userId = "0";
        patient.userName = "邢海龙";

        Patient patient1 = new Patient();
        patient1.userId = "1";
        patient1.userName = "文字";
        list.add(patient);
        list.add(patient1);
        return list;
    }

    /**
     * 获取病人列表
     *
     * @param bqid      科室代码
     * @param type      0病区病人;1体温单 ;2 医嘱;3口服单;4注射单;5 输液单;6血糖
     * @param starttime
     * @param endtime
     * @param hsgh
     * @param jgid      机构ID
     * @return
     */
    public BizResponse<SickPersonVo> getPatientList(String bqid, int type, int starttime,
                                                    int endtime, String hsgh, String jgid) {
        BizResponse<SickPersonVo> response = new BizResponse<>();
        switch (type) {
            case 0: // 病区病人(我的病人)
                response = getDeptPatients(bqid, hsgh, jgid);
                break;
            case 1: // 体温单病人
                response.isSuccess = false;
                response.message = "服务未实现";
                break;
            case 2: // 医嘱
                response.isSuccess = false;
                response.message = "服务未实现";
                break;
            case 3: // 口服单
                response.isSuccess = false;
                response.message = "服务未实现";
                break;
            case 4: // 注射单
                response.isSuccess = false;
                response.message = "服务未实现";
                break;
            case 5: // 输液单
                response.isSuccess = false;
                response.message = "服务未实现";
                break;
            case 6: // 血糖
                response.isSuccess = false;
                response.message = "服务未实现";
                break;
            case 1000: // 手术科室
                String ssks = bqid;
                response = getPatientsForSSKS(ssks, hsgh, jgid);
                break;
            default:
                response.isSuccess = false;
                response.message = "服务不支持该类型";
        }
        return response;
    }

    public BizResponse<SickPersonVo> getDeptPatients(String bqid, String hsgh, String jgid) {
        BizResponse<SickPersonVo> response;
        if (StringUtils.isBlank(hsgh)) {
            response = getPatientsForDept(bqid, jgid);
        } else {
            response = getMyPatientList(bqid, hsgh, jgid);
        }
        return response;
    }

    public BizResponse<ZKbean> getFXZKRecord(List<String> zyhList, String jgid) {
        BizResponse<ZKbean> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            List<ZKbean> deRecordVoList = service.getFXZKRecord(zyhList, jgid);

            response.isSuccess = true;
            response.datalist = deRecordVoList;
            response.message = "获取风险评估质控成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取风险评估质控]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取风险评估质控]服务内部错误";
        }
        return response;
    }

    public BizResponse<RecondBean> getLastDERecord(List<String> zyhList, String jgid) {
        BizResponse<RecondBean> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            List<RecondBean> deRecordVoList = service.getLastDERecord(zyhList, jgid);

            response.isSuccess = true;
            response.datalist = deRecordVoList;
            response.message = "获取最近一次风险评估记录集合成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取最近一次风险评估记录集]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取最近一次风险评估记录集]服务内部错误";
        }
        return response;
    }

    public BizResponse<BCRYBean> getGroupRYList(String bqid, String ygdm, String jgid) {
        BizResponse<BCRYBean> response = new BizResponse<>();
        try {

            List<BCRYBean> bcryList = service.getGroupRYList(ygdm, bqid);
            response.datalist = bcryList;
            response.isSuccess = true;
            response.message = "病人责任组获取成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[病人责任组]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[病人责任组]服务内部错误!";
        }
        return response;
    }

    public BizResponse<String> saveGroupRYList(List<BCRYBean> items, String ygdm, String bqdm) {
        BizResponse<String> response = new BizResponse<>();
        try {

            for (int i = 0; i < items.size(); i++) {
                items.get(i).JLXH = String.valueOf(identityService.getIdentityMax("IENR_BCRY", DataSource.MOB));
            }
            Integer temp = service.saveGroupRYList(items, ygdm, bqdm);
//            response.datalist =bcryList;
            response.isSuccess = true;
            response.message = "保存病人责任组信息成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存病人责任组]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存病人责任组]服务内部错误!";
        }
        return response;
    }

    public BizResponse<BCSZBean> getGroupCfgList(String bqdm, String ygdm, String jgid) {
        BizResponse<BCSZBean> response = new BizResponse<>();
        try {
            List<BCSZBean> bsczList = service.getGroupCfgList(bqdm, jgid);
            for (BCSZBean bccwBean : bsczList) {
                bccwBean.bccwBeans = service.getGroupCWList(bccwBean.BCBH);
                //寻找 选中
                List<BCRYBean> bcryBeanList = service.getGroupRYList(ygdm, bqdm);
                if (bcryBeanList != null) {
                    for (BCRYBean bcryBean : bcryBeanList) {
                        if (bcryBean.BCBH.equals(bccwBean.BCBH)) {
                            bccwBean.selected = true;
                        }
                    }
                }
            }
            response.datalist = bsczList;
            response.isSuccess = true;
            response.message = "病人责任组设置获取成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[病人责任组设置]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[病人责任组设置]服务内部错误!";
        }
        return response;
    }

    /**
     * 根据病区ID获取所有病人列表
     *
     * @param bqid
     * @param jgid
     * @return
     */
    public BizResponse<SickPersonVo> getPatientsForDept(String bqid, String jgid) {
        BizResponse<SickPersonVo> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.HRP);
        try {
            response.datalist = service.getPatientsForDept(bqid, jgid);
            List<String> zyhList = new ArrayList<>();
            for (int i = 0; i < response.datalist.size(); i++) {
                SickPersonVo vo = response.datalist.get(i);
                zyhList.add(vo.ZYH);
            }
           /*协和 # if (!zyhList.isEmpty()) {
                //查病人诊断
                List<ZDJB> list = mapper.getPatientJBZD(zyhList);
                //
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < response.datalist.size(); i++) {
                        SickPersonVo vo = response.datalist.get(i);
                        String[] zdmc_lbArr = findBRZDMC(vo.ZYH, list);
                        vo.BRZDMC = zdmc_lbArr[0];
                        vo.BRZDLB = zdmc_lbArr[1];
                    }
                }
            }*/
            // 入院诊断
            if (!zyhList.isEmpty()) {
                List<PatientDiagonosis> diagonoses = service.getPatientDiagnoseFromRYZDList(zyhList);
                if (diagonoses != null && !diagonoses.isEmpty()) {
                    for (int i = 0; i < response.datalist.size(); i++) {
                        SickPersonVo vo = response.datalist.get(i);
                        PatientDiagonosis patientDiagonosis = findRYZD(vo.ZYH, diagonoses);
                        if (patientDiagonosis!=null) {
                            vo.BRZDMC = patientDiagonosis.JBMC;
                        }
                    }
                }
            }

            //设置临床路径
            for (SickPersonVo vo : response.datalist) {
                //
                String LCLJ_ZT = null;
                Integer count = mapper.getPatientBRLJCount_OnWay(vo.ZYH);
                if (count <= 0) {
                    count = mapper.getPatientBRLJCount_OutWay(vo.ZYH);
                    if (count <= 0) {
                        /*no_op*/
                    } else {
                        LCLJ_ZT = "2";//已出径
                    }
                } else {
                    LCLJ_ZT = "1";//在临床路径
                }
                vo.LCLJ = LCLJ_ZT;
            }
            //病人血型
            for (SickPersonVo vo : response.datalist) {
                if (!TextUtils.isBlank(vo.BRXX)) {
                    if ("0".equals(vo.BRXX)) {
                        vo.BRXX = null;
                    } else {
                        List<String> tempList = service.getPatientDMMCFromMOB_DMZD(vo.BRXX, "21");
                        if (tempList != null && tempList.size() > 0) {
                            vo.BRXX = tempList.get(0);
                        }
                    }

                }
            }
            //主诊医生
            for (SickPersonVo vo : response.datalist) {
                if (!TextUtils.isBlank(vo.ZZYS)) {
                    vo.ZZYS = handler
                            .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, vo.ZZYS);

                }
            }
            //病人年龄
//            String nowTime = DateConvert.getStandardString(timeService.now(DataSource.HRP));
            for (SickPersonVo vo : response.datalist) {
                if (!TextUtils.isBlank(vo.CSNY)) {
                    vo.BRNL = BirthdayUtil.getAgesPairCommonStrSimple(vo.CSNY, vo.RYRQ);
                }
            }
            //赋值风险评估
            BizResponse<RecondBean> deRecordVoBizResponse = getLastDERecord(zyhList, jgid);
            if (deRecordVoBizResponse.isSuccess) {
                List<RecondBean> deRecordVoList = deRecordVoBizResponse.datalist;
                for (SickPersonVo vo : response.datalist) {
                    vo.recondBeanList4Sicker = findFxList(vo.ZYH, deRecordVoList);
                }
            }
            //赋值质控提醒
            BizResponse<ZKbean> fdP = getFXZKRecord(zyhList, jgid);
            if (fdP.isSuccess) {
                List<ZKbean> deRecordVoList = fdP.datalist;
                for (SickPersonVo vo : response.datalist) {
                    vo.zKbeanList4Sicker = findZKList(vo.ZYH, deRecordVoList);
                }
            }
            //过敏药物标示
            for (SickPersonVo vo : response.datalist) {
                // 过敏药物
                List<AllergicDrug> allergicDrugs = service.getPatientAllergicDrugs(vo.ZYH, jgid);
                vo.hasGMYP = allergicDrugs != null && !allergicDrugs.isEmpty();
            }

            response.isSuccess = true;
            response.message = "病人列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[病区病人列表]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[病区病人列表]服务内部错误!";
        }
        return response;
    }

    private List<ZKbean> findZKList(String zyh, List<ZKbean> deqcRemindVoList) {
        if (deqcRemindVoList == null || deqcRemindVoList.isEmpty()) {
            return null;
        }
        List<ZKbean> deqcRemindVoList4Sicker = new ArrayList<>();
        for (int i = 0; i < deqcRemindVoList.size(); i++) {
            if (zyh.equals(deqcRemindVoList.get(i).ZYH)) {
                deqcRemindVoList4Sicker.add(deqcRemindVoList.get(i));
            }
        }
        return deqcRemindVoList4Sicker;
    }

    private List<RecondBean> findFxList(String zyh, List<RecondBean> deRecordVoList) {
        if (deRecordVoList == null || deRecordVoList.isEmpty()) {
            return null;
        }
        List<RecondBean> deRecordVoList4Sicker = new ArrayList<>();
        for (int i = 0; i < deRecordVoList.size(); i++) {
            if (zyh.equals(deRecordVoList.get(i).ZYH)) {
                deRecordVoList4Sicker.add(deRecordVoList.get(i));
            }
        }
        return deRecordVoList4Sicker;
    }
    private PatientDiagonosis findRYZD(String zyh,  List<PatientDiagonosis> patientDiagonoses) {
        for (PatientDiagonosis patientDiagonose : patientDiagonoses) {
            if (patientDiagonose.ZYH.equals(zyh)){
                return patientDiagonose;
            }
        }
        return null;
    }
    private String[] findBRZDMC(String zyh, List<ZDJB> list) {
        String lb = "1";
        String zdmc = gainZDMC(zyh, list, "出院诊断");
        if (zdmc == null || "".equals(zdmc)) {
            zdmc = gainZDMC(zyh, list, "当前诊断");
            lb = "2";
        }
        if (zdmc == null || "".equals(zdmc)) {
            zdmc = gainZDMC(zyh, list, "入院诊断");
            lb = "3";
        }
        if (zdmc == null || "".equals(zdmc)) {
            zdmc = gainZDMC(zyh, list, "初步诊断");
            lb = "4";
        }
        if (zdmc == null || "".equals(zdmc)) {
            zdmc = "";
            lb = "0";
        }
        return new String[]{zdmc, lb};
    }

    private String gainZDMC(String zyh, List<ZDJB> list, String zdlb) {
        if (zyh == null || "".equals(zyh)) {
            return null;
        }
        if (zdlb == null || "".equals(zdlb)) {
            return null;
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        String zdmc = null;
        for (ZDJB zdjb : list) {
            if (zyh.equals(zdjb.ZYH) && zdlb.equals(zdjb.ZDLB)) {
                zdmc = zdjb.ZDMC;
            }
        }
        return zdmc;
    }

    /**
     * 根据手术科室 获取所有病人列表
     *
     * @param ssks
     * @param jgid
     * @return
     */
    public BizResponse<SickPersonVo> getPatientsForSSKS(String ssks, String hsgh, String jgid) {
        BizResponse<SickPersonVo> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.HRP);
        try {
            response.datalist = service.getPatientsForSSKS(ssks, hsgh, jgid);
            response.isSuccess = true;
            response.message = "手术科室病人列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[手术科室病人列表]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[手术科室病人列表]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取我的病人
     *
     * @param bqid
     * @param hsgh
     * @param jgid
     * @return
     */
    public BizResponse<SickPersonVo> getMyPatientList(String bqid, String hsgh, String jgid) {
        BizResponse<SickPersonVo> response = new BizResponse<>();
        try {
            /* 2018-10-19 田孝鸣 获取我的病人删除无用代码
            原流程：从IENR_WDBR表中获取，从his中获取（没意义）；
            新流程：班次设置对应班次床位，班次人员对应班次设置

            // 判断是否启用HIS我的病人管理
            Boolean bool;
            keepOrRoutingDateSource(DataSource.MOB);
            List<String> csqzList = systemParamService.getParamsInMOB("1", "IENR", "IENR_F_P_Patient", jgid);
            if (csqzList != null && !csqzList.isEmpty()) {
                String csqz = csqzList.get(0);
                bool = "1".equals(csqz);
            } else {
                bool = false;
            }
            if (bool) {
                // 从HIS获取我的病人
                keepOrRoutingDateSource(DataSource.HRP);
                myPatientList = service.getMyPatientHIS(bqid, hsgh, jgid);
            } else {
                // 从MOB获取我的病人
                keepOrRoutingDateSource(DataSource.MOB);
                myPatientList = service.getMyPatientMOB(bqid, hsgh, jgid);
                // 根据BRCH获取HIS中的ZYH
                keepOrRoutingDateSource(DataSource.HRP);
                for (SickPersonVo patient : myPatientList) {
                    List<SickPersonVo> sickPersonVoListTemp = service.getPatientByBrchHIS(bqid, patient.BRCH, jgid);
                    if (sickPersonVoListTemp != null && !sickPersonVoListTemp.isEmpty()) {
                        for (SickPersonVo sickPersonVoTemp : sickPersonVoListTemp) {
                            if ("0".equals(sickPersonVoTemp.CYPB)) {
                                patient.ZYH = sickPersonVoTemp.ZYH;
                                patient.ZYHM = sickPersonVoTemp.ZYHM;
                                break;
                            }
                        }
                    }
                }
            }
            */
           /* List<String> zyhList = new ArrayList<>();
            for (SickPersonVo patient : myPatientList) {
                if (patient.ZYH != null && !"".equals(patient.ZYH)) {
                    zyhList.add(patient.ZYH);
                }
            }*/
            // 获取病区全部病人
            keepOrRoutingDateSource(DataSource.HRP);
            List<SickPersonVo> allPatient = service.getPatientsForDept(bqid, jgid);
            // 过滤出我的病人
            List<SickPersonVo> myPatientList = new ArrayList<>();
         /* ##### 原写法  for (SickPersonVo patient : allPatient) {
                if (zyhList.contains(patient.ZYH)) {
                    myPatientList.add(patient);
                }
            }*/
            //协和
            List<BCSZBean> bcszBeanList = new ArrayList<>();
            BizResponse<BCSZBean> bcszBeansListReq = getGroupCfgList(bqid, hsgh, jgid);
            if (bcszBeansListReq.isSuccess) {
                bcszBeanList = bcszBeansListReq.datalist;
            }
            List<String> brchList = new ArrayList<>();
            if (bcszBeanList != null) {
                for (BCSZBean bcszBean : bcszBeanList) {
                    if (bcszBean.selected && bcszBean.bccwBeans != null) {
                        for (BCCWBean bccwBean : bcszBean.bccwBeans) {
                            brchList.add(bccwBean.CWHM);
                        }
                    }
                }
            }
            if (!brchList.isEmpty()) {
                for (SickPersonVo patient : allPatient) {
                    if (brchList.contains(patient.BRCH)) {
                        myPatientList.add(patient);
                    }

                }
            }
            //
            response.datalist = myPatientList;
            //
            List<String> zyhList = new ArrayList<>();
            for (SickPersonVo patient : myPatientList) {
                if (patient.ZYH != null && !"".equals(patient.ZYH)) {
                    zyhList.add(patient.ZYH);
                }
            }
            //
            // 入院诊断
            if (!zyhList.isEmpty()) {
                List<PatientDiagonosis> diagonoses = service.getPatientDiagnoseFromRYZDList(zyhList);
                if (diagonoses != null && !diagonoses.isEmpty()) {
                    for (int i = 0; i < response.datalist.size(); i++) {
                        SickPersonVo vo = response.datalist.get(i);
                        PatientDiagonosis patientDiagonosis = findRYZD(vo.ZYH, diagonoses);
                        if (patientDiagonosis!=null) {
                            vo.BRZDMC = patientDiagonosis.JBMC;
                        }
                    }
                }
            }

            /*协和
            if (!zyhList.isEmpty()) {
                //查病人诊断
                List<ZDJB> list = mapper.getPatientJBZD(zyhList);
                //
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < response.datalist.size(); i++) {
                        SickPersonVo vo = response.datalist.get(i);
                        String[] zdmc_lbArr = findBRZDMC(vo.ZYH, list);
                        vo.BRZDMC = zdmc_lbArr[0];
                        vo.BRZDLB = zdmc_lbArr[1];
                    }
                }
            }*/
            //设置临床路径
            for (SickPersonVo vo : response.datalist) {
                //
                String LCLJ_ZT = null;
                Integer count = mapper.getPatientBRLJCount_OnWay(vo.ZYH);
                if (count <= 0) {
                    count = mapper.getPatientBRLJCount_OutWay(vo.ZYH);
                    if (count <= 0) {
                        /*no_op*/
                    } else {
                        LCLJ_ZT = "2";//已出径
                    }
                } else {
                    LCLJ_ZT = "1";//在临床路径
                }
                vo.LCLJ = LCLJ_ZT;
            }
            //病人血型
            for (SickPersonVo vo : response.datalist) {
                if (!TextUtils.isBlank(vo.BRXX)) {
                    if ("0".equals(vo.BRXX)) {
                        vo.BRXX = null;
                    } else {
                        List<String> tempList = service.getPatientDMMCFromMOB_DMZD(vo.BRXX, "21");
                        if (tempList != null && tempList.size() > 0) {
                            vo.BRXX = tempList.get(0);
                        }
                    }

                }
            }
            //主诊医生
            for (SickPersonVo vo : response.datalist) {
                if (!TextUtils.isBlank(vo.ZZYS)) {
                    vo.ZZYS = handler
                            .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, vo.ZZYS);

                }
            }
            //病人年龄
//            String nowTime = DateConvert.getStandardString(timeService.now(DataSource.HRP));
            for (SickPersonVo vo : response.datalist) {
                if (!TextUtils.isBlank(vo.CSNY)) {
                    vo.BRNL = BirthdayUtil.getAgesPairCommonStrSimple(vo.CSNY, vo.RYRQ);
                }
            }
            //赋值风险评估
            BizResponse<RecondBean> deRecordVoBizResponse = getLastDERecord(zyhList, jgid);
            if (deRecordVoBizResponse.isSuccess) {
                List<RecondBean> deRecordVoList = deRecordVoBizResponse.datalist;
                for (SickPersonVo vo : response.datalist) {
                    vo.recondBeanList4Sicker = findFxList(vo.ZYH, deRecordVoList);
                }
            }
            //赋值质控提醒
            BizResponse<ZKbean> fdP = getFXZKRecord(zyhList, jgid);
            if (fdP.isSuccess) {
                List<ZKbean> deRecordVoList = fdP.datalist;
                for (SickPersonVo vo : response.datalist) {
                    vo.zKbeanList4Sicker = findZKList(vo.ZYH, deRecordVoList);
                }
            }
            //过敏药物标示
            for (SickPersonVo vo : response.datalist) {
                // 过敏药物
                List<AllergicDrug> allergicDrugs = service.getPatientAllergicDrugs(vo.ZYH, jgid);
                vo.hasGMYP = allergicDrugs != null && !allergicDrugs.isEmpty();
            }


            response.isSuccess = true;
            response.message = "我的病人列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[我的病人列表获取失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[我的病人列表获取失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取病区简要病人信息列表
     *
     * @param bqid
     * @param jgid
     * @return
     */
    public BizResponse<Patient> getSimplePatientsForDept(String bqid, String jgid) {
        BizResponse<Patient> response = new BizResponse<>();
        String nowStr = timeService.now(DataSource.PORTAL);
        keepOrRoutingDateSource(DataSource.HRP);
        try {
            response.datalist = service.getSimplePatientsForDept(bqid, jgid, nowStr);
            response.isSuccess = true;
            response.message = "病人列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[病区病人列表]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[病区病人列表]服务内部错误!";
        }
        return response;
    }

    /**
     * 根据病人住院号码获取病人信息
     *
     * @param zyhm
     * @return
     */
    public BizResponse<Patient> getPatient(String zyhm) {
        keepOrRoutingDateSource(DataSource.HRP);
        BizResponse<Patient> bizResponse = new BizResponse<Patient>();
        Patient patient = mapper.getPatient(zyhm);
        bizResponse.isSuccess = true;
        bizResponse.data = patient;
        return bizResponse;
    }

    /**
     * 根据病人住院号码获取病人信息
     *
     * @param zyhm
     * @return
     */
    public BizResponse<Patient> getPatient(String zyhm, String jgid) {
        keepOrRoutingDateSource(DataSource.HRP);
        BizResponse<Patient> bizResponse = new BizResponse<Patient>();
        Patient patient = mapper.getPatientByZyhmAndJg(zyhm, jgid);
        bizResponse.isSuccess = true;
        bizResponse.data = patient;
        return bizResponse;
    }

    /**
     * 根据病人住院号获取病人信息
     *
     * @param zyh
     * @return
     */
    public BizResponse<Patient> getPatientByZyh(String zyh) {
        keepOrRoutingDateSource(DataSource.HRP);
        BizResponse<Patient> bizResponse = new BizResponse<Patient>();
        Patient patient = mapper.getPatientByZyh(zyh);
        bizResponse.isSuccess = true;
        bizResponse.data = patient;
        return bizResponse;
    }

    /**
     * 根据病人住院号获取病人信息
     *
     * @param zyh
     * @return
     */
    public BizResponse<Patient> getPatientByZyhForEvalution(String zyh) {
        keepOrRoutingDateSource(DataSource.HRP);
        BizResponse<Patient> bizResponse = new BizResponse<Patient>();
        Patient patient = mapper.getPatientByZyhForEvalution(zyh);
        bizResponse.isSuccess = true;
        bizResponse.data = patient;
        return bizResponse;
    }

    /**
     * 根据病人腕带获取病人住院号
     *
     * @param scanStr
     * @return
     */
    public BizResponse<String> getPatientZyhByScan(String scanStr) {
        keepOrRoutingDateSource(DataSource.HRP);
        BizResponse<String> response = new BizResponse<>();

        try {
            response.data = service.getPatientZyhByScan(scanStr);
            response.isSuccess = true;
            response.message = "获取住院号成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[腕带获取住院号]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[腕带获取住院号]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取病人详情
     *
     * @param zyh
     * @param jgid
     * @return
     */
      /*
           升级编号【56010016】============================================= start
           病人列表：重整病人信息，确定哪些信息需要演示
           ================= Classichu 2017/10/18 9:34
           */
    public BizResponse<PatientDetailResponse> getPatientDetail(String zyh, String jgid) {
        BizResponse<PatientDetailResponse> response = new BizResponse<>();
        PatientDetailResponse patientDetailResponse = new PatientDetailResponse();

        try {
            keepOrRoutingDateSource(DataSource.HRP);
            // 1.病人详细信息
            SickPersonDetailVo detailVo = service.getPatientDetail(zyh, jgid);
            /*detailVo.XZMC = handler
                    .getValueByKeyFromCached(CachedDictEnum.MOB_BRXZ, jgid, detailVo.BRXZ);*/
            detailVo.KSMC = handler
                    .getValueByKeyFromCached(CachedDictEnum.MOB_KSDM, jgid, detailVo.BRKS);
            detailVo.YSMC = handler
                    .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, detailVo.ZZYS);

            //饮食代码
            if (!TextUtils.isBlank(detailVo.YSDM)) {
                List<String> tempList = service.getPatientDMMCFromMOB_DMZD(detailVo.YSDM, "20");
                if (tempList != null && tempList.size() > 0) {
                    detailVo.YSDM = tempList.get(0);
                }
            }
            //饮食代码 协和
        /*    if (!TextUtils.isEmpty(detailVo.ZYH)) {
                detailVo.YSDM = service.getPatientYSMC(detailVo.ZYH, jgid);
            }*/

            //病人血型
            if (!TextUtils.isBlank(detailVo.BRXX)) {
                if ("0".equals(detailVo.BRXX)) {
                    detailVo.BRXX = null;
                } else {
                    List<String> tempList = service.getPatientDMMCFromMOB_DMZD(detailVo.BRXX, "21");
                    if (tempList != null && tempList.size() > 0) {
                        detailVo.BRXX = tempList.get(0);
                    }
                }

            }
              /*  if (detailVo.BRXX==null||"0".equals(detailVo.BRXX)){
                    detailVo.BRXX="未提供";
                }else {
                    List<String>  tempList= service.getPatientDMMCFromMOB_DMZD(detailVo.BRXX,"21");
                    if (tempList!=null&&tempList.size()>0){
                        detailVo.BRXX=tempList.get(0);
                    }
                }*/
            //病人年龄
//            String nowTime = DateConvert.getStandardString(timeService.now(DataSource.HRP));
            if (!TextUtils.isBlank(detailVo.CSNY)) {
                detailVo.BRNL = BirthdayUtil.getAgesPairCommonStrSimple(detailVo.CSNY, detailVo.RYRQ);
            }
            //病人性质
            if (!TextUtils.isBlank(detailVo.BRXZ)) {
                List<String> brxzlist = service.getPatientBRXZFromMOB(detailVo.BRXZ);
                if (brxzlist != null && brxzlist.size() > 0) {
                    detailVo.BRXZ = brxzlist.get(0);
                }
            }
            patientDetailResponse.setPatient(detailVo);
            // 2.费用信息
            ExpenseTotal total1 = service.getPatientPayMoney(zyh, jgid);
            if (total1 == null) total1 = new ExpenseTotal();
            if (total1.ZJJE == null) total1.ZJJE = "0";
            if (total1.ZFJE == null) total1.ZFJE = "0";
            total1.JKJE = "0";
            total1.FYYE = "0";
            ExpenseTotal total2 = service.getPatientAdvancePayMoney(zyh, jgid);
            if (total2 != null && StringUtils.isNotBlank(total2.JKJE)) {
                total1.JKJE = total2.JKJE;
            }

            Double s1 = Double.valueOf(total1.JKJE);
            Double s2 = Double.valueOf(total1.ZFJE);

            int temp = (int) (s1 * 100) - (int) (s2 * 100);
            total1.FYYE = String.valueOf(Double.valueOf(temp) / 100);
            patientDetailResponse.setExpenseTotal(total1);

            // 3.疾病诊断信息
           /* String jllx = detailVo.JLLX;
            String zdid = service.getPatientDiagnose(zyh, jllx, jgid);
            String zdmc = handler.getValueByKeyFromCached(CachedDictEnum.GY_JBBM, jgid, zdid);
            patientDetailResponse.setDiagnose(zdmc);*/
            // 入院诊断
            List<PatientDiagonosis> diagonosesList = service.getPatientDiagnoseFromRYZD(zyh);
            StringBuilder stringBuilder = new StringBuilder();
            for (PatientDiagonosis patientDiagonosis : diagonosesList) {
                stringBuilder.append(patientDiagonosis.JBMC);
                stringBuilder.append("/");
            }
            String qqq = stringBuilder.toString();
            //   123/
            qqq = qqq.endsWith("/") ? qqq.substring(0, qqq.length() - 1) : qqq;
            patientDetailResponse.setDiagnose(qqq);

       /* ###协和诊断    List<String> zdmcList = service.getPatientZD(zyh);
            StringBuilder stringBuilder = new StringBuilder();
            for (String zdmc : zdmcList) {
                stringBuilder.append(zdmc);
                stringBuilder.append("/");
            }
            String qqq = stringBuilder.toString();
            //   123/
            qqq = qqq.endsWith("/") ? qqq.substring(0, qqq.length() - 1) : qqq;
            patientDetailResponse.setDiagnose(qqq);*/

            // 4.过敏药物
            List<AllergicDrug> allergicDrugs = service.getPatientAllergicDrugs(zyh, jgid);
            patientDetailResponse.setAllergicDrugs(allergicDrugs);

            // 5.病人状态
            keepOrRoutingDateSource(DataSource.MOB);
          /* REMOVE 2017-9-19 14:54:34
           List<State> states = service.getPatientStates(zyh, jgid);
            patientDetailResponse.setStates(states);*/

            response.data = patientDetailResponse;
            response.isSuccess = true;
            response.message = "获取病人详情成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[病区病人详情]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[病区病人详情]服务内部错误!";
        }
        return response;
    }
    /* =============================================================== end */

    /*升级编号【56010038】============================================= start
                   外出管理PDA上只有登记功能，查询需要找到具体的人再查询，不太方便，最好能有一个查询整个病区外出病人的列表
               ================= classichu 2018/3/7 19:49
               */
    public BizResponse<SickPersonVo> getPatientForHand(String zyh, String jgid) {
        BizResponse<SickPersonVo> response = new BizResponse<>();
        try {
            keepOrRoutingDateSource(DataSource.HRP);
            response.data = service.getPatientForScan(zyh, jgid);
            response.isSuccess = true;
            response.message = "获取病人信息成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[腕带获取病人信息失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[腕带获取病人信息失败]服务内部错误!";
        }
        return response;
    }
    /* =============================================================== end */

    /**
     * 根据病人腕带获取病人信息
     *
     * @param barcode
     * @param prefix
     * @param jgid
     * @return
     */

    public BizResponse<SickPersonVo> getPatientForScan(String prefix, String barcode, String jgid) {
        BizResponse<SickPersonVo> response = new BizResponse<>();
        try {
            keepOrRoutingDateSource(DataSource.MOB);
            //CK和WD增加pda不含此2字段
            String zyh = null;
            if ("null".equals(prefix) ){
                zyh = service.getPatientZyhByScan(barcode);
            } else {
                zyh = service.getPatientZyhByScan(prefix + barcode);
            }
            keepOrRoutingDateSource(DataSource.HRP);
            response.data = service.getPatientForScan(zyh, jgid);
            response.isSuccess = true;
            response.message = "获取病人信息成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[腕带获取病人信息失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[腕带获取病人信息失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 检查病人有无绑定rfid
     *
     * @param zyh
     * @param jgid
     * @return
     */
    public BizResponse<String> getRFID(String zyh, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<String> response = new BizResponse<>();
        try {
            String wdtm = service.getRFID(zyh, jgid);
            if (!StringUtils.isBlank(wdtm)) {
                response.data = wdtm;
                response.isSuccess = true;
                response.message = "获取RFID号成功!";
            } else {
                response.data = "";
                response.isSuccess = false;
                response.message = "当前病人未绑定RFID!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取当前病人的RFID失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取当前病人的RFID失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 给病人绑定rfid腕带
     *
     * @param zyh
     * @param jgid
     * @return
     */
    public BizResponse<String> patientBindRFID(String zyh, String sbid, String yhid, String bqid, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<String> response = new BizResponse<>();
        try {
            String now = DateUtil.getApplicationDateTime();
            int status = getDeviceStatus(bqid, sbid, 0, now);
            if (status != 0) {
                response.isSuccess = false;
                response.message = "RFID不是闲置状态!";
            } else {
                boolean isSucess = service.editDeviceInfo(sbid, "1") > 0;
                if (!isSucess) {
                    response.isSuccess = false;
                    response.message = "更新设备状态失败!";
                } else {
                    String wdhm = String.valueOf(identityService.getIdentityMax("IENR_BRWD", DataSource.MOB));
                    isSucess = service.addBRWDInfo(wdhm, zyh, "0", "0", sbid, now, yhid, "3", jgid) > 0;
                    if (!isSucess) {
                        response.isSuccess = false;
                        response.message = "新增病人腕带信息失败!";
                    } else {
                        response.isSuccess = true;
                        response.message = "给当前病人绑定RFID成功!";
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[给当前病人绑定RFID失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[给当前病人绑定RFID失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 给病人取消绑定rfid腕带
     *
     * @param zyh
     * @param jgid
     * @return
     */
    public BizResponse<String> patientUnBindRFID(String zyh, String sbid, String shbs, String yhid, String bqid, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<String> response = new BizResponse<>();
        try {
            String wdtm = service.getRFID(zyh, jgid);
            if (!StringUtils.isBlank(wdtm)) {
                sbid = wdtm;
            } else {
                response.isSuccess = false;
                response.message = "当前病人未绑定RFID!";
            }
            String now = DateUtil.getApplicationDateTime();
            int status = getDeviceStatus(bqid, sbid, 0, now);
            if (status != 1) {
                response.isSuccess = false;
                response.message = "RFID不在使用中!";
            } else {
                String sbzt = shbs.equals("1") ? "2" : "0";
                boolean isSucess = service.editBRWDInfo(sbid) > 0;
                if (!isSucess) {
                    response.isSuccess = false;
                    response.message = "更新腕带状态失败!";
                } else {
                    isSucess = service.editDeviceInfo(sbid, sbzt) > 0;
                    if (!isSucess) {
                        response.isSuccess = false;
                        response.message = "更新设备状态信息失败!";
                    } else {
                        response.isSuccess = true;
                        response.message = "给当前病人解除绑定RFID成功!";
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[给当前病人解除绑定RFID失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[给当前病人解除绑定RFID失败]服务内部错误!";
        }
        return response;
    }


    private int getDeviceStatus(String bqid, String deviceid, int devicetype, String now)
            throws SQLException, DataAccessException {
        int status = -1;
        if (devicetype == 0) {
            return status;
        }
        String sbzt = service.getDeviceStatus(deviceid, String.valueOf(devicetype));
        if (!StringUtils.isBlank(sbzt)) {
            status = Integer.parseInt(sbzt);
        } else {
            String sbxh = String.valueOf(identityService.getIdentityMax("IENR_SBLB", DataSource.MOB));
            int count = service.addDeviceInfo(sbxh, "1", deviceid, "", "0", bqid, now, "");
            if (count > 0) {
                status = 0;
            } else {
                status = -1;
            }
        }
        return status;

    }

}
