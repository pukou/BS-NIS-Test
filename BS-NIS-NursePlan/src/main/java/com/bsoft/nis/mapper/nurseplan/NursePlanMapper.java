package com.bsoft.nis.mapper.nurseplan;

import com.bsoft.nis.domain.dangerevaluate.FXPGJLBean;
import com.bsoft.nis.domain.dangerevaluate.HLJHJLBean;
import com.bsoft.nis.domain.nurseplan.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 护理计划
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
public interface NursePlanMapper {

    List<Plan> getPlanList(@Param(value = "BQID") String bqid, @Param(value = "JGID") String jgid);

    List<Plan> getFocusList(@Param(value = "BQID") String bqid, @Param(value = "JGID") String jgid);

    List<FocusRelevanceBean> getFocusRelevanceList();

    List<JYXM_PATIENTINFO_Bean> getJYXM_PATIENTINFO_List(@Param(value = "BRID") String brid,@Param(value = "JGTS") String jgts,
                                                         @Param(value = "XMID") String xmid,@Param(value = "JGID") String jgid);

    List<JD_GL_JYXM_Bean> getJD_GL_JYXM_List(@Param(value = "BQDM") String bqdm,@Param(value = "JGID") String jgid);


    List<HL_JD_JL_BEAN> getHLJDJL(@Param(value = "ZYH") String zyh,@Param(value = "JGID") String jgid);


    List<JD_GL_TZXM_Bean> getJD_GL_TZXM_List(@Param(value = "BQDM") String bqdm,@Param(value = "JGID") String jgid);

    List<JD_GL_SMTZ_Bean> getJD_GL_SMTZ_List(@Param(value = "ZYH") String zyh,@Param(value = "JGID") String jgid,
                                             @Param(value = "XMH") String xmh,@Param(value = "DXQZXX") String dxqzxx,
                                             @Param(value = "DXQZSX") String dxqzsx);

    List<PGLX_JD_GL_FXPG_Bean> getJD_GL_List_BY_PGLX(@Param("PGLX") String pglx, @Param(value = "BQDM") String bqdm,@Param("JGID") String jgid);

    List<FXPGJLBean> getFXPGJLList(@Param("ZYH") String ZYH, @Param(value = "BQDM") String bqdm,@Param("JGID") String jgid);

    List<HLJHJLBean> getHLJHJLList(@Param("ZYH") String ZYH, @Param("JGID") String jgid);

    List<ZDMS_Bean> getZDMS_List(@Param("JLWT") String JLWT);

    List<CSMS_Bean> getCSMS_List(@Param("JLWT") String JLWT);

    List<SimpleRecord> getPlanSimpleRecordListForWtxh(@Param(value = "ZYH") String zyh, @Param(value = "XH") String xh);

    List<SimpleRecord> getFocusSimpleRecordListForWtxh(@Param(value = "ZYH") String zyh, @Param(value = "XH") String xh);

    List<SimpleRecord> getPlanSimpleRecordListForGlxh(@Param(value = "ZYH") String zyh, @Param(value = "XH") String xh);

    List<SimpleRecord> getFocusSimpleRecordListForGlxh(@Param(value = "ZYH") String zyh, @Param(value = "XH") String xh);

    Problem getProblem(@Param(value = "WTXH") String wtxh, @Param(value = "JGID") String jgid);

    List<Goal> getGoalList(@Param(value = "WTXH") String wtxh);

    List<Measure> getMeasureList(@Param(value = "WTXH") String wtxh);

    List<DiagnosticBasis> getDiagnosticBasisList(@Param(value = "WTXH") String wtxh);

    List<RelevantFactor> getRelevantFactorList(@Param(value = "WTXH") String wtxh);

    int addNursePlanProblem(@Param(value = "JLWT") String jlwt, @Param(value = "WTXH") String wtxh, @Param(value = "XGYS") String xgys,
                            @Param(value = "CJSJ") String cjsj, @Param(value = "CJGH") String cjgh, @Param(value = "KSSJ") String kssj,
                            @Param(value = "KSGH") String ksgh, @Param(value = "GLXH") String glxh, @Param(value = "GLLX") String gllx,
                            @Param(value = "WTLX") String wtlx, @Param(value = "JHWTXH") String jhwtxh, @Param(value = "WTMS") String wtms,
                            @Param(value = "ZYH") String zyh, @Param(value = "BQID") String bqid, @Param(value = "JGID") String jgid,
                            @Param(value = "dbtype") String dbtype);

