package com.bsoft.nis.common.servicesup.mapper;

import com.bsoft.nis.domain.core.UserParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */
public interface SystemParamsMapper {

    /**
     * 在HRP库中，获取用户参数信息
     *
     * @param gsjb
     * @param gsdx
     * @param csmc
     * @param jgid
     * @return CSQK 结果集
     */
    List<String> getParamsInHRP(@Param("GSJB") String gsjb, @Param("GSDX") String gsdx, @Param("CSMC") String csmc, @Param("JGID") String jgid);

    /**
     * 在MOB库中，获取用户参数信息
     *
     * @param gsjb
     * @param gsdx
     * @param csmc
     * @param jgid
     * @return  CSQK 结果集
     */
    List<String> getParamsInMOB(@Param("GSJB") String gsjb, @Param("GSDX") String gsdx, @Param("CSMC") String csmc, @Param("JGID") String jgid);


    List<UserParams> getParamsListInHRP(@Param("GSJB") String gsjb, @Param("GSDX") String gsdx, @Param("array") String[] csmcs, @Param("JGID") String jgid);

    List<UserParams> getParamsListInMOB(@Param("GSJB") String gsjb, @Param("GSDX") String gsdx, @Param("array") String[] csmcs, @Param("JGID") String jgid);
}
