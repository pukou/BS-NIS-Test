package com.bsoft.nis.common.servicesup.mapper;

import com.bsoft.nis.domain.core.CachedConfig;
import com.bsoft.nis.domain.core.SqlStr;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Describtion:字典缓存接口
 * Created: dragon
 * Date： 2016/10/19.
 */
public interface DictCachedMapper {
    CachedConfig getCachedConfigByName(String dictname) throws SQLException,DataAccessException;

    List<Map> getDictDatasBySql(SqlStr sql) throws SQLException,DataAccessException;
}
