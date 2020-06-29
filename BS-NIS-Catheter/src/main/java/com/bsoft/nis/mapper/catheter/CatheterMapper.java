package com.bsoft.nis.mapper.catheter;


import com.bsoft.nis.domain.catheter.CatheterMeasurData;
import com.bsoft.nis.domain.catheter.CatheterSpinnerData;
import com.bsoft.nis.domain.catheter.db.CatheterYLGJLvo;
import com.bsoft.nis.domain.lifesign.LifeSignRealSaveDataItem;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface CatheterMapper {

	List<SickPersonVo> getpationtList(@Param(value = "KSSJ") String kssj,
			@Param(value = "JSSJ") String jssj, @Param(value = "BRBQ") String brbq,
			@Param(value = "JGID") String jgid, @Param(value = "XMXHS") List<String> xmxhs,
			@Param(value = "XMLXS") List<String> xmlxs,
			@Param(value = "dbtype") String dbtype);

	List<CatheterMeasurData> getMeasurData(@Param(value = "BRBQ") String brbq,
			@Param(value = "JGID") String jgid, @Param(value = "ZYH") String zyh,
			@Param(value = "KSSJ") String kssj, @Param(value = "JSSJ") String jssj,
			@Param(value = "XMXHS") List<String> xmxhs,
			@Param(value = "XMLXS") List<String> xmlxs,
			@Param(value = "dbtype") String dbtype);

	List<CatheterYLGJLvo> getYLGJL(@Param(value = "BRBQ") String brbq,
			@Param(value = "JGID") String jgid, @Param(value = "ZYH") String zyh,
			@Param(value = "JLRQ") String jlrq, @Param(value = "dbtype") String dbtype);

	List<String> getDZXM();

	Integer saveCatheter(CatheterYLGJLvo catheterYLGJLvo);

	Boolean cancelCatheter(@Param(value = "JLXH") String jlxh,
			@Param(value = "JGID") String jgid);

	List<CatheterSpinnerData> getSpinnerData(@Param(value = "YPXH") String ypxh);

	Integer saveLifeSign(@Param(value = "item") LifeSignRealSaveDataItem item,
			@Param(value = "dbtype") String dbtype);

	String getLifeSignTwdxsByXmh(@Param(value = "XMH") String tzxm);

	Integer deleteSmtzFromCatheter(@Param(value = "CJH") String cjh,
			@Param(value = "JGID") String jgid);

	String getTzcjhByJlxh(@Param(value = "JLXH") String jlxh,
			@Param(value = "JGID") String jgid);

	List<String> getXmlxByXmxh(@Param(value = "XMXHS") List<String> xmxhs);
}
