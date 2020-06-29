package com.bsoft.nis.common.servicesup.mapper;

import com.bsoft.nis.domain.core.Dictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */
public interface DictionaryMapper {
    /**
     * 根据代码类别与代码识别号获取字典项
     *
     * @param dmlb
     * @param dmsb
     * @return
     */
    Dictionary getDic(@Param(value = "DMLB") String dmlb,
                      @Param(value = "DMSB") String dmsb);

    /**
     * 根据代码类别，获取字典列表
     *
     * @param dmlb
     * @return
     */
    List<Dictionary> getDicsByDmlb(@Param(value = "DMLB") String dmlb);

    /**
     * 根据代码类别，获取HIS字典列表(GY_DMZD)
     * @param dmlb
     * @return
     */
    List<Dictionary> getHisDictsByDmlb(String dmlb);

    /**
     * 根据代码类别，获取HIS字典列表(GY_DMZD)
     * @param dmlbs
     * @return
     */
    List<Dictionary> getHisDictsByMulDmlb(List<String> dmlbs);
}
