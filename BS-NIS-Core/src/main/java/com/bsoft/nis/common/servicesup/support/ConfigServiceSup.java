package com.bsoft.nis.common.servicesup.support;

import com.bsoft.nis.common.servicesup.mapper.ConfigServiceMapper;
import com.bsoft.nis.domain.core.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
@Service
public class ConfigServiceSup {
    @Autowired
    ConfigServiceMapper dictionaryMapper;

    /**
     * 根据代码类别与代码识别号获取字典项
     *
     * @param dmlb
     * @param dmsb
     * @return
     */
    public SystemConfig getConfig(String dmlb, String dmsb)
            throws SQLException {

        return dictionaryMapper.getConfig(dmlb, dmsb);
    }

    /**
     * 根据代码类别，获取字典列表
     *
     * @param dmlb
     * @return
     */
    public List<SystemConfig> getConfigsByDmlb(String dmlb)
            throws SQLException {

        return dictionaryMapper.getConfigsByDmlb(dmlb);
    }
}
