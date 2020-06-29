package com.bsoft.nis.adviceexecute.ModelManager;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.AdviceBqyzExt;
import com.bsoft.nis.domain.adviceexecute.FlowRecordDetailInfo;
import com.bsoft.nis.domain.adviceexecute.TransfusionInfo;
import com.bsoft.nis.domain.adviceqyery.db.TransfusionInfoVoTemp;
import com.bsoft.nis.domain.adviceqyery.db.TransfusionPatrolRecordVo;
import com.bsoft.nis.mapper.adviceexecute.AdviceExecuteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-23
 * Time: 14:25
 * Version:
 */
@Component
public class TransfuseInfoManager extends RouteDataSourceService {

	@Autowired
	AdviceExecuteMapper mapper;

	String dbType;

	/**
	 * 通过计划号获取输液单单对象数据
	 *
	 * @param jhh                       计划号
	 * @param transfusionJoinPlanByTime
	 * @param jgid                      机构id
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public TransfusionInfo getTransfusionInfoByJhh(String jhh, boolean transfusionJoinPlanByTime,
			String jgid) throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		TransfusionInfo transfusionInfo;
		if (transfusionJoinPlanByTime) {
			transfusionInfo = mapper.getTransfusionInfoByJhhAndSjd(jhh, jgid);
		} else {
			transfusionInfo = mapper.getTransfusionInfoByJhhAndSjbh(jhh, jgid);
		}
		if(transfusionInfo != null) {
			transfusionInfo.Details = mapper
					.getTransfusionDetailInfoList(transfusionInfo.SYDH, jgid);
		}

		return transfusionInfo;
	}

	/**
	 * 通过条码获取输液单单对象数据
	 *
	 * @param barcode              条码
	 * @param prefix               条码前缀
	 * @param transfusionUsePrefix
	 * @param jgid                 机构id
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public TransfusionInfo getTransfusionInfoByBarcode(String barcode, String prefix,
			boolean transfusionUsePrefix, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		String tmbh;
		if (transfusionUsePrefix) {
			tmbh = prefix + barcode;
		} else {
			tmbh = barcode;
		}
		TransfusionInfo transfusionInfo = mapper.getTransfusionInfoByBarcode(tmbh, jgid);
		if (transfusionInfo != null) {
			transfusionInfo.Details = mapper
					.getTransfusionDetailInfoList(transfusionInfo.SYDH, jgid);
		}

		return transfusionInfo;
	}

	/**
	 * 通过条码获取输液单单对象数据
	 *
	 * @param sydh 输液单号
	 * @param jgid 机构id
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public TransfusionInfo getTransfusionInfoBySydh(String sydh, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		TransfusionInfo transfusionInfo = mapper.getTransfusionInfoBySydh(sydh, jgid);
		if (transfusionInfo != null) {
			transfusionInfo.Details = mapper.getTransfusionDetailInfoList(sydh, jgid);
		}
		return transfusionInfo;
	}

	/**
	 * 根据住院号生成所有输液中的输液明细数据
	 *
	 * @param zyh  住院号
	 * @param jgid 机构id
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<FlowRecordDetailInfo> getFlowRecordDetailInfoListForTran(String zyh,
			String jgid) throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		//return mapper.getFlowRecordDetailInfoListForTran(zyh, jgid);
		List<FlowRecordDetailInfo> frdis = mapper.getFlowRecordDetailInfoListForTran(zyh, jgid);
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
	 * 根据住院号生成所有输液中的输液单数据
	 *
	 * @param zyh  住院号
	 * @param jgid 机构id
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<String> getSydhListForDp(String zyh, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		return mapper.getSydhListForDp(zyh, jgid);
	}

	/**
	 * 更新口服单信息 - 更新核对信息
	 *
	 * @param hdsj 核对时间
	 * @param hdgh 核对工号
	 * @param info 输液单
	 * @param jgid 机构id
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public int editTransfusionInfoForDoubleCheckControl(String hdsj, String hdgh,
			TransfusionInfo info, String jgid) throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		dbType = getCurrentDataSourceDBtype();
		return mapper
				.editTransfusionInfoForDoubleCheckControl(hdsj, hdgh, info.SYDH, jgid, dbType);
	}

	/**
	 * 结束输液暂停
	 *
	 * @param sydh
	 * @param jssj
	 * @param jsgh
	 * @param jgid
	 * @return
	 */
	public Integer transfuseStopEnd(String sydh, String jssj, String jsgh, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		dbType = getCurrentDataSourceDBtype();
		return mapper.transfuseStopEnd(sydh, jssj, jsgh, jgid, dbType);
	}

