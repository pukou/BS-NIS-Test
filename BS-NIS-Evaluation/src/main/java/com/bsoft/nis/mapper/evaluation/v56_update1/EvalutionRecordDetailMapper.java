package com.bsoft.nis.mapper.evaluation.v56_update1;

import com.bsoft.nis.domain.evaluation.evalnew.EvalutionRecordDetail;

public interface EvalutionRecordDetailMapper {
    int deleteByPrimaryKey(Long MXXH);

    int insert(EvalutionRecordDetail record);

    int insertSelective(EvalutionRecordDetail record);

    EvalutionRecordDetail selectByPrimaryKey(Long MXXH);

    int updateByPrimaryKeySelective(EvalutionRecordDetail record);

    int updateByPrimaryKey(EvalutionRecordDetail record);
}