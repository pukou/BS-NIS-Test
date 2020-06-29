package com.bsoft.nis.mapper.advicequery;

import com.bsoft.nis.domain.adviceexecute.PhraseModel;
import com.bsoft.nis.domain.adviceqyery.AdviceDetail;
import com.bsoft.nis.domain.adviceqyery.TransfusionPatrolRecord;
import com.bsoft.nis.domain.adviceqyery.TransfusionVo;
import com.bsoft.nis.domain.adviceqyery.db.AdviceBqyzVo;
import com.bsoft.nis.domain.adviceqyery.db.TransfusionInfoVoTemp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 医嘱执行(查询)
 * User: 苏泽雄
 * Date: 16/12/16
 * Time: 17:08:49
 */
public interface AdviceQueryMapper {

    List<AdviceBqyzVo> getAdviceBqyzList(@Param("ZYH") String zyh, @Param("KSSJ") String kssj,
                                         @Param("JSSJ") String jssj, @Param("DQSJ") String now, @Param("JGID") String jgid,
                                         @Param("JLLX") String jllx, @Param("LSYZ") String lsyz, @Param("WXBZ") String wxbz,
                                         @Param("dbtype") String dbtype);

    List<AdviceBqyzVo> getAdviceOne(@Param("JLXH") String jlxh, @Param("JGID") String jgid);

    List<AdviceDetail> getAdviceRecord(@Param("YZXH") String yzxh, @Param("JGID") String jgid);

    List<TransfusionVo> getTransfusionListByZyh(@Param("ZYH") String zyh,
                                                @Param("KSSJ") String kssj, @Param("JSSJ") String jssj, @Param("SYZT") String syzt,
                                                @Param("JGID") String jgid, @Param("dbtype") String dbtype);

    List<String> getSydhByZyh(@Param("ZYH") String zyh, @Param("KSSJ") String kssj,
                              @Param("JSSJ") String jssj, @Param("SYZT") String syzt, @Param("JGID") String jgid,
                              @Param("dbtype") String dbtype);

    List<TransfusionInfoVoTemp> getTransfusionInfoList(@Param("SYDHS") List<String> sydhList);

    String getYzmcByYzxh(@Param("JLXH") String jlxh, @Param("JGID") String jgid);

    List<PhraseModel> getTransfusionReactionList(@Param("JGID") String jgid);

    List<TransfusionPatrolRecord> getTransfusionPatrolList(@Param("SYDH") String sydh,
                                                           @Param("JGID") String jgid);

    List<String> getKfdhListByZyh(@Param("ZYH") String zyh, @Param("KSSJ") String kssj,
                                  @Param("JSSJ") String jssj, @Param("JGID") String jgid,
                                  @Param("dbtype") String dbtype);

    List<String> getZsdhListByZyh(@Param("ZYH") String zyh, @Param("KSSJ") String kssj,
                                  @Param("JSSJ") String jssj, @Param("JGID") String jgid,
                                  @Param("dbtype") String dbtype);
}
