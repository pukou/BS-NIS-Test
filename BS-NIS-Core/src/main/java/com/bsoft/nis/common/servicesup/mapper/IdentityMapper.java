package com.bsoft.nis.common.servicesup.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface IdentityMapper {

    List<Map> getIdentityList();

    /**
     * 返回新增的行数
     *
     * @param
     * @return
     */
    int insertItem(@Param("TABLE") String table, @Param("BMC") String bmc,
                   @Param("DQZ") long dqz, @Param("CSZ") int count,
                   @Param("DZZ") int dzz);

    /**
     * 返回更新的行数
     *
     * @param bmc
     *            表名称
     * @param count
     *            新增的总值
     * @return
     */
    int updateCurValue(@Param("TABLE") String table, @Param("BMC") String bmc,
                       @Param("count") int count);

    /**
     * 获取当前值
     *
     * @param bmc
     *            表名称
     * @return
     */
    Map<String, Object> getCurValue(@Param("TABLE") String table,
                                    @Param("BMC") String bmc);

    List<Map> getCurList(@Param("TABLE") String table,
                         @Param("BMC") String bmc);
}
