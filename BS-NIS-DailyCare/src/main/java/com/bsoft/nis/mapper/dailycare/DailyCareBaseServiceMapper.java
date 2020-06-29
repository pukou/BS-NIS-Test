package com.bsoft.nis.mapper.dailycare;

import com.bsoft.nis.domain.dailycare.DailySecondItem;
import com.bsoft.nis.domain.dailycare.DailyTopItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by king on 2016/10/28.
 */
public interface DailyCareBaseServiceMapper {

	List<DailyTopItem> getDailyNurseType(@Param(value = "KSDM") String ksdm,
			@Param(value = "JGID") String jgid);

	List<DailySecondItem> getDailyNurseList(@Param(value = "LBBS") String lbbs,
			@Param(value = "JGID") String jgid, @Param(value = "SYSTYPE") int sysType);

	Integer SaveDailyNurseItems(@Param(value = "JLBS") Long jlbs,
			@Param(value = "ZYH") String zyh, @Param(value = "XMBS") String xmbs,
			@Param(value = "ZFBZ") String zfbz, @Param(value = "ZXR") String zxr,
			@Param(value = "ZXSJ") String zxsj, @Param(value = "BRBQ") String brbq,
			@Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

}
