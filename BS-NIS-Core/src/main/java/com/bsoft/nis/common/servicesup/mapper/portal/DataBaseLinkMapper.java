package com.bsoft.nis.common.servicesup.mapper.portal;

import com.bsoft.nis.domain.core.portal.DataBaseLink;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.util.List;

/**
 * Describtion:数据库连接
 * Created: dragon
 * Date： 2016/10/31.
 */
public interface DataBaseLinkMapper {

    List<DataBaseLink> getDataBaseLinkName(@Param(value = "DBID") String databaseid) throws SQLException,DataAccessException;

}
