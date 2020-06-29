package com.bsoft.nis.common.service;

import com.bsoft.nis.common.servicesup.support.DicServiceSup;
import com.bsoft.nis.domain.core.Dictionary;
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
 * 系统字典服务
 * Created by Administrator on 2016/10/14.
 */
@Service
public class DictService extends RouteDataSourceService{
    private Log logger = LogFactory.getLog(DictService.class);

    @Autowired
    DicServiceSup dicService;

    /**
     * 在MOB库中，根据代码类别，获取字典列表。
     *
     * @param dmlb 代码类别
     * @return 成功业务对象赋值在BizResponse.datalist
     */
    public BizResponse<Dictionary> getDicsByDmlb(String dmlb) {

        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<Dictionary> result = new BizResponse<>();
        try {

            List<Dictionary> datalist = dicService.getDicsByDmlb(dmlb);
            result.isSuccess = true;
            result.datalist = datalist;
        } catch (RuntimeException | SQLException ex) {
            logger.error(ex.getMessage(), ex);
            result.message = ex.getMessage();
        }
        return result;

    }

    /**
     * 在MOB库中，根据代码类别与代码识别号获取字典项
     *
     * @param dmlb
     * @param dmsb
     * @return 成功业务对象赋值在BizResponse.data
     */
    public BizResponse<Dictionary> getDic(String dmlb, String dmsb) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<Dictionary> result = new BizResponse<>();
        try {
            Dictionary data = dicService.getDic(dmlb, dmsb);
            result.isSuccess = true;
            result.data = data;
        } catch (RuntimeException | SQLException ex) {
            logger.error(ex.getMessage(), ex);
            result.message = ex.getMessage();
        }
        return result;
    }
}
