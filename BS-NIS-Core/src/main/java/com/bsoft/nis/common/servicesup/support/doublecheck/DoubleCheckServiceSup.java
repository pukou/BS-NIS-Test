package com.bsoft.nis.common.servicesup.support.doublecheck;

import com.bsoft.nis.common.servicesup.mapper.doublecheck.DoubleCheckMapper;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.core.doublecheck.DoubleCheckType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Describtion:日志子服务
 * Created: dragon
 * Date： 2016/11/22.
 */
@Service
public class DoubleCheckServiceSup extends RouteDataSourceService {
    @Autowired
    DoubleCheckMapper mapper;

    public void addDoubleCheck(DoubleCheckType doubleCheckType)
            throws SQLException, DataAccessException {
        String dbtype = getCurrentDataSourceDBtype();
        mapper.addDoubleCheck(doubleCheckType,dbtype);
    }

    public List<DoubleCheckType> getDoubleCheck(String hdbs1, String jgid)
            throws SQLException, DataAccessException{
        return mapper.getDoubleCheck(hdbs1,jgid);
    }
}
