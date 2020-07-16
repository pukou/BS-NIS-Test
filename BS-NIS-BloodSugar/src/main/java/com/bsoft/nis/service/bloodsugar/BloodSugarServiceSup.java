package com.bsoft.nis.service.bloodsugar;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.bloodsugar.BloodSugar;
import com.bsoft.nis.domain.bloodsugar.PersonBloodSugar;
import com.bsoft.nis.mapper.bloodsugar.BloodSugarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2017-05-24
 * Time: 11:31
 * Version:
 */
@Service
public class BloodSugarServiceSup extends RouteDataSourceService {

    @Autowired
    BloodSugarMapper mapper;

    String dbType;

    /**
     * 获取测量时点列表
     *
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<String> getClsdList()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getClsdList();
    }

    /**
     * 获取当前病人当前时间段的血糖列表
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param zyh   住院号
     * @param jgid  机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<BloodSugar> getBloodSugarList(String start, String end, String zyh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getBloodSugarList(start, end, zyh, jgid, dbType);
    }
    public List<PersonBloodSugar> getNeedGetBloodSugarList(String brbq, String jgid)
            throws SQLException, DataAccessException {
//        keepOrRoutingDateSource(DataSource.MOB);
        keepOrRoutingDateSource(DataSource.HRP);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getNeedGetBloodSugarList(brbq, jgid, dbType);
    }
    /**
     * 新增血糖治疗数据
     *
     * @param jlxh 记录序号
     * @param zyh  住院号
     * @param sxbq 书写病区
     * @param brch 病人床号
     * @param sxsj 书写时间
     * @param sxgh 书写工号
     * @param cjsj 创建时间
     * @param cjgh 创建工号
     * @param clsd 测量时点
     * @param clz  测量值
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addBloodSugar(String jlxh, String zyh, String sxbq, String brch,
                             String sxsj, String sxgh, String cjsj, String cjgh,
                             String clsd, String clz, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        switch (clsd) {
            case "空腹":
                return mapper.addBloodSugar1(jlxh, zyh, sxbq, brch, sxsj, sxgh, cjsj, cjgh, clsd, clz, jgid, dbType);
            case "餐后":
                return mapper.addBloodSugar2(jlxh, zyh, sxbq, brch, sxsj, sxgh, cjsj, cjgh, clsd, clz, jgid, dbType);
            case "随机":
                return mapper.addBloodSugar3(jlxh, zyh, sxbq, brch, sxsj, sxgh, cjsj, cjgh, clsd, clz, jgid, dbType);
            default:
                return mapper.addBloodSugar(jlxh, zyh, sxbq, brch, sxsj, sxgh, cjsj, cjgh, clsd, clz, jgid, dbType);
        }

    }

    /**
     * 修改血糖治疗数据
     *
     * @param jlxh 记录序号
     * @param clsd 测量时点
     * @param sxsj 书写时间
     * @param clz  测量值
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editBloodSugar(String jlxh, String clsd, String sxsj, String clz)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        switch (clsd) {
            case "空腹":
                return mapper.editBloodSugar1(jlxh, clsd, sxsj, clz, dbType);
            case "餐后":
                return mapper.editBloodSugar2(jlxh, clsd, sxsj, clz, dbType);
            case "随机":
                return mapper.editBloodSugar3(jlxh, clsd, sxsj, clz, dbType);
            default:
                return mapper.editBloodSugar(jlxh, clsd, sxsj, clz, dbType);
        }

    }

    /**
     * 删除血糖治疗数据
     *
     * @param jlxh 记录序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteBloodSugar(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.deleteBloodSugar(jlxh);
    }
}
