package com.bsoft.nis.core.datasource;

/**
 * 切换或保持当前数据源
 * Created by Administrator on 2016/10/11.
 */
public class RouteDataSourceService {

    public void keepOrRoutingDateSource(DataSource source) {

        DataSource old = DataSourceContextHolder.getDataSource();
        if (source != old) {
            DataSourceContextHolder.setDataSource(source);
        }

        // set dbase type for every thread
        String dbtype ;
        dbtype = DataBaseTypeHandler.getDbType(source);
        DataSourceContextHolder.setDatabaseTypeName(dbtype);
    }

    /**
     * get current DataSource Database Type From ThreadLocal
     * @return
     */
    public String getCurrentDataSourceDBtype(){
        return DataSourceContextHolder.getDataBaseTypeName();
    }
}
