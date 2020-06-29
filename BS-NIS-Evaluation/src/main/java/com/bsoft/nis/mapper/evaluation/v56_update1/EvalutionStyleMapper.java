package com.bsoft.nis.mapper.evaluation.v56_update1;

import com.bsoft.nis.domain.evaluation.evalnew.ComboUi;
import com.bsoft.nis.domain.evaluation.evalnew.EvalutionStyle;
import com.bsoft.nis.domain.evaluation.evalnew.EvalutionStyleKey;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

public interface EvalutionStyleMapper {
    int deleteByPrimaryKey(EvalutionStyleKey key);

    int insert(EvalutionStyle record);

    int insertSelective(EvalutionStyle record);

    List<EvalutionStyle> selectByPrimaryKey(EvalutionStyleKey key) throws SQLException;

    int updateByPrimaryKeySelective(EvalutionStyle record);

    int updateByPrimaryKeyWithBLOBs(EvalutionStyle record);

    int updateByPrimaryKey(EvalutionStyle record);

    List<ComboUi> getComboboxDatas(@Param(value = "sql") String sql) throws SQLException;
}