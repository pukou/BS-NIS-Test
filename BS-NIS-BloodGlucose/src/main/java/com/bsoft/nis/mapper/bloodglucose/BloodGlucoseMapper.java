package com.bsoft.nis.mapper.bloodglucose;

import com.bsoft.nis.domain.bloodglucose.BloodGlucoseDetail;
import com.bsoft.nis.domain.bloodglucose.db.BGDetailVo;
import com.bsoft.nis.domain.bloodglucose.db.BGRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 血糖治疗记录Mapper
 * User: 苏泽雄
 * Date: 16/12/23
 * Time: 14:03:50
 */
public interface BloodGlucoseMapper {

	List<BloodGlucoseDetail> getBloodGlucoseDetail(@Param(value = "ZYH") String zyh,
			@Param(value = "JHRQ") String jhrq, @Param(value = "BRBQ") String brbq,
			@Param(value = "XMLX") String xmlx, @Param(value = "XMXH") String xmxh,
			@Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

	Integer addBloodGlucose(BGDetailVo detail);

	Integer updateBloodGlucose(BGDetailVo detail);

	Integer checkInsulin(@Param(value = "MXXH") String mxxh,
			@Param(value = "JLGH") String jlgh, @Param(value = "JLSJ") String jlsj,
			@Param(value = "dbtype") String dbtype);

	List<BloodGlucoseDetail> getBGHistoryByJlxh(@Param(value = "ZYH") String zyh,
			@Param(value = "JHRQ") String jhrq, @Param(value = "BRBQ") String brbq,
			@Param(value = "XMLX") String xmlx, @Param(value = "JGID") String jgid,
			@Param(value = "dbtype") String dbtype);

	List<BloodGlucoseDetail> getBGHistoryList(@Param(value = "ZYH") String zyh,
			@Param(value = "KSSJ") String kssj, @Param(value = "JSSJ") String jssj,
			@Param(value = "BRBQ") String brbq, @Param(value = "JGID") String jgid,
			@Param(value = "dbtype") String dbtype);

	String getFirstJlxhOfBG(@Param(value = "ZYH") String zyh,
			@Param(value = "JHRQ") String jhrq, @Param(value = "BRBQ") String brbq,
			@Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

	Integer addBGRecord(BGRecordVo record);

	List<String> getMxxhListByJhrq(@Param(value = "ZYH") String zyh,
			@Param(value = "JHRQ") String jhrq, @Param(value = "XMLX") String xmlx,
			@Param(value = "XMXH") String xmxh, @Param(value = "JGID") String jgid,
			@Param(value = "dbtype") String dbtype);

	Integer deleteDetail(@Param(value = "MXXH") String mxxh);
}
