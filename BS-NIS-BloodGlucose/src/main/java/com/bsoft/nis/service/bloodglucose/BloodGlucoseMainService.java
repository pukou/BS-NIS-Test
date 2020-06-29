package com.bsoft.nis.service.bloodglucose;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.servicesup.support.ConfigServiceSup;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.bloodglucose.*;
import com.bsoft.nis.domain.bloodglucose.db.BGDetailVo;
import com.bsoft.nis.domain.bloodglucose.db.BGRecordVo;
import com.bsoft.nis.domain.core.SystemConfig;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.mapper.patient.PatientMapper;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.bloodglucose.support.BloodGlucoseServiceSup;
import com.bsoft.nis.service.patient.PatientMainService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 血糖治疗记录单主服务
 * User: 苏泽雄
 * Date: 16/12/23
 * Time: 14:04:15
 */
@Service
public class BloodGlucoseMainService extends RouteDataSourceService {

	private Log logger = LogFactory.getLog(BloodGlucoseMainService.class);

	@Autowired
	BloodGlucoseServiceSup service;
	@Autowired
	ConfigServiceSup configService; // 系统配置服务
	@Autowired
	IdentityService identityService; // 种子表服务
	@Autowired
	DateTimeService timeService; // 日期时间服务
	@Autowired
	DictCachedHandler handler; // 缓存处理器
	@Autowired
	PatientMapper patientMapper; // 病人mapper
	@Autowired
	PatientMainService patientService; // 病人服务

