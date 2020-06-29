package com.bsoft.nis.mapper.healthguid;

import com.bsoft.nis.domain.healthguid.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 健康教育
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
public interface HealthGuidMapper {

    List<HealthGuid> getHealthGuidList(@Param(value = "BQID") String bqid, @Param(value = "JGID") String jgid);

    List<HealthGuidItem> getHealthGuidItemListForBd(@Param(value = "GLLX") String gllx, @Param(value = "GLXH") String glxh, @Param(value = "ZYH") String zyh,
                                                    @Param(value = "BQID") String bqid, @Param(value = "JGID") String jgid);

    List<HealthGuidItem> getHealthGuidItemListForFl(@Param(value = "GLLX") String gllx, @Param(value = "YSXH") String ysxh, @Param(value = "ZYH") String zyh,
                                                    @Param(value = "BQID") String bqid, @Param(value = "JGID") String jgid);

    String getMaxJlxh(@Param(value = "GLXH") String glxh, @Param(value = "GLLX") String gllx);

    List<HealthGuidOperItem> getHealthGuidXjdx();

    List<HealthGuidOperItem> getHealthGuidXjfs();

    List<HealthGuidOperItem> getHealthGuidXgpj();

    List<HealthGuidType> getHealthGuidTypeListByYsxh(@Param(value = "YSXH") String ysxh);

    List<HealthGuidType> getHealthGuidTypeListByGlxh(@Param(value = "GLXH") String glxh);

    List<HealthGuidDetail> getHealthGuidDetailListByGlxh(@Param(value = "GLXH") String glxh);

    HealthGuidData getHealthGuidDataByJlxh(@Param(value = "JLXH") String jlxh);

    List<HealthGuidDetail> getHealthGuidDetailListByJlxh(@Param(value = "JLXH") String jlxh);

    int addHealthGuidJkxjjl(@Param(value = "JLXH") String jlxh, @Param(value = "ZYH") String zyh, @Param(value = "BQID") String bqid,
                            @Param(value = "GLXH") String glxh, @Param(value = "GLLX") String gllx, @Param(value = "JLSJ") String jlsj,
                            @Param(value = "JLGH") String jlgh, @Param(value = "CJSJ") String cjsj, @Param(value = "CJGH") String cjgh,
                            @Param(value = "QMSJ") String qmsj, @Param(value = "QMGH") String qmgh, @Param(value = "JGID") String jgid,
                            @Param(value = "BZXX") String bzxx, @Param(value = "dbtype") String dbtype);

    int addHealthGuidXjjlxm(@Param(value = "JLXM") String jlxm, @Param(value = "JLXH") String jlxh, @Param(value = "GLXH") String glxh,
                            @Param(value = "XMXH") String xmxh, @Param(value = "ZDYBZ") String zdybz, @Param(value = "XMNR") String xmnr,
                            @Param(value = "BZXX") String bzxx, @Param(value = "XJSJ") String xjsj, @Param(value = "XJGH") String xjgh,
                            @Param(value = "XJDX") String xjdx, @Param(value = "XJFS") String xjfs, @Param(value = "XJPJ") String xjpj,
                            @Param(value = "dbtype") String dbtype);

    int editHealthGuidXjjlxm(@Param(value = "JLXM") String jlxm, @Param(value = "XJSJ") String xjsj, @Param(value = "XJGH") String xjgh,
                             @Param(value = "XJDX") String xjdx, @Param(value = "XJFS") String xjfs, @Param(value = "XJPJ") String xjpj,
                             @Param(value = "dbtype") String dbtype);

    int deleteHealthGuidJkxjjlByJlxh(@Param(value = "JLXH") String jlxh);

    int deleteHealthGuidXjjlxmByJlxm(@Param(value = "JLXM") String jlxm);

    int deleteHealthGuidXjjlxmByJlxh(@Param(value = "JLXH") String jlxh);

    int deleteHealthGuidXjjlxmByGlxh(@Param(value = "JLXH") String jlxh, @Param(value = "GLXH") String glxh);

    String getYsxhByGlxh(@Param(value = "GLXH") String glxh);

    String getHealthGuidRemark(@Param(value = "XMXH") String xmxh);

    int Signature(@Param(value = "JLXH") String jlxh, @Param(value = "QMGH") String qmgh, @Param(value = "QMSJ") String qmsj,
                  @Param(value = "dbtype") String dbtype);

    int CancleSignature(@Param(value = "JLXH") String jlxh);

    int Evaluate(@Param(value = "JLXM") String jlxm, @Param(value = "XJPJ") String xjpj, @Param(value = "PJSJ") String pjsj,
                 @Param(value = "PJGH") String pjgh, @Param(value = "dbtype") String dbtype);

    int CancleEvaluateByJlxm(@Param(value = "JLXM") String jlxm);

    int CancleEvaluateByJlxhAndGlxh(@Param(value = "JLXH") String jlxh, @Param(value = "GLXH") String glxh);

}
