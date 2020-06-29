package com.bsoft.nis.service.advicecheck.support;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.advicecheck.AdviceForm;
import com.bsoft.nis.domain.advicecheck.AdviceFormDetail;
import com.bsoft.nis.domain.advicecheck.CheckForm;
import com.bsoft.nis.mapper.advicecheck.AdviceCheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by king on 2016/11/28.
 */
@Service
public class AdviceCheckServiceSup extends RouteDataSourceService {

    @Autowired
    AdviceCheckMapper mapper;

    /**
     * 获取加药摆药核对列表
     *
     * @param brbq   病人病区
     * @param syrq   输液日期
     * @param jyhdbz 加药核对标志
     * @param byhdbz 摆药核对标志
     * @param gslx   归属类型
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<AdviceForm> getDosingCheckList(String brbq, String syrq, String jyhdbz, String byhdbz, String gslx)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getDosingCheckList(brbq, syrq, jyhdbz, byhdbz, gslx, dbtype);
    }

    /**
     * 根据病区获得病人
     *
     * @param brbq 病人病区
     * @param jgid 机构id
     * @return
     */
    public List<AdviceForm> getPatientsByBQ(String brbq, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        return mapper.getPatientsByBQ(brbq, jgid);
    }


    /**
     * @param tmbh 条码编号
     * @param gslx 归属类型
     * @return
     */
    public List<CheckForm> create(String tmbh, String gslx)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.create(tmbh, gslx);
    }

    public List<CheckForm> createBySydh(String sydh, String gslx)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.createBySydh(sydh, gslx);
    }

    /**
     * 获取医嘱编号信息
     *
     * @param jlxh
     * @return
     */
    public List<AdviceFormDetail> getAdviceName(List<String> jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        return mapper.getAdviceName(jlxh);
    }

    /**
     * 获得药品核对详情
     *
     * @param sydh
     * @param gslx
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<AdviceFormDetail> getCheckDetail(String sydh, String gslx)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getCheckDetail(sydh, gslx);
    }

    /**
     * 摆药加药核对更新信息
     *
     * @param hdgh 核对工号
     * @param hdsj 核对时间
     * @param sydh 输液单号
     * @return
     */
    public int updateCheckForm1(String hdgh, String hdsj, String sydh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.updateCheckForm1(hdgh, hdsj, sydh, jgid, dbtype);
    }

    /**
     * 摆药加药核对更新信息
     *
     * @param hdgh 核对工号
     * @param hdsj 核对时间
     * @param sydh 输液单号
     * @return
     */
    public int updateCheckForm2(String hdgh, String hdsj, String sydh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.updateCheckForm2(hdgh, hdsj, sydh, jgid, dbtype);
    }

    /**
     * 摆药加药核对更新信息
     *
     * @param hdgh 核对工号
     * @param hdsj 核对时间
     * @param sydh 输液单号
     * @return
     */
    public int updateCheckForm3(String hdgh, String hdsj, String sydh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.updateCheckForm3(hdgh, hdsj, sydh, jgid, dbtype);
    }

    /**
     * 摆药加药核对更新信息
     *
     * @param hdgh 核对工号
     * @param hdsj 核对时间
     * @param sydh 输液单号
     * @return
     */
    public int updateCheckForm4(String hdgh, String hdsj, String sydh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.updateCheckForm4(hdgh, hdsj, sydh, jgid, dbtype);
    }


}
