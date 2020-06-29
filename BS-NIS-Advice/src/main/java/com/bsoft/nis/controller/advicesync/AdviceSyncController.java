package com.bsoft.nis.controller.advicesync;

import com.bsoft.nis.domain.adviceqyery.AdviceVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.adviceSync.AdviceSyncMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 医嘱同步（病区医嘱）主服务
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-03-01
 * Time: 16:58
 * Version:
 */
@Controller
public class AdviceSyncController {

    @Autowired
    AdviceSyncMainService service;

    /**
     * 根据住院号码同步病区医嘱计划
     *
     * @param zyh  住院号
     * @param kssj 开始时间
     * @param jssj 结束时间
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "mobile/advice/get/syncPlanByZyh")
    @ResponseBody
    public Response<String> syncPlanByZyh(@RequestParam(value = "zyh") String zyh,
                                          @RequestParam(value = "kssj") String kssj,
                                          @RequestParam(value = "jssj") String jssj,
                                          @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> biz;
        biz = service.syncPlanByZyh(zyh, kssj, jssj, jgid);

        response.ReType = biz.isSuccess ? 0 : -1;
        response.Data = biz.data;
        response.Msg = biz.message;

        return response;
    }

    /**
     * 根据（关联）计划号列表同步病区医嘱计划
     *
     * @param jhh  关联计划号列表
     *             gljhh1,gljhh2,gljhh3,...
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "mobile/advice/get/syncPlanByJhh")
    @ResponseBody
    public Response<String> syncPlanByJhh(@RequestParam(value = "jhh") String jhh,
                                          @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> biz;
        biz = service.syncPlanByJhh(jhh, jgid);

        response.ReType = biz.isSuccess ? 0 : -1;
        response.Data = biz.data;
        response.Msg = biz.message;

        return response;
    }

    /**
     * 根据病区代码同步病区医嘱计划
     *
     * @param bqdm 病区代码
     * @param kssj 开始时间
     * @param jssj 结束时间
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "mobile/advice/get/syncPlanByBq")
    @ResponseBody
    public Response<String> syncPlanByBq(@RequestParam(value = "bqdm") String bqdm,
                                         @RequestParam(value = "kssj") String kssj,
                                         @RequestParam(value = "jssj") String jssj,
                                         @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> biz;
        biz = service.syncPlanByBq(bqdm, kssj, jssj, jgid);

        response.ReType = biz.isSuccess ? 0 : -1;
        response.Data = biz.data;
        response.Msg = biz.message;

        return response;
    }
}
