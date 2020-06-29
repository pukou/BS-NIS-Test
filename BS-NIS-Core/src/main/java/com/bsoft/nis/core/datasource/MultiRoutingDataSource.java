package com.bsoft.nis.core.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源路由
 * Created by Administrator on 2016/10/11.
 */
public class MultiRoutingDataSource extends AbstractRoutingDataSource{
    @Override
    protected Object determineCurrentLookupKey() {
        DataSource db = DataSourceContextHolder.getDataSource();
        return db;
    }
}
