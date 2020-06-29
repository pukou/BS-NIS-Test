package com.bsoft.nis.service.adviceexecute.support;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.PlanInfo;
import com.bsoft.nis.domain.adviceexecute.db.PlanRefusalVo;
import com.bsoft.nis.mapper.adviceexecute.AdviceExecuteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Description: 医嘱执行相关数据库直接查询服务
 * User: 苏泽雄
 * Date: 17/1/6
 * Time: 10:27:25
 */
@Service
public class AdviceExecuteServiceSup extends RouteDataSourceService {

	@Autowired
	AdviceExecuteMapper mapper;

	/**
	 * 判断拒绝原因是否已存在
	 *
	 * @param jhh
	 * @param dyxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public Integer checkRefuse(String jhh, String dyxh)
			throws SQLException, DataAccessException {
		return mapper.checkRefuse(jhh, dyxh);
	}

	/**
	 * 保存拒绝执行的数据
	 *
	 * @param planRefusalInsertList 新建计划拒绝
	 * @param planUpdateList        更新医嘱计划
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer savePlanRefusal(List<PlanRefusalVo> planRefusalInsertList,
			List<PlanInfo> planUpdateList) throws SQLException, DataAccessException {
		Integer num = 0;
		if (planRefusalInsertList != null && !planRefusalInsertList.isEmpty()) {
			for (PlanRefusalVo refusal : planRefusalInsertList) {
				num += insertPlanRefusal(refusal);
			}
		}
		if (planUpdateList != null && !planUpdateList.isEmpty()) {
			for (PlanInfo plan : planUpdateList) {
				num += updateAdvicePlanOfRefuse(plan);
			}
		}
		return num;
	}

	/**
	 *
	 * @param refusal
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer insertPlanRefusal(PlanRefusalVo refusal)
			throws SQLException, DataAccessException {
		refusal.dbtype = getCurrentDataSourceDBtype();
		return mapper.insertPlanRefusal(refusal);
	}

	@Transactional(rollbackFor = Exception.class)
	public Integer updateAdvicePlanOfRefuse(PlanInfo plan)
			throws SQLException, DataAccessException {
		plan.dbtype = getCurrentDataSourceDBtype();
		return mapper.updateAdvicePlanOfRefuse(plan);
	}
}
