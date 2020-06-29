package com.bsoft.nis.service.evaluation.v56_update1;

import com.bsoft.nis.common.service.ConfigService;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.DataSourceUtil;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.core.SystemConfig;
import com.bsoft.nis.domain.core.cached.Map2;
import com.bsoft.nis.domain.dangerevaluate.DERecord;
import com.bsoft.nis.domain.evaluation.evalnew.*;
import com.bsoft.nis.domain.lifesign.LifeSignHistoryDataItem;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.domain.patient.db.PatientDiagonosis;
import com.bsoft.nis.domain.synchron.InArgument;
import com.bsoft.nis.domain.synchron.Project;
import com.bsoft.nis.domain.synchron.SelectResult;
import com.bsoft.nis.mapper.evaluation.v56_update1.EvalutionRecordMapper;
import com.bsoft.nis.mapper.evaluation.v56_update1.EvalutionStyleMapper;
import com.bsoft.nis.mapper.evaluation.v56_update1.EvalutionStyleProjectMapper;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.evaluation.support.EvaluationService;
import com.bsoft.nis.service.evaluation.support.v56_update1.EvalutionUpdate1Service;
import com.bsoft.nis.service.patient.PatientMainService;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
import com.bsoft.nis.util.date.birthday.BirthdayUtil;
import ctd.net.rpc.Client;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 护理评估主服务
 * description:重新设计护理评估单：
 * 1.支持无限级项目
 * 2.支持自伸缩
 * create by: dragon xinghl@bsoft.com.cn
 * create time:2017/11/20 9:29
 * since:5.6 update1
 */
@Service
public class EvaluationUpdate1MainService extends RouteDataSourceService {
    private Log logger = LogFactory.getLog(EvaluationUpdate1MainService.class);

    @Autowired
    EvalutionStyleMapper styleMapper;
    @Autowired
    EvalutionStyleProjectMapper styleProjectMapper;

    @Autowired
    EvalutionRecordMapper recordMapper;

    @Autowired
    IdentityService identityService;
    @Autowired
    DateTimeService dateTimeService;

    @Autowired
    EvalutionUpdate1Service service;

    @Autowired
    PatientMainService patientService;
    @Autowired
    ConfigService configService;
    @Autowired
    PatientServiceSup patientServiceSup;
    /**
     * 5.6 版本服务：部分服务借用
     */
    @Autowired
    EvaluationService oldEvalutionService;
    @Autowired
    SystemParamService systemParamService;

    /**
     * 根据记录序号获取评估记录（包含样式和记录）
     *
     * @param jlxh
     * @return
     */
    public BizResponse<Map> getEvalutionRecordByPrimaryKey(String jlxh) {
        BizResponse<Map> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        Map map = new HashMap();
        try {
            // 1.根据jlxh获取记录
            EvalutionRecord record = recordMapper.selectByPrimaryKey(Long.valueOf(jlxh));
            if (record == null) {
                response.isSuccess = false;
                response.message = "评估记录获取失败!";
                return response;
            }
            // 2.根据样式序号和版本号获取模板数据
            String jgid = String.valueOf(record.getJGID());
            Long styleId = record.getYSXH();
            Integer version = record.getBBH();
            BizResponse<EvalutionStyle> styleBizResponse = getEvalutionStyleSingleByPrimaryKey(jgid, styleId, version);
            if (!styleBizResponse.isSuccess) {
                response.isSuccess = false;
                response.message = "评估单样式获取失败!";
                return response;
            }
            map.put("style", styleBizResponse.datalist);
            map.put("record", record);
            response.isSuccess = true;
            response.message = "获取成功";
            response.data = map;
        } catch (SQLException e) {
            handleError(e, response);
        } catch (Exception e) {
            handleError(e, response);
        }
        return response;
    }

    /**
     * 评估单记录列表获取
     *
     * @param zyh
     * @param brbq
     * @param jgid
     * @return
     */
    public BizResponse<List<EvalutionRecord>> getRecordList(Long zyh, Integer brbq, Integer jgid) {
        BizResponse<List<EvalutionRecord>> bizResponse = new BizResponse<>();
        List<EvalutionRecord> evalutionRecordList = null;
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            evalutionRecordList = recordMapper.selectRecordList(zyh, brbq, jgid);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (evalutionRecordList == null) {
            bizResponse.isSuccess = false;
            bizResponse.message = "评估单记录列表获取失败!";
        }
        bizResponse.isSuccess = true;
        bizResponse.message = "评估单记录列表获取成功!";
        bizResponse.data = evalutionRecordList;
        return bizResponse;

    }

