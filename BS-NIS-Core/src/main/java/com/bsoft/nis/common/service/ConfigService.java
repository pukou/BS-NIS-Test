package com.bsoft.nis.common.service;

import com.bsoft.nis.common.servicesup.support.ConfigServiceSup;
import com.bsoft.nis.domain.core.SystemConfig;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
/**
 * 系统配置服务
 * Created by Administrator on 2016/10/14.
 */
@Service
public class ConfigService extends RouteDataSourceService{

    private Log logger = LogFactory.getLog(ConfigService.class);

    @Autowired
    ConfigServiceSup dicService;

    /**
     * 在MOB库，根据代码类别，获取配制列表
     *
     * @param dmlb 代码类别
     * @return 成功业务对象赋值在BizResponse.datalist
     */
    public BizResponse<SystemConfig> getConfigsByDmlb(String dmlb) {

        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<SystemConfig> result = new BizResponse<>();
        try {
            List<SystemConfig> datalist = dicService.getConfigsByDmlb(dmlb);
            result.isSuccess = true;
            result.datalist = datalist;
        } catch (RuntimeException | SQLException ex) {
            logger.error(ex.getMessage(), ex);
            result.message = ex.getMessage();
        }
        return result;

    }

    /**
     * 在MOB库，根据代码类别与代码识别号获取配制项
     *
     * @param dmlb 代码类别
     * @param dmsb 代码识别
     * @return 成功业务对象赋值在BizResponse.data
     */
    public BizResponse<SystemConfig> getConfig(String dmlb, String dmsb) {

        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<SystemConfig> result = new BizResponse<>();
        try {
            SystemConfig data = dicService.getConfig(dmlb, dmsb);
            result.isSuccess = true;
            result.data = data;
        } catch (RuntimeException | SQLException ex) {
            logger.error(ex.getMessage(), ex);
            result.message = ex.getMessage();
        }
        return result;

    }
}
