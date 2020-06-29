package com.bsoft.nis.controller.dailycare;

import com.bsoft.nis.domain.dailycare.DailySecondItem;
import com.bsoft.nis.domain.dailycare.DailyTopItem;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.dailycare.DailyCareBaseService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 护理常规控制器
 * Created by king on 2016/10/28.
 */
@Controller
public class DailyCareBaseController {


    @Autowired
    DailyCareBaseService service;

    /**
     * 获取护理常规一级列表
     *
     * @param ksdm    科室代码
     * @param jgid    机构id
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/dailycare/get/GetDailyNurseType")
    public
    @ResponseBody
    Response<List<DailyTopItem>> getDailyNurseType(@RequestParam(value = "ksdm") String ksdm,
                                                   @RequestParam(value = "jgid") String jgid,
                                                   @RequestParam(value = "sysType", required = false) int sysType) {
        Response<List<DailyTopItem>> response = new Response<>();
        BizResponse<DailyTopItem> bizResponse = new BizResponse<>();
        bizResponse = service.getDailyNurseType(ksdm, jgid);

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
     * 获取护理常规二级列表
     *
     * @param type    列表编号
     * @param jgid    机构id
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/dailycare/get/GetDailyNurseList")
    public
    @ResponseBody
    Response<List<DailySecondItem>> getDailyNurseList(@RequestParam(value = "type") String type,
                                                      @RequestParam(value = "jgid") String jgid,
                                                      @RequestParam(value = "sysType", required = false) int sysType) {
        Response<List<DailySecondItem>> response = new Response<>();
        BizResponse<DailySecondItem> bizResponse = new BizResponse<>();

        bizResponse = service.getDailyNurseList(type, jgid, sysType);

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
     * 保存常规护理项目
     *
     * @param brbq     病人病区
     * @param zyh      住院号
     * @param listXMBS 项目标识列表
     * @param urid     用户id
     * @param jgid     机构id
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/dailycare/post/SaveDailyNurseItems")
    public
    @ResponseBody
    Response<String> saveDailyNurseItems(@RequestParam(value = "brbq") String brbq,
                                         @RequestParam(value = "zyh") String zyh,
                                         @RequestParam(value = "listXMBS") String listXMBS,
                                         @RequestParam(value = "urid") String urid,
                                         @RequestParam(value = "jgid") String jgid,
                                         @RequestParam(value = "sysType", required = false) int sysType) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        ObjectMapper mapper = new ObjectMapper();
        List<String> xmbs = new ArrayList<>();
        try {
            xmbs = mapper.readValue(listXMBS, new TypeReference<List<String>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        bizResponse = service.SaveDailyNurseItems(brbq, zyh, xmbs, urid, jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;

        return response;

    }
}
