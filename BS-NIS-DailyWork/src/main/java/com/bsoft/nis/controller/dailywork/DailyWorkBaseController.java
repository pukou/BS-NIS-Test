package com.bsoft.nis.controller.dailywork;

import com.bsoft.nis.domain.dailywork.DailyWorkCount;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.dailywork.DailyWorkBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by king on 2016/11/17.
 */
@Controller
public class DailyWorkBaseController {

    @Autowired
    DailyWorkBaseService service;

    @RequestMapping(value = "auth/mobile/dailywork/get/GetMission")
    public
    @ResponseBody
    Response<DailyWorkCount> getMission(@RequestParam(value = "brbq") String brbq,
                                        @RequestParam(value = "hsgh") String hsgh,
                                        @RequestParam(value = "gzsj") String gzsj,
                                        @RequestParam(value = "jgid") String jgid,
                                        @RequestParam(value = "sysType") String sysType){
        Response<DailyWorkCount> response = new Response<>();
        BizResponse<DailyWorkCount> bizResponse;
        bizResponse = service.getMission(brbq,hsgh, gzsj, jgid);

        if(bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;

        return response;

    }
}
