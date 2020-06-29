package com.bsoft.nis.common.service.log;

import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.servicesup.support.log.LogServiceSup;
import com.bsoft.nis.domain.core.log.db.OperLog;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Describtion:操作日志服务
 * Created: dragon
 * Date： 2016/11/22.
 */
@Service
public class LogService extends RouteDataSourceService{
    @Autowired
    LogServiceSup service;
    @Autowired
    IdentityService identityService;

    private Log logger = LogFactory.getLog(LogService.class);


    /**
     * 根据主键获取操作日志
     * @param jlbh
     * @return
     */
    public BizResponse<List<OperLog>> getLogByPrimary(String jlbh){
        BizResponse<List<OperLog>> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try{
            response.data = service.getLogByPrimary(jlbh);
            response.isSuccess = true;
            response.message = "获取数据成功!";
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[日志服务]数据库错误!";
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message= "[日志服务]内部错误!";
        }
        return response;
    }

    /**
     * 获取标准采集日志
     * @param zyh
     * @param jgid
     * @return
     */
    public BizResponse<List<OperLog>> getSpecimenCollectLogs(String zyh,String jgid){
        BizResponse<List<OperLog>> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try{
            response.data = service.getSpecimenCollectLogs(zyh,jgid);
            response.isSuccess = true;
            response.message = "获取数据成功!";
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[标本采集日志服务]数据库错误!";
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message= "[标本采集日志服务]内部错误!";
        }
        return response;
    }

    /**
     * 新增日志
     * @param log
     * @return
     */
    public BizResponse<String> addLog(OperLog log){
        BizResponse<String> response = new BizResponse<>();
        Long primary;
        try{
            primary = identityService.getIdentityMax("IENR_CZJL",DataSource.MOB);
            keepOrRoutingDateSource(DataSource.MOB);
            log.JLBH = primary;
            log.ZFBZ = 0;
            response.data = String.valueOf(service.addLog(log));
            response.isSuccess = true;
            response.message = "新增日志成功!";
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[日志保存]数据库错误!";
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message= "[日志服务]内部错误!";
        }
        return response;
    }

    /**
     * 删除日志
     * @return
     */
    public BizResponse<String> deleteLog(String jlbh){
        BizResponse<String> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try{
            response.data = String.valueOf(service.deleteLog(jlbh));
            response.isSuccess = true;
            response.message = "删除日志成功!";
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[日志删除]数据库错误!";
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message= "[日志删除]内部错误!";
        }
        return response;
    }

    /**
     * 根据病人/业务编号/操作类别/操作类型批量删除
     * @param zyh       住院号
     * @param jgid      机构ID
     * @param glbh      关联业务主键
     * @param operType  操作类别
     * @param czlxs     操作类型列表
     * @return
     */
    public BizResponse<String> deleteLog(String zyh,String jgid,String glbh,LogOperType operType,List<LogOperSubType> czlxs){
        BizResponse<String> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try{
            if (czlxs.size()<= 0){
                response.isSuccess = false;
                response.message = "操作类型列表不可为空!";
                return response;
            }
            response.data = String.valueOf(service.deleteLog(zyh, jgid, glbh, operType, czlxs));
            response.isSuccess = true;
            response.message = "删除日志成功!";
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[日志删除]数据库错误!";
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message= "[日志删除]内部错误!";
        }
        return response;
    }

}
