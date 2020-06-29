package com.bsoft.nis.core.datasource;


import org.apache.commons.lang3.StringUtils;

/**
 * description:数据源工具类
 * create by: dragon xinghl@bsoft.com.cn
 * create time:2017/11/23 11:01
 * since:5.6 update1
 */
public class DataSourceUtil {
    public static DataSource findDataSourceByDbString(String dbString){
        DataSource ds = DataSource.MOB;
        if (StringUtils.isBlank(dbString)){
            ds = DataSource.MOB;
        }
        // SQLMOBENR/SQLMOBHIS/SQLMOB
        if (dbString.equals("SQLMOBHIS")){
            ds = DataSource.HRP;
        }else if (dbString.equals("SQLMOBENR")){
            ds = DataSource.ENR;
        }else if (dbString.equals("SQLMOB")){
            ds = DataSource.MOB;
        }
        return ds;
    }
}
