package com.bsoft.nis.controller.dangerevaluate;

import com.bsoft.nis.domain.dangerevaluate.*;
import com.bsoft.nis.domain.evaluation.nursingeval.KeyValue;
import com.bsoft.nis.domain.synchron.SelectResult;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.dangerevaluate.DangerEvaluateMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 风险评估控制器
 * User: 苏泽雄
 * Date: 16/12/1
 * Time: 9:51:15
 */
@Controller
public class DangerEvaluateController {

	@Autowired
	DangerEvaluateMainService service;
	/**
	 * 获取风险评估列表
	 *
	 * @param zyh  住院号
	 * @param jgid 机构id
	 * @param bqid 病区id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/getDEList")
	//	@RequestMapping(value = "mobile/dangerevaluate/get/getDEList")
	@ResponseBody
	public Response<List<DEOverview>> getDEList(@RequestParam(value = "zyh") String zyh,
			@RequestParam(value = "jgid") String jgid,
			@RequestParam(value = "bqid") String bqid) {

		Response<List<DEOverview>> response = new Response<>();
		BizResponse<DEOverview> biz;
		biz = service.getDEList(zyh, jgid, bqid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.datalist;
		response.Msg = biz.message;

		return response;
	}



	/**
	 * 添加一条风险评估
	 *
	 * @param pgdh 评估单号
	 * @param pglx 评估类型
	 * @param jgid 机构id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/addDE")
	//	@RequestMapping(value = "mobile/dangerevaluate/get/addDE")
	@ResponseBody
	public Response<DERecord> addDangerEvaluate(@RequestParam(value = "pgdh") String pgdh,
			@RequestParam(value = "pglx") String pglx,
			@RequestParam(value = "jgid") String jgid) {

		Response<DERecord> response = new Response<>();
		BizResponse<DERecord> biz;
		biz = service.getNewDangerEvaluate(pgdh, pglx, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 获取第一条风险记录,不存在就添加一条
	 *
	 * @param zyh  住院号
	 * @param pgdh 评估单号
	 * @param pglx 评估类型
	 * @param jgid 机构id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/addOrGetDE")
	//	@RequestMapping(value = "mobile/dangerevaluate/get/addOrGetDE")
	@ResponseBody
	public Response<DERecord> addOrGetDangerEvaluate(@RequestParam(value = "zyh") String zyh,
			@RequestParam(value = "pgdh") String pgdh,
			@RequestParam(value = "pglx") String pglx,
			@RequestParam(value = "jgid") String jgid,
			@RequestParam(value = "hqfs") String hqfs) {

		Response<DERecord> response = new Response<>();
		BizResponse<DERecord> biz;
		biz = service.addOrGetDangerEvaluate(zyh, pgdh, pglx, jgid, hqfs);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 添加一条风险评估措施，如果已经存在则返回记录
	 *
	 * @param pgdh 评估单号
	 * @param pglx 评估类型
	 * @param pgxh 评估序号
	 * @param jlxh 记录序号
	 * @param jgid 机构id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/addDEMeasure")
	//	@RequestMapping(value = "mobile/dangerevaluate/get/addDEMeasure")
	@ResponseBody
	public Response<DEMeasure> addDEMeasure(@RequestParam(value = "pgdh") String pgdh,
			@RequestParam(value = "pglx") String pglx,
			@RequestParam(value = "pgxh") String pgxh,
			@RequestParam(value = "jlxh") String jlxh,
			@RequestParam(value = "jgid") String jgid) {

		Response<DEMeasure> response = new Response<>();
		BizResponse<DEMeasure> biz;
		biz = service.addDEMeasure(pgdh, pglx, pgxh, jlxh, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 保存风险评估记录
	 *
	 * @param data JSON格式的保存数据，包含DERecord,ZYH,BQID,JGID
	 * @return
	 */
	@RequestMapping(value = {"auth/mobile/dangerevaluate/post/saveDE", "mobile/dangerevaluate/post/saveDE"})
	//	@RequestMapping(value = "mobile/dangerevaluate/post/saveDE")
	@ResponseBody
	public Response<DERecord> saveDangerEvaluate(@RequestBody DERecordPostData data) {
		Response<DERecord> response = new Response<>();
		BizResponse<DERecord> biz;
		biz = service.saveDangerEvaluate(data);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}


