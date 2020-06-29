package com.bsoft.nis.mapper.advicesync;

import com.bsoft.nis.domain.adviceexecute.PlanInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Description: 医嘱同步
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-03-01
 * Time: 16:58
 * Version:
 */
public interface AdviceSyncMapper {

    List<PlanInfo> getBQPlanInfoListByZyh(@Param("ZYH") String zyh, @Param("GSLXLIST") List<String> gslxList,
                                          @Param("JHRQ") String jssj, @Param("JGID") String jgid,
                                          @Param("dbtype") String dbtype);

    List<PlanInfo> getBQPlanInfoListByJhhList(@Param("JHHLIST") List<String> jhhList);

    List<PlanInfo> getBQPlanInfoListByBqdm(@Param("BQDM") String bqdm, @Param("GSLXLIST") List<String> gslxList,
                                           @Param("JHRQ") String jssj, @Param("JGID") String jgid,
                                           @Param("dbtype") String dbtype);

    List<PlanInfo> getPlanInfoListByZyh(@Param("ZYH") String zyh, @Param("GSLXLIST") List<String> gslxList,
                                        @Param("JHRQ") String jssj, @Param("JGID") String jgid,
                                        @Param("dbtype") String dbtype);

    List<PlanInfo> getPlanInfoListByGljhhList(@Param("JHHLIST") List<String> jhhList);

    List<PlanInfo> getPlanInfoListByBqdm(@Param("BQDM") String bqdm, @Param("GSLXLIST") List<String> gslxList,
                                         @Param("JHRQ") String jssj, @Param("JGID") String jgid,
                                         @Param("dbtype") String dbtype);

    List<String> getGslxListByBqdm(@Param("BQDM") String bqdm);

    int addPlanInfoList(Map map);

    int editPlanInfo(@Param("GLJHH") String gljhh);

    int editBQPlanInfo(@Param("JHH") String jhh);
}
