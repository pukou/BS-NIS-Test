package com.bsoft.nis.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SQL语句包装类
 * Created by Administrator on 2016/10/19.
 */
public class SqlStr {
    private String sql;

    public SqlStr(){}

    public SqlStr(String v1){
        this.sql = v1;
    }
    @JsonProperty(value = "sql")
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
