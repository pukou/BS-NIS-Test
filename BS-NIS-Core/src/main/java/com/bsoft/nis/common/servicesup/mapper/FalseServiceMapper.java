package com.bsoft.nis.common.servicesup.mapper;

import com.bsoft.nis.domain.core.FalseDict;
import com.bsoft.nis.domain.core.SqlStr;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */
public interface FalseServiceMapper {
    List<FalseDict> getDictCollectionOneKey(SqlStr sql);
}
