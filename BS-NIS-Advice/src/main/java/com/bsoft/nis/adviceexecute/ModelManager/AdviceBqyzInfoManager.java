package com.bsoft.nis.adviceexecute.ModelManager;

import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.AdviceBqyzInfo;
import com.bsoft.nis.mapper.adviceexecute.AdviceExecuteMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 病区医嘱
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-21
 * Time: 15:49
 * Version:
 */
@Component
public class AdviceBqyzInfoManager extends RouteDataSourceService {

    @Autowired
    AdviceExecuteMapper mapper;

    String dbType;

    @Autowired
    DictCachedHandler handler; // 缓存处理器

    /**
     * 通过医嘱序号获取病区医嘱对象数据
     *
     * @param yzxh 医嘱序号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public AdviceBqyzInfo getAdviceBqyzInfo(String zyh,String yzxh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        AdviceBqyzInfo adviceBqyzInfo = mapper.getAdviceBqyzInfo(zyh,yzxh, jgid);
        adviceBqyzInfo.JLDW = handler.getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", adviceBqyzInfo.YPXH, "JLDW");
        adviceBqyzInfo.SLDW = handler.getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", adviceBqyzInfo.YPXH, "BFDW");
        return adviceBqyzInfo;
    }

    /**
     * 通过医嘱组号获取病区医嘱对象列表数据
     *
     * @param yzzh 医嘱组号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<AdviceBqyzInfo> getAdviceBqyzInfoList(String zyh,String yzzh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        //  2018-03-15 14:54:17 【医嘱执行 费用信息  也进行了核对，不能继续执行下去】
        List<AdviceBqyzInfo> adviceBqyzInfoList = mapper.getAdviceBqyzInfoList(zyh,yzzh, jgid);
        List<AdviceBqyzInfo> adviceBqyzInfoListNew = new ArrayList<>();
        for (AdviceBqyzInfo adviceBqyzInfo : adviceBqyzInfoList) {
            //YPLX > 0
             if (!StringUtils.isEmpty(adviceBqyzInfo.YPLX)&&Integer.valueOf(adviceBqyzInfo.YPLX)>0){
                 adviceBqyzInfo.JLDW = handler.getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", adviceBqyzInfo.YPXH, "JLDW");
                 adviceBqyzInfo.SLDW = handler.getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", adviceBqyzInfo.YPXH, "BFDW");
                 adviceBqyzInfoListNew.add(adviceBqyzInfo);
            }
        }
        return adviceBqyzInfoListNew;
    }

    /**
     * 修改病区医嘱
     *
     * @param adviceBqyzInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editAdviceBqyzInfo(AdviceBqyzInfo adviceBqyzInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        dbType = getCurrentDataSourceDBtype();
        adviceBqyzInfo.dbtype = dbType;
        //TODO：待确认ZY_BQYZ表中不包含HSZXGH2字段
//        if(StringUtils.isBlank(adviceBqyzInfo.HSZXGH2)) {
//            return mapper.editAdviceBqyzInfo(adviceBqyzInfo);
//        } else {
//            return mapper.editAdviceBqyzInfoHD(adviceBqyzInfo);
//        }
        return mapper.editAdviceBqyzInfo(adviceBqyzInfo);
    }
}
