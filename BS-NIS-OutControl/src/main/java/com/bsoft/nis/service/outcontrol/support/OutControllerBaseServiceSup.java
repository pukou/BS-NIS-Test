package com.bsoft.nis.service.outcontrol.support;

import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.outcontrol.OutControl;
import com.bsoft.nis.mapper.outcontrol.OutControlBaseServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * 外出管理
 * Created by king on 2016/11/16.
 */
@Service
public class OutControllerBaseServiceSup extends RouteDataSourceService {

	@Autowired
	OutControlBaseServiceMapper mapper;

	/**
	 * 获取外出病人记录
	 *
	 * @param zyh  住院号
	 * @param brbq 病人病区
	 * @param jgid 机构id
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)
	public List<OutControl> getOutPatientByZyh(String zyh, String brbq, String jgid)
			throws SQLException, DataAccessException {

		return mapper.getOutPatientByZyh(zyh, brbq, jgid);
	}
	/*升级编号【56010038】============================================= start
                外出管理PDA上只有登记功能，查询需要找到具体的人再查询，不太方便，最好能有一个查询整个病区外出病人的列表
            ================= classichu 2018/3/7 19:49
            */
	/**
	 * 获取病区所有外出病人列表
	 *
	 * @param brbq 病人病区
	 * @param jgid 机构id
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)
	public List<OutControl> getAllOurPatients(String brbq, String jgid)
			throws SQLException, DataAccessException {

		return mapper.getAllOurPatients(brbq, jgid);
	}
	/* =============================================================== end */

	/**
	 * 获取病人当前外出状态
	 *
	 * @param zyh  住院号
	 * @param brbq 病人病区
	 * @param jgid 机构id
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)
	public List<OutControl> getPatientStatus(String zyh, String brbq, String jgid)
			throws SQLException, DataAccessException {

		return mapper.getPatientStatus(zyh, brbq, jgid);
	}


	/**
	 * 外出登记
	 *
	 * @param jlxh   记录序号
	 * @param wcdjsj 外出登记时间
	 * @param wcsj   外出时间
	 * @param wcdjhs 外出登记护士
	 * @param yjhcsj 预计回床时间
	 * @param pzys   批准医生
	 * @param ptry   陪同人员
	 * @param zyh    住院号
	 * @param brbq   病人病区
	 * @param jgid   机构id
	 * @param wcyy   外出原因
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer registerOutPatient(Long jlxh, String wcdjsj, String wcsj, String wcdjhs,
			String yjhcsj, String pzys, int ptry, String zyh, String brbq, String jgid,
			String wcyy)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.registerOutPatient(jlxh, wcdjsj, wcsj, wcdjhs, yjhcsj, pzys, ptry, zyh, brbq,
				jgid, wcyy, dbtype);
	}

	/**
	 * 回床登记
	 *
	 * @param jlxh   记录序号
	 * @param hcdjhs 回床登记护士
	 * @param hcsj   回床时间
	 * @param hcdjsj 回床登记时间
	 * @param jgid   机构id
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer registerBackToBed(Long jlxh, String hcdjhs, String hcsj, String hcdjsj,
			String jgid) throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.registerBackToBed(jlxh, hcdjhs, hcsj, hcdjsj, jgid, dbtype);
	}


}
