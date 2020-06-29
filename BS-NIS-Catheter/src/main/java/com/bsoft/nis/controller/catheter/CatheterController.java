package com.bsoft.nis.controller.catheter;

import com.bsoft.nis.domain.catheter.CatheterRespose;
import com.bsoft.nis.domain.catheter.db.CatheterYLGJLvo;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.catheter.CatheterMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2016/11/25.
 */
@Controller
public class CatheterController {
    @Autowired
    CatheterMainService service;


    /**
     * 获取病人列表
     *
     * @param brbq
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/catheter/getpationtList")
    public
    @ResponseBody
    Response<List<SickPersonVo>> getpationtList(@RequestParam(value = "brbq") String brbq,
                                                @RequestParam(value = "jgid") String jgid) {
        Response<List<SickPersonVo>> response = new Response<>();
        BizResponse<SickPersonVo> bizResponse = new BizResponse<>();
        bizResponse = service.getpationtList(brbq, jgid);
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.datalist;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }


    /**
     * 获取病人信息
     *
     * @param brbq
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/catheter/getCatheter")
    public
    @ResponseBody
    Response<CatheterRespose> getCatheter(@RequestParam(value = "brbq") String brbq,
                                          @RequestParam(value = "jgid") String jgid,
                                          @RequestParam(value = "zyh") String zyh) {
        Response<CatheterRespose> response = new Response<>();
        BizResponse<CatheterRespose> bizResponse = new BizResponse<>();
        bizResponse = service.getCatheter(brbq, jgid, zyh);
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.data;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }



    /**
     * 保存数据
     *
     * @param list
     * @return
     */
    @RequestMapping(value = "auth/mobile/catheter/saveCatheter")
    public
    @ResponseBody
    Response<String> saveCatheter(@RequestBody List<CatheterYLGJLvo> list) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();
        if (list == null || list.size() <= 0) {
            response.ReType = -1;
            response.Msg = "没有要保存的数据";
        }
        bizResponse = service.saveCatheter(list);
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.data;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    /**
     * 删除数据
     *
     * @param jlxh
     * @return
     */
    @RequestMapping(value = "auth/mobile/catheter/cancelCatheter")
    public
    @ResponseBody
    Response<String> cancelCatheter(@RequestParam(value = "jlxh") String jlxh,
                                    @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();
        bizResponse = service.cancelCatheter(jlxh, jgid);
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.data;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }
}
