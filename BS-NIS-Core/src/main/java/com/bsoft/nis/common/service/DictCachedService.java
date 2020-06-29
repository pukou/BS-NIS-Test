package com.bsoft.nis.common.service;

import com.bsoft.nis.common.servicesup.mapper.DictCachedMapper;
import com.bsoft.nis.domain.core.CachedConfig;
import com.bsoft.nis.domain.core.SqlStr;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Describtion:字典缓存服务
 * Created: dragon
 * Date： 2016/10/19.
 */
@Service
public class DictCachedService extends RouteDataSourceService{
    private Log logger = LogFactory.getLog(DictCachedService.class);

    @Autowired
    DictCachedMapper mapper;

    public List<Map> getDictDatasByDictName(String dictname,String jgid,String whereStr){
        List<Map> list = new ArrayList<>();
        BizResponse<CachedConfig> res_config ;
        res_config = getCachedConfigByName(dictname);
        if (!res_config.isSuccess){
            return list;
        }
        CachedConfig config = res_config.data;
        String sql = config.getZSQL();
        if (sql == null){
            return list;
        }else{
            if(StringUtils.isEmpty(sql)){
                return list;
            }
        }

        // 1.组合SQL语句
        sql = sql.toUpperCase();
        if (jgid != null){
            if (sql.contains("WHERE")){
                sql += " AND JGID ='"+jgid+"'" ;
            }else{
                sql += " WHERE JGID ='"+jgid+"'" ;
            }
        }

        if (!StringUtils.isEmpty(whereStr)){
            if (sql.contains("WHERE")){
                sql += " AND  "+whereStr;
            }else{
                sql += " WHERE  "+whereStr;
            }
        }

        // 2.指定数据源
        String datasource = config.getDBLJ().trim().toUpperCase();
        switch (datasource){
            case "SQLMOBHIS":
                keepOrRoutingDateSource(DataSource.HRP);
                break;
            case "SQLHIS":
                keepOrRoutingDateSource(DataSource.HRP);
                break;
            case "SQLMOB":
                keepOrRoutingDateSource(DataSource.MOB);
                break;
            default:
                keepOrRoutingDateSource(DataSource.MOB);
                break;
        }

        // 3.查询字典数据（缓存）
        SqlStr sql1 = new SqlStr();
        sql1.setSql(sql);
        try{
            list = mapper.getDictDatasBySql(sql1);
        }catch (SQLException | DataAccessException ex){
            logger.error(ex.getMessage());
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return list;
    }
    /**
     * 获取字典缓存数据
     * @return
     */
    public List<Map> getDictDatasByDictName(String dictname,String jgid){
        List<Map> list = new ArrayList<>();
        BizResponse<CachedConfig> res_config ;
        res_config = getCachedConfigByName(dictname);
        if (!res_config.isSuccess){
            return list;
        }
        CachedConfig config = res_config.data;
        String sql = config.getZSQL();
        if (sql == null){
            return list;
        }else{
            if(StringUtils.isEmpty(sql)){
                return list;
            }
        }

        // 1.组合SQL语句
        sql = sql.toUpperCase();
        if (jgid != null){
            if (sql.contains("WHERE")){
                sql += " AND JGID ='"+jgid+"'" ;
            }else{
                sql += " WHERE JGID ='"+jgid+"'" ;
            }
        }

        // 2.指定数据源
        String datasource = config.getDBLJ().trim().toUpperCase();
        switch (datasource){
            case "SQLMOBHIS":
                keepOrRoutingDateSource(DataSource.HRP);
                break;
            case "SQLHIS":
                keepOrRoutingDateSource(DataSource.HRP);
                break;
            case "SQLMOB":
                keepOrRoutingDateSource(DataSource.MOB);
                break;
            default:
                keepOrRoutingDateSource(DataSource.MOB);
                break;
        }

        // 3.查询字典数据（缓存）
        SqlStr sql1 = new SqlStr();
        sql1.setSql(sql);
        try{
            list = mapper.getDictDatasBySql(sql1);
        }catch (SQLException | DataAccessException ex){
            logger.error(ex.getMessage());
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return list;
    }

    /**
     * 根据字典名称获取字典配置信息
     * @param dictname
     * @return
     */
    private BizResponse<CachedConfig> getCachedConfigByName(String dictname){
        keepOrRoutingDateSource(DataSource.PORTAL);
        BizResponse<CachedConfig> response = new BizResponse<>();

        try{
            CachedConfig config = mapper.getCachedConfigByName(dictname);

            // 可以考虑未配置字典数据方案
            if(config == null){
                response.isSuccess = false;
                response.message = "未配置字典缓存";
            }else{
                response.isSuccess = true;
            }
            response.data = config;
        }catch (SQLException | DataAccessException ex){
            logger.error(ex.getMessage());
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return response;
    }
}
