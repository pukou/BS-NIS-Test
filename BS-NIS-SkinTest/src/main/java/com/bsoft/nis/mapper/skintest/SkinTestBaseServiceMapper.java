package com.bsoft.nis.mapper.skintest;


import com.bsoft.nis.domain.skintest.SickerPersonSkinTest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Classichu on 2017/11/16.
 * 中医护理
 */
public interface SkinTestBaseServiceMapper {

	//获取主要主要症状记录列表
	List<SickerPersonSkinTest> getAllSkinTest(@Param(value = "ZYH") String zyh,
                                           @Param(value = "BRBQ") String brbq, @Param(value = "JGID") String jgid);

	List<SickerPersonSkinTest> getNeedSkinTest(@Param(value = "ZYH") String zyh,
										   @Param(value = "BRBQ") String brbq, @Param(value = "JGID") String jgid);

    Integer updateSkinTest(SickerPersonSkinTest skinTest);

	List<List<?>> update_ZYBQYZ_PSJG(Map<String, Object> zxparms);

}
