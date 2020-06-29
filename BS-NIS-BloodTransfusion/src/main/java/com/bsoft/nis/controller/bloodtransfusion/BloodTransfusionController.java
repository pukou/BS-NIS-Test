package com.bsoft.nis.controller.bloodtransfusion;

import com.bsoft.nis.domain.bloodtransfusion.BloodRecieveSaveData;
import com.bsoft.nis.domain.bloodtransfusion.BloodReciveInfo;
import com.bsoft.nis.domain.bloodtransfusion.BloodTransfusionInfo;
import com.bsoft.nis.domain.bloodtransfusion.BloodTransfusionTourInfo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.bloodtransfusion.BloodTransfusionMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 输血模块控制器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Controller
public class BloodTransfusionController {

    @Autowired
    BloodTransfusionMainService service;

    @RequestMapping(value = "/bloodtransfusion")
    public String getMainPatientPage() {
        return "bloodtransfusion/bloodtransfusion";
    }

    /**
     * 获取输血计划列表
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param zyh   住院号
     * @param jgid  机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodtransfusion/get/getBloodTransfusionPlanList")
    public
    @ResponseBody
    Response<List<BloodTransfusionInfo>> getBloodTransfusionPlanList(@RequestParam(value = "start") String start,
                                                                     @RequestParam(value = "end") String end,
                                                                     @RequestParam(value = "zyh") String zyh,
                                                                     @RequestParam(value = "jgid") String jgid) {
        Response<List<BloodTransfusionInfo>> response = new Response<>();
        BizResponse<BloodTransfusionInfo> bizResponse = new BizResponse<>();

        bizResponse = service.getBloodTransfusionPlanList(start, end, zyh, jgid);
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
     * 输血医嘱执行
     *
     * @param xdh           血袋号
     * @param xdxh          血袋序号
     * @param zyh           住院号
     * @param hdgh          核对工号
     * @param zxgh          执行工号
     * @param operationType 操作类型 0 开始；3结束
     * @param jgid          机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodtransfusion/get/excueteBloodTransfusion")
    public
    @ResponseBody
    Response<List<BloodTransfusionInfo>> excueteBloodTransfusion(@RequestParam(value = "xdh") String xdh,
                                                                 @RequestParam(value = "xdxh") String xdxh,
                                                                 @RequestParam(value = "zyh") String zyh,
                                                                 @RequestParam(value = "hdgh") String hdgh,
                                                                 @RequestParam(value = "zxgh") String zxgh,
                                                                 @RequestParam(value = "operationType") String operationType,
                                                                 @RequestParam(value = "jgid") String jgid) {
        Response<List<BloodTransfusionInfo>> response = new Response<>();
        BizResponse<BloodTransfusionInfo> bizResponse = new BizResponse<>();

        bizResponse = service.excueteBloodTransfusion(xdh, xdxh, zyh, hdgh, zxgh, operationType, jgid);
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
     * 获取输血签收列表
     *
     * @param start  血袋号
     * @param end    血袋序号
     * @param bqid   住院号
     * @param status 核对工号
     *               0:未签收，1：已签收
     * @param jgid   机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodtransfusion/get/getBloodRecieveList")
    public
    @ResponseBody
    Response<List<BloodReciveInfo>> getBloodRecieveList(@RequestParam(value = "start") String start,
                                                        @RequestParam(value = "end") String end,
                                                        @RequestParam(value = "bqid") String bqid,
                                                        @RequestParam(value = "status") String status,
                                                        @RequestParam(value = "jgid") String jgid) {
        Response<List<BloodReciveInfo>> response = new Response<>();
        BizResponse<BloodReciveInfo> bizResponse = new BizResponse<>();

        bizResponse = service.getBloodRecieveList(start, end, bqid, status, jgid);
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
     * 取消签收
     *
     * @param xmid
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodtransfusion/get/devliyBloodRecieve")
    public
    @ResponseBody
    Response<String> devliyBloodRecieve(@RequestParam(value = "xmid") String xmid,
                                        @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.devliyBloodRecieve(xmid, jgid);
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
     * 签收
     *
     * @param bloodRecieveSaveData
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodtransfusion/post/saveBloodRecieve")
    public
    @ResponseBody
    Response<String> saveBloodRecieve(@RequestBody BloodRecieveSaveData bloodRecieveSaveData) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.saveBloodRecieve(bloodRecieveSaveData);
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
     * 获取输血巡视记录
     *
     * @param sxdh 输血单号
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodtransfusion/get/getBloodTransfusionTourInfoList")
    public
    @ResponseBody
    Response<List<BloodTransfusionTourInfo>> getBloodTransfusionTourInfoList(@RequestParam(value = "sxdh") String sxdh,
                                                                             @RequestParam(value = "jgid") String jgid) {
        Response<List<BloodTransfusionTourInfo>> response = new Response<>();
        BizResponse<BloodTransfusionTourInfo> bizResponse = new BizResponse<>();

        bizResponse = service.getBloodTransfusionTourInfoList(sxdh, jgid);
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
     * 保存输血巡视记录
     *
     * @param bloodTransfusionTourInfo
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodtransfusion/post/saveBloodTransfusionTourInfo")
    public
    @ResponseBody
    Response<String> saveBloodTransfusionTourInfo(@RequestBody BloodTransfusionTourInfo bloodTransfusionTourInfo) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.saveBloodTransfusionTourInfo(bloodTransfusionTourInfo);
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
     * 血袋上交
     *
     * @param sxdh 输血单号
     * @param yhid 用户id
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodtransfusion/get/saveBloodBagRecieve")
    public
    @ResponseBody
    Response<String> saveBloodBagRecieve(@RequestParam(value = "sxdh") String sxdh,
                                         @RequestParam(value = "yhid") String yhid,
                                         @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.saveBloodBagRecieve(sxdh, yhid, jgid);
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
