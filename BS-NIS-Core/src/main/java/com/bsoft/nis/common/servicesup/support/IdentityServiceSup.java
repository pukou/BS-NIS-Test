package com.bsoft.nis.common.servicesup.support;

import com.bsoft.nis.common.servicesup.mapper.IdentityMapper;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/14.
 */
@Service
public class IdentityServiceSup extends RouteDataSourceService {
    @Autowired
    IdentityMapper mapper;

    @Transactional(readOnly = true)
    public List<Map> getIdentityList() throws SQLException,DataAccessException{
        return mapper.getIdentityList();
    }

    @Transactional(readOnly = true)
    public Map<String,Object> getCurValue(String table, String tableName) throws SQLException,DataAccessException{
        return mapper.getCurValue(table, tableName);
    }

    @Transactional(readOnly = true)
    public List<Map> getCurList(String table, String tableName) throws SQLException,DataAccessException{
        return mapper.getCurList(table,tableName);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertItem(String table, String tableName, long dqz, int count, int dzz) throws SQLException,DataAccessException{
        mapper.insertItem(table,tableName,dqz,count,dzz);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCurValue(String table, String tableName, int count) throws SQLException,DataAccessException{
        mapper.updateCurValue(table,tableName,count);
    }
}
