package com.bsoft.nis.controller.oralmedication;

import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.oralmedication.OralMedicationMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 包药机控制器
 * Created by Administrator on 2016/10/10.
 */
@Controller
public class OralMedicationController {

    @Autowired
    OralMedicationMainService service;


    //@RequestMapping("/OMSService/Mobile")
    //public @ResponseBody String sendData(@RequestParam(value = "data") String xml){
    @RequestMapping(value = "/OMSService/Mobile", method = RequestMethod.POST)
    public @ResponseBody String sendData(@RequestBody String xml){
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        BizResponse<String> biz = service.sendData(xml);
        String status = biz.isSuccess ? "0" : "1";

        sb.append("<result>");
        sb.append("<status>").append(status).append("</status>");
        sb.append("<message>").append(biz.message == null ? "" : biz.message).append("</message>");
        sb.append("</result>");
        String returnXML = sb.toString();
        return returnXML;
    }
}

