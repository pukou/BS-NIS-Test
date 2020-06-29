package com.bsoft.nis.adviceexecute.ModelManager;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.*;
import com.bsoft.nis.domain.timingserver.transferdata.OralPackage;
import com.bsoft.nis.mapper.adviceexecute.AdviceExecuteMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 口服单管理器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-22
 * Time: 17:53
 * Version:
 */
@Component
public class OralInfoManager extends RouteDataSourceService {

    @Autowired
    AdviceExecuteMapper mapper;

    String dbType;

    /**
     * 通过计划号获取口服单对象数据
     *
     * @param jhh                计划号
     * @param oralJoinPlanByTime
     * @param jgid               机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public OralInfo getOralInfoByJhh(String jhh, boolean oralJoinPlanByTime, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        List<OralModelInfo> oralModelInfoList;
        if (oralJoinPlanByTime) {
            oralModelInfoList = mapper.getOralModelInfoListByJhhAndSjd(jhh, jgid);
        } else {
            oralModelInfoList = mapper.getOralModelInfoListByJhhAndSjbh(jhh, jgid);
        }
        OralInfo oralInfo = getOralInfo(oralModelInfoList);
        return oralInfo;
    }

    /**
     * 根据条码生成口服单
     *
     * @param barcode       条码
     * @param prefix        条码编号
     * @param oralUsePrefix
     * @param jgid          机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public OralInfo getOralInfoByBarcode(String barcode, String prefix, boolean oralUsePrefix, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        String tmbh;
        if (oralUsePrefix) {
            tmbh = prefix + barcode;
        } else {
            tmbh = barcode;
        }
        List<OralModelInfo> oralModelInfoList = mapper.getOralModelInfoListByBarcode(tmbh, jgid);
        OralInfo oralInfo = getOralInfo(oralModelInfoList);
        return oralInfo;
    }

    /**
     * 根据口服单号生成口服单
     *
     * @param kfdh 口服单号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public OralInfo getOralInfoByKfdh(String kfdh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        List<OralModelInfo> oralModelInfoList = mapper.getOralModelInfoListByKfdh(kfdh, jgid);
        OralInfo oralInfo = getOralInfo(oralModelInfoList);
        return oralInfo;
    }

    /**
     * 根据口服单号生成口服明细数据
     *
     * @param kfdh 口服单号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<FlowRecordDetailInfo> getFlowRecordDetailInfoListForOral(String kfdh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        //return mapper.getFlowRecordDetailInfoListForOral(kfdh, jgid);
        List<FlowRecordDetailInfo> frdis = mapper.getFlowRecordDetailInfoListForOral(kfdh, jgid);
        if(frdis != null && frdis.size() > 0) {
            keepOrRoutingDateSource(DataSource.HRP);
            List<AdviceBqyzExt> exts = mapper.getAdviceBqyzExt(frdis);
            for (FlowRecordDetailInfo info : frdis) {
                for (AdviceBqyzExt item : exts) {
                    if (info.YZXH.equals(item.YZXH)) {
                        info.YZMC = item.YZMC;
                        break;
                    }
                }
            }
        }
        return frdis;
    }

    /**
     * 更新口服单信息 - 更新核对信息
     *
     * @param hdsj 核对时间
     * @param hdgh 核对工号
     * @param info 口服单
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int updateOralInfoForDoubleCheckControl(String hdsj, String hdgh, OralInfo info, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        int count = 0;
        for(OralPackageInfo packageInfo: info.Packages) {
            count += mapper.editOralInfoForDoubleCheckControl(hdsj, hdgh, packageInfo.KFMX, dbType);
        }
        return count;
    }

    /**
     * 修改口服包装数据 - 执行
     *
     * @param oralPackageInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editOralPackageInfo(OralPackageInfo oralPackageInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        oralPackageInfo.dbtype = dbType;
        return mapper.editOralPackageInfo(oralPackageInfo);
    }

    //批量更新ENR_KFBZ
    public int editOralPackageInfo(List<OralPackageInfo> list)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();

        int nums = 0;

        for(OralPackageInfo oralPackageInfo : list){
            oralPackageInfo.dbtype = dbType;
            int ret = mapper.editOralPackageInfo(oralPackageInfo);
            if( ret > 0) nums ++;
        }

        return nums;
    }

    //判断KFMX为key的数据在pkgs中是否存在
    private boolean inListForKfmx(List<OralPackageInfo> pkgs, String kfmx){
        if(pkgs == null || pkgs.size() == 0) return false;

        for(OralPackageInfo item : pkgs){
            if(kfmx.equals(item.KFMX)) return true;
        }

        return false;
    }

    private OralInfo getOralInfo(List<OralModelInfo> oralModelInfoList) {
        OralInfo oralInfo = null;
        if (oralModelInfoList != null && oralModelInfoList.size() > 0) {
            oralInfo = new OralInfo();
            oralInfo.KFDH = oralModelInfoList.get(0).KFDH;
            oralInfo.ZYH = oralModelInfoList.get(0).ZYH;
            oralInfo.YPYF = oralModelInfoList.get(0).YPYF;
            oralInfo.KFSJ = oralModelInfoList.get(0).KFSJ;
            oralInfo.Packages = new ArrayList<>();
            String kfmx = "";
            OralPackageInfo oralPackageInfo = null;
            for (OralModelInfo info : oralModelInfoList) {
                //if (StringUtils.isBlank(kfmx)) {
                if (!inListForKfmx(oralInfo.Packages, info.KFMX)) {
                    //在列表中不存在的才需要添加
                    oralPackageInfo = new OralPackageInfo();
                    oralPackageInfo.KFMX = info.KFMX;
                    oralPackageInfo.TMBH = info.TMBH;
                    oralPackageInfo.KFZT = info.KFZT;
                    oralPackageInfo.ZXSJ = info.ZXSJ;
                    oralPackageInfo.ZXGH = info.ZXGH;
                    oralPackageInfo.HDSJ = info.HDSJ;
                    oralPackageInfo.HDGH = info.HDGH;
                    oralPackageInfo.ZFBZ = info.ZFBZ;
                    oralPackageInfo.Details = new ArrayList<>();
                    oralInfo.Packages.add(oralPackageInfo);
                    kfmx = oralPackageInfo.KFMX;
                }
                if (oralPackageInfo != null) {
                    OralDetailInfo oralDetailInfo = new OralDetailInfo();
                    oralDetailInfo.JLDW = info.JLDW;
                    oralDetailInfo.SLDW = info.SLDW;
                    oralDetailInfo.YCJL = info.BZJL;
                    oralDetailInfo.YCSL = info.BZSL;
                    oralDetailInfo.YZXH = info.YZXH;
                    oralDetailInfo.SJBH = info.SJBH;
                    oralDetailInfo.SJMC = info.SJMC;
                    oralDetailInfo.BZMX = info.BZMX;
                    oralDetailInfo.YPXH = info.YPXH;
                    oralPackageInfo.Details.add(oralDetailInfo);
                }
            }

        }
        return oralInfo;
    }
}
