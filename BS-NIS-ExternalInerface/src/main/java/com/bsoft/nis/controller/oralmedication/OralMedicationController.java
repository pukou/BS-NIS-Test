package com.bsoft.nis.controller.oralmedication;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.oralmedication.OralMedicationMainService;
import com.bsoft.nis.service.oralmedication.PTSFMainService;
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

    @Autowired
    PTSFMainService ptsfMainService;


    //@RequestMapping("/OMSService/Mobile")
    //public @ResponseBody String sendData(@RequestParam(value = "data") String xml){
    @RequestMapping(value = "/OMSService/Mobile", method = RequestMethod.POST)
    public @ResponseBody String sendData(@RequestBody String xml){
        //新增平台访问权限
        if ("0".equals(getPTSF())){
            return "平台无权限访问该医院接口 No such permission!";
        }
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

    /**
     * @Author ling
     * @Description //平台调用该接口时判断该医院是否通过 1为通过 0为不通过
     * @Date 18:08 2020/6/29/0029
     * @Param []
     * @return java.lang.String
     **/
    public String getPTSF(){
        DataSource ds = DataSource.MOB;
        String sql = "select gsjb from MOB_YHCS t where t.csmc = 'IENR_PTSF' and t.csbz = '平台访问移动护理接口'";
        String value = null;
        value = ptsfMainService.getPTSFBySQL(sql);
        return value;
    }
}

