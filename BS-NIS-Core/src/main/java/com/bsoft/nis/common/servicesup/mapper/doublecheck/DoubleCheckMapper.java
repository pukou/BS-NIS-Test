package com.bsoft.nis.common.servicesup.mapper.doublecheck;

import com.bsoft.nis.domain.core.doublecheck.DoubleCheckType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Describtion:日志服务mapper
 * Created: dragon
 * Date： 2016/11/22.
 */
public interface DoubleCheckMapper {


    void addDoubleCheck(DoubleCheckType doubleCheckType,@Param(value = "dbtype") String dbtype);

    List<DoubleCheckType> getDoubleCheck(@Param(value = "HDBS")String hdbs1, @Param(value = "JGID")String jgid);
}
