package com.bsoft.nis.mapper.inspection;


import com.bsoft.nis.domain.inspection.*;
import com.bsoft.nis.domain.inspection.db.SpecimenJYTM;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface InspectionMapper {

   List<InspectionVo> GetInspectionList(@Param(value = "BRID")String zyhm, @Param(value = "JGID")String jgid);
   /*
            升级编号【56010025】============================================= start
            检验检查：检验List项目数据趋势图，项目分类查看
            ================= Classichu 2017/10/18 9:34
            */
   List<InspectionXMBean> GetInspectionXMBeanList(@Param(value = "XMID")String xmid,
                                                  @Param(value = "BRID") String zyhm, @Param(value = "JGID")String jgid);
      /* =============================================================== end */

   List<InspectionDetailVo> GetInspectionDetail(@Param(value = "YBHM")String ybhm, @Param(value = "JGID")String jgid);

   List<ExamineVo> GetRisList(@Param(value = "BRID")String zyhm, @Param(value = "JGID")String jgid);

   List<ExamineDetailVo> GetRisDetail(@Param(value = "DJBS")String djbs, @Param(value = "JGID")String jgid);

   List<SpecimenVo> GetCaptureData(@Param(value = "BRID")String zyhm,@Param(value = "JGID") String jgid,
                                   @Param(value = "RYRQ") String ryrq, @Param(value = "dbtype") String dbtype);

   List<SpecimenVo> GetCaptureDataList(@Param(value = "zyhmList")List<String> zyhmList,@Param(value = "JGID") String jgid,
                                   @Param(value = "RYRQ") String ryrq,@Param(value = "dbtype") String dbtype);

   List<SpecimenVo> GetHistoryCaptureData(@Param(value = "BRID")String zyhm, @Param(value = "KSSJ")String start,
                                          @Param(value = "JSSJ")String end, @Param(value = "JGID")String jgid,
                                          @Param(value = "dbtype") String dbtype);

   List<SpecimenVo> GetHistoryCaptureDataList(@Param(value = "zyhmList")List<String> zyhmList, @Param(value = "KSSJ")String start,
                                          @Param(value = "JSSJ")String end, @Param(value = "JGID")String jgid,
                                          @Param(value = "dbtype") String dbtype);

   SpecimenJYTM getJYTM(@Param(value = "TMBH")String tmbh);

   List<List<?>> GetDocNoBySp(Map<String, Object> map);

   List<List<?>> CaptureExecute(Map<String, Object> zxparms);

   Map<String,String> GetSQBH(@Param(value = "TMBH")String tmbh, @Param(value = "JGID")String jgid);

   void UpdateYZB(@Param(value = "HSZXSJ")Date hszxsj, @Param(value = "ZXHSGH")String zxhsgh,
                  @Param(value = "SQDH")String sqdh, @Param("ZYH")String zyh, @Param("JGID")String jgid);

   Map<String,Object> DoubleCheck(@Param("TMBH")String tmbh, @Param("JGID")String jgid);

   List<CYInfoBean> GetCYInfoByTMBH(@Param("TMBH")String tmbh, @Param("BRID")String brid);

   List<Long> GetYZXH(@Param("TMBH")String tmbh, @Param("JGID")String jgid);

   int UpdateYZB2(@Param(value = "HSZXSJ") Date hszxsj, @Param(value = "ZXHSGH")String zxhsgh, @Param(value = "list") List<Long> list);
}
