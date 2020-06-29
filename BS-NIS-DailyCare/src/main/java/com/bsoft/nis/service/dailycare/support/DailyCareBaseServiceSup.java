package com.bsoft.nis.service.dailycare.support;

import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.dailycare.DailySecondItem;
import com.bsoft.nis.domain.dailycare.DailyTopItem;
import com.bsoft.nis.mapper.dailycare.DailyCareBaseServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * 护理常规
 * Created by king on 2016/10/28.
 */
@Service
public class DailyCareBaseServiceSup extends RouteDataSourceService {

	@Autowired
	DailyCareBaseServiceMapper mapper;

	/**
	 * 获取护理常规一级列表
	 *
	 * @param jgid 机构id
	 * @param ksdm 科室代码
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)
	public List<DailyTopItem> getDailyNurseType(String ksdm, String jgid)
			throws SQLException, DataAccessException {
		return mapper.getDailyNurseType(ksdm, jgid);
	}

	/**
	 * 获取护理常规二级列表
	 *
	 * @param type    护理类型
	 * @param jgid    机构id
	 * @param sysType
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)
	public List<DailySecondItem> getDailyNurseList(String type, String jgid, int sysType)
			throws SQLException, DataAccessException {
		return mapper.getDailyNurseList(type, jgid, sysType);
	}

	/**
	 * 保存常规护理项目
	 *
	 * @param jlbs 记录标识
	 * @param zyh  住院号
	 * @param xmbs 项目标识
	 * @param zfbz 作废标志
	 * @param zxr  执行人
	 * @param zxsj 执行时间呢
	 * @param brbq 病人病区
	 * @param jgid 机构id
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer SaveDailyNurseItems(Long jlbs, String zyh, String xmbs, String zfbz,
			String zxr, String zxsj, String brbq, String jgid)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.SaveDailyNurseItems(jlbs, zyh, xmbs, zfbz, zxr, zxsj, brbq, jgid, dbtype);
	}
}
