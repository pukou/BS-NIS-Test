package com.bsoft.nis.common.service;

import com.bsoft.nis.common.servicesup.mapper.DateTimeMapper;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/18.
 */
@Service
public class DateTimeService extends RouteDataSourceService {
    private static final Log logger = LogFactory.getLog(DateTimeService.class);

    @Autowired
    DateTimeMapper mapper;

    /**
     * 获取数据库当前时间
     *
     * @param dataSource 非空，要查询的数据库
     * @return 成功返回数据库当前时间，失败返回null
     */
    public String now(DataSource dataSource) {

        if (dataSource == null) {
            return null;
        }
        keepOrRoutingDateSource(dataSource);
        String dateTime = null;
        try {
            String dbtype = getCurrentDataSourceDBtype();
            dateTime = mapper.now(dbtype);
        } catch (RuntimeException | SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return dateTime;
    }


    /**
     * 获取数据库当前时间，返回日期类型
     *
     * @param dataSource
     * @return
     */
    public Date nowDate(DataSource dataSource) {
        String nowStr = now(dataSource);
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(nowStr);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return date;
    }

    public String getNowDateStr(DataSource dataSource) {
        Date date = nowDate(dataSource);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateStr = sdf.format(date);
        return nowDateStr;
    }

    public String getNowDateTimeStr(DataSource dataSource) {
        Date date = nowDate(dataSource);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDateStr = sdf.format(date);
        return nowDateStr;
    }
}
