package com.bsoft.nis.common.service;

import com.bsoft.nis.core.config.ConfigHandler;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.common.servicesup.support.IdentityServiceSup;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 主键获取服务(种子表)
 * Created by Administrator on 2016/10/14.
 */
@Service
public class IdentityService extends RouteDataSourceService{

    private Log logger = LogFactory.getLog(IdentityService.class);

    @Autowired
    IdentityServiceSup service;

    public BizResponse<Map> getIdentityList(){
        BizResponse<Map> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);

        try{
            String orgStr = ConfigHandler.getSystemConfig("organization");
            boolean isMulOrg;
            if (StringUtils.isBlank(orgStr)){
                isMulOrg = false;
            }else{
                isMulOrg = orgStr.equals("1");
            }
            // 注册事务
            if (isMulOrg){
                keepOrRoutingDateSource(DataSource.PORTAL);
            }else{
                keepOrRoutingDateSource(DataSource.MOB);
            }
             List<Map> list = service.getIdentityList();
             response.datalist = list;
            response.isSuccess = true;
            response.message = "";
        }catch (SQLException | DataAccessException ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }

        return response;
    }

    /**
     * 根据表名，获取单个长整型的主键值
     * @param tableName
     * @return
     */
    public Long getIdentityMax(String tableName){
        int count = 1;
        Long index = 0L;
        BizResponse<Long> response ;
        response = getIdentityMax(tableName,count);
        if (response.isSuccess){
            index = response.datalist.get(0);
        }else{
            index = 0L;
        }
        return index;
    }

    public Long getIdentityMax(String tableName,DataSource dataSource){
        int count = 1;
        Long index = 0L;
        BizResponse<Long> response ;
        response = getIdentityMax(tableName,count,dataSource);
        if (response.isSuccess){
            index = response.datalist.get(0);
        }else{
            index = 0L;
        }
        return index;
    }

    /**
     * 根据表名，获取主键为整型的主键值
     * @param tableName 表名
     * @param count     主键个数
     * @return
     */
    public BizResponse<Long> getIdentityMax(String tableName,int count){
        BizResponse<Long> response = new BizResponse<>();
        List<Long> datalist = new ArrayList<Long>();
        String table = "GY_IDENTITY_"
                + tableName.substring(0, tableName.indexOf("_"));
        if (count < 1 || StringUtils.isEmpty(tableName)){
            response.isSuccess = false;
            response.message = "种子表获取量必须大于零且表名称不可为空!";
            return response;
        }

        String orgStr = ConfigHandler.getSystemConfig("organization");
        boolean isMulOrg;
        if (StringUtils.isBlank(orgStr)){
            isMulOrg = false;
        }else{
            isMulOrg = orgStr.equals("1");
        }
        // 注册事务
        if (isMulOrg){
            keepOrRoutingDateSource(DataSource.PORTAL);
        }else{
            keepOrRoutingDateSource(DataSource.MOB);
        }

        try{
            List<Map> list = service.getCurList(table,tableName);

            if(list.size() <=0){
                // 插入一条记录
                service.insertItem(table,tableName,count,1,1);
                for (int i = 1; i<= count;i++){
                    datalist.add((long)i);
                }
            }else{
                service.updateCurValue(table, tableName, count);
                Map<String, Object> result = service.getCurValue(table, tableName);
                long curValue = ((BigDecimal) result.get("DQZ")).longValue();
                Object dzzValue = result.get("DZZ");
                int increValue = 0;
                if (dzzValue instanceof BigDecimal) {
                    increValue = ((BigDecimal) dzzValue).intValue();
                } else if (dzzValue instanceof Integer) {
                    increValue = (Integer) dzzValue;
                } else if(dzzValue instanceof Float){   //DZZ为Float时处理
                    increValue = (int)(float) dzzValue;
                } else if(dzzValue instanceof Double){  //DZZ为Double时处理
                    increValue = (int)(double) dzzValue;
                } else {
                    response.isSuccess = false;
                    response.message = "获取当前值类型失败";
                    return response;
                }
                for (int i = 0; i < count; i++) {
                    datalist.add(curValue - increValue * i);
                }
            }
        }catch (SQLException | DataAccessException ex){
            logger.error(ex.getMessage());
            response.isSuccess = false;
            response.message = ex.getMessage();
        }catch (Exception ex){
            logger.error(ex.getMessage());
            response.isSuccess = false;
            response.message = ex.getMessage();
        }
        response.isSuccess = true;
        //add 2018-4-18 16:13:29
        // 翻转顺序  兼容多条主键的时候，业务取list处理，也兼容部分取 get(0) 获取第一条主键去累加的问题
        Collections.reverse(datalist);
        response.datalist = datalist;
        return response;
    }

    public BizResponse<Long> getIdentityMax(String tableName,int count,DataSource dataSource){
        BizResponse<Long> response = new BizResponse<>();
        List<Long> datalist = new ArrayList<Long>();
        String table = "GY_IDENTITY_"
                + tableName.substring(0, tableName.indexOf("_"));
        if (count < 1 || StringUtils.isEmpty(tableName)){
            response.isSuccess = false;
            response.message = "种子表获取量必须大于零且表名称不可为空!";
            return response;
        }

        String orgStr = ConfigHandler.getSystemConfig("organization");
        boolean isMulOrg;
        if (StringUtils.isBlank(orgStr)){
            isMulOrg = false;
        }else{
            isMulOrg = orgStr.equals("1");
        }
        // 注册事务
        if (isMulOrg){
            keepOrRoutingDateSource(DataSource.PORTAL);
        }else{
            if (dataSource == null){
                keepOrRoutingDateSource(DataSource.MOB);
            }else{
                keepOrRoutingDateSource(dataSource);
            }

        }

        try{
            List<Map> list = service.getCurList(table,tableName);

            if(list.size() <=0){
                // 插入一条记录
                service.insertItem(table,tableName,count,1,1);
                for (int i = 1; i<= count;i++){
                    datalist.add((long)i);
                }
            }else{
                service.updateCurValue(table, tableName, count);
                Map<String, Object> result = service.getCurValue(table, tableName);
                long curValue = ((BigDecimal) result.get("DQZ")).longValue();
                Object dzzValue = result.get("DZZ");
                int increValue = 0;
                if (dzzValue instanceof BigDecimal) {
                    increValue = ((BigDecimal) dzzValue).intValue();
                } else if (dzzValue instanceof Integer) {
                    increValue = (Integer) dzzValue;
                } else if(dzzValue instanceof Float){   //DZZ为Float时处理
                    increValue = (int)(float) dzzValue;
                } else if(dzzValue instanceof Double){  //DZZ为Double时处理
                    increValue = (int)(double) dzzValue;
                } else{
                    response.isSuccess = false;
                    response.message = "获取当前值类型失败";
                    return response;
                }
                for (int i = 0; i < count; i++) {
                    datalist.add(curValue - increValue * i);
                }
            }
        }catch (SQLException | DataAccessException ex){
            logger.error(ex.getMessage());
            response.isSuccess = false;
            response.message = ex.getMessage();
        }catch (Exception ex){
            logger.error(ex.getMessage());
            response.isSuccess = false;
            response.message = ex.getMessage();
        }
        response.isSuccess = true;
        //add 2018-4-18 16:13:29
        // 翻转顺序  兼容多条主键的时候，业务取list处理，也兼容部分取 get(0) 获取第一条主键去累加的问题
        Collections.reverse(datalist);
        response.datalist = datalist;
        return response;
    }
}