    int addNurseFocusProblem(@Param(value = "JLJD") String jljd, @Param(value = "JDXH") String jdxh,
                             @Param(value = "CJSJ") String cjsj, @Param(value = "CJGH") String cjgh, @Param(value = "KSSJ") String kssj,
                             @Param(value = "KSGH") String ksgh, @Param(value = "GLXH") String glxh, @Param(value = "GLLX") String gllx,
                             @Param(value = "WTLX") String wtlx, @Param(value = "JHWTXH") String jhwtxh, @Param(value = "WTMS") String wtms,
                             @Param(value = "ZYH") String zyh, @Param(value = "BQID") String bqid, @Param(value = "JGID") String jgid,
                             @Param(value = "JLGLLX") String jlgllx, @Param(value = "GLJL") String gljl,
                             @Param(value = "dbtype") String dbtype);

    int editNursePlanProblem(@Param(value = "JLWT") String jlwt, @Param(value = "WTMS") String wtms, @Param(value = "XGYS") String xgys,
                             @Param(value = "KSSJ") String kssj, @Param(value = "dbtype") String dbtype);

    int editNurseFocusProblem(@Param(value = "JLJD") String jljd, @Param(value = "WTMS") String wtms,
                              @Param(value = "KSSJ") String kssj, @Param(value = "dbtype") String dbtype);

    int deleteNursePlanProblem(@Param(value = "JLWT") String jlwt);

    int deleteNurseFocusProblem(@Param(value = "JLJD") String jljd);

    int terminateNursePlanProblem(@Param(value = "JLWT") String jlwt, @Param(value = "TZSJ") String tzsj, @Param(value = "TZGH") String tzgh,
                                  @Param(value = "dbtype") String dbtype);

    int terminateNurseFocusProblem(@Param(value = "JLJD") String jljd, @Param(value = "TZSJ") String tzsj, @Param(value = "TZGH") String tzgh,
                                   @Param(value = "dbtype") String dbtype);

    int addNursePlanMeasure(@Param(value = "JLCS") String jlcs, @Param(value = "JLWT") String jlwt, @Param(value = "CSMS") String csms,
                            @Param(value = "CSXH") String csxh, @Param(value = "ZDYBZ") String zdybz, @Param(value = "XJBZ") String xjbz,
                            @Param(value = "KSSJ") String kssj, @Param(value = "KSGH") String ksgh, @Param(value = "JSSJ") String jssj,
                            @Param(value = "JSGH") String jsgh, @Param(value = "CSZH") String cszh, @Param(value = "dbtype") String dbtype);

    int editNursePlanMeasure(@Param(value = "JLCS") String jlcs, @Param(value = "CSMS") String csms, @Param(value = "XJBZ") String xjbz,
                             @Param(value = "KSSJ") String kssj, @Param(value = "JSSJ") String jssj, @Param(value = "dbtype") String dbtype);

    int deleteNursePlanMeasure(@Param(value = "JLCS") String jlcs);

    int addNurseFocusMeasure(@Param(value = "JLCS") String jlcs, @Param(value = "JLJD") String jljd, @Param(value = "CSMS") String csms,
                             @Param(value = "CSXH") String csxh, @Param(value = "ZDYBZ") String zdybz, @Param(value = "XJBZ") String xjbz,
                             @Param(value = "KSSJ") String kssj, @Param(value = "KSGH") String ksgh, @Param(value = "JSSJ") String jssj,
                             @Param(value = "JSGH") String jsgh, @Param(value = "CSZH") String cszh, @Param(value = "dbtype") String dbtype);

    int editNurseFocusMeasure(@Param(value = "JLCS") String jlcs, @Param(value = "CSMS") String csms, @Param(value = "XJBZ") String xjbz,
                              @Param(value = "KSSJ") String kssj, @Param(value = "JSSJ") String jssj, @Param(value = "dbtype") String dbtype);

    int deleteNurseFocusMeasure(@Param(value = "JLCS") String jlcs);

    int addNursePlanGoal(@Param(value = "JLMB") String jlmb, @Param(value = "JLWT") String jlwt, @Param(value = "MBXH") String mbxh,
                         @Param(value = "MBMS") String mbms, @Param(value = "ZDYMB") String zdymb);

    int editNursePlanGoal(@Param(value = "JLMB") String jlmb, @Param(value = "MBMS") String mbms);

    int deleteNursePlanGoal(@Param(value = "JLMB") String jlmb);

