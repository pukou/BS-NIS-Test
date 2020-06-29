package com.bsoft.nis.service.handover.support;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.handover.*;
import com.bsoft.nis.mapper.handover.HandOverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2017-02-15
 * Time: 10:39
 * Version:
 */
@Service
public class HandOverServiceSup extends RouteDataSourceService {

    @Autowired
    HandOverMapper mapper;

    String dbType;

    /**
     * 获取模板数据 - 样式列表
     *
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HandOverForm> getHandOverFormList(String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHandOverFormList(jgid);
    }

    /**
     * 获取当前病人指定模板下的记录数据
     *
     * @param ysxh 样式序号
     * @param ztbz 状态标志
     * @param zyh  住院号
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HandOverRecord> getHandOverRecordList(String ysxh, String ztbz, String zyh, String bqid, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHandOverRecordList(ysxh, ztbz, zyh, bqid, jgid);
    }
    /**
     * 获取当前病人指定模板下的记录数据
     *
     * @param ysxh 样式序号
     * @param ztbz 状态标志
     * @param zyh  住院号
     * @param ssks 手术科室
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HandOverRecord> getHandOverRecordListBySSKS(String ysxh, String ztbz, String zyh,String ssks, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHandOverRecordListBySSKS(ysxh, ztbz, zyh,ssks, jgid);
    }
    /**
     * 根据记录序号获取记录数据
     *
     * @param jlxh 记录序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public HandOverRecord getHandOverRecord(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHandOverRecord(jlxh);
    }

    /**
     * 根据记录序号获取记录项目数据
     *
     * @param jlxh 记录序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HandOverProject> getHandOverRecordProjectList(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHandOverRecordProjectList(jlxh);
    }

    /**
     * 根据记录序号获取记录选项数据
     *
     * @param jlxh 记录序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HandOverOption> getHandOverRecordOptionList(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHandOverRecordOptionList(jlxh);
    }

    /**
     * 获取模板数据 - 样式数据
     *
     * @param ysxh 样式序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public HandOverForm getHandOverForm(String ysxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHandOverForm(ysxh);
    }

    /**
     * 获取模板数据 - 分类列表
     *
     * @param ysxh 样式序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HandOverClassify> getHandOverClassifyList(String ysxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHandOverClassifyList(ysxh);
    }

    /**
     * 获取模板数据 - 项目列表
     *
     * @param ysxh 样式序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HandOverProject> getHandOverProjectList(String ysxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHandOverProjectList(ysxh);
    }

    /**
     * 获取模板数据 - 选项列表
     *
     * @param ysxh 样式序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HandOverOption> getHandOverOptionList(String ysxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHandOverOptionList(ysxh);
    }

    /**
     * 添加交接单数据 - 主记录
     *
     * @param handOverRecord
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addHandOverRecord(HandOverRecord handOverRecord)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        handOverRecord.dbtype = dbType;
        return mapper.addHandOverRecord(handOverRecord);
    }

    /**
     * 添加交接单数据 - 主记录 - 操作方
     *
     * @param handOverRecord
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editHandOverRecordForSender(HandOverRecord handOverRecord)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        handOverRecord.dbtype = dbType;
        return mapper.editHandOverRecordForSender(handOverRecord);
    }

    /**
     * 修改交接单数据 - 主记录 - 接受方
     *
     * @param handOverRecord
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editHandOverRecordForReceiver(HandOverRecord handOverRecord)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        handOverRecord.dbtype = dbType;
        return mapper.editHandOverRecordForReceiver(handOverRecord);
    }

    /**
     * 发送交接单 - 主记录
     *
     * @param jlxh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int sendHandOverRecord(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.sendHandOverRecord(jlxh);
    }

    /**
     * 作废交接单 - 主记录
     *
     * @param jlxh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int delHandOverRecord(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.delHandOverRecord(jlxh);
    }

    /**
     * 添加交接单记录项目
     *
     * @param handOverProject
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addHandOverProject(HandOverProject handOverProject)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.addHandOverProject(handOverProject);
    }

    /**
     * 修改交接单记录项目
     *
     * @param handOverProject
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editHandOverProject(HandOverProject handOverProject)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.editHandOverProject(handOverProject);
    }

    /**
     * 删除交接单记录项目
     *
     * @param jlxm
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int delHandOverProject(String jlxm)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.delHandOverProject(jlxm);
    }

    /**
     * 删除交接单记录项目
     *
     * @param jlxh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int delHandOverProjectByJlxh(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.delHandOverProjectByJlxh(jlxh);
    }

    /**
     * 添加交接单记录选项
     *
     * @param handOverOption
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addHandOverOption(HandOverOption handOverOption)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.addHandOverOption(handOverOption);
    }

    /**
     * 修改交接单记录选项
     *
     * @param handOverOption
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editHandOverOption(HandOverOption handOverOption)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.editHandOverOption(handOverOption);
    }

    /**
     * 删除交接单记录选项
     *
     * @param jlxx
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int delHandOverOption(String jlxx)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.delHandOverOption(jlxx);
    }

    /**
     * 删除交接单记录选项
     *
     * @param jlxm
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int delHandOverOptionByJlxm(String jlxm)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.delHandOverOptionByJlxm(jlxm);
    }

    /**
     * 删除交接单记录选项
     *
     * @param jlxh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int delHandOverOptionByJlxh(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.delHandOverOptionByJlxh(jlxh);
    }

    /**
     * 获取关联数据 - 生命体征
     *
     * @param txsj 填写时间
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<RelativeItem> getLifeSignDataList(String txsj, String zyh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getLifeSignDataList(txsj, zyh, jgid, dbType);
    }

    /**
     * 获取关联数据 - 生命体征
     *
     * @param txsj 填写时间
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<RelativeItem> getRiskDataList(String txsj, String zyh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getRiskDataList(txsj, zyh, jgid, dbType);
    }

    /**
     * 通过住院号获取当前诊断
     *
     * @param zyh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getDqzd(String zyh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        return mapper.getDqzd(zyh);
    }
}
