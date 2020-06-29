package com.bsoft.nis.common.servicesup.support.log;

import com.bsoft.nis.common.service.log.LogOperSubType;
import com.bsoft.nis.common.service.log.LogOperType;
import com.bsoft.nis.common.servicesup.mapper.log.LogServiceMapper;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.core.log.db.OperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describtion:日志子服务
 * Created: dragon
 * Date： 2016/11/22.
 */
@Service
public class LogServiceSup extends RouteDataSourceService {
    @Autowired
    LogServiceMapper mapper;

    /**
     * 根据主键获取操作日志
     * @param jlbh
     * @return
     */
    public List<OperLog> getLogByPrimary(String jlbh)
            throws SQLException,DataAccessException{
        return mapper.getLogByPrimary(jlbh);
    }

    /**
     * 获取标本采集日志
     * @param zyh
     * @param jgid
     * @return
     */
    public List<OperLog> getSpecimenCollectLogs(String zyh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getSpecimenCollectLogs(zyh,jgid);
    }

    /**
     * 新增操作日志
     * @param log
     * @return
     */
    public Integer addLog(OperLog log)
            throws SQLException,DataAccessException{
        String dbtype = getCurrentDataSourceDBtype();
        if (log!=null) {
            log.dbtype = dbtype;
        }
        return mapper.addLog(log);
    }

    /**
     * 删除操作日志
     * @param jlbh
     * @return
     */
    public Integer deleteLog(String jlbh)
            throws SQLException,DataAccessException{
        return mapper.deleteLog(jlbh);
    }

    /**
     * 根据病人/业务编号/操作类别/操作类型批量删除
     * @param zyh
     * @param jgid
     * @param glbh
     * @param operType
     * @param czlxss
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public Integer deleteLog(String zyh, String jgid, String glbh, LogOperType operType, List<LogOperSubType> czlxss)
            throws SQLException,DataAccessException{
        Map map = new HashMap();
        map.put("ZYH",zyh);
        map.put("JGID",jgid);
        map.put("GLBH",glbh);
        map.put("CZLB",operType.get());
        List<String> czlxs = new ArrayList<>();
        for (LogOperSubType type:czlxss){
            czlxs.add(String.valueOf(type.get()));
        }
        map.put("CZLXS",czlxs);
        return mapper.deleteLogMuch(map);
    }
}
