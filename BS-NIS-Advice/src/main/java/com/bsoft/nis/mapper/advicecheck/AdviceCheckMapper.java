package com.bsoft.nis.mapper.advicecheck;

import com.bsoft.nis.domain.advicecheck.AdviceForm;
import com.bsoft.nis.domain.advicecheck.AdviceFormDetail;
import com.bsoft.nis.domain.advicecheck.CheckForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by king on 2016/11/28.
 */
public interface AdviceCheckMapper {

    List<AdviceForm> getDosingCheckList(@Param(value = "BRBQ") String brbq, @Param(value = "SYRQ") String syrq, @Param(value = "JYHDBZ") String jyhdbz,
                                        @Param(value = "BYHDBZ") String byhdbz, @Param(value = "GSLX") String gslx, @Param(value = "dbtype") String dbtype);

    List<AdviceForm> getPatientsByBQ(@Param(value = "BRBQ") String brbq, @Param(value = "JGID") String jgid);

    int updateCheckForm1(@Param(value = "HDGH") String hdgh, @Param(value = "HDSJ") String hdsj, @Param(value = "SYDH") String sydh,
                         @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    int updateCheckForm2(@Param(value = "HDGH") String hdgh, @Param(value = "HDSJ") String hdsj, @Param(value = "SYDH") String sydh,
                         @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    int updateCheckForm3(@Param(value = "HDGH") String hdgh, @Param(value = "HDSJ") String hdsj, @Param(value = "SYDH") String sydh,
                         @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    int updateCheckForm4(@Param(value = "HDGH") String hdgh, @Param(value = "HDSJ") String hdsj, @Param(value = "SYDH") String sydh,
                         @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    List<CheckForm> create(@Param(value = "TMBH") String tmbh, @Param(value = "GSLX") String gslx);

    List<AdviceFormDetail> getAdviceName(@Param(value = "JLXH") List<String> jlxh);

    List<AdviceFormDetail> getCheckDetail(@Param(value = "SYDH") String sydh, @Param(value = "GSLX") String gslx);

    List<CheckForm> createBySydh(@Param(value = "SYDH") String sydh, @Param(value = "GSLX") String gslx);


}
