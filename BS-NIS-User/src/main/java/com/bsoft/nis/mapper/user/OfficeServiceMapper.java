package com.bsoft.nis.mapper.user;

import com.bsoft.nis.domain.office.AreaVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public interface OfficeServiceMapper {
    List<AreaVo> getOfficesByYGDM(@Param(value = "YGDM") String ygdm, @Param(value = "JGID") String jgid);

    List<AreaVo> getAreaVoForSurgery(@Param(value = "JGID") String jgid);
}
