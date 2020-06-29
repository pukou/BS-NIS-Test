package com.bsoft.nis.mapper.nurserecord;

import com.bsoft.nis.domain.nurserecord.LastDataBean;
import com.bsoft.nis.domain.nurserecord.RefrenceValue;
import com.bsoft.nis.domain.nurserecord.db.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Describtion:
 * Created: dragon
 * Date： 2016/12/7.
 */
public interface NurseRecordInterface {
    List<Controll> getActivieCtrlListByJlbh(@Param(value = "JGID") String jgid,
                                            @Param(value = "JLBH") String jlbh,
                                            @Param(value = "ZYH") String zyh,
                                            @Param(value = "JLSJ") String nowStr
    , @Param(value = "dbtype") String dbtype);

    List<Controll> getActivieCtrlListByJgbh(@Param(value = "JGID") String jgid,
                                            @Param(value = "JGBH") String jgbh,
                                            @Param(value = "ZYH") String zyh,
                                            @Param(value = "JLSJ") String nowStr, @Param(value = "dbtype") String dbtype);
    /*
      升级编号【56010022】============================================= start
      护理记录:可以查看项目最近3次的记录，可以选择其中一次的数据到当前的护理记录单上。
      ================= Classichu 2017/10/18 10:41
      */
    List<LastDataBean> getlastXMData(@Param(value = "JGID") String jgid,
                                     @Param(value = "XMBH") String xmbh,
                                     @Param(value = "HSGH") String hsgh,
                                     @Param(value = "ZYH") String zyh);
    /* =============================================================== end */
    List<Controll> getCtrlListByJlbh(@Param(value = "JGID") String jgid,
                                     @Param(value = "JLBH") String jlbh);

    List<Controll> getCtrlListByJgbh(@Param(value = "JGID") String jgid,
                                     @Param(value = "JGBH") String jgbh);

    List<RefrenceValue> getDangerRefrence(@Param(value = "ZYH") String zyh,
                                          @Param(value = "PAGEINDEX") int pageIndex,
                                          @Param(value = "JGID") String jgid);

    List<RefrenceValue> getLifeSignRefrece(@Param(value = "ZYH") String zyh,
                                           @Param(value = "PAGEINDEX") int pageIndex,
                                           @Param(value = "JGID") String jgid,
                                           @Param(value = "XMH") String xmh);

    List<Map> getDangerRecordSum(@Param(value = "ZYH") String zyh);

    List<Map> getLifeSignRecordSum(@Param(value = "ZYH") String zyh,
                                   @Param(value = "XMH") String yskz,
                                   @Param(value = "JGID") String jgid);

    List<NRDbTree> getNRTree(@Param(value = "ZYH") String zyh,@Param(value = "JGID") String jgid);

    List<NRDbTree> getNRTreeByMblx(@Param(value = "ZYH") String zyh,@Param(value = "JGID") String jgid,@Param(value = "MBLX") String mblx);

    List<Map> getNRTreeByZYHAndJGBH(@Param(value = "ZYH") String zyh,@Param(value = "JGBH") String jgbh);

    Integer addNurseRecord(NRData saveData, @Param(value = "dbtype") String dbtype);

    Integer addNurseRecordDetail(NRItem item);

    Integer addNurseRecordContent(NRContent content);

    List<NRData> getNRData(@Param(value = "JLBH") String jlbh,@Param(value = "JGID") String jgid);

    List<NRContent> getNRContents(@Param(value = "ZYH") String zyh,@Param(value = "JGBH") String jgbh,@Param(value = "JGID") String jgid);

    Integer updatePrimaryRecord(NRData nrData);

    Integer updateNurseRecordDetail(NRItem item);

    Integer deleteNurseRecord(@Param(value = "JLBH") String jlbh,@Param(value = "ZYH") String zyh,@Param(value = "JGID") String jgid);

    List<EMR_WH_QMXX> getSignValids(@Param(value = "YHID") String yhid);

    Integer signNameNurseRecord(@Param(value = "WCQM") String jlxh,@Param(value = "WCSJ") String nowStr,@Param(value = "JLBH") String jlbh,@Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    List<DrugMedicalAdviceRefContent> getGrugMedicalAdvices(@Param(value = "ZYH") String zyh,@Param(value = "KSSJ") String startime,@Param(value = "JSSJ") String endStr,@Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    List<OperationRefContent> getOperationRefs(@Param(value = "ZYH") String zyh,@Param(value = "JGID") String jgid);

    List<SignRefContent> getLifeSignRefs(@Param(value = "ZYH") String zyh,@Param(value = "KSSJ") String startime,@Param(value = "JSSJ") String endStr,@Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    /**
     * TODO:测试用，删除
     * @param ksrq
     * @param jsrq
     * @return
     */
    List<Map> getLzjh(@Param(value = "ksrq") String ksrq,@Param(value = "jsrq") String jsrq);
}
