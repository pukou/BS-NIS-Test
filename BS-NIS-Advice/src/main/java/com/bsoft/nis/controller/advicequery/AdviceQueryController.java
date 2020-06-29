package com.bsoft.nis.controller.advicequery;

import com.bsoft.nis.domain.adviceexecute.PhraseModel;
import com.bsoft.nis.domain.adviceqyery.AdviceDetail;
import com.bsoft.nis.domain.adviceqyery.AdviceVo;
import com.bsoft.nis.domain.adviceqyery.TransfusionData;
import com.bsoft.nis.domain.adviceqyery.TransfusionPatrolRecord;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.advicequery.AdviceQueryMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 医嘱执行(查询)控制器
 * User: 苏泽雄
 * Date: 16/12/16
 * Time: 17:08:08
 */
@Controller
public class AdviceQueryController {

    @Autowired
    AdviceQueryMainService service;

    /**
     * 获取医嘱列表
     *
     * @param zyh  住院号
     * @param lsyz 临时医嘱
     * @param wxbz 无效标志
     * @param kssj 开始时间
     * @param jssj 结束时间
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/get/getAdviceList")
    @ResponseBody
    public Response<List<AdviceVo>> getAdviceList(@RequestParam(value = "zyh") String zyh,
                                                  @RequestParam(value = "lsyz") String lsyz,
                                                  @RequestParam(value = "wxbz") String wxbz,
                                                  @RequestParam(value = "kssj") String kssj,
                                                  @RequestParam(value = "jssj") String jssj,
                                                  @RequestParam(value = "jgid") String jgid) {
        Response<List<AdviceVo>> response = new Response<>();
        BizResponse<AdviceVo> biz;
        biz = service.getAdviceList(zyh, lsyz, wxbz, kssj, jssj, jgid);

        response.ReType = biz.isSuccess ? 0 : -1;
        response.Data = biz.datalist;
        response.Msg = biz.message;

        return response;
    }

    /**
     * 取医嘱明细(执行记录)
     *
     * @param jlxh 记录序号(医嘱序号）
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/get/getAdviceDetail")
    @ResponseBody
    public Response<List<AdviceDetail>> getAdviceDetail(
            @RequestParam(value = "jlxh") String jlxh,
            @RequestParam(value = "jgid") String jgid) {
        Response<List<AdviceDetail>> response = new Response<>();
        BizResponse<AdviceDetail> biz;
        biz = service.getAdviceDetail(jlxh, jgid);

        response.ReType = biz.isSuccess ? 0 : -1;
        response.Data = biz.datalist;
        response.Msg = biz.message;

        return response;
    }

    /**
     * 获取病人输液单以及明细信息
     *
     * @param zyh  住院号
     * @param ksrq 开始日期（当天日期）
     * @param syzt 输液状态  2 正在输液  空 全部
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/get/getTransfusionListPatient")
    @ResponseBody
    public Response<TransfusionData> getTransfusionListPatient(
            @RequestParam(value = "zyh") String zyh, @RequestParam(value = "ksrq") String ksrq,
            @RequestParam(value = "syzt") String syzt,
            @RequestParam(value = "jgid") String jgid) {
        Response<TransfusionData> response = new Response<>();
        BizResponse<TransfusionData> biz;
        biz = service.getTransfusionListPatient(zyh, ksrq, syzt, jgid);

        response.ReType = biz.isSuccess ? 0 : -1;
        response.Data = biz.data;
        response.Msg = biz.message;

        return response;
    }

    /**
     * 查询单条输液
     *
     * @param sydh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/get/GetTransfusion")
    @ResponseBody
    public Response<TransfusionData> GetTransfusion(
            @RequestParam(value = "sydh") String sydh,
            @RequestParam(value = "jgid") String jgid) {
        Response<TransfusionData> response = new Response<>();
        BizResponse<TransfusionPatrolRecord> biz;
        biz = service.GetTransfusion(sydh, jgid);

        response.ReType = biz.isSuccess ? 0 : -1;
        TransfusionData transfusionData = new TransfusionData();
        transfusionData.SYXS = biz.datalist;
        response.Data = transfusionData;
        response.Msg = biz.message;

        return response;
    }

    /**
     * 获取输液反应类型
     *
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/get/GetTransfusionReaction")
    @ResponseBody
    public Response<TransfusionData> GetTransfusionReaction(@RequestParam(value = "jgid") String jgid) {
        Response<TransfusionData> response = new Response<>();
        BizResponse<PhraseModel> biz;
        biz = service.GetTransfusionReaction(jgid);
        response.ReType = biz.isSuccess ? 0 : -1;
        TransfusionData transfusionData = new TransfusionData();
        transfusionData.SYFY = biz.datalist;
        response.Data = transfusionData;
        response.Msg = biz.message;

        return response;
    }

    /**
     * 获取病人列表
     *
     * @param bqid
     * @param type      医嘱相关包括：2 医嘱  3 口服单  4 注射单  5 输液单
     * @param starttime
     * @param endtime
     * @param hsgh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/get/GetPatientList")
    @ResponseBody
    public Response<List<SickPersonVo>> GetPatientList(
            @RequestParam(value = "bqid") String bqid, @RequestParam(value = "type") int type,
            @RequestParam(value = "starttime", required = false) int starttime,
            @RequestParam(value = "endtime", required = false) int endtime,
            @RequestParam(value = "hsgh", required = false) String hsgh,
            @RequestParam(value = "jgid") String jgid) {
        Response<List<SickPersonVo>> response = new Response<>();
        BizResponse<SickPersonVo> biz;
        biz = service.GetPatientList(bqid, type, starttime, endtime, hsgh, jgid);

        response.ReType = biz.isSuccess ? 0 : -1;
        response.Data = biz.datalist;
        response.Msg = biz.message;

        return response;
    }
}
