package com.bsoft.nis.mapper.bloodtransfusion;

import com.bsoft.nis.domain.bloodtransfusion.BloodReciveInfo;
import com.bsoft.nis.domain.bloodtransfusion.BloodTransfusionInfo;
import com.bsoft.nis.domain.bloodtransfusion.BloodTransfusionTourInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
public interface BloodTransfusionMapper {

    List<BloodTransfusionInfo> getBloodTransfusionPlanList(@Param(value = "START") String start, @Param(value = "END") String end,
                                                           @Param(value = "ZYHM") String zyhm, @Param(value = "JGID") String jgid,
                                                           @Param(value = "dbtype") String dbtype);

    int startBloodTransfusion(@Param(value = "XDH") String xdh, @Param(value = "XDXH") String xdxh,
                              @Param(value = "ZXGH") String zxgh, @Param(value = "HDGH") String hdgh,
                              @Param(value = "SXSJ") String sxsj, @Param(value = "JGID") String jgid,
                              @Param(value = "dbtype") String dbtype);

    int endBloodTransfusion(@Param(value = "XDH") String xdh, @Param(value = "XDXH") String xdxh,
                            @Param(value = "JSR") String jsr, @Param(value = "JSSJ") String jssj,
                            @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    List<BloodTransfusionInfo> getBloodTransfusionInfoList(@Param(value = "XDH") String xdh, @Param(value = "XDXH") String xdxh,
                                                           @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    List<BloodReciveInfo> getBloodRecieveList(@Param(value = "START") String start, @Param(value = "END") String end,
                                              @Param(value = "BQID") String bqid, @Param(value = "STATUS") String status,
                                              @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    int bloodRecieve(@Param(value = "BYTM") String bytm, @Param(value = "SXZ") String sxz,
                     @Param(value = "JXHS") String jxhs, @Param(value = "JXRQ") String jxrq,
                     @Param(value = "dbtype") String dbtype);

    int cancleBloodRecieve(@Param(value = "BYTM") String bytm);

    List<BloodTransfusionTourInfo> getBloodTransfusionTourInfoList(@Param(value = "SXDH") String sxdh, @Param(value = "JGID") String jgid);

    int addBloodTransfusionTourInfo(BloodTransfusionTourInfo bloodTransfusionTourInfo);

    int editBloodTransfusionTourInfo(BloodTransfusionTourInfo bloodTransfusionTourInfo);

    int deleteBloodTransfusionTourInfo(BloodTransfusionTourInfo bloodTransfusionTourInfo);

    int saveBloodBagRecieve(@Param(value = "SXDH") String sxdh, @Param(value = "SJGH") String yhid,
                            @Param(value = "SJRQ") String sjsj, @Param(value = "JGID") String jgid,
                            @Param(value = "dbtype") String dbtype);


}