	/**
	 * 获取血糖治疗历史记录
	 *
	 *
	 * @param zyh
	 * @param kssj
	 * @param jssj
	 * @param brbq
	 * @param jgid
	 * @return
	 */
	public BizResponse<BGHistoryData> getBloodGlucoseHistory(String zyh, String kssj,
			String jssj, String brbq, String jgid) {
		keepOrRoutingDateSource(DataSource.MOB);
		BizResponse<BGHistoryData> response = new BizResponse<>();
		BGHistoryData data = new BGHistoryData();
		List<BloodGlucoseDetail> glucose = new ArrayList<>();
		List<BloodGlucoseDetail> insulin = new ArrayList<>();

		try {
			// 获取时间段内所有历史明细记录
			List<BloodGlucoseDetail> historys = service.getBGHistoryList(zyh, kssj, jssj, brbq, jgid);
			if (historys != null && !historys.isEmpty()) {
				for (BloodGlucoseDetail history : historys) {
					if ("1".equals(history.ZTBZ)) {
						history.JLXM = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid,
								history.JLGH);
					}
					String xmlx = history.XMLX;
					if ("1".equals(xmlx)) {
						glucose.add(history);
					} else if ("2".equals(xmlx)) {
						insulin.add(history);
					}
				}
			}
			data.GLUCOSE = glucose;
			data.INSULIN = insulin;

			response.isSuccess = true;
			response.data = data;
			response.message = "获取血糖治疗历史记录成功";
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取血糖治疗历史记录失败]数据库查询错误";
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取血糖治疗历史记录失败]服务内部错误";
		}
		return response;
	}

	/**
	 * 获取血糖治疗列表
	 *
	 * @param xmlx
	 * @param zyh
	 * @param jhrq
	 * @param brbq
	 * @param jgid
	 * @param xmxh
	 * @return
	 */
	public BizResponse<BloodGlucoseRecord> getBGList(String xmlx, String zyh, String jhrq,
			String brbq, String jgid, String xmxh) {
		keepOrRoutingDateSource(DataSource.MOB);
		BizResponse<BloodGlucoseRecord> response = new BizResponse<>();
		BloodGlucoseRecord record = new BloodGlucoseRecord();

		try {
			// 待执行明细记录
			List<BloodGlucoseDetail> details = new ArrayList<>();
			if (!"9".equals(xmxh)) {
				details = service.getBloodGlucoseDetail(zyh, jhrq, brbq, xmlx, xmxh, jgid);
			} else if ("9".equals(xmxh)) { // 临时时间点时，返回一个无内容的Detail
				BloodGlucoseDetail _detail = new BloodGlucoseDetail();
				// 获取主表jlxh，没有时置为空，并在保存时判断
				String jlxh = service.getFirstJlxhOfBG(zyh, jhrq, brbq, jgid);
				_detail.JLXH = (jlxh != null && !"".equals(jlxh)) ? jlxh : null;
				_detail.JHBZ = "0";
				_detail.XMDW = "mmol/L";
				_detail.XMLX = "1";
				_detail.XMXH = xmxh;
				_detail.XMNR = "临时";
				_detail.ZTBZ = "0";
				details.add(_detail);
			}

			// 全部明细记录
			List<BloodGlucoseDetail> historys = service.getBGHistoryByJlxh(zyh, jhrq, brbq, xmlx, jgid);
			for (BloodGlucoseDetail history : historys) {
				if ("1".equals(history.ZTBZ)) {
					history.JLXM = handler
							.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, history.JLGH);
				}
			}

			record.DETAILS = details;
			record.HISTORYS = historys;

			response.isSuccess = true;
			response.data = record;
			response.message = "获取血糖治疗列表成功";
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取血糖治疗列表失败]数据库查询错误";
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取血糖治疗列表失败]服务内部错误";
		}
		return response;
	}

	/**
	 * 添加一条临时的血糖记录
	 * @param zyh
	 * @param jhrq
	 * @param brbq
	 * @param jgid
	 * @param xmxh
	 * @param xmnr
	 * @return
	 */
	public BizResponse<BloodGlucoseDetail> addDetail(String zyh, String jhrq, String brbq,
			String jgid, String xmxh, String xmnr) {
		keepOrRoutingDateSource(DataSource.MOB);
		BizResponse<BloodGlucoseDetail> response = new BizResponse<>();

		try {
			BloodGlucoseDetail detail = new BloodGlucoseDetail();
			// 获取主表jlxh，没有时置为空，并在保存时判断
			String jlxh = service.getFirstJlxhOfBG(zyh, jhrq, brbq, jgid);
			detail.JLXH = (jlxh != null && !"".equals(jlxh)) ? jlxh : null;
			detail.JHBZ = "0";
			detail.XMDW = "mmol/L";
			detail.XMLX = "1";
			detail.XMXH = xmxh;
			detail.XMNR = xmnr;
			detail.ZTBZ = "0";

			response.isSuccess = true;
			response.data = detail;
			response.message = "获取血糖治疗列表成功";
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取血糖治疗列表失败]数据库查询错误";
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取血糖治疗列表失败]服务内部错误";
		}
		return response;
	}

	/**
	 * 保存血糖记录明细
	 *
	 * @param data
	 * @return
	 */
	public BizResponse<BloodGlucoseRecord> saveBloodGlucose(BGSavePostData data) {
		keepOrRoutingDateSource(DataSource.MOB);
		BizResponse<BloodGlucoseRecord> response = new BizResponse<>();
		List<BGDetailVo> updateList = new ArrayList<>();
		List<BGDetailVo> addList = new ArrayList<>();
		List<BGDetailVo> checkList = new ArrayList<>();
		String jlxh = null; // 主记录的jlxh
		String jgid = data.JGID;
		boolean addMainRecord = false; // 是否新增主表数据
		BGRecordVo recordVo = new BGRecordVo();

		try {
			String now = timeService.now(DataSource.MOB);
			// 获取修改的血糖记录列表
			List<BloodGlucoseDetail> list = data.DETAILS;
			if (list != null && !list.isEmpty()) {
				for (BloodGlucoseDetail detail : list) {
					BGDetailVo detailVo = new BGDetailVo(detail);
					String xmlx = detailVo.XMLX;
					if ("1".equals(xmlx)) { // 血糖
						String mxxh = detailVo.MXXH;
						if (mxxh == null || "".equals(mxxh)) { // 新增
							// 判断jlxh
							String _jlxh = detailVo.JLXH;
							if (_jlxh == null || "".equals(_jlxh)) { // detail没有jlxh
								if (jlxh == null || "".equals(jlxh)) { // 没有主记录，新增主记录
									// 获取jlxh
									jlxh = String.valueOf(identityService.getIdentityMax("IENR_XTJL",DataSource.MOB));
									// 获取病人
									keepOrRoutingDateSource(DataSource.HRP);
									Patient patient = patientMapper.getPatientByZyh(data.ZYH);
									// 新增主记录
									recordVo.JLXH = jlxh;
									recordVo.JLGH = detail.JLGH;
									recordVo.ZYH = data.ZYH;
									recordVo.BRXM = patient.BRXM;
									recordVo.KSDM = patient.BRKS;
									recordVo.BRBQ = data.BRBQ;
									recordVo.BRCH = patient.BRCH;
									recordVo.JHRQ = data.JHRQ;
									recordVo.JLSJ = now;
									recordVo.YDSMC = detail.YDSMC;
//									recordVo.YZBXH;
									recordVo.JGID = jgid;
									addMainRecord = true;
								}
								detailVo.JLXH = jlxh;
							}

							mxxh = String.valueOf(identityService.getIdentityMax("IENR_XTJLMX",DataSource.MOB));
							detailVo.MXXH = mxxh;
							detailVo.JHBZ = "0";
							detailVo.JLSJ = now;
							detailVo.ZTBZ = "1";
							addList.add(detailVo);
						} else { // 修改
							detailVo.JLSJ = now;
							detailVo.ZTBZ = "1";
							updateList.add(detailVo);
						}
					} else if ("2".equals(xmlx)) { // 胰岛素
						detailVo.JLSJ = now;
						detailVo.ZTBZ = "1";
						checkList.add(detailVo);
					}
				}
			}

			// 执行数据库操作
			keepOrRoutingDateSource(DataSource.MOB);
			if (addMainRecord) {
				service.saveBloodGlucose(recordVo, addList, updateList, checkList);
			} else {
				service.saveBloodGlucose(addList, updateList, checkList);
			}

			// 获取返回值
			BloodGlucoseRecord newRecord = getBGList(data.XMLX, data.ZYH, data.JHRQ, data.BRBQ, data.JGID, data.XMXH).data;

			response.isSuccess = true;
			response.data = newRecord;
			response.message = "保存血糖记录明细成功";
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[保存血糖记录明细失败]数据库查询错误";
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[保存血糖记录明细失败]服务内部错误";
		}
		return response;
	}

	/**
	 * 获取血糖治疗的时间点
	 * @return
	 */
	public BizResponse<GlucoseTimeData> getGlucoseTimes() {
		keepOrRoutingDateSource(DataSource.MOB);
		BizResponse<GlucoseTimeData> response = new BizResponse<>();
		GlucoseTimeData data = new GlucoseTimeData();

		try {
			// 血糖时间点
			List<GlucoseTime> glucoseTimes = new ArrayList<>();
			List<SystemConfig> configList = configService.getConfigsByDmlb("458");
			for (SystemConfig config : configList) {
				GlucoseTime time = new GlucoseTime();
				time.XMXH = config.DMSB;
				time.XMNR = config.DMMC;
				glucoseTimes.add(time);
			}
			data.GLUCOSETIME = glucoseTimes;

			// 胰岛素时间点
			List<GlucoseTime> insulinTimes = new ArrayList<>();
			List<SystemConfig> configList2 = configService.getConfigsByDmlb("459");
			for (SystemConfig config : configList2) {
				GlucoseTime time = new GlucoseTime();
				time.XMXH = config.DMSB;
				time.XMNR = config.DMMC;
				insulinTimes.add(time);
			}
			data.INSULINTIME = insulinTimes;

			response.isSuccess = true;
			response.data = data;
			response.message = "获取血糖治疗的时间点成功";
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取血糖治疗的时间点失败]数据库查询错误";
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取血糖治疗的时间点失败]服务内部错误";
		}
		return response;
	}

	/**
	 * 根据xmlx和xmxh获取未执行的记录
	 *
	 * @param xmlx
	 * @param zyh
	 * @param jhrq
	 * @param brbq
	 * @param xmxh
	 * @param jgid
	 * @return
	 */
	public BizResponse<BloodGlucoseRecord> getUnexecutedBG(String xmlx, String zyh, String jhrq, String brbq,
			String xmxh, String jgid) {
		keepOrRoutingDateSource(DataSource.MOB);
		BizResponse<BloodGlucoseRecord> response = new BizResponse<>();
		BloodGlucoseRecord record = new BloodGlucoseRecord();

		try {
			// 待执行明细记录
			record.DETAILS = service.getBloodGlucoseDetail(zyh, jhrq, brbq, xmlx, xmxh, jgid);

			response.isSuccess = true;
			response.data = record;
			response.message = "获取待执行血糖治疗数据成功";
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取待执行血糖治疗数据失败]数据库查询错误";
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取待执行血糖治疗数据失败]服务内部错误";
		}
		return response;
	}

	/**
	 * 获取血糖治疗病人
	 *
	 * @param bqid
	 * @param jhrq
	 * @param xmlx
	 * @param xmxh
	 * @param hsgh
	 * @param jgid
	 * @return
	 */
	public BizResponse<SickPersonVo> GetPatientList(String bqid, String jhrq, String xmlx, String xmxh, String hsgh,
			String jgid) {
		// 获取病区全部病人(我的病人)
		BizResponse<SickPersonVo> response = patientService.getDeptPatients(bqid, hsgh, jgid);
		List<SickPersonVo> patients = response.datalist;
		List<SickPersonVo> patientList = new ArrayList<>();
		try {
			keepOrRoutingDateSource(DataSource.MOB);
			for (SickPersonVo patient : patients) {
				List<String> mxxhList = service
						.getMxxhListByJhrq(patient.ZYH, jhrq, xmlx, xmxh, jgid);
				if (mxxhList != null && !mxxhList.isEmpty()) {
					patientList.add(patient);
				}
			}
			response.isSuccess = true;
			response.datalist = patientList;
			response.message = "获取病人列表成功";
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取病人列表失败]数据库查询错误";
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[获取病人列表失败]服务内部错误";
		}
		return response;
	}

	/**
	 * 删除明细（只有临时的血糖记录可以删除）
	 * @param mxxh
	 * @return
	 */
	public BizResponse<String> deleteDetail(String mxxh) {
		BizResponse<String> response = new BizResponse<>();
		try {
			keepOrRoutingDateSource(DataSource.MOB);
			service.deleteDetail(mxxh);
			response.isSuccess = true;
			response.message = "删除血糖记录成功";
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[删除血糖记录失败]数据库查询错误";
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.isSuccess = false;
			response.message = "[删除血糖记录失败]服务内部错误";
		}
		return response;
	}
}
