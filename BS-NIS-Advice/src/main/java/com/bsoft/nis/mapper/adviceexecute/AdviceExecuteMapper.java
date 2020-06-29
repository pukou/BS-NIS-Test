package com.bsoft.nis.mapper.adviceexecute;

import com.bsoft.nis.domain.adviceexecute.*;
import com.bsoft.nis.domain.adviceexecute.db.PlanRefusalVo;
import com.bsoft.nis.domain.adviceqyery.db.TransfusionInfoVoTemp;
import com.bsoft.nis.domain.adviceqyery.db.TransfusionPatrolRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 医嘱执行
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-21
 * Time: 15:19
 * Version:
 */
public interface AdviceExecuteMapper {

    PlanInfo getPlanInfoByJhh(@Param(value = "JHH") String jhh, @Param(value = "JGID") String jgid);

    List<PlanInfo> getPlanInfoListByQrdh(@Param(value = "QRDH") String yzzh, @Param(value = "GSLX") String jhsj,
                                         @Param(value = "JGID") String jgid);

    List<PlanInfo> getPlanInfoListByYzzhAndJhsj(@Param(value = "YZZH") String yzzh, @Param(value = "JHSJ") String jhsj,
                                                @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    List<PlanInfo> getPlanInfoListByYzzhAndSjbh(@Param(value = "YZZH") String yzzh, @Param(value = "SJBH") String sjbh,
                                                @Param(value = "JHRQ") String jhrq, @Param(value = "JGID") String jgid,
                                                @Param(value = "dbtype") String dbtype);

    PlanInfo getPlanInfoByYzxhAndJhsj(@Param(value = "YZXH") String yzxh, @Param(value = "JHSJ") String jhsj,
                                      @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    PlanInfo getPlanInfoByYzxhAndSjbh(@Param(value = "YZXH") String yzxh, @Param(value = "SJBH") String sjbh,
                                      @Param(value = "JHRQ") String jhrq, @Param(value = "JGID") String jgid,
                                      @Param(value = "dbtype") String dbtype);

    AdviceBqyzInfo getAdviceBqyzInfo(@Param(value = "ZYH") String zyh,@Param(value = "YZXH") String yzxh, @Param(value = "JGID") String jgid);

    List<AdviceBqyzInfo> getAdviceBqyzInfoList(@Param(value = "ZYH") String zyh,@Param(value = "YZZH") String yzzh, @Param(value = "JGID") String jgid);


    AdviceYzbInfo getAdviceYzbInfo(@Param(value = "ZYH") String zyh,@Param(value = "YZXH") String yzxh, @Param(value = "JGID") String jgid);

    List<AdviceYzbInfo> getAdviceYzbInfoList(@Param(value = "ZYH") String zyh,@Param(value = "YSYZBH") List<String> ysyzbhList, @Param(value = "JGID") String jgid);

    InjectionInfo getInjectionInfoByJhhAndSjd(@Param(value = "JHH") String jhh, @Param(value = "JGID") String jgid);

    InjectionInfo getInjectionInfoByJhhAndSjbh(@Param(value = "JHH") String jhh, @Param(value = "JGID") String jgid);

    InjectionInfo getInjectionInfoByBarcode(@Param(value = "TMBH") String tmbh, @Param(value = "JGID") String jgid);

    List<InjectionDetailInfo> getInjectionDetailInfoList(@Param(value = "ZSDH") String zsdh, @Param(value = "JGID") String jgid);

    List<OralModelInfo> getOralModelInfoListByJhhAndSjd(@Param(value = "JHH") String jhh, @Param(value = "JGID") String jgid);

    List<OralModelInfo> getOralModelInfoListByJhhAndSjbh(@Param(value = "JHH") String jhh, @Param(value = "JGID") String jgid);

    List<OralModelInfo> getOralModelInfoListByBarcode(@Param(value = "TMBH") String tmbh, @Param(value = "JGID") String jgid);

    List<OralModelInfo> getOralModelInfoListByKfdh(@Param(value = "KFDH") String kfdh, @Param(value = "JGID") String jgid);

    TransfusionInfo getTransfusionInfoByJhhAndSjd(@Param(value = "JHH") String jhh, @Param(value = "JGID") String jgid);

    TransfusionInfo getTransfusionInfoByJhhAndSjbh(@Param(value = "JHH") String jhh, @Param(value = "JGID") String jgid);

    TransfusionInfo getTransfusionInfoByBarcode(@Param(value = "TMBH") String tmbh, @Param(value = "JGID") String jgid);

    TransfusionInfo getTransfusionInfoBySydh(@Param(value = "SYDH") String tmbh, @Param(value = "JGID") String jgid);

    List<TransfusionDetailInfo> getTransfusionDetailInfoList(@Param(value = "SYDH") String sydh, @Param(value = "JGID") String jgid);

    List<FlowRecordDetailInfo> getFlowRecordDetailInfoListForOral(@Param(value = "KFDH") String sydh, @Param(value = "JGID") String jgid);

    List<FlowRecordDetailInfo> getFlowRecordDetailInfoListForTran(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid);

    List<String> getSydhListForDp(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid);

    List<TransfusionInfo> getTransfusionInfoListByZyh(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid,@Param(value = "SYRQ") String SYRQ,@Param(value = "dbtype") String dbtype);
    /*
        升级编号【56010053】============================================= start
        多瓶超过2瓶转接瓶后提示选择接哪瓶的问题
        ================= Classichu 2017/11/14 16:25

        */
    List<TransfusionInfo> getTransfusionInfoListByZyh4TransfuseExecut(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid,@Param(value = "SYRQ") String SYRQ, @Param(value = "dbtype") String dbtype);
/* =============================================================== end */

    List<TransfusionInfo> getTransfusionInfoListByZyh4TransfuseExecutAll(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid,@Param(value = "SYRQ") String SYRQ, @Param(value = "dbtype") String dbtype);

    int editPlanInfoForDoubleCheckControl(PlanInfo planInfo);

    int editOralInfoForDoubleCheckControl(@Param(value = "HDSJ") String hdsj, @Param(value = "HDGH") String hdgh,
                                          @Param(value = "KFMX") String KFMX, @Param(value = "dbtype") String dbtype);

    int editTransfusionInfoForDoubleCheckControl(@Param(value = "HDSJ") String hdsj, @Param(value = "HDGH") String hdgh,
                                                 @Param(value = "SYDH") String sydh, @Param(value = "JGID") String jgid,
                                                 @Param(value = "dbtype") String dbtype);

    int editInjectionInfoForDoubleCheckControl(@Param(value = "HDSJ") String hdsj, @Param(value = "HDGH") String hdgh,
                                               @Param(value = "ZSDH") String zsdh, @Param(value = "JGID") String jgid,
                                               @Param(value = "dbtype") String dbtype);

    int editPlanInfo(PlanInfo planInfo);

    int editAdviceYzbInfo(AdviceYzbInfo adviceYzbInfo);

    int editAdviceYzbInfoHD(AdviceYzbInfo adviceYzbInfo);

    int editAdviceBqyzInfo(AdviceBqyzInfo adviceBqyzInfo);

    int editAdviceBqyzInfoHD(AdviceBqyzInfo adviceBqyzInfo);

    int editBQPlanInfo(PlanInfo planInfo);

	int editBQPlanInfoForTrans(PlanInfo planInfo);

    int editPlanInfoList(PlanInfo planInfo);

    int editInjectionInfoJs(InjectionInfo injectionInfo);

    int editInjectionInfoKsAndJs(InjectionInfo injectionInfo);

    int editPlanInfoListJs(PlanInfo planInfo);

    int editPlanInfoListKsAndJs(PlanInfo planInfo);

    int editOralPackageInfo(OralPackageInfo oralPackageInfo);

	Integer checkRefuse(@Param(value = "JHH") String jhh, @Param(value = "DYXH") String dyxh);

	Integer insertPlanRefusal(PlanRefusalVo refusal);

	Integer updateAdvicePlanOfRefuse(PlanInfo advicePlan);

	Integer transfuseStopEnd(@Param(value = "SYDH") String sydh,
			@Param(value = "JSSJ") String jssj, @Param(value = "JSGH") String jsgh,
			@Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbType);

	Integer updateTransfusionStatus(@Param(value = "SYDH") String sydh,
			@Param(value = "SYZT") String syzt, @Param(value = "JGID") String jgid);

	Integer editTransfusion(TransfusionInfo transfusionInfo);

	Integer editTransfusion4Clear(TransfusionInfo transfusionInfo);

	List<TransfusionInfoVoTemp> getDropSpeedInfo(@Param(value = "SYDH") String sydh,
			@Param(value = "JGID") String jgid);

	Integer updatePlanInfoForSYD(PlanInfo planInfo);
	Integer updatePlanInfoForSYD4Clear(PlanInfo planInfo);

	Integer updatePlanInfoForSYDByTime(PlanInfo planInfo);
	Integer updatePlanInfoForSYDByTime4Clear(PlanInfo planInfo);

	TransfusionInfo transfuseStopCheck(@Param(value = "SYDH") String sydh,
			@Param(value = "JGID") String jgid);

	AdviceYzbInfo getZxsjOfAdviceYzb(@Param(value = "YZBXH") String yzbxh);

	List<String> getGLJHHByJHH(@Param(value = "JHHLIST") List<String> jhhList);

    List<PlanInfo> getPlanInfoList(@Param(value = "ZYH") String zyh, @Param(value = "START") String start,
                                   @Param(value = "END") String end, @Param(value = "GSLX") String gslx,
                                   @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);


    List<PhraseModel> getRefuseReasonList(@Param("JGID") String jgid);

    int CheckRefuse(@Param(value = "JHH") String jhh, @Param(value = "DYXH") String dyxh);

    int editPlanInfoForRefuseExecut(PlanInfo planInfo);

    int addRefuseExecutReason(@Param(value = "JHH") String jhh, @Param(value = "DYXH") String dyxh,
                              @Param(value = "JJSJ") String jjsj, @Param(value = "JJGH") String jjgh,
                              @Param(value = "dbtype") String dbtype);

	Integer insertTransfusionPatrol(TransfusionPatrolRecordVo record);

	Float getTbzxjlByLxh(@Param(value = "LXH") String lxh, @Param(value = "JGID") String jgid);

    Integer getNeedSyncDataCount(@Param(value = "ZYH") String zyh, @Param(value = "XMID") String xmid, @Param(value = "JHRQ") String jhrq,@Param(value = "dbtype") String dbtype);

    List<String> getSqpdList();

    List<AdviceBqyzExt> getAdviceBqyzExt(@Param(value = "FRDIS") List<FlowRecordDetailInfo> frdis);
}
