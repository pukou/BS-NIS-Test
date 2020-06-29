package com.bsoft.nis.common.servicesup.mapper;

import com.bsoft.nis.domain.core.SystemConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */
public interface ConfigServiceMapper {
    /**
     * 根据代码类别与代码识别号获取系统配制
     *
     * @param dmlb
     * @param dmsb
     * @return
     */
    SystemConfig getConfig(@Param(value = "DMLB") String dmlb,
                           @Param(value = "DMSB") String dmsb);

    /**
     * 根据代码类别，获取系统配制列表
     *
     * @param dmlb
     * @return
     */
    List<SystemConfig> getConfigsByDmlb(@Param(value = "DMLB") String dmlb);
}
