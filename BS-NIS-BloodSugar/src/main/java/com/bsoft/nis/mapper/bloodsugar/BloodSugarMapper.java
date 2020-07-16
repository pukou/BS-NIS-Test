package com.bsoft.nis.mapper.bloodsugar;

import com.bsoft.nis.domain.bloodsugar.BloodSugar;
import com.bsoft.nis.domain.bloodsugar.PersonBloodSugar;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2017-05-22
 * Time: 13:38
 * Version:
 */
public interface BloodSugarMapper {

    List<String> getClsdList();

    List<BloodSugar> getBloodSugarList(@Param(value = "START") String start, @Param(value = "END") String end,
                                       @Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid,
                                       @Param(value = "dbtype") String dbtype);

    List<PersonBloodSugar> getNeedGetBloodSugarList(@Param(value = "BRBQ") String brbq, @Param(value = "JGID") String jgid,
                                                    @Param(value = "dbtype") String dbtype);

    int addBloodSugar(@Param(value = "JLXH") String jlxh, @Param(value = "ZYH") String zyh,
                      @Param(value = "SXBQ") String sxbq, @Param(value = "BRCH") String brch,
                      @Param(value = "SXSJ") String sxsj, @Param(value = "SXGH") String sxgh,
                      @Param(value = "CJSJ") String cjsj, @Param(value = "CJGH") String cjgh,
                      @Param(value = "CLSD") String clsd, @Param(value = "CLZ") String clz,
                      @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    int addBloodSugar1(@Param(value = "JLXH") String jlxh, @Param(value = "ZYH") String zyh,
                      @Param(value = "SXBQ") String sxbq, @Param(value = "BRCH") String brch,
                      @Param(value = "SXSJ") String sxsj, @Param(value = "SXGH") String sxgh,
                      @Param(value = "CJSJ") String cjsj, @Param(value = "CJGH") String cjgh,
                      @Param(value = "CLSD") String clsd, @Param(value = "CLZ") String clz,
                      @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    int addBloodSugar2(@Param(value = "JLXH") String jlxh, @Param(value = "ZYH") String zyh,
                      @Param(value = "SXBQ") String sxbq, @Param(value = "BRCH") String brch,
                      @Param(value = "SXSJ") String sxsj, @Param(value = "SXGH") String sxgh,
                      @Param(value = "CJSJ") String cjsj, @Param(value = "CJGH") String cjgh,
                      @Param(value = "CLSD") String clsd, @Param(value = "CLZ") String clz,
                      @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    int addBloodSugar3(@Param(value = "JLXH") String jlxh, @Param(value = "ZYH") String zyh,
                      @Param(value = "SXBQ") String sxbq, @Param(value = "BRCH") String brch,
                      @Param(value = "SXSJ") String sxsj, @Param(value = "SXGH") String sxgh,
                      @Param(value = "CJSJ") String cjsj, @Param(value = "CJGH") String cjgh,
                      @Param(value = "CLSD") String clsd, @Param(value = "CLZ") String clz,
                      @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    int editBloodSugar(@Param(value = "JLXH") String jlxh, @Param(value = "CLSD") String clsd,
                       @Param(value = "SXSJ") String sxsj, @Param(value = "CLZ") String clz,
                       @Param(value = "dbtype") String dbtype);

    int editBloodSugar1(@Param(value = "JLXH") String jlxh, @Param(value = "CLSD") String clsd,
                       @Param(value = "SXSJ") String sxsj, @Param(value = "CLZ") String clz,
                       @Param(value = "dbtype") String dbtype);

    int editBloodSugar2(@Param(value = "JLXH") String jlxh, @Param(value = "CLSD") String clsd,
                       @Param(value = "SXSJ") String sxsj, @Param(value = "CLZ") String clz,
                       @Param(value = "dbtype") String dbtype);

    int editBloodSugar3(@Param(value = "JLXH") String jlxh, @Param(value = "CLSD") String clsd,
                       @Param(value = "SXSJ") String sxsj, @Param(value = "CLZ") String clz,
                       @Param(value = "dbtype") String dbtype);

    int deleteBloodSugar(@Param(value = "JLXH") String jlxh);
}
