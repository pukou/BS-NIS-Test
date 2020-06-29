package com.bsoft.nis.mapper.dangerevaluate;

import com.bsoft.nis.domain.dangerevaluate.*;
import com.bsoft.nis.domain.dangerevaluate.db.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 风险评估Mapper
 * User: 苏泽雄
 * Date: 16/12/1
 * Time: 9:52:50
 */
public interface DangerEvaluateMapper {

	List<DEQualityControlVo> getDEQualityControl();

	List<DEOverview> getDEList(@Param("JGID") String jgid, @Param("BRNL") String brnl, @Param("BQID") String bqid);

	List<SimDERecord> getSimDERecordList(@Param("PGDH") String pgdh, @Param("ZYH") String zyh,@Param("dbtype") String dbtype);

	List<DEQualityControlVo> getSimQualityControl(@Param("PGDH") String pgdh, @Param("order") String order);

	List<DEFactor> getDEFactorList(@Param("PGDH") String pgdh);

	List<FactorGoal> getDEFactorGoalList(@Param("FXYZ") String fxyz);

	List<String> getFirstDERecord(@Param("ZYH") String zyh, @Param("PGDH") String pgdh, @Param("PGLX") String pglx, @Param("JGID") String jgid);


	DERecord getDERecordByPgxh(@Param("PGXH") String pgxh);


	List<DEPGHBean> getFXPGDHList(@Param("PGLX") String pglx, @Param("JGID") String jgid);


	DEMeasureFormVo getDEMeasureCode(@Param("PGDH") String pgdh, @Param("JGID") String jgid);

	List<DEMeasureItemVo> getDEMeasureItems(@Param("PGDH") String pgdh, @Param("JGID") String jgid);

	List<DEEvaluate> getDEEvaluateList(@Param("PGDH") String pgdh, @Param("JGID") String jgid);

	List<MeasureRecord> getDEMeasureRecord(@Param("PGXH") String pgxh);

	List<DEEvaluate> getCSPJList(@Param("CSDH") String csdh);

	SimMeasureRecord getPreOneCSJL(@Param("CSDH") String csdh,@Param("PGXH") String pgxh,@Param("ZYH") String zyh);

	List<DEMeasureItemVo> getDEMeasureItemRecord(@Param("CSDH") String csdh, @Param("JLXH") String jlxh);

	List<FactorGoal> getDEFactorGoalWithMXXH(@Param("FXYZ") String fxyz, @Param("PGXH") String pgxh);

	Integer addDERecord(DERecordVo recordVo);

	Integer updateDERecord(DERecordVo record);

	Integer addDERecordDetail(DERecordDetailVo detail);

	Integer deleteDERecordDetail(@Param("MXXH") String mxxh);

	Integer finishDEQCRemind(@Param("PGLX") String pglx, @Param("ZYH") String zyh);

	Integer addDEQCRemind(DEQCRemindVo remind);

	List<MeasureOverview> getDEMeasureList(@Param("PGDH") String pgdh, @Param("JGID") String jgid);

	List<SimMeasureRecord> getSimMeasureRecord(@Param("BDXH") String bdxh, @Param("PGXH") String pgxh, @Param("ZYH") String zyh, @Param("JGID") String jgid);

	Integer addMeasureRecord(MeasureRecordVo record);

	Integer updateMeasureRecord(MeasureRecordVo record);

	Integer addMeasureItem(MeasureItemVo item);

	Integer deleteMeasureItem(@Param("JLXM") String jlxm);

	DEQCRemindVo getZYHOfDEQCRemind(@Param("PGXH") String pgxh);

	String getMAXOfDEQCRemind(@Param("ZYH") String zyh, @Param("PGLX") String pglx);

	Integer returnDEQCRemind(@Param("PGXH") String pgxh);

	Integer cancleDEQCRemind(@Param("PGXH") String pgxh);

	Integer deleteDERecord(@Param("PGXH") String pgxh);

	Integer deleteDEMeasure(@Param("JLXH") String jlxh);

	Integer checkDERecord(@Param("HSZQM") String hszqm, @Param("HSZQMSJ") String hszqmsj, @Param("PGXH") String pgxh, @Param("dbtype") String dbtype);

	Integer evaluateMeasure(@Param("JLXH") String jlxh, @Param("HSZQM") String hszqm, @Param("HSZQMSJ") String hszqmsj, @Param("CSPJ") String cspj, @Param("dbtype") String dbtype);

	List<PainEvaluate> getPainEvaluate(@Param("JGID") String jgid);

	// TODO SQL语句有待优化
	List<PEOption> getPEOption(@Param("PGXH") String pgxh, @Param("XMXH") String xmxh);

	PEOption getPEOptionOfNoXXXH(@Param("PGXH") String pgxh, @Param("XMXH") String xmxh);

	List<PERecordVo> getPERecord();

	Integer addPERecord(PERecordVo record);

	Integer deletePERecord(@Param("JLXM") String jlxm);

	Integer updatePERecord(PERecordVo record);
}
