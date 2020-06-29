package com.bsoft.nis.mapper.lifesign;

import com.bsoft.nis.domain.clinicalevent.ClinicalEventInfo;
import com.bsoft.nis.domain.lifesign.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Description: 生命体征控制器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
public interface LifeSignMapper {

    List<LifeSignTypeItem> getLifeSignTypeItemList(@Param(value = "BQID") String bqid, @Param(value = "JGID") String jgid);

    List<LifeSignInputItem> getLifeSignInputItemList(@Param(value = "LBH") String lbh);

    List<LifeSignInputItem> getAllLifeSignInputItemList();

    LifeSignInputItem getLifeSignInputItem(@Param(value = "SRXH") String lbh);

    List<LifeSignControlItem> getLifeSignControlItemList(@Param(value = "SRXH") String srxh);

    List<LifeSignOptionItem> getLifeSignOptionItemList(@Param(value = "KJH") String kjh);

    List<LifeSignOptionItem> getAllLifeSignOptionItemList();

    List<LifeSignControlItem> getAllLifeSignControlItemList();

    List<LifeSignOptionItem> getLifeSignSpecialOptionItemList(@Param(value = "KJH") String kjh);

    String getLifeSignKjlxByTsbs(@Param(value = "TSBS") String tsbs);

    List<LifeSignQualityInfo> getQualityInfoList();

    Integer deleteSmtzByCjzh(@Param("CJZH") String cjzh);

    String getLifeSignLbbzByTsbs(@Param(value = "TSBS") String tsbs);

    String getLifeSignTwdxsByXmh(@Param(value = "XMH") String xmh);

    int addLifeSignDataBatch(@Param(value = "list") List<LifeSignRealSaveDataItem> lifeSignRealSaveDataItemList, @Param(value = "dbtype") String dbtype);

    List<LifeSignHistoryDataItem> getLifeSignHistoryDataItem(@Param(value = "ZYH") String zyh, @Param(value = "START") String start,
                                                             @Param(value = "END") String end, @Param(value = "dbtype") String dbtype);

    List<LifeSignHistoryDataType> getLifeSignHistoryDataType();

    int deleteLifeSignHistoryData(@Param(value = "CJH") String cjh);

    int updateLifeSignHistoryData(@Param(value = "CJH") String cjh,@Param(value = "VAL") String value);


	Date getLifeSignDoubleCheckMaxCjsj(@Param(value = "TZXM") String tzxm, @Param(value = "ZYH") String zyh);

    List<LifeSignDoubleCheckHistoryDataItem> getLifeSignDoubleCheckHistoryDataItem(@Param(value = "TZXM") String tzxm, @Param(value = "ZYH") String zyh,
                                                                                   @Param(value = "START") String start, @Param(value = "END") String end,
                                                                                   @Param(value = "dbtype") String dbtype);

    List<LifeSignDoubleCheckCoolingMeasure> getLifeSignDoubleCheckCoolingMeasure();

    List<LifeSignDoubleCheckHistoryDataItem> getLifeSignSpecialDataItem(@Param(value = "ZYH") String zyh, @Param(value = "START") String start,
                                                                        @Param(value = "END") String end, @Param(value = "dbtype") String dbtype);

    List<ClinicalEventInfo> getClinicalEventInfoList(@Param(value = "ZYH") String zyh, @Param(value = "SJFL") String sjfl);

    int addClinicalEventInfo(ClinicalEventInfo clinicalEventInfo);

    int editClinicalEventInfo(ClinicalEventInfo clinicalEventInfo);

    int deleteClinicalEventInfo(@Param(value = "SJXH") String sjxh);

    String getClinicalEventDieInfo(@Param(value = "ZYH") String zyh);

    List<String> getClinicalEventChildbirthInfo(@Param(value = "ZYH") String zyh);

	String getCjzhByCjh(@Param(value = "CJH") String cjh, @Param(value = "JGID") String jgid);

    List<String> getZKXM(@Param(value = "ZYH") String zyh, @Param(value = "KSSJ") Date start, @Param(value = "JSSJ") Date end);

    List<LifeSignHistoryInfo> getLifeSignHistoryInfo(@Param("ZYH") String zyh, @Param("XMH") String xmh, @Param("dbtype") String dbtype);
}
