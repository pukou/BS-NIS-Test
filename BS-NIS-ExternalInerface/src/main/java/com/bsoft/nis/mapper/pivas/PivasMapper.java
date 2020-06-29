package com.bsoft.nis.mapper.pivas;

import com.bsoft.nis.domain.pivas.PivasTransDetail;
import com.bsoft.nis.domain.pivas.PivasTransform;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface PivasMapper {

    Boolean addSYD(PivasTransform pivasTransform);

    Boolean addSYMX(PivasTransDetail pivasTransDetail);

    Integer getPIVAS(@Param(value = "TMBH") String tmbh);
}
