package com.bsoft.nis.common.service;

import com.bsoft.nis.common.servicesup.support.SystemParamServiceSup;
import com.bsoft.nis.domain.core.UserParams;
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
 * 系统参数服务
 * Created by Administrator on 2016/10/14.
 */
@Service
public class SystemParamService extends RouteDataSourceService{

    @Autowired
    SystemParamServiceSup service;

    private Log logger = LogFactory.getLog(SystemParamService.class);

    /**
     * 获取用户参数
     *
     * @param gsjb       归属级别,非空
     * @param gsdx       归属对象
     * @param csmc       参数名称，非空
     * @param jgid       机构ID，非空
     * @param dataSource 当前支持HRP 和 MOB库 ,非空
     * @return 成功BizResponse.datalist 存放参数情况（CSQK）结果集
     */
    public BizResponse<String> getUserParams(String gsjb, String gsdx, String csmc, String jgid, DataSource dataSource) {

        BizResponse<String> result = new BizResponse<>();
        try {
            keepOrRoutingDateSource(dataSource);
            List<String> datalist = null;
            switch (dataSource) {
                case HRP:
                    datalist = service.getParamsInHRP(gsjb, gsdx, csmc, jgid);
                    break;
                case MOB:
                    datalist = service.getParamsInMOB(gsjb, gsdx, csmc, jgid);
                    break;
            }
            result.isSuccess = true;
            result.datalist = datalist;
        } catch (RuntimeException | SQLException ex) {
            logger.error(ex.getMessage(), ex);
            result.message = ex.getMessage();
        }
        return result;
    }


    /**
     * 获取用户参数
     *
     * @param gsjb       归属级别,非空
     * @param gsdx       归属对象
     * @param csmcs       参数名称列表，非空
     * @param jgid       机构ID，非空
     * @param dataSource 当前支持HRP 和 MOB库 ,非空
     * @return 成功 {@link BizResponse#datalist} 存放 {@link UserParams}结果集
     */
    public BizResponse<UserParams> getUserParamsList(String gsjb, String gsdx, String[] csmcs, String jgid, DataSource dataSource) {

        BizResponse<UserParams> result = new BizResponse<>();
        try {
            keepOrRoutingDateSource(dataSource);
            List<UserParams> datalist = null;
            switch (dataSource) {
                case HRP:
                    datalist = service.getParamsListInHRP(gsjb, gsdx, csmcs, jgid);
                    break;
                case MOB:
                    datalist = service.getParamsListInMOB(gsjb, gsdx, csmcs, jgid);
                    break;
            }
            result.isSuccess = true;
            result.datalist = datalist;
        } catch (RuntimeException | SQLException ex) {
            logger.error(ex.getMessage(), ex);
            result.message = ex.getMessage();
        }
        return result;
    }
}
