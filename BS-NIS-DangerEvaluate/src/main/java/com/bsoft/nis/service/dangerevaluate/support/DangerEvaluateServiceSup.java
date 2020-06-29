package com.bsoft.nis.service.dangerevaluate.support;

import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.dangerevaluate.*;
import com.bsoft.nis.domain.dangerevaluate.db.*;
import com.bsoft.nis.mapper.dangerevaluate.DangerEvaluateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Description: 风险评估
 * User: 苏泽雄
 * Date: 16/12/1
 * Time: 9:56:26
 */
@Service
public class DangerEvaluateServiceSup extends RouteDataSourceService {

	@Autowired
	DangerEvaluateMapper mapper;

	/**
	 * 获取风险质控
	 *
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<DEQualityControlVo> getDEQualityControl()
			throws SQLException, DataAccessException {
		return mapper.getDEQualityControl();
	}

	/**
	 * 获取风险评估列表
	 *
	 * @param jgid
	 * @param bqid
	 * @param brnl @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<DEOverview> getDEList(String jgid, String brnl, String bqid)
			throws SQLException, DataAccessException {
		return mapper.getDEList(jgid, brnl, bqid);
	}

	public List<DEPGHBean> getFXPGDHList(String pglx, String jgid)
			throws SQLException, DataAccessException {
		return mapper.getFXPGDHList(pglx, jgid);
	}


	/**
	 * 获取风险评估记录
	 *
	 * @param pgdh
	 * @param zyh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<SimDERecord> getSimDERecordList(String pgdh, String zyh)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.getSimDERecordList(pgdh, zyh,dbtype);
	}

	/**
	 * 获取风险表单的质控规则
	 *
	 * @param pgdh
	 * @param order
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<DEQualityControlVo> getSimQualityControl(String pgdh, String order)
			throws SQLException, DataAccessException {
		return mapper.getSimQualityControl(pgdh, order);
	}

	/**
	 * 获取风险评估单的评估因子
	 *
	 * @param pgdh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<DEFactor> getDEFactorList(String pgdh)
			throws SQLException, DataAccessException {
		return mapper.getDEFactorList(pgdh);
	}

	/**
	 * 获取风险因子的因子评分
	 *
	 * @param fxyz
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<FactorGoal> getDEFactorGoalList(String fxyz)
			throws SQLException, DataAccessException {
		return mapper.getDEFactorGoalList(fxyz);
	}

	/**
	 * 获取第一条风险评估记录
	 *
	 * @param zyh
	 * @param pgdh
	 * @param pglx
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<String> getFirstDERecord(String zyh, String pgdh, String pglx, String jgid)
			throws SQLException, DataAccessException {
		return mapper.getFirstDERecord(zyh, pgdh, pglx, jgid);
	}

	/**
	 * 获取评估记录
	 *
	 * @param pgxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public DERecord getDERecordByPgxh(String pgxh) throws SQLException, DataAccessException {
		return mapper.getDERecordByPgxh(pgxh);
	}


	/**
	 * 获取措施单号
	 *
	 * @param pgdh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public DEMeasureFormVo getDEMeasureCode(String pgdh, String jgid)
			throws SQLException, DataAccessException {
		return mapper.getDEMeasureCode(pgdh, jgid);
	}

	/**
	 * 获取风险措施单
	 *
	 * @param pgdh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<DEMeasureItemVo> getDEMeasureItems(String pgdh, String jgid)
			throws SQLException, DataAccessException {
		return mapper.getDEMeasureItems(pgdh, jgid);
	}

	/**
	 * 获取风险措施评价
	 *
	 * @param pgdh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<DEEvaluate> getDEEvaluateList(String pgdh, String jgid)
			throws SQLException, DataAccessException {
		return mapper.getDEEvaluateList(pgdh, jgid);
	}

	/**
	 * 获取风险措施主记录
	 *
	 * @param pgxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<MeasureRecord> getDEMeasureRecord(String pgxh)
			throws SQLException, DataAccessException {
		return mapper.getDEMeasureRecord(pgxh);
	}
	public SimMeasureRecord getPreOneCSJL(String csdh,String pgxh,String zyh)
			throws SQLException, DataAccessException {
		return mapper.getPreOneCSJL(csdh,pgxh, zyh);
	}
	public List<DEEvaluate> getCSPJList(String csdh)
			throws SQLException, DataAccessException {
		return mapper.getCSPJList(csdh);
	}

	/**
	 * 获取风险措施项目记录
	 *
	 * @param csdh
	 * @param jlxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<DEMeasureItemVo> getDEMeasureItemRecord(String csdh, String jlxh)
			throws SQLException, DataAccessException {
		return mapper.getDEMeasureItemRecord(csdh, jlxh);
	}

	/**
	 * 获取评估分值(获取因子评分，同时获取评估明细的MXXH)
	 *
	 * @param fxyz
	 * @param pgxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<FactorGoal> getDEFactorGoalWithMXXH(String fxyz, String pgxh)
			throws SQLException, DataAccessException {
		return mapper.getDEFactorGoalWithMXXH(fxyz, pgxh);
	}

	/**
	 * 保存风险评估记录
	 *
	 * @param recordAddList    新增风险评估记录
	 * @param recordUpdateList 修改风险评估记录
	 * @param detailAddList    新增记录明细
	 * @param detailDeleteList 删除记录明细
	 * @param remindFinshMap   已提醒的风险质控提醒
	 * @param remindAddList    新增风险质控提醒
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer saveDERecord(List<DERecordVo> recordAddList,
			List<DERecordVo> recordUpdateList, List<DERecordDetailVo> detailAddList,
			List<String> detailDeleteList, Map<String, String> remindFinshMap,
			List<DEQCRemindVo> remindAddList) throws SQLException, DataAccessException {
		Integer num = 0;
		if (recordAddList != null && !recordAddList.isEmpty()) {
			for (DERecordVo record : recordAddList) {
				num += addDERecord(record);
			}
		}
		if (recordUpdateList != null && !recordUpdateList.isEmpty()) {
			for (DERecordVo record : recordUpdateList) {
				num += updateDERecord(record);
			}
		}
		if (detailAddList != null && !detailAddList.isEmpty()) {
			for (DERecordDetailVo detail : detailAddList) {
				num += addDERecordDetail(detail);
			}
		}
		if (detailDeleteList != null && !detailDeleteList.isEmpty()) {
			for (String mxxh : detailDeleteList) {
				num += deleteDERecordDetail(mxxh);
			}
		}
		num += finishDEQCRemind(remindFinshMap.get("PGLX"), remindFinshMap.get("ZYH"));
		if (remindAddList != null && !remindAddList.isEmpty()) {
			for (DEQCRemindVo remind : remindAddList) {
				num += addDEQCRemind(remind);
			}
		}
		return num;
	}

	/**
	 * 新建风险评估记录
	 *
	 * @param record
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addDERecord(DERecordVo record) throws SQLException, DataAccessException {
		record.dbtype = getCurrentDataSourceDBtype();
		return mapper.addDERecord(record);
	}

	/**
	 * 更新风险评估记录
	 *
	 * @param record
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer updateDERecord(DERecordVo record) throws SQLException, DataAccessException {
		record.dbtype = getCurrentDataSourceDBtype();
		return mapper.updateDERecord(record);
	}

	/**
	 * 新建评分明细
	 *
	 * @param detail
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addDERecordDetail(DERecordDetailVo detail)
			throws SQLException, DataAccessException {
		return mapper.addDERecordDetail(detail);
	}

	/**
	 * 删除评分明细
	 *
	 * @param mxxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer deleteDERecordDetail(String mxxh) throws SQLException, DataAccessException {
		return mapper.deleteDERecordDetail(mxxh);
	}

	/**
	 * 根据评估类型和住院号，将一个类型的评估质控提醒全部置为已提醒
	 *
	 * @param pglx
	 * @param zyh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer finishDEQCRemind(String pglx, String zyh)
			throws SQLException, DataAccessException {
		return mapper.finishDEQCRemind(pglx, zyh);
	}

	/**
	 * 新增评估质控提醒
	 *
	 * @param remind
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addDEQCRemind(DEQCRemindVo remind)
			throws SQLException, DataAccessException {
		remind.dbtype = getCurrentDataSourceDBtype();
		return mapper.addDEQCRemind(remind);
	}

	/**
	 * 获取措施单列表
	 *
	 * @param pgdh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<MeasureOverview> getDEMeasureList(String pgdh, String jgid)
			throws SQLException, DataAccessException {
		return mapper.getDEMeasureList(pgdh, jgid);
	}

	/**
	 * 获取措施简略措施记录
	 *
	 * @param bdxh
	 * @param pgxh
	 * @param zyh
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<SimMeasureRecord> getSimMeasureRecord(String bdxh, String pgxh, String zyh,
			String jgid) throws SQLException, DataAccessException {
		return mapper.getSimMeasureRecord(bdxh, pgxh, zyh, jgid);
	}

	/**
	 * 保存评估措施记录
	 *
	 * @param recordAddList    新增风险措施记录
	 * @param recordUpdateList 更新风险措施记录
	 * @param itemAddList      新增风险措施项目
	 * @param itemDeleteList   删除风险措施项目
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer saveDEMeasure(List<MeasureRecordVo> recordAddList,
			List<MeasureRecordVo> recordUpdateList, List<MeasureItemVo> itemAddList,
			List<String> itemDeleteList) throws SQLException, DataAccessException {
		Integer num = 0;
		if (recordAddList != null && !recordAddList.isEmpty()) {
			for (MeasureRecordVo record : recordAddList) {
				num += addMeasureRecord(record);
			}
		}
		if (recordUpdateList != null && !recordUpdateList.isEmpty()) {
			for (MeasureRecordVo record : recordUpdateList) {
				num += updateMeasureRecord(record);
			}
		}
		if (itemAddList != null && !itemAddList.isEmpty()) {
			for (MeasureItemVo item : itemAddList) {
				num += addMeasureItem(item);
			}
		}
		if (itemDeleteList != null && !itemDeleteList.isEmpty()) {
			for (String jlxm : itemDeleteList) {
				num += deleteMeasureItem(jlxm);
			}
		}
		return num;
	}

	/**
	 * 新增风险措施记录
	 *
	 * @param record
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addMeasureRecord(MeasureRecordVo record)
			throws SQLException, DataAccessException {
		record.dbtype = getCurrentDataSourceDBtype();
		return mapper.addMeasureRecord(record);
	}

	/**
	 * 修改风险措施记录
	 *
	 * @param record
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer updateMeasureRecord(MeasureRecordVo record)
			throws SQLException, DataAccessException {
		record.dbtype = getCurrentDataSourceDBtype();
		return mapper.updateMeasureRecord(record);
	}

	/**
	 * 新增风险措施项目
	 *
	 * @param item
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addMeasureItem(MeasureItemVo item)
			throws SQLException, DataAccessException {
		return mapper.addMeasureItem(item);
	}

	/**
	 * 删除风险措施项目
	 *
	 * @param jlxm
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer deleteMeasureItem(String jlxm) throws SQLException, DataAccessException {
		return mapper.deleteMeasureItem(jlxm);
	}

	/**
	 * 根据pgxh获取提醒的ZYH和PGLX
	 *
	 * @param pgxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public DEQCRemindVo getZYHOfDEQCRemind(String pgxh)
			throws SQLException, DataAccessException {
		return mapper.getZYHOfDEQCRemind(pgxh);
	}

	/**
	 * 获取病人风险质控提醒的最大pgxh
	 *
	 * @param zyh
	 * @param pglx
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public String getMAXOfDEQCRemind(String zyh, String pglx)
			throws SQLException, DataAccessException {
		return mapper.getMAXOfDEQCRemind(zyh, pglx);
	}

	/**
	 * 删除评估记录
	 *
	 * @param map        包括pgxh,maxpgxh,zyh
	 * @param needCancle 是否需要撤销maxpgxh的已提醒
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer deleteDERecord(Map<String, String> map, Boolean needCancle)
			throws SQLException, DataAccessException {
		Integer num = 0;
		if (needCancle) {
			num += returnDEQCRemind(map.get("maxpgxh"));
		}
		num += deleteDERecord(map.get("pgxh"));
		num += cancleDEQCRemind(map.get("pgxh"));
		return num;
	}

	/**
	 * 作废风险质控提醒
	 *
	 * @param pgxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer cancleDEQCRemind(String pgxh) throws SQLException, DataAccessException {
		return mapper.cancleDEQCRemind(pgxh);
	}

	/**
	 * 删除风险评估记录
	 *
	 * @param pgxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer deleteDERecord(String pgxh) throws SQLException, DataAccessException {
		return mapper.deleteDERecord(pgxh);
	}

	/**
	 * 撤销提醒为未提醒状态
	 *
	 * @param pgxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer returnDEQCRemind(String pgxh) throws SQLException, DataAccessException {
		return mapper.returnDEQCRemind(pgxh);
	}

	/**
	 * 删除风险措施
	 *
	 * @param jlxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer deleteDEMeasure(String jlxh) throws SQLException, DataAccessException {
		return mapper.deleteDEMeasure(jlxh);
	}

	/**
	 * 护士长审核风险评估
	 *
	 * @param hszqm
	 * @param hszqmsj
	 * @param pgxh
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer checkDERecord(String hszqm, String hszqmsj, String pgxh)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.checkDERecord(hszqm, hszqmsj, pgxh, dbtype);
	}

	/**
	 * 评价风险措施
	 *
	 * @param jlxh
	 * @param hszqm
	 * @param hszqmsj
	 * @param cspj
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer evaluateMeasure(String jlxh, String hszqm, String hszqmsj, String cspj)
			throws SQLException, DataAccessException {
		String dbtype = getCurrentDataSourceDBtype();
		return mapper.evaluateMeasure(jlxh, hszqm, hszqmsj, cspj, dbtype);
	}

	/**
	 * 获取疼痛评估项目
	 *
	 * @param jgid
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<PainEvaluate> getPainEvaluate(String jgid)
			throws SQLException, DataAccessException {
		return mapper.getPainEvaluate(jgid);
	}

	/**
	 * 获取疼痛评估选项和相关的记录内容
	 *
	 * @param pgxh
	 * @param xmxh
	 * @return
	 */
	public List<PEOption> getPEOption(String pgxh, String xmxh)
			throws SQLException, DataAccessException {
		return mapper.getPEOption(pgxh, xmxh);
	}