	/**
	 * 更新输液单的输液状态
	 *
	 * @param sydh
	 * @param syzt
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public Integer updateTransfusionStatus(String sydh, String syzt, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		return mapper.updateTransfusionStatus(sydh, syzt, jgid);
	}

	/**
	 * 更新输液单
	 * 输液开始 写开始时间 打执行中标志 首瓶标志=1 末瓶标志=0
	 * 输液结束 写结束时间，打完成标志 末瓶标志=1
	 * 输液接瓶 本瓶写开始时间 打执行中标志 首瓶标志=0 末瓶标志=0 / 上瓶写结束时间，打完成标志
	 *
	 * @param transfusionInfo (KSSJ, KSGH, SPBZ, ZXDH, JSSJ, JSGH, MPBZ, PJDS, SYZT; SYDH, JGID)
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public Integer editTransfusion(TransfusionInfo transfusionInfo)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		transfusionInfo.dbtype = getCurrentDataSourceDBtype();
		return mapper.editTransfusion(transfusionInfo);
	}
	public Integer editTransfusion4Clear(TransfusionInfo transfusionInfo)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		transfusionInfo.dbtype = getCurrentDataSourceDBtype();
		return mapper.editTransfusion4Clear(transfusionInfo);
	}
	/**
	 * 获取输液剂量单位信息(一次剂量，剂量单位，开始时间)
	 *
	 * @param sydh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<TransfusionInfoVoTemp> getDropSpeedInfo(String sydh, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		return mapper.getDropSpeedInfo(sydh, jgid);
	}

	/**
	 * 获取输液状态和执行单号
	 *
	 * @param sydh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public TransfusionInfo transfuseStopCheck(String sydh, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		return mapper.transfuseStopCheck(sydh, jgid);
	}

    /**
     * 根据住院号生成所有输液中或者暂停中的输液单数据
     *
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<TransfusionInfo> getTransfusionInfoListByZyh(String zyh, String jgid, String syrq)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
		String dbtype = getCurrentDataSourceDBtype();
        return mapper.getTransfusionInfoListByZyh(zyh, jgid,syrq,dbtype);
    }

	/*
     升级编号【56010053】============================================= start
     多瓶超过2瓶转接瓶后提示选择接哪瓶的问题
     ================= Classichu 2017/11/14 16:25

     */
	public List<TransfusionInfo> getTransfusionInfoListByZyh4TransfuseExecut(String zyh, String jgid,String syrq)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getTransfusionInfoListByZyh4TransfuseExecut(zyh, jgid, syrq,dbtype);
	}
	/* =============================================================== end */
	public List<TransfusionInfo> getTransfusionInfoListByZyh4TransfuseExecutAll(String zyh, String jgid,String syrq)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getTransfusionInfoListByZyh4TransfuseExecutAll(zyh, jgid, syrq,dbtype);
	}
	/**
	 * 新建输液巡视记录
	 *
	 * @param record
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public Integer insertTransfusionPatrol(TransfusionPatrolRecordVo record)
			throws SQLException, DataAccessException {
		record.dbtype = getCurrentDataSourceDBtype();
		keepOrRoutingDateSource(DataSource.MOB);
		return mapper.insertTransfusionPatrol(record);
	}
}
