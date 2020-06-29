package com.bsoft.nis.mapper.outcontrol;

import com.bsoft.nis.domain.outcontrol.OutControl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by king on 2016/11/16.
 */
public interface OutControlBaseServiceMapper {

	List<OutControl> getOutPatientByZyh(@Param(value = "ZYH") String zyh,
			@Param(value = "BRBQ") String brbq, @Param(value = "JGID") String jgid);

	List<OutControl> getPatientStatus(@Param(value = "ZYH") String zyh,
			@Param(value = "BRBQ") String brbq, @Param(value = "JGID") String jgid);
	/*升级编号【56010038】============================================= start
                    外出管理PDA上只有登记功能，查询需要找到具体的人再查询，不太方便，最好能有一个查询整个病区外出病人的列表
                ================= classichu 2018/3/7 19:49
                */
	List<OutControl> getAllOurPatients(@Param(value = "BRBQ") String brbq, @Param(value = "JGID") String jgid);
/* =============================================================== end */

	Integer registerOutPatient(@Param(value = "JLXH") Long jlxh,
			@Param(value = "WCDJSJ") String wcdjsj, @Param(value = "WCSJ") String wcsj,
			@Param(value = "WCDJHS") String wcdjhs, @Param(value = "YJHCSJ") String yjhcsj,
			@Param(value = "PZYS") String pzys, @Param(value = "PTRY") int ptry,
			@Param(value = "ZYH") String zyh, @Param(value = "BRBQ") String brbq,
			@Param(value = "JGID") String jgid, @Param(value = "WCYY") String wcyy,
			@Param(value = "dbtype") String dbtype);

	Integer registerBackToBed(@Param(value = "JLXH") Long jlxh,
			@Param(value = "HCDJHS") String hcdjhs, @Param(value = "HCSJ") String hcsj,
			@Param(value = "HCDJSJ") String hcdjsj, @Param(value = "JGID") String jgid,
			@Param(value = "dbtype") String dbtype);
}
