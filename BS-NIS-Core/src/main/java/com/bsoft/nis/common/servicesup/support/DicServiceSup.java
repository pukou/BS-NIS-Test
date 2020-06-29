package com.bsoft.nis.common.servicesup.support;

import com.bsoft.nis.common.servicesup.mapper.DictionaryMapper;
import com.bsoft.nis.domain.core.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
@Service
public class DicServiceSup {
    @Autowired
    DictionaryMapper dictionaryMapper;

    /**
     * 根据代码类别与代码识别号获取字典项
     *
     * @param dmlb
     * @param dmsb
     * @return
     */
    public Dictionary getDic(String dmlb, String dmsb) throws SQLException {

        return dictionaryMapper.getDic(dmlb, dmsb);
    }

    /**
     * 根据代码类别，获取字典列表
     *
     * @param dmlb
     * @return
     */
    public List<Dictionary> getDicsByDmlb(String dmlb) throws SQLException {

        return dictionaryMapper.getDicsByDmlb(dmlb);
    }

    /**
     * 根据代码类别，获取HIS字典列表(GY_DMZD)
     * @param dmlb
     * @return
     * @throws SQLException
     */
    public List<Dictionary> getHisDictsByDmlb(String dmlb) throws SQLException{
        return dictionaryMapper.getHisDictsByDmlb(dmlb);
    }

    /**
     * 根据代码类别，获取HIS字典列表(GY_DMZD)
     * @param dmlbs
     * @return
     * @throws SQLException
     */
    public List<Dictionary> getHisDictsByMulDmlb(List<String> dmlbs) throws SQLException{
        return dictionaryMapper.getHisDictsByMulDmlb(dmlbs);
    }
}
