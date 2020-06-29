package com.bsoft.nis.service.dailywork.support;

import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.dailywork.DailyWork;
import com.bsoft.nis.mapper.dailywork.DailyWorkBaseServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by king on 2016/11/17.
 */
@Service
public class DailyWorkBaseServiceSup extends RouteDataSourceService {

	@Autowired
	DailyWorkBaseServiceMapper mapper;

	/**
	 * 获得计划
	 *
	 * @param data
	 * @param jhrq
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)
	public List<DailyWork> getPlan(List<String> data, String jhrq, String jgid)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getPlan(data, jhrq, jgid, dbtype);
	}

	/**
	 * 根据病区获得病人
	 *
	 * @param brbq 病人病区
	 * @param jgid 机构id
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<DailyWork> getPatientsByBq(String brbq, String jgid)
			throws SQLException, DataAccessException {
		return mapper.getPatientsByBq(brbq, jgid);
	}


	/**
	 * 修改建议
	 *
	 * @param data 病人病区
	 * @param kzsj 开嘱时间
	 * @param jgid 机构id
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)
	public List<DailyWork> getChangeAdvice(List<String> data, String kzsj, String jgid)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getChangeAdvice(data, kzsj, jgid, dbtype);
	}


	@Transactional(readOnly = true)
	public List<DailyWork> getInspection(List<String> zyhmList, String jgid,String ryrq)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getInspection(zyhmList,ryrq,jgid ,dbtype);
	}

	/**
	 * 风险提醒
	 *
	 * @param data
	 * @param jhrq
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)
	public List<DailyWork> getRisk(List<String> data, String jhrq, String jgid)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getRisk(data, jhrq, jgid, dbtype);
	}
}
