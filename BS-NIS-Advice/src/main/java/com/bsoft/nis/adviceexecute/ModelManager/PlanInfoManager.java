package com.bsoft.nis.adviceexecute.ModelManager;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.PhraseModel;
import com.bsoft.nis.domain.adviceexecute.PlanInfo;
import com.bsoft.nis.mapper.adviceexecute.AdviceExecuteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-21
 * Time: 15:24
 * Version:
 */
@Component
public class PlanInfoManager extends RouteDataSourceService {

    @Autowired
    AdviceExecuteMapper mapper;

    String dbType;

    /**
     * 通过计划号获取计划对象数据
     *
     * @param jhh  计划号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public PlanInfo getPlanInfoByJhh(String jhh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        PlanInfo planInfo = mapper.getPlanInfoByJhh(jhh, jgid);
        return getPlanInfoForSrhdbz(planInfo);
    }

    /**
     * 根据确认单号生成计划列表
     *
     * @param qrdh 确认单号
     * @param gslx 归属类型
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PlanInfo> getPlanInfoListByQrdh(String qrdh, String gslx, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        List<PlanInfo> planInfoList = mapper.getPlanInfoListByQrdh(qrdh, gslx, jgid);
        return getPlanInfoListForSrhdbz(planInfoList);
    }

    /**
     * 生成一个时间点的一组医嘱的计划对象
     *
     * @param yzzh 医嘱组号
     * @param jhsj 计划时间
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PlanInfo> getPlanInfoListByYzzhAndJhsj(String yzzh, String jhsj, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        List<PlanInfo> planInfoList = mapper.getPlanInfoListByYzzhAndJhsj(yzzh, jhsj, jgid, dbType);
        return getPlanInfoListForSrhdbz(planInfoList);
    }

    /**
     * 生成一个日期下的一个时间编号的一组医嘱的计划对象
     *
     * @param yzzh 医嘱组号
     * @param sjbh 时间编号
     * @param jhrq 计划日期
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PlanInfo> getPlanInfoListByYzzhAndSjbh(String yzzh, String sjbh, String jhrq, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        List<PlanInfo> planInfoList = mapper.getPlanInfoListByYzzhAndSjbh(yzzh, sjbh, jhrq, jgid, dbType);
        return getPlanInfoListForSrhdbz(planInfoList);
    }

    /**
     * 生成一个时间点的一条医嘱的计划对象
     *
     * @param yzxh 医嘱序号
     * @param jhsj 计划时间
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public PlanInfo getPlanInfoByYzxhAndJhsj(String yzxh, String jhsj, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        PlanInfo planInfo = mapper.getPlanInfoByYzxhAndJhsj(yzxh, jhsj, jgid, dbType);
        return getPlanInfoForSrhdbz(planInfo);
    }

    /**
     * 生成一个日期下的一个时间编号的一条医嘱的计划对象
     *
     * @param yzxh 医嘱序号
     * @param sjbh 时间编号
     * @param jhrq 计划日期
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public PlanInfo getPlanInfoByYzxhAndSjbh(String yzxh, String sjbh, String jhrq, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        PlanInfo planInfo = mapper.getPlanInfoByYzxhAndSjbh(yzxh, sjbh, jhrq, jgid, dbType);
        return getPlanInfoForSrhdbz(planInfo);
    }

    /**
     * 修改医嘱计划 - 更新核对信息
     *
     * @param planInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editPlanInfoForDoubleCheckControl(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.editPlanInfoForDoubleCheckControl(planInfo);
    }

    /**
     * 修改医嘱计划 - 结束计划
     *
     * @param planInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editPlanInfo(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.editPlanInfo(planInfo);
    }

    /**
     * 修改病区医嘱计划 - 结束计划
     *
     * @param planInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editBQPlanInfo(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.editBQPlanInfo(planInfo);
    }

    /**
     * 修改病区医嘱计划 - 输液相关的计划开始和结束
     * 因为输液相关的数据库执行操作与其他不同，单独独立出来
     *
     * @param planInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editBQPlanInfoForTrans(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.editBQPlanInfoForTrans(planInfo);
    }

    /**
     * 修改医嘱计划列表 - 结束计划
     *
     * @param planInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editPlanInfoList(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.editPlanInfoList(planInfo);
    }

    /**
     * 修改医嘱计划列表 - 结束计划
     *
     * @param planInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editPlanInfoListJs(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.editPlanInfoListJs(planInfo);
    }

    /**
     * 修改医嘱计划列表 - 结束计划
     *
     * @param planInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editPlanInfoListKsAndJs(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.editPlanInfoListKsAndJs(planInfo);
    }

    /**
     * 修改医嘱计划,输液类与计划不通过时间关联
     *
     * @param planInfo (KSSJ, KSGH, QRDH, ZXLX, JSSJ, JSGH, ZXZT, ZDLX; QRDH, JGID)
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public Integer updatePlanInfoForSYD(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.updatePlanInfoForSYD(planInfo);
    }

    public Integer updatePlanInfoForSYD4Clear(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.updatePlanInfoForSYD4Clear(planInfo);
    }

    /**
     * 修改医嘱计划,输液类与计划通过时间关联
     *
     * @param planInfo (KSSJ, KSGH, QRDH, ZXLX, JSSJ, JSGH, ZXZT, ZDLX; QRDH, JGID)
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public Integer updatePlanInfoForSYDByTime(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.updatePlanInfoForSYDByTime(planInfo);
    }
    public Integer updatePlanInfoForSYDByTime4Clear(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.updatePlanInfoForSYDByTime4Clear(planInfo);
    }
    /**
     * 根据计划号获取关联计划号
     *
     * @param jhhList
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<String> getGLJHHByJHH(List<String> jhhList)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getGLJHHByJHH(jhhList);
    }

    /**
     * 获取当前病人当天医嘱计划列表
     *
     * @param zyh   住院号
     * @param start 开始时间
     * @param end   结束时间
     * @param gslx  归属类型
     * @param jgid  机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PlanInfo> getPlanInfoList(String zyh, String start, String end, String gslx, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getPlanInfoList(zyh, start, end, gslx, jgid, dbType);
    }

    /**
     * 获取拒绝医嘱原因列表
     *
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<PhraseModel> getRefuseReasonList(String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getRefuseReasonList(jgid);
    }

    /**
     * 判断当前计划拒绝状态
     *
     * @param jhh  计划号
     * @param dyxh 短语序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int CheckRefuse(String jhh, String dyxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.CheckRefuse(jhh, dyxh);
    }

    /**
     * 计划拒绝执行
     *
     * @param planInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editPlanInfoForRefuseExecut(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        planInfo.dbtype = dbType;
        return mapper.editPlanInfoForRefuseExecut(planInfo);
    }

    /**
     * 计划拒绝执行
     *
     * @param jhh  计划号
     * @param dyxh 短语序号
     * @param jjsj 拒绝时间
     * @param jjgh 拒绝工号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addRefuseExecutReason(String jhh, String dyxh, String jjsj, String jjgh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.addRefuseExecutReason(jhh, dyxh, jjsj, jjgh, dbType);
    }

    /**
     * 根据LXH获取计划类型的TBZXJL(ENR_JHLX)
     *
     * @param lxh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public Float getTbzxjlByLxh(String lxh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getTbzxjlByLxh(lxh, jgid);
    }
    public Integer getNeedSyncDataCount(String zyh, String xmid,String jhrq)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getNeedSyncDataCount(zyh, xmid,jhrq,dbType);
    }
    private List<String> getSqpdList()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        return mapper.getSqpdList();
    }

    /**
     * 双人核对标志赋值
     */
    private PlanInfo getPlanInfoForSrhdbz(PlanInfo planInfo)
            throws SQLException, DataAccessException {
        List<String> sqpdList = getSqpdList();
        for (String ypxh : sqpdList) {
            if (ypxh.equals(planInfo.XMXH)) {
                planInfo.SRHDBZ = "1";
                break;
            }
        }
        return planInfo;
    }

    /**
     * 双人核对标志赋值
     */
    private List<PlanInfo> getPlanInfoListForSrhdbz(List<PlanInfo> planInfoList)
            throws SQLException, DataAccessException {
        List<String> sqpdList = getSqpdList();
        boolean isFind = false;
        for (PlanInfo planInfo : planInfoList) {
            for (String ypxh : sqpdList) {
                if (ypxh.equals(planInfo.XMXH)) {
                    isFind = true;
                    break;
                }
            }
        }
        if (isFind) {
            for (PlanInfo planInfo : planInfoList) {
                planInfo.SRHDBZ = "1";
            }
        }
        return planInfoList;
    }
}
