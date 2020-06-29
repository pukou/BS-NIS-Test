package com.bsoft.nis.core.datasource;

/**
 * 多数据源切换
 * Created by Administrator on 2016/10/11.
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<DataSource> contextHolder = new ThreadLocal<DataSource>();

    private static final ThreadLocal<String> dataBaseTypeName = new ThreadLocal<>();

    public static void setDataSource(DataSource datasource) {
        contextHolder.set(datasource);
    }

    public static DataSource getDataSource() {
        return contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }

    public static void setDatabaseTypeName(String databaseTypeName){
        dataBaseTypeName.set(databaseTypeName);
    }

    public static String getDataBaseTypeName(){
        return dataBaseTypeName.get();
    }
}
