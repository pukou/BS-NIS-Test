package com.bsoft.nis.service;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.office.AreaVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.user.support.OfficeServiceSup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * 科室服务
 * Created by Administrator on 2016/10/17.
 */
@Service
public class OfficeService extends RouteDataSourceService {
    private Log logger = LogFactory.getLog(OfficeService.class);

    @Autowired
    OfficeServiceSup service;

    public BizResponse<AreaVo> getOfficesByYGDM(String ygdm, String jgid) throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        BizResponse<AreaVo> response = new BizResponse<>();

        try {
            response.datalist = service.getOfficesByYGDM(ygdm, jgid);
            response.isSuccess = true;
            response.message = "科室获取成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "【获取科室】数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "【获取科室】服务内部错误";
        }
        return response;
    }
    public BizResponse<AreaVo> getSurgeryOffices(String jgid) throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        BizResponse<AreaVo> response = new BizResponse<>();
        try {
            response.datalist = service.getAreaVoForSurgery(jgid);//KSDM 存放 SSKS
            for (AreaVo areaVo:
            response.datalist) {
                areaVo.YGDM="[isSurgery]";//标记 代表手术科室
            }
            response.isSuccess = true;
            response.message = "手术科室获取成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "【获取手术科室】数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "【获取手术科室】服务内部错误";
        }
        return response;
    }
    /**
     * 获取手术科室列表列表
     *
     * @param jgid
     * @return
     */
    public BizResponse<AreaVo> getAreaVoForSurgery(String jgid) {
        keepOrRoutingDateSource(DataSource.HRP);
        BizResponse<AreaVo> response = new BizResponse<>();
        try {
            response.datalist = service.getAreaVoForSurgery(jgid);
            response.isSuccess = true;
            response.message = "科室获取成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "【获取科室】数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "【获取科室】服务内部错误!";
        }
        return response;
    }
}
