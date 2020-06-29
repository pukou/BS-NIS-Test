package com.bsoft.nis.common.servicesup.mapper.dataset;

import com.bsoft.nis.domain.core.SqlStr;
import com.bsoft.nis.domain.core.dataset.DataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Describtion:数据组Mapper
 * Created: dragon
 * Date： 2016/10/24.
 */
public interface DataSetServiceMapper {

    List<DataSet> getDataSetsBySJZXH(@Param(value = "SJZXH") String sjzxh) throws SQLException,DataAccessException;

    List<Map> getDataSetDatasBySql(SqlStr sqlStr) throws SQLException,DataAccessException;
}
