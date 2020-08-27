package com.bsoft.nis.mapper.patient;

import com.bsoft.nis.domain.patient.*;
import com.bsoft.nis.domain.patient.db.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface PatientMapper {
    Patient getPatient(@Param(value = "ZYHM") String zyhm);

    Patient getPatientByZyhmAndJg(@Param(value = "ZYHM") String zyhm, @Param(value = "JGID") String jgid);

    Patient getPatientByZyh(@Param(value = "ZYH") String zyh);
    List<ZDJB> getPatientJBZD(@Param(value = "ZYH") List<String> zyhs);
    List<BCRYBean> getGroupRYList(@Param(value = "YGDM") String ygdm,@Param(value = "BQDM") String bqdm);
    Integer deleteGroupRYList(@Param(value = "YGDM") String ygdm,@Param(value = "BQDM") String bqdm);
    Integer insertGroupRYList(@Param(value = "itemList")List<BCRYBean> items,@Param(value = "dbtype") String dbtype);
    List<BCCWBean> getGroupCWList(@Param(value = "BCBH") String bcbh);
    List<BCSZBean> getGroupCfgList(@Param(value = "BQDM") String bqdm,@Param(value = "JGID") String jgid);
    Integer getPatientBRLJCount_OnWay(@Param(value = "ZYH")String zyh);
    Integer getPatientBRLJCount_OutWay(@Param(value = "ZYH")String zyh);
    /*
              升级编号【56010016】============================================= start
              病人列表：重整病人信息，确定哪些信息需要演示
              ================= Classichu 2017/10/18 9:34
              */
    List<String> getPatientZD(@Param(value = "ZYH") String zyh);
    List<String> getPatientDMMCFromMOB_DMZD(@Param(value = "DMSB") String dmsb,@Param(value = "DMLB") String dmlb);
    List<String> getPatientBRXZFromMOB(@Param(value = "BRXZ") String brxz);
    /* =============================================================== end */
    List<RecondBean> getLastDERecord(@Param("ZYHLIST") List<String>  zyhList, @Param("JGID")String jgid);
    List<ZKbean> getFXZKRecord(@Param("ZYHLIST") List<String>  zyhList, @Param("JGID")String jgid);

    Patient getPatientByZyhForEvalution(@Param(value = "ZYH") String zyh);

    List<SickPersonVo> getPatientsForDept(@Param(value = "BQID") String bqid, @Param(value = "JGID") String jgid,@Param(value = "dbtype") String dbtype);

    List<SickPersonVo> getPatientsForSSKS(@Param(value = "SSKS") String ssks,@Param(value = "HSGH") String hsgh, @Param(value = "JGID") String jgid,@Param(value = "dbtype") String dbtype);

    SickPersonVo getPatientForScan(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid);

    List<Patient> getSimplePatientsForDept(@Param(value = "BRBQ") String bqid, @Param(value = "JGID") String jgid, @Param(value = "systime") String systime, @Param(value = "dbtype") String dbtype);

    SickPersonDetailVo getPatientDetail(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid,@Param(value = "dbtype") String dbtype);
    String getPatientYSMC(@Param(value = "ZYH") String zyh,@Param(value = "dbtype") String dbtype);

    ExpenseTotal getPatientPayMoney(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid);

    ExpenseTotal getPatientAdvancePayMoney(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid);

    String getPatientDiagnose(@Param(value = "ZYH") String zyh, @Param(value = "JLLX") String jllx, @Param(value = "JGID") String jgid);

    List<AllergicDrug> getPatientAllergicDrugs(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid);

    List<State> getPatientStates(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid);

    String getPatientZyhByScan(@Param(value = "BRWD") String scanStr);

    String getPatientZyhByZyhm(@Param(value = "ZYHM") String scanStr);

    List<SickPersonVo> getMyPatientHIS(@Param(value = "BRBQ") String brbq, @Param(value = "ZRHS") String hsgh, @Param(value = "JGID") String jgid);

    List<SickPersonVo> getMyPatientMOB(@Param(value = "BRBQ") String brbq, @Param(value = "HSGH") String hsgh, @Param(value = "JGID") String jgid);

    List<SickPersonVo> getPatientByBrchHIS(@Param(value = "BRBQ") String brbq, @Param(value = "BRCH") String brch, @Param(value = "JGID") String jgid,
                                     @Param(value = "dbtype") String dbtype);

    String getRFID(@Param(value = "ZYH") String zyh, @Param(value = "JGID") String jgid);

    String getDeviceStatus(@Param(value = "SBID") String sbid, @Param(value = "LXXH") String lxxh);

    int addDeviceInfo(@Param(value = "SBXH") String sbxh, @Param(value = "LXXH") String lxxh,
                      @Param(value = "SBID") String sbid, @Param(value = "SBMS") String sbms,
                      @Param(value = "SBZT") String sbzt, @Param(value = "SSKS") String ssks,
                      @Param(value = "LRSJ") String lrsj, @Param(value = "BZ") String bz,
                      @Param(value = "dbtype") String dbtype);

    int editDeviceInfo(@Param(value = "SBID") String sbid, @Param(value = "SBZT") String sbzt);

    int addBRWDInfo(@Param(value = "WDHM") String wdhm, @Param(value = "ZYH") String zyh,
                    @Param(value = "WDZT") String wdzt, @Param(value = "YEPB") String yepb,
                    @Param(value = "WDTM") String wdtm, @Param(value = "CSSJ") String cssj,
                    @Param(value = "CSGH") String csgh, @Param(value = "SBLX") String sblx,
                    @Param(value = "JGID") String jgid, @Param(value = "dbtype") String dbtype);

    int editBRWDInfo(@Param(value = "WDHM") String wdhm);

    List<PatientDiagonosis> getPatientDiagnoseFromRYZD(@Param(value = "ZYH") String zyh);
    List<PatientDiagonosis> getPatientDiagnoseFromRYZDList(@Param("ZYHLIST") List<String>  zyhList);
}
