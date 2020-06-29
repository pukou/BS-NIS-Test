package com.bsoft.nis.service.advicequery.support;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.PhraseModel;
import com.bsoft.nis.domain.adviceqyery.AdviceDetail;
import com.bsoft.nis.domain.adviceqyery.TransfusionPatrolRecord;
import com.bsoft.nis.domain.adviceqyery.TransfusionVo;
import com.bsoft.nis.domain.adviceqyery.db.AdviceBqyzVo;
import com.bsoft.nis.domain.adviceqyery.db.TransfusionInfoVoTemp;
import com.bsoft.nis.mapper.advicequery.AdviceQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Description: 医嘱执行(查询)数据库服务
 * User: 苏泽雄
 * Date: 16/12/16
 * Time: 17:19:00
 */
@Service
public class AdviceQueryServiceSup extends RouteDataSourceService {

	@Autowired
	AdviceQueryMapper mapper;

	/**
	 * 医嘱查询(获取病区医嘱)
	 *
	 * @param zyh
	 * @param kssj
	 * @param jssj
	 * @param now
	 * @param jgid
	 * @param jllx 病人类型  1 成人  2 婴儿
	 * @param lsyz 临时医嘱
	 * @param wxbz 无效标志  0 有效  1 无效
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<AdviceBqyzVo> getAdviceBqyzList(String zyh, String kssj, String jssj,
			String now, String jgid, String jllx, String lsyz, String wxbz)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.HRP);
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getAdviceBqyzList(zyh, kssj, jssj, now, jgid, jllx, lsyz, wxbz, dbtype);
	}

	/**
	 * 获取一条病区医嘱
	 *
	 * @param jlxh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<AdviceBqyzVo> getAdviceOne(String jlxh, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.HRP);
		return mapper.getAdviceOne(jlxh, jgid);
	}

	/**
	 * 获取一条医嘱的执行记录
	 *
	 * @param yzxh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<AdviceDetail> getAdviceRecord(String yzxh, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		return mapper.getAdviceRecord(yzxh, jgid);
	}

	/**
	 * 根据zyh获取输液单
	 *
	 * @param zyh
	 * @param kssj
	 * @param jssj
	 * @param syzt
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<TransfusionVo> getTransfusionListByZyh(String zyh, String kssj, String jssj,
			String syzt, String jgid) throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getTransfusionListByZyh(zyh, kssj, jssj, syzt, jgid, dbtype);
	}

	/**
	 * 根据zyh获取输液单号
	 *
	 * @param zyh
	 * @param kssj
	 * @param jssj
	 * @param syzt
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<String> getSydhByZyh(String zyh, String kssj, String jssj, String syzt,
			String jgid) throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getSydhByZyh(zyh, kssj, jssj, syzt, jgid, dbtype);
	}

	/**
	 * 根据输液单号获取输液单明细
	 *
	 * @param sydhList
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<TransfusionInfoVoTemp> getTransfusionInfoList(List<String> sydhList)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		return mapper.getTransfusionInfoList(sydhList);
	}

	/**
	 * 根据医嘱序号获取医嘱名称(V_MOB_HIS_BQYZ)
	 *
	 * @param jlxh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public String getYzmcByYzxh(String jlxh, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.HRP);
		return mapper.getYzmcByYzxh(jlxh, jgid);
	}

	/**
	 * 获取输液反应类型
	 *
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<PhraseModel> getTransfusionReactionList(String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		return mapper.getTransfusionReactionList(jgid);
	}

	/**
	 * 获取输液巡视记录
	 *
	 * @param sydh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<TransfusionPatrolRecord> getTransfusionPatrolList(String sydh, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		return mapper.getTransfusionPatrolList(sydh, jgid);
	}

	/**
	 * 根据ZYH获取KFDH
	 *
	 * @param kssj
	 * @param jssj
	 * @param zyh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<String> getKfdhListByZyh(String kssj, String jssj, String zyh, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getKfdhListByZyh(zyh, kssj, jssj, jgid, dbtype);
	}

	/**
	 * 根据ZYH获取ZSDH
	 *
	 * @param kssj
	 * @param jssj
	 * @param zyh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<String> getZsdhListByZyh(String kssj, String jssj, String zyh, String jgid)
			throws SQLException, DataAccessException {
		keepOrRoutingDateSource(DataSource.MOB);
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getZsdhListByZyh(zyh, kssj, jssj, jgid, dbtype);
	}
}
