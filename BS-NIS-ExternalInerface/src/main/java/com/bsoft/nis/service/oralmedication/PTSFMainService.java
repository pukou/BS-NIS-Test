package com.bsoft.nis.service.oralmedication;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.core.SqlStr;
import com.bsoft.nis.mapper.oralmedication.OralMedicationMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * @Classname PTSFMainService
 * @Description TODO
 * @Date 2020/6/29/0029 18:20
 * @Created by ling
 */
@Service
public class PTSFMainService extends RouteDataSourceService {

    private Log logs = LogFactory.getLog(PTSFMainService.class);

    @Autowired
    OralMedicationMapper mapper;

    public String getPTSFBySQL(DataSource ds, String sql){
        String sf = null;
        SqlStr sqlO = new SqlStr();
        sqlO.setSql(sql);
        keepOrRoutingDateSource(ds);
        try {
            sf = mapper.getPTSF(sqlO);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sf;
    }


}
