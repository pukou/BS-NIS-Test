package com.bsoft.nis.service.dailycare;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.dailycare.DailySecondItem;
import com.bsoft.nis.domain.dailycare.DailyTopItem;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.dailycare.support.DailyCareBaseServiceSup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by king on 2016/10/28.
 */
@Service
public class DailyCareBaseService extends RouteDataSourceService {

	@Autowired
	DailyCareBaseServiceSup service;

	@Autowired
	IdentityService identityService;

	@Autowired
	DateTimeService timeService; // 日期时间服务

	public BizResponse<DailyTopItem> getDailyNurseType(String ksdm, String jgid) {
		keepOrRoutingDateSource(DataSource.MOB);
		BizResponse<DailyTopItem> response = new BizResponse<>();

		try {
			response.datalist = service.getDailyNurseType(ksdm, jgid);
			response.isSuccess = true;
			response.message = "护理类别获取成功!";
		} catch (SQLException | DataAccessException e) {
			response.isSuccess = false;
			response.message = "【获取护理类别】数据库查询错误";
		} catch (Exception e) {
			response.isSuccess = false;
			response.message = "【获取护理类别】服务内部错误";
		}
		return response;
	}


	public BizResponse<DailySecondItem> getDailyNurseList(String type, String jgid,
			int sysType) {
		keepOrRoutingDateSource(DataSource.MOB);
		BizResponse<DailySecondItem> response = new BizResponse<>();

		try {
			response.datalist = service.getDailyNurseList(type, jgid, sysType);
			response.isSuccess = true;
			response.message = "护理常规数据获取成功!";
		} catch (SQLException | DataAccessException e) {
			response.isSuccess = false;
			response.message = "【获取护理常规数据】数据库查询错误";
		} catch (Exception e) {
			response.isSuccess = false;
			response.message = "【获取护理常规数据】服务内部错误";
		}
		return response;
	}


	public BizResponse<String> SaveDailyNurseItems(String brbq, String zyh, List<String> xmbs,
			String urid, String jgid) {
		BizResponse<String> response = new BizResponse<>();

		String zxsj = timeService.now(DataSource.HRP);
		try {
			for (String xmb : xmbs) {
				Long idList = identityService.getIdentityMax("IENR_BRHL",DataSource.MOB);
				keepOrRoutingDateSource(DataSource.MOB);
				service.SaveDailyNurseItems(idList, zyh, xmb, "0", urid, zxsj, brbq, jgid);
			}
			response.isSuccess = true;
			response.message = "常规护理项目保存成功!";
		} catch (SQLException | DataAccessException e) {
			response.isSuccess = false;
			response.message = "【获取常规护理项目】数据库保存错误";
		} catch (Exception e) {
			response.isSuccess = false;
			response.message = "【获取常规护理项目】服务内部错误";
		}
		return response;
	}

}
