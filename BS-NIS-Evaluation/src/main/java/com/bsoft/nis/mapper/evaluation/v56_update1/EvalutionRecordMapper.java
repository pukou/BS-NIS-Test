package com.bsoft.nis.mapper.evaluation.v56_update1;

import com.bsoft.nis.domain.evaluation.evalnew.EvalutionRecord;
import com.bsoft.nis.domain.evaluation.evalnew.EvalutionStyle;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface EvalutionRecordMapper {
    int deleteByPrimaryKey(Long JLXH);

    int insert(EvalutionRecord record);

    int insertSelective(EvalutionRecord record);

    EvalutionRecord selectByPrimaryKey(Long JLXH) throws SQLException;

    List<EvalutionRecord> selectRecordList(@Param("ZYH") Long ZYH, @Param("BRBQ") Integer BRBQ, @Param("JGID") Integer JGID) throws SQLException;

    List<EvalutionStyle> selectStyleList(@Param("JGID") Integer JGID) throws SQLException;

    List<EvalutionStyle> selectStyleByPrimaryKey(@Param("YSXH") Long YSXH,@Param("JGID") Integer JGID) throws SQLException;

    int updateByPrimaryKeySelective(EvalutionRecord record);

    int updateByPrimaryKeyWithBLOBs(EvalutionRecord record);

    int updateByPrimaryKey(EvalutionRecord record);

    int updateNurseSign(@Param(value = "JLXH") String jlxh,@Param(value = "QMGH") String usercode,@Param(value = "QMXM") String username,@Param(value = "QMSJ") Date now);

    int updatePNurseSign(@Param(value = "JLXH") String jlxh,@Param(value = "SYGH") String usercode,@Param(value = "SYXM") String username,@Param(value = "SYSJ") Date now);

    int clearSignInfo(@Param(value = "JLXH") String jlxh,@Param(value = "WHO") Integer who);
}