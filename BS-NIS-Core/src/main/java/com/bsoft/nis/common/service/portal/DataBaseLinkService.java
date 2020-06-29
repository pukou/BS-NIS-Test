package com.bsoft.nis.common.service.portal;

import com.bsoft.nis.common.servicesup.mapper.portal.DataBaseLinkMapper;
import com.bsoft.nis.domain.core.portal.DataBaseLink;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:数据库连接服务
 * Created: dragon
 * Date： 2016/10/31.
 */
@Service
public class DataBaseLinkService extends RouteDataSourceService{

    private Log logger = LogFactory.getLog(DataBaseLinkService.class);

    @Autowired
    DataBaseLinkMapper mapper;

    /**
     * 根据连接ID获取数据库连接名
     * @param databaseid
     * @return
     */
    public String getDataBaseLinkName(String databaseid){
        keepOrRoutingDateSource(DataSource.PORTAL);
        String ret = "";
        List<DataBaseLink> links = new ArrayList<>();

        try{
            links = mapper.getDataBaseLinkName(databaseid);
            if (links.size() > 0){
                ret = links.get(0).getLJMC();
            }
        }catch (SQLException | DataAccessException ex){
            logger.error(ex.getMessage(), ex);
        }

        return  ret;
    }

    /**
     * 根据事务名获取事务
     * @param sourcename
     * @return
     */
    public DataSource getDataSourceBySourceName(String sourcename){
        DataSource dataSource = DataSource.MOB;
        if (sourcename.equals("SQLMOB")){
            dataSource = DataSource.MOB;
        }else if (sourcename.equals("SQLMOBHIS") || sourcename.equals("SQLHIS") || sourcename.equals("SQLCIS")){
            dataSource = DataSource.HRP;
        }else if (sourcename.equals("SQLENR") || sourcename.equals("SQLMOBENR")){
            dataSource = DataSource.ENR;
        }else{
            dataSource = DataSource.MOB;
        }
        return dataSource;
    }
}
