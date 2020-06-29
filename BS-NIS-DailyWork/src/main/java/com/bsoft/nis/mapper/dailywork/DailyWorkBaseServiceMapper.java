package com.bsoft.nis.mapper.dailywork;

import com.bsoft.nis.domain.dailywork.DailyWork;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by king on 2016/11/17.
 */
public interface DailyWorkBaseServiceMapper {

	List<DailyWork> getPlan(@Param(value = "ZYH") List<String> data,
			@Param(value = "JHRQ") String jhrq, @Param(value = "JGID") String jgid,
			@Param(value = "dbtype") String dbtype);

	List<DailyWork> getPatientsByBq(@Param(value = "BRBQ") String brbq,
			@Param(value = "JGID") String jgid);

	List<DailyWork> getChangeAdvice(@Param(value = "ZYH") List<String> data,
			@Param(value = "KZSJ") String kzsj, @Param(value = "JGID") String jgid,
			@Param(value = "dbtype") String dbtype);

	List<DailyWork> getInspection(@Param(value = "zyhmList") List<String> zyhmList,@Param(value = "RYRQ") String ryrq,
			@Param(value = "JGID") String jgid,@Param(value = "dbtype") String dbtype);

	List<DailyWork> getRisk(@Param(value = "ZYH") List<String> data,
			@Param(value = "TXRQ") String jhrq, @Param(value = "JGID") String jgid,
			@Param(value = "dbtype") String dbtype);
}
