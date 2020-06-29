package com.bsoft.nis.core.datasource;

/**
 * Describtion:database enum
 * Created: dragon
 * Date： 2016/12/7.
 */
public enum  DataBaseEnum {
    SQLServer("sqlserver"),
    Oracle("oracle");

    protected String name;

    DataBaseEnum(String name){
        this.name = name;
    }

    public String getDbType(){
        return this.name;
    }
}
