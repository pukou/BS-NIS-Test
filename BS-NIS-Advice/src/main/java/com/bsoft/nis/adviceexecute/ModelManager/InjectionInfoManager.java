package com.bsoft.nis.adviceexecute.ModelManager;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.InjectionInfo;
import com.bsoft.nis.mapper.adviceexecute.AdviceExecuteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * Description: 注射单
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-21
 * Time: 16:19
 * Version:
 */
@Component
public class InjectionInfoManager extends RouteDataSourceService {

    @Autowired
    AdviceExecuteMapper mapper;

    String dbType;

    /**
     * 通过计划号获取注射单对象数据
     *
     * @param jhh                     计划号
     * @param injectionJoinPlanByTime
     * @param jgid                    机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public InjectionInfo getInjectionInfoByJhh(String jhh, boolean injectionJoinPlanByTime, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        InjectionInfo injectionInfo;
        if (injectionJoinPlanByTime) {
            injectionInfo = mapper.getInjectionInfoByJhhAndSjd(jhh, jgid);
        } else {
            injectionInfo = mapper.getInjectionInfoByJhhAndSjbh(jhh, jgid);
        }
        if (injectionInfo != null) {
	        injectionInfo.Details = mapper.getInjectionDetailInfoList(injectionInfo.ZSDH, jgid);
        }

        return injectionInfo;
    }

    /**
     * 通过条码获取注射单对象数据
     *
     * @param barcode            条码
     * @param prefix             前缀
     * @param injectionUsePrefix
     * @param jgid               机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public InjectionInfo getInjectionInfoByBarcode(String barcode, String prefix, boolean injectionUsePrefix, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        String tmbh;
        if (injectionUsePrefix) {
            tmbh = prefix + barcode;
        } else {
            tmbh = barcode;
        }
        InjectionInfo injectionInfo = mapper.getInjectionInfoByBarcode(tmbh, jgid);
        if (injectionInfo != null) {
	        injectionInfo.Details = mapper.getInjectionDetailInfoList(injectionInfo.ZSDH, jgid);
        }

        return injectionInfo;
    }

    /**
     * 更新注射单信息 - 更新核对信息
     *
     * @param hdsj 核对时间
     * @param hdgh 核对工号
     * @param info 注射单
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editInjectionInfoForDoubleCheckControl(String hdsj, String hdgh, InjectionInfo info, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.editInjectionInfoForDoubleCheckControl(hdsj, hdgh, info.ZSDH, jgid, dbType);
    }

    /**
     * 更新注射单信息 - 结束操作
     *
     * @param injectionInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editInjectionInfoJs(InjectionInfo injectionInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        injectionInfo.dbtype = dbType;
        return mapper.editInjectionInfoJs(injectionInfo);
    }

    /**
     * 更新注射单信息 - 开始+结束操作
     *
     * @param injectionInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editInjectionInfoKsAndJs(InjectionInfo injectionInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        injectionInfo.dbtype = dbType;
        return mapper.editInjectionInfoKsAndJs(injectionInfo);
    }
}
