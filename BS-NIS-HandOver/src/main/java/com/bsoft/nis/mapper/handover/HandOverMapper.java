package com.bsoft.nis.mapper.handover;

import com.bsoft.nis.domain.handover.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2017-02-15
 * Time: 10:39
 * Version:
 */
public interface HandOverMapper {

    List<HandOverForm> getHandOverFormList(@Param(value = "JGID") String jgid);

    List<HandOverRecord> getHandOverRecordList(@Param(value = "YSXH") String ysxh, @Param(value = "ZTBZ") String ztbz,
                                               @Param(value = "ZYH") String zyh, @Param(value = "BQID") String bqid,
                                               @Param(value = "JGID") String jgid);

    List<HandOverRecord> getHandOverRecordListBySSKS(@Param(value = "YSXH") String ysxh, @Param(value = "ZTBZ") String ztbz,
                                               @Param(value = "ZYH") String zyh,@Param(value = "SSKS") String ssks,
                                               @Param(value = "JGID") String jgid);

    HandOverRecord getHandOverRecord(@Param(value = "JLXH") String jlxh);

    List<HandOverProject> getHandOverRecordProjectList(@Param(value = "JLXH") String jlxh);

    List<HandOverOption> getHandOverRecordOptionList(@Param(value = "JLXH") String jlxh);

    HandOverForm getHandOverForm(@Param(value = "YSXH") String ysxh);

    List<HandOverClassify> getHandOverClassifyList(@Param(value = "YSXH") String ysxh);

    List<HandOverProject> getHandOverProjectList(@Param(value = "YSXH") String ysxh);

    List<HandOverOption> getHandOverOptionList(@Param(value = "YSXH") String ysxh);

    int addHandOverRecord(HandOverRecord handOverRecord);

    int editHandOverRecordForSender(HandOverRecord handOverRecord);

    int editHandOverRecordForReceiver(HandOverRecord handOverRecord);

    int sendHandOverRecord(@Param(value = "JLXH") String jlxh);

    int delHandOverRecord(@Param(value = "JLXH") String jlxh);

    int addHandOverProject(HandOverProject handOverProject);

    int editHandOverProject(HandOverProject handOverProject);

    int delHandOverProject(@Param(value = "JLXM") String jlxm);

    int delHandOverProjectByJlxh(@Param(value = "JLXH") String jlxh);

    int addHandOverOption(HandOverOption handOverOption);

    int editHandOverOption(HandOverOption handOverOption);

    int delHandOverOption(@Param(value = "JLXX") String jlxx);

    int delHandOverOptionByJlxm(@Param(value = "JLXM") String jlxm);

    int delHandOverOptionByJlxh(@Param(value = "JLXH") String jlxh);

    List<RelativeItem> getLifeSignDataList(@Param(value = "TXSJ") String txsj, @Param(value = "ZYH") String zyh,
                                           @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    List<RelativeItem> getRiskDataList(@Param(value = "TXSJ") String txsj, @Param(value = "ZYH") String zyh,
                                       @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    String getDqzd(@Param(value = "ZYH") String zyh);

}
