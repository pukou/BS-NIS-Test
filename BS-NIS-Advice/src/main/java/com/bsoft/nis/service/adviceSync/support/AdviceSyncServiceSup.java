package com.bsoft.nis.service.adviceSync.support;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.PlanInfo;
import com.bsoft.nis.domain.adviceqyery.db.AdviceBqyzVo;
import com.bsoft.nis.mapper.advicesync.AdviceSyncMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 医嘱同步（病区医嘱）主服务
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-03-01
 * Time: 16:58
 * Version:
 */
@Service
public class AdviceSyncServiceSup extends RouteDataSourceService {

    @Autowired
    AdviceSyncMapper mapper;

    /**
     * 根据住院号获取病区医嘱计划列表
     *
     * @param zyh
     * @param gslxList
     * @param jhrq
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PlanInfo> getBQPlanInfoListByZyh(String zyh, List<String> gslxList, String jhrq, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getBQPlanInfoListByZyh(zyh, gslxList, jhrq, jgid, dbtype);
    }

    /**
     * 根据计划号列表获取病区医嘱计划列表
     *
     * @param jhhList
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PlanInfo> getBQPlanInfoListByJhhList(List<String> jhhList)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        return mapper.getBQPlanInfoListByJhhList(jhhList);
    }

    /**
     * 根据病区获取病区医嘱计划列表
     *
     * @param bqdm
     * @param gslxList
     * @param jhrq
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PlanInfo> getBQPlanInfoListByBqdm(String bqdm, List<String> gslxList, String jhrq, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getBQPlanInfoListByBqdm(bqdm, gslxList, jhrq, jgid, dbtype);
    }

    /**
     * 根据住院号获取医嘱计划列表
     *
     * @param zyh
     * @param gslxList
     * @param jhrq
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PlanInfo> getPlanInfoListByZyh(String zyh, List<String> gslxList, String jhrq, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getPlanInfoListByZyh(zyh, gslxList, jhrq, jgid, dbtype);
    }

    /**
     * 根据关联计划号列表获取医嘱计划列表
     *
     * @param jhhList
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PlanInfo> getPlanInfoListByGljhhList(List<String> jhhList)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getPlanInfoListByGljhhList(jhhList);
    }

    /**
     * 根据病区获取医嘱计划列表
     *
     * @param bqdm
     * @param gslxList
     * @param jhrq
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PlanInfo> getPlanInfoListByBqdm(String bqdm, List<String> gslxList, String jhrq, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getPlanInfoListByBqdm(bqdm, gslxList, jhrq, jgid, dbtype);
    }

    /**
     * 根据病区代码获取该病区定制的归属类型列表
     *
     * @param bqdm
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<String> getGslxListByBqdm(String bqdm)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getGslxListByBqdm(bqdm);
    }

    /**
     * 根据病区代码获取该病区定制的归属类型列表
     *
     * @param planInfoList
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addPlanInfoList(List<PlanInfo> planInfoList)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        String dbtype = getCurrentDataSourceDBtype();
        Map map = new HashMap();
        map.put("dbtype",dbtype);
        map.put("planInfoList",planInfoList);
        return mapper.addPlanInfoList(map);
    }

    /**
     * 作废医嘱计划
     *
     * @param gljhh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editPlanInfo(String gljhh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.editPlanInfo(gljhh);
    }

    /**
     * 更新同步标识
     *
     * @param jhh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editBQPlanInfo(String jhh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.editBQPlanInfo(jhh);
    }
}
