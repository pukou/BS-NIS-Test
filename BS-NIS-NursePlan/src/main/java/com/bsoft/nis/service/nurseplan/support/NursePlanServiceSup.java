package com.bsoft.nis.service.nurseplan.support;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.dangerevaluate.FXPGJLBean;
import com.bsoft.nis.domain.dangerevaluate.HLJHJLBean;
import com.bsoft.nis.domain.nurseplan.*;
import com.bsoft.nis.mapper.nurseplan.NursePlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Description: 护理计划
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Service
public class NursePlanServiceSup extends RouteDataSourceService {

    @Autowired
    NursePlanMapper mapper;

    String dbType;

    /**
     * 获取病区定制计划列表
     *
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Plan> getPlanList(String bqid, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getPlanList(bqid, jgid);
    }

    /**
     * 获取病区定制焦点列表
     *
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Plan> getFocusList(String bqid, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getFocusList(bqid, jgid);
    }

    /**
     *
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<FocusRelevanceBean> getFocusRelevanceList()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getFocusRelevanceList();
    }

    public List<JYXM_PATIENTINFO_Bean> getJYXM_PATIENTINFO_List(String zyhm,String xmid,String jgts,String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        return mapper.getJYXM_PATIENTINFO_List(zyhm,xmid,jgts,jgid);
    }

    public List<JD_GL_JYXM_Bean> getJD_GL_JYXM_List(String bqdm,String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getJD_GL_JYXM_List(bqdm,jgid);
    }
    /**
     *
     * @param zyh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HL_JD_JL_BEAN> getHLJDJL(String zyh,String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHLJDJL(zyh,jgid);
    }
    /**
     *
     * @param bqdm
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<JD_GL_TZXM_Bean> getJD_GL_TZXM_List(String bqdm,String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getJD_GL_TZXM_List(bqdm,jgid);
    }

    public List<JD_GL_SMTZ_Bean> getJD_GL_SMTZ_List(String zyh,String jgid,String xmh,String dxqzxx,String dxqzsx)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getJD_GL_SMTZ_List(zyh,jgid,xmh,dxqzxx,dxqzsx);
    }

    public List<FXPGJLBean> getFXPGJLList(String zyh,String bqdm, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getFXPGJLList(zyh,bqdm,jgid);
    }

    public List<PGLX_JD_GL_FXPG_Bean> getJD_GL_List_BY_PGLX(String pglx,String bqdm, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getJD_GL_List_BY_PGLX(pglx,bqdm,jgid);
    }

    public List<HLJHJLBean> getHLJHJLList(String zyh, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getHLJHJLList(zyh,jgid);
    }

    public List<ZDMS_Bean> getZDMS_List(String jlwt)
            throws SQLException, DataAccessException {
        return mapper.getZDMS_List(jlwt);
    }

    public List<CSMS_Bean> getCSMS_List(String jlwt)
            throws SQLException, DataAccessException {
        return mapper.getCSMS_List(jlwt);
    }

    /**
     * 获取指定问题序号的计划问题缩略记录-计划
     *
     * @param zyh  住院号
     * @param wtxh 问题序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<SimpleRecord> getPlanSimpleRecordListForWtxh(String zyh, String wtxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getPlanSimpleRecordListForWtxh(zyh, wtxh);
    }

    /**
     * 获取指定问题序号的计划问题缩略记录-焦点
     *
     * @param zyh  住院号
     * @param wtxh 问题序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<SimpleRecord> getFocusSimpleRecordListForWtxh(String zyh, String wtxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getFocusSimpleRecordListForWtxh(zyh, wtxh);
    }

    /**
     * 获取指定归类序号的计划问题缩略记录-计划
     *
     * @param zyh  住院号
     * @param glxh 归类序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<SimpleRecord> getPlanSimpleRecordListForGlxh(String zyh, String glxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getPlanSimpleRecordListForGlxh(zyh, glxh);
    }

    /**
     * 获取指定归类序号的计划问题缩略记录-焦点
     *
     * @param zyh  住院号
     * @param glxh 归类序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<SimpleRecord> getFocusSimpleRecordListForGlxh(String zyh, String glxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getFocusSimpleRecordListForGlxh(zyh, glxh);
    }

    /**
     * 获取新增护理问题
     *
     * @param wtxh 问题序号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public Problem getProblem(String wtxh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getProblem(wtxh, jgid);
    }

    /**
     * 根据问题序号获取目标列表
     *
     * @param wtxh 问题序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Goal> getGoalList(String wtxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getGoalList(wtxh);
    }

    /**
     * 根据问题序号获取措施列表
     *
     * @param wtxh 问题序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Measure> getMeasureList(String wtxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getMeasureList(wtxh);
    }

    /**
     * 根据问题序号获取诊断列表
     *
     * @param wtxh 问题序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<DiagnosticBasis> getDiagnosticBasisList(String wtxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getDiagnosticBasisList(wtxh);
    }

    /**
     * 根据问题序号获取相关因素
     *
     * @param wtxh 问题序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<RelevantFactor> getRelevantFactorList(String wtxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getRelevantFactorList(wtxh);
    }

    /**
     * 新增记录问题
     *
     * @param jlwt   记录问题
     * @param wtxh   问题序号
     * @param xgys   相关因素
     * @param cjsj   采集时间
     * @param cjgh   采集工号
     * @param kssj   开始时间
     * @param ksgh   开始工号
     * @param glxh   归类序号
     * @param gllx   归类类型
     * @param wtlx   问题类型
     * @param jhwtxh 计划问题序号
     * @param wtms   问题描述
     * @param zyh    住院号
     * @param bqid   病区id
     * @param jgid   机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addNursePlanProblem(String jlwt, String wtxh, String xgys, String cjsj, String cjgh, String kssj,
                                   String ksgh, String glxh, String gllx, String wtlx, String jhwtxh, String wtms,
                                   String zyh, String bqid, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.addNursePlanProblem(jlwt, wtxh, xgys, cjsj, cjgh, kssj,
                ksgh, glxh, gllx, wtlx, jhwtxh, wtms, zyh, bqid, jgid, dbType);
    }

    /**
     * 新增记录问题-焦点
     *
     * @param jljd   记录焦点
     * @param jdxh   焦点序号
     * @param cjsj   采集时间
     * @param cjgh   采集工号
     * @param kssj   开始时间
     * @param ksgh   开始工号
     * @param glxh   归类序号
     * @param gllx   归类类型
     * @param wtlx   问题类型
     * @param jhwtxh 计划问题序号
     * @param wtms   问题描述
     * @param zyh    住院号
     * @param bqid   病区id
     * @param jgid   机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addNurseFocusProblem(String jljd, String jdxh, String cjsj, String cjgh, String kssj,
                                    String ksgh, String glxh, String gllx, String wtlx, String jhwtxh, String wtms,
                                    String zyh, String bqid, String jgid, String jlgllx, String gllj)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.addNurseFocusProblem(jljd, jdxh, cjsj, cjgh, kssj,
                ksgh, glxh, gllx, wtlx, jhwtxh, wtms, zyh, bqid, jgid,jlgllx,gllj, dbType);
    }

    /**
     * 修改记录问题-计划
     *
     * @param jlwt 记录问题
     * @param wtms 问题描述
     * @param xgys 相关因素
     * @param kssj 开始时间
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editNursePlanProblem(String jlwt, String wtms, String xgys, String kssj)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.editNursePlanProblem(jlwt, wtms, xgys, kssj, dbType);
    }

    /**
     * 修改记录问题-焦点
     *
     * @param jljd 记录焦点
     * @param wtms 问题描述
     * @param kssj 开始时间
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editNurseFocusProblem(String jljd, String wtms, String kssj)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.editNurseFocusProblem(jljd, wtms, kssj, dbType);
    }

    /**
     * 删除记录问题-计划
     *
     * @param jlwt 记录问题
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteNursePlanProblem(String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteNursePlanProblem(jlwt);
    }

    /**
     * 删除记录问题-焦点
     *
     * @param jljd 记录焦点
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteNurseFocusProblem(String jljd)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteNurseFocusProblem(jljd);
    }

    /**
     * 结束记录问题-计划
     *
     * @param jlwt 记录问题
     * @param tzsj 停止时间
     * @param tzgh 停止工号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int terminateNursePlanProblem(String jlwt, String tzsj, String tzgh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.terminateNursePlanProblem(jlwt, tzsj, tzgh, dbType);
    }

    /**
     * 结束记录问题-焦点
     *
     * @param jljd 记录焦点
     * @param tzsj 停止时间
     * @param tzgh 停止工号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int terminateNurseFocusProblem(String jljd, String tzsj, String tzgh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.terminateNurseFocusProblem(jljd, tzsj, tzgh, dbType);
    }

    /**
     * 新增记录措施-计划
     *
     * @param jlcs  记录措施
     * @param jlwt  记录问题
     * @param csms  措施描述
     * @param csxh  措施序号
     * @param zdybz 自定义标志
     * @param xjbz  宣教标志
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addNursePlanMeasure(String jlcs, String jlwt, String csms, String csxh, String zdybz, String xjbz,
                                   String kssj, String ksgh, String jssj, String jsgh, String cszh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.addNursePlanMeasure(jlcs, jlwt, csms, csxh, zdybz, xjbz, kssj, ksgh, jssj, jsgh, cszh, dbType);
    }

    /**
     * 修改记录措施-计划
     *
     * @param jlcs 记录措施
     * @param csms 措施描述
     * @param xjbz 宣教标志
     * @param kssj 开始时间
     * @param jssj 结束时间
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editNursePlanMeasure(String jlcs, String csms, String xjbz, String kssj, String jssj)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.editNursePlanMeasure(jlcs, csms, xjbz, kssj, jssj, dbType);
    }

    /**
     * 删除记录措施-计划
     *
     * @param jlcs 记录措施
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteNursePlanMeasure(String jlcs)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteNursePlanMeasure(jlcs);
    }

    /**
     * 新增记录措施-焦点
     *
     * @param jlcs  记录措施
     * @param jljd  记录焦点
     * @param csms  措施描述
     * @param csxh  措施序号
     * @param zdybz 自定义标志
     * @param xjbz  宣教标志
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addNurseFocusMeasure(String jlcs, String jljd, String csms, String csxh, String zdybz, String xjbz,
                                    String kssj, String ksgh, String jssj, String jsgh, String cszh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.addNurseFocusMeasure(jlcs, jljd, csms, csxh, zdybz, xjbz, kssj, ksgh, jssj, jsgh, cszh, dbType);
    }

    /**
     * 修改记录措施-焦点
     *
     * @param jlcs 记录措施
     * @param csms 措施描述
     * @param xjbz 宣教标志
     * @param kssj 开始时间
     * @param jssj 结束时间
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editNurseFocusMeasure(String jlcs, String csms, String xjbz, String kssj, String jssj)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.editNurseFocusMeasure(jlcs, csms, xjbz, kssj, jssj, dbType);
    }

    /**
     * 删除记录措施-焦点
     *
     * @param jlcs 记录措施
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteNurseFocusMeasure(String jlcs)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteNurseFocusMeasure(jlcs);
    }

    /**
     * 新增记录目标
     *
     * @param jlmb  记录目标
     * @param jlwt  记录问题
     * @param mbxh  目标序号
     * @param mbms  目标描述
     * @param zdymb 自定义目标
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addNursePlanGoal(String jlmb, String jlwt, String mbxh, String mbms, String zdymb)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.addNursePlanGoal(jlmb, jlwt, mbxh, mbms, zdymb);
    }

    /**
     * 修改记录目标
     *
     * @param jlmb 记录目标
     * @param mbms 目标描述
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editNursePlanGoal(String jlmb, String mbms)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.editNursePlanGoal(jlmb, mbms);
    }

    /**
     * 删除记录目标
     *
     * @param jlmb 记录目标
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteNursePlanGoal(String jlmb)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteNursePlanGoal(jlmb);
    }

    /**
     * 新增记录诊断依据
     *
     * @param jlzd  记录诊断
     * @param jlwt  记录问题
     * @param zdxh  诊断序号
     * @param zdms  诊断描述
     * @param zdybz 自定义标志
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addNursePlanDiagnosticBasis(String jlzd, String jlwt, String zdxh, String zdms, String zdybz)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.addNursePlanDiagnosticBasis(jlzd, jlwt, zdxh, zdms, zdybz);
    }

    /**
     * 修改记录诊断依据
     *
     * @param jlzd 记录诊断
     * @param zdms 诊断描述
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editNursePlanDiagnosticBasis(String jlzd, String zdms)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.editNursePlanDiagnosticBasis(jlzd, zdms);
    }

    /**
     * 删除记录诊断依据
     *
     * @param jlzd 记录诊断
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteNursePlanDiagnosticBasis(String jlzd)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteNursePlanDiagnosticBasis(jlzd);
    }

    /**
     * 新增记录诊断依据
     *
     * @param jlzd  记录诊断
     * @param jljd  记录焦点
     * @param zdxh  诊断序号
     * @param zdms  诊断描述
     * @param zdybz 自定义标志
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addNurseFocusDiagnosticBasis(String jlzd, String jljd, String zdxh, String zdms, String zdybz)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.addNurseFocusDiagnosticBasis(jlzd, jljd, zdxh, zdms, zdybz);
    }

    /**
     * 修改记录诊断依据
     *
     * @param jlzd 记录诊断
     * @param zdms 诊断描述
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editNurseFocusDiagnosticBasis(String jlzd, String zdms)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.editNurseFocusDiagnosticBasis(jlzd, zdms);
    }

    /**
     * 删除记录诊断依据
     *
     * @param jlzd 记录诊断
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteNurseFocusDiagnosticBasis(String jlzd)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteNurseFocusDiagnosticBasis(jlzd);
    }

    /**
     * 新增记录-记录因素
     *
     * @param jlys 记录因素
     * @param jlwt 记录问题
     * @param ysxh 因素序号
     * @param ysms 因素描述
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addNursePlanRelevantFactor(String jlys, String jlwt, String ysxh, String ysms)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.addNursePlanRelevantFactor(jlys, jlwt, ysxh, ysms);
    }

    /**
     * 修改记录-记录因素
     *
     * @param jlys 记录因素
     * @param ysms 因素描述
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editNursePlanRelevantFactor(String jlys, String ysms)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.editNursePlanRelevantFactor(jlys, ysms);
    }

    /**
     * 删除记录-相关因素
     *
     * @param jlys 记录因素
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteNursePlanRelevantFactor(String jlys)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteNursePlanRelevantFactor(jlys);
    }

    /**
     * 获取护理问题列表 - 计划
     *
     * @param wtxh 问题序号
     * @param glxh 归类序号
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Problem> getPlanProblemHistoryList(String wtxh, String glxh, String zyh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getPlanProblemHistoryList(wtxh, glxh, zyh, jgid);
    }

    /**
     * 获取护理问题列表 - 焦点
     *
     * @param wtxh 问题序号
     * @param glxh 归类序号
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Problem> getFocusProblemHistoryList(String wtxh, String glxh, String zyh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getFocusProblemHistoryList(wtxh, glxh, zyh, jgid);
    }

    /**
     * 获取护理问题-目标列表
     *
     * @param wtxh 问题序号
     * @param jlwt 记录问题
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Goal> getGoalHistoryList(String wtxh, String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getGoalHistoryList(wtxh, jlwt);
    }

    /**
     * 获取护理问题-措施列表-计划
     *
     * @param jlwt 记录问题
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Measure> getPlanMeasureHistoryList(String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getPlanMeasureHistoryList(jlwt);
    }

    /**
     * 获取护理问题-措施列表-焦点
     *
     * @param jlwt 记录问题
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Measure> getFocusMeasureHistoryList(String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getFocusMeasureHistoryList(jlwt);
    }

    /**
     * 获取护理问题-诊断依据列表-计划
     *
     * @param wtxh 问题序号
     * @param jlwt 记录问题
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<DiagnosticBasis> getPlanDiagnosticBasisHistoryList(String wtxh, String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getPlanDiagnosticBasisHistoryList(wtxh, jlwt);
    }

    /**
     * 获取护理问题-诊断依据列表-焦点
     *
     * @param wtxh 问题序号
     * @param jlwt 记录问题
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<DiagnosticBasis> getFocusDiagnosticBasisHistoryList(String wtxh, String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getFocusDiagnosticBasisHistoryList(wtxh, jlwt);
    }

    /**
     * 获取护理问题-相关因素列表
     *
     * @param wtxh 问题序号
     * @param jlwt 记录问题
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<RelevantFactor> getRelevantFactorHistoryList(String wtxh, String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getRelevantFactorHistoryList(wtxh, jlwt);
    }

    /**
     * 获取评价记录-计划
     *
     * @param jlwt 记录问题
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Evaluate> getPlanEvaluateHistoryList(String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getPlanEvaluateHistoryList(jlwt);
    }

    /**
     * 获取评价记录-焦点
     *
     * @param jlwt 记录问题
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Evaluate> getFocusEvaluateHistoryList(String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getFocusEvaluateHistoryList(jlwt);
    }

    /**
     * 获取评价项目-评价模板数据
     *
     * @param wtxh 问题序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Evaluate> getEvaluateTemplateList(String wtxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getEvaluateTemplateList(wtxh);
    }

    /**
     * 新增问题评价记录-计划
     *
     * @param jlpj 记录评价
     * @param jlwt 记录问题
     * @param pjxh 评价序号
     * @param pjms 评价描述
     * @param pjsj 评价时间
     * @param pjgh 评价工号
     * @param pjzh 评价组号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addPlanProblemEvaluate(String jlpj, String jlwt, String pjxh, String pjms, String pjsj, String pjgh, String pjzh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.addPlanProblemEvaluate(jlpj, jlwt, pjxh, pjms, pjsj, pjgh, pjzh, dbType);
    }

    /**
     * 新增问题评价记录-焦点
     *
     * @param jlpj 记录评价
     * @param jljd 记录焦点
     * @param pjxh 评价序号
     * @param pjms 评价描述
     * @param pjsj 评价时间
     * @param pjgh 评价工号
     * @param pjzh 评价组号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addFocusProblemEvaluate(String jlpj, String jljd, String pjxh, String pjms, String pjsj, String pjgh, String pjzh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.addFocusProblemEvaluate(jlpj, jljd, pjxh, pjms, pjsj, pjgh, pjzh, dbType);
    }

    /**
     * 更新评价状态-计划
     *
     * @param jlwt 记录问题
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editPlanProblemEvaluateStatus(String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.editPlanProblemEvaluateStatus(jlwt);
    }

    /**
     * 更新评价状态-焦点
     *
     * @param jlwt 记录问题
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editFocusProblemEvaluateStatus(String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.editFocusProblemEvaluateStatus(jlwt);
    }

    /**
     * 删除问题评价-计划
     *
     * @param jlpj 记录评价
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deletePlanProblemEvaluate(String jlpj)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deletePlanProblemEvaluate(jlpj);
    }

    /**
     * 删除问题评价-焦点
     *
     * @param jlpj 记录评价
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteFocusProblemEvaluate(String jlpj)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteFocusProblemEvaluate(jlpj);
    }

    /**
     * 保存问题评价的同步时，获取问题记录
     *
     * @param jlwt
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Plan> getPlanListForSync(String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getPlanListForSync(jlwt);
    }

    /**
     * 保存问题评价的同步时，获取焦点记录
     *
     * @param jlwt
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Plan> getFocusListForSync(String jlwt)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getFocusListForSync(jlwt);
    }

	/**
	 * 根据jlpj获取pjzh
	 * @param jlpj
	 * @param bdlx 4 护理计划  8 护理焦点
	 * @return
	 */
	public String getEvaluatePjzhByJlpj(String jlpj, String bdlx)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		if ("4".equals(bdlx)) {
			return mapper.getPlanEvaluatePjzhByJlpj(jlpj);
		} else {
			return mapper.getFocusEvaluatePjzhByJlpj(jlpj);
		}
	}
}
