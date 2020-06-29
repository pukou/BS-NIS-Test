package com.bsoft.nis.mapper.evaluation.v56_update1;

import com.bsoft.nis.domain.evaluation.evalnew.EvalutionStyleKey;
import com.bsoft.nis.domain.evaluation.evalnew.EvalutionStyleProject;
import com.bsoft.nis.domain.evaluation.evalnew.EvalutionStyleProjectKey;

import java.sql.SQLException;
import java.util.List;

public interface EvalutionStyleProjectMapper {
    int deleteByPrimaryKey(EvalutionStyleProjectKey key);

    int insert(EvalutionStyleProject record);

    int insertSelective(EvalutionStyleProject record);

    EvalutionStyleProject selectByPrimaryKey(EvalutionStyleProjectKey key);

    int updateByPrimaryKeySelective(EvalutionStyleProject record);

    int updateByPrimaryKeyWithBLOBs(EvalutionStyleProject record);

    int updateByPrimaryKey(EvalutionStyleProject record);

    List<EvalutionStyleProject> selectRootProjectsByStyleKey(EvalutionStyleKey key) throws SQLException;

    List<EvalutionStyleProject> selectRootChildProjectsByStyleKey(EvalutionStyleKey key) throws SQLException;
}