package com.bsoft.nis.core.datasource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.List;
import java.util.Properties;

/**
 * Describtion:database properties file resolve into DataBaseTypeHandler APP_DATABASES
 * <p>
 *     to get database source dbtype when switching datasource
 * </p>
 * Created: dragon
 * Date： 2016/12/7.
 */
public class DataBasePropertyPlaceholder extends PropertyPlaceholderConfigurer{
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);

        for (Object key1 : props.keySet()) {
            String keyStr = key1.toString();
            String value = props.getProperty(keyStr);
            if (keyStr.contains("druid")) continue;
            String[] keys = keyStr.split("\\.");
            if (keys.length <=0) continue;
            if (keys.length != 2) continue;
            String key = keys[0];
            String property = keys[1];

            if (property.contains("driverClassName")){
                DataBase dataBase = new DataBase();
                dataBase.name = keys[0].toUpperCase();
                if (value.contains("sqlserver")){
                    dataBase.dbtype = "sqlserver";
                }else if (value.contains("oracle")){
                    dataBase.dbtype = "oracle";
                }else if (value.contains("mysql")){
                    dataBase.dbtype = "mysql";
                }else if (value.contains("db2")){
                    dataBase.dbtype ="db2";
                }
                DataBaseTypeHandler.APP_DATABASES.add(dataBase);
            }else{
                continue;
            }
        }
    }
}
