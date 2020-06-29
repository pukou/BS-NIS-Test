package com.bsoft.nis.service.catheter;


import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.catheter.CatheterMeasurData;
import com.bsoft.nis.domain.catheter.CatheterRespose;
import com.bsoft.nis.domain.catheter.db.CatheterYLGJLvo;
import com.bsoft.nis.domain.lifesign.LifeSignRealSaveDataItem;
import com.bsoft.nis.domain.patient.db.SickPersonDetailVo;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.catheter.support.CatheterService;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 */
@Service
public class CatheterMainService extends RouteDataSourceService {

	private Log logger = LogFactory.getLog(CatheterMainService.class);

	@Autowired
	CatheterService service;
	@Autowired
	DateTimeService timeService;//获取数据库时间服务
	@Autowired
	IdentityService identity;//获取种子服务
	@Autowired
	DictCachedHandler handler; // 缓存处理器
	@Autowired
	PatientServiceSup patientService; // 病人服务

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar cal = Calendar.getInstance();

	/**
	 * 获取导管引流的病人列表
	 * @param brbq
	 * @param jgid
	 * @return
	 */
	public BizResponse<SickPersonVo> getpationtList(String brbq, String jgid) {
		BizResponse<SickPersonVo> bizResponse = new BizResponse<>();
		try {
			// 获取导管的XMXH
			keepOrRoutingDateSource(DataSource.MOB);
			List<String> xmxhs = service.getDZXM();
			if (xmxhs == null || xmxhs.size() <= 0) {
				bizResponse.isSuccess = false;
				bizResponse.message = "没有维护关联导管类型，请先维护";
				return bizResponse;
			}
			keepOrRoutingDateSource(DataSource.HRP);
			// 获取导管的XMLX(根据XMXH从V_MOB_HIS_YLSF获取)
			List<String> xmlxs = service.getXmlxByXmxh(xmxhs);
			// 获取KSSJ和JSSJ
			String now = timeService.now(DataSource.HRP);
			cal.setTime(sdf.parse(now));
			String kssj = sdf.format(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			String jssj = sdf.format(cal.getTime());
			// 获取病人列表
			bizResponse.datalist = service.getpationtList(kssj, jssj, brbq, jgid, xmxhs, xmlxs);
			bizResponse.isSuccess = true;
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			bizResponse.isSuccess = false;
			bizResponse.message = e.getMessage();
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			bizResponse.isSuccess = false;
			bizResponse.message = e.getMessage();
		} catch (Exception e) {
			bizResponse.isSuccess = false;
			bizResponse.message = e.getMessage();
		}
		return bizResponse;
	}

	/**
	 * 获取病人的需测数据和已测数据
	 * @param brbq
	 * @param jgid
	 * @param zyh
	 * @return
	 */
	public BizResponse<CatheterRespose> getCatheter(String brbq, String jgid, String zyh) {
		BizResponse<CatheterRespose> bizResponse = new BizResponse<>();
		CatheterRespose catheterRespose = new CatheterRespose();
		try {
			// 获取导管的XMXH
			keepOrRoutingDateSource(DataSource.MOB);
			List<String> xmxhs = service.getDZXM();
			if (xmxhs == null || xmxhs.size() <= 0) {
				bizResponse.isSuccess = false;
				bizResponse.message = "没有维护关联导管类型，请先维护";
				return bizResponse;
			}
			keepOrRoutingDateSource(DataSource.HRP);
			// 获取导管的XMLX(根据XMXH从V_MOB_HIS_YLSF获取)
			List<String> xmlxs = service.getXmlxByXmxh(xmxhs);
			// 获取KSSJ和JSSJ
			String now = timeService.now(DataSource.HRP);
			cal.setTime(sdf.parse(now));
			String kssj = sdf.format(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			String jssj = sdf.format(cal.getTime());
			// 获取需测列表
			catheterRespose.Table1 = service.getMeasurData(brbq, jgid, zyh, kssj, jssj, xmxhs, xmlxs);
			keepOrRoutingDateSource(DataSource.MOB);
			for (CatheterMeasurData catheterMeasurData : catheterRespose.Table1) {
				catheterMeasurData.spinners = service.getSpinnerData(catheterMeasurData.YPXH);
			}
			// 获取已测列表
			catheterRespose.Table2 = service.getYLGJL(brbq, jgid, zyh, kssj);
			for (CatheterYLGJLvo record : catheterRespose.Table2) {
				if (record.JLGH != null) {
					record.JLGH = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, record.JLGH);
				}
			}
			bizResponse.data = catheterRespose;
			bizResponse.isSuccess = true;
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			bizResponse.isSuccess = false;
			bizResponse.message = e.getMessage();
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			bizResponse.isSuccess = false;
			bizResponse.message = e.getMessage();
		} catch (Exception e) {
			bizResponse.isSuccess = false;
			bizResponse.message = e.getMessage();
		}
		return bizResponse;
	}

	/**
	 * 保存引流管数据，同步保存生命体征数据
	 * @param list
	 * @return
	 */
	public BizResponse<String> saveCatheter(List<CatheterYLGJLvo> list) {
		BizResponse<String> bizResponse = new BizResponse<>();
		List<LifeSignRealSaveDataItem> lifeSignList = new ArrayList<>();
		try {
			if (list == null || list.isEmpty()) {
				bizResponse.isSuccess = false;
				bizResponse.message = "没有需要保存的内容";
				return bizResponse;
			}
			String now = timeService.now(DataSource.MOB);
			String jlrq = now.substring(0, 10);

			keepOrRoutingDateSource(DataSource.HRP);
			SickPersonDetailVo patient = patientService
					.getPatientDetail(list.get(0).ZYH, list.get(0).JGID);

			List<Long> jlxhList = identity.getIdentityMax("IENR_YLGJL", list.size(),DataSource.MOB).datalist;
			List<Long> cjhList = identity.getIdentityMax("BQ_SMTZ", list.size(),DataSource.ENR).datalist;
			List<Long> cjzhList = identity.getIdentityMax("BQ_SMTZ_GROUP", list.size(),DataSource.ENR).datalist;

			keepOrRoutingDateSource(DataSource.ENR);
			for (int i = 0; i < list.size(); i++) {
				CatheterYLGJLvo catheter = list.get(i);
				// 体征数据
				String twdxs = service.getLifeSignTwdxsByXmh(catheter.TZXM);

				LifeSignRealSaveDataItem item = new LifeSignRealSaveDataItem();
				item.CJH = String.valueOf(cjhList.get(i));
				item.CJZH = String.valueOf(cjzhList.get(i));
				item.XMH = catheter.TZXM;
				item.CJSJ = now;
				item.JHBZ = "1";
				item.TWDXS = StringUtils.isBlank(twdxs) ? "0" : twdxs;
				item.ZYH = catheter.ZYH;
				item.BRBQ = catheter.BRBQ;
				item.BRKS = patient.BRKS;
				item.BRCH = patient.BRCH;
				item.TZNR = catheter.YLL;
				item.FCBZ = "0";
				item.JLSJ = now;
				item.JLGH = catheter.JLGH;
				item.ZFBZ = "0";
				item.YCBZ = "0";
				item.JGID = catheter.JGID;
				lifeSignList.add(item);

				// 引流管数据
				catheter.JLXH = jlxhList.get(i);
				catheter.JLSJ = now;
				catheter.CJH = item.CJH;
				catheter.JLRQ = jlrq;
			}

			keepOrRoutingDateSource(DataSource.MOB);
			service.saveCatheter(list);
			keepOrRoutingDateSource(DataSource.ENR);
			service.saveLifeSign(lifeSignList);

			bizResponse.isSuccess = true;
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			bizResponse.isSuccess = false;
			bizResponse.message = e.getMessage();
		} catch (Exception e) {
			bizResponse.isSuccess = false;
			bizResponse.message = e.getMessage();
		}
		return bizResponse;
	}

	/**
	 * 删除引流管记录，同时删除相关体征记录
	 * @param jlxh
	 * @param jgid
	 * @return
	 */
	public BizResponse<String> cancelCatheter(String jlxh, String jgid) {
		BizResponse<String> bizResponse = new BizResponse<>();
		try {
			keepOrRoutingDateSource(DataSource.MOB);
			// 获取CJH
			String cjh = service.getTzcjhByJlxh(jlxh, jgid);
			// 删除引流管记录
			service.cancelCatheter(jlxh, jgid);
			// 删除体征记录
			keepOrRoutingDateSource(DataSource.ENR);
			service.deleteSmtzFromCatheter(cjh, jgid);
			bizResponse.isSuccess = true;
		} catch (SQLException | DataAccessException e) {
			logger.error(e.getMessage());
			bizResponse.isSuccess = false;
			bizResponse.message = e.getMessage();
		} catch (Exception e) {
			bizResponse.isSuccess = false;
			bizResponse.message = e.getMessage();
		}
		return bizResponse;
	}
}
