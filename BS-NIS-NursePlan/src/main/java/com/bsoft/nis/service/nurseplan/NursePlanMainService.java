package com.bsoft.nis.service.nurseplan;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.dangerevaluate.FXPGJLBean;
import com.bsoft.nis.domain.dangerevaluate.HLJHJLBean;
import com.bsoft.nis.domain.nurseplan.*;
import com.bsoft.nis.domain.synchron.InArgument;
import com.bsoft.nis.domain.synchron.Project;
import com.bsoft.nis.domain.synchron.SelectResult;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.nurseplan.support.NursePlanServiceSup;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
import com.bsoft.nis.util.date.DateUtil;
import ctd.net.rpc.Client;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 护理计划主服务
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Service
public class NursePlanMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(NursePlanMainService.class);


    @Autowired
    NursePlanServiceSup service; // 护理计划服务

    @Autowired
    PatientServiceSup patientServiceSup; // 病人服务

    @Autowired
    IdentityService identityService;//种子表服务

    @Autowired
    SystemParamService systemParamService;//用户参数服务

    @Autowired
    DictCachedHandler handler; // 缓存处理器

    @Autowired
    DateTimeService timeService; // 日期时间服务

    /**
     * 获取病区定制计划列表
     *
     * @param zyh  住院号
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    public BizResponse<Plan> getPlanList(String zyh, String bqid, String jgid) {
        BizResponse<Plan> response = new BizResponse<>();
        try {
            List<Plan> planList = service.getPlanList(bqid, jgid);
            for (Plan plan : planList) {
                if (plan.GLLX.equals("2")) {//获取归类下问题及数量
                    plan.SimpleRecord = service.getPlanSimpleRecordListForGlxh(zyh, plan.XH);
                } else if (plan.GLLX.equals("1")) {//获取单个问题及其数量
                    plan.SimpleRecord = service.getPlanSimpleRecordListForWtxh(zyh, plan.XH);
                }
            }
            response.datalist = planList;
            response.isSuccess = true;
            response.message = "获取病区定制计划列表成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取病区定制计划列表失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取病区定制计划列表失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取病区定制焦点列表
     *
     * @param zyh  住院号
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    public BizResponse<Plan> getFocusList(String zyh, String bqid, String jgid) {
        BizResponse<Plan> response = new BizResponse<>();
        try {
            List<Plan> planList = service.getFocusList(bqid, jgid);
            for (Plan plan : planList) {
                if (plan.GLLX.equals("2")) {//获取归类下问题及数量
                    plan.SimpleRecord = service.getFocusSimpleRecordListForGlxh(zyh, plan.XH);
                } else if (plan.GLLX.equals("1")) {//获取单个问题及其数量
                    plan.SimpleRecord = service.getFocusSimpleRecordListForWtxh(zyh, plan.XH);
                }
            }
            response.datalist = planList;
            response.isSuccess = true;
            response.message = "获取病区定制焦点列表成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取病区定制焦点列表失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取病区定制焦点列表失败]服务内部错误!";
        }
        return response;
    }
    /**
     * 2017-5-5 12:16:10 add
     * @param zyh
     * @param jgid
     * @return
     */
    @Deprecated
    public BizResponse<FXPGJLBean> getFXPGJLList(String zyh,String bqdm, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<FXPGJLBean> response = new BizResponse<>();
        List<FXPGJLBean> pgdhList ;
        try {
            pgdhList = service.getFXPGJLList(zyh,bqdm, jgid);
            response.isSuccess = true;
            response.datalist = pgdhList;
            response.message = "查询风险评估记录列表成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询风险评估记录列表失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询风险评估记录列表失败]服务内部错误";
        }
        return  response;
    }
    /**
     * 获取焦点关联数据列表
     * @return
     */
    public BizResponse<FocusRelevanceGroupBean> getFocusRelevanceGroupList(String zyh,String bqdm,String jgid,boolean isQueryEdited) {
        BizResponse<FocusRelevanceGroupBean> response = new BizResponse<>();
        try {
            List<FocusRelevanceBean> focusRelevanceBeanList= service.getFocusRelevanceList();
            List<HL_JD_JL_BEAN> serviceHLJDJL= service.getHLJDJL(zyh,jgid);
            List<FocusRelevanceGroupBean> list =new ArrayList<>();
            for (int i = 0; i <focusRelevanceBeanList.size() ; i++) {
                FocusRelevanceGroupBean focusRelevanceGroupBean=new FocusRelevanceGroupBean();
                focusRelevanceGroupBean.PZBH=focusRelevanceBeanList.get(i).PZBH;
                focusRelevanceGroupBean.DMMC=focusRelevanceBeanList.get(i).DMMC;
               switch (focusRelevanceBeanList.get(i).PZBH){
                   case  "1"://1=风险评估  SQL中 C.DYGLLX = 1  关联和查询记录 在一个SQL里
                       //获取（有关联焦点的）风险评估记录
                       List<FXPGJLBean> fxpgjlBeans=service.getFXPGJLList(zyh,bqdm,jgid);
                      //
                       for(int j = 0; j < fxpgjlBeans.size(); j++) {

                           String pglx=fxpgjlBeans.get(j).PGLX;
                           //获取关联焦点   1对多的记录
                           List<PGLX_JD_GL_FXPG_Bean> pglx_jd_gl_fxpg_beans=service.getJD_GL_List_BY_PGLX(pglx,bqdm,jgid);
                           //赋值
                           fxpgjlBeans.get(j).JD_GL_FXPG_BEANS=pglx_jd_gl_fxpg_beans;
                       }

                       List<FXPGJLBean> fxpgjlBeans_need_edit=new ArrayList<>();
                       List<FXPGJLBean> fxpgjlBeans_edited=new ArrayList<>();
                       //
                       for(int j = 0; j < fxpgjlBeans.size(); j++) {
                           String pgxh=fxpgjlBeans.get(j).PGXH;
                           if (serviceHLJDJL!=null&&serviceHLJDJL.size()>0){
                               boolean edited=false;
                               for (HL_JD_JL_BEAN bean:
                                       serviceHLJDJL) {
                                   if ("1".equals(bean.JLGLLX)&&bean.GLJL.equals(pgxh)){
                                       edited=true;
                                       break;
                                   }
                               }
                                if (edited){
                                    fxpgjlBeans_edited.add(fxpgjlBeans.get(j));
                                }else{
                                    fxpgjlBeans_need_edit.add(fxpgjlBeans.get(j));
                                }
                           }else {
                               fxpgjlBeans_need_edit=new ArrayList<>(fxpgjlBeans);
                           }
                       }

                     if (isQueryEdited) {
                         focusRelevanceGroupBean.FXPGJLBeanList = new ArrayList<>(fxpgjlBeans_edited);
                     }else{
                         focusRelevanceGroupBean.FXPGJLBeanList = new ArrayList<>(fxpgjlBeans_need_edit);
                     }
                       list.add(focusRelevanceGroupBean);
                       break;
                   case  "2":
                       List<HLJHJLBean> hljhjlBeans=service.getHLJHJLList(zyh,jgid);
                       for (int j = 0; j < hljhjlBeans.size(); j++) {
                         String jlwt= hljhjlBeans.get(j).JLWT;
                         // add
                           hljhjlBeans.get(j).ZDMS_List=service.getZDMS_List(jlwt);
                           hljhjlBeans.get(j).CSMS_List=service.getCSMS_List(jlwt);
                       }
                       //
                       List<HLJHJLBean> hljhjlBeans_edited=new ArrayList<>();
                       List<HLJHJLBean> hljhjlBeans_need_edit=new ArrayList<>();
                       //
                       for(int j = 0; j < hljhjlBeans.size(); j++) {
                           String jlwt=hljhjlBeans.get(j).JLWT;
                           if (serviceHLJDJL!=null&&serviceHLJDJL.size()>0){
                               boolean edited=false;
                               for (HL_JD_JL_BEAN bean:
                                       serviceHLJDJL) {
                                   if ("2".equals(bean.JLGLLX)&&bean.GLJL.equals(jlwt)){
                                       edited=true;
                                       break;
                                   }
                               }
                               if (edited){
                                   hljhjlBeans_edited.add(hljhjlBeans.get(j));
                               }else{
                                   hljhjlBeans_need_edit.add(hljhjlBeans.get(j));
                               }
                           }else {
                               hljhjlBeans_need_edit=new ArrayList<>(hljhjlBeans);
                           }
                       }
                       if (isQueryEdited) {
                           focusRelevanceGroupBean.HLJHJLBeanList = new ArrayList<>(hljhjlBeans_edited);
                       }else{
                           focusRelevanceGroupBean.HLJHJLBeanList = new ArrayList<>(hljhjlBeans_need_edit);
                       }
                       list.add(focusRelevanceGroupBean);
                       break;
                   case  "3"://3=生命体征  SQL中 C.DYGLLX = 3
                       //关联的项目
                       List<JD_GL_TZXM_Bean> jd_gl_tzxm_beans=service.getJD_GL_TZXM_List(bqdm,jgid);//fixme 2017年5月4日16:56:53
                       List<JD_GL_SMTZ_Bean> smtz_beans_all=new ArrayList<>();
                       List<String> xmh_DYGLDXTempList=new ArrayList<>();
                       for (int j = 0; j <jd_gl_tzxm_beans.size() ; j++) {
                           String xmh_DYGLDX=jd_gl_tzxm_beans.get(j).DYGLDX;
                           if (xmh_DYGLDXTempList.contains(xmh_DYGLDX)){
                               //不重复查询相同的xmh
                               continue;
                           }
                           String XX=jd_gl_tzxm_beans.get(j).DXQZXX;
                           String SX=jd_gl_tzxm_beans.get(j).DXQZSX;
                           try {
                               /**这里-0.001和+0.001的用意是处理 如 DXQZXX：38.00 , 数据库里的 >='38.00' 38的记录会被排除*/
                               //DecimalFormat decimalFormat =new  DecimalFormat("#.00");
                               //XX= decimalFormat.format(Float.valueOf(jd_gl_tzxm_beans.get(j).DXQZXX)-0.001);
                               XX= String.valueOf(Float.valueOf(jd_gl_tzxm_beans.get(j).DXQZXX)-0.001);
                               SX= String.valueOf(Float.valueOf(jd_gl_tzxm_beans.get(j).DXQZSX)+0.001);
                           }catch (Exception e){
                               //e.printStackTrace();
                           }
                           List<JD_GL_SMTZ_Bean> smtz_beans_temp=service.getJD_GL_SMTZ_List(zyh,jgid,xmh_DYGLDX,XX,SX);
                           xmh_DYGLDXTempList.add(xmh_DYGLDX);
                           //
                           String glxh=jd_gl_tzxm_beans.get(j).GLXH;
                           String gllx=jd_gl_tzxm_beans.get(j).GLLX;
                           String wtxh=jd_gl_tzxm_beans.get(j).WTXH;
                           String dygllx=jd_gl_tzxm_beans.get(j).DYGLLX;
                           //后期赋值
                           for (JD_GL_SMTZ_Bean smtz_bean:smtz_beans_temp
                                ) {
                               smtz_bean.glxh=glxh;
                               smtz_bean.gllx=gllx;
                               smtz_bean.wtxh=wtxh;
                               smtz_bean.dygllx=dygllx;
                           }
                           smtz_beans_all.addAll(smtz_beans_temp);
                       }
                       //
                       List<JD_GL_SMTZ_Bean> smtz_beans_all_edited=new ArrayList<>();
                       List<JD_GL_SMTZ_Bean> smtz_beans_all_need_edit=new ArrayList<>();
                        //
                       for(int j = 0; j < smtz_beans_all.size(); j++) {
                           String cjh=String.valueOf(smtz_beans_all.get(j).CJH);
                           if (serviceHLJDJL!=null&&serviceHLJDJL.size()>0){
                               boolean edited=false;
                               for (HL_JD_JL_BEAN bean:
                                       serviceHLJDJL) {
                                   if ("3".equals(bean.JLGLLX)&&bean.GLJL.equals(cjh)){
                                       edited=true;
                                       break;
                                   }
                               }
                               if (edited){
                                   smtz_beans_all_edited.add(smtz_beans_all.get(j));
                               }else{
                                   smtz_beans_all_need_edit.add(smtz_beans_all.get(j));
                               }
                           }else {
                               smtz_beans_all_need_edit=new ArrayList<>(smtz_beans_all);
                           }
                       }
                       if (isQueryEdited) {
                           focusRelevanceGroupBean.JD_GL_SMTZ_BeanList = new ArrayList<>(smtz_beans_all_edited);
                       }else{
                           focusRelevanceGroupBean.JD_GL_SMTZ_BeanList = new ArrayList<>(smtz_beans_all_need_edit);
                       }
                       //
                       list.add(focusRelevanceGroupBean);
                       break;
                   case  "4":
                       List<JD_GL_JYXM_Bean> jd_gl_jyxm_beans=service.getJD_GL_JYXM_List(bqdm,jgid);
                       List<JYXM_PATIENTINFO_Bean> jyxm_all=new ArrayList<>();
                       List<String> xmid_DYGLDXTempList=new ArrayList<>();
                       for (int j = 0; j <jd_gl_jyxm_beans.size() ; j++) {
                           String xmid_DYGLDX=jd_gl_jyxm_beans.get(j).DYGLDX;
                           String pdqz_4_JGTS=jd_gl_jyxm_beans.get(j).PDQZ;
                         if (xmid_DYGLDXTempList.contains(xmid_DYGLDX)){
                               //不重复查询相同的xmid
                               continue;
                           }
                           List<JYXM_PATIENTINFO_Bean> jyxm_temp=service.getJYXM_PATIENTINFO_List(zyh,xmid_DYGLDX,pdqz_4_JGTS,jgid);
                           xmid_DYGLDXTempList.add(xmid_DYGLDX);
                           //
                           String glxh=jd_gl_jyxm_beans.get(j).GLXH;
                           String gllx=jd_gl_jyxm_beans.get(j).GLLX;
                           String wtxh=jd_gl_jyxm_beans.get(j).WTXH;
                           String dygllx=jd_gl_jyxm_beans.get(j).DYGLLX;
                           //后期赋值
                           for (JYXM_PATIENTINFO_Bean jyxmBean:jyxm_temp
                                   ) {
                               jyxmBean.glxh=glxh;
                               jyxmBean.gllx=gllx;
                               jyxmBean.wtxh=wtxh;
                               jyxmBean.dygllx=dygllx;
                           }
                           jyxm_all.addAll(jyxm_temp);
                       }
                       //
                       List<JYXM_PATIENTINFO_Bean> jyxm_all_edited=new ArrayList<>();
                       List<JYXM_PATIENTINFO_Bean> jyxm_all_need_edit=new ArrayList<>();
                       //
                       for(int j = 0; j < jyxm_all.size(); j++) {
                           String ybhm=jyxm_all.get(j).YBHM;
                           String xmid=jyxm_all.get(j).XMID;
                           String jgid_XX=jyxm_all.get(j).JGID;
                           if (serviceHLJDJL!=null&&serviceHLJDJL.size()>0){
                               boolean edited=false;
                               for (HL_JD_JL_BEAN bean:
                                       serviceHLJDJL) {
                                   if ("4".equals(bean.JLGLLX)&&bean.GLJL.equals(ybhm + '|' + xmid + '|' + jgid_XX)){
                                       edited=true;
                                       break;
                                   }
                               }
                               if (edited){
                                   jyxm_all_edited.add(jyxm_all.get(j));
                               }else{
                                   jyxm_all_need_edit.add(jyxm_all.get(j));
                               }
                           }else {
                               jyxm_all_need_edit=new ArrayList<>(jyxm_all);
                           }
                       }
                       if (isQueryEdited) {
                           focusRelevanceGroupBean.JYXM_PATIENTINFO_BeanList = new ArrayList<>(jyxm_all_edited);
                       }else{
                           focusRelevanceGroupBean.JYXM_PATIENTINFO_BeanList = new ArrayList<>(jyxm_all_need_edit);
                       }
                       //
                       list.add(focusRelevanceGroupBean);
                       break;
               }
            }

            response.datalist = list;
            response.isSuccess = true;
            response.message = "获取焦点关联数据列表成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取焦点关联数据列表失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取焦点关联数据列表失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取焦点中关联的体征项目列表
     * @param bqdm
     * @param jgid
     * @return
     */
    @Deprecated
    public BizResponse<JD_GL_TZXM_Bean> getJD_GL_TZXM_List(String bqdm,String jgid) {
        BizResponse<JD_GL_TZXM_Bean> response = new BizResponse<>();
        try {
            List<JD_GL_TZXM_Bean> list = service.getJD_GL_TZXM_List(bqdm,jgid);
            response.datalist = list;
            response.isSuccess = true;
            response.message = "获取焦点中关联的体征项目列表成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取焦点中关联的体征项目列表失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取焦点中关联的体征项目列表失败]服务内部错误!";
        }
        return response;
    }
    /**
     * 获取新增护理问题
     *
     * @param wtxh 问题序号
     * @param jgid 机构id
     * @return
     */
    public BizResponse<Problem> getNewProblem(String wtxh, String jgid) {
        BizResponse<Problem> response = new BizResponse<>();
        try {
            Problem problem = service.getProblem(wtxh, jgid);
            problem.JHMB = service.getGoalList(wtxh);
            problem.JHCS = new ArrayList<>();
            problem.JHCSTemplate = service.getMeasureList(wtxh);
            problem.ZDYJ = service.getDiagnosticBasisList(wtxh);
            problem.XGYSList = service.getRelevantFactorList(wtxh);
            response.data = problem;
            response.isSuccess = true;
            response.message = "获取新增护理问题数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取新增护理问题数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取新增护理问题数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 保存护理计划问题
     *
     * @param problemSaveData 护理计划保存数据对象
     * @return
     */
    public BizResponse<Problem> saveNursePlanData(ProblemSaveData problemSaveData) {
        BizResponse<Problem> response = new BizResponse<>();
        try {
            if (problemSaveData == null || problemSaveData.Problem == null) {
                response.isSuccess = false;
                response.message = "[保存护理计划问题数据失败]传入参数有误!";
                return response;
            }
            if (problemSaveData.Problem.JLWT == null) {//新增问题、措施、目标、诊断记录
                response = addNursePlanData(problemSaveData);
            } else {//修改问题、目标、措施、诊断记录
                response = editNursePlanData(problemSaveData);
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存护理计划问题数据失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存护理计划问题数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 保存护理焦点问题
     *
     * @param problemSaveData 护理焦点保存数据对象
     * @return
     */
    public BizResponse<Problem> saveNurseFocusData(ProblemSaveData problemSaveData) {
        BizResponse<Problem> response = new BizResponse<>();
        try {
            if (problemSaveData == null || problemSaveData.Problem == null) {
                response.isSuccess = false;
                response.message = "[保存护理焦点问题数据失败]传入参数有误!";
                return response;
            }
            if (problemSaveData.Problem.JLWT == null) {//新增问题、措施、目标、诊断记录
                response = addNurseFocusData(problemSaveData);
            } else {//修改问题、目标、措施、诊断记录
                response = editNurseFocusData(problemSaveData);
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存护理焦点问题数据失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存护理焦点问题数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 删除护理计划问题
     *
     * @param jlwt 记录问题
     * @param jgid 机构id
     * @return
     */
    public BizResponse<String> deleteNursePlanProblem(String jlwt, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
            boolean isSucess = service.deleteNursePlanProblem(jlwt) > 0;
            if (isSucess) {
                deleSyncData(jlwt, jlwt, "1", "4", jgid);
                response.data = "删除护理计划问题数据成功!";
                response.isSuccess = true;
                response.message = "删除护理计划问题数据成功!";
            } else {
                response.data = "删除护理计划问题数据失败!";
                response.isSuccess = false;
                response.message = "删除护理计划问题数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除护理计划问题数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除护理计划问题数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 删除护理焦点问题
     *
     * @param jlwt 记录问题
     * @param jgid 机构id
     * @return
     */
    public BizResponse<String> deleteNurseFocusProblem(String jlwt, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
            boolean isSucess = service.deleteNurseFocusProblem(jlwt) > 0;
            if (isSucess) {
                deleSyncData(jlwt, jlwt, "1", "8", jgid);
                response.data = "删除护理焦点问题数据成功!";
                response.isSuccess = true;
                response.message = "删除护理焦点问题数据成功!";
            } else {
                response.data = "删除护理焦点问题数据失败!";
                response.isSuccess = false;
                response.message = "删除护理焦点问题数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除护理焦点问题数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除护理焦点问题数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 结束护理计划问题
     *
     * @param jlwt 记录问题
     * @param glxh 关联序号
     * @param wtxh 问题序号
     * @param zyh  住院号
     * @param yhid 用户id
     * @param jgid 机构id
     * @return
     */
    public BizResponse<Problem> terminateNursePlanProblem(String jlwt, String glxh, String wtxh, String zyh, String yhid, String jgid) {
        BizResponse<Problem> response = new BizResponse<>();
        try {
            String now = timeService.now(DataSource.MOB);
            boolean isSucess = service.terminateNursePlanProblem(jlwt, now, yhid) > 0;
            if (isSucess) {
                response.datalist = getExistPlanProblem(wtxh, glxh, zyh, jgid);
                response.isSuccess = true;
                response.message = "结束护理计划问题成功!";
            } else {
                response.isSuccess = false;
                response.message = "结束护理计划问题失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[结束护理计划问题失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[结束护理计划问题失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 结束护理计划焦点
     *
     * @param jlwt 记录问题
     * @param glxh 关联序号
     * @param wtxh 问题序号
     * @param zyh  住院号
     * @param yhid 用户id
     * @param jgid 机构id
     * @return
     */
    public BizResponse<Problem> terminateNurseFocusProblem(String jlwt, String glxh, String wtxh, String zyh, String yhid, String jgid) {
        BizResponse<Problem> response = new BizResponse<>();
        try {
            String now = timeService.now(DataSource.MOB);
            boolean isSucess = service.terminateNurseFocusProblem(jlwt, now, yhid) > 0;
            if (isSucess) {
                response.datalist = getExistFocusProblem(wtxh, glxh, zyh, jgid);
                response.isSuccess = true;
                response.message = "结束护理焦点问题成功!";
            } else {
                response.isSuccess = false;
                response.message = "结束护理焦点问题失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[结束护理焦点问题失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[结束护理焦点问题失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取已记录的问题列表
     *
     * @param wtxh 问题序号
     * @param glxh 归类序号
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     */
    public BizResponse<Problem> getPlanProblemList(String wtxh, String glxh, String zyh, String jgid) {
        BizResponse<Problem> response = new BizResponse<>();
        try {
            List<Problem> problemList = getExistPlanProblem(wtxh, glxh, zyh, jgid);
            response.datalist = problemList;
            response.isSuccess = true;
            response.message = "获取已记录的问题列表数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取已记录的问题列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取已记录的问题列表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取已记录的问题列表
     *
     * @param wtxh 问题序号
     * @param glxh 归类序号
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     */
    public BizResponse<Problem> getFocusProblemList(String wtxh, String glxh, String zyh, String jgid) {
        BizResponse<Problem> response = new BizResponse<>();
        try {
            List<Problem> problemList = getExistFocusProblem(wtxh, glxh, zyh, jgid);
            response.datalist = problemList;
            response.isSuccess = true;
            response.message = "获取已记录的问题列表数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取已记录的问题列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取已记录的问题列表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取评价列表(记录及评价项目)-计划
     *
     * @param jlwt 记录问题
     * @param wtxh 问题序号
     * @param jgid 机构id
     * @return
     */
    public BizResponse<EvaluateAndRecord> getPlanEvaluateList(String jlwt, String wtxh, String jgid) {
        BizResponse<EvaluateAndRecord> response = new BizResponse<>();
        try {
            response.data = getRealPlanEvaluateList(jlwt, wtxh, jgid);
            response.isSuccess = true;
            response.message = "获取已记录的问题列表数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取已记录的问题列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取已记录的问题列表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取评价列表(记录及评价项目)-焦点
     *
     * @param jlwt 记录问题
     * @param wtxh 问题序号
     * @param jgid 机构id
     * @return
     */
    public BizResponse<EvaluateAndRecord> getFocusEvaluateList(String jlwt, String wtxh, String jgid) {
        BizResponse<EvaluateAndRecord> response = new BizResponse<>();
        try {
            response.data = getRealFocusEvaluateList(jlwt, wtxh, jgid);
            response.isSuccess = true;
            response.message = "获取已记录的问题列表数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取已记录的问题列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取已记录的问题列表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 保存问题评价-计划
     *
     * @param problemEvaluateSaveData
     * @return
     */
    public BizResponse<EvaluateAndRecord> savePlanProblemEvaluate(ProblemEvaluateSaveData problemEvaluateSaveData) {
        BizResponse<EvaluateAndRecord> response = new BizResponse<>();
        try {
            List<Evaluate> evaluateList = problemEvaluateSaveData.evaluateList;
            String pjzh = String.valueOf(identityService.getIdentityMax("IENR_JHPJJL_PJZH",DataSource.MOB));
            for (Evaluate item : evaluateList) {
                if (item.selected) {
                    item.JLPJ = String.valueOf(identityService.getIdentityMax("IENR_JHPJJL",DataSource.MOB));
                }
            }
            keepOrRoutingDateSource(DataSource.MOB);
            return realSavePlanProblemEvaluate(problemEvaluateSaveData, pjzh);

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存问题评价数据失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存问题评价数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 保存问题评价-焦点
     *
     * @param problemEvaluateSaveData
     * @return
     */
    public BizResponse<EvaluateAndRecord> saveFocusProblemEvaluate(ProblemEvaluateSaveData problemEvaluateSaveData) {
        BizResponse<EvaluateAndRecord> response = new BizResponse<>();
        try {
            List<Evaluate> evaluateList = problemEvaluateSaveData.evaluateList;
            String pjzh = String.valueOf(identityService.getIdentityMax("IENR_JHPJJL_PJZH",DataSource.MOB));
            for (Evaluate item : evaluateList) {
                if (item.selected) {
                    item.JLPJ = String.valueOf(identityService.getIdentityMax("IENR_JDPJJL",DataSource.MOB));
                }
            }
            keepOrRoutingDateSource(DataSource.MOB);
            return realSaveFocusProblemEvaluate(problemEvaluateSaveData, pjzh);

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存焦点评价数据失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存焦点评价数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 删除问题评价
     *
     * @param jlwt 记录问题
     * @param jlpj 记录评价
     * @param jgid 机构id
     * @return
     */
    public BizResponse<String> deletePlanProblemEvaluate(String jlwt, String jlpj, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
	        String pjzh = service.getEvaluatePjzhByJlpj(jlpj, "4");
            boolean isSucess = service.deletePlanProblemEvaluate(jlpj) > 0;
            if (isSucess) {
                deleSyncData(jlwt, pjzh, "2", "4", jgid);
                response.isSuccess = true;
                response.message = "删除问题评价数据成功!";
            } else {
                response.isSuccess = false;
                response.message = "删除问题评价数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除问题评价数据失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除问题评价数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 删除焦点评价
     *
     * @param jljd 记录焦点
     * @param jlpj 记录评价
     * @param jgid 机构id
     * @return
     */
    public BizResponse<String> deleteFocusProblemEvaluate(String jljd, String jlpj, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
	        String pjzh = service.getEvaluatePjzhByJlpj(jlpj, "8");
            boolean isSucess = service.deleteFocusProblemEvaluate(jlpj) > 0;
            if (isSucess) {
                deleSyncData(jljd, pjzh, "2", "8", jgid);
                response.isSuccess = true;
                response.message = "删除焦点评价数据成功!";
            } else {
                response.isSuccess = false;
                response.message = "删除焦点评价数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除焦点评价数据失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除检点评价数据失败]服务内部错误!";
        }
        return response;
    }


    private List<Problem> getExistPlanProblem(String wtxh, String glxh, String zyh, String jgid)
            throws SQLException, DataAccessException {
        List<Problem> problemList = service.getPlanProblemHistoryList(wtxh, glxh, zyh, jgid);
        for (Problem problem : problemList) {
            problem.JHCS = service.getPlanMeasureHistoryList(problem.JLWT);
            for (Measure item : problem.JHCS) {
                if (!StringUtils.isBlank(item.KSGH)) {
                    item.KSXM = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, item.KSGH);
                }
                if (!StringUtils.isBlank(item.JSGH)) {
                    item.JSXM = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, item.JSGH);
                }
                if (!StringUtils.isBlank(item.JLCS)) {
                    item.SELECTED = true;
                }
            }
            problem.JHCSTemplate = service.getMeasureList(wtxh);
            problem.JHMB = service.getGoalHistoryList(problem.WTXH, problem.JLWT);
            for (Goal item : problem.JHMB) {
                if (!StringUtils.isBlank(item.JLMB)) {
                    item.SELECTED = true;
                }
            }
            problem.ZDYJ = service.getPlanDiagnosticBasisHistoryList(problem.WTXH, problem.JLWT);
            for (DiagnosticBasis item : problem.ZDYJ) {
                if (!StringUtils.isBlank(item.JLZD)) {
                    item.SELECTED = true;
                }
            }
            problem.XGYSList = service.getRelevantFactorHistoryList(problem.WTXH, problem.JLWT);
            for (RelevantFactor item : problem.XGYSList) {
                if (!StringUtils.isBlank(item.JLYS)) {
                    item.SELECTED = true;
                }
            }
        }
        return problemList;
    }


    private List<Problem> getExistFocusProblem(String wtxh, String glxh, String zyh, String jgid)
            throws SQLException, DataAccessException {
        List<Problem> problemList = service.getFocusProblemHistoryList(wtxh, glxh, zyh, jgid);
        for (Problem problem : problemList) {
            problem.JHCS = service.getFocusMeasureHistoryList(problem.JLWT);
            for (Measure item : problem.JHCS) {
                if (!StringUtils.isBlank(item.KSGH)) {
                    item.KSXM = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, item.KSGH);
                }
                if (!StringUtils.isBlank(item.JSGH)) {
                    item.JSXM = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, item.JSGH);
                }
                if (!StringUtils.isBlank(item.JLCS)) {
                    item.SELECTED = true;
                }
            }
            problem.JHCSTemplate = service.getMeasureList(wtxh);
            problem.JHMB = null;
            problem.ZDYJ = service.getFocusDiagnosticBasisHistoryList(problem.WTXH, problem.JLWT);
            for (DiagnosticBasis item : problem.ZDYJ) {
                if (!StringUtils.isBlank(item.JLZD)) {
                    item.SELECTED = true;
                }
            }
            problem.XGYSList = null;
        }
        return problemList;
    }


    private EvaluateAndRecord getRealPlanEvaluateList(String jlwt, String wtxh, String jgid)
            throws SQLException, DataAccessException {
        EvaluateAndRecord evaluateAndRecord = new EvaluateAndRecord();
        evaluateAndRecord.PJLS = service.getPlanEvaluateHistoryList(jlwt);//评价记录
        for (Evaluate item : evaluateAndRecord.PJLS) {
            if (!StringUtils.isBlank(item.PJGH)) {
                item.PJHS = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, item.PJGH);
            }
        }
        evaluateAndRecord.PJXM = service.getEvaluateTemplateList(wtxh);//评价模板数据
        String now = timeService.now(DataSource.MOB);
        for (Evaluate item : evaluateAndRecord.PJXM) {
            item.PJSJ = now;
        }
        return evaluateAndRecord;
    }


    private EvaluateAndRecord getRealFocusEvaluateList(String jlwt, String wtxh, String jgid)
            throws SQLException, DataAccessException {
        EvaluateAndRecord evaluateAndRecord = new EvaluateAndRecord();
        evaluateAndRecord.PJLS = service.getFocusEvaluateHistoryList(jlwt);//评价记录
        for (Evaluate item : evaluateAndRecord.PJLS) {
            if (!StringUtils.isBlank(item.PJGH)) {
                item.PJHS = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, item.PJGH);
            }
        }
        evaluateAndRecord.PJXM = service.getEvaluateTemplateList(wtxh);//评价模板数据
        String now = timeService.now(DataSource.MOB);
        for (Evaluate item : evaluateAndRecord.PJXM) {
            item.PJSJ = now;
        }
        return evaluateAndRecord;
    }

    private BizResponse<Problem> addNursePlanData(ProblemSaveData problemSaveData)
            throws SQLException, DataAccessException {
        Problem problem = problemSaveData.Problem;
        problem.JLWT = String.valueOf(identityService.getIdentityMax("IENR_JHWTJL",DataSource.MOB));
        if (problem.JHCS != null) {//措施
            String previousCszh = "";//上一组的措施组号
            String cszhIdentity = "";//暂存种子表措施组号
            for (Measure measure : problem.JHCS) {
                if (measure.SELECTED) {
                    measure.JLCS = String.valueOf(identityService.getIdentityMax("IENR_JHCSJL",DataSource.MOB));
                    if (!previousCszh.equals(measure.CSZH)) {
                        previousCszh = measure.CSZH;
                        cszhIdentity = String.valueOf(identityService.getIdentityMax("IENR_JHCSJL_CSZH",DataSource.MOB));
                    }
                    measure.CSZH = cszhIdentity;
                }
            }
        }
        if (problem.JHMB != null) {//目标
            for (Goal goal : problem.JHMB) {
                if (goal.SELECTED) {
                    goal.JLMB = String.valueOf(identityService.getIdentityMax("IENR_JHMBJL",DataSource.MOB));
                }
            }
        }
        if (problem.ZDYJ != null) {//诊断依据
            for (DiagnosticBasis item : problem.ZDYJ) {
                if (item.SELECTED) {
                    item.JLZD = String.valueOf(identityService.getIdentityMax("IENR_JHZDJL",DataSource.MOB));
                }
            }
        }
        if (problem.XGYSList != null) {//相关因素
            for (RelevantFactor item : problem.XGYSList) {
                if (item.SELECTED) {//插入
                    item.JLYS = String.valueOf(identityService.getIdentityMax("IENR_JHYSJL",DataSource.MOB));
                }
            }
        }
        keepOrRoutingDateSource(DataSource.MOB);
        return realAddNursePlanData(problemSaveData);
    }

    private BizResponse<Problem> addNurseFocusData(ProblemSaveData problemSaveData)
            throws SQLException, DataAccessException {
        Problem problem = problemSaveData.Problem;
        problem.JLWT = String.valueOf(identityService.getIdentityMax("IENR_HLJDJL",DataSource.MOB));
        if (problem.JHCS != null) {//措施
            String previousCszh = "";//上一组的措施组号
            String cszhIdentity = "";//暂存种子表措施组号
            for (Measure measure : problem.JHCS) {
                if (measure.SELECTED) {
                    measure.JLCS = String.valueOf(identityService.getIdentityMax("IENR_JDCSJL",DataSource.MOB));
                    if (!previousCszh.equals(measure.CSZH)) {
                        previousCszh = measure.CSZH;
                        cszhIdentity = String.valueOf(identityService.getIdentityMax("IENR_JHCSJL_CSZH",DataSource.MOB));
                    }
                    measure.CSZH = cszhIdentity;
                }
            }
        }
        if (problem.ZDYJ != null) {//诊断依据
            for (DiagnosticBasis item : problem.ZDYJ) {
                if (item.SELECTED) {
                    item.JLZD = String.valueOf(identityService.getIdentityMax("IENR_JDZDJL",DataSource.MOB));
                }
            }
        }
        keepOrRoutingDateSource(DataSource.MOB);
        return realAddNurseFocusData(problemSaveData);
    }

    /**
     * 新增问题、措施、目标、诊断记录
     * <p>
     * 注意：调用此方法之前需要显式指定事务
     *
     * @param problemSaveData 护理计划保存数据对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private BizResponse<Problem> realAddNursePlanData(ProblemSaveData problemSaveData)
            throws SQLException, DataAccessException {
        BizResponse<Problem> response = new BizResponse<>();
        Problem problem = problemSaveData.Problem;
        String now = DateUtil.getApplicationDateTime();
        String kssj = StringUtils.isBlank(problem.KSSJ) ? now : problem.KSSJ;
        boolean isSucess;
        isSucess = service.addNursePlanProblem(problem.JLWT, problem.WTXH, problem.XGYS, now, problemSaveData.YHID,
                kssj, problemSaveData.YHID, problem.GLXH, problemSaveData.GLLX, problem.WTLX, problem.WTXH,
                problem.WTMS, problemSaveData.ZYH, problemSaveData.BQID, problemSaveData.JGID) > 0;
        if (!isSucess) {
            response.isSuccess = false;
            response.message = "[保存护理计划问题数据失败]数据库执行错误!";
        }
        if (problem.JHCS != null) {//措施
            for (Measure measure : problem.JHCS) {
                if (measure.SELECTED) {
                    isSucess = service.addNursePlanMeasure(measure.JLCS, problem.JLWT, measure.CSMS, measure.CSXH, measure.ZDYBZ, measure.XJBZ,
                            measure.KSSJ, measure.KSGH, measure.JSSJ, measure.JSGH, measure.CSZH) > 0;
                    if (!isSucess) {
                        response.isSuccess = false;
                        response.message = "[保存护理计划措施数据失败]数据库执行错误!";
                    }
                }
            }
        }
        if (problem.JHMB != null) {//目标
            for (Goal goal : problem.JHMB) {
                if (goal.SELECTED) {
                    isSucess = service.addNursePlanGoal(goal.JLMB, problem.JLWT, goal.MBXH, goal.MBMS, goal.ZDYBZ) > 0;
                    if (!isSucess) {
                        response.isSuccess = false;
                        response.message = "[保存护理计划目标数据失败]数据库执行错误!";
                    }
                }
            }
        }
        if (problem.ZDYJ != null) {//诊断依据
            for (DiagnosticBasis item : problem.ZDYJ) {
                if (item.SELECTED) {
                    isSucess = service.addNursePlanDiagnosticBasis(item.JLZD, problem.JLWT, item.ZDXH, item.ZDMS, item.ZDYBZ) > 0;
                    if (!isSucess) {
                        response.isSuccess = false;
                        response.message = "[保存护理计划诊断依据数据失败]数据库执行错误!";
                    }
                }
            }
        }
        if (problem.XGYSList != null) {//相关因素
            for (RelevantFactor item : problem.XGYSList) {
                if (item.SELECTED) {
                    isSucess = service.addNursePlanRelevantFactor(item.JLYS, problem.JLWT, item.YSXH, item.YSMS) > 0;
                    if (!isSucess) {
                        response.isSuccess = false;
                        response.message = "[保存护理计划诊断依据数据失败]数据库执行错误!";
                    }
                }
            }
        }
        if (isSucess) {
            List<Problem> problemList = getExistPlanProblem(problem.WTXH, problem.GLXH, problemSaveData.ZYH, problemSaveData.JGID);
            for (Problem newProblem : problemList) {
                if (problem.JLWT.equals(newProblem.JLWT)) {
                    Response<SelectResult> syncResponse = buildProblemSyncData(problemSaveData, "0", "1");
                    if (syncResponse.ReType == 2) {
                        newProblem.IsSync = true;
                        newProblem.SyncData = syncResponse.Data;
                    }
                }
            }
            response.datalist = problemList;
            response.isSuccess = true;
            response.message = "保存护理计划问题数据成功!";
        }
        return response;
    }

    /**
     * 新增问题、措施、目标、诊断记录
     * <p>
     * 注意：调用此方法之前需要显式指定事务
     *
     * @param problemSaveData 护理焦点保存数据对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private BizResponse<Problem> realAddNurseFocusData(ProblemSaveData problemSaveData)
            throws SQLException, DataAccessException {
        BizResponse<Problem> response = new BizResponse<>();
        Problem problem = problemSaveData.Problem;
        String now = DateUtil.getApplicationDateTime();
        String kssj = StringUtils.isBlank(problem.KSSJ) ? now : problem.KSSJ;
        boolean isSucess;
        isSucess = service.addNurseFocusProblem(problem.JLWT, problem.WTXH, now, problemSaveData.YHID,
                kssj, problemSaveData.YHID, problem.GLXH, problemSaveData.GLLX, problem.WTLX, problem.WTXH,
                problem.WTMS, problemSaveData.ZYH, problemSaveData.BQID, problemSaveData.JGID,problemSaveData.JLGLLX,problemSaveData.GLJL) > 0;
        if (!isSucess) {
            response.isSuccess = false;
            response.message = "[保存护理焦点问题数据失败]数据库执行错误!";
        }
        if (problem.JHCS != null) {//措施
            for (Measure measure : problem.JHCS) {
                if (measure.SELECTED) {
                    isSucess = service.addNurseFocusMeasure(measure.JLCS, problem.JLWT, measure.CSMS, measure.CSXH, measure.ZDYBZ, measure.XJBZ,
                            measure.KSSJ, measure.KSGH, measure.JSSJ, measure.JSGH, measure.CSZH) > 0;
                    if (!isSucess) {
                        response.isSuccess = false;
                        response.message = "[保存护理计划措施数据失败]数据库执行错误!";
                    }
                }
            }
        }
        if (problem.ZDYJ != null) {//诊断依据
            for (DiagnosticBasis item : problem.ZDYJ) {
                if (item.SELECTED) {
                    isSucess = service.addNurseFocusDiagnosticBasis(item.JLZD, problem.JLWT, item.ZDXH, item.ZDMS, item.ZDYBZ) > 0;
                    if (!isSucess) {
                        response.isSuccess = false;
                        response.message = "[保存护理计划诊断依据数据失败]数据库执行错误!";
                    }
                }
            }
        }
        if (isSucess) {
            List<Problem> problemList = getExistFocusProblem(problem.WTXH, problem.GLXH, problemSaveData.ZYH, problemSaveData.JGID);
            for (Problem newProblem : problemList) {
                if (problem.JLWT.equals(newProblem.JLWT)) {
                    Response<SelectResult> syncResponse = buildProblemSyncData(problemSaveData, "0", "1");
                    if (syncResponse.ReType == 2) {
                        newProblem.IsSync = true;
                        newProblem.SyncData = syncResponse.Data;
                    }
                }
            }
            response.datalist = problemList;
            response.isSuccess = true;
            response.message = "保存护理计划问题数据成功!";
        }
        return response;
    }

    private BizResponse<Problem> editNursePlanData(ProblemSaveData problemSaveData)
            throws SQLException, DataAccessException {
        Problem problem = problemSaveData.Problem;
        if (problem.JHCS != null) {//措施
            String previousCszh = "";
            String cszhIdentity = "";
            for (Measure measure : problem.JHCS) {
                if (measure.SELECTED && StringUtils.isBlank(measure.JLCS)) {//新增
                    measure.JLCS = "add:" + String.valueOf(identityService.getIdentityMax("IENR_JHCSJL",DataSource.MOB));
                    if (!previousCszh.equals(measure.CSZH)) {
                        previousCszh = measure.CSZH;
                        cszhIdentity = String.valueOf(identityService.getIdentityMax("IENR_JHCSJL_CSZH",DataSource.MOB));
                    }
                    measure.CSZH = cszhIdentity;
                }
            }
        }
        if (problem.JHMB != null) {//目标
            for (Goal goal : problem.JHMB) {
                if (goal.SELECTED && StringUtils.isBlank(goal.JLMB)) {//插入
                    goal.JLMB = "add:" + String.valueOf(identityService.getIdentityMax("IENR_JHMBJL",DataSource.MOB));
                }
            }
        }
        if (problem.ZDYJ != null) {//诊断依据
            for (DiagnosticBasis item : problem.ZDYJ) {
                if (item.SELECTED && StringUtils.isBlank(item.JLZD)) {//插入
                    item.JLZD = "add:" + String.valueOf(identityService.getIdentityMax("IENR_JHZDJL",DataSource.MOB));
                }
            }
        }
        if (problem.XGYSList != null) {//相关因素
            for (RelevantFactor item : problem.XGYSList) {
                if (item.SELECTED && StringUtils.isBlank(item.JLYS)) {//插入
                    item.JLYS = "add:" + String.valueOf(identityService.getIdentityMax("IENR_JHYSJL",DataSource.MOB));
                }
            }
        }
        keepOrRoutingDateSource(DataSource.MOB);
        return realEditNursePlanData(problemSaveData);
    }

    private BizResponse<Problem> editNurseFocusData(ProblemSaveData problemSaveData)
            throws SQLException, DataAccessException {
        Problem problem = problemSaveData.Problem;
        if (problem.JHCS != null) {//措施
            String previousCszh = "";
            String cszhIdentity = "";
            for (Measure measure : problem.JHCS) {
                if (measure.SELECTED && StringUtils.isBlank(measure.JLCS)) {//新增
                    measure.JLCS = "add:" + String.valueOf(identityService.getIdentityMax("IENR_JDCSJL",DataSource.MOB));
                    if (!previousCszh.equals(measure.CSZH)) {
                        previousCszh = measure.CSZH;
                        cszhIdentity = String.valueOf(identityService.getIdentityMax("IENR_JHCSJL_CSZH",DataSource.MOB));
                    }
                    measure.CSZH = cszhIdentity;
                }
            }
        }
        if (problem.ZDYJ != null) {//诊断依据
            for (DiagnosticBasis item : problem.ZDYJ) {
                if (item.SELECTED && StringUtils.isBlank(item.JLZD)) {//插入
                    item.JLZD = "add:" + String.valueOf(identityService.getIdentityMax("IENR_JDZDJL",DataSource.MOB));
                }
            }
        }
        keepOrRoutingDateSource(DataSource.MOB);
        return realEditNurseFocusData(problemSaveData);
    }

    /**
     * 修改问题、目标、措施、诊断记录
     * <p>
     * 注意：调用此方法之前需要显式指定事务
     *
     * @param problemSaveData 护理计划保存数据对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private BizResponse<Problem> realEditNursePlanData(ProblemSaveData problemSaveData)
            throws SQLException, DataAccessException {
        BizResponse<Problem> response = new BizResponse<>();
        Problem problem = problemSaveData.Problem;
        String now = DateUtil.getApplicationDateTime();
        String kssj = StringUtils.isBlank(problem.KSSJ) ? now : problem.KSSJ;
        boolean isSucess;
        isSucess = service.editNursePlanProblem(problem.JLWT, problem.WTMS, problem.XGYS, kssj) > 0;
        if (!isSucess) {
            response.isSuccess = false;
            response.message = "[保存护理计划问题数据失败]数据库执行错误!";
        }
        if (problem.JHCS != null) {//措施
            for (Measure measure : problem.JHCS) {
                if (measure.SELECTED) {
                    if (measure.JLCS.contains("add:")) {//新增
                        measure.JLCS = measure.JLCS.replace("add:", "");
                        measure.CSZH = measure.CSZH.replace("add:", "");
                        isSucess = service.addNursePlanMeasure(measure.JLCS, problem.JLWT, measure.CSMS, measure.CSXH, measure.ZDYBZ, measure.XJBZ,
                                measure.KSSJ, measure.KSGH, measure.JSSJ, measure.JSGH, measure.CSZH) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理计划措施数据失败]数据库执行错误!";
                        }
                    } else {//修改
                        if (measure.MODIFIED) {
                            isSucess = service.editNursePlanMeasure(measure.JLCS, measure.CSMS, measure.XJBZ, measure.KSSJ, measure.JSSJ) > 0;
                            if (!isSucess) {
                                response.isSuccess = false;
                                response.message = "[保存护理计划措施数据失败]数据库执行错误!";
                            }
                        }
                    }
                } else {
                    if (!StringUtils.isBlank(measure.JLCS)) {//删除
                        isSucess = service.deleteNursePlanMeasure(measure.JLCS) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理计划措施数据失败]数据库执行错误!";
                        }
                    }
                }
            }
        }
        if (problem.JHMB != null) {//目标
            for (Goal goal : problem.JHMB) {
                if (goal.SELECTED) {
                    if (goal.JLMB.contains("add:")) {//插入
                        goal.JLMB = goal.JLMB.replace("add:", "");
                        isSucess = service.addNursePlanGoal(goal.JLMB, problem.JLWT, goal.MBXH, goal.MBMS, goal.ZDYBZ) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理计划目标数据失败]数据库执行错误!";
                        }
                    } else {
                        if (goal.MODIFIED) {//修改
                            isSucess = service.editNursePlanGoal(goal.JLMB, goal.MBMS) > 0;
                            if (!isSucess) {
                                response.isSuccess = false;
                                response.message = "[保存护理计划目标数据失败]数据库执行错误!";
                            }
                        }
                    }

                } else {
                    if (!StringUtils.isBlank(goal.JLMB)) {//删除
                        isSucess = service.deleteNursePlanGoal(goal.JLMB) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理计划目标数据失败]数据库执行错误!";
                        }
                    }
                }
            }
        }
        if (problem.ZDYJ != null) {//诊断依据
            for (DiagnosticBasis item : problem.ZDYJ) {
                if (item.SELECTED) {
                    if (item.JLZD.contains("add:")) {//插入
                        item.JLZD = item.JLZD.replace("add:", "");
                        isSucess = service.addNursePlanDiagnosticBasis(item.JLZD, problem.JLWT, item.ZDXH, item.ZDMS, item.ZDYBZ) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理计划诊断依据数据失败]数据库执行错误!";
                        }
                    } else {
                        if (item.MODIFIED) {//修改
                            isSucess = service.editNursePlanDiagnosticBasis(item.JLZD, item.ZDMS) > 0;
                            if (!isSucess) {
                                response.isSuccess = false;
                                response.message = "[保存护理计划诊断依据数据失败]数据库执行错误!";
                            }
                        }
                    }

                } else {
                    if (!StringUtils.isBlank(item.JLZD)) {//删除
                        isSucess = service.deleteNursePlanDiagnosticBasis(item.JLZD) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理计划目标数据失败]数据库执行错误!";
                        }
                    }
                }
            }
        }
        if (problem.XGYSList != null) {//相关因素
            for (RelevantFactor item : problem.XGYSList) {
                if (item.SELECTED) {
                    if (item.JLYS.contains("add:")) {//插入
                        item.JLYS = item.JLYS.replace("add:", "");
                        isSucess = service.addNursePlanRelevantFactor(item.JLYS, problem.JLWT, item.YSXH, item.YSMS) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理计划相关因素数据失败]数据库执行错误!";
                        }
                    } else {
                        if (item.MODIFIED) {//修改
                            isSucess = service.editNursePlanRelevantFactor(item.JLYS, item.YSMS) > 0;
                            if (!isSucess) {
                                response.isSuccess = false;
                                response.message = "[保存护理计划相关因素数据失败]数据库执行错误!";
                            }
                        }
                    }

                } else {
                    if (!StringUtils.isBlank(item.JLYS)) {//删除
                        isSucess = service.deleteNursePlanRelevantFactor(item.JLYS) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理计划相关因素失败]数据库执行错误!";
                        }
                    }
                }
            }
        }
        if (isSucess) {
            List<Problem> problemList = getExistPlanProblem(problem.WTXH, problem.GLXH, problemSaveData.ZYH, problemSaveData.JGID);
            for (Problem newProblem : problemList) {
                if (problem.JLWT.equals(newProblem.JLWT)) {
                    Response<SelectResult> syncResponse = buildProblemSyncData(problemSaveData, "1", "1");
                    if (syncResponse.ReType == 2) {
                        newProblem.IsSync = true;
                        newProblem.SyncData = syncResponse.Data;
                    }
                }
            }
            response.datalist = problemList;
            response.isSuccess = true;
            response.message = "保存护理计划问题数据成功!";
        }
        return response;
    }

    /**
     * 修改问题、目标、措施、诊断记录
     * <p>
     * 注意：调用此方法之前需要显式指定事务
     *
     * @param problemSaveData 护理焦点保存数据对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private BizResponse<Problem> realEditNurseFocusData(ProblemSaveData problemSaveData)
            throws SQLException, DataAccessException {
        BizResponse<Problem> response = new BizResponse<>();
        Problem problem = problemSaveData.Problem;
        String now = DateUtil.getApplicationDateTime();
        String kssj = StringUtils.isBlank(problem.KSSJ) ? now : problem.KSSJ;
        boolean isSucess;
        isSucess = service.editNurseFocusProblem(problem.JLWT, problem.WTMS, kssj) > 0;
        if (!isSucess) {
            response.isSuccess = false;
            response.message = "[保存护理焦点问题数据失败]数据库执行错误!";
        }
        if (problem.JHCS != null) {//措施
            for (Measure measure : problem.JHCS) {
                if (measure.SELECTED) {
                    if (measure.JLCS.contains("add:")) {//新增
                        measure.JLCS = measure.JLCS.replace("add:", "");
                        measure.CSZH = measure.CSZH.replace("add:", "");
                        isSucess = service.addNurseFocusMeasure(measure.JLCS, problem.JLWT, measure.CSMS, measure.CSXH, measure.ZDYBZ, measure.XJBZ,
                                measure.KSSJ, measure.KSGH, measure.JSSJ, measure.JSGH, measure.CSZH) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理焦点措施数据失败]数据库执行错误!";
                        }
                    } else {//修改
                        if (measure.MODIFIED) {
                            isSucess = service.editNurseFocusMeasure(measure.JLCS, measure.CSMS, measure.XJBZ, measure.KSSJ, measure.JSSJ) > 0;
                            if (!isSucess) {
                                response.isSuccess = false;
                                response.message = "[保存护理焦点措施数据失败]数据库执行错误!";
                            }
                        }
                    }
                } else {
                    if (!StringUtils.isBlank(measure.JLCS)) {//删除
                        isSucess = service.deleteNurseFocusMeasure(measure.JLCS) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理焦点措施数据失败]数据库执行错误!";
                        }
                    }
                }
            }
        }
        if (problem.ZDYJ != null) {//诊断依据
            for (DiagnosticBasis item : problem.ZDYJ) {
                if (item.SELECTED) {
                    if (item.JLZD.contains("add:")) {//插入
                        item.JLZD = item.JLZD.replace("add:", "");
                        isSucess = service.addNurseFocusDiagnosticBasis(item.JLZD, problem.JLWT, item.ZDXH, item.ZDMS, item.ZDYBZ) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理焦点诊断依据数据失败]数据库执行错误!";
                        }
                    } else {
                        if (item.MODIFIED) {//修改
                            isSucess = service.editNurseFocusDiagnosticBasis(item.JLZD, item.ZDMS) > 0;
                            if (!isSucess) {
                                response.isSuccess = false;
                                response.message = "[保存护理焦点诊断依据数据失败]数据库执行错误!";
                            }
                        }
                    }

                } else {
                    if (!StringUtils.isBlank(item.JLZD)) {//删除
                        isSucess = service.deleteNurseFocusDiagnosticBasis(item.JLZD) > 0;
                        if (!isSucess) {
                            response.isSuccess = false;
                            response.message = "[保存护理焦点目标数据失败]数据库执行错误!";
                        }
                    }
                }
            }
        }
        if (isSucess) {
            List<Problem> problemList = getExistFocusProblem(problem.WTXH, problem.GLXH, problemSaveData.ZYH, problemSaveData.JGID);
            for (Problem newProblem : problemList) {
                if (problem.JLWT.equals(newProblem.JLWT)) {
                    Response<SelectResult> syncResponse = buildProblemSyncData(problemSaveData, "1", "1");
                    if (syncResponse.ReType == 2) {
                        newProblem.IsSync = true;
                        newProblem.SyncData = syncResponse.Data;
                    }
                }
            }
            response.datalist = problemList;
            response.isSuccess = true;
            response.message = "保存护理焦点问题数据成功!";
        }
        return response;
    }

    /**
     * 保存问题评价-计划
     * <p>
     * 注意：调用此方法之前必须显式指定事务
     *
     * @param problemEvaluateSaveData
     * @param pjzh
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private BizResponse<EvaluateAndRecord> realSavePlanProblemEvaluate(ProblemEvaluateSaveData problemEvaluateSaveData, String pjzh)
            throws SQLException, DataAccessException {
        BizResponse<EvaluateAndRecord> response = new BizResponse<>();
        List<Evaluate> evaluateList = problemEvaluateSaveData.evaluateList;
        String pjsj = evaluateList.size() > 0 ? evaluateList.get(0).PJSJ : DateUtil.getApplicationDateTime();
        boolean isSucess;
        for (Evaluate item : evaluateList) {
            if (item.selected) {
                isSucess = service.addPlanProblemEvaluate(item.JLPJ, problemEvaluateSaveData.JLWT,
                        item.PJXH, item.PJMS, pjsj, problemEvaluateSaveData.YHID, pjzh) > 0;
                if (!isSucess) {
                    response.isSuccess = false;
                    response.message = "保存问题评价数据失败!";
                    return response;
                }
            }
        }
        isSucess = service.editPlanProblemEvaluateStatus(problemEvaluateSaveData.JLWT) > 0;
        if (!isSucess) {
            response.isSuccess = false;
            response.message = "保存问题评价数据失败!";
            return response;
        }
        EvaluateAndRecord evaluateAndRecord = getRealPlanEvaluateList(problemEvaluateSaveData.JLWT, problemEvaluateSaveData.WTXH, problemEvaluateSaveData.JGID);
        Response<SelectResult> syncResponse = buildEvaluateSyncData(problemEvaluateSaveData, pjzh, "0", "2", "4");
        if (syncResponse.ReType == 2) {
            evaluateAndRecord.IsSync = true;
            evaluateAndRecord.SyncData = syncResponse.Data;
        }
        response.data = evaluateAndRecord;
        response.isSuccess = true;
        response.message = "保存问题评价数据成功!";
        return response;
    }

    /**
     * 保存问题评价-焦点
     * <p>
     * 注意：调用此方法之前必须显式指定事务
     *
     * @param problemEvaluateSaveData
     * @param pjzh
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private BizResponse<EvaluateAndRecord> realSaveFocusProblemEvaluate(ProblemEvaluateSaveData problemEvaluateSaveData, String pjzh)
            throws SQLException, DataAccessException {
        BizResponse<EvaluateAndRecord> response = new BizResponse<>();
        List<Evaluate> evaluateList = problemEvaluateSaveData.evaluateList;
        String pjsj = evaluateList.size() > 0 ? evaluateList.get(0).PJSJ : DateUtil.getApplicationDateTime();
        boolean isSucess;
        for (Evaluate item : evaluateList) {
            if (item.selected) {
                isSucess = service.addFocusProblemEvaluate(item.JLPJ, problemEvaluateSaveData.JLWT,
                        item.PJXH, item.PJMS, pjsj, problemEvaluateSaveData.YHID, pjzh) > 0;
                if (!isSucess) {
                    response.isSuccess = false;
                    response.message = "保存焦点评价数据失败!";
                    return response;
                }
            }
        }
        isSucess = service.editFocusProblemEvaluateStatus(problemEvaluateSaveData.JLWT) > 0;
        if (!isSucess) {
            response.isSuccess = false;
            response.message = "保存焦点评价数据失败!";
            return response;
        }
        EvaluateAndRecord evaluateAndRecord = getRealFocusEvaluateList(problemEvaluateSaveData.JLWT, problemEvaluateSaveData.WTXH, problemEvaluateSaveData.JGID);
        Response<SelectResult> syncResponse = buildEvaluateSyncData(problemEvaluateSaveData, pjzh, "0", "2", "8");
        if (syncResponse.ReType == 2) {
            evaluateAndRecord.IsSync = true;
            evaluateAndRecord.SyncData = syncResponse.Data;
        }
        response.data = evaluateAndRecord;
        response.isSuccess = true;
        response.message = "保存焦点评价数据成功!";
        return response;
    }

    /**
     * 护理计划问题的同步
     *
     * @param data
     * @param czbz
     * @param lymxlx
     * @return
     */
    private Response<SelectResult> buildProblemSyncData(ProblemSaveData data,
                                                        String czbz, String lymxlx) {
        Problem problem = data.Problem;
        InArgument inArgument = new InArgument();
        inArgument.lymxlx = lymxlx;
        inArgument.lymx = problem.JLWT;
        inArgument.zyh = data.ZYH;
        inArgument.bqdm = data.BQID;
        inArgument.hsgh = data.YHID;
        inArgument.bdlx = problem.WTLX.equals("1") ? "4" : "8"; // WTLX=1 计划  2 焦点
        inArgument.lybd = "0";
        inArgument.flag = czbz;
        inArgument.jlxh = problem.JLWT;
        String jlsj = problem.KSSJ;
        if (jlsj == null || "".equals(jlsj)) {
            jlsj = timeService.now(DataSource.MOB);
        }
        inArgument.jlsj = jlsj;
        inArgument.jgid = data.JGID;

        Project projectWT = new Project("1", problem.JLWT);
        Project newProjectWT = new Project(problem.WTXH, problem.WTMS);
        projectWT.saveProjects.add(newProjectWT);
        inArgument.projects.add(projectWT);

        Project projectMB = new Project();
        projectMB.key = "2";
        if (problem.JHMB != null) {
            for (Goal goal : problem.JHMB) {
                if (goal.SELECTED) {
                    Project newProjectMB = new Project(goal.MBXH, goal.MBMS);
                    projectMB.saveProjects.add(newProjectMB);
                }
            }
        }
        inArgument.projects.add(projectMB);

        Project projectCS = new Project();
        projectCS.key = "3";
        for (Measure measure : problem.JHCS) {
            if (measure.SELECTED) {
                Project newProjectCS = new Project(measure.CSXH, measure.CSMS);
                projectCS.saveProjects.add(newProjectCS);
            }
        }
        inArgument.projects.add(projectCS);

        Project projectPJ = new Project();
        projectPJ.key = "4";
       /* for (RelevantFactor factor : problem.XGYSList) {
            if (factor.SELECTED) {
                Project newProjectPJ = new Project(factor.JLYS, factor.YSMS);
                projectPJ.saveProjects.add(newProjectPJ);
            }
        }*/
        inArgument.projects.add(projectPJ);

        Project projectYJ = new Project();
        projectYJ.key = "5";
        for (DiagnosticBasis basis : problem.ZDYJ) {
            if (basis.SELECTED) {
                Project newprojectYJ = new Project(basis.JLZD, basis.ZDMS);
                projectYJ.saveProjects.add(newprojectYJ);
            }
        }
        inArgument.projects.add(projectYJ);

        Response<SelectResult> response = new Response<>();
        try {
            response = Client
                    .rpcInvoke("nis-synchron.synchronRpcServerProvider", "synchron", inArgument);
        } catch (Throwable throwable) {
            response.ReType = 0;
            response.Msg = "同步目标失败";
            logger.error(throwable.getMessage(), throwable);
        }
        return response;
    }

    /**
     * 问题评价的同步
     *
     * @param data
     * @param pjzh
     * @param czbz
     * @param lymxlx
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    private Response<SelectResult> buildEvaluateSyncData(ProblemEvaluateSaveData data,
                                                         String pjzh, String czbz, String lymxlx, String bdlx)
            throws SQLException, DataAccessException {
        InArgument inArgument = new InArgument();
        Response<SelectResult> response = new Response<>();
        // 获取问题记录
        List<Plan> planList = bdlx.equals("4") ? service.getPlanListForSync(data.JLWT) : service.getFocusListForSync(data.JLWT);
        if (planList.size() != 1) {
            response.ReType = 0;
            return response;
        }

        inArgument.lymxlx = lymxlx;
        inArgument.lymx = pjzh;
        inArgument.zyh = data.ZYH;
        inArgument.bqdm = data.BQID;
        inArgument.hsgh = data.YHID;
        inArgument.bdlx = bdlx;
        inArgument.lybd = "0";
        inArgument.flag = czbz;
        inArgument.jlxh = data.JLWT;
        inArgument.jlsj = data.evaluateList.get(0).PJSJ;
        inArgument.jgid = data.JGID;

        Project projectWT = new Project("1", pjzh);
        Project newProjectWT = new Project(planList.get(0).XH, planList.get(0).MS);
        projectWT.saveProjects.add(newProjectWT);
        inArgument.projects.add(projectWT);

        Project projectPJ = new Project();
        projectPJ.key = "4";
        for (Evaluate evaluate : data.evaluateList) {
	        if (evaluate.selected) {
		        Project newProjectPJ = new Project(evaluate.PJXH, evaluate.PJMS);
		        projectPJ.saveProjects.add(newProjectPJ);
	        }
        }
        inArgument.projects.add(projectPJ);

	    Project projectMB = new Project();
	    projectMB.key = "2";
	    inArgument.projects.add(projectMB);
	    Project projectCS = new Project();
	    projectCS.key = "3";
	    inArgument.projects.add(projectCS);
	    Project projectYJ = new Project();
	    projectYJ.key = "5";
	    inArgument.projects.add(projectYJ);

        try {
            response = Client
                    .rpcInvoke("nis-synchron.synchronRpcServerProvider", "synchron", inArgument);
        } catch (Throwable throwable) {
            response.ReType = 0;
            response.Msg = "同步目标失败";
            logger.error(throwable.getMessage(), throwable);
        }
        return response;
    }

    /**
     * 删除操作的同步
     *
     * @param jlxh
     * @param lymx
     * @param lymxlx
     * @param bdlx   4 计划 8 焦点
     * @param jgid
     */
    private void deleSyncData(String jlxh, String lymx, String lymxlx, String bdlx, String jgid) {
        InArgument inArgument = new InArgument();
        inArgument.bdlx = bdlx;
        inArgument.jlxh = jlxh;
        inArgument.lymx = lymx;
        inArgument.lymxlx = lymxlx;
        inArgument.jgid = jgid;
        inArgument.flag = "2";

        Response<String> response = new Response<>();
        try {
            response = Client.rpcInvoke("nis-synchron.synchronRpcServerProvider", "DeleSyncData", inArgument);
        } catch (Throwable throwable) {
            response.ReType = 0;
            response.Msg = "同步目标失败";
            logger.error(throwable.getMessage(), throwable);
        }
    }
}
