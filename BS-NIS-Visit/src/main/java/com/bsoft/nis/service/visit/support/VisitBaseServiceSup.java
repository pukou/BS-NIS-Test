package com.bsoft.nis.service.visit.support;

import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.visit.CheckState;
import com.bsoft.nis.domain.visit.VisitHistory;
import com.bsoft.nis.domain.visit.VisitPerson;
import com.bsoft.nis.mapper.visit.VisitBaseServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by king on 2016/11/18.
 */
@Service
public class VisitBaseServiceSup extends RouteDataSourceService {

    @Autowired
    VisitBaseServiceMapper mapper;

    /**
     * 获取巡视记录
     *
     * @param zyh  住院号
     * @param xssj 巡视时间
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(readOnly = true)
    public List<VisitHistory> getPatrolHistory(String zyh, String xssj, String jgid) throws SQLException, DataAccessException {

        String dbtype = this.getCurrentDataSourceDBtype();
        return mapper.getPatrolHistory(zyh, xssj, jgid,dbtype);
    }

    /*升级编号【56010027】============================================= start
                         处理房间条码、获取房间病人处理
            ================= classichu 2018/3/22 10:41
            */
    @Transactional(rollbackFor = Exception.class)
    public List<String> getRoomPatientList(String fjhm, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getRoomPatientList(fjhm, jgid);
    }
    /* =============================================================== end */

    /**
     * 查找病人
     *
     * @param ksdm 科室代码
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(readOnly = true)
    public List<VisitPerson> getPatrol(String ksdm, String jgid) throws SQLException, DataAccessException {

        return mapper.getPatrol(ksdm, jgid);
    }

    /**
     * 已巡视病人
     *
     * @param xsgh 巡视工号
     * @param xssj 巡视时间
     * @param jgid 机构id
     * @param zyh  住院号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(readOnly = true)
    public List<String> getPatrolZyh(String xsgh, String xssj, String jgid, List<String> zyh) throws SQLException, DataAccessException {

        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getPatrolZyh(xsgh, xssj, jgid, zyh, dbtype);
    }

    @Transactional(readOnly = true)
    public int delPatrol(String xsbs, String urid, Date zfsj, String jgid) throws SQLException, DataAccessException {

        //String dbtype = getCurrentDataSourceDBtype();
        return mapper.updatePatrol(xsbs, urid, zfsj);
    }

    /**
     * 需巡视病人
     *
     * @param zyh       住院号
     * @param jgid      机构id
     * @param startxssj 开始巡视时间
     * @param endxssj   结束巡视时间
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(readOnly = true)
    public List<String> getPatrolPatitent(String zyh, String jgid, String startxssj, String endxssj) throws SQLException, DataAccessException {

        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getPatrolPatitent(zyh, jgid, startxssj, endxssj,dbtype);
    }

    /**
     * 巡视情况
     *
     * @param jgid 机构id
     * @return
     */
    @Transactional(readOnly = true)
    public List<CheckState> getPatrolTypeInfo(String jgid) throws SQLException, DataAccessException {

        return mapper.getPatrolTypeInfo(jgid);
    }

    /**
     * 查询病人
     *
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(readOnly = true)
    public List<VisitPerson> getPatrol2(String zyh, String jgid) throws SQLException, DataAccessException {

        return mapper.getPatrol2(zyh, jgid);
    }

    /**
     * 已巡视详情
     *
     * @param xsbs 巡视
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(readOnly = true)
    public List<VisitPerson> getPatrolDetail(Long xsbs, String zyh, String jgid) throws SQLException, DataAccessException {

        return mapper.getPatrolDetail(xsbs, zyh, jgid);
    }

    /**
     * 巡视保存
     *
     * @param xsbs 巡视标识
     * @param zyh  住院号
     * @param xsgh 巡视工号
     * @param xssj 巡视时间
     * @param jlsj 记录时间
     * @param xsqk 巡视情况
     * @param smbz 扫描标志
     * @param brbq 病人病区
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean savePatrol(Long xsbs, String zyh, String xsgh, String xssj, String jlsj, String xsqk, String smbz, String brbq, String jgid) throws SQLException, DataAccessException {

        String dbtype = getCurrentDataSourceDBtype();
        return mapper.savePatrol(xsbs, zyh, xsgh, xssj, jlsj, xsqk, smbz, brbq, jgid,dbtype);
    }

}
