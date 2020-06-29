package com.bsoft.nis.controller.bloodglucose;

import com.bsoft.nis.domain.bloodglucose.BGHistoryData;
import com.bsoft.nis.domain.bloodglucose.BGSavePostData;
import com.bsoft.nis.domain.bloodglucose.BloodGlucoseDetail;
import com.bsoft.nis.domain.bloodglucose.BloodGlucoseRecord;
import com.bsoft.nis.domain.bloodglucose.GlucoseTimeData;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.bloodglucose.BloodGlucoseMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 血糖治疗记录控制器
 * User: 苏泽雄
 * Date: 16/12/23
 * Time: 14:03:34
 */
@Controller
public class BloodGlucoseController {

	@Autowired
	BloodGlucoseMainService service;

	/**
	 * 获取血糖治疗历史记录
	 *
	 * @param zyh  住院号
	 * @param kssj 开始时间
	 * @param jssj 结束时间
	 * @param brbq 病人病区
	 * @param jgid 机构id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/bloodglucose/get/getBGHistory")
	@ResponseBody
	public Response<BGHistoryData> getBloodGlucoseHistory(@RequestParam(value = "zyh") String zyh,
			@RequestParam(value = "kssj") String kssj,
			@RequestParam(value = "jssj") String jssj,
			@RequestParam(value = "brbq") String brbq,
			@RequestParam(value = "jgid") String jgid) {
		Response<BGHistoryData> response = new Response<>();
		BizResponse<BGHistoryData> biz;
		biz = service.getBloodGlucoseHistory(zyh, kssj, jssj, brbq, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 获取血糖治疗的时间点
	 *
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/bloodglucose/get/getGlucoseTimes")
	@ResponseBody
	public Response<GlucoseTimeData> getGlucoseTimes() {
		Response<GlucoseTimeData> response = new Response<>();
		BizResponse<GlucoseTimeData> biz;
		biz = service.getGlucoseTimes();

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 获取血糖治疗列表
	 *
	 * @param xmlx 项目类型  1 血糖  2 胰岛素
	 * @param zyh  住院号
	 * @param jhrq 查询日期  'yyyy-MM-dd'
	 * @param brbq 病人病区
	 * @param jgid 机构id
	 * @param xmxh 项目序号(时间点)
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/bloodglucose/get/getBGList")
	@ResponseBody
	public Response<BloodGlucoseRecord> getBGList(@RequestParam(value = "xmlx") String xmlx,
			@RequestParam(value = "zyh") String zyh, @RequestParam(value = "jhrq") String jhrq,
			@RequestParam(value = "brbq") String brbq,
			@RequestParam(value = "jgid") String jgid,
			@RequestParam(value = "xmxh") String xmxh) {
		Response<BloodGlucoseRecord> response = new Response<>();
		BizResponse<BloodGlucoseRecord> biz;
		biz = service.getBGList(xmlx, zyh, jhrq, brbq, jgid, xmxh);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

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
	@RequestMapping(value = "auth/mobile/bloodglucose/get/addDetail")
	@ResponseBody
	public Response<BloodGlucoseDetail> addDetail(@RequestParam(value = "zyh") String zyh,
			@RequestParam(value = "jhrq") String jhrq,
			@RequestParam(value = "brbq") String brbq,
			@RequestParam(value = "jgid") String jgid,
			@RequestParam(value = "xmxh") String xmxh,
			@RequestParam(value = "xmnr") String xmnr) {
		Response<BloodGlucoseDetail> response = new Response<>();
		BizResponse<BloodGlucoseDetail> biz;
		biz = service.addDetail(zyh, jhrq, brbq, jgid, xmxh, xmnr);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

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
	@RequestMapping(value = "auth/mobile/bloodglucose/get/getUnexecutedBG")
	@ResponseBody
	public Response<BloodGlucoseRecord> getUnexecutedBG(
			@RequestParam(value = "xmlx") String xmlx,
			@RequestParam(value = "zyh") String zyh, @RequestParam(value = "jhrq") String jhrq,
			@RequestParam(value = "brbq") String brbq,
			@RequestParam(value = "xmxh") String xmxh,
			@RequestParam(value = "jgid") String jgid) {
		Response<BloodGlucoseRecord> response = new Response<>();
		BizResponse<BloodGlucoseRecord> biz;
		biz = service.getUnexecutedBG(xmlx, zyh, jhrq, brbq, xmxh, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 保存血糖记录明细
	 *
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/bloodglucose/post/saveBloodGlucose")
	@ResponseBody
	public Response<BloodGlucoseRecord> saveBloodGlucose(
			@RequestBody BGSavePostData data) {
		Response<BloodGlucoseRecord> response = new Response<>();
		BizResponse<BloodGlucoseRecord> biz;
		biz = service.saveBloodGlucose(data);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 获取血糖治疗病人
	 *
	 * @param bqid
	 * @param jhrq  计划日期
	 * @param xmlx  项目类型  1 血糖  2 胰岛素
	 * @param xmxh  项目序号(时间点)
	 * @param hsgh
	 * @param jgid
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/bloodglucose/get/GetPatientList")
	@ResponseBody
	public Response<List<SickPersonVo>> GetPatientList(
			@RequestParam(value = "bqid") String bqid,
			@RequestParam(value = "jhrq") String jhrq,
			@RequestParam(value = "xmlx") String xmlx,
			@RequestParam(value = "xmxh") String xmxh,
			@RequestParam(value = "hsgh", required = false) String hsgh,
			@RequestParam(value = "jgid") String jgid) {
		Response<List<SickPersonVo>> response = new Response<>();
		BizResponse<SickPersonVo> biz;
		biz = service.GetPatientList(bqid, jhrq, xmlx, xmxh, hsgh, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.datalist;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 删除明细（只有临时的血糖记录可以删除）
	 * @param mxxh
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/bloodglucose/get/deleteDetail")
	@ResponseBody
	public Response<String> deleteDetail(@RequestParam(value = "mxxh") String mxxh) {
		Response<String> response = new Response<>();
		BizResponse<String> biz;
		biz = service.deleteDetail(mxxh);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}
}
