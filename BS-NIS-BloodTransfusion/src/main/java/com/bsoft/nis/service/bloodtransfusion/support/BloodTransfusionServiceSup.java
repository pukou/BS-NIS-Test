package com.bsoft.nis.service.bloodtransfusion.support;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.bloodtransfusion.BloodReciveInfo;
import com.bsoft.nis.domain.bloodtransfusion.BloodTransfusionInfo;
import com.bsoft.nis.domain.bloodtransfusion.BloodTransfusionTourInfo;
import com.bsoft.nis.mapper.bloodtransfusion.BloodTransfusionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Service
public class BloodTransfusionServiceSup extends RouteDataSourceService {

    @Autowired
    BloodTransfusionMapper mapper;

    String dbType;

    /**
     * 获取输血计划列表
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param zyhm  住院号码
     * @param jgid  机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<BloodTransfusionInfo> getBloodTransfusionPlanList(String start, String end, String zyhm, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getBloodTransfusionPlanList(start, end, zyhm, jgid, dbType);
    }

    /**
     * 开始输血
     *
     * @param xdh  血袋号
     * @param xdxh 血袋序号
     * @param zxgh 执行工号
     * @param hdgh 核对工号
     * @param sxsj 输血时间
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int startBloodTransfusion(String xdh, String xdxh, String zxgh, String hdgh, String sxsj, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        dbType = getCurrentDataSourceDBtype();
        return mapper.startBloodTransfusion(xdh, xdxh, zxgh, hdgh, sxsj, jgid, dbType);
    }

    /**
     * 结束输血
     *
     * @param xdh  血袋号
     * @param xdxh 血袋序号
     * @param jsr  结束人
     * @param jssj 结束时间
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int endBloodTransfusion(String xdh, String xdxh, String jsr, String jssj, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        dbType = getCurrentDataSourceDBtype();
        return mapper.endBloodTransfusion(xdh, xdxh, jsr, jssj, jgid, dbType);
    }

    /**
     * 获取输血信息
     *
     * @param xdh  血袋号
     * @param xdxh 血袋序号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<BloodTransfusionInfo> getBloodTransfusionInfoList(String xdh, String xdxh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getBloodTransfusionInfoList(xdh, xdxh, jgid, dbType);
    }

    /**
     * 获取输血签收列表
     *
     * @param start  血袋号
     * @param end    血袋序号
     * @param bqid   住院号
     * @param status 核对工号
     *               0:未签收，1：已签收
     * @param jgid   机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<BloodReciveInfo> getBloodRecieveList(String start, String end, String bqid, String status, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getBloodRecieveList(start, end, bqid, status, jgid, dbType);
    }

    /**
     * 血液交接
     *
     * @param bytm
     * @param sxz  送血人
     * @param jxhs 接血人
     * @param jxrq 接血日期
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int bloodRecieve(String bytm, String sxz, String jxhs, String jxrq)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        dbType = getCurrentDataSourceDBtype();
        return mapper.bloodRecieve(bytm, sxz, jxhs, jxrq, dbType);
    }

    /**
     * 取消交接
     *
     * @param bytm
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int cancleBloodRecieve(String bytm)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        return mapper.cancleBloodRecieve(bytm);
    }

    /**
     * 获取输血巡视记录
     *
     * @param sxdh 输血单号
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<BloodTransfusionTourInfo> getBloodTransfusionTourInfoList(String sxdh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        return mapper.getBloodTransfusionTourInfoList(sxdh, jgid);
    }

    /**
     * 添加输血巡视记录
     *
     * @param bloodTransfusionTourInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addBloodTransfusionTourInfo(BloodTransfusionTourInfo bloodTransfusionTourInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        dbType = getCurrentDataSourceDBtype();
        bloodTransfusionTourInfo.dbtype = dbType;
        return mapper.addBloodTransfusionTourInfo(bloodTransfusionTourInfo);
    }

    /**
     * 修改输血巡视记录
     *
     * @param bloodTransfusionTourInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editBloodTransfusionTourInfo(BloodTransfusionTourInfo bloodTransfusionTourInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        dbType = getCurrentDataSourceDBtype();
        bloodTransfusionTourInfo.dbtype = dbType;
        return mapper.editBloodTransfusionTourInfo(bloodTransfusionTourInfo);
    }

    /**
     * 删除输血巡视记录
     *
     * @param bloodTransfusionTourInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteBloodTransfusionTourInfo(BloodTransfusionTourInfo bloodTransfusionTourInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        dbType = getCurrentDataSourceDBtype();
        bloodTransfusionTourInfo.dbtype = dbType;
        return mapper.deleteBloodTransfusionTourInfo(bloodTransfusionTourInfo);
    }

    /**
     * 血袋上交
     *
     * @param sxdh 输血单号
     * @param yhid 用户id
     * @param sjsj 上交时间
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int saveBloodBagRecieve(String sxdh, String yhid, String sjsj, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.LIS);
        dbType = getCurrentDataSourceDBtype();
        return mapper.saveBloodBagRecieve(sxdh, yhid, sjsj, jgid, dbType);
    }
}