	/**
	 * 获取XMLX=1(手工输入)的疼痛评估项目的记录内容
	 *
	 * @param pgxh
	 * @param xmxh
	 * @return
	 */
	public PEOption getPEOptionOfNoXXXH(String pgxh, String xmxh)
			throws SQLException, DataAccessException {
		return mapper.getPEOptionOfNoXXXH(pgxh, xmxh);
	}

	/**
	 * 保存疼痛综合评估记录
	 *
	 * @param recordAddList    新增记录
	 * @param recordUpdateList 修改记录
	 * @param recordDeleteList 删除记录
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer savePainEvaluate(List<PERecordVo> recordAddList,
			List<PERecordVo> recordUpdateList, List<String> recordDeleteList)
			throws SQLException, DataAccessException {
		Integer num = 0;
		if (recordAddList != null && !recordAddList.isEmpty()) {
			for (PERecordVo record : recordAddList) {
				num += addPERecord(record);
			}
		}
		if (recordUpdateList != null && !recordUpdateList.isEmpty()) {
			for (PERecordVo record : recordUpdateList) {
				num += updatePERecord(record);
			}
		}
		if (recordDeleteList != null && !recordDeleteList.isEmpty()) {
			for (String jlxm : recordDeleteList) {
				num += deletePERecord(jlxm);
			}
		}
		return num;
	}

	/**
	 * 新增疼痛综合评估记录
	 *
	 * @param record
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer addPERecord(PERecordVo record) throws SQLException, DataAccessException {
		return mapper.addPERecord(record);
	}

	/**
	 * 修改疼痛综合评估记录
	 *
	 * @param record
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer updatePERecord(PERecordVo record) throws SQLException, DataAccessException {
		return mapper.updatePERecord(record);
	}

	/**
	 * 删除疼痛综合评估记录
	 *
	 * @param jlxm
	 * @return
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Integer deletePERecord(String jlxm) throws SQLException, DataAccessException {
		return mapper.deletePERecord(jlxm);
	}
}
