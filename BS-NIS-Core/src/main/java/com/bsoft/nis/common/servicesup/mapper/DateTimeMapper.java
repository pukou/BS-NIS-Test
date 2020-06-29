package com.bsoft.nis.common.servicesup.mapper;

import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/10/18.
 */
public interface DateTimeMapper {
    String now(@Param(value = "dbtype") String dbtype) throws SQLException;

    String now1(@Param(value = "dbtype") String dbtype) throws SQLException;
}
