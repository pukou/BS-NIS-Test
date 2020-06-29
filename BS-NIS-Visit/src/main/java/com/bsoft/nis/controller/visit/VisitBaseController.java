package com.bsoft.nis.controller.visit;

import com.bsoft.nis.domain.visit.VisitCount;
import com.bsoft.nis.domain.visit.VisitHistory;
import com.bsoft.nis.domain.visit.VisitPerson;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.visit.VisitBaseService;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by king on 2016/11/18.
 */
@Controller
public class VisitBaseController {

    @Autowired
    VisitBaseService service;

    /**
     * 获取巡视记录
     *
     * @param zyh     住院号
     * @param xsrq    巡视日期
     * @param jgid    机构id
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/visit/get/GetPatrolHistory")
    public
    @ResponseBody
    Response<List<VisitHistory>> getPatrolHistory(@RequestParam(value = "zyh") String zyh,
                                                  @RequestParam(value = "xsrq") String xsrq,
                                                  @RequestParam(value = "jgid") String jgid,
                                                  @RequestParam(value = "sysType") String sysType) {

        Response<List<VisitHistory>> response = new Response<>();
        BizResponse<VisitHistory> bizResponse;

        bizResponse = service.getPatrolHistory(zyh, xsrq, jgid);

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
     * 获取巡视记录，巡视病人
     *
     * @param urid    用户id
     * @param ksdm    科室代码
     * @param jgid    机构id
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/visit/get/GetPatrol")
    public
    @ResponseBody
    Response<VisitCount> getPatrol(@RequestParam(value = "urid") String urid,
                                   @RequestParam(value = "ksdm") String ksdm,
                                   @RequestParam(value = "jgid") String jgid,
                                   @RequestParam(value = "sysType") String sysType) {

        Response<VisitCount> response = new Response<>();
        BizResponse<VisitCount> bizResponse;

        bizResponse = service.getPatrol(urid, ksdm, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;

        return response;

    }

    @RequestMapping(value = {"auth/mobile/visit/del/delPatrol", "mobile/visit/del/delPatrol"})
    public
    @ResponseBody
    Response<String> delPatrol(@RequestParam(value = "xsbs") String xsbs,
                                   @RequestParam(value = "urid") String urid,
                                   @RequestParam(value = "jgid") String jgid) {

        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = service.delPatrol(xsbs, urid, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;

        return response;

    }

    /**
     * 扫描巡视
     *
     * @param brbq    病区
     * @param urid    护士id
     * @param sanStr  扫描标志
     * @param xsqk    巡视情况
     * @param jgid    机构id
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/visit/post/SetPatrolForScan")
    public
    @ResponseBody
    Response<List<VisitPerson>> setPatrolForScan(@RequestParam(value = "brbq") String brbq,
                                                 @RequestParam(value = "urid") String urid,
                                                 @RequestParam(value = "sanStr") String sanStr,
                                                 @RequestParam(value = "xsqk") String xsqk,
                                                 @RequestParam(value = "jgid") String jgid,
                                                 @RequestParam(value = "sysType") int sysType) {
        Response<List<VisitPerson>> response = new Response<>();
        BizResponse<VisitPerson> bizResponse;

        bizResponse = service.setPatrolForScan(brbq, urid, sanStr, xsqk, jgid);
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
     * 手动巡视
     *
     * @param brbq    病人病区
     * @param urid    护士id
     * @param zyh     住院号
     * @param xsqk    巡视情况
     * @param jgid    机构id
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/visit/post/SetPatrol")
    public
    @ResponseBody
    Response<List<VisitPerson>> SetPatrol(@RequestParam(value = "isScan") String isScan,
                                          @RequestParam(value = "brbq") String brbq,
                                          @RequestParam(value = "urid") String urid,
                                          @RequestParam(value = "zyh") String zyh,
                                          @RequestParam(value = "xsqk") String xsqk,
                                          @RequestParam(value = "jgid") String jgid,
                                          @RequestParam(value = "sysType") int sysType) {

        Response<List<VisitPerson>> response = new Response<>();
        BizResponse<VisitPerson> bizResponse;

        bizResponse = service.setPatrol(isScan,brbq, urid, zyh, xsqk, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;

        return response;
    }
    /*升级编号【56010027】============================================= start
                         处理房间条码、获取房间病人处理
            ================= classichu 2018/3/22 10:41
            */
    @RequestMapping(value = "auth/mobile/visit/post/SetPatrol_Some")
    public
    @ResponseBody
    Response<List<VisitPerson>> SetPatrol_Some(@RequestParam(value = "brbq") String brbq,
                                               @RequestParam(value = "urid") String urid,
                                               @RequestParam(value = "zyh_list") String zyh_list,
                                               @RequestParam(value = "xsqk") String xsqk,
                                               @RequestParam(value = "jgid") String jgid,
                                               @RequestParam(value = "sysType") int sysType) {

        Response<List<VisitPerson>> response = new Response<>();
        BizResponse<VisitPerson> bizResponse=new BizResponse<>();

        if (!TextUtils.isBlank(zyh_list)&&zyh_list.contains(",")){
            String[] zyh_Array=zyh_list.split(",");
            if (zyh_Array.length>0){
                for (String zyh : zyh_Array) {
                    if (!TextUtils.isBlank(zyh)){
                        bizResponse = service.setPatrol_Some(brbq, urid, zyh, xsqk, jgid);
                    }
                }
            }
        }
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;

        return response;
    }
    @RequestMapping(value = "auth/mobile/visit/get/getRoomPatientList")
    public
    @ResponseBody
    Response<VisitCount> getRoomPatientList(@RequestParam(value = "ksdm") String ksdm,
                                            @RequestParam(value = "fjhm") String fjhm,
                                            @RequestParam(value = "jgid") String jgid) {
        Response<VisitCount> response = new Response<>();
        BizResponse<VisitCount> bizResponse = new BizResponse<>();

        bizResponse = service.getRoomPatientList(ksdm,fjhm, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }
    /* =============================================================== end */

}
