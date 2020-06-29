package com.bsoft.nis.mapper.oralmedication;

import com.bsoft.nis.domain.oralmedication.OMSPackage;
import com.bsoft.nis.domain.oralmedication.OMSTablet;
import com.bsoft.nis.domain.oralmedication.OMSTitle;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface OralMedicationMapper {

    Boolean addOMSTablet(OMSTablet omsTablet);

    Boolean addOMSPackage(OMSPackage omsPackage);

    Boolean addOMSTitle(@Param("omsTitle") OMSTitle omsTitle);

    int getOMSCntByTmbh(@Param(value = "TMBH") String tmbh);

    int addXTJCJL(@Param("JLXH")long jlxh, @Param("ZYH") String zyh, @Param("BRBQ") String brbq, @Param("BRCH") String brch, @Param("CJSJ") String cjsj,@Param("CJGH") String cjgh, @Param("CLSD") String clsd, @Param("CLZ") String clz, @Param("PTHC") String xml, @Param("dbtype") String dbtype);
}