    /**
     * 评估单样式列表获取
     *
     * @param jgid
     * @return
     */
    public BizResponse<List<EvalutionStyle>> getStyleList(Integer jgid) {
        BizResponse<List<EvalutionStyle>> bizResponse = new BizResponse<>();
        List<EvalutionStyle> evalutionRecordList = null;
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            evalutionRecordList = recordMapper.selectStyleList(jgid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (evalutionRecordList == null) {
            bizResponse.isSuccess = false;
            bizResponse.message = "评估单样式列表获取失败!";
        }
        bizResponse.isSuccess = true;
        bizResponse.message = "评估单样式列表获取成功!";
        bizResponse.data = evalutionRecordList;
        return bizResponse;

    }

    /**
     * 评估单样式列表获取
     *
     * @param jgid
     * @return
     */
    public BizResponse<List<EvalutionStyle>> selectStyleByPrimaryKey(Long ysxh, Integer jgid) {
        BizResponse<List<EvalutionStyle>> bizResponse = new BizResponse<>();
        List<EvalutionStyle> evalutionRecordList = null;
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            evalutionRecordList = recordMapper.selectStyleByPrimaryKey(ysxh, jgid);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (evalutionRecordList == null) {
            bizResponse.isSuccess = false;
            bizResponse.message = "评估单样式列表获取失败!";
        }
        bizResponse.isSuccess = true;
        bizResponse.message = "评估单样式列表获取成功!";
        bizResponse.data = evalutionRecordList;
        return bizResponse;

    }

    /**
     * 根据样式id和版本获取评估样式
     *
     * @param jgid
     * @param styleId
     * @param version
     * @param zyh
     * @return
     */
    public BizResponse<EvalutionStyle> getEvalutionStyleSingleByPrimaryKey(String jgid,
                                                                           Long styleId,
                                                                           Integer version,
                                                                           String zyh) {
        BizResponse<EvalutionStyle> response = new BizResponse<>();
        // 根据样式ID和样式版本号获取样式
        response = wrapperEvalutionStyleFromDb(jgid, styleId, version);
        // 根据住院号获取病人基本信息
        BizResponse<Map2> patientInfo = getPatientInfo(zyh);
        if (patientInfo.isSuccess) {
            if (patientInfo.datalist.size() > 0) {
                if (response.datalist.size() > 0) {
                    response.datalist.get(0).setBaseInfo(patientInfo.datalist);
                }
            }
        }
        return response;
    }

    /**
     * 根据样式id和版本获取评估样式
     *
     * @param jgid    机构ID
     * @param styleId 样式序号
     * @param version 样式版本号
     * @return 表单样式
     */
    public BizResponse<EvalutionStyle> getEvalutionStyleSingleByPrimaryKey(String jgid,
                                                                           Long styleId,
                                                                           Integer version) {
        BizResponse<EvalutionStyle> response = new BizResponse<>();
        // 根据样式ID和样式版本号获取样式
        response = wrapperEvalutionStyleFromDb(jgid, styleId, version);

        return response;
    }

    /**
     * 从二维表组装评估样式
     *
     * @param jgid
     * @param styleId
     * @param version
     * @return
     */
    public BizResponse<EvalutionStyle> wrapperEvalutionStyleFromDb(String jgid, Long styleId, Integer version) {
        BizResponse<EvalutionStyle> response = new BizResponse<>();

        try {
            keepOrRoutingDateSource(DataSource.MOB);

            // 获取评估样式
            EvalutionStyleKey key = new EvalutionStyleKey(Integer.valueOf(jgid), styleId, version);
            List<EvalutionStyle> styles = styleMapper.selectByPrimaryKey(key);
            // 获取根目录样式项目
            List<EvalutionStyleProject> rootProjects = styleProjectMapper.selectRootProjectsByStyleKey(key);
            // 获取非根目录样式项目
            List<EvalutionStyleProject> childProjects = styleProjectMapper.selectRootChildProjectsByStyleKey(key);

            response.datalist = wrapperEvalutionStyle(styles, rootProjects, childProjects);
            // 获取样式项目中的基本信息对应的数据
            // 根据样式类型获取关联数据策略
            if (styles.size() > 0) {
                String getPolicy = oldEvalutionService.getSJHQFS(String.valueOf(styles.get(0).getYSLX()));
                styles.get(0).setSJHQFS(getPolicy);
            }

            response.isSuccess = true;
            response.message = "数据成功组装";
        } catch (SQLException e) {
            handleError(e, response);
        }
        return response;
    }

    /**
     * 从Xml中组装评估样式
     *
     * @param jgid
     * @param styleId
     * @param version
     * @return
     */
    public BizResponse<EvalutionStyle> wrapperEvalutionStyleFromXml(String jgid, Long styleId, Integer version) {
        BizResponse<EvalutionStyle> response = new BizResponse<>();
        return response;
    }

    /**
     * 从配置的sql语句 获取下拉框数据
     *
     * @param dataSource
     * @param sql
     * @return
     */
    public List<ComboUi> getComboboxDatas(String dataSource, String sql) {
        List<ComboUi> datas = new ArrayList<>();
        keepOrRoutingDateSource(DataSourceUtil.findDataSourceByDbString(dataSource));
        try {
            datas = styleMapper.getComboboxDatas(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return datas;
    }

    /**
     * 新增评估记录
     *
     * @param record
     * @return
     */
    public BizResponse<EvalutionRecord> addEvalutionRecord(EvalutionRecord record) {
        BizResponse<EvalutionRecord> response = new BizResponse<>();
        BizResponse<Long> idResponse = new BizResponse<>();
        try {
            // 主记录处理
            idResponse = identityService.getIdentityMax("IENR_HLPGJL", 1, DataSource.MOB);
            if (!idResponse.isSuccess) {
                response.isSuccess = false;
                response.message = "[IENR_HLPGJL]主键值获取失败!\r\n详见错误日志";
                return response;
            }
            Long primaryKey = idResponse.datalist.get(0);
            record.setJLXH(primaryKey);
            record.setZFBZ((short) 0);
            Date date = dateTimeService.nowDate(DataSource.PORTAL);
            record.setJLSJ(date);

            // 处理明细
            List<EvalutionRecordDetail> details = record.getDetails();
            if (details.size() > 0) {
                idResponse = identityService.getIdentityMax("IENR_PGJLMX", details.size(), DataSource.MOB);
                int index = 0;
                for (EvalutionRecordDetail detail : details) {
                    detail.setMXXH(idResponse.datalist.get(index));
                    detail.setJLXH(primaryKey);
                    index++;
                }
            }
            keepOrRoutingDateSource(DataSource.MOB);
            service.addEvalutionRecord(record);
            // 处理同步
            List<String> synchronParam = systemParamService.getUserParams("1", "IENR", "IENR_YTHHL_YDHLSJTB", String.valueOf(record.getJGID()), DataSource.MOB).datalist;
            if (synchronParam.size() > 0) {
                if (synchronParam.get(0).equals("1")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String txsjStr = sdf.format(record.getTXSJ());
                    Response<SelectResult> _response = BuilderSyncData(String.valueOf(record.getZYH()),
                            String.valueOf(record.getBRBQ()),
                            record.getTXGH(),
                            String.valueOf(record.getYSXH()),
                            txsjStr,
                            String.valueOf(record.getJLXH()),
                            "0",
                            String.valueOf(record.getJGID()));
                    if (_response.ReType != 0) {
                        logger.error(_response.Msg);
                    }
                }
            }
            response.data = record;
            response.isSuccess = true;
            response.message = "保存成功!";
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = "评估单保存失败";
        }
        return response;
    }

    /**
     * 修改评估记录
     *
     * @param record
     * @return
     */
    public BizResponse<EvalutionRecord> updateEvalutionRecord(EvalutionRecord record) {
        BizResponse<EvalutionRecord> response = new BizResponse<>();
        BizResponse<Long> idResponse = new BizResponse<>();
        try {
            // 主记录无需处理
            // 处理明细记录
            List<EvalutionRecordDetail> details = record.getDetails();
            List<EvalutionRecordDetail> retDetails = new ArrayList<>();
            if (details.size() > 0) {
                int addCount = 0;// 需新增数量
                for (EvalutionRecordDetail detail : details) {
                    detail.setJLXH(record.getJLXH());
                    if (detail.getStatus().equals("add")) {
                        addCount++;
                    }
                    if (!detail.getStatus().equals("delete")) {
                        retDetails.add(detail);
                    }
                }
                if (addCount > 0) {
                    idResponse = identityService.getIdentityMax("IENR_PGJLMX", addCount, DataSource.MOB);
                    if (!idResponse.isSuccess) {
                        response.isSuccess = false;
                        response.message = "[IENR_PGJLMX]主键值获取失败!\r\n详见错误日志";
                        return response;
                    }
                    addCount = 0;
                    for (EvalutionRecordDetail detail : details) {
                        if (detail.getStatus().equals("add")) {
                            detail.setMXXH(idResponse.datalist.get(addCount));
                            addCount++;
                        }
                    }
                }
            }

            keepOrRoutingDateSource(DataSource.MOB);
            service.updateEvalutionRecord(record);
            // 返回去除delete状态的记录
            record.setDetails(retDetails);
            // 处理同步
            String _jgid = record.getJGID() == null ? "1" : String.valueOf(record.getJGID());
            List<String> synchronParam = systemParamService.getUserParams("1", "IENR", "IENR_YTHHL_YDHLSJTB", _jgid, DataSource.MOB).datalist;

            if (synchronParam != null && synchronParam.size() > 0) {
                if (synchronParam.get(0).equals("1")) {
                    /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String txsjStr = sdf.format(record.getTXSJ());
                    Response<SelectResult> _response = BuilderSyncData(String.valueOf(record.getZYH()),
                            String.valueOf(record.getBRBQ()),
                            record.getTXGH(),
                            String.valueOf(record.getYSXH()),
                            txsjStr,
                            String.valueOf(record.getJLXH()),
                            "1",
                            String.valueOf(record.getJGID()));
                    if (_response.ReType != 0){
                        logger.error("入院评估单同步失败!");
                    }*/
                }
            }
            response.data = record;
            response.isSuccess = true;
            response.message = "保存成功";
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return response;
    }

    /**
     * 获取病人信息
     *
     * @param zyh
     * @return
     */
    public BizResponse<Map2> getPatientInfo(String zyh) {
        BizResponse<Map2> response = new BizResponse<>();
        try {
            // 获取病人信息
            BizResponse<Patient> pBizResponse = patientService.getPatientByZyhForEvalution(zyh);
            // 获取配置信息 DMLB = 470
            BizResponse<SystemConfig> configBizResponse = configService.getConfigsByDmlb(String.valueOf(470));
            String nowStr = dateTimeService.now(DataSource.PORTAL);

            Patient patient = pBizResponse.data;
            if (patient == null) {
                response.isSuccess = false;
                response.message = "病人不存在";
                return response;
            }

            if (!configBizResponse.isSuccess) {
                response.isSuccess = false;
                response.message = "配置config[470]不存在!";
                return response;
            }

            // 对照项目
            List<Map2> baseInfos = new ArrayList<>();
            List<SystemConfig> configs = configBizResponse.datalist;
            for (SystemConfig config : configs) {

                if (config.DMSB.equals("1")) { // 病人科室
                    Map2 map2 = new Map2("1", patient.BRKSVALUE);
                    baseInfos.add(map2);
                    continue;
                }
                if (config.DMSB.equals("2")) { // 病人病区
                    Map2 map2 = new Map2("2", patient.BRBQVALUE);
                    baseInfos.add(map2);
                    continue;
                }
                if (config.DMSB.equals("3")) { // 病人床号
                    Map2 map2 = new Map2("3", patient.BRCH);
                    baseInfos.add(map2);
                    continue;
                }
                if (config.DMSB.equals("4")) { // 住院号码
                    Map2 map2 = new Map2("4", patient.ZYHM);
                    baseInfos.add(map2);
                    continue;
                }
                if (config.DMSB.equals("5")) { // 病人姓名
                    Map2 map2 = new Map2("5", patient.BRXM);
                    baseInfos.add(map2);
                    continue;
                }
                if (config.DMSB.equals("6")) {
                    if (patient.BRXB == null) {
                        continue;
                    }
                    if (patient.BRXB.equals("1")) {
                        patient.BRXBVALUE = "男";
                    } else if (patient.BRXB.equals("2")) {
                        patient.BRXBVALUE = "女";
                    }
                    Map2 map2 = new Map2("6", patient.BRXBVALUE);

                    baseInfos.add(map2);
                    continue;
                }

                if (config.DMSB.equals("7")) {
                    if (!StringUtils.isBlank(patient.CSNY)) {
//                        String age =BirthdayUtil.getAgesCommon(patient.CSNY, nowStr);
                        String age = BirthdayUtil.getAgesPairCommonStrSimple(patient.CSNY, nowStr);
                        Map2 map2 = new Map2("7", age);
                        baseInfos.add(map2);
                        continue;
                    }
                }
                if (config.DMSB.equals("8")) { // 病人职业
                    /*Map2 map2 = new Map2("8",patient.);
                    baseInfos.add(map2);
                    continue;*/
                }

                if (config.DMSB.equals("9")) { // 病人民族

                }
                if (config.DMSB.equals("10")) { // 病人籍贯

                }
                if (config.DMSB.equals("11")) { // 病人婚姻

                }
                if (config.DMSB.equals("12")) { // 出院日期
                    Map2 map2 = new Map2("12", patient.CYRQ);
                    baseInfos.add(map2);
                }
                if (config.DMSB.equals("13")) { // 联系电话
                    /*Map2 map2 = new Map2("13",patient.LXDH);
                    baseInfos.add(map2);*/
                }
                if (config.DMSB.equals("14")) { // 死亡日期

                }
                if (config.DMSB.equals("15")) { // 死亡诊断

                }
                if (config.DMSB.equals("16")) { // 入院日期
                    Map2 map2 = new Map2("16", patient.RYRQ);
                    baseInfos.add(map2);
                }
                if (config.DMSB.equals("17")) { // 入院诊断
                    // 入院诊断
                    List<PatientDiagonosis> diagonoses = patientServiceSup.getPatientDiagnoseFromRYZD(zyh);
                    String brzd = "";
                    int count = diagonoses.size();
                    for (int i = 0; i < count; i++) {
                        PatientDiagonosis patientDiagonosis = diagonoses.get(i);
                        if ((i + 1) == count) {
                            brzd += patientDiagonosis.JBMC;
                        } else {
                            brzd += patientDiagonosis.JBMC + ",";
                        }
                    }
                    Map2 map2 = new Map2("17", brzd);
                    baseInfos.add(map2);

                }
                if (config.DMSB.equals("18")) { // 出院方式

                }
                if (config.DMSB.equals("19")) { // 采集时间

                }
                if (config.DMSB.equals("20")) { // 主任医生

                }
                if (config.DMSB.equals("21")) { // 出生日期
                    Map2 map2 = new Map2("21", patient.CSNY);
                    baseInfos.add(map2);
                }
            }
            response.datalist = baseInfos;
            response.isSuccess = true;
            response.message = "数据整合成功!";

        } catch (Exception e) {
            handleError(e, response);
        }
        return response;
    }

    private List<EvalutionStyle> wrapperEvalutionStyle(List<EvalutionStyle> styles,
                                                       List<EvalutionStyleProject> rootProjects,
                                                       List<EvalutionStyleProject> rootChildProjects) {
        for (EvalutionStyle style : styles) {
            for (EvalutionStyleProject rootProject : rootProjects) {
                style.getProjects().add(rootProject);
                // 查找子项目
                List<EvalutionStyleProject> projects = findChildProjects(rootProject, rootChildProjects);
            }
        }
        return styles;
    }

    private List<EvalutionStyle> wrapperEvalutionStyle(List<EvalutionStyle> styles) {

        return styles;
    }

    /**
     * 查找子项目
     *
     * @param parent
     * @param contains
     * @return
     */
    private List<EvalutionStyleProject> findChildProjects(EvalutionStyleProject parent, List<EvalutionStyleProject> contains) {
        List<EvalutionStyleProject> projects = new ArrayList<>();
        for (EvalutionStyleProject project : contains) {
            if (project.getSJXM().equals(parent.getXMXH())) {
                projects.add(project);
            }
        }
        parent.setChildren(projects);
        if (projects.size() > 0) {
            for (EvalutionStyleProject project : projects) {
                findChildProjects(project, contains);
            }
        }
        return projects;
    }


    /**
     * 处理错误信息
     *
     * @param e
     * @param response
     */
    private void handleError(Exception e, BizResponse response) {
        if (e != null) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            if (e instanceof SQLException)
                response.message = "数据库查询错误";
            else
                response.message = "服务内部错误";
        }
    }

    /**
     * 同步表单数据源
     *
     * @param zyh
     * @param bqdm
     * @param txgh
     * @param bdxh
     * @param txsj
     * @param jlxh
     * @param czbz
     * @param jgid
     * @return
     */
    private Response<SelectResult> BuilderSyncData(String zyh, String bqdm, String txgh, String bdxh, String txsj, String jlxh, String czbz, String jgid) {

        InArgument inArgument = new InArgument();
        inArgument.zyh = zyh;
        inArgument.bqdm = bqdm;
        inArgument.hsgh = txgh;
        inArgument.bdlx = "1";
        inArgument.lybd = bdxh;
        inArgument.flag = czbz;
        inArgument.jlxh = jlxh;
        inArgument.jgid = jgid;
        inArgument.lymx = "0";
        inArgument.lymxlx = "0";
        if (txsj == null || "".equals(txsj)) {
            txsj = dateTimeService.now(DataSource.PORTAL);
        }
        inArgument.jlsj = txsj;
        Project project = new Project("0", jlxh);
        Project _project = new Project("0", "");
        project.saveProjects.add(_project);
        inArgument.projects.add(project);
        Response<SelectResult> response = new Response<>();

        try {
            response = Client.rpcInvoke("nis-synchron.synchronRpcServerProvider", "synchron", inArgument);
        } catch (Throwable throwable) {
            response.ReType = -1;
            response.Msg = "同步目标失败";
            logger.error(throwable.getMessage(), throwable);
        }
        return response;
    }

    /**
     * 签名：分为签名，取消签名
     *
     * @param mode     sign unsign
     * @param jlxh     记录序号
     * @param signwho  护士签名，护士长签名
     * @param usercode 用户ID
     * @param username 用户名
     * @return
     */
    public BizResponse<String> signEvalutionRecord(String mode, String jlxh, String signwho, String usercode, String username) {
        BizResponse<String> response = new BizResponse<>();
        try {
            Date nowDate = dateTimeService.nowDate(DataSource.PORTAL);
            if (mode.equals("sign")) {
                signMainRecord(jlxh, signwho, usercode, username, nowDate);
            } else if (mode.equals("unsign")) {
                unsignMainRecord(jlxh, signwho, usercode, username, nowDate);
            }
            response.isSuccess = true;
            response.message = "更新成功";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = e.getMessage();
        }
        return response;
    }

    public void signMainRecord(String jlxh, String signwho, String usercode, String username, Date now) throws SQLException {
        keepOrRoutingDateSource(DataSource.MOB);
        // 护士签名
        if (signwho.equals("1")) {
            service.updateNurseSign(jlxh, usercode, username, now);
            // 护士长签名：签名的同时，更新状态为审阅
        } else if (signwho.equals("2")) {
            service.updatePNurseSign(jlxh, usercode, username, now);
        }
    }

    public void unsignMainRecord(String jlxh, String signwho, String usercode, String username, Date now) throws SQLException {
        keepOrRoutingDateSource(DataSource.MOB);
        service.clearSignInfo(jlxh, Integer.valueOf(signwho));
    }

    /**
     * 获取护理评估项目关联数据
     * 2：风险评估 3：健康宣教 5：生命体征
     *
     * @param params 入参
     * @return
     */
    public BizResponse<EvalutionRecordDetail> getRelationData(List<RelationDataParam> params) {
        BizResponse<EvalutionRecordDetail> response = new BizResponse<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<EvalutionRecordDetail> results = new ArrayList<>();
        try {
            // 入参校验
            if (params.size() <= 0)
                throw new IllegalArgumentException("参数列表为空");
            invalidParams(params);
            String yslx = params.get(0).YSLX;
            Date _txsj = sdf.parse(params.get(0).TXSJ);

            //关联数据获取策略
            keepOrRoutingDateSource(DataSource.MOB);
            String getPolicy = oldEvalutionService.getSJHQFS(yslx);
            getPolicy = StringUtils.isEmpty(getPolicy) ? "2" : getPolicy; //hqfs=2   默认值

            //分类获取关联数据：根据不同的getPolicy
            for (RelationDataParam param : params) {
                //  5：生命体征 2：风险评估 3：健康宣教
                String ywlb = param.YWLB;
                switch (ywlb) {
                    case "5":
                        results.addAll(handleLifeSignRelationDatas(param, getPolicy, _txsj));
                        break;
                    case "2":
                        results.addAll(handleRiskRelationDatas(param, getPolicy, _txsj));
                        break;
                    case "3":
                        results.addAll(handleHealthRelationDatas(param, getPolicy, _txsj));
                        break;
                }
            }
            response.datalist = results;
            response.message = "关联数据获取成功!";
            response.isSuccess = true;
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = e.getMessage();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = e.getMessage();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            response.isSuccess = false;
            response.message = e.getMessage();
        }
        return response;
    }

    /**
     * 处理生命体征关联数据
     *
     * @param param     关联数据入参
     * @param getPolicy 获取策略
     * @param writeTime 填写时间
     * @return
     */
    private List<EvalutionRecordDetail> handleLifeSignRelationDatas(RelationDataParam param, String getPolicy, Date writeTime) throws SQLException, ParseException {

        List<EvalutionRecordDetail> details = new ArrayList<>();
        keepOrRoutingDateSource(DataSource.ENR);
        String oranCode = param.JGID;
        if (StringUtils.isEmpty(oranCode)) oranCode = "1";
        List<LifeSignHistoryDataItem> lifeSignDatas = new ArrayList<>();
        if (getPolicy.equals("3")) {
            lifeSignDatas = oldEvalutionService.getSMTZByGroupId(param.CJZH);
        } else {
            lifeSignDatas = oldEvalutionService.getSMTZ(param.ZYH, oranCode);
        }

        List<RelationDataParamItem> items = param.YWLBMX;

        for (RelationDataParamItem item : items) {
            String glxmh = item.GLXMH;
            EvalutionRecordDetail _d = null;
            if (getPolicy.equals("2")) { // 评估时间最近的有效记录
                _d = getLifeSignResultByRecentlyPolicy(lifeSignDatas, param, item, glxmh, writeTime);
            } else if (getPolicy.equals("1")) { // 第一次操作的有效记录
                _d = getLifeSignResultByFirstlyPolicy(lifeSignDatas, param, item, glxmh, writeTime);
            } else if (getPolicy.equals("3")) { // 当前创建的记录
                // 生命体征是通过项目组号获取
                _d = getLifeSignResultByNowPolicy(lifeSignDatas, param, item, glxmh, writeTime);
            }
            if (_d != null)
                details.add(_d);
        }
        return details;
    }

    /**
     * 生命体征：根据本次记录策略
     *
     * @param lifeSignDatas 生命体征数据
     * @param param         关联数据入参
     * @param item          关联明细项目信息
     * @param glxmh         关联项目号
     * @param writeTime     填写时间
     * @return
     * @throws ParseException
     */
    private EvalutionRecordDetail getLifeSignResultByNowPolicy(List<LifeSignHistoryDataItem> lifeSignDatas, RelationDataParam param, RelationDataParamItem item, String glxmh, Date writeTime) throws ParseException {
        int index = -1;
        for (int i = 0; i < lifeSignDatas.size(); i++) {
            LifeSignHistoryDataItem lifeData = lifeSignDatas.get(i);
            if (String.valueOf(lifeData.XMH).equals(glxmh)) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            LifeSignHistoryDataItem lifeSignItem = lifeSignDatas.get(index);
            return new EvalutionRecordDetail(Long.valueOf(item.PGXMH),
                    lifeSignItem.TZNR,
                    Short.valueOf(param.YWLB),
                    Long.valueOf(lifeSignItem.CJH),
                    Short.valueOf(item.XMKJLX));
        } else {
            return null;
        }
    }


    /**
     * 生命体征：根据第一次记录策略
     *
     * @param lifeSignDatas 生命体征数据
     * @param param         关联数据入参
     * @param item          关联明细项目信息
     * @param glxmh         关联项目号
     * @param writeTime     填写时间
     * @return
     * @throws ParseException
     */
    private EvalutionRecordDetail getLifeSignResultByFirstlyPolicy(List<LifeSignHistoryDataItem> lifeSignDatas, RelationDataParam param, RelationDataParamItem item, String glxmh, Date writeTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long maxDiffTime = 0;
        int index = -1;
        for (int i = 0; i < lifeSignDatas.size(); i++) {
            LifeSignHistoryDataItem lifeData = lifeSignDatas.get(i);
            if (String.valueOf(lifeData.XMH).equals(glxmh) && (writeTime.getTime() > sdf.parse(lifeData.CJSJ).getTime())) {
                long diff = writeTime.getTime() - sdf.parse(lifeData.CJSJ).getTime();
                if (diff >= maxDiffTime) {
                    maxDiffTime = diff;
                    index = i;
                }
            }
        }
        if (index > -1) {
            LifeSignHistoryDataItem lifeSignItem = lifeSignDatas.get(index);
            return new EvalutionRecordDetail(Long.valueOf(item.PGXMH),
                    lifeSignItem.TZNR,
                    Short.valueOf(param.YWLB),
                    Long.valueOf(lifeSignItem.CJH),
                    Short.valueOf(item.XMKJLX));
        } else {
            return null;
        }
    }

    /**
     * 生命体征：根据最近一次记录策略
     *
     * @param lifeSignDatas 生命体征数据
     * @param param         关联数据入参
     * @param item          关联明细项目信息
     * @param glxmh         关联项目号
     * @param writeTime     填写时间
     * @return
     * @throws ParseException
     */
    private EvalutionRecordDetail getLifeSignResultByRecentlyPolicy(List<LifeSignHistoryDataItem> lifeSignDatas, RelationDataParam param, RelationDataParamItem item, String glxmh, Date writeTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long minDiffTime = writeTime.getTime();
        int index = -1;
        for (int i = 0; i < lifeSignDatas.size(); i++) {
            LifeSignHistoryDataItem lifeData = lifeSignDatas.get(i);
            if (String.valueOf(lifeData.XMH).equals(glxmh) && (writeTime.getTime() > sdf.parse(lifeData.CJSJ).getTime())) {
                long diff = writeTime.getTime() - sdf.parse(lifeData.CJSJ).getTime();
                if (diff <= minDiffTime) {
                    minDiffTime = diff;
                    index = i;
                }
            }
        }
        if (index > -1) {
            LifeSignHistoryDataItem lifeSignItem = lifeSignDatas.get(index);
            return new EvalutionRecordDetail(Long.valueOf(item.PGXMH),
                    lifeSignItem.TZNR,
                    Short.valueOf(param.YWLB),
                    Long.valueOf(lifeSignItem.CJH),
                    Short.valueOf(item.XMKJLX));
        } else {
            return null;
        }
    }

    /**
     * 处理风险评估关联数据
     *
     * @param param     关联数据入参
     * @param getPolicy 获取策略
     * @param writeTime 填写时间
     * @return
     */
    private List<EvalutionRecordDetail> handleRiskRelationDatas(RelationDataParam param, String getPolicy, Date writeTime) throws SQLException, ParseException {
        List<EvalutionRecordDetail> details = new ArrayList<>();
        keepOrRoutingDateSource(DataSource.MOB);
        String oranCode = param.JGID;
        if (StringUtils.isEmpty(oranCode)) oranCode = "1";
        List<DERecord> riskDatas = oldEvalutionService.getFXPG(param.ZYH, oranCode);
        List<RelationDataParamItem> items = param.YWLBMX;

        for (RelationDataParamItem item : items) {
            String glxmh = item.GLXMH;
            EvalutionRecordDetail _d = null;
            if (getPolicy.equals("2")) { // 评估时间最近的有效记录
                _d = getRiskResultByRecentlyPolicy(riskDatas, param, item, glxmh, writeTime);
            } else if (getPolicy.equals("1")) { // 第一次操作的有效记录
                _d = getRiskResultByFirstlyPolicy(riskDatas, param, item, glxmh, writeTime);
            } else if (getPolicy.equals("3")) { // 当前创建的记录
                if (!StringUtils.isEmpty(item.DZBDJL)) { // 生命体征是通过项目组号获取
                    List<DERecord> riskDatas1 = oldEvalutionService.getFXPGByPrimaryKey(item.DZBDJL);
                    _d = getRiskResultByNowPolicy(riskDatas1, param, item, glxmh, writeTime);
                }
            }
            if (_d != null)
                details.add(_d);
        }
        return details;
    }

    /**
     * 风险评估：根据本次策略
     *
     * @param riskDatas 风险评估数据
     * @param param     关联数据入参
     * @param item      关联明细项目信息
     * @param glxmh     关联项目号
     * @param writeTime 填写时间
     * @return
     * @throws ParseException
     */
    private EvalutionRecordDetail getRiskResultByNowPolicy(List<DERecord> riskDatas, RelationDataParam param, RelationDataParamItem item, String glxmh, Date writeTime) throws ParseException {
        int index = -1;
        for (int i = 0; i < riskDatas.size(); i++) {
            DERecord deRecord = riskDatas.get(i);
            if (String.valueOf(deRecord.PGLX).equals(glxmh)) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            DERecord deRecord = riskDatas.get(index);
            return new EvalutionRecordDetail(Long.valueOf(item.PGXMH),
                    deRecord.PGZF,
                    Short.valueOf(param.YWLB),
                    Long.valueOf(deRecord.PGXH),
                    Short.valueOf(item.XMKJLX));
        } else {
            return null;
        }
    }

    /**
     * 风险评估：根据最近策略
     *
     * @param riskDatas 风险评估数据
     * @param param     关联数据入参
     * @param item      关联明细项目信息
     * @param glxmh     关联项目号
     * @param writeTime 填写时间
     * @return
     * @throws ParseException
     */
    private EvalutionRecordDetail getRiskResultByRecentlyPolicy(List<DERecord> riskDatas, RelationDataParam param, RelationDataParamItem item, String glxmh, Date writeTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long minDiffTime = writeTime.getTime();
        int index = -1;
        for (int i = 0; i < riskDatas.size(); i++) {
            DERecord deRecord = riskDatas.get(i);
            if (String.valueOf(deRecord.PGLX).equals(glxmh) && (writeTime.getTime() > sdf.parse(deRecord.PGSJ).getTime())) {
                long diff = writeTime.getTime() - sdf.parse(deRecord.PGSJ).getTime();
                if (diff <= minDiffTime) {
                    minDiffTime = diff;
                    index = i;
                }
            }
        }
        if (index > -1) {
            DERecord deRecord = riskDatas.get(index);
            return new EvalutionRecordDetail(Long.valueOf(item.PGXMH),
                    deRecord.PGZF,
                    Short.valueOf(param.YWLB),
                    Long.valueOf(deRecord.PGXH),
                    Short.valueOf(item.XMKJLX));
        } else {
            return null;
        }
    }

    /**
     * 风险评估：根据第一次策略
     *
     * @param riskDatas 风险评估数据
     * @param param     关联数据入参
     * @param item      关联明细项目信息
     * @param glxmh     关联项目号
     * @param writeTime 填写时间
     * @return
     * @throws ParseException
     */
    private EvalutionRecordDetail getRiskResultByFirstlyPolicy(List<DERecord> riskDatas, RelationDataParam param, RelationDataParamItem item, String glxmh, Date writeTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long maxDiffTime = 0;
        int index = -1;
        for (int i = 0; i < riskDatas.size(); i++) {
            DERecord deRecord = riskDatas.get(i);
            if (String.valueOf(deRecord.PGLX).equals(glxmh) && (writeTime.getTime() > sdf.parse(deRecord.PGSJ).getTime())) {
                long diff = writeTime.getTime() - sdf.parse(deRecord.PGSJ).getTime();
                if (diff >= maxDiffTime) {
                    maxDiffTime = diff;
                    index = i;
                }
            }
        }
        if (index > -1) {
            DERecord deRecord = riskDatas.get(index);
            return new EvalutionRecordDetail(Long.valueOf(item.PGXMH),
                    deRecord.PGZF,
                    Short.valueOf(param.YWLB),
                    Long.valueOf(deRecord.PGXH),
                    Short.valueOf(item.XMKJLX));
        } else {
            return null;
        }
    }

    /**
     * 处理健康宣教关联数据
     *
     * @param param     关联数据入参
     * @param getPolicy 获取策略
     * @param writeTime 填写时间
     * @return
     */
    private List<EvalutionRecordDetail> handleHealthRelationDatas(RelationDataParam param, String getPolicy, Date writeTime) throws SQLException {
        List<EvalutionRecordDetail> details = new ArrayList<>();

        return details;
    }

    private void invalidParams(List<RelationDataParam> params) throws IllegalArgumentException {
        if (params == null || params.size() <= 0)
            throw new IllegalArgumentException("params长度不可为零");
        for (RelationDataParam param : params) {
            if (StringUtils.isEmpty(param.TXSJ))
                throw new IllegalArgumentException("[TXSJ]非法入参:" + param.TXSJ);
            if (StringUtils.isEmpty(param.YSLX))
                throw new IllegalArgumentException("[YSLX]非法入参:" + param.YSLX);
            if (StringUtils.isEmpty(param.YSXH))
                throw new IllegalArgumentException("[YSXH]非法入参" + param.YSXH);
            if (StringUtils.isEmpty(param.ZYH))
                throw new IllegalArgumentException("[ZYH]非法入参" + param.ZYH);
            if (StringUtils.isEmpty(param.YWLB))
                throw new IllegalArgumentException("[YWLB]非法入参" + param.YWLB);
        }
    }
}
