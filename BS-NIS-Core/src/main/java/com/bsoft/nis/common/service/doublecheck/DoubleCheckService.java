package com.bsoft.nis.common.service.doublecheck;

import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.servicesup.support.doublecheck.DoubleCheckServiceSup;
import com.bsoft.nis.domain.core.doublecheck.DoubleCheckType;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * Describtion:操作日志服务
 * Created: dragon
 * Date： 2016/11/22.
 */
@Service
public class DoubleCheckService extends RouteDataSourceService {
    @Autowired
    DoubleCheckServiceSup service;
    @Autowired
    IdentityService identityService;

    private Log logger = LogFactory.getLog(DoubleCheckService.class);

    /**
     * 插入双人核对记录
     *
     * @param doubleCheckType
     * @return
     */
    public BizResponse<String> addDoubleCheck(DoubleCheckType doubleCheckType) {
        BizResponse<String> bizResponse = new BizResponse<>();
        try {
            doubleCheckType.setJLXH(identityService.getIdentityMax("IENR_SRHDJL", 1,DataSource.MOB).datalist.get(0));
            service.addDoubleCheck(doubleCheckType);
            bizResponse.isSuccess = true;
        } catch (SQLException e) {
            e.printStackTrace();
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;
    }


    /**
     * 根据HDBS1 值 获取核对记录
     *
     * @param HDBS1
     * @param jgid
     * @return
     */
    public BizResponse<DoubleCheckType> getDoubleCheck(String HDBS1, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DoubleCheckType> bizResponse = new BizResponse<>();
        try {
            bizResponse.datalist = service.getDoubleCheck(HDBS1, jgid);
            bizResponse.isSuccess = true;
        } catch (SQLException e) {
            e.printStackTrace();
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;
    }

}
