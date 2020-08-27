package com.bsoft.nis.service.patient.support;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.patient.*;
import com.bsoft.nis.domain.patient.db.*;
import com.bsoft.nis.mapper.patient.PatientMapper;
import com.bsoft.nis.util.date.birthday.BirthdayUtil;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * 病人服务
 * Created by Administrator on 2016/10/18.
 */
@Service
public class PatientServiceSup extends RouteDataSourceService {

    @Autowired
    PatientMapper mapper;

    /**
     * 获取病区病人列表
     *
     * @param bqid
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<SickPersonVo> getPatientsForDept(String bqid, String jgid)
            throws SQLException, DataAccessException {
        String dbType = getCurrentDataSourceDBtype();
        return mapper.getPatientsForDept(bqid, jgid,dbType);
    }

    public List<BCRYBean> getGroupRYList(String ygdm, String bqdm)
            throws SQLException, DataAccessException {
        String dbType = getCurrentDataSourceDBtype();
        //  XH      keepOrRoutingDateSource(DataSource.HRP);
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getGroupRYList(ygdm, bqdm);
    }
    public Integer saveGroupRYList(@Param(value = "itemList")List<BCRYBean> items,String ygdm,String bqdm)
            throws SQLException, DataAccessException {
        String dbType = getCurrentDataSourceDBtype();
//  XH      keepOrRoutingDateSource(DataSource.HRP);
        keepOrRoutingDateSource(DataSource.MOB);
        mapper.deleteGroupRYList(ygdm, bqdm);
        if (items!=null&&!items.isEmpty()){
            mapper.insertGroupRYList(items,dbType);
        }
        return 0;
    }
    public List<BCCWBean> getGroupCWList(String bcbh)
            throws SQLException, DataAccessException {
        String dbType = getCurrentDataSourceDBtype();
        //  XH      keepOrRoutingDateSource(DataSource.HRP);
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getGroupCWList(bcbh);
    }
    public List<BCSZBean> getGroupCfgList(String bqdm,String jgid)
            throws SQLException, DataAccessException {
        String dbType = getCurrentDataSourceDBtype();
        //  XH      keepOrRoutingDateSource(DataSource.HRP);
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getGroupCfgList(bqdm,jgid);
    }
    public List<ZDJB> getPatientJBZD(List<String> zyhs)
            throws SQLException, DataAccessException {
        return mapper.getPatientJBZD(zyhs);
    }
    public Integer getPatientBRLJCount_OnWay(String zyh)
            throws SQLException, DataAccessException {
        return mapper.getPatientBRLJCount_OnWay(zyh);
    }
    public Integer getPatientBRLJCount_OutWay(String zyh)
            throws SQLException, DataAccessException {
        return mapper.getPatientBRLJCount_OutWay(zyh);
    }
    /*
           升级编号【56010016】============================================= start
           病人列表：重整病人信息，确定哪些信息需要演示
           ================= Classichu 2017/10/18 9:34
           */
    public List<String> getPatientZD(String zyh)
            throws SQLException, DataAccessException {
        return mapper.getPatientZD(zyh);
    }
    public List<String> getPatientDMMCFromMOB_DMZD(String dmsb,String dmlb)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getPatientDMMCFromMOB_DMZD(dmsb,dmlb);
    }
    public List<String> getPatientBRXZFromMOB(String dmlb)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        return mapper.getPatientBRXZFromMOB(dmlb);
    }
    /* =============================================================== end */
    public List<RecondBean>  getLastDERecord(List<String> zyhList, String jgid) throws SQLException, DataAccessException {
        return mapper.getLastDERecord(zyhList,jgid);
    }
    public List<ZKbean>  getFXZKRecord(List<String> zyhList, String jgid) throws SQLException, DataAccessException {
        return mapper.getFXZKRecord(zyhList,jgid);
    }
    public List<SickPersonVo> getPatientsForSSKS(String ssks,String hsgh, String jgid)
            throws SQLException, DataAccessException {
       String dbType = getCurrentDataSourceDBtype();
        return mapper.getPatientsForSSKS(ssks,hsgh,jgid,dbType);
    }
    /**
     * 获取病区病人列表
     *
     * @param bqid
     * @param jgid
     * @param nowStr
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Patient> getSimplePatientsForDept(String bqid, String jgid, String nowStr)
            throws SQLException, DataAccessException {
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getSimplePatientsForDept(bqid, jgid, nowStr, dbtype);
    }

    /**
     * 病人详情
     *
     * @param zyh
     * @param jgid
     * @return
     */
    public SickPersonDetailVo getPatientDetail(String zyh, String jgid)
            throws SQLException, DataAccessException {

        keepOrRoutingDateSource(DataSource.HRP);

        // 1.获取基本信息
        SickPersonDetailVo sickPersonDetailVo = new SickPersonDetailVo();
        String dbtype = getCurrentDataSourceDBtype();
        sickPersonDetailVo = mapper.getPatientDetail(zyh, jgid,dbtype);

        //计算年龄(生命体征中会用到)
        if (!TextUtils.isBlank(sickPersonDetailVo.CSNY)) {
            try {
                sickPersonDetailVo.BRNL = BirthdayUtil.getAgesPairCommonStrSimple(sickPersonDetailVo.CSNY, sickPersonDetailVo.RYRQ);
            }catch(Exception ex){}
        }

        if(sickPersonDetailVo.BRNL == null) sickPersonDetailVo.BRNL = "0";

        // 2.转换字典信息

        return sickPersonDetailVo;
    }

    /**
     * 协和
     * @param zyh
     * @param jgid
     * @return
     * @throws DataAccessException
     */
    public String getPatientYSMC(String zyh, String jgid)
            throws DataAccessException {

        keepOrRoutingDateSource(DataSource.HRP);

        String dbtype = getCurrentDataSourceDBtype();
        String ysmc = mapper.getPatientYSMC(zyh,dbtype);

        return ysmc;
    }
    /**
     * 获取病人总付款金额
     *
     * @param zyh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public ExpenseTotal getPatientPayMoney(String zyh, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getPatientPayMoney(zyh, jgid);
    }

    /**
     * 获取病人缴付金额
     *
     * @param zyh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public ExpenseTotal getPatientAdvancePayMoney(String zyh, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getPatientAdvancePayMoney(zyh, jgid);
    }

    /**
     * 获取疾病诊断
     *
     * @param zyh
     * @param jllx
     * @param jgid
     * @return
     */
    @Deprecated
    public String getPatientDiagnose(String zyh, String jllx, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getPatientDiagnose(zyh, jllx, jgid);
    }

    /**
     * 获取病人诊断
     * @param zyh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PatientDiagonosis> getPatientDiagnoseFromRYZD(String zyh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        return mapper.getPatientDiagnoseFromRYZD(zyh);
    }
    public List<PatientDiagonosis> getPatientDiagnoseFromRYZDList(List<String>  zyhList)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        return mapper.getPatientDiagnoseFromRYZDList(zyhList);
    }
    /**
     * 获取过敏药物
     *
     * @param zyh
     * @param jgid
     * @return
     */
    public List<AllergicDrug> getPatientAllergicDrugs(String zyh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        return mapper.getPatientAllergicDrugs(zyh, jgid);
    }

    /**
     * 获取病人状态
     *
     * @param zyh
     * @param jgid
     * @return
     */
    public List<State> getPatientStates(String zyh, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getPatientStates(zyh, jgid);
    }

    /**
     * 根据病人腕带获取病人住院号
     *
     * @param scanStr
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getPatientZyhByScan(String scanStr)
            throws SQLException, DataAccessException {
        return mapper.getPatientZyhByScan(scanStr);
    }

    /**
     * 根据病人zyhm获取病人住院号
     *
     * @param scanStr
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getPatientZyhByZyhm(String scanStr)
            throws SQLException, DataAccessException {
        return mapper.getPatientZyhByZyhm(scanStr);
    }

    /**
     * 根据病人住院号获取病人信息-扫描用
     *
     * @param zyh
     * @param zyh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public SickPersonVo getPatientForScan(String zyh, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getPatientForScan(zyh, jgid);
    }

    /**
     * 获取HIS中我的病人列表
     *
     * @param brbq
     * @param hsgh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<SickPersonVo> getMyPatientHIS(String brbq, String hsgh, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getMyPatientHIS(brbq, hsgh, jgid);
    }

    /**
     * 获取MOB中我的病人列表
     *
     * @param brbq
     * @param hsgh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<SickPersonVo> getMyPatientMOB(String brbq, String hsgh, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getMyPatientMOB(brbq, hsgh, jgid);
    }

    /**
     * 根据病人创号获取HIS中病人
     *
     * @param brbq
     * @param brch
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<SickPersonVo> getPatientByBrchHIS(String brbq, String brch, String jgid)
            throws SQLException, DataAccessException {
        String dbType = getCurrentDataSourceDBtype();
        return mapper.getPatientByBrchHIS(brbq, brch, jgid,dbType);
    }

    /**
     * 检查病人有无绑定rfid
     *
     * @param zyh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getRFID(String zyh, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getRFID(zyh, jgid);
    }

    /**
     * 获取设备状态
     *
     * @param sbid
     * @param lxxh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getDeviceStatus(String sbid, String lxxh)
            throws SQLException, DataAccessException {
        return mapper.getDeviceStatus(sbid, lxxh);
    }

    /**
     * 新增设备信息
     *
     * @param sbxh
     * @param lxxh
     * @param sbid
     * @param sbms
     * @param sbzt
     * @param ssks
     * @param lrsj
     * @param bz
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addDeviceInfo(String sbxh, String lxxh, String sbid, String sbms,
                             String sbzt, String ssks, String lrsj, String bz)
            throws SQLException, DataAccessException {
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.addDeviceInfo(sbxh, lxxh, sbid, sbms,
                sbzt, ssks, lrsj, bz, dbtype);
    }

    /**
     * 修改设备信息
     *
     * @param sbid
     * @param sbzt
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editDeviceInfo(String sbid, String sbzt)
            throws SQLException, DataAccessException {
        return mapper.editDeviceInfo(sbid, sbzt);
    }

    /**
     * 新增条码信息
     *
     * @param wdhm
     * @param zyh
     * @param wdzt
     * @param yepb
     * @param wdtm
     * @param cssj
     * @param csgh
     * @param sblx 识别类型 0 腕带 1 床头卡 2 RFID
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addBRWDInfo(String wdhm, String zyh, String wdzt, String yepb, String wdtm,
                           String cssj, String csgh, String sblx, String jgid)
            throws SQLException, DataAccessException {
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.addBRWDInfo(wdhm, zyh, wdzt, yepb, wdtm, cssj, csgh, sblx, jgid, dbtype);
    }

    /**
     * 修改条码信息
     *
     * @param wdhm
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editBRWDInfo(String wdhm)
            throws SQLException, DataAccessException {
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.editBRWDInfo(wdhm);
    }
}