	/**
	 * 获取评估措施列表
	 *
	 * @param pgdh 评估单号
	 * @param pgxh 评估序号
	 * @param zyh  住院号
	 * @param jgid 机构id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/getDEMeasureList")
	//	@RequestMapping(value = "mobile/dangerevaluate/get/getDEMeasureList")
	@ResponseBody
	public Response<List<MeasureOverview>> getDEMeasureList(
			@RequestParam(value = "pgdh") String pgdh,
			@RequestParam(value = "pgxh") String pgxh, @RequestParam(value = "zyh") String zyh,
			@RequestParam(value = "jgid") String jgid) {
		Response<List<MeasureOverview>> response = new Response<>();
		BizResponse<MeasureOverview> biz;
		biz = service.getDEMeasureList(pgdh, pgxh, zyh, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.datalist;
		response.Msg = biz.message;

		return response;
	}

	/**
	 *2017年4月28日09:11:11
	 * @param pglx
	 * @param jgid
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/getFXPGList")
	@ResponseBody
	public Response<List<DEPGHBean>> getFXPGList(
			@RequestParam(value = "pglx") String pglx,
			@RequestParam(value = "jgid") String jgid) {
		Response<List<DEPGHBean>> response = new Response<>();
		BizResponse<DEPGHBean> biz = service.getFXPGList(pglx , jgid);
		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.datalist;
		response.Msg = biz.message;
		return response;
	}
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/getCSPJList")
	@ResponseBody
	public Response<List<DEEvaluate>> getCSPJList(
			@RequestParam(value = "csdh") String csdh,
			@RequestParam(value = "jgid") String jgid) {
		Response<List<DEEvaluate>> response = new Response<>();
		BizResponse<DEEvaluate> biz = service.getCSPJList(csdh);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.datalist;
		response.Msg = biz.message;
		return response;
	}
	/**
	 * 保存评估措施记录
	 *
	 * @param data JSON格式的保存数据，包含MeasureRecord,PGDH,ZYH,BQID,JGID
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/post/saveDEMeasure")
	//	@RequestMapping(value = "mobile/dangerevaluate/post/saveDEMeasure")
	@ResponseBody
	public Response<DEMeasure> saveDEMeasure(@RequestBody MeasureRecordPostData data) {
		Response<DEMeasure> response = new Response<>();
		BizResponse<DEMeasure> biz;
		biz = service.saveDEMeasure(data);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 获取评估单记录
	 *
	 * @param pgxh 评估序号
	 * @param jgid 机构id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/getDERecord")
	//	@RequestMapping(value = "mobile/dangerevaluate/get/getDERecord")
	@ResponseBody
	public Response<DERecord> getDERecord(@RequestParam(value = "pgxh") String pgxh,
			@RequestParam(value = "jgid") String jgid) {
		Response<DERecord> response = new Response<>();
		BizResponse<DERecord> biz;
		biz = service.getDERecord(pgxh, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 删除评估记录
	 *
	 * @param pgxh 评估序号
	 * @param jgid 机构id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/deleteDERecord")
	//	@RequestMapping(value = "mobile/dangerevaluate/get/deleteDERecord")
	@ResponseBody
	public Response<String> deleteDERecord(@RequestParam(value = "pgxh") String pgxh,
			@RequestParam(value = "jgid") String jgid) {
		Response<String> response = new Response<>();
		BizResponse<String> biz;
		biz = service.deleteDERecord(pgxh, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/getPreOneCSJL")
	@ResponseBody
	public Response<KeyValue<String,String>> getPreOneCSJL(@RequestParam(value = "csdh") String csdh,@RequestParam(value = "pgxh") String pgxh,
										   @RequestParam(value = "zyh") String zyh) {
		Response<KeyValue<String,String>> response = new Response<>();
		BizResponse<KeyValue<String,String>> biz;
		biz = service.getPreOneCSJL(csdh,pgxh, zyh);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}
	/**
	 * 删除措施评价
	 *
	 * @param jlxh 记录序号
	 * @param jgid 机构id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/deleteDEMeasure")
	//	@RequestMapping(value = "mobile/dangerevaluate/get/deleteDEMeasure")
	@ResponseBody
	public Response<String> deleteDEMeasure(@RequestParam(value = "jlxh") String jlxh,
			@RequestParam(value = "jgid") String jgid) {
		Response<String> response = new Response<>();
		BizResponse<String> biz;
		biz = service.deleteDEMeasure(jlxh, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 护士长审阅
	 *
	 * @param pgxh  评估序号
	 * @param hszgh 护士长工号
	 * @param jgid  机构id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/checkDERecord")
	//	@RequestMapping(value = "mobile/dangerevaluate/get/checkDERecord")
	@ResponseBody
	public Response<DERecord> checkDERecord(@RequestParam(value = "pgxh") String pgxh,
			@RequestParam(value = "hszgh") String hszgh,
			@RequestParam(value = "jgid") String jgid) {
		Response<DERecord> response = new Response<>();
		BizResponse<DERecord> biz;
		biz = service.checkDERecord(pgxh, hszgh, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 评价风险措施
	 *
	 * @param jlxh 记录序号
	 * @param pjsj 评价时间  yyyy-MM-ddTHH:mm:ss
	 * @param pjjg 评价结果
	 * @param pjr  评价人
	 * @param pgdh 评估单号
	 * @param pgxh 评估序号
	 * @param jgid 机构id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/evaluateMeasure")
	//	@RequestMapping(value = "mobile/dangerevaluate/get/evaluateMeasure")
	@ResponseBody
	public Response<DEMeasure> evaluateMeasure(@RequestParam(value = "jlxh") String jlxh,
			@RequestParam(value = "pjsj") String pjsj,
			@RequestParam(value = "pjjg") String pjjg, @RequestParam(value = "pjr") String pjr,
			@RequestParam(value = "pgdh") String pgdh,
			@RequestParam(value = "pgxh") String pgxh,
			@RequestParam(value = "jgid") String jgid) {
		Response<DEMeasure> response = new Response<>();
		BizResponse<DEMeasure> biz;
		pjsj = pjsj.replace("T", " ");
		biz = service.evaluateMeasure(jlxh, pjsj, pjjg, pjr, pgdh, pgxh, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.data;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 获取当前的疼痛综合评估记录，没有时则新增
	 *
	 * @param jgid 机构id
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/get/addOrGetPE")
	//	@RequestMapping(value = "mobile/dangerevaluate/get/addOrGetPE")
	@ResponseBody
	public Response<List<PainEvaluate>> addOrGetPE(@RequestParam(value = "jgid") String jgid,
			@RequestParam(value = "pgxh") String pgxh) {
		Response<List<PainEvaluate>> response = new Response<>();
		BizResponse<PainEvaluate> biz;
		biz = service.addOrGetPE(pgxh, jgid);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.datalist;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 保存疼痛综合评估记录
	 *
	 * @param data JSON格式的保存数据，包含PEOption,ZYH,JGID,PGXH
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/post/savePE")
	//	@RequestMapping(value = "mobile/dangerevaluate/post/savePE")
	@ResponseBody
	public Response<List<PainEvaluate>> savePainEvaluate(@RequestBody PERecordPostData data) {
		Response<List<PainEvaluate>> response = new Response<>();
		BizResponse<PainEvaluate> biz;
		biz = service.savePainEvaluate(data);

		response.ReType = biz.isSuccess ? 0 : -1;
		response.Data = biz.datalist;
		response.Msg = biz.message;

		return response;
	}

	/**
	 * 调用表单后的第二次同步
	 *
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "auth/mobile/dangerevaluate/post/synchronRepeat")
	@ResponseBody
	public Response<String> synchronRepeat(@RequestBody SelectResult data) {
		return service.synchronRepeat(data);
	}
}