    int addNursePlanDiagnosticBasis(@Param(value = "JLZD") String jlzd, @Param(value = "JLWT") String jlwt, @Param(value = "ZDXH") String zdxh,
                                    @Param(value = "ZDMS") String zdms, @Param(value = "ZDYBZ") String zdybz);

    int editNursePlanDiagnosticBasis(@Param(value = "JLZD") String jlzd, @Param(value = "ZDMS") String zdms);

    int deleteNursePlanDiagnosticBasis(@Param(value = "JLZD") String jlzd);

    int addNurseFocusDiagnosticBasis(@Param(value = "JLZD") String jlzd, @Param(value = "JLJD") String jljd, @Param(value = "ZDXH") String zdxh,
                                     @Param(value = "ZDMS") String zdms, @Param(value = "ZDYBZ") String zdybz);

    int editNurseFocusDiagnosticBasis(@Param(value = "JLZD") String jlzd, @Param(value = "ZDMS") String zdms);

    int deleteNurseFocusDiagnosticBasis(@Param(value = "JLZD") String jlzd);

    int addNursePlanRelevantFactor(@Param(value = "JLYS") String jlys, @Param(value = "JLWT") String jlwt, @Param(value = "YSXH") String ysxh,
                                   @Param(value = "YSMS") String ysms);

    int editNursePlanRelevantFactor(@Param(value = "JLYS") String jlys, @Param(value = "YSMS") String ysms);

    int deleteNursePlanRelevantFactor(@Param(value = "JLYS") String jlys);

    List<Problem> getPlanProblemHistoryList(@Param(value = "WTXH") String wtxh, @Param(value = "GLXH") String glxh,
                                            @Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid);

    List<Problem> getFocusProblemHistoryList(@Param(value = "WTXH") String wtxh, @Param(value = "GLXH") String glxh,
                                             @Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid);

    List<Goal> getGoalHistoryList(@Param(value = "WTXH") String wtxh, @Param(value = "JLWT") String jlwt);

    List<Measure> getPlanMeasureHistoryList(@Param(value = "JLWT") String jlwt);

    List<Measure> getFocusMeasureHistoryList(@Param(value = "JLWT") String jlwt);

    List<DiagnosticBasis> getPlanDiagnosticBasisHistoryList(@Param(value = "WTXH") String wtxh, @Param(value = "JLWT") String jlwt);

    List<DiagnosticBasis> getFocusDiagnosticBasisHistoryList(@Param(value = "WTXH") String wtxh, @Param(value = "JLWT") String jlwt);

    List<RelevantFactor> getRelevantFactorHistoryList(@Param(value = "WTXH") String wtxh, @Param(value = "JLWT") String jlwt);

    List<Evaluate> getPlanEvaluateHistoryList(@Param(value = "JLWT") String jlwt);

    List<Evaluate> getFocusEvaluateHistoryList(@Param(value = "JLWT") String jlwt);

    List<Evaluate> getEvaluateTemplateList(@Param(value = "WTXH") String wtxh);

    int addPlanProblemEvaluate(@Param(value = "JLPJ") String jlpj, @Param(value = "JLWT") String jlwt, @Param(value = "PJXH") String pjxh,
                               @Param(value = "PJMS") String pjms, @Param(value = "PJSJ") String pjsj, @Param(value = "PJGH") String pjgh,
                               @Param(value = "PJZH") String pjzh, @Param(value = "dbtype") String dbtype);

    int addFocusProblemEvaluate(@Param(value = "JLPJ") String jlpj, @Param(value = "JLJD") String jljd, @Param(value = "PJXH") String pjxh,
                                @Param(value = "PJMS") String pjms, @Param(value = "PJSJ") String pjsj, @Param(value = "PJGH") String pjgh,
                                @Param(value = "PJZH") String pjzh, @Param(value = "dbtype") String dbtype);

    int editPlanProblemEvaluateStatus(@Param(value = "JLWT") String jlwt);

    int editFocusProblemEvaluateStatus(@Param(value = "JLWT") String jlwt);

    int deletePlanProblemEvaluate(@Param(value = "JLPJ") String jlpj);

    int deleteFocusProblemEvaluate(@Param(value = "JLPJ") String jlpj);

    List<Plan> getPlanListForSync(@Param(value = "JLWT") String jlwt);

    List<Plan> getFocusListForSync(@Param(value = "JLJD") String jlwt);

	String getPlanEvaluatePjzhByJlpj(@Param(value = "JLPJ") String jlpj);

	String getFocusEvaluatePjzhByJlpj(@Param(value = "JLPJ") String jlpj);
}
