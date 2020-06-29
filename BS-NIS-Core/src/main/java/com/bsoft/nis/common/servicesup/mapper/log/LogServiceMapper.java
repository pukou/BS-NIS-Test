package com.bsoft.nis.common.servicesup.mapper.log;

import com.bsoft.nis.domain.core.log.db.OperLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Describtion:日志服务mapper
 * Created: dragon
 * Date： 2016/11/22.
 */
public interface LogServiceMapper {
    List<OperLog> getLogByPrimary(@Param(value = "JLBH") String jlbh);

    List<OperLog> getSpecimenCollectLogs(@Param(value = "ZYH") String zyh,@Param(value = "JGID") String jgid);

    Integer addLog(OperLog log);

    Integer deleteLog(@Param(value = "JLBH") String jlbh);

    Integer deleteLogMuch(Map map);
}
