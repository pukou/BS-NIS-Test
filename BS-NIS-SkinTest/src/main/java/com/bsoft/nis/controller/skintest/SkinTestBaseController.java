package com.bsoft.nis.controller.skintest;


import com.bsoft.nis.domain.skintest.SickerPersonSkinTest;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.skintest.SkinTestBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Classichu on 2017/11/16.
 * 中医护理
 */
@Controller
public class SkinTestBaseController {

    @Autowired
    SkinTestBaseService service;


    /**
     * 获取
     *
     * @param type
     * @param brbq 病人病区
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/skintest/get/getSkinTest")
    //@RequestMapping(value = {"auth/mobile/traditional/get/getZYZZJL","mobile/traditional/get/getZYZZJL"})
    public
    @ResponseBody
    Response<List<SickerPersonSkinTest>> getSkinTest(@RequestParam(value = "type") String type,
                                                     @RequestParam(value = "zyh") String zyh,
                                                     @RequestParam(value = "brbq") String brbq,
                                                     @RequestParam(value = "jgid") String jgid) {
        Response<List<SickerPersonSkinTest>> response = new Response<>();
        BizResponse<SickerPersonSkinTest> bizResponse;
        if (zyh == null || "".equals(zyh) || "null".equalsIgnoreCase(zyh)) {
            zyh = null;
        }
        bizResponse = service.getSkinTest(zyh, type, brbq, jgid);

        if (bizResponse != null && bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;

        return response;

    }

    @RequestMapping(value = "auth/mobile/skintest/post/saveSkinTest")
    public
    @ResponseBody
    Response<String> saveSkinTest(@RequestBody SickerPersonSkinTest skinTest) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.saveSkinTest(skinTest);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }
}
