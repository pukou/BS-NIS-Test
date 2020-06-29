package com.bsoft.nis.mapper.traditional;

import com.bsoft.nis.domain.traditional.JSJL;
import com.bsoft.nis.domain.traditional.Traditional_HLFA;
import com.bsoft.nis.domain.traditional.Traditional_SHJS;
import com.bsoft.nis.domain.traditional.Traditional_XGBZ;
import com.bsoft.nis.domain.traditional.Traditional_ZYZZ;
import com.bsoft.nis.domain.traditional.Traditional_ZZFJ;
import com.bsoft.nis.domain.traditional.Traditional_ZZFJJL;
import com.bsoft.nis.domain.traditional.Traditional_ZZJL;
import com.bsoft.nis.domain.traditional._HLJS;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Classichu on 2017/11/16.
 * 中医护理
 */
public interface TraditionalNursingBaseServiceMapper {

	//获取主要主要症状记录列表
	List<Traditional_ZZJL> getZY_ZZJL(@Param(value = "ZYH") String zyh,
									  @Param(value = "BRBQ") String brbq, @Param(value = "JGID") String jgid);
	//获取症状分级列表
	List<Traditional_ZZFJ> getZY_ZZFJ(@Param(value = "ZZBH") String zzbh);
	//获取技术记录列表
	List<JSJL> getZY_JSJL(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid);

	//获取方案列表
	List<Traditional_HLFA> getZY_HLFA(@Param(value = "JGID") String jgid);
	//获取主要症状列表
	List<Traditional_ZYZZ> getZY_ZYZZ(@Param(value = "FABH") String fabh);
	//获取技术列表
	List<Traditional_SHJS> getZY_SHJS(@Param(value = "ZZBH") String zzbh);


	List<_HLJS> getZY_YZJHbyZZBH(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid, @Param(value = "ZZBH") String zzbh, @Param(value = "JHRQ") String jhrq);

	List<String> getZY_RYZD(@Param(value = "ZYH") String zyh);

	Integer insertZY_ZZFJJL(Traditional_ZZFJJL traditional_zzfjjl);

	List<Traditional_XGBZ> getZY_XGBZ(@Param(value = "ZZBH") String zzbh,@Param(value = "FSCZ") String fscz);

	Integer insertZY_JSJL(JSJL jsjl);


	Integer updateZY_ZZJL_PF(@Param(value = "SSHJL") String sshjl,@Param(value = "SSHFJ") String sshfj,@Param(value = "SSHPF") String sshpf,
	@Param(value = "SSHSJ") String sshsj,@Param(value = "ZZBH") String zzbh,@Param(value = "FAJL") String fajl,@Param(value = "HLXG") String hlxg);

}
