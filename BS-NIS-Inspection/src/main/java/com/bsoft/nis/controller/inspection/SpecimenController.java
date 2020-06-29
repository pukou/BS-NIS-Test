package com.bsoft.nis.controller.inspection;

import com.bsoft.nis.domain.inspection.CYInfoBean;
import com.bsoft.nis.domain.inspection.SpecimenVo;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.inspection.SpecimenMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 标本采集控制器
 * Created by Administrator on 2016/10/10.
 */
@Controller
public class SpecimenController {

    @Autowired
    SpecimenMainService service;

    /**
     * 获取检验采集数据
     *
     * @param zyh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/inspection/GetSpecimenList")
    public
    @ResponseBody
    Response<List<SpecimenVo>> GetSpecimenList(@RequestParam(value = "zyh") String zyh,
                                               @RequestParam(value = "jgid") String jgid) {
        Response<List<SpecimenVo>> response = new Response<>();
        BizResponse<SpecimenVo> bizResponse = new BizResponse<SpecimenVo>();
        bizResponse = service.GetSpecimenList(zyh, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

    @RequestMapping(value = "auth/mobile/inspection/GetCYInfoByTMBH")
    public
    @ResponseBody
    Response<List<CYInfoBean>> GetCYInfoByTMBH(@RequestParam(value = "brid") String brid,
                                               @RequestParam(value = "tmbh") String tmbh,
                                               @RequestParam(value = "jgid") String jgid) {
        Response<List<CYInfoBean>> response = new Response<>();
        BizResponse<CYInfoBean> bizResponse = new BizResponse<>();
        bizResponse = service.GetCYInfoByTMBH(tmbh, brid,jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

    /**
     * 获取标本采集历史记录
     *
     * @param zyh
     * @param start
     * @param end
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/inspection/GetHistorySpecimenList")
    public
    @ResponseBody
    Response<List<SpecimenVo>> GetHistorySpecimenList(@RequestParam(value = "zyh") String zyh,
                                                      @RequestParam(value = "start") String start,
                                                      @RequestParam(value = "end") String end,
                                                      @RequestParam(value = "jgid") String jgid) {
        Response<List<SpecimenVo>> response = new Response<>();
        BizResponse<SpecimenVo> bizResponse = new BizResponse<SpecimenVo>();
        bizResponse = service.GetHistorySpecimenList(zyh, start, end, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

    /**
     * 检验执行
     *
     * @param zyh
     * @param urid
     * @param tmbh
     * @param isScan
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/inspection/ExecuteSpecimen")
    public
    @ResponseBody
    Response<String> ExecuteSpecimen(@RequestParam(value = "zyh") String zyh,
                                     @RequestParam(value = "urid") String urid,
                                     @RequestParam(value = "tmbh") String tmbh,
                                     @RequestParam(value = "isScan") String isScan,
                                     @RequestParam(value = "sbmc") String sbmc,
                                     @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<String>();
        bizResponse = service.ExecuteSpecimen(zyh, urid, tmbh, isScan, sbmc, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 撤销操作
     *
     * @param zyh
     * @param urid
     * @param tmbh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/inspection/CancelSpecimen")
    public
    @ResponseBody
    Response<String> CancelSpecimen(@RequestParam(value = "zyh") String zyh,
                                    @RequestParam(value = "urid") String urid,
                                    @RequestParam(value = "tmbh") String tmbh,
                                    @RequestParam(value = "sbmc") String sbmc,
                                    @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<String>();
        bizResponse = service.CancelSpecimen(zyh, urid, tmbh, sbmc, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        return response;
    }


    /**
     * 检验发放
     *
     * @param zyh
     * @param urid
     * @param tmbh
     * @param isScan
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/inspection/Delivery")
    public
    @ResponseBody
    Response<String> Delivery(@RequestParam(value = "zyh") String zyh,
                              @RequestParam(value = "urid") String urid,
                              @RequestParam(value = "tmbh") String tmbh,
                              @RequestParam(value = "isScan") String isScan,
                              @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<String>();
        bizResponse = service.Delivery(zyh, urid, tmbh, isScan, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        return response;
    }


    /**
     * 根据病区代码获取检验采集病人列表
     *
     * @param bqdm
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/inspection/GetPatientList")
    public
    @ResponseBody
    Response<List<SickPersonVo>> GetPatientList(@RequestParam(value = "bqdm") String bqdm,
                                                @RequestParam(value = "jgid") String jgid,
                                                @RequestParam(value = "mCheckBoxFiter") String mCheckBoxFiter,
                                                @RequestParam(value = "mTypeFilterPos") String mTypeFilterPos) {
        Response<List<SickPersonVo>> response = new Response<>();
        BizResponse<SickPersonVo> bizResponse = new BizResponse<SickPersonVo>();
        bizResponse = service.GetPatientList(bqdm, jgid,mCheckBoxFiter,mTypeFilterPos);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Data = bizResponse.datalist;
        response.Msg = bizResponse.message;
        return response;
    }

}
