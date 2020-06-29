package com.bsoft.nis.core.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:database product type handler
 * Created: dragon
 * Date： 2016/12/7.
 */
public class DataBaseTypeHandler {

    public static List<DataBase> APP_DATABASES = new ArrayList<>();

    public static String getDbType(DataSource dataSource){
        String dbname = "",dbtype = "sqlserver";
        switch (dataSource){
            case HRP:
                dbname = "HRP";
                break;
            case PORTAL:
                dbname ="PORTAL";
                break;
            case ENR:
                dbname = "ENR";
                break;
            case EMR:
                dbname = "EMR";
                break;
            case LIS:
                dbname ="LIS";
                break;
            case RIS:
                dbname ="RIS";
                break;
            case MOB:
                dbname = "MOB";
                break;
        }

        for (DataBase db:APP_DATABASES){
            if (db.name.equals(dbname)){
                dbtype = db.dbtype;
                break;
            }
        }

        return dbtype;
    }
}
