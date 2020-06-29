package com.bsoft.nis.mapper.visit;

import com.bsoft.nis.domain.visit.CheckState;
import com.bsoft.nis.domain.visit.VisitHistory;
import com.bsoft.nis.domain.visit.VisitPerson;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by king on 2016/11/18.
 */
public interface VisitBaseServiceMapper {

    /*升级编号【56010027】============================================= start
                             处理房间条码、获取房间病人处理
                ================= classichu 2018/3/22 10:41
                */
    List<String> getRoomPatientList(@Param(value = "FJHM") String fjhm, @Param(value = "JGID") String jgid);
    /* =============================================================== end */

    int updatePatrol(@Param("XSBS") String xsbs, @Param("ZFGH") String zfgh, @Param("ZFSJ") Date zfsj);

    List<VisitHistory> getPatrolHistory(@Param(value = "ZYH") String zyh,@Param(value = "XSSJ") String xssj,@Param(value = "JGID") String jgid,@Param(value ="dbtype") String dbtype);
    List<VisitPerson> getPatrol(@Param(value = "KSDM") String ksdm,@Param(value = "JGID") String jgid);
    List<String> getPatrolZyh(@Param(value = "XSGH") String xsgh,@Param(value = "XSSJ") String xssj,@Param(value = "JGID") String jgid,@Param(value = "ZYH") List<String> zyh,@Param(value ="dbtype") String dbtype);
    List<String> getPatrolPatitent(@Param(value = "ZYH") String zyh,@Param(value = "JGID") String jgid,@Param(value = "STARTXSSJ") String startxssj,@Param(value = "ENDXSSJ") String endxssj,@Param(value ="dbtype") String dbtype);
    List<CheckState> getPatrolTypeInfo(@Param(value = "JGID") String jgid);
    List<VisitPerson> getPatrol2(@Param(value = "ZYH") String zyh,@Param(value = "JGID") String jgid);
    List<VisitPerson> getPatrolDetail(@Param(value = "XSBS") Long xsbs,@Param(value = "ZYH") String zyh,@Param(value = "JGID") String jgid);
    Boolean savePatrol(@Param(value = "XSBS") Long xsbs,@Param(value = "ZYH") String zyh,@Param(value = "XSGH") String xsgh,@Param(value = "XSSJ") String xssj,
                   @Param(value = "JLSJ") String jlsj,@Param(value = "XSQK") String xsqk,@Param(value = "SMBZ") String smbz,@Param(value = "BRBQ") String brbq,@Param(value = "JGID") String jgid,@Param(value ="dbtype") String dbtype);
}
