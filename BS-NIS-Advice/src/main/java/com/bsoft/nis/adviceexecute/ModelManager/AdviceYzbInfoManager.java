package com.bsoft.nis.adviceexecute.ModelManager;

import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.AdviceBqyzInfo;
import com.bsoft.nis.domain.adviceexecute.AdviceYzbInfo;
import com.bsoft.nis.mapper.adviceexecute.AdviceExecuteMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-21
 * Time: 15:59
 * Version:
 */
@Component
public class AdviceYzbInfoManager extends RouteDataSourceService {

    @Autowired
    AdviceExecuteMapper mapper;

    String dbType;

    @Autowired
    DictCachedHandler handler; // 缓存处理器

    /**
     * 通过医嘱序号获取医嘱本对象数据
     *
     * @param yzxh 医嘱序号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public AdviceYzbInfo getAdviceYzbInfo(String zyh,String yzxh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        AdviceYzbInfo adviceYzbInfo = mapper.getAdviceYzbInfo(zyh,yzxh, jgid);
        if (adviceYzbInfo!=null) {
            adviceYzbInfo.JLDW = handler.getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", adviceYzbInfo.YPXH, "JLDW");
            adviceYzbInfo.SLDW = handler.getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", adviceYzbInfo.YPXH, "BFDW");
        }
        return adviceYzbInfo;
    }

    /**
     * 通过医嘱序号获取医嘱本对象数据
     *
     * @param yzzh 医嘱组号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<AdviceYzbInfo> getAdviceYzbInfoList(String zyh,String yzzh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        List<String> ysyzbhList = new ArrayList<>();
        List<AdviceBqyzInfo> adviceBqyzInfoList = mapper.getAdviceBqyzInfoList(zyh,yzzh, jgid);
        for (AdviceBqyzInfo info : adviceBqyzInfoList) {
            ysyzbhList.add(info.YSYZBH);
        }
        if (ysyzbhList.isEmpty()) {
            ysyzbhList.add("-1");
        }
        List<AdviceYzbInfo> adviceYzbInfoList = mapper.getAdviceYzbInfoList(zyh,ysyzbhList, jgid);
        for (AdviceYzbInfo adviceYzbInfo : adviceYzbInfoList) {
            adviceYzbInfo.JLDW = handler.getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", adviceYzbInfo.YPXH, "JLDW");
            adviceYzbInfo.SLDW = handler.getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", adviceYzbInfo.YPXH, "BFDW");
        }
        return adviceYzbInfoList;
    }

    /**
     * 修改医嘱本
     *
     * @param adviceYzbInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editAdviceYzbInfo(AdviceYzbInfo adviceYzbInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        dbType = getCurrentDataSourceDBtype();
        adviceYzbInfo.dbtype = dbType;
        //TODO：待确认EMR_YZB表中不包含HSZXGH2字段
//        if (StringUtils.isBlank(adviceYzbInfo.HSZXGH2)) {
//            return mapper.editAdviceYzbInfo(adviceYzbInfo);
//        } else {
//            return mapper.editAdviceYzbInfoHD(adviceYzbInfo);
//        }
        return mapper.editAdviceYzbInfo(adviceYzbInfo);
    }

    /**
     * 从EMR_YZB获取医嘱执行时间和医嘱期效
     *
     * @param yzbxh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public AdviceYzbInfo getZxsjOfAdviceYzb(String yzbxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        return mapper.getZxsjOfAdviceYzb(yzbxh);
    }
}
