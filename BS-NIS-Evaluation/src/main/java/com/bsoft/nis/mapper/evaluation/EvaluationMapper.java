package com.bsoft.nis.mapper.evaluation;


import com.bsoft.nis.domain.dangerevaluate.DERecord;
import com.bsoft.nis.domain.evaluation.EvaluateRecordItem;
import com.bsoft.nis.domain.evaluation.SingleCharacter;
import com.bsoft.nis.domain.evaluation.db.*;
import com.bsoft.nis.domain.evaluation.dynamic.Classification;
import com.bsoft.nis.domain.evaluation.dynamic.Form;
import com.bsoft.nis.domain.evaluation.dynamic.SqlDropData;
import com.bsoft.nis.domain.evaluation.pgnr.EvalFljlVo;
import com.bsoft.nis.domain.evaluation.pgnr.EvalJlxmVo;
import com.bsoft.nis.domain.evaluation.pgnr.EvalXmxxVo;
import com.bsoft.nis.domain.healthguid.HealthGuidData;
import com.bsoft.nis.domain.lifesign.LifeSignHistoryDataItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface EvaluationMapper {

   List<EvaluateRecordItem> getRcordFromEMR(@Param(value = "ZYH")String zyh, @Param(value = "KSSJ")String start, @Param(value = "JSSJ")String end, @Param(value = "JGID")String jgid,@Param(value = "dbtype") String dbtype);

   List<EvaluateRecordItem> getRcordFromMOB(@Param("ZYH")String zyh, @Param(value = "KSSJ")String start, @Param(value = "JSSJ")String end, @Param("JGID")String jgid,@Param(value = "dbtype") String dbtype);

   List<EvaluateRecordItem> GetNewEvaluationList(@Param("BQDM")String bqdm, @Param("JGID")String jgid);

   List<EvaluateRecordItem> GetNewEvaluationListForYslx(@Param("YSLX")String yslx, @Param("BQDM")String bqdm, @Param("JGID")String jgid);

   EvaluateBDJLVo getBDJL(@Param("JLXH")String jlxh);

   List<EvaluateBDJLXMVo> getBDJLXM(@Param("JLXH")String jlxh, @Param("YSXM")String ysxm);

   List<EvaluateBDJLXXVo> getBDJLXX(@Param("JLXH")String jlxh, @Param("YSXX")String ysxx, @Param("YSXM")String ysxm);


   Form getBDYS(@Param("YSXH")String ysxh, @Param("JGID")String jgid);

   List<Classification> getYSFL(@Param("YSXH")String ysxh, @Param("JLXH")String jlxh);

   List<EvaluateBDYSXMVo> getFLXM(@Param("YSXH")String ysxh,@Param("DZLX")String dzlx);

   List<EvaluateBDYSXXVo> getFLXX(@Param("YSXH")String ysxh,@Param("DZLX")String dzlx);

   List<LifeSignHistoryDataItem> getSMTZ(@Param("ZYH")String zyh, @Param("JGID")String jgid);

   List<DERecord> getFXPG(@Param("ZYH")String zyh, @Param("JGID")String jgid);

   List<HealthGuidData> getJKXJ(@Param("ZYH")String zyh, @Param("JGID")String jgid);

   Integer getBTBZ(@Param("YSXH")String ysxh, @Param("YSXM")Long ysxm, @Param("BQDM")String bqdm, @Param("JGID")String jgid);

   List<SqlDropData> getDropListBySql(@Param("SQL")String sql);

   Boolean addBDJL(EvaluateBDJLVo bdjlVo);

   List<EvaluateBDYSFLVo> getBDYSFL(@Param("YSXH")String ysxh);

   Boolean addBDJLFL(EvaluateBDJLFLVo jlflVo);

   Integer getBDJLCount(@Param("YSXH")String ysxh, @Param("ZYH")String zyh, @Param("JGID")String jgid);

   //add 2017年4月26日11:15:26
   String getSJHQFS(@Param("YSLX")String yslx);

   Boolean updateBDJL(EvaluateBDJLVo bdjlVo);

   Boolean updateBDJLXM(EvaluateBDJLXMVo jlxmVo);

   Boolean updateBDJLXX(EvaluateBDJLXXVo xxVo);

   Boolean addBDJLXM(EvaluateBDJLXMVo jlxmVo);

   Boolean addBDJLXX(EvaluateBDJLXXVo xxVo);

   Boolean deleteBDJLXX(@Param("YSXX")String ysxx);

   List<EvaluateBDJLFLVo> getBDJLFL(@Param("JLXH")String jlxh, @Param("YSFL")String ysfl);

   List<SingleCharacter> getSingle(@Param("BQDM")String bqdm, @Param("YSXH")String ysxh, @Param("JGID")String jgid);

   Boolean updateBDJLFL(EvaluateBDJLFLVo jlflVo);

   List<EvaluateBDJLVo> getBDQMXX(@Param("JLXH")String jlxh);

   Boolean updateBDJLFLPGNR(@Param(value = "PGNR")String flpgnr, @Param(value = "JLXH")String jlxh,@Param(value = "JLFL")String jlfl);

   Boolean updatteBDJLPGNR(@Param(value = "PGNR")String pgnr, @Param("JLXH")String jlxh);
   @Deprecated
   Boolean updateBDJLFLFLLX(@Param(value = "JLFL")String jlfl, @Param(value = "FLLX")String fllx);

   Boolean updateBDJLFLFLLXNew(@Param(value = "JLXH")String jlxh,@Param(value = "YSFL")String ysfl, @Param(value = "FLLX")String fllx);

   List<LifeSignHistoryDataItem> getSMTZByGroupId(@Param(value = "CJZH") String dzbdjl);

   List<DERecord> getFXPGByPrimaryKey(@Param(value = "PGXH") String dzbdjl);

   List<EvalFljlVo> getEvalFljl(@Param(value = "JLXH") long jlxh);
   List<EvalXmxxVo> getEvalYsxm(@Param(value = "YSXH") long ysxh);
   List<EvalXmxxVo> getEvalYsxx(@Param(value = "YSXH") long ysxh);
   List<EvalJlxmVo> getEvalJlxm(@Param(value = "JLXH") long jlxh);
   List<EvalJlxmVo> getEvalJlxx(@Param(value = "JLXH") long jlxh);

}
