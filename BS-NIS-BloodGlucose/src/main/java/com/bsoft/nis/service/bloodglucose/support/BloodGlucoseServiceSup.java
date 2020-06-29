package com.bsoft.nis.service.bloodglucose.support;

import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.bloodglucose.BloodGlucoseDetail;
import com.bsoft.nis.domain.bloodglucose.db.BGDetailVo;
import com.bsoft.nis.domain.bloodglucose.db.BGRecordVo;
import com.bsoft.nis.mapper.bloodglucose.BloodGlucoseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Description: 血糖治疗记录单数据库服务
 * User: 苏泽雄
 * Date: 16/12/23
 * Time: 14:04:54
 */
@Service
public class BloodGlucoseServiceSup extends RouteDataSourceService {

	@Autowired
	BloodGlucoseMapper mapper;

	/**
	 * 获取血糖记录明细
	 *
	 * @param zyh
	 * @param jhrq
	 * @param brbq
	 * @param xmlx
	 * @param xmxh @return    @throws SQLException
	 * @param jgid
	 * @throws DataAccessException
	 */
	public List<BloodGlucoseDetail> getBloodGlucoseDetail(String zyh, String jhrq, String brbq,
			String xmlx, String xmxh, String jgid) throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getBloodGlucoseDetail(zyh, jhrq, brbq, xmlx, xmxh, jgid, dbtype);
	}

	/**
	 * 获取一天的血糖治疗的历史记录
	 *
	 * @param zyh
	 * @param jhrq
	 * @param brbq
	 * @param xmlx @return
	 * @param jgid
	 */
	public List<BloodGlucoseDetail> getBGHistoryByJlxh(String zyh, String jhrq, String brbq,
			String xmlx, String jgid) throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getBGHistoryByJlxh(zyh, jhrq, brbq, xmlx, jgid, dbtype);
	}

	/**
	 * 获取时间段内血糖治疗的历史记录
	 *
	 * @param zyh
	 * @param kssj
	 * @param jssj
	 * @param brbq
	 * @param jgid
	 * @return
	 */
	public List<BloodGlucoseDetail> getBGHistoryList(String zyh, String kssj, String jssj,
			String brbq, String jgid) throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getBGHistoryList(zyh, kssj, jssj, brbq, jgid, dbtype);
	}

	/**
	 * 保存血糖记录明细
	 *
	 * @param addList    血糖新增列表
	 * @param updateList 血糖修改列表
	 * @param checkList  胰岛素执行列表
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer saveBloodGlucose(List<BGDetailVo> addList, List<BGDetailVo> updateList,
			List<BGDetailVo> checkList) throws SQLException, DataAccessException {
		Integer num = 0;
		if (addList != null && !addList.isEmpty()) {
			for (BGDetailVo detail : addList) {
				num += addBloodGlucose(detail);
			}
		}
		if (updateList != null && !updateList.isEmpty()) {
			for (BGDetailVo detail : updateList) {
				num += updateBloodGlucose(detail);
			}
		}
		if (checkList != null && !checkList.isEmpty()) {
			for (BGDetailVo detail : checkList) {
				num += checkInsulin(detail.MXXH, detail.JLSJ, detail.JLGH);
			}
		}
		return num;
	}

	/**
	 * 保存血糖记录明细(新增主记录)
	 *
	 * @param recordVo   主记录
	 * @param addList    血糖新增列表
	 * @param updateList 血糖修改列表
	 * @param checkList  胰岛素执行列表
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer saveBloodGlucose(BGRecordVo recordVo, List<BGDetailVo> addList,
			List<BGDetailVo> updateList, List<BGDetailVo> checkList) throws SQLException, DataAccessException {
		Integer num = 0;
		num += addBGRecord(recordVo);

		if (addList != null && !addList.isEmpty()) {
			for (BGDetailVo detail : addList) {
				num += addBloodGlucose(detail);
			}
		}
		if (updateList != null && !updateList.isEmpty()) {
			for (BGDetailVo detail : updateList) {
				num += updateBloodGlucose(detail);
			}
		}
		if (checkList != null && !checkList.isEmpty()) {
			for (BGDetailVo detail : checkList) {
				num += checkInsulin(detail.MXXH, detail.JLSJ, detail.JLGH);
			}
		}
		return num;
	}

	/**
	 * 新增血糖主记录
	 *
	 * @param record
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addBGRecord(BGRecordVo record)
			throws SQLException, DataAccessException {
		record.dbtype = getCurrentDataSourceDBtype();
		return mapper.addBGRecord(record);
	}

	/**
	 * 新增血糖记录明细
	 *
	 * @param detail
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addBloodGlucose(BGDetailVo detail)
			throws SQLException, DataAccessException {
		detail.dbtype = getCurrentDataSourceDBtype();
		return mapper.addBloodGlucose(detail);
	}

	/**
	 * 更新血糖记录明细
	 *
	 * @param detail
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer updateBloodGlucose(BGDetailVo detail)
			throws SQLException, DataAccessException {
		detail.dbtype = getCurrentDataSourceDBtype();
		return mapper.updateBloodGlucose(detail);
	}

	/**
	 * 执行胰岛素记录
	 *
	 * @param mxxh
	 * @param jlsj
	 * @param jlgh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer checkInsulin(String mxxh, String jlsj, String jlgh)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.checkInsulin(mxxh, jlgh, jlsj, dbtype);
	}

	/**
	 * 获取第一条主记录的jlxh
	 *
	 * @param zyh
	 * @param jhrq
	 * @param brbq
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public String getFirstJlxhOfBG(String zyh, String jhrq, String brbq, String jgid)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getFirstJlxhOfBG(zyh, jhrq, brbq, jgid, dbtype);
	}

	/**
	 * 根据计划日期和时间点获取mxxh，用于验证是否有计划内容
	 * @param zyh
	 * @param jhrq
	 * @param xmlx
	 * @param xmxh
	 * @param jgid
	 * @return
	 */
	public List<String> getMxxhListByJhrq(String zyh, String jhrq, String xmlx, String xmxh,
			String jgid) throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getMxxhListByJhrq(zyh, jhrq, xmlx, xmxh, jgid, dbtype);
	}

	/**
	 * 删除明细
	 * @param mxxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public Integer deleteDetail(String mxxh) throws SQLException, DataAccessException {
		return mapper.deleteDetail(mxxh);
	}
}
